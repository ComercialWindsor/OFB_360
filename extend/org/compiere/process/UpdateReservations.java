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

import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;


/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: UpdateReservations.java,v 1.2 2012/06/12 00:51:01  $
 */
public class UpdateReservations extends SvrProcess
{
	
	/** Properties						*/
	private Properties 		m_ctx;	
	
	private int p_Product_ID =0;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Product_ID"))
				p_Product_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		//update reservado
		int count = 0;
		PreparedStatement pstmt = null;
				try
				{
					pstmt = DB.prepareStatement("select M_Product_ID from M_Product Where producttype='I'" +
							" and isactive='Y' and ad_client_id="+this.getAD_Client_ID() + (p_Product_ID>0?"and m_product_id="+p_Product_ID:""), get_TrxName());
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
						
						updatedReserved(rs.getInt(1));
						updatedOrdered(rs.getInt(1));
						count++;
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "sql:", e);
				}
			

				commit();
				//begin update storages
				return "Actualizados " + count + " Productos";
		
	}	//	doIt

public void updatedReserved(int Product_ID){
		
		PreparedStatement pstmt = null;
		String mysql="select sum(ol.qtyreserved),ol.m_warehouse_id from" +
				" c_orderline ol inner join c_order o on (ol.c_order_id=o.c_order_id)" +
				" where o.docstatus IN ('CO','CL') and o.issotrx='Y' and ol.m_product_id="+Product_ID +
				" and ol.qtyreserved>=0 group by ol.m_warehouse_id";//ininoles se agrega condicion = para que actualize aun cuando la sumatoria sea 0
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{				
				OFBReserved(rs.getInt(2), Product_ID, rs.getBigDecimal(1));
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, mysql, e);
		}
		
	}
	
	public void updatedOrdered(int Product_ID){
		
		PreparedStatement pstmt = null;
		String mysql="select sum(ol.qtyreserved),ol.m_warehouse_id from" +
				" c_orderline ol inner join c_order o on (ol.c_order_id=o.c_order_id)" +
				" where o.docstatus IN ('CO','CL') and o.issotrx='N' and ol.m_product_id="+Product_ID +
				" and ol.qtyreserved>=0 group by ol.m_warehouse_id";
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				OFBOrdered(rs.getInt(2), Product_ID, rs.getBigDecimal(1));
				
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, mysql, e);
		}
		
	}
	
	/** faaguilar OFB
	 * reservations new table*/
	public void OFBReserved(int M_WareHouse_ID, int M_Product_ID, BigDecimal reserved)
	{
		PreparedStatement pstmt = null;
		
		String mysql="SELECT * from M_StorageReservation where M_WareHouse_ID = ? and M_Product_ID = ?";
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1, M_WareHouse_ID);
			pstmt.setInt(2, M_Product_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				X_M_StorageReservation storage = new X_M_StorageReservation (getCtx(),rs,get_TrxName() );
					storage.setQtyReserved(reserved);
					storage.save();
			}
			else{
				X_M_StorageReservation storage = new X_M_StorageReservation (getCtx(),0,get_TrxName() );
				storage.setQtyReserved(reserved);
				storage.setQtyOrdered(Env.ZERO);
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
	
	
	public void OFBOrdered(int M_WareHouse_ID, int M_Product_ID, BigDecimal ordered)
	{
		PreparedStatement pstmt = null;
		
		String mysql="SELECT * from M_StorageReservation where M_WareHouse_ID = ? and M_Product_ID = ?";
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1, M_WareHouse_ID);
			pstmt.setInt(2, M_Product_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				X_M_StorageReservation storage = new X_M_StorageReservation (getCtx(),rs,get_TrxName() );
					storage.setQtyOrdered(ordered);
					storage.save();
			}
			else{
				X_M_StorageReservation storage = new X_M_StorageReservation (getCtx(),0,get_TrxName() );
				storage.setQtyOrdered(ordered);
				storage.setQtyReserved(Env.ZERO);
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
}	//	CopyOrder
