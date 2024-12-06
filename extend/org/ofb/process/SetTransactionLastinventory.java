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
package org.ofb.process;

import java.math.*;
import java.sql.*;
import java.util.ArrayList;
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
 *  @version $Id: SetTransactionLastinventory.java,v 1.2 2012/09/12 00:51:01  $
 */
public class SetTransactionLastinventory extends SvrProcess
{
    private Properties 		m_ctx;	
	
	private int p_Product_ID =0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	
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
		
		m_ctx = Env.getCtx();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		String mysql="select p.m_product_id "+
					"from m_product p "+ 
					"inner join m_inventoryline il  on (p.m_product_id=il.m_product_id) "+
					"inner join m_inventory i on (il.m_inventory_id=i.m_inventory_id) "+
					"where p.isactive='Y' and p.ad_client_id=? and p.ISSTOCKED='Y' "+ 
					"and p.PRODUCTTYPE='I' and i.docstatus='CO'" ;
					
		if(p_Product_ID>0)
			mysql+=" and M_Product_ID=?";
		
		mysql += " group by p.m_product_id	";
		
		
		String sql2 = "select t.T_transaction_ID,t.movementdate, t.m_inventoryline_id  "+
						"from m_transaction t  "+
						"inner join m_inventoryline l on (t.m_inventoryline_id =l.m_inventoryline_id )  "+
						"inner join m_inventory i on (il.m_inventory_id=i.m_inventory_id)  "+		
						"where t.m_product_id=? and  l.qtyinternaluse=0 and i.docstatus='CO'" +
						" order by 2 desc, 3 desc";	
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1, getAD_Client_ID());
			if(p_Product_ID>0)
				pstmt.setInt(2, p_Product_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				
				MTransaction trx= null;
				MInventoryLine ivl = null;
				
				PreparedStatement pstmt2 = DB.prepareStatement(sql2, get_TrxName());
				pstmt2.setInt(1, rs.getInt(1));
				ResultSet rs2 = pstmt.executeQuery();
				if (rs2.next())
				{
					trx = new MTransaction(Env.getCtx(), rs2.getInt(1) ,get_TrxName());
					ivl = new MInventoryLine(Env.getCtx(), rs2.getInt(3) ,get_TrxName());
				}
				rs2.close();
				pstmt2.close();
				pstmt2 = null;
				
				if(trx!=null && ivl!=null)
				{
					
					trx.setMovementQty(ivl.getQtyCount());
					trx.saveEx();
					
					DB.executeUpdateEx("update m_transaction set movementqty=0 " +
							"where m_transaction_id<? and movementdate <= ? and m_product_id=? and m_locator_id=?",
							new Object[]{trx.getM_Transaction_ID(),trx.getMovementDate(),ivl.getM_Product_ID(),ivl.getM_Locator_ID()} ,get_TrxName());
				}
				
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, mysql, e);
		}
		
		commitEx();
		
		//begin update storages
				pstmt = null;
				try
				{
					String sql ="select m_product_id,m_locator_id,M_ATTRIBUTESETINSTANCE_ID,qtyonhand,trxqty from ( "+
								"select s.m_product_id, s.m_locator_id,s.M_ATTRIBUTESETINSTANCE_ID, sum(s.qtyonhand) as qtyonhand, "+
								"(select sum(t.movementqty) from m_transaction t where t.m_product_id=s.m_product_id and t.m_locator_id=s.m_locator_id and t.M_ATTRIBUTESETINSTANCE_ID=s.M_ATTRIBUTESETINSTANCE_ID) as trxqty "+
								"from m_storage s "+
								"group by s.m_product_id, s.m_locator_id,M_ATTRIBUTESETINSTANCE_ID) "+
								"where qtyonhand<>trxqty ";
					
					if(p_Product_ID>0)
						sql +=" and M_Product_ID=" + p_Product_ID;
					
					
					int lastProduct_ID=0;
					
					pstmt = DB.prepareStatement(sql, get_TrxName());
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
						MStorage[] storages = getStorages(rs.getInt("m_product_id"), rs.getInt("m_locator_id"), rs.getInt("m_attributesetinstance_id"));
						
						if(lastProduct_ID!=rs.getInt("m_product_id"))
						{
							for (int j = 0; j < storages.length; j++)
							{
								MStorage st = storages[j];
								st.setQtyOnHand(Env.ZERO);
								st.saveEx();
							}
							
							lastProduct_ID=rs.getInt("m_product_id");
						}
						
						
						boolean fixed = false;
						for (int j = 0; j < storages.length;)
						{
							MStorage st = storages[j];
							st.setQtyOnHand(rs.getBigDecimal("trxqty") );
							st.saveEx();
							
							fixed = true;
							
						}
						
						if(!fixed)
						{
							MStorage.add(getCtx(), MLocator.get(getCtx(), rs.getInt(2)).getM_Warehouse_ID(), rs.getInt(2), rs.getInt(3),rs.getInt(4), 0, rs.getBigDecimal(1), Env.ZERO, Env.ZERO, get_TrxName());
						}
						
						
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, mysql, e);
				}
				//end update storages
				
				
				commitEx();
				
			return "ok";
	}
	
	public MStorage[] getStorages(int product_id, int locator_id, int ATTRIBUTESETINSTANCE_ID )
	{
		String mysql = "select * from m_storage where m_product_id=? and m_locator_id=? and M_ATTRIBUTESETINSTANCE_ID=?";
		
		PreparedStatement pstmt = null;
		ArrayList<MStorage> list = new ArrayList<MStorage> ();
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1, product_id);
			pstmt.setInt(2, locator_id);
			pstmt.setInt(3, ATTRIBUTESETINSTANCE_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MStorage line= new MStorage(this.getCtx(),rs,get_TrxName());
				list.add(line);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, mysql, e);
		}
		
		MStorage[] lines = new MStorage[list.size ()];
		list.toArray (lines);
		return lines;
		
	}
	
}	//	CopyOrder
