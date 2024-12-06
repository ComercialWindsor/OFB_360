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

/** Generated Model for T_SEGUIMIENTO
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_SEGUIMIENTO extends PO implements I_T_SEGUIMIENTO, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180808L;

    /** Standard Constructor */
    public X_T_SEGUIMIENTO (Properties ctx, int T_SEGUIMIENTO_ID, String trxName)
    {
      super (ctx, T_SEGUIMIENTO_ID, trxName);
      /** if (T_SEGUIMIENTO_ID == 0)
        {
			setC_BPartner_ID (0);
// @C_BPartner_ID
			setM_Locator_ID (0);
			setProcessed (false);
			setREPRESENTADA_ID (0);
			setT_SEGUIMIENTO_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_SEGUIMIENTO (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_SEGUIMIENTO[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_User getAD_User() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AD_User getAD_USER_ID_COMENTA() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getAD_USER_ID_COMENTARIO(), get_TrxName());	}

	/** Set AD_USER_ID_COMENTARIO.
		@param AD_USER_ID_COMENTARIO AD_USER_ID_COMENTARIO	  */
	public void setAD_USER_ID_COMENTARIO (int AD_USER_ID_COMENTARIO)
	{
		set_Value (COLUMNNAME_AD_USER_ID_COMENTARIO, Integer.valueOf(AD_USER_ID_COMENTARIO));
	}

	/** Get AD_USER_ID_COMENTARIO.
		@return AD_USER_ID_COMENTARIO	  */
	public int getAD_USER_ID_COMENTARIO () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_USER_ID_COMENTARIO);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set APROBACION_MUESTRA.
		@param APROBACION_MUESTRA APROBACION_MUESTRA	  */
	public void setAPROBACION_MUESTRA (boolean APROBACION_MUESTRA)
	{
		set_Value (COLUMNNAME_APROBACION_MUESTRA, Boolean.valueOf(APROBACION_MUESTRA));
	}

	/** Get APROBACION_MUESTRA.
		@return APROBACION_MUESTRA	  */
	public boolean isAPROBACION_MUESTRA () 
	{
		Object oo = get_Value(COLUMNNAME_APROBACION_MUESTRA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set BODEGA_SOLUTEC.
		@param BODEGA_SOLUTEC BODEGA_SOLUTEC	  */
	public void setBODEGA_SOLUTEC (boolean BODEGA_SOLUTEC)
	{
		set_Value (COLUMNNAME_BODEGA_SOLUTEC, Boolean.valueOf(BODEGA_SOLUTEC));
	}

	/** Get BODEGA_SOLUTEC.
		@return BODEGA_SOLUTEC	  */
	public boolean isBODEGA_SOLUTEC () 
	{
		Object oo = get_Value(COLUMNNAME_BODEGA_SOLUTEC);
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

	/** Set C_BPartner_ID.
		@param C_BPartner_ID C_BPartner_ID	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get C_BPartner_ID.
		@return C_BPartner_ID	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
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

	public I_C_ProjectOFB getC_ProjectOFB() throws RuntimeException
    {
		return (I_C_ProjectOFB)MTable.get(getCtx(), I_C_ProjectOFB.Table_Name)
			.getPO(getC_ProjectOFB_ID(), get_TrxName());	}

	/** Set C_ProjectOFB_ID.
		@param C_ProjectOFB_ID C_ProjectOFB_ID	  */
	public void setC_ProjectOFB_ID (int C_ProjectOFB_ID)
	{
		if (C_ProjectOFB_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_ProjectOFB_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_ProjectOFB_ID, Integer.valueOf(C_ProjectOFB_ID));
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

	/** Set DESPACHADO_BODEGA.
		@param DESPACHADO_BODEGA DESPACHADO_BODEGA	  */
	public void setDESPACHADO_BODEGA (boolean DESPACHADO_BODEGA)
	{
		set_Value (COLUMNNAME_DESPACHADO_BODEGA, Boolean.valueOf(DESPACHADO_BODEGA));
	}

	/** Get DESPACHADO_BODEGA.
		@return DESPACHADO_BODEGA	  */
	public boolean isDESPACHADO_BODEGA () 
	{
		Object oo = get_Value(COLUMNNAME_DESPACHADO_BODEGA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set EMail Address.
		@param EMail 
		Electronic Mail Address
	  */
	public void setEMail (boolean EMail)
	{
		set_Value (COLUMNNAME_EMail, Boolean.valueOf(EMail));
	}

	/** Get EMail Address.
		@return Electronic Mail Address
	  */
	public boolean isEMail () 
	{
		Object oo = get_Value(COLUMNNAME_EMail);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set EMITECOMPROBANTE.
		@param EMITECOMPROBANTE EMITECOMPROBANTE	  */
	public void setEMITECOMPROBANTE (String EMITECOMPROBANTE)
	{
		set_Value (COLUMNNAME_EMITECOMPROBANTE, EMITECOMPROBANTE);
	}

	/** Get EMITECOMPROBANTE.
		@return EMITECOMPROBANTE	  */
	public String getEMITECOMPROBANTE () 
	{
		return (String)get_Value(COLUMNNAME_EMITECOMPROBANTE);
	}

	/** Set FECHA.
		@param FECHA FECHA	  */
	public void setFECHA (Timestamp FECHA)
	{
		set_Value (COLUMNNAME_FECHA, FECHA);
	}

	/** Get FECHA.
		@return FECHA	  */
	public Timestamp getFECHA () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FECHA);
	}

	/** Set LLAMADA.
		@param LLAMADA LLAMADA	  */
	public void setLLAMADA (boolean LLAMADA)
	{
		set_Value (COLUMNNAME_LLAMADA, Boolean.valueOf(LLAMADA));
	}

	/** Get LLAMADA.
		@return LLAMADA	  */
	public boolean isLLAMADA () 
	{
		Object oo = get_Value(COLUMNNAME_LLAMADA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_M_AttributeSetInstance getM_AttributeSetInstance() throws RuntimeException
    {
		return (I_M_AttributeSetInstance)MTable.get(getCtx(), I_M_AttributeSetInstance.Table_Name)
			.getPO(getM_AttributeSetInstance_ID(), get_TrxName());	}

	/** Set Attribute Set Instance.
		@param M_AttributeSetInstance_ID 
		Product Attribute Set Instance
	  */
	public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
	{
		if (M_AttributeSetInstance_ID < 0) 
			set_Value (COLUMNNAME_M_AttributeSetInstance_ID, null);
		else 
			set_Value (COLUMNNAME_M_AttributeSetInstance_ID, Integer.valueOf(M_AttributeSetInstance_ID));
	}

	/** Get Attribute Set Instance.
		@return Product Attribute Set Instance
	  */
	public int getM_AttributeSetInstance_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_AttributeSetInstance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_Locator getM_Locator() throws RuntimeException
    {
		return (I_M_Locator)MTable.get(getCtx(), I_M_Locator.Table_Name)
			.getPO(getM_Locator_ID(), get_TrxName());	}

	/** Set Locator.
		@param M_Locator_ID 
		Warehouse Locator
	  */
	public void setM_Locator_ID (int M_Locator_ID)
	{
		if (M_Locator_ID < 1) 
			set_Value (COLUMNNAME_M_Locator_ID, null);
		else 
			set_Value (COLUMNNAME_M_Locator_ID, Integer.valueOf(M_Locator_ID));
	}

	/** Get Locator.
		@return Warehouse Locator
	  */
	public int getM_Locator_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Locator_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Motivo.
		@param Motivo Motivo	  */
	public void setMotivo (boolean Motivo)
	{
		set_Value (COLUMNNAME_Motivo, Boolean.valueOf(Motivo));
	}

	/** Get Motivo.
		@return Motivo	  */
	public boolean isMotivo () 
	{
		Object oo = get_Value(COLUMNNAME_Motivo);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** M_PriceList_ID AD_Reference_ID=1000068 */
	public static final int M_PRICELIST_ID_AD_Reference_ID=1000068;
	/** Productos de Stock = 1001715 */
	public static final String M_PRICELIST_ID_ProductosDeStock = "1001715";
	/** Muestras = 1003343 */
	public static final String M_PRICELIST_ID_Muestras = "1003343";
	/** Set Price List.
		@param M_PriceList_ID 
		Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (String M_PriceList_ID)
	{

		set_Value (COLUMNNAME_M_PriceList_ID, M_PriceList_ID);
	}

	/** Get Price List.
		@return Unique identifier of a Price List
	  */
	public String getM_PriceList_ID () 
	{
		return (String)get_Value(COLUMNNAME_M_PriceList_ID);
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

	/** Set NOMBRE_MUESTRA.
		@param NOMBRE_MUESTRA NOMBRE_MUESTRA	  */
	public void setNOMBRE_MUESTRA (String NOMBRE_MUESTRA)
	{
		set_Value (COLUMNNAME_NOMBRE_MUESTRA, NOMBRE_MUESTRA);
	}

	/** Get NOMBRE_MUESTRA.
		@return NOMBRE_MUESTRA	  */
	public String getNOMBRE_MUESTRA () 
	{
		return (String)get_Value(COLUMNNAME_NOMBRE_MUESTRA);
	}

	/** Set ORIGEN_MUESTRA.
		@param ORIGEN_MUESTRA ORIGEN_MUESTRA	  */
	public void setORIGEN_MUESTRA (boolean ORIGEN_MUESTRA)
	{
		set_Value (COLUMNNAME_ORIGEN_MUESTRA, Boolean.valueOf(ORIGEN_MUESTRA));
	}

	/** Get ORIGEN_MUESTRA.
		@return ORIGEN_MUESTRA	  */
	public boolean isORIGEN_MUESTRA () 
	{
		Object oo = get_Value(COLUMNNAME_ORIGEN_MUESTRA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set PorcentajeSierre.
		@param PorcentajeSierre PorcentajeSierre	  */
	public void setPorcentajeSierre (int PorcentajeSierre)
	{
		set_Value (COLUMNNAME_PorcentajeSierre, Integer.valueOf(PorcentajeSierre));
	}

	/** Get PorcentajeSierre.
		@return PorcentajeSierre	  */
	public int getPorcentajeSierre () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PorcentajeSierre);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set POTENCIAL_VENTA.
		@param POTENCIAL_VENTA POTENCIAL_VENTA	  */
	public void setPOTENCIAL_VENTA (BigDecimal POTENCIAL_VENTA)
	{
		set_Value (COLUMNNAME_POTENCIAL_VENTA, POTENCIAL_VENTA);
	}

	/** Get POTENCIAL_VENTA.
		@return POTENCIAL_VENTA	  */
	public BigDecimal getPOTENCIAL_VENTA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_POTENCIAL_VENTA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set QTY_DELIVERED.
		@param QTY_DELIVERED QTY_DELIVERED	  */
	public void setQTY_DELIVERED (BigDecimal QTY_DELIVERED)
	{
		set_Value (COLUMNNAME_QTY_DELIVERED, QTY_DELIVERED);
	}

	/** Get QTY_DELIVERED.
		@return QTY_DELIVERED	  */
	public BigDecimal getQTY_DELIVERED () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QTY_DELIVERED);
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

	/** Set RESPUESTA.
		@param RESPUESTA RESPUESTA	  */
	public void setRESPUESTA (String RESPUESTA)
	{
		set_Value (COLUMNNAME_RESPUESTA, RESPUESTA);
	}

	/** Get RESPUESTA.
		@return RESPUESTA	  */
	public String getRESPUESTA () 
	{
		return (String)get_Value(COLUMNNAME_RESPUESTA);
	}

	/** Set SOLICITAR_MUESTRA.
		@param SOLICITAR_MUESTRA SOLICITAR_MUESTRA	  */
	public void setSOLICITAR_MUESTRA (boolean SOLICITAR_MUESTRA)
	{
		set_Value (COLUMNNAME_SOLICITAR_MUESTRA, Boolean.valueOf(SOLICITAR_MUESTRA));
	}

	/** Get SOLICITAR_MUESTRA.
		@return SOLICITAR_MUESTRA	  */
	public boolean isSOLICITAR_MUESTRA () 
	{
		Object oo = get_Value(COLUMNNAME_SOLICITAR_MUESTRA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set T_SEGUIMIENTO.
		@param T_SEGUIMIENTO_ID T_SEGUIMIENTO	  */
	public void setT_SEGUIMIENTO_ID (int T_SEGUIMIENTO_ID)
	{
		if (T_SEGUIMIENTO_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_SEGUIMIENTO_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_SEGUIMIENTO_ID, Integer.valueOf(T_SEGUIMIENTO_ID));
	}

	/** Get T_SEGUIMIENTO.
		@return T_SEGUIMIENTO	  */
	public int getT_SEGUIMIENTO_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_SEGUIMIENTO_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set VALIDACION.
		@param VALIDACION VALIDACION	  */
	public void setVALIDACION (boolean VALIDACION)
	{
		set_Value (COLUMNNAME_VALIDACION, Boolean.valueOf(VALIDACION));
	}

	/** Get VALIDACION.
		@return VALIDACION	  */
	public boolean isVALIDACION () 
	{
		Object oo = get_Value(COLUMNNAME_VALIDACION);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set VISITA.
		@param VISITA VISITA	  */
	public void setVISITA (boolean VISITA)
	{
		set_Value (COLUMNNAME_VISITA, Boolean.valueOf(VISITA));
	}

	/** Get VISITA.
		@return VISITA	  */
	public boolean isVISITA () 
	{
		Object oo = get_Value(COLUMNNAME_VISITA);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}