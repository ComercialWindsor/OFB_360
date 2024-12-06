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
package org.blumos.process;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.math.BigDecimal;

import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *  @author mfrojas
 */
public class GenerateInventoryAd extends SvrProcess
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
		String sqlDet = "SELECT cab.I_IMPORTADW_ID, cab.AD_ORG_ID, cab.NUMERO_DOC,cab.FECHA," +
				" cab.ALMACEN,cab.ISABORT,cab.DESCRIPTION2, " +
				//campos de linea
				" det.I_IMPORTADWL_ID, det.ISABORT as ISABORTDET,det.PRODUCTO,det.MOTIVO,det.PU," +
				" det.NUMERO_DOC as NRODOCDET, det.CANTIDAD, det.CENTRO_DE_COSTO, det.MOTIVO,det.PXQ " +
				" FROM I_IMPORTADW cab " +
				" LEFT JOIN I_IMPORTADWL det ON (cab.NUMERO_DOC = det.NUMERO_DOC)" +
				" WHERE cab.processed = 'N' AND (det.processed = 'N' OR det.processed IS NULL) " +
				" Order By cab.NUMERO_DOC Desc, I_IMPORTADWL_ID ASC";
		log.config("sqldet "+sqlDet);
		PreparedStatement pstmt = null;
		int cant = 0;
		String ID_movXML = "0";
		String ID_movXMLLine = "0";
		try
		{
			pstmt = DB.prepareStatement (sqlDet, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();			
			int LastMov = -1;
			int id_MovAD = 0;
			MInventory mov = null;			
			while (rs.next ())
			{
				id_MovAD = DB.getSQLValue(get_TrxName(),"SELECT MAX(M_Inventory_ID) FROM M_Inventory " +
						" WHERE DocumentNo = '"+rs.getString("NUMERO_DOC")+"' AND DocStatus IN ('DR','IP','CO')"); 
				//ininoles generacion de cabecera
				if(id_MovAD != LastMov)
				{
					if(mov != null)
					{
						mov.processIt("CO");
						mov.save();
					}
					mov = new MInventory(getCtx(), 0, get_TrxName());
					mov.setDocumentNo(rs.getString("NUMERO_DOC"));
					mov.setAD_Org_ID(1000023);					
					//808 es desarrollo, 869 es prod. 
					//mov.setC_DocType_ID(1000808);
					
					//mov.setC_DocType_ID(1000869);
					// se cambia a campo name para reconocer
					
					//mfrojas, 20180820
					//se cambia los NVL por COALESCE
					String sqldoctype = "SELECT coalesce(max(c_doctype_id),0) from c_doctype where name like 'WSConsumoAjuste' and AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
					int doctype = DB.getSQLValue(get_TrxName(), sqldoctype);
					
					if(doctype > 0)
						mov.setC_DocType_ID(doctype);
					else
						return "No existe tipo de documentos";
					
					mov.setDescription("Insertado XML. "+rs.getString("DESCRIPTION2"));
					//buscamos bodega
					//Se cambia, ID Bodega será productos terminados; 
					int ID_Bodega = 1000038;
					//int ID_Bodega = DB.getSQLValue(get_TrxName(), " SELECT MAX(COALESCE(M_Warehouse_ID,1000037)) FROM M_Warehouse " +
					//		" WHERE NAME = '"+rs.getString("ALMACEN")+"'");	
					//log.config("bodega"+ID_Bodega);
					mov.setM_Warehouse_ID(ID_Bodega);
					//generamos fecha desde string
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					log.config("fecha"+rs.getString("FECHA"));
				    Date parsedDate = dateFormat.parse(rs.getString("FECHA"));
				    Timestamp dateDoc = new java.sql.Timestamp(parsedDate.getTime());
					if(dateDoc!=null)
						mov.setMovementDate(dateDoc);
					//
					//@mfrojas validación, que no exista nro de documento.
					String sqldocumentno = "SELECT count(1) from m_inventory where documentno like '"+rs.getString("NUMERO_DOC")+"' and c_doctype_id = "+doctype+" and m_inventory_id != "+mov.get_ID();
					log.config("obtener nro documento existente "+sqldocumentno);
				
					int cantidaddoc = DB.getSQLValue(get_TrxName(), sqldocumentno);
					if(cantidaddoc > 0)
					{
						DB.executeUpdate("UPDATE i_importadw set description2 = 'NRO DOCUMENTO YA EXISTE', isabort='Y' where i_importadw_id = "+rs.getInt("I_Importadw_ID"),get_TrxName());
						return "Ya existe el nro de documento "+rs.getInt("I_ImportadW_ID");
					}

					mov.save();
					//
					cant++;
				}		
				//generacion de detalle
				MInventoryLine line = new MInventoryLine(getCtx(),0,get_TrxName());
				
				//line.setAD_Org_ID(mov.getAD_Org_ID());
				String sqlorg = "SELECT coalesce(AD_Org_ID,0) FROM AD_Org where name like '"+rs.getString("CENTRO_DE_COSTO")+"'";
				log.config("sql org "+sqlorg);
				int id_orgline = DB.getSQLValue(get_TrxName(), sqlorg);
				if(id_orgline == -1)
					line.setAD_Org_ID(mov.getAD_Org_ID());
				else
					line.setAD_Org_ID(id_orgline);
				
				line.setM_Inventory_ID(mov.get_ID());				
				//buscamos producto
				//int ID_Prod = DB.getSQLValue(get_TrxName(), " SELECT MAX(COALESCE(M_Product_ID,1003640)) FROM M_Product " +
				//		" WHERE VALUE like '"+rs.getString("CODPROD")+"'");
				
				//@mfrojas
				
				String sqlst = "SELECT max(m_product_Id) from m_product where value like '"+rs.getString("PRODUCTO")+"'";
				log.config("sql de producto = "+sqlst);
				int ID_Prod = DB.getSQLValue(get_TrxName(), sqlst);
				log.config("codprod "+rs.getString("PRODUCTO"));
				log.config("sql prod = "+ID_Prod);

				if(ID_Prod <= 0)
				{
					/*
					 * Agregar código extra
					 * isabort='Y'
					 * update description de cabecera
					 * Actualizar nro documento de cabecera (Agregar --1)
					 * */
					DB.executeUpdate("UPDATE M_Inventory set documentno = documentno || '-NN', description = description || ' ** No existe producto' where m_inventory_id = "+line.getM_Inventory_ID(),get_TrxName());

					return "No existe Producto";
				}
				line.setM_Product_ID(ID_Prod);
				
				//@mfrojas asociar la cantidad en negativo o positivo
				//positivo -> salida de bodega
				//negativo -> entrada a bodega
				
				log.config("Cantidad internal use "+rs.getBigDecimal("CANTIDAD"));
				log.config("Cantidad internal use "+rs.getBigDecimal("CANTIDAD").negate());
				//String tipomovimiento = rs.getString("TIPOMOV");
				//if(tipomovimiento.compareToIgnoreCase("Entrada")==0)
					line.setQtyInternalUse(rs.getBigDecimal("CANTIDAD").negate());
				//else if(tipomovimiento.compareToIgnoreCase("Salida")==0)
				//	line.setQtyInternalUse(rs.getBigDecimal("CANT"));
				
				
				//buscamos locator
				int ID_Loc = DB.getSQLValue(get_TrxName(), " SELECT MAX(M_Locator_ID) FROM M_Locator " +
						" WHERE IsDefault = 'Y' AND M_Warehouse_ID = "+mov.getM_Warehouse_ID());
				if(ID_Loc <= 0)
					ID_Loc = DB.getSQLValue(get_TrxName(), " SELECT MAX(M_Locator_ID) FROM M_Locator " +
							" WHERE M_Warehouse_ID = "+mov.getM_Warehouse_ID());
				line.setM_Locator_ID(ID_Loc);
				//buscamos cargo
				//@mfrojas el cargo es igual al motivo. 
				/*int ID_Charge = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Charge_ID) " +
						" FROM C_Charge cc " +
						" INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
						" WHERE ct.value = 'TCBW'");
				if(ID_Charge <=0)
					ID_Charge = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Charge_ID) " +
							" FROM C_Charge cc " +
							" WHERE IsActive = 'Y'");
				*/
				int ID_Charge = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Charge_ID) " +
						" FROM C_Charge cc " +
						" WHERE cc.name like '"+rs.getString("MOTIVO")+"' " +
						" ");
				
				log.config("cargo = "+rs.getString("MOTIVO"));
				log.config("ID CARGO "+ID_Charge);
				if(ID_Charge <= 0 )
					return "No existe nombre de cargo";
				line.setC_Charge_ID(ID_Charge);
				line.setM_AttributeSetInstance_ID(1059402);

				line.save();	
				//@mfrojas se ejecuta update de centro de costo, y de costo
				log.config("PXQ = "+rs.getString("PXQ")); 
				BigDecimal bdd=new BigDecimal(rs.getString("PXQ"));
				 

				DB.executeUpdate("UPDATE M_Inventoryline set costo = "+bdd+" where m_inventoryline_id = "+line.get_ID(),get_TrxName());
				DB.executeUpdate("UPDATE M_Inventoryline set CentroCosto = '"+rs.getString("CENTRO_DE_COSTO")+"' where m_inventoryline_id = "+line.get_ID(),get_TrxName());
				
				

				//@mfrojas se agrega código para ingresar un registro en mcostdetail, para que la 
				//contabilidad generada tenga el costo enviado.
				
/*				MCostDetail cost = new MCostDetail(getCtx(),0,get_TrxName());
				cost.setAmt(bdd);
	*/			
				//Si es entrada, el costo es positivo. Si es salida, el costo es negativo.
/*				if(tipomovimiento.compareToIgnoreCase("Entrada")==0)
					cost.setQty(rs.getBigDecimal("CANT"));
				else if(tipomovimiento.compareToIgnoreCase("Salida")==0)
					cost.setQty(rs.getBigDecimal("CANT").negate());
				
				cost.setM_InventoryLine_ID(line.get_ID());
				cost.setM_Product_ID(ID_Prod);
				cost.setDescription("Costo generado por producción vía WS");
				cost.setC_AcctSchema_ID(1000005);
				
				
				cost.save();
				
				DB.executeUpdate("UPDATE M_Cost set CurrentCostPrice = "+bdd.divide(rs.getBigDecimal("CANT"),2,RoundingMode.HALF_UP)+" where m_product_id = "+ID_Prod, get_TrxName());
				//@mfrojas costo agregado. 
*/				

				//se guardan ID para procesar
				LastMov = mov.get_ID();
				if(rs.getString("I_IMPORTADW_ID") != null)
					ID_movXML = ID_movXML+","+rs.getString("I_IMPORTADW_ID");
				if(rs.getString("I_IMPORTADWL_ID") != null)
					ID_movXMLLine = ID_movXMLLine+","+rs.getString("I_IMPORTADWL_ID");
				
				//@mfrojas se agrega actualización a m_storage para que no haya problemas de stock (por revisar!)
				String sqlstorage = "SELECT count(1) from m_storage where ad_client_id = 1000005 and m_product_id = "+ID_Prod+" and M_AttributesetInstance_ID = 1059402 and m_locator_id = "+ID_Loc;
				log.config("sql storage "+sqlstorage);
				//int storage = DB.getSQLValue(get_TrxName(), sqlstorage);
/*				if(storage > 0)
					DB.executeUpdate("Update m_storage set qtyonhand = 500 where ad_client_id = 1000005 and m_product_id = "+ID_Prod+" and M_Attributesetinstance_id = 1059402 and m_locator_id = "+ID_Loc,get_TrxName());
				else
				{
					//String sqlinsert = "INSERT INTO M_Storage VALUES ("+ID_Prod+","+ID_Loc+",1000005, 1000000, 'Y', '01/01/17',100,'01/01/17',100,500,0,0,'01/01/17',1059402)";
					
					
					MStorage m;
					m = new MStorage(getCtx(), 0, get_TrxName());
					m.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
					m.setM_Locator_ID(ID_Loc);
					m.setM_Product_ID(ID_Prod);
					m.setQtyOnHand(Env.ONEHUNDRED);
					m.setM_AttributeSetInstance_ID(1059402);
					if(m.save())
						log.config("SI");
					else
						log.config("NO");
					
					
				}
*/
			}
			if(mov != null && mov.getDocStatus().compareTo("DR")==0)
			{
				mov.processIt("CO");
				mov.saveEx(get_TrxName());
				
				
			}
					
			log.config("Se han generado "+cant+" documentos");
			//actualizamos registros procesados
			DB.executeUpdate("UPDATE I_IMPORTADW SET processed = 'Y' WHERE I_IMPORTADW_ID IN ("+ID_movXML+")",get_TrxName());
			DB.executeUpdate("UPDATE I_IMPORTADWL SET processed = 'Y' WHERE I_IMPORTADWL_ID IN ("+ID_movXMLLine+")",get_TrxName());
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

