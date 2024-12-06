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
package org.andes.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.model.MBank;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.ofb.model.OFBForward;
 
/**bibliotecas de metodo que descomprime*/ 
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *	Generate XML from MInOut
 *	
 *  @author Italo Niñoles Ininoles
 *  @version $Id: ExportDTEMInOut.java,v 1.2 05/09/2014 $
 */
public class AndesRedFileWriteCD4 extends SvrProcess
{
	/** Properties						*/
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private Timestamp  p_dateTrx;
	private int p_NumSec;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			
			if (name.equals("DateTrx"))
				p_dateTrx = (Timestamp)para[i].getParameter();
			else if (name.equals("correlativo"))
				p_NumSec = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		int contador = 0;
		String sqlFile = "SELECT FileName,FileNameNew,C_Bank_ID FROM C_BankAccount WHERE IsActive = 'Y' " +
				" AND FileName IS NOT NULL";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		pstmt = DB.prepareStatement(sqlFile,get_TrxName());
		rs = pstmt.executeQuery();
		String[ ] cuentasSec = new String[2];
		cuentasSec[0] = "2773101";
		cuentasSec[1] = "2773133";
		while (rs.next())
		{			
			MBank bank = new MBank(getCtx(), rs.getInt("C_Bank_ID"), get_TrxName());
			String comodin = "\\";
			int indiceCicloArchivo = 1;
			
			//generamos variables de cuenta
			if(bank.getDescription().compareToIgnoreCase("security") == 0)
				indiceCicloArchivo = 2;
			if(bank.getDescription().compareToIgnoreCase("BCI") == 0)
			{
				// se extrae archivo comprimido
				String BCIFile = OFBForward.PathFileBCICD4();
				if(BCIFile != null && BCIFile.trim().length() > 5)
				{
		        	File filenew = new File(BCIFile);
		    		String dirOut =OFBForward.PathDataIMacroCD4()+comodin;
		    		unZipFiles(filenew, dirOut);
				}
			}				
			for(int ia = 1; ia <= indiceCicloArchivo; ia++ )
			{
				ArrayList<String[]> datos=new ArrayList<String[]>();
				
				//obtenemos ruta
				String ruta = OFBForward.PathDataIMacroCD4();
		        //seteamos nueva ruta
		        String pFile = ruta+comodin+rs.getString("FileName").replace("--",Integer.toString(ia));
		        if(bank.getDescription().compareToIgnoreCase("BCI") == 0)
		        {
		        	//fecha para banco BCI
				    DateFormat dateFormatBCI = new SimpleDateFormat("MMdd");
			        Date BCIdate1 = new Date();
			        String fechaStrBCI = dateFormatBCI.format(BCIdate1);
		        	pFile = ruta+comodin+rs.getString("FileName").replace("-MESDIA-",fechaStrBCI);
		        }
		        if(bank.getDescription().compareToIgnoreCase("security") == 0)
		        	pFile = pFile.replace("-CCTA-", cuentasSec[ia-1]);
			    try 
			    {
			    	FileInputStream fis =new FileInputStream(pFile);
				    InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				    BufferedReader br = new BufferedReader(isr);
				    String linea=br.readLine();
				    int cantLines = 0;
				    while(linea!=null)
				    {
				        datos.add(linea.split(";"));
					    linea=br.readLine();
					    cantLines ++;
				    }
				    br.close();
				    isr.close();
				    fis.close();
				    log.config(datos.toString());
				    StringBuffer newLine = null;
				    DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
			        Date date1 = new Date();
			        String fechaStr = dateFormat.format(date1);		
				    //banco SANTANDER
			        if(bank.getDescription().compareToIgnoreCase("Santander") == 0)
			        {
			        	copiarArchivo(pFile, OFBForward.PathDataOutIMacroCD4()+comodin);
			        	contador++;
			        }
			        //banco BCI
			        else if(bank.getDescription().compareToIgnoreCase("BCI") == 0)
				    {
			    		
				        String numAccount = "-"; 
				        BufferedWriter bw = null;
				        int indice = 0;
					    for (int x = 0; x < cantLines; x++)
					    {	
						    //se crea archivo por cada cuenta				    	
				    		if(numAccount.compareToIgnoreCase(datos.get(x)[0].substring(5, 13)) != 0)
					    	{
					    		contador++;
					    		if(bw != null)
					    			bw.close();	
					    		indice++;
					    		//se genera nuevo archivo					        
							    String rutaArchivo = OFBForward.PathDataOutIMacroCD4()+comodin+rs.getString("FileNameNew").replace("FECHA", fechaStr).replace("--",rellenarCadena(Integer.toString(indice), 2, "0")).replace("-CCTA-", datos.get(x)[0].substring(5, 13));
						    	File archivo = new File(rutaArchivo);					    	
						    	//borrado de archivo si ya existe
						    	if(archivo.exists()) 
						    	      archivo.delete();
						    	bw = new BufferedWriter(new FileWriter(archivo));
					    	}
					    	newLine = new StringBuffer();
					    	newLine.append(datos.get(x)[0]);		    		
				    		bw.write(newLine.toString());
				    		bw.newLine();
					    	numAccount = datos.get(x)[0].substring(5, 13);
					    }
					    bw.close();				    
				    }		     
			        else if(bank.getDescription().compareToIgnoreCase("de Chile") == 0)
			        {
			        	copiarArchivo(pFile, OFBForward.PathDataOutIMacroCD4()+comodin);
			        	contador++;
			        }
			        //banco BICE
			        else if(bank.getDescription().compareToIgnoreCase("Bice") == 0)
				    {
					    //se genera nuevo archivo
					    String rutaArchivo = OFBForward.PathDataOutIMacroCD4()+comodin+rs.getString("FileNameNew").replace("FECHA", fechaStr);
				    	File archivo = new File(rutaArchivo);
				    	BufferedWriter bw;
				    	//fecha de dia habil anterior para comparar despues
				    	DateFormat dateFormatBice = new SimpleDateFormat("yyyyMMdd");				    				    	
				        String fechaStrBice = dateFormatBice.format(p_dateTrx);		
				    	//borrado de archivo si ya existe
				    	if(archivo.exists()) 
				    	      archivo.delete();
				    	bw = new BufferedWriter(new FileWriter(archivo));
					    for (int x = 0; x < cantLines; x++)
					    {
					    	newLine = new StringBuffer();				    	
					    	if(x == 0)	//linea 1 queda igual
					    	{
						    	newLine.append(datos.get(x)[0].trim());
						    	bw.write(newLine.toString());
						    	bw.newLine();
					    	}
					    	else if(x == cantLines-2)	//ultima linea queda igual
					    	{
						    	newLine.append(datos.get(x)[0].trim());
						    	bw.write(newLine.toString());
						    	bw.newLine();
					    	}
					    	else if(x == cantLines-1)	//ultima linea vacia no se agrega
						    	log.config("Linea en blanco no agregada");
					    	else	//modificaciones a linea 2 en adelante
					    	{
					    		//se agregan lineas solo si son de la fecha de hoy
					    		if(fechaStrBice.compareTo(datos.get(x)[0].trim().substring(7, 15)) == 0)
					    		{
					    			if(datos.get(x)[0].length() < 84)
						    		{
					    				int dif = 84-datos.get(x)[0].length();
					    				newLine.append(datos.get(x)[0].trim().substring(0, 42));
					    				newLine.append(datos.get(x)[0].trim().substring(43, 49));
							    		newLine.append(rellenarCadena(datos.get(x)[0].trim().substring(49, 57),16,"0"));
							    		newLine.append(datos.get(x)[0].trim().substring(57, 84-dif));
							    		bw.write(newLine.toString());
								    	bw.newLine();
						    		}
						    		else if(datos.get(x)[0].length() == 84)
						    		{	
						    			newLine.append(datos.get(x)[0].trim().substring(0, 84));
						    			bw.write(newLine.toString());
								    	bw.newLine();
						    		}
						    		else
						    		{
						    			newLine.append(datos.get(x)[0].trim().substring(0, 48));
							    		newLine.append(rellenarCadena(datos.get(x)[0].trim().substring(49, 49+14),22,"0"));
							    		newLine.append(rellenarCadena(datos.get(x)[0].trim().substring(76, 77),14," "));
							    		bw.write(newLine.toString());
								    	bw.newLine();
						    		}
					    		}
					    	}
					    	//end Banco Bice				    	
					    }
					    bw.close();
					    contador++;
				    }
				    else if(bank.getDescription().compareToIgnoreCase("Estado") == 0) // banco estado
				    {   
					    String numAccount = "-"; 
				        BufferedWriter bw = null;
				        int indice = 0;
				        boolean flag = false;
					    for (int x = 0; x < cantLines; x++)
					    {	
					        //se crea archivo por cada cuenta				    	
					    	if(numAccount.compareToIgnoreCase(datos.get(x)[0].substring(0, 11)) != 0)
					    	{
					    		contador++;
					    		if(bw != null)
					    			bw.close();	
					    		indice++;
					    		//se genera nuevo archivo					        
							    String rutaArchivo = OFBForward.PathDataOutIMacroCD4()+comodin+rs.getString("FileNameNew").replace("FECHA", fechaStr).replace("--",rellenarCadena(Integer.toString(indice), 2, "0")).replace("-CCTA-",datos.get(x)[0].substring(0,11));
						    	File archivo = new File(rutaArchivo);					    	
						    	//borrado de archivo si ya existe
						    	if(archivo.exists()) 
						    	      archivo.delete();
						    	bw = new BufferedWriter(new FileWriter(archivo));
						    	flag = true;
					    	}
					    	newLine = new StringBuffer();
					    	String lineStr = datos.get(x)[0];
					    	if(flag)
					    	{
					    		//ininoles dia sera reemplazado por fecha de parametro					    	
					    		DateFormat dateFormatHead = new SimpleDateFormat("ddMMyy");
					            Date dateHead = new Date();
					            dateHead.setTime(p_dateTrx.getTime());
					            String fechaStrHead = dateFormatHead.format(dateHead);
					    		lineStr = datos.get(x)[0].substring(0, 33)+rellenarCadena(fechaStrHead, 6, "0")
					    			+datos.get(x)[0].substring(39,datos.get(x)[0].length());				    		
					    	}
					    	newLine.append(lineStr);		    		
				    		bw.write(newLine.toString());
				    		bw.newLine();
					    	numAccount = datos.get(x)[0].substring(0, 11);
					    	flag = false;
					    }
					    bw.close();		
					    contador++;
				    }
				    else if(bank.getDescription().compareToIgnoreCase("Corpbanca") == 0) //corpbanca
				    {
					    //se genera nuevo archivo
					    String rutaArchivo = OFBForward.PathDataOutIMacroCD4()+comodin+rs.getString("FileNameNew").replace("FECHA", fechaStr);
				    	File archivo = new File(rutaArchivo);
				    	BufferedWriter bw;
				    	//fecha de hoy para comparar despues
				    	/*DateFormat dateFormatCB = new SimpleDateFormat("yyyyMMdd");
				        String fechaStrCB = dateFormatCB.format(date1);*/		
				    	//borrado de archivo si ya existe
				    	if(archivo.exists()) 
				    	      archivo.delete();
				    	bw = new BufferedWriter(new FileWriter(archivo));
				    	String numAccount = "";
				    	String numCartola = "";
				    	String referencia= "+";
					    for (int x = 4; x < cantLines; x++)
					    {
					    	//guardamos numero de cuenta
					    	if(x == 4)
					    		numAccount = datos.get(x)[1].trim();
					    	//guardamos numero de cartola
					    	if(x == 5)
					    		numCartola = datos.get(x)[1].trim();
					    	//se agregan las lineas
					    	if(x >= 10)
					    	{
					    		//tipo de movimiento
					    		if(datos.get(x)[0] != null && !datos.get(x)[0].trim().toUpperCase().contains("SIN MOV"))
						    	{
						    		String movType = "4";
						    		String signoSaldo = "+";
						    		if(datos.get(x)[1].trim().toUpperCase().contains("CHEQUE"))
						    			movType = "4";
						    		else if(datos.get(x)[4] != null && datos.get(x)[4].trim().length() > 0)
						    			movType = "3";
						    		else if(datos.get(x)[5] != null && datos.get(x)[5].trim().length() > 0)
						    			movType = "2";
						    		//end
						    		//signo saldo
						    		if(datos.get(x)[6] != null && datos.get(x)[6].trim().contains("-"))
						    			signoSaldo = "-";					    		
						    		//end
						    		//referencia					    		
						    		referencia=rellenarCadena(datos.get(x)[2], 9, "0");					    		
						    		//end
						    		String dateLine = datos.get(x)[0].substring(6,10)+datos.get(x)[0].substring(3,5)+datos.get(x)[0].substring(0,2);
							    	newLine = new StringBuffer();			
						    		newLine.append("027001CLP"+numAccount.substring(6,numAccount.length())+dateLine);
						    		//newLine.append(numCartola);
						    		newLine.append(rellenarCadena(Integer.toString(p_NumSec), 7, "0"));
						    		newLine.append(referencia);
						    		newLine.append(dateLine);
						    		newLine.append(codigoCorpBanca(datos.get(x)[1].trim().toUpperCase(), movType));
						    		newLine.append(movType);
						    		newLine.append(rellenarCadenaDerecha(datos.get(x)[1].trim(),30," "));
						    		newLine.append(rellenarCadena(datos.get(x)[4].replace("$","").replace(".","").trim(),13,"0"));
						    		newLine.append("00");
						    		newLine.append(rellenarCadena(datos.get(x)[5].replace("$","").replace(".","").trim(),13,"0"));
						    		newLine.append("00");
						    		newLine.append(rellenarCadena(datos.get(x)[6].replace("$","").replace(".","").trim(),13,"0"));
						    		newLine.append("00");
						    		newLine.append(signoSaldo);
						    		newLine.append(rellenarCadena(" ", 8, " "));
						    		bw.write(newLine.toString());
							    	bw.newLine();
						    	}
					    	}
					    }
					    bw.close();
					    contador++;
				    }
				    else if(bank.getDescription().compareToIgnoreCase("security") == 0) //security
				    {
					    //se genera nuevo archivo
					    String rutaArchivo = OFBForward.PathDataOutIMacroCD4()+comodin+rs.getString("FileNameNew").replace("FECHA", fechaStr).replace("--",Integer.toString(ia)).replace("-CCTA-", cuentasSec[ia-1]);
				    	File archivo = new File(rutaArchivo);
				    	BufferedWriter bw;
				    	//borrado de archivo si ya existe
				    	if(archivo.exists()) 
				    	      archivo.delete();
				    	bw = new BufferedWriter(new FileWriter(archivo));
					    for (int x = 0; x < cantLines; x++)
					    {
					    	newLine = new StringBuffer();
					    	if(x == 0)	//linea 1, se modifica numero de cuenta para que cuadre
					    	{
					    		String strLine = "1E-"
					    			+datos.get(x)[0].trim().substring(4,11)
					    			+"-"
					    			+datos.get(x)[0].trim().substring(11,13)
						    		+datos.get(x)[0].trim().substring(13, 70)
						    		+"+"
						    		+datos.get(x)[0].trim().substring(71, 86)
						    		+rellenarCadena(datos.get(x)[0].trim().substring(86,130),45," ")
						    		+datos.get(x)[0].trim().substring(130,131)
						    		+datos.get(x)[0].trim().substring(132,147)
						    		+"+"
						    		+datos.get(x)[0].trim().substring(149,164)
						    		+datos.get(x)[0].trim().substring(164,174);
					    		newLine.append(rellenarCadenaDerecha(strLine, 300, " "));						    	
					    	}
					    	else if(x == cantLines-1)	//ultima linea se modifica segun solicitud
					    	{
					    		String signo = datos.get(x)[0].trim().substring(1,2);
					    		String strLine = datos.get(x)[0].trim().substring(0,2) 
					    			+ datos.get(x)[0].trim().substring(3,18)
					    			+ signo
					    			+ datos.get(x)[0].trim().substring(18,33)
					    			+ signo
					    			+ datos.get(x)[0].trim().substring(33,48)
					    			+ signo
					    			+ datos.get(x)[0].trim().substring(49,64);
					    		newLine.append(rellenarCadenaDerecha(strLine, 300, " "));
					    	}
					    	/*else if(x == cantLines-2)	//ultima linea vacia no se agrega
						    	log.config("Linea en blanco no agregada");*/
					    	else if(x == cantLines-2)	//ultima linea del detalle se modifica segun solicitud
					    	{	
					    		String tipo = datos.get(x)[0].trim().substring(70,71);
					    		String desc = datos.get(x)[0].trim().substring(11,61);
					    		String cod = "42";
					    		if(desc.toUpperCase().contains("CHEQUE") && tipo.toUpperCase().compareTo("C")==0)
					    			cod = "42";
					    		else if(desc.toUpperCase().contains("TRANSFERENCIA") && tipo.toUpperCase().compareTo("C")==0)					    			
					    			cod = "28";
					    		else if(desc.toUpperCase().contains("CARGO") && tipo.toUpperCase().compareTo("C")==0)
					    			cod = "28";
					    		else if(desc.toUpperCase().contains("TRANSFERENCIA") && tipo.toUpperCase().compareTo("A")==0)
					    			cod = "78";
					    		else if(desc.toUpperCase().contains("DEPOSITO") && tipo.toUpperCase().compareTo("A")==0)
					    			cod = "51";
					    		else if(desc.toUpperCase().contains("ABONO") && tipo.toUpperCase().compareTo("A")==0)
					    			cod = "78";
					    		String signo = datos.get(x)[0].trim().substring(71,72);
					    		String strLine = datos.get(x)[0].trim().substring(0, 11) 
					    		+cod+" "
				    			+ datos.get(x)[0].trim().substring(11, 58)
				    			+ datos.get(x)[0].trim().substring(61, 72)
				    			+ datos.get(x)[0].trim().substring(72, 87)
				    			+ signo 
				    			+ datos.get(x)[0].trim().substring(87, 102);
				    			newLine.append(rellenarCadenaDerecha(strLine, 300, " "));
					    	}
					    	else	//modificaciones a linea 2 en adelante
					    	{	
					    		String tipo = datos.get(x)[0].trim().substring(70,71);
					    		String desc = datos.get(x)[0].trim().substring(11,61);
					    		String cod = "42";
					    		if(desc.toUpperCase().contains("CHEQUE") && tipo.toUpperCase().compareTo("C")==0)
					    			cod = "42";
					    		else if(desc.toUpperCase().contains("TRANSFERENCIA") && tipo.toUpperCase().compareTo("C")==0)					    			
					    			cod = "28";
					    		else if(desc.toUpperCase().contains("CARGO") && tipo.toUpperCase().compareTo("C")==0)
					    			cod = "28";
					    		else if(desc.toUpperCase().contains("TRANSFERENCIA") && tipo.toUpperCase().compareTo("A")==0)
					    			cod = "78";
					    		else if(desc.toUpperCase().contains("DEPOSITO") && tipo.toUpperCase().compareTo("A")==0)
					    			cod = "51";
					    		else if(desc.toUpperCase().contains("ABONO") && tipo.toUpperCase().compareTo("A")==0)
					    			cod = "78";
					    		String strLine = datos.get(x)[0].trim().substring(0, 11)
					    			+cod+" "
					    			+ datos.get(x)[0].trim().substring(11, 58)
					    			+ datos.get(x)[0].trim().substring(61, 72)
					    			+ datos.get(x)[0].trim().substring(72, 87);					    		
					    			newLine.append(rellenarCadenaDerecha(strLine, 300, " "));
					    	}
					    	bw.write(newLine.toString());
					    	bw.newLine();
					    }
					    bw.close();
					    contador++;
				    }
			    }catch (Exception e) {
					log.config("No se pudo cargar archivo:"+pFile);
				}
			}
	    }
		commitEx();	
		return "Se han generado "+contador+" archivos de salida";
	}	//	doIt
	private String rellenarCadena(String string, int largo, String caracter)
	{
		String ceros = "";
		int cantidad = largo - string.length();
		if (cantidad >= 1)
		{
			for(int i=0;i<cantidad;i++)
			{
				ceros += caracter;
			}
			return (ceros + string);
		}
		else
			return string;
	}
	private String copiarArchivo(String archivoOrigen, String RutaDestino)
	{
		Runtime aplicacion = Runtime.getRuntime(); 
	    try
	    {	
	    	String eje = "cmd.exe /K copy "+archivoOrigen+" "+RutaDestino;
	    	aplicacion.exec(eje);
	    	/*try 
	    	{
	    		Thread.sleep (200000);
	    	} catch (Exception e) 
	    	{
	    		log.config("Error al esperar tiempo");
	    	}*/
	    }
	    catch(Exception e)
	    {
	    	return e.toString();
	    }	    
	    return "Proceso OK";
	}
	private String rellenarCadenaDerecha(String string, int largo, String caracter)
	{
		String ceros = "";
		int cantidad = largo - string.length();
		if (cantidad >= 1)
		{
			for(int i=0;i<cantidad;i++)
			{
				ceros += caracter;
			}
			return (string + ceros);
		}
		else
			return string;
	}
	public static void unZipFiles(File zipfile, String descDir) {
		File file = new File(descDir);
		if (!file.exists()) {
			try {
				file.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			ZipFile zf = new ZipFile(zipfile);
			for (Enumeration entries = zf.entries(); entries.hasMoreElements();) 
			{
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String zipEntryName = entry.getName();
				InputStream in = zf.getInputStream(entry);
				OutputStream out = new FileOutputStream(descDir + zipEntryName);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
				in.close();
				out.close();
				System.out.println("Descompresión completa.");
			}

			zf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	private String codigoCorpBanca(String desc, String type)
	{
		String cod = "CK";
		if(desc.trim().toUpperCase().contains("PROTESTO DE CHEQUES"))
			cod = "AG";
		else if(desc.trim().toUpperCase().contains("CHEQUE POR CAMARA"))
			cod = "CQ";
		else if(desc.trim().toUpperCase().contains("CHEQUE"))
			cod = "CK";
		else if(desc.trim().toUpperCase().contains("N/CREDITO"))
			cod = "CR";
		else if(desc.trim().toUpperCase().contains("DEBITO"))
			cod = "DB";
		else if(desc.trim().toUpperCase().contains("DISMINUCION DE PRINCIPAL"))
			cod = "DD";
		else if(desc.trim().toUpperCase().contains("DEPOSITO EN EFECTIVO"))
			cod = "DE";
		else if(desc.trim().toUpperCase().contains("DEPOSITO CON CHEQUE"))
			cod = "DM";
		else if(desc.trim().toUpperCase().contains("DEPOSITO"))
			cod = "DP";
		else if(desc.trim().toUpperCase().contains("N/DEBITO"))
			cod = "DR";
		else if(desc.trim().toUpperCase().contains("TRASPASO DE FONDOS"))
			cod = "FT";
		else if(desc.trim().toUpperCase().contains("CAMBIO DE MONEDA EXTRANJERA"))
			cod = "FX";
		else if(desc.trim().toUpperCase().contains("GIRO POR CAJA"))
			cod = "GI";
		else if(desc.trim().toUpperCase().contains("COMISION CTA. VISTA POR GIROS"))
			cod = "G1";
		else if(desc.trim().toUpperCase().contains("TRANSFERENCIA INTERNAS"))
			cod = "IT";
		else if(desc.trim().toUpperCase().contains("NEGOCIACION CARTA DE CREDITO"))
			cod = "LA";
		else if(desc.trim().toUpperCase().contains("NEGOCIACION CARTA DE CREDITO"))
			cod = "LN";
		else if(desc.trim().toUpperCase().contains("PAGO DE PRESTAMO"))
			cod = "LP";
		else if(desc.trim().toUpperCase().contains("TRANSACCION DE CREDITO"))
			cod = "MC";
		else if(desc.trim().toUpperCase().contains("TRANSACCION DE DEBITO"))
			cod = "MD";
		else if(desc.trim().toUpperCase().contains("CHEQUE DE GERENCIA"))
			cod = "OF";
		else if(desc.trim().toUpperCase().contains("PAGO DE REMUNERACIONES"))
			cod = "PR";
		else if(desc.trim().toUpperCase().contains("CARGO POR CHEQUE DEVUELTO"))
			cod = "RD";
		else if(desc.trim().toUpperCase().contains("RENOVACION AUTOMATICA"))
			cod = "RL";
		else if(desc.trim().toUpperCase().contains("CARGO POR CHEQUES VIAJEROS"))
			cod = "TF";
		else if(desc.trim().toUpperCase().contains("IMPUESTO POR SOBREGIRO"))
			cod = "TX";
		else if(desc.trim().toUpperCase().contains("TRANFERENCIA POR TELEX/CREDITO"))
			cod = "WC";
		else if(desc.trim().toUpperCase().contains("TRANSFERENCIA POR TELEX/CREDITO"))
			cod = "FT";
		else if(desc.trim().toUpperCase().contains("GIRO"))
			cod = "WH";
		else if(desc.trim().toUpperCase().contains("ND APERTURA L/C"))
			cod = "01";
		else if(desc.trim().toUpperCase().contains("COMISION MANTENCION DE CUENTA"))
			cod = "01";
		else if(desc.trim().toUpperCase().contains("COMISION TARJETA"))
			cod = "10";
		else if(desc.trim().toUpperCase().contains("COMISIÓN CARTOLA EXTRA MESÓN"))
			cod = "15";
		else if(desc.trim().toUpperCase().contains("COMIS. MENSUAL TARJETA CLIENTE"))
			cod = "16";
		else if(desc.trim().toUpperCase().contains("COMISION SOBREGIRO CHEQUES"))
			cod = "17";
		else if(desc.trim().toUpperCase().contains("COMISION SOBREGIRO CHEQUES"))
			cod = "18";
		else if(desc.trim().toUpperCase().contains("COMISION SEMESTRAL PLAN PYME"))
			cod = "20";
		else if(desc.trim().toUpperCase().contains("INTERESES POR SOBREGIRO"))
			cod = "3E";
		else if(desc.trim().toUpperCase().contains("PAGO DE INTERES SOBRE CUENTA CTE"))
			cod = "3O";
		else if(desc.trim().toUpperCase().contains("COMISION LINEA DE CREDITO"))
			cod = "3P";
		else if(desc.trim().toUpperCase().contains("RETIRO DE INTERESES"))
			cod = "3Q";
		else if(desc.trim().toUpperCase().contains("TRANSFERENCIA DE TELEX"))
			cod = "3V";
		else if(desc.trim().toUpperCase().contains("APERTURA DE CONTRATO"))
			cod = "3X";
		else if(desc.trim().toUpperCase().contains("PAGO A PRESTAMOS"))
			cod = "3Y";
		else if(desc.trim().toUpperCase().contains("COMISION DIGICORP"))
			cod = "31";
		else if(desc.trim().toUpperCase().contains("RETIRO DE CAJERO AUTOMATICO"))
			cod = "33";
		else if(desc.trim().toUpperCase().contains("TRANSFERENCIA CAJERO AUTOMATICO"))
			cod = "35";
		else if(desc.trim().toUpperCase().contains("COMISION USO INTERNACIONAL"))
			cod = "36";
		else if(desc.trim().toUpperCase().contains("COMISIONES/DEBITO"))
			cod = "59";
		else if(desc.trim().toUpperCase().contains("COMISION POR MANEJO"))
			cod = "7M";
		else if(desc.trim().toUpperCase().contains("COMISIÓN LINEA RESPALDO EXTRA"))
			cod = "75";
		else if(desc.trim().toUpperCase().contains("SEG DESGRAVAMEN POR USO LRE"))
			cod = "81";
		else if(desc.trim().toUpperCase().contains("TRANSF. DESDE LINEA DE SOBREG"))
			cod = "9A";
		//ininoles nuevos tipos
		else if(desc.trim().toUpperCase().contains("ABONO"))
			cod = "FT";
		else if(desc.trim().toUpperCase().contains("TRANSF."))
			cod = "FT";
		else if(desc.trim().toUpperCase().contains("TRANSFERENCIA POR TELEX/CREDI"))
			cod = "FT";
		return cod;
	}
	
}	//	



