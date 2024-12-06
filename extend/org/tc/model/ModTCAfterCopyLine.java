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

import org.compiere.model.MClient;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
/**
 *	Validator for TC
 *
 *  @author Italo Niñoles
 */
public class ModTCAfterCopyLine implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTCAfterCopyLine ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTCAfterCopyLine.class);
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
		engine.addModelChange(MOrderLine.Table_Name, this);		
		engine.addModelChange(MInOutLine.Table_Name, this);
		engine.addModelChange(MInvoiceLine.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if(type == TYPE_BEFORE_NEW && po.get_Table_ID()==MOrderLine.Table_ID) 
		{	
			MOrderLine oLine = (MOrderLine)po;			
			MOrder order = new MOrder(po.getCtx(), oLine.getC_Order_ID(), po.get_TrxName());
			if(oLine.getC_Order().isSOTrx() && oLine.get_ValueAsInt("C_OrderLineRef_ID") > 0
					&& order.getDocStatus().compareToIgnoreCase("CO") != 0)
			{
				MOrderLine oldLine = new MOrderLine(po.getCtx(), oLine.get_ValueAsInt("C_OrderLineRef_ID"),po.get_TrxName());
				if(oldLine.get_ValueAsInt("C_BPartner_Location2_ID") > 0)
					oLine.set_CustomColumn("C_BPartner_Location2_ID",oldLine.get_ValueAsInt("C_BPartner_Location2_ID"));
				if(oldLine.get_ValueAsInt("C_BPartner_Location3_ID") > 0)
					oLine.set_CustomColumn("C_BPartner_Location3_ID",oldLine.get_ValueAsInt("C_BPartner_Location3_ID"));
				if(oldLine.get_ValueAsInt("C_BPartner_Location4_ID") > 0)
					oLine.set_CustomColumn("C_BPartner_Location4_ID",oldLine.get_ValueAsInt("C_BPartner_Location4_ID"));
				//ver tema atribute instance
				
			}
		}	
		if(type == TYPE_BEFORE_NEW && po.get_Table_ID()==MInOutLine.Table_ID) 
		{	
			MInOutLine ioLine = (MInOutLine)po;
			if(ioLine.getC_OrderLine_ID() > 0)
			{
				MOrderLine oLine = new MOrderLine(po.getCtx(),ioLine.getC_OrderLine_ID(),po.get_TrxName());
				if(oLine.get_ValueAsInt("C_BPartner_Location2_ID") > 0)
					ioLine.set_CustomColumn("C_BPartner_Location2_ID",oLine.get_ValueAsInt("C_BPartner_Location2_ID"));
				if(oLine.get_ValueAsInt("C_BPartner_Location3_ID") > 0)
					ioLine.set_CustomColumn("C_BPartner_Location3_ID",oLine.get_ValueAsInt("C_BPartner_Location3_ID"));
				if(oLine.get_ValueAsInt("C_BPartner_Location4_ID") > 0)
					ioLine.set_CustomColumn("C_BPartner_Location4_ID",oLine.get_ValueAsInt("C_BPartner_Location4_ID"));
			}
		}
		if(type == TYPE_BEFORE_NEW && po.get_Table_ID()==MInvoiceLine.Table_ID) 
		{	
			MInvoiceLine iLine = (MInvoiceLine)po;
			if(iLine.getM_InOutLine_ID() > 0)
			{
				MInOutLine ioLine = new MInOutLine(po.getCtx(),iLine.getM_InOutLine_ID(),po.get_TrxName());
				if(ioLine.get_ValueAsInt("C_BPartner_Location2_ID") > 0)
					iLine.set_CustomColumn("C_BPartner_Location2_ID",ioLine.get_ValueAsInt("C_BPartner_Location2_ID"));
				if(ioLine.get_ValueAsInt("C_BPartner_Location3_ID") > 0)
					iLine.set_CustomColumn("C_BPartner_Location3_ID",ioLine.get_ValueAsInt("C_BPartner_Location3_ID"));
				if(ioLine.get_ValueAsInt("C_BPartner_Location4_ID") > 0)
					iLine.set_CustomColumn("C_BPartner_Location4_ID",ioLine.get_ValueAsInt("C_BPartner_Location4_ID"));
			}else if(iLine.getC_OrderLine_ID() > 0)
			{
				MOrderLine oLine = new MOrderLine(po.getCtx(),iLine.getC_OrderLine_ID(),po.get_TrxName());
				if(oLine.get_ValueAsInt("C_BPartner_Location2_ID") > 0)
					iLine.set_CustomColumn("C_BPartner_Location2_ID",oLine.get_ValueAsInt("C_BPartner_Location2_ID"));
				if(oLine.get_ValueAsInt("C_BPartner_Location3_ID") > 0)
					iLine.set_CustomColumn("C_BPartner_Location3_ID",oLine.get_ValueAsInt("C_BPartner_Location3_ID"));
				if(oLine.get_ValueAsInt("C_BPartner_Location4_ID") > 0)
					iLine.set_CustomColumn("C_BPartner_Location4_ID",oLine.get_ValueAsInt("C_BPartner_Location4_ID"));
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