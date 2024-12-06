/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.ofb.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MInvoice;
import org.compiere.model.MMovement;
import org.compiere.model.MProject;
import org.compiere.model.X_DM_Document;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import test.functional.DBTest;

/**
 *	Order Callouts.
 *	
 *  @author Fabian Aguilar OFB faaguilar
 *  @version $Id: CalloutDMDocument.java,v 1.34 2006/11/25 21:57:24  Exp $
 */
public class CalloutStringToDate extends CalloutEngine
{

	 
	public String ToDateTP_Inicial (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String solofecha=(String)mTab.getValue("DateTxtI");
		String solohora=(String)mTab.getValue("HourTxtI");
		Timestamp fechaCompleta;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		log.info("solofecha: "+solofecha);
		if(solofecha==null || solofecha.trim().equals(""))
		  return"";
		if(solohora==null || solohora.trim().equals(""))
		  solohora="00:00:00";
		
		log.info("solohora: "+solohora);
		
		solofecha+=" "+solohora;
		
		log.info("solofecha: "+solofecha);
		try{
		java.util.Date date = sdf.parse(solofecha);
		fechaCompleta = new java.sql.Timestamp(date.getTime());
		}
		catch(Exception e)
		{
			log.info(e.getMessage());
			return "";
		}
		
		mTab.setValue("TP_InicialHR", fechaCompleta);

		return "";
	}	//	charge
	
	public String ToDateTP_Final (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String solofecha=(String)mTab.getValue("DateTxtF");
		String solohora=(String)mTab.getValue("HourTxtF");
		Timestamp fechaCompleta;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		log.info("solofecha: "+solofecha);
		if(solofecha==null || solofecha.trim().equals(""))
		  return"";
		if(solohora==null || solohora.trim().equals(""))
		  solohora="00:00:00";		
		
		log.info("solohora: "+solohora);
		
		solofecha+=" "+solohora;
		
		log.info("solofecha: "+solofecha);
		try{
		java.util.Date date = sdf.parse(solofecha);
		fechaCompleta = new java.sql.Timestamp(date.getTime());
		}
		catch(Exception e)
		{
			log.info(e.getMessage());
			return "";
		}
		
		mTab.setValue("TP_FinalHR", fechaCompleta);

		return "";
	}
	
	public String ToDate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String solofecha=(String)mTab.getValue("DateTxt");
		String solohora=(String)mTab.getValue("HourTxt");
		Timestamp fechaCompleta;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		log.info("solofecha: "+solofecha);
		if(solofecha==null || solofecha.trim().equals(""))
		  return"";
		if(solohora==null || solohora.trim().equals(""))
		  solohora="00:00:00";
		
		log.info("solohora: "+solohora);
		
		solofecha+=" "+solohora;
		
		log.info("solofecha: "+solofecha);
		try{
		java.util.Date date = sdf.parse(solofecha);
		fechaCompleta = new java.sql.Timestamp(date.getTime());
		}
		catch(Exception e)
		{
			log.info(e.getMessage());
			return "";
		}
		
		mTab.setValue("CompleteDate", fechaCompleta);

		return "";
	}	
	public String ToDateShort_Inicial (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String solofecha=(String)mTab.getValue("DateTxtI");
		String solohora=(String)mTab.getValue("HourTxtI4");
		Timestamp fechaCompleta;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		log.info("solofecha: "+solofecha);
		if(solofecha==null || solofecha.trim().equals(""))
		  return"";
		if(solohora==null || solohora.trim().equals(""))
		  solohora="00:00:00";
		
		log.info("solohora: "+solohora);
		
		//ininoles validaciones para ingreso rapido de hora y fecha
		if (solofecha.length()>10)
		{
			solofecha = solofecha.substring(8,10)+"-"+solofecha.substring(5,7)+"-"+solofecha.substring(0,4);
			mTab.setValue("DateTxtI", solofecha);
			
		}
		
		if (solohora.length() != 4 && solohora.length() != 3 && solohora.length() != 8)
			return "Hora No V�lida";
		
		if(solohora.length() == 3)
			solohora = "0"+solohora;
		
		if(solohora.length() == 4)
			solohora = String.valueOf(solohora.charAt(0)) + String.valueOf(solohora.charAt(1)) + ":" + String.valueOf(solohora.charAt(2)) + String.valueOf(solohora.charAt(3))
				+":00";
		if (!solohora.equals("00:00:00"))
				mTab.setValue("HourTxtI4", solohora);
		
		solofecha+=" "+solohora;
		
		log.info("solofecha: "+solofecha);
		try{
		java.util.Date date = sdf.parse(solofecha);
		fechaCompleta = new java.sql.Timestamp(date.getTime());
		}
		catch(Exception e)
		{
			log.info(e.getMessage());
			return "";
		}
		
		mTab.setValue("TP_InicialHR", fechaCompleta);

		return "";
	}
	
	public String ToDateShort_Final (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String solofecha=(String)mTab.getValue("DateTxtF");
		String solohora=(String)mTab.getValue("HourTxtF4");
		Timestamp fechaCompleta;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		log.info("solofecha: "+solofecha);
		if(solofecha==null || solofecha.trim().equals(""))
		  return"";
		if(solohora==null || solohora.trim().equals(""))
		  solohora="00:00:00";
		
		log.info("solohora: "+solohora);
		
		//ininoles validaciones para ingreso rapido de hora y fecha
		if (solofecha.length()>10)
		{
			solofecha = solofecha.substring(8,10)+"-"+solofecha.substring(5,7)+"-"+solofecha.substring(0,4);
			mTab.setValue("DateTxtF", solofecha);			
		}
		
		if (solohora.length() != 4 && solohora.length() != 3 && solohora.length() != 8)
			return "Hora No V�lida";
		
		if(solohora.length() == 3)
			solohora = "0"+solohora;
		
		if(solohora.length() == 4)
			solohora = String.valueOf(solohora.charAt(0)) + String.valueOf(solohora.charAt(1)) + ":" + String.valueOf(solohora.charAt(2)) + String.valueOf(solohora.charAt(3))
				+":00";
		if (!solohora.equals("00:00:00"))
				mTab.setValue("HourTxtF4", solohora);
		
		solofecha+=" "+solohora;
		
		log.info("solofecha: "+solofecha);
		try{
		java.util.Date date = sdf.parse(solofecha);
		fechaCompleta = new java.sql.Timestamp(date.getTime());
		}
		catch(Exception e)
		{
			log.info(e.getMessage());
			return "";
		}
		
		mTab.setValue("TP_FinalHR", fechaCompleta);

		return "";
	}
	
	public String ToDatehourShort_Final (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (value == null || value == "")
		{			
			return "";
		}
		
		String fechaHora=(String)mTab.getValue("DateHourTxtF");
		String solohora="";
		String solofecha="";
		int ID_Mov = (Integer)mTab.getValue("M_Movement_ID");		
		MMovement mov = new MMovement(ctx, ID_Mov, null);
		String fechaCab = mov.getMovementDate().toString();
		Timestamp fechaCompleta;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		log.info("fehaHora: "+fechaHora);
		if(fechaHora==null || fechaHora.trim().equals(""))
		  return"";
		
		//validacion de largo de cadena
		if (fechaHora.length() != 6)
		{	
			return "Deben Ser 6 digitos";						
		}
		
		//validacion de caracteres
		for (int i=0;i<fechaHora.length();i++)
		{			
			if(isNumeric(String.valueOf(fechaHora.charAt(i)))) 
			{}	
			else
			{
				return "Caract�r No V�lido";				
			}			
		}
		
		solohora = fechaHora.substring(0,2)+":"+fechaHora.substring(2,4)+":00";		
	
		String dia = fechaHora.substring(4,6);
		int diaNum = Integer.parseInt(dia);
		String mes = fechaCab.substring(5,7); 
		String ahno = fechaCab.substring(0,4);
		
		//validacion default dia
		if (dia.equalsIgnoreCase("00"))
			dia = fechaCab.substring(8,10);
		
		solofecha = dia+"-"+mes+"-"+ahno;
		
		//validaciones rangos de fechas
		if (mes.equalsIgnoreCase("02") && diaNum > 29)
		{	
			return "Fecha Fuera de Rango";
		}
		else if ((mes.equalsIgnoreCase("01") || mes.equalsIgnoreCase("03") || mes.equalsIgnoreCase("05") 
					|| mes.equalsIgnoreCase("07") || mes.equalsIgnoreCase("08") || mes.equalsIgnoreCase("10") || mes.equalsIgnoreCase("12")) && diaNum > 31 )
		{
			return "Fecha Fuera de Rango";
		}
		else if ((mes.equalsIgnoreCase("04") || mes.equalsIgnoreCase("06") || mes.equalsIgnoreCase("09") 
				|| mes.equalsIgnoreCase("11")) && diaNum > 30)
		{
			return "Fecha Fuera de Rango";
		}
			
		solofecha+=" "+solohora;
		
		log.info("solofecha: "+solofecha);
		try{
		java.util.Date date = sdf.parse(solofecha);
		fechaCompleta = new java.sql.Timestamp(date.getTime());
		}
		catch(Exception e)
		{
			log.info(e.getMessage());
			return "";
		}
		
		mTab.setValue("TP_FinalHR", fechaCompleta);

		return "";
	}
	
	public String ToDatehourShort_Inicial (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (value == null || value == "")
		{			
			return "";
		}
		
		String fechaHora=(String)mTab.getValue("DateHourTxtI");
		String solohora="";
		String solofecha="";
		int ID_Mov = (Integer)mTab.getValue("M_Movement_ID");		
		MMovement mov = new MMovement(ctx, ID_Mov, null);
		String fechaCab = mov.getMovementDate().toString();
		Timestamp fechaCompleta;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		log.info("fehaHora: "+fechaHora);
		if(fechaHora==null || fechaHora.trim().equals(""))
		  return"";
		
		//validacion de largo de cadena
		if (fechaHora.length() != 6)
		{
			return "Deben Ser 6 digitos";						
		}
		
		//validacion de caracteres
		for (int i=0;i<fechaHora.length();i++)
		{			
			if(isNumeric(String.valueOf(fechaHora.charAt(i)))) 
			{}	
			else
			{
				return "Caract�r No V�lido";				
			}
			
		}
		
		solohora = fechaHora.substring(0,2)+":"+fechaHora.substring(2,4)+":00";		
	
		String dia = fechaHora.substring(4,6);
		int diaNum = Integer.parseInt(dia);
		String mes = fechaCab.substring(5,7); 
		String ahno = fechaCab.substring(0,4);
		
		//validacion default dia
		if (dia.equalsIgnoreCase("00"))
			dia = fechaCab.substring(8,10);
		
		solofecha = dia+"-"+mes+"-"+ahno;
		
		//validaciones rangos de fechas
		if (mes.equalsIgnoreCase("02") && diaNum > 29)
		{
			return "Fecha Fuera de Rango";
		}
		else if ((mes.equalsIgnoreCase("01") || mes.equalsIgnoreCase("03") || mes.equalsIgnoreCase("05") 
					|| mes.equalsIgnoreCase("07") || mes.equalsIgnoreCase("08") || mes.equalsIgnoreCase("10") || mes.equalsIgnoreCase("12")) && diaNum > 31 )
		{
			return "Fecha Fuera de Rango";
		}
		else if ((mes.equalsIgnoreCase("04") || mes.equalsIgnoreCase("06") || mes.equalsIgnoreCase("09") 
				|| mes.equalsIgnoreCase("11")) && diaNum > 30)
		{
			return "Fecha Fuera de Rango";
		}
			
		solofecha+=" "+solohora;
		
		log.info("solofecha: "+solofecha);
		try{
		java.util.Date date = sdf.parse(solofecha);
		fechaCompleta = new java.sql.Timestamp(date.getTime());
		}
		catch(Exception e)
		{
			log.info(e.getMessage());
			return "";
		}
		
		mTab.setValue("TP_InicialHR", fechaCompleta);

		return "";
	}
	
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	


}	//

