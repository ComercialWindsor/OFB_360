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
import java.util.Calendar;
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
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
/**
 *	Generate XML consolidated from Invoice 
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ExportDTEInvoiceCodGenerico.java,v 1.2 03/06/2014 $
 */
public class ExportDTEMInOutCGProvectis extends SvrProcess
{
	/** Properties						*/
	private Properties 		m_ctx;	
	private int p_M_InOut_ID = 0;

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
            Element Documento = document.createElement("Documento");
            document.getDocumentElement().appendChild(Documento);
            Documento.setAttribute("ID", (new StringBuilder()).append("DTE-").append(inOut.getDocumentNo()).toString());
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
            	IndTrl = null;
			}
            if ( IndTrl != null && IndTrl.length()>0)
            {
            	Element IndTraslado = document.createElement("IndTraslado");
                org.w3c.dom.Text iTraslado = document.createTextNode(IndTrl);
                IndTraslado.appendChild(iTraslado);
                IdDoc.appendChild(IndTraslado);         	
            }
            
            
            
            //ininoles nuevo campo termino de pago
            if (inOut.getC_Order_ID() > 0)
            {
            	MOrder order = new MOrder(inOut.getCtx(), inOut.getC_Order_ID(), inOut.get_TrxName());            	
            	MPaymentTerm pterm = new MPaymentTerm(inOut.getCtx(), order.getC_PaymentTerm_ID(), inOut.get_TrxName());
                Element PayTerm = document.createElement("TermPagoGlosa");
                org.w3c.dom.Text term = document.createTextNode(pterm.getName());
                PayTerm.appendChild(term);
                IdDoc.appendChild(PayTerm);            	
            }
                        
            //ininoles nuevo campo vendedor       
            
            //indicacion
            
            //ininoles nuevo campo descripcion cabecera                        
            /*Element HDescription = document.createElement("HeaderDescription");
            org.w3c.dom.Text Hdesc = document.createTextNode(getDescription()==null?" ":getDescription());
            HDescription.appendChild(Hdesc);
            IdDoc.appendChild(HDescription);*/
            //end ininoles
            
            //ininoles nuevo campo orden de venta            
            /*if (getC_Order_ID() > 0)
            {
            	Element SalesOrder = document.createElement("SalesOrder");
            	org.w3c.dom.Text SOrder = document.createTextNode(getC_Order().getDocumentNo());
            	SalesOrder.appendChild(SOrder);
            	IdDoc.appendChild(SalesOrder);
            }*/
            //end ininoles
                        
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
            MBPartner BP = new MBPartner(inOut.getCtx(), inOut.getC_BPartner_ID(), inOut.get_TrxName());
            MBPartnerLocation bloc = new MBPartnerLocation(inOut.getCtx(), inOut.getC_BPartner_Location_ID(), inOut.get_TrxName());
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
            org.w3c.dom.Text contacto = document.createTextNode(inOut.getAD_User_ID()>0?inOut.getAD_User().getName():" "); //nombre completo contacto
            ContactoRecep.appendChild(contacto);
            Receptor.appendChild(ContactoRecep);
            
            Element CorreoRecep = document.createElement("CorreoRecep");
            org.w3c.dom.Text corrRecep = document.createTextNode(inOut.getAD_User().getEMail()==null?" ":inOut.getAD_User().getEMail()); //mail del contacto
            CorreoRecep.appendChild(corrRecep);
            Receptor.appendChild(CorreoRecep);
            
            
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
            Element CmnaRecep = document.createElement("CmnaRecep");
	        org.w3c.dom.Text Cmna = document.createTextNode(bloc.getLocation(true).getAddress3()==null?" ":bloc.getLocation(true).getAddress3());
	        CmnaRecep.appendChild(Cmna);
	        Receptor.appendChild(CmnaRecep);
            
            Element CiudadRecep = document.createElement("CiudadRecep");
            org.w3c.dom.Text reg = document.createTextNode(bloc.getLocation(true).getC_City_ID()>0?MCity.get(inOut.getCtx(), bloc.getLocation(true).getC_City_ID()).getName():"Santiago");
            CiudadRecep.appendChild(reg);
            Receptor.appendChild(CiudadRecep);
            
            //ininoles nuevos campos pedidos por hernani
            Element transporte = document.createElement("Transporte");
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
            
            mylog = "Totales";
            Element Totales = document.createElement("Totales");
            Encabezado.appendChild(Totales);
            
            BigDecimal amountGrandT = Env.ZERO;            
            BigDecimal priceT = Env.ZERO;
            BigDecimal taxAmt = Env.ZERO;
            if (inOut.getC_Order_ID() > 0)
            {
            	//calculo de monto de la guia
            	MInOutLine iLines2[] = inOut.getLines();
            	for(int a = 0; a < iLines2.length; a++)
                {	
            		priceT = Env.ZERO;
            		taxAmt = Env.ZERO;
            		MInOutLine iLine = iLines2[a];
            		String nameCg = iLine.get_ValueAsString("ProductNameCG"); 
            		if (nameCg != null && nameCg.trim().length() > 0)
            		{
               			priceT = (BigDecimal)iLine.get_Value("PriceEntered_CG");   
            			BigDecimal qtyGC = (BigDecimal)iLine.get_Value("QtyGC");
                		priceT = priceT.multiply(qtyGC);
                		//se saca logica para impuestos ya que no cuadran montos entre lineas y total
                		/*if(iLine.getC_OrderLine().getC_Tax_ID() > 0)
                		{
                			if (iLine.getC_OrderLine().getC_Tax().getRate().compareTo(Env.ZERO) > 0)
                			{
                				taxAmt = priceT.multiply(iLine.getC_OrderLine().getC_Tax().getRate());
                				taxAmt = taxAmt.divide(Env.ONEHUNDRED);
                			}
                		}*/
                		//priceT = priceT.add(taxAmt);
                		amountGrandT = amountGrandT.add(priceT);
            		}
                }
            }
                       
            Element TasaIVA = document.createElement("TasaIVA");
	        org.w3c.dom.Text tiva = document.createTextNode("19");
	        TasaIVA.appendChild(tiva);
	        Totales.appendChild(TasaIVA);	        
	        
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
            	
            	String nameCg = iLine.get_ValueAsString("ProductNameCG");
            	if (nameCg != null && nameCg != "" && nameCg != " ")
        		{
            	
	                Element Detalle = document.createElement("Detalle");
	                Documento.appendChild(Detalle);	                
	                
	                Element NroLinDet = document.createElement("NroLinDet");
	                org.w3c.dom.Text line = document.createTextNode(Integer.toString(indLine));
	                NroLinDet.appendChild(line);
	                Detalle.appendChild(NroLinDet);
	                Element NmbItem = document.createElement("NmbItem");
	                String pname = nameCg;	                
	                org.w3c.dom.Text Item = document.createTextNode(pname);
	                NmbItem.appendChild(Item);
	                Detalle.appendChild(NmbItem);
	                
	                String descCG = iLine.get_ValueAsString("DescriptionCG");
	                if (descCG == null)
	                {
	                	descCG = " ";
	                }
	                Element DscItem = document.createElement("DscItem");
	                org.w3c.dom.Text desc = document.createTextNode(descCG==null?" ":descCG);
	                DscItem.appendChild(desc);
	                Detalle.appendChild(DscItem);
	                
	                /*Element QtyRef = document.createElement("QtyRef");
	                org.w3c.dom.Text qty = document.createTextNode(iLine.getQtyEntered().toString());
	                QtyRef.appendChild(qty);
	                Detalle.appendChild(QtyRef);*/                
	                
	                //ininoles unidad de medida
	                /*if (iLine.getM_Product_ID() > 0)
	                {	
	                	Element UomRef = document.createElement("UnmdItem");
	                    org.w3c.dom.Text uom = document.createTextNode(iLine.getProduct().getC_UOM().getName()==null?" ":iLine.getProduct().getC_UOM().getName());
	                    UomRef.appendChild(uom);
	                    Detalle.appendChild(UomRef);                	
	                }*/
	                
	                //ininoles end
	                //ininoles seteo de monto
	                BigDecimal mtoItem = Env.ZERO;
	                BigDecimal prcRefMnt = Env.ZERO;
	                BigDecimal qtyGC = Env.ZERO;
	                if(iLine.getC_OrderLine_ID() > 0)
	                {                	   
	                	prcRefMnt = (BigDecimal)iLine.get_Value("PriceEntered_CG");
	                	qtyGC = (BigDecimal)iLine.get_Value("QtyGC");
	                	mtoItem = prcRefMnt.multiply(qtyGC);
	                }
	                                
	                Element QtyItem = document.createElement("QtyItem");
	                org.w3c.dom.Text qt = document.createTextNode(qtyGC.setScale(0, 4).toString());
	                QtyItem.appendChild(qt);
	                Detalle.appendChild(QtyItem);
	                
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
                org.w3c.dom.Text ref = document.createTextNode(folioreferencia);
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
            mylog = "firma";
            Element Firma = document.createElement("TmstFirma");
            Timestamp today = new Timestamp(TimeUtil.getToday().getTimeInMillis());
            org.w3c.dom.Text Ftext = document.createTextNode((new StringBuilder()).append(today.toString().substring(0, 10)).append("T").append(today.toString().substring(11, 19)).toString());
            Firma.appendChild(Ftext);
            Documento.appendChild(Firma);
            
            //tag observacion solicitado por hernani
            Element Observacion = document.createElement("Observacion");
            org.w3c.dom.Text ObservacionTxt = document.createTextNode(inOut.getDescription()==null?" ":inOut.getDescription());
            Observacion.appendChild(ObservacionTxt);
            Documento.appendChild(Observacion);            
            
            mylog = "archivo";
            String ExportDir = (String)company.get_Value("ExportDir");
            ExportDir = ExportDir.replace("\\", "/");
            javax.xml.transform.Source source = new DOMSource(document);
            javax.xml.transform.Result result = new StreamResult(new File(ExportDir, (new StringBuilder()).append(inOut.getDocumentNo()).append(".xml").toString()));
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
        
        return "XML CG PROVECTIS Generated";
    }	
}	//	InvoiceCreateInOut
