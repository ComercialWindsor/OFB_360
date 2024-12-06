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

/** Generated Model for T_BL_ER_PARAMETROS
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_ER_PARAMETROS extends PO implements I_T_BL_ER_PARAMETROS, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171117L;

    /** Standard Constructor */
    public X_T_BL_ER_PARAMETROS (Properties ctx, int T_BL_ER_PARAMETROS_ID, String trxName)
    {
      super (ctx, T_BL_ER_PARAMETROS_ID, trxName);
      /** if (T_BL_ER_PARAMETROS_ID == 0)
        {
			setACTIVA_CONSULTA (false);
			setDESDE_A (new Timestamp( System.currentTimeMillis() ));
			setDESDE_B (new Timestamp( System.currentTimeMillis() ));
			setHASTA_A (new Timestamp( System.currentTimeMillis() ));
			setHASTA_B (new Timestamp( System.currentTimeMillis() ));
			setT_BL_ER_PARAMETROS_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_ER_PARAMETROS (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_ER_PARAMETROS[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ACTIVA_CONSULTA.
		@param ACTIVA_CONSULTA ACTIVA_CONSULTA	  */
	public void setACTIVA_CONSULTA (boolean ACTIVA_CONSULTA)
	{
		set_Value (COLUMNNAME_ACTIVA_CONSULTA, Boolean.valueOf(ACTIVA_CONSULTA));
	}

	/** Get ACTIVA_CONSULTA.
		@return ACTIVA_CONSULTA	  */
	public boolean isACTIVA_CONSULTA () 
	{
		Object oo = get_Value(COLUMNNAME_ACTIVA_CONSULTA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set BANDERA_CONTROL.
		@param BANDERA_CONTROL BANDERA_CONTROL	  */
	public void setBANDERA_CONTROL (boolean BANDERA_CONTROL)
	{
		set_Value (COLUMNNAME_BANDERA_CONTROL, Boolean.valueOf(BANDERA_CONTROL));
	}

	/** Get BANDERA_CONTROL.
		@return BANDERA_CONTROL	  */
	public boolean isBANDERA_CONTROL () 
	{
		Object oo = get_Value(COLUMNNAME_BANDERA_CONTROL);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set BANDERA2.
		@param BANDERA2 BANDERA2	  */
	public void setBANDERA2 (boolean BANDERA2)
	{
		set_Value (COLUMNNAME_BANDERA2, Boolean.valueOf(BANDERA2));
	}

	/** Get BANDERA2.
		@return BANDERA2	  */
	public boolean isBANDERA2 () 
	{
		Object oo = get_Value(COLUMNNAME_BANDERA2);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set BOTON_BALANCE.
		@param BOTON_BALANCE BOTON_BALANCE	  */
	public void setBOTON_BALANCE (String BOTON_BALANCE)
	{
		set_Value (COLUMNNAME_BOTON_BALANCE, BOTON_BALANCE);
	}

	/** Get BOTON_BALANCE.
		@return BOTON_BALANCE	  */
	public String getBOTON_BALANCE () 
	{
		return (String)get_Value(COLUMNNAME_BOTON_BALANCE);
	}

	/** Set BOTON_BALANCE_TRIBUTARIO.
		@param BOTON_BALANCE_TRIBUTARIO BOTON_BALANCE_TRIBUTARIO	  */
	public void setBOTON_BALANCE_TRIBUTARIO (String BOTON_BALANCE_TRIBUTARIO)
	{
		set_Value (COLUMNNAME_BOTON_BALANCE_TRIBUTARIO, BOTON_BALANCE_TRIBUTARIO);
	}

	/** Get BOTON_BALANCE_TRIBUTARIO.
		@return BOTON_BALANCE_TRIBUTARIO	  */
	public String getBOTON_BALANCE_TRIBUTARIO () 
	{
		return (String)get_Value(COLUMNNAME_BOTON_BALANCE_TRIBUTARIO);
	}

	/** Set BOTON_ER.
		@param BOTON_ER BOTON_ER	  */
	public void setBOTON_ER (String BOTON_ER)
	{
		set_Value (COLUMNNAME_BOTON_ER, BOTON_ER);
	}

	/** Get BOTON_ER.
		@return BOTON_ER	  */
	public String getBOTON_ER () 
	{
		return (String)get_Value(COLUMNNAME_BOTON_ER);
	}

	/** Set DESDE_A.
		@param DESDE_A DESDE_A	  */
	public void setDESDE_A (Timestamp DESDE_A)
	{
		set_Value (COLUMNNAME_DESDE_A, DESDE_A);
	}

	/** Get DESDE_A.
		@return DESDE_A	  */
	public Timestamp getDESDE_A () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DESDE_A);
	}

	/** Set DESDE_B.
		@param DESDE_B DESDE_B	  */
	public void setDESDE_B (Timestamp DESDE_B)
	{
		set_Value (COLUMNNAME_DESDE_B, DESDE_B);
	}

	/** Get DESDE_B.
		@return DESDE_B	  */
	public Timestamp getDESDE_B () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DESDE_B);
	}

	/** Set HASTA_A.
		@param HASTA_A HASTA_A	  */
	public void setHASTA_A (Timestamp HASTA_A)
	{
		set_Value (COLUMNNAME_HASTA_A, HASTA_A);
	}

	/** Get HASTA_A.
		@return HASTA_A	  */
	public Timestamp getHASTA_A () 
	{
		return (Timestamp)get_Value(COLUMNNAME_HASTA_A);
	}

	/** Set HASTA_B.
		@param HASTA_B HASTA_B	  */
	public void setHASTA_B (Timestamp HASTA_B)
	{
		set_Value (COLUMNNAME_HASTA_B, HASTA_B);
	}

	/** Get HASTA_B.
		@return HASTA_B	  */
	public Timestamp getHASTA_B () 
	{
		return (Timestamp)get_Value(COLUMNNAME_HASTA_B);
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

	/** Set T_BL_ER_PARAMETROS.
		@param T_BL_ER_PARAMETROS_ID T_BL_ER_PARAMETROS	  */
	public void setT_BL_ER_PARAMETROS_ID (int T_BL_ER_PARAMETROS_ID)
	{
		if (T_BL_ER_PARAMETROS_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_ER_PARAMETROS_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_ER_PARAMETROS_ID, Integer.valueOf(T_BL_ER_PARAMETROS_ID));
	}

	/** Get T_BL_ER_PARAMETROS.
		@return T_BL_ER_PARAMETROS	  */
	public int getT_BL_ER_PARAMETROS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_ER_PARAMETROS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}