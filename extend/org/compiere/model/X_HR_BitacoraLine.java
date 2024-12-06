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

/** Generated Model for HR_BitacoraLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_HR_BitacoraLine extends PO implements I_HR_BitacoraLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160303L;

    /** Standard Constructor */
    public X_HR_BitacoraLine (Properties ctx, int HR_BitacoraLine_ID, String trxName)
    {
      super (ctx, HR_BitacoraLine_ID, trxName);
      /** if (HR_BitacoraLine_ID == 0)
        {
			setHR_Bitacora_ID (0);
			setHR_BitacoraLine_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_HR_BitacoraLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_BitacoraLine[")
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

	public I_HR_Bitacora getHR_Bitacora() throws RuntimeException
    {
		return (I_HR_Bitacora)MTable.get(getCtx(), I_HR_Bitacora.Table_Name)
			.getPO(getHR_Bitacora_ID(), get_TrxName());	}

	/** Set HR_Bitacora.
		@param HR_Bitacora_ID HR_Bitacora	  */
	public void setHR_Bitacora_ID (int HR_Bitacora_ID)
	{
		if (HR_Bitacora_ID < 1) 
			set_Value (COLUMNNAME_HR_Bitacora_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Bitacora_ID, Integer.valueOf(HR_Bitacora_ID));
	}

	/** Get HR_Bitacora.
		@return HR_Bitacora	  */
	public int getHR_Bitacora_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Bitacora_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HR_BitacoraLine.
		@param HR_BitacoraLine_ID HR_BitacoraLine	  */
	public void setHR_BitacoraLine_ID (int HR_BitacoraLine_ID)
	{
		if (HR_BitacoraLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_BitacoraLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_BitacoraLine_ID, Integer.valueOf(HR_BitacoraLine_ID));
	}

	/** Get HR_BitacoraLine.
		@return HR_BitacoraLine	  */
	public int getHR_BitacoraLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_BitacoraLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HR_Prebitacora getHR_Prebitacora() throws RuntimeException
    {
		return (I_HR_Prebitacora)MTable.get(getCtx(), I_HR_Prebitacora.Table_Name)
			.getPO(getHR_Prebitacora_ID(), get_TrxName());	}

	/** Set HR_Prebitacora.
		@param HR_Prebitacora_ID HR_Prebitacora	  */
	public void setHR_Prebitacora_ID (int HR_Prebitacora_ID)
	{
		if (HR_Prebitacora_ID < 1) 
			set_Value (COLUMNNAME_HR_Prebitacora_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Prebitacora_ID, Integer.valueOf(HR_Prebitacora_ID));
	}

	/** Get HR_Prebitacora.
		@return HR_Prebitacora	  */
	public int getHR_Prebitacora_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Prebitacora_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Movement getM_Movement() throws RuntimeException
    {
		return (I_M_Movement)MTable.get(getCtx(), I_M_Movement.Table_Name)
			.getPO(getM_Movement_ID(), get_TrxName());	}

	/** Set Inventory Move.
		@param M_Movement_ID 
		Movement of Inventory
	  */
	public void setM_Movement_ID (int M_Movement_ID)
	{
		if (M_Movement_ID < 1) 
			set_Value (COLUMNNAME_M_Movement_ID, null);
		else 
			set_Value (COLUMNNAME_M_Movement_ID, Integer.valueOf(M_Movement_ID));
	}

	/** Get Inventory Move.
		@return Movement of Inventory
	  */
	public int getM_Movement_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Movement_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
}