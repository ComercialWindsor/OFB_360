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
import javax.swing.JOptionPane;

import org.adempiere.plaf.AdempierePLAF;
import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MCash;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
import org.compiere.model.PrintInfo;
import org.compiere.model.X_MP_OT;
import org.compiere.plaf.CompiereColor;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportCtl;
import org.compiere.print.ReportEngine;
import org.compiere.print.ReportViewerProvider;
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

import org.ofb.model.OFBForward;

/**
 * Allocation Form
 *
 * @author  Jorg Janke
 * @version $Id: VAllocation.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 * 
 * Contributor : Fabian Aguilar - OFBConsulting - Multiallocation
 */
public class VGeneratePayInvoice extends CPanel
	implements FormPanel, ActionListener, VetoableChangeListener
{
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

	private int         m_C_Currency_ID = 0;
	private int         m_C_Order_ID = 0;
	private String		m_Banco;
	private String 		m_CreditCard;
	private String		m_TenderType;
	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private CPanel allocationPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private JLabel orderLabel = new JLabel();
	private VLookup orderSearch = null;
	private JLabel TenderLabel = new JLabel();
	private VLookup Tendertype = null;
	private JLabel AccountLabel = new JLabel();
	private VLookup BankAccount = null;
	private JLabel BancosLabel = new JLabel();
	private VLookup Bancos = null;
	private JLabel CardTypeLabel = new JLabel();
	private VLookup CreditCardType = null;
	private JLabel CheckNoLabel = new JLabel();
	private CTextField CheckNo = new CTextField();
	private JLabel A_nameLabel = new JLabel();
	private CTextField A_name = new CTextField();
	private JLabel CreditCardNumberLabel = new JLabel();
	private CTextField CreditCardNumber = new CTextField();
	private JLabel accountLabel = new JLabel();
	private CTextField accountNumber = new CTextField();
	private JLabel dateLabel = new JLabel();
	private VDate dateField = new VDate();
	private JCheckBox cashPay = new JCheckBox();
	private MiniTable invoiceTable = new MiniTable();
	private CPanel infoPanel = new CPanel();
	private CPanel paymentPanel = new CPanel();
	private CPanel invoicePanel = new CPanel();
	private JLabel paymentLabel = new JLabel();
	private JLabel invoiceLabel = new JLabel();
	private BorderLayout invoiceLayout = new BorderLayout();
	private JLabel paymentInfo = new JLabel();
	private JLabel invoiceInfo = new JLabel();
	private JScrollPane invoiceScrollPane = new JScrollPane();
	private GridBagLayout allocationLayout = new GridBagLayout();
	private JButton allocateButton = new JButton();
	private StatusBar statusBar = new StatusBar();
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
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		TenderLabel.setText("Tipo de Pago");
		AccountLabel.setText("Cuenta Bancaria");
		CreditCardNumberLabel.setText("Numero");
		CheckNoLabel.setText("Num. Cheque");
		BancosLabel.setText("Banco");
		CardTypeLabel.setText("Tarjeta");
		cashPay.setText("Efectivo?");
		A_nameLabel.setText("Emisor");
		cashPay.setSelected(false);
		cashPay.addActionListener(this);
		CheckNo.setColumns(10);
		CreditCardNumber.setColumns(10);
		A_name.setColumns(20);
		dateLabel.setText("Vencimiento");
		dateField.setValue(Env.getContextAsDate(Env.getCtx(), "#Date"));
		accountNumber.setColumns(12);
		accountLabel.setText("Num. Cuenta");
		//
		parameterPanel.setLayout(parameterLayout);
		allocationPanel.setLayout(allocationLayout);
		orderLabel.setText(Msg.translate(Env.getCtx(), "C_Order_ID"));
		invoiceLabel.setRequestFocusEnabled(false);
		invoiceLabel.setText("Documentos");
//		paymentPanel.setLayout(paymentLayout);
		invoicePanel.setLayout(invoiceLayout);
		invoiceInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		invoiceInfo.setHorizontalTextPosition(SwingConstants.RIGHT);
		invoiceInfo.setText(".");
//		paymentInfo.setHorizontalAlignment(SwingConstants.RIGHT);
//		paymentInfo.setHorizontalTextPosition(SwingConstants.RIGHT);
//		paymentInfo.setText(".");
		allocateButton.setText(Msg.getMsg(Env.getCtx(), "Process"));
		allocateButton.addActionListener(this);
		invoiceScrollPane.setPreferredSize(new Dimension(200, 200));
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		
		//org filter
//		organizationLabel.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
//		parameterPanel.add(organizationLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
//				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));
//		parameterPanel.add(organizationPick, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
//				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));
//		
		parameterPanel.add(orderLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(orderSearch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(cashPay, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
			
		parameterPanel.add(TenderLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(Tendertype, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		
		parameterPanel.add(AccountLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(BankAccount, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(dateLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(dateField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(CheckNoLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(CheckNo, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(CardTypeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(CreditCardType, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(CreditCardNumberLabel, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(CreditCardNumber, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		parameterPanel.add(A_nameLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(A_name, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(accountLabel, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(accountNumber, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	
		
		mainPanel.add(allocationPanel, BorderLayout.SOUTH);
		
		allocationPanel.add(allocateButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		
		paymentPanel.add(paymentLabel, BorderLayout.NORTH);
		paymentPanel.add(paymentInfo, BorderLayout.SOUTH);
		invoicePanel.add(invoiceLabel, BorderLayout.NORTH);
		invoicePanel.add(invoiceInfo, BorderLayout.SOUTH);
		invoicePanel.add(invoiceScrollPane, BorderLayout.CENTER);
		invoiceScrollPane.getViewport().add(invoiceTable, null);
		//
		infoPanel.setLayout(new BorderLayout());
		mainPanel.add(infoPanel, BorderLayout.CENTER);
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel.add(invoicePanel,BorderLayout.CENTER);
		infoPanel.setPreferredSize(new Dimension(800,250));
		
		m_TenderType=(String)Tendertype.getValue();
		m_CreditCard=(String)CreditCardType.getValue();
//		infoPanel.setDividerLocation(110);
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
	private void dynInit() throws Exception
	{
		// Organization filter selection
		int AD_Column_ID = 839; //C_Period.AD_Org_ID (needed to allow org 0)
		MLookup lookupOrg = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		organizationPick = new VLookup("AD_Org_ID", true, false, true, lookupOrg);
		organizationPick.setValue(Env.getAD_Org_ID(Env.getCtx()));
		organizationPick.addVetoableChangeListener(this);
		
		m_AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());

		// Order
		AD_Column_ID = 4247;        //  C_Invoice.C_Order_ID
		MLookup lookupOrder = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		orderSearch = new VLookup("C_Order_ID", true, false, true, lookupOrder);
		orderSearch.addVetoableChangeListener(this);
		
		//Tender Type
		AD_Column_ID = 5046;        //  C_Payment.TenderType
		MLookup lookupTender = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.List);
		Tendertype = new VLookup("TenderType", true, false, true, lookupTender);
		Tendertype.addVetoableChangeListener(this);
		
		//Bank Account
		AD_Column_ID = 3880;
		MLookup lookupBank = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		BankAccount = new VLookup("C_BankAccount_ID", true, false, true, lookupBank);
		BankAccount.addVetoableChangeListener(this);
		
		//Bancos
		AD_Column_ID = DB.getSQLValue(null, "select AD_Column_ID from AD_Column where AD_Table_ID=335 and upper(columnname)='BANCOS'");
		MLookup lookupBancos = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.List);
		Bancos = new VLookup("Bancos", true, false, true, lookupBancos);
		Bancos.addVetoableChangeListener(this);
		
		//CreditCardType
		AD_Column_ID = 3869;
		MLookup lookupCardType = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.List);
		CreditCardType = new VLookup("CreditCardType", true, false, true, lookupCardType);
		CreditCardType.addVetoableChangeListener(this);
		
		
		
		
		statusBar.setStatusDB("");

		
	}   //  dynInit

	/**
	 *  Load Business Partner Info
	 *  - Payments
	 *  - Invoices
	 */
	private void loadBPartner ()
	{		
		log.config("Order=" + m_C_Order_ID );
		//  Need to have both values
		if (m_C_Order_ID == 0)
			return;

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
		StringBuffer sql = new StringBuffer("select doc.name,i.dateinvoiced,i.documentno,i.description,i.grandtotal,i.docstatus,l.name "
		+" from c_invoice i "
		+" inner join c_doctype doc on (i.c_doctype_id=doc.c_doctype_id)"
		+" inner join AD_Ref_List l on (i.PaymentRule=l.value and l.AD_Reference_ID=195)"
		+" where i.c_order_id=?"
		+" union all"
		+" select doc.name,s.datetrx,s.documentno,l.name,s.payamt,s.docstatus,s.A_Name"
		+" from c_payment s"
		+" inner join c_doctype doc on (s.c_doctype_id=doc.c_doctype_id)"
		+" inner join AD_Ref_List l on (s.tendertype=l.value and l.AD_Reference_ID=214)"
		+" where s.c_invoice_id IN (select c_invoice_id from c_invoice where c_order_id=?)");                                   //  #7
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_C_Order_ID);
			pstmt.setInt(2, m_C_Order_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(rs.getString(1));//document type
				line.add(rs.getTimestamp(2));//date
				line.add(rs.getString(3));//document no
				line.add(rs.getString(4));//description
				line.add(rs.getBigDecimal(5));//total
				line.add(rs.getString(6));//docstatus
				line.add(rs.getString(7));//tipo
				
				
					data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}

		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("Tipo Documento");
		columnNames.add("Fecha");
		columnNames.add("Documento");
		columnNames.add( "Descripcion");
		columnNames.add( "Total");
		columnNames.add( "Estado");
		columnNames.add( "Tipo");

		//  Set Model
		DefaultTableModel modelI = new DefaultTableModel(data, columnNames);
		invoiceTable.setModel(modelI);
		//
		int i = 0;
		invoiceTable.setColumnClass(i++, String.class, false);         //  0-Selection
		invoiceTable.setColumnClass(i++, Timestamp.class, true);        //  1-TrxDate
		invoiceTable.setColumnClass(i++, String.class, true);           //  2-Value
		invoiceTable.setColumnClass(i++, String.class, true);       //  5-ConvAmt
		invoiceTable.setColumnClass(i++, BigDecimal.class, true);       //  6-ConvAmt Open
		invoiceTable.setColumnClass(i++, String.class, true);       //  5-ConvAmt
		//  Table UI
		invoiceTable.autoSize();

		//  Calculate Totals
	}   //  loadBPartner


	
	/**************************************************************************
	 *  Action Listener.
	 *  - MultiCurrency
	 *  - Allocate
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(allocateButton))
		{
			saveData();
		}
		
		else if (e.getSource().equals(cashPay))
		{
			if(cashPay.isSelected())
			{
				//clean
				CheckNo.setText("");
				A_name.setText("");
				CreditCardNumber.setText("");
				accountNumber.setText("");
				//clean
				CreditCardType.setVisible(false);
				CheckNoLabel.setVisible(false);
				CheckNo.setVisible(false);
				AccountLabel.setVisible(false);
				CreditCardNumber.setVisible(false);
				accountLabel.setVisible(false);
				accountNumber.setVisible(false);
				Tendertype.setVisible(false);
				TenderLabel.setVisible(false);
				AccountLabel.setVisible(false);
				BankAccount.setVisible(false);
				CardTypeLabel.setVisible(false);
				CreditCardNumberLabel.setVisible(false);
				BancosLabel.setVisible(false);
				A_nameLabel.setVisible(false);
				A_name.setVisible(false);
				dateLabel.setVisible(false);
				dateField.setVisible(false);
				
				
			}
			else
			{
				//clean
				CheckNo.setText("");
				A_name.setText("");
				CreditCardNumber.setText("");
				accountNumber.setText("");
				CreditCardType.setVisible(true);
				CheckNoLabel.setVisible(true);
				CheckNo.setVisible(true);
				AccountLabel.setVisible(true);
				BankAccount.setVisible(true);
				CreditCardNumber.setVisible(true);
				accountLabel.setVisible(true);
				accountNumber.setVisible(true);
				Tendertype.setVisible(true);
				TenderLabel.setVisible(true);
				AccountLabel.setVisible(true);
				CardTypeLabel.setVisible(true);
				CreditCardNumberLabel.setVisible(true);
				BancosLabel.setVisible(true);
				A_nameLabel.setVisible(true);
				A_name.setVisible(true);
				dateLabel.setVisible(true);
				dateField.setVisible(true);
			}
		}
		
		
	}   //  actionPerformed


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

		//  Order
		if (name.equals("C_Order_ID"))
		{
			orderSearch.setValue(value);
			m_C_Order_ID = ((Integer)value).intValue();
			loadBPartner();
		}
		else if (name.equals("TenderType"))
		{
			Tendertype.setValue(value);
			m_TenderType = (String)value;
			
			if(m_TenderType.equals("K")  ){
				CreditCardType.setVisible(false);
				AccountLabel.setVisible(false);
				CreditCardNumber.setVisible(false);
				CardTypeLabel.setVisible(false);
				CreditCardNumberLabel.setVisible(false);
				//**********************
				//clean
				CheckNo.setText("");
				A_name.setText("");
				CreditCardNumber.setText("");
				accountNumber.setText("");
				//clean
				CheckNoLabel.setVisible(true);
				CheckNo.setVisible(true);
				AccountLabel.setVisible(true);
				accountLabel.setVisible(true);
				accountNumber.setVisible(true);
				dateLabel.setVisible(true);
				dateField.setVisible(true);

			}
			else if(m_TenderType.equals("C")  ){
				//clean
				CheckNo.setText("");
				A_name.setText("");
				CreditCardNumber.setText("");
				accountNumber.setText("");
				//clean
				
				CheckNoLabel.setVisible(false);
				CheckNo.setVisible(false);
				AccountLabel.setVisible(false);
				accountLabel.setVisible(false);
				accountNumber.setVisible(false);
				dateLabel.setVisible(false);
				dateField.setVisible(false);
				//*********************
				CreditCardType.setVisible(true);
				AccountLabel.setVisible(true);
				BankAccount.setVisible(true);
				CreditCardNumber.setVisible(true);
				CardTypeLabel.setVisible(true);
				CreditCardNumberLabel.setVisible(true);
				
			}
		}
		else if (name.equals("C_BankAccount_ID"))
		{
			BankAccount.setValue(value);
			((Integer)value).intValue();
		}
		else if (name.equals("Bancos"))
		{
			Bancos.setValue(value);
			m_Banco=(String)value;
		}
		else if (name.equals("CreditCardType"))
		{
			CreditCardType.setValue(value);
			m_CreditCard=(String)value;
		}
		
	}   //  vetoableChange

	
	/**************************************************************************
	 *  Save Data
	 */
	private void saveData()
	{
		if(!validar())
			return;
		Trx trx = Trx.get(Trx.createTrxName("AL"), true);
		
		try{
			MOrder order=new MOrder(Env.getCtx(),m_C_Order_ID ,trx.getTrxName());
			MInvoice invoice=null;
			
			int m_noInvoices=DB.getSQLValue(trx.getTrxName(), "select count(1) from c_invoice where docstatus IN ('DR','CO','CL') and c_order_id="+m_C_Order_ID);
			
			if(order.getDocStatus().equals("DR") || order.getDocStatus().equals("VO")){
				statusBar.setStatusLine("La Orden no esta completada");
				return;
			}
			
			if(m_noInvoices<=0)//creating invoice
			{
				//validacion antes de crear la factura
				//faaguilar OFB cashbook default
				
				//mfrojas validador ofbforward windsor
				int revisar = 0;
				try
				{
					boolean usecashbook = OFBForward.WindsorUseCashBook();
					if(usecashbook)
					{
						revisar = 1;
					}
				}
				catch (Exception e)
				{
					log.config(e.toString());
				}
				if(revisar == 0)
				{	
					int C_CashBook_ID=DB.getSQLValue(trx.getTrxName(),"select C_CashBook_ID from C_CashBook where ISDEFAULT='Y' and AD_Org_ID IN (0,"+order.getAD_Org_ID()+") and AD_Client_ID="+order.getAD_Client_ID());
					if(C_CashBook_ID<=0)
					{
						JOptionPane.showMessageDialog(this, "No existe un libro de efectivo por defecto", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					MCash cash;
					cash = MCash.getDefault (Env.getCtx(), order.getAD_Client_ID(), 
							C_CashBook_ID,  trx.getTrxName());
					if (cash == null || cash.get_ID() == 0)
					{
						JOptionPane.showMessageDialog(this, "No hay una caja de efectivo disponible", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
	            //faaguilar OFB cashbook default END
				
				//fin validacion
				invoice= new MInvoice(order,0,Env.getContextAsDate(Env.getCtx(), "#Date"));
				
				if(cashPay.isSelected()){
					invoice.setPaymentRule(MInvoice.PAYMENTRULE_Cash);
				}
				else if(((String)Tendertype.getValue()).equals("K")  ){
					invoice.setPaymentRule(MInvoice.PAYMENTRULE_Check);
				}
				else if(((String)Tendertype.getValue()).equals("C")  ){
					invoice.setPaymentRule(MInvoice.PAYMENTRULE_CreditCard);
				}
				else if(((String)Tendertype.getValue()).equals("D")  ){
					invoice.setPaymentRule(MInvoice.PAYMENTRULE_DirectDebit);
				}
				else if(((String)Tendertype.getValue()).equals("G")  ){
					invoice.setPaymentRule("G");
				}
				else if(((String)Tendertype.getValue()).equals("B")  ){
					invoice.setPaymentRule("E");
				}
				else {
					invoice.setPaymentRule(MInvoice.PAYMENTRULE_OnCredit);
				}
				invoice.save();
				for(int i=0;i<order.getLines().length;i++)
				{
					MInvoiceLine line= new MInvoiceLine(invoice);
					line.setOrderLine(order.getLines()[i]);
					line.setQty(order.getLines()[i].getQtyOrdered());
					line.save();
				}
				invoice.setDocAction(MInvoice.ACTION_Complete);
				invoice.processIt(MInvoice.ACTION_Complete);
				invoice.save();
			}
			
			if(m_noInvoices<=0 && !cashPay.isSelected())//creating payment
			{
				MPayment payment=new MPayment(Env.getCtx(),0 ,trx.getTrxName());
				//payment.setAD_Org_ID(Env.getContextAsInt(Env.getCtx(), "#AD_Org_ID"));
				payment.setAD_Org_ID(order.getAD_Org_ID());
				payment.setC_Invoice_ID(order.getC_Invoice_ID());
				payment.setTenderType((String)Tendertype.getValue());
				payment.setDateTrx((Timestamp)dateField.getValue());
				payment.setDateAcct(Env.getContextAsDate(Env.getCtx(), "#Date"));
				payment.setPayAmt(order.getGrandTotal());
				payment.setC_BPartner_ID(order.getC_BPartner_ID());
				payment.setCheckNo(CheckNo.getText());
				payment.setCreditCardType((String)CreditCardType.getValue());
				payment.setCreditCardNumber(CreditCardNumber.getText());
				payment.setAccountNo(accountNumber.getText());
				payment.setC_BankAccount_ID(((Integer)BankAccount.getValue()).intValue());
				payment.setC_Currency_ID(order.getC_Currency_ID());
				payment.setIsReceipt(true);
				payment.setA_Name(A_name.getText());
				payment.save();
				payment.setDocAction(MPayment.ACTION_Complete);
				payment.processIt(MPayment.ACTION_Complete);
				payment.save();
				
				if(invoice!=null)
				{
					invoice.setC_Payment_ID(payment.getC_Payment_ID());
					invoice.save();
				}
			}
			trx.commit();
			trx.close();
			
			//impresion de factura
			if(invoice!=null){
				try{
					MDocType doc=new MDocType(Env.getCtx(), invoice.getC_DocType_ID(),null);
					int AD_PrintFormat_ID = doc.getAD_PrintFormat_ID();
					int C_BPartner_ID = invoice.getC_BPartner_ID();
					String DocumentNo = invoice.getDocumentNo();
					int copies = 1;
					MPrintFormat format = MPrintFormat.get (Env.getCtx(), AD_PrintFormat_ID, false);
					MQuery query = new MQuery("MP_OT");
					query.addRestriction("C_Invoice_ID", MQuery.EQUAL, new Integer(invoice.getC_Invoice_ID()));
					PrintInfo info = new PrintInfo(
							DocumentNo,
							invoice.get_Table_ID(),
							invoice.getC_Invoice_ID(),
							C_BPartner_ID);
						info.setCopies(copies);
						info.setDocumentCopy(false);		//	true prints "Copy" on second
						info.setPrinterName(format.getPrinterName());
					ReportEngine re = new ReportEngine(Env.getCtx(), format, query, info, null);
					ReportViewerProvider provider = ReportCtl.getReportViewerProvider();
					provider.openViewer(re);
				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(this, "No se Encuetra bien definido el formato de impresion", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			statusBar.setStatusLine(e.getMessage());
		}
		//clean
		CheckNo.setText("");
		A_name.setText("");
		CreditCardNumber.setText("");
		accountNumber.setText("");
		//Tender Type
		/*int AD_Column_ID = 5046;        //  C_Payment.TenderType
		MLookup lookupTender = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.List);
		Tendertype = new VLookup("TenderType", true, false, true, lookupTender);
		Tendertype.addVetoableChangeListener(this);
		
		//Bank Account
		AD_Column_ID = 3880;
		MLookup lookupBank = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		BankAccount = new VLookup("C_BankAccount_ID", true, false, true, lookupBank);
		BankAccount.addVetoableChangeListener(this);
		
		//Bancos
		AD_Column_ID = DB.getSQLValue(null, "select AD_Column_ID from AD_Column where AD_Table_ID=335 and upper(columnname)='BANCOS'");
		MLookup lookupBancos = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.List);
		Bancos = new VLookup("Bancos", true, false, true, lookupBancos);
		Bancos.addVetoableChangeListener(this);
		*/
		//clean
		loadBPartner();
	}   //  saveData

	public boolean validar(){
		boolean revalue=true;
		
		if(m_TenderType.equals("K")){
			if(CheckNo.getText()==null || CheckNo.getText().length()==0){
				JOptionPane.showMessageDialog(null, "Debe Ingresar un Numero de Cheque");
				revalue=false;
			}
			
		}
		else if(m_TenderType.equals("C")){
			if(CreditCardNumber.getText()==null || CreditCardNumber.getText().length()==0){
				JOptionPane.showMessageDialog(null, "Debe Ingresar un Numero de Tarjeta");
				revalue=false;
			}
			
		}
		
		if(m_C_Order_ID==0){
			JOptionPane.showMessageDialog(null, "Debe Seleccionar una orden de venta");
			revalue=false;
		}
		
		return revalue;
	}
}   //  VAllocation
