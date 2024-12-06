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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MOrg;
import org.compiere.model.X_TP_Refund;
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
public class GenerateRefund extends SvrProcess
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
		//String sqlHR = "SELECT pm.M_Movement_ID,pm.C_BPartner_ID,pm.movementdate,pm.AD_OrgRef_ID as AD_Org_ID," +
				//ininoles se saca cantidad de dias de la HR
				" (select MAX(extract(day from mml.tp_inicialhr)) from m_movementline mml where mml.m_movement_id =  pm.M_Movement_ID)- " +
				" (select MIN(extract(day from mml.tp_finalhr)) from m_movementline mml where mml.m_movement_id = pm.M_Movement_ID) + 1 " +
				" as cantDays , null as Pre_M_Movement_ID,pm.created" +
				//end
				" FROM M_Movement pm " +
				/*" WHERE pm.M_Movement_ID NOT IN (SELECT rLine.M_Movement_ID FROM TP_RefundLine rLine " +
				" INNER JOIN TP_Refund r ON (r.TP_Refund_ID = rLine.TP_Refund_ID) " +
				" WHERE r.DocStatus IN ('CO','DR','IP') AND rLine.M_Movement_ID IS NOT NULL) " +
				" AND pm.AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+*/
				" WHERE pm.AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+
				//" AND pm.MovementDate BETWEEN ? AND ? ";
				" AND C_Bpartner_ID = 1002699" + //ininoles liea comenada solo usar para analisis en debug 
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
			//sqlHR = sqlHR + " AND pm.AD_OrgRef_ID = "+p_Org_ID+" AND pm.DocStatus IN ('CO','CL','DR')";
		}
		//codigo de disponibilidades
		MOrg orgH = new MOrg(getCtx(), p_Org_ID, get_TrxName());
		Boolean IsMultyDaysH = orgH.get_ValueAsBoolean("IsMultipleDays");
		if(IsMultyDaysH)
		{
			sqlHR = sqlHR + " UNION" +
				" SELECT null,pml.C_Bpartner_ID,pm.movementDate,pm.AD_Org_ID,1,pm.Pre_M_Movement_ID,pm.created" +
				" FROM Pre_M_Movement pm " +
				" INNER JOIN Pre_M_MovementLine pml ON (pm.Pre_M_Movement_ID = pml.Pre_M_Movement_ID AND C_Bpartner_ID IS NOT NULL) " +
				" WHERE pm.AD_Org_ID = "+p_Org_ID+" AND pm.movementDate BETWEEN ?  AND ? AND pm.DocStatus IN ('CO','CL')"+
				" AND pml.WorkShiftBP IN ('FZ','TS','TN')";
		}
		sqlHR = sqlHR + " ORDER BY C_BPartner_ID, movementdate, created";
			
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
			pstmtHR.setTimestamp(5, p_DateTrxFrom);
			pstmtHR.setTimestamp(6, p_DateTrxTo);
			rsHR = pstmtHR.executeQuery();		
			int ID_BPartner = 0;
			int ID_Org = 0;
			MOrg org = null;
			while (rsHR.next())
			{
				ID_BPartner = rsHR.getInt("C_BPartner_ID");
				ID_Org = rsHR.getInt("AD_Org_ID");
				BigDecimal amtOrg = null;
				BigDecimal amtBP = null;
				BigDecimal amtRefund = null; 
				//generamos viatico para BP
				if(ID_BPartner > 0 && ID_Org > 0)
				{
					//creamos org para sacar monto
					org = new MOrg(getCtx(), ID_Org, get_TrxName());
					Boolean IsMultyDays = org.get_ValueAsBoolean("IsMultipleDays");
					amtOrg = (BigDecimal)org.get_Value("AmountRefund");
					//creamos BP para buscar monto
					MBPartner bPartner = new MBPartner(getCtx(), ID_BPartner, get_TrxName());
					amtBP = (BigDecimal)bPartner.get_Value("AmountRefund");
					//buscamos viatico en destino
					BigDecimal amtDes = DB.getSQLValueBD(get_TrxName(), "SELECT MAX(AmountRefund) " +
							" FROM M_MovementLine ml " +
							" INNER JOIN TP_Destination d ON (ml.TP_Destination_ID = d.TP_Destination_ID)" +
							" WHERE M_Movement_ID = ?",rsHR.getInt("M_Movement_ID"));
					
					if(amtDes != null && amtDes.compareTo(Env.ZERO) > 0)
						amtRefund = amtDes;
					else if(amtBP != null && amtBP.compareTo(Env.ZERO) > 0)
						amtRefund = amtBP;
					else if(amtOrg != null && amtOrg.compareTo(Env.ZERO) > 0)
						amtRefund = amtOrg;
					else
						amtRefund = Env.ZERO;
					//buscamos viatico abierto existente
					int ID_Viatico = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(TP_Refund_ID),0)" +
							" FROM TP_Refund WHERE DocStatus NOT IN ('CO','VO') AND Type='01' " +
							" AND C_BPartner_ID = "+ID_BPartner);
					if(ID_Viatico > 0)//existe viatico abierto para conductor
					{
						//antes de generar la linea de viatico normal, hay que revisar que no exista otra linea con los mismo datos
						int cantV = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
								" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
								" WHERE r.Type = '01' AND r.C_BPartner_ID = "+ID_BPartner+" AND rl.DateTrx = ?",rsHR.getTimestamp("MovementDate"));
						if(cantV < 1 && rsHR.getTimestamp("MovementDate").compareTo(p_DateTrxTo) <= 0
								&& rsHR.getTimestamp("MovementDate").compareTo(p_DateTrxFrom) >= 0)
						{
							//generamos la linea del viatico
							X_TP_RefundLine line = new X_TP_RefundLine(getCtx(), 0, get_TrxName());
							line.setTP_Refund_ID(ID_Viatico);
							if(rsHR.getInt("M_Movement_ID") > 0)
								line.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
							if(rsHR.getInt("Pre_M_Movement_ID") > 0)
								line.set_CustomColumn("Pre_M_Movement_ID",rsHR.getInt("Pre_M_Movement_ID"));
							line.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
							//line.setDescription("Ingresado Automaticamente");
							line.setDateTrx(rsHR.getTimestamp("MovementDate"));						
							line.setAmt(amtRefund);
							line.save();
							cantLine++;
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
									int cantV2 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
											" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
											" WHERE r.Type = '01' AND r.C_BPartner_ID = "+ID_BPartner+" AND rl.DateTrx = ?",new Timestamp(calInicial.getTimeInMillis()));
									if(cantV2 < 1  && (new Timestamp(calInicial.getTimeInMillis())).compareTo(p_DateTrxTo) <= 0
											&& (new Timestamp(calInicial.getTimeInMillis())).compareTo(p_DateTrxFrom) >= 0)										
									{
										//generamos la linea del viatico
										X_TP_RefundLine line2 = new X_TP_RefundLine(getCtx(), 0, get_TrxName());
										line2.setTP_Refund_ID(ID_Viatico);
										if(rsHR.getInt("M_Movement_ID") > 0)
											line2.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
										if(rsHR.getInt("Pre_M_Movement_ID") > 0)
											line2.set_CustomColumn("Pre_M_Movement_ID",rsHR.getInt("Pre_M_Movement_ID"));
										line2.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
										//line2.setDescription("Ingresado Automaticamente");
										line2.setDateTrx(new Timestamp(calInicial.getTimeInMillis()));
										line2.setAmt(amtRefund);
										line2.save();
										cantLine++;
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
						viatico.setDescription("Generado Automaticamente");
						viatico.setDocStatus("DR");
						viatico.setType("01");
						viatico.save();
						canthead++;
						//generamos la linea del viatico
						//antes de generar la linea de viatico normal, hay que revisar que no exista otra linea con los mismo datos
						int cantV = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
								" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
								" WHERE r.C_BPartner_ID = "+ID_BPartner+" AND rl.DateTrx = ?",rsHR.getTimestamp("MovementDate"));
						if(cantV < 1  && rsHR.getTimestamp("MovementDate").compareTo(p_DateTrxTo) <= 0
								&& rsHR.getTimestamp("MovementDate").compareTo(p_DateTrxFrom) >= 0)
						{
							X_TP_RefundLine line = new X_TP_RefundLine(getCtx(), 0, get_TrxName());
							line.setTP_Refund_ID(viatico.get_ID());
							if(rsHR.getInt("M_Movement_ID") > 0)
								line.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
							if(rsHR.getInt("Pre_M_Movement_ID") > 0)
								line.set_CustomColumn("Pre_M_Movement_ID",rsHR.getInt("Pre_M_Movement_ID"));
							line.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
							//line.setDescription("Ingresado Automaticamente");
							line.setDateTrx(rsHR.getTimestamp("MovementDate"));						
							line.setAmt(amtRefund);
							line.save();
							cantLine++;
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
									//generamos la linea del viatico
									//antes de generar la linea de viatico normal, hay que revisar que no exista otra linea con los mismo datos 
									int cantV2 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
											" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
											" WHERE r.Type = '01' AND r.C_BPartner_ID = "+ID_BPartner+" AND rl.DateTrx = ?",new Timestamp(calInicial.getTimeInMillis()));
									if(cantV2 < 1  && (new Timestamp(calInicial.getTimeInMillis())).compareTo(p_DateTrxTo) <= 0
											&& (new Timestamp(calInicial.getTimeInMillis())).compareTo(p_DateTrxFrom) >= 0)										
									{
										X_TP_RefundLine line2 = new X_TP_RefundLine(getCtx(), 0, get_TrxName());
										line2.setTP_Refund_ID(viatico.get_ID());
										if(rsHR.getInt("M_Movement_ID") > 0)
											line2.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
										if(rsHR.getInt("Pre_M_Movement_ID") > 0)
											line2.set_CustomColumn("Pre_M_Movement_ID",rsHR.getInt("Pre_M_Movement_ID"));
										line2.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
										//line2.setDescription("Ingresado Automaticamente");
										line2.setDateTrx(new Timestamp(calInicial.getTimeInMillis()));
										line2.setAmt(amtRefund);
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
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage()+" SQL:"+sqlHR, e);
		}
		finally
		{
			rsHR.close ();	pstmtHR.close ();
			pstmtHR = null;	rsHR = null;
		}
		return "Se han generado "+canthead+" documentos de Viatico. Se han agregado "+cantLine+" lineas";
	}	//	doIt
}	//	Replenish
