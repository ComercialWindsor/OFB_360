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
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
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
import org.compiere.model.X_MP_MaintainDetail;
import org.compiere.model.X_MP_OT;
import org.compiere.model.X_MP_OT_Resource;
import org.compiere.model.X_MP_OT_Task;
import org.compiere.plaf.CompiereColor;
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
import org.compiere.util.Util;
import org.ofb.model.OFBForward;

/**
 * Pronostico Mantencion 
 *
 * @author  Fabian Aguilar
 * @version $Id: VMPGenerateOT.java,v 1.2 2010/03/10 00:51:28$
 * 
 * Contributor : Fabian Aguilar - OFBConsulting - generate OT
 */
public class VMPGenerateOTTSM extends CPanel
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
	private static CLogger log = CLogger.getCLogger(VMPGenerateOTTSM.class);

	
	private boolean     m_calculating = false;
	private int         m_C_Currency_ID = 0;
	
	//
	private CPanel mainPanel = new CPanel();
	private CPanel parameterPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel allocationPanel = new CPanel();
	private MiniTable selectedTable = new MiniTable();
	private MiniTable prognosisTable = new MiniTable();
	private CPanel infoPanel = new CPanel();
	private int         m_A_Asset_ID = 0;

	
	private CPanel prognosisPanel = new CPanel();
	private CPanel selectedPanel = new CPanel();
	private JLabel prognosisLabel = new JLabel();
	private JLabel selectedLabel = new JLabel();
	private BorderLayout prognosisLayout = new BorderLayout();
	private BorderLayout selectedLayout = new BorderLayout();
	private JLabel prognosisInfo = new JLabel();
	//private JLabel invoiceInfo = new JLabel();
	private JScrollPane prognosisScrollPane = new JScrollPane();
	//private JScrollPane invoiceScrollPane = new JScrollPane();
	private GridBagLayout allocationLayout = new GridBagLayout();
	private JLabel differenceLabel = new JLabel();
	private CTextField differenceField = new CTextField();
	private JButton ProcessButton = new JButton();
	private JButton searchButton = new JButton();
	private JButton addButton = new JButton();
	private JButton EnableButton = new JButton();
	private JButton ChangeButton = new JButton();
	private JLabel assetLabel = new JLabel();
	private JCheckBox selectall = new JCheckBox();
	private JLabel allocCurrencyLabel = new JLabel();
	private StatusBar statusBar = new StatusBar();
	private JLabel dateLabel = new JLabel();
	private VDate dateField = new VDate();
	private VLookup assetSearch = null;




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
		prognosisLabel.setRequestFocusEnabled(false);
		prognosisLabel.setText("Mantenciones Pronosticadas");
		selectedLabel.setRequestFocusEnabled(false);
		selectedLabel.setText("seleccionadas");
		prognosisPanel.setLayout(prognosisLayout);
		selectedPanel.setLayout(selectedLayout);
		/*invoiceInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		invoiceInfo.setHorizontalTextPosition(SwingConstants.RIGHT);
		invoiceInfo.setText(".");*/
		prognosisInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		prognosisInfo.setHorizontalTextPosition(SwingConstants.RIGHT);
		prognosisInfo.setText(".");
		differenceLabel.setText(Msg.getMsg(Env.getCtx(), "Difference"));
		differenceField.setBackground(AdempierePLAF.getFieldBackground_Inactive());
		differenceField.setEditable(false);
		differenceField.setText("0");
		differenceField.setColumns(8);
		differenceField.setHorizontalAlignment(SwingConstants.RIGHT);
		ProcessButton.setText("Generar OT");
		ProcessButton.addActionListener(this);
		searchButton.setText("Buscar");
		searchButton.addActionListener(this);
		addButton.setText("Agregar Seleccion");
		addButton.addActionListener(this);
		EnableButton.setText("Habilitar Edicion");
		EnableButton.addActionListener(this);
		ChangeButton.setText("Aplicar Fecha");
		ChangeButton.addActionListener(this);
		
		assetLabel.setText(Msg.translate(Env.getCtx(), "A_Asset_ID"));
		selectall.setText(Msg.getMsg(Env.getCtx(), "Seleccionar Todo"));
		selectall.addActionListener(this);
		allocCurrencyLabel.setText(".");
		//invoiceScrollPane.setPreferredSize(new Dimension(300, 300));
		prognosisScrollPane.setPreferredSize(new Dimension(300, 300));
		
		
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
			parameterPanel.add(assetSearch, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
		mainPanel.add(allocationPanel, BorderLayout.SOUTH);
//		allocationPanel.add(searchButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
//			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
//		allocationPanel.add(addButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
//			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
//		allocationPanel.add(removeButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
//				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		allocationPanel.add(ProcessButton, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		
		prognosisPanel.add(prognosisLabel, BorderLayout.NORTH);
		prognosisPanel.add(prognosisInfo, BorderLayout.SOUTH);
		prognosisPanel.add(prognosisScrollPane, BorderLayout.CENTER);
		prognosisScrollPane.getViewport().add(prognosisTable, null);
		/*selectedPanel.add(selectedLabel, BorderLayout.NORTH);
		selectedPanel.add(invoiceInfo, BorderLayout.SOUTH);
		selectedPanel.add(invoiceScrollPane, BorderLayout.CENTER);
		invoiceScrollPane.getViewport().add(selectedTable, null);*/
		//
		mainPanel.add(infoPanel, BorderLayout.CENTER);
		
		
		infoPanel.setLayout(new BorderLayout());
		

		infoPanel.add(prognosisPanel,BorderLayout.CENTER);
		infoPanel.setPreferredSize(new Dimension(1000,450));
//		infoPanel.setDividerLocation(210);
		
		
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
		int AD_Column_ID = MColumn.getColumn_ID("MP_Maintain","A_Asset_ID");        //  MP_Maintain.A_Asset_ID
		MLookup lookupBP = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		assetSearch = new VLookup("A_Asset_ID", true, false, true, lookupBP);
		assetSearch.addVetoableChangeListener(this);
	
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
		 *  Load Prognosis table
		 *      1-TrxDate, 2-DocumentNo, (3-Currency, 4-PayAmt,)
		 *      5-ConvAmt, 6-ConvOpen, 7-Allocated
		 */
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuffer sql = new StringBuffer("select p.AD_PINSTANCE_ID,p.AD_CLIENT_ID,p.AD_ORG_ID," +
				"p.A_ASSET_ID,p.MP_MAINTAINDetail_ID,p.DESCRIPTION,p.PROGRAMMINGTYPE,p.CICLO,p.DATETRX," +
				"mp.Description as MP_name,a.value||'-'||a.name as assetname  " +
				" from MP_Prognosis p" +
				" Inner Join MP_MainTainDetail mp on (p.MP_MAINTAINDetail_ID=mp.MP_MAINTAINDetail_ID)"+
				" Inner Join A_Asset a on (p.A_ASSET_ID=a.A_ASSET_ID)"+
				" where p.Processed='N' and p.Selected='N' " +  (m_A_Asset_ID>0?" and p.A_Asset_ID = "+m_A_Asset_ID:" and p.A_Asset_ID = 0")+
				" order by p.DATETRX asc");
		
		log.config("Prognosis=" + sql.toString());
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			//pstmt.setInt(1, Instance_ID);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
				//line.add(new Boolean(false));       //  1-new date
				line.add(rs.getInt(8));       //  1-Ciclo
				line.add(rs.getTimestamp(9)); //  2-Fecha
				KeyNamePair pp = new KeyNamePair(rs.getInt(4), rs.getString(11));
				line.add(pp);      //  3-Activo
				KeyNamePair pp2 = new KeyNamePair(rs.getInt(5), rs.getString("MP_name"));
				line.add(pp2);  //  4-mp
				line.add(rs.getString(7));      //  5-programing type
				line.add(rs.getString(6)); 				//  6-description
				
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
		prognosisTable.getModel().removeTableModelListener(this);
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("Generar OT");
		//columnNames.add("Aplicar Nueva Fecha");
		columnNames.add(Msg.getMsg(Env.getCtx(), "Ciclo"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Util.cleanAmp(Msg.translate(Env.getCtx(), "Asset")));
		columnNames.add(Msg.getMsg(Env.getCtx(), "MP"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "Programing Type"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "Description"));

		
		//  Set Model
		DefaultTableModel modelP = new DefaultTableModel(data, columnNames);
		modelP.addTableModelListener(this);
		prognosisTable.setModel(modelP);
		//
		int i = 0;
		prognosisTable.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		//prognosisTable.setColumnClass(i++, Boolean.class, false);         //  1-new date
		prognosisTable.setColumnClass(i++, Integer.class, true);           //  1-ciclo
		prognosisTable.setColumnClass(i++, Timestamp.class, true);        //  2-TrxDate
		prognosisTable.setColumnClass(i++, String.class, true);       //  3-asset
		prognosisTable.setColumnClass(i++, String.class, true);       //  4-MP
		prognosisTable.setColumnClass(i++, String.class, true);      //  5-programing type
		prognosisTable.setColumnClass(i++, String.class, true);      //  6-Description


		
		

		//  Table UI
		prognosisTable.autoSize();
		
		prognosisTable.setColumnReadOnly(1, false);


		
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
		else if (e.getSource().equals(EnableButton))
		{
			prognosisTable.setColumnReadOnly(1, false);
			prognosisTable.setColumnReadOnly(2, false);
			prognosisTable.setColumnReadOnly(3, false);
			/*Object Period = JOptionPane.showInputDialog(
					   this,
					   "Seleccione Periodo",
					   "Selector de opciones",
					   JOptionPane.QUESTION_MESSAGE,
					   null,  // null para icono defecto
					   new Object[] { "1", "2", "3", "4", "5", "6", "7", "8" }, 
					   "1");

			
			String trxName = Trx.createTrxName("IOG");	
			Trx trx = Trx.get(trxName, true);
			AD_Process_ID = DB.getSQLValue(trxName, "select AD_Process_ID from AD_Process where upper(procedurename)='MP_PROGNOSIS_PROCESS'");
			if(instance==null){
				instance = new MPInstance(Env.getCtx(), AD_Process_ID, 0);
				if (!instance.save())
				{
					log.severe("ProcessNoInstance");
					
					return;
				}
			}
			
			//call process
			ProcessInfo pi = new ProcessInfo ("VMPGenerateOT", AD_Process_ID);
			pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());
//			Add Parameter - Selection=Y
			MPInstancePara ip = new MPInstancePara(instance, 10);
			ip.setParameter("PeriodNo",Integer.parseInt((String)Period));
			if (!ip.save())
			{
				String msg = "No Parameter added";  //  not translated
				log.log(Level.SEVERE, msg);
				return;
			}
			
//			Execute Process
			ProcessCtl worker = new ProcessCtl(this, Env.getWindowNo(this), pi, trx);
			worker.start(); 
			
			loadMPs(instance.getAD_PInstance_ID());*/
		}
		/*else if (e.getSource().equals(addButton)){
			ProcessButton.setEnabled(true);
			//updating rows selected
			TableModel prognosis = prognosisTable.getModel();
			int rows = prognosis.getRowCount();
			for (int i = 0; i < rows; i++)
			{
				if (((Boolean)prognosis.getValueAt(i, 0)).booleanValue())
				{
					KeyNamePair pp = (KeyNamePair)prognosis.getValueAt(i, 3);   //  Asset
					int Asset_ID=pp.getKey();
					pp = (KeyNamePair)prognosis.getValueAt(i, 4);   //  MP
					int MP_ID=pp.getKey();
					
					int ciclo=((Integer)prognosis.getValueAt(i, 1)).intValue();   //  Ciclo
					String ProgramingType=(String)prognosis.getValueAt(i, 5);   //  Programing
					
					String sql="Update MP_Prognosis set selected='Y' where ciclo="+ciclo
					+ " and MP_MAINTAIN_ID="+MP_ID+" and A_Asset_ID="+Asset_ID
					+ " and PROGRAMMINGTYPE='"+ ProgramingType +"'";  
					
					DB.executeUpdate(sql);
					
				}
			}

			
			//load rows
			loadMPs(instance.getAD_PInstance_ID());
		}*/
		else if (e.getSource().equals(ChangeButton)){
			ProcessButton.setEnabled(true);
			//updating rows selected
			TableModel prognosis = prognosisTable.getModel();
			int rows = prognosis.getRowCount();
			Timestamp DateTrx = (Timestamp)dateField.getValue();
			for (int i = 0; i < rows; i++)
			{
				if (((Boolean)prognosis.getValueAt(i, 0)).booleanValue())
				{
					
					prognosis.setValueAt(DateTrx, i, 2);
					
				}
			}
		}
		else if (e.getSource().equals(selectall)){
			TableModel prognosis = prognosisTable.getModel();
			int rows = prognosis.getRowCount();
			for (int i = 0; i < rows; i++)
			{		
				if(m_A_Asset_ID==0)
					prognosis.setValueAt(selectall.isSelected(), i, 0);
				else{
					KeyNamePair pp = (KeyNamePair)prognosis.getValueAt(i, 3);//Asset
					int Asset_ID=pp.getKey();
					if(Asset_ID==m_A_Asset_ID)
						prognosis.setValueAt(selectall.isSelected(), i, 0);
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
		DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);
		Timestamp allocDate = null;					
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
			loadMPs ();
		}
		
	}   //  vetoableChange

	
	/**************************************************************************
	 *  Save Data
	 */
	private void saveData()
	{
		
		TableModel prognosis = prognosisTable.getModel();
		int rows = prognosis.getRowCount();
		
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)prognosis.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)prognosis.getValueAt(i, 4);//MP
				int MPD_ID=pp.getKey();
				pp = (KeyNamePair)prognosis.getValueAt(i, 3);//Asset
				int Asset_ID=pp.getKey();
				Timestamp Datetrx =(Timestamp)prognosis.getValueAt(i, 2);//Date
				String description =(String)prognosis.getValueAt(i, 6);//Description
				int ciclo=((Integer)prognosis.getValueAt(i, 1)).intValue();   //  Ciclo
				String ProgramingType=(String)prognosis.getValueAt(i, 5);   //  Programing
				
				
				if(!createOT(MPD_ID,Datetrx,description,Asset_ID)){
						log.log(Level.SEVERE, "OT not created #" + i);
						continue;
				}
				
				//updating  MP
				updateMPD(MPD_ID,Datetrx,description);
				
				String sql="Update MP_Prognosis set Processed='Y' where ciclo="+ciclo
				+ " and MP_MAINTAINDetail_ID="+MPD_ID+" and A_Asset_ID="+Asset_ID
				+ " and PROGRAMMINGTYPE='"+ ProgramingType +"'";  
				
				DB.executeUpdate(sql);
				
				
			}
		}
		loadMPs ();
	}   //  saveData
	
	
	public boolean createOT(int MPD_ID, Timestamp Datetrx,String description,int Asset_ID ){
		
		//creatin header OT
		int MP_ID = new X_MP_MaintainDetail(Env.getCtx(), MPD_ID, null).getMP_Maintain_ID();
		X_MP_OT newOT=new X_MP_OT(Env.getCtx(), 0,null);
		newOT.setDateTrx(Datetrx);
		newOT.setDescription(description);
		newOT.setA_Asset_ID(Asset_ID);
		newOT.setMP_Maintain_ID(MP_ID);
		newOT.set_CustomColumn("MP_MaintainDetail_ID", MPD_ID);
		newOT.setDocStatus("DR");
		newOT.setDocAction("CO");
		newOT.setC_DocType_ID(MDocType.getOfDocBaseType(Env.getCtx(), "OTP")[0].getC_DocType_ID());
		if(!newOT.save())//creada nueva OT
			return false;
		
		if(!createOTTaskDetail(MP_ID,newOT,MPD_ID))
			return false;
		
		String sql = "select mp_maintain_id,mp_maintainDetail_id from mp_maintainDetail where ischild='Y' and MP_MAINTAINPARENT_ID=? and A_Asset_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,MP_ID);
			pstmt.setInt(2,Asset_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				createOTTaskDetail(rs.getInt(1),newOT,rs.getInt(2));
				lookChilds(rs.getInt(1),newOT,rs.getInt(2),Asset_ID);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		
		
		return true;
	}
	
	public void lookChilds(int MP_ID, X_MP_OT OT,int MPD_ID,int Asset_ID){
		
		String sql = "select mp_maintain_id,mp_maintainDetail_id from mp_maintainDetail where ischild='Y' and MP_MAINTAINPARENT_ID=? and A_Asset_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,MP_ID);
			pstmt.setInt(2,Asset_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				createOTTaskDetail(rs.getInt(1),OT,rs.getInt(2));
				lookChilds(rs.getInt(1),OT,rs.getInt(2),Asset_ID);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
	}
	
	public boolean createOTTaskDetail(int MP_ID, X_MP_OT OT,int MPD_ID){
		
		String sql = "select * from MP_MAINTAIN_TASK where MP_MAINTAIN_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,MP_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				X_MP_OT_Task ta = new X_MP_OT_Task(Env.getCtx(), 0,null);
				ta.setMP_Maintain_ID(MP_ID);
				ta.set_CustomColumn("MP_MaintainDetail_ID", MPD_ID);
				ta.setAD_Org_ID(rs.getInt("AD_Org_ID"));
				ta.setMP_OT_ID(OT.getMP_OT_ID());
				ta.setDescription(rs.getString("Description"));
				ta.setDuration(rs.getBigDecimal("Duration"));
				ta.setC_UOM_ID(rs.getInt("C_UOM_ID"));
				ta.setStatus(X_MP_OT_Task.STATUS_NotStarted);
				ta.saveEx();
				
				createOTResourceDetail(rs.getInt("MP_MAINTAIN_TASK_ID"),ta.getMP_OT_Task_ID());
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		//update MP
		if(OFBForward.NewUpdateMantainceDetailTSM())
		{
			updateMPDTSM(OT.get_ValueAsInt("MP_MaintainDetail_ID"),OT.getDateTrx(),OT.getDescription(),OT);
		}
		else
			updateMPD(OT.get_ValueAsInt("MP_MaintainDetail_ID"),OT.getDateTrx(),OT.getDescription());
		
		return true;
		
	}
	public boolean createOTResourceDetail(int oldTask_ID, int newTask_ID){
		
		String sql = "select * from MP_MAINTAIN_RESOURCE where MP_MAINTAIN_TASK_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,oldTask_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				X_MP_OT_Resource re=new X_MP_OT_Resource(Env.getCtx(), 0,null);
				re.setAD_Org_ID(rs.getInt("AD_Org_ID"));
				re.setMP_OT_Task_ID(newTask_ID);
				re.setCostAmt(rs.getBigDecimal("CostAmt"));
				re.setS_Resource_ID(rs.getInt("S_Resource_ID"));
				re.setM_BOM_ID(rs.getInt("M_BOM_ID"));
				re.setResourceQty(rs.getBigDecimal("RESOURCEQTY"));
				re.setResourceType(rs.getString("RESOURCETYPE"));
				re.set_ValueOfColumn("M_Product_ID", rs.getInt("M_Product_ID"));
				re.saveEx();
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		return true;
		
	}
	
	public void updateMPD(int MPD_ID,Timestamp Datetrx,String description ){
		X_MP_MaintainDetail mp= new X_MP_MaintainDetail(Env.getCtx(), MPD_ID,null);
		mp.setdatelastrunmp(Datetrx);
		mp.setDateLastRun(Datetrx);
		if(mp.isProgrammingType().equals("C"))
			mp.setDateNextRun(new Timestamp(Datetrx.getTime()+(mp.getInterval().longValue()*86400000) ));
		else
		{
			mp.setnextmp(mp.getInterval().add(mp.getlastread()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			mp.setlastmp(new BigDecimal(Float.parseFloat(description.split(":")[description.split(":").length-1].replace(',', '.'))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
		}
			//mp.setNEXTMP(mp.getNEXTMP().add(mp.getINTERVAL().add( new BigDecimal(mp.getLASTMP().longValue()%mp.getINTERVAL().longValue()))));
		mp.save(); //actualizacion MP
	}
	//ininoles nuevo tipo de actualización apara TSM por campo en OT
	public void updateMPDTSM(int MPD_ID,Timestamp Datetrx,String description, X_MP_OT OT)
	{
		X_MP_MaintainDetail mp= new X_MP_MaintainDetail(Env.getCtx(), MPD_ID,null);
		mp.setdatelastrunmp(Datetrx);
		mp.setDateLastRun(Datetrx);
		if(mp.isProgrammingType().equals("C"))
			mp.setDateNextRun(new Timestamp(Datetrx.getTime()+(mp.getInterval().longValue()*86400000) ));
		else
		{
			BigDecimal actualKm = (BigDecimal)OT.get_Value("tsm_km");
			if(actualKm != null)
				mp.setnextmp(mp.getInterval().add(actualKm).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			else
				mp.setnextmp(mp.getInterval().add(mp.getlastread()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
			mp.setlastmp(new BigDecimal(Float.parseFloat(description.split(":")[description.split(":").length-1].replace(',', '.'))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
		}
			//mp.setNEXTMP(mp.getNEXTMP().add(mp.getINTERVAL().add( new BigDecimal(mp.getLASTMP().longValue()%mp.getINTERVAL().longValue()))));
		mp.save(); //actualizacion MP
	}
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
