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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.adempiere.plaf.AdempierePLAF;
import org.compiere.apps.StatusBar;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MColumn;
import org.compiere.model.MDocType;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MQuery;
import org.compiere.model.PrintInfo;
import org.compiere.model.X_MP_OT;
import org.compiere.plaf.CompiereColor;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportCtl;
import org.compiere.print.ReportEngine;
import org.compiere.print.ReportViewerProvider;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 * Programacion de Ots 
 *
 * @author  Fabian Aguilar
 * @version $Id: VMPProgrammingOT.java,v 1.2 2010/03/10 00:51:28$
 * 
 * Contributor : Fabian Aguilar - OFBConsulting - programming OT
 */
public class VMPProgrammingOT extends CPanel
	implements FormPanel, ActionListener, TableModelListener, VetoableChangeListener, ASyncProcess
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
	private static CLogger log = CLogger.getCLogger(VMPProgrammingOT.class);

	
	private boolean     m_calculating = false;
	private int         m_C_Currency_ID = 0;
	
	//
	private CPanel mainPanel = new CPanel();
	private CPanel parameterPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel allocationPanel = new CPanel();
	private MiniTable otTable = new MiniTable();
	private CPanel infoPanel = new CPanel();
	private int         m_A_Asset_ID = 0;
	private int         m_AD_User_ID = 0;

	
	private CPanel otPanel = new CPanel();
	private JLabel otLabel = new JLabel();
	private BorderLayout otLayout = new BorderLayout();
	private JLabel otInfo = new JLabel();
	private JScrollPane otScrollPane = new JScrollPane();
	private GridBagLayout allocationLayout = new GridBagLayout();
	private JLabel differenceLabel = new JLabel();
	private CTextField differenceField = new CTextField();
	private JButton ProcessButton = new JButton();
	private JButton searchButton = new JButton();
	private JButton setButton = new JButton();
	private JButton EnableButton = new JButton();
	private JButton ChangeButton = new JButton();
	private JLabel assetLabel = new JLabel();
	private JCheckBox selectall = new JCheckBox();
	private JLabel allocCurrencyLabel = new JLabel();
	private StatusBar statusBar = new StatusBar();
	private JLabel dateLabel = new JLabel();
	private VDate dateField = new VDate();
	private VLookup assetSearch = null;
	private VLookup userSearch = null;




	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(this);
		//
		mainPanel.setLayout(mainLayout);
		
		
		dateLabel.setText(Msg.getMsg(Env.getCtx(), "Date"));
		dateLabel.setToolTipText(Msg.getMsg(Env.getCtx(), "AllocDate", false));
		
		//
		parameterPanel.setLayout(parameterLayout);
		allocationPanel.setLayout(allocationLayout);
		otLabel.setRequestFocusEnabled(false);
		otLabel.setText("OT List");

		otPanel.setLayout(otLayout);
		differenceLabel.setText(Msg.getMsg(Env.getCtx(), "Difference"));
		differenceField.setBackground(AdempierePLAF.getFieldBackground_Inactive());
		differenceField.setEditable(false);
		differenceField.setText("0");
		differenceField.setColumns(8);
		differenceField.setHorizontalAlignment(SwingConstants.RIGHT);
		ProcessButton.setText(Msg.getMsg(Env.getCtx(), "Print"));
		ProcessButton.addActionListener(this);
		searchButton.setText(Msg.getMsg(Env.getCtx(), "Search"));
		searchButton.addActionListener(this);
		setButton.setText(Msg.getMsg(Env.getCtx(),"set User"));
		setButton.addActionListener(this);
		EnableButton.setText(Msg.getMsg(Env.getCtx(), "Enable Editing"));
		EnableButton.addActionListener(this);
		ChangeButton.setText(Msg.getMsg(Env.getCtx(), "set Date"));
		ChangeButton.addActionListener(this);
		
		
		assetLabel.setText("Usuario");
		selectall.setText(Msg.getMsg(Env.getCtx(), "Seleccionar Todo"));
		selectall.addActionListener(this);
		allocCurrencyLabel.setText(".");
		otScrollPane.setPreferredSize(new Dimension(300, 300));
		
		
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
//		  parameterPanel.add(EnableButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
//				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
			parameterPanel.add(dateLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(dateField, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
			parameterPanel.add(ChangeButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(selectall, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(assetLabel, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(userSearch, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(setButton, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
		mainPanel.add(allocationPanel, BorderLayout.SOUTH);
//		allocationPanel.add(searchButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
//			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
//		allocationPanel.add(addButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
//			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		allocationPanel.add(selectall, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		allocationPanel.add(ProcessButton, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		
		otPanel.add(otLabel, BorderLayout.NORTH);
		otPanel.add(otInfo, BorderLayout.SOUTH);
		otPanel.add(otScrollPane, BorderLayout.CENTER);
		otScrollPane.getViewport().add(otTable, null);
		
		//
		mainPanel.add(infoPanel, BorderLayout.CENTER);
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(otPanel,BorderLayout.CENTER);
		infoPanel.setPreferredSize(new Dimension(1000,450));

		
		
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
		
	//  Asset
	/*	int AD_Column_ID = MColumn.getColumn_ID("MP_Maintain","A_Asset_ID");        //  MP_Maintain.A_Asset_ID
		MLookup lookupBP = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		assetSearch = new VLookup("A_Asset_ID", true, false, true, lookupBP);
		assetSearch.addVetoableChangeListener(this);*/
	
    //	user
		int AD_Column_ID= MColumn.getColumn_ID(X_MP_OT.Table_Name,"AD_User_ID");
		MLookup lookupUs = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		userSearch = new VLookup("AD_User_ID", true, false, true, lookupUs);
		userSearch.addVetoableChangeListener(this);
		
	//  Date set to Login Date
		dateField.setValue(Env.getContextAsDate(Env.getCtx(), "#Date"));
		//  Translation
		statusBar.setStatusLine("Seleccione la mantencion, cambie la fecha pronosticada o genere directamente la OT ");
		statusBar.setStatusDB("");
		loadMPs();
		

	}   //  dynInit

	/**
	 *  Load Business Partner Info
	 *  - Payments
	 *  - Invoices
	 */
	private void loadMPs ()
	{		
		//log.config("Instance_ID=" +Instance_ID);
		//  Need to have both values
		

		
		
		/********************************
		 *  Load ot table
		 *      1-TrxDate, 2-DocumentNo, (3-Currency, 4-PayAmt,)
		 *      5-ConvAmt, 6-ConvOpen, 7-Allocated
		 */
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuffer sql = new StringBuffer("select o.DATETRX,doc.name,o.DOCUMENTNO,o.AD_USER_ID,o.DESCRIPTION, o.MP_OT_ID, u.name, o.A_Asset_ID, a.name as AssetName"+
				" From MP_OT o " +
				" inner join A_Asset a on (o.A_Asset_ID=a.A_Asset_ID)" +
				" inner join C_DocType doc on (o.C_DocType_ID=doc.C_DocType_ID)" +
				" Left Outer Join AD_User u on(o.AD_User_ID=u.AD_User_ID)" +
				" where o.DOCSTATUS='DR' and not exists " +
				" (select * from MP_OT_TASK t where t.STATUS<>'NS' and t.MP_OT_ID=o.MP_OT_ID)");
		
		sql.append(" and o.AD_Client_ID="+ Env.getAD_Client_ID(Env.getCtx()));
		
		log.config("ot=" + sql.toString());
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			//pstmt.setInt(1, Instance_ID);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(1));       //  1-fecha programada
				line.add(rs.getString(2)); // 2-tipo OT
				KeyNamePair pp = new KeyNamePair(rs.getInt(6), rs.getString(3));
				line.add(pp); //  3-numero documento
				KeyNamePair pp3 = new KeyNamePair(rs.getInt(8), rs.getString(9));
				line.add(pp3);      //  4-asset
				KeyNamePair pp2 = new KeyNamePair(rs.getInt(4), rs.getString(7));
				line.add(pp2);  //  5-user
				line.add(rs.getString(5)); 				//  6-description
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
		otTable.getModel().removeTableModelListener(this);
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("Seleccionar");
		columnNames.add("Fecha Programada");
		columnNames.add("Tipo OT");
		columnNames.add("Documento");
		columnNames.add("Activo");
		columnNames.add("Usuario");
		columnNames.add("Descripcion");
		

		
		//  Set Model
		DefaultTableModel modelP = new DefaultTableModel(data, columnNames);
		modelP.addTableModelListener(this);
		otTable.setModel(modelP);
		//
		int i = 0;
		otTable.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		otTable.setColumnClass(i++, Timestamp.class, false);         //  1-fecha programada
		otTable.setColumnClass(i++, String.class, true);			//2-tipo ot
		otTable.setColumnClass(i++, String.class, true);           //  3-numero documento
		otTable.setColumnClass(i++, String.class, true);       //  4-asset
		otTable.setColumnClass(i++, String.class, true);       //  5-user
		otTable.setColumnClass(i++, String.class, true);      //  6-description

		//  Table UI
		otTable.autoSize();
		
		otTable.setColumnReadOnly(1, false);


		
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
		if (e.getSource().equals(ProcessButton))
		{
			//ProcessButton.setEnabled(false);
			saveData();

			
		}
		else if (e.getSource().equals(ChangeButton)){
			ProcessButton.setEnabled(true);
			//updating rows selected
			TableModel ot = otTable.getModel();
			int rows = ot.getRowCount();
			Timestamp DateTrx = (Timestamp)dateField.getValue();
			for (int i = 0; i < rows; i++)
			{
				if (((Boolean)ot.getValueAt(i, 0)).booleanValue())
				{
					KeyNamePair pp = (KeyNamePair)ot.getValueAt(i, 3);//DocumentNo
					int OT_ID=pp.getKey();
					X_MP_OT currentOT=new X_MP_OT(Env.getCtx(), OT_ID,null);
					currentOT.setDateTrx(DateTrx);
					currentOT.save();	
					
				}
			}
			loadMPs ();	
		}
		else if (e.getSource().equals(setButton)){
			TableModel ot = otTable.getModel();
			int rows = ot.getRowCount();
			if(m_AD_User_ID!=0){
				for (int i = 0; i < rows; i++)
				{
					if (((Boolean)ot.getValueAt(i, 0)).booleanValue())
					{
						KeyNamePair pp = (KeyNamePair)ot.getValueAt(i, 3);//DocumentNo
						int OT_ID=pp.getKey();
						X_MP_OT currentOT=new X_MP_OT(Env.getCtx(), OT_ID,null);
						currentOT.setAD_User_ID(m_AD_User_ID);
						currentOT.save();
						
					}
				}
				loadMPs ();	
			}
		}
		else if (e.getSource().equals(selectall)){
			TableModel ot = otTable.getModel();
			int rows = ot.getRowCount();
			for (int i = 0; i < rows; i++)
			{		
				if(m_A_Asset_ID==0)
					ot.setValueAt(selectall.isSelected(), i, 0);
				else{
					KeyNamePair pp = (KeyNamePair)ot.getValueAt(i, 6);//Asset
					int Asset_ID=pp.getKey();
					if(Asset_ID==m_A_Asset_ID)
						ot.setValueAt(selectall.isSelected(), i, 0);
				}
				
			}
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
	

		/**
		 *  Setting defaults
		 */
		if (m_calculating)  //  Avoid recursive calls
			return;
		m_calculating = true;
		

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
		if (name.equals("A_Asset_ID"))
		{
			assetSearch.setValue(value);
			m_A_Asset_ID = ((Integer)value).intValue();
			
		}else if (name.equals("AD_User_ID")){
			userSearch.setValue(value);
			m_AD_User_ID=((Integer)value).intValue();
		}
		
	}   //  vetoableChange

	
	/**************************************************************************
	 *  Save Data
	 */
	private void saveData()
	{
		
		TableModel ot = otTable.getModel();
		int rows = ot.getRowCount();
		//Timestamp DateTrx = (Timestamp)dateField.getValue();
		//Trx trx = Trx.get(Trx.createTrxName("AL"), true);
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)ot.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)ot.getValueAt(i, 3);//OT
				int OT_ID=pp.getKey();
				try{
					X_MP_OT currentOT=new X_MP_OT(Env.getCtx(), OT_ID,null);
					MDocType doc=new MDocType(Env.getCtx(), currentOT.getC_DocType_ID(),null);
					int AD_PrintFormat_ID = doc.getAD_PrintFormat_ID();
					int C_BPartner_ID = 0;
					String DocumentNo = currentOT.getDocumentNo();
					int copies = 1;
					MPrintFormat format = MPrintFormat.get (Env.getCtx(), AD_PrintFormat_ID, false);
					MQuery query = new MQuery("MP_OT");
					query.addRestriction("MP_OT_ID", MQuery.EQUAL, new Integer(OT_ID));
					PrintInfo info = new PrintInfo(
							DocumentNo,
							X_MP_OT.Table_ID,
							OT_ID,
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
		loadMPs ();
	}   //  saveData
	
	/**************************************************************************
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.setEnabled(false);
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi result of execute ASync call
	 */
	public void unlockUI (ProcessInfo pi)
	{
		this.setEnabled(true);
		this.setCursor(Cursor.getDefaultCursor());
		
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return this.isEnabled();
	}   //  isUILocked

	/**
	 *  Method to be executed async.
	 *  Called from the Worker
	 *  @param pi ProcessInfo
	 */
	public void executeASync (ProcessInfo pi)
	{
	}   //  executeASync

	
	

}   //  VAllocation
