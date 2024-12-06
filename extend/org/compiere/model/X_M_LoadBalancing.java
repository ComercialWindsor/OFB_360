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
import org.compiere.util.KeyNamePair;

/** Generated Model for M_LoadBalancing
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_M_LoadBalancing extends PO implements I_M_LoadBalancing, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150615L;

    /** Standard Constructor */
    public X_M_LoadBalancing (Properties ctx, int M_LoadBalancing_ID, String trxName)
    {
      super (ctx, M_LoadBalancing_ID, trxName);
      /** if (M_LoadBalancing_ID == 0)
        {
			setM_LoadBalancing_ID (0);
        } */
    }

    /** Load Constructor */
    public X_M_LoadBalancing (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_M_LoadBalancing[")
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

	/** Set M_LoadBalancing.
		@param M_LoadBalancing_ID M_LoadBalancing	  */
	public void setM_LoadBalancing_ID (int M_LoadBalancing_ID)
	{
		if (M_LoadBalancing_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_LoadBalancing_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_LoadBalancing_ID, Integer.valueOf(M_LoadBalancing_ID));
	}

	/** Get M_LoadBalancing.
		@return M_LoadBalancing	  */
	public int getM_LoadBalancing_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_LoadBalancing_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Pep AD_Reference_ID=1000077 */
	public static final int PEP_AD_Reference_ID=1000077;
	/** 1 = UN */
	public static final String PEP_1 = "UN";
	/** 2 = DO */
	public static final String PEP_2 = "DO";
	/** 3 = TR */
	public static final String PEP_3 = "TR";
	/** 4 = CU */
	public static final String PEP_4 = "CU";
	/** Set Pep.
		@param Pep Pep	  */
	public void setPep (String Pep)
	{

		set_Value (COLUMNNAME_Pep, Pep);
	}

	/** Get Pep.
		@return Pep	  */
	public String getPep () 
	{
		return (String)get_Value(COLUMNNAME_Pep);
	}

	/** Set QtyFriday.
		@param QtyFriday QtyFriday	  */
	public void setQtyFriday (int QtyFriday)
	{
		set_Value (COLUMNNAME_QtyFriday, Integer.valueOf(QtyFriday));
	}

	/** Get QtyFriday.
		@return QtyFriday	  */
	public int getQtyFriday () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyFriday);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyMonday.
		@param QtyMonday QtyMonday	  */
	public void setQtyMonday (int QtyMonday)
	{
		set_Value (COLUMNNAME_QtyMonday, Integer.valueOf(QtyMonday));
	}

	/** Get QtyMonday.
		@return QtyMonday	  */
	public int getQtyMonday () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyMonday);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtySaturday.
		@param QtySaturday QtySaturday	  */
	public void setQtySaturday (int QtySaturday)
	{
		set_Value (COLUMNNAME_QtySaturday, Integer.valueOf(QtySaturday));
	}

	/** Get QtySaturday.
		@return QtySaturday	  */
	public int getQtySaturday () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtySaturday);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyThursday.
		@param QtyThursday QtyThursday	  */
	public void setQtyThursday (int QtyThursday)
	{
		set_Value (COLUMNNAME_QtyThursday, Integer.valueOf(QtyThursday));
	}

	/** Get QtyThursday.
		@return QtyThursday	  */
	public int getQtyThursday () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyThursday);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyTuesday.
		@param QtyTuesday QtyTuesday	  */
	public void setQtyTuesday (int QtyTuesday)
	{
		set_Value (COLUMNNAME_QtyTuesday, Integer.valueOf(QtyTuesday));
	}

	/** Get QtyTuesday.
		@return QtyTuesday	  */
	public int getQtyTuesday () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyTuesday);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set QtyWednesday.
		@param QtyWednesday QtyWednesday	  */
	public void setQtyWednesday (int QtyWednesday)
	{
		set_Value (COLUMNNAME_QtyWednesday, Integer.valueOf(QtyWednesday));
	}

	/** Get QtyWednesday.
		@return QtyWednesday	  */
	public int getQtyWednesday () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_QtyWednesday);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}