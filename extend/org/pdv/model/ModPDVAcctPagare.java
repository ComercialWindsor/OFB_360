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
package org.pdv.model;


import java.math.BigDecimal;

import org.compiere.acct.FactLine;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for PDV Colegios
 *
 *  @author Fabian Aguilar
 */
public class ModPDVAcctPagare implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPDVAcctPagare ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPDVAcctPagare.class);
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
		//	Documents to be monitored
		
		engine.addModelChange(FactLine.Table_Name, this);		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farías
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==FactLine.Table_ID) 
		{
			FactLine fLine = (FactLine)po;
			if(fLine.getAD_Table_ID() == MInvoice.Table_ID)
			{
				MInvoice inv = new MInvoice(po.getCtx(), fLine.getRecord_ID(), po.get_TrxName());
				if(inv.getC_DocType_ID() == 1000070)
				{
					BigDecimal debito = fLine.getAmtAcctDr();
					BigDecimal credito = fLine.getAmtAcctCr();
					//if(fLine.getAmtAcctDr().compareTo(Env.ZERO) > 0)
					//{
					/*	fLine.setAmtAcctCr(debito);
						fLine.setAmtAcctDr(credito);
						fLine.setAmtSourceCr(debito);
						fLine.setAmtSourceDr(credito);*/
					//}
					DB.executeUpdateEx("UPDATE Fact_Acct SET amtsourcedr = "+credito+", amtsourcecr = "+debito+", "+
							" amtacctdr = "+credito+", amtacctcr = "+debito +					
							"WHERE fact_acct_id = "+fLine.get_ID(), po.get_TrxName());
				}				
			}
			if(fLine.getAD_Table_ID() == MAllocationHdr.Table_ID)
			{
				MAllocationHdr allo = new MAllocationHdr(po.getCtx(), fLine.getRecord_ID(), po.get_TrxName());				
				Boolean isPagare = false;
				MAllocationLine[] lines =allo.getLines(false);
				int id_Invoice = 0;
				for (int i=0;i<lines.length;i++)
				{
					if(lines[i].getC_Invoice_ID() > 0)
					{
						MInvoice inv = new MInvoice(po.getCtx(), lines[i].getC_Invoice_ID(),po.get_TrxName());
						if(inv.getC_DocType_ID() == 1000070)
						{
							isPagare = true;							
						}
						id_Invoice = lines[i].getC_Invoice_ID();
					}
				}
				if (isPagare)
				{
					if(fLine.getAmtAcctCr().compareTo(Env.ZERO) != 0)
					{
						if(id_Invoice > 0)
						{							
							int id_charge = 0;							
							id_charge =	DB.getSQLValue(po.get_TrxName(),"Select MAX(C_Charge_ID) as C_Charge_ID FROM C_InvoiceLine WHERE C_invoicefac_id > 0 AND C_Invoice_ID = "+id_Invoice);
							if (id_charge > 0)
							{
								;
							}
							else
							{
								id_charge =	DB.getSQLValue(po.get_TrxName(),"Select MAX(C_Charge_ID) as C_Charge_ID FROM C_InvoiceLine WHERE C_Invoice_ID = "+id_Invoice);
							}
							
							
							String sqlAcctCharge = "";
							if (fLine.getAmtAcctCr().compareTo(Env.ZERO) > 0)
							{
								sqlAcctCharge = "SELECT MAX(Ch_Expense_Acct) FROM C_Charge_Acct WHERE C_Charge_ID = "+id_charge;
							}else
							{
								sqlAcctCharge = "SELECT MAX(Ch_Revenue_Acct) FROM C_Charge_Acct WHERE C_Charge_ID = "+id_charge;
							}
							int id_vCombination = DB.getSQLValue(po.get_TrxName(), sqlAcctCharge);
							int id_acct =DB.getSQLValue(po.get_TrxName(),"SELECT account_id FROM c_validcombination where c_validcombination_id = "+id_vCombination);
							
							DB.executeUpdateEx("UPDATE Fact_Acct SET account_id = "+id_acct+" WHERE fact_acct_id = "+fLine.get_ID(), po.get_TrxName());
						}			
					}
				}				
			}
		}			
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
				
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