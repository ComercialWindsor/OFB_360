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

/** Generated Interface for RH_RecruitmentPartner
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_RH_RecruitmentPartner 
{

    /** TableName=RH_RecruitmentPartner */
    public static final String Table_Name = "RH_RecruitmentPartner";

    /** AD_Table_ID=1000162 */
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

    /** Column name Date2 */
    public static final String COLUMNNAME_Date2 = "Date2";

	/** Set Date2	  */
	public void setDate2 (Timestamp Date2);

	/** Get Date2	  */
	public Timestamp getDate2();

    /** Column name Date3 */
    public static final String COLUMNNAME_Date3 = "Date3";

	/** Set Date3	  */
	public void setDate3 (Timestamp Date3);

	/** Get Date3	  */
	public Timestamp getDate3();

    /** Column name Date4 */
    public static final String COLUMNNAME_Date4 = "Date4";

	/** Set Date4	  */
	public void setDate4 (Timestamp Date4);

	/** Get Date4	  */
	public Timestamp getDate4();

    /** Column name Date5 */
    public static final String COLUMNNAME_Date5 = "Date5";

	/** Set Date5	  */
	public void setDate5 (Timestamp Date5);

	/** Get Date5	  */
	public Timestamp getDate5();

    /** Column name Date6 */
    public static final String COLUMNNAME_Date6 = "Date6";

	/** Set Date6	  */
	public void setDate6 (Timestamp Date6);

	/** Get Date6	  */
	public Timestamp getDate6();

    /** Column name Date7 */
    public static final String COLUMNNAME_Date7 = "Date7";

	/** Set Date7	  */
	public void setDate7 (Timestamp Date7);

	/** Get Date7	  */
	public Timestamp getDate7();

    /** Column name Date8 */
    public static final String COLUMNNAME_Date8 = "Date8";

	/** Set Date8	  */
	public void setDate8 (Timestamp Date8);

	/** Get Date8	  */
	public Timestamp getDate8();

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

    /** Column name DrivingTestAssessment */
    public static final String COLUMNNAME_DrivingTestAssessment = "DrivingTestAssessment";

	/** Set DrivingTestAssessment	  */
	public void setDrivingTestAssessment (String DrivingTestAssessment);

	/** Get DrivingTestAssessment	  */
	public String getDrivingTestAssessment();

    /** Column name DrivingTestAssessmentDesc */
    public static final String COLUMNNAME_DrivingTestAssessmentDesc = "DrivingTestAssessmentDesc";

	/** Set DrivingTestAssessmentDesc	  */
	public void setDrivingTestAssessmentDesc (String DrivingTestAssessmentDesc);

	/** Get DrivingTestAssessmentDesc	  */
	public String getDrivingTestAssessmentDesc();

    /** Column name EvaluationKeyClouding */
    public static final String COLUMNNAME_EvaluationKeyClouding = "EvaluationKeyClouding";

	/** Set EvaluationKeyClouding	  */
	public void setEvaluationKeyClouding (String EvaluationKeyClouding);

	/** Get EvaluationKeyClouding	  */
	public String getEvaluationKeyClouding();

    /** Column name EvaluationKeyCloudingDesc */
    public static final String COLUMNNAME_EvaluationKeyCloudingDesc = "EvaluationKeyCloudingDesc";

	/** Set EvaluationKeyCloudingDesc	  */
	public void setEvaluationKeyCloudingDesc (String EvaluationKeyCloudingDesc);

	/** Get EvaluationKeyCloudingDesc	  */
	public String getEvaluationKeyCloudingDesc();

    /** Column name ExaminationPerformed */
    public static final String COLUMNNAME_ExaminationPerformed = "ExaminationPerformed";

	/** Set ExaminationPerformed	  */
	public void setExaminationPerformed (String ExaminationPerformed);

	/** Get ExaminationPerformed	  */
	public String getExaminationPerformed();

    /** Column name ExaminationPerformedDesc */
    public static final String COLUMNNAME_ExaminationPerformedDesc = "ExaminationPerformedDesc";

	/** Set ExaminationPerformedDesc	  */
	public void setExaminationPerformedDesc (String ExaminationPerformedDesc);

	/** Get ExaminationPerformedDesc	  */
	public String getExaminationPerformedDesc();

    /** Column name InducedPerformance */
    public static final String COLUMNNAME_InducedPerformance = "InducedPerformance";

	/** Set InducedPerformance	  */
	public void setInducedPerformance (String InducedPerformance);

	/** Get InducedPerformance	  */
	public String getInducedPerformance();

    /** Column name InducedPerformanceDesc */
    public static final String COLUMNNAME_InducedPerformanceDesc = "InducedPerformanceDesc";

	/** Set InducedPerformanceDesc	  */
	public void setInducedPerformanceDesc (String InducedPerformanceDesc);

	/** Get InducedPerformanceDesc	  */
	public String getInducedPerformanceDesc();

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

    /** Column name IsApproved */
    public static final String COLUMNNAME_IsApproved = "IsApproved";

	/** Set Approved.
	  * Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved);

	/** Get Approved.
	  * Indicates if this document requires approval
	  */
	public boolean isApproved();

    /** Column name IsValid */
    public static final String COLUMNNAME_IsValid = "IsValid";

	/** Set Valid.
	  * Element is valid
	  */
	public void setIsValid (boolean IsValid);

	/** Get Valid.
	  * Element is valid
	  */
	public boolean isValid();

    /** Column name LaborReferencesEvaluation */
    public static final String COLUMNNAME_LaborReferencesEvaluation = "LaborReferencesEvaluation";

	/** Set LaborReferencesEvaluation	  */
	public void setLaborReferencesEvaluation (String LaborReferencesEvaluation);

	/** Get LaborReferencesEvaluation	  */
	public String getLaborReferencesEvaluation();

    /** Column name LaborReferencesEvaluationDesc */
    public static final String COLUMNNAME_LaborReferencesEvaluationDesc = "LaborReferencesEvaluationDesc";

	/** Set LaborReferencesEvaluationDesc	  */
	public void setLaborReferencesEvaluationDesc (String LaborReferencesEvaluationDesc);

	/** Get LaborReferencesEvaluationDesc	  */
	public String getLaborReferencesEvaluationDesc();

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

    /** Column name PsycholaboralEvaluation */
    public static final String COLUMNNAME_PsycholaboralEvaluation = "PsycholaboralEvaluation";

	/** Set PsycholaboralEvaluation	  */
	public void setPsycholaboralEvaluation (String PsycholaboralEvaluation);

	/** Get PsycholaboralEvaluation	  */
	public String getPsycholaboralEvaluation();

    /** Column name PsycholaboralEvaluationDesc */
    public static final String COLUMNNAME_PsycholaboralEvaluationDesc = "PsycholaboralEvaluationDesc";

	/** Set PsycholaboralEvaluationDesc	  */
	public void setPsycholaboralEvaluationDesc (String PsycholaboralEvaluationDesc);

	/** Get PsycholaboralEvaluationDesc	  */
	public String getPsycholaboralEvaluationDesc();

    /** Column name RH_RecruitmentDocument_ID */
    public static final String COLUMNNAME_RH_RecruitmentDocument_ID = "RH_RecruitmentDocument_ID";

	/** Set RH_RecruitmentDocument	  */
	public void setRH_RecruitmentDocument_ID (int RH_RecruitmentDocument_ID);

	/** Get RH_RecruitmentDocument	  */
	public int getRH_RecruitmentDocument_ID();

	public I_RH_RecruitmentDocument getRH_RecruitmentDocument() throws RuntimeException;

    /** Column name RH_RecruitmentPartner_ID */
    public static final String COLUMNNAME_RH_RecruitmentPartner_ID = "RH_RecruitmentPartner_ID";

	/** Set RH_RecruitmentPartner_ID	  */
	public void setRH_RecruitmentPartner_ID (int RH_RecruitmentPartner_ID);

	/** Get RH_RecruitmentPartner_ID	  */
	public int getRH_RecruitmentPartner_ID();

    /** Column name rhbackground */
    public static final String COLUMNNAME_rhbackground = "rhbackground";

	/** Set rhbackground	  */
	public void setrhbackground (String rhbackground);

	/** Get rhbackground	  */
	public String getrhbackground();

    /** Column name rhbackgrounddesc */
    public static final String COLUMNNAME_rhbackgrounddesc = "rhbackgrounddesc";

	/** Set rhbackgrounddesc	  */
	public void setrhbackgrounddesc (String rhbackgrounddesc);

	/** Get rhbackgrounddesc	  */
	public String getrhbackgrounddesc();

    /** Column name rhprescreening */
    public static final String COLUMNNAME_rhprescreening = "rhprescreening";

	/** Set rhprescreening	  */
	public void setrhprescreening (String rhprescreening);

	/** Get rhprescreening	  */
	public String getrhprescreening();

    /** Column name rhprescreeningdesc */
    public static final String COLUMNNAME_rhprescreeningdesc = "rhprescreeningdesc";

	/** Set rhprescreeningdesc	  */
	public void setrhprescreeningdesc (String rhprescreeningdesc);

	/** Get rhprescreeningdesc	  */
	public String getrhprescreeningdesc();

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
