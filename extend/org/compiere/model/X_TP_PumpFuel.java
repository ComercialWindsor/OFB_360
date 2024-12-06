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

/** Generated Model for TP_PumpFuel
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_TP_PumpFuel extends PO implements I_TP_PumpFuel, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160722L;

    /** Standard Constructor */
    public X_TP_PumpFuel (Properties ctx, int TP_PumpFuel_ID, String trxName)
    {
      super (ctx, TP_PumpFuel_ID, trxName);
      /** if (TP_PumpFuel_ID == 0)
        {
			setTP_PumpFuel_ID (0);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_TP_PumpFuel (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TP_PumpFuel[")
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

	/** Line AD_Reference_ID=1000087 */
	public static final int LINE_AD_Reference_ID=1000087;
	/** 1 = 1 */
	public static final String LINE_1 = "1";
	/** 2 = 2 */
	public static final String LINE_2 = "2";
	/** 3 = 3 */
	public static final String LINE_3 = "3";
	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (String Line)
	{

		set_Value (COLUMNNAME_Line, Line);
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public String getLine () 
	{
		return (String)get_Value(COLUMNNAME_Line);
	}

	/** Set TP_PumpFuel.
		@param TP_PumpFuel_ID TP_PumpFuel	  */
	public void setTP_PumpFuel_ID (int TP_PumpFuel_ID)
	{
		if (TP_PumpFuel_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TP_PumpFuel_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TP_PumpFuel_ID, Integer.valueOf(TP_PumpFuel_ID));
	}

	/** Get TP_PumpFuel.
		@return TP_PumpFuel	  */
	public int getTP_PumpFuel_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_PumpFuel_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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