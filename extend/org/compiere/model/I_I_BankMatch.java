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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for I_BankMatch
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_I_BankMatch 
{

    /** TableName=I_BankMatch */
    public static final String Table_Name = "I_BankMatch";

    /** AD_Table_ID=1000052 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name Amt */
    public static final String COLUMNNAME_Amt = "Amt";

	/** Set Amount.
	  * Amount
	  */
	public void setAmt (BigDecimal Amt);

	/** Get Amount.
	  * Amount
	  */
	public BigDecimal getAmt();

    /** Column name BankAccountNo */
    public static final String COLUMNNAME_BankAccountNo = "BankAccountNo";

	/** Set Bank Account No.
	  * Bank Account Number
	  */
	public void setBankAccountNo (String BankAccountNo);

	/** Get Bank Account No.
	  * Bank Account Number
	  */
	public String getBankAccountNo();

    /** Column name C_BankAccount_ID */
    public static final String COLUMNNAME_C_BankAccount_ID = "C_BankAccount_ID";

	/** Set Bank Account.
	  * Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID);

	/** Get Bank Account.
	  * Account at the Bank
	  */
	public int getC_BankAccount_ID();

	public I_C_BankAccount getC_BankAccount() throws RuntimeException;

    /** Column name CodSucursal */
    public static final String COLUMNNAME_CodSucursal = "CodSucursal";

	/** Set CodSucursal	  */
	public void setCodSucursal (String CodSucursal);

	/** Get CodSucursal	  */
	public String getCodSucursal();

    /** Column name CodTransaction */
    public static final String COLUMNNAME_CodTransaction = "CodTransaction";

	/** Set CodTransaction	  */
	public void setCodTransaction (String CodTransaction);

	/** Get CodTransaction	  */
	public String getCodTransaction();

    /** Column name C_Payment_ID */
    public static final String COLUMNNAME_C_Payment_ID = "C_Payment_ID";

	/** Set Payment.
	  * Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID);

	/** Get Payment.
	  * Payment identifier
	  */
	public int getC_Payment_ID();

	public I_C_Payment getC_Payment() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name DocumentDate */
    public static final String COLUMNNAME_DocumentDate = "DocumentDate";

	/** Set DocumentDate	  */
	public void setDocumentDate (Timestamp DocumentDate);

	/** Get DocumentDate	  */
	public Timestamp getDocumentDate();

    /** Column name Folio */
    public static final String COLUMNNAME_Folio = "Folio";

	/** Set Folio	  */
	public void setFolio (BigDecimal Folio);

	/** Get Folio	  */
	public BigDecimal getFolio();

    /** Column name I_BankMatch_ID */
    public static final String COLUMNNAME_I_BankMatch_ID = "I_BankMatch_ID";

	/** Set I_BankMatch	  */
	public void setI_BankMatch_ID (int I_BankMatch_ID);

	/** Get I_BankMatch	  */
	public int getI_BankMatch_ID();

    /** Column name I_IsImported */
    public static final String COLUMNNAME_I_IsImported = "I_IsImported";

	/** Set Imported.
	  * Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported);

	/** Get Imported.
	  * Has this import been processed
	  */
	public boolean isI_IsImported();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsMatched */
    public static final String COLUMNNAME_IsMatched = "IsMatched";

	/** Set IsMatched	  */
	public void setIsMatched (boolean IsMatched);

	/** Get IsMatched	  */
	public boolean isMatched();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name SerialNo */
    public static final String COLUMNNAME_SerialNo = "SerialNo";

	/** Set SerialNo	  */
	public void setSerialNo (String SerialNo);

	/** Get SerialNo	  */
	public String getSerialNo();

    /** Column name Signo */
    public static final String COLUMNNAME_Signo = "Signo";

	/** Set Signo	  */
	public void setSigno (String Signo);

	/** Get Signo	  */
	public String getSigno();

    /** Column name StatementDate */
    public static final String COLUMNNAME_StatementDate = "StatementDate";

	/** Set Statement date.
	  * Date of the statement
	  */
	public void setStatementDate (Timestamp StatementDate);

	/** Get Statement date.
	  * Date of the statement
	  */
	public Timestamp getStatementDate();

    /** Column name Transaction_Type */
    public static final String COLUMNNAME_Transaction_Type = "Transaction_Type";

	/** Set Transaction_Type	  */
	public void setTransaction_Type (String Transaction_Type);

	/** Get Transaction_Type	  */
	public String getTransaction_Type();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
