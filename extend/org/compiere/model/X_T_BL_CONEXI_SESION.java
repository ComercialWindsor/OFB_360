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
import java.util.Properties;

/** Generated Model for T_BL_CONEXI_SESION
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_CONEXI_SESION extends PO implements I_T_BL_CONEXI_SESION, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180705L;

    /** Standard Constructor */
    public X_T_BL_CONEXI_SESION (Properties ctx, int T_BL_CONEXI_SESION_ID, String trxName)
    {
      super (ctx, T_BL_CONEXI_SESION_ID, trxName);
      /** if (T_BL_CONEXI_SESION_ID == 0)
        {
			setT_BL_CONEXI_SESION_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_CONEXI_SESION (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_CONEXI_SESION[")
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

	/** Set COD_BLUMOS.
		@param COD_BLUMOS COD_BLUMOS	  */
	public void setCOD_BLUMOS (String COD_BLUMOS)
	{
		set_Value (COLUMNNAME_COD_BLUMOS, COD_BLUMOS);
	}

	/** Get COD_BLUMOS.
		@return COD_BLUMOS	  */
	public String getCOD_BLUMOS () 
	{
		return (String)get_Value(COLUMNNAME_COD_BLUMOS);
	}

	/** Set CORREO_ENVIADO.
		@param CORREO_ENVIADO CORREO_ENVIADO	  */
	public void setCORREO_ENVIADO (boolean CORREO_ENVIADO)
	{
		set_Value (COLUMNNAME_CORREO_ENVIADO, Boolean.valueOf(CORREO_ENVIADO));
	}

	/** Get CORREO_ENVIADO.
		@return CORREO_ENVIADO	  */
	public boolean isCORREO_ENVIADO () 
	{
		Object oo = get_Value(COLUMNNAME_CORREO_ENVIADO);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set CRITERIOS.
		@param CRITERIOS CRITERIOS	  */
	public void setCRITERIOS (String CRITERIOS)
	{
		set_Value (COLUMNNAME_CRITERIOS, CRITERIOS);
	}

	/** Get CRITERIOS.
		@return CRITERIOS	  */
	public String getCRITERIOS () 
	{
		return (String)get_Value(COLUMNNAME_CRITERIOS);
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

	/** Set ENVIAR_CORREO.
		@param ENVIAR_CORREO ENVIAR_CORREO	  */
	public void setENVIAR_CORREO (boolean ENVIAR_CORREO)
	{
		set_Value (COLUMNNAME_ENVIAR_CORREO, Boolean.valueOf(ENVIAR_CORREO));
	}

	/** Get ENVIAR_CORREO.
		@return ENVIAR_CORREO	  */
	public boolean isENVIAR_CORREO () 
	{
		Object oo = get_Value(COLUMNNAME_ENVIAR_CORREO);
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

	/** Set POR_REPRESENTADA.
		@param POR_REPRESENTADA POR_REPRESENTADA	  */
	public void setPOR_REPRESENTADA (boolean POR_REPRESENTADA)
	{
		set_Value (COLUMNNAME_POR_REPRESENTADA, Boolean.valueOf(POR_REPRESENTADA));
	}

	/** Get POR_REPRESENTADA.
		@return POR_REPRESENTADA	  */
	public boolean isPOR_REPRESENTADA () 
	{
		Object oo = get_Value(COLUMNNAME_POR_REPRESENTADA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set POR_USUARIO_COMPRAS.
		@param POR_USUARIO_COMPRAS POR_USUARIO_COMPRAS	  */
	public void setPOR_USUARIO_COMPRAS (boolean POR_USUARIO_COMPRAS)
	{
		set_Value (COLUMNNAME_POR_USUARIO_COMPRAS, Boolean.valueOf(POR_USUARIO_COMPRAS));
	}

	/** Get POR_USUARIO_COMPRAS.
		@return POR_USUARIO_COMPRAS	  */
	public boolean isPOR_USUARIO_COMPRAS () 
	{
		Object oo = get_Value(COLUMNNAME_POR_USUARIO_COMPRAS);
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

	/** Set PRODUCTO_SOLUTEC.
		@param PRODUCTO_SOLUTEC PRODUCTO_SOLUTEC	  */
	public void setPRODUCTO_SOLUTEC (boolean PRODUCTO_SOLUTEC)
	{
		set_Value (COLUMNNAME_PRODUCTO_SOLUTEC, Boolean.valueOf(PRODUCTO_SOLUTEC));
	}

	/** Get PRODUCTO_SOLUTEC.
		@return PRODUCTO_SOLUTEC	  */
	public boolean isPRODUCTO_SOLUTEC () 
	{
		Object oo = get_Value(COLUMNNAME_PRODUCTO_SOLUTEC);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set REPORTE.
		@param REPORTE REPORTE	  */
	public void setREPORTE (String REPORTE)
	{
		set_Value (COLUMNNAME_REPORTE, REPORTE);
	}

	/** Get REPORTE.
		@return REPORTE	  */
	public String getREPORTE () 
	{
		return (String)get_Value(COLUMNNAME_REPORTE);
	}

	public I_AD_User getRESPONSABLE_PROYECCIONES() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getRESPONSABLE_PROYECCIONES_ID(), get_TrxName());	}

	/** Set RESPONSABLE_PROYECCIONES_ID.
		@param RESPONSABLE_PROYECCIONES_ID RESPONSABLE_PROYECCIONES_ID	  */
	public void setRESPONSABLE_PROYECCIONES_ID (int RESPONSABLE_PROYECCIONES_ID)
	{
		if (RESPONSABLE_PROYECCIONES_ID < 1) 
			set_Value (COLUMNNAME_RESPONSABLE_PROYECCIONES_ID, null);
		else 
			set_Value (COLUMNNAME_RESPONSABLE_PROYECCIONES_ID, Integer.valueOf(RESPONSABLE_PROYECCIONES_ID));
	}

	/** Get RESPONSABLE_PROYECCIONES_ID.
		@return RESPONSABLE_PROYECCIONES_ID	  */
	public int getRESPONSABLE_PROYECCIONES_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RESPONSABLE_PROYECCIONES_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set T_BL_CONEXI_SESION.
		@param T_BL_CONEXI_SESION_ID T_BL_CONEXI_SESION	  */
	public void setT_BL_CONEXI_SESION_ID (int T_BL_CONEXI_SESION_ID)
	{
		if (T_BL_CONEXI_SESION_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_SESION_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_SESION_ID, Integer.valueOf(T_BL_CONEXI_SESION_ID));
	}

	/** Get T_BL_CONEXI_SESION.
		@return T_BL_CONEXI_SESION	  */
	public int getT_BL_CONEXI_SESION_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CONEXI_SESION_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AD_User getUSUARIO_COMPRAS() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getUSUARIO_COMPRAS_ID(), get_TrxName());	}

	/** Set USUARIO_COMPRAS_ID.
		@param USUARIO_COMPRAS_ID USUARIO_COMPRAS_ID	  */
	public void setUSUARIO_COMPRAS_ID (int USUARIO_COMPRAS_ID)
	{
		if (USUARIO_COMPRAS_ID < 1) 
			set_Value (COLUMNNAME_USUARIO_COMPRAS_ID, null);
		else 
			set_Value (COLUMNNAME_USUARIO_COMPRAS_ID, Integer.valueOf(USUARIO_COMPRAS_ID));
	}

	/** Get USUARIO_COMPRAS_ID.
		@return USUARIO_COMPRAS_ID	  */
	public int getUSUARIO_COMPRAS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_USUARIO_COMPRAS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}