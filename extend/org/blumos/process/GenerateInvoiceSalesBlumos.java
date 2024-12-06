package org.blumos.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;

import org.compiere.model.X_T_BL_REF_DTE;

public class GenerateInvoiceSalesBlumos extends SvrProcess {
	
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
		/** String sqlDet = "SELECT inv.numero_nf, inv.numero_pedido, inv.entrada_saida, inv.nat_op, inv.cf_op, inv.cf_op, inv.nome, inv.cnpj,  " + 
			" inv.dt_emissao, inv.cidade, inv.fone_fax, inv.uf, inv.inscricao_estadual, inv.hora_saida, inv.obs, inv.moeda, inv.encargo, " +
			" inv.data_impressa, inv.total_produtos, inv.total_nf, inv.status,  inv.forma_pagamento, inv.finalidade_nfe, " +
			" inv.codigo_cliente, inv.total_iva_chile, inv.processed, inv.vendedor, inv.terminopago,  " +
			//
			" invl.numero_nf, invl.descricao, invl.unidade, invl.qtde, invl.valor_unitario, invl.valor_total, invl.codigo_produto, " +
			" invl.iva_chile, invl.valor_iva_chile, invl.processed,inv.i_importinvoicexmlw_id, invl.i_importinvoicexmlwline_id,invl.costo_xml" +
			" FROM i_importinvoicexmlw inv " +
			" LEFT JOIN i_importinvoicexmlwline invl ON (inv.numero_nf = invl.numero_nf)" +
			" WHERE inv.processed = 'N'  and inv.isabort='N' AND (invl.isAbort='N' OR invl.isAbort is null) and (invl.processed = 'N' OR invl.processed IS NULL) " +
			" Order By inv.numero_nf Asc";
		*/
		//mfrojas 20181010 se cambia sql, para poder asociar los documentos de referencia y el tipo de socio de negocio.
		
		String sqlDet = "SELECT inv.numero_nf, inv.numero_pedido, inv.entrada_saida, inv.nat_op, inv.cf_op, inv.cf_op, inv.nome, inv.cnpj,  " + 
				" inv.dt_emissao, inv.cidade, inv.fone_fax, inv.uf, inv.inscricao_estadual, inv.hora_saida, inv.obs, inv.moeda, inv.encargo, " +
				" inv.data_impressa, inv.total_produtos, inv.total_nf, inv.status,  inv.forma_pagamento, inv.finalidade_nfe, " +
				" inv.codigo_cliente, inv.total_iva_chile, inv.processed, inv.vendedor, inv.terminopago,  " +
				//
				" invl.numero_nf, invl.descricao, invl.unidade, invl.qtde, invl.valor_unitario, invl.valor_total, invl.codigo_produto, " +
				" invl.iva_chile, invl.valor_iva_chile, invl.processed,inv.i_importinvoicexmlw_id, invl.i_importinvoicexmlwline_id,invl.costo_xml, " +
				" inv.venta_directa " +
				" FROM i_importinvoicexmlw inv " +
				" LEFT JOIN i_importinvoicexmlwline invl ON (inv.numero_nf = invl.numero_nf)" +
				
				" WHERE inv.processed = 'N'  and inv.isabort='N' AND (invl.isAbort='N' OR invl.isAbort is null) and (invl.processed = 'N' OR invl.processed IS NULL) " +
				" Order By inv.numero_nf Asc";
		
		log.config("sql = "+sqlDet);
		PreparedStatement pstmt = null;
		int cant = 0;
		String ID_invXML = "0";
		String ID_invXMLLine = "0";
		//@mfrojas se agrega variable de tipo string, para agregar al valor de retorno, en caso de encontrar errores.
		String Hay_Errores = "";
		int pr=0;

		try
		{
			pstmt = DB.prepareStatement (sqlDet, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();			
			String OrderFlag = null;
			String lastOrder = "";
			//Boolean flagComplete = false;
			MOrder ord = null;
			MInOut min = null;
			MInvoice inv = null;
			int lineNo = 0;
			while (rs.next ())
			{
				String LogError = "";
				OrderFlag = rs.getString("numero_nf");;
				//generación de cabecera de OV
				if(lastOrder.compareTo(OrderFlag) != 0 )
				{
					if(ord != null)
					{
						ord.setDocAction("CO");
						if(ord.processIt ("CO"))
						{
							if(ord.save())
								cant++;
						}
						//commitEx();
						ord = null;
					}
					if(ord == null)
					{			
						
						//se buscan valores necesarios para la NV
						//cliente
						String flag_vdirecta = rs.getString("venta_directa");

						int ID_ClienteRef = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Bpartner_ID) FROM C_Bpartner " +
								" WHERE IsActive = 'Y' AND AD_Client_ID = 1000000 AND (value||''||digito = '"+rs.getString("cnpj")+"' OR value = '"+rs.getString("cnpj")+"' OR value||'-'||digito = '"+rs.getString("cnpj")+"')");
						//validacion de clientref
						if(ID_ClienteRef <= 0)
							LogError = LogError + "Error 105: Cliente no registrado en BLUMOS";
						log.config("cliente ref"+ID_ClienteRef);						
						int ID_Cliente = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Bpartner_ID) FROM C_Bpartner " +
								" WHERE IsActive = 'Y' AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+" AND UPPER(NAME) LIKE '%BLUMOS%'");
						log.config("cliente "+ID_Cliente);
						if(ID_Cliente <= 0)
							LogError = LogError + "ERROR 101: El Socio de Negocio "+rs.getString("cnpj")+" no existe. Debe crearlo. Documento nro "+rs.getString("numero_nf")+".";
						//direccion
						int bpartnerlocation = DB.getSQLValue(get_TrxName(), "select max(c_bpartner_location_id) from c_bpartner_location where isactive='Y' and c_bpartner_id = "+ID_Cliente);
						if(bpartnerlocation <= 0)
							bpartnerlocation = 1006771;
						if(bpartnerlocation <= 0)
							LogError = LogError + "ERROR 102: El socio de negocio "+rs.getString("cnpj")+" no tiene dirección asociada. Documento nro "+rs.getString("numero_nf")+". ";
						//direccion ref
						int bpartnerlocationrefbill = DB.getSQLValue(get_TrxName(), "select max(c_bpartner_location_id) from c_bpartner_location where isactive='Y' and c_bpartner_id = "+ID_ClienteRef);
						int bpartnerlocationrefship = DB.getSQLValue(get_TrxName(), "select max(c_bpartner_location_id) from c_bpartner_location where isactive='Y' and c_bpartner_id = "+ID_ClienteRef);

						if(bpartnerlocationrefbill <= 0)
							LogError = LogError + "ERROR 102: El socio de negocio "+rs.getString("cnpj")+" no tiene dirección asociada. Documento nro "+rs.getString("numero_nf")+". ";

						//termino de pago
						int tp = 0;
						if(rs.getString("terminopago") != null)
						{
							int terminopago = Integer.parseInt(rs.getString("terminopago"));
							String sqlterminopago = "SELECT max(c_paymentterm_id) from c_paymentterm where c_paymentterm_id = "+terminopago+" AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
							log.config(sqlterminopago);						
							tp = DB.getSQLValue(get_TrxName(), sqlterminopago);
							if(tp <= 0)
								LogError = LogError + "ERROR 104: No existe termino de pago. ";
						}else
							LogError = "ERROR 104: No existe termino de pago. ";						
						//vendedor
						//mfrojas, 20180820
						//se cambia los NVL por COALESCE
						String sqlvendedor = "SELECT coalesce(max(ad_user_id),0) from ad_user where name like '"+ rs.getString("vendedor")+"' and AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
						int vend = DB.getSQLValue(get_TrxName(), sqlvendedor);
						if(vend <= 0)
							LogError = LogError + "ERROR 103: No existe vendedor";
						//validacion numero de doc
						String DocNo = null;
						//validacion 1
						int ID_Order = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_Order WHERE DocStatus IN ('CO','CL','DR','IP','IN') AND DocumentNo LIKE 'FS-"+rs.getString("numero_nf")+"'");
						if(ID_Order > 0)
						{
							DocNo = "FS-"+rs.getString("numero_nf")+"-1";
							//segundo nivel de validacion
							ID_Order = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_Order WHERE DocStatus IN ('CO','CL','DR','IP','IN') AND DocumentNo LIKE '"+DocNo+"'");
							if(ID_Order > 0)
							{
								DocNo = "FS-"+rs.getString("numero_nf")+"-2";
								//tercer nivel de validacion
								ID_Order = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_Order WHERE DocStatus IN ('CO','CL','DR','IP','IN') AND DocumentNo LIKE '"+DocNo+"'");
								if(ID_Order > 0)
									DocNo = "FS-"+rs.getString("numero_nf")+"-3";
							}
						}
						else
							DocNo = "FS-"+rs.getString("numero_nf");
						
						
						//id cliente en solutec
						if(flag_vdirecta.compareTo("Y") == 0)
						{
							ID_ClienteRef = DB.getSQLValue(get_TrxName(), "SELECT coalesce(MAX(C_Bpartner_ID),0) FROM C_Bpartner " +
									" WHERE IsActive = 'Y' AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+" AND " +
									" (value||''||digito = '"+rs.getString("cnpj")+"' OR value = '"+rs.getString("cnpj")+"' OR value||'-'||digito = '"+rs.getString("cnpj")+"')");
							if(ID_ClienteRef <= 0)
								LogError = LogError + "Error 106: Cliente no registrado en SOLUTEC";
							
							bpartnerlocationrefbill = DB.getSQLValue(get_TrxName(), "select max(c_bpartner_location_id) from c_bpartner_location " +
									" where isactive='Y' " +
									" and isbillto='Y' and " +
									" c_bpartner_id = "+ID_ClienteRef);
							if(bpartnerlocationrefbill <= 0)
								LogError = LogError + "ERROR 107: El socio de negocio "+rs.getString("cnpj")+" no tiene dirección de facturacion asociada. Documento nro "+rs.getString("numero_nf")+". ";

							bpartnerlocationrefship = DB.getSQLValue(get_TrxName(), "select max(c_bpartner_location_id) from c_bpartner_location " +
									" where isactive='Y' " +
									" and isshipto='Y' and " +
									" c_bpartner_id = "+ID_ClienteRef);

							if(bpartnerlocationrefbill <= 0)
								LogError = LogError + "ERROR 108: El socio de negocio "+rs.getString("cnpj")+" no tiene dirección de envio asociada. Documento nro "+rs.getString("numero_nf")+". ";

						}
						if(LogError != null && LogError.trim().length() > 1)
						{
							Hay_Errores = ". Hubo errores en el proceso "+LogError;
							DB.executeUpdate("UPDATE I_ImportInvoiceXMLW SET isabort='Y',help = '"+LogError+"' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());
						}						
						if(ID_Cliente > 0 && bpartnerlocation > 0 && tp > 0 && vend > 0 && ID_ClienteRef > 0 && bpartnerlocationrefbill > 0 && bpartnerlocationrefship > 0)
						{
							ord = new MOrder(getCtx(),0,get_TrxName());
							ord.setAD_Org_ID(1000023);
							ord.setC_DocType_ID(1000305);
							ord.setC_DocTypeTarget_ID(1000305);							
							if(rs.getTimestamp("dt_emissao")!=null)
							{
								ord.setDateOrdered(rs.getTimestamp("dt_emissao"));
								ord.setDateAcct(rs.getTimestamp("dt_emissao"));
							}
							
							if(flag_vdirecta.compareTo("Y")==0)
							{


								ord.setC_BPartner_ID(ID_ClienteRef);
								ord.setC_BPartner_Location_ID(bpartnerlocationrefship);
								ord.setBill_Location_ID(bpartnerlocationrefbill);
							}
							
							else if(flag_vdirecta.compareTo("N") == 0)
							{
								ord.setC_BPartner_ID(ID_Cliente);
								ord.setC_BPartner_Location_ID(bpartnerlocation);
								ord.setBill_Location_ID(bpartnerlocation);

							}
							
							
							//ord.setC_BPartner_Location_ID(bpartnerlocation);
							ord.setDocStatus("DR");
							ord.setDeliveryRule("A");
							ord.setM_Warehouse_ID(1000038);							
							//ord.setDocumentNo(rs.getString("numero_nf"));
							//ord.setDocumentNo(rs.getString("numero_pedido"));
							ord.setDocumentNo(DocNo);
							ord.setFreightCostRule("I");
							//obtener id de lista de precios
							String sqlpricelist = "SELECT coalesce(max(m_pricelist_id),0) from m_pricelist where name like 'WS_VENTAS' and AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
							pr = DB.getSQLValue(get_TrxName(), sqlpricelist);
							if(pr > 0)
								ord.setM_PriceList_ID(pr);
							else
								return "No existe lista de precios";
							
							ord.setC_PaymentTerm_ID(tp);
							ord.setIsSOTrx(true);
							ord.setSalesRep_ID(vend);	
							ord.setPOReference("000");
							if(ID_ClienteRef > 0)
								ord.set_CustomColumn("C_BPartnerRef_ID", ID_ClienteRef);
							if(ord.save())
							{
								DB.executeUpdate("UPDATE I_ImportInvoiceXMLW SET C_Order_ID = "+ord.get_ID()+" WHERE I_ImportInvoiceXMLW_ID IN ("+rs.getInt("I_ImportInvoiceXMLW_ID")+")",get_TrxName());
							}
							
							
							//@mfrojas INGRESO DE MINOUT
							
							min = new MInOut(getCtx(),0,get_TrxName());
							//Generación de Cabecera de Despacho. 
							min.setDocumentNo(ord.getDocumentNo());
							min.setAD_Org_ID(ord.getAD_Org_ID());
							min.setIsSOTrx(true);
							String sqldoctype = "SELECT coalesce(max(c_doctype_id),0) from c_doctype where name like '%Despacho%' and AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
							int doc = DB.getSQLValue(get_TrxName(), sqldoctype);
							if(doc > 0)
								min.setC_DocType_ID(doc);
							else
							{
								log.config("No existe tipo de documento de despacho");
								log.config("sql "+sqldoctype);
								return "No existe documento de despacho";
							}
							
							min.setMovementDate(ord.getDateOrdered());
							min.setDateAcct(ord.getDateOrdered());
							min.setC_BPartner_ID(ord.getC_BPartner_ID());
							min.setC_BPartner_Location_ID(ord.getC_BPartner_Location_ID());
							min.setM_Warehouse_ID(ord.getM_Warehouse_ID());
							min.setDeliveryRule("A");
							min.setFreightCostRule("I");
							min.setDocStatus("DR");
							min.setC_Order_ID(ord.get_ID());
							min.save();

							//@mfrojas TERMINO DE MINOUT
							
							
							//@mfrojas INGRESO DE FACTURA
							
							inv = new MInvoice(getCtx(),0,get_TrxName());
							
							inv = new MInvoice(getCtx(), 0, get_TrxName());
							String tipodoc = rs.getString("nat_op");
							if(tipodoc.toLowerCase().contains("factura"))
								
							{
								if(tipodoc.toLowerCase().contains("electr"))
								{
									inv.setC_DocType_ID(1000277);
									inv.setC_DocTypeTarget_ID(1000277);							
								}
								else
								{
									inv.setC_DocType_ID(1000278);
									inv.setC_DocTypeTarget_ID(1000278);
								}
							}
							else if (tipodoc.toLowerCase().contains("nota"))
							{

								if(tipodoc.toLowerCase().contains("electr"))
								{
									inv.setC_DocType_ID(1000747);
									inv.setC_DocTypeTarget_ID(1000747);							
								}
								else
								{
									inv.setC_DocType_ID(1000279);
									inv.setC_DocTypeTarget_ID(1000279);	
								}

							}
							
							inv.setIsSOTrx(true);
							inv.setM_PriceList_ID(ord.getM_PriceList_ID());
							inv.setDescription(rs.getString("obs"));
							inv.setDocStatus("DR");
							inv.setC_BPartner_ID(ord.getC_BPartner_ID());
							inv.setC_BPartner_Location_ID(ord.getBill_Location_ID());
							String sqlvalidatedoc = "SELECT count(1) from c_invoice where issotrx='Y' and c_bpartner_id = "+ord.getC_BPartner_ID()+" and documentno like '"+rs.getString("numero_nf")+"'";
							log.config(sqlvalidatedoc);
							//int counter = DB.getSQLValue(get_TrxName(), sqlvalidatedoc);
							/*if(counter == 0)
								inv.setDocumentNo(rs.getString("numero_nf"));
							else
							{
								DB.executeUpdate("UPDATE I_ImportInvoiceXMLW SET isabort='Y' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());
								return "Error 201: Ya existe este número de factura ("+rs.getString("numero_nf")+") para el proveedor Solutec";
							}
							*/
							inv.setDateAcct(ord.getDateAcct());
							inv.setDateInvoiced(ord.getDateOrdered());
							inv.setSalesRep_ID(ord.getSalesRep_ID());
							inv.setC_PaymentTerm_ID(ord.getC_PaymentTerm_ID());
							inv.setC_Order_ID(ord.getC_Order_ID());
							
							inv.save();
							
							//@mfrojas TERMINO DE INGRESO DE FACTURA


							//	
						}
					}					
				}
				if(ord != null)
				{
					//generacion de detalle
					MOrderLine oLine = new MOrderLine(ord);
					//se buscan valores necesarios
					//producto
					String sqlst = "SELECT max(m_product_Id) from m_product where value like '"+rs.getString("codigo_produto")+"' AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
					log.config("sql de producto = "+sqlst);
					int prod_id = DB.getSQLValue(get_TrxName(), sqlst);					
					if(prod_id > 0)
					{
						//verificamos si existe prod en la lista de precios
						int existPL = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM M_ProductPrice" +
								" WHERE M_PriceList_Version_ID in(select m_pricelist_version_id from m_pricelist_version where m_pricelist_id = "+pr+")  AND M_Product_ID = "+prod_id);
						if(existPL > 0)
						{
							lineNo += 10;
							oLine.setLine(lineNo);
							oLine.setM_Product_ID(prod_id);
							oLine.setM_AttributeSetInstance_ID(60000);
							//cantidad
							if(rs.getString("qtde") != null)
								oLine.setQty(rs.getBigDecimal("qtde"));
							else
								oLine.setQty(Env.ONE);
							//precio
							if(rs.getBigDecimal("valor_unitario")!=null)
								oLine.setPrice(rs.getBigDecimal("valor_unitario"));
							else
								oLine.setPrice(Env.ZERO);
							//neto de linea
							if(rs.getBigDecimal("valor_total") != null)
								oLine.setLineNetAmt(rs.getBigDecimal("valor_total"));					
							//descripcion
							if(rs.getString("descricao") != null)
								oLine.setDescription(rs.getString("descricao"));
							//costo 
							if(rs.getBigDecimal("costo_xml") != null)
								oLine.set_CustomColumn("costo_xml", rs.getBigDecimal("costo_xml"));
							if(oLine.save())
							{
								//@mfrojas ingreso de linea de inout.
								
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
								int loc = DB.getSQLValue(get_TrxName(),"SELECT coalesce(max(m_locator_id),0) from m_locator where m_warehouse_id = "+ord.getM_Warehouse_ID()+" and isactive='Y'");
								if(loc > 0)
									minl.setM_Locator_ID(loc);
								else
									minl.setM_Locator_ID(1000066);
								
								minl.setC_OrderLine_ID(oLine.getC_OrderLine_ID());

								minl.save();
								
								//@mfrojas termino de linea de inout.
								
								
								
								//@mfrojas ingreso de linea de factura
								
								MInvoiceLine invl = new MInvoiceLine(inv);
								
								invl.setM_Product_ID(oLine.getM_Product_ID());
								invl.setQty(oLine.getQtyOrdered());
								invl.setQtyEntered(oLine.getQtyEntered());
								invl.setQtyInvoiced(oLine.getQtyOrdered());
								invl.setPrice(oLine.getPriceEntered());
								invl.setPriceEntered(oLine.getPriceEntered());
								invl.setPriceActual(oLine.getPriceEntered());
								invl.setDescription(oLine.getDescription());
								invl.setM_InOutLine_ID(minl.get_ID());
								invl.setC_OrderLine_ID(oLine.get_ID());
								
								invl.save();
								//@mfrojas termino de ingreso linea de factura
								DB.executeUpdate("UPDATE I_ImportInvoiceXMLWLine SET C_OrderLine_ID = "+oLine.get_ID()+" WHERE I_ImportInvoiceXMLWLine_ID IN ("+rs.getInt("I_ImportInvoiceXMLWLine_ID")+")",get_TrxName());
							}
						}
						else
							{
								LogError = LogError + "ERROR 302: Producto "+rs.getString("codigo_produto")+" no existe en lista de precio ";
								return "ERROR 302: Producto "+rs.getString("codigo_produto")+" no existe en lista de precio ";
								
							}
						
					}else
					{
						LogError = LogError + "ERROR 301: Producto con código "+rs.getString("codigo_produto");
						return "ERROR 301: Producto con código "+rs.getString("codigo_produto");
					}
					if(LogError != null && LogError.trim().length() > 1)
					{
						Hay_Errores = ". Hubo errores en el proceso "+LogError;
						DB.executeUpdate("UPDATE I_ImportInvoiceXMLW SET isabort='Y',help = '"+LogError+"' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());
					}
				}
				lastOrder = OrderFlag;
				//se guardan ID para actualizar
				if(rs.getString("I_ImportInvoiceXMLW_ID") != null)
					ID_invXML = ID_invXML+","+rs.getString("I_ImportInvoiceXMLW_ID");
				if(rs.getString("I_ImportInvoiceXMLWLine_ID") != null)
					ID_invXMLLine = ID_invXMLLine+","+rs.getString("I_ImportInvoiceXMLWLine_ID");
			}
			//se completa ultima nota de venta
			if(ord != null && ord.getDocStatus().compareTo("CO") != 0)
			{
				ord.setDocAction("CO");
				if(ord.processIt ("CO"))
				{
					if(ord.save())
						cant++;
				}
				min.setDocAction("CO");
				min.processIt("CO");
				
				min.save();
				
				//mfrojas 20181010 se agrega código para ingresar los valores de referencia de la factura
				String sqlref = "SELECT a.i_importinvoicexmlwref_id, a.numero_nf, a.date_ref, a.tipo_doc_ref, a.doc_referenciado, a.cod_ref, a.razon_ref " +
						" from I_IMPORTINVOICEXMLWREF a WHERE (a.processed='N' OR a.processed is NULL) and a.numero_nf like '"+OrderFlag+"'";
				log.config("sqlref "+sqlref);
				
				PreparedStatement pstmt2 = null;
				
				pstmt2 = DB.prepareStatement (sqlref, get_TrxName());
				ResultSet rs2 = pstmt2.executeQuery ();			
				String ID_invxmlref = "0";
				int line = 10;
				while (rs2.next ())
				{
					X_T_BL_REF_DTE dte = new X_T_BL_REF_DTE(getCtx(), 0, get_TrxName());
					dte.setC_Invoice_ID(inv.get_ID());
					dte.setDATE_REF(rs2.getTimestamp("date_ref"));
					dte.setCOD_REF(rs2.getString("cod_ref"));
					dte.setTIPO_DOC_REF(rs2.getString("tipo_doc_ref"));
					dte.setDOC_REFERENCIADO(rs2.getString("doc_referenciado"));
					dte.setRAZON_REF(rs2.getString("razon_ref"));
					dte.setLine(line);
					dte.save();
					if(rs2.getString("I_ImportInvoiceXMLWref_ID") != null)
						ID_invxmlref = ID_invxmlref+","+rs2.getString("I_ImportInvoiceXMLWref_ID");

					line += 10;
				}
				DB.executeUpdate("UPDATE I_ImportInvoiceXMLWREF SET processed = 'Y' WHERE I_ImportInvoiceXMLWRef_ID in ("+ID_invxmlref+")",get_TrxName());

				inv.setDocAction("CO");
				inv.processIt("CO");
				inv.save();
				inv.set_CustomColumn("EMITIR_DTE", "Y");
				inv.save();
				//commitEx();
				ord = null;
				min = null;
				inv = null;
			}
			
			//actualizamos registros procesados
			DB.executeUpdate("UPDATE I_ImportInvoiceXMLW SET processed = 'Y' WHERE I_ImportInvoiceXMLW_ID IN ("+ID_invXML+") AND (isAbort='N' OR isAbort is null)",get_TrxName());
			DB.executeUpdate("UPDATE I_ImportInvoiceXMLWLINE SET processed = 'Y' WHERE I_ImportInvoiceXMLWLine_ID IN ("+ID_invXMLLine+") AND (isAbort='N' OR isAbort is null)",get_TrxName());
			rs.close ();
			pstmt.close ();
			pstmt = null;	
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}		
		return "GENERADOS "+cant+" PROCESOS "+Hay_Errores;
	}	//	doIt
	public int GenerateShip(MOrder order)
	{
		MInOut min = new MInOut(getCtx(),0,get_TrxName());
		//Generación de Cabecera de Despacho. 
		min.setDocumentNo(order.getDocumentNo());
		min.setAD_Org_ID(order.getAD_Org_ID());
		min.setIsSOTrx(true);
		String sqldoctype = "SELECT coalesce(max(c_doctype_id),0) from c_doctype where name like '%Despacho%' and AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
		int doc = DB.getSQLValue(get_TrxName(), sqldoctype);
		if(doc > 0)
			min.setC_DocType_ID(doc);
		else
		{
			log.config("No existe tipo de documento de despacho");
			log.config("sql "+sqldoctype);
			return 0;
		}
		
		min.setMovementDate(order.getDateOrdered());
		min.setDateAcct(order.getDateOrdered());
		min.setC_BPartner_ID(order.getC_BPartner_ID());
		min.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());
		min.setM_Warehouse_ID(order.getM_Warehouse_ID());
		min.setDeliveryRule("A");
		min.setFreightCostRule("I");
		min.setDocStatus("DR");
		min.setC_Order_ID(order.get_ID());
		min.save();
		
		
		
		return 0;		
	}
	public int GenerateInv(MOrder order, MInOut ship)
	{
		return 0;		
	}
}
