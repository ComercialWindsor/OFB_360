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

/** Generated Model for HRO_Selection
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_HRO_Selection extends PO implements I_HRO_Selection, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140930L;

    /** Standard Constructor */
    public X_HRO_Selection (Properties ctx, int HRO_Selection_ID, String trxName)
    {
      super (ctx, HRO_Selection_ID, trxName);
      /** if (HRO_Selection_ID == 0)
        {
			setcartareco (false);
			setcv (false);
			setevaficha (false);
			setevapsico (false);
			setgrado (null);
			setHRO_CallEntries_ID (0);
			setHRO_Selection_ID (0);
			setName (null);
			setProcessed (false);
			setpuntaje (0);
			setStatus (null);
// IP
			settitulo (null);
        } */
    }

    /** Load Constructor */
    public X_HRO_Selection (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HRO_Selection[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set cartareco.
		@param cartareco cartareco	  */
	public void setcartareco (boolean cartareco)
	{
		set_Value (COLUMNNAME_cartareco, Boolean.valueOf(cartareco));
	}

	/** Get cartareco.
		@return cartareco	  */
	public boolean iscartareco () 
	{
		Object oo = get_Value(COLUMNNAME_cartareco);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set cv.
		@param cv cv	  */
	public void setcv (boolean cv)
	{
		set_Value (COLUMNNAME_cv, Boolean.valueOf(cv));
	}

	/** Get cv.
		@return cv	  */
	public boolean iscv () 
	{
		Object oo = get_Value(COLUMNNAME_cv);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set evaficha.
		@param evaficha evaficha	  */
	public void setevaficha (boolean evaficha)
	{
		set_Value (COLUMNNAME_evaficha, Boolean.valueOf(evaficha));
	}

	/** Get evaficha.
		@return evaficha	  */
	public boolean isevaficha () 
	{
		Object oo = get_Value(COLUMNNAME_evaficha);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set evapsico.
		@param evapsico evapsico	  */
	public void setevapsico (boolean evapsico)
	{
		set_Value (COLUMNNAME_evapsico, Boolean.valueOf(evapsico));
	}

	/** Get evapsico.
		@return evapsico	  */
	public boolean isevapsico () 
	{
		Object oo = get_Value(COLUMNNAME_evapsico);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set grado.
		@param grado grado	  */
	public void setgrado (String grado)
	{
		set_Value (COLUMNNAME_grado, grado);
	}

	/** Get grado.
		@return grado	  */
	public String getgrado () 
	{
		return (String)get_Value(COLUMNNAME_grado);
	}

	public I_HRO_CallEntries getHRO_CallEntries() throws RuntimeException
    {
		return (I_HRO_CallEntries)MTable.get(getCtx(), I_HRO_CallEntries.Table_Name)
			.getPO(getHRO_CallEntries_ID(), get_TrxName());	}

	/** Set HRO_CallEntries.
		@param HRO_CallEntries_ID HRO_CallEntries	  */
	public void setHRO_CallEntries_ID (int HRO_CallEntries_ID)
	{
		if (HRO_CallEntries_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HRO_CallEntries_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HRO_CallEntries_ID, Integer.valueOf(HRO_CallEntries_ID));
	}

	/** Get HRO_CallEntries.
		@return HRO_CallEntries	  */
	public int getHRO_CallEntries_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRO_CallEntries_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HRO_Selection.
		@param HRO_Selection_ID HRO_Selection	  */
	public void setHRO_Selection_ID (int HRO_Selection_ID)
	{
		if (HRO_Selection_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HRO_Selection_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HRO_Selection_ID, Integer.valueOf(HRO_Selection_ID));
	}

	/** Get HRO_Selection.
		@return HRO_Selection	  */
	public int getHRO_Selection_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRO_Selection_ID);
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

	/** Set puntaje.
		@param puntaje puntaje	  */
	public void setpuntaje (int puntaje)
	{
		set_Value (COLUMNNAME_puntaje, Integer.valueOf(puntaje));
	}

	/** Get puntaje.
		@return puntaje	  */
	public int getpuntaje () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_puntaje);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Status AD_Reference_ID=1000114 */
	public static final int STATUS_AD_Reference_ID=1000114;
	/** En Proceso = IP */
	public static final String STATUS_EnProceso = "IP";
	/** Seleccionado = SE */
	public static final String STATUS_Seleccionado = "SE";
	/** Descartado = DE */
	public static final String STATUS_Descartado = "DE";
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

	/** Set titulo.
		@param titulo titulo	  */
	public void settitulo (String titulo)
	{
		set_Value (COLUMNNAME_titulo, titulo);
	}

	/** Get titulo.
		@return titulo	  */
	public String gettitulo () 
	{
		return (String)get_Value(COLUMNNAME_titulo);
	}
}