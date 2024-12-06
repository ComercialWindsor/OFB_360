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
package org.metlife.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.ws.soap.SOAPBinding;

import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.metlife.process.CXFConnector;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 *	Generate XML Consolidated from Invoice (Generic) 
 *	
 *  @author Italo Ni�oles ininoles
 *  @version $Id: ExportDTEInvoiceCG.java,v 1.2 19/05/2011 $
 */
public class SendMailMetlife extends SvrProcess
{	
	/** Properties						*/
	private Properties 		m_ctx;	
	private String p_Template = "";
	public String urlPdf = "";
	
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
			else if (name.equals("MailTemplate"))
				p_Template =  (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		m_ctx = Env.getCtx();
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		String mysql="SELECT C_Bpartner_ID, AD_User_ID, C_CampaignFollow_ID FROM C_CampaignFollow WHERE IsSelectedForMailing = 'Y' " +
				"AND Validation = 'N'";
		int cant = 0;
		String IDS_follow = "0";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(mysql, get_TrxName());			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MBPartner bPart = new MBPartner(getCtx(), rs.getInt("C_BPartner_ID"), get_TrxName());
				MUser user = new MUser(getCtx(), rs.getInt("AD_User_ID"), get_TrxName());
				//seteo variables a enviar
				String CustomerRUT = "99512160-3";;
				String CustomerMail = returnIaValue(bPart.get_ID(),1000009)!=""?returnIaValue(bPart.get_ID(),1000009):"Desconocido";
				String Template = p_Template;
				//seteamos variable file path a mano
				String comodin = "\\";
				String pathTemp = comodin+comodin+"mlfsoaqat01"+comodin+"templates"+comodin+"MFSMutuariaBienvenida"+comodin+"Muturia.jpg";				
				//end seteo
	    		String DocumentFilePath = pathTemp;
	    		String NombreCliente = "" +bPart.getName();
	    		String NombreEjecutivo = user.getDescription();
	    		if (NombreEjecutivo == null || NombreEjecutivo == "" || NombreEjecutivo == " ")
	    			NombreEjecutivo = user.getName();
	    		String Rut = ""+bPart.getValue();
	    		String Fechadenacimiento = ""+(returnIaValue(bPart.get_ID(),1000018)!=""?returnIaValue(bPart.get_ID(),1000018):"Desconocido");
	    		String Telefono = ""+(returnIaValue(bPart.get_ID(),1000004)!=""?returnIaValue(bPart.get_ID(),1000004):"Desconocido"); 
	    		
	    		String Valordelapropiedad = ""+ (returnIaValue(bPart.get_ID(),1000015)!=""?returnIaValue(bPart.get_ID(),1000015):"Desconocido"); 
	    		String Montodecredito = ""+(returnIaValue(bPart.get_ID(),1000016)!=""?returnIaValue(bPart.get_ID(),1000016):"Desconocido");
	    		//sacamos ceros de cadenas de montos
	    		int pos1 = Valordelapropiedad.indexOf('.');
	    		if(pos1 > 0)
	    			Valordelapropiedad = Valordelapropiedad.substring(0, pos1+3);	    		
	    		int pos2 = Montodecredito.indexOf('.');
	    		if(pos2 > 0)
	    			Montodecredito = Montodecredito.substring(0, pos2+3);
	    		
	    		String Cargoejecutivo = user.getTitle()!=null?user.getTitle():"Desconocido"; 
	    		String Fonofijo = user.getPhone()!=null?user.getPhone():"Desconocido";  
	    		String celular = user.getPhone2()!=null?user.getPhone2():"Desconocido"; 
				String CorreoEje = user.getEMail();
				//se setean nuevos campos solicitados por maud
				String TipoPropiedad = (returnIaValue(bPart.get_ID(),1000014)!=""?returnIaValue(bPart.get_ID(),1000014):"Desconocido");
				String Inmueble = (returnIaValue(bPart.get_ID(),1000012)!=""?returnIaValue(bPart.get_ID(),1000012):"Desconocido");
				if (CorreoEje == null || CorreoEje == "" || CorreoEje == " ")
					CorreoEje = "correo@metlife.cl";
				
				String msg = llamarMFSMutuariaBienvenida(CustomerRUT,CustomerMail,Template,
						DocumentFilePath,NombreCliente,NombreEjecutivo,Rut,Fechadenacimiento, 
						Telefono, Valordelapropiedad,Montodecredito, Cargoejecutivo, Fonofijo,
						celular, CorreoEje, TipoPropiedad, Inmueble);
				log.config(msg);
				IDS_follow = IDS_follow + ","+rs.getInt("C_CampaignFollow_ID");
				cant++;				
			}
		}catch (Exception e)
		{
			log.severe("Error " + e.getMessage());
		}
		//actualizamos seguimientos usados
		DB.executeUpdate("UPDATE C_CampaignFollow SET Validation = 'Y' WHERE C_CampaignFollow_ID IN ("+IDS_follow+")", get_TrxName());
		return "Se han enviado "+cant+ " correos";
	}	//	doIt
    private String llamarMFSMutuariaBienvenida(String CustomerRUT,String CustomerMail,String Template,
    		String DocumentFilePath,String NombreCliente,String NombreEjecutivo,String Rut,
    		String Fechadenacimiento, String Telefono, String Valordelapropiedad, 
    		String Montodecredito, String Cargoejecutivo, String Fonofijo, String celular, String CorreoEje,
    		String TipoPropiedad,String Inmueble) throws Exception //nuevos campos solicitados por maud
    {	    	
    	String msgRet = "";
    	//final String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.documentservice.metlife.cl/\">"+ //se cambia llamada por nuevo ws 
    	final String request = 	"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cus=\"http://customsenderws.ws.documentservice.metlife.cl/\">"+
		   "<soapenv:Header/>"+
		   "<soapenv:Body>"+
		   //"<ws:sendCustomDocuments>"+
		   "<cus:sendCustomDocuments>"+
		   "<customDocumentsDescriptor>"+
		   "<CustomDocumentsDescriptor>"+
		   "<CustomerRUT>"+CustomerRUT+"</CustomerRUT>"+
		   "<CustomerMail>"+CustomerMail+"</CustomerMail>"+               
		   "<Template>"+Template+"</Template>"+
		   "<BusinessLine>Mutuaria</BusinessLine>"+
		   "<DocumentFilePath>"+DocumentFilePath+"</DocumentFilePath>"+ 
		   "<Encryption>false</Encryption>"+
		   //ininoles nuevos campos solicitados por maud
		   "<SenderMail>"+CorreoEje+"</SenderMail>"+
           "<SenderName>"+NombreEjecutivo+"</SenderName>"+	
           //ininoles end
		   "<CustomDocumentData>"+
		   "<DataName>NombreCliente</DataName>"+
		   "<DataValue>"+NombreCliente+"</DataValue>"+
		   "</CustomDocumentData>"+
		   "<CustomDocumentData>"+
		   "<DataName>NombreEjecutivo</DataName>"+
		   "<DataValue>"+NombreEjecutivo+"</DataValue>"+                 
		   "</CustomDocumentData>"+
		   "<CustomDocumentData>"+
		   "<DataName>Rut</DataName>"+
		   "<DataValue>"+Rut+"</DataValue> "+
		   "</CustomDocumentData>"+
		   "<CustomDocumentData>"+                 
		   "<DataName>Fechadenacimiento</DataName>"+
		   "<DataValue>"+Fechadenacimiento+"</DataValue>"+
		   "</CustomDocumentData>"+    
		   "<CustomDocumentData>"+                                
		   "<DataName>Telefono</DataName>"+
		   "<DataValue>"+Telefono+"</DataValue>"+
		   "</CustomDocumentData>"+
		   "<CustomDocumentData>"+                                    
		   "<DataName>Valordelapropiedad</DataName>"+
		   "<DataValue>"+Valordelapropiedad+"</DataValue>"+
		   "</CustomDocumentData>"+
		   "<CustomDocumentData>"+                                                      
		   "<DataName>Montodecredito</DataName>"+
		   "<DataValue>"+Montodecredito+"</DataValue>"+
		   "</CustomDocumentData>"+
		   "<CustomDocumentData>"+                                                                        
		   "<DataName>Cargoejecutivo</DataName>"+
		   "<DataValue>"+Cargoejecutivo+"</DataValue>"+
		   "</CustomDocumentData>"+
		   "<CustomDocumentData>"+     
		   "<DataName>Fonofijo</DataName>"+
		   "<DataValue>"+Fonofijo+"</DataValue>"+
		   "</CustomDocumentData>"+
		   "<CustomDocumentData>"+     
		   "<DataName>celular</DataName>"+
		   "<DataValue>"+celular+"</DataValue>"+
		   "</CustomDocumentData>"+
		   "<CustomDocumentData>"+     
           "<DataName>CorreoEjecutivo</DataName>"+
           "<DataValue>"+CorreoEje+"</DataValue>"+                  
    	   "</CustomDocumentData>"+
    	   //nuevos campos en xml
    	   "<CustomDocumentData>"+     
		   "<DataName>TipoPropiedad</DataName>"+
		   "<DataValue>"+TipoPropiedad+"</DataValue>"+
		   "</CustomDocumentData>"+
		   "<CustomDocumentData>"+     
		   "<DataName>Inmueble</DataName>"+
		   "<DataValue>"+Inmueble+"</DataValue>"+
		   "</CustomDocumentData>"+    
		   //ininoles end
		   "</CustomDocumentsDescriptor>"+
		   "</customDocumentsDescriptor>"+
	       "<userDescriptor>"+
	       "<UserRUT>1-9</UserRUT>"+
		   "<UserPassword>bluePrintsIT1</UserPassword>"+
		   "</userDescriptor>"+
		   //"</ws:sendCustomDocuments>"+
		   "</cus:sendCustomDocuments>"+
		   "</soapenv:Body>"+
		   "</soapenv:Envelope>";

    		log.config(request);
   			Source response = null;
   					
   			try
   			{				
   			/*	final CXFConnector wsc = new CXFConnector();					
   				wsc.setSoapAction("http://ws.documentservice.metlife.cl/DocumentSenderService/getBusinessLinesResponse");
   				wsc.setRequest(request);
   				wsc.setBinding(SOAPBinding.SOAP11HTTP_BINDING);
   				wsc.setEndpointAddress("http://10.77.208.94:9080/documentService/DocumentSenderService");
   				wsc.setServiceName("DocumentSenderService");
   				wsc.setPortName("DocumentSenderPort");
   				wsc.setTargetNS("http://ws.documentservice.metlife.cl/");
   				wsc.executeConnector();
   				response = wsc.getResponse();*/ 
   				//nueva configuracion de llamada
   				final CXFConnector wsc = new CXFConnector();					
   				wsc.setSoapAction("http://customsenderws.ws.documentservice.metlife.cl/CustomSenderDocumentSenderService/getBusinessLinesResponse");
   				wsc.setRequest(request);
   				wsc.setBinding(SOAPBinding.SOAP11HTTP_BINDING);
   				wsc.setEndpointAddress("http://mlfsoaqat01:9080/documentService/CustomSenderDocumentSenderService");
   				wsc.setServiceName("CustomSenderDocumentSenderService");
   				wsc.setPortName("CustomSenderDocumentSenderPort");
   				wsc.setTargetNS("http://customsenderws.ws.documentservice.metlife.cl/");
   				wsc.executeConnector();
   				response = wsc.getResponse(); 
   				
   			}
   			catch(Exception e)
   			{
   				throw new Exception("No se ha podido establecer conexion con el Servicio de Correo");
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
    						if(att.item(x).getLocalName().equals("Description") || att.item(x).getNodeName().equals("Description"))
    							msgRet=att.item(x).getFirstChild().getNodeValue();
    					} // fin for return
    				}//FIN DATOS
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    		return msgRet;    			
    }  
    public Node findReturn(Node node) {
		Node value = null;
		NodeList list = node.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			// Get child node
			Node childNode = list.item(i);
			if(childNode.getNodeName().equals(("SendingStatus")))
			{
				value = childNode;
				break;
			}

			value=findReturn(childNode);

		}
		return value;
    }
    public String returnIaValue(int ID_BPartner,int ID_IArea)
    {
    	String iAValue_ID = "";

    	iAValue_ID = DB.getSQLValueString(get_TrxName(), "SELECT MAX(ia.value) " +
    			" FROM R_ContactInterest ci " +
    			" INNER JOIN R_InterestAreaValues ia ON (ci.R_InterestAreaValues_ID = ia.R_InterestAreaValues_ID) " +
    			" WHERE ci.C_Bpartner_ID = "+ID_BPartner+" AND ci.R_InterestArea_ID = "+ID_IArea);
    	if(iAValue_ID != null && iAValue_ID != "" && iAValue_ID != " ")
    		return iAValue_ID;
    	else
    		return "";
    }
	
}	
