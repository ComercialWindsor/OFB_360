/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for C_Margin
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_C_Margin extends PO implements I_C_Margin, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170519L;

    /** Standard Constructor */
    public X_C_Margin (Properties ctx, int C_Margin_ID, String trxName)
    {
      super (ctx, C_Margin_ID, trxName);
      /** if (C_Margin_ID == 0)
        {
			setC_Margin_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_Margin (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_C_Margin[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set C_Margin_ID.
		@param C_Margin_ID C_Margin_ID	  */
	public void setC_Margin_ID (int C_Margin_ID)
	{
		if (C_Margin_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Margin_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Margin_ID, Integer.valueOf(C_Margin_ID));
	}

	/** Get C_Margin_ID.
		@return C_Margin_ID	  */
	public int getC_Margin_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Margin_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_MarginHeader getC_MarginHeader() throws RuntimeException
    {
		return (I_C_MarginHeader)MTable.get(getCtx(), I_C_MarginHeader.Table_Name)
			.getPO(getC_MarginHeader_ID(), get_TrxName());	}

	/** Set C_MarginHeader_ID.
		@param C_MarginHeader_ID C_MarginHeader_ID	  */
	public void setC_MarginHeader_ID (int C_MarginHeader_ID)
	{
		if (C_MarginHeader_ID < 1) 
			set_Value (COLUMNNAME_C_MarginHeader_ID, null);
		else 
			set_Value (COLUMNNAME_C_MarginHeader_ID, Integer.valueOf(C_MarginHeader_ID));
	}

	/** Get C_MarginHeader_ID.
		@return C_MarginHeader_ID	  */
	public int getC_MarginHeader_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_MarginHeader_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Period getC_Period() throws RuntimeException
    {
		return (I_C_Period)MTable.get(getCtx(), I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set costo_final.
		@param costo_final costo_final	  */
	public void setcosto_final (BigDecimal costo_final)
	{
		set_Value (COLUMNNAME_costo_final, costo_final);
	}

	/** Get costo_final.
		@return costo_final	  */
	public BigDecimal getcosto_final () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costo_final);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costo_inicial.
		@param costo_inicial 
		costo inicial
	  */
	public void setcosto_inicial (BigDecimal costo_inicial)
	{
		set_Value (COLUMNNAME_costo_inicial, costo_inicial);
	}

	/** Get costo_inicial.
		@return costo inicial
	  */
	public BigDecimal getcosto_inicial () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costo_inicial);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set inv_final.
		@param inv_final inv_final	  */
	public void setinv_final (BigDecimal inv_final)
	{
		set_Value (COLUMNNAME_inv_final, inv_final);
	}

	/** Get inv_final.
		@return inv_final	  */
	public BigDecimal getinv_final () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_inv_final);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set inventario_inicial.
		@param inventario_inicial 
		inventario inicial
	  */
	public void setinventario_inicial (BigDecimal inventario_inicial)
	{
		set_Value (COLUMNNAME_inventario_inicial, inventario_inicial);
	}

	/** Get inventario_inicial.
		@return inventario inicial
	  */
	public BigDecimal getinventario_inicial () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_inventario_inicial);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_M_Product getM_Product() throws RuntimeException
    {
		return (I_M_Product)MTable.get(getCtx(), I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set porcent.
		@param porcent 
		% sobre ventas
	  */
	public void setporcent (BigDecimal porcent)
	{
		set_Value (COLUMNNAME_porcent, porcent);
	}

	/** Get porcent.
		@return % sobre ventas
	  */
	public BigDecimal getporcent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_porcent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set precio_final.
		@param precio_final precio_final	  */
	public void setprecio_final (BigDecimal precio_final)
	{
		set_Value (COLUMNNAME_precio_final, precio_final);
	}

	/** Get precio_final.
		@return precio_final	  */
	public BigDecimal getprecio_final () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_precio_final);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set provisiones.
		@param provisiones provisiones	  */
	public void setprovisiones (BigDecimal provisiones)
	{
		set_Value (COLUMNNAME_provisiones, provisiones);
	}

	/** Get provisiones.
		@return provisiones	  */
	public BigDecimal getprovisiones () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_provisiones);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set qty_cost.
		@param qty_cost 
		costo de ventas
	  */
	public void setqty_cost (BigDecimal qty_cost)
	{
		set_Value (COLUMNNAME_qty_cost, qty_cost);
	}

	/** Get qty_cost.
		@return costo de ventas
	  */
	public BigDecimal getqty_cost () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_qty_cost);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set qty_sales.
		@param qty_sales 
		q litros
	  */
	public void setqty_sales (BigDecimal qty_sales)
	{
		set_Value (COLUMNNAME_qty_sales, qty_sales);
	}

	/** Get qty_sales.
		@return q litros
	  */
	public BigDecimal getqty_sales () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_qty_sales);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set total.
		@param total 
		margen bruto
	  */
	public void settotal (BigDecimal total)
	{
		set_Value (COLUMNNAME_total, total);
	}

	/** Get total.
		@return margen bruto
	  */
	public BigDecimal gettotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_total);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set total_compras.
		@param total_compras 
		total compras
	  */
	public void settotal_compras (BigDecimal total_compras)
	{
		set_Value (COLUMNNAME_total_compras, total_compras);
	}

	/** Get total_compras.
		@return total compras
	  */
	public BigDecimal gettotal_compras () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_total_compras);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set total_purchases2.
		@param total_purchases2 
		$Litro
	  */
	public void settotal_purchases2 (BigDecimal total_purchases2)
	{
		set_Value (COLUMNNAME_total_purchases2, total_purchases2);
	}

	/** Get total_purchases2.
		@return $Litro
	  */
	public BigDecimal gettotal_purchases2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_total_purchases2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set total_sales2.
		@param total_sales2 
		ventas
	  */
	public void settotal_sales2 (BigDecimal total_sales2)
	{
		set_Value (COLUMNNAME_total_sales2, total_sales2);
	}

	/** Get total_sales2.
		@return ventas
	  */
	public BigDecimal gettotal_sales2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_total_sales2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}