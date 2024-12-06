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

/** Generated Interface for T_BL_VAC_SOLICITUD
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_VAC_SOLICITUD 
{

    /** TableName=T_BL_VAC_SOLICITUD */
    public static final String Table_Name = "T_BL_VAC_SOLICITUD";

    /** AD_Table_ID=1000190 */
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

    /** Column name ANULAR */
    public static final String COLUMNNAME_ANULAR = "ANULAR";

	/** Set ANULAR	  */
	public void setANULAR (boolean ANULAR);

	/** Get ANULAR	  */
	public boolean isANULAR();

    /** Column name APROBAR */
    public static final String COLUMNNAME_APROBAR = "APROBAR";

	/** Set APROBAR	  */
	public void setAPROBAR (boolean APROBAR);

	/** Get APROBAR	  */
	public boolean isAPROBAR();

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

    /** Column name CONTROL_IMPRESION */
    public static final String COLUMNNAME_CONTROL_IMPRESION = "CONTROL_IMPRESION";

	/** Set CONTROL_IMPRESION	  */
	public void setCONTROL_IMPRESION (boolean CONTROL_IMPRESION);

	/** Get CONTROL_IMPRESION	  */
	public boolean isCONTROL_IMPRESION();

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

    /** Column name DIAS */
    public static final String COLUMNNAME_DIAS = "DIAS";

	/** Set DIAS	  */
	public void setDIAS (BigDecimal DIAS);

	/** Get DIAS	  */
	public BigDecimal getDIAS();

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

    /** Column name FECHA_MOVIMIENTO */
    public static final String COLUMNNAME_FECHA_MOVIMIENTO = "FECHA_MOVIMIENTO";

	/** Set FECHA_MOVIMIENTO	  */
	public void setFECHA_MOVIMIENTO (Timestamp FECHA_MOVIMIENTO);

	/** Get FECHA_MOVIMIENTO	  */
	public Timestamp getFECHA_MOVIMIENTO();

    /** Column name HASTA */
    public static final String COLUMNNAME_HASTA = "HASTA";

	/** Set HASTA	  */
	public void setHASTA (Timestamp HASTA);

	/** Get HASTA	  */
	public Timestamp getHASTA();

    /** Column name IMPRIME_COMPROBANTE */
    public static final String COLUMNNAME_IMPRIME_COMPROBANTE = "IMPRIME_COMPROBANTE";

	/** Set IMPRIME_COMPROBANTE	  */
	public void setIMPRIME_COMPROBANTE (String IMPRIME_COMPROBANTE);

	/** Get IMPRIME_COMPROBANTE	  */
	public String getIMPRIME_COMPROBANTE();

    /** Column name IMPRIME_RESUMEN */
    public static final String COLUMNNAME_IMPRIME_RESUMEN = "IMPRIME_RESUMEN";

	/** Set IMPRIME_RESUMEN	  */
	public void setIMPRIME_RESUMEN (String IMPRIME_RESUMEN);

	/** Get IMPRIME_RESUMEN	  */
	public String getIMPRIME_RESUMEN();

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

    /** Column name MEDIO_DIA */
    public static final String COLUMNNAME_MEDIO_DIA = "MEDIO_DIA";

	/** Set MEDIO_DIA	  */
	public void setMEDIO_DIA (boolean MEDIO_DIA);

	/** Get MEDIO_DIA	  */
	public boolean isMEDIO_DIA();

    /** Column name PROCESAR */
    public static final String COLUMNNAME_PROCESAR = "PROCESAR";

	/** Set PROCESAR	  */
	public void setPROCESAR (String PROCESAR);

	/** Get PROCESAR	  */
	public String getPROCESAR();

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

    /** Column name PROGRESIVO */
    public static final String COLUMNNAME_PROGRESIVO = "PROGRESIVO";

	/** Set PROGRESIVO	  */
	public void setPROGRESIVO (boolean PROGRESIVO);

	/** Get PROGRESIVO	  */
	public boolean isPROGRESIVO();

    /** Column name REVERTIR */
    public static final String COLUMNNAME_REVERTIR = "REVERTIR";

	/** Set REVERTIR	  */
	public void setREVERTIR (boolean REVERTIR);

	/** Get REVERTIR	  */
	public boolean isREVERTIR();

    /** Column name REVERTIR_BOTON */
    public static final String COLUMNNAME_REVERTIR_BOTON = "REVERTIR_BOTON";

	/** Set REVERTIR_BOTON	  */
	public void setREVERTIR_BOTON (String REVERTIR_BOTON);

	/** Get REVERTIR_BOTON	  */
	public String getREVERTIR_BOTON();

    /** Column name SALDO_NORMAL */
    public static final String COLUMNNAME_SALDO_NORMAL = "SALDO_NORMAL";

	/** Set SALDO_NORMAL	  */
	public void setSALDO_NORMAL (BigDecimal SALDO_NORMAL);

	/** Get SALDO_NORMAL	  */
	public BigDecimal getSALDO_NORMAL();

    /** Column name SALDO_PP */
    public static final String COLUMNNAME_SALDO_PP = "SALDO_PP";

	/** Set SALDO_PP	  */
	public void setSALDO_PP (BigDecimal SALDO_PP);

	/** Get SALDO_PP	  */
	public BigDecimal getSALDO_PP();

    /** Column name SALDO_PROGRESIVO */
    public static final String COLUMNNAME_SALDO_PROGRESIVO = "SALDO_PROGRESIVO";

	/** Set SALDO_PROGRESIVO	  */
	public void setSALDO_PROGRESIVO (BigDecimal SALDO_PROGRESIVO);

	/** Get SALDO_PROGRESIVO	  */
	public BigDecimal getSALDO_PROGRESIVO();

    /** Column name SALDO_TOTAL */
    public static final String COLUMNNAME_SALDO_TOTAL = "SALDO_TOTAL";

	/** Set SALDO_TOTAL	  */
	public void setSALDO_TOTAL (BigDecimal SALDO_TOTAL);

	/** Get SALDO_TOTAL	  */
	public BigDecimal getSALDO_TOTAL();

    /** Column name SOLICITAR */
    public static final String COLUMNNAME_SOLICITAR = "SOLICITAR";

	/** Set SOLICITAR	  */
	public void setSOLICITAR (boolean SOLICITAR);

	/** Get SOLICITAR	  */
	public boolean isSOLICITAR();

    /** Column name T_BL_VAC_MOVIMIENTOS_ID */
    public static final String COLUMNNAME_T_BL_VAC_MOVIMIENTOS_ID = "T_BL_VAC_MOVIMIENTOS_ID";

	/** Set T_BL_VAC_MOVIMIENTOS	  */
	public void setT_BL_VAC_MOVIMIENTOS_ID (int T_BL_VAC_MOVIMIENTOS_ID);

	/** Get T_BL_VAC_MOVIMIENTOS	  */
	public int getT_BL_VAC_MOVIMIENTOS_ID();

	public I_T_BL_VAC_MOVIMIENTOS getT_BL_VAC_MOVIMIENTOS() throws RuntimeException;

    /** Column name T_BL_VAC_SOLICITUD_ID */
    public static final String COLUMNNAME_T_BL_VAC_SOLICITUD_ID = "T_BL_VAC_SOLICITUD_ID";

	/** Set T_BL_VAC_SOLICITUD	  */
	public void setT_BL_VAC_SOLICITUD_ID (int T_BL_VAC_SOLICITUD_ID);

	/** Get T_BL_VAC_SOLICITUD	  */
	public int getT_BL_VAC_SOLICITUD_ID();

    /** Column name TIPO_MOVIMIENTO */
    public static final String COLUMNNAME_TIPO_MOVIMIENTO = "TIPO_MOVIMIENTO";

	/** Set TIPO_MOVIMIENTO	  */
	public void setTIPO_MOVIMIENTO (String TIPO_MOVIMIENTO);

	/** Get TIPO_MOVIMIENTO	  */
	public String getTIPO_MOVIMIENTO();

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
