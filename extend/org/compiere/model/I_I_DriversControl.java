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

/** Generated Interface for I_DriversControl
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_I_DriversControl 
{

    /** TableName=I_DriversControl */
    public static final String Table_Name = "I_DriversControl";

    /** AD_Table_ID=1000137 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name A_Asset_ID */
    public static final String COLUMNNAME_A_Asset_ID = "A_Asset_ID";

	/** Set Asset.
	  * Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID);

	/** Get Asset.
	  * Asset used internally or by customers
	  */
	public int getA_Asset_ID();

	public I_A_Asset getA_Asset() throws RuntimeException;

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

    /** Column name Address1 */
    public static final String COLUMNNAME_Address1 = "Address1";

	/** Set Address 1.
	  * Address line 1 for this location
	  */
	public void setAddress1 (String Address1);

	/** Get Address 1.
	  * Address line 1 for this location
	  */
	public String getAddress1();

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

    /** Column name C_ProjectOFB_ID */
    public static final String COLUMNNAME_C_ProjectOFB_ID = "C_ProjectOFB_ID";

	/** Set C_ProjectOFB_ID	  */
	public void setC_ProjectOFB_ID (int C_ProjectOFB_ID);

	/** Get C_ProjectOFB_ID	  */
	public int getC_ProjectOFB_ID();

	public I_C_ProjectOFB getC_ProjectOFB() throws RuntimeException;

    /** Column name CategoryType */
    public static final String COLUMNNAME_CategoryType = "CategoryType";

	/** Set Category Type.
	  * Source of the Journal with this category
	  */
	public void setCategoryType (String CategoryType);

	/** Get Category Type.
	  * Source of the Journal with this category
	  */
	public String getCategoryType();

    /** Column name CategoryType2 */
    public static final String COLUMNNAME_CategoryType2 = "CategoryType2";

	/** Set Category Type 2.
	  * Source of the Journal with this category
	  */
	public void setCategoryType2 (String CategoryType2);

	/** Get Category Type 2.
	  * Source of the Journal with this category
	  */
	public String getCategoryType2();

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

    /** Column name Date1 */
    public static final String COLUMNNAME_Date1 = "Date1";

	/** Set Date.
	  * Date when business is not conducted
	  */
	public void setDate1 (Timestamp Date1);

	/** Get Date.
	  * Date when business is not conducted
	  */
	public Timestamp getDate1();

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

    /** Column name EndTime */
    public static final String COLUMNNAME_EndTime = "EndTime";

	/** Set End Time.
	  * End of the time span
	  */
	public void setEndTime (Timestamp EndTime);

	/** Get End Time.
	  * End of the time span
	  */
	public Timestamp getEndTime();

    /** Column name I_BPartnerName */
    public static final String COLUMNNAME_I_BPartnerName = "I_BPartnerName";

	/** Set I_BPartnerName	  */
	public void setI_BPartnerName (String I_BPartnerName);

	/** Get I_BPartnerName	  */
	public String getI_BPartnerName();

    /** Column name I_DriversControl_ID */
    public static final String COLUMNNAME_I_DriversControl_ID = "I_DriversControl_ID";

	/** Set I_DriversControl	  */
	public void setI_DriversControl_ID (int I_DriversControl_ID);

	/** Get I_DriversControl	  */
	public int getI_DriversControl_ID();

    /** Column name I_ErrorMsg */
    public static final String COLUMNNAME_I_ErrorMsg = "I_ErrorMsg";

	/** Set Import Error Message.
	  * Messages generated from import process
	  */
	public void setI_ErrorMsg (String I_ErrorMsg);

	/** Get Import Error Message.
	  * Messages generated from import process
	  */
	public String getI_ErrorMsg();

    /** Column name I_FlotaName */
    public static final String COLUMNNAME_I_FlotaName = "I_FlotaName";

	/** Set I_FlotaName	  */
	public void setI_FlotaName (String I_FlotaName);

	/** Get I_FlotaName	  */
	public String getI_FlotaName();

    /** Column name I_IsImported */
    public static final String COLUMNNAME_I_IsImported = "I_IsImported";

	/** Set Imported.
	  * Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported);

	/** Get Imported.
	  * Has this import been processed
	  */
	public boolean isI_IsImported();

    /** Column name I_MonthYear */
    public static final String COLUMNNAME_I_MonthYear = "I_MonthYear";

	/** Set I_MonthYear	  */
	public void setI_MonthYear (String I_MonthYear);

	/** Get I_MonthYear	  */
	public String getI_MonthYear();

    /** Column name I_Supervisor */
    public static final String COLUMNNAME_I_Supervisor = "I_Supervisor";

	/** Set I_Supervisor	  */
	public void setI_Supervisor (String I_Supervisor);

	/** Get I_Supervisor	  */
	public String getI_Supervisor();

    /** Column name I_TractoValue */
    public static final String COLUMNNAME_I_TractoValue = "I_TractoValue";

	/** Set I_TractoValue	  */
	public void setI_TractoValue (String I_TractoValue);

	/** Get I_TractoValue	  */
	public String getI_TractoValue();

    /** Column name Interval */
    public static final String COLUMNNAME_Interval = "Interval";

	/** Set Interval	  */
	public void setInterval (String Interval);

	/** Get Interval	  */
	public String getInterval();

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

    /** Column name Latitude */
    public static final String COLUMNNAME_Latitude = "Latitude";

	/** Set Latitude	  */
	public void setLatitude (BigDecimal Latitude);

	/** Get Latitude	  */
	public BigDecimal getLatitude();

    /** Column name Longitude */
    public static final String COLUMNNAME_Longitude = "Longitude";

	/** Set Longitude	  */
	public void setLongitude (BigDecimal Longitude);

	/** Get Longitude	  */
	public BigDecimal getLongitude();

    /** Column name MaxSpeed */
    public static final String COLUMNNAME_MaxSpeed = "MaxSpeed";

	/** Set MaxSpeed	  */
	public void setMaxSpeed (int MaxSpeed);

	/** Get MaxSpeed	  */
	public int getMaxSpeed();

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

    /** Column name SpeedCategory */
    public static final String COLUMNNAME_SpeedCategory = "SpeedCategory";

	/** Set SpeedCategory	  */
	public void setSpeedCategory (int SpeedCategory);

	/** Get SpeedCategory	  */
	public int getSpeedCategory();

    /** Column name StartTime */
    public static final String COLUMNNAME_StartTime = "StartTime";

	/** Set Start Time.
	  * Time started
	  */
	public void setStartTime (Timestamp StartTime);

	/** Get Start Time.
	  * Time started
	  */
	public Timestamp getStartTime();

    /** Column name Status */
    public static final String COLUMNNAME_Status = "Status";

	/** Set Status.
	  * Status of the currently running check
	  */
	public void setStatus (String Status);

	/** Get Status.
	  * Status of the currently running check
	  */
	public String getStatus();

    /** Column name Supervisor_ID */
    public static final String COLUMNNAME_Supervisor_ID = "Supervisor_ID";

	/** Set Supervisor.
	  * Supervisor for this user/organization - used for escalation and approval
	  */
	public void setSupervisor_ID (int Supervisor_ID);

	/** Get Supervisor.
	  * Supervisor for this user/organization - used for escalation and approval
	  */
	public int getSupervisor_ID();

	public I_AD_User getSupervisor() throws RuntimeException;

    /** Column name TypeMatrix */
    public static final String COLUMNNAME_TypeMatrix = "TypeMatrix";

	/** Set TypeMatrix	  */
	public void setTypeMatrix (String TypeMatrix);

	/** Get TypeMatrix	  */
	public String getTypeMatrix();

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

    /** Column name WaitTime */
    public static final String COLUMNNAME_WaitTime = "WaitTime";

	/** Set Wait Time.
	  * Time in minutes to wait (sleep)
	  */
	public void setWaitTime (Timestamp WaitTime);

	/** Get Wait Time.
	  * Time in minutes to wait (sleep)
	  */
	public Timestamp getWaitTime();
}
