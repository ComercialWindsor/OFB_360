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

/** Generated Interface for M_LoadBalancing
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_M_LoadBalancing 
{

    /** TableName=M_LoadBalancing */
    public static final String Table_Name = "M_LoadBalancing";

    /** AD_Table_ID=1000102 */
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

    /** Column name M_LoadBalancing_ID */
    public static final String COLUMNNAME_M_LoadBalancing_ID = "M_LoadBalancing_ID";

	/** Set M_LoadBalancing	  */
	public void setM_LoadBalancing_ID (int M_LoadBalancing_ID);

	/** Get M_LoadBalancing	  */
	public int getM_LoadBalancing_ID();

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

    /** Column name Pep */
    public static final String COLUMNNAME_Pep = "Pep";

	/** Set Pep	  */
	public void setPep (String Pep);

	/** Get Pep	  */
	public String getPep();

    /** Column name QtyFriday */
    public static final String COLUMNNAME_QtyFriday = "QtyFriday";

	/** Set QtyFriday	  */
	public void setQtyFriday (int QtyFriday);

	/** Get QtyFriday	  */
	public int getQtyFriday();

    /** Column name QtyMonday */
    public static final String COLUMNNAME_QtyMonday = "QtyMonday";

	/** Set QtyMonday	  */
	public void setQtyMonday (int QtyMonday);

	/** Get QtyMonday	  */
	public int getQtyMonday();

    /** Column name QtySaturday */
    public static final String COLUMNNAME_QtySaturday = "QtySaturday";

	/** Set QtySaturday	  */
	public void setQtySaturday (int QtySaturday);

	/** Get QtySaturday	  */
	public int getQtySaturday();

    /** Column name QtyThursday */
    public static final String COLUMNNAME_QtyThursday = "QtyThursday";

	/** Set QtyThursday	  */
	public void setQtyThursday (int QtyThursday);

	/** Get QtyThursday	  */
	public int getQtyThursday();

    /** Column name QtyTuesday */
    public static final String COLUMNNAME_QtyTuesday = "QtyTuesday";

	/** Set QtyTuesday	  */
	public void setQtyTuesday (int QtyTuesday);

	/** Get QtyTuesday	  */
	public int getQtyTuesday();

    /** Column name QtyWednesday */
    public static final String COLUMNNAME_QtyWednesday = "QtyWednesday";

	/** Set QtyWednesday	  */
	public void setQtyWednesday (int QtyWednesday);

	/** Get QtyWednesday	  */
	public int getQtyWednesday();

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
