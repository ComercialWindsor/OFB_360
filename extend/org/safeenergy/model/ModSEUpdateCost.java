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
package org.safeenergy.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCost;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MTax;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.OFBProductCost;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	Validator for SAFE Energy
 *
 *  @author Fabian Aguilar
 */
public class ModSEUpdateCost implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModSEUpdateCost ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModSEUpdateCost.class);
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
		engine.addDocValidate(MInOut.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farías
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
				
	return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInOut.Table_ID)
		{
			MInOut receipt = (MInOut)po;
			MAcctSchema ass = MAcctSchema.getClientAcctSchema(po.getCtx(), Env.getAD_Client_ID(po.getCtx()))[0];
			
			if (!receipt.isSOTrx() && receipt.getC_Order_ID() > 0)
			{
				BigDecimal totalCost = DB.getSQLValueBD(po.get_TrxName(), "SELECT (SUM(cost1)+SUM(cost2)+SUM(cost3)+SUM(cost4)+SUM(cost5)+SUM(cost6)+SUM(cost7)+SUM(cost8)) as cost " +
						"FROM POI_ImportCost WHERE M_InOut_ID = ?",receipt.get_ID());
				
				MInOutLine[] receiptLines = receipt.getLines(false);
				for (int i = 0; i < receiptLines.length; i++)
				{
					MInOutLine line = receiptLines[i];
					if (line.getM_Product_ID() > 0)
					{
						MCost cost = null;
						cost = OFBProductCost.getMCost(line.getM_Product_ID(),0,0,ass.getC_AcctSchema_ID(),ass.getM_CostType_ID() ,po.get_TrxName(),po.getCtx());
						BigDecimal acumulatedAmt = (BigDecimal)cost.get_Value("managementAmt");
						BigDecimal acumulatedQty = (BigDecimal)cost.get_Value("managementQty");
						BigDecimal productCost = (BigDecimal)cost.get_Value("managementCost");
						if (acumulatedAmt == null)
							acumulatedAmt = Env.ZERO;
						if(acumulatedQty == null)
							acumulatedQty = Env.ZERO;
						if(productCost == null)
							productCost = Env.ZERO;
						
						if (line.getC_OrderLine_ID() > 0)
						{
							MOrderLine oLine = new MOrderLine(po.getCtx(), line.getC_OrderLine_ID(), po.get_TrxName());
							MOrder order = new MOrder(po.getCtx(), oLine.getC_Order_ID(), po.get_TrxName());
						    String sqlSchema = "SELECT MAX(C_AcctSchema1_ID) as C_AcctSchema1_ID FROM AD_ClientInfo WHERE AD_Client_ID = "+order.getAD_Client_ID();
						    int id_Schema = DB.getSQLValue(po.get_TrxName(), sqlSchema);
							MAcctSchema as = new MAcctSchema(po.getCtx(), id_Schema, po.get_TrxName());
							BigDecimal amt = oLine.getLineNetAmt();
							//calculo de neto de linea
						
							MTax imp = new MTax(po.getCtx(), oLine.getC_Tax_ID(), po.get_TrxName());
							
							if (amt != null)
							{
								BigDecimal amtImp = amt.multiply(imp.getRate());
								amtImp = amtImp.divide(Env.ONEHUNDRED, 6,RoundingMode.HALF_EVEN);
								amt = amt.add(amtImp);
								BigDecimal ponderado = new BigDecimal("0.0");
								if (totalCost != null)								
								{
									ponderado = (amt.divide(order.getGrandTotal(), 6,RoundingMode.HALF_EVEN)).multiply(totalCost);
								}
								BigDecimal costBase = amt.add(ponderado);		
								
								//monto con conversion
								BigDecimal amtConvert = null;
								if (order.getC_Currency_ID() != as.getC_Currency_ID())
								{
									amtConvert = MConversionRate.convert(po.getCtx(), costBase.divide(line.getQtyEntered(), 6,RoundingMode.HALF_EVEN), 
											order.getC_Currency_ID(), as.getC_Currency_ID(),
											order.getDateOrdered(), order.getC_ConversionType_ID(), 
											order.getAD_Client_ID(), order.getAD_Org_ID());
									log.config("monto dentro de if: "+ amtConvert);
								}
								else
								{
									amtConvert =  oLine.getLineNetAmt();
								}								
								acumulatedAmt = acumulatedAmt.add(amtConvert.multiply(line.getQtyEntered()));
								acumulatedQty = acumulatedQty.add(line.getQtyEntered());
								
								if (productCost == null)
								{
									productCost = amtConvert;
								}
								else if(productCost.compareTo(Env.ZERO) == 0)
								{
									productCost = (productCost.add(amtConvert)).divide(Env.ONE);
								}else
								{
									productCost = (productCost.add(amtConvert)).divide(new BigDecimal("2.0"),6,RoundingMode.HALF_EVEN);
								}
								
								
								//seteo de costos
								cost.set_CustomColumn("managementAmt", acumulatedAmt);
								cost.set_CustomColumn("managementQty", acumulatedQty);
								cost.set_CustomColumn("managementCost", productCost);
								cost.save();
							}
						}
					}					
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