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
import org.compiere.model.MProject;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_Payment;
import org.compiere.model.X_C_ProjectOFB;
import org.compiere.model.X_DM_Document;
import org.compiere.model.X_DM_DocumentLine;
import org.compiere.model.X_DM_MandateAgreement;
import org.compiere.model.X_MP_AssetMeter_Log;
import org.compiere.model.X_M_Movement;
import org.compiere.model.X_M_MovementLine;
import org.compiere.model.X_M_Requisition;
import org.compiere.model.X_M_RequisitionLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MProduct;



/**
 *	Validator for company Sismode
 *
 *  @author Italo Niñoles
 */
public class ModelOFBAguas implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBAguas ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBAguas.class);
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
		engine.addModelChange(X_M_RequisitionLine.Table_Name, this); 
		
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if(type == TYPE_AFTER_NEW &&  po.get_Table_ID()==X_M_RequisitionLine.Table_ID)
		{
			X_M_RequisitionLine rl = (X_M_RequisitionLine) po;
			X_M_Requisition mr = new X_M_Requisition(po.getCtx(), rl.getM_Requisition_ID(), po.get_TrxName());
			
			int idProOFB = mr.get_ValueAsInt("C_ProjectOFB_ID");
			
			if (idProOFB > 0)
			{
				X_C_ProjectOFB pro = new X_C_ProjectOFB(po.getCtx(), idProOFB, po.get_TrxName());
				
				rl.set_CustomColumn("A_CreateAsset", pro.get_ValueAsBoolean("A_CreateAsset"));
				rl.set_CustomColumn("A_CapvsExp", pro.get_ValueAsString("A_CapvsExp"));
				rl.set_CustomColumn("A_Asset_Group_ID", pro.get_ValueAsInt("A_Asset_Group_ID"));
				rl.set_CustomColumn("A_Asset_ID", pro.get_ValueAsInt("A_Asset_ID"));
				rl.set_CustomColumn("C_ChargeRef_ID", pro.get_ValueAsInt("C_Charge_ID"));
				rl.save();
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