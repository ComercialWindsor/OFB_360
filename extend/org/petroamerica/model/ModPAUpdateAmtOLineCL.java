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
import java.sql.Timestamp;

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
 *	Validator for PA Colegios
 *
 *  @author Italo Niñoles
 */
public class ModPAUpdateAmtOLineCL implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPAUpdateAmtOLineCL ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPAUpdateAmtOLineCL.class);
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
		engine.addModelChange(MOrderLine.Table_Name, this);		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MOrderLine.Table_ID) 
		{	
			MOrderLine oLine = (MOrderLine)po;			
			MOrder order = new MOrder(po.getCtx(), oLine.getC_Order_ID(), po.get_TrxName());
			//if(oLine.getC_Order().isSOTrx() && oLine.get_ValueAsInt("C_OrderLineRef_ID") > 0)//viene de un copiar lineas
			if(oLine.get_ValueAsInt("C_OrderLineRef_ID") > 0)//viene de un copiar lineas
			{
				if(order.getDocStatus().compareToIgnoreCase("CO") != 0)
				{
					//calculamos nuevo valor y seteamos
					MProductPricing pp = new MProductPricing (oLine.getM_Product_ID(), oLine.getC_Order().getC_BPartner_ID(), oLine.getQtyOrdered(), oLine.getC_Order().isSOTrx());
					int M_PriceList_ID = oLine.getC_Order().getM_PriceList_ID();
					pp.setM_PriceList_ID(M_PriceList_ID);
					Timestamp orderDate = oLine.getC_Order().getDateOrdered();
					int M_PriceList_Version_ID = 0;
					if ( M_PriceList_Version_ID == 0 && M_PriceList_ID > 0)
					{
						String sql = "SELECT plv.M_PriceList_Version_ID "
							+ "FROM M_PriceList_Version plv "
							+ "WHERE plv.M_PriceList_ID=? "						//	1
							+ " AND plv.ValidFrom <= ? "
							+ "ORDER BY plv.ValidFrom DESC";
						M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, M_PriceList_ID, orderDate);
					}
					pp.setM_PriceList_Version_ID(M_PriceList_Version_ID); 
					pp.setPriceDate(orderDate);
					BigDecimal newPrice = pp.getPriceList();
					oLine.setPrice(newPrice);		
					oLine.setLineNetAmt();
					oLine.setTax();
					
					//impuesto fijo				
					BigDecimal taxNewPro = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(FixedTax) " +
							"FROM M_ProductPrice WHERE M_PriceList_Version_ID = "+M_PriceList_Version_ID+" AND M_Product_ID = "+oLine.getM_Product_ID());				
					BigDecimal taxNewLine = null;				
					if((taxNewLine == null || taxNewLine.compareTo(Env.ZERO) == 0) && taxNewPro != null)
					{
						oLine.set_CustomColumn("FixedTax",taxNewPro);
						taxNewLine = taxNewPro;
					}
					if(taxNewLine != null)
					{
						taxNewPro = taxNewLine.multiply(oLine.getQtyOrdered());					
						oLine.set_CustomColumn("FixedTaxAmt",taxNewPro);
					}
					//impuesto variable
					taxNewPro = null;
					taxNewLine = null;
					taxNewPro = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(VariableTax) " +
							"FROM M_ProductPrice WHERE M_PriceList_Version_ID = "+M_PriceList_Version_ID+" AND M_Product_ID = "+oLine.getM_Product_ID());
					taxNewLine = null;
					if((taxNewLine == null || taxNewLine.compareTo(Env.ZERO) == 0) && taxNewPro != null)
					{
						oLine.set_CustomColumn("VariableTax",taxNewPro);
						taxNewLine = taxNewPro;
					}
					if(taxNewLine != null)
					{
						taxNewPro = taxNewLine.multiply(oLine.getQtyOrdered());
						oLine.set_CustomColumn("VariableTaxAmt",taxNewPro);
					}	
					//calculamos impuesto IVA
					BigDecimal IVATax = null;
					if(oLine.getC_Tax().getName().toLowerCase().contains("iva"))
					{
						IVATax = oLine.getLineNetAmt().multiply(oLine.getC_Tax().getRate());
						IVATax = IVATax.divide(Env.ONEHUNDRED);
						IVATax = IVATax.setScale(10,BigDecimal.ROUND_HALF_EVEN);
						oLine.set_CustomColumn("IVATaxAmt", IVATax);
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