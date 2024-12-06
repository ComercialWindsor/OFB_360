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

/** Generated Model for RH_AttendanceAndPunctuality
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_AttendanceAndPunctuality extends PO implements I_RH_AttendanceAndPunctuality, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20130102L;

    /** Standard Constructor */
    public X_RH_AttendanceAndPunctuality (Properties ctx, int RH_AttendanceAndPunctuality_ID, String trxName)
    {
      super (ctx, RH_AttendanceAndPunctuality_ID, trxName);
      /** if (RH_AttendanceAndPunctuality_ID == 0)
        {
			setRH_AttendanceAndPunctuality_ID (0);
			setTimeType (null);
        } */
    }

    /** Load Constructor */
    public X_RH_AttendanceAndPunctuality (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_AttendanceAndPunctuality[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
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

	/** Set RegisterDate.
		@param RegisterDate RegisterDate	  */
	public void setRegisterDate (Timestamp RegisterDate)
	{
		set_Value (COLUMNNAME_RegisterDate, RegisterDate);
	}

	/** Get RegisterDate.
		@return RegisterDate	  */
	public Timestamp getRegisterDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_RegisterDate);
	}

	/** Set RegisterTime.
		@param RegisterTime RegisterTime	  */
	public void setRegisterTime (String RegisterTime)
	{
		set_Value (COLUMNNAME_RegisterTime, RegisterTime);
	}

	/** Get RegisterTime.
		@return RegisterTime	  */
	public String getRegisterTime () 
	{
		return (String)get_Value(COLUMNNAME_RegisterTime);
	}

	/** Set RH_AttendanceAndPunctuality_ID.
		@param RH_AttendanceAndPunctuality_ID RH_AttendanceAndPunctuality_ID	  */
	public void setRH_AttendanceAndPunctuality_ID (int RH_AttendanceAndPunctuality_ID)
	{
		if (RH_AttendanceAndPunctuality_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_AttendanceAndPunctuality_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_AttendanceAndPunctuality_ID, Integer.valueOf(RH_AttendanceAndPunctuality_ID));
	}

	/** Get RH_AttendanceAndPunctuality_ID.
		@return RH_AttendanceAndPunctuality_ID	  */
	public int getRH_AttendanceAndPunctuality_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_AttendanceAndPunctuality_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** TimeType AD_Reference_ID=1000055 */
	public static final int TIMETYPE_AD_Reference_ID=1000055;
	/** Entrada = IN */
	public static final String TIMETYPE_Entrada = "IN";
	/** Salida = OUT */
	public static final String TIMETYPE_Salida = "OUT";
	/** Set TimeType.
		@param TimeType TimeType	  */
	public void setTimeType (String TimeType)
	{

		set_Value (COLUMNNAME_TimeType, TimeType);
	}

	/** Get TimeType.
		@return TimeType	  */
	public String getTimeType () 
	{
		return (String)get_Value(COLUMNNAME_TimeType);
	}
}