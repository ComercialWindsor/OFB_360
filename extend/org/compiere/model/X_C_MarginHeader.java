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
import java.util.Properties;

/** Generated Model for C_MarginHeader
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_C_MarginHeader extends PO implements I_C_MarginHeader, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170519L;

    /** Standard Constructor */
    public X_C_MarginHeader (Properties ctx, int C_MarginHeader_ID, String trxName)
    {
      super (ctx, C_MarginHeader_ID, trxName);
      /** if (C_MarginHeader_ID == 0)
        {
			setC_MarginHeader_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_MarginHeader (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_MarginHeader[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set C_MarginHeader_ID.
		@param C_MarginHeader_ID C_MarginHeader_ID	  */
	public void setC_MarginHeader_ID (int C_MarginHeader_ID)
	{
		if (C_MarginHeader_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_MarginHeader_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_MarginHeader_ID, Integer.valueOf(C_MarginHeader_ID));
	}

	/** Get C_MarginHeader_ID.
		@return C_MarginHeader_ID	  */
	public int getC_MarginHeader_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_MarginHeader_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Period getC_Period() throws RuntimeException
    {
		return (I_C_Period)MTable.get(getCtx(), I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set OfbButton.
		@param OfbButton OfbButton	  */
	public void setOfbButton (String OfbButton)
	{
		set_Value (COLUMNNAME_OfbButton, OfbButton);
	}

	/** Get OfbButton.
		@return OfbButton	  */
	public String getOfbButton () 
	{
		return (String)get_Value(COLUMNNAME_OfbButton);
	}
}