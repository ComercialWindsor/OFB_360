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
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MSequence;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_C_OrderLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MProduct;



/**
 *	Validator for company ignisterra
 *
 *  @author Italo Niñoles
 */
public class ModelOFBIgnisterra implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBIgnisterra ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBIgnisterra.class);
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
		engine.addModelChange(X_C_Order.Table_Name, this); // ID tabla 323
		engine.addModelChange(X_C_OrderLine.Table_Name, this);
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==X_C_Order.Table_ID)  
		{
			MOrder order = (MOrder) po;
			
			if (order.isSOTrx())
			{
				MSequence seq = new MSequence(po.getCtx(), 1000286, po.get_TrxName());

				if (order.getDocStatus().equalsIgnoreCase("CO"))
				{
					//@mfrojas se valida también que actualmente el correlativo esté definido
					String sqlcorrelative ="Select correlative from c_order where c_order_id = "+po.get_ValueAsInt("C_Order_ID");
					PreparedStatement pstmt1 = null;
					pstmt1=DB.prepareStatement(sqlcorrelative,null);
					ResultSet rs=pstmt1.executeQuery();
					int correlative = 0;
					
					rs.next();
					correlative = rs.getInt("correlative");
					log.config(""+correlative);
					if(correlative==0)
					{
						String sqlUpdate = "UPDATE C_Order SET Correlative = "+seq.getCurrentNext()+" WHERE C_Order_ID = "+order.get_ID();
						DB.executeUpdate(sqlUpdate, po.get_TrxName());										
						seq.setCurrentNext(seq.getCurrentNext()+seq.getIncrementNo());
						seq.save();
					}
				}				
			}	
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==X_C_OrderLine.Table_ID)  
		{
			MOrderLine orderLine = (MOrderLine) po;
			MOrder order = new MOrder(po.getCtx(), orderLine.getC_Order_ID(), po.get_TrxName());
			
			if (order.isSOTrx())
			{
				MOrderLine[] lines = order.getLines(false, null);
				if (lines.length > 0)
				{
					if (order.get_ValueAsString("Category").equalsIgnoreCase("S")
							&& order.getTotalLines().compareTo(new BigDecimal("3000000.0"))<=0 )
					{
						String sqlUpdate = "UPDATE C_Order SET IsValid = 'Y' WHERE C_Order_ID = "+order.get_ID();
						DB.executeUpdate(sqlUpdate, po.get_TrxName());
					}					
				}
			}			
		}	
		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==X_C_Order.Table_ID)  
		{
			MOrder order = (MOrder) po;
			
			if (order.isSOTrx())
			{
				MOrderLine[] lines = order.getLines(false, null);
				if (lines.length > 0)
				{
					if (order.get_ValueAsString("Category").equalsIgnoreCase("S")
							&& order.getTotalLines().compareTo(new BigDecimal("3000000.0"))<=0 )
					{
						String sqlUpdate = "UPDATE C_Order SET IsValid = 'Y' WHERE C_Order_ID = "+order.get_ID();
						DB.executeUpdate(sqlUpdate, po.get_TrxName());
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