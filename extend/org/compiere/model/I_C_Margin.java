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

/** Generated Interface for C_Margin
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_C_Margin 
{

    /** TableName=C_Margin */
    public static final String Table_Name = "C_Margin";

    /** AD_Table_ID=1000146 */
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

    /** Column name C_Margin_ID */
    public static final String COLUMNNAME_C_Margin_ID = "C_Margin_ID";

	/** Set C_Margin_ID	  */
	public void setC_Margin_ID (int C_Margin_ID);

	/** Get C_Margin_ID	  */
	public int getC_Margin_ID();

    /** Column name C_MarginHeader_ID */
    public static final String COLUMNNAME_C_MarginHeader_ID = "C_MarginHeader_ID";

	/** Set C_MarginHeader_ID	  */
	public void setC_MarginHeader_ID (int C_MarginHeader_ID);

	/** Get C_MarginHeader_ID	  */
	public int getC_MarginHeader_ID();

	public I_C_MarginHeader getC_MarginHeader() throws RuntimeException;

    /** Column name C_Period_ID */
    public static final String COLUMNNAME_C_Period_ID = "C_Period_ID";

	/** Set Period.
	  * Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID);

	/** Get Period.
	  * Period of the Calendar
	  */
	public int getC_Period_ID();

	public I_C_Period getC_Period() throws RuntimeException;

    /** Column name costo_final */
    public static final String COLUMNNAME_costo_final = "costo_final";

	/** Set costo_final	  */
	public void setcosto_final (BigDecimal costo_final);

	/** Get costo_final	  */
	public BigDecimal getcosto_final();

    /** Column name costo_inicial */
    public static final String COLUMNNAME_costo_inicial = "costo_inicial";

	/** Set costo_inicial.
	  * costo inicial
	  */
	public void setcosto_inicial (BigDecimal costo_inicial);

	/** Get costo_inicial.
	  * costo inicial
	  */
	public BigDecimal getcosto_inicial();

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

    /** Column name inv_final */
    public static final String COLUMNNAME_inv_final = "inv_final";

	/** Set inv_final	  */
	public void setinv_final (BigDecimal inv_final);

	/** Get inv_final	  */
	public BigDecimal getinv_final();

    /** Column name inventario_inicial */
    public static final String COLUMNNAME_inventario_inicial = "inventario_inicial";

	/** Set inventario_inicial.
	  * inventario inicial
	  */
	public void setinventario_inicial (BigDecimal inventario_inicial);

	/** Get inventario_inicial.
	  * inventario inicial
	  */
	public BigDecimal getinventario_inicial();

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

    /** Column name porcent */
    public static final String COLUMNNAME_porcent = "porcent";

	/** Set porcent.
	  * % sobre ventas
	  */
	public void setporcent (BigDecimal porcent);

	/** Get porcent.
	  * % sobre ventas
	  */
	public BigDecimal getporcent();

    /** Column name precio_final */
    public static final String COLUMNNAME_precio_final = "precio_final";

	/** Set precio_final	  */
	public void setprecio_final (BigDecimal precio_final);

	/** Get precio_final	  */
	public BigDecimal getprecio_final();

    /** Column name provisiones */
    public static final String COLUMNNAME_provisiones = "provisiones";

	/** Set provisiones	  */
	public void setprovisiones (BigDecimal provisiones);

	/** Get provisiones	  */
	public BigDecimal getprovisiones();

    /** Column name qty_cost */
    public static final String COLUMNNAME_qty_cost = "qty_cost";

	/** Set qty_cost.
	  * costo de ventas
	  */
	public void setqty_cost (BigDecimal qty_cost);

	/** Get qty_cost.
	  * costo de ventas
	  */
	public BigDecimal getqty_cost();

    /** Column name qty_sales */
    public static final String COLUMNNAME_qty_sales = "qty_sales";

	/** Set qty_sales.
	  * q litros
	  */
	public void setqty_sales (BigDecimal qty_sales);

	/** Get qty_sales.
	  * q litros
	  */
	public BigDecimal getqty_sales();

    /** Column name total */
    public static final String COLUMNNAME_total = "total";

	/** Set total.
	  * margen bruto
	  */
	public void settotal (BigDecimal total);

	/** Get total.
	  * margen bruto
	  */
	public BigDecimal gettotal();

    /** Column name total_compras */
    public static final String COLUMNNAME_total_compras = "total_compras";

	/** Set total_compras.
	  * total compras
	  */
	public void settotal_compras (BigDecimal total_compras);

	/** Get total_compras.
	  * total compras
	  */
	public BigDecimal gettotal_compras();

    /** Column name total_purchases2 */
    public static final String COLUMNNAME_total_purchases2 = "total_purchases2";

	/** Set total_purchases2.
	  * $Litro
	  */
	public void settotal_purchases2 (BigDecimal total_purchases2);

	/** Get total_purchases2.
	  * $Litro
	  */
	public BigDecimal gettotal_purchases2();

    /** Column name total_sales2 */
    public static final String COLUMNNAME_total_sales2 = "total_sales2";

	/** Set total_sales2.
	  * ventas
	  */
	public void settotal_sales2 (BigDecimal total_sales2);

	/** Get total_sales2.
	  * ventas
	  */
	public BigDecimal gettotal_sales2();

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
