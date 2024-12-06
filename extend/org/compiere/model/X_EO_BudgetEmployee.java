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

/** Generated Model for EO_BudgetEmployee
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_EO_BudgetEmployee extends PO implements I_EO_BudgetEmployee, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20141113L;

    /** Standard Constructor */
    public X_EO_BudgetEmployee (Properties ctx, int EO_BudgetEmployee_ID, String trxName)
    {
      super (ctx, EO_BudgetEmployee_ID, trxName);
      /** if (EO_BudgetEmployee_ID == 0)
        {
			setEO_BudgetEmployee_ID (0);
			setEO_BudgetHdrEmployee_ID (0);
			setTotalAmt (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_EO_BudgetEmployee (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_EO_BudgetEmployee[")
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

	public I_C_Period getC_Period() throws RuntimeException
    {
		return (I_C_Period)MTable.get(getCtx(), I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** ContractType AD_Reference_ID=1000053 */
	public static final int CONTRACTTYPE_AD_Reference_ID=1000053;
	/** Indefinido = I */
	public static final String CONTRACTTYPE_Indefinido = "I";
	/** Plazo Fijo = F */
	public static final String CONTRACTTYPE_PlazoFijo = "F";
	/** Honorarios = H */
	public static final String CONTRACTTYPE_Honorarios = "H";
	/** Set ContractType.
		@param ContractType ContractType	  */
	public void setContractType (String ContractType)
	{

		throw new IllegalArgumentException ("ContractType is virtual column");	}

	/** Get ContractType.
		@return ContractType	  */
	public String getContractType () 
	{
		return (String)get_Value(COLUMNNAME_ContractType);
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

	/** Set EntryYear.
		@param EntryYear EntryYear	  */
	public void setEntryYear (int EntryYear)
	{
		throw new IllegalArgumentException ("EntryYear is virtual column");	}

	/** Get EntryYear.
		@return EntryYear	  */
	public int getEntryYear () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_EntryYear);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set EO_BudgetEmployee.
		@param EO_BudgetEmployee_ID EO_BudgetEmployee	  */
	public void setEO_BudgetEmployee_ID (int EO_BudgetEmployee_ID)
	{
		if (EO_BudgetEmployee_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_EO_BudgetEmployee_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_EO_BudgetEmployee_ID, Integer.valueOf(EO_BudgetEmployee_ID));
	}

	/** Get EO_BudgetEmployee.
		@return EO_BudgetEmployee	  */
	public int getEO_BudgetEmployee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_EO_BudgetEmployee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_EO_BudgetHdrEmployee getEO_BudgetHdrEmployee() throws RuntimeException
    {
		return (I_EO_BudgetHdrEmployee)MTable.get(getCtx(), I_EO_BudgetHdrEmployee.Table_Name)
			.getPO(getEO_BudgetHdrEmployee_ID(), get_TrxName());	}

	/** Set EO_BudgetHdrEmployee.
		@param EO_BudgetHdrEmployee_ID EO_BudgetHdrEmployee	  */
	public void setEO_BudgetHdrEmployee_ID (int EO_BudgetHdrEmployee_ID)
	{
		if (EO_BudgetHdrEmployee_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_EO_BudgetHdrEmployee_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_EO_BudgetHdrEmployee_ID, Integer.valueOf(EO_BudgetHdrEmployee_ID));
	}

	/** Get EO_BudgetHdrEmployee.
		@return EO_BudgetHdrEmployee	  */
	public int getEO_BudgetHdrEmployee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_EO_BudgetHdrEmployee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HoursAvailable.
		@param HoursAvailable HoursAvailable	  */
	public void setHoursAvailable (int HoursAvailable)
	{
		throw new IllegalArgumentException ("HoursAvailable is virtual column");	}

	/** Get HoursAvailable.
		@return HoursAvailable	  */
	public int getHoursAvailable () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HoursAvailable);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Product getM_Product() throws RuntimeException
    {
		return (I_M_Product)MTable.get(getCtx(), I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set MonthHours.
		@param MonthHours MonthHours	  */
	public void setMonthHours (int MonthHours)
	{
		throw new IllegalArgumentException ("MonthHours is virtual column");	}

	/** Get MonthHours.
		@return MonthHours	  */
	public int getMonthHours () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MonthHours);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Position.
		@param Position Position	  */
	public void setPosition (String Position)
	{
		throw new IllegalArgumentException ("Position is virtual column");	}

	/** Get Position.
		@return Position	  */
	public String getPosition () 
	{
		return (String)get_Value(COLUMNNAME_Position);
	}

	/** Set Rut.
		@param Rut Rut	  */
	public void setRut (String Rut)
	{
		throw new IllegalArgumentException ("Rut is virtual column");	}

	/** Get Rut.
		@return Rut	  */
	public String getRut () 
	{
		return (String)get_Value(COLUMNNAME_Rut);
	}

	/** Set Seniority.
		@param Seniority Seniority	  */
	public void setSeniority (int Seniority)
	{
		throw new IllegalArgumentException ("Seniority is virtual column");	}

	/** Get Seniority.
		@return Seniority	  */
	public int getSeniority () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Seniority);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Total Amount.
		@param TotalAmt 
		Total Amount
	  */
	public void setTotalAmt (BigDecimal TotalAmt)
	{
		set_Value (COLUMNNAME_TotalAmt, TotalAmt);
	}

	/** Get Total Amount.
		@return Total Amount
	  */
	public BigDecimal getTotalAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}