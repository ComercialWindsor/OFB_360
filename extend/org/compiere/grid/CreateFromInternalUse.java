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
import org.compiere.model.MInventoryLine;
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
 *  @author italo niñoles
 *  @version  $Id: CreateFromInternalUse.java,v 1.4 2013/09/03 $
 *
 */
public class CreateFromInternalUse extends CreateFrom
{
	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	public CreateFromInternalUse(GridTab mTab)
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
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, Timestamp.class, true);        //  1--Datetrx
		miniTable.setColumnClass(2, String.class, true);    //  2-BPartner
		miniTable.setColumnClass(3, String.class, true);		//  3- Documentno
		miniTable.setColumnClass(4, String.class, true);     //  4-DocumentType
		miniTable.setColumnClass(5, String.class, true);    //  5-Producto
		miniTable.setColumnClass(6, BigDecimal.class, true);    //  6-Qty
		miniTable.setColumnClass(7, String.class, true);    //  7-Description
		miniTable.setColumnClass(8, String.class, true);    //  8-Locator
		miniTable.setColumnClass(9, int.class, true);    //  9-AttributeSetInstance	
		
		//  Table UI
		miniTable.autoSize();
	}

	/**
	 *  Save - Create Invoice Lines
	 *  @return true if saved
	 */
	public boolean save(IMiniTable miniTable, String trxName)
	{
		
		int header_ID=((Integer)getGridTab().getValue("M_Inventory_ID")).intValue();
		String type = getGridTab().get_ValueAsString("TypeInventory");
		//ininoles tomar solo el primer caracter del tipo de solicitud
		type = Character.toString(type.charAt(0));
		  
		//  Lines
		for (int i = 0; i < miniTable.getRowCount(); i++)
		{
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
			{
				
				MInventoryLine line = new MInventoryLine(Env.getCtx(), 0, trxName);
				KeyNamePair document = (KeyNamePair)miniTable.getValueAt(i, 3);
				
				KeyNamePair prod = (KeyNamePair)miniTable.getValueAt(i, 5); //product
				KeyNamePair loc = (KeyNamePair)miniTable.getValueAt(i, 8); //locator
				int atr = (Integer)miniTable.getValueAt(i, 9); //AttributeSetInstance
				String desc = (String)miniTable.getValueAt(i, 7); //description
				BigDecimal qty = (BigDecimal)miniTable.getValueAt(i, 6); //qty
				//cargo por defecto
				String sql = "SELECT MAX(C_Charge_ID) FROM c_charge where tipocargo = 'TC12'";
				int chargeID = DB.getSQLValue(null, sql);
				
				line.setM_Inventory_ID(header_ID);
				if (loc.getKey() > 0)
					line.setM_Locator_ID(loc.getKey());
				line.setM_Product_ID(prod.getKey());
				if (atr > 0 )
					line.setM_AttributeSetInstance_ID(atr);
				line.setDescription(desc);
				line.setQtyInternalUse(qty);
				line.setC_Charge_ID(chargeID);
								
				if(type.equals("R"))
					line.set_CustomColumn("M_InOutLIne_ID",document.getKey());
					
				if(type.equals("O"))
					line.set_CustomColumn("MP_OT_Resource_ID",document.getKey());
				
			    line.save();				
				
			}   //   if selected
		}   //  for all rows
		
		return true;
	}   //  saveInvoice

	protected Vector<String> getOISColumnNames()
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>(10);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.getElement(Env.getCtx(), "DateTrx"));
		columnNames.add(Msg.getElement(Env.getCtx(), "C_BPartner_ID"));
		columnNames.add(Msg.getElement(Env.getCtx(), "DocumentNo"));
		columnNames.add(Msg.getElement(Env.getCtx(), "DocumentType"));
		columnNames.add(Msg.translate(Env.getCtx(), "Producto"));
		columnNames.add(Msg.translate(Env.getCtx(), "Qty"));
		columnNames.add(Msg.translate(Env.getCtx(), "Description"));
		columnNames.add(Msg.translate(Env.getCtx(), "Locator"));
		columnNames.add(Msg.translate(Env.getCtx(), "AttributeSetInstance"));
		
	    return columnNames;
	}

}
