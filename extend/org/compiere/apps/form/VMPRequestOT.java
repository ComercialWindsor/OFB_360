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
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.adempiere.plaf.AdempierePLAF;
import org.compiere.apps.StatusBar;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MColumn;
import org.compiere.model.MDocType;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrder;
import org.compiere.model.MRMA;
import org.compiere.model.X_MP_JobStandar;
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
import org.compiere.util.Trx;

/**
 * Solicitud OT Mantencion 
 *
 * @author  Fabian Aguilar
 * @version $Id: VMPRequestOT.java,v 1.2 2010/03/10 00:51:28$
 * 
 * Contributor : Fabian Aguilar - OFBConsulting - generate OT
 */
public class VMPRequestOT extends CPanel
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
	private static CLogger log = CLogger.getCLogger(VMPRequestOT.class);

	
	private boolean     m_calculating = false;
	private int         m_C_Currency_ID = 0;
	
	//
	private CPanel mainPanel = new CPanel();
	private CPanel parameterPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel allocationPanel = new CPanel();
	private MiniTable requestTable = new MiniTable();
	private CPanel infoPanel = new CPanel();
	private int         m_A_Asset_ID = 0;
	private int         m_Job_ID = 0;

	
	private CPanel requestPanel = new CPanel();
	private JLabel requestLabel = new JLabel();
	private BorderLayout requestLayout = new BorderLayout();
	private JLabel requestInfo = new JLabel();
	private JScrollPane requestScrollPane = new JScrollPane();
	private GridBagLayout allocationLayout = new GridBagLayout();
	private JLabel differenceLabel = new JLabel();
	private CTextField differenceField = new CTextField();
	private JButton ProcessButton = new JButton();
	private JButton VoidButton = new JButton();
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
	private JLabel jobLabel = new JLabel();
	private VLookup jobSearch = null;
	private JButton jobButton = new JButton();

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
		requestLabel.setRequestFocusEnabled(false);
		requestLabel.setText("Request List");
		requestPanel.setLayout(requestLayout);
		requestInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		requestInfo.setHorizontalTextPosition(SwingConstants.RIGHT);
		requestInfo.setText(".");
		differenceLabel.setText(Msg.getMsg(Env.getCtx(), "Difference"));
		differenceField.setBackground(AdempierePLAF.getFieldBackground_Inactive());
		differenceField.setEditable(false);
		differenceField.setText("0");
		differenceField.setColumns(8);
		differenceField.setHorizontalAlignment(SwingConstants.RIGHT);
		ProcessButton.setText("Generar OT");
		ProcessButton.addActionListener(this);
		VoidButton.setText("Rechazar Solicitud");
		VoidButton.addActionListener(this);
		searchButton.setText("Buscar");
		searchButton.addActionListener(this);
		addButton.setText("Agregar Seleccion");
		addButton.addActionListener(this);
		EnableButton.setText("Habilitar Edicion");
		EnableButton.addActionListener(this);
		ChangeButton.setText("Aplicar Fecha");
		ChangeButton.addActionListener(this);
		jobButton.setText("Asignar Estandar");
		jobButton.addActionListener(this);
		jobLabel.setText("Estandar");
		
		assetLabel.setText(Msg.translate(Env.getCtx(), "A_Asset_ID"));
		selectall.setText(Msg.getMsg(Env.getCtx(), "Select all"));
		selectall.addActionListener(this);
		allocCurrencyLabel.setText(".");
		requestScrollPane.setPreferredSize(new Dimension(300, 300));
		
		
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
//		  parameterPanel.add(EnableButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
//				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
			parameterPanel.add(dateLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(dateField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
			parameterPanel.add(ChangeButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(assetLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(assetSearch, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(selectall, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(jobLabel, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(jobSearch, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			parameterPanel.add(jobButton, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
		mainPanel.add(allocationPanel, BorderLayout.SOUTH);
		allocationPanel.add(VoidButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		allocationPanel.add(ProcessButton, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		
		requestPanel.add(requestLabel, BorderLayout.NORTH);
		requestPanel.add(requestInfo, BorderLayout.SOUTH);
		requestPanel.add(requestScrollPane, BorderLayout.CENTER);
		requestScrollPane.getViewport().add(requestTable, null);
		mainPanel.add(infoPanel, BorderLayout.CENTER);
		
		
		infoPanel.setLayout(new BorderLayout());
		
		infoPanel.add(requestPanel,BorderLayout.CENTER);
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
		int AD_Column_ID = MColumn.getColumn_ID("MP_Maintain","A_Asset_ID");        //  MP_Maintain.A_Asset_ID
		MLookup lookupBP = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		assetSearch = new VLookup("A_Asset_ID", true, false, true, lookupBP);
		assetSearch.addVetoableChangeListener(this);
	
	// jobstandard
		AD_Column_ID = MColumn.getColumn_ID("MP_Maintain","MP_JobStandar_ID");        //  MP_Maintain.A_Asset_ID
		MLookup lookupjob = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		jobSearch = new VLookup("MP_JobStandar_ID", true, false, true, lookupjob);
		jobSearch.addVetoableChangeListener(this);
	
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
		 *  Load Request table
		 *      1-TrxDate, 2-DocumentNo, (3-Currency, 4-PayAmt,)
		 *      5-ConvAmt, 6-ConvOpen, 7-Allocated
		 */
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuffer sql = new StringBuffer("select rt.DATEDOC,rt.DATEREQUIRED,rt.DOCUMENTNO,rt.MP_OT_REQUEST_ID, rt.AD_Org_ID, rt.AD_User_ID, "+
			"rt.A_Asset_ID, rt.MP_JOBSTANDAR_ID, rt.Description, u.name, a.name as AssetName, jo.name as jobName,rl.name as PriorityRule, rt.OT_Request_Type "+
			"from MP_OT_REQUEST rt "+
			"inner join AD_User u on (rt.AD_User_ID=u.AD_User_ID) "+
			"inner join A_Asset a on (rt.A_Asset_ID=a.A_Asset_ID) "+
			"left outer join MP_JOBSTANDAR jo on (rt.MP_JOBSTANDAR_ID=jo.MP_JOBSTANDAR_ID) " +
			"inner join AD_Ref_List rl on (rt.PriorityRule=rl.value and rl. AD_Reference_ID=154) " +
			"where rt.DocStatus='WC' and rt.processed='Y'");
		
		sql.append(" and rt.AD_Client_ID="+ Env.getAD_Client_ID(Env.getCtx()));

		log.config("Prognosis=" + sql.toString());
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			//pstmt.setInt(1, Instance_ID);
			String tipo=new String();
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
				if(rs.getString(14).equals("RV"))
					tipo="Revision";
				if(rs.getString(14).equals("RP"))
					tipo="Reparacion";
				if(rs.getString(14).equals("CN"))
					tipo="Completar Niveles";
				line.add(tipo); // 1-tipo solicitud
				line.add(rs.getTimestamp(2));       //  2-fecha solicitada
				KeyNamePair pp = new KeyNamePair(rs.getInt(4), rs.getString(3));
				line.add(pp); //  3-numero documento
				line.add(rs.getTimestamp(1));  //  4-fecha doc
				KeyNamePair pp2 = new KeyNamePair(rs.getInt(6), rs.getString(10));
				line.add(pp2);  //  5-user
				KeyNamePair pp3 = new KeyNamePair(rs.getInt(7), rs.getString(11));
				line.add(pp3);      //  6-asset
				line.add(rs.getString(9)); 				//  7-description
				line.add(rs.getString(13)); //8-prioridad
				KeyNamePair pp4 = new KeyNamePair(rs.getInt(8), rs.getString(12));
				line.add(pp4);   //9 - jobstandard
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
		requestTable.getModel().removeTableModelListener(this);
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("Seleccionar");
		columnNames.add("Tipo Solicitud");
		columnNames.add("Fecha Programacion");
		columnNames.add("Num Solicitud");
		columnNames.add("Fecha Solicitud");
		columnNames.add("Usuario");
		columnNames.add("Activo");
		columnNames.add("Descripcion");
		columnNames.add("Prioridad");
		columnNames.add("Trabajo Estandar");

		
		//  Set Model
		DefaultTableModel modelP = new DefaultTableModel(data, columnNames);
		modelP.addTableModelListener(this);
		requestTable.setModel(modelP);
		//
		int i = 0;
		requestTable.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		requestTable.setColumnClass(i++, String.class, true);			//1-tipo solicitud
		requestTable.setColumnClass(i++, Timestamp.class, false);         //  2-fecha solicitada
		requestTable.setColumnClass(i++, String.class, true);           //  3-numero documento
		requestTable.setColumnClass(i++, Timestamp.class, true);        //  4-fecha doc
		requestTable.setColumnClass(i++, String.class, true);       //  5-user
		requestTable.setColumnClass(i++, String.class, true);       //  6-asset
		requestTable.setColumnClass(i++, String.class, true);      //  7-description
		requestTable.setColumnClass(i++, String.class, true);		//8-prioridad
		requestTable.setColumnClass(i++, String.class, true);      //  9-jobstandard


		
		

		//  Table UI
		requestTable.autoSize();
		
		requestTable.setColumnReadOnly(1, false);


		
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
		else if (e.getSource().equals(jobButton))
		{
			TableModel prognosis = requestTable.getModel();
			int rows = prognosis.getRowCount();
			X_MP_JobStandar job=new X_MP_JobStandar(Env.getCtx(), m_Job_ID,null);
			for (int i = 0; i < rows; i++)
			{
				if (((Boolean)prognosis.getValueAt(i, 0)).booleanValue())
				{
					KeyNamePair pp = new KeyNamePair(job.getMP_JobStandar_ID(), job.getName());
					prognosis.setValueAt(pp, i, 9);					
				}
			}
			
		}
		else if (e.getSource().equals(VoidButton)){
			voidData();
		}
		else if (e.getSource().equals(ChangeButton))
		{
			ProcessButton.setEnabled(true);
			//updating rows selected
			TableModel prognosis = requestTable.getModel();
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
			TableModel prognosis = requestTable.getModel();
			int rows = prognosis.getRowCount();
			for (int i = 0; i < rows; i++)
			{		
				if(m_A_Asset_ID==0)
					prognosis.setValueAt(selectall.isSelected(), i, 0);
				else{
					KeyNamePair pp = (KeyNamePair)prognosis.getValueAt(i, 6);//Asset
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
			
		}
		else if(name.equals("MP_JobStandar_ID"))
		{
			jobSearch.setValue(value);
			m_Job_ID = ((Integer)value).intValue();
		}
		
	}   //  vetoableChange

	
	/**************************************************************************
	 *  Save Data
	 */
	private void saveData()
	{
		
		TableModel prognosis = requestTable.getModel();
		int rows = prognosis.getRowCount();
		Trx trx = Trx.get(Trx.createTrxName("AL"), true);
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)prognosis.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)prognosis.getValueAt(i, 3);//Req
				int Req_ID=pp.getKey();
				pp = (KeyNamePair)prognosis.getValueAt(i, 6);//Asset
				int Asset_ID=pp.getKey();
				Timestamp Datetrx =(Timestamp)prognosis.getValueAt(i, 2);//Date
				String description =(String)prognosis.getValueAt(i, 7);//Description
				
				
				int job_id=0;
				if(prognosis.getValueAt(i, 9)!=null){
					pp=(KeyNamePair)prognosis.getValueAt(i, 9);//job
					job_id=pp.getKey();
				}
				   
				if(!createOT(job_id,Datetrx,description,Asset_ID, Req_ID)){
						log.log(Level.SEVERE, "OT not created #" + i);
						continue;
				}
				
				String sql="Update MP_OT_REQUEST set DocStatus='AP',MP_JobStandar_ID="+job_id
				+ " where MP_OT_REQUEST_ID="+Req_ID; 
				
				DB.executeUpdate(sql);
				
				
			}
		}
		loadMPs ();
	}   //  saveData
	
	
	/**************************************************************************
	 *  Void Data
	 */
	private void voidData()
	{
		
		TableModel prognosis = requestTable.getModel();
		int rows = prognosis.getRowCount();
		Trx trx = Trx.get(Trx.createTrxName("AL"), true);
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)prognosis.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)prognosis.getValueAt(i, 3);//Req
				int Req_ID=pp.getKey();
				
				String sql="Update MP_OT_REQUEST set DocStatus='VO'"
				+ " where MP_OT_REQUEST_ID="+Req_ID; 
				
				DB.executeUpdate(sql);
				
				
			}
		}
		loadMPs ();
	}   //  voidDate
	
	public boolean createOT(int Job_ID, Timestamp Datetrx,String description,int Asset_ID, int Req_ID ){
		
		//creatin header OT
		X_MP_OT newOT=new X_MP_OT(Env.getCtx(), 0,null);
		newOT.setDateTrx(Datetrx);
		newOT.setDescription(description);
		newOT.setA_Asset_ID(Asset_ID);
		newOT.setMP_JobStandar_ID(Job_ID);
		newOT.setDocStatus("DR");
		newOT.setDocAction("CO");
		newOT.setMP_OT_Request_ID(Req_ID);
		newOT.setC_DocType_ID(MDocType.getOfDocBaseType(Env.getCtx(), "OTC")[0].getC_DocType_ID());
		if(!newOT.save())//creada nueva OT
			return false;
		
		if(!createOTTaskDetail(Job_ID,newOT))
			return false;
		
		return true;
	}
	
	public void lookChilds(int MP_ID, X_MP_OT OT){
		
		String sql = "select mp_maintain_id from mp_maintain where ischild='Y' and MP_MAINTAINPARENT_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,MP_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				createOTTaskDetail(rs.getInt(1),OT);
				lookChilds(rs.getInt(1),OT);
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
	
	public boolean createOTTaskDetail(int Job_ID, X_MP_OT OT){
		
		String sql = "select * from MP_JOBSTANDAR_TASK where MP_JOBSTANDAR_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,Job_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				X_MP_OT_Task ta = new X_MP_OT_Task(Env.getCtx(), 0,null);
				//ta.setMP_Maintain_ID(MP_ID);
				ta.setAD_Org_ID(rs.getInt("AD_Org_ID"));
				ta.setMP_OT_ID(OT.getMP_OT_ID());
				ta.setDescription(rs.getString("Description"));
				ta.setDuration(rs.getBigDecimal("Duration"));
				ta.setC_UOM_ID(rs.getInt("C_UOM_ID"));
				ta.setStatus(ta.STATUS_NotStarted);
				ta.saveEx();
				
				createOTResourceDetail(rs.getInt("MP_JOBSTANDAR_TASK_ID"),ta.getMP_OT_Task_ID());
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
	public boolean createOTResourceDetail(int oldTask_ID, int newTask_ID){
		
		String sql = "select * from MP_JOBSTANDAR_RESOURCE where MP_JOBSTANDAR_TASK_ID=?";
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
