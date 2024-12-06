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

/** Generated Model for DM_MandateAgreementLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_DM_MandateAgreementLine extends PO implements I_DM_MandateAgreementLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120117L;

    /** Standard Constructor */
    public X_DM_MandateAgreementLine (Properties ctx, int DM_MandateAgreementLine_ID, String trxName)
    {
      super (ctx, DM_MandateAgreementLine_ID, trxName);
      /** if (DM_MandateAgreementLine_ID == 0)
        {
			setAmt (Env.ZERO);
			setDM_MandateAgreement_ID (0);
			setDM_MandateAgreementLine_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_DM_MandateAgreementLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_DM_MandateAgreementLine[")
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

	/** Set Date Promised.
		@param DatePromised 
		Date Order was promised
	  */
	public void setDatePromised (Timestamp DatePromised)
	{
		set_Value (COLUMNNAME_DatePromised, DatePromised);
	}

	/** Get Date Promised.
		@return Date Order was promised
	  */
	public Timestamp getDatePromised () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DatePromised);
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

	public I_DM_MandateAgreement getDM_MandateAgreement() throws RuntimeException
    {
		return (I_DM_MandateAgreement)MTable.get(getCtx(), I_DM_MandateAgreement.Table_Name)
			.getPO(getDM_MandateAgreement_ID(), get_TrxName());	}

	/** Set DM_MandateAgreement.
		@param DM_MandateAgreement_ID DM_MandateAgreement	  */
	public void setDM_MandateAgreement_ID (int DM_MandateAgreement_ID)
	{
		if (DM_MandateAgreement_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DM_MandateAgreement_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DM_MandateAgreement_ID, Integer.valueOf(DM_MandateAgreement_ID));
	}

	/** Get DM_MandateAgreement.
		@return DM_MandateAgreement	  */
	public int getDM_MandateAgreement_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_MandateAgreement_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DM_MandateAgreementLine.
		@param DM_MandateAgreementLine_ID DM_MandateAgreementLine	  */
	public void setDM_MandateAgreementLine_ID (int DM_MandateAgreementLine_ID)
	{
		if (DM_MandateAgreementLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DM_MandateAgreementLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DM_MandateAgreementLine_ID, Integer.valueOf(DM_MandateAgreementLine_ID));
	}

	/** Get DM_MandateAgreementLine.
		@return DM_MandateAgreementLine	  */
	public int getDM_MandateAgreementLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_MandateAgreementLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (int Line)
	{
		set_Value (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
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