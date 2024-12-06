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

/** Generated Interface for T_BL_ER_PARAMETROS
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_ER_PARAMETROS 
{

    /** TableName=T_BL_ER_PARAMETROS */
    public static final String Table_Name = "T_BL_ER_PARAMETROS";

    /** AD_Table_ID=1000135 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name ACTIVA_CONSULTA */
    public static final String COLUMNNAME_ACTIVA_CONSULTA = "ACTIVA_CONSULTA";

	/** Set ACTIVA_CONSULTA	  */
	public void setACTIVA_CONSULTA (boolean ACTIVA_CONSULTA);

	/** Get ACTIVA_CONSULTA	  */
	public boolean isACTIVA_CONSULTA();

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

    /** Column name BANDERA_CONTROL */
    public static final String COLUMNNAME_BANDERA_CONTROL = "BANDERA_CONTROL";

	/** Set BANDERA_CONTROL	  */
	public void setBANDERA_CONTROL (boolean BANDERA_CONTROL);

	/** Get BANDERA_CONTROL	  */
	public boolean isBANDERA_CONTROL();

    /** Column name BANDERA2 */
    public static final String COLUMNNAME_BANDERA2 = "BANDERA2";

	/** Set BANDERA2	  */
	public void setBANDERA2 (boolean BANDERA2);

	/** Get BANDERA2	  */
	public boolean isBANDERA2();

    /** Column name BOTON_BALANCE */
    public static final String COLUMNNAME_BOTON_BALANCE = "BOTON_BALANCE";

	/** Set BOTON_BALANCE	  */
	public void setBOTON_BALANCE (String BOTON_BALANCE);

	/** Get BOTON_BALANCE	  */
	public String getBOTON_BALANCE();

    /** Column name BOTON_BALANCE_TRIBUTARIO */
    public static final String COLUMNNAME_BOTON_BALANCE_TRIBUTARIO = "BOTON_BALANCE_TRIBUTARIO";

	/** Set BOTON_BALANCE_TRIBUTARIO	  */
	public void setBOTON_BALANCE_TRIBUTARIO (String BOTON_BALANCE_TRIBUTARIO);

	/** Get BOTON_BALANCE_TRIBUTARIO	  */
	public String getBOTON_BALANCE_TRIBUTARIO();

    /** Column name BOTON_ER */
    public static final String COLUMNNAME_BOTON_ER = "BOTON_ER";

	/** Set BOTON_ER	  */
	public void setBOTON_ER (String BOTON_ER);

	/** Get BOTON_ER	  */
	public String getBOTON_ER();

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

    /** Column name T_BL_ER_PARAMETROS_ID */
    public static final String COLUMNNAME_T_BL_ER_PARAMETROS_ID = "T_BL_ER_PARAMETROS_ID";

	/** Set T_BL_ER_PARAMETROS	  */
	public void setT_BL_ER_PARAMETROS_ID (int T_BL_ER_PARAMETROS_ID);

	/** Get T_BL_ER_PARAMETROS	  */
	public int getT_BL_ER_PARAMETROS_ID();

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
