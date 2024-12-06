/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;


/**
 *  @author Italo Niñoles - OFBConsulting New 
 *  @version 	$Id: MPaymentRequest.java,v 1.4 2014/07/05 10:40:00 ininoles Exp $
 */
public final class MPaymentRequest extends X_C_PaymentRequest 
	implements DocAction
{
    
	/**
	 * 	Get Payments Of BPartner
	 *	@param ctx context
	 *	@param C_BPartner_ID id
	 *	@param trxName transaction
	 *	@return array
	 */
	public static MPaymentRequest[] getOfBPartner (Properties ctx, int C_BPartner_ID, String trxName)
	{
		//FR: [ 2214883 ] Remove SQL code and Replace for Query - red1
		final String whereClause = "C_BPartner_ID=?";
		List <MPaymentRequest> list = new Query(ctx, I_C_Payment.Table_Name, whereClause, trxName)
		.setParameters(C_BPartner_ID)
		.list();

		//
		MPaymentRequest[] retValue = new MPaymentRequest[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getOfBPartner
	
	
	/**************************************************************************
	 *  Default Constructor
	 *  @param ctx context
	 *  @param  C_Payment_ID    payment to load, (0 create new payment)
	 *  @param trxName trx name
	 */
	public MPaymentRequest (Properties ctx, int C_Payment_ID, String trxName)
	{
		super (ctx, C_Payment_ID, trxName);
		//  New
		if (C_Payment_ID == 0)
		{
			setDocAction(DOCACTION_Complete);
			setDocStatus(DOCSTATUS_Drafted);
			setProcessed(false);
			setProcessing(false);
			setPayAmt(Env.ZERO);
			setDateTrx (new Timestamp(System.currentTimeMillis()));
			setDateAcct (getDateTrx());
			setTenderType(TENDERTYPE_Check);
		}
	}   //  MPayment
	
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *	@param trxName transaction
	 */
	public MPaymentRequest (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPaymentRequest
	
	/** Error Message						*/
	private String				m_errorMessage = null;
	
	/** Reversal Indicator			*/
	public static String	REVERSE_INDICATOR = "^";
	

	/**************************************************************************
	 * 	Set Error Message
	 *	@param errorMessage error message
	 */
	public void setErrorMessage(String errorMessage)
	{
		m_errorMessage = errorMessage;
	}	//	setErrorMessage

	/**
	 * 	Get Error Message
	 *	@return error message
	 */
	public String getErrorMessage()
	{
		return m_errorMessage;
	}	//	getErrorMessage

	/**
	 *  Set Payment Amount
	 *  @param PayAmt Pay Amt
	 */
	public void setPayAmt (BigDecimal PayAmt)
	{
		super.setPayAmt(PayAmt == null ? Env.ZERO : PayAmt);
	}	//	setPayAmt
	
	/**
	 * 	Get Document Status
	 *	@return Document Status Clear Text
	 */
	public String getDocStatusName()
	{
		return MRefList.getListName(getCtx(), 131, getDocStatus());
	}	//	getDocStatusName

	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription
	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		//if (processAction.equalsIgnoreCase("--"))
			//processAction = "PR";
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	process
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		log.info(toString());
		setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt

	
	/**************************************************************************
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		if (! MPaySelectionCheck.deleteGeneratedDraft(getCtx(), getC_Payment_ID(), get_TrxName())) {
			m_processMsg = "Could not delete draft generated payment selection lines";
			return DocAction.STATUS_Invalid;
		}
		//faaguilar OFB payamt = 0 Begin
		if(getPayAmt().signum()==0)
		{
			m_processMsg = "PayAmt=0";
			return DocAction.STATUS_Invalid;
		}
		//end 		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		if(getLines().length==0 && !getRequestType().equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion) )
			return  "No Lines";
		
		if(getC_BankAccount_ID()<=0)
			return "No Account Bank";		

		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
		
		
		
	}	//	prepareIt
	
	/**************************************************************************
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		
		generatePayment();
		
		for(X_C_PaymentRequestLine line: getLines()){
			line.setProcessed(true);
			line.save();
		}			
		
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;	
		
	}	//	completeIt
	/**
	 * 	Void Document.
	 * 	@return true if success 
	 */
	public boolean voidIt()
	{
		log.info(toString());
		// Before Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
		if (m_processMsg != null)
			return false;
		
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}
		/**
		 * Payment validation
		 */		
		MPayment pay = new MPayment(Env.getCtx(), getC_Payment_ID(), get_TrxName());
		if(pay.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_Completed) || pay.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_InProgress) )
		{
			m_processMsg = "Payment Processed ID:"+pay.get_ID();
			setDocAction(DOCACTION_None);
			return false;
		}
		
		pay.deleteEx(true, get_TrxName());
		setC_Payment_ID(0);		
		save();
		
		if (DOCSTATUS_Drafted.equals(getDocStatus())
				|| DOCSTATUS_Invalid.equals(getDocStatus())
				|| DOCSTATUS_InProgress.equals(getDocStatus())
				|| DOCSTATUS_Approved.equals(getDocStatus())
				|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
				addDescription(Msg.getMsg(getCtx(), "Voided") + " (" + getPayAmt() + ")");
				setPayAmt(Env.ZERO);			
		}
		
		if(getRequestType().equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion ))
		{	
			MProject pj = new  MProject(getCtx(),get_ValueAsInt("C_Project_ID"),get_TrxName() );
			pj.setProjectBalanceAmt(pj.getProjectBalanceAmt().subtract(getPayAmt()));
			pj.save();
		} 
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;
		
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		log.info(toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;
		
		setDocAction(DOCACTION_None);
		
		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;		
		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Accrual - none
	 * 	@return true if success 
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
				
		return false;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt()
	{
		log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;	
		
		if (! reverseCorrectIt())
			return false;

		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;				
		
		return true;
	}	//	reActivateIt
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPayment[");
		sb.append(get_ID()).append("-").append(getDocumentNo())			
			.append(",PayAmt=").append(getPayAmt());
		return sb.toString ();
	}	//	toString
	
	

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.PAYMENT, getC_Payment_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	
	
	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getCreatedBy();
	}	//	getDoc_User_ID


	public String generatePayment()
	{
		String ret = ""; 
		//if(getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_WaitingConfirmation))
		//{	 
			if(getRequestType().equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura) || getRequestType().equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion))
			{
				MPayment pay = new MPayment(Env.getCtx(), 0, get_TrxName());
				pay.setIsReceipt(false);
				pay.setAD_Org_ID(getAD_Org_ID());
				pay.setC_BPartner_ID(getC_BPartner_ID());
				pay.setC_BankAccount_ID(getC_BankAccount_ID());
				pay.setDateTrx(getDateTrx());
				pay.setDateAcct(getDateAcct());
				pay.setC_Currency_ID(getC_Currency_ID());
				pay.setTenderType(getTenderType());
				
				if(getTenderType().equals(X_C_PaymentRequest.TENDERTYPE_Check))
					pay.setCheckNo(getCheckNo());	
				if(getRequestType().equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura))
					pay.setC_Currency_ID(getC_Currency_ID());
					
				if(getC_Charge_ID()>0)
				  pay.setC_Charge_ID(getC_Charge_ID());
				
				pay.setPayAmt(getPayAmt());
				pay.set_CustomColumn("C_PaymentRequest_ID", getC_PaymentRequest_ID());
				pay.save();
								
				log.config("Payment ID :"+pay.getC_Payment_ID() );
				setC_Payment_ID(pay.getC_Payment_ID());
				setProcessed(true);
				save();
				log.config("pr saved");
				
				if(getRequestType().equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura)){
					//create allocations
					MAllocationHdr alloc = new MAllocationHdr (Env.getCtx(), true,	//	manual
							getDateTrx(), getC_Currency_ID(), "Payment Request", get_TrxName());
						alloc.setAD_Org_ID(getAD_Org_ID());
						if (!alloc.save())
						{
							log.log(Level.SEVERE, "Allocation not created");
							return  "Allocation not created";
						}
					
					X_C_PaymentRequestLine lines[]	= getLines();
					for(X_C_PaymentRequestLine  line:lines){
						if(line.getC_Invoice_ID()>0){
							
							MAllocationLine aLine = new MAllocationLine (alloc, line.getAmt().negate(), 
									Env.ZERO, Env.ZERO, Env.ZERO);
							aLine.setC_Invoice_ID(line.getC_Invoice_ID());
							aLine.setC_Payment_ID(pay.getC_Payment_ID());
							if (!aLine.save())
								log.log(Level.SEVERE, "Allocation Line not written ");
						}
					}
				}
			   if(getRequestType().equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion ))
			   {			
				   	MProject pj = new  MProject(getCtx(),get_ValueAsInt("C_Project_ID"),get_TrxName() );
				   	pj.setProjectBalanceAmt(pj.getProjectBalanceAmt().add(getPayAmt()));
				   	pj.save();
			   }
			   ret = "Pago Generado :" + pay.getDocumentNo();
			}
			else
			{				
				int count =0;
				X_C_PaymentRequestLine lines[]	= getLines();				
				for(X_C_PaymentRequestLine  line:lines)
				{
					
					MJournalLine gline = new MJournalLine (getCtx(),line.getGL_JournalLine_ID(),get_TrxName() );
					
					MPayment pay = new MPayment(Env.getCtx(), 0, get_TrxName());
					pay.setIsReceipt(false);
					pay.setAD_Org_ID(getAD_Org_ID());
					pay.setC_BPartner_ID( gline.get_ValueAsInt("C_BPartner_ID") );
					pay.setC_BankAccount_ID(getC_BankAccount_ID());
					pay.setDateTrx(getDateTrx());
					pay.setDateAcct(getDateAcct());
					pay.setC_Currency_ID(getC_Currency_ID());
					pay.setTenderType(getTenderType());
					pay.setDescription(gline.getDescription());
					
					if(getTenderType().equals(X_C_PaymentRequest.TENDERTYPE_Check))
						pay.setCheckNo(getCheckNo());	
					if(getRequestType().equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura))
						pay.setC_Currency_ID(getC_Currency_ID());
					
					if(getC_Charge_ID()>0)
						  pay.setC_Charge_ID(getC_Charge_ID());
						
					pay.setPayAmt(line.getAmt());
					pay.set_CustomColumn("C_PaymentRequest_ID", getC_PaymentRequest_ID());
					pay.save();
					
					count ++;
					
					line.set_ValueOfColumn("C_Payment_ID", pay.getC_Payment_ID());
					line.save();
				}
				ret = "Pagos Generados :"+count;
			}
			
		//}
		return ret;
	}


	@Override
	public boolean approveIt() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean rejectIt() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean reverseCorrectIt() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getDocumentInfo() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public BigDecimal getApprovalAmt() {
		// TODO Auto-generated method stub
		return null;
	}
	


	
}   //  MPayment
