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
 * 	Create  Costing for Product
 *	
 *  @author Fabian Aguilar
 *  @version $Id: OFBCostCreate.java,v 1.0 $
 */
public class OFBCostCreate extends SvrProcess
{
	/**	Product				*/
	private int 	p_M_Product_ID = 0; 
	
	private int 	p_M_Product_Category_ID=0;

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
		String sql = "SELECT p.M_Product_ID FROM M_Product p " +
		"Inner Join M_CostDetail d on (p.M_Product_ID=d.M_product_ID)"
		+ " WHERE p.isactive='Y' And Not exists (select * from M_Cost c where c.M_product_ID=p.M_Product_ID)";
		
		if(p_M_Product_ID>0)
			sql += " AND p.M_Product_ID="+p_M_Product_ID;
		
		
		if(p_M_Product_Category_ID>0)
			sql += " AND p.M_Product_Category_ID="+p_M_Product_Category_ID;
			
		MAcctSchema as = MAcctSchema.getClientAcctSchema(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()))[0];
		MCostElement element= getStandarCostelement();
		int count=0;
		
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					MProduct product = MProduct.get(getCtx(), rs.getInt(1));
					int Org_ID = product.getCostingLevel(as).equals(X_M_Product_Category_Acct.COSTINGLEVEL_Client)?0:product.getAD_Org_ID() ;
					
					MCost cost = new MCost(product, product.getM_AttributeSetInstance_ID(),
							 as, Org_ID, element.getM_CostElement_ID());
					
					cost.save();
					count ++;
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
				return "@OK@" + count;
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
				
			}
		
		return "@Error@";
	}	//	doIt
	
	public MCostElement getStandarCostelement(){
		
		return MCostElement.getByCostingMethod(Env.getCtx(), MCostElement.COSTINGMETHOD_StandardCosting).get(0);
	}
}	//	CostCreate
