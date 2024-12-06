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
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for PM_Supervision
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_PM_Supervision extends PO implements I_PM_Supervision, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20131003L;

    /** Standard Constructor */
    public X_PM_Supervision (Properties ctx, int PM_Supervision_ID, String trxName)
    {
      super (ctx, PM_Supervision_ID, trxName);
      /** if (PM_Supervision_ID == 0)
        {
			setC_Project_ID (0);
			setPM_Supervision_ID (0);
        } */
    }

    /** Load Constructor */
    public X_PM_Supervision (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_PM_Supervision[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_Project getC_Project() throws RuntimeException
    {
		return (I_C_Project)MTable.get(getCtx(), I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_Value (COLUMNNAME_C_Project_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CodigoBip.
		@param CodigoBip CodigoBip	  */
	public void setCodigoBip (String CodigoBip)
	{
		throw new IllegalArgumentException ("CodigoBip is virtual column");	}

	/** Get CodigoBip.
		@return CodigoBip	  */
	public String getCodigoBip () 
	{
		return (String)get_Value(COLUMNNAME_CodigoBip);
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

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	public I_C_Project getpm_programming() throws RuntimeException
    {
		return (I_C_Project)MTable.get(getCtx(), I_C_Project.Table_Name)
			.getPO(getpm_programming_id(), get_TrxName());	}

	/** Set pm_programming_id.
		@param pm_programming_id pm_programming_id	  */
	public void setpm_programming_id (int pm_programming_id)
	{
		set_Value (COLUMNNAME_pm_programming_id, Integer.valueOf(pm_programming_id));
	}

	/** Get pm_programming_id.
		@return pm_programming_id	  */
	public int getpm_programming_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_pm_programming_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set PM_Supervision.
		@param PM_Supervision_ID PM_Supervision	  */
	public void setPM_Supervision_ID (int PM_Supervision_ID)
	{
		if (PM_Supervision_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PM_Supervision_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PM_Supervision_ID, Integer.valueOf(PM_Supervision_ID));
	}

	/** Get PM_Supervision.
		@return PM_Supervision	  */
	public int getPM_Supervision_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PM_Supervision_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Status AD_Reference_ID=1000055 */
	public static final int STATUS_AD_Reference_ID=1000055;
	/** Anular = AN */
	public static final String STATUS_Anular = "AN";
	/** Definida = DE */
	public static final String STATUS_Definida = "DE";
	/** Programada = PO */
	public static final String STATUS_Programada = "PO";
	/** Realizada = RE */
	public static final String STATUS_Realizada = "RE";
	/** Solicitada = SO */
	public static final String STATUS_Solicitada = "SO";
	/** Set Status.
		@param Status 
		Status of the currently running check
	  */
	public void setStatus (String Status)
	{

		set_Value (COLUMNNAME_Status, Status);
	}

	/** Get Status.
		@return Status of the currently running check
	  */
	public String getStatus () 
	{
		return (String)get_Value(COLUMNNAME_Status);
	}
}