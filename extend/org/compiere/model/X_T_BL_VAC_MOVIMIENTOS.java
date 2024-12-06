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

/** Generated Model for T_BL_VAC_MOVIMIENTOS
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_VAC_MOVIMIENTOS extends PO implements I_T_BL_VAC_MOVIMIENTOS, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180316L;

    /** Standard Constructor */
    public X_T_BL_VAC_MOVIMIENTOS (Properties ctx, int T_BL_VAC_MOVIMIENTOS_ID, String trxName)
    {
      super (ctx, T_BL_VAC_MOVIMIENTOS_ID, trxName);
      /** if (T_BL_VAC_MOVIMIENTOS_ID == 0)
        {
			setC_BPartner_ID (0);
			setDESDE (new Timestamp( System.currentTimeMillis() ));
			setDIAS (Env.ZERO);
			setFECHA_MOVIMIENTO (new Timestamp( System.currentTimeMillis() ));
			setHASTA (new Timestamp( System.currentTimeMillis() ));
			setProcessed (false);
			setPROGRESIVO (false);
			setT_BL_VAC_MOVIMIENTOS_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_VAC_MOVIMIENTOS (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_VAC_MOVIMIENTOS[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set FECHA_MOVIMIENTO.
		@param FECHA_MOVIMIENTO FECHA_MOVIMIENTO	  */
	public void setFECHA_MOVIMIENTO (Timestamp FECHA_MOVIMIENTO)
	{
		set_Value (COLUMNNAME_FECHA_MOVIMIENTO, FECHA_MOVIMIENTO);
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

	/** Set T_BL_VAC_MOVIMIENTOS.
		@param T_BL_VAC_MOVIMIENTOS_ID T_BL_VAC_MOVIMIENTOS	  */
	public void setT_BL_VAC_MOVIMIENTOS_ID (int T_BL_VAC_MOVIMIENTOS_ID)
	{
		if (T_BL_VAC_MOVIMIENTOS_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_VAC_MOVIMIENTOS_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_VAC_MOVIMIENTOS_ID, Integer.valueOf(T_BL_VAC_MOVIMIENTOS_ID));
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

	public I_T_BL_VAC_SOLICITUD getT_BL_VAC_SOLICITUD() throws RuntimeException
    {
		return (I_T_BL_VAC_SOLICITUD)MTable.get(getCtx(), I_T_BL_VAC_SOLICITUD.Table_Name)
			.getPO(getT_BL_VAC_SOLICITUD_ID(), get_TrxName());	}

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