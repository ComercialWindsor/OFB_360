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
package org.ofb.model;

import java.math.BigDecimal;

import org.compiere.model.MClient;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company Sismode
 *
 *  @author Italo Niñoles
 */
public class ModCopiaCamposInventory implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCopiaCamposInventory ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCopiaCamposInventory.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;
	

	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing global validator: "+this.toString());
		}

		//	Tables to be monitored
		//	Documents to be monitored
		engine.addModelChange(MInventoryLine.Table_Name, this);
		engine.addModelChange(MInventory.Table_Name, this); 
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		//validaciones detalle hr
		if(type == TYPE_AFTER_NEW && po.get_Table_ID()==MInventoryLine.Table_ID)  
		{
			MInventoryLine mml = (MInventoryLine) po;			
			try
			{
				BigDecimal qtyCount = mml.getQtyCount();
				if(qtyCount != null)
				{
					/*mml.set_CustomColumn("QtySuggested", qtyCount);
					mml.set_CustomColumn("QtyReal", qtyCount);*/
					DB.executeUpdate("UPDATE M_InventoryLine SET QtySuggested="+qtyCount+",QtyReal="+qtyCount+" "
							+ " WHERE M_InventoryLine_ID = "+mml.get_ID(), po.get_TrxName());					
				}
			}
			catch (Exception e)
			{
				log.severe(""+e);
			}	
		}
		if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID()==MInventory.Table_ID && po.is_ValueChanged("Procesar"))  
		{
			MInventory inv = (MInventory) po;
			
			int id_role = Env.getAD_Role_ID(po.getCtx());
			String msg = "";
			//int M_Inventory_ID = getRecord_ID();
			//MInventory inv = new MInventory(getCtx(), M_Inventory_ID, get_TrxName());
			
			if(inv.getDocStatus().compareToIgnoreCase("DR") ==0)
			{	
				//inv.setDocStatus("L1");
				DB.executeUpdate("UPDATE M_Inventory SET DocStatus='L1'"
						+ " WHERE M_Inventory_ID = "+inv.get_ID(), po.get_TrxName());
				msg = "Procesado";
			}
			else if(inv.getDocStatus().compareToIgnoreCase("L1") ==0)
			{	
				if (id_role != 1000022 && id_role != 1000021 && id_role != 1000012)				
					return("Rol sin privilegios suficientes");
				//inv.setDocStatus("L2");
				DB.executeUpdate("UPDATE M_Inventory SET DocStatus='L2'"
						+ " WHERE M_Inventory_ID = "+inv.get_ID(), po.get_TrxName());
				msg = "Procesado";
			}
			else if(inv.getDocStatus().compareToIgnoreCase("L2") ==0)
			{
				if (id_role != 1000021 && id_role != 1000012)				
					return("Rol sin privilegios suficientes");
				//inv.setDocStatus("L3");
				DB.executeUpdate("UPDATE M_Inventory SET DocStatus='L3'"
						+ " WHERE M_Inventory_ID = "+inv.get_ID(), po.get_TrxName());
				msg = "Procesado";
			}
			else if(inv.getDocStatus().compareToIgnoreCase("L3") ==0)
			{	
				if (id_role != 1000012)				
					return("Rol sin privilegios suficientes");
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
					MRequisition req = new MRequisition(po.getCtx(), 0, po.get_TrxName());
					//c_doctype
					int idDocType = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(C_DocType_ID) FROM C_DocType WHERE docbasetype = 'POR' "
							+ " AND IsActive = 'Y' AND AD_Client_ID = "+inv.getAD_Client_ID());
					if (idDocType > 0)
						req.setC_DocType_ID(idDocType);
					else
						req.setC_DocType_ID(1000018);
					//org
					req.setAD_Org_ID(inv.getAD_Org_ID());
					//pricelist
					int idPriceList = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(M_PriceList_ID) FROM M_PriceList WHERE IsActive = 'Y'"
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
					
					//inv.set_CustomColumn("M_requisition_ID", req.get_ID());
					DB.executeUpdate("UPDATE M_Inventory SET M_requisition_ID="+req.get_ID()
							+ " WHERE M_Inventory_ID = "+inv.get_ID(), po.get_TrxName());
					msg = "Procesado. Solicitud Creada";
				}
				//inv.setDocStatus("CO");
				//inv.setProcessed(true);
				DB.executeUpdate("UPDATE M_Inventory SET DocStatus='CO',Processed='Y'"
						+ " WHERE M_Inventory_ID = "+inv.get_ID(), po.get_TrxName());
			}		
			//inv.save();
			DB.executeUpdate("UPDATE M_Inventory SET Procesar='N'"
					+ " WHERE M_Inventory_ID = "+inv.get_ID(), po.get_TrxName());
			//return msg;			
		}
	return null;
	}	//	modelChange

	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);

		
		return null;
	}	//	docValidate

	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString


	

}	