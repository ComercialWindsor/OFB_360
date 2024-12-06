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
package org.tsm.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAsset;
import org.compiere.model.MClient;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MProduct;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.ofb.model.OFBForward;
import org.ofb.utils.DateUtils;

/**
 *	Validator for TSM
 *
 *  @author Italo Niñoles
 */
public class ModTSMSAMOA implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTSMSAMOA ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTSMSAMOA.class);
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
			log.info("Initializing Model Price Validator: "+this.toString());
		}

		//	Tables to be monitored
		//engine.addModelChange(MInventoryLine.Table_Name, this);		
		engine.addDocValidate(MInventory.Table_Name, this);
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInventory.Table_ID) 
		{	
			MInventory inv = (MInventory)po;
			int flag = DB.getSQLValue(po.get_TrxName(), "SELECT SUM(QtyInternalUse) FROM M_InventoryLine " +
					" WHERE IsActive = 'Y' AND M_Inventory_ID = "+inv.get_ID());
			if(flag > 0)
			{
				if(inv.getAD_Org_ID() == 1000058 && (inv.getM_Warehouse_ID() == 1000013 || inv.getM_Warehouse_ID() == 1000001))
				//if(inv.getAD_Org_ID() == 1000058 && inv.getM_Warehouse_ID() == 1000013)
				{
					try 
					{
						int cant = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) " +
								" FROM M_InventoryLine il" +
								" INNER JOIN M_Product mp ON (il.M_Product_ID = mp.M_Product_ID) " +
								" WHERE il.IsActive = 'Y' AND mp.IsSamoa = 'Y' AND M_Inventory_ID = "+inv.get_ID());
						if(cant > 0)
						{	
				            //String ruta = "/ruta/filename.txt";
				            String ruta = OFBForward.PathFileTSMSAMOA();
				            ruta = ruta+inv.getDocumentNo()+".txt";
				            //String contenido = "";
				            String bu = "";
				            MAsset asset = null;				            
				            //se genera detalle
				            File file = new File(ruta);
				            // Si el archivo no existe es creado
				            if (!file.exists()) 
				            {
				                file.createNewFile();
				            }
				            FileWriter fw = new FileWriter(file);
				            BufferedWriter bw = new BufferedWriter(fw);
				            MInventoryLine[] lines = inv.getLines(false);			            
				    		for (MInventoryLine line : lines)
				    		{
				    			if (!line.isActive())
				    				continue;
				    			if(line.getM_Product_ID() > 0)
				    			{
					    			MProduct prod = new MProduct(po.getCtx(), line.getM_Product_ID(),po.get_TrxName());
					    			if(prod.get_ValueAsBoolean("IsSamoa"))
					    			{
					    				if(line.get_ValueAsInt("A_Asset_ID") > 0)
					    				{
					    					asset = new MAsset(po.getCtx(), line.get_ValueAsInt("A_Asset_ID"),po.get_TrxName());
							    			bu = inv.getDocumentNo()+";"+inv.getM_Warehouse().getName()+";"+
							    				DateUtils.formatDate(DateUtils.today(), true)+";"+asset.getValue()+";"+
							    				line.getM_Product().getValue()+";"+line.getQtyInternalUse().setScale(3);
							    			//bu.append("\n\n");							            
								            bw.write(bu.toString());
								            bw.newLine();
					    				}
					    				else // si no existe activo se va a buscar a la solicitud 
					    				{
					    					int ID_Asset = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(rl.a_asset_id) " +
					    							" FROM m_requisitionline rl" +
					    							" INNER JOIN m_requisition r ON (r.m_requisition_id = rl.m_requisition_id)" +
					    							" INNER JOIN m_inventory i ON (r.m_inventory_id = i.m_inventory_id)" +
					    							" INNER JOIN m_inventoryline il ON (i.m_inventory_ID = il.m_inventory_ID)" +
					    							" WHERE il.line = rl.line AND il.m_inventoryline_id = "+line.get_ID());
					    					if(ID_Asset > 0)
					    					{
					    						asset = new MAsset(po.getCtx(), ID_Asset,po.get_TrxName());
								    			bu = inv.getDocumentNo()+";"+inv.getM_Warehouse().getName()+";"+
								    				DateUtils.formatDate(DateUtils.today(), true)+";"+asset.getValue()+";"+
								    				line.getM_Product().getValue()+";"+line.getQtyInternalUse().setScale(3);
								    			//bu.append("\n\n");							            
									            bw.write(bu.toString());
									            bw.newLine();
					    					}
					    					else
					    					{
					    						throw new AdempiereException("ERROR:Linea sin activo relacionado");
					    					}
					    				}
					    					
							            
							            
							            
					    			}
					    			
				    			}
				    		}
				            bw.close();
						}					
			        }
					catch (Exception e) 
			        {
			            e.printStackTrace();
			        }
				}
			}
		}
		
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
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString


	

}	