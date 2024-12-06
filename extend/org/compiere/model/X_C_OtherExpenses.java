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

/** Generated Model for C_OtherExpenses
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_C_OtherExpenses extends PO implements I_C_OtherExpenses, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160323L;

    /** Standard Constructor */
    public X_C_OtherExpenses (Properties ctx, int C_OtherExpenses_ID, String trxName)
    {
      super (ctx, C_OtherExpenses_ID, trxName);
      /** if (C_OtherExpenses_ID == 0)
        {
			setC_BPartner_ID (0);
			setC_BPartner_Location_ID (0);
			setc_bpartner2_id (0);
			setC_OtherExpenses_ID (0);
// @#AD_Org_ID@
			setDatePromised (new Timestamp( System.currentTimeMillis() ));
			setExpense (null);
			sethoras (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_C_OtherExpenses (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_OtherExpenses[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	public I_C_Bank getC_Bank() throws RuntimeException
    {
		return (I_C_Bank)MTable.get(getCtx(), I_C_Bank.Table_Name)
			.getPO(getC_Bank_ID(), get_TrxName());	}

	/** Set Bank.
		@param C_Bank_ID 
		Bank
	  */
	public void setC_Bank_ID (int C_Bank_ID)
	{
		if (C_Bank_ID < 1) 
			set_Value (COLUMNNAME_C_Bank_ID, null);
		else 
			set_Value (COLUMNNAME_C_Bank_ID, Integer.valueOf(C_Bank_ID));
	}

	/** Get Bank.
		@return Bank
	  */
	public int getC_Bank_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Bank_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BankAccount getC_BankAccount() throws RuntimeException
    {
		return (I_C_BankAccount)MTable.get(getCtx(), I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccount_ID(), get_TrxName());	}

	/** Set Bank Account.
		@param C_BankAccount_ID 
		Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID < 1) 
			set_Value (COLUMNNAME_C_BankAccount_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankAccount_ID, Integer.valueOf(C_BankAccount_ID));
	}

	/** Get Bank Account.
		@return Account at the Bank
	  */
	public int getC_BankAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccount_ID);
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

	public I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException
    {
		return (I_C_BPartner_Location)MTable.get(getCtx(), I_C_BPartner_Location.Table_Name)
			.getPO(getC_BPartner_Location_ID(), get_TrxName());	}

	/** Set Partner Location.
		@param C_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
	{
		if (C_BPartner_Location_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, Integer.valueOf(C_BPartner_Location_ID));
	}

	/** Get Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getc_bpartner2() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getc_bpartner2_id(), get_TrxName());	}

	/** Set Business Partner 2.
		@param c_bpartner2_id Business Partner 2	  */
	public void setc_bpartner2_id (int c_bpartner2_id)
	{
		set_Value (COLUMNNAME_c_bpartner2_id, Integer.valueOf(c_bpartner2_id));
	}

	/** Get Business Partner 2.
		@return Business Partner 2	  */
	public int getc_bpartner2_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_c_bpartner2_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_OtherExpenses.
		@param C_OtherExpenses_ID C_OtherExpenses	  */
	public void setC_OtherExpenses_ID (int C_OtherExpenses_ID)
	{
		if (C_OtherExpenses_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_OtherExpenses_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_OtherExpenses_ID, Integer.valueOf(C_OtherExpenses_ID));
	}

	/** Get C_OtherExpenses.
		@return C_OtherExpenses	  */
	public int getC_OtherExpenses_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OtherExpenses_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Category AD_Reference_ID=1000083 */
	public static final int CATEGORY_AD_Reference_ID=1000083;
	/** Proveedor = 01 */
	public static final String CATEGORY_Proveedor = "01";
	/** Remuneración = 02 */
	public static final String CATEGORY_Remuneracion = "02";
	/** Set Category.
		@param Category Category	  */
	public void setCategory (String Category)
	{

		set_Value (COLUMNNAME_Category, Category);
	}

	/** Get Category.
		@return Category	  */
	public String getCategory () 
	{
		return (String)get_Value(COLUMNNAME_Category);
	}

	/** Concept AD_Reference_ID=1000082 */
	public static final int CONCEPT_AD_Reference_ID=1000082;
	/** Viático = 01 */
	public static final String CONCEPT_Viatico = "01";
	/** Servicios Básicos = 02 */
	public static final String CONCEPT_ServiciosBasicos = "02";
	/** Arriendo = 03 */
	public static final String CONCEPT_Arriendo = "03";
	/** Set Concepto.
		@param Concept Concepto	  */
	public void setConcept (String Concept)
	{

		set_Value (COLUMNNAME_Concept, Concept);
	}

	/** Get Concepto.
		@return Concepto	  */
	public String getConcept () 
	{
		return (String)get_Value(COLUMNNAME_Concept);
	}

	/** Set currency_1.
		@param currency_1 currency_1	  */
	public void setcurrency_1 (BigDecimal currency_1)
	{
		set_Value (COLUMNNAME_currency_1, currency_1);
	}

	/** Get currency_1.
		@return currency_1	  */
	public BigDecimal getcurrency_1 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_1);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currency_10.
		@param currency_10 currency_10	  */
	public void setcurrency_10 (BigDecimal currency_10)
	{
		set_Value (COLUMNNAME_currency_10, currency_10);
	}

	/** Get currency_10.
		@return currency_10	  */
	public BigDecimal getcurrency_10 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_10);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currency_100.
		@param currency_100 currency_100	  */
	public void setcurrency_100 (BigDecimal currency_100)
	{
		set_Value (COLUMNNAME_currency_100, currency_100);
	}

	/** Get currency_100.
		@return currency_100	  */
	public BigDecimal getcurrency_100 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_100);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currency_1000.
		@param currency_1000 currency_1000	  */
	public void setcurrency_1000 (BigDecimal currency_1000)
	{
		set_Value (COLUMNNAME_currency_1000, currency_1000);
	}

	/** Get currency_1000.
		@return currency_1000	  */
	public BigDecimal getcurrency_1000 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_1000);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currency_10000.
		@param currency_10000 currency_10000	  */
	public void setcurrency_10000 (BigDecimal currency_10000)
	{
		set_Value (COLUMNNAME_currency_10000, currency_10000);
	}

	/** Get currency_10000.
		@return currency_10000	  */
	public BigDecimal getcurrency_10000 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_10000);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currency_2000.
		@param currency_2000 currency_2000	  */
	public void setcurrency_2000 (BigDecimal currency_2000)
	{
		set_Value (COLUMNNAME_currency_2000, currency_2000);
	}

	/** Get currency_2000.
		@return currency_2000	  */
	public BigDecimal getcurrency_2000 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_2000);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currency_20000.
		@param currency_20000 currency_20000	  */
	public void setcurrency_20000 (BigDecimal currency_20000)
	{
		set_Value (COLUMNNAME_currency_20000, currency_20000);
	}

	/** Get currency_20000.
		@return currency_20000	  */
	public BigDecimal getcurrency_20000 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_20000);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currency_5.
		@param currency_5 currency_5	  */
	public void setcurrency_5 (BigDecimal currency_5)
	{
		set_Value (COLUMNNAME_currency_5, currency_5);
	}

	/** Get currency_5.
		@return currency_5	  */
	public BigDecimal getcurrency_5 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_5);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currency_50.
		@param currency_50 currency_50	  */
	public void setcurrency_50 (BigDecimal currency_50)
	{
		set_Value (COLUMNNAME_currency_50, currency_50);
	}

	/** Get currency_50.
		@return currency_50	  */
	public BigDecimal getcurrency_50 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_50);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currency_500.
		@param currency_500 currency_500	  */
	public void setcurrency_500 (BigDecimal currency_500)
	{
		set_Value (COLUMNNAME_currency_500, currency_500);
	}

	/** Get currency_500.
		@return currency_500	  */
	public BigDecimal getcurrency_500 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_500);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set currency_5000.
		@param currency_5000 currency_5000	  */
	public void setcurrency_5000 (BigDecimal currency_5000)
	{
		set_Value (COLUMNNAME_currency_5000, currency_5000);
	}

	/** Get currency_5000.
		@return currency_5000	  */
	public BigDecimal getcurrency_5000 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currency_5000);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Date Promised.
		@param DatePromised 
		Date Order was promised
	  */
	public void setDatePromised (Timestamp DatePromised)
	{
		set_Value (COLUMNNAME_DatePromised, DatePromised);
	}

	/** Get Date Promised.
		@return Date Order was promised
	  */
	public Timestamp getDatePromised () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DatePromised);
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

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** <None> = -- */
	public static final String DOCACTION_None = "--";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Review = L1 */
	public static final String DOCACTION_Review = "L1";
	/** Validate = L2 */
	public static final String DOCACTION_Validate = "L2";
	/** Set Document Action.
		@param DocAction 
		The targeted status of the document
	  */
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction () 
	{
		return (String)get_Value(COLUMNNAME_DocAction);
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
	/** En Revisión = L1 */
	public static final String DOCSTATUS_EnRevisionL1 = "L1";
	/** En Validación = L2 */
	public static final String DOCSTATUS_EnValidacionL2 = "L2";
	/** 40 Carga Pedido = L3 */
	public static final String DOCSTATUS_40CargaPedido = "L3";
	/** En Revision = 20 */
	public static final String DOCSTATUS_EnRevision = "20";
	/** En Validacion = 30 */
	public static final String DOCSTATUS_EnValidacion = "30";
	/** Solicitud Ingresada = 10 */
	public static final String DOCSTATUS_SolicitudIngresada = "10";
	/** Solicitud Aprobada = 40 */
	public static final String DOCSTATUS_SolicitudAprobada = "40";
	/** Devuelto por Revisión = 15 */
	public static final String DOCSTATUS_DevueltoPorRevision = "15";
	/** Devuelto por Validación = 25 */
	public static final String DOCSTATUS_DevueltoPorValidacion = "25";
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

	/** Set DocumentDate.
		@param DocumentDate DocumentDate	  */
	public void setDocumentDate (Timestamp DocumentDate)
	{
		set_Value (COLUMNNAME_DocumentDate, DocumentDate);
	}

	/** Get DocumentDate.
		@return DocumentDate	  */
	public Timestamp getDocumentDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DocumentDate);
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

	/** Expense AD_Reference_ID=1000081 */
	public static final int EXPENSE_AD_Reference_ID=1000081;
	/** Remesa = RE */
	public static final String EXPENSE_Remesa = "RE";
	/** Pago Excepcional = PE */
	public static final String EXPENSE_PagoExcepcional = "PE";
	/** Set Expense.
		@param Expense Expense	  */
	public void setExpense (String Expense)
	{

		set_Value (COLUMNNAME_Expense, Expense);
	}

	/** Get Expense.
		@return Expense	  */
	public String getExpense () 
	{
		return (String)get_Value(COLUMNNAME_Expense);
	}

	/** Set Grand Total.
		@param GrandTotal 
		Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_Value (COLUMNNAME_GrandTotal, GrandTotal);
	}

	/** Get Grand Total.
		@return Total amount of document
	  */
	public BigDecimal getGrandTotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrandTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set horas.
		@param horas horas	  */
	public void sethoras (Timestamp horas)
	{
		set_Value (COLUMNNAME_horas, horas);
	}

	/** Get horas.
		@return horas	  */
	public Timestamp gethoras () 
	{
		return (Timestamp)get_Value(COLUMNNAME_horas);
	}

	/** Set Override.
		@param Override Override	  */
	public void setOverride (boolean Override)
	{
		set_Value (COLUMNNAME_Override, Boolean.valueOf(Override));
	}

	/** Get Override.
		@return Override	  */
	public boolean isOverride () 
	{
		Object oo = get_Value(COLUMNNAME_Override);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** TenderType AD_Reference_ID=214 */
	public static final int TENDERTYPE_AD_Reference_ID=214;
	/** Credit Card = C */
	public static final String TENDERTYPE_CreditCard = "C";
	/** Check = K */
	public static final String TENDERTYPE_Check = "K";
	/** Direct Deposit = A */
	public static final String TENDERTYPE_DirectDeposit = "A";
	/** Direct Debit = D */
	public static final String TENDERTYPE_DirectDebit = "D";
	/** Account = T */
	public static final String TENDERTYPE_Account = "T";
	/** Cash = X */
	public static final String TENDERTYPE_Cash = "X";
	/** Carta de credito = Z */
	public static final String TENDERTYPE_CartaDeCredito = "Z";
	/** Pagare = P */
	public static final String TENDERTYPE_Pagare = "P";
	/** Vale Vista = V */
	public static final String TENDERTYPE_ValeVista = "V";
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

	/** Set Total Lines.
		@param TotalLines 
		Total of all document lines
	  */
	public void setTotalLines (BigDecimal TotalLines)
	{
		throw new IllegalArgumentException ("TotalLines is virtual column");	}

	/** Get Total Lines.
		@return Total of all document lines
	  */
	public BigDecimal getTotalLines () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalLines);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}