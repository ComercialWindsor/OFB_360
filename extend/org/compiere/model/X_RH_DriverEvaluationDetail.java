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
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for RH_DriverEvaluationDetail
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_DriverEvaluationDetail extends PO implements I_RH_DriverEvaluationDetail, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170327L;

    /** Standard Constructor */
    public X_RH_DriverEvaluationDetail (Properties ctx, int RH_DriverEvaluationDetail_ID, String trxName)
    {
      super (ctx, RH_DriverEvaluationDetail_ID, trxName);
      /** if (RH_DriverEvaluationDetail_ID == 0)
        {
			setIsValid (false);
// N
			setProcessed (false);
// N
			setRH_DriverEvaluationDetail_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_DriverEvaluationDetail (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_DriverEvaluationDetail[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Valid.
		@param IsValid 
		Element is valid
	  */
	public void setIsValid (boolean IsValid)
	{
		set_Value (COLUMNNAME_IsValid, Boolean.valueOf(IsValid));
	}

	/** Get Valid.
		@return Element is valid
	  */
	public boolean isValid () 
	{
		Object oo = get_Value(COLUMNNAME_IsValid);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Percentage.
		@param Percentage 
		Percent of the entire amount
	  */
	public void setPercentage (BigDecimal Percentage)
	{
		set_Value (COLUMNNAME_Percentage, Percentage);
	}

	/** Get Percentage.
		@return Percent of the entire amount
	  */
	public BigDecimal getPercentage () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Percentage10.
		@param Percentage10 Percentage10	  */
	public void setPercentage10 (BigDecimal Percentage10)
	{
		set_Value (COLUMNNAME_Percentage10, Percentage10);
	}

	/** Get Percentage10.
		@return Percentage10	  */
	public BigDecimal getPercentage10 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage10);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Percentage2.
		@param Percentage2 Percentage2	  */
	public void setPercentage2 (BigDecimal Percentage2)
	{
		set_Value (COLUMNNAME_Percentage2, Percentage2);
	}

	/** Get Percentage2.
		@return Percentage2	  */
	public BigDecimal getPercentage2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Percentage3.
		@param Percentage3 Percentage3	  */
	public void setPercentage3 (BigDecimal Percentage3)
	{
		set_Value (COLUMNNAME_Percentage3, Percentage3);
	}

	/** Get Percentage3.
		@return Percentage3	  */
	public BigDecimal getPercentage3 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage3);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Percentage4.
		@param Percentage4 Percentage4	  */
	public void setPercentage4 (BigDecimal Percentage4)
	{
		set_Value (COLUMNNAME_Percentage4, Percentage4);
	}

	/** Get Percentage4.
		@return Percentage4	  */
	public BigDecimal getPercentage4 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage4);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Percentage5.
		@param Percentage5 Percentage5	  */
	public void setPercentage5 (BigDecimal Percentage5)
	{
		set_Value (COLUMNNAME_Percentage5, Percentage5);
	}

	/** Get Percentage5.
		@return Percentage5	  */
	public BigDecimal getPercentage5 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage5);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Percentage6.
		@param Percentage6 Percentage6	  */
	public void setPercentage6 (BigDecimal Percentage6)
	{
		set_Value (COLUMNNAME_Percentage6, Percentage6);
	}

	/** Get Percentage6.
		@return Percentage6	  */
	public BigDecimal getPercentage6 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage6);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Percentage7.
		@param Percentage7 Percentage7	  */
	public void setPercentage7 (BigDecimal Percentage7)
	{
		set_Value (COLUMNNAME_Percentage7, Percentage7);
	}

	/** Get Percentage7.
		@return Percentage7	  */
	public BigDecimal getPercentage7 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage7);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Percentage8.
		@param Percentage8 Percentage8	  */
	public void setPercentage8 (BigDecimal Percentage8)
	{
		set_Value (COLUMNNAME_Percentage8, Percentage8);
	}

	/** Get Percentage8.
		@return Percentage8	  */
	public BigDecimal getPercentage8 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage8);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Percentage9.
		@param Percentage9 Percentage9	  */
	public void setPercentage9 (BigDecimal Percentage9)
	{
		set_Value (COLUMNNAME_Percentage9, Percentage9);
	}

	/** Get Percentage9.
		@return Percentage9	  */
	public BigDecimal getPercentage9 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percentage9);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Question1.
		@param Question1 Question1	  */
	public void setQuestion1 (String Question1)
	{
		set_Value (COLUMNNAME_Question1, Question1);
	}

	/** Get Question1.
		@return Question1	  */
	public String getQuestion1 () 
	{
		return (String)get_Value(COLUMNNAME_Question1);
	}

	/** Set Question10.
		@param Question10 Question10	  */
	public void setQuestion10 (String Question10)
	{
		set_Value (COLUMNNAME_Question10, Question10);
	}

	/** Get Question10.
		@return Question10	  */
	public String getQuestion10 () 
	{
		return (String)get_Value(COLUMNNAME_Question10);
	}

	/** Set Question2.
		@param Question2 Question2	  */
	public void setQuestion2 (String Question2)
	{
		set_Value (COLUMNNAME_Question2, Question2);
	}

	/** Get Question2.
		@return Question2	  */
	public String getQuestion2 () 
	{
		return (String)get_Value(COLUMNNAME_Question2);
	}

	/** Set Question3.
		@param Question3 Question3	  */
	public void setQuestion3 (String Question3)
	{
		set_Value (COLUMNNAME_Question3, Question3);
	}

	/** Get Question3.
		@return Question3	  */
	public String getQuestion3 () 
	{
		return (String)get_Value(COLUMNNAME_Question3);
	}

	/** Set Question4.
		@param Question4 Question4	  */
	public void setQuestion4 (String Question4)
	{
		set_Value (COLUMNNAME_Question4, Question4);
	}

	/** Get Question4.
		@return Question4	  */
	public String getQuestion4 () 
	{
		return (String)get_Value(COLUMNNAME_Question4);
	}

	/** Set Question5.
		@param Question5 Question5	  */
	public void setQuestion5 (String Question5)
	{
		set_Value (COLUMNNAME_Question5, Question5);
	}

	/** Get Question5.
		@return Question5	  */
	public String getQuestion5 () 
	{
		return (String)get_Value(COLUMNNAME_Question5);
	}

	/** Set Question6.
		@param Question6 Question6	  */
	public void setQuestion6 (String Question6)
	{
		set_Value (COLUMNNAME_Question6, Question6);
	}

	/** Get Question6.
		@return Question6	  */
	public String getQuestion6 () 
	{
		return (String)get_Value(COLUMNNAME_Question6);
	}

	/** Set Question7.
		@param Question7 Question7	  */
	public void setQuestion7 (String Question7)
	{
		set_Value (COLUMNNAME_Question7, Question7);
	}

	/** Get Question7.
		@return Question7	  */
	public String getQuestion7 () 
	{
		return (String)get_Value(COLUMNNAME_Question7);
	}

	/** Set Question8.
		@param Question8 Question8	  */
	public void setQuestion8 (String Question8)
	{
		set_Value (COLUMNNAME_Question8, Question8);
	}

	/** Get Question8.
		@return Question8	  */
	public String getQuestion8 () 
	{
		return (String)get_Value(COLUMNNAME_Question8);
	}

	/** Set Question9.
		@param Question9 Question9	  */
	public void setQuestion9 (String Question9)
	{
		set_Value (COLUMNNAME_Question9, Question9);
	}

	/** Get Question9.
		@return Question9	  */
	public String getQuestion9 () 
	{
		return (String)get_Value(COLUMNNAME_Question9);
	}

	public I_RH_DriverEvaluation getRH_DriverEvaluation() throws RuntimeException
    {
		return (I_RH_DriverEvaluation)MTable.get(getCtx(), I_RH_DriverEvaluation.Table_Name)
			.getPO(getRH_DriverEvaluation_ID(), get_TrxName());	}

	/** Set RH_DriverEvaluation.
		@param RH_DriverEvaluation_ID RH_DriverEvaluation	  */
	public void setRH_DriverEvaluation_ID (int RH_DriverEvaluation_ID)
	{
		if (RH_DriverEvaluation_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_DriverEvaluation_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_DriverEvaluation_ID, Integer.valueOf(RH_DriverEvaluation_ID));
	}

	/** Get RH_DriverEvaluation.
		@return RH_DriverEvaluation	  */
	public int getRH_DriverEvaluation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_DriverEvaluation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set RH_DriverEvaluationDetail.
		@param RH_DriverEvaluationDetail_ID RH_DriverEvaluationDetail	  */
	public void setRH_DriverEvaluationDetail_ID (int RH_DriverEvaluationDetail_ID)
	{
		if (RH_DriverEvaluationDetail_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_DriverEvaluationDetail_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_DriverEvaluationDetail_ID, Integer.valueOf(RH_DriverEvaluationDetail_ID));
	}

	/** Get RH_DriverEvaluationDetail.
		@return RH_DriverEvaluationDetail	  */
	public int getRH_DriverEvaluationDetail_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_DriverEvaluationDetail_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set total.
		@param total total	  */
	public void settotal (BigDecimal total)
	{
		set_Value (COLUMNNAME_total, total);
	}

	/** Get total.
		@return total	  */
	public BigDecimal gettotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_total);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}