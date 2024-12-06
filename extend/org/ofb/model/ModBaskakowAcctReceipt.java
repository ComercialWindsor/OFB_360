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

import org.compiere.acct.FactLine;
import org.compiere.model.MClient;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for Baskakow
 *
 *  @author italo niñoles
 */
public class ModBaskakowAcctReceipt implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBaskakowAcctReceipt ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBaskakowAcctReceipt.class);
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
     *	OFB Consulting Ltda. By Italo Niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==FactLine.Table_ID) 
		{
			FactLine fLine = (FactLine)po;
			if(fLine.getAD_Table_ID() == MInOut.Table_ID)
			{
				MInOut ship = new MInOut(po.getCtx(), fLine.getRecord_ID(), po.get_TrxName());
				if(ship.isSOTrx() == true)
				{
					if(fLine.getM_Product_ID() > 0)
					{
						MInOutLine[] lines = ship.getLines(false);
						for (int i = 0; i < lines.length; i++)
						{
							MInOutLine line = lines[i];
							BigDecimal buyM3 = (BigDecimal)line.get_Value("buy_m3");
							if (buyM3 == null)
								buyM3 = Env.ZERO;
							if (line.getM_Product_ID() == fLine.getM_Product_ID() && fLine.getLine_ID() == line.get_ID() && fLine.getQty().compareTo(Env.ZERO) > 0)
							{
								BigDecimal amt = DB.getSQLValueBD(po.get_TrxName(), "(SELECT mppo.pricelist  " +
										" from m_inoutline miol JOIN c_orderbp cobp ON miol.c_orderbp_id = cobp.c_orderbp_id " +
										" JOIN c_bplocatorprice mppo ON miol.m_product_id = mppo.m_product_id AND " +
										" mppo.c_bpartner_location_id = cobp.c_bpartner_location_id WHERE miol.M_InOutLine_ID = ?)", line.get_ID());
								if (amt != null)
								{
									amt = amt.multiply(buyM3);
									if (amt.signum() > 0)
									{
										DB.executeUpdateEx(
												"UPDATE Fact_Acct SET amtsourcedr = "+amt+", amtacctdr = "+amt+" WHERE fact_acct_id = "+fLine.get_ID(), po.get_TrxName());
									}
								}	
							}
							else if (line.getM_Product_ID() == fLine.getM_Product_ID() && fLine.getLine_ID() == line.get_ID() && fLine.getQty().compareTo(Env.ZERO) <= 0)
							{
								BigDecimal amt = DB.getSQLValueBD(po.get_TrxName(), "(SELECT mppo.pricelist  " +
										" from m_inoutline miol JOIN c_orderbp cobp ON miol.c_orderbp_id = cobp.c_orderbp_id " +
										" JOIN c_bplocatorprice mppo ON miol.m_product_id = mppo.m_product_id AND " +
										" mppo.c_bpartner_location_id = cobp.c_bpartner_location_id WHERE miol.M_InOutLine_ID = ?)", line.get_ID());
								if (amt != null)
								{
									amt = amt.multiply(buyM3);
									if (amt.signum() > 0)
									{
										DB.executeUpdateEx(
												"UPDATE Fact_Acct SET amtsourcecr = "+amt+", amtacctcr = "+amt+" WHERE fact_acct_id = "+fLine.get_ID(), po.get_TrxName());
									}
								}	
							}
							
						}
					}
					/*else
					{
						MInOutLine[] lines = ship.getLines(false);
						BigDecimal amtAcum = Env.ZERO;
						for (int i = 0; i < lines.length; i++)
						{
							MInOutLine line = lines[i];
							BigDecimal amt = DB.getSQLValueBD(po.get_TrxName(), "(SELECT mppo.pricelist  " +
									" from m_inoutline miol JOIN c_orderbp cobp ON miol.c_orderbp_id = cobp.c_orderbp_id " +
									" JOIN c_bplocatorprice mppo ON miol.m_product_id = mppo.m_product_id AND " +
									" mppo.c_bpartner_location_id = cobp.c_bpartner_location_id WHERE miol.M_InOutLine_ID = ?)", line.get_ID());
							if (amt != null)
							{
								amt = amt.multiply(line.getQtyEntered());
							}
							amtAcum = amtAcum.add(amt); 
						}
						if (amtAcum.signum() > 0)
						{
							DB.executeUpdateEx("UPDATE Fact_Acct SET amtsourcedr = "+amtAcum+", amtacctdr = "+amtAcum+" WHERE fact_acct_id = "+fLine.get_ID(), po.get_TrxName());
						}
					}*/
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