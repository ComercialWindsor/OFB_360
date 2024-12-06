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

/** Generated Interface for T_BL_FLASH_VENTAS_DETALLE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_FLASH_VENTAS_DETALLE 
{

    /** TableName=T_BL_FLASH_VENTAS_DETALLE */
    public static final String Table_Name = "T_BL_FLASH_VENTAS_DETALLE";

    /** AD_Table_ID=1000113 */
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

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public I_C_DocType getC_DocType() throws RuntimeException;

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

    /** Column name C_InvoiceLine_ID */
    public static final String COLUMNNAME_C_InvoiceLine_ID = "C_InvoiceLine_ID";

	/** Set Invoice Line.
	  * Invoice Detail Line
	  */
	public void setC_InvoiceLine_ID (int C_InvoiceLine_ID);

	/** Get Invoice Line.
	  * Invoice Detail Line
	  */
	public int getC_InvoiceLine_ID();

	public I_C_InvoiceLine getC_InvoiceLine() throws RuntimeException;

    /** Column name CODIGO_BLUMOS */
    public static final String COLUMNNAME_CODIGO_BLUMOS = "CODIGO_BLUMOS";

	/** Set CODIGO_BLUMOS	  */
	public void setCODIGO_BLUMOS (String CODIGO_BLUMOS);

	/** Get CODIGO_BLUMOS	  */
	public String getCODIGO_BLUMOS();

    /** Column name COMISION */
    public static final String COLUMNNAME_COMISION = "COMISION";

	/** Set COMISION	  */
	public void setCOMISION (BigDecimal COMISION);

	/** Get COMISION	  */
	public BigDecimal getCOMISION();

    /** Column name COMISION_SOLUTEC */
    public static final String COLUMNNAME_COMISION_SOLUTEC = "COMISION_SOLUTEC";

	/** Set COMISION_SOLUTEC	  */
	public void setCOMISION_SOLUTEC (BigDecimal COMISION_SOLUTEC);

	/** Get COMISION_SOLUTEC	  */
	public BigDecimal getCOMISION_SOLUTEC();

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

    /** Column name COSTOLINEA */
    public static final String COLUMNNAME_COSTOLINEA = "COSTOLINEA";

	/** Set COSTOLINEA	  */
	public void setCOSTOLINEA (BigDecimal COSTOLINEA);

	/** Get COSTOLINEA	  */
	public BigDecimal getCOSTOLINEA();

    /** Column name COSTOUNITARIO */
    public static final String COLUMNNAME_COSTOUNITARIO = "COSTOUNITARIO";

	/** Set COSTOUNITARIO	  */
	public void setCOSTOUNITARIO (BigDecimal COSTOUNITARIO);

	/** Get COSTOUNITARIO	  */
	public BigDecimal getCOSTOUNITARIO();

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

    /** Column name DateInvoiced */
    public static final String COLUMNNAME_DateInvoiced = "DateInvoiced";

	/** Set Date Invoiced.
	  * Date printed on Invoice
	  */
	public void setDateInvoiced (Timestamp DateInvoiced);

	/** Get Date Invoiced.
	  * Date printed on Invoice
	  */
	public Timestamp getDateInvoiced();

    /** Column name DIA */
    public static final String COLUMNNAME_DIA = "DIA";

	/** Set DIA	  */
	public void setDIA (String DIA);

	/** Get DIA	  */
	public String getDIA();

    /** Column name ESINDENT */
    public static final String COLUMNNAME_ESINDENT = "ESINDENT";

	/** Set T_BL_FLASH_VENTAS	  */
	public void setESINDENT (int ESINDENT);

	/** Get T_BL_FLASH_VENTAS	  */
	public int getESINDENT();

	public I_T_BL_CARTERA getESIND() throws RuntimeException;

    /** Column name ForeignPrice */
    public static final String COLUMNNAME_ForeignPrice = "ForeignPrice";

	/** Set ForeignPrice	  */
	public void setForeignPrice (BigDecimal ForeignPrice);

	/** Get ForeignPrice	  */
	public BigDecimal getForeignPrice();

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

    /** Column name LINEA_PRODUCTO */
    public static final String COLUMNNAME_LINEA_PRODUCTO = "LINEA_PRODUCTO";

	/** Set LINEA_PRODUCTO	  */
	public void setLINEA_PRODUCTO (String LINEA_PRODUCTO);

	/** Get LINEA_PRODUCTO	  */
	public String getLINEA_PRODUCTO();

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
	public void setORG_DE_VENTA (int ORG_DE_VENTA);

	/** Get ORG_DE_VENTA	  */
	public int getORG_DE_VENTA();

    /** Column name PLANTA_ID */
    public static final String COLUMNNAME_PLANTA_ID = "PLANTA_ID";

	/** Set PLANTA_ID	  */
	public void setPLANTA_ID (int PLANTA_ID);

	/** Get PLANTA_ID	  */
	public int getPLANTA_ID();

	public I_C_BPartner_Location getPLANTA() throws RuntimeException;

    /** Column name PriceEntered */
    public static final String COLUMNNAME_PriceEntered = "PriceEntered";

	/** Set Price.
	  * Price Entered - the price based on the selected/base UoM
	  */
	public void setPriceEntered (BigDecimal PriceEntered);

	/** Get Price.
	  * Price Entered - the price based on the selected/base UoM
	  */
	public BigDecimal getPriceEntered();

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

    /** Column name QtyOnHand */
    public static final String COLUMNNAME_QtyOnHand = "QtyOnHand";

	/** Set On Hand Quantity.
	  * On Hand Quantity
	  */
	public void setQtyOnHand (BigDecimal QtyOnHand);

	/** Get On Hand Quantity.
	  * On Hand Quantity
	  */
	public BigDecimal getQtyOnHand();

    /** Column name REPRESENTADA_ID */
    public static final String COLUMNNAME_REPRESENTADA_ID = "REPRESENTADA_ID";

	/** Set REPRESENTADA_ID	  */
	public void setREPRESENTADA_ID (int REPRESENTADA_ID);

	/** Get REPRESENTADA_ID	  */
	public int getREPRESENTADA_ID();

	public I_C_BPartner getREPRESENTADA() throws RuntimeException;

    /** Column name REPRESENTADA_PADRE_ID */
    public static final String COLUMNNAME_REPRESENTADA_PADRE_ID = "REPRESENTADA_PADRE_ID";

	/** Set REPRESENTADA_PADRE_ID	  */
	public void setREPRESENTADA_PADRE_ID (int REPRESENTADA_PADRE_ID);

	/** Get REPRESENTADA_PADRE_ID	  */
	public int getREPRESENTADA_PADRE_ID();

	public I_C_BPartner getREPRESENTADA_PADRE() throws RuntimeException;

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

    /** Column name T_BL_FLASH_VENTAS_DETALLE_ID */
    public static final String COLUMNNAME_T_BL_FLASH_VENTAS_DETALLE_ID = "T_BL_FLASH_VENTAS_DETALLE_ID";

	/** Set T_BL_FLASH_VENTAS_DETALLE	  */
	public void setT_BL_FLASH_VENTAS_DETALLE_ID (int T_BL_FLASH_VENTAS_DETALLE_ID);

	/** Get T_BL_FLASH_VENTAS_DETALLE	  */
	public int getT_BL_FLASH_VENTAS_DETALLE_ID();

    /** Column name ULTIMA_FECHA_DESPACHO */
    public static final String COLUMNNAME_ULTIMA_FECHA_DESPACHO = "ULTIMA_FECHA_DESPACHO";

	/** Set ULTIMA_FECHA_DESPACHO	  */
	public void setULTIMA_FECHA_DESPACHO (Timestamp ULTIMA_FECHA_DESPACHO);

	/** Get ULTIMA_FECHA_DESPACHO	  */
	public Timestamp getULTIMA_FECHA_DESPACHO();

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

    /** Column name VENDEDORCARTERA_ID */
    public static final String COLUMNNAME_VENDEDORCARTERA_ID = "VENDEDORCARTERA_ID";

	/** Set VENDEDORCARTERA_ID	  */
	public void setVENDEDORCARTERA_ID (int VENDEDORCARTERA_ID);

	/** Get VENDEDORCARTERA_ID	  */
	public int getVENDEDORCARTERA_ID();

	public I_AD_User getVENDEDORCARTERA() throws RuntimeException;

    /** Column name VENTA_EN_DOLAR */
    public static final String COLUMNNAME_VENTA_EN_DOLAR = "VENTA_EN_DOLAR";

	/** Set VENTA_EN_DOLAR	  */
	public void setVENTA_EN_DOLAR (BigDecimal VENTA_EN_DOLAR);

	/** Get VENTA_EN_DOLAR	  */
	public BigDecimal getVENTA_EN_DOLAR();

    /** Column name VENTA_MON_EXTRANJERA */
    public static final String COLUMNNAME_VENTA_MON_EXTRANJERA = "VENTA_MON_EXTRANJERA";

	/** Set VENTA_MON_EXTRANJERA	  */
	public void setVENTA_MON_EXTRANJERA (BigDecimal VENTA_MON_EXTRANJERA);

	/** Get VENTA_MON_EXTRANJERA	  */
	public BigDecimal getVENTA_MON_EXTRANJERA();

    /** Column name VENTANUEVA */
    public static final String COLUMNNAME_VENTANUEVA = "VENTANUEVA";

	/** Set VENTANUEVA	  */
	public void setVENTANUEVA (boolean VENTANUEVA);

	/** Get VENTANUEVA	  */
	public boolean isVENTANUEVA();

    /** Column name VENTA_UF */
    public static final String COLUMNNAME_VENTA_UF = "VENTA_UF";

	/** Set VENTA_UF	  */
	public void setVENTA_UF (BigDecimal VENTA_UF);

	/** Get VENTA_UF	  */
	public BigDecimal getVENTA_UF();
}
