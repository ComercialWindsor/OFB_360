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
package org.petroamerica.model;

import java.math.BigDecimal;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	Validator for company PT
 *
 *  @author Italo Niñoles
 */
public class ModPetroAmericaValidPay implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPetroAmericaValidPay ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPetroAmericaValidPay.class);
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
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
	return null;
	}	//	modelChange
	
	public static String rtrim(String s, char c) {
	    int i = s.length()-1;
	    while (i >= 0 && s.charAt(i) == c)
	    {
	        i--;
	    }
	    return s.substring(0,i+1);
	}

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

		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MPayment.Table_ID)
		{
			MPayment pay = (MPayment)po;			
			BigDecimal amtInvoice = null;
			BigDecimal amtOrder = null;
			if(pay.getC_Invoice_ID() > 0)
			{
				//calculamos monto factura
				String sqlInvoice = "SELECT " +
				" SUM(abs(currencyConvert(invoiceOpen(C_Invoice_ID,C_InvoicePaySchedule_ID),i.C_Currency_ID,228,i.dateacct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP)) as amt" +
				" FROM c_invoice_v i WHERE C_Invoice_ID = "+pay.getC_Invoice_ID();
				amtInvoice = DB.getSQLValueBD(po.get_TrxName(), sqlInvoice);
				
				if(pay.getC_Invoice_ID() > 0 && amtInvoice != null && pay.getPayAmt().compareTo(amtInvoice) > 0)
					return "Error: Monto de Pago supera Monto de Factura";
			}
			if(pay.getC_Order_ID() > 0)
			{
				MOrder order = new MOrder(po.getCtx(), pay.getC_Order_ID(), po.get_TrxName());
				//calculamos monto order
				String sqlOrder = "select ABS(SUM(cp.PayAmt)) as amt FROM C_Payment cp " +
						"WHERE IsActive = 'Y' AND DocStatus IN ('CO','CL','DR','IP') AND C_Order_ID = "+pay.getC_Order_ID();
				amtOrder = DB.getSQLValueBD(po.get_TrxName(), sqlOrder);
				if(amtOrder == null)
					amtOrder = Env.ZERO;					
				amtOrder = order.getGrandTotal().subtract(amtOrder);
				
				if(pay.getC_Order_ID() > 0 && amtOrder != null && pay.getPayAmt().compareTo(amtOrder) > 0)
					return "Error: Monto de Pago supera Monto de Orden";
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