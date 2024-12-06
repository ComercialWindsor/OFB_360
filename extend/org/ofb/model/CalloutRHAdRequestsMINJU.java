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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.X_RH_AdministrativeRequests;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Order Callouts.
 *	
 *  @author Italo Niñoles OFB ininoles.
 *  @version $Id: CalloutRHAdministrativeRequests.java,v 2.0 2012/12/03  Exp $
 */
public class CalloutRHAdRequestsMINJU extends CalloutEngine
{
	
	public String EndDate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws ParseException
	{
		if(value==null)
			return "";

		Timestamp dateStart = (Timestamp)mTab.getValue("datestartrequest");
		if(dateStart != null)
		{
			Calendar startCal = Calendar.getInstance();		 
			startCal.setTime(dateStart);
			startCal.add(Calendar.YEAR, 2);
			dateStart.setTime(startCal.getTimeInMillis());
			if(dateStart != null)
				mTab.setValue("dateendrequest", dateStart);		
		}		
		return "";
	}
	
	public static java.util.Date calculateEndDate(Date startDate, int duration)
	{		
	  Calendar startCal = Calendar.getInstance();
	 
	  startCal.setTime(startDate);
			
	  for (int i = 1; i < duration; i++)
	  {
	    startCal.add(Calendar.DAY_OF_MONTH, 1);
	    while (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
	    {
	      startCal.add(Calendar.DAY_OF_MONTH, 1);
	    }
	  }
	 
	  return startCal.getTime();
	}
	
	public static java.util.Date calculateEndDateFeriados(java.util.Date startDate, int duration)
	
	{		
	  Calendar startCal = Calendar.getInstance();
	 
	  startCal.setTime(startDate);
			
	  for (int i = 1; i < duration; i++)
	  {		  
		startCal.add(Calendar.DAY_OF_MONTH, 1);
	    while (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
	    {
	      startCal.add(Calendar.DAY_OF_MONTH, 1);
	    }
	    if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
	    {
	    	startCal.set(Calendar.HOUR_OF_DAY, 00);
	    	startCal.set(Calendar.MINUTE, 00);
	    	startCal.set(Calendar.SECOND, 00);
	    	
	    	Timestamp Hactual= new Timestamp(startCal.getTimeInMillis());
	    	    		    	
	    	String sqlDY = "select Coalesce((upper(substring(feriado from 1 for 1))),'N') from dateofyear where trunc(date2) = ?";	    	
	    	String feriado = DB.getSQLValueString(null,sqlDY,Hactual);
	    	if(feriado!= null && feriado.equals("Y"))
	    	{
	    		//startCal.add(Calendar.DAY_OF_MONTH, 1);
	    		i = i-1;
	    	}
	    }
	  }
	 
	  return startCal.getTime();
	}
	
	public String EndTimeSHE (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws ParseException
	{
		if(value==null)
			return "";
		
		int IDAdmReq = (Integer) mTab.getValue("RH_AdministrativeRequests_ID");
		
		X_RH_AdministrativeRequests AdmRequest = new X_RH_AdministrativeRequests(Env.getCtx(), IDAdmReq, null);
		
		if (AdmRequest.getRequestType().equals("SHE"))			
		{		
			if (mTab.getValue("StartTime") != null && mTab.getValue("EndTime") != null)
			{
				Timestamp startTime = (Timestamp)mTab.getValue("StartTime");
				Timestamp endTime = (Timestamp)mTab.getValue("EndTime");
			
				if (endTime.compareTo(startTime) < 1)				
				{				
					if (mField.getColumnName().equals("StartTime"))
					{
						mTab.setValue("StartTime", null);
					}
					else if (mField.getColumnName().equals("EndTime"))
					{
						mTab.setValue("EndTime", null);
					}				
					return "Hora Fin no puede ser mayor o igual a Hora Inicio";
				
				}
			}
		}
		
		return "";
	}
	
	public static int fechasDiferenciaEnDias(Date fechaInicial, Date fechaFinal)
	{

		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String fechaInicioString = df.format(fechaInicial);
		try {
		fechaInicial = df.parse(fechaInicioString);
		}
		catch (ParseException ex) {
		}

		String fechaFinalString = df.format(fechaFinal);
		try {
		fechaFinal = df.parse(fechaFinalString);
		}
		catch (ParseException ex) {
		}

		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		return ( (int) dias);
		}
}


