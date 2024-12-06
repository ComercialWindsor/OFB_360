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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for I_BPartnerXML
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_I_BPartnerXML 
{

    /** TableName=I_BPartnerXML */
    public static final String Table_Name = "I_BPartnerXML";

    /** AD_Table_ID=1000101 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

	/** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID);

	/** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID();

	public I_AD_User getAD_User() throws RuntimeException;

    /** Column name Address1 */
    public static final String COLUMNNAME_Address1 = "Address1";

	/** Set Address 1.
	  * Address line 1 for this location
	  */
	public void setAddress1 (String Address1);

	/** Get Address 1.
	  * Address line 1 for this location
	  */
	public String getAddress1();

    /** Column name Address2 */
    public static final String COLUMNNAME_Address2 = "Address2";

	/** Set Address 2.
	  * Address line 2 for this location
	  */
	public void setAddress2 (String Address2);

	/** Get Address 2.
	  * Address line 2 for this location
	  */
	public String getAddress2();

    /** Column name Birthday */
    public static final String COLUMNNAME_Birthday = "Birthday";

	/** Set Birthday.
	  * Birthday or Anniversary day
	  */
	public void setBirthday (Timestamp Birthday);

	/** Get Birthday.
	  * Birthday or Anniversary day
	  */
	public Timestamp getBirthday();

    /** Column name BPContactGreeting */
    public static final String COLUMNNAME_BPContactGreeting = "BPContactGreeting";

	/** Set BP Contact Greeting.
	  * Greeting for Business Partner Contact
	  */
	public void setBPContactGreeting (String BPContactGreeting);

	/** Get BP Contact Greeting.
	  * Greeting for Business Partner Contact
	  */
	public String getBPContactGreeting();

    /** Column name C_BP_Group_ID */
    public static final String COLUMNNAME_C_BP_Group_ID = "C_BP_Group_ID";

	/** Set Business Partner Group.
	  * Business Partner Group
	  */
	public void setC_BP_Group_ID (int C_BP_Group_ID);

	/** Get Business Partner Group.
	  * Business Partner Group
	  */
	public int getC_BP_Group_ID();

	public I_C_BP_Group getC_BP_Group() throws RuntimeException;

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_BPartner_Location_ID */
    public static final String COLUMNNAME_C_BPartner_Location_ID = "C_BPartner_Location_ID";

	/** Set Partner Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID);

	/** Get Partner Location.
	  * Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID();

	public I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException;

    /** Column name C_Country_ID */
    public static final String COLUMNNAME_C_Country_ID = "C_Country_ID";

	/** Set Country.
	  * Country 
	  */
	public void setC_Country_ID (int C_Country_ID);

	/** Get Country.
	  * Country 
	  */
	public int getC_Country_ID();

	public I_C_Country getC_Country() throws RuntimeException;

    /** Column name C_Greeting_ID */
    public static final String COLUMNNAME_C_Greeting_ID = "C_Greeting_ID";

	/** Set Greeting.
	  * Greeting to print on correspondence
	  */
	public void setC_Greeting_ID (int C_Greeting_ID);

	/** Get Greeting.
	  * Greeting to print on correspondence
	  */
	public int getC_Greeting_ID();

	public I_C_Greeting getC_Greeting() throws RuntimeException;

    /** Column name C_Region_ID */
    public static final String COLUMNNAME_C_Region_ID = "C_Region_ID";

	/** Set Region.
	  * Identifies a geographical Region
	  */
	public void setC_Region_ID (int C_Region_ID);

	/** Get Region.
	  * Identifies a geographical Region
	  */
	public int getC_Region_ID();

	public I_C_Region getC_Region() throws RuntimeException;

    /** Column name City */
    public static final String COLUMNNAME_City = "City";

	/** Set City.
	  * Identifies a City
	  */
	public void setCity (String City);

	/** Get City.
	  * Identifies a City
	  */
	public String getCity();

    /** Column name Comments */
    public static final String COLUMNNAME_Comments = "Comments";

	/** Set Comments.
	  * Comments or additional information
	  */
	public void setComments (String Comments);

	/** Get Comments.
	  * Comments or additional information
	  */
	public String getComments();

    /** Column name ContactDescription */
    public static final String COLUMNNAME_ContactDescription = "ContactDescription";

	/** Set Contact Description.
	  * Description of Contact
	  */
	public void setContactDescription (String ContactDescription);

	/** Get Contact Description.
	  * Description of Contact
	  */
	public String getContactDescription();

    /** Column name ContactName */
    public static final String COLUMNNAME_ContactName = "ContactName";

	/** Set Contact Name.
	  * Business Partner Contact Name
	  */
	public void setContactName (String ContactName);

	/** Get Contact Name.
	  * Business Partner Contact Name
	  */
	public String getContactName();

    /** Column name CountryCode */
    public static final String COLUMNNAME_CountryCode = "CountryCode";

	/** Set ISO Country Code.
	  * Upper-case two-letter alphanumeric ISO Country code according to ISO 3166-1 - http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html
	  */
	public void setCountryCode (String CountryCode);

	/** Get ISO Country Code.
	  * Upper-case two-letter alphanumeric ISO Country code according to ISO 3166-1 - http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html
	  */
	public String getCountryCode();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name DUNS */
    public static final String COLUMNNAME_DUNS = "DUNS";

	/** Set D-U-N-S.
	  * Dun & Bradstreet Number
	  */
	public void setDUNS (String DUNS);

	/** Get D-U-N-S.
	  * Dun & Bradstreet Number
	  */
	public String getDUNS();

    /** Column name EMail */
    public static final String COLUMNNAME_EMail = "EMail";

	/** Set EMail Address.
	  * Electronic Mail Address
	  */
	public void setEMail (String EMail);

	/** Get EMail Address.
	  * Electronic Mail Address
	  */
	public String getEMail();

    /** Column name Fax */
    public static final String COLUMNNAME_Fax = "Fax";

	/** Set Fax.
	  * Facsimile number
	  */
	public void setFax (String Fax);

	/** Get Fax.
	  * Facsimile number
	  */
	public String getFax();

    /** Column name GroupValue */
    public static final String COLUMNNAME_GroupValue = "GroupValue";

	/** Set Group Key.
	  * Business Partner Group Key
	  */
	public void setGroupValue (String GroupValue);

	/** Get Group Key.
	  * Business Partner Group Key
	  */
	public String getGroupValue();

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String Help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

    /** Column name I_BPartnerXML_ID */
    public static final String COLUMNNAME_I_BPartnerXML_ID = "I_BPartnerXML_ID";

	/** Set I_BPartnerXML	  */
	public void setI_BPartnerXML_ID (int I_BPartnerXML_ID);

	/** Get I_BPartnerXML	  */
	public int getI_BPartnerXML_ID();

    /** Column name I_ErrorMsg */
    public static final String COLUMNNAME_I_ErrorMsg = "I_ErrorMsg";

	/** Set Import Error Message.
	  * Messages generated from import process
	  */
	public void setI_ErrorMsg (String I_ErrorMsg);

	/** Get Import Error Message.
	  * Messages generated from import process
	  */
	public String getI_ErrorMsg();

    /** Column name I_IsImported */
    public static final String COLUMNNAME_I_IsImported = "I_IsImported";

	/** Set Imported.
	  * Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported);

	/** Get Imported.
	  * Has this import been processed
	  */
	public boolean isI_IsImported();

    /** Column name IA_AreaFono */
    public static final String COLUMNNAME_IA_AreaFono = "IA_AreaFono";

	/** Set IA_AreaFono	  */
	public void setIA_AreaFono (String IA_AreaFono);

	/** Get IA_AreaFono	  */
	public String getIA_AreaFono();

    /** Column name IA_Celular */
    public static final String COLUMNNAME_IA_Celular = "IA_Celular";

	/** Set IA_Celular	  */
	public void setIA_Celular (String IA_Celular);

	/** Get IA_Celular	  */
	public String getIA_Celular();

    /** Column name IA_Ciudad */
    public static final String COLUMNNAME_IA_Ciudad = "IA_Ciudad";

	/** Set IA_Ciudad	  */
	public void setIA_Ciudad (String IA_Ciudad);

	/** Get IA_Ciudad	  */
	public String getIA_Ciudad();

    /** Column name IA_Comuna */
    public static final String COLUMNNAME_IA_Comuna = "IA_Comuna";

	/** Set IA_Comuna	  */
	public void setIA_Comuna (String IA_Comuna);

	/** Get IA_Comuna	  */
	public String getIA_Comuna();

    /** Column name IA_Corredora */
    public static final String COLUMNNAME_IA_Corredora = "IA_Corredora";

	/** Set IA_Corredora	  */
	public void setIA_Corredora (String IA_Corredora);

	/** Get IA_Corredora	  */
	public String getIA_Corredora();

    /** Column name IA_Direccion */
    public static final String COLUMNNAME_IA_Direccion = "IA_Direccion";

	/** Set IA_Direccion	  */
	public void setIA_Direccion (String IA_Direccion);

	/** Get IA_Direccion	  */
	public String getIA_Direccion();

    /** Column name IA_Email */
    public static final String COLUMNNAME_IA_Email = "IA_Email";

	/** Set IA_Email	  */
	public void setIA_Email (String IA_Email);

	/** Get IA_Email	  */
	public String getIA_Email();

    /** Column name IA_EstadoCivil */
    public static final String COLUMNNAME_IA_EstadoCivil = "IA_EstadoCivil";

	/** Set IA_EstadoCivil	  */
	public void setIA_EstadoCivil (String IA_EstadoCivil);

	/** Get IA_EstadoCivil	  */
	public String getIA_EstadoCivil();

    /** Column name IA_FechaNac */
    public static final String COLUMNNAME_IA_FechaNac = "IA_FechaNac";

	/** Set IA_FechaNac	  */
	public void setIA_FechaNac (String IA_FechaNac);

	/** Get IA_FechaNac	  */
	public String getIA_FechaNac();

    /** Column name IA_Financ */
    public static final String COLUMNNAME_IA_Financ = "IA_Financ";

	/** Set IA_Financ	  */
	public void setIA_Financ (String IA_Financ);

	/** Get IA_Financ	  */
	public String getIA_Financ();

    /** Column name IA_Genero */
    public static final String COLUMNNAME_IA_Genero = "IA_Genero";

	/** Set IA_Genero	  */
	public void setIA_Genero (String IA_Genero);

	/** Get IA_Genero	  */
	public String getIA_Genero();

    /** Column name IA_Inmueble */
    public static final String COLUMNNAME_IA_Inmueble = "IA_Inmueble";

	/** Set IA_Inmueble	  */
	public void setIA_Inmueble (String IA_Inmueble);

	/** Get IA_Inmueble	  */
	public String getIA_Inmueble();

    /** Column name IA_Materno */
    public static final String COLUMNNAME_IA_Materno = "IA_Materno";

	/** Set IA_Materno	  */
	public void setIA_Materno (String IA_Materno);

	/** Get IA_Materno	  */
	public String getIA_Materno();

    /** Column name IA_MontoCredito */
    public static final String COLUMNNAME_IA_MontoCredito = "IA_MontoCredito";

	/** Set IA_MontoCredito	  */
	public void setIA_MontoCredito (String IA_MontoCredito);

	/** Get IA_MontoCredito	  */
	public String getIA_MontoCredito();

    /** Column name IA_MotivoContacto */
    public static final String COLUMNNAME_IA_MotivoContacto = "IA_MotivoContacto";

	/** Set IA_MotivoContacto	  */
	public void setIA_MotivoContacto (String IA_MotivoContacto);

	/** Get IA_MotivoContacto	  */
	public String getIA_MotivoContacto();

    /** Column name IA_Nombre */
    public static final String COLUMNNAME_IA_Nombre = "IA_Nombre";

	/** Set IA_Nombre	  */
	public void setIA_Nombre (String IA_Nombre);

	/** Get IA_Nombre	  */
	public String getIA_Nombre();

    /** Column name IA_Observacion */
    public static final String COLUMNNAME_IA_Observacion = "IA_Observacion";

	/** Set IA_Observacion	  */
	public void setIA_Observacion (String IA_Observacion);

	/** Get IA_Observacion	  */
	public String getIA_Observacion();

    /** Column name IA_Origen */
    public static final String COLUMNNAME_IA_Origen = "IA_Origen";

	/** Set IA_Origen	  */
	public void setIA_Origen (String IA_Origen);

	/** Get IA_Origen	  */
	public String getIA_Origen();

    /** Column name IA_Paterno */
    public static final String COLUMNNAME_IA_Paterno = "IA_Paterno";

	/** Set IA_Paterno	  */
	public void setIA_Paterno (String IA_Paterno);

	/** Get IA_Paterno	  */
	public String getIA_Paterno();

    /** Column name IA_ProdInteres */
    public static final String COLUMNNAME_IA_ProdInteres = "IA_ProdInteres";

	/** Set IA_ProdInteres	  */
	public void setIA_ProdInteres (String IA_ProdInteres);

	/** Get IA_ProdInteres	  */
	public String getIA_ProdInteres();

    /** Column name IA_RazonSocial */
    public static final String COLUMNNAME_IA_RazonSocial = "IA_RazonSocial";

	/** Set IA_RazonSocial	  */
	public void setIA_RazonSocial (String IA_RazonSocial);

	/** Get IA_RazonSocial	  */
	public String getIA_RazonSocial();

    /** Column name IA_Region */
    public static final String COLUMNNAME_IA_Region = "IA_Region";

	/** Set IA_Region	  */
	public void setIA_Region (String IA_Region);

	/** Get IA_Region	  */
	public String getIA_Region();

    /** Column name IA_RutEmpresa */
    public static final String COLUMNNAME_IA_RutEmpresa = "IA_RutEmpresa";

	/** Set IA_RutEmpresa	  */
	public void setIA_RutEmpresa (String IA_RutEmpresa);

	/** Get IA_RutEmpresa	  */
	public String getIA_RutEmpresa();

    /** Column name IA_SolColab */
    public static final String COLUMNNAME_IA_SolColab = "IA_SolColab";

	/** Set IA_SolColab	  */
	public void setIA_SolColab (String IA_SolColab);

	/** Get IA_SolColab	  */
	public String getIA_SolColab();

    /** Column name IA_TelFijo */
    public static final String COLUMNNAME_IA_TelFijo = "IA_TelFijo";

	/** Set IA_TelFijo	  */
	public void setIA_TelFijo (String IA_TelFijo);

	/** Get IA_TelFijo	  */
	public String getIA_TelFijo();

    /** Column name IA_TipoEtiqueta */
    public static final String COLUMNNAME_IA_TipoEtiqueta = "IA_TipoEtiqueta";

	/** Set IA_TipoEtiqueta	  */
	public void setIA_TipoEtiqueta (String IA_TipoEtiqueta);

	/** Get IA_TipoEtiqueta	  */
	public String getIA_TipoEtiqueta();

    /** Column name IA_TipoPropiedad */
    public static final String COLUMNNAME_IA_TipoPropiedad = "IA_TipoPropiedad";

	/** Set IA_TipoPropiedad	  */
	public void setIA_TipoPropiedad (String IA_TipoPropiedad);

	/** Get IA_TipoPropiedad	  */
	public String getIA_TipoPropiedad();

    /** Column name IA_UrlOrigen */
    public static final String COLUMNNAME_IA_UrlOrigen = "IA_UrlOrigen";

	/** Set IA_UrlOrigen	  */
	public void setIA_UrlOrigen (String IA_UrlOrigen);

	/** Get IA_UrlOrigen	  */
	public String getIA_UrlOrigen();

    /** Column name IA_ValorEtiqueta */
    public static final String COLUMNNAME_IA_ValorEtiqueta = "IA_ValorEtiqueta";

	/** Set IA_ValorEtiqueta	  */
	public void setIA_ValorEtiqueta (String IA_ValorEtiqueta);

	/** Get IA_ValorEtiqueta	  */
	public String getIA_ValorEtiqueta();

    /** Column name IA_ValorPropiedad */
    public static final String COLUMNNAME_IA_ValorPropiedad = "IA_ValorPropiedad";

	/** Set IA_ValorPropiedad	  */
	public void setIA_ValorPropiedad (String IA_ValorPropiedad);

	/** Get IA_ValorPropiedad	  */
	public String getIA_ValorPropiedad();

    /** Column name InterestAreaName */
    public static final String COLUMNNAME_InterestAreaName = "InterestAreaName";

	/** Set Interest Area.
	  * Name of the Interest Area
	  */
	public void setInterestAreaName (String InterestAreaName);

	/** Get Interest Area.
	  * Name of the Interest Area
	  */
	public String getInterestAreaName();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsCustomer */
    public static final String COLUMNNAME_IsCustomer = "IsCustomer";

	/** Set Customer.
	  * Indicates if this Business Partner is a Customer
	  */
	public void setIsCustomer (boolean IsCustomer);

	/** Get Customer.
	  * Indicates if this Business Partner is a Customer
	  */
	public boolean isCustomer();

    /** Column name IsEmployee */
    public static final String COLUMNNAME_IsEmployee = "IsEmployee";

	/** Set Employee.
	  * Indicates if  this Business Partner is an employee
	  */
	public void setIsEmployee (boolean IsEmployee);

	/** Get Employee.
	  * Indicates if  this Business Partner is an employee
	  */
	public boolean isEmployee();

    /** Column name IsVendor */
    public static final String COLUMNNAME_IsVendor = "IsVendor";

	/** Set Vendor.
	  * Indicates if this Business Partner is a Vendor
	  */
	public void setIsVendor (boolean IsVendor);

	/** Get Vendor.
	  * Indicates if this Business Partner is a Vendor
	  */
	public boolean isVendor();

    /** Column name NAICS */
    public static final String COLUMNNAME_NAICS = "NAICS";

	/** Set NAICS/SIC.
	  * Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html
	  */
	public void setNAICS (String NAICS);

	/** Get NAICS/SIC.
	  * Standard Industry Code or its successor NAIC - http://www.osha.gov/oshstats/sicser.html
	  */
	public String getNAICS();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name Name2 */
    public static final String COLUMNNAME_Name2 = "Name2";

	/** Set Name 2.
	  * Additional Name
	  */
	public void setName2 (String Name2);

	/** Get Name 2.
	  * Additional Name
	  */
	public String getName2();

    /** Column name Password */
    public static final String COLUMNNAME_Password = "Password";

	/** Set Password.
	  * Password of any length (case sensitive)
	  */
	public void setPassword (String Password);

	/** Get Password.
	  * Password of any length (case sensitive)
	  */
	public String getPassword();

    /** Column name Phone */
    public static final String COLUMNNAME_Phone = "Phone";

	/** Set Phone.
	  * Identifies a telephone number
	  */
	public void setPhone (String Phone);

	/** Get Phone.
	  * Identifies a telephone number
	  */
	public String getPhone();

    /** Column name Phone2 */
    public static final String COLUMNNAME_Phone2 = "Phone2";

	/** Set 2nd Phone.
	  * Identifies an alternate telephone number.
	  */
	public void setPhone2 (String Phone2);

	/** Get 2nd Phone.
	  * Identifies an alternate telephone number.
	  */
	public String getPhone2();

    /** Column name Postal */
    public static final String COLUMNNAME_Postal = "Postal";

	/** Set ZIP.
	  * Postal code
	  */
	public void setPostal (String Postal);

	/** Get ZIP.
	  * Postal code
	  */
	public String getPostal();

    /** Column name Postal_Add */
    public static final String COLUMNNAME_Postal_Add = "Postal_Add";

	/** Set Additional Zip.
	  * Additional ZIP or Postal code
	  */
	public void setPostal_Add (String Postal_Add);

	/** Get Additional Zip.
	  * Additional ZIP or Postal code
	  */
	public String getPostal_Add();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name R_InterestArea_ID */
    public static final String COLUMNNAME_R_InterestArea_ID = "R_InterestArea_ID";

	/** Set Interest Area.
	  * Interest Area or Topic
	  */
	public void setR_InterestArea_ID (int R_InterestArea_ID);

	/** Get Interest Area.
	  * Interest Area or Topic
	  */
	public int getR_InterestArea_ID();

	public I_R_InterestArea getR_InterestArea() throws RuntimeException;

    /** Column name RegionName */
    public static final String COLUMNNAME_RegionName = "RegionName";

	/** Set Region.
	  * Name of the Region
	  */
	public void setRegionName (String RegionName);

	/** Get Region.
	  * Name of the Region
	  */
	public String getRegionName();

    /** Column name TaxID */
    public static final String COLUMNNAME_TaxID = "TaxID";

	/** Set Tax ID.
	  * Tax Identification
	  */
	public void setTaxID (String TaxID);

	/** Get Tax ID.
	  * Tax Identification
	  */
	public String getTaxID();

    /** Column name Title */
    public static final String COLUMNNAME_Title = "Title";

	/** Set Title.
	  * Name this entity is referred to as
	  */
	public void setTitle (String Title);

	/** Get Title.
	  * Name this entity is referred to as
	  */
	public String getTitle();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get UpdatedBy.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
