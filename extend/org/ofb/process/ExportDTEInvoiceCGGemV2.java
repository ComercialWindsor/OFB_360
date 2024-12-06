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

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.process.SvrProcess;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
/**
 *	Generate XML No consolidate GEMININIS 
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ExportDTEInvoiceNGem.java,v 1.2 23/06/2014 $
 */
public class ExportDTEInvoiceCGGemV2 extends SvrProcess
{
	/** Properties						*/
	private Properties 		m_ctx;	
	private int p_C_Invoice_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_C_Invoice_ID=getRecord_ID();
		m_ctx = Env.getCtx();
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		MInvoice inv=new MInvoice(m_ctx,p_C_Invoice_ID, get_TrxName());
		 log.info("doIt ()");
		String msg=CreateXML(inv);
		
		return msg;
	}	//	doIt
	
	public String CreateXML(MInvoice invoice)
    {
        MDocType doc = new MDocType(invoice.getCtx(), invoice.getC_DocTypeTarget_ID(), invoice.get_TrxName());
        if(doc.get_Value("CreateXML") == null)
            return "";
        if(!((Boolean)doc.get_Value("CreateXML")).booleanValue())
            return "";
        int typeDoc = Integer.parseInt((String)doc.get_Value("DocumentNo"));
        if(typeDoc == 0)
            return "";
        String mylog = new String();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
        	DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "DTE", null);
            document.setXmlVersion("1.0");
            Element Documento = document.createElement("Documento");
            document.getDocumentElement().appendChild(Documento);
            Documento.setAttribute("ID", (new StringBuilder()).append("DTE-").append(invoice.getDocumentNo()).toString());
            Element Encabezado = document.createElement("Encabezado");
            Documento.appendChild(Encabezado);
            Element IdDoc = document.createElement("IdDoc");
            Encabezado.appendChild(IdDoc);
            mylog = "IdDoc";
            log.info(mylog);
            Element TipoDTE = document.createElement("TipoDTE");
            org.w3c.dom.Text text = document.createTextNode(Integer.toString(typeDoc));
            TipoDTE.appendChild(text);
            IdDoc.appendChild(TipoDTE);
            Element Folio = document.createElement("Folio");
            org.w3c.dom.Text fo = document.createTextNode(invoice.getDocumentNo());
            Folio.appendChild(fo);
            IdDoc.appendChild(Folio);
            Element FchEmis = document.createElement("FchEmis");
            org.w3c.dom.Text emis = document.createTextNode(invoice.getDateInvoiced().toString().substring(0, 10));
            FchEmis.appendChild(emis);
            IdDoc.appendChild(FchEmis);
            Element FchCancel = document.createElement("FchCancel");
            org.w3c.dom.Text cancel = document.createTextNode(invoice.getDateInvoiced().toString().substring(0, 10));
            FchCancel.appendChild(cancel);
            IdDoc.appendChild(FchCancel);
            Element FchVenc = document.createElement("FchVenc");
            org.w3c.dom.Text venc = document.createTextNode(invoice.getDateInvoiced().toString().substring(0, 10));
            FchVenc.appendChild(venc);
            IdDoc.appendChild(FchVenc);
            //nuevo campo tipo de transaccion venta ininoles
            Element TpoTranVenta = document.createElement("TpoTranVenta");
            org.w3c.dom.Text TpoTranVentaTxt = document.createTextNode(invoice.get_ValueAsString("TipoTranVenta"));
            TpoTranVenta.appendChild(TpoTranVentaTxt);
            IdDoc.appendChild(TpoTranVenta);
            
            //ininoles nuevo campo termino de pago
           /* MPaymentTerm pterm = new MPaymentTerm(invoice.getCtx(), invoice.getC_PaymentTerm_ID(), invoice.get_TrxName());
            Element PayTerm = document.createElement("PayTerm");
            org.w3c.dom.Text term = document.createTextNode(pterm.getName());
            PayTerm.appendChild(term);
            IdDoc.appendChild(PayTerm);            
            //ininoles nuevo campo vendedor            
            MUser salesUser = new MUser(invoice.getCtx(), invoice.getSalesRep_ID(), invoice.get_TrxName());
            Element SalesRep = document.createElement("SalesRep");
            org.w3c.dom.Text sales = document.createTextNode(salesUser.getName());
            SalesRep.appendChild(sales);
            IdDoc.appendChild(SalesRep);
            //ininoles nuevo campo descripcion cabecera                        
            Element HDescription = document.createElement("HeaderDescription");
            org.w3c.dom.Text Hdesc = document.createTextNode(invoice.getDescription()==null?" ":invoice.getDescription());
            HDescription.appendChild(Hdesc);
            IdDoc.appendChild(HDescription);*/
            //end ininoles
                        
            Element Emisor = document.createElement("Emisor");
            Encabezado.appendChild(Emisor);
            mylog = "Emisor";
            log.info(mylog);
            MOrg company = MOrg.get(invoice.getCtx(), invoice.getAD_Org_ID());
            Element Rut = document.createElement("RUTEmisor");
            org.w3c.dom.Text rut = document.createTextNode((String)company.get_Value("Rut"));
            Rut.appendChild(rut);
            Emisor.appendChild(Rut);
            Element RznSoc = document.createElement("RznSoc");
            org.w3c.dom.Text rzn = document.createTextNode(company.getName());
            RznSoc.appendChild(rzn);
            Emisor.appendChild(RznSoc);
            Element GiroEmis = document.createElement("GiroEmis");
            org.w3c.dom.Text gi = document.createTextNode((String)company.get_Value("Giro"));
            GiroEmis.appendChild(gi);
            Emisor.appendChild(GiroEmis);
            Element Acteco = document.createElement("Acteco");
            org.w3c.dom.Text teco = document.createTextNode((String)company.get_Value("Acteco"));
            Acteco.appendChild(teco);
            Emisor.appendChild(Acteco);
            Element DirOrigen = document.createElement("DirOrigen");
            org.w3c.dom.Text dir = document.createTextNode((String)company.get_Value("Address1"));
            DirOrigen.appendChild(dir);
            Emisor.appendChild(DirOrigen);
            
            Element CmnaOrigen = document.createElement("CmnaOrigen");
            org.w3c.dom.Text com = document.createTextNode((String)company.get_Value("Comuna"));
            CmnaOrigen.appendChild(com);
            Emisor.appendChild(CmnaOrigen);
            Element CiudadOrigen = document.createElement("CiudadOrigen");
            org.w3c.dom.Text city = document.createTextNode((String)company.get_Value("City"));
            CiudadOrigen.appendChild(city);
            Emisor.appendChild(CiudadOrigen);
            mylog = "receptor";
            log.info(mylog);
            MBPartner BP = new MBPartner(invoice.getCtx(), invoice.getC_BPartner_ID(), invoice.get_TrxName());
            MBPartnerLocation bloc = new MBPartnerLocation(invoice.getCtx(), invoice.getC_BPartner_Location_ID(), invoice.get_TrxName());
            Element Receptor = document.createElement("Receptor");
            Encabezado.appendChild(Receptor);
            Element RUTRecep = document.createElement("RUTRecep");
            org.w3c.dom.Text rutc = document.createTextNode((new StringBuilder()).append(BP.getValue()).append("-").append(BP.get_ValueAsString("Digito")).toString());
            RUTRecep.appendChild(rutc);
            Receptor.appendChild(RUTRecep);
            Element RznSocRecep = document.createElement("RznSocRecep");
            org.w3c.dom.Text RznSocR = document.createTextNode(BP.getName());
            RznSocRecep.appendChild(RznSocR);
            Receptor.appendChild(RznSocRecep);
            Element GiroRecep = document.createElement("GiroRecep");
            org.w3c.dom.Text giro = document.createTextNode((String)BP.get_Value("Giro"));
            GiroRecep.appendChild(giro);
            Receptor.appendChild(GiroRecep);
            
            Element ContactoRecep = document.createElement("Contacto");
            org.w3c.dom.Text contacto = document.createTextNode(invoice.getAD_User_ID()>0?invoice.getAD_User().getName():" "); //nombre completo contacto
            ContactoRecep.appendChild(contacto);
            Receptor.appendChild(ContactoRecep);
            
            Element CorreoRecep = document.createElement("CorreoRecep");
            org.w3c.dom.Text corrRecep = document.createTextNode(invoice.getAD_User().getEMail()==null?" ":invoice.getAD_User().getEMail()); //mail del contacto
            CorreoRecep.appendChild(corrRecep);
            Receptor.appendChild(CorreoRecep);
            
            
            Element DirRecep = document.createElement("DirRecep");
            org.w3c.dom.Text dirr = document.createTextNode(bloc.getLocation(true).getAddress1());
            DirRecep.appendChild(dirr);
            Receptor.appendChild(DirRecep);
            if(bloc.getLocation(true).getAddress2()!=null && bloc.getLocation(true).getAddress2().length()>0 ){
	            Element CmnaRecep = document.createElement("CmnaRecep");
	            org.w3c.dom.Text Cmna = document.createTextNode(bloc.getLocation(true).getAddress2());
	            CmnaRecep.appendChild(Cmna);
	            Receptor.appendChild(CmnaRecep);
            }
            
            Element CiudadRecep = document.createElement("CiudadRecep");
            org.w3c.dom.Text reg = document.createTextNode(bloc.getLocation(true).getC_City_ID()>0?MCity.get(invoice.getCtx(), bloc.getLocation(true).getC_City_ID()).getName():"Santiago");
            CiudadRecep.appendChild(reg);
            Receptor.appendChild(CiudadRecep);
            
            mylog = "Totales";
            Element Totales = document.createElement("Totales");
            Encabezado.appendChild(Totales);
            BigDecimal amountex = DB.getSQLValueBD(get_TrxName(), (new StringBuilder()).append("select Round(COALESCE(SUM(il.LineNetAmt),0),0) from C_InvoiceLine il  inner join C_Tax t on (il.C_Tax_ID=t.C_Tax_ID) and t.istaxexempt='Y' and il.C_Invoice_ID=").append(invoice.getC_Invoice_ID()).toString());
            BigDecimal amountNeto = DB.getSQLValueBD(get_TrxName(), (new StringBuilder()).append("select Round(COALESCE(SUM(il.LineNetAmt),0),0) from C_InvoiceLine il  inner join C_Tax t on (il.C_Tax_ID=t.C_Tax_ID) and t.istaxexempt='N' and il.C_Invoice_ID=").append(invoice.getC_Invoice_ID()).toString());
            
            
	        Element MntNeto = document.createElement("MntNeto");
	        org.w3c.dom.Text neto = document.createTextNode(amountNeto!=null?amountNeto.toString():"0");
	        MntNeto.appendChild(neto);
	        Totales.appendChild(MntNeto);
            
            
            Element MntExe = document.createElement("MntExe");
            org.w3c.dom.Text exe = document.createTextNode(amountex != null ? amountex.toString() : "0");
            MntExe.appendChild(exe);
            Totales.appendChild(MntExe);
            
            if(amountNeto.signum()>0){
	            Element TasaIVA = document.createElement("TasaIVA");
	            org.w3c.dom.Text tiva = document.createTextNode("19");
	            TasaIVA.appendChild(tiva);
	            Totales.appendChild(TasaIVA);
	        }
	        
	            
	            Element IVA = document.createElement("IVA");
	            BigDecimal ivaamt= Env.ZERO;
	            if(amountex.intValue()!=invoice.getGrandTotal().intValue())
	            	ivaamt=invoice.getGrandTotal().subtract(invoice.getTotalLines()).setScale(0, 4);
	            org.w3c.dom.Text iva = document.createTextNode(ivaamt.toString());
	            IVA.appendChild(iva);
	            Totales.appendChild(IVA);
            
            Element MntTotal = document.createElement("MntTotal");
            org.w3c.dom.Text total = document.createTextNode(invoice.getGrandTotal().setScale(0, 4).toString());
            MntTotal.appendChild(total);
            Totales.appendChild(MntTotal);
            /*Element MontoPeriodo = document.createElement("MontoPeriodo");
            org.w3c.dom.Text mnt = document.createTextNode(getTotalLines().setScale(0, 4).toString());
            MontoPeriodo.appendChild(mnt);
            Totales.appendChild(MontoPeriodo);*/
			mylog = "detalle";
			log.info(mylog);
            
            //ininoles lineas agrupadas por producto y precio.
            
            String MySqlCG = "select MAX(il.M_PRODUCT_ID) AS M_PRODUCT_ID, MAX(il.C_CHARGE_ID) AS C_CHARGE_ID, "+
            		"MAX(il.C_TAX_ID) AS C_TAX_ID, MAX(il.DESCRIPTION2) as DESCRIPTION,MAX(il.Name) as name, " +
            		"SUM(il.QTYENTERED) AS QTYENTERED, MAX(il.PRICELIST) AS PRICELIST, SUM(il.QTYINVOICED) AS QTYINVOICED, " +
            		"MAX(il.PRICEACTUAL) AS PRICEACTUAL, SUM(il.LINENETAMT) AS LINENETAMT, il.M_Product_Name_ID, "+
            		"MAX(OTHER_INFO_3) as OTHER_INFO_3,MAX(OTHER_INFO_1) as OTHER_INFO_1 " +
            		"from RVOFB_InfoLineXML il where c_invoice_id = ? and M_Product_Name_ID > 0 " +
            		"GROUP BY M_Product_Name_ID " +
            		"UNION " +
            		"select il.M_PRODUCT_ID, il.C_CHARGE_ID, C_TAX_ID, il.DESCRIPTION,il.Name, il.QTYENTERED, " +
            		"il.PRICELIST, il.QTYINVOICED, il.PRICEACTUAL,il.LINENETAMT, il.M_Product_Name_ID, "+
            		"OTHER_INFO_3 ,OTHER_INFO_1 " +
            		"from RVOFB_InfoLineXML il where c_invoice_id = ? and M_Product_Name_ID is null";
            PreparedStatement pstmt = null;
    		ResultSet rs = null;
    		
    		try
    		{
    			pstmt = DB.prepareStatement(MySqlCG, invoice.get_TrxName());
    			pstmt.setInt(1, invoice.get_ID());			
    			pstmt.setInt(2, invoice.get_ID());
    			rs = pstmt.executeQuery();
    			int lineInvoice = 0;
    			while (rs.next())
    			{
    				MProduct produc = null;
    				MCharge charge = null;
    				lineInvoice = lineInvoice+1;
    				if (rs.getInt("M_PRODUCT_ID") > 0)
    				{
    					produc = new MProduct(invoice.getCtx(), rs.getInt("M_PRODUCT_ID"), invoice.get_TrxName());// se crea producto para usos futuros
    				}
    				if (rs.getInt("C_CHARGE_ID")>0)
    				{
    					charge = new MCharge(invoice.getCtx(), rs.getInt("C_CHARGE_ID"), invoice.get_TrxName());// se crea cargo para usos futuros
    				}
    				if(rs.getInt("M_PRODUCT_ID")==0 && rs.getInt("C_CHARGE_ID")==0 && rs.getInt("M_Product_Name_ID")==0)
                		continue;
                	
                    Element Detalle = document.createElement("Detalle");
                    Documento.appendChild(Detalle);
                    
                    MTax tax=new MTax(invoice.getCtx() ,rs.getInt("C_TAX_ID"),invoice.get_TrxName() );
                    
                    if(tax.isTaxExempt()){
                    	 Element IndEx = document.createElement("IndExe");
                         org.w3c.dom.Text lineE = document.createTextNode("1");
                         IndEx.appendChild(lineE);
                         Detalle.appendChild(IndEx);	
                    }
                  
                    Element NroLinDet = document.createElement("NroLinDet");
                    org.w3c.dom.Text line = document.createTextNode(Integer.toString(lineInvoice));
                    NroLinDet.appendChild(line);
                    Detalle.appendChild(NroLinDet);
                    
                    /*Element CdgI = document.createElement("CdgItem");
                    Documento.appendChild(CdgI);*/
                    
                    //ininoles SKUAK geminis
                    String otI3 = rs.getString("OTHER_INFO_3");
                    if (otI3 == null || otI3 == "" || otI3 == " ")
                    {
                    	otI3 = "";
                    }
                    if (rs.getString("OTHER_INFO_3") != null && otI3.length() > 0)
                    {
                    	Element TpoC = document.createElement("TpoCodigo");
                        org.w3c.dom.Text TpoCod = document.createTextNode("SKUAK");
                        TpoC.appendChild(TpoCod);
                        Detalle.appendChild(TpoC);
                        
                        Element VpoC = document.createElement("VlrCodigo");
                        org.w3c.dom.Text VpoCod = document.createTextNode(rs.getString("OTHER_INFO_3")==null?" ":rs.getString("OTHER_INFO_3"));
                        VpoC.appendChild(VpoCod);
                        Detalle.appendChild(VpoC);
                    }
                    
                  //ininoles SKUIN geminis
                    String otI1 = rs.getString("OTHER_INFO_1");
                    if (otI1 == null || otI1 == "" || otI1 == " ")
                    {
                    	otI1 = "";
                    }
                    //ininoles end    
                    
                    if (rs.getString("OTHER_INFO_1") != null && otI1.length() > 0)
                    {
                    	Element TpoC2 = document.createElement("TpoCodigo");
                        org.w3c.dom.Text TpoCod2 = document.createTextNode("SKUIN");
                        TpoC2.appendChild(TpoCod2);
                        Detalle.appendChild(TpoC2);
                        
                        Element VpoC2 = document.createElement("VlrCodigo");
                        org.w3c.dom.Text VpoCod2 = document.createTextNode(rs.getString("OTHER_INFO_1")==null?" ":rs.getString("OTHER_INFO_1"));
                        VpoC2.appendChild(VpoCod2);
                        Detalle.appendChild(VpoC2);
                    }                    
                    
                    //ininoles OFB se agrega codigo de producto a XML
                    if (rs.getInt("M_PRODUCT_ID") > 0)
                    {
                    	Element CodItem = document.createElement("CodItem");
                    	String pcod="";
                    	if(rs.getInt("M_PRODUCT_ID") > 0 )                                        	
                    		pcod=produc.getValue();                    
                    	else//si la linea posee cargo el codigo enviado es 0
                    		pcod="0";
                    	org.w3c.dom.Text CItem = document.createTextNode(pcod);
                    	CodItem.appendChild(CItem);
                    	Detalle.appendChild(CodItem);
                    }
                    //ininoles END
                    
                    Element NmbItem = document.createElement("NmbItem");
                    String pname="";
                    
                    if (rs.getInt("M_Product_Name_ID")>0)
                    	pname = rs.getString("NAME")==null?" ":rs.getString("NAME");
                    else if(rs.getInt("M_PRODUCT_ID") > 0)                    	
                    	pname=produc.getName();
                    else if (rs.getInt("C_CHARGE_ID") > 0)
                    	pname=charge.getName();                    
                    else 
                    	pname = " ";
                    org.w3c.dom.Text Item = document.createTextNode(pname);
                    NmbItem.appendChild(Item);
                    Detalle.appendChild(NmbItem);
                    
                    /*
                    Element NmbItemGen = document.createElement("NmbItemGen");
                    String pnameGen="";
                  	pnameGen=rs.getString("NAME")==null?" ":rs.getString("NAME");
                    org.w3c.dom.Text ItemGen = document.createTextNode(pnameGen);
                    NmbItemGen.appendChild(ItemGen);
                    Detalle.appendChild(NmbItemGen);
                      */                             
                    Element DscItem = document.createElement("DscItem");
                    String pdescGen = ""; 
                    pdescGen= rs.getString("DESCRIPTION")==null?" ":rs.getString("DESCRIPTION");
                    org.w3c.dom.Text desc = document.createTextNode(pdescGen);
                    DscItem.appendChild(desc);
                    Detalle.appendChild(DscItem);
                    
                    //se modifica para que muestre cantidad 1 en los productos con codigos genericos
                    Element QtyRef = document.createElement("QtyRef");
                    int cant = 0;
                    if (rs.getInt("M_Product_Name_ID")>0)
                    	cant = 1;
                    else 
                    	cant = rs.getInt("QTYENTERED");
                    org.w3c.dom.Text qty = document.createTextNode(Integer.toString(cant));                    
                    QtyRef.appendChild(qty);
                    Detalle.appendChild(QtyRef);
                    
                    Element PrcRef = document.createElement("PrcRef");
                    String plD = " ";
                    if (rs.getInt("M_Product_Name_ID")>0)
                    	plD = rs.getBigDecimal("LINENETAMT").setScale(0, 4).toString();
                    else 
                    	plD = rs.getBigDecimal("PRICELIST").toString();
                    org.w3c.dom.Text pl = document.createTextNode(plD);
                    PrcRef.appendChild(pl);
                    Detalle.appendChild(PrcRef);
                    
                    
                    Element QtyItem = document.createElement("QtyItem");
                    String qtD = " ";
                    if (rs.getInt("M_Product_Name_ID")>0)
                    	qtD = Integer.toString(1);
                    else 
                    	qtD = Integer.toString(rs.getInt("QTYINVOICED"));
                    org.w3c.dom.Text qt = document.createTextNode(qtD);
                    QtyItem.appendChild(qt);
                    Detalle.appendChild(QtyItem);
                    
                    Element PrcItem = document.createElement("PrcItem");
                    String paD = " ";
                    if (rs.getInt("M_Product_Name_ID")>0)
                    	paD = rs.getBigDecimal("LINENETAMT").setScale(0, 4).toString();
                    else 
                    	paD = rs.getBigDecimal("PRICEACTUAL").setScale(0, 4).toString();                    
                    org.w3c.dom.Text pa = document.createTextNode(paD);
                    PrcItem.appendChild(pa);
                    Detalle.appendChild(PrcItem);
                    
                    Element MontoItem = document.createElement("MontoItem");
                    org.w3c.dom.Text tl = document.createTextNode(rs.getBigDecimal("LINENETAMT").setScale(0, 4).toString());
                    MontoItem.appendChild(tl);
                    Detalle.appendChild(MontoItem);

    			}
    				
    				
    		}catch (Exception e)
    		{
    			log.log(Level.SEVERE, MySqlCG, e);
    		}//
    		//Fin detalle agrupado 
            
            mylog = "referencia";
            String tiporeferencia = new String();
            String folioreferencia  = new String();
            String fechareferencia = new String();
            int tipo_Ref =0;
            
            if(invoice.get_Value("C_RefDoc_ID") != null && ((Integer)invoice.get_Value("C_RefDoc_ID")).intValue() > 0)//referencia factura
            {
                mylog = "referencia:invoice";
                MInvoice refdoc = new MInvoice(invoice.getCtx(), ((Integer)invoice.get_Value("C_RefDoc_ID")).intValue(), invoice.get_TrxName());
                MDocType Refdoctype = new MDocType(invoice.getCtx(), refdoc.getC_DocType_ID(), invoice.get_TrxName());
                tiporeferencia = (String) Refdoctype.get_Value("DocumentNo");
                folioreferencia = (String) refdoc.getDocumentNo();
                fechareferencia = refdoc.getDateInvoiced().toString().substring(0, 10);
                tipo_Ref = 1; //factura
            } 
            
            if(invoice.getPOReference() != null && invoice.getPOReference().length() > 0)//referencia orden
            {
            	 mylog = "referencia:order";
            	 //MOrder refdoc = new MOrder(invoice.getCtx(), ((Integer)get_Value("C_RefOrder_ID")).intValue(), invoice.get_TrxName()); 
            	 tiporeferencia = "801";
                 folioreferencia = invoice.getPOReference();
                 fechareferencia = invoice.getDateOrdered().toString().substring(0, 10);
            	 tipo_Ref = 2; //Orden
            }
            
            if(invoice.get_Value("C_RefInOut_ID") != null && ((Integer)invoice.get_Value("C_RefInOut_ID")).intValue() > 0)//referencia despacho
            {
            	 mylog = "referencia:despacho";
            	 MInOut refdoc = new MInOut(invoice.getCtx(), ((Integer)invoice.get_Value("C_RefInOut_ID")).intValue(), invoice.get_TrxName()); 
            	 tiporeferencia = "52";
                 folioreferencia = (String) refdoc.getDocumentNo();
                 fechareferencia = refdoc.getMovementDate().toString().substring(0, 10);
            	 tipo_Ref = 3; //despacho
            }
            
            if(tipo_Ref>0){
                Element Referencia = document.createElement("Referencia");
                Documento.appendChild(Referencia);
                Element NroLinRef = document.createElement("NroLinRef");
                org.w3c.dom.Text Nro = document.createTextNode("1");
                NroLinRef.appendChild(Nro);
                Referencia.appendChild(NroLinRef);
                Element TpoDocRef = document.createElement("TpoDocRef");
                org.w3c.dom.Text tpo = document.createTextNode(tiporeferencia);
                TpoDocRef.appendChild(tpo);
                Referencia.appendChild(TpoDocRef);
                Element FolioRef = document.createElement("FolioRef");
                org.w3c.dom.Text ref = document.createTextNode(folioreferencia);
                FolioRef.appendChild(ref);
                Referencia.appendChild(FolioRef);              
                Element FchRef = document.createElement("FchRef");
                org.w3c.dom.Text fchref = document.createTextNode(fechareferencia);
                FchRef.appendChild(fchref);
                Referencia.appendChild(FchRef);
                Element CodRef = document.createElement("CodRef");
                org.w3c.dom.Text codref = document.createTextNode(invoice.get_ValueAsString("CodRef"));
                CodRef.appendChild(codref);
                Referencia.appendChild(CodRef);
                
                //nuevos campos de referencia geminis. ininoles
                String sqlDN = "Select o.DocumentNo FROM C_Order o INNER JOIN C_Invoice i ON (o.C_Order_ID = i.C_Order_ID) WHERE i.C_Invoice_ID= ?";
                String docNoRef = DB.getSQLValueString(get_TrxName(), sqlDN, invoice.get_ID()); 
                
                if (docNoRef != null)
                {
                	Element Referencia2 = document.createElement("Referencia");
                    Documento.appendChild(Referencia2);
                    Element NroLinRef2 = document.createElement("NroLinRef");
                    org.w3c.dom.Text Nro2 = document.createTextNode("2");
                    NroLinRef2.appendChild(Nro2);
                    Referencia2.appendChild(NroLinRef2);
                    
                    Element TpoDocRef2 = document.createElement("TpoDocRef");
                    org.w3c.dom.Text tpo2 = document.createTextNode("HES");
                    TpoDocRef2.appendChild(tpo2);
                    Referencia2.appendChild(TpoDocRef2);
                    
                    Element FolioRef2 = document.createElement("FolioRef");
                    org.w3c.dom.Text ref2 = document.createTextNode(docNoRef);
                    FolioRef2.appendChild(ref2);
                    Referencia2.appendChild(FolioRef2);
                                      
                    Element FchRef2 = document.createElement("FchRef");
                    org.w3c.dom.Text fchref2 = document.createTextNode(fechareferencia);
                    FchRef2.appendChild(fchref2);
                    Referencia2.appendChild(FchRef2);
                    
                    Element CodRef2 = document.createElement("CodRef");
                    org.w3c.dom.Text codref2 = document.createTextNode(invoice.get_ValueAsString("CodRef"));
                    CodRef2.appendChild(codref2);
                    Referencia2.appendChild(CodRef2);
                }
                //end ininoles                
            }
            //fin referencia
            mylog = "firma";
            log.info(mylog);
            Element Firma = document.createElement("TmstFirma");
            Timestamp today = new Timestamp(TimeUtil.getToday().getTimeInMillis());
            org.w3c.dom.Text Ftext = document.createTextNode((new StringBuilder()).append(today.toString().substring(0, 10)).append("T").append(today.toString().substring(11, 19)).toString());
            Firma.appendChild(Ftext);
            Documento.appendChild(Firma);
            mylog = "archivo";
            String ExportDir = (String)company.get_Value("ExportDir");
            ExportDir = ExportDir.replace("\\", "/");
            javax.xml.transform.Source source = new DOMSource(document);
            javax.xml.transform.Result result = new StreamResult(new File(ExportDir, (new StringBuilder()).append(invoice.getDocumentNo()).append(".xml").toString()));
            javax.xml.transform.Result console = new StreamResult(System.out);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("encoding", "ISO-8859-1");
            transformer.transform(source, result);
            transformer.transform(source, console);
        }
        catch(Exception e)
        {
            log.severe((new StringBuilder()).append("CreateXML: ").append(mylog).append("--").append(e.getMessage()).toString());
            return (new StringBuilder()).append("CreateXML: ").append(mylog).append("--").append(e.getMessage()).toString();
        }
        return "XML Geminis Generated";
    }	
}	//	InvoiceCreateInOut
