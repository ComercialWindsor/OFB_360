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

/** Generated Model for T_BL_CONEXI_SESION_SALDOS
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_CONEXI_SESION_SALDOS extends PO implements I_T_BL_CONEXI_SESION_SALDOS, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180705L;

    /** Standard Constructor */
    public X_T_BL_CONEXI_SESION_SALDOS (Properties ctx, int T_BL_CONEXI_SESION_SALDOS_ID, String trxName)
    {
      super (ctx, T_BL_CONEXI_SESION_SALDOS_ID, trxName);
      /** if (T_BL_CONEXI_SESION_SALDOS_ID == 0)
        {
			setCANTIDAD_A_COMPRAR (Env.ZERO);
			setETA_COMPRA (new Timestamp( System.currentTimeMillis() ));
			setT_BL_CONEXI_SESION_SALDOS_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_CONEXI_SESION_SALDOS (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_CONEXI_SESION_SALDOS[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set CANTIDAD_A_COMPRAR.
		@param CANTIDAD_A_COMPRAR CANTIDAD_A_COMPRAR	  */
	public void setCANTIDAD_A_COMPRAR (BigDecimal CANTIDAD_A_COMPRAR)
	{
		set_Value (COLUMNNAME_CANTIDAD_A_COMPRAR, CANTIDAD_A_COMPRAR);
	}

	/** Get CANTIDAD_A_COMPRAR.
		@return CANTIDAD_A_COMPRAR	  */
	public BigDecimal getCANTIDAD_A_COMPRAR () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CANTIDAD_A_COMPRAR);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	public I_C_Order getC_Order() throws RuntimeException
    {
		return (I_C_Order)MTable.get(getCtx(), I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_Value (COLUMNNAME_C_Order_ID, null);
		else 
			set_Value (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set ETA_COMPRA.
		@param ETA_COMPRA ETA_COMPRA	  */
	public void setETA_COMPRA (Timestamp ETA_COMPRA)
	{
		set_Value (COLUMNNAME_ETA_COMPRA, ETA_COMPRA);
	}

	/** Get ETA_COMPRA.
		@return ETA_COMPRA	  */
	public Timestamp getETA_COMPRA () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ETA_COMPRA);
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

	/** Set QUIERECOMPRAR.
		@param QUIERECOMPRAR QUIERECOMPRAR	  */
	public void setQUIERECOMPRAR (boolean QUIERECOMPRAR)
	{
		set_Value (COLUMNNAME_QUIERECOMPRAR, Boolean.valueOf(QUIERECOMPRAR));
	}

	/** Get QUIERECOMPRAR.
		@return QUIERECOMPRAR	  */
	public boolean isQUIERECOMPRAR () 
	{
		Object oo = get_Value(COLUMNNAME_QUIERECOMPRAR);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_T_BL_CONEXI_SESION getT_BL_CONEXI_SESION() throws RuntimeException
    {
		return (I_T_BL_CONEXI_SESION)MTable.get(getCtx(), I_T_BL_CONEXI_SESION.Table_Name)
			.getPO(getT_BL_CONEXI_SESION_ID(), get_TrxName());	}

	/** Set T_BL_CONEXI_SESION.
		@param T_BL_CONEXI_SESION_ID T_BL_CONEXI_SESION	  */
	public void setT_BL_CONEXI_SESION_ID (int T_BL_CONEXI_SESION_ID)
	{
		if (T_BL_CONEXI_SESION_ID < 1) 
			set_Value (COLUMNNAME_T_BL_CONEXI_SESION_ID, null);
		else 
			set_Value (COLUMNNAME_T_BL_CONEXI_SESION_ID, Integer.valueOf(T_BL_CONEXI_SESION_ID));
	}

	/** Get T_BL_CONEXI_SESION.
		@return T_BL_CONEXI_SESION	  */
	public int getT_BL_CONEXI_SESION_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CONEXI_SESION_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set T_BL_CONEXI_SESION_SALDOS.
		@param T_BL_CONEXI_SESION_SALDOS_ID T_BL_CONEXI_SESION_SALDOS	  */
	public void setT_BL_CONEXI_SESION_SALDOS_ID (int T_BL_CONEXI_SESION_SALDOS_ID)
	{
		if (T_BL_CONEXI_SESION_SALDOS_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_SESION_SALDOS_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_SESION_SALDOS_ID, Integer.valueOf(T_BL_CONEXI_SESION_SALDOS_ID));
	}

	/** Get T_BL_CONEXI_SESION_SALDOS.
		@return T_BL_CONEXI_SESION_SALDOS	  */
	public int getT_BL_CONEXI_SESION_SALDOS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CONEXI_SESION_SALDOS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}