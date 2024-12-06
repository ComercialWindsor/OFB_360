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
package org.compiere.process;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.model.*;
 
/**
 *	Create (Generate) Payment Massive
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessPaymentMassive.java,v 1.2 2006/07/30 00:51:02 $
 */
public class ProcessPaymentMassive extends SvrProcess
{
	/** Properties						*/
	private Properties 		m_ctx;	
	private int p_PaymentMassive_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_PaymentMassive_ID=getRecord_ID();
		m_ctx = Env.getCtx();
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		
		
		
		X_C_PaymentMassive massive= new X_C_PaymentMassive(m_ctx,p_PaymentMassive_ID, get_TrxName());
		MPayment payment = new MPayment (m_ctx,0, get_TrxName());
		
		if(massive.getDocStatus().equals("CO") || massive.getDocStatus().equals("VO"))
			return "";
		
		payment.setAD_Org_ID(massive.getAD_Org_ID());
		payment.setC_BankAccount_ID(massive.getC_BankAccount_ID());
		payment.setC_BPartner_ID(massive.getC_BPartner_ID());
		payment.setPayAmt(massive.getPayAmt());
		payment.setCheckNo(massive.getCheckNo());
		payment.setTenderType(massive.getTenderType());
		payment.setC_Currency_ID(massive.getC_Currency_ID());
		payment.setDateTrx(massive.getDateTrx());
		payment.setC_DocType_ID(massive.getC_DocType_ID());
		if(!payment.save())
			return "No se puede Completar - crear el Pago";
		payment.processIt(DocAction.ACTION_Complete);
		payment.save();
		
		//create allocations
		MAllocationHdr alloc = new MAllocationHdr (Env.getCtx(), true,	//	manual
				massive.getDateTrx(), massive.getC_Currency_ID(), "Payment Massive", get_TrxName());
			alloc.setAD_Org_ID(massive.getAD_Org_ID());
			if (!alloc.save())
			{
				log.log(Level.SEVERE, "Allocation not created");
				return  "Allocation not created";
			}
			X_C_PaymentMassiveLine[] lines=massive.getLines();
			
			if(lines.length==0){
				log.log(Level.SEVERE, "no Lines");
				return  "No Lines";
			}
			
			for(int i=0;i<lines.length;i++){
				
				MAllocationLine aLine = new MAllocationLine (alloc, lines[i].getPayAmt().negate(), 
						Env.ZERO, Env.ZERO, Env.ZERO);
					aLine.setC_Invoice_ID(lines[i].getC_Invoice_ID());
					aLine.setC_Payment_ID(payment.getC_Payment_ID());
					if (!aLine.save())
						log.log(Level.SEVERE, "Allocation Line not written ");
			}
			
		alloc.processIt(DocAction.ACTION_Complete);
		alloc.save();
		//Fin create allocations
		massive.setC_Payment_ID(payment.getC_Payment_ID());
		massive.setDocStatus("CO");
		massive.setProcessed(true);
		massive.save();
		
		commitEx();
		
		return "Pago Creado:" + payment.getDocumentNo();
	}	//	doIt
	
}	//	InvoiceCreateInOut
