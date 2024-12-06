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

/** Generated Model for I_IMPORTCONSUMPTION
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_IMPORTCONSUMPTION extends PO implements I_I_IMPORTCONSUMPTION, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170220L;

    /** Standard Constructor */
    public X_I_IMPORTCONSUMPTION (Properties ctx, int I_IMPORTCONSUMPTION_ID, String trxName)
    {
      super (ctx, I_IMPORTCONSUMPTION_ID, trxName);
      /** if (I_IMPORTCONSUMPTION_ID == 0)
        {
			setI_IMPORTCONSUMPTION_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_IMPORTCONSUMPTION (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_IMPORTCONSUMPTION[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set almacen.
		@param almacen almacen	  */
	public void setalmacen (String almacen)
	{
		set_Value (COLUMNNAME_almacen, almacen);
	}

	/** Get almacen.
		@return almacen	  */
	public String getalmacen () 
	{
		return (String)get_Value(COLUMNNAME_almacen);
	}

	/** Set desc.
		@param desc desc	  */
	public void setdesc (String desc)
	{
		set_Value (COLUMNNAME_desc, desc);
	}

	/** Get desc.
		@return desc	  */
	public String getdesc () 
	{
		return (String)get_Value(COLUMNNAME_desc);
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

	/** Set fechadoc.
		@param fechadoc fechadoc	  */
	public void setfechadoc (String fechadoc)
	{
		set_Value (COLUMNNAME_fechadoc, fechadoc);
	}

	/** Get fechadoc.
		@return fechadoc	  */
	public String getfechadoc () 
	{
		return (String)get_Value(COLUMNNAME_fechadoc);
	}

	/** Set I_IMPORTCONSUMPTION_ID.
		@param I_IMPORTCONSUMPTION_ID I_IMPORTCONSUMPTION_ID	  */
	public void setI_IMPORTCONSUMPTION_ID (int I_IMPORTCONSUMPTION_ID)
	{
		if (I_IMPORTCONSUMPTION_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_IMPORTCONSUMPTION_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_IMPORTCONSUMPTION_ID, Integer.valueOf(I_IMPORTCONSUMPTION_ID));
	}

	/** Get I_IMPORTCONSUMPTION_ID.
		@return I_IMPORTCONSUMPTION_ID	  */
	public int getI_IMPORTCONSUMPTION_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_IMPORTCONSUMPTION_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Abort Process.
		@param IsAbort 
		Aborts the current process
	  */
	public void setIsAbort (boolean IsAbort)
	{
		set_Value (COLUMNNAME_IsAbort, Boolean.valueOf(IsAbort));
	}

	/** Get Abort Process.
		@return Aborts the current process
	  */
	public boolean isAbort () 
	{
		Object oo = get_Value(COLUMNNAME_IsAbort);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set nrodoc.
		@param nrodoc nrodoc	  */
	public void setnrodoc (String nrodoc)
	{
		set_Value (COLUMNNAME_nrodoc, nrodoc);
	}

	/** Get nrodoc.
		@return nrodoc	  */
	public String getnrodoc () 
	{
		return (String)get_Value(COLUMNNAME_nrodoc);
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