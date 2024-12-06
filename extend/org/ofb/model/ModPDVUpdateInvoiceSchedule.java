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
package org.ofb.model;

import java.util.logging.Level;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MClient;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for PDV Colegios
 *
 *  @author Fabian Aguilar
 */
public class ModPDVUpdateInvoiceSchedule implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPDVUpdateInvoiceSchedule ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPDVUpdateInvoiceSchedule.class);
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
		engine.addDocValidate(MAllocationHdr.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Far�as
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
				
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
				
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MAllocationHdr.Table_ID)
		{
			MAllocationHdr allo = (MAllocationHdr)po;
			MAllocationLine[] alines = allo.getLines(false);
			for (int i = 0; i < alines.length; i++)
			{
				MAllocationLine aline = alines[i];				
				if(aline.get_ValueAsInt("C_InvoicePaySchedule_ID")>0)
				{
					MInvoicePaySchedule is = new MInvoicePaySchedule(po.getCtx(),aline.get_ValueAsInt("C_InvoicePaySchedule_ID"),po.get_TrxName());
					/*is.set_CustomColumn("AllocatedAmt", aline.getAmount());
					is.set_CustomColumn("IsPaid", true);
					is.save();*/
					try{
						String sqlUpdate = "UPDATE C_InvoicePaySchedule SET IsPaid = 'Y', AllocatedAmt = "+aline.getAmount()+
								" WHERE C_InvoicePaySchedule_ID = "+is.get_ID();
						DB.executeUpdate(sqlUpdate, po.get_TrxName());
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, e.getMessage(), e);
					}					
				}
				else
				{
					MPayment pay = new MPayment(po.getCtx(), aline.getC_Payment_ID(), po.get_TrxName());
					if(pay.get_ValueAsInt("C_InvoicePaySchedule_ID") > 0)
					{
						try{
							String sqlUpdate = "UPDATE C_AllocationLine " +
									" SET C_InvoicePaySchedule_ID = "+pay.get_ValueAsInt("C_InvoicePaySchedule_ID")+
									" WHERE C_AllocationLine_ID = "+aline.get_ID();
							DB.executeUpdate(sqlUpdate, po.get_TrxName());
						}
						catch (Exception e)
						{
							log.log(Level.SEVERE, e.getMessage(), e);
						}
					}
				}
			}
		}		
		if(timing == TIMING_AFTER_VOID && po.get_Table_ID()==MAllocationHdr.Table_ID)
		{
			MAllocationHdr allo = (MAllocationHdr)po;
			MAllocationLine[] alines = allo.getLines(false);
			for (int i = 0; i < alines.length; i++)
			{
				MAllocationLine aline = alines[i];				
				if(aline.get_ValueAsInt("C_InvoicePaySchedule_ID")>0)
				{
					MInvoicePaySchedule is = new MInvoicePaySchedule(po.getCtx(),aline.get_ValueAsInt("C_InvoicePaySchedule_ID"),po.get_TrxName());
					/*is.set_CustomColumn("AllocatedAmt", Env.ZERO);
					is.set_CustomColumn("IsPaid", false);
					is.save();*/
					try{
						String sqlUpdate = "UPDATE C_InvoicePaySchedule SET IsPaid = 'N', AllocatedAmt = "+Env.ZERO+
								" WHERE C_InvoicePaySchedule_ID = "+is.get_ID();
						DB.executeUpdate(sqlUpdate, po.get_TrxName());
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, e.getMessage(), e);
					}	
					
					aline.set_CustomColumn("C_InvoicePaySchedule_ID", null);
					aline.save();
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