package org.blumos.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MInOut;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;

public class GenerateInvoicePurchaseBlumos extends SvrProcess {
	
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
				" inv.datta_periodo_contabil, inv.valor_iva, inv.processed, inv.flag, " +
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
		//@mfrojas se agrega variable de tipo string, para agregar al valor de retorno, en caso de encontrar errores.
		String Hay_Errores = "";
		int pr = 0;

		try
		{
			pstmt = DB.prepareStatement (sqlDet, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();			
			String OrderFlag = null;
			String lastOrder = "";
			//Boolean flagComplete = false;
			MOrder ord = null;
			int lineNo = 0;
			while (rs.next ())
			{
				String LogError = "";
				OrderFlag = rs.getString("numero_nf");;
				//generación de cabecera de OC
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
						//se buscan valores necesarios para la OC
						//proveedor
/*						int ID_ClienteRef = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Bpartner_ID) FROM C_Bpartner " +
								" WHERE IsActive = 'Y' AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+" AND (value||''||digito = '"+rs.getString("cnpj")+"' OR value = '"+rs.getString("cnpj")+"' OR value||'-'||digito = '"+rs.getString("cnpj")+"')");
						log.config("cliente ref"+ID_ClienteRef);						
						int ID_Cliente = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Bpartner_ID) FROM C_Bpartner " +
								" WHERE IsActive = 'Y' AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+" AND UPPER(NAME) LIKE '%BLUMOS%'");
						log.config("cliente "+ID_Cliente);
						if(ID_Cliente <= 0)
							LogError = LogError + "ERROR 101: El Socio de Negocio "+rs.getString("cnpj")+" no existe. Debe crearlo. Documento nro "+rs.getString("numero_nf")+".";
							
							
							
*/						//@mfrojas se obtiene flag para saber qué socio de negocio utilizar
						
						String flag_v = rs.getString("flag");
						log.config("FLAG: "+flag_v);
						String sqlstt = "SELECT MAX(C_Bpartner_ID) FROM C_Bpartner " +
								" WHERE IsActive = 'Y' AND (value = '"+rs.getString("cnpj")+"' OR value||'-'||digito = '"+rs.getString("cnpj")+"')";
						log.config(sqlstt);
						//@mfrojas se agrega ad_client a búsqueda de BP
						int Proveedor = 0;
						if(flag_v.compareTo("'Y'")==0 || flag_v.compareTo("Y")==0)	
							Proveedor = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Bpartner_ID) FROM C_Bpartner " +
								" WHERE AD_Client_ID = 1000005 and IsActive = 'Y' AND (value||''||digito = '"+rs.getString("cnpj")+"' OR value = '"+rs.getString("cnpj")+"' OR value||'-'||digito = '"+rs.getString("cnpj")+"')");
						else if(flag_v.compareTo("'N'")==0 || flag_v.compareTo("N")==0)
							Proveedor = 1005653;
						log.config("proveedor "+Proveedor);
						if(Proveedor <= 0)
							LogError = LogError + "ERROR 101: El Socio de Negocio "+rs.getString("cnpj")+" no existe. Debe crearlo. Documento nro "+rs.getString("numero_nf")+".";
		

						//direccion
						int bpartnerlocation = DB.getSQLValue(get_TrxName(), "select max(c_bpartner_location_id) from c_bpartner_location where isactive='Y' and c_bpartner_id = "+Proveedor);
						//if(bpartnerlocation <= 0)
						//	bpartnerlocation = 1006771;
						if(bpartnerlocation <= 0)
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
						/*String sqlvendedor = "SELECT nvl(max(ad_user_id),0) from ad_user where name like '"+ rs.getString("vendedor")+"' and AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
						int vend = DB.getSQLValue(get_TrxName(), sqlvendedor);
						if(vend <= 0)
							LogError = LogError + "ERROR 103: No existe vendedor";
							*/
						
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
							
						if(LogError != null && LogError.trim().length() > 1)
						{
							Hay_Errores = ". Hubo errores en el proceso "+LogError;
							DB.executeUpdate("UPDATE I_ImportInvoiceXMLWP SET isabort='Y',help = '"+LogError+"' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());
						}						
						if(Proveedor > 0 && bpartnerlocation > 0 && tp > 0)
						{
							ord = new MOrder(getCtx(),0,get_TrxName());
							ord.setAD_Org_ID(1000023);
							ord.setC_DocType_ID(1000291);
							ord.setC_DocTypeTarget_ID(1000291);							
							if(rs.getTimestamp("dt_emissao")!=null)
							{
								ord.setDateOrdered(rs.getTimestamp("dt_emissao"));
								ord.setDateAcct(rs.getTimestamp("dt_emissao"));
							}
							ord.setC_BPartner_ID(Proveedor);
							ord.setC_BPartner_Location_ID(bpartnerlocation);
							ord.setDocStatus("DR");
							ord.setDeliveryRule("A");
							ord.setM_Warehouse_ID(1000038);							
							//ord.setDocumentNo(rs.getString("numero_nf"));
							//ord.setDocumentNo(rs.getString("numero_pedido"));
							ord.setDocumentNo(DocNo);
							ord.setFreightCostRule("I");
							//ord.setM_PriceList_ID(1002936);
							//obtener id de lista de precios
							//mfrojas, 20180820
							//se cambia los NVL por COALESCE
							String sqlpricelist = "SELECT coalesce(max(m_pricelist_id),0) from m_pricelist where name like 'WS_COMPRAS' and AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
							pr = DB.getSQLValue(get_TrxName(), sqlpricelist);
							if(pr > 0)
								ord.setM_PriceList_ID(pr);
							else
								return "No existe lista de precios";

							ord.setC_PaymentTerm_ID(tp);
							ord.setIsSOTrx(false);
							ord.setSalesRep_ID(1000982);
/*							if(ID_ClienteRef > 0)
								ord.set_CustomColumn("C_BPartnerRef_ID", ID_ClienteRef);
*/							if(ord.save())
							{
								DB.executeUpdate("UPDATE I_ImportInvoiceXMLWP SET C_Order_ID = "+ord.get_ID()+" WHERE I_ImportInvoiceXMLWP_ID IN ("+rs.getInt("I_ImportInvoiceXMLWP_ID")+")",get_TrxName());
							}
							
							//	
						}
					}					
				}
				if(ord != null)
				{
					//generacion de detalle
					MOrderLine oLine = new MOrderLine(ord);
					// se buscan valores necesarios
					//producto
					String sqlst = "SELECT max(m_product_Id) from m_product where value like '"+rs.getString("codigo_mp")+"' AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
					log.config("sql de producto = "+sqlst);
					int prod_id = DB.getSQLValue(get_TrxName(), sqlst);					
					if(prod_id > 0)
					{
						//verificamos si existe prod en la lista de precios
//						int existPL = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM M_ProductPrice" +
//								" WHERE M_PriceList_Version_ID = 1002886 AND M_Product_ID = "+prod_id);
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
							/*if(rs.getBigDecimal("costo_xml") != null)
								oLine.set_CustomColumn("costo_xml", rs.getBigDecimal("costo_xml"));
								*/
							if(oLine.save())
							{
								DB.executeUpdate("UPDATE I_ImportInvoiceXMLWPLine SET C_OrderLine_ID = "+oLine.get_ID()+" WHERE I_ImportInvoiceXMLWPLine_ID IN ("+rs.getInt("I_ImportInvoiceXMLWPLine_ID")+")",get_TrxName());
							}
						}
						else
							LogError = LogError + "ERROR 302: Producto "+rs.getString("codigo_mp")+" no existe en lista de precio ";
						
					}else
					{
						LogError = LogError + "ERROR 301: Producto con código "+rs.getString("codigo_mp");
					}
					if(LogError != null && LogError.trim().length() > 1)
					{
						Hay_Errores = ". Hubo errores en el proceso "+LogError;
						DB.executeUpdate("UPDATE I_ImportInvoiceXMLWPline SET isabort='Y',help = '"+LogError+"' WHERE numero_nf='"+rs.getString("numero_nf")+"'",get_TrxName());
					}
				}
				lastOrder = OrderFlag;
				//se guardan ID para actualizar
				if(rs.getString("I_ImportInvoiceXMLWP_ID") != null)
					ID_invXML = ID_invXML+","+rs.getString("I_ImportInvoiceXMLWP_ID");
				if(rs.getString("I_ImportInvoiceXMLWPLine_ID") != null)
					ID_invXMLLine = ID_invXMLLine+","+rs.getString("I_ImportInvoiceXMLWPLine_ID");
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
				//commitEx();
				ord = null;
			}
			
			//actualizamos registros procesados
			DB.executeUpdate("UPDATE I_ImportInvoiceXMLWP SET processed = 'Y' WHERE I_ImportInvoiceXMLWP_ID IN ("+ID_invXML+") AND (isAbort='N' OR isAbort is null)",get_TrxName());
			DB.executeUpdate("UPDATE I_ImportInvoiceXMLWPLINE SET processed = 'Y' WHERE I_ImportInvoiceXMLWPLine_ID IN ("+ID_invXMLLine+") AND (isAbort='N' OR isAbort is null)",get_TrxName());
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
		return 0;		
	}
	public int GenerateInv(MOrder order, MInOut ship)
	{
		return 0;		
	}
}	
