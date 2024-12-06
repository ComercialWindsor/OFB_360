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
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for MP_OT_Task
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_MP_OT_Task extends PO implements I_MP_OT_Task, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20110102L;

    /** Standard Constructor */
    public X_MP_OT_Task (Properties ctx, int MP_OT_Task_ID, String trxName)
    {
      super (ctx, MP_OT_Task_ID, trxName);
      /** if (MP_OT_Task_ID == 0)
        {
			setC_UOM_ID (0);
			setMP_OT_ID (0);
			setMP_OT_Task_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_MP_OT_Task (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_MP_OT_Task[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Duration.
		@param Duration 
		Normal Duration in Duration Unit
	  */
	public void setDuration (BigDecimal Duration)
	{
		set_Value (COLUMNNAME_Duration, Duration);
	}

	/** Get Duration.
		@return Normal Duration in Duration Unit
	  */
	public BigDecimal getDuration () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Duration);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_MP_Maintain getMP_Maintain() throws RuntimeException
    {
		return (I_MP_Maintain)MTable.get(getCtx(), I_MP_Maintain.Table_Name)
			.getPO(getMP_Maintain_ID(), get_TrxName());	}

	/** Set MP_Maintain.
		@param MP_Maintain_ID MP_Maintain	  */
	public void setMP_Maintain_ID (int MP_Maintain_ID)
	{
		if (MP_Maintain_ID < 1) 
			set_Value (COLUMNNAME_MP_Maintain_ID, null);
		else 
			set_Value (COLUMNNAME_MP_Maintain_ID, Integer.valueOf(MP_Maintain_ID));
	}

	/** Get MP_Maintain.
		@return MP_Maintain	  */
	public int getMP_Maintain_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_Maintain_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_MP_OT getMP_OT() throws RuntimeException
    {
		return (I_MP_OT)MTable.get(getCtx(), I_MP_OT.Table_Name)
			.getPO(getMP_OT_ID(), get_TrxName());	}

	/** Set MP_OT.
		@param MP_OT_ID MP_OT	  */
	public void setMP_OT_ID (int MP_OT_ID)
	{
		if (MP_OT_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MP_OT_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MP_OT_ID, Integer.valueOf(MP_OT_ID));
	}

	/** Get MP_OT.
		@return MP_OT	  */
	public int getMP_OT_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_OT_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set MP_OT_Task.
		@param MP_OT_Task_ID MP_OT_Task	  */
	public void setMP_OT_Task_ID (int MP_OT_Task_ID)
	{
		if (MP_OT_Task_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MP_OT_Task_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MP_OT_Task_ID, Integer.valueOf(MP_OT_Task_ID));
	}

	/** Get MP_OT_Task.
		@return MP_OT_Task	  */
	public int getMP_OT_Task_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_OT_Task_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Status AD_Reference_ID=1000024 */
	public static final int STATUS_AD_Reference_ID=1000024;
	/** Not Started = NS */
	public static final String STATUS_NotStarted = "NS";
	/** In Progress = IP */
	public static final String STATUS_InProgress = "IP";
	/** Completed = CO */
	public static final String STATUS_Completed = "CO";
	/** Stop = ST */
	public static final String STATUS_Stop = "ST";
	/** Set Status.
		@param Status 
		Status of the currently running check
	  */
	public void setStatus (String Status)
	{

		set_Value (COLUMNNAME_Status, Status);
	}

	/** Get Status.
		@return Status of the currently running check
	  */
	public String getStatus () 
	{
		return (String)get_Value(COLUMNNAME_Status);
	}
}