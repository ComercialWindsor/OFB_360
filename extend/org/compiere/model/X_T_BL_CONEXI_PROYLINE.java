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

/** Generated Model for T_BL_CONEXI_PROYLINE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_CONEXI_PROYLINE extends PO implements I_T_BL_CONEXI_PROYLINE, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180525L;

    /** Standard Constructor */
    public X_T_BL_CONEXI_PROYLINE (Properties ctx, int T_BL_CONEXI_PROYLINE_ID, String trxName)
    {
      super (ctx, T_BL_CONEXI_PROYLINE_ID, trxName);
      /** if (T_BL_CONEXI_PROYLINE_ID == 0)
        {
			setCANTIDAD_P (0);
			setFECHA_P (new Timestamp( System.currentTimeMillis() ));
			setT_BL_CONEXI_PROYLINE_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_CONEXI_PROYLINE (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_CONEXI_PROYLINE[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set CANTIDAD_P.
		@param CANTIDAD_P CANTIDAD_P	  */
	public void setCANTIDAD_P (int CANTIDAD_P)
	{
		set_Value (COLUMNNAME_CANTIDAD_P, Integer.valueOf(CANTIDAD_P));
	}

	/** Get CANTIDAD_P.
		@return CANTIDAD_P	  */
	public int getCANTIDAD_P () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CANTIDAD_P);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set COD_BLUMOS.
		@param COD_BLUMOS COD_BLUMOS	  */
	public void setCOD_BLUMOS (String COD_BLUMOS)
	{
		set_Value (COLUMNNAME_COD_BLUMOS, COD_BLUMOS);
	}

	/** Get COD_BLUMOS.
		@return COD_BLUMOS	  */
	public String getCOD_BLUMOS () 
	{
		return (String)get_Value(COLUMNNAME_COD_BLUMOS);
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

	/** Set FECHA_P.
		@param FECHA_P FECHA_P	  */
	public void setFECHA_P (Timestamp FECHA_P)
	{
		set_Value (COLUMNNAME_FECHA_P, FECHA_P);
	}

	/** Get FECHA_P.
		@return FECHA_P	  */
	public Timestamp getFECHA_P () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FECHA_P);
	}

	public I_T_BL_CONEXI_PROY getT_BL_CONEXI_PROY() throws RuntimeException
    {
		return (I_T_BL_CONEXI_PROY)MTable.get(getCtx(), I_T_BL_CONEXI_PROY.Table_Name)
			.getPO(getT_BL_CONEXI_PROY_ID(), get_TrxName());	}

	/** Set T_BL_CONEXI_PROY.
		@param T_BL_CONEXI_PROY_ID T_BL_CONEXI_PROY	  */
	public void setT_BL_CONEXI_PROY_ID (int T_BL_CONEXI_PROY_ID)
	{
		if (T_BL_CONEXI_PROY_ID < 1) 
			set_Value (COLUMNNAME_T_BL_CONEXI_PROY_ID, null);
		else 
			set_Value (COLUMNNAME_T_BL_CONEXI_PROY_ID, Integer.valueOf(T_BL_CONEXI_PROY_ID));
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

	/** Set T_BL_CONEXI_PROYLINE.
		@param T_BL_CONEXI_PROYLINE_ID T_BL_CONEXI_PROYLINE	  */
	public void setT_BL_CONEXI_PROYLINE_ID (int T_BL_CONEXI_PROYLINE_ID)
	{
		if (T_BL_CONEXI_PROYLINE_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_PROYLINE_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_PROYLINE_ID, Integer.valueOf(T_BL_CONEXI_PROYLINE_ID));
	}

	/** Get T_BL_CONEXI_PROYLINE.
		@return T_BL_CONEXI_PROYLINE	  */
	public int getT_BL_CONEXI_PROYLINE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CONEXI_PROYLINE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}