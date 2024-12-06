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
package org.metlife.model;

import org.compiere.model.MClient;
import org.compiere.model.MContactInterest;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_CampaignFollow;
import org.compiere.model.X_R_ContactInterest;
import org.compiere.model.X_R_InterestAreaValues;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for Metlife
 *
 *  @author Italo Niñoles
 */
public class ModelOFBMetLifeUpLead implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBMetLifeUpLead ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBMetLifeUpLead.class);
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
		engine.addModelChange(X_C_CampaignFollow.Table_Name, this); // ID tabla 323
		engine.addModelChange(MContactInterest.Table_Name, this); // ID tabla 323
		
				
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)&& po.get_Table_ID()==X_C_CampaignFollow.Table_ID)  
		{
			X_C_CampaignFollow cFollow = (X_C_CampaignFollow) po;
			if (cFollow.get_ValueAsBoolean("IsUseUpdate"))
			{
				DB.executeUpdate("UPDATE C_CampaignFollow SET AD_UserRefUp_ID = "+Env.getAD_User_ID(po.getCtx())+
						" WHERE C_CampaignFollow_ID = "+cFollow.get_ID(), po.get_TrxName());
			}
		}		
		if(type == TYPE_BEFORE_DELETE && po.get_Table_ID()==X_C_CampaignFollow.Table_ID)  
		{
			//X_C_CampaignFollow cFollow = (X_C_CampaignFollow) po;
			if (Env.getAD_Role_ID(po.getCtx()) == 1000033)
			{
				return "Acción No Permitida";
			}	
		}
		if(type == TYPE_AFTER_CHANGE && po.get_Table_ID()==X_R_ContactInterest.Table_ID)  
		{			
			X_R_ContactInterest cInterest = (X_R_ContactInterest) po;
			if(cInterest.get_ValueAsString("InterestAreaValue") != null 
					&& cInterest.get_ValueAsString("InterestAreaValue") != "")
			{
				int id_AValues = DB.getSQLValue(po.get_TrxName(),"SELECT MAX(R_InterestAreaValues_ID) FROM R_InterestAreaValues" +
						" WHERE lower(value) = '"+cInterest.get_ValueAsString("InterestAreaValue").toLowerCase()+"' " +
								" AND R_InterestArea_ID = "+cInterest.getR_InterestArea_ID());
				if(id_AValues > 0)
				{
					DB.executeUpdate("UPDATE R_ContactInterest SET R_InterestAreaValues_ID = "+id_AValues+
							" WHERE R_ContactInterest_ID = "+cInterest.get_ID(), po.get_TrxName());
				}else
				{
					X_R_InterestAreaValues IAreaValue = new X_R_InterestAreaValues(po.getCtx(), 0, po.get_TrxName());
					IAreaValue.setR_InterestArea_ID(cInterest.getR_InterestArea_ID());
					IAreaValue.setValue(cInterest.get_ValueAsString("InterestAreaValue"));					
					IAreaValue.save();
					
					DB.executeUpdate("UPDATE R_ContactInterest SET R_InterestAreaValues_ID = "+IAreaValue.get_ID()+
							" WHERE R_ContactInterest_ID = "+cInterest.get_ID(), po.get_TrxName());
					
				}
			}
				
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
	}	//	toString}	
}