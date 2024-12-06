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

/** Generated Interface for TP_CostingValues
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_TP_CostingValues 
{

    /** TableName=TP_CostingValues */
    public static final String Table_Name = "TP_CostingValues";

    /** AD_Table_ID=1000064 */
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

    /** Column name Amt */
    public static final String COLUMNNAME_Amt = "Amt";

	/** Set Amount.
	  * Amount
	  */
	public void setAmt (BigDecimal Amt);

	/** Get Amount.
	  * Amount
	  */
	public BigDecimal getAmt();

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

    /** Column name MaxQty */
    public static final String COLUMNNAME_MaxQty = "MaxQty";

	/** Set MaxQty	  */
	public void setMaxQty (BigDecimal MaxQty);

	/** Get MaxQty	  */
	public BigDecimal getMaxQty();

    /** Column name MinQty */
    public static final String COLUMNNAME_MinQty = "MinQty";

	/** Set Minimum Quantity.
	  * Minimum quantity for the business partner
	  */
	public void setMinQty (BigDecimal MinQty);

	/** Get Minimum Quantity.
	  * Minimum quantity for the business partner
	  */
	public BigDecimal getMinQty();

    /** Column name TP_CostingType */
    public static final String COLUMNNAME_TP_CostingType = "TP_CostingType";

	/** Set TP_CostingType	  */
	public void setTP_CostingType (String TP_CostingType);

	/** Get TP_CostingType	  */
	public String getTP_CostingType();

    /** Column name TP_CostingValues_ID */
    public static final String COLUMNNAME_TP_CostingValues_ID = "TP_CostingValues_ID";

	/** Set TP_CostingValues	  */
	public void setTP_CostingValues_ID (int TP_CostingValues_ID);

	/** Get TP_CostingValues	  */
	public int getTP_CostingValues_ID();

    /** Column name TP_Destination_ID */
    public static final String COLUMNNAME_TP_Destination_ID = "TP_Destination_ID";

	/** Set TP_Destination	  */
	public void setTP_Destination_ID (int TP_Destination_ID);

	/** Get TP_Destination	  */
	public int getTP_Destination_ID();

	public I_TP_Destination getTP_Destination() throws RuntimeException;

    /** Column name TP_Type */
    public static final String COLUMNNAME_TP_Type = "TP_Type";

	/** Set TP_Type	  */
	public void setTP_Type (String TP_Type);

	/** Get TP_Type	  */
	public String getTP_Type();

    /** Column name TP_TypeValue */
    public static final String COLUMNNAME_TP_TypeValue = "TP_TypeValue";

	/** Set TP_TypeValue	  */
	public void setTP_TypeValue (String TP_TypeValue);

	/** Get TP_TypeValue	  */
	public String getTP_TypeValue();

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
