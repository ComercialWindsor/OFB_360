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
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for T_BL_CONEXI_PROYCLIENTE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_CONEXI_PROYCLIENTE extends PO implements I_T_BL_CONEXI_PROYCLIENTE, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180525L;

    /** Standard Constructor */
    public X_T_BL_CONEXI_PROYCLIENTE (Properties ctx, int T_BL_CONEXI_PROYCLIENTE_ID, String trxName)
    {
      super (ctx, T_BL_CONEXI_PROYCLIENTE_ID, trxName);
      /** if (T_BL_CONEXI_PROYCLIENTE_ID == 0)
        {
			setT_BL_CONEXI_PROYCLIENTE_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_CONEXI_PROYCLIENTE (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_CONEXI_PROYCLIENTE[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set H0.
		@param H0 H0	  */
	public void setH0 (BigDecimal H0)
	{
		set_Value (COLUMNNAME_H0, H0);
	}

	/** Get H0.
		@return H0	  */
	public BigDecimal getH0 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H0);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H1.
		@param H1 H1	  */
	public void setH1 (BigDecimal H1)
	{
		set_Value (COLUMNNAME_H1, H1);
	}

	/** Get H1.
		@return H1	  */
	public BigDecimal getH1 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H1);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H10.
		@param H10 H10	  */
	public void setH10 (BigDecimal H10)
	{
		set_Value (COLUMNNAME_H10, H10);
	}

	/** Get H10.
		@return H10	  */
	public BigDecimal getH10 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H10);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H11.
		@param H11 H11	  */
	public void setH11 (BigDecimal H11)
	{
		set_Value (COLUMNNAME_H11, H11);
	}

	/** Get H11.
		@return H11	  */
	public BigDecimal getH11 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H11);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H12.
		@param H12 H12	  */
	public void setH12 (BigDecimal H12)
	{
		set_Value (COLUMNNAME_H12, H12);
	}

	/** Get H12.
		@return H12	  */
	public BigDecimal getH12 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H12);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H2.
		@param H2 H2	  */
	public void setH2 (BigDecimal H2)
	{
		set_Value (COLUMNNAME_H2, H2);
	}

	/** Get H2.
		@return H2	  */
	public BigDecimal getH2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H3.
		@param H3 H3	  */
	public void setH3 (BigDecimal H3)
	{
		set_Value (COLUMNNAME_H3, H3);
	}

	/** Get H3.
		@return H3	  */
	public BigDecimal getH3 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H3);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H4.
		@param H4 H4	  */
	public void setH4 (BigDecimal H4)
	{
		set_Value (COLUMNNAME_H4, H4);
	}

	/** Get H4.
		@return H4	  */
	public BigDecimal getH4 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H4);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H5.
		@param H5 H5	  */
	public void setH5 (BigDecimal H5)
	{
		set_Value (COLUMNNAME_H5, H5);
	}

	/** Get H5.
		@return H5	  */
	public BigDecimal getH5 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H5);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H6.
		@param H6 H6	  */
	public void setH6 (BigDecimal H6)
	{
		set_Value (COLUMNNAME_H6, H6);
	}

	/** Get H6.
		@return H6	  */
	public BigDecimal getH6 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H6);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H7.
		@param H7 H7	  */
	public void setH7 (BigDecimal H7)
	{
		set_Value (COLUMNNAME_H7, H7);
	}

	/** Get H7.
		@return H7	  */
	public BigDecimal getH7 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H7);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H8.
		@param H8 H8	  */
	public void setH8 (BigDecimal H8)
	{
		set_Value (COLUMNNAME_H8, H8);
	}

	/** Get H8.
		@return H8	  */
	public BigDecimal getH8 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H8);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set H9.
		@param H9 H9	  */
	public void setH9 (BigDecimal H9)
	{
		set_Value (COLUMNNAME_H9, H9);
	}

	/** Get H9.
		@return H9	  */
	public BigDecimal getH9 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_H9);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set NOMBRE_PRODUCTO.
		@param NOMBRE_PRODUCTO NOMBRE_PRODUCTO	  */
	public void setNOMBRE_PRODUCTO (String NOMBRE_PRODUCTO)
	{
		set_Value (COLUMNNAME_NOMBRE_PRODUCTO, NOMBRE_PRODUCTO);
	}

	/** Get NOMBRE_PRODUCTO.
		@return NOMBRE_PRODUCTO	  */
	public String getNOMBRE_PRODUCTO () 
	{
		return (String)get_Value(COLUMNNAME_NOMBRE_PRODUCTO);
	}

	/** Set P0.
		@param P0 P0	  */
	public void setP0 (BigDecimal P0)
	{
		set_Value (COLUMNNAME_P0, P0);
	}

	/** Get P0.
		@return P0	  */
	public BigDecimal getP0 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_P0);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set P1.
		@param P1 P1	  */
	public void setP1 (BigDecimal P1)
	{
		set_Value (COLUMNNAME_P1, P1);
	}

	/** Get P1.
		@return P1	  */
	public BigDecimal getP1 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_P1);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set P2.
		@param P2 P2	  */
	public void setP2 (BigDecimal P2)
	{
		set_Value (COLUMNNAME_P2, P2);
	}

	/** Get P2.
		@return P2	  */
	public BigDecimal getP2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_P2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set P3.
		@param P3 P3	  */
	public void setP3 (BigDecimal P3)
	{
		set_Value (COLUMNNAME_P3, P3);
	}

	/** Get P3.
		@return P3	  */
	public BigDecimal getP3 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_P3);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set P4.
		@param P4 P4	  */
	public void setP4 (BigDecimal P4)
	{
		set_Value (COLUMNNAME_P4, P4);
	}

	/** Get P4.
		@return P4	  */
	public BigDecimal getP4 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_P4);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set P5.
		@param P5 P5	  */
	public void setP5 (BigDecimal P5)
	{
		set_Value (COLUMNNAME_P5, P5);
	}

	/** Get P5.
		@return P5	  */
	public BigDecimal getP5 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_P5);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set T_BL_CONEXI_PROYCLIENTE.
		@param T_BL_CONEXI_PROYCLIENTE_ID T_BL_CONEXI_PROYCLIENTE	  */
	public void setT_BL_CONEXI_PROYCLIENTE_ID (int T_BL_CONEXI_PROYCLIENTE_ID)
	{
		if (T_BL_CONEXI_PROYCLIENTE_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_PROYCLIENTE_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_PROYCLIENTE_ID, Integer.valueOf(T_BL_CONEXI_PROYCLIENTE_ID));
	}

	/** Get T_BL_CONEXI_PROYCLIENTE.
		@return T_BL_CONEXI_PROYCLIENTE	  */
	public int getT_BL_CONEXI_PROYCLIENTE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CONEXI_PROYCLIENTE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
}