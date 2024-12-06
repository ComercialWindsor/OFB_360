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

/** Generated Model for RH_EvaluationGuide
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_EvaluationGuide extends PO implements I_RH_EvaluationGuide, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171226L;

    /** Standard Constructor */
    public X_RH_EvaluationGuide (Properties ctx, int RH_EvaluationGuide_ID, String trxName)
    {
      super (ctx, RH_EvaluationGuide_ID, trxName);
      /** if (RH_EvaluationGuide_ID == 0)
        {
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
			setRH_EvaluationGuide_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_EvaluationGuide (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_EvaluationGuide[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Answer1.
		@param Answer1 Answer1	  */
	public void setAnswer1 (BigDecimal Answer1)
	{
		set_Value (COLUMNNAME_Answer1, Answer1);
	}

	/** Get Answer1.
		@return Answer1	  */
	public BigDecimal getAnswer1 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Answer1);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Answer10.
		@param Answer10 Answer10	  */
	public void setAnswer10 (BigDecimal Answer10)
	{
		set_Value (COLUMNNAME_Answer10, Answer10);
	}

	/** Get Answer10.
		@return Answer10	  */
	public BigDecimal getAnswer10 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Answer10);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Answer2.
		@param Answer2 Answer2	  */
	public void setAnswer2 (BigDecimal Answer2)
	{
		set_Value (COLUMNNAME_Answer2, Answer2);
	}

	/** Get Answer2.
		@return Answer2	  */
	public BigDecimal getAnswer2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Answer2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Answer3.
		@param Answer3 Answer3	  */
	public void setAnswer3 (BigDecimal Answer3)
	{
		set_Value (COLUMNNAME_Answer3, Answer3);
	}

	/** Get Answer3.
		@return Answer3	  */
	public BigDecimal getAnswer3 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Answer3);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Answer4.
		@param Answer4 Answer4	  */
	public void setAnswer4 (BigDecimal Answer4)
	{
		set_Value (COLUMNNAME_Answer4, Answer4);
	}

	/** Get Answer4.
		@return Answer4	  */
	public BigDecimal getAnswer4 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Answer4);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Answer5.
		@param Answer5 Answer5	  */
	public void setAnswer5 (BigDecimal Answer5)
	{
		set_Value (COLUMNNAME_Answer5, Answer5);
	}

	/** Get Answer5.
		@return Answer5	  */
	public BigDecimal getAnswer5 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Answer5);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Answer6.
		@param Answer6 Answer6	  */
	public void setAnswer6 (BigDecimal Answer6)
	{
		set_Value (COLUMNNAME_Answer6, Answer6);
	}

	/** Get Answer6.
		@return Answer6	  */
	public BigDecimal getAnswer6 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Answer6);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Answer7.
		@param Answer7 Answer7	  */
	public void setAnswer7 (BigDecimal Answer7)
	{
		set_Value (COLUMNNAME_Answer7, Answer7);
	}

	/** Get Answer7.
		@return Answer7	  */
	public BigDecimal getAnswer7 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Answer7);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Answer8.
		@param Answer8 Answer8	  */
	public void setAnswer8 (BigDecimal Answer8)
	{
		set_Value (COLUMNNAME_Answer8, Answer8);
	}

	/** Get Answer8.
		@return Answer8	  */
	public BigDecimal getAnswer8 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Answer8);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Answer9.
		@param Answer9 Answer9	  */
	public void setAnswer9 (BigDecimal Answer9)
	{
		set_Value (COLUMNNAME_Answer9, Answer9);
	}

	/** Get Answer9.
		@return Answer9	  */
	public BigDecimal getAnswer9 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Answer9);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set RH_EvaluationGuide.
		@param RH_EvaluationGuide_ID RH_EvaluationGuide	  */
	public void setRH_EvaluationGuide_ID (int RH_EvaluationGuide_ID)
	{
		if (RH_EvaluationGuide_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_EvaluationGuide_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_EvaluationGuide_ID, Integer.valueOf(RH_EvaluationGuide_ID));
	}

	/** Get RH_EvaluationGuide.
		@return RH_EvaluationGuide	  */
	public int getRH_EvaluationGuide_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_EvaluationGuide_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SumOfAnswers.
		@param SumOfAnswers SumOfAnswers	  */
	public void setSumOfAnswers (BigDecimal SumOfAnswers)
	{
		set_Value (COLUMNNAME_SumOfAnswers, SumOfAnswers);
	}

	/** Get SumOfAnswers.
		@return SumOfAnswers	  */
	public BigDecimal getSumOfAnswers () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SumOfAnswers);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}