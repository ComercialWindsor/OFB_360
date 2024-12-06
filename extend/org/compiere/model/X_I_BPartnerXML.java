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
import org.compiere.util.KeyNamePair;

/** Generated Model for I_BPartnerXML
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_BPartnerXML extends PO implements I_I_BPartnerXML, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150204L;

    /** Standard Constructor */
    public X_I_BPartnerXML (Properties ctx, int I_BPartnerXML_ID, String trxName)
    {
      super (ctx, I_BPartnerXML_ID, trxName);
      /** if (I_BPartnerXML_ID == 0)
        {
			setI_BPartnerXML_ID (0);
			setI_IsImported (false);
        } */
    }

    /** Load Constructor */
    public X_I_BPartnerXML (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_BPartnerXML[")
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

	/** Set Address 1.
		@param Address1 
		Address line 1 for this location
	  */
	public void setAddress1 (String Address1)
	{
		set_Value (COLUMNNAME_Address1, Address1);
	}

	/** Get Address 1.
		@return Address line 1 for this location
	  */
	public String getAddress1 () 
	{
		return (String)get_Value(COLUMNNAME_Address1);
	}

	/** Set Address 2.
		@param Address2 
		Address line 2 for this location
	  */
	public void setAddress2 (String Address2)
	{
		set_Value (COLUMNNAME_Address2, Address2);
	}

	/** Get Address 2.
		@return Address line 2 for this location
	  */
	public String getAddress2 () 
	{
		return (String)get_Value(COLUMNNAME_Address2);
	}

	/** Set Birthday.
		@param Birthday 
		Birthday or Anniversary day
	  */
	public void setBirthday (Timestamp Birthday)
	{
		set_Value (COLUMNNAME_Birthday, Birthday);
	}

	/** Get Birthday.
		@return Birthday or Anniversary day
	  */
	public Timestamp getBirthday () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Birthday);
	}

	/** Set BP Contact Greeting.
		@param BPContactGreeting 
		Greeting for Business Partner Contact
	  */
	public void setBPContactGreeting (String BPContactGreeting)
	{
		set_Value (COLUMNNAME_BPContactGreeting, BPContactGreeting);
	}

	/** Get BP Contact Greeting.
		@return Greeting for Business Partner Contact
	  */
	public String getBPContactGreeting () 
	{
		return (String)get_Value(COLUMNNAME_BPContactGreeting);
	}

	public I_C_BP_Group getC_BP_Group() throws RuntimeException
    {
		return (I_C_BP_Group)MTable.get(getCtx(), I_C_BP_Group.Table_Name)
			.getPO(getC_BP_Group_ID(), get_TrxName());	}

	/** Set Business Partner Group.
		@param C_BP_Group_ID 
		Business Partner Group
	  */
	public void setC_BP_Group_ID (int C_BP_Group_ID)
	{
		if (C_BP_Group_ID < 1) 
			set_Value (COLUMNNAME_C_BP_Group_ID, null);
		else 
			set_Value (COLUMNNAME_C_BP_Group_ID, Integer.valueOf(C_BP_Group_ID));
	}

	/** Get Business Partner Group.
		@return Business Partner Group
	  */
	public int getC_BP_Group_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BP_Group_ID);
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

	public I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException
    {
		return (I_C_BPartner_Location)MTable.get(getCtx(), I_C_BPartner_Location.Table_Name)
			.getPO(getC_BPartner_Location_ID(), get_TrxName());	}

	/** Set Partner Location.
		@param C_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
	{
		if (C_BPartner_Location_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, Integer.valueOf(C_BPartner_Location_ID));
	}

	/** Get Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Country getC_Country() throws RuntimeException
    {
		return (I_C_Country)MTable.get(getCtx(), I_C_Country.Table_Name)
			.getPO(getC_Country_ID(), get_TrxName());	}

	/** Set Country.
		@param C_Country_ID 
		Country 
	  */
	public void setC_Country_ID (int C_Country_ID)
	{
		if (C_Country_ID < 1) 
			set_Value (COLUMNNAME_C_Country_ID, null);
		else 
			set_Value (COLUMNNAME_C_Country_ID, Integer.valueOf(C_Country_ID));
	}

	/** Get Country.
		@return Country 
	  */
	public int getC_Country_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Country_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Greeting getC_Greeting() throws RuntimeException
    {
		return (I_C_Greeting)MTable.get(getCtx(), I_C_Greeting.Table_Name)
			.getPO(getC_Greeting_ID(), get_TrxName());	}

	/** Set Greeting.
		@param C_Greeting_ID 
		Greeting to print on correspondence
	  */
	public void setC_Greeting_ID (int C_Greeting_ID)
	{
		if (C_Greeting_ID < 1) 
			set_Value (COLUMNNAME_C_Greeting_ID, null);
		else 
			set_Value (COLUMNNAME_C_Greeting_ID, Integer.valueOf(C_Greeting_ID));
	}

	/** Get Greeting.
		@return Greeting to print on correspondence
	  */
	public int getC_Greeting_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Greeting_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Region getC_Region() throws RuntimeException
    {
		return (I_C_Region)MTable.get(getCtx(), I_C_Region.Table_Name)
			.getPO(getC_Region_ID(), get_TrxName());	}

	/** Set Region.
		@param C_Region_ID 
		Identifies a geographical Region
	  */
	public void setC_Region_ID (int C_Region_ID)
	{
		if (C_Region_ID < 1) 
			set_Value (COLUMNNAME_C_Region_ID, null);
		else 
			set_Value (COLUMNNAME_C_Region_ID, Integer.valueOf(C_Region_ID));
	}

	/** Get Region.
		@return Identifies a geographical Region
	  */
	public int getC_Region_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Region_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set City.
		@param City 
		Identifies a City
	  */
	public void setCity (String City)
	{
		set_Value (COLUMNNAME_City, City);
	}

	/** Get City.
		@return Identifies a City
	  */
	public String getCity () 
	{
		return (String)get_Value(COLUMNNAME_City);
	}

	/** Set Comments.
		@param Comments 
		Comments or additional information
	  */
	public void setComments (String Comments)
	{
		set_Value (COLUMNNAME_Comments, Comments);
	}

	/** Get Comments.
		@return Comments or additional information
	  */
	public String getComments () 
	{
		return (String)get_Value(COLUMNNAME_Comments);
	}

	/** Set Contact Description.
		@param ContactDescription 
		Description of Contact
	  */
	public void setContactDescription (String ContactDescription)
	{
		set_Value (COLUMNNAME_ContactDescription, ContactDescription);
	}

	/** Get Contact Description.
		@return Description of Contact
	  */
	public String getContactDescription () 
	{
		return (String)get_Value(COLUMNNAME_ContactDescription);
	}

	/** Set Contact Name.
		@param ContactName 
		Business Partner Contact Name
	  */
	public void setContactName (String ContactName)
	{
		set_Value (COLUMNNAME_ContactName, ContactName);
	}

	/** Get Contact Name.
		@return Business Partner Contact Name
	  */
	public String getContactName () 
	{
		return (String)get_Value(COLUMNNAME_ContactName);
	}

	/** Set ISO Country Code.
		@param CountryCode 
		Upper-case two-letter alphanumeric ISO Country code according to ISO 3166-1 - http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html
	  */
	public void setCountryCode (String CountryCode)
	{
		set_Value (COLUMNNAME_CountryCode, CountryCode);
	}

	/** Get ISO Country Code.
		@return Upper-case two-letter alphanumeric ISO Country code according to ISO 3166-1 - http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html
	  */
	public String getCountryCode () 
	{
		return (String)get_Value(COLUMNNAME_CountryCode);
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

	/** Set D-U-N-S.
		@param DUNS 
		Dun & Bradstreet Number
	  */
	public void setDUNS (String DUNS)
	{
		set_Value (COLUMNNAME_DUNS, DUNS);
	}

	/** Get D-U-N-S.
		@return Dun & Bradstreet Number
	  */
	public String getDUNS () 
	{
		return (String)get_Value(COLUMNNAME_DUNS);
	}

	/** Set EMail Address.
		@param EMail 
		Electronic Mail Address
	  */
	public void setEMail (String EMail)
	{
		set_Value (COLUMNNAME_EMail, EMail);
	}

	/** Get EMail Address.
		@return Electronic Mail Address
	  */
	public String getEMail () 
	{
		return (String)get_Value(COLUMNNAME_EMail);
	}

	/** Set Fax.
		@param Fax 
		Facsimile number
	  */
	public void setFax (String Fax)
	{
		set_Value (COLUMNNAME_Fax, Fax);
	}

	/** Get Fax.
		@return Facsimile number
	  */
	public String getFax () 
	{
		return (String)get_Value(COLUMNNAME_Fax);
	}

	/** Set Group Key.
		@param GroupValue 
		Business Partner Group Key
	  */
	public void setGroupValue (String GroupValue)
	{
		set_Value (COLUMNNAME_GroupValue, GroupValue);
	}

	/** Get Group Key.
		@return Business Partner Group Key
	  */
	public String getGroupValue () 
	{
		return (String)get_Value(COLUMNNAME_GroupValue);
	}

	/** Set Comment/Help.
		@param Help 
		Comment or Hint
	  */
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp () 
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	/** Set I_BPartnerXML.
		@param I_BPartnerXML_ID I_BPartnerXML	  */
	public void setI_BPartnerXML_ID (int I_BPartnerXML_ID)
	{
		if (I_BPartnerXML_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_BPartnerXML_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_BPartnerXML_ID, Integer.valueOf(I_BPartnerXML_ID));
	}

	/** Get I_BPartnerXML.
		@return I_BPartnerXML	  */
	public int getI_BPartnerXML_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_BPartnerXML_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Import Error Message.
		@param I_ErrorMsg 
		Messages generated from import process
	  */
	public void setI_ErrorMsg (String I_ErrorMsg)
	{
		set_Value (COLUMNNAME_I_ErrorMsg, I_ErrorMsg);
	}

	/** Get Import Error Message.
		@return Messages generated from import process
	  */
	public String getI_ErrorMsg () 
	{
		return (String)get_Value(COLUMNNAME_I_ErrorMsg);
	}

	/** Set Imported.
		@param I_IsImported 
		Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported)
	{
		set_Value (COLUMNNAME_I_IsImported, Boolean.valueOf(I_IsImported));
	}

	/** Get Imported.
		@return Has this import been processed
	  */
	public boolean isI_IsImported () 
	{
		Object oo = get_Value(COLUMNNAME_I_IsImported);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IA_AreaFono.
		@param IA_AreaFono IA_AreaFono	  */
	public void setIA_AreaFono (String IA_AreaFono)
	{
		set_Value (COLUMNNAME_IA_AreaFono, IA_AreaFono);
	}

	/** Get IA_AreaFono.
		@return IA_AreaFono	  */
	public String getIA_AreaFono () 
	{
		return (String)get_Value(COLUMNNAME_IA_AreaFono);
	}

	/** Set IA_Celular.
		@param IA_Celular IA_Celular	  */
	public void setIA_Celular (String IA_Celular)
	{
		set_Value (COLUMNNAME_IA_Celular, IA_Celular);
	}

	/** Get IA_Celular.
		@return IA_Celular	  */
	public String getIA_Celular () 
	{
		return (String)get_Value(COLUMNNAME_IA_Celular);
	}

	/** Set IA_Ciudad.
		@param IA_Ciudad IA_Ciudad	  */
	public void setIA_Ciudad (String IA_Ciudad)
	{
		set_Value (COLUMNNAME_IA_Ciudad, IA_Ciudad);
	}

	/** Get IA_Ciudad.
		@return IA_Ciudad	  */
	public String getIA_Ciudad () 
	{
		return (String)get_Value(COLUMNNAME_IA_Ciudad);
	}

	/** Set IA_Comuna.
		@param IA_Comuna IA_Comuna	  */
	public void setIA_Comuna (String IA_Comuna)
	{
		set_Value (COLUMNNAME_IA_Comuna, IA_Comuna);
	}

	/** Get IA_Comuna.
		@return IA_Comuna	  */
	public String getIA_Comuna () 
	{
		return (String)get_Value(COLUMNNAME_IA_Comuna);
	}

	/** Set IA_Corredora.
		@param IA_Corredora IA_Corredora	  */
	public void setIA_Corredora (String IA_Corredora)
	{
		set_Value (COLUMNNAME_IA_Corredora, IA_Corredora);
	}

	/** Get IA_Corredora.
		@return IA_Corredora	  */
	public String getIA_Corredora () 
	{
		return (String)get_Value(COLUMNNAME_IA_Corredora);
	}

	/** Set IA_Direccion.
		@param IA_Direccion IA_Direccion	  */
	public void setIA_Direccion (String IA_Direccion)
	{
		set_Value (COLUMNNAME_IA_Direccion, IA_Direccion);
	}

	/** Get IA_Direccion.
		@return IA_Direccion	  */
	public String getIA_Direccion () 
	{
		return (String)get_Value(COLUMNNAME_IA_Direccion);
	}

	/** Set IA_Email.
		@param IA_Email IA_Email	  */
	public void setIA_Email (String IA_Email)
	{
		set_Value (COLUMNNAME_IA_Email, IA_Email);
	}

	/** Get IA_Email.
		@return IA_Email	  */
	public String getIA_Email () 
	{
		return (String)get_Value(COLUMNNAME_IA_Email);
	}

	/** Set IA_EstadoCivil.
		@param IA_EstadoCivil IA_EstadoCivil	  */
	public void setIA_EstadoCivil (String IA_EstadoCivil)
	{
		set_Value (COLUMNNAME_IA_EstadoCivil, IA_EstadoCivil);
	}

	/** Get IA_EstadoCivil.
		@return IA_EstadoCivil	  */
	public String getIA_EstadoCivil () 
	{
		return (String)get_Value(COLUMNNAME_IA_EstadoCivil);
	}

	/** Set IA_FechaNac.
		@param IA_FechaNac IA_FechaNac	  */
	public void setIA_FechaNac (String IA_FechaNac)
	{
		set_Value (COLUMNNAME_IA_FechaNac, IA_FechaNac);
	}

	/** Get IA_FechaNac.
		@return IA_FechaNac	  */
	public String getIA_FechaNac () 
	{
		return (String)get_Value(COLUMNNAME_IA_FechaNac);
	}

	/** Set IA_Financ.
		@param IA_Financ IA_Financ	  */
	public void setIA_Financ (String IA_Financ)
	{
		set_Value (COLUMNNAME_IA_Financ, IA_Financ);
	}

	/** Get IA_Financ.
		@return IA_Financ	  */
	public String getIA_Financ () 
	{
		return (String)get_Value(COLUMNNAME_IA_Financ);
	}

	/** Set IA_Genero.
		@param IA_Genero IA_Genero	  */
	public void setIA_Genero (String IA_Genero)
	{
		set_Value (COLUMNNAME_IA_Genero, IA_Genero);
	}

	/** Get IA_Genero.
		@return IA_Genero	  */
	public String getIA_Genero () 
	{
		return (String)get_Value(COLUMNNAME_IA_Genero);
	}

	/** Set IA_Inmueble.
		@param IA_Inmueble IA_Inmueble	  */
	public void setIA_Inmueble (String IA_Inmueble)
	{
		set_Value (COLUMNNAME_IA_Inmueble, IA_Inmueble);
	}

	/** Get IA_Inmueble.
		@return IA_Inmueble	  */
	public String getIA_Inmueble () 
	{
		return (String)get_Value(COLUMNNAME_IA_Inmueble);
	}

	/** Set IA_Materno.
		@param IA_Materno IA_Materno	  */
	public void setIA_Materno (String IA_Materno)
	{
		set_Value (COLUMNNAME_IA_Materno, IA_Materno);
	}

	/** Get IA_Materno.
		@return IA_Materno	  */
	public String getIA_Materno () 
	{
		return (String)get_Value(COLUMNNAME_IA_Materno);
	}

	/** Set IA_MontoCredito.
		@param IA_MontoCredito IA_MontoCredito	  */
	public void setIA_MontoCredito (String IA_MontoCredito)
	{
		set_Value (COLUMNNAME_IA_MontoCredito, IA_MontoCredito);
	}

	/** Get IA_MontoCredito.
		@return IA_MontoCredito	  */
	public String getIA_MontoCredito () 
	{
		return (String)get_Value(COLUMNNAME_IA_MontoCredito);
	}

	/** Set IA_MotivoContacto.
		@param IA_MotivoContacto IA_MotivoContacto	  */
	public void setIA_MotivoContacto (String IA_MotivoContacto)
	{
		set_Value (COLUMNNAME_IA_MotivoContacto, IA_MotivoContacto);
	}

	/** Get IA_MotivoContacto.
		@return IA_MotivoContacto	  */
	public String getIA_MotivoContacto () 
	{
		return (String)get_Value(COLUMNNAME_IA_MotivoContacto);
	}

	/** Set IA_Nombre.
		@param IA_Nombre IA_Nombre	  */
	public void setIA_Nombre (String IA_Nombre)
	{
		set_Value (COLUMNNAME_IA_Nombre, IA_Nombre);
	}

	/** Get IA_Nombre.
		@return IA_Nombre	  */
	public String getIA_Nombre () 
	{
		return (String)get_Value(COLUMNNAME_IA_Nombre);
	}

	/** Set IA_Observacion.
		@param IA_Observacion IA_Observacion	  */
	public void setIA_Observacion (String IA_Observacion)
	{
		set_Value (COLUMNNAME_IA_Observacion, IA_Observacion);
	}

	/** Get IA_Observacion.
		@return IA_Observacion	  */
	public String getIA_Observacion () 
	{
		return (String)get_Value(COLUMNNAME_IA_Observacion);
	}

	/** Set IA_Origen.
		@param IA_Origen IA_Origen	  */
	public void setIA_Origen (String IA_Origen)
	{
		set_Value (COLUMNNAME_IA_Origen, IA_Origen);
	}

	/** Get IA_Origen.
		@return IA_Origen	  */
	public String getIA_Origen () 
	{
		return (String)get_Value(COLUMNNAME_IA_Origen);
	}

	/** Set IA_Paterno.
		@param IA_Paterno IA_Paterno	  */
	public void setIA_Paterno (String IA_Paterno)
	{
		set_Value (COLUMNNAME_IA_Paterno, IA_Paterno);
	}

	/** Get IA_Paterno.
		@return IA_Paterno	  */
	public String getIA_Paterno () 
	{
		return (String)get_Value(COLUMNNAME_IA_Paterno);
	}

	/** Set IA_ProdInteres.
		@param IA_ProdInteres IA_ProdInteres	  */
	public void setIA_ProdInteres (String IA_ProdInteres)
	{
		set_Value (COLUMNNAME_IA_ProdInteres, IA_ProdInteres);
	}

	/** Get IA_ProdInteres.
		@return IA_ProdInteres	  */
	public String getIA_ProdInteres () 
	{
		return (String)get_Value(COLUMNNAME_IA_ProdInteres);
	}

	/** Set IA_RazonSocial.
		@param IA_RazonSocial IA_RazonSocial	  */
	public void setIA_RazonSocial (String IA_RazonSocial)
	{
		set_Value (COLUMNNAME_IA_RazonSocial, IA_RazonSocial);
	}

	/** Get IA_RazonSocial.
		@return IA_RazonSocial	  */
	public String getIA_RazonSocial () 
	{
		return (String)get_Value(COLUMNNAME_IA_RazonSocial);
	}

	/** Set IA_Region.
		@param IA_Region IA_Region	  */
	public void setIA_Region (String IA_Region)
	{
		set_Value (COLUMNNAME_IA_Region, IA_Region);
	}

	/** Get IA_Region.
		@return IA_Region	  */
	public String getIA_Region () 
	{
		return (String)get_Value(COLUMNNAME_IA_Region);
	}

	/** Set IA_RutEmpresa.
		@param IA_RutEmpresa IA_RutEmpresa	  */
	public void setIA_RutEmpresa (String IA_RutEmpresa)
	{
		set_Value (COLUMNNAME_IA_RutEmpresa, IA_RutEmpresa);
	}

	/** Get IA_RutEmpresa.
		@return IA_RutEmpresa	  */
	public String getIA_RutEmpresa () 
	{
		return (String)get_Value(COLUMNNAME_IA_RutEmpresa);
	}

	/** Set IA_SolColab.
		@param IA_SolColab IA_SolColab	  */
	public void setIA_SolColab (String IA_SolColab)
	{
		set_Value (COLUMNNAME_IA_SolColab, IA_SolColab);
	}

	/** Get IA_SolColab.
		@return IA_SolColab	  */
	public String getIA_SolColab () 
	{
		return (String)get_Value(COLUMNNAME_IA_SolColab);
	}

	/** Set IA_TelFijo.
		@param IA_TelFijo IA_TelFijo	  */
	public void setIA_TelFijo (String IA_TelFijo)
	{
		set_Value (COLUMNNAME_IA_TelFijo, IA_TelFijo);
	}

	/** Get IA_TelFijo.
		@return IA_TelFijo	  */
	public String getIA_TelFijo () 
	{
		return (String)get_Value(COLUMNNAME_IA_TelFijo);
	}

	/** Set IA_TipoEtiqueta.
		@param IA_TipoEtiqueta IA_TipoEtiqueta	  */
	public void setIA_TipoEtiqueta (String IA_TipoEtiqueta)
	{
		set_Value (COLUMNNAME_IA_TipoEtiqueta, IA_TipoEtiqueta);
	}

	/** Get IA_TipoEtiqueta.
		@return IA_TipoEtiqueta	  */
	public String getIA_TipoEtiqueta () 
	{
		return (String)get_Value(COLUMNNAME_IA_TipoEtiqueta);
	}

	/** Set IA_TipoPropiedad.
		@param IA_TipoPropiedad IA_TipoPropiedad	  */
	public void setIA_TipoPropiedad (String IA_TipoPropiedad)
	{
		set_Value (COLUMNNAME_IA_TipoPropiedad, IA_TipoPropiedad);
	}

	/** Get IA_TipoPropiedad.
		@return IA_TipoPropiedad	  */
	public String getIA_TipoPropiedad () 
	{
		return (String)get_Value(COLUMNNAME_IA_TipoPropiedad);
	}

	/** Set IA_UrlOrigen.
		@param IA_UrlOrigen IA_UrlOrigen	  */
	public void setIA_UrlOrigen (String IA_UrlOrigen)
	{
		set_Value (COLUMNNAME_IA_UrlOrigen, IA_UrlOrigen);
	}

	/** Get IA_UrlOrigen.
		@return IA_UrlOrigen	  */
	public String getIA_UrlOrigen () 
	{
		return (String)get_Value(COLUMNNAME_IA_UrlOrigen);
	}

	/** Set IA_ValorEtiqueta.
		@param IA_ValorEtiqueta IA_ValorEtiqueta	  */
	public void setIA_ValorEtiqueta (String IA_ValorEtiqueta)
	{
		set_Value (COLUMNNAME_IA_ValorEtiqueta, IA_ValorEtiqueta);
	}

	/** Get IA_ValorEtiqueta.
		@return IA_ValorEtiqueta	  */
	public String getIA_ValorEtiqueta () 
	{
		return (String)get_Value(COLUMNNAME_IA_ValorEtiqueta);
	}

	/** Set IA_ValorPropiedad.
		@param IA_ValorPropiedad IA_ValorPropiedad	  */
	public void setIA_ValorPropiedad (String IA_ValorPropiedad)
	{
		set_Value (COLUMNNAME_IA_ValorPropiedad, IA_ValorPropiedad);
	}

	/** Get IA_ValorPropiedad.
		@return IA_ValorPropiedad	  */
	public String getIA_ValorPropiedad () 
	{
		return (String)get_Value(COLUMNNAME_IA_ValorPropiedad);
	}

	/** Set Interest Area.
		@param InterestAreaName 
		Name of the Interest Area
	  */
	public void setInterestAreaName (String InterestAreaName)
	{
		set_Value (COLUMNNAME_InterestAreaName, InterestAreaName);
	}

	/** Get Interest Area.
		@return Name of the Interest Area
	  */
	public String getInterestAreaName () 
	{
		return (String)get_Value(COLUMNNAME_InterestAreaName);
	}

	/** Set Customer.
		@param IsCustomer 
		Indicates if this Business Partner is a Customer
	  */
	public void setIsCustomer (boolean IsCustomer)
	{
		set_Value (COLUMNNAME_IsCustomer, Boolean.valueOf(IsCustomer));
	}

	/** Get Customer.
		@return Indicates if this Business Partner is a Customer
	  */
	public boolean isCustomer () 
	{
		Object oo = get_Value(COLUMNNAME_IsCustomer);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Employee.
		@param IsEmployee 
		Indicates if  this Business Partner is an employee
	  */
	public void setIsEmployee (boolean IsEmployee)
	{
		set_Value (COLUMNNAME_IsEmployee, Boolean.valueOf(IsEmployee));
	}

	/** Get Employee.
		@return Indicates if  this Business Partner is an employee
	  */
	public boolean isEmployee () 
	{
		Object oo = get_Value(COLUMNNAME_IsEmployee);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Vendor.
		@param IsVendor 
		Indicates if this Business Partner is a Vendor
	  */
	public void setIsVendor (boolean IsVendor)
	{
		set_Value (COLUMNNAME_IsVendor, Boolean.valueOf(IsVendor));
	}

	/** Get Vendor.
		@return Indicates if this Business Partner is a Vendor
	  */
	public boolean isVendor () 
	{
		Object oo = get_Value(COLUMNNAME_IsVendor);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set NAICS/SIC.
		@param NAICS 
		Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html
	  */
	public void setNAICS (String NAICS)
	{
		set_Value (COLUMNNAME_NAICS, NAICS);
	}

	/** Get NAICS/SIC.
		@return Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html
	  */
	public String getNAICS () 
	{
		return (String)get_Value(COLUMNNAME_NAICS);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Name 2.
		@param Name2 
		Additional Name
	  */
	public void setName2 (String Name2)
	{
		set_Value (COLUMNNAME_Name2, Name2);
	}

	/** Get Name 2.
		@return Additional Name
	  */
	public String getName2 () 
	{
		return (String)get_Value(COLUMNNAME_Name2);
	}

	/** Set Password.
		@param Password 
		Password of any length (case sensitive)
	  */
	public void setPassword (String Password)
	{
		set_Value (COLUMNNAME_Password, Password);
	}

	/** Get Password.
		@return Password of any length (case sensitive)
	  */
	public String getPassword () 
	{
		return (String)get_Value(COLUMNNAME_Password);
	}

	/** Set Phone.
		@param Phone 
		Identifies a telephone number
	  */
	public void setPhone (String Phone)
	{
		set_Value (COLUMNNAME_Phone, Phone);
	}

	/** Get Phone.
		@return Identifies a telephone number
	  */
	public String getPhone () 
	{
		return (String)get_Value(COLUMNNAME_Phone);
	}

	/** Set 2nd Phone.
		@param Phone2 
		Identifies an alternate telephone number.
	  */
	public void setPhone2 (String Phone2)
	{
		set_Value (COLUMNNAME_Phone2, Phone2);
	}

	/** Get 2nd Phone.
		@return Identifies an alternate telephone number.
	  */
	public String getPhone2 () 
	{
		return (String)get_Value(COLUMNNAME_Phone2);
	}

	/** Set ZIP.
		@param Postal 
		Postal code
	  */
	public void setPostal (String Postal)
	{
		set_Value (COLUMNNAME_Postal, Postal);
	}

	/** Get ZIP.
		@return Postal code
	  */
	public String getPostal () 
	{
		return (String)get_Value(COLUMNNAME_Postal);
	}

	/** Set Additional Zip.
		@param Postal_Add 
		Additional ZIP or Postal code
	  */
	public void setPostal_Add (String Postal_Add)
	{
		set_Value (COLUMNNAME_Postal_Add, Postal_Add);
	}

	/** Get Additional Zip.
		@return Additional ZIP or Postal code
	  */
	public String getPostal_Add () 
	{
		return (String)get_Value(COLUMNNAME_Postal_Add);
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

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_R_InterestArea getR_InterestArea() throws RuntimeException
    {
		return (I_R_InterestArea)MTable.get(getCtx(), I_R_InterestArea.Table_Name)
			.getPO(getR_InterestArea_ID(), get_TrxName());	}

	/** Set Interest Area.
		@param R_InterestArea_ID 
		Interest Area or Topic
	  */
	public void setR_InterestArea_ID (int R_InterestArea_ID)
	{
		if (R_InterestArea_ID < 1) 
			set_Value (COLUMNNAME_R_InterestArea_ID, null);
		else 
			set_Value (COLUMNNAME_R_InterestArea_ID, Integer.valueOf(R_InterestArea_ID));
	}

	/** Get Interest Area.
		@return Interest Area or Topic
	  */
	public int getR_InterestArea_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_InterestArea_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Region.
		@param RegionName 
		Name of the Region
	  */
	public void setRegionName (String RegionName)
	{
		set_Value (COLUMNNAME_RegionName, RegionName);
	}

	/** Get Region.
		@return Name of the Region
	  */
	public String getRegionName () 
	{
		return (String)get_Value(COLUMNNAME_RegionName);
	}

	/** Set Tax ID.
		@param TaxID 
		Tax Identification
	  */
	public void setTaxID (String TaxID)
	{
		set_Value (COLUMNNAME_TaxID, TaxID);
	}

	/** Get Tax ID.
		@return Tax Identification
	  */
	public String getTaxID () 
	{
		return (String)get_Value(COLUMNNAME_TaxID);
	}

	/** Set Title.
		@param Title 
		Name this entity is referred to as
	  */
	public void setTitle (String Title)
	{
		set_Value (COLUMNNAME_Title, Title);
	}

	/** Get Title.
		@return Name this entity is referred to as
	  */
	public String getTitle () 
	{
		return (String)get_Value(COLUMNNAME_Title);
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