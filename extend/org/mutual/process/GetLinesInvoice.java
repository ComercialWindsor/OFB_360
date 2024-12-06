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
package org.mutual.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class GetLinesInvoice extends SvrProcess
{	
	private int			ID_Request = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 ID_Request = getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MInvoice inv = new MInvoice(getCtx(), ID_Request, get_TrxName());
		int id_invoice = 0;
		int cant = 0;
		if (inv.get_ValueAsInt("Ref_Invoice_ID") > 0)
			id_invoice = inv.get_ValueAsInt("Ref_Invoice_ID");
		else if (inv.get_ValueAsInt("Ref_InvoiceRequest_ID") > 0)
			id_invoice = inv.get_ValueAsInt("Ref_InvoiceRequest_ID");
		
		String sql = "SELECT C_InvoiceLine_ID FROM C_InvoiceLine WHERE C_Invoice_ID = "+id_invoice
				+ " AND IsUseMutualSP = 'N' ORDER BY Line";
		
		PreparedStatement pstmt = null;
		 ResultSet rs = null;
		 try
		 {
			pstmt = DB.prepareStatement(sql, this.get_TrxName());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MInvoiceLine iLine = new MInvoiceLine(getCtx(), rs.getInt("C_InvoiceLine_ID"), get_TrxName());
				MInvoiceLine iLineTo = new MInvoiceLine(getCtx(), 0, get_TrxName());
				iLineTo.setC_Invoice_ID(inv.get_ID());
				iLineTo.setAD_Org_ID(iLine.getAD_Org_ID());
				if (inv.get_ValueAsInt("Ref_Invoice_ID") > 0)
					iLineTo.set_CustomColumn("Ref_InvoiceLine_ID", iLine.get_ID());
				else if (inv.get_ValueAsInt("Ref_InvoiceRequest_ID") > 0)
					iLineTo.set_CustomColumn("Ref_InvoiceReqLine_ID", iLine.get_ID());				
				iLineTo.setM_Product_ID(iLine.getM_Product_ID());					
				//validacion cargo
				if (iLine.getC_Charge_ID() > 0)
					iLineTo.setC_Charge_ID(iLine.getC_Charge_ID());
				iLineTo.setM_AttributeSetInstance_ID(iLine.getM_AttributeSetInstance_ID());					
				//ininoles calculamos saldo de la linea
				/*BigDecimal qty = DB.getSQLValueBD(null, "SELECT SUM(QtyEntered) FROM C_InvoiceLine cil" +
						" INNER JOIN C_Invoice ci ON (cil.C_Invoice_ID = ci.C_Invoice_ID)" +
						" WHERE cil.Ref_InvoiceLine_ID = ? AND ci.DocStatus IN ('CO','DR','IP')", iLine.get_ID());
				//BigDecimal qty = DB.getSQLValueBD(null, "SELECT SUM(QtyEntered) FROM C_InvoiceLine WHERE Ref_InvoiceLine_ID = ?", iLine.get_ID());
				if (qty == null)
					qty = Env.ZERO;
				if (qty.compareTo(Env.ZERO) == 0)
				{*/
				iLineTo.setQtyEntered(iLine.getQtyEntered());
				iLineTo.setQtyInvoiced(iLine.getQtyEntered());
				/*}
				else
				{
					iLineTo.setQtyEntered(iLine.getQtyEntered().subtract(qty));
					iLineTo.setQtyInvoiced(iLine.getQtyEntered().subtract(qty));
				}	*/
				iLineTo.setDescription(iLine.getDescription());
				iLineTo.setC_UOM_ID(iLine.getC_UOM_ID());
				/*iLineTo.setPriceEntered(iLine.getPriceEntered());
				iLineTo.setPriceActual(iLine.getPriceActual());
				iLineTo.setPriceList(iLine.getPriceList());*/
				iLineTo.setC_Tax_ID(iLine.getC_Tax_ID());
				iLineTo.setPrice(iLine.getPriceEntered());
				//iLineTo.setLineNetAmt(iLine.getPriceEntered().multiply(iLineTo.getQtyEntered()));
				iLineTo.setLineNetAmt();
				iLineTo.save();
				//ininles end
				cant++;
			} 
		 }
		 catch (Exception e)
		 {
			log.severe(e.getMessage());
		 }
		 finally{
			 DB.close(rs, pstmt);
		 	rs=null;pstmt=null;
		 }
	   return "Agregadas "+cant+ " Lineas";
	}	//	doIt
}
