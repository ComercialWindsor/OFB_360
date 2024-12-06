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

/** Generated Interface for OFB_Ssomac
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_OFB_Ssomac 
{

    /** TableName=OFB_Ssomac */
    public static final String Table_Name = "OFB_Ssomac";

    /** AD_Table_ID=1000118 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name A_Asset_ID */
    public static final String COLUMNNAME_A_Asset_ID = "A_Asset_ID";

	/** Set Asset.
	  * Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID);

	/** Get Asset.
	  * Asset used internally or by customers
	  */
	public int getA_Asset_ID();

	public I_A_Asset getA_Asset() throws RuntimeException;

    /** Column name A_Asset2_ID */
    public static final String COLUMNNAME_A_Asset2_ID = "A_Asset2_ID";

	/** Set A_Asset2_ID	  */
	public void setA_Asset2_ID (int A_Asset2_ID);

	/** Get A_Asset2_ID	  */
	public int getA_Asset2_ID();

	public I_A_Asset getA_Asset2() throws RuntimeException;

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

    /** Column name AD_Org_Ref_ID */
    public static final String COLUMNNAME_AD_Org_Ref_ID = "AD_Org_Ref_ID";

	/** Set AD_Org_Ref_ID	  */
	public void setAD_Org_Ref_ID (int AD_Org_Ref_ID);

	/** Get AD_Org_Ref_ID	  */
	public int getAD_Org_Ref_ID();

    /** Column name AD_Org_Ref2_ID */
    public static final String COLUMNNAME_AD_Org_Ref2_ID = "AD_Org_Ref2_ID";

	/** Set AD_Org_Ref2_ID	  */
	public void setAD_Org_Ref2_ID (int AD_Org_Ref2_ID);

	/** Get AD_Org_Ref2_ID	  */
	public int getAD_Org_Ref2_ID();

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

    /** Column name AD_User2_ID */
    public static final String COLUMNNAME_AD_User2_ID = "AD_User2_ID";

	/** Set AD_User2_ID	  */
	public void setAD_User2_ID (int AD_User2_ID);

	/** Get AD_User2_ID	  */
	public int getAD_User2_ID();

	public I_AD_User getAD_User2() throws RuntimeException;

    /** Column name Admonition */
    public static final String COLUMNNAME_Admonition = "Admonition";

	/** Set Admonition	  */
	public void setAdmonition (boolean Admonition);

	/** Get Admonition	  */
	public boolean isAdmonition();

    /** Column name Admonition2 */
    public static final String COLUMNNAME_Admonition2 = "Admonition2";

	/** Set Admonition2	  */
	public void setAdmonition2 (boolean Admonition2);

	/** Get Admonition2	  */
	public boolean isAdmonition2();

    /** Column name AlertMessage */
    public static final String COLUMNNAME_AlertMessage = "AlertMessage";

	/** Set Alert Message.
	  * Message of the Alert
	  */
	public void setAlertMessage (String AlertMessage);

	/** Get Alert Message.
	  * Message of the Alert
	  */
	public String getAlertMessage();

    /** Column name Assistants */
    public static final String COLUMNNAME_Assistants = "Assistants";

	/** Set Assistants	  */
	public void setAssistants (String Assistants);

	/** Get Assistants	  */
	public String getAssistants();

    /** Column name Boton1 */
    public static final String COLUMNNAME_Boton1 = "Boton1";

	/** Set Boton1	  */
	public void setBoton1 (String Boton1);

	/** Get Boton1	  */
	public String getBoton1();

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

    /** Column name C_Period_ID */
    public static final String COLUMNNAME_C_Period_ID = "C_Period_ID";

	/** Set Period.
	  * Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID);

	/** Get Period.
	  * Period of the Calendar
	  */
	public int getC_Period_ID();

	public I_C_Period getC_Period() throws RuntimeException;

    /** Column name C_Project_ID */
    public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";

	/** Set Project.
	  * Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID);

	/** Get Project.
	  * Financial Project
	  */
	public int getC_Project_ID();

	public I_C_ProjectOFB getC_Project() throws RuntimeException;

    /** Column name C_Project2_ID */
    public static final String COLUMNNAME_C_Project2_ID = "C_Project2_ID";

	/** Set C_Project2_ID	  */
	public void setC_Project2_ID (int C_Project2_ID);

	/** Get C_Project2_ID	  */
	public int getC_Project2_ID();

	public I_C_ProjectOFB getC_Project2() throws RuntimeException;

    /** Column name C_Year_ID */
    public static final String COLUMNNAME_C_Year_ID = "C_Year_ID";

	/** Set Year.
	  * Calendar Year
	  */
	public void setC_Year_ID (int C_Year_ID);

	/** Get Year.
	  * Calendar Year
	  */
	public int getC_Year_ID();

	public I_C_Year getC_Year() throws RuntimeException;

    /** Column name Cause */
    public static final String COLUMNNAME_Cause = "Cause";

	/** Set Cause	  */
	public void setCause (String Cause);

	/** Get Cause	  */
	public String getCause();

    /** Column name Cause2 */
    public static final String COLUMNNAME_Cause2 = "Cause2";

	/** Set Cause2	  */
	public void setCause2 (String Cause2);

	/** Get Cause2	  */
	public String getCause2();

    /** Column name ChargeAmt */
    public static final String COLUMNNAME_ChargeAmt = "ChargeAmt";

	/** Set Charge amount.
	  * Charge Amount
	  */
	public void setChargeAmt (BigDecimal ChargeAmt);

	/** Get Charge amount.
	  * Charge Amount
	  */
	public BigDecimal getChargeAmt();

    /** Column name Classification */
    public static final String COLUMNNAME_Classification = "Classification";

	/** Set Classification.
	  * Classification for grouping
	  */
	public void setClassification (String Classification);

	/** Get Classification.
	  * Classification for grouping
	  */
	public String getClassification();

    /** Column name CommitmentType */
    public static final String COLUMNNAME_CommitmentType = "CommitmentType";

	/** Set Commitment Type.
	  * Create Commitment and/or Reservations for Budget Control
	  */
	public void setCommitmentType (String CommitmentType);

	/** Get Commitment Type.
	  * Create Commitment and/or Reservations for Budget Control
	  */
	public String getCommitmentType();

    /** Column name CommitteeDate */
    public static final String COLUMNNAME_CommitteeDate = "CommitteeDate";

	/** Set CommitteeDate	  */
	public void setCommitteeDate (Timestamp CommitteeDate);

	/** Get CommitteeDate	  */
	public Timestamp getCommitteeDate();

    /** Column name ConstancyDate */
    public static final String COLUMNNAME_ConstancyDate = "ConstancyDate";

	/** Set ConstancyDate	  */
	public void setConstancyDate (Timestamp ConstancyDate);

	/** Get ConstancyDate	  */
	public Timestamp getConstancyDate();

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

    /** Column name Date1 */
    public static final String COLUMNNAME_Date1 = "Date1";

	/** Set Date.
	  * Date when business is not conducted
	  */
	public void setDate1 (Timestamp Date1);

	/** Get Date.
	  * Date when business is not conducted
	  */
	public Timestamp getDate1();

    /** Column name DateReport */
    public static final String COLUMNNAME_DateReport = "DateReport";

	/** Set Report Date.
	  * Expense/Time Report Date
	  */
	public void setDateReport (Timestamp DateReport);

	/** Get Report Date.
	  * Expense/Time Report Date
	  */
	public Timestamp getDateReport();

    /** Column name DateRequired */
    public static final String COLUMNNAME_DateRequired = "DateRequired";

	/** Set Date Required.
	  * Date when required
	  */
	public void setDateRequired (Timestamp DateRequired);

	/** Get Date Required.
	  * Date when required
	  */
	public Timestamp getDateRequired();

    /** Column name DeliveryViaRule */
    public static final String COLUMNNAME_DeliveryViaRule = "DeliveryViaRule";

	/** Set Delivery Via.
	  * How the order will be delivered
	  */
	public void setDeliveryViaRule (String DeliveryViaRule);

	/** Get Delivery Via.
	  * How the order will be delivered
	  */
	public String getDeliveryViaRule();

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

    /** Column name DetailInfo */
    public static final String COLUMNNAME_DetailInfo = "DetailInfo";

	/** Set Detail Information.
	  * Additional Detail Information
	  */
	public void setDetailInfo (String DetailInfo);

	/** Get Detail Information.
	  * Additional Detail Information
	  */
	public String getDetailInfo();

    /** Column name Discount */
    public static final String COLUMNNAME_Discount = "Discount";

	/** Set Discount %.
	  * Discount in percent
	  */
	public void setDiscount (boolean Discount);

	/** Get Discount %.
	  * Discount in percent
	  */
	public boolean isDiscount();

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

    /** Column name DriverStatus */
    public static final String COLUMNNAME_DriverStatus = "DriverStatus";

	/** Set DriverStatus	  */
	public void setDriverStatus (String DriverStatus);

	/** Get DriverStatus	  */
	public String getDriverStatus();

    /** Column name EMailVerifyDate */
    public static final String COLUMNNAME_EMailVerifyDate = "EMailVerifyDate";

	/** Set EMail Verify.
	  * Date Email was verified
	  */
	public void setEMailVerifyDate (Timestamp EMailVerifyDate);

	/** Get EMail Verify.
	  * Date Email was verified
	  */
	public Timestamp getEMailVerifyDate();

    /** Column name Evaluation */
    public static final String COLUMNNAME_Evaluation = "Evaluation";

	/** Set Evaluation	  */
	public void setEvaluation (String Evaluation);

	/** Get Evaluation	  */
	public String getEvaluation();

    /** Column name FixAmt */
    public static final String COLUMNNAME_FixAmt = "FixAmt";

	/** Set Fix amount.
	  * Fix amounted amount to be levied or paid
	  */
	public void setFixAmt (BigDecimal FixAmt);

	/** Get Fix amount.
	  * Fix amounted amount to be levied or paid
	  */
	public BigDecimal getFixAmt();

    /** Column name Grade */
    public static final String COLUMNNAME_Grade = "Grade";

	/** Set Grade	  */
	public void setGrade (String Grade);

	/** Get Grade	  */
	public String getGrade();

    /** Column name Group1 */
    public static final String COLUMNNAME_Group1 = "Group1";

	/** Set Group1	  */
	public void setGroup1 (String Group1);

	/** Get Group1	  */
	public String getGroup1();

    /** Column name Informed */
    public static final String COLUMNNAME_Informed = "Informed";

	/** Set Informed	  */
	public void setInformed (String Informed);

	/** Get Informed	  */
	public String getInformed();

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

    /** Column name IsTimeReport */
    public static final String COLUMNNAME_IsTimeReport = "IsTimeReport";

	/** Set Time Report.
	  * Line is a time report only (no expense)
	  */
	public void setIsTimeReport (Timestamp IsTimeReport);

	/** Get Time Report.
	  * Line is a time report only (no expense)
	  */
	public Timestamp getIsTimeReport();

    /** Column name ItemName */
    public static final String COLUMNNAME_ItemName = "ItemName";

	/** Set Print Item Name	  */
	public void setItemName (String ItemName);

	/** Get Print Item Name	  */
	public String getItemName();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public I_M_Product getM_Product() throws RuntimeException;

    /** Column name MailText */
    public static final String COLUMNNAME_MailText = "MailText";

	/** Set Mail Text.
	  * Text used for Mail message
	  */
	public void setMailText (boolean MailText);

	/** Get Mail Text.
	  * Text used for Mail message
	  */
	public boolean isMailText();

    /** Column name month */
    public static final String COLUMNNAME_month = "month";

	/** Set month	  */
	public void setmonth (String month);

	/** Get month	  */
	public String getmonth();

    /** Column name NoMonths */
    public static final String COLUMNNAME_NoMonths = "NoMonths";

	/** Set Number of Months	  */
	public void setNoMonths (int NoMonths);

	/** Get Number of Months	  */
	public int getNoMonths();

    /** Column name OFB_Ssomac_ID */
    public static final String COLUMNNAME_OFB_Ssomac_ID = "OFB_Ssomac_ID";

	/** Set OFB_Ssomac	  */
	public void setOFB_Ssomac_ID (int OFB_Ssomac_ID);

	/** Get OFB_Ssomac	  */
	public int getOFB_Ssomac_ID();

    /** Column name PaymentTermValue */
    public static final String COLUMNNAME_PaymentTermValue = "PaymentTermValue";

	/** Set Payment Term Key.
	  * Key of the Payment Term
	  */
	public void setPaymentTermValue (BigDecimal PaymentTermValue);

	/** Get Payment Term Key.
	  * Key of the Payment Term
	  */
	public BigDecimal getPaymentTermValue();

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

    /** Column name Responsible */
    public static final String COLUMNNAME_Responsible = "Responsible";

	/** Set Responsible	  */
	public void setResponsible (int Responsible);

	/** Get Responsible	  */
	public int getResponsible();

	public I_AD_User getResponsi() throws RuntimeException;

    /** Column name rut_conductor */
    public static final String COLUMNNAME_rut_conductor = "rut_conductor";

	/** Set rut_conductor	  */
	public void setrut_conductor (String rut_conductor);

	/** Get rut_conductor	  */
	public String getrut_conductor();

    /** Column name Status */
    public static final String COLUMNNAME_Status = "Status";

	/** Set Status.
	  * Status of the currently running check
	  */
	public void setStatus (String Status);

	/** Get Status.
	  * Status of the currently running check
	  */
	public String getStatus();

    /** Column name Supervisor_ID */
    public static final String COLUMNNAME_Supervisor_ID = "Supervisor_ID";

	/** Set Supervisor.
	  * Supervisor for this user/organization - used for escalation and approval
	  */
	public void setSupervisor_ID (int Supervisor_ID);

	/** Get Supervisor.
	  * Supervisor for this user/organization - used for escalation and approval
	  */
	public int getSupervisor_ID();

	public I_AD_User getSupervisor() throws RuntimeException;

    /** Column name TenderType */
    public static final String COLUMNNAME_TenderType = "TenderType";

	/** Set Tender type.
	  * Method of Payment
	  */
	public void setTenderType (String TenderType);

	/** Get Tender type.
	  * Method of Payment
	  */
	public String getTenderType();

    /** Column name Training */
    public static final String COLUMNNAME_Training = "Training";

	/** Set Training	  */
	public void setTraining (boolean Training);

	/** Get Training	  */
	public boolean isTraining();

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
