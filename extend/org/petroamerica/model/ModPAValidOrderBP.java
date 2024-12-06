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

import java.math.BigDecimal;

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MRole;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for PDV Colegios
 *
 *  @author Italo Niñoles
 */
public class ModPAValidOrderBP implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPAValidOrderBP ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPAValidOrderBP.class);
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
		engine.addDocValidate(MOrder.Table_Name, this);		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MOrder.Table_ID) 
		{	
			MOrder order = (MOrder)po;
			if(order.isSOTrx())
			{
				//flag si no es solicitud de devolucion
				int flag = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) " +
						" FROM C_DocType WHERE C_DocType_ID = "+order.getC_DocType_ID()+"" +
						" AND C_DocType_ID IN ( SELECT dt.C_DocType_ID FROM C_DocType dt " +
						" INNER JOIN C_DocType dt2 ON (dt.C_DocTypeInvoice_ID = dt2.C_DocType_ID)" +
						" WHERE dt2.DocBaseType = 'ARC')");
				
				//calculamos cantidad de doc vencidos
			
				/*String sqlAmt = "SELECT SUM(invoiceopen(c_invoice_ID,c_invoicepayschedule_id)) as amt" +
						" FROM C_Invoice_v WHERE AD_Client_ID = "+Env.getAD_Client_ID(po.getCtx())+
						" AND duedate < now() AND issotrx = 'Y' AND DocStatus NOT IN ('VO') " +
						" AND C_BPartner_ID = "+order.getC_BPartner_ID();*/
				String sqlAmt = "select qty from rvofb_authorization where C_BPartner_ID = "+order.getC_BPartner_ID();
				BigDecimal amt = DB.getSQLValueBD(po.get_TrxName(), sqlAmt);
				if(amt == null)
					amt = Env.ZERO;
				
				//calculamos dias de vencimiento
				/*String sqlDays = "SELECT MAX(extract(days from now()-duedate)) as days " +
						" FROM C_Invoice_v WHERE AD_Client_ID = "+Env.getAD_Client_ID(po.getCtx())+
						" AND duedate < now() AND issotrx = 'Y' AND DocStatus NOT IN ('VO') AND C_BPartner_ID = "+order.getC_BPartner_ID()+
						" AND invoiceopen(c_invoice_ID,c_invoicepayschedule_id) > 0";*/
				//String sqlDays = "select coalesce(day,0) from rvofb_authorization where C_BPartner_ID = "+order.getC_BPartner_ID();
				//ininoles se cambia campo donde se lee los dias
				//ininoles 16-09-2020 ya no se usaran validaciones con campo day2
				//String sqlDays = "select coalesce(day2,0) from rvofb_authorization where C_BPartner_ID = "+order.getC_BPartner_ID();
				//int days = DB.getSQLValue(po.get_TrxName(), sqlDays);
				int days = 0;
				
				String sqlAmtTotal = "select coalesce(dueAmt,0) from rvofb_authorization where C_BPartner_ID = "+order.getC_BPartner_ID();
				BigDecimal amtTotal = DB.getSQLValueBD(po.get_TrxName(), sqlAmtTotal);
				if(amtTotal == null)
					amtTotal = Env.ZERO;
				
				if(flag < 1)
				{
					//obtenemos tipo de rol
					boolean isGG = false;
					boolean isGV = false;
					MRole rol = new MRole(po.getCtx(), Env.getAD_Role_ID(po.getCtx()), po.get_TrxName());
					if(rol.getDescription() != null && rol.getDescription().toLowerCase().contains("gerente general"))
						isGG = true;
					else if(rol.getDescription() != null && rol.getDescription().toLowerCase().contains("gerente de venta"))
						isGV = true;
					
					//ininoles se setean valores a comparar 
					int DiasMora = 0;
					BigDecimal MontoMora = new BigDecimal("5000000.00"); 
					MBPartner part = new MBPartner(po.getCtx(), order.getC_BPartner_ID(), po.get_TrxName());
					if(part.get_ValueAsString("CreditGroup").compareTo("01") == 0)
						DiasMora = 7;
					else if(part.get_ValueAsString("CreditGroup").compareTo("02") == 0
							|| part.get_ValueAsString("CreditGroup").compareTo("03") == 0)
					{
						DiasMora = 7;
						MontoMora = new BigDecimal("3000000.00"); 
					}
					
					if(isGV == false && isGG == false && (amt.compareTo(Env.ONE) >= 0 || amtTotal.compareTo(Env.ONE) >= 0))
						return  "Orden necesita autorizacion: Cant doc vencidos: "+amt.intValue()+", Dias de atraso: "+days;
					if(isGV == false && isGG == false && days > 0)
						return  "Orden necesita autorizacion: Cant doc vencidos: "+amt.intValue()+", Dias de atraso: "+days;		
					if(isGV && amtTotal.compareTo(MontoMora) > 0)
						return  "Orden necesita autorizacion: Cant doc vencidos: "+amt.intValue()+", Dias de atraso: "+days+", Monto Total mora: "+amtTotal.intValue();
					if(isGV && days > DiasMora)
						return  "Orden necesita autorizacion: Cant doc vencidos: "+amt.intValue()+", Dias de atraso: "+days;
				}
				
				//validamos direccion valida
				MBPartnerLocation bLoc = new MBPartnerLocation(po.getCtx(), order.getC_BPartner_Location_ID(), po.get_TrxName());
				if(!bLoc.get_ValueAsBoolean("IsValid"))
				{
					/*DB.executeUpdate("UPDATE C_Order SET DocStatus = 'IN' WHERE C_Order_ID = "+order.get_ID(), po.get_TrxName());
					order.setDocStatus("IN");
					order.save();
					log.saveWarning("Error: Dirección No Válida", "");*/
					return "Error: Dirección No Válida";
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