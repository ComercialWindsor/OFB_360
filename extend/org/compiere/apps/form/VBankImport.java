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

import java.awt.BorderLayout;
import java.awt.Cursor;
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
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.plaf.AdempierePLAF;
import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.grid.ed.VNumber;
import org.compiere.minigrid.IMiniTable;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MBankAccount;
import org.compiere.model.MBankStatement;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MColumn;
import org.compiere.model.MFactAcct;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPayment;
import org.compiere.model.X_C_BankMatch;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;

public class VBankImport 
	implements FormPanel, ActionListener, TableModelListener, VetoableChangeListener
{
	private CPanel panel = new CPanel();

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public static CLogger log = CLogger.getCLogger(Allocation.class);
	
	public void init (int WindowNo, FormFrame frame)
	{
		m_WindowNo = WindowNo;
		m_frame = frame;
		Env.setContext(Env.getCtx(), m_WindowNo, "IsSOTrx", "Y");   //  defaults to no
		try
		{
			dynInit();
			jbInit();
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
			trx = Trx.get(Trx.createTrxName("AL"), true);
			 loadBankAccountOK();
			 loadBankAccountNO();
			 loadBankAccountFree();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init

	public MBankAccount bankAccount;
	/**	Window No			*/
	private int         m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 	m_frame;
	private MiniTable paymentOKTable = new MiniTable();
	private CPanel paymentOKPanel = new CPanel();
	private JLabel paymentOKLabel = new JLabel();
	private BorderLayout paymentOKLayout = new BorderLayout();
	private JScrollPane paymentOKScrollPane = new JScrollPane();
	
	private MiniTable paymentNoTable = new MiniTable();
	private CPanel paymentNoPanel = new CPanel();
	private JLabel paymentNoLabel = new JLabel();
	private BorderLayout paymentNoLayout = new BorderLayout();
	private JScrollPane paymentNoScrollPane = new JScrollPane();
	
	private MiniTable paymentFreeTable = new MiniTable();
	private CPanel paymentFreePanel = new CPanel();
	private JLabel paymentFreeLabel = new JLabel();
	private BorderLayout paymentFreeLayout = new BorderLayout();
	private JScrollPane paymentFreeScrollPane = new JScrollPane();
	
	private CPanel  centerPanel = new CPanel();
	private BorderLayout centerLayout = new BorderLayout();
	
	private JSplitPane infoPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	
	private StatusBar statusBar = new StatusBar();
	
	private GridBagLayout allocationLayout = new GridBagLayout();
	
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private CPanel allocationPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();

	private JLabel documentTypeLabel = new JLabel();
	protected VLookup documentTypeField;
	
	private JLabel bankAccountLabel = new JLabel();
	protected VLookup bankAccountField;
	
	private CLabel amtFromLabel = new CLabel(Msg.translate(Env.getCtx(), "PayAmt"));
	protected VNumber amtFromField = new VNumber("AmtFrom", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "AmtFrom"));
	private CLabel amtToLabel = new CLabel("-");
	protected VNumber amtToField = new VNumber("AmtTo", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "AmtTo"));
	
	protected CLabel BPartner_idLabel = new CLabel(Msg.translate(Env.getCtx(), "BPartner"));
	protected VLookup bPartnerLookup;
	
	private CLabel dateFromLabel = new CLabel(Msg.translate(Env.getCtx(), "DateTrx"));
	protected VDate dateFromField = new VDate("DateFrom", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateFrom"));
	private CLabel dateToLabel = new CLabel("-");
	protected VDate dateToField = new VDate("DateTo", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateTo"));
	
	private JButton actionButton = new JButton();
	
	private JButton matchButton  = new JButton();
	private JButton tempMatchButton  = new JButton();
	private JButton delMatchButton  = new JButton();
	private JButton searchButton  = new JButton();
	
	private boolean validMatch = false;
	private JCheckBox onlyTemp = new JCheckBox();
	private Trx trx= null;

	
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(panel);
		//
		mainPanel.setLayout(mainLayout);
		centerPanel.setLayout(centerLayout);
		bankAccountLabel.setText(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
    	
		paymentNoLabel.setText("Movimiento Importado");
		paymentFreeLabel.setText("Documento de Sistema");
		paymentOKLabel.setText("Documentos Cotejados");
		
		onlyTemp.setSelected(false);
		onlyTemp.setText("Reasignar Temporales");
    	
    	dateFromLabel.setLabelFor(dateFromField);
    	dateFromField.setToolTipText(Msg.translate(Env.getCtx(), "DateFrom"));
    	dateToLabel.setLabelFor(dateToField);
    	dateToField.setToolTipText(Msg.translate(Env.getCtx(), "DateTo"));
    	amtFromLabel.setLabelFor(amtFromField);
    	amtFromField.setToolTipText(Msg.translate(Env.getCtx(), "AmtFrom"));
    	amtToLabel.setLabelFor(amtToField);
    	amtToField.setToolTipText(Msg.translate(Env.getCtx(), "AmtTo"));
    	
    	documentTypeLabel.setText(Msg.translate(Env.getCtx(), "C_DocType_ID"));
    	
    	actionButton.setText("Confirmar/Procesar");
    	matchButton.setText("Cojetar");
    	tempMatchButton.setText("Cojetar Temporal");
    	searchButton.setText("Buscar");
    	delMatchButton.setText("DesCotejar");
    	matchButton.addActionListener(this);
    	tempMatchButton.addActionListener(this);
    	actionButton.addActionListener(this);
    	searchButton.addActionListener(this);
    	delMatchButton.addActionListener(this);
		//
		parameterPanel.setLayout(parameterLayout);
		paymentNoPanel.setLayout(paymentNoLayout);
		paymentFreePanel.setLayout(paymentFreeLayout);
		paymentOKPanel.setLayout(paymentOKLayout);
		allocationPanel.setLayout(allocationLayout);
		
		
		paymentNoPanel.setPreferredSize(new Dimension(300, 600));
		paymentFreePanel.setPreferredSize(new Dimension(300, 600));
		paymentOKPanel.setPreferredSize(new Dimension(400, 300));
		
		
		bankAccountField.setSize(bankAccountField.getWidth()+10, bankAccountField.getHeight()+10);
		
		
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		
		parameterPanel.add(bankAccountLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	if (bankAccountField != null)
    		parameterPanel.add(bankAccountField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    	parameterPanel.add(BPartner_idLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    		parameterPanel.add(bPartnerLookup, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));   	
    		
    		parameterPanel.add(documentTypeLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
        			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        	if(documentTypeField!= null)
        		parameterPanel.add(documentTypeField, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
        				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    
    	parameterPanel.add(amtFromLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterPanel.add(amtFromField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	parameterPanel.add(amtToLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterPanel.add(amtToField, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    	parameterPanel.add(dateFromLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterPanel.add(dateFromField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	parameterPanel.add(dateToLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterPanel.add(dateToField, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	parameterPanel.add(searchButton, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	
    	mainPanel.add(allocationPanel, BorderLayout.SOUTH);
    	allocationPanel.add(onlyTemp, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    	
    	mainPanel.add(allocationPanel, BorderLayout.SOUTH);
    	allocationPanel.add(matchButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    	
    	mainPanel.add(allocationPanel, BorderLayout.SOUTH);
    	allocationPanel.add(tempMatchButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    	
    	mainPanel.add(allocationPanel, BorderLayout.SOUTH);
    	allocationPanel.add(delMatchButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    	
    		allocationPanel.add(actionButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    	
    	paymentNoPanel.add(paymentNoLabel, BorderLayout.NORTH);
		paymentNoPanel.add(paymentNoScrollPane, BorderLayout.CENTER);
		paymentNoScrollPane.getViewport().add(paymentNoTable, null);
		
		paymentFreePanel.add(paymentFreeLabel, BorderLayout.NORTH);
		paymentFreePanel.add(paymentFreeScrollPane, BorderLayout.CENTER);
		paymentFreeScrollPane.getViewport().add(paymentFreeTable, null);
		
		paymentOKPanel.add(paymentOKLabel, BorderLayout.NORTH);
		paymentOKPanel.add(paymentOKScrollPane, BorderLayout.CENTER);
		paymentOKScrollPane.getViewport().add(paymentOKTable, null);
		
		infoPanel.setLeftComponent(paymentNoPanel);
		infoPanel.setRightComponent(paymentFreePanel);
		infoPanel.add(paymentNoPanel, JSplitPane.LEFT);
		infoPanel.add(paymentFreePanel, JSplitPane.RIGHT );
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel.setContinuousLayout(true);
		infoPanel.setPreferredSize(new Dimension(800,600));
		
		centerPanel.add(infoPanel,BorderLayout.CENTER);
		centerPanel.add(paymentOKPanel,BorderLayout.SOUTH);
		//
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
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
		
		int AD_Column_ID = 4917;        //  C_BankStatement.C_BankAccount_ID
		MLookup lookup = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		
		bankAccountField = new VLookup ("C_BankAccount_ID", false,false, true, lookup);
		//bankAccountField.addActionListener(this);
		bankAccountField.setSize(bankAccountField.getWidth()+10, bankAccountField.getHeight()+10);
		
		bPartnerLookup = new VLookup("C_BPartner_ID", false, false, true,
				MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, 3499, DisplayType.Search));
		BPartner_idLabel.setLabelFor(bPartnerLookup);
		
		
		/*Timestamp date = Env.getContextAsDate(Env.getCtx(), m_WindowNo, MBankStatement.COLUMNNAME_StatementDate);
		dateToField.setValue(date);
		dateFromField.setValue(date);*/

		MLookup lookupDocument = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, MColumn.getColumn_ID(MPayment.Table_Name, MPayment.COLUMNNAME_C_DocType_ID), DisplayType.TableDir);
		documentTypeField = new VLookup (MPayment.COLUMNNAME_C_DocType_ID,false,false,true,lookupDocument);
		//documentTypeField.addActionListener(this);
	
		
		
		onlyTemp.addActionListener(this);
		//bankAccount = new MBankAccount(Env.getCtx(), C_BankAccount_ID, null);
	}   //  dynInit
	
	/**************************************************************************
	 *  Action Listener.
	 *  - MultiCurrency
	 *  - Allocate
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		log.config("");
		 if (e.getSource().equals(actionButton))
		{
			log.config("action button");
			saveData();
			if(onlyTemp.isSelected())
			 {
				 loadBankAccountFree();
				 loadBankAccountTemp();
				 Vector<Vector<Object>> data = new Vector<Vector<Object>>();
				 loadTableNOOIS(data);
			 }
			else
			{
				loadBankAccountOK();
				loadBankAccountNO();
			}
			
		}
		 else if ( e.getSource().equals(matchButton) )
		{
			 log.config("matching");
			 if(matching()){
				 if(onlyTemp.isSelected())
				 {
					 loadBankAccountFree();
					 loadBankAccountTemp();
					 Vector<Vector<Object>> data = new Vector<Vector<Object>>();
					 loadTableNOOIS(data);
				 }
				 else
				 {
					 loadBankAccountOK();
					 loadBankAccountNO();
					 loadBankAccountFree();
				 }
			 }
				
		}
		 else if ( e.getSource().equals(tempMatchButton) )
			{
				 log.config("temp matching");
				 if(tempMatching()){
					 loadBankAccountOK();
					 loadBankAccountNO();
					 loadBankAccountFree();
				 }
					
			}
		 else if (e.getSource().equals(searchButton))
		 {
			 log.config("search");
			 loadBankAccountOK();
			 loadBankAccountNO();
			 loadBankAccountFree();
		 }
		 else if ( e.getSource().equals(delMatchButton) )
		 {
			 log.config("delmatch");
			 if(delMatching())
			 {
				 if(onlyTemp.isSelected())
				 {
					 loadBankAccountFree();
					 loadBankAccountTemp();
					 Vector<Vector<Object>> data = new Vector<Vector<Object>>();
					 loadTableNOOIS(data);
				 }
				 else{
				 loadBankAccountFree();
				 loadBankAccountNO();
				 loadBankAccountOK();
				 }
			 }
		 }
		 else if (e.getSource().equals(onlyTemp))
		 {
			 if(onlyTemp.isSelected())
			 {
				 loadBankAccountFree();
				 loadBankAccountTemp();
				 Vector<Vector<Object>> data = new Vector<Vector<Object>>();
				 loadTableNOOIS(data);
				 tempMatchButton.setEnabled(false);
			 }
			 else
			 {
				 loadBankAccountFree();
				 loadBankAccountNO();
				 loadBankAccountOK();
				 tempMatchButton.setEnabled(true);
			 }
		 }
		
	}   //  actionPerformed

	protected void loadBankAccountOK()
	{
		
		loadTableOKOIS(getBankData(true));//matched
		
	}
	
	protected void loadBankAccountTemp()
	{
		
		loadTableOKOIS(getBankDataTemp(true));//matched
		
	}
	
	protected void loadBankAccountNO()
	{
		
		loadTableNOOIS(getBankData(false));//No matched
		
	}
	
	protected void loadBankAccountFree()
	{
		
		loadTableFreeOIS(getPaymentData( bPartnerLookup.getValue(), dateFromField.getValue(), dateToField.getValue(),
				amtFromField.getValue(), amtToField.getValue(),documentTypeField.getValue()));//No matched
		
	}
	
	protected void loadTableFreeOIS (Vector<?> data)
	{
		//  Remove previous listeners
		paymentFreeTable.getModel().removeTableModelListener(this);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, getOISColumnNames2());
		model.addTableModelListener(this);
		paymentFreeTable.setModel(model);
		// 
		
		configureMiniTable2(paymentFreeTable);
	}
	
	protected void loadTableNOOIS (Vector<?> data)
	{
		//  Remove previous listeners
		paymentNoTable.getModel().removeTableModelListener(this);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, getOISColumnNames());
		model.addTableModelListener(this);
		paymentNoTable.setModel(model);
		// 
		
		configureMiniTable(paymentNoTable);
	}
	
	protected void loadTableOKOIS (Vector<?> data)
	{
		//  Remove previous listeners
		paymentOKTable.getModel().removeTableModelListener(this);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, getOISColumnNames());
		model.addTableModelListener(this);
		paymentOKTable.setModel(model);
		// 
		
		configureMiniTable(paymentOKTable);
	}
	
	protected Vector<String> getOISColumnNames()
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>(6);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Msg.translate(Env.getCtx(), "Description"));
		columnNames.add("Folio");
		columnNames.add("Numero");
		columnNames.add(Msg.translate(Env.getCtx(), "Amount"));
		columnNames.add("Movimiento");
		columnNames.add("CodTransaccion");
		columnNames.add(Msg.translate(Env.getCtx(), "C_Payment_ID"));
		columnNames.add(Msg.getElement(Env.getCtx(), "C_BankAccount_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "ID"));
		
		
	    
	    return columnNames;
	}
	
	protected void configureMiniTable (IMiniTable miniTable)
	{
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, Timestamp.class, true);     //  1-TrxDate
		miniTable.setColumnClass(2, String.class, true);        //  2-Description
		miniTable.setColumnClass(3, String.class, true);        //  3-folio
		miniTable.setColumnClass(4, String.class, true);    //  4-serial no
		miniTable.setColumnClass(5, BigDecimal.class, true);    //  5-Amount
		miniTable.setColumnClass(6, String.class, true);    	//  6-tipo movimiento
		miniTable.setColumnClass(7, String.class, true);    	//  7-codtransaction
		miniTable.setColumnClass(8, String.class, true);    	//  8-Payment
		miniTable.setColumnClass(9, String.class, true);    	//  9-bankaccount
		miniTable.setColumnClass(10, Integer.class, true);    	//  10-ID
	
		//  Table UI
		miniTable.autoSize();
	}
	
	
	protected Vector<Vector<Object>> getBankData(  boolean isMatched)
	{
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		if(bankAccountField.getValue()==null)
			return data;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT b.C_BankAccount_id,b.bankaccountno,b.description,b.documentdate,b.folio " );
		sql.append(",b.signo,b.amt,b.c_payment_id,p.documentno,b.C_BankMatch_ID, b.serialno,b.CodTransaction ");
		sql.append( "FROM C_BankMatch b");
		sql.append(" Inner JOIN C_BankAccount a ON (b.C_BankAccount_id=a.C_BankAccount_id) ");
		sql.append( " LEFT OUTER JOIN C_Payment p ON (b.C_Payment_ID=p.C_Payment_ID) ");

		sql.append( getSQLWhere( isMatched));
		
		if(isMatched)
			sql.append(" ORDER BY b.c_payment_id");
		else
			sql.append(" ORDER BY b.amt");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trx.getTrxName());
			setParameters(pstmt,  isMatched);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(6);
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(4));       //  1-DateTrx
				line.add(rs.getString(3));      //  2-description
				line.add(rs.getString(5));      //  3-folio
				line.add(rs.getInt(11)); //4- serial no
				line.add(rs.getBigDecimal(7));      	//  5-monto
				line.add(rs.getString(6).trim());      //  6-tipo movimiento
				line.add(rs.getString(12)); // 7-codtransaction
				KeyNamePair pp = new KeyNamePair(rs.getInt(8), rs.getString(9));
				line.add(pp);                       //  8-payment
				pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				line.add(pp);                       //  9-bankaccount
				line.add(rs.getInt(10)); // 10-ID 
				
				
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return data;
	}
	
	protected Vector<Vector<Object>> getBankDataTemp(  boolean isMatched)
	{
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		if(bankAccountField.getValue()==null)
			return data;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT b.C_BankAccount_id,b.bankaccountno,b.description,b.documentdate,b.folio " );
		sql.append(",b.signo,b.amt,b.c_payment_id,p.documentno,b.C_BankMatch_ID, b.serialno,b.CodTransaction ");
		sql.append( "FROM C_BankMatch b");
		sql.append(" Inner JOIN C_BankAccount a ON (b.C_BankAccount_id=a.C_BankAccount_id) ");
		sql.append( " LEFT OUTER JOIN C_Payment p ON (b.C_Payment_ID=p.C_Payment_ID) ");

		sql.append("WHERE b.Processed='Y' and b.C_Charge_ID is not null" );
		if(bankAccountField.getValue()!=null)
			 sql.append(" AND b.C_BankAccount_ID = ?");
		sql.append(" AND b.IsMatched=? ");
		sql.append(" ORDER BY b.amt");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trx.getTrxName());
			setParameters(pstmt,  isMatched);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(6);
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(4));       //  1-DateTrx
				line.add(rs.getString(3));      //  2-description
				line.add(rs.getString(5));      //  3-folio
				line.add(rs.getInt(11)); //4- serial no
				line.add(rs.getBigDecimal(7));      	//  5-monto
				line.add(rs.getString(6).trim());      //  6-tipo movimiento
				line.add(rs.getString(12)); // 7-codtransaction
				KeyNamePair pp = new KeyNamePair(rs.getInt(8), rs.getString(9));
				line.add(pp);                       //  8-payment
				pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				line.add(pp);                       //  9-bankaccount
				line.add(rs.getInt(10)); // 10-ID 
				
				
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return data;
	}
	
	
	protected Vector<String> getOISColumnNames2()
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>(6);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add("Movimiento");
		columnNames.add(Msg.translate(Env.getCtx(), "C_Payment_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "Amount"));
		columnNames.add(Msg.translate(Env.getCtx(), "Description"));
		columnNames.add("Tipo");
		columnNames.add("Banco");
		
	    
	    return columnNames;
	}
	
	protected void configureMiniTable2 (IMiniTable miniTable)
	{
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, Timestamp.class, true);     //  1-TrxDate
		miniTable.setColumnClass(2, String.class, true);        //  2- tipo movimiento
		miniTable.setColumnClass(3, String.class, true);    	//  3-Payment
		miniTable.setColumnClass(4, BigDecimal.class, true);    //  4-Amount
		miniTable.setColumnClass(5, String.class, true);    //  5- descripcion
		miniTable.setColumnClass(6, String.class, true);        //  6-tender
		miniTable.setColumnClass(7, String.class, true);        //  7-bankaccount
	
		//  Table UI
		miniTable.autoSize();
	}
		
	protected Vector<Vector<Object>> getPaymentData( Object BPartner, Object DateFrom, Object DateTo, 
			Object AmtFrom, Object AmtTo,Object DocType)
	{
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		if(bankAccountField.getValue()==null)
			return data;
		
		String sql = "SELECT p.datetrx,p.c_payment_id,p.documentno,p.C_BankAccount_id,a.accountno,p.payamt,l2.name,p.isreceipt, bp.value ||'-'||bp.digito || '/'||bp.name "
			+ " FROM  C_Payment p " 
			+ " Inner JOIN C_BankAccount a ON (p.C_BankAccount_id=a.C_BankAccount_id) " 
			+ " Inner JOIN C_Bpartner bp  ON (p.C_Bpartner_ID=bp.C_Bpartner_ID) " 
			+ " LEFT OUTER JOIN AD_Ref_List l2 ON (p.TenderType=l2.value and l2.AD_Reference_ID=214) ";

		sql = sql + getSQLWhere( BPartner, DateFrom, DateTo, AmtFrom, AmtTo,DocType) + " ORDER BY p.payamt";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trx.getTrxName());
			setParameters(pstmt, false, BPartner, DateFrom, DateTo, AmtFrom, AmtTo,DocType);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(6);
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(1));       //  1-DateTrx
				
				line.add( rs.getString(8).equals("Y")?"A":"C");//2- tipo movimiento
				KeyNamePair pp = new KeyNamePair(rs.getInt(2), rs.getString(3));
				line.add(pp);                       //  3-C_Payment_ID
				
				line.add(rs.getBigDecimal(6));      //  4-PayAmt
				line.add(rs.getString(9));		//5- descripcion
				line.add(rs.getString(7));      //  6-tender
				pp = new KeyNamePair(rs.getInt(4), rs.getString(5));
				line.add(pp);                       //  7-bankaccount
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return data;
	}
	
	/**
	 *  Set Parameters for Query.
	 *  (as defined in getSQLWhere)
	 *  @param pstmt statement
	 *  @param forCount for counting records
	 *  @throws SQLException
	 */
	void setParameters(PreparedStatement pstmt, boolean forCount, 
			 Object BPartner, Object DateFrom, Object DateTo, 
			Object AmtFrom, Object AmtTo, Object DocType) 
	throws SQLException
	{
		int index = 1;
		
		if(bankAccountField.getValue()!=null)
		pstmt.setInt(index++,(Integer) bankAccountField.getValue());
		
		
		//
		if (BPartner != null)
		{
			Integer bp = (Integer) BPartner;
			pstmt.setInt(index++, bp.intValue());
			log.fine("BPartner=" + bp);
		}
		//
		if (DateFrom != null || DateTo != null)
		{
			Timestamp from = (Timestamp) DateFrom;
			Timestamp to = (Timestamp) DateTo;
			log.fine("Date From=" + from + ", To=" + to);
			if (from == null && to != null)
				pstmt.setTimestamp(index++, to);
			else if (from != null && to == null)
				pstmt.setTimestamp(index++, from);
			else if (from != null && to != null)
			{
				pstmt.setTimestamp(index++, from);
				pstmt.setTimestamp(index++, to);
			}
		}
		//
		if (AmtFrom != null || AmtTo != null)
		{
			BigDecimal from = (BigDecimal) AmtFrom;
			BigDecimal to = (BigDecimal) AmtTo;
			log.fine("Amt From=" + from + ", To=" + to);
			if (from == null && to != null)
				pstmt.setBigDecimal(index++, to);
			else if (from != null && to == null)
				pstmt.setBigDecimal(index++, from);
			else if (from != null && to != null)
			{
				pstmt.setBigDecimal(index++, from);
				pstmt.setBigDecimal(index++, to);
			}
		}
		if(DocType!=null)
			pstmt.setInt(index++, (Integer) DocType);
		
	}   //  setParameters
	
	/**
	 *  Set Parameters for Query.
	 *  (as defined in getSQLWhere)
	 *  @param pstmt statement
	 *  @param forCount for counting records
	 *  @throws SQLException
	 */
	void setParameters(PreparedStatement pstmt,  boolean isMatched) 
	throws SQLException
	{
		int index = 1;
		
		if(bankAccountField.getValue()!=null)
		pstmt.setInt(index++,(Integer) bankAccountField.getValue());
		
		pstmt.setString(index++, isMatched?"Y":"N");

	}   //  setParameters
	
	
	/**
	 *  Get SQL WHERE parameter
	 *  @param f field
	 *  @return Upper case text with % at the end
	 */
	private String getSQLText (String text)
	{
		String s = text.toUpperCase();
		if (!s.endsWith("%"))
			s += "%";
		log.fine( "String=" + s);
		return s;
	}   //  getSQLText
	
	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return sql where clause
	 */
	public String getSQLWhere( boolean isMatched)
	{
		StringBuffer sql = new StringBuffer("WHERE b.Processed='N' " );
		
		if(bankAccountField.getValue()!=null)
			 sql.append(" AND b.C_BankAccount_ID = ?");
		
		
		sql.append(" AND b.IsMatched=?");

		log.fine(sql.toString());
		return sql.toString();
	}	//	getSQLWhere
	
	/**************************************************************************
	 *	Construct SQL Where Clause and define parameters
	 *  (setParameters needs to set parameters)
	 *  Includes first AND
	 *  @return sql where clause
	 */
	public String getSQLWhere( Object BPartner, Object DateFrom, Object DateTo, 
			Object AmtFrom, Object AmtTo,Object DocType)
	{
		StringBuffer sql = new StringBuffer("WHERE p.Processed='Y' AND p.IsReconciled='N' "
		+ " AND p.DocStatus IN ('CO','CL','RE') AND p.PayAmt<>0" );
		
		if(bankAccountField.getValue()!=null)
			 sql.append(" AND p.C_BankAccount_ID = ?");
		
		
		//
		if (BPartner != null)
			sql.append(" AND p.C_BPartner_ID=?");
		//
		if (DateFrom != null || DateTo != null)
		{
			Timestamp from = (Timestamp) DateFrom;
			Timestamp to = (Timestamp) DateTo;
			if (from == null && to != null)
				sql.append(" AND TRUNC(p.DateTrx) <= ?");
			else if (from != null && to == null)
				sql.append(" AND TRUNC(p.DateTrx) >= ?");
			else if (from != null && to != null)
				sql.append(" AND TRUNC(p.DateTrx) BETWEEN ? AND ?");
		}
		//
		if (AmtFrom != null || AmtTo != null)
		{
			BigDecimal from = (BigDecimal) AmtFrom;
			BigDecimal to = (BigDecimal) AmtTo;
			if (from == null && to != null)
				sql.append(" AND p.PayAmt <= ?");
			else if (from != null && to == null)
				sql.append(" AND p.PayAmt >= ?");
			else if (from != null && to != null)
				sql.append(" AND p.PayAmt BETWEEN ? AND ?");
		}
		
		if(DocType!=null)
			sql.append(" AND p.C_DocType_ID=?");
		
		 sql.append(" AND Not Exists (select * from C_BankMatch b where b.C_Payment_ID=p.C_Payment_ID)");
		 
		log.fine(sql.toString());
		return sql.toString();
	}	//	getSQLWhere
	
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
			
			return;
		}
		
	}   //  tableChanged

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
		
		if (value == null)
			return;
	}   //  vetoableChange
	
	
	
	public boolean validate()
	{
		
		/**docs de sistema*/
		int pRows = paymentFreeTable.getRowCount();
		TableModel paymentFree = paymentFreeTable.getModel();
		MPayment pay = null;
		int found=0;
		BigDecimal totalAmtP= new BigDecimal("0.0");
		for (int i = 0; i < pRows; i++)
		{
			if (((Boolean)paymentFree.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)paymentFree.getValueAt(i, 3);   //  Value
				int C_Payment_ID = pp.getKey();
				pay= new MPayment(Env.getCtx(),C_Payment_ID,null);
				totalAmtP = totalAmtP.add(pay.getPayAmt());
				found++;
			}
				
		}
		
		/*if(found>1 || found==0){
			ADialog.error(m_WindowNo, panel, "Error","Debe Seleccionar 1 Pago");
			return false;
		}*/
		
		/**docs importados*/
		pRows = paymentNoTable.getRowCount();
		TableModel paymentNo = paymentNoTable.getModel();
		BigDecimal totalAmt= new BigDecimal("0.0");
		X_C_BankMatch cbank = null;
		int found2=0;
		for (int i = 0; i < pRows; i++)
		{
			if (((Boolean)paymentNo.getValueAt(i, 0)).booleanValue())
			{
				int cbank_ID= (Integer)paymentNo.getValueAt(i, 10);   //  Value
				cbank = new X_C_BankMatch (Env.getCtx(), cbank_ID,null );
				totalAmt = totalAmt.add(cbank.getAmt());
				found2++;
			}
		}
		if(found>1 || found2==0){
			ADialog.error(m_WindowNo, panel, "Error","Debe Seleccionar 1 movimiento");
			return false;
		}
		
		if(totalAmt.longValue()!=totalAmtP.longValue() ){
			ADialog.error(m_WindowNo, panel, "Error","La comparacion de Montos no Cuadra");
			return false;
		}
		
		if((pay.isReceipt() && cbank.getSigno().equals("C") ) || (!pay.isReceipt() && cbank.getSigno().equals("A")) )
		{
			ADialog.error(m_WindowNo, panel, "Error","Los Signos no  Cuadran");
			return false;
		}
			
		
		return true;
	}//validate
	
	public boolean validateTemp()
	{
		
		/**docs de sistema*/
		int pRows = paymentFreeTable.getRowCount();
		TableModel paymentFree = paymentFreeTable.getModel();
		MPayment pay = null;
		int found=0;
		BigDecimal totalAmtP= new BigDecimal("0.0");
		for (int i = 0; i < pRows; i++)
		{
			if (((Boolean)paymentFree.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)paymentFree.getValueAt(i, 3);   //  Value
				int C_Payment_ID = pp.getKey();
				pay= new MPayment(Env.getCtx(),C_Payment_ID,null);
				totalAmtP = totalAmtP.add(pay.getPayAmt());
				found++;
			}
				
		}
		
		/*if(found>1 || found==0){
			ADialog.error(m_WindowNo, panel, "Error","Debe Seleccionar 1 Pago");
			return false;
		}*/
		
		/**docs temporales*/
		pRows = paymentOKTable.getRowCount();
		TableModel paymentNo = paymentOKTable.getModel();
		BigDecimal totalAmt= new BigDecimal("0.0");
		X_C_BankMatch cbank = null;
		int found2=0;
		for (int i = 0; i < pRows; i++)
		{
			if (((Boolean)paymentNo.getValueAt(i, 0)).booleanValue())
			{
				int cbank_ID= (Integer)paymentNo.getValueAt(i, 10);   //  Value
				cbank = new X_C_BankMatch (Env.getCtx(), cbank_ID,null );
				totalAmt = totalAmt.add(cbank.getAmt());
				found2++;
			}
		}
		if(found>1 || found2==0){
			ADialog.error(m_WindowNo, panel, "Error","Debe Seleccionar 1 movimiento");
			return false;
		}
		
		if(totalAmt.longValue()!=totalAmtP.longValue() ){
			ADialog.error(m_WindowNo, panel, "Error","La comparacion de Montos no Cuadra");
			return false;
		}
		
		if((pay.isReceipt() && cbank.getSigno().equals("C") ) || (!pay.isReceipt() && cbank.getSigno().equals("A")) )
		{
			ADialog.error(m_WindowNo, panel, "Error","Los Signos no  Cuadran");
			return false;
		}
			
		
		return true;
	}//validatetemp
	
	public boolean delMatching(){
		
	
		boolean value=false;
		int pRows = paymentOKTable.getRowCount();
		TableModel payment = paymentOKTable.getModel();
		int charge_ID = DB.getSQLValue(null,"SELECT C_Charge_ID FROM C_Charge WHERE upper(TipoCargo)='TC12'  and isactive='Y' and AD_client_ID=" +Env.getAD_Client_ID(Env.getCtx()));
		for (int i = 0; i < pRows; i++)
		{
			if (((Boolean)payment.getValueAt(i, 0)).booleanValue())
			{
				int cbank_ID= (Integer)paymentOKTable.getValueAt(i, 10);   //  Value
				X_C_BankMatch cbank = new X_C_BankMatch (Env.getCtx(), cbank_ID,trx.getTrxName() );
				cbank.setC_Payment_ID(0);
				cbank.set_CustomColumn("C_Charge_ID", null);
				cbank.setIsMatched(false);
				if(onlyTemp.isSelected())
				{
					cbank.set_CustomColumn("C_Charge_ID", charge_ID);
					cbank.setIsMatched(true);
				}
				cbank.save();
				value=true;
			}
		}
		
		trx.commit();
		return value;
	}
	
	public boolean matching(){
	
		if(!onlyTemp.isSelected())
		{
			if(!validate())
				return false;
			
			/**docs de sistema*/
			int pRows = paymentFreeTable.getRowCount();
			TableModel paymentFree = paymentFreeTable.getModel();
			MPayment pay = null;
			for (int i = 0; i < pRows; i++)
			{
				if (((Boolean)paymentFree.getValueAt(i, 0)).booleanValue())
				{
					KeyNamePair pp = (KeyNamePair)paymentFree.getValueAt(i, 3);   //  Value
					int C_Payment_ID = pp.getKey();
					pay= new MPayment(Env.getCtx(),C_Payment_ID,trx.getTrxName());
					break;
				}
			}
			
			/**docs importados*/
			pRows = paymentNoTable.getRowCount();
			TableModel paymentNo = paymentNoTable.getModel();
			for (int i = 0; i < pRows; i++)
			{
				if (((Boolean)paymentNo.getValueAt(i, 0)).booleanValue())
				{
					int cbank_ID= (Integer)paymentNo.getValueAt(i, 10);   //  Value
					X_C_BankMatch cbank = new X_C_BankMatch (Env.getCtx(), cbank_ID,trx.getTrxName() );
					cbank.setC_Payment_ID(pay.getC_Payment_ID());
					cbank.setIsMatched(true);
					cbank.save();
				}
			}
		}
		else //reasignacion de temporales
		{
			if(!validateTemp())
				return false;
			
			/**docs de sistema*/
			int pRows = paymentFreeTable.getRowCount();
			TableModel paymentFree = paymentFreeTable.getModel();
			MPayment pay = null;
			for (int i = 0; i < pRows; i++)
			{
				if (((Boolean)paymentFree.getValueAt(i, 0)).booleanValue())
				{
					KeyNamePair pp = (KeyNamePair)paymentFree.getValueAt(i, 3);   //  Value
					int C_Payment_ID = pp.getKey();
					pay= new MPayment(Env.getCtx(),C_Payment_ID,trx.getTrxName());
					break;
				}
			}
			
			/**docs temporales*/
			pRows = paymentOKTable.getRowCount();
			TableModel paymentNo = paymentOKTable.getModel();
			for (int i = 0; i < pRows; i++)
			{
				if (((Boolean)paymentNo.getValueAt(i, 0)).booleanValue())
				{
					int cbank_ID= (Integer)paymentNo.getValueAt(i, 10);   //  Value
					X_C_BankMatch cbank = new X_C_BankMatch (Env.getCtx(), cbank_ID,trx.getTrxName() );
					cbank.setC_Payment_ID(pay.getC_Payment_ID());
					cbank.setIsMatched(true);
					cbank.save();
				}
			}
		}
		
		trx.commit();
		return true;
	}
	
	public boolean tempMatching(){
		
			
		/**docs de sistema*/
		int pRows = paymentFreeTable.getRowCount();
		TableModel paymentFree = paymentFreeTable.getModel();
		int count=0;
		for (int i = 0; i < pRows; i++)
		{
			if (((Boolean)paymentFree.getValueAt(i, 0)).booleanValue())
			{
				count++;
			}
		}
		
		if(count>0)
		{
			ADialog.error(m_WindowNo, panel, "Error","Para Asinacion Temporal solo debe seleccionar movimientos Importados");
			return false;
		}
		
		/**docs importados*/
		pRows = paymentNoTable.getRowCount();
		TableModel paymentNo = paymentNoTable.getModel();
		int charge_ID = DB.getSQLValue(null,"SELECT C_Charge_ID FROM C_Charge WHERE upper(TipoCargo)='TC12'  and isactive='Y' and AD_client_ID=" +Env.getAD_Client_ID(Env.getCtx()));
		if(charge_ID<=0)
		{
			ADialog.error(m_WindowNo, panel, "Error","Debe configurar un cargo para las asignaciones temporales");
			return false;
		}
		for (int i = 0; i < pRows; i++)
		{
			if (((Boolean)paymentNo.getValueAt(i, 0)).booleanValue())
			{
				int cbank_ID= (Integer)paymentNo.getValueAt(i, 10);   //  Value
				X_C_BankMatch cbank = new X_C_BankMatch (Env.getCtx(), cbank_ID,trx.getTrxName() );
				//cbank.setC_Payment_ID(pay.getC_Payment_ID());
				cbank.set_CustomColumn("C_Charge_ID", charge_ID);
				cbank.setIsMatched(true);
				cbank.save();
			}
		}
		
		trx.commit();
		return true;
	}
	
	/**************************************************************************
	 *  Save Data
	 */
	public void saveData()
	{
		
		int pRows = paymentOKTable.getRowCount();
		TableModel paymentOK = paymentOKTable.getModel();
		MBankStatement bank = null;
		int lastPayment_ID=0;
		int lastBankStatementLine_ID=0;
		for (int i = 0; i < pRows; i++)
		{
			
				int cbank_ID= (Integer)paymentOK.getValueAt(i, 10);   //  Value
				X_C_BankMatch cbank = new X_C_BankMatch (Env.getCtx(), cbank_ID,trx.getTrxName() );
				
				if(!onlyTemp.isSelected())//genera deposito
				{
					if(bank==null){
						bank = new MBankStatement(Env.getCtx(), 0,trx.getTrxName() );
						bank.setC_BankAccount_ID(cbank.getC_BankAccount_ID());
						bank.setAD_Org_ID(MBankAccount.get(Env.getCtx(), cbank.getC_BankAccount_ID()).getAD_Org_ID());
						bank.setName("Generado Automaticamente");
						//ininoles estado de cuentas que tenga la fecha indicada en el logeo
						bank.setStatementDate(Env.getContextAsDate(Env.getCtx(), "#Date"));
						try{
						bank.saveEx();
						}
						catch (AdempiereException e)
						{
							ADialog.error(m_WindowNo, panel, "Error",e.getMessage());
							return;
						}
						
					}
					
					if(cbank.getC_Payment_ID()>0){// normal
						if(lastPayment_ID!=cbank.getC_Payment_ID()){
							
							lastPayment_ID=cbank.getC_Payment_ID();
						
							MBankStatementLine line = new MBankStatementLine(bank);
							if(cbank.getC_Payment_ID()>0)
							line.setPayment(new MPayment(Env.getCtx(),cbank.getC_Payment_ID(),trx.getTrxName()));
							line.saveEx();
							lastBankStatementLine_ID = line.getC_BankStatementLine_ID();
							
						}
						cbank.set_CustomColumn("C_BankStatementLine_ID", lastBankStatementLine_ID);
						cbank.setC_BankStatement_ID(bank.getC_BankStatement_ID());
						cbank.setProcessed(true);
						cbank.saveEx();
					}
					else if (cbank.get_ValueAsInt("C_Charge_ID")>0)//Temporal sin pago
					{
						MBankStatementLine line = new MBankStatementLine(bank);
						line.setC_Charge_ID( cbank.get_ValueAsInt("C_Charge_ID"));
						line.setStatementLineDate(cbank.getStatementDate());
						if(cbank.getSigno().trim().toUpperCase().equals("A"))
						{
							line.setStmtAmt(cbank.getAmt());
							line.setChargeAmt(cbank.getAmt());
							line.setTrxAmt(cbank.getAmt());
						}
						else
						{
							line.setStmtAmt(cbank.getAmt().negate());
							line.setChargeAmt(cbank.getAmt().negate());
							line.setTrxAmt(cbank.getAmt().negate());
						}
						line.setC_Currency_ID(bank.getC_BankAccount().getC_Currency_ID());
						line.saveEx();
						lastBankStatementLine_ID = line.getC_BankStatementLine_ID();
						
						cbank.set_CustomColumn("C_BankStatementLine_ID", lastBankStatementLine_ID);
						cbank.setC_BankStatement_ID(bank.getC_BankStatement_ID());
						cbank.setProcessed(true);
						cbank.saveEx();
					}
				}
				else// no genera deposito, actualiza 
				{
					if(cbank.getC_Payment_ID()>0)
					{
						DB.executeUpdate("Update C_BankStatementLine set C_Charge_ID=null, ChargeAmt=0, C_Payment_ID="+cbank.getC_Payment_ID()+" where C_BankStatementLine_ID="+cbank.get_ValueAsInt("C_BankStatementLine_ID"), trx.getTrxName());
						cbank.set_CustomColumn("C_Charge_ID", null);
						cbank.saveEx();
						
						MFactAcct.deleteEx(MBankStatement.Table_ID, (new MBankStatementLine (Env.getCtx(), cbank.get_ValueAsInt("C_BankStatementLine_ID"),trx.getTrxName()) ).getC_BankStatement_ID(), trx.getTrxName());
						
						trx.commit();
					}
				
				}
			
		}//end for
		
		if(bank!=null)
		{
			bank.processIt("CO");
			bank.saveEx();
			trx.commit();
		}
		
	}   //  saveData
	
	
}