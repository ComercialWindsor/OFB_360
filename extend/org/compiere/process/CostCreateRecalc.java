/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.compiere.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.model.*;

/**
 * 	Create/Update Costing for Product
 *	
 *  @author Jorg Janke
 *  @version $Id: CostCreate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class CostCreateRecalc extends SvrProcess
{
	/**	Product				*/
	private int 	p_M_Product_ID = 0; 
	
	private int 	p_M_Product_Category_ID=0;//faaguilar OFB

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
		//	log.fine("prepare - " + para[i]);
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_Category_ID"))//faaguilar OFB
				p_M_Product_Category_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);		
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (text with variables)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		log.info("M_Product_ID=" + p_M_Product_ID);
		if (p_M_Product_ID == 0 && p_M_Product_Category_ID==0)//faaguilar OFB
			throw new AdempiereUserError("@NotFound@: @M_Product_ID@ = " + p_M_Product_ID);
		
		if(p_M_Product_ID>0){//faaguilar OFB
			MProduct product = MProduct.get(getCtx(), p_M_Product_ID);
			if (product.get_ID() != p_M_Product_ID)
				throw new AdempiereUserError("@NotFound@: @M_Product_ID@ = " + p_M_Product_ID);
			//
			MCostElement[] ces = MCostElement.getCostingMethods(product);
			MAcctSchema as = MAcctSchema.getClientAcctSchema(this.getCtx(), this.getAD_Client_ID())[0];
			for (int i = 0; i < ces.length; i++)
			{
				MOrg orgs[] = MOrg.getOfClient(product);
				for(int x = 0; x < orgs.length ; x++){
					MCostElement ce = ces[i];
					MCost cost = OFBProductCost.getMCost(product.getM_Product_ID(), orgs[x].getAD_Org_ID(), ce.getM_CostElement_ID()
							, as.getC_AcctSchema_ID(), as.getM_CostType_ID(), this.get_TrxName(), getCtx());
					
					cost.setCumulatedQty(Env.ZERO);
					cost.setCurrentCostPrice(Env.ZERO);
					cost.setCumulatedAmt(Env.ZERO);
					cost.setCurrentQty(Env.ZERO);
					cost.saveEx();
				}
			}
			
			DB.executeUpdate("update m_costdetail set processed='N', enabled='Y' " +
					" where m_product_id="+p_M_Product_ID, get_TrxName());
			
			OFBProductCost.processCost(product.getM_Product_ID(), get_TrxName(), getCtx());
			commitEx();
			return "@OK@";
		}
		
		//faaguilar OFB begin
		if(p_M_Product_Category_ID>0){
			String sql = "SELECT p.M_Product_ID FROM M_Product p " +
			"Inner Join M_Cost d on (p.M_Product_ID=d.M_product_ID)"
			+ " WHERE p.M_Product_Category_ID=?"
			+ " AND p.isactive='Y' ";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				pstmt.setInt (1, p_M_Product_Category_ID);
				ResultSet rs = pstmt.executeQuery ();
				MAcctSchema as = MAcctSchema.getClientAcctSchema(this.getCtx(), this.getAD_Client_ID())[0];
				while (rs.next ())
				{
					MProduct product = MProduct.get(getCtx(), rs.getInt(1));
					
					MCostElement[] ces = MCostElement.getCostingMethods(product);
					for (int i = 0; i < ces.length; i++)
					{
						MCostElement ce = ces[i];
						MOrg orgs[] = MOrg.getOfClient(product);
						for(int x = 0; x < orgs.length ; x++){
							MCost cost = OFBProductCost.getMCost(product.getM_Product_ID(), orgs[x].getAD_Org_ID(), ce.getM_CostElement_ID()
									, as.getC_AcctSchema_ID(), as.getM_CostType_ID(), this.get_TrxName(), getCtx());
							
							cost.setCumulatedQty(Env.ZERO);
							cost.setCurrentCostPrice(Env.ZERO);
							cost.setCumulatedAmt(Env.ZERO);
							cost.setCurrentQty(Env.ZERO);
							cost.saveEx();
						}
					}
					
					DB.executeUpdate("update m_costdetail set processed='N', enabled='Y' " +
							" where m_product_id="+p_M_Product_ID, get_TrxName());
					
					OFBProductCost.processCost(product.getM_Product_ID(), get_TrxName(), getCtx());
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
				commitEx();
				return "@OK@";
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
				
			}
		}
		
		//faaguilar OFB end
		return "@Error@";
	}	//	doIt
	
}	//	CostCreate
