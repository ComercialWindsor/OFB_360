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

/** Generated Model for MP_AssetPartner
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_MP_AssetPartner extends PO implements I_MP_AssetPartner, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160217L;

    /** Standard Constructor */
    public X_MP_AssetPartner (Properties ctx, int MP_AssetPartner_ID, String trxName)
    {
      super (ctx, MP_AssetPartner_ID, trxName);
      /** if (MP_AssetPartner_ID == 0)
        {
			setMP_AssetPartner_ID (0);
        } */
    }

    /** Load Constructor */
    public X_MP_AssetPartner (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_MP_AssetPartner[")
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
			set_ValueNoCheck (COLUMNNAME_A_Asset_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
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
			set_ValueNoCheck (COLUMNNAME_A_AssetRef_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_A_AssetRef_ID, Integer.valueOf(A_AssetRef_ID));
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

	/** Set Attribute.
		@param Attribute Attribute	  */
	public void setAttribute (String Attribute)
	{
		set_Value (COLUMNNAME_Attribute, Attribute);
	}

	/** Get Attribute.
		@return Attribute	  */
	public String getAttribute () 
	{
		return (String)get_Value(COLUMNNAME_Attribute);
	}

	/** Set Attribute2.
		@param Attribute2 Attribute2	  */
	public void setAttribute2 (String Attribute2)
	{
		set_Value (COLUMNNAME_Attribute2, Attribute2);
	}

	/** Get Attribute2.
		@return Attribute2	  */
	public String getAttribute2 () 
	{
		return (String)get_Value(COLUMNNAME_Attribute2);
	}

	/** Set Attribute3.
		@param Attribute3 Attribute3	  */
	public void setAttribute3 (String Attribute3)
	{
		set_Value (COLUMNNAME_Attribute3, Attribute3);
	}

	/** Get Attribute3.
		@return Attribute3	  */
	public String getAttribute3 () 
	{
		return (String)get_Value(COLUMNNAME_Attribute3);
	}

	/** Set Attribute4.
		@param Attribute4 Attribute4	  */
	public void setAttribute4 (String Attribute4)
	{
		set_Value (COLUMNNAME_Attribute4, Attribute4);
	}

	/** Get Attribute4.
		@return Attribute4	  */
	public String getAttribute4 () 
	{
		return (String)get_Value(COLUMNNAME_Attribute4);
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

	/** Set MP_AssetPartner.
		@param MP_AssetPartner_ID MP_AssetPartner	  */
	public void setMP_AssetPartner_ID (int MP_AssetPartner_ID)
	{
		if (MP_AssetPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MP_AssetPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MP_AssetPartner_ID, Integer.valueOf(MP_AssetPartner_ID));
	}

	/** Get MP_AssetPartner.
		@return MP_AssetPartner	  */
	public int getMP_AssetPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_AssetPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}