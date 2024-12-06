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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
 
/**
 *	report infoprojectPROVECTIS
 *	
 *  @author ininoles
 *  @version $Id: Balances8.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class ProjectOFBPROVECTIS extends SvrProcess
{
	private int	p_ProjectOFB_ID = 0;	
	private int p_PInstance_ID;	

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
			else if (name.equals("C_ProjectOFB_ID"))
			{
				p_ProjectOFB_ID = para[i].getParameterAsInt();				
			}
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		
		String sqlPrincipal = "SELECT C_ProjectOFB_ID FROM C_ProjectOFB WHERE isactive = 'Y'";
		//String sqlOrdenC ="SELECT C_Order_id FROM ";
		
		String sqlOrdenV ="SELECT col.AD_CLIENT_ID, col.AD_ORG_ID, col.CREATED, col.CREATEDBY, col.UPDATED,col.UPDATEDBY," +
				"col.C_OrderLine_ID,col.IsActive,col.C_Order_ID,co.SalesRep_ID "+
				"FROM C_OrderLine col INNER JOIN C_Order co on (col.C_Order_ID = co.C_Order_ID) "+
				"WHERE co.isactive = 'Y' AND co.issotrx = 'Y' AND co.ad_client_id = ? AND co.docstatus IN ('CO','CL') "+
				"AND C_ProjectOFB_ID = ?";
		
		if(p_ProjectOFB_ID > 0)
		{
			sqlPrincipal = sqlPrincipal + " AND C_ProjectOFB_ID = " + p_ProjectOFB_ID;
		}
			
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sqlPrincipal, get_TrxName());
		
		DB.executeUpdate("DELETE FROM  T_InfoProjectOFB", get_TrxName());
		
		try
		{
		ResultSet rs = pstmt.executeQuery();
		
			//Ciclo de todas las fichas de negocio
		while (rs.next())
		{
				
				
				X_C_ProjectOFB ProjectOFB = new X_C_ProjectOFB(getCtx(), rs.getInt("C_ProjectOFB_ID"),get_TrxName());
				
			PreparedStatement pstmt2 = null;
			pstmt2 = DB.prepareStatement(sqlOrdenV, get_TrxName());
			pstmt2.setInt(1, getAD_Client_ID());
			pstmt2.setInt(2, ProjectOFB.get_ID());			
			ResultSet rs2 = pstmt2.executeQuery();
			
			//Ciclo de las lineas de notas de venta o propuestas  
			while (rs2.next())
			{
				int CantError = 0;
				String MsgError = " ";
				
				MOrderLine orderLineV = new MOrderLine(getCtx(), rs2.getInt("C_OrderLine_ID"), get_TrxName());
								
				/*String sqlOL = "SELECT coalesce(SUM(qtyentered),0) FROM C_InvoiceLine cil "+
				"INNER JOIN C_Invoice ci on (cil.C_Invoice_ID = ci.C_Invoice_ID) WHERE ci.docstatus in ('CO','CL') "+
				"AND ci.isactive = 'Y' AND cil.C_OrderLine_ID = ?";*/
				
				//insert solo de la ficha y order line				
				String insert="INSERT INTO T_InfoProjectOFB (AD_CLIENT_ID, AD_ORG_ID, CREATEDBY, UPDATEDBY,Isactive,AD_PInstance_ID, " +
						"C_ProjectOFB_ID,C_Order_ID ,C_OrderLine_ID,M_Product_ID,QtyEntered,LineNetAmt,LineNetAmtC,CantErrores,description,SalesRep_ID) "+
				  		"VALUES	("+rs2.getInt("AD_CLIENT_ID")+", "+rs2.getInt("AD_ORG_ID")+", 100,"+
						"100,'"+rs2.getString("IsActive")+"',"+p_PInstance_ID+","+
					    ProjectOFB.get_ID()+"," +orderLineV.getC_Order_ID()+","+orderLineV.getC_OrderLine_ID()+","+orderLineV.getM_Product_ID()+","+
					    orderLineV.getQtyEntered()+","+orderLineV.getLineNetAmt()+","+"0"+","+CantError+",'"+MsgError+"',"+rs2.getInt("SalesRep_ID")+")";				    		
						
				DB.executeUpdate(insert, get_TrxName());
				//fin insert
				
				
				String sqlOL = "SELECT coalesce(SUM(qtyentered),0) FROM M_InOutLine miol "+
				"INNER JOIN M_InOut mio on (miol.M_InOut_ID = mio.M_InOut_ID) "+
				"WHERE mio.docstatus in ('CO','CL') AND mio.isactive = 'Y' AND miol.C_OrderLine_ID = ?";
				
				BigDecimal sumDV = DB.getSQLValueBD(get_TrxName(), sqlOL, orderLineV.get_ID());
				
				//validacion que linea de orden este despachada
				if(orderLineV.getQtyEntered().compareTo(sumDV)>0)
				{
					MsgError = MsgError +  "Cantidad Despachada Menor que Cantidad de Propuesta; ";
					CantError = CantError +1;
				}
				else
				{
					//actualizacion de cantidad despachada						
					String updateVS = "UPDATE T_InfoProjectOFB SET "+
							" PriceEntered = "+orderLineV.getPriceEntered()+", QtyShipment = "+sumDV+							
							" WHERE C_OrderLine_ID = "+orderLineV.get_ID();
					
					DB.executeUpdate(updateVS, get_TrxName());						
					//fin actualizacion 
					
					String sqlIL = "SELECT coalesce(SUM(cil.qtyentered),0) FROM C_InvoiceLine cil INNER JOIN C_Invoice ci on (cil.C_Invoice_ID = ci.C_Invoice_ID) WHERE ci.docstatus in ('CO','CL') "+
					"AND ci.isactive = 'Y' AND (cil.M_InOutLine_ID IN ( SELECT M_InOutLine_ID FROM M_InOutLine WHERE isactive = 'Y' AND C_OrderLine_ID = ?) "+
					"OR cil.C_OrderLine_ID = ?)";
					
					BigDecimal sumFV = DB.getSQLValueBD(get_TrxName(), sqlIL, orderLineV.get_ID(),orderLineV.get_ID());
					
					//validacion que linea de orden este facturada 
					if(orderLineV.getQtyEntered().compareTo(sumFV)>0)
					{
						MsgError = MsgError +  "Cantidad Facturada Menor que Cantidad de Propuesta; ";
						CantError = CantError +1;
					}
					else
					{
						//actualizacion cantidad facturada y monto						
						String updateV = "UPDATE T_InfoProjectOFB SET LineNetAmt = "+orderLineV.getLineNetAmt() +								
								", QtyInvoicedV = "+sumFV+
								" WHERE C_OrderLine_ID = "+orderLineV.get_ID();
						
						DB.executeUpdate(updateV, get_TrxName());						
						//fin actualizacion 
						
						//revision proceso de compra
						//
						//Lineas OC asociadas a la propuesta a traves del recibo y la tabla de relacion excepto para las OC de provectis ID=1003403.
						String sqlOrdenC = "SELECT DISTINCT mil.C_OrderLine_ID FROM M_InOutOrderLine miol "+
						"INNER JOIN M_InOut mi on (miol.M_InOut_ID = mi.M_InOut_ID) LEFT JOIN M_InOutLine mil on (mi.M_InOut_ID = mil.M_InOut_ID) "+
						"LEFT JOIN C_OrderLine col on (col.C_OrderLine_ID = mil.C_OrderLine_ID) LEFT JOIN C_Order co on (col.C_Order_ID = co.C_Order_id) "+
						"WHERE miol.C_OrderLine_ID = ? AND co.c_bpartner_id not in (1003403)";
						
						PreparedStatement pstmt3 = null;
						pstmt3 = DB.prepareStatement(sqlOrdenC, get_TrxName());
						pstmt3.setInt(1, orderLineV.get_ID());								
						ResultSet rs3 = pstmt3.executeQuery();
						
						//contador errores de compra
						//int CantErrorC = 0;		
						//BigDecimal montoTotalC = new BigDecimal("0.0");
						
						while (rs3.next())
						{
							MOrderLine orderLineC = new MOrderLine(getCtx(), rs3.getInt("C_OrderLine_ID"), get_TrxName());
							
												
							
							String sqlOLC = "SELECT coalesce(SUM(qtyentered),0) FROM M_InOutLine miol "+
							"INNER JOIN M_InOut mio on (miol.M_InOut_ID = mio.M_InOut_ID) "+
							"WHERE mio.docstatus in ('CO','CL') AND mio.isactive = 'Y' AND miol.C_OrderLine_ID = ?";
									
							BigDecimal sumRC = DB.getSQLValueBD(get_TrxName(), sqlOLC, orderLineC.get_ID());
									
							//validacion que linea de orden de compra este despachada
							if(orderLineC.getQtyEntered().compareTo(sumRC)>0)
							{
								MsgError = MsgError +  "Cantidad Recibida Menor que Cantidad Orden de Compra; ";
								CantError = CantError +1;
							}
							else
							{
								String sqlILC = "SELECT coalesce(SUM(cil.qtyentered),0) FROM C_InvoiceLine cil "+
										"INNER JOIN C_Invoice ci on (cil.C_Invoice_ID = ci.C_Invoice_ID) WHERE ci.docstatus in ('CO','CL') "+
										"AND ci.isactive = 'Y' AND (cil.M_InOutLine_ID IN (SELECT M_InOutLine_ID FROM M_InOutLine WHERE isactive = 'Y' AND C_OrderLine_ID = ?)" +
										"OR cil.C_OrderLine_ID = ?)";
										
										BigDecimal sumFC = DB.getSQLValueBD(get_TrxName(), sqlILC, orderLineC.get_ID(),orderLineC.get_ID());
										
										//validacion que linea de orden de compra este facturada   
										if(orderLineC.getQtyEntered().compareTo(sumFC)>0)
										{
											MsgError = MsgError +  "Cantidad Facturada Menor que Cantidad Orden de Compra; ";
											CantError = CantError +1;
										}
										else
										{
											//la sumatoria se hara directamnete con una query indicando la linea de la propuesta 
											/*String sqlSumC = "SELECT coalesce(SUM(cil.linenetamt),0) / (SELECT count(M_InOutOrderLine_ID) "+
											"FROM M_InOut mio2 INNER JOIN M_InOutOrderLine miool2 on (mio2.M_InOut_ID=miool2.M_InOut_ID) "+
											"WHERE mio2.M_InOut_ID IN (Select M_InOut_ID FROM M_InOutLine WHERE M_InOutLine.C_OrderLine_ID = ?) "+
											") FROM C_InvoiceLine cil INNER JOIN C_Invoice ci on (cil.C_Invoice_ID = ci.C_Invoice_ID) "+
											"WHERE ci.docstatus in ('CO','CL') AND ci.isactive = 'Y' AND " +
											"(cil.M_InOutLine_ID IN  (SELECT M_InOutLine_ID FROM M_InOutLine WHERE isactive = 'Y' AND C_OrderLine_ID = ?) " +
											"OR cil.C_OrderLine_ID = ?)";*/
											
											//BigDecimal sumC = DB.getSQLValueBD(get_TrxName(), sqlSumC, orderLineC.get_ID(),orderLineC.get_ID(),orderLineC.get_ID());
																						
											//montoTotalC = montoTotalC.add(sumC);
													
											CantError = CantError + CantError  ;
										}
							}
						}
						//Actualizacion de montos de compra si no hay errores
						//Se hara despues
						/*if (CantErrorC < 1)
						{							
							String updateMC = "UPDATE T_InfoProjectOFB SET linenetamtc = "+montoTotalC+	
									" WHERE C_OrderLine_ID = "+orderLineV.get_ID();
					
							DB.executeUpdate(updateMC, get_TrxName());						
							//fin actualizacion
						}*/
					}
				
					
				}
				//Actualizacion de campos secundarios
				MOrder orderV = new MOrder(getCtx(), orderLineV.getC_Order_ID(), get_TrxName());
				MBPartner bp = new MBPartner(getCtx(), orderV.getC_BPartner_ID(), get_TrxName());
				String rutCompleto = bp.getValue()+"-"+bp.get_ValueAsInt("Digito");
				MProduct prodV = new MProduct(getCtx(), orderLineV.getM_Product_ID(), get_TrxName());
				
				/*String sqlPay = "SELECT MAX(cp.documentno)"+
				"FROM C_Payment cp INNER JOIN C_AllocationLine cal on (cp.C_Payment_ID = cal.C_Payment_ID) "+
				"INNER JOIN C_Invoice ci on (cal.C_Invoice_ID = ci.C_Invoice_ID) WHERE ci.C_Invoice_ID IN (SELECT cil.C_Invoice_ID "+
				"FROM C_InvoiceLine cil WHERE cil.C_OrderLine_ID = ?)";
				
				String nPay = DB.getSQLValueString(get_TrxName(), sqlPay, orderLineV.get_ID());
				
				String sqlInv ="SELECT MAX(ci.documentno) "+
				"FROM C_Invoice ci WHERE ci.C_Invoice_ID IN (SELECT cil.C_Invoice_ID FROM C_InvoiceLine cil WHERE cil.C_OrderLine_ID = ?)";
				
				String nInv = DB.getSQLValueString(get_TrxName(), sqlInv, orderLineV.get_ID());
				*/
				
				//sumatoria de total facturas venta
				
				String sqlInvV = "SELECT coalesce((SUM(currencyconvert(ci.GrandTotal,ci.C_Currency_ID,228,ci.dateinvoiced, ci.C_ConversionType_id, "+
				"ci.AD_Client_ID, ci.AD_Org_ID))),0) as amt FROM C_Invoice ci WHERE ci.docstatus in ('CO','CL') AND (ci.c_order_id = ? " +
				"OR C_Invoice_ID in (SElECT DISTINCT ci2.C_Invoice_ID 	FROM C_Invoice ci2 INNER JOIN C_InvoiceLine cil2 ON " +
				"(ci2.C_Invoice_ID = cil2.C_Invoice_ID) WHERE cil2.C_OrderLine_ID IN (SELECT col.C_OrderLine_ID FROM C_OrderLine col "+
				"WHERE col.C_Order_ID =?) ))";

				BigDecimal sumInvV = DB.getSQLValueBD(get_TrxName(), sqlInvV, orderLineV.getC_Order_ID(),orderLineV.getC_Order_ID());
				
				//sumatoria de total de pagos de venta
				String sqlPayV = "SELECT coalesce((SUM(currencyconvert(cp.PayAmt,cp.C_Currency_ID,228,cp.datetrx,cp.C_ConversionType_id,cp.AD_Client_ID,cp.AD_Org_ID) "+
				")),0) FROM C_Payment cp INNER JOIN C_AllocationLine cal ON (cp.C_Payment_ID = cal.C_Payment_ID) WHERE "+
				"cp.docstatus in ('CO','CL') AND cal.C_Invoice_ID IN "+
				"(SELECT C_Invoice_ID FROM C_Invoice ci WHERE ci.docstatus in ('CO','CL') AND (ci.c_order_id = ? OR C_Invoice_ID in (SElECT DISTINCT ci2.C_Invoice_ID "+
				"FROM C_Invoice ci2 INNER JOIN C_InvoiceLine cil2 ON (ci2.C_Invoice_ID = cil2.C_Invoice_ID) WHERE cil2.C_OrderLine_ID IN "+
				"(SELECT col.C_OrderLine_ID FROM C_OrderLine col WHERE col.C_Order_ID =?) )))"; 
				
				BigDecimal sumPayV = DB.getSQLValueBD(get_TrxName(), sqlPayV, orderLineV.getC_Order_ID(), orderLineV.getC_Order_ID());
				
				//sumatoria de total facturas compra
				
				String sqlInvC = "SELECT COALESCE (((SELECT COALESCE ((SUM( (currencyconvert(ci.GrandTotal,ci.C_Currency_ID,228,ci.dateinvoiced, ci.C_ConversionType_id,ci.AD_Client_ID, ci.AD_Org_ID))/ "+
				"(SELECT COUNT(*) FROM M_InOutOrderLine miol2 INNER JOIN M_InOut mio2 ON (miol2.M_InOut_ID = mio2.M_InOut_ID) WHERE mio2.C_Order_ID = ci.C_Order_ID))),0) "+
				"as amt FROM C_Invoice ci WHERE ci.docstatus in ('CO','CL') AND ci.C_Order_ID in ( SELECT DISTINCT col.c_order_id FROM M_InOutOrderLine miol INNER JOIN M_InOut mi on (miol.M_InOut_ID = mi.M_InOut_ID) "+
				"LEFT JOIN M_InOutLine mil on (mi.M_InOut_ID = mil.M_InOut_ID) LEFT JOIN C_OrderLine col on (col.C_OrderLine_ID = mil.C_OrderLine_ID) "+
				"LEFT JOIN C_Order co on (col.C_Order_ID = co.C_Order_id) WHERE miol.C_OrderLine_ID = ? AND co.c_bpartner_id not in (1003403)))+ "+
				"(SELECT COALESCE (( SUM((currencyconvert(co.GrandTotal,co.C_Currency_ID,228,co.dateordered, co.C_ConversionType_id,co.AD_Client_ID, co.AD_Org_ID))/ "+
				"(SELECT COUNT(*) FROM M_InOutOrderLine miol2 INNER JOIN M_InOut mio2 ON (miol2.M_InOut_ID = mio2.M_InOut_ID) WHERE mio2.C_Order_ID = co.C_Order_ID))),0) "+
				"FROM M_InOutOrderLine miol INNER JOIN M_InOut mi on (miol.M_InOut_ID = mi.M_InOut_ID) LEFT JOIN M_InOutLine mil on (mi.M_InOut_ID = mil.M_InOut_ID) "+
				"LEFT JOIN C_OrderLine col on (col.C_OrderLine_ID = mil.C_OrderLine_ID) LEFT JOIN C_Order co on (col.C_Order_ID = co.C_Order_id) "+
				"WHERE miol.C_OrderLine_ID = ? AND co.c_bpartner_id in (1003403))),0) FROM DUAL";
				
				BigDecimal sumInvC = DB.getSQLValueBD(get_TrxName(), sqlInvC, orderLineV.get_ID(),orderLineV.get_ID());
				
				String sqlPayC = "SELECT coalesce((SUM(currencyconvert(cp.PayAmt,cp.C_Currency_ID,228,cp.datetrx,cp.C_ConversionType_id,cp.AD_Client_ID,cp.AD_Org_ID))),0) "+
				"as amt FROM C_Payment cp INNER JOIN C_AllocationLine cal ON (cp.C_Payment_ID = cal.C_Payment_ID) WHERE cp.docstatus in ('CO','CL') "+
				"AND cal.C_Invoice_ID IN (SELECT ci.C_Invoice_id FROM C_Invoice ci WHERE ci.docstatus in ('CO','CL') AND ci.C_Order_ID in ( "+ 
				"SELECT DISTINCT col.c_order_id FROM M_InOutOrderLine miol INNER JOIN M_InOut mi on (miol.M_InOut_ID = mi.M_InOut_ID) LEFT JOIN M_InOutLine mil on (mi.M_InOut_ID = mil.M_InOut_ID) "+
				"LEFT JOIN C_OrderLine col on (col.C_OrderLine_ID = mil.C_OrderLine_ID) LEFT JOIN C_Order co on (col.C_Order_ID = co.C_Order_id) "+
				"WHERE miol.C_OrderLine_ID = ?))";
				
				BigDecimal sumPayC = DB.getSQLValueBD(get_TrxName(), sqlPayC, orderLineV.get_ID());
				
				//actualizacion campos adicionales y errores y sumatorias de venta 				
				String updateCS = "UPDATE T_InfoProjectOFB SET "+
								"C_BPartner_ID = "+bp.get_ID()+
								", canterrores = "+CantError+
								", Description = '" +MsgError+"'"+
								", Rut = '" +rutCompleto+"'" +
								", M_Product_Category_ID = "+prodV.getM_Product_Category_ID()+
								", sumInvV ="+sumInvV+
								", sumPayV ="+sumPayV+
								", sumInvC ="+sumInvC+
								", sumPayC ="+sumPayC+
								//", nPayment = '"+nPay+"', nInvoice = '"+nInv+"'"+
								" WHERE C_OrderLine_ID = "+orderLineV.get_ID();
				
				DB.executeUpdate(updateCS, get_TrxName());						
				//fin actualizacion
				
			}
			
			
			
		}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "";
	}	//	doIt
	
}	//	OrderOpen
