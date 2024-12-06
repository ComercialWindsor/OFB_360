/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.compiere.grid;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.table.*;

import org.compiere.apps.*;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Create Transactions for Payment Massive
 *
 *  @author Fabian aguilar
 *  @version  $Id: VCreateFromPaymentMassive.java,v 1.2 2009/11/15 00:51:28 $
 */
public class VCreateFromPaymentMassiveUI extends CreateFromPaymentMassive implements ActionListener, VetoableChangeListener
{
	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	private static final long serialVersionUID = 1L;
	
	private VCreateFromDialog dialog;
	
	public VCreateFromPaymentMassiveUI(GridTab mTab)
	{
		super (mTab);
		log.info("");
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
   
    private int AD_Client_ID=0;
    
    /**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
    public boolean dynInit() throws Exception
	{
    	
    	super.dynInit();

        
		AD_Client_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "AD_Client_ID");
		int AD_Org_ID=(Integer)getGridTab().getValue("AD_Org_ID");
		
		setTitle("Busqueda de Documentos");
		loadInvoices(AD_Org_ID);
		
		return true;
	}   //  dynInit

    private void jbInit() throws Exception
    {
    	
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

	private void loadInvoices (int AD_Org_ID)
	{
		log.config ("AD_Org_ID=" + AD_Org_ID);
		Timestamp ts2 = (Timestamp)getGridTab().getValue("DateTrx");
		if (ts2 == null)
			ts2 = new Timestamp(System.currentTimeMillis());
		Integer Currency_ID=(Integer)getGridTab().getValue("C_Currency_ID");
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		int C_PaymentMassive_ID=(Integer)getGridTab().getValue("C_PaymentMassive_ID");
		//int C_BPartner_ID=(Integer)getGridTab().getValue("C_BPartner_ID");
		
               String sql="";
        
               sql = "SELECT p.DateInvoiced,p.C_invoice_ID,p.DocumentNo,p.C_Currency_ID, p.GrandTotal,"
                       + " bp.Name,org.name as OrgName, doc.name as Tipo, "
                       + " currencyConvert(invoiceOpen(p.C_Invoice_ID,p.C_InvoicePaySchedule_ID),p.C_Currency_ID,?,?,p.C_ConversionType_ID,p.AD_Client_ID,p.AD_Org_ID)"
                       + " FROM C_Invoice_v p"
                       + " INNER JOIN AD_Org org ON (p.AD_Org_ID=org.AD_Org_ID)"
                       + " INNER JOIN C_BPartner bp ON (p.C_BPartner_ID=bp.C_BPartner_ID) "
                       + " Inner Join C_DocType doc ON (P.C_DocType_ID=doc.C_DocType_ID) "
                       + " WHERE p.Processed='Y' AND p.DocStatus='CO' and p.issotrx='N' and invoiceOpen(p.C_Invoice_ID,p.C_InvoicePaySchedule_ID)>0"
                       + " and p.AD_Client_ID="+AD_Client_ID;
               
               			if(AD_Org_ID!=0)
               				sql+=" and (p.AD_Org_ID=? Or p.AD_Org_ID IN (Select AD_Org_ID from AD_OrgInfo where parent_org_id=?))";
                    
               			sql+=" and p.C_Invoice_ID not In (select l.C_Invoice_ID from c_paymentMassiveLine l where  l.c_paymentmassive_id="+C_PaymentMassive_ID+")";
               			
               log.config ("**"+sql);

   
               try
               {
                       PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
                       pstmt.setInt(1,Currency_ID.intValue());
                       pstmt.setTimestamp(2, ts2);
                       if(AD_Org_ID!=0){
	                       pstmt.setInt(3, AD_Org_ID);
	                       pstmt.setInt(4, AD_Org_ID);
                       }
                       ResultSet rs = pstmt.executeQuery();
                       while (rs.next())
                       {
                               Vector<Object> line = new Vector<Object>(10);
                            
                               
	               			   line.add(new Boolean(false));       //  0-Selection
                               KeyNamePair pp = new KeyNamePair(rs.getInt(2), rs.getString(3));
                               line.add( rs.getString(8));                       //  1--Document Type
                               line.add(pp);                       //  2--C_invoice_ID
                               line.add(rs.getBigDecimal(5));      //  3-grandtotal
                               line.add(rs.getBigDecimal(9));      //  4-Open
                               line.add(rs.getTimestamp(1));       //  5-DateTrx                          
                               line.add(rs.getString(6));              //  6-BParner
                               line.add(rs.getString(7));                      // 7- Org
                             
                               data.add(line);
                       }
                       rs.close();
                       pstmt.close();
               }
               catch (SQLException e)
               {
                       log.log(Level.SEVERE, sql, e);
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
	}
	
	
	protected boolean validar()
	{
		return true;
	}
	
	public void showWindow()
	{
		dialog.setVisible(true);
	}
	
	public void closeWindow()
	{
		dialog.dispose();
	}
	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		log.config("Action=" + e.getActionCommand());
	}

}   //  VCreateFromStatement
