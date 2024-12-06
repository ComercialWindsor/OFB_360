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
package org.ofb.process;

import java.math.BigDecimal;

import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	GenerateReqFromInventory
 *	
 *  @author Italo Niñoles
 *  @version $Id: GenerateReqFromInventory.java,v 1.2 2015/06/09 ininoles
 *  
 */
public class GenerateReqFromInventory extends SvrProcess
{

	protected void prepare()
	{

	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int id_role = Env.getAD_Role_ID(getCtx());
		String msg = "";
		int M_Inventory_ID = getRecord_ID();
		MInventory inv = new MInventory(getCtx(), M_Inventory_ID, get_TrxName());
		
		if(inv.getDocStatus().compareToIgnoreCase("DR") ==0)
		{	
			inv.setDocStatus("L1");
			msg = "Procesado";
		}
		else if(inv.getDocStatus().compareToIgnoreCase("L1") ==0)
		{	
			if (id_role != 1000022 && id_role != 1000021 && id_role != 1000012)				
				throw new AdempiereUserError("Rol sin privilegios suficientes");
			inv.setDocStatus("L2");
			msg = "Procesado";
		}
		else if(inv.getDocStatus().compareToIgnoreCase("L2") ==0)
		{
			if (id_role != 1000021 && id_role != 1000012)				
				throw new AdempiereUserError("Rol sin privilegios suficientes");
			inv.setDocStatus("L3");
			msg = "Procesado";
		}
		else if(inv.getDocStatus().compareToIgnoreCase("L3") ==0)
		{	
			if (id_role != 1000012)				
				throw new AdempiereUserError("Rol sin privilegios suficientes");
			int idReqRe = 0;
			try
			{
				idReqRe = inv.get_ValueAsInt("M_Requisition_ID");
			}
			catch (Exception e)
			{
				log.severe("No se pudo setear campo M_Requisition_ID");
				idReqRe = 0;
			}			
			if (idReqRe > 0)
				msg = "Procesado. Solicitud ya relacionada ";
			else
			{
				MRequisition req = new MRequisition(getCtx(), 0, get_TrxName());
				//c_doctype
				int idDocType = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_DocType_ID) FROM C_DocType WHERE docbasetype = 'POR' "
						+ " AND IsActive = 'Y' AND AD_Client_ID = "+inv.getAD_Client_ID());
				if (idDocType > 0)
					req.setC_DocType_ID(idDocType);
				else
					req.setC_DocType_ID(1000018);
				//org
				req.setAD_Org_ID(inv.getAD_Org_ID());
				//pricelist
				int idPriceList = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_PriceList_ID) FROM M_PriceList WHERE IsActive = 'Y'"
						+ " AND issopricelist = 'N' AND isdefault = 'Y' AND AD_Client_ID = "+inv.getAD_Client_ID());
				if (idPriceList > 0)
					req.setM_PriceList_ID(idPriceList);
				else
					req.setC_DocType_ID(1000001);
				//user				
				int idUser = 0;
				try
				{
					idUser =inv.get_ValueAsInt("AD_User_ID"); 
				}
				catch (Exception e)
				{
					log.severe("No se pudo setear variable AD_User_ID");
					idUser = 0;
				}				
				if(idUser > 0)
					req.setAD_User_ID(idUser);
				else
					req.setAD_User_ID(100);
				//warehouse
				/*int idWareH = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_Warehouse_ID) FROM M_Warehouse WHERE IsActive = 'Y'"
						+ " AND AD_Org_ID = "+inv.getAD_Org_ID());
				if (idWareH > 0)
					req.setM_Warehouse_ID(idWareH);
				else
					idWareH = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_Warehouse_ID) FROM M_Warehouse WHERE IsActive = 'Y'");				
				if (idWareH > 0)
					req.setM_Warehouse_ID(idWareH);
				else
					req.setM_Warehouse_ID(1000000);
				*/
				req.setM_Warehouse_ID(inv.getM_Warehouse_ID());
				req.save();
								
				//lines
				MInventoryLine[] invLines = inv.getLines(false);
				
				for (int i = 0; i < invLines.length; i++)
				{
					MInventoryLine iLine = invLines[i];					
					MRequisitionLine reqLine = new MRequisitionLine(req);
					reqLine.setAD_Org_ID(req.getAD_Org_ID());
					reqLine.setM_Product_ID(iLine.getM_Product_ID());
					BigDecimal qtyReal = (BigDecimal)iLine.get_Value("qtyReal");
					reqLine.setQty(qtyReal);
					reqLine.save();
					try
					{
						reqLine.set_CustomColumn("M_InventoryLine_ID", iLine.get_ID());
						reqLine.save();
					}
					catch (Exception e)
					{
						log.severe("No se pudo setear campo M_InventoryLine_ID");
					}
				}	
				inv.set_CustomColumn("M_requisition_ID", req.get_ID());
				msg = "Procesado. Solicitud Creada";
			}
			inv.setDocStatus("CO");
			inv.setProcessed(true);
		}		
		inv.save();
		return msg;
	}	//	doIt	
}	//	Replenish
