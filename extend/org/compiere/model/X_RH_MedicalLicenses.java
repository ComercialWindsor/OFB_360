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
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for MedicalLicenses
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_MedicalLicenses extends PO implements I_RH_MedicalLicenses, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120207L;

    /** Standard Constructor */
    public X_RH_MedicalLicenses (Properties ctx, int MedicalLicenses_ID, String trxName)
    {
      super (ctx, MedicalLicenses_ID, trxName);
      /** if (MedicalLicenses_ID == 0)
        {
			setAppealCompin (false);
			setAppealIsapre (false);
			setLicenseStatus (null);
			setMedicalLicenses_ID (0);
			setPaymentStatus (null);
        } */
    }

    /** Load Constructor */
    public X_RH_MedicalLicenses (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_MedicalLicenses[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AppealCompin.
		@param AppealCompin AppealCompin	  */
	public void setAppealCompin (boolean AppealCompin)
	{
		set_Value (COLUMNNAME_AppealCompin, Boolean.valueOf(AppealCompin));
	}

	/** Get AppealCompin.
		@return AppealCompin	  */
	public boolean isAppealCompin () 
	{
		Object oo = get_Value(COLUMNNAME_AppealCompin);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AppealIsapre.
		@param AppealIsapre AppealIsapre	  */
	public void setAppealIsapre (boolean AppealIsapre)
	{
		set_Value (COLUMNNAME_AppealIsapre, Boolean.valueOf(AppealIsapre));
	}

	/** Get AppealIsapre.
		@return AppealIsapre	  */
	public boolean isAppealIsapre () 
	{
		Object oo = get_Value(COLUMNNAME_AppealIsapre);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set CollectionIsapre.
		@param CollectionIsapre CollectionIsapre	  */
	public void setCollectionIsapre (BigDecimal CollectionIsapre)
	{
		set_Value (COLUMNNAME_CollectionIsapre, CollectionIsapre);
	}

	/** Get CollectionIsapre.
		@return CollectionIsapre	  */
	public BigDecimal getCollectionIsapre () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CollectionIsapre);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_Payment getC_Payment() throws RuntimeException
    {
		return (I_C_Payment)MTable.get(getCtx(), I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Days.
		@param Days Days	  */
	public void setDays (BigDecimal Days)
	{
		set_Value (COLUMNNAME_Days, Days);
	}

	/** Get Days.
		@return Days	  */
	public BigDecimal getDays () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Days);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DescriptionAppeal.
		@param DescriptionAppeal DescriptionAppeal	  */
	public void setDescriptionAppeal (String DescriptionAppeal)
	{
		set_Value (COLUMNNAME_DescriptionAppeal, DescriptionAppeal);
	}

	/** Get DescriptionAppeal.
		@return DescriptionAppeal	  */
	public String getDescriptionAppeal () 
	{
		return (String)get_Value(COLUMNNAME_DescriptionAppeal);
	}

	/** Set DescriptionAppealI.
		@param DescriptionAppealI DescriptionAppealI	  */
	public void setDescriptionAppealI (String DescriptionAppealI)
	{
		set_Value (COLUMNNAME_DescriptionAppealI, DescriptionAppealI);
	}

	/** Get DescriptionAppealI.
		@return DescriptionAppealI	  */
	public String getDescriptionAppealI () 
	{
		return (String)get_Value(COLUMNNAME_DescriptionAppealI);
	}

	/** Set Disease.
		@param Disease Disease	  */
	public void setDisease (String Disease)
	{
		set_Value (COLUMNNAME_Disease, Disease);
	}

	/** Get Disease.
		@return Disease	  */
	public String getDisease () 
	{
		return (String)get_Value(COLUMNNAME_Disease);
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

	/** Set Doctor.
		@param Doctor Doctor	  */
	public void setDoctor (String Doctor)
	{
		set_Value (COLUMNNAME_Doctor, Doctor);
	}

	/** Get Doctor.
		@return Doctor	  */
	public String getDoctor () 
	{
		return (String)get_Value(COLUMNNAME_Doctor);
	}

	/** Set LicensedAmount.
		@param LicensedAmount LicensedAmount	  */
	public void setLicensedAmount (BigDecimal LicensedAmount)
	{
		set_Value (COLUMNNAME_LicensedAmount, LicensedAmount);
	}

	/** Get LicensedAmount.
		@return LicensedAmount	  */
	public BigDecimal getLicensedAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LicensedAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** LicenseStatus AD_Reference_ID=1000040 */
	public static final int LICENSESTATUS_AD_Reference_ID=1000040;
	/** Aceptada = ACT */
	public static final String LICENSESTATUS_Aceptada = "ACT";
	/** Rechazada = RCZ */
	public static final String LICENSESTATUS_Rechazada = "RCZ";
	/** Reducida = RDC */
	public static final String LICENSESTATUS_Reducida = "RDC";
	/** Set LicenseStatus.
		@param LicenseStatus LicenseStatus	  */
	public void setLicenseStatus (String LicenseStatus)
	{

		set_Value (COLUMNNAME_LicenseStatus, LicenseStatus);
	}

	/** Get LicenseStatus.
		@return LicenseStatus	  */
	public String getLicenseStatus () 
	{
		return (String)get_Value(COLUMNNAME_LicenseStatus);
	}

	/** Set MedicalLicenses.
		@param MedicalLicenses_ID MedicalLicenses	  */
	public void setMedicalLicenses_ID (int MedicalLicenses_ID)
	{
		if (MedicalLicenses_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MedicalLicenses_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MedicalLicenses_ID, Integer.valueOf(MedicalLicenses_ID));
	}

	/** Get MedicalLicenses.
		@return MedicalLicenses	  */
	public int getMedicalLicenses_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MedicalLicenses_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** PaymentStatus AD_Reference_ID=1000039 */
	public static final int PAYMENTSTATUS_AD_Reference_ID=1000039;
	/** Solicitud Cobra a ISAPRE = SCI */
	public static final String PAYMENTSTATUS_SolicitudCobraAISAPRE = "SCI";
	/** Recepción Conforme = RPC */
	public static final String PAYMENTSTATUS_RecepcionConforme = "RPC";
	/** Set PaymentStatus.
		@param PaymentStatus PaymentStatus	  */
	public void setPaymentStatus (String PaymentStatus)
	{

		set_Value (COLUMNNAME_PaymentStatus, PaymentStatus);
	}

	/** Get PaymentStatus.
		@return PaymentStatus	  */
	public String getPaymentStatus () 
	{
		return (String)get_Value(COLUMNNAME_PaymentStatus);
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