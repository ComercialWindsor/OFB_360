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

/** Generated Model for AdministrativeRequests
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_AdministrativeRequests extends PO implements I_RH_AdministrativeRequests, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120207L;

    /** Standard Constructor */
    public X_RH_AdministrativeRequests (Properties ctx, int AdministrativeRequests_ID, String trxName)
    {
      super (ctx, AdministrativeRequests_ID, trxName);
      /** if (AdministrativeRequests_ID == 0)
        {
			setAdministrativeRequests_ID (0);
			setC_BPartner_ID (0);
			setdateendrequest (new Timestamp( System.currentTimeMillis() ));
			setdatestartrequest (new Timestamp( System.currentTimeMillis() ));
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setProcessed (false);
			setRequestType (null);
        } */
    }

    /** Load Constructor */
    public X_RH_AdministrativeRequests (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AdministrativeRequests[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AdministrativeRequests_ID.
		@param AdministrativeRequests_ID AdministrativeRequests_ID	  */
	public void setAdministrativeRequests_ID (int AdministrativeRequests_ID)
	{
		if (AdministrativeRequests_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AdministrativeRequests_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AdministrativeRequests_ID, Integer.valueOf(AdministrativeRequests_ID));
	}

	/** Get AdministrativeRequests_ID.
		@return AdministrativeRequests_ID	  */
	public int getAdministrativeRequests_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AdministrativeRequests_ID);
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

	/** Set dateendrequest.
		@param dateendrequest dateendrequest	  */
	public void setdateendrequest (Timestamp dateendrequest)
	{
		set_Value (COLUMNNAME_dateendrequest, dateendrequest);
	}

	/** Get dateendrequest.
		@return dateendrequest	  */
	public Timestamp getdateendrequest () 
	{
		return (Timestamp)get_Value(COLUMNNAME_dateendrequest);
	}

	/** Set datestartrequest.
		@param datestartrequest datestartrequest	  */
	public void setdatestartrequest (Timestamp datestartrequest)
	{
		set_Value (COLUMNNAME_datestartrequest, datestartrequest);
	}

	/** Get datestartrequest.
		@return datestartrequest	  */
	public Timestamp getdatestartrequest () 
	{
		return (Timestamp)get_Value(COLUMNNAME_datestartrequest);
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
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_ValueNoCheck (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
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

	/** RequestType AD_Reference_ID=1000031 */
	public static final int REQUESTTYPE_AD_Reference_ID=1000031;
	/** Cometido = CMT */
	public static final String REQUESTTYPE_Cometido = "CMT";
	/** Permisos Administrativos = PAD */
	public static final String REQUESTTYPE_PermisosAdministrativos = "PAD";
	/** Permisos Sin Goze de Sueldo = PSS */
	public static final String REQUESTTYPE_PermisosSinGozeDeSueldo = "PSS";
	/** Permisos Sindicales = PSD */
	public static final String REQUESTTYPE_PermisosSindicales = "PSD";
	/** Tiempo Compensado = TCP */
	public static final String REQUESTTYPE_TiempoCompensado = "TCP";
	/** Solicitud Horas Extra = SHE */
	public static final String REQUESTTYPE_SolicitudHorasExtra = "SHE";
	/** Solicitud Vacaciones = SVC */
	public static final String REQUESTTYPE_SolicitudVacaciones = "SVC";
	/** Permisos Paternales o Maternales = PPM */
	public static final String REQUESTTYPE_PermisosPaternalesOMaternales = "PPM";
	/** Permisos Especiales Honorarios = PEH */
	public static final String REQUESTTYPE_PermisosEspecialesHonorarios = "PEH";
	/** Set Request Type.
		@param RequestType Request Type	  */
	public void setRequestType (String RequestType)
	{

		set_ValueNoCheck (COLUMNNAME_RequestType, RequestType);
	}

	/** Get Request Type.
		@return Request Type	  */
	public String getRequestType () 
	{
		return (String)get_Value(COLUMNNAME_RequestType);
	}

	/** Set Signature1.
		@param Signature1 Signature1	  */
	public void setSignature1 (boolean Signature1)
	{
		set_Value (COLUMNNAME_Signature1, Boolean.valueOf(Signature1));
	}

	/** Get Signature1.
		@return Signature1	  */
	public boolean isSignature1 () 
	{
		Object oo = get_Value(COLUMNNAME_Signature1);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** TaskType AD_Reference_ID=1000033 */
	public static final int TASKTYPE_AD_Reference_ID=1000033;
	/** Viaje a Viña = VAV */
	public static final String TASKTYPE_ViajeAVion = "VAV";
	/** Viaje a Santiago = VAS */
	public static final String TASKTYPE_ViajeASantiago = "VAS";
	/** Set TaskType.
		@param TaskType TaskType	  */
	public void setTaskType (String TaskType)
	{

		set_Value (COLUMNNAME_TaskType, TaskType);
	}

	/** Get TaskType.
		@return TaskType	  */
	public String getTaskType () 
	{
		return (String)get_Value(COLUMNNAME_TaskType);
	}
}