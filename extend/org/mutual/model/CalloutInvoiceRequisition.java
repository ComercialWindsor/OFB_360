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
package org.mutual.model;

import java.math.BigDecimal;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	mutual Callouts.
 *	
 *  @author Italo Ni�oles
 *  @version $Id: CalloutInvoiceRequisition.java,v 1.5 2015/10/25 
 */
public class CalloutInvoiceRequisition extends CalloutEngine
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
	public String Invoice (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		int ID_Invoice = (Integer)value;
		MInvoice inv = new MInvoice(ctx, ID_Invoice, null);
		mTab.setValue("AD_Client_ID", inv.getAD_Client_ID());
		mTab.setValue("AD_Org_ID", inv.getAD_Org_ID());
		mTab.setValue("C_BPartner_ID", inv.getC_BPartner_ID());
		mTab.setValue("C_BPartner_Location_ID", inv.getC_BPartner_Location_ID());
		mTab.setValue("AD_User_ID", inv.getAD_User_ID());
		mTab.setValue("M_PriceList_ID", inv.getM_PriceList_ID());
		mTab.setValue("C_Currency_ID", inv.getC_Currency_ID());
		mTab.setValue("PaymentRule", inv.getPaymentRule());
		mTab.setValue("C_PaymentTerm_ID", inv.getC_PaymentTerm_ID());
		mTab.setValue("SalesRep_ID", inv.getSalesRep_ID());		
		return "";
    }
	public String InvoiceLine (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		int ID_InvoiceLine = (Integer)value;
		MInvoiceLine invLine = new MInvoiceLine(ctx, ID_InvoiceLine, null);
		mTab.setValue("AD_Client_ID", invLine.getAD_Client_ID());
		mTab.setValue("AD_Org_ID", invLine.getAD_Org_ID());
		mTab.setValue("M_Product_ID", invLine.getM_Product_ID());
		//validacion cargo
		int ID_Charge = invLine.getC_Charge_ID();
		if (ID_Charge > 0)
			mTab.setValue("C_Charge_ID", invLine.getC_Charge_ID());
		mTab.setValue("M_AttributeSetInstance_ID", invLine.getM_AttributeSetInstance_ID());
		//ininoles calculamos saldo de la linea / se validara por ticket no por cantidad
		/*BigDecimal qty = DB.getSQLValueBD(null, "SELECT SUM(QtyEntered) FROM C_InvoiceLine cil" +
				" INNER JOIN C_Invoice ci ON (cil.C_Invoice_ID = ci.C_Invoice_ID)" +
				" WHERE cil.Ref_InvoiceLine_ID = ? AND ci.DocStatus IN ('CO','DR','IP')", ID_InvoiceLine);
		if (qty == null)
			qty = Env.ZERO;*/
		//if (qty.compareTo(Env.ZERO) == 0)
		mTab.setValue("QtyEntered", invLine.getQtyEntered());
		//else
			//mTab.setValue("QtyEntered", invLine.getQtyEntered().subtract(qty));
		//ininles end
		mTab.setValue("Description", invLine.getDescription());
		mTab.setValue("C_UOM_ID", invLine.getC_UOM_ID());
		mTab.setValue("PriceEntered", invLine.getPriceEntered());
		mTab.setValue("PriceActual", invLine.getPriceActual());
		mTab.setValue("PriceList", invLine.getPriceList());
		mTab.setValue("C_Tax_ID", invLine.getC_Tax_ID());
		
		return "";
    }	
	public String InvoiceLineNC (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		int ID_InvoiceLine = (Integer)value;
		MInvoiceLine invLine = new MInvoiceLine(ctx, ID_InvoiceLine, null);
		mTab.setValue("AD_Client_ID", invLine.getAD_Client_ID());
		mTab.setValue("AD_Org_ID", invLine.getAD_Org_ID());
		mTab.setValue("M_Product_ID", invLine.getM_Product_ID());
		//validacion cargo
		int ID_Charge = invLine.getC_Charge_ID();
		if (ID_Charge > 0)
			mTab.setValue("C_Charge_ID", invLine.getC_Charge_ID());
		mTab.setValue("M_AttributeSetInstance_ID", invLine.getM_AttributeSetInstance_ID());
		//ininoles calculamos saldo de la linea
		BigDecimal qty = DB.getSQLValueBD(null, "SELECT SUM(QtyEntered) FROM C_InvoiceLine WHERE Ref_InvoiceReqLine_ID = ?", ID_InvoiceLine);
		if (qty == null)
			qty = Env.ZERO;
		if (qty.compareTo(Env.ZERO) == 0)
			mTab.setValue("QtyEntered", invLine.getQtyEntered());
		else
			mTab.setValue("QtyEntered", invLine.getQtyEntered().subtract(qty));
		//ininles end
		mTab.setValue("Description", invLine.getDescription());
		mTab.setValue("C_UOM_ID", invLine.getC_UOM_ID());
		mTab.setValue("PriceEntered", invLine.getPriceEntered());
		mTab.setValue("PriceActual", invLine.getPriceActual());
		mTab.setValue("PriceList", invLine.getPriceList());
		mTab.setValue("C_Tax_ID", invLine.getC_Tax_ID());
		
		return "";
    }
	
}	//	CalloutOrder

