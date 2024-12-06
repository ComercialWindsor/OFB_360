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

/** Generated Model for OFB_Ssomac
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_OFB_Ssomac extends PO implements I_OFB_Ssomac, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160823L;

    /** Standard Constructor */
    public X_OFB_Ssomac (Properties ctx, int OFB_Ssomac_ID, String trxName)
    {
      super (ctx, OFB_Ssomac_ID, trxName);
      /** if (OFB_Ssomac_ID == 0)
        {
			setOFB_Ssomac_ID (0);
        } */
    }

    /** Load Constructor */
    public X_OFB_Ssomac (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_OFB_Ssomac[")
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

	public I_A_Asset getA_Asset2() throws RuntimeException
    {
		return (I_A_Asset)MTable.get(getCtx(), I_A_Asset.Table_Name)
			.getPO(getA_Asset2_ID(), get_TrxName());	}

	/** Set A_Asset2_ID.
		@param A_Asset2_ID A_Asset2_ID	  */
	public void setA_Asset2_ID (int A_Asset2_ID)
	{
		if (A_Asset2_ID < 1) 
			set_Value (COLUMNNAME_A_Asset2_ID, null);
		else 
			set_Value (COLUMNNAME_A_Asset2_ID, Integer.valueOf(A_Asset2_ID));
	}

	/** Get A_Asset2_ID.
		@return A_Asset2_ID	  */
	public int getA_Asset2_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_AD_User getAD_User2() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getAD_User2_ID(), get_TrxName());	}

	/** Set AD_User2_ID.
		@param AD_User2_ID AD_User2_ID	  */
	public void setAD_User2_ID (int AD_User2_ID)
	{
		if (AD_User2_ID < 1) 
			set_Value (COLUMNNAME_AD_User2_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User2_ID, Integer.valueOf(AD_User2_ID));
	}

	/** Get AD_User2_ID.
		@return AD_User2_ID	  */
	public int getAD_User2_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Admonition.
		@param Admonition Admonition	  */
	public void setAdmonition (boolean Admonition)
	{
		set_Value (COLUMNNAME_Admonition, Boolean.valueOf(Admonition));
	}

	/** Get Admonition.
		@return Admonition	  */
	public boolean isAdmonition () 
	{
		Object oo = get_Value(COLUMNNAME_Admonition);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Admonition2.
		@param Admonition2 Admonition2	  */
	public void setAdmonition2 (boolean Admonition2)
	{
		set_Value (COLUMNNAME_Admonition2, Boolean.valueOf(Admonition2));
	}

	/** Get Admonition2.
		@return Admonition2	  */
	public boolean isAdmonition2 () 
	{
		Object oo = get_Value(COLUMNNAME_Admonition2);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Alert Message.
		@param AlertMessage 
		Message of the Alert
	  */
	public void setAlertMessage (String AlertMessage)
	{
		set_Value (COLUMNNAME_AlertMessage, AlertMessage);
	}

	/** Get Alert Message.
		@return Message of the Alert
	  */
	public String getAlertMessage () 
	{
		return (String)get_Value(COLUMNNAME_AlertMessage);
	}

	/** Set Assistants.
		@param Assistants Assistants	  */
	public void setAssistants (String Assistants)
	{
		set_Value (COLUMNNAME_Assistants, Assistants);
	}

	/** Get Assistants.
		@return Assistants	  */
	public String getAssistants () 
	{
		return (String)get_Value(COLUMNNAME_Assistants);
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

	public I_C_ProjectOFB getC_Project() throws RuntimeException
    {
		return (I_C_ProjectOFB)MTable.get(getCtx(), I_C_ProjectOFB.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_Value (COLUMNNAME_C_Project_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ProjectOFB getC_Project2() throws RuntimeException
    {
		return (I_C_ProjectOFB)MTable.get(getCtx(), I_C_ProjectOFB.Table_Name)
			.getPO(getC_Project2_ID(), get_TrxName());	}

	/** Set C_Project2_ID.
		@param C_Project2_ID C_Project2_ID	  */
	public void setC_Project2_ID (int C_Project2_ID)
	{
		if (C_Project2_ID < 1) 
			set_Value (COLUMNNAME_C_Project2_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project2_ID, Integer.valueOf(C_Project2_ID));
	}

	/** Get C_Project2_ID.
		@return C_Project2_ID	  */
	public int getC_Project2_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Year getC_Year() throws RuntimeException
    {
		return (I_C_Year)MTable.get(getCtx(), I_C_Year.Table_Name)
			.getPO(getC_Year_ID(), get_TrxName());	}

	/** Set Year.
		@param C_Year_ID 
		Calendar Year
	  */
	public void setC_Year_ID (int C_Year_ID)
	{
		if (C_Year_ID < 1) 
			set_Value (COLUMNNAME_C_Year_ID, null);
		else 
			set_Value (COLUMNNAME_C_Year_ID, Integer.valueOf(C_Year_ID));
	}

	/** Get Year.
		@return Calendar Year
	  */
	public int getC_Year_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Year_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

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

	/** Set Cause2.
		@param Cause2 Cause2	  */
	public void setCause2 (String Cause2)
	{
		set_Value (COLUMNNAME_Cause2, Cause2);
	}

	/** Get Cause2.
		@return Cause2	  */
	public String getCause2 () 
	{
		return (String)get_Value(COLUMNNAME_Cause2);
	}

	/** Set Charge amount.
		@param ChargeAmt 
		Charge Amount
	  */
	public void setChargeAmt (BigDecimal ChargeAmt)
	{
		set_Value (COLUMNNAME_ChargeAmt, ChargeAmt);
	}

	/** Get Charge amount.
		@return Charge Amount
	  */
	public BigDecimal getChargeAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ChargeAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Classification AD_Reference_ID=1000082 */
	public static final int CLASSIFICATION_AD_Reference_ID=1000082;
	/** SSOMAC = 01 */
	public static final String CLASSIFICATION_SSOMAC = "01";
	/** Operacional = 02 */
	public static final String CLASSIFICATION_Operacional = "02";
	/** Otros = 03 */
	public static final String CLASSIFICATION_Otros = "03";
	/** Multa = 04 */
	public static final String CLASSIFICATION_Multa = "04";
	/** Set Classification.
		@param Classification 
		Classification for grouping
	  */
	public void setClassification (String Classification)
	{

		set_Value (COLUMNNAME_Classification, Classification);
	}

	/** Get Classification.
		@return Classification for grouping
	  */
	public String getClassification () 
	{
		return (String)get_Value(COLUMNNAME_Classification);
	}

	/** Set Commitment Type.
		@param CommitmentType 
		Create Commitment and/or Reservations for Budget Control
	  */
	public void setCommitmentType (String CommitmentType)
	{
		set_Value (COLUMNNAME_CommitmentType, CommitmentType);
	}

	/** Get Commitment Type.
		@return Create Commitment and/or Reservations for Budget Control
	  */
	public String getCommitmentType () 
	{
		return (String)get_Value(COLUMNNAME_CommitmentType);
	}

	/** Set CommitteeDate.
		@param CommitteeDate CommitteeDate	  */
	public void setCommitteeDate (Timestamp CommitteeDate)
	{
		set_Value (COLUMNNAME_CommitteeDate, CommitteeDate);
	}

	/** Get CommitteeDate.
		@return CommitteeDate	  */
	public Timestamp getCommitteeDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_CommitteeDate);
	}

	/** Set ConstancyDate.
		@param ConstancyDate ConstancyDate	  */
	public void setConstancyDate (Timestamp ConstancyDate)
	{
		set_Value (COLUMNNAME_ConstancyDate, ConstancyDate);
	}

	/** Get ConstancyDate.
		@return ConstancyDate	  */
	public Timestamp getConstancyDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ConstancyDate);
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

	/** Set Report Date.
		@param DateReport 
		Expense/Time Report Date
	  */
	public void setDateReport (Timestamp DateReport)
	{
		set_Value (COLUMNNAME_DateReport, DateReport);
	}

	/** Get Report Date.
		@return Expense/Time Report Date
	  */
	public Timestamp getDateReport () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateReport);
	}

	/** Set Date Required.
		@param DateRequired 
		Date when required
	  */
	public void setDateRequired (Timestamp DateRequired)
	{
		set_Value (COLUMNNAME_DateRequired, DateRequired);
	}

	/** Get Date Required.
		@return Date when required
	  */
	public Timestamp getDateRequired () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateRequired);
	}

	/** Set Delivery Via.
		@param DeliveryViaRule 
		How the order will be delivered
	  */
	public void setDeliveryViaRule (String DeliveryViaRule)
	{
		set_Value (COLUMNNAME_DeliveryViaRule, DeliveryViaRule);
	}

	/** Get Delivery Via.
		@return How the order will be delivered
	  */
	public String getDeliveryViaRule () 
	{
		return (String)get_Value(COLUMNNAME_DeliveryViaRule);
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

	/** Set Detail Information.
		@param DetailInfo 
		Additional Detail Information
	  */
	public void setDetailInfo (String DetailInfo)
	{
		set_Value (COLUMNNAME_DetailInfo, DetailInfo);
	}

	/** Get Detail Information.
		@return Additional Detail Information
	  */
	public String getDetailInfo () 
	{
		return (String)get_Value(COLUMNNAME_DetailInfo);
	}

	/** Set Discount %.
		@param Discount 
		Discount in percent
	  */
	public void setDiscount (boolean Discount)
	{
		set_Value (COLUMNNAME_Discount, Boolean.valueOf(Discount));
	}

	/** Get Discount %.
		@return Discount in percent
	  */
	public boolean isDiscount () 
	{
		Object oo = get_Value(COLUMNNAME_Discount);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** DocStatus AD_Reference_ID=1000081 */
	public static final int DOCSTATUS_AD_Reference_ID=1000081;
	/** Borrador = DR */
	public static final String DOCSTATUS_Borrador = "DR";
	/** En Mantenimiento = EM */
	public static final String DOCSTATUS_EnMantenimiento = "EM";
	/** En Operaciones = EO */
	public static final String DOCSTATUS_EnOperaciones = "EO";
	/** En Comite = EC */
	public static final String DOCSTATUS_EnComite = "EC";
	/** En RRHH = ER */
	public static final String DOCSTATUS_EnRRHH = "ER";
	/** Completo = CO */
	public static final String DOCSTATUS_Completo = "CO";
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

	/** DriverStatus AD_Reference_ID=1000079 */
	public static final int DRIVERSTATUS_AD_Reference_ID=1000079;
	/** Vigente = VI */
	public static final String DRIVERSTATUS_Vigente = "VI";
	/** Desvinculado = DE */
	public static final String DRIVERSTATUS_Desvinculado = "DE";
	/** Set DriverStatus.
		@param DriverStatus DriverStatus	  */
	public void setDriverStatus (String DriverStatus)
	{

		set_Value (COLUMNNAME_DriverStatus, DriverStatus);
	}

	/** Get DriverStatus.
		@return DriverStatus	  */
	public String getDriverStatus () 
	{
		return (String)get_Value(COLUMNNAME_DriverStatus);
	}

	/** Set EMail Verify.
		@param EMailVerifyDate 
		Date Email was verified
	  */
	public void setEMailVerifyDate (Timestamp EMailVerifyDate)
	{
		set_Value (COLUMNNAME_EMailVerifyDate, EMailVerifyDate);
	}

	/** Get EMail Verify.
		@return Date Email was verified
	  */
	public Timestamp getEMailVerifyDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EMailVerifyDate);
	}

	/** Set Evaluation.
		@param Evaluation Evaluation	  */
	public void setEvaluation (String Evaluation)
	{
		set_Value (COLUMNNAME_Evaluation, Evaluation);
	}

	/** Get Evaluation.
		@return Evaluation	  */
	public String getEvaluation () 
	{
		return (String)get_Value(COLUMNNAME_Evaluation);
	}

	/** Set Fix amount.
		@param FixAmt 
		Fix amounted amount to be levied or paid
	  */
	public void setFixAmt (BigDecimal FixAmt)
	{
		set_Value (COLUMNNAME_FixAmt, FixAmt);
	}

	/** Get Fix amount.
		@return Fix amounted amount to be levied or paid
	  */
	public BigDecimal getFixAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_FixAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Grade AD_Reference_ID=1000084 */
	public static final int GRADE_AD_Reference_ID=1000084;
	/** G1 = G1 */
	public static final String GRADE_G1 = "G1";
	/** G2 = G2 */
	public static final String GRADE_G2 = "G2";
	/** G3 = G3 */
	public static final String GRADE_G3 = "G3";
	/** Set Grade.
		@param Grade Grade	  */
	public void setGrade (String Grade)
	{

		set_Value (COLUMNNAME_Grade, Grade);
	}

	/** Get Grade.
		@return Grade	  */
	public String getGrade () 
	{
		return (String)get_Value(COLUMNNAME_Grade);
	}

	/** Set Group1.
		@param Group1 Group1	  */
	public void setGroup1 (String Group1)
	{
		set_Value (COLUMNNAME_Group1, Group1);
	}

	/** Get Group1.
		@return Group1	  */
	public String getGroup1 () 
	{
		return (String)get_Value(COLUMNNAME_Group1);
	}

	/** Set Informed.
		@param Informed Informed	  */
	public void setInformed (String Informed)
	{
		set_Value (COLUMNNAME_Informed, Informed);
	}

	/** Get Informed.
		@return Informed	  */
	public String getInformed () 
	{
		return (String)get_Value(COLUMNNAME_Informed);
	}

	/** Set Time Report.
		@param IsTimeReport 
		Line is a time report only (no expense)
	  */
	public void setIsTimeReport (Timestamp IsTimeReport)
	{
		set_Value (COLUMNNAME_IsTimeReport, IsTimeReport);
	}

	/** Get Time Report.
		@return Line is a time report only (no expense)
	  */
	public Timestamp getIsTimeReport () 
	{
		return (Timestamp)get_Value(COLUMNNAME_IsTimeReport);
	}

	/** Set Print Item Name.
		@param ItemName Print Item Name	  */
	public void setItemName (String ItemName)
	{
		set_Value (COLUMNNAME_ItemName, ItemName);
	}

	/** Get Print Item Name.
		@return Print Item Name	  */
	public String getItemName () 
	{
		return (String)get_Value(COLUMNNAME_ItemName);
	}

	public I_M_Product getM_Product() throws RuntimeException
    {
		return (I_M_Product)MTable.get(getCtx(), I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Mail Text.
		@param MailText 
		Text used for Mail message
	  */
	public void setMailText (boolean MailText)
	{
		set_Value (COLUMNNAME_MailText, Boolean.valueOf(MailText));
	}

	/** Get Mail Text.
		@return Text used for Mail message
	  */
	public boolean isMailText () 
	{
		Object oo = get_Value(COLUMNNAME_MailText);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set month.
		@param month month	  */
	public void setmonth (String month)
	{
		set_Value (COLUMNNAME_month, month);
	}

	/** Get month.
		@return month	  */
	public String getmonth () 
	{
		return (String)get_Value(COLUMNNAME_month);
	}

	/** Set Number of Months.
		@param NoMonths Number of Months	  */
	public void setNoMonths (int NoMonths)
	{
		set_Value (COLUMNNAME_NoMonths, Integer.valueOf(NoMonths));
	}

	/** Get Number of Months.
		@return Number of Months	  */
	public int getNoMonths () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NoMonths);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set OFB_Ssomac.
		@param OFB_Ssomac_ID OFB_Ssomac	  */
	public void setOFB_Ssomac_ID (int OFB_Ssomac_ID)
	{
		if (OFB_Ssomac_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_OFB_Ssomac_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_OFB_Ssomac_ID, Integer.valueOf(OFB_Ssomac_ID));
	}

	/** Get OFB_Ssomac.
		@return OFB_Ssomac	  */
	public int getOFB_Ssomac_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_OFB_Ssomac_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payment Term Key.
		@param PaymentTermValue 
		Key of the Payment Term
	  */
	public void setPaymentTermValue (BigDecimal PaymentTermValue)
	{
		set_Value (COLUMNNAME_PaymentTermValue, PaymentTermValue);
	}

	/** Get Payment Term Key.
		@return Key of the Payment Term
	  */
	public BigDecimal getPaymentTermValue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PaymentTermValue);
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

	public I_AD_User getResponsi() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getResponsible(), get_TrxName());	}

	/** Set Responsible.
		@param Responsible Responsible	  */
	public void setResponsible (int Responsible)
	{
		set_Value (COLUMNNAME_Responsible, Integer.valueOf(Responsible));
	}

	/** Get Responsible.
		@return Responsible	  */
	public int getResponsible () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Responsible);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set rut_conductor.
		@param rut_conductor rut_conductor	  */
	public void setrut_conductor (String rut_conductor)
	{
		throw new IllegalArgumentException ("rut_conductor is virtual column");	}

	/** Get rut_conductor.
		@return rut_conductor	  */
	public String getrut_conductor () 
	{
		return (String)get_Value(COLUMNNAME_rut_conductor);
	}

	/** Status AD_Reference_ID=1000080 */
	public static final int STATUS_AD_Reference_ID=1000080;
	/** Abierto = OP */
	public static final String STATUS_Abierto = "OP";
	/** Cerrado = CL */
	public static final String STATUS_Cerrado = "CL";
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

	/** Set Tender type.
		@param TenderType 
		Method of Payment
	  */
	public void setTenderType (String TenderType)
	{
		set_Value (COLUMNNAME_TenderType, TenderType);
	}

	/** Get Tender type.
		@return Method of Payment
	  */
	public String getTenderType () 
	{
		return (String)get_Value(COLUMNNAME_TenderType);
	}

	/** Set Training.
		@param Training Training	  */
	public void setTraining (boolean Training)
	{
		set_Value (COLUMNNAME_Training, Boolean.valueOf(Training));
	}

	/** Get Training.
		@return Training	  */
	public boolean isTraining () 
	{
		Object oo = get_Value(COLUMNNAME_Training);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Type AD_Reference_ID=1000083 */
	public static final int TYPE_AD_Reference_ID=1000083;
	/** ACTP = 01 */
	public static final String TYPE_ACTP = "01";
	/** ASTP = 02 */
	public static final String TYPE_ASTP = "02";
	/** CHOQUE = 03 */
	public static final String TYPE_CHOQUE = "03";
	/** COLISIÓN = 04 */
	public static final String TYPE_COLISION = "04";
	/** CONTAMINACIÓN = 05 */
	public static final String TYPE_CONTAMINACION = "05";
	/** DERRAME = 06 */
	public static final String TYPE_DERRAME = "06";
	/** OPERACIONAL = 07 */
	public static final String TYPE_OPERACIONAL = "07";
	/** OTROS = 09 */
	public static final String TYPE_OTROS = "09";
	/** ROBO = 10 */
	public static final String TYPE_ROBO = "10";
	/** SOBRELLENADO = 11 */
	public static final String TYPE_SOBRELLENADO = "11";
	/** VOLCAMIENTO = 12 */
	public static final String TYPE_VOLCAMIENTO = "12";
	/** MULTAS = 13 */
	public static final String TYPE_MULTAS = "13";
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