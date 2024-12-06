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
 * Contributor(s): Chris Farley - northernbrewer                              *
 *****************************************************************************/
package org.tc.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  @author Italo Niñoles
 */
public class GenerateInternalUse extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		String sqlDet = "SELECT * FROM I_InventoryXML mi WHERE Processed <> 'Y' AND I_IsImported <> 'Y' "+
				" Order By DocumentNo ASC";
		
		PreparedStatement pstmt = null;
		int cant = 0;
		String ID_movXML = "0";
		log.config("sqldet "+sqlDet);
		try
		{
			pstmt = DB.prepareStatement (sqlDet, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();			
			String LastInv = "";			
			MInventory inv = null;			
			while (rs.next ())
			{
				if(LastInv != rs.getString("DocumentNo"))
				{
					inv = new MInventory(getCtx(), 0, get_TrxName());
					inv.setAD_Org_ID(rs.getInt("AD_Org_ID"));					
					//inv.setC_DocType_ID(1000064);
					inv.setC_DocType_ID(1000023);
					inv.setDescription("Generado automaticamente. - "+rs.getString("Description"));
					inv.setDocStatus("DR");
					inv.setM_Warehouse_ID(rs.getInt("M_Warehouse_ID"));
					//inv.setDocumentNo(rs.getString("DocumentNo"));
					inv.set_CustomColumn("DocNo", rs.getString("DocumentNo"));

					//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					//Se cambia formato fecha a fecha-hora
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

				    Date parsedDate = dateFormat.parse(rs.getString("DateTrx"));
				    Timestamp dateDoc = new java.sql.Timestamp(parsedDate.getTime());
				    log.config("datedoc "+dateDoc);
					if(dateDoc!=null)
						inv.setMovementDate(dateDoc);

					inv.save();					
					cant++;
				}		
				//generacion de detalle
				int ID_Locator = DB.getSQLValue(get_TrxName(), "SELECT M_Locator_ID FROM M_Locator " +
					" WHERE M_Warehouse_ID = "+inv.getM_Warehouse_ID()+" ORDER BY IsDefault ");				
				int ID_Prod = DB.getSQLValue(get_TrxName(), "SELECT M_Product_ID FROM M_Product " +
						" WHERE value like '"+rs.getString("value")+"' ORDER BY M_Product_ID ");
				int ID_Charge = DB.getSQLValue(get_TrxName(), "SELECT C_Charge_ID FROM C_Charge cc" +
					" INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
					" WHERE ct.value = 'TCUI'");
				BigDecimal qty = rs.getBigDecimal("QtyInternalUse");
				if(qty == null)
					qty = Env.ZERO;
				if(rs.getString("Transaction_Type").compareTo("S") == 0)
					qty = qty.negate();	
				
				//mfrojas se vuelve a cambiar el signo al original.
				//qty = qty.negate();
				qty = qty.abs(); //debe ser positivo
				MInventoryLine line  = new MInventoryLine(getCtx(), 0, get_TrxName());
				line.setM_Inventory_ID (inv.get_ID());		//	Parent
				line.setAD_Org_ID(inv.getAD_Org_ID());
				line.setM_Locator_ID (ID_Locator);		//	FK
				line.setM_Product_ID (ID_Prod);	
				line.setQtyInternalUse(qty);
				line.setC_Charge_ID(ID_Charge);
				line.set_CustomColumn("Odometro", rs.getBigDecimal("Odometro"));
				int ID_Partner = DB.getSQLValue(get_TrxName(), "SELECT C_Bpartner_ID from C_BPartner where value like '"+rs.getString("BPartnerValue")+"'");
				line.set_CustomColumn("C_BPartnerC_ID", ID_Partner);
				
				//obtener bien
				int ID_Act = DB.getSQLValue(get_TrxName(),"SELECT A_Asset_ID FROM A_Asset where value like '"+rs.getString("Equipo")+"'");
				line.set_CustomColumn("A_Asset2_ID", ID_Act);
				
				line.set_CustomColumn("origen", rs.getString("origen"));
				
				//mfrojas setear conjunto de atributos, igual al max del producto.
				int att = DB.getSQLValue(get_TrxName(), "SELECT max(M_Attributesetinstance_id) from m_storage where m_product_id = "+ID_Prod);
				line.setM_AttributeSetInstance_ID(att);
				
				line.save();				
				LastInv = rs.getString("DocumentNo");
				
				if(rs.getString("I_InventoryXML_ID") != null)
					ID_movXML = ID_movXML+","+rs.getString("I_InventoryXML_ID");									
			}
			inv.processIt("CO");
			inv.save();
			log.config("Se han generado "+cant+" consumos ");
			//actualizamos registros procesados
			DB.executeUpdate("UPDATE I_InventoryXML SET processed = 'Y' WHERE I_InventoryXML_ID IN ("+ID_movXML+")",get_TrxName());
			rs.close ();
			pstmt.close ();
			pstmt = null;	
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}		
		return "OK";
	}	//	doIt
}	//	Replenish

