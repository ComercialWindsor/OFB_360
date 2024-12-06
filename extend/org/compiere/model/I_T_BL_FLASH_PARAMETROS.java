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

/** Generated Interface for T_BL_FLASH_PARAMETROS
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_FLASH_PARAMETROS 
{

    /** TableName=T_BL_FLASH_PARAMETROS */
    public static final String Table_Name = "T_BL_FLASH_PARAMETROS";

    /** AD_Table_ID=1000110 */
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

    /** Column name ACTIVA_GRUPO */
    public static final String COLUMNNAME_ACTIVA_GRUPO = "ACTIVA_GRUPO";

	/** Set ACTIVA_GRUPO	  */
	public void setACTIVA_GRUPO (boolean ACTIVA_GRUPO);

	/** Get ACTIVA_GRUPO	  */
	public boolean isACTIVA_GRUPO();

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

    /** Column name BANDERA_CONTROL */
    public static final String COLUMNNAME_BANDERA_CONTROL = "BANDERA_CONTROL";

	/** Set BANDERA_CONTROL	  */
	public void setBANDERA_CONTROL (boolean BANDERA_CONTROL);

	/** Get BANDERA_CONTROL	  */
	public boolean isBANDERA_CONTROL();

    /** Column name CALCULA_COMISIONES */
    public static final String COLUMNNAME_CALCULA_COMISIONES = "CALCULA_COMISIONES";

	/** Set CALCULA_COMISIONES	  */
	public void setCALCULA_COMISIONES (boolean CALCULA_COMISIONES);

	/** Get CALCULA_COMISIONES	  */
	public boolean isCALCULA_COMISIONES();

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

    /** Column name CODIGO_BLUMOS */
    public static final String COLUMNNAME_CODIGO_BLUMOS = "CODIGO_BLUMOS";

	/** Set CODIGO_BLUMOS	  */
	public void setCODIGO_BLUMOS (String CODIGO_BLUMOS);

	/** Get CODIGO_BLUMOS	  */
	public String getCODIGO_BLUMOS();

    /** Column name COMPARATIVO */
    public static final String COLUMNNAME_COMPARATIVO = "COMPARATIVO";

	/** Set COMPARATIVO	  */
	public void setCOMPARATIVO (boolean COMPARATIVO);

	/** Get COMPARATIVO	  */
	public boolean isCOMPARATIVO();

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

    /** Column name DESDE */
    public static final String COLUMNNAME_DESDE = "DESDE";

	/** Set DESDE	  */
	public void setDESDE (Timestamp DESDE);

	/** Get DESDE	  */
	public Timestamp getDESDE();

    /** Column name DESDE_B */
    public static final String COLUMNNAME_DESDE_B = "DESDE_B";

	/** Set DESDE_B	  */
	public void setDESDE_B (Timestamp DESDE_B);

	/** Get DESDE_B	  */
	public Timestamp getDESDE_B();

    /** Column name EMITE_REPORTE_COMISIONES */
    public static final String COLUMNNAME_EMITE_REPORTE_COMISIONES = "EMITE_REPORTE_COMISIONES";

	/** Set EMITE_REPORTE_COMISIONES	  */
	public void setEMITE_REPORTE_COMISIONES (String EMITE_REPORTE_COMISIONES);

	/** Get EMITE_REPORTE_COMISIONES	  */
	public String getEMITE_REPORTE_COMISIONES();

    /** Column name HASTA */
    public static final String COLUMNNAME_HASTA = "HASTA";

	/** Set HASTA	  */
	public void setHASTA (Timestamp HASTA);

	/** Get HASTA	  */
	public Timestamp getHASTA();

    /** Column name HASTA_B */
    public static final String COLUMNNAME_HASTA_B = "HASTA_B";

	/** Set HASTA_B	  */
	public void setHASTA_B (Timestamp HASTA_B);

	/** Get HASTA_B	  */
	public Timestamp getHASTA_B();

    /** Column name INCLUIR_INDENT */
    public static final String COLUMNNAME_INCLUIR_INDENT = "INCLUIR_INDENT";

	/** Set INCLUIR_INDENT	  */
	public void setINCLUIR_INDENT (boolean INCLUIR_INDENT);

	/** Get INCLUIR_INDENT	  */
	public boolean isINCLUIR_INDENT();

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

    /** Column name MARGENPORC */
    public static final String COLUMNNAME_MARGENPORC = "MARGENPORC";

	/** Set MARGENPORC	  */
	public void setMARGENPORC (BigDecimal MARGENPORC);

	/** Get MARGENPORC	  */
	public BigDecimal getMARGENPORC();

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

    /** Column name PROCESAR */
    public static final String COLUMNNAME_PROCESAR = "PROCESAR";

	/** Set PROCESAR	  */
	public void setPROCESAR (String PROCESAR);

	/** Get PROCESAR	  */
	public String getPROCESAR();

    /** Column name REPORTE_CLP */
    public static final String COLUMNNAME_REPORTE_CLP = "REPORTE_CLP";

	/** Set REPORTE_CLP	  */
	public void setREPORTE_CLP (String REPORTE_CLP);

	/** Get REPORTE_CLP	  */
	public String getREPORTE_CLP();

    /** Column name REPORTE_UF */
    public static final String COLUMNNAME_REPORTE_UF = "REPORTE_UF";

	/** Set REPORTE_UF	  */
	public void setREPORTE_UF (String REPORTE_UF);

	/** Get REPORTE_UF	  */
	public String getREPORTE_UF();

    /** Column name REPORTE_USD */
    public static final String COLUMNNAME_REPORTE_USD = "REPORTE_USD";

	/** Set REPORTE_USD	  */
	public void setREPORTE_USD (String REPORTE_USD);

	/** Get REPORTE_USD	  */
	public String getREPORTE_USD();

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

    /** Column name SOLO_INDENT */
    public static final String COLUMNNAME_SOLO_INDENT = "SOLO_INDENT";

	/** Set SOLO_INDENT	  */
	public void setSOLO_INDENT (boolean SOLO_INDENT);

	/** Get SOLO_INDENT	  */
	public boolean isSOLO_INDENT();

    /** Column name SOLUTEC */
    public static final String COLUMNNAME_SOLUTEC = "SOLUTEC";

	/** Set SOLUTEC	  */
	public void setSOLUTEC (String SOLUTEC);

	/** Get SOLUTEC	  */
	public String getSOLUTEC();

    /** Column name T_BL_FLASH_PARAMETROS_ID */
    public static final String COLUMNNAME_T_BL_FLASH_PARAMETROS_ID = "T_BL_FLASH_PARAMETROS_ID";

	/** Set T_BL_FLASH_PARAMETROS	  */
	public void setT_BL_FLASH_PARAMETROS_ID (int T_BL_FLASH_PARAMETROS_ID);

	/** Get T_BL_FLASH_PARAMETROS	  */
	public int getT_BL_FLASH_PARAMETROS_ID();

    /** Column name TOTAL_CANTIDAD */
    public static final String COLUMNNAME_TOTAL_CANTIDAD = "TOTAL_CANTIDAD";

	/** Set TOTAL_CANTIDAD	  */
	public void setTOTAL_CANTIDAD (BigDecimal TOTAL_CANTIDAD);

	/** Get TOTAL_CANTIDAD	  */
	public BigDecimal getTOTAL_CANTIDAD();

    /** Column name TOTAL_COSTO */
    public static final String COLUMNNAME_TOTAL_COSTO = "TOTAL_COSTO";

	/** Set TOTAL_COSTO	  */
	public void setTOTAL_COSTO (BigDecimal TOTAL_COSTO);

	/** Get TOTAL_COSTO	  */
	public BigDecimal getTOTAL_COSTO();

    /** Column name TOTAL_DOLARES */
    public static final String COLUMNNAME_TOTAL_DOLARES = "TOTAL_DOLARES";

	/** Set TOTAL_DOLARES	  */
	public void setTOTAL_DOLARES (BigDecimal TOTAL_DOLARES);

	/** Get TOTAL_DOLARES	  */
	public BigDecimal getTOTAL_DOLARES();

    /** Column name TOTAL_MARGEN */
    public static final String COLUMNNAME_TOTAL_MARGEN = "TOTAL_MARGEN";

	/** Set TOTAL_MARGEN	  */
	public void setTOTAL_MARGEN (BigDecimal TOTAL_MARGEN);

	/** Get TOTAL_MARGEN	  */
	public BigDecimal getTOTAL_MARGEN();

    /** Column name TOTAL_PESOS */
    public static final String COLUMNNAME_TOTAL_PESOS = "TOTAL_PESOS";

	/** Set TOTAL_PESOS	  */
	public void setTOTAL_PESOS (BigDecimal TOTAL_PESOS);

	/** Get TOTAL_PESOS	  */
	public BigDecimal getTOTAL_PESOS();

    /** Column name TOTAL_UF */
    public static final String COLUMNNAME_TOTAL_UF = "TOTAL_UF";

	/** Set TOTAL_UF	  */
	public void setTOTAL_UF (BigDecimal TOTAL_UF);

	/** Get TOTAL_UF	  */
	public BigDecimal getTOTAL_UF();

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

    /** Column name USACARTERA */
    public static final String COLUMNNAME_USACARTERA = "USACARTERA";

	/** Set USACARTERA	  */
	public void setUSACARTERA (boolean USACARTERA);

	/** Get USACARTERA	  */
	public boolean isUSACARTERA();

    /** Column name USA_FAMILIA_REPRESENTADA */
    public static final String COLUMNNAME_USA_FAMILIA_REPRESENTADA = "USA_FAMILIA_REPRESENTADA";

	/** Set USA_FAMILIA_REPRESENTADA	  */
	public void setUSA_FAMILIA_REPRESENTADA (boolean USA_FAMILIA_REPRESENTADA);

	/** Get USA_FAMILIA_REPRESENTADA	  */
	public boolean isUSA_FAMILIA_REPRESENTADA();

    /** Column name VENTA_NUEVA */
    public static final String COLUMNNAME_VENTA_NUEVA = "VENTA_NUEVA";

	/** Set VENTA_NUEVA	  */
	public void setVENTA_NUEVA (boolean VENTA_NUEVA);

	/** Get VENTA_NUEVA	  */
	public boolean isVENTA_NUEVA();

    /** Column name VER_COMPRAS */
    public static final String COLUMNNAME_VER_COMPRAS = "VER_COMPRAS";

	/** Set VER_COMPRAS	  */
	public void setVER_COMPRAS (boolean VER_COMPRAS);

	/** Get VER_COMPRAS	  */
	public boolean isVER_COMPRAS();
}
