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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;


/**
 *	Payment Request Line Model
 *
 *  @author Italo Niñoles
 *  @version 	$Id: MPaymentRequest.java,v 1.4 2014/07/05 10:40:00 ininoles Exp $ * 
 * 	@author Italo Niñoles - OFBConsulting New 
 */
public class MPaymentRequestLine extends X_C_PaymentRequestLine
{		
	/**************************************************************************
	 * 	Payment Request Line Constructor
	 * 	@param ctx context
	 * 	@param trxName transaction name
	 */
	public MPaymentRequestLine (Properties ctx, int C_InvoiceLine_ID, String trxName)
	{
		super (ctx, C_InvoiceLine_ID, trxName);
		if (C_InvoiceLine_ID == 0)
		{	
			setAmt(Env.ZERO);
			//
		}
	}	//	CP Line

	/**
	 * 	Parent Constructor
	 * 	@param invoice parent
	 */
	public MPaymentRequestLine (MPaymentRequest paymentRequest)
	{
		this (paymentRequest.getCtx(), 0, paymentRequest.get_TrxName());
		if (paymentRequest.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setClientOrg(paymentRequest.getAD_Client_ID(), paymentRequest.getAD_Org_ID());
		setC_PaymentRequest_ID(paymentRequest.getC_PaymentRequest_ID());
	}	//	MInvoiceLine


	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
	public MPaymentRequestLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInvoiceLine
	
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MPaymentRequestLine[")
			.append(get_ID()).append(",")
			.append(",Amt=").append(getAmt())
			.append(",C_Invoice_ID=").append(getC_Invoice_ID())
			.append(",GL_JournalLine_ID=").append(getGL_JournalLine_ID())
			.append(",C_InvoicePaySchedule_ID=").append(getC_InvoicePaySchedule_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString	

	/**
	 * 	Get Description Text.
	 * 	For jsp access (vs. isDescription)
	 *	@return description
	 */
	public String getDescriptionText()
	{
		return super.getDescription();
	}	//	getDescriptionText
	
	/**
	 * 	Get Amt.	  	
	 *	@return Amt
	 */
	public BigDecimal getAmt()
	{
		return super.getAmt();
	}	//	getAmt
	
	/**
	 * 	Get Invoice.	  	
	 *	@return C_Invoice_ID
	 */
	public int getC_Invoice_ID()
	{
		return super.getC_Invoice_ID();
	}	//	get Invoice
	
	/**
	 * 	Get Journal Line.	  	
	 *	@return GL_JournalLine_ID
	 */	
	public int getGL_JournalLine_ID()
	{
		return super.getGL_JournalLine_ID();
	}	//	get JournalLine


	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if save
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//update header amt
		BigDecimal total = DB.getSQLValueBD(get_TrxName(), "select sum(amt) from C_PaymentRequestLine where isactive='Y' and C_PaymentRequest_ID="+ getC_PaymentRequest_ID());
		
		X_C_PaymentRequest hr = new X_C_PaymentRequest(getCtx(), getC_PaymentRequest_ID(), get_TrxName());
		hr.setPayAmt(total==null?Env.ZERO:total );
		hr.save();
		
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		return true;
	}	//	afterSave

	/**
	 * 	After Delete
	 *	@param success success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		return true;
	}	//	afterDelete
}	//	MInvoiceLine
