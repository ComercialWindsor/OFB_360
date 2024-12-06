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

/** Generated Model for PA_FuelAuditLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_PA_FuelAuditLine extends PO implements I_PA_FuelAuditLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160722L;

    /** Standard Constructor */
    public X_PA_FuelAuditLine (Properties ctx, int PA_FuelAuditLine_ID, String trxName)
    {
      super (ctx, PA_FuelAuditLine_ID, trxName);
      /** if (PA_FuelAuditLine_ID == 0)
        {
			setPA_FuelAudit_ID (0);
			setPA_FuelAuditLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_PA_FuelAuditLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_PA_FuelAuditLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set acumqty.
		@param acumqty acumqty	  */
	public void setacumqty (BigDecimal acumqty)
	{
		set_Value (COLUMNNAME_acumqty, acumqty);
	}

	/** Get acumqty.
		@return acumqty	  */
	public BigDecimal getacumqty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_acumqty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Amount.
		@param Amount 
		Amount in a defined currency
	  */
	public void setAmount (BigDecimal Amount)
	{
		set_Value (COLUMNNAME_Amount, Amount);
	}

	/** Get Amount.
		@return Amount in a defined currency
	  */
	public BigDecimal getAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	public I_PA_FuelAudit getPA_FuelAudit() throws RuntimeException
    {
		return (I_PA_FuelAudit)MTable.get(getCtx(), I_PA_FuelAudit.Table_Name)
			.getPO(getPA_FuelAudit_ID(), get_TrxName());	}

	/** Set PA_FuelAudit.
		@param PA_FuelAudit_ID PA_FuelAudit	  */
	public void setPA_FuelAudit_ID (int PA_FuelAudit_ID)
	{
		if (PA_FuelAudit_ID < 1) 
			set_Value (COLUMNNAME_PA_FuelAudit_ID, null);
		else 
			set_Value (COLUMNNAME_PA_FuelAudit_ID, Integer.valueOf(PA_FuelAudit_ID));
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

	/** Set PA_FuelAuditLine.
		@param PA_FuelAuditLine_ID PA_FuelAuditLine	  */
	public void setPA_FuelAuditLine_ID (int PA_FuelAuditLine_ID)
	{
		if (PA_FuelAuditLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PA_FuelAuditLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PA_FuelAuditLine_ID, Integer.valueOf(PA_FuelAuditLine_ID));
	}

	/** Get PA_FuelAuditLine.
		@return PA_FuelAuditLine	  */
	public int getPA_FuelAuditLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PA_FuelAuditLine_ID);
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
}