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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	CopyFromJobStandar
 *	
 */
public class UpdateBPCreditUF extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */

	protected void prepare()
	{	

	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		String sqlPList = "SELECT C_BPartner_ID FROM C_BPartner WHERE IsActive = 'Y' " +
				" AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		try
		{
			pstmt = DB.prepareStatement (sqlPList, get_TrxName());					
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MBPartner bp = new MBPartner(getCtx(), rs.getInt("C_BPartner_ID"), get_TrxName());
				int ID_CLF = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Currency_ID) FROM C_Currency WHERE CurSymbol = 'CLF'");
				
				String sqlAmt = "SELECT currencyconvert(montouf,"+ID_CLF+",228,now(),114,AD_Client_ID,AD_Org_ID), C_Bpartner_ID, montouf" +
						" FROM C_BPartner bp WHERE C_BPartner_ID = "+bp.get_ID();
				BigDecimal amtConvert = DB.getSQLValueBD(get_TrxName(), sqlAmt);
				if(amtConvert != null && amtConvert.compareTo(Env.ZERO) > 0)
				{
					/*bp.set_CustomColumn("OwnCreditLimit", amtConvert);
					bp.save();*/
					//ininoles se actualiza credito externo por DB para que no ejecute otros model
					BigDecimal param[] = new BigDecimal[1];
					param[0] = amtConvert;
					DB.executeUpdateEx("UPDATE C_BPartner SET ExternalCreditLimit = ? WHERE C_BPartner_ID = "+bp.get_ID(),param,get_TrxName());					
				}
				Timestamp dateStartCredit = (Timestamp)bp.get_Value("StartDateCreditLimit");
				Timestamp dateEndCredit = (Timestamp)bp.get_Value("EndDateCreditLimit");
				//Calendar calendar = Calendar.getInstance();
				Timestamp today = new Timestamp(Calendar.getInstance().getTimeInMillis());
				if(dateStartCredit != null && dateStartCredit.compareTo(today) > 0)
					bp.setSOCreditStatus("S");
				if(dateEndCredit != null && dateEndCredit.compareTo(today) < 0)
					bp.setSOCreditStatus("S");				
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
