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
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.MBPartner;
import org.compiere.model.MBankStatement;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for PA
 *
 *  @author Italo Niñoles
 */
public class ModPAUpdateCreditBP implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPAUpdateCreditBP ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPAUpdateCreditBP.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;
	

	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing Model Price Validator: "+this.toString());
		}

		//	Tables to be monitored
		engine.addModelChange(MBPartner.Table_Name, this);
		//doc to be monitored
		//engine.addDocValidate(MInOut.Table_Name, this);
		engine.addDocValidate(MOrder.Table_Name, this);
		engine.addDocValidate(MPayment.Table_Name, this);
		engine.addDocValidate(MInvoice.Table_Name, this);
		engine.addDocValidate(MBankStatement.Table_Name, this);		
		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if(type == TYPE_AFTER_NEW && po.get_Table_ID()==MBPartner.Table_ID)
		{
			MBPartner bp = (MBPartner)po;
			BigDecimal amtTotal = Env.ZERO;
			BigDecimal ownCredit = (BigDecimal)bp.get_Value("OwnCreditLimit");
			BigDecimal extCredit = (BigDecimal)bp.get_Value("ExternalCreditLimit");
			BigDecimal creditUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
			if(ownCredit != null)
				amtTotal = ownCredit;
			if(extCredit != null)
				amtTotal = amtTotal.add(extCredit);
			if(amtTotal == null)
				amtTotal = Env.ZERO;
			if(creditUsed != null)
				amtTotal = amtTotal.subtract(creditUsed);
			bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
			bp.save();
		}		
		if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID()==MBPartner.Table_ID
				&& (po.is_ValueChanged("OwnCreditLimit") || po.is_ValueChanged("ExternalCreditLimit")
						|| po.is_ValueChanged("SO_CreditUsedOFB")))
		{
			MBPartner bp = (MBPartner)po;
			BigDecimal amtTotal = Env.ZERO;
			BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
			BigDecimal ownCredit = (BigDecimal)bp.get_Value("OwnCreditLimit");
			BigDecimal extCredit = (BigDecimal)bp.get_Value("ExternalCreditLimit");
			if(ownCredit != null)
				amtTotal = ownCredit;
			if(extCredit != null)
				amtTotal = amtTotal.add(extCredit);
			if(amtTotal == null)
				amtTotal = Env.ZERO;
			if(amtUsed != null)
				amtTotal = amtTotal.subtract(amtUsed);
			bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
			//bp.save();			
		}
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		//se gasta credito al completar nota de venta
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MOrder.Table_ID) 
		{	
			MOrder order = (MOrder)po;
			//validación que descuente solo si no es devolución.
			int flag = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) " +
					" FROM C_DocType WHERE C_DocType_ID = "+order.getC_DocType_ID()+"" +
					" AND C_DocType_ID IN ( SELECT dt.C_DocType_ID FROM C_DocType dt " +
					" INNER JOIN C_DocType dt2 ON (dt.C_DocTypeInvoice_ID = dt2.C_DocType_ID)" +
					" WHERE dt2.DocBaseType = 'ARC')");
			if(order.isSOTrx() && flag < 1)
			{
				MBPartner bp = new MBPartner(po.getCtx(), order.getC_BPartner_ID(), po.get_TrxName());
				//ininoles se calcula nuevo grantotal ya que no esta tomando impuestos especificos
				MOrderLine[] olines = order.getLines();
				BigDecimal newGrandTotal = order.getTotalLines();
				BigDecimal taxNewLine = Env.ZERO;
				for (int i = 0; i < olines.length; i++)
				{
					MOrderLine oline = olines[i];
					if((BigDecimal)oline.get_Value("FixedTaxAmt") != null)
						taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("FixedTaxAmt"));
					if((BigDecimal)oline.get_Value("VariableTax") != null)
						taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("VariableTaxAmt"));
					if((BigDecimal)oline.get_Value("IVATaxAmt") != null)
						taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("IVATaxAmt"));				
				}			
				if(taxNewLine != null)
					newGrandTotal = newGrandTotal.add(taxNewLine);
				//ininoles se redondea el nuevo grandtotal a 100 para que cuadre lo consumido con la factura
				//BigDecimal amtRound= newGrandTotal.divide(Env.ONEHUNDRED, 0,RoundingMode.HALF_EVEN);
				//amtRound = amtRound.setScale(0, RoundingMode.HALF_EVEN);
				//ininoles ahora no se redondea
				//newGrandTotal = newGrandTotal.divide(Env.ONEHUNDRED, 0, RoundingMode.HALF_EVEN);				
				//newGrandTotal = newGrandTotal.multiply(Env.ONEHUNDRED);				
				BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
				if(amtTotal == null)
					amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
				if(amtTotal == null)
					amtTotal = Env.ZERO;
				amtTotal = amtTotal.subtract(newGrandTotal);
				bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
				BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
				if(amtUsed == null)
					amtUsed = Env.ZERO;
				amtUsed = amtUsed.add(newGrandTotal);
				bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
				bp.save();	
			}	
		}
		//se libera credito con el pago solo si no es cheque
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MPayment.Table_ID) 
		{	
			MPayment pay = (MPayment)po;
			if(pay.isReceipt() && pay.getTenderType().compareTo("K") != 0)
			{
				MBPartner bp = new MBPartner(po.getCtx(), pay.getC_BPartner_ID(), po.get_TrxName());
				BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
				if(amtTotal == null)
					amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
				if(amtTotal == null)
					amtTotal = Env.ZERO;
				amtTotal = amtTotal.add(pay.getPayAmt());
				bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
				BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
				if(amtUsed == null)
					amtUsed = Env.ZERO;
				amtUsed = amtUsed.subtract(pay.getPayAmt());
				bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
				bp.save();			
			}
		}
		//se libera credito con estado de cuentas con cheques
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MBankStatement.Table_ID) 
		{	
			MBankStatement sBank = (MBankStatement)po;
			String sql = "SELECT cp.C_Payment_ID FROM C_BankStatement bs " +
					" INNER JOIN C_BankStatementLine bsLine ON (bs.C_BankStatement_ID = bsLine.C_BankStatement_ID) " +
					" INNER JOIN C_Payment cp ON (bsLine.C_Payment_ID = cp.C_Payment_ID) " +
					" WHERE cp.IsReceipt = 'Y' AND cp.TenderType = 'K' AND bs.C_BankStatement_ID = ?";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, po.get_TrxName());
				pstmt.setInt(1, sBank.get_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					MPayment pay = new MPayment(po.getCtx(), rs.getInt("C_Payment_ID"), po.get_TrxName());
					if(pay.isReceipt() && pay.getTenderType().compareTo("K") == 0)
					{
						MBPartner bp = new MBPartner(po.getCtx(), pay.getC_BPartner_ID(), po.get_TrxName());
						BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
						if(amtTotal == null)
							amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
						if(amtTotal == null)
							amtTotal = Env.ZERO;
						amtTotal = amtTotal.add(pay.getPayAmt());
						bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
						BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
						if(amtUsed == null)
							amtUsed = Env.ZERO;
						amtUsed = amtUsed.subtract(pay.getPayAmt());
						bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
						bp.save();			
					}
				}				
			}catch (Exception e) {
				log.config("Error: "+e);
			}finally
			{
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
		}
		//se gasta credito con el protesto
		/*
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInvoice.Table_ID) 
		{	
			MInvoice inv = (MInvoice)po;
			if(inv.getC_DocTypeTarget().getDocBaseType().equals("PTK") && inv.isSOTrx())
			{
				MBPartner bp = new MBPartner(po.getCtx(), inv.getC_BPartner_ID(), po.get_TrxName());
				BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
				if(amtTotal == null)
					amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
				if(amtTotal == null)
					amtTotal = Env.ZERO;
				amtTotal = amtTotal.subtract(inv.getGrandTotal());
				bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
				BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
				if(amtUsed == null)
					amtUsed = Env.ZERO;
				amtUsed = amtUsed.add(inv.getGrandTotal());
				bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
				bp.save();	
			}	
		}*/
		//se aumenta credito con la nota de credito
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInvoice.Table_ID) 
		{	
			MInvoice inv = (MInvoice)po;
			if(inv.getC_DocTypeTarget().getDocBaseType().equals("ARC") && inv.isSOTrx())
			{
				MBPartner bp = new MBPartner(po.getCtx(), inv.getC_BPartner_ID(), po.get_TrxName());
				BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
				if(amtTotal == null)
					amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
				if(amtTotal == null)
					amtTotal = Env.ZERO;
				amtTotal = amtTotal.add(inv.getGrandTotal());
				bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
				BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
				if(amtUsed == null)
					amtUsed = Env.ZERO;
				amtUsed = amtUsed.subtract(inv.getGrandTotal());
				bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
				bp.save();	
			}	
		}
		//se disminuye credito con la nota de debito
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInvoice.Table_ID) 
		{	
			MInvoice inv = (MInvoice)po;
			if(inv.getC_DocTypeTarget_ID() == 1000077)
			{
				MBPartner bp = new MBPartner(po.getCtx(), inv.getC_BPartner_ID(), po.get_TrxName());
				BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
				if(amtTotal == null)
					amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
				if(amtTotal == null)
					amtTotal = Env.ZERO;
				amtTotal = amtTotal.subtract(inv.getGrandTotal());
				bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
				BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
				if(amtUsed == null)
					amtUsed = Env.ZERO;
				amtUsed = amtUsed.add(inv.getGrandTotal());
				bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
				bp.save();	
			}	
		}
		//se libera credito con nota de credito
		//ya esta nota de credito, codigo duplicado
		/*
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInvoice.Table_ID) 
		{	
			MInvoice inv = (MInvoice)po;
			if(inv.getC_DocTypeTarget().getDocBaseType().compareTo("ARC") == 0 && inv.isSOTrx())
			{
				MBPartner bp = new MBPartner(po.getCtx(), inv.getC_BPartner_ID(), po.get_TrxName());
				BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
				if(amtTotal == null)
					amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
				if(amtTotal == null)
					amtTotal = Env.ZERO;
				amtTotal = amtTotal.add(inv.getGrandTotal());
				bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
				BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
				if(amtUsed == null)
					amtUsed = Env.ZERO;
				amtUsed = amtUsed.subtract(inv.getGrandTotal());
				bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
				bp.save();	
			}	
		}*/
		//se libera credito al reactivar nota de venta
		if(timing == TIMING_AFTER_REACTIVATE && po.get_Table_ID()==MOrder.Table_ID) 
		{	
			MOrder order = (MOrder)po;
			//validación que descuente solo si no es devolución.
			int flag = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) " +
					" FROM C_DocType WHERE C_DocType_ID = "+order.getC_DocType_ID()+"" +
					" AND C_DocType_ID IN ( SELECT dt.C_DocType_ID FROM C_DocType dt " +
					" INNER JOIN C_DocType dt2 ON (dt.C_DocTypeInvoice_ID = dt2.C_DocType_ID)" +
					" WHERE dt2.DocBaseType = 'ARC')");
			if(order.isSOTrx() && flag < 1)
			{
				MBPartner bp = new MBPartner(po.getCtx(), order.getC_BPartner_ID(), po.get_TrxName());
				BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
				if(amtTotal == null)
					amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
				if(amtTotal == null)
					amtTotal = Env.ZERO;
				amtTotal = amtTotal.add(order.getGrandTotal());
				bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
				BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
				if(amtUsed == null)
					amtUsed = Env.ZERO;
				amtUsed = amtUsed.subtract(order.getGrandTotal());
				bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
				bp.save();	
			}	
		}
		//se libera credito al anular nota de venta
		if(timing == TIMING_BEFORE_VOID && po.get_Table_ID()==MOrder.Table_ID) 
		{	
			MOrder order = (MOrder)po;
			//solo se tomara credito si el estado del documento
			if(order.getDocStatus().compareTo("CO") == 0)
			{
				//validación que descuente solo si no es devolución.
				int flag = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) " +
						" FROM C_DocType WHERE C_DocType_ID = "+order.getC_DocType_ID()+"" +
						" AND C_DocType_ID IN ( SELECT dt.C_DocType_ID FROM C_DocType dt " +
						" INNER JOIN C_DocType dt2 ON (dt.C_DocTypeInvoice_ID = dt2.C_DocType_ID)" +
						" WHERE dt2.DocBaseType = 'ARC')");
				if(order.isSOTrx() && flag < 1)
				{
					MBPartner bp = new MBPartner(po.getCtx(), order.getC_BPartner_ID(), po.get_TrxName());
					BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
					if(amtTotal == null)
						amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
					if(amtTotal == null)
						amtTotal = Env.ZERO;
					amtTotal = amtTotal.add(order.getGrandTotal());
					bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
					BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
					if(amtUsed == null)
						amtUsed = Env.ZERO;
					amtUsed = amtUsed.subtract(order.getGrandTotal());
					bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
					bp.save();	
				}	
			}
		}
		//se libera credito al cerrar(saldar) nota de venta
		if(timing == TIMING_BEFORE_CLOSE && po.get_Table_ID()==MOrder.Table_ID) 
		{	
			MOrder order = (MOrder)po;
			//validación que descuente solo si no es devolución.
			int flag = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) " +
					" FROM C_DocType WHERE C_DocType_ID = "+order.getC_DocType_ID()+"" +
					" AND C_DocType_ID IN ( SELECT dt.C_DocType_ID FROM C_DocType dt " +
					" INNER JOIN C_DocType dt2 ON (dt.C_DocTypeInvoice_ID = dt2.C_DocType_ID)" +
					" WHERE dt2.DocBaseType = 'ARC')");
			if(order.isSOTrx() && flag < 1)
			{
				//calculamos monto a liberar
				BigDecimal amtGrandTotal = Env.ZERO;				
				MOrderLine[] oLines = order.getLines(true, null);	//	Line is default
				for (int i = 0; i < oLines.length; i++)
				{
					MOrderLine oLine = oLines[i];
					//precio por catidad 
					BigDecimal amtLine = Env.ZERO;
					amtLine = oLine.getPriceEntered().multiply(oLine.getQtyEntered().subtract(oLine.getQtyDelivered()));
					amtLine = amtLine.setScale(order.getC_Currency().getStdPrecision(), RoundingMode.HALF_EVEN);
					BigDecimal amtTaxTemp = Env.ZERO;					
					//se suma iva
					if((BigDecimal)oLine.get_Value("IVATaxAmt") != null)
					{
						amtTaxTemp = amtLine.multiply(oLine.getC_Tax().getRate().divide(Env.ONEHUNDRED));
						amtLine = amtLine.add(amtTaxTemp);
					}			
					//se suma impuesto fijo
					if((BigDecimal)oLine.get_Value("FixedTax") != null)
					{
						amtTaxTemp = (BigDecimal)oLine.get_Value("FixedTax");
						amtTaxTemp = amtTaxTemp.multiply(oLine.getQtyEntered().subtract(oLine.getQtyDelivered()));
						amtLine = amtLine.add(amtTaxTemp);
					}
					//se suma impuesto variable
					if((BigDecimal)oLine.get_Value("VariableTax") != null)
					{
						amtTaxTemp = (BigDecimal)oLine.get_Value("VariableTax");
						amtTaxTemp = amtTaxTemp.multiply(oLine.getQtyEntered().subtract(oLine.getQtyDelivered()));
						amtLine = amtLine.add(amtTaxTemp);
					}
					amtGrandTotal = amtGrandTotal.add(amtLine);
					amtGrandTotal = amtGrandTotal.setScale(0, RoundingMode.HALF_EVEN);
				}
				if(amtGrandTotal != null && amtGrandTotal.compareTo(Env.ZERO) > 0)
				{
					MBPartner bp = new MBPartner(po.getCtx(), order.getC_BPartner_ID(), po.get_TrxName());
					BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
					if(amtTotal == null)
						amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
					if(amtTotal == null)
						amtTotal = Env.ZERO;
					amtTotal = amtTotal.add(amtGrandTotal);
					bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
					BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
					if(amtUsed == null)
						amtUsed = Env.ZERO;
					amtUsed = amtUsed.subtract(amtGrandTotal);
					bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
					bp.save();	
				}	
			}
		}
		//se gasta credito al anular pago solo si no es cheque
		if(timing == TIMING_AFTER_VOID && po.get_Table_ID()==MPayment.Table_ID) 
		{	
			MPayment pay = (MPayment)po;
			if(pay.isReceipt() && pay.getTenderType().compareTo("K") != 0)
			{
				MBPartner bp = new MBPartner(po.getCtx(), pay.getC_BPartner_ID(), po.get_TrxName());
				BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
				if(amtTotal == null)
					amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
				if(amtTotal == null)
					amtTotal = Env.ZERO;
				amtTotal = amtTotal.add(pay.getPayAmt());
				bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
				BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
				if(amtUsed == null)
					amtUsed = Env.ZERO;
				amtUsed = amtUsed.add(pay.getPayAmt());
				bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
				bp.save();		
			}
		}
		//se gasta credito al anular estado de cuentas con cheques
		if(timing == TIMING_BEFORE_VOID && po.get_Table_ID()==MBankStatement.Table_ID) 
		{	
			MBankStatement sBank = (MBankStatement)po;
			String sql = "SELECT cp.C_Payment_ID FROM C_BankStatement bs " +
					" INNER JOIN C_BankStatementLine bsLine ON (bs.C_BankStatement_ID = bsLine.C_BankStatement_ID) " +
					" INNER JOIN C_Payment cp ON (bsLine.C_Payment_ID = cp.C_Payment_ID) " +
					" WHERE cp.IsReceipt = 'Y' AND cp.TenderType = 'K' AND bs.C_BankStatement_ID = ?";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, po.get_TrxName());
				pstmt.setInt(1, sBank.get_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					MPayment pay = new MPayment(po.getCtx(), rs.getInt("C_Payment_ID"), po.get_TrxName());
					if(pay.isReceipt() && pay.getTenderType().compareTo("K") == 0)
					{
						MBPartner bp = new MBPartner(po.getCtx(), pay.getC_BPartner_ID(), po.get_TrxName());
						BigDecimal amtTotal = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
						if(amtTotal == null)
							amtTotal = (BigDecimal)bp.get_Value("OwnCreditLimit");
						if(amtTotal == null)
							amtTotal = Env.ZERO;
						amtTotal = amtTotal.add(pay.getPayAmt());
						bp.set_CustomColumn("TotalOpenBalanceOFB", amtTotal);
						BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
						if(amtUsed == null)
							amtUsed = Env.ZERO;
						amtUsed = amtUsed.add(pay.getPayAmt());
						bp.set_CustomColumn("SO_CreditUsedOFB", amtUsed);
						bp.save();			
					}
				}				
			}catch (Exception e) {
				log.config("Error: "+e);
			}finally
			{
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
		}
		return null;
	}	//	docValidate
	
	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString


	

}	
