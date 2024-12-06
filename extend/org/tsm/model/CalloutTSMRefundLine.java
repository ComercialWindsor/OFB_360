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
package org.tsm.model;

import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.model.*;
import org.compiere.util.DB;

/**
 *	TSM Callouts.
 *	
 *  @author Italo Niñoles
 *  @version $Id: CalloutIgnisterra.java,v 1.5 2015/12/09 
 */
public class CalloutTSMRefundLine extends CalloutEngine
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
	public String amt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";

		String amtStr = DB.getSQLValueString(null,"SELECT rl.Description FROM AD_Ref_List rl " +
				" INNER JOIN AD_Reference ref ON (rl.ad_reference_id = ref.ad_reference_id)" +
				" WHERE ref.name like 'Description Refund' AND rl.IsActive = 'Y' AND Value = '"+value+"'");
		if(amtStr != null && amtStr.trim() != "")
		{
			amtStr = amtStr.replaceAll(",","");
			amtStr = amtStr+".0" ;
			BigDecimal amt = new BigDecimal(amtStr);
			if(amt != null)
				mTab.setValue("Amt", amt);
		}		
		return "";
    }
	public String amtNew (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";

		BigDecimal amt = DB.getSQLValueBD(null,"SELECT amt FROM TP_RefundAmt " +
				" WHERE TP_RefundAmt_ID = "+value);
		if(amt != null)
				mTab.setValue("Amt", amt);	
		return "";
    }
}	//	CalloutOrder

