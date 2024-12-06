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

/** Generated Interface for T_BL_ER_SALDOS
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_ER_SALDOS 
{

    /** TableName=T_BL_ER_SALDOS */
    public static final String Table_Name = "T_BL_ER_SALDOS";

    /** AD_Table_ID=1000136 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

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

    /** Column name AccountType */
    public static final String COLUMNNAME_AccountType = "AccountType";

	/** Set Account Type.
	  * Indicates the type of account
	  */
	public void setAccountType (String AccountType);

	/** Get Account Type.
	  * Indicates the type of account
	  */
	public String getAccountType();

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

    /** Column name DEBEHASTA_A */
    public static final String COLUMNNAME_DEBEHASTA_A = "DEBEHASTA_A";

	/** Set DEBEHASTA_A	  */
	public void setDEBEHASTA_A (BigDecimal DEBEHASTA_A);

	/** Get DEBEHASTA_A	  */
	public BigDecimal getDEBEHASTA_A();

    /** Column name DEBEHASTA_B */
    public static final String COLUMNNAME_DEBEHASTA_B = "DEBEHASTA_B";

	/** Set DEBEHASTA_B	  */
	public void setDEBEHASTA_B (BigDecimal DEBEHASTA_B);

	/** Get DEBEHASTA_B	  */
	public BigDecimal getDEBEHASTA_B();

    /** Column name DESDE_A */
    public static final String COLUMNNAME_DESDE_A = "DESDE_A";

	/** Set DESDE_A	  */
	public void setDESDE_A (Timestamp DESDE_A);

	/** Get DESDE_A	  */
	public Timestamp getDESDE_A();

    /** Column name DESDE_B */
    public static final String COLUMNNAME_DESDE_B = "DESDE_B";

	/** Set DESDE_B	  */
	public void setDESDE_B (Timestamp DESDE_B);

	/** Get DESDE_B	  */
	public Timestamp getDESDE_B();

    /** Column name HABERHASTA_A */
    public static final String COLUMNNAME_HABERHASTA_A = "HABERHASTA_A";

	/** Set HABERHASTA_A	  */
	public void setHABERHASTA_A (BigDecimal HABERHASTA_A);

	/** Get HABERHASTA_A	  */
	public BigDecimal getHABERHASTA_A();

    /** Column name HABERHASTA_B */
    public static final String COLUMNNAME_HABERHASTA_B = "HABERHASTA_B";

	/** Set HABERHASTA_B	  */
	public void setHABERHASTA_B (BigDecimal HABERHASTA_B);

	/** Get HABERHASTA_B	  */
	public BigDecimal getHABERHASTA_B();

    /** Column name HASTA_A */
    public static final String COLUMNNAME_HASTA_A = "HASTA_A";

	/** Set HASTA_A	  */
	public void setHASTA_A (Timestamp HASTA_A);

	/** Get HASTA_A	  */
	public Timestamp getHASTA_A();

    /** Column name HASTA_B */
    public static final String COLUMNNAME_HASTA_B = "HASTA_B";

	/** Set HASTA_B	  */
	public void setHASTA_B (Timestamp HASTA_B);

	/** Get HASTA_B	  */
	public Timestamp getHASTA_B();

    /** Column name INI_A */
    public static final String COLUMNNAME_INI_A = "INI_A";

	/** Set INI_A	  */
	public void setINI_A (Timestamp INI_A);

	/** Get INI_A	  */
	public Timestamp getINI_A();

    /** Column name INI_B */
    public static final String COLUMNNAME_INI_B = "INI_B";

	/** Set INI_B	  */
	public void setINI_B (Timestamp INI_B);

	/** Get INI_B	  */
	public Timestamp getINI_B();

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

    /** Column name LOG_CONTROL */
    public static final String COLUMNNAME_LOG_CONTROL = "LOG_CONTROL";

	/** Set LOG_CONTROL	  */
	public void setLOG_CONTROL (String LOG_CONTROL);

	/** Get LOG_CONTROL	  */
	public String getLOG_CONTROL();

    /** Column name SALDO_A */
    public static final String COLUMNNAME_SALDO_A = "SALDO_A";

	/** Set SALDO_A	  */
	public void setSALDO_A (BigDecimal SALDO_A);

	/** Get SALDO_A	  */
	public BigDecimal getSALDO_A();

    /** Column name SALDO_B */
    public static final String COLUMNNAME_SALDO_B = "SALDO_B";

	/** Set SALDO_B	  */
	public void setSALDO_B (BigDecimal SALDO_B);

	/** Get SALDO_B	  */
	public BigDecimal getSALDO_B();

    /** Column name SALDOHASTA_A */
    public static final String COLUMNNAME_SALDOHASTA_A = "SALDOHASTA_A";

	/** Set SALDOHASTA_A	  */
	public void setSALDOHASTA_A (BigDecimal SALDOHASTA_A);

	/** Get SALDOHASTA_A	  */
	public BigDecimal getSALDOHASTA_A();

    /** Column name SALDOHASTA_B */
    public static final String COLUMNNAME_SALDOHASTA_B = "SALDOHASTA_B";

	/** Set SALDOHASTA_B	  */
	public void setSALDOHASTA_B (BigDecimal SALDOHASTA_B);

	/** Get SALDOHASTA_B	  */
	public BigDecimal getSALDOHASTA_B();

    /** Column name T_BL_ER_PARAMETROS_ID */
    public static final String COLUMNNAME_T_BL_ER_PARAMETROS_ID = "T_BL_ER_PARAMETROS_ID";

	/** Set T_BL_ER_PARAMETROS	  */
	public void setT_BL_ER_PARAMETROS_ID (int T_BL_ER_PARAMETROS_ID);

	/** Get T_BL_ER_PARAMETROS	  */
	public int getT_BL_ER_PARAMETROS_ID();

	public I_T_BL_ER_PARAMETROS getT_BL_ER_PARAMETROS() throws RuntimeException;

    /** Column name T_BL_ER_SALDOS_ID */
    public static final String COLUMNNAME_T_BL_ER_SALDOS_ID = "T_BL_ER_SALDOS_ID";

	/** Set T_BL_ER_SALDOS	  */
	public void setT_BL_ER_SALDOS_ID (int T_BL_ER_SALDOS_ID);

	/** Get T_BL_ER_SALDOS	  */
	public int getT_BL_ER_SALDOS_ID();

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
