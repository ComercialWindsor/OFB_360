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

/** Generated Model for OFB_DashboardTableCol
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_OFB_DashboardTableCol extends PO implements I_OFB_DashboardTableCol, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140630L;

    /** Standard Constructor */
    public X_OFB_DashboardTableCol (Properties ctx, int OFB_DashboardTableCol_ID, String trxName)
    {
      super (ctx, OFB_DashboardTableCol_ID, trxName);
      /** if (OFB_DashboardTableCol_ID == 0)
        {
			setColName (null);
			setDateColumn (null);
			setLine (0);
			setOFB_DashboardTable_ID (0);
			setOFB_DashboardTableCol_ID (0);
			setSelectClause (null);
			setWhereClause (null);
        } */
    }

    /** Load Constructor */
    public X_OFB_DashboardTableCol (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 2 - Client 
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
      StringBuffer sb = new StringBuffer ("X_OFB_DashboardTableCol[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ColName.
		@param ColName ColName	  */
	public void setColName (String ColName)
	{
		set_Value (COLUMNNAME_ColName, ColName);
	}

	/** Get ColName.
		@return ColName	  */
	public String getColName () 
	{
		return (String)get_Value(COLUMNNAME_ColName);
	}

	/** Set Date Column.
		@param DateColumn 
		Fully qualified date column
	  */
	public void setDateColumn (String DateColumn)
	{
		set_Value (COLUMNNAME_DateColumn, DateColumn);
	}

	/** Get Date Column.
		@return Fully qualified date column
	  */
	public String getDateColumn () 
	{
		return (String)get_Value(COLUMNNAME_DateColumn);
	}

	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (int Line)
	{
		set_Value (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_OFB_DashboardTable getOFB_DashboardTable() throws RuntimeException
    {
		return (I_OFB_DashboardTable)MTable.get(getCtx(), I_OFB_DashboardTable.Table_Name)
			.getPO(getOFB_DashboardTable_ID(), get_TrxName());	}

	/** Set OFB_DashboardTable.
		@param OFB_DashboardTable_ID OFB_DashboardTable	  */
	public void setOFB_DashboardTable_ID (int OFB_DashboardTable_ID)
	{
		if (OFB_DashboardTable_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_OFB_DashboardTable_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_OFB_DashboardTable_ID, Integer.valueOf(OFB_DashboardTable_ID));
	}

	/** Get OFB_DashboardTable.
		@return OFB_DashboardTable	  */
	public int getOFB_DashboardTable_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_OFB_DashboardTable_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set OFB_DashboardTableCol.
		@param OFB_DashboardTableCol_ID OFB_DashboardTableCol	  */
	public void setOFB_DashboardTableCol_ID (int OFB_DashboardTableCol_ID)
	{
		if (OFB_DashboardTableCol_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_OFB_DashboardTableCol_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_OFB_DashboardTableCol_ID, Integer.valueOf(OFB_DashboardTableCol_ID));
	}

	/** Get OFB_DashboardTableCol.
		@return OFB_DashboardTableCol	  */
	public int getOFB_DashboardTableCol_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_OFB_DashboardTableCol_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sql SELECT.
		@param SelectClause 
		SQL SELECT clause
	  */
	public void setSelectClause (String SelectClause)
	{
		set_Value (COLUMNNAME_SelectClause, SelectClause);
	}

	/** Get Sql SELECT.
		@return SQL SELECT clause
	  */
	public String getSelectClause () 
	{
		return (String)get_Value(COLUMNNAME_SelectClause);
	}

	/** Set Sql WHERE.
		@param WhereClause 
		Fully qualified SQL WHERE clause
	  */
	public void setWhereClause (String WhereClause)
	{
		set_Value (COLUMNNAME_WhereClause, WhereClause);
	}

	/** Get Sql WHERE.
		@return Fully qualified SQL WHERE clause
	  */
	public String getWhereClause () 
	{
		return (String)get_Value(COLUMNNAME_WhereClause);
	}
}