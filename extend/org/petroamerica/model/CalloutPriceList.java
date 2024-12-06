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
import org.compiere.util.Env;

/**
 *	Order Callouts.
 *	
 *  @author Jorg Janke
 *  @version $Id: CalloutOrder.java,v 1.5 2006/10/08 06:57:33 comdivision Exp $
 */
public class CalloutPriceList extends CalloutEngine
{
	/**
	 *	Order Header Change - DocType.
	 *		- InvoiceRuld/DeliveryRule/PaymentRule
	 *		- temporary Document
	 *  Context:
	 *  	- DocSubTypeSO
	 *		- HasCharges
	 *	- (re-sets Business Partner info of required)
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	//
	public String calculatePrice (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (value == null)
			return "";
		//se leen valores
		BigDecimal amt = (BigDecimal)mTab.getValue("Price");
		BigDecimal fixedTax = (BigDecimal)mTab.getValue("FixedTax");
		if(fixedTax == null)
			fixedTax = Env.ZERO;
		BigDecimal variableTax = (BigDecimal)mTab.getValue("VariableTax");
		if(variableTax == null)
			variableTax = Env.ZERO;
		//MPriceListVersion plv = new MPriceListVersion(ctx,(Integer)mTab.getValue("M_PriceList_Version_ID"), null);
		if(amt != null && amt.compareTo(Env.ONE) > 0)
		{
			//se restan impuestos espesificos
			if(fixedTax != null)
				amt = amt.subtract(fixedTax);
			if(variableTax != null)
				amt = amt.subtract(variableTax);
			//se calcula y descuenta IVA
			BigDecimal tax = new BigDecimal("1.19");
			amt = amt.divide(tax, 10, RoundingMode.HALF_EVEN);
			mTab.setValue("PriceList", amt);
			mTab.setValue("PriceStd", amt);
			mTab.setValue("PriceLimit", amt.subtract(Env.ONEHUNDRED));
		}
		//
		return "";
	}	//	qty

	
	
}	//	CalloutOrder

