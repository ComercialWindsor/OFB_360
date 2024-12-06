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
 * Contributor(s): Chris Farley - northernbrewer                              *
 *****************************************************************************/
package org.tsm.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MCharge;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	CopyFromJobStandar
 *	
 */
public class GenerateLineRecauchaje extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_Order_ID;
	private int p_AssetGroup_ID;
		
	protected void prepare()
	{	
		p_Order_ID = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("A_Asset_Group_ID"))
			{
				p_AssetGroup_ID = para[i].getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		MOrder order = new MOrder(getCtx(), p_Order_ID, get_TrxName());
		int cant = 0;
		String sql = "SELECT A_Asset_ID FROM A_Asset WHERE IsActive = 'Y' AND custodio IN ('TR') " +
			" AND M_Warehouse_ID = "+order.getM_Warehouse_ID()+
			" AND A_Asset_ID NOT IN (SELECT A_Asset_ID FROM C_OrderLine col " +
			" INNER JOIN C_Order co ON (col.C_Order_ID = co.C_Order_ID)" +
			" WHERE col.A_Asset_ID IS NOT NULL AND co.DocStatus IN ('DR','IP'))";
		
		if(p_AssetGroup_ID > 0)
			sql = sql + " AND A_Asset_Group_ID = "+p_AssetGroup_ID;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();			
			
			while (rs.next())
			{
				//obtenemos ID de cargo
				int ID_Charge = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Charge_ID) FROM C_Charge cc" +
						" INNER JOIN C_ChargeType cct ON (cc.C_ChargeType_ID = cct.C_ChargeType_ID)" +
						" WHERE cct.Value = 'TCRC'");
				MCharge charge = new MCharge(getCtx(), ID_Charge, get_TrxName());
				//generamos la linea de la orden
				MOrderLine oLine = new MOrderLine(order);
				oLine.setAD_Org_ID(order.getAD_Org_ID());
				oLine.setC_Charge_ID(ID_Charge);
				oLine.setPrice(charge.getChargeAmt());			
				oLine.setQty(Env.ONE);
				oLine.setTax();
				oLine.setLineNetAmt();
				oLine.set_CustomColumn("A_Asset_ID", rs.getInt("A_Asset_ID"));
				oLine.setDescription("Generada Automaticamente");
				oLine.set_CustomColumn("A_CreateAsset", true);
				oLine.save();
				cant++;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}		
		return "Se han agregado "+cant+" lineas a la orden";
	}	//	doIt
}	//	Replenish
