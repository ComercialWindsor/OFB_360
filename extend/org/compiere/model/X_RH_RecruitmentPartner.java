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

/** Generated Model for RH_RecruitmentPartner
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_RecruitmentPartner extends PO implements I_RH_RecruitmentPartner, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170327L;

    /** Standard Constructor */
    public X_RH_RecruitmentPartner (Properties ctx, int RH_RecruitmentPartner_ID, String trxName)
    {
      super (ctx, RH_RecruitmentPartner_ID, trxName);
      /** if (RH_RecruitmentPartner_ID == 0)
        {
			setIsValid (false);
// N
			setName (null);
			setProcessed (false);
			setRH_RecruitmentPartner_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_RecruitmentPartner (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_RecruitmentPartner[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Date2.
		@param Date2 Date2	  */
	public void setDate2 (Timestamp Date2)
	{
		set_Value (COLUMNNAME_Date2, Date2);
	}

	/** Get Date2.
		@return Date2	  */
	public Timestamp getDate2 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date2);
	}

	/** Set Date3.
		@param Date3 Date3	  */
	public void setDate3 (Timestamp Date3)
	{
		set_Value (COLUMNNAME_Date3, Date3);
	}

	/** Get Date3.
		@return Date3	  */
	public Timestamp getDate3 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date3);
	}

	/** Set Date4.
		@param Date4 Date4	  */
	public void setDate4 (Timestamp Date4)
	{
		set_Value (COLUMNNAME_Date4, Date4);
	}

	/** Get Date4.
		@return Date4	  */
	public Timestamp getDate4 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date4);
	}

	/** Set Date5.
		@param Date5 Date5	  */
	public void setDate5 (Timestamp Date5)
	{
		set_Value (COLUMNNAME_Date5, Date5);
	}

	/** Get Date5.
		@return Date5	  */
	public Timestamp getDate5 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date5);
	}

	/** Set Date6.
		@param Date6 Date6	  */
	public void setDate6 (Timestamp Date6)
	{
		set_Value (COLUMNNAME_Date6, Date6);
	}

	/** Get Date6.
		@return Date6	  */
	public Timestamp getDate6 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date6);
	}

	/** Set Date7.
		@param Date7 Date7	  */
	public void setDate7 (Timestamp Date7)
	{
		set_Value (COLUMNNAME_Date7, Date7);
	}

	/** Get Date7.
		@return Date7	  */
	public Timestamp getDate7 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date7);
	}

	/** Set Date8.
		@param Date8 Date8	  */
	public void setDate8 (Timestamp Date8)
	{
		set_Value (COLUMNNAME_Date8, Date8);
	}

	/** Get Date8.
		@return Date8	  */
	public Timestamp getDate8 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date8);
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

	/** Set DrivingTestAssessment.
		@param DrivingTestAssessment DrivingTestAssessment	  */
	public void setDrivingTestAssessment (String DrivingTestAssessment)
	{
		set_Value (COLUMNNAME_DrivingTestAssessment, DrivingTestAssessment);
	}

	/** Get DrivingTestAssessment.
		@return DrivingTestAssessment	  */
	public String getDrivingTestAssessment () 
	{
		return (String)get_Value(COLUMNNAME_DrivingTestAssessment);
	}

	/** Set DrivingTestAssessmentDesc.
		@param DrivingTestAssessmentDesc DrivingTestAssessmentDesc	  */
	public void setDrivingTestAssessmentDesc (String DrivingTestAssessmentDesc)
	{
		set_Value (COLUMNNAME_DrivingTestAssessmentDesc, DrivingTestAssessmentDesc);
	}

	/** Get DrivingTestAssessmentDesc.
		@return DrivingTestAssessmentDesc	  */
	public String getDrivingTestAssessmentDesc () 
	{
		return (String)get_Value(COLUMNNAME_DrivingTestAssessmentDesc);
	}

	/** Set EvaluationKeyClouding.
		@param EvaluationKeyClouding EvaluationKeyClouding	  */
	public void setEvaluationKeyClouding (String EvaluationKeyClouding)
	{
		set_Value (COLUMNNAME_EvaluationKeyClouding, EvaluationKeyClouding);
	}

	/** Get EvaluationKeyClouding.
		@return EvaluationKeyClouding	  */
	public String getEvaluationKeyClouding () 
	{
		return (String)get_Value(COLUMNNAME_EvaluationKeyClouding);
	}

	/** Set EvaluationKeyCloudingDesc.
		@param EvaluationKeyCloudingDesc EvaluationKeyCloudingDesc	  */
	public void setEvaluationKeyCloudingDesc (String EvaluationKeyCloudingDesc)
	{
		set_Value (COLUMNNAME_EvaluationKeyCloudingDesc, EvaluationKeyCloudingDesc);
	}

	/** Get EvaluationKeyCloudingDesc.
		@return EvaluationKeyCloudingDesc	  */
	public String getEvaluationKeyCloudingDesc () 
	{
		return (String)get_Value(COLUMNNAME_EvaluationKeyCloudingDesc);
	}

	/** Set ExaminationPerformed.
		@param ExaminationPerformed ExaminationPerformed	  */
	public void setExaminationPerformed (String ExaminationPerformed)
	{
		set_Value (COLUMNNAME_ExaminationPerformed, ExaminationPerformed);
	}

	/** Get ExaminationPerformed.
		@return ExaminationPerformed	  */
	public String getExaminationPerformed () 
	{
		return (String)get_Value(COLUMNNAME_ExaminationPerformed);
	}

	/** Set ExaminationPerformedDesc.
		@param ExaminationPerformedDesc ExaminationPerformedDesc	  */
	public void setExaminationPerformedDesc (String ExaminationPerformedDesc)
	{
		set_Value (COLUMNNAME_ExaminationPerformedDesc, ExaminationPerformedDesc);
	}

	/** Get ExaminationPerformedDesc.
		@return ExaminationPerformedDesc	  */
	public String getExaminationPerformedDesc () 
	{
		return (String)get_Value(COLUMNNAME_ExaminationPerformedDesc);
	}

	/** Set InducedPerformance.
		@param InducedPerformance InducedPerformance	  */
	public void setInducedPerformance (String InducedPerformance)
	{
		set_Value (COLUMNNAME_InducedPerformance, InducedPerformance);
	}

	/** Get InducedPerformance.
		@return InducedPerformance	  */
	public String getInducedPerformance () 
	{
		return (String)get_Value(COLUMNNAME_InducedPerformance);
	}

	/** Set InducedPerformanceDesc.
		@param InducedPerformanceDesc InducedPerformanceDesc	  */
	public void setInducedPerformanceDesc (String InducedPerformanceDesc)
	{
		set_Value (COLUMNNAME_InducedPerformanceDesc, InducedPerformanceDesc);
	}

	/** Get InducedPerformanceDesc.
		@return InducedPerformanceDesc	  */
	public String getInducedPerformanceDesc () 
	{
		return (String)get_Value(COLUMNNAME_InducedPerformanceDesc);
	}

	/** Set Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_Value (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved () 
	{
		Object oo = get_Value(COLUMNNAME_IsApproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Valid.
		@param IsValid 
		Element is valid
	  */
	public void setIsValid (boolean IsValid)
	{
		set_Value (COLUMNNAME_IsValid, Boolean.valueOf(IsValid));
	}

	/** Get Valid.
		@return Element is valid
	  */
	public boolean isValid () 
	{
		Object oo = get_Value(COLUMNNAME_IsValid);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set LaborReferencesEvaluation.
		@param LaborReferencesEvaluation LaborReferencesEvaluation	  */
	public void setLaborReferencesEvaluation (String LaborReferencesEvaluation)
	{
		set_Value (COLUMNNAME_LaborReferencesEvaluation, LaborReferencesEvaluation);
	}

	/** Get LaborReferencesEvaluation.
		@return LaborReferencesEvaluation	  */
	public String getLaborReferencesEvaluation () 
	{
		return (String)get_Value(COLUMNNAME_LaborReferencesEvaluation);
	}

	/** Set LaborReferencesEvaluationDesc.
		@param LaborReferencesEvaluationDesc LaborReferencesEvaluationDesc	  */
	public void setLaborReferencesEvaluationDesc (String LaborReferencesEvaluationDesc)
	{
		set_Value (COLUMNNAME_LaborReferencesEvaluationDesc, LaborReferencesEvaluationDesc);
	}

	/** Get LaborReferencesEvaluationDesc.
		@return LaborReferencesEvaluationDesc	  */
	public String getLaborReferencesEvaluationDesc () 
	{
		return (String)get_Value(COLUMNNAME_LaborReferencesEvaluationDesc);
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

	/** Set PsycholaboralEvaluation.
		@param PsycholaboralEvaluation PsycholaboralEvaluation	  */
	public void setPsycholaboralEvaluation (String PsycholaboralEvaluation)
	{
		set_Value (COLUMNNAME_PsycholaboralEvaluation, PsycholaboralEvaluation);
	}

	/** Get PsycholaboralEvaluation.
		@return PsycholaboralEvaluation	  */
	public String getPsycholaboralEvaluation () 
	{
		return (String)get_Value(COLUMNNAME_PsycholaboralEvaluation);
	}

	/** Set PsycholaboralEvaluationDesc.
		@param PsycholaboralEvaluationDesc PsycholaboralEvaluationDesc	  */
	public void setPsycholaboralEvaluationDesc (String PsycholaboralEvaluationDesc)
	{
		set_Value (COLUMNNAME_PsycholaboralEvaluationDesc, PsycholaboralEvaluationDesc);
	}

	/** Get PsycholaboralEvaluationDesc.
		@return PsycholaboralEvaluationDesc	  */
	public String getPsycholaboralEvaluationDesc () 
	{
		return (String)get_Value(COLUMNNAME_PsycholaboralEvaluationDesc);
	}

	public I_RH_RecruitmentDocument getRH_RecruitmentDocument() throws RuntimeException
    {
		return (I_RH_RecruitmentDocument)MTable.get(getCtx(), I_RH_RecruitmentDocument.Table_Name)
			.getPO(getRH_RecruitmentDocument_ID(), get_TrxName());	}

	/** Set RH_RecruitmentDocument.
		@param RH_RecruitmentDocument_ID RH_RecruitmentDocument	  */
	public void setRH_RecruitmentDocument_ID (int RH_RecruitmentDocument_ID)
	{
		if (RH_RecruitmentDocument_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentDocument_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentDocument_ID, Integer.valueOf(RH_RecruitmentDocument_ID));
	}

	/** Get RH_RecruitmentDocument.
		@return RH_RecruitmentDocument	  */
	public int getRH_RecruitmentDocument_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_RecruitmentDocument_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set RH_RecruitmentPartner_ID.
		@param RH_RecruitmentPartner_ID RH_RecruitmentPartner_ID	  */
	public void setRH_RecruitmentPartner_ID (int RH_RecruitmentPartner_ID)
	{
		if (RH_RecruitmentPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_RecruitmentPartner_ID, Integer.valueOf(RH_RecruitmentPartner_ID));
	}

	/** Get RH_RecruitmentPartner_ID.
		@return RH_RecruitmentPartner_ID	  */
	public int getRH_RecruitmentPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_RecruitmentPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set rhbackground.
		@param rhbackground rhbackground	  */
	public void setrhbackground (String rhbackground)
	{
		set_Value (COLUMNNAME_rhbackground, rhbackground);
	}

	/** Get rhbackground.
		@return rhbackground	  */
	public String getrhbackground () 
	{
		return (String)get_Value(COLUMNNAME_rhbackground);
	}

	/** Set rhbackgrounddesc.
		@param rhbackgrounddesc rhbackgrounddesc	  */
	public void setrhbackgrounddesc (String rhbackgrounddesc)
	{
		set_Value (COLUMNNAME_rhbackgrounddesc, rhbackgrounddesc);
	}

	/** Get rhbackgrounddesc.
		@return rhbackgrounddesc	  */
	public String getrhbackgrounddesc () 
	{
		return (String)get_Value(COLUMNNAME_rhbackgrounddesc);
	}

	/** Set rhprescreening.
		@param rhprescreening rhprescreening	  */
	public void setrhprescreening (String rhprescreening)
	{
		set_Value (COLUMNNAME_rhprescreening, rhprescreening);
	}

	/** Get rhprescreening.
		@return rhprescreening	  */
	public String getrhprescreening () 
	{
		return (String)get_Value(COLUMNNAME_rhprescreening);
	}

	/** Set rhprescreeningdesc.
		@param rhprescreeningdesc rhprescreeningdesc	  */
	public void setrhprescreeningdesc (String rhprescreeningdesc)
	{
		set_Value (COLUMNNAME_rhprescreeningdesc, rhprescreeningdesc);
	}

	/** Get rhprescreeningdesc.
		@return rhprescreeningdesc	  */
	public String getrhprescreeningdesc () 
	{
		return (String)get_Value(COLUMNNAME_rhprescreeningdesc);
	}
}