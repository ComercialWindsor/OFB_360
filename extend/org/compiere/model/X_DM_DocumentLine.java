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

/** Generated Model for DM_DocumentLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_DM_DocumentLine extends PO implements I_DM_DocumentLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120807L;

    /** Standard Constructor */
    public X_DM_DocumentLine (Properties ctx, int DM_DocumentLine_ID, String trxName)
    {
      super (ctx, DM_DocumentLine_ID, trxName);
      /** if (DM_DocumentLine_ID == 0)
        {
			setAmt (Env.ZERO);
			setDM_DocumentLine_ID (0);
			setM_Product_ID (0);
        } */
    }

    /** Load Constructor */
    public X_DM_DocumentLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_DM_DocumentLine[")
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

	public I_C_ProjectSchedule getC_ProjectSchedule() throws RuntimeException
    {
		return (I_C_ProjectSchedule)MTable.get(getCtx(), I_C_ProjectSchedule.Table_Name)
			.getPO(getC_ProjectSchedule_ID(), get_TrxName());	}

	/** Set C_ProjectSchedule.
		@param C_ProjectSchedule_ID C_ProjectSchedule	  */
	public void setC_ProjectSchedule_ID (int C_ProjectSchedule_ID)
	{
		if (C_ProjectSchedule_ID < 1) 
			set_Value (COLUMNNAME_C_ProjectSchedule_ID, null);
		else 
			set_Value (COLUMNNAME_C_ProjectSchedule_ID, Integer.valueOf(C_ProjectSchedule_ID));
	}

	/** Get C_ProjectSchedule.
		@return C_ProjectSchedule	  */
	public int getC_ProjectSchedule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectSchedule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_DM_Document getDM_Document() throws RuntimeException
    {
		return (I_DM_Document)MTable.get(getCtx(), I_DM_Document.Table_Name)
			.getPO(getDM_Document_ID(), get_TrxName());	}

	/** Set DM_Document.
		@param DM_Document_ID DM_Document	  */
	public void setDM_Document_ID (int DM_Document_ID)
	{
		if (DM_Document_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DM_Document_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DM_Document_ID, Integer.valueOf(DM_Document_ID));
	}

	/** Get DM_Document.
		@return DM_Document	  */
	public int getDM_Document_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_Document_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DM_DocumentLine.
		@param DM_DocumentLine_ID DM_DocumentLine	  */
	public void setDM_DocumentLine_ID (int DM_DocumentLine_ID)
	{
		if (DM_DocumentLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DM_DocumentLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DM_DocumentLine_ID, Integer.valueOf(DM_DocumentLine_ID));
	}

	/** Get DM_DocumentLine.
		@return DM_DocumentLine	  */
	public int getDM_DocumentLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_DocumentLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

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
}