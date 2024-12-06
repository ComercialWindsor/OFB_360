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
package org.ofb.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.model.MCharge;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.compiere.process.*;
import org.compiere.util.DB;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class BudgetReview extends SvrProcess
{
	/**	The Order				*/
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int C_Order_ID = getRecord_ID();
		log.info("C_Order_ID=" + C_Order_ID);		
		if (C_Order_ID == 0)
			throw new IllegalArgumentException("Target C_Order_ID == 0");
		
		MOrder order = new MOrder (getCtx(), C_Order_ID, get_TrxName());

		int cantIB = 0;	
		String helpInfo = "";
		Timestamp dateOrder = order.getDateOrdered();

		Calendar datePO = Calendar.getInstance();
		datePO.setTime((Date)dateOrder) ;
		int ahno = datePO.get(Calendar.YEAR);
		int mes = datePO.get(Calendar.MONTH);
		mes = mes +1;
		
		MOrderLine[] lines = order.getLines(false, null);
		for (int i = 0; i < lines.length; i++)			
		{			
			MOrderLine line = new MOrderLine(getCtx(),lines[i].getC_OrderLine_ID(), get_TrxName());
			//se asigna centro de costo
			int CCosto = line.get_ValueAsInt("C_ProjectOFB_ID");
			
			int tipoCuenta;
			//valores mensuales
			BigDecimal qpresupuesto = new BigDecimal("0.0");
			BigDecimal qpresupuestoPM = new BigDecimal("0.0");
			//valores abuales
			BigDecimal qpresupuestoA = new BigDecimal("0.0");
			BigDecimal qpresupuestoPMA = new BigDecimal("0.0");
			
			//se asigna tipo cuenta
			if (line.getM_Product_ID()>0)
			{
				MProduct prod = new MProduct(getCtx(),line.getM_Product_ID(), get_TrxName());
				MProductCategory  catProd = new MProductCategory(getCtx(), prod.getM_Product_Category_ID(), get_TrxName());
				tipoCuenta = catProd.get_ValueAsInt("erpg_tipo_cuenta");
				 
			}
			else if (line.getC_Charge_ID() > 0)
			{
				MCharge charge = new MCharge(getCtx(), line.getC_Charge_ID(), get_TrxName());
				tipoCuenta = charge.get_ValueAsInt("erpg_tipo_cuenta");
			}
			else
			{
				tipoCuenta =0;
			}
			//*********

			//Se asignaran valores del mes
			PreparedStatement pstmt = null;
						
			String mysql = "select abs(qpresupuesto) as qpresupuesto, "+
			"abs(qpresupuesto_prog_manual) qpresupuesto_prog_manual from erpg_programa_flujo_anual "+
			"where erpg_tipo_cuenta = ? and c_projectofb_id = ? and extract(year from fmes_anio) = ? " +
			"and extract(month from fmes_anio) = ? and isactive like 'Y'";
			
			try 
			{
				pstmt = DB.prepareStatement(mysql, get_TrxName());
				pstmt.setInt(1, tipoCuenta);
				pstmt.setInt(2, CCosto);
				pstmt.setString(3,Integer.toString(ahno));
				pstmt.setInt(4, mes);
								
				ResultSet rs = pstmt.executeQuery();
				
				if (rs.next())
				{
					qpresupuesto = rs.getBigDecimal(1)==null?new BigDecimal("0.0"):rs.getBigDecimal(1);
					qpresupuestoPM = rs.getBigDecimal(2)==null?new BigDecimal("0.0"):rs.getBigDecimal(2);
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}									
			BigDecimal saldoMes = qpresupuestoPM.subtract(qpresupuesto);			
			//******
			
			//se asignan valores anuales			
			PreparedStatement pstmtA = null;
			
			String mysqlA = "select abs(sum(qpresupuesto)) as qpresupuesto, "+
			"abs(sum(qpresupuesto_prog_manual)) as qpresupuesto_prog_manual "+
			"from erpg_programa_flujo_anual where erpg_tipo_cuenta =  ? "+
			"and c_projectofb_id = ? and extract(year from fmes_anio) = ? and isactive like 'Y' ";
			
			try 
			{
				pstmtA = DB.prepareStatement(mysqlA, get_TrxName());
				pstmtA.setInt(1, tipoCuenta);
				pstmtA.setInt(2, CCosto);
				pstmtA.setString(3,Integer.toString(ahno));
												
				ResultSet rsA = pstmtA.executeQuery();
				
				if (rsA.next())
				{
					qpresupuestoA = rsA.getBigDecimal(1)==null?new BigDecimal("0.0"):rsA.getBigDecimal(1);
					qpresupuestoPMA = rsA.getBigDecimal(2)==null?new BigDecimal("0.0"):rsA.getBigDecimal(2);
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}			
			BigDecimal saldoAnual = qpresupuestoPMA.subtract(qpresupuestoA);
			//******
			
			if (line.getLineNetAmt().compareTo(saldoMes) <= 0)
			{
				cantIB = cantIB +0;
				helpInfo = "Presupuesto Correcto - Presupuesto mes:"+qpresupuestoPM+" Disponible mes:"+saldoMes+" a Consumir:"+line.getLineNetAmt();
				line.set_CustomColumn("BudgetInfo", 1);
			}			
			else
			{
				if (line.getLineNetAmt().compareTo(saldoAnual)<=0)
				{
					cantIB = cantIB +0;
					helpInfo = "Presupuesto mensual sobrepasado. Uso presupuesto anual - Presupuesto mes:"+qpresupuestoPM+" Disponible mes:"+
							saldoMes+" a Consumir:"+line.getLineNetAmt()+" Saldo anual:"+saldoAnual;
					line.set_CustomColumn("BudgetInfo", 2);
				}			
				else 
				{
					cantIB = cantIB + 1;
					helpInfo = "Presupuesto mensual y anual sobrepasado - Presupuesto mes:"+qpresupuestoPM+" Disponible mes:"+
							saldoMes+" a Consumir:"+line.getLineNetAmt()+" Saldo anual:"+saldoAnual;
					line.set_CustomColumn("BudgetInfo", 3);
				}
			}
			//seteo informacion presupuesto
			line.set_CustomColumn("HelpBudget", helpInfo);
			line.save();
		}
		//seteo campo de presupuesto en cabecera
		if (cantIB > 0)
			order.set_CustomColumn("BudgetStatus", false);
		else 
			order.set_CustomColumn("BudgetStatus", true);
		
		order.save();
		//
		return "";
	}	//	doIt
	
	

}	//	
