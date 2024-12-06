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

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;

import java.math.BigDecimal;
import org.compiere.util.*;
import java.util.Calendar;
import java.util.Date;

import org.compiere.process.*;
 
/**
 *	report balances 8
 *	
 *  @author ininoles
 *  @version $Id: GOREPresupuestoLeyGasto.java,v 1.2 2009/04/17 00:51:02 ininoles$
 */
public class GOREPresupuestoLeyGasto extends SvrProcess
{
	/**	The Order				*/
	
	private Timestamp 	p_DateAcct_From;
	private Timestamp 	p_DateAcct_To;
	private int 	p_Account_ID_From=0;
	private String  p_AccountValue_From = null;
	private int 	p_Account_ID_To=0;
	private String  p_AccountValue_To = null;
	private int 	p_PInstance_ID;
	private String  p_Postingtype = null;

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
			else if (name.equals("DateAcct"))
			{
				p_DateAcct_From = (Timestamp)para[i].getParameter();
				p_DateAcct_To = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("Account_ID"))
			{
				p_Account_ID_From = para[i].getParameterAsInt();
				p_Account_ID_To = para[i].getParameter_ToAsInt();
			}
			else if (name.equals("PostingType"))
			{
				p_Postingtype = (String)para[i].getParameter();				
			}
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		
		
		/*********/
		DB.executeUpdate("DELETE FROM T_PresupuestoLGasto", get_TrxName());
		/*********/
		
		String sqlVF = "select cev.value from c_validcombination cvc "+
					"inner join c_elementvalue cev on (cvc.account_id = cev.c_elementvalue_id) where c_validcombination_id = ?";
		
		String sqlVT = "select cev.value from c_validcombination cvc "+
					"inner join c_elementvalue cev on (cvc.account_id = cev.c_elementvalue_id) where c_validcombination_id = ?";	
		
		p_AccountValue_From = DB.getSQLValueString(get_TrxName(), sqlVF, p_Account_ID_From);
		p_AccountValue_To = DB.getSQLValueString(get_TrxName(), sqlVT, p_Account_ID_To);

		Timestamp actual;
		
		java.util.Date date= new java.util.Date();
		actual = (new Timestamp(date.getTime()));
		
		int year = actual.getYear();
		year = year + 1900;
		
		
		/*Date utilDate = (java.util.Date)p_DateAcct_From;;
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(utilDate);
		
		int year = utilDate.getYear();*/
		
		/*String sqlC = "select max(fa.ad_client_id) as ad_client_id , max(fa.ad_org_id) as ad_org_id, max(fa.isactive) as isactive, "+
		"max(fa.created) as created, max(fa.createdby) as createdby, max(fa.updated) as updated, max(fa.updatedby) as updatedby, "+
		"fa.account_id, max(fa.datetrx) as datetrx, max(fa.dateacct) as dateacct, "+
		"sum(fa.amtsourcedr) as amtsourcedrI, sum(fa.amtsourcecr) as amtsourcecrI, "+
		"(select sum(fa2.amtsourcedr) from fact_acct  fa2 "+
		"inner join c_elementvalue ce2 on (fa2.account_id = ce2.c_elementvalue_id) where fa2.dateacct between ? and ? "+
		"and ce2.value between ? and ? and fa2.account_id = fa.account_id "+
		"and fa2.fact_acct_id not in (select fact_acct_id from fact_acct where record_id in "+
		"(select gl_journal_id from gl_journal where description like '%Carga Inicial%') and ad_table_id = 224)) as amtsourcedr, "+
		"(select sum(fa2.amtsourcecr) from fact_acct  fa2 "+
		"inner join c_elementvalue ce2 on (fa2.account_id = ce2.c_elementvalue_id) where fa2.dateacct between ? and ? "+
		"and ce2.value between ? and ? and fa2.account_id = fa.account_id "+
		"and fa2.fact_acct_id not in (select fact_acct_id from fact_acct where record_id in "+
		"(select gl_journal_id from gl_journal where description like '%Carga Inicial%') and ad_table_id = 224) ) as amtsourcecr "+
		"from fact_acct  fa "+
		"inner join c_elementvalue ce on (fa.account_id = ce.c_elementvalue_id) where fa.dateacct between ? and ? "+
		"and ce.value between ? and ? "+
		"and fa.fact_acct_id in (select fact_acct_id from fact_acct where record_id in "+
		"(select gl_journal_id from gl_journal where description like '%Carga Inicial%') "+
		"and ad_table_id = 224) group by fa.account_id order by account_id";*/
		
		//ininoles nuevo select 29-12-2013
		
		String sqlC = "select max(fa.ad_client_id) as ad_client_id , max(fa.ad_org_id) as ad_org_id, max(fa.isactive) as isactive, "+
				"max(fa.created) as created, max(fa.createdby) as createdby, max(fa.updated) as updated, max(fa.updatedby) as updatedby, "+ 
				"fa.account_id, max(fa.datetrx) as datetrx, max(fa.dateacct) as dateacct, "+ 
				"coalesce((select sum(fa2.amtsourcedr) from fact_acct fa2 "+ 
				"inner join c_elementvalue ce2 on (fa2.account_id = ce2.c_elementvalue_id) where EXTRACT(YEAR from fa2.dateacct) = ? "+
				"and fa2.account_id = fa.account_id and fa2.postingtype = fa.postingtype "+
				"and fa2.fact_acct_id in (select fact_acct_id from fact_acct where record_id in "+ 
				"(select gl_journal_id from gl_journal where description like '%Carga Inicial%') and ad_table_id = 224)),0)as amtsourcedrI, "+ 
				"coalesce((select sum(fa2.amtsourcecr) from fact_acct fa2 "+ 
				"inner join c_elementvalue ce2 on (fa2.account_id = ce2.c_elementvalue_id) where EXTRACT(YEAR from fa2.dateacct) = ? "+
				"and fa2.account_id = fa.account_id and fa2.postingtype = fa.postingtype "+
				"and fa2.fact_acct_id in (select fact_acct_id from fact_acct where record_id in "+ 
				"(select gl_journal_id from gl_journal where description like '%Carga Inicial%') and ad_table_id = 224) ),0) as amtsourcecrI, "+ 
				"coalesce((select sum(fa2.amtsourcedr) from fact_acct fa2 "+ 
				"inner join c_elementvalue ce2 on (fa2.account_id = ce2.c_elementvalue_id) where fa2.dateacct between ? and ? "+
				"and fa2.account_id = fa.account_id and fa2.postingtype = fa.postingtype "+
				"and fa2.fact_acct_id not in (select fact_acct_id from fact_acct where record_id in "+ 
				"(select gl_journal_id from gl_journal where description like '%Carga Inicial%') and ad_table_id = 224)),0) as amtsourcedr, "+ 
				"coalesce((select sum(fa2.amtsourcecr) from fact_acct fa2 "+ 
				"inner join c_elementvalue ce2 on (fa2.account_id = ce2.c_elementvalue_id) where fa2.dateacct between ? and ? "+
				"and fa2.account_id = fa.account_id and fa2.postingtype = fa.postingtype "+
				"and fa2.fact_acct_id not in (select fact_acct_id from fact_acct where record_id in "+ 
				"(select gl_journal_id from gl_journal where description like '%Carga Inicial%') and ad_table_id = 224) ),0) as amtsourcecr "+
				"from adempiere.fact_acct  fa "+ 
				"inner join adempiere.c_elementvalue ce on (fa.account_id = ce.c_elementvalue_id) where fa.dateacct between ? and ? "+
				"and ce.value between ? and ? "+ 
				"and fa.postingtype = ? "+
				"group by fa.account_id, fa.postingtype order by account_id";
		
			
			//detalle de cuentas agrupado por cuenta	
			PreparedStatement pstmt = null;
			try
			{				
				//sumatorias de cuenta
				pstmt = DB.prepareStatement(sqlC, get_TrxName());				
				pstmt.setInt(1, year);
				pstmt.setInt(2, year);
				pstmt.setTimestamp(3, p_DateAcct_From);
				pstmt.setTimestamp(4, p_DateAcct_To);
				pstmt.setTimestamp(5, p_DateAcct_From);
				pstmt.setTimestamp(6, p_DateAcct_To);
				pstmt.setTimestamp(7, p_DateAcct_From);
				pstmt.setTimestamp(8, p_DateAcct_To);
				pstmt.setString(9, p_AccountValue_From);
				pstmt.setString(10, p_AccountValue_To);
				pstmt.setString(11, p_Postingtype);
				
				ResultSet rs = pstmt.executeQuery();
				
				String sysdate="TO_DATE('"+TimeUtil.getToday().get(Calendar.YEAR)+"-"+ TimeUtil.getToday().get(Calendar.MONTH)+"-"+ TimeUtil.getToday().get(Calendar.DAY_OF_MONTH)+"','YYYY-MM-DD')";
				
				while (rs.next())
				{

					String InsertC=new String("INSERT INTO T_PresupuestoLGasto(ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
					"account_id,datetrx,dateacct,amtsourcedr,amtsourcecr,amtsourcedri,amtsourcecri,account2_id,postingtype) "+
					"VALUES ("+rs.getInt("AD_Client_ID")+","+rs.getInt("AD_Org_ID")+",'"+rs.getString("IsActive")+"',"+sysdate+","+
					rs.getInt("CreatedBy")+","+sysdate+","+rs.getInt("UpdatedBy")+","+p_Account_ID_From+",'"+
					rs.getTimestamp("datetrx")+"','"+rs.getTimestamp("dateacct")+"',"+rs.getDouble("amtsourcedr")+","+
					rs.getDouble("amtsourcecr")+","+rs.getDouble("amtsourcedrI")+","+rs.getDouble("amtsourcecrI")+","+
					rs.getInt("Account_ID") +",'"+p_Postingtype+"')");
						
					DB.executeUpdate(InsertC, get_TrxName());
				}		
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			
			DB.executeUpdate("UPDATE T_PresupuestoLGasto SET AD_PInstance_ID="+p_PInstance_ID, get_TrxName());
			
			String sqlIns = "UPDATE ad_pinstance_para SET p_number_to="+p_Account_ID_From+"where ad_pinstance_id ="+p_PInstance_ID+
					"and parametername like 'Account_ID'"; 
			DB.executeUpdate(sqlIns, get_TrxName());
		
		return "";
	}	//	doIt
		
}	//	Reporte Presupuesto ley de gasto
