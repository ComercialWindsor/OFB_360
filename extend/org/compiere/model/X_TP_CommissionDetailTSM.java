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

/** Generated Model for TP_CommissionDetailTSM
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_TP_CommissionDetailTSM extends PO implements I_TP_CommissionDetailTSM, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20161201L;

    /** Standard Constructor */
    public X_TP_CommissionDetailTSM (Properties ctx, int TP_CommissionDetailTSM_ID, String trxName)
    {
      super (ctx, TP_CommissionDetailTSM_ID, trxName);
      /** if (TP_CommissionDetailTSM_ID == 0)
        {
			setTP_CommissionDetailTSM_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TP_CommissionDetailTSM (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TP_CommissionDetailTSM[")
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

	/** Set TP_CommissionDetailTSM_ID.
		@param TP_CommissionDetailTSM_ID TP_CommissionDetailTSM_ID	  */
	public void setTP_CommissionDetailTSM_ID (int TP_CommissionDetailTSM_ID)
	{
		if (TP_CommissionDetailTSM_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TP_CommissionDetailTSM_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TP_CommissionDetailTSM_ID, Integer.valueOf(TP_CommissionDetailTSM_ID));
	}

	/** Get TP_CommissionDetailTSM_ID.
		@return TP_CommissionDetailTSM_ID	  */
	public int getTP_CommissionDetailTSM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_CommissionDetailTSM_ID);
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
			set_ValueNoCheck (COLUMNNAME_TP_CommissionTSM_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TP_CommissionTSM_ID, Integer.valueOf(TP_CommissionTSM_ID));
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