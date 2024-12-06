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
import org.compiere.model.*;

/** Generated Model for MP_OT_Request
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_MP_OT_Request extends PO implements I_MP_OT_Request, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20110102L;

    /** Standard Constructor */
    public X_MP_OT_Request (Properties ctx, int MP_OT_Request_ID, String trxName)
    {
      super (ctx, MP_OT_Request_ID, trxName);
      /** if (MP_OT_Request_ID == 0)
        {
			setA_Asset_ID (0);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
			setDateRequired (new Timestamp( System.currentTimeMillis() ));
			setDocStatus (null);
			setDocumentNo (null);
			setMP_OT_Request_ID (0);
			setOT_Request_Type (null);
			setPriorityRule (null);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_MP_OT_Request (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_MP_OT_Request[")
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
			set_Value (COLUMNNAME_A_Asset_ID, null);
		else 
			set_Value (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
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

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** Set Date Required.
		@param DateRequired 
		Date when required
	  */
	public void setDateRequired (Timestamp DateRequired)
	{
		set_Value (COLUMNNAME_DateRequired, DateRequired);
	}

	/** Get Date Required.
		@return Date when required
	  */
	public Timestamp getDateRequired () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateRequired);
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

	/** DocStatus AD_Reference_ID=1000017 */
	public static final int DOCSTATUS_AD_Reference_ID=1000017;
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Approve = AP */
	public static final String DOCSTATUS_Approve = "AP";
	/** Void = VO */
	public static final String DOCSTATUS_Void = "VO";
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

	public I_MP_JobStandar getMP_JobStandar() throws RuntimeException
    {
		return (I_MP_JobStandar)MTable.get(getCtx(), I_MP_JobStandar.Table_Name)
			.getPO(getMP_JobStandar_ID(), get_TrxName());	}

	/** Set MP_JobStandard.
		@param MP_JobStandar_ID MP_JobStandard	  */
	public void setMP_JobStandar_ID (int MP_JobStandar_ID)
	{
		if (MP_JobStandar_ID < 1) 
			set_Value (COLUMNNAME_MP_JobStandar_ID, null);
		else 
			set_Value (COLUMNNAME_MP_JobStandar_ID, Integer.valueOf(MP_JobStandar_ID));
	}

	/** Get MP_JobStandard.
		@return MP_JobStandard	  */
	public int getMP_JobStandar_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_JobStandar_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set MP_OT_Request.
		@param MP_OT_Request_ID MP_OT_Request	  */
	public void setMP_OT_Request_ID (int MP_OT_Request_ID)
	{
		if (MP_OT_Request_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MP_OT_Request_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MP_OT_Request_ID, Integer.valueOf(MP_OT_Request_ID));
	}

	/** Get MP_OT_Request.
		@return MP_OT_Request	  */
	public int getMP_OT_Request_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_OT_Request_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** OT_Request_Type AD_Reference_ID=1000016 */
	public static final int OT_REQUEST_TYPE_AD_Reference_ID=1000016;
	/** Repair = RP */
	public static final String OT_REQUEST_TYPE_Repair = "RP";
	/** CheckUp = RV */
	public static final String OT_REQUEST_TYPE_CheckUp = "RV";
	/** Set OT_Request_Type.
		@param OT_Request_Type OT_Request_Type	  */
	public void setOT_Request_Type (String OT_Request_Type)
	{

		set_Value (COLUMNNAME_OT_Request_Type, OT_Request_Type);
	}

	/** Get OT_Request_Type.
		@return OT_Request_Type	  */
	public String getOT_Request_Type () 
	{
		return (String)get_Value(COLUMNNAME_OT_Request_Type);
	}

	/** PriorityRule AD_Reference_ID=154 */
	public static final int PRIORITYRULE_AD_Reference_ID=154;
	/** High = 3 */
	public static final String PRIORITYRULE_High = "3";
	/** Medium = 5 */
	public static final String PRIORITYRULE_Medium = "5";
	/** Low = 7 */
	public static final String PRIORITYRULE_Low = "7";
	/** Urgent = 1 */
	public static final String PRIORITYRULE_Urgent = "1";
	/** Minor = 9 */
	public static final String PRIORITYRULE_Minor = "9";
	/** Set Priority.
		@param PriorityRule 
		Priority of a document
	  */
	public void setPriorityRule (String PriorityRule)
	{

		set_Value (COLUMNNAME_PriorityRule, PriorityRule);
	}

	/** Get Priority.
		@return Priority of a document
	  */
	public String getPriorityRule () 
	{
		return (String)get_Value(COLUMNNAME_PriorityRule);
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