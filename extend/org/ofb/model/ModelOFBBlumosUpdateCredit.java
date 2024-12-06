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
package org.ofb.model;

import java.math.BigDecimal;

import org.compiere.model.MBPartner;
import org.compiere.model.MBankStatement;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MClient;
import org.compiere.model.MConversionRate;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

/**
 *	
 */
public class ModelOFBBlumosUpdateCredit implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBBlumosUpdateCredit ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBBlumosUpdateCredit.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;
	

	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing global validator: "+this.toString());
		}

		//	Tables to be monitored
		//	Documents to be monitored
		engine.addDocValidate(MPayment.Table_Name, this);		
		engine.addDocValidate(MBankStatement.Table_Name, this);

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
					
		return null;
	}	//	modelChange

	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MPayment.Table_ID)
		{	
			MPayment pay = (MPayment) po;
			BigDecimal balance = pay.getC_BPartner().getTotalOpenBalance();
			MBPartner bPartner = new MBPartner(po.getCtx(), pay.getC_BPartner_ID(), po.get_TrxName());
			
			BigDecimal payAmt = MConversionRate.convertBase(po.getCtx(), pay.getPayAmt(), 
					pay.getC_Currency_ID(), pay.getDateAcct(), pay.getC_ConversionType_ID(), 
					pay.getAD_Client_ID(), pay.getAD_Org_ID());
			if (payAmt == null)
				payAmt = Env.ZERO;
			if (balance == null)
				balance = Env.ZERO;
			if (pay.isReceipt())
				balance = balance.add(payAmt);
			else
				balance = balance.subtract(payAmt);
			bPartner.setTotalOpenBalance(balance);
			bPartner.save();
		}
		if(timing == TIMING_AFTER_VOID && po.get_Table_ID()==MPayment.Table_ID)
		{	
			MPayment pay = (MPayment) po;
			BigDecimal balance = pay.getC_BPartner().getTotalOpenBalance();
			MBPartner bPartner = new MBPartner(po.getCtx(), pay.getC_BPartner_ID(), po.get_TrxName());
			
			BigDecimal payAmt = MConversionRate.convertBase(po.getCtx(), pay.getPayAmt(), 
					pay.getC_Currency_ID(), pay.getDateAcct(), pay.getC_ConversionType_ID(), 
					pay.getAD_Client_ID(), pay.getAD_Org_ID());
			if (payAmt == null)
				payAmt = Env.ZERO;
			if (balance == null)
				balance = Env.ZERO;
			if (pay.isReceipt())
				balance = balance.subtract(payAmt);
			else
				balance = balance.add(payAmt);
			bPartner.setTotalOpenBalance(balance);
			bPartner.save();
		}
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MBankStatement.Table_ID)
		{	
			MBankStatement bStatement = (MBankStatement) po;
			MBankStatementLine[] lines = bStatement.getLines(true);			
			for (int i = 0; i < lines.length; i++)
			{
				MBankStatementLine line = lines[i];
				if(line.getC_Payment_ID() > 0)
				{
					MPayment pay = new MPayment(po.getCtx(), line.getC_Payment_ID(), po.get_TrxName());
					MBPartner bPartner = new MBPartner(po.getCtx(), pay.getC_BPartner_ID(), po.get_TrxName());
					BigDecimal balance = pay.getC_BPartner().getTotalOpenBalance();
					BigDecimal payAmt = MConversionRate.convertBase(po.getCtx(), line.getStmtAmt().abs(), 
							pay.getC_Currency_ID(), pay.getDateAcct(), pay.getC_ConversionType_ID(), 
							pay.getAD_Client_ID(), pay.getAD_Org_ID());
					if (payAmt == null)
						payAmt = Env.ZERO;
					if (balance == null)
						balance = Env.ZERO;
					if (pay.isReceipt())
						balance = balance.subtract(payAmt);
					else
						balance = balance.add(payAmt);
					bPartner.setTotalOpenBalance(balance);
					bPartner.save();
				}
			}
		}
		if(timing == TIMING_BEFORE_VOID && po.get_Table_ID()==MBankStatement.Table_ID)
		{	
			MBankStatement bStatement = (MBankStatement) po;
			MBankStatementLine[] lines = bStatement.getLines(true);			
			for (int i = 0; i < lines.length; i++)
			{
				MBankStatementLine line = lines[i];
				if(line.getC_Payment_ID() > 0)
				{
					MPayment pay = new MPayment(po.getCtx(), line.getC_Payment_ID(), po.get_TrxName());
					MBPartner bPartner = new MBPartner(po.getCtx(), pay.getC_BPartner_ID(), po.get_TrxName());
					BigDecimal balance = pay.getC_BPartner().getTotalOpenBalance();
					BigDecimal payAmt = MConversionRate.convertBase(po.getCtx(), line.getStmtAmt().abs(), 
							pay.getC_Currency_ID(), pay.getDateAcct(), pay.getC_ConversionType_ID(), 
							pay.getAD_Client_ID(), pay.getAD_Org_ID());
					if (payAmt == null)
						payAmt = Env.ZERO;
					if (balance == null)
						balance = Env.ZERO;
					if (pay.isReceipt())
						balance = balance.add(payAmt);
					else
						balance = balance.subtract(payAmt);
					bPartner.setTotalOpenBalance(balance);
					bPartner.save();
				}
			}
		}
		return null;
	}	//	docValidate

	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString


	

}	