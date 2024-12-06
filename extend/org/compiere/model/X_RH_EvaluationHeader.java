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
import org.compiere.util.KeyNamePair;

/** Generated Model for RH_EvaluationHeader
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_EvaluationHeader extends PO implements I_RH_EvaluationHeader, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171226L;

    /** Standard Constructor */
    public X_RH_EvaluationHeader (Properties ctx, int RH_EvaluationHeader_ID, String trxName)
    {
      super (ctx, RH_EvaluationHeader_ID, trxName);
      /** if (RH_EvaluationHeader_ID == 0)
        {
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setProcessed (false);
			setRH_EvaluationHeader_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_EvaluationHeader (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_EvaluationHeader[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AD_OrgRef_ID.
		@param AD_OrgRef_ID AD_OrgRef_ID	  */
	public void setAD_OrgRef_ID (int AD_OrgRef_ID)
	{
		if (AD_OrgRef_ID < 1) 
			set_Value (COLUMNNAME_AD_OrgRef_ID, null);
		else 
			set_Value (COLUMNNAME_AD_OrgRef_ID, Integer.valueOf(AD_OrgRef_ID));
	}

	/** Get AD_OrgRef_ID.
		@return AD_OrgRef_ID	  */
	public int getAD_OrgRef_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_OrgRef_ID);
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

	/** Set Document Action.
		@param DocAction 
		The targeted status of the document
	  */
	public void setDocAction (String DocAction)
	{
		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction () 
	{
		return (String)get_Value(COLUMNNAME_DocAction);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
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

	public I_RH_EvaluationGuide getRH_EvaluationGuide() throws RuntimeException
    {
		return (I_RH_EvaluationGuide)MTable.get(getCtx(), I_RH_EvaluationGuide.Table_Name)
			.getPO(getRH_EvaluationGuide_ID(), get_TrxName());	}

	/** Set RH_EvaluationGuide.
		@param RH_EvaluationGuide_ID RH_EvaluationGuide	  */
	public void setRH_EvaluationGuide_ID (int RH_EvaluationGuide_ID)
	{
		if (RH_EvaluationGuide_ID < 1) 
			set_Value (COLUMNNAME_RH_EvaluationGuide_ID, null);
		else 
			set_Value (COLUMNNAME_RH_EvaluationGuide_ID, Integer.valueOf(RH_EvaluationGuide_ID));
	}

	/** Get RH_EvaluationGuide.
		@return RH_EvaluationGuide	  */
	public int getRH_EvaluationGuide_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_EvaluationGuide_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set RH_EvaluationHeader.
		@param RH_EvaluationHeader_ID RH_EvaluationHeader	  */
	public void setRH_EvaluationHeader_ID (int RH_EvaluationHeader_ID)
	{
		if (RH_EvaluationHeader_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_EvaluationHeader_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_EvaluationHeader_ID, Integer.valueOf(RH_EvaluationHeader_ID));
	}

	/** Get RH_EvaluationHeader.
		@return RH_EvaluationHeader	  */
	public int getRH_EvaluationHeader_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_EvaluationHeader_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}