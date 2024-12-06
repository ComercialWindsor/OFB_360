/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.geminis.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MCharge;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.compiere.util.DB;

/**
 *	Order Callouts Geminis.
 *	
 *  @author Italo Ni�oles OFB ininoles
 *  @version $Id: CalloutGeminisOC.java,v 1.34 2014/09/02 10:00:00  Exp $
 */
public class CalloutGeminisOC extends CalloutEngine
{	 
	public String BudgetReview (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		Boolean op = (Boolean)value;
		if(op)
		{
			int C_Order_ID =  Integer.parseInt((mTab.getValue("C_Order_ID")).toString());
			log.info("C_Order_ID=" + C_Order_ID);		
			
			if (C_Order_ID == 0)
				throw new IllegalArgumentException("Target C_Order_ID == 0");
			
			MOrder order = new MOrder (ctx, C_Order_ID, null);

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
				MOrderLine line = new MOrderLine(ctx,lines[i].getC_OrderLine_ID(), null);
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
					MProduct prod = new MProduct(ctx,line.getM_Product_ID(), null);
					MProductCategory  catProd = new MProductCategory(ctx, prod.getM_Product_Category_ID(), null);
					tipoCuenta = catProd.get_ValueAsInt("erpg_tipo_cuenta");
					 
				}
				else if (line.getC_Charge_ID() > 0)
				{
					MCharge charge = new MCharge(ctx, line.getC_Charge_ID(), null);
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
					pstmt = DB.prepareStatement(mysql, null);
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
					pstmtA = DB.prepareStatement(mysqlA, null);
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
			{
				mTab.setValue("BudgetStatus", false);
				mTab.setValue("IsSelected", false);
				//order.set_CustomColumn("BudgetStatus", false);
				//throw new IllegalArgumentException("");
			}	
			else
			{
				mTab.setValue("BudgetStatus", true);
				mTab.setValue("IsSelected", true);
				//order.set_CustomColumn("BudgetStatus", true);				
			}
			order.save();
		}		
			
		return "";
	}
}	//	

