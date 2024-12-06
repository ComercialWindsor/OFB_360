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

/** Generated Model for C_BankAccountBalance
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_C_BankAccountBalance extends PO implements I_C_BankAccountBalance, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151118L;

    /** Standard Constructor */
    public X_C_BankAccountBalance (Properties ctx, int C_BankAccountBalance_ID, String trxName)
    {
      super (ctx, C_BankAccountBalance_ID, trxName);
      /** if (C_BankAccountBalance_ID == 0)
        {
			setC_BankAccountBalance_ID (0);
			setDateDoc (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_C_BankAccountBalance (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_BankAccountBalance[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AccountingBalance.
		@param AccountingBalance AccountingBalance	  */
	public void setAccountingBalance (BigDecimal AccountingBalance)
	{
		set_Value (COLUMNNAME_AccountingBalance, AccountingBalance);
	}

	/** Get AccountingBalance.
		@return AccountingBalance	  */
	public BigDecimal getAccountingBalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AccountingBalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set AvailableBalance.
		@param AvailableBalance AvailableBalance	  */
	public void setAvailableBalance (BigDecimal AvailableBalance)
	{
		set_Value (COLUMNNAME_AvailableBalance, AvailableBalance);
	}

	/** Get AvailableBalance.
		@return AvailableBalance	  */
	public BigDecimal getAvailableBalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AvailableBalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_BankAccount getC_BankAccount() throws RuntimeException
    {
		return (I_C_BankAccount)MTable.get(getCtx(), I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccount_ID(), get_TrxName());	}

	/** Set Bank Account.
		@param C_BankAccount_ID 
		Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BankAccount_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BankAccount_ID, Integer.valueOf(C_BankAccount_ID));
	}

	/** Get Bank Account.
		@return Account at the Bank
	  */
	public int getC_BankAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccount_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_BankAccountBalance.
		@param C_BankAccountBalance_ID C_BankAccountBalance	  */
	public void setC_BankAccountBalance_ID (int C_BankAccountBalance_ID)
	{
		if (C_BankAccountBalance_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BankAccountBalance_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BankAccountBalance_ID, Integer.valueOf(C_BankAccountBalance_ID));
	}

	/** Get C_BankAccountBalance.
		@return C_BankAccountBalance	  */
	public int getC_BankAccountBalance_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccountBalance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
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

	/** Set Retention.
		@param Retention Retention	  */
	public void setRetention (BigDecimal Retention)
	{
		set_Value (COLUMNNAME_Retention, Retention);
	}

	/** Get Retention.
		@return Retention	  */
	public BigDecimal getRetention () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Retention);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Retention1.
		@param Retention1 Retention1	  */
	public void setRetention1 (BigDecimal Retention1)
	{
		set_Value (COLUMNNAME_Retention1, Retention1);
	}

	/** Get Retention1.
		@return Retention1	  */
	public BigDecimal getRetention1 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Retention1);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Retention2.
		@param Retention2 Retention2	  */
	public void setRetention2 (BigDecimal Retention2)
	{
		set_Value (COLUMNNAME_Retention2, Retention2);
	}

	/** Get Retention2.
		@return Retention2	  */
	public BigDecimal getRetention2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Retention2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TotalBalance.
		@param TotalBalance TotalBalance	  */
	public void setTotalBalance (BigDecimal TotalBalance)
	{
		set_Value (COLUMNNAME_TotalBalance, TotalBalance);
	}

	/** Get TotalBalance.
		@return TotalBalance	  */
	public BigDecimal getTotalBalance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalBalance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}