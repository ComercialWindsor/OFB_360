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

/** Generated Model for PM_ProjectPay
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_PM_ProjectPay extends PO implements I_PM_ProjectPay, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140517L;

    /** Standard Constructor */
    public X_PM_ProjectPay (Properties ctx, int PM_ProjectPay_ID, String trxName)
    {
      super (ctx, PM_ProjectPay_ID, trxName);
      /** if (PM_ProjectPay_ID == 0)
        {
			setAmt (Env.ZERO);
			setPM_ProjectPay_ID (0);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_PM_ProjectPay (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_PM_ProjectPay[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount.
		@param Amt 
		Amount
	  */
	public void setAmt (BigDecimal Amt)
	{
		set_Value (COLUMNNAME_Amt, Amt);
	}

	/** Get Amount.
		@return Amount
	  */
	public BigDecimal getAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set AmtM.
		@param AmtM AmtM	  */
	public void setAmtM (BigDecimal AmtM)
	{
		set_Value (COLUMNNAME_AmtM, AmtM);
	}

	/** Get AmtM.
		@return AmtM	  */
	public BigDecimal getAmtM () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmtM);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (I_C_Invoice)MTable.get(getCtx(), I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Project getC_Project() throws RuntimeException
    {
		return (I_C_Project)MTable.get(getCtx(), I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Project_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
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

	public I_C_ProjectSchedule getC_ProjectSchedule() throws RuntimeException
    {
		return (I_C_ProjectSchedule)MTable.get(getCtx(), I_C_ProjectSchedule.Table_Name)
			.getPO(getC_ProjectSchedule_ID(), get_TrxName());	}

	/** Set C_ProjectSchedule.
		@param C_ProjectSchedule_ID C_ProjectSchedule	  */
	public void setC_ProjectSchedule_ID (int C_ProjectSchedule_ID)
	{
		if (C_ProjectSchedule_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_ProjectSchedule_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_ProjectSchedule_ID, Integer.valueOf(C_ProjectSchedule_ID));
	}

	/** Get C_ProjectSchedule.
		@return C_ProjectSchedule	  */
	public int getC_ProjectSchedule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectSchedule_ID);
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

	public I_DM_Document getDM_Document() throws RuntimeException
    {
		return (I_DM_Document)MTable.get(getCtx(), I_DM_Document.Table_Name)
			.getPO(getDM_Document_ID(), get_TrxName());	}

	/** Set DM_Document.
		@param DM_Document_ID DM_Document	  */
	public void setDM_Document_ID (int DM_Document_ID)
	{
		if (DM_Document_ID < 1) 
			set_Value (COLUMNNAME_DM_Document_ID, null);
		else 
			set_Value (COLUMNNAME_DM_Document_ID, Integer.valueOf(DM_Document_ID));
	}

	/** Get DM_Document.
		@return DM_Document	  */
	public int getDM_Document_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_Document_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Pay_Type AD_Reference_ID=1000077 */
	public static final int PAY_TYPE_AD_Reference_ID=1000077;
	/** Otros = OT */
	public static final String PAY_TYPE_Otros = "OT";
	/** Resolucion = RT */
	public static final String PAY_TYPE_Resolucion = "RT";
	/** Oficio = OF */
	public static final String PAY_TYPE_Oficio = "OF";
	/** Multa = D1 */
	public static final String PAY_TYPE_Multa = "D1";
	/** Devolución de Anticipo = D2 */
	public static final String PAY_TYPE_DevolucionDeAnticipo = "D2";
	/** Factura / Boleta de Honorarios = FA */
	public static final String PAY_TYPE_FacturaBoletaDeHonorarios = "FA";
	/** Retención = R1 */
	public static final String PAY_TYPE_Retencion = "R1";
	/** Retención por Honorarios = RH */
	public static final String PAY_TYPE_RetencionPorHonorarios = "RH";
	/** Anticipo = AT */
	public static final String PAY_TYPE_Anticipo = "AT";
	/** Retencion de Anticipo = RA */
	public static final String PAY_TYPE_RetencionDeAnticipo = "RA";
	/** Devolucion de Retencion = ER */
	public static final String PAY_TYPE_DevolucionDeRetencion = "ER";
	/** Devolucion otras Retenciones = DO */
	public static final String PAY_TYPE_DevolucionOtrasRetenciones = "DO";
	/** Memorándum = ME */
	public static final String PAY_TYPE_Memorudum = "ME";
	/** Resolución Afecta = RQ */
	public static final String PAY_TYPE_ResolucionAfecta = "RQ";
	/** Boleta de Garantía = PB */
	public static final String PAY_TYPE_BoletaDeGarantía = "PB";
	/** Set Pay_Type.
		@param Pay_Type Pay_Type	  */
	public void setPay_Type (String Pay_Type)
	{

		set_Value (COLUMNNAME_Pay_Type, Pay_Type);
	}

	/** Get Pay_Type.
		@return Pay_Type	  */
	public String getPay_Type () 
	{
		return (String)get_Value(COLUMNNAME_Pay_Type);
	}

	/** Set PM_ProjectPay.
		@param PM_ProjectPay_ID PM_ProjectPay	  */
	public void setPM_ProjectPay_ID (int PM_ProjectPay_ID)
	{
		if (PM_ProjectPay_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PM_ProjectPay_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PM_ProjectPay_ID, Integer.valueOf(PM_ProjectPay_ID));
	}

	/** Get PM_ProjectPay.
		@return PM_ProjectPay	  */
	public int getPM_ProjectPay_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PM_ProjectPay_ID);
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

	/** Set ReferenceAmount.
		@param ReferenceAmount ReferenceAmount	  */
	public void setReferenceAmount (BigDecimal ReferenceAmount)
	{
		set_Value (COLUMNNAME_ReferenceAmount, ReferenceAmount);
	}

	/** Get ReferenceAmount.
		@return ReferenceAmount	  */
	public BigDecimal getReferenceAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ReferenceAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}