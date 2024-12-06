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
import java.net.URL;
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
 *  @author Italo Ni�oles ininoles
 *  @version $Id: ExportDTEInvoiceCG.java,v 1.2 19/05/2011 $
 */
public class ExportDTEInvoiceFOLNGemTest extends SvrProcess
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
		String msg=CreateXMLCG(inv);
		
		return msg;
	}	//	doIt
	
	public String CreateXMLCG(MInvoice invoice)
    {
		
		
		String wsRespuesta = "";
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
            document.setTextContent("text/xml");
            Attr atr = document.createAttribute("xmlns");
            atr.setValue("http://www.sii.cl/SiiDte");            
            
            Element Documento = document.createElement("Documento");
            document.getDocumentElement().appendChild(Documento);
            Documento.setAttribute("ID", (new StringBuilder()).append("F").append(invoice.getDocumentNo()).append("T").append((String)doc.get_Value("DocumentNo")).toString());
            Element Encabezado = document.createElement("Encabezado");
            Documento.appendChild(Encabezado);
            Element IdDoc = document.createElement("IdDoc");
            Encabezado.appendChild(IdDoc);
            mylog = "IdDoc";
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
            Element FchVenc = document.createElement("FchVenc");
            org.w3c.dom.Text venc = document.createTextNode(invoice.getDateInvoiced().toString().substring(0, 10));
            FchVenc.appendChild(venc);
            IdDoc.appendChild(FchVenc);
            Element Emisor = document.createElement("Emisor");
            Encabezado.appendChild(Emisor);
            mylog = "Emisor";
            MOrg company = MOrg.get(invoice.getCtx(), invoice.getAD_Org_ID());
            Element Rut = document.createElement("RUTEmisor");
            org.w3c.dom.Text rut = document.createTextNode((String)company.get_Value("Rut"));
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
            nameRzn = nameRzn.replace("'", "");
            nameRzn = nameRzn.replace("\"", "");
            Element RznSoc = document.createElement("RznSoc");
            org.w3c.dom.Text rzn = document.createTextNode(nameRzn);
            RznSoc.appendChild(rzn);
            Emisor.appendChild(RznSoc);            
            String giroEmisStr = (String)company.get_Value("Giro");
            giroEmisStr = giroEmisStr.replace("'", "");
            giroEmisStr = giroEmisStr.replace("\"", "");
            Element GiroEmis = document.createElement("GiroEmis");
            org.w3c.dom.Text gi = document.createTextNode(giroEmisStr);
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
            MBPartner BP = new MBPartner(invoice.getCtx(), invoice.getC_BPartner_ID(), invoice.get_TrxName());
            MBPartnerLocation bloc = new MBPartnerLocation(invoice.getCtx(), invoice.getC_BPartner_Location_ID(), invoice.get_TrxName());
            Element Receptor = document.createElement("Receptor");
            Encabezado.appendChild(Receptor);
            Element RUTRecep = document.createElement("RUTRecep");
            org.w3c.dom.Text rutc = document.createTextNode((new StringBuilder()).append(BP.getValue()).append("-").append(BP.get_ValueAsString("Digito")).toString());
            RUTRecep.appendChild(rutc);
            Receptor.appendChild(RUTRecep);
            
            String RznSocRecepStr = BP.getName();
            RznSocRecepStr = RznSocRecepStr.replace("'", "");
            RznSocRecepStr = RznSocRecepStr.replace("\"", "");
            Element RznSocRecep = document.createElement("RznSocRecep");
            org.w3c.dom.Text RznSocR = document.createTextNode(RznSocRecepStr);
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
            
            String dirRecepStr = bloc.getLocation(true).getAddress1();
            dirRecepStr = dirRecepStr.replace("'", "");
            dirRecepStr = dirRecepStr.replace("\"", "");
            Element DirRecep = document.createElement("DirRecep");
            org.w3c.dom.Text dirr = document.createTextNode(dirRecepStr);
            DirRecep.appendChild(dirr);
            Receptor.appendChild(DirRecep);
            
            String CmnaRecepStr = null;
            
            if (bloc.getLocation(true).getAddress2() != null)
            	CmnaRecepStr = bloc.getLocation(true).getAddress2();
            else if (bloc.getLocation(true).getC_City_ID()>0)            
            	CmnaRecepStr = MCity.get(invoice.getCtx(), bloc.getLocation(true).getC_City_ID()).getName();
	         
        	Element CmnaRecep = document.createElement("CmnaRecep");
            org.w3c.dom.Text Cmna = document.createTextNode(CmnaRecepStr);
            CmnaRecep.appendChild(Cmna);
            Receptor.appendChild(CmnaRecep);
            
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
            
            mylog = "detalle";
            MInvoiceLine iLines[] = invoice.getLines(false);
            int lineInvoice = 0;
            for(int i = 0; i < iLines.length; i++)
            {	
            	MInvoiceLine iLine = iLines[i];
            	if(iLine.getM_Product_ID()==0 && iLine.getC_Charge_ID()==0)
            		continue;
            	
                Element Detalle = document.createElement("Detalle");
                Documento.appendChild(Detalle);
                
                MTax tax=new MTax(invoice.getCtx() ,iLine.getC_Tax_ID(),invoice.get_TrxName() );                
                if(tax.isTaxExempt()){
                	 Element IndEx = document.createElement("IndExe");
                     org.w3c.dom.Text lineE = document.createTextNode("1");
                     IndEx.appendChild(lineE);
                     Detalle.appendChild(IndEx);	
                }
                
                lineInvoice = lineInvoice+1;              
                Element NroLinDet = document.createElement("NroLinDet");
                org.w3c.dom.Text line = document.createTextNode(Integer.toString(iLine.getLine() / 10));
                NroLinDet.appendChild(line);
                Detalle.appendChild(NroLinDet);
                Element NmbItem = document.createElement("NmbItem");
                String pname="";
                if(iLine.getProduct()!=null )
                	pname=iLine.getProduct().getName();
                else
                	pname=iLine.getC_Charge().getName();
                
                pname = pname.replace("'", "");
                pname = pname.replace("\"", "");
                org.w3c.dom.Text Item = document.createTextNode(pname);
                NmbItem.appendChild(Item);
                Detalle.appendChild(NmbItem);
                
                Element DscItem = document.createElement("DscItem");
                org.w3c.dom.Text desc = document.createTextNode(iLine.getDescription()==null?" ":iLine.getDescription());
                DscItem.appendChild(desc);
                Detalle.appendChild(DscItem);
                
                Element QtyItem = document.createElement("QtyItem");
                org.w3c.dom.Text qt = document.createTextNode(iLine.getQtyInvoiced().toString());
                QtyItem.appendChild(qt);
                Detalle.appendChild(QtyItem);
                                
                String unmdItemStr = "";
                if(iLine.getM_Product_ID() > 0)
                	unmdItemStr = iLine.getM_Product().getC_UOM().getUOMSymbol();
                else
                	unmdItemStr = "UN";                
                if (unmdItemStr == null)
                	unmdItemStr = "UN";                
                Element UnmdItem = document.createElement("UnmdItem");
                org.w3c.dom.Text UM = document.createTextNode(unmdItemStr);
                UnmdItem.appendChild(UM);
                Detalle.appendChild(UnmdItem);
                
                Element PrcItem = document.createElement("PrcItem");
                org.w3c.dom.Text pa = document.createTextNode(iLine.getPriceActual().setScale(0, 4).toString());
                PrcItem.appendChild(pa);
                Detalle.appendChild(PrcItem);
                Element MontoItem = document.createElement("MontoItem");
                org.w3c.dom.Text tl = document.createTextNode(iLine.getLineNetAmt().setScale(0, 4).toString());
                MontoItem.appendChild(tl);
                Detalle.appendChild(MontoItem);
            }
            
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
                org.w3c.dom.Text codref = document.createTextNode(Integer.toString(tipo_Ref));
                CodRef.appendChild(codref);
                Referencia.appendChild(CodRef);                
            }
            //fin referencia
            //referencia HES Geminis
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
                                  
                if(fechareferencia == null)
                	fechareferencia = invoice.getDateInvoiced().toString().substring(0, 10);
                if(fechareferencia.compareTo(" ") == 0 || fechareferencia.compareTo("") == 0)
                	fechareferencia = invoice.getDateInvoiced().toString().substring(0, 10);
                if(fechareferencia.length()<2)
                	fechareferencia = invoice.getDateInvoiced().toString().substring(0, 10);                
                
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
            
            if (invoice.getDescription() != null && invoice.getDescription() != "" && invoice.getDescription() != " ")
            {            
	            mylog = "Adicional";
	            Element Adicional = document.createElement("Adicional");
	            Documento.appendChild(Adicional);
	            Element NodosA = document.createElement("NodosA");
	            Adicional.appendChild(NodosA);
	            Element A6 = document.createElement("A6");
	            org.w3c.dom.Text a6Text = document.createTextNode(invoice.getDescription());
	            A6.appendChild(a6Text);
	            NodosA.appendChild(A6);
            }            

            mylog = "archivo";            
            
	        String ExportDir = (String)company.get_Value("ExportDir");
	        try
	        {
	        	File theDir = new File(ExportDir);
	        	if (!theDir.exists())
	        		ExportDir = (String)company.get_Value("ExportDir2"); 
	        }
	        catch(Exception e)
	        {
	         throw new AdempiereException("no existe directorio");
	        }
	          
	        ExportDir = ExportDir.replace("\\", "/");
	        javax.xml.transform.Source source = new DOMSource(document);
	        javax.xml.transform.Result result = new StreamResult(new File(ExportDir, (new StringBuilder()).append(invoice.getDocumentNo()).append(".xml").toString()));
	        javax.xml.transform.Result console = new StreamResult(System.out);
	        Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty("indent", "yes");
	        transformer.setOutputProperty("encoding", "UTF-8");
	        transformer.transform(source, result);
	        transformer.transform(source, console);
                   
            //convertir a base 64                       
    		File file = new File(ExportDir, (new StringBuilder()).append(invoice.getDocumentNo()).append(".xml").toString());

    		//setear atributos de cabecera
    		Document docValid = builder.parse(file);
    		Element raiz = docValid.getDocumentElement();
    		raiz.setAttribute("version", "1.0");
    		raiz.setAttribute("xmlns", "http://www.sii.cl/SiiDte");
    		
    		//se guarda nuevo xml
            source = new DOMSource(docValid);
            result = new StreamResult(new File(ExportDir, (new StringBuilder()).append(invoice.getDocumentNo()).append(".xml").toString()));
            console = new StreamResult(System.out);
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.transform(source, result);
            transformer.transform(source, console);
    		
            //codificacion a base64
    		byte[] fileArray = new byte[(int) file.length()];
    		InputStream inputStream;
    		inputStream = new FileInputStream(file);
			inputStream.read(fileArray);    		
			
    		byte[] encoded = Base64.encodeBase64(fileArray);     
    		String encodedFile = "";
    		try 
    		{	
    			encodedFile = new String(encoded);
    		}catch (Exception e) {
    		}
    		
    		//enlace con factura en linea    	    
    		String token = "";
    		String rutEmpresa, rutUsuario, password, archivoXML, tipoDocumento, folioDocumento;
    		
    		rutEmpresa = ""; 
			rutUsuario = "";
			password = "";
			
    		try
    		{	
    			rutEmpresa = OFBForward.RutEmpresaFEL(); 
    			rutUsuario = OFBForward.RutUsuarioFEL();
    			password = OFBForward.ContrasenaFEL();
    		
    		}catch (Exception e)
    		{
    			log.log(Level.SEVERE, e.getMessage(), e);
    		}
    		
    		archivoXML = ExportDir+invoice.getDocumentNo()+".xml";
    		tipoDocumento = Integer.toString(typeDoc);
    		folioDocumento = invoice.getDocumentNo();
    		if (archivoXML != "" && rutEmpresa != "" && rutUsuario != "" && password != "")
    		{
    			//Aqu� se rescata el token de seguridad (expira en 5 minutos)
    			token = llamarWSToken(rutEmpresa, rutUsuario, password);    			
    			if (token.substring(0, 5).compareToIgnoreCase("error") == 0)
    			{
    				return token;
    			}
    			else
    			{	
    				//	Aqu� se env�a el token de seguridad, el objeto del archivo XML encriptado, el tipo de documento y el folio del documento
    				wsRespuesta = llamarWSEnviarDTE(token, encodedFile, rutEmpresa, tipoDocumento, folioDocumento);
    				log.config(wsRespuesta);    				
    				if (urlPdf != null && urlPdf != "" && urlPdf != " ")
    				{
    					urlPdf = urlPdf+"&Ced=1";
    					invoice.set_CustomColumn("URLXML",urlPdf);
    				}    				
    				invoice.set_CustomColumn("DescriptionFEL",wsRespuesta);
    				invoice.save();
    				if (urlPdf != null && urlPdf != "" && urlPdf != " ")
    				{
    					PrintPDF(urlPdf);
    				}
    			}    		
    		}
    	}
        catch(Exception e)
        {
            log.severe((new StringBuilder()).append("CreateXML: ").append(mylog).append("--").append(e.getMessage()).toString());
            return (new StringBuilder()).append("CreateXML: ").append(mylog).append("--").append(e.getMessage()).toString();
        }       
        
    
        
        return "XML CG Generated "+ wsRespuesta;
    }	
        
    private String llamarWSToken(String rutEmpresa,String rutPersona,String password) throws Exception
    {
	    	String token = "";	
	    	final String request = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wst=\"/var/www//WsTest/\">"+
	   			 "<soapenv:Header/>"+
	   			 "<soapenv:Body>"+
	   			 "<wst:SolicitarSesion soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"+
	   			 "<rutEmpresa xsi:type=\"xsd:string\">"+rutEmpresa+"</rutEmpresa>"+
	   			 "<rutPersona xsi:type=\"xsd:string\">"+rutPersona+"</rutPersona>"+
	   			 "<password xsi:type=\"xsd:string\">"+password+"</password>"+
	   	         "</wst:SolicitarSesion>"+
	   			 "</soapenv:Body>"+
	   			 "</soapenv:Envelope>";
	
	   			Source response = null;
	   					
	   			try
	   			{				
	   				final CXFConnector wsc = new CXFConnector();					
	   				wsc.setSoapAction("http://clientes2.dtefacturaenlinea.cl/WsTest/wsFEL.php/SolicitarSesion");
	   				wsc.setRequest(request);
	   				wsc.setBinding(SOAPBinding.SOAP11HTTP_BINDING);
	   				wsc.setEndpointAddress("http://clientes2.dtefacturaenlinea.cl/WsTest/wsFEL.php");
	   				wsc.setServiceName("DTElectronico");
	   				wsc.setPortName("DTElectronicoPort");
	   				wsc.setTargetNS("/var/www//WsTest/");
	   				wsc.executeConnector();
	   				response = wsc.getResponse(); 
	   				
	   			}
	   			catch(Exception e)
	   			{
	   				throw new Exception("No se ha podido establecer conexion con el Servicio de Facturacion");
	   			}
    				
    				
    			try
    			{
    				//comienza la lectura del xml recibido
    				DocumentBuilderFactory.newInstance().newDocumentBuilder();
    				SAXSource output = (SAXSource) response;
    				Transformer tf = TransformerFactory.newInstance().newTransformer();
    			
    				DOMResult result = new DOMResult();
    				tf.transform(output, result);
    				Document doc = (Document) result.getNode();
    				
    				Node datos = findReturn(doc.getChildNodes().item(0)); 
    									
    				if(datos!=null){
    					NodeList att = datos.getChildNodes(); 
    					for(int x=0;x<att.getLength();x++)
    					{	
    						log.config(att.item(x).getLocalName());
    						if(att.item(x).getLocalName().equals("return") || att.item(x).getNodeName().equals("return"))
    							token=att.item(x).getFirstChild().getNodeValue();
    					} // fin for return
    				}//FIN DATOS
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    		return token;    			
    }  
    private String llamarWSEnviarDTE(String token,String dte,String rutEmpresa, String tipoDocumento,String folioDocumento ) throws Exception
    {	
    	String msg = "";    	
    	String EstadoDte = "";
    	String GlosaEstadoDte = "";
    	
    	final String request = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wst=\"/var/www//WsTest/\">"+
   			 "<soapenv:Header/>"+
   			 "<soapenv:Body>"+
   			 "<wst:EnviarDTE soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"+
   			 "<sesion xsi:type=\"xsd:string\">"+token+"</sesion>"+
   			 "<archivo xsi:type=\"xsd:string\">"+dte+"</archivo>"+    			 
   			 "<rutEmpresa xsi:type=\"xsd:string\">"+rutEmpresa+"</rutEmpresa>"+
   			 "<tipoDocumento xsi:type=\"xsd:string\">"+tipoDocumento+"</tipoDocumento>"+
   			 "<folio xsi:type=\"xsd:string\">"+folioDocumento+"</folio>"+
   	         "</wst:EnviarDTE>"+
   			 "</soapenv:Body>"+
   			 "</soapenv:Envelope>";

   			Source response = null;
   					
   			try
   			{				
   				final CXFConnector wsc = new CXFConnector();					
   				wsc.setSoapAction("http://clientes2.dtefacturaenlinea.cl/WsTest/wsFEL.php/EnviarDTE");
   				wsc.setRequest(request);
   				wsc.setBinding(SOAPBinding.SOAP11HTTP_BINDING);   				
   				wsc.setEndpointAddress("http://clientes2.dtefacturaenlinea.cl/WsTest/wsFEL.php");
   				wsc.setServiceName("DTElectronico");
   				wsc.setPortName("DTElectronicoPort");
   				wsc.setTargetNS("/var/www//WsTest/");    				
   				wsc.executeConnector();
   				response = wsc.getResponse();
   				
   			}
   			catch(Exception e)
   			{
   				throw new Exception("No se ha podido establecer conexion con el Servicio de Facturacion - " + e);
   			}    				
    			try
    			{
    				//comienza la lectura del xml recibido
    				DocumentBuilderFactory.newInstance().newDocumentBuilder();
    				SAXSource output = (SAXSource) response;
    				Transformer tf = TransformerFactory.newInstance().newTransformer();
    			
    				DOMResult result = new DOMResult();
    				tf.transform(output, result);
    				Document doc = (Document) result.getNode();
    				
    				Node datos = findReturnEnviarDTE(doc.getChildNodes().item(0)); 
    									
    				if(datos!=null){
    					NodeList att = datos.getChildNodes(); 
    					for(int x=0;x<att.getLength();x++)
    					{	
    						log.config(att.item(x).getLocalName());
    						if(att.item(x).getLocalName().equals("return") || att.item(x).getNodeName().equals("return"))
							{	
    							msg = att.item(x).getFirstChild().getNodeValue();								
							}    							
    					} // fin for return
    				}//FIN DATOS    				
    				EstadoDte = findText(msg, "<EstadoDTE>", "</EstadoDTE>");
					GlosaEstadoDte = findText(msg, "<GlosaEstadoDTE>", "</GlosaEstadoDTE>");
					urlPdf = findText(msg, "<PDF Url=\"", "\"/>");
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    		return "Estado DTE: "+EstadoDte+" Glosa Estado: "+GlosaEstadoDte+ " Url:"+urlPdf;    			
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
