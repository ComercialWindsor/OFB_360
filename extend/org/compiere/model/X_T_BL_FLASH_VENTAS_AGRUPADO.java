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

/** Generated Model for T_BL_FLASH_VENTAS_AGRUPADO
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_FLASH_VENTAS_AGRUPADO extends PO implements I_T_BL_FLASH_VENTAS_AGRUPADO, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180405L;

    /** Standard Constructor */
    public X_T_BL_FLASH_VENTAS_AGRUPADO (Properties ctx, int T_BL_FLASH_VENTAS_AGRUPADO_ID, String trxName)
    {
      super (ctx, T_BL_FLASH_VENTAS_AGRUPADO_ID, trxName);
      /** if (T_BL_FLASH_VENTAS_AGRUPADO_ID == 0)
        {
			setT_BL_FLASH_VENTAS_AGRUPADO_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_FLASH_VENTAS_AGRUPADO (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_FLASH_VENTAS_AGRUPADO[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AGRUPA_ANO.
		@param AGRUPA_ANO AGRUPA_ANO	  */
	public void setAGRUPA_ANO (boolean AGRUPA_ANO)
	{
		set_Value (COLUMNNAME_AGRUPA_ANO, Boolean.valueOf(AGRUPA_ANO));
	}

	/** Get AGRUPA_ANO.
		@return AGRUPA_ANO	  */
	public boolean isAGRUPA_ANO () 
	{
		Object oo = get_Value(COLUMNNAME_AGRUPA_ANO);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGRUPA_AREA.
		@param AGRUPA_AREA AGRUPA_AREA	  */
	public void setAGRUPA_AREA (boolean AGRUPA_AREA)
	{
		set_Value (COLUMNNAME_AGRUPA_AREA, Boolean.valueOf(AGRUPA_AREA));
	}

	/** Get AGRUPA_AREA.
		@return AGRUPA_AREA	  */
	public boolean isAGRUPA_AREA () 
	{
		Object oo = get_Value(COLUMNNAME_AGRUPA_AREA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGRUPA_CLIENTE.
		@param AGRUPA_CLIENTE AGRUPA_CLIENTE	  */
	public void setAGRUPA_CLIENTE (boolean AGRUPA_CLIENTE)
	{
		set_Value (COLUMNNAME_AGRUPA_CLIENTE, Boolean.valueOf(AGRUPA_CLIENTE));
	}

	/** Get AGRUPA_CLIENTE.
		@return AGRUPA_CLIENTE	  */
	public boolean isAGRUPA_CLIENTE () 
	{
		Object oo = get_Value(COLUMNNAME_AGRUPA_CLIENTE);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGRUPA_CODIGO_BLUMOS.
		@param AGRUPA_CODIGO_BLUMOS AGRUPA_CODIGO_BLUMOS	  */
	public void setAGRUPA_CODIGO_BLUMOS (boolean AGRUPA_CODIGO_BLUMOS)
	{
		set_Value (COLUMNNAME_AGRUPA_CODIGO_BLUMOS, Boolean.valueOf(AGRUPA_CODIGO_BLUMOS));
	}

	/** Get AGRUPA_CODIGO_BLUMOS.
		@return AGRUPA_CODIGO_BLUMOS	  */
	public boolean isAGRUPA_CODIGO_BLUMOS () 
	{
		Object oo = get_Value(COLUMNNAME_AGRUPA_CODIGO_BLUMOS);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGRUPA_MES.
		@param AGRUPA_MES AGRUPA_MES	  */
	public void setAGRUPA_MES (boolean AGRUPA_MES)
	{
		set_Value (COLUMNNAME_AGRUPA_MES, Boolean.valueOf(AGRUPA_MES));
	}

	/** Get AGRUPA_MES.
		@return AGRUPA_MES	  */
	public boolean isAGRUPA_MES () 
	{
		Object oo = get_Value(COLUMNNAME_AGRUPA_MES);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGRUPA_PRODUCTO.
		@param AGRUPA_PRODUCTO AGRUPA_PRODUCTO	  */
	public void setAGRUPA_PRODUCTO (boolean AGRUPA_PRODUCTO)
	{
		set_Value (COLUMNNAME_AGRUPA_PRODUCTO, Boolean.valueOf(AGRUPA_PRODUCTO));
	}

	/** Get AGRUPA_PRODUCTO.
		@return AGRUPA_PRODUCTO	  */
	public boolean isAGRUPA_PRODUCTO () 
	{
		Object oo = get_Value(COLUMNNAME_AGRUPA_PRODUCTO);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGRUPA_PROYECTO.
		@param AGRUPA_PROYECTO AGRUPA_PROYECTO	  */
	public void setAGRUPA_PROYECTO (boolean AGRUPA_PROYECTO)
	{
		set_Value (COLUMNNAME_AGRUPA_PROYECTO, Boolean.valueOf(AGRUPA_PROYECTO));
	}

	/** Get AGRUPA_PROYECTO.
		@return AGRUPA_PROYECTO	  */
	public boolean isAGRUPA_PROYECTO () 
	{
		Object oo = get_Value(COLUMNNAME_AGRUPA_PROYECTO);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGRUPA_REPRESENTADA.
		@param AGRUPA_REPRESENTADA AGRUPA_REPRESENTADA	  */
	public void setAGRUPA_REPRESENTADA (boolean AGRUPA_REPRESENTADA)
	{
		set_Value (COLUMNNAME_AGRUPA_REPRESENTADA, Boolean.valueOf(AGRUPA_REPRESENTADA));
	}

	/** Get AGRUPA_REPRESENTADA.
		@return AGRUPA_REPRESENTADA	  */
	public boolean isAGRUPA_REPRESENTADA () 
	{
		Object oo = get_Value(COLUMNNAME_AGRUPA_REPRESENTADA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGRUPA_SOLUTEC.
		@param AGRUPA_SOLUTEC AGRUPA_SOLUTEC	  */
	public void setAGRUPA_SOLUTEC (boolean AGRUPA_SOLUTEC)
	{
		set_Value (COLUMNNAME_AGRUPA_SOLUTEC, Boolean.valueOf(AGRUPA_SOLUTEC));
	}

	/** Get AGRUPA_SOLUTEC.
		@return AGRUPA_SOLUTEC	  */
	public boolean isAGRUPA_SOLUTEC () 
	{
		Object oo = get_Value(COLUMNNAME_AGRUPA_SOLUTEC);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AGRUPA_VENDEDOR.
		@param AGRUPA_VENDEDOR AGRUPA_VENDEDOR	  */
	public void setAGRUPA_VENDEDOR (boolean AGRUPA_VENDEDOR)
	{
		set_Value (COLUMNNAME_AGRUPA_VENDEDOR, Boolean.valueOf(AGRUPA_VENDEDOR));
	}

	/** Get AGRUPA_VENDEDOR.
		@return AGRUPA_VENDEDOR	  */
	public boolean isAGRUPA_VENDEDOR () 
	{
		Object oo = get_Value(COLUMNNAME_AGRUPA_VENDEDOR);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set CALCULA_COMISIONES.
		@param CALCULA_COMISIONES CALCULA_COMISIONES	  */
	public void setCALCULA_COMISIONES (boolean CALCULA_COMISIONES)
	{
		set_Value (COLUMNNAME_CALCULA_COMISIONES, Boolean.valueOf(CALCULA_COMISIONES));
	}

	/** Get CALCULA_COMISIONES.
		@return CALCULA_COMISIONES	  */
	public boolean isCALCULA_COMISIONES () 
	{
		Object oo = get_Value(COLUMNNAME_CALCULA_COMISIONES);
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
	public void setORG_DE_VENTA (int ORG_DE_VENTA)
	{
		set_Value (COLUMNNAME_ORG_DE_VENTA, Integer.valueOf(ORG_DE_VENTA));
	}

	/** Get ORG_DE_VENTA.
		@return ORG_DE_VENTA	  */
	public int getORG_DE_VENTA () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ORG_DE_VENTA);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set SALDO_BODEGA_CENTRAL.
		@param SALDO_BODEGA_CENTRAL SALDO_BODEGA_CENTRAL	  */
	public void setSALDO_BODEGA_CENTRAL (BigDecimal SALDO_BODEGA_CENTRAL)
	{
		set_Value (COLUMNNAME_SALDO_BODEGA_CENTRAL, SALDO_BODEGA_CENTRAL);
	}

	/** Get SALDO_BODEGA_CENTRAL.
		@return SALDO_BODEGA_CENTRAL	  */
	public BigDecimal getSALDO_BODEGA_CENTRAL () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDO_BODEGA_CENTRAL);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set T_BL_FLASH_VENTAS_AGRUPADO.
		@param T_BL_FLASH_VENTAS_AGRUPADO_ID T_BL_FLASH_VENTAS_AGRUPADO	  */
	public void setT_BL_FLASH_VENTAS_AGRUPADO_ID (int T_BL_FLASH_VENTAS_AGRUPADO_ID)
	{
		if (T_BL_FLASH_VENTAS_AGRUPADO_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_VENTAS_AGRUPADO_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_VENTAS_AGRUPADO_ID, Integer.valueOf(T_BL_FLASH_VENTAS_AGRUPADO_ID));
	}

	/** Get T_BL_FLASH_VENTAS_AGRUPADO.
		@return T_BL_FLASH_VENTAS_AGRUPADO	  */
	public int getT_BL_FLASH_VENTAS_AGRUPADO_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_FLASH_VENTAS_AGRUPADO_ID);
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