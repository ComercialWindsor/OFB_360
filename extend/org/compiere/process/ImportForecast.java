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
 *****************************************************************************/
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MBankAccount;
import org.compiere.model.MForecast;
import org.compiere.model.MForecastLine;
import org.compiere.model.MPayment;
import org.compiere.model.X_I_Payment;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * 	Import Forecast Manufactura
 *	
 *  @author fabian aguilar
 *  @version $Id: ImportForecast.java,v 1.2 2011/02/04 00:51:01 $
 *  
 *  
 */
public class ImportForecast extends SvrProcess
{
	/**	Organization to be imported to	*/
	private int				p_AD_Org_ID = 0;
	/**  WareHouse*/
	private int				p_M_Warehouse_ID = 0;
	/** Default Bank Account			*/
	private boolean			p_deleteOldImported = false;

	private int  M_Forecast_ID = 0;
	/** Properties						*/
	private Properties 		m_ctx;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				p_deleteOldImported = "Y".equals(para[i].getParameter());
			else if (name.equals("M_Forecast_ID"))
				M_Forecast_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		m_ctx = Env.getCtx();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		
		StringBuffer sql;
		int no=0;
		String clientCheck = getWhereClause();
		//	Delete Old Imported
		if (p_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE I_Forecast "
				  + "WHERE I_IsImported='Y'").append (clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated

		sql = new StringBuffer ("UPDATE I_Forecast o "
			+ "SET AD_Org_ID= "+p_AD_Org_ID
			+ "WHERE  I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
		
		//		Set WareHouse
		sql = new StringBuffer ("UPDATE I_Forecast o "
			+ "SET M_Warehouse_ID= "+p_M_Warehouse_ID
			+ "WHERE  I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		
//		Product
		sql = new StringBuffer ("UPDATE I_Forecast o "
			  + "SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
			  + " WHERE o.ProductValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) "
			  + "WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL"
			  + " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Product from Value=" + no);
		
//		Set Period
		sql = new StringBuffer ("UPDATE I_Forecast i "
			+ "SET C_Period_ID=(SELECT MAX(p.C_Period_ID) FROM C_Period p"
			+ " INNER JOIN C_Year y ON (y.C_Year_ID=p.C_Year_ID)"
			+ " INNER JOIN AD_ClientInfo c ON (c.C_Calendar_ID=y.C_Calendar_ID)"
			+ " WHERE c.AD_Client_ID=i.AD_Client_ID"
			+ " AND i.DatePromised BETWEEN p.StartDate AND p.EndDate AND p.IsActive='Y') "
			+ "WHERE C_Period_ID IS NULL"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Period=" + no);
		sql = new StringBuffer ("UPDATE I_Forecast i "
			+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Period, '"
			+ "WHERE C_Period_ID IS NULL OR C_Period_ID NOT IN"
			+ "(SELECT C_Period_ID FROM C_Period p"
			+ " INNER JOIN C_Year y ON (y.C_Year_ID=p.C_Year_ID)"
			+ " INNER JOIN AD_ClientInfo c ON (c.C_Calendar_ID=y.C_Calendar_ID) "
			+ " WHERE c.AD_Client_ID=i.AD_Client_ID"
			+ " AND i.DatePromised BETWEEN p.StartDate AND p.EndDate AND p.IsActive='Y' )"
			+ " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.warning ("Invalid Period=" + no);

		sql = new StringBuffer ("UPDATE I_Forecast o "
				+ "SET SalesRep_ID= "+ Env.getAD_User_ID(m_ctx)
				+ "WHERE  I_IsImported<>'Y'").append (clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (no != 0)
				log.warning ("Invalid user=" + no);

		commitEx();
		
		//Import lines
		sql = new StringBuffer("SELECT * FROM I_Forecast"
			+ " WHERE I_IsImported='N' and M_Product_ID is not null"
			+ " ORDER BY DatePromised");
			
		MForecastLine line = null;
		PreparedStatement pstmt = null;
		int noInsert = 0;
		int noUpdated = 0;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery();
				
			while (rs.next())
			{ 
				int oldline_id = DB.getSQLValue(get_TrxName(),"select m_forecastline_id from m_forecastline where m_forecast_ID=? and datepromised=? and m_product_id=?", new Object[]{M_Forecast_ID,rs.getTimestamp("datepromised"),rs.getInt("M_Product_ID")});
				if(oldline_id>0){
					line = new MForecastLine(Env.getCtx(),oldline_id, get_TrxName());
					noUpdated++;
				}
				else {
					line = new MForecastLine(Env.getCtx(),0, get_TrxName());
					noInsert++;
				}
				
				line.setAD_Org_ID(rs.getInt("AD_Org_ID"));
				line.setC_Period_ID(rs.getInt("C_Period_ID"));
				line.setDatePromised(rs.getTimestamp("DatePromised"));
				line.setM_Forecast_ID(M_Forecast_ID);
				line.setQty(rs.getBigDecimal("Qty"));
				line.setSalesRep_ID(rs.getInt("SalesRep_ID"));
				line.setM_Warehouse_ID(rs.getInt("M_Warehouse_ID"));
				line.setM_Product_ID(rs.getInt("M_Product_ID"));
				line.saveEx();
				
			}
			
			//	Close database connection
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;

		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		
		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_Forecast "
			+ "SET I_IsImported='Y'  "
			+ "WHERE I_IsImported<>'Y' and SalesRep_ID="+Env.getAD_User_ID(m_ctx));
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		addLog (0, null, new BigDecimal (noUpdated), "@Updated@");
		//
		addLog (0, null, new BigDecimal (noInsert), "@Inserted@");
		return "";
	}	//	doIt
	
	//@Override
	public String getWhereClause()
	{
		return " AND AD_Client_ID=" + Env.getAD_Client_ID(m_ctx);
	}
}	//	ImportPayment
