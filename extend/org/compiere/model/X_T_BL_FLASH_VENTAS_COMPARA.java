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

/** Generated Model for T_BL_FLASH_VENTAS_COMPARA
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_FLASH_VENTAS_COMPARA extends PO implements I_T_BL_FLASH_VENTAS_COMPARA, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180405L;

    /** Standard Constructor */
    public X_T_BL_FLASH_VENTAS_COMPARA (Properties ctx, int T_BL_FLASH_VENTAS_COMPARA_ID, String trxName)
    {
      super (ctx, T_BL_FLASH_VENTAS_COMPARA_ID, trxName);
      /** if (T_BL_FLASH_VENTAS_COMPARA_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_T_BL_FLASH_VENTAS_COMPARA (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_FLASH_VENTAS_COMPARA[")
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

	/** Set ANO2.
		@param ANO2 ANO2	  */
	public void setANO2 (String ANO2)
	{
		set_Value (COLUMNNAME_ANO2, ANO2);
	}

	/** Get ANO2.
		@return ANO2	  */
	public String getANO2 () 
	{
		return (String)get_Value(COLUMNNAME_ANO2);
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

	/** Set COSTOLINEA2.
		@param COSTOLINEA2 COSTOLINEA2	  */
	public void setCOSTOLINEA2 (BigDecimal COSTOLINEA2)
	{
		set_Value (COLUMNNAME_COSTOLINEA2, COSTOLINEA2);
	}

	/** Get COSTOLINEA2.
		@return COSTOLINEA2	  */
	public BigDecimal getCOSTOLINEA2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COSTOLINEA2);
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

	/** Set DIFCANTIDAD.
		@param DIFCANTIDAD DIFCANTIDAD	  */
	public void setDIFCANTIDAD (BigDecimal DIFCANTIDAD)
	{
		set_Value (COLUMNNAME_DIFCANTIDAD, DIFCANTIDAD);
	}

	/** Get DIFCANTIDAD.
		@return DIFCANTIDAD	  */
	public BigDecimal getDIFCANTIDAD () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DIFCANTIDAD);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DIFCOSTO.
		@param DIFCOSTO DIFCOSTO	  */
	public void setDIFCOSTO (BigDecimal DIFCOSTO)
	{
		set_Value (COLUMNNAME_DIFCOSTO, DIFCOSTO);
	}

	/** Get DIFCOSTO.
		@return DIFCOSTO	  */
	public BigDecimal getDIFCOSTO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DIFCOSTO);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DIFERENCIA.
		@param DIFERENCIA DIFERENCIA	  */
	public void setDIFERENCIA (String DIFERENCIA)
	{
		set_Value (COLUMNNAME_DIFERENCIA, DIFERENCIA);
	}

	/** Get DIFERENCIA.
		@return DIFERENCIA	  */
	public String getDIFERENCIA () 
	{
		return (String)get_Value(COLUMNNAME_DIFERENCIA);
	}

	/** Set DIFMARGEN.
		@param DIFMARGEN DIFMARGEN	  */
	public void setDIFMARGEN (BigDecimal DIFMARGEN)
	{
		set_Value (COLUMNNAME_DIFMARGEN, DIFMARGEN);
	}

	/** Get DIFMARGEN.
		@return DIFMARGEN	  */
	public BigDecimal getDIFMARGEN () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DIFMARGEN);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DIF_MARGEN_DOLAR.
		@param DIF_MARGEN_DOLAR DIF_MARGEN_DOLAR	  */
	public void setDIF_MARGEN_DOLAR (BigDecimal DIF_MARGEN_DOLAR)
	{
		set_Value (COLUMNNAME_DIF_MARGEN_DOLAR, DIF_MARGEN_DOLAR);
	}

	/** Get DIF_MARGEN_DOLAR.
		@return DIF_MARGEN_DOLAR	  */
	public BigDecimal getDIF_MARGEN_DOLAR () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DIF_MARGEN_DOLAR);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DIF_MARGEN_UF.
		@param DIF_MARGEN_UF DIF_MARGEN_UF	  */
	public void setDIF_MARGEN_UF (BigDecimal DIF_MARGEN_UF)
	{
		set_Value (COLUMNNAME_DIF_MARGEN_UF, DIF_MARGEN_UF);
	}

	/** Get DIF_MARGEN_UF.
		@return DIF_MARGEN_UF	  */
	public BigDecimal getDIF_MARGEN_UF () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DIF_MARGEN_UF);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DIFNETO.
		@param DIFNETO DIFNETO	  */
	public void setDIFNETO (BigDecimal DIFNETO)
	{
		set_Value (COLUMNNAME_DIFNETO, DIFNETO);
	}

	/** Get DIFNETO.
		@return DIFNETO	  */
	public BigDecimal getDIFNETO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DIFNETO);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DIFPORCVTADOLAR.
		@param DIFPORCVTADOLAR DIFPORCVTADOLAR	  */
	public void setDIFPORCVTADOLAR (BigDecimal DIFPORCVTADOLAR)
	{
		set_Value (COLUMNNAME_DIFPORCVTADOLAR, DIFPORCVTADOLAR);
	}

	/** Get DIFPORCVTADOLAR.
		@return DIFPORCVTADOLAR	  */
	public BigDecimal getDIFPORCVTADOLAR () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DIFPORCVTADOLAR);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DIFVENTADOLAR.
		@param DIFVENTADOLAR DIFVENTADOLAR	  */
	public void setDIFVENTADOLAR (BigDecimal DIFVENTADOLAR)
	{
		set_Value (COLUMNNAME_DIFVENTADOLAR, DIFVENTADOLAR);
	}

	/** Get DIFVENTADOLAR.
		@return DIFVENTADOLAR	  */
	public BigDecimal getDIFVENTADOLAR () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DIFVENTADOLAR);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DIFVENTAUF.
		@param DIFVENTAUF DIFVENTAUF	  */
	public void setDIFVENTAUF (BigDecimal DIFVENTAUF)
	{
		set_Value (COLUMNNAME_DIFVENTAUF, DIFVENTAUF);
	}

	/** Get DIFVENTAUF.
		@return DIFVENTAUF	  */
	public BigDecimal getDIFVENTAUF () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DIFVENTAUF);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set LINENETAMT2.
		@param LINENETAMT2 LINENETAMT2	  */
	public void setLINENETAMT2 (BigDecimal LINENETAMT2)
	{
		set_Value (COLUMNNAME_LINENETAMT2, LINENETAMT2);
	}

	/** Get LINENETAMT2.
		@return LINENETAMT2	  */
	public BigDecimal getLINENETAMT2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LINENETAMT2);
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

	/** Set MARGEN_DOLAR2.
		@param MARGEN_DOLAR2 MARGEN_DOLAR2	  */
	public void setMARGEN_DOLAR2 (BigDecimal MARGEN_DOLAR2)
	{
		set_Value (COLUMNNAME_MARGEN_DOLAR2, MARGEN_DOLAR2);
	}

	/** Get MARGEN_DOLAR2.
		@return MARGEN_DOLAR2	  */
	public BigDecimal getMARGEN_DOLAR2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MARGEN_DOLAR2);
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

	/** Set MARGENLINEA2.
		@param MARGENLINEA2 MARGENLINEA2	  */
	public void setMARGENLINEA2 (BigDecimal MARGENLINEA2)
	{
		set_Value (COLUMNNAME_MARGENLINEA2, MARGENLINEA2);
	}

	/** Get MARGENLINEA2.
		@return MARGENLINEA2	  */
	public BigDecimal getMARGENLINEA2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MARGENLINEA2);
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

	/** Set MARGENPORC2.
		@param MARGENPORC2 MARGENPORC2	  */
	public void setMARGENPORC2 (BigDecimal MARGENPORC2)
	{
		set_Value (COLUMNNAME_MARGENPORC2, MARGENPORC2);
	}

	/** Get MARGENPORC2.
		@return MARGENPORC2	  */
	public BigDecimal getMARGENPORC2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MARGENPORC2);
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

	/** Set MARGEN_UF2.
		@param MARGEN_UF2 MARGEN_UF2	  */
	public void setMARGEN_UF2 (BigDecimal MARGEN_UF2)
	{
		set_Value (COLUMNNAME_MARGEN_UF2, MARGEN_UF2);
	}

	/** Get MARGEN_UF2.
		@return MARGEN_UF2	  */
	public BigDecimal getMARGEN_UF2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MARGEN_UF2);
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

	/** Set MES2.
		@param MES2 MES2	  */
	public void setMES2 (String MES2)
	{
		set_Value (COLUMNNAME_MES2, MES2);
	}

	/** Get MES2.
		@return MES2	  */
	public String getMES2 () 
	{
		return (String)get_Value(COLUMNNAME_MES2);
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

	/** Set QTYINVOICED2.
		@param QTYINVOICED2 QTYINVOICED2	  */
	public void setQTYINVOICED2 (BigDecimal QTYINVOICED2)
	{
		set_Value (COLUMNNAME_QTYINVOICED2, QTYINVOICED2);
	}

	/** Get QTYINVOICED2.
		@return QTYINVOICED2	  */
	public BigDecimal getQTYINVOICED2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QTYINVOICED2);
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

	/** Set T_BL_FLASH_VENTAS_COMPARA.
		@param T_BL_FLASH_VENTAS_COMPARA_ID T_BL_FLASH_VENTAS_COMPARA	  */
	public void setT_BL_FLASH_VENTAS_COMPARA_ID (int T_BL_FLASH_VENTAS_COMPARA_ID)
	{
		if (T_BL_FLASH_VENTAS_COMPARA_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_VENTAS_COMPARA_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_VENTAS_COMPARA_ID, Integer.valueOf(T_BL_FLASH_VENTAS_COMPARA_ID));
	}

	/** Get T_BL_FLASH_VENTAS_COMPARA.
		@return T_BL_FLASH_VENTAS_COMPARA	  */
	public int getT_BL_FLASH_VENTAS_COMPARA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_FLASH_VENTAS_COMPARA_ID);
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

	/** Set VENTA_EN_DOLAR2.
		@param VENTA_EN_DOLAR2 VENTA_EN_DOLAR2	  */
	public void setVENTA_EN_DOLAR2 (BigDecimal VENTA_EN_DOLAR2)
	{
		set_Value (COLUMNNAME_VENTA_EN_DOLAR2, VENTA_EN_DOLAR2);
	}

	/** Get VENTA_EN_DOLAR2.
		@return VENTA_EN_DOLAR2	  */
	public BigDecimal getVENTA_EN_DOLAR2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VENTA_EN_DOLAR2);
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

	/** Set VENTA_UF2.
		@param VENTA_UF2 VENTA_UF2	  */
	public void setVENTA_UF2 (BigDecimal VENTA_UF2)
	{
		set_Value (COLUMNNAME_VENTA_UF2, VENTA_UF2);
	}

	/** Get VENTA_UF2.
		@return VENTA_UF2	  */
	public BigDecimal getVENTA_UF2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VENTA_UF2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}