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
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MBPartner;
import org.compiere.model.MProject;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_DM_Document;
import org.compiere.model.X_RH_AdministrativeRequests;
import org.compiere.model.X_RH_AdministrativeRequestsLine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

/**
 *	Order Callouts.
 *	
 *  @author Italo Niñoles OFB ininoles.
 *  @version $Id: CalloutRHAdministrativeRequests.java,v 2.0 2012/12/03  Exp $
 */
public class CalloutRHAdministrativeRequests extends CalloutEngine
{

	 
	public String Authorizations (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		
		if(value==null)
			return "";
		
		String sql = "select max(ad_user_id)"
				+ " from ad_user adu where password is not null"
				+ " and adu.C_BPartner_ID = ?";
		
		int userID = DB.getSQLValue(null, sql, mTab.getValue("C_Bpartner_ID"));
		
		String requestType = "";
		requestType = mTab.get_ValueAsString("RequestType");
		
		//logica para boton listo para aprobar
		if (mField.getColumnName().equals("Signature3") && value.equals(true))
		{
			int Record_ID = (Integer)mTab.getValue("RH_AdministrativeRequests_ID");
			X_RH_AdministrativeRequests doc=new X_RH_AdministrativeRequests(Env.getCtx(), Record_ID,null);
			//inicio logica de saldos
			if(doc.getRequestType().equals("PAD"))//accion para permisos administrativos ininoles
			{								
				MBPartner bp = MBPartner.get(Env.getCtx(), doc.getC_BPartner_ID());
				BigDecimal dd = DB.getSQLValueBD(null, "select coalesce ((SUM( case when block = 'MDM' then 0.5 "+
									"when block = 'MDT' then 0.5 when block = 'DIA' then 1 end)),0) "+
									"from RH_AdministrativeRequestsline where RH_AdministrativeRequests_ID=?", doc.getAdministrativeRequests_ID());
				
				BigDecimal PADep = DB.getSQLValueBD(null, "select coalesce ((SUM(case when arl.block = 'MDM' then 0.5 "+
						"when arl.block = 'MDT' then 0.5 when arl.block = 'DIA' then 1 end)),0) from RH_AdministrativeRequestsline arl "+
						"inner join RH_AdministrativeRequests ar on (arl.RH_AdministrativeRequests_id = ar.RH_AdministrativeRequests_id) "+
						"where ar.requesttype like 'PAD' and (ar.docstatus = 'DR' OR ar.docstatus = 'IP') and ar.isactive = 'Y' and ar.Signature3 = 'Y' "+
						"and ar.c_bpartner_id = ? and ar.RH_AdministrativeRequests_ID not in (?)",bp.get_ID(),doc.getAdministrativeRequests_ID());
				
				BigDecimal cantPAD = (BigDecimal)bp.get_Value("RH_AdministrativeDays");
											
				if (dd.add(PADep).compareTo(cantPAD) > 0)
				{
					mTab.setValue("Signature3", "N");
					return "Dias Administrativos Disponibles Insuficientes";					
				}												
			}
			if(doc.getRequestType().equals("CMT"))//validacion detalle cometidos ininoles
			{
				int cantCMTD = DB.getSQLValue(null, "select count(*) from RH_AdministrativeRequestsline "+
						"where RH_AdministrativeRequests_ID=?", doc.getAdministrativeRequests_ID());
			
				if (cantCMTD < 1)
				{
					mTab.setValue("Signature3", "N");
					return "Solicitud de Cometido debe Tener Detalle(Comunas)";
				}
			}
			
			if(doc.getRequestType().equals("TCP"))//accion para tiempo compensado ininoles
			{											
				MBPartner bp = MBPartner.get(Env.getCtx(), doc.getC_BPartner_ID());											
				BigDecimal dd = DB.getSQLValueBD(null, "select coalesce ((SUM(numberhours)),0) as qty "+
											"from RH_AdministrativeRequestsline "+
											"where RH_AdministrativeRequests_ID = ?", doc.getAdministrativeRequests_ID());
				
				int cantTCPD = DB.getSQLValue(null, "select count(*) from RH_AdministrativeRequestsline "+
							"where RH_AdministrativeRequests_ID=?", doc.getAdministrativeRequests_ID());
				
				if (cantTCPD < 1)
				{
					mTab.setValue("Signature3", "N");
					return "Solicitud Tiempo Compensado debe Tener Detalle";
				}
				
				BigDecimal TCPep = DB.getSQLValueBD(null, "select coalesce ((SUM(numberhours)),0) as qty from RH_AdministrativeRequestsline arl "+
						"inner join RH_AdministrativeRequests ar on (arl.RH_AdministrativeRequests_id = ar.RH_AdministrativeRequests_id) "+
						"where ar.requesttype like 'TCP' and (ar.docstatus = 'DR' OR ar.docstatus = 'IP') and ar.isactive = 'Y' and ar.Signature3 = 'Y'"+
						"and ar.c_bpartner_id = ? and ar.RH_AdministrativeRequests_ID not in (?)",bp.get_ID(),doc.getAdministrativeRequests_ID());
				
				BigDecimal cantTCP = (BigDecimal)bp.get_Value("numberhours");
				
				if (dd.add(TCPep).compareTo(cantTCP) > 0)
				{
					mTab.setValue("Signature3", "N");
					return "Horas Acumuladas Disponibles Insuficientes";
				}								
			}
			if(doc.getRequestType().equals("SVC"))//Logica para feriado legal tomando campo cantidad ininoles 
			{
				MBPartner bp = MBPartner.get(Env.getCtx(), doc.getC_BPartner_ID());
				BigDecimal dd = DB.getSQLValueBD(null, "select NumberDays from RH_AdministrativeRequests where " +
						"RH_AdministrativeRequests_ID = ?", doc.getAdministrativeRequests_ID());
				
				BigDecimal SVCep = DB.getSQLValueBD(null, "select coalesce ((sum(NumberDays)),0) from RH_AdministrativeRequests ar "+
						"where ar.requesttype like 'SVC' and (ar.docstatus = 'DR' OR ar.docstatus = 'IP') and ar.isactive = 'Y' and ar.Signature3 = 'Y' "+
						"and ar.c_bpartner_id = ? and ar.RH_AdministrativeRequests_ID not in (?)",bp.get_ID(),doc.getAdministrativeRequests_ID());
				
				BigDecimal cantSVC = (BigDecimal)bp.get_Value("RH_AcumulateDays");
											
				if (dd.add(SVCep).compareTo(cantSVC) > 0)
				{
					mTab.setValue("Signature3", "N");
					return "Dias Legales Disponibles Insuficientes";
				}								
			}
			if(doc.getRequestType().equals("PSD"))//accion para permisos sindicales ininoles
			{								
				MBPartner bp = MBPartner.get(Env.getCtx(), doc.getC_BPartner_ID());				
				BigDecimal dd = DB.getSQLValueBD(null, "select coalesce ((SUM(numberhours)),0) as qty " +
								"from RH_AdministrativeRequestsline " +
								"where RH_AdministrativeRequests_ID = ?", doc.getAdministrativeRequests_ID());
				
				BigDecimal PSDep = DB.getSQLValueBD(null, "select coalesce ((SUM(numberhours)),0) as qty from RH_AdministrativeRequestsline arl "+
						"inner join RH_AdministrativeRequests ar on (arl.RH_AdministrativeRequests_id = ar.RH_AdministrativeRequests_id) "+
						"where ar.requesttype like 'PSD' and (ar.docstatus = 'DR' OR ar.docstatus = 'IP') and ar.isactive = 'Y' and ar.Signature3 = 'Y' "+
						"and ar.c_bpartner_id = ? and ar.RH_AdministrativeRequests_ID not in (?)",bp.get_ID(),doc.getAdministrativeRequests_ID());
				
				BigDecimal cantPSD = (BigDecimal)bp.get_Value("RH_UnionHours");
				
				if (dd.add(PSDep).compareTo(cantPSD) > 0)
				{
					mTab.setValue("Signature3", "N");
					return "Horas Sindicales Disponibles Insuficientes";
				}								
			}			
			//fin logica de saldos

			//validacion logica horas extra
			if(doc.getRequestType().equals("SHE"))//accion para solicitud hora extra
			{
				PreparedStatement pstmt = null;
				String sqlVL = "SELECT RH_AdministrativeRequestsLine_ID FROM RH_AdministrativeRequestsLine WHERE RH_AdministrativeRequests_ID = ? ";
				
				try 
				{
					pstmt = DB.prepareStatement(sqlVL, doc.get_TrxName());
					pstmt.setInt(1, doc.get_ID());											
					ResultSet rs = pstmt.executeQuery();
					
					String sqlDY = "select Coalesce((upper(substring(Feriado from 1 for 1))),'N') from dateofyear where trunc(date2) = ?";
					//Timestamp dayAR = (Timestamp)doc.get_Value("datestartrequest");
					
					Date dateStar = new Date(doc.getdatestartrequest().getTime());
					Timestamp dateStarTS = new Timestamp(dateStar.getTime());					
					
			    	String feriado = DB.getSQLValueString(null,sqlDY,dateStarTS);
			    	
			    	if(feriado == null)
			    		feriado = "N";
			    	
			  	  	if (feriado.equals("N"))
			  	  	{
			  	  		Calendar startCal = Calendar.getInstance();			   	 
			  	  		startCal.setTime((Timestamp)doc.get_Value("datestartrequest"));
			  	  		if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			  	  			feriado = "Y";
			  	  	}
			  	  	
					while (rs.next())						
					{
						X_RH_AdministrativeRequestsLine RL = new X_RH_AdministrativeRequestsLine(Env.getCtx(), rs.getInt(1),null);
						String typeHExtra = RL.get_ValueAsString("Percentage");
						if (typeHExtra == null)
						{
							mTab.setValue("Signature3", false);
							return "Detalle de Hora sin Porcentaje: "+RL.get_ID();
							
						}
						else
						{							
					    	if(feriado.equals("Y") && typeHExtra.equalsIgnoreCase("25") ) //validacion dia festivo
					    	{
					    		mTab.setValue("Signature3", false);
					    		return "Intervalo no válido, revise Hora inicio/Hora fin : "+RL.get_ID();
					    	}
					    	if(feriado.equalsIgnoreCase("N")) //validaciones dia no festivo
					    	{
					    		Timestamp st = (Timestamp)RL.get_Value("StartTime");
					    		Timestamp et = (Timestamp)RL.get_Value("EndTime");
					    		
					    		java.util.Date StartTime = new Time(st.getTime());
					    		java.util.Date EndTime = new Time(et.getTime());
					    		
					    		DateFormat dateFormat = new SimpleDateFormat ("hh:mm:ss");
					    		
					    		String rango1 = "17:15:00";
					    		String rango2 = "21:00:00";
					    		String rango3 = "23:59:00";					    		
					    		String rango4 = "00:00:00";
					    		String rango5 = "06:59:00";
					    		String rango6 = "16:15:00";
					    		
					    		java.util.Date hora1, hora2, hora3, horai, horaf, hora4, hora5, hora6;
					    		
					    		hora1 = dateFormat.parse(rango1);
					    		hora2 = dateFormat.parse(rango2);
					    		hora3 = dateFormat.parse(rango3);
					    		hora4 = dateFormat.parse(rango4);
					    		hora5 = dateFormat.parse(rango5);
					    		hora6 = dateFormat.parse(rango6);

					    		horai = dateFormat.parse(StartTime.toString());
					    		horaf = dateFormat.parse(EndTime.toString());
					    		//validacion horas al 25%
					    		if (typeHExtra.equalsIgnoreCase("25"))
					    		{
					    			Calendar startCalF = Calendar.getInstance();			   	 
						  	  		startCalF.setTime((Timestamp)doc.get_Value("datestartrequest"));
						  	  		
						  	  		if (startCalF.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)  
						  	  		{
						  	  			if(horai.compareTo(hora6)<=0 || horaf.compareTo(hora2)>0)
						  	  			{
						  	  				mTab.setValue("Signature3", false);
						  	  				return "Intervalo no válido, revise Hora inicio/Hora fin : "+RL.get_ID();
						  	  			}
						  	  		}						  	  		
						  	  		else
						  	  		{
						  	  			if(horai.compareTo(hora1)<=0 || horaf.compareTo(hora2)>0)
						  	  			{
						  	  				mTab.setValue("Signature3", false);
						  	  				return "Intervalo no válido, revise Hora inicio/Hora fin : "+RL.get_ID();
						  	  			}						  	  			
						  	  		}
					    			
					    								    			
					    		}
					    		if (typeHExtra.equalsIgnoreCase("50"))
					    		{
					    			if (((horai.compareTo(hora2)>0) && horaf.compareTo(hora3)<=0) ||
					    					((horai.compareTo(hora4)>0) && horaf.compareTo(hora5)<=0)   )
					    			{					    				
					    			}
					    			else
					    			{
					    				mTab.setValue("Signature3", false);
					    				return "Intervalo no válido, revise Hora inicio/Hora fin : "+RL.get_ID();
					    			}
					    				
					    		}
					    	}
						}
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
			
			//validacion detalle
			if(doc.getRequestType().equals("PAD") || doc.getRequestType().equals("TCP")
					|| doc.getRequestType().equals("SHE")|| doc.getRequestType().equals("PA")
					|| doc.getRequestType().equals("PSD")|| doc.getRequestType().equals("CMT"))//accion para permisos administrativos ininoles
			{
				int cantRH = DB.getSQLValue(null, "select count(*) from RH_AdministrativeRequestsline "+
						"where RH_AdministrativeRequests_ID=?", doc.getAdministrativeRequests_ID());
			
				if (cantRH < 1)
				{
					mTab.setValue("Signature3", "N");
					return "Solicitud debe Tener Lineas de Detalle";
				}
			}
			
			//comienzo logica de validacion
			if (requestType.equals("PAD") || requestType.equals("PEH") || 
					requestType.equals("PSS") || requestType.equals("SVC") || 
					requestType.equals("TCP") || requestType.equals("CMT") || 
					requestType.equals("STV") || requestType.equals("SHE"))
			{
				X_AD_User user = new X_AD_User(Env.getCtx(), userID, null);
				
				mTab.setValue("Supervisor_ID", user.getSupervisor_ID());
			}			
			else if (requestType.equals("JIN") || requestType.equals("PEF") ||
						requestType.equals("PFF") || requestType.equals("PPM") ||
						requestType.equals("PSD") || requestType.equals("SVP"))
			{				
				if (requestType.equals("PSD"))
				{
					mTab.setValue("Signature1", "Y");
					mTab.setValue("Supervisor_ID", 1000000);
				}
				String sqlUGP = "select MAX(AD_Role_ID) from AD_Role where ugp = 'Y'";
				int roleUGP = DB.getSQLValue(null, sqlUGP);
				mTab.setValue("AD_Role_UGP", roleUGP);
			}
			else 
			{
				String sqlUGP = "select MAX(AD_Role_ID) from AD_Role where ugp = 'Y'";
				int roleUGP = DB.getSQLValue(null, sqlUGP);
				mTab.setValue("AD_Role_UGP", roleUGP);
				
				X_AD_User user = new X_AD_User(Env.getCtx(), userID, null);
				
				mTab.setValue("Supervisor_ID", user.getSupervisor_ID());
			}
									
			//ininoles marcado de fecha cuando se cliquea
			mTab.setValue("dateSignature3",new Timestamp(System.currentTimeMillis()));
				
		}
		//logica de firma jefe directo 
		if (mField.getColumnName().equals("Signature1") && value.equals(true))
		{
			if(requestType.equals("PAD") || requestType.equals("PEH") || 
					requestType.equals("PSS") || requestType.equals("SVC") || 
					requestType.equals("TCP") || requestType.equals("CMT"))
			{
				mTab.setValue("Signature2", "Y");
				mTab.setValue("JDAFBy", 1000000);
			}
			else if (requestType.equals("SHE"))
			{
				String sqlJDAF = "select MAX(AD_Role_ID) from AD_Role where jdaf = 'Y'";
				int roleJDAF = DB.getSQLValue(null, sqlJDAF);
				mTab.setValue("AD_Role_JDAF", roleJDAF);	
			}

			//ininoles marcado de diferencia respecto signature3			
			if (mTab.getValue("dateSignature3") != null)						
				mTab.setValue("difSupervisor",fechasDiferenciaEnDias((Date)mTab.getValue("dateSignature3"), (Date)new Timestamp(System.currentTimeMillis())));						
		}
		//logica de firma usuario UGP
		if (mField.getColumnName().equals("SignatureRRHH") && value.equals(true))
		{
			int idUGP = Env.getAD_User_ID(ctx);
			if(requestType.equals("JIN") || requestType.equals("PEF") ||
					requestType.equals("PFF") || requestType.equals("PPM") ||
					requestType.equals("PSD") || requestType.equals("STV"))
			{				
				String sqlJDAF = "select MAX(AD_Role_ID) from AD_Role where jdaf = 'Y'";
				int roleJDAF = DB.getSQLValue(null, sqlJDAF);
				mTab.setValue("AD_Role_JDAF", roleJDAF);				
				mTab.setValue("UGPBy", idUGP);	
			}
			else if (requestType.equals("SVP"))
			{
				mTab.setValue("UGPBy", idUGP);	
				mTab.setValue("Signature2", "Y");
				mTab.setValue("JDAFBy", 1000000);
			}
			else 
			{
				mTab.setValue("UGPBy", idUGP);
			}
			
			//ininoles marcado de diferencia respecto signature3		
			if (mTab.getValue("dateSignature3") != null)
				mTab.setValue("difUGP",fechasDiferenciaEnDias((Date)mTab.getValue("dateSignature3"), (Date)new Timestamp(System.currentTimeMillis())));
		}
		//logica jefe daf		
		if (mField.getColumnName().equals("Signature2") && value.equals(true))
		{
			mTab.setValue("JDAFBy", Env.getAD_User_ID(ctx));
			//mTab.setValue("JDAFBy",);
		}
		return "";
	}
	//call out para que llene por defecto el Socio de negocio asociado al usuario logeado Administrative Requests
	public String BpartnerDefault (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		int IDUser = Env.getAD_User_ID(ctx);
		
		String sql = "SELECT C_Bpartner_ID FROM AD_User WHERE AD_User_ID = ?";
		
		int IDBPartner = DB.getSQLValue(null, sql, IDUser);
		
		mTab.setValue("C_Bpartner_ID", IDBPartner);
		
		return "";
	}
	
	public String EndDateSVC (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws ParseException
	{
		if(value==null)
			return "";
		
		if (mTab.get_ValueAsString("RequestType").equals("SVC"))			
		{		
									
			java.util.Date fi = (java.util.Date)mTab.getValue("datestartrequest");
			BigDecimal qtyDays= (BigDecimal)mTab.getValue("NumberDays");
			int qty = Integer.valueOf(qtyDays.intValue());
			
			//Date ff = calculateEndDate(fi, qty); se cambia metodo para que tambien tome los feriados} ininoles				
			java.util.Date ff = calculateEndDateFeriados(fi, qty);

			Timestamp cff = new Timestamp(ff.getTime());
			
			mTab.setValue("dateendrequest", cff);			
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
	
	public static int fechasDiferenciaEnDias(Date fechaInicial, Date fechaFinal) {

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


