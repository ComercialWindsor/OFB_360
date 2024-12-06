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

/** Generated Model for HR_Prebitacora
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_HR_Prebitacora extends PO implements I_HR_Prebitacora, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160303L;

    /** Standard Constructor */
    public X_HR_Prebitacora (Properties ctx, int HR_Prebitacora_ID, String trxName)
    {
      super (ctx, HR_Prebitacora_ID, trxName);
      /** if (HR_Prebitacora_ID == 0)
        {
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
			setDocumentNo (null);
			setHR_Prebitacora_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_HR_Prebitacora (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_Prebitacora[")
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

	/** ColumnType AD_Reference_ID=1000073 */
	public static final int COLUMNTYPE_AD_Reference_ID=1000073;
	/** Activo = A */
	public static final String COLUMNTYPE_Activo = "A";
	/** Socio de Negocio = B */
	public static final String COLUMNTYPE_SocioDeNegocio = "B";
	/** Set Column Type.
		@param ColumnType Column Type	  */
	public void setColumnType (String ColumnType)
	{

		set_Value (COLUMNNAME_ColumnType, ColumnType);
	}

	/** Get Column Type.
		@return Column Type	  */
	public String getColumnType () 
	{
		return (String)get_Value(COLUMNNAME_ColumnType);
	}

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	/** Set days.
		@param days days	  */
	public void setdays (BigDecimal days)
	{
		set_Value (COLUMNNAME_days, days);
	}

	/** Get days.
		@return days	  */
	public BigDecimal getdays () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_days);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	public I_HR_Concept_TSM getHR_Concept_TSM() throws RuntimeException
    {
		return (I_HR_Concept_TSM)MTable.get(getCtx(), I_HR_Concept_TSM.Table_Name)
			.getPO(getHR_Concept_TSM_ID(), get_TrxName());	}

	/** Set HR_Concept_TSM_ID.
		@param HR_Concept_TSM_ID HR_Concept_TSM_ID	  */
	public void setHR_Concept_TSM_ID (int HR_Concept_TSM_ID)
	{
		if (HR_Concept_TSM_ID < 1) 
			set_Value (COLUMNNAME_HR_Concept_TSM_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Concept_TSM_ID, Integer.valueOf(HR_Concept_TSM_ID));
	}

	/** Get HR_Concept_TSM_ID.
		@return HR_Concept_TSM_ID	  */
	public int getHR_Concept_TSM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Concept_TSM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HR_Prebitacora.
		@param HR_Prebitacora_ID HR_Prebitacora	  */
	public void setHR_Prebitacora_ID (int HR_Prebitacora_ID)
	{
		if (HR_Prebitacora_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Prebitacora_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Prebitacora_ID, Integer.valueOf(HR_Prebitacora_ID));
	}

	/** Get HR_Prebitacora.
		@return HR_Prebitacora	  */
	public int getHR_Prebitacora_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Prebitacora_ID);
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

	/** Workshift AD_Reference_ID=1000053 */
	public static final int WORKSHIFT_AD_Reference_ID=1000053;
	/** 1 Turno = 30 */
	public static final String WORKSHIFT_1Turno = "30";
	/** 2 Turnos = 60 */
	public static final String WORKSHIFT_2Turnos = "60";
	/** 3 Turnos = 90 */
	public static final String WORKSHIFT_3Turnos = "90";
	/** Set Workshift.
		@param Workshift Workshift	  */
	public void setWorkshift (String Workshift)
	{

		set_Value (COLUMNNAME_Workshift, Workshift);
	}

	/** Get Workshift.
		@return Workshift	  */
	public String getWorkshift () 
	{
		return (String)get_Value(COLUMNNAME_Workshift);
	}
}