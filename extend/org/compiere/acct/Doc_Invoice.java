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
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCostDetail;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.MLandedCostAllocation;
import org.compiere.model.MPayment;
import org.compiere.model.MTax;
import org.compiere.model.OFBProductCost;
import org.compiere.model.ProductCost;
import org.compiere.model.X_Fact_Acct;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.ofb.model.OFBForward;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Invoice (318)
 *  Document Types:     ARI, ARC, ARF, API, APC
 *  </pre>
 *  @author Jorg Janke
 *  @author Armen Rizal, Goodwill Consulting
 *  	<li>BF: 2797257	Landed Cost Detail is not using allocation qty
 *  
 *  @version  $Id: Doc_Invoice.java,v 1.2 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_Invoice extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_Invoice(MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MInvoice.class, rs, null, trxName);
	}	//	Doc_Invoice

	/** Contained Optional Tax Lines    */
	private DocTax[]        m_taxes = null;
	/** Currency Precision				*/
	private int				m_precision = -1;
	/** All lines are Service			*/
	private boolean			m_allLinesService = true;
	/** All lines are product item		*/
	private boolean			m_allLinesItem = true;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MInvoice invoice = (MInvoice)getPO();
		setDateDoc(invoice.getDateInvoiced());
		setIsTaxIncluded(invoice.isTaxIncluded());
		//	Amounts
		setAmount(Doc.AMTTYPE_Gross, invoice.getGrandTotal());
		setAmount(Doc.AMTTYPE_Net, invoice.getTotalLines());
		setAmount(Doc.AMTTYPE_Charge, invoice.getChargeAmt());
				
		//	Contained Objects
		m_taxes = loadTaxes();
		p_lines = loadLines(invoice);
		log.fine("Lines=" + p_lines.length + ", Taxes=" + m_taxes.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load Invoice Taxes
	 *  @return DocTax Array
	 */
	private DocTax[] loadTaxes()
	{
		ArrayList<DocTax> list = new ArrayList<DocTax>();
		String sql = "SELECT it.C_Tax_ID, t.Name, t.Rate, it.TaxBaseAmt, it.TaxAmt, t.IsSalesTax "
			+ "FROM C_Tax t, C_InvoiceTax it "
			+ "WHERE t.C_Tax_ID=it.C_Tax_ID AND it.C_Invoice_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, getTrxName());
			pstmt.setInt(1, get_ID());
			rs = pstmt.executeQuery();
			//
			while (rs.next())
			{
				int C_Tax_ID = rs.getInt(1);
				String name = rs.getString(2);
				//faaguilar OFB begin -- geminis iva no recuperable
				if(name.trim().toUpperCase().equals("IVA NO RECUPERABLE"))
					continue;
				//faaguilar OFB end
				BigDecimal rate = rs.getBigDecimal(3);
				BigDecimal taxBaseAmt = rs.getBigDecimal(4);
				BigDecimal amount = rs.getBigDecimal(5);
				boolean salesTax = "Y".equals(rs.getString(6));
				//
				DocTax taxLine = new DocTax(C_Tax_ID, name, rate, 
					taxBaseAmt, amount, salesTax);
				log.fine(taxLine.toString());
				list.add(taxLine);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return null;
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		//	Return Array
		DocTax[] tl = new DocTax[list.size()];
		list.toArray(tl);
		return tl;
	}	//	loadTaxes

	/**
	 *	Load Invoice Line
	 *	@param invoice invoice
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines (MInvoice invoice)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		BigDecimal totalTemp=new BigDecimal("0.0");//faaguilar OFB
		//
		MInvoiceLine[] lines = invoice.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			
			
			if (line.isDescription())
				continue;
			DocLine docLine = new DocLine(line, this);
			//	Qty
			BigDecimal Qty = line.getQtyInvoiced();
			boolean cm = getDocumentType().equals(DOCTYPE_ARCredit) 
				|| getDocumentType().equals(DOCTYPE_APCredit);
			docLine.setQty(cm ? Qty.negate() : Qty, invoice.isSOTrx());
			//
			BigDecimal LineNetAmt = line.getLineNetAmt();
			BigDecimal PriceList = line.getPriceList();
			
			//faaguilar OFB Invoice Service begin
			if(getDocumentType().equals(DOCTYPE_API_Service) || getDocumentType().equals(DOCTYPE_PSC_Service)){
				LineNetAmt = line.getLineTotalAmt().setScale(0, BigDecimal.ROUND_DOWN);
				totalTemp=totalTemp.add(LineNetAmt);
				
				if(i==lines.length-1){
					if(totalTemp.compareTo(invoice.getGrandTotal())<0)
						LineNetAmt=LineNetAmt.add(invoice.getGrandTotal().subtract(totalTemp));
					else if(totalTemp.compareTo(invoice.getGrandTotal())>0) 
						LineNetAmt=LineNetAmt.subtract(totalTemp.subtract(invoice.getGrandTotal()));
				}
			}
			//faaguilar OFB Invoice Service end
			//faaguilar OFB begin -- geminis iva no recuperable
			if(line.getC_Tax_ID()>0)
				if(line.getC_Tax().getName().trim().toUpperCase().equals("IVA NO RECUPERABLE"))
				{
					LineNetAmt = line.getLineTotalAmt();
					PriceList = line.getLineTotalAmt().divide(line.getQtyInvoiced(), BigDecimal.ROUND_HALF_UP);
				}
			//faaguilar OFB end
			
			int C_Tax_ID = docLine.getC_Tax_ID();
			//	Correct included Tax
			if (isTaxIncluded() && C_Tax_ID != 0)
			{
				MTax tax = MTax.get(getCtx(), C_Tax_ID);
				if (!tax.isZeroTax())
				{
					BigDecimal LineNetAmtTax = tax.calculateTax(LineNetAmt, true, getStdPrecision());
					log.fine("LineNetAmt=" + LineNetAmt + " - Tax=" + LineNetAmtTax);
					LineNetAmt = LineNetAmt.subtract(LineNetAmtTax);
					for (int t = 0; t < m_taxes.length; t++)
					{
						if (m_taxes[t].getC_Tax_ID() == C_Tax_ID)
						{
							m_taxes[t].addIncludedTax(LineNetAmtTax);
							break;
						}
					}
					BigDecimal PriceListTax = tax.calculateTax(PriceList, true, getStdPrecision());
					PriceList = PriceList.subtract(PriceListTax);
				}
			}	//	correct included Tax
			
			docLine.setAmount (LineNetAmt, PriceList, Qty);	//	qty for discount calc
			
			//faaguilar OFB special for docs PTK and Asset
			docLine.m_Payment_ID=line.get_ValueAsInt("C_Payment_ID");
			
			if(line.isA_CreateAsset())
				if(line.getA_CapvsExp().equals("Cap"))
				{
				docLine.isAsset=true;
				docLine.AssetGroup_ID=line.get_ValueAsInt("A_Asset_Group_ID");
				}
			//END faaguilar OFB special for docs PTK and Asset
			
			
			if (docLine.isItem())
				m_allLinesService = false;
			else
				m_allLinesItem = false;
			//
			log.fine(docLine.toString());
			list.add(docLine);
		}
		
		//	Convert to Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);

		//	Included Tax - make sure that no difference
		if (isTaxIncluded())
		{
			for (int i = 0; i < m_taxes.length; i++)
			{
				if (m_taxes[i].isIncludedTaxDifference())
				{
					BigDecimal diff = m_taxes[i].getIncludedTaxDifference(); 
					for (int j = 0; j < dls.length; j++)
					{
						if (dls[j].getC_Tax_ID() == m_taxes[i].getC_Tax_ID())
						{
							dls[j].setLineNetAmtDifference(diff);
							break;
						}
					}	//	for all lines
				}	//	tax difference
			}	//	for all taxes
		}	//	Included Tax difference
		
		//	Return Array
		return dls;
	}	//	loadLines

	/**
	 * 	Get Currency Precision
	 *	@return precision
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
		StringBuffer sb = new StringBuffer (" [");
		//  Total
		retValue = retValue.add(getAmount(Doc.AMTTYPE_Gross));
		sb.append(getAmount(Doc.AMTTYPE_Gross));
		//  - Header Charge
		retValue = retValue.subtract(getAmount(Doc.AMTTYPE_Charge));
		sb.append("-").append(getAmount(Doc.AMTTYPE_Charge));
		//  - Tax
		for (int i = 0; i < m_taxes.length; i++)
		{
			retValue = retValue.subtract(m_taxes[i].getAmount());
			sb.append("-").append(m_taxes[i].getAmount());
		}
		//  - Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			retValue = retValue.subtract(p_lines[i].getAmtSource());
			sb.append("-").append(p_lines[i].getAmtSource());
		}
		sb.append("]");
		//
		log.fine(toString() + " Balance=" + retValue + sb.toString());
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
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);

		//faaguilar OFB validation not post begin
			    MInvoice doc = (MInvoice)getPO();
				MDocType type = MDocType.get(getCtx(),  doc.getC_DocType_ID());
				if(!type.get_ValueAsBoolean("Posted"))
					return facts;
		//END faaguilar validation not post end
				
		//faaguilar OFB begin
		MInvoice invoice = (MInvoice)getPO();
		MClient client = new MClient(getCtx(), getAD_Client_ID(), getTrxName());
		if(invoice.getDocStatus().equals("VO")){
			
			MInvoiceLine[] iLines = invoice.getLines(false);
			for (int i = 0; i < iLines.length; i++)
			{
				MInvoiceLine iLine = iLines[i];
				MCostDetail cd = OFBProductCost.get (as.getCtx(), "C_InvoiceLine_ID=?", 
						iLine.getC_InvoiceLine_ID(), iLine.getM_AttributeSetInstance_ID(), as.getC_AcctSchema_ID(), getTrxName());
				
				if (cd != null )//posee detalle de costo la factura
				{
					
					if(cd.isProcessed())
						if(cd.getAmt().signum()>0)
							OFBProductCost.createInvoice(as, cd.getAD_Org_ID(), 
									cd.getM_Product_ID(), cd.getM_AttributeSetInstance_ID(),
									cd.getC_InvoiceLine_ID(), cd.getM_CostElement_ID(),
									cd.getAmt().negate(), Env.ZERO, "Anulacion Factura", getTrxName());
					else{
						String sql = "DELETE From M_CostDetail "
							+ "WHERE Processed='N' "
							+ " AND C_InvoiceLine_ID=" + iLine.getC_InvoiceLine_ID()
							+ " AND C_AcctSchema_ID =" + as.getC_AcctSchema_ID();			
							
						int no = DB.executeUpdate(sql, this.getTrxName());
						if (no != 0)
							log.config("Deleted #" + no);
					}
						
				}
			}
			return facts;
		}
		//faaguilar OFb end
		
		//  Cash based accounting
		if (!as.isAccrual())
			return facts;

		//  ** ARI, ARF
		if (getDocumentType().equals(DOCTYPE_ARInvoice) 
			|| getDocumentType().equals(DOCTYPE_ARProForma))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			
			//  Header Charge           CR
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			if (amt != null && amt.signum() != 0)
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
					getC_Currency_ID(), null, amt);
			//  TaxDue                  CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), null, amt);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			//  Revenue                 CR
			for (int i = 0; i < p_lines.length; i++)
			{
				amt = p_lines[i].getAmtSource();
				BigDecimal dAmt = null;
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{
						amt = amt.add(discount);
						dAmt = discount;
						fact.createLine (p_lines[i],
								p_lines[i].getAccount(ProductCost.ACCTTYPE_P_TDiscountGrant, as),
								getC_Currency_ID(), dAmt, null);
					}
				}
						
				fact.createLine (p_lines[i],
					p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as),
					getC_Currency_ID(), null, amt);
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
			
			//  Receivables     DR
			int receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_C_Receivable, as);
			int receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_C_Receivable_Services, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| receivables_ID == receivablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
					getC_Currency_ID(), grossAmt, null);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
					getC_Currency_ID(), serviceAmt, null);
			//ininoles se agrega linea de impuesto especifico para PA
			if(OFBForward.UpdateIVAPA())
			{
				BigDecimal amtIVA = DB.getSQLValueBD("C_Invoice_ID","SELECT ROUND(COALESCE(SUM(IVATaxAmt),0)) FROM C_InvoiceLine cil " +
					" INNER JOIN C_Tax ct ON (cil.C_Tax_ID = ct.C_Tax_ID)" +
					" WHERE cil.IsActive = 'Y' AND IsTaxExempt = 'N' AND C_Invoice_ID = "+invoice.get_ID());
				if(amtIVA == null)
					amtIVA = Env.ZERO;
				BigDecimal oTax = invoice.getGrandTotal().subtract(invoice.getTotalLines()).subtract(amtIVA);
				int customTAx = getValidCombination_ID (Doc.ACCTTYPE_ImpuestoEspecificoCombustible, as);
				fact.createLine(null, MAccount.get(getCtx(), customTAx),
						getC_Currency_ID(),null,oTax);
				
			}
			//ininoles end
		}
		//  ARC
		else if (getDocumentType().equals(DOCTYPE_ARCredit))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;

			//  Header Charge   DR
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			if (amt != null && amt.signum() != 0)
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
					getC_Currency_ID(), amt, null);
			//  TaxDue          DR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), amt, null);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			//  Revenue         CR
			for (int i = 0; i < p_lines.length; i++)
			{
				amt = p_lines[i].getAmtSource();
				BigDecimal dAmt = null;
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{
						amt = amt.add(discount);
						dAmt = discount;
						fact.createLine (p_lines[i],
								p_lines[i].getAccount (ProductCost.ACCTTYPE_P_TDiscountGrant, as),
								getC_Currency_ID(), null, dAmt);
					}
				}
				fact.createLine (p_lines[i],
					p_lines[i].getAccount (ProductCost.ACCTTYPE_P_Revenue, as),
					getC_Currency_ID(), amt, null);
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
			//  Receivables             CR
			int receivables_ID = getValidCombination_ID (Doc.ACCTTYPE_C_Receivable, as);
			int receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_C_Receivable_Services, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| receivables_ID == receivablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
					getC_Currency_ID(), null, grossAmt);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
					getC_Currency_ID(), null, serviceAmt);
			
			//ininoles se agrega linea de impuesto especifico para PA
			if(OFBForward.UpdateIVAPA())
			{
				BigDecimal amtIVA = DB.getSQLValueBD("C_Invoice_ID","SELECT ROUND(COALESCE(SUM(IVATaxAmt),0)) FROM C_InvoiceLine cil " +
					" INNER JOIN C_Tax ct ON (cil.C_Tax_ID = ct.C_Tax_ID)" +
					" WHERE cil.IsActive = 'Y' AND IsTaxExempt = 'N' AND C_Invoice_ID = "+invoice.get_ID());
				if(amtIVA == null)
					amtIVA = Env.ZERO;
				BigDecimal oTax = invoice.getGrandTotal().subtract(invoice.getTotalLines()).subtract(amtIVA);
				int customTAx = getValidCombination_ID (Doc.ACCTTYPE_ImpuestoEspecificoCombustible, as);
				fact.createLine(null, MAccount.get(getCtx(), customTAx),
						getC_Currency_ID(),oTax,null);
				
			}
		}
		
		//  ** API
		else if (getDocumentType().equals(DOCTYPE_APInvoice))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;

			//  Charge          DR
			fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
				getC_Currency_ID(), getAmount(Doc.AMTTYPE_Charge), null);
			//  TaxCredit       DR
			for (int i = 0; i < m_taxes.length; i++)
			{
				FactLine tl = fact.createLine(null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					getC_Currency_ID(), m_taxes[i].getAmount(), null);
				if (tl != null)
					tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
			}
			//  Expense         DR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				boolean landedCost = landedCost(as, fact, line, true);
				if (landedCost && as.isExplicitCostAdjustment())
				{
					fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), line.getAmtSource(), null);
					//
					FactLine fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), null, line.getAmtSource());
					String desc = line.getDescription();
					if (desc == null)
						desc = "100%";
					else
						desc += " 100%";
					fl.setDescription(desc);
				}
				if (!landedCost)
				{
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
							MAccount tradeDiscountReceived = line.getAccount(ProductCost.ACCTTYPE_P_TDiscountRec, as);
							fact.createLine (line, tradeDiscountReceived,
									getC_Currency_ID(), null, dAmt);
						}
					}
					fact.createLine (line, expense,
						getC_Currency_ID(), amt, null);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					//
					if (line.getM_Product_ID() != 0
						&& line.getProduct().isService())	//	otherwise Inv Matching
						/*MCostDetail.createInvoice(as, line.getAD_Org_ID(), 
							line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							line.get_ID(), 0,		//	No Cost Element
							line.getAmtSource(), line.getQty(),
							line.getDescription(), getTrxName());*///faaguilar OFB original code commented
						OFBProductCost.createInvoice(as, line.getAD_Org_ID(), 
								line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
								line.get_ID(), 0,		//	No Cost Element
								line.getAmtSource(), line.getQty(),
								line.getDescription(), getTrxName());//faaguilar OFB
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}

			//  Liability               CR
			int payables_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability, as);
			int payablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability_Services, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payables_ID),
					getC_Currency_ID(), null, grossAmt);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
					getC_Currency_ID(), null, serviceAmt);
			//
			updateProductPO(as);	//	Only API
			updateProductInfo (as.getC_AcctSchema_ID());    //  only API
			//ininoles se agrega linea de impuesto especifico para PA
			if(OFBForward.UpdateIVAPA())
			{
				BigDecimal amtIVA = DB.getSQLValueBD("C_Invoice_ID","SELECT ROUND(COALESCE(SUM(IVATaxAmt),0)) FROM C_InvoiceLine cil " +
					" INNER JOIN C_Tax ct ON (cil.C_Tax_ID = ct.C_Tax_ID)" +
					" WHERE cil.IsActive = 'Y' AND IsTaxExempt = 'N' AND C_Invoice_ID = "+invoice.get_ID());
				if(amtIVA == null)
					amtIVA = Env.ZERO;
				BigDecimal oTax = invoice.getGrandTotal().subtract(invoice.getTotalLines()).subtract(amtIVA);
				int customTAx = getValidCombination_ID (Doc.ACCTTYPE_ImpuestoEspecificoCombustible, as);
				fact.createLine(null, MAccount.get(getCtx(), customTAx),
						getC_Currency_ID(),oTax,null);
				
			}
		}
		//  APC
		else if (getDocumentType().equals(DOCTYPE_APCredit))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			//  Charge                  CR
			fact.createLine (null, getAccount(Doc.ACCTTYPE_Charge, as),
				getC_Currency_ID(), null, getAmount(Doc.AMTTYPE_Charge));
			//  TaxCredit               CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				FactLine tl = fact.createLine (null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					getC_Currency_ID(), null, m_taxes[i].getAmount());
				if (tl != null)
					tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
			}
			//  Expense                 CR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				boolean landedCost = landedCost(as, fact, line, false);
				if (landedCost && as.isExplicitCostAdjustment())
				{
					fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), null, line.getAmtSource());
					//
					FactLine fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), line.getAmtSource(), null);
					String desc = line.getDescription();
					if (desc == null)
						desc = "100%";
					else
						desc += " 100%";
					fl.setDescription(desc);
				}
				if (!landedCost)
				{
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
							MAccount tradeDiscountReceived = line.getAccount(ProductCost.ACCTTYPE_P_TDiscountRec, as);
							fact.createLine (line, tradeDiscountReceived,
									getC_Currency_ID(), dAmt, null);
						}
					}
					fact.createLine (line, expense,
						getC_Currency_ID(), null, amt);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					//
					if (line.getM_Product_ID() != 0
						&& line.getProduct().isService())	//	otherwise Inv Matching
						MCostDetail.createInvoice(as, line.getAD_Org_ID(), 
							line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							line.get_ID(), 0,		//	No Cost Element
							line.getAmtSource().negate(), line.getQty(),
							line.getDescription(), getTrxName());
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}
			//  Liability       DR
			int payables_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability, as);
			int payablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability_Services, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payables_ID),
					getC_Currency_ID(), grossAmt, null);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
					getC_Currency_ID(), serviceAmt, null);
			if(OFBForward.UpdateIVAPA())
			{
				BigDecimal amtIVA = DB.getSQLValueBD("C_Invoice_ID","SELECT ROUND(COALESCE(SUM(IVATaxAmt),0)) FROM C_InvoiceLine cil " +
					" INNER JOIN C_Tax ct ON (cil.C_Tax_ID = ct.C_Tax_ID)" +
					" WHERE cil.IsActive = 'Y' AND IsTaxExempt = 'N' AND C_Invoice_ID = "+invoice.get_ID());
				if(amtIVA == null)
					amtIVA = Env.ZERO;
				BigDecimal oTax = invoice.getGrandTotal().subtract(invoice.getTotalLines()).subtract(amtIVA);
				int customTAx = getValidCombination_ID (Doc.ACCTTYPE_ImpuestoEspecificoCombustible, as);
				fact.createLine(null, MAccount.get(getCtx(), customTAx),
						getC_Currency_ID(),null,oTax);
				
			}
		}
	//  ARB begin faaguilar OFB
		else if (getDocumentType().equals(DOCTYPE_AR_Boleta))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			
			//  Header Charge           CR
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			if (amt != null && amt.signum() != 0)
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
					getC_Currency_ID(), null, amt);
			//  TaxDue                  CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), null, amt);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			//  Revenue                 CR
			for (int i = 0; i < p_lines.length; i++)
			{
				amt = p_lines[i].getAmtSource();
				BigDecimal dAmt = null;
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{
						amt = amt.add(discount);
						dAmt = discount;
					}
				}
				fact.createLine (p_lines[i],
					p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as),
					getC_Currency_ID(), dAmt, amt);
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
			
			//  Receivables     DR
			int receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_ARB, as);
			int receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_ARB, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| receivables_ID == receivablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
					getC_Currency_ID(), grossAmt, null);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
					getC_Currency_ID(), serviceAmt, null);
		}
		//  ARB end faaguilar OFB
		// Begin faaguilar AP Boleta 
		else if (getDocumentType().equals(DOCTYPE_AP_Boleta))
		{
			System.out.println("*** AP Boleta");
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			BigDecimal TotalTax=Env.ZERO; //Fabian
			//  Charge          DR
			fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
				getC_Currency_ID(), getAmount(Doc.AMTTYPE_Charge), null);
			//  TaxCredit       DR
			TotalTax=DB.getSQLValueBD("C_Invoice","Select (GrandTotal - TotalLines) from C_Invoice where C_Invoice_ID=?", get_ID());
			
			for (int i = 0; i < m_taxes.length; i++)
			{
				FactLine tl = fact.createLine(null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					getC_Currency_ID(), null, m_taxes[i].getAmount());
					
				TotalTax.add(m_taxes[i].getAmount()); // Fabian
				
				if (tl != null)
					tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				
			}
			
			System.out.println("*** TotalTax: " + TotalTax);
			//  Expense         DR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				boolean landedCost = landedCost(as, fact, line, true);
				
				if (landedCost && as.isExplicitCostAdjustment() && line.isAsset==false)
				{
					MTax tax2 =MTax.get(getCtx(), line.getC_Tax_ID());
					BigDecimal LineNetAmtTax2 = tax2.calculateTax(line.getAmtSource(), false, getStdPrecision());
					
					fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), line.getAmtSource().add(LineNetAmtTax2), null);
					//
					FactLine fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), null, line.getAmtSource());
					String desc = line.getDescription();
					if (desc == null)
						desc = "100%";
					else
						desc += " 100%";
					fl.setDescription(desc);
				}
				if (!landedCost && line.isAsset==false)
				{
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
						}
					}
					
					MTax tax2 =MTax.get(getCtx(), line.getC_Tax_ID());
					BigDecimal LineNetAmtTax2 = tax2.calculateTax(line.getAmtSource(), false, getStdPrecision());
					
					fact.createLine (line, expense,
						getC_Currency_ID(), amt.add(LineNetAmtTax2), dAmt);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					//
					if (line.getM_Product_ID() != 0
						&& line.getProduct().isService())	//	otherwise Inv Matching
						MCostDetail.createInvoice(as, line.getAD_Org_ID(), 
							line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							line.get_ID(), 0,		//	No Cost Element
							line.getAmtSource(), line.getQty(),
							line.getDescription(), getTrxName());
				}
				if (line.isAsset)   //assets
				{
					BigDecimal amt = line.getAmtSource();
					
					int acct_id=DB.getSQLValue("A_Asset_Group_Acct","select A_ASSET_ACCT from A_Asset_Group_Acct "
					+" where A_Asset_Group_ID="+ line.AssetGroup_ID  +" and AD_CLient_ID="+getAD_Client_ID());
				    log.info("comb_ID:"+ acct_id);
					MAccount myacct = new MAccount(getCtx(), acct_id,null);
					//MAccount myacct= MAccount.get(getCtx(),getAD_Client_ID(),0,as.getC_AcctSchema_ID(),acct_id
					//,0,0, 0,0,0,0,0,0,0,0,0,0,0,0);
				   /* if(line.myTax_ID!=1000057)
					fact.createLine (line, myacct,
						getC_Currency_ID(), line.getAmtSource(), null);
					else
					{*/
						MTax tax2 =MTax.get(getCtx(), line.getC_Tax_ID());
						BigDecimal LineNetAmtTax2 = tax2.calculateTax(amt, false, getStdPrecision());
						fact.createLine (line, myacct,
						getC_Currency_ID(), line.getAmtSource().add(LineNetAmtTax2), null);
					
				//	}
				
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}

			//  Liability               CR
			int payables_ID = getValidCombination_ID (Doc.ACCTTYPE_APB, as);
			int payablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_APB, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payables_ID),
					getC_Currency_ID(), null, grossAmt.subtract(TotalTax));  //Fabian
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
					getC_Currency_ID(), null, serviceAmt.subtract(TotalTax)); //Fabian
			//
			updateProductPO(as);	//	Only API
			updateProductInfo (as.getC_AcctSchema_ID());    //  only API	
			
		}
		else if (getDocumentType().equals(DOCTYPE_PTCheck) && 
				(client.getName().toLowerCase().contains("pedro de valdivia") || client.getName().toLowerCase().contains("geminis"))
				&& invoice.getC_DocType().getName().toLowerCase().contains("reverso")) //faaguilar OFB PTK
		{
			log.config("if cpv o geminis");
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			//  Header Charge           CR
			
			//  TaxDue                  CR
			/*for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), null, amt);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}*/
			
			for (int z = 0; z < p_lines.length; z++)
			{
				//if(p_lines[z].m_Payment_ID>0)
				//{
				int receivables_ID = 0;
				int receivablesServices_ID = 0;
				int charge_IDAcct = 0;
				if(p_lines[z].m_Payment_ID>0)
				{
					MPayment pay = new MPayment (getCtx(), p_lines[z].m_Payment_ID,getTrxName());
					String SqlDetPay = "select fact_acct_id,account_id,amtsourcedr,amtsourcecr from fact_acct where record_id = "+pay.get_ID()+
					" and AD_Table_ID = 335 ";
					
					PreparedStatement pstmtDetP = null;
					ResultSet rsDetP = null;
					try
					{
						pstmtDetP = DB.prepareStatement(SqlDetPay, getTrxName());						
						rsDetP = pstmtDetP.executeQuery();
						//
						while (rsDetP.next())
						{
							BigDecimal sourceDR = rsDetP.getBigDecimal("amtsourcedr");
							BigDecimal sourceCR = rsDetP.getBigDecimal("amtsourcecr");
							//int account_ID = getValidCombination_ID(rsDetP.getInt("account_id"), as);							
							if(sourceDR.compareTo(Env.ZERO) != 0)
							{
								fact.createLine(null, MAccount.get(new X_Fact_Acct(getCtx(), rsDetP.getInt("fact_acct_id"),	getTrxName())),
										getC_Currency_ID(), null, sourceDR);
							}
							else if(sourceCR.compareTo(Env.ZERO) != 0)
							{
								fact.createLine(null, MAccount.get(new X_Fact_Acct(getCtx(), rsDetP.getInt("fact_acct_id"),	getTrxName())),
										getC_Currency_ID(), sourceCR, null);
							}
						}
					}
					catch (SQLException e)
					{
						log.log(Level.SEVERE, SqlDetPay, e);
					}
					finally {
						DB.close(rsDetP, pstmtDetP);
						rsDetP = null; pstmtDetP = null;
					}
				}
				else if (p_lines[z].getC_Charge_ID()> 0)
				{
					receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_PTCheck, as);
					receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_PTCheck, as);
					String sqlCVC_ID = "SELECT CH_Expense_Acct FROM C_Charge_Acct WHERE C_Charge_ID = "+p_lines[z].getC_Charge_ID()+" AND C_AcctSchema_ID=?";
					charge_IDAcct = DB.getSQLValue(getTrxName(), sqlCVC_ID, as.get_ID());
					log.config("charge_IDAcct ID Valid Combination : "+charge_IDAcct);
					if (m_allLinesItem || !as.isPostServices() 
							|| receivables_ID == receivablesServices_ID)
					{
						grossAmt = p_lines[z].getAmtSource();
						serviceAmt = Env.ZERO;
					}
					else if (m_allLinesService)
					{
						serviceAmt = p_lines[z].getAmtSource();
						grossAmt = Env.ZERO;
					}
					BigDecimal amt  = Env.ZERO;
					if (grossAmt.signum() != 0){					
						fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
							getC_Currency_ID(), grossAmt, null);
						amt = grossAmt;
					}
					if (serviceAmt.signum() != 0){
						fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
							getC_Currency_ID(), serviceAmt, null);
						amt = serviceAmt;
					}
					log.config("Variable amt = "+amt);
					if (amt != null && amt.signum() != 0)
						fact.createLine(null, MAccount.get(getCtx(), charge_IDAcct),
							getC_Currency_ID(), null, amt);
					
				}
			}
		}
		//nuevo tipo ap pero cambio de documento reverso
		else if (getDocumentType().equals(DOCTYPE_AP_VDC) && client.getName().toLowerCase().contains("pedro de valdivia") && invoice.getC_DocType().getName().toLowerCase().contains("reverso")) //faaguilar OFB PTK
		{
			log.config("if cpv");
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			//  Header Charge           CR
			
			//  TaxDue                  CR
			/*for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), null, amt);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}*/
			
			for (int z = 0; z < p_lines.length; z++)
			{
				//if(p_lines[z].m_Payment_ID>0)
				//{
				int receivables_ID = 0;
				int receivablesServices_ID = 0;
				int charge_IDAcct = 0;
				if(p_lines[z].m_Payment_ID>0)
				{
					MPayment pay = new MPayment (getCtx(), p_lines[z].m_Payment_ID,getTrxName());
					String SqlDetPay = "select fact_acct_id,account_id,amtsourcedr,amtsourcecr from fact_acct where record_id = "+pay.get_ID()+
					" and AD_Table_ID = 335 ";
					
					PreparedStatement pstmtDetP = null;
					ResultSet rsDetP = null;
					try
					{
						pstmtDetP = DB.prepareStatement(SqlDetPay, getTrxName());						
						rsDetP = pstmtDetP.executeQuery();
						//
						while (rsDetP.next())
						{
							BigDecimal sourceDR = rsDetP.getBigDecimal("amtsourcedr");
							BigDecimal sourceCR = rsDetP.getBigDecimal("amtsourcecr");
							//int account_ID = getValidCombination_ID(rsDetP.getInt("account_id"), as);							
							if(sourceDR.compareTo(Env.ZERO) != 0)
							{
								fact.createLine(null, MAccount.get(new X_Fact_Acct(getCtx(), rsDetP.getInt("fact_acct_id"),	getTrxName())),
										getC_Currency_ID(), null, sourceDR);
							}
							else if(sourceCR.compareTo(Env.ZERO) != 0)
							{
								fact.createLine(null, MAccount.get(new X_Fact_Acct(getCtx(), rsDetP.getInt("fact_acct_id"),	getTrxName())),
										getC_Currency_ID(), sourceCR, null);
							}
						}
					}
					catch (SQLException e)
					{
						log.log(Level.SEVERE, SqlDetPay, e);
					}
					finally {
						DB.close(rsDetP, pstmtDetP);
						rsDetP = null; pstmtDetP = null;
					}
				}
				else if (p_lines[z].getC_Charge_ID()> 0)
				{
					receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_PTCheck, as);
					receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_PTCheck, as);
					String sqlCVC_ID = "SELECT CH_Expense_Acct FROM C_Charge_Acct WHERE C_Charge_ID = "+p_lines[z].getC_Charge_ID()+" AND C_AcctSchema_ID=?";
					charge_IDAcct = DB.getSQLValue(getTrxName(), sqlCVC_ID, as.get_ID());
					log.config("charge_IDAcct ID Valid Combination : "+charge_IDAcct);
					if (m_allLinesItem || !as.isPostServices() 
							|| receivables_ID == receivablesServices_ID)
					{
						grossAmt = p_lines[z].getAmtSource();
						serviceAmt = Env.ZERO;
					}
					else if (m_allLinesService)
					{
						serviceAmt = p_lines[z].getAmtSource();
						grossAmt = Env.ZERO;
					}
					BigDecimal amt  = Env.ZERO;
					if (grossAmt.signum() != 0){					
						fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
							getC_Currency_ID(), grossAmt, null);
						amt = grossAmt;
					}
					if (serviceAmt.signum() != 0){
						fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
							getC_Currency_ID(), serviceAmt, null);
						amt = serviceAmt;
					}
					log.config("Variable amt = "+amt);
					if (amt != null && amt.signum() != 0)
						fact.createLine(null, MAccount.get(getCtx(), charge_IDAcct),
							getC_Currency_ID(), null, amt);
					
				}
			}
		}
		else if (getDocumentType().equals(DOCTYPE_PTCheck)) //faaguilar OFB PTK
		{
			log.config("if normal");
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			
			//  Header Charge           CR
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			if (amt != null && amt.signum() != 0)
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
					getC_Currency_ID(), null, amt);
			//  TaxDue                  CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), null, amt);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			//  Revenue                 CR
			for (int i = 0; i < p_lines.length; i++)
			{
				amt = p_lines[i].getAmtSource();
				BigDecimal dAmt = null;
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{
						amt = amt.add(discount);
						dAmt = discount;
					}
				}
				fact.createLine (p_lines[i],
					p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as),
					getC_Currency_ID(), dAmt, amt);
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
			}
			
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
			
			for (int z = 0; z < p_lines.length; z++)
			{
				//if(p_lines[z].m_Payment_ID>0)
				//{
				int receivables_ID = 0;
				int receivablesServices_ID = 0;
				if(p_lines[z].m_Payment_ID>0)
				{
					MPayment pay = new MPayment (getCtx(), p_lines[z].m_Payment_ID,getTrxName());
					if("L".equals(pay.getTenderType()))
					{
						 receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_PTLetra, as);
						 receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_PTLetra, as);
					}
					else
					{
						 receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_PTCheck, as);
						 receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_PTCheck, as);
						
					}
				}
				else
				{
					receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_PTCheck, as);
					receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_PTCheck, as);
				}
				if (m_allLinesItem || !as.isPostServices() 
					|| receivables_ID == receivablesServices_ID)
				{
					grossAmt = p_lines[z].getAmtSource();
					serviceAmt = Env.ZERO;
				}
				else if (m_allLinesService)
				{
					serviceAmt = p_lines[z].getAmtSource();
					grossAmt = Env.ZERO;
				}
				if (grossAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
						getC_Currency_ID(), grossAmt, null);
				if (serviceAmt.signum() != 0)
					fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
						getC_Currency_ID(), serviceAmt, null);
					
			    //}
			}
		}
		//END faaguilar OFB PTK Protested
		else if (getDocumentType().equals(DOCTYPE_AR_CDC)) //faaguilar OFB Customer document change
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			
			//  Header Charge           CR
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			
			if (amt != null && amt.signum() != 0){
				 fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
					getC_Currency_ID(), null, amt);
				log.info("amt cahrge:" + amt);
			}
			
			//  TaxDue                  CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), null, amt);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			//  Revenue                 CR
			for (int i = 0; i < p_lines.length; i++)
			{
				amt = p_lines[i].getAmtSource();
				BigDecimal dAmt = null;
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{
						amt = amt.add(discount);
						dAmt = discount;
					}
				}
				fact.createLine (p_lines[i],
					p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as),
					getC_Currency_ID(), dAmt, amt);
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
				
				
			}
			
			//----------------
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			MInvoiceLine[] lines = invoice.getLines(true);
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
					fLines[i].setAD_Org_ID(lines[i].getAD_Org_ID());
					
					if(fLines[i].getAmtAcctCr().signum()>0 && i<lines.length){
						
						if(lines[i].get_ValueAsInt("C_Payment_ID") > 0)
						{
							MPayment pay = new MPayment( getCtx()  ,lines[i].get_ValueAsInt("C_Payment_ID") ,getTrxName() );
							fLines[i].setC_BPartner_ID(pay.getC_BPartner_ID());
							
							log.info("payment found");
						}
						else if(lines[i].get_ValueAsInt("C_InvoiceFac_ID") > 0)
						{
							MInvoice inv = new MInvoice ( getCtx()  ,lines[i].get_ValueAsInt("C_InvoiceFac_ID") ,getTrxName() );
							fLines[i].setC_BPartner_ID(inv.getC_BPartner_ID());
							log.info("invoice found");
						}
					
					}
				}
			}
				
					int receivables_ID = 0;
					int receivablesServices_ID = 0;
								
						 receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_CDC, as);
						 receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_CDC, as);	
					
					if (m_allLinesItem || !as.isPostServices() 
						|| receivables_ID == receivablesServices_ID)
					{
						grossAmt = getAmount(Doc.AMTTYPE_Gross);
						serviceAmt = Env.ZERO;
					}
					else if (m_allLinesService)
					{
						serviceAmt = getAmount(Doc.AMTTYPE_Gross);
						grossAmt = Env.ZERO;
					}
					if (grossAmt.signum() != 0)
						fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
							getC_Currency_ID(), grossAmt, null);
					if (serviceAmt.signum() != 0)
						fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
							getC_Currency_ID(), serviceAmt, null);
					
			
		}
		//END faaguilar OFB Customer document change
		else if (getDocumentType().equals(DOCTYPE_AP_VDC)) //faaguilar OFB Vendor document change
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			
			BigDecimal camt = getAmount(Doc.AMTTYPE_Charge);
			
			if (camt != null && camt.signum() != 0){
				 fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
					getC_Currency_ID(), getAmount(Doc.AMTTYPE_Charge), null);
				 log.info("amt charge:" + camt);
				 log.config("fact created");
			}
			
			//  TaxCredit       DR
			for (int i = 0; i < m_taxes.length; i++)
			{
				FactLine tl = fact.createLine(null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					getC_Currency_ID(), m_taxes[i].getAmount(), null);
				log.config("*fact created");
				if (tl != null)
					tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
			}
			//  Expense         DR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
					
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
						}
					}
					log.config("**fact created");
					 fact.createLine (line, expense,
						getC_Currency_ID(), amt, dAmt);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					//
				
				
				
			}
			
			//----------------
			//  Set Locations
			
			FactLine[] fLines = fact.getLines();
			MInvoiceLine[] lines = invoice.getLines(true);
			log.config("fLines.length:"+ fLines.length+ "lines.length:"+lines.length);
		//  Set Locations
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
					fLines[i].setAD_Org_ID(lines[i].getAD_Org_ID());
					
					if(fLines[i].getAmtAcctDr().signum()>0 && i<lines.length){
						
							if(lines[i].get_ValueAsInt("C_Payment_ID") > 0)
							{
								MPayment pay = new MPayment( getCtx()  ,lines[i].get_ValueAsInt("C_Payment_ID") ,getTrxName() );
								fLines[i].setC_BPartner_ID(pay.getC_BPartner_ID());
								
								log.info("payment found");
							}
							else if(lines[i].get_ValueAsInt("C_InvoiceFac_ID") > 0)
							{
								MInvoice inv = new MInvoice ( getCtx()  ,lines[i].get_ValueAsInt("C_InvoiceFac_ID") ,getTrxName() );
								fLines[i].setC_BPartner_ID(inv.getC_BPartner_ID());
								log.info("invoice found");
							}
						
					}
				}
			}
			
					int receivables_ID = 0;
					int receivablesServices_ID = 0;
					
					
						 receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_VDC, as);
						 receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_VDC, as);
					
					if (m_allLinesItem || !as.isPostServices() 
						|| receivables_ID == receivablesServices_ID)
					{
						grossAmt = getAmount(Doc.AMTTYPE_Gross);
						serviceAmt = Env.ZERO;
					}
					else if (m_allLinesService)
					{
						serviceAmt = getAmount(Doc.AMTTYPE_Gross);
						grossAmt = Env.ZERO;
					}
					if (grossAmt.signum() != 0)
						fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
							getC_Currency_ID(), null, grossAmt);
					if (serviceAmt.signum() != 0)
						fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
							getC_Currency_ID(), null, serviceAmt);
		
			
			
		}
		//END faaguilar OFB Vendor document change
		else if (getDocumentType().equals(DOCTYPE_AR_FAT) && invoice.isSOTrx())//faaguilar ofb accounting for factoring doc
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			
			//  Header Charge           CR
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			if (amt != null && amt.signum() != 0)
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
					getC_Currency_ID(), null, amt);
			//  TaxDue                  CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), null, amt);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			//  Revenue                 CR
			
			
			for (int i = 0; i < p_lines.length; i++)
			{
				//faaguilar OFB begin
				MInvoiceLine invl =  new MInvoiceLine (getCtx(),p_lines[i].get_ID(), getTrxName());
				MAccount account = null;
				if(invl.get_ValueAsInt("C_Payment_ID") > 0)
				{
					account= getAccount(invl.get_ValueAsInt("C_Payment_ID"), 0);
					
				}
				else if(invl.get_ValueAsInt("C_InvoiceFac_ID") > 0)
				{
					account= getAccount(0, invl.get_ValueAsInt("C_InvoiceFac_ID"));
				}
				else
					account = p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as);
				//faaguilar OFB end
				
				amt = p_lines[i].getAmtSource();
				BigDecimal dAmt = null;
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{
						amt = amt.add(discount);
						dAmt = discount;
						fact.createLine (p_lines[i],
								p_lines[i].getAccount(ProductCost.ACCTTYPE_P_TDiscountGrant, as),
								getC_Currency_ID(), dAmt, null);
					}
				}
				fact.createLine (p_lines[i],
						account,
						getC_Currency_ID(), null, amt);//faaguilar OFB
				/*fact.createLine (p_lines[i],
					p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as),
					getC_Currency_ID(), null, amt);*/
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
			
			//  Receivables     DR
			int receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_FAT, as);
			int receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_FAT, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| receivables_ID == receivablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
					getC_Currency_ID(), grossAmt, null);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
					getC_Currency_ID(), serviceAmt, null);
			//ininoles nuevas lineas de factoring CPV
			if (grossAmt.signum() != 0)
			{
				log.severe("valor antes de fact grossAmt="+grossAmt);
				fact.createLine(null, MAccount.get(getCtx(), getValidCombination_ID(Doc.ACCTTYPE_FactoringD, as) ),
					getC_Currency_ID(), grossAmt, null);
				fact.createLine(null, MAccount.get(getCtx(), getValidCombination_ID(Doc.ACCTTYPE_FactoringC, as) ),
					getC_Currency_ID(), null, grossAmt);
			}
			//ininoles end
		}
		//ininoles nueva contabilidad AP Factoring sin extinguir
		else if (getDocumentType().equals(DOCTYPE_AR_FAT) && 
				invoice.isSOTrx()== false && invoice.get_ValueAsBoolean("Extinguir") == false)//faaguilar ofb accounting for factoring doc
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			
			//  Header Charge           CR
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			if (amt != null && amt.signum() != 0)
				fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
					getC_Currency_ID(),amt, null);
			//  TaxDue                  CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), amt, null);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			//  Revenue                 CR
			
			
			for (int i = 0; i < p_lines.length; i++)
			{
				//faaguilar OFB begin
				MInvoiceLine invl =  new MInvoiceLine (getCtx(),p_lines[i].get_ID(), getTrxName());
				MAccount account = null;
				if(invl.get_ValueAsInt("C_Payment_ID") > 0)
				{
					account= getAccount(invl.get_ValueAsInt("C_Payment_ID"), 0);
					
				}
				else if(invl.get_ValueAsInt("C_InvoiceFac_ID") > 0)
				{
					account= getAccount(0, invl.get_ValueAsInt("C_InvoiceFac_ID"));
				}
				else
					account = p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as);
				//faaguilar OFB end
				
				amt = p_lines[i].getAmtSource();
				BigDecimal dAmt = null;
				if (as.isTradeDiscountPosted())
				{
					BigDecimal discount = p_lines[i].getDiscount();
					if (discount != null && discount.signum() != 0)
					{
						amt = amt.add(discount);
						dAmt = discount;
						fact.createLine (p_lines[i],
								p_lines[i].getAccount(ProductCost.ACCTTYPE_P_TDiscountGrant, as),
								getC_Currency_ID(),null, dAmt);
					}
				}
				fact.createLine (p_lines[i],
						account,
						getC_Currency_ID(),amt,null);//faaguilar OFB
				/*fact.createLine (p_lines[i],
					p_lines[i].getAccount(ProductCost.ACCTTYPE_P_Revenue, as),
					getC_Currency_ID(), null, amt);*/
				if (!p_lines[i].isItem())
				{
					grossAmt = grossAmt.subtract(amt);
					serviceAmt = serviceAmt.add(amt);
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);      //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
			
			//  Receivables     DR
			int receivables_ID = getValidCombination_ID(Doc.ACCTTYPE_FAT, as);
			int receivablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_FAT, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| receivables_ID == receivablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivables_ID),
					getC_Currency_ID(),null, grossAmt);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), receivablesServices_ID),
					getC_Currency_ID(),null, serviceAmt);
			//ininoles nuevas lineas de factoring CPV
			if (grossAmt.signum() != 0)
			{
				log.severe("valor antes de fact grossAmt="+grossAmt);
				fact.createLine(null, MAccount.get(getCtx(), getValidCombination_ID(Doc.ACCTTYPE_FactoringD, as) ),
					getC_Currency_ID(),null, grossAmt);
				fact.createLine(null, MAccount.get(getCtx(), getValidCombination_ID(Doc.ACCTTYPE_FactoringC, as) ),
					getC_Currency_ID(), grossAmt,null);
			}
			//ininoles end
		}
		//ininoles AP Factoring marcado para extinguir
		else if (getDocumentType().equals(DOCTYPE_AR_FAT) && 
				invoice.isSOTrx()== false && invoice.get_ValueAsBoolean("Extinguir"))//faaguilar ofb accounting for factoring doc
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);			
			log.severe("valor grossAmt="+grossAmt);			
			//  Header Charge           CR
			BigDecimal amt = getAmount(Doc.AMTTYPE_Charge);
			
			//  TaxDue                  CR
			for (int i = 0; i < m_taxes.length; i++)
			{
				amt = m_taxes[i].getAmount();
				if (amt != null && amt.signum() != 0)
				{
					FactLine tl = fact.createLine(null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
						getC_Currency_ID(), amt, null);
					if (tl != null)
						tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
				}
			}
			//ininoles nuevas lineas de factoring CPV
			if (grossAmt.signum() != 0)
			{
				log.severe("valor antes de fact grossAmt="+grossAmt);		
				fact.createLine(null, MAccount.get(getCtx(), getValidCombination_ID(Doc.ACCTTYPE_FactoringD, as) ),
						getC_Currency_ID(),null, grossAmt);
					fact.createLine(null, MAccount.get(getCtx(), getValidCombination_ID(Doc.ACCTTYPE_FactoringC, as) ),
						getC_Currency_ID(), grossAmt,null);
			}
			//ininoles end
		}
		//APS Invoice Service faaguilar OFB
		else if (getDocumentType().equals(DOCTYPE_API_Service))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;

			//  Charge          DR
			fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
				getC_Currency_ID(), getAmount(Doc.AMTTYPE_Charge), null);

			//  Expense         DR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				boolean landedCost = landedCost(as, fact, line, true);
				if (landedCost && as.isExplicitCostAdjustment())
				{
					fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), line.getAmtSource(), null);
					//
					FactLine fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), null, line.getAmtSource());
					String desc = line.getDescription();
					if (desc == null)
						desc = "100%";
					else
						desc += " 100%";
					fl.setDescription(desc);
				}
				if (!landedCost)
				{
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
							MAccount tradeDiscountReceived = line.getAccount(ProductCost.ACCTTYPE_P_TDiscountRec, as);
							fact.createLine (line, tradeDiscountReceived,
									getC_Currency_ID(), null, dAmt);
						}
					}
					fact.createLine (line, expense,
						getC_Currency_ID(), amt, null);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					//
					if (line.getM_Product_ID() != 0
						&& line.getProduct().isService())	//	otherwise Inv Matching
						MCostDetail.createInvoice(as, line.getAD_Org_ID(), 
							line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							line.get_ID(), 0,		//	No Cost Element
							line.getAmtSource(), line.getQty(),
							line.getDescription(), getTrxName());
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}

			//  Liability               CR
			int payables_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability, as);
			int payablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability_Services, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payables_ID),
					getC_Currency_ID(), null, grossAmt);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
					getC_Currency_ID(), null, serviceAmt);
			//
			updateProductPO(as);	//	Only API
			updateProductInfo (as.getC_AcctSchema_ID());    //  only API
		}//APS Invoice Service faaguilar OFB
		//PSC nota credito servicio begin
		else if (getDocumentType().equals(DOCTYPE_PSC_Service))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;
			//  Charge                  CR
			fact.createLine (null, getAccount(Doc.ACCTTYPE_Charge, as),
				getC_Currency_ID(), null, getAmount(Doc.AMTTYPE_Charge));
			//  TaxCredit               CR
		
			//  Expense                 CR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				boolean landedCost = landedCost(as, fact, line, false);
				if (landedCost && as.isExplicitCostAdjustment())
				{
					fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), null, line.getAmtSource());
					//
					FactLine fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), line.getAmtSource(), null);
					String desc = line.getDescription();
					if (desc == null)
						desc = "100%";
					else
						desc += " 100%";
					fl.setDescription(desc);
				}
				if (!landedCost)
				{
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
							MAccount tradeDiscountReceived = line.getAccount(ProductCost.ACCTTYPE_P_TDiscountRec, as);
							fact.createLine (line, tradeDiscountReceived,
									getC_Currency_ID(), dAmt, null);
						}
					}
					fact.createLine (line, expense,
						getC_Currency_ID(), null, amt);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					//
					if (line.getM_Product_ID() != 0
						&& line.getProduct().isService())	//	otherwise Inv Matching
						MCostDetail.createInvoice(as, line.getAD_Org_ID(), 
							line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							line.get_ID(), 0,		//	No Cost Element
							line.getAmtSource().negate(), line.getQty(),
							line.getDescription(), getTrxName());
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}
			//  Liability       DR
			int payables_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability, as);
			int payablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Liability_Services, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payables_ID),
					getC_Currency_ID(), grossAmt, null);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
					getC_Currency_ID(), serviceAmt, null);
		}
		//PSC nota credito servicio end
		//faaguilar OFB APA
		else if (getDocumentType().equals(DOCTYPE_API_Compromise))
		{
			log.config("Compromiso");
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;

			//  Charge          DR
			fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
				getC_Currency_ID(), getAmount(Doc.AMTTYPE_Charge), null);
			//  TaxCredit       DR
			for (int i = 0; i < m_taxes.length; i++)
			{
				FactLine tl = fact.createLine(null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					getC_Currency_ID(), m_taxes[i].getAmount(), null);
				if (tl != null)
					tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
			}
		
			
			MInvoicePaySchedule[] lines = MInvoicePaySchedule.getInvoicePaySchedule(invoice.getCtx(), invoice.getC_Invoice_ID(), 0, invoice.get_TrxName());
			log.config("Cuotas :"+lines.length);
			for (int i = 0; i < lines.length; i++)
			{
				 if(i<12)
				 {
					FactLine fl = fact.createLine (null, getAccount(Doc.ACCTTYPE_APACorto, as, invoice),
							invoice.getC_Currency_ID(), null,lines[i].getDueAmt() );
					
					log.config("Cuotas :"+lines[i].getDueAmt());
					fl.setDescription("Cuota Compromiso");
					
				 }
				 else
				 {
					 FactLine fl = fact.createLine (null, getAccount(Doc.ACCTTYPE_APALargo, as, invoice),
							 invoice.getC_Currency_ID(), null,lines[i].getDueAmt() );
					 
					 fl.setDescription("Cuota Compromiso");
					 log.config("Cuotas :"+lines[i].getDueAmt());		
				 }
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}

			//  Liability               CR
			int compromiseacct_ID = getValidCombination_ID(Doc.ACCTTYPE_APA, as, invoice);
			int payables_ID = compromiseacct_ID;
			int payablesServices_ID = compromiseacct_ID;
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payables_ID),
					getC_Currency_ID(), grossAmt, null );
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
					getC_Currency_ID(), serviceAmt, null );
			//
			updateProductPO(as);	//	Only API
			updateProductInfo (as.getC_AcctSchema_ID());    //  only API
		}
	//  faaguilar OFB AP Leasing
		else if (getDocumentType().equals(DOCTYPE_APLeasing))
		{
			BigDecimal grossAmt = getAmount(Doc.AMTTYPE_Gross);
			BigDecimal serviceAmt = Env.ZERO;

			//  Charge          DR
			fact.createLine(null, getAccount(Doc.ACCTTYPE_Charge, as),
				getC_Currency_ID(), getAmount(Doc.AMTTYPE_Charge), null);
			//  TaxCredit       DR
			for (int i = 0; i < m_taxes.length; i++)
			{
				FactLine tl = fact.createLine(null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					getC_Currency_ID(), m_taxes[i].getAmount(), null);
				if (tl != null)
					tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
			}
			//  Expense         DR
			for (int i = 0; i < p_lines.length; i++)
			{
				DocLine line = p_lines[i];
				boolean landedCost = landedCost(as, fact, line, true);
				if (landedCost && as.isExplicitCostAdjustment())
				{
					fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), line.getAmtSource(), null);
					//
					FactLine fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
						getC_Currency_ID(), null, line.getAmtSource());
					String desc = line.getDescription();
					if (desc == null)
						desc = "100%";
					else
						desc += " 100%";
					fl.setDescription(desc);
				}
				if (!landedCost)
				{
					MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
					if (line.isItem())
						expense = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
					BigDecimal amt = line.getAmtSource();
					BigDecimal dAmt = null;
					if (as.isTradeDiscountPosted() && !line.isItem())
					{
						BigDecimal discount = line.getDiscount();
						if (discount != null && discount.signum() != 0)
						{
							amt = amt.add(discount);
							dAmt = discount;
							MAccount tradeDiscountReceived = line.getAccount(ProductCost.ACCTTYPE_P_TDiscountRec, as);
							fact.createLine (line, tradeDiscountReceived,
									getC_Currency_ID(), null, dAmt);
						}
					}
					fact.createLine (line, expense,
						getC_Currency_ID(), amt, null);
					if (!line.isItem())
					{
						grossAmt = grossAmt.subtract(amt);
						serviceAmt = serviceAmt.add(amt);
					}
					//
					if (line.getM_Product_ID() != 0
						&& line.getProduct().isService())	//	otherwise Inv Matching
						/*MCostDetail.createInvoice(as, line.getAD_Org_ID(), 
							line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							line.get_ID(), 0,		//	No Cost Element
							line.getAmtSource(), line.getQty(),
							line.getDescription(), getTrxName());*///faaguilar OFB original code commented
						OFBProductCost.createInvoice(as, line.getAD_Org_ID(), 
								line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
								line.get_ID(), 0,		//	No Cost Element
								line.getAmtSource(), line.getQty(),
								line.getDescription(), getTrxName());//faaguilar OFB
				}
			}
			//  Set Locations
			FactLine[] fLines = fact.getLines();
			for (int i = 0; i < fLines.length; i++)
			{
				if (fLines[i] != null)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
			}

			//  Liability               CR
			int payables_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Leasing, as);
			int payablesServices_ID = getValidCombination_ID (Doc.ACCTTYPE_V_Leasing, as);
			if (m_allLinesItem || !as.isPostServices() 
				|| payables_ID == payablesServices_ID)
			{
				grossAmt = getAmount(Doc.AMTTYPE_Gross);
				serviceAmt = Env.ZERO;
			}
			else if (m_allLinesService)
			{
				serviceAmt = getAmount(Doc.AMTTYPE_Gross);
				grossAmt = Env.ZERO;
			}
			if (grossAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payables_ID),
					getC_Currency_ID(), null, grossAmt);
			if (serviceAmt.signum() != 0)
				fact.createLine(null, MAccount.get(getCtx(), payablesServices_ID),
					getC_Currency_ID(), null, serviceAmt);
			//
			updateProductPO(as);	//	Only API
			updateProductInfo (as.getC_AcctSchema_ID());    //  only API
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
	 * 	Create Fact Cash Based (i.e. only revenue/expense)
	 *	@param as accounting schema
	 *	@param fact fact to add lines to
	 *	@param multiplier source amount multiplier
	 *	@return accounted amount
	 */
	public BigDecimal createFactCash (MAcctSchema as, Fact fact, BigDecimal multiplier)
	{
		boolean creditMemo = getDocumentType().equals(DOCTYPE_ARCredit)
			|| getDocumentType().equals(DOCTYPE_APCredit);
		boolean payables = getDocumentType().equals(DOCTYPE_APInvoice)
			|| getDocumentType().equals(DOCTYPE_APCredit);
		BigDecimal acctAmt = Env.ZERO;
		FactLine fl = null;
		//	Revenue/Cost
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine line = p_lines[i];
			boolean landedCost = false;
			if  (payables)
				landedCost = landedCost(as, fact, line, false);
			if (landedCost && as.isExplicitCostAdjustment())
			{
				fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
					getC_Currency_ID(), null, line.getAmtSource());
				//
				fl = fact.createLine (line, line.getAccount(ProductCost.ACCTTYPE_P_Expense, as),
					getC_Currency_ID(), line.getAmtSource(), null);
				String desc = line.getDescription();
				if (desc == null)
					desc = "100%";
				else
					desc += " 100%";
				fl.setDescription(desc);
			}
			if (!landedCost)
			{
				MAccount acct = line.getAccount(
					payables ? ProductCost.ACCTTYPE_P_Expense : ProductCost.ACCTTYPE_P_Revenue, as);
				if (payables)
				{
					//	if Fixed Asset
					if (line.isItem())
						acct = line.getAccount (ProductCost.ACCTTYPE_P_InventoryClearing, as);
				}
				BigDecimal amt = line.getAmtSource().multiply(multiplier);
				BigDecimal amt2 = null;
				if (creditMemo)
				{
					amt2 = amt;
					amt = null;
				}
				if (payables)	//	Vendor = DR
					fl = fact.createLine (line, acct,
						getC_Currency_ID(), amt, amt2);
				else			//	Customer = CR
					fl = fact.createLine (line, acct,
						getC_Currency_ID(), amt2, amt);
				if (fl != null)
					acctAmt = acctAmt.add(fl.getAcctBalance());
			}
		}
		//  Tax
		for (int i = 0; i < m_taxes.length; i++)
		{
			BigDecimal amt = m_taxes[i].getAmount();
			BigDecimal amt2 = null;
			if (creditMemo)
			{
				amt2 = amt;
				amt = null;
			}
			FactLine tl = null;
			if (payables)
				tl = fact.createLine (null, m_taxes[i].getAccount(m_taxes[i].getAPTaxType(), as),
					getC_Currency_ID(), amt, amt2);
			else
				tl = fact.createLine (null, m_taxes[i].getAccount(DocTax.ACCTTYPE_TaxDue, as),
					getC_Currency_ID(), amt2, amt);
			if (tl != null)
				tl.setC_Tax_ID(m_taxes[i].getC_Tax_ID());
		}
		//  Set Locations
		FactLine[] fLines = fact.getLines();
		for (int i = 0; i < fLines.length; i++)
		{
			if (fLines[i] != null)
			{
				if (payables)
				{
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), true);  //  from Loc
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), false);    //  to Loc
				}
				else
				{
					fLines[i].setLocationFromOrg(fLines[i].getAD_Org_ID(), true);    //  from Loc
					fLines[i].setLocationFromBPartner(getC_BPartner_Location_ID(), false);  //  to Loc
				}
			}
		}
		return acctAmt;
	}	//	createFactCash
	
	
	/**
	 * 	Create Landed Cost accounting & Cost lines
	 *	@param as accounting schema
	 *	@param fact fact
	 *	@param line document line
	 *	@param dr DR entry (normal api)
	 *	@return true if landed costs were created
	 */
	private boolean landedCost (MAcctSchema as, Fact fact, DocLine line, boolean dr) 
	{
		int C_InvoiceLine_ID = line.get_ID();
		MLandedCostAllocation[] lcas = MLandedCostAllocation.getOfInvoiceLine(
			getCtx(), C_InvoiceLine_ID, getTrxName());
		if (lcas.length == 0)
			return false;
		
		//	Calculate Total Base
		double totalBase = 0;
		for (int i = 0; i < lcas.length; i++)
			totalBase += lcas[i].getBase().doubleValue();
		
		//	Create New
		MInvoiceLine il = new MInvoiceLine (getCtx(), C_InvoiceLine_ID, getTrxName());
		for (int i = 0; i < lcas.length; i++)
		{
			MLandedCostAllocation lca = lcas[i];
			if (lca.getBase().signum() == 0)
				continue;
			double percent = lca.getBase().doubleValue() / totalBase;
			String desc = il.getDescription();
			if (desc == null)
				desc = percent + "%";
			else
				desc += " - " + percent + "%";
			if (line.getDescription() != null)
				desc += " - " + line.getDescription(); 
			
			//	Accounting
			ProductCost pc = new ProductCost (Env.getCtx(), 
				lca.getM_Product_ID(), lca.getM_AttributeSetInstance_ID(), getTrxName());
			BigDecimal drAmt = null;
			BigDecimal crAmt = null;
			if (dr)
				drAmt = lca.getAmt();
			else
				crAmt = lca.getAmt();
			FactLine fl = fact.createLine (line, pc.getAccount(ProductCost.ACCTTYPE_P_CostAdjustment, as),
				getC_Currency_ID(), drAmt, crAmt);
			fl.setDescription(desc);
			fl.setM_Product_ID(lca.getM_Product_ID());
			
			//	Cost Detail - Convert to AcctCurrency
			BigDecimal allocationAmt =  lca.getAmt();
			if (getC_Currency_ID() != as.getC_Currency_ID())
				allocationAmt = MConversionRate.convert(getCtx(), allocationAmt, 
					getC_Currency_ID(), as.getC_Currency_ID(),
					getDateAcct(), getC_ConversionType_ID(), 
					getAD_Client_ID(), getAD_Org_ID());
			if (allocationAmt.scale() > as.getCostingPrecision())
				allocationAmt = allocationAmt.setScale(as.getCostingPrecision(), BigDecimal.ROUND_HALF_UP);
			if (!dr)
				allocationAmt = allocationAmt.negate();
			// AZ Goodwill
			// use createInvoice to create/update non Material Cost Detail
			desc="landedCosts ||"+desc;//faaguilar OFB
			/*MCostDetail.createInvoice(as, lca.getAD_Org_ID(), 
					lca.getM_Product_ID(), lca.getM_AttributeSetInstance_ID(),
					C_InvoiceLine_ID, lca.getM_CostElement_ID(),
					allocationAmt,Env.ZERO, // lca.getQty(), faaguilar OFB change
					desc, getTrxName());*///faaguilar OFB original code commented
			OFBProductCost.createInvoice(as, lca.getAD_Org_ID(), 
					lca.getM_Product_ID(), lca.getM_AttributeSetInstance_ID(),
					C_InvoiceLine_ID, lca.getM_CostElement_ID(),
					allocationAmt,Env.ZERO, 
					desc, getTrxName());//faaguilar OFB
			// end AZ
		}
		
		log.config("Created #" + lcas.length);
		return true;
	}	//	landedCosts

	/**
	 * 	Update ProductPO PriceLastInv
	 *	@param as accounting schema
	 */
	private void updateProductPO (MAcctSchema as)
	{
		MClientInfo ci = MClientInfo.get(getCtx(), as.getAD_Client_ID());
		if (ci.getC_AcctSchema1_ID() != as.getC_AcctSchema_ID())
			return;
		
		StringBuffer sql = new StringBuffer (
			"UPDATE M_Product_PO po "
			+ "SET PriceLastInv = "
			//	select
			+ "(SELECT currencyConvert(il.PriceActual,i.C_Currency_ID,po.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID) "
			+ "FROM C_Invoice i, C_InvoiceLine il "
			+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
			+ " AND po.M_Product_ID=il.M_Product_ID AND po.C_BPartner_ID=i.C_BPartner_ID");
			//jz + " AND ROWNUM=1 AND i.C_Invoice_ID=").append(get_ID()).append(") ")
			if (DB.isOracle()) //jz
			{
				sql.append(" AND ROWNUM=1 ");
			}
			else 
			{
				sql.append(" AND il.C_InvoiceLine_ID = (SELECT MIN(il1.C_InvoiceLine_ID) "
						+ "FROM C_Invoice i1, C_InvoiceLine il1 "
						+ "WHERE i1.C_Invoice_ID=il1.C_Invoice_ID"
						+ " AND po.M_Product_ID=il1.M_Product_ID AND po.C_BPartner_ID=i1.C_BPartner_ID")
						.append("  AND i1.C_Invoice_ID=").append(get_ID()).append(") ");
			}
			sql.append("  AND i.C_Invoice_ID=").append(get_ID()).append(") ")
			//	update
			.append("WHERE EXISTS (SELECT * "
			+ "FROM C_Invoice i, C_InvoiceLine il "
			+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
			+ " AND po.M_Product_ID=il.M_Product_ID AND po.C_BPartner_ID=i.C_BPartner_ID"
			+ " AND i.C_Invoice_ID=").append(get_ID()).append(")");
		int no = DB.executeUpdate(sql.toString(), getTrxName());
		log.fine("Updated=" + no);
	}	//	updateProductPO
	
	/**
	 *  Update Product Info (old).
	 *  - Costing (PriceLastInv)
	 *  - PO (PriceLastInv)
	 *  @param C_AcctSchema_ID accounting schema
	 *  @deprecated old costing
	 */
	private void updateProductInfo (int C_AcctSchema_ID)
	{
		log.fine("C_Invoice_ID=" + get_ID());

		/** @todo Last.. would need to compare document/last updated date
		 *  would need to maintain LastPriceUpdateDate on _PO and _Costing */

		//  update Product Costing
		//  requires existence of currency conversion !!
		//  if there are multiple lines of the same product last price uses first
		//	-> TotalInvAmt is sometimes NULL !! -> error
		// begin globalqss 2005-10-19
		// postgresql doesn't support LIMIT on UPDATE or DELETE statements
		/*
		StringBuffer sql = new StringBuffer (
			"UPDATE M_Product_Costing pc "
			+ "SET (PriceLastInv, TotalInvAmt,TotalInvQty) = "
			//	select
			+ "(SELECT currencyConvert(il.PriceActual,i.C_Currency_ID,a.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID),"
			+ " currencyConvert(il.LineNetAmt,i.C_Currency_ID,a.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID),il.QtyInvoiced "
			+ "FROM C_Invoice i, C_InvoiceLine il, C_AcctSchema a "
			+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
			+ " AND pc.M_Product_ID=il.M_Product_ID AND pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
			+ " AND ROWNUM=1"
			+ " AND pc.C_AcctSchema_ID=").append(C_AcctSchema_ID).append(" AND i.C_Invoice_ID=")
			.append(get_ID()).append(") ")
			//	update
			.append("WHERE EXISTS (SELECT * "
			+ "FROM C_Invoice i, C_InvoiceLine il, C_AcctSchema a "
			+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
			+ " AND pc.M_Product_ID=il.M_Product_ID AND pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
			+ " AND pc.C_AcctSchema_ID=").append(C_AcctSchema_ID).append(" AND i.C_Invoice_ID=")
				.append(get_ID()).append(")");
		*/
		// the next command is equivalent and works in postgresql and oracle
		StringBuffer sql = new StringBuffer (
				"UPDATE M_Product_Costing pc "
				+ "SET (PriceLastInv, TotalInvAmt,TotalInvQty) = "
				//	select
				+ "(SELECT currencyConvert(il.PriceActual,i.C_Currency_ID,a.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID),"
				+ " currencyConvert(il.LineNetAmt,i.C_Currency_ID,a.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID),il.QtyInvoiced "
				+ "FROM C_Invoice i, C_InvoiceLine il, C_AcctSchema a "
				+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
				+ " AND il.c_invoiceline_id = (SELECT MIN(C_InvoiceLine_ID) FROM C_InvoiceLine il2" +
						" WHERE  il2.M_PRODUCT_ID=il.M_PRODUCT_ID AND C_Invoice_ID=")
				.append(get_ID()).append(")"
				+ " AND pc.M_Product_ID=il.M_Product_ID AND pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
				+ " AND pc.C_AcctSchema_ID=").append(C_AcctSchema_ID).append(" AND i.C_Invoice_ID=")
				.append(get_ID()).append(") ")
				//	update
				.append("WHERE EXISTS (SELECT * "
				+ "FROM C_Invoice i, C_InvoiceLine il, C_AcctSchema a "
				+ "WHERE i.C_Invoice_ID=il.C_Invoice_ID"
				+ " AND pc.M_Product_ID=il.M_Product_ID AND pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
				+ " AND pc.C_AcctSchema_ID=").append(C_AcctSchema_ID).append(" AND i.C_Invoice_ID=")
					.append(get_ID()).append(")");
		// end globalqss 2005-10-19
		int no = DB.executeUpdate(sql.toString(), getTrxName());
		log.fine("M_Product_Costing - Updated=" + no);
	}   //  updateProductInfo

	
	/**
	 * faaguilar OFB
	 * custom methods*/
	public MAccount getAccount (int payment_ID, int invoice_ID)
		{
			
			MAccount account =null;
			String sql = "SELECT * "
				+ "FROM Fact_Acct "
				+ "WHERE AD_Table_ID = ? AND Record_ID=?"
				+ " AND AmtAcctDr>0";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, getTrxName());
				
				if(payment_ID>0){
					pstmt.setInt(1,335);
					pstmt.setInt(2, payment_ID);
				}
				else{
					pstmt.setInt(1, 318);
					pstmt.setInt(2, invoice_ID);
				}
				
				rs = pstmt.executeQuery();
				if (rs.next())
					account= MAccount.get( new X_Fact_Acct (getCtx(),rs,getTrxName()));
			
			}catch (SQLException e)
				{
					log.log(Level.SEVERE, sql, e);
				}
				finally {
					DB.close(rs, pstmt);
					rs = null; pstmt = null;
				}
				return account;
		}   //  getAccount
	
	/**
	 *	Get the account for Accounting Schema
	 *  @param AcctType see ACCTTYPE_*
	 *  @param as accounting schema
	 *  @return Account
	 */
	private final MAccount getAccount (int AcctType, MAcctSchema as, MInvoice invoice)
	{
		int C_ValidCombination_ID = getValidCombination_ID(AcctType, as, invoice);
		if (C_ValidCombination_ID == 0)
			return null;
		//	Return Account
		MAccount acct = MAccount.get (as.getCtx(), C_ValidCombination_ID);
		log.config("acct:"+acct);
		return acct;
	}	//	getAccount
	
	/**
	 *	Get the Valid Combination id for Accounting Schema
	 *  @param AcctType see ACCTTYPE_*
	 *  @param as accounting schema
	 *  @return C_ValidCombination_ID
	 */
	
		
	private int getValidCombination_ID (int AcctType, MAcctSchema as, MInvoice invoice)
	{
		int para_1 = 0;     //  first parameter (second is always AcctSchema)
		String sql = null;
		
		if (AcctType == ACCTTYPE_APA)//faaguilar OFB compromisos
		{
			sql = "SELECT C_CompAdq_Acct FROM C_DocType WHERE C_DocType_ID=?";
			para_1 = invoice.getC_DocType_ID();
        }
        else if (AcctType == ACCTTYPE_APALargo)//faaguilar OFB compromisos Largo
		{
			sql = "SELECT C_CompAdqLargo_Acct FROM C_DocType WHERE C_DocType_ID=?";
			para_1 = invoice.getC_DocType_ID();
        }
        else if (AcctType == ACCTTYPE_APACorto)//faaguilar OFB compromisos Corto
		{
			sql = "SELECT C_CompAdqCorto_Acct FROM C_DocType WHERE C_DocType_ID=?";
			para_1 = invoice.getC_DocType_ID();
        }
		
	//  Get Acct
			int Account_ID = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt (1, para_1);
				
				rs = pstmt.executeQuery();
				if (rs.next())
					Account_ID = rs.getInt(1);
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, "AcctType=" + AcctType + " - SQL=" + sql, e);
				return 0;
			}
			finally {
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
			//	No account
			if (Account_ID == 0)
			{
				log.severe ("NO account Type="
					+ AcctType + ", Record=" + invoice.get_ID());
				return 0;
			}
			log.config("Account_ID :"+Account_ID);
			return Account_ID;
	}
}   //  Doc_Invoice
