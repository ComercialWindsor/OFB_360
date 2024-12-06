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

/** Generated Model for HRO_JobRequest
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_HRO_JobRequest extends PO implements I_HRO_JobRequest, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140930L;

    /** Standard Constructor */
    public X_HRO_JobRequest (Properties ctx, int HRO_JobRequest_ID, String trxName)
    {
      super (ctx, HRO_JobRequest_ID, trxName);
      /** if (HRO_JobRequest_ID == 0)
        {
			setdaterequest (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDocStatus (null);
// DR
			setHR_Job_ID (0);
			setHRO_JobRequest_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_HRO_JobRequest (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HRO_JobRequest[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Cause AD_Reference_ID=1000111 */
	public static final int CAUSE_AD_Reference_ID=1000111;
	/** Despido = DE */
	public static final String CAUSE_Despido = "DE";
	/** Reemplazo = RE */
	public static final String CAUSE_Reemplazo = "RE";
	/** Nuevos Proyectos = NP */
	public static final String CAUSE_NuevosProyectos = "NP";
	/** Aumento del Personal = AP */
	public static final String CAUSE_AumentoDelPersonal = "AP";
	/** Set Cause.
		@param Cause Cause	  */
	public void setCause (String Cause)
	{

		set_Value (COLUMNNAME_Cause, Cause);
	}

	/** Get Cause.
		@return Cause	  */
	public String getCause () 
	{
		return (String)get_Value(COLUMNNAME_Cause);
	}

	/** Set daterequest.
		@param daterequest daterequest	  */
	public void setdaterequest (Timestamp daterequest)
	{
		set_Value (COLUMNNAME_daterequest, daterequest);
	}

	/** Get daterequest.
		@return daterequest	  */
	public Timestamp getdaterequest () 
	{
		return (Timestamp)get_Value(COLUMNNAME_daterequest);
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

	/** DocStatus AD_Reference_ID=1000110 */
	public static final int DOCSTATUS_AD_Reference_ID=1000110;
	/** Esperando Confirmacion = WC */
	public static final String DOCSTATUS_EsperandoConfirmacion = "WC";
	/** Rechazada = RJ */
	public static final String DOCSTATUS_Rechazada = "RJ";
	/** Aprobada / en Proceso = IP */
	public static final String DOCSTATUS_AprobadaEnProceso = "IP";
	/** Finalizada = CL */
	public static final String DOCSTATUS_Finalizada = "CL";
	/** Borrador = DR */
	public static final String DOCSTATUS_Borrador = "DR";
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

	public org.eevolution.model.I_HR_Job getHR_Job() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Job)MTable.get(getCtx(), org.eevolution.model.I_HR_Job.Table_Name)
			.getPO(getHR_Job_ID(), get_TrxName());	}

	/** Set Payroll Job.
		@param HR_Job_ID Payroll Job	  */
	public void setHR_Job_ID (int HR_Job_ID)
	{
		if (HR_Job_ID < 1) 
			set_Value (COLUMNNAME_HR_Job_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Job_ID, Integer.valueOf(HR_Job_ID));
	}

	/** Get Payroll Job.
		@return Payroll Job	  */
	public int getHR_Job_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Job_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HRO_JobRequest.
		@param HRO_JobRequest_ID HRO_JobRequest	  */
	public void setHRO_JobRequest_ID (int HRO_JobRequest_ID)
	{
		if (HRO_JobRequest_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HRO_JobRequest_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HRO_JobRequest_ID, Integer.valueOf(HRO_JobRequest_ID));
	}

	/** Get HRO_JobRequest.
		@return HRO_JobRequest	  */
	public int getHRO_JobRequest_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRO_JobRequest_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Job Cant.
		@param JobCant Job Cant	  */
	public void setJobCant (int JobCant)
	{
		set_Value (COLUMNNAME_JobCant, Integer.valueOf(JobCant));
	}

	/** Get Job Cant.
		@return Job Cant	  */
	public int getJobCant () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_JobCant);
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
}