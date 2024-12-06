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

/** Generated Interface for RH_EvaluationLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_RH_EvaluationLine 
{

    /** TableName=RH_EvaluationLine */
    public static final String Table_Name = "RH_EvaluationLine";

    /** AD_Table_ID=1000200 */
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

    /** Column name Answer1 */
    public static final String COLUMNNAME_Answer1 = "Answer1";

	/** Set Answer1	  */
	public void setAnswer1 (String Answer1);

	/** Get Answer1	  */
	public String getAnswer1();

    /** Column name Answer10 */
    public static final String COLUMNNAME_Answer10 = "Answer10";

	/** Set Answer10	  */
	public void setAnswer10 (String Answer10);

	/** Get Answer10	  */
	public String getAnswer10();

    /** Column name Answer2 */
    public static final String COLUMNNAME_Answer2 = "Answer2";

	/** Set Answer2	  */
	public void setAnswer2 (String Answer2);

	/** Get Answer2	  */
	public String getAnswer2();

    /** Column name Answer3 */
    public static final String COLUMNNAME_Answer3 = "Answer3";

	/** Set Answer3	  */
	public void setAnswer3 (String Answer3);

	/** Get Answer3	  */
	public String getAnswer3();

    /** Column name Answer4 */
    public static final String COLUMNNAME_Answer4 = "Answer4";

	/** Set Answer4	  */
	public void setAnswer4 (String Answer4);

	/** Get Answer4	  */
	public String getAnswer4();

    /** Column name Answer5 */
    public static final String COLUMNNAME_Answer5 = "Answer5";

	/** Set Answer5	  */
	public void setAnswer5 (String Answer5);

	/** Get Answer5	  */
	public String getAnswer5();

    /** Column name Answer6 */
    public static final String COLUMNNAME_Answer6 = "Answer6";

	/** Set Answer6	  */
	public void setAnswer6 (String Answer6);

	/** Get Answer6	  */
	public String getAnswer6();

    /** Column name Answer7 */
    public static final String COLUMNNAME_Answer7 = "Answer7";

	/** Set Answer7	  */
	public void setAnswer7 (String Answer7);

	/** Get Answer7	  */
	public String getAnswer7();

    /** Column name Answer8 */
    public static final String COLUMNNAME_Answer8 = "Answer8";

	/** Set Answer8	  */
	public void setAnswer8 (String Answer8);

	/** Get Answer8	  */
	public String getAnswer8();

    /** Column name Answer9 */
    public static final String COLUMNNAME_Answer9 = "Answer9";

	/** Set Answer9	  */
	public void setAnswer9 (String Answer9);

	/** Get Answer9	  */
	public String getAnswer9();

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

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

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

    /** Column name question1_drive */
    public static final String COLUMNNAME_question1_drive = "question1_drive";

	/** Set question1_drive	  */
	public void setquestion1_drive (String question1_drive);

	/** Get question1_drive	  */
	public String getquestion1_drive();

    /** Column name question10_drive */
    public static final String COLUMNNAME_question10_drive = "question10_drive";

	/** Set question10_drive	  */
	public void setquestion10_drive (String question10_drive);

	/** Get question10_drive	  */
	public String getquestion10_drive();

    /** Column name question2_drive */
    public static final String COLUMNNAME_question2_drive = "question2_drive";

	/** Set question2_drive	  */
	public void setquestion2_drive (String question2_drive);

	/** Get question2_drive	  */
	public String getquestion2_drive();

    /** Column name question3_drive */
    public static final String COLUMNNAME_question3_drive = "question3_drive";

	/** Set question3_drive	  */
	public void setquestion3_drive (String question3_drive);

	/** Get question3_drive	  */
	public String getquestion3_drive();

    /** Column name question4_drive */
    public static final String COLUMNNAME_question4_drive = "question4_drive";

	/** Set question4_drive	  */
	public void setquestion4_drive (String question4_drive);

	/** Get question4_drive	  */
	public String getquestion4_drive();

    /** Column name question5_drive */
    public static final String COLUMNNAME_question5_drive = "question5_drive";

	/** Set question5_drive	  */
	public void setquestion5_drive (String question5_drive);

	/** Get question5_drive	  */
	public String getquestion5_drive();

    /** Column name question6_drive */
    public static final String COLUMNNAME_question6_drive = "question6_drive";

	/** Set question6_drive	  */
	public void setquestion6_drive (String question6_drive);

	/** Get question6_drive	  */
	public String getquestion6_drive();

    /** Column name question7_drive */
    public static final String COLUMNNAME_question7_drive = "question7_drive";

	/** Set question7_drive	  */
	public void setquestion7_drive (String question7_drive);

	/** Get question7_drive	  */
	public String getquestion7_drive();

    /** Column name question8_drive */
    public static final String COLUMNNAME_question8_drive = "question8_drive";

	/** Set question8_drive	  */
	public void setquestion8_drive (String question8_drive);

	/** Get question8_drive	  */
	public String getquestion8_drive();

    /** Column name question9_drive */
    public static final String COLUMNNAME_question9_drive = "question9_drive";

	/** Set question9_drive	  */
	public void setquestion9_drive (String question9_drive);

	/** Get question9_drive	  */
	public String getquestion9_drive();

    /** Column name Result */
    public static final String COLUMNNAME_Result = "Result";

	/** Set Result.
	  * Result of the action taken
	  */
	public void setResult (BigDecimal Result);

	/** Get Result.
	  * Result of the action taken
	  */
	public BigDecimal getResult();

    /** Column name RH_EvaluationHeader_ID */
    public static final String COLUMNNAME_RH_EvaluationHeader_ID = "RH_EvaluationHeader_ID";

	/** Set RH_EvaluationHeader	  */
	public void setRH_EvaluationHeader_ID (int RH_EvaluationHeader_ID);

	/** Get RH_EvaluationHeader	  */
	public int getRH_EvaluationHeader_ID();

	public I_RH_EvaluationHeader getRH_EvaluationHeader() throws RuntimeException;

    /** Column name RH_EvaluationLine_ID */
    public static final String COLUMNNAME_RH_EvaluationLine_ID = "RH_EvaluationLine_ID";

	/** Set RH_EvaluationLine	  */
	public void setRH_EvaluationLine_ID (int RH_EvaluationLine_ID);

	/** Get RH_EvaluationLine	  */
	public int getRH_EvaluationLine_ID();

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
