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
package org.petroamerica.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
/**
 *	PA Callouts.
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutOrder.java,v 1.5 2006/10/08 06:57:33 comdivision Exp $
 */
public class CalloutBPartLocationCost extends CalloutEngine
{
	//ininoles OFB PA
	public String calculateCost (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (value == null)
			return "";
		//se leen valores
		BigDecimal totalCost = (BigDecimal)mTab.getValue("TotalCost");
		//solo si valor no es nulo se calculara el costo
		if(totalCost != null)
		{
			BigDecimal tax = new BigDecimal("1.19");
			BigDecimal amt =  null;
			amt = totalCost.divide(tax,10,RoundingMode.HALF_EVEN);
			if(amt != null)
				mTab.setValue("Cost", amt);
		}
		//
		return "";
	}	//	qty

	
	
}	//	CalloutOrder

