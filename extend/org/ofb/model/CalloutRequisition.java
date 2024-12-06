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
package org.ofb.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.*;
import org.compiere.util.DB;

/**
 *	Ignistera Callouts.
 *	
 *  @author Italo Ni�oles
 *  @version $Id: CalloutIgnisterra.java,v 1.5 2014/07/22 
 */
public class CalloutRequisition extends CalloutEngine
{
	/**
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String budget (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//ininoles se cambia productref por product
		String sql="Select M_Product_ID,Qty,PriceActual,M_AttributeSetInstance_ID from EO_Budget where EO_Budget_ID=?";
		try 
		{
			pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1, (Integer)value);											
			rs = pstmt.executeQuery();
			if (rs.next())						
			{
				//ininoles se cambia productref por product
				mTab.setValue("M_Product_ID", rs.getInt("M_Product_ID"));
				mTab.setValue("Qty", rs.getBigDecimal("Qty"));
				mTab.setValue("PriceActual", rs.getBigDecimal("PriceActual"));
				mTab.setValue("M_AttributeSetInstance_ID", rs.getBigDecimal("M_AttributeSetInstance_ID"));//ininoles se agrega atribute set
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
		}
		
		return "";
    }	
	
}	//	CalloutOrder

