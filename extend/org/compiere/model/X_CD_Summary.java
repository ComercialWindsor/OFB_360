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

/** Generated Model for CD_Summary
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_CD_Summary extends PO implements I_CD_Summary, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160908L;

    /** Standard Constructor */
    public X_CD_Summary (Properties ctx, int CD_Summary_ID, String trxName)
    {
      super (ctx, CD_Summary_ID, trxName);
      /** if (CD_Summary_ID == 0)
        {
			setCD_Summary_ID (0);
        } */
    }

    /** Load Constructor */
    public X_CD_Summary (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_CD_Summary[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_CD_Header getCD_Header() throws RuntimeException
    {
		return (I_CD_Header)MTable.get(getCtx(), I_CD_Header.Table_Name)
			.getPO(getCD_Header_ID(), get_TrxName());	}

	/** Set CD_Header.
		@param CD_Header_ID CD_Header	  */
	public void setCD_Header_ID (int CD_Header_ID)
	{
		if (CD_Header_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_CD_Header_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_CD_Header_ID, Integer.valueOf(CD_Header_ID));
	}

	/** Get CD_Header.
		@return CD_Header	  */
	public int getCD_Header_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CD_Header_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CD_Summary.
		@param CD_Summary_ID CD_Summary	  */
	public void setCD_Summary_ID (int CD_Summary_ID)
	{
		if (CD_Summary_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_CD_Summary_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_CD_Summary_ID, Integer.valueOf(CD_Summary_ID));
	}

	/** Get CD_Summary.
		@return CD_Summary	  */
	public int getCD_Summary_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CD_Summary_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CreditLine.
		@param CreditLine CreditLine	  */
	public void setCreditLine (BigDecimal CreditLine)
	{
		set_Value (COLUMNNAME_CreditLine, CreditLine);
	}

	/** Get CreditLine.
		@return CreditLine	  */
	public BigDecimal getCreditLine () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CreditLine);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Grand Total.
		@param GrandTotal 
		Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_Value (COLUMNNAME_GrandTotal, GrandTotal);
	}

	/** Get Grand Total.
		@return Total amount of document
	  */
	public BigDecimal getGrandTotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrandTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set InvestmentAmount.
		@param InvestmentAmount InvestmentAmount	  */
	public void setInvestmentAmount (BigDecimal InvestmentAmount)
	{
		set_Value (COLUMNNAME_InvestmentAmount, InvestmentAmount);
	}

	/** Get InvestmentAmount.
		@return InvestmentAmount	  */
	public BigDecimal getInvestmentAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_InvestmentAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Surplus.
		@param Surplus Surplus	  */
	public void setSurplus (BigDecimal Surplus)
	{
		set_Value (COLUMNNAME_Surplus, Surplus);
	}

	/** Get Surplus.
		@return Surplus	  */
	public BigDecimal getSurplus () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Surplus);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TotalBanks.
		@param TotalBanks TotalBanks	  */
	public void setTotalBanks (BigDecimal TotalBanks)
	{
		set_Value (COLUMNNAME_TotalBanks, TotalBanks);
	}

	/** Get TotalBanks.
		@return TotalBanks	  */
	public BigDecimal getTotalBanks () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalBanks);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TotalExpenses.
		@param TotalExpenses TotalExpenses	  */
	public void setTotalExpenses (BigDecimal TotalExpenses)
	{
		set_Value (COLUMNNAME_TotalExpenses, TotalExpenses);
	}

	/** Get TotalExpenses.
		@return TotalExpenses	  */
	public BigDecimal getTotalExpenses () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalExpenses);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TotalIncome.
		@param TotalIncome TotalIncome	  */
	public void setTotalIncome (BigDecimal TotalIncome)
	{
		set_Value (COLUMNNAME_TotalIncome, TotalIncome);
	}

	/** Get TotalIncome.
		@return TotalIncome	  */
	public BigDecimal getTotalIncome () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalIncome);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TotalTransfer.
		@param TotalTransfer TotalTransfer	  */
	public void setTotalTransfer (BigDecimal TotalTransfer)
	{
		set_Value (COLUMNNAME_TotalTransfer, TotalTransfer);
	}

	/** Get TotalTransfer.
		@return TotalTransfer	  */
	public BigDecimal getTotalTransfer () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalTransfer);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}