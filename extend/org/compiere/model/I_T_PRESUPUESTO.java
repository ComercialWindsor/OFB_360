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

/** Generated Interface for T_PRESUPUESTO
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_PRESUPUESTO 
{

    /** TableName=T_PRESUPUESTO */
    public static final String Table_Name = "T_PRESUPUESTO";

    /** AD_Table_ID=1000045 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name accrued */
    public static final String COLUMNNAME_accrued = "accrued";

	/** Set accrued	  */
	public void setaccrued (BigDecimal accrued);

	/** Get accrued	  */
	public BigDecimal getaccrued();

    /** Column name accrued_available */
    public static final String COLUMNNAME_accrued_available = "accrued_available";

	/** Set accrued_available	  */
	public void setaccrued_available (BigDecimal accrued_available);

	/** Get accrued_available	  */
	public BigDecimal getaccrued_available();

    /** Column name accrued_mineduc */
    public static final String COLUMNNAME_accrued_mineduc = "accrued_mineduc";

	/** Set accrued_mineduc	  */
	public void setaccrued_mineduc (BigDecimal accrued_mineduc);

	/** Get accrued_mineduc	  */
	public BigDecimal getaccrued_mineduc();

    /** Column name accrued_unab */
    public static final String COLUMNNAME_accrued_unab = "accrued_unab";

	/** Set accrued_unab	  */
	public void setaccrued_unab (BigDecimal accrued_unab);

	/** Get accrued_unab	  */
	public BigDecimal getaccrued_unab();

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

    /** Column name C_SubProjectOFB_ID */
    public static final String COLUMNNAME_C_SubProjectOFB_ID = "C_SubProjectOFB_ID";

	/** Set C_SubProjectOFB_ID	  */
	public void setC_SubProjectOFB_ID (int C_SubProjectOFB_ID);

	/** Get C_SubProjectOFB_ID	  */
	public int getC_SubProjectOFB_ID();

	/*public I_C_SubProjectOFB getC_SubProjectOFB() throws RuntimeException;*/

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

    /** Column name executed */
    public static final String COLUMNNAME_executed = "executed";

	/** Set executed	  */
	public void setexecuted (BigDecimal executed);

	/** Get executed	  */
	public BigDecimal getexecuted();

    /** Column name executed_available */
    public static final String COLUMNNAME_executed_available = "executed_available";

	/** Set executed_available	  */
	public void setexecuted_available (BigDecimal executed_available);

	/** Get executed_available	  */
	public BigDecimal getexecuted_available();

    /** Column name executed_mineduc */
    public static final String COLUMNNAME_executed_mineduc = "executed_mineduc";

	/** Set executed_mineduc	  */
	public void setexecuted_mineduc (BigDecimal executed_mineduc);

	/** Get executed_mineduc	  */
	public BigDecimal getexecuted_mineduc();

    /** Column name executed_unab */
    public static final String COLUMNNAME_executed_unab = "executed_unab";

	/** Set executed_unab	  */
	public void setexecuted_unab (BigDecimal executed_unab);

	/** Get executed_unab	  */
	public BigDecimal getexecuted_unab();

    /** Column name fecha */
    public static final String COLUMNNAME_fecha = "fecha";

	/** Set fecha	  */
	public void setfecha (Timestamp fecha);

	/** Get fecha	  */
	public Timestamp getfecha();

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

    /** Column name M_Product_Category_ID */
    public static final String COLUMNNAME_M_Product_Category_ID = "M_Product_Category_ID";

	/** Set Product Category.
	  * Category of a Product
	  */
	public void setM_Product_Category_ID (int M_Product_Category_ID);

	/** Get Product Category.
	  * Category of a Product
	  */
	public int getM_Product_Category_ID();

	public I_M_Product_Category getM_Product_Category() throws RuntimeException;

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

    /** Column name mineduc_available */
    public static final String COLUMNNAME_mineduc_available = "mineduc_available";

	/** Set mineduc_available	  */
	public void setmineduc_available (BigDecimal mineduc_available);

	/** Get mineduc_available	  */
	public BigDecimal getmineduc_available();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name Name2 */
    public static final String COLUMNNAME_Name2 = "Name2";

	/** Set Name 2.
	  * Additional Name
	  */
	public void setName2 (String Name2);

	/** Get Name 2.
	  * Additional Name
	  */
	public String getName2();

    /** Column name QtyEntered */
    public static final String COLUMNNAME_QtyEntered = "QtyEntered";

	/** Set Quantity.
	  * The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEntered (BigDecimal QtyEntered);

	/** Get Quantity.
	  * The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEntered();

    /** Column name T_PRESUPUESTO_ID */
    public static final String COLUMNNAME_T_PRESUPUESTO_ID = "T_PRESUPUESTO_ID";

	/** Set T_PRESUPUESTO	  */
	public void setT_PRESUPUESTO_ID (int T_PRESUPUESTO_ID);

	/** Get T_PRESUPUESTO	  */
	public int getT_PRESUPUESTO_ID();

    /** Column name unab_available */
    public static final String COLUMNNAME_unab_available = "unab_available";

	/** Set unab_available	  */
	public void setunab_available (BigDecimal unab_available);

	/** Get unab_available	  */
	public BigDecimal getunab_available();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
