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

/** Generated Interface for RH_PatternDriverEvaluation
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_RH_PatternDriverEvaluation 
{

    /** TableName=RH_PatternDriverEvaluation */
    public static final String Table_Name = "RH_PatternDriverEvaluation";

    /** AD_Table_ID=1000164 */
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

    /** Column name Percentage */
    public static final String COLUMNNAME_Percentage = "Percentage";

	/** Set Percentage.
	  * Percent of the entire amount
	  */
	public void setPercentage (BigDecimal Percentage);

	/** Get Percentage.
	  * Percent of the entire amount
	  */
	public BigDecimal getPercentage();

    /** Column name Percentage10 */
    public static final String COLUMNNAME_Percentage10 = "Percentage10";

	/** Set Percentage10	  */
	public void setPercentage10 (BigDecimal Percentage10);

	/** Get Percentage10	  */
	public BigDecimal getPercentage10();

    /** Column name Percentage2 */
    public static final String COLUMNNAME_Percentage2 = "Percentage2";

	/** Set Percentage2	  */
	public void setPercentage2 (BigDecimal Percentage2);

	/** Get Percentage2	  */
	public BigDecimal getPercentage2();

    /** Column name Percentage3 */
    public static final String COLUMNNAME_Percentage3 = "Percentage3";

	/** Set Percentage3	  */
	public void setPercentage3 (BigDecimal Percentage3);

	/** Get Percentage3	  */
	public BigDecimal getPercentage3();

    /** Column name Percentage4 */
    public static final String COLUMNNAME_Percentage4 = "Percentage4";

	/** Set Percentage4	  */
	public void setPercentage4 (BigDecimal Percentage4);

	/** Get Percentage4	  */
	public BigDecimal getPercentage4();

    /** Column name Percentage5 */
    public static final String COLUMNNAME_Percentage5 = "Percentage5";

	/** Set Percentage5	  */
	public void setPercentage5 (BigDecimal Percentage5);

	/** Get Percentage5	  */
	public BigDecimal getPercentage5();

    /** Column name Percentage6 */
    public static final String COLUMNNAME_Percentage6 = "Percentage6";

	/** Set Percentage6	  */
	public void setPercentage6 (BigDecimal Percentage6);

	/** Get Percentage6	  */
	public BigDecimal getPercentage6();

    /** Column name Percentage7 */
    public static final String COLUMNNAME_Percentage7 = "Percentage7";

	/** Set Percentage7	  */
	public void setPercentage7 (BigDecimal Percentage7);

	/** Get Percentage7	  */
	public BigDecimal getPercentage7();

    /** Column name Percentage8 */
    public static final String COLUMNNAME_Percentage8 = "Percentage8";

	/** Set Percentage8	  */
	public void setPercentage8 (BigDecimal Percentage8);

	/** Get Percentage8	  */
	public BigDecimal getPercentage8();

    /** Column name Percentage9 */
    public static final String COLUMNNAME_Percentage9 = "Percentage9";

	/** Set Percentage9	  */
	public void setPercentage9 (BigDecimal Percentage9);

	/** Get Percentage9	  */
	public BigDecimal getPercentage9();

    /** Column name Question1 */
    public static final String COLUMNNAME_Question1 = "Question1";

	/** Set Question1	  */
	public void setQuestion1 (String Question1);

	/** Get Question1	  */
	public String getQuestion1();

    /** Column name Question10 */
    public static final String COLUMNNAME_Question10 = "Question10";

	/** Set Question10	  */
	public void setQuestion10 (String Question10);

	/** Get Question10	  */
	public String getQuestion10();

    /** Column name Question2 */
    public static final String COLUMNNAME_Question2 = "Question2";

	/** Set Question2	  */
	public void setQuestion2 (String Question2);

	/** Get Question2	  */
	public String getQuestion2();

    /** Column name Question3 */
    public static final String COLUMNNAME_Question3 = "Question3";

	/** Set Question3	  */
	public void setQuestion3 (String Question3);

	/** Get Question3	  */
	public String getQuestion3();

    /** Column name Question4 */
    public static final String COLUMNNAME_Question4 = "Question4";

	/** Set Question4	  */
	public void setQuestion4 (String Question4);

	/** Get Question4	  */
	public String getQuestion4();

    /** Column name Question5 */
    public static final String COLUMNNAME_Question5 = "Question5";

	/** Set Question5	  */
	public void setQuestion5 (String Question5);

	/** Get Question5	  */
	public String getQuestion5();

    /** Column name Question6 */
    public static final String COLUMNNAME_Question6 = "Question6";

	/** Set Question6	  */
	public void setQuestion6 (String Question6);

	/** Get Question6	  */
	public String getQuestion6();

    /** Column name Question7 */
    public static final String COLUMNNAME_Question7 = "Question7";

	/** Set Question7	  */
	public void setQuestion7 (String Question7);

	/** Get Question7	  */
	public String getQuestion7();

    /** Column name Question8 */
    public static final String COLUMNNAME_Question8 = "Question8";

	/** Set Question8	  */
	public void setQuestion8 (String Question8);

	/** Get Question8	  */
	public String getQuestion8();

    /** Column name Question9 */
    public static final String COLUMNNAME_Question9 = "Question9";

	/** Set Question9	  */
	public void setQuestion9 (String Question9);

	/** Get Question9	  */
	public String getQuestion9();

    /** Column name RH_PatternDriverEvaluation_ID */
    public static final String COLUMNNAME_RH_PatternDriverEvaluation_ID = "RH_PatternDriverEvaluation_ID";

	/** Set RH_PatternDriverEvaluation	  */
	public void setRH_PatternDriverEvaluation_ID (int RH_PatternDriverEvaluation_ID);

	/** Get RH_PatternDriverEvaluation	  */
	public int getRH_PatternDriverEvaluation_ID();

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
