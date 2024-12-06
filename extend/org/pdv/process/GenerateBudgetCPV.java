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
package org.pdv.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Italo Niñoles Ininoles
 *  @version $Id: ProcessPaymentRequest.java,v 1.2 2014/08/26 10:11:01  $
 */
public class GenerateBudgetCPV extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int p_cant;
	private int p_IDBudget;
			
	protected void prepare()
	{	
		p_IDBudget = getRecord_ID();
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("Qty"))
				p_cant = para[i].getParameterAsInt();			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		X_EO_BudgetHdr budHdr = new X_EO_BudgetHdr(getCtx(), p_IDBudget, get_TrxName());		
		
		String sql = "SELECT * FROM EO_Budget WHERE  EO_BudgetHdr_ID = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt(1, budHdr.get_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{	
				X_EO_Budget bud = new X_EO_Budget(getCtx(), rs.getInt("EO_Budget_ID"), get_TrxName());
				for (int i=1; i <= p_cant; i++)
				{
					X_EO_Budget budNew = new X_EO_Budget(getCtx(), 0, get_TrxName());
					budNew.setEO_BudgetHdr_ID(bud.getEO_BudgetHdr_ID());
					budNew.setAD_Org_ID(bud.getAD_Org_ID());
					budNew.setM_Product_ID(bud.getM_Product_ID());
					budNew.setM_AttributeSetInstance_ID(bud.getM_AttributeSetInstance_ID());
					budNew.setQty(bud.getQty());
					budNew.setPriceActual(bud.getPriceActual());
					budNew.setM_ProductRef_ID(bud.getM_ProductRef_ID());
					if (bud.getC_BPartner_ID() > 0)
					{
						budNew.setC_BPartner_ID(bud.getC_BPartner_ID());
					}
					budNew.setC_Period_ID(bud.getC_Period_ID()+i);
					budNew.set_CustomColumn("Comments", bud.get_ValueAsString("Comments"));
					budNew.save();					
				}				
			}
		}catch (SQLException e)
		{
			log.log(Level.SEVERE, "BP - " + sql.toString(), e);
		}
		return "Detalle Generado";
	}	//	doIt	
}	//	CopyOrder
