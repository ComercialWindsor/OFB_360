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
import org.compiere.util.KeyNamePair;

/** Generated Model for T_BL_REF_DTE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_REF_DTE extends PO implements I_T_BL_REF_DTE, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181010L;

    /** Standard Constructor */
    public X_T_BL_REF_DTE (Properties ctx, int T_BL_REF_DTE_ID, String trxName)
    {
      super (ctx, T_BL_REF_DTE_ID, trxName);
      /** if (T_BL_REF_DTE_ID == 0)
        {
			setC_Invoice_ID (0);
			setCOD_REF (null);
			setDATE_REF (new Timestamp( System.currentTimeMillis() ));
// @SQL=SELECT DateInvoiced AS DefaultValue FROM C_Invoice WHERE DocumentNO=@DOC_REFERENCIADO@
			setLine (0);
// @SQL=SELECT NVL(MAX(Line),0)+10 AS DefaultValue FROM T_BL_REF_DTE WHERE C_Invoice_ID=@C_Invoice_ID@
			setRAZON_REF (null);
			setT_BL_REF_DTE_ID (0);
			setTIPO_DOC_REF (null);
        } */
    }

    /** Load Constructor */
    public X_T_BL_REF_DTE (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_REF_DTE[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (I_C_Invoice)MTable.get(getCtx(), I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Invoice_ID()));
    }

	/** COD_REF AD_Reference_ID=1000032 */
	public static final int COD_REF_AD_Reference_ID=1000032;
	/** Anula = 1 */
	public static final String COD_REF_Anula = "1";
	/** Corrige Texto = 2 */
	public static final String COD_REF_CorrigeTexto = "2";
	/** Corrige Monto = 3 */
	public static final String COD_REF_CorrigeMonto = "3";
	/** Set COD_REF.
		@param COD_REF COD_REF	  */
	public void setCOD_REF (String COD_REF)
	{

		set_Value (COLUMNNAME_COD_REF, COD_REF);
	}

	/** Get COD_REF.
		@return COD_REF	  */
	public String getCOD_REF () 
	{
		return (String)get_Value(COLUMNNAME_COD_REF);
	}

	/** Set DATE_REF.
		@param DATE_REF DATE_REF	  */
	public void setDATE_REF (Timestamp DATE_REF)
	{
		set_Value (COLUMNNAME_DATE_REF, DATE_REF);
	}

	/** Get DATE_REF.
		@return DATE_REF	  */
	public Timestamp getDATE_REF () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DATE_REF);
	}

	/** Set DOC_REFERENCIADO.
		@param DOC_REFERENCIADO DOC_REFERENCIADO	  */
	public void setDOC_REFERENCIADO (String DOC_REFERENCIADO)
	{
		set_Value (COLUMNNAME_DOC_REFERENCIADO, DOC_REFERENCIADO);
	}

	/** Get DOC_REFERENCIADO.
		@return DOC_REFERENCIADO	  */
	public String getDOC_REFERENCIADO () 
	{
		return (String)get_Value(COLUMNNAME_DOC_REFERENCIADO);
	}

	public I_C_Invoice getID_DOC_() throws RuntimeException
    {
		return (I_C_Invoice)MTable.get(getCtx(), I_C_Invoice.Table_Name)
			.getPO(getID_DOC_REF(), get_TrxName());	}

	/** Set ID_DOC_REF.
		@param ID_DOC_REF ID_DOC_REF	  */
	public void setID_DOC_REF (int ID_DOC_REF)
	{
		set_Value (COLUMNNAME_ID_DOC_REF, Integer.valueOf(ID_DOC_REF));
	}

	/** Get ID_DOC_REF.
		@return ID_DOC_REF	  */
	public int getID_DOC_REF () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ID_DOC_REF);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (int Line)
	{
		set_Value (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set RAZON_REF.
		@param RAZON_REF RAZON_REF	  */
	public void setRAZON_REF (String RAZON_REF)
	{
		set_Value (COLUMNNAME_RAZON_REF, RAZON_REF);
	}

	/** Get RAZON_REF.
		@return RAZON_REF	  */
	public String getRAZON_REF () 
	{
		return (String)get_Value(COLUMNNAME_RAZON_REF);
	}

	/** Set T_BL_REF_DTE.
		@param T_BL_REF_DTE_ID T_BL_REF_DTE	  */
	public void setT_BL_REF_DTE_ID (int T_BL_REF_DTE_ID)
	{
		if (T_BL_REF_DTE_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_REF_DTE_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_REF_DTE_ID, Integer.valueOf(T_BL_REF_DTE_ID));
	}

	/** Get T_BL_REF_DTE.
		@return T_BL_REF_DTE	  */
	public int getT_BL_REF_DTE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_REF_DTE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** TIPO_DOC_REF AD_Reference_ID=1000031 */
	public static final int TIPO_DOC_REF_AD_Reference_ID=1000031;
	/** Factura de Venta Manual = 30 */
	public static final String TIPO_DOC_REF_FacturaDeVentaManual = "30";
	/** Factura de Compra Manual = 45 */
	public static final String TIPO_DOC_REF_FacturaDeCompraManual = "45";
	/** Nota de Débito manual = 55 */
	public static final String TIPO_DOC_REF_NotaDeDebitoManual = "55";
	/** Nota de Crédito Manual = 60 */
	public static final String TIPO_DOC_REF_NotaDeCreditoManual = "60";
	/** Liquidación de Factura = 40 */
	public static final String TIPO_DOC_REF_LiquidacionDeFactura = "40";
	/** Liquidación de Comisionista distribuidor = 103 */
	public static final String TIPO_DOC_REF_LiquidacionDeComisionistaDistribuidor = "103";
	/** Factura Exenta Manual = 32 */
	public static final String TIPO_DOC_REF_FacturaExentaManual = "32";
	/** Factura de Ventas Electrónica = 33 */
	public static final String TIPO_DOC_REF_FacturaDeVentasElectronica = "33";
	/** Factura Exenta electrónica = 34 */
	public static final String TIPO_DOC_REF_FacturaExentaElectronica = "34";
	/** Factura de Compra Electrónica = 46 */
	public static final String TIPO_DOC_REF_FacturaDeCompraElectronica = "46";
	/** Nota de Débito Electrónica = 56 */
	public static final String TIPO_DOC_REF_NotaDeDebitoElectronica = "56";
	/** Nota de Crédito Electrónica = 61 */
	public static final String TIPO_DOC_REF_NotaDeCreditoElectronica = "61";
	/** Guía de Despacho Manual = 50 */
	public static final String TIPO_DOC_REF_GuíaDeDespachoManual = "50";
	/** Guía de Despacho Electrónica = 52 */
	public static final String TIPO_DOC_REF_GuiaDeDespachoElectronica = "52";
	/** 801: Orden de Compra/Planta = 801 */
	public static final String TIPO_DOC_REF_801OrdenDeCompraPlanta = "801";
	/** Contrato = 803 */
	public static final String TIPO_DOC_REF_Contrato = "803";
	/** Numero de Recepcion = HES */
	public static final String TIPO_DOC_REF_NumeroDeRecepcion = "HES";
	/** 802: Número de Atención = 802 */
	public static final String TIPO_DOC_REF_802NumeroDeAtencion = "802";
	/** Set TIPO_DOC_REF.
		@param TIPO_DOC_REF TIPO_DOC_REF	  */
	public void setTIPO_DOC_REF (String TIPO_DOC_REF)
	{

		set_Value (COLUMNNAME_TIPO_DOC_REF, TIPO_DOC_REF);
	}

	/** Get TIPO_DOC_REF.
		@return TIPO_DOC_REF	  */
	public String getTIPO_DOC_REF () 
	{
		return (String)get_Value(COLUMNNAME_TIPO_DOC_REF);
	}
}