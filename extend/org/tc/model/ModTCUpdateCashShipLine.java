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
package org.tc.model;

import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
/**
 *	Validator for TC
 *
 *  @author Italo Niñoles
 */
public class ModTCUpdateCashShipLine implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTCUpdateCashShipLine ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTCUpdateCashShipLine.class);
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
		engine.addModelChange(MInOutLine.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==MInOutLine.Table_ID) 
		{	
			MInOutLine ioLine = (MInOutLine)po;
			MInOut io = new MInOut(po.getCtx(), ioLine.getM_InOut_ID(), po.get_TrxName());
			if(io.isSOTrx())
			{
				int ID_BPartner = 0;
				ID_BPartner = ioLine.get_ValueAsInt("C_BPartner_ID");
				if(ID_BPartner > 0)
				{
					MBPartner bp = null;
					bp = new MBPartner(po.getCtx(), ID_BPartner, po.get_TrxName());
					if(ioLine.get_ValueAsInt("C_Cash_ID") <= 0)
					{	
						int ID_Cash = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(cc.C_Cash_ID) " +
								" FROM C_Cash cc INNER JOIN C_CashBook cb ON (cc.C_CashBook_ID = cb.C_CashBook_ID) " +
								" WHERE cc.DOcStatus IN ('DR','IP') AND cb.C_BPartner_ID = "+bp.get_ID());
						if(ID_Cash > 0)
							ioLine.set_CustomColumn("C_Cash_ID", ID_Cash);
						else
							return "No se puede guardar la lines si no hay un diario de efectivo abierto";
					}
				}				
			}
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