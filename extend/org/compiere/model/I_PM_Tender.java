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

/** Generated Interface for PM_Tender
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_PM_Tender 
{

    /** TableName=PM_Tender */
    public static final String Table_Name = "PM_Tender";

    /** AD_Table_ID=1000041 */
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

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

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

    /** Column name Milestone1 */
    public static final String COLUMNNAME_Milestone1 = "Milestone1";

	/** Set Milestone1	  */
	public void setMilestone1 (Timestamp Milestone1);

	/** Get Milestone1	  */
	public Timestamp getMilestone1();

    /** Column name Milestone2 */
    public static final String COLUMNNAME_Milestone2 = "Milestone2";

	/** Set Milestone2	  */
	public void setMilestone2 (Timestamp Milestone2);

	/** Get Milestone2	  */
	public Timestamp getMilestone2();

    /** Column name Milestone3 */
    public static final String COLUMNNAME_Milestone3 = "Milestone3";

	/** Set Milestone3	  */
	public void setMilestone3 (Timestamp Milestone3);

	/** Get Milestone3	  */
	public Timestamp getMilestone3();

    /** Column name Milestone4 */
    public static final String COLUMNNAME_Milestone4 = "Milestone4";

	/** Set Milestone4	  */
	public void setMilestone4 (Timestamp Milestone4);

	/** Get Milestone4	  */
	public Timestamp getMilestone4();

    /** Column name Milestone5 */
    public static final String COLUMNNAME_Milestone5 = "Milestone5";

	/** Set Milestone5	  */
	public void setMilestone5 (Timestamp Milestone5);

	/** Get Milestone5	  */
	public Timestamp getMilestone5();

    /** Column name Milestone6 */
    public static final String COLUMNNAME_Milestone6 = "Milestone6";

	/** Set Milestone6	  */
	public void setMilestone6 (Timestamp Milestone6);

	/** Get Milestone6	  */
	public Timestamp getMilestone6();

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

    /** Column name PM_Tender_ID */
    public static final String COLUMNNAME_PM_Tender_ID = "PM_Tender_ID";

	/** Set PM_Tender	  */
	public void setPM_Tender_ID (int PM_Tender_ID);

	/** Get PM_Tender	  */
	public int getPM_Tender_ID();

    /** Column name PM_TenderType */
    public static final String COLUMNNAME_PM_TenderType = "PM_TenderType";

	/** Set PM_TenderType	  */
	public void setPM_TenderType (String PM_TenderType);

	/** Get PM_TenderType	  */
	public String getPM_TenderType();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

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
