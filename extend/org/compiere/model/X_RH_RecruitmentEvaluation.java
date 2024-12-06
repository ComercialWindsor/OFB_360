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

/** Generated Model for RH_RecruitmentEvaluation
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_RecruitmentEvaluation extends PO implements I_RH_RecruitmentEvaluation, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170327L;

    /** Standard Constructor */
    public X_RH_RecruitmentEvaluation (Properties ctx, int RH_RecruitmentEvaluation_ID, String trxName)
    {
      super (ctx, RH_RecruitmentEvaluation_ID, trxName);
      /** if (RH_RecruitmentEvaluation_ID == 0)
        {
			setProcessed (false);
			setRH_RecruitmentEvaluation_ID (0);
			setRH_RecruitmentPartner_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_RecruitmentEvaluation (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_RecruitmentEvaluation[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set RH_RecruitmentEvaluation.
		@param RH_RecruitmentEvaluation_ID RH_RecruitmentEvaluation	  */
	public void setRH_RecruitmentEvaluation_ID (int RH_RecruitmentEvaluation_ID)
	{
		if (RH_RecruitmentEvaluation_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentEvaluation_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentEvaluation_ID, Integer.valueOf(RH_RecruitmentEvaluation_ID));
	}

	/** Get RH_RecruitmentEvaluation.
		@return RH_RecruitmentEvaluation	  */
	public int getRH_RecruitmentEvaluation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_RecruitmentEvaluation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_RH_RecruitmentPartner getRH_RecruitmentPartner() throws RuntimeException
    {
		return (I_RH_RecruitmentPartner)MTable.get(getCtx(), I_RH_RecruitmentPartner.Table_Name)
			.getPO(getRH_RecruitmentPartner_ID(), get_TrxName());	}

	/** Set RH_RecruitmentPartner_ID.
		@param RH_RecruitmentPartner_ID RH_RecruitmentPartner_ID	  */
	public void setRH_RecruitmentPartner_ID (int RH_RecruitmentPartner_ID)
	{
		if (RH_RecruitmentPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentPartner_ID, Integer.valueOf(RH_RecruitmentPartner_ID));
	}

	/** Get RH_RecruitmentPartner_ID.
		@return RH_RecruitmentPartner_ID	  */
	public int getRH_RecruitmentPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_RecruitmentPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set rhbackground.
		@param rhbackground rhbackground	  */
	public void setrhbackground (String rhbackground)
	{
		set_Value (COLUMNNAME_rhbackground, rhbackground);
	}

	/** Get rhbackground.
		@return rhbackground	  */
	public String getrhbackground () 
	{
		return (String)get_Value(COLUMNNAME_rhbackground);
	}

	/** Set rhbackgrounddesc.
		@param rhbackgrounddesc rhbackgrounddesc	  */
	public void setrhbackgrounddesc (String rhbackgrounddesc)
	{
		set_Value (COLUMNNAME_rhbackgrounddesc, rhbackgrounddesc);
	}

	/** Get rhbackgrounddesc.
		@return rhbackgrounddesc	  */
	public String getrhbackgrounddesc () 
	{
		return (String)get_Value(COLUMNNAME_rhbackgrounddesc);
	}

	/** Set rhprescreening.
		@param rhprescreening rhprescreening	  */
	public void setrhprescreening (String rhprescreening)
	{
		set_Value (COLUMNNAME_rhprescreening, rhprescreening);
	}

	/** Get rhprescreening.
		@return rhprescreening	  */
	public String getrhprescreening () 
	{
		return (String)get_Value(COLUMNNAME_rhprescreening);
	}

	/** Set rhprescreeningdesc.
		@param rhprescreeningdesc rhprescreeningdesc	  */
	public void setrhprescreeningdesc (String rhprescreeningdesc)
	{
		set_Value (COLUMNNAME_rhprescreeningdesc, rhprescreeningdesc);
	}

	/** Get rhprescreeningdesc.
		@return rhprescreeningdesc	  */
	public String getrhprescreeningdesc () 
	{
		return (String)get_Value(COLUMNNAME_rhprescreeningdesc);
	}
}