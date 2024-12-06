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

/** Generated Interface for T_BL_CONEXI_SESIONLINE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_CONEXI_SESIONLINE 
{

    /** TableName=T_BL_CONEXI_SESIONLINE */
    public static final String Table_Name = "T_BL_CONEXI_SESIONLINE";

    /** AD_Table_ID=1000157 */
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

    /** Column name AD_Table_ID */
    public static final String COLUMNNAME_AD_Table_ID = "AD_Table_ID";

	/** Set Table.
	  * Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID);

	/** Get Table.
	  * Database Table information
	  */
	public int getAD_Table_ID();

	public I_AD_Table getAD_Table() throws RuntimeException;

    /** Column name CANTIDAD */
    public static final String COLUMNNAME_CANTIDAD = "CANTIDAD";

	/** Set CANTIDAD	  */
	public void setCANTIDAD (BigDecimal CANTIDAD);

	/** Get CANTIDAD	  */
	public BigDecimal getCANTIDAD();

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

    /** Column name CONCEPTO */
    public static final String COLUMNNAME_CONCEPTO = "CONCEPTO";

	/** Set CONCEPTO	  */
	public void setCONCEPTO (String CONCEPTO);

	/** Get CONCEPTO	  */
	public String getCONCEPTO();

    /** Column name CONCEPTO_LINK */
    public static final String COLUMNNAME_CONCEPTO_LINK = "CONCEPTO_LINK";

	/** Set CONCEPTO_LINK	  */
	public void setCONCEPTO_LINK (String CONCEPTO_LINK);

	/** Get CONCEPTO_LINK	  */
	public String getCONCEPTO_LINK();

    /** Column name C_OrderLine_ID */
    public static final String COLUMNNAME_C_OrderLine_ID = "C_OrderLine_ID";

	/** Set Sales Order Line.
	  * Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID);

	/** Get Sales Order Line.
	  * Sales Order Line
	  */
	public int getC_OrderLine_ID();

	public I_C_OrderLine getC_OrderLine() throws RuntimeException;

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

    /** Column name DateAcct */
    public static final String COLUMNNAME_DateAcct = "DateAcct";

	/** Set Account Date.
	  * Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct);

	/** Get Account Date.
	  * Accounting Date
	  */
	public Timestamp getDateAcct();

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

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

    /** Column name DES_PRODUCTO */
    public static final String COLUMNNAME_DES_PRODUCTO = "DES_PRODUCTO";

	/** Set DES_PRODUCTO	  */
	public void setDES_PRODUCTO (String DES_PRODUCTO);

	/** Get DES_PRODUCTO	  */
	public String getDES_PRODUCTO();

    /** Column name H_O_P */
    public static final String COLUMNNAME_H_O_P = "H_O_P";

	/** Set H_O_P	  */
	public void setH_O_P (String H_O_P);

	/** Get H_O_P	  */
	public String getH_O_P();

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

    /** Column name IsSOTrx */
    public static final String COLUMNNAME_IsSOTrx = "IsSOTrx";

	/** Set Sales Transaction.
	  * This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx);

	/** Get Sales Transaction.
	  * This is a Sales Transaction
	  */
	public boolean isSOTrx();

    /** Column name Line_ID */
    public static final String COLUMNNAME_Line_ID = "Line_ID";

	/** Set Line ID.
	  * Transaction line ID (internal)
	  */
	public void setLine_ID (int Line_ID);

	/** Get Line ID.
	  * Transaction line ID (internal)
	  */
	public int getLine_ID();

	public I_GL_Journal getLine() throws RuntimeException;

    /** Column name M_InOutLine_ID */
    public static final String COLUMNNAME_M_InOutLine_ID = "M_InOutLine_ID";

	/** Set Shipment/Receipt Line.
	  * Line on Shipment or Receipt document
	  */
	public void setM_InOutLine_ID (int M_InOutLine_ID);

	/** Get Shipment/Receipt Line.
	  * Line on Shipment or Receipt document
	  */
	public int getM_InOutLine_ID();

	public I_M_InOutLine getM_InOutLine() throws RuntimeException;

    /** Column name M_InventoryLine_ID */
    public static final String COLUMNNAME_M_InventoryLine_ID = "M_InventoryLine_ID";

	/** Set Phys.Inventory Line.
	  * Unique line in an Inventory document
	  */
	public void setM_InventoryLine_ID (int M_InventoryLine_ID);

	/** Get Phys.Inventory Line.
	  * Unique line in an Inventory document
	  */
	public int getM_InventoryLine_ID();

	public I_M_InventoryLine getM_InventoryLine() throws RuntimeException;

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

    /** Column name M_MovementLine_ID */
    public static final String COLUMNNAME_M_MovementLine_ID = "M_MovementLine_ID";

	/** Set Move Line.
	  * Inventory Move document Line
	  */
	public void setM_MovementLine_ID (int M_MovementLine_ID);

	/** Get Move Line.
	  * Inventory Move document Line
	  */
	public int getM_MovementLine_ID();

	public I_M_MovementLine getM_MovementLine() throws RuntimeException;

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

    /** Column name M_ProductionLine_ID */
    public static final String COLUMNNAME_M_ProductionLine_ID = "M_ProductionLine_ID";

	/** Set Production Line.
	  * Document Line representing a production
	  */
	public void setM_ProductionLine_ID (int M_ProductionLine_ID);

	/** Get Production Line.
	  * Document Line representing a production
	  */
	public int getM_ProductionLine_ID();

	public I_M_ProductionLine getM_ProductionLine() throws RuntimeException;

    /** Column name ORIGEN */
    public static final String COLUMNNAME_ORIGEN = "ORIGEN";

	/** Set ORIGEN	  */
	public void setORIGEN (String ORIGEN);

	/** Get ORIGEN	  */
	public String getORIGEN();

    /** Column name Record_ID */
    public static final String COLUMNNAME_Record_ID = "Record_ID";

	/** Set Record ID.
	  * Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID);

	/** Get Record ID.
	  * Direct internal record ID
	  */
	public int getRecord_ID();

    /** Column name T_BL_CONEXI_PROYLINE_ID */
    public static final String COLUMNNAME_T_BL_CONEXI_PROYLINE_ID = "T_BL_CONEXI_PROYLINE_ID";

	/** Set T_BL_CONEXI_PROYLINE	  */
	public void setT_BL_CONEXI_PROYLINE_ID (int T_BL_CONEXI_PROYLINE_ID);

	/** Get T_BL_CONEXI_PROYLINE	  */
	public int getT_BL_CONEXI_PROYLINE_ID();

	public I_T_BL_CONEXI_PROYLINE getT_BL_CONEXI_PROYLINE() throws RuntimeException;

    /** Column name T_BL_CONEXI_SESION_ID */
    public static final String COLUMNNAME_T_BL_CONEXI_SESION_ID = "T_BL_CONEXI_SESION_ID";

	/** Set T_BL_CONEXI_SESION	  */
	public void setT_BL_CONEXI_SESION_ID (int T_BL_CONEXI_SESION_ID);

	/** Get T_BL_CONEXI_SESION	  */
	public int getT_BL_CONEXI_SESION_ID();

	public I_T_BL_CONEXI_SESION getT_BL_CONEXI_SESION() throws RuntimeException;

    /** Column name T_BL_CONEXI_SESIONLINE_ID */
    public static final String COLUMNNAME_T_BL_CONEXI_SESIONLINE_ID = "T_BL_CONEXI_SESIONLINE_ID";

	/** Set T_BL_CONEXI_SESIONLINE	  */
	public void setT_BL_CONEXI_SESIONLINE_ID (int T_BL_CONEXI_SESIONLINE_ID);

	/** Get T_BL_CONEXI_SESIONLINE	  */
	public int getT_BL_CONEXI_SESIONLINE_ID();

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
