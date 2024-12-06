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
 *  @version $Id: GeneralClose.java,v 1.2 2011/06/12 00:51:01  $
 */
public class GeneralOpen extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int 			p_Org_ID;
	
	private int 			p_Year_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Org_ID"))
				p_Org_ID =  para[i].getParameterAsInt();
			else if (name.equals("C_Year_ID"))
				p_Year_ID =  para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		PreparedStatement pstmt = null;
		   String mysql="SELECT f.account_id,sum(f.amtacctdr) as amtdr,sum(f.amtacctcr) as amtcr from fact_acct f " +
		   		" Inner Join c_period p on (p.c_period_id=f.c_period_id) " +
		   		" inner join C_elementvalue e on (f.account_id=e.C_elementvalue_id) " +
		   		" where p.c_year_id=? AND e.accounttype In ('A','L') " +
		   		"  AND (f.AD_Org_ID=? OR f.AD_Org_ID in (Select oi.AD_ORG_ID from AD_orginfo oi where oi.PARENT_ORG_ID=?) )" +
		   		" group by f.account_id";
		   
		
		MPeriod period = getPeriod(p_Year_ID,1);
		MAcctSchema as = MAcctSchema.getClientAcctSchema(getCtx(), Env.getAD_Client_ID(getCtx()))[0];
		MJournalBatch batch = null;
		MJournal journal = null;
			try
			{
				pstmt = DB.prepareStatement(mysql, get_TrxName());
				pstmt.setInt(1, getLastYear());
				pstmt.setInt(2, p_Org_ID);
				pstmt.setInt(3, p_Org_ID);
				
				ResultSet rs = pstmt.executeQuery();
				
				
				while (rs.next())
				{
					
					
					if(batch == null)
					{
						MClient client= MClient.get(getCtx(),Env.getAD_Client_ID(getCtx()));
						MYear year = new MYear (getCtx(), p_Year_ID ,get_TrxName());
						
						batch = new MJournalBatch (getCtx(), 0, get_TrxName());
						batch.setClientOrg(getAD_Client_ID(),p_Org_ID);
						batch.setDescription("Apertura "+ year.getYearAsInt());
						batch.setDateDoc(period.getStartDate());
						batch.setDateAcct(period.getStartDate());
						batch.setC_DocType_ID(MDocType.getOfDocBaseType(getCtx(), "GLJ")[0].getC_DocType_ID());
						batch.setGL_Category_ID(MGLCategory.getDefault(getCtx(), MGLCategory.CATEGORYTYPE_Manual).getGL_Category_ID());
						batch.setC_Period_ID(period.getC_Period_ID());
						batch.setC_Currency_ID(client.getC_Currency_ID());
						batch.setPostingType(X_GL_JournalBatch.POSTINGTYPE_Commitment);
						batch.save();
						
						journal= new MJournal(batch);
						journal.setDescription("Apertura "+ year.getYearAsInt());
						journal.setC_AcctSchema_ID(as.getC_AcctSchema_ID() );
						journal.setGL_Category_ID(MGLCategory.getDefault(getCtx(), MGLCategory.CATEGORYTYPE_Document).getGL_Category_ID() );
						journal.setC_ConversionType_ID(114);
						journal.setPostingType(X_GL_JournalBatch.POSTINGTYPE_Commitment);
						journal.save();
					}
					
					MJournalLine line= new MJournalLine(journal);
					line.setAmtSourceDr(rs.getBigDecimal("amtdr"));
					line.setAmtSourceCr(rs.getBigDecimal("amtcr"));
					line.setAmtAcct(rs.getBigDecimal("amtdr"), rs.getBigDecimal("amtcr"));
					line.setC_ValidCombination_ID(getCombination(rs.getInt(1)) );
					line.save();
					
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}

			if(batch == null)
				return " Sin Movimientos";
			else
				return batch.getDocumentNo();
		
	}	//	doIt

	
	public MPeriod getPeriod(int year_ID,int periodNO){
		
		PreparedStatement pstmt = null;
		String sql = "select * from c_period where c_year_id = ? and periodno=?";
		MPeriod period = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			
			pstmt.setInt(1, year_ID);
			pstmt.setInt(2, periodNO);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				period = new MPeriod (getCtx(), rs,get_TrxName());
			}
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return period;
	}
	
   public int getLastYear(){
		
		PreparedStatement pstmt = null;
		String sql = "select c_year_id from c_year where fiscalyear = ?";
		MYear current = new MYear (getCtx(), p_Year_ID ,get_TrxName());
		int year=0;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			
			pstmt.setInt(1, current.getYearAsInt()-1);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				year = rs.getInt(1);
			}
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return year;
	}
	
   public int getCombination(int account_id){
		
		PreparedStatement pstmt = null;
		String sql = "select c_validcombination_id from C_ValidCombination where account_id=?";
		int account = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			
			pstmt.setInt(1, account_id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				account = rs.getInt(1);
			}
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return account;
	}

	
}	//	CopyOrder
