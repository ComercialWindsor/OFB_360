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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for MP_AssetMeter_Log
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_MP_AssetMeter_Log extends PO implements I_MP_AssetMeter_Log, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20130711L;

    /** Standard Constructor */
    public X_MP_AssetMeter_Log (Properties ctx, int MP_AssetMeter_Log_ID, String trxName)
    {
      super (ctx, MP_AssetMeter_Log_ID, trxName);
      /** if (MP_AssetMeter_Log_ID == 0)
        {
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
			setMP_AssetMeter_ID (0);
			setMP_AssetMeter_Log_ID (0);
        } */
    }

    /** Load Constructor */
    public X_MP_AssetMeter_Log (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_MP_AssetMeter_Log[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_User getAD_User() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set Usuario.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get Usuario.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Amount.
		@param Amt 
		Amount
	  */
	public void setAmt (BigDecimal Amt)
	{
		set_Value (COLUMNNAME_Amt, Amt);
	}

	/** Get Amount.
		@return Amount
	  */
	public BigDecimal getAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currentamt.
		@param currentamt currentamt	  */
	public void setcurrentamt (BigDecimal currentamt)
	{
		set_Value (COLUMNNAME_currentamt, currentamt);
	}

	/** Get currentamt.
		@return currentamt	  */
	public BigDecimal getcurrentamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currentamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set hourend.
		@param hourend hourend	  */
	public void sethourend (Timestamp hourend)
	{
		set_Value (COLUMNNAME_hourend, hourend);
	}

	/** Get hourend.
		@return hourend	  */
	public Timestamp gethourend () 
	{
		return (Timestamp)get_Value(COLUMNNAME_hourend);
	}

	/** Set hourstart.
		@param hourstart hourstart	  */
	public void sethourstart (Timestamp hourstart)
	{
		set_Value (COLUMNNAME_hourstart, hourstart);
	}

	/** Get hourstart.
		@return hourstart	  */
	public Timestamp gethourstart () 
	{
		return (Timestamp)get_Value(COLUMNNAME_hourstart);
	}

	public I_MP_AssetMeter getMP_AssetMeter() throws RuntimeException
    {
		return (I_MP_AssetMeter)MTable.get(getCtx(), I_MP_AssetMeter.Table_Name)
			.getPO(getMP_AssetMeter_ID(), get_TrxName());	}

	/** Set MP_AssetMeter.
		@param MP_AssetMeter_ID MP_AssetMeter	  */
	public void setMP_AssetMeter_ID (int MP_AssetMeter_ID)
	{
		if (MP_AssetMeter_ID < 1) 
			set_Value (COLUMNNAME_MP_AssetMeter_ID, null);
		else 
			set_Value (COLUMNNAME_MP_AssetMeter_ID, Integer.valueOf(MP_AssetMeter_ID));
	}

	/** Get MP_AssetMeter.
		@return MP_AssetMeter	  */
	public int getMP_AssetMeter_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_AssetMeter_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set MP_AssetMeter_Log.
		@param MP_AssetMeter_Log_ID MP_AssetMeter_Log	  */
	public void setMP_AssetMeter_Log_ID (int MP_AssetMeter_Log_ID)
	{
		if (MP_AssetMeter_Log_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MP_AssetMeter_Log_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MP_AssetMeter_Log_ID, Integer.valueOf(MP_AssetMeter_Log_ID));
	}

	/** Get MP_AssetMeter_Log.
		@return MP_AssetMeter_Log	  */
	public int getMP_AssetMeter_Log_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_AssetMeter_Log_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}