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
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for MP_Maintain
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_MP_Maintain 
{

    /** TableName=MP_Maintain */
    public static final String Table_Name = "MP_Maintain";

    /** AD_Table_ID=1000027 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name A_Asset_Group_ID */
    public static final String COLUMNNAME_A_Asset_Group_ID = "A_Asset_Group_ID";

	/** Set Asset Group.
	  * Group of Assets
	  */
	public void setA_Asset_Group_ID (int A_Asset_Group_ID);

	/** Get Asset Group.
	  * Group of Assets
	  */
	public int getA_Asset_Group_ID();

	public I_A_Asset_Group getA_Asset_Group() throws RuntimeException;

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

    /** Column name CopyFrom */
    public static final String COLUMNNAME_CopyFrom = "CopyFrom";

	/** Set Copy From.
	  * Copy From Record
	  */
	public void setCopyFrom (String CopyFrom);

	/** Get Copy From.
	  * Copy From Record
	  */
	public String getCopyFrom();

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

    /** Column name currentmp */
    public static final String COLUMNNAME_currentmp = "currentmp";

	/** Set currentmp	  */
	public void setcurrentmp (BigDecimal currentmp);

	/** Get currentmp	  */
	public BigDecimal getcurrentmp();

    /** Column name DateLastOT */
    public static final String COLUMNNAME_DateLastOT = "DateLastOT";

	/** Set DateLastOT	  */
	public void setDateLastOT (Timestamp DateLastOT);

	/** Get DateLastOT	  */
	public Timestamp getDateLastOT();

    /** Column name DateLastRun */
    public static final String COLUMNNAME_DateLastRun = "DateLastRun";

	/** Set Date last run.
	  * Date the process was last run.
	  */
	public void setDateLastRun (Timestamp DateLastRun);

	/** Get Date last run.
	  * Date the process was last run.
	  */
	public Timestamp getDateLastRun();

    /** Column name datelastrunmp */
    public static final String COLUMNNAME_datelastrunmp = "datelastrunmp";

	/** Set datelastrunmp	  */
	public void setdatelastrunmp (Timestamp datelastrunmp);

	/** Get datelastrunmp	  */
	public Timestamp getdatelastrunmp();

    /** Column name DateNextRun */
    public static final String COLUMNNAME_DateNextRun = "DateNextRun";

	/** Set Date next run.
	  * Date the process will run next
	  */
	public void setDateNextRun (Timestamp DateNextRun);

	/** Get Date next run.
	  * Date the process will run next
	  */
	public Timestamp getDateNextRun();

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

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name Interval */
    public static final String COLUMNNAME_Interval = "Interval";

	/** Set Interval	  */
	public void setInterval (BigDecimal Interval);

	/** Get Interval	  */
	public BigDecimal getInterval();

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

    /** Column name IsChild */
    public static final String COLUMNNAME_IsChild = "IsChild";

	/** Set IsChild	  */
	public void setIsChild (boolean IsChild);

	/** Get IsChild	  */
	public boolean isChild();

    /** Column name lastmp */
    public static final String COLUMNNAME_lastmp = "lastmp";

	/** Set lastmp	  */
	public void setlastmp (BigDecimal lastmp);

	/** Get lastmp	  */
	public BigDecimal getlastmp();

    /** Column name lastread */
    public static final String COLUMNNAME_lastread = "lastread";

	/** Set lastread	  */
	public void setlastread (BigDecimal lastread);

	/** Get lastread	  */
	public BigDecimal getlastread();

    /** Column name MP_JobStandar_ID */
    public static final String COLUMNNAME_MP_JobStandar_ID = "MP_JobStandar_ID";

	/** Set MP_JobStandard	  */
	public void setMP_JobStandar_ID (int MP_JobStandar_ID);

	/** Get MP_JobStandard	  */
	public int getMP_JobStandar_ID();

	public I_MP_JobStandar getMP_JobStandar() throws RuntimeException;

    /** Column name MP_Maintain_ID */
    public static final String COLUMNNAME_MP_Maintain_ID = "MP_Maintain_ID";

	/** Set MP_Maintain	  */
	public void setMP_Maintain_ID (int MP_Maintain_ID);

	/** Get MP_Maintain	  */
	public int getMP_Maintain_ID();

    /** Column name MP_MaintainParent_ID */
    public static final String COLUMNNAME_MP_MaintainParent_ID = "MP_MaintainParent_ID";

	/** Set MP_MaintainParent_ID	  */
	public void setMP_MaintainParent_ID (int MP_MaintainParent_ID);

	/** Get MP_MaintainParent_ID	  */
	public int getMP_MaintainParent_ID();

	public I_MP_Maintain getMP_MaintainParent() throws RuntimeException;

    /** Column name MP_Meter_ID */
    public static final String COLUMNNAME_MP_Meter_ID = "MP_Meter_ID";

	/** Set MP_Meter	  */
	public void setMP_Meter_ID (int MP_Meter_ID);

	/** Get MP_Meter	  */
	public int getMP_Meter_ID();

	public I_MP_Meter getMP_Meter() throws RuntimeException;

    /** Column name nextmp */
    public static final String COLUMNNAME_nextmp = "nextmp";

	/** Set nextmp	  */
	public void setnextmp (BigDecimal nextmp);

	/** Get nextmp	  */
	public BigDecimal getnextmp();

    /** Column name PriorityRule */
    public static final String COLUMNNAME_PriorityRule = "PriorityRule";

	/** Set Priority.
	  * Priority of a document
	  */
	public void setPriorityRule (boolean PriorityRule);

	/** Get Priority.
	  * Priority of a document
	  */
	public boolean isPriorityRule();

    /** Column name ProgrammingType */
    public static final String COLUMNNAME_ProgrammingType = "ProgrammingType";

	/** Set ProgrammingType	  */
	public void setProgrammingType (String ProgrammingType);

	/** Get ProgrammingType	  */
	public String getProgrammingType();

    /** Column name promuse */
    public static final String COLUMNNAME_promuse = "promuse";

	/** Set promuse	  */
	public void setpromuse (BigDecimal promuse);

	/** Get promuse	  */
	public BigDecimal getpromuse();

    /** Column name Range */
    public static final String COLUMNNAME_Range = "Range";

	/** Set Range	  */
	public void setRange (BigDecimal Range);

	/** Get Range	  */
	public BigDecimal getRange();

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
