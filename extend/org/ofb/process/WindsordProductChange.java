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

package org.ofb.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.*;

/**
 *	
 *	
 *  @author faaguilar OFB
 *  @version $Id: WindsordProductChange.java,v 1.2 2011/06/12 00:51:01  $
 */

public class WindsordProductChange extends SvrProcess 
{
	private int p_Invoice_ID = 0;
	private int p_InvoiceLine_ID = 0;
	private int p_NewProduct_ID = 0;
	private BigDecimal p_NewQty;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Invoice_ID"))
				p_Invoice_ID = para[i].getParameterAsInt();
			else if (name.equals("C_InvoiceLine_ID"))
				p_InvoiceLine_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_ID"))
				p_NewProduct_ID = para[i].getParameterAsInt();
			else if (name.equals("QtyInvoiced"))
				p_NewQty = (BigDecimal)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	
	protected String doIt() throws Exception
	{
		if(p_Invoice_ID<=0 || p_InvoiceLine_ID<=0 || p_NewProduct_ID<=0)
			throw new AdempiereException("Parametros Incompletos");
		
		if(p_NewQty==null || p_NewQty.signum()==0)
			throw new AdempiereException("la cantidad no puede ser 0");
		
		//verificacion de Precio
		MInvoice invoice = new MInvoice(getCtx(),p_Invoice_ID, get_TrxName());
		if(!invoice.getDocStatus().equals("CO") && !invoice.getDocStatus().equals("CL"))
			throw new AdempiereException("la boleta / factura no posee un estado valido");
		
		
		MInvoiceLine line = new MInvoiceLine(getCtx(),p_InvoiceLine_ID, get_TrxName());
		
		if(p_NewQty.compareTo(line.getQtyInvoiced())>0)
			throw new AdempiereException("Error - La cantidad supera la cantidad original");

		int inOutLine_ID = DB.getSQLValue(get_TrxName(), "Select M_InOutLine_ID from M_InOutLine where C_OrderLine_ID=?", line.getC_OrderLine_ID());
		
		if(inOutLine_ID<=0)
			throw new AdempiereException("no se puede encontrar la ENTREGA de la boleta");
		
		MInOutLine  out = new MInOutLine(getCtx(),inOutLine_ID, get_TrxName());
		
		MProductPricing pp = new MProductPricing (p_NewProduct_ID, invoice.getC_BPartner_ID(), line.getQtyInvoiced(), invoice.isSOTrx());
		pp.setM_PriceList_ID(invoice.getM_PriceList_ID());
		int M_PriceList_Version_ID = 0;
		
		if ( M_PriceList_Version_ID == 0 )
		{
			String sql = "SELECT plv.M_PriceList_Version_ID "
				+ "FROM M_PriceList_Version plv "
				+ "WHERE plv.M_PriceList_ID=? "						//	1
				+ " AND plv.ValidFrom <= ? "
				+ "ORDER BY plv.ValidFrom DESC";
			//	Use newest price list - may not be future
			
			M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, invoice.getM_PriceList_ID(), invoice.getDateOrdered());
			
		}
		pp.setM_PriceList_Version_ID(M_PriceList_Version_ID); 
		pp.setPriceDate(invoice.getDateOrdered());
		
		if(pp.getPriceStd().compareTo(line.getPriceActual())!=0)
			throw new AdempiereException("Productos de Distinto Precio - Error : Nuevo: "+pp.getPriceStd() + " - Original: "+line.getPriceActual());
		
		//Ejecutar Actualizacion de Stock - 
		
		Timestamp MovementDate = TimeUtil.getDay(null);
		
		//inventario entrada - salida
		MWarehouse ware = MWarehouse.get(getCtx(), invoice.getC_Order().getM_Warehouse_ID());
		MInventory inventory = new MInventory(getCtx(),0, get_TrxName());
		inventory.setDescription("Devolucion Cambio Producto Boleta Mismo Precio :" + invoice.getDocumentNo());
		inventory.setClientOrg(invoice.getAD_Client_ID(), invoice.getAD_Org_ID());
		inventory.setM_Warehouse_ID(ware.getM_Warehouse_ID());
		inventory.setMovementDate(MovementDate);
		if (!inventory.save())
		{
			log.log(Level.SEVERE, "Inventory not saved");
			throw new AdempiereException("No se puede actualizar el inventario");
		}
		
		MInventoryLine iline1 = new MInventoryLine (inventory, 
				out.getM_Locator_ID(), out.getM_Product_ID(),out.getM_AttributeSetInstance_ID(),
				Env.ZERO, p_NewQty);
		iline1.save();
		
		MInventoryLine iline2 = new MInventoryLine (inventory, 
				ware.getDefaultLocator().getM_Locator_ID(), p_NewProduct_ID,0,
				p_NewQty,Env.ZERO);
		iline2.save();
		
		line.addDescription("*Producto Cambiado* por:"+ MProduct.get(getCtx(), p_NewProduct_ID).getName());
		line.save();
		
		inventory.processIt("CO");
		inventory.setProcessed(true);
		inventory.save();
		
		
		return "Producto Cambiado, Inventario Actualizado";
		
	}
		
}
