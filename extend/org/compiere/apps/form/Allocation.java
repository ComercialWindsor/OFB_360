/******************************************************************************
 * Copyright (C) 2009 Low Heng Sin                                            *
 * Copyright (C) 2009 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.compiere.apps.form;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.table.TableModel;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.acct.Doc;
import org.compiere.apps.ADialog;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.MRole;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;
import org.ofb.model.OFBForward;

public class Allocation
{
	public DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);

	/**	Logger			*/
	public static CLogger log = CLogger.getCLogger(Allocation.class);

	private boolean     m_calculating = false;
	public int         	m_C_Currency_ID = 0;
	public int         	m_C_BPartner_ID = 0;
	private int         m_noInvoices = 0;
	private int         m_noPayments = 0;
	public BigDecimal	totalInv = new BigDecimal(0.0);
	public BigDecimal 	totalPay = new BigDecimal(0.0);
	public BigDecimal	totalDiff = new BigDecimal(0.0);
	//faaguilar OFB Multiallocation
	protected static final int INDICATOR_NONE = 0;
	protected static final int INDICATOR_TOTAL_PAY = 1;
	protected static final int INDICATOR_SUBPAYMENT_SO = 2;
	protected static final int INDICATOR_GREATER_PAYMENT_SO = 3;
	protected static final int INDICATOR_CREDIT_MEMO = 4;
	protected static final int INDICATOR_GREATER_PAYMENT_PO = 5;
	protected static final int INDICATOR_SUBPAYMENT_PO = 6;
	protected static final int INDICATOR_VERSUS_INVOICE = 7;
	protected static final int INDICATOR_VERSUS_SUBINVOICE = 8;
	protected static final int INDICATOR_WRITEOFF_INVOICE=9;
	protected static final int INDICATOR_VERSUS_PAYMENT=10;
	protected static final int INDICATOR_VERSUS_SUBPAYMENT=11;
	protected static final int INDICATOR_CURRENCY_DIFF=12;
	
    BigDecimal totalPayment = new BigDecimal(0.0);
	BigDecimal totalInvoiced = new BigDecimal(0.0);
	BigDecimal totalCredit = new BigDecimal(0.0);
	BigDecimal pvspamt = new BigDecimal(0.0);
	int indicator = INDICATOR_NONE;
	boolean multiprocess=false;
	//faaguilar OFB Multiallocation END
	public Timestamp allocDate = null;

	//  Index	changed if multi-currency
	private int         i_payment = 7;
	//
	private int         i_open = 6;
	private int         i_discount = 7;
	private int         i_writeOff = 8; 
	private int         i_applied = 9;
	private int 		i_overUnder = 10;
//	private int			i_multiplier = 10;
	private int 		i_cuota = 11;
	
	public int         	m_AD_Org_ID = 0;
	public int         	m_AD_OrgDoc_ID = 0;//faaguilar OFB

	private ArrayList<Integer>	m_bpartnerCheck = new ArrayList<Integer>(); 

	public void dynInit() throws Exception
	{
		m_C_Currency_ID = Env.getContextAsInt(Env.getCtx(), "$C_Currency_ID");   //  default
		//
		log.info("Currency=" + m_C_Currency_ID);
		
		m_AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());
	}
	
	/**
	 *  Load Business Partner Info
	 *  - Payments
	 *  - Invoices
	 */
	public void checkBPartner()
	{		
		log.config("BPartner=" + m_C_BPartner_ID + ", Cur=" + m_C_Currency_ID);
		//  Need to have both values
		if (m_C_BPartner_ID == 0 || m_C_Currency_ID == 0)
			return;

		//	Async BPartner Test
		Integer key = new Integer(m_C_BPartner_ID);
		if (!m_bpartnerCheck.contains(key))
		{
			new Thread()
			{
				public void run()
				{
					MPayment.setIsAllocated (Env.getCtx(), m_C_BPartner_ID, null);
					MInvoice.setIsPaid (Env.getCtx(), m_C_BPartner_ID, null);
				}
			}.start();
			m_bpartnerCheck.add(key);
		}
	}
	
	public Vector<Vector<Object>> getPaymentData(boolean isMultiCurrency, Object date, IMiniTable paymentTable)
	{		
		/********************************
		 *  Load unallocated Payments
		 *      1-TrxDate, 2-DocumentNo, (3-Currency, 4-PayAmt,)
		 *      5-ConvAmt, 6-ConvOpen, 7-Allocated
		 */
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuffer sql = new StringBuffer("SELECT p.DateAcct,p.DocumentNo,p.C_Payment_ID,"  //  1..3
			+ "c.ISO_Code,p.PayAmt,"                            //  4..5
			+ "currencyConvert(p.PayAmt,p.C_Currency_ID,?,?,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID),"//  6   #1, #2
			+ "currencyConvert(paymentAvailable(C_Payment_ID),p.C_Currency_ID,?,?,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID),"  //  7   #3, #4
			+ "p.MultiplierAP, doc.name||'-'||l2.name " //faaguilar documenttype 9
			+ "FROM C_Payment_v p"		//	Corrected for AP/AR
			+ " INNER JOIN C_Currency c ON (p.C_Currency_ID=c.C_Currency_ID) "
			+ " INNER join C_Doctype doc ON (p.C_DocType_ID=doc.C_DocType_ID) " //faaguilar documenttype
			+ " LEFT OUTER JOIN AD_Ref_List l2 ON (p.TenderType=l2.value and l2.AD_Reference_ID=214) "//faaguilar documenttype	
			+ "WHERE p.IsAllocated='N' AND p.Processed='Y'"
			+ " AND p.C_Charge_ID IS NULL"		//	Prepayments OK
			+ " AND p.C_BPartner_ID=?");                   		//      #5
		if (!isMultiCurrency)
			sql.append(" AND p.C_Currency_ID=?");				//      #6
		if (m_AD_Org_ID != 0 )
			sql.append(" AND p.AD_Org_ID=" + m_AD_Org_ID);
		//sql.append(" AND (p.Custodio is null OR p.Custodio not in ('A'))");//faaguilar OFB//se saca logica por peticion de CPV
		sql.append(" ORDER BY p.DateTrx,p.DocumentNo");
		
		// role security
		sql = new StringBuffer( MRole.getDefault(Env.getCtx(), false).addAccessSQL( sql.toString(), "p", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO ) );
		
		log.fine("PaySQL=" + sql.toString());
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_C_Currency_ID);
			pstmt.setTimestamp(2, (Timestamp)date);
			pstmt.setInt(3, m_C_Currency_ID);
			pstmt.setTimestamp(4, (Timestamp)date);
			pstmt.setInt(5, m_C_BPartner_ID);
			if (!isMultiCurrency)
				pstmt.setInt(6, m_C_Currency_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(1));       //  1-TrxDate
				KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(2));
				line.add(pp);                       //  2-DocumentNo
				if (isMultiCurrency)
				{
					line.add(rs.getString(4));      //  3-Currency
					line.add(rs.getBigDecimal(5));  //  4-PayAmt
				}
				line.add(rs.getBigDecimal(6));      //  3/5-ConvAmt
				BigDecimal available = rs.getBigDecimal(7);
				if (available == null || available.signum() == 0)	//	nothing available
					continue;
				line.add(available);				//  4/6-ConvOpen/Available
				line.add(Env.ZERO);					//  5/7-Payment
				
				line.add(rs.getString(9));                  // 7/9DocumentType faaguilar OFB
				
//				line.add(rs.getBigDecimal(8));		//  6/8-Multiplier
				//
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		
		return data;
	}
	
	public Vector<String> getPaymentColumnNames(boolean isMultiCurrency)
	{	
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Util.cleanAmp(Msg.translate(Env.getCtx(), "DocumentNo")));
		if (isMultiCurrency)
		{
			columnNames.add(Msg.getMsg(Env.getCtx(), "TrxCurrency"));
			columnNames.add(Msg.translate(Env.getCtx(), "Amount"));
		}
		columnNames.add(Msg.getMsg(Env.getCtx(), "ConvertedAmount"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "OpenAmt"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "AppliedAmt"));
		columnNames.add("Tipo");//faaguilar OFB
		
		
		return columnNames;
	}
	
	public void setPaymentColumnClass(IMiniTable paymentTable, boolean isMultiCurrency)
	{
		int i = 0;
		paymentTable.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		paymentTable.setColumnClass(i++, Timestamp.class, true);        //  1-TrxDate
		paymentTable.setColumnClass(i++, String.class, true);           //  2-Value
		if (isMultiCurrency)
		{
			paymentTable.setColumnClass(i++, String.class, true);       //  3-Currency
			paymentTable.setColumnClass(i++, BigDecimal.class, true);   //  4-PayAmt
		}
		paymentTable.setColumnClass(i++, BigDecimal.class, true);       //  5-ConvAmt
		paymentTable.setColumnClass(i++, BigDecimal.class, true);       //  6-ConvOpen
		paymentTable.setColumnClass(i++, BigDecimal.class, false);      //  7-Allocated
		paymentTable.setColumnClass(i++, String.class, false);      //  11-DocumentType faaguilar OFB
		
//		paymentTable.setColumnClass(i++, BigDecimal.class, true);      	//  8-Multiplier

		//
		i_payment = isMultiCurrency ? 7 : 5;
		

		//  Table UI
		paymentTable.autoSize();
	}
	
	public Vector<Vector<Object>> getInvoiceData(boolean isMultiCurrency, Object date, IMiniTable invoiceTable)
	{
		/********************************
		 *  Load unpaid Invoices
		 *      1-TrxDate, 2-Value, (3-Currency, 4-InvAmt,)
		 *      5-ConvAmt, 6-ConvOpen, 7-ConvDisc, 8-WriteOff, 9-Applied
		 * 
		 SELECT i.DateInvoiced,i.DocumentNo,i.C_Invoice_ID,c.ISO_Code,
		 i.GrandTotal*i.MultiplierAP "GrandTotal", 
		 currencyConvert(i.GrandTotal*i.MultiplierAP,i.C_Currency_ID,i.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID) "GrandTotal $", 
		 invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID) "Open",
		 currencyConvert(invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID),i.C_Currency_ID,i.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP "Open $", 
		 invoiceDiscount(i.C_Invoice_ID,SysDate,C_InvoicePaySchedule_ID) "Discount",
		 currencyConvert(invoiceDiscount(i.C_Invoice_ID,SysDate,C_InvoicePaySchedule_ID),i.C_Currency_ID,i.C_Currency_ID,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.Multiplier*i.MultiplierAP "Discount $",
		 i.MultiplierAP, i.Multiplier 
		 FROM C_Invoice_v i INNER JOIN C_Currency c ON (i.C_Currency_ID=c.C_Currency_ID) 
		 WHERE -- i.IsPaid='N' AND i.Processed='Y' AND i.C_BPartner_ID=1000001
		 */
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		String dateStr = "DueDate";		
		dateStr = OFBForward.getDateVAllocation();
		if(dateStr == null || dateStr.trim().length() ==0)
			dateStr = "DueDate";
		//ininoles se cambia DateAcct por DueDate
		StringBuffer sql = new StringBuffer("SELECT i."+"DueDate"+",i.DocumentNo,i.C_Invoice_ID," //  1..3
			+ "c.ISO_Code,i.GrandTotal*i.MultiplierAP, "                            //  4..5    Orig Currency
			+ "currencyConvert(i.GrandTotal*i.MultiplierAP,i.C_Currency_ID,?,?,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID), " //  6   #1  Converted, #2 Date
			+ "currencyConvert(invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID),i.C_Currency_ID,?,?,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP, "  //  7   #3, #4  Converted Open
			+ "currencyConvert(invoiceDiscount"                               //  8       AllowedDiscount
			+ "(i.C_Invoice_ID,?,C_InvoicePaySchedule_ID),i.C_Currency_ID,?,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.Multiplier*i.MultiplierAP,"               //  #5, #6
			+ "i.MultiplierAP, doc.name, " //faaguilar DocumentType 10
			+ "i.c_invoicepayschedule_id " //ininoles IDcuota 11
			+ "FROM C_Invoice_v i"		//  corrected for CM/Split
			+ " INNER JOIN C_Currency c ON (i.C_Currency_ID=c.C_Currency_ID) "
			+ " INNER JOIN C_DocType doc ON (i.C_Doctype_ID=doc.C_DocType_ID) " //faaguilar DocumentType	
			+ "WHERE i.IsPaid='N' AND i.Processed='Y'"
			+ " AND i.DocStatus <> 'VO' " //ininoles no mostrar facturas nulas
			+ " AND i.C_BPartner_ID=?");                                            //  #7
		//ININOLES Se sobreescribe sql para usar misma fecha que factura
		if(OFBForward.AllocationUseDateInvoice())
		{
			sql = new StringBuffer("SELECT i.DueDate,i.DocumentNo,i.C_Invoice_ID," //  1..3
					+ "c.ISO_Code,i.GrandTotal*i.MultiplierAP, "                            //  4..5    Orig Currency
					+ " CASE WHEN i.multiplyrate > 1 " 
					+ " 	THEN 1/i.multiplyrate*(i.GrandTotal*i.MultiplierAP) "
					+ " ELSE  "
					+ " 	currencyConvert(i.GrandTotal*i.MultiplierAP,i.C_Currency_ID,?,?,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)" 
					+ " END ," //  6   #1  Converted, #2 Date
					+ " CASE WHEN i.multiplyrate > 1 " 
					+ " 	THEN 1/i.multiplyrate*(invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID))*i.MultiplierAP "
					+ " ELSE  "
					+ " 	currencyConvert(invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID),i.C_Currency_ID,?,?,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP "  //  7   #3, #4  Converted Open
					+ " END,"
					+ "currencyConvert(invoiceDiscount"                               //  8       AllowedDiscount
					+ "(i.C_Invoice_ID,?,C_InvoicePaySchedule_ID),i.C_Currency_ID,?,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.Multiplier*i.MultiplierAP,"               //  #5, #6
					+ "i.MultiplierAP, doc.name, " //faaguilar DocumentType 10
					+ "i.c_invoicepayschedule_id " //ininoles IDcuota 11
					+ "FROM C_Invoice_v i"		//  corrected for CM/Split
					+ " INNER JOIN C_Currency c ON (i.C_Currency_ID=c.C_Currency_ID) "
					+ " INNER JOIN C_DocType doc ON (i.C_Doctype_ID=doc.C_DocType_ID) " //faaguilar DocumentType	
					+ "WHERE i.IsPaid='N' AND i.Processed='Y'"
					+ " AND i.DocStatus <> 'VO' " //ininoles no mostrar facturas nulas
					+ " AND i.C_BPartner_ID=?");  
		}		
		if (!isMultiCurrency)
			sql.append(" AND i.C_Currency_ID=?");                                   //  #8
		if (m_AD_Org_ID != 0 ) 
			sql.append(" AND i.AD_Org_ID=" + m_AD_Org_ID);
		sql.append(" AND i.Extinta='N' AND i.ISFACTORING='N' ");//faaguilar OFB
		sql.append(" ORDER BY i.DateInvoiced, i.DocumentNo");
		log.fine("InvSQL=" + sql.toString());
		
		// role security
		sql = new StringBuffer( MRole.getDefault(Env.getCtx(), false).addAccessSQL( sql.toString(), "i", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO ) );
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_C_Currency_ID);
			pstmt.setTimestamp(2, (Timestamp)date);
			pstmt.setInt(3, m_C_Currency_ID);
			pstmt.setTimestamp(4, (Timestamp)date);
			pstmt.setTimestamp(5, (Timestamp)date);
			pstmt.setInt(6, m_C_Currency_ID);
			pstmt.setInt(7, m_C_BPartner_ID);
			if (!isMultiCurrency)
				pstmt.setInt(8, m_C_Currency_ID);			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(1));       //  1-TrxDate
				KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(2));
				line.add(pp);                       //  2-Value
				if (isMultiCurrency)
				{
					line.add(rs.getString(4));      //  3-Currency
					line.add(rs.getBigDecimal(5));  //  4-Orig Amount
				}
				line.add(rs.getBigDecimal(6));      //  3/5-ConvAmt
				BigDecimal open = rs.getBigDecimal(7);
				if (open == null)		//	no conversion rate
					open = Env.ZERO;
				line.add(open);      				//  4/6-ConvOpen
				BigDecimal discount = rs.getBigDecimal(8);
				if (discount == null)	//	no concersion rate
					discount = Env.ZERO;
				line.add(discount);					//  5/7-ConvAllowedDisc
				line.add(Env.ZERO);      			//  6/8-WriteOff
				line.add(Env.ZERO);					// 7/9-Applied
				line.add(open);				    //  8/10-OverUnder
				line.add(rs.getString(10)); 		//DocumentType faaguilar OFB
				line.add(rs.getInt(11));			//11 id de cuota

//				line.add(rs.getBigDecimal(9));		//	8/10-Multiplier
				//	Add when open <> 0 (i.e. not if no conversion rate)
				if (Env.ZERO.compareTo(open) != 0)
					data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		
		return data;
	}

	public Vector<String> getInvoiceColumnNames(boolean isMultiCurrency)
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Util.cleanAmp(Msg.translate(Env.getCtx(), "DocumentNo")));
		if (isMultiCurrency)
		{
			columnNames.add(Msg.getMsg(Env.getCtx(), "TrxCurrency"));
			columnNames.add(Msg.translate(Env.getCtx(), "Amount"));
		}
		columnNames.add(Msg.getMsg(Env.getCtx(), "ConvertedAmount"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "OpenAmt"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "Discount"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "WriteOff"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "AppliedAmt"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "OverUnderAmt"));
		columnNames.add("Tipo"); //faaguilar OFB
		columnNames.add("ID Cuota"); //faaguilar OFB
//		columnNames.add(" ");	//	Multiplier
		
		return columnNames;
	}
	
	public void setInvoiceColumnClass(IMiniTable invoiceTable, boolean isMultiCurrency)
	{
		int i = 0;
		invoiceTable.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		invoiceTable.setColumnClass(i++, Timestamp.class, true);        //  1-TrxDate
		invoiceTable.setColumnClass(i++, String.class, true);           //  2-Value
		if (isMultiCurrency)
		{
			invoiceTable.setColumnClass(i++, String.class, true);       //  3-Currency
			invoiceTable.setColumnClass(i++, BigDecimal.class, true);   //  4-Amt
		}
		invoiceTable.setColumnClass(i++, BigDecimal.class, true);       //  5-ConvAmt
		invoiceTable.setColumnClass(i++, BigDecimal.class, true);       //  6-ConvAmt Open
		invoiceTable.setColumnClass(i++, BigDecimal.class, false);      //  7-Conv Discount
		invoiceTable.setColumnClass(i++, BigDecimal.class, false);      //  8-Conv WriteOff
		invoiceTable.setColumnClass(i++, BigDecimal.class, false);      //  9-Conv OverUnder
		invoiceTable.setColumnClass(i++, BigDecimal.class, true);		//	10-Conv Applied
		invoiceTable.setColumnClass(i++, String.class, false);      //  11-DocumentType faaguilar OFB
		invoiceTable.setColumnClass(i++, int.class, true);			//11 ID cuota
//		invoiceTable.setColumnClass(i++, BigDecimal.class, true);      	//  10-Multiplier
		//  Table UI
		invoiceTable.autoSize();
	}
	
	public void calculate(boolean isMultiCurrency)
	{
		i_open = isMultiCurrency ? 6 : 4;
		i_discount = isMultiCurrency ? 7 : 5;
		i_writeOff = isMultiCurrency ? 8 : 6;
		i_applied = isMultiCurrency ? 9 : 7;
		i_overUnder = isMultiCurrency ? 10 : 8;
//		i_multiplier = isMultiCurrency ? 10 : 8;
		i_cuota = isMultiCurrency ? 12 : 10; //ininoles posicion para traer cuota		
	}   //  loadBPartner
	
	public String writeOff(int row, int col, boolean isInvoice, IMiniTable payment, IMiniTable invoice, boolean isAutoWriteOff)
	{
		String msg = "";
		/**
		 *  Setting defaults
		 */
		if (m_calculating)  //  Avoid recursive calls
			return msg;
		m_calculating = true;
		
		log.config("Row=" + row 
			+ ", Col=" + col + ", InvoiceTable=" + isInvoice);
        
		//  Payments
		if (!isInvoice)
		{
			BigDecimal open = (BigDecimal)payment.getValueAt(row, i_open);
			BigDecimal applied = (BigDecimal)payment.getValueAt(row, i_payment);
			
			if (col == 0)
			{
				// selection of payment row
				if (((Boolean)payment.getValueAt(row, 0)).booleanValue())
				{
					applied = open;   //  Open Amount
					if (totalDiff.abs().compareTo(applied.abs()) < 0			// where less is available to allocate than open
							&& totalDiff.signum() == -applied.signum() )    	// and the available amount has the opposite sign
						applied = totalDiff.negate();						// reduce the amount applied to what's available
					
				}
				else    //  de-selected
					applied = Env.ZERO;
			}
			
			
			if (col == i_payment)
			{
				if ( applied.signum() == -open.signum() )
					applied = applied.negate();
				if ( open.abs().compareTo( applied.abs() ) < 0 )
							applied = open;
			}
			
			payment.setValueAt(applied, row, i_payment);
		}

		//  Invoice
		else 
		{
			boolean selected = ((Boolean) invoice.getValueAt(row, 0)).booleanValue();
			BigDecimal open = (BigDecimal)invoice.getValueAt(row, i_open);
			BigDecimal discount = (BigDecimal)invoice.getValueAt(row, i_discount);
			BigDecimal applied = (BigDecimal)invoice.getValueAt(row, i_applied);
			BigDecimal writeOff = (BigDecimal) invoice.getValueAt(row, i_writeOff);
			BigDecimal overUnder = (BigDecimal) invoice.getValueAt(row, i_overUnder);
			int openSign = open.signum();
			
			if (col == 0)  //selection
			{
				//  selected - set applied amount
				if ( selected )
				{
					applied = open;    //  Open Amount
					applied = applied.subtract(discount);
					writeOff = Env.ZERO;  //  to be sure
					overUnder = Env.ZERO;

					if (totalDiff.abs().compareTo(applied.abs()) < 0			// where less is available to allocate than open
							&& totalDiff.signum() == applied.signum() )     	// and the available amount has the same sign
						applied = totalDiff;									// reduce the amount applied to what's available

					if ( isAutoWriteOff )
						writeOff = open.subtract(applied.add(discount));
					else
						overUnder = open.subtract(applied.add(discount));
				}
				else    //  de-selected
				{
					writeOff = Env.ZERO;
					applied = Env.ZERO;
					overUnder = Env.ZERO;
				}
			}
			
			// check entered values are sensible and possibly auto write-off
			if ( selected && col != 0 )
			{
				
				// values should have same sign as open except possibly over/under
				if ( discount.signum() == -openSign )
					discount = discount.negate();
				if ( writeOff.signum() == -openSign)
					writeOff = writeOff.negate();
				if ( applied.signum() == -openSign )
					applied = applied.negate();
				
				// discount and write-off must be less than open amount
				if ( discount.abs().compareTo(open.abs()) > 0)
					discount = open;
				if ( writeOff.abs().compareTo(open.abs()) > 0)
					writeOff = open;
				
				
				/*
				 * Two rules to maintain:
				 *
				 * 1) |writeOff + discount| < |open| 
				 * 2) discount + writeOff + overUnder + applied = 0
				 *
				 *   As only one column is edited at a time and the initial position was one of compliance
				 *   with the rules, we only need to redistribute the increase/decrease in the edited column to 
				 *   the others.
				*/
				BigDecimal newTotal = discount.add(writeOff).add(applied).add(overUnder);  // all have same sign
				BigDecimal difference = newTotal.subtract(open);
				
				// rule 2
				BigDecimal diffWOD = writeOff.add(discount).subtract(open);
										
				if ( diffWOD.signum() == open.signum() )  // writeOff and discount are too large
				{
					if ( col == i_discount )       // then edit writeoff
					{
						writeOff = writeOff.subtract(diffWOD);
					} 
					else                            // col = i_writeoff
					{
						discount = discount.subtract(diffWOD);
					}
					
					difference = difference.subtract(diffWOD);
				}
				
				// rule 1
				if ( col == i_applied )
					overUnder = overUnder.subtract(difference);
				else
					applied = applied.subtract(difference);
				
			}
			
			//	Warning if write Off > 30%
			if (isAutoWriteOff && writeOff.doubleValue()/open.doubleValue() > .30)
				msg = "AllocationWriteOffWarn";

			invoice.setValueAt(discount, row, i_discount);
			invoice.setValueAt(applied, row, i_applied);
			invoice.setValueAt(writeOff, row, i_writeOff);
			invoice.setValueAt(overUnder, row, i_overUnder);
			
			invoice.repaint(); //  update r/o
		}

		m_calculating = false;
		
		return msg;
	}
	
	/**
	 *  Calculate Allocation info
	 */
	public String calculatePayment(IMiniTable payment, boolean isMultiCurrency)
	{
		log.config("");

		//  Payment
		totalPay = new BigDecimal(0.0);
		int rows = payment.getRowCount();
		m_noPayments = 0;
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)payment.getValueAt(i, 0)).booleanValue())
			{
				Timestamp ts = (Timestamp)payment.getValueAt(i, 1);
				if ( !isMultiCurrency )  // the converted amounts are only valid for the selected date
				{
					allocDate = TimeUtil.max(allocDate, ts);
					//se sobreescribe fecha con fecha de vencimiento del pago
					if(OFBForward.AllocationUseActualDate())
					{				
						KeyNamePair pp = (KeyNamePair)payment.getValueAt(i, 2);   //  Value
						int C_Payment_ID = pp.getKey();
						MPayment payN = new MPayment(Env.getCtx(), C_Payment_ID, null);
						allocDate = payN.getDateAcct();
					}
				}
				BigDecimal bd = (BigDecimal)payment.getValueAt(i, i_payment);
				totalPay = totalPay.add(bd);  //  Applied Pay
				m_noPayments++;
				log.fine("Payment_" + i + " = " + bd + " - Total=" + totalPay);
			}
		}
		return String.valueOf(m_noPayments) + " - "
			+ Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(totalPay) + " ";
	}
	
	public String calculateInvoice(IMiniTable invoice, boolean isMultiCurrency)
	{		
		//  Invoices
		totalInv = new BigDecimal(0.0);
		int rows = invoice.getRowCount();
		m_noInvoices = 0;

		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)invoice.getValueAt(i, 0)).booleanValue())
			{
				Timestamp ts = (Timestamp)invoice.getValueAt(i, 1);
				if ( !isMultiCurrency )  // converted amounts only valid for selected date
					allocDate = TimeUtil.max(allocDate, ts);
				if(OFBForward.AllocationUseActualDate())
				{				
					KeyNamePair pp = (KeyNamePair)invoice.getValueAt(i, 2);   //  Value
					int Invoice_ID = pp.getKey();
					MInvoice invN = new MInvoice(Env.getCtx(), Invoice_ID, null);
					allocDate = invN.getDateAcct();
				}
				BigDecimal bd = (BigDecimal)invoice.getValueAt(i, i_applied);
				totalInv = totalInv.add(bd);  //  Applied Inv
				m_noInvoices++;
				log.fine("Invoice_" + i + " = " + bd + " - Total=" + totalPay);
			}
		}
		return String.valueOf(m_noInvoices) + " - "
			+ Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(totalInv) + " ";
	}
	
	/**************************************************************************
	 *  Save Data
	 */
	public String saveData(int m_WindowNo, Object date, IMiniTable payment, IMiniTable invoice, String trxName)
	{
		if (m_noInvoices + m_noPayments == 0)
			return "";

		//  fixed fields
		int AD_Client_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Client_ID");
		int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Org_ID");
		AD_Org_ID=m_AD_Org_ID; //faaguilar OFB Fix
		int C_BPartner_ID = m_C_BPartner_ID;
		int C_Order_ID = 0;
		int C_CashLine_ID = 0;
		Timestamp DateTrx = (Timestamp)date;
		int C_Currency_ID = m_C_Currency_ID;	//	the allocation currency
		//
		if (AD_Org_ID == 0)
		{
			//ADialog.error(m_WindowNo, this, "Org0NotAllowed", null);
			//new AdempiereException("@Org0NotAllowed@"); faaguilar OFB commented
			AD_Org_ID = m_AD_OrgDoc_ID; //faaguilar OFB
		}
		
		//faaguilar OFB period open? Begin
		if(!MPeriod.isOpen(Env.getCtx(), DateTrx, Doc.DOCTYPE_Allocation))
		{
			new AdempiereException("Period Closed");
		}
		/*if(OFBForward.AllocationUseActualDate())
		{
			Timestamp Today = new Timestamp(TimeUtil.getToday().getTimeInMillis());
			DateTrx = Today;
		}*/
		//faaguilar OFB period open? End
		
		//
		log.config("Client=" + AD_Client_ID + ", Org=" + AD_Org_ID
			+ ", BPartner=" + C_BPartner_ID + ", Date=" + DateTrx);

		//  Payment - Loop and add them to paymentList/amountList
		int pRows = payment.getRowCount();
		ArrayList<Integer> paymentList = new ArrayList<Integer>(pRows);
		ArrayList<BigDecimal> amountList = new ArrayList<BigDecimal>(pRows);
		BigDecimal paymentAppliedAmt = Env.ZERO;
		for (int i = 0; i < pRows; i++)
		{
			//  Payment line is selected
			if (((Boolean)payment.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)payment.getValueAt(i, 2);   //  Value
				//  Payment variables
				int C_Payment_ID = pp.getKey();
				paymentList.add(new Integer(C_Payment_ID));
				//ininoles se reemplaza fecha por fecha de pago
				if(OFBForward.AllocationUsePayDate())
				{
					MPayment pay = new MPayment(Env.getCtx(), C_Payment_ID, null);
					DateTrx = pay.getDateAcct();
				}
				BigDecimal PaymentAmt = (BigDecimal)payment.getValueAt(i, i_payment);  //  Applied Payment
				amountList.add(PaymentAmt);
				//
				paymentAppliedAmt = paymentAppliedAmt.add(PaymentAmt);
				//
				log.fine("C_Payment_ID=" + C_Payment_ID 
					+ " - PaymentAmt=" + PaymentAmt); // + " * " + Multiplier + " = " + PaymentAmtAbs);
			}
		}
		log.config("Number of Payments=" + paymentList.size() + " - Total=" + paymentAppliedAmt);

		//  Invoices - Loop and generate allocations
		int iRows = invoice.getRowCount();
		
		//	Create Allocation
		MAllocationHdr alloc = new MAllocationHdr (Env.getCtx(), true,	//	manual
			DateTrx, C_Currency_ID, Env.getContext(Env.getCtx(), "#AD_User_Name"), trxName);
		alloc.setAD_Org_ID(AD_Org_ID);
		alloc.saveEx();
		//	For all invoices
		int invoiceLines = 0;
		BigDecimal unmatchedApplied = Env.ZERO;
		for (int i = 0; i < iRows; i++)
		{
			//  Invoice line is selected
			if (((Boolean)invoice.getValueAt(i, 0)).booleanValue())
			{
				invoiceLines++;
				KeyNamePair pp = (KeyNamePair)invoice.getValueAt(i, 2);    //  Value
				//  Invoice variables
				int C_Invoice_ID = pp.getKey();
				BigDecimal AppliedAmt = (BigDecimal)invoice.getValueAt(i, i_applied);
				//  semi-fixed fields (reset after first invoice)
				BigDecimal DiscountAmt = (BigDecimal)invoice.getValueAt(i, i_discount);
				BigDecimal WriteOffAmt = (BigDecimal)invoice.getValueAt(i, i_writeOff);
				//	OverUnderAmt needs to be in Allocation Currency
				BigDecimal OverUnderAmt = ((BigDecimal)invoice.getValueAt(i, i_open))
					.subtract(AppliedAmt).subtract(DiscountAmt).subtract(WriteOffAmt);
				//ininoles seteamos id de cuota
				int ID_InvoiceSchedule = 0;
				if ((Integer)invoice.getValueAt(i, i_cuota) != null)
				{
					ID_InvoiceSchedule =(Integer)invoice.getValueAt(i, i_cuota); 
				}
				
				log.config("Invoice #" + i + " - AppliedAmt=" + AppliedAmt);// + " -> " + AppliedAbs);
				//  loop through all payments until invoice applied
				
				for (int j = 0; j < paymentList.size() && AppliedAmt.signum() != 0; j++)
				{
					int C_Payment_ID = ((Integer)paymentList.get(j)).intValue();
					BigDecimal PaymentAmt = (BigDecimal)amountList.get(j);
					if (PaymentAmt.signum() == AppliedAmt.signum())	// only match same sign (otherwise appliedAmt increases)
					{												// and not zero (appliedAmt was checked earlier)
						log.config(".. with payment #" + j + ", Amt=" + PaymentAmt);
						
						BigDecimal amount = AppliedAmt;
						if (amount.abs().compareTo(PaymentAmt.abs()) > 0)  // if there's more open on the invoice
							amount = PaymentAmt;							// than left in the payment
						
						//	Allocation Line
						/*faaguilar OFB original code commented begin
						MAllocationLine aLine = new MAllocationLine (alloc, amount, 
							DiscountAmt, WriteOffAmt, OverUnderAmt);
						  faaguilar OFB original code commented end*/
						//faaguilar OFB begin
						MAllocationLine aLine= null;
						if(indicator != INDICATOR_WRITEOFF_INVOICE){ //faaguilar OFB writeeoff payment
							aLine= new MAllocationLine (alloc, PaymentAmt, 
							Env.ZERO, Env.ZERO, Env.ZERO);}
						else{
							aLine = new MAllocationLine (alloc, Env.ZERO, 
									Env.ZERO, PaymentAmt, Env.ZERO);}
						//faaguilar OFB end
						aLine.setDocInfo(C_BPartner_ID, C_Order_ID, C_Invoice_ID);
						aLine.setPaymentInfo(C_Payment_ID, C_CashLine_ID);
						aLine.saveEx();
						//ininoles update de cuota
						String sqlInvoiceSchedule = "";
						try
						{
							if (ID_InvoiceSchedule > 0)
							{
								sqlInvoiceSchedule = "UPDATE c_allocationline " +
										"SET c_invoicepayschedule_id = " + ID_InvoiceSchedule +
										" WHERE C_AllocationLine_ID = " +aLine.get_ID();
								int no = DB.executeUpdate(sqlInvoiceSchedule, trxName);
									log.config("InvoiceSchedule #" + i + " is paid - updated=" + no);
							}
						}catch(Exception e)
						{
							log.log(Level.SEVERE, sqlInvoiceSchedule.toString(), e);
						}
								
						//  Apply Discounts and WriteOff only first time
						DiscountAmt = Env.ZERO;
						WriteOffAmt = Env.ZERO;
						//  subtract amount from Payment/Invoice
						AppliedAmt = AppliedAmt.subtract(amount);
						PaymentAmt = PaymentAmt.subtract(amount);
						log.fine("Allocation Amount=" + amount + " - Remaining  Applied=" + AppliedAmt + ", Payment=" + PaymentAmt);
						amountList.set(j, PaymentAmt);  //  update
					}	//	for all applied amounts
				}	//	loop through payments for invoice
				
				if ( AppliedAmt.signum() == 0 && DiscountAmt.signum() == 0 && WriteOffAmt.signum() == 0)
					continue;
				else {			// remainder will need to match against other invoices
					int C_Payment_ID = 0;
					
					//	Allocation Line
					MAllocationLine aLine = new MAllocationLine (alloc, AppliedAmt, 
						DiscountAmt, WriteOffAmt, OverUnderAmt);
					aLine.setDocInfo(C_BPartner_ID, C_Order_ID, C_Invoice_ID);
					aLine.setPaymentInfo(C_Payment_ID, C_CashLine_ID);
					aLine.saveEx();
					log.fine("Allocation Amount=" + AppliedAmt);
					unmatchedApplied = unmatchedApplied.add(AppliedAmt);
				}
			}   //  invoice selected
		}   //  invoice loop

		// check for unapplied payment amounts (eg from payment reversals)
		for (int i = 0; i < paymentList.size(); i++)	{
			BigDecimal payAmt = (BigDecimal) amountList.get(i);
			if ( payAmt.signum() == 0 )
					continue;
			int C_Payment_ID = ((Integer)paymentList.get(i)).intValue();
			log.fine("Payment=" + C_Payment_ID  
					+ ", Amount=" + payAmt);

			//	Allocation Line
			MAllocationLine aLine= null;
			if(indicator != INDICATOR_WRITEOFF_INVOICE) //faaguilar OFB writeeoff payment
				aLine = new MAllocationLine (alloc, payAmt, 
					Env.ZERO, Env.ZERO, Env.ZERO);
			else
				aLine = new MAllocationLine (alloc, Env.ZERO, 
						Env.ZERO, payAmt, Env.ZERO);
			
			aLine.setDocInfo(C_BPartner_ID, 0, 0);
			aLine.setPaymentInfo(C_Payment_ID, 0);
			aLine.saveEx();
			
			if(indicator != INDICATOR_WRITEOFF_INVOICE) //ininoles validacion solo la hara si no es WRITEOFF
				unmatchedApplied = unmatchedApplied.subtract(payAmt);
		}		
		
		if ( unmatchedApplied.signum() != 0 )
			log.log(Level.SEVERE, "Allocation not balanced -- out by " + unmatchedApplied );

		//	Should start WF
		if (alloc.get_ID() != 0)
		{
			alloc.processIt(DocAction.ACTION_Complete);
			alloc.saveEx();
		}
		
		//  Test/Set IsPaid for Invoice - requires that allocation is posted
		for (int i = 0; i < iRows; i++)
		{
			//  Invoice line is selected
			if (((Boolean)invoice.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)invoice.getValueAt(i, 2);    //  Value
				//  Invoice variables
				int C_Invoice_ID = pp.getKey();
				String sql = "SELECT invoiceOpen(C_Invoice_ID, 0) "
					+ "FROM C_Invoice WHERE C_Invoice_ID=?";
				BigDecimal open = DB.getSQLValueBD(trxName, sql, C_Invoice_ID);
				if (open != null && open.signum() == 0)	{
					sql = "UPDATE C_Invoice SET IsPaid='Y' "
						+ "WHERE C_Invoice_ID=" + C_Invoice_ID;
					int no = DB.executeUpdate(sql, trxName);
					log.config("Invoice #" + i + " is paid - updated=" + no);
				} else
					log.config("Invoice #" + i + " is not paid - " + open);
			}
		}
		//  Test/Set Payment is fully allocated
		for (int i = 0; i < paymentList.size(); i++)
		{
			int C_Payment_ID = ((Integer)paymentList.get(i)).intValue();
			MPayment pay = new MPayment (Env.getCtx(), C_Payment_ID, trxName);
			if (pay.testAllocation())
				pay.saveEx();
			log.config("Payment #" + i + (pay.isAllocated() ? " not" : " is") 
					+ " fully allocated");
		}
		paymentList.clear();
		amountList.clear();
		
		return alloc.getDocumentNo();
	}   //  saveData
	
	//faaguilar OFB multiallocation method
	protected int revision_multiallocation(IMiniTable payment, IMiniTable invoice, boolean autoWriteOff)
	{
		multiprocess=true;
		log.info("multiallocation custom method");
        BigDecimal difference = new BigDecimal("0.0");
		//  Payment
		
		BigDecimal totalPay = new BigDecimal(0.0);
		BigDecimal payamt1 = new BigDecimal(0.0);
		BigDecimal payamt2 = new BigDecimal(0.0);
		int rows = payment.getRowCount();
		int lastPayment_ID=0;
		int lastInvoice_ID=0;
		int lastInvoice2_ID=0;
		m_noPayments = 0;
		int payrow=-1;
		int payrow2=-1;
		int RetValue=INDICATOR_NONE;
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)payment.getValueAt(i, 0)).booleanValue())
			{
				BigDecimal bd = (BigDecimal)payment.getValueAt(i, i_open);
				totalPay = totalPay.add(bd);  //  Applied Pay
				if(payamt1.intValue()==0)
					payamt1=(BigDecimal)payment.getValueAt(i, i_open);
				else
					payamt2=(BigDecimal)payment.getValueAt(i, i_open);
				m_noPayments++;
				if(payrow<0)
					payrow=i;
				else
					payrow2=i;
				
				KeyNamePair pp = (KeyNamePair)payment.getValueAt(i, 2);   //  Value
				lastPayment_ID = pp.getKey();
				
					
				log.fine("Payment_" + i + " = " + bd + " - Total=" + totalPay);
			}
		}

		//  Invoices
		
		BigDecimal totalInv = new BigDecimal(0.0);
		BigDecimal invamt1 = new BigDecimal(0.0);
		BigDecimal invamt2 = new BigDecimal(0.0);
		rows = invoice.getRowCount();
		m_noInvoices = 0;
		int invrow=-1;
		int invrow2=-1;
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)invoice.getValueAt(i, 0)).booleanValue())
			{
				BigDecimal bd = (BigDecimal)invoice.getValueAt(i, i_open);
				totalInv = totalInv.add(bd);  //  Applied Inv
				if(invamt1.intValue()==0)
					invamt1=(BigDecimal)invoice.getValueAt(i, i_open);
				else
					invamt2=(BigDecimal)invoice.getValueAt(i, i_open);
					
				m_noInvoices++;
				if(invrow<0)
				invrow=i;
				else
				invrow2=i;
				
				KeyNamePair pp = (KeyNamePair)invoice.getValueAt(i, 2);    //  Value
				if(lastInvoice_ID==0)
					lastInvoice_ID = pp.getKey();
				else
					lastInvoice2_ID = pp.getKey();
				log.fine( "Invoice_" + i + " = " + bd + " - Total=" + totalPay);
			}
		}
		totalPayment=totalPay;
		totalInvoiced=totalInv;
		log.config("noInvoices:"+ m_noInvoices +" total: " + totalInvoiced+ " noPayments:"+ m_noPayments + "total:"+totalPay);
		if(m_noInvoices> 1 && m_noPayments>1)
			RetValue= INDICATOR_NONE;
		
				
		if(autoWriteOff)
			if(m_noInvoices> 0 && m_noPayments>0)
				RetValue= INDICATOR_NONE;
				
		if(m_noPayments==1 && m_noInvoices==1)
		{	
			if(totalInvoiced.intValue()>0 && totalPay.intValue()>0)//Sales trx
			{
				if(totalInvoiced.compareTo(totalPay)==0) //equals
				{
					payment.setValueAt(totalPay, payrow, i_payment);
					invoice.setValueAt(totalInvoiced, invrow, i_applied);
					RetValue= INDICATOR_TOTAL_PAY;
				}
				else if (totalInvoiced.compareTo(totalPay)>0) // sub payment
				{
					//log.info("payrow:"+payrow+",invrow:"+invrow+" ,i_applied:"+i_applied);
					payment.setValueAt(totalPay, payrow, i_payment);
					invoice.setValueAt(totalPay, invrow, i_applied);
					
					
					//invoice.setValueAt(totalInvoiced.subtract(totalPay.abs()), invrow, i_overUnder);
					
					
					RetValue= INDICATOR_SUBPAYMENT_SO;
				}
				else //<0 greater payment
				{
					payment.setValueAt(totalInvoiced, payrow, i_payment);
					invoice.setValueAt(totalInvoiced, invrow, i_applied);
					
					//payment.setValueAt(totalPay.subtract(totalInvoiced.abs()), payrow, i_overUnder);
					
					RetValue= INDICATOR_GREATER_PAYMENT_SO;
				}
			}
			if(totalInvoiced.intValue()<0 && totalPay.intValue()<0)//Purchase trx
			{
				if(totalInvoiced.abs().compareTo(totalPay.abs())==0) //equals
				{
					payment.setValueAt(totalPay, payrow, i_payment);
					invoice.setValueAt(totalInvoiced, invrow, i_applied);
					RetValue= INDICATOR_TOTAL_PAY;
				}
				else if (totalInvoiced.abs().compareTo(totalPay.abs())>0) // sub payment
				{
					payment.setValueAt(totalPay, payrow, i_payment);
					invoice.setValueAt(totalPay, invrow, i_applied);
					RetValue= INDICATOR_SUBPAYMENT_PO;
				}
				else //<0 greater payment
				{
					payment.setValueAt(totalInvoiced, payrow, i_payment);
					invoice.setValueAt(totalInvoiced, invrow, i_applied);
					RetValue= INDICATOR_GREATER_PAYMENT_PO;
				}
				
			}
		}// Chilean Implementation, if you need, only comment Begin
		else if (m_noPayments==2 && m_noInvoices==0) //payment vs payment
		{
			if((payamt1.intValue()>0 && payamt2.intValue()<0) || (payamt1.intValue()<0 && payamt2.intValue()>0))//receipt vs vendor
			{
				log.info("payrow:"+payrow+"payrow2:"+payrow2+",payamt1:"+payamt1+" ,payamt2:"+payamt2);
				if (payamt1.abs().compareTo(payamt2.abs())>0)//payment2 subpayment
				{
					if(payamt2.intValue()<0)
						payment.setValueAt(payamt2.abs(), payrow, i_payment);
					else
						payment.setValueAt(payamt2.negate(), payrow, i_payment);
					payment.setValueAt(payamt2, payrow2, i_payment);
					RetValue= INDICATOR_VERSUS_SUBPAYMENT;
				}
				else if (payamt2.abs().compareTo(payamt1.abs())>0)//payment1 subpayment
				{
					if(payamt1.intValue()<0)
						payment.setValueAt(payamt1.abs(), payrow2, i_payment);
					else
						payment.setValueAt(payamt1.negate(), payrow2, i_payment);
						
					payment.setValueAt(payamt1, payrow, i_payment);
					RetValue= INDICATOR_VERSUS_SUBPAYMENT;
				}
				else if (payamt2.abs().compareTo(payamt1.abs())==0)//payment1 equals payment2
				{
					payment.setValueAt(payamt1, payrow, i_payment);
					payment.setValueAt(payamt2, payrow2, i_payment);
					RetValue= INDICATOR_VERSUS_PAYMENT;
				}
			}
			
		}
		else if (m_noPayments==0 && m_noInvoices==2) //invoice vs invoice
		{
			//validacion de moneda
			MInvoice inv1 = new MInvoice(Env.getCtx(), lastInvoice_ID , null);
			MInvoice inv2 = new MInvoice(Env.getCtx(), lastInvoice2_ID , null);
			if(inv1.getC_Currency_ID()==inv2.getC_Currency_ID() && inv1.getC_Currency_ID()!=m_C_Currency_ID)
				return this.INDICATOR_CURRENCY_DIFF;
			
			//fin validacion moneda
			
			if((invamt1.intValue()>0 && invamt2.intValue()<0) || (invamt1.intValue()<0 && invamt2.intValue()>0))//sales vs credit
			{
				log.info("invrow:"+invrow+"invrow2:"+invrow2+",invamt1:"+invamt1+" ,invamt2:"+invamt2);
				if (invamt1.abs().compareTo(invamt2.abs())>0)//invamt2 sub
				{
					if(invamt2.intValue()<0)
						invoice.setValueAt(invamt2.abs(), invrow, i_applied);
					else
						invoice.setValueAt(invamt2.negate(), invrow, i_applied);	
					invoice.setValueAt(invamt2, invrow2, i_applied);
					
					/*if(invamt1.signum()>0)
						invoice.setValueAt(invamt1.subtract(invamt2.abs()), invrow, i_overUnder);
					else
						invoice.setValueAt(invamt1.add(invamt2.abs()), invrow, i_overUnder);*/
					
					RetValue= INDICATOR_VERSUS_SUBINVOICE;
				}
				else if (invamt2.abs().compareTo(invamt1.abs())>0)//invamt1 sub
				{
					if(invamt1.intValue()<0)
						invoice.setValueAt(invamt1.abs(), invrow2, i_applied);
					else
						invoice.setValueAt(invamt1.negate(), invrow2, i_applied);	
					invoice.setValueAt(invamt1, invrow, i_applied);
					
					/*if(invamt2.signum()>0)
						invoice.setValueAt(invamt2.subtract(invamt1.abs()), invrow, i_overUnder);
					else
						invoice.setValueAt(invamt2.add(invamt1.abs()), invrow, i_overUnder);*/
					
					RetValue= INDICATOR_VERSUS_SUBINVOICE;
				}
				else if (invamt2.abs().compareTo(invamt1.abs())==0)//invamt1 equals invamt2
				{
					invoice.setValueAt(invamt1, invrow, i_applied);	
					invoice.setValueAt(invamt2, invrow2, i_applied);
					RetValue= INDICATOR_VERSUS_INVOICE;
					
				}
			}
		}
		else if (m_noPayments==0 && m_noInvoices==1) //writeoff
		{
			if (autoWriteOff)
			{
				invoice.setValueAt(invamt1, invrow, i_writeOff);
				invoice.setValueAt(Env.ZERO, invrow, i_applied);	
				RetValue = INDICATOR_WRITEOFF_INVOICE;
			}
			// Chilean Implementation, if you need, only comment End
		}
		else if (m_noPayments==1 && m_noInvoices==0) //writeoff 1
		{
			if (autoWriteOff)
			{
				payment.setValueAt(payamt1, payrow, i_payment);
				indicator= INDICATOR_WRITEOFF_INVOICE;
				RetValue = INDICATOR_WRITEOFF_INVOICE;
			}
			// Chilean Implementation, if you need, only comment End
		}
		
		
		
		if(lastPayment_ID>0)
			m_AD_OrgDoc_ID=DB.getSQLValue(null, "select ad_org_id from c_payment where c_payment_id="+lastPayment_ID);
		else
			m_AD_OrgDoc_ID=DB.getSQLValue(null, "select ad_org_id from c_invoice where c_invoice_id="+lastInvoice_ID);
		
		multiprocess=false;
		return RetValue;
		
	}
}
