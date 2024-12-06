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
package org.dpp.model;

import java.math.BigDecimal;

import org.compiere.FA.CreateAssetForecast;
import org.compiere.model.MAsset;
import org.compiere.model.MAssetAcct;
import org.compiere.model.MAssetChange;
import org.compiere.model.MClient;
import org.compiere.model.MCurrency;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_A_Asset_Addition;
import org.compiere.model.X_A_Depreciation_Workfile;
import org.compiere.model.X_C_InvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

//import com.sun.ejb.containers.CommitCEntityContainer;

/**
 *	Validator for DPP
 *
 *  @author Italo Niñoles
 */
public class ModDPPUpdateAssetAmt implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModDPPUpdateAssetAmt ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModDPPUpdateAssetAmt.class);
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
		
		engine.addModelChange(MInvoiceLine.Table_Name, this);
		//	Documents to be monitored
		engine.addDocValidate(MInvoice.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MInvoiceLine.Table_ID)
		{
			MInvoiceLine line = (MInvoiceLine)po;
			MInvoice inv = new MInvoice(po.getCtx(), line.getC_Invoice_ID(), po.get_TrxName());
			MCurrency curr = new MCurrency(po.getCtx(), inv.getC_Currency_ID(), po.get_TrxName());			
			if (inv.isSOTrx() == false)
			{
				line.setPrice(line.getPriceEntered().setScale(curr.getStdPrecision(),BigDecimal.ROUND_HALF_EVEN));
				line.setTaxAmt(line.getTaxAmt().setScale(curr.getStdPrecision(),BigDecimal.ROUND_HALF_EVEN));				
			}
		}				
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
		
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInvoice.Table_ID)
		{
			MInvoice inv = (MInvoice)po;
			MCurrency curr = new MCurrency(po.getCtx(), inv.getC_Currency_ID(), po.get_TrxName());
			if (inv.isSOTrx() == false)
			{
				MInvoiceLine[] lines = inv.getLines(false);
				for (int i = 0; i < lines.length; i++)
				{
					MInvoiceLine line = lines[i];
					if (line.getA_Asset_ID() > 0)
					{
						if(line.get_ValueAsInt("A_Asset_Group_ID")>0 && line.get_ValueAsString("A_CapvsExp").equals(X_C_InvoiceLine.A_CAPVSEXP_Capital))
						{	
							MAsset asset = new MAsset(po.getCtx(), line.getA_Asset_ID());
							BigDecimal tax = Env.ZERO;
							
							if(line.getC_Tax().getRate().compareTo(Env.ZERO) > 0)
								tax = line.getTaxAmt();
							
							int acct_id = DB.getSQLValue(po.get_TrxName(), "select A_Asset_Acct_ID from A_Asset_Acct where A_Asset_ID="+asset.getA_Asset_ID());
							MAssetAcct assetacct = new MAssetAcct (po.getCtx(), acct_id, po.get_TrxName());
							
							BigDecimal amt = line.getLineNetAmt().add(tax).divide(line.getQtyEntered(), BigDecimal.ROUND_HALF_EVEN);
							amt = amt.setScale(curr.getStdPrecision(),BigDecimal.ROUND_HALF_EVEN);
							
							log.config("MAssetChange change");
							int change_id = DB.getSQLValue(po.get_TrxName(), "select A_Asset_Change_ID from A_Asset_Change where A_Asset_ID="+asset.getA_Asset_ID());
							MAssetChange change = new MAssetChange (po.getCtx(), change_id, po.get_TrxName());						
							change.setAssetValueAmt(amt);
							change.save();
							
							log.config("X_A_Depreciation_Workfile");
						    int Workfile_id = DB.getSQLValue(po.get_TrxName(), "select A_Depreciation_Workfile_ID from A_Depreciation_Workfile where A_Asset_ID="+asset.getA_Asset_ID());
							X_A_Depreciation_Workfile assetwk = new X_A_Depreciation_Workfile (po.getCtx(), Workfile_id, po.get_TrxName());						
							assetwk.setA_Asset_Cost(amt);						
							assetwk.save();
							
							log.config("X_A_Asset_Addition");
							int Addition_id = DB.getSQLValue(po.get_TrxName(), "select A_Asset_Addition_ID from A_Asset_Addition where A_Asset_ID="+asset.getA_Asset_ID());
							if (Addition_id > 0)
							{
								X_A_Asset_Addition assetadd = new X_A_Asset_Addition (po.getCtx(), Addition_id, po.get_TrxName());
								assetadd.setAssetValueAmt(amt);
								assetadd.save();
							}
							
							DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and Corrected='N' and A_Asset_ID="+asset.getA_Asset_ID(), po.get_TrxName());
							CreateAssetForecast.createForecast(asset, change, assetacct, po.get_TrxName());
						}
						
						if(line.getA_Asset_ID()>0 && line.get_ValueAsString("A_CapvsExp").equals(X_C_InvoiceLine.A_CAPVSEXP_Expense))
						{
							MAsset asset = new MAsset(po.getCtx(),  line.getA_Asset_ID() ,po.get_TrxName());
							//BigDecimal monto = line.getLineNetAmt().add(line.getTaxAmt());
							BigDecimal monto = line.getTaxAmt();
							monto = monto.setScale(curr.getStdPrecision(),BigDecimal.ROUND_HALF_EVEN);
							
							int change_id = DB.getSQLValue(po.get_TableName(), "select A_Asset_Change_ID from A_Asset_Change where A_Asset_ID="+asset.getA_Asset_ID());
							MAssetChange change = new MAssetChange (po.getCtx(), change_id, po.get_TrxName());
							change.setAssetValueAmt(change.getAssetValueAmt().add(monto) );
							change.save();
							
							int Workfile_id = DB.getSQLValue(po.get_TableName(), "select A_Depreciation_Workfile_ID from A_Depreciation_Workfile where A_Asset_ID="+asset.getA_Asset_ID());
							X_A_Depreciation_Workfile assetwk = new X_A_Depreciation_Workfile (po.getCtx(), Workfile_id, po.get_TrxName());
							assetwk.setA_Asset_Cost(assetwk.getA_Asset_Cost().add(monto));
							assetwk.save();
							
							int Addition_id = DB.getSQLValue(po.get_TableName(), "select MAX(A_Asset_Addition_ID) from A_Asset_Addition where A_Asset_ID="+asset.getA_Asset_ID(),po.get_TrxName());
							X_A_Asset_Addition assetadd = new X_A_Asset_Addition (po.getCtx(), Addition_id, po.get_TrxName());
							assetadd.setAssetValueAmt(assetadd.getAssetValueAmt().add(monto));
							assetadd.save();
							
							int acct_id = DB.getSQLValue(po.get_TableName(), "select A_Asset_Acct_ID from A_Asset_Acct where A_Asset_ID="+asset.getA_Asset_ID(),po.get_TrxName());
							MAssetAcct assetacct = new MAssetAcct (po.getCtx(), acct_id, po.get_TrxName());
														
							DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and Corrected='N' and A_Asset_ID="+asset.getA_Asset_ID(), po.get_TrxName());
							CreateAssetForecast.createForecast(asset, change, assetacct, po.get_TrxName());
						}
					}				
				}				
			}
			
			//validacion descuadre de pesos por decimales
			BigDecimal amtSumLine = new BigDecimal("0.0");
			BigDecimal amtSumLineNA = new BigDecimal("0.0");
			String sqlSum = "SELECT SUM(LineTotalAmt) FROM C_InvoiceLine WHERE " +
					"A_Asset_ID IS NOT NULL AND C_Invoice_ID = "+inv.get_ID();
			amtSumLine = DB.getSQLValueBD(po.get_TrxName(), sqlSum);
			
			String sqlSumNA = "SELECT SUM(LineTotalAmt) FROM C_InvoiceLine WHERE " +
			"A_Asset_ID IS NULL AND C_Invoice_ID = "+inv.get_ID();
			amtSumLineNA = DB.getSQLValueBD(po.get_TrxName(), sqlSumNA);
			
			if (amtSumLine == null)
				amtSumLine = Env.ZERO;
			if (amtSumLineNA == null)
				amtSumLineNA = Env.ZERO;				
			
			if(amtSumLine.add(amtSumLineNA).compareTo(inv.getGrandTotal()) != 0)
			{
				int ID_LastAsset = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(A_Asset_ID) " +
						" FROM C_InvoiceLine WHERE A_Asset_ID IS NOT NULL AND C_Invoice_ID = "+inv.get_ID());
				
				//hacemos proceso de seteat montos
				BigDecimal diffCurrency = inv.getGrandTotal().subtract(amtSumLine.add(amtSumLineNA));
				
				MAsset asset = new MAsset(po.getCtx(), ID_LastAsset);
				//BigDecimal tax = Env.ZERO;
								
				int acct_id = DB.getSQLValue(po.get_TrxName(), "select A_Asset_Acct_ID from A_Asset_Acct where A_Asset_ID="+asset.getA_Asset_ID());
				MAssetAcct assetacct = new MAssetAcct (po.getCtx(), acct_id, po.get_TrxName());
				
				log.config("MAssetChange change");
				int change_id = DB.getSQLValue(po.get_TrxName(), "select A_Asset_Change_ID from A_Asset_Change where A_Asset_ID="+asset.getA_Asset_ID());
				MAssetChange change = new MAssetChange (po.getCtx(), change_id, po.get_TrxName());						
				change.setAssetValueAmt(change.getAssetValueAmt().add(diffCurrency));
				change.save();
				
				log.config("X_A_Depreciation_Workfile");
			    int Workfile_id = DB.getSQLValue(po.get_TrxName(), "select A_Depreciation_Workfile_ID from A_Depreciation_Workfile where A_Asset_ID="+asset.getA_Asset_ID());
				X_A_Depreciation_Workfile assetwk = new X_A_Depreciation_Workfile (po.getCtx(), Workfile_id, po.get_TrxName());						
				assetwk.setA_Asset_Cost(assetwk.getA_Asset_Cost().add(diffCurrency));						
				assetwk.save();
				
				log.config("X_A_Asset_Addition");
				int Addition_id = DB.getSQLValue(po.get_TrxName(), "select A_Asset_Addition_ID from A_Asset_Addition where A_Asset_ID="+asset.getA_Asset_ID());
				if (Addition_id > 0)
				{
					X_A_Asset_Addition assetadd = new X_A_Asset_Addition (po.getCtx(), Addition_id, po.get_TrxName());
					assetadd.setAssetValueAmt(assetadd.getAssetValueAmt().add(diffCurrency));
					assetadd.save();
				}
				
				DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and Corrected='N' and A_Asset_ID="+asset.getA_Asset_ID(), po.get_TrxName());
				CreateAssetForecast.createForecast(asset, change, assetacct, po.get_TrxName());
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
