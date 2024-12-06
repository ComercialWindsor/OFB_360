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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MPeriod;
import org.compiere.model.X_TP_CommissionBP;
import org.compiere.model.X_TP_CommissionTSM;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class ProcessCommission extends SvrProcess
{
	private int				p_TP_Commission_ID = 0;
	private String				p_DocAction; 
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 ProcessInfoParameter[] para = getParameter();
		 for (int i = 0; i < para.length; i++)
		 {
			 String name = para[i].getParameterName();
			
			 if (name.equals("DocAction"))
				 p_DocAction = (String) para[i].getParameter();
			 else
				 log.log(Level.SEVERE, "Unknown Parameter: " + name);
		 }
		 p_TP_Commission_ID=getRecord_ID();		 
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_TP_Commission_ID > 0)
		{
			X_TP_CommissionTSM com = new X_TP_CommissionTSM(getCtx(), p_TP_Commission_ID, get_TrxName());
			if(p_DocAction == null)
				p_DocAction = "CO";
			if(com.getDocStatus().compareToIgnoreCase("DR")==0 && p_DocAction.compareTo("CO") == 0)
			{
				MPeriod period = new MPeriod(getCtx(), com.getC_Period_ID(), get_TrxName());
				String sql= "SELECT mm.ad_org_id, mm.C_BPartner_ID,mm.C_ProjectOFB_ID,MAX(co.rut) as rut, " +
						" MAX(co.CommissionType) as CommissionType,MAX(co.Commission) as Commission," +
						" count(mm.M_Movement_ID) as qty " +
						" FROM rvofb_commission co" +
						" INNER JOIN M_Movement mm ON (co.C_BPartner_ID = mm.C_BPartner_ID) " +
						" INNER JOIN AD_Org org ON (mm.AD_Org_ID = org.AD_Org_ID)" +
						" WHERE org.IsNoCommission <> 'Y' AND MovementDate Between ? AND ? " +
						" GROUP BY mm.C_BPartner_ID, mm.C_ProjectOFB_ID, mm.AD_Org_ID " +
						" ORDER BY mm.ad_org_id, mm.C_ProjectOFB_ID,mm.C_BPartner_ID";
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try
				{
					pstmt = DB.prepareStatement(sql, get_TrxName());
					pstmt.setTimestamp(1, period.getStartDate());
					pstmt.setTimestamp(2, period.getEndDate());
					rs = pstmt.executeQuery();
					while(rs.next())
					{
						X_TP_CommissionBP comBP = new X_TP_CommissionBP(getCtx(), 0, get_TrxName());
						comBP.setTP_CommissionTSM_ID(com.get_ID());
						comBP.setAD_Org_ID(rs.getInt("AD_Org_ID"));
						comBP.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
						comBP.setRut(rs.getString("Rut"));
						comBP.setCommissionType(rs.getString("CommissionType"));
						comBP.setCommission(rs.getBigDecimal("Commission"));
						comBP.setC_ProjectOFB_ID(rs.getInt("C_ProjectOFB_ID"));
						comBP.setQty(rs.getBigDecimal("qty"));
						comBP.saveEx();
					}
				}
				catch (SQLException e)
				{
					throw new DBException(e, sql);
				}
				com.setDocStatus("IP");
			}
			else if(com.getDocStatus().compareToIgnoreCase("IP")==0 && p_DocAction.compareTo("CO") == 0)
			{
				com.setDocStatus("CO");
				com.setProcessed(true);
			}
			else if(com.getDocStatus().compareToIgnoreCase("DR")==0 && p_DocAction.compareTo("VO") == 0)
			{
				com.setDocStatus("VO");
				com.setProcessed(true);
			}
			else if(com.getDocStatus().compareToIgnoreCase("IP")==0 && p_DocAction.compareTo("VO") == 0)
			{
				com.setDocStatus("VO");
				com.setProcessed(true);
			}
			else if(com.getDocStatus().compareToIgnoreCase("CO")==0 && p_DocAction.compareTo("VO") == 0)
			{
				com.setDocStatus("VO");
				com.setProcessed(true);
			}
			com.saveEx();
		}
		return "Procesado ";
	}	//	doIt
}
