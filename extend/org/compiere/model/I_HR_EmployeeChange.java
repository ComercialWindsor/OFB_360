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

/** Generated Interface for HR_EmployeeChange
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0
 */
public interface I_HR_EmployeeChange 
{

    /** TableName=HR_EmployeeChange */
    public static final String Table_Name = "HR_EmployeeChange";

    /** AD_Table_ID=2000123 */
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

    /** Column name Benio */
    public static final String COLUMNNAME_Benio = "Benio";

	/** Set Benio	  */
	public void setBenio (String Benio);

	/** Get Benio	  */
	public String getBenio();

    /** Column name BenioOld */
    public static final String COLUMNNAME_BenioOld = "BenioOld";

	/** Set BenioOld	  */
	public void setBenioOld (String BenioOld);

	/** Get BenioOld	  */
	public String getBenioOld();

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

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_Region_ID */
    public static final String COLUMNNAME_C_Region_ID = "C_Region_ID";

	/** Set Region.
	  * Identifies a geographical Region
	  */
	public void setC_Region_ID (int C_Region_ID);

	/** Get Region.
	  * Identifies a geographical Region
	  */
	public int getC_Region_ID();

	public org.compiere.model.I_C_Region getC_Region() throws RuntimeException;

    /** Column name C_RegionOld_ID */
    public static final String COLUMNNAME_C_RegionOld_ID = "C_RegionOld_ID";

	/** Set C_RegionOld_ID	  */
	public void setC_RegionOld_ID (int C_RegionOld_ID);

	/** Get C_RegionOld_ID	  */
	public int getC_RegionOld_ID();

	public org.compiere.model.I_C_Region getC_RegionOld() throws RuntimeException;

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

    /** Column name grade */
    public static final String COLUMNNAME_grade = "grade";

	/** Set grade	  */
	public void setgrade (String grade);

	/** Get grade	  */
	public String getgrade();

    /** Column name gradeOld */
    public static final String COLUMNNAME_gradeOld = "gradeOld";

	/** Set gradeOld	  */
	public void setgradeOld (String gradeOld);

	/** Get gradeOld	  */
	public String getgradeOld();

    /** Column name HR_EmployeeChange_ID */
    public static final String COLUMNNAME_HR_EmployeeChange_ID = "HR_EmployeeChange_ID";

	/** Set HR_EmployeeChange ID	  */
	public void setHR_EmployeeChange_ID (int HR_EmployeeChange_ID);

	/** Get HR_EmployeeChange ID	  */
	public int getHR_EmployeeChange_ID();

    /** Column name HR_Job_ID */
    public static final String COLUMNNAME_HR_Job_ID = "HR_Job_ID";

	/** Set Payroll Job	  */
	public void setHR_Job_ID (int HR_Job_ID);

	/** Get Payroll Job	  */
	public int getHR_Job_ID();

	public org.eevolution.model.I_HR_Job getHR_Job() throws RuntimeException;

    /** Column name HR_JobOld_ID */
    public static final String COLUMNNAME_HR_JobOld_ID = "HR_JobOld_ID";

	/** Set HR_JobOld_ID	  */
	public void setHR_JobOld_ID (int HR_JobOld_ID);

	/** Get HR_JobOld_ID	  */
	public int getHR_JobOld_ID();

	public org.eevolution.model.I_HR_Job getHR_JobOld() throws RuntimeException;

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

    /** Column name IsAResponsibility */
    public static final String COLUMNNAME_IsAResponsibility = "IsAResponsibility";

	/** Set IsAResponsibility	  */
	public void setIsAResponsibility (boolean IsAResponsibility);

	/** Get IsAResponsibility	  */
	public boolean isAResponsibility();

    /** Column name IsAResponsibilityOld */
    public static final String COLUMNNAME_IsAResponsibilityOld = "IsAResponsibilityOld";

	/** Set IsAResponsibilityOld	  */
	public void setIsAResponsibilityOld (boolean IsAResponsibilityOld);

	/** Get IsAResponsibilityOld	  */
	public boolean isAResponsibilityOld();

    /** Column name IsBienestar */
    public static final String COLUMNNAME_IsBienestar = "IsBienestar";

	/** Set IsBienestar	  */
	public void setIsBienestar (boolean IsBienestar);

	/** Get IsBienestar	  */
	public boolean isBienestar();

    /** Column name IsBienestarOld */
    public static final String COLUMNNAME_IsBienestarOld = "IsBienestarOld";

	/** Set IsBienestarOld	  */
	public void setIsBienestarOld (boolean IsBienestarOld);

	/** Get IsBienestarOld	  */
	public boolean isBienestarOld();

    /** Column name IsCritical */
    public static final String COLUMNNAME_IsCritical = "IsCritical";

	/** Set Is Critical Component.
	  * Indicate that a Manufacturing Order can not begin without have this component
	  */
	public void setIsCritical (boolean IsCritical);

	/** Get Is Critical Component.
	  * Indicate that a Manufacturing Order can not begin without have this component
	  */
	public boolean isCritical();

    /** Column name IsCriticalOld */
    public static final String COLUMNNAME_IsCriticalOld = "IsCriticalOld";

	/** Set IsCriticalOld	  */
	public void setIsCriticalOld (boolean IsCriticalOld);

	/** Get IsCriticalOld	  */
	public boolean isCriticalOld();

    /** Column name IsProfessional */
    public static final String COLUMNNAME_IsProfessional = "IsProfessional";

	/** Set IsProfessional	  */
	public void setIsProfessional (boolean IsProfessional);

	/** Get IsProfessional	  */
	public boolean isProfessional();

    /** Column name IsProfessionalOld */
    public static final String COLUMNNAME_IsProfessionalOld = "IsProfessionalOld";

	/** Set IsProfessionalOld	  */
	public void setIsProfessionalOld (boolean IsProfessionalOld);

	/** Get IsProfessionalOld	  */
	public boolean isProfessionalOld();

    /** Column name IsUsed */
    public static final String COLUMNNAME_IsUsed = "IsUsed";

	/** Set IsUsed	  */
	public void setIsUsed (boolean IsUsed);

	/** Get IsUsed	  */
	public boolean isUsed();

    /** Column name ofbbutton */
    public static final String COLUMNNAME_ofbbutton = "ofbbutton";

	/** Set ofbbutton	  */
	public void setofbbutton (String ofbbutton);

	/** Get ofbbutton	  */
	public String getofbbutton();

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

    /** Column name QtyEntered */
    public static final String COLUMNNAME_QtyEntered = "QtyEntered";

	/** Set Quantity.
	  * The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEntered (BigDecimal QtyEntered);

	/** Get Quantity.
	  * The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEntered();

    /** Column name QtyEnteredOld */
    public static final String COLUMNNAME_QtyEnteredOld = "QtyEnteredOld";

	/** Set QtyEnteredOld.
	  * The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEnteredOld (BigDecimal QtyEnteredOld);

	/** Get QtyEnteredOld.
	  * The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEnteredOld();

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
