package org.ofb.process;



import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPBinding;

import org.compiere.model.MProject;
import org.compiere.model.X_C_ProjectLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;



public class CallChileIndicaCanalizacionMideplan  extends SvrProcess{

	@Override
	protected void prepare() {

	}

	@Override
	protected String doIt() throws Exception {

		MProject project = new MProject(getCtx(), getRecord_ID(), get_TrxName());
		if(project.getPOReference()==null  || project.getPOReference().length()<2)
			return "Codigo BIP no valido";
		
		String bip=project.getPOReference().split("-")[0];
	    String digito= project.getPOReference().split("-")[1];

		final String request = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:CanalizacionMideplan\"> " +
		   "<soapenv:Header/>"+
		   "<soapenv:Body>"+
		      "<urn:CanalizacionMideplan soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"+
		         "<CodigoBIP xsi:type=\"xsd:string\">"+ bip +"</CodigoBIP>"+
		         "<DigitoVerificador xsi:type=\"xsd:string\"> "+ digito +"</DigitoVerificador>"+
		      "</urn:CanalizacionMideplan>"+
		   "</soapenv:Body>"+
		   "</soapenv:Envelope>";

		final Source response =
			execute(
					request, 
					SOAPBinding.SOAP11HTTP_BINDING, 
					"http://desarrollo.chileindica.cl/~test/chileindica/ws/CanalizacionMideplan/ConsultaBip.php",
					"CanalizacionMideplan", 
					"CanalizacionMideplanPortType", 
			"urn:CanalizacionMideplan");


		fillProjectInfo(project, response);
		
		return null;
	}

	private static final String nsSchema = "http://www.weather.gov/forecasts/xml/DWMLgen/schema/DWML.xsd";
	private static final String soapSchema = "http://schemas.xmlsoap.org/soap/envelope/";
	private static final String xsiSchema = "http://www.w3.org/2001/XMLSchema-instance";
	private static final String encodingStyle = "http://schemas.xmlsoap.org/soap/encoding/";


	private Source execute(final String request, final String binding, final String endpoint, final String service, final String port, final String ns) throws Exception {
		
		try
		{
		
			final CXFConnector wsc = new CXFConnector();
			wsc.setRequest(request);
			wsc.setBinding(binding);
			wsc.setEndpointAddress(endpoint);
			wsc.setServiceName(service);
			wsc.setPortName(port);
			wsc.setTargetNS(ns);
			wsc.executeConnector();
			final Source response = wsc.getResponse(); 
			return response;
		}
		catch(Exception e)
		{
			throw new Exception("No se ha podido establecer conexión con el Servicio Chile Indica Canalizacion mideplan, por favor verifique que " +
					"el servicio este habilitado");
		}
		
	}


	
	private void fillProjectInfo(MProject pj, Source response) //  parametro pj boton
	{ 

		try{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			SAXSource output = (SAXSource) response;
			Transformer tf = TransformerFactory.newInstance().newTransformer();
		
			DOMResult result = new DOMResult();
			tf.transform(output, result);
			Document doc = (Document) result.getNode();
			
			Node datos = findReturn(doc.getChildNodes().item(0)); // aca esta todo se ejecuta de un proyecto

			if(datos!=null){
				NodeList att = datos.getChildNodes(); // trae los hijos del return // comuna provincia region
				for(int x=0;x<att.getLength();x++)
				{
					log.config(att.item(x).getLocalName());
					if(att.item(x).getLocalName().equals("nombreIdi") || att.item(x).getNodeName().equals("nombreIdi"))
						pj.setName(att.item(x).getFirstChild().getNodeValue());
					
					if(att.item(x).getLocalName().equals("nombreSubSector") || att.item(x).getNodeName().equals("nombreSubSector"))
						pj.setNote(att.item(x).getFirstChild().getNodeValue());
					
					if(att.item(x).getLocalName().equals("benefAmbosSexos") || att.item(x).getNodeName().equals("benefAmbosSexos"))
						pj.set_CustomColumn("TotalBeneficiary", new BigDecimal(att.item(x).getFirstChild().getNodeValue()));
					
					if(att.item(x).getLocalName().equals("codigoBip") || att.item(x).getNodeName().equals("codigoBip"))
						pj.setValue(att.item(x).getFirstChild().getNodeValue());
					
					pj.set_CustomColumn("WService", "Y");
					
					String sqlo = "";
					String sqlo2 = "";
					String aux_sql= "";
					String aux_sql2="";
					if(att.item(x).getLocalName().equals("codigoComuna") || att.item(x).getNodeName().equals("codigoComuna"))
						aux_sql = att.item(x).getFirstChild().getNodeValue();     			
 
					sqlo="select c.c_city_id as c_city_id,p.c_province_id as c_province_id "+
						 "from adempiere.c_city c "+
						 "inner join adempiere.c_province p on (c.c_province_id=p.c_province_id) "+
						 "where c.code='"+aux_sql+"'";
					
						try
						{
							PreparedStatement pstmt = DB.prepareStatement(sqlo, null);
							ResultSet rs = pstmt.executeQuery();
							if (rs.next())
							{
								pj.set_CustomColumn("C_City_ID", rs.getInt("c_city_id"));
								pj.set_CustomColumn("C_Province_ID", rs.getInt("c_province_id"));
								pj.set_CustomColumn("MainGoal", pj.getC_Project_ID());

							}
							rs.close();
							pstmt.close();
						}
						catch (SQLException e)
						{
							log.log(Level.SEVERE, sqlo, e);
							
						}
					
						//--
						// inicio detalle
						
					if(att.item(x).getLocalName().equals("ProgramacionArray") || att.item(x).getNodeName().equals("ProgramacionArray"))
						
					{
						
						NodeList hijosprogramacion = att.item(x).getChildNodes();
						log.config("primer hijo programacion :"+hijosprogramacion.item(0).getLocalName());
						NodeList hijositem = hijosprogramacion.item(0).getChildNodes();
						
												
							for(int z=0;z<hijositem.getLength();z++)
							{
								log.config("Hijos Item: "+hijositem.item(z).getLocalName());
								   
								if(hijositem.item(z).getLocalName().equals("ItemProgramacionData") || hijositem.item(z).getLocalName().equals("ItemProgramacionData"))
								{
									//NodeList items = att.item(z).getChildNodes();
									NodeList items = hijositem.item(z).getChildNodes();
									int lineNo = 0;
									
									for(int a=0;a<items.getLength();a++)
									{
										log.config("item primer nivel: "+items.item(a).getLocalName());
										
										if(items.item(a).getLocalName().equals("item") || items.item(a).getLocalName().equals("item"))
										{
											X_C_ProjectLine pjl = null;
											NodeList items_detail = items.item(a).getChildNodes();
											
											lineNo += 10;
											pjl = new X_C_ProjectLine(getCtx(), 0, this.get_TrxName());
											pjl.setC_Project_ID(pj.getC_Project_ID());
											for(int b=0;b<items.getLength();b++)
											{														
												
												pjl.setLine(lineNo);
												log.config("item detalle: "+items_detail.item(b).getLocalName());	
												log.config("item detalle: "+items_detail.item(b).getFirstChild().getNodeValue());	
												
												if(items_detail.item(b).getLocalName().equals("codigoItem") || items_detail.item(b).getNodeName().equals("codigoItem"))
													aux_sql2 = items_detail.item(b).getFirstChild().getNodeValue();     	
													//pjl.setDescription(items_detail.item(b).getFirstChild().getNodeValue());
												sqlo2="select m_product_id "+
														 "from adempiere.m_product "+
														 "where VersionNo='"+aux_sql2+"'";
													
														try
														{
															PreparedStatement pstmt2 = DB.prepareStatement(sqlo2, null);
															ResultSet rs2 = pstmt2.executeQuery();
															if (rs2.next())
															{
																//pjl.set_CustomColumn("M_Product_ID", rs2.getInt("m_product_id"));
																pjl.setM_Product_ID( rs2.getInt("m_product_id"));
															}
															rs2.close();
															pstmt2.close();
														}
														catch (SQLException e)
														{
															log.log(Level.SEVERE, sqlo2, e);
														}
												
												
												if(items_detail.item(b).getLocalName().equals("monto") || items_detail.item(b).getNodeName().equals("monto"))
												pjl.set_CustomColumn("PlannedPrice", new BigDecimal(items_detail.item(b).getFirstChild().getNodeValue()));
											//	BigDecimal dd = new BigDecimal(items_detail.item(b).getFirstChild().getNodeValue());
											//	total.add(dd);
												
												if(items_detail.item(b).getLocalName().equals("duracion") || items_detail.item(b).getNodeName().equals("duracion"))
													pjl.set_CustomColumn("Duration", new BigDecimal(items_detail.item(b).getFirstChild().getNodeValue()));
																		
											}
											pjl.save();
										}
										
									}
									
								} // FIN IF ItemProgramacionData
								
								
								if(hijositem.item(z).getLocalName().equals("DatosProgramacionData") || hijositem.item(z).getLocalName().equals("DatosProgramacionData"))
								{
									//NodeList items = att.item(z).getChildNodes();
									NodeList itemsdata = hijositem.item(z).getChildNodes();
																		
									for(int ad=0;ad<itemsdata.getLength();ad++)
									{
										log.config("items DatosProgramacionData: "+itemsdata.item(ad).getLocalName());
										
										if(itemsdata.item(ad).getLocalName().equals("anoMoneda") || itemsdata.item(ad).getLocalName().equals("anoMoneda"))
											pj.set_CustomColumn("Year", itemsdata.item(ad).getFirstChild().getNodeValue());
											
										if(itemsdata.item(ad).getLocalName().equals("codigoEtapa") || itemsdata.item(ad).getLocalName().equals("codigoEtapa"))
											pj.set_CustomColumn("ProyectStage", itemsdata.item(ad).getFirstChild().getNodeValue());
											
										
										if(itemsdata.item(ad).getLocalName().equals("descripcion") || itemsdata.item(ad).getLocalName().equals("descripcion"))
											pj.set_CustomColumn("Description", itemsdata.item(ad).getFirstChild().getNodeValue());
										
										
									}
								}
								
		
							}//fin for
						
					}
					//--
					// fin detalle
					//--
					
					if(att.item(x).getLocalName().equals("codigoSeia") || att.item(x).getNodeName().equals("codigoSeia"))
					pj.set_CustomColumn("Codigo_SEIA", att.item(x).getFirstChild().getNodeValue());
					
					if(att.item(x).getLocalName().equals("nombreSeia") || att.item(x).getNodeName().equals("nombreSeia"))
					pj.set_CustomColumn("Nombre_SEIA", att.item(x).getFirstChild().getNodeValue());
					
					if(att.item(x).getLocalName().equals("codigoTipologia") || att.item(x).getNodeName().equals("codigoTipologia"))
						pj.set_CustomColumn("Typology", att.item(x).getFirstChild().getNodeValue());
					
					if(att.item(x).getLocalName().equals("codigoProceso") || att.item(x).getNodeName().equals("codigoProceso"))
						pj.set_CustomColumn("ProjectProcess", att.item(x).getFirstChild().getNodeValue());
					
					if(att.item(x).getLocalName().equals("vidaUtil") || att.item(x).getNodeName().equals("vidaUtil"))
						pj.set_CustomColumn("UseLifeYears",  new BigDecimal(att.item(x).getFirstChild().getNodeValue()));
					
					if(att.item(x).getLocalName().equals("benefAmbosSexos") || att.item(x).getNodeName().equals("benefAmbosSexos"))
						pj.set_CustomColumn("Benef_Ambos_Sexos",  new BigDecimal(att.item(x).getFirstChild().getNodeValue()));
					
					if(att.item(x).getLocalName().equals("beneficiariosHombres") || att.item(x).getNodeName().equals("beneficiariosHombres"))
						pj.set_CustomColumn("Beneficiarios_Hombres",  new BigDecimal(att.item(x).getFirstChild().getNodeValue()));
					
					if(att.item(x).getLocalName().equals("beneficiariosMujeres") || att.item(x).getNodeName().equals("beneficiariosMujeres"))
						pj.set_CustomColumn("Beneficiarios_Mujeres",  new BigDecimal(att.item(x).getFirstChild().getNodeValue()));
					
					if(att.item(x).getLocalName().equals("areaInfluencia") || att.item(x).getNodeName().equals("areaInfluencia"))
						pj.set_CustomColumn("Area_Influencia", att.item(x).getFirstChild().getNodeValue());
						
					
					//***
					//** etapa
					//**
					String sql_etapa = "";
					String aux_etapa= "";
					
					if(att.item(x).getLocalName().equals("codigoInstitucionEtapa") || att.item(x).getNodeName().equals("codigoInstitucionEtapa"))
						aux_etapa = att.item(x).getFirstChild().getNodeValue();   
										
					sql_etapa="select bp.c_bpartner_id, "+
							  "re.C_BPartnerRelation_ID "+
						      "from adempiere.C_BP_Relation re "+
							  "inner join adempiere.c_bpartner bp on (re.c_bpartner_id=bp.c_bpartner_id) "+
							  "where bp.code='"+aux_etapa+"'";
					
							try
							{
								PreparedStatement pstmtetapa = DB.prepareStatement(sql_etapa, null);
								ResultSet rsetapa = pstmtetapa.executeQuery();
								if (rsetapa.next())
								{
									pj.set_CustomColumn("C_BPartnerA_ID", rsetapa.getInt(1));
									pj.set_CustomColumn("C_BPartnerC_ID", rsetapa.getInt(2));

								}
								rsetapa.close();
								pstmtetapa.close();
							}
							catch (SQLException eee)
							{
								log.log(Level.SEVERE, sql_etapa, eee);
								
							}
					
					//***
					//** finetapa
					//**
					
					
					//
					// datos sector
					//
					String sql_sector = "";
					String aux_sector= "";
					if(att.item(x).getLocalName().equals("codigoSubSector") || att.item(x).getNodeName().equals("codigoSubSector"))
						aux_sector = att.item(x).getFirstChild().getNodeValue();     			
 
					sql_sector="select pm_sector_id,pm_subsector_id "+
						 "from adempiere.pm_subsector "+
						 "where value='"+aux_sector+"'";
					
						try
						{
							PreparedStatement pstmtsector = DB.prepareStatement(sql_sector, null);
							ResultSet rssector = pstmtsector.executeQuery();
							if (rssector.next())
							{
								pj.set_CustomColumn("PM_Sector_ID", rssector.getInt(1));
								pj.set_CustomColumn("PM_SubSector_ID", rssector.getInt(2));
								

							}
							rssector.close();
							pstmtsector.close();
						}
						catch (SQLException ee)
						{
							log.log(Level.SEVERE, sql_sector, ee);
							
						}
						
						//
						// findatos sector
						//	
				
				
				
				} // fin for return
				
				pj.save();
			}
			

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}


	public String getTagValue(String xml, String tagName)
	{
		String value = "";

		value = xml.substring(
				xml.indexOf("<" + tagName + ">") + new String("<" + tagName + ">").length(),
				xml.indexOf("</" + tagName + ">")
		);


		return value;
	}


	String nodeString = "";
	public String findRowSet(Node node,int nivel, String valor) {
		// Process node

		// If there are any children, visit each one
		NodeList list = node.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			// Get child node
			Node childNode = list.item(i);
			if(childNode.getNodeName().equals(("return")))
			{
				NodeList att = childNode.getChildNodes();
				for(int x=0;x<att.getLength();x++)
				{
					if(att.item(x).getLocalName().equals(valor) || att.item(x).getNodeName().equals(valor))
						nodeString = att.item(x).getNodeValue();
				}
				
			}

			findRowSet(childNode, nivel ++,valor);

		}
		return nodeString;
	}
	
	public Node findReturn(Node node) {
		Node value = null;
		NodeList list = node.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			// Get child node
			Node childNode = list.item(i);
			if(childNode.getNodeName().equals(("return")))
			{
				value = childNode;
				break;
			}

			value=findReturn(childNode);

		}
		return value;
	}

	public Document loadXMLFromString(String xml) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		return builder.parse(new ByteArrayInputStream(xml.getBytes()));
	}

	public Document string2DOM(String s)
	{
		Document tmpX=null;
		DocumentBuilder builder = null;
		try{
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}catch(javax.xml.parsers.ParserConfigurationException error){
			//coderror=10;
			//msgerror="Error crando factory String2DOM "+error.getMessage();
			return null;
		}
		try{
			tmpX=builder.parse(new ByteArrayInputStream(s.getBytes()));
		}catch(org.xml.sax.SAXException error){
			//coderror=10;
			//msgerror="Error parseo SAX String2DOM "+error.getMessage();
			return null;
		}catch(IOException error){
			//coderror=10;
			//msgerror="Error generando Bytes String2DOM "+error.getMessage();
			return null;
		}
		return tmpX;
	}
	
	public Node findReturnLine(Node node) {
		Node value = null;
		NodeList list = node.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			// Get child node
			Node childNode = list.item(i);
			log.config(childNode.getNodeName());
			if(childNode.getNodeName().equals(("ItemProgramacionData")))
			{
				value = childNode;
				break;
			}

			value=findReturn(childNode);

		}
		return value;
	}
}
