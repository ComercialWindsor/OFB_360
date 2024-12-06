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

/** Generated Model for I_TempPrototipo
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_TempPrototipo extends PO implements I_I_TempPrototipo, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150713L;

    /** Standard Constructor */
    public X_I_TempPrototipo (Properties ctx, int I_TempPrototipo_ID, String trxName)
    {
      super (ctx, I_TempPrototipo_ID, trxName);
      /** if (I_TempPrototipo_ID == 0)
        {
			setI_TempPrototipo_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_TempPrototipo (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_TempPrototipo[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set I_TempPrototipo.
		@param I_TempPrototipo_ID I_TempPrototipo	  */
	public void setI_TempPrototipo_ID (int I_TempPrototipo_ID)
	{
		if (I_TempPrototipo_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_TempPrototipo_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_TempPrototipo_ID, Integer.valueOf(I_TempPrototipo_ID));
	}

	/** Get I_TempPrototipo.
		@return I_TempPrototipo	  */
	public int getI_TempPrototipo_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_TempPrototipo_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}