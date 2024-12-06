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

/** Generated Interface for T_BL_FLASH_VENTAS_COMPARA
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_FLASH_VENTAS_COMPARA 
{

    /** TableName=T_BL_FLASH_VENTAS_COMPARA */
    public static final String Table_Name = "T_BL_FLASH_VENTAS_COMPARA";

    /** AD_Table_ID=1000166 */
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

    /** Column name ANO2 */
    public static final String COLUMNNAME_ANO2 = "ANO2";

	/** Set ANO2	  */
	public void setANO2 (String ANO2);

	/** Get ANO2	  */
	public String getANO2();

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

    /** Column name COSTOLINEA2 */
    public static final String COLUMNNAME_COSTOLINEA2 = "COSTOLINEA2";

	/** Set COSTOLINEA2	  */
	public void setCOSTOLINEA2 (BigDecimal COSTOLINEA2);

	/** Get COSTOLINEA2	  */
	public BigDecimal getCOSTOLINEA2();

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

    /** Column name DIFCANTIDAD */
    public static final String COLUMNNAME_DIFCANTIDAD = "DIFCANTIDAD";

	/** Set DIFCANTIDAD	  */
	public void setDIFCANTIDAD (BigDecimal DIFCANTIDAD);

	/** Get DIFCANTIDAD	  */
	public BigDecimal getDIFCANTIDAD();

    /** Column name DIFCOSTO */
    public static final String COLUMNNAME_DIFCOSTO = "DIFCOSTO";

	/** Set DIFCOSTO	  */
	public void setDIFCOSTO (BigDecimal DIFCOSTO);

	/** Get DIFCOSTO	  */
	public BigDecimal getDIFCOSTO();

    /** Column name DIFERENCIA */
    public static final String COLUMNNAME_DIFERENCIA = "DIFERENCIA";

	/** Set DIFERENCIA	  */
	public void setDIFERENCIA (String DIFERENCIA);

	/** Get DIFERENCIA	  */
	public String getDIFERENCIA();

    /** Column name DIFMARGEN */
    public static final String COLUMNNAME_DIFMARGEN = "DIFMARGEN";

	/** Set DIFMARGEN	  */
	public void setDIFMARGEN (BigDecimal DIFMARGEN);

	/** Get DIFMARGEN	  */
	public BigDecimal getDIFMARGEN();

    /** Column name DIF_MARGEN_DOLAR */
    public static final String COLUMNNAME_DIF_MARGEN_DOLAR = "DIF_MARGEN_DOLAR";

	/** Set DIF_MARGEN_DOLAR	  */
	public void setDIF_MARGEN_DOLAR (BigDecimal DIF_MARGEN_DOLAR);

	/** Get DIF_MARGEN_DOLAR	  */
	public BigDecimal getDIF_MARGEN_DOLAR();

    /** Column name DIF_MARGEN_UF */
    public static final String COLUMNNAME_DIF_MARGEN_UF = "DIF_MARGEN_UF";

	/** Set DIF_MARGEN_UF	  */
	public void setDIF_MARGEN_UF (BigDecimal DIF_MARGEN_UF);

	/** Get DIF_MARGEN_UF	  */
	public BigDecimal getDIF_MARGEN_UF();

    /** Column name DIFNETO */
    public static final String COLUMNNAME_DIFNETO = "DIFNETO";

	/** Set DIFNETO	  */
	public void setDIFNETO (BigDecimal DIFNETO);

	/** Get DIFNETO	  */
	public BigDecimal getDIFNETO();

    /** Column name DIFPORCVTADOLAR */
    public static final String COLUMNNAME_DIFPORCVTADOLAR = "DIFPORCVTADOLAR";

	/** Set DIFPORCVTADOLAR	  */
	public void setDIFPORCVTADOLAR (BigDecimal DIFPORCVTADOLAR);

	/** Get DIFPORCVTADOLAR	  */
	public BigDecimal getDIFPORCVTADOLAR();

    /** Column name DIFVENTADOLAR */
    public static final String COLUMNNAME_DIFVENTADOLAR = "DIFVENTADOLAR";

	/** Set DIFVENTADOLAR	  */
	public void setDIFVENTADOLAR (BigDecimal DIFVENTADOLAR);

	/** Get DIFVENTADOLAR	  */
	public BigDecimal getDIFVENTADOLAR();

    /** Column name DIFVENTAUF */
    public static final String COLUMNNAME_DIFVENTAUF = "DIFVENTAUF";

	/** Set DIFVENTAUF	  */
	public void setDIFVENTAUF (BigDecimal DIFVENTAUF);

	/** Get DIFVENTAUF	  */
	public BigDecimal getDIFVENTAUF();

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

    /** Column name LINENETAMT2 */
    public static final String COLUMNNAME_LINENETAMT2 = "LINENETAMT2";

	/** Set LINENETAMT2	  */
	public void setLINENETAMT2 (BigDecimal LINENETAMT2);

	/** Get LINENETAMT2	  */
	public BigDecimal getLINENETAMT2();

    /** Column name MARGEN_DOLAR */
    public static final String COLUMNNAME_MARGEN_DOLAR = "MARGEN_DOLAR";

	/** Set MARGEN_DOLAR	  */
	public void setMARGEN_DOLAR (BigDecimal MARGEN_DOLAR);

	/** Get MARGEN_DOLAR	  */
	public BigDecimal getMARGEN_DOLAR();

    /** Column name MARGEN_DOLAR2 */
    public static final String COLUMNNAME_MARGEN_DOLAR2 = "MARGEN_DOLAR2";

	/** Set MARGEN_DOLAR2	  */
	public void setMARGEN_DOLAR2 (BigDecimal MARGEN_DOLAR2);

	/** Get MARGEN_DOLAR2	  */
	public BigDecimal getMARGEN_DOLAR2();

    /** Column name MARGENLINEA */
    public static final String COLUMNNAME_MARGENLINEA = "MARGENLINEA";

	/** Set MARGENLINEA	  */
	public void setMARGENLINEA (BigDecimal MARGENLINEA);

	/** Get MARGENLINEA	  */
	public BigDecimal getMARGENLINEA();

    /** Column name MARGENLINEA2 */
    public static final String COLUMNNAME_MARGENLINEA2 = "MARGENLINEA2";

	/** Set MARGENLINEA2	  */
	public void setMARGENLINEA2 (BigDecimal MARGENLINEA2);

	/** Get MARGENLINEA2	  */
	public BigDecimal getMARGENLINEA2();

    /** Column name MARGENPORC */
    public static final String COLUMNNAME_MARGENPORC = "MARGENPORC";

	/** Set MARGENPORC	  */
	public void setMARGENPORC (BigDecimal MARGENPORC);

	/** Get MARGENPORC	  */
	public BigDecimal getMARGENPORC();

    /** Column name MARGENPORC2 */
    public static final String COLUMNNAME_MARGENPORC2 = "MARGENPORC2";

	/** Set MARGENPORC2	  */
	public void setMARGENPORC2 (BigDecimal MARGENPORC2);

	/** Get MARGENPORC2	  */
	public BigDecimal getMARGENPORC2();

    /** Column name MARGEN_UF */
    public static final String COLUMNNAME_MARGEN_UF = "MARGEN_UF";

	/** Set MARGEN_UF	  */
	public void setMARGEN_UF (BigDecimal MARGEN_UF);

	/** Get MARGEN_UF	  */
	public BigDecimal getMARGEN_UF();

    /** Column name MARGEN_UF2 */
    public static final String COLUMNNAME_MARGEN_UF2 = "MARGEN_UF2";

	/** Set MARGEN_UF2	  */
	public void setMARGEN_UF2 (BigDecimal MARGEN_UF2);

	/** Get MARGEN_UF2	  */
	public BigDecimal getMARGEN_UF2();

    /** Column name MES */
    public static final String COLUMNNAME_MES = "MES";

	/** Set MES	  */
	public void setMES (String MES);

	/** Get MES	  */
	public String getMES();

    /** Column name MES2 */
    public static final String COLUMNNAME_MES2 = "MES2";

	/** Set MES2	  */
	public void setMES2 (String MES2);

	/** Get MES2	  */
	public String getMES2();

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

    /** Column name QTYINVOICED2 */
    public static final String COLUMNNAME_QTYINVOICED2 = "QTYINVOICED2";

	/** Set QTYINVOICED2	  */
	public void setQTYINVOICED2 (BigDecimal QTYINVOICED2);

	/** Get QTYINVOICED2	  */
	public BigDecimal getQTYINVOICED2();

    /** Column name REPRESENTADA_ID */
    public static final String COLUMNNAME_REPRESENTADA_ID = "REPRESENTADA_ID";

	/** Set REPRESENTADA_ID	  */
	public void setREPRESENTADA_ID (int REPRESENTADA_ID);

	/** Get REPRESENTADA_ID	  */
	public int getREPRESENTADA_ID();

	public I_C_BPartner getREPRESENTADA() throws RuntimeException;

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

    /** Column name T_BL_FLASH_VENTAS_COMPARA_ID */
    public static final String COLUMNNAME_T_BL_FLASH_VENTAS_COMPARA_ID = "T_BL_FLASH_VENTAS_COMPARA_ID";

	/** Set T_BL_FLASH_VENTAS_COMPARA	  */
	public void setT_BL_FLASH_VENTAS_COMPARA_ID (int T_BL_FLASH_VENTAS_COMPARA_ID);

	/** Get T_BL_FLASH_VENTAS_COMPARA	  */
	public int getT_BL_FLASH_VENTAS_COMPARA_ID();

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

    /** Column name VENTA_EN_DOLAR2 */
    public static final String COLUMNNAME_VENTA_EN_DOLAR2 = "VENTA_EN_DOLAR2";

	/** Set VENTA_EN_DOLAR2	  */
	public void setVENTA_EN_DOLAR2 (BigDecimal VENTA_EN_DOLAR2);

	/** Get VENTA_EN_DOLAR2	  */
	public BigDecimal getVENTA_EN_DOLAR2();

    /** Column name VENTA_UF */
    public static final String COLUMNNAME_VENTA_UF = "VENTA_UF";

	/** Set VENTA_UF	  */
	public void setVENTA_UF (BigDecimal VENTA_UF);

	/** Get VENTA_UF	  */
	public BigDecimal getVENTA_UF();

    /** Column name VENTA_UF2 */
    public static final String COLUMNNAME_VENTA_UF2 = "VENTA_UF2";

	/** Set VENTA_UF2	  */
	public void setVENTA_UF2 (BigDecimal VENTA_UF2);

	/** Get VENTA_UF2	  */
	public BigDecimal getVENTA_UF2();
}
