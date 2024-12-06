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

/** Generated Interface for DM_PerformanceBond_Proc
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_DM_PerformanceBond_Proc 
{

    /** TableName=DM_PerformanceBond_Proc */
    public static final String Table_Name = "DM_PerformanceBond_Proc";

    /** AD_Table_ID=1000098 */
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

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

	/** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID);

	/** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID();

	public I_AD_User getAD_User() throws RuntimeException;

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

    /** Column name BondCondition */
    public static final String COLUMNNAME_BondCondition = "BondCondition";

	/** Set BondCondition.
	  * Bond Condition
	  */
	public void setBondCondition (String BondCondition);

	/** Get BondCondition.
	  * Bond Condition
	  */
	public String getBondCondition();

    /** Column name BondType */
    public static final String COLUMNNAME_BondType = "BondType";

	/** Set BondType.
	  * Type of Bond
	  */
	public void setBondType (String BondType);

	/** Get BondType.
	  * Type of Bond
	  */
	public String getBondType();

    /** Column name BtnProcess */
    public static final String COLUMNNAME_BtnProcess = "BtnProcess";

	/** Set BtnProcess	  */
	public void setBtnProcess (String BtnProcess);

	/** Get BtnProcess	  */
	public String getBtnProcess();

    /** Column name C_Bank_ID */
    public static final String COLUMNNAME_C_Bank_ID = "C_Bank_ID";

	/** Set Bank.
	  * Bank
	  */
	public void setC_Bank_ID (int C_Bank_ID);

	/** Get Bank.
	  * Bank
	  */
	public int getC_Bank_ID();

	public I_C_Bank getC_Bank() throws RuntimeException;

    /** Column name C_BankRef_ID */
    public static final String COLUMNNAME_C_BankRef_ID = "C_BankRef_ID";

	/** Set C_BankRef_ID	  */
	public void setC_BankRef_ID (int C_BankRef_ID);

	/** Get C_BankRef_ID	  */
	public int getC_BankRef_ID();

	public I_C_Bank getC_BankRef() throws RuntimeException;

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name CollectionReason */
    public static final String COLUMNNAME_CollectionReason = "CollectionReason";

	/** Set CollectionReason	  */
	public void setCollectionReason (String CollectionReason);

	/** Get CollectionReason	  */
	public String getCollectionReason();

    /** Column name correlative */
    public static final String COLUMNNAME_correlative = "correlative";

	/** Set correlative	  */
	public void setcorrelative (int correlative);

	/** Get correlative	  */
	public int getcorrelative();

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

    /** Column name DateAcct */
    public static final String COLUMNNAME_DateAcct = "DateAcct";

	/** Set Account Date.
	  * Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct);

	/** Get Account Date.
	  * Accounting Date
	  */
	public Timestamp getDateAcct();

    /** Column name DateAcctRef */
    public static final String COLUMNNAME_DateAcctRef = "DateAcctRef";

	/** Set DateAcctRef	  */
	public void setDateAcctRef (Timestamp DateAcctRef);

	/** Get DateAcctRef	  */
	public Timestamp getDateAcctRef();

    /** Column name DateCollection */
    public static final String COLUMNNAME_DateCollection = "DateCollection";

	/** Set DateCollection	  */
	public void setDateCollection (Timestamp DateCollection);

	/** Get DateCollection	  */
	public Timestamp getDateCollection();

    /** Column name DateMoneyReception */
    public static final String COLUMNNAME_DateMoneyReception = "DateMoneyReception";

	/** Set DateMoneyReception	  */
	public void setDateMoneyReception (Timestamp DateMoneyReception);

	/** Get DateMoneyReception	  */
	public Timestamp getDateMoneyReception();

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

    /** Column name DateTrxRef */
    public static final String COLUMNNAME_DateTrxRef = "DateTrxRef";

	/** Set DateTrxRef	  */
	public void setDateTrxRef (Timestamp DateTrxRef);

	/** Get DateTrxRef	  */
	public Timestamp getDateTrxRef();

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

    /** Column name DM_PerformanceBond_ID */
    public static final String COLUMNNAME_DM_PerformanceBond_ID = "DM_PerformanceBond_ID";

	/** Set DM_PerformanceBond_ID	  */
	public void setDM_PerformanceBond_ID (int DM_PerformanceBond_ID);

	/** Get DM_PerformanceBond_ID	  */
	public int getDM_PerformanceBond_ID();

	public I_DM_PerformanceBond getDM_PerformanceBond() throws RuntimeException;

    /** Column name DM_PerformanceBond_Proc_ID */
    public static final String COLUMNNAME_DM_PerformanceBond_Proc_ID = "DM_PerformanceBond_Proc_ID";

	/** Set DM_PerformanceBond_Proc	  */
	public void setDM_PerformanceBond_Proc_ID (int DM_PerformanceBond_Proc_ID);

	/** Get DM_PerformanceBond_Proc	  */
	public int getDM_PerformanceBond_Proc_ID();

    /** Column name DM_PerformanceBondRef_ID */
    public static final String COLUMNNAME_DM_PerformanceBondRef_ID = "DM_PerformanceBondRef_ID";

	/** Set DM_PerformanceBondRef_ID	  */
	public void setDM_PerformanceBondRef_ID (int DM_PerformanceBondRef_ID);

	/** Get DM_PerformanceBondRef_ID	  */
	public int getDM_PerformanceBondRef_ID();

	public I_DM_PerformanceBond getDM_PerformanceBondRef() throws RuntimeException;

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name DocumentRefNo */
    public static final String COLUMNNAME_DocumentRefNo = "DocumentRefNo";

	/** Set DocumentRefNo	  */
	public void setDocumentRefNo (String DocumentRefNo);

	/** Get DocumentRefNo	  */
	public String getDocumentRefNo();

    /** Column name Guarantee */
    public static final String COLUMNNAME_Guarantee = "Guarantee";

	/** Set Guarantee.
	  * Type of Guarantee
	  */
	public void setGuarantee (String Guarantee);

	/** Get Guarantee.
	  * Type of Guarantee
	  */
	public String getGuarantee();

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

    /** Column name LowReason */
    public static final String COLUMNNAME_LowReason = "LowReason";

	/** Set LowReason	  */
	public void setLowReason (String LowReason);

	/** Get LowReason	  */
	public String getLowReason();

    /** Column name OfbButton */
    public static final String COLUMNNAME_OfbButton = "OfbButton";

	/** Set OfbButton	  */
	public void setOfbButton (String OfbButton);

	/** Get OfbButton	  */
	public String getOfbButton();

    /** Column name Posted */
    public static final String COLUMNNAME_Posted = "Posted";

	/** Set Posted.
	  * Posting status
	  */
	public void setPosted (boolean Posted);

	/** Get Posted.
	  * Posting status
	  */
	public boolean isPosted();

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

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name Type */
    public static final String COLUMNNAME_Type = "Type";

	/** Set Type.
	  * Type of Validation (SQL, Java Script, Java Language)
	  */
	public void setType (String Type);

	/** Get Type.
	  * Type of Validation (SQL, Java Script, Java Language)
	  */
	public String getType();

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
