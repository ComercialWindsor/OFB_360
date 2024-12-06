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
import org.compiere.util.KeyNamePair;

/** Generated Interface for C_BankAccountBalance
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_C_BankAccountBalance 
{

    /** TableName=C_BankAccountBalance */
    public static final String Table_Name = "C_BankAccountBalance";

    /** AD_Table_ID=1000110 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AccountingBalance */
    public static final String COLUMNNAME_AccountingBalance = "AccountingBalance";

	/** Set AccountingBalance	  */
	public void setAccountingBalance (BigDecimal AccountingBalance);

	/** Get AccountingBalance	  */
	public BigDecimal getAccountingBalance();

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

    /** Column name AvailableBalance */
    public static final String COLUMNNAME_AvailableBalance = "AvailableBalance";

	/** Set AvailableBalance	  */
	public void setAvailableBalance (BigDecimal AvailableBalance);

	/** Get AvailableBalance	  */
	public BigDecimal getAvailableBalance();

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

    /** Column name C_BankAccountBalance_ID */
    public static final String COLUMNNAME_C_BankAccountBalance_ID = "C_BankAccountBalance_ID";

	/** Set C_BankAccountBalance	  */
	public void setC_BankAccountBalance_ID (int C_BankAccountBalance_ID);

	/** Get C_BankAccountBalance	  */
	public int getC_BankAccountBalance_ID();

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

    /** Column name DateDoc */
    public static final String COLUMNNAME_DateDoc = "DateDoc";

	/** Set Document Date.
	  * Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc);

	/** Get Document Date.
	  * Date of the Document
	  */
	public Timestamp getDateDoc();

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

    /** Column name Retention */
    public static final String COLUMNNAME_Retention = "Retention";

	/** Set Retention	  */
	public void setRetention (BigDecimal Retention);

	/** Get Retention	  */
	public BigDecimal getRetention();

    /** Column name Retention1 */
    public static final String COLUMNNAME_Retention1 = "Retention1";

	/** Set Retention1	  */
	public void setRetention1 (BigDecimal Retention1);

	/** Get Retention1	  */
	public BigDecimal getRetention1();

    /** Column name Retention2 */
    public static final String COLUMNNAME_Retention2 = "Retention2";

	/** Set Retention2	  */
	public void setRetention2 (BigDecimal Retention2);

	/** Get Retention2	  */
	public BigDecimal getRetention2();

    /** Column name TotalBalance */
    public static final String COLUMNNAME_TotalBalance = "TotalBalance";

	/** Set TotalBalance	  */
	public void setTotalBalance (BigDecimal TotalBalance);

	/** Get TotalBalance	  */
	public BigDecimal getTotalBalance();

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
