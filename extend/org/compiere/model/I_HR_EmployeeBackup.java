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

/** Generated Interface for HR_EmployeeBackup
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0
 */
public interface I_HR_EmployeeBackup 
{

    /** TableName=HR_EmployeeBackup */
    public static final String Table_Name = "HR_EmployeeBackup";

    /** AD_Table_ID=2000126 */
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

    /** Column name AFP_Porcent */
    public static final String COLUMNNAME_AFP_Porcent = "AFP_Porcent";

	/** Set AFP_Porcent	  */
	public void setAFP_Porcent (BigDecimal AFP_Porcent);

	/** Get AFP_Porcent	  */
	public BigDecimal getAFP_Porcent();

    /** Column name Benio */
    public static final String COLUMNNAME_Benio = "Benio";

	/** Set Benio	  */
	public void setBenio (String Benio);

	/** Get Benio	  */
	public String getBenio();

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

	public I_C_ValidCombination getC_BPartner() throws RuntimeException;

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

    /** Column name DateStart */
    public static final String COLUMNNAME_DateStart = "DateStart";

	/** Set Date Start.
	  * Date Start for this Order
	  */
	public void setDateStart (Timestamp DateStart);

	/** Get Date Start.
	  * Date Start for this Order
	  */
	public Timestamp getDateStart();

    /** Column name grade */
    public static final String COLUMNNAME_grade = "grade";

	/** Set grade	  */
	public void setgrade (String grade);

	/** Get grade	  */
	public String getgrade();

    /** Column name HR_Employee_ID */
    public static final String COLUMNNAME_HR_Employee_ID = "HR_Employee_ID";

	/** Set Payroll Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID);

	/** Get Payroll Employee	  */
	public int getHR_Employee_ID();

	public org.eevolution.model.I_HR_Employee getHR_Employee() throws RuntimeException;

    /** Column name HR_EmployeeBackup_ID */
    public static final String COLUMNNAME_HR_EmployeeBackup_ID = "HR_EmployeeBackup_ID";

	/** Set HR_EmployeeBackup ID	  */
	public void setHR_EmployeeBackup_ID (int HR_EmployeeBackup_ID);

	/** Get HR_EmployeeBackup ID	  */
	public int getHR_EmployeeBackup_ID();

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

    /** Column name ISAPRE_Porcent */
    public static final String COLUMNNAME_ISAPRE_Porcent = "ISAPRE_Porcent";

	/** Set ISAPRE_Porcent	  */
	public void setISAPRE_Porcent (BigDecimal ISAPRE_Porcent);

	/** Get ISAPRE_Porcent	  */
	public BigDecimal getISAPRE_Porcent();

    /** Column name ISAPRE_UF */
    public static final String COLUMNNAME_ISAPRE_UF = "ISAPRE_UF";

	/** Set ISAPRE_UF	  */
	public void setISAPRE_UF (BigDecimal ISAPRE_UF);

	/** Get ISAPRE_UF	  */
	public BigDecimal getISAPRE_UF();

    /** Column name IsAResponsibility */
    public static final String COLUMNNAME_IsAResponsibility = "IsAResponsibility";

	/** Set IsAResponsibility	  */
	public void setIsAResponsibility (boolean IsAResponsibility);

	/** Get IsAResponsibility	  */
	public boolean isAResponsibility();

    /** Column name IsINP */
    public static final String COLUMNNAME_IsINP = "IsINP";

	/** Set IsINP	  */
	public void setIsINP (boolean IsINP);

	/** Get IsINP	  */
	public boolean isINP();

    /** Column name IsProfessional */
    public static final String COLUMNNAME_IsProfessional = "IsProfessional";

	/** Set IsProfessional	  */
	public void setIsProfessional (boolean IsProfessional);

	/** Get IsProfessional	  */
	public boolean isProfessional();

    /** Column name MaxAmount */
    public static final String COLUMNNAME_MaxAmount = "MaxAmount";

	/** Set MaxAmount	  */
	public void setMaxAmount (BigDecimal MaxAmount);

	/** Get MaxAmount	  */
	public BigDecimal getMaxAmount();

    /** Column name TopeIsapre_UF */
    public static final String COLUMNNAME_TopeIsapre_UF = "TopeIsapre_UF";

	/** Set TopeIsapre_UF	  */
	public void setTopeIsapre_UF (BigDecimal TopeIsapre_UF);

	/** Get TopeIsapre_UF	  */
	public BigDecimal getTopeIsapre_UF();

    /** Column name TypeUniqueTax */
    public static final String COLUMNNAME_TypeUniqueTax = "TypeUniqueTax";

	/** Set TypeUniqueTax	  */
	public void setTypeUniqueTax (String TypeUniqueTax);

	/** Get TypeUniqueTax	  */
	public String getTypeUniqueTax();

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

    /** Column name UseUniqueTax */
    public static final String COLUMNNAME_UseUniqueTax = "UseUniqueTax";

	/** Set UseUniqueTax	  */
	public void setUseUniqueTax (boolean UseUniqueTax);

	/** Get UseUniqueTax	  */
	public boolean isUseUniqueTax();
}
