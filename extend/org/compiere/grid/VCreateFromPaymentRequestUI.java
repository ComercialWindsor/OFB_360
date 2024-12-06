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
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.table.DefaultTableModel;

import org.compiere.apps.AEnv;
import org.compiere.apps.ConfirmPanel;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.model.GridTab;
import org.compiere.model.MColumn;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRole;
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
import org.ofb.model.OFBForward;

public class VCreateFromPaymentRequestUI extends CreateFromPaymentRequest implements ActionListener, VetoableChangeListener
{
	private static final long serialVersionUID = 1L;
	
	private VCreateFromDialog dialog;

	public VCreateFromPaymentRequestUI(GridTab mTab)
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
	//private int BPartner_ID;
	
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
	
	protected CLabel Null_NUllLabel = new CLabel(Msg.translate(Env.getCtx(), "     "));
	
	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	public boolean dynInit() throws Exception
	{
		log.config("");
		
		super.dynInit();
		
		dialog.setTitle("Solicitud desde");

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
		
		/*BPartner_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		if(BPartner_ID>0)
		bPartnerLookup.setValue(BPartner_ID);*///no seteo por defecto al BP
		
		Timestamp date = Env.getContextAsDate(Env.getCtx(), p_WindowNo, X_C_PaymentRequest.COLUMNNAME_DateTrx);
		dateFromField.setValue(date);
		dateToField.setValue(date);
			
		//sacar rol para probar validacion ininoles
		MRole rol = new MRole(Env.getCtx(), Env.getAD_Role_ID(Env.getCtx()), null);
		
		boolean modOrg = rol.get_ValueAsBoolean("ModifyOrgPR");
				
		if (modOrg == true)//bloquear campo de organizacion
		{
			OrgLookup = new VLookup("AD_Org_ID", false, true, false,//se deja el campo organizacion como solo lectura 
					MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, MColumn.getColumn_ID(X_C_PaymentRequest.Table_Name, X_C_PaymentRequest.COLUMNNAME_AD_Org_ID), DisplayType.Search));
			Org_idLabel.setLabelFor(OrgLookup);
			
			Org_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "AD_Org_ID");
			OrgLookup.setValue(Org_ID);
		}
		else
		{			
			OrgLookup = new VLookup("AD_Org_ID", false, false, true, 
					MLookupFactory.get (Env.getCtx(), p_WindowNo, 0, MColumn.getColumn_ID(X_C_PaymentRequest.Table_Name, X_C_PaymentRequest.COLUMNNAME_AD_Org_ID), DisplayType.Search));
			Org_idLabel.setLabelFor(OrgLookup);
		}
		

		
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
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 200, 5, 5), 0, 0));
    	if (bPartnerLookup != null)
    		parameterBankPanel.add(bPartnerLookup, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    	parameterBankPanel.add(Org_idLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,100, 5, 5), 0, 0));
    	if(OrgLookup!= null)
    		parameterBankPanel.add(OrgLookup, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 200), 0, 0));
    	
  		parameterBankPanel.add(dateFromLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,200, 5, 5), 0, 0));
    	if(dateFromField!= null)
    		parameterBankPanel.add(dateFromField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	
    	parameterBankPanel.add(dateToLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 100, 5, 5), 0, 0));
    	if(dateToField!= null)
    		parameterBankPanel.add(dateToField, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 200), 0, 0));
    	
    	parameterBankPanel.add(documentNoLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 200, 5, 5), 0, 0));
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
		
		String type = Env.getContext(Env.getCtx(), p_WindowNo, X_C_PaymentRequest.COLUMNNAME_RequestType);
		type = getGridTab().get_ValueAsString(X_C_PaymentRequest.COLUMNNAME_RequestType);
		//ininoles tomar solo el primer caracter del tipo de solicitud
		type = Character.toString(type.charAt(0));  
		
		
		StringBuffer sql = new StringBuffer("");
		
		
		if(type.equals("I"))//invoice
		{	
			if (DB.isPostgreSQL())
			{
				sql.append( "select i.c_invoice_id,i.documentno,i.duedate,i.c_bpartner_id,bp.name, "+
						"abs(currencyConvert(invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID),i.C_Currency_ID,228,i.dateacct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP) as amt, "+ 
						"i.description,i.C_InvoicePaySchedule_ID, coalesce(pter.name, ' ') as ptername  "+
						"from c_invoice_v i inner join c_bpartner bp on (i.c_bpartner_id=bp.c_bpartner_id) "+ 
						" LEFT Join C_PaymentTerm pter ON (i.C_PaymentTerm_ID=pter.C_PaymentTerm_ID) "+        		
						" where i.issotrx='N' and i.docstatus in ('CO','CL') and ispaid = 'N' and docbaseType NOT IN ('APC','ARC') " +
						" and i.ad_client_id="+Env.getAD_Client_ID(Env.getCtx()));
			}else
			{
				sql.append( "select i.c_invoice_id,i.documentno,i.duedate,i.c_bpartner_id,bp.name, "+
					"abs(currencyConvert(invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID),i.C_Currency_ID,228,i.dateacct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP) as amt, "+ 
					"i.description,i.C_InvoicePaySchedule_ID, nvl(pter.name, ' ') as ptername  "+
					"from c_invoice_v i inner join c_bpartner bp on (i.c_bpartner_id=bp.c_bpartner_id) "+ 
					" LEFT Join C_PaymentTerm pter ON (i.C_PaymentTerm_ID=pter.C_PaymentTerm_ID) "+        		
					" where i.issotrx='N' and i.docstatus in ('CO','CL') and ispaid = 'N' and docbaseType NOT IN ('APC','ARC') " +
					" and i.ad_client_id="+Env.getAD_Client_ID(Env.getCtx()));
			}
			//ininoles se agrega validacion que no se use 2 veces una factura en documentos en borrador
			if(OFBForward.usePaymentOncePR())
			{
				sql.append(" AND i.C_Invoice_ID NOT IN(SELECT prl2.C_Invoice_ID " +
						" FROM C_PaymentRequestLine prl2" +
						" INNER JOIN C_PaymentRequest pr2 ON (prl2.C_PaymentRequest_ID = pr2.C_PaymentRequest_ID)" +
						" WHERE DocStatus NOT IN ('VO'))");
			}
			
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
					sql.append(" AND TRUNC(i.duedate) <= ?");
				else if (from != null && to == null)
					sql.append(" AND TRUNC(i.duedate) >= ?");
				else if (from != null && to != null)
					sql.append(" AND TRUNC(i.duedate) BETWEEN ? AND ?");
			}
			//se cambia validacion por monto ininoles version 2
			//Se dejara configurables a travez de ofbforward version 3 
			if(OFBForward.PayRequestLineValidInvoice())
			{
				sql.append(" And ( "+
						"(i.C_InvoicePaySchedule_ID is null AND i.c_invoice_id Not IN (select rl.c_invoice_id from c_paymentrequestline rl inner join c_paymentrequest r on (rl.c_paymentrequest_id=r.c_paymentrequest_id) where r.docstatus IN ('DR','CL','WC','CO')and c_invoice_id is not null)) "+
						"OR " +
						"(i.C_InvoicePaySchedule_ID is not null AND i.C_InvoicePaySchedule_ID Not IN (select rl.C_InvoicePaySchedule_ID from c_paymentrequestline rl inner join c_paymentrequest r on (rl.c_paymentrequest_id=r.c_paymentrequest_id) where r.docstatus IN ('DR','CL','WC','CO')and C_InvoicePaySchedule_ID is not null)) "+
						")" );
			}else
			{
				sql.append(" And (abs(currencyConvert(invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID),i.C_Currency_ID,228,i.dateacct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP)) > 0");
			}
		}
		else if(type.equals("J"))//journal line
		{	
			if(OFBForward.PayRequestUseJLineDate())
			{
				sql.append( "select gl.gl_journalline_id, j.documentno,gl.datetrx,gl.c_bpartner_id,bp.name,gl.amtacctdr,gl.amtacctcr,j.description " +
						" from gl_journal j " +
						"inner join gl_journalline gl on (j.gl_journal_id=gl.gl_journal_id) " +
						"inner join c_bpartner bp on (gl.c_bpartner_id=bp.c_bpartner_id) " +
						" where j.docstatus in ('CO','CL')");
			}
			else
			{	
				sql.append( "select gl.gl_journalline_id, j.documentno,j.dateacct,gl.c_bpartner_id,bp.name,gl.amtacctdr,gl.amtacctcr,j.description " +
					" from gl_journal j " +
					"inner join gl_journalline gl on (j.gl_journal_id=gl.gl_journal_id) " +
					"inner join c_bpartner bp on (gl.c_bpartner_id=bp.c_bpartner_id) " +
					" where j.docstatus in ('CO','CL')");
			}
			
			if(bPartnerLookup.getValue()!=null && (Integer)bPartnerLookup.getValue()>0)
				sql.append( " And gl.c_bpartner_id=" +(Integer)bPartnerLookup.getValue());
			
			if(OrgLookup.getValue()!=null)
				sql.append( " And j.AD_Org_ID=" + (Integer)OrgLookup.getValue());
			
			if (documentNoField.getText().length() > 0)
				sql.append(" AND UPPER(j.DocumentNo) LIKE '%" + documentNoField.getText() + "%'");
			
			if (dateFromField.getValue() != null || dateToField.getValue() != null)
			{
				Timestamp from = (Timestamp) dateFromField.getValue();
				Timestamp to = (Timestamp) dateToField.getValue();
				if(OFBForward.PayRequestFilterDateTrxLine())
				{
					if (from == null && to != null)
						sql.append(" AND TRUNC(gl.DateTrx) <= ?");
					else if (from != null && to == null)
						sql.append(" AND TRUNC(gl.DateTrx) >= ?");
					else if (from != null && to != null)
						sql.append(" AND TRUNC(gl.DateTrx) BETWEEN ? AND ?");
				}
				else
				{
					if (from == null && to != null)
						sql.append(" AND TRUNC(j.DateAcct) <= ?");
					else if (from != null && to == null)
						sql.append(" AND TRUNC(j.DateAcct) >= ?");
					else if (from != null && to != null)
						sql.append(" AND TRUNC(j.DateAcct) BETWEEN ? AND ?");
				}
			}
			
			sql.append(" And gl.gl_journalline_id Not IN (select rl.gl_journalline_id from c_paymentrequestline rl" +
					" inner join c_paymentrequest r on (rl.c_paymentrequest_id=r.c_paymentrequest_id) where r.docstatus IN ('DR','CL','WC','CO')and gl_journalline_id is not null ) ");
			if(OFBForward.PayRequestNotInCashLine())
			{
				sql.append(" AND gl.gl_journalline_id Not IN (SELECT cl.GL_JournalLine_id FROM C_CashLine cl " +
					" inner join C_Cash cc on (cl.C_Cash_ID = cc.C_Cash_ID) where cc.docstatus IN ('DR','CL','WC','CO')and gl_journalline_id is not null) ");
			}
			if(OFBForward.PayRequestNotClosedJournal())
			{
				sql.append(" AND j.DocStatus NOT IN ('CL','VO') ");
			}
			if(OFBForward.PayRequestSelectedAccount())
			{
				sql.append(" AND gl.gl_journalline_id Not IN (SELECT GL_JournalLine_ID " +
						" FROM c_validcombination vc " +
						" INNER JOIN C_ElementValue ev ON (vc.Account_ID = ev.C_ElementValue_ID) " +
						" INNER JOIN GL_JournalLine jl ON (vc.c_validcombination_ID = jl.c_validcombination_ID) " +
						" where ev.NoPaymentRequest = 'Y') ");
			}
			
		}	
		else if(type.equals("P"))//remuneracion ininoles
		{	
			sql.append( "select gl.gl_journalline_id, j.documentno,j.dateacct,gl.c_bpartner_id, "+
					"bp.name,gl.amtacctdr,gl.amtacctcr,j.description, "+
					"org.name,cpo.name,cvc.combination "+
					"from gl_journal j "+ 
					"inner join gl_journalline gl on (j.gl_journal_id=gl.gl_journal_id) "+
					"inner join ad_org org on (j.ad_org_id = org.ad_org_id) "+
					"left join c_bpartner bp on (gl.c_bpartner_id=bp.c_bpartner_id) "+ 
					"left join c_projectOFB cpo on (j.c_projectOFB_id = cpo.c_projectOFB_id) "+
					"left join C_ValidCombination cvc on (cvc.C_ValidCombination_ID = gl.C_ValidCombination_ID) "+
					"where j.docstatus in ('CO','CL') "+
					"and gl.amtacctcr > 0 "+ //ininoles traer solo lineas con credito
					"and j.ad_client_id="+Env.getAD_Client_ID(Env.getCtx()));
			
			if(bPartnerLookup.getValue()!=null && (Integer)bPartnerLookup.getValue()>0)
				sql.append( " And gl.c_bpartner_id=" +(Integer)bPartnerLookup.getValue());
			
			if(OrgLookup.getValue()!=null)
				sql.append( " And j.AD_Org_ID=" + (Integer)OrgLookup.getValue());
			
			if (documentNoField.getText().length() > 0)
				sql.append(" AND UPPER(j.DocumentNo) LIKE '%" + documentNoField.getText() + "%'");
			
			if (dateFromField.getValue() != null || dateToField.getValue() != null)
			{
				Timestamp from = (Timestamp) dateFromField.getValue();
				Timestamp to = (Timestamp) dateToField.getValue();
				if (from == null && to != null)
					sql.append(" AND TRUNC(j.DateAcct) <= ?");
				else if (from != null && to == null)
					sql.append(" AND TRUNC(j.DateAcct) >= ?");
				else if (from != null && to != null)
					sql.append(" AND TRUNC(j.DateAcct) BETWEEN ? AND ?");
			}
			
			sql.append(" And gl.gl_journalline_id Not IN (select rl.gl_journalline_id from c_paymentrequestline rl" +
					" inner join c_paymentrequest r on (rl.c_paymentrequest_id=r.c_paymentrequest_id) where r.docstatus IN ('DR','CL','WC','CO')and gl_journalline_id is not null ) ");
			
		}
		else if(type.equals("R"))//desde resolucion
		{
			sql.append("select dm.DM_Document_ID, dm.documentno,dm.datetrx, dm.c_bpartner_id, bp.name, dm.amt-dm.allocatedamt, dm.description,0,dm.AD_Org_ID, org.name" +
					" from  DM_Document dm " +
					" left outer join c_bpartner bp on (dm.c_bpartner_id=bp.c_bpartner_id) " +
					" left outer join AD_Org org on (dm.AD_Org_ID = org.AD_Org_ID) " +
					" where dm.docstatus='CO' and (dm.amt-dm.allocatedamt)>0");
			
			if(bPartnerLookup.getValue()!=null && (Integer)bPartnerLookup.getValue()>0)
				sql.append( " And dm.c_bpartner_id=" +(Integer)bPartnerLookup.getValue());
			
			if(OrgLookup.getValue()!=null)
				sql.append( " And dm.AD_Org_ID=" + (Integer)OrgLookup.getValue());
			
			if (documentNoField.getText().length() > 0)
				sql.append(" AND UPPER(dm.DocumentNo) LIKE '%" + documentNoField.getText() + "%'");
			
			if (dateFromField.getValue() != null || dateToField.getValue() != null)
			{
				Timestamp from = (Timestamp) dateFromField.getValue();
				Timestamp to = (Timestamp) dateToField.getValue();
				if (from == null && to != null)
					sql.append(" AND TRUNC(dm.datetrx) <= ?");
				else if (from != null && to == null)
					sql.append(" AND TRUNC(dm.datetrx) >= ?");
				else if (from != null && to != null)
					sql.append(" AND TRUNC(dm.datetrx) BETWEEN ? AND ?");
			}
		}
		else{ // resolution
			
			/*sql.append("select dm.DM_MandateAgreement_ID, dm2.documentno,dm.datetrx, dm.c_bpartner_id, bp.name, dm.amt-dm.allocatedamt, dm.description " +
					"from  DM_MandateAgreement dm " +
					" Inner join DM_Document dm2 on (dm2.DM_MandateAgreement_Id=dm.DM_MandateAgreement_ID )" +
					"left outer join c_bpartner bp on (dm.c_bpartner_id=bp.c_bpartner_id) " +
					" where dm.docstatus='CO' and dm2.docstatus='CO' and (dm.amt-dm.allocatedamt)>0");
			
			if(bPartnerLookup.getValue()!=null && (Integer)bPartnerLookup.getValue()>0)
				sql.append( " And dm.c_bpartner_id=" +(Integer)bPartnerLookup.getValue());
			
			if(OrgLookup.getValue()!=null)
				sql.append( " And dm.AD_Org_ID=" + (Integer)OrgLookup.getValue());
			
			if (documentNoField.getText().length() > 0)
				sql.append(" AND UPPER(dm.DocumentNo) LIKE '%" + documentNoField.getText() + "%'");
			
			if (dateFromField.getValue() != null || dateToField.getValue() != null)
			{
				Timestamp from = (Timestamp) dateFromField.getValue();
				Timestamp to = (Timestamp) dateToField.getValue();
				if (from == null && to != null)
					sql.append(" AND TRUNC(dm.datetrx) <= ?");
				else if (from != null && to == null)
					sql.append(" AND TRUNC(dm.datetrx) >= ?");
				else if (from != null && to != null)
					sql.append(" AND TRUNC(dm.datetrx) BETWEEN ? AND ?");
			}*/
			sql.append( " select i.c_invoice_id,i.documentno,i.dateinvoiced,i.c_bpartner_id,bp.name,invoiceOpen(i.c_invoice_id,null), i.description,0" +
					" from c_invoice i inner join c_bpartner bp on (i.c_bpartner_id=bp.c_bpartner_id) " +
					" where i.issotrx='N' and i.docstatus in ('CO','CL')");
			
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
						sql.append(" AND TRUNC(i.DateInvoiced) <= ?");
					else if (from != null && to == null)
						sql.append(" AND TRUNC(i.DateInvoiced) >= ?");
					else if (from != null && to != null)
						sql.append(" AND TRUNC(i.DateInvoiced) BETWEEN ? AND ?");
				}
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
				if( (type.equals("I") || type.equals("R") ) && rs.getBigDecimal(6).signum()==0)
					continue;
				
				Vector<Object> line = new Vector<Object>(10);
				
				KeyNamePair pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				
				KeyNamePair pp2 = new KeyNamePair(rs.getInt(4), rs.getString(5));
				
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(3));    //  1--Datetrx - Duedate
				
				//if (type.equals("I") || type.equals("J") || type.equals("R"))//ininoles setear BP solo si no es remuneraciones
				if (rs.getInt(4) < 1 || rs.getString(5) == null)
				{
					line.add(" ");
				}else
				{
					line.add(pp2);      //  2--BPartner
				}
				
				//if (type.equals("P"))
				//	line.add(" ");
				
				
				line.add(pp);     //3- Documentno
				
				if(type.equals("I"))
				{
					line.add(rs.getBigDecimal(6)); 		//4-amt
					line.add(rs.getString(7));     // 5 description
					line.add(" ");     // 6 description2
					line.add(" ");     // 7 descripcion3
					line.add(" ");     // 8 descripcion4
					if (rs.getString(8) == null || rs.getInt(8)==0)//se agrega manejo de cuota
					{
						line.add(0);
					}else
					{
						line.add(rs.getInt(8));
					}
					if(type.equals("I"))
						line.add(rs.getString(9));
					
				}
				if(type.equals("R"))
				{
					KeyNamePair pp3 = new KeyNamePair(rs.getInt(9), rs.getString(10));
					line.add(rs.getBigDecimal(6)); 		//4-amt
					line.add(rs.getString(7));     // 5 description
					line.add(pp3);     // 6 organización
					line.add(" ");     // 7 descripcion3
					line.add(" ");     // 8 descripcion4
					if (rs.getString(8) == null || rs.getInt(8)==0)//se agrega manejo de cuota
						line.add(0);
					else
						line.add(rs.getInt(8));
				}
				if(type.equals("J")) 
				{
					line.add(rs.getBigDecimal(6).signum()==0?rs.getBigDecimal(7):rs.getBigDecimal(6)); 		//4-amt
					line.add(rs.getString(8));     // 5 description
					line.add(" ");     // 6 descripcion2
					line.add(" ");     // 7 descripcion3
					line.add(" ");     // 8 descripcion4
					line.add(0);
				}
				if(type.equals("P"))//ininoles llenar campos para remuneraciones
				{					
					//line.add(rs.getBigDecimal(6).signum()==0?rs.getBigDecimal(7):rs.getBigDecimal(6)); 		//4-amt
					line.add(rs.getBigDecimal(7)); 		//4-amt
					line.add(rs.getString(8));     // 5 description
					line.add(rs.getString(9));     // 6 descripcion2
					line.add(rs.getString(10));     // 7 descripcion3
					line.add(rs.getString(11));     // 8 descripcion4
					line.add(0);
					
				}
				
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
