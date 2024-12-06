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

/** Generated Model for T_BL_CTACTEFORM
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_CTACTEFORM extends PO implements I_T_BL_CTACTEFORM, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180816L;

    /** Standard Constructor */
    public X_T_BL_CTACTEFORM (Properties ctx, int T_BL_CTACTEFORM_ID, String trxName)
    {
      super (ctx, T_BL_CTACTEFORM_ID, trxName);
      /** if (T_BL_CTACTEFORM_ID == 0)
        {
			setDESDE (new Timestamp( System.currentTimeMillis() ));
			setFECHA_CAMBIO (new Timestamp( System.currentTimeMillis() ));
			setHASTA (new Timestamp( System.currentTimeMillis() ));
			setT_BL_CTACTEFORM_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_CTACTEFORM (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_CTACTEFORM[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ABIERTA.
		@param ABIERTA ABIERTA	  */
	public void setABIERTA (boolean ABIERTA)
	{
		set_Value (COLUMNNAME_ABIERTA, Boolean.valueOf(ABIERTA));
	}

	/** Get ABIERTA.
		@return ABIERTA	  */
	public boolean isABIERTA () 
	{
		Object oo = get_Value(COLUMNNAME_ABIERTA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	public I_C_ElementValue getACCOUNT2() throws RuntimeException
    {
		return (I_C_ElementValue)MTable.get(getCtx(), I_C_ElementValue.Table_Name)
			.getPO(getACCOUNT2_ID(), get_TrxName());	}

	/** Set ACCOUNT2_ID.
		@param ACCOUNT2_ID ACCOUNT2_ID	  */
	public void setACCOUNT2_ID (int ACCOUNT2_ID)
	{
		if (ACCOUNT2_ID < 1) 
			set_Value (COLUMNNAME_ACCOUNT2_ID, null);
		else 
			set_Value (COLUMNNAME_ACCOUNT2_ID, Integer.valueOf(ACCOUNT2_ID));
	}

	/** Get ACCOUNT2_ID.
		@return ACCOUNT2_ID	  */
	public int getACCOUNT2_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ACCOUNT2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ElementValue getACCOUNT3() throws RuntimeException
    {
		return (I_C_ElementValue)MTable.get(getCtx(), I_C_ElementValue.Table_Name)
			.getPO(getACCOUNT3_ID(), get_TrxName());	}

	/** Set ACCOUNT3_ID.
		@param ACCOUNT3_ID ACCOUNT3_ID	  */
	public void setACCOUNT3_ID (int ACCOUNT3_ID)
	{
		if (ACCOUNT3_ID < 1) 
			set_Value (COLUMNNAME_ACCOUNT3_ID, null);
		else 
			set_Value (COLUMNNAME_ACCOUNT3_ID, Integer.valueOf(ACCOUNT3_ID));
	}

	/** Get ACCOUNT3_ID.
		@return ACCOUNT3_ID	  */
	public int getACCOUNT3_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ACCOUNT3_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ElementValue getACCOUNT4() throws RuntimeException
    {
		return (I_C_ElementValue)MTable.get(getCtx(), I_C_ElementValue.Table_Name)
			.getPO(getACCOUNT4_ID(), get_TrxName());	}

	/** Set ACCOUNT4_ID.
		@param ACCOUNT4_ID ACCOUNT4_ID	  */
	public void setACCOUNT4_ID (int ACCOUNT4_ID)
	{
		if (ACCOUNT4_ID < 1) 
			set_Value (COLUMNNAME_ACCOUNT4_ID, null);
		else 
			set_Value (COLUMNNAME_ACCOUNT4_ID, Integer.valueOf(ACCOUNT4_ID));
	}

	/** Get ACCOUNT4_ID.
		@return ACCOUNT4_ID	  */
	public int getACCOUNT4_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ACCOUNT4_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ElementValue getACCOUNT5() throws RuntimeException
    {
		return (I_C_ElementValue)MTable.get(getCtx(), I_C_ElementValue.Table_Name)
			.getPO(getACCOUNT5_ID(), get_TrxName());	}

	/** Set ACCOUNT5_ID.
		@param ACCOUNT5_ID ACCOUNT5_ID	  */
	public void setACCOUNT5_ID (int ACCOUNT5_ID)
	{
		if (ACCOUNT5_ID < 1) 
			set_Value (COLUMNNAME_ACCOUNT5_ID, null);
		else 
			set_Value (COLUMNNAME_ACCOUNT5_ID, Integer.valueOf(ACCOUNT5_ID));
	}

	/** Get ACCOUNT5_ID.
		@return ACCOUNT5_ID	  */
	public int getACCOUNT5_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ACCOUNT5_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_AcctSchema getC_AcctSchema() throws RuntimeException
    {
		return (I_C_AcctSchema)MTable.get(getCtx(), I_C_AcctSchema.Table_Name)
			.getPO(getC_AcctSchema_ID(), get_TrxName());	}

	/** Set Accounting Schema.
		@param C_AcctSchema_ID 
		Rules for accounting
	  */
	public void setC_AcctSchema_ID (int C_AcctSchema_ID)
	{
		if (C_AcctSchema_ID < 1) 
			set_Value (COLUMNNAME_C_AcctSchema_ID, null);
		else 
			set_Value (COLUMNNAME_C_AcctSchema_ID, Integer.valueOf(C_AcctSchema_ID));
	}

	/** Get Accounting Schema.
		@return Rules for accounting
	  */
	public int getC_AcctSchema_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_AcctSchema_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CALCULAR_DIF_CAMBIO.
		@param CALCULAR_DIF_CAMBIO CALCULAR_DIF_CAMBIO	  */
	public void setCALCULAR_DIF_CAMBIO (boolean CALCULAR_DIF_CAMBIO)
	{
		set_Value (COLUMNNAME_CALCULAR_DIF_CAMBIO, Boolean.valueOf(CALCULAR_DIF_CAMBIO));
	}

	/** Get CALCULAR_DIF_CAMBIO.
		@return CALCULAR_DIF_CAMBIO	  */
	public boolean isCALCULAR_DIF_CAMBIO () 
	{
		Object oo = get_Value(COLUMNNAME_CALCULAR_DIF_CAMBIO);
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

	public I_C_ConversionType getC_ConversionType() throws RuntimeException
    {
		return (I_C_ConversionType)MTable.get(getCtx(), I_C_ConversionType.Table_Name)
			.getPO(getC_ConversionType_ID(), get_TrxName());	}

	/** Set Currency Type.
		@param C_ConversionType_ID 
		Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID)
	{
		if (C_ConversionType_ID < 1) 
			set_Value (COLUMNNAME_C_ConversionType_ID, null);
		else 
			set_Value (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
	}

	/** Get Currency Type.
		@return Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
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

	/** Set EMITIR_REP_REVAL.
		@param EMITIR_REP_REVAL EMITIR_REP_REVAL	  */
	public void setEMITIR_REP_REVAL (String EMITIR_REP_REVAL)
	{
		set_Value (COLUMNNAME_EMITIR_REP_REVAL, EMITIR_REP_REVAL);
	}

	/** Get EMITIR_REP_REVAL.
		@return EMITIR_REP_REVAL	  */
	public String getEMITIR_REP_REVAL () 
	{
		return (String)get_Value(COLUMNNAME_EMITIR_REP_REVAL);
	}

	/** Set FECHA_CAMBIO.
		@param FECHA_CAMBIO FECHA_CAMBIO	  */
	public void setFECHA_CAMBIO (Timestamp FECHA_CAMBIO)
	{
		set_Value (COLUMNNAME_FECHA_CAMBIO, FECHA_CAMBIO);
	}

	/** Get FECHA_CAMBIO.
		@return FECHA_CAMBIO	  */
	public Timestamp getFECHA_CAMBIO () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FECHA_CAMBIO);
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

	/** Set PROCESAR.
		@param PROCESAR PROCESAR	  */
	public void setPROCESAR (boolean PROCESAR)
	{
		set_Value (COLUMNNAME_PROCESAR, Boolean.valueOf(PROCESAR));
	}

	/** Get PROCESAR.
		@return PROCESAR	  */
	public boolean isPROCESAR () 
	{
		Object oo = get_Value(COLUMNNAME_PROCESAR);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set T_BL_CTACTEFORM.
		@param T_BL_CTACTEFORM_ID T_BL_CTACTEFORM	  */
	public void setT_BL_CTACTEFORM_ID (int T_BL_CTACTEFORM_ID)
	{
		if (T_BL_CTACTEFORM_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CTACTEFORM_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CTACTEFORM_ID, Integer.valueOf(T_BL_CTACTEFORM_ID));
	}

	/** Get T_BL_CTACTEFORM.
		@return T_BL_CTACTEFORM	  */
	public int getT_BL_CTACTEFORM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CTACTEFORM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}