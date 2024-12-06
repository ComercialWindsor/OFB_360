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
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for I_BankMatch
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_BankMatch extends PO implements I_I_BankMatch, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20110808L;

    /** Standard Constructor */
    public X_I_BankMatch (Properties ctx, int I_BankMatch_ID, String trxName)
    {
      super (ctx, I_BankMatch_ID, trxName);
      /** if (I_BankMatch_ID == 0)
        {
			setI_BankMatch_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_BankMatch (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_BankMatch[")
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

	/** Set Bank Account No.
		@param BankAccountNo 
		Bank Account Number
	  */
	public void setBankAccountNo (String BankAccountNo)
	{
		set_Value (COLUMNNAME_BankAccountNo, BankAccountNo);
	}

	/** Get Bank Account No.
		@return Bank Account Number
	  */
	public String getBankAccountNo () 
	{
		return (String)get_Value(COLUMNNAME_BankAccountNo);
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
			set_Value (COLUMNNAME_C_BankAccount_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankAccount_ID, Integer.valueOf(C_BankAccount_ID));
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

	/** Set CodSucursal.
		@param CodSucursal CodSucursal	  */
	public void setCodSucursal (String CodSucursal)
	{
		set_Value (COLUMNNAME_CodSucursal, CodSucursal);
	}

	/** Get CodSucursal.
		@return CodSucursal	  */
	public String getCodSucursal () 
	{
		return (String)get_Value(COLUMNNAME_CodSucursal);
	}

	/** Set CodTransaction.
		@param CodTransaction CodTransaction	  */
	public void setCodTransaction (String CodTransaction)
	{
		set_Value (COLUMNNAME_CodTransaction, CodTransaction);
	}

	/** Get CodTransaction.
		@return CodTransaction	  */
	public String getCodTransaction () 
	{
		return (String)get_Value(COLUMNNAME_CodTransaction);
	}

	public I_C_Payment getC_Payment() throws RuntimeException
    {
		return (I_C_Payment)MTable.get(getCtx(), I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
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

	/** Set DocumentDate.
		@param DocumentDate DocumentDate	  */
	public void setDocumentDate (Timestamp DocumentDate)
	{
		set_Value (COLUMNNAME_DocumentDate, DocumentDate);
	}

	/** Get DocumentDate.
		@return DocumentDate	  */
	public Timestamp getDocumentDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DocumentDate);
	}

	/** Set Folio.
		@param Folio Folio	  */
	public void setFolio (BigDecimal Folio)
	{
		set_Value (COLUMNNAME_Folio, Folio);
	}

	/** Get Folio.
		@return Folio	  */
	public BigDecimal getFolio () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Folio);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set I_BankMatch.
		@param I_BankMatch_ID I_BankMatch	  */
	public void setI_BankMatch_ID (int I_BankMatch_ID)
	{
		if (I_BankMatch_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_BankMatch_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_BankMatch_ID, Integer.valueOf(I_BankMatch_ID));
	}

	/** Get I_BankMatch.
		@return I_BankMatch	  */
	public int getI_BankMatch_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_BankMatch_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Imported.
		@param I_IsImported 
		Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported)
	{
		set_Value (COLUMNNAME_I_IsImported, Boolean.valueOf(I_IsImported));
	}

	/** Get Imported.
		@return Has this import been processed
	  */
	public boolean isI_IsImported () 
	{
		Object oo = get_Value(COLUMNNAME_I_IsImported);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsMatched.
		@param IsMatched IsMatched	  */
	public void setIsMatched (boolean IsMatched)
	{
		set_Value (COLUMNNAME_IsMatched, Boolean.valueOf(IsMatched));
	}

	/** Get IsMatched.
		@return IsMatched	  */
	public boolean isMatched () 
	{
		Object oo = get_Value(COLUMNNAME_IsMatched);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set SerialNo.
		@param SerialNo SerialNo	  */
	public void setSerialNo (String SerialNo)
	{
		set_Value (COLUMNNAME_SerialNo, SerialNo);
	}

	/** Get SerialNo.
		@return SerialNo	  */
	public String getSerialNo () 
	{
		return (String)get_Value(COLUMNNAME_SerialNo);
	}

	/** Set Signo.
		@param Signo Signo	  */
	public void setSigno (String Signo)
	{
		set_Value (COLUMNNAME_Signo, Signo);
	}

	/** Get Signo.
		@return Signo	  */
	public String getSigno () 
	{
		return (String)get_Value(COLUMNNAME_Signo);
	}

	/** Set Statement date.
		@param StatementDate 
		Date of the statement
	  */
	public void setStatementDate (Timestamp StatementDate)
	{
		set_Value (COLUMNNAME_StatementDate, StatementDate);
	}

	/** Get Statement date.
		@return Date of the statement
	  */
	public Timestamp getStatementDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StatementDate);
	}

	/** Set Transaction_Type.
		@param Transaction_Type Transaction_Type	  */
	public void setTransaction_Type (String Transaction_Type)
	{
		set_Value (COLUMNNAME_Transaction_Type, Transaction_Type);
	}

	/** Get Transaction_Type.
		@return Transaction_Type	  */
	public String getTransaction_Type () 
	{
		return (String)get_Value(COLUMNNAME_Transaction_Type);
	}
}