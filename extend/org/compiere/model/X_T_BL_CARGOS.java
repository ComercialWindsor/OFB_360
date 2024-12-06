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
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for T_BL_CARGOS
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_T_BL_CARGOS extends PO implements I_T_BL_CARGOS, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180405L;

    /** Standard Constructor */
    public X_T_BL_CARGOS (Properties ctx, int T_BL_CARGOS_ID, String trxName)
    {
      super (ctx, T_BL_CARGOS_ID, trxName);
      /** if (T_BL_CARGOS_ID == 0)
        {
			setT_BL_CARGOS_ID (0);
        } */
    }

    /** Load Constructor */
    public X_T_BL_CARGOS (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_T_BL_CARGOS[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set CODIGO.
		@param CODIGO CODIGO	  */
	public void setCODIGO (String CODIGO)
	{
		set_Value (COLUMNNAME_CODIGO, CODIGO);
	}

	/** Get CODIGO.
		@return CODIGO	  */
	public String getCODIGO () 
	{
		return (String)get_Value(COLUMNNAME_CODIGO);
	}

	/** Set ESGERENTE.
		@param ESGERENTE ESGERENTE	  */
	public void setESGERENTE (boolean ESGERENTE)
	{
		set_Value (COLUMNNAME_ESGERENTE, Boolean.valueOf(ESGERENTE));
	}

	/** Get ESGERENTE.
		@return ESGERENTE	  */
	public boolean isESGERENTE () 
	{
		Object oo = get_Value(COLUMNNAME_ESGERENTE);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set NOMBRECARGO.
		@param NOMBRECARGO NOMBRECARGO	  */
	public void setNOMBRECARGO (String NOMBRECARGO)
	{
		set_Value (COLUMNNAME_NOMBRECARGO, NOMBRECARGO);
	}

	/** Get NOMBRECARGO.
		@return NOMBRECARGO	  */
	public String getNOMBRECARGO () 
	{
		return (String)get_Value(COLUMNNAME_NOMBRECARGO);
	}

	/** Set RAIZ.
		@param RAIZ RAIZ	  */
	public void setRAIZ (boolean RAIZ)
	{
		set_Value (COLUMNNAME_RAIZ, Boolean.valueOf(RAIZ));
	}

	/** Get RAIZ.
		@return RAIZ	  */
	public boolean isRAIZ () 
	{
		Object oo = get_Value(COLUMNNAME_RAIZ);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_T_BL_CARGOS getSupervisor() throws RuntimeException
    {
		return (I_T_BL_CARGOS)MTable.get(getCtx(), I_T_BL_CARGOS.Table_Name)
			.getPO(getSupervisor_ID(), get_TrxName());	}

	/** Set Supervisor.
		@param Supervisor_ID 
		Supervisor for this user/organization - used for escalation and approval
	  */
	public void setSupervisor_ID (int Supervisor_ID)
	{
		if (Supervisor_ID < 1) 
			set_Value (COLUMNNAME_Supervisor_ID, null);
		else 
			set_Value (COLUMNNAME_Supervisor_ID, Integer.valueOf(Supervisor_ID));
	}

	/** Get Supervisor.
		@return Supervisor for this user/organization - used for escalation and approval
	  */
	public int getSupervisor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Supervisor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set T_BL_CARGOS.
		@param T_BL_CARGOS_ID T_BL_CARGOS	  */
	public void setT_BL_CARGOS_ID (int T_BL_CARGOS_ID)
	{
		if (T_BL_CARGOS_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_T_BL_CARGOS_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_T_BL_CARGOS_ID, Integer.valueOf(T_BL_CARGOS_ID));
	}

	/** Get T_BL_CARGOS.
		@return T_BL_CARGOS	  */
	public int getT_BL_CARGOS_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_T_BL_CARGOS_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}