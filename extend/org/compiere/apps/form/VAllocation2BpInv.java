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
package org.compiere.apps.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.adempiere.plaf.AdempierePLAF;
import org.compiere.acct.Doc;
import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MColumn;
import org.compiere.model.MInvoice;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.DocAction;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;
import org.compiere.util.Util;

/**
 * Allocation Form
 *
 * @author  Fabian Aguilar
 * @version $Id: VAllocation.java,v 1.2 2006/07/30 00:51:28 faaguilar Exp $
 * 
 * Contributor : Fabian Aguilar - OFBConsulting - Multiallocation
 */
public class VAllocation2BpInv extends Allocation
	implements FormPanel, ActionListener, TableModelListener, VetoableChangeListener
{
	private CPanel panel = new CPanel();
	//faaguilar OFB Multiallocation
	private static final int INDICATOR_NONE = 0;
	private static final int INDICATOR_TOTAL_PAY = 1;
	private static final int INDICATOR_SUBPAYMENT_SO = 2;
	private static final int INDICATOR_GREATER_PAYMENT_SO = 3;
	private static final int INDICATOR_CREDIT_MEMO = 4;
	private static final int INDICATOR_GREATER_PAYMENT_PO = 5;
	private static final int INDICATOR_SUBPAYMENT_PO = 6;
	private static final int INDICATOR_VERSUS_INVOICE = 7;
	private static final int INDICATOR_VERSUS_SUBINVOICE = 8;
	private static final int INDICATOR_WRITEOFF_INVOICE=9;
	private static final int INDICATOR_VERSUS_PAYMENT=10;
	private static final int INDICATOR_VERSUS_SUBPAYMENT=11;
	
    BigDecimal totalPayment = new BigDecimal(0.0);
	BigDecimal totalInvoiced = new BigDecimal(0.0);
	BigDecimal totalCredit = new BigDecimal(0.0);
	BigDecimal pvspamt = new BigDecimal(0.0);
	int indicator = INDICATOR_NONE;
	boolean multiprocess=false;
	//faaguilar OFB Multiallocation END
	/**
	 * 
	 */
	private static final long serialVersionUID = -5322824600164192235L;

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		Env.setContext(Env.getCtx(), m_WindowNo, "IsSOTrx", "Y");   //  defaults to no
		m_C_Currency_ID = Env.getContextAsInt(Env.getCtx(), "$C_Currency_ID");   //  default
		//
		log.info("Currency=" + m_C_Currency_ID);
		try
		{
			dynInit();
			jbInit();
			calculate();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init

	/**	Window No			*/
	private int         m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 	m_frame;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VAllocation.class);

	private boolean     m_calculating = false;
	private int         m_C_Currency_ID = 0;
	private int         m_C_BPartner_ID = 0;
	private int         m_C_BPartner2_ID = 0; //faaguilar OFB multi BP
	private int         m_C_OrderPOImp_ID = 0;
	private int         m_noInvoices = 0;
	private int         m_noPayments = 0;
	private BigDecimal	totalInv = new BigDecimal(0.0);
	private BigDecimal 	totalPay = new BigDecimal(0.0);
	private BigDecimal	totalDiff = new BigDecimal(0.0);

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
	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private CPanel allocationPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private JLabel bpartnerLabel = new JLabel();
	private VLookup bpartnerSearch = null;
	//OFB faaguilar multi bpartner import
	private JLabel bpartnerLabel2 = new JLabel();
	private VLookup bpartnerSearch2 = null;
	private JLabel POImplabel = new JLabel();
	private VLookup POImpSearch = null;
	//END OFB
	private MiniTable invoiceTable = new MiniTable();
	private MiniTable paymentTable = new MiniTable();
	private JSplitPane infoPanel = new JSplitPane();
	private CPanel paymentPanel = new CPanel();
	private CPanel invoicePanel = new CPanel();
	private JLabel paymentLabel = new JLabel();
	private JLabel invoiceLabel = new JLabel();
	private BorderLayout paymentLayout = new BorderLayout();
	private BorderLayout invoiceLayout = new BorderLayout();
	private JLabel paymentInfo = new JLabel();
	private JLabel invoiceInfo = new JLabel();
	private JScrollPane paymentScrollPane = new JScrollPane();
	private JScrollPane invoiceScrollPane = new JScrollPane();
	private GridBagLayout allocationLayout = new GridBagLayout();
	private JLabel differenceLabel = new JLabel();
	private CTextField differenceField = new CTextField();
	private JButton allocateButton = new JButton();
	private JLabel currencyLabel = new JLabel();
	private VLookup currencyPick = null;
	private JCheckBox multiCurrency = new JCheckBox();
	private JLabel allocCurrencyLabel = new JLabel();
	private StatusBar statusBar = new StatusBar();
	private JLabel dateLabel = new JLabel();
	private VDate dateField = new VDate();
	private JCheckBox autoWriteOff = new JCheckBox();
	private int         m_AD_Org_ID = 0;
	private JLabel organizationLabel = new JLabel();
	private VLookup organizationPick = null;
	
	private ArrayList<Integer>	m_bpartnerCheck = new ArrayList<Integer>(); 

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(panel);
		//
		mainPanel.setLayout(mainLayout);
		dateLabel.setText(Msg.getMsg(Env.getCtx(), "Date"));
		dateLabel.setToolTipText(Msg.getMsg(Env.getCtx(), "AllocDate", false));
		autoWriteOff.setSelected(false);
		autoWriteOff.setText(Msg.getMsg(Env.getCtx(), "AutoWriteOff", true));
		autoWriteOff.setToolTipText(Msg.getMsg(Env.getCtx(), "AutoWriteOff", false));
		//
		parameterPanel.setLayout(parameterLayout);
		allocationPanel.setLayout(allocationLayout);
		bpartnerLabel.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		//faaguilar OFB multi BP import
		POImplabel.setText("Carpeta");
		bpartnerLabel2.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		//faaguilar END
		paymentLabel.setRequestFocusEnabled(false);
		paymentLabel.setText(" " + Msg.translate(Env.getCtx(), "C_Payment_ID"));
		invoiceLabel.setRequestFocusEnabled(false);
		invoiceLabel.setText(" " + Msg.translate(Env.getCtx(), "C_Invoice_ID"));
		paymentPanel.setLayout(paymentLayout);
		invoicePanel.setLayout(invoiceLayout);
		invoiceInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		invoiceInfo.setHorizontalTextPosition(SwingConstants.RIGHT);
		invoiceInfo.setText(".");
		paymentInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		paymentInfo.setHorizontalTextPosition(SwingConstants.RIGHT);
		paymentInfo.setText(".");
		differenceLabel.setText(Msg.getMsg(Env.getCtx(), "Difference"));
		differenceField.setBackground(AdempierePLAF.getFieldBackground_Inactive());
		differenceField.setEditable(false);
		differenceField.setText("0");
		differenceField.setColumns(8);
		differenceField.setHorizontalAlignment(SwingConstants.RIGHT);
		allocateButton.setText(Msg.getMsg(Env.getCtx(), "Process"));
		allocateButton.addActionListener(this);
		currencyLabel.setText(Msg.translate(Env.getCtx(), "C_Currency_ID"));
		multiCurrency.setText(Msg.getMsg(Env.getCtx(), "MultiCurrency"));
		multiCurrency.addActionListener(this);
		allocCurrencyLabel.setText(".");
		invoiceScrollPane.setPreferredSize(new Dimension(200, 200));
		paymentScrollPane.setPreferredSize(new Dimension(200, 200));
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		
		//org filter
		organizationLabel.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		parameterPanel.add(organizationLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));
		parameterPanel.add(organizationPick, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));
		
		parameterPanel.add(bpartnerLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bpartnerSearch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
			
		//OFB faaguilar bp
		parameterPanel.add(bpartnerLabel2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bpartnerSearch2, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		//OFB faaguilar end	
		
	   //faaguilar PO imp Search
	   parameterPanel.add(POImplabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(POImpSearch, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
	   //faaguilar END;
	   
		parameterPanel.add(dateLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(dateField, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(currencyLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(currencyPick, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(multiCurrency, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(autoWriteOff, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(allocationPanel, BorderLayout.SOUTH);
		allocationPanel.add(differenceLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
		allocationPanel.add(differenceField, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		allocationPanel.add(allocateButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		allocationPanel.add(allocCurrencyLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		paymentPanel.add(paymentLabel, BorderLayout.NORTH);
		paymentPanel.add(paymentInfo, BorderLayout.SOUTH);
		paymentPanel.add(paymentScrollPane, BorderLayout.CENTER);
		paymentScrollPane.getViewport().add(paymentTable, null);
		invoicePanel.add(invoiceLabel, BorderLayout.NORTH);
		invoicePanel.add(invoiceInfo, BorderLayout.SOUTH);
		invoicePanel.add(invoiceScrollPane, BorderLayout.CENTER);
		invoiceScrollPane.getViewport().add(invoiceTable, null);
		//
		mainPanel.add(infoPanel, BorderLayout.CENTER);
		infoPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel.setTopComponent(paymentPanel);
		infoPanel.setBottomComponent(invoicePanel);
		infoPanel.add(paymentPanel, JSplitPane.TOP);
		infoPanel.add(invoicePanel, JSplitPane.BOTTOM);
		infoPanel.setContinuousLayout(true);
		infoPanel.setPreferredSize(new Dimension(800,250));
		infoPanel.setDividerLocation(110);
	}   //  jbInit

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
	}	//	dispose

	/**
	 *  Dynamic Init (prepare dynamic fields)
	 *  @throws Exception if Lookups cannot be initialized
	 */
	public void dynInit() throws Exception
	{
		//  Currency
		int AD_Column_ID = 3505;    //  C_Invoice.C_Currency_ID
		MLookup lookupCur = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		currencyPick = new VLookup("C_Currency_ID", true, false, true, lookupCur);
		currencyPick.setValue(new Integer(m_C_Currency_ID));
		currencyPick.addVetoableChangeListener(this);

		// Organization filter selection
		AD_Column_ID = 1404; //M_Product.AD_Org_ID (allow org 0) faaguilar
		MLookup lookupOrg = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		organizationPick = new VLookup("AD_Org_ID", true, false, true, lookupOrg);
		organizationPick.setValue(Env.getAD_Org_ID(Env.getCtx()));
		organizationPick.addVetoableChangeListener(this);
		
		m_AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());

		//  BPartner
		AD_Column_ID = 3499;        //  C_Invoice.C_BPartner_ID
		MLookup lookupBP = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		bpartnerSearch = new VLookup("C_BPartner_ID", true, false, true, lookupBP);
		bpartnerSearch.addVetoableChangeListener(this);
		
		//  BPartner2 faaguilar OFB
		AD_Column_ID = 5398;        //  C_Invoice.C_BPartner_ID
		MLookup lookupBP2 = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		bpartnerSearch2 = new VLookup("C_BPartner_ID", true, false, true, lookupBP2);
		bpartnerSearch2.addVetoableChangeListener(this);
		
		// POImpSearch faaguilar OFB
		AD_Column_ID =  MColumn.getColumn_ID("C_Invoice", "C_OrderPOImp_ID"); //  Invoice.C_OrderPOImp_ID
		MLookup lookupPO = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		POImpSearch = new VLookup("C_OrderPOImp_ID", true, false, true, lookupPO);
		POImpSearch.addVetoableChangeListener(this);
		
		//  Translation
		statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "AllocateStatus"));
		statusBar.setStatusDB("");

		//  Date set to Login Date
		dateField.setValue(Env.getContextAsDate(Env.getCtx(), "#Date"));
		dateField.addVetoableChangeListener(this);
	}   //  dynInit

	/**
	 *  Load Business Partner Info
	 *  - Payments
	 *  - Invoices
	 */
	private void loadBPartner ()
	{		
		log.config("BPartner=" + m_C_BPartner_ID + ", Cur=" + m_C_Currency_ID);
		//  Need to have both values
		if(m_C_BPartner_ID==0 && m_C_BPartner2_ID==0 && m_C_OrderPOImp_ID==0)
			return;
		if((m_C_BPartner_ID==0 || m_C_BPartner2_ID==0)&& m_C_OrderPOImp_ID==0)
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
			+ "p.MultiplierAP, doc.name||'-'||l2.name, bp.name " //faaguilar documenttype 9
			+ "FROM C_Payment_v p"		//	Corrected for AP/AR
			+ " INNER JOIN C_Currency c ON (p.C_Currency_ID=c.C_Currency_ID) "
			+ " INNER join C_Doctype doc ON (p.C_DocType_ID=doc.C_DocType_ID) " //faaguilar documenttype
			+ " LEFT OUTER JOIN AD_Ref_List l2 ON (p.TenderType=l2.value and l2.AD_Reference_ID=214) "//faaguilar documenttype	
			+ " INNER join C_bpartner bp ON (bp.C_bpartner_ID=p.C_bpartner_ID) " //faaguilar partner name
			+ "WHERE p.IsAllocated='N' AND p.Processed='Y'"
			+ " AND p.C_Charge_ID IS NULL");		//	Prepayments OK
		//faaguilar ofb BP & carpeta
		if(m_C_BPartner_ID>0)
			sql.append(" AND p.C_BPartner_ID="+m_C_BPartner_ID);
		if(m_C_OrderPOImp_ID>0 )//faaguilar OFB import
		    sql.append(" AND p.C_OrderPOImp_ID="+m_C_OrderPOImp_ID);
		//faaguilar ofb BP & carpeta END
		if (!multiCurrency.isSelected())
			sql.append(" AND p.C_Currency_ID=?");				//      #6
		if (m_AD_Org_ID != 0 )
			sql.append(" AND p.AD_Org_ID=" + m_AD_Org_ID);
		//sql.append(" AND p.Custodio is null ");
		sql.append(" ORDER BY p.DateTrx,p.DocumentNo");
		
		// role security
		sql = new StringBuffer( MRole.getDefault(Env.getCtx(), false).addAccessSQL( sql.toString(), "p", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO ) );
		
		log.fine("PaySQL=" + sql.toString());
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_C_Currency_ID);
			pstmt.setTimestamp(2, (Timestamp)dateField.getValue());
			pstmt.setInt(3, m_C_Currency_ID);
			pstmt.setTimestamp(4, (Timestamp)dateField.getValue());
			//pstmt.setInt(5, m_C_BPartner_ID);
			if (!multiCurrency.isSelected())
				pstmt.setInt(5, m_C_Currency_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(1));       //  1-TrxDate
				KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(2));
				line.add(pp);                       //  2-DocumentNo
				if (multiCurrency.isSelected())
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
				line.add(rs.getString(9));                  //DocumentType
				line.add(rs.getString(10));                  //Partner
				
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
		//  Remove previous listeners
		paymentTable.getModel().removeTableModelListener(this);
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Util.cleanAmp(Msg.translate(Env.getCtx(), "DocumentNo")));
		if (multiCurrency.isSelected())
		{
			columnNames.add(Msg.getMsg(Env.getCtx(), "TrxCurrency"));
			columnNames.add(Msg.translate(Env.getCtx(), "Amount"));
		}
		columnNames.add(Msg.getMsg(Env.getCtx(), "ConvertedAmount"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "OpenAmt"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "AppliedAmt"));
		columnNames.add("Tipo"); //DocumentType
		columnNames.add("Socio"); //Partner
//		columnNames.add(" ");	//	Multiplier

		//  Set Model
		DefaultTableModel modelP = new DefaultTableModel(data, columnNames);
		modelP.addTableModelListener(this);
		paymentTable.setModel(modelP);
		//
		int i = 0;
		paymentTable.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		paymentTable.setColumnClass(i++, Timestamp.class, true);        //  1-TrxDate
		paymentTable.setColumnClass(i++, String.class, true);           //  2-Value
		if (multiCurrency.isSelected())
		{
			paymentTable.setColumnClass(i++, String.class, true);       //  3-Currency
			paymentTable.setColumnClass(i++, BigDecimal.class, true);   //  4-PayAmt
		}
		paymentTable.setColumnClass(i++, BigDecimal.class, true);       //  5-ConvAmt
		paymentTable.setColumnClass(i++, BigDecimal.class, true);       //  6-ConvOpen
		paymentTable.setColumnClass(i++, BigDecimal.class, false);      //  7-Allocated
		paymentTable.setColumnClass(i++, String.class, false);      //  8-DocumentType
		paymentTable.setColumnClass(i++, String.class, false);      //  8-Partner
//		paymentTable.setColumnClass(i++, BigDecimal.class, true);      	//  8-Multiplier

		//
		i_payment = multiCurrency.isSelected() ? 7 : 5;
		

		//  Table UI
		paymentTable.autoSize();

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
		data = new Vector<Vector<Object>>();
		sql = new StringBuffer("SELECT i.DateAcct,i.DocumentNo,i.C_Invoice_ID," //  1..3
			+ "c.ISO_Code,i.GrandTotal*i.MultiplierAP, "                            //  4..5    Orig Currency
			+ "currencyConvert(i.GrandTotal*i.MultiplierAP,i.C_Currency_ID,?,?,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID), " //  6   #1  Converted, #2 Date
			+ "currencyConvert(invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID),i.C_Currency_ID,?,?,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP, "  //  7   #3, #4  Converted Open
			+ "currencyConvert(invoiceDiscount"                               //  8       AllowedDiscount
			+ "(i.C_Invoice_ID,?,C_InvoicePaySchedule_ID),i.C_Currency_ID,?,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.Multiplier*i.MultiplierAP,"               //  #5, #6
			+ "i.MultiplierAP, doc.name, bp.name as Partner, " //faaguilar DocumentType 10
			+ " i.c_invoicepayschedule_id " //ininoles id de cuota
			+ "FROM C_Invoice_v i"		//  corrected for CM/Split
			+ " INNER JOIN C_Currency c ON (i.C_Currency_ID=c.C_Currency_ID) "
			+ " INNER JOIN C_DocType doc ON (i.C_Doctype_ID=doc.C_DocType_ID) " //faaguilar DocumentType
			+ " INNER join C_bpartner bp ON (bp.C_bpartner_ID=i.C_bpartner_ID) " //faaguilar partner name	
			+ "WHERE i.IsPaid='N' AND i.Processed='Y'");
			//+ " AND i.C_BPartner_ID=?");   
		if(m_C_BPartner2_ID>0)//faaguilar OFB bp
			sql.append(" AND i.C_BPartner_ID="+m_C_BPartner2_ID);//faaguilar OFB bp      
		if(m_C_BPartner_ID>0)//fgonzalez
			sql.append(" or i.C_BPartner_ID="+m_C_BPartner_ID);//fgonzalez   	
		if(m_C_OrderPOImp_ID>0 )//faaguilar OFB import
		    sql.append(" AND i.C_OrderPOImp_ID="+m_C_OrderPOImp_ID);   //faaguilar OFB import		                                         //  #7
		if (!multiCurrency.isSelected())
			sql.append(" AND i.C_Currency_ID=?");                                   //  #8
		if (m_AD_Org_ID != 0 ) 
			sql.append(" AND i.AD_Org_ID=" + m_AD_Org_ID);
		sql.append(" ORDER BY i.DateInvoiced, i.DocumentNo");
		log.fine("InvSQL=" + sql.toString());
		
		// role security
		sql = new StringBuffer( MRole.getDefault(Env.getCtx(), false).addAccessSQL( sql.toString(), "i", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO ) );
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_C_Currency_ID);
			pstmt.setTimestamp(2, (Timestamp)dateField.getValue());
			pstmt.setInt(3, m_C_Currency_ID);
			pstmt.setTimestamp(4, (Timestamp)dateField.getValue());
			pstmt.setTimestamp(5, (Timestamp)dateField.getValue());
			pstmt.setInt(6, m_C_Currency_ID);
			//pstmt.setInt(7, m_C_BPartner_ID);
			if (!multiCurrency.isSelected())
				pstmt.setInt(7, m_C_Currency_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(1));       //  1-TrxDate
				KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(2));
				line.add(pp);                       //  2-Value
				if (multiCurrency.isSelected())
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
				line.add(rs.getString(10)); 		//DocumentType
				line.add(rs.getString(11)); 		//Partner
				line.add(rs.getInt(12)); 		//12 ininoles id cuota
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

		//  Remove previous listeners
		invoiceTable.getModel().removeTableModelListener(this);
		//  Header Info
		columnNames = new Vector<String>();
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Util.cleanAmp(Msg.translate(Env.getCtx(), "DocumentNo")));
		if (multiCurrency.isSelected())
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
		columnNames.add("Tipo");
		columnNames.add("Partner");
		columnNames.add("ID Cuota");
//		columnNames.add(" ");	//	Multiplier

		//  Set Model
		DefaultTableModel modelI = new DefaultTableModel(data, columnNames);
		modelI.addTableModelListener(this);
		invoiceTable.setModel(modelI);
		//
		i = 0;
		invoiceTable.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		invoiceTable.setColumnClass(i++, Timestamp.class, true);        //  1-TrxDate
		invoiceTable.setColumnClass(i++, String.class, true);           //  2-Value
		if (multiCurrency.isSelected())
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
		invoiceTable.setColumnClass(i++, String.class, false);      //  11-DocumentType
		invoiceTable.setColumnClass(i++, String.class, false);      //  12-partner
		invoiceTable.setColumnClass(i++, int.class, false);      //  13-ID_Cuota
//		invoiceTable.setColumnClass(i++, BigDecimal.class, true);      	//  10-Multiplier
		//  Table UI
		invoiceTable.autoSize();

		i_open = multiCurrency.isSelected() ? 6 : 4;
		i_discount = multiCurrency.isSelected() ? 7 : 5;
		i_writeOff = multiCurrency.isSelected() ? 8 : 6;
		i_applied = multiCurrency.isSelected() ? 9 : 7;
		i_overUnder = multiCurrency.isSelected() ? 10 : 8;
//		i_multiplier = multiCurrency.isSelected() ? 10 : 8;
		i_cuota =  multiCurrency.isSelected() ? 13 : 11;

		//  Calculate Totals
		calculate();
	}   //  loadBPartner


	
	/**************************************************************************
	 *  Action Listener.
	 *  - MultiCurrency
	 *  - Allocate
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		log.config("");
		if (e.getSource().equals(multiCurrency))
			loadBPartner();
		//	Allocate
		else if (e.getSource().equals(allocateButton))
		{
			allocateButton.setEnabled(false);
			saveData();
			loadBPartner();
			allocateButton.setEnabled(true);
		}
	}   //  actionPerformed

	/**
	 *  Table Model Listener.
	 *  - Recalculate Totals
	 *  @param e event
	 */
	public void tableChanged(TableModelEvent e)
	{
		boolean isUpdate = (e.getType() == TableModelEvent.UPDATE);
		//  Not a table update
		if (!isUpdate)
		{
			calculate();
			return;
		}
		
		//faaguilar OFB Multiallocation
		if(multiprocess)
			return;
		//faaguilar OFB Multiallocation	
		

		/**
		 *  Setting defaults
		 */
		if (m_calculating)  //  Avoid recursive calls
			return;
		m_calculating = true;
		int row = e.getFirstRow();
		int col = e.getColumn();
		boolean isInvoice = (e.getSource().equals(invoiceTable.getModel()));
		log.config("Row=" + row 
			+ ", Col=" + col + ", InvoiceTable=" + isInvoice);
        
		//  Payments
		if (!isInvoice)
		{
			TableModel payment = paymentTable.getModel();
			
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
			TableModel invoice = invoiceTable.getModel();
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

					if ( autoWriteOff.isSelected() )
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
			if (autoWriteOff.isSelected() && writeOff.doubleValue()/open.doubleValue() > .30)
				ADialog.warn(m_WindowNo, panel, "AllocationWriteOffWarn");
			

			invoice.setValueAt(discount, row, i_discount);
			invoice.setValueAt(applied, row, i_applied);
			invoice.setValueAt(writeOff, row, i_writeOff);
			invoice.setValueAt(overUnder, row, i_overUnder);
			
			invoiceTable.repaint(); //  update r/o
			

		
		}

		m_calculating = false;
		calculate();
	}   //  tableChanged

	/**
	 *  Calculate Allocation info
	 */
	private void calculate ()
	{
		log.config("");
		//
		DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);
		Timestamp allocDate = null;

		//  Payment
		TableModel payment = paymentTable.getModel();
		totalPay = new BigDecimal(0.0);
		int rows = payment.getRowCount();
		m_noPayments = 0;
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)payment.getValueAt(i, 0)).booleanValue())
			{
				Timestamp ts = (Timestamp)payment.getValueAt(i, 1);
				if ( !multiCurrency.isSelected() )  // the converted amounts are only valid for the selected date
					allocDate = TimeUtil.max(allocDate, ts);
				BigDecimal bd = (BigDecimal)payment.getValueAt(i, i_payment);
				totalPay = totalPay.add(bd);  //  Applied Pay
				m_noPayments++;
				log.fine("Payment_" + i + " = " + bd + " - Total=" + totalPay);
			}
		}
		paymentInfo.setText(String.valueOf(m_noPayments) + " - "
			+ Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(totalPay) + " ");

		//  Invoices
		TableModel invoice = invoiceTable.getModel();
		totalInv = new BigDecimal(0.0);
		rows = invoice.getRowCount();
		m_noInvoices = 0;

		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)invoice.getValueAt(i, 0)).booleanValue())
			{
				Timestamp ts = (Timestamp)invoice.getValueAt(i, 1);
				if ( !multiCurrency.isSelected() )  // converted amounts only valid for selected date
					allocDate = TimeUtil.max(allocDate, ts);
				BigDecimal bd = (BigDecimal)invoice.getValueAt(i, i_applied);
				totalInv = totalInv.add(bd);  //  Applied Inv
				m_noInvoices++;
				log.fine("Invoice_" + i + " = " + bd + " - Total=" + totalPay);
			}
		}
		invoiceInfo.setText(String.valueOf(m_noInvoices) + " - "
			+ Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(totalInv) + " ");

		//	Set AllocationDate
		//if (allocDate != null)
		//	dateField.setValue(allocDate);
		//  Set Allocation Currency
		allocCurrencyLabel.setText(currencyPick.getDisplay());
		//  Difference
		totalDiff = totalPay.subtract(totalInv);
		differenceField.setText(format.format(totalDiff));
		
		/*if (totalDiff.compareTo(new BigDecimal(0.0)) == 0)
			allocateButton.setEnabled(true);
		else
			allocateButton.setEnabled(false);*/ //comment by faaguilar OFB
		//faaguilar OFB Begin
		int p_case=revision_multiallocation();
		if (p_case!=INDICATOR_NONE)
		{
			log.info("Indicator:"+p_case);
			indicator=p_case;
			allocateButton.setEnabled(true);
		}
		else
			allocateButton.setEnabled(false);
		//faaguilar OFB End
		
	}   //  calculate

	/**
	 *  Vetoable Change Listener.
	 *  - Business Partner
	 *  - Currency
	 * 	- Date
	 *  @param e event
	 */
	public void vetoableChange (PropertyChangeEvent e)
	{
		String name = e.getPropertyName();
		Object value = e.getNewValue();
		log.config(name + "=" + value);
		
		// Organization
		if (name.equals("AD_Org_ID"))
		{
		if (value == null)
				m_AD_Org_ID = 0;
			else
				m_AD_Org_ID = ((Integer) value).intValue();
			
			loadBPartner();
		}
		
		if (value == null)
			return;

		//  BPartner
	 /*	if (name.equals("C_BPartner_ID"))
		{
			bpartnerSearch.setValue(value);
			m_C_BPartner_ID = ((Integer)value).intValue();
			loadBPartner();
		}*/
		//Begin OFB faaguilar bp
		if(e.getSource().equals(bpartnerSearch))
		{
			log.config("**e.getSource().equals(bpartnerSearch)");
			bpartnerSearch.setValue(value);
			m_C_BPartner_ID = ((Integer)value).intValue();
			loadBPartner();
		}
		else if(e.getSource().equals(bpartnerSearch2))
		{
			log.config("**e.getSource().equals(bpartnerSearch2)");
			bpartnerSearch2.setValue(value);
			m_C_BPartner2_ID = ((Integer)value).intValue();
			loadBPartner();
		}
		else if(e.getSource().equals(POImpSearch))
		{
			POImpSearch.setValue(value);
			m_C_OrderPOImp_ID = ((Integer)value).intValue();
			loadBPartner();
		}//end faaguilar OFB
		//	Currency
		else if (name.equals("C_Currency_ID"))
		{
			m_C_Currency_ID = ((Integer)value).intValue();
			loadBPartner();
		}
		//	Date for Multi-Currency
		else if (name.equals("Date") && multiCurrency.isSelected())
			loadBPartner();
	}   //  vetoableChange

	
	/**************************************************************************
	 *  Save Data
	 */
	private void saveData()
	{
		if (m_noInvoices + m_noPayments == 0)
			return;

		//  fixed fields
		int AD_Client_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Client_ID");
		int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Org_ID");
		AD_Org_ID=m_AD_Org_ID; //faaguilar OFB Fix
		int C_BPartner_ID = m_C_BPartner_ID;
		int C_Order_ID = 0;
		int C_CashLine_ID = 0;
		Timestamp DateTrx = (Timestamp)dateField.getValue();
		int C_Currency_ID = m_C_Currency_ID;	//	the allocation currency
		//
		if (AD_Org_ID == 0)
		{
			AD_Org_ID = m_AD_OrgDoc_ID; //faaguilar OFB
		}
		//faaguilar OFB period open? Begin
		
		if(!MPeriod.isOpen(Env.getCtx(), DateTrx, Doc.DOCTYPE_Allocation))
		{
			ADialog.error(m_WindowNo, panel, "Period Close", null);
			return;
		}
		//Timestamp Today = new Timestamp(TimeUtil.getToday().getTimeInMillis());
		//DateTrx = Today;
		
		//faaguilar OFB period open? End
		//
		log.config("Client=" + AD_Client_ID + ", Org=" + AD_Org_ID
			+ ", BPartner=" + C_BPartner_ID + ", Date=" + DateTrx);
		
		Trx trx = Trx.get(Trx.createTrxName("AL"), true);

		//  Payment - Loop and add them to paymentList/amountList
		int pRows = paymentTable.getRowCount();
		TableModel payment = paymentTable.getModel();
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
				//
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
		int iRows = invoiceTable.getRowCount();
		TableModel invoice = invoiceTable.getModel();
		
		//	Create Allocation
		MAllocationHdr alloc = new MAllocationHdr (Env.getCtx(), true,	//	manual
			DateTrx, C_Currency_ID, Env.getContext(Env.getCtx(), "#AD_User_Name"), trx.getTrxName());
		alloc.setAD_Org_ID(AD_Org_ID);
		if (!alloc.save())
		{
			log.log(Level.SEVERE, "Allocation not created");
			return;
		}
		
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
				if ((Integer)invoice.getValueAt(i, i_cuota) != null && (Integer)invoice.getValueAt(i, i_cuota) > 0)
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
						MAllocationLine aLine = new MAllocationLine (alloc, amount, 
							DiscountAmt, WriteOffAmt, OverUnderAmt);
						aLine.setDocInfo(C_BPartner_ID, C_Order_ID, C_Invoice_ID);
						aLine.setPaymentInfo(C_Payment_ID, C_CashLine_ID);
						if (!aLine.save())
							log.log(Level.SEVERE, "Allocation Line not written - Invoice=" + C_Invoice_ID);

						//ininoles update de cuota
						String sqlInvoiceSchedule = "";
						try
						{
							if (ID_InvoiceSchedule > 0)
							{
								sqlInvoiceSchedule = "UPDATE c_allocationline " +
										"SET c_invoicepayschedule_id = " + ID_InvoiceSchedule +
										" WHERE C_AllocationLine_ID = " +aLine.get_ID();
								int no = DB.executeUpdate(sqlInvoiceSchedule, trx.getTrxName());
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
					if (!aLine.save(trx.getTrxName()))
						log.log(Level.SEVERE, "Allocation Line not written - Invoice=" + C_Invoice_ID);

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
			MAllocationLine aLine = new MAllocationLine (alloc, payAmt, 
				Env.ZERO, Env.ZERO, Env.ZERO);
			aLine.setDocInfo(C_BPartner_ID, 0, 0);
			aLine.setPaymentInfo(C_Payment_ID, 0);
			if (!aLine.save(trx.getTrxName()))
				log.log(Level.SEVERE, "Allocation Line not saved - Payment=" + C_Payment_ID);
			unmatchedApplied = unmatchedApplied.subtract(payAmt);
		}		
		
		if ( unmatchedApplied.signum() != 0 )
			log.log(Level.SEVERE, "Allocation not balanced -- out by " + unmatchedApplied );

		//	Should start WF
		if (alloc.get_ID() != 0)
		{
			alloc.processIt(DocAction.ACTION_Complete);
			alloc.save();
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
				BigDecimal open = DB.getSQLValueBD(trx.getTrxName(), sql, C_Invoice_ID);
				if (open != null && open.signum() == 0)	{
					sql = "UPDATE C_Invoice SET IsPaid='Y' "
						+ "WHERE C_Invoice_ID=" + C_Invoice_ID;
					int no = DB.executeUpdate(sql, trx.getTrxName());
					log.config("Invoice #" + i + " is paid - updated=" + no);
				} else
					log.config("Invoice #" + i + " is not paid - " + open);
			}
		}
		//  Test/Set Payment is fully allocated
		for (int i = 0; i < paymentList.size(); i++)
		{
			int C_Payment_ID = ((Integer)paymentList.get(i)).intValue();
			MPayment pay = new MPayment (Env.getCtx(), C_Payment_ID, trx.getTrxName());
			if (pay.testAllocation())
				pay.save();
			log.config("Payment #" + i + (pay.isAllocated() ? " not" : " is") 
					+ " fully allocated");
		}
		paymentList.clear();
		amountList.clear();
		trx.commit();
		trx.close();

		autoWriteOff.setSelected(false);//faaguilar OFB
		statusBar.setStatusLine(alloc.getDocumentNo());
	}   //  saveData
	
	
	//faaguilar OFB method
	private int revision_multiallocation()
	{
		multiprocess=true;
		log.info("multiallocation custom method");
        BigDecimal difference = new BigDecimal("0.0");
		//  Payment
		TableModel payment = paymentTable.getModel();
		BigDecimal totalPay = new BigDecimal(0.0);
		BigDecimal payamt1 = new BigDecimal(0.0);
		BigDecimal payamt2 = new BigDecimal(0.0);
		int lastPayment_ID=0;
		int lastInvoice_ID=0;
		int rows = payment.getRowCount();
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
		TableModel invoice = invoiceTable.getModel();
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
				lastInvoice_ID = pp.getKey();
				
				log.fine( "Invoice_" + i + " = " + bd + " - Total=" + totalPay);
			}
		}
		
		totalPayment=totalPay;
		totalInvoiced=totalInv;
		log.config("noInvoices:"+ m_noInvoices +" total: " + totalInvoiced+ " noPayments:"+ m_noPayments + "total:"+totalPay);
		if(m_noInvoices> 1 && m_noPayments>1)
		{
			RetValue= INDICATOR_NONE;
			statusBar.setStatusLine("Muchos vs Muchos no permitido");
		}
		
		if(autoWriteOff.isSelected())
		{
			RetValue= INDICATOR_NONE;
			statusBar.setStatusLine("debe usar la ventana comun");
		}
		
				
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
					RetValue= INDICATOR_SUBPAYMENT_SO;
				}
				else //<0 greater payment
				{
					payment.setValueAt(totalInvoiced, payrow, i_payment);
					invoice.setValueAt(totalInvoiced, invrow, i_applied);
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
					RetValue= INDICATOR_VERSUS_SUBINVOICE;
				}
				else if (invamt2.abs().compareTo(invamt1.abs())>0)//invamt1 sub
				{
					if(invamt1.intValue()<0)
						invoice.setValueAt(invamt1.abs(), invrow2, i_applied);
					else
						invoice.setValueAt(invamt1.negate(), invrow2, i_applied);	
					invoice.setValueAt(invamt1, invrow, i_applied);
					RetValue= INDICATOR_VERSUS_SUBINVOICE;
				}
				else if (invamt2.abs().compareTo(invamt1.abs())==0)//invamt1 equals invamt2
				{
					invoice.setValueAt(invamt1, invrow, i_applied);	
					invoice.setValueAt(invamt2, invrow2, i_applied);
					RetValue= INDICATOR_VERSUS_INVOICE;
					
				}
			}
		}// Chilean Implementation, if you need, only comment End
		
		
		
		if(lastPayment_ID>0)
			m_AD_OrgDoc_ID=DB.getSQLValue(null, "select ad_org_id from c_payment where c_payment_id="+lastPayment_ID);
		else
			m_AD_OrgDoc_ID=DB.getSQLValue(null, "select ad_org_id from c_invoice where c_invoice_id="+lastInvoice_ID);
		
		
		multiprocess=false;
		return RetValue;
		
	}

}   //  VAllocation
