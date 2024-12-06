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

/** Generated Model for A_AssetCriticalDate
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_A_AssetCriticalDate extends PO implements I_A_AssetCriticalDate, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151111L;

    /** Standard Constructor */
    public X_A_AssetCriticalDate (Properties ctx, int A_AssetCriticalDate_ID, String trxName)
    {
      super (ctx, A_AssetCriticalDate_ID, trxName);
      /** if (A_AssetCriticalDate_ID == 0)
        {
			setA_AssetCriticalDate_ID (0);
        } */
    }

    /** Load Constructor */
    public X_A_AssetCriticalDate (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_A_AssetCriticalDate[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_A_Asset getA_Asset() throws RuntimeException
    {
		return (I_A_Asset)MTable.get(getCtx(), I_A_Asset.Table_Name)
			.getPO(getA_Asset_ID(), get_TrxName());	}

	/** Set Asset.
		@param A_Asset_ID 
		Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID)
	{
		if (A_Asset_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_A_Asset_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
	}

	/** Get Asset.
		@return Asset used internally or by customers
	  */
	public int getA_Asset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set A_AssetCriticalDate.
		@param A_AssetCriticalDate_ID A_AssetCriticalDate	  */
	public void setA_AssetCriticalDate_ID (int A_AssetCriticalDate_ID)
	{
		if (A_AssetCriticalDate_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_A_AssetCriticalDate_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_AssetCriticalDate_ID, Integer.valueOf(A_AssetCriticalDate_ID));
	}

	/** Get A_AssetCriticalDate.
		@return A_AssetCriticalDate	  */
	public int getA_AssetCriticalDate_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_AssetCriticalDate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AD_User getAD_User() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_C_CriticalDateConcept getC_CriticalDateConcept() throws RuntimeException
    {
		return (I_C_CriticalDateConcept)MTable.get(getCtx(), I_C_CriticalDateConcept.Table_Name)
			.getPO(getC_CriticalDateConcept_ID(), get_TrxName());	}

	/** Set C_CriticalDateConcept.
		@param C_CriticalDateConcept_ID C_CriticalDateConcept	  */
	public void setC_CriticalDateConcept_ID (int C_CriticalDateConcept_ID)
	{
		if (C_CriticalDateConcept_ID < 1) 
			set_Value (COLUMNNAME_C_CriticalDateConcept_ID, null);
		else 
			set_Value (COLUMNNAME_C_CriticalDateConcept_ID, Integer.valueOf(C_CriticalDateConcept_ID));
	}

	/** Get C_CriticalDateConcept.
		@return C_CriticalDateConcept	  */
	public int getC_CriticalDateConcept_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_CriticalDateConcept_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Concept.
		@param Concept Concept	  */
	public void setConcept (String Concept)
	{
		set_Value (COLUMNNAME_Concept, Concept);
	}

	/** Get Concept.
		@return Concept	  */
	public String getConcept () 
	{
		return (String)get_Value(COLUMNNAME_Concept);
	}

	/** Set descrip2.
		@param descrip2 descrip2	  */
	public void setdescrip2 (String descrip2)
	{
		set_Value (COLUMNNAME_descrip2, descrip2);
	}

	/** Get descrip2.
		@return descrip2	  */
	public String getdescrip2 () 
	{
		return (String)get_Value(COLUMNNAME_descrip2);
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

	/** Set Due Date.
		@param DueDate 
		Date when the payment is due
	  */
	public void setDueDate (Timestamp DueDate)
	{
		set_Value (COLUMNNAME_DueDate, DueDate);
	}

	/** Get Due Date.
		@return Date when the payment is due
	  */
	public Timestamp getDueDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DueDate);
	}

	/** Set FirstDateReminder.
		@param FirstDateReminder FirstDateReminder	  */
	public void setFirstDateReminder (Timestamp FirstDateReminder)
	{
		set_Value (COLUMNNAME_FirstDateReminder, FirstDateReminder);
	}

	/** Get FirstDateReminder.
		@return FirstDateReminder	  */
	public Timestamp getFirstDateReminder () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FirstDateReminder);
	}

	/** Set SecondDateReminder.
		@param SecondDateReminder SecondDateReminder	  */
	public void setSecondDateReminder (Timestamp SecondDateReminder)
	{
		set_Value (COLUMNNAME_SecondDateReminder, SecondDateReminder);
	}

	/** Get SecondDateReminder.
		@return SecondDateReminder	  */
	public Timestamp getSecondDateReminder () 
	{
		return (Timestamp)get_Value(COLUMNNAME_SecondDateReminder);
	}
}