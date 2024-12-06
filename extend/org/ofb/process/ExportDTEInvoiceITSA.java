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
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPBinding;

import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.process.SvrProcess;
import org.ofb.model.OFBForward;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileInputStream;
import java.io.InputStream;
 


import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.codec.binary.Base64; 

/**
 *	Generate XML Consolidated from Invoice (Generic) 
 *	
 *  @author mfrojas
 *  @version $Id: ExportDTEInvoiceCG.java,v 1.2 19/05/2011 $
 */
public class ExportDTEInvoiceITSA extends SvrProcess
{	
	/** Properties						*/
	private Properties 		m_ctx;	
	private int p_C_Invoice_ID = 0;
	public String urlPdf = "";
	
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
		MInvoice inv=new MInvoice(m_ctx,p_C_Invoice_ID,get_TrxName());
		String msg=CreateXMLCGITSA(inv);
		
		return msg;
	}	//	doIt
	
	public String CreateXMLCGITSA(MInvoice invoice)
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
	            Document document = implementation.createDocument(null, "Documento",null);
	            document.setXmlVersion("1.0");
	            document.setTextContent("text/xml");
	            
	            Attr atr = document.createAttribute("xmlns");
	            atr.setValue("http://www.sii.cl/SiiDte");  
	            
	            //Element Documento = document.createElement("Documento");
	            //document.getDocumentElement().appendChild(Documento);
	           // Documento.setAttribute("ID", (new StringBuilder()).append("DTE-").append((String)doc.get_Value("DocumentNo")).toString());
	           // Documento.setAttribute("ID", (new StringBuilder()).append("T").append((String)doc.get_Value("DocumentNo")).toString().concat("F").concat((String)invoice.get_Value("Documentno")));
	            
	            Element Encabezado = document.createElement("Encabezado");
	            document.getDocumentElement().appendChild(Encabezado);

	            //Documento.appendChild(Encabezado);
	            Element IdDoc = document.createElement("IdDoc");
	            Encabezado.appendChild(IdDoc);
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
	            
	            log.config("Fecha Emision "+invoice.getDateInvoiced().toString().substring(0, 10));
	            FchEmis.appendChild(emis);
	            IdDoc.appendChild(FchEmis);


	            //@mfrojas validar forma de pago con fecha de pago y fecha de facturación
	            
	            String fmapagodocumento = null;
	            
	            if(invoice.getDateInvoiced().toString().substring(0, 10).compareToIgnoreCase(invoice.getDatePrinted().toString().substring(0, 10)) == 0)
	            	fmapagodocumento = "1";
	            else
	            	fmapagodocumento = "2";

	            
	            Element FmaPago = document.createElement("FmaPago");
	            org.w3c.dom.Text fma = document.createTextNode(fmapagodocumento);
	            FmaPago.appendChild(fma);
	            IdDoc.appendChild(FmaPago);

	            //@mfrojas se quita FchCancel
	            
	            /*Element FchCancel = document.createElement("FchCancel");
	            org.w3c.dom.Text cancel = document.createTextNode(invoice.getDateInvoiced().toString().substring(0, 10));
	            log.config("Fecha Cancel "+invoice.getDateInvoiced().toString().substring(0, 10));
	            FchCancel.appendChild(cancel);
	            IdDoc.appendChild(FchCancel);
	            */
	            Timestamp datevenc = invoice.getDatePrinted();
	            String datevenc2 = null;
	            log.config("Fecha Vencimiento: "+datevenc);
	            if (datevenc == null)
	            {
	            	datevenc2 = "invoiced";
	            }
	            else
	            	datevenc2 = "printed";
	            log.config("");
	            //datevenc2 = datevenc2.trim();
	            log.config("datevenc2: "+datevenc2);
	            if (datevenc2 == "printed")
	            	datevenc2 = invoice.getDatePrinted().toString().substring(0, 10);
	            else
	            	datevenc2 = invoice.getDateInvoiced().toString().substring(0,10);
	            
	            Element FchVenc = document.createElement("FchVenc");
	            log.config("Fecha Vencimiento: "+datevenc2);
	            org.w3c.dom.Text venc = document.createTextNode(datevenc2);
	            FchVenc.appendChild(venc);
	            IdDoc.appendChild(FchVenc);
	            
	            //ininoles nuevo campo termino de pago
/*	            MPaymentTerm pterm = new MPaymentTerm(invoice.getCtx(), invoice.getC_PaymentTerm_ID(), invoice.get_TrxName());
	            log.config("ID"+invoice.getC_PaymentTerm_ID());

	            Element PayTerm = document.createElement("PayTerm");
	            org.w3c.dom.Text term = document.createTextNode(pterm.getName());
	            PayTerm.appendChild(term);
	            IdDoc.appendChild(PayTerm);   
	            */         
	          //mfrojas nuevo campo termino de pago
	            
	            //ininoles nuevo campo vendedor 
	            
	            //@mfrojas se quita campo salesrep y header description
	            
	            /*
	            MUser salesUser = new MUser(invoice.getCtx(), invoice.getSalesRep_ID(),invoice.get_TrxName());
	            Element SalesRep = document.createElement("SalesRep");
	            org.w3c.dom.Text sales = document.createTextNode(salesUser.getName());
	            log.config("Sales Rep  "+salesUser.getName());
	            SalesRep.appendChild(sales);
	            IdDoc.appendChild(SalesRep);
	            
	            
	            //ininoles nuevo campo descripcion cabecera                        
	            Element HDescription = document.createElement("HeaderDescription");
	            org.w3c.dom.Text Hdesc = document.createTextNode(invoice.getDescription()==null?" ":invoice.getDescription());
	            log.config("desc "+invoice.getDescription());
	            HDescription.appendChild(Hdesc);
	            IdDoc.appendChild(HDescription);
	            //end ininoles
	            */            
	           
	            Element Emisor = document.createElement("Emisor");
	            Encabezado.appendChild(Emisor);
	            mylog = "Emisor";
	            MOrg company = MOrg.get(getCtx(), invoice.getAD_Org_ID());
	            Element Rut = document.createElement("RUTEmisor");
	            org.w3c.dom.Text rut = document.createTextNode((String)company.get_Value("Rut"));
	            log.config("Rut Emisor: "+(String)company.get_Value("Rut"));
	            Rut.appendChild(rut);
	            Emisor.appendChild(Rut);
	            //ininoles validacion nuevo nombre razon social
	            String nameRzn = company.getDescription();
	            if (nameRzn == null)
	            {
	            	nameRzn = " ";
	            }
	            nameRzn = nameRzn.trim();
	            if (nameRzn.length() < 2)
	            	nameRzn = company.getName();
	            //ininoles end            
	            Element RznSoc = document.createElement("RznSoc");
	            org.w3c.dom.Text rzn = document.createTextNode(nameRzn);
	            RznSoc.appendChild(rzn);
	            Emisor.appendChild(RznSoc);
	            
	            Element GiroEmis = document.createElement("GiroEmis");
	            org.w3c.dom.Text gi = document.createTextNode((String)company.get_Value("Giro"));
	            GiroEmis.appendChild(gi);
	            Emisor.appendChild(GiroEmis);
	            Element Acteco = document.createElement("Acteco");
	            org.w3c.dom.Text teco = document.createTextNode((String)company.get_Value("Acteco"));
	            log.config("ACTECO: "+(String)company.get_Value("Acteco"));
	            Acteco.appendChild(teco);
	            Emisor.appendChild(Acteco);
	            Element DirOrigen = document.createElement("DirOrigen");
	            org.w3c.dom.Text dir = document.createTextNode((String)company.get_Value("Address1"));
	            DirOrigen.appendChild(dir);
	            Emisor.appendChild(DirOrigen);
	            
	            Element CmnaOrigen = document.createElement("CmnaOrigen");
	            org.w3c.dom.Text com = document.createTextNode((String)company.get_Value("Comuna"));
	            log.config("COMUNA "+(String)company.get_Value("Comuna"));
	            CmnaOrigen.appendChild(com);
	            Emisor.appendChild(CmnaOrigen);
	            Element CiudadOrigen = document.createElement("CiudadOrigen");
	            
	            org.w3c.dom.Text city = document.createTextNode((String)company.get_Value("City"));
	            log.config("COMUNA "+(String)company.get_Value("City"));
	            CiudadOrigen.appendChild(city);
	            Emisor.appendChild(CiudadOrigen);
	            mylog = "receptor";
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
	            
	            //@mfrojas se quita este campo
	            /*
	            Element CorreoRecep = document.createElement("CorreoRecep");
	            org.w3c.dom.Text corrRecep = document.createTextNode(invoice.getAD_User().getEMail()==null?" ":invoice.getAD_User().getEMail()); //mail del contacto
	            CorreoRecep.appendChild(corrRecep);
	            Receptor.appendChild(CorreoRecep);
	            */
	            
	            Element DirRecep = document.createElement("DirRecep");
	            org.w3c.dom.Text dirr = document.createTextNode(bloc.getLocation(true).getAddress1());
	            DirRecep.appendChild(dirr);
	            Receptor.appendChild(DirRecep);
	            /*if(bloc.getLocation(true).getAddress2()!=null && bloc.getLocation(true).getAddress2().length()>0 ){
		            Element CmnaRecep = document.createElement("CmnaRecep");
		            org.w3c.dom.Text Cmna = document.createTextNode(bloc.getLocation(true).getAddress2());
		            CmnaRecep.appendChild(Cmna);
		            Receptor.appendChild(CmnaRecep);
	            }*/
	            String ciudad2 = "";
                ciudad2 = DB.getSQLValueString(invoice.get_TrxName(), "SELECT city from c_location where c_location_id = ?", bloc.getLocation(true).get_ID());
                log.config("CIUDAD2"+ciudad2);
                log.config("ID "+bloc.get_ID());
                if(ciudad2 == null)
                	ciudad2 = "Santiago";

	            
	            Element CmnaRecep = document.createElement("CmnaRecep");
	            org.w3c.dom.Text reg2 = document.createTextNode(bloc.getLocation(true).getC_City_ID()>0?MCity.get(getCtx(), bloc.getLocation(true).getC_City_ID()).getName():ciudad2);
	            CmnaRecep.appendChild(reg2);
	            Receptor.appendChild(CmnaRecep);
	            
	            Element CiudadRecep = document.createElement("CiudadRecep");
	            org.w3c.dom.Text reg = document.createTextNode(bloc.getLocation(true).getAddress2()!=null?bloc.getLocation(true).getAddress2():"");
	            CiudadRecep.appendChild(reg);
	            Receptor.appendChild(CiudadRecep);
	            
	            
	            //mfrojas transportista

	            String validaciontransporte = invoice.get_ValueAsString("IsCarrier");
	            log.config("validacion "+validaciontransporte);
	            if(validaciontransporte.equals("true"))
	            {	
	            mylog="Transporte";
	            Element Transporte = document.createElement("Transporte");
	            
	            Encabezado.appendChild(Transporte);
	            Element Patente = document.createElement("Patente");
	            org.w3c.dom.Text patent = document.createTextNode(invoice.get_ValueAsString("Patent"));
	            Patente.appendChild(patent);
	            Transporte.appendChild(Patente);
	            
	            Element RUTTrans = document.createElement("RUTTrans");
	            org.w3c.dom.Text ruttrans = document.createTextNode(invoice.get_ValueAsString("CarrierID"));
	            RUTTrans.appendChild(ruttrans);
	            Transporte.appendChild(RUTTrans);
	            Element DirDest = document.createElement("DirDest");
	            org.w3c.dom.Text dirde = document.createTextNode(invoice.get_ValueAsString("Address4"));
	            log.config("rut transportista : "+invoice.get_ValueAsString("Address4"));
	            
	            log.config("Fecha Emision "+invoice.getDateInvoiced().toString().substring(0, 10));
	            DirDest.appendChild(dirde);
	            Transporte.appendChild(DirDest);
	            Element CmnaDest = document.createElement("CmnaDest");
	            org.w3c.dom.Text cmna = document.createTextNode(invoice.get_ValueAsString("DestinationCity1"));
	            log.config("Comuna destino "+invoice.get_ValueAsString("DestinationCity1"));
	            CmnaDest.appendChild(cmna);
	            Transporte.appendChild(CmnaDest);
	
	            
	            Element CiudadDest = document.createElement("CiudadDest");
	            log.config("Fecha Vencimiento: "+datevenc2);
	            org.w3c.dom.Text ciudad = document.createTextNode(invoice.get_ValueAsString("DestinationCity2"));
	            CiudadDest.appendChild(ciudad);
	            Transporte.appendChild(CiudadDest);
	            } 
	            //mfrojas transportista
	            
	            
	            
	            mylog = "Totales";
	            Element Totales = document.createElement("Totales");
	            Encabezado.appendChild(Totales);
	            BigDecimal amountex = DB.getSQLValueBD(invoice.get_TrxName(), (new StringBuilder()).append("select Round(COALESCE(SUM(il.LineNetAmt),0),0) from C_InvoiceLine il  inner join C_Tax t on (il.C_Tax_ID=t.C_Tax_ID) and t.istaxexempt='Y' and il.C_Invoice_ID=").append(invoice.getC_Invoice_ID()).toString());
	            BigDecimal amountNeto = DB.getSQLValueBD(invoice.get_TrxName(), (new StringBuilder()).append("select Round(COALESCE(SUM(il.LineNetAmt),0),0) from C_InvoiceLine il  inner join C_Tax t on (il.C_Tax_ID=t.C_Tax_ID) and t.istaxexempt='N' and il.C_Invoice_ID=").append(invoice.getC_Invoice_ID()).toString());
	            
	            
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

	            
	            
	            mylog = "detalle";
	            MInvoiceLine iLines[] = invoice.getLines(false);
	            for(int i = 0; i < iLines.length; i++)
	            {
	            	MInvoiceLine iLine = iLines[i];
	            	if(iLine.getM_Product_ID()==0 && iLine.getC_Charge_ID()==0)
	            		continue;
	            	
	                Element Detalle = document.createElement("Detalle");
		            document.getDocumentElement().appendChild(Detalle);

	                //Documento.appendChild(Detalle);
	                
	                MTax tax=new MTax(invoice.getCtx() ,iLine.getC_Tax_ID(),invoice.get_TrxName() );
	                
	                if(tax.isTaxExempt()){
	                	 Element IndEx = document.createElement("IndExe");
	                     org.w3c.dom.Text lineE = document.createTextNode("1");
	                     IndEx.appendChild(lineE);
	                     Detalle.appendChild(IndEx);	
	                }
	              
	                Element NroLinDet = document.createElement("NroLinDet");
	                org.w3c.dom.Text line = document.createTextNode(Integer.toString(iLine.getLine() / 10));
	                NroLinDet.appendChild(line);
	                Detalle.appendChild(NroLinDet);
	                
	                //@mfrojas se agrega tag de codigo de item
	                
	                Element cdgitem = document.createElement("CdgItem");
	                document.getDocumentElement().appendChild(cdgitem);
	                
	                Element tpocodigo = document.createElement("TpoCodigo");
	                org.w3c.dom.Text dd = document.createTextNode("");
	                tpocodigo.appendChild(dd);
	                cdgitem.appendChild(tpocodigo);
	                
	                String tpocodigostring = "";
	                int attributesetinstanceid = iLine.getM_AttributeSetInstance_ID();
	                if (attributesetinstanceid != 0)
	                {
	                	tpocodigostring = DB.getSQLValueString(invoice.get_TrxName(), "SELECT value from m_attributeinstance where m_Attribute_id = 1000014 and m_attributesetinstance_id = "+iLine.getM_AttributeSetInstance_ID());
	                }
	                else
	                {
	                	
	                	if(iLine.getProduct()!=null)
	                		tpocodigostring = DB.getSQLValueString(invoice.get_TrxName(), "SELECT value from m_product where m_product_id = "+iLine.getM_Product_ID());
	                	else
	                		tpocodigostring = DB.getSQLValueString(invoice.get_TrxName(), "SELECT name from c_charge where c_charge_id = "+iLine.getC_Charge_ID());
	                	
	                }
	                
	                log.config("TpoCodigo "+tpocodigostring);

	                Element vlrcodigo = document.createElement("VlrCodigo");
	                org.w3c.dom.Text vlr = document.createTextNode(tpocodigostring);
	                vlrcodigo.appendChild(vlr);
	                cdgitem.appendChild(vlrcodigo);
	                
	                
	                Detalle.appendChild(cdgitem);
	                
	                //@mfrojas end
	                
	                
	                Element NmbItem = document.createElement("NmbItem");
	                String pname="";
	                if(iLine.getProduct()!=null )
	                	pname=iLine.getProduct().getName();
	                else
	                	pname=iLine.getC_Charge().getName();
	                log.config("obane "+pname);
	                org.w3c.dom.Text Item = document.createTextNode((String)pname);
	                NmbItem.appendChild(Item);
	                Detalle.appendChild(NmbItem);
	                //Validacion de string de descripcion
	                
	                String dscitem = "";
	                /*if (attributesetinstanceid != 0)
	                {
	                	dscitem = DB.getSQLValueString(invoice.get_TrxName(), "SELECT value from m_attributeinstance where m_Attribute_id = 1000019 and m_attributesetinstance_id = "+iLine.getM_AttributeSetInstance_ID());
	                }
	                else
	                {
	                	if(iLine.getDescription()==null)
	                		dscitem = " ";
	                	else
	                	{
					*/
	                		if(iLine.getDescription().length() < 171)
	                			dscitem = iLine.getDescription();
	                		else
	                			dscitem = iLine.getDescription().substring(0, 170);
	                		
	                //	}
	                //}
	
	                log.config("descitem: "+dscitem);
	                
	                //dscitem = iLine.getQtyInvoiced().toString().concat(" ").concat(iLine.getC_UOM().getName()).concat(" ").concat(dscitem);
	                
	                Element DscItem = document.createElement("DscItem");
	                //org.w3c.dom.Text desc = document.createTextNode(iLine.getDescription()==null?" ":iLine.getDescription());
	                org.w3c.dom.Text desc = document.createTextNode(dscitem);
	                DscItem.appendChild(desc);
	                Detalle.appendChild(DscItem);
	             /*   
	                Element QtyRef = document.createElement("QtyRef");
	                org.w3c.dom.Text qty = document.createTextNode(iLine.getQtyEntered().toString());
	                QtyRef.appendChild(qty);
	                Detalle.appendChild(QtyRef);
	                
	                */
/*	                
	                Element PrcRef = document.createElement("PrcRef");
	                org.w3c.dom.Text pl = document.createTextNode(iLine.getPriceList().toString());
	                PrcRef.appendChild(pl);
	                Detalle.appendChild(PrcRef);
*/	                
	                Element QtyItem = document.createElement("QtyItem");
	                org.w3c.dom.Text qt = document.createTextNode(iLine.getQtyInvoiced().setScale(2, RoundingMode.HALF_UP).toString());
	                //org.w3c.dom.Text qt = document.createTextNode(iLine.getQtyInvoiced().toString());

	                QtyItem.appendChild(qt);
	                Detalle.appendChild(QtyItem);
	                //mfrojas unidad de medida
	                

	                
	                String sqluom = DB.getSQLValueString(invoice.get_TrxName(), "SELECT UOMSymbol from c_uom where c_uom_id in (select c_uom_id from c_invoiceline where c_invoiceline_id = ?)", iLine.get_ID());
	                
	                Element UnmdItem = document.createElement("UnmdItem");
	                org.w3c.dom.Text Unn = document.createTextNode(sqluom);
	                UnmdItem.appendChild(Unn);
	                Detalle.appendChild(UnmdItem);
	 
	                
	                BigDecimal monto = iLine.getPriceActual();
	                BigDecimal zerovalue = new BigDecimal ("0");
	                String value1 = "0";
	                
	                int res = monto.compareTo(zerovalue);
	                log.config("valor resultado = "+res);
	                if(res == -1)
	                {

	                }
	                else
	                {
	                	BigDecimal preciounitario = Env.ZERO;
	                	if(iLine.getPriceList() == Env.ZERO)
	                		preciounitario = iLine.getPriceActual();
	                	else
	                		preciounitario = iLine.getPriceList();
	                	
		                Element PrcItem = document.createElement("PrcItem");
		                org.w3c.dom.Text pa = document.createTextNode(preciounitario.setScale(2, RoundingMode.HALF_UP).toString());
		                //org.w3c.dom.Text pa = document.createTextNode(preciounitario.toString());

	                	PrcItem.appendChild(pa);
	                	Detalle.appendChild(PrcItem);

	                }
	                
	                //@mfrojas revisar si existe descuento
	                
	                if(iLine.getPriceList().compareTo(iLine.getPriceActual()) == 1)
	                {
	                	BigDecimal porcentajedescuento = Env.ZERO;
	                	BigDecimal porcentajedescuentoreal = Env.ZERO;
	                	porcentajedescuento = (iLine.getPriceActual().multiply(Env.ONEHUNDRED));
	                	porcentajedescuento = porcentajedescuento.divide(iLine.getPriceList(),2,RoundingMode.HALF_EVEN);
	                	porcentajedescuentoreal = Env.ONEHUNDRED.subtract(porcentajedescuento);
	                	
	                	log.config("porcentajedescuentoreal "+porcentajedescuentoreal);
	                	
	                	Element DescuentoPct = document.createElement("DescuentoPct");
		                org.w3c.dom.Text pa1 = document.createTextNode(porcentajedescuentoreal.toString());
	                	DescuentoPct.appendChild(pa1);
	                	Detalle.appendChild(DescuentoPct);
	                	
	                	BigDecimal descuentomonto = iLine.getPriceList().subtract(iLine.getPriceActual());
	                	descuentomonto = iLine.getQtyInvoiced().multiply(descuentomonto).setScale(0, RoundingMode.HALF_UP);
	                	
	                	log.config("decuento por monto "+descuentomonto);
	                	Element DescuentoMonto = document.createElement("DescuentoMonto");
		                org.w3c.dom.Text pa2 = document.createTextNode(descuentomonto.toString());
	                	DescuentoMonto.appendChild(pa2);
	                	Detalle.appendChild(DescuentoMonto);
	                	
	                	
	                	
	                }
	                
	                
	                BigDecimal monto2 = iLine.getLineNetAmt();
	                
	                int res2 = monto2.compareTo(zerovalue);
	                if(res2 == -1)
	                {
	                	Element MontoItem = document.createElement("MontoItem");
	                	org.w3c.dom.Text t1 = document.createTextNode(value1);
	                	MontoItem.appendChild(t1);
	                	Detalle.appendChild(MontoItem);
	                }
	                else
	                {
	                	
	                
	                	Element MontoItem = document.createElement("MontoItem");
	                	org.w3c.dom.Text tl = document.createTextNode(iLine.getLineNetAmt().setScale(0, 4).toString());
	                	MontoItem.appendChild(tl);
	                	Detalle.appendChild(MontoItem);
	                }
	            }
                
	            
	            //@mfrojas Descuentos
	            
	            String sqldescuentos = "select sum(linenetamt) from c_invoiceline where linenetamt < 0 and c_invoice_id = "+invoice.get_ID();
	            log.config("SQL DE FACTURA = "+sqldescuentos);
	            
	            int descuento = DB.getSQLValue(get_TrxName(), sqldescuentos);
	            log.config("descuento = "+descuento);
	            	
	            if(descuento < 0)
	            {
	            	descuento = descuento * -1;
	            	mylog = "DscRcgGlobal";
	            	Element DscRcgGlobal = document.createElement("DscRcgGlobal");
		            document.getDocumentElement().appendChild(DscRcgGlobal);

	            	//Documento.appendChild(DscRcgGlobal);
	            	
	            	Element NroLinDR = document.createElement("NroLinDR");
	            	org.w3c.dom.Text Nro = document.createTextNode("1");
	            	NroLinDR.appendChild(Nro);
	            	DscRcgGlobal.appendChild(NroLinDR);
	            	
	            	Element TpoMov = document.createElement("TpoMov");
	            	org.w3c.dom.Text Nro2 = document.createTextNode("D");
	            	TpoMov.appendChild(Nro2);
	            	DscRcgGlobal.appendChild(TpoMov);
	            	
	            	Element GlosaDR = document.createElement("GlosaDR");
	            	org.w3c.dom.Text Nro3 = document.createTextNode("Descuento");
	            	GlosaDR.appendChild(Nro3);
	            	DscRcgGlobal.appendChild(GlosaDR);
	            	
	            	Element TpoValor = document.createElement("TpoValor");
	            	org.w3c.dom.Text Nro4 = document.createTextNode("$");
	            	TpoValor.appendChild(Nro4);
	            	DscRcgGlobal.appendChild(TpoValor);
	            	
	            	Element ValorDR = document.createElement("ValorDR");
	            	org.w3c.dom.Text Nro5 = document.createTextNode(String.valueOf(descuento));
	            	ValorDR.appendChild(Nro5);
	            	DscRcgGlobal.appendChild(ValorDR);

	            	
	            	
	            	
	            }
	           //@mfrojas referencias documentos adjuntos (hasta 5)
	            
	            String attachment1 = invoice.get_ValueAsString("Attachment1");
	            log.config("attachment 1 = "+attachment1);
	            if(attachment1 == "true")
	            {	
	            	mylog = "Referencia";
	            	
	            	Element Referencia = document.createElement("Referencia");
		            document.getDocumentElement().appendChild(Referencia);
	            	//Documento.appendChild(Referencia);
	            	Element NroLinRef = document.createElement("NroLinRef");
	            	org.w3c.dom.Text Nro = document.createTextNode("1");
	            	NroLinRef.appendChild(Nro);
	            	Referencia.appendChild(NroLinRef);
	            	Element TpoDocRef = document.createElement("TpoDocRef");
	            	org.w3c.dom.Text tpo = document.createTextNode(invoice.get_ValueAsString("RefDocType1"));
	            	TpoDocRef.appendChild(tpo);
	            	Referencia.appendChild(TpoDocRef);
	            	Element FolioRef = document.createElement("FolioRef");
	            	org.w3c.dom.Text ref = document.createTextNode(invoice.get_ValueAsString("FolioNumber1"));
	            	FolioRef.appendChild(ref);
	            	Referencia.appendChild(FolioRef);

	            	Element FchRef = document.createElement("FchRef");
	            	org.w3c.dom.Text fchref = document.createTextNode(invoice.get_ValueAsString("DateRef1").toString().substring(0, 10));
	            	FchRef.appendChild(fchref);
	            	Referencia.appendChild(FchRef);
	            	Element RazonRef = document.createElement("RazonRef");
	            	org.w3c.dom.Text codref = document.createTextNode(invoice.get_ValueAsString("RefReason1"));
	            	RazonRef.appendChild(codref);
	            	Referencia.appendChild(RazonRef);
	            	
	            }
	            
	            String attachment2 = invoice.get_ValueAsString("Attachment2");
	            log.config("attachment 2 = "+attachment2);
	            if(attachment2 == "true")
	            {	
	            	mylog = "Referencia";
	            	
	            	Element Referencia = document.createElement("Referencia");
	            	document.getDocumentElement().appendChild(Referencia);
	            	//Documento.appendChild(Referencia);
	            	Element NroLinRef = document.createElement("NroLinRef");
	            	org.w3c.dom.Text Nro = document.createTextNode("2");
	            	NroLinRef.appendChild(Nro);
	            	Referencia.appendChild(NroLinRef);
	            	Element TpoDocRef = document.createElement("TpoDocRef");
	            	org.w3c.dom.Text tpo = document.createTextNode(invoice.get_ValueAsString("RefDocType2"));
	            	TpoDocRef.appendChild(tpo);
	            	Referencia.appendChild(TpoDocRef);
	            	Element FolioRef = document.createElement("FolioRef");
	            	org.w3c.dom.Text ref = document.createTextNode(invoice.get_ValueAsString("FolioNumber2"));
	            	FolioRef.appendChild(ref);
	            	Referencia.appendChild(FolioRef);

	            	Element FchRef = document.createElement("FchRef");
	            	org.w3c.dom.Text fchref = document.createTextNode(invoice.get_ValueAsString("DateRef2").toString().substring(0, 10));
	            	FchRef.appendChild(fchref);
	            	Referencia.appendChild(FchRef);
	            	Element RazonRef = document.createElement("RazonRef");
	            	org.w3c.dom.Text codref = document.createTextNode(invoice.get_ValueAsString("RefReason2"));
	            	RazonRef.appendChild(codref);
	            	Referencia.appendChild(RazonRef);
	            }	
	            	
	            String attachment3 = invoice.get_ValueAsString("Attachment3");
	            log.config("attachment 3 = "+attachment3);
	            if(attachment3 == "true")
	            {	
	            	mylog = "Referencia";
	            	
	            	Element Referencia = document.createElement("Referencia");
	            	document.getDocumentElement().appendChild(Referencia);
	            	//Documento.appendChild(Referencia);
	            	Element NroLinRef = document.createElement("NroLinRef");
	            	org.w3c.dom.Text Nro = document.createTextNode("3");
	            	NroLinRef.appendChild(Nro);
	            	Referencia.appendChild(NroLinRef);
	            	Element TpoDocRef = document.createElement("TpoDocRef");
	            	org.w3c.dom.Text tpo = document.createTextNode(invoice.get_ValueAsString("RefDocType3"));
	            	TpoDocRef.appendChild(tpo);
	            	Referencia.appendChild(TpoDocRef);
	            	Element FolioRef = document.createElement("FolioRef");
	            	org.w3c.dom.Text ref = document.createTextNode(invoice.get_ValueAsString("FolioNumber3"));
	            	FolioRef.appendChild(ref);
	            	Referencia.appendChild(FolioRef);

	            	Element FchRef = document.createElement("FchRef");
	            	org.w3c.dom.Text fchref = document.createTextNode(invoice.get_ValueAsString("DateRef3").toString().substring(0, 10));
	            	FchRef.appendChild(fchref);
	            	Referencia.appendChild(FchRef);
	            	Element RazonRef = document.createElement("RazonRef");
	            	org.w3c.dom.Text codref = document.createTextNode(invoice.get_ValueAsString("RefReason3"));
	            	RazonRef.appendChild(codref);
	            	Referencia.appendChild(RazonRef);
	            }	
	            String attachment4 = invoice.get_ValueAsString("Attachment4");
	            
	            log.config("attachment 4 = "+attachment4);
	            if(attachment4 == "true")
	            {	
	            	mylog = "Referencia";
	            	
	            	Element Referencia = document.createElement("Referencia");
	            	document.getDocumentElement().appendChild(Referencia);
	            	//Documento.appendChild(Referencia);
	            	Element NroLinRef = document.createElement("NroLinRef");
	            	org.w3c.dom.Text Nro = document.createTextNode("4");
	            	NroLinRef.appendChild(Nro);
	            	Referencia.appendChild(NroLinRef);
	            	Element TpoDocRef = document.createElement("TpoDocRef");
	            	org.w3c.dom.Text tpo = document.createTextNode(invoice.get_ValueAsString("RefDocType4"));
	            	TpoDocRef.appendChild(tpo);
	            	Referencia.appendChild(TpoDocRef);
	            	Element FolioRef = document.createElement("FolioRef");
	            	org.w3c.dom.Text ref = document.createTextNode(invoice.get_ValueAsString("FolioNumber4"));
	            	FolioRef.appendChild(ref);
	            	Referencia.appendChild(FolioRef);

	            	Element FchRef = document.createElement("FchRef");
	            	org.w3c.dom.Text fchref = document.createTextNode(invoice.get_ValueAsString("DateRef4").toString().substring(0, 10));
	            	FchRef.appendChild(fchref);
	            	Referencia.appendChild(FchRef);
	            	Element RazonRef = document.createElement("RazonRef");
	            	org.w3c.dom.Text codref = document.createTextNode(invoice.get_ValueAsString("RefReason4"));
	            	RazonRef.appendChild(codref);
	            	Referencia.appendChild(RazonRef);
	            }	
	            String attachment5 = invoice.get_ValueAsString("Attachment5");
	            log.config("attachment 5 = "+attachment5);
	            if(attachment5 == "true")
	            {	
	            	mylog = "Referencia";
	            	
	            	Element Referencia = document.createElement("Referencia");
	            	document.getDocumentElement().appendChild(Referencia);
	            	//Documento.appendChild(Referencia);
	            	Element NroLinRef = document.createElement("NroLinRef");
	            	org.w3c.dom.Text Nro = document.createTextNode("5");
	            	NroLinRef.appendChild(Nro);
	            	Referencia.appendChild(NroLinRef);
	            	Element TpoDocRef = document.createElement("TpoDocRef");
	            	org.w3c.dom.Text tpo = document.createTextNode(invoice.get_ValueAsString("RefDocType5"));
	            	TpoDocRef.appendChild(tpo);
	            	Referencia.appendChild(TpoDocRef);
	            	Element FolioRef = document.createElement("FolioRef");
	            	org.w3c.dom.Text ref = document.createTextNode(invoice.get_ValueAsString("FolioNumber5"));
	            	FolioRef.appendChild(ref);
	            	Referencia.appendChild(FolioRef);

	            	Element FchRef = document.createElement("FchRef");
	            	org.w3c.dom.Text fchref = document.createTextNode(invoice.get_ValueAsString("DateRef5").toString().substring(0, 10));
	            	FchRef.appendChild(fchref);
	            	Referencia.appendChild(FchRef);
	            	Element RazonRef = document.createElement("RazonRef");
	            	org.w3c.dom.Text codref = document.createTextNode(invoice.get_ValueAsString("RefReason5"));
	            	RazonRef.appendChild(codref);
	            	Referencia.appendChild(RazonRef);
	            }	
	            
	            //@mfrojas observaciones
	            
	            String obs = invoice.get_ValueAsString("Help");
	      
	            if(obs != null)
	            {
	            	mylog = "Adjuntos";
	            	Element Adjuntos = document.createElement("Adjuntos");
	            	document.getDocumentElement().appendChild(Adjuntos);
	            	//Documento.appendChild(Adjuntos);
	            	//@mfrojas datos adjuntos 1 2 y 3
	            	
	            /*	Element DatoAdj1 = document.createElement("DatoAdj1");
	            	org.w3c.dom.Text adj1 = document.createTextNode(invoice.getAD_User().getEMail()==null?" ":invoice.getAD_User().getEMail());
	            	DatoAdj1.appendChild(adj1);
	            	Adjuntos.appendChild(DatoAdj1);
	            	
	            	Element DatoAdj2 = document.createElement("DatoAdj2");
	            	org.w3c.dom.Text adj2 = document.createTextNode(invoice.getAD_User().getPhone()==null?" ":invoice.getAD_User().getPhone());
	            	DatoAdj2.appendChild(adj2);
	            	Adjuntos.appendChild(DatoAdj2);
	            	
	            	Element DatoAdj3 = document.createElement("DatoAdj3");
	            	org.w3c.dom.Text adj3 = document.createTextNode(invoice.getAD_User().getPhone()==null?" ":invoice.getAD_User().getPhone());
	            	DatoAdj3.appendChild(adj3);
	            	Adjuntos.appendChild(DatoAdj3);*/

	            	Element Observacion = document.createElement("Observacion");
	            	org.w3c.dom.Text adj = document.createTextNode(obs);
	            	Observacion.appendChild(adj);
	            	Adjuntos.appendChild(Observacion);
	            }
	            /*
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
	            	 //MOrder refdoc = new MOrder(getCtx(), ((Integer)get_Value("C_RefOrder_ID")).intValue(), get_TrxName()); 
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
            }
	            //fin referencia
	             * 
	             * 
	             */
	            
	            
	            mylog = "firma";
	            Element Firma = document.createElement("TmstFirma");
	            Timestamp today = new Timestamp(TimeUtil.getToday().getTimeInMillis());
	            org.w3c.dom.Text Ftext = document.createTextNode((new StringBuilder()).append(today.toString().substring(0, 10)).append("T").append(today.toString().substring(11, 19)).toString());
	            Firma.appendChild(Ftext);
	            document.getDocumentElement().appendChild(Firma);
	            //Documento.appendChild(Firma);
	            mylog = "archivo";
	           
	            String ExportDir = (String)company.get_Value("ExportDir");
	            log.config("EXPORTDIR1 "+ExportDir);
	  	        try
	  	          {
	  	        	  File theDir = new File(ExportDir);
	  	        	  if (!theDir.exists())
	  	        		  ExportDir = (String)company.get_Value("ExportDir"); 
	  	          }
	  	        catch(Exception e)
	  	          {
	  	        	  throw new AdempiereException("no existe directorio");
	  	          }
	  	        	//mfrojas debug
	  	        	log.config("EXPORTDIR "+ExportDir);
		            ExportDir = ExportDir.replace("\\", "/");
		            javax.xml.transform.Source source = new DOMSource(document);
		            javax.xml.transform.Result result = new StreamResult(new File(ExportDir, (new StringBuilder()).append(invoice.getDocumentNo()).append(".xml").toString()));
		            //mfrojas 
		            String nombrearchivo = invoice.getDocumentNo() + ".xml";
		            log.config("To string :"+nombrearchivo);
		            //end mfrojas
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
	        return "XML Generated";
	    
	
		
		
    }
        
    public Node findReturn(Node node) {
		Node value = null;
		NodeList list = node.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			// Get child node
			Node childNode = list.item(i);
			if(childNode.getNodeName().equals(("ns1:SolicitarSesionResponse")))
			{
				value = childNode;
				break;
			}

			value=findReturn(childNode);

		}
		return value;
    }
    public Node findReturnEnviarDTE(Node node) {
		Node value = null;
		NodeList list = node.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			// Get child node
			Node childNode = list.item(i);
			if(childNode.getNodeName().equals(("ns1:EnviarDTEResponse")))
			{
				value = childNode;
				break;
			}

			value=findReturnEnviarDTE(childNode);

		}
		return value;
    }
    public String findText(String cadena, String fragmentoIni, String fragmentoFin)
    {
    	String newText = "";
    	boolean flag;
    	int indiceIni = 0;
    	int indiceFin = 0;
    	
    	flag = cadena.contains(fragmentoIni);
    	
    	if (flag)    		
    	{
    		indiceIni = cadena.indexOf(fragmentoIni);
    		indiceIni = indiceIni + fragmentoIni.length();    		
    		indiceFin = cadena.indexOf(fragmentoFin);
    		newText = cadena.substring(indiceIni, indiceFin);
    	}
    	else
    		newText = "Error - No existe Cadena Especificada";    	
    	
    	return newText;
    }
    
    public String PrintPDF(String p_url) 
	{
		try
		{
			PrintService service = PrintServiceLookup.lookupDefaultPrintService();
			if (service != null) {
				DocFlavor psFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
			    PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();    
			    DocPrintJob job = service.createPrintJob();
			    Doc pdfDoc = new SimpleDoc(new URL(p_url).openStream(),psFormat, null);
			    job.print(pdfDoc, attributes);         
			}
			else
			{
				return "No Print Service";
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		try
		{
			PrintService service = PrintServiceLookup.lookupDefaultPrintService();
			if (service != null) {
				
			    PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();    
			    DocPrintJob job = service.createPrintJob();
			    SimpleDoc pdfDoc = new SimpleDoc(new URL(p_url).openStream(),DocFlavor.URL.AUTOSENSE, null);
			    job.print(pdfDoc, attributes);  
			       
			}
			else
			{
				return "No Print Service";
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		try
		{
			PrintService service = PrintServiceLookup.lookupDefaultPrintService();
			if (service != null) {
				
			    PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();    
			    DocPrintJob job = service.createPrintJob();
			    SimpleDoc pdfDoc = new SimpleDoc(new URL(p_url).openStream(),DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
			    job.print(pdfDoc, attributes);  
			        
			}
			else
			{
				return "No Print Service";
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "Imprimiendo...";
	}
}	//	InvoiceCreateInOut
