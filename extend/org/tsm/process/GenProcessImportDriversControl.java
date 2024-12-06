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
package org.tsm.process;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.logging.Level;


import org.compiere.model.X_TP_DriversControl;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author mfrojas
 *  @version $Id: GenProcessImportDriversControl.java $
 */
public class GenProcessImportDriversControl extends SvrProcess
{
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private String 			p_PathFile;

	 protected void prepare()
	{	 
			ProcessInfoParameter[] para = getParameter();
			for (int i = 0; i < para.length; i++)
			{
				String name = para[i].getParameterName();
				if (para[i].getParameter() == null)
					;
				else if (name.equals("archivo"))
					p_PathFile = (String)para[i].getParameter();
				else
					log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
			}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		//primero se importan los documentos
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		String rutaArchivo = p_PathFile;
		String pFile = rutaArchivo;
		ArrayList<String[]> datos=new ArrayList<String[]>();
		int cantLines = 0;
		int cant = 0;
	    try 
	    {
	    	FileInputStream fis =new FileInputStream(pFile);
		    InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
		    BufferedReader br = new BufferedReader(isr);
		    String linea=br.readLine();		   
		    //se recorre archivo
		    while(linea!=null)
		    {
		        datos.add(linea.split(";"));
			    linea=br.readLine();
			    cantLines = cantLines + 1;
		    }
		    br.close();
		    isr.close();
		    fis.close();
		    log.config("Se ha leido "+cantLines+" desde archivo");
		    BigDecimal qty = Env.ZERO;
		    //se leen campos del archivo
		    for (int x = 1; x < cantLines; x++)
		    {	
		    	//se busca campos obligatorios en las distintas posiciones del archivo
				X_TP_DriversControl dControl = null;

		    	//Primero se deberá validar el campo tp_Driverscontrol_id.
				//Si existe el id en la tabla, entonces se actualizará la informacion. 
				//Si no existe, se crea registro nuevo
				int ID_DriversControl = 0;
				if(datos.get(x)[25].length() > 0)
				{
					if(Integer.parseInt(datos.get(x)[25]) != 0)
					{
						ID_DriversControl = Integer.parseInt(datos.get(x)[25]);
						int valuereview = DB.getSQLValue(get_TrxName(), "SELECT count(1) from tp_driverscontrol where tp_driverscontrol_id = "+ID_DriversControl);
						if(valuereview == 0)
							ID_DriversControl = 0;
					}
					
				}
				
				dControl = new X_TP_DriversControl(getCtx(), ID_DriversControl, get_TrxName());
		    	
		    	

		    	//Activo
		    	int ID_Asset = DB.getSQLValue(get_TrxName(), "SELECT MAX(A_Asset_ID) FROM " +
		    			" A_Asset WHERE IsActive = 'Y' AND value like '"+datos.get(x)[0]+"'");
		    	if(ID_Asset == 0)
		    		return "El activo debe venir identificado. Valor erroneo: "+datos.get(x)[0];
		    	dControl.setA_Asset_ID(ID_Asset);
		    	
		    	//AD_Org
	    		int ID_Org = Integer.parseInt(datos.get(x)[2]);
	    		dControl.setAD_Org_ID(ID_Org);
	    		
		    	//C_BPartner
		    	int ID_Bpartner = DB.getSQLValue(get_TrxName(), "SELECT MAX(c_bpartner_Id) FROM " +
		    			" C_BPartner WHERE IsActive = 'Y' AND value like '"+datos.get(x)[3]+"'");
		    	if(ID_Bpartner == 0)
		    		return "El socio de negocio debe venir identificado";
		    	dControl.setC_BPartner_ID(ID_Bpartner);
		    	
		    	//locator
		    	if(datos.get(x)[4].length() > 0)
		    	{
		    		int ID_Locator = Integer.parseInt(datos.get(x)[4]);
		    		if(ID_Locator>0)
		    			dControl.setC_Location_ID(ID_Locator);
		    	}
		    	//periodo
	    		
		    	if(datos.get(x)[10].length() > 0)
		    	{
		    		int ID_Period = DB.getSQLValue(get_TrxName(), "SELECT MAX(c_period_id) FROM " +
		    			" c_period WHERE IsActive = 'Y' AND startdate < '"+datos.get(x)[10]+"' and enddate > '"+datos.get(x)[10]+"'");
		    		dControl.setC_Period_ID(ID_Period);
		    	}
	    		//projectofb
	    		
	    		
	    		//categorytype
	    		
	    		
	    		//date1
	    		Timestamp date1 = Timestamp.valueOf(datos.get(x)[10]);
	    		if(date1 != null)
	    			dControl.setDate1(date1);
	    		
	    		//Description
	    		String Desc = datos.get(x)[11];
	    		if(Desc != null)
	    			dControl.setDescription(Desc);
	    		
	    		//endtime
	    		Timestamp endtime = Timestamp.valueOf(datos.get(x)[12]);
	    		if(endtime != null)
	    			dControl.setEndTime(endtime);
	    		
	    		//interval
	    		if(datos.get(x)[13].length() > 0)
	    		{
	    			String interval = datos.get(x)[13];
	    			if(interval != null)
	    				dControl.setInterval(interval);
	    		}
	    		//iscomitee
	    		String iscomittee = datos.get(x)[15];
	    		if(iscomittee != null)
	    			dControl.setIsCommittee(false);
	    		
	    		//latitude
	    		BigDecimal latitude = (BigDecimal) decimalFormat.parse(datos.get(x)[16].trim());
	    		if(latitude.compareTo(Env.ZERO) != 0)
	    			dControl.setLatitude(latitude);
	    		
	    		//longitude
	    		BigDecimal longitude = (BigDecimal) decimalFormat.parse(datos.get(x)[17].trim());
	    		if(longitude.compareTo(Env.ZERO) != 0)
	    			dControl.setLongitude(longitude);

	    		//maxspeed
	    		if(datos.get(x)[18].length() > 0)
	    		{	
	    			BigDecimal maxspeed = (BigDecimal) decimalFormat.parse(datos.get(x)[18].trim());
	    			if(maxspeed.compareTo(Env.ZERO) >= 0)
	    				dControl.setMaxSpeed(maxspeed.intValue());
	    		}
	    		//petition
	    		String petition = datos.get(x)[19];
	    		if(petition != null)
	    			dControl.setPetition(false);
	    		
	    		//reason
	    		String reason = datos.get(x)[20];
	    		if(reason != null)
	    			dControl.setReason(reason);
	    		
	    		//speedcategory
	    		if(datos.get(x)[21].length() > 0)
	    		{
	    			BigDecimal speedcategory = (BigDecimal) decimalFormat.parse(datos.get(x)[21].trim());
	    			if(speedcategory.compareTo(Env.ZERO) >= 0)
	    				dControl.setSpeedCategory(speedcategory.intValue());
	    		}
	    		
	    		//starttime
	    		Timestamp starttime = Timestamp.valueOf(datos.get(x)[22]);
	    		if(starttime != null)
	    			dControl.setStartTime(starttime);
	    		
	    		//status
	    		String status = datos.get(x)[23];
	    		if(status != null)
	    			dControl.setStatus(status);
	    		
		    	//Supervisor
	    		if(datos.get(x)[24].length() > 0)
	    		{
	    			int supervisor = (Integer) decimalFormat.parse(datos.get(x)[24].trim());
	    			if(supervisor > 0)
	    				dControl.setSupervisor_ID(supervisor);
	    		}
	    		//typematrix
	    		String typematrix = datos.get(x)[26];
	    		if(typematrix != null)
	    			dControl.setTypeMatrix(typematrix);
	    		
	    		//waittime
/*	    		if(datos.get(x)[29].length() > 0)
	    		{
	    			Timestamp waittime = Timestamp.valueOf(datos.get(x)[29]);
	    			if(waittime != null)
	    				dControl.setWaitTime(waittime);
	    		}
*/
	    		//address1
	    		if(datos.get(x)[30].length() > 0)
	    		{
	    			String address1 = datos.get(x)[30];
	    			if(address1 != null)
	    				dControl.setAddress1(address1);
	    		}
	    		//mailtext
	    		if(datos.get(x)[31].length() > 0)
	    		{
	    			String mailtext = datos.get(x)[31];
	    			if(mailtext != null)
	    				dControl.setMailText(mailtext);
	    		}
	    		
	    		//categorytype2
	    		if(datos.get(x)[32].length() > 0)
	    		{
	    			String categorytype2 = datos.get(x)[32];
	    			if(categorytype2 != null)
	    				dControl.setCategoryType2(categorytype2);
	    		}
	    		
	    		//docstatus
	    		if(datos.get(x)[33].length() > 0)
	    		{
	    			String docstatus = datos.get(x)[33];
	    			if(docstatus != null)
	    				dControl.setDocStatus(docstatus);
	    		}
	    		
	    		//docaction
	    		if(datos.get(x)[34].length() > 0)
	    		{	
	    			String docaction = datos.get(x)[34];
	    			if(docaction != null)
	    				dControl.set_CustomColumn("docaction",docaction);
	    		}
	    		dControl.setProcessed(false);
	    		
	    		//document_id
	    		if(datos.get(x)[36].length() > 0)
	    		{
	    			int ID_doctype = Integer.parseInt(datos.get(x)[36]);
	    			if(ID_doctype > 0)
	    				dControl.setC_DocType_ID(ID_doctype);
	    		}
	    		
	    		//documentno
	    		if(datos.get(x)[38].length() > 0)
	    		{
	    			String docno = datos.get(x)[38];
	    			if(docno != null)
	    				dControl.set_CustomColumn("DocumentNo", docno);
	    		}
	    		
	    		dControl.save();
		    	
	    	}
		    datos = null;
	    }
		catch (Exception e) 
		{
			log.config("ERROR: "+e);
		}
		return "Se han cargado "+cantLines+" lineas";		
		
		//return "Procesado ";
	}	//	doIt
}
