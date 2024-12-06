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

/** Generated Model for C_PaymentRequestLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_C_PaymentRequestLine extends PO implements I_C_PaymentRequestLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20111222L;

    /** Standard Constructor */
    public X_C_PaymentRequestLine (Properties ctx, int C_PaymentRequestLine_ID, String trxName)
    {
      super (ctx, C_PaymentRequestLine_ID, trxName);
      /** if (C_PaymentRequestLine_ID == 0)
        {
			setAmt (Env.ZERO);
			setC_PaymentRequest_ID (0);
			setC_PaymentRequestLine_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_C_PaymentRequestLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_PaymentRequestLine[")
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

	public I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (I_C_Invoice)MTable.get(getCtx(), I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_C_PaymentRequest getC_PaymentRequest() throws RuntimeException
    {
		return (I_C_PaymentRequest)MTable.get(getCtx(), I_C_PaymentRequest.Table_Name)
			.getPO(getC_PaymentRequest_ID(), get_TrxName());	}

	/** Set C_PaymentRequest.
		@param C_PaymentRequest_ID C_PaymentRequest	  */
	public void setC_PaymentRequest_ID (int C_PaymentRequest_ID)
	{
		if (C_PaymentRequest_ID < 1) 
			set_Value (COLUMNNAME_C_PaymentRequest_ID, null);
		else 
			set_Value (COLUMNNAME_C_PaymentRequest_ID, Integer.valueOf(C_PaymentRequest_ID));
	}

	/** Get C_PaymentRequest.
		@return C_PaymentRequest	  */
	public int getC_PaymentRequest_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentRequest_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_PaymentRequestLine.
		@param C_PaymentRequestLine_ID C_PaymentRequestLine	  */
	public void setC_PaymentRequestLine_ID (int C_PaymentRequestLine_ID)
	{
		if (C_PaymentRequestLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_PaymentRequestLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_PaymentRequestLine_ID, Integer.valueOf(C_PaymentRequestLine_ID));
	}

	/** Get C_PaymentRequestLine.
		@return C_PaymentRequestLine	  */
	public int getC_PaymentRequestLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentRequestLine_ID);
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

	public I_GL_JournalLine getGL_JournalLine() throws RuntimeException
    {
		return (I_GL_JournalLine)MTable.get(getCtx(), I_GL_JournalLine.Table_Name)
			.getPO(getGL_JournalLine_ID(), get_TrxName());	}

	/** Set Journal Line.
		@param GL_JournalLine_ID 
		General Ledger Journal Line
	  */
	public void setGL_JournalLine_ID (int GL_JournalLine_ID)
	{
		if (GL_JournalLine_ID < 1) 
			set_Value (COLUMNNAME_GL_JournalLine_ID, null);
		else 
			set_Value (COLUMNNAME_GL_JournalLine_ID, Integer.valueOf(GL_JournalLine_ID));
	}

	/** Get Journal Line.
		@return General Ledger Journal Line
	  */
	public int getGL_JournalLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_GL_JournalLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
	
	/** Set C_InvoicePaySchedule_ID.
	@param C_InvoicePaySchedule_ID	
  */
	public void setC_InvoicePaySchedule_ID (int C_InvoicePaySchedule_ID)
	{
	if (C_InvoicePaySchedule_ID < 1) 
		set_Value (COLUMNNAME_C_InvoicePaySchedule_ID, null);
	else 
		set_Value (COLUMNNAME_C_InvoicePaySchedule_ID, Integer.valueOf(C_InvoicePaySchedule_ID));
	}

	/** Get Journal Line.
	@return General Ledger Journal Line
  */
	public int getC_InvoicePaySchedule_ID () 
	{
	Integer ii = (Integer)get_Value(COLUMNNAME_C_InvoicePaySchedule_ID);
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