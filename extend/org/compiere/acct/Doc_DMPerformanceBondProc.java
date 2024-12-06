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
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.X_DM_PerformanceBond;
import org.compiere.model.X_DM_PerformanceBondDet_Proc;
import org.compiere.model.X_DM_PerformanceBond_Proc;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Invoice (318)
 *  Document Types:     ARI, ARC, ARF, API, APC
 *  </pre>
 *  @author ininoles OFB
 *  
 *  @version  $Id: Doc_Invoice.java,v 1.2 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_DMPerformanceBondProc extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_DMPerformanceBondProc(MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, X_DM_PerformanceBond_Proc.class, rs, null, trxName);
	}	//	Doc_Invoice

		
	/** All lines are Service			*/

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	
	/** Currency Precision				*/
	private int				m_precision = -1;
	/** All lines are Service			*/
	
	protected String loadDocumentDetails ()
	{
		X_DM_PerformanceBond_Proc proc = new X_DM_PerformanceBond_Proc(getCtx(), getPO().get_ID(), null);
		
		setDateDoc(proc.getDateAcct());
		setDateAcct(proc.getDateAcct());
		
		//setIsTaxIncluded(invoice.isTaxIncluded());
		//	Amounts
		setAmount(Doc.AMTTYPE_Gross, new BigDecimal("0.0"));
		setAmount(Doc.AMTTYPE_Net, new BigDecimal("0.0"));
		//setAmount(Doc.AMTTYPE_Charge, new BigDecimal("0.0"));
		
				
		//	Contained Objects		
		//log.fine("Lines=" + p_lines.length + ", Taxes=" + m_taxes.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load Invoice Taxes
	 *  @return DocTax Array
	 */
	/**
	 *	Load Invoice Line
	 *	@param invoice invoice
	 *  @return DocLine Array
	 */
	
	private int getStdPrecision()
	{
		if (m_precision == -1)
			m_precision = MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
		return m_precision;
	}	//	getPrecision

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;		
		//
		log.fine(toString() + " Balance=" + retValue);
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  ARI, ARC, ARF, API, APC.
	 *  <pre>
	 *  ARI, ARF
	 *      Receivables     DR
	 *      Charge                  CR
	 *      TaxDue                  CR
	 *      Revenue                 CR
	 *
	 *  ARC
	 *      Receivables             CR
	 *      Charge          DR
	 *      TaxDue          DR
	 *      Revenue         RR
	 *
	 *  API
	 *      Payables                CR
	 *      Charge          DR
	 *      TaxCredit       DR
	 *      Expense         DR
	 *
	 *  APC
	 *      Payables        DR
	 *      Charge                  CR
	 *      TaxCredit               CR
	 *      Expense                 CR
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		
		//faaguilar OFB validation not post begin
		X_DM_PerformanceBond_Proc procDoc = new X_DM_PerformanceBond_Proc(getCtx(), p_po.get_ID(),getTrxName());
		
		MDocType type = MDocType.get(getCtx(),  procDoc.getC_DocType_ID());
		if(!type.get_ValueAsBoolean("Posted"))
			return facts;
		//END faaguilar validation not post end
			
		//  create Fact Header
		
		Fact fact = new Fact(this, as, Fact.POST_Actual);		
		//  Cash based accounting
		if (!as.isAccrual())
			return facts;

		//  alta boleta 
		if (getDocumentType().equals(DOCTYPE_AltaBoleta))
		{		    
			FactLine fl = null;
			FactLine fl2 = null;
			//hacemos la conversion de moneda 
			BigDecimal amtConvert = null;
			log.config("variables para la conversion:"+procDoc.getAmt()+" , "+procDoc.getC_Currency_ID()+
					" , "+ as.getC_Currency_ID()+" , "+	getDateAcct()+" , "+114+" , "+getAD_Client_ID()+" , "+getAD_Org_ID());
			
			if (procDoc.getC_Currency_ID() != as.getC_Currency_ID())
			{
				amtConvert = MConversionRate.convert(getCtx(), procDoc.getAmt(), 
					procDoc.getC_Currency_ID(), as.getC_Currency_ID(),
					getDateAcct(), 114, 
					getAD_Client_ID(), getAD_Org_ID());
				log.config("monto dentro de if: "+ amtConvert);
			}
			else
			{
				amtConvert = procDoc.getAmt();
			}
			
			fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_AltaBoletaD, as),
					as.getC_Currency_ID(), amtConvert, null);
			
			fl.setC_BPartner_ID(procDoc.getC_BPartner_ID());
			fl.save();
			
			fl2 = fact.createLine(null, getAccount(Doc.ACCTTYPE_AltaBoletaC, as),
					as.getC_Currency_ID(),amtConvert.negate(), null);		
			fl2.setC_BPartner_ID(procDoc.getC_BPartner_ID());
			fl2.save();
		}	
		//baja boleta
		else if (getDocumentType().equals(DOCTYPE_BajaVisadaBoleta))
		{
			FactLine fl = null;
			FactLine fl2 = null;
			X_DM_PerformanceBond boleta = new X_DM_PerformanceBond(getCtx(),procDoc.getDM_PerformanceBond_ID(),getTrxName());
			
			//hacemos la conversion de moneda 
			BigDecimal amtConvert = new BigDecimal("0.0");
			Timestamp dateDoc = (Timestamp) boleta.get_Value("DateDoc");
			
			if (procDoc.getC_Currency_ID() != as.getC_Currency_ID())
			{
				amtConvert = MConversionRate.convert(getCtx(), boleta.getAmt(), 
					procDoc.getC_Currency_ID(), as.getC_Currency_ID(),
					dateDoc,114, getAD_Client_ID(), getAD_Org_ID());
			}
			else
			{
				amtConvert = boleta.getAmt();
			}
			
			fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_VencimientoBoletaD, as),
					as.getC_Currency_ID(), amtConvert, null);
			
			fl.setC_BPartner_ID(procDoc.getC_BPartner_ID());
			fl.save();
			
			fl2 = fact.createLine(null, getAccount(Doc.ACCTTYPE_VencimientoBoletaC, as),
					as.getC_Currency_ID(), amtConvert.negate(), null);
			fl2.setC_BPartner_ID(procDoc.getC_BPartner_ID());
			fl2.save();
		}
		//cobro boleta
		else if (getDocumentType().equals(DOCTYPE_CobroBoleta))
		{
			FactLine fl = null;
			FactLine fl2 = null;
			FactLine fl3 = null;
			FactLine fl4 = null;
			X_DM_PerformanceBond boleta = new X_DM_PerformanceBond(getCtx(),procDoc.getDM_PerformanceBond_ID(),getTrxName());
			
			//hacemos la conversion de moneda 
			BigDecimal amtConvert = new BigDecimal("0.0");
			Timestamp dateDoc = (Timestamp) boleta.get_Value("DateDoc");
			
			if (procDoc.getC_Currency_ID() != as.getC_Currency_ID())
			{
				amtConvert = MConversionRate.convert(getCtx(), boleta.getAmt(), 
					procDoc.getC_Currency_ID(), as.getC_Currency_ID(),
					dateDoc, 114, getAD_Client_ID(), getAD_Org_ID());
			} 
			else
			{
				amtConvert = boleta.getAmt();
			}
			//ininoles usamos nueva cuenta de boleta
			
			int C_VCombinationTemp_ID = DB.getSQLValue(getTrxName(),"SELECT DM_Cobro_Acct " +
					" FROM C_Bank WHERE C_Bank_ID = "+boleta.getC_Bank_ID());
			MAccount acct = MAccount.get (as.getCtx(), C_VCombinationTemp_ID);
			
			fl = fact.createLine(null, acct,as.getC_Currency_ID(), amtConvert, null);
			fl.setC_BPartner_ID(procDoc.getC_BPartner_ID());
			fl.save();
			
			fl2 = fact.createLine(null, getAccount(Doc.ACCTTYPE_CobroBoletaC, as),
					as.getC_Currency_ID(), amtConvert.negate(), null);
			fl2.setC_BPartner_ID(procDoc.getC_BPartner_ID());
			fl2.save();			
			
			//nuevos asientos con cuentas de baja
			fl3 = fact.createLine(null, getAccount(Doc.ACCTTYPE_VencimientoBoletaD, as),
					as.getC_Currency_ID(), amtConvert, null);			
			fl3.setC_BPartner_ID(procDoc.getC_BPartner_ID());
			fl3.save();
			
			fl4 = fact.createLine(null, getAccount(Doc.ACCTTYPE_VencimientoBoletaC, as),
					as.getC_Currency_ID(), amtConvert.negate(), null);
			fl4.setC_BPartner_ID(procDoc.getC_BPartner_ID());
			fl4.save();
		}
		
		else if (getDocumentType().equals(DOCTYPE_VencimientoBoleta))
		{
			
			String sqlDetail = "SELECT DM_PerformanceBondDet_Proc_ID FROM DM_PerformanceBondDet_Proc WHERE DM_PerformanceBond_Proc_ID = ? ";
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try
			{
				pstmt = DB.prepareStatement(sqlDetail, getTrxName());
				pstmt.setInt(1,p_po.get_ID());
				rs = pstmt.executeQuery();
								
				
				while(rs.next())
				{
					FactLine fl = null;
					FactLine fl2 = null;
					
					X_DM_PerformanceBondDet_Proc detail = new X_DM_PerformanceBondDet_Proc(getCtx(), rs.getInt(1), getTrxName());
					
					X_DM_PerformanceBond boletaDetail = new X_DM_PerformanceBond(getCtx(),detail.getDM_PerformanceBond_ID(),getTrxName());
					//hacemos la conversion de moneda 
					BigDecimal amtConvert = new BigDecimal("0.0");
					Timestamp dateDoc = (Timestamp) boletaDetail.get_Value("DateDoc");
					
					if (procDoc.getC_Currency_ID() != as.getC_Currency_ID())
					{
						amtConvert = MConversionRate.convert(getCtx(), boletaDetail.getAmt(), 
							procDoc.getC_Currency_ID(), as.getC_Currency_ID(),
							dateDoc,114, getAD_Client_ID(), getAD_Org_ID());
					} 
					else
					{
						amtConvert = boletaDetail.getAmt();
					}
					
					fl = fact.createLine(null,getAccount(Doc.ACCTTYPE_VencimientoBoletaD, as), as.getC_Currency_ID(), amtConvert, null);
					fl.setC_BPartner_ID(procDoc.getC_BPartner_ID());
					fl.save();
					
					fl2 = fact.createLine(null,getAccount(Doc.ACCTTYPE_VencimientoBoletaC, as), as.getC_Currency_ID(), amtConvert.negate(), null);
					fl2.setC_BPartner_ID(procDoc.getC_BPartner_ID());
					fl2.save();
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sqlDetail, e);
			}
			finally 
			{
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}				
		}
		else
		{
			p_Error = "DocumentType unknown: " + getDocumentType();
			log.log(Level.SEVERE, p_Error);
			fact = null;
		}
		//
		facts.add(fact);		
		return facts;
	}   //  createFact

	/**
	 * ininoles OFB
	 * custom method*/

}   //  
