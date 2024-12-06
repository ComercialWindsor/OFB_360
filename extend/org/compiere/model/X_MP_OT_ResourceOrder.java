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

/** Generated Model for MP_OT_ResourceOrder
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_MP_OT_ResourceOrder extends PO implements I_MP_OT_ResourceOrder, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20161025L;

    /** Standard Constructor */
    public X_MP_OT_ResourceOrder (Properties ctx, int MP_OT_ResourceOrder_ID, String trxName)
    {
      super (ctx, MP_OT_ResourceOrder_ID, trxName);
      /** if (MP_OT_ResourceOrder_ID == 0)
        {
			setMP_OT_ResourceOrder_ID (0);
			setMP_OT_Task_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_MP_OT_ResourceOrder (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_MP_OT_ResourceOrder[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_Order getC_Order() throws RuntimeException
    {
		return (I_C_Order)MTable.get(getCtx(), I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_Value (COLUMNNAME_C_Order_ID, null);
		else 
			set_Value (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_OrderLine getC_OrderLine() throws RuntimeException
    {
		return (I_C_OrderLine)MTable.get(getCtx(), I_C_OrderLine.Table_Name)
			.getPO(getC_OrderLine_ID(), get_TrxName());	}

	/** Set Sales Order Line.
		@param C_OrderLine_ID 
		Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID)
	{
		if (C_OrderLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_OrderLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_OrderLine_ID, Integer.valueOf(C_OrderLine_ID));
	}

	/** Get Sales Order Line.
		@return Sales Order Line
	  */
	public int getC_OrderLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Cost Value.
		@param CostAmt 
		Value with Cost
	  */
	public void setCostAmt (BigDecimal CostAmt)
	{
		set_Value (COLUMNNAME_CostAmt, CostAmt);
	}

	/** Get Cost Value.
		@return Value with Cost
	  */
	public BigDecimal getCostAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_M_BOM getM_BOM() throws RuntimeException
    {
		return (I_M_BOM)MTable.get(getCtx(), I_M_BOM.Table_Name)
			.getPO(getM_BOM_ID(), get_TrxName());	}

	/** Set BOM.
		@param M_BOM_ID 
		Bill of Material
	  */
	public void setM_BOM_ID (int M_BOM_ID)
	{
		if (M_BOM_ID < 1) 
			set_Value (COLUMNNAME_M_BOM_ID, null);
		else 
			set_Value (COLUMNNAME_M_BOM_ID, Integer.valueOf(M_BOM_ID));
	}

	/** Get BOM.
		@return Bill of Material
	  */
	public int getM_BOM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_BOM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set MP_OT_ResourceOrder.
		@param MP_OT_ResourceOrder_ID MP_OT_ResourceOrder	  */
	public void setMP_OT_ResourceOrder_ID (int MP_OT_ResourceOrder_ID)
	{
		if (MP_OT_ResourceOrder_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MP_OT_ResourceOrder_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MP_OT_ResourceOrder_ID, Integer.valueOf(MP_OT_ResourceOrder_ID));
	}

	/** Get MP_OT_ResourceOrder.
		@return MP_OT_ResourceOrder	  */
	public int getMP_OT_ResourceOrder_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_OT_ResourceOrder_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_MP_OT_Task getMP_OT_Task() throws RuntimeException
    {
		return (I_MP_OT_Task)MTable.get(getCtx(), I_MP_OT_Task.Table_Name)
			.getPO(getMP_OT_Task_ID(), get_TrxName());	}

	/** Set MP_OT_Task.
		@param MP_OT_Task_ID MP_OT_Task	  */
	public void setMP_OT_Task_ID (int MP_OT_Task_ID)
	{
		if (MP_OT_Task_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MP_OT_Task_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MP_OT_Task_ID, Integer.valueOf(MP_OT_Task_ID));
	}

	/** Get MP_OT_Task.
		@return MP_OT_Task	  */
	public int getMP_OT_Task_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_OT_Task_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ResourceQty.
		@param ResourceQty ResourceQty	  */
	public void setResourceQty (BigDecimal ResourceQty)
	{
		set_Value (COLUMNNAME_ResourceQty, ResourceQty);
	}

	/** Get ResourceQty.
		@return ResourceQty	  */
	public BigDecimal getResourceQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ResourceQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** ResourceType AD_Reference_ID=1000023 */
	public static final int RESOURCETYPE_AD_Reference_ID=1000023;
	/** Parte (Material) = RR */
	public static final String RESOURCETYPE_ParteMaterial = "RR";
	/** Personal (Humano) = HH */
	public static final String RESOURCETYPE_PersonalHumano = "HH";
	/** Herramienta = TT */
	public static final String RESOURCETYPE_Herramienta = "TT";
	/** Producto = BT */
	public static final String RESOURCETYPE_Producto = "BT";
	/** Lista de Materiales (BOM) = BP */
	public static final String RESOURCETYPE_ListaDeMaterialesBOM = "BP";
	/** Set ResourceType.
		@param ResourceType ResourceType	  */
	public void setResourceType (String ResourceType)
	{

		set_Value (COLUMNNAME_ResourceType, ResourceType);
	}

	/** Get ResourceType.
		@return ResourceType	  */
	public String getResourceType () 
	{
		return (String)get_Value(COLUMNNAME_ResourceType);
	}

	public I_S_Resource getS_Resource() throws RuntimeException
    {
		return (I_S_Resource)MTable.get(getCtx(), I_S_Resource.Table_Name)
			.getPO(getS_Resource_ID(), get_TrxName());	}

	/** Set Resource.
		@param S_Resource_ID 
		Resource
	  */
	public void setS_Resource_ID (int S_Resource_ID)
	{
		if (S_Resource_ID < 1) 
			set_Value (COLUMNNAME_S_Resource_ID, null);
		else 
			set_Value (COLUMNNAME_S_Resource_ID, Integer.valueOf(S_Resource_ID));
	}

	/** Get Resource.
		@return Resource
	  */
	public int getS_Resource_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_S_Resource_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Window Height.
		@param WinHeight Window Height	  */
	public void setWinHeight (BigDecimal WinHeight)
	{
		set_Value (COLUMNNAME_WinHeight, WinHeight);
	}

	/** Get Window Height.
		@return Window Height	  */
	public BigDecimal getWinHeight () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WinHeight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Window Width.
		@param WinWidth Window Width	  */
	public void setWinWidth (BigDecimal WinWidth)
	{
		set_Value (COLUMNNAME_WinWidth, WinWidth);
	}

	/** Get Window Width.
		@return Window Width	  */
	public BigDecimal getWinWidth () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WinWidth);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}