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
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for RH_EvaluationLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_EvaluationLine extends PO implements I_RH_EvaluationLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171226L;

    /** Standard Constructor */
    public X_RH_EvaluationLine (Properties ctx, int RH_EvaluationLine_ID, String trxName)
    {
      super (ctx, RH_EvaluationLine_ID, trxName);
      /** if (RH_EvaluationLine_ID == 0)
        {
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
			setRH_EvaluationLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_EvaluationLine (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_RH_EvaluationLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Answer1 AD_Reference_ID=319 */
	public static final int ANSWER1_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ANSWER1_Yes = "Y";
	/** No = N */
	public static final String ANSWER1_No = "N";
	/** Set Answer1.
		@param Answer1 Answer1	  */
	public void setAnswer1 (String Answer1)
	{

		set_Value (COLUMNNAME_Answer1, Answer1);
	}

	/** Get Answer1.
		@return Answer1	  */
	public String getAnswer1 () 
	{
		return (String)get_Value(COLUMNNAME_Answer1);
	}

	/** Answer10 AD_Reference_ID=319 */
	public static final int ANSWER10_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ANSWER10_Yes = "Y";
	/** No = N */
	public static final String ANSWER10_No = "N";
	/** Set Answer10.
		@param Answer10 Answer10	  */
	public void setAnswer10 (String Answer10)
	{

		set_Value (COLUMNNAME_Answer10, Answer10);
	}

	/** Get Answer10.
		@return Answer10	  */
	public String getAnswer10 () 
	{
		return (String)get_Value(COLUMNNAME_Answer10);
	}

	/** Answer2 AD_Reference_ID=319 */
	public static final int ANSWER2_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ANSWER2_Yes = "Y";
	/** No = N */
	public static final String ANSWER2_No = "N";
	/** Set Answer2.
		@param Answer2 Answer2	  */
	public void setAnswer2 (String Answer2)
	{

		set_Value (COLUMNNAME_Answer2, Answer2);
	}

	/** Get Answer2.
		@return Answer2	  */
	public String getAnswer2 () 
	{
		return (String)get_Value(COLUMNNAME_Answer2);
	}

	/** Answer3 AD_Reference_ID=319 */
	public static final int ANSWER3_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ANSWER3_Yes = "Y";
	/** No = N */
	public static final String ANSWER3_No = "N";
	/** Set Answer3.
		@param Answer3 Answer3	  */
	public void setAnswer3 (String Answer3)
	{

		set_Value (COLUMNNAME_Answer3, Answer3);
	}

	/** Get Answer3.
		@return Answer3	  */
	public String getAnswer3 () 
	{
		return (String)get_Value(COLUMNNAME_Answer3);
	}

	/** Answer4 AD_Reference_ID=319 */
	public static final int ANSWER4_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ANSWER4_Yes = "Y";
	/** No = N */
	public static final String ANSWER4_No = "N";
	/** Set Answer4.
		@param Answer4 Answer4	  */
	public void setAnswer4 (String Answer4)
	{

		set_Value (COLUMNNAME_Answer4, Answer4);
	}

	/** Get Answer4.
		@return Answer4	  */
	public String getAnswer4 () 
	{
		return (String)get_Value(COLUMNNAME_Answer4);
	}

	/** Answer5 AD_Reference_ID=319 */
	public static final int ANSWER5_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ANSWER5_Yes = "Y";
	/** No = N */
	public static final String ANSWER5_No = "N";
	/** Set Answer5.
		@param Answer5 Answer5	  */
	public void setAnswer5 (String Answer5)
	{

		set_Value (COLUMNNAME_Answer5, Answer5);
	}

	/** Get Answer5.
		@return Answer5	  */
	public String getAnswer5 () 
	{
		return (String)get_Value(COLUMNNAME_Answer5);
	}

	/** Answer6 AD_Reference_ID=319 */
	public static final int ANSWER6_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ANSWER6_Yes = "Y";
	/** No = N */
	public static final String ANSWER6_No = "N";
	/** Set Answer6.
		@param Answer6 Answer6	  */
	public void setAnswer6 (String Answer6)
	{

		set_Value (COLUMNNAME_Answer6, Answer6);
	}

	/** Get Answer6.
		@return Answer6	  */
	public String getAnswer6 () 
	{
		return (String)get_Value(COLUMNNAME_Answer6);
	}

	/** Answer7 AD_Reference_ID=319 */
	public static final int ANSWER7_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ANSWER7_Yes = "Y";
	/** No = N */
	public static final String ANSWER7_No = "N";
	/** Set Answer7.
		@param Answer7 Answer7	  */
	public void setAnswer7 (String Answer7)
	{

		set_Value (COLUMNNAME_Answer7, Answer7);
	}

	/** Get Answer7.
		@return Answer7	  */
	public String getAnswer7 () 
	{
		return (String)get_Value(COLUMNNAME_Answer7);
	}

	/** Answer8 AD_Reference_ID=319 */
	public static final int ANSWER8_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ANSWER8_Yes = "Y";
	/** No = N */
	public static final String ANSWER8_No = "N";
	/** Set Answer8.
		@param Answer8 Answer8	  */
	public void setAnswer8 (String Answer8)
	{

		set_Value (COLUMNNAME_Answer8, Answer8);
	}

	/** Get Answer8.
		@return Answer8	  */
	public String getAnswer8 () 
	{
		return (String)get_Value(COLUMNNAME_Answer8);
	}

	/** Answer9 AD_Reference_ID=319 */
	public static final int ANSWER9_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String ANSWER9_Yes = "Y";
	/** No = N */
	public static final String ANSWER9_No = "N";
	/** Set Answer9.
		@param Answer9 Answer9	  */
	public void setAnswer9 (String Answer9)
	{

		set_Value (COLUMNNAME_Answer9, Answer9);
	}

	/** Get Answer9.
		@return Answer9	  */
	public String getAnswer9 () 
	{
		return (String)get_Value(COLUMNNAME_Answer9);
	}

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set question1_drive.
		@param question1_drive question1_drive	  */
	public void setquestion1_drive (String question1_drive)
	{
		set_Value (COLUMNNAME_question1_drive, question1_drive);
	}

	/** Get question1_drive.
		@return question1_drive	  */
	public String getquestion1_drive () 
	{
		return (String)get_Value(COLUMNNAME_question1_drive);
	}

	/** Set question10_drive.
		@param question10_drive question10_drive	  */
	public void setquestion10_drive (String question10_drive)
	{
		set_Value (COLUMNNAME_question10_drive, question10_drive);
	}

	/** Get question10_drive.
		@return question10_drive	  */
	public String getquestion10_drive () 
	{
		return (String)get_Value(COLUMNNAME_question10_drive);
	}

	/** Set question2_drive.
		@param question2_drive question2_drive	  */
	public void setquestion2_drive (String question2_drive)
	{
		set_Value (COLUMNNAME_question2_drive, question2_drive);
	}

	/** Get question2_drive.
		@return question2_drive	  */
	public String getquestion2_drive () 
	{
		return (String)get_Value(COLUMNNAME_question2_drive);
	}

	/** Set question3_drive.
		@param question3_drive question3_drive	  */
	public void setquestion3_drive (String question3_drive)
	{
		set_Value (COLUMNNAME_question3_drive, question3_drive);
	}

	/** Get question3_drive.
		@return question3_drive	  */
	public String getquestion3_drive () 
	{
		return (String)get_Value(COLUMNNAME_question3_drive);
	}

	/** Set question4_drive.
		@param question4_drive question4_drive	  */
	public void setquestion4_drive (String question4_drive)
	{
		set_Value (COLUMNNAME_question4_drive, question4_drive);
	}

	/** Get question4_drive.
		@return question4_drive	  */
	public String getquestion4_drive () 
	{
		return (String)get_Value(COLUMNNAME_question4_drive);
	}

	/** Set question5_drive.
		@param question5_drive question5_drive	  */
	public void setquestion5_drive (String question5_drive)
	{
		set_Value (COLUMNNAME_question5_drive, question5_drive);
	}

	/** Get question5_drive.
		@return question5_drive	  */
	public String getquestion5_drive () 
	{
		return (String)get_Value(COLUMNNAME_question5_drive);
	}

	/** Set question6_drive.
		@param question6_drive question6_drive	  */
	public void setquestion6_drive (String question6_drive)
	{
		set_Value (COLUMNNAME_question6_drive, question6_drive);
	}

	/** Get question6_drive.
		@return question6_drive	  */
	public String getquestion6_drive () 
	{
		return (String)get_Value(COLUMNNAME_question6_drive);
	}

	/** Set question7_drive.
		@param question7_drive question7_drive	  */
	public void setquestion7_drive (String question7_drive)
	{
		set_Value (COLUMNNAME_question7_drive, question7_drive);
	}

	/** Get question7_drive.
		@return question7_drive	  */
	public String getquestion7_drive () 
	{
		return (String)get_Value(COLUMNNAME_question7_drive);
	}

	/** Set question8_drive.
		@param question8_drive question8_drive	  */
	public void setquestion8_drive (String question8_drive)
	{
		set_Value (COLUMNNAME_question8_drive, question8_drive);
	}

	/** Get question8_drive.
		@return question8_drive	  */
	public String getquestion8_drive () 
	{
		return (String)get_Value(COLUMNNAME_question8_drive);
	}

	/** Set question9_drive.
		@param question9_drive question9_drive	  */
	public void setquestion9_drive (String question9_drive)
	{
		set_Value (COLUMNNAME_question9_drive, question9_drive);
	}

	/** Get question9_drive.
		@return question9_drive	  */
	public String getquestion9_drive () 
	{
		return (String)get_Value(COLUMNNAME_question9_drive);
	}

	/** Set Result.
		@param Result 
		Result of the action taken
	  */
	public void setResult (BigDecimal Result)
	{
		set_Value (COLUMNNAME_Result, Result);
	}

	/** Get Result.
		@return Result of the action taken
	  */
	public BigDecimal getResult () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Result);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_RH_EvaluationHeader getRH_EvaluationHeader() throws RuntimeException
    {
		return (I_RH_EvaluationHeader)MTable.get(getCtx(), I_RH_EvaluationHeader.Table_Name)
			.getPO(getRH_EvaluationHeader_ID(), get_TrxName());	}

	/** Set RH_EvaluationHeader.
		@param RH_EvaluationHeader_ID RH_EvaluationHeader	  */
	public void setRH_EvaluationHeader_ID (int RH_EvaluationHeader_ID)
	{
		if (RH_EvaluationHeader_ID < 1) 
			set_Value (COLUMNNAME_RH_EvaluationHeader_ID, null);
		else 
			set_Value (COLUMNNAME_RH_EvaluationHeader_ID, Integer.valueOf(RH_EvaluationHeader_ID));
	}

	/** Get RH_EvaluationHeader.
		@return RH_EvaluationHeader	  */
	public int getRH_EvaluationHeader_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_EvaluationHeader_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set RH_EvaluationLine.
		@param RH_EvaluationLine_ID RH_EvaluationLine	  */
	public void setRH_EvaluationLine_ID (int RH_EvaluationLine_ID)
	{
		if (RH_EvaluationLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_EvaluationLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_EvaluationLine_ID, Integer.valueOf(RH_EvaluationLine_ID));
	}

	/** Get RH_EvaluationLine.
		@return RH_EvaluationLine	  */
	public int getRH_EvaluationLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_EvaluationLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}