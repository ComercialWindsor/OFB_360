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

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MRole;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company PT
 *
 *  @author Italo Niñoles
 */
public class ModPetroAmericaRolOrder implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPetroAmericaRolOrder ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPetroAmericaRolOrder.class);
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
		engine.addDocValidate(MOrder.Table_Name, this);
				

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
	
	public static String rtrim(String s, char c) {
	    int i = s.length()-1;
	    while (i >= 0 && s.charAt(i) == c)
	    {
	        i--;
	    }
	    return s.substring(0,i+1);
	}

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

		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MOrder.Table_ID)
		{
			MOrder order = (MOrder)po;
			if(!order.isSOTrx())
			{
				int cantA = 0;
				int cantB = 0;
				MRole rol = new MRole(po.getCtx(), Env.getAD_Role_ID(po.getCtx()),po.get_TrxName());
				//seteamo valores de variables auxiliares
				cantA = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM C_Order co" +
						" INNER JOIN C_OrderLine col ON (co.C_Order_ID = col.C_Order_ID)" +
						" INNER JOIN C_Charge cc ON (cc.C_Charge_ID = col.C_Charge_ID)" +
						" INNER JOIN C_ChargeType ct ON (ct.C_ChargeType_ID = cc.C_ChargeType_ID)" +
						" WHERE co.C_Order_ID = "+order.get_ID()+" AND ct.value = 'TCAF'");
				
				cantB = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM C_Order co " +
						" INNER JOIN C_OrderLine col ON (co.C_Order_ID = col.C_Order_ID) " +
						" INNER JOIN M_Product mp ON (mp.M_Product_ID = col.M_Product_ID) " +
						" INNER JOIN M_Product_Category pc ON (pc.M_Product_Category_ID = mp.M_Product_Category_ID) " +
						" WHERE co.C_Order_ID = "+order.get_ID()+" AND LOWER(pc.name) like '%combustible%'");
				//rol3
				if(rol.getDescription() != null && rol.getDescription().toLowerCase().contains("basico"))
				{
					if(cantA > 0 || cantB > 0)
						return "Error de permisos: Rol no puede completar orden";
				}	
				//rol2
				if(rol.getDescription() != null && rol.getDescription().toLowerCase().contains("combustible"))
				{
					if(cantA > 0 | cantB < 1)
						return "Error de permisos: Rol no puede completar orden";
				}
				//rol1
				if(rol.getDescription() != null && rol.getDescription().toLowerCase().contains("activo"))
				{
					if(cantA < 1)
						return "Error de permisos: Rol no puede completar orden";
				}
				//rol 4
				//se asume al no tener descripcion que puede completar todo
				
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
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString


	

}	