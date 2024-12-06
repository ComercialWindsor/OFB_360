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
public class ExportDTEInvoiceCGProvectisR2 extends SvrProcess
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
		String msg=CreateXMLCG(inv);
		
		return msg;
	}	//	doIt
	
	public String CreateXMLCG(MInvoice invoice)
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
            /* se saca a peticion de hernani
            Element FchCancel = document.createElement("FchCancel");
            org.w3c.dom.Text cancel = document.createTextNode(invoice.getDateInvoiced().toString().substring(0, 10));
            FchCancel.appendChild(cancel);
            IdDoc.appendChild(FchCancel);
            */
            
            //ininoles calculo de fecha de vencimiento
            int cantdias = 0;
            Timestamp fchVencCal = invoice.getDateInvoiced();
            //primero calculamos los dias a sumar
            if (invoice.getC_PaymentTerm_ID() > 0)
            {
                MPaymentTerm tpago = new MPaymentTerm(invoice.getCtx(), invoice.getC_PaymentTerm_ID(), invoice.get_TrxName());
                cantdias = tpago.getNetDays();
            }                               
            if (cantdias > 0)
            {
            	Calendar calFchVenc = Calendar.getInstance();
                calFchVenc.setTimeInMillis(invoice.getDateInvoiced().getTime());     
            	calFchVenc.add(Calendar.DATE, cantdias);
            	fchVencCal = new Timestamp(calFchVenc.getTimeInMillis());
            }           
            
            Element FchVenc = document.createElement("FchVenc");
            org.w3c.dom.Text venc = document.createTextNode(fchVencCal.toString().substring(0, 10));
            FchVenc.appendChild(venc);
            IdDoc.appendChild(FchVenc);
            //ininoles end
            
            //ininoles nuevo campo termino de pago
            /*MPaymentTerm pterm = new MPaymentTerm(invoice.getCtx(), invoice.getC_PaymentTerm_ID(), invoice.get_TrxName());
            Element PayTerm = document.createElement("PayTerm");
            org.w3c.dom.Text term = document.createTextNode(pterm.getName());
            PayTerm.appendChild(term);
            IdDoc.appendChild(PayTerm);*/           
            
            
            //ininoles nuevo campo descripcion cabecera         
            /*
            Element HDescription = document.createElement("HeaderDescription");
            org.w3c.dom.Text Hdesc = document.createTextNode(invoice.getDescription()==null?" ":invoice.getDescription());
            HDescription.appendChild(Hdesc);
            IdDoc.appendChild(HDescription);
            //end ininoles
            */
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
          //ininoles nuevo campo vendedor provectis           
            int id_salesRep = 0;
            String CdgVendedor = " ";
            MUser salesUser  = null;
            if (invoice.get_ValueAsInt("SalesRepV_ID") > 0)
            {
            	id_salesRep = invoice.get_ValueAsInt("SalesRepV_ID");
            	salesUser = new MUser(invoice.getCtx(), id_salesRep,invoice.get_TrxName());
            	CdgVendedor = salesUser.getName();
            }
            else
            {
            	CdgVendedor = " ";
            }            
            Element SalesRep = document.createElement("CdgVendedor");
            org.w3c.dom.Text sales = document.createTextNode(CdgVendedor);
            SalesRep.appendChild(sales);
            IdDoc.appendChild(SalesRep);
            
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
            
            //ininoles nuevos campos geminis
            String StrContacto = " ";
            if (invoice.getAD_User_ID()>0)
            {
            	if (invoice.getAD_User().getName() != null )
            	{
            		StrContacto = invoice.getAD_User().getName();
            		if (invoice.getAD_User().getPhone() != null)            		
            		 StrContacto = StrContacto + " - " +invoice.getAD_User().getPhone();
            	}
            }
            Element ContactoRecep = document.createElement("Contacto");
            org.w3c.dom.Text contacto = document.createTextNode(invoice.getAD_User_ID()>0?StrContacto:" "); //nombre completo contacto
            ContactoRecep.appendChild(contacto);
            Receptor.appendChild(ContactoRecep);
            
            /*Element CorreoRecep = document.createElement("CorreoRecep");
            org.w3c.dom.Text corrRecep = document.createTextNode(invoice.getAD_User_ID()>0?invoice.getAD_User().getEMail():" "); //correo contacto
            CorreoRecep.appendChild(corrRecep);
            Receptor.appendChild(CorreoRecep);*/
            //ininoles
            
            
            Element DirRecep = document.createElement("DirRecep");
            org.w3c.dom.Text dirr = document.createTextNode(bloc.getLocation(true).getAddress1());
            DirRecep.appendChild(dirr);
            Receptor.appendChild(DirRecep);
            
	        Element CmnaRecep = document.createElement("CmnaRecep");
	        org.w3c.dom.Text Cmna = document.createTextNode(bloc.getLocation(true).getAddress3()==null?" ":bloc.getLocation(true).getAddress3());
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
            
            if (amountNeto != null && amountNeto.compareTo(Env.ZERO) > 0)
            {
            	Element MntNeto = document.createElement("MntNeto");
    	        org.w3c.dom.Text neto = document.createTextNode(amountNeto!=null?amountNeto.toString():"0");
    	        MntNeto.appendChild(neto);
    	        Totales.appendChild(MntNeto);
            }            
            if (amountex != null)
            {
            	Element MntExe = document.createElement("MntExe");
                org.w3c.dom.Text exe = document.createTextNode(amountex != null ? amountex.toString() : "0");
                MntExe.appendChild(exe);
                Totales.appendChild(MntExe);
            }
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
            
            //ininoles lineas agrupadas por campo codigo generico
            
            String MySqlCG = "select M_PRODUCT_ID, C_CHARGE_ID, C_TAX_ID, DESCRIPTION, name, " +
            		"QTYENTERED, PRICELIST, QTYINVOICED, PRICEACTUAL, LINENETAMT, M_Product_Name_ID "+            		
            		"from RVOFB_DetailXML where c_invoice_id = ? ";
            PreparedStatement pstmt = null;
    		ResultSet rs = null;
    		
    		try
    		{
    			pstmt = DB.prepareStatement(MySqlCG, invoice.get_TrxName());
    			pstmt.setInt(1, invoice.get_ID());
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
                    
                    //MTax tax=new MTax(invoice.getCtx() ,rs.getInt("C_TAX_ID"),invoice.get_TrxName() );
                    
                    //se saca a peticion de javier plos tag de IndExe Index exento
                    /*
                    if(tax.isTaxExempt()){
                    	 Element IndEx = document.createElement("IndExe");
                         org.w3c.dom.Text lineE = document.createTextNode("1");
                         IndEx.appendChild(lineE);
                         Detalle.appendChild(IndEx);	
                    }*/
                  
                    Element NroLinDet = document.createElement("NroLinDet");
                    org.w3c.dom.Text line = document.createTextNode(Integer.toString(lineInvoice));
                    NroLinDet.appendChild(line);
                    Detalle.appendChild(NroLinDet);
                    
                    //ininoles OFB se agrega codigo de producto a XML
                    if (rs.getInt("M_PRODUCT_ID") > 0)
                    {
                    	 /*Element CdgI = document.createElement("CdgItem");
                         Documento.appendChild(CdgI);
                         */
                    	
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
                    if(rs.getInt("M_PRODUCT_ID") > 0)                    	
                    	pname=produc.getName();
                    else if (rs.getInt("C_CHARGE_ID") > 0)
                    	pname=charge.getName();
                    else if (rs.getInt("M_Product_Name_ID")>0)
                    	pname = rs.getString("NAME")==null?" ":rs.getString("NAME");
                    else 
                    	pname = " ";
                    org.w3c.dom.Text Item = document.createTextNode(pname);
                    NmbItem.appendChild(Item);
                    Detalle.appendChild(NmbItem);
                                                 
                    /* sacado a peticion de hernani
                    Element DscItem = document.createElement("DscItem");
                    String pdescGen = ""; 
                    pdescGen= rs.getString("DESCRIPTION")==null?" ":rs.getString("DESCRIPTION");
                    org.w3c.dom.Text desc = document.createTextNode(pdescGen);
                    DscItem.appendChild(desc);
                    Detalle.appendChild(DscItem);
                    */
                    
                    //se modifica para que muestre cantidad 1 en los productos con codigos genericos
                    /*Element QtyRef = document.createElement("QtyRef");                    
                    int cant = rs.getInt("QTYENTERED");
                    org.w3c.dom.Text qty = document.createTextNode(Integer.toString(cant));                    
                    QtyRef.appendChild(qty);
                    Detalle.appendChild(QtyRef);
                    
                    Element PrcRef = document.createElement("PrcRef");
                    String plD = " ";
                    plD = rs.getBigDecimal("PRICELIST").toString();
                    org.w3c.dom.Text pl = document.createTextNode(plD);
                    PrcRef.appendChild(pl);
                    Detalle.appendChild(PrcRef);*/                    
                    
                    Element QtyItem = document.createElement("QtyItem");
                    String qtD = " ";
                    qtD = Integer.toString(rs.getInt("QTYINVOICED"));
                    org.w3c.dom.Text qt = document.createTextNode(qtD);
                    QtyItem.appendChild(qt);
                    Detalle.appendChild(QtyItem);
                    
                    Element PrcItem = document.createElement("PrcItem");
                    String paD = " ";
                    paD = rs.getBigDecimal("PRICELIST").setScale(0, 4).toString();                    
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
    		}
    		
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
            //nueva referencia nota de pedido
            if(invoice.get_ValueAsString("FolioRef2") != null 
            		&& invoice.get_ValueAsString("FolioRef2").trim().length() > 0)
            {
                Element Referencia = document.createElement("Referencia");
                Documento.appendChild(Referencia);
                Element NroLinRef = document.createElement("NroLinRef");
                org.w3c.dom.Text Nro = document.createTextNode("2");
                NroLinRef.appendChild(Nro);
                Referencia.appendChild(NroLinRef);
                Element TpoDocRef = document.createElement("TpoDocRef");
                org.w3c.dom.Text tpo = document.createTextNode("802");
                TpoDocRef.appendChild(tpo);
                Referencia.appendChild(TpoDocRef);
                Element FolioRef = document.createElement("FolioRef");
                org.w3c.dom.Text ref = document.createTextNode(invoice.get_ValueAsString("FolioRef2"));
                FolioRef.appendChild(ref);
                Referencia.appendChild(FolioRef);
                Timestamp fechaRef2 = (Timestamp)invoice.get_Value("DateOrdered2");
                if(fechaRef2 != null)
                {
	                Element FchRef = document.createElement("FchRef");
	                org.w3c.dom.Text fchref = document.createTextNode(fechaRef2.toString().substring(0, 10));
	                FchRef.appendChild(fchref);
	                Referencia.appendChild(FchRef);
                }
            }
            
            //fin referencia
            //Se crea tag adjuntos
            Element Adjuntos = document.createElement("Adjuntos");
            Documento.appendChild(Adjuntos);
            
            /*mylog = "firma";
            Element Firma = document.createElement("TmstFirma");
            Timestamp today = new Timestamp(TimeUtil.getToday().getTimeInMillis());
            org.w3c.dom.Text Ftext = document.createTextNode((new StringBuilder()).append(today.toString().substring(0, 10)).append("T").append(today.toString().substring(11, 19)).toString());
            Firma.appendChild(Ftext);
            Adjuntos.appendChild(Firma);*/
                        
            //descripicion de cabecera
            Element HDescription = document.createElement("Observacion");
            org.w3c.dom.Text Hdesc = document.createTextNode(invoice.getDescription()==null?" ":invoice.getDescription());
            HDescription.appendChild(Hdesc);
            Adjuntos.appendChild(HDescription);
            
            //campos en blanco solo para que funcione
            Element DatoAdj4 = document.createElement("DatoAdj4");
            org.w3c.dom.Text TextDatoAdj4 = document.createTextNode("Informacion adicional a incluir en el DOC");
            DatoAdj4.appendChild(TextDatoAdj4);
            Adjuntos.appendChild(DatoAdj4);
            
            Element DatoAdj5 = document.createElement("DatoAdj5");
            org.w3c.dom.Text TextDatoAdj5 = document.createTextNode("Informacion adicional a incluir en el DOC");
            DatoAdj5.appendChild(TextDatoAdj5);
            Adjuntos.appendChild(DatoAdj5);
            
            Element DatoAdj6 = document.createElement("DatoAdj6");
            org.w3c.dom.Text TextDatoAdj6 = document.createTextNode("Informacion adicional a incluir en el DOC");
            DatoAdj6.appendChild(TextDatoAdj6);
            Adjuntos.appendChild(DatoAdj6);
            
            Element copias = document.createElement("Copias");
            org.w3c.dom.Text TextCopias = document.createTextNode("3");
            copias.appendChild(TextCopias);
            Adjuntos.appendChild(copias);
            
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
        return "XML CG PROVECTIS Generated";
    }	
}	//	InvoiceCreateInOut
