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

/** Generated Model for T_BL_FLASH_CTACTE_RES
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_FLASH_CTACTE_RES extends PO implements I_T_BL_FLASH_CTACTE_RES, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180405L;

    /** Standard Constructor */
    public X_T_BL_FLASH_CTACTE_RES (Properties ctx, int T_BL_FLASH_CTACTE_RES_ID, String trxName)
    {
      super (ctx, T_BL_FLASH_CTACTE_RES_ID, trxName);
      /** if (T_BL_FLASH_CTACTE_RES_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_T_BL_FLASH_CTACTE_RES (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_FLASH_CTACTE_RES[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_ElementValue getAccount() throws RuntimeException
    {
		return (I_C_ElementValue)MTable.get(getCtx(), I_C_ElementValue.Table_Name)
			.getPO(getAccount_ID(), get_TrxName());	}

	/** Set Account.
		@param Account_ID 
		Account used
	  */
	public void setAccount_ID (int Account_ID)
	{
		if (Account_ID < 1) 
			set_Value (COLUMNNAME_Account_ID, null);
		else 
			set_Value (COLUMNNAME_Account_ID, Integer.valueOf(Account_ID));
	}

	/** Get Account.
		@return Account used
	  */
	public int getAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Account_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_T_BL_FLASH_CTACTE getT_BL_FLASH_CTACTE() throws RuntimeException
    {
		return (I_T_BL_FLASH_CTACTE)MTable.get(getCtx(), I_T_BL_FLASH_CTACTE.Table_Name)
			.getPO(getT_BL_FLASH_CTACTE_ID(), get_TrxName());	}

	/** Set T_BL_FLASH_CTACTE.
		@param T_BL_FLASH_CTACTE_ID T_BL_FLASH_CTACTE	  */
	public void setT_BL_FLASH_CTACTE_ID (int T_BL_FLASH_CTACTE_ID)
	{
		if (T_BL_FLASH_CTACTE_ID < 1) 
			set_Value (COLUMNNAME_T_BL_FLASH_CTACTE_ID, null);
		else 
			set_Value (COLUMNNAME_T_BL_FLASH_CTACTE_ID, Integer.valueOf(T_BL_FLASH_CTACTE_ID));
	}

	/** Get T_BL_FLASH_CTACTE.
		@return T_BL_FLASH_CTACTE	  */
	public int getT_BL_FLASH_CTACTE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_FLASH_CTACTE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set T_BL_FLASH_CTACTE_RES.
		@param T_BL_FLASH_CTACTE_RES_ID T_BL_FLASH_CTACTE_RES	  */
	public void setT_BL_FLASH_CTACTE_RES_ID (int T_BL_FLASH_CTACTE_RES_ID)
	{
		if (T_BL_FLASH_CTACTE_RES_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_CTACTE_RES_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_CTACTE_RES_ID, Integer.valueOf(T_BL_FLASH_CTACTE_RES_ID));
	}

	/** Get T_BL_FLASH_CTACTE_RES.
		@return T_BL_FLASH_CTACTE_RES	  */
	public int getT_BL_FLASH_CTACTE_RES_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_FLASH_CTACTE_RES_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TOTAL_DET.
		@param TOTAL_DET TOTAL_DET	  */
	public void setTOTAL_DET (BigDecimal TOTAL_DET)
	{
		set_Value (COLUMNNAME_TOTAL_DET, TOTAL_DET);
	}

	/** Get TOTAL_DET.
		@return TOTAL_DET	  */
	public BigDecimal getTOTAL_DET () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TOTAL_DET);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}