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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MRMA;
import org.compiere.model.MRMALine;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.X_C_PaymentMassive;
import org.compiere.model.X_C_PaymentMassiveLine;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 *  Create Invoice Transactions from PO Orders or Receipt
 *
 *  @author Fabian aguilar
 *  @version  $Id: CreateFromPaymentMassive.java,v 1.4 2006/07/30 00:51:28 jjanke Exp $
 *
 * 
 */
public class CreateFromPaymentMassive extends CreateFrom
{
	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	public CreateFromPaymentMassive(GridTab mTab)
	{
		super(mTab);
		log.info(mTab.toString());
	}   //  VCreateFromInvoice

	/**
	 *  Dynamic Init
	 *  @return true if initialized
	 */
	public boolean dynInit() throws Exception
	{
		log.config("");
		setTitle("Pago Masivo");

		return true;
	}   //  dynInit


	/**
	 *  Load PBartner dependent Order/Invoice/Shipment Field.
	 *  @param C_BPartner_ID BPartner
	 */
	protected ArrayList<KeyNamePair> loadRMAData(int C_BPartner_ID) {
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();

		String sqlStmt = "SELECT r.M_RMA_ID, r.DocumentNo || '-' || r.Amt from M_RMA r "
				+ "WHERE ISSOTRX='N' AND r.DocStatus in ('CO', 'CL') "
				+ "AND r.C_BPartner_ID=? "
				+ "AND NOT EXISTS (SELECT * FROM C_Invoice inv "
				+ "WHERE inv.M_RMA_ID=r.M_RMA_ID AND inv.DocStatus IN ('CO', 'CL'))";

		PreparedStatement pstmt = null;
		try {
			pstmt = DB.prepareStatement(sqlStmt, null);
			pstmt.setInt(1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlStmt.toString(), e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception ex) {
					log.severe("Could not close prepared statement");
				}
			}
		}

		return list;
	}

	/**
	 *  List number of rows selected
	 */
	public void info()
	{

	}   //  infoInvoice

	protected void configureMiniTable (IMiniTable miniTable)
	{
		//
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, String.class, true);       //   1--Document Type
		miniTable.setColumnClass(2, String.class, true);        //  2-Invoice
		miniTable.setColumnClass(3, BigDecimal.class, true);    //  3-Amount
		miniTable.setColumnClass(4, BigDecimal.class, true);   //4-Open
		miniTable.setColumnClass(5, Timestamp.class, true);     //  5-TrxDate
		miniTable.setColumnClass(6, String.class, true);        //  6-BPartner
		miniTable.setColumnClass(7, String.class, true);        //  7-Org
		
		miniTable.autoSize();
	}

	/**
	 *  Save - Create Invoice Lines
	 *  @return true if saved
	 */
	public boolean save(IMiniTable miniTable, String trxName)
	{
		log.config("");
		int rows = miniTable.getRowCount();
		if (rows == 0)
			return false;
		BigDecimal TotalAmt=new BigDecimal("0.0");
		X_C_PaymentMassive massive= new X_C_PaymentMassive(Env.getCtx(),((Integer)getGridTab().getValue("C_PaymentMassive_ID")).intValue(),null);
		
		//  Lines
		for (int i = 0; i < miniTable.getRowCount(); i++)
		{
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
			{
				int C_Invoice_ID=0;
				KeyNamePair pp = (KeyNamePair)miniTable.getValueAt(i, 2);   //  2-C_Invoice_ID
				C_Invoice_ID= pp.getKey();
				BigDecimal openAmt=(BigDecimal)miniTable.getValueAt(i, 4);
					
				X_C_PaymentMassiveLine line = new X_C_PaymentMassiveLine(Env.getCtx(),0,null);
				line.setC_Invoice_ID(C_Invoice_ID);
				line.setAD_Org_ID(massive.getAD_Org_ID());
				line.setPayAmt(openAmt);
				line.setC_PaymentMassive_ID(massive.getC_PaymentMassive_ID());
				line.saveEx();
				TotalAmt=TotalAmt.add(openAmt);
			}   //   if selected
		}   //  for all rows
		TotalAmt=DB.getSQLValueBD(null, "select sum(payamt) from C_PaymentMassiveLine where C_PaymentMassive_ID="+massive.getC_PaymentMassive_ID());
		massive.setPayAmt(TotalAmt);
		massive.saveEx();
		return true;
	}   //  saveInvoice

	protected Vector<String> getOISColumnNames()
	{
	//  Header Info
        Vector<String> columnNames = new Vector<String>(10);
        columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
        columnNames.add("Documento");
        columnNames.add(Msg.getElement(Env.getCtx(), "C_Invoice_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "GrandTotal"));
        columnNames.add("Pendiente");
        columnNames.add(Msg.translate(Env.getCtx(), "Date"));
        columnNames.add(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
        columnNames.add(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		
        return columnNames;
	}

}
