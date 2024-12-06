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

/** Generated Interface for T_SEGUIMIENTO
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_SEGUIMIENTO 
{

    /** TableName=T_SEGUIMIENTO */
    public static final String Table_Name = "T_SEGUIMIENTO";

    /** AD_Table_ID=1000002 */
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

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

	/** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID);

	/** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID();

	public I_AD_User getAD_User() throws RuntimeException;

    /** Column name AD_USER_ID_COMENTARIO */
    public static final String COLUMNNAME_AD_USER_ID_COMENTARIO = "AD_USER_ID_COMENTARIO";

	/** Set AD_USER_ID_COMENTARIO	  */
	public void setAD_USER_ID_COMENTARIO (int AD_USER_ID_COMENTARIO);

	/** Get AD_USER_ID_COMENTARIO	  */
	public int getAD_USER_ID_COMENTARIO();

	public I_AD_User getAD_USER_ID_COMENTA() throws RuntimeException;

    /** Column name APROBACION_MUESTRA */
    public static final String COLUMNNAME_APROBACION_MUESTRA = "APROBACION_MUESTRA";

	/** Set APROBACION_MUESTRA	  */
	public void setAPROBACION_MUESTRA (boolean APROBACION_MUESTRA);

	/** Get APROBACION_MUESTRA	  */
	public boolean isAPROBACION_MUESTRA();

    /** Column name BODEGA_SOLUTEC */
    public static final String COLUMNNAME_BODEGA_SOLUTEC = "BODEGA_SOLUTEC";

	/** Set BODEGA_SOLUTEC	  */
	public void setBODEGA_SOLUTEC (boolean BODEGA_SOLUTEC);

	/** Get BODEGA_SOLUTEC	  */
	public boolean isBODEGA_SOLUTEC();

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set C_BPartner_ID	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get C_BPartner_ID	  */
	public int getC_BPartner_ID();

	public I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public I_C_Invoice getC_Invoice() throws RuntimeException;

    /** Column name C_Order_ID */
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";

	/** Set Order.
	  * Order
	  */
	public void setC_Order_ID (int C_Order_ID);

	/** Get Order.
	  * Order
	  */
	public int getC_Order_ID();

	public I_C_Order getC_Order() throws RuntimeException;

    /** Column name CORREO_ENVIADO */
    public static final String COLUMNNAME_CORREO_ENVIADO = "CORREO_ENVIADO";

	/** Set CORREO_ENVIADO	  */
	public void setCORREO_ENVIADO (boolean CORREO_ENVIADO);

	/** Get CORREO_ENVIADO	  */
	public boolean isCORREO_ENVIADO();

    /** Column name C_ProjectOFB_ID */
    public static final String COLUMNNAME_C_ProjectOFB_ID = "C_ProjectOFB_ID";

	/** Set C_ProjectOFB_ID	  */
	public void setC_ProjectOFB_ID (int C_ProjectOFB_ID);

	/** Get C_ProjectOFB_ID	  */
	public int getC_ProjectOFB_ID();

	public I_C_ProjectOFB getC_ProjectOFB() throws RuntimeException;

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

    /** Column name C_UOM_ID */
    public static final String COLUMNNAME_C_UOM_ID = "C_UOM_ID";

	/** Set UOM.
	  * Unit of Measure
	  */
	public void setC_UOM_ID (int C_UOM_ID);

	/** Get UOM.
	  * Unit of Measure
	  */
	public int getC_UOM_ID();

	public I_C_UOM getC_UOM() throws RuntimeException;

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

    /** Column name DESPACHADO_BODEGA */
    public static final String COLUMNNAME_DESPACHADO_BODEGA = "DESPACHADO_BODEGA";

	/** Set DESPACHADO_BODEGA	  */
	public void setDESPACHADO_BODEGA (boolean DESPACHADO_BODEGA);

	/** Get DESPACHADO_BODEGA	  */
	public boolean isDESPACHADO_BODEGA();

    /** Column name EMail */
    public static final String COLUMNNAME_EMail = "EMail";

	/** Set EMail Address.
	  * Electronic Mail Address
	  */
	public void setEMail (boolean EMail);

	/** Get EMail Address.
	  * Electronic Mail Address
	  */
	public boolean isEMail();

    /** Column name EMITECOMPROBANTE */
    public static final String COLUMNNAME_EMITECOMPROBANTE = "EMITECOMPROBANTE";

	/** Set EMITECOMPROBANTE	  */
	public void setEMITECOMPROBANTE (String EMITECOMPROBANTE);

	/** Get EMITECOMPROBANTE	  */
	public String getEMITECOMPROBANTE();

    /** Column name FECHA */
    public static final String COLUMNNAME_FECHA = "FECHA";

	/** Set FECHA	  */
	public void setFECHA (Timestamp FECHA);

	/** Get FECHA	  */
	public Timestamp getFECHA();

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

    /** Column name LLAMADA */
    public static final String COLUMNNAME_LLAMADA = "LLAMADA";

	/** Set LLAMADA	  */
	public void setLLAMADA (boolean LLAMADA);

	/** Get LLAMADA	  */
	public boolean isLLAMADA();

    /** Column name M_AttributeSetInstance_ID */
    public static final String COLUMNNAME_M_AttributeSetInstance_ID = "M_AttributeSetInstance_ID";

	/** Set Attribute Set Instance.
	  * Product Attribute Set Instance
	  */
	public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID);

	/** Get Attribute Set Instance.
	  * Product Attribute Set Instance
	  */
	public int getM_AttributeSetInstance_ID();

	public I_M_AttributeSetInstance getM_AttributeSetInstance() throws RuntimeException;

    /** Column name M_Locator_ID */
    public static final String COLUMNNAME_M_Locator_ID = "M_Locator_ID";

	/** Set Locator.
	  * Warehouse Locator
	  */
	public void setM_Locator_ID (int M_Locator_ID);

	/** Get Locator.
	  * Warehouse Locator
	  */
	public int getM_Locator_ID();

	public I_M_Locator getM_Locator() throws RuntimeException;

    /** Column name Motivo */
    public static final String COLUMNNAME_Motivo = "Motivo";

	/** Set Motivo	  */
	public void setMotivo (boolean Motivo);

	/** Get Motivo	  */
	public boolean isMotivo();

    /** Column name M_PriceList_ID */
    public static final String COLUMNNAME_M_PriceList_ID = "M_PriceList_ID";

	/** Set Price List.
	  * Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (String M_PriceList_ID);

	/** Get Price List.
	  * Unique identifier of a Price List
	  */
	public String getM_PriceList_ID();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public I_M_Product getM_Product() throws RuntimeException;

    /** Column name NOMBRE_MUESTRA */
    public static final String COLUMNNAME_NOMBRE_MUESTRA = "NOMBRE_MUESTRA";

	/** Set NOMBRE_MUESTRA	  */
	public void setNOMBRE_MUESTRA (String NOMBRE_MUESTRA);

	/** Get NOMBRE_MUESTRA	  */
	public String getNOMBRE_MUESTRA();

    /** Column name ORIGEN_MUESTRA */
    public static final String COLUMNNAME_ORIGEN_MUESTRA = "ORIGEN_MUESTRA";

	/** Set ORIGEN_MUESTRA	  */
	public void setORIGEN_MUESTRA (boolean ORIGEN_MUESTRA);

	/** Get ORIGEN_MUESTRA	  */
	public boolean isORIGEN_MUESTRA();

    /** Column name PorcentajeSierre */
    public static final String COLUMNNAME_PorcentajeSierre = "PorcentajeSierre";

	/** Set PorcentajeSierre	  */
	public void setPorcentajeSierre (int PorcentajeSierre);

	/** Get PorcentajeSierre	  */
	public int getPorcentajeSierre();

    /** Column name POTENCIAL_VENTA */
    public static final String COLUMNNAME_POTENCIAL_VENTA = "POTENCIAL_VENTA";

	/** Set POTENCIAL_VENTA	  */
	public void setPOTENCIAL_VENTA (BigDecimal POTENCIAL_VENTA);

	/** Get POTENCIAL_VENTA	  */
	public BigDecimal getPOTENCIAL_VENTA();

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

    /** Column name QTY_DELIVERED */
    public static final String COLUMNNAME_QTY_DELIVERED = "QTY_DELIVERED";

	/** Set QTY_DELIVERED	  */
	public void setQTY_DELIVERED (BigDecimal QTY_DELIVERED);

	/** Get QTY_DELIVERED	  */
	public BigDecimal getQTY_DELIVERED();

    /** Column name REPRESENTADA_ID */
    public static final String COLUMNNAME_REPRESENTADA_ID = "REPRESENTADA_ID";

	/** Set REPRESENTADA_ID	  */
	public void setREPRESENTADA_ID (int REPRESENTADA_ID);

	/** Get REPRESENTADA_ID	  */
	public int getREPRESENTADA_ID();

	public I_C_BPartner getREPRESENTADA() throws RuntimeException;

    /** Column name RESPUESTA */
    public static final String COLUMNNAME_RESPUESTA = "RESPUESTA";

	/** Set RESPUESTA	  */
	public void setRESPUESTA (String RESPUESTA);

	/** Get RESPUESTA	  */
	public String getRESPUESTA();

    /** Column name SOLICITAR_MUESTRA */
    public static final String COLUMNNAME_SOLICITAR_MUESTRA = "SOLICITAR_MUESTRA";

	/** Set SOLICITAR_MUESTRA	  */
	public void setSOLICITAR_MUESTRA (boolean SOLICITAR_MUESTRA);

	/** Get SOLICITAR_MUESTRA	  */
	public boolean isSOLICITAR_MUESTRA();

    /** Column name T_SEGUIMIENTO_ID */
    public static final String COLUMNNAME_T_SEGUIMIENTO_ID = "T_SEGUIMIENTO_ID";

	/** Set T_SEGUIMIENTO	  */
	public void setT_SEGUIMIENTO_ID (int T_SEGUIMIENTO_ID);

	/** Get T_SEGUIMIENTO	  */
	public int getT_SEGUIMIENTO_ID();

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

    /** Column name VALIDACION */
    public static final String COLUMNNAME_VALIDACION = "VALIDACION";

	/** Set VALIDACION	  */
	public void setVALIDACION (boolean VALIDACION);

	/** Get VALIDACION	  */
	public boolean isVALIDACION();

    /** Column name VISITA */
    public static final String COLUMNNAME_VISITA = "VISITA";

	/** Set VISITA	  */
	public void setVISITA (boolean VISITA);

	/** Get VISITA	  */
	public boolean isVISITA();
}
