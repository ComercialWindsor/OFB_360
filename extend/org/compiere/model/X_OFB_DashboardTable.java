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

/** Generated Model for OFB_DashboardTable
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_OFB_DashboardTable extends PO implements I_OFB_DashboardTable, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140626L;

    /** Standard Constructor */
    public X_OFB_DashboardTable (Properties ctx, int OFB_DashboardTable_ID, String trxName)
    {
      super (ctx, OFB_DashboardTable_ID, trxName);
      /** if (OFB_DashboardTable_ID == 0)
        {
			setLine (0);
			setNameRow1 (null);
			setOFB_DashboardTable_ID (0);
			setTableName (null);
        } */
    }

    /** Load Constructor */
    public X_OFB_DashboardTable (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_OFB_DashboardTable[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** MeasureDisplay AD_Reference_ID=367 */
	public static final int MEASUREDISPLAY_AD_Reference_ID=367;
	/** Year = 1 */
	public static final String MEASUREDISPLAY_Year = "1";
	/** Quarter = 3 */
	public static final String MEASUREDISPLAY_Quarter = "3";
	/** Month = 5 */
	public static final String MEASUREDISPLAY_Month = "5";
	/** Total = 0 */
	public static final String MEASUREDISPLAY_Total = "0";
	/** Week = 7 */
	public static final String MEASUREDISPLAY_Week = "7";
	/** Day = 8 */
	public static final String MEASUREDISPLAY_Day = "8";
	/** Set Measure Display.
		@param MeasureDisplay 
		Measure Scope initially displayed
	  */
	public void setMeasureDisplay (String MeasureDisplay)
	{

		set_Value (COLUMNNAME_MeasureDisplay, MeasureDisplay);
	}

	/** Get Measure Display.
		@return Measure Scope initially displayed
	  */
	public String getMeasureDisplay () 
	{
		return (String)get_Value(COLUMNNAME_MeasureDisplay);
	}

	/** Set NameRow1.
		@param NameRow1 NameRow1	  */
	public void setNameRow1 (String NameRow1)
	{
		set_Value (COLUMNNAME_NameRow1, NameRow1);
	}

	/** Get NameRow1.
		@return NameRow1	  */
	public String getNameRow1 () 
	{
		return (String)get_Value(COLUMNNAME_NameRow1);
	}

	/** Set NameRow2.
		@param NameRow2 NameRow2	  */
	public void setNameRow2 (String NameRow2)
	{
		set_Value (COLUMNNAME_NameRow2, NameRow2);
	}

	/** Get NameRow2.
		@return NameRow2	  */
	public String getNameRow2 () 
	{
		return (String)get_Value(COLUMNNAME_NameRow2);
	}

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

	/** Set DB Table Name.
		@param TableName 
		Name of the table in the database
	  */
	public void setTableName (String TableName)
	{
		set_Value (COLUMNNAME_TableName, TableName);
	}

	/** Get DB Table Name.
		@return Name of the table in the database
	  */
	public String getTableName () 
	{
		return (String)get_Value(COLUMNNAME_TableName);
	}
}