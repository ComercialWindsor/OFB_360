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

/** Generated Model for T_PRESUPUESTO
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_PRESUPUESTO extends PO implements I_T_PRESUPUESTO, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140627L;

    /** Standard Constructor */
    public X_T_PRESUPUESTO (Properties ctx, int T_PRESUPUESTO_ID, String trxName)
    {
      super (ctx, T_PRESUPUESTO_ID, trxName);
      /** if (T_PRESUPUESTO_ID == 0)
        {
			setM_Product_Category_ID (0);
			setQtyEntered (Env.ZERO);
			setT_PRESUPUESTO_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_PRESUPUESTO (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_PRESUPUESTO[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set accrued.
		@param accrued accrued	  */
	public void setaccrued (BigDecimal accrued)
	{
		set_Value (COLUMNNAME_accrued, accrued);
	}

	/** Get accrued.
		@return accrued	  */
	public BigDecimal getaccrued () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_accrued);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set accrued_available.
		@param accrued_available accrued_available	  */
	public void setaccrued_available (BigDecimal accrued_available)
	{
		set_Value (COLUMNNAME_accrued_available, accrued_available);
	}

	/** Get accrued_available.
		@return accrued_available	  */
	public BigDecimal getaccrued_available () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_accrued_available);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set accrued_mineduc.
		@param accrued_mineduc accrued_mineduc	  */
	public void setaccrued_mineduc (BigDecimal accrued_mineduc)
	{
		set_Value (COLUMNNAME_accrued_mineduc, accrued_mineduc);
	}

	/** Get accrued_mineduc.
		@return accrued_mineduc	  */
	public BigDecimal getaccrued_mineduc () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_accrued_mineduc);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set accrued_unab.
		@param accrued_unab accrued_unab	  */
	public void setaccrued_unab (BigDecimal accrued_unab)
	{
		set_Value (COLUMNNAME_accrued_unab, accrued_unab);
	}

	/** Get accrued_unab.
		@return accrued_unab	  */
	public BigDecimal getaccrued_unab () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_accrued_unab);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/*public I_C_SubProjectOFB getC_SubProjectOFB() throws RuntimeException
    {
		return (I_C_SubProjectOFB)MTable.get(getCtx(), I_C_SubProjectOFB.Table_Name)
			.getPO(getC_SubProjectOFB_ID(), get_TrxName());	}*/

	/** Set C_SubProjectOFB_ID.
		@param C_SubProjectOFB_ID C_SubProjectOFB_ID	  */
	public void setC_SubProjectOFB_ID (int C_SubProjectOFB_ID)
	{
		if (C_SubProjectOFB_ID < 1) 
			set_Value (COLUMNNAME_C_SubProjectOFB_ID, null);
		else 
			set_Value (COLUMNNAME_C_SubProjectOFB_ID, Integer.valueOf(C_SubProjectOFB_ID));
	}

	/** Get C_SubProjectOFB_ID.
		@return C_SubProjectOFB_ID	  */
	public int getC_SubProjectOFB_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SubProjectOFB_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set executed.
		@param executed executed	  */
	public void setexecuted (BigDecimal executed)
	{
		set_Value (COLUMNNAME_executed, executed);
	}

	/** Get executed.
		@return executed	  */
	public BigDecimal getexecuted () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_executed);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set executed_available.
		@param executed_available executed_available	  */
	public void setexecuted_available (BigDecimal executed_available)
	{
		set_Value (COLUMNNAME_executed_available, executed_available);
	}

	/** Get executed_available.
		@return executed_available	  */
	public BigDecimal getexecuted_available () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_executed_available);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set executed_mineduc.
		@param executed_mineduc executed_mineduc	  */
	public void setexecuted_mineduc (BigDecimal executed_mineduc)
	{
		set_Value (COLUMNNAME_executed_mineduc, executed_mineduc);
	}

	/** Get executed_mineduc.
		@return executed_mineduc	  */
	public BigDecimal getexecuted_mineduc () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_executed_mineduc);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set executed_unab.
		@param executed_unab executed_unab	  */
	public void setexecuted_unab (BigDecimal executed_unab)
	{
		set_Value (COLUMNNAME_executed_unab, executed_unab);
	}

	/** Get executed_unab.
		@return executed_unab	  */
	public BigDecimal getexecuted_unab () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_executed_unab);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set fecha.
		@param fecha fecha	  */
	public void setfecha (Timestamp fecha)
	{
		set_Value (COLUMNNAME_fecha, fecha);
	}

	/** Get fecha.
		@return fecha	  */
	public Timestamp getfecha () 
	{
		return (Timestamp)get_Value(COLUMNNAME_fecha);
	}

	/** Set Line Amount.
		@param LineNetAmt 
		Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public void setLineNetAmt (BigDecimal LineNetAmt)
	{
		set_Value (COLUMNNAME_LineNetAmt, LineNetAmt);
	}

	/** Get Line Amount.
		@return Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public BigDecimal getLineNetAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LineNetAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_M_Product_Category getM_Product_Category() throws RuntimeException
    {
		return (I_M_Product_Category)MTable.get(getCtx(), I_M_Product_Category.Table_Name)
			.getPO(getM_Product_Category_ID(), get_TrxName());	}

	/** Set Product Category.
		@param M_Product_Category_ID 
		Category of a Product
	  */
	public void setM_Product_Category_ID (int M_Product_Category_ID)
	{
		if (M_Product_Category_ID < 1) 
			set_Value (COLUMNNAME_M_Product_Category_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_Category_ID, Integer.valueOf(M_Product_Category_ID));
	}

	/** Get Product Category.
		@return Category of a Product
	  */
	public int getM_Product_Category_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_Category_ID);
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

	/** Set mineduc_available.
		@param mineduc_available mineduc_available	  */
	public void setmineduc_available (BigDecimal mineduc_available)
	{
		set_Value (COLUMNNAME_mineduc_available, mineduc_available);
	}

	/** Get mineduc_available.
		@return mineduc_available	  */
	public BigDecimal getmineduc_available () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_mineduc_available);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Name 2.
		@param Name2 
		Additional Name
	  */
	public void setName2 (String Name2)
	{
		throw new IllegalArgumentException ("Name2 is virtual column");	}

	/** Get Name 2.
		@return Additional Name
	  */
	public String getName2 () 
	{
		return (String)get_Value(COLUMNNAME_Name2);
	}

	/** Set Quantity.
		@param QtyEntered 
		The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEntered (BigDecimal QtyEntered)
	{
		set_Value (COLUMNNAME_QtyEntered, QtyEntered);
	}

	/** Get Quantity.
		@return The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEntered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyEntered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set T_PRESUPUESTO.
		@param T_PRESUPUESTO_ID T_PRESUPUESTO	  */
	public void setT_PRESUPUESTO_ID (int T_PRESUPUESTO_ID)
	{
		if (T_PRESUPUESTO_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_PRESUPUESTO_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_PRESUPUESTO_ID, Integer.valueOf(T_PRESUPUESTO_ID));
	}

	/** Get T_PRESUPUESTO.
		@return T_PRESUPUESTO	  */
	public int getT_PRESUPUESTO_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_PRESUPUESTO_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set unab_available.
		@param unab_available unab_available	  */
	public void setunab_available (BigDecimal unab_available)
	{
		set_Value (COLUMNNAME_unab_available, unab_available);
	}

	/** Get unab_available.
		@return unab_available	  */
	public BigDecimal getunab_available () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_unab_available);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}