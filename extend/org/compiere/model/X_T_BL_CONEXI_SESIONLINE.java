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

/** Generated Model for T_BL_CONEXI_SESIONLINE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_CONEXI_SESIONLINE extends PO implements I_T_BL_CONEXI_SESIONLINE, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180705L;

    /** Standard Constructor */
    public X_T_BL_CONEXI_SESIONLINE (Properties ctx, int T_BL_CONEXI_SESIONLINE_ID, String trxName)
    {
      super (ctx, T_BL_CONEXI_SESIONLINE_ID, trxName);
      /** if (T_BL_CONEXI_SESIONLINE_ID == 0)
        {
			setT_BL_CONEXI_SESIONLINE_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_CONEXI_SESIONLINE (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_CONEXI_SESIONLINE[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set CANTIDAD.
		@param CANTIDAD CANTIDAD	  */
	public void setCANTIDAD (BigDecimal CANTIDAD)
	{
		set_Value (COLUMNNAME_CANTIDAD, CANTIDAD);
	}

	/** Get CANTIDAD.
		@return CANTIDAD	  */
	public BigDecimal getCANTIDAD () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CANTIDAD);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set CONCEPTO.
		@param CONCEPTO CONCEPTO	  */
	public void setCONCEPTO (String CONCEPTO)
	{
		set_Value (COLUMNNAME_CONCEPTO, CONCEPTO);
	}

	/** Get CONCEPTO.
		@return CONCEPTO	  */
	public String getCONCEPTO () 
	{
		return (String)get_Value(COLUMNNAME_CONCEPTO);
	}

	/** Set CONCEPTO_LINK.
		@param CONCEPTO_LINK CONCEPTO_LINK	  */
	public void setCONCEPTO_LINK (String CONCEPTO_LINK)
	{
		set_Value (COLUMNNAME_CONCEPTO_LINK, CONCEPTO_LINK);
	}

	/** Get CONCEPTO_LINK.
		@return CONCEPTO_LINK	  */
	public String getCONCEPTO_LINK () 
	{
		return (String)get_Value(COLUMNNAME_CONCEPTO_LINK);
	}

	public I_C_OrderLine getC_OrderLine() throws RuntimeException
    {
		return (I_C_OrderLine)MTable.get(getCtx(), I_C_OrderLine.Table_Name)
			.getPO(getC_OrderLine_ID(), get_TrxName());	}

	/** Set Sales Order Line.
		@param C_OrderLine_ID 
		Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID)
	{
		if (C_OrderLine_ID < 1) 
			set_Value (COLUMNNAME_C_OrderLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_OrderLine_ID, Integer.valueOf(C_OrderLine_ID));
	}

	/** Get Sales Order Line.
		@return Sales Order Line
	  */
	public int getC_OrderLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderLine_ID);
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

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
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

	/** Set DES_PRODUCTO.
		@param DES_PRODUCTO DES_PRODUCTO	  */
	public void setDES_PRODUCTO (String DES_PRODUCTO)
	{
		set_Value (COLUMNNAME_DES_PRODUCTO, DES_PRODUCTO);
	}

	/** Get DES_PRODUCTO.
		@return DES_PRODUCTO	  */
	public String getDES_PRODUCTO () 
	{
		return (String)get_Value(COLUMNNAME_DES_PRODUCTO);
	}

	/** Set H_O_P.
		@param H_O_P H_O_P	  */
	public void setH_O_P (String H_O_P)
	{
		set_Value (COLUMNNAME_H_O_P, H_O_P);
	}

	/** Get H_O_P.
		@return H_O_P	  */
	public String getH_O_P () 
	{
		return (String)get_Value(COLUMNNAME_H_O_P);
	}

	/** Set Sales Transaction.
		@param IsSOTrx 
		This is a Sales Transaction
	  */
	public void setIsSOTrx (boolean IsSOTrx)
	{
		set_Value (COLUMNNAME_IsSOTrx, Boolean.valueOf(IsSOTrx));
	}

	/** Get Sales Transaction.
		@return This is a Sales Transaction
	  */
	public boolean isSOTrx () 
	{
		Object oo = get_Value(COLUMNNAME_IsSOTrx);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_GL_Journal getLine() throws RuntimeException
    {
		return (I_GL_Journal)MTable.get(getCtx(), I_GL_Journal.Table_Name)
			.getPO(getLine_ID(), get_TrxName());	}

	/** Set Line ID.
		@param Line_ID 
		Transaction line ID (internal)
	  */
	public void setLine_ID (int Line_ID)
	{
		if (Line_ID < 1) 
			set_Value (COLUMNNAME_Line_ID, null);
		else 
			set_Value (COLUMNNAME_Line_ID, Integer.valueOf(Line_ID));
	}

	/** Get Line ID.
		@return Transaction line ID (internal)
	  */
	public int getLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_InOutLine getM_InOutLine() throws RuntimeException
    {
		return (I_M_InOutLine)MTable.get(getCtx(), I_M_InOutLine.Table_Name)
			.getPO(getM_InOutLine_ID(), get_TrxName());	}

	/** Set Shipment/Receipt Line.
		@param M_InOutLine_ID 
		Line on Shipment or Receipt document
	  */
	public void setM_InOutLine_ID (int M_InOutLine_ID)
	{
		if (M_InOutLine_ID < 1) 
			set_Value (COLUMNNAME_M_InOutLine_ID, null);
		else 
			set_Value (COLUMNNAME_M_InOutLine_ID, Integer.valueOf(M_InOutLine_ID));
	}

	/** Get Shipment/Receipt Line.
		@return Line on Shipment or Receipt document
	  */
	public int getM_InOutLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_InOutLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_M_InventoryLine getM_InventoryLine() throws RuntimeException
    {
		return (I_M_InventoryLine)MTable.get(getCtx(), I_M_InventoryLine.Table_Name)
			.getPO(getM_InventoryLine_ID(), get_TrxName());	}

	/** Set Phys.Inventory Line.
		@param M_InventoryLine_ID 
		Unique line in an Inventory document
	  */
	public void setM_InventoryLine_ID (int M_InventoryLine_ID)
	{
		if (M_InventoryLine_ID < 1) 
			set_Value (COLUMNNAME_M_InventoryLine_ID, null);
		else 
			set_Value (COLUMNNAME_M_InventoryLine_ID, Integer.valueOf(M_InventoryLine_ID));
	}

	/** Get Phys.Inventory Line.
		@return Unique line in an Inventory document
	  */
	public int getM_InventoryLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_InventoryLine_ID);
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

	public I_M_MovementLine getM_MovementLine() throws RuntimeException
    {
		return (I_M_MovementLine)MTable.get(getCtx(), I_M_MovementLine.Table_Name)
			.getPO(getM_MovementLine_ID(), get_TrxName());	}

	/** Set Move Line.
		@param M_MovementLine_ID 
		Inventory Move document Line
	  */
	public void setM_MovementLine_ID (int M_MovementLine_ID)
	{
		if (M_MovementLine_ID < 1) 
			set_Value (COLUMNNAME_M_MovementLine_ID, null);
		else 
			set_Value (COLUMNNAME_M_MovementLine_ID, Integer.valueOf(M_MovementLine_ID));
	}

	/** Get Move Line.
		@return Inventory Move document Line
	  */
	public int getM_MovementLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_MovementLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_M_ProductionLine getM_ProductionLine() throws RuntimeException
    {
		return (I_M_ProductionLine)MTable.get(getCtx(), I_M_ProductionLine.Table_Name)
			.getPO(getM_ProductionLine_ID(), get_TrxName());	}

	/** Set Production Line.
		@param M_ProductionLine_ID 
		Document Line representing a production
	  */
	public void setM_ProductionLine_ID (int M_ProductionLine_ID)
	{
		if (M_ProductionLine_ID < 1) 
			set_Value (COLUMNNAME_M_ProductionLine_ID, null);
		else 
			set_Value (COLUMNNAME_M_ProductionLine_ID, Integer.valueOf(M_ProductionLine_ID));
	}

	/** Get Production Line.
		@return Document Line representing a production
	  */
	public int getM_ProductionLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ProductionLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ORIGEN.
		@param ORIGEN ORIGEN	  */
	public void setORIGEN (String ORIGEN)
	{
		set_Value (COLUMNNAME_ORIGEN, ORIGEN);
	}

	/** Get ORIGEN.
		@return ORIGEN	  */
	public String getORIGEN () 
	{
		return (String)get_Value(COLUMNNAME_ORIGEN);
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

	public I_T_BL_CONEXI_PROYLINE getT_BL_CONEXI_PROYLINE() throws RuntimeException
    {
		return (I_T_BL_CONEXI_PROYLINE)MTable.get(getCtx(), I_T_BL_CONEXI_PROYLINE.Table_Name)
			.getPO(getT_BL_CONEXI_PROYLINE_ID(), get_TrxName());	}

	/** Set T_BL_CONEXI_PROYLINE.
		@param T_BL_CONEXI_PROYLINE_ID T_BL_CONEXI_PROYLINE	  */
	public void setT_BL_CONEXI_PROYLINE_ID (int T_BL_CONEXI_PROYLINE_ID)
	{
		if (T_BL_CONEXI_PROYLINE_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_PROYLINE_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_PROYLINE_ID, Integer.valueOf(T_BL_CONEXI_PROYLINE_ID));
	}

	/** Get T_BL_CONEXI_PROYLINE.
		@return T_BL_CONEXI_PROYLINE	  */
	public int getT_BL_CONEXI_PROYLINE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CONEXI_PROYLINE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_T_BL_CONEXI_SESION getT_BL_CONEXI_SESION() throws RuntimeException
    {
		return (I_T_BL_CONEXI_SESION)MTable.get(getCtx(), I_T_BL_CONEXI_SESION.Table_Name)
			.getPO(getT_BL_CONEXI_SESION_ID(), get_TrxName());	}

	/** Set T_BL_CONEXI_SESION.
		@param T_BL_CONEXI_SESION_ID T_BL_CONEXI_SESION	  */
	public void setT_BL_CONEXI_SESION_ID (int T_BL_CONEXI_SESION_ID)
	{
		if (T_BL_CONEXI_SESION_ID < 1) 
			set_Value (COLUMNNAME_T_BL_CONEXI_SESION_ID, null);
		else 
			set_Value (COLUMNNAME_T_BL_CONEXI_SESION_ID, Integer.valueOf(T_BL_CONEXI_SESION_ID));
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

	/** Set T_BL_CONEXI_SESIONLINE.
		@param T_BL_CONEXI_SESIONLINE_ID T_BL_CONEXI_SESIONLINE	  */
	public void setT_BL_CONEXI_SESIONLINE_ID (int T_BL_CONEXI_SESIONLINE_ID)
	{
		if (T_BL_CONEXI_SESIONLINE_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_SESIONLINE_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CONEXI_SESIONLINE_ID, Integer.valueOf(T_BL_CONEXI_SESIONLINE_ID));
	}

	/** Get T_BL_CONEXI_SESIONLINE.
		@return T_BL_CONEXI_SESIONLINE	  */
	public int getT_BL_CONEXI_SESIONLINE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CONEXI_SESIONLINE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}