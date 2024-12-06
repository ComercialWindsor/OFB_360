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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for TP_DriversControl
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_TP_DriversControl extends PO implements I_TP_DriversControl, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160823L;

    /** Standard Constructor */
    public X_TP_DriversControl (Properties ctx, int TP_DriversControl_ID, String trxName)
    {
      super (ctx, TP_DriversControl_ID, trxName);
      /** if (TP_DriversControl_ID == 0)
        {
			setTP_DriversControl_ID (0);
        } */
    }

    /** Load Constructor */
    public X_TP_DriversControl (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_TP_DriversControl[")
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

	/** Set Boton1.
		@param Boton1 Boton1	  */
	public void setBoton1 (String Boton1)
	{
		set_Value (COLUMNNAME_Boton1, Boton1);
	}

	/** Get Boton1.
		@return Boton1	  */
	public String getBoton1 () 
	{
		return (String)get_Value(COLUMNNAME_Boton1);
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

	public I_C_DocType getC_DocType() throws RuntimeException
    {
		return (I_C_DocType)MTable.get(getCtx(), I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Location getC_Location() throws RuntimeException
    {
		return (I_C_Location)MTable.get(getCtx(), I_C_Location.Table_Name)
			.getPO(getC_Location_ID(), get_TrxName());	}

	/** Set Address.
		@param C_Location_ID 
		Location or Address
	  */
	public void setC_Location_ID (int C_Location_ID)
	{
		if (C_Location_ID < 1) 
			set_Value (COLUMNNAME_C_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_Location_ID, Integer.valueOf(C_Location_ID));
	}

	/** Get Address.
		@return Location or Address
	  */
	public int getC_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Period getC_Period() throws RuntimeException
    {
		return (I_C_Period)MTable.get(getCtx(), I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ProjectOFB getC_ProjectOFB() throws RuntimeException
    {
		return (I_C_ProjectOFB)MTable.get(getCtx(), I_C_ProjectOFB.Table_Name)
			.getPO(getC_ProjectOFB_ID(), get_TrxName());	}

	/** Set C_ProjectOFB_ID.
		@param C_ProjectOFB_ID C_ProjectOFB_ID	  */
	public void setC_ProjectOFB_ID (int C_ProjectOFB_ID)
	{
		if (C_ProjectOFB_ID < 1) 
			set_Value (COLUMNNAME_C_ProjectOFB_ID, null);
		else 
			set_Value (COLUMNNAME_C_ProjectOFB_ID, Integer.valueOf(C_ProjectOFB_ID));
	}

	/** Get C_ProjectOFB_ID.
		@return C_ProjectOFB_ID	  */
	public int getC_ProjectOFB_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectOFB_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** CategoryType AD_Reference_ID=1000090 */
	public static final int CATEGORYTYPE_AD_Reference_ID=1000090;
	/** No Autorizada / Justificada = 01 */
	public static final String CATEGORYTYPE_NoAutorizadaJustificada = "01";
	/** No Autorizada / No Justificada = 02 */
	public static final String CATEGORYTYPE_NoAutorizadaNoJustificada = "02";
	/** No Autorizada / Sin Respuesta = 03 */
	public static final String CATEGORYTYPE_NoAutorizadaSinRespuesta = "03";
	/** No Autorizada / No Justificada  = 04 */
	//public static final String CATEGORYTYPE_NoAutorizadaNoJustificada = "04";
	/** No Autorizada / Prohibida  = 05 */
	public static final String CATEGORYTYPE_NoAutorizadaProhibida = "05";
	/** Prohibida / Justificada  = 06 */
	public static final String CATEGORYTYPE_ProhibidaJustificada = "06";
	/** Prohibida / No Justificada  = 07 */
	public static final String CATEGORYTYPE_ProhibidaNoJustificada = "07";
	/** Prohibida / Sin Respuesta  = 08 */
	public static final String CATEGORYTYPE_ProhibidaSinRespuesta = "08";
	/** Prohibida/ No Justificada  = 09 */
	//public static final String CATEGORYTYPE_ProhibidaNoJustificada = "09";
	/** Set Category Type.
		@param CategoryType 
		Source of the Journal with this category
	  */
	public void setCategoryType (String CategoryType)
	{

		set_Value (COLUMNNAME_CategoryType, CategoryType);
	}

	/** Get Category Type.
		@return Source of the Journal with this category
	  */
	public String getCategoryType () 
	{
		return (String)get_Value(COLUMNNAME_CategoryType);
	}

	/** CategoryType2 AD_Reference_ID=1000089 */
	public static final int CATEGORYTYPE2_AD_Reference_ID=1000089;
	/** Exc.Vel.Flota = 01 */
	public static final String CATEGORYTYPE2_ExcVelFlota = "01";
	/** Exc.Vel.Geocerca = 02 */
	public static final String CATEGORYTYPE2_ExcVelGeocerca = "02";
	/** Exc.Vel.Nocturno = 03 */
	public static final String CATEGORYTYPE2_ExcVelNocturno = "03";
	/** Exc.Vel.Comuna = 04 */
	public static final String CATEGORYTYPE2_ExcVelComuna = "04";
	/** Set Category Type 2.
		@param CategoryType2 
		Source of the Journal with this category
	  */
	public void setCategoryType2 (String CategoryType2)
	{

		set_Value (COLUMNNAME_CategoryType2, CategoryType2);
	}

	/** Get Category Type 2.
		@return Source of the Journal with this category
	  */
	public String getCategoryType2 () 
	{
		return (String)get_Value(COLUMNNAME_CategoryType2);
	}

	/** Set Date.
		@param Date1 
		Date when business is not conducted
	  */
	public void setDate1 (Timestamp Date1)
	{
		set_Value (COLUMNNAME_Date1, Date1);
	}

	/** Get Date.
		@return Date when business is not conducted
	  */
	public Timestamp getDate1 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date1);
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

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Waiting Valorization = WV */
	public static final String DOCSTATUS_WaitingValorization = "WV";
	/** Aprobacion Jefe = AJ */
	public static final String DOCSTATUS_AprobacionJefe = "AJ";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set End Time.
		@param EndTime 
		End of the time span
	  */
	public void setEndTime (Timestamp EndTime)
	{
		set_Value (COLUMNNAME_EndTime, EndTime);
	}

	/** Get End Time.
		@return End of the time span
	  */
	public Timestamp getEndTime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndTime);
	}

	/** Set Interval.
		@param Interval Interval	  */
	public void setInterval (String Interval)
	{
		set_Value (COLUMNNAME_Interval, Interval);
	}

	/** Get Interval.
		@return Interval	  */
	public String getInterval () 
	{
		return (String)get_Value(COLUMNNAME_Interval);
	}

	/** Set IsCommittee.
		@param IsCommittee IsCommittee	  */
	public void setIsCommittee (boolean IsCommittee)
	{
		set_Value (COLUMNNAME_IsCommittee, Boolean.valueOf(IsCommittee));
	}

	/** Get IsCommittee.
		@return IsCommittee	  */
	public boolean isCommittee () 
	{
		Object oo = get_Value(COLUMNNAME_IsCommittee);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Latitude.
		@param Latitude Latitude	  */
	public void setLatitude (BigDecimal Latitude)
	{
		set_Value (COLUMNNAME_Latitude, Latitude);
	}

	/** Get Latitude.
		@return Latitude	  */
	public BigDecimal getLatitude () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Latitude);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Longitude.
		@param Longitude Longitude	  */
	public void setLongitude (BigDecimal Longitude)
	{
		set_Value (COLUMNNAME_Longitude, Longitude);
	}

	/** Get Longitude.
		@return Longitude	  */
	public BigDecimal getLongitude () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Longitude);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Mail Text.
		@param MailText 
		Text used for Mail message
	  */
	public void setMailText (String MailText)
	{
		set_Value (COLUMNNAME_MailText, MailText);
	}

	/** Get Mail Text.
		@return Text used for Mail message
	  */
	public String getMailText () 
	{
		return (String)get_Value(COLUMNNAME_MailText);
	}

	/** Set MaxSpeed.
		@param MaxSpeed MaxSpeed	  */
	public void setMaxSpeed (int MaxSpeed)
	{
		set_Value (COLUMNNAME_MaxSpeed, Integer.valueOf(MaxSpeed));
	}

	/** Get MaxSpeed.
		@return MaxSpeed	  */
	public int getMaxSpeed () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MaxSpeed);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Petition.
		@param Petition Petition	  */
	public void setPetition (boolean Petition)
	{
		set_Value (COLUMNNAME_Petition, Boolean.valueOf(Petition));
	}

	/** Get Petition.
		@return Petition	  */
	public boolean isPetition () 
	{
		Object oo = get_Value(COLUMNNAME_Petition);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Reason.
		@param Reason Reason	  */
	public void setReason (String Reason)
	{
		set_Value (COLUMNNAME_Reason, Reason);
	}

	/** Get Reason.
		@return Reason	  */
	public String getReason () 
	{
		return (String)get_Value(COLUMNNAME_Reason);
	}

	/** Set SpeedCategory.
		@param SpeedCategory SpeedCategory	  */
	public void setSpeedCategory (int SpeedCategory)
	{
		set_Value (COLUMNNAME_SpeedCategory, Integer.valueOf(SpeedCategory));
	}

	/** Get SpeedCategory.
		@return SpeedCategory	  */
	public int getSpeedCategory () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SpeedCategory);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Start Time.
		@param StartTime 
		Time started
	  */
	public void setStartTime (Timestamp StartTime)
	{
		set_Value (COLUMNNAME_StartTime, StartTime);
	}

	/** Get Start Time.
		@return Time started
	  */
	public Timestamp getStartTime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartTime);
	}

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

	public I_AD_User getSupervisor() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getSupervisor_ID(), get_TrxName());	}

	/** Set Supervisor.
		@param Supervisor_ID 
		Supervisor for this user/organization - used for escalation and approval
	  */
	public void setSupervisor_ID (int Supervisor_ID)
	{
		if (Supervisor_ID < 1) 
			set_Value (COLUMNNAME_Supervisor_ID, null);
		else 
			set_Value (COLUMNNAME_Supervisor_ID, Integer.valueOf(Supervisor_ID));
	}

	/** Get Supervisor.
		@return Supervisor for this user/organization - used for escalation and approval
	  */
	public int getSupervisor_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Supervisor_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TP_DriversControl.
		@param TP_DriversControl_ID TP_DriversControl	  */
	public void setTP_DriversControl_ID (int TP_DriversControl_ID)
	{
		if (TP_DriversControl_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_TP_DriversControl_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_TP_DriversControl_ID, Integer.valueOf(TP_DriversControl_ID));
	}

	/** Get TP_DriversControl.
		@return TP_DriversControl	  */
	public int getTP_DriversControl_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TP_DriversControl_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** TypeMatrix AD_Reference_ID=1000088 */
	public static final int TYPEMATRIX_AD_Reference_ID=1000088;
	/** Detencion = DE */
	public static final String TYPEMATRIX_Detencion = "DE";
	/** Velocidad = VE */
	public static final String TYPEMATRIX_Velocidad = "VE";
	/** Set TypeMatrix.
		@param TypeMatrix TypeMatrix	  */
	public void setTypeMatrix (String TypeMatrix)
	{

		set_Value (COLUMNNAME_TypeMatrix, TypeMatrix);
	}

	/** Get TypeMatrix.
		@return TypeMatrix	  */
	public String getTypeMatrix () 
	{
		return (String)get_Value(COLUMNNAME_TypeMatrix);
	}

	/** Set Wait Time.
		@param WaitTime 
		Time in minutes to wait (sleep)
	  */
	public void setWaitTime (Timestamp WaitTime)
	{
		set_Value (COLUMNNAME_WaitTime, WaitTime);
	}

	/** Get Wait Time.
		@return Time in minutes to wait (sleep)
	  */
	public Timestamp getWaitTime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_WaitTime);
	}
}