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
package org.baskakow.model;

import java.math.BigDecimal;

import org.compiere.model.MClient;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;



/**
 *	Validator for company Sismode
 *
 *  @author Italo Niñoles
 */
public class ModUpdateCost implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModUpdateCost ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModUpdateCost.class);
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
		engine.addModelChange(MInOutLine.Table_Name, this); 
		//	Documents to be monitored
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW)&&  po.get_Table_ID()==MInOutLine.Table_ID)
		{
			MInOutLine shipL = (MInOutLine) po;
			MInOut ship = new MInOut(po.getCtx(), shipL.getM_InOut_ID(), po.get_TrxName());
			
			if (ship.isSOTrx())
			{
				BigDecimal productCost = (BigDecimal)shipL.get_Value("productcost");
				if (productCost == null || productCost.compareTo(Env.ZERO) == 0 || productCost != null) //@mfrojas se agrega el != null, pues debe poder cambiar siempre
				{
					String sqlPC = "SELECT MAX(mppo.pricelist)  " +
							"from m_inoutline miol " +
							"JOIN c_orderbp cobp ON miol.c_orderbp_id = cobp.c_orderbp_id " +
							"JOIN c_bplocatorprice mppo ON miol.m_product_id = mppo.m_product_id AND mppo.c_bpartner_location_id = cobp.c_bpartner_location_id " +
							"WHERE miol.M_InOutLine_ID = "+shipL.get_ID();
					log.config(sqlPC);
					BigDecimal productCostFrom = DB.getSQLValueBD(po.get_TrxName(), sqlPC);
					log.config("Cost prod"+productCostFrom+" <-");
					//@mfrojas Se cambia el setCustomColumn por un update. EL customcolumn no escribía el dato.
					if (productCostFrom != null)
						{
						DB.executeUpdateEx(
								"UPDATE M_InoutLine set productcost = "+productCostFrom+" where m_inoutline_id = "+shipL.get_ID(), po.get_TrxName());
							//shipL.set_CustomColumn("productcost", productCostFrom);
						}
					
					
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
	}	//	toString


	

}	