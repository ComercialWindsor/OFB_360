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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MLocator;
import org.compiere.model.MProduct;
import org.compiere.model.X_MP_OT;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 */
public class GenerateInternalUseFromOTNew extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_OT_ID;
	private int p_Warehouse_ID;
			
	protected void prepare()
	{	
		p_OT_ID = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("M_Warehouse_ID"))
			{
				p_Warehouse_ID = para[i].getParameterAsInt();
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
		X_MP_OT ot = new X_MP_OT(getCtx(), p_OT_ID, get_TrxName());
		int warehouse_ID = 0;
		if(ot.get_ValueAsInt("M_Inventory_ID") > 0)
		{
			new AdempiereException("Consumo YA Generado");
			return "";
		}
		else
		{	
			int cant = 0;
			String sql = "SELECT otr.M_Product_ID, otr.ResourceQty, ot.A_Asset_ID " +
			"FROM MP_OT ot " +
			"INNER JOIN MP_OT_Task ott ON (ot.MP_OT_ID = ott.MP_OT_ID) " +
			"INNER JOIN MP_OT_Resource otr ON (ott.MP_OT_Task_ID = otr.MP_OT_Task_ID) " +
			"WHERE otr.M_Product_ID > 0 AND ot.MP_OT_ID = "+p_OT_ID;
			
			MInventory iUse = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String docNo = "--";
			String ret = "";
			
			try
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				rs = pstmt.executeQuery();	
				boolean flag = true;
				int id_DocType = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_DocType_ID) FROM C_DocType " +
						" WHERE DocBaseType = 'MMI' AND upper(name) like '%INVENTARIO%'");
				int id_Charge = DB.getSQLValue(get_TrxName(), " SELECT MAX(C_Charge_ID) FROM C_Charge cc " +
						" INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) WHERE ct.value = 'TCIU'");
				
				//ininoles se setean bodega con reglas de negocio 15-11-2019
				warehouse_ID = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(ml.M_Warehouse_ID),0)" +
						" FROM M_Locator ml" +
						" INNER JOIN M_Storage ms ON (ml.M_Locator_ID = ms.M_Locator_ID)" +
						" WHERE ml.AD_Org_ID = " + ot.getAD_Org_ID()+
						" AND ms.qtyonhand > 0 AND ms.M_Product_ID IN (SELECT otr.M_Product_ID" +
						" FROM MP_OT_Task ott" +
						" INNER JOIN MP_OT_Resource otr ON (ott.MP_OT_Task_ID = otr.MP_OT_Task_ID)" +
						" WHERE otr.M_Product_ID > 0 AND ott.MP_OT_ID = "+ot.get_ID()+")");
				
				if(warehouse_ID < 0 && p_Warehouse_ID > 0)
					warehouse_ID = p_Warehouse_ID;
				
				int id_LocatorBase = DB.getSQLValue(get_TrxName(), " SELECT MAX(M_Locator_ID) FROM M_Locator ml " +
						" WHERE ml.M_Warehouse_ID = "+warehouse_ID+" AND IsDefault = 'Y'");
				if(id_LocatorBase < 1)
					id_LocatorBase = DB.getSQLValue(get_TrxName(), " SELECT MAX(M_Locator_ID) FROM M_Locator ml " +
							" WHERE ml.M_Warehouse_ID = "+warehouse_ID);
				int id_Locator = 0;
					
				PreparedStatement pstmtSt = null;
				ResultSet rsSt = null;
				pstmtSt = DB.prepareStatement (sql, get_TrxName());
				rsSt = pstmtSt.executeQuery();		
				//se analiza linea por linea para ver si hay stock
				while (rsSt.next())
				{
					if(rs.getBigDecimal("ResourceQty").compareTo(Env.ZERO) > 0
							&& rs.getInt("M_Product_ID") > 0)
					{
						id_Locator = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(ml.M_Locator_ID),0)" +
								" FROM M_Locator ml" +
								" INNER JOIN M_Storage ms ON (ml.M_Locator_ID = ms.M_Locator_ID)" +
								" WHERE ms.qtyonhand > 0 AND ml.M_Warehouse_ID = "+warehouse_ID+" AND ms.M_Product_ID ="+rs.getInt("M_Product_ID"));
						if(id_Locator <=0 && id_LocatorBase >0)
							id_Locator = id_LocatorBase ;		
						
						BigDecimal qtyEntered = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(qtyonhand) FROM M_Storage " +
								" WHERE M_Product_ID="+rs.getInt("M_Product_ID")+" AND M_Locator_ID="+id_Locator);
						BigDecimal qtyIntransit = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(ml.QtyInternalUse) FROM M_InventoryLine ml " +
								" INNER JOIN M_Inventory mi ON (mi.M_Inventory_ID = ml.M_Inventory_ID) " +
								" WHERE ml.M_Product_ID="+rs.getInt("M_Product_ID")+" AND ml.M_Locator_ID="+id_Locator+
								" AND mi.DocStatus IN ('DR','IP')");
						if(qtyEntered == null)
							qtyEntered = Env.ZERO;
						if(qtyIntransit == null)
							qtyIntransit = Env.ZERO;
						qtyEntered = qtyEntered.subtract(qtyIntransit);
						
						if(rs.getBigDecimal("ResourceQty").compareTo(qtyEntered) > 0)
						{
							flag = false;
							MProduct prod = new MProduct(getCtx(), rs.getInt("M_Product_ID"), get_TrxName());
							MLocator loc = new MLocator(getCtx(), id_Locator, get_TrxName());
							ret = "ERROR: Producto "+prod.getName()+" sin stock. Stock:"+qtyEntered+
							". Solicitado:"+rs.getBigDecimal("ResourceQty")+" en ubicaciòn "+loc.getValue();
						}						
					}
				}
				if(flag)
				{
					while (rs.next())
					{
						//se genera cabecera del consumo
						if (iUse == null)
						{
							iUse = new MInventory(getCtx(), 0, get_TrxName());
							iUse.setAD_Org_ID(ot.getAD_Org_ID());
							iUse.setM_Warehouse_ID(p_Warehouse_ID);
							iUse.setDescription("Generado desde OT "+ot.getDocumentNo());
							iUse.setMovementDate(ot.getDateTrx());					
							iUse.setC_DocType_ID(id_DocType);
							iUse.save();
						}				
						//se genera detalle del consumo
						if(rs.getBigDecimal("ResourceQty").compareTo(Env.ZERO) > 0)
						{
							MInventoryLine iLine = new MInventoryLine(getCtx(),0,get_TrxName());
							iLine.setM_Inventory_ID(iUse.get_ID());
							iLine.setAD_Org_ID(iUse.getAD_Org_ID());
							iLine.setM_Product_ID(rs.getInt("M_Product_ID"));
							iLine.setQtyInternalUse(rs.getBigDecimal("ResourceQty"));
							iLine.setQtyCount(Env.ZERO);
							iLine.setC_Charge_ID(id_Charge);
							//se busca ubicación 
							id_Locator = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(ml.M_Locator_ID),0)" +
									" FROM M_Locator ml" +
									" INNER JOIN M_Storage ms ON (ml.M_Locator_ID = ms.M_Locator_ID)" +
									" WHERE ms.qtyonhand > 0 AND ml.M_Warehouse_ID = "+warehouse_ID+" AND ms.M_Product_ID ="+rs.getInt("M_Product_ID"));
							if(id_Locator <=0 && id_LocatorBase >0)
								id_Locator = id_LocatorBase ;						
							iLine.setM_Locator_ID(id_Locator);
							iLine.save();
							try {
								iLine.set_CustomColumn("A_Asset_ID", rs.getInt("A_Asset_ID"));
								iLine.save();
							} catch (Exception e) {
								log.config("Error seteando variable A_Asset_ID "+e);
							}
							
							cant++;
						}
					}
					if(iUse != null)
					{
						DB.executeUpdate("UPDATE MP_OT SET M_Inventory_ID = "+iUse.get_ID()+" WHERE MP_OT_ID = "+ot.get_ID(), get_TrxName());
						docNo = iUse.getDocumentNo();
					}
					ret = "Se ha generado el consumo numero:"+docNo+". Y se han agregado "+cant+" lineas ";
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			/*finally
			{
				pstmt.close(); rs.close();
				pstmt=null; rs=null;
			}*/
			return ret;
		}
	}	//	doIt
}	//	Replenish
