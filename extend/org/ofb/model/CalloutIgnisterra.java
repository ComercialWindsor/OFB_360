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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.*;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Msg;

/**
 *	Ignistera Callouts.
 *	
 *  @author Italo Niñoles
 *  @version $Id: CalloutIgnisterra.java,v 1.5 2014/07/22 
 */
public class CalloutIgnisterra extends CalloutEngine
{
	/**	Debug Steps			*/
	private boolean steps = false;

	/**
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String validSO (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		if((Boolean)value == false)
			return "";
		
		int orderID = (Integer)mTab.getValue("C_Order_ID");
		MOrder order = new MOrder(ctx, orderID, null);
		BigDecimal amtLines = (BigDecimal)mTab.getValue("TotalLines");	
		BigDecimal acomparar = new BigDecimal("3000000.0");
		
		if (order.isSOTrx())
		{
			if((Boolean)value)
			{
				mTab.setValue("SentToClient", false);
				if (mTab.get_ValueAsString("Category").equalsIgnoreCase("S")
						&& amtLines.compareTo(acomparar)<=0)
				{
					//no hacer nada
				}
				else 
				{
					mTab.setValue("IsValid", false);
				}
			}
		}		
		return "";
    }	
	public String validSOSTC (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		if((Boolean)value == false)
			return "";
		
		int orderID = (Integer)mTab.getValue("C_Order_ID");
		MOrder order = new MOrder(ctx, orderID, null);
				
		if (order.isSOTrx())
		{
			if((Boolean)value)
			{
				mTab.setValue("ClientReturns", false);				
			}
		}		
		return "";
    }	
	
}	//	CalloutOrder

