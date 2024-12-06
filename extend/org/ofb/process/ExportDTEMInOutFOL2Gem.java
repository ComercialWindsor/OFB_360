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
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Properties;
import java.util.logging.*;

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

import org.apache.commons.codec.binary.Base64;
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
 
/**
 *	Generate XML from MInOut
 *	
 *  @author Italo Niñoles Ininoles
 *  @version $Id: ExportDTEMInOut.java,v 1.2 05/09/2014 $
 */
public class ExportDTEMInOutFOL2Gem extends SvrProcess
{
	/** Properties						*/
	private Properties 		m_ctx;	
	private int p_M_InOut_ID = 0;
	public String urlPdf = "";

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_M_InOut_ID=getRecord_ID();
		m_ctx = Env.getCtx();
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		MInOut inOut=new MInOut(m_ctx,p_M_InOut_ID, get_TrxName());
		String msg=CreateXMLCG(inOut);
		
		return msg;
	}	//	doIt
	

	public String CreateXMLCG(MInOut inOut)
    {
		String wsRespuesta = "";
		MDocType doc = new MDocType(inOut.getCtx(), inOut.getC_DocType_ID(), inOut.get_TrxName());
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
            Documento.setAttribute("ID", (new StringBuilder()).append("F").append(inOut.getDocumentNo()).append("T").append((String)doc.get_Value("DocumentNo")).toString());
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
            org.w3c.dom.Text fo = document.createTextNode(inOut.getDocumentNo());
            Folio.appendChild(fo);
            IdDoc.appendChild(Folio);
            Element FchEmis = document.createElement("FchEmis");
            org.w3c.dom.Text emis = document.createTextNode(inOut.getMovementDate().toString().substring(0, 10));
            FchEmis.appendChild(emis);
            IdDoc.appendChild(FchEmis);
            
            //ininoles end
            
            //tipo de despacho ininoles            
            /*String DVRule = "select rlt.name from AD_Ref_List rl left join AD_Ref_List_Trl rlt on (rl.AD_Ref_List_ID = rlt.AD_Ref_List_ID )"+
            	"where AD_Reference_ID = 152 and rl.value = '"+getDeliveryViaRule() +"' and ad_language like '"+Env.getAD_Language(inOut.inOut.getCtx())+"' ";
            String DVRuleName = DB.getSQLValueString(inOut.get_TrxName(), DVRule);*/
            
            Element TipoDespacho = document.createElement("TipoDespacho");
            org.w3c.dom.Text TDespacho = document.createTextNode("2");
            TipoDespacho.appendChild(TDespacho);
            IdDoc.appendChild(TipoDespacho);
            //ininoles end
            
            
          //ininoles indicaciones traslado
            String IndTrl = null;
            try {
            	IndTrl = inOut.get_ValueAsString("IndTraslado");
            }
            catch (Exception e) {
            	IndTrl = "1";
			}
            if (IndTrl == null || IndTrl == "" || IndTrl == " ")
            	IndTrl = "1";
            if ( IndTrl != null && IndTrl.length()>0)
            {
            	Element IndTraslado = document.createElement("IndTraslado");
                org.w3c.dom.Text iTraslado = document.createTextNode(IndTrl);
                IndTraslado.appendChild(iTraslado);
                IdDoc.appendChild(IndTraslado);         	
            }
                        
            Element Emisor = document.createElement("Emisor");
            Encabezado.appendChild(Emisor);
            mylog = "Emisor";
            MOrg company = MOrg.get(inOut.getCtx(), inOut.getAD_Org_ID());
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
            nameRzn = nameRzn.replace("'", "");
            nameRzn = nameRzn.replace("\"", "");
            //ininoles end            
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
            MBPartner BP = new MBPartner(inOut.getCtx(), inOut.getC_BPartner_ID(), inOut.get_TrxName());
            MBPartnerLocation bloc = new MBPartnerLocation(inOut.getCtx(), inOut.getC_BPartner_Location_ID(), inOut.get_TrxName());
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
            org.w3c.dom.Text contacto = document.createTextNode(inOut.getAD_User_ID()>0?inOut.getAD_User().getName():" "); //nombre completo contacto
            ContactoRecep.appendChild(contacto);
            Receptor.appendChild(ContactoRecep);
            
            String dirRecepStr = bloc.getLocation(true).getAddress1();
            dirRecepStr = dirRecepStr.replace("'", "");
            dirRecepStr = dirRecepStr.replace("\"", "");
            Element DirRecep = document.createElement("DirRecep");
            org.w3c.dom.Text dirr = document.createTextNode(dirRecepStr);            
            DirRecep.appendChild(dirr);
            Receptor.appendChild(DirRecep);
            
            /*if(bloc.getLocation(true).getAddress2()!=null && bloc.getLocation(true).getAddress2().length()>0 ){
	            Element CmnaRecep = document.createElement("CmnaRecep");
	            org.w3c.dom.Text Cmna = document.createTextNode(bloc.getLocation(true).getAddress2());
	            CmnaRecep.appendChild(Cmna);
	            Receptor.appendChild(CmnaRecep);
            }
            Element CmnaRecep = document.createElement("CmnaRecep");
	        org.w3c.dom.Text Cmna = document.createTextNode(bloc.getLocation(true).getAddress3()==null?" ":bloc.getLocation(true).getAddress3());
	        CmnaRecep.appendChild(Cmna);
	        Receptor.appendChild(CmnaRecep);*/
            String CmnaRecepStr = null;            
            if (bloc.getLocation(true).getAddress2() != null)
            	CmnaRecepStr = bloc.getLocation(true).getAddress2();
            else if (bloc.getLocation(true).getC_City_ID()>0)            
            	CmnaRecepStr = MCity.get(inOut.getCtx(), bloc.getLocation(true).getC_City_ID()).getName();
            else
            	CmnaRecepStr = bloc.getLocation(true).getAddress3();
            
            Element CmnaRecep = document.createElement("CmnaRecep");
            org.w3c.dom.Text Cmna = document.createTextNode(CmnaRecepStr);
            CmnaRecep.appendChild(Cmna);
            Receptor.appendChild(CmnaRecep);
            
            Element CiudadRecep = document.createElement("CiudadRecep");
            org.w3c.dom.Text reg = document.createTextNode(bloc.getLocation(true).getC_City_ID()>0?MCity.get(inOut.getCtx(), bloc.getLocation(true).getC_City_ID()).getName():"Santiago");
            CiudadRecep.appendChild(reg);
            Receptor.appendChild(CiudadRecep);
            
            //ininoles nuevos campos pedidos por hernani
            /*Element transporte = document.createElement("Transporte");
            Encabezado.appendChild(transporte);
            
            Element DirDest = document.createElement("DirDest");
            org.w3c.dom.Text dirdest = document.createTextNode(bloc.getLocation(true).getAddress1());
            DirDest.appendChild(dirdest);
            transporte.appendChild(DirDest);
            
            Element CmnaDest = document.createElement("CmnaDest");
	        org.w3c.dom.Text CmnaDestTxt = document.createTextNode(bloc.getLocation(true).getAddress3()==null?" ":bloc.getLocation(true).getAddress3());
	        CmnaDest.appendChild(CmnaDestTxt);
	        transporte.appendChild(CmnaDest);
            
            Element CiudadDest = document.createElement("CiudadDest");
            org.w3c.dom.Text regDest = document.createTextNode(bloc.getLocation(true).getC_City_ID()>0?MCity.get(inOut.getCtx(), bloc.getLocation(true).getC_City_ID()).getName():"Santiago");
            CiudadDest.appendChild(regDest);
            transporte.appendChild(CiudadDest);
            */
            mylog = "Totales";
            Element Totales = document.createElement("Totales");
            Encabezado.appendChild(Totales);
            
            BigDecimal amountGrandT = Env.ZERO;            
            BigDecimal priceT = Env.ZERO;
            BigDecimal taxAmt = Env.ZERO;
            BigDecimal taxIVAAmt = Env.ZERO;
            BigDecimal taxIVAAmtAcu = Env.ZERO;
            BigDecimal taxExeAmtAcu = Env.ZERO;
            BigDecimal taxNetoAmtAcu = Env.ZERO;
            
            if (inOut.getC_Order_ID() > 0)
            {
            	//calculo de monto de la guia
            	MInOutLine iLines2[] = inOut.getLines();
            	for(int a = 0; a < iLines2.length; a++)
                {	
            		priceT = Env.ZERO;
            		taxAmt = Env.ZERO;
            		taxIVAAmt = Env.ZERO;
            		MInOutLine iLine = iLines2[a];            		
           			priceT = iLine.getC_OrderLine().getPriceEntered();   
        			BigDecimal qtyO = iLine.getQtyEntered();
            		priceT = priceT.multiply(qtyO);
            		if(iLine.getC_OrderLine().getC_Tax_ID() > 0)
            		{
            			if (iLine.getC_OrderLine().getC_Tax().getRate().compareTo(Env.ZERO) > 0)
            			{
            				taxAmt = priceT.multiply(iLine.getC_OrderLine().getC_Tax().getRate());
            				taxAmt = taxAmt.divide(Env.ONEHUNDRED);
            			}
            			if (iLine.getC_OrderLine().getC_Tax().isTaxExempt())            			
            				taxExeAmtAcu = taxExeAmtAcu.add(priceT);
            			if (iLine.getC_OrderLine().getC_Tax().getName().toUpperCase().contains("IVA")
            					&& iLine.getC_OrderLine().getC_Tax().getRate().compareTo(new BigDecimal("19.0")) == 0)
            			{
            				taxIVAAmt = priceT.multiply(iLine.getC_OrderLine().getC_Tax().getRate());
            				taxIVAAmt = taxIVAAmt.divide(Env.ONEHUNDRED);
            			}
            			taxNetoAmtAcu = taxNetoAmtAcu.add(priceT);            			
            		}            		
            		amountGrandT = amountGrandT.add(priceT.add(taxAmt));
            		taxIVAAmtAcu = taxIVAAmtAcu.add(taxIVAAmt);
                }
            }
            Element MntNeto = document.createElement("MntNeto");
	        org.w3c.dom.Text MntNetotxt = document.createTextNode(taxNetoAmtAcu.setScale(0, 4).toString());
	        MntNeto.appendChild(MntNetotxt);
	        Totales.appendChild(MntNeto);
	        
	        Element MntExe = document.createElement("MntExe");
	        org.w3c.dom.Text MntExeTxt = document.createTextNode(taxExeAmtAcu.setScale(0, 4).toString());
	        MntExe.appendChild(MntExeTxt);
	        Totales.appendChild(MntExe);            
                       
            Element TasaIVA = document.createElement("TasaIVA");
	        org.w3c.dom.Text tiva = document.createTextNode("19");
	        TasaIVA.appendChild(tiva);
	        Totales.appendChild(TasaIVA);	        
	        
	        Element MntIva = document.createElement("IVA");
	        org.w3c.dom.Text MntIvaTxt = document.createTextNode(taxIVAAmtAcu.setScale(0, 4).toString());
	        MntIva.appendChild(MntIvaTxt);
	        Totales.appendChild(MntIva);           
	        
            Element MntTotal = document.createElement("MntTotal");
            org.w3c.dom.Text total = document.createTextNode(amountGrandT.setScale(0, 4).toString());
            MntTotal.appendChild(total);
            Totales.appendChild(MntTotal);
           
            mylog = "detalle";
            MInOutLine iLines[] = inOut.getLines();
            int indLine = 1;
            for(int i = 0; i < iLines.length; i++)
            {
            	MInOutLine iLine = iLines[i];
            	if(iLine.getM_Product_ID()==0 && iLine.getC_Charge_ID()==0)
            		continue;
            	
	            Element Detalle = document.createElement("Detalle");
                Documento.appendChild(Detalle);	                
                
                Element NroLinDet = document.createElement("NroLinDet");
                org.w3c.dom.Text line = document.createTextNode(Integer.toString(indLine));
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
                
                Element QtyItem = document.createElement("QtyItem");
                org.w3c.dom.Text qt = document.createTextNode(iLine.getQtyEntered().toString());
                QtyItem.appendChild(qt);
                Detalle.appendChild(QtyItem);
                
                Element UnmdItem = document.createElement("UnmdItem");
                org.w3c.dom.Text UItxt = document.createTextNode(iLine.getC_UOM().getUOMSymbol().trim().compareToIgnoreCase("Ea")==0?"UN":iLine.getC_UOM().getUOMSymbol());
                UnmdItem.appendChild(UItxt);
                Detalle.appendChild(UnmdItem);
                
              //ininoles seteo de monto
                BigDecimal mtoItem = Env.ZERO;
                BigDecimal prcRefMnt = Env.ZERO;
                if(iLine.getC_OrderLine_ID() > 0)
                {                	   
                	prcRefMnt = iLine.getC_OrderLine().getPriceEntered();
                	mtoItem = prcRefMnt.multiply(iLine.getQtyEntered());
                }
                
                Element PrcRef = document.createElement("PrcItem");
                org.w3c.dom.Text PrcRefTxt = document.createTextNode(prcRefMnt.setScale(0, 4).toString());
                PrcRef.appendChild(PrcRefTxt);
                Detalle.appendChild(PrcRef);
                
                Element MtoItem = document.createElement("MontoItem");
                org.w3c.dom.Text MtoTxt = document.createTextNode(mtoItem.setScale(0, 4).toString());
                MtoItem.appendChild(MtoTxt);
                Detalle.appendChild(MtoItem);                
                indLine = indLine + 1;
            }
            
            mylog = "referencia";
            String tiporeferencia = new String();
            String folioreferencia  = new String();
            String fechareferencia = new String();
            int tipo_Ref =0;
            
            if(inOut.getPOReference() != null && inOut.getPOReference().length() > 0)//referencia orden
            {
            	 mylog = "referencia:order";
            	 //MOrder refdoc = new MOrder(inOut.getCtx(), ((Integer)get_Value("C_RefOrder_ID")).intValue(), inOut.get_TrxName()); 
            	 tiporeferencia = "801";
                 folioreferencia = inOut.getPOReference();
                 fechareferencia = inOut.getDateOrdered().toString().substring(0, 10);
            	 tipo_Ref = 2; //Orden
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
                org.w3c.dom.Text ref = document.createTextNode("*Tipo Documento: Orden de Compra; *Folio:"+folioreferencia+"; *Fecha Emisión:"+fechareferencia);
                FolioRef.appendChild(ref);
                Referencia.appendChild(FolioRef);
                Element FchRef = document.createElement("FchRef");
                org.w3c.dom.Text fchref = document.createTextNode(fechareferencia);
                FchRef.appendChild(fchref);
                Referencia.appendChild(FchRef);
                String CodRefTxt = null;
                try {
                	CodRefTxt = inOut.get_ValueAsString("CodRef");
                }
                catch (Exception e) {
                	CodRefTxt = null;
				}
                if ( CodRefTxt != null && CodRefTxt.length()>0)
                {
                	Element CodRef = document.createElement("CodRef");
                    org.w3c.dom.Text codref = document.createTextNode(inOut.get_ValueAsString("CodRef")==null?"0":inOut.get_ValueAsString("CodRef"));                
                    CodRef.appendChild(codref);
                    Referencia.appendChild(CodRef);                	
                }                
                
            }
            //fin referencia            
            if (inOut.getDescription() != null && inOut.getDescription() != "" && inOut.getDescription() != " ")
            {            
	            mylog = "Adicional";
	            Element Adicional = document.createElement("Adicional");
	            Documento.appendChild(Adicional);
	            Element NodosA = document.createElement("NodosA");
	            Adicional.appendChild(NodosA);
	            Element A6 = document.createElement("A6");
	            org.w3c.dom.Text a6Text = document.createTextNode(inOut.getDescription());
	            A6.appendChild(a6Text);
	            NodosA.appendChild(A6);
            }
            mylog = "archivo";
            String ExportDir = (String)company.get_Value("ExportDir");
            ExportDir = ExportDir.replace("\\", "/");
            javax.xml.transform.Source source = new DOMSource(document);
            javax.xml.transform.Result result = new StreamResult(new File(ExportDir, (new StringBuilder()).append(inOut.getDocumentNo()).append(".xml").toString()));
            javax.xml.transform.Result console = new StreamResult(System.out);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.transform(source, result);
            transformer.transform(source, console);
            
          //convertir a base 64                       
    		File file = new File(ExportDir, (new StringBuilder()).append(inOut.getDocumentNo()).append(".xml").toString());

    		//setear atributos de cabecera
    		Document docValid = builder.parse(file);
    		Element raiz = docValid.getDocumentElement();
    		raiz.setAttribute("version", "1.0");
    		raiz.setAttribute("xmlns", "http://www.sii.cl/SiiDte");
    		
    		//se guarda nuevo xml
            source = new DOMSource(docValid);
            result = new StreamResult(new File(ExportDir, (new StringBuilder()).append(inOut.getDocumentNo()).append(".xml").toString()));
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
    		} catch (Exception e) {
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
    		
    		archivoXML = ExportDir+inOut.getDocumentNo()+".xml";
    		tipoDocumento = Integer.toString(typeDoc);
    		folioDocumento = inOut.getDocumentNo();
    		if (archivoXML != "" && rutEmpresa != "" && rutUsuario != "" && password != "")
    		{
    			//Aquï¿½ se rescata el token de seguridad (expira en 5 minutos)
    			token = llamarWSToken(rutEmpresa, rutUsuario, password);    			
    			if (token.substring(0, 5).compareToIgnoreCase("error") == 0)
    			{
    				return token;
    			}
    			else
    			{	
    				//	Aquï¿½ se envï¿½a el token de seguridad, el objeto del archivo XML encriptado, el tipo de documento y el folio del documento
    				wsRespuesta = llamarWSEnviarDTE(token, encodedFile, rutEmpresa, tipoDocumento, folioDocumento);
    				log.config(wsRespuesta);    				
    				if (urlPdf != null && urlPdf != "" && urlPdf != " ")
    				{
    					urlPdf = urlPdf+"&Ced=2";
    					inOut.set_CustomColumn("URLXML",urlPdf);
    				}    				
    				inOut.set_CustomColumn("DescriptionFEL",wsRespuesta);
    				inOut.save();
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
        
        return "XML Shipment Generated";
    }	
	
    
	private String llamarWSToken(String rutEmpresa,String rutPersona,String password) throws Exception
    {
    	String token = "";	
    	final String request = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wst=\"/var/www//WsTest/\"> " +
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
    				wsc.setSoapAction("http://clientes2.dtefacturaenlinea.cl/WsFEL/wsFEL.php/SolicitarSesion");
    				wsc.setRequest(request);
    				wsc.setBinding(SOAPBinding.SOAP11HTTP_BINDING);
    				wsc.setEndpointAddress("http://clientes2.dtefacturaenlinea.cl/WsFEL/wsFEL.php");
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
    				wsc.setSoapAction("http://clientes2.dtefacturaenlinea.cl/WsFEL/wsFEL.php/EnviarDTE");
    				wsc.setRequest(request);
    				wsc.setBinding(SOAPBinding.SOAP11HTTP_BINDING);
    				wsc.setEndpointAddress("http://clientes2.dtefacturaenlinea.cl/WsFEL/wsFEL.php");
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
