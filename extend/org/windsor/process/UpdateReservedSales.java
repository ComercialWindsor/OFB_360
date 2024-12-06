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
package org.windsor.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	
 *	
 */
public class UpdateReservedSales extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_COrder_ID;
	private int p_MRequisiton_ID;
			
	protected void prepare()
	{	
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("C_Order_ID"))
			{
				p_COrder_ID = para[i].getParameterAsInt();
			}
			else if (name.equals("M_Requisition_ID"))
			{
				p_MRequisiton_ID = para[i].getParameterAsInt();
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
		//codigo de ordenes
		String sqlOrder = "SELECT * FROM C_OrderLine col" +
				" INNER JOIN C_Order co ON (col.C_Order_ID = co.C_Order_ID)" +				
				" WHERE IssoTrx = 'Y' AND col.qtyReserved <> 0 ";
		if (p_COrder_ID > 0)
			sqlOrder = sqlOrder + " AND co.C_Order_ID = "+p_COrder_ID;
	
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sqlOrder,get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				BigDecimal qty = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(movementQty)" +
						" FROM M_InOutLine iol" +
						" INNER JOIN M_InOut io ON (iol.M_InOut_ID = io.M_InOut_ID)" +
						" WHERE DocStatus IN ('CO','CL') AND C_OrderLine_ID = ?",rs.getInt("C_OrderLine_ID"));
				//se calcula diferencia
				BigDecimal qtyDif = rs.getBigDecimal("QtyOrdered").subtract(qty);
				//se actualiza reservado
				DB.executeUpdate("UPDATE C_OrderLine SET QtyReserved = " +qtyDif+
						" WHERE C_OrderLine_ID = "+rs.getInt("C_OrderLine_ID"),get_TrxName());
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		//codigo de solicitudes
		String sqlReq = "SELECT * FROM M_RequisitionLine col" +
			" INNER JOIN M_Requisition co ON (col.M_Requisition_ID = co.M_Requisition_ID)" +				
			" WHERE IssoTrx = 'Y' AND col.qtyReserved <> 0 AND liberada <> 'Y' ";
		if(p_MRequisiton_ID > 0)
			sqlReq = sqlReq +" AND co.M_Requisition_ID = "+p_MRequisiton_ID; 
		
		PreparedStatement pstmtR = null;
		try
		{
			pstmtR = DB.prepareStatement(sqlReq,get_TrxName());
			ResultSet rsR = pstmtR.executeQuery();
			while(rsR.next())
			{
				BigDecimal qty = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(QtyOrdered)" +
						" FROM C_OrderLine co" +
						" INNER JOIN C_Order col ON (col.C_Order_ID = co.C_Order_ID)" +
						" WHERE DocStatus IN ('CO','CL','IP') AND M_RequisitionLine_ID = ?",rsR.getInt("M_RequisitionLine_ID"));
				//se calcula diferencia
				BigDecimal qtyDif = rsR.getBigDecimal("Qty").subtract(qty);
				//se actualiza reservado
				DB.executeUpdate("UPDATE M_RequisitionLine SET QtyReserved = " +qtyDif+
						" WHERE M_RequisitionLine_ID = "+rsR.getInt("M_RequisitionLine_ID"),get_TrxName());
			}
			rsR.close();
			pstmtR.close();
			pstmtR = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "Procesado";
	}	//	doIt
}	//	Replenish
