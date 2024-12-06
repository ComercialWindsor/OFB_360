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

/** Generated Model for PM_Tender
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_PM_Tender extends PO implements I_PM_Tender, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120117L;

    /** Standard Constructor */
    public X_PM_Tender (Properties ctx, int PM_Tender_ID, String trxName)
    {
      super (ctx, PM_Tender_ID, trxName);
      /** if (PM_Tender_ID == 0)
        {
			setDocStatus (null);
// DR
			setName (null);
			setPM_Tender_ID (0);
			setProcessed (false);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_PM_Tender (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_PM_Tender[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Milestone1.
		@param Milestone1 Milestone1	  */
	public void setMilestone1 (Timestamp Milestone1)
	{
		set_Value (COLUMNNAME_Milestone1, Milestone1);
	}

	/** Get Milestone1.
		@return Milestone1	  */
	public Timestamp getMilestone1 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Milestone1);
	}

	/** Set Milestone2.
		@param Milestone2 Milestone2	  */
	public void setMilestone2 (Timestamp Milestone2)
	{
		set_Value (COLUMNNAME_Milestone2, Milestone2);
	}

	/** Get Milestone2.
		@return Milestone2	  */
	public Timestamp getMilestone2 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Milestone2);
	}

	/** Set Milestone3.
		@param Milestone3 Milestone3	  */
	public void setMilestone3 (Timestamp Milestone3)
	{
		set_Value (COLUMNNAME_Milestone3, Milestone3);
	}

	/** Get Milestone3.
		@return Milestone3	  */
	public Timestamp getMilestone3 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Milestone3);
	}

	/** Set Milestone4.
		@param Milestone4 Milestone4	  */
	public void setMilestone4 (Timestamp Milestone4)
	{
		set_Value (COLUMNNAME_Milestone4, Milestone4);
	}

	/** Get Milestone4.
		@return Milestone4	  */
	public Timestamp getMilestone4 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Milestone4);
	}

	/** Set Milestone5.
		@param Milestone5 Milestone5	  */
	public void setMilestone5 (Timestamp Milestone5)
	{
		set_Value (COLUMNNAME_Milestone5, Milestone5);
	}

	/** Get Milestone5.
		@return Milestone5	  */
	public Timestamp getMilestone5 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Milestone5);
	}

	/** Set Milestone6.
		@param Milestone6 Milestone6	  */
	public void setMilestone6 (Timestamp Milestone6)
	{
		set_Value (COLUMNNAME_Milestone6, Milestone6);
	}

	/** Get Milestone6.
		@return Milestone6	  */
	public Timestamp getMilestone6 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Milestone6);
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

	/** Set PM_Tender.
		@param PM_Tender_ID PM_Tender	  */
	public void setPM_Tender_ID (int PM_Tender_ID)
	{
		if (PM_Tender_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PM_Tender_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PM_Tender_ID, Integer.valueOf(PM_Tender_ID));
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

	/** PM_TenderType AD_Reference_ID=1000052 */
	public static final int PM_TENDERTYPE_AD_Reference_ID=1000052;
	/** Cultura = CU */
	public static final String PM_TENDERTYPE_Cultura = "CU";
	/** Ciudadania = CI */
	public static final String PM_TENDERTYPE_Ciudadania = "CI";
	/** Deporte = DE */
	public static final String PM_TENDERTYPE_Deporte = "DE";
	/** Seguridad = SE */
	public static final String PM_TENDERTYPE_Seguridad = "SE";
	/** Set PM_TenderType.
		@param PM_TenderType PM_TenderType	  */
	public void setPM_TenderType (String PM_TenderType)
	{

		set_Value (COLUMNNAME_PM_TenderType, PM_TenderType);
	}

	/** Get PM_TenderType.
		@return PM_TenderType	  */
	public String getPM_TenderType () 
	{
		return (String)get_Value(COLUMNNAME_PM_TenderType);
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

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}