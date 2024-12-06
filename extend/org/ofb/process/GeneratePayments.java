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

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *	
 *	
 *  @author Fabian Aguilar
 *  para Colegios PDV
 *  @version $Id: GeneratePayments.java  $
 */

public class GeneratePayments extends SvrProcess 
{
	
	
	private int bankAccount_ID =0;
	private int checkno=0;
	private String banco;
	private String accountNo ="";
	private String tarjeta="";
	private int checkno2=0;
	private String banco2;
	private String tarjeta2="";
	private String accountNo2 ="";
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_BankAccount_ID"))
				bankAccount_ID = para[i].getParameterAsInt();
			else if (name.equals("CheckNo"))
				checkno = para[i].getParameterAsInt();
			else if (name.equals("banco"))
				banco = (String)para[i].getParameter();
			else if (name.equals("CreditCardType"))
				tarjeta = (String)para[i].getParameter();
			else if (name.equals("CheckNo2"))
				checkno2 = para[i].getParameterAsInt();
			else if (name.equals("banco2"))
				banco2 = (String)para[i].getParameter();
			else if (name.equals("CreditCardType2"))
				tarjeta2 = (String)para[i].getParameter();
			else if (name.equals("AccountNo"))
				accountNo = (String)para[i].getParameter();
			else if (name.equals("AccountNo2"))
				accountNo2 = (String)para[i].getParameter();			
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	
	protected String doIt() throws Exception
	{
		MOrder order = new MOrder (Env.getCtx(), getRecord_ID() ,get_TrxName());
		int colegiaturas = 0;
		
		int invoice_ID = DB.getSQLValue(get_TrxName(), "select C_Invoice_ID from C_Invoice where DocStatus IN ('CO','CL')" +
				" and IsSoTRX='Y' and C_Order_ID=?", getRecord_ID());
		int orderLine_ID= order.getLines()[0].getC_OrderLine_ID();
		
		//validaciones
		if(bankAccount_ID==0 && (!order.getPaymentRule().equals("P") || !order.get_ValueAsString("PaymentRule2").equals("P")    ) )
		{
			new AdempiereException("Paga generar los pagos, debe indicar la cuenta bancaria destino");
			return"Paga generar los pagos, debe indicar la cuenta bancaria destino";
		}
		if(invoice_ID<=0)
		{
			new AdempiereException("Paga generar los pagos, esta cotizacion debe tener factura");
			return"Paga generar los pagos, esta cotizacion debe tener factura";
		}
		if(order.getPaymentRule().equals("S") && checkno==0)//cheque
		{
			new AdempiereException("Se debe indicar el numero de cheque para la matricula");
			return"Se debe indicar el numero de cheque para la matricula";
		}
		if(order.get_ValueAsString("PaymentRule2").equals("S") && checkno2==0)//cheque
		{
			new AdempiereException("Se debe indicar el numero de cheque para la colegiatura");
			return"Se debe indicar el numero de cheque para la colegiatura";
		}
		if(order.getPaymentRule().equals("K") && tarjeta.equals(""))//tarjeta credito
		{
			new AdempiereException("Se deben indicar los datos de tarjeta de credito el pago de matricula");
			return"Se deben indicar los datos de tarjeta de credito el pago de matricula";
		}
		if(order.get_ValueAsString("PaymentRule2").equals("K") && tarjeta2.equals(""))//tarjeta credito
		{
			new AdempiereException("Se deben indicar los datos de tarjeta de credito el pago de colegiatura");
			return"Se deben indicar los datos de tarjeta de credito el pago de colegiatura";
		}
		//fin validaciones
		
		MOrderLine[] lines = order.getLines();
		BigDecimal colAmt = Env.ZERO;
		BigDecimal maAmt = Env.ZERO; 
		for (MOrderLine line:lines)
		{
			if(line.getM_Product_ID()>0)
			{
				if(line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("COLEGIATURA")>=0)
					colAmt = colAmt.add(line.getLineNetAmt());
				
				if(line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("MATRÍCULA")>=0 || line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("MATRICULA")>=0)
					maAmt = maAmt.add(line.getLineNetAmt());
				
			}
			else
			{
				if(line.getC_Charge_ID()>0)
				{
					MCharge tcharge = MCharge.get(getCtx(), line.getC_Charge_ID());
					if(tcharge.get_ValueAsString("TipoCargo").equals("TC13"))
						continue;
				}
				colAmt = colAmt.add(line.getLineNetAmt());
				
			}
		}
		
		String tenderM="A";
		if(order.getPaymentRule().equals("S"))//cheque
			tenderM="K";
		else if(order.getPaymentRule().equals("K"))//Credit Card
			tenderM="C";
		else if(order.getPaymentRule().equals("D"))//Direct Debit
			tenderM="D";
		else if(order.getPaymentRule().equals("B"))//Cash
			tenderM="A";
		else if(order.getPaymentRule().equals("T"))//Direct Deposit
			tenderM="A";
		else if(order.getPaymentRule().equals("J"))
			tenderM="T";
		else if(order.getPaymentRule().equals("V"))
			tenderM="V";
			
		String tenderC="A";
		if(order.get_ValueAsString("PaymentRule2").equals("S"))
			tenderC="K";
		else if(order.get_ValueAsString("PaymentRule2").equals("K"))
			tenderC="C";
		else if(order.get_ValueAsString("PaymentRule2").equals("D"))
			tenderC="D";
		else if(order.get_ValueAsString("PaymentRule2").equals("B"))
			tenderC="A";
		else if(order.get_ValueAsString("PaymentRule2").equals("T"))//Direct Deposit
			tenderC="A";
		else if(order.get_ValueAsString("PaymentRule2").equals("J"))
			tenderC="T";
		else if(order.get_ValueAsString("PaymentRule2").equals("V"))
			tenderC="V";
		
		List<MPayment> payments = new ArrayList<MPayment>();
		List<MInvoice> invoices = new ArrayList<MInvoice>();
		//Matriculas
		MPaymentTerm pt = new MPaymentTerm(getCtx(), order.getC_PaymentTerm_ID(), get_TrxName());
		if(!order.getPaymentRule().equals("P")) //distinto de pagare
		{
			BigDecimal remainder = maAmt;
			MPayment pay = null;
			for(MPaySchedule ps:pt.getSchedule(false))
			{
				pay = new MPayment(getCtx(), 0, get_TrxName());
				pay.setC_Order_ID(getRecord_ID());
				pay.setC_Invoice_ID(invoice_ID);
				pay.setIsReceipt(true);
				pay.setC_BankAccount_ID(bankAccount_ID);
				pay.setPayAmt(maAmt.multiply(ps.getPercentage())
						.divide(Env.ONEHUNDRED, 0, BigDecimal.ROUND_HALF_UP));
				pay.setDateTrx(TimeUtil.addDays(order.getDateOrdered(), ps.getNetDays()));
				pay.setC_BPartner_ID(order.getC_BPartner_ID());
				pay.setTenderType(tenderM);
				
				pay.setC_Currency_ID(order.getC_Currency_ID());
				
				if(tenderM.equals("S"))//no existe tender S 
				{
					pay.set_CustomColumn("banco", banco);
					pay.setCheckNo(Integer.toString(checkno));					
					checkno++;
				}
				if(tenderM.equals("K"))//cheque
				{
					pay.set_CustomColumn("banco", banco);
					pay.setCheckNo(Integer.toString(checkno));	
					pay.setCreditCardNumber(accountNo);
				}
				if(tenderM.equals("C"))//Tarjeta de Credito
				{
					pay.set_CustomColumn("banco", banco);
					pay.set_CustomColumn("CreditCardType", tarjeta);
					pay.setCreditCardNumber(accountNo);
				}
				if(tenderM.equals("D"))//Direct Debit
				{
					pay.setCreditCardNumber(accountNo);
					pay.set_CustomColumn("banco", banco);
				}
				if(tenderM.equals("A"))//Direct Deposit
				{
					pay.setCreditCardNumber(accountNo);
					pay.set_CustomColumn("banco", banco);
				}
				pay.setAccountNo(accountNo);
				pay.save();
				remainder = remainder.subtract(pay.getPayAmt());
				
				payments.add(pay);
			}
			
			if (remainder.compareTo(Env.ZERO) != 0 && pay != null)
			{
				pay.setPayAmt(pay.getPayAmt().add(remainder));
				pay.save();
			}
			if(pay==null) // no schedule, al dia
			{
				pay = new MPayment(getCtx(), 0, get_TrxName());
				pay.setC_Order_ID(getRecord_ID());
				pay.setC_Invoice_ID(invoice_ID);
				pay.setIsReceipt(true);
				pay.setC_BankAccount_ID(bankAccount_ID);
				pay.setPayAmt(maAmt);
				pay.setDateTrx(TimeUtil.addDays(order.getDateOrdered(), pt.getNetDays()));
				pay.setC_BPartner_ID(order.getC_BPartner_ID());
				pay.setTenderType(tenderM);
				pay.setC_Currency_ID(order.getC_Currency_ID());
				if(tenderM.equals("K"))
				{
					pay.set_CustomColumn("banco", banco);
					pay.setCheckNo(Integer.toString(checkno));
					checkno++;
				}
				if(tenderM.equals("C"))
				{
					pay.set_CustomColumn("banco", banco);
					pay.set_CustomColumn("CreditCardType", tarjeta);
					pay.setCreditCardNumber(accountNo);
				}
				if(tenderM.equals("D"))//Direct Debit
				{
					pay.setCreditCardNumber(accountNo);
					pay.set_CustomColumn("banco", banco);
				}
				if(tenderM.equals("A"))//Direct Deposit
				{
					pay.setCreditCardNumber(accountNo);
					pay.set_CustomColumn("banco", banco);
				}
				pay.setAccountNo(accountNo);
				pay.save();
				payments.add(pay);
			}
			
		}
		else //pagare matricula
		{
			//MDocType dt = MDocType.get(getCtx(), order.getC_DocType_ID());
			MInvoice invoice = new MInvoice(order, 1000070,order.getDateOrdered());
			invoice.setPaymentRule(order.getPaymentRule());
			invoice.setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
			invoice.setDescription("Pagare Matricula");
			invoice.save();
			
			MInvoiceLine line = new MInvoiceLine(invoice);
			line.setC_OrderLine_ID(orderLine_ID);
			String sql3 = "SELECT C_Charge_ID FROM C_Charge WHERE upper(TipoCargo)='TC10'  and isactive='Y' and AD_client_ID=" +invoice.getAD_Client_ID();
			int C_ChargeChange_ID= DB.getSQLValue("C_Charge",sql3); 
			line.setPrice(maAmt);
			line.setQty(Env.ONE);
			line.setC_Charge_ID(C_ChargeChange_ID);
			line.set_ValueOfColumn("C_InvoiceFac_ID",invoice_ID);
			line.setLineNetAmt();
			line.save();
			
			invoices.add(invoice);
		}
		//Colegiarutas
		pt = new MPaymentTerm(getCtx(), order.get_ValueAsInt("C_PaymentTerm2_ID"), get_TrxName());
		if(!order.get_ValueAsString("PaymentRule2").equals("P")) //distinto de pagare
		{
			BigDecimal remainder = colAmt;
			MPayment pay = null;
			for(MPaySchedule ps:pt.getSchedule(false))
			{
				pay = new MPayment(getCtx(), 0, get_TrxName());
				pay.setC_Order_ID(getRecord_ID());
				pay.setC_Invoice_ID(invoice_ID);
				pay.setIsReceipt(true);
				pay.setC_BankAccount_ID(bankAccount_ID);
				pay.setPayAmt(colAmt.multiply(ps.getPercentage())
						.divide(Env.ONEHUNDRED, 0, BigDecimal.ROUND_HALF_UP));
				String sqlOPS = "select C_OrderPaySchedule_ID from C_OrderPaySchedule where C_Order_ID = ? and c_payschedule_id = ? "; 
				//ininoles se va a buscar fecha de la cuota del mismo PaySchedule
				int OPSchedule_ID = DB.getSQLValue(get_TrxName(), sqlOPS, order.get_ID(),ps.get_ID());				
				X_C_OrderPaySchedule osV = new X_C_OrderPaySchedule(getCtx(), OPSchedule_ID, get_TrxName());
				pay.setDateTrx(osV.getDueDate());
				//ininoles end
				//pay.setDateTrx(TimeUtil.addDays(order.getDateOrdered(), ps.getNetDays()));				
				pay.setC_BPartner_ID(order.getC_BPartner_ID());
				pay.setC_Currency_ID(order.getC_Currency_ID());
				pay.setTenderType(tenderC);
				/*if(tenderC.equals("S"))//no existe S se cambia en codigo de validacion anterior
				{
					pay.set_CustomColumn("banco", banco2);
					pay.setCheckNo(Integer.toString(checkno2));
					checkno2++;
				}*/
				if(tenderC.equals("K"))//cheque
				{  
				    pay.set_CustomColumn("banco", banco2);
					pay.setCheckNo(Integer.toString(checkno2));
					checkno2++;
				}
				if(tenderC.equals("C"))//tarjeta de credito
				{
					pay.set_CustomColumn("banco", banco2);
				    pay.set_CustomColumn("CreditCardType", tarjeta2);
				    pay.setCreditCardNumber(accountNo2);
				}
				if(tenderC.equals("D"))//debito directo
				{  
				    pay.setCreditCardNumber(accountNo2);
				    pay.set_CustomColumn("CreditCardType", tarjeta2);
				    pay.set_CustomColumn("banco", banco2);
				}
				if(tenderM.equals("A"))//Direct Deposit
				{	
					pay.set_CustomColumn("banco", banco2);
				}
				
				pay.setAccountNo(accountNo2);
				pay.save();
				remainder = remainder.subtract(pay.getPayAmt());
				
				payments.add(pay);
			}
			
			if (remainder.compareTo(Env.ZERO) != 0 && pay != null)
			{
				pay.setPayAmt(pay.getPayAmt().add(remainder));
				pay.save();
			}
			if(pay==null) // no schedule, al dia
			{
				pay = new MPayment(getCtx(), 0, get_TrxName());
				pay.setC_Order_ID(getRecord_ID());
				pay.setC_Invoice_ID(invoice_ID);
				pay.setIsReceipt(true);
				pay.setC_BankAccount_ID(bankAccount_ID);
				pay.setPayAmt(colAmt);
				pay.setDateTrx(TimeUtil.addDays(order.getDateOrdered(), pt.getNetDays()));
				pay.setC_BPartner_ID(order.getC_BPartner_ID());
				pay.setC_Currency_ID(order.getC_Currency_ID());
				pay.setTenderType(tenderC);
				if(tenderC.equals("K"))
				{
					pay.set_CustomColumn("banco", banco2);
					pay.setCheckNo(Integer.toString(checkno2));
					checkno2++;
				}
				if(tenderC.equals("C"))//tarjeta de credito
				{
				    pay.set_CustomColumn("CreditCardType", tarjeta2);
				    pay.set_CustomColumn("banco", banco2);
				    pay.setCreditCardNumber(accountNo2);
				}
				if(tenderC.equals("D"))//debito directo
				{  
				    pay.setCreditCardNumber(accountNo2);
				    pay.set_CustomColumn("CreditCardType", tarjeta2);
				    pay.set_CustomColumn("banco", banco2);
				}
				if(tenderM.equals("A"))//Direct Deposit
				{
					pay.set_CustomColumn("banco", banco2);
				}
				pay.setAccountNo(accountNo2);
				pay.save();
				payments.add(pay);
			}
		}
		else //pagare  - agregar cargo de 2,6UF
		{
			//MDocType dt = MDocType.get(getCtx(), order.getC_DocType_ID());
			MInvoice invoice = new MInvoice(order, 1000070,order.getDateOrdered());
			invoice.setPaymentRule(order.get_ValueAsString("PaymentRule2"));
			invoice.setC_PaymentTerm_ID(order.get_ValueAsInt("C_PaymentTerm2_ID"));
			invoice.setDescription("Pagare Colegiatura");
			invoice.save();
			
			MInvoiceLine line = new MInvoiceLine(invoice);
			line.setC_OrderLine_ID(orderLine_ID);
			String sql3 = "SELECT C_Charge_ID FROM C_Charge WHERE upper(TipoCargo)='TC10'  and isactive='Y' and AD_client_ID=" +invoice.getAD_Client_ID();
			int C_ChargeChange_ID= DB.getSQLValue("C_Charge",sql3); 
			line.setPrice(colAmt);
			line.setQty(Env.ONE);
			line.setC_Charge_ID(C_ChargeChange_ID);
			line.set_ValueOfColumn("C_InvoiceFac_ID",invoice_ID);
			line.setLineNetAmt();
			line.save();
			
			//cargo pagare 2,3 UF
			for (MOrderLine oline:lines)
			{
				if(oline.getC_Charge_ID()>0)//copiar
				{
					MCharge tcharge = MCharge.get(getCtx(), oline.getC_Charge_ID());
					if(tcharge.get_ValueAsString("TipoCargo").equals("TC13"))
					{
						MInvoiceLine line2 = new MInvoiceLine(invoice);
						line2.setOrderLine(oline);
						line2.setQty(oline.getQtyOrdered());
						line2.setC_OrderLine_ID(0);
						line2.save();
					}
				}
				
			}
			
			invoices.add(invoice);
			
		}
		
		
		for(MPayment p:payments)
		{
			p.processIt(MPayment.ACTION_Complete);
			p.setProcessed(true);
			p.save();
		}
		
		for(MInvoice p:invoices)
		{
			p.processIt(MInvoice.ACTION_Complete);
			p.setProcessed(true);
			p.save();
		}
		
		return "Pagos Generados: "+payments.size() + " Pagares generados: "+ invoices.size();
	}	
	
	
	
}
