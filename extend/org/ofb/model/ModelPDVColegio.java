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
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MCharge;
import org.compiere.model.MClient;
import org.compiere.model.MConversionRate;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentTerm;
import org.compiere.model.MProductPricing;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_OrderPaySchedule;
import org.compiere.model.X_EO_Agreement;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for PDV Colegios
 *
 *  @author Fabian Aguilar
 */
public class ModelPDVColegio implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelPDVColegio ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelPDVColegio.class);
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
		engine.addModelChange(MOrder.Table_Name, this); 
		engine.addModelChange(MOrderLine.Table_Name, this); //
		engine.addModelChange(MInvoice.Table_Name, this);
		engine.addModelChange(MInvoiceLine.Table_Name, this);
		engine.addModelChange(MInvoicePaySchedule.Table_Name, this);
		//	Documents to be monitored
		engine.addDocValidate(MInvoice.Table_Name, this);
		engine.addDocValidate(MPayment.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farías
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if(  type == TYPE_AFTER_CHANGE && po.get_Table_ID()==MOrder.Table_ID && po.is_ValueChanged("C_PaymentTerm_ID")) // Cambio Término de Pago Matrícula
		{
			
			//MPaymentTerm term = new MPaymentTerm(po.getCtx(), po.get_ValueAsInt("C_PaymentTerm_ID"), po.get_TrxName());
			MOrder order = (MOrder)po;
			DB.executeUpdate("delete from C_OrderPaySchedule where C_Order_ID="+order.getC_Order_ID(), po.get_TrxName());
			/*for(MOrderLine line : order.getLines())
			{
				if(line.getM_Product_ID()>0)
					if(line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("MATRÍCULA")>=0 || line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("MATRICULA")>=0)
					{
						line.setPrice(line.getPriceList().subtract(line.getPriceList().multiply(term.getDiscount().divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP))) );
						line.setDiscount(term.getDiscount());
						line.setLineNetAmt();
						line.save();
					}
				
			}*/ //no descuento para matricula
		
		}
		
		if(  type == TYPE_AFTER_CHANGE && po.get_Table_ID()==MOrder.Table_ID && po.is_ValueChanged("C_PaymentTerm2_ID")) // Término de Pago Colegiatura
		{
			MPaymentTerm term = new MPaymentTerm(po.getCtx(), po.get_ValueAsInt("C_PaymentTerm2_ID"), po.get_TrxName());
			MOrder order = (MOrder)po;
			DB.executeUpdate("delete from C_OrderPaySchedule where C_Order_ID="+order.getC_Order_ID(), po.get_TrxName());
			for(MOrderLine line : order.getLines())
			{
				if(line.getM_Product_ID()>0)
					if(line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("COLEGIATURA")>=0)
					{
						line.setPrice(line.getPriceList().subtract(line.getPriceList().multiply(term.getDiscount().divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP))) );
						line.setDiscount(term.getDiscount());
						line.setLineNetAmt();
						line.save();
					}
				
			}
		
		}
		
		
		if( (type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MOrderLine.Table_ID)// Antes de guardar o modificar una linea
		{
			MOrderLine line = (MOrderLine)po;
			MOrder order = line.getParent();
			if(!order.isSOTrx())
				return null;
			MPaymentTerm termco =  new MPaymentTerm(po.getCtx(), order.get_ValueAsInt("C_PaymentTerm2_ID"), po.get_TrxName());
			MPaymentTerm termma = new MPaymentTerm(po.getCtx(), order.get_ValueAsInt("C_PaymentTerm_ID"), po.get_TrxName());
			if(line.getM_Product_ID()>0)
			{
				if(line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("COLEGIATURA")>=0)
				{
					if(line.get_ValueAsInt("EO_Agreement_ID")>0)
					{
						X_EO_Agreement agree = new X_EO_Agreement(line.getCtx(), line.get_ValueAsInt("EO_Agreement_ID"),line.get_TrxName());
						BigDecimal tmp = Env.ONEHUNDRED.subtract(Env.ONEHUNDRED.multiply(termco.getDiscount().divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP)));
						tmp = tmp.subtract(tmp.multiply(agree.getDiscount().divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP)));
						BigDecimal totalDiscount = Env.ONEHUNDRED.subtract(tmp);
						
						line.setPrice(line.getPriceList().subtract(line.getPriceList().multiply(totalDiscount.divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP))) );
						line.setDiscount(totalDiscount);
						line.setLineNetAmt();
					}
					else
					{
					line.setPrice(line.getPriceList().subtract(line.getPriceList().multiply(termco.getDiscount().divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP))) );
					line.setDiscount(termco.getDiscount());
					line.setLineNetAmt();
					}
				}
				if(line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("MATRÍCULA")>=0 || line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("MATRICULA")>=0)
				{
					// si trae precio 0 tomar precio limite en uf begin
					if(line.getPriceList().signum()==0 && order.getM_PriceList().getC_Currency().getISO_Code().equals("CLP"))
					{
						MProductPricing pp = new MProductPricing (line.getM_Product_ID(), order.getC_BPartner_ID(), line.getQtyEntered(), order.isSOTrx());
						pp.setM_PriceList_ID(order.getM_PriceList_ID());
						int M_PriceList_Version_ID = 0;
						String sql = "SELECT plv.M_PriceList_Version_ID "
								+ "FROM M_PriceList_Version plv "
								+ "WHERE plv.M_PriceList_ID=? "						//	1
								+ " AND plv.ValidFrom <= ? "
								+ "ORDER BY plv.ValidFrom DESC";
							//	Use newest price list - may not be future
						M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, order.getM_PriceList_ID(), order.getDateOrdered());
						pp.setM_PriceList_Version_ID(M_PriceList_Version_ID);
						pp.setPriceDate(order.getDateOrdered());
						
						BigDecimal amt = MConversionRate.convertBase(line.getCtx(), pp.getPriceLimit(),	//	CM adjusted
								1000000, order.getDateOrdered(), 114, order.getAD_Client_ID(), order.getAD_Org_ID());
						
						if(amt==null)
							return "No existe tasa de cambio para UF, no se puede completar la operacion";
						
						
						line.setPriceList(amt);
						line.setPrice(amt);
						line.setLineNetAmt();
					}
					// si trae precio 0 tomar precio limite en uf end
					//line.setPrice(line.getPriceList().subtract(line.getPriceList().multiply(termma.getDiscount().divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP))) );
					//line.setDiscount(termma.getDiscount());
					
				}
				
			}
		}
		
		if(type == TYPE_BEFORE_NEW && po.get_Table_ID()==MInvoice.Table_ID)
		{
			MInvoice inv = (MInvoice)po;
			
			if(inv.getC_Order_ID()>0 && inv.isSOTrx() && inv.getC_DocTypeTarget().getDocBaseType().equals("ARI"))
			{
				MOrder order = new MOrder(po.getCtx(), inv.getC_Order_ID(), po.get_TrxName());
				inv.set_CustomColumn("C_PaymentTerm2_ID", order.get_Value("C_PaymentTerm2_ID"));
				inv.set_CustomColumn("PaymentRule2", order.get_Value("PaymentRule2"));
			}
		}
		
		if(type == TYPE_BEFORE_NEW && po.get_Table_ID()==MInvoiceLine.Table_ID)
		{
			MInvoiceLine inv = (MInvoiceLine)po;
			if(inv.getC_OrderLine_ID()>0)
			{
				MOrderLine ol = new MOrderLine(po.getCtx(), inv.getC_OrderLine_ID(), po.get_TrxName());
				inv.set_CustomColumn("AD_User_ID", ol.get_Value("AD_User_ID"));
				inv.set_CustomColumn("EO_Agreement_ID", ol.get_Value("EO_Agreement_ID"));
			}
			
		}
		
		if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID()==MInvoicePaySchedule.Table_ID)
		{
			MInvoicePaySchedule is = (MInvoicePaySchedule)po;
			
			if(is.get_Value("AllocatedAmt")!=null)
			{
				if(((BigDecimal)is.get_Value("AllocatedAmt")).compareTo(is.getDueAmt())>=0)
				is.set_CustomColumn("IsPaid", true);
			}
		}
		
		if(type == TYPE_BEFORE_NEW && po.get_Table_ID()==MInvoicePaySchedule.Table_ID)
		{
			MInvoicePaySchedule is = (MInvoicePaySchedule)po;
			
			is.set_CustomColumn("Name", is.getDueDate().toString() +"-"+ is.getDueAmt());
			if(is.get_Value("ForeignPrice")==null)
			{
				MInvoice in =MInvoice.get(is.getCtx(), is.getC_Invoice_ID());
				if(in.get_Value("ForeignPrice")!=null && ((BigDecimal)in.get_Value("ForeignPrice")).signum()>0)
				{
					is.set_CustomColumn("ForeignPrice", ((BigDecimal)in.get_Value("ForeignPrice")).multiply( is.getC_PaySchedule().getPercentage().divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP)    ));
				}
			}
		}
		
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==MOrder.Table_ID) // Cambio regla de pago colegiatura 
		{			
			MOrder order = (MOrder)po;
			if(!order.isSOTrx())
				return null;
			
			if(order.getDocStatus().equalsIgnoreCase("DR") || order.getDocStatus().equalsIgnoreCase("IP"))
			{			
				if(order.isSOTrx())
				{			
					if(order.get_ValueAsString("PaymentRule2").equalsIgnoreCase("P"))
					{	
						agregaCargoPagare(order);
					}			
					else
					{
						String sql3 = "SELECT max(C_Charge_ID) FROM C_Charge WHERE upper(TipoCargo)='TC13'  and isactive='Y' and AD_client_ID=" +order.getAD_Client_ID();
						int C_ChargeChange_ID = DB.getSQLValue("C_Charge",sql3); 
						if(C_ChargeChange_ID>0)
						{
							DB.executeUpdate("delete from C_OrderLine where C_Order_ID="+order.getC_Order_ID() +" and C_Charge_ID = "+C_ChargeChange_ID, po.get_TrxName());
						}
					}
				}
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
		
			if(inv.getC_Order_ID()>0 && inv.isSOTrx() && inv.getC_DocType().getDocBaseType().equals("ARI"))//factura de venta
			{
				DB.executeUpdate("Delete from C_InvoicePaySchedule where C_Invoice_ID="+inv.getC_Invoice_ID(), po.get_TrxName());
				PreparedStatement pstmt = null;
				try
				{
					pstmt = DB.prepareStatement("select C_OrderPaySchedule_ID from C_OrderPaySchedule where C_Order_ID="+inv.getC_Order_ID(), po.get_TrxName());
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
						X_C_OrderPaySchedule os=new X_C_OrderPaySchedule(inv.getCtx(), rs.getInt("C_OrderPaySchedule_ID"), inv.get_TrxName());
						MInvoicePaySchedule ip = new MInvoicePaySchedule(inv.getCtx(), 0, inv.get_TrxName());
						ip.setC_Invoice_ID(inv.getC_Invoice_ID());
						ip.setC_PaySchedule_ID(os.get_ValueAsInt("C_PaySchedule_ID"));
						ip.setDueAmt (os.getDueAmt());
						ip.set_CustomColumn("ForeignPrice",os.get_Value("ForeignPrice"));
						ip.setDiscountAmt (Env.ZERO);
						ip.setDueDate (os.getDueDate());
						ip.setDiscountDate (os.getDueDate());
						ip.save();
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "getInvoicePaySchedule", e); 
				}
				try
				{
					if (pstmt != null)
						pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					pstmt = null;
				}
			}//end if
			
			
			if(inv.isSOTrx() && inv.getC_DocType().getDocBaseType().equals("CDC"))//pagare
			{
				DB.executeUpdate("Delete from C_InvoicePaySchedule where C_Invoice_ID="+inv.getC_Invoice_ID(), po.get_TrxName());
				PreparedStatement pstmt = null;
				try
				{
					pstmt = DB.prepareStatement("select C_OrderPaySchedule_ID from C_OrderPaySchedule where C_Order_ID="+inv.getC_Order_ID()+" and IsMatricula='N' ", po.get_TrxName());
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
						X_C_OrderPaySchedule os=new X_C_OrderPaySchedule(inv.getCtx(), rs.getInt("C_OrderPaySchedule_ID"), inv.get_TrxName());
						MInvoicePaySchedule ip = new MInvoicePaySchedule(inv.getCtx(), 0, inv.get_TrxName());
						ip.setC_Invoice_ID(inv.getC_Invoice_ID());
						ip.setC_PaySchedule_ID(os.get_ValueAsInt("C_PaySchedule_ID"));
						ip.set_CustomColumn("ForeignPrice",os.get_Value("ForeignPrice"));
						ip.setDueAmt (os.getDueAmt());
						ip.setDiscountAmt (Env.ZERO);
						ip.setDueDate (os.getDueDate());
						ip.setDiscountDate (os.getDueDate());
						ip.save();
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "getInvoicePaySchedule", e); 
				}
				try
				{
					if (pstmt != null)
						pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					pstmt = null;
				}
			}//end if
		}
		
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MPayment.Table_ID)
		{
			MPayment pay = (MPayment)po;
			
			if(pay.get_ValueAsInt("C_InvoicePaySchedule_ID")>0)
			{
				MInvoicePaySchedule is = new MInvoicePaySchedule(po.getCtx(),pay.get_ValueAsInt("C_InvoicePaySchedule_ID"),po.get_TrxName());
				is.set_CustomColumn("AllocatedAmt", pay.getPayAmt());
				is.save();
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


	private void agregaCargoPagare(MOrder order)
	{
		String sql3 = "SELECT max(C_Charge_ID) FROM C_Charge WHERE upper(TipoCargo)='TC13'  and isactive='Y' and AD_client_ID=" +order.getAD_Client_ID();
		int C_ChargeChange_ID = DB.getSQLValue("C_Charge",sql3); 
		if(C_ChargeChange_ID<=0)
			throw new AdempiereException("No existe Cargo para Pagares");
		BigDecimal chargeAmt = MCharge.get(order.getCtx(), C_ChargeChange_ID).getChargeAmt();

		DB.executeUpdate("delete from C_OrderLine where C_Order_ID="+order.getC_Order_ID() +" and C_Charge_ID = "+C_ChargeChange_ID, order.get_TrxName());

		int colegiaturas = 0;
		//	se agrega linea de cargo de pagare			
		String sqlCant = "select count(distinct(C_OrderLine_ID)) FROM C_OrderLine col "+
			"INNER JOIN M_Product mp on (col.M_Product_ID = mp.M_Product_ID) "+
			"INNER JOIN M_Product_Category mpc on (mp.M_Product_Category_ID = mpc.M_Product_Category_ID) "+
			"WHERE col.C_Order_ID = "+order.get_ID()+" AND upper(mpc.name) like '%COLEGIATURA%' ";

		colegiaturas = DB.getSQLValue(order.get_TrxName(), sqlCant);
		
		if (colegiaturas > 0)
		{		
			chargeAmt = chargeAmt.multiply(new BigDecimal(colegiaturas));
			//	UF to CLP
			
			if(order.getC_Currency_ID()!=1000000)//distinto UF
			{
				BigDecimal chargeAmtCal = MConversionRate.convertBase(order.getCtx(), 
						chargeAmt, 1000000, order.getDateOrdered(), 
						114, order.getAD_Client_ID(), order.getAD_Org_ID());
		
				if (chargeAmtCal==null) 
					chargeAmtCal = MConversionRate.convert(order.getCtx(), chargeAmt, 1000000, order.getC_Currency_ID(), order.getAD_Client_ID(), order.getAD_Org_ID());
	
				if(chargeAmtCal==null)
					throw new AdempiereException("No existe tasa de cambio al dia UF");
				
				chargeAmt = chargeAmtCal;
			}
			//
			MOrderLine line = new MOrderLine(order);						
			line.setPriceActual(chargeAmt);
			line.setQty(Env.ONE);
			line.setQtyDelivered(Env.ZERO);
			line.setC_Charge_ID(C_ChargeChange_ID);
			line.setLineNetAmt();
			line.save();
		}	
		
	}

}	