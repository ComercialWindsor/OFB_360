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

/** Generated Model for DM_PerformanceBond_Proc
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_DM_PerformanceBond_Proc extends PO implements I_DM_PerformanceBond_Proc, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150112L;

    /** Standard Constructor */
    public X_DM_PerformanceBond_Proc (Properties ctx, int DM_PerformanceBond_Proc_ID, String trxName)
    {
      super (ctx, DM_PerformanceBond_Proc_ID, trxName);
      /** if (DM_PerformanceBond_Proc_ID == 0)
        {
			setAmt (Env.ZERO);
			setC_Currency_ID (0);
			setC_DocType_ID (0);
			setDM_PerformanceBond_Proc_ID (0);
			setDocStatus (null);
// DR
			setPosted (false);
// N
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_DM_PerformanceBond_Proc (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_DM_PerformanceBond_Proc[")
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

	/** BondCondition AD_Reference_ID=1000114 */
	public static final int BONDCONDITION_AD_Reference_ID=1000114;
	/** Credito Contingente = CC1 */
	public static final String BONDCONDITION_CreditoContingente = "CC1";
	/** Efectivo = CC2 */
	public static final String BONDCONDITION_Efectivo = "CC2";
	/** Set BondCondition.
		@param BondCondition 
		Bond Condition
	  */
	public void setBondCondition (String BondCondition)
	{

		set_Value (COLUMNNAME_BondCondition, BondCondition);
	}

	/** Get BondCondition.
		@return Bond Condition
	  */
	public String getBondCondition () 
	{
		return (String)get_Value(COLUMNNAME_BondCondition);
	}

	/** BondType AD_Reference_ID=1000113 */
	public static final int BONDTYPE_AD_Reference_ID=1000113;
	/** A la vista = BB1 */
	public static final String BONDTYPE_ALaVista = "BB1";
	/** 30 Días = BB2 */
	public static final String BONDTYPE_30Días = "BB2";
	/** Set BondType.
		@param BondType 
		Type of Bond
	  */
	public void setBondType (String BondType)
	{

		set_Value (COLUMNNAME_BondType, BondType);
	}

	/** Get BondType.
		@return Type of Bond
	  */
	public String getBondType () 
	{
		return (String)get_Value(COLUMNNAME_BondType);
	}

	/** Set BtnProcess.
		@param BtnProcess BtnProcess	  */
	public void setBtnProcess (String BtnProcess)
	{
		set_Value (COLUMNNAME_BtnProcess, BtnProcess);
	}

	/** Get BtnProcess.
		@return BtnProcess	  */
	public String getBtnProcess () 
	{
		return (String)get_Value(COLUMNNAME_BtnProcess);
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

	public I_C_Bank getC_BankRef() throws RuntimeException
    {
		return (I_C_Bank)MTable.get(getCtx(), I_C_Bank.Table_Name)
			.getPO(getC_BankRef_ID(), get_TrxName());	}

	/** Set C_BankRef_ID.
		@param C_BankRef_ID C_BankRef_ID	  */
	public void setC_BankRef_ID (int C_BankRef_ID)
	{
		if (C_BankRef_ID < 1) 
			set_Value (COLUMNNAME_C_BankRef_ID, null);
		else 
			set_Value (COLUMNNAME_C_BankRef_ID, Integer.valueOf(C_BankRef_ID));
	}

	/** Get C_BankRef_ID.
		@return C_BankRef_ID	  */
	public int getC_BankRef_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankRef_ID);
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

	public I_C_Currency getC_Currency() throws RuntimeException
    {
		return (I_C_Currency)MTable.get(getCtx(), I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
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

	/** Set CollectionReason.
		@param CollectionReason CollectionReason	  */
	public void setCollectionReason (String CollectionReason)
	{
		set_Value (COLUMNNAME_CollectionReason, CollectionReason);
	}

	/** Get CollectionReason.
		@return CollectionReason	  */
	public String getCollectionReason () 
	{
		return (String)get_Value(COLUMNNAME_CollectionReason);
	}

	/** Set correlative.
		@param correlative correlative	  */
	public void setcorrelative (int correlative)
	{
		set_Value (COLUMNNAME_correlative, Integer.valueOf(correlative));
	}

	/** Get correlative.
		@return correlative	  */
	public int getcorrelative () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_correlative);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		set_Value (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
	}

	/** Set DateAcctRef.
		@param DateAcctRef DateAcctRef	  */
	public void setDateAcctRef (Timestamp DateAcctRef)
	{
		set_Value (COLUMNNAME_DateAcctRef, DateAcctRef);
	}

	/** Get DateAcctRef.
		@return DateAcctRef	  */
	public Timestamp getDateAcctRef () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcctRef);
	}

	/** Set DateCollection.
		@param DateCollection DateCollection	  */
	public void setDateCollection (Timestamp DateCollection)
	{
		set_Value (COLUMNNAME_DateCollection, DateCollection);
	}

	/** Get DateCollection.
		@return DateCollection	  */
	public Timestamp getDateCollection () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateCollection);
	}

	/** Set DateMoneyReception.
		@param DateMoneyReception DateMoneyReception	  */
	public void setDateMoneyReception (Timestamp DateMoneyReception)
	{
		set_Value (COLUMNNAME_DateMoneyReception, DateMoneyReception);
	}

	/** Get DateMoneyReception.
		@return DateMoneyReception	  */
	public Timestamp getDateMoneyReception () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateMoneyReception);
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

	/** Set DateTrxRef.
		@param DateTrxRef DateTrxRef	  */
	public void setDateTrxRef (Timestamp DateTrxRef)
	{
		set_Value (COLUMNNAME_DateTrxRef, DateTrxRef);
	}

	/** Get DateTrxRef.
		@return DateTrxRef	  */
	public Timestamp getDateTrxRef () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrxRef);
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

	public I_DM_PerformanceBond getDM_PerformanceBond() throws RuntimeException
    {
		return (I_DM_PerformanceBond)MTable.get(getCtx(), I_DM_PerformanceBond.Table_Name)
			.getPO(getDM_PerformanceBond_ID(), get_TrxName());	}

	/** Set DM_PerformanceBond_ID.
		@param DM_PerformanceBond_ID DM_PerformanceBond_ID	  */
	public void setDM_PerformanceBond_ID (int DM_PerformanceBond_ID)
	{
		if (DM_PerformanceBond_ID < 1) 
			set_Value (COLUMNNAME_DM_PerformanceBond_ID, null);
		else 
			set_Value (COLUMNNAME_DM_PerformanceBond_ID, Integer.valueOf(DM_PerformanceBond_ID));
	}

	/** Get DM_PerformanceBond_ID.
		@return DM_PerformanceBond_ID	  */
	public int getDM_PerformanceBond_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_PerformanceBond_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DM_PerformanceBond_Proc.
		@param DM_PerformanceBond_Proc_ID DM_PerformanceBond_Proc	  */
	public void setDM_PerformanceBond_Proc_ID (int DM_PerformanceBond_Proc_ID)
	{
		if (DM_PerformanceBond_Proc_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DM_PerformanceBond_Proc_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DM_PerformanceBond_Proc_ID, Integer.valueOf(DM_PerformanceBond_Proc_ID));
	}

	/** Get DM_PerformanceBond_Proc.
		@return DM_PerformanceBond_Proc	  */
	public int getDM_PerformanceBond_Proc_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_PerformanceBond_Proc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_DM_PerformanceBond getDM_PerformanceBondRef() throws RuntimeException
    {
		return (I_DM_PerformanceBond)MTable.get(getCtx(), I_DM_PerformanceBond.Table_Name)
			.getPO(getDM_PerformanceBondRef_ID(), get_TrxName());	}

	/** Set DM_PerformanceBondRef_ID.
		@param DM_PerformanceBondRef_ID DM_PerformanceBondRef_ID	  */
	public void setDM_PerformanceBondRef_ID (int DM_PerformanceBondRef_ID)
	{
		if (DM_PerformanceBondRef_ID < 1) 
			set_Value (COLUMNNAME_DM_PerformanceBondRef_ID, null);
		else 
			set_Value (COLUMNNAME_DM_PerformanceBondRef_ID, Integer.valueOf(DM_PerformanceBondRef_ID));
	}

	/** Get DM_PerformanceBondRef_ID.
		@return DM_PerformanceBondRef_ID	  */
	public int getDM_PerformanceBondRef_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_PerformanceBondRef_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** DocStatus AD_Reference_ID=1000119 */
	public static final int DOCSTATUS_AD_Reference_ID=1000119;
	/** Borrador = DR */
	public static final String DOCSTATUS_Borrador = "DR";
	/** Completo = CO */
	public static final String DOCSTATUS_Completo = "CO";
	/** Custodia Sin Registro = SR */
	public static final String DOCSTATUS_CustodiaSinRegistro = "SR";
	/** Alta Solicitada = AS */
	public static final String DOCSTATUS_AltaSolicitada = "AS";
	/** Alta Aprobada = AA */
	public static final String DOCSTATUS_AltaAprobada = "AA";
	/** Alta Visada = AV */
	public static final String DOCSTATUS_AltaVisada = "AV";
	/** Baja Solicitada = BS */
	public static final String DOCSTATUS_BajaSolicitada = "BS";
	/** Baja Aprobada = BA */
	public static final String DOCSTATUS_BajaAprobada = "BA";
	/** Cobro Solicitado = CS */
	public static final String DOCSTATUS_CobroSolicitado = "CS";
	/** Cobro Aprobado = CA */
	public static final String DOCSTATUS_CobroAprobado = "CA";
	/** Cobro Visado = CV */
	public static final String DOCSTATUS_CobroVisado = "CV";
	/** Baja Visada = BV */
	public static final String DOCSTATUS_BajaVisada = "BV";
	/** Documento Cobrado = DC */
	public static final String DOCSTATUS_DocumentoCobrado = "DC";
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

	/** Set DocumentRefNo.
		@param DocumentRefNo DocumentRefNo	  */
	public void setDocumentRefNo (String DocumentRefNo)
	{
		set_Value (COLUMNNAME_DocumentRefNo, DocumentRefNo);
	}

	/** Get DocumentRefNo.
		@return DocumentRefNo	  */
	public String getDocumentRefNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentRefNo);
	}

	/** Guarantee AD_Reference_ID=1000115 */
	public static final int GUARANTEE_AD_Reference_ID=1000115;
	/** Advance = ADV */
	public static final String GUARANTEE_Advance = "ADV";
	/** Exchange Retention = CAN */
	public static final String GUARANTEE_ExchangeRetention = "CAN";
	/** Efficient Execution = EFF */
	public static final String GUARANTEE_EfficientExecution = "EFF";
	/** Faithful Performance = FAI */
	public static final String GUARANTEE_FaithfulPerformance = "FAI";
	/** Offer Seriousness = OFF */
	public static final String GUARANTEE_OfferSeriousness = "OFF";
	/** Green Areas = GRE */
	public static final String GUARANTEE_GreenAreas = "GRE";
	/** Temporary Spurt = TEM */
	public static final String GUARANTEE_TemporarySpurt = "TEM";
	/** Authorized installers = AUT */
	public static final String GUARANTEE_AuthorizedInstallers = "AUT";
	/** Urbanization = URB */
	public static final String GUARANTEE_Urbanization = "URB";
	/** Building Permit = BUI */
	public static final String GUARANTEE_BuildingPermit = "BUI";
	/** Consumptions and low = CON */
	public static final String GUARANTEE_ConsumptionsAndLow = "CON";
	/** Others = OTH */
	public static final String GUARANTEE_Others = "OTH";
	/** Set Guarantee.
		@param Guarantee 
		Type of Guarantee
	  */
	public void setGuarantee (String Guarantee)
	{

		set_Value (COLUMNNAME_Guarantee, Guarantee);
	}

	/** Get Guarantee.
		@return Type of Guarantee
	  */
	public String getGuarantee () 
	{
		return (String)get_Value(COLUMNNAME_Guarantee);
	}

	/** LowReason AD_Reference_ID=1000120 */
	public static final int LOWREASON_AD_Reference_ID=1000120;
	/** Termino de Obra o Servicio = TO */
	public static final String LOWREASON_TerminoDeObraOServicio = "TO";
	/** Término de Contrato = TC */
	public static final String LOWREASON_TerminoDeContrato = "TC";
	/** Set LowReason.
		@param LowReason LowReason	  */
	public void setLowReason (String LowReason)
	{

		set_Value (COLUMNNAME_LowReason, LowReason);
	}

	/** Get LowReason.
		@return LowReason	  */
	public String getLowReason () 
	{
		return (String)get_Value(COLUMNNAME_LowReason);
	}

	/** Set OfbButton.
		@param OfbButton OfbButton	  */
	public void setOfbButton (String OfbButton)
	{
		set_Value (COLUMNNAME_OfbButton, OfbButton);
	}

	/** Get OfbButton.
		@return OfbButton	  */
	public String getOfbButton () 
	{
		return (String)get_Value(COLUMNNAME_OfbButton);
	}

	/** Set Posted.
		@param Posted 
		Posting status
	  */
	public void setPosted (boolean Posted)
	{
		set_Value (COLUMNNAME_Posted, Boolean.valueOf(Posted));
	}

	/** Get Posted.
		@return Posting status
	  */
	public boolean isPosted () 
	{
		Object oo = get_Value(COLUMNNAME_Posted);
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

	/** Type AD_Reference_ID=1000117 */
	public static final int TYPE_AD_Reference_ID=1000117;
	/** Alta Nueva = AN */
	public static final String TYPE_AltaNueva = "AN";
	/** Alta Por Reemplazo = AR */
	public static final String TYPE_AltaPorReemplazo = "AR";
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