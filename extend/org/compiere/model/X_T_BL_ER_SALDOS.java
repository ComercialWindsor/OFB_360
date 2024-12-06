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

/** Generated Model for T_BL_ER_SALDOS
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_ER_SALDOS extends PO implements I_T_BL_ER_SALDOS, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180830L;

    /** Standard Constructor */
    public X_T_BL_ER_SALDOS (Properties ctx, int T_BL_ER_SALDOS_ID, String trxName)
    {
      super (ctx, T_BL_ER_SALDOS_ID, trxName);
      /** if (T_BL_ER_SALDOS_ID == 0)
        {
			setT_BL_ER_PARAMETROS_ID (0);
			setT_BL_ER_SALDOS_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_ER_SALDOS (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_ER_SALDOS[")
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

	/** AccountType AD_Reference_ID=117 */
	public static final int ACCOUNTTYPE_AD_Reference_ID=117;
	/** Asset = A */
	public static final String ACCOUNTTYPE_Asset = "A";
	/** Liability = L */
	public static final String ACCOUNTTYPE_Liability = "L";
	/** Revenue = R */
	public static final String ACCOUNTTYPE_Revenue = "R";
	/** Expense = E */
	public static final String ACCOUNTTYPE_Expense = "E";
	/** Owner's Equity = O */
	public static final String ACCOUNTTYPE_OwnerSEquity = "O";
	/** Memo = M */
	public static final String ACCOUNTTYPE_Memo = "M";
	/** Set Account Type.
		@param AccountType 
		Indicates the type of account
	  */
	public void setAccountType (String AccountType)
	{

		set_Value (COLUMNNAME_AccountType, AccountType);
	}

	/** Get Account Type.
		@return Indicates the type of account
	  */
	public String getAccountType () 
	{
		return (String)get_Value(COLUMNNAME_AccountType);
	}

	/** Set DEBEHASTA_A.
		@param DEBEHASTA_A DEBEHASTA_A	  */
	public void setDEBEHASTA_A (BigDecimal DEBEHASTA_A)
	{
		set_Value (COLUMNNAME_DEBEHASTA_A, DEBEHASTA_A);
	}

	/** Get DEBEHASTA_A.
		@return DEBEHASTA_A	  */
	public BigDecimal getDEBEHASTA_A () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DEBEHASTA_A);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DEBEHASTA_B.
		@param DEBEHASTA_B DEBEHASTA_B	  */
	public void setDEBEHASTA_B (BigDecimal DEBEHASTA_B)
	{
		set_Value (COLUMNNAME_DEBEHASTA_B, DEBEHASTA_B);
	}

	/** Get DEBEHASTA_B.
		@return DEBEHASTA_B	  */
	public BigDecimal getDEBEHASTA_B () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DEBEHASTA_B);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DESDE_A.
		@param DESDE_A DESDE_A	  */
	public void setDESDE_A (Timestamp DESDE_A)
	{
		set_Value (COLUMNNAME_DESDE_A, DESDE_A);
	}

	/** Get DESDE_A.
		@return DESDE_A	  */
	public Timestamp getDESDE_A () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DESDE_A);
	}

	/** Set DESDE_B.
		@param DESDE_B DESDE_B	  */
	public void setDESDE_B (Timestamp DESDE_B)
	{
		set_Value (COLUMNNAME_DESDE_B, DESDE_B);
	}

	/** Get DESDE_B.
		@return DESDE_B	  */
	public Timestamp getDESDE_B () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DESDE_B);
	}

	/** Set HABERHASTA_A.
		@param HABERHASTA_A HABERHASTA_A	  */
	public void setHABERHASTA_A (BigDecimal HABERHASTA_A)
	{
		set_Value (COLUMNNAME_HABERHASTA_A, HABERHASTA_A);
	}

	/** Get HABERHASTA_A.
		@return HABERHASTA_A	  */
	public BigDecimal getHABERHASTA_A () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_HABERHASTA_A);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HABERHASTA_B.
		@param HABERHASTA_B HABERHASTA_B	  */
	public void setHABERHASTA_B (BigDecimal HABERHASTA_B)
	{
		set_Value (COLUMNNAME_HABERHASTA_B, HABERHASTA_B);
	}

	/** Get HABERHASTA_B.
		@return HABERHASTA_B	  */
	public BigDecimal getHABERHASTA_B () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_HABERHASTA_B);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HASTA_A.
		@param HASTA_A HASTA_A	  */
	public void setHASTA_A (Timestamp HASTA_A)
	{
		set_Value (COLUMNNAME_HASTA_A, HASTA_A);
	}

	/** Get HASTA_A.
		@return HASTA_A	  */
	public Timestamp getHASTA_A () 
	{
		return (Timestamp)get_Value(COLUMNNAME_HASTA_A);
	}

	/** Set HASTA_B.
		@param HASTA_B HASTA_B	  */
	public void setHASTA_B (Timestamp HASTA_B)
	{
		set_Value (COLUMNNAME_HASTA_B, HASTA_B);
	}

	/** Get HASTA_B.
		@return HASTA_B	  */
	public Timestamp getHASTA_B () 
	{
		return (Timestamp)get_Value(COLUMNNAME_HASTA_B);
	}

	/** Set INI_A.
		@param INI_A INI_A	  */
	public void setINI_A (Timestamp INI_A)
	{
		set_Value (COLUMNNAME_INI_A, INI_A);
	}

	/** Get INI_A.
		@return INI_A	  */
	public Timestamp getINI_A () 
	{
		return (Timestamp)get_Value(COLUMNNAME_INI_A);
	}

	/** Set INI_B.
		@param INI_B INI_B	  */
	public void setINI_B (Timestamp INI_B)
	{
		set_Value (COLUMNNAME_INI_B, INI_B);
	}

	/** Get INI_B.
		@return INI_B	  */
	public Timestamp getINI_B () 
	{
		return (Timestamp)get_Value(COLUMNNAME_INI_B);
	}

	/** Set LOG_CONTROL.
		@param LOG_CONTROL LOG_CONTROL	  */
	public void setLOG_CONTROL (String LOG_CONTROL)
	{
		set_Value (COLUMNNAME_LOG_CONTROL, LOG_CONTROL);
	}

	/** Get LOG_CONTROL.
		@return LOG_CONTROL	  */
	public String getLOG_CONTROL () 
	{
		return (String)get_Value(COLUMNNAME_LOG_CONTROL);
	}

	/** Set SALDO_A.
		@param SALDO_A SALDO_A	  */
	public void setSALDO_A (BigDecimal SALDO_A)
	{
		set_Value (COLUMNNAME_SALDO_A, SALDO_A);
	}

	/** Get SALDO_A.
		@return SALDO_A	  */
	public BigDecimal getSALDO_A () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDO_A);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SALDO_B.
		@param SALDO_B SALDO_B	  */
	public void setSALDO_B (BigDecimal SALDO_B)
	{
		set_Value (COLUMNNAME_SALDO_B, SALDO_B);
	}

	/** Get SALDO_B.
		@return SALDO_B	  */
	public BigDecimal getSALDO_B () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDO_B);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SALDOHASTA_A.
		@param SALDOHASTA_A SALDOHASTA_A	  */
	public void setSALDOHASTA_A (BigDecimal SALDOHASTA_A)
	{
		set_Value (COLUMNNAME_SALDOHASTA_A, SALDOHASTA_A);
	}

	/** Get SALDOHASTA_A.
		@return SALDOHASTA_A	  */
	public BigDecimal getSALDOHASTA_A () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDOHASTA_A);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SALDOHASTA_B.
		@param SALDOHASTA_B SALDOHASTA_B	  */
	public void setSALDOHASTA_B (BigDecimal SALDOHASTA_B)
	{
		set_Value (COLUMNNAME_SALDOHASTA_B, SALDOHASTA_B);
	}

	/** Get SALDOHASTA_B.
		@return SALDOHASTA_B	  */
	public BigDecimal getSALDOHASTA_B () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDOHASTA_B);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_T_BL_ER_PARAMETROS getT_BL_ER_PARAMETROS() throws RuntimeException
    {
		return (I_T_BL_ER_PARAMETROS)MTable.get(getCtx(), I_T_BL_ER_PARAMETROS.Table_Name)
			.getPO(getT_BL_ER_PARAMETROS_ID(), get_TrxName());	}

	/** Set T_BL_ER_PARAMETROS.
		@param T_BL_ER_PARAMETROS_ID T_BL_ER_PARAMETROS	  */
	public void setT_BL_ER_PARAMETROS_ID (int T_BL_ER_PARAMETROS_ID)
	{
		if (T_BL_ER_PARAMETROS_ID < 1) 
			set_Value (COLUMNNAME_T_BL_ER_PARAMETROS_ID, null);
		else 
			set_Value (COLUMNNAME_T_BL_ER_PARAMETROS_ID, Integer.valueOf(T_BL_ER_PARAMETROS_ID));
	}

	/** Get T_BL_ER_PARAMETROS.
		@return T_BL_ER_PARAMETROS	  */
	public int getT_BL_ER_PARAMETROS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_ER_PARAMETROS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set T_BL_ER_SALDOS.
		@param T_BL_ER_SALDOS_ID T_BL_ER_SALDOS	  */
	public void setT_BL_ER_SALDOS_ID (int T_BL_ER_SALDOS_ID)
	{
		if (T_BL_ER_SALDOS_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_ER_SALDOS_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_ER_SALDOS_ID, Integer.valueOf(T_BL_ER_SALDOS_ID));
	}

	/** Get T_BL_ER_SALDOS.
		@return T_BL_ER_SALDOS	  */
	public int getT_BL_ER_SALDOS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_ER_SALDOS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}