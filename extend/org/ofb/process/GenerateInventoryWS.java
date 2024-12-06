/******************************************************************************
0 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	Genera  Inventario desde WS
 *	Process_ID = 1000450
 *  AD_Menu_ID=1000497
 *  @author fabian aguilar
 *  @version $Id: GenerateInvMoveWS.java,v 1.2 2014/09/12 $
 */
public class GenerateInventoryWS extends SvrProcess
{
	private Properties 		m_ctx;	
	
	private String p_IMEI;
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
			else if (name.equals("IMEI"))
				p_IMEI = (String) para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		m_ctx = Env.getCtx();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		/*********/

		
		String sql = "Select * from M_INVENTORYLINETEMP where PROCESSED='N' and IMEI='"+p_IMEI+"'" ;
		
		MInventory inv = null;
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{	
				if(inv==null)
				{
					inv = new MInventory(m_ctx, 0, this.get_TrxName());
					inv.setDescription("Desde WS");
					inv.setM_Warehouse_ID(rs.getInt("M_WAREHOUSE_ID"));
					inv.setC_DocType_ID(1000023);
					
					inv.save();
				}
				
				MInventoryLine line = new MInventoryLine(m_ctx, 0, this.get_TrxName());
				line.setM_Inventory_ID(inv.getM_Inventory_ID());
				line.setM_Locator_ID(rs.getInt("M_LOCATOR_ID"));
				line.setM_Product_ID(rs.getInt("M_PRODUCT_ID"));
				line.setQtyCount(rs.getBigDecimal("QTY"));
				line.set_ValueOfColumn("TEMPLINE_ID", rs.getInt("M_INVENTORYLINETEMP_ID"));
				line.save();
				
			}
			
			rs.close();
			pstmt.close();
			DB.close(rs, pstmt);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		if(inv!=null)
		{
			
			DB.executeUpdate("Update M_INVENTORYLINETEMP set PROCESSED='Y' Where M_INVENTORYLINETEMP_ID "
					+ " IN (select TEMPLINE_ID from M_InventoryLine where M_Inventory_ID="+inv.getM_Inventory_ID() +") ", this.get_TrxName());
			
			return "Generado :"+ inv.getDocumentNo();
		}
		else
			return "No Generado";
	}	//	doIt
	
}	//	OrderOpen
