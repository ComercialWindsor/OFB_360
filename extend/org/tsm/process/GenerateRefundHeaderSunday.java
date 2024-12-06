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

import org.compiere.model.MBPartner;
import org.compiere.model.MOrg;
import org.compiere.model.X_TP_Refund;
import org.compiere.model.X_TP_RefundAmt;
import org.compiere.model.X_TP_RefundHeader;
import org.compiere.model.X_TP_RefundLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Replenishment Report
 *	
 *  @author Jorg Janke
 *  @version $Id: ReplenishReport.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 *  
 *  Carlos Ruiz globalqss - integrate bug fixing from Chris Farley
 *    [ 1619517 ] Replenish report fails when no records in m_storage
 */
public class GenerateRefundHeaderSunday extends SvrProcess
{	
	/** Warehouse				*/
	private Timestamp	p_DateTrxFrom;
	private Timestamp	p_DateTrxTo;
	private int	p_Org_ID;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("DateTrx"))
			{
				p_DateTrxFrom = (Timestamp)para[i].getParameter();
				p_DateTrxTo = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("AD_Org_ID"))
			{
				p_Org_ID = para[i].getParameterAsInt();
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
		//Ingreso de viaticos
		int cantLine = 0;
		int canthead = 0;
		
		//agregamos lineas de viaticos desde hojas de rura
		String sqlHR = "SELECT pm.M_Movement_ID,pm.C_BPartner_ID,pm.movementdate,pm.AD_Org_ID," +
				" EXTRACT(epoch FROM " +
				" ((select (to_char(MAX(mml.tp_inicialhr), 'YYYY-MM-DD'))::timestamp from m_movementline mml where mml.m_movement_id =  1458510) -" +
				" (select (to_char(MIN(mml.tp_finalhr), 'YYYY-MM-DD'))::timestamp from m_movementline mml where mml.m_movement_id = 1458510)))/(3600*24)+ 1 " +
				" as cantDays , pm.created,tieneferiado(pm.M_Movement_ID) as feriado,tienedomingo(pm.M_Movement_ID) as domingo "  +
				" FROM M_Movement pm " +
				" WHERE pm.AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+" AND (tienedomingo(pm.M_Movement_ID) = 1 "+
				//ininoles se agrega ademas dias feriados
				" OR tieneferiado(pm.M_Movement_ID) > 0 )"+
				" AND " +
				" (((select MIN(mml.tp_finalhr) from m_movementline mml where mml.m_movement_id = pm.M_Movement_ID)  > ? " +
				" AND" +
				" (select MIN(mml.tp_finalhr) from m_movementline mml where mml.m_movement_id = pm.M_Movement_ID)  < ?)" +
				" OR " +
				" (select MAX(mml.tp_inicialhr) from m_movementline mml where mml.m_movement_id =  pm.M_Movement_ID) < ? " +
				" AND" +
				" (select MAX(mml.tp_inicialhr) from m_movementline mml where mml.m_movement_id =  pm.M_Movement_ID) > ?) ";
		if(p_Org_ID > 0)
		{
			sqlHR = sqlHR + " AND pm.AD_Org_ID = "+p_Org_ID+" AND pm.DocStatus IN ('CO','CL','DR')";
		}
		//codigo de disponibilidades
		sqlHR = sqlHR + " ORDER BY C_BPartner_ID,movementdate,m_movement_id asc,created";
		log.config("sql:"+sqlHR);
		PreparedStatement pstmtHR = null;
		ResultSet rsHR = null;
		try
		{
			//seteamos variable to
			p_DateTrxTo.setHours(23);
			p_DateTrxTo.setMinutes(59);
			p_DateTrxTo.setSeconds(59);
			pstmtHR = DB.prepareStatement (sqlHR, get_TrxName());
			pstmtHR.setTimestamp(1, p_DateTrxFrom);
			pstmtHR.setTimestamp(2, p_DateTrxTo);
			pstmtHR.setTimestamp(3, p_DateTrxTo);
			pstmtHR.setTimestamp(4, p_DateTrxFrom);
			
			rsHR = pstmtHR.executeQuery();		
			int ID_BPartner = 0;
			int ID_Org = 0;			
			int id_RefundAmt = 0;
			int id_RefundAmtF = 0;
			if(p_Org_ID > 0)
			{
				id_RefundAmt = DB.getSQLValue(get_TrxName(), "SELECT MAX(TP_RefundAmt_ID) FROM TP_RefundAmt " +
						" WHERE value like 'DT' AND AD_Org_ID = "+p_Org_ID);
				id_RefundAmtF = DB.getSQLValue(get_TrxName(), "SELECT MAX(TP_RefundAmt_ID) FROM TP_RefundAmt " +
						" WHERE value like 'F' AND AD_Org_ID = "+p_Org_ID);
			}
			MOrg org = null;
			
			//se genera viatico
			X_TP_RefundHeader headViatico = new X_TP_RefundHeader(getCtx(), 0, get_TrxName());
			headViatico.setAD_Org_ID(p_Org_ID);
			headViatico.setDocumentDate(p_DateTrxFrom);
			headViatico.setDateEnd(p_DateTrxTo);
			headViatico.setDescription("Generado Automaticamente viatico domingos y festivos");
			headViatico.setDocStatus("DR");
			headViatico.setType("02");
			headViatico.save();
			//int ID_refundLineValid = 0;
			while (rsHR.next())
			{
				if(rsHR.getInt("M_Movement_ID") == 2077664
						|| rsHR.getInt("M_Movement_ID") == 2077636)
				{
					log.config("stop para debug");
				}
				
				ID_BPartner = rsHR.getInt("C_BPartner_ID");
				ID_Org = rsHR.getInt("AD_Org_ID");
				if(p_Org_ID != rsHR.getInt("AD_Org_ID"))
				{
					id_RefundAmt = DB.getSQLValue(get_TrxName(), "SELECT MAX(TP_RefundAmt_ID) FROM TP_RefundAmt " +
							" WHERE value like 'DT' AND AD_Org_ID = "+rsHR.getInt("AD_Org_ID"));
					id_RefundAmtF = DB.getSQLValue(get_TrxName(), "SELECT MAX(TP_RefundAmt_ID) FROM TP_RefundAmt " +
							" WHERE value like 'F' AND AD_Org_ID = "+rsHR.getInt("AD_Org_ID"));
				}
				/*else
				{
					id_RefundAmt = DB.getSQLValue(get_TrxName(), "SELECT MAX(TP_RefundAmt_ID) FROM TP_RefundAmt " +
							" WHERE value like 'DT' AND AD_Org_ID = "+p_Org_ID);
					id_RefundAmtF = DB.getSQLValue(get_TrxName(), "SELECT MAX(TP_RefundAmt_ID) FROM TP_RefundAmt " +
							" WHERE value like 'F' AND AD_Org_ID = "+p_Org_ID);
				}*/
				if(id_RefundAmt < 1)
				{
					id_RefundAmt = DB.getSQLValue(get_TrxName(), "SELECT MAX(TP_RefundAmt_ID) FROM TP_RefundAmt " +
							" WHERE value like 'DT' ");
				}
				
				//generamos viatico para BP
				if(ID_BPartner > 0 && ID_Org > 0)
				{
					//creamos org para sacar monto
					org = new MOrg(getCtx(), ID_Org, get_TrxName());
					Boolean IsMultyDays = org.get_ValueAsBoolean("IsMultipleDays");
					MBPartner bPartner = new MBPartner(getCtx(), ID_BPartner, get_TrxName());					
					int ID_Viatico = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(TP_Refund_ID),0)" +
							" FROM TP_Refund WHERE DocStatus NOT IN ('CO','VO') AND Type='02' " +
							" AND C_BPartner_ID = "+ID_BPartner+" AND AD_Org_ID = "+ID_Org+
							" AND TP_RefundHeader_ID = "+headViatico.get_ID());
					if(ID_Viatico > 0)//existe viatico abierto para conductor en viatico header
					{
						//int flag = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM c_nonbusinessday WHERE date1 = ?",rsHR.getTimestamp("MovementDate"));
						//int flag = DB.getSQLValue(get_TrxName(), "SELECT tieneferiado(mm.M_Movement_ID) FROM M_Movement mm WHERE M_Movement_ID = ",rsHR.getInt("M_Movement_ID"));
						//se valida si es dia domingo 
						if(rsHR.getTimestamp("MovementDate").getDay() == 0 || rsHR.getInt("feriado") > 0)
						{
							//antes de generar la linea de viatico normal, hay que revisar que no exista otra linea con los mismos datos
							int cantV = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
									" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
									" WHERE r.Type = '02' AND r.C_BPartner_ID = "+ID_BPartner+" " +
									" AND TP_RefundAmt_ID IN ("+id_RefundAmt+","+id_RefundAmtF+") AND rl.DateTrx = ?",rsHR.getTimestamp("MovementDate"));
							if(cantV < 1 && rsHR.getTimestamp("MovementDate").compareTo(p_DateTrxTo) <= 0
									&& rsHR.getTimestamp("MovementDate").compareTo(p_DateTrxFrom) >= 0)
							{
								//generamos la linea del viatico
								X_TP_RefundLine line = new X_TP_RefundLine(getCtx(), 0, get_TrxName());								
								line.setTP_Refund_ID(ID_Viatico);
								line.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));							
								line.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
								line.setDateTrx(rsHR.getTimestamp("MovementDate"));						
								if(id_RefundAmt > 0 || id_RefundAmtF > 0 )
								{
									X_TP_RefundAmt rAmt = null;
									if(rsHR.getTimestamp("MovementDate").getDay() == 0)
										rAmt = new X_TP_RefundAmt(getCtx(), id_RefundAmt, get_TrxName());
									else
										rAmt = new X_TP_RefundAmt(getCtx(), id_RefundAmtF, get_TrxName());
									line.set_CustomColumn("TP_RefundAmt_ID",rAmt.get_ID());
									line.setAmt(rAmt.getAmt());
								}
								else
									line.setAmt(Env.ZERO);
								line.save();
								cantLine++;
							}
						}
						if(IsMultyDays)
						{	
							if(rsHR.getInt("cantDays") > 0)
							{
								Calendar calInicial = Calendar.getInstance();		
								calInicial.setTimeInMillis(rsHR.getTimestamp("MovementDate").getTime());
								for(int a=1;a < rsHR.getInt("cantDays");a++)
								{	
									calInicial.add(Calendar.DATE, 1);
									//antes de generar la linea de viatico normal, hay que revisar que no exista otra linea con los mismo datos
									int flag2 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM c_nonbusinessday WHERE date1 = ?",new Timestamp(calInicial.getTimeInMillis()));
									//int flag2 = DB.getSQLValue(get_TrxName(), "SELECT tieneferiado(mm.M_Movement_ID) FROM M_Movement mm WHERE M_Movement_ID = ",rsHR.getInt("M_Movement_ID"));
									if(calInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || flag2 > 0)
									{
										int cantV2 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
												" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
												" WHERE r.Type = '02' AND r.C_BPartner_ID = "+ID_BPartner+" " +
												" AND TP_RefundAmt_ID IN ("+id_RefundAmt+","+id_RefundAmtF+") AND rl.DateTrx = ?",rsHR.getTimestamp("MovementDate"));
										if(cantV2 < 1  && (new Timestamp(calInicial.getTimeInMillis())).compareTo(p_DateTrxTo) <= 0
												&& (new Timestamp(calInicial.getTimeInMillis())).compareTo(p_DateTrxFrom) >= 0)										
										{
											//generamos la linea del viatico
											X_TP_RefundLine line2 = new X_TP_RefundLine(getCtx(), 0, get_TrxName());
											line2.setTP_Refund_ID(ID_Viatico);
											line2.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));							
											line2.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
											line2.setDateTrx(rsHR.getTimestamp("MovementDate"));						
											if(id_RefundAmt > 0 || id_RefundAmtF > 0 )
											{
												X_TP_RefundAmt rAmt = null;
												if(calInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY )
													rAmt = new X_TP_RefundAmt(getCtx(), id_RefundAmt, get_TrxName());
												else
													rAmt = new X_TP_RefundAmt(getCtx(), id_RefundAmtF, get_TrxName());
												line2.set_CustomColumn("TP_RefundAmt_ID",rAmt.get_ID());
												line2.setAmt(rAmt.getAmt());
											}
											else
												line2.setAmt(Env.ZERO);
											line2.save();
											cantLine++;
										}
									}
								}
							}	
						}
					}
					else//no existe documento de viatico
					{
						//se genera viatico
						X_TP_Refund viatico = new X_TP_Refund(getCtx(), 0, get_TrxName());
						viatico.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
						viatico.setDateDoc(p_DateTrxTo);
						viatico.setC_BPartner_ID(ID_BPartner);						
						viatico.setDescription("Generado Automaticamente viatico domingo o festivo");
						viatico.setDocStatus("DR");
						viatico.setType("02");
						viatico.setTP_RefundHeader_ID(headViatico.get_ID());
						viatico.set_CustomColumn("AD_OrgRef_ID", bPartner.getAD_Org_ID());
						viatico.save();
						canthead++;
						//generamos la linea del viatico solo si es domingo o feriado
						int flag = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM c_nonbusinessday WHERE date1 = ?",rsHR.getTimestamp("MovementDate"));
						if(rsHR.getTimestamp("MovementDate").getDay() == 0 || flag > 0)
						{
							//antes de generar la linea de viatico, hay que revisar que no exista otra linea con los mismo datos
							int cantV = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
									" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
									" WHERE r.Type = '02' AND r.C_BPartner_ID = "+ID_BPartner+" " +
									" AND TP_RefundAmt_ID IN ("+id_RefundAmt+","+id_RefundAmtF+") AND rl.DateTrx = ?",rsHR.getTimestamp("MovementDate"));
							if(cantV < 1  && rsHR.getTimestamp("MovementDate").compareTo(p_DateTrxTo) <= 0
									&& rsHR.getTimestamp("MovementDate").compareTo(p_DateTrxFrom) >= 0)
							{
								X_TP_RefundLine line = new X_TP_RefundLine(getCtx(), 0, get_TrxName());
								line.setTP_Refund_ID(viatico.get_ID());
								line.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));							
								line.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
								line.setDateTrx(rsHR.getTimestamp("MovementDate"));						
								if(id_RefundAmt > 0 || id_RefundAmtF > 0 )
								{
									X_TP_RefundAmt rAmt = null;
									if(rsHR.getTimestamp("MovementDate").getDay() == 0 )
										rAmt = new X_TP_RefundAmt(getCtx(), id_RefundAmt, get_TrxName());
									else
										rAmt = new X_TP_RefundAmt(getCtx(), id_RefundAmtF, get_TrxName());
									line.set_CustomColumn("TP_RefundAmt_ID",rAmt.get_ID());
									line.setAmt(rAmt.getAmt());
								}
								else
									line.setAmt(Env.ZERO);
								line.save();
								cantLine++;
							}
						}
						if(IsMultyDays)
						{	
							if(rsHR.getInt("cantDays") > 0)
							{
								Calendar calInicial = Calendar.getInstance();		
								calInicial.setTimeInMillis(rsHR.getTimestamp("MovementDate").getTime());
								for(int a=1;a < rsHR.getInt("cantDays");a++)
								{	
									calInicial.add(Calendar.DATE, 1);
									//generamos la linea del viatico solo si es domingo
									int flag2 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM c_nonbusinessday WHERE date1 = ?",new Timestamp(calInicial.getTimeInMillis()));
									if(calInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || flag2 > 0)
									{										
										//antes de generar la linea de viatico normal, hay que revisar que no exista otra linea con los mismo datos 
										int cantV2 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
												" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
												" WHERE r.Type = '02' AND r.C_BPartner_ID = "+ID_BPartner+" " +
												" AND TP_RefundAmt_ID IN ("+id_RefundAmt+","+id_RefundAmtF+") AND rl.DateTrx = ?",rsHR.getTimestamp("MovementDate"));
										if(cantV2 < 1  && (new Timestamp(calInicial.getTimeInMillis())).compareTo(p_DateTrxTo) <= 0
												&& (new Timestamp(calInicial.getTimeInMillis())).compareTo(p_DateTrxFrom) >= 0)										
										{
											//generamos la linea del viatico
											X_TP_RefundLine line2 = new X_TP_RefundLine(getCtx(), 0, get_TrxName());
											line2.setTP_Refund_ID(viatico.get_ID());
											line2.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));							
											line2.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
											line2.setDateTrx(rsHR.getTimestamp("MovementDate"));						
											if(id_RefundAmt > 0 || id_RefundAmtF > 0 )
											{
												X_TP_RefundAmt rAmt = null;
												if(calInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY )
													rAmt = new X_TP_RefundAmt(getCtx(), id_RefundAmt, get_TrxName());
												else
													rAmt = new X_TP_RefundAmt(getCtx(), id_RefundAmtF, get_TrxName());
												line2.set_CustomColumn("TP_RefundAmt_ID",rAmt.get_ID());
												line2.setAmt(rAmt.getAmt());
											}
											else
												line2.setAmt(Env.ZERO);
											line2.save();
											cantLine++;
										}
									}
								}
							}	
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage()+" SQL:"+sqlHR, e);
		}
		finally
		{
			if(rsHR != null && pstmtHR != null)
			{
				rsHR.close ();	pstmtHR.close ();
			}
			pstmtHR = null;	rsHR = null;
		}
		return "Se han generado "+canthead+" documentos de Viatico. Se han agregado "+cantLine+" lineas";
	}	//	doIt
}	//	Replenish
