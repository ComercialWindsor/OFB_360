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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.compiere.model.MBankAccount;
import org.compiere.model.X_C_BankAccountBalance;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.ofb.model.OFBForward;
 
/**
 *	Generate XML from MInOut
 *	
 *  @author Italo Niñoles Ininoles
 *  @version $Id: ExportDTEMInOut.java,v 1.2 05/09/2014 $
 */
public class AndesRedFileWriteAD extends SvrProcess
{
	/** Properties						*/
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{	
			if (para[i].getParameter() == null)
				;
		}
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{	
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		int contador = 0;
		int pCuenta = 1;
		int pSaldoC = -1;
		int pSaldoD = -1;
		int pRet1d = -1;
		int pRetMas1d = -1;
		int pRetOtro = -1;
		int pTotal = -1;
		String strDeleteDoc = "";
		boolean flagBCI = false;
		
		for (int a = 0; a < 14; a++)
	    {				
			if(a < 2)//banco bbva
			{
				pCuenta = 1;
				pSaldoC = 3;
				pSaldoD = 4;				
				pTotal = 3;
			}else if (a < 3 )//banco bci
			{
				pCuenta = 0;
				pSaldoC = 1;
				pSaldoD = 2;
				pRet1d = 3;
				pRetMas1d = 4;
				pRetOtro = 5;
				pTotal = 2;
			}else if (a < 4 && !flagBCI)//banco bci lineas de credito en la misma cuenta
			{
				pCuenta = 0;
				pSaldoC = 6;
				pSaldoD = 6;
				pRet1d = -1;
				pRetMas1d = -1;
				pRetOtro = -1;
				pTotal = 6;
				//se actualiza indice campos para que no tenga problemas en las demas instrucciones
				a--;
				flagBCI = true;
			}else if (a < 4)//banco santander1
			{
				pCuenta = 0;
				pSaldoC = 4;
				pSaldoD = 3;
				pRet1d = 2;
				pRetMas1d = -1;
				pRetOtro = 1;
				pTotal = 4;
			}else if (a < 5)//banco santander2
			{
				pCuenta = 0;
				pSaldoC = 3;
				pSaldoD = 3;
				pRet1d = -1;
				pRetMas1d = -1;
				pRetOtro = -1;
				pTotal = 3;
			}else if (a < 6)//banco estado 1 
			{
				pCuenta = 0;
				pSaldoC = 1;
				pSaldoD = 2;
				pRet1d = 3;
				pRetMas1d = 5;
				pRetOtro = 6;
				pTotal = 1;
			}else if (a < 7)//banco estado 2
			{
				pCuenta = 0;
				pSaldoC = 4;
				pSaldoD = 4;
				pRet1d = -1; //se cambia de 1
				pRetMas1d = -1;
				pRetOtro = -1; //se cambia de 1
				pTotal = 4;
			}else if (a < 8)//banco corpbanca
			{
				pCuenta = 1;
				pSaldoC = 2;
				pSaldoD = 3;
				pRet1d = -1;
				pRetMas1d = -1;
				pRetOtro = -1;
				pTotal = 2;
			}
			else if (a < 9)//banco itau
			{
				pCuenta = 0;
				pSaldoC = 2;
				pSaldoD = 1;
				pRet1d = -1;
				pRetMas1d = -1;
				pRetOtro = -1;
				pTotal = 2;
			}
			else if (a < 10)//banco scotiabank
			{
				pCuenta = 0;
				pSaldoC = 2;
				pSaldoD = 4;
				pRet1d = 3;
				pRetMas1d = -1;
				pRetOtro = -1;
				pTotal = 2;
			}
			else if (a < 11)//banco security
			{
				pCuenta = 0;
				pSaldoC = 2;
				pSaldoD = 1;
				pRet1d = -1;
				pRetMas1d = -1;
				pRetOtro = 4;
				pTotal = 2;
			}
			else if (a < 12)//banco de chile
			{
				pCuenta = 0;
				pSaldoC = 1;
				pSaldoD = 2;
				pRet1d = 3;
				pRetMas1d = 4;
				pRetOtro = -1;
				pTotal = 1;
			}
			else if (a < 13)//banco de chile 2
			{
				pCuenta = 0;
				pSaldoC = 2;
				pSaldoD = 2;
				pRet1d = -1;
				pRetMas1d = -1;
				pRetOtro = -1;
				pTotal = 2;
			}
			else if (a < 14)//banco bice
			{
				pCuenta = 1;
				pSaldoC = 3;
				pSaldoD = 4;
				pRet1d = -1;
				pRetMas1d = -1;
				pRetOtro = -1;
				pTotal = 3;
			}
			
			ArrayList<String[]> datos=new ArrayList<String[]>();
			String comodin = "\\";
			//obtenemos ruta
			String ruta = OFBForward.PathDataIMacro();
			//obtenemos fecha
			//Fecha actual desglosada:
	        //Calendar fecha = Calendar.getInstance();	 
	       //String fechaStr = Integer.toString(fecha.get(Calendar.DAY_OF_MONTH)) +(fecha.get(Calendar.MONTH)+1)
	       // 	+fecha.get(Calendar.YEAR);
	       // fechaStr = fechaStr.substring(0, 4)+fechaStr.substring(6, 8);
	        
	        //@mfrojas se cambia el formato de fecha.
	        
	        DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
	        Date date1 = new Date();
	        String fechaStr = dateFormat.format(date1);
	        //seteamos nueva ruta
	        String pFile = ruta+comodin+(a+1)+"_"+fechaStr+".csv";
		    try 
		    {
		    	FileInputStream fis =new FileInputStream(pFile);
			    InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			    BufferedReader br = new BufferedReader(isr);
			    String linea=br.readLine();
			    //string guarda nombres de archivos para borrar posteriormente
			    strDeleteDoc = strDeleteDoc+pFile+",";
			    int cantLines = 0;
			    while(linea!=null)
			    {
			        datos.add(linea.split(";"));
				    linea=br.readLine();
				    cantLines = cantLines + 1;
			    }
			    br.close();
			    isr.close();
			    fis.close();
			    log.config(datos.toString());
			    
			    for (int x = 0; x < cantLines; x++)
			    {
			    	String numAcount = "0";
			    	try
		    		{
			    		numAcount = datos.get(x)[pCuenta].trim();
		    		}catch (Exception e) 
		    		{
		    			numAcount = "0";
					}
			    	  	
			    	try
			    	{
			    		if (a < 2)
			    			numAcount = numAcount.substring(0,20);
			    		else if (a == 2 && !flagBCI)
			    			numAcount = numAcount.substring(10,18);
			    		else if (a == 2 && flagBCI)
			    			numAcount = "L-"+numAcount.substring(10,18);
			    		else if (a >=5 && a <6)
			    			numAcount = numAcount.substring(23,34);		    		
			    		else if (a >=8 && a <9)
			    			numAcount = numAcount.substring(17,27);
			    		else if (a >=11 && a <12)
			    			numAcount = numAcount.substring(10,25);
			    		else if (a >=12 && a <13)
			    			numAcount = numAcount.substring(9,24);
			    		
			    		//validacion para security linea de credito
			    		if(a == 10)
			    		{
			    			if(numAcount.substring(0, 1).compareToIgnoreCase("L") == 0)
			    				pRetOtro = -1;
			    		}
			    	}
			    	catch (Exception e) 
			    	{
			    		numAcount = "0";
					}
			    	
			    	int id_acount = DB.getSQLValue(get_TrxName(), "SELECT C_BankAccount_ID FROM C_BankAccount" +
			    			" WHERE AccountNo = '"+numAcount+"'");
			    	if (id_acount > 0)
			    	{
			    		MBankAccount bAcount = new MBankAccount(getCtx(), id_acount, get_TrxName());
			    		X_C_BankAccountBalance bal = new X_C_BankAccountBalance(getCtx(), 0, get_TrxName());
			    		bal.setAD_Org_ID(bAcount.getAD_Org_ID());
			    		bal.setC_BankAccount_ID(bAcount.get_ID());
			    		Calendar calendar = Calendar.getInstance();
			    		Timestamp actual = new Timestamp(calendar.getTime().getTime());
			    		bal.setDateDoc(actual);
			    		if (pSaldoC >= 0)
			    			bal.setAccountingBalance((BigDecimal) decimalFormat.parse(datos.get(x)[pSaldoC].replace("$","").replace("\"","").trim()));
			    		else
			    			bal.setAccountingBalance(Env.ZERO);
			    		if (pSaldoD >= 0)
			    			bal.setAvailableBalance((BigDecimal) decimalFormat.parse(datos.get(x)[pSaldoD].replace("$","").replace("\"","").trim()));
			    		else
			    			bal.setAvailableBalance(Env.ZERO);
			    		if (pRet1d >= 0)
			    			bal.setRetention((BigDecimal) decimalFormat.parse(datos.get(x)[pRet1d].replace("$","").replace("\"","").trim()));
			    		else
			    			bal.setRetention(Env.ZERO);
			    		if (pRetMas1d >= 0)
		    				bal.setRetention1((BigDecimal) decimalFormat.parse(datos.get(x)[pRetMas1d].replace("$","").replace("\"","").trim()));
			    		else
			    			bal.setRetention1(Env.ZERO);
			    		if (pRetOtro >= 0)
		    				bal.setRetention2((BigDecimal) decimalFormat.parse(datos.get(x)[pRetOtro].replace("$","").replace("\"","").trim()));
			    		else
			    			bal.setRetention2(Env.ZERO);
			    		if (pTotal >= 0)
			    			bal.setTotalBalance((BigDecimal) decimalFormat.parse(datos.get(x)[pTotal].replace("$","").replace("\"","").trim()));
			    		else 
			    			bal.setTotalBalance(Env.ZERO);
			    		bal.save();
			    		contador = contador+1;
			    	}
			    }
		    }catch (Exception e) {
				log.config("No se pudo cargar archivo:"+pFile);
			}
	    }
		commitEx();
		
		strDeleteDoc = strDeleteDoc.substring(0, strDeleteDoc.length()-1);
		String msgD = DeleteEjecucion(strDeleteDoc);
		log.config(msgD);
		
		return "Se han agregado "+contador+" Saldos en el sistema";
	}	//	doIt
	
	public String DeleteEjecucion(String files)
	{
		Runtime aplicacion = Runtime.getRuntime(); 
	    try
	    {
	    	aplicacion.exec("cmd.exe /K del "+files); 
	    }
	    catch(Exception e)
	    {
	    	return e.toString();
	    }	    
	    return "Proceso OK";
	}
	public String DeleteEjecucionCompleta(String rutaBatDelete)
	{
		Runtime aplicacion = Runtime.getRuntime(); 
	    try
	    {
	    	aplicacion.exec("cmd.exe /K "+rutaBatDelete); 
	    }
	    catch(Exception e)
	    {
	    	return e.toString();
	    }	    
	    return "Proceso OK";
	}
	
}	//	InvoiceCreateInOut
