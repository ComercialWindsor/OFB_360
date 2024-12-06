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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.acct.Doc;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ResetAcct.java,v 1.2 2008/07/30 00:51:01 jjanke Exp $
 */
public class ResetAcct extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 private int Table_ID =0;
	 
	 private int	p_Period_From = 0;

	 private int	p_Period_To = 0;
	 
	 protected void prepare()
	{
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Table_ID"))
				Table_ID = para[i].getParameterAsInt();
		    else if (name.equals("C_Period_ID"))
			{
				p_Period_From = para[i].getParameterAsInt();
				p_Period_To = para[i].getParameter_ToAsInt();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		if (p_Period_From == 0 || p_Period_To==0)
			throw new IllegalArgumentException("Periods Mandatory");
		
		
		MPeriod from = MPeriod.get(getCtx(), p_Period_From);
		MPeriod to = MPeriod.get(getCtx(), p_Period_To);
		
		if(from.getC_Year_ID() != to.getC_Year_ID())
			throw new IllegalArgumentException("Periods Same year");
		
		String sql ="select * from C_Period where periodno >= " + from.getPeriodNo() +" and  periodno <= " + to.getPeriodNo() +" and C_Year_ID="+from.getC_Year_ID();
		
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				
				int noOpen= DB.getSQLValue(get_TrxName(), "select count(1) from C_PeriodControl where periodstatus!='O' and c_period_id ="+rs.getInt("c_period_id"));
				if(noOpen>0)
				{
					addLog (0, rs.getTimestamp("startdate"), null, rs.getString("name")+ " Periodo Cerrado");
					continue;
				}
				
				int[] docs=Doc.getDocumentsTableID();
				String[] tables=Doc.getDocumentsTableName();
				
				for (int i = 0; i < tables.length; i++)
				{
					int AD_Table_ID = docs[i];
					String TableName = tables[i];
					
					if(Table_ID>0)
						if(Table_ID!=AD_Table_ID)
							continue;
					
					if(TableName.equals("C_DocType") || TableName.equals("PP_Order") || TableName.equals("PP_Cost_Collector") || TableName.equals("DD_Order") 
							|| TableName.equals("HR_Process") || TableName.equals("C_BankFactoring"))
						continue;
					
					String SqlUpdate="Update " + TableName + " set posted='N', Processing='N' Where 1=1";
					String TableField = "DateAcct";
					if(TableName.equals(MBankStatement.Table_Name))
						TableField = "StatementDate";
					if(TableName.equals(MInventory.Table_Name) || TableName.equals(MMovement.Table_Name) || TableName.equals(X_M_Production.Table_Name) || TableName.equals(MProjectIssue.Table_Name))
						TableField = "MovementDate";
					if(TableName.equals(MRequisition.Table_Name))
						TableField = "DateDoc";
					
					SqlUpdate+= " And " + TableField + " Between ? and ? and ad_client_ID = ?";
					
					//ininoles validacion campo IsCustomAcct
					int cantV = -1;
					try
					{
						String sqlValid = "SELECT count(IsCustomAcct) FROM "+TableName;
						cantV = DB.getSQLValue(null, sqlValid);
					}
					catch (Exception e1)
					{
						log.log(Level.SEVERE, sql+ " -- No existe campo IsCustomAcct en la tabla", e1);
					}
					
					if (cantV >= 0)
					{
						SqlUpdate+= " And IsCustomAcct = 'N' ";
						
					}
					//end ininoles
					
					CPreparedStatement cs = DB.prepareStatement (SqlUpdate, get_TrxName());
					cs.setTimestamp(1, rs.getTimestamp("startdate"));
					cs.setTimestamp(2, rs.getTimestamp("enddate"));
					cs.setInt(3, getAD_Client_ID());
					cs.executeUpdate();
					
					//ininoles validacion con campo IsCustomAcct
					
					String sqlDelete = "Delete from Fact_Acct where AD_Table_ID="+ AD_Table_ID +" and C_Period_ID= " + rs.getInt("C_Period_ID")+" and AD_Client_ID= "+getAD_Client_ID();
					
					if (cantV >= 0)
					{
						sqlDelete+= " AND (select IsCustomAcct FROM "+ TableName +" where Fact_Acct.Record_ID = "+TableName+"_ID) = 'N'";
						
					}							
					
					DB.executeUpdate(sqlDelete, get_TrxName());
					commit();
					cs=null;
					
					addLog (0, rs.getTimestamp("startdate"), null, rs.getString("name")+ " Periodo Reseteado");
				}
				
				
				
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		
		
		
	   return "Contabilidad Borrada";
	}	//	doIt


	
}	//	ResetAcct
