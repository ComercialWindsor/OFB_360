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
package org.compiere.process;

import java.util.logging.Level;

import org.compiere.model.I_C_OrderLine;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.util.DB;
 
/**
 *	Create (Generate) Invoice from Shipment
 *	
 *  @author Jorg Janke
 *  @version $Id: InOutCreateInvoice.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class InOutCreateInvoice extends SvrProcess
{
	/**	Shipment					*/
	private int 	p_M_InOut_ID = 0;
	/**	Price List Version			*/
	private int		p_M_PriceList_ID = 0;
	/* Document No					*/
	private String	p_InvoiceDocumentNo = null;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_PriceList_ID"))
				p_M_PriceList_ID = para[i].getParameterAsInt();
			else if (name.equals("InvoiceDocumentNo"))
				p_InvoiceDocumentNo = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_M_InOut_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Create Invoice.
	 *	@return document no
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("M_InOut_ID=" + p_M_InOut_ID 
			+ ", M_PriceList_ID=" + p_M_PriceList_ID
			+ ", InvoiceDocumentNo=" + p_InvoiceDocumentNo);
		if (p_M_InOut_ID == 0)
			throw new IllegalArgumentException("No Shipment");
		//
		MInOut ship = new MInOut (getCtx(), p_M_InOut_ID, null);
		if (ship.get_ID() == 0)
			throw new IllegalArgumentException("Shipment not found");
		if (!MInOut.DOCSTATUS_Completed.equals(ship.getDocStatus()) && !MInOut.DOCSTATUS_Closed.equals(ship.getDocStatus()))//ininoles faaguilar OFB
			throw new IllegalArgumentException("Shipment not completed");
		
		if(ship.getC_Order_ID()==0)//faaguilar OFB
			throw new IllegalArgumentException("No Order");//faaguilar OFB
		
		//faaguilar OFB begin
		//agregar validacion en este punto para mas de 1 factura ininoles OFB
		String m_sql = "select count(c_invoiceline_id) from c_invoiceline cil " +
				"inner join m_inoutline mil on (cil.m_inoutline_id = mil.m_inoutline_id) " +
				"inner join c_invoice ci on (cil.c_invoice_id = ci.c_invoice_id)" +
				"where ci.docstatus <> 'VO' and mil.m_inout_id =?";
		int invoicecount = DB.getSQLValue(get_TrxName(), m_sql, p_M_InOut_ID);
		
		if(invoicecount > 0)
			throw new IllegalArgumentException("Invoice already exists");
		//faaguilar OFB end
		
		MOrder order = new MOrder(getCtx(),ship.getC_Order_ID(),this.get_TrxName()); //faaguilar OFB
		
		MInvoice invoice = new MInvoice (ship, null);
		// Should not override pricelist for RMA
		//if (p_M_PriceList_ID != 0 && ship.getM_RMA_ID() == 0)
			invoice.setM_PriceList_ID(order.getM_PriceList_ID());//faaguilar OFB
			
		if (p_InvoiceDocumentNo != null && p_InvoiceDocumentNo.length() > 0)
			invoice.setDocumentNo(p_InvoiceDocumentNo);
		if (!invoice.save())
			throw new IllegalArgumentException("Cannot save Invoice");
		
		MInOutLine[] shipLines = ship.getLines(false);
		for (int i = 0; i < shipLines.length; i++)
		{
			MInOutLine sLine = shipLines[i];
			
			if(sLine.getC_OrderLine_ID()==0)//faaguilar OFB
				throw new IllegalArgumentException("Una de las lineas no se relaciona con una Orden");//faaguilar OFB
			
			I_C_OrderLine oline = sLine.getC_OrderLine(); //faaguilar OFB
			
			MInvoiceLine line = new MInvoiceLine(invoice);
			line.setShipLine(sLine);
			line.setOrderLine((MOrderLine) oline);//faaguilar OFB
			
			if (sLine.sameOrderLineUOM())
				line.setQtyEntered(sLine.getQtyEntered());
			else
				line.setQtyEntered(sLine.getMovementQty());
			line.setQtyInvoiced(sLine.getMovementQty());
			
			line.setPriceActual(oline.getPriceActual());//faaguilar OFB
			line.setPriceEntered(oline.getPriceEntered());//faaguilar OFB
			
			
			if (!line.save())
				throw new IllegalArgumentException("Cannot save Invoice Line");
		}
		
		return invoice.getDocumentNo();
	}	//	InOutCreateInvoice
	
}	//	InOutCreateInvoice
