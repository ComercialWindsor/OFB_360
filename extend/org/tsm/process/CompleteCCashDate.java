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
package org.tsm.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MCash;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Replenishment Report
 *	
 *  @author Jorg Janke
 *  @version $Id: ReplenishReport.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 *  
 *  Carlos Ruiz globalqss - integrate bug fixing from Chris Farley
 *    [ 1619517 ] Replenish report fails when no records in m_storage
 */
public class CompleteCCashDate extends SvrProcess
{	
	/** Warehouse				*/
	private Timestamp	p_DateTrxFrom;
	private Timestamp	p_DateTrxTo;
	private int	p_Org_ID;
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
			else if (name.equals("DateTrx"))
			{
				p_DateTrxFrom = (Timestamp)para[i].getParameter();
				p_DateTrxTo = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("AD_Org_ID"))
			{
				p_Org_ID = para[i].getParameterAsInt();
			}
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
		//ininoles solo se ejecutara proceso si ambos parametros estan llenos
		int canthead = 0;
		int cantHeadVoided = 0;
		String sql = "SELECT C_Cash_ID " +
				" FROM C_Cash cc " +
				" WHERE DocStatus IN ('DR','IN') ";
		if(p_Org_ID > 0)
			sql = sql + " AND cc.AD_Org_ID = "+p_Org_ID;
		else
			throw new AdempiereException("ERROR: Debe seleccionar flota");
		if(p_DateTrxFrom != null && p_DateTrxTo != null)
			sql = sql + " AND cc.StatementDate BETWEEN ? AND ? "; 
		else
			throw new AdempiereException("ERROR: Debe seleccionar rango de fechas");
		sql = sql + " ORDER BY C_Cash_ID ";
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setTimestamp(1, p_DateTrxFrom);
			pstmt.setTimestamp(2, p_DateTrxTo);
			rs = pstmt.executeQuery();		
			while (rs.next())
			{
				MCash cash = new MCash(getCtx(), rs.getInt("C_Cash_ID"), get_TrxName());				
				//validacion solo completar fondos fijos
				int cantFF = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_CashLine" +
						" WHERE IsActive = 'Y' AND CashType = 'R' AND C_Cash_ID="+cash.get_ID());
				if(cantFF > 0)
				{
					//si monto es 0 se anula
					BigDecimal amtValid = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(Amount) FROM C_CashLine " +
							" WHERE IsActive = 'Y' AND C_Cash_ID="+cash.get_ID());
					if(amtValid == null || amtValid.compareTo(Env.ZERO) <= 0)
					{
						cash.setDocStatus("DR");
						cash.processIt("VO");
						if(cash.save())
							cantHeadVoided++;
					}
					else
					{
						cash.setDocStatus("DR");
						cash.processIt("CO");
						if(cash.save())
							canthead++;
					}
				}
			}	
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage()+" SQL:"+sql, e);
		}
		finally
		{
			rs.close ();	pstmt.close ();
			pstmt = null;	rs = null;
		}
		return "Se han completado "+canthead+" fondos fijo. Se han anulado "+cantHeadVoided+" fondos fijos";
	}	//	doIt
}	//	Replenish
