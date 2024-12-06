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

/** Generated Model for C_CampaignActivities
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_C_CampaignActivities extends PO implements I_C_CampaignActivities, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150107L;

    /** Standard Constructor */
    public X_C_CampaignActivities (Properties ctx, int C_CampaignActivities_ID, String trxName)
    {
      super (ctx, C_CampaignActivities_ID, trxName);
      /** if (C_CampaignActivities_ID == 0)
        {
			setC_CampaignActivities_ID (0);
			setC_CampaignFollow_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_CampaignActivities (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_CampaignActivities[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set C_CampaignActivities.
		@param C_CampaignActivities_ID C_CampaignActivities	  */
	public void setC_CampaignActivities_ID (int C_CampaignActivities_ID)
	{
		if (C_CampaignActivities_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_CampaignActivities_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_CampaignActivities_ID, Integer.valueOf(C_CampaignActivities_ID));
	}

	/** Get C_CampaignActivities.
		@return C_CampaignActivities	  */
	public int getC_CampaignActivities_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_CampaignActivities_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_CampaignFollow getC_CampaignFollow() throws RuntimeException
    {
		return (I_C_CampaignFollow)MTable.get(getCtx(), I_C_CampaignFollow.Table_Name)
			.getPO(getC_CampaignFollow_ID(), get_TrxName());	}

	/** Set C_CampaignFollow.
		@param C_CampaignFollow_ID C_CampaignFollow	  */
	public void setC_CampaignFollow_ID (int C_CampaignFollow_ID)
	{
		if (C_CampaignFollow_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_CampaignFollow_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_CampaignFollow_ID, Integer.valueOf(C_CampaignFollow_ID));
	}

	/** Get C_CampaignFollow.
		@return C_CampaignFollow	  */
	public int getC_CampaignFollow_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_CampaignFollow_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** ContactStatus AD_Reference_ID=1000110 */
	public static final int CONTACTSTATUS_AD_Reference_ID=1000110;
	/** Contactado = SC */
	public static final String CONTACTSTATUS_Contactado = "SC";
	/** No Contactado = NC */
	public static final String CONTACTSTATUS_NoContactado = "NC";
	/** Necesario Otro Contacto = OC */
	public static final String CONTACTSTATUS_NecesarioOtroContacto = "OC";
	/** Desconocido en Contacto = DC */
	public static final String CONTACTSTATUS_DesconocidoEnContacto = "DC";
	/** Set ContactStatus.
		@param ContactStatus ContactStatus	  */
	public void setContactStatus (String ContactStatus)
	{

		set_Value (COLUMNNAME_ContactStatus, ContactStatus);
	}

	/** Get ContactStatus.
		@return ContactStatus	  */
	public String getContactStatus () 
	{
		return (String)get_Value(COLUMNNAME_ContactStatus);
	}

	/** Set Contract Date.
		@param DateContract 
		The (planned) effective date of this document.
	  */
	public void setDateContract (Timestamp DateContract)
	{
		set_Value (COLUMNNAME_DateContract, DateContract);
	}

	/** Get Contract Date.
		@return The (planned) effective date of this document.
	  */
	public Timestamp getDateContract () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateContract);
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

	/** Set HourStart.
		@param HourStart HourStart	  */
	public void setHourStart (Timestamp HourStart)
	{
		set_Value (COLUMNNAME_HourStart, HourStart);
	}

	/** Get HourStart.
		@return HourStart	  */
	public Timestamp getHourStart () 
	{
		return (Timestamp)get_Value(COLUMNNAME_HourStart);
	}
}