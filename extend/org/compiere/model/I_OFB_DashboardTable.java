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

/** Generated Interface for OFB_DashboardTable
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_OFB_DashboardTable 
{

    /** TableName=OFB_DashboardTable */
    public static final String Table_Name = "OFB_DashboardTable";

    /** AD_Table_ID=1000060 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 2 - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(2);

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

    /** Column name Line */
    public static final String COLUMNNAME_Line = "Line";

	/** Set Line No.
	  * Unique line for this document
	  */
	public void setLine (int Line);

	/** Get Line No.
	  * Unique line for this document
	  */
	public int getLine();

    /** Column name MeasureDisplay */
    public static final String COLUMNNAME_MeasureDisplay = "MeasureDisplay";

	/** Set Measure Display.
	  * Measure Scope initially displayed
	  */
	public void setMeasureDisplay (String MeasureDisplay);

	/** Get Measure Display.
	  * Measure Scope initially displayed
	  */
	public String getMeasureDisplay();

    /** Column name NameRow1 */
    public static final String COLUMNNAME_NameRow1 = "NameRow1";

	/** Set NameRow1	  */
	public void setNameRow1 (String NameRow1);

	/** Get NameRow1	  */
	public String getNameRow1();

    /** Column name NameRow2 */
    public static final String COLUMNNAME_NameRow2 = "NameRow2";

	/** Set NameRow2	  */
	public void setNameRow2 (String NameRow2);

	/** Get NameRow2	  */
	public String getNameRow2();

    /** Column name OFB_DashboardTable_ID */
    public static final String COLUMNNAME_OFB_DashboardTable_ID = "OFB_DashboardTable_ID";

	/** Set OFB_DashboardTable	  */
	public void setOFB_DashboardTable_ID (int OFB_DashboardTable_ID);

	/** Get OFB_DashboardTable	  */
	public int getOFB_DashboardTable_ID();

    /** Column name TableName */
    public static final String COLUMNNAME_TableName = "TableName";

	/** Set DB Table Name.
	  * Name of the table in the database
	  */
	public void setTableName (String TableName);

	/** Get DB Table Name.
	  * Name of the table in the database
	  */
	public String getTableName();

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
