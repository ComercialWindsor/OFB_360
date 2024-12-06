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
package org.dpp.model;

import org.compiere.model.MAsset;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_A_Asset_Use;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 *	Validator for DPP
 *
 *  @author Fabian Aguilar
 */
public class ModDPPUpdateAsset implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModDPPUpdateAsset ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModDPPUpdateAsset.class);
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
		//	Documents to be monitored		
		engine.addModelChange(MAsset.Table_Name, this); 
		engine.addModelChange(X_A_Asset_Use.Table_Name, this);
		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farï¿½as
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if(type == TYPE_AFTER_NEW && po.get_Table_ID()==MAsset.Table_ID)
		{
			MAsset asset = (MAsset) po;

			String sqlaccount = "SELECT MAX(ad_orgref_id) from ad_orginfo where ad_org_id in " +
					"(select parent_org_id from ad_orginfo where ad_org_id = "+asset.getAD_Org_ID()+")";
			
			int org = DB.getSQLValue(null, sqlaccount);
			if(org > 0)
			{
				asset.set_CustomColumn("AD_OrgRef_ID", org);
				asset.save();
			}
			else
			{
			asset.set_CustomColumn("AD_OrgRef_ID", asset.getAD_Org_ID());
			asset.save();
			}
		}
		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==X_A_Asset_Use.Table_ID)
		{
			//@mfrojas se cambia a type_after_new y type_after_change (original, before new y beforechange)
			X_A_Asset_Use assetUse = (X_A_Asset_Use) po;			
			MAsset asset = new MAsset(po.getCtx(), assetUse.getA_Asset_ID(),po.get_TrxName());
			
			int org_ID = assetUse.get_ValueAsInt("Org2_ID");
			if(org_ID > 0)
			{
				asset.setAD_Org_ID(org_ID);
				asset.save();
				DB.executeUpdate("UPDATE A_Asset_Use SET AD_Org_ID = "+org_ID+" WHERE A_Asset_Use_ID = "+assetUse.get_ID(),po.get_TrxName());
				/*assetUse.setAD_Org_ID(org_ID);
				assetUse.save();*/
			}
			//@mfrojas actualmente se está ocupando el org3 en pestaña responsables.
			//No tocaré el código anterior.
			int org3_ID = assetUse.get_ValueAsInt("Org3_ID");
			if(org3_ID > 0)
			{
				asset.setAD_Org_ID(org3_ID);
				asset.save();
				DB.executeUpdate("Update A_Asset_Use SET AD_Org_ID = "+org3_ID+" WHERE A_Asset_Use_ID = "+assetUse.get_ID(),po.get_TrxName());
			}
		}
				
	return null;
	}	//	modelChange

	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.x
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
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString	
}	
