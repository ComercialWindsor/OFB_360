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

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MStorage;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_M_StorageReservation;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 */
public class UpdateSalesReqStock extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_COrder_ID;
			
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
		int count = 0;
		if(p_COrder_ID > 0)
		{
			MOrder order = new MOrder(getCtx(), p_COrder_ID, get_TrxName());
			if(!order.get_ValueAsBoolean("IsSalesReversed"))
			{
				MOrderLine[] oLines = order.getLines(false, null);
				for (int i = 0; i < oLines.length; i++)
				{
					MOrderLine line = oLines[i];
					if(line.get_ValueAsInt("M_RequisitionLine_ID") > 0)
					{
						//actualizacion de usado y reservado de la linea de solicitud
						DB.executeUpdate("UPDATE M_RequisitionLine SET QtyUsed = QtyUsed-"+line.getQtyEntered()+","+
								" QtyReserved = QtyReserved+"+line.getQtyEntered()+
								" WHERE M_RequisitionLine_ID = "+line.get_ValueAsInt("M_RequisitionLine_ID"), get_TrxName());
						//actualizacion de reserva en bodega (debe sumarse la cantidad a reservado antes rebajado)
						MWarehouse wh = new MWarehouse(getCtx(), order.getM_Warehouse_ID(), get_TrxName());
						if(existReservationTable(order.get_TrxName()))
						{
							//no se agrega reservado ya que se hizo con la solicitud
							OFBReservation(order.getM_Warehouse_ID(), line.getM_Product_ID(),Env.ZERO, line.getQtyOrdered());
							//siempre semodifica para el storage nativo
							
							MStorage.add(getCtx(), order.getM_Warehouse_ID(),wh.getDefaultLocator().getM_Locator_ID(), 
									line.getM_Product_ID(),line.getM_AttributeSetInstance_ID(), line.getM_AttributeSetInstance_ID(),
								Env.ZERO, line.getQtyOrdered(), Env.ZERO, order.get_TrxName());
						}
						else
						{							
							MStorage.add(wh.getCtx(), wh.getM_Warehouse_ID(),wh.getDefaultLocator().getM_Locator_ID(), 
									line.getM_Product_ID(),line.getM_AttributeSetInstance_ID(), line.getM_AttributeSetInstance_ID(),
								Env.ZERO, line.getQtyOrdered(), Env.ZERO, order.get_TrxName());
						}
					}
					count++;
				}			
				DB.executeUpdate("UPDATE C_Order SET IsSalesReversed = 'Y' " +
						" WHERE C_Order_ID = "+order.get_ID(),get_TrxName());
			}
			else
			{
				return "ERROR: Ya se ha devuelto la reserva de solicitud en esta nota de venta";
			}
		}
		return "Se ha actualizado "+count+" lineas de la orden de venta";
	}	//	doIt
	
	public void OFBReservation(int M_WareHouse_ID, int M_Product_ID, BigDecimal ordered, BigDecimal reserved)
	{
		PreparedStatement pstmt = null;
		
		String mysql="SELECT * from M_StorageReservation where M_WareHouse_ID = ? and M_Product_ID = ?";
		try
		{
			pstmt = DB.prepareStatement(mysql,get_TrxName());
			pstmt.setInt(1, M_WareHouse_ID);
			pstmt.setInt(2, M_Product_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				X_M_StorageReservation storage = new X_M_StorageReservation (getCtx(),rs,get_TrxName());
					storage.setQtyReserved(storage.getQtyReserved().subtract(reserved));
					storage.save();
			}
			else{
				X_M_StorageReservation storage = new X_M_StorageReservation (getCtx(),0,get_TrxName() );
				storage.setQtyReserved(reserved.negate());
				storage.setM_Product_ID(M_Product_ID);
				storage.setM_Warehouse_ID(M_WareHouse_ID);
				storage.save();
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}	
	public boolean existReservationTable(String Trx)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean val = false;
		
		String mysql="SELECT count(1) from M_StorageReservation";
		
		if(!DB.isOracle())
			mysql = "select count(1) from AD_Table where tablename='M_StorageReservation'";
		try
		{
			pstmt = DB.prepareStatement(mysql,Trx);
			rs = pstmt.executeQuery();
			if (rs.next())
				if(rs.getInt(1)>0)
					val = true;
			
		}
		catch (Exception e)
		{
			
			val = false;
		}
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return val;
	}
}	//	Replenish
