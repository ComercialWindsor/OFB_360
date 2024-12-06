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
import org.compiere.process.SvrProcess;
import org.metlife.process.CXFConnector;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 *	Generate XML Consolidated from Invoice (Generic) 
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ExportDTEInvoiceCG.java,v 1.2 19/05/2011 $
 */
public class SendMailAssignment extends SvrProcess
{	
	/** Properties						*/
	private Properties 		m_ctx;	
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		m_ctx = Env.getCtx();
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		String mysql="SELECT C_CampaignFollow_ID FROM C_CampaignFollow WHERE SendAssignMail = 'Y' ";
		int cant = 0;
		String IDS_follow = "0";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(mysql, get_TrxName());			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{				
				sendMailCFollow(rs.getInt("C_CampaignFollow_ID"));
				IDS_follow = IDS_follow + ","+rs.getInt("C_CampaignFollow_ID");
				cant++;				
			}
		}catch (Exception e)
		{
			log.severe("Error " + e.getMessage());
		}
		//actualizamos seguimientos usados
		DB.executeUpdate("UPDATE C_CampaignFollow SET SendAssignMail = 'N' WHERE C_CampaignFollow_ID IN ("+IDS_follow+")", get_TrxName());
		return "Se han enviado "+cant+ " correos";
	}	//	doIt
	public void sendMailCFollow (int ID_CFollow)
	{
		//ininoles mandamos correo
		X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), ID_CFollow, get_TrxName());
		if (cFollow.getAD_User_ID() > 0)
		{
			try
			{
				MUser user = new MUser(getCtx(), cFollow.getAD_User_ID(),get_TrxName());
				if(!user.getName().toUpperCase().contains("CLIENTE"))
				{						
					/*CallableStatement cst = DB.prepareCall("{call PA_LEADALLOCATION (?)}");
					cst.setInt(1, cFollow.get_ID());
					cst.execute();*/
					//se ocupa nuevo metodo para envio de correos 
					MBPartner bPart = new MBPartner(getCtx(), cFollow.getC_BPartner_ID(), get_TrxName());					
					//seteo variables a enviar
					String CustomerRUT = "99512160-3";
					String CustomerMail = user.getEMail();
					//seteamos variable file path a mano
					String comodin = "\\";
					String pathTemp = comodin+comodin+"mlfsoaqat01"+comodin+"templates"+comodin+"MFSMutuariaBienvenida"+comodin+"Muturia.jpg";				
					//end seteo
		    		String DocumentFilePath = pathTemp;
		    		String NombreCliente = "Nombre:" +bPart.getName();
					String msg = llamarMFSMutuariaAsignacion(CustomerRUT,CustomerMail,DocumentFilePath,NombreCliente);
					log.config(msg);
				}
			}
			catch (Exception e)
			{	
				log.log(Level.SEVERE, "No se pudo ejecutar proceso de correo", e);
			}
		}
		//ininoles end
	}
	
	private String llamarMFSMutuariaAsignacion(String CustomerRUT,String CustomerMail,String DocumentFilePath,
			String NombreCliente) throws Exception
    {	    	
    	String msgRet = "";
    	final String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.documentservice.metlife.cl/\">" +
    			"<soapenv:Header/> "+
    			"<soapenv:Body>"+
			    "<ws:sendCustomDocuments>"+
			    "<customDocumentsDescriptor>"+
			    "<CustomDocumentsDescriptor>"+
			    "<CustomerRUT>"+CustomerRUT+"</CustomerRUT>"+
			    "<CustomerMail>"+CustomerMail+"</CustomerMail>"+               
			    "<Template>MFSMutuariaNuevoLeads</Template>"+
			    "<BusinessLine>mutuaria</BusinessLine>"+
			    "<DocumentFilePath>"+DocumentFilePath+"</DocumentFilePath>"+ 
			    "<Encryption>false</Encryption>"+
			    "<CustomDocumentData>"+
			    "<DataName>NombreCliente</DataName>"+
			    "<DataValue>"+NombreCliente+"</DataValue>"+
			    "</CustomDocumentData>"+
			    "</CustomDocumentsDescriptor>"+
			    "</customDocumentsDescriptor>"+
			    "<userDescriptor>"+
			    "<UserRUT>1-9</UserRUT>"+
			    "<UserPassword>bluePrintsIT1</UserPassword>"+
			    "</userDescriptor>"+
			    "</ws:sendCustomDocuments>"+
			    "</soapenv:Body>"+
			    "</soapenv:Envelope>";

   			Source response = null;
   					
   			try
   			{				
   				final CXFConnector wsc = new CXFConnector();					
   				wsc.setSoapAction("http://ws.documentservice.metlife.cl/DocumentSenderService/getBusinessLinesResponse");
   				wsc.setRequest(request);
   				wsc.setBinding(SOAPBinding.SOAP11HTTP_BINDING);
   				wsc.setEndpointAddress("http://10.77.208.94:9080/documentService/DocumentSenderService");
   				wsc.setServiceName("DocumentSenderService");
   				wsc.setPortName("DocumentSenderPort");
   				wsc.setTargetNS("http://ws.documentservice.metlife.cl/");
   				wsc.executeConnector();
   				response = wsc.getResponse(); 
   				
   			}
   			catch(Exception e)
   			{
   				throw new Exception("No se ha podido establecer conexion con el Servicio de correo. Llamada:"+request+e.toString(),e);
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
