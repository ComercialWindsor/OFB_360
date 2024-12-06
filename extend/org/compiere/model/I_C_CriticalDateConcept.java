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

/** Generated Interface for C_CriticalDateConcept
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_C_CriticalDateConcept 
{

    /** TableName=C_CriticalDateConcept */
    public static final String Table_Name = "C_CriticalDateConcept";

    /** AD_Table_ID=1000089 */
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

    /** Column name C_CriticalDateConcept_ID */
    public static final String COLUMNNAME_C_CriticalDateConcept_ID = "C_CriticalDateConcept_ID";

	/** Set C_CriticalDateConcept	  */
	public void setC_CriticalDateConcept_ID (int C_CriticalDateConcept_ID);

	/** Get C_CriticalDateConcept	  */
	public int getC_CriticalDateConcept_ID();

    /** Column name C_ProjectOFB_ID */
    public static final String COLUMNNAME_C_ProjectOFB_ID = "C_ProjectOFB_ID";

	/** Set C_ProjectOFB_ID	  */
	public void setC_ProjectOFB_ID (int C_ProjectOFB_ID);

	/** Get C_ProjectOFB_ID	  */
	public int getC_ProjectOFB_ID();

	public I_C_ProjectOFB getC_ProjectOFB() throws RuntimeException;

    /** Column name Classname */
    public static final String COLUMNNAME_Classname = "Classname";

	/** Set Classname.
	  * Java Classname
	  */
	public void setClassname (String Classname);

	/** Get Classname.
	  * Java Classname
	  */
	public String getClassname();

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

    /** Column name IsDue */
    public static final String COLUMNNAME_IsDue = "IsDue";

	/** Set Due.
	  * Subscription Renewal is Due
	  */
	public void setIsDue (BigDecimal IsDue);

	/** Get Due.
	  * Subscription Renewal is Due
	  */
	public BigDecimal getIsDue();

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

    /** Column name PickupTruck */
    public static final String COLUMNNAME_PickupTruck = "PickupTruck";

	/** Set PickupTruck	  */
	public void setPickupTruck (boolean PickupTruck);

	/** Get PickupTruck	  */
	public boolean isPickupTruck();

    /** Column name Requirement */
    public static final String COLUMNNAME_Requirement = "Requirement";

	/** Set Requirement	  */
	public void setRequirement (String Requirement);

	/** Get Requirement	  */
	public String getRequirement();

    /** Column name Semitrailer */
    public static final String COLUMNNAME_Semitrailer = "Semitrailer";

	/** Set Semitrailer	  */
	public void setSemitrailer (boolean Semitrailer);

	/** Get Semitrailer	  */
	public boolean isSemitrailer();

    /** Column name TankTruck */
    public static final String COLUMNNAME_TankTruck = "TankTruck";

	/** Set TankTruck	  */
	public void setTankTruck (boolean TankTruck);

	/** Get TankTruck	  */
	public boolean isTankTruck();

    /** Column name Tractors */
    public static final String COLUMNNAME_Tractors = "Tractors";

	/** Set Tractors	  */
	public void setTractors (boolean Tractors);

	/** Get Tractors	  */
	public boolean isTractors();

    /** Column name Trailer */
    public static final String COLUMNNAME_Trailer = "Trailer";

	/** Set Trailer	  */
	public void setTrailer (boolean Trailer);

	/** Get Trailer	  */
	public boolean isTrailer();

    /** Column name Type */
    public static final String COLUMNNAME_Type = "Type";

	/** Set Type.
	  * Type of Validation (SQL, Java Script, Java Language)
	  */
	public void setType (String Type);

	/** Get Type.
	  * Type of Validation (SQL, Java Script, Java Language)
	  */
	public String getType();

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
