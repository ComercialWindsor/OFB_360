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
package org.qdc.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOut;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MProduct;
import org.compiere.model.X_OFB_ProductCost;
import org.compiere.model.MOrder;
import org.compiere.model.MInvoice;
import org.compiere.model.MConversionRate;
import org.compiere.model.MOrderLine;

/**
 *	
 *	
 *  @author mfrojas
 *  @version $Id: ProcessQDCGetCost.java $
 */
public class ProcessQDCGetCost extends SvrProcess
{
	//private String			p_DocStatus = null;
	private int				p_Product_ID = 0; 
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
				else if (name.equals("M_Product_ID"))
					p_Product_ID = ((BigDecimal)para[i].getParameter()).intValue();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
		 
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		String sql = "SELECT m_product_id from m_product where isactive='Y' ";
		if (p_Product_ID > 0)
		{
			sql = sql.concat(" AND m_product_id = "+p_Product_ID);
			
			
		}	
							
		//Se debe obtener y calcular el ultimo precio de compra de todos los productos. 
		log.config("sql producto = "+sql);
		PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next())
		{
			//Obtener ultimo precio de compra en una OC
			int product_id = rs.getInt(1);
			BigDecimal amount = Env.ZERO;
			
			int order_id = DB.getSQLValue(get_TrxName(), "SELECT coalesce(max(c_order_id),0) from c_order where " +
					" docstatus='CO' and issotrx='N' and c_order_id in ( SELECT " +
					" c_order_id from c_orderline where m_product_id = "+product_id+" and c_orderline_id in (" +
					" SELECT c_orderline_id from m_inoutline where m_inout_id in (select m_inout_id from m_inout " +
					" where docstatus='CO' and isactive='Y')))");
			if(order_id == 0)
				continue;
			
			int orderline_id = DB.getSQLValue(get_TrxName(), "SELECT c_orderline_id from c_orderline where " +
					" C_Order_ID = "+order_id+" AND m_product_id = "+product_id);
			
			MOrder ord = new MOrder(getCtx(), order_id , get_TrxName());
			MOrderLine ordl = new MOrderLine(getCtx(), orderline_id, get_TrxName());
			
			BigDecimal priceentered = ordl.getPriceEntered();
			
			//Ahora que se obtuvo el ultimo precio de compra, hay que ver la moneda de esa compra.
			
			
			
			int currency_id = ord.getC_Currency_ID();
			if(currency_id == 0)
				continue;
			
			if(currency_id != 228)
			{
				Timestamp dateorder = null;
				dateorder = ord.getDateOrdered();
				//Si la moneda es distinta del peso chileno, se debe obtener el valor de tasa de cambio de la moneda de la OC
				String sqlobtainrate = "SELECT coalesce(max(c_conversion_rate_id),0) FROM c_conversion_rate " +
						" WHERE validfrom = (select dateordered from c_order where c_order_id = "+order_id+") and isactive='Y' and c_currency_id = "+currency_id;
				//Actualizacion 20200102
				//Se debe obtener la tasa de cambio del recibo y no de la orden.
				
				//Primero: Obtener el ID del recibo
				String sqlobtainminout = "SELECT coalesce(max(m_inout_id),0) from m_inoutline where c_orderline_id = "+orderline_id;
				int minout_id = DB.getSQLValue(get_TrxName(), sqlobtainminout);
				if(minout_id > 0)
					sqlobtainrate = "SELECT coalesce(max(c_conversion_rate_id),0) FROM c_conversion_rate " +
							" WHERE validfrom = (select movementdate from m_inout where m_inout_id = "+minout_id+") and isactive='Y' and c_currency_id = "+currency_id;
				log.config("obtain rate "+sqlobtainrate);
				int rate_id = DB.getSQLValue(get_TrxName(), sqlobtainrate);
				if(rate_id == 0)
					continue;
				
				MConversionRate conv = new MConversionRate(getCtx(),rate_id,get_TrxName());
				amount = conv.getMultiplyRate().multiply(priceentered);
					
				//monto asociado a la compra, es amount en CLP
				
			}
			else
				amount = priceentered;
			
			//Existe orden, tiene recibo asociado, y se tiene el monto en peso chileno.
			
			//Ahora se debe ver si es que el producto tiene distribuciones de costo asociada.
			
			//String sqlgetinoutline = "SELECT m_inoutline_id from m_inoutline where c_orderline_id = "+orderline_id;
			
			String sqllanded = "SELECT coalesce(sum(amt/qty),0) from c_landedcostallocation where m_product_id = "+product_id+"and c_invoiceline_id in (select c_invoiceline_Id from c_landedcost" +
					" WHERE m_inout_id in (select m_inout_id from m_inoutline where c_orderline_id = ?))";
			BigDecimal amountlanded = DB.getSQLValueBD(get_TrxName(), sqllanded, orderline_id);
			
			//suma de valores 
			
			BigDecimal amountadd  = amount.add(amountlanded);
			
			//Ahora se buscará si hay registros asociados en ofbproductcost
			
			int last_id = DB.getSQLValue(get_TrxName(), "SELECT coalesce(max(ofb_productcost_id),0) from ofb_productcost where m_product_id = "+product_id);
			X_OFB_ProductCost po = new X_OFB_ProductCost(getCtx(),last_id, get_TrxName());
			
			if(last_id == 0)
				po.setM_Product_ID(product_id);
			
			BigDecimal currentcost = DB.getSQLValueBD(get_TrxName(), "SELECT max(currentcostprice) from m_cost where m_product_id = ?", product_id); 
			po.setDescription("Actualizado");
			//precio OC
			po.setAmount(amount);
			//distribucion costos
			po.setAmount2(amountlanded);
			//total (suma de ambos)
			po.setAmount3(amountadd);
			po.set_CustomColumn("C_Order_ID", order_id);
			po.set_CustomColumn("Amount4", currentcost);
			po.save();
			
		}
		
	   return "Procesado";
	}	//	doIt
}
