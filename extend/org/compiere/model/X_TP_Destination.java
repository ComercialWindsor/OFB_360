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

/** Generated Model for TP_Destination
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_TP_Destination extends PO implements I_TP_Destination, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20130930L;

    /** Standard Constructor */
    public X_TP_Destination (Properties ctx, int TP_Destination_ID, String trxName)
    {
      super (ctx, TP_Destination_ID, trxName);
      /** if (TP_Destination_ID == 0)
        {
			setName (null);
			setTP_CodDestination (null);
			setTP_Commune (null);
			setTP_Destination_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TP_Destination (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TP_Destination[")
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

	/** Set TP_CodDestination.
		@param TP_CodDestination TP_CodDestination	  */
	public void setTP_CodDestination (String TP_CodDestination)
	{
		set_Value (COLUMNNAME_TP_CodDestination, TP_CodDestination);
	}

	/** Get TP_CodDestination.
		@return TP_CodDestination	  */
	public String getTP_CodDestination () 
	{
		return (String)get_Value(COLUMNNAME_TP_CodDestination);
	}

	/** Set TP_Commune.
		@param TP_Commune TP_Commune	  */
	public void setTP_Commune (String TP_Commune)
	{
		set_Value (COLUMNNAME_TP_Commune, TP_Commune);
	}

	/** Get TP_Commune.
		@return TP_Commune	  */
	public String getTP_Commune () 
	{
		return (String)get_Value(COLUMNNAME_TP_Commune);
	}

	/** Set TP_Destination.
		@param TP_Destination_ID TP_Destination	  */
	public void setTP_Destination_ID (int TP_Destination_ID)
	{
		if (TP_Destination_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TP_Destination_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TP_Destination_ID, Integer.valueOf(TP_Destination_ID));
	}

	/** Get TP_Destination.
		@return TP_Destination	  */
	public int getTP_Destination_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_Destination_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}