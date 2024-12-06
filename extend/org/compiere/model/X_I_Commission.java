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

/** Generated Model for I_Commission
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_Commission extends PO implements I_I_Commission, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20161201L;

    /** Standard Constructor */
    public X_I_Commission (Properties ctx, int I_Commission_ID, String trxName)
    {
      super (ctx, I_Commission_ID, trxName);
      /** if (I_Commission_ID == 0)
        {
			setI_Commission_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_Commission (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_Commission[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

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

	public I_C_Period getC_Period() throws RuntimeException
    {
		return (I_C_Period)MTable.get(getCtx(), I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Commission Amount.
		@param CommissionAmt 
		Commission Amount
	  */
	public void setCommissionAmt (BigDecimal CommissionAmt)
	{
		set_Value (COLUMNNAME_CommissionAmt, CommissionAmt);
	}

	/** Get Commission Amount.
		@return Commission Amount
	  */
	public BigDecimal getCommissionAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CommissionAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set I_Commission.
		@param I_Commission_ID I_Commission	  */
	public void setI_Commission_ID (int I_Commission_ID)
	{
		if (I_Commission_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_Commission_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_Commission_ID, Integer.valueOf(I_Commission_ID));
	}

	/** Get I_Commission.
		@return I_Commission	  */
	public int getI_Commission_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_Commission_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_TP_CommissionTSM getTP_CommissionTSM() throws RuntimeException
    {
		return (I_TP_CommissionTSM)MTable.get(getCtx(), I_TP_CommissionTSM.Table_Name)
			.getPO(getTP_CommissionTSM_ID(), get_TrxName());	}

	/** Set TP_CommissionTSM_ID.
		@param TP_CommissionTSM_ID TP_CommissionTSM_ID	  */
	public void setTP_CommissionTSM_ID (int TP_CommissionTSM_ID)
	{
		if (TP_CommissionTSM_ID < 1) 
			set_Value (COLUMNNAME_TP_CommissionTSM_ID, null);
		else 
			set_Value (COLUMNNAME_TP_CommissionTSM_ID, Integer.valueOf(TP_CommissionTSM_ID));
	}

	/** Get TP_CommissionTSM_ID.
		@return TP_CommissionTSM_ID	  */
	public int getTP_CommissionTSM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_CommissionTSM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}