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

/** Generated Model for T_BL_CARTERA_PROYECTO
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_CARTERA_PROYECTO extends PO implements I_T_BL_CARTERA_PROYECTO, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180405L;

    /** Standard Constructor */
    public X_T_BL_CARTERA_PROYECTO (Properties ctx, int T_BL_CARTERA_PROYECTO_ID, String trxName)
    {
      super (ctx, T_BL_CARTERA_PROYECTO_ID, trxName);
      /** if (T_BL_CARTERA_PROYECTO_ID == 0)
        {
			setT_BL_CARTERA_PROYECTO_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_CARTERA_PROYECTO (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_CARTERA_PROYECTO[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_ProjectOFB getC_ProjectOFB() throws RuntimeException
    {
		return (I_C_ProjectOFB)MTable.get(getCtx(), I_C_ProjectOFB.Table_Name)
			.getPO(getC_ProjectOFB_ID(), get_TrxName());	}

	/** Set C_ProjectOFB_ID.
		@param C_ProjectOFB_ID C_ProjectOFB_ID	  */
	public void setC_ProjectOFB_ID (int C_ProjectOFB_ID)
	{
		if (C_ProjectOFB_ID < 1) 
			set_Value (COLUMNNAME_C_ProjectOFB_ID, null);
		else 
			set_Value (COLUMNNAME_C_ProjectOFB_ID, Integer.valueOf(C_ProjectOFB_ID));
	}

	/** Get C_ProjectOFB_ID.
		@return C_ProjectOFB_ID	  */
	public int getC_ProjectOFB_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectOFB_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_T_BL_CARTERA getT_BL_CARTERA() throws RuntimeException
    {
		return (I_T_BL_CARTERA)MTable.get(getCtx(), I_T_BL_CARTERA.Table_Name)
			.getPO(getT_BL_CARTERA_ID(), get_TrxName());	}

	/** Set T_BL_CARTERA.
		@param T_BL_CARTERA_ID T_BL_CARTERA	  */
	public void setT_BL_CARTERA_ID (int T_BL_CARTERA_ID)
	{
		if (T_BL_CARTERA_ID < 1) 
			set_Value (COLUMNNAME_T_BL_CARTERA_ID, null);
		else 
			set_Value (COLUMNNAME_T_BL_CARTERA_ID, Integer.valueOf(T_BL_CARTERA_ID));
	}

	/** Get T_BL_CARTERA.
		@return T_BL_CARTERA	  */
	public int getT_BL_CARTERA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CARTERA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set T_BL_CARTERA_PROYECTO.
		@param T_BL_CARTERA_PROYECTO_ID T_BL_CARTERA_PROYECTO	  */
	public void setT_BL_CARTERA_PROYECTO_ID (int T_BL_CARTERA_PROYECTO_ID)
	{
		if (T_BL_CARTERA_PROYECTO_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CARTERA_PROYECTO_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CARTERA_PROYECTO_ID, Integer.valueOf(T_BL_CARTERA_PROYECTO_ID));
	}

	/** Get T_BL_CARTERA_PROYECTO.
		@return T_BL_CARTERA_PROYECTO	  */
	public int getT_BL_CARTERA_PROYECTO_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CARTERA_PROYECTO_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}