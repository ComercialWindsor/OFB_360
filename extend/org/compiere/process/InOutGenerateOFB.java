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
import java.util.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Generate Shipments.
 *	Manual or Automatic
 *	
 *  @author Fabian Aguilar
 *  @version $Id: InOutGenerateOFB.java,v 1.0 2011/11/23 $
 */
public class InOutGenerateOFB extends SvrProcess
{
	/**	Manual Selection		*/
	private boolean 	p_Selection = false;
	/** Warehouse				*/
	private int			p_M_Warehouse_ID = 0;
	/** BPartner				*/
	private int			p_C_BPartner_ID = 0;
	/** Promise Date			*/
	private Timestamp	p_DatePromised = null;
	/** Include Orders w. unconfirmed Shipments	*/
	private boolean		p_IsUnconfirmedInOut = false;
	/** DocAction				*/
	private String		p_docAction = DocAction.ACTION_Complete;
	/** Consolidate				*/
	private boolean		p_ConsolidateDocument = true;
    /** Shipment Date                       */
	private Timestamp       p_DateShipped = null;
	
	/**	The current Shipment	*/
	private MInOut 		m_shipment = null;
	/** Numner of Shipments		*/
	private int			m_created = 0;
	/**	Line Number				*/
	private int			m_line = 0;
	/** Movement Date			*/
	private Timestamp	m_movementDate = null;
	/**	Last BP Location		*/
	private int			m_lastC_BPartner_Location_ID = -1;

	/** The Query sql			*/
	private String 		m_sql = null;

	
	/** Storages temp space				*/
	private HashMap<SParameter,MStorage[]> m_map = new HashMap<SParameter,MStorage[]>();
	/** Last Parameter					*/
	private SParameter		m_lastPP = null;
	/** Last Storage					*/
	private MStorage[]		m_lastStorages = null;
	
	/** Msg*/
	
	private String msg = "";

	
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
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("DatePromised"))
				p_DatePromised = (Timestamp)para[i].getParameter();
			else if (name.equals("Selection"))
				p_Selection = "Y".equals(para[i].getParameter());
			else if (name.equals("IsUnconfirmedInOut"))
				p_IsUnconfirmedInOut = "Y".equals(para[i].getParameter());
			else if (name.equals("ConsolidateDocument"))
				p_ConsolidateDocument = "Y".equals(para[i].getParameter());
			else if (name.equals("DocAction"))
				p_docAction = (String)para[i].getParameter();
			else if (name.equals("MovementDate"))
                p_DateShipped = (Timestamp)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
			//  juddm - added ability to specify a shipment date from Generate Shipments
			if (p_DateShipped == null) {
				m_movementDate = Env.getContextAsDate(getCtx(), "#Date");
				if (m_movementDate == null)
					m_movementDate = new Timestamp(System.currentTimeMillis());
			} else
				m_movementDate = p_DateShipped;
			//	DocAction check
			if (!DocAction.ACTION_Complete.equals(p_docAction))
				p_docAction = DocAction.ACTION_Prepare;
		}
	}	//	prepare

	/**
	 * 	Generate Shipments
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("Selection=" + p_Selection
			+ ", M_Warehouse_ID=" + p_M_Warehouse_ID 
			+ ", C_BPartner_ID=" + p_C_BPartner_ID 
			+ ", Consolidate=" + p_ConsolidateDocument
			+ ", IsUnconfirmed=" + p_IsUnconfirmedInOut
			+ ", Movement=" + m_movementDate);
		
		if (p_M_Warehouse_ID == 0)
			throw new AdempiereUserError("@NotFound@ @M_Warehouse_ID@");
		
		if (p_Selection)	//	VInOutGen
		{
			m_sql = "SELECT C_Order.* FROM C_Order, T_Selection "
				+ "WHERE C_Order.DocStatus='CO' AND C_Order.IsSOTrx='Y' AND C_Order.AD_Client_ID=? "
				+ "AND C_Order.C_Order_ID = T_Selection.T_Selection_ID " 
				+ "AND T_Selection.AD_PInstance_ID=? ";
		}
		else
		{
			m_sql = "SELECT * FROM C_Order o "
				+ "WHERE DocStatus='CO' AND IsSOTrx='Y'"
				//	No Offer,POS
				+ " AND o.C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType "
					+ "WHERE DocBaseType='SOO' AND DocSubTypeSO NOT IN ('ON','OB','WR'))"
				+ "	AND o.IsDropShip='N'"
				//	No Manual
				+ " AND o.DeliveryRule<>'M'"
				//	Open Order Lines with Warehouse
				+ " AND EXISTS (SELECT * FROM C_OrderLine ol "
					+ "WHERE ol.M_Warehouse_ID=?";					//	#1
			if (p_DatePromised != null)
				m_sql += " AND TRUNC(ol.DatePromised)<=?";		//	#2
			m_sql += " AND o.C_Order_ID=ol.C_Order_ID AND ol.QtyOrdered<>ol.QtyDelivered)";
			//
			if (p_C_BPartner_ID != 0)
				m_sql += " AND o.C_BPartner_ID=?";					//	#3
		}
		m_sql += " ORDER BY M_Warehouse_ID, PriorityRule, M_Shipper_ID, C_BPartner_ID, C_BPartner_Location_ID, C_Order_ID";
	//	m_sql += " FOR UPDATE";

		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (m_sql, get_TrxName());
			int index = 1;
			if (p_Selection)
			{
				pstmt.setInt(index++, Env.getAD_Client_ID(getCtx()));
				pstmt.setInt(index++, getAD_PInstance_ID());
			}
			else	
			{
				pstmt.setInt(index++, p_M_Warehouse_ID);
				if (p_DatePromised != null)
					pstmt.setTimestamp(index++, p_DatePromised);
				if (p_C_BPartner_ID != 0)
					pstmt.setInt(index++, p_C_BPartner_ID);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, m_sql, e);
		}
		return generate(pstmt);
	}	//	doIt
	
	/**
	 * 	Generate Shipments
	 * 	@param pstmt Order Query
	 *	@return info
	 */
	private String generate (PreparedStatement pstmt)
	{
		MClient client = MClient.get(getCtx());
		try
		{
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())		//	Order
			{
				MOrder order = new MOrder (getCtx(), rs, get_TrxName());
				//	New Header different Shipper, Shipment Location
				if (!p_ConsolidateDocument 
					|| (m_shipment != null 
					&& (m_shipment.getC_BPartner_Location_ID() != order.getC_BPartner_Location_ID()
						|| m_shipment.getM_Shipper_ID() != order.getM_Shipper_ID() )))
					completeShipment();
				log.fine("check: " + order + " - DeliveryRule=" + order.getDeliveryRule());
				//
				
				boolean completeOrder = MOrder.DELIVERYRULE_CompleteOrder.equals(order.getDeliveryRule());
				boolean completeLine = MOrder.DELIVERYRULE_CompleteLine.equals(order.getDeliveryRule());
				boolean available = MOrder.DELIVERYRULE_Availability.equals(order.getDeliveryRule());
				
				//	OrderLine WHERE
				String where = " AND M_Warehouse_ID=" + p_M_Warehouse_ID;
				if (p_DatePromised != null)
					where += " AND (TRUNC(DatePromised)<=" + DB.TO_DATE(p_DatePromised, true)
						+ " OR DatePromised IS NULL)";		
				
				
				
				MOrderLine[] lines = order.getLines (where, "ORDER BY C_BPartner_Location_ID, M_Product_ID");
				exploreLines(lines,completeOrder,completeLine,available);
				
				m_line += 1000;
			}	//	while order
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, m_sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		completeShipment();
		return "@Created@ = " + m_created;
	}	//	generate
	
	/**
	 * explora las lineas de la orden
	 * verificando stock y requerimientos
	 * **/
	private void exploreLines(MOrderLine[] lines, boolean completeOrder,boolean completeLine, boolean available)
	{
		boolean error = false;
		
		if(completeOrder || completeLine) //verificacion
			for (int i = 0; i < lines.length; i++)
			{
				MOrderLine line = lines[i];
				if (line.getM_Warehouse_ID() != p_M_Warehouse_ID)
					continue;
				
				log.fine("check: " + line);
				BigDecimal onHand = Env.ZERO;
				BigDecimal toDeliver = line.getQtyOrdered()
					.subtract(line.getQtyDelivered());
				MProduct product = line.getProduct();
				//	Nothing to Deliver
				if (product != null && toDeliver.signum() == 0)
					continue;
				
				// or it's a charge - Bug#: 1603966 
				if (line.getC_Charge_ID()!=0 && toDeliver.signum() == 0)
					continue;
				
				//	Comments & lines w/o product & services
				if ((product == null || !product.isStocked())
					&& (line.getQtyOrdered().signum() == 0 	//	comments
						|| toDeliver.signum() != 0))		//	lines w/o product
				{
					if (!MOrder.DELIVERYRULE_CompleteOrder.equals(line.getParent().getDeliveryRule()))	//	printed later
						createLine (line.getParent(), line, toDeliver, null, false);
					continue;
				}
				
				if(!product.isItem())
					continue;
	
				//	Stored Product
				String MMPolicy = product.getMMPolicy();
				MStorage[] storages = getStorages(line.getM_Warehouse_ID(), 
					line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
					MClient.MMPOLICY_FiFo.equals(MMPolicy)); 
				
				//updateStorages(storages);
				BigDecimal inProcess = getInProcessW(line.getM_Product_ID(),line.getM_Warehouse_ID());
				
				for (int j = 0; j < storages.length; j++)
				{
					MStorage storage = storages[j];
					onHand = onHand.add(storage.getQtyOnHand());
					log.config("storage "+j+",id:"+ storage.getM_Locator_ID() +" QtyOnHand:"+storage.getQtyOnHand());
				}
				
				BigDecimal stock = onHand;
				onHand = onHand.subtract(inProcess);
				
				boolean fullLine = onHand.compareTo(toDeliver) >= 0
					|| toDeliver.signum() < 0;
				
				
				if( (completeOrder || completeLine ) && !fullLine)
				{
					try{
					addLog (0, line.getDateOrdered(), null, line.getParent().getDocumentNo() + ": No se posee stock para "+ line.getM_Product().getValue());
					addLog (0, line.getDateOrdered(), null, line.getParent().getDocumentNo() + ": Encontrado "+ onHand + "|Stock :"+ stock +" enProceso: "+ inProcess+" | Requerido " +toDeliver + "|"+  getReplenish(line.getM_Product_ID(), line.getM_AttributeSetInstance_ID()));
					}
					catch(Exception e){}
					error = true;
				}	
				//createLine (line.getParent(), line, toDeliver, storages, false); //crea linea		
				
			}	//	for all order lines
		
		if(error)
			return;
		
		
		for (int i = 0; i < lines.length; i++)// generacion
		{
			MOrderLine line = lines[i];
			if (line.getM_Warehouse_ID() != p_M_Warehouse_ID)
				continue;
			
			log.fine("check: " + line);
			BigDecimal onHand = Env.ZERO;
			BigDecimal toDeliver = line.getQtyOrdered()
				.subtract(line.getQtyDelivered());
			MProduct product = line.getProduct();
			//	Nothing to Deliver
			if (product != null && toDeliver.signum() == 0)
				continue;
			
			// or it's a charge - Bug#: 1603966 
			if (line.getC_Charge_ID()!=0 && toDeliver.signum() == 0)
				continue;
			
			//	Comments & lines w/o product & services
			if ((product == null || !product.isStocked())
				&& (line.getQtyOrdered().signum() == 0 	//	comments
					|| toDeliver.signum() != 0))		//	lines w/o product
			{
				if (!MOrder.DELIVERYRULE_CompleteOrder.equals(line.getParent().getDeliveryRule()))	//	printed later
					createLine (line.getParent(), line, toDeliver, null, false);
				continue;
			}
		
//			Stored Product
			/*String MMPolicy = product.getMMPolicy();
			MStorage[] storages = getStorages(line.getM_Warehouse_ID(), 
				line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
				MClient.MMPOLICY_FiFo.equals(MMPolicy)); */
			
			String MMPolicy = product.getMMPolicy();
			Integer[] storages = getLocators(line.getM_Warehouse_ID(), 
				line.getM_Product_ID(), line.getM_AttributeSetInstance_ID()); 
			
			createLine (line.getParent(), line, toDeliver, storages, false);
			
		}
		
		
	}//exploreLines
	
	
	/**************************************************************************
	 * 	Create Line
	 *	@param order order
	 *	@param orderLine line
	 *	@param qty qty
	 *	@param storages storage info
	 *	@param force force delivery
	 */
	private void createLine (MOrder order, MOrderLine orderLine, BigDecimal qty, 
			Integer[] storages, boolean force)
	{
		//	Complete last Shipment - can have multiple shipments
		if (m_lastC_BPartner_Location_ID != orderLine.getC_BPartner_Location_ID() )
			completeShipment();
		m_lastC_BPartner_Location_ID = orderLine.getC_BPartner_Location_ID();
		//	Create New Shipment
		if (m_shipment == null)
		{
			//MDocType mydoc = order.getC_DocType().getC_DocTypeShipment_ID();
			m_shipment = new MInOut (order, order.getC_DocType().getC_DocTypeShipment_ID(), m_movementDate);
			m_shipment.setM_Warehouse_ID(orderLine.getM_Warehouse_ID());	//	sets Org too
			if (order.getC_BPartner_ID() != orderLine.getC_BPartner_ID())
				m_shipment.setC_BPartner_ID(orderLine.getC_BPartner_ID());
			if (order.getC_BPartner_Location_ID() != orderLine.getC_BPartner_Location_ID())
				m_shipment.setC_BPartner_Location_ID(orderLine.getC_BPartner_Location_ID());
			if (!m_shipment.save())
				throw new IllegalStateException("Could not create Shipment");
		}
		
		//		Product
			MProduct product = orderLine.getProduct();
			product.getValue();
			
		//	Non Inventory Lines
		if (storages == null || !product.isItem() || storages.length==0)
		{
			MInOutLine line = new MInOutLine (m_shipment);
			line.setOrderLine(orderLine, 0, Env.ZERO);
			line.setQty(qty);	//	Correct UOM
			if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
				line.setQtyEntered(qty
					.multiply(orderLine.getQtyEntered())
					.divide(orderLine.getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP));
			line.setLine(m_line + orderLine.getLine());
			if (!line.save())
				throw new IllegalStateException("Could not create Shipment Line");
			log.fine(line.toString());
			return;
		}
		
		
		
		
		boolean linePerASI = false;
		if (product.getM_AttributeSet_ID() != 0)
		{
			MAttributeSet mas = MAttributeSet.get(getCtx(), product.getM_AttributeSet_ID());
			linePerASI = mas.isInstanceAttribute();
		}
		
		//	Inventory Lines
		ArrayList<MInOutLine> list = new ArrayList<MInOutLine>();
		BigDecimal toDeliver = qty;
		for (int i = 0; i < storages.length; i++)
		{
			int storage = storages[i];
			log.config("storage "+i+",id:"+ storage );
			BigDecimal deliver = toDeliver;
			
			MInOutLine line = null;
			
			
			if (deliver.signum() == 0)	//	zero deliver
				continue;
			int M_Locator_ID = storage;
			
			
			BigDecimal inProcess = getInProcess(orderLine.getM_Product_ID(),M_Locator_ID);
			BigDecimal total = getTotalLocator(M_Locator_ID,orderLine.getM_Product_ID(),orderLine.getM_AttributeSetInstance_ID() );
			BigDecimal totalEff = total.subtract(inProcess);
			//
			if(totalEff.signum()<=0)
				continue;
			
			if (!linePerASI)	//	find line with Locator
			{
				for (int ll = 0; ll < list.size(); ll++)
				{
					MInOutLine test = (MInOutLine)list.get(ll);
					if (test.getM_Locator_ID() == M_Locator_ID)
					{
						line = test;
						break;
					}
				}
			}
			
			if (line == null)	//	new line
			{
				line = new MInOutLine (m_shipment);
				line.setOrderLine(orderLine, M_Locator_ID, order.isSOTrx() ? deliver : Env.ZERO);
				
				line.set_CustomColumn("QtyNeeded", qty);//faaguilar OFB
				
				
				if(totalEff.compareTo(deliver)<0)
					deliver = totalEff;
				
				if(totalEff.compareTo(qty)>=0)
					deliver = toDeliver;
				
				
				line.setQty(deliver);
				line.set_CustomColumn("QtyFound", totalEff);//faaguilar OFB
				line.set_CustomColumn("QtyCountR", totalEff);//faaguilar OFB
				
				//faaguilar OFB begin
				String pickupDesc="";
				if(i+1<storages.length){
					MLocator locTemp = MLocator.get(getCtx(), storages[i+1]);
					pickupDesc+= locTemp.getValue() + " C:"+ getTotalLocator(locTemp.getM_Locator_ID(),orderLine.getM_Product_ID(),orderLine.getM_AttributeSetInstance_ID() );
				
				}
				if(i+2<storages.length){
					MLocator locTemp = MLocator.get(getCtx(), storages[i+2]);
					pickupDesc+= "/ " + locTemp.getValue() + " C:"+ getTotalLocator(locTemp.getM_Locator_ID(),orderLine.getM_Product_ID(),orderLine.getM_AttributeSetInstance_ID() );
				}
				
				line.set_CustomColumn("PickupDesc", pickupDesc);
				//faaguilar OFB end
					
				list.add(line);
				log.config("new line qty="+deliver+" locator:"+M_Locator_ID);
				
				
			}
			else{				//	existing line
				line.setQty(line.getMovementQty().add(deliver));
				log.config("old add line qty="+deliver+" locator:"+M_Locator_ID);
			}
			if (orderLine.getQtyEntered().compareTo(orderLine.getQtyOrdered()) != 0)
				line.setQtyEntered(line.getMovementQty().multiply(orderLine.getQtyEntered())
					.divide(orderLine.getQtyOrdered(), 12, BigDecimal.ROUND_HALF_UP));
			line.setLine(m_line + orderLine.getLine());
			if (linePerASI)
				line.setM_AttributeSetInstance_ID(orderLine.getM_AttributeSetInstance_ID());
			if (!line.save())
				throw new IllegalStateException("Could not create Shipment Line");
			log.config("ToDeliver=" + qty + "/" + deliver + " - " + line);
			toDeliver = toDeliver.subtract(deliver);
			//	Temp adjustment
			
			//
			if (toDeliver.signum() == 0)
				break;
		}		
		if (toDeliver.signum() != 0)
			throw new IllegalStateException("Not All Delivered - Remainder=" + toDeliver);
	}	//	createLine

	
	/**
	 * 	Get Storages
	 *	@param M_Warehouse_ID
	 *	@param M_Product_ID
	 *	@param M_AttributeSetInstance_ID
	 *	@param FiFo
	 *	@return storages
	 */
	private MStorage[] getStorages(int M_Warehouse_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		boolean FiFo)
	{
		
		//
		m_lastStorages = null;
				
		
			ArrayList<MStorage> list = new ArrayList<MStorage>();
			String sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
				+ "s.AD_Client_ID,s.AD_Org_ID,s.IsActive,s.Created,s.CreatedBy,s.Updated,s.UpdatedBy,"
				+ "s.QtyOnHand,s.QtyReserved,s.QtyOrdered,s.DateLastInventory "
				+ "FROM M_Storage s"
				+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID) "
				+ "WHERE s.QtyOnHand> 0 and l.M_Warehouse_ID=?" 
				+ " AND s.M_Product_ID=? ";
			
			sql+= " And l.M_Locator_ID IN "+ getPositiveLocators(M_Warehouse_ID,M_Product_ID,M_AttributeSetInstance_ID) ;
			
				if(M_AttributeSetInstance_ID>0)
					sql+= " AND COALESCE(s.M_AttributeSetInstance_ID,0)=? ";
				
				sql+= " ORDER BY l.PriorityNo Desc, s.QtyOnHand- (select coalesce(sum(loc.movementqty),0) from m_inoutline loc "
					+ "inner join m_inout i on (i.m_inout_id=loc.m_inout_id) "
					+ "where i.DocStatus='IP' and loc.m_product_id= s.M_Product_ID "
					+ " and loc.m_locator_id=l.m_locator_id) Asc";
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try
				{
					pstmt = DB.prepareStatement(sql, get_TrxName());
					pstmt.setInt(1, M_Warehouse_ID);
					pstmt.setInt(2, M_Product_ID);
					if (M_AttributeSetInstance_ID>0)
						pstmt.setInt(3, M_AttributeSetInstance_ID);
					
					rs = pstmt.executeQuery();
					while (rs.next())
						list.add (new MStorage (getCtx(), rs, get_TrxName()));
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, sql, e);
				}
				finally
				{
					DB.close(rs, pstmt);
					rs = null; pstmt = null;
				}
				log.config("sql:"+sql);
				MStorage[] retValue = new MStorage[list.size()];
				list.toArray(retValue);
			
			
			m_lastStorages = retValue;
			
		
		
		return m_lastStorages;
	}	//	getStorages
	
	
	/**
	 * 	Get Storages
	 *	@param M_Product_ID
	 *	@param M_AttributeSetInstance_ID
	 *	@return msg
	 */
	private String getReplenish(int M_Product_ID, int M_AttributeSetInstance_ID)
	{
		
		//
		String msg=" R : ";
		int count = 0;
		int M_Warehouse_ID = DB.getSQLValue(get_TrxName(), "select M_Warehouse_ID from M_Warehouse where ISINTRANSIT='Y' and AD_Client_ID=" + this.getAD_Client_ID());	
		if(M_Warehouse_ID<=0)
			return "";
		
			String sql = "SELECT s.M_Product_ID,s.M_Locator_ID,s.M_AttributeSetInstance_ID,"
				+ "s.AD_Client_ID,s.AD_Org_ID,s.IsActive,s.Created,s.CreatedBy,s.Updated,s.UpdatedBy,"
				+ "s.QtyOnHand,s.QtyReserved,s.QtyOrdered,s.DateLastInventory,l.value "
				+ "FROM M_Storage s"
				+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID) "
				+ "WHERE s.QtyOnHand> 0 and l.M_Warehouse_ID=?" 
				+ " AND s.M_Product_ID=? ";
			
			sql+= " And l.M_Locator_ID IN "+ getPositiveLocators(M_Warehouse_ID,M_Product_ID,M_AttributeSetInstance_ID) ;
			
				if(M_AttributeSetInstance_ID>0)
					sql+= " AND COALESCE(s.M_AttributeSetInstance_ID,0)=? ";
				
				sql+= " ORDER BY l.PriorityNo Desc, s.QtyOnHand Asc";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try
				{
					pstmt = DB.prepareStatement(sql, get_TrxName());
					pstmt.setInt(1, M_Warehouse_ID);
					pstmt.setInt(2, M_Product_ID);
					if (M_AttributeSetInstance_ID>0)
						pstmt.setInt(3, M_AttributeSetInstance_ID);
					
					rs = pstmt.executeQuery();
					while (rs.next())
					{
						if(count == 2)
							continue;
						
						msg +=" "+ rs.getString("Value") + " : " + getTotalLocator(rs.getInt("M_Locator_ID"),rs.getInt("M_Product_ID"), M_AttributeSetInstance_ID);
						count++;
					}
						
						
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, sql, e);
				}
				finally
				{
					DB.close(rs, pstmt);
					rs = null; pstmt = null;
				}
				log.config("sql:"+sql);
			
		
		
		return msg;
	}	//	getStorages
	
	
	
	
	private String getPositiveLocators(int M_Warehouse_ID, 
			int M_Product_ID, int M_AttributeSetInstance_ID){
		
		String out="(0 ";
		
		String sql = "SELECT Sum(s.QtyOnHand),s.M_Locator_ID,l.PriorityNo "
			+ "FROM M_Storage s"
			+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID) "
			+ "WHERE l.M_Warehouse_ID=? " 
			+ " AND s.M_Product_ID=? ";
		
			if(M_AttributeSetInstance_ID>0)
				sql+= " AND COALESCE(s.M_AttributeSetInstance_ID,0)=? ";
			
			sql+=" Group by s.M_Locator_ID ,l.PriorityNo  Having Sum(s.QtyOnHand)>0 ";
			
			sql+= " ORDER BY l.PriorityNo Desc, Sum(s.QtyOnHand) Asc";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, M_Warehouse_ID);
				pstmt.setInt(2, M_Product_ID);
				if (M_AttributeSetInstance_ID>0)
					pstmt.setInt(3, M_AttributeSetInstance_ID);
				
				rs = pstmt.executeQuery();
				while (rs.next())
					out+= ","+ rs.getInt(2);
					
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
			
			out+=",0)";
			
			
			if(out.equals("(0, ,0)"))
				out="(0)";
			
			return out;
		
	}
	
	private BigDecimal getTotalLocator(int Locator_ID, int Product_ID, int ATTRIBUTESETINSTANCE_ID){
		
		String sql=" Select Sum(QtyOnhand) from M_Storage where M_Locator_ID=? and M_Product_ID=? ";
		
		if(ATTRIBUTESETINSTANCE_ID>0)
		 sql+=" and M_ATTRIBUTESETINSTANCE_ID=?";
		
		
		BigDecimal rvalue= new BigDecimal(0);
		
		if(ATTRIBUTESETINSTANCE_ID>0)
			rvalue=DB.getSQLValueBD(get_TrxName(), sql, Locator_ID,Product_ID,ATTRIBUTESETINSTANCE_ID);
		else
			rvalue=DB.getSQLValueBD(get_TrxName(), sql, Locator_ID,Product_ID);
		
		return rvalue;
		
	}
	
	private BigDecimal getInProcess(int Product_ID, int Locator_ID){
		
		String sql = "select sum(l.movementqty) from m_inoutline l "
				+"inner join m_inout i on (i.m_inout_id=l.m_inout_id) "
				+" where i.DocStatus='IP' and l.m_product_id= ? "
				+" and l.m_locator_id= ? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BigDecimal reValue = new BigDecimal(0.0);
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, Product_ID);
			pstmt.setInt(2, Locator_ID);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				reValue= rs.getBigDecimal(1);
				
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		if(reValue==null)
			reValue = new BigDecimal(0.0);
		
		return reValue;
	}
	
 private BigDecimal getInProcessW(int Product_ID, int Warehouse_ID){
		
		String sql = "select sum(l.movementqty) from m_inoutline l "
				+"inner join m_inout i on (i.m_inout_id=l.m_inout_id) "
				+" where i.DocStatus='IP' and l.m_product_id= ? "
				+" and i.M_WAREHOUSE_ID= ? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BigDecimal reValue = new BigDecimal(0.0);
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, Product_ID);
			pstmt.setInt(2, Warehouse_ID);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				reValue= rs.getBigDecimal(1);
				
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		if(reValue==null)
			reValue = new BigDecimal(0.0);
		
		return reValue;
	}

	/**
	 * 	Complete Shipment
	 */
	private void completeShipment()
	{
		if (m_shipment != null)
		{
			//	Fails if there is a confirmation
			if (!m_shipment.processIt(p_docAction))
				log.warning("Failed: " + m_shipment);
			m_shipment.save();
			//
			try{
				addLog(m_shipment.getM_InOut_ID(), m_shipment.getMovementDate(), null, "Orden OK:" + m_shipment.getC_Order().getDocumentNo() + " | Despacho: "+  m_shipment.getDocumentNo());
			}
			catch(Exception e){
				addLog(m_shipment.getM_InOut_ID(), m_shipment.getMovementDate(), null,  m_shipment.getDocumentNo());
			}
			m_created++;
			m_map = new HashMap<SParameter,MStorage[]>();
			if (m_lastPP != null && m_lastStorages != null)
				m_map.put(m_lastPP, m_lastStorages);
		}
		m_shipment = null;
		m_line = 0;
	}	//	completeOrder
	
	/**
	 * 	InOutGenerate Parameter
	 */
	class SParameter
	{
		/**
		 * 	Parameter
		 *	@param p_Warehouse_ID warehouse
		 *	@param p_Product_ID 
		 *	@param p_AttributeSetInstance_ID 
		 *	@param p_AttributeSet_ID
		 *	@param p_allAttributeInstances 
		 *	@param p_minGuaranteeDate
		 *	@param p_FiFo
		 */
		protected SParameter (int p_Warehouse_ID, 
			int p_Product_ID, int p_AttributeSetInstance_ID, int p_AttributeSet_ID,
			boolean p_allAttributeInstances, Timestamp p_minGuaranteeDate,
			boolean p_FiFo)
		{
			this.M_Warehouse_ID = p_Warehouse_ID;
			this.M_Product_ID = p_Product_ID;
			this.M_AttributeSetInstance_ID = p_AttributeSetInstance_ID; 
			this.M_AttributeSet_ID = p_AttributeSet_ID;
			this.allAttributeInstances = p_allAttributeInstances;
			this.minGuaranteeDate = p_minGuaranteeDate;
			this.FiFo = p_FiFo;	
		}
		/** Warehouse		*/
		public int M_Warehouse_ID;
		/** Product			*/
		public int M_Product_ID;
		/** ASI				*/
		public int M_AttributeSetInstance_ID;
		/** AS				*/
		public int M_AttributeSet_ID;
		/** All instances	*/
		public boolean allAttributeInstances;
		/** Mon Guarantee Date	*/
		public Timestamp minGuaranteeDate;
		/** FiFo			*/
		public boolean FiFo;

		/**
		 * 	Equals
		 *	@param obj
		 *	@return true if equal
		 */
		public boolean equals (Object obj)
		{
			if (obj != null && obj instanceof SParameter)
			{
				SParameter cmp = (SParameter)obj;
				boolean eq = cmp.M_Warehouse_ID == M_Warehouse_ID
					&& cmp.M_Product_ID == M_Product_ID
					&& cmp.M_AttributeSetInstance_ID == M_AttributeSetInstance_ID
					&& cmp.M_AttributeSet_ID == M_AttributeSet_ID
					&& cmp.allAttributeInstances == allAttributeInstances
					&& cmp.FiFo == FiFo;
				if (eq)
				{
					if (cmp.minGuaranteeDate == null && minGuaranteeDate == null)
						;
					else if (cmp.minGuaranteeDate != null && minGuaranteeDate != null
						&& cmp.minGuaranteeDate.equals(minGuaranteeDate))
						;
					else
						eq = false;
				}
				return eq;
			}
			return false;
		}	//	equals
		
		/**
		 * 	hashCode
		 *	@return hash code
		 */
		public int hashCode ()
		{
			long hash = M_Warehouse_ID
				+ (M_Product_ID * 2)
				+ (M_AttributeSetInstance_ID * 3)
				+ (M_AttributeSet_ID * 4);

			if (allAttributeInstances)
				hash *= -1;
			if (FiFo);	
				hash *= -2;
			if (hash < 0)
				hash = -hash + 7;
			while (hash > Integer.MAX_VALUE)
				hash -= Integer.MAX_VALUE;
			//
			if (minGuaranteeDate != null)
			{
				hash += minGuaranteeDate.hashCode();
				while (hash > Integer.MAX_VALUE)
					hash -= Integer.MAX_VALUE;
			}
				
			return (int)hash;
		}	//	hashCode
		
	}	//	Parameter
	
	
	/**
	 * 	Get Locators
	 *	@param M_Warehouse_ID
	 *	@param M_Product_ID
	 *	@param M_AttributeSetInstance_ID
	 *	@param FiFo
	 *	@return storages
	 */
	private Integer[] getLocators(int M_Warehouse_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID)
	{
		
		List<Integer> locators = new ArrayList<Integer>();
			
			String sql = "SELECT s.M_Product_ID,s.M_Locator_ID, "
				+ " SUM(s.QtyOnHand) as QtyOnHand"
				+ " FROM M_Storage s"
				+ " INNER JOIN M_Locator l ON (l.M_Locator_ID=s.M_Locator_ID) "
				+ " WHERE l.isactive='Y' and l.M_Warehouse_ID=? AND s.M_Product_ID=? " ;
			if(M_AttributeSetInstance_ID>0)
				sql+= " AND COALESCE(s.M_AttributeSetInstance_ID,0)=? ";
			
			sql+="   Group by s.M_Product_ID,s.M_Locator_ID" +
				"  Having SUM(s.QtyOnHand)>0 " +
				"  order by 3 asc";
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try
				{
					pstmt = DB.prepareStatement(sql, get_TrxName());
					pstmt.setInt(1, M_Warehouse_ID);
					pstmt.setInt(2, M_Product_ID);
					if (M_AttributeSetInstance_ID>0)
						pstmt.setInt(3, M_AttributeSetInstance_ID);
					
					rs = pstmt.executeQuery();
					while (rs.next())
						locators.add (rs.getInt(2));
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, sql, e);
				}
				finally
				{
					DB.close(rs, pstmt);
					rs = null; pstmt = null;
				}
				log.config("sql:"+sql);
				Integer[] retValue = new Integer[locators.size()];
				locators.toArray(retValue);	
		
		return retValue;
	}	//	getLocators

	
}	//	InOutGenerate
