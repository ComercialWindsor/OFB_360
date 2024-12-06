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

/** Generated Interface for T_BL_CONEXI_SESION
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_CONEXI_SESION 
{

    /** TableName=T_BL_CONEXI_SESION */
    public static final String Table_Name = "T_BL_CONEXI_SESION";

    /** AD_Table_ID=1000156 */
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

    /** Column name COD_BLUMOS */
    public static final String COLUMNNAME_COD_BLUMOS = "COD_BLUMOS";

	/** Set COD_BLUMOS	  */
	public void setCOD_BLUMOS (String COD_BLUMOS);

	/** Get COD_BLUMOS	  */
	public String getCOD_BLUMOS();

    /** Column name CORREO_ENVIADO */
    public static final String COLUMNNAME_CORREO_ENVIADO = "CORREO_ENVIADO";

	/** Set CORREO_ENVIADO	  */
	public void setCORREO_ENVIADO (boolean CORREO_ENVIADO);

	/** Get CORREO_ENVIADO	  */
	public boolean isCORREO_ENVIADO();

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

    /** Column name CRITERIOS */
    public static final String COLUMNNAME_CRITERIOS = "CRITERIOS";

	/** Set CRITERIOS	  */
	public void setCRITERIOS (String CRITERIOS);

	/** Get CRITERIOS	  */
	public String getCRITERIOS();

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

    /** Column name ENVIAR_CORREO */
    public static final String COLUMNNAME_ENVIAR_CORREO = "ENVIAR_CORREO";

	/** Set ENVIAR_CORREO	  */
	public void setENVIAR_CORREO (boolean ENVIAR_CORREO);

	/** Get ENVIAR_CORREO	  */
	public boolean isENVIAR_CORREO();

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

    /** Column name POR_REPRESENTADA */
    public static final String COLUMNNAME_POR_REPRESENTADA = "POR_REPRESENTADA";

	/** Set POR_REPRESENTADA	  */
	public void setPOR_REPRESENTADA (boolean POR_REPRESENTADA);

	/** Get POR_REPRESENTADA	  */
	public boolean isPOR_REPRESENTADA();

    /** Column name POR_USUARIO_COMPRAS */
    public static final String COLUMNNAME_POR_USUARIO_COMPRAS = "POR_USUARIO_COMPRAS";

	/** Set POR_USUARIO_COMPRAS	  */
	public void setPOR_USUARIO_COMPRAS (boolean POR_USUARIO_COMPRAS);

	/** Get POR_USUARIO_COMPRAS	  */
	public boolean isPOR_USUARIO_COMPRAS();

    /** Column name PROCESAR */
    public static final String COLUMNNAME_PROCESAR = "PROCESAR";

	/** Set PROCESAR	  */
	public void setPROCESAR (boolean PROCESAR);

	/** Get PROCESAR	  */
	public boolean isPROCESAR();

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

    /** Column name PRODUCTO_SOLUTEC */
    public static final String COLUMNNAME_PRODUCTO_SOLUTEC = "PRODUCTO_SOLUTEC";

	/** Set PRODUCTO_SOLUTEC	  */
	public void setPRODUCTO_SOLUTEC (boolean PRODUCTO_SOLUTEC);

	/** Get PRODUCTO_SOLUTEC	  */
	public boolean isPRODUCTO_SOLUTEC();

    /** Column name REPORTE */
    public static final String COLUMNNAME_REPORTE = "REPORTE";

	/** Set REPORTE	  */
	public void setREPORTE (String REPORTE);

	/** Get REPORTE	  */
	public String getREPORTE();

    /** Column name RESPONSABLE_PROYECCIONES_ID */
    public static final String COLUMNNAME_RESPONSABLE_PROYECCIONES_ID = "RESPONSABLE_PROYECCIONES_ID";

	/** Set RESPONSABLE_PROYECCIONES_ID	  */
	public void setRESPONSABLE_PROYECCIONES_ID (int RESPONSABLE_PROYECCIONES_ID);

	/** Get RESPONSABLE_PROYECCIONES_ID	  */
	public int getRESPONSABLE_PROYECCIONES_ID();

	public I_AD_User getRESPONSABLE_PROYECCIONES() throws RuntimeException;

    /** Column name T_BL_CONEXI_SESION_ID */
    public static final String COLUMNNAME_T_BL_CONEXI_SESION_ID = "T_BL_CONEXI_SESION_ID";

	/** Set T_BL_CONEXI_SESION	  */
	public void setT_BL_CONEXI_SESION_ID (int T_BL_CONEXI_SESION_ID);

	/** Get T_BL_CONEXI_SESION	  */
	public int getT_BL_CONEXI_SESION_ID();

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

    /** Column name USUARIO_COMPRAS_ID */
    public static final String COLUMNNAME_USUARIO_COMPRAS_ID = "USUARIO_COMPRAS_ID";

	/** Set USUARIO_COMPRAS_ID	  */
	public void setUSUARIO_COMPRAS_ID (int USUARIO_COMPRAS_ID);

	/** Get USUARIO_COMPRAS_ID	  */
	public int getUSUARIO_COMPRAS_ID();

	public I_AD_User getUSUARIO_COMPRAS() throws RuntimeException;
}
