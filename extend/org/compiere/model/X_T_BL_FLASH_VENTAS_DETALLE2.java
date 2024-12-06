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

/** Generated Model for T_BL_FLASH_VENTAS_DETALLE2
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_FLASH_VENTAS_DETALLE2 extends PO implements I_T_BL_FLASH_VENTAS_DETALLE2, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180405L;

    /** Standard Constructor */
    public X_T_BL_FLASH_VENTAS_DETALLE2 (Properties ctx, int T_BL_FLASH_VENTAS_DETALLE2_ID, String trxName)
    {
      super (ctx, T_BL_FLASH_VENTAS_DETALLE2_ID, trxName);
      /** if (T_BL_FLASH_VENTAS_DETALLE2_ID == 0)
        {
			setT_BL_FLASH_VENTAS_DETALLE2_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_FLASH_VENTAS_DETALLE2 (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_FLASH_VENTAS_DETALLE2[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ANO.
		@param ANO ANO	  */
	public void setANO (String ANO)
	{
		set_Value (COLUMNNAME_ANO, ANO);
	}

	/** Get ANO.
		@return ANO	  */
	public String getANO () 
	{
		return (String)get_Value(COLUMNNAME_ANO);
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

	public I_C_DocType getC_DocType() throws RuntimeException
    {
		return (I_C_DocType)MTable.get(getCtx(), I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
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

	public I_C_InvoiceLine getC_InvoiceLine() throws RuntimeException
    {
		return (I_C_InvoiceLine)MTable.get(getCtx(), I_C_InvoiceLine.Table_Name)
			.getPO(getC_InvoiceLine_ID(), get_TrxName());	}

	/** Set Invoice Line.
		@param C_InvoiceLine_ID 
		Invoice Detail Line
	  */
	public void setC_InvoiceLine_ID (int C_InvoiceLine_ID)
	{
		if (C_InvoiceLine_ID < 1) 
			set_Value (COLUMNNAME_C_InvoiceLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_InvoiceLine_ID, Integer.valueOf(C_InvoiceLine_ID));
	}

	/** Get Invoice Line.
		@return Invoice Detail Line
	  */
	public int getC_InvoiceLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_InvoiceLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CODIGO_BLUMOS.
		@param CODIGO_BLUMOS CODIGO_BLUMOS	  */
	public void setCODIGO_BLUMOS (String CODIGO_BLUMOS)
	{
		set_Value (COLUMNNAME_CODIGO_BLUMOS, CODIGO_BLUMOS);
	}

	/** Get CODIGO_BLUMOS.
		@return CODIGO_BLUMOS	  */
	public String getCODIGO_BLUMOS () 
	{
		return (String)get_Value(COLUMNNAME_CODIGO_BLUMOS);
	}

	/** Set COMISION.
		@param COMISION COMISION	  */
	public void setCOMISION (BigDecimal COMISION)
	{
		set_Value (COLUMNNAME_COMISION, COMISION);
	}

	/** Get COMISION.
		@return COMISION	  */
	public BigDecimal getCOMISION () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COMISION);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set COMISION_SOLUTEC.
		@param COMISION_SOLUTEC COMISION_SOLUTEC	  */
	public void setCOMISION_SOLUTEC (BigDecimal COMISION_SOLUTEC)
	{
		set_Value (COLUMNNAME_COMISION_SOLUTEC, COMISION_SOLUTEC);
	}

	/** Get COMISION_SOLUTEC.
		@return COMISION_SOLUTEC	  */
	public BigDecimal getCOMISION_SOLUTEC () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COMISION_SOLUTEC);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set COSTOLINEA.
		@param COSTOLINEA COSTOLINEA	  */
	public void setCOSTOLINEA (BigDecimal COSTOLINEA)
	{
		set_Value (COLUMNNAME_COSTOLINEA, COSTOLINEA);
	}

	/** Get COSTOLINEA.
		@return COSTOLINEA	  */
	public BigDecimal getCOSTOLINEA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COSTOLINEA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set COSTOUNITARIO.
		@param COSTOUNITARIO COSTOUNITARIO	  */
	public void setCOSTOUNITARIO (BigDecimal COSTOUNITARIO)
	{
		set_Value (COLUMNNAME_COSTOUNITARIO, COSTOUNITARIO);
	}

	/** Get COSTOUNITARIO.
		@return COSTOUNITARIO	  */
	public BigDecimal getCOSTOUNITARIO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COSTOUNITARIO);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_ProjectOFB getC_ProjectOFB() throws RuntimeException
    {
		return (I_C_ProjectOFB)MTable.get(getCtx(), I_C_ProjectOFB.Table_Name)
			.getPO(getC_ProjectOFB_ID(), get_TrxName());	}

	/** Set C_ProjectOFB_ID.
		@param C_ProjectOFB_ID C_ProjectOFB_ID	  */
	public void setC_ProjectOFB_ID (int C_ProjectOFB_ID)
	{
		if (C_ProjectOFB_ID < 1) 
			set_Value (COLUMNNAME_C_ProjectOFB_ID, null);
		else 
			set_Value (COLUMNNAME_C_ProjectOFB_ID, Integer.valueOf(C_ProjectOFB_ID));
	}

	/** Get C_ProjectOFB_ID.
		@return C_ProjectOFB_ID	  */
	public int getC_ProjectOFB_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectOFB_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_UOM getC_UOM() throws RuntimeException
    {
		return (I_C_UOM)MTable.get(getCtx(), I_C_UOM.Table_Name)
			.getPO(getC_UOM_ID(), get_TrxName());	}

	/** Set UOM.
		@param C_UOM_ID 
		Unit of Measure
	  */
	public void setC_UOM_ID (int C_UOM_ID)
	{
		if (C_UOM_ID < 1) 
			set_Value (COLUMNNAME_C_UOM_ID, null);
		else 
			set_Value (COLUMNNAME_C_UOM_ID, Integer.valueOf(C_UOM_ID));
	}

	/** Get UOM.
		@return Unit of Measure
	  */
	public int getC_UOM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_UOM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Date Invoiced.
		@param DateInvoiced 
		Date printed on Invoice
	  */
	public void setDateInvoiced (Timestamp DateInvoiced)
	{
		set_Value (COLUMNNAME_DateInvoiced, DateInvoiced);
	}

	/** Get Date Invoiced.
		@return Date printed on Invoice
	  */
	public Timestamp getDateInvoiced () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateInvoiced);
	}

	/** Set DIA.
		@param DIA DIA	  */
	public void setDIA (String DIA)
	{
		set_Value (COLUMNNAME_DIA, DIA);
	}

	/** Get DIA.
		@return DIA	  */
	public String getDIA () 
	{
		return (String)get_Value(COLUMNNAME_DIA);
	}

	public I_T_BL_CARTERA getESIND() throws RuntimeException
    {
		return (I_T_BL_CARTERA)MTable.get(getCtx(), I_T_BL_CARTERA.Table_Name)
			.getPO(getESINDENT(), get_TrxName());	}

	/** Set T_BL_FLASH_VENTAS.
		@param ESINDENT T_BL_FLASH_VENTAS	  */
	public void setESINDENT (int ESINDENT)
	{
		set_Value (COLUMNNAME_ESINDENT, Integer.valueOf(ESINDENT));
	}

	/** Get T_BL_FLASH_VENTAS.
		@return T_BL_FLASH_VENTAS	  */
	public int getESINDENT () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ESINDENT);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ForeignPrice.
		@param ForeignPrice ForeignPrice	  */
	public void setForeignPrice (BigDecimal ForeignPrice)
	{
		set_Value (COLUMNNAME_ForeignPrice, ForeignPrice);
	}

	/** Get ForeignPrice.
		@return ForeignPrice	  */
	public BigDecimal getForeignPrice () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ForeignPrice);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set LINEA_PRODUCTO.
		@param LINEA_PRODUCTO LINEA_PRODUCTO	  */
	public void setLINEA_PRODUCTO (String LINEA_PRODUCTO)
	{
		set_Value (COLUMNNAME_LINEA_PRODUCTO, LINEA_PRODUCTO);
	}

	/** Get LINEA_PRODUCTO.
		@return LINEA_PRODUCTO	  */
	public String getLINEA_PRODUCTO () 
	{
		return (String)get_Value(COLUMNNAME_LINEA_PRODUCTO);
	}

	/** Set Line Amount.
		@param LineNetAmt 
		Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public void setLineNetAmt (BigDecimal LineNetAmt)
	{
		set_Value (COLUMNNAME_LineNetAmt, LineNetAmt);
	}

	/** Get Line Amount.
		@return Line Extended Amount (Quantity * Actual Price) without Freight and Charges
	  */
	public BigDecimal getLineNetAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LineNetAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MARGEN_DOLAR.
		@param MARGEN_DOLAR MARGEN_DOLAR	  */
	public void setMARGEN_DOLAR (BigDecimal MARGEN_DOLAR)
	{
		set_Value (COLUMNNAME_MARGEN_DOLAR, MARGEN_DOLAR);
	}

	/** Get MARGEN_DOLAR.
		@return MARGEN_DOLAR	  */
	public BigDecimal getMARGEN_DOLAR () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MARGEN_DOLAR);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MARGENLINEA.
		@param MARGENLINEA MARGENLINEA	  */
	public void setMARGENLINEA (BigDecimal MARGENLINEA)
	{
		set_Value (COLUMNNAME_MARGENLINEA, MARGENLINEA);
	}

	/** Get MARGENLINEA.
		@return MARGENLINEA	  */
	public BigDecimal getMARGENLINEA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MARGENLINEA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MARGENPORC.
		@param MARGENPORC MARGENPORC	  */
	public void setMARGENPORC (BigDecimal MARGENPORC)
	{
		set_Value (COLUMNNAME_MARGENPORC, MARGENPORC);
	}

	/** Get MARGENPORC.
		@return MARGENPORC	  */
	public BigDecimal getMARGENPORC () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MARGENPORC);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MARGEN_UF.
		@param MARGEN_UF MARGEN_UF	  */
	public void setMARGEN_UF (BigDecimal MARGEN_UF)
	{
		set_Value (COLUMNNAME_MARGEN_UF, MARGEN_UF);
	}

	/** Get MARGEN_UF.
		@return MARGEN_UF	  */
	public BigDecimal getMARGEN_UF () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MARGEN_UF);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MES.
		@param MES MES	  */
	public void setMES (String MES)
	{
		set_Value (COLUMNNAME_MES, MES);
	}

	/** Get MES.
		@return MES	  */
	public String getMES () 
	{
		return (String)get_Value(COLUMNNAME_MES);
	}

	public I_M_Product getM_Product() throws RuntimeException
    {
		return (I_M_Product)MTable.get(getCtx(), I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ORG_DE_VENTA.
		@param ORG_DE_VENTA ORG_DE_VENTA	  */
	public void setORG_DE_VENTA (BigDecimal ORG_DE_VENTA)
	{
		set_Value (COLUMNNAME_ORG_DE_VENTA, ORG_DE_VENTA);
	}

	/** Get ORG_DE_VENTA.
		@return ORG_DE_VENTA	  */
	public BigDecimal getORG_DE_VENTA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ORG_DE_VENTA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set PLANTA_ID.
		@param PLANTA_ID PLANTA_ID	  */
	public void setPLANTA_ID (int PLANTA_ID)
	{
		if (PLANTA_ID < 1) 
			set_Value (COLUMNNAME_PLANTA_ID, null);
		else 
			set_Value (COLUMNNAME_PLANTA_ID, Integer.valueOf(PLANTA_ID));
	}

	/** Get PLANTA_ID.
		@return PLANTA_ID	  */
	public int getPLANTA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PLANTA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Price.
		@param PriceEntered 
		Price Entered - the price based on the selected/base UoM
	  */
	public void setPriceEntered (BigDecimal PriceEntered)
	{
		set_Value (COLUMNNAME_PriceEntered, PriceEntered);
	}

	/** Get Price.
		@return Price Entered - the price based on the selected/base UoM
	  */
	public BigDecimal getPriceEntered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PriceEntered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Quantity Invoiced.
		@param QtyInvoiced 
		Invoiced Quantity
	  */
	public void setQtyInvoiced (BigDecimal QtyInvoiced)
	{
		set_Value (COLUMNNAME_QtyInvoiced, QtyInvoiced);
	}

	/** Get Quantity Invoiced.
		@return Invoiced Quantity
	  */
	public BigDecimal getQtyInvoiced () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyInvoiced);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set On Hand Quantity.
		@param QtyOnHand 
		On Hand Quantity
	  */
	public void setQtyOnHand (BigDecimal QtyOnHand)
	{
		set_Value (COLUMNNAME_QtyOnHand, QtyOnHand);
	}

	/** Get On Hand Quantity.
		@return On Hand Quantity
	  */
	public BigDecimal getQtyOnHand () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyOnHand);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_BPartner getREPRESENTADA() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getREPRESENTADA_ID(), get_TrxName());	}

	/** Set REPRESENTADA_ID.
		@param REPRESENTADA_ID REPRESENTADA_ID	  */
	public void setREPRESENTADA_ID (int REPRESENTADA_ID)
	{
		if (REPRESENTADA_ID < 1) 
			set_Value (COLUMNNAME_REPRESENTADA_ID, null);
		else 
			set_Value (COLUMNNAME_REPRESENTADA_ID, Integer.valueOf(REPRESENTADA_ID));
	}

	/** Get REPRESENTADA_ID.
		@return REPRESENTADA_ID	  */
	public int getREPRESENTADA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_REPRESENTADA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set REPRESENTADA_PADRE_ID.
		@param REPRESENTADA_PADRE_ID REPRESENTADA_PADRE_ID	  */
	public void setREPRESENTADA_PADRE_ID (int REPRESENTADA_PADRE_ID)
	{
		if (REPRESENTADA_PADRE_ID < 1) 
			set_Value (COLUMNNAME_REPRESENTADA_PADRE_ID, null);
		else 
			set_Value (COLUMNNAME_REPRESENTADA_PADRE_ID, Integer.valueOf(REPRESENTADA_PADRE_ID));
	}

	/** Get REPRESENTADA_PADRE_ID.
		@return REPRESENTADA_PADRE_ID	  */
	public int getREPRESENTADA_PADRE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_REPRESENTADA_PADRE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AD_User getSalesRep() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getSalesRep_ID(), get_TrxName());	}

	/** Set Sales Representative.
		@param SalesRep_ID 
		Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID)
	{
		if (SalesRep_ID < 1) 
			set_Value (COLUMNNAME_SalesRep_ID, null);
		else 
			set_Value (COLUMNNAME_SalesRep_ID, Integer.valueOf(SalesRep_ID));
	}

	/** Get Sales Representative.
		@return Sales Representative or Company Agent
	  */
	public int getSalesRep_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SalesRep_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SOLUTEC.
		@param SOLUTEC SOLUTEC	  */
	public void setSOLUTEC (boolean SOLUTEC)
	{
		set_Value (COLUMNNAME_SOLUTEC, Boolean.valueOf(SOLUTEC));
	}

	/** Get SOLUTEC.
		@return SOLUTEC	  */
	public boolean isSOLUTEC () 
	{
		Object oo = get_Value(COLUMNNAME_SOLUTEC);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_T_BL_FLASH_PARAMETROS getT_BL_FLASH_PARAMETROS() throws RuntimeException
    {
		return (I_T_BL_FLASH_PARAMETROS)MTable.get(getCtx(), I_T_BL_FLASH_PARAMETROS.Table_Name)
			.getPO(getT_BL_FLASH_PARAMETROS_ID(), get_TrxName());	}

	/** Set T_BL_FLASH_PARAMETROS.
		@param T_BL_FLASH_PARAMETROS_ID T_BL_FLASH_PARAMETROS	  */
	public void setT_BL_FLASH_PARAMETROS_ID (int T_BL_FLASH_PARAMETROS_ID)
	{
		if (T_BL_FLASH_PARAMETROS_ID < 1) 
			set_Value (COLUMNNAME_T_BL_FLASH_PARAMETROS_ID, null);
		else 
			set_Value (COLUMNNAME_T_BL_FLASH_PARAMETROS_ID, Integer.valueOf(T_BL_FLASH_PARAMETROS_ID));
	}

	/** Get T_BL_FLASH_PARAMETROS.
		@return T_BL_FLASH_PARAMETROS	  */
	public int getT_BL_FLASH_PARAMETROS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_FLASH_PARAMETROS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set T_BL_FLASH_VENTAS_DETALLE2.
		@param T_BL_FLASH_VENTAS_DETALLE2_ID T_BL_FLASH_VENTAS_DETALLE2	  */
	public void setT_BL_FLASH_VENTAS_DETALLE2_ID (int T_BL_FLASH_VENTAS_DETALLE2_ID)
	{
		if (T_BL_FLASH_VENTAS_DETALLE2_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_VENTAS_DETALLE2_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_VENTAS_DETALLE2_ID, Integer.valueOf(T_BL_FLASH_VENTAS_DETALLE2_ID));
	}

	/** Get T_BL_FLASH_VENTAS_DETALLE2.
		@return T_BL_FLASH_VENTAS_DETALLE2	  */
	public int getT_BL_FLASH_VENTAS_DETALLE2_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_FLASH_VENTAS_DETALLE2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ULTIMA_FECHA_DESPACHO.
		@param ULTIMA_FECHA_DESPACHO ULTIMA_FECHA_DESPACHO	  */
	public void setULTIMA_FECHA_DESPACHO (Timestamp ULTIMA_FECHA_DESPACHO)
	{
		set_Value (COLUMNNAME_ULTIMA_FECHA_DESPACHO, ULTIMA_FECHA_DESPACHO);
	}

	/** Get ULTIMA_FECHA_DESPACHO.
		@return ULTIMA_FECHA_DESPACHO	  */
	public Timestamp getULTIMA_FECHA_DESPACHO () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ULTIMA_FECHA_DESPACHO);
	}

	public I_AD_User getVENDEDORCARTERA() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getVENDEDORCARTERA_ID(), get_TrxName());	}

	/** Set VENDEDORCARTERA_ID.
		@param VENDEDORCARTERA_ID VENDEDORCARTERA_ID	  */
	public void setVENDEDORCARTERA_ID (int VENDEDORCARTERA_ID)
	{
		if (VENDEDORCARTERA_ID < 1) 
			set_Value (COLUMNNAME_VENDEDORCARTERA_ID, null);
		else 
			set_Value (COLUMNNAME_VENDEDORCARTERA_ID, Integer.valueOf(VENDEDORCARTERA_ID));
	}

	/** Get VENDEDORCARTERA_ID.
		@return VENDEDORCARTERA_ID	  */
	public int getVENDEDORCARTERA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_VENDEDORCARTERA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set VENTA_EN_DOLAR.
		@param VENTA_EN_DOLAR VENTA_EN_DOLAR	  */
	public void setVENTA_EN_DOLAR (BigDecimal VENTA_EN_DOLAR)
	{
		set_Value (COLUMNNAME_VENTA_EN_DOLAR, VENTA_EN_DOLAR);
	}

	/** Get VENTA_EN_DOLAR.
		@return VENTA_EN_DOLAR	  */
	public BigDecimal getVENTA_EN_DOLAR () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VENTA_EN_DOLAR);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VENTA_MON_EXTRANJERA.
		@param VENTA_MON_EXTRANJERA VENTA_MON_EXTRANJERA	  */
	public void setVENTA_MON_EXTRANJERA (BigDecimal VENTA_MON_EXTRANJERA)
	{
		set_Value (COLUMNNAME_VENTA_MON_EXTRANJERA, VENTA_MON_EXTRANJERA);
	}

	/** Get VENTA_MON_EXTRANJERA.
		@return VENTA_MON_EXTRANJERA	  */
	public BigDecimal getVENTA_MON_EXTRANJERA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VENTA_MON_EXTRANJERA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VENTANUEVA.
		@param VENTANUEVA VENTANUEVA	  */
	public void setVENTANUEVA (boolean VENTANUEVA)
	{
		set_Value (COLUMNNAME_VENTANUEVA, Boolean.valueOf(VENTANUEVA));
	}

	/** Get VENTANUEVA.
		@return VENTANUEVA	  */
	public boolean isVENTANUEVA () 
	{
		Object oo = get_Value(COLUMNNAME_VENTANUEVA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set VENTA_UF.
		@param VENTA_UF VENTA_UF	  */
	public void setVENTA_UF (BigDecimal VENTA_UF)
	{
		set_Value (COLUMNNAME_VENTA_UF, VENTA_UF);
	}

	/** Get VENTA_UF.
		@return VENTA_UF	  */
	public BigDecimal getVENTA_UF () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VENTA_UF);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}