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

/** Generated Model for T_BL_CONEXI_PROY
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_CONEXI_PROY extends PO implements I_T_BL_CONEXI_PROY, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180525L;

    /** Standard Constructor */
    public X_T_BL_CONEXI_PROY (Properties ctx, int T_BL_CONEXI_PROY_ID, String trxName)
    {
      super (ctx, T_BL_CONEXI_PROY_ID, trxName);
      /** if (T_BL_CONEXI_PROY_ID == 0)
        {
			setT_BL_CONEXI_PROY_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_CONEXI_PROY (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_CONEXI_PROY[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ACTUALIZA_PRODUCTOS.
		@param ACTUALIZA_PRODUCTOS ACTUALIZA_PRODUCTOS	  */
	public void setACTUALIZA_PRODUCTOS (boolean ACTUALIZA_PRODUCTOS)
	{
		set_Value (COLUMNNAME_ACTUALIZA_PRODUCTOS, Boolean.valueOf(ACTUALIZA_PRODUCTOS));
	}

	/** Get ACTUALIZA_PRODUCTOS.
		@return ACTUALIZA_PRODUCTOS	  */
	public boolean isACTUALIZA_PRODUCTOS () 
	{
		Object oo = get_Value(COLUMNNAME_ACTUALIZA_PRODUCTOS);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ACTUALIZA_VENTAS.
		@param ACTUALIZA_VENTAS ACTUALIZA_VENTAS	  */
	public void setACTUALIZA_VENTAS (boolean ACTUALIZA_VENTAS)
	{
		set_Value (COLUMNNAME_ACTUALIZA_VENTAS, Boolean.valueOf(ACTUALIZA_VENTAS));
	}

	/** Get ACTUALIZA_VENTAS.
		@return ACTUALIZA_VENTAS	  */
	public boolean isACTUALIZA_VENTAS () 
	{
		Object oo = get_Value(COLUMNNAME_ACTUALIZA_VENTAS);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set COD_BLUMOS.
		@param COD_BLUMOS COD_BLUMOS	  */
	public void setCOD_BLUMOS (String COD_BLUMOS)
	{
		set_ValueNoCheck (COLUMNNAME_COD_BLUMOS, COD_BLUMOS);
	}

	/** Get COD_BLUMOS.
		@return COD_BLUMOS	  */
	public String getCOD_BLUMOS () 
	{
		return (String)get_Value(COLUMNNAME_COD_BLUMOS);
	}

	/** Set COMENTARIOS.
		@param COMENTARIOS COMENTARIOS	  */
	public void setCOMENTARIOS (String COMENTARIOS)
	{
		set_Value (COLUMNNAME_COMENTARIOS, COMENTARIOS);
	}

	/** Get COMENTARIOS.
		@return COMENTARIOS	  */
	public String getCOMENTARIOS () 
	{
		return (String)get_Value(COLUMNNAME_COMENTARIOS);
	}

	/** Set CORREO_NACIONAL.
		@param CORREO_NACIONAL CORREO_NACIONAL	  */
	public void setCORREO_NACIONAL (boolean CORREO_NACIONAL)
	{
		set_Value (COLUMNNAME_CORREO_NACIONAL, Boolean.valueOf(CORREO_NACIONAL));
	}

	/** Get CORREO_NACIONAL.
		@return CORREO_NACIONAL	  */
	public boolean isCORREO_NACIONAL () 
	{
		Object oo = get_Value(COLUMNNAME_CORREO_NACIONAL);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set LOG_CONTROL.
		@param LOG_CONTROL LOG_CONTROL	  */
	public void setLOG_CONTROL (String LOG_CONTROL)
	{
		set_Value (COLUMNNAME_LOG_CONTROL, LOG_CONTROL);
	}

	/** Get LOG_CONTROL.
		@return LOG_CONTROL	  */
	public String getLOG_CONTROL () 
	{
		return (String)get_Value(COLUMNNAME_LOG_CONTROL);
	}

	/** Set PRODUCTO_SOLUTEC.
		@param PRODUCTO_SOLUTEC PRODUCTO_SOLUTEC	  */
	public void setPRODUCTO_SOLUTEC (boolean PRODUCTO_SOLUTEC)
	{
		set_Value (COLUMNNAME_PRODUCTO_SOLUTEC, Boolean.valueOf(PRODUCTO_SOLUTEC));
	}

	/** Get PRODUCTO_SOLUTEC.
		@return PRODUCTO_SOLUTEC	  */
	public boolean isPRODUCTO_SOLUTEC () 
	{
		Object oo = get_Value(COLUMNNAME_PRODUCTO_SOLUTEC);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_AD_User getRESPONSABLE() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getRESPONSABLE_ID(), get_TrxName());	}

	/** Set RESPONSABLE_ID.
		@param RESPONSABLE_ID RESPONSABLE_ID	  */
	public void setRESPONSABLE_ID (int RESPONSABLE_ID)
	{
		if (RESPONSABLE_ID < 1) 
			set_Value (COLUMNNAME_RESPONSABLE_ID, null);
		else 
			set_Value (COLUMNNAME_RESPONSABLE_ID, Integer.valueOf(RESPONSABLE_ID));
	}

	/** Get RESPONSABLE_ID.
		@return RESPONSABLE_ID	  */
	public int getRESPONSABLE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RESPONSABLE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set T_BL_CONEXI_PROY.
		@param T_BL_CONEXI_PROY_ID T_BL_CONEXI_PROY	  */
	public void setT_BL_CONEXI_PROY_ID (int T_BL_CONEXI_PROY_ID)
	{
		if (T_BL_CONEXI_PROY_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_PROY_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_PROY_ID, Integer.valueOf(T_BL_CONEXI_PROY_ID));
	}

	/** Get T_BL_CONEXI_PROY.
		@return T_BL_CONEXI_PROY	  */
	public int getT_BL_CONEXI_PROY_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CONEXI_PROY_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}