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


import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MBankStatement;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
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
public class ModPDVCustodios implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPDVCustodios ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPDVCustodios.class);
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
		
		engine.addDocValidate(MAllocationHdr.Table_Name, this);
		engine.addDocValidate(MInvoice.Table_Name, this);
		engine.addDocValidate(MBankStatement.Table_Name, this);
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
		
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MAllocationHdr.Table_ID)			
		{
			MAllocationHdr allo = (MAllocationHdr)po;
			MAllocationLine[] alines = allo.getLines(false);
			for (int i = 0; i < alines.length; i++)
			{
				MAllocationLine aline = alines[i];				
				if(aline.getC_Payment_ID()>0)
				{
					MPayment pay = new MPayment(po.getCtx(), aline.getC_Payment_ID(), po.get_TrxName());
					pay.set_CustomColumn("custodio_old", pay.get_ValueAsString("custodio"));
					pay.set_CustomColumn("custodio", "A");
					pay.set_CustomColumn("DocTypeName", "Asignacion de Pago");
					pay.save();
				}
				if(aline.getC_Invoice_ID()>0)
				{
					MInvoice inv = new MInvoice(po.getCtx(), aline.getC_Invoice_ID(), po.get_TrxName());
					inv.set_CustomColumn("custodio_old", inv.get_ValueAsString("custodio"));
					inv.set_CustomColumn("custodio", "A");
					inv.set_CustomColumn("DocTypeName", "Asignacion de Pago");
					inv.save();
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
				String cusOld = "";
				if(aline.getC_Payment_ID()>0)
				{
					MPayment pay = new MPayment(po.getCtx(), aline.getC_Payment_ID(), po.get_TrxName());
					cusOld = pay.get_ValueAsString("custodio_old");					
					pay.set_CustomColumn("custodio_old", pay.get_ValueAsString("custodio"));
					int ind = 0;
					if (cusOld != null && cusOld != "" && cusOld != " ")
					{
						pay.set_CustomColumn("custodio", cusOld);
						ind = 1;
					}
					pay.save();
					if (ind == 0)
					{
						DB.executeUpdate("UPDATE C_Payment set Custodio=null WHERE C_Payment_ID = "+pay.get_ID(),po.get_TrxName());							
					}
					//pay.set_CustomColumn("DocTypeName", "Asignacion de Pago");
				}
				if(aline.getC_Invoice_ID()>0)
				{
					MInvoice inv = new MInvoice(po.getCtx(), aline.getC_Invoice_ID(), po.get_TrxName());
					cusOld = inv.get_ValueAsString("custodio_old");
					inv.set_CustomColumn("custodio_old", inv.get_ValueAsString("custodio"));
					int ind = 0;
					if (cusOld != null && cusOld != "" && cusOld != " ")
					{
						inv.set_CustomColumn("custodio", cusOld);
						ind = 1;
					}
					inv.save();
					if (ind == 0)
					{
						DB.executeUpdate("UPDATE C_Invoice set Custodio=null WHERE C_Invoice_ID = "+inv.get_ID(),po.get_TrxName());							
					}
					//inv.set_CustomColumn("DocTypeName", "Asignacion de Pago");					
				} 		
			}		
		}	
		
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInvoice.Table_ID)			
		{
			MInvoice inv = (MInvoice)po;
			if (inv.isSOTrx())
			{
				MInvoiceLine[] ilines = inv.getLines(false);
				for (int i = 0; i < ilines.length; i++)
				{
					MInvoiceLine iline = ilines[i];
					String cus = "";
					String glosa = "";
					String auxGlosa = ""; 
					if (inv.getC_DocType_ID() == 1000102)
					{
						cus = "B";					
						auxGlosa = DB.getSQLValueString(po.get_TrxName(), "SELECT NAME FROM AD_Ref_List WHERE AD_Reference_ID=1000067" +
								"AND value like '"+inv.get_ValueAsString("DepositType")+"'");
						glosa = inv.getC_DocType().getName() + " - "+ auxGlosa;
					}
					else if (inv.getC_DocType_ID() == 1000052)
					{
						cus = "C";
						glosa = inv.getC_DocType().getName();
					}
					else if (inv.getC_DocType_ID() == 1000051)
					{
						cus = "F";
						glosa = inv.getC_DocType().getName();
					}
					else if (inv.getC_DocType_ID() == 1000050 || inv.getC_DocType_ID() == 1000124 || inv.getC_DocType_ID() == 1000122)
					{
						cus = "P";
						glosa = inv.getC_DocType().getName();
					}
					else if (inv.getC_DocType().getDocBaseType().compareTo("CDC") == 0)
					{
						cus = "C";
						glosa = inv.getC_DocType().getName();
					}
					else
					{
						glosa = inv.getC_DocType().getName();
					}
					if(iline.get_ValueAsInt("C_Payment_ID") > 0)
					{
						MPayment pay = new MPayment(po.getCtx(), iline.get_ValueAsInt("C_Payment_ID"), po.get_TrxName());
						pay.set_CustomColumn("custodio_old", pay.get_ValueAsString("custodio"));
						if (cus != null && cus != "" && cus != " ")
							pay.set_CustomColumn("custodio", cus);
						pay.set_CustomColumn("DocTypeName", glosa);
						pay.save();
					}
					if(iline.get_ValueAsInt("c_invoicefac_id")>0)
					{
						MInvoice invDe = new MInvoice(po.getCtx(), iline.get_ValueAsInt("c_invoicefac_id"), po.get_TrxName());
						invDe.set_CustomColumn("custodio_old", inv.get_ValueAsString("custodio"));
						if (cus != null && cus != "" && cus != " ")
							invDe.set_CustomColumn("custodio", cus);
						invDe.set_CustomColumn("DocTypeName", glosa);
						invDe.save();
					} 						
				}
			}					
		}	
		if(timing == TIMING_AFTER_VOID && po.get_Table_ID()==MInvoice.Table_ID)			
		{
			MInvoice inv = (MInvoice)po;
			if (inv.isSOTrx())
			{
				MInvoiceLine[] ilines = inv.getLines(false);
				for (int i = 0; i < ilines.length; i++)
				{
					MInvoiceLine iline = ilines[i];
					String cusOld = "";
					if(iline.get_ValueAsInt("C_Payment_ID") > 0)
					{
						MPayment pay = new MPayment(po.getCtx(), iline.get_ValueAsInt("C_Payment_ID"), po.get_TrxName());
						cusOld = pay.get_ValueAsString("custodio_old");
						pay.set_CustomColumn("custodio_old", pay.get_ValueAsString("custodio"));
						int ind = 0;
						if (cusOld != null && cusOld != "" && cusOld != " ")
						{
							pay.set_CustomColumn("custodio", cusOld);
							ind = 1;
						}
						pay.save();
						if (ind == 0)
						{
							DB.executeUpdate("UPDATE C_Payment set Custodio=null WHERE C_Payment_ID = "+pay.get_ID(),po.get_TrxName());							
						}
						//pay.set_CustomColumn("DocTypeName", glosa);						
					}
					if(iline.get_ValueAsInt("c_invoicefac_id")>0)
					{
						MInvoice invDe = new MInvoice(po.getCtx(), iline.get_ValueAsInt("c_invoicefac_id"), po.get_TrxName());
						cusOld = invDe.get_ValueAsString("custodio_old");
						invDe.set_CustomColumn("custodio_old", invDe.get_ValueAsString("custodio"));
						int ind = 0;
						if (cusOld != null && cusOld != "" && cusOld != " ")
						{
							invDe.set_CustomColumn("custodio", cusOld);
							ind = 1;
						}
						invDe.save();
						if (ind == 0)
						{
							DB.executeUpdate("UPDATE C_Invoice set Custodio=null WHERE C_Invoice_ID = "+invDe.get_ID(),po.get_TrxName());							
						}
						//invDe.set_CustomColumn("DocTypeName", glosa);
					} 
				}
			}
		}
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MBankStatement.Table_ID)			
		{
			MBankStatement statement = (MBankStatement)po;
			MBankStatementLine[] slines = statement.getLines(false);
			for (int i = 0; i < slines.length; i++)
			{
				MBankStatementLine sline = slines[i];
				if(sline.getC_Payment_ID() > 0)
				{
					MPayment pay = new MPayment(po.getCtx(), sline.getC_Payment_ID(), po.get_TrxName());
					pay.set_CustomColumn("custodio_old", pay.get_ValueAsString("custodio"));
					pay.set_CustomColumn("custodio", "O");
					pay.set_CustomColumn("DocTypeName", "Estado de Cuentas Bancario");
					pay.save();
				}
			}
		}
		if(timing == TIMING_AFTER_VOID && po.get_Table_ID()==MBankStatement.Table_ID)			
		{
			MBankStatement statement = (MBankStatement)po;
			MBankStatementLine[] slines = statement.getLines(false);
			for (int i = 0; i < slines.length; i++)
			{
				MBankStatementLine sline = slines[i];
				if(sline.getC_Payment_ID() > 0)
				{
					MPayment pay = new MPayment(po.getCtx(), sline.getC_Payment_ID(), po.get_TrxName());
					String cusOld = pay.get_ValueAsString("custodio_old");					
					pay.set_CustomColumn("custodio_old", pay.get_ValueAsString("custodio"));
					int ind = 0;
					if (cusOld != null && cusOld != "" && cusOld != " ")
					{
						pay.set_CustomColumn("custodio", cusOld);
						ind = 1;
					}
					pay.save();
					if (ind == 0)
					{
						DB.executeUpdate("UPDATE C_Payment set Custodio=null WHERE C_Payment_ID = "+pay.get_ID(),po.get_TrxName());							
					}
					//pay.set_CustomColumn("DocTypeName", "Estado de Cuentas Bancario");
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