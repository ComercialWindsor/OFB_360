/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for OFB_RplImp
 *  @author Adempiere (generated) 
 *  @version Release 3.5.2aOFB v71G - $Id$ */
public class X_OFB_RplImp extends PO implements I_OFB_RplImp, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_OFB_RplImp (Properties ctx, int OFB_RplImp_ID, String trxName)
    {
      super (ctx, OFB_RplImp_ID, trxName);
      /** if (OFB_RplImp_ID == 0)
        {
			setOFB_RplImp_ID (0);
			setProcessed (false);
			setRemoteID (0);
			setRemoteRecord_ID (0);
        } */
    }

    /** Load Constructor */
    public X_OFB_RplImp (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_OFB_RplImp[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set OFB_RplImp.
		@param OFB_RplImp_ID OFB_RplImp	  */
	public void setOFB_RplImp_ID (int OFB_RplImp_ID)
	{
		if (OFB_RplImp_ID < 1)
			 throw new IllegalArgumentException ("OFB_RplImp_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_OFB_RplImp_ID, Integer.valueOf(OFB_RplImp_ID));
	}

	/** Get OFB_RplImp.
		@return OFB_RplImp	  */
	public int getOFB_RplImp_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_OFB_RplImp_ID);
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

	/** Set RemoteHost.
		@param RemoteHost RemoteHost	  */
	public void setRemoteHost (String RemoteHost)
	{
		set_Value (COLUMNNAME_RemoteHost, RemoteHost);
	}

	/** Get RemoteHost.
		@return RemoteHost	  */
	public String getRemoteHost () 
	{
		return (String)get_Value(COLUMNNAME_RemoteHost);
	}

	/** Set RemoteID.
		@param RemoteID RemoteID	  */
	public void setRemoteID (int RemoteID)
	{
		set_Value (COLUMNNAME_RemoteID, Integer.valueOf(RemoteID));
	}

	/** Get RemoteID.
		@return RemoteID	  */
	public int getRemoteID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RemoteID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set RemoteRecord_ID.
		@param RemoteRecord_ID RemoteRecord_ID	  */
	public void setRemoteRecord_ID (int RemoteRecord_ID)
	{
		if (RemoteRecord_ID < 1)
			 throw new IllegalArgumentException ("RemoteRecord_ID is mandatory.");
		set_Value (COLUMNNAME_RemoteRecord_ID, Integer.valueOf(RemoteRecord_ID));
	}

	/** Get RemoteRecord_ID.
		@return RemoteRecord_ID	  */
	public int getRemoteRecord_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RemoteRecord_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DB Table Name.
		@param TableName 
		Name of the table in the database
	  */
	public void setTableName (String TableName)
	{
		set_Value (COLUMNNAME_TableName, TableName);
	}

	/** Get DB Table Name.
		@return Name of the table in the database
	  */
	public String getTableName () 
	{
		return (String)get_Value(COLUMNNAME_TableName);
	}

	/** Set xml.
		@param xml xml	  */
	public void setxml (String xml)
	{
		set_Value (COLUMNNAME_xml, xml);
	}

	/** Get xml.
		@return xml	  */
	public String getxml () 
	{
		return (String)get_Value(COLUMNNAME_xml);
	}
}