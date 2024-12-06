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
 *	Reporte Contable Generico GORE
 *	
 *  @author faaguilar
 *  @version $Id: LibroMayor.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class GORETempFactAcct extends SvrProcess
{
	/**	The Order				*/
	
	private Timestamp 	p_DateAcct_From;
	private Timestamp 	p_DateAcct_To;
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
		DB.executeUpdate("DELETE FROM T_fact_acct", get_TrxName());
		/*********/
				
		String sqlC = "SELECT ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
					"account_id,datetrx,dateacct,c_period_id,postingtype,c_currency_id, "+
					"amtsourcedr,amtsourcecr,amtacctdr,amtacctcr,c_bpartner_id,ad_orgtrx_id,c_project_id,description "+
					"FROM fact_acct WHERE dateacct between ? and ?";
			
			//Select con parametro de fechas 	
			PreparedStatement pstmt = null;
			try
			{				
				//registros con filtro de fecha
				pstmt = DB.prepareStatement(sqlC, get_TrxName());
				pstmt.setTimestamp(1, p_DateAcct_From);
				pstmt.setTimestamp(2, p_DateAcct_To);				
				
				ResultSet rs = pstmt.executeQuery();
												
				while (rs.next())
				{

					String Insert=new String("INSERT INTO T_Fact_Acct(ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,"+
					"account_id,datetrx,dateacct,c_period_id,postingtype,c_currency_id,"+
					"amtsourcedr,amtsourcecr,amtacctdr,amtacctcr,c_bpartner_id,ad_orgtrx_id,c_project_id,description) "+
					"VALUES ("+rs.getInt("AD_Client_ID")+","+rs.getInt("AD_Org_ID")+",'"+rs.getString("IsActive")+"','"+rs.getTimestamp("Created")+"',"+
					rs.getInt("CreatedBy")+",'"+rs.getTimestamp("Updated")+"',"+rs.getInt("UpdatedBy")+","+rs.getInt("Account_ID")+",'"+
					rs.getTimestamp("datetrx")+"','"+rs.getTimestamp("dateacct")+"',"+rs.getInt("C_Period_ID")+",'"+rs.getString("PostingType")+"',"+
					rs.getInt("C_Currency_ID")+","+rs.getDouble("amtsourcedr")+","+rs.getDouble("amtsourcecr")+","+
					rs.getDouble("AmtAcctdr")+","+rs.getDouble("AmtAcctcr")+","+rs.getInt("C_BPartner_ID")+","+
					rs.getInt("AD_Orgtrx_ID")+","+rs.getInt("C_Project_ID")+",'"+rs.getString("Description")+"')");
						
					DB.executeUpdate(Insert, get_TrxName());
				}		
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			
			DB.executeUpdate("UPDATE T_fact_acct SET AD_PInstance_ID="+p_PInstance_ID, get_TrxName());
		
		return "";
	}	//	doIt
		
}	//	Reporte Contable Generico GORE 
