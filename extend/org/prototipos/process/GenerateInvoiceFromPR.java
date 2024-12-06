/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.prototipos.process;

import java.math.BigDecimal;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessPaymentRequest.java,v 1.2 2011/06/12 00:51:01  $
 */
public class GenerateInvoiceFromPR extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int	Record_ID;
	private String	p_DocumentNo; 
		
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DocumentNo"))
				p_DocumentNo = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		Record_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		X_C_PaymentRequest pr = new X_C_PaymentRequest(Env.getCtx(), Record_ID, get_TrxName());
		String requestType = pr.getRequestType();
		requestType = Character.toString(requestType.charAt(0));
		
		if (pr.get_ValueAsInt("C_Invoice_ID") > 0)
		{
			log.log(Level.SEVERE, "Invoice Already Generated");
			throw new IllegalArgumentException("Factura ya generada");		
		}
		
		if (requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion) && pr.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_WaitingConfirmation))
		{
			MInvoice inv = new MInvoice(getCtx(), 0, get_TrxName());
			inv.setClientOrg(pr.getAD_Client_ID(), pr.getAD_Org_ID());
			inv.setIsSOTrx(false);
			MBPartner bp = new MBPartner(getCtx(), pr.getC_BPartner_ID(), get_TrxName());
			inv.setBPartner(bp);
			inv.setDocumentNo(p_DocumentNo);
			int id_PriceList = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_PriceList_ID) FROM M_PriceList WHERE IsDefault = 'Y' AND IsSOPriceList = 'N'");			
			inv.setM_PriceList_ID(id_PriceList);
			inv.setC_Currency_ID(pr.getC_Currency_ID());
			inv.setDateInvoiced(pr.getDateTrx());
			inv.setDateAcct(pr.getDateAcct());
			inv.setDescription("Generado Automaticamente desde Solicitud de Pago "+pr.getDocumentNo());
			
			if (!inv.save())
				throw new IllegalArgumentException("Cannot save Invoice");
			
			//generacion de linea cargo1
			MInvoiceLine iLine = new MInvoiceLine(inv);
			iLine.setC_Invoice_ID(inv.get_ID());
			iLine.setC_Charge_ID(pr.getC_Charge_ID());
			
			iLine.setQtyEntered(Env.ONE);
			iLine.setQtyInvoiced(Env.ONE);
			
			iLine.setPriceActual(pr.getPayAmt());//faaguilar OFB
			iLine.setPriceEntered(pr.getPayAmt());
			int id_Tax = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Tax_ID) FROM C_Tax WHERE IsTaxExempt = 'N' AND IsDefault='Y'");
			iLine.setC_Tax_ID(id_Tax);
			
			if (!iLine.save())
				throw new IllegalArgumentException("Cannot save Invoice Line");
			
			//generacion de linea cargo2
			MInvoiceLine iLine2 = new MInvoiceLine(inv);
			iLine2.setC_Invoice_ID(inv.get_ID());
			
			int ID_Charge2 = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Charge_ID) FROM C_Charge cc INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID)"+ 
					" WHERE ct.value = 'TCET'");
			iLine2.setC_Charge_ID(ID_Charge2);
			MCharge charge2 = new MCharge(getCtx(), ID_Charge2, get_TrxName());
						
			iLine2.setQtyEntered(Env.ONE);
			iLine2.setQtyInvoiced(Env.ONE);
			BigDecimal amtLine2 = (pr.getPayAmt().divide(Env.ONEHUNDRED)).multiply(charge2.getChargeAmt());
			
			iLine2.setPriceActual(amtLine2.negate());
			iLine2.setPriceEntered(amtLine2.negate());
			int id_TaxEx = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Tax_ID) FROM C_Tax WHERE IsTaxExempt = 'Y'");
			iLine2.setC_Tax_ID(id_TaxEx);
			
			if (!iLine2.save())
				throw new IllegalArgumentException("Cannot save Invoice Line");
			
			//generacion de linea cargo3
			MInvoiceLine iLine3 = new MInvoiceLine(inv);
			iLine3.setC_Invoice_ID(inv.get_ID());
			
			int ID_Charge3 = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Charge_ID) FROM C_Charge cc INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID)"+ 
					" WHERE ct.value = 'TCICA'");
			iLine3.setC_Charge_ID(ID_Charge3);
			MCharge charge3 = new MCharge(getCtx(), ID_Charge3, get_TrxName());
						
			iLine3.setQtyEntered(Env.ONE);
			iLine3.setQtyInvoiced(Env.ONE);
			BigDecimal amtLine3 = (pr.getPayAmt().divide(Env.ONEHUNDRED)).multiply(charge3.getChargeAmt());
			
			iLine3.setPriceActual(amtLine3.negate());
			iLine3.setPriceEntered(amtLine3.negate());			
			iLine3.setC_Tax_ID(id_TaxEx);
			
			if (!iLine3.save())
				throw new IllegalArgumentException("Cannot save Invoice Line");
			
			inv.calculateTaxTotal();
			inv.save();			
			//commitEx();
			
			pr.set_CustomColumn("C_Invoice_ID", inv.get_ID());
			pr.setPayAmt(inv.getGrandTotal());
			pr.save();
		}
			
		return "Factura Generada";
		
	}	//	doIt
}	//	CopyOrder
