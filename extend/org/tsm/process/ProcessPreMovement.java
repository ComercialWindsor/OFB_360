/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Contributor(s): Chris Farley - northernbrewer                              *
 *****************************************************************************/
package org.tsm.process;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAsset;
import org.compiere.model.MBPartner;
import org.compiere.model.X_HR_Prebitacora;
import org.compiere.model.X_Pre_M_Movement;
import org.compiere.model.X_Pre_M_MovementLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	CopyFromJobStandar
 *	
 */
public class ProcessPreMovement extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_Order_ID;
	private String p_Action;
		
	protected void prepare()
	{	
		p_Order_ID = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("DocAction"))
			{
				p_Action = (String)para[i].getParameter();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		if (p_Action == null)
			p_Action = "CO";
		X_Pre_M_Movement preMov = new X_Pre_M_Movement(getCtx(), p_Order_ID, get_TrxName());		
		//procesamos por primera vez para traer lineas
		if(preMov.getDocStatus().compareTo("DR") == 0 && p_Action.compareTo("CO") == 0)
		{
			int cant = 0;			
			/*String sql = "SELECT A_Asset_ID,A_AssetRampla_ID, C_BpartnerRef_ID FROM A_Asset " +
			//" WHERE C_BpartnerRef_ID IS NOT NULL AND AD_Org_ID = "+preMov.getAD_Org_ID()+
			//debe traer tenga o no conductor
			//" WHERE C_BpartnerRef_ID IS NOT NULL AND AD_OrgRef_ID = "+preMov.getAD_Org_ID();
			" WHERE A_Asset_ID NOT IN (SELECT A_AssetRampla_ID FROM A_Asset WHERE IsActive = 'Y') " +
			" AND AD_OrgRef_ID = "+preMov.getAD_Org_ID();
			//" AND Workshift = '"+preMov.get_ValueAsString("Workshift")+"' ";
			//se agrega cadena para dia
			/*if(preMov.getMovementDate().getDay() == 0)
				sql = sql + " AND DaySunday = 'Y'";
			else
				sql = sql + " AND DayMS = 'Y'";
			*/
			//ininoles nuevo sql de activos y choferes
			String sql = " SELECT A_Asset_ID,A_AssetRampla_ID, C_BpartnerRef_ID FROM A_Asset" +
					" WHERE IsActive = 'Y' AND A_Asset_ID NOT IN (SELECT A_AssetRampla_ID FROM A_Asset WHERE IsActive = 'Y' AND A_AssetRampla_ID IS NOT NULL)" +
					" AND AD_OrgRef_ID = "+preMov.getAD_Org_ID() + " AND A_Asset_Type_ID IN ('01','02')"+
					" UNION" +
					" SELECT null,null,C_Bpartner_ID FROM C_Bpartner" +
					" WHERE IsActive = 'Y' AND IsUnlinked <> 'Y' AND AD_OrgRef_ID = "+preMov.getAD_Org_ID() +
					" AND C_Bpartner_ID NOT IN (SELECT C_BpartnerRef_ID FROM A_Asset WHERE IsActive = 'Y' AND C_BpartnerRef_ID IS NOT NULL)";
			
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				ResultSet rs = pstmt.executeQuery ();			
				
				while (rs.next())
				{
					//buscamos si posee rampla
					/*int ID_Rampla = DB.getSQLValue(get_TrxName(), "SELECT MAX(am.A_Asset_ID) FROM MP_AssetMeter_Log aml" +
							" INNER JOIN MP_AssetMeter am ON (aml.MP_AssetMeter_ID = am.MP_AssetMeter_ID)" +
							" INNER JOIN MP_Meter me ON (me.MP_Meter_ID = am.MP_Meter_ID) WHERE aml.IsActive = 'Y' AND am.IsActive = 'Y'" +
							" AND upper(me.name) like 'PAREO' AND aml.A_AssetRef_ID = "+rs.getInt("A_Asset_ID"));
					*/
					//ininoles ahora la rampla sera solo un campo en el activo
					int ID_Rampla = rs.getInt("A_AssetRampla_ID");
					
					//generamos linea de pre hoja de ruta
					X_Pre_M_MovementLine pMovLine = new X_Pre_M_MovementLine(getCtx(), 0, get_TrxName());
					pMovLine.setPre_M_Movement_ID(preMov.get_ID());
					pMovLine.setAD_Org_ID(preMov.getAD_Org_ID());
					if(rs.getInt("A_Asset_ID") > 0)
						pMovLine.setA_Asset_ID(rs.getInt("A_Asset_ID"));
					if(rs.getInt("C_BpartnerRef_ID") > 0)
						pMovLine.setC_BPartner_ID(rs.getInt("C_BpartnerRef_ID"));
					if(ID_Rampla > 0)
						pMovLine.setA_AssetRef_ID(ID_Rampla);
					//antes de guardar se revisa si posee incidentes
					int ID_preBitacora = 0;
					if(rs.getInt("A_Asset_ID") > 0)
					{
						ID_preBitacora = DB.getSQLValue(get_TrxName(), "SELECT MAX(HR_Prebitacora_ID) FROM HR_Prebitacora  WHERE A_Asset_ID = "+rs.getInt("A_Asset_ID")+"  AND DateTrx = ?",preMov.getMovementDate());
						if(ID_preBitacora > 0)
						{
							X_HR_Prebitacora preBitacora = new X_HR_Prebitacora(getCtx(), ID_preBitacora, get_TrxName());
							pMovLine.set_CustomColumn("HR_Prebitacora_ID", ID_preBitacora);
							pMovLine.set_CustomColumn("HR_Concept_TSM_ID", preBitacora.getHR_Concept_TSM_ID());
						}
					}
					if(rs.getInt("C_BpartnerRef_ID") > 0)
					{
						ID_preBitacora = DB.getSQLValue(get_TrxName(), "SELECT MAX(HR_Prebitacora_ID) FROM HR_Prebitacora  WHERE C_BPartner_ID = "+rs.getInt("C_BpartnerRef_ID")+"  AND DateTrx = ?",preMov.getMovementDate());
						if(ID_preBitacora > 0)
						{
							X_HR_Prebitacora preBitacora = new X_HR_Prebitacora(getCtx(), ID_preBitacora, get_TrxName());
							pMovLine.set_CustomColumn("HR_PrebitacoraRef_ID", ID_preBitacora);
							pMovLine.set_CustomColumn("HR_Concept_TSMBP_ID", preBitacora.getHR_Concept_TSM_ID());
						}
					}
					/*if(ID_preBitacora > 0)
						pMovLine.set_CustomColumn("", ID_preBitacora);*/
					pMovLine.save();
					cant++;
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			if(cant > 0)
			{
				preMov.setDocStatus("IP");
				preMov.save();
			}
			return "Se han agregado "+cant+" lineas a la disponibilidad";
		}
		//validamos para completar
		else if(p_Action.compareTo("CO") == 0 && 
				(preMov.getDocStatus().compareTo("IP") == 0 || preMov.getDocStatus().compareTo("WC") ==0))
		{
			boolean overWrite = false;
			overWrite = preMov.get_ValueAsBoolean("OverWrite");
			//oberwrite false hacemos validaciones
			if(!overWrite)
			{
				//validacion cantidad de lineas
				/*
				int cantLine = DB.getSQLValue(get_TrxName(),"SELECT COUNT(1) FROM Pre_M_MovementLine " +
						"WHERE IsActive = 'Y' AND Pre_M_Movement_ID = "+preMov.get_ID());
				if(cantLine < 0)
					cantLine = 0;
				MOrg org = new MOrg(getCtx(), preMov.getAD_Org_ID(), get_TrxName());
				int qtyAsset = 0;
				//String Workshift =  preMov.get_ValueAsString("Workshift");
				if(preMov.getMovementDate().getDay() == 0 && preMov.get_ValueAsString("Workshift").compareTo("30") == 0)
					qtyAsset = org.get_ValueAsInt("qty01S");
				else if(preMov.getMovementDate().getDay() == 0 && preMov.get_ValueAsString("Workshift").compareTo("60") == 0)
					qtyAsset = org.get_ValueAsInt("qty02S");
				else if(preMov.getMovementDate().getDay() == 0 && preMov.get_ValueAsString("Workshift").compareTo("90") == 0)
					qtyAsset = org.get_ValueAsInt("qty03S");
				else if(preMov.getMovementDate().getDay() != 0 && preMov.get_ValueAsString("Workshift").compareTo("30") == 0)
					qtyAsset = org.get_ValueAsInt("qty01MS");
				else if(preMov.getMovementDate().getDay() != 0 && preMov.get_ValueAsString("Workshift").compareTo("60") == 0)
					qtyAsset = org.get_ValueAsInt("qty02MS");
				else if(preMov.getMovementDate().getDay() != 0 && preMov.get_ValueAsString("Workshift").compareTo("90") == 0)
					qtyAsset = org.get_ValueAsInt("qty03MS");
				else
					qtyAsset = 0;				
				if(cantLine != qtyAsset)
					throw new AdempiereException("Cantidad de Lineas Distinta a Cantidad de Activos");
				 */
				
				//validacion sera contra el total de activos/bp analizarla bien al terminar todo
				int cantBP = DB.getSQLValue(get_TrxName(),"SELECT COUNT(1) FROM C_Bpartner " +						
						" WHERE IsActive = 'Y' AND AD_OrgRef_ID = "+preMov.getAD_Org_ID()+" AND IsUnlinked = 'N' "+
						" AND C_BPartner_ID NOT IN (SELECT C_BPartner_ID FROM Pre_M_MovementLine WHERE C_BPartner_ID IS NOT NULL " +
						" AND Pre_M_Movement_ID ="+preMov.get_ID()+")");
				if(cantBP > 0)
					throw new AdempiereException("ERROR: No estan en la disponibilidad "+cantBP+" conductores");
				
				int cantAset = DB.getSQLValue(get_TrxName(),"SELECT COUNT(1) FROM A_Asset " +
						" WHERE IsActive = 'Y' AND A_Asset_Type_ID IN ('01','02') AND AD_OrgRef_ID = "+preMov.getAD_Org_ID()+
						" AND A_Asset_ID NOT IN (SELECT A_Asset_ID FROM Pre_M_MovementLine WHERE A_Asset_ID IS NOT NULL " +
						" AND Pre_M_Movement_ID ="+preMov.get_ID()+") " +
						" AND A_Asset_ID NOT IN (SELECT A_AssetRef_ID FROM Pre_M_MovementLine WHERE A_Asset_ID IS NOT NULL " +
						" AND Pre_M_Movement_ID ="+preMov.get_ID()+")");
				if(cantAset > 0)
					throw new AdempiereException("ERROR: No estan en la disponibilidad "+cantAset+" activos");
				//end
				/*int cantLine = DB.getSQLValue(get_TrxName(),"SELECT COUNT(1) FROM Pre_M_MovementLine " +
						"WHERE IsActive = 'Y' AND A_Asset_ID > 0 AND Pre_M_Movement_ID = "+preMov.get_ID());
				int qtyAsset = 0;				
				String sqlValQty = "SELECT COUNT(1) FROM A_Asset WHERE IsActive = 'Y' " +
						"AND AD_OrgRef_ID = "+preMov.getAD_Org_ID();						
				qtyAsset = DB.getSQLValue(get_TrxName(),sqlValQty);				
				if(cantLine < 0)
					cantLine = 0;
				if(cantLine != qtyAsset)
					throw new AdempiereException("Cantidad de Lineas Distinta a Cantidad de Activos");
				*/
				//validacion misma disponibilidad
				int cantPMov = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM Pre_M_Movement " +
						" WHERE Pre_M_Movement_ID <> "+preMov.get_ID()+" AND MovementDate = ? AND AD_Org_ID="+preMov.getAD_Org_ID()+
						" AND Workshift='"+preMov.get_ValueAsString("Workshift")+"'",preMov.getMovementDate());
				if(cantPMov > 0)
					throw new AdempiereException("ERROR: Ya existe una disponibilidad para el mismo dia, turno y flota");
				//validacion no incidencias
				int cantIndBP = DB.getSQLValue(get_TrxName(),"SELECT COUNT(1) FROM RVOFB_Pre_M_MovementLine" +
						" WHERE Pre_M_Movement_ID="+preMov.get_ID()+" AND (A_Asset_ID > 0 OR C_Bpartner_ID > 0) AND IsActive = 'Y'");
				if(cantIndBP > 0)
					throw new AdempiereException("ERROR: Hay incidencias en la disponibilidad");
				//validacion fecha
				//disponibilidad no puede ser mayor a 7 dias
				Calendar calCalendarioHead = Calendar.getInstance();
				calCalendarioHead.add(Calendar.DATE, 7);		        
		        if(preMov.getMovementDate().compareTo(new Timestamp(calCalendarioHead.getTimeInMillis())) > 0)
		        	throw new AdempiereException("Error: Fecha supera 6 dias");
		        //end
				//validaciones socio de linea
				String sqlLine = "SELECT Pre_M_MovementLine_ID FROM Pre_M_MovementLine " +
				" WHERE Pre_M_Movement_ID = "+preMov.get_ID();
		
				PreparedStatement pstmtLine = null;				
				pstmtLine = DB.prepareStatement (sqlLine, get_TrxName());
				ResultSet rsLine = pstmtLine.executeQuery ();								
				while (rsLine.next())
				{	
					X_Pre_M_MovementLine pMovLine = new X_Pre_M_MovementLine(getCtx(), rsLine.getInt("Pre_M_MovementLine_ID"), get_TrxName());					
					//validamos solo si la linea esta activa
					if(pMovLine.isActive())
					{
						//validacion que no se repita la linea de activo
						if(pMovLine.getA_Asset_ID() > 0)
						{
							int cantPMovLine = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM Pre_M_Movement mm" +
									" INNER JOIN Pre_M_MovementLine mml ON (mm.Pre_M_Movement_ID = mml.Pre_M_Movement_ID) " +
									" WHERE mm.Pre_M_Movement_ID <> "+preMov.get_ID()+"AND mm.MovementDate = ? " +
									" AND mm.AD_Org_ID="+preMov.getAD_Org_ID()+" AND mm.Workshift='"+preMov.get_ValueAsString("Workshift")+"'"+
									" AND mml.A_Asset_ID = "+pMovLine.getA_Asset_ID(),preMov.getMovementDate());
							if(cantPMovLine > 0)
								throw new AdempiereException("ERROR: Ya existe una disponibilidad para el mismo activo:"+pMovLine.getA_Asset().getValue());
						}
						//validacion que no se repita la linea de socio de negocio
						if(pMovLine.getC_BPartner_ID() > 0)
						{
							int cantPMovLine = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM Pre_M_Movement mm" +
									" INNER JOIN Pre_M_MovementLine mml ON (mm.Pre_M_Movement_ID = mml.Pre_M_Movement_ID) " +
									" WHERE mm.Pre_M_Movement_ID <> "+preMov.get_ID()+" AND mm.MovementDate = ? " +
									" AND mm.AD_Org_ID="+preMov.getAD_Org_ID()+" AND mm.Workshift='"+preMov.get_ValueAsString("Workshift")+"'"+
									" AND mml.C_Bpartner_ID = "+pMovLine.getC_BPartner_ID(),preMov.getMovementDate());
							if(cantPMovLine > 0)
								throw new AdempiereException("ERROR: Ya existe una disponibilidad para el mismo conductor:"+pMovLine.getC_BPartner().getValue());
						}
						//validaciones de socio de negocio
						//String turno = pMovLine.get_ValueAsString("Workshift");
						/*String turno = preMov.get_ValueAsString("Workshift");
						if(turno == null)
							turno = "30";*/
						/*if(pMovLine.getC_BPartner_ID() > 0)
						{*/
							//validacion 6 dias seguidos trabajados		
							//Timestamp dateTo = preMov.getMovementDate();
							//calculamos 6 dias atras
							/*Calendar calCalendario = Calendar.getInstance();
					        calCalendario.setTimeInMillis(dateTo.getTime());
					        int cantDaysAgo = 6;
					        if(turno.compareTo("30")==0)
					        	cantDaysAgo = 6;
					        else
					        	cantDaysAgo = 5;					        
					        calCalendario.add(Calendar.DATE, -cantDaysAgo);
							Timestamp dateFrom = new Timestamp(calCalendario.getTimeInMillis());
							
							int cantDays = DB.getSQLValue(get_TrxName(), 
									" SELECT COUNT(pm.Pre_M_Movement_ID) FROM Pre_M_MovementLine pml " +
									" INNER JOIN Pre_M_Movement pm ON (pml.Pre_M_Movement_ID = pm.Pre_M_Movement_ID)" +
									" WHERE pml.IsActive = 'Y' AND pm.DocStatus IN ('CO') AND pml.C_Bpartner_ID = "+pMovLine.getC_BPartner_ID()+
									" AND pm.movementdate BETWEEN ? AND ? ", dateFrom,dateTo);
							if(cantDays > cantDaysAgo)
							{
								throw new AdempiereException("Mas de "+cantDaysAgo+" dias Trabajados. Conductor: "+pMovLine.getC_BPartner().getName());
							}*/
							
							//validación  
							/*int cantSunday = DB.getSQLValue(get_TrxName(), "SELECT COUNT(DISTINCT(pm.Pre_M_Movement_ID))" +
									" FROM Pre_M_MovementLine pml " +
									" INNER JOIN Pre_M_Movement pm ON (pml.Pre_M_Movement_ID = pm.Pre_M_Movement_ID)" +
									" WHERE pml.IsActive = 'Y' AND pm.DocStatus IN ('CO') AND to_char(movementdate, 'd') = '1'");
							
							if(cantSunday > 2)
								throw new AdempiereException("Mas de 2 domingos Trabajados. Conductor: "+pMovLine.getC_BPartner().getName());
							*/
						//}
						if(pMovLine.getC_BPartner_ID() > 0)
						{
							int cantPMovLine = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM Pre_M_Movement mm" +
									" INNER JOIN Pre_M_MovementLine mml ON (mm.Pre_M_Movement_ID = mml.Pre_M_Movement_ID) " +
									" WHERE mm.Pre_M_Movement_ID <> "+preMov.get_ID()+" AND mm.MovementDate = ? " +
									" AND mm.AD_Org_ID <> "+preMov.getAD_Org_ID()+
									" AND mml.C_Bpartner_ID = "+pMovLine.getC_BPartner_ID(),preMov.getMovementDate());
							if(cantPMovLine > 0)
								throw new AdempiereException("ERROR: Ya existe una disponibilidad en otra flota para el mismo conductor:"+pMovLine.getC_BPartner().getValue());
						}
						
					}
					//actualizacion de org actual
					if(pMovLine.getA_Asset_ID() > 0)
					{
						MAsset asset = new MAsset(getCtx(), pMovLine.getA_Asset_ID(), get_TrxName());						
						pMovLine.set_CustomColumn("AD_OrgRefA_ID",asset.get_ValueAsInt("AD_OrgRef_ID"));
						pMovLine.save(get_TrxName());
					}
					if(pMovLine.getC_BPartner_ID() > 0)
					{
						MBPartner partner = new MBPartner(getCtx(), pMovLine.getC_BPartner_ID(), get_TrxName());						
						pMovLine.set_CustomColumn("AD_OrgRefB_ID",partner.get_ValueAsInt("AD_OrgRef_ID"));
						pMovLine.save(get_TrxName());
					}
				}
				
		        if(preMov.getDocStatus().compareTo("IP") == 0) 
		        {
					preMov.setDocStatus("WC");
					preMov.save();
		        }else if(preMov.getDocStatus().compareTo("WC") == 0) 
		        {
		        	preMov.setDocStatus("CO");
					preMov.setProcessed(true);
					preMov.save();
					DB.executeUpdate("UPDATE Pre_M_MovementLine SET Processed = 'Y' " +
						" WHERE Pre_M_Movement_ID = "+preMov.get_ID(), get_TrxName());
					//se actualiza 
		        }
				
			}
			else	// sin validaciones
			{
				if(preMov.getDocStatus().compareTo("IP") == 0) 
		        {
					preMov.setDocStatus("WC");
					preMov.save();
		        }else if(preMov.getDocStatus().compareTo("WC") == 0) 
		        {
		        	preMov.setDocStatus("CO");
					preMov.setProcessed(true);
					preMov.save();
					DB.executeUpdate("UPDATE Pre_M_MovementLine SET Processed = 'Y' " +
						" WHERE Pre_M_Movement_ID = "+preMov.get_ID(), get_TrxName());
		        }
			}			
			return "Procesado";			
		}
		else if (p_Action.compareTo("VO") == 0)
		{
			preMov.setDocStatus("VO");
			preMov.setProcessed(true);
			preMov.save();
			return "Anulado";
		}
		else
			return "Procesado";		
	}	//	doIt
}	//	Replenish
