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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for PA_FuelAudit
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_PA_FuelAudit extends PO implements I_PA_FuelAudit, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160722L;

    /** Standard Constructor */
    public X_PA_FuelAudit (Properties ctx, int PA_FuelAudit_ID, String trxName)
    {
      super (ctx, PA_FuelAudit_ID, trxName);
      /** if (PA_FuelAudit_ID == 0)
        {
			setPA_FuelAudit_ID (0);
        } */
    }

    /** Load Constructor */
    public X_PA_FuelAudit (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_PA_FuelAudit[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AuditedAmount.
		@param AuditedAmount AuditedAmount	  */
	public void setAuditedAmount (BigDecimal AuditedAmount)
	{
		set_Value (COLUMNNAME_AuditedAmount, AuditedAmount);
	}

	/** Get AuditedAmount.
		@return AuditedAmount	  */
	public BigDecimal getAuditedAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AuditedAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set DifAuditedAmount.
		@param DifAuditedAmount DifAuditedAmount	  */
	public void setDifAuditedAmount (BigDecimal DifAuditedAmount)
	{
		set_Value (COLUMNNAME_DifAuditedAmount, DifAuditedAmount);
	}

	/** Get DifAuditedAmount.
		@return DifAuditedAmount	  */
	public BigDecimal getDifAuditedAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DifAuditedAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** FuelDispenser AD_Reference_ID=1000082 */
	public static final int FUELDISPENSER_AD_Reference_ID=1000082;
	/** A1 = A1 */
	public static final String FUELDISPENSER_A1 = "A1";
	/** A2 = A2 */
	public static final String FUELDISPENSER_A2 = "A2";
	/** B1 = B1 */
	public static final String FUELDISPENSER_B1 = "B1";
	/** B2 = B2 */
	public static final String FUELDISPENSER_B2 = "B2";
	/** Set FuelDispenser.
		@param FuelDispenser FuelDispenser	  */
	public void setFuelDispenser (String FuelDispenser)
	{

		set_Value (COLUMNNAME_FuelDispenser, FuelDispenser);
	}

	/** Get FuelDispenser.
		@return FuelDispenser	  */
	public String getFuelDispenser () 
	{
		return (String)get_Value(COLUMNNAME_FuelDispenser);
	}

	public I_M_Locator getM_Locator() throws RuntimeException
    {
		return (I_M_Locator)MTable.get(getCtx(), I_M_Locator.Table_Name)
			.getPO(getM_Locator_ID(), get_TrxName());	}

	/** Set Locator.
		@param M_Locator_ID 
		Warehouse Locator
	  */
	public void setM_Locator_ID (int M_Locator_ID)
	{
		if (M_Locator_ID < 1) 
			set_Value (COLUMNNAME_M_Locator_ID, null);
		else 
			set_Value (COLUMNNAME_M_Locator_ID, Integer.valueOf(M_Locator_ID));
	}

	/** Get Locator.
		@return Warehouse Locator
	  */
	public int getM_Locator_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Locator_ID);
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

	public I_M_Warehouse getM_Warehouse() throws RuntimeException
    {
		return (I_M_Warehouse)MTable.get(getCtx(), I_M_Warehouse.Table_Name)
			.getPO(getM_Warehouse_ID(), get_TrxName());	}

	/** Set Warehouse.
		@param M_Warehouse_ID 
		Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID)
	{
		if (M_Warehouse_ID < 1) 
			set_Value (COLUMNNAME_M_Warehouse_ID, null);
		else 
			set_Value (COLUMNNAME_M_Warehouse_ID, Integer.valueOf(M_Warehouse_ID));
	}

	/** Get Warehouse.
		@return Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Warehouse_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Movement Quantity.
		@param MovementQty 
		Quantity of a product moved.
	  */
	public void setMovementQty (BigDecimal MovementQty)
	{
		set_Value (COLUMNNAME_MovementQty, MovementQty);
	}

	/** Get Movement Quantity.
		@return Quantity of a product moved.
	  */
	public BigDecimal getMovementQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MovementQty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set PA_FuelAudit.
		@param PA_FuelAudit_ID PA_FuelAudit	  */
	public void setPA_FuelAudit_ID (int PA_FuelAudit_ID)
	{
		if (PA_FuelAudit_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PA_FuelAudit_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PA_FuelAudit_ID, Integer.valueOf(PA_FuelAudit_ID));
	}

	/** Get PA_FuelAudit.
		@return PA_FuelAudit	  */
	public int getPA_FuelAudit_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_FuelAudit_ID);
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

	public I_TP_PumpFuel getTP_PumpFuel() throws RuntimeException
    {
		return (I_TP_PumpFuel)MTable.get(getCtx(), I_TP_PumpFuel.Table_Name)
			.getPO(getTP_PumpFuel_ID(), get_TrxName());	}

	/** Set TP_PumpFuel.
		@param TP_PumpFuel_ID TP_PumpFuel	  */
	public void setTP_PumpFuel_ID (int TP_PumpFuel_ID)
	{
		if (TP_PumpFuel_ID < 1) 
			set_Value (COLUMNNAME_TP_PumpFuel_ID, null);
		else 
			set_Value (COLUMNNAME_TP_PumpFuel_ID, Integer.valueOf(TP_PumpFuel_ID));
	}

	/** Get TP_PumpFuel.
		@return TP_PumpFuel	  */
	public int getTP_PumpFuel_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_PumpFuel_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Type AD_Reference_ID=1000083 */
	public static final int TYPE_AD_Reference_ID=1000083;
	/** Numeral Final = FI */
	public static final String TYPE_NumeralFinal = "FI";
	/** Numeral Inicial = IN */
	public static final String TYPE_NumeralInicial = "IN";
	/** Set Type.
		@param Type 
		Type of Validation (SQL, Java Script, Java Language)
	  */
	public void setType (String Type)
	{

		set_Value (COLUMNNAME_Type, Type);
	}

	/** Get Type.
		@return Type of Validation (SQL, Java Script, Java Language)
	  */
	public String getType () 
	{
		return (String)get_Value(COLUMNNAME_Type);
	}
}