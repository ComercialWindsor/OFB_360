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

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for RH_DriverEvaluation
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_DriverEvaluation extends PO implements I_RH_DriverEvaluation, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170327L;

    /** Standard Constructor */
    public X_RH_DriverEvaluation (Properties ctx, int RH_DriverEvaluation_ID, String trxName)
    {
      super (ctx, RH_DriverEvaluation_ID, trxName);
      /** if (RH_DriverEvaluation_ID == 0)
        {
			setProcessed (false);
// N
			setRH_DriverEvaluation_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_DriverEvaluation (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_DriverEvaluation[")
        .append(get_ID()).append("]");
      return sb.toString();
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
			set_Value (COLUMNNAME_C_ProjectOFB_ID, null);
		else 
			set_Value (COLUMNNAME_C_ProjectOFB_ID, Integer.valueOf(C_ProjectOFB_ID));
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

	/** Set Date.
		@param Date1 
		Date when business is not conducted
	  */
	public void setDate1 (Timestamp Date1)
	{
		set_Value (COLUMNNAME_Date1, Date1);
	}

	/** Get Date.
		@return Date when business is not conducted
	  */
	public Timestamp getDate1 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date1);
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

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Waiting Valorization = WV */
	public static final String DOCSTATUS_WaitingValorization = "WV";
	/** Aprobacion Jefe = AJ */
	public static final String DOCSTATUS_AprobacionJefe = "AJ";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
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

	/** Set RH_DriverEvaluation.
		@param RH_DriverEvaluation_ID RH_DriverEvaluation	  */
	public void setRH_DriverEvaluation_ID (int RH_DriverEvaluation_ID)
	{
		if (RH_DriverEvaluation_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_DriverEvaluation_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_DriverEvaluation_ID, Integer.valueOf(RH_DriverEvaluation_ID));
	}

	/** Get RH_DriverEvaluation.
		@return RH_DriverEvaluation	  */
	public int getRH_DriverEvaluation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_DriverEvaluation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_RH_PatternDriverEvaluation getRH_PatternDriverEvaluation() throws RuntimeException
    {
		return (I_RH_PatternDriverEvaluation)MTable.get(getCtx(), I_RH_PatternDriverEvaluation.Table_Name)
			.getPO(getRH_PatternDriverEvaluation_ID(), get_TrxName());	}

	/** Set RH_PatternDriverEvaluation.
		@param RH_PatternDriverEvaluation_ID RH_PatternDriverEvaluation	  */
	public void setRH_PatternDriverEvaluation_ID (int RH_PatternDriverEvaluation_ID)
	{
		if (RH_PatternDriverEvaluation_ID < 1) 
			set_Value (COLUMNNAME_RH_PatternDriverEvaluation_ID, null);
		else 
			set_Value (COLUMNNAME_RH_PatternDriverEvaluation_ID, Integer.valueOf(RH_PatternDriverEvaluation_ID));
	}

	/** Get RH_PatternDriverEvaluation.
		@return RH_PatternDriverEvaluation	  */
	public int getRH_PatternDriverEvaluation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_PatternDriverEvaluation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}