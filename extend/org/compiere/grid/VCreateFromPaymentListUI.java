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
package org.compiere.grid;

import java.awt.BorderLayout;
import java.awt.Cursor;
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
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import org.compiere.apps.AEnv;
import org.compiere.apps.ConfirmPanel;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.model.GridTab;
import org.compiere.model.MBankAccount;
import org.compiere.model.MBankStatement;
import org.compiere.model.MColumn;
import org.compiere.model.MDocType;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPayment;
import org.compiere.model.X_C_PaymentRequest;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public class VCreateFromPaymentListUI extends CreateFromPaymentList implements ActionListener, VetoableChangeListener
{
	private static final long serialVersionUID = 1L;
	
	private VCreateFromDialog dialog;

	public VCreateFromPaymentListUI(GridTab mTab)
	{
		super(mTab);
		log.info(getGridTab().toString());
		
		dialog = new VCreateFromDialog(this, getGridTab().getWindowNo(), true);
		
		p_WindowNo = getGridTab().getWindowNo();

		try
		{
			if (!dynInit())
				return;
			jbInit();

			setInitOK(true);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
			setInitOK(false);
		}
		AEnv.positionCenterWindow(Env.getWindow(p_WindowNo), dialog);
	}   //  VCreateFrom
	
	/** Window No               */
	private int p_WindowNo;

	/**	Logger			*/
	private CLogger log = CLogger.getCLogger(getClass());
	
	//
	private int Org_ID;
	private int BPartner_ID;
	
	private CLabel documentNoLabel = new CLabel(Msg.translate(Env.getCtx(), "DocumentNo"));
	protected CTextField documentNoField = new CTextField(10);
	
	protected CLabel BPartner_idLabel = new CLabel(Msg.translate(Env.getCtx(), "BPartner"));
	protected VLookup bPartnerLookup;
	
	protected CLabel Org_idLabel = new CLabel(Msg.translate(Env.getCtx(), "AD_Org_ID"));
	protected VLookup OrgLookup;
	
	private CLabel dateFromLabel = new CLabel(Msg.translate(Env.getCtx(), "DateTrx"));
	protected VDate dateFromField = new VDate("DateFrom", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateFrom"));
	private CLabel dateToLabel = new CLabel("-");
	protected VDate dateToField = new VDate("DateTo", false, false, true, DisplayType.Date, Msg.translate(Env.getCtx(), "DateTo"));
	
	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	public boolean dynInit() throws Exception
	{
		log.config("");
		
		super.dynInit();
		
		dialog.setTitle("Listado de  Pagos");

		//Refresh button
		CButton refreshButton = ConfirmPanel.createRefreshButton(false);
		refreshButton.setMargin(new Insets (1, 10, 0, 10));
		refreshButton.setDefaultCapable(true);
		refreshButton.addActionListener(this);
		dialog.getConfirmPanel().addButton(refreshButton);
		dialog.getRootPane().setDefaultButton(refreshButton);
				
		bPartnerLookup = new VLookup("C_BPartner_ID", false, false, true,
				MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, 3499, DisplayType.Search));
		BPartner_idLabel.setLabelFor(bPartnerLookup);
		
		Timestamp date = Env.getContextAsDate(Env.getCtx(), p_WindowNo, X_C_PaymentRequest.COLUMNNAME_DateTrx);
		dateToField.setValue(date);
			
		OrgLookup = new VLookup("AD_Org_ID", false, false, true,
				MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, MColumn.getColumn_ID(X_C_PaymentRequest.Table_Name, X_C_PaymentRequest.COLUMNNAME_AD_Org_ID), DisplayType.Search));
		Org_idLabel.setLabelFor(OrgLookup);
		
		Org_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "AD_Org_ID");
		OrgLookup.setValue(Org_ID);
		
		loadLines();
		return true;
	}   //  dynInit
    
	/**
	 *  Static Init.
	 *  <pre>
	 *  parameterPanel
	 *      parameterBankPanel
	 *      parameterStdPanel
	 *          bPartner/order/invoice/shopment/licator Label/Field
	 *  dataPane
	 *  southPanel
	 *      confirmPanel
	 *      statusBar
	 *  </pre>
	 *  @throws Exception
	 */
    private void jbInit() throws Exception
    {
    	documentNoLabel.setLabelFor(documentNoField);
    	dateFromLabel.setLabelFor(dateFromField);
    	dateFromField.setToolTipText(Msg.translate(Env.getCtx(), "DateFrom"));
    	dateToLabel.setLabelFor(dateToField);
    	dateToField.setToolTipText(Msg.translate(Env.getCtx(), "DateTo"));
    	
    	CPanel parameterPanel = dialog.getParameterPanel();
    	parameterPanel.setLayout(new BorderLayout());
    	
    	CPanel parameterBankPanel = new CPanel();
    	parameterBankPanel.setLayout(new GridBagLayout());
    	parameterPanel.add(parameterBankPanel, BorderLayout.CENTER);
    	
    	parameterBankPanel.add(BPartner_idLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	if (bPartnerLookup != null)
    		parameterBankPanel.add(bPartnerLookup, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    	parameterBankPanel.add(Org_idLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	if(OrgLookup!= null)
    		parameterBankPanel.add(OrgLookup, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	
    	parameterBankPanel.add(dateFromLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	if(dateFromField!= null)
    		parameterBankPanel.add(dateFromField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	parameterBankPanel.add(dateToLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	if(dateToField!= null)
    		parameterBankPanel.add(dateToField, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	
    	parameterBankPanel.add(documentNoLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    	parameterBankPanel.add(documentNoField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	
    	
    	
    }   //  jbInit

	/*************************************************************************/

	private boolean 	m_actionActive = false;
	
	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		log.config("Action=" + e.getActionCommand());
		
		if ( e.getActionCommand().equals(ConfirmPanel.A_REFRESH) )
		{
			Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
			
			loadLines ();
			dialog.tableChanged(null);
			Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		}
		
		m_actionActive = false;
	}   //  actionPerformed

	
	
	/**************************************************************************
	 *  Load BPartner Field
	 *  @param forInvoice true if Invoices are to be created, false receipts
	 *  @throws Exception if Lookups cannot be initialized
	 */
	protected void initBPartner (boolean forInvoice) throws Exception
	{
		
	}   //  initBPartner

	private void loadLines ()
	{
		
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		
		
		StringBuffer sql = new StringBuffer("");
			
			sql.append( " select i.c_payment_id,i.documentno,i.datetrx,i.c_bpartner_id,bp.name,i.payamt, i.description" +
				" from c_payment i inner join c_bpartner bp on (i.c_bpartner_id=bp.c_bpartner_id) " +
				" where i.isreceipt='N' and i.docstatus in ('DR') and i.payamt>0 ");
		
			if(bPartnerLookup.getValue()!=null && (Integer)bPartnerLookup.getValue()>0)
				sql.append( " And i.c_bpartner_id=" + (Integer)bPartnerLookup.getValue());
			
			if(OrgLookup.getValue()!=null && (Integer)OrgLookup.getValue()>0)
				sql.append( " And i.AD_Org_ID=" + (Integer)OrgLookup.getValue());
			
			if (documentNoField.getText().length() > 0)
				sql.append(" AND UPPER(i.DocumentNo) LIKE '%" + documentNoField.getText() + "%'");
			
			if (dateFromField.getValue() != null || dateToField.getValue() != null)
			{
				Timestamp from = (Timestamp) dateFromField.getValue();
				Timestamp to = (Timestamp) dateToField.getValue();
				if (from == null && to != null)
					sql.append(" AND TRUNC(i.datetrx) <= ?");
				else if (from != null && to == null)
					sql.append(" AND TRUNC(i.datetrx) >= ?");
				else if (from != null && to != null)
					sql.append(" AND TRUNC(i.datetrx) BETWEEN ? AND ?");
			}
			
			
		
				
		log.config ("**"+sql);
		
		try
		{
			
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			int index = 1;
			if (dateFromField.getValue() != null || dateToField.getValue() != null)
			{
				Timestamp from = (Timestamp) dateFromField.getValue();
				Timestamp to = (Timestamp) dateToField.getValue();
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
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>(10);
				
				KeyNamePair pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				
				KeyNamePair pp2 = new KeyNamePair(rs.getInt(4), rs.getString(5));
				
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(3));    //  1--Datetrx
				line.add(pp2);      //  2--BPartner
				line.add(pp);     //3- Documentno
				line.add(rs.getBigDecimal(6)); 		//4-amt
				line.add(rs.getString(7));     // 5 description
				
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		
		loadTableOIS(data);
	}
	
	
	/**
	 *  Load Order/Invoice/Shipment data into Table
	 *  @param data data
	 */
	protected void loadTableOIS (Vector<?> data)
	{
		//  Remove previous listeners
		dialog.getMiniTable().getModel().removeTableModelListener(dialog);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, getOISColumnNames());
		model.addTableModelListener(dialog);
		dialog.getMiniTable().setModel(model);
		// 
		
		configureMiniTable(dialog.getMiniTable());
	}   //  loadOrder
	
	public void showWindow()
	{
		dialog.setVisible(true);
	}
	
	public void closeWindow()
	{
		dialog.dispose();
	}
	
	/**
	 *  Change Listener
	 *  @param e event
	 */
	public void vetoableChange (PropertyChangeEvent e)
	{
		log.config(e.getPropertyName() + "=" + e.getNewValue());

		
		dialog.tableChanged(null);
	}   //  vetoableChange
}
