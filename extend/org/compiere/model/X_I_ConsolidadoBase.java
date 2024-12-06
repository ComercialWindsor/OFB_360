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

/** Generated Model for I_ConsolidadoBase
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_ConsolidadoBase extends PO implements I_I_ConsolidadoBase, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20200616L;

    /** Standard Constructor */
    public X_I_ConsolidadoBase (Properties ctx, int I_ConsolidadoBase_ID, String trxName)
    {
      super (ctx, I_ConsolidadoBase_ID, trxName);
      /** if (I_ConsolidadoBase_ID == 0)
        {
			setI_ConsolidadoBase_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_ConsolidadoBase (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_ConsolidadoBase[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ComprasCOPEC.
		@param ComprasCOPEC ComprasCOPEC	  */
	public void setComprasCOPEC (BigDecimal ComprasCOPEC)
	{
		set_Value (COLUMNNAME_ComprasCOPEC, ComprasCOPEC);
	}

	/** Get ComprasCOPEC.
		@return ComprasCOPEC	  */
	public BigDecimal getComprasCOPEC () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ComprasCOPEC);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ComprasENAP.
		@param ComprasENAP ComprasENAP	  */
	public void setComprasENAP (BigDecimal ComprasENAP)
	{
		set_Value (COLUMNNAME_ComprasENAP, ComprasENAP);
	}

	/** Get ComprasENAP.
		@return ComprasENAP	  */
	public BigDecimal getComprasENAP () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ComprasENAP);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ComprasENEX.
		@param ComprasENEX ComprasENEX	  */
	public void setComprasENEX (BigDecimal ComprasENEX)
	{
		set_Value (COLUMNNAME_ComprasENEX, ComprasENEX);
	}

	/** Get ComprasENEX.
		@return ComprasENEX	  */
	public BigDecimal getComprasENEX () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ComprasENEX);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ComprasESMAX.
		@param ComprasESMAX ComprasESMAX	  */
	public void setComprasESMAX (BigDecimal ComprasESMAX)
	{
		set_Value (COLUMNNAME_ComprasESMAX, ComprasESMAX);
	}

	/** Get ComprasESMAX.
		@return ComprasESMAX	  */
	public BigDecimal getComprasESMAX () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ComprasESMAX);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Date.
		@param Date1 
		Date when business is not conducted
	  */
	public void setDate1 (Timestamp Date1)
	{
		set_Value (COLUMNNAME_Date1, Date1);
	}

	/** Get Date.
		@return Date when business is not conducted
	  */
	public Timestamp getDate1 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date1);
	}

	/** Set I_ConsolidadoBase_ID.
		@param I_ConsolidadoBase_ID I_ConsolidadoBase_ID	  */
	public void setI_ConsolidadoBase_ID (int I_ConsolidadoBase_ID)
	{
		if (I_ConsolidadoBase_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_ConsolidadoBase_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_ConsolidadoBase_ID, Integer.valueOf(I_ConsolidadoBase_ID));
	}

	/** Get I_ConsolidadoBase_ID.
		@return I_ConsolidadoBase_ID	  */
	public int getI_ConsolidadoBase_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_ConsolidadoBase_ID);
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

	public I_M_Warehouse getM_Warehouse() throws RuntimeException
    {
		return (I_M_Warehouse)MTable.get(getCtx(), I_M_Warehouse.Table_Name)
			.getPO(getM_Warehouse_ID(), get_TrxName());	}

	/** Set Warehouse.
		@param M_Warehouse_ID 
		Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID)
	{
		if (M_Warehouse_ID < 1) 
			set_Value (COLUMNNAME_M_Warehouse_ID, null);
		else 
			set_Value (COLUMNNAME_M_Warehouse_ID, Integer.valueOf(M_Warehouse_ID));
	}

	/** Get Warehouse.
		@return Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Warehouse_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set OtrasEntradas.
		@param OtrasEntradas OtrasEntradas	  */
	public void setOtrasEntradas (BigDecimal OtrasEntradas)
	{
		set_Value (COLUMNNAME_OtrasEntradas, OtrasEntradas);
	}

	/** Get OtrasEntradas.
		@return OtrasEntradas	  */
	public BigDecimal getOtrasEntradas () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_OtrasEntradas);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set OtrasSalidas.
		@param OtrasSalidas OtrasSalidas	  */
	public void setOtrasSalidas (BigDecimal OtrasSalidas)
	{
		set_Value (COLUMNNAME_OtrasSalidas, OtrasSalidas);
	}

	/** Get OtrasSalidas.
		@return OtrasSalidas	  */
	public BigDecimal getOtrasSalidas () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_OtrasSalidas);
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

	/** Set Saldoantesdevariacion.
		@param Saldoantesdevariacion Saldoantesdevariacion	  */
	public void setSaldoantesdevariacion (BigDecimal Saldoantesdevariacion)
	{
		set_Value (COLUMNNAME_Saldoantesdevariacion, Saldoantesdevariacion);
	}

	/** Get Saldoantesdevariacion.
		@return Saldoantesdevariacion	  */
	public BigDecimal getSaldoantesdevariacion () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Saldoantesdevariacion);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SaldoInicial.
		@param SaldoInicial SaldoInicial	  */
	public void setSaldoInicial (BigDecimal SaldoInicial)
	{
		set_Value (COLUMNNAME_SaldoInicial, SaldoInicial);
	}

	/** Get SaldoInicial.
		@return SaldoInicial	  */
	public BigDecimal getSaldoInicial () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SaldoInicial);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set StockFisico.
		@param StockFisico StockFisico	  */
	public void setStockFisico (BigDecimal StockFisico)
	{
		set_Value (COLUMNNAME_StockFisico, StockFisico);
	}

	/** Get StockFisico.
		@return StockFisico	  */
	public BigDecimal getStockFisico () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_StockFisico);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Transferencias.
		@param Transferencias Transferencias	  */
	public void setTransferencias (BigDecimal Transferencias)
	{
		set_Value (COLUMNNAME_Transferencias, Transferencias);
	}

	/** Get Transferencias.
		@return Transferencias	  */
	public BigDecimal getTransferencias () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Transferencias);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set UsoPropio.
		@param UsoPropio UsoPropio	  */
	public void setUsoPropio (BigDecimal UsoPropio)
	{
		set_Value (COLUMNNAME_UsoPropio, UsoPropio);
	}

	/** Get UsoPropio.
		@return UsoPropio	  */
	public BigDecimal getUsoPropio () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_UsoPropio);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set UsoPropioPA.
		@param UsoPropioPA UsoPropioPA	  */
	public void setUsoPropioPA (BigDecimal UsoPropioPA)
	{
		set_Value (COLUMNNAME_UsoPropioPA, UsoPropioPA);
	}

	/** Get UsoPropioPA.
		@return UsoPropioPA	  */
	public BigDecimal getUsoPropioPA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_UsoPropioPA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Variacion.
		@param Variacion Variacion	  */
	public void setVariacion (BigDecimal Variacion)
	{
		set_Value (COLUMNNAME_Variacion, Variacion);
	}

	/** Get Variacion.
		@return Variacion	  */
	public BigDecimal getVariacion () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Variacion);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Ventas.
		@param Ventas Ventas	  */
	public void setVentas (BigDecimal Ventas)
	{
		set_Value (COLUMNNAME_Ventas, Ventas);
	}

	/** Get Ventas.
		@return Ventas	  */
	public BigDecimal getVentas () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Ventas);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}