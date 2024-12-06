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

/** Generated Model for DM_PerformanceBondDet_Proc
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_DM_PerformanceBondDet_Proc extends PO implements I_DM_PerformanceBondDet_Proc, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150112L;

    /** Standard Constructor */
    public X_DM_PerformanceBondDet_Proc (Properties ctx, int DM_PerformanceBondDet_Proc_ID, String trxName)
    {
      super (ctx, DM_PerformanceBondDet_Proc_ID, trxName);
      /** if (DM_PerformanceBondDet_Proc_ID == 0)
        {
			setAmt (Env.ZERO);
			setDM_PerformanceBond_ID (0);
			setDM_PerformanceBond_Proc_ID (0);
			setDM_PerformanceBondDet_Proc_ID (0);
        } */
    }

    /** Load Constructor */
    public X_DM_PerformanceBondDet_Proc (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_DM_PerformanceBondDet_Proc[")
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
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
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

	public I_DM_PerformanceBond getDM_PerformanceBond() throws RuntimeException
    {
		return (I_DM_PerformanceBond)MTable.get(getCtx(), I_DM_PerformanceBond.Table_Name)
			.getPO(getDM_PerformanceBond_ID(), get_TrxName());	}

	/** Set DM_PerformanceBond_ID.
		@param DM_PerformanceBond_ID DM_PerformanceBond_ID	  */
	public void setDM_PerformanceBond_ID (int DM_PerformanceBond_ID)
	{
		if (DM_PerformanceBond_ID < 1) 
			set_Value (COLUMNNAME_DM_PerformanceBond_ID, null);
		else 
			set_Value (COLUMNNAME_DM_PerformanceBond_ID, Integer.valueOf(DM_PerformanceBond_ID));
	}

	/** Get DM_PerformanceBond_ID.
		@return DM_PerformanceBond_ID	  */
	public int getDM_PerformanceBond_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_PerformanceBond_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_DM_PerformanceBond_Proc getDM_PerformanceBond_Proc() throws RuntimeException
    {
		return (I_DM_PerformanceBond_Proc)MTable.get(getCtx(), I_DM_PerformanceBond_Proc.Table_Name)
			.getPO(getDM_PerformanceBond_Proc_ID(), get_TrxName());	}

	/** Set DM_PerformanceBond_Proc_ID.
		@param DM_PerformanceBond_Proc_ID DM_PerformanceBond_Proc_ID	  */
	public void setDM_PerformanceBond_Proc_ID (int DM_PerformanceBond_Proc_ID)
	{
		if (DM_PerformanceBond_Proc_ID < 1) 
			set_Value (COLUMNNAME_DM_PerformanceBond_Proc_ID, null);
		else 
			set_Value (COLUMNNAME_DM_PerformanceBond_Proc_ID, Integer.valueOf(DM_PerformanceBond_Proc_ID));
	}

	/** Get DM_PerformanceBond_Proc_ID.
		@return DM_PerformanceBond_Proc_ID	  */
	public int getDM_PerformanceBond_Proc_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_PerformanceBond_Proc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DM_PerformanceBondDet_Proc.
		@param DM_PerformanceBondDet_Proc_ID DM_PerformanceBondDet_Proc	  */
	public void setDM_PerformanceBondDet_Proc_ID (int DM_PerformanceBondDet_Proc_ID)
	{
		if (DM_PerformanceBondDet_Proc_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DM_PerformanceBondDet_Proc_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DM_PerformanceBondDet_Proc_ID, Integer.valueOf(DM_PerformanceBondDet_Proc_ID));
	}

	/** Get DM_PerformanceBondDet_Proc.
		@return DM_PerformanceBondDet_Proc	  */
	public int getDM_PerformanceBondDet_Proc_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_PerformanceBondDet_Proc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}