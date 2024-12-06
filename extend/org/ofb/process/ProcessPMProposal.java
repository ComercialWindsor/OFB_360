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
package org.ofb.process;

import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class ProcessPMProposal extends SvrProcess
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
		
		X_PM_Proposal Pro=new X_PM_Proposal(Env.getCtx(), Record_ID,get_TrxName());
		
		if(P_DocAction.equals("CO") && !Pro.isProcessed()){
			
			if(getPending(Pro.getPM_Tender_ID())>0)
				return "Aun quedan proyectos sin presupuesto asignado";
			
			//crear acuerdo core y resolucion de asignacion
			create(Pro.getPM_Tender_ID(),Pro.getAD_Org_ID());
			
			Pro.setProcessed(true);
			Pro.setDocStatus("CO");
			Pro.save();
			return "Confirmado";
		}
		
		if(P_DocAction.equals("RJ") && !Pro.isProcessed() && Pro.getDocStatus().equals("DR")){
			
			X_PM_Approval App=new X_PM_Approval(Env.getCtx(), Pro.getPM_Approval_ID() ,get_TrxName());
			App.setDocStatus("NA");
			App.setProcessed(false);
			App.save();
			
			Pro.setProcessed(false);
			Pro.setDocStatus("WC");
			Pro.save();
			return "Propuesta Rechazada";
		}

			return "No es posible Cumplir la Accion ";
		
	}	//	doIt

	public void create(int Tender_ID, int Org_ID){
		   
		   PreparedStatement pstmt = null;
		   String mysql="SELECT c_project_id,committedamt,POReference,C_BPartner_ID,C_BPartnerC_ID,C_BPartnerA_ID,PM_Gore_UT,plannedamt,Year from c_project where isapproved='Y' and pm_tender_id = ?";
			try
			{
				pstmt = DB.prepareStatement(mysql, get_TrxName());
				pstmt.setInt(1, Tender_ID);
				
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					
					//actualizar items proyecto inicio
					BigDecimal pct= rs.getBigDecimal("committedamt").multiply(Env.ONEHUNDRED);
					pct = pct.divide(rs.getBigDecimal("plannedamt"), 6, BigDecimal.ROUND_HALF_UP);
					pct = pct.divide(Env.ONEHUNDRED);
					MProject pj = new MProject (Env.getCtx(), rs.getInt(1),get_TrxName());
					MProjectLine[] lines = pj.getLines();
					for(MProjectLine line : lines){	
						line.setCommittedAmt(line.getPlannedAmt().multiply(pct).setScale(0,BigDecimal.ROUND_HALF_UP));
						line.saveEx();
					}
					//actualizar items proyecto fin
					
					X_DM_MandateAgreement cm = new X_DM_MandateAgreement(Env.getCtx(), 0 ,get_TrxName());//convenio mandato
					cm.setAD_Org_ID(Org_ID);
					cm.setDocStatus("DR");
					cm.setC_Project_ID(rs.getInt(1));
					cm.setDateTrx(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
					cm.setAmt(rs.getBigDecimal(2));
					
					//cm.setAllocatedAmt(Env.ZERO);
					cm.setAllocatedAmt(rs.getBigDecimal(2));
					cm.set_CustomColumn("Year", rs.getString("Year"));
					cm.set_CustomColumn("Code", rs.getString("POReference"));
					cm.set_CustomColumn("C_BPartner_ID", rs.getInt("C_BPartner_ID"));
					cm.set_CustomColumn("C_BPartnerC_ID", rs.getInt("C_BPartnerC_ID"));
					cm.set_CustomColumn("C_BPartnerA_ID", rs.getInt("C_BPartnerA_ID"));
					
					BigDecimal factor = DB.getSQLValueBD(get_TrxName(), "select factor from PM_YearFactor where Year=?",rs.getString("Year"));
					cm.set_CustomColumn("Factor", factor==null?Env.ONE:factor);
					
					if(rs.getString("PM_Gore_UT").equals("Y"))
						cm.set_CustomColumn("DM_MandateAgreementType", "02");
					else
						cm.set_CustomColumn("DM_MandateAgreementType", "01");
					
					cm.save();
					
					X_DM_Document rss = new X_DM_Document(Env.getCtx(), 0 ,get_TrxName());//solicitud de fondos
					rss.setAD_Org_ID(Org_ID);
					rss.setDocStatus("DR");
					rss.setC_Project_ID(rs.getInt(1));
					rss.setDM_DocumentType("AP");
					rss.setDateTrx(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
					rss.setAmt(Env.ZERO);
					rss.save();
					
					//generacion de lineas en la asignacion de fondos Inicio ININOLES faaguilar
					String sql2 = "select C_ProjectLine_ID, Line, Description, M_Product_ID, PlannedPrice "
									+ "from C_projectline where c_project_id =? ";												
					try {
						PreparedStatement pstmtl = DB.prepareStatement(sql2, get_TrxName());
						pstmtl.setInt(1, rs.getInt(1));
						ResultSet rsl = pstmtl.executeQuery();
					
						while (rsl.next())
						{
							X_DM_DocumentLine dcl = new X_DM_DocumentLine(getCtx(), 0, get_TrxName());
							dcl.setDM_Document_ID(rss.getDM_Document_ID());
							dcl.setAmt(rsl.getBigDecimal(5));
							dcl.set_CustomColumn("C_ProjectLine_ID", rsl.getInt(1));
							dcl.setM_Product_ID(rsl.getInt(4));
							dcl.setDateTrx(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
							dcl.setDescription(rsl.getString(3));			
							dcl.save();
						}
					}	
					catch (Exception e)
					{
						log.log(Level.SEVERE, e.getMessage(), e);
					}
					//generacion de lineas en la asignacion de fondos FIN ININOLES faaguilar
					
					pj.set_CustomColumn("IsPriorized", true);
					pj.save();
					
					
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			
	   }
	
	public int getPending(int Tender_ID){
		   
		   int count = 0;
		   PreparedStatement pstmt = null;
		   String mysql="SELECT count(1) from c_project where isapproved='Y' and score>=60 and committedamt=0 and pm_tender_id = ?";
			try
			{
				pstmt = DB.prepareStatement(mysql, get_TrxName());
				pstmt.setInt(1, Tender_ID);
				
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
				{
					count = rs.getInt(1);
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			return count;
			
	   }
	
}	//	CopyOrder
