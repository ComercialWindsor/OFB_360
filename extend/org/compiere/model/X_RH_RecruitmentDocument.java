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

/** Generated Model for RH_RecruitmentDocument
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_RecruitmentDocument extends PO implements I_RH_RecruitmentDocument, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170327L;

    /** Standard Constructor */
    public X_RH_RecruitmentDocument (Properties ctx, int RH_RecruitmentDocument_ID, String trxName)
    {
      super (ctx, RH_RecruitmentDocument_ID, trxName);
      /** if (RH_RecruitmentDocument_ID == 0)
        {
			setName (null);
			setProcessed (false);
			setRH_RecruitmentDocument_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_RecruitmentDocument (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_RecruitmentDocument[")
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

	/** Set RH_RecruitmentDocument.
		@param RH_RecruitmentDocument_ID RH_RecruitmentDocument	  */
	public void setRH_RecruitmentDocument_ID (int RH_RecruitmentDocument_ID)
	{
		if (RH_RecruitmentDocument_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentDocument_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentDocument_ID, Integer.valueOf(RH_RecruitmentDocument_ID));
	}

	/** Get RH_RecruitmentDocument.
		@return RH_RecruitmentDocument	  */
	public int getRH_RecruitmentDocument_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_RecruitmentDocument_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_RH_RecruitmentRequisition getRH_RecruitmentRequisition() throws RuntimeException
    {
		return (I_RH_RecruitmentRequisition)MTable.get(getCtx(), I_RH_RecruitmentRequisition.Table_Name)
			.getPO(getRH_RecruitmentRequisition_ID(), get_TrxName());	}

	/** Set RH_RecruitmentRequisition.
		@param RH_RecruitmentRequisition_ID RH_RecruitmentRequisition	  */
	public void setRH_RecruitmentRequisition_ID (int RH_RecruitmentRequisition_ID)
	{
		if (RH_RecruitmentRequisition_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentRequisition_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentRequisition_ID, Integer.valueOf(RH_RecruitmentRequisition_ID));
	}

	/** Get RH_RecruitmentRequisition.
		@return RH_RecruitmentRequisition	  */
	public int getRH_RecruitmentRequisition_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_RecruitmentRequisition_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}