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

/** Generated Model for T_BL_VAC_SOLICITUD
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_VAC_SOLICITUD extends PO implements I_T_BL_VAC_SOLICITUD, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180316L;

    /** Standard Constructor */
    public X_T_BL_VAC_SOLICITUD (Properties ctx, int T_BL_VAC_SOLICITUD_ID, String trxName)
    {
      super (ctx, T_BL_VAC_SOLICITUD_ID, trxName);
      /** if (T_BL_VAC_SOLICITUD_ID == 0)
        {
			setC_BPartner_ID (0);
			setT_BL_VAC_SOLICITUD_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_VAC_SOLICITUD (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_VAC_SOLICITUD[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ANULAR.
		@param ANULAR ANULAR	  */
	public void setANULAR (boolean ANULAR)
	{
		set_Value (COLUMNNAME_ANULAR, Boolean.valueOf(ANULAR));
	}

	/** Get ANULAR.
		@return ANULAR	  */
	public boolean isANULAR () 
	{
		Object oo = get_Value(COLUMNNAME_ANULAR);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set APROBAR.
		@param APROBAR APROBAR	  */
	public void setAPROBAR (boolean APROBAR)
	{
		set_Value (COLUMNNAME_APROBAR, Boolean.valueOf(APROBAR));
	}

	/** Get APROBAR.
		@return APROBAR	  */
	public boolean isAPROBAR () 
	{
		Object oo = get_Value(COLUMNNAME_APROBAR);
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

	/** Set COMENTARIOS.
		@param COMENTARIOS COMENTARIOS	  */
	public void setCOMENTARIOS (String COMENTARIOS)
	{
		set_Value (COLUMNNAME_COMENTARIOS, COMENTARIOS);
	}

	/** Get COMENTARIOS.
		@return COMENTARIOS	  */
	public String getCOMENTARIOS () 
	{
		return (String)get_Value(COLUMNNAME_COMENTARIOS);
	}

	/** Set CONTROL_IMPRESION.
		@param CONTROL_IMPRESION CONTROL_IMPRESION	  */
	public void setCONTROL_IMPRESION (boolean CONTROL_IMPRESION)
	{
		set_Value (COLUMNNAME_CONTROL_IMPRESION, Boolean.valueOf(CONTROL_IMPRESION));
	}

	/** Get CONTROL_IMPRESION.
		@return CONTROL_IMPRESION	  */
	public boolean isCONTROL_IMPRESION () 
	{
		Object oo = get_Value(COLUMNNAME_CONTROL_IMPRESION);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set DIAS.
		@param DIAS DIAS	  */
	public void setDIAS (BigDecimal DIAS)
	{
		set_Value (COLUMNNAME_DIAS, DIAS);
	}

	/** Get DIAS.
		@return DIAS	  */
	public BigDecimal getDIAS () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DIAS);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set FECHA_MOVIMIENTO.
		@param FECHA_MOVIMIENTO FECHA_MOVIMIENTO	  */
	public void setFECHA_MOVIMIENTO (Timestamp FECHA_MOVIMIENTO)
	{
		set_ValueNoCheck (COLUMNNAME_FECHA_MOVIMIENTO, FECHA_MOVIMIENTO);
	}

	/** Get FECHA_MOVIMIENTO.
		@return FECHA_MOVIMIENTO	  */
	public Timestamp getFECHA_MOVIMIENTO () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FECHA_MOVIMIENTO);
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

	/** Set IMPRIME_COMPROBANTE.
		@param IMPRIME_COMPROBANTE IMPRIME_COMPROBANTE	  */
	public void setIMPRIME_COMPROBANTE (String IMPRIME_COMPROBANTE)
	{
		set_Value (COLUMNNAME_IMPRIME_COMPROBANTE, IMPRIME_COMPROBANTE);
	}

	/** Get IMPRIME_COMPROBANTE.
		@return IMPRIME_COMPROBANTE	  */
	public String getIMPRIME_COMPROBANTE () 
	{
		return (String)get_Value(COLUMNNAME_IMPRIME_COMPROBANTE);
	}

	/** Set IMPRIME_RESUMEN.
		@param IMPRIME_RESUMEN IMPRIME_RESUMEN	  */
	public void setIMPRIME_RESUMEN (String IMPRIME_RESUMEN)
	{
		set_Value (COLUMNNAME_IMPRIME_RESUMEN, IMPRIME_RESUMEN);
	}

	/** Get IMPRIME_RESUMEN.
		@return IMPRIME_RESUMEN	  */
	public String getIMPRIME_RESUMEN () 
	{
		return (String)get_Value(COLUMNNAME_IMPRIME_RESUMEN);
	}

	/** Set MEDIO_DIA.
		@param MEDIO_DIA MEDIO_DIA	  */
	public void setMEDIO_DIA (boolean MEDIO_DIA)
	{
		set_Value (COLUMNNAME_MEDIO_DIA, Boolean.valueOf(MEDIO_DIA));
	}

	/** Get MEDIO_DIA.
		@return MEDIO_DIA	  */
	public boolean isMEDIO_DIA () 
	{
		Object oo = get_Value(COLUMNNAME_MEDIO_DIA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set PROGRESIVO.
		@param PROGRESIVO PROGRESIVO	  */
	public void setPROGRESIVO (boolean PROGRESIVO)
	{
		set_Value (COLUMNNAME_PROGRESIVO, Boolean.valueOf(PROGRESIVO));
	}

	/** Get PROGRESIVO.
		@return PROGRESIVO	  */
	public boolean isPROGRESIVO () 
	{
		Object oo = get_Value(COLUMNNAME_PROGRESIVO);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set REVERTIR.
		@param REVERTIR REVERTIR	  */
	public void setREVERTIR (boolean REVERTIR)
	{
		set_Value (COLUMNNAME_REVERTIR, Boolean.valueOf(REVERTIR));
	}

	/** Get REVERTIR.
		@return REVERTIR	  */
	public boolean isREVERTIR () 
	{
		Object oo = get_Value(COLUMNNAME_REVERTIR);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set REVERTIR_BOTON.
		@param REVERTIR_BOTON REVERTIR_BOTON	  */
	public void setREVERTIR_BOTON (String REVERTIR_BOTON)
	{
		set_Value (COLUMNNAME_REVERTIR_BOTON, REVERTIR_BOTON);
	}

	/** Get REVERTIR_BOTON.
		@return REVERTIR_BOTON	  */
	public String getREVERTIR_BOTON () 
	{
		return (String)get_Value(COLUMNNAME_REVERTIR_BOTON);
	}

	/** Set SALDO_NORMAL.
		@param SALDO_NORMAL SALDO_NORMAL	  */
	public void setSALDO_NORMAL (BigDecimal SALDO_NORMAL)
	{
		set_Value (COLUMNNAME_SALDO_NORMAL, SALDO_NORMAL);
	}

	/** Get SALDO_NORMAL.
		@return SALDO_NORMAL	  */
	public BigDecimal getSALDO_NORMAL () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDO_NORMAL);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SALDO_PP.
		@param SALDO_PP SALDO_PP	  */
	public void setSALDO_PP (BigDecimal SALDO_PP)
	{
		set_Value (COLUMNNAME_SALDO_PP, SALDO_PP);
	}

	/** Get SALDO_PP.
		@return SALDO_PP	  */
	public BigDecimal getSALDO_PP () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDO_PP);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SALDO_PROGRESIVO.
		@param SALDO_PROGRESIVO SALDO_PROGRESIVO	  */
	public void setSALDO_PROGRESIVO (BigDecimal SALDO_PROGRESIVO)
	{
		set_Value (COLUMNNAME_SALDO_PROGRESIVO, SALDO_PROGRESIVO);
	}

	/** Get SALDO_PROGRESIVO.
		@return SALDO_PROGRESIVO	  */
	public BigDecimal getSALDO_PROGRESIVO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDO_PROGRESIVO);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SALDO_TOTAL.
		@param SALDO_TOTAL SALDO_TOTAL	  */
	public void setSALDO_TOTAL (BigDecimal SALDO_TOTAL)
	{
		set_Value (COLUMNNAME_SALDO_TOTAL, SALDO_TOTAL);
	}

	/** Get SALDO_TOTAL.
		@return SALDO_TOTAL	  */
	public BigDecimal getSALDO_TOTAL () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SALDO_TOTAL);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SOLICITAR.
		@param SOLICITAR SOLICITAR	  */
	public void setSOLICITAR (boolean SOLICITAR)
	{
		set_Value (COLUMNNAME_SOLICITAR, Boolean.valueOf(SOLICITAR));
	}

	/** Get SOLICITAR.
		@return SOLICITAR	  */
	public boolean isSOLICITAR () 
	{
		Object oo = get_Value(COLUMNNAME_SOLICITAR);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_T_BL_VAC_MOVIMIENTOS getT_BL_VAC_MOVIMIENTOS() throws RuntimeException
    {
		return (I_T_BL_VAC_MOVIMIENTOS)MTable.get(getCtx(), I_T_BL_VAC_MOVIMIENTOS.Table_Name)
			.getPO(getT_BL_VAC_MOVIMIENTOS_ID(), get_TrxName());	}

	/** Set T_BL_VAC_MOVIMIENTOS.
		@param T_BL_VAC_MOVIMIENTOS_ID T_BL_VAC_MOVIMIENTOS	  */
	public void setT_BL_VAC_MOVIMIENTOS_ID (int T_BL_VAC_MOVIMIENTOS_ID)
	{
		if (T_BL_VAC_MOVIMIENTOS_ID < 1) 
			set_Value (COLUMNNAME_T_BL_VAC_MOVIMIENTOS_ID, null);
		else 
			set_Value (COLUMNNAME_T_BL_VAC_MOVIMIENTOS_ID, Integer.valueOf(T_BL_VAC_MOVIMIENTOS_ID));
	}

	/** Get T_BL_VAC_MOVIMIENTOS.
		@return T_BL_VAC_MOVIMIENTOS	  */
	public int getT_BL_VAC_MOVIMIENTOS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_VAC_MOVIMIENTOS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set T_BL_VAC_SOLICITUD.
		@param T_BL_VAC_SOLICITUD_ID T_BL_VAC_SOLICITUD	  */
	public void setT_BL_VAC_SOLICITUD_ID (int T_BL_VAC_SOLICITUD_ID)
	{
		if (T_BL_VAC_SOLICITUD_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_VAC_SOLICITUD_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_VAC_SOLICITUD_ID, Integer.valueOf(T_BL_VAC_SOLICITUD_ID));
	}

	/** Get T_BL_VAC_SOLICITUD.
		@return T_BL_VAC_SOLICITUD	  */
	public int getT_BL_VAC_SOLICITUD_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_VAC_SOLICITUD_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** TIPO_MOVIMIENTO AD_Reference_ID=1000064 */
	public static final int TIPO_MOVIMIENTO_AD_Reference_ID=1000064;
	/** Abono = A */
	public static final String TIPO_MOVIMIENTO_Abono = "A";
	/** Cargo = C */
	public static final String TIPO_MOVIMIENTO_Cargo = "C";
	/** Movimiento Importado Sistema Anterior = B */
	public static final String TIPO_MOVIMIENTO_MovimientoImportadoSistemaAnterior = "B";
	/** Set TIPO_MOVIMIENTO.
		@param TIPO_MOVIMIENTO TIPO_MOVIMIENTO	  */
	public void setTIPO_MOVIMIENTO (String TIPO_MOVIMIENTO)
	{

		set_Value (COLUMNNAME_TIPO_MOVIMIENTO, TIPO_MOVIMIENTO);
	}

	/** Get TIPO_MOVIMIENTO.
		@return TIPO_MOVIMIENTO	  */
	public String getTIPO_MOVIMIENTO () 
	{
		return (String)get_Value(COLUMNNAME_TIPO_MOVIMIENTO);
	}
}