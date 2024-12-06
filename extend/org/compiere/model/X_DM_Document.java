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

/** Generated Model for DM_Document
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_DM_Document extends PO implements I_DM_Document, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120117L;

    /** Standard Constructor */
    public X_DM_Document (Properties ctx, int DM_Document_ID, String trxName)
    {
      super (ctx, DM_Document_ID, trxName);
      /** if (DM_Document_ID == 0)
        {
			setAmt (Env.ZERO);
			setDM_Document_ID (0);
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_DM_Document (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_DM_Document[")
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

	public I_C_Project getC_Project() throws RuntimeException
    {
		return (I_C_Project)MTable.get(getCtx(), I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_Value (COLUMNNAME_C_Project_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
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

	public I_DM_Document getDM_DocumentParent() throws RuntimeException
    {
		return (I_DM_Document)MTable.get(getCtx(), I_DM_Document.Table_Name)
			.getPO(getDM_DocumentParent_ID(), get_TrxName());	}

	/** Set DM_DocumentParent_ID.
		@param DM_DocumentParent_ID DM_DocumentParent_ID	  */
	public void setDM_DocumentParent_ID (int DM_DocumentParent_ID)
	{
		if (DM_DocumentParent_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DM_DocumentParent_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DM_DocumentParent_ID, Integer.valueOf(DM_DocumentParent_ID));
	}

	/** Get DM_DocumentParent_ID.
		@return DM_DocumentParent_ID	  */
	public int getDM_DocumentParent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_DocumentParent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** DM_DocumentType AD_Reference_ID=1000053 */
	public static final int DM_DOCUMENTTYPE_AD_Reference_ID=1000053;
	/** Acuerdo Core = AC */
	public static final String DM_DOCUMENTTYPE_AcuerdoCore = "AC";
	/** Resolucion Asignacion = RS */
	public static final String DM_DOCUMENTTYPE_ResolucionAsignacion = "RS";
	/** Memo = ME */
	public static final String DM_DOCUMENTTYPE_Memo = "ME";
	/** Convenio Mandato = CM */
	public static final String DM_DOCUMENTTYPE_ConvenioMandato = "CM";
	/** Set DM_DocumentType.
		@param DM_DocumentType DM_DocumentType	  */
	public void setDM_DocumentType (String DM_DocumentType)
	{

		set_Value (COLUMNNAME_DM_DocumentType, DM_DocumentType);
	}

	/** Get DM_DocumentType.
		@return DM_DocumentType	  */
	public String getDM_DocumentType () 
	{
		return (String)get_Value(COLUMNNAME_DM_DocumentType);
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
			set_Value (COLUMNNAME_DM_MandateAgreement_ID, null);
		else 
			set_Value (COLUMNNAME_DM_MandateAgreement_ID, Integer.valueOf(DM_MandateAgreement_ID));
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

	public I_DM_Document getDM_RS() throws RuntimeException
    {
		return (I_DM_Document)MTable.get(getCtx(), I_DM_Document.Table_Name)
			.getPO(getDM_RS_ID(), get_TrxName());	}

	/** Set DM_RS_ID.
		@param DM_RS_ID DM_RS_ID	  */
	public void setDM_RS_ID (int DM_RS_ID)
	{
		if (DM_RS_ID < 1) 
			set_Value (COLUMNNAME_DM_RS_ID, null);
		else 
			set_Value (COLUMNNAME_DM_RS_ID, Integer.valueOf(DM_RS_ID));
	}

	/** Get DM_RS_ID.
		@return DM_RS_ID	  */
	public int getDM_RS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_RS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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