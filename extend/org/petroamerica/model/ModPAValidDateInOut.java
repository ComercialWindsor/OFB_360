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

import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.model.MClient;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
/**
 *	Validator for Petroameica
 *
 *  @author Italo Ni�oles
 */
public class ModPAValidDateInOut implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPAValidDateInOut ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPAValidDateInOut.class);
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
		//engine.addModelChange(MInOut.Table_Name, this);
		engine.addDocValidate(MInOut.Table_Name, this);
		engine.addDocValidate(MInvoice.Table_Name, this);		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo ni�oles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID() == MInOut.Table_ID) 
		{	
			MInOut inOut = (MInOut)po;
			if(inOut.isSOTrx())
			{	
				if(!inOut.get_ValueAsBoolean("Override"))
				{
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					cal.add(Calendar.DATE, -7);
					Timestamp PreviousDate = new Timestamp(cal.getTime().getTime());
					cal = Calendar.getInstance();
					cal.set(Calendar.HOUR, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					cal.add(Calendar.DATE, 2);
					Timestamp NextDate = new Timestamp(cal.getTime().getTime());				
					if(inOut.getC_Order().getDateOrdered().compareTo(PreviousDate) < 0
							|| inOut.getC_Order().getDateOrdered().compareTo(NextDate) > 0)
					{
						return "Error: Fecha de despacho debe estar entre "+PreviousDate.toString()+
						" y "+NextDate.toString();
					}
				}
			}
		}
		
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID() == MInvoice.Table_ID) 
		{	
			MInvoice inv = (MInvoice)po;
			if(inv.isSOTrx())
			{	
				if(!inv.get_ValueAsBoolean("Override"))
				{
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					cal.add(Calendar.DATE, -7);
					Timestamp PreviousDate = new Timestamp(cal.getTime().getTime());
					cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					cal.add(Calendar.DATE, 2);
					Timestamp NextDate = new Timestamp(cal.getTime().getTime());				
					if(inv.getC_Order().getDateOrdered().compareTo(PreviousDate) < 0
							|| inv.getC_Order().getDateOrdered().compareTo(NextDate) > 0)
					{
						return "Error: Fecha de despacho debe estar entre "+PreviousDate.toString()+
						" y "+NextDate.toString();
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