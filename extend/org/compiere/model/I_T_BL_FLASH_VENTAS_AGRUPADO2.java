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

/** Generated Interface for T_BL_FLASH_VENTAS_AGRUPADO2
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_FLASH_VENTAS_AGRUPADO2 
{

    /** TableName=T_BL_FLASH_VENTAS_AGRUPADO2 */
    public static final String Table_Name = "T_BL_FLASH_VENTAS_AGRUPADO2";

    /** AD_Table_ID=1000165 */
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

    /** Column name AGRUPA_ANO */
    public static final String COLUMNNAME_AGRUPA_ANO = "AGRUPA_ANO";

	/** Set AGRUPA_ANO	  */
	public void setAGRUPA_ANO (boolean AGRUPA_ANO);

	/** Get AGRUPA_ANO	  */
	public boolean isAGRUPA_ANO();

    /** Column name AGRUPA_AREA */
    public static final String COLUMNNAME_AGRUPA_AREA = "AGRUPA_AREA";

	/** Set AGRUPA_AREA	  */
	public void setAGRUPA_AREA (boolean AGRUPA_AREA);

	/** Get AGRUPA_AREA	  */
	public boolean isAGRUPA_AREA();

    /** Column name AGRUPA_CLIENTE */
    public static final String COLUMNNAME_AGRUPA_CLIENTE = "AGRUPA_CLIENTE";

	/** Set AGRUPA_CLIENTE	  */
	public void setAGRUPA_CLIENTE (boolean AGRUPA_CLIENTE);

	/** Get AGRUPA_CLIENTE	  */
	public boolean isAGRUPA_CLIENTE();

    /** Column name AGRUPA_CODIGO_BLUMOS */
    public static final String COLUMNNAME_AGRUPA_CODIGO_BLUMOS = "AGRUPA_CODIGO_BLUMOS";

	/** Set AGRUPA_CODIGO_BLUMOS	  */
	public void setAGRUPA_CODIGO_BLUMOS (boolean AGRUPA_CODIGO_BLUMOS);

	/** Get AGRUPA_CODIGO_BLUMOS	  */
	public boolean isAGRUPA_CODIGO_BLUMOS();

    /** Column name AGRUPA_MES */
    public static final String COLUMNNAME_AGRUPA_MES = "AGRUPA_MES";

	/** Set AGRUPA_MES	  */
	public void setAGRUPA_MES (boolean AGRUPA_MES);

	/** Get AGRUPA_MES	  */
	public boolean isAGRUPA_MES();

    /** Column name AGRUPA_PRODUCTO */
    public static final String COLUMNNAME_AGRUPA_PRODUCTO = "AGRUPA_PRODUCTO";

	/** Set AGRUPA_PRODUCTO	  */
	public void setAGRUPA_PRODUCTO (boolean AGRUPA_PRODUCTO);

	/** Get AGRUPA_PRODUCTO	  */
	public boolean isAGRUPA_PRODUCTO();

    /** Column name AGRUPA_PROYECTO */
    public static final String COLUMNNAME_AGRUPA_PROYECTO = "AGRUPA_PROYECTO";

	/** Set AGRUPA_PROYECTO	  */
	public void setAGRUPA_PROYECTO (boolean AGRUPA_PROYECTO);

	/** Get AGRUPA_PROYECTO	  */
	public boolean isAGRUPA_PROYECTO();

    /** Column name AGRUPA_REPRESENTADA */
    public static final String COLUMNNAME_AGRUPA_REPRESENTADA = "AGRUPA_REPRESENTADA";

	/** Set AGRUPA_REPRESENTADA	  */
	public void setAGRUPA_REPRESENTADA (boolean AGRUPA_REPRESENTADA);

	/** Get AGRUPA_REPRESENTADA	  */
	public boolean isAGRUPA_REPRESENTADA();

    /** Column name AGRUPA_SOLUTEC */
    public static final String COLUMNNAME_AGRUPA_SOLUTEC = "AGRUPA_SOLUTEC";

	/** Set AGRUPA_SOLUTEC	  */
	public void setAGRUPA_SOLUTEC (boolean AGRUPA_SOLUTEC);

	/** Get AGRUPA_SOLUTEC	  */
	public boolean isAGRUPA_SOLUTEC();

    /** Column name AGRUPA_VENDEDOR */
    public static final String COLUMNNAME_AGRUPA_VENDEDOR = "AGRUPA_VENDEDOR";

	/** Set AGRUPA_VENDEDOR	  */
	public void setAGRUPA_VENDEDOR (boolean AGRUPA_VENDEDOR);

	/** Get AGRUPA_VENDEDOR	  */
	public boolean isAGRUPA_VENDEDOR();

    /** Column name ANO */
    public static final String COLUMNNAME_ANO = "ANO";

	/** Set ANO	  */
	public void setANO (String ANO);

	/** Get ANO	  */
	public String getANO();

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

    /** Column name CODIGO_BLUMOS */
    public static final String COLUMNNAME_CODIGO_BLUMOS = "CODIGO_BLUMOS";

	/** Set CODIGO_BLUMOS	  */
	public void setCODIGO_BLUMOS (String CODIGO_BLUMOS);

	/** Get CODIGO_BLUMOS	  */
	public String getCODIGO_BLUMOS();

    /** Column name COSTOLINEA */
    public static final String COLUMNNAME_COSTOLINEA = "COSTOLINEA";

	/** Set COSTOLINEA	  */
	public void setCOSTOLINEA (BigDecimal COSTOLINEA);

	/** Get COSTOLINEA	  */
	public BigDecimal getCOSTOLINEA();

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

    /** Column name LineNetAmt */
    public static final String COLUMNNAME_LineNetAmt = "LineNetAmt";

	/** Set Line Amount.
	  * Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public void setLineNetAmt (BigDecimal LineNetAmt);

	/** Get Line Amount.
	  * Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public BigDecimal getLineNetAmt();

    /** Column name MARGEN_DOLAR */
    public static final String COLUMNNAME_MARGEN_DOLAR = "MARGEN_DOLAR";

	/** Set MARGEN_DOLAR	  */
	public void setMARGEN_DOLAR (BigDecimal MARGEN_DOLAR);

	/** Get MARGEN_DOLAR	  */
	public BigDecimal getMARGEN_DOLAR();

    /** Column name MARGENLINEA */
    public static final String COLUMNNAME_MARGENLINEA = "MARGENLINEA";

	/** Set MARGENLINEA	  */
	public void setMARGENLINEA (BigDecimal MARGENLINEA);

	/** Get MARGENLINEA	  */
	public BigDecimal getMARGENLINEA();

    /** Column name MARGENPORC */
    public static final String COLUMNNAME_MARGENPORC = "MARGENPORC";

	/** Set MARGENPORC	  */
	public void setMARGENPORC (BigDecimal MARGENPORC);

	/** Get MARGENPORC	  */
	public BigDecimal getMARGENPORC();

    /** Column name MARGEN_UF */
    public static final String COLUMNNAME_MARGEN_UF = "MARGEN_UF";

	/** Set MARGEN_UF	  */
	public void setMARGEN_UF (BigDecimal MARGEN_UF);

	/** Get MARGEN_UF	  */
	public BigDecimal getMARGEN_UF();

    /** Column name MES */
    public static final String COLUMNNAME_MES = "MES";

	/** Set MES	  */
	public void setMES (String MES);

	/** Get MES	  */
	public String getMES();

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

    /** Column name ORG_DE_VENTA */
    public static final String COLUMNNAME_ORG_DE_VENTA = "ORG_DE_VENTA";

	/** Set ORG_DE_VENTA	  */
	public void setORG_DE_VENTA (BigDecimal ORG_DE_VENTA);

	/** Get ORG_DE_VENTA	  */
	public BigDecimal getORG_DE_VENTA();

    /** Column name QtyInvoiced */
    public static final String COLUMNNAME_QtyInvoiced = "QtyInvoiced";

	/** Set Quantity Invoiced.
	  * Invoiced Quantity
	  */
	public void setQtyInvoiced (BigDecimal QtyInvoiced);

	/** Get Quantity Invoiced.
	  * Invoiced Quantity
	  */
	public BigDecimal getQtyInvoiced();

    /** Column name REPRESENTADA_ID */
    public static final String COLUMNNAME_REPRESENTADA_ID = "REPRESENTADA_ID";

	/** Set REPRESENTADA_ID	  */
	public void setREPRESENTADA_ID (int REPRESENTADA_ID);

	/** Get REPRESENTADA_ID	  */
	public int getREPRESENTADA_ID();

	public I_C_BPartner getREPRESENTADA() throws RuntimeException;

    /** Column name SALDO_BODEGA_CENTRAL */
    public static final String COLUMNNAME_SALDO_BODEGA_CENTRAL = "SALDO_BODEGA_CENTRAL";

	/** Set SALDO_BODEGA_CENTRAL	  */
	public void setSALDO_BODEGA_CENTRAL (BigDecimal SALDO_BODEGA_CENTRAL);

	/** Get SALDO_BODEGA_CENTRAL	  */
	public BigDecimal getSALDO_BODEGA_CENTRAL();

    /** Column name SalesRep_ID */
    public static final String COLUMNNAME_SalesRep_ID = "SalesRep_ID";

	/** Set Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID);

	/** Get Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public int getSalesRep_ID();

	public I_AD_User getSalesRep() throws RuntimeException;

    /** Column name SOLUTEC */
    public static final String COLUMNNAME_SOLUTEC = "SOLUTEC";

	/** Set SOLUTEC	  */
	public void setSOLUTEC (boolean SOLUTEC);

	/** Get SOLUTEC	  */
	public boolean isSOLUTEC();

    /** Column name T_BL_FLASH_PARAMETROS_ID */
    public static final String COLUMNNAME_T_BL_FLASH_PARAMETROS_ID = "T_BL_FLASH_PARAMETROS_ID";

	/** Set T_BL_FLASH_PARAMETROS	  */
	public void setT_BL_FLASH_PARAMETROS_ID (int T_BL_FLASH_PARAMETROS_ID);

	/** Get T_BL_FLASH_PARAMETROS	  */
	public int getT_BL_FLASH_PARAMETROS_ID();

	public I_T_BL_FLASH_PARAMETROS getT_BL_FLASH_PARAMETROS() throws RuntimeException;

    /** Column name T_BL_FLASH_VENTAS_AGRUPADO2_ID */
    public static final String COLUMNNAME_T_BL_FLASH_VENTAS_AGRUPADO2_ID = "T_BL_FLASH_VENTAS_AGRUPADO2_ID";

	/** Set T_BL_FLASH_VENTAS_AGRUPADO2	  */
	public void setT_BL_FLASH_VENTAS_AGRUPADO2_ID (int T_BL_FLASH_VENTAS_AGRUPADO2_ID);

	/** Get T_BL_FLASH_VENTAS_AGRUPADO2	  */
	public int getT_BL_FLASH_VENTAS_AGRUPADO2_ID();

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

    /** Column name VENTA_EN_DOLAR */
    public static final String COLUMNNAME_VENTA_EN_DOLAR = "VENTA_EN_DOLAR";

	/** Set VENTA_EN_DOLAR	  */
	public void setVENTA_EN_DOLAR (BigDecimal VENTA_EN_DOLAR);

	/** Get VENTA_EN_DOLAR	  */
	public BigDecimal getVENTA_EN_DOLAR();

    /** Column name VENTA_UF */
    public static final String COLUMNNAME_VENTA_UF = "VENTA_UF";

	/** Set VENTA_UF	  */
	public void setVENTA_UF (BigDecimal VENTA_UF);

	/** Get VENTA_UF	  */
	public BigDecimal getVENTA_UF();
}
