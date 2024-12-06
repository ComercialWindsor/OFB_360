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

/** Generated Model for RH_AttendancePunctualityLog
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_AttendancePunctualityLog extends PO implements I_RH_AttendancePunctualityLog, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20130102L;

    /** Standard Constructor */
    public X_RH_AttendancePunctualityLog (Properties ctx, int RH_AttendancePunctualityLog_ID, String trxName)
    {
      super (ctx, RH_AttendancePunctualityLog_ID, trxName);
      /** if (RH_AttendancePunctualityLog_ID == 0)
        {
			setRH_AttendancePunctualityLog_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_AttendancePunctualityLog (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_AttendancePunctualityLog[")
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

	/** Set RH_AttendancePunctualityLog.
		@param RH_AttendancePunctualityLog_ID RH_AttendancePunctualityLog	  */
	public void setRH_AttendancePunctualityLog_ID (int RH_AttendancePunctualityLog_ID)
	{
		if (RH_AttendancePunctualityLog_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_AttendancePunctualityLog_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_AttendancePunctualityLog_ID, Integer.valueOf(RH_AttendancePunctualityLog_ID));
	}

	/** Get RH_AttendancePunctualityLog.
		@return RH_AttendancePunctualityLog	  */
	public int getRH_AttendancePunctualityLog_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_AttendancePunctualityLog_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}