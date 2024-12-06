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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.model.*;

/**
 * 	Recalc Product Cost  to Last Invoice or Last PO
 *  @author fabian aguilar
 *  @version $Id: CostReset.java,v 1.2 28-07-2010 00:51:01 $
 */
public class CostResetTo extends SvrProcess
{
	/**	Product				*/
	private int 	p_M_Product_ID = 0; 
	
	private int 	p_M_Product_Category_ID = 0;

	private int  p_AD_Org_ID = 0;
	
	private boolean  p_UpdateQty;
	
	private boolean  p_FromPO = false;//indica si el costo se saca de la Ultima PO
	
	private int p_WareHouse_ID = 0;
	
	private BigDecimal p_Margen = Env.ZERO;
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
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Warehouse_ID"))
				p_WareHouse_ID = para[i].getParameterAsInt();
			else if (name.equals("UpdateQty"))
				p_UpdateQty = para[i].getParameterAsBoolean();
			else if (name.equals("FromPO"))
				p_FromPO = para[i].getParameterAsBoolean();
			else if (name.equals("Margen"))
				p_Margen = (BigDecimal)para[i].getParameter();
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
		if (p_M_Product_ID == 0 && p_M_Product_Category_ID==0)
			throw new AdempiereUserError("@NotFound@: @M_Product_ID@ = " + p_M_Product_ID);
		
		//validacon parametros
		if(p_UpdateQty)
			if(p_WareHouse_ID==0)
				throw new AdempiereUserError("Para Actualizar la Cantidad se Requiere Un Warehouse");
			
		if(p_Margen.signum()==0)
			throw new AdempiereUserError("El Margen Debe ser al Menos 1");
		//********************************
		
		if(p_M_Product_ID>0){
			MProduct product = MProduct.get(getCtx(), p_M_Product_ID);
		
			if (product.get_ID() != p_M_Product_ID)
				throw new AdempiereUserError("@NotFound@: @M_Product_ID@ = " + p_M_Product_ID);
			
			if(p_FromPO)
				OFBProductCost.resetCostToPO(p_M_Product_ID, p_Margen, p_AD_Org_ID, p_UpdateQty, p_WareHouse_ID, get_TrxName(),getCtx());
			else
				OFBProductCost.resetCostToInvoice(p_M_Product_ID, p_Margen, p_AD_Org_ID, p_UpdateQty, p_WareHouse_ID, get_TrxName(),getCtx());
			
			this.commitEx();
			return "@OK@";
		}
		
		if(p_M_Product_Category_ID>0){
			String sql = "SELECT p.M_Product_ID FROM M_Product p " +
					"Inner Join M_CostDetail d on (p.M_Product_ID=d.M_product_ID)"
				+ " WHERE p.M_Product_Category_ID=?"
				+ " AND p.isactive='Y' ";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				pstmt.setInt (1, p_M_Product_Category_ID);
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					if(p_FromPO)
						OFBProductCost.resetCostToPO(rs.getInt(1), p_Margen, p_AD_Org_ID, p_UpdateQty, p_WareHouse_ID, get_TrxName(),getCtx());
					else
						OFBProductCost.resetCostToInvoice(rs.getInt(1), p_Margen, p_AD_Org_ID, p_UpdateQty, p_WareHouse_ID, get_TrxName(),getCtx());
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
				this.commitEx();
				return "@OK@";
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
				
			}
		}
		return "@Error@";
	}	//	doIt
	
}	//	CostCreate
