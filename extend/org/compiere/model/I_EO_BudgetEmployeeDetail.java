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

/** Generated Interface for EO_BudgetEmployeeDetail
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_EO_BudgetEmployeeDetail 
{

    /** TableName=EO_BudgetEmployeeDetail */
    public static final String Table_Name = "EO_BudgetEmployeeDetail";

    /** AD_Table_ID=1000073 */
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

    /** Column name EO_BudgetEmployee_ID */
    public static final String COLUMNNAME_EO_BudgetEmployee_ID = "EO_BudgetEmployee_ID";

	/** Set EO_BudgetEmployee	  */
	public void setEO_BudgetEmployee_ID (int EO_BudgetEmployee_ID);

	/** Get EO_BudgetEmployee	  */
	public int getEO_BudgetEmployee_ID();

	public I_EO_BudgetEmployee getEO_BudgetEmployee() throws RuntimeException;

    /** Column name EO_BudgetEmployeeDetail_ID */
    public static final String COLUMNNAME_EO_BudgetEmployeeDetail_ID = "EO_BudgetEmployeeDetail_ID";

	/** Set EO_BudgetEmployeeDetail	  */
	public void setEO_BudgetEmployeeDetail_ID (int EO_BudgetEmployeeDetail_ID);

	/** Get EO_BudgetEmployeeDetail	  */
	public int getEO_BudgetEmployeeDetail_ID();

    /** Column name Grade */
    public static final String COLUMNNAME_Grade = "Grade";

	/** Set Grade	  */
	public void setGrade (String Grade);

	/** Get Grade	  */
	public String getGrade();

    /** Column name HoursAvailable */
    public static final String COLUMNNAME_HoursAvailable = "HoursAvailable";

	/** Set HoursAvailable	  */
	public void setHoursAvailable (int HoursAvailable);

	/** Get HoursAvailable	  */
	public int getHoursAvailable();

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

    /** Column name QtyHours1 */
    public static final String COLUMNNAME_QtyHours1 = "QtyHours1";

	/** Set QtyHours1	  */
	public void setQtyHours1 (int QtyHours1);

	/** Get QtyHours1	  */
	public int getQtyHours1();

    /** Column name QtyHours10 */
    public static final String COLUMNNAME_QtyHours10 = "QtyHours10";

	/** Set QtyHours10	  */
	public void setQtyHours10 (int QtyHours10);

	/** Get QtyHours10	  */
	public int getQtyHours10();

    /** Column name QtyHours11 */
    public static final String COLUMNNAME_QtyHours11 = "QtyHours11";

	/** Set QtyHours11	  */
	public void setQtyHours11 (int QtyHours11);

	/** Get QtyHours11	  */
	public int getQtyHours11();

    /** Column name QtyHours12 */
    public static final String COLUMNNAME_QtyHours12 = "QtyHours12";

	/** Set QtyHours12	  */
	public void setQtyHours12 (int QtyHours12);

	/** Get QtyHours12	  */
	public int getQtyHours12();

    /** Column name QtyHours2 */
    public static final String COLUMNNAME_QtyHours2 = "QtyHours2";

	/** Set QtyHours2	  */
	public void setQtyHours2 (int QtyHours2);

	/** Get QtyHours2	  */
	public int getQtyHours2();

    /** Column name QtyHours3 */
    public static final String COLUMNNAME_QtyHours3 = "QtyHours3";

	/** Set QtyHours3	  */
	public void setQtyHours3 (int QtyHours3);

	/** Get QtyHours3	  */
	public int getQtyHours3();

    /** Column name QtyHours4 */
    public static final String COLUMNNAME_QtyHours4 = "QtyHours4";

	/** Set QtyHours4	  */
	public void setQtyHours4 (int QtyHours4);

	/** Get QtyHours4	  */
	public int getQtyHours4();

    /** Column name QtyHours5 */
    public static final String COLUMNNAME_QtyHours5 = "QtyHours5";

	/** Set QtyHours5	  */
	public void setQtyHours5 (int QtyHours5);

	/** Get QtyHours5	  */
	public int getQtyHours5();

    /** Column name QtyHours6 */
    public static final String COLUMNNAME_QtyHours6 = "QtyHours6";

	/** Set QtyHours6	  */
	public void setQtyHours6 (int QtyHours6);

	/** Get QtyHours6	  */
	public int getQtyHours6();

    /** Column name QtyHours7 */
    public static final String COLUMNNAME_QtyHours7 = "QtyHours7";

	/** Set QtyHours7	  */
	public void setQtyHours7 (int QtyHours7);

	/** Get QtyHours7	  */
	public int getQtyHours7();

    /** Column name QtyHours8 */
    public static final String COLUMNNAME_QtyHours8 = "QtyHours8";

	/** Set QtyHours8	  */
	public void setQtyHours8 (int QtyHours8);

	/** Get QtyHours8	  */
	public int getQtyHours8();

    /** Column name QtyHours9 */
    public static final String COLUMNNAME_QtyHours9 = "QtyHours9";

	/** Set QtyHours9	  */
	public void setQtyHours9 (int QtyHours9);

	/** Get QtyHours9	  */
	public int getQtyHours9();

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
