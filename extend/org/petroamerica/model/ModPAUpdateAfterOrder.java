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

import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProductPricing;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for PA
 *
 *  @author Italo Niñoles
 */
public class ModPAUpdateAfterOrder implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPAUpdateAfterOrder ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPAUpdateAfterOrder.class);
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
		engine.addDocValidate(MOrder.Table_Name, this);				
		
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

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MOrder.Table_ID)			
		{	
			MOrder order = (MOrder)po;
			MOrderLine[] olines = order.getLines();
			MBPartnerLocation loc = new MBPartnerLocation(po.getCtx(), order.getC_BPartner_Location_ID(),po.get_TrxName());
			BigDecimal cost = (BigDecimal)loc.get_Value("Cost");
			int M_PriceList_Version_ID = 0;			
			if ( M_PriceList_Version_ID == 0 && order.getM_PriceList_ID() > 0)
			{
				String sql = "SELECT plv.M_PriceList_Version_ID "
					+ "FROM M_PriceList_Version plv "
					+ "WHERE plv.M_PriceList_ID=? "						//	1
					+ " AND plv.ValidFrom <= ? "
					+ "ORDER BY plv.ValidFrom DESC";
				//	Use newest price list - may not be future
				
				M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, order.getM_PriceList_ID(), order.getDateOrdered());
				if ( M_PriceList_Version_ID > 0 )
				{
					order.set_CustomColumn("M_PriceList_Version_ID",M_PriceList_Version_ID);
					order.save();
				}
			}	
			BigDecimal taxNewLine = Env.ZERO;
			for (int i = 0; i < olines.length; i++)
			{
				MOrderLine oline = olines[i];
				MProductPricing pp = new MProductPricing (oline.getM_Product_ID(), order.getC_BPartner_ID(), oline.getQtyOrdered(), order.isSOTrx());
				pp.setM_PriceList_ID(order.getM_PriceList_ID());
				/** PLV is only accurate if PL selected in header */				
				
				pp.setM_PriceList_Version_ID(M_PriceList_Version_ID); 
				pp.setPriceDate(order.getDateOrdered());
				//ininoles se descuenta costo de direccion
				BigDecimal priceStd = pp.getPriceStd();							 
				if(cost != null)			
					priceStd = priceStd.add(cost);		
				//
				if(priceStd != null && priceStd.compareTo(Env.ZERO) > 0)
				{
					oline.setPrice(priceStd);
					/*oline.setPriceList(pp.getPriceList());
					oline.setPriceLimit(pp.getPriceLimit());
					oline.setPrice(pp.getPriceStd());*/
					oline.setTax();
					oline.setLineNetAmt();
					oline.save();
				}
				//nuevos campos para calcular nuevo grantotal
				if((BigDecimal)oline.get_Value("FixedTaxAmt") != null)
					taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("FixedTaxAmt"));
				if((BigDecimal)oline.get_Value("VariableTax") != null)
					taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("VariableTaxAmt"));
				if((BigDecimal)oline.get_Value("IVATaxAmt") != null)
					taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("IVATaxAmt"));				
			}
			if(taxNewLine != null)
			{				
				taxNewLine = taxNewLine.setScale(10,BigDecimal.ROUND_HALF_EVEN);
				DB.executeUpdate("UPDATE C_Order SET GrandTotal = "+order.getTotalLines().add(taxNewLine)+" " +
						" WHERE C_Order_ID = "+order.get_ID(), po.get_TrxName());
			}
			//order.calculateTaxTotal();
			//order.save();
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
