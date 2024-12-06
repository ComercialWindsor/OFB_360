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
package org.baskakow.model;


import java.math.BigDecimal;

import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

/**
 *	Validator for Baskakow
 *
 *  @author italo niñoles
 */
public class ModBaskakowUpdateSO_Credit implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBaskakowUpdateSO_Credit ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBaskakowUpdateSO_Credit.class);
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
			log.info("Initializing Model Price Validator: "+this.toString());
		}

		//	Tables to be monitored	
		engine.addModelChange(MBPartner.Table_Name, this);
		//	Documents to be monitored
		engine.addDocValidate(MInOut.Table_Name, this);
		engine.addDocValidate(MPayment.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Italo Niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID() == MBPartner.Table_ID 
				&& (po.is_ValueChanged("TotalOpenBalanceOFB") || po.is_ValueChanged("SO_CreditUsedOFB")
						|| po.is_ValueChanged("SO_CreditLimit"))) 
		{
			MBPartner bp = (MBPartner)po;
			BigDecimal usedOFB = Env.ZERO;
			if((BigDecimal)bp.get_Value("SO_CreditUsedOFB") == null)
				usedOFB = Env.ZERO;
			else 
				usedOFB = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
			BigDecimal amtOpen = bp.getSO_CreditLimit().subtract(usedOFB);
			//bp.setTotalOpenBalance(amtOpen);			
			bp.setTotalOpenBalance(Env.ZERO);
			bp.setSO_CreditUsed(Env.ZERO);
			bp.set_CustomColumn("TotalOpenBalanceOFB",amtOpen);
			/*if(amtOpen.compareTo(Env.ZERO) > 0)
				bp.setSOCreditStatus("O");
			else
				bp.setSOCreditStatus("H");*/
		}
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{	
		log.info(po.get_TableName() + " Timing: "+timing);				
		//model que actualiza los saldos
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID() == MInOut.Table_ID) 
		{	
			MInOut ship = (MInOut)po;
			MBPartner bp = new MBPartner(po.getCtx(), ship.getC_BPartner_ID(), po.get_TrxName());
				
			//monto lo sacaremos linea x linea
			BigDecimal invAmt = Env.ZERO;			
			MInOutLine[] fromLines = ship.getLines(false);
			for (int i = 0; i < fromLines.length; i++)
			{
				MInOutLine sLine = fromLines[i];
				if(sLine.getC_OrderLine_ID() > 0)
				{
					BigDecimal amtTax = Env.ZERO;
					BigDecimal amtTemp = Env.ZERO;
					amtTemp = sLine.getQtyEntered().multiply(sLine.getC_OrderLine().getPriceEntered());
					amtTax = amtTemp.multiply(sLine.getC_OrderLine().getC_Tax().getRate().divide(Env.ONEHUNDRED));
					amtTemp = amtTemp.add(amtTax);
					amtTemp = amtTemp.setScale(sLine.getC_OrderLine().getC_Order().getC_Currency().getStdPrecision(), BigDecimal.ROUND_HALF_UP);
					invAmt = invAmt.add(amtTemp);					
				}
			}			
			if(invAmt == null)
				invAmt = Env.ZERO;
			BigDecimal usedOFB = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");				
			if(usedOFB == null)
				usedOFB = Env.ZERO;
			if (ship.isSOTrx())			
				usedOFB = usedOFB.add(invAmt);
			else
				usedOFB = usedOFB.subtract(invAmt);
			bp.set_CustomColumn("SO_CreditUsedOFB", usedOFB);
			bp.set_CustomColumn("TotalOpenBalanceOFB", bp.getSO_CreditLimit().subtract(usedOFB));
			bp.setTotalOpenBalance(Env.ZERO);
			bp.setSO_CreditUsed(Env.ZERO);
			bp.save();
		}
		if(timing == TIMING_BEFORE_VOID && po.get_Table_ID() == MInOut.Table_ID) 
		{	
			MInOut ship = (MInOut)po;
			MBPartner bp = new MBPartner(po.getCtx(), ship.getC_BPartner_ID(), po.get_TrxName());
			
			//monto lo sacaremos linea x linea
			BigDecimal invAmt = Env.ZERO;			
			MInOutLine[] fromLines = ship.getLines(false);
			for (int i = 0; i < fromLines.length; i++)
			{
				MInOutLine sLine = fromLines[i];
				if(sLine.getC_OrderLine_ID() > 0)
				{
					BigDecimal amtTax = Env.ZERO;
					BigDecimal amtTemp = Env.ZERO;
					amtTemp = sLine.getQtyEntered().multiply(sLine.getC_OrderLine().getPriceEntered());
					amtTax = amtTemp.multiply(sLine.getC_OrderLine().getC_Tax().getRate().divide(Env.ONEHUNDRED));
					amtTemp = amtTemp.add(amtTax);
					amtTemp = amtTemp.setScale(sLine.getC_OrderLine().getC_Order().getC_Currency().getStdPrecision(), BigDecimal.ROUND_HALF_UP);
					invAmt = invAmt.add(amtTemp);					
				}
			}
			if(invAmt == null)
				invAmt = Env.ZERO;
			BigDecimal usedOFB = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");				
			if(usedOFB == null)
				usedOFB = Env.ZERO;
			if (ship.isSOTrx())			
				usedOFB = usedOFB.subtract(invAmt);
			else
				usedOFB = usedOFB.add(invAmt);
			bp.set_CustomColumn("SO_CreditUsedOFB", usedOFB);
			bp.set_CustomColumn("TotalOpenBalanceOFB", bp.getSO_CreditLimit().subtract(usedOFB));
			bp.setTotalOpenBalance(Env.ZERO);
			bp.setSO_CreditUsed(Env.ZERO);
			bp.save();
		}
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID() == MPayment.Table_ID) 
		{
			MPayment pay = (MPayment)po;
			MBPartner bp = new MBPartner(po.getCtx(),pay.getC_BPartner_ID(), po.get_TrxName());
			
			BigDecimal usedOFB = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");				
			if(usedOFB == null)
				usedOFB = Env.ZERO;
			if (pay.isReceipt())			
				usedOFB = usedOFB.subtract(pay.getPayAmt());
			else
				usedOFB = usedOFB.add(pay.getPayAmt());			
			bp.set_CustomColumn("SO_CreditUsedOFB", usedOFB);
			bp.set_CustomColumn("TotalOpenBalanceOFB", bp.getSO_CreditLimit().subtract(usedOFB));
			bp.setTotalOpenBalance(Env.ZERO);
			bp.setSO_CreditUsed(Env.ZERO);
			bp.save();
		}
		if(timing == TIMING_BEFORE_VOID && po.get_Table_ID() == MPayment.Table_ID) 
		{
			MPayment pay = (MPayment)po;
			MBPartner bp = new MBPartner(po.getCtx(),pay.getC_BPartner_ID(), po.get_TrxName());
			
			BigDecimal usedOFB = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");				
			if(usedOFB == null)
				usedOFB = Env.ZERO;
			if (pay.isReceipt())			
				usedOFB = usedOFB.add(pay.getPayAmt());
			else
				usedOFB = usedOFB.subtract(pay.getPayAmt());			
			bp.set_CustomColumn("SO_CreditUsedOFB", usedOFB);
			bp.set_CustomColumn("TotalOpenBalanceOFB", bp.getSO_CreditLimit().subtract(usedOFB));
			bp.setTotalOpenBalance(Env.ZERO);
			bp.setSO_CreditUsed(Env.ZERO);
			bp.save();
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
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString


	

}	