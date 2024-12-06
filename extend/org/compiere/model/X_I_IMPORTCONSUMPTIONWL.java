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

/** Generated Model for I_IMPORTCONSUMPTIONWL
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_IMPORTCONSUMPTIONWL extends PO implements I_I_IMPORTCONSUMPTIONWL, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170220L;

    /** Standard Constructor */
    public X_I_IMPORTCONSUMPTIONWL (Properties ctx, int I_IMPORTCONSUMPTIONWL_ID, String trxName)
    {
      super (ctx, I_IMPORTCONSUMPTIONWL_ID, trxName);
      /** if (I_IMPORTCONSUMPTIONWL_ID == 0)
        {
			setI_ImportConsumptionWL_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_IMPORTCONSUMPTIONWL (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_IMPORTCONSUMPTIONWL[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set cant.
		@param cant cant	  */
	public void setcant (String cant)
	{
		set_Value (COLUMNNAME_cant, cant);
	}

	/** Get cant.
		@return cant	  */
	public String getcant () 
	{
		return (String)get_Value(COLUMNNAME_cant);
	}

	/** Set codprod.
		@param codprod codprod	  */
	public void setcodprod (String codprod)
	{
		set_Value (COLUMNNAME_codprod, codprod);
	}

	/** Get codprod.
		@return codprod	  */
	public String getcodprod () 
	{
		return (String)get_Value(COLUMNNAME_codprod);
	}

	/** Set costo.
		@param costo costo	  */
	public void setcosto (String costo)
	{
		set_Value (COLUMNNAME_costo, costo);
	}

	/** Get costo.
		@return costo	  */
	public String getcosto () 
	{
		return (String)get_Value(COLUMNNAME_costo);
	}

	/** Set I_ImportConsumptionWL.
		@param I_ImportConsumptionWL_ID I_ImportConsumptionWL	  */
	public void setI_ImportConsumptionWL_ID (int I_ImportConsumptionWL_ID)
	{
		if (I_ImportConsumptionWL_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_ImportConsumptionWL_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_ImportConsumptionWL_ID, Integer.valueOf(I_ImportConsumptionWL_ID));
	}

	/** Get I_ImportConsumptionWL.
		@return I_ImportConsumptionWL	  */
	public int getI_ImportConsumptionWL_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_ImportConsumptionWL_ID);
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

	/** Set tipomov.
		@param tipomov tipomov	  */
	public void settipomov (String tipomov)
	{
		set_Value (COLUMNNAME_tipomov, tipomov);
	}

	/** Get tipomov.
		@return tipomov	  */
	public String gettipomov () 
	{
		return (String)get_Value(COLUMNNAME_tipomov);
	}
}