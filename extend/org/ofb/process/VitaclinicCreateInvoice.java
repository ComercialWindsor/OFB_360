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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: GenerateDocImages.java $
 */
public class VitaclinicCreateInvoice extends SvrProcess
{
	
	private int 			Record_ID;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 	Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		
		MOrder order = new MOrder(getCtx(), Record_ID, get_TrxName());
		// Creamos Factura de insumos
		MInvoice invoiceInsumos = new MInvoice(order, 1000002, order.getDateOrdered());
		invoiceInsumos.setDescription(invoiceInsumos.getDescription()+" - Insumos");
		invoiceInsumos.save();
		
		String sqlInsumos= "SELECT * FROM C_OrderLine col INNER JOIN M_Product mp on (col.M_Product_ID = mp.M_Product_ID) "+
				  "WHERE C_Order_ID = ? AND rutmedico is null AND mp.m_product_category_id NOT IN (1000173)";
		PreparedStatement pstmt1 = null;		
		ResultSet rs1 = null;
		try
		{
			pstmt1 = DB.prepareStatement(sqlInsumos, get_TrxName());
			pstmt1.setInt(1, Record_ID);
			rs1 = pstmt1.executeQuery ();
			while (rs1.next ())
			{
				MOrderLine oLine = new MOrderLine(getCtx(), rs1.getInt("C_OrderLine_ID"), get_TrxName());
				MInvoiceLine iLine = new MInvoiceLine(invoiceInsumos);
				iLine.setInvoice(invoiceInsumos);
				iLine.setOrderLine(oLine);
				iLine.setM_Product_ID(oLine.getM_Product_ID());
				iLine.setPrice(oLine.getPriceEntered());
				iLine.setC_Tax_ID(oLine.getC_Tax_ID());
				iLine.setQty(oLine.getQtyEntered());
				iLine.setDescription(oLine.getDescription());
				iLine.save();
			}	
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "BP - " + sqlInsumos.toString(), e);
		}
		finally{
			 DB.close(rs1, pstmt1);
			 rs1=null;pstmt1=null;
		}
		
		// Creamos Factura de Facilities
		MInvoice invoiceFacilities = new MInvoice(order, 1000002, order.getDateOrdered());
		invoiceFacilities.setDescription(invoiceFacilities.getDescription()+" - Facilities");
		invoiceFacilities.save();
				
		String sqlFacilities = "SELECT * FROM C_OrderLine col INNER JOIN M_Product mp on (col.M_Product_ID = mp.M_Product_ID) "+
						  "WHERE C_Order_ID = ? AND rutmedico is null AND mp.m_product_category_id IN (1000173)";
		PreparedStatement pstmt2 = null;		
		ResultSet rs2 = null;
		try
		{
			pstmt2 = DB.prepareStatement(sqlFacilities, get_TrxName());
			pstmt2.setInt(1, Record_ID);
			rs2 = pstmt2.executeQuery ();
			while (rs2.next ())
			{
				MOrderLine oLine = new MOrderLine(getCtx(), rs2.getInt("C_OrderLine_ID"), get_TrxName());
				MInvoiceLine iLine = new MInvoiceLine(invoiceFacilities);
				iLine.setC_Invoice_ID(invoiceFacilities.get_ID());
				iLine.setInvoice(invoiceFacilities);
				iLine.setOrderLine(oLine);
				iLine.setM_Product_ID(oLine.getM_Product_ID());
				iLine.setPrice(oLine.getPriceEntered());
				iLine.setC_Tax_ID(oLine.getC_Tax_ID());
				iLine.setQty(oLine.getQtyEntered());
				iLine.setDescription(oLine.getDescription());
				iLine.save();
			}			
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "BP - " + sqlFacilities.toString(), e);
		}
		finally
		{
			 DB.close(rs2, pstmt2);
			 rs2=null;pstmt2=null;
		}
		
		//Creamos Boletas de Honorarios
		int id_Boleta = 0;
		String rutBoleta = " ";
		int id_BP = 0;
		String sqlBoletas = "SELECT * FROM C_OrderLine WHERE C_Order_ID = ? AND rutmedico is not null ORDER BY rutmedico DESC";
		PreparedStatement pstmt3 = null;		
		ResultSet rs3 = null;
		try
		{
			pstmt3 = DB.prepareStatement(sqlBoletas, get_TrxName());
			pstmt3.setInt(1, Record_ID);
			rs3 = pstmt3.executeQuery ();
			while (rs3.next ())
			{
				MOrderLine oLine = new MOrderLine(getCtx(), rs3.getInt("C_OrderLine_ID"), get_TrxName());
				
				MBPartner bp = null;
				MBPartnerLocation bploc = null;
				String nuevoRut = rs3.getString("rutmedico");
				if (rutBoleta.compareTo(nuevoRut) != 0)
				{	
					rutBoleta = rs3.getString("rutmedico");
					MInvoice invoiceBoleta = new MInvoice(getCtx(),0,get_TrxName());
					
					//validacion de BP
					String sqlVAlidBP =  "Select COUNT(1) FROM C_BPartner WHERE VALUE = ?";
					int cantValidBP = DB.getSQLValue(get_TrxName(), sqlVAlidBP, rutBoleta);
					int id_BPLocator = 0;
					
					if (cantValidBP > 0)
					{
						id_BP = DB.getSQLValue(get_TrxName(),"Select MAX(C_BPartner_ID) FROM C_BPartner WHERE VALUE = ?",rutBoleta);
						bp = new MBPartner(this.getCtx(), id_BP, this.get_TrxName());
						
						id_BPLocator = DB.getSQLValue(get_TrxName(), "SELECT C_BPartner_Location_ID FROM C_BPartner_Location " +
								" WHERE C_BPartner_ID=  ? AND IsActive = 'Y' AND IsBillTo = 'Y' ", id_BP);
						
						if (id_BPLocator < 1)
						{
							MLocation loc = new MLocation(getCtx(), 152, 1000001, "Santiago", get_TrxName());
							loc.setAddress1("Desconocida");
							loc.save();
							
							bploc = new MBPartnerLocation(bp);
							bploc.setC_Location_ID(loc.getC_Location_ID());
							bploc.setName("Desconocida");
							bploc.save();
							
							id_BPLocator = bploc.get_ID();
						}
						
						
					}else//se crea BP
					{
						bp = new MBPartner(this.getCtx(), 0, this.get_TrxName());
						bp.setAD_Org_ID(0);
						bp.setValue(rs3.getString("rutmedico"));
						bp.set_CustomColumn("DIGITO",rs3.getString("dvmedico"));
						bp.setName(rs3.getString("nombremedico"));
						bp.setC_BP_Group_ID(1000000);
						bp.save();
						
						MLocation loc = new MLocation(getCtx(), 152, 1000001, "Santiago", get_TrxName());
						loc.setAddress1("Desconocida");
						loc.save();
						
						bploc = new MBPartnerLocation(bp);
						bploc.setC_Location_ID(loc.getC_Location_ID());
						bploc.setName("Desconocida");
						bploc.save();
						
						id_BP = bp.get_ID();
						id_BPLocator = bploc.get_ID();
					}
					//se crea boleta
					invoiceBoleta.setClientOrg(order.getAD_Client_ID(), order.getAD_Org_ID());
					invoiceBoleta.setOrder(order);
					invoiceBoleta.setC_DocType_ID(1000061);
					invoiceBoleta.setC_DocTypeTarget_ID(1000061);
					invoiceBoleta.setDateInvoiced(order.getDateOrdered());
					invoiceBoleta.setDateAcct(order.getDateOrdered());
					invoiceBoleta.setSalesRep_ID(order.getSalesRep_ID());
					invoiceBoleta.setC_BPartner_ID(id_BP);
					invoiceBoleta.setC_BPartner_Location_ID(id_BPLocator);
					invoiceBoleta.setAD_User_ID(order.getBill_User_ID());
					invoiceBoleta.set_CustomColumn("C_BPartnerRef_ID", order.getC_BPartner_ID());
					//					
					invoiceBoleta.save();
					
					//Se crea linea de factura
					MInvoiceLine iLine = new MInvoiceLine(invoiceBoleta);
					iLine.setOrderLine(oLine);
					iLine.setQty(oLine.getQtyEntered());
					iLine.save();
					
					id_Boleta = invoiceBoleta.get_ID();					
				}
				else
				{
					MInvoice invoiceBoleta = new MInvoice(getCtx(),id_Boleta,get_TrxName());
					invoiceBoleta.set_CustomColumn("C_BPartnerRef_ID", order.getC_BPartner_ID());
					invoiceBoleta.save();
					
					MInvoiceLine iLine = new MInvoiceLine(invoiceBoleta);
					iLine.setOrderLine(oLine);
					iLine.setQty(oLine.getQtyEntered());
					iLine.save();
				}
			}			
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "BP - " + sqlBoletas.toString(), e);
		}
		finally
		{
			 DB.close(rs2, pstmt2);
			 rs2=null;pstmt2=null;
		}
	   return "Facturas Generadas ";
	}	//	doIt
}	//	Create Invoice Vitaclinic
