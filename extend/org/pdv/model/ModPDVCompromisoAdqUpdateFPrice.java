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
package org.pdv.model;


import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;


/**
 *	Validator for PDV Colegios
 *
 *  @author Fabian Aguilar
 */
public class ModPDVCompromisoAdqUpdateFPrice implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPDVCompromisoAdqUpdateFPrice ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPDVCompromisoAdqUpdateFPrice.class);
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
		//	Documents to be monitored
		
		engine.addDocValidate(MInvoice.Table_Name, this);		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farías
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
			
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInvoice.Table_ID)			
		{
			MInvoice inv = (MInvoice)po;
			String docbase=inv.getDocBase();
			
			if(docbase.equals("APA"))
			{
				if(inv.isSOTrx()==false)
				{
					String date1=inv.getDateAcct().toString().substring(0,10);
					String mysql="Select nvl(max(I.c_invoicecorrelative),0) FROM C_INVOICE I "
					 +" WHere i.AD_CLIENT_ID="+ inv.getAD_Client_ID() +" ANd "
					 +" TRIM(TO_Char(i.DATEacct,'mm'))= '"+ date1.substring(5,7)+"'"
					 +" AND	i.ISSOTRX='N' AND TRIM(TO_Char(i.DATEacct,'yyyy'))= '"+ date1.substring(0,4)+"'"
					 +" and i.DOCSTATUS IN ('CO','CL') and Isactive='Y' AND i.AD_Org_ID ="+inv.getAD_Org_ID();

					int nextcorr=DB.getSQLValue("C_Invoice",mysql);
				     if(nextcorr==0)
				     {
				  	     DB.executeUpdate("update c_invoice set c_invoicecorrelative="+1+" where c_invoice_id="+inv.getC_Invoice_ID(),inv.get_TrxName());
				     }
				     else
				     {
				         nextcorr=nextcorr+1;
				         DB.executeUpdate("update c_invoice set c_invoicecorrelative="+nextcorr+" where c_invoice_id="+inv.getC_Invoice_ID(),inv.get_TrxName());
				     }
				}
	        }		
		}		
		
		/*if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MPayment.Table_ID)			
		{
			MPayment pay = (MPayment)po;
			String docbase=pay.getC_DocType().getDocBaseType();
			
			if(docbase.equals("APP"))
			{
				if(pay.isReceipt()==false)
				{
					String date1=pay.getDateAcct().toString().substring(0,10);
					String mysql="Select nvl(max(p.c_paymentcorrelative),0) FROM C_Payment p "
					 +" WHere p.AD_CLIENT_ID="+ pay.getAD_Client_ID() +" AND "
					 +" TRIM(TO_Char(p.DATEacct,'mm'))= '"+ date1.substring(5,7)+"'"
					 +" AND	p.isreceipt='N' AND TRIM(TO_Char(p.DATEacct,'yyyy'))= '"+ date1.substring(0,4)+"'"
					 +" and p.DOCSTATUS IN ('CO','CL') and p.Isactive='Y' AND p.AD_Org_ID ="+pay.getAD_Org_ID();

					int nextcorr=DB.getSQLValue("C_Payment",mysql);
				     if(nextcorr==0)
				     {
				  	     DB.executeUpdate("update c_payment set c_paymentcorrelative="+1+" where c_payment_id="+pay.getC_Payment_ID(),pay.get_TrxName());
				     }
				     else
				     {
				         nextcorr=nextcorr+1;
				         DB.executeUpdate("update c_payment set c_paymentcorrelative="+nextcorr+" where c_payment_id="+pay.getC_Payment_ID(),pay.get_TrxName());
				     }
				}
	        }		
		}*/
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