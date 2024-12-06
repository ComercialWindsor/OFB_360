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

/** Generated Model for PM_Approval
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_PM_Approval extends PO implements I_PM_Approval, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120117L;

    /** Standard Constructor */
    public X_PM_Approval (Properties ctx, int PM_Approval_ID, String trxName)
    {
      super (ctx, PM_Approval_ID, trxName);
      /** if (PM_Approval_ID == 0)
        {
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setPM_Approval_ID (0);
			setPM_Scoring_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_PM_Approval (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_PM_Approval[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set PM_Approval.
		@param PM_Approval_ID PM_Approval	  */
	public void setPM_Approval_ID (int PM_Approval_ID)
	{
		if (PM_Approval_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PM_Approval_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PM_Approval_ID, Integer.valueOf(PM_Approval_ID));
	}

	/** Get PM_Approval.
		@return PM_Approval	  */
	public int getPM_Approval_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PM_Approval_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_PM_Scoring getPM_Scoring() throws RuntimeException
    {
		return (I_PM_Scoring)MTable.get(getCtx(), I_PM_Scoring.Table_Name)
			.getPO(getPM_Scoring_ID(), get_TrxName());	}

	/** Set PM_Scoring.
		@param PM_Scoring_ID PM_Scoring	  */
	public void setPM_Scoring_ID (int PM_Scoring_ID)
	{
		if (PM_Scoring_ID < 1) 
			set_Value (COLUMNNAME_PM_Scoring_ID, null);
		else 
			set_Value (COLUMNNAME_PM_Scoring_ID, Integer.valueOf(PM_Scoring_ID));
	}

	/** Get PM_Scoring.
		@return PM_Scoring	  */
	public int getPM_Scoring_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PM_Scoring_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_PM_Tender getPM_Tender() throws RuntimeException
    {
		return (I_PM_Tender)MTable.get(getCtx(), I_PM_Tender.Table_Name)
			.getPO(getPM_Tender_ID(), get_TrxName());	}

	/** Set PM_Tender.
		@param PM_Tender_ID PM_Tender	  */
	public void setPM_Tender_ID (int PM_Tender_ID)
	{
		if (PM_Tender_ID < 1) 
			set_Value (COLUMNNAME_PM_Tender_ID, null);
		else 
			set_Value (COLUMNNAME_PM_Tender_ID, Integer.valueOf(PM_Tender_ID));
	}

	/** Get PM_Tender.
		@return PM_Tender	  */
	public int getPM_Tender_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PM_Tender_ID);
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

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}