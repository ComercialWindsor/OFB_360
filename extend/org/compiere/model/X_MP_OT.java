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

/** Generated Model for MP_OT
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_MP_OT extends PO implements I_MP_OT, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20110102L;

    /** Standard Constructor */
    public X_MP_OT (Properties ctx, int MP_OT_ID, String trxName)
    {
      super (ctx, MP_OT_ID, trxName);
      /** if (MP_OT_ID == 0)
        {
			setA_Asset_ID (0);
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
			setDocAction (null);
			setDocStatus (null);
			setDocumentNo (null);
			setMP_OT_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_MP_OT (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_MP_OT[")
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

	public I_C_DocType getC_DocType() throws RuntimeException
    {
		return (I_C_DocType)MTable.get(getCtx(), I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
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

	public I_MP_Maintain getMP_Maintain() throws RuntimeException
    {
		return (I_MP_Maintain)MTable.get(getCtx(), I_MP_Maintain.Table_Name)
			.getPO(getMP_Maintain_ID(), get_TrxName());	}

	/** Set MP_Maintain.
		@param MP_Maintain_ID MP_Maintain	  */
	public void setMP_Maintain_ID (int MP_Maintain_ID)
	{
		if (MP_Maintain_ID < 1) 
			set_Value (COLUMNNAME_MP_Maintain_ID, null);
		else 
			set_Value (COLUMNNAME_MP_Maintain_ID, Integer.valueOf(MP_Maintain_ID));
	}

	/** Get MP_Maintain.
		@return MP_Maintain	  */
	public int getMP_Maintain_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_Maintain_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set MP_OT.
		@param MP_OT_ID MP_OT	  */
	public void setMP_OT_ID (int MP_OT_ID)
	{
		if (MP_OT_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MP_OT_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MP_OT_ID, Integer.valueOf(MP_OT_ID));
	}

	/** Get MP_OT.
		@return MP_OT	  */
	public int getMP_OT_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_OT_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_MP_OT_Request getMP_OT_Request() throws RuntimeException
    {
		return (I_MP_OT_Request)MTable.get(getCtx(), I_MP_OT_Request.Table_Name)
			.getPO(getMP_OT_Request_ID(), get_TrxName());	}

	/** Set MP_OT_Request.
		@param MP_OT_Request_ID MP_OT_Request	  */
	public void setMP_OT_Request_ID (int MP_OT_Request_ID)
	{
		if (MP_OT_Request_ID < 1) 
			set_Value (COLUMNNAME_MP_OT_Request_ID, null);
		else 
			set_Value (COLUMNNAME_MP_OT_Request_ID, Integer.valueOf(MP_OT_Request_ID));
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