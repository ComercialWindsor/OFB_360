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
import java.util.List;
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
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
/**
 *	Generate XML Baskakow 
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ExportDTEInvoiceNBaskakow.java,v 1.2 23/06/2014 $
 */
public class ExportDTEInvoiceNBaskakow extends SvrProcess
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
		MInvoice inv=new MInvoice(m_ctx,p_C_Invoice_ID,get_TrxName());
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
            document.setTextContent("text/xml");
            Attr atr = document.createAttribute("xmlns");
            atr.setValue("http://www.sii.cl/SiiDte");    
            
            Element Documento = document.createElement("Documento");
            document.getDocumentElement().appendChild(Documento);
            Documento.setAttribute("ID", (new StringBuilder()).append("DTE-").append(invoice.getDocumentNo()).toString());
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
            Element FchCancel = document.createElement("FchCancel");
            org.w3c.dom.Text cancel = document.createTextNode(invoice.getDateInvoiced().toString().substring(0, 10));
            FchCancel.appendChild(cancel);
            IdDoc.appendChild(FchCancel);
            Element FchVenc = document.createElement("FchVenc");
            org.w3c.dom.Text venc = document.createTextNode(invoice.getDateInvoiced().toString().substring(0, 10));
            FchVenc.appendChild(venc);
            IdDoc.appendChild(FchVenc);
            
          //ininoles nuevo campo termino de pago
            String StrFmaPago = "";
            MPaymentTerm pterm = new MPaymentTerm(invoice.getCtx(),invoice.getC_PaymentTerm_ID(), invoice.get_TrxName());
            if (pterm.getDescription().length()>0 && pterm.getDescription() != null)            	
            {
            	StrFmaPago = pterm.getDescription();
            }else
            {
            	StrFmaPago = pterm.getName();
            }            
            Element PayTerm = document.createElement("FmaPago");
            org.w3c.dom.Text term = document.createTextNode(StrFmaPago);
            PayTerm.appendChild(term);
            IdDoc.appendChild(PayTerm);            
            //ininoles nuevo campo vendedor            
            MUser salesUser = new MUser(invoice.getCtx(), invoice.getSalesRep_ID(), invoice.get_TrxName());
            Element SalesRep = document.createElement("CdgVendedor");
            org.w3c.dom.Text sales = document.createTextNode(salesUser.getName());
            SalesRep.appendChild(sales);
            IdDoc.appendChild(SalesRep);
            //ininoles nuevo campo descripcion cabecera                        
            Element HDescription = document.createElement("HeaderDescription");
            org.w3c.dom.Text Hdesc = document.createTextNode(invoice.getDescription()==null?"-":invoice.getDescription());
            HDescription.appendChild(Hdesc);
            IdDoc.appendChild(HDescription);
            //end ininoles
                        
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
            
            //ininoles se saca direccion de nuevo campo
            MBPartnerLocation obra = null;
            if (invoice.getC_Order_ID() > 0)
            {
            	MOrder SOrder = new MOrder(invoice.getCtx(), invoice.getC_Order_ID(), invoice.get_TrxName());
            	obra = new MBPartnerLocation(invoice.getCtx(), SOrder.getC_BPartner_Location_ID(), invoice.get_TrxName());
            }else
            {
            	obra = new MBPartnerLocation(invoice.getCtx(), invoice.getC_BPartner_Location_ID(), invoice.get_TrxName());
            }             
            MBPartnerLocation bloc = new MBPartnerLocation(invoice.getCtx(), invoice.getC_BPartner_Location_ID(), invoice.get_TrxName());
            //ininoles end
            
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
            
            //ininoles validacion contacto ya que no es usado en baskakow
            String nomContacto = invoice.getAD_User_ID()>0?invoice.getAD_User().getName():" ";
            if (nomContacto == null)
            	nomContacto = " ";
            //ininoles end
            
            Element ContactoRecep = document.createElement("Contacto");
            org.w3c.dom.Text contacto = document.createTextNode(nomContacto); //nombre completo contacto
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
            String CmnaRecepMsg = "";
            
            if(bloc.getLocation(true).getAddress2()!=null && bloc.getLocation(true).getAddress2().length()>0 )
            {
            	CmnaRecepMsg = bloc.getLocation(true).getAddress2();
            }
            else
            {
            	CmnaRecepMsg = "Desconocida";
            }
	        Element CmnaRecep = document.createElement("CmnaRecep2");
	        org.w3c.dom.Text Cmna = document.createTextNode(CmnaRecepMsg);
	        CmnaRecep.appendChild(Cmna);
	        Receptor.appendChild(CmnaRecep);
            
            Element CiudadRecep = document.createElement("CmnaRecep");
            org.w3c.dom.Text reg = document.createTextNode(bloc.getLocation(true).getC_City_ID()>0?MCity.get(invoice.getCtx(), bloc.getLocation(true).getC_City_ID()).getName():"Santiago");
            CiudadRecep.appendChild(reg);
            Receptor.appendChild(CiudadRecep);
            
            //se agrega nombre de obra ininoles
            Element ObraName = document.createElement("OName");
	        org.w3c.dom.Text oName = document.createTextNode(obra.getName());
	        ObraName.appendChild(oName);
	        Receptor.appendChild(ObraName);            
            //ininoles end
            
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
            	
				//solo toma lineas positivas
            	if (iLine.getLineNetAmt().compareTo(Env.ZERO)>=0)
            	{    				

	                Element Detalle = document.createElement("Detalle");
	                Documento.appendChild(Detalle);
	                
	                MTax tax=new MTax(invoice.getCtx() ,iLine.getC_Tax_ID(), invoice.get_TrxName() );
	                
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
	                Element NmbItem = document.createElement("NmbItem");
	                String pname="";
	                if(iLine.getProduct()!=null )
	                	pname=iLine.getProduct().getName();
	                else
	                	pname=iLine.getC_Charge().getName();
	                org.w3c.dom.Text Item = document.createTextNode(pname);
	                NmbItem.appendChild(Item);
	                Detalle.appendChild(NmbItem);
	                
	                Element DscItem = document.createElement("DscItem");
	                org.w3c.dom.Text desc = document.createTextNode(iLine.getDescription()==null?" ":iLine.getDescription());
	                DscItem.appendChild(desc);
	                Detalle.appendChild(DscItem);
	                
	                /*
	                Element QtyRef = document.createElement("QtyRef");
	                org.w3c.dom.Text qty = document.createTextNode(iLine.getQtyEntered().toString());
	                QtyRef.appendChild(qty);
	                Detalle.appendChild(QtyRef);
	                
	                //ininoles si precio de referencia es 0 que no aparesca el tag
	                if(iLine.getPriceList().compareTo(Env.ZERO) > 0)
	                {
	                	Element PrcRef = document.createElement("PrcRef");
	                	org.w3c.dom.Text pl = document.createTextNode(iLine.getPriceList().toString());
	                	PrcRef.appendChild(pl);
	                	Detalle.appendChild(PrcRef);
	                }*/
	                //ininoles end
	                
	                Element QtyItem = document.createElement("QtyItem");
	                org.w3c.dom.Text qt = document.createTextNode(iLine.getQtyInvoiced().toString());
	                QtyItem.appendChild(qt);
	                Detalle.appendChild(QtyItem);
	                Element PrcItem = document.createElement("PrcItem");
	                org.w3c.dom.Text pa = document.createTextNode(iLine.getPriceActual().setScale(0, 4).toString());
	                PrcItem.appendChild(pa);
	                Detalle.appendChild(PrcItem);
	                Element MontoItem = document.createElement("MontoItem");
	                org.w3c.dom.Text tl = document.createTextNode(iLine.getLineNetAmt().setScale(0, 4).toString());
	                MontoItem.appendChild(tl);
	                Detalle.appendChild(MontoItem);
            	}
            }
            
          //ininoles nuevos campos solicitados 
    		try
    		{
    			String TpoMovStr = invoice.get_ValueAsString("TpoMov");
    			String GlosaDRStr = invoice.get_ValueAsString("GlosaDR");
    			String TpoValorStr = invoice.get_ValueAsString("TpoValor");
    			BigDecimal AmtValorDR = DB.getSQLValueBD(invoice.get_TrxName(), "SELECT SUM(LineNetAmt) FROM C_InvoiceLine " +
    					" WHERE C_Invoice_ID = ? AND LineNetAmt < 0",	invoice.get_ID());
    			
    			if (TpoMovStr != null && TpoMovStr != "" && GlosaDRStr != null && GlosaDRStr != "" &&
    					TpoValorStr != null && TpoValorStr != "" && AmtValorDR != null && AmtValorDR.compareTo(Env.ZERO) < 0)
    			{
		    		mylog = "DscRcgGlobal";
		    		Element DscRcgGlobal = document.createElement("DscRcgGlobal");
		            Documento.appendChild(DscRcgGlobal);
		            
		    		int correlativo = 1;
		            Element NroLinDR = document.createElement("NroLinDR");
		            org.w3c.dom.Text NroLinDRTxt = document.createTextNode(Integer.toString(correlativo));
		            NroLinDR.appendChild(NroLinDRTxt);
		            DscRcgGlobal.appendChild(NroLinDR);
		            
		            Element TpoMov = document.createElement("TpoMov");
		            org.w3c.dom.Text TpoMovTxt = document.createTextNode(TpoMovStr);
		            TpoMov.appendChild(TpoMovTxt);
		            DscRcgGlobal.appendChild(TpoMov); 
		            
		            Element GlosaDR = document.createElement("GlosaDR");
		            org.w3c.dom.Text GlosaDRTxt = document.createTextNode(GlosaDRStr);
		            GlosaDR.appendChild(GlosaDRTxt);
		            DscRcgGlobal.appendChild(GlosaDR);
		            
		            Element TpoValor = document.createElement("TpoValor");
		            org.w3c.dom.Text TpoValorTxt = document.createTextNode(TpoValorStr);
		            TpoValor.appendChild(TpoValorTxt);
		            DscRcgGlobal.appendChild(TpoValor);
		            
		            Element ValorDR = document.createElement("ValorDR");
		            org.w3c.dom.Text ValorDRTxt = document.createTextNode(AmtValorDR.abs().setScale(0, 4).toString());
		            ValorDR.appendChild(ValorDRTxt);
		            DscRcgGlobal.appendChild(ValorDR);
    			}
    		}
    		catch(Exception e)
            {
    			log.severe("Error nuevas variables: NroLinDR,TpoMov,GlosaDR,TpoValor,ValorDR  " + e.getMessage());
            }    
    		//ininoles end

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
                org.w3c.dom.Text Nro = document.createTextNode("0001");
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
                String CodRefTxt = null;
                try {
                	CodRefTxt = invoice.get_ValueAsString("CodRef");
                }
                catch (Exception e) {
                	CodRefTxt = null;
				}
                if ( CodRefTxt != null && CodRefTxt.length()>0)
                {
                	Element CodRef = document.createElement("CodRef");
                    org.w3c.dom.Text codref = document.createTextNode(invoice.get_ValueAsString("CodRef")==null?"0":invoice.get_ValueAsString("CodRef"));                
                    CodRef.appendChild(codref);
                    Referencia.appendChild(CodRef);                	
                }                
            }
            //fin referencia
            mylog = "firma";
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
            
            //se guarda archivo para modificaciones                       
    		File file = new File(ExportDir, (new StringBuilder()).append(invoice.getDocumentNo()).append(".xml").toString());

    		//setear nuevos atributos de cabecera
    		Document docValid = builder.parse(file);
    		Element raiz = docValid.getDocumentElement();    		
    		raiz.setAttribute("xmlns", "http://www.sii.cl/SiiDte");
    		raiz.setAttribute("version", "1.0");
    		    		
    		//se guarda nuevo xml
            source = new DOMSource(docValid);
            result = new StreamResult(new File(ExportDir, (new StringBuilder()).append(invoice.getDocumentNo()).append(".xml").toString()));
            console = new StreamResult(System.out);
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.transform(source, result);
            transformer.transform(source, console);
        }
        catch(Exception e)
        {
            log.severe((new StringBuilder()).append("CreateXML: ").append(mylog).append("--").append(e.getMessage()).toString());
            return (new StringBuilder()).append("CreateXML: ").append(mylog).append("--").append(e.getMessage()).toString();
        }
        return "XML Baskakow Generated";
    }	
}	//	InvoiceCreateInOut
