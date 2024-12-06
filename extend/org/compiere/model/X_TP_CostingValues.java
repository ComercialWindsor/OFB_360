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

/** Generated Model for TP_CostingValues
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_TP_CostingValues extends PO implements I_TP_CostingValues, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20130930L;

    /** Standard Constructor */
    public X_TP_CostingValues (Properties ctx, int TP_CostingValues_ID, String trxName)
    {
      super (ctx, TP_CostingValues_ID, trxName);
      /** if (TP_CostingValues_ID == 0)
        {
			setTP_CostingValues_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TP_CostingValues (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TP_CostingValues[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount.
		@param Amt 
		Amount
	  */
	public void setAmt (BigDecimal Amt)
	{
		set_Value (COLUMNNAME_Amt, Amt);
	}

	/** Get Amount.
		@return Amount
	  */
	public BigDecimal getAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_ProjectOFB getC_ProjectOFB() throws RuntimeException
    {
		return (I_C_ProjectOFB)MTable.get(getCtx(), I_C_ProjectOFB.Table_Name)
			.getPO(getC_ProjectOFB_ID(), get_TrxName());	}

	/** Set C_ProjectOFB_ID.
		@param C_ProjectOFB_ID C_ProjectOFB_ID	  */
	public void setC_ProjectOFB_ID (int C_ProjectOFB_ID)
	{
		if (C_ProjectOFB_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_ProjectOFB_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_ProjectOFB_ID, Integer.valueOf(C_ProjectOFB_ID));
	}

	/** Get C_ProjectOFB_ID.
		@return C_ProjectOFB_ID	  */
	public int getC_ProjectOFB_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectOFB_ID);
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

	/** Set MaxQty.
		@param MaxQty MaxQty	  */
	public void setMaxQty (BigDecimal MaxQty)
	{
		set_Value (COLUMNNAME_MaxQty, MaxQty);
	}

	/** Get MaxQty.
		@return MaxQty	  */
	public BigDecimal getMaxQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MaxQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Minimum Quantity.
		@param MinQty 
		Minimum quantity for the business partner
	  */
	public void setMinQty (BigDecimal MinQty)
	{
		set_Value (COLUMNNAME_MinQty, MinQty);
	}

	/** Get Minimum Quantity.
		@return Minimum quantity for the business partner
	  */
	public BigDecimal getMinQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MinQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** TP_CostingType AD_Reference_ID=1000046 */
	public static final int TP_COSTINGTYPE_AD_Reference_ID=1000046;
	/** Por Kilometros = K */
	public static final String TP_COSTINGTYPE_PorKilometros = "K";
	/** Por Volumen = V */
	public static final String TP_COSTINGTYPE_PorVolumen = "V";
	/** Definido por Destino = D */
	public static final String TP_COSTINGTYPE_DefinidoPorDestino = "D";
	/** Set TP_CostingType.
		@param TP_CostingType TP_CostingType	  */
	public void setTP_CostingType (String TP_CostingType)
	{

		set_Value (COLUMNNAME_TP_CostingType, TP_CostingType);
	}

	/** Get TP_CostingType.
		@return TP_CostingType	  */
	public String getTP_CostingType () 
	{
		return (String)get_Value(COLUMNNAME_TP_CostingType);
	}

	/** Set TP_CostingValues.
		@param TP_CostingValues_ID TP_CostingValues	  */
	public void setTP_CostingValues_ID (int TP_CostingValues_ID)
	{
		if (TP_CostingValues_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TP_CostingValues_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TP_CostingValues_ID, Integer.valueOf(TP_CostingValues_ID));
	}

	/** Get TP_CostingValues.
		@return TP_CostingValues	  */
	public int getTP_CostingValues_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_CostingValues_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_TP_Destination getTP_Destination() throws RuntimeException
    {
		return (I_TP_Destination)MTable.get(getCtx(), I_TP_Destination.Table_Name)
			.getPO(getTP_Destination_ID(), get_TrxName());	}

	/** Set TP_Destination.
		@param TP_Destination_ID TP_Destination	  */
	public void setTP_Destination_ID (int TP_Destination_ID)
	{
		if (TP_Destination_ID < 1) 
			set_Value (COLUMNNAME_TP_Destination_ID, null);
		else 
			set_Value (COLUMNNAME_TP_Destination_ID, Integer.valueOf(TP_Destination_ID));
	}

	/** Get TP_Destination.
		@return TP_Destination	  */
	public int getTP_Destination_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_Destination_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** TP_Type AD_Reference_ID=1000045 */
	public static final int TP_TYPE_AD_Reference_ID=1000045;
	/** 3L2 = 1 */
	public static final String TP_TYPE_3L2 = "1";
	/** 3L3 = 2 */
	public static final String TP_TYPE_3L3 = "2";
	/** Set TP_Type.
		@param TP_Type TP_Type	  */
	public void setTP_Type (String TP_Type)
	{

		set_Value (COLUMNNAME_TP_Type, TP_Type);
	}

	/** Get TP_Type.
		@return TP_Type	  */
	public String getTP_Type () 
	{
		return (String)get_Value(COLUMNNAME_TP_Type);
	}

	/** TP_TypeValue AD_Reference_ID=1000047 */
	public static final int TP_TYPEVALUE_AD_Reference_ID=1000047;
	/** Valor en Km = K */
	public static final String TP_TYPEVALUE_ValorEnKm = "K";
	/** Valor en Volumen = V */
	public static final String TP_TYPEVALUE_ValorEnVolumen = "V";
	/** Costo FIjo = F */
	public static final String TP_TYPEVALUE_CostoFIjo = "F";
	/** Set TP_TypeValue.
		@param TP_TypeValue TP_TypeValue	  */
	public void setTP_TypeValue (String TP_TypeValue)
	{

		set_Value (COLUMNNAME_TP_TypeValue, TP_TypeValue);
	}

	/** Get TP_TypeValue.
		@return TP_TypeValue	  */
	public String getTP_TypeValue () 
	{
		return (String)get_Value(COLUMNNAME_TP_TypeValue);
	}
}