//@mfrojas generateMovement Blumos

package org.blumos.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;

public class GeneratePurchaseInvoice extends SvrProcess {
	
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
		String sqlDet = "SELECT inv.numero_nf, inv.numero_pedido, inv.entrada_saida, inv.nat_op, inv.cf_op, inv.nome, inv.cnpj,  " + 
				" inv.dt_emissao, inv.endereco, inv.bairro, inv.cidade, inv.fone_fax, inv.uf, inv.hora_saida, inv.obs, inv.moeda,  " +
				" inv.data_impressa, inv.total_produtos, inv.total_nf, inv.status,  inv.data_status, inv.terminopago, " +
				" inv.datta_periodo_contabil, inv.valor_iva, inv.processed, " +
				//mfrojas campos dde invoiceline
				" invl.numero_nf, invl.descricao, invl.unidade, invl.qtde, invl.valor_unitario, invl.valor_total, invl.codigo_mp, " +
				" invl.iva, invl.valor_iva, invl.numero_pedido,invl.clas_fiscal, invl.processed,inv.i_importinvoicexmlwp_id, invl.i_importinvoicexmlwpline_id" +
				" FROM i_importinvoicexmlwp inv " +
				" LEFT JOIN i_importinvoicexmlwpline invl ON (inv.numero_nf = invl.numero_nf)" +
				" WHERE inv.processed = 'N' AND inv.isAbort='N' AND (invl.isAbort='N' OR invl.isAbort is null)AND (invl.processed = 'N' OR invl.processed IS NULL) " +
				" Order By inv.numero_nf Desc";
		
		log.config("sql = "+sqlDet);
		PreparedStatement pstmt = null;
		int cant = 0;
		String ID_invXML = "0";
		String ID_invXMLLine = "0";
		try
		{
			pstmt = DB.prepareStatement (sqlDet, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();			
			int LastMov = -1;
			int id_invAD = 0;
			MInvoice inv = null;
			MInOut min = null;
			MOrder ord = null;
			while (rs.next ())
			{
				id_invAD = DB.getSQLValue(get_TrxName(),"SELECT MAX(C_Invoice_ID) FROM C_Invoice");
						
				//generación de cabecera de factura
				if(id_invAD != LastMov)
				{
					if(inv != null)
					{
						ord.processIt("CO");
						ord.save();
						min.processIt("CO");
						min.save();
						inv.processIt("CO");
						inv.save();
						
						ord.completeIt();
						ord.processIt("CO");
						ord.setDocStatus("CO");
						ord.setDocAction("CL");
						ord.setProcessed(true);
						
						ord.save();
						inv.set_CustomColumn("EMITIR_DTE", "Y");
						inv.save();
					}
					//boolean flag = false;
					inv = new MInvoice(getCtx(), 0, get_TrxName());
					inv.setAD_Org_ID(1000023);

					String tipodoc = "";
					tipodoc = rs.getString("nat_op");

					if(tipodoc.toLowerCase().contains("factura"))
						
					{
						if(tipodoc.toLowerCase().contains("electr"))
						{
							inv.setC_DocType_ID(1000663);
							inv.setC_DocTypeTarget_ID(1000663);							
						}
						else
						{
							inv.setC_DocType_ID(1000280);
							inv.setC_DocTypeTarget_ID(1000280);
						}
					}
					else if (tipodoc.toLowerCase().contains("nota"))
					{
						if(tipodoc.toLowerCase().contains("electr"))
						{
							inv.setC_DocType_ID(1000378);
							inv.setC_DocTypeTarget_ID(1000378);							
						}
						else
						{
						
							inv.setC_DocType_ID(1000281);
							inv.setC_DocTypeTarget_ID(1000281);
						}
					}
					else
					{	
						inv.setC_DocType_ID(1000036);
						inv.setC_DocTypeTarget_ID(1000036);
					}
					inv.setIsSOTrx(false);
					inv.setDescription(rs.getString("obs"));
					
					inv.setM_PriceList_ID(1002936);
					//int counter = DB.getSQLValue(get_TrxName(), sqlvalidatedoc);
					//if(counter == 0)
					//	inv.setDocumentNo(rs.getString("numero_nf"));
					//else
					//	return "Ya existe este número de factura para el proveedor";
					//inv.save();
					inv.setDocStatus("DR");

					
					String sqlstt = "SELECT MAX(C_Bpartner_ID) FROM C_Bpartner " +
							" WHERE IsActive = 'Y' AND (value = '"+rs.getString("cnpj")+"' OR value||'-'||digito = '"+rs.getString("cnpj")+"')";
					log.config(sqlstt);
					int Proveedor = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Bpartner_ID) FROM C_Bpartner " +
							" WHERE IsActive = 'Y' AND (value||''||digito = '"+rs.getString("cnpj")+"' OR value = '"+rs.getString("cnpj")+"' OR value||'-'||digito = '"+rs.getString("cnpj")+"')");
					log.config("proveedor "+Proveedor);
					if(Proveedor > 0)
		
						inv.set_CustomColumn("C_Bpartner_ID", Proveedor);
					
					else
					{
						DB.executeUpdate("UPDATE I_ImportInvoiceXMLWP SET isabort='Y' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());
	
						return "ERROR 101: El Socio de Negocio "+rs.getString("cnpj")+"no existe. Debe crearlo. Documento nro "+rs.getString("numero_nf");
						}
					
					
					int bpartnerlocation = DB.getSQLValue(get_TrxName(), "select max(c_bpartner_location_id) from c_bpartner_location where isactive='Y' and c_bpartner_id = "+Proveedor);
					if(bpartnerlocation > 0)
						inv.setC_BPartner_Location_ID(bpartnerlocation);
					else
					{
						DB.executeUpdate("UPDATE I_ImportInvoiceXMLW SET isabort='Y' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());

						return "ERROR 102: El socio de negocio "+rs.getString("cnpj")+"no tiene dirección asociada. Documento nro "+rs.getString("numero_nf");
					}

					String sqlvalidatedoc = "SELECT count(1) from c_invoice where issotrx='N' and c_bpartner_id = "+Proveedor+" and documentno like '"+rs.getString("numero_nf")+"'";
					log.config(sqlvalidatedoc);
					int counter = DB.getSQLValue(get_TrxName(), sqlvalidatedoc);
					if(counter == 0)
						inv.setDocumentNo(rs.getString("numero_nf"));
					else
					{
						DB.executeUpdate("UPDATE I_ImportInvoiceXMLWP SET isabort='Y' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());

						return "Error 201: Ya existe este número de factura "+rs.getString("numero_nf")+" para el proveedor"+rs.getString("cnpj");
					}
					log.config("terminopago "+rs.getString("terminopago"));
					
					if(rs.getString("terminopago") == null)
						return "ERROR 104: No existe termino de pago";
					
					int terminopago = Integer.parseInt(rs.getString("terminopago"));
					String sqlterminopago = "SELECT max(c_paymentterm_id) from c_paymentterm where c_paymentterm_id = "+terminopago;
					log.config(sqlterminopago);
					
					int tp = DB.getSQLValue(get_TrxName(), sqlterminopago);
					if(tp > 0)
						inv.setC_PaymentTerm_ID(tp);
					else
					{
						DB.executeUpdate("UPDATE I_ImportInvoiceXMLWP SET isabort='Y' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());
						return "ERROR 104: No existe termino de pago";
					}

					//inv.setC_BPartner_Location_ID(1008437);

					if(rs.getTimestamp("dt_emissao")!=null)
					{
						inv.setDateInvoiced(rs.getTimestamp("dt_emissao"));
						inv.setDateAcct(rs.getTimestamp("dt_emissao"));
					}
			
					log.config("fecha "+rs.getTimestamp("dt_emissao"));
					
					inv.save();
					
					
					//@mfrojas realizar ingreso de recibo de materiales
					
					min = new MInOut(getCtx(),0,get_TrxName());
					
					min.setAD_Org_ID(1000023);
					min.setC_DocType_ID(1000289);
					min.setIsSOTrx(false);
					min.setDocumentNo(rs.getString("numero_nf"));
					min.setMovementDate(rs.getTimestamp("dt_emissao"));
					min.setC_BPartner_ID(Proveedor);
					min.setC_BPartner_Location_ID(bpartnerlocation);
					min.setM_Warehouse_ID(1000037);
					min.setDeliveryRule("A");
					min.setFreightCostRule("I");
					min.setDateAcct(rs.getTimestamp("dt_emissao"));
					min.setDocStatus("DR");
					
					min.save();
					
					//@mfrojas realizar ingreso de OC
					
					ord = new MOrder(getCtx(),0,get_TrxName());
					
					ord.setAD_Org_ID(1000023);
					ord.setC_DocType_ID(1000291);
					ord.setC_DocTypeTarget_ID(1000291);
					ord.setDateOrdered(rs.getTimestamp("dt_emissao"));
					ord.setC_BPartner_ID(Proveedor);
					ord.setC_BPartner_Location_ID(bpartnerlocation);
					ord.setDocStatus("DR");
					ord.setDeliveryRule("A");
					ord.setM_Warehouse_ID(1000037);
					ord.setDocumentNo(rs.getString("numero_nf"));
					ord.setFreightCostRule("I");
					ord.setM_PriceList_ID(1002936);
					ord.setC_PaymentTerm_ID(tp);
					ord.setIsSOTrx(false);
					ord.setSalesRep_ID(1000982);
					
					ord.save();
					
					
					DB.executeUpdate("UPDATE C_invoice  SET c_order_id="+ord.get_ID()+" WHERE c_invoice_id = "+inv.get_ID()+"",get_TrxName());
					DB.executeUpdate("UPDATE M_InOut  SET c_order_id="+ord.get_ID()+" WHERE M_InOut_ID = "+min.get_ID()+"",get_TrxName());

					
					cant++;
				}		
				//generacion de detalle
				//boolean flagLine = false;
				MInvoiceLine line = new MInvoiceLine(inv);
				
				String sqlst = "SELECT max(m_product_Id) from m_product where value like '%"+rs.getString("codigo_mp")+"%'";
				log.config("sql de producto = "+sqlst);
				int prod_id = DB.getSQLValue(get_TrxName(), sqlst);
				if(prod_id > 0)
					line.setM_Product_ID(prod_id);
				else
				{
					DB.executeUpdate("UPDATE I_ImportInvoiceXMLWP SET isabort='Y' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());
					DB.executeUpdate("UPDATE I_ImportInvoiceXMLWPLine SET isabort='Y' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());

					return "ERROR 301: Producto con código "+rs.getString("codigo_mp")+" no se conoce. Nro documento "+rs.getString("numero_nf");
				}
				
				if(rs.getString("qtde") != null)
				{
					line.setQtyInvoiced(rs.getBigDecimal("qtde"));
					line.setQty(rs.getBigDecimal("qtde"));
					line.setQtyEntered(rs.getBigDecimal("qtde"));
					
				}
				else
				{
					line.setQtyInvoiced(Env.ONE);
					line.setQty(Env.ONE);
					line.setQtyEntered(Env.ONE);
					
				}
				
				if(rs.getBigDecimal("valor_unitario")!=null)
				{
					line.setPriceEntered(rs.getBigDecimal("valor_unitario"));
					line.setPrice(rs.getBigDecimal("valor_unitario"));
					line.setPriceActual(rs.getBigDecimal("valor_unitario"));

				}
				
				
				if(rs.getBigDecimal("valor_total") != null)
					line.setLineNetAmt(rs.getBigDecimal("valor_total"));
				
					
				line.save();
					
				if(rs.getString("descricao") != null)
					line.setDescription(rs.getString("descricao"));
					
				LastMov = inv.get_ID();

				if(rs.getString("I_ImportInvoiceXMLWP_ID") != null)
					ID_invXML = ID_invXML+","+rs.getString("I_ImportInvoiceXMLWP_ID");
				if(rs.getString("I_ImportInvoiceXMLWPLine_ID") != null)
					ID_invXMLLine = ID_invXMLLine+","+rs.getString("I_ImportInvoiceXMLWPLine_ID");
				
				//@mfrojas MINOUTLINE
				
				MInOutLine minl = new MInOutLine(min);
				
				minl.setM_Product_ID(prod_id);
				if(rs.getString("qtde") != null)
				{
					minl.setQty(rs.getBigDecimal("qtde"));
					minl.setQtyEntered(rs.getBigDecimal("qtde"));
					
				}
				else
				{
					minl.setQty(Env.ONE);
					minl.setQtyEntered(Env.ONE);
					
				}
				minl.setDescription(rs.getString("descricao"));
				minl.setM_Locator_ID(1000065);
				minl.setM_AttributeSetInstance_ID(60000);
				
				//@mfrojas CORDERLINE
				
				MOrderLine ordl = new MOrderLine(ord);
				ordl.setM_Product_ID(prod_id);
				if(rs.getString("qtde") != null)
				{
					ordl.setQtyEntered(rs.getBigDecimal("qtde"));
					//ordl.setQtyInvoiced(rs.getBigDecimal("qtde"));
					//ordl.setQtyReserved(Env.ZERO);
					//ordl.setQtyDelivered(rs.getBigDecimal("qtde"));
					ordl.setQtyOrdered(rs.getBigDecimal("qtde"));
					
					
				}
				else
				{
					ordl.setQtyEntered(Env.ZERO);
					ordl.setQtyInvoiced(Env.ZERO);
					ordl.setQtyReserved(Env.ZERO);
					ordl.setQtyDelivered(Env.ZERO);
					ordl.setQtyOrdered(Env.ZERO);
					
				}
				
				ordl.setPrice(rs.getBigDecimal("valor_unitario"));
				ordl.setPriceEntered(rs.getBigDecimal("valor_unitario"));
				ordl.setPriceActual(rs.getBigDecimal("valor_unitario"));
				
				ordl.setDescription(rs.getString("descricao"));
				ordl.setM_AttributeSetInstance_ID(60000);
				ordl.save();
				minl.setC_OrderLine_ID(ordl.get_ID());
				minl.save();
				line.setM_InOutLine_ID(minl.get_ID());
				line.setC_OrderLine_ID(ordl.get_ID());
				line.save();


			}
			
			if(inv != null)
			{

				ord.processIt("CO");
				ord.save();
				min.processIt("CO");
				min.save();
				inv.processIt("CO");
				inv.save();
				
				ord.completeIt();
				ord.processIt("CO");
				ord.setDocStatus("CO");
				ord.setDocAction("CL");
				ord.setProcessed(true);
				
				ord.save();
				inv.set_CustomColumn("EMITIR_DTE", "Y");
				inv.save();
			}
					
			//actualizamos registros procesados
			DB.executeUpdate("UPDATE I_ImportInvoiceXMLWP SET processed = 'Y' WHERE I_ImportInvoiceXMLWP_ID IN ("+ID_invXML+")",get_TrxName());
			DB.executeUpdate("UPDATE I_ImportInvoiceXMLWPLINE SET processed = 'Y' WHERE I_ImportInvoiceXMLWPLine_ID IN ("+ID_invXMLLine+")",get_TrxName());
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
}
