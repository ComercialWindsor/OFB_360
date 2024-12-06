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
package org.geminis.model;

import org.compiere.model.MCash;
import org.compiere.model.MClient;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company Sismode
 *
 *  @author Julio Farias
 */
public class ModelOFBGeminis implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBGeminis ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBGeminis.class);
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
		engine.addModelChange(MPayment.Table_Name, this); // ID tabla 335
		engine.addDocValidate(MCash.Table_Name, this); 
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farías
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==335)
		{
			MPayment pay = (MPayment) po;
			
			boolean override = pay.get_ValueAsBoolean("Override");
			
			if (override == false)
			{
				String sql = "SELECT COUNT(*) FROM " +
						"C_Payment cp WHERE cp.C_BPartner_ID = ? "+
						"AND (SELECT COUNT(*) FROM C_AllocationLine cal WHERE cal.C_Payment_ID = cp.C_Payment_ID) < 1 "+
						"AND cp.C_Payment_ID <> ? "+
						"AND cp.PayAmt = ? "+
						"AND cp.IsReceipt = ? " +
						"AND cp.DocStatus NOT IN ('VO')";						
						
				int cant = DB.getSQLValue(null, sql, pay.getC_BPartner_ID(),pay.get_ID(),pay.getPayAmt(),pay.get_ValueAsBoolean("IsReceipt"));
								
				if (cant > 0)
				{
					return "Existe un pago sin asignaciones para el mismo socio de negocio con el mismo monto";
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
		if(timing == TIMING_AFTER_VOID && po.get_Table_ID()==MCash.Table_ID)
		{
			MCash cash = (MCash) po;
			cash.setBeginningBalance(Env.ZERO);
			cash.setEndingBalance(Env.ZERO);
			cash.saveEx(po.get_TrxName());
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
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString


	

}	