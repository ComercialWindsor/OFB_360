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
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for RH_HoursUsedDetail
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_HoursUsedDetail extends PO implements I_RH_HoursUsedDetail, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20161026L;

    /** Standard Constructor */
    public X_RH_HoursUsedDetail (Properties ctx, int RH_HoursUsedDetail_ID, String trxName)
    {
      super (ctx, RH_HoursUsedDetail_ID, trxName);
      /** if (RH_HoursUsedDetail_ID == 0)
        {
			setRH_HoursUsedDetail_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_HoursUsedDetail (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_HoursUsedDetail[")
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

	/** Set hours.
		@param hours hours	  */
	public void sethours (BigDecimal hours)
	{
		set_Value (COLUMNNAME_hours, hours);
	}

	/** Get hours.
		@return hours	  */
	public BigDecimal gethours () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_hours);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_RH_AdministrativeRequests getRH_AdministrativeRequests() throws RuntimeException
    {
		return (I_RH_AdministrativeRequests)MTable.get(getCtx(), I_RH_AdministrativeRequests.Table_Name)
			.getPO(getRH_AdministrativeRequests_ID(), get_TrxName());	}

	/** Set RH_AdministrativeRequests_ID.
		@param RH_AdministrativeRequests_ID RH_AdministrativeRequests_ID	  */
	public void setRH_AdministrativeRequests_ID (int RH_AdministrativeRequests_ID)
	{
		if (RH_AdministrativeRequests_ID < 1) 
			set_Value (COLUMNNAME_RH_AdministrativeRequests_ID, null);
		else 
			set_Value (COLUMNNAME_RH_AdministrativeRequests_ID, Integer.valueOf(RH_AdministrativeRequests_ID));
	}

	/** Get RH_AdministrativeRequests_ID.
		@return RH_AdministrativeRequests_ID	  */
	public int getRH_AdministrativeRequests_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_AdministrativeRequests_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_RH_HoursAvailable getRH_HoursAvailable() throws RuntimeException
    {
		return (I_RH_HoursAvailable)MTable.get(getCtx(), I_RH_HoursAvailable.Table_Name)
			.getPO(getRH_HoursAvailable_ID(), get_TrxName());	}

	/** Set RH_HoursAvailable.
		@param RH_HoursAvailable_ID RH_HoursAvailable	  */
	public void setRH_HoursAvailable_ID (int RH_HoursAvailable_ID)
	{
		if (RH_HoursAvailable_ID < 1) 
			set_Value (COLUMNNAME_RH_HoursAvailable_ID, null);
		else 
			set_Value (COLUMNNAME_RH_HoursAvailable_ID, Integer.valueOf(RH_HoursAvailable_ID));
	}

	/** Get RH_HoursAvailable.
		@return RH_HoursAvailable	  */
	public int getRH_HoursAvailable_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_HoursAvailable_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set RH_HoursUsedDetail_ID.
		@param RH_HoursUsedDetail_ID RH_HoursUsedDetail_ID	  */
	public void setRH_HoursUsedDetail_ID (int RH_HoursUsedDetail_ID)
	{
		if (RH_HoursUsedDetail_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_HoursUsedDetail_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_HoursUsedDetail_ID, Integer.valueOf(RH_HoursUsedDetail_ID));
	}

	/** Get RH_HoursUsedDetail_ID.
		@return RH_HoursUsedDetail_ID	  */
	public int getRH_HoursUsedDetail_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_HoursUsedDetail_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}