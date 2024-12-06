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

/** Generated Model for T_BL_ER_SALIDA
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_ER_SALIDA extends PO implements I_T_BL_ER_SALIDA, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171117L;

    /** Standard Constructor */
    public X_T_BL_ER_SALIDA (Properties ctx, int T_BL_ER_SALIDA_ID, String trxName)
    {
      super (ctx, T_BL_ER_SALIDA_ID, trxName);
      /** if (T_BL_ER_SALIDA_ID == 0)
        {
			setT_BL_ER_PARAMETROS_ID (0);
			setT_BL_ER_SALIDA_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_ER_SALIDA (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_ER_SALIDA[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_ElementValue getAccount() throws RuntimeException
    {
		return (I_C_ElementValue)MTable.get(getCtx(), I_C_ElementValue.Table_Name)
			.getPO(getAccount_ID(), get_TrxName());	}

	/** Set Account.
		@param Account_ID 
		Account used
	  */
	public void setAccount_ID (int Account_ID)
	{
		if (Account_ID < 1) 
			set_Value (COLUMNNAME_Account_ID, null);
		else 
			set_Value (COLUMNNAME_Account_ID, Integer.valueOf(Account_ID));
	}

	/** Get Account.
		@return Account used
	  */
	public int getAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Account_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set BANDERA_GTOTAL.
		@param BANDERA_GTOTAL BANDERA_GTOTAL	  */
	public void setBANDERA_GTOTAL (boolean BANDERA_GTOTAL)
	{
		set_Value (COLUMNNAME_BANDERA_GTOTAL, Boolean.valueOf(BANDERA_GTOTAL));
	}

	/** Get BANDERA_GTOTAL.
		@return BANDERA_GTOTAL	  */
	public boolean isBANDERA_GTOTAL () 
	{
		Object oo = get_Value(COLUMNNAME_BANDERA_GTOTAL);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set CUENTA.
		@param CUENTA CUENTA	  */
	public void setCUENTA (String CUENTA)
	{
		set_Value (COLUMNNAME_CUENTA, CUENTA);
	}

	/** Get CUENTA.
		@return CUENTA	  */
	public String getCUENTA () 
	{
		return (String)get_Value(COLUMNNAME_CUENTA);
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

	/** Set GTOTAL_A.
		@param GTOTAL_A GTOTAL_A	  */
	public void setGTOTAL_A (BigDecimal GTOTAL_A)
	{
		set_Value (COLUMNNAME_GTOTAL_A, GTOTAL_A);
	}

	/** Get GTOTAL_A.
		@return GTOTAL_A	  */
	public BigDecimal getGTOTAL_A () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GTOTAL_A);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set GTOTAL_B.
		@param GTOTAL_B GTOTAL_B	  */
	public void setGTOTAL_B (BigDecimal GTOTAL_B)
	{
		set_Value (COLUMNNAME_GTOTAL_B, GTOTAL_B);
	}

	/** Get GTOTAL_B.
		@return GTOTAL_B	  */
	public BigDecimal getGTOTAL_B () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GTOTAL_B);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set NOMBRE_ABUELO.
		@param NOMBRE_ABUELO NOMBRE_ABUELO	  */
	public void setNOMBRE_ABUELO (String NOMBRE_ABUELO)
	{
		set_Value (COLUMNNAME_NOMBRE_ABUELO, NOMBRE_ABUELO);
	}

	/** Get NOMBRE_ABUELO.
		@return NOMBRE_ABUELO	  */
	public String getNOMBRE_ABUELO () 
	{
		return (String)get_Value(COLUMNNAME_NOMBRE_ABUELO);
	}

	/** Set NOMBRE_CUENTA.
		@param NOMBRE_CUENTA NOMBRE_CUENTA	  */
	public void setNOMBRE_CUENTA (String NOMBRE_CUENTA)
	{
		set_Value (COLUMNNAME_NOMBRE_CUENTA, NOMBRE_CUENTA);
	}

	/** Get NOMBRE_CUENTA.
		@return NOMBRE_CUENTA	  */
	public String getNOMBRE_CUENTA () 
	{
		return (String)get_Value(COLUMNNAME_NOMBRE_CUENTA);
	}

	/** Set NOMBRE_GTOTAL.
		@param NOMBRE_GTOTAL NOMBRE_GTOTAL	  */
	public void setNOMBRE_GTOTAL (String NOMBRE_GTOTAL)
	{
		set_Value (COLUMNNAME_NOMBRE_GTOTAL, NOMBRE_GTOTAL);
	}

	/** Get NOMBRE_GTOTAL.
		@return NOMBRE_GTOTAL	  */
	public String getNOMBRE_GTOTAL () 
	{
		return (String)get_Value(COLUMNNAME_NOMBRE_GTOTAL);
	}

	/** Set NOMBRE_PADRE.
		@param NOMBRE_PADRE NOMBRE_PADRE	  */
	public void setNOMBRE_PADRE (String NOMBRE_PADRE)
	{
		set_Value (COLUMNNAME_NOMBRE_PADRE, NOMBRE_PADRE);
	}

	/** Get NOMBRE_PADRE.
		@return NOMBRE_PADRE	  */
	public String getNOMBRE_PADRE () 
	{
		return (String)get_Value(COLUMNNAME_NOMBRE_PADRE);
	}

	/** Set SALDO_A.
		@param SALDO_A SALDO_A	  */
	public void setSALDO_A (BigDecimal SALDO_A)
	{
		set_Value (COLUMNNAME_SALDO_A, SALDO_A);
	}

	/** Get SALDO_A.
		@return SALDO_A	  */
	public BigDecimal getSALDO_A () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDO_A);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SALDO_B.
		@param SALDO_B SALDO_B	  */
	public void setSALDO_B (BigDecimal SALDO_B)
	{
		set_Value (COLUMNNAME_SALDO_B, SALDO_B);
	}

	/** Get SALDO_B.
		@return SALDO_B	  */
	public BigDecimal getSALDO_B () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDO_B);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_T_BL_ER_PARAMETROS getT_BL_ER_PARAMETROS() throws RuntimeException
    {
		return (I_T_BL_ER_PARAMETROS)MTable.get(getCtx(), I_T_BL_ER_PARAMETROS.Table_Name)
			.getPO(getT_BL_ER_PARAMETROS_ID(), get_TrxName());	}

	/** Set T_BL_ER_PARAMETROS.
		@param T_BL_ER_PARAMETROS_ID T_BL_ER_PARAMETROS	  */
	public void setT_BL_ER_PARAMETROS_ID (int T_BL_ER_PARAMETROS_ID)
	{
		if (T_BL_ER_PARAMETROS_ID < 1) 
			set_Value (COLUMNNAME_T_BL_ER_PARAMETROS_ID, null);
		else 
			set_Value (COLUMNNAME_T_BL_ER_PARAMETROS_ID, Integer.valueOf(T_BL_ER_PARAMETROS_ID));
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

	/** Set T_BL_ER_SALIDA.
		@param T_BL_ER_SALIDA_ID T_BL_ER_SALIDA	  */
	public void setT_BL_ER_SALIDA_ID (int T_BL_ER_SALIDA_ID)
	{
		if (T_BL_ER_SALIDA_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_ER_SALIDA_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_ER_SALIDA_ID, Integer.valueOf(T_BL_ER_SALIDA_ID));
	}

	/** Get T_BL_ER_SALIDA.
		@return T_BL_ER_SALIDA	  */
	public int getT_BL_ER_SALIDA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_ER_SALIDA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}