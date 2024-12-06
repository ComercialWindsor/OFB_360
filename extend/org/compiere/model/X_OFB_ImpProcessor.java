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
import org.compiere.util.KeyNamePair;

/** Generated Model for OFB_ImpProcessor
 *  @author Adempiere (generated) 
 *  @version Release 3.5.2aOFB v71G - $Id$ */
public class X_OFB_ImpProcessor extends PO implements I_OFB_ImpProcessor, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_OFB_ImpProcessor (Properties ctx, int OFB_ImpProcessor_ID, String trxName)
    {
      super (ctx, OFB_ImpProcessor_ID, trxName);
      /** if (OFB_ImpProcessor_ID == 0)
        {
			setName (null);
			setOFB_ImpProcessor_ID (0);
        } */
    }

    /** Load Constructor */
    public X_OFB_ImpProcessor (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_OFB_ImpProcessor[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Account.
		@param Account Account	  */
	public void setAccount (String Account)
	{
		set_Value (COLUMNNAME_Account, Account);
	}

	/** Get Account.
		@return Account	  */
	public String getAccount () 
	{
		return (String)get_Value(COLUMNNAME_Account);
	}

	/** Set Database Name.
		@param DBInstance 
		Database Name
	  */
	public void setDBInstance (String DBInstance)
	{
		set_Value (COLUMNNAME_DBInstance, DBInstance);
	}

	/** Get Database Name.
		@return Database Name
	  */
	public String getDBInstance () 
	{
		return (String)get_Value(COLUMNNAME_DBInstance);
	}

	/** DBType AD_Reference_ID=1000011 */
	public static final int DBTYPE_AD_Reference_ID=1000011;
	/** Oracle = Orc */
	public static final String DBTYPE_Oracle = "Orc";
	/** PostgreSQL = Pos */
	public static final String DBTYPE_PostgreSQL = "Pos";
	/** Set DBType.
		@param DBType DBType	  */
	public void setDBType (String DBType)
	{

		if (DBType == null || DBType.equals("Orc") || DBType.equals("Pos")); else throw new IllegalArgumentException ("DBType Invalid value - " + DBType + " - Reference_ID=1000011 - Orc - Pos");		set_Value (COLUMNNAME_DBType, DBType);
	}

	/** Get DBType.
		@return DBType	  */
	public String getDBType () 
	{
		return (String)get_Value(COLUMNNAME_DBType);
	}

	/** Set Host.
		@param Host Host	  */
	public void setHost (String Host)
	{
		set_Value (COLUMNNAME_Host, Host);
	}

	/** Get Host.
		@return Host	  */
	public String getHost () 
	{
		return (String)get_Value(COLUMNNAME_Host);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		if (Name == null)
			throw new IllegalArgumentException ("Name is mandatory.");
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

	/** Set OFB_ImpProcessor.
		@param OFB_ImpProcessor_ID OFB_ImpProcessor	  */
	public void setOFB_ImpProcessor_ID (int OFB_ImpProcessor_ID)
	{
		if (OFB_ImpProcessor_ID < 1)
			 throw new IllegalArgumentException ("OFB_ImpProcessor_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_OFB_ImpProcessor_ID, Integer.valueOf(OFB_ImpProcessor_ID));
	}

	/** Get OFB_ImpProcessor.
		@return OFB_ImpProcessor	  */
	public int getOFB_ImpProcessor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_OFB_ImpProcessor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set PasswordInfo.
		@param PasswordInfo PasswordInfo	  */
	public void setPasswordInfo (String PasswordInfo)
	{
		set_Value (COLUMNNAME_PasswordInfo, PasswordInfo);
	}

	/** Get PasswordInfo.
		@return PasswordInfo	  */
	public String getPasswordInfo () 
	{
		return (String)get_Value(COLUMNNAME_PasswordInfo);
	}

	/** Set Port.
		@param Port Port	  */
	public void setPort (int Port)
	{
		set_Value (COLUMNNAME_Port, Integer.valueOf(Port));
	}

	/** Get Port.
		@return Port	  */
	public int getPort () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Port);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}