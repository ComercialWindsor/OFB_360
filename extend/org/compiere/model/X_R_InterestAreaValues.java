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

/** Generated Model for R_InterestAreaValues
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_R_InterestAreaValues extends PO implements I_R_InterestAreaValues, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150125L;

    /** Standard Constructor */
    public X_R_InterestAreaValues (Properties ctx, int R_InterestAreaValues_ID, String trxName)
    {
      super (ctx, R_InterestAreaValues_ID, trxName);
      /** if (R_InterestAreaValues_ID == 0)
        {
			setR_InterestArea_ID (0);
			setR_InterestAreaValues_ID (0);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_R_InterestAreaValues (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_R_InterestAreaValues[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_R_InterestArea getR_InterestArea() throws RuntimeException
    {
		return (I_R_InterestArea)MTable.get(getCtx(), I_R_InterestArea.Table_Name)
			.getPO(getR_InterestArea_ID(), get_TrxName());	}

	/** Set Interest Area.
		@param R_InterestArea_ID 
		Interest Area or Topic
	  */
	public void setR_InterestArea_ID (int R_InterestArea_ID)
	{
		if (R_InterestArea_ID < 1) 
			set_Value (COLUMNNAME_R_InterestArea_ID, null);
		else 
			set_Value (COLUMNNAME_R_InterestArea_ID, Integer.valueOf(R_InterestArea_ID));
	}

	/** Get Interest Area.
		@return Interest Area or Topic
	  */
	public int getR_InterestArea_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_InterestArea_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set R_InterestAreaValues.
		@param R_InterestAreaValues_ID R_InterestAreaValues	  */
	public void setR_InterestAreaValues_ID (int R_InterestAreaValues_ID)
	{
		if (R_InterestAreaValues_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_R_InterestAreaValues_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_R_InterestAreaValues_ID, Integer.valueOf(R_InterestAreaValues_ID));
	}

	/** Get R_InterestAreaValues.
		@return R_InterestAreaValues	  */
	public int getR_InterestAreaValues_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_InterestAreaValues_ID);
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