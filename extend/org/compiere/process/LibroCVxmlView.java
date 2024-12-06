/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
package org.compiere.process;

import java.io.File;
import java.math.*;
import java.sql.*;
import java.util.Calendar;
import java.util.logging.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *	libro compra ventas xml
 *	
 *  @author fabian aguilar
 *  @version $Id: LibroCVxml.java,v 1.2 2011/02/01 00:51:01 $
 */
public class LibroCVxmlView extends SvrProcess
{
	int org_ID =0;
	
	int period_ID=0;
	
	boolean p_TipoOperacion;
	
	String p_TipoLibro="";
	
	String p_TipoEnvio="";
	
	String p_where="";
	String p_where2="";
	
	String p_rut="";
	
	String p_ExportDir="";
	
	Timestamp datefrom=null;
	Timestamp dateto =null;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Org_ID"))
				org_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("C_Period_ID"))
				period_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("IsSOTrx"))
				p_TipoOperacion = "Y".equals(para[i].getParameter());
			else if (name.equals("TipoLibro"))
				p_TipoLibro = ((String)para[i].getParameter());
			else if (name.equals("TipoEnvio"))
				p_TipoEnvio = ((String)para[i].getParameter());
			else if (name.equals("ExportDir"))
				p_ExportDir = ((String)para[i].getParameter());
			else if (name.equals("Rut"))
				p_rut = ((String)para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 String mylog = new String();
        try
        {
        	 MPeriod Pe =MPeriod.get(getCtx(), period_ID);
             Calendar cal = Calendar.getInstance();
             cal.setTimeInMillis(Pe.getEndDate().getTime());
             int mes;
             mes = cal.get(cal.MONTH) +1;
             
             String periodoTributario=cal.get(cal.YEAR)+"-"+  (mes<10?"0"+mes:mes) ;
             
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "LibroCompraVenta", null);
            document.setXmlVersion("1.0");
           
            /*Element LibroCompraVenta = document.createElement("LibroCompraVenta");
            document.getDocumentElement().appendChild(LibroCompraVenta);*/
            Element EnvioLibro = document.createElement("EnvioLibro");
            EnvioLibro.setAttribute("ID", "IEV-"+periodoTributario);
            
            document.getDocumentElement().appendChild(EnvioLibro);
            Element Caratula = document.createElement("Caratula");
            EnvioLibro.appendChild(Caratula);
            //inicio caratula
            MOrg company = MOrg.get(getCtx(), org_ID);
            Element RutEmisorLibro = document.createElement("RutEmisorLibro");
            org.w3c.dom.Text rut = document.createTextNode((String)company.get_Value("Rut"));
            RutEmisorLibro.appendChild(rut);
            Caratula.appendChild(RutEmisorLibro);
            
            Element RutEnvia = document.createElement("RutEnvia");
            org.w3c.dom.Text rutE = document.createTextNode(p_rut);
            RutEnvia.appendChild(rutE);
            Caratula.appendChild(RutEnvia);
            
           
            Element PeriodoTributario = document.createElement("PeriodoTributario");// (AAAA-MM)
            org.w3c.dom.Text periodo = document.createTextNode(periodoTributario);
            PeriodoTributario.appendChild(periodo);
            Caratula.appendChild(PeriodoTributario);
            
            Timestamp today = new Timestamp(TimeUtil.getToday().getTimeInMillis());
            Element FchResol = document.createElement("FchResol");// AAAA-MM-DD
            org.w3c.dom.Text fecha = document.createTextNode(company.get_ValueAsString("FchResol") );
            FchResol.appendChild(fecha);
            Caratula.appendChild(FchResol);
            
            Element NroResol = document.createElement("NroResol");
            org.w3c.dom.Text nro = document.createTextNode(company.get_ValueAsString("NroResol"));
            NroResol.appendChild(nro);
            Caratula.appendChild(NroResol);
            
            Element TipoOperacion = document.createElement("TipoOperacion");
            org.w3c.dom.Text tipo = document.createTextNode(p_TipoOperacion?"VENTA":"COMPRA");
            TipoOperacion.appendChild(tipo);
            Caratula.appendChild(TipoOperacion);
            
            Element TipoLibro = document.createElement("TipoLibro");
            org.w3c.dom.Text tipoL = document.createTextNode(p_TipoLibro);
            TipoLibro.appendChild(tipoL);
            Caratula.appendChild(TipoLibro);
            
            Element TipoEnvio = document.createElement("TipoEnvio");
            org.w3c.dom.Text tipoE = document.createTextNode(p_TipoEnvio);
            TipoEnvio.appendChild(tipoE);
            Caratula.appendChild(TipoEnvio);
            
            //fin caratula
            String whereDoc="";
            if(p_TipoOperacion){
            	p_where=" and issotrx='Y' and ad_org_id="+org_ID;
            	p_where2 = " and i2.isSOTrx='Y' ";
            	whereDoc ="'ARI','ARC'";
            }
            else{
            	p_where =" and issotrx='N'  and ad_org_id="+org_ID;
            	p_where2 = " and i2.isSOTrx='N' ";
            	whereDoc="'API','APC'";
            }
            
            datefrom=Pe.getStartDate();
            dateto=Pe.getEndDate();
            
          //inicio ResumenSegmento
            if(p_TipoEnvio.equals("PARCIAL") || p_TipoEnvio.equals("AJUSTE"))
            	addResumenSegmento(period_ID, document, EnvioLibro,whereDoc);
          //fin ResumenSegmento
            	
	      //inicio Resumen periodo
	       if(p_TipoEnvio.equals("TOTAL") || p_TipoEnvio.equals("AJUSTE") || p_TipoEnvio.equals("FINAL"))
	    	   addResumenPeriodo(period_ID, document, EnvioLibro,whereDoc);
	                    
	            
	       //inicio detalle
	        if(!p_TipoEnvio.equals("FINAL"))
	        	addDetalle(period_ID, document, EnvioLibro,whereDoc );
	            	
            
            mylog = "firma";
            Element Firma = document.createElement("TmstFirma");
            org.w3c.dom.Text Ftext = document.createTextNode((new StringBuilder()).append(today.toString().substring(0, 10)).append("T").append(today.toString().substring(11, 19)).toString());
            Firma.appendChild(Ftext);
            EnvioLibro.appendChild(Firma);
            
            Element keycontainer = document.getDocumentElement();
            keycontainer.setAttribute("xmlns", "http://www.sii.cl/SiiDte" );
            keycontainer.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance" );
            keycontainer.setAttribute("version", "1.0" );
            keycontainer.setAttribute("xsi:schemaLocation", "http://www.sii.cl/SiiDte LibroCV_v10.xsd" );
            

            mylog = "archivo";
            String ExportDir = p_ExportDir;
            ExportDir = ExportDir.replace("\\", "/");
            javax.xml.transform.Source source = new DOMSource(document);
            javax.xml.transform.Result result = new StreamResult(new File(ExportDir, (new StringBuilder()).append("libroCV").append(".xml").toString()));
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
        }
		
		return "";
	}	//	doIt
	
	public void addResumenSegmento(int C_Period_ID, Document document,Element EnvioLibro , String whereDoc){
		String sql = "select doc.documentno,count(1),sum(i.totallines),"
					+" (select coalesce(sum(round(tax.taxamt,0)),0) from c_invoicetax tax"
					+" inner join c_invoice i2 on (i2.c_invoice_id=tax.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" inner join c_tax tt on (tax.c_tax_id=tt.c_tax_id and upper(tt.name) like '%IVA%')"
					+" where i2.docstatus!='DR' and doc2.documentno=doc.documentno"
					+" and i2.dateacct between ? and ?) as iva,"
					+" (select coalesce(sum(round(tax.taxamt,0)),0) from c_invoicetax tax"
					+" inner join c_invoice i2 on (i2.c_invoice_id=tax.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" inner join c_tax tt on (tax.c_tax_id=tt.c_tax_id and tt.istaxexempt='Y')"
					+" where i2.docstatus!='DR' and doc2.documentno=doc.documentno"
					+" and i2.dateacct between ? and ?) as exento"
					+" , sum(i.grandtotal)"
					+"  from c_invoice i"
					+"  inner join c_doctype doc on (i.c_doctype_id=doc.c_doctype_id)"
					+"  where i.docstatus!='DR'  and doc.docbasetype IN ("+whereDoc+")"
					+" and i.dateacct between ? and ? " + p_where
					+" group by doc.documentno";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setTimestamp(1, datefrom);
			pstmt.setTimestamp(2, dateto);
			pstmt.setTimestamp(3, datefrom);
			pstmt.setTimestamp(4, dateto);
			pstmt.setTimestamp(5, datefrom);
			pstmt.setTimestamp(6, dateto);
			rs = pstmt.executeQuery();
			
			 Element ResumenSegmento = document.createElement("ResumenSegmento");
	         EnvioLibro.appendChild(ResumenSegmento);
	            
			while (rs.next()){
				
				Element TotalesSegmento = document.createElement("TotalesSegmento");
				ResumenSegmento.appendChild(TotalesSegmento);
				
				Element TpoDoc = document.createElement("TpoDoc");
            	org.w3c.dom.Text tipoD = document.createTextNode(rs.getString(1));
            	TpoDoc.appendChild(tipoD);
            	TotalesSegmento.appendChild(TpoDoc);
            	
            	Element TotDoc = document.createElement("TotDoc");
            	org.w3c.dom.Text totD = document.createTextNode(rs.getString(2));
            	TotDoc.appendChild(totD);
            	TotalesSegmento.appendChild(TotDoc);
            	
            	
            	Element TotMntExe= document.createElement("TotMntExe");
            	org.w3c.dom.Text totE = document.createTextNode(rs.getString(5));
            	TotMntExe.appendChild(totE);
            	TotalesSegmento.appendChild(TotMntExe);
            	
            	Element TotMntNeto= document.createElement("TotMntNeto");
            	org.w3c.dom.Text totN = document.createTextNode(rs.getString(3));
            	TotMntNeto.appendChild(totN);
            	TotalesSegmento.appendChild(TotMntNeto);
            	
            	Element TotMntIVA= document.createElement("TotMntIVA");
            	org.w3c.dom.Text totI = document.createTextNode(rs.getString(4));
            	TotMntIVA.appendChild(totI);
            	TotalesSegmento.appendChild(TotMntIVA);
            	
            	Element TotMntTotal= document.createElement("TotMntTotal");
            	org.w3c.dom.Text tott = document.createTextNode(rs.getString(6));
            	TotMntTotal.appendChild(tott);
            	TotalesSegmento.appendChild(TotMntTotal);
            	
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.severe(e.getMessage());
		}
	}
	
	public void addResumenPeriodo(int C_Period_ID, Document document,Element EnvioLibro, String whereDoc){
		String sql = "select doc.documentno,count(1),sum(currencyConvert(i.totallines,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),"
					+" (select coalesce(round(sum(currencyConvert(tax.taxamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoicetax tax"
					+" inner join c_invoice i2 on (i2.c_invoice_id=tax.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" inner join c_tax tt on (tax.c_tax_id=tt.c_tax_id and upper(tt.name) LIKE '%IVA%')"
					+" where i2.docstatus not in ('DR','VO') and doc2.documentno=doc.documentno "+ p_where2 
					+" and i2.dateacct between ? and ? and i2.isactive='Y' and i2.ad_org_id="+org_ID +") as iva," //4 iva
					+" (select coalesce(round(sum(currencyConvert(tax.taxbaseamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoicetax tax"
					+" inner join c_invoice i2 on (i2.c_invoice_id=tax.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" inner join c_tax tt on (tax.c_tax_id=tt.c_tax_id )"
					+" where i2.docstatus not in ('DR','VO') and doc2.documentno=doc.documentno and tt.istaxexempt='Y' "+ p_where2
					+" and i2.dateacct between ? and ? and i2.isactive='Y' and i2.ad_org_id="+org_ID+") as exento" //5 exen
					+",( select coalesce(round(sum(currencyConvert(il.linenetamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoiceline il " +
							"inner join c_invoice i2 on (il.c_invoice_id=i2.c_invoice_id) " +
							"inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id) " +
							"where i2.dateacct between ? and ? and i2.isactive='Y'  and i2.ad_org_id="+org_ID + p_where2 +
							" and il.c_charge_id=1000059 and doc2.documentno=doc.documentno and i2.docstatus not in ('DR','VO')) as especificoGasolina"  //6
					+" , sum(i.grandtotal)," //7 total
					
					+" (select coalesce(round(sum(currencyConvert(tax.taxbaseamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoicetax tax"
					+" inner join c_invoice i2 on (i2.c_invoice_id=tax.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" inner join c_tax tt on (tax.c_tax_id=tt.c_tax_id and tt.istaxexempt='N')"
					+" where i2.docstatus not in ('DR','VO') and doc2.documentno=doc.documentno " + p_where2
					+" and i2.dateacct between ? and ? and i2.isactive='Y' and i2.ad_org_id="+org_ID+") as neto" //8 neto
					
					+",( select coalesce(round(sum(currencyConvert(il.linenetamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoiceline il " +
					"inner join c_invoice i2 on (il.c_invoice_id=i2.c_invoice_id) " +
					"inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id) " +
					"where i2.dateacct between ? and ? and i2.isactive='Y'  and i2.ad_org_id="+org_ID + p_where2 +
					" and il.c_charge_id=1000014 and doc2.documentno=doc.documentno and i2.docstatus not in ('DR','VO')) as especificoDiesel," //9 diesel
					
					
					+" (select coalesce(round(sum(currencyConvert(il.taxamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoiceline il"
					+" inner join c_invoice i2 on (i2.c_invoice_id=il.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" where i2.docstatus not in ('DR','VO') and doc2.documentno=doc.documentno and il.c_tax_id=1000004 "+ p_where2
					+" and i2.dateacct between ? and ? and i2.isactive='Y' and i2.ad_org_id="+org_ID +") as rdiesel," //10 taxrecuperable diesel
					
					+" (select coalesce(round(sum(currencyConvert(il.linenetamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoiceline il"
					+" inner join c_invoice i2 on (i2.c_invoice_id=il.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" where i2.docstatus not in ('DR','VO') and doc2.documentno=doc.documentno and il.c_tax_id=1000006 " + p_where2
					+" and i2.dateacct between ? and ? and i2.isactive='Y' and i2.ad_org_id="+org_ID +") as ndiesel," //11 tax norecuperable diesel
					
					+" (select coalesce(round(sum(currencyConvert(il.taxamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoiceline il"
					+" inner join c_invoice i2 on (i2.c_invoice_id=il.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" where i2.docstatus not in ('DR','VO') and doc2.documentno=doc.documentno and il.c_tax_id=1000008 " + p_where2
					+" and i2.dateacct between ? and ? and i2.isactive='Y' and i2.ad_org_id="+org_ID +") as rgasolina," //12 tax recuperable gasolina
					
					+" (select coalesce(round(sum(currencyConvert(il.linenetamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoiceline il"
					+" inner join c_invoice i2 on (i2.c_invoice_id=il.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" where i2.docstatus not in ('DR','VO') and doc2.documentno=doc.documentno and il.c_tax_id=1000007 " + p_where2
					+" and i2.dateacct between ? and ? and i2.isactive='Y' and i2.ad_org_id="+org_ID +") as ngasolina," //13 tax no recuperable gasolina
					
					+" (select coalesce(round(sum(currencyConvert(il.linenetamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoiceline il"
					+" inner join c_invoice i2 on (i2.c_invoice_id=il.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" where i2.docstatus not in ('DR','VO') and doc2.documentno=doc.documentno and il.c_tax_id=1000004 " + p_where2
					+" and i2.dateacct between ? and ? and i2.isactive='Y' and i2.ad_org_id="+org_ID +") as ndiesel2," //14 tax no recuperable diesel 2 
					
					+" (select coalesce(round(sum(currencyConvert(il.linenetamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),0) from c_invoiceline il"
					+" inner join c_invoice i2 on (i2.c_invoice_id=il.c_invoice_id)"
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" where i2.docstatus not in ('DR','VO') and doc2.documentno=doc.documentno and il.c_tax_id=1000008 "+ p_where2
					+" and i2.dateacct between ? and ? and i2.isactive='Y' and i2.ad_org_id="+org_ID +") as ngasolina2" //15 tax no recuperable gasolina2
					
					+"  from c_invoice i"
					+"  inner join c_doctype doc on (i.c_doctype_id=doc.c_doctype_id)"
					+"  where i.docstatus not in ('DR','VO') and i.isactive='Y'  and doc.docbasetype IN ("+whereDoc+")"
					+" and i.dateacct between ? and ? " +p_where +" and doc.documentno is not null";
						
					sql+=" group by doc.documentno";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
			
				pstmt.setTimestamp(1, datefrom);
				pstmt.setTimestamp(2, dateto);
				pstmt.setTimestamp(3, datefrom);
				pstmt.setTimestamp(4, dateto);
				pstmt.setTimestamp(5, datefrom);
				pstmt.setTimestamp(6, dateto);
				pstmt.setTimestamp(7, datefrom);
				pstmt.setTimestamp(8, dateto);
				pstmt.setTimestamp(9, datefrom);
				pstmt.setTimestamp(10, dateto);
				pstmt.setTimestamp(11, datefrom);
				pstmt.setTimestamp(12, dateto);
				pstmt.setTimestamp(13, datefrom);
				pstmt.setTimestamp(14, dateto);
				
				pstmt.setTimestamp(15, datefrom);
				pstmt.setTimestamp(16, dateto);
				pstmt.setTimestamp(17, datefrom);
				pstmt.setTimestamp(18, dateto);
				pstmt.setTimestamp(19, datefrom);
				pstmt.setTimestamp(20, dateto);
				pstmt.setTimestamp(21, datefrom);
				pstmt.setTimestamp(22, dateto);
				
				pstmt.setTimestamp(23, datefrom);
				pstmt.setTimestamp(24, dateto);
				
				rs = pstmt.executeQuery();
				
				Element ResumenPeriodo = document.createElement("ResumenPeriodo");
 	            EnvioLibro.appendChild(ResumenPeriodo);
			    boolean  entro = false;
				while (rs.next()){
					entro =true;
					Element TotalesPeriodo = document.createElement("TotalesPeriodo");
					ResumenPeriodo.appendChild(TotalesPeriodo);
	 	            
					String tipoDoc=rs.getString(1);
					Element TpoDoc = document.createElement("TpoDoc");
	            	org.w3c.dom.Text tipoD = document.createTextNode(tipoDoc);
	            	TpoDoc.appendChild(tipoD);
	            	TotalesPeriodo.appendChild(TpoDoc);
	            	
	            	String totaldocs = rs.getString(2);
	            	if(tipoDoc.trim().equals("919"))
	            		totaldocs = DB.getSQLValueString(get_TrxName(), "select sum(il.qtyinvoiced) from c_invoiceline il " +
	            				"inner join c_invoice i on (i.c_invoice_id=il.c_invoice_id) " +
	            				"inner join c_doctype doc on (i.c_doctype_id=doc.c_doctype_id) " +
	            				" where i.docstatus not in ('DR','VO') and i.isactive='Y' and doc.documentno='919'" +
	            				" and i.dateacct between ? and ? " +p_where,datefrom,dateto );
	            	
	            	Element TotDoc = document.createElement("TotDoc");
	            	org.w3c.dom.Text totD = document.createTextNode(totaldocs);
	            	TotDoc.appendChild(totD);
	            	TotalesPeriodo.appendChild(TotDoc);
	            	
	            	/* 
	            	Element TotAnulado = document.createElement("TotAnulado");//*
	            	org.w3c.dom.Text totA = document.createTextNode(" ");
	            	TotAnulado.appendChild(totA);
	            	ResumenPeriodo.appendChild(TotAnulado);*/
	            	
	            	BigDecimal exen=rs.getBigDecimal(5).setScale(0, BigDecimal.ROUND_HALF_EVEN);
	            	BigDecimal cargo_gasolina=rs.getBigDecimal(6).setScale(0, BigDecimal.ROUND_HALF_EVEN);
	            	BigDecimal cargo_diesel=rs.getBigDecimal(9).setScale(0, BigDecimal.ROUND_HALF_EVEN);
	            	BigDecimal r_diesel=rs.getBigDecimal(10).setScale(0, BigDecimal.ROUND_HALF_EVEN);
	            	BigDecimal n_diesel=rs.getBigDecimal(11).setScale(0, BigDecimal.ROUND_HALF_EVEN);
	            	BigDecimal r_gasolina=rs.getBigDecimal(12).setScale(0, BigDecimal.ROUND_HALF_EVEN);
	            	BigDecimal n_gasolina=rs.getBigDecimal(13).setScale(0, BigDecimal.ROUND_HALF_EVEN);
	            	BigDecimal n_diesel2=rs.getBigDecimal(14).setScale(0, BigDecimal.ROUND_HALF_EVEN);
	            	BigDecimal n_gasolina2=rs.getBigDecimal(15).setScale(0, BigDecimal.ROUND_HALF_EVEN);
	            	
	            	/*if(org_ID != 1000002){
		            	exen=exen.subtract(cargo_gasolina);
		            	exen=exen.subtract(cargo_diesel);
	            	}*/
	            	
	            	Element TotMntExe= document.createElement("TotMntExe");
	            	org.w3c.dom.Text totE = document.createTextNode(exen.toString());
	            	TotMntExe.appendChild(totE);
	            	TotalesPeriodo.appendChild(TotMntExe);
	            	

					BigDecimal mntneto = rs.getBigDecimal(8);
					//mntneto = mntneto.subtract(activoNeto);
	            	Element TotMntNeto= document.createElement("TotMntNeto");
	            	org.w3c.dom.Text totN = document.createTextNode(mntneto.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
	            	TotMntNeto.appendChild(totN);
	            	TotalesPeriodo.appendChild(TotMntNeto);
	            	
	            	BigDecimal mntiva = rs.getBigDecimal(4);
	            	//mntiva = mntiva.subtract(activoIva);
	            	Element TotMntIVA= document.createElement("TotMntIVA");
	            	org.w3c.dom.Text totI = document.createTextNode(mntiva.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
	            	TotMntIVA.appendChild(totI);
	            	TotalesPeriodo.appendChild(TotMntIVA);
	            	
	            	//compra de activos fijos inicio
	            	BigDecimal activoNeto = Env.ZERO;
	            	BigDecimal activoIva = Env.ZERO;
					if(!p_TipoOperacion)//compras
					{
						PreparedStatement pstmt3 = null;
		    			ResultSet rs3 = null;
		    			String sql3 = " select count(1) , sum(invl.linenetamt) as total , trunc(sum (invl.taxamt)) as iva " +
		    					"from C_InvoiceLine invl " +
		    					"Inner join C_Invoice inv on (invl.c_invoice_id=inv.c_invoice_id) " +
		    					"inner join C_DocType doc on (inv.C_Doctype_ID=doc.C_DocType_ID) " +
		    					" where inv.issotrx='N' and inv.docstatus in ('CO','CL') and invl.a_capvsexp = 'Cap' " +
		    					" and inv.dateacct between ? and ? and doc.Documentno=? and invl.a_createasset='Y' and inv.ad_org_id="+org_ID;
		    			try
		    			{
		    				pstmt3 = DB.prepareStatement(sql3, get_TrxName());
		    				pstmt3.setTimestamp(1, datefrom);
		    				pstmt3.setTimestamp(2, dateto);
		    				pstmt3.setString(3, tipoDoc);
		    				rs3 = pstmt3.executeQuery();
		    				while (rs3.next()){
		    					
		    					if(rs3.getString(1) == null || rs3.getInt(1)==0)
		    						continue;
		    					Object [] param = {datefrom,dateto,tipoDoc};
		    					int countDocs = DB.getSQLValue(get_TrxName(), "select count(1) from c_invoice i "+
									"inner join c_doctype doc on (i.c_doctype_id=doc.c_doctype_id) "+
									"where i.issotrx='N' and i.docstatus in ('CO','CL') and i.isactive='Y' "+
									"and exists (select * from c_invoiceline il where il.c_invoice_id=i.c_invoice_id "+
									"and il.a_capvsexp = 'Cap'  and il.a_createasset='Y') "+
									"and i.dateacct between ? and ? and doc.Documentno=? and i.ad_Org_ID="+org_ID, param);
		    					
		    					Element TotOpActivoFijo= document.createElement("TotOpActivoFijo");
		    	            	org.w3c.dom.Text totop = document.createTextNode(countDocs+"");
		    	            	TotOpActivoFijo.appendChild(totop);
		    	            	TotalesPeriodo.appendChild(TotOpActivoFijo);
		    	            	
		    					Element TotMntActivoFijo= document.createElement("TotMntActivoFijo");
		    	            	org.w3c.dom.Text totmaf = document.createTextNode(rs3.getBigDecimal(2).setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
		    	            	TotMntActivoFijo.appendChild(totmaf);
		    	            	TotalesPeriodo.appendChild(TotMntActivoFijo);
		    	            	
		    	            	Element TotMntIVAActivoFijo= document.createElement("TotMntIVAActivoFijo");
		    	            	org.w3c.dom.Text totimaf = document.createTextNode(rs3.getBigDecimal(3).setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
		    	            	TotMntIVAActivoFijo.appendChild(totimaf);
		    	            	TotalesPeriodo.appendChild(TotMntIVAActivoFijo);
		    	            	
		    	            	activoNeto = rs3.getBigDecimal(2);
		    	            	activoIva = rs3.getBigDecimal(3);
		    				}
		    				rs3.close();
							pstmt3.close();
							pstmt3 = null;
						}
						catch (Exception e)
						{
							log.severe(e.getMessage());
						}
					}
					//compra activos fijos fin
					
	            
	            	
	            	
	            	
	            	//algoritmo iva no recuperable inicio
	            	PreparedStatement pstmt2 = null;
	    			ResultSet rs2 = null;
	    			String sql2="select coalesce(sum(currencyConvert(il.taxamt,i2.C_Currency_ID,228,i2.DateAcct,i2.C_ConversionType_ID,i2.AD_Client_ID,i2.AD_Org_ID)),0),il.CodIVANoRec," +
	    				" count(1)" 
	    			+" from c_invoicetax tax" 
	    			+" inner join c_invoice i2 on (i2.c_invoice_id=tax.c_invoice_id) "
	    			+" inner join c_invoiceline il on (i2.c_invoice_id=il.c_invoice_id) "
					+" inner join c_doctype doc2 on (i2.c_doctype_id=doc2.c_doctype_id)"
					+" inner join c_tax tt on (tax.c_tax_id=tt.c_tax_id and upper(tt.name)='IVA NO RECUPERABLE')"
					+" where i2.docstatus!='DR' and i2.dateacct between ? and ? and i2.isactive='Y' " +
					"  and doc2.docbasetype IN ("+whereDoc+") and i2.ad_org_id="+org_ID;

	    			sql2+=" and doc2.documentno='"+tipoDoc+"'";
	    			
					sql2+= " group by il.CodIVANoRec";
					
					
	    			try
	    			{
	    				pstmt2 = DB.prepareStatement(sql2, get_TrxName());
	    				int count=0;
	    				pstmt2.setTimestamp(1, datefrom);
	    				pstmt2.setTimestamp(2, dateto);
	    				/*pstmt2.setTimestamp(3, datefrom);
	    				pstmt2.setTimestamp(4, dateto);*/
	    				//Element TotIVANoRec = null;
	    				rs2 = pstmt2.executeQuery();
	    				while (rs2.next()){
	    					
	    					if(rs2.getLong(1)==0)
	    						continue;
	    					
	    					//if(count==0){
	    					Element TotIVANoRec= document.createElement("TotIVANoRec");
	    		            	TotalesPeriodo.appendChild(TotIVANoRec);
	    						
	    					//	count++;
	    					//}
	    					
	    					
	    					
	    					int countDocs = rs2.getInt(3);
	    					
	    					
	    					
	    					if(rs2.getString(2)!=null && rs2.getString(2).length()>0){
	    						Object [] param = {rs2.getString(2),datefrom,dateto,tipoDoc};
	    						
	    						countDocs=DB.getSQLValue(get_TrxName(), "select count(1) "
	    								+"from c_invoice i "
	    								+"inner join c_doctype doc on (i.c_doctype_id=doc.c_doctype_id) "
	    								+"where exists (select * from c_invoiceline il  "
	    								+"inner join c_tax t on (il.c_tax_id=t.c_tax_id) "
	    								+"where upper(t.name)='IVA NO RECUPERABLE' and il.CodIVANoRec=? and il.c_invoice_id=i.c_invoice_id) "
	    								+"and i.dateacct between ? and ? and i.isactive='Y' "
	    								+"and i.docstatus!='DR' and doc.documentno=? and i.ad_org_id="+org_ID, param);
	    						
		    					Element CodIVANoRec= document.createElement("CodIVANoRec");
		    	            	org.w3c.dom.Text codivano = document.createTextNode(rs2.getString(2));
		    	            	CodIVANoRec.appendChild(codivano);
		    	            	TotIVANoRec.appendChild(CodIVANoRec);
	    					}
	    	            	
	    	            	Element TotOpIVANoRec= document.createElement("TotOpIVANoRec");
	    	            	org.w3c.dom.Text totopiva = document.createTextNode(countDocs+"");
	    	            	TotOpIVANoRec.appendChild(totopiva);
	    	            	TotIVANoRec.appendChild(TotOpIVANoRec);
	    	            	
	    	            	Element MntIVANoRec = document.createElement("TotMntIVANoRec");
	    	            	org.w3c.dom.Text mntivanorec = document.createTextNode(rs2.getBigDecimal(1).setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
	    	            	MntIVANoRec.appendChild(mntivanorec);
	    	            	TotIVANoRec.appendChild(MntIVANoRec);
	    					
	    				}

						rs2.close();
						pstmt2.close();
						pstmt2 = null;
					}
					catch (Exception e)
					{
						log.severe(e.getMessage());
					}
	            	//algoritmo iva no recuperable fin
					
					//impuesto especifico inicio
	            	if(!p_TipoOperacion){//compras
	            		
	            		BigDecimal norec= Env.ZERO;
	            		
	            		if(org_ID == 1000002 && (r_diesel.signum()>0 || r_gasolina.signum()>0) ){
	            			
		            		Element TotOtrosImp= document.createElement("TotOtrosImp");
			            	TotalesPeriodo.appendChild(TotOtrosImp);
			            	
			            	if(r_diesel.signum()>0){
				            	Element CodImp= document.createElement("CodImp");
		    	            	org.w3c.dom.Text codimp = document.createTextNode("29");
		    	            	CodImp.appendChild(codimp);
		    	            	TotOtrosImp.appendChild(CodImp);
		    	            	
		    	            	Element TotMntImp= document.createElement("TotMntImp");
		    	            	org.w3c.dom.Text totopiva = document.createTextNode(r_diesel.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
		    	            	TotMntImp.appendChild(totopiva);
		    	            	TotOtrosImp.appendChild(TotMntImp);
			            	}
	            		
			            	if( r_gasolina.signum()>0){
	            			
				            	Element CodImp2= document.createElement("CodImp");
		    	            	org.w3c.dom.Text codimp2 = document.createTextNode("35");
		    	            	CodImp2.appendChild(codimp2);
		    	            	TotOtrosImp.appendChild(CodImp2);
		    	            	
		    	            	Element TotMntImp2= document.createElement("TotMntImp");
		    	            	org.w3c.dom.Text totopiva2 = document.createTextNode(r_gasolina.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
		    	            	TotMntImp2.appendChild(totopiva2);
		    	            	TotOtrosImp.appendChild(TotMntImp2);
			            	}
	            		}
    	            	//---------------------------
    	            	if(norec.signum()>0 || n_gasolina.signum()>0 || n_diesel.signum()>0 || n_gasolina2.signum()>0 || n_diesel2.signum()>0){
    	            		norec = norec.add(n_gasolina);
    	            		norec = norec.add(n_diesel);
    	            		norec = norec.add(n_gasolina2);
    	            		norec = norec.add(n_diesel2);
    	            		norec = norec.subtract(r_gasolina);
    	            		norec = norec.subtract(r_diesel);
	    	            	Element TotImpSinCredito= document.createElement("TotImpSinCredito");
	    	            	org.w3c.dom.Text totsc = document.createTextNode(norec.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
	    	            	TotImpSinCredito.appendChild(totsc);
	    	            	TotalesPeriodo.appendChild(TotImpSinCredito);
    	            	}
    	            	
	            	}
	            	//impuesto especifico fin
	            	
	            	Element TotMntTotal= document.createElement("TotMntTotal");
	            	org.w3c.dom.Text tott = document.createTextNode(rs.getBigDecimal(7).setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
	            	TotMntTotal.appendChild(tott);
	            	TotalesPeriodo.appendChild(TotMntTotal);
	            	
				}
				
				if(!entro)//librosin movimientos
				{
					Element TotalesPeriodo = document.createElement("TotalesPeriodo");
					ResumenPeriodo.appendChild(TotalesPeriodo);
	 	            
					
					Element TpoDoc = document.createElement("TpoDoc");
	            	org.w3c.dom.Text tipoD = document.createTextNode(p_TipoOperacion?"33":"46");
	            	TpoDoc.appendChild(tipoD);
	            	TotalesPeriodo.appendChild(TpoDoc);
	            	
	            	Element TotDoc = document.createElement("TotDoc");
	            	org.w3c.dom.Text totD = document.createTextNode("0");
	            	TotDoc.appendChild(totD);
	            	TotalesPeriodo.appendChild(TotDoc);
	            	
	            	Element TotMntExe= document.createElement("TotMntExe");
	            	org.w3c.dom.Text totE = document.createTextNode("0");
	            	TotMntExe.appendChild(totE);
	            	TotalesPeriodo.appendChild(TotMntExe);
	            
	            	Element TotMntNeto= document.createElement("TotMntNeto");
	            	org.w3c.dom.Text totN = document.createTextNode("0");
	            	TotMntNeto.appendChild(totN);
	            	TotalesPeriodo.appendChild(TotMntNeto);
	          
	            	Element TotMntIVA= document.createElement("TotMntIVA");
	            	org.w3c.dom.Text totI = document.createTextNode("0");
	            	TotMntIVA.appendChild(totI);
	            	TotalesPeriodo.appendChild(TotMntIVA);
	            	
	            	Element TotMntTotal= document.createElement("TotMntTotal");
	            	org.w3c.dom.Text tott = document.createTextNode("0");
	            	TotMntTotal.appendChild(tott);
	            	TotalesPeriodo.appendChild(TotMntTotal);
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.severe(e.getMessage());
			}
				
	}
			
	public void addDetalle(int C_Period_ID, Document document,Element EnvioLibro, String whereDoc){
				String sql = "select documentno,folio,tasa,dateinvoiced,rut,name,iva,grandtotal,docbasetype," 
					+"documentname,exento,diesel,gasolina,c_invoice_id,rdiesel,rgasolina,ndiesel,ngasolina," 
					+"ndiesel2, ngasolina2 from rvofb_detailcvxml"
					+" where docbasetype IN ("+whereDoc+")"
					+" and dateacct between ? and ? "+p_where;
					if(p_TipoOperacion)
						sql+=" and documentno not in ('35','38','919','924') ";
					
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try
					{
						pstmt = DB.prepareStatement(sql, get_TrxName());
					
						pstmt.setTimestamp(1, datefrom);
						pstmt.setTimestamp(2, dateto);
						rs = pstmt.executeQuery();			
					        
						while (rs.next()){
							
							if(p_TipoOperacion)
								if(rs.getString(1).equals("35") || rs.getString(1).equals("38"))
									continue;
							
							int invoice_id=rs.getInt(14);
							
							Element Detalle = document.createElement("Detalle");
			 	            EnvioLibro.appendChild(Detalle);
			 	            
							Element TpoDoc = document.createElement("TpoDoc");
			            	org.w3c.dom.Text tipoD = document.createTextNode(rs.getString(1));
			            	TpoDoc.appendChild(tipoD);
			            	Detalle.appendChild(TpoDoc);
			            	
			            	if(rs.getString("docbasetype").equals("ARC") ){
			            	Element Emisor = document.createElement("Emisor");//*solo si es nota de credito debito recibida
			            	org.w3c.dom.Text emi = document.createTextNode("1");
			            	Emisor.appendChild(emi);
			            	Detalle.appendChild(Emisor);
			            	}
			            	
			            	Element NroDoc = document.createElement("NroDoc");
			            	org.w3c.dom.Text no = document.createTextNode(getNumbers(rs.getString("folio")));
			            	NroDoc.appendChild(no);
			            	Detalle.appendChild(NroDoc);
			            	
			            	Element TasaImp = document.createElement("TasaImp");
			            	org.w3c.dom.Text tasa = document.createTextNode(rs.getString("tasa"));
			            	TasaImp.appendChild(tasa);
			            	Detalle.appendChild(TasaImp);
			            	
			            	Element FchDoc = document.createElement("FchDoc");
			            	org.w3c.dom.Text fdoc = document.createTextNode(rs.getString("dateinvoiced").toString().substring(0, 10));
			            	FchDoc.appendChild(fdoc);
			            	Detalle.appendChild(FchDoc);
			            	
			            	Element RUTDoc = document.createElement("RUTDoc");
			            	org.w3c.dom.Text rutdoc = document.createTextNode(rs.getString("rut"));
			            	RUTDoc.appendChild(rutdoc);
			            	Detalle.appendChild(RUTDoc);
			            	
			            	if(rs.getString("documentname").toUpperCase().indexOf("ELECTRO")>-1){
			            	Element RznSoc = document.createElement("RznSoc");//solo docs no electronicos
			            	org.w3c.dom.Text razon = document.createTextNode(rs.getString("name"));
			            	RznSoc.appendChild(razon);
			            	Detalle.appendChild(RznSoc);
			            	}
			            	
			            	BigDecimal cargo_diesel = rs.getBigDecimal(12).setScale(0, BigDecimal.ROUND_HALF_EVEN);
			            	BigDecimal cargo_gasolina = rs.getBigDecimal(13).setScale(0, BigDecimal.ROUND_HALF_EVEN);
			            	BigDecimal r_diesel = rs.getBigDecimal(15).setScale(0, BigDecimal.ROUND_HALF_EVEN);
			            	BigDecimal r_gasolina = rs.getBigDecimal(16).setScale(0, BigDecimal.ROUND_HALF_EVEN);
			            	BigDecimal n_diesel = rs.getBigDecimal(17).setScale(0, BigDecimal.ROUND_HALF_EVEN);
			            	BigDecimal n_gasolina = rs.getBigDecimal(18).setScale(0, BigDecimal.ROUND_HALF_EVEN);
			            	BigDecimal n_diesel2 = rs.getBigDecimal(19).setScale(0, BigDecimal.ROUND_HALF_EVEN);
			            	BigDecimal n_gasolina2 = rs.getBigDecimal(20).setScale(0, BigDecimal.ROUND_HALF_EVEN);
			            	
			            	BigDecimal mntexe=rs.getBigDecimal(11);
			            	
			            	/*mntexe=mntexe.subtract(diesel); 
			            	mntexe=mntexe.subtract(gasolina); */
			            	
			            	Element MntExe = document.createElement("MntExe");
			            	org.w3c.dom.Text exe = document.createTextNode(mntexe.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
			            	MntExe.appendChild(exe);
			            	Detalle.appendChild(MntExe);
			            	
			            	BigDecimal mntneto = DB.getSQLValueBD(get_TrxName(), (new StringBuilder()).append("select COALESCE(SUM(currencyConvert(il.LineNetAmt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0) from C_InvoiceLine il  inner join c_invoice i on (il.c_invoice_id=i.c_invoice_id) inner join C_Tax t on (il.C_Tax_ID=t.C_Tax_ID) where t.istaxexempt='N' and i.C_Invoice_ID=").append(rs.getInt("C_Invoice_ID")).toString());
			            	//mntneto = mntneto.subtract(activoNeto);
							Element MntNeto = document.createElement("MntNeto");
			            	org.w3c.dom.Text neto = document.createTextNode(mntneto.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
			            	MntNeto.appendChild(neto);
			            	Detalle.appendChild(MntNeto);
			            	
			            	BigDecimal mntiva = rs.getBigDecimal("iva");
			            	//mntiva = mntiva.subtract(activoIva);
			            	Element MntIVA = document.createElement("MntIVA");
			            	org.w3c.dom.Text iva = document.createTextNode(mntiva.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
			            	MntIVA.appendChild(iva);
			            	Detalle.appendChild(MntIVA);
			            	
			            	//compra de activos fijos inicio
			            	BigDecimal activoNeto = Env.ZERO;
			            	BigDecimal activoIva = Env.ZERO;
							if(!p_TipoOperacion)//compras
							{
								PreparedStatement pstmt3 = null;
				    			ResultSet rs3 = null;
				    			String sql3 = " select  sum(currencyConvert(il.linenetamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)) as neto , sum (currencyConvert(il.taxamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)) as iva  " +
				    					"from C_InvoiceLine il inner join c_invoice i on (il.c_invoice_id=i.c_invoice_id) " +
				    					" Where il.c_invoice_id = ? and il.a_capvsexp = 'Cap' and il.a_createasset='Y' ";
				    			try
				    			{
				    				pstmt3 = DB.prepareStatement(sql3, get_TrxName());
				    				pstmt3.setInt(1, invoice_id);
				    				
				    				rs3 = pstmt3.executeQuery();
				    				while (rs3.next()){
				    					
				    					if(rs3.getString(1) == null)
				    						continue;
				    					
				    					Element MntActivoFijo= document.createElement("MntActivoFijo");
				    	            	org.w3c.dom.Text totmaf = document.createTextNode(rs3.getBigDecimal(1).setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
				    	            	MntActivoFijo.appendChild(totmaf);
				    	            	Detalle.appendChild(MntActivoFijo);
				    	            	
				    	            	activoNeto = rs3.getBigDecimal(1);
				    	            	activoIva = rs3.getBigDecimal(2);
				    	            	
				    	            	Element MntIVAActivoFijo= document.createElement("MntIVAActivoFijo");
				    	            	org.w3c.dom.Text totimaf = document.createTextNode(rs3.getBigDecimal(2).setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
				    	            	MntIVAActivoFijo.appendChild(totimaf);
				    	            	Detalle.appendChild(MntIVAActivoFijo);
				    	            	
				    	            	
				    				}
				    				rs3.close();
									pstmt3.close();
									pstmt3 = null;
								}
								catch (Exception e)
								{
									log.severe(e.getMessage());
								}
							}
							//compra de activos fijos fin
							
							        			            	
			            				            	
			            	
			            	//algoritmo iva no recuperable inicio
			            	PreparedStatement pstmt2 = null;
			    			ResultSet rs2 = null;
			    			String sql2="select coalesce(sum(currencyConvert(il.taxamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0),il.CodIVANoRec" 
			    			+" from  c_invoice i " 
			    			+" inner join c_invoiceline il on (il.c_invoice_id=i.c_invoice_id)"
							+" inner join c_tax tt on (il.c_tax_id=tt.c_tax_id and upper(tt.name)='IVA NO RECUPERABLE')"
							+" where i.c_invoice_id="+rs.getInt("c_invoice_id")
							+ " group by il.CodIVANoRec";
			    			try
			    			{
			    				pstmt2 = DB.prepareStatement(sql2, get_TrxName());
			    				int count=0;
			    				Element TotIVANoRec = null;
			    				rs2 = pstmt2.executeQuery();
			    				while (rs2.next()){
			    					
			    					if(count==0){
			    						TotIVANoRec= document.createElement("IVANoRec");
			    						Detalle.appendChild(TotIVANoRec);
			    						
			    						count++;
			    					}
			    					if(rs2.getString(2)!=null && rs2.getString(2).length()>0){
			    					Element CodIVANoRec= document.createElement("CodIVANoRec");
			    	            	org.w3c.dom.Text codivano = document.createTextNode(rs2.getString(2));
			    	            	CodIVANoRec.appendChild(codivano);
			    	            	TotIVANoRec.appendChild(CodIVANoRec);
			    					}
			    					
			    	            	Element MntIVANoRec = document.createElement("MntIVANoRec");
			    	            	org.w3c.dom.Text mntivanorec = document.createTextNode(rs2.getBigDecimal(1).setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
			    	            	MntIVANoRec.appendChild(mntivanorec);
			    	            	TotIVANoRec.appendChild(MntIVANoRec);
			    					
			    				}

								rs2.close();
								pstmt2.close();
								pstmt2 = null;
							}
							catch (Exception e)
							{
								log.severe(e.getMessage());
							}
			            	//algoritmo iva no recuperable fin
							
							//impuesto especifico inicio
			            	if(!p_TipoOperacion){//compras
			            		
			            		BigDecimal norec= Env.ZERO;
			            		
			            		if(org_ID == 1000002 && (r_diesel.signum()>0 || r_gasolina.signum()>0) ){
			            			
				            		Element TotOtrosImp= document.createElement("OtrosImp");
				            		Detalle.appendChild(TotOtrosImp);
					            	
					            	if(r_diesel.signum()>0){
						            	Element CodImp= document.createElement("CodImp");
				    	            	org.w3c.dom.Text codimp = document.createTextNode("29");
				    	            	CodImp.appendChild(codimp);
				    	            	TotOtrosImp.appendChild(CodImp);
				    	            	
				    	            	Element TasaImp2= document.createElement("TasaImp");
				    	            	org.w3c.dom.Text tasa2 = document.createTextNode("29.65");
				    	            	TasaImp2.appendChild(tasa2);
				    	            	TotOtrosImp.appendChild(TasaImp2);
				    	            	
				    	            	Element TotMntImp= document.createElement("MntImp");
				    	            	org.w3c.dom.Text totopiva = document.createTextNode(r_diesel.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
				    	            	TotMntImp.appendChild(totopiva);
				    	            	TotOtrosImp.appendChild(TotMntImp);
					            	}
			            		
					            	if( r_gasolina.signum()>0){
			            			
						            	Element CodImp2= document.createElement("CodImp");
				    	            	org.w3c.dom.Text codimp2 = document.createTextNode("35");
				    	            	CodImp2.appendChild(codimp2);
				    	            	TotOtrosImp.appendChild(CodImp2);
				    	            	
				    	            	Element TotMntImp2= document.createElement("MntImp");
				    	            	org.w3c.dom.Text totopiva2 = document.createTextNode(r_gasolina.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
				    	            	TotMntImp2.appendChild(totopiva2);
				    	            	TotOtrosImp.appendChild(TotMntImp2);
					            	}
			            		}
		    	            	//---------------------------
		    	            	if(norec.signum()>0 || n_gasolina.signum()>0 || n_diesel.signum()>0 || n_diesel2.signum()>0 || n_gasolina2.signum()>0){
		    	            		norec = norec.add(n_gasolina);
		    	            		norec = norec.add(n_diesel);
		    	            		norec = norec.add(n_diesel2);
		    	            		norec = norec.add(n_gasolina2);
		    	            		norec = norec.subtract(r_gasolina);
		    	            		norec = norec.subtract(r_diesel);
			    	            	Element TotImpSinCredito= document.createElement("MntSinCred");
			    	            	org.w3c.dom.Text totsc = document.createTextNode(norec.setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
			    	            	TotImpSinCredito.appendChild(totsc);
			    	            	Detalle.appendChild(TotImpSinCredito);
		    	            	}
		    	            	
			            	}
			            	//impuesto especifico fin
							
			            	Element MntTotal = document.createElement("MntTotal");
			            	org.w3c.dom.Text total = document.createTextNode(rs.getBigDecimal("grandtotal").setScale(0, BigDecimal.ROUND_HALF_EVEN).toString());
			            	MntTotal.appendChild(total);
			            	Detalle.appendChild(MntTotal);
						}
						rs.close();
						pstmt.close();
						pstmt = null;
					}
					catch (Exception e)
					{
						log.severe(e.getMessage());
					}
	}
	
	
	public String getNumbers(String in){
		
		String salida= new String();
		try{
			char list[]=in.toCharArray();
			for(int i=0; i<list.length;i++ )
				salida+=Integer.parseInt(Character.toString(list[i]));
			
		}
		catch(Exception e)
		{
			return salida;
		}
		
		return salida;
	}
}	//	CopyOrder