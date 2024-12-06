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
import java.util.logging.*;

import org.compiere.acct.Doc;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ResetAcct.java,v 1.2 2008/07/30 00:51:01 jjanke Exp $
 */
public class CleanAcct extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 
	 private int	p_Period_From = 0;

	 private int	p_Period_To = 0;
	 
	 protected void prepare()
	{
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("C_Period_ID"))
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
		String SqlUpdate ="";
		
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
					
					
					if(TableName.equals("C_DocType") || TableName.equals("PP_Order") || TableName.equals("PP_Cost_Collector") || TableName.equals("DD_Order") 
							|| TableName.equals("HR_Process") || TableName.equals("C_BankFactoring"))
						continue;
					
					SqlUpdate="Update " + TableName + " set posted='N', Processing='N' Where 1=1";
					String TableField = "DateAcct";
					if(TableName.equals(MBankStatement.Table_Name))
						TableField = "StatementDate";
					if(TableName.equals(MInventory.Table_Name) || TableName.equals(MMovement.Table_Name) || TableName.equals(X_M_Production.Table_Name) || TableName.equals(MProjectIssue.Table_Name))
						TableField = "MovementDate";
					if(TableName.equals(MRequisition.Table_Name))
						TableField = "DateDoc";
					
					SqlUpdate+= " And " + TableField + " Between ? and ?";
					
					SqlUpdate+= " And Not Exists (select * from fact_acct f where f.AD_Table_ID="+AD_Table_ID+ " and f.Record_ID="+TableName+"_ID )";
					
					CPreparedStatement cs = DB.prepareStatement (SqlUpdate, get_TrxName());
					cs.setTimestamp(1, rs.getTimestamp("startdate"));
					cs.setTimestamp(2, rs.getTimestamp("enddate"));
					cs.executeUpdate();
					
					
					
					commit();
					cs=null;
					
					addLog (0, rs.getTimestamp("startdate"), null, rs.getString("name")+ " Periodo Limpio");
				}
				
				
				
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, SqlUpdate, e);
		}
		
		
		
		
	   return "Contabilidad Borrada";
	}	//	doIt


	
}	//	ResetAcct
