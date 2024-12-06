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
package org.ofb.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import org.compiere.model.X_M_LoadBalancing;
import org.compiere.model.X_M_RequisitionCalendar;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	GenerateReqFromInventory
 *	
 *  @author Italo Niñoles
 *  @version $Id: GenerateReqFromInventory.java,v 1.2 2015/06/09 ininoles
 *  
 */
public class ProcessReqCalendar extends SvrProcess
{

	private String	p_Action = "";	
	protected void prepare()
	{
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
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare
	

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int id_role = Env.getAD_Role_ID(getCtx());
		String msg = "";
		int reqCal_ID = getRecord_ID();
		X_M_RequisitionCalendar reqCal= new X_M_RequisitionCalendar(getCtx(),reqCal_ID,get_TrxName());
		
		if(p_Action.compareToIgnoreCase("PR") == 0)
		{
			if((reqCal.getDocStatus().compareToIgnoreCase("10" ) == 0 ||
					reqCal.getDocStatus().compareToIgnoreCase("15") == 0) &&
					reqCal.getC_DocType_ID() == 1000123)
			{	
				if (id_role != 1000023)				
					throw new AdempiereUserError("Rol sin privilegios suficientes");
				reqCal.setDocStatus("20");
				msg = "Procesado";
			}
			else if((reqCal.getDocStatus().compareToIgnoreCase("10" ) == 0 ||
					reqCal.getDocStatus().compareToIgnoreCase("15") == 0) &&
					reqCal.getC_DocType_ID() == 1000124)
			{
				if (id_role != 1000025)				
					throw new AdempiereUserError("Rol sin privilegios suficientes");
				reqCal.setDocStatus("20");
				msg = "Procesado";				
			}
			else if(reqCal.getDocStatus().compareToIgnoreCase("20") ==0 ||
					reqCal.getDocStatus().compareToIgnoreCase("25") ==0)
			{	
				if (id_role != 1000024)				
					throw new AdempiereUserError("Rol sin privilegios suficientes");
				reqCal.setDocStatus("30");
				msg = "Procesado";
			}
			else if(reqCal.getDocStatus().compareToIgnoreCase("30") ==0  &&
					reqCal.getC_DocType_ID() == 1000123)
			{
				if (id_role != 1000025)				
					throw new AdempiereUserError("Rol sin privilegios suficientes");
				
				//actualizacion de balanceo de carga
				int id_LB = DB.getSQLValue(get_TrxName(), "SELECT M_LoadBalancing_ID FROM M_LoadBalancing"
						+ " WHERE AD_Org_ID = "+reqCal.getAD_Org_ID()+" AND pep = '"+reqCal.getPep() +"'");
				if (id_LB > 0)					
				{
					X_M_LoadBalancing loadB = new X_M_LoadBalancing(getCtx(), id_LB, get_TrxName());
					String sqlDetail = "SELECT DayOfWeek FROM M_RequisitionCalendarLine "
							+ "WHERE M_RequisitionCalendar_ID = "+reqCal.get_ID();
					try
					{
						PreparedStatement pstmt = DB.prepareStatement(sqlDetail, get_TrxName());
						ResultSet rs = pstmt.executeQuery();
						while (rs.next())
						{
							String day = rs.getString("DayOfWeek");
							if (day.compareToIgnoreCase("MON") == 0)
							{	
								loadB.setQtyMonday(loadB.getQtyMonday()+1);
							}
							else if (day.compareToIgnoreCase("TUE") == 0)
							{
								loadB.setQtyTuesday(loadB.getQtyTuesday()+1);
							}
							else if (day.compareToIgnoreCase("THU") == 0)
							{
								loadB.setQtyThursday(loadB.getQtyThursday()+1);
							}
							else if (day.compareToIgnoreCase("WED") == 0)
							{
								loadB.setQtyWednesday(loadB.getQtyWednesday()+1);
							}
							else if (day.compareToIgnoreCase("FRI") == 0)
							{
								loadB.setQtyFriday(loadB.getQtyFriday()+1);
							}
							else if (day.compareToIgnoreCase("SAT") == 0)
							{
								loadB.setQtySaturday(loadB.getQtySaturday()+1);
							}
							loadB.save();
						}
						rs.close();
						pstmt.close();
						pstmt = null;
						
					}
					catch (Exception e)
					{
						log.config(sqlDetail+" - "+e);					
					}							
				}
				else
				{
					throw new AdempiereUserError("No Existe Balanceo");
				}
				
				reqCal.setDocStatus("40");
				reqCal.setProcessed(true);
				msg = "Procesado. Solicitud Aprobada. Balance Cargado";				
			}
			else if(reqCal.getDocStatus().compareToIgnoreCase("30") ==0  &&
					reqCal.getC_DocType_ID() == 1000124)
			{
				if (id_role != 1000023)				
					throw new AdempiereUserError("Rol sin privilegios suficientes");
				
				//actualizacion de balanceo de carga
				int id_LB = DB.getSQLValue(get_TrxName(), "SELECT M_LoadBalancing_ID FROM M_LoadBalancing"
						+ " WHERE AD_Org_ID = "+reqCal.getAD_Org_ID()+" AND pep = '"+reqCal.getPep() +"'");
				if (id_LB > 0)					
				{
					X_M_LoadBalancing loadB = new X_M_LoadBalancing(getCtx(), id_LB, get_TrxName());
					String sqlDetail = "SELECT DayOfWeek FROM M_RequisitionCalendarLine "
							+ "WHERE M_RequisitionCalendar_ID = "+reqCal.get_ID();
					try
					{
						PreparedStatement pstmt = DB.prepareStatement(sqlDetail, get_TrxName());
						ResultSet rs = pstmt.executeQuery();
						while (rs.next())
						{
							String day = rs.getString("DayOfWeek");
							if (day.compareToIgnoreCase("MON") == 0)
							{	
								loadB.setQtyMonday(loadB.getQtyMonday()+1);
							}
							else if (day.compareToIgnoreCase("TUE") == 0)
							{
								loadB.setQtyTuesday(loadB.getQtyTuesday()+1);
							}
							else if (day.compareToIgnoreCase("THU") == 0)
							{
								loadB.setQtyThursday(loadB.getQtyThursday()+1);
							}
							else if (day.compareToIgnoreCase("WED") == 0)
							{
								loadB.setQtyWednesday(loadB.getQtyWednesday()+1);
							}
							else if (day.compareToIgnoreCase("FRI") == 0)
							{
								loadB.setQtyFriday(loadB.getQtyFriday()+1);
							}
							else if (day.compareToIgnoreCase("SAT") == 0)
							{
								loadB.setQtySaturday(loadB.getQtySaturday()+1);
							}
							loadB.save();
						}
						rs.close();
						pstmt.close();
						pstmt = null;
					}
					catch (Exception e)
					{
						log.config(sqlDetail+" - "+e);					
					}					
				}
				else
				{
					throw new AdempiereUserError("No Existe Balanceo");
				}
				reqCal.setDocStatus("40");
				reqCal.setProcessed(true);
				msg = "Procesado. Solicitud Aprobada. Balance Cargado";
			}			
		}
		else if(p_Action.compareToIgnoreCase("VO") == 0)
		{
			if(reqCal.getDocStatus().compareToIgnoreCase("20") == 0)
			{	
				if (id_role != 1000024)				
					throw new AdempiereUserError("Rol sin privilegios suficientes");
				reqCal.setDocStatus("15");
				msg = "Anulado";
			}
			if(reqCal.getDocStatus().compareToIgnoreCase("30") == 0 &&
					reqCal.getC_DocType_ID() == 1000123)
			{	
				if (id_role != 1000025)				
					throw new AdempiereUserError("Rol sin privilegios suficientes");
				reqCal.setDocStatus("25");
				msg = "Anulado";
			}
			if(reqCal.getDocStatus().compareToIgnoreCase("30") == 0 &&
					reqCal.getC_DocType_ID() == 1000124)
			{	
				if (id_role != 1000023)				
					throw new AdempiereUserError("Rol sin privilegios suficientes");
				reqCal.setDocStatus("25");
				msg = "Anulado";
			}
		}else
		{
			throw new AdempiereUserError("Sin Accion Seleccionada");
		}		
		reqCal.save();
		return msg;
	}	//	doIt	
}	//	Replenish
