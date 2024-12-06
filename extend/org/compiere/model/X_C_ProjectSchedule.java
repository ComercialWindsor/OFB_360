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

/** Generated Model for C_ProjectSchedule
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_C_ProjectSchedule extends PO implements I_C_ProjectSchedule, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120605L;

    /** Standard Constructor */
    public X_C_ProjectSchedule (Properties ctx, int C_ProjectSchedule_ID, String trxName)
    {
      super (ctx, C_ProjectSchedule_ID, trxName);
      /** if (C_ProjectSchedule_ID == 0)
        {
			setC_Project_ID (0);
			setC_ProjectSchedule_ID (0);
			setDueAmt (Env.ZERO);
			setduebalance (Env.ZERO);
			setDueDate (new Timestamp( System.currentTimeMillis() ));
			setIsValid (false);
        } */
    }

    /** Load Constructor */
    public X_C_ProjectSchedule (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_ProjectSchedule[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_Project getC_Project() throws RuntimeException
    {
		return (I_C_Project)MTable.get(getCtx(), I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_Value (COLUMNNAME_C_Project_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_ProjectSchedule.
		@param C_ProjectSchedule_ID C_ProjectSchedule	  */
	public void setC_ProjectSchedule_ID (int C_ProjectSchedule_ID)
	{
		if (C_ProjectSchedule_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_ProjectSchedule_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_ProjectSchedule_ID, Integer.valueOf(C_ProjectSchedule_ID));
	}

	/** Get C_ProjectSchedule.
		@return C_ProjectSchedule	  */
	public int getC_ProjectSchedule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectSchedule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Amount due.
		@param DueAmt 
		Amount of the payment due
	  */
	public void setDueAmt (BigDecimal DueAmt)
	{
		set_Value (COLUMNNAME_DueAmt, DueAmt);
	}

	/** Get Amount due.
		@return Amount of the payment due
	  */
	public BigDecimal getDueAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DueAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set duebalance.
		@param duebalance duebalance	  */
	public void setduebalance (BigDecimal duebalance)
	{
		set_Value (COLUMNNAME_duebalance, duebalance);
	}

	/** Get duebalance.
		@return duebalance	  */
	public BigDecimal getduebalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_duebalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Due Date.
		@param DueDate 
		Date when the payment is due
	  */
	public void setDueDate (Timestamp DueDate)
	{
		set_Value (COLUMNNAME_DueDate, DueDate);
	}

	/** Get Due Date.
		@return Date when the payment is due
	  */
	public Timestamp getDueDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DueDate);
	}

	/** Set Valid.
		@param IsValid 
		Element is valid
	  */
	public void setIsValid (boolean IsValid)
	{
		set_Value (COLUMNNAME_IsValid, Boolean.valueOf(IsValid));
	}

	/** Get Valid.
		@return Element is valid
	  */
	public boolean isValid () 
	{
		Object oo = get_Value(COLUMNNAME_IsValid);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}