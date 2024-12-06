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

/** Generated Interface for RH_RequisitionRestructuring
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_RH_RequisitionRestructuring 
{

    /** TableName=RH_RequisitionRestructuring */
    public static final String Table_Name = "RH_RequisitionRestructuring";

    /** AD_Table_ID=1000195 */
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

    /** Column name C_BPartner_ID2 */
    public static final String COLUMNNAME_C_BPartner_ID2 = "C_BPartner_ID2";

	/** Set C_BPartner_ID2	  */
	public void setC_BPartner_ID2 (int C_BPartner_ID2);

	/** Get C_BPartner_ID2	  */
	public int getC_BPartner_ID2();

	public I_C_BPartner getC_BPartner_() throws RuntimeException;

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

    /** Column name Charge */
    public static final String COLUMNNAME_Charge = "Charge";

	/** Set Charge	  */
	public void setCharge (int Charge);

	/** Get Charge	  */
	public int getCharge();

	public I_C_BPartner getCha() throws RuntimeException;

    /** Column name ChargeProposed */
    public static final String COLUMNNAME_ChargeProposed = "ChargeProposed";

	/** Set ChargeProposed	  */
	public void setChargeProposed (String ChargeProposed);

	/** Get ChargeProposed	  */
	public String getChargeProposed();

    /** Column name ChargeSQL */
    public static final String COLUMNNAME_ChargeSQL = "ChargeSQL";

	/** Set ChargeSQL	  */
	public void setChargeSQL (String ChargeSQL);

	/** Get ChargeSQL	  */
	public String getChargeSQL();

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

    /** Column name DateCharge */
    public static final String COLUMNNAME_DateCharge = "DateCharge";

	/** Set DateCharge	  */
	public void setDateCharge (Timestamp DateCharge);

	/** Get DateCharge	  */
	public Timestamp getDateCharge();

    /** Column name DateInduction */
    public static final String COLUMNNAME_DateInduction = "DateInduction";

	/** Set DateInduction.
	  * Transaction Date
	  */
	public void setDateInduction (Timestamp DateInduction);

	/** Get DateInduction.
	  * Transaction Date
	  */
	public Timestamp getDateInduction();

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

    /** Column name HR_Job_ID */
    public static final String COLUMNNAME_HR_Job_ID = "HR_Job_ID";

	/** Set Payroll Job	  */
	public void setHR_Job_ID (int HR_Job_ID);

	/** Get Payroll Job	  */
	public int getHR_Job_ID();

	public org.eevolution.model.I_HR_Job getHR_Job() throws RuntimeException;

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

    /** Column name IsNewCharge */
    public static final String COLUMNNAME_IsNewCharge = "IsNewCharge";

	/** Set IsNewCharge	  */
	public void setIsNewCharge (boolean IsNewCharge);

	/** Get IsNewCharge	  */
	public boolean isNewCharge();

    /** Column name IsNewHDC */
    public static final String COLUMNNAME_IsNewHDC = "IsNewHDC";

	/** Set IsNewHDC	  */
	public void setIsNewHDC (boolean IsNewHDC);

	/** Get IsNewHDC	  */
	public boolean isNewHDC();

    /** Column name IsNewOrg */
    public static final String COLUMNNAME_IsNewOrg = "IsNewOrg";

	/** Set IsNewOrg	  */
	public void setIsNewOrg (boolean IsNewOrg);

	/** Get IsNewOrg	  */
	public boolean isNewOrg();

    /** Column name IsNewRent */
    public static final String COLUMNNAME_IsNewRent = "IsNewRent";

	/** Set IsNewRent	  */
	public void setIsNewRent (boolean IsNewRent);

	/** Get IsNewRent	  */
	public boolean isNewRent();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name Net_Amount */
    public static final String COLUMNNAME_Net_Amount = "Net_Amount";

	/** Set Net_Amount	  */
	public void setNet_Amount (BigDecimal Net_Amount);

	/** Get Net_Amount	  */
	public BigDecimal getNet_Amount();

    /** Column name OtherCharge */
    public static final String COLUMNNAME_OtherCharge = "OtherCharge";

	/** Set OtherCharge	  */
	public void setOtherCharge (boolean OtherCharge);

	/** Get OtherCharge	  */
	public boolean isOtherCharge();

    /** Column name PorcentRentSQL */
    public static final String COLUMNNAME_PorcentRentSQL = "PorcentRentSQL";

	/** Set PorcentRentSQL	  */
	public void setPorcentRentSQL (BigDecimal PorcentRentSQL);

	/** Get PorcentRentSQL	  */
	public BigDecimal getPorcentRentSQL();

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

    /** Column name Proposed_Amount */
    public static final String COLUMNNAME_Proposed_Amount = "Proposed_Amount";

	/** Set Proposed_Amount	  */
	public void setProposed_Amount (BigDecimal Proposed_Amount);

	/** Get Proposed_Amount	  */
	public BigDecimal getProposed_Amount();

    /** Column name Reason */
    public static final String COLUMNNAME_Reason = "Reason";

	/** Set Reason	  */
	public void setReason (String Reason);

	/** Get Reason	  */
	public String getReason();

    /** Column name RH_RequisitionRestructuring_ID */
    public static final String COLUMNNAME_RH_RequisitionRestructuring_ID = "RH_RequisitionRestructuring_ID";

	/** Set RH_RequisitionRestructuring_ID	  */
	public void setRH_RequisitionRestructuring_ID (int RH_RequisitionRestructuring_ID);

	/** Get RH_RequisitionRestructuring_ID	  */
	public int getRH_RequisitionRestructuring_ID();

    /** Column name Signature1 */
    public static final String COLUMNNAME_Signature1 = "Signature1";

	/** Set Signature1	  */
	public void setSignature1 (boolean Signature1);

	/** Get Signature1	  */
	public boolean isSignature1();

    /** Column name Signature2 */
    public static final String COLUMNNAME_Signature2 = "Signature2";

	/** Set Signature2	  */
	public void setSignature2 (boolean Signature2);

	/** Get Signature2	  */
	public boolean isSignature2();

    /** Column name Signature3 */
    public static final String COLUMNNAME_Signature3 = "Signature3";

	/** Set Signature3	  */
	public void setSignature3 (boolean Signature3);

	/** Get Signature3	  */
	public boolean isSignature3();

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

    /** Column name ValidRentSQL */
    public static final String COLUMNNAME_ValidRentSQL = "ValidRentSQL";

	/** Set ValidRentSQL	  */
	public void setValidRentSQL (String ValidRentSQL);

	/** Get ValidRentSQL	  */
	public String getValidRentSQL();
}
