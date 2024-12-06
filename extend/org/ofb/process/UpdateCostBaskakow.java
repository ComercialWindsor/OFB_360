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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	report infoprojectPROVECTIS
 *	
 *  @author ininoles
 *  @version $Id: UpdateCostBaskakow.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class UpdateCostBaskakow extends SvrProcess
{
	private int p_PInstance_ID;	

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
		String sqlP = "SELECT obp.C_OrderBP_ID,obp.C_BPartner_Location_ID,col.M_Product_ID FROM C_OrderBP obp "+
				"INNER JOIN C_OrderLine col ON (obp.C_OrderLine_ID = col.C_OrderLine_ID) "+
				"INNER JOIN C_Order co ON (col.C_Order_ID = co.C_Order_ID) " +
				"WHERE co.docstatus in ('CO')";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cant = 0;
		
		try
		{	
			pstmt = DB.prepareStatement(sqlP, get_TrxName());									
			rs = pstmt.executeQuery();
		
			//ciclo de C_OrderBP a actualizar
				
			while (rs.next())
			{
				String sqlUpdate = "UPDATE C_OrderBP SET productcost = (SELECT PriceList FROM C_BPLocatorPrice blp"+
						" WHERE C_BPartner_Location_ID = "+rs.getInt(2)+ 
						" AND M_Product_ID = "+rs.getInt(3)+")"+
						" WHERE C_OrderBP_ID = "+rs.getInt(1);
				
				DB.executeUpdate(sqlUpdate, get_TrxName());
				cant = cant +1;
			}
			
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}	
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		
		return "Se han actualizado "+ cant +" lineas ";
	}	//	doIt
	
}	//	OrderOpen
