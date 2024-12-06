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

/** Generated Interface for T_BL_CTACTEFORM
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_CTACTEFORM 
{

    /** TableName=T_BL_CTACTEFORM */
    public static final String Table_Name = "T_BL_CTACTEFORM";

    /** AD_Table_ID=1000217 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name ABIERTA */
    public static final String COLUMNNAME_ABIERTA = "ABIERTA";

	/** Set ABIERTA	  */
	public void setABIERTA (boolean ABIERTA);

	/** Get ABIERTA	  */
	public boolean isABIERTA();

    /** Column name Account_ID */
    public static final String COLUMNNAME_Account_ID = "Account_ID";

	/** Set Account.
	  * Account used
	  */
	public void setAccount_ID (int Account_ID);

	/** Get Account.
	  * Account used
	  */
	public int getAccount_ID();

	public I_C_ElementValue getAccount() throws RuntimeException;

    /** Column name ACCOUNT2_ID */
    public static final String COLUMNNAME_ACCOUNT2_ID = "ACCOUNT2_ID";

	/** Set ACCOUNT2_ID	  */
	public void setACCOUNT2_ID (int ACCOUNT2_ID);

	/** Get ACCOUNT2_ID	  */
	public int getACCOUNT2_ID();

	public I_C_ElementValue getACCOUNT2() throws RuntimeException;

    /** Column name ACCOUNT3_ID */
    public static final String COLUMNNAME_ACCOUNT3_ID = "ACCOUNT3_ID";

	/** Set ACCOUNT3_ID	  */
	public void setACCOUNT3_ID (int ACCOUNT3_ID);

	/** Get ACCOUNT3_ID	  */
	public int getACCOUNT3_ID();

	public I_C_ElementValue getACCOUNT3() throws RuntimeException;

    /** Column name ACCOUNT4_ID */
    public static final String COLUMNNAME_ACCOUNT4_ID = "ACCOUNT4_ID";

	/** Set ACCOUNT4_ID	  */
	public void setACCOUNT4_ID (int ACCOUNT4_ID);

	/** Get ACCOUNT4_ID	  */
	public int getACCOUNT4_ID();

	public I_C_ElementValue getACCOUNT4() throws RuntimeException;

    /** Column name ACCOUNT5_ID */
    public static final String COLUMNNAME_ACCOUNT5_ID = "ACCOUNT5_ID";

	/** Set ACCOUNT5_ID	  */
	public void setACCOUNT5_ID (int ACCOUNT5_ID);

	/** Get ACCOUNT5_ID	  */
	public int getACCOUNT5_ID();

	public I_C_ElementValue getACCOUNT5() throws RuntimeException;

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

    /** Column name C_AcctSchema_ID */
    public static final String COLUMNNAME_C_AcctSchema_ID = "C_AcctSchema_ID";

	/** Set Accounting Schema.
	  * Rules for accounting
	  */
	public void setC_AcctSchema_ID (int C_AcctSchema_ID);

	/** Get Accounting Schema.
	  * Rules for accounting
	  */
	public int getC_AcctSchema_ID();

	public I_C_AcctSchema getC_AcctSchema() throws RuntimeException;

    /** Column name CALCULAR_DIF_CAMBIO */
    public static final String COLUMNNAME_CALCULAR_DIF_CAMBIO = "CALCULAR_DIF_CAMBIO";

	/** Set CALCULAR_DIF_CAMBIO	  */
	public void setCALCULAR_DIF_CAMBIO (boolean CALCULAR_DIF_CAMBIO);

	/** Get CALCULAR_DIF_CAMBIO	  */
	public boolean isCALCULAR_DIF_CAMBIO();

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

    /** Column name C_ConversionType_ID */
    public static final String COLUMNNAME_C_ConversionType_ID = "C_ConversionType_ID";

	/** Set Currency Type.
	  * Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID);

	/** Get Currency Type.
	  * Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID();

	public I_C_ConversionType getC_ConversionType() throws RuntimeException;

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

    /** Column name DESDE */
    public static final String COLUMNNAME_DESDE = "DESDE";

	/** Set DESDE	  */
	public void setDESDE (Timestamp DESDE);

	/** Get DESDE	  */
	public Timestamp getDESDE();

    /** Column name EMITIR_REP_REVAL */
    public static final String COLUMNNAME_EMITIR_REP_REVAL = "EMITIR_REP_REVAL";

	/** Set EMITIR_REP_REVAL	  */
	public void setEMITIR_REP_REVAL (String EMITIR_REP_REVAL);

	/** Get EMITIR_REP_REVAL	  */
	public String getEMITIR_REP_REVAL();

    /** Column name FECHA_CAMBIO */
    public static final String COLUMNNAME_FECHA_CAMBIO = "FECHA_CAMBIO";

	/** Set FECHA_CAMBIO	  */
	public void setFECHA_CAMBIO (Timestamp FECHA_CAMBIO);

	/** Get FECHA_CAMBIO	  */
	public Timestamp getFECHA_CAMBIO();

    /** Column name HASTA */
    public static final String COLUMNNAME_HASTA = "HASTA";

	/** Set HASTA	  */
	public void setHASTA (Timestamp HASTA);

	/** Get HASTA	  */
	public Timestamp getHASTA();

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

    /** Column name PROCESAR */
    public static final String COLUMNNAME_PROCESAR = "PROCESAR";

	/** Set PROCESAR	  */
	public void setPROCESAR (boolean PROCESAR);

	/** Get PROCESAR	  */
	public boolean isPROCESAR();

    /** Column name T_BL_CTACTEFORM_ID */
    public static final String COLUMNNAME_T_BL_CTACTEFORM_ID = "T_BL_CTACTEFORM_ID";

	/** Set T_BL_CTACTEFORM	  */
	public void setT_BL_CTACTEFORM_ID (int T_BL_CTACTEFORM_ID);

	/** Get T_BL_CTACTEFORM	  */
	public int getT_BL_CTACTEFORM_ID();

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
