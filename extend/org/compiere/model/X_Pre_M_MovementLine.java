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

/** Generated Model for Pre_M_MovementLine
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_Pre_M_MovementLine extends PO implements I_Pre_M_MovementLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160318L;

    /** Standard Constructor */
    public X_Pre_M_MovementLine (Properties ctx, int Pre_M_MovementLine_ID, String trxName)
    {
      super (ctx, Pre_M_MovementLine_ID, trxName);
      /** if (Pre_M_MovementLine_ID == 0)
        {
			setA_Asset_ID (0);
			setA_AssetRef_ID (0);
			setC_BPartner_ID (0);
			setPre_M_Movement_ID (0);
			setPre_M_MovementLine_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_Pre_M_MovementLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_Pre_M_MovementLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_A_Asset getA_Asset() throws RuntimeException
    {
		return (I_A_Asset)MTable.get(getCtx(), I_A_Asset.Table_Name)
			.getPO(getA_Asset_ID(), get_TrxName());	}

	/** Set Asset.
		@param A_Asset_ID 
		Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID)
	{
		if (A_Asset_ID < 1) 
			set_Value (COLUMNNAME_A_Asset_ID, null);
		else 
			set_Value (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
	}

	/** Get Asset.
		@return Asset used internally or by customers
	  */
	public int getA_Asset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_A_Asset getA_AssetRef() throws RuntimeException
    {
		return (I_A_Asset)MTable.get(getCtx(), I_A_Asset.Table_Name)
			.getPO(getA_AssetRef_ID(), get_TrxName());	}

	/** Set A_AssetRef_ID.
		@param A_AssetRef_ID A_AssetRef_ID	  */
	public void setA_AssetRef_ID (int A_AssetRef_ID)
	{
		if (A_AssetRef_ID < 1) 
			set_Value (COLUMNNAME_A_AssetRef_ID, null);
		else 
			set_Value (COLUMNNAME_A_AssetRef_ID, Integer.valueOf(A_AssetRef_ID));
	}

	/** Get A_AssetRef_ID.
		@return A_AssetRef_ID	  */
	public int getA_AssetRef_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_AssetRef_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_Pre_M_Movement getPre_M_Movement() throws RuntimeException
    {
		return (I_Pre_M_Movement)MTable.get(getCtx(), I_Pre_M_Movement.Table_Name)
			.getPO(getPre_M_Movement_ID(), get_TrxName());	}

	/** Set Pre_M_Movement.
		@param Pre_M_Movement_ID Pre_M_Movement	  */
	public void setPre_M_Movement_ID (int Pre_M_Movement_ID)
	{
		if (Pre_M_Movement_ID < 1) 
			set_Value (COLUMNNAME_Pre_M_Movement_ID, null);
		else 
			set_Value (COLUMNNAME_Pre_M_Movement_ID, Integer.valueOf(Pre_M_Movement_ID));
	}

	/** Get Pre_M_Movement.
		@return Pre_M_Movement	  */
	public int getPre_M_Movement_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Pre_M_Movement_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Pre_M_MovementLine.
		@param Pre_M_MovementLine_ID Pre_M_MovementLine	  */
	public void setPre_M_MovementLine_ID (int Pre_M_MovementLine_ID)
	{
		if (Pre_M_MovementLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Pre_M_MovementLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Pre_M_MovementLine_ID, Integer.valueOf(Pre_M_MovementLine_ID));
	}

	/** Get Pre_M_MovementLine.
		@return Pre_M_MovementLine	  */
	public int getPre_M_MovementLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Pre_M_MovementLine_ID);
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
}