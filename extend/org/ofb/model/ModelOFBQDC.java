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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.logging.Level;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MMovement;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MPriceListVersion;
import org.compiere.model.MProductPrice;
import org.compiere.model.MProject;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_InvoiceLine;
import org.compiere.model.X_C_OrderLine;
import org.compiere.model.X_C_Payment;
import org.compiere.model.X_DM_Document;
import org.compiere.model.X_DM_DocumentLine;
import org.compiere.model.X_DM_MandateAgreement;
import org.compiere.model.X_MP_AssetMeter_Log;
import org.compiere.model.X_M_Movement;
import org.compiere.model.X_M_MovementLine;
import org.compiere.model.X_TP_Destination;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MProduct;



/**
 *	Validator for company Sismode
 *
 *  @author Italo Niñoles
 */
public class ModelOFBQDC implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBQDC ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBQDC.class);
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
		engine.addModelChange(X_C_OrderLine.Table_Name, this); 
		engine.addModelChange(X_C_InvoiceLine.Table_Name, this);
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		//validaciones cabecera
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==X_C_OrderLine.Table_ID)
		{				
			MOrderLine ol = (MOrderLine) po;
			MOrder mo = new MOrder(po.getCtx(), ol.getC_Order_ID(), po.get_TrxName());
			int id_Prod = 0;
			
			id_Prod = ol.getM_Product_ID();
			
			if (mo.isSOTrx())
			{
				if (id_Prod > 0)
				{
					String sqlV = "SELECT MIN(mpp.priceLimit) FROM C_OrderLine col "+
							"INNER JOIN C_Order co on (col.C_Order_ID = co.C_Order_ID) "+
							"INNER JOIN M_PriceList mpl on (co.M_PriceList_ID = mpl.M_PriceList_ID) "+
							"LEFT  JOIN M_PriceList_Version plv on (mpl.M_PriceList_ID = plv.M_PriceList_ID) "+
							"LEFT JOIN M_ProductPrice mpp on (plv.M_PriceList_Version_ID = mpp.M_PriceList_Version_ID "+ 
							"AND col.M_Product_ID = mpp.M_Product_ID) WHERE plv.IsActive = 'Y' "+
							"AND plv.ValidFrom < ? AND C_OrderLine_ID = "+ol.get_ID();
					
					BigDecimal amtPL = DB.getSQLValueBD(po.get_TrxName(), sqlV, mo.getDateOrdered());
										
					if (amtPL.compareTo(Env.ZERO) > 0)
					{
						if (ol.getPriceEntered().compareTo(amtPL) < 0)
							return "Invalid Limit Price";						
					}
				}
			}
		}	
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==X_C_InvoiceLine.Table_ID)
		{				
			MInvoiceLine il = (MInvoiceLine) po;
			MInvoice mi = new MInvoice(po.getCtx(), il.getC_Invoice_ID(), po.get_TrxName());
			int id_Prod = 0;
			
			id_Prod = il.getM_Product_ID();
			
			if (mi.isSOTrx())
			{
				if (id_Prod > 0)
				{
					String sqlV = "SELECT MIN(mpp.priceLimit) FROM C_InvoiceLine cil "+  
							"INNER JOIN C_invoice ci on (cil.C_Invoice_ID = ci.C_Invoice_ID) "+ 
							"INNER JOIN M_PriceList mpl on (ci.M_PriceList_ID = mpl.M_PriceList_ID) " +
							"LEFT  JOIN M_PriceList_Version plv on (mpl.M_PriceList_ID = plv.M_PriceList_ID) "+ 
							"LEFT JOIN M_ProductPrice mpp on (plv.M_PriceList_Version_ID = mpp.M_PriceList_Version_ID "+ 
							"AND cil.M_Product_ID = mpp.M_Product_ID) WHERE plv.IsActive = 'Y' "+ 
							"AND plv.ValidFrom < ? AND C_InvoiceLine_ID = "+il.get_ID();
					
					BigDecimal amtPL = DB.getSQLValueBD(po.get_TrxName(), sqlV, mi.getDateInvoiced());
										
					if (amtPL.compareTo(Env.ZERO) > 0)
					{
						if (il.getPriceEntered().compareTo(amtPL) < 0)
							return "Invalid Limit Price";						
					}
				}
			}
		}
		
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