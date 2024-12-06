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

/** Generated Model for T_BL_VAC_MAESTRO
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_VAC_MAESTRO extends PO implements I_T_BL_VAC_MAESTRO, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180316L;

    /** Standard Constructor */
    public X_T_BL_VAC_MAESTRO (Properties ctx, int T_BL_VAC_MAESTRO_ID, String trxName)
    {
      super (ctx, T_BL_VAC_MAESTRO_ID, trxName);
      /** if (T_BL_VAC_MAESTRO_ID == 0)
        {
			setC_BPartner_ID (0);
			setT_BL_VAC_MAESTRO_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_VAC_MAESTRO (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_VAC_MAESTRO[")
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

	/** Set FECHA_CERTIFICADO.
		@param FECHA_CERTIFICADO FECHA_CERTIFICADO	  */
	public void setFECHA_CERTIFICADO (Timestamp FECHA_CERTIFICADO)
	{
		set_Value (COLUMNNAME_FECHA_CERTIFICADO, FECHA_CERTIFICADO);
	}

	/** Get FECHA_CERTIFICADO.
		@return FECHA_CERTIFICADO	  */
	public Timestamp getFECHA_CERTIFICADO () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FECHA_CERTIFICADO);
	}

	/** Set FECHA_CERTIFICADO_HASTA.
		@param FECHA_CERTIFICADO_HASTA FECHA_CERTIFICADO_HASTA	  */
	public void setFECHA_CERTIFICADO_HASTA (Timestamp FECHA_CERTIFICADO_HASTA)
	{
		set_Value (COLUMNNAME_FECHA_CERTIFICADO_HASTA, FECHA_CERTIFICADO_HASTA);
	}

	/** Get FECHA_CERTIFICADO_HASTA.
		@return FECHA_CERTIFICADO_HASTA	  */
	public Timestamp getFECHA_CERTIFICADO_HASTA () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FECHA_CERTIFICADO_HASTA);
	}

	/** Set FECHA_INGRESO.
		@param FECHA_INGRESO FECHA_INGRESO	  */
	public void setFECHA_INGRESO (Timestamp FECHA_INGRESO)
	{
		set_Value (COLUMNNAME_FECHA_INGRESO, FECHA_INGRESO);
	}

	/** Get FECHA_INGRESO.
		@return FECHA_INGRESO	  */
	public Timestamp getFECHA_INGRESO () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FECHA_INGRESO);
	}

	/** Set FINIQUITADO.
		@param FINIQUITADO FINIQUITADO	  */
	public void setFINIQUITADO (boolean FINIQUITADO)
	{
		set_Value (COLUMNNAME_FINIQUITADO, Boolean.valueOf(FINIQUITADO));
	}

	/** Get FINIQUITADO.
		@return FINIQUITADO	  */
	public boolean isFINIQUITADO () 
	{
		Object oo = get_Value(COLUMNNAME_FINIQUITADO);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IMPRIME_DETALLE.
		@param IMPRIME_DETALLE IMPRIME_DETALLE	  */
	public void setIMPRIME_DETALLE (String IMPRIME_DETALLE)
	{
		set_Value (COLUMNNAME_IMPRIME_DETALLE, IMPRIME_DETALLE);
	}

	/** Get IMPRIME_DETALLE.
		@return IMPRIME_DETALLE	  */
	public String getIMPRIME_DETALLE () 
	{
		return (String)get_Value(COLUMNNAME_IMPRIME_DETALLE);
	}

	/** Set MESES_RECONOCIDOS.
		@param MESES_RECONOCIDOS MESES_RECONOCIDOS	  */
	public void setMESES_RECONOCIDOS (BigDecimal MESES_RECONOCIDOS)
	{
		set_Value (COLUMNNAME_MESES_RECONOCIDOS, MESES_RECONOCIDOS);
	}

	/** Get MESES_RECONOCIDOS.
		@return MESES_RECONOCIDOS	  */
	public BigDecimal getMESES_RECONOCIDOS () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MESES_RECONOCIDOS);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set PRESENTA_CERTIFICADO.
		@param PRESENTA_CERTIFICADO PRESENTA_CERTIFICADO	  */
	public void setPRESENTA_CERTIFICADO (boolean PRESENTA_CERTIFICADO)
	{
		set_Value (COLUMNNAME_PRESENTA_CERTIFICADO, Boolean.valueOf(PRESENTA_CERTIFICADO));
	}

	/** Get PRESENTA_CERTIFICADO.
		@return PRESENTA_CERTIFICADO	  */
	public boolean isPRESENTA_CERTIFICADO () 
	{
		Object oo = get_Value(COLUMNNAME_PRESENTA_CERTIFICADO);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set T_BL_VAC_MAESTRO.
		@param T_BL_VAC_MAESTRO_ID T_BL_VAC_MAESTRO	  */
	public void setT_BL_VAC_MAESTRO_ID (int T_BL_VAC_MAESTRO_ID)
	{
		if (T_BL_VAC_MAESTRO_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_VAC_MAESTRO_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_VAC_MAESTRO_ID, Integer.valueOf(T_BL_VAC_MAESTRO_ID));
	}

	/** Get T_BL_VAC_MAESTRO.
		@return T_BL_VAC_MAESTRO	  */
	public int getT_BL_VAC_MAESTRO_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_VAC_MAESTRO_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}