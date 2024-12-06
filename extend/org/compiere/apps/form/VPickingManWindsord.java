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
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.compiere.apps.ADialog;
import org.compiere.apps.StatusBar;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MInOut;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPayment;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.compiere.util.Util;

public class VPickingManWindsord extends Allocation
	implements FormPanel, ActionListener, VetoableChangeListener
{
	private CPanel panel = new CPanel();

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
		try
		{
			super.dynInit();
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

	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private CPanel allocationPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private JLabel bpartnerLabel = new JLabel();
	private VLookup bpartnerSearch = null;
	private MiniTable inoutTable = new MiniTable();
	
	private CPanel inoutPanel = new CPanel();
	
	private JLabel inoutLabel = new JLabel();
	
	private BorderLayout inoutLayout = new BorderLayout();
	
	private JLabel inoutInfo = new JLabel();
	
	private JScrollPane inoutScrollPane = new JScrollPane();
	private GridBagLayout allocationLayout = new GridBagLayout();
	
	private JButton setButton = new JButton();
	
	private StatusBar statusBar = new StatusBar();
	private CLabel dateFromLabel = new CLabel(Msg.translate(Env.getCtx(), "DateOrdered"));
	protected VDate dateFromField = new VDate("DateFrom", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateFrom"));
	private CLabel dateToLabel = new CLabel("-");
	protected VDate dateToField = new VDate("DateTo", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateTo"));
	
	private JLabel organizationLabel = new JLabel();
	private VLookup organizationPick = null;
	
	private JLabel pickmanLabel = new JLabel();
	private VLookup pickmanPick = null;
	
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(panel);
		//
		mainPanel.setLayout(mainLayout);
		//
		parameterPanel.setLayout(parameterLayout);
		allocationPanel.setLayout(allocationLayout);
		bpartnerLabel.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		
		inoutLabel.setRequestFocusEnabled(false);
		inoutLabel.setText("Despachos");
		
		pickmanLabel.setText("Pickeador");
		
		inoutPanel.setLayout(inoutLayout);
		inoutInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		inoutInfo.setHorizontalTextPosition(SwingConstants.RIGHT);
		inoutInfo.setText(".");
		
		setButton.setText(Msg.getMsg(Env.getCtx(), "Set Pickeador"));
		setButton.addActionListener(this);
		
		inoutScrollPane.setPreferredSize(new Dimension(200, 200));
	
		mainPanel.add(parameterPanel, BorderLayout.NORTH);
		
		//org filter
		organizationLabel.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		parameterPanel.add(organizationLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));
		parameterPanel.add(organizationPick, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));
		
		parameterPanel.add(bpartnerLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(bpartnerSearch, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		
		parameterPanel.add(dateFromLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		parameterPanel.add(dateFromField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(dateToLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		parameterPanel.add(dateToField, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		mainPanel.add(allocationPanel, BorderLayout.SOUTH);
		
		allocationPanel.add(pickmanLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		allocationPanel.add(pickmanPick, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		
		allocationPanel.add(setButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		
		inoutPanel.add(inoutLabel, BorderLayout.NORTH);
		inoutPanel.add(inoutInfo, BorderLayout.SOUTH);
		inoutPanel.add(inoutScrollPane, BorderLayout.CENTER);
		inoutScrollPane.getViewport().add(inoutTable, null);
		//
		mainPanel.add(inoutPanel, BorderLayout.CENTER);
		
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
		
		// Organization filter selection
		int AD_Column_ID = 839; //C_Period.AD_Org_ID (needed to allow org 0)
		MLookup lookupOrg = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		organizationPick = new VLookup("AD_Org_ID", true, false, true, lookupOrg);
		organizationPick.setValue(Env.getAD_Org_ID(Env.getCtx()));
		organizationPick.addVetoableChangeListener(this);
		
		AD_Column_ID = 1009452;
		MLookup lookuppickman = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.TableDir);
		pickmanPick = new VLookup("M_SHIPMAN_ID", true, false, true, lookuppickman);
		
		

		//  BPartner
		AD_Column_ID = 3499;        //  C_inout.C_BPartner_ID
		MLookup lookupBP = MLookupFactory.get (Env.getCtx(), m_WindowNo, 0, AD_Column_ID, DisplayType.Search);
		bpartnerSearch = new VLookup("C_BPartner_ID", true, false, true, lookupBP);
		bpartnerSearch.addVetoableChangeListener(this);

		//  Translation
		//statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "AllocateStatus"));
		statusBar.setStatusDB("");

		//  Date set to Login Date
		dateFromField.addVetoableChangeListener(this);
		dateToField.addVetoableChangeListener(this);
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
		//	Allocate
		if (e.getSource().equals(setButton))
		{
			if(pickmanPick.getValue() ==null)
				ADialog.warn(m_WindowNo, panel, "Debe seleccionar Pickeador");
			else
			{
				setButton.setEnabled(false);
				saveData();
				loadInOut();
				setButton.setEnabled(true);
				}
		}
	}   //  actionPerformed

	
	/**
	 *  Vetoable Change Listener.
	 *  - Business Partner
	 *  - org
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
		
		// Organization
		if (name.equals("AD_Org_ID"))
		{
			if (value == null)
				m_AD_Org_ID = 0;
			else
				m_AD_Org_ID = ((Integer) value).intValue();
			
			loadInOut();
		}

		//  BPartner
		if (name.equals("C_BPartner_ID"))
		{
			bpartnerSearch.setValue(value);
			m_C_BPartner_ID = ((Integer)value).intValue();
			loadInOut();
		}
		
		//	Date for Multi-Currency
		if(e.equals(dateToField) || e.equals(dateFromField) )
			loadInOut();
	}   //  vetoableChange
	
	public void loadInOut()
	{
		checkBPartner();
		
		Vector<Vector<Object>> data = getInOutData();
		Vector<String> columnNames = getInOutColumnNames();
		//  Set Model
		DefaultTableModel modelI = new DefaultTableModel(data, columnNames);
		inoutTable.setModel(modelI);
		setInOutColumnClass();
		//
		
	}
	
	
	/**************************************************************************
	 *  Save Data
	 */
	public void saveData()
	{
		try
		{
			Trx.run(new TrxRunnable() 
			{
				public void run(String trxName)
				{
					statusBar.setStatusLine(saveData(trxName));
				}
			});
		}
		catch (Exception e)
		{
			ADialog.error(m_WindowNo, panel, "Error", e.getLocalizedMessage());
			return;
		}
		
		
	}   //  saveData
	
	/**************************************************************************
	 *  Save Data
	 */
	public String saveData(String trxName)
	{
		for (int i = 0; i < inoutTable.getRowCount(); i++)
		{
			if (((Boolean)inoutTable.getValueAt(i, 0)).booleanValue())
			{
				
				KeyNamePair pp = (KeyNamePair)inoutTable.getValueAt(i, 2);   //  2-C_Payment_ID
				int InOut_ID = pp.getKey();
				
				MInOut out = new MInOut(Env.getCtx(),InOut_ID,trxName );
				out.set_CustomColumn("M_SHIPMAN_ID", (Integer)pickmanPick.getValue());
				
				if (!out.save())
					log.log(Level.SEVERE, "InOut not saved" + i);
			}   //   if selected
		}   //  for all rows
		
		return "";
	}
	
	public Vector<Vector<Object>> getInOutData()
	{		
		/********************************
		 *  Load unallocated Payments
		 *      1-TrxDate, 2-DocumentNo, (3-Currency, 4-PayAmt,)
		 *      5-ConvAmt, 6-ConvOpen, 7-Allocated
		 */
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuffer sql = new StringBuffer("SELECT o.M_InOut_ID, o.Ad_Org_ID, o.DocumentNo, o.DateOrdered, bp.name, w.Name as Warehouse, s.Name as Shipper, c.Documentno as SalesOrder "
			+ "FROM M_InOut o"		
			+ " INNER JOIN C_BPartner bp ON (o.C_BPartner_ID=bp.C_BPartner_ID) "
			+ " INNER join M_Warehouse w ON (o.M_Warehouse_ID=w.M_Warehouse_ID) " 
			+ " Left Outer join M_SHIPMAN s ON (s.M_SHIPMAN_ID=o.M_SHIPMAN_ID) " 	
			+ " Left Outer join C_Order c ON (o.C_Order_ID=c.C_Order_ID) " 	
			+ " WHERE o.DocStatus = 'IP' and o.MovementType='C-'");
		
		 if(m_AD_Org_ID>0)
			 sql.append(" and o.AD_Org_ID=?");
		 
		 if(m_C_BPartner_ID>0)
			 sql.append(" and o.C_BPartner_ID=?");
		 
		 if (dateFromField != null || dateToField != null)
			{
				Timestamp from = (Timestamp) dateFromField.getValue();
				Timestamp to = (Timestamp) dateToField.getValue();
				if (from == null && to != null)
					sql.append(" AND TRUNC(o.DateOrdered) <= ?");
				else if (from != null && to == null)
					sql.append(" AND TRUNC(o.DateOrdered) >= ?");
				else if (from != null && to != null)
					sql.append(" AND TRUNC(o.DateOrdered) BETWEEN ? AND ?");
			}
		
		
		log.fine("PaySQL=" + sql.toString());
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			int i = 1;
			 if(m_AD_Org_ID>0)
				 pstmt.setInt(i++, m_AD_Org_ID);
			 if(m_C_BPartner_ID>0)
				 pstmt.setInt(i++, m_C_BPartner_ID);
			 if (dateFromField != null || dateToField != null)
				{
					Timestamp from = (Timestamp) dateFromField.getValue();
					Timestamp to = (Timestamp) dateToField.getValue();
					log.fine("Date From=" + from + ", To=" + to);
					if (from == null && to != null)
						pstmt.setTimestamp(i++, to);
					else if (from != null && to == null)
						pstmt.setTimestamp(i++, from);
					else if (from != null && to != null)
					{
						pstmt.setTimestamp(i++, from);
						pstmt.setTimestamp(i++, to);
					}
				}
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(4));       //  1-TrxDate
				KeyNamePair pp = new KeyNamePair(rs.getInt(1), rs.getString(3));
				line.add(pp);                       //  2-DocumentNo
				
				line.add(rs.getString(6));      //  3-warehouse
				line.add(rs.getString(5));		//  4-bpartner
				line.add(rs.getString(8));		//  5-sales order
				line.add(rs.getString(7));		//  6-shipman
				
				
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
	
	public Vector<String> getInOutColumnNames()
	{	
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Util.cleanAmp(Msg.translate(Env.getCtx(), "DocumentNo")));
		
		columnNames.add(Msg.getMsg(Env.getCtx(), "Bodega"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "Cliente"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "Orden de Venta"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "Pickeador"));
		return columnNames;
	}
	
	public void setInOutColumnClass()
	{
		int i = 0;
		inoutTable.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		inoutTable.setColumnClass(i++, Timestamp.class, true);        //  1-TrxDate
		inoutTable.setColumnClass(i++, String.class, true);           //  2-Value
		
		inoutTable.setColumnClass(i++, String.class, false);      //  3-warehouse
		inoutTable.setColumnClass(i++, String.class, false); 		//  4-bpartner
		inoutTable.setColumnClass(i++, String.class, false); 		//  4-bpartner
		inoutTable.setColumnClass(i++, String.class, false); 		//  5-shipman

		//  Table UI
		inoutTable.autoSize();
	}
}