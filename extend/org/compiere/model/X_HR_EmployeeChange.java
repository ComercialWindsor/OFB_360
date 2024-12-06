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

/** Generated Model for HR_EmployeeChange
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_HR_EmployeeChange extends PO implements I_HR_EmployeeChange, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181218L;

    /** Standard Constructor */
    public X_HR_EmployeeChange (Properties ctx, int HR_EmployeeChange_ID, String trxName)
    {
      super (ctx, HR_EmployeeChange_ID, trxName);
      /** if (HR_EmployeeChange_ID == 0)
        {
			setC_BPartner_ID (0);
			setHR_EmployeeChange_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HR_EmployeeChange (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_EmployeeChange[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Benio AD_Reference_ID=2000157 */
	public static final int BENIO_AD_Reference_ID=2000157;
	/** 0 a 2 Años = 0-2 */
	public static final String BENIO_0A2Anos = "0-2";
	/** 2 a 4 Años = 2-4 */
	public static final String BENIO_2A4Anos = "2-4";
	/** 4 a 6 Años = 4-6 */
	public static final String BENIO_4A6Anos = "4-6";
	/** 6 a 8 Años = 6-8 */
	public static final String BENIO_6A8Anos = "6-8";
	/** 8 a 10 Años = 8-1 */
	public static final String BENIO_8A10Anos = "8-1";
	/** 10 a 12 Años = 10 */
	public static final String BENIO_10A12Anos = "10";
	/** 12 a 14 Años = 12 */
	public static final String BENIO_12A14Anos = "12";
	/** 14 a 16 Años = 14 */
	public static final String BENIO_14A16Anos = "14";
	/** 16 a 18 Años = 16 */
	public static final String BENIO_16A18Anos = "16";
	/** 18 a 20 Años = 18 */
	public static final String BENIO_18A20Anos = "18";
	/** 20 a 22 Años = 20 */
	public static final String BENIO_20A22Anos = "20";
	/** 22 a 24 Años = 22 */
	public static final String BENIO_22A24Anos = "22";
	/** 24 a 26 Años = 24 */
	public static final String BENIO_24A26Anos = "24";
	/** 26 a 28 Años = 26 */
	public static final String BENIO_26A28Anos = "26";
	/** 28 a 30 Años = 28 */
	public static final String BENIO_28A30Anos = "28";
	/** Set Benio.
		@param Benio Benio	  */
	public void setBenio (String Benio)
	{

		set_Value (COLUMNNAME_Benio, Benio);
	}

	/** Get Benio.
		@return Benio	  */
	public String getBenio () 
	{
		return (String)get_Value(COLUMNNAME_Benio);
	}

	/** BenioOld AD_Reference_ID=2000157 */
	public static final int BENIOOLD_AD_Reference_ID=2000157;
	/** 0 a 2 Años = 0-2 */
	public static final String BENIOOLD_0A2Anos = "0-2";
	/** 2 a 4 Años = 2-4 */
	public static final String BENIOOLD_2A4Anos = "2-4";
	/** 4 a 6 Años = 4-6 */
	public static final String BENIOOLD_4A6Anos = "4-6";
	/** 6 a 8 Años = 6-8 */
	public static final String BENIOOLD_6A8Anos = "6-8";
	/** 8 a 10 Años = 8-1 */
	public static final String BENIOOLD_8A10Anos = "8-1";
	/** 10 a 12 Años = 10 */
	public static final String BENIOOLD_10A12Anos = "10";
	/** 12 a 14 Años = 12 */
	public static final String BENIOOLD_12A14Anos = "12";
	/** 14 a 16 Años = 14 */
	public static final String BENIOOLD_14A16Anos = "14";
	/** 16 a 18 Años = 16 */
	public static final String BENIOOLD_16A18Anos = "16";
	/** 18 a 20 Años = 18 */
	public static final String BENIOOLD_18A20Anos = "18";
	/** 20 a 22 Años = 20 */
	public static final String BENIOOLD_20A22Anos = "20";
	/** 22 a 24 Años = 22 */
	public static final String BENIOOLD_22A24Anos = "22";
	/** 24 a 26 Años = 24 */
	public static final String BENIOOLD_24A26Anos = "24";
	/** 26 a 28 Años = 26 */
	public static final String BENIOOLD_26A28Anos = "26";
	/** 28 a 30 Años = 28 */
	public static final String BENIOOLD_28A30Anos = "28";
	/** Set BenioOld.
		@param BenioOld BenioOld	  */
	public void setBenioOld (String BenioOld)
	{

		set_Value (COLUMNNAME_BenioOld, BenioOld);
	}

	/** Get BenioOld.
		@return BenioOld	  */
	public String getBenioOld () 
	{
		return (String)get_Value(COLUMNNAME_BenioOld);
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
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

	public org.compiere.model.I_C_Region getC_Region() throws RuntimeException
    {
		return (org.compiere.model.I_C_Region)MTable.get(getCtx(), org.compiere.model.I_C_Region.Table_Name)
			.getPO(getC_Region_ID(), get_TrxName());	}

	/** Set Region.
		@param C_Region_ID 
		Identifies a geographical Region
	  */
	public void setC_Region_ID (int C_Region_ID)
	{
		if (C_Region_ID < 1) 
			set_Value (COLUMNNAME_C_Region_ID, null);
		else 
			set_Value (COLUMNNAME_C_Region_ID, Integer.valueOf(C_Region_ID));
	}

	/** Get Region.
		@return Identifies a geographical Region
	  */
	public int getC_Region_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Region_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Region getC_RegionOld() throws RuntimeException
    {
		return (org.compiere.model.I_C_Region)MTable.get(getCtx(), org.compiere.model.I_C_Region.Table_Name)
			.getPO(getC_RegionOld_ID(), get_TrxName());	}

	/** Set C_RegionOld_ID.
		@param C_RegionOld_ID C_RegionOld_ID	  */
	public void setC_RegionOld_ID (int C_RegionOld_ID)
	{
		if (C_RegionOld_ID < 1) 
			set_Value (COLUMNNAME_C_RegionOld_ID, null);
		else 
			set_Value (COLUMNNAME_C_RegionOld_ID, Integer.valueOf(C_RegionOld_ID));
	}

	/** Get C_RegionOld_ID.
		@return C_RegionOld_ID	  */
	public int getC_RegionOld_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_RegionOld_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** grade AD_Reference_ID=2000138 */
	public static final int GRADE_AD_Reference_ID=2000138;
	/** 1 = 01 */
	public static final String GRADE_1 = "01";
	/** 2 = 02 */
	public static final String GRADE_2 = "02";
	/** 3 = 03 */
	public static final String GRADE_3 = "03";
	/** 4 = 04 */
	public static final String GRADE_4 = "04";
	/** 5 = 05 */
	public static final String GRADE_5 = "05";
	/** 6 = 06 */
	public static final String GRADE_6 = "06";
	/** 7 = 07 */
	public static final String GRADE_7 = "07";
	/** 8 = 08 */
	public static final String GRADE_8 = "08";
	/** 9 = 09 */
	public static final String GRADE_9 = "09";
	/** 10 = 10 */
	public static final String GRADE_10 = "10";
	/** 11 = 11 */
	public static final String GRADE_11 = "11";
	/** 12 = 12 */
	public static final String GRADE_12 = "12";
	/** 13 = 13 */
	public static final String GRADE_13 = "13";
	/** 14 = 14 */
	public static final String GRADE_14 = "14";
	/** 15 = 15 */
	public static final String GRADE_15 = "15";
	/** 16 = 16 */
	public static final String GRADE_16 = "16";
	/** 17 = 17 */
	public static final String GRADE_17 = "17";
	/** 18 = 18 */
	public static final String GRADE_18 = "18";
	/** 19 = 19 */
	public static final String GRADE_19 = "19";
	/** 20 = 20 */
	public static final String GRADE_20 = "20";
	/** 21 = 21 */
	public static final String GRADE_21 = "21";
	/** 22 = 22 */
	public static final String GRADE_22 = "22";
	/** 23 = 23 */
	public static final String GRADE_23 = "23";
	/** 24 = 24 */
	public static final String GRADE_24 = "24";
	/** 25 = 25 */
	public static final String GRADE_25 = "25";
	/** 26 = 26 */
	public static final String GRADE_26 = "26";
	/** 27 = 27 */
	public static final String GRADE_27 = "27";
	/** 28 = 28 */
	public static final String GRADE_28 = "28";
	/** 29 = 29 */
	public static final String GRADE_29 = "29";
	/** 30 = 30 */
	public static final String GRADE_30 = "30";
	/** 1A = 1A */
	public static final String GRADE_1A = "1A";
	/** 1B = 1B */
	public static final String GRADE_1B = "1B";
	/** 1C = 1C */
	public static final String GRADE_1C = "1C";
	/** A = 0A */
	public static final String GRADE_A = "0A";
	/** B = 0B */
	public static final String GRADE_B = "0B";
	/** C = 0C */
	public static final String GRADE_C = "0C";
	/** 31 = 31 */
	public static final String GRADE_31 = "31";
	/** Set grade.
		@param grade grade	  */
	public void setgrade (String grade)
	{

		set_Value (COLUMNNAME_grade, grade);
	}

	/** Get grade.
		@return grade	  */
	public String getgrade () 
	{
		return (String)get_Value(COLUMNNAME_grade);
	}

	/** gradeOld AD_Reference_ID=2000138 */
	public static final int GRADEOLD_AD_Reference_ID=2000138;
	/** 1 = 01 */
	public static final String GRADEOLD_1 = "01";
	/** 2 = 02 */
	public static final String GRADEOLD_2 = "02";
	/** 3 = 03 */
	public static final String GRADEOLD_3 = "03";
	/** 4 = 04 */
	public static final String GRADEOLD_4 = "04";
	/** 5 = 05 */
	public static final String GRADEOLD_5 = "05";
	/** 6 = 06 */
	public static final String GRADEOLD_6 = "06";
	/** 7 = 07 */
	public static final String GRADEOLD_7 = "07";
	/** 8 = 08 */
	public static final String GRADEOLD_8 = "08";
	/** 9 = 09 */
	public static final String GRADEOLD_9 = "09";
	/** 10 = 10 */
	public static final String GRADEOLD_10 = "10";
	/** 11 = 11 */
	public static final String GRADEOLD_11 = "11";
	/** 12 = 12 */
	public static final String GRADEOLD_12 = "12";
	/** 13 = 13 */
	public static final String GRADEOLD_13 = "13";
	/** 14 = 14 */
	public static final String GRADEOLD_14 = "14";
	/** 15 = 15 */
	public static final String GRADEOLD_15 = "15";
	/** 16 = 16 */
	public static final String GRADEOLD_16 = "16";
	/** 17 = 17 */
	public static final String GRADEOLD_17 = "17";
	/** 18 = 18 */
	public static final String GRADEOLD_18 = "18";
	/** 19 = 19 */
	public static final String GRADEOLD_19 = "19";
	/** 20 = 20 */
	public static final String GRADEOLD_20 = "20";
	/** 21 = 21 */
	public static final String GRADEOLD_21 = "21";
	/** 22 = 22 */
	public static final String GRADEOLD_22 = "22";
	/** 23 = 23 */
	public static final String GRADEOLD_23 = "23";
	/** 24 = 24 */
	public static final String GRADEOLD_24 = "24";
	/** 25 = 25 */
	public static final String GRADEOLD_25 = "25";
	/** 26 = 26 */
	public static final String GRADEOLD_26 = "26";
	/** 27 = 27 */
	public static final String GRADEOLD_27 = "27";
	/** 28 = 28 */
	public static final String GRADEOLD_28 = "28";
	/** 29 = 29 */
	public static final String GRADEOLD_29 = "29";
	/** 30 = 30 */
	public static final String GRADEOLD_30 = "30";
	/** 1A = 1A */
	public static final String GRADEOLD_1A = "1A";
	/** 1B = 1B */
	public static final String GRADEOLD_1B = "1B";
	/** 1C = 1C */
	public static final String GRADEOLD_1C = "1C";
	/** A = 0A */
	public static final String GRADEOLD_A = "0A";
	/** B = 0B */
	public static final String GRADEOLD_B = "0B";
	/** C = 0C */
	public static final String GRADEOLD_C = "0C";
	/** 31 = 31 */
	public static final String GRADEOLD_31 = "31";
	/** Set gradeOld.
		@param gradeOld gradeOld	  */
	public void setgradeOld (String gradeOld)
	{

		set_Value (COLUMNNAME_gradeOld, gradeOld);
	}

	/** Get gradeOld.
		@return gradeOld	  */
	public String getgradeOld () 
	{
		return (String)get_Value(COLUMNNAME_gradeOld);
	}

	/** Set HR_EmployeeChange ID.
		@param HR_EmployeeChange_ID HR_EmployeeChange ID	  */
	public void setHR_EmployeeChange_ID (int HR_EmployeeChange_ID)
	{
		if (HR_EmployeeChange_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_EmployeeChange_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_EmployeeChange_ID, Integer.valueOf(HR_EmployeeChange_ID));
	}

	/** Get HR_EmployeeChange ID.
		@return HR_EmployeeChange ID	  */
	public int getHR_EmployeeChange_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_EmployeeChange_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public org.eevolution.model.I_HR_Job getHR_JobOld() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Job)MTable.get(getCtx(), org.eevolution.model.I_HR_Job.Table_Name)
			.getPO(getHR_JobOld_ID(), get_TrxName());	}

	/** Set HR_JobOld_ID.
		@param HR_JobOld_ID HR_JobOld_ID	  */
	public void setHR_JobOld_ID (int HR_JobOld_ID)
	{
		if (HR_JobOld_ID < 1) 
			set_Value (COLUMNNAME_HR_JobOld_ID, null);
		else 
			set_Value (COLUMNNAME_HR_JobOld_ID, Integer.valueOf(HR_JobOld_ID));
	}

	/** Get HR_JobOld_ID.
		@return HR_JobOld_ID	  */
	public int getHR_JobOld_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_JobOld_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set IsAResponsibility.
		@param IsAResponsibility IsAResponsibility	  */
	public void setIsAResponsibility (boolean IsAResponsibility)
	{
		set_Value (COLUMNNAME_IsAResponsibility, Boolean.valueOf(IsAResponsibility));
	}

	/** Get IsAResponsibility.
		@return IsAResponsibility	  */
	public boolean isAResponsibility () 
	{
		Object oo = get_Value(COLUMNNAME_IsAResponsibility);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsAResponsibilityOld.
		@param IsAResponsibilityOld IsAResponsibilityOld	  */
	public void setIsAResponsibilityOld (boolean IsAResponsibilityOld)
	{
		set_Value (COLUMNNAME_IsAResponsibilityOld, Boolean.valueOf(IsAResponsibilityOld));
	}

	/** Get IsAResponsibilityOld.
		@return IsAResponsibilityOld	  */
	public boolean isAResponsibilityOld () 
	{
		Object oo = get_Value(COLUMNNAME_IsAResponsibilityOld);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsBienestar.
		@param IsBienestar IsBienestar	  */
	public void setIsBienestar (boolean IsBienestar)
	{
		set_Value (COLUMNNAME_IsBienestar, Boolean.valueOf(IsBienestar));
	}

	/** Get IsBienestar.
		@return IsBienestar	  */
	public boolean isBienestar () 
	{
		Object oo = get_Value(COLUMNNAME_IsBienestar);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsBienestarOld.
		@param IsBienestarOld IsBienestarOld	  */
	public void setIsBienestarOld (boolean IsBienestarOld)
	{
		set_Value (COLUMNNAME_IsBienestarOld, Boolean.valueOf(IsBienestarOld));
	}

	/** Get IsBienestarOld.
		@return IsBienestarOld	  */
	public boolean isBienestarOld () 
	{
		Object oo = get_Value(COLUMNNAME_IsBienestarOld);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Is Critical Component.
		@param IsCritical 
		Indicate that a Manufacturing Order can not begin without have this component
	  */
	public void setIsCritical (boolean IsCritical)
	{
		set_Value (COLUMNNAME_IsCritical, Boolean.valueOf(IsCritical));
	}

	/** Get Is Critical Component.
		@return Indicate that a Manufacturing Order can not begin without have this component
	  */
	public boolean isCritical () 
	{
		Object oo = get_Value(COLUMNNAME_IsCritical);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsCriticalOld.
		@param IsCriticalOld IsCriticalOld	  */
	public void setIsCriticalOld (boolean IsCriticalOld)
	{
		set_Value (COLUMNNAME_IsCriticalOld, Boolean.valueOf(IsCriticalOld));
	}

	/** Get IsCriticalOld.
		@return IsCriticalOld	  */
	public boolean isCriticalOld () 
	{
		Object oo = get_Value(COLUMNNAME_IsCriticalOld);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsProfessional.
		@param IsProfessional IsProfessional	  */
	public void setIsProfessional (boolean IsProfessional)
	{
		set_Value (COLUMNNAME_IsProfessional, Boolean.valueOf(IsProfessional));
	}

	/** Get IsProfessional.
		@return IsProfessional	  */
	public boolean isProfessional () 
	{
		Object oo = get_Value(COLUMNNAME_IsProfessional);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsProfessionalOld.
		@param IsProfessionalOld IsProfessionalOld	  */
	public void setIsProfessionalOld (boolean IsProfessionalOld)
	{
		set_Value (COLUMNNAME_IsProfessionalOld, Boolean.valueOf(IsProfessionalOld));
	}

	/** Get IsProfessionalOld.
		@return IsProfessionalOld	  */
	public boolean isProfessionalOld () 
	{
		Object oo = get_Value(COLUMNNAME_IsProfessionalOld);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IsUsed.
		@param IsUsed IsUsed	  */
	public void setIsUsed (boolean IsUsed)
	{
		set_Value (COLUMNNAME_IsUsed, Boolean.valueOf(IsUsed));
	}

	/** Get IsUsed.
		@return IsUsed	  */
	public boolean isUsed () 
	{
		Object oo = get_Value(COLUMNNAME_IsUsed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ofbbutton.
		@param ofbbutton ofbbutton	  */
	public void setofbbutton (String ofbbutton)
	{
		set_Value (COLUMNNAME_ofbbutton, ofbbutton);
	}

	/** Get ofbbutton.
		@return ofbbutton	  */
	public String getofbbutton () 
	{
		return (String)get_Value(COLUMNNAME_ofbbutton);
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

	/** Set Quantity.
		@param QtyEntered 
		The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEntered (BigDecimal QtyEntered)
	{
		set_Value (COLUMNNAME_QtyEntered, QtyEntered);
	}

	/** Get Quantity.
		@return The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEntered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyEntered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set QtyEnteredOld.
		@param QtyEnteredOld 
		The Quantity Entered is based on the selected UoM
	  */
	public void setQtyEnteredOld (BigDecimal QtyEnteredOld)
	{
		set_Value (COLUMNNAME_QtyEnteredOld, QtyEnteredOld);
	}

	/** Get QtyEnteredOld.
		@return The Quantity Entered is based on the selected UoM
	  */
	public BigDecimal getQtyEnteredOld () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyEnteredOld);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}