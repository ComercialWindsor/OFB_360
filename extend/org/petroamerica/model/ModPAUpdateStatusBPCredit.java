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
package org.petroamerica.model;

import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
/**
 *	Validator for PA 
 *
 *  @author Italo Ni�oles
 */
public class ModPAUpdateStatusBPCredit implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPAUpdateStatusBPCredit ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPAUpdateStatusBPCredit.class);
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
		engine.addModelChange(MBPartner.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo ni�oles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==MBPartner.Table_ID 
				&& (po.is_ValueChanged("OwnCreditLimit") || po.is_ValueChanged("ExternalCreditLimit")
					 || po.is_ValueChanged("StartDateCreditLimit") || po.is_ValueChanged("EndDateCreditLimit")
					 || po.is_ValueChanged("CreditGroup")// || po.is_ValueChanged("SO_CreditUsedOFB")
					 || po.is_ValueChanged("C_BP_Group_ID")
					 || po.is_ValueChanged("PaymentRule")
					 || po.is_ValueChanged("C_PaymentTerm_ID")
					 || po.is_ValueChanged("SalesRep_ID")
					 || po.is_ValueChanged("SO_Description")
					 || po.is_ValueChanged("MontoUF"))) 
		{	
			MBPartner bp = (MBPartner)po;
			//bp.setSOCreditStatus("H");
			bp.set_CustomColumn("SOCreditStatusOFB", "H");
			bp.set_CustomColumn("Signature1", false);
			bp.set_CustomColumn("Signature2", false);
			bp.set_CustomColumn("Signature4", false);
		}		
		return null;
	}	//	modelChange

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