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

/** Generated Model for HR_Concept_TSM
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_HR_Concept_TSM extends PO implements I_HR_Concept_TSM, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170907L;

    /** Standard Constructor */
    public X_HR_Concept_TSM (Properties ctx, int HR_Concept_TSM_ID, String trxName)
    {
      super (ctx, HR_Concept_TSM_ID, trxName);
      /** if (HR_Concept_TSM_ID == 0)
        {
			setAcronym (null);
			setHR_Concept_TSM_ID (0);
			setName (null);
			setOwner (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_HR_Concept_TSM (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Concept_TSM[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Acronym.
		@param Acronym Acronym	  */
	public void setAcronym (String Acronym)
	{
		set_Value (COLUMNNAME_Acronym, Acronym);
	}

	/** Get Acronym.
		@return Acronym	  */
	public String getAcronym () 
	{
		return (String)get_Value(COLUMNNAME_Acronym);
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

	/** Set HR_Concept_TSM_ID.
		@param HR_Concept_TSM_ID HR_Concept_TSM_ID	  */
	public void setHR_Concept_TSM_ID (int HR_Concept_TSM_ID)
	{
		if (HR_Concept_TSM_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Concept_TSM_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Concept_TSM_ID, Integer.valueOf(HR_Concept_TSM_ID));
	}

	/** Get HR_Concept_TSM_ID.
		@return HR_Concept_TSM_ID	  */
	public int getHR_Concept_TSM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Concept_TSM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Owner AD_Reference_ID=1000155 */
	public static final int OWNER_AD_Reference_ID=1000155;
	/** Operaciones = Operaciones */
	public static final String OWNER_Operaciones = "Operaciones";
	/** Recursos Humanos = RR.HH */
	public static final String OWNER_RecursosHumanos = "RR.HH";
	/** Set Owner.
		@param Owner Owner	  */
	public void setOwner (String Owner)
	{

		set_Value (COLUMNNAME_Owner, Owner);
	}

	/** Get Owner.
		@return Owner	  */
	public String getOwner () 
	{
		return (String)get_Value(COLUMNNAME_Owner);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}