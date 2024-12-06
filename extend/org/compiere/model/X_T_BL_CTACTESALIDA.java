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
import org.compiere.util.KeyNamePair;

/** Generated Model for T_BL_CTACTESALIDA
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_CTACTESALIDA extends PO implements I_T_BL_CTACTESALIDA, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180820L;

    /** Standard Constructor */
    public X_T_BL_CTACTESALIDA (Properties ctx, int T_BL_CTACTESALIDA_ID, String trxName)
    {
      super (ctx, T_BL_CTACTESALIDA_ID, trxName);
      /** if (T_BL_CTACTESALIDA_ID == 0)
        {
			setT_BL_CTACTESALIDA_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_CTACTESALIDA (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_CTACTESALIDA[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ABIERTA.
		@param ABIERTA ABIERTA	  */
	public void setABIERTA (boolean ABIERTA)
	{
		set_ValueNoCheck (COLUMNNAME_ABIERTA, Boolean.valueOf(ABIERTA));
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

	public I_AD_Table getAD_Table() throws RuntimeException
    {
		return (I_AD_Table)MTable.get(getCtx(), I_AD_Table.Table_Name)
			.getPO(getAD_Table_ID(), get_TrxName());	}

	/** Set Table.
		@param AD_Table_ID 
		Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1) 
			set_Value (COLUMNNAME_AD_Table_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Accounted Credit.
		@param AmtAcctCr 
		Accounted Credit Amount
	  */
	public void setAmtAcctCr (BigDecimal AmtAcctCr)
	{
		set_Value (COLUMNNAME_AmtAcctCr, AmtAcctCr);
	}

	/** Get Accounted Credit.
		@return Accounted Credit Amount
	  */
	public BigDecimal getAmtAcctCr () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtAcctCr);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Accounted Debit.
		@param AmtAcctDr 
		Accounted Debit Amount
	  */
	public void setAmtAcctDr (BigDecimal AmtAcctDr)
	{
		set_Value (COLUMNNAME_AmtAcctDr, AmtAcctDr);
	}

	/** Get Accounted Debit.
		@return Accounted Debit Amount
	  */
	public BigDecimal getAmtAcctDr () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtAcctDr);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Source Credit.
		@param AmtSourceCr 
		Source Credit Amount
	  */
	public void setAmtSourceCr (BigDecimal AmtSourceCr)
	{
		set_Value (COLUMNNAME_AmtSourceCr, AmtSourceCr);
	}

	/** Get Source Credit.
		@return Source Credit Amount
	  */
	public BigDecimal getAmtSourceCr () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtSourceCr);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Source Debit.
		@param AmtSourceDr 
		Source Debit Amount
	  */
	public void setAmtSourceDr (BigDecimal AmtSourceDr)
	{
		set_Value (COLUMNNAME_AmtSourceDr, AmtSourceDr);
	}

	/** Get Source Debit.
		@return Source Debit Amount
	  */
	public BigDecimal getAmtSourceDr () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtSourceDr);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	public I_C_Currency getC_Currency() throws RuntimeException
    {
		return (I_C_Currency)MTable.get(getCtx(), I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ElementValue getC_ElementValue() throws RuntimeException
    {
		return (I_C_ElementValue)MTable.get(getCtx(), I_C_ElementValue.Table_Name)
			.getPO(getC_ElementValue_ID(), get_TrxName());	}

	/** Set Account Element.
		@param C_ElementValue_ID 
		Account Element
	  */
	public void setC_ElementValue_ID (int C_ElementValue_ID)
	{
		if (C_ElementValue_ID < 1) 
			set_Value (COLUMNNAME_C_ElementValue_ID, null);
		else 
			set_Value (COLUMNNAME_C_ElementValue_ID, Integer.valueOf(C_ElementValue_ID));
	}

	/** Get Account Element.
		@return Account Element
	  */
	public int getC_ElementValue_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ElementValue_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CLIENTNOMBRE.
		@param CLIENTNOMBRE CLIENTNOMBRE	  */
	public void setCLIENTNOMBRE (String CLIENTNOMBRE)
	{
		set_Value (COLUMNNAME_CLIENTNOMBRE, CLIENTNOMBRE);
	}

	/** Get CLIENTNOMBRE.
		@return CLIENTNOMBRE	  */
	public String getCLIENTNOMBRE () 
	{
		return (String)get_Value(COLUMNNAME_CLIENTNOMBRE);
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

	/** Set Correlativo.
		@param Correlativo Correlativo	  */
	public void setCorrelativo (int Correlativo)
	{
		set_ValueNoCheck (COLUMNNAME_Correlativo, Integer.valueOf(Correlativo));
	}

	/** Get Correlativo.
		@return Correlativo	  */
	public int getCorrelativo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Correlativo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Period getC_Period() throws RuntimeException
    {
		return (I_C_Period)MTable.get(getCtx(), I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		set_Value (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
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

	/** Set DESDE.
		@param DESDE DESDE	  */
	public void setDESDE (Timestamp DESDE)
	{
		set_ValueNoCheck (COLUMNNAME_DESDE, DESDE);
	}

	/** Get DESDE.
		@return DESDE	  */
	public Timestamp getDESDE () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DESDE);
	}

	/** Set DES_TABLA.
		@param DES_TABLA DES_TABLA	  */
	public void setDES_TABLA (String DES_TABLA)
	{
		set_Value (COLUMNNAME_DES_TABLA, DES_TABLA);
	}

	/** Get DES_TABLA.
		@return DES_TABLA	  */
	public String getDES_TABLA () 
	{
		return (String)get_Value(COLUMNNAME_DES_TABLA);
	}

	/** Set DIGITO.
		@param DIGITO DIGITO	  */
	public void setDIGITO (String DIGITO)
	{
		set_Value (COLUMNNAME_DIGITO, DIGITO);
	}

	/** Get DIGITO.
		@return DIGITO	  */
	public String getDIGITO () 
	{
		return (String)get_Value(COLUMNNAME_DIGITO);
	}

	/** Set FECHA_DCTO.
		@param FECHA_DCTO FECHA_DCTO	  */
	public void setFECHA_DCTO (Timestamp FECHA_DCTO)
	{
		set_ValueNoCheck (COLUMNNAME_FECHA_DCTO, FECHA_DCTO);
	}

	/** Get FECHA_DCTO.
		@return FECHA_DCTO	  */
	public Timestamp getFECHA_DCTO () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FECHA_DCTO);
	}

	/** Set FECHA_VCTO.
		@param FECHA_VCTO FECHA_VCTO	  */
	public void setFECHA_VCTO (Timestamp FECHA_VCTO)
	{
		set_Value (COLUMNNAME_FECHA_VCTO, FECHA_VCTO);
	}

	/** Get FECHA_VCTO.
		@return FECHA_VCTO	  */
	public Timestamp getFECHA_VCTO () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FECHA_VCTO);
	}

	/** Set HASTA.
		@param HASTA HASTA	  */
	public void setHASTA (Timestamp HASTA)
	{
		set_ValueNoCheck (COLUMNNAME_HASTA, HASTA);
	}

	/** Get HASTA.
		@return HASTA	  */
	public Timestamp getHASTA () 
	{
		return (Timestamp)get_Value(COLUMNNAME_HASTA);
	}

	/** Set INFO_FACTURA.
		@param INFO_FACTURA INFO_FACTURA	  */
	public void setINFO_FACTURA (String INFO_FACTURA)
	{
		set_Value (COLUMNNAME_INFO_FACTURA, INFO_FACTURA);
	}

	/** Get INFO_FACTURA.
		@return INFO_FACTURA	  */
	public String getINFO_FACTURA () 
	{
		return (String)get_Value(COLUMNNAME_INFO_FACTURA);
	}

	/** Set ISO Currency Code.
		@param ISO_Code 
		Three letter ISO 4217 Code of the Currency
	  */
	public void setISO_Code (String ISO_Code)
	{
		set_Value (COLUMNNAME_ISO_Code, ISO_Code);
	}

	/** Get ISO Currency Code.
		@return Three letter ISO 4217 Code of the Currency
	  */
	public String getISO_Code () 
	{
		return (String)get_Value(COLUMNNAME_ISO_Code);
	}

	/** Set MONEDAREFERENCIA.
		@param MONEDAREFERENCIA MONEDAREFERENCIA	  */
	public void setMONEDAREFERENCIA (BigDecimal MONEDAREFERENCIA)
	{
		set_Value (COLUMNNAME_MONEDAREFERENCIA, MONEDAREFERENCIA);
	}

	/** Get MONEDAREFERENCIA.
		@return MONEDAREFERENCIA	  */
	public BigDecimal getMONEDAREFERENCIA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MONEDAREFERENCIA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set PERIODO.
		@param PERIODO PERIODO	  */
	public void setPERIODO (String PERIODO)
	{
		set_Value (COLUMNNAME_PERIODO, PERIODO);
	}

	/** Get PERIODO.
		@return PERIODO	  */
	public String getPERIODO () 
	{
		return (String)get_Value(COLUMNNAME_PERIODO);
	}

	/** Set RAZON_SOCIAL.
		@param RAZON_SOCIAL RAZON_SOCIAL	  */
	public void setRAZON_SOCIAL (String RAZON_SOCIAL)
	{
		set_Value (COLUMNNAME_RAZON_SOCIAL, RAZON_SOCIAL);
	}

	/** Get RAZON_SOCIAL.
		@return RAZON_SOCIAL	  */
	public String getRAZON_SOCIAL () 
	{
		return (String)get_Value(COLUMNNAME_RAZON_SOCIAL);
	}

	/** Set Record ID.
		@param Record_ID 
		Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID)
	{
		if (Record_ID < 0) 
			set_Value (COLUMNNAME_Record_ID, null);
		else 
			set_Value (COLUMNNAME_Record_ID, Integer.valueOf(Record_ID));
	}

	/** Get Record ID.
		@return Direct internal record ID
	  */
	public int getRecord_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Record_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set REFERENCIA.
		@param REFERENCIA REFERENCIA	  */
	public void setREFERENCIA (BigDecimal REFERENCIA)
	{
		set_Value (COLUMNNAME_REFERENCIA, REFERENCIA);
	}

	/** Get REFERENCIA.
		@return REFERENCIA	  */
	public BigDecimal getREFERENCIA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_REFERENCIA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set RUT.
		@param RUT RUT	  */
	public void setRUT (String RUT)
	{
		set_Value (COLUMNNAME_RUT, RUT);
	}

	/** Get RUT.
		@return RUT	  */
	public String getRUT () 
	{
		return (String)get_Value(COLUMNNAME_RUT);
	}

	/** Set SALDO.
		@param SALDO SALDO	  */
	public void setSALDO (BigDecimal SALDO)
	{
		set_Value (COLUMNNAME_SALDO, SALDO);
	}

	/** Get SALDO.
		@return SALDO	  */
	public BigDecimal getSALDO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDO);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set T_BL_CTACTEFORM.
		@param T_BL_CTACTEFORM_ID T_BL_CTACTEFORM	  */
	public void setT_BL_CTACTEFORM_ID (int T_BL_CTACTEFORM_ID)
	{
		if (T_BL_CTACTEFORM_ID < 1) 
			set_Value (COLUMNNAME_T_BL_CTACTEFORM_ID, null);
		else 
			set_Value (COLUMNNAME_T_BL_CTACTEFORM_ID, Integer.valueOf(T_BL_CTACTEFORM_ID));
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

	/** Set T_BL_CTACTESALIDA.
		@param T_BL_CTACTESALIDA_ID T_BL_CTACTESALIDA	  */
	public void setT_BL_CTACTESALIDA_ID (int T_BL_CTACTESALIDA_ID)
	{
		if (T_BL_CTACTESALIDA_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CTACTESALIDA_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CTACTESALIDA_ID, Integer.valueOf(T_BL_CTACTESALIDA_ID));
	}

	/** Get T_BL_CTACTESALIDA.
		@return T_BL_CTACTESALIDA	  */
	public int getT_BL_CTACTESALIDA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CTACTESALIDA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}