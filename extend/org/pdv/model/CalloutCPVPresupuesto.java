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
package org.pdv.model;

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
public class CalloutCPVPresupuesto extends CalloutEngine
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
	public String AmtBPartner (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
				
		int ID_Product = (Integer)mTab.getValue("M_Product_ID");
		if (ID_Product > 0 )
		{
			String sqlAmt = "SELECT MAX(PriceList) FROM M_Product_PO WHERE M_Product_ID = ? AND IsCurrentVendor = 'Y'";
			BigDecimal amt = DB.getSQLValueBD(null, sqlAmt, ID_Product);
			String sqlBP = "SELECT MAX(C_BPartner_ID) FROM M_Product_PO WHERE M_Product_ID = ? AND IsCurrentVendor = 'Y'";
			int ID_BPartner = DB.getSQLValue(null, sqlBP, ID_Product);
			if (amt != null)
			{
				if (amt.compareTo(Env.ZERO) > 0)
				{
					mTab.setValue("PriceActual", amt);
				}
			}
			if (ID_BPartner > 0)
			{
				mTab.setValue("C_Bpartner_ID", ID_BPartner);
			}
		}
		
		
		return "";
    }	
}	//	CalloutOrder

