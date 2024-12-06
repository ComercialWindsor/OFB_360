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

/** Generated Interface for A_AssetCriticalDate
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_A_AssetCriticalDate 
{

    /** TableName=A_AssetCriticalDate */
    public static final String Table_Name = "A_AssetCriticalDate";

    /** AD_Table_ID=1000088 */
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

    /** Column name A_AssetCriticalDate_ID */
    public static final String COLUMNNAME_A_AssetCriticalDate_ID = "A_AssetCriticalDate_ID";

	/** Set A_AssetCriticalDate	  */
	public void setA_AssetCriticalDate_ID (int A_AssetCriticalDate_ID);

	/** Get A_AssetCriticalDate	  */
	public int getA_AssetCriticalDate_ID();

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

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

	/** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID);

	/** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID();

	public I_AD_User getAD_User() throws RuntimeException;

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

    /** Column name C_CriticalDateConcept_ID */
    public static final String COLUMNNAME_C_CriticalDateConcept_ID = "C_CriticalDateConcept_ID";

	/** Set C_CriticalDateConcept	  */
	public void setC_CriticalDateConcept_ID (int C_CriticalDateConcept_ID);

	/** Get C_CriticalDateConcept	  */
	public int getC_CriticalDateConcept_ID();

	public I_C_CriticalDateConcept getC_CriticalDateConcept() throws RuntimeException;

    /** Column name Concept */
    public static final String COLUMNNAME_Concept = "Concept";

	/** Set Concept	  */
	public void setConcept (String Concept);

	/** Get Concept	  */
	public String getConcept();

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

    /** Column name descrip2 */
    public static final String COLUMNNAME_descrip2 = "descrip2";

	/** Set descrip2	  */
	public void setdescrip2 (String descrip2);

	/** Get descrip2	  */
	public String getdescrip2();

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

    /** Column name DueDate */
    public static final String COLUMNNAME_DueDate = "DueDate";

	/** Set Due Date.
	  * Date when the payment is due
	  */
	public void setDueDate (Timestamp DueDate);

	/** Get Due Date.
	  * Date when the payment is due
	  */
	public Timestamp getDueDate();

    /** Column name FirstDateReminder */
    public static final String COLUMNNAME_FirstDateReminder = "FirstDateReminder";

	/** Set FirstDateReminder	  */
	public void setFirstDateReminder (Timestamp FirstDateReminder);

	/** Get FirstDateReminder	  */
	public Timestamp getFirstDateReminder();

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

    /** Column name SecondDateReminder */
    public static final String COLUMNNAME_SecondDateReminder = "SecondDateReminder";

	/** Set SecondDateReminder	  */
	public void setSecondDateReminder (Timestamp SecondDateReminder);

	/** Get SecondDateReminder	  */
	public Timestamp getSecondDateReminder();

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
