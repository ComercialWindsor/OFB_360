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

/** Generated Model for T_BL_COMISIONES
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_COMISIONES extends PO implements I_T_BL_COMISIONES, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180525L;

    /** Standard Constructor */
    public X_T_BL_COMISIONES (Properties ctx, int T_BL_COMISIONES_ID, String trxName)
    {
      super (ctx, T_BL_COMISIONES_ID, trxName);
      /** if (T_BL_COMISIONES_ID == 0)
        {
			setT_BL_COMISIONES_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_COMISIONES (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_COMISIONES[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set COM_BLM_H.
		@param COM_BLM_H COM_BLM_H	  */
	public void setCOM_BLM_H (BigDecimal COM_BLM_H)
	{
		set_Value (COLUMNNAME_COM_BLM_H, COM_BLM_H);
	}

	/** Get COM_BLM_H.
		@return COM_BLM_H	  */
	public BigDecimal getCOM_BLM_H () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COM_BLM_H);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set COM_BLM_N.
		@param COM_BLM_N COM_BLM_N	  */
	public void setCOM_BLM_N (BigDecimal COM_BLM_N)
	{
		set_Value (COLUMNNAME_COM_BLM_N, COM_BLM_N);
	}

	/** Get COM_BLM_N.
		@return COM_BLM_N	  */
	public BigDecimal getCOM_BLM_N () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COM_BLM_N);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set COM_BLM_T.
		@param COM_BLM_T COM_BLM_T	  */
	public void setCOM_BLM_T (BigDecimal COM_BLM_T)
	{
		set_Value (COLUMNNAME_COM_BLM_T, COM_BLM_T);
	}

	/** Get COM_BLM_T.
		@return COM_BLM_T	  */
	public BigDecimal getCOM_BLM_T () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COM_BLM_T);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set COM_DESDE.
		@param COM_DESDE COM_DESDE	  */
	public void setCOM_DESDE (Timestamp COM_DESDE)
	{
		set_Value (COLUMNNAME_COM_DESDE, COM_DESDE);
	}

	/** Get COM_DESDE.
		@return COM_DESDE	  */
	public Timestamp getCOM_DESDE () 
	{
		return (Timestamp)get_Value(COLUMNNAME_COM_DESDE);
	}

	/** Set COM_HASTA.
		@param COM_HASTA COM_HASTA	  */
	public void setCOM_HASTA (Timestamp COM_HASTA)
	{
		set_Value (COLUMNNAME_COM_HASTA, COM_HASTA);
	}

	/** Get COM_HASTA.
		@return COM_HASTA	  */
	public Timestamp getCOM_HASTA () 
	{
		return (Timestamp)get_Value(COLUMNNAME_COM_HASTA);
	}

	/** Set COM_SLT_H.
		@param COM_SLT_H COM_SLT_H	  */
	public void setCOM_SLT_H (BigDecimal COM_SLT_H)
	{
		set_Value (COLUMNNAME_COM_SLT_H, COM_SLT_H);
	}

	/** Get COM_SLT_H.
		@return COM_SLT_H	  */
	public BigDecimal getCOM_SLT_H () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COM_SLT_H);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set COM_SLT_N.
		@param COM_SLT_N COM_SLT_N	  */
	public void setCOM_SLT_N (BigDecimal COM_SLT_N)
	{
		set_Value (COLUMNNAME_COM_SLT_N, COM_SLT_N);
	}

	/** Get COM_SLT_N.
		@return COM_SLT_N	  */
	public BigDecimal getCOM_SLT_N () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COM_SLT_N);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set COM_SLT_T.
		@param COM_SLT_T COM_SLT_T	  */
	public void setCOM_SLT_T (BigDecimal COM_SLT_T)
	{
		set_Value (COLUMNNAME_COM_SLT_T, COM_SLT_T);
	}

	/** Get COM_SLT_T.
		@return COM_SLT_T	  */
	public BigDecimal getCOM_SLT_T () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COM_SLT_T);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MGN_BLM_H.
		@param MGN_BLM_H MGN_BLM_H	  */
	public void setMGN_BLM_H (BigDecimal MGN_BLM_H)
	{
		set_Value (COLUMNNAME_MGN_BLM_H, MGN_BLM_H);
	}

	/** Get MGN_BLM_H.
		@return MGN_BLM_H	  */
	public BigDecimal getMGN_BLM_H () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MGN_BLM_H);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MGN_BLM_N.
		@param MGN_BLM_N MGN_BLM_N	  */
	public void setMGN_BLM_N (BigDecimal MGN_BLM_N)
	{
		set_Value (COLUMNNAME_MGN_BLM_N, MGN_BLM_N);
	}

	/** Get MGN_BLM_N.
		@return MGN_BLM_N	  */
	public BigDecimal getMGN_BLM_N () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MGN_BLM_N);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MGN_BLM_T.
		@param MGN_BLM_T MGN_BLM_T	  */
	public void setMGN_BLM_T (BigDecimal MGN_BLM_T)
	{
		set_Value (COLUMNNAME_MGN_BLM_T, MGN_BLM_T);
	}

	/** Get MGN_BLM_T.
		@return MGN_BLM_T	  */
	public BigDecimal getMGN_BLM_T () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MGN_BLM_T);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MGN_SLT_H.
		@param MGN_SLT_H MGN_SLT_H	  */
	public void setMGN_SLT_H (BigDecimal MGN_SLT_H)
	{
		set_Value (COLUMNNAME_MGN_SLT_H, MGN_SLT_H);
	}

	/** Get MGN_SLT_H.
		@return MGN_SLT_H	  */
	public BigDecimal getMGN_SLT_H () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MGN_SLT_H);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MGN_SLT_N.
		@param MGN_SLT_N MGN_SLT_N	  */
	public void setMGN_SLT_N (BigDecimal MGN_SLT_N)
	{
		set_Value (COLUMNNAME_MGN_SLT_N, MGN_SLT_N);
	}

	/** Get MGN_SLT_N.
		@return MGN_SLT_N	  */
	public BigDecimal getMGN_SLT_N () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MGN_SLT_N);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MGN_SLT_T.
		@param MGN_SLT_T MGN_SLT_T	  */
	public void setMGN_SLT_T (BigDecimal MGN_SLT_T)
	{
		set_Value (COLUMNNAME_MGN_SLT_T, MGN_SLT_T);
	}

	/** Get MGN_SLT_T.
		@return MGN_SLT_T	  */
	public BigDecimal getMGN_SLT_T () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MGN_SLT_T);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set NOMBRE_VENDEDOR.
		@param NOMBRE_VENDEDOR NOMBRE_VENDEDOR	  */
	public void setNOMBRE_VENDEDOR (String NOMBRE_VENDEDOR)
	{
		set_Value (COLUMNNAME_NOMBRE_VENDEDOR, NOMBRE_VENDEDOR);
	}

	/** Get NOMBRE_VENDEDOR.
		@return NOMBRE_VENDEDOR	  */
	public String getNOMBRE_VENDEDOR () 
	{
		return (String)get_Value(COLUMNNAME_NOMBRE_VENDEDOR);
	}

	/** Set PERIODO_COMISION.
		@param PERIODO_COMISION PERIODO_COMISION	  */
	public void setPERIODO_COMISION (String PERIODO_COMISION)
	{
		set_Value (COLUMNNAME_PERIODO_COMISION, PERIODO_COMISION);
	}

	/** Get PERIODO_COMISION.
		@return PERIODO_COMISION	  */
	public String getPERIODO_COMISION () 
	{
		return (String)get_Value(COLUMNNAME_PERIODO_COMISION);
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

	/** Set T_BL_COMISIONES.
		@param T_BL_COMISIONES_ID T_BL_COMISIONES	  */
	public void setT_BL_COMISIONES_ID (int T_BL_COMISIONES_ID)
	{
		if (T_BL_COMISIONES_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_COMISIONES_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_COMISIONES_ID, Integer.valueOf(T_BL_COMISIONES_ID));
	}

	/** Get T_BL_COMISIONES.
		@return T_BL_COMISIONES	  */
	public int getT_BL_COMISIONES_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_COMISIONES_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set TSA_BLM_H.
		@param TSA_BLM_H TSA_BLM_H	  */
	public void setTSA_BLM_H (BigDecimal TSA_BLM_H)
	{
		set_Value (COLUMNNAME_TSA_BLM_H, TSA_BLM_H);
	}

	/** Get TSA_BLM_H.
		@return TSA_BLM_H	  */
	public BigDecimal getTSA_BLM_H () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TSA_BLM_H);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TSA_BLM_N.
		@param TSA_BLM_N TSA_BLM_N	  */
	public void setTSA_BLM_N (BigDecimal TSA_BLM_N)
	{
		set_Value (COLUMNNAME_TSA_BLM_N, TSA_BLM_N);
	}

	/** Get TSA_BLM_N.
		@return TSA_BLM_N	  */
	public BigDecimal getTSA_BLM_N () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TSA_BLM_N);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TSA_BLM_T.
		@param TSA_BLM_T TSA_BLM_T	  */
	public void setTSA_BLM_T (BigDecimal TSA_BLM_T)
	{
		set_Value (COLUMNNAME_TSA_BLM_T, TSA_BLM_T);
	}

	/** Get TSA_BLM_T.
		@return TSA_BLM_T	  */
	public BigDecimal getTSA_BLM_T () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TSA_BLM_T);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TSA_SLT_H.
		@param TSA_SLT_H TSA_SLT_H	  */
	public void setTSA_SLT_H (BigDecimal TSA_SLT_H)
	{
		set_Value (COLUMNNAME_TSA_SLT_H, TSA_SLT_H);
	}

	/** Get TSA_SLT_H.
		@return TSA_SLT_H	  */
	public BigDecimal getTSA_SLT_H () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TSA_SLT_H);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TSA_SLT_N.
		@param TSA_SLT_N TSA_SLT_N	  */
	public void setTSA_SLT_N (BigDecimal TSA_SLT_N)
	{
		set_Value (COLUMNNAME_TSA_SLT_N, TSA_SLT_N);
	}

	/** Get TSA_SLT_N.
		@return TSA_SLT_N	  */
	public BigDecimal getTSA_SLT_N () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TSA_SLT_N);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TSA_SLT_T.
		@param TSA_SLT_T TSA_SLT_T	  */
	public void setTSA_SLT_T (BigDecimal TSA_SLT_T)
	{
		set_Value (COLUMNNAME_TSA_SLT_T, TSA_SLT_T);
	}

	/** Get TSA_SLT_T.
		@return TSA_SLT_T	  */
	public BigDecimal getTSA_SLT_T () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TSA_SLT_T);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VENDEDOR.
		@param VENDEDOR VENDEDOR	  */
	public void setVENDEDOR (String VENDEDOR)
	{
		set_Value (COLUMNNAME_VENDEDOR, VENDEDOR);
	}

	/** Get VENDEDOR.
		@return VENDEDOR	  */
	public String getVENDEDOR () 
	{
		return (String)get_Value(COLUMNNAME_VENDEDOR);
	}

	/** Set VTA_BLM_H.
		@param VTA_BLM_H VTA_BLM_H	  */
	public void setVTA_BLM_H (BigDecimal VTA_BLM_H)
	{
		set_Value (COLUMNNAME_VTA_BLM_H, VTA_BLM_H);
	}

	/** Get VTA_BLM_H.
		@return VTA_BLM_H	  */
	public BigDecimal getVTA_BLM_H () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VTA_BLM_H);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VTA_BLM_N.
		@param VTA_BLM_N VTA_BLM_N	  */
	public void setVTA_BLM_N (BigDecimal VTA_BLM_N)
	{
		set_Value (COLUMNNAME_VTA_BLM_N, VTA_BLM_N);
	}

	/** Get VTA_BLM_N.
		@return VTA_BLM_N	  */
	public BigDecimal getVTA_BLM_N () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VTA_BLM_N);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VTA_BLM_T.
		@param VTA_BLM_T VTA_BLM_T	  */
	public void setVTA_BLM_T (BigDecimal VTA_BLM_T)
	{
		set_Value (COLUMNNAME_VTA_BLM_T, VTA_BLM_T);
	}

	/** Get VTA_BLM_T.
		@return VTA_BLM_T	  */
	public BigDecimal getVTA_BLM_T () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VTA_BLM_T);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VTA_SLT_H.
		@param VTA_SLT_H VTA_SLT_H	  */
	public void setVTA_SLT_H (BigDecimal VTA_SLT_H)
	{
		set_Value (COLUMNNAME_VTA_SLT_H, VTA_SLT_H);
	}

	/** Get VTA_SLT_H.
		@return VTA_SLT_H	  */
	public BigDecimal getVTA_SLT_H () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VTA_SLT_H);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VTA_SLT_N.
		@param VTA_SLT_N VTA_SLT_N	  */
	public void setVTA_SLT_N (BigDecimal VTA_SLT_N)
	{
		set_Value (COLUMNNAME_VTA_SLT_N, VTA_SLT_N);
	}

	/** Get VTA_SLT_N.
		@return VTA_SLT_N	  */
	public BigDecimal getVTA_SLT_N () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VTA_SLT_N);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VTA_SLT_T.
		@param VTA_SLT_T VTA_SLT_T	  */
	public void setVTA_SLT_T (BigDecimal VTA_SLT_T)
	{
		set_Value (COLUMNNAME_VTA_SLT_T, VTA_SLT_T);
	}

	/** Get VTA_SLT_T.
		@return VTA_SLT_T	  */
	public BigDecimal getVTA_SLT_T () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VTA_SLT_T);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}