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

/** Generated Model for HRO_CallEntries
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_HRO_CallEntries extends PO implements I_HRO_CallEntries, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140930L;

    /** Standard Constructor */
    public X_HRO_CallEntries (Properties ctx, int HRO_CallEntries_ID, String trxName)
    {
      super (ctx, HRO_CallEntries_ID, trxName);
      /** if (HRO_CallEntries_ID == 0)
        {
			setDateFrom (new Timestamp( System.currentTimeMillis() ));
// @#Date@
			setDateTo (new Timestamp( System.currentTimeMillis() ));
			setDocStatus (null);
// DR
			setHR_Job_ID (0);
			setHRO_CallEntries_ID (0);
			setHRO_JobRequest_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_HRO_CallEntries (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HRO_CallEntries[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Date From.
		@param DateFrom 
		Starting date for a range
	  */
	public void setDateFrom (Timestamp DateFrom)
	{
		set_Value (COLUMNNAME_DateFrom, DateFrom);
	}

	/** Get Date From.
		@return Starting date for a range
	  */
	public Timestamp getDateFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateFrom);
	}

	/** Set Date To.
		@param DateTo 
		End date of a date range
	  */
	public void setDateTo (Timestamp DateTo)
	{
		set_Value (COLUMNNAME_DateTo, DateTo);
	}

	/** Get Date To.
		@return End date of a date range
	  */
	public Timestamp getDateTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTo);
	}

	/** DocStatus AD_Reference_ID=1000113 */
	public static final int DOCSTATUS_AD_Reference_ID=1000113;
	/** Borrador = DR */
	public static final String DOCSTATUS_Borrador = "DR";
	/** En Proceso = IP */
	public static final String DOCSTATUS_EnProceso = "IP";
	/** Finalizada = CL */
	public static final String DOCSTATUS_Finalizada = "CL";
	/** Cancelada = VO */
	public static final String DOCSTATUS_Cancelada = "VO";
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

	/** Set HRO_CallEntries.
		@param HRO_CallEntries_ID HRO_CallEntries	  */
	public void setHRO_CallEntries_ID (int HRO_CallEntries_ID)
	{
		if (HRO_CallEntries_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HRO_CallEntries_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HRO_CallEntries_ID, Integer.valueOf(HRO_CallEntries_ID));
	}

	/** Get HRO_CallEntries.
		@return HRO_CallEntries	  */
	public int getHRO_CallEntries_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRO_CallEntries_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_HRO_JobRequest getHRO_JobRequest() throws RuntimeException
    {
		return (I_HRO_JobRequest)MTable.get(getCtx(), I_HRO_JobRequest.Table_Name)
			.getPO(getHRO_JobRequest_ID(), get_TrxName());	}

	/** Set HRO_JobRequest.
		@param HRO_JobRequest_ID HRO_JobRequest	  */
	public void setHRO_JobRequest_ID (int HRO_JobRequest_ID)
	{
		if (HRO_JobRequest_ID < 1) 
			set_Value (COLUMNNAME_HRO_JobRequest_ID, null);
		else 
			set_Value (COLUMNNAME_HRO_JobRequest_ID, Integer.valueOf(HRO_JobRequest_ID));
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

	/** Set medios.
		@param medios medios	  */
	public void setmedios (String medios)
	{
		set_Value (COLUMNNAME_medios, medios);
	}

	/** Get medios.
		@return medios	  */
	public String getmedios () 
	{
		return (String)get_Value(COLUMNNAME_medios);
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

	/** Type AD_Reference_ID=1000112 */
	public static final int TYPE_AD_Reference_ID=1000112;
	/** Interno Cerrado = IC */
	public static final String TYPE_InternoCerrado = "IC";
	/** Interno Abierto = IA */
	public static final String TYPE_InternoAbierto = "IA";
	/** Externo = EX */
	public static final String TYPE_Externo = "EX";
	/** Set Type.
		@param Type 
		Type of Validation (SQL, Java Script, Java Language)
	  */
	public void setType (String Type)
	{

		set_Value (COLUMNNAME_Type, Type);
	}

	/** Get Type.
		@return Type of Validation (SQL, Java Script, Java Language)
	  */
	public String getType () 
	{
		return (String)get_Value(COLUMNNAME_Type);
	}
}