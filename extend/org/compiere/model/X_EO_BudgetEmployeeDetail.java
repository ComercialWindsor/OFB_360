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

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for EO_BudgetEmployeeDetail
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_EO_BudgetEmployeeDetail extends PO implements I_EO_BudgetEmployeeDetail, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20141113L;

    /** Standard Constructor */
    public X_EO_BudgetEmployeeDetail (Properties ctx, int EO_BudgetEmployeeDetail_ID, String trxName)
    {
      super (ctx, EO_BudgetEmployeeDetail_ID, trxName);
      /** if (EO_BudgetEmployeeDetail_ID == 0)
        {
			setEO_BudgetEmployee_ID (0);
			setEO_BudgetEmployeeDetail_ID (0);
        } */
    }

    /** Load Constructor */
    public X_EO_BudgetEmployeeDetail (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_EO_BudgetEmployeeDetail[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	public I_EO_BudgetEmployee getEO_BudgetEmployee() throws RuntimeException
    {
		return (I_EO_BudgetEmployee)MTable.get(getCtx(), I_EO_BudgetEmployee.Table_Name)
			.getPO(getEO_BudgetEmployee_ID(), get_TrxName());	}

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

	/** Set EO_BudgetEmployeeDetail.
		@param EO_BudgetEmployeeDetail_ID EO_BudgetEmployeeDetail	  */
	public void setEO_BudgetEmployeeDetail_ID (int EO_BudgetEmployeeDetail_ID)
	{
		if (EO_BudgetEmployeeDetail_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_EO_BudgetEmployeeDetail_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_EO_BudgetEmployeeDetail_ID, Integer.valueOf(EO_BudgetEmployeeDetail_ID));
	}

	/** Get EO_BudgetEmployeeDetail.
		@return EO_BudgetEmployeeDetail	  */
	public int getEO_BudgetEmployeeDetail_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_EO_BudgetEmployeeDetail_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Grade AD_Reference_ID=1000055 */
	public static final int GRADE_AD_Reference_ID=1000055;
	/** 1°A = 1A */
	public static final String GRADE_1A = "1A";
	/** 2°A = 2A */
	public static final String GRADE_2A = "2A";
	/** 3°A = 3A */
	public static final String GRADE_3A = "3A";
	/** 4°A = 4A */
	public static final String GRADE_4A = "4A";
	/** 5°A = 5A */
	public static final String GRADE_5A = "5A";
	/** 6°A = 6A */
	public static final String GRADE_6A = "6A";
	/** 7°A = 7A */
	public static final String GRADE_7A = "7A";
	/** 8°A = 8A */
	public static final String GRADE_8A = "8A";
	/** Set Grade.
		@param Grade Grade	  */
	public void setGrade (String Grade)
	{

		set_Value (COLUMNNAME_Grade, Grade);
	}

	/** Get Grade.
		@return Grade	  */
	public String getGrade () 
	{
		return (String)get_Value(COLUMNNAME_Grade);
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

	/** Set QtyHours1.
		@param QtyHours1 QtyHours1	  */
	public void setQtyHours1 (int QtyHours1)
	{
		set_Value (COLUMNNAME_QtyHours1, Integer.valueOf(QtyHours1));
	}

	/** Get QtyHours1.
		@return QtyHours1	  */
	public int getQtyHours1 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours1);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours10.
		@param QtyHours10 QtyHours10	  */
	public void setQtyHours10 (int QtyHours10)
	{
		set_Value (COLUMNNAME_QtyHours10, Integer.valueOf(QtyHours10));
	}

	/** Get QtyHours10.
		@return QtyHours10	  */
	public int getQtyHours10 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours10);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours11.
		@param QtyHours11 QtyHours11	  */
	public void setQtyHours11 (int QtyHours11)
	{
		set_Value (COLUMNNAME_QtyHours11, Integer.valueOf(QtyHours11));
	}

	/** Get QtyHours11.
		@return QtyHours11	  */
	public int getQtyHours11 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours11);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours12.
		@param QtyHours12 QtyHours12	  */
	public void setQtyHours12 (int QtyHours12)
	{
		set_Value (COLUMNNAME_QtyHours12, Integer.valueOf(QtyHours12));
	}

	/** Get QtyHours12.
		@return QtyHours12	  */
	public int getQtyHours12 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours12);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours2.
		@param QtyHours2 QtyHours2	  */
	public void setQtyHours2 (int QtyHours2)
	{
		set_Value (COLUMNNAME_QtyHours2, Integer.valueOf(QtyHours2));
	}

	/** Get QtyHours2.
		@return QtyHours2	  */
	public int getQtyHours2 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours2);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours3.
		@param QtyHours3 QtyHours3	  */
	public void setQtyHours3 (int QtyHours3)
	{
		set_Value (COLUMNNAME_QtyHours3, Integer.valueOf(QtyHours3));
	}

	/** Get QtyHours3.
		@return QtyHours3	  */
	public int getQtyHours3 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours3);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours4.
		@param QtyHours4 QtyHours4	  */
	public void setQtyHours4 (int QtyHours4)
	{
		set_Value (COLUMNNAME_QtyHours4, Integer.valueOf(QtyHours4));
	}

	/** Get QtyHours4.
		@return QtyHours4	  */
	public int getQtyHours4 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours4);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours5.
		@param QtyHours5 QtyHours5	  */
	public void setQtyHours5 (int QtyHours5)
	{
		set_Value (COLUMNNAME_QtyHours5, Integer.valueOf(QtyHours5));
	}

	/** Get QtyHours5.
		@return QtyHours5	  */
	public int getQtyHours5 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours5);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours6.
		@param QtyHours6 QtyHours6	  */
	public void setQtyHours6 (int QtyHours6)
	{
		set_Value (COLUMNNAME_QtyHours6, Integer.valueOf(QtyHours6));
	}

	/** Get QtyHours6.
		@return QtyHours6	  */
	public int getQtyHours6 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours6);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours7.
		@param QtyHours7 QtyHours7	  */
	public void setQtyHours7 (int QtyHours7)
	{
		set_Value (COLUMNNAME_QtyHours7, Integer.valueOf(QtyHours7));
	}

	/** Get QtyHours7.
		@return QtyHours7	  */
	public int getQtyHours7 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours7);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours8.
		@param QtyHours8 QtyHours8	  */
	public void setQtyHours8 (int QtyHours8)
	{
		set_Value (COLUMNNAME_QtyHours8, Integer.valueOf(QtyHours8));
	}

	/** Get QtyHours8.
		@return QtyHours8	  */
	public int getQtyHours8 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours8);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyHours9.
		@param QtyHours9 QtyHours9	  */
	public void setQtyHours9 (int QtyHours9)
	{
		set_Value (COLUMNNAME_QtyHours9, Integer.valueOf(QtyHours9));
	}

	/** Get QtyHours9.
		@return QtyHours9	  */
	public int getQtyHours9 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyHours9);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}