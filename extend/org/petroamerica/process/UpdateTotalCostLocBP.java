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
package org.petroamerica.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.ofb.utils.DateUtils;

/**
 *
 *	
 */
public class UpdateTotalCostLocBP extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private Timestamp dateEnd;

	protected void prepare()
	{	
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("DateTrx"))
				dateEnd = (Timestamp)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		if(dateEnd == null)
			dateEnd=DateUtils.today();
		String sqlPList = "SELECT C_BPartner_Location_ID FROM C_BPartner_Location WHERE IsActive = 'Y' " +
				" AND DateEnd Is NOT NULL AND ? <= DateEnd AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		try
		{
			pstmt = DB.prepareStatement (sqlPList, get_TrxName());					
			pstmt.setTimestamp(1, dateEnd);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				DB.executeUpdate("UPDATE C_BPartner_Location SET totalCost = totalCost+addAmt " +
						" WHERE C_BPartner_Location_ID = "+rs.getInt("C_BPartner_Location_ID"), get_TrxName());			
				
				DB.executeUpdate("UPDATE C_BPartner_Location SET cost = totalCost/1.19 " +
						" WHERE C_BPartner_Location_ID = "+rs.getInt("C_BPartner_Location_ID"), get_TrxName());
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		finally
		{
			pstmt.close();
			rs.close();	
			pstmt = null;
			rs = null;	
		}		
		return "OK";
	}	//	doIt
}	//	Replenish
