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

import org.compiere.model.MPriceList;
import org.compiere.model.MPriceListVersion;
import org.compiere.model.MProductPrice;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	CopyFromJobStandar
 *	
 */
public class CopyPListVersion extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_PListVersion_ID;
		
	protected void prepare()
	{	
		p_PListVersion_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		MPriceListVersion pListV = new MPriceListVersion(getCtx(), p_PListVersion_ID, get_TrxName());
		String sqlPList = "SELECT M_PriceList_ID FROM M_PriceList WHERE IsActive = 'Y' " +
				" AND AD_Client_ID = "+pListV.getAD_Client_ID()+" AND M_PriceList_ID <> "+pListV.getM_PriceList_ID()+
				" AND IsSOPriceList = 'Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		Timestamp today = new Timestamp(Calendar.getInstance().getTimeInMillis());
		try
		{
			pstmt = DB.prepareStatement (sqlPList, get_TrxName());					
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MPriceList pList = new MPriceList(getCtx(), rs.getInt("M_PriceList_ID"), get_TrxName());
				//buscamos ultima lista de precios para sacar productos a posterior antes de guardar la nueva
				String sql = "SELECT plv.M_PriceList_Version_ID "
					+ "FROM M_PriceList_Version plv "
					+ "WHERE plv.M_PriceList_ID=? "						//	1
					+ " AND plv.ValidFrom <= ? "
					+ "ORDER BY plv.ValidFrom DESC";				
				int oldPLVersion_ID = DB.getSQLValueEx(null, sql, pList.get_ID(), today);				
				//se genera version de lista de precios nueva
				MPriceListVersion pListVNew = new MPriceListVersion(pList);						
				pListVNew.setAD_Org_ID(pList.getAD_Org_ID());
				pListVNew.setName(pList.getName()+" - "+today);
				pListVNew.setDescription("Generado automaticamente");
				pListVNew.setValidFrom(today);
				pListVNew.setM_DiscountSchema_ID(pListV.getM_DiscountSchema_ID());
				pListVNew.save();				
				//agregamos los productos
				String sqlProd = "SELECT m_product_ID FROM M_ProductPrice " +
						" WHERE M_PriceList_Version_ID = " + oldPLVersion_ID;
				PreparedStatement pstmtProd = null;
				ResultSet rsProd = null;	
				try
				{
					pstmtProd = DB.prepareStatement (sqlProd, get_TrxName());					
					rsProd = pstmtProd.executeQuery ();
					BigDecimal price9 = new BigDecimal("9999.0"); 
					while (rsProd.next ())
					{	
						MProductPrice ppNew = new MProductPrice(getCtx(), pListVNew.get_ID(), rsProd.getInt("M_Product_ID"),get_TrxName());
						ppNew.setPriceList(price9);
						ppNew.setPriceStd(price9);
						//ppNew.setPriceLimit(price9);
						ppNew.setPriceLimit(Env.ONE);
						ppNew.set_CustomColumn("Variabletax", price9);
						ppNew.set_CustomColumn("fixedtax", price9);
						ppNew.save();
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
				finally
				{
					pstmtProd.close();
					rsProd.close();	
					pstmtProd = null;
					rsProd = null;	
				}
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
