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

/** Generated Model for TP_RefundLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_TP_RefundLine extends PO implements I_TP_RefundLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160824L;

    /** Standard Constructor */
    public X_TP_RefundLine (Properties ctx, int TP_RefundLine_ID, String trxName)
    {
      super (ctx, TP_RefundLine_ID, trxName);
      /** if (TP_RefundLine_ID == 0)
        {
			setTP_Refund_ID (0);
			setTP_RefundLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TP_RefundLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TP_RefundLine[")
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

	public I_M_Movement getM_Movement() throws RuntimeException
    {
		return (I_M_Movement)MTable.get(getCtx(), I_M_Movement.Table_Name)
			.getPO(getM_Movement_ID(), get_TrxName());	}

	/** Set Inventory Move.
		@param M_Movement_ID 
		Movement of Inventory
	  */
	public void setM_Movement_ID (int M_Movement_ID)
	{
		if (M_Movement_ID < 1) 
			set_Value (COLUMNNAME_M_Movement_ID, null);
		else 
			set_Value (COLUMNNAME_M_Movement_ID, Integer.valueOf(M_Movement_ID));
	}

	/** Get Inventory Move.
		@return Movement of Inventory
	  */
	public int getM_Movement_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Movement_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_TP_Refund getTP_Refund() throws RuntimeException
    {
		return (I_TP_Refund)MTable.get(getCtx(), I_TP_Refund.Table_Name)
			.getPO(getTP_Refund_ID(), get_TrxName());	}

	/** Set TP_Refund.
		@param TP_Refund_ID TP_Refund	  */
	public void setTP_Refund_ID (int TP_Refund_ID)
	{
		if (TP_Refund_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TP_Refund_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TP_Refund_ID, Integer.valueOf(TP_Refund_ID));
	}

	/** Get TP_Refund.
		@return TP_Refund	  */
	public int getTP_Refund_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_Refund_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TP_RefundLine.
		@param TP_RefundLine_ID TP_RefundLine	  */
	public void setTP_RefundLine_ID (int TP_RefundLine_ID)
	{
		if (TP_RefundLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TP_RefundLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TP_RefundLine_ID, Integer.valueOf(TP_RefundLine_ID));
	}

	/** Get TP_RefundLine.
		@return TP_RefundLine	  */
	public int getTP_RefundLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_RefundLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}