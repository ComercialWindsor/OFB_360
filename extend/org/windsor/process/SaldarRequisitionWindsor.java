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
package org.windsor.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MRequisitionLine;
import org.compiere.model.MStorage;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_M_StorageReservation;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class SaldarRequisitionWindsor extends SvrProcess
{
	/**	The Order				*/
	private int		p_M_Requisition_ID = 0;
	private int		p_M_RequisitionLine_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_M_Requisition_ID = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_RequisitionLine_ID"))
				p_M_RequisitionLine_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		String sql = "SELECT * FROM M_RequisitionLine WHERE IsActive = 'Y' AND M_Requisition_ID = "+p_M_Requisition_ID;
		if(p_M_RequisitionLine_ID > 0)
			sql = sql + " AND M_RequisitionLine_ID = "+p_M_RequisitionLine_ID;
		PreparedStatement pstmt = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				MRequisitionLine rLine = new MRequisitionLine(getCtx(), rs, get_TrxName());
				BigDecimal qtyDif = rLine.getQty().subtract((BigDecimal)rLine.get_Value("QtyUsed"));				
				if(qtyDif != null && qtyDif.compareTo(Env.ZERO) > 0)
				{
					//se desreserva cantidad
					MWarehouse wh = MWarehouse.get(rLine.getCtx(), rLine.getM_Requisition().getM_Warehouse_ID());
					if(existReservationTable(rLine.get_TrxName()))
					{
						OFBReservation(wh.get_ID(), rLine.getM_Product_ID(),Env.ZERO, qtyDif.negate());
						MStorage.add(wh.getCtx(), wh.getM_Warehouse_ID(),wh.getDefaultLocator().getM_Locator_ID(), 
								rLine.getM_Product_ID(), 
							rLine.getM_AttributeSetInstance_ID(), rLine.getM_AttributeSetInstance_ID(),
							Env.ZERO, qtyDif.negate(), Env.ZERO, rLine.get_TrxName());
					}
					else
					{							
						MStorage.add(wh.getCtx(), wh.getM_Warehouse_ID(),wh.getDefaultLocator().getM_Locator_ID(), 
								rLine.getM_Product_ID(), 
							rLine.getM_AttributeSetInstance_ID(), rLine.getM_AttributeSetInstance_ID(),
							Env.ZERO, qtyDif.negate(), Env.ZERO, rLine.get_TrxName());
					}
					//se actualiza cantidad usada
					DB.executeUpdate("UPDATE M_RequisitionLine SET QtyUsed = Qty, QtyReserved = 0 " +
							" WHERE M_RequisitionLine_ID = "+rs.getInt("M_RequisitionLine_ID"), get_TrxName());
				}
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "actualizado";
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
					//storage.setQtyOrdered(storage.getQtyOrdered().subtract(ordered));
					storage.save();
			}
			else{
				X_M_StorageReservation storage = new X_M_StorageReservation (getCtx(),0,get_TrxName() );
				storage.setQtyReserved(reserved.negate());
				//storage.setQtyOrdered(ordered);
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

}	//	CopyFromOrder
