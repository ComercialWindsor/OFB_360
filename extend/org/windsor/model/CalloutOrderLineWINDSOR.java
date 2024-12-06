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
package org.windsor.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Properties;
import org.compiere.model.*;

import java.sql.ResultSet;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *	mutual Callouts.
 *	
 *  @author Italo Niï¿½oles
 *  @version $Id: CalloutInvoiceRequisition.java,v 1.5 2015/10/25 
 */
public class CalloutOrderLineWINDSOR extends CalloutEngine
{
	/**
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @return Error message or ""
	 */
	public String OrderLineWH (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		int ID_Product = (Integer)value;
		if(ID_Product > 0)
		{			
			MProduct prod = new MProduct(ctx, ID_Product, null);
			if(prod.get_Value("WinHeight") != null)
			mTab.setValue("WinHeight", prod.get_Value("WinHeight"));
			if(prod.get_Value("WinWidth") != null)
			mTab.setValue("WinWidth", prod.get_Value("WinWidth"));
		}	
		return "";
    }
	public String qtyDisponible (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(mTab.getValue("M_RequisitionLine_ID") == null)
		{		
			PreparedStatement pstmt=null;
			
			Integer client_id=(Integer)mTab.getValue("AD_Client_ID");
			Integer product_id=(Integer)mTab.getValue("M_Product_ID");
			Integer disponible;
			disponible=(Integer)0;
			String resultStr = "";
			if (client_id==1000000)
			{
				if(product_id!=null && product_id!=0 )
				{
					String sql="SELECT " + // qtyavailableofb(?,1000001)+qtyavailableofb(?,1000010) 
							
" COALESCE ( "+
"         (SELECT SUM (s.qtyonhand) "+
 "           FROM rv_storage s "+
  "         WHERE     s.M_Product_ID = p.m_product_id "+
   "              AND s.m_warehouse_id IN (1000001, 1000010,1000033) "+
    "             AND s.isactive = 'Y'), "+
     "    0) "+
    " - (  (SELECT COALESCE (SUM (ol2.qtyreserved), 0)      "+
   "         FROM C_orderline ol2      "+
    "             INNER JOIN C_Order o2  "+
     "               ON (ol2.C_ORDER_ID = o2.c_order_ID)  "+
      "     WHERE     ol2.M_Product_ID = p.m_product_id "+
       "          AND o2.m_warehouse_id = 1000001 "+
        "         AND o2.saldada <> 'Y' "+
         "        AND o2.docstatus IN ('IP', 'CO', 'CL') "+
          "       AND o2.issotrx = 'Y' "+
           "      AND o2.c_doctypetarget_ID NOT IN "+
            "            (1000110, 1000048, 1000568)) "+
      " + (SELECT COALESCE (SUM (rl.qtyreserved), 0)     "+
       "     FROM M_Requisitionline rl     "+
        "         INNER JOIN M_Requisition r  "+
         "           ON (rl.M_Requisition_ID = r.M_Requisition_ID)  "+
          " WHERE     rl.M_Product_ID = p.m_product_id  "+
           "      AND r.m_warehouse_id = 1000001  "+
            "     AND r.docstatus IN ('CO', 'CL')  "+
             "    AND r.issotrx = 'Y'))  "+
             " as suma FROM m_product p where p.m_product_ID=? or p.m_product_ID=?"	
							
							
							;
					resultStr=" Producto:"+product_id;
					try {
						pstmt=DB.prepareStatement(sql,null);
						pstmt.setInt(1,product_id);
						pstmt.setInt(2,product_id);
						ResultSet rs= pstmt.executeQuery();
						if(rs.next())
						{
							disponible= rs.getInt("suma");     
							resultStr=resultStr+" disponible: "+ disponible;
						}
						BigDecimal qtyBD = (BigDecimal)mTab.getValue("QtyEntered");
						Integer qty=qtyBD.intValue();
						mTab.setValue("Disponible",disponible);
						if(qty>disponible)
						{
							mTab.setValue("SINDISPONIBLE",'Y');
						}
						if(qty<=disponible)
						{
							mTab.setValue("SINDISPONIBLE",'N');
						}
						if(resultStr != null && resultStr.trim().length() > 3)
							mTab.fireDataStatusEEvent (resultStr, "Validación de Stock", true);
					} catch (Exception e) {
						log.config("Error: "+e.toString());
					}
				} 
			}   
		}
		return "";
	}
	public String qtyW (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		int M_Product_ID = Env.getContextAsInt(ctx, WindowNo, "M_Product_ID");
		BigDecimal QtyOrdered = Env.ZERO;
		BigDecimal QtyEntered, PriceActual, PriceEntered;
		
		//	No Product
		if (M_Product_ID == 0)
		{
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			QtyOrdered = QtyEntered;
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		//	UOM Changed - convert from Entered -> Product
		else if (mField.getColumnName().equals("C_UOM_ID"))
		{
			int C_UOM_To_ID = ((Integer)value).intValue();
			QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID 
					+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);  
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			PriceActual = (BigDecimal)mTab.getValue("PriceActual");
			PriceEntered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, PriceActual);
			if (PriceEntered == null)
				PriceEntered = PriceActual; 
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", QtyEntered/PriceActual=" + QtyEntered + "/" + PriceActual
				+ " -> " + conversion 
				+ " QtyOrdered/PriceEntered=" + QtyOrdered + "/" + PriceEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
			mTab.setValue("PriceEntered", PriceEntered);
		}
		//	QtyEntered changed - calculate QtyOrdered
		else if (mField.getColumnName().equals("QtyEntered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyEntered = (BigDecimal)value;
			BigDecimal QtyEntered1 = QtyEntered.setScale(MUOM.getPrecision(ctx, C_UOM_To_ID), BigDecimal.ROUND_HALF_UP);
			if (QtyEntered.compareTo(QtyEntered1) != 0)
			{
				log.fine("Corrected QtyEntered Scale UOM=" + C_UOM_To_ID 
					+ "; QtyEntered=" + QtyEntered + "->" + QtyEntered1);  
				QtyEntered = QtyEntered1;
				mTab.setValue("QtyEntered", QtyEntered);
			}
			QtyOrdered = MUOMConversion.convertProductFrom (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyEntered);
			if (QtyOrdered == null)
				QtyOrdered = QtyEntered;
			boolean conversion = QtyEntered.compareTo(QtyOrdered) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", QtyEntered=" + QtyEntered
				+ " -> " + conversion 
				+ " QtyOrdered=" + QtyOrdered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyOrdered", QtyOrdered);
		}
		//	QtyOrdered changed - calculate QtyEntered (should not happen)
		else if (mField.getColumnName().equals("QtyOrdered"))
		{
			int C_UOM_To_ID = Env.getContextAsInt(ctx, WindowNo, "C_UOM_ID");
			QtyOrdered = (BigDecimal)value;
			int precision = MProduct.get(ctx, M_Product_ID).getUOMPrecision(); 
			BigDecimal QtyOrdered1 = QtyOrdered.setScale(precision, BigDecimal.ROUND_HALF_UP);
			if (QtyOrdered.compareTo(QtyOrdered1) != 0)
			{
				log.fine("Corrected QtyOrdered Scale " 
					+ QtyOrdered + "->" + QtyOrdered1);  
				QtyOrdered = QtyOrdered1;
				mTab.setValue("QtyOrdered", QtyOrdered);
			}
			QtyEntered = MUOMConversion.convertProductTo (ctx, M_Product_ID, 
				C_UOM_To_ID, QtyOrdered);
			if (QtyEntered == null)
				QtyEntered = QtyOrdered;
			boolean conversion = QtyOrdered.compareTo(QtyEntered) != 0;
			log.fine("UOM=" + C_UOM_To_ID 
				+ ", QtyOrdered=" + QtyOrdered
				+ " -> " + conversion 
				+ " QtyEntered=" + QtyEntered);
			Env.setContext(ctx, WindowNo, "UOMConversion", conversion ? "Y" : "N");
			mTab.setValue("QtyEntered", QtyEntered);
		}
		else
		{
		//	QtyEntered = (BigDecimal)mTab.getValue("QtyEntered");
			QtyOrdered = (BigDecimal)mTab.getValue("QtyOrdered");
		}
		
		//	Storage
		if (M_Product_ID != 0 
			&& Env.isSOTrx(ctx, WindowNo)
			&& QtyOrdered.signum() > 0)		//	no negative (returns)
		{
			MProduct product = MProduct.get (ctx, M_Product_ID);
			if (product.isStocked() && product.get_ValueAsBoolean("CheckQty") && product.getAD_Client_ID() != 1000000)//faaguilar OFB add && product.get_ValueAsBoolean("CheckQty")
			{
				int M_Warehouse_ID = Env.getContextAsInt(ctx, WindowNo, "M_Warehouse_ID");
				int M_AttributeSetInstance_ID = Env.getContextAsInt(ctx, WindowNo, "M_AttributeSetInstance_ID");
				
				//faaguilar OFB begin
				
				BigDecimal available = Env.ZERO;
				String whereClause = "AD_Client_ID=?";
				List<MWarehouse> list = new Query(ctx, MWarehouse.Table_Name, whereClause, null)
				.setParameters(product.getAD_Client_ID())
				.setOnlyActiveRecords(true)
				.setOrderBy(MWarehouse.COLUMNNAME_M_Warehouse_ID)
				.list();
				
				for(MWarehouse wh : list)
				{
					BigDecimal temp = DB.getSQLValueBD(null, "select  BOMQTYAVAILABLE(M_Product_ID,?,0) from M_Product  " +
							"Where M_Product_ID=? ", wh.getM_Warehouse_ID(),product.getM_Product_ID());
					
					available=available.add(temp);
				}
				
				
				/*BigDecimal available = MStorage.getQtyAvailable
					(M_Warehouse_ID, M_Product_ID.intValue(), M_AttributeSetInstance_ID, null);*/
				
				//faaguilar OFB end
				
				if (available == null)
					available = Env.ZERO;
				if (available.signum() == 0)
					mTab.fireDataStatusEEvent ("NoQtyAvailable", "0", false);
				else if (available.compareTo(QtyOrdered) < 0)
				{
					String info = "Disponible :" + available.toString();//faaguilar OFB
					mTab.fireDataStatusEEvent ("InsufficientQtyAvailable", info, false);
				}
				else
				{
					Integer C_OrderLine_ID = (Integer)mTab.getValue("C_OrderLine_ID");
					if (C_OrderLine_ID == null)
						C_OrderLine_ID = new Integer(0);
					BigDecimal notReserved = MOrderLine.getNotReserved(ctx, 
						M_Warehouse_ID, M_Product_ID, M_AttributeSetInstance_ID,
						C_OrderLine_ID.intValue());
					if (notReserved == null)
						notReserved = Env.ZERO;
					BigDecimal total = available.subtract(notReserved);
					if (total.compareTo(QtyOrdered) < 0)
					{
						String info = Msg.parseTranslation(ctx, "@QtyAvailable@=" + available 
							+ "  -  @QtyNotReserved@=" + notReserved + "  =  " + total);
						mTab.fireDataStatusEEvent ("InsufficientQtyAvailable", 
							info, false);
					}
				}
			}
		}
		//
		return "";
	}	//	qty
}	//	CalloutOrderLineWINDSOR

