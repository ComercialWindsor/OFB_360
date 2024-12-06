/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.pdv.process;

import java.math.*;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import com.sun.corba.ee.org.apache.bcel.generic.NEWARRAY;
import com.sun.imageio.plugins.common.BogusColorSpace;

/**
 *	
 *	
 *  @author Italo Niñoles Ininoles
 *  @version $Id: ProcessPaymentRequest.java,v 1.2 2014/08/26 10:11:01  $
 */
public class UpdateAmtCOrder extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int p_Order_ID;
	private Properties 		m_ctx;
	private Timestamp p_DateDoc = null;
	
	protected void prepare()
	{
		p_Order_ID=getRecord_ID();
		m_ctx = Env.getCtx();
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DateDoc"))
				p_DateDoc = (Timestamp)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		MOrder order = new MOrder(getCtx(), p_Order_ID, get_TrxName());
		
		if (p_DateDoc == null)
		{
			p_DateDoc = new Timestamp(System.currentTimeMillis());
		}
		//actualizamos fechas de la cabecera de la SO		
		order.setDateOrdered(p_DateDoc);
		order.setDateAcct(p_DateDoc);
		order.save();
		
		MOrderLine[] lines = order.getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MOrderLine line = lines[i];
					
			if(line.getM_Product_ID()>0)
			{
				if(line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("MATRÍCULA")>=0 || line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("MATRICULA")>=0)
				{
					if(order.getC_Currency().getISO_Code().equals("CLP"))
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
						
						pp.setPriceDate(p_DateDoc);
						
						BigDecimal amt = MConversionRate.convertBase(line.getCtx(), pp.getPriceLimit(),	//	CM adjusted
								1000000, p_DateDoc, 114, order.getAD_Client_ID(), order.getAD_Org_ID());
						
						if(amt==null)
							return "No existe tasa de cambio para UF, no se puede completar la operacion";
						//ininoles seteamos valor antiguo
						try{
							line.set_CustomColumn("AmtBeforeProcess", line.getPriceActual());
							//Seteamos precio en UF antiguo (precio limite)
							line.set_CustomColumn("ForeignPrice", pp.getPriceLimit());
						}
						catch (Exception e)
						{
							log.log(Level.SEVERE, e.getMessage(), e);
						}								
						//end ininoles
								
						line.setPriceList(amt);
						line.setPrice(amt);
						line.setLineNetAmt();
						line.save();
					}
				}
				else if(line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("COLEGIATURA")>=0)
				{	
					if(order.getC_Currency().getISO_Code().equals("CLP"))
					{
						MPaymentTerm termco =  new MPaymentTerm(order.getCtx(), order.get_ValueAsInt("C_PaymentTerm2_ID"), order.get_TrxName());
						
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
					
						pp.setPriceDate(p_DateDoc);
					
						BigDecimal amt = MConversionRate.convertBase(line.getCtx(), pp.getPriceLimit(),	//	CM adjusted
							1000000, p_DateDoc, 114, order.getAD_Client_ID(), order.getAD_Org_ID());
					
						if(amt==null)
							return "No existe tasa de cambio para UF, no se puede completar la operacion";
					
						BigDecimal amtAcum = new BigDecimal("0.0");
						
						if(line.get_ValueAsInt("EO_Agreement_ID")>0)
						{
							X_EO_Agreement agree = new X_EO_Agreement(line.getCtx(), line.get_ValueAsInt("EO_Agreement_ID"),line.get_TrxName());
							BigDecimal tmp = Env.ONEHUNDRED.subtract(Env.ONEHUNDRED.multiply(termco.getDiscount().divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP)));
							tmp = tmp.subtract(tmp.multiply(agree.getDiscount().divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP)));
							BigDecimal totalDiscount = Env.ONEHUNDRED.subtract(tmp);
					
							//ininoles seteamos valor antiguo
							try{
									line.set_CustomColumn("AmtBeforeProcess", line.getPriceActual());	
							}
							catch (Exception e)
							{
								log.log(Level.SEVERE, e.getMessage(), e);
							}								
							//	end ininoles			
							amtAcum = amt.subtract(amt.multiply(totalDiscount.divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP))) ;
							
							line.setPriceList(amt);
							line.setPrice(amtAcum);
							line.setDiscount(totalDiscount);
							line.setLineNetAmt();
							//despues de todos los descuento calculamos y actualizamos el valor del ForeignPrice en uf
							BigDecimal amtAcumUF = MConversionRate.convert(line.getCtx(),amtAcum,order.getC_Currency_ID(),
									1000000,p_DateDoc, 114, order.getAD_Client_ID(), order.getAD_Org_ID());
							try{								
									line.set_CustomColumn("ForeignPrice", amtAcumUF);	
							}catch (Exception e)
							{
								log.log(Level.SEVERE, e.getMessage(), e);
							}								
							//							
							line.save();
						}
						else
						{
							//ininoles seteamos valor antiguo
							try{
								line.set_CustomColumn("AmtBeforeProcess", line.getPriceActual());	
							}
							catch (Exception e)
							{
								log.log(Level.SEVERE, e.getMessage(), e);
							}								
							//end ininoles
							
							amtAcum = amt.subtract(amt.multiply(termco.getDiscount().divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP)));
							
							line.setPriceList(amt);
							line.setPrice(amtAcum);
							line.setDiscount(termco.getDiscount());
							line.setLineNetAmt();
							//despues de todos los descuento calculamos y actualizamos el valor del ForeignPrice en uf
							BigDecimal amtAcumUF = MConversionRate.convert(line.getCtx(),amtAcum,order.getC_Currency_ID(),
									1000000,p_DateDoc, 114, order.getAD_Client_ID(), order.getAD_Org_ID());
							try{								
									line.set_CustomColumn("ForeignPrice", amtAcumUF);	
							}catch (Exception e)
							{
								log.log(Level.SEVERE, e.getMessage(), e);
							}								
							//	
							
							line.save();
						}
					}
				}
			}else if (line.getC_Charge_ID()>0) 
			{
				MCharge charge = new MCharge(getCtx(), line.getC_Charge_ID(), get_TrxName());
				if(order.getC_Currency().getISO_Code().equals("CLP"))
				{								
					BigDecimal amt = MConversionRate.convertBase(line.getCtx(), charge.getChargeAmt(),	//	CM adjusted
							1000000, p_DateDoc, 114, order.getAD_Client_ID(), order.getAD_Org_ID());
					
					if(amt==null)
						return "No existe tasa de cambio para UF, no se puede completar la operacion";
					//ininoles seteamos valor antiguo
					try{
						line.set_CustomColumn("AmtBeforeProcess", line.getPriceActual());						
						//seteamos valor en UF antiguo
						line.set_CustomColumn("ForeignPrice", charge.getChargeAmt());
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, e.getMessage(), e);
					}								
					//end ininoles
							
					line.setPriceList(amt);
					line.setPrice(amt);
					line.setLineNetAmt();
					line.save();
				}
			}
			
		}
		return "Orden Actualizada";
	}	//	doIt	
}	//	CopyOrder
