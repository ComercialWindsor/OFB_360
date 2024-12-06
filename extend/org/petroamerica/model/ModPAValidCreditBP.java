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
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for PDV Colegios
 *
 *  @author Italo Niñoles
 */
public class ModPAValidCreditBP implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPAValidCreditBP ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPAValidCreditBP.class);
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
		engine.addModelChange(MOrder.Table_Name, this);
		engine.addDocValidate(MOrder.Table_Name, this);		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if((type == TYPE_BEFORE_NEW ||  type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==MOrder.Table_ID)
		{
			MOrder order = (MOrder)po;
			MBPartner bPart = new MBPartner(po.getCtx(),order.getC_BPartner_ID(),po.get_TrxName());
			if(bPart.get_ValueAsString("SOCreditStatusOFB").compareTo("H") == 0 )
				return "Error: Cliente con credito retenido";
		}
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MOrder.Table_ID) 
		{	
			MOrder order = (MOrder)po;
			if(order.isSOTrx())
			{
				int flag = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) " +
						" FROM C_DocType WHERE C_DocType_ID = "+order.getC_DocType_ID()+"" +
						" AND C_DocType_ID IN ( SELECT dt.C_DocType_ID FROM C_DocType dt " +
						" INNER JOIN C_DocType dt2 ON (dt.C_DocTypeInvoice_ID = dt2.C_DocType_ID)" +
						" WHERE dt2.DocBaseType = 'ARC')");
				if(!order.get_ValueAsBoolean("Override") && flag < 1)
				{
					MBPartner bp = new MBPartner(po.getCtx(), order.getC_BPartner_ID(), po.get_TrxName());
					//ininoles se usa nuevo campo para estado de credito para no tener problemas
					String SOCreditStatusOFB = bp.get_ValueAsString("SOCreditStatusOFB");
					if(SOCreditStatusOFB == null || SOCreditStatusOFB.trim() == "" 
						|| SOCreditStatusOFB.trim() == "")
						SOCreditStatusOFB = bp.getSOCreditStatus();
					if(SOCreditStatusOFB == null || SOCreditStatusOFB.trim() == "" 
						|| SOCreditStatusOFB.trim() == "")
						SOCreditStatusOFB = "O";
					//end
					if(SOCreditStatusOFB.compareToIgnoreCase("H")==0)
						return "Error: Problema con crédito de cliente";
					if(SOCreditStatusOFB.compareToIgnoreCase("S")==0)
						return "Error: Problema con crédito de cliente";
					if(SOCreditStatusOFB.compareToIgnoreCase("W")==0)
						return "Error: Problema con crédito de cliente";
					if(SOCreditStatusOFB.compareToIgnoreCase("O")==0)
					{	
						BigDecimal amtcredit = (BigDecimal)bp.get_Value("TotalOpenBalanceOFB");
						//en caso de ser null o 0 recalculamos el saldo
						if(amtcredit == null || amtcredit.compareTo(Env.ZERO) == 0)
						{
							BigDecimal amtUsed = (BigDecimal)bp.get_Value("SO_CreditUsedOFB");
							BigDecimal ownCredit = (BigDecimal)bp.get_Value("OwnCreditLimit");
							BigDecimal extCredit = (BigDecimal)bp.get_Value("ExternalCreditLimit");
							if(ownCredit != null)
								amtcredit = ownCredit;
							if(extCredit != null)
								amtcredit = amtcredit.add(extCredit);
							if(amtUsed != null)
								amtcredit = amtcredit.subtract(amtUsed);
						}							
						if(amtcredit == null)
							amtcredit = (BigDecimal)bp.get_Value("OwnCreditLimit");
						if(amtcredit == null)
							amtcredit = Env.ZERO;
						//ininoles nueva forma de calcular grandtotal
						MOrderLine[] olines = order.getLines();
						BigDecimal newGrandTotal = order.getTotalLines();
						BigDecimal taxNewLine = Env.ZERO;
						for (int i = 0; i < olines.length; i++)
						{
							MOrderLine oline = olines[i];
							if((BigDecimal)oline.get_Value("FixedTaxAmt") != null)
								taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("FixedTaxAmt"));
							if((BigDecimal)oline.get_Value("VariableTax") != null)
								taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("VariableTaxAmt"));
							if((BigDecimal)oline.get_Value("IVATaxAmt") != null)
								taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("IVATaxAmt"));				
						}			
						if(taxNewLine != null)
						{
							newGrandTotal = newGrandTotal.add(taxNewLine);
							newGrandTotal = newGrandTotal.setScale(0, RoundingMode.HALF_EVEN);
						}
						//ininoles end						
						amtcredit = amtcredit.subtract(newGrandTotal);
						if(amtcredit.compareTo(Env.ZERO) < 0)
							return "Error: Credito de cliente insuficiente";
					}	
					Timestamp dateStartCredit = (Timestamp)bp.get_Value("StartDateCreditLimit");
					Timestamp dateEndCredit = (Timestamp)bp.get_Value("EndDateCreditLimit");
					//Calendar calendar = Calendar.getInstance();
					Timestamp today = new Timestamp(Calendar.getInstance().getTimeInMillis());
					if(dateStartCredit != null && dateStartCredit.compareTo(today) > 0)
						return "Error: Credito no vigente";
					if(dateEndCredit != null && dateEndCredit.compareTo(today) < 0)
						return "Error: Credito no vigente";
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
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString


	

}	