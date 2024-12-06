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

/** Generated Model for C_CampaignFollow
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_C_CampaignFollow extends PO implements I_C_CampaignFollow, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150107L;

    /** Standard Constructor */
    public X_C_CampaignFollow (Properties ctx, int C_CampaignFollow_ID, String trxName)
    {
      super (ctx, C_CampaignFollow_ID, trxName);
      /** if (C_CampaignFollow_ID == 0)
        {
			setC_BPartner_ID (0);
			setC_Campaign_ID (0);
			setC_CampaignFollow_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_CampaignFollow (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_CampaignFollow[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_User getAD_User() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set btnUpdated.
		@param btnUpdated btnUpdated	  */
	public void setbtnUpdated (String btnUpdated)
	{
		set_Value (COLUMNNAME_btnUpdated, btnUpdated);
	}

	/** Get btnUpdated.
		@return btnUpdated	  */
	public String getbtnUpdated () 
	{
		return (String)get_Value(COLUMNNAME_btnUpdated);
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
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
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

	public I_C_Campaign getC_Campaign() throws RuntimeException
    {
		return (I_C_Campaign)MTable.get(getCtx(), I_C_Campaign.Table_Name)
			.getPO(getC_Campaign_ID(), get_TrxName());	}

	/** Set Campaign.
		@param C_Campaign_ID 
		Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID)
	{
		if (C_Campaign_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Campaign_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Campaign_ID, Integer.valueOf(C_Campaign_ID));
	}

	/** Get Campaign.
		@return Marketing Campaign
	  */
	public int getC_Campaign_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Campaign_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

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

	/** FinalStatus AD_Reference_ID=1000113 */
	public static final int FINALSTATUS_AD_Reference_ID=1000113;
	/** Aceptado = A */
	public static final String FINALSTATUS_Aceptado = "A";
	/** Rechazado = R */
	public static final String FINALSTATUS_Rechazado = "R";
	/** En Negociacion = N */
	public static final String FINALSTATUS_EnNegociacion = "N";
	/** Set FinalStatus.
		@param FinalStatus FinalStatus	  */
	public void setFinalStatus (String FinalStatus)
	{

		set_Value (COLUMNNAME_FinalStatus, FinalStatus);
	}

	/** Get FinalStatus.
		@return FinalStatus	  */
	public String getFinalStatus () 
	{
		return (String)get_Value(COLUMNNAME_FinalStatus);
	}

	/** Status AD_Reference_ID=1000111 */
	public static final int STATUS_AD_Reference_ID=1000111;
	/** No Contactado = NC */
	public static final String STATUS_NoContactado = "NC";
	/** Contacto Fallido = CF */
	public static final String STATUS_ContactoFallido = "CF";
	/** Cliente No Interesado = NI */
	public static final String STATUS_ClienteNoInteresado = "NI";
	/** Cliente Interesado = IN */
	public static final String STATUS_ClienteInteresado = "IN";
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