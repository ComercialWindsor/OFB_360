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
import org.compiere.model.*;

/** Generated Model for MP_Prognosis
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_MP_Prognosis extends PO implements I_MP_Prognosis, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20110102L;

    /** Standard Constructor */
    public X_MP_Prognosis (Properties ctx, int MP_Prognosis_ID, String trxName)
    {
      super (ctx, MP_Prognosis_ID, trxName);
      /** if (MP_Prognosis_ID == 0)
        {
			setAD_PInstance_ID (0);
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
			setMP_Prognosis_ID (0);
			setProgrammingType (null);
        } */
    }

    /** Load Constructor */
    public X_MP_Prognosis (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_MP_Prognosis[")
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

	public I_AD_PInstance getAD_PInstance() throws RuntimeException
    {
		return (I_AD_PInstance)MTable.get(getCtx(), I_AD_PInstance.Table_Name)
			.getPO(getAD_PInstance_ID(), get_TrxName());	}

	/** Set Process Instance.
		@param AD_PInstance_ID 
		Instance of the process
	  */
	public void setAD_PInstance_ID (int AD_PInstance_ID)
	{
		if (AD_PInstance_ID < 1) 
			set_Value (COLUMNNAME_AD_PInstance_ID, null);
		else 
			set_Value (COLUMNNAME_AD_PInstance_ID, Integer.valueOf(AD_PInstance_ID));
	}

	/** Get Process Instance.
		@return Instance of the process
	  */
	public int getAD_PInstance_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PInstance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Ciclo.
		@param Ciclo Ciclo	  */
	public void setCiclo (int Ciclo)
	{
		set_Value (COLUMNNAME_Ciclo, Integer.valueOf(Ciclo));
	}

	/** Get Ciclo.
		@return Ciclo	  */
	public int getCiclo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Ciclo);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set MP_Prognosis.
		@param MP_Prognosis_ID MP_Prognosis	  */
	public void setMP_Prognosis_ID (int MP_Prognosis_ID)
	{
		if (MP_Prognosis_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MP_Prognosis_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MP_Prognosis_ID, Integer.valueOf(MP_Prognosis_ID));
	}

	/** Get MP_Prognosis.
		@return MP_Prognosis	  */
	public int getMP_Prognosis_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_Prognosis_ID);
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

	/** ProgrammingType AD_Reference_ID=1000015 */
	public static final int PROGRAMMINGTYPE_AD_Reference_ID=1000015;
	/** Calendar = C */
	public static final String PROGRAMMINGTYPE_Calendar = "C";
	/** Meter = M */
	public static final String PROGRAMMINGTYPE_Meter = "M";
	/** Set ProgrammingType.
		@param ProgrammingType ProgrammingType	  */
	public void setProgrammingType (String ProgrammingType)
	{

		set_Value (COLUMNNAME_ProgrammingType, ProgrammingType);
	}

	/** Get ProgrammingType.
		@return ProgrammingType	  */
	public String getProgrammingType () 
	{
		return (String)get_Value(COLUMNNAME_ProgrammingType);
	}

	/** Set Selected.
		@param Selected Selected	  */
	public void setSelected (boolean Selected)
	{
		set_Value (COLUMNNAME_Selected, Boolean.valueOf(Selected));
	}

	/** Get Selected.
		@return Selected	  */
	public boolean isSelected () 
	{
		Object oo = get_Value(COLUMNNAME_Selected);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}