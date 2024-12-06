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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.util.*;
import org.compiere.model.*;
 
/**
 *	update storages
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: UpdateTrxs.java,v 1.2 2011/03/04 00:51:02 $
 */
public class UpdateTrxs extends SvrProcess
{
	/** Properties						*/
	private Properties 		m_ctx;	
	
	private int p_Product_ID =0;

	protected static final int CMINUS_CO = 1;//Entregas A Cliente Completadas
	protected static final int CMINUS_VO = 3;//Entregas A Cliente Reversados
	protected static final int CMINUS_RE = 2;//Entregas A Cliente Reverso
	protected static final int VPLUS_CO = 4;//Recibos Proveedor Completados
	protected static final int VPLUS_VO = 6;//Recibos de Proveedor Reversados
	protected static final int VPLUS_RE = 5; //Recibos de Proveedor Reverso
	protected static final int UPLUS_RE = 8; //Reverso uso interno
	protected static final int UMINUS_CO = 7; //Uso interno completado
	
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
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		int fixes = 0;
		int errors = 0;
		
		String mysql="select * from OFB_Det_Transaction where 1=1 ";
		if(p_Product_ID>0)
			mysql+=" and M_Product_ID=?";
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			if(p_Product_ID>0)
			pstmt.setInt(1, p_Product_ID);
			ResultSet rs = pstmt.executeQuery();
			BigDecimal qty=Env.ZERO;
			while (rs.next())
			{
				
				if(rs.getInt("M_InoutLine_ID")>0)//Entregas y Recibos
				{
					MTransaction trans[]=getTransactions(rs.getInt("M_InoutLine_ID"),0,0,rs.getInt("M_Product_ID"));
					for(int i=0;i<trans.length;i++)
						trans[i].deleteEx(true);
					
					MInOutLine sLine= new MInOutLine(m_ctx,rs.getInt("M_InoutLine_ID"),get_TrxName());
					MTransaction mtrx = null;
					boolean created = false;
					if (sLine.getM_AttributeSetInstance_ID() == 0)
					{
						MInOutLineMA mas[] = MInOutLineMA.get(getCtx(),
							sLine.getM_InOutLine_ID(), get_TrxName());
						for (int j = 0; j < mas.length; j++)
						{
							MInOutLineMA ma = mas[j];
							BigDecimal QtyMA = ma.getMovementQty();
							if (rs.getString("MovementType").charAt(1) == '-')
								QtyMA = QtyMA.negate();
								
						//					Create Transaction
							mtrx = new MTransaction (getCtx(), sLine.getAD_Org_ID(),
									rs.getString("MovementType"), sLine.getM_Locator_ID(),
									sLine.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
									QtyMA, sLine.getParent().getMovementDate(), get_TrxName());
								mtrx.setM_InOutLine_ID(sLine.getM_InOutLine_ID());
								if (!mtrx.save())
								  errors++;
								else
								  fixes ++;
								
							created = true;
						}
					}
					
					if(!created)
					{
						BigDecimal Qty =  sLine.getMovementQty();
						if (rs.getString("MovementType").charAt(1) == '-')
							Qty = Qty.negate();
						
						mtrx = new MTransaction (getCtx(), sLine.getAD_Org_ID(),
								rs.getString("MovementType"), sLine.getM_Locator_ID(),
								sLine.getM_Product_ID(), sLine.getM_AttributeSetInstance_ID(),
								Qty, sLine.getParent().getMovementDate(), get_TrxName());
							mtrx.setM_InOutLine_ID(sLine.getM_InOutLine_ID());
							if (!mtrx.save())
								  errors++;
								else
								  fixes ++;
					}					
				    
				} // FIN /Entregas y Recibos
				
				
				if(rs.getInt("m_inventoryline_id")>0 && (rs.getInt("tipo")==7 || rs.getInt("tipo")==8 ) )//Usos Interno
				{
					MTransaction trans[]=getTransactions(0,0,rs.getInt("m_inventoryline_id"),rs.getInt("M_Product_ID"));
					for(int i=0;i<trans.length;i++)
						trans[i].deleteEx(true);
					
					MInventoryLine line= new MInventoryLine(m_ctx,rs.getInt("m_inventoryline_id"),get_TrxName());
					MTransaction mtrx = null;
					String movementType = line.getQtyInternalUse().signum()>0?"I-":"I+";
					boolean created = false;
					
					if (line.getM_AttributeSetInstance_ID() == 0 )
					{
						MInventoryLineMA mas[] = MInventoryLineMA.get(getCtx(),
								line.getM_InventoryLine_ID(), get_TrxName());

						for (int j = 0; j < mas.length; j++)
						{
							MInventoryLineMA ma = mas[j];
							BigDecimal QtyMA = ma.getMovementQty().abs();
							if (movementType.charAt(1) == '-')
								QtyMA = QtyMA.negate();
								
						//					Create Transaction
							mtrx = new MTransaction (getCtx(), line.getAD_Org_ID(),
									movementType, line.getM_Locator_ID(),
									line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
									QtyMA, line.getParent().getMovementDate(), get_TrxName());
							mtrx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
							if (!mtrx.save())
								  errors++;
								else
								  fixes ++;
							
							created = true;
						}
						
					}
					
					if(!created)
					{
						BigDecimal Qty =  line.getQtyInternalUse().abs();
						if (movementType.charAt(1) == '-')
							Qty = Qty.negate();
						
						mtrx = new MTransaction (getCtx(), line.getAD_Org_ID(),
								movementType, line.getM_Locator_ID(),
								line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
								Qty, line.getParent().getMovementDate(), get_TrxName());
						mtrx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
							if (!mtrx.save())
								  errors++;
								else
								  fixes ++;
					}
					
				}// FIN Usos Interno
				
				
				if(rs.getInt("m_inventoryline_id")>0 && (rs.getInt("tipo")==9 || rs.getInt("tipo")==10 ) )//inventarios completos
				{
					MTransaction trans[]=getTransactions(0,0,rs.getInt("m_inventoryline_id"),rs.getInt("M_Product_ID"));
					for(int i=0;i<trans.length;i++)
						trans[i].deleteEx(true);
					
					MInventoryLine line= new MInventoryLine(m_ctx,rs.getInt("m_inventoryline_id"),get_TrxName());
					MTransaction mtrx = null;
					String movementType = line.getQtyCount().compareTo(line.getQtyBook()) >=0?"I+":"I-";
					boolean created = false;
					if (line.getM_AttributeSetInstance_ID() == 0 )
					{
						MInventoryLineMA mas[] = MInventoryLineMA.get(getCtx(),
								line.getM_InventoryLine_ID(), get_TrxName());

						for (int j = 0; j < mas.length; j++)
						{
							MInventoryLineMA ma = mas[j];
							BigDecimal QtyMA = ma.getMovementQty().abs();
							if (movementType.charAt(1) == '-')
								QtyMA = QtyMA.negate();
								
						//					Create Transaction
							mtrx = new MTransaction (getCtx(), line.getAD_Org_ID(),
									movementType, line.getM_Locator_ID(),
									line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
									QtyMA, line.getParent().getMovementDate(), get_TrxName());
							mtrx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
							if (!mtrx.save())
								  errors++;
								else
								  fixes ++;
							
							created = true;
						}
						
					}
					
					if(!created)
					{
						BigDecimal Qty =  line.getQtyCount().subtract(line.getQtyBook());
						
						mtrx = new MTransaction (getCtx(), line.getAD_Org_ID(),
								movementType, line.getM_Locator_ID(),
								line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
								Qty, line.getParent().getMovementDate(), get_TrxName());
						mtrx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
							if (!mtrx.save())
								  errors++;
								else
								  fixes ++;
					}
				}//fin inventarios completos
				
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, mysql, e);
		}
		

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
		
		return " Fixed "+ fixes + " Errors " + errors;
	}	//	doIt
	
	
	public MTransaction[] getTransactions(int inoutline_id, int movementline_id, int inventoryline_id, int product_id){
		
		String mysql="select * from M_Transaction where  m_product_id=? ";
		
		if(inventoryline_id>0)
			mysql+=" and  m_inventoryline_id=? ";
		if(movementline_id>0)
			mysql+=" and m_movementline_id=?";
		if(inoutline_id>0)
			mysql+=" and m_inoutline_id=?";
		
		PreparedStatement pstmt = null;
		ArrayList<MTransaction> list = new ArrayList<MTransaction> ();
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1, product_id);
			if(inventoryline_id>0)
				pstmt.setInt(2, inventoryline_id);
			if(movementline_id>0)
				pstmt.setInt(2, movementline_id);
			if(inoutline_id>0)
				pstmt.setInt(2, inoutline_id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MTransaction line= new MTransaction(this.getCtx(),rs,get_TrxName());
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
		
		MTransaction[] lines = new MTransaction[list.size ()];
		list.toArray (lines);
		return lines;
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
	
	
}	//	InvoiceCreateInOut
