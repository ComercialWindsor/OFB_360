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
package org.tsm.model;

import java.math.BigDecimal;

import org.compiere.model.MCash;
import org.compiere.model.MCashBook;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_PaymentRequestLine;
import org.compiere.model.X_DM_Document;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company TSM
 *
 *  @author Italo Niñoles
 */
public class ModTSMFondoFijo implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTSMFondoFijo ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTSMFondoFijo.class);
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
		//engine.addModelChange(X_TP_Refund.Table_Name, this);
		//engine.addModelChange(X_TP_RefundHeader.Table_Name, this);
		engine.addModelChange(X_C_PaymentRequestLine.Table_Name, this);
		//	Documents to be monitored
		engine.addDocValidate(MCash.Table_Name, this);
		


	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		/*if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID()==X_TP_Refund.Table_ID
				&& po.is_ValueChanged("DocStatus"))  
		{
			X_TP_Refund refund = (X_TP_Refund) po;
			BigDecimal amt =  DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM TP_RefundLine" +
					" WHERE IsActive = 'Y' AND TP_Refund_ID ="+refund.get_ID());
			if(amt != null && amt.compareTo(Env.ZERO) > 0)
			{
				//generacion de resolucion
				X_DM_Document doc = new X_DM_Document(po.getCtx(), 0, po.get_TrxName());
				doc.setAD_Org_ID(refund.getAD_Org_ID());
				doc.set_CustomColumn("C_BPartner_ID",refund.getC_BPartner_ID());
				doc.setAmt(amt);
				//doc.setDescription("Generado desde viatico "+refund.getDocumentNo());
				String desc = DB.getSQLValueString(po.get_TrxName(), "SELECT rlt.name FROM AD_Ref_List rl " +
						" INNER JOIN AD_Ref_List_Trl rlt ON (rl.AD_Ref_List_ID = rlt.AD_Ref_List_ID AND AD_Language='es_CL') " +
						" WHERE AD_Reference_ID=1000092 AND rl.VALUE = '"+refund.getType()+"'");
				if(desc != null && desc.trim().length() > 3)
					doc.setDescription("Tipo: "+desc);
				doc.set_CustomColumn("TP_Refund_ID", refund.get_ID());
				doc.setDateTrx(refund.getDateDoc());
				doc.setDocStatus("CO");
				doc.setProcessed(true);
				doc.saveEx();
				//refund.set_CustomColumn("DM_Document_ID",doc.get_ID());
			}			
		}*/
		/*if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID()==X_TP_RefundHeader.Table_ID
				&& po.is_ValueChanged("DocStatus"))  
		{
			X_TP_RefundHeader refundHead = (X_TP_RefundHeader) po;
			BigDecimal amt =  DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM TP_RefundLine rl" +
					" INNER JOIN TP_Refund rf ON (rl.TP_Refund_ID = rf.TP_Refund_ID) " +
					" WHERE rl.IsActive = 'Y' AND rf.TP_RefundHeader_ID ="+refundHead.get_ID());
			if(amt != null && amt.compareTo(Env.ZERO) > 0)
			{
				//generacion de resolucion
				X_DM_Document doc = new X_DM_Document(po.getCtx(), 0, po.get_TrxName());
				doc.setAD_Org_ID(refundHead.getAD_Org_ID());
				doc.set_CustomColumn("C_BPartner_ID",refundHead.getC_BPartner_ID());
				doc.setAmt(amt);
				//doc.setDescription("Generado desde viatico "+refund.getDocumentNo());
				String desc = DB.getSQLValueString(po.get_TrxName(), "SELECT rlt.name FROM AD_Ref_List rl " +
						" INNER JOIN AD_Ref_List_Trl rlt ON (rl.AD_Ref_List_ID = rlt.AD_Ref_List_ID AND AD_Language='es_CL') " +
						" WHERE AD_Reference_ID=1000092 AND rl.VALUE = '"+refundHead.getType()+"'");
				if(desc != null && desc.trim().length() > 3)
					doc.setDescription("Tipo: "+desc);
				doc.set_CustomColumn("TP_RefundHeader_ID", refundHead.get_ID());
				doc.setDateTrx(refundHead.getDateEnd());
				doc.setDocStatus("CO");
				doc.setProcessed(true);
				doc.saveEx();
				//refund.set_CustomColumn("DM_Document_ID",doc.get_ID());
			}			
		}*/
		
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW || type == TYPE_AFTER_DELETE )
				&& po.get_Table_ID()==X_C_PaymentRequestLine.Table_ID)  
		{
			X_C_PaymentRequestLine rLine = (X_C_PaymentRequestLine) po;
			if(rLine.get_ValueAsInt("DM_Document_ID") > 0)
			{
				X_DM_Document doc = new X_DM_Document(po.getCtx(), rLine.get_ValueAsInt("DM_Document_ID"), po.get_TrxName());
				BigDecimal allocatedAmt = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(Amt) FROM C_PaymentRequestLine" +
						" WHERE DM_Document_ID="+doc.get_ID());
				if(allocatedAmt == null)
					allocatedAmt = Env.ZERO;
				doc.set_CustomColumn("AllocatedAmt",allocatedAmt);
				doc.saveEx();
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
		/*if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MCash.Table_ID)  
		{
			MCash cash = (MCash) po;			
			BigDecimal amt =  DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amount) FROM C_CashLine" +
					" WHERE CashType = 'R' AND IsActive = 'Y' AND C_Cash_ID ="+cash.get_ID());
			if(amt != null && amt.compareTo(Env.ZERO) != 0)
			{
				//se agrega linea inversa
				MCashLine line = new MCashLine(cash);
				line.setAmount(amt.negate());
				line.setCashType("R");
				line.save();
			}			
		}		*/
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MCash.Table_ID)  
		{
			MCash cash = (MCash) po;
			//ahora se vera por marca en libro de efectivo si se genera o no resolucion
			if(cash.getC_CashBook_ID() > 0)
			{
				MCashBook cashBook = new MCashBook(po.getCtx(), cash.getC_CashBook_ID(), po.get_TrxName());
				if(cashBook.get_ValueAsString("TypeBook").compareTo("FF") == 0)
				{
					BigDecimal amt =  DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amount) FROM C_CashLine" +
							" WHERE IsActive = 'Y' AND C_Cash_ID ="+cash.get_ID());
					if(amt != null && amt.compareTo(Env.ZERO) > 0)
					{
						//generacion de resolucion
						X_DM_Document doc = new X_DM_Document(po.getCtx(), 0, po.get_TrxName());
						doc.setAD_Org_ID(cash.getAD_Org_ID());
						doc.set_CustomColumn("C_BPartner_ID",cashBook.get_ValueAsInt("C_BPartner_ID"));
						doc.setAmt(amt);
						doc.setDescription("Generado desde diario de efectivo "+cash.getDocumentNo());
						doc.set_CustomColumn("C_Cash_ID", cash.get_ID());
						doc.setDateTrx(cash.getStatementDate());
						doc.setDocStatus("CO");
						doc.setProcessed(true);
						doc.saveEx();
						cash.set_CustomColumn("DM_Document_ID", doc.get_ID());
						updateHeader(cash);
						cash.saveEx();
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
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString
	private boolean updateHeader(MCash cash)
	{
		String sql = "SELECT COALESCE(SUM(cl.Amount),0) "
				+ "FROM C_CashLine cl "
				+ "WHERE cl.IsActive='Y'"
				+ " AND cl.C_Cash_ID=" + cash.getC_Cash_ID();
		BigDecimal StatementDifference = DB.getSQLValueBD(cash.get_TrxName(), sql);
		cash.setStatementDifference(StatementDifference);
		cash.setEndingBalance(cash.getBeginningBalance().add(StatementDifference));
		cash.saveEx();
		return true;
	}	//	updateHeader

	

}	
