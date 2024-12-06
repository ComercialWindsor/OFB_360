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

/** Generated Model for HR_EmployeeBackup
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_HR_EmployeeBackup extends PO implements I_HR_EmployeeBackup, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20181218L;

    /** Standard Constructor */
    public X_HR_EmployeeBackup (Properties ctx, int HR_EmployeeBackup_ID, String trxName)
    {
      super (ctx, HR_EmployeeBackup_ID, trxName);
      /** if (HR_EmployeeBackup_ID == 0)
        {
			setHR_EmployeeBackup_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HR_EmployeeBackup (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HR_EmployeeBackup[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AFP_Porcent.
		@param AFP_Porcent AFP_Porcent	  */
	public void setAFP_Porcent (BigDecimal AFP_Porcent)
	{
		set_Value (COLUMNNAME_AFP_Porcent, AFP_Porcent);
	}

	/** Get AFP_Porcent.
		@return AFP_Porcent	  */
	public BigDecimal getAFP_Porcent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AFP_Porcent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	public I_C_ValidCombination getC_BPartner() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
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

	/** Set Date Start.
		@param DateStart 
		Date Start for this Order
	  */
	public void setDateStart (Timestamp DateStart)
	{
		set_Value (COLUMNNAME_DateStart, DateStart);
	}

	/** Get Date Start.
		@return Date Start for this Order
	  */
	public Timestamp getDateStart () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateStart);
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

	public org.eevolution.model.I_HR_Employee getHR_Employee() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Employee)MTable.get(getCtx(), org.eevolution.model.I_HR_Employee.Table_Name)
			.getPO(getHR_Employee_ID(), get_TrxName());	}

	/** Set Payroll Employee.
		@param HR_Employee_ID Payroll Employee	  */
	public void setHR_Employee_ID (int HR_Employee_ID)
	{
		if (HR_Employee_ID < 1) 
			set_Value (COLUMNNAME_HR_Employee_ID, null);
		else 
			set_Value (COLUMNNAME_HR_Employee_ID, Integer.valueOf(HR_Employee_ID));
	}

	/** Get Payroll Employee.
		@return Payroll Employee	  */
	public int getHR_Employee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Employee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HR_EmployeeBackup ID.
		@param HR_EmployeeBackup_ID HR_EmployeeBackup ID	  */
	public void setHR_EmployeeBackup_ID (int HR_EmployeeBackup_ID)
	{
		if (HR_EmployeeBackup_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_EmployeeBackup_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_EmployeeBackup_ID, Integer.valueOf(HR_EmployeeBackup_ID));
	}

	/** Get HR_EmployeeBackup ID.
		@return HR_EmployeeBackup ID	  */
	public int getHR_EmployeeBackup_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_EmployeeBackup_ID);
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

	/** Set ISAPRE_Porcent.
		@param ISAPRE_Porcent ISAPRE_Porcent	  */
	public void setISAPRE_Porcent (BigDecimal ISAPRE_Porcent)
	{
		set_Value (COLUMNNAME_ISAPRE_Porcent, ISAPRE_Porcent);
	}

	/** Get ISAPRE_Porcent.
		@return ISAPRE_Porcent	  */
	public BigDecimal getISAPRE_Porcent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ISAPRE_Porcent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ISAPRE_UF.
		@param ISAPRE_UF ISAPRE_UF	  */
	public void setISAPRE_UF (BigDecimal ISAPRE_UF)
	{
		set_Value (COLUMNNAME_ISAPRE_UF, ISAPRE_UF);
	}

	/** Get ISAPRE_UF.
		@return ISAPRE_UF	  */
	public BigDecimal getISAPRE_UF () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ISAPRE_UF);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set IsINP.
		@param IsINP IsINP	  */
	public void setIsINP (boolean IsINP)
	{
		set_Value (COLUMNNAME_IsINP, Boolean.valueOf(IsINP));
	}

	/** Get IsINP.
		@return IsINP	  */
	public boolean isINP () 
	{
		Object oo = get_Value(COLUMNNAME_IsINP);
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

	/** Set MaxAmount.
		@param MaxAmount MaxAmount	  */
	public void setMaxAmount (BigDecimal MaxAmount)
	{
		set_Value (COLUMNNAME_MaxAmount, MaxAmount);
	}

	/** Get MaxAmount.
		@return MaxAmount	  */
	public BigDecimal getMaxAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MaxAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TopeIsapre_UF.
		@param TopeIsapre_UF TopeIsapre_UF	  */
	public void setTopeIsapre_UF (BigDecimal TopeIsapre_UF)
	{
		set_Value (COLUMNNAME_TopeIsapre_UF, TopeIsapre_UF);
	}

	/** Get TopeIsapre_UF.
		@return TopeIsapre_UF	  */
	public BigDecimal getTopeIsapre_UF () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TopeIsapre_UF);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** TypeUniqueTax AD_Reference_ID=2000162 */
	public static final int TYPEUNIQUETAX_AD_Reference_ID=2000162;
	/** Tipo 1 = 01 */
	public static final String TYPEUNIQUETAX_Tipo1 = "01";
	/** Tipo 2 = 02 */
	public static final String TYPEUNIQUETAX_Tipo2 = "02";
	/** Set TypeUniqueTax.
		@param TypeUniqueTax TypeUniqueTax	  */
	public void setTypeUniqueTax (String TypeUniqueTax)
	{

		set_Value (COLUMNNAME_TypeUniqueTax, TypeUniqueTax);
	}

	/** Get TypeUniqueTax.
		@return TypeUniqueTax	  */
	public String getTypeUniqueTax () 
	{
		return (String)get_Value(COLUMNNAME_TypeUniqueTax);
	}

	/** Set UseUniqueTax.
		@param UseUniqueTax UseUniqueTax	  */
	public void setUseUniqueTax (boolean UseUniqueTax)
	{
		set_Value (COLUMNNAME_UseUniqueTax, Boolean.valueOf(UseUniqueTax));
	}

	/** Get UseUniqueTax.
		@return UseUniqueTax	  */
	public boolean isUseUniqueTax () 
	{
		Object oo = get_Value(COLUMNNAME_UseUniqueTax);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}