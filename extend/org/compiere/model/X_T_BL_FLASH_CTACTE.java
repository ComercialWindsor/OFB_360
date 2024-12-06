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

/** Generated Model for T_BL_FLASH_CTACTE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_FLASH_CTACTE extends PO implements I_T_BL_FLASH_CTACTE, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180405L;

    /** Standard Constructor */
    public X_T_BL_FLASH_CTACTE (Properties ctx, int T_BL_FLASH_CTACTE_ID, String trxName)
    {
      super (ctx, T_BL_FLASH_CTACTE_ID, trxName);
      /** if (T_BL_FLASH_CTACTE_ID == 0)
        {
			setACTIVA_CONSULTA (false);
        } */
    }

    /** Load Constructor */
    public X_T_BL_FLASH_CTACTE (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_FLASH_CTACTE[")
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

	/** Set DESDE.
		@param DESDE DESDE	  */
	public void setDESDE (Timestamp DESDE)
	{
		set_Value (COLUMNNAME_DESDE, DESDE);
	}

	/** Get DESDE.
		@return DESDE	  */
	public Timestamp getDESDE () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DESDE);
	}

	/** Set HASTA.
		@param HASTA HASTA	  */
	public void setHASTA (Timestamp HASTA)
	{
		set_Value (COLUMNNAME_HASTA, HASTA);
	}

	/** Get HASTA.
		@return HASTA	  */
	public Timestamp getHASTA () 
	{
		return (Timestamp)get_Value(COLUMNNAME_HASTA);
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

	/** Set T_BL_FLASH_CTACTE.
		@param T_BL_FLASH_CTACTE_ID T_BL_FLASH_CTACTE	  */
	public void setT_BL_FLASH_CTACTE_ID (int T_BL_FLASH_CTACTE_ID)
	{
		if (T_BL_FLASH_CTACTE_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_CTACTE_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_CTACTE_ID, Integer.valueOf(T_BL_FLASH_CTACTE_ID));
	}

	/** Get T_BL_FLASH_CTACTE.
		@return T_BL_FLASH_CTACTE	  */
	public int getT_BL_FLASH_CTACTE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_FLASH_CTACTE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TOTAL_RES.
		@param TOTAL_RES TOTAL_RES	  */
	public void setTOTAL_RES (BigDecimal TOTAL_RES)
	{
		set_Value (COLUMNNAME_TOTAL_RES, TOTAL_RES);
	}

	/** Get TOTAL_RES.
		@return TOTAL_RES	  */
	public BigDecimal getTOTAL_RES () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TOTAL_RES);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}