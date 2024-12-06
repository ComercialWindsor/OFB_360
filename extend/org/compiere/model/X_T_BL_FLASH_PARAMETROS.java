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

/** Generated Model for T_BL_FLASH_PARAMETROS
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_FLASH_PARAMETROS extends PO implements I_T_BL_FLASH_PARAMETROS, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180405L;

    /** Standard Constructor */
    public X_T_BL_FLASH_PARAMETROS (Properties ctx, int T_BL_FLASH_PARAMETROS_ID, String trxName)
    {
      super (ctx, T_BL_FLASH_PARAMETROS_ID, trxName);
      /** if (T_BL_FLASH_PARAMETROS_ID == 0)
        {
			setACTIVA_CONSULTA (false);
			setDESDE (new Timestamp( System.currentTimeMillis() ));
			setHASTA (new Timestamp( System.currentTimeMillis() ));
			setT_BL_FLASH_PARAMETROS_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_FLASH_PARAMETROS (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_FLASH_PARAMETROS[")
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

	/** Set ACTIVA_GRUPO.
		@param ACTIVA_GRUPO ACTIVA_GRUPO	  */
	public void setACTIVA_GRUPO (boolean ACTIVA_GRUPO)
	{
		set_Value (COLUMNNAME_ACTIVA_GRUPO, Boolean.valueOf(ACTIVA_GRUPO));
	}

	/** Get ACTIVA_GRUPO.
		@return ACTIVA_GRUPO	  */
	public boolean isACTIVA_GRUPO () 
	{
		Object oo = get_Value(COLUMNNAME_ACTIVA_GRUPO);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set COMPARATIVO.
		@param COMPARATIVO COMPARATIVO	  */
	public void setCOMPARATIVO (boolean COMPARATIVO)
	{
		set_Value (COLUMNNAME_COMPARATIVO, Boolean.valueOf(COMPARATIVO));
	}

	/** Get COMPARATIVO.
		@return COMPARATIVO	  */
	public boolean isCOMPARATIVO () 
	{
		Object oo = get_Value(COLUMNNAME_COMPARATIVO);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set EMITE_REPORTE_COMISIONES.
		@param EMITE_REPORTE_COMISIONES EMITE_REPORTE_COMISIONES	  */
	public void setEMITE_REPORTE_COMISIONES (String EMITE_REPORTE_COMISIONES)
	{
		set_Value (COLUMNNAME_EMITE_REPORTE_COMISIONES, EMITE_REPORTE_COMISIONES);
	}

	/** Get EMITE_REPORTE_COMISIONES.
		@return EMITE_REPORTE_COMISIONES	  */
	public String getEMITE_REPORTE_COMISIONES () 
	{
		return (String)get_Value(COLUMNNAME_EMITE_REPORTE_COMISIONES);
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

	/** Set INCLUIR_INDENT.
		@param INCLUIR_INDENT INCLUIR_INDENT	  */
	public void setINCLUIR_INDENT (boolean INCLUIR_INDENT)
	{
		set_Value (COLUMNNAME_INCLUIR_INDENT, Boolean.valueOf(INCLUIR_INDENT));
	}

	/** Get INCLUIR_INDENT.
		@return INCLUIR_INDENT	  */
	public boolean isINCLUIR_INDENT () 
	{
		Object oo = get_Value(COLUMNNAME_INCLUIR_INDENT);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set PROCESAR.
		@param PROCESAR PROCESAR	  */
	public void setPROCESAR (String PROCESAR)
	{
		set_Value (COLUMNNAME_PROCESAR, PROCESAR);
	}

	/** Get PROCESAR.
		@return PROCESAR	  */
	public String getPROCESAR () 
	{
		return (String)get_Value(COLUMNNAME_PROCESAR);
	}

	/** Set REPORTE_CLP.
		@param REPORTE_CLP REPORTE_CLP	  */
	public void setREPORTE_CLP (String REPORTE_CLP)
	{
		set_Value (COLUMNNAME_REPORTE_CLP, REPORTE_CLP);
	}

	/** Get REPORTE_CLP.
		@return REPORTE_CLP	  */
	public String getREPORTE_CLP () 
	{
		return (String)get_Value(COLUMNNAME_REPORTE_CLP);
	}

	/** Set REPORTE_UF.
		@param REPORTE_UF REPORTE_UF	  */
	public void setREPORTE_UF (String REPORTE_UF)
	{
		set_Value (COLUMNNAME_REPORTE_UF, REPORTE_UF);
	}

	/** Get REPORTE_UF.
		@return REPORTE_UF	  */
	public String getREPORTE_UF () 
	{
		return (String)get_Value(COLUMNNAME_REPORTE_UF);
	}

	/** Set REPORTE_USD.
		@param REPORTE_USD REPORTE_USD	  */
	public void setREPORTE_USD (String REPORTE_USD)
	{
		set_Value (COLUMNNAME_REPORTE_USD, REPORTE_USD);
	}

	/** Get REPORTE_USD.
		@return REPORTE_USD	  */
	public String getREPORTE_USD () 
	{
		return (String)get_Value(COLUMNNAME_REPORTE_USD);
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

	/** Set SOLO_INDENT.
		@param SOLO_INDENT SOLO_INDENT	  */
	public void setSOLO_INDENT (boolean SOLO_INDENT)
	{
		set_Value (COLUMNNAME_SOLO_INDENT, Boolean.valueOf(SOLO_INDENT));
	}

	/** Get SOLO_INDENT.
		@return SOLO_INDENT	  */
	public boolean isSOLO_INDENT () 
	{
		Object oo = get_Value(COLUMNNAME_SOLO_INDENT);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** SOLUTEC AD_Reference_ID=319 */
	public static final int SOLUTEC_AD_Reference_ID=319;
	/** Yes = Y */
	public static final String SOLUTEC_Yes = "Y";
	/** No = N */
	public static final String SOLUTEC_No = "N";
	/** Set SOLUTEC.
		@param SOLUTEC SOLUTEC	  */
	public void setSOLUTEC (String SOLUTEC)
	{

		set_Value (COLUMNNAME_SOLUTEC, SOLUTEC);
	}

	/** Get SOLUTEC.
		@return SOLUTEC	  */
	public String getSOLUTEC () 
	{
		return (String)get_Value(COLUMNNAME_SOLUTEC);
	}

	/** Set T_BL_FLASH_PARAMETROS.
		@param T_BL_FLASH_PARAMETROS_ID T_BL_FLASH_PARAMETROS	  */
	public void setT_BL_FLASH_PARAMETROS_ID (int T_BL_FLASH_PARAMETROS_ID)
	{
		if (T_BL_FLASH_PARAMETROS_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_PARAMETROS_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_FLASH_PARAMETROS_ID, Integer.valueOf(T_BL_FLASH_PARAMETROS_ID));
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

	/** Set TOTAL_CANTIDAD.
		@param TOTAL_CANTIDAD TOTAL_CANTIDAD	  */
	public void setTOTAL_CANTIDAD (BigDecimal TOTAL_CANTIDAD)
	{
		set_Value (COLUMNNAME_TOTAL_CANTIDAD, TOTAL_CANTIDAD);
	}

	/** Get TOTAL_CANTIDAD.
		@return TOTAL_CANTIDAD	  */
	public BigDecimal getTOTAL_CANTIDAD () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TOTAL_CANTIDAD);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TOTAL_COSTO.
		@param TOTAL_COSTO TOTAL_COSTO	  */
	public void setTOTAL_COSTO (BigDecimal TOTAL_COSTO)
	{
		set_Value (COLUMNNAME_TOTAL_COSTO, TOTAL_COSTO);
	}

	/** Get TOTAL_COSTO.
		@return TOTAL_COSTO	  */
	public BigDecimal getTOTAL_COSTO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TOTAL_COSTO);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TOTAL_DOLARES.
		@param TOTAL_DOLARES TOTAL_DOLARES	  */
	public void setTOTAL_DOLARES (BigDecimal TOTAL_DOLARES)
	{
		set_Value (COLUMNNAME_TOTAL_DOLARES, TOTAL_DOLARES);
	}

	/** Get TOTAL_DOLARES.
		@return TOTAL_DOLARES	  */
	public BigDecimal getTOTAL_DOLARES () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TOTAL_DOLARES);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TOTAL_MARGEN.
		@param TOTAL_MARGEN TOTAL_MARGEN	  */
	public void setTOTAL_MARGEN (BigDecimal TOTAL_MARGEN)
	{
		set_Value (COLUMNNAME_TOTAL_MARGEN, TOTAL_MARGEN);
	}

	/** Get TOTAL_MARGEN.
		@return TOTAL_MARGEN	  */
	public BigDecimal getTOTAL_MARGEN () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TOTAL_MARGEN);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TOTAL_PESOS.
		@param TOTAL_PESOS TOTAL_PESOS	  */
	public void setTOTAL_PESOS (BigDecimal TOTAL_PESOS)
	{
		set_Value (COLUMNNAME_TOTAL_PESOS, TOTAL_PESOS);
	}

	/** Get TOTAL_PESOS.
		@return TOTAL_PESOS	  */
	public BigDecimal getTOTAL_PESOS () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TOTAL_PESOS);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TOTAL_UF.
		@param TOTAL_UF TOTAL_UF	  */
	public void setTOTAL_UF (BigDecimal TOTAL_UF)
	{
		set_Value (COLUMNNAME_TOTAL_UF, TOTAL_UF);
	}

	/** Get TOTAL_UF.
		@return TOTAL_UF	  */
	public BigDecimal getTOTAL_UF () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TOTAL_UF);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set USACARTERA.
		@param USACARTERA USACARTERA	  */
	public void setUSACARTERA (boolean USACARTERA)
	{
		set_Value (COLUMNNAME_USACARTERA, Boolean.valueOf(USACARTERA));
	}

	/** Get USACARTERA.
		@return USACARTERA	  */
	public boolean isUSACARTERA () 
	{
		Object oo = get_Value(COLUMNNAME_USACARTERA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set USA_FAMILIA_REPRESENTADA.
		@param USA_FAMILIA_REPRESENTADA USA_FAMILIA_REPRESENTADA	  */
	public void setUSA_FAMILIA_REPRESENTADA (boolean USA_FAMILIA_REPRESENTADA)
	{
		set_Value (COLUMNNAME_USA_FAMILIA_REPRESENTADA, Boolean.valueOf(USA_FAMILIA_REPRESENTADA));
	}

	/** Get USA_FAMILIA_REPRESENTADA.
		@return USA_FAMILIA_REPRESENTADA	  */
	public boolean isUSA_FAMILIA_REPRESENTADA () 
	{
		Object oo = get_Value(COLUMNNAME_USA_FAMILIA_REPRESENTADA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set VENTA_NUEVA.
		@param VENTA_NUEVA VENTA_NUEVA	  */
	public void setVENTA_NUEVA (boolean VENTA_NUEVA)
	{
		set_Value (COLUMNNAME_VENTA_NUEVA, Boolean.valueOf(VENTA_NUEVA));
	}

	/** Get VENTA_NUEVA.
		@return VENTA_NUEVA	  */
	public boolean isVENTA_NUEVA () 
	{
		Object oo = get_Value(COLUMNNAME_VENTA_NUEVA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set VER_COMPRAS.
		@param VER_COMPRAS VER_COMPRAS	  */
	public void setVER_COMPRAS (boolean VER_COMPRAS)
	{
		set_Value (COLUMNNAME_VER_COMPRAS, Boolean.valueOf(VER_COMPRAS));
	}

	/** Get VER_COMPRAS.
		@return VER_COMPRAS	  */
	public boolean isVER_COMPRAS () 
	{
		Object oo = get_Value(COLUMNNAME_VER_COMPRAS);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}