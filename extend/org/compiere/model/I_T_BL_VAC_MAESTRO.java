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

/** Generated Interface for T_BL_VAC_MAESTRO
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_VAC_MAESTRO 
{

    /** TableName=T_BL_VAC_MAESTRO */
    public static final String Table_Name = "T_BL_VAC_MAESTRO";

    /** AD_Table_ID=1000181 */
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

    /** Column name COMENTARIOS */
    public static final String COLUMNNAME_COMENTARIOS = "COMENTARIOS";

	/** Set COMENTARIOS	  */
	public void setCOMENTARIOS (String COMENTARIOS);

	/** Get COMENTARIOS	  */
	public String getCOMENTARIOS();

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

    /** Column name FECHA_CERTIFICADO */
    public static final String COLUMNNAME_FECHA_CERTIFICADO = "FECHA_CERTIFICADO";

	/** Set FECHA_CERTIFICADO	  */
	public void setFECHA_CERTIFICADO (Timestamp FECHA_CERTIFICADO);

	/** Get FECHA_CERTIFICADO	  */
	public Timestamp getFECHA_CERTIFICADO();

    /** Column name FECHA_CERTIFICADO_HASTA */
    public static final String COLUMNNAME_FECHA_CERTIFICADO_HASTA = "FECHA_CERTIFICADO_HASTA";

	/** Set FECHA_CERTIFICADO_HASTA	  */
	public void setFECHA_CERTIFICADO_HASTA (Timestamp FECHA_CERTIFICADO_HASTA);

	/** Get FECHA_CERTIFICADO_HASTA	  */
	public Timestamp getFECHA_CERTIFICADO_HASTA();

    /** Column name FECHA_INGRESO */
    public static final String COLUMNNAME_FECHA_INGRESO = "FECHA_INGRESO";

	/** Set FECHA_INGRESO	  */
	public void setFECHA_INGRESO (Timestamp FECHA_INGRESO);

	/** Get FECHA_INGRESO	  */
	public Timestamp getFECHA_INGRESO();

    /** Column name FINIQUITADO */
    public static final String COLUMNNAME_FINIQUITADO = "FINIQUITADO";

	/** Set FINIQUITADO	  */
	public void setFINIQUITADO (boolean FINIQUITADO);

	/** Get FINIQUITADO	  */
	public boolean isFINIQUITADO();

    /** Column name IMPRIME_DETALLE */
    public static final String COLUMNNAME_IMPRIME_DETALLE = "IMPRIME_DETALLE";

	/** Set IMPRIME_DETALLE	  */
	public void setIMPRIME_DETALLE (String IMPRIME_DETALLE);

	/** Get IMPRIME_DETALLE	  */
	public String getIMPRIME_DETALLE();

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

    /** Column name MESES_RECONOCIDOS */
    public static final String COLUMNNAME_MESES_RECONOCIDOS = "MESES_RECONOCIDOS";

	/** Set MESES_RECONOCIDOS	  */
	public void setMESES_RECONOCIDOS (BigDecimal MESES_RECONOCIDOS);

	/** Get MESES_RECONOCIDOS	  */
	public BigDecimal getMESES_RECONOCIDOS();

    /** Column name PRESENTA_CERTIFICADO */
    public static final String COLUMNNAME_PRESENTA_CERTIFICADO = "PRESENTA_CERTIFICADO";

	/** Set PRESENTA_CERTIFICADO	  */
	public void setPRESENTA_CERTIFICADO (boolean PRESENTA_CERTIFICADO);

	/** Get PRESENTA_CERTIFICADO	  */
	public boolean isPRESENTA_CERTIFICADO();

    /** Column name T_BL_VAC_MAESTRO_ID */
    public static final String COLUMNNAME_T_BL_VAC_MAESTRO_ID = "T_BL_VAC_MAESTRO_ID";

	/** Set T_BL_VAC_MAESTRO	  */
	public void setT_BL_VAC_MAESTRO_ID (int T_BL_VAC_MAESTRO_ID);

	/** Get T_BL_VAC_MAESTRO	  */
	public int getT_BL_VAC_MAESTRO_ID();

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
