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
import org.compiere.model.X_C_PaymentRequest;
import org.compiere.model.X_C_PaymentRequestLine;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 *
 *  @author Fabian Aguilar
 *  @version  $Id: CreateFromPaymentRequest.java,v 1.4 2012/01/02 00:51:28 $
 *
 */
public class CreateFromPaymentRequest extends CreateFrom
{
	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	
	public CreateFromPaymentRequest(GridTab mTab)
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
		setTitle(Msg.getElement(Env.getCtx(), "C_Invoice_ID", false) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));

		return true;
	}   //  dynInit


	

	/**
	 *  List number of rows selected
	 */
	public void info()
	{

	}   //  infoInvoice

	protected void configureMiniTable (IMiniTable miniTable)
	{
		String type2 = getGridTab().get_ValueAsString(X_C_PaymentRequest.COLUMNNAME_RequestType);
		//ininoles tomar solo el primer caracter del tipo de solicitud
		type2 = Character.toString(type2.charAt(0));
		
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, Timestamp.class, true);        //  1--Datetrx
		miniTable.setColumnClass(2, String.class, true);    //  2-BPartner
		miniTable.setColumnClass(3, String.class, true);		//  3- Documentno
		miniTable.setColumnClass(4, BigDecimal.class, true);     //  4-price
		miniTable.setColumnClass(5, String.class, true);    //  5-description
		miniTable.setColumnClass(6, String.class, true);    //  6-organizacion ininoles
		miniTable.setColumnClass(7, String.class, true);    //  7-proyecto ininoles
		miniTable.setColumnClass(8, String.class, true);    //  8-combinacion ininoles
		miniTable.setColumnClass(9, int.class, true);    //  9-cuota ininoles
		if(type2.equals("I"))
			miniTable.setColumnClass(10, String.class, true);    //  10-termino de pago
		
		//  Table UI
		miniTable.autoSize();
	}

	/**
	 *  Save - Create Invoice Lines
	 *  @return true if saved
	 */
	public boolean save(IMiniTable miniTable, String trxName)
	{		
		int header_ID=((Integer)getGridTab().getValue("C_PaymentRequest_ID")).intValue();
		String type = getGridTab().get_ValueAsString(X_C_PaymentRequest.COLUMNNAME_RequestType);
		//ininoles tomar solo el primer caracter del tipo de solicitud
		type = Character.toString(type.charAt(0));
		
		BigDecimal total = new BigDecimal(0);
  
		//  Lines
		for (int i = 0; i < miniTable.getRowCount(); i++)
		{
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
			{
				
				X_C_PaymentRequestLine line = new X_C_PaymentRequestLine(Env.getCtx(), 0, trxName);
				KeyNamePair document = (KeyNamePair)miniTable.getValueAt(i, 3);
				
				BigDecimal amt = (BigDecimal)miniTable.getValueAt(i, 4);
				String desc = (String)miniTable.getValueAt(i, 5);
				int Schedule_ID =  (Integer)miniTable.getValueAt(i, 9);
				
				line.setC_PaymentRequest_ID(header_ID);
				line.setAmt(amt);
				line.setDescription(desc);
				
				if(type.equals("I"))
					line.setC_Invoice_ID(document.getKey());
					
				if(type.equals("J") || type.equals("P"))
				    line.setGL_JournalLine_ID(document.getKey());
				
				if (type.equals("I") && Schedule_ID > 0)					
					line.set_CustomColumn("C_InvoicePaySchedule_ID",Schedule_ID);
				
				if(type.equals("R"))
					line.set_CustomColumn("DM_Document_ID", document.getKey());
				
			    line.save();
			    
			    total = total.add(amt);
				
				
			}   //   if selected
		}   //  for all rows
		
		return true;
	}   //  saveInvoice

	protected Vector<String> getOISColumnNames()
	{
		String type2 = getGridTab().get_ValueAsString(X_C_PaymentRequest.COLUMNNAME_RequestType);
		//ininoles tomar solo el primer caracter del tipo de solicitud
		type2 = Character.toString(type2.charAt(0));
		//  Header Info
		Vector<String> columnNames = new Vector<String>(10);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.getElement(Env.getCtx(), "DateTrx"));
		columnNames.add(Msg.getElement(Env.getCtx(), "C_BPartner_ID"));
		columnNames.add(Msg.getElement(Env.getCtx(), "DocumentNo"));
		columnNames.add(Msg.getElement(Env.getCtx(), "Amt"));
		columnNames.add(Msg.translate(Env.getCtx(), "Description"));
		columnNames.add(Msg.translate(Env.getCtx(), "Organización"));
		columnNames.add(Msg.translate(Env.getCtx(), "Proyecto"));
		columnNames.add(Msg.translate(Env.getCtx(), "Combinación"));
		columnNames.add(Msg.translate(Env.getCtx(), "Cuota"));
		if(type2.equals("I"))			
			columnNames.add("Termino de pago");
		
	    return columnNames;
	}

}
