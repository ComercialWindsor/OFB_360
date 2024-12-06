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
import org.compiere.util.KeyNamePair;

/** Generated Model for RH_RequisitionRestructuring
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_RequisitionRestructuring extends PO implements I_RH_RequisitionRestructuring, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20171020L;

    /** Standard Constructor */
    public X_RH_RequisitionRestructuring (Properties ctx, int RH_RequisitionRestructuring_ID, String trxName)
    {
      super (ctx, RH_RequisitionRestructuring_ID, trxName);
      /** if (RH_RequisitionRestructuring_ID == 0)
        {
			setC_BPartner_ID (0);
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setProcessed (false);
			setRH_RequisitionRestructuring_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_RequisitionRestructuring (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_RequisitionRestructuring[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AD_Org_Ref_ID.
		@param AD_Org_Ref_ID AD_Org_Ref_ID	  */
	public void setAD_Org_Ref_ID (int AD_Org_Ref_ID)
	{
		if (AD_Org_Ref_ID < 1) 
			set_Value (COLUMNNAME_AD_Org_Ref_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Org_Ref_ID, Integer.valueOf(AD_Org_Ref_ID));
	}

	/** Get AD_Org_Ref_ID.
		@return AD_Org_Ref_ID	  */
	public int getAD_Org_Ref_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Org_Ref_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AD_Org_Ref2_ID.
		@param AD_Org_Ref2_ID AD_Org_Ref2_ID	  */
	public void setAD_Org_Ref2_ID (int AD_Org_Ref2_ID)
	{
		if (AD_Org_Ref2_ID < 1) 
			set_Value (COLUMNNAME_AD_Org_Ref2_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Org_Ref2_ID, Integer.valueOf(AD_Org_Ref2_ID));
	}

	/** Get AD_Org_Ref2_ID.
		@return AD_Org_Ref2_ID	  */
	public int getAD_Org_Ref2_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Org_Ref2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_C_BPartner getC_BPartner_() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID2(), get_TrxName());	}

	/** Set C_BPartner_ID2.
		@param C_BPartner_ID2 C_BPartner_ID2	  */
	public void setC_BPartner_ID2 (int C_BPartner_ID2)
	{
		set_Value (COLUMNNAME_C_BPartner_ID2, Integer.valueOf(C_BPartner_ID2));
	}

	/** Get C_BPartner_ID2.
		@return C_BPartner_ID2	  */
	public int getC_BPartner_ID2 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID2);
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

	public I_C_BPartner getCha() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getCharge(), get_TrxName());	}

	/** Set Charge.
		@param Charge Charge	  */
	public void setCharge (int Charge)
	{
		set_Value (COLUMNNAME_Charge, Integer.valueOf(Charge));
	}

	/** Get Charge.
		@return Charge	  */
	public int getCharge () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Charge);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ChargeProposed.
		@param ChargeProposed ChargeProposed	  */
	public void setChargeProposed (String ChargeProposed)
	{
		set_Value (COLUMNNAME_ChargeProposed, ChargeProposed);
	}

	/** Get ChargeProposed.
		@return ChargeProposed	  */
	public String getChargeProposed () 
	{
		return (String)get_Value(COLUMNNAME_ChargeProposed);
	}

	/** Set ChargeSQL.
		@param ChargeSQL ChargeSQL	  */
	public void setChargeSQL (String ChargeSQL)
	{
		throw new IllegalArgumentException ("ChargeSQL is virtual column");	}

	/** Get ChargeSQL.
		@return ChargeSQL	  */
	public String getChargeSQL () 
	{
		return (String)get_Value(COLUMNNAME_ChargeSQL);
	}

	/** Set DateCharge.
		@param DateCharge DateCharge	  */
	public void setDateCharge (Timestamp DateCharge)
	{
		set_Value (COLUMNNAME_DateCharge, DateCharge);
	}

	/** Get DateCharge.
		@return DateCharge	  */
	public Timestamp getDateCharge () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateCharge);
	}

	/** Set DateInduction.
		@param DateInduction 
		Transaction Date
	  */
	public void setDateInduction (Timestamp DateInduction)
	{
		set_Value (COLUMNNAME_DateInduction, DateInduction);
	}

	/** Get DateInduction.
		@return Transaction Date
	  */
	public Timestamp getDateInduction () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateInduction);
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

	/** Set IsNewCharge.
		@param IsNewCharge IsNewCharge	  */
	public void setIsNewCharge (boolean IsNewCharge)
	{
		set_Value (COLUMNNAME_IsNewCharge, Boolean.valueOf(IsNewCharge));
	}

	/** Get IsNewCharge.
		@return IsNewCharge	  */
	public boolean isNewCharge () 
	{
		Object oo = get_Value(COLUMNNAME_IsNewCharge);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsNewHDC.
		@param IsNewHDC IsNewHDC	  */
	public void setIsNewHDC (boolean IsNewHDC)
	{
		set_Value (COLUMNNAME_IsNewHDC, Boolean.valueOf(IsNewHDC));
	}

	/** Get IsNewHDC.
		@return IsNewHDC	  */
	public boolean isNewHDC () 
	{
		Object oo = get_Value(COLUMNNAME_IsNewHDC);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsNewOrg.
		@param IsNewOrg IsNewOrg	  */
	public void setIsNewOrg (boolean IsNewOrg)
	{
		set_Value (COLUMNNAME_IsNewOrg, Boolean.valueOf(IsNewOrg));
	}

	/** Get IsNewOrg.
		@return IsNewOrg	  */
	public boolean isNewOrg () 
	{
		Object oo = get_Value(COLUMNNAME_IsNewOrg);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsNewRent.
		@param IsNewRent IsNewRent	  */
	public void setIsNewRent (boolean IsNewRent)
	{
		set_Value (COLUMNNAME_IsNewRent, Boolean.valueOf(IsNewRent));
	}

	/** Get IsNewRent.
		@return IsNewRent	  */
	public boolean isNewRent () 
	{
		Object oo = get_Value(COLUMNNAME_IsNewRent);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Net_Amount.
		@param Net_Amount Net_Amount	  */
	public void setNet_Amount (BigDecimal Net_Amount)
	{
		set_Value (COLUMNNAME_Net_Amount, Net_Amount);
	}

	/** Get Net_Amount.
		@return Net_Amount	  */
	public BigDecimal getNet_Amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Net_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set OtherCharge.
		@param OtherCharge OtherCharge	  */
	public void setOtherCharge (boolean OtherCharge)
	{
		set_Value (COLUMNNAME_OtherCharge, Boolean.valueOf(OtherCharge));
	}

	/** Get OtherCharge.
		@return OtherCharge	  */
	public boolean isOtherCharge () 
	{
		Object oo = get_Value(COLUMNNAME_OtherCharge);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set PorcentRentSQL.
		@param PorcentRentSQL PorcentRentSQL	  */
	public void setPorcentRentSQL (BigDecimal PorcentRentSQL)
	{
		throw new IllegalArgumentException ("PorcentRentSQL is virtual column");	}

	/** Get PorcentRentSQL.
		@return PorcentRentSQL	  */
	public BigDecimal getPorcentRentSQL () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PorcentRentSQL);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Proposed_Amount.
		@param Proposed_Amount Proposed_Amount	  */
	public void setProposed_Amount (BigDecimal Proposed_Amount)
	{
		set_Value (COLUMNNAME_Proposed_Amount, Proposed_Amount);
	}

	/** Get Proposed_Amount.
		@return Proposed_Amount	  */
	public BigDecimal getProposed_Amount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Proposed_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set RH_RequisitionRestructuring_ID.
		@param RH_RequisitionRestructuring_ID RH_RequisitionRestructuring_ID	  */
	public void setRH_RequisitionRestructuring_ID (int RH_RequisitionRestructuring_ID)
	{
		if (RH_RequisitionRestructuring_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_RequisitionRestructuring_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_RequisitionRestructuring_ID, Integer.valueOf(RH_RequisitionRestructuring_ID));
	}

	/** Get RH_RequisitionRestructuring_ID.
		@return RH_RequisitionRestructuring_ID	  */
	public int getRH_RequisitionRestructuring_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_RequisitionRestructuring_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Signature2.
		@param Signature2 Signature2	  */
	public void setSignature2 (boolean Signature2)
	{
		set_Value (COLUMNNAME_Signature2, Boolean.valueOf(Signature2));
	}

	/** Get Signature2.
		@return Signature2	  */
	public boolean isSignature2 () 
	{
		Object oo = get_Value(COLUMNNAME_Signature2);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Signature3.
		@param Signature3 Signature3	  */
	public void setSignature3 (boolean Signature3)
	{
		set_Value (COLUMNNAME_Signature3, Boolean.valueOf(Signature3));
	}

	/** Get Signature3.
		@return Signature3	  */
	public boolean isSignature3 () 
	{
		Object oo = get_Value(COLUMNNAME_Signature3);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ValidRentSQL.
		@param ValidRentSQL ValidRentSQL	  */
	public void setValidRentSQL (String ValidRentSQL)
	{
		throw new IllegalArgumentException ("ValidRentSQL is virtual column");	}

	/** Get ValidRentSQL.
		@return ValidRentSQL	  */
	public String getValidRentSQL () 
	{
		return (String)get_Value(COLUMNNAME_ValidRentSQL);
	}
}