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
import org.compiere.process.*;
 
/**
 *	report balances 8
 *	
 *  @author faaguilar
 *  @version $Id: LibroMayor.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class GOREAnaliticoXCuenta extends SvrProcess
{
	/**	The Order				*/
	
	private Timestamp 	p_DateAcct_From;
	private Timestamp 	p_DateAcct_To;
	private int 	p_Account_ID_From=0;
	private String  p_AccountValue_From = null;
	private int 	p_Account_ID_To=0;
	private String  p_AccountValue_To = null;
	private int 	p_PInstance_ID;

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
		DB.executeUpdate("DELETE FROM T_AnaliticoXCuenta", get_TrxName());
		/*********/
		
		String sqlVF = "select cev.value from c_validcombination cvc "+
					"inner join c_elementvalue cev on (cvc.account_id = cev.c_elementvalue_id) where c_validcombination_id = ?";
		
		String sqlVT = "select cev.value from c_validcombination cvc "+
					"inner join c_elementvalue cev on (cvc.account_id = cev.c_elementvalue_id) where c_validcombination_id = ?";	
		
		p_AccountValue_From = DB.getSQLValueString(get_TrxName(), sqlVF, p_Account_ID_From);
		p_AccountValue_To = DB.getSQLValueString(get_TrxName(), sqlVT, p_Account_ID_To);
		
		
		String sqlD = "select max(fa.ad_client_id) as ad_client_id , max(fa.ad_org_id) as ad_org_id, max(fa.isactive) as isactive, "+
				"max(fa.created) as created, max(fa.createdby) as createdby, max(fa.updated) as updated, max(fa.updatedby) as updatedby, "+
				"fa.account_id, max(fa.datetrx) as datetrx, max(fa.dateacct) as dateacct, max(fa.c_period_id) as c_period_id, "+
				"sum(fa.amtsourcedr) as amtsourcedr, sum(fa.amtsourcecr) as amtsourcecr, sum(fa.amtacctdr) as amtacctdr, "+
				"sum(fa.amtacctcr) as amtacctcr, fa.c_bpartner_id as c_bpartner_id, 1 as orden from fact_acct  fa "+
				"inner join c_elementvalue ce on (fa.account_id = ce.c_elementvalue_id) where fa.dateacct between ? and ? "+
				"and ce.value between ? and ? group by c_bpartner_id, fa.account_id order by account_id ";
		
		String sqlC = "select max(fa.ad_client_id) as ad_client_id , max(fa.ad_org_id) as ad_org_id, max(fa.isactive) as isactive, " +
				" max(fa.created) as created, max(fa.createdby) as createdby, max(fa.updated) as updated, max(fa.updatedby) as updatedby, "+
				"fa.account_id, max(fa.datetrx) as datetrx, max(fa.dateacct) as dateacct, max(fa.c_period_id) as c_period_id, "+
				"sum(fa.amtsourcedr) as amtsourcedr, sum(fa.amtsourcecr) as amtsourcecr, sum(fa.amtacctdr) as amtacctdr, "+
				"sum(fa.amtacctcr) as amtacctcr, max(fa.c_bpartner_id) as c_bpartner_id, 2 as orden from fact_acct  fa "+
				"inner join c_elementvalue ce on (fa.account_id = ce.c_elementvalue_id) where fa.dateacct between ? and ? "+
				"and ce.value between ? and ? group by fa.account_id order by account_id ";
			
			//detalle de cuentas por socio de negocio
			PreparedStatement pstmt = null;
			PreparedStatement pstmt2 = null;
			try
			{
				pstmt = DB.prepareStatement(sqlD, get_TrxName());
				pstmt.setTimestamp(1, p_DateAcct_From);
				pstmt.setTimestamp(2, p_DateAcct_To);
				pstmt.setString(3, p_AccountValue_From);
				pstmt.setString(4, p_AccountValue_To);
				ResultSet rs = pstmt.executeQuery();
				
				String sysdate="TO_DATE('"+TimeUtil.getToday().get(Calendar.YEAR)+"-"+ TimeUtil.getToday().get(Calendar.MONTH)+"-"+ TimeUtil.getToday().get(Calendar.DAY_OF_MONTH)+"','YYYY-MM-DD')";
				
				while (rs.next())
				{
					
					String InsertD=new String("INSERT INTO T_AnaliticoXCuenta(ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
					"account_id,datetrx,dateacct,c_period_id,amtsourcedr,amtsourcecr,amtacctdr,amtacctcr,c_bpartner_id,orden,account2_id) "+
					"VALUES ("+rs.getInt("AD_Client_ID")+","+rs.getInt("AD_Org_ID")+",'"+rs.getString("IsActive")+"',"+sysdate+","+
					rs.getInt("CreatedBy")+","+sysdate+","+rs.getInt("UpdatedBy")+","+p_Account_ID_From+",'"+
					rs.getTimestamp("datetrx")+"','"+rs.getTimestamp("dateacct")+"',"+rs.getInt("c_period_id")+","+rs.getDouble("amtsourcedr")+","+
					rs.getDouble("amtsourcecr")+","+rs.getDouble("amtacctdr")+","+rs.getDouble("amtacctcr")+","+rs.getInt("c_bpartner_id")+","+
					rs.getInt("orden")+","+rs.getInt("Account_ID")+")");
					
					DB.executeUpdate(InsertD, get_TrxName());
				}	
				
				
				//sumatorias de cuenta
				pstmt2 = DB.prepareStatement(sqlC, get_TrxName());
				pstmt2.setTimestamp(1, p_DateAcct_From);
				pstmt2.setTimestamp(2, p_DateAcct_To);
				pstmt2.setString(3, p_AccountValue_From);
				pstmt2.setString(4, p_AccountValue_To);
				ResultSet rs2 = pstmt2.executeQuery();
				while (rs2.next())
				{

					String InsertC=new String("INSERT INTO T_AnaliticoXCuenta(ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
					"account_id,datetrx,dateacct,c_period_id,amtsourcedr,amtsourcecr,amtacctdr,amtacctcr,c_bpartner_id,orden,account2_id) "+
					"VALUES ("+rs2.getInt("AD_Client_ID")+","+rs2.getInt("AD_Org_ID")+",'"+rs2.getString("IsActive")+"',"+sysdate+","+
					rs2.getInt("CreatedBy")+","+sysdate+","+rs2.getInt("UpdatedBy")+","+p_Account_ID_From+",'"+
					rs2.getTimestamp("datetrx")+"','"+rs2.getTimestamp("dateacct")+"',"+rs2.getInt("c_period_id")+","+rs2.getDouble("amtsourcedr")+","+
					rs2.getDouble("amtsourcecr")+","+rs2.getDouble("amtacctdr")+","+rs2.getDouble("amtacctcr")+","+null+","+
					rs2.getInt("orden")+","+rs2.getInt("Account_ID")+")");
						
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
			
			DB.executeUpdate("UPDATE T_AnaliticoXCuenta SET AD_PInstance_ID="+p_PInstance_ID, get_TrxName());
			
			String sqlIns = "UPDATE ad_pinstance_para SET p_number_to="+p_Account_ID_From+"where ad_pinstance_id ="+p_PInstance_ID+
					"and parametername like 'Account_ID'"; 
			DB.executeUpdate(sqlIns, get_TrxName());
			
		return "";
	}	//	doIt
		
}	//	Reporte Analitico de cuenta
