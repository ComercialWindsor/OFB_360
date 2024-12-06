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

/** Generated Model for C_PaymentListLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_C_PaymentListLine extends PO implements I_C_PaymentListLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120617L;

    /** Standard Constructor */
    public X_C_PaymentListLine (Properties ctx, int C_PaymentListLine_ID, String trxName)
    {
      super (ctx, C_PaymentListLine_ID, trxName);
      /** if (C_PaymentListLine_ID == 0)
        {
			setAmt (Env.ZERO);
			setC_PaymentList_ID (0);
			setC_PaymentListLine_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_C_PaymentListLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_PaymentListLine[")
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

	public I_C_Payment getC_Payment() throws RuntimeException
    {
		return (I_C_Payment)MTable.get(getCtx(), I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_PaymentList getC_PaymentList() throws RuntimeException
    {
		return (I_C_PaymentList)MTable.get(getCtx(), I_C_PaymentList.Table_Name)
			.getPO(getC_PaymentList_ID(), get_TrxName());	}

	/** Set C_PaymentList.
		@param C_PaymentList_ID C_PaymentList	  */
	public void setC_PaymentList_ID (int C_PaymentList_ID)
	{
		if (C_PaymentList_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_PaymentList_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_PaymentList_ID, Integer.valueOf(C_PaymentList_ID));
	}

	/** Get C_PaymentList.
		@return C_PaymentList	  */
	public int getC_PaymentList_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentList_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_PaymentListLine.
		@param C_PaymentListLine_ID C_PaymentListLine	  */
	public void setC_PaymentListLine_ID (int C_PaymentListLine_ID)
	{
		if (C_PaymentListLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_PaymentListLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_PaymentListLine_ID, Integer.valueOf(C_PaymentListLine_ID));
	}

	/** Get C_PaymentListLine.
		@return C_PaymentListLine	  */
	public int getC_PaymentListLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentListLine_ID);
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