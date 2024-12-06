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
package org.mutual.model;

import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
/**
 *	Validator for company Sismode
 *
 *  @author Julio Farias
 */
public class ModMutualInsertSAP implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModMutualInsertSAP ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModMutualInsertSAP.class);
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
		//engine.addModelChange(MInvoice.Table_Name, this);
			
		engine.addDocValidate(MInvoice.Table_Name, this);

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Italo Niñoles
     */
	
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
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

		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInvoice.Table_ID)
		{		
			//cuando se completan notas de creditos
			MInvoice inv = (MInvoice) po;
			int id_doc = 0;
			int id_doc2 = 0;
			if (DB.isPostgreSQL())
			{
				id_doc = 1000062;
				//id_doc = 1000068;
			}
			else if (DB.isOracle())
			{
				id_doc = 1000067;
				id_doc2 = 1000068;
			}
			else 
				id_doc = 1000067;
			if (inv.getC_DocTypeTarget_ID() == id_doc || inv.getC_DocTypeTarget_ID() == id_doc2)
			{
				insertSAPTemp(inv.get_ID(),po);
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

	public void insertSAPTemp (int Invoice_ID, PO po)
	{
		log.info("Invoice_ID=" + Invoice_ID);		
		/*int id_solpesap = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(i_solpesap_ID) FROM i_solpesap ");
		if (id_solpesap == 0)
			id_solpesap = 1000000;		
		id_solpesap = id_solpesap + 1;*/
		
		String sqlInsert = "INSERT INTO i_solpesap (ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,c_invreqnumber,doc_type_id,c_distribution, " +
				"org_value,c_bpartnervalue,c_bparnername,c_bparnername2,c_orderreference,referencetype,invoicereference, " +
				"c_dateinvreq,c_dateref,address1,address2,city,productvalue,m_productacct,qty,totallines,orderorg, " +
				"ceconamecust,ceconamemut,ad_username,isinmediate,c_creditmemoinfo,referencereason,currency, " +
				"headerdescription,desc_glosa) SELECT ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, c_invreqnumber,doc_type_id,c_distribution, " +
				"org_value,c_bpartnervalue,c_bparnername,c_bparnername2,c_orderreference,referencetype,invoicereference, " +
				"c_dateinvreq,c_dateref,address1,address2,city,productvalue,m_productacct,qty,totallines,orderorg, " +
				"ceconamecust,ceconamemut,ad_username,isinmediate,c_creditmemoinfo,referencereason,currency, " +
				"headerdescription,desc_glosa FROM RVOFB_solpesapview WHERE C_Invoice_ID = "+Invoice_ID;				
		DB.executeUpdate(sqlInsert, po.get_TrxName());
	}
}