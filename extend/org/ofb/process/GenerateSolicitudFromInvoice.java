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

package org.ofb.process;

import java.math.BigDecimal;

import org.compiere.model.*;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author faaguilar OFB
 *  @version $Id: GenerateSolicitudFromInvoice.java,v 1.2 2011/06/12 00:51:01  $
 */

public class GenerateSolicitudFromInvoice extends SvrProcess 
{
	
	private int p_Invoice_ID = 0;
	
	protected void prepare()
	{
		
		p_Invoice_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	
	protected String doIt() throws Exception
	{
		if(p_Invoice_ID == 0)
			return "";
		
		MInvoice in = new MInvoice(getCtx(), p_Invoice_ID, get_TrxName());
		BigDecimal open = DB.getSQLValueBD(get_TrxName(), "Select invoiceopen(?,null) from C_Invoice where C_Invoice_ID="+in.getC_Invoice_ID(), in.getC_Invoice_ID());
		if(open.signum()==0)
			return "no posee monto abierto";
		
		int  C_BankAccount_ID = DB.getSQLValue(get_TrxName(), "select C_BankAccount_ID from C_BankAccount where AD_Org_ID=?", in.getAD_Org_ID());
		if(C_BankAccount_ID<=0)
			return "no se encuentra la cuenta bancaria para generar la solicitud";
		
		
		X_C_PaymentRequest pr = new X_C_PaymentRequest(getCtx(), 0, get_TrxName());
		pr.setAD_Org_ID(in.getAD_Org_ID());
		pr.setC_BankAccount_ID(C_BankAccount_ID);
		pr.setC_BPartner_ID(in.getC_BPartner_ID());
		pr.setDateTrx(TimeUtil.getDay(0));
		pr.setDateAcct(TimeUtil.getDay(0));
		pr.setRequestType("I");
		pr.setTenderType("K");
		pr.setC_Currency_ID(in.getC_Currency_ID());
		pr.setPayAmt(open);
		pr.setDocStatus("DR");
		pr.setDescription("Solicitud generada automaticamente");
		pr.save();
		
		X_C_PaymentRequestLine line = new X_C_PaymentRequestLine(getCtx(), 0, get_TrxName());
		line.setC_PaymentRequest_ID(pr.getC_PaymentRequest_ID());
		line.setC_Invoice_ID(p_Invoice_ID);
		line.setAmt(in.getGrandTotal());
		line.save();
		
		return "Solicitud :" + pr.getDocumentNo() + " Generada";
		
	}
		
}
