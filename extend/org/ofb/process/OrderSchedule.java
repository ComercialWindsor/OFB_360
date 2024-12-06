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

package org.ofb.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

/**
 *	
 *	
 *  @author Fabian Aguilar
 *  para Colegios PDV
 *  @version $Id: OrderSchedule.java  $
 */

public class OrderSchedule extends SvrProcess 
{
	
	private Timestamp p_FirstDate;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("StartDate"))
				p_FirstDate = (Timestamp) para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	
	protected String doIt() throws Exception
	{
		MOrder order = new MOrder (Env.getCtx(), getRecord_ID() ,get_TrxName());
		if(order.get_ValueAsString("PaymentRule2").equals("P"))
			agregaCargoPagare(order);
		
		//ininoles validacion de catidad de matricula y colegiaturas
		
			if(order.isSOTrx())
			{
				String SqlMatricula = "select coalesce(sum(col.qtyentered),0) from c_orderline col "+
							"inner join m_product mp on (col.m_product_id = mp.m_product_id) "+
							"inner join m_product_category mpc on (mp.m_product_category_id = mpc.m_product_category_id) "+
							"where col.C_Order_ID = "+order.get_ID()+" and upper(mpc.name) like '%MATRICULA%'"; 
					
				String SqlColegio = "select coalesce(sum(col.qtyentered),0) from c_orderline col "+
							"inner join m_product mp on (col.m_product_id = mp.m_product_id) "+
							"inner join m_product_category mpc on (mp.m_product_category_id = mpc.m_product_category_id) "+
							"where col.C_Order_ID = "+order.get_ID()+" and upper(mpc.name) like '%COLEGIATURA%'";
					
				BigDecimal qtyMatricula;
				BigDecimal qtyColegio;
					
				try{
						
						qtyMatricula = DB.getSQLValueBD(null, SqlMatricula);
						qtyColegio = DB.getSQLValueBD(null, SqlColegio);
						
				}catch (Exception e) {
						qtyMatricula = Env.ZERO;
						qtyColegio = Env.ZERO;					
				}
					
				if (qtyMatricula.compareTo(qtyColegio) != 0)				
				{	
						throw new AdempiereException("Error: Cantidad de Matriculas y Colegiaturas no coincide. Matriculas: "+qtyMatricula
									+" Colegiaturas: "+qtyColegio);
				}
			}	
				
		//ininoles end
		
		DB.executeUpdate("delete from C_OrderPaySchedule where C_Order_ID="+order.getC_Order_ID(), get_TrxName());
		
		MOrderLine[] lines = order.getLines(true,null);
		BigDecimal colAmt = Env.ZERO;
		BigDecimal maAmt = Env.ZERO; 
		for (MOrderLine line:lines)
		{
			if(line.getM_Product_ID()>0)
			{
				if(line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("COLEGIATURA")>=0)
					colAmt = colAmt.add(line.getLineNetAmt());
				
				if(line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("MATRÍCULA")>=0 || line.getM_Product().getM_Product_Category().getName().toUpperCase().indexOf("MATRICULA")>=0)
					maAmt = maAmt.add(line.getLineNetAmt());
				
			}
			else
				colAmt = colAmt.add(line.getLineNetAmt());
		}
		
		//Matriculas
		MPaymentTerm pt = new MPaymentTerm(getCtx(), order.getC_PaymentTerm_ID(), get_TrxName());
		BigDecimal remainder = maAmt;
		X_C_OrderPaySchedule os = null;
		for(MPaySchedule ps:pt.getSchedule(false))
		{
			os = new X_C_OrderPaySchedule(getCtx(), 0, get_TrxName());
			os.setC_Order_ID(getRecord_ID());
			os.setDueAmt(maAmt.multiply(ps.getPercentage())
					.divide(Env.ONEHUNDRED, 0, BigDecimal.ROUND_HALF_UP));
			os.setDueDate(TimeUtil.addDays(order.getDateOrdered(), ps.getNetDays()));
			os.setIsValid(true);
			os.set_CustomColumn("C_PaySchedule_ID", ps.getC_PaySchedule_ID());
			os.set_CustomColumn("IsMatricula", true);
			os.save();
			remainder = remainder.subtract(os.getDueAmt());
			//seteamos variable ForeignPrice ininoles
			BigDecimal amtAcumUF = MConversionRate.convert(getCtx(),os.getDueAmt(),order.getC_Currency_ID(),
					1000000,order.getDateOrdered(), 114, order.getAD_Client_ID(), order.getAD_Org_ID());
			
			if(amtAcumUF==null)
				return "No existe tasa de cambio para UF, no se puede completar la operacion";
			else{
				try{								
					os.set_CustomColumn("ForeignPrice", amtAcumUF);	
				}catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}			
				os.save();
			}
			//end ininoles
		}
		
		if (remainder.compareTo(Env.ZERO) != 0 && os != null)
		{
			os.setDueAmt(os.getDueAmt().add(remainder));
			os.save();
			//seteamos variable ForeignPrice ininoles
			BigDecimal amtAcumUF = MConversionRate.convert(getCtx(),os.getDueAmt(),order.getC_Currency_ID(),
					1000000,order.getDateOrdered(), 114, order.getAD_Client_ID(), order.getAD_Org_ID());
			if(amtAcumUF==null)
				return "No existe tasa de cambio para UF, no se puede completar la operacion";
			else{
				try{								
					os.set_CustomColumn("ForeignPrice", amtAcumUF);	
				}catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}			
				os.save();
			}
			//end ininoles			
		}
		if(os==null) // no schedule
		{
			os = new X_C_OrderPaySchedule(getCtx(), 0, get_TrxName());
			os.setC_Order_ID(getRecord_ID());
			os.setDueAmt(maAmt);
			os.setDueDate(TimeUtil.addDays(order.getDateOrdered(), pt.getNetDays()));
			os.set_CustomColumn("IsMatricula", true);
			os.setIsValid(true);
			os.save();			
			//seteamos variable ForeignPrice ininoles
			BigDecimal amtAcumUF = MConversionRate.convert(getCtx(),maAmt,order.getC_Currency_ID(),
					1000000,order.getDateOrdered(), 114, order.getAD_Client_ID(), order.getAD_Org_ID());
			if(amtAcumUF==null)
				return "No existe tasa de cambio para UF, no se puede completar la operacion";
			else{
				try{								
					os.set_CustomColumn("ForeignPrice", amtAcumUF);	
				}catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}			
				os.save();
			}
			//end ininoles
		}
		
		//Colegiarutas
		os=null;
		remainder = colAmt;
		pt = new MPaymentTerm(getCtx(), order.get_ValueAsInt("C_PaymentTerm2_ID"), get_TrxName());
		int i=0;
		final Timestamp dueDate = p_FirstDate;
		for(MPaySchedule ps:pt.getSchedule(false))
		{
			os = new X_C_OrderPaySchedule(getCtx(), 0, get_TrxName());
			os.setC_Order_ID(getRecord_ID());
			os.setDueAmt(colAmt.multiply(ps.getPercentage())
					.divide(Env.ONEHUNDRED, 0, BigDecimal.ROUND_HALF_UP));
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(dueDate.getTime());
		    
		    if(i>0)
		    {
		    	cal.set(Calendar.DAY_OF_MONTH, 1);	//	first day
		    	cal.add(Calendar.MONTH, i);//suma mes
		    }
			Timestamp newDate= new Timestamp(cal.getTimeInMillis());
			os.setDueDate(newDate);
			
			os.setIsValid(true);
			os.set_CustomColumn("C_PaySchedule_ID", ps.getC_PaySchedule_ID());
			os.save();
			i++;
			remainder = remainder.subtract(os.getDueAmt());
			//seteamos variable ForeignPrice ininoles en cada cuota
			BigDecimal amtAcumUF = MConversionRate.convert(getCtx(),os.getDueAmt(),order.getC_Currency_ID(),
					1000000,order.getDateOrdered(), 114, order.getAD_Client_ID(), order.getAD_Org_ID());
			if(amtAcumUF==null)
				return "No existe tasa de cambio para UF, no se puede completar la operacion";
			else{
				try{								
					os.set_CustomColumn("ForeignPrice", amtAcumUF);	
				}catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}			
				os.save();
			}
			//end ininoles
			
		}
		if (remainder.compareTo(Env.ZERO) != 0 && os != null)
		{
			os.setDueAmt(os.getDueAmt().add(remainder));
			os.save();
			//seteamos variable ForeignPrice ininoles
			BigDecimal amtAcumUF = MConversionRate.convert(getCtx(),os.getDueAmt(),order.getC_Currency_ID(),
					1000000,order.getDateOrdered(), 114, order.getAD_Client_ID(), order.getAD_Org_ID());
			if(amtAcumUF==null)
				return "No existe tasa de cambio para UF, no se puede completar la operacion";
			else{
				try{								
					os.set_CustomColumn("ForeignPrice", amtAcumUF);	
				}catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}			
				os.save();
			}
			//end ininoles
		}
		if(os==null) // no schedule
		{
			os = new X_C_OrderPaySchedule(getCtx(), 0, get_TrxName());
			os.setC_Order_ID(getRecord_ID());
			os.setDueAmt(colAmt);
			os.setDueDate(TimeUtil.addDays(order.getDateOrdered(), pt.getNetDays()));
			os.setIsValid(true);
			os.save();
			//seteamos variable ForeignPrice ininoles
			BigDecimal amtAcumUF = MConversionRate.convert(getCtx(),os.getDueAmt(),order.getC_Currency_ID(),
					1000000,order.getDateOrdered(), 114, order.getAD_Client_ID(), order.getAD_Org_ID());
			if(amtAcumUF==null)
				return "No existe tasa de cambio para UF, no se puede completar la operacion";
			else{
				try{								
					os.set_CustomColumn("ForeignPrice", amtAcumUF);	
				}catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}			
				os.save();
			}
			//end ininoles
		}

		return "Cuotas Calculadas";
	}	
	
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
			//chargeAmt = chargeAmt.multiply(new BigDecimal(colegiaturas)); //ininoles es 1 monto fijo independiente de la cantidad de pagares
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
			
			//sumamos nuevo 2% del neto sin el cargo del pagare al valor de la linea
			 String sqlAmtCharge2 = "select (SUM(col.LineNetAmt)*2)/100" +
			 		" FROM C_OrderLine col " +
			 		" LEFT JOIN M_Product mp on (col.M_Product_ID = mp.M_Product_ID)" +
			 		" LEFT JOIN M_Product_Category mpc on (mp.M_Product_Category_ID = mpc.M_Product_Category_ID)" +
			 		" WHERE col.C_Order_ID = "+order.get_ID()+" AND (upper(mpc.name) not like '%MATRICULAS%' OR mpc.name is null)" +
			 		" AND (C_Charge_ID is null OR (C_Charge_ID <> "+C_ChargeChange_ID+" and c_charge_id is not null))";			 
			 BigDecimal amtCharge2 = DB.getSQLValueBD(order.get_TrxName(), sqlAmtCharge2);
			 chargeAmt = chargeAmt.add(amtCharge2);			
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
