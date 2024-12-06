/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.tsm.process;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.*;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author ininoles
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class ProcessRequestOT extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private String P_DocAction;
	/*OT ID*/
	private int 			Record_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DocAction"))
				P_DocAction = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		//String newStatus = P_DocAction;
		String newAction = P_DocAction;
		X_MP_OT_Request ROT=new X_MP_OT_Request(Env.getCtx(), Record_ID,get_TrxName());

		//validacion de rol
		int cant = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_Document_Action_Access daa" +
				" INNER JOIN AD_Ref_List rl ON (daa.AD_Ref_List_ID = rl.AD_Ref_List_ID) " +
				" WHERE value = '"+newAction+"' AND AD_Role_ID = "+Env.getAD_Role_ID(getCtx())+
				" AND C_DocType_ID = " + ROT.get_ValueAsInt("C_DocType_ID"));
		//procesar
		if(cant > 0)
		{
			if(P_DocAction.equals("PR"))
			{
				//ahora se crearan los incidentes al procesar la primera vez y luego al completar se actualizaran
				//se crear registros de incidencias
				//generamos demas registros dependiendo de los dias
				//ininoles 15-01-2018 solo se generaran incidencias si es concepto mantención 
				if(ROT.get_ValueAsString("Concept").compareTo("13") == 0)
				{
					Calendar calCalendario = Calendar.getInstance();		
					Timestamp dateStart = ROT.getDateDoc();
					if((Timestamp)ROT.get_Value("DateEnd")!= null)
					{
						Timestamp dateEnd = (Timestamp)ROT.get_Value("DateEnd");
						long dif = dateEnd.getTime()-dateStart.getTime();
						dif = dif/(1000*60*60*24);
						calCalendario.setTimeInMillis(dateStart.getTime());				
						if(dif >= 0)
						{
							dif++;
							X_HR_Prebitacora prebitacoraSub = null;
							//obtenemos fecha en calendario			
					        //calCalendario.setTimeInMillis(prebitacora.getDateTrx().getTime());			
							while(dif > 0)
							{
								//ininoles antes de guardar nueva prebitacora(incidente) se valida que no sea sabado o domingo
								prebitacoraSub = new X_HR_Prebitacora(Env.getCtx(),0,null);
								MAsset asset = new MAsset(getCtx(), ROT.getA_Asset_ID(), get_TrxName());
								if(asset.get_ValueAsInt("AD_OrgRef_ID") > 0)
									prebitacoraSub.setAD_Org_ID(asset.get_ValueAsInt("AD_OrgRef_ID"));
								else
									prebitacoraSub.setAD_Org_ID(asset.getAD_Org_ID());
								prebitacoraSub.setColumnType("A");
								prebitacoraSub.setA_Asset_ID(ROT.getA_Asset_ID());
								//prebitacoraSub.setC_BPartner_ID(prebitacora.getC_BPartner_ID());				
								//prebitacoraSub.setWorkshift(prebitacora.getWorkshift());
								prebitacoraSub.setHR_Concept_TSM_ID(1000011);
								prebitacoraSub.setProcessed(false);
								prebitacoraSub.setIsActive(true);				
								prebitacoraSub.setDateTrx(new Timestamp(calCalendario.getTimeInMillis()));	
								prebitacoraSub.set_CustomColumn("MP_OT_Request_ID", ROT.get_ID());
								prebitacoraSub.saveEx();
								//calculamos nueva fecha sumandole 1 dia
								calCalendario.add(Calendar.DATE,1);
								dif--;
							}
						}				
					}
				}
				ROT.setProcessed(true);			
				ROT.setDocStatus("WC");
				ROT.save();
				return "Confirmado";
			}
			else if(P_DocAction.equals("CO"))
			{
				ROT.setProcessed(true);			
				ROT.setDocStatus("CO");
				ROT.save();
				//se borran relaciones a tablas de disponibilidad
				PreparedStatement pstmtDRe1 = null;				
				String sqlDelRe1 = "UPDATE Pre_M_MovementLine SET HR_Concept_TSM_ID = null, HR_Prebitacora_ID = null " +
						" WHERE HR_Prebitacora_ID IN " +
						" (SELECT HR_Prebitacora_ID FROM HR_Prebitacora WHERE MP_OT_Request_ID = "+ROT.get_ID()+
						" AND (DateTrx < ? OR DateTrx > ?))";
				pstmtDRe1 = DB.prepareStatement(sqlDelRe1, get_TrxName());
				pstmtDRe1.setTimestamp(1, ROT.getDateDoc());
				pstmtDRe1.setTimestamp(2, (Timestamp)ROT.get_Value("DateEnd"));
				pstmtDRe1.executeUpdate();
				//se borran incidentes fuera de los nuevos rangos de la solicitud de OT		
				PreparedStatement pstmtD1 = null;				
				String sqlDel1 = "DELETE FROM HR_Prebitacora WHERE MP_OT_Request_ID = "+ROT.get_ID()+
				 " AND (DateTrx < ? OR DateTrx > ?)";
				pstmtD1 = DB.prepareStatement(sqlDel1, get_TrxName());
				pstmtD1.setTimestamp(1, ROT.getDateDoc());
				pstmtD1.setTimestamp(2, (Timestamp)ROT.get_Value("DateEnd"));
				pstmtD1.executeUpdate();
				//fin borrado
				
				//se crear nuevos registros de incidencias
				//generamos demas registros dependiendo de los dias
				//ininoles 15-01-2018 solo se generaran registros si concepto es mantenimiento
				if(ROT.get_ValueAsString("Concept").compareTo("13") == 0)
				{
					Calendar calCalendario = Calendar.getInstance();		
					Timestamp dateStart = ROT.getDateDoc();
					if((Timestamp)ROT.get_Value("DateEnd")!= null)
					{
						Timestamp dateEnd = (Timestamp)ROT.get_Value("DateEnd");
						long dif = dateEnd.getTime()-dateStart.getTime();
						dif = dif/(1000*60*60*24);
						calCalendario.setTimeInMillis(dateStart.getTime());				
						if(dif >= 0)
						{
							dif++;
							X_HR_Prebitacora prebitacoraSub = null;
							//obtenemos fecha en calendario			
					        //calCalendario.setTimeInMillis(prebitacora.getDateTrx().getTime());			
							while(dif > 0)
							{
								//ininoles antes de guardar nueva prebitacora(incidente) se valida que no sea sabado o domingo
								//antes de generar los incidentes se valida que no exista previamente
								int exist = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM HR_Prebitacora " +
										" WHERE MP_OT_Request_ID="+ROT.get_ID()+" AND DateTrx = ?",new Timestamp(calCalendario.getTimeInMillis()));
								//si no existe se crea  registro
								if(exist < 1)
								{
									prebitacoraSub = new X_HR_Prebitacora(Env.getCtx(),0,null);
									MAsset asset = new MAsset(getCtx(), ROT.getA_Asset_ID(), get_TrxName());
									if(asset.get_ValueAsInt("AD_OrgRef_ID") > 0)
										prebitacoraSub.setAD_Org_ID(asset.get_ValueAsInt("AD_OrgRef_ID"));
									else
										prebitacoraSub.setAD_Org_ID(asset.getAD_Org_ID());
									prebitacoraSub.setColumnType("A");
									prebitacoraSub.setA_Asset_ID(ROT.getA_Asset_ID());
									//prebitacoraSub.setC_BPartner_ID(prebitacora.getC_BPartner_ID());				
									//prebitacoraSub.setWorkshift(prebitacora.getWorkshift());
									prebitacoraSub.setHR_Concept_TSM_ID(1000011);
									prebitacoraSub.setProcessed(false);
									prebitacoraSub.setIsActive(true);				
									prebitacoraSub.setDateTrx(new Timestamp(calCalendario.getTimeInMillis()));	
									prebitacoraSub.set_CustomColumn("MP_OT_Request_ID", ROT.get_ID());
									prebitacoraSub.saveEx();
								}
								//calculamos nueva fecha sumandole 1 dia
								calCalendario.add(Calendar.DATE,1);
								dif--;
							}
						}				
					}
				}
				//
				return "Confirmado";
			}
			else if(P_DocAction.equals("RA") && ROT.isProcessed() && ROT.getDocStatus().equals("WC"))
			{
				ROT.setProcessed(false);
				ROT.save();
				//se borran registros de incidencias
				DB.executeUpdate("DELETE FROM HR_Prebitacora WHERE MP_OT_Request_ID IS NOT NULL" +
						" AND MP_OT_Request_ID = "+ROT.get_ID(), get_TrxName());
				//
				return "Reactivado para Edicion";
			}
			else if(P_DocAction.equals("AN"))
			{
				ROT.setProcessed(true);
				ROT.setDocStatus("VO");
				ROT.save();
				//se borran registros de incidencias
				DB.executeUpdate("DELETE FROM HR_Prebitacora WHERE MP_OT_Request_ID IS NOT NULL" +
						" AND MP_OT_Request_ID = "+ROT.get_ID(), get_TrxName());
				//
				return "Anulado";
			}
			else if(P_DocAction.equals("CL"))
			{
				ROT.setProcessed(true);
				ROT.setDocStatus("CL");
				ROT.save();
				return "Cerrado";
			}
			return "No es posible Cumplir la Accion ";
		}
		else
			throw new AdempiereException("Error: Permisos de rol insuficientes");
	}	//	doIt
}	//	CopyOrder
