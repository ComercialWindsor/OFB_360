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

/** Generated Model for AdministrativeRequestsLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_AdministrativeRequestsLine extends PO implements I_RH_AdministrativeRequestsLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120207L;

    /** Standard Constructor */
    public X_RH_AdministrativeRequestsLine (Properties ctx, int AdministrativeRequestsLine_ID, String trxName)
    {
      super (ctx, AdministrativeRequestsLine_ID, trxName);
      /** if (AdministrativeRequestsLine_ID == 0)
        {
			setAdministrativeRequests_ID (0);
			setAdministrativeRequestsLine_ID (0);
			setdatestartrequest (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_RH_AdministrativeRequestsLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AdministrativeRequestsLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_RH_AdministrativeRequests getAdministrativeRequests() throws RuntimeException
    {
		return (I_RH_AdministrativeRequests)MTable.get(getCtx(), I_RH_AdministrativeRequests.Table_Name)
			.getPO(getAdministrativeRequests_ID(), get_TrxName());	}

	/** Set AdministrativeRequests_ID.
		@param AdministrativeRequests_ID AdministrativeRequests_ID	  */
	public void setAdministrativeRequests_ID (int AdministrativeRequests_ID)
	{
		if (AdministrativeRequests_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AdministrativeRequests_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AdministrativeRequests_ID, Integer.valueOf(AdministrativeRequests_ID));
	}

	/** Get AdministrativeRequests_ID.
		@return AdministrativeRequests_ID	  */
	public int getAdministrativeRequests_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AdministrativeRequests_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AdministrativeRequestsLine.
		@param AdministrativeRequestsLine_ID AdministrativeRequestsLine	  */
	public void setAdministrativeRequestsLine_ID (int AdministrativeRequestsLine_ID)
	{
		if (AdministrativeRequestsLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AdministrativeRequestsLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AdministrativeRequestsLine_ID, Integer.valueOf(AdministrativeRequestsLine_ID));
	}

	/** Get AdministrativeRequestsLine.
		@return AdministrativeRequestsLine	  */
	public int getAdministrativeRequestsLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AdministrativeRequestsLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** block AD_Reference_ID=1000032 */
	public static final int BLOCK_AD_Reference_ID=1000032;
	/** 1/2 Día Mañana = MDM */
	public static final String BLOCK_12DiaManana = "MDM";
	/** 1/2 Día Tarde = MDT */
	public static final String BLOCK_12DiaTarde = "MDT";
	/** Set block.
		@param block block	  */
	public void setblock (String block)
	{

		set_Value (COLUMNNAME_block, block);
	}

	/** Get block.
		@return block	  */
	public String getblock () 
	{
		return (String)get_Value(COLUMNNAME_block);
	}

	/** Set datestartrequest.
		@param datestartrequest datestartrequest	  */
	public void setdatestartrequest (Timestamp datestartrequest)
	{
		set_Value (COLUMNNAME_datestartrequest, datestartrequest);
	}

	/** Get datestartrequest.
		@return datestartrequest	  */
	public Timestamp getdatestartrequest () 
	{
		return (Timestamp)get_Value(COLUMNNAME_datestartrequest);
	}

	/** Set numberhours.
		@param numberhours numberhours	  */
	public void setnumberhours (BigDecimal numberhours)
	{
		set_Value (COLUMNNAME_numberhours, numberhours);
	}

	/** Get numberhours.
		@return numberhours	  */
	public BigDecimal getnumberhours () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_numberhours);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}