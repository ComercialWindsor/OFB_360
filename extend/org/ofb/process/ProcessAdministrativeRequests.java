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

import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class ProcessAdministrativeRequests extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private String P_DocAction;
	/*OT ID*/
	private int 			Record_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DocAction"))
				P_DocAction = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		X_RH_AdministrativeRequests doc=new X_RH_AdministrativeRequests(Env.getCtx(), Record_ID,get_TrxName());
		
		if (doc.getRequestType().equals("CMT") || doc.getRequestType().equals("JIN") || doc.getRequestType().equals("PFF")
				|| doc.getRequestType().equals("PPM") || doc.getRequestType().equals("PFF") || doc.getRequestType().equals("PSS")
				|| doc.getRequestType().equals("PSD") || doc.getRequestType().equals("SVC") || doc.getRequestType().equals("SHE")) 
		{
			String sqlValidPeriod = "SELECT MAX(CPC.periodStatus) FROM C_Period CP "+
					"INNER JOIN C_PeriodControl CPC ON (CP.C_Period_ID = CPC.C_Period_ID) "+ 
					"WHERE ? between startdate and (endDate + '23 hour'::interval + '59 minutes'::interval +'59 seconds'::interval)::timestamp without time zone "+
					"AND DocBaseType = 'SOO'";
			
			String periodStatus = DB.getSQLValueString(get_TrxName(), sqlValidPeriod, doc.getdatestartrequest());
			
			if (!periodStatus.equalsIgnoreCase("O"))
			{
				throw new AdempiereException("No se puede procesar la solicitud. Periodo Cerrado");
				//return "No se puede procesar la solicitud. Periodo Cerrado";				
			}
		}
		
		String sqlValid = "SELECT COUNT(DISTINCT (ar.rh_administrativerequests_ID)) "+
				"FROM rh_administrativerequests  ar "+
				"LEFT JOIN RH_AdministrativeRequestsLine arl on (ar.rh_administrativerequests_ID = arl.rh_administrativerequests_ID ) "+
				"WHERE "+    
				"( "+
				"	( ? >= ar.datestartrequest "+    
				"	  AND "+ 
				"	  ? <= ar.dateendrequest "+
				"	) "+      
				"	OR "+
				"	( ? >= ar.datestartrequest "+   
				"	  AND "+ 
				"     ? <= ar.dateendrequest "+
				"	) "+
				") "+
				"AND ar.c_bpartner_id = ? "+   
				"AND ar.requesttype = 'CMT' "+   
				"AND ar.rh_administrativerequests_ID <> ? "+ 
				"AND ar.IsActive = 'Y' "+ 
				"AND ar.DocStatus not in ('VO') "+
				"AND arl.C_City_ID IN " +
				"	(SELECT C_City_ID FROM RH_AdministrativeRequestsLine WHERE rh_administrativerequests_ID = ?)";
		int cantValid = DB.getSQLValue(get_TrxName(), sqlValid, doc.getdatestartrequest(),doc.getdatestartrequest(),
					doc.getdateendrequest(),doc.getdateendrequest(),doc.getC_BPartner_ID(),doc.get_ID(),doc.get_ID());
		
		if (doc.getRequestType().equals("CMT") && cantValid > 0)
		{
				return "No se puede procesar el cometido, existen "+ cantValid +" cometidos en conflicto";
		}
		else
		{
		
			if(P_DocAction.equals("IP") && !doc.isProcessed()){
			
				doc.setProcessed(true);
				doc.setDocStatus("IP");
				doc.save();
				return "Procesado";
			}

			if(P_DocAction.equals("CO") &&  (!doc.isProcessed() || doc.getDocStatus().equals("IP")  ) ){
			
				if(doc.getRequestType().equals("SHE"))
					{
					String TypeSHE = doc.get_ValueAsString("TypeSHE");				
					if (TypeSHE.equals("TC"))
					{
						BigDecimal hh25 = DB.getSQLValueBD(get_TrxName(), "select trunc( " +
								"CAST(" + 
								"(sum(extract('epoch' from (endtime - starttime))/60)/60) " +
								"as numeric) " +
								",1) " +
								"from RH_AdministrativeRequestsLine " +
								" where  RH_AdministrativeRequests_ID=? and Percentage='25'", doc.getAdministrativeRequests_ID());
					
						if(hh25!=null && hh25.signum()>0)
							hh25= hh25.multiply(new BigDecimal("1.25"));
						else
							hh25=Env.ZERO;
					
					
						BigDecimal hh50 = DB.getSQLValueBD(get_TrxName(), "select trunc( " +
							"CAST(" + 
							"(sum(extract('epoch' from (endtime - starttime))/60)/60) " +
							"as numeric) " +
							",1) " +
							"from RH_AdministrativeRequestsLine " +
							" where  RH_AdministrativeRequests_ID=? and Percentage='50'", doc.getAdministrativeRequests_ID());
				
						if(hh50!=null && hh50.signum()>0)
							hh50= hh50.multiply(new BigDecimal("1.5"));
						else
							hh50=Env.ZERO;
					
						BigDecimal hh = hh25.add(hh50);
					
						MBPartner bp = MBPartner.get(getCtx(), doc.getC_BPartner_ID());
										
						bp.set_CustomColumn("numberhours", ((BigDecimal)bp.get_Value("numberhours")).add(hh.setScale(1, BigDecimal.ROUND_DOWN)));
						bp.save();				
					}
				}
			
				if(doc.getRequestType().equals("PEF"))
				{
					MBPartner bp = MBPartner.get(getCtx(), doc.getC_BPartner_ID());				
					BigDecimal dd = DB.getSQLValueBD(get_TrxName(), "select extract(days from(dateendrequest-datestartrequest)) from RH_AdministrativeRequests where " +
						"RH_AdministrativeRequests_ID = ?", doc.getAdministrativeRequests_ID());
					bp.set_CustomColumn("RH_AcumulateDays", ((BigDecimal)bp.get_Value("RH_AcumulateDays")).subtract(dd));
					bp.save();
				}
				if(doc.getRequestType().equals("SVC"))//Logica para feriado legal tomando campo cantidad ininoles 
				{
					MBPartner bp = MBPartner.get(getCtx(), doc.getC_BPartner_ID());
					BigDecimal dd = DB.getSQLValueBD(get_TrxName(), "select NumberDays from RH_AdministrativeRequests where " +
						"RH_AdministrativeRequests_ID = ?", doc.getAdministrativeRequests_ID());
				
					BigDecimal SVCep = DB.getSQLValueBD(get_TrxName(), "select coalesce ((sum(NumberDays)),0) from RH_AdministrativeRequests ar "+
						"where ar.requesttype like 'SVC' and (ar.docstatus = 'DR' OR ar.docstatus = 'IP') and ar.isactive = 'Y' and ar.Signature3 = 'Y' "+
						"and ar.c_bpartner_id = ? and ar.RH_AdministrativeRequests_ID not in (?)",bp.get_ID(),doc.getAdministrativeRequests_ID());
				
					BigDecimal cantSVC = (BigDecimal)bp.get_Value("RH_AcumulateDays");
											
					if (dd.add(SVCep).compareTo(cantSVC) > 0)
					{
						return "Dias Legales Disponibles Insuficientes";
					}
					else 
					{
						bp.set_CustomColumn("RH_AcumulateDays", ((BigDecimal)bp.get_Value("RH_AcumulateDays")).subtract(dd));
						bp.save();					
					}				
				}//end ininoles 
			
				if(doc.getRequestType().equals("PFF") || doc.getRequestType().equals("PPM"))
				{
					MBPartner bp = MBPartner.get(getCtx(), doc.getC_BPartner_ID());
					BigDecimal dd = DB.getSQLValueBD(get_TrxName(), "select extract(days from(dateendrequest-datestartrequest)) from RH_AdministrativeRequests where " +
						"RH_AdministrativeRequests_ID = ?", doc.getAdministrativeRequests_ID());
					bp.set_CustomColumn("RH_AdicionalDays", ((BigDecimal)bp.get_Value("RH_AcumulateDays")).subtract(dd));
					bp.set_CustomColumn("RH_ExpirationAdD", TimeUtil.addDays(null, 7));
					bp.save();
				}
			
				if(doc.getRequestType().equals("SVP"))
				{
					MBPartner bp = MBPartner.get(getCtx(), doc.getC_BPartner_ID());
					bp.set_CustomColumn("RH_LegalyDays", doc.get_Value("NumberDays"));
					bp.save();
				}
			
				if(doc.getRequestType().equals("STV"))
				{
					MBPartner bp = MBPartner.get(getCtx(), doc.getC_BPartner_ID());
					bp.set_CustomColumn("RH_AcumulateDays", doc.get_Value("NumberDays"));
					bp.set_CustomColumn("RH_ExpirationAdD", doc.getdateendrequest());
					bp.save();
				}
				if(doc.getRequestType().equals("PAD"))//accion para permisos administrativos ininoles
				{								
					MBPartner bp = MBPartner.get(getCtx(), doc.getC_BPartner_ID());
					BigDecimal dd = DB.getSQLValueBD(get_TrxName(), "select SUM( case when block = 'MDM' then 0.5 "+
									"when block = 'MDT' then 0.5 when block = 'DIA' then 1 end) "+
									"from RH_AdministrativeRequestsline where RH_AdministrativeRequests_ID=?", doc.getAdministrativeRequests_ID());
				
					BigDecimal PADep = DB.getSQLValueBD(get_TrxName(), "select coalesce ((SUM(case when arl.block = 'MDM' then 0.5 "+
						"when arl.block = 'MDT' then 0.5 when arl.block = 'DIA' then 1 end)),0) from RH_AdministrativeRequestsline arl "+
						"inner join RH_AdministrativeRequests ar on (arl.RH_AdministrativeRequests_id = ar.RH_AdministrativeRequests_id) "+
						"where ar.requesttype like 'PAD' and (ar.docstatus = 'DR' OR ar.docstatus = 'IP') and ar.isactive = 'Y' and ar.Signature3 = 'Y' "+
						"and ar.c_bpartner_id = ? and ar.RH_AdministrativeRequests_ID not in (?)",bp.get_ID(),doc.getAdministrativeRequests_ID());
				
					BigDecimal cantPAD = (BigDecimal)bp.get_Value("RH_AdministrativeDays");
											
					if (dd.add(PADep).compareTo(cantPAD) > 0)
					{
						return "Dias Administrativos Disponibles Insuficientes";
					}
					else 
					{
						bp.set_CustomColumn("RH_AdministrativeDays", ((BigDecimal)bp.get_Value("RH_AdministrativeDays")).subtract(dd));
						bp.save();					
					}								
				}			//end ininoles
				if(doc.getRequestType().equals("PSD"))//accion para permisos sindicales ininoles
				{								
					MBPartner bp = MBPartner.get(getCtx(), doc.getC_BPartner_ID());				
					BigDecimal dd = DB.getSQLValueBD(get_TrxName(), "select SUM(numberhours) as qty " +
								"from RH_AdministrativeRequestsline " +
								"where RH_AdministrativeRequests_ID = ?", doc.getAdministrativeRequests_ID());
				
					BigDecimal PSDep = DB.getSQLValueBD(get_TrxName(), "select coalesce ((SUM(numberhours)),0) as qty from RH_AdministrativeRequestsline arl "+
						"inner join RH_AdministrativeRequests ar on (arl.RH_AdministrativeRequests_id = ar.RH_AdministrativeRequests_id) "+
						"where ar.requesttype like 'PSD' and (ar.docstatus = 'DR' OR ar.docstatus = 'IP') and ar.isactive = 'Y' and ar.Signature3 = 'Y' "+
						"and ar.c_bpartner_id = ? and ar.RH_AdministrativeRequests_ID not in (?)",bp.get_ID(),doc.getAdministrativeRequests_ID());
				
					BigDecimal cantPSD = (BigDecimal)bp.get_Value("RH_UnionHours");
				
					if (dd.add(PSDep).compareTo(cantPSD) > 0)
					{
						return "Horas Sindicales Disponibles Insuficientes";
					}
					else 
					{
						bp.set_CustomColumn("RH_UnionHours", ((BigDecimal)bp.get_Value("RH_UnionHours")).subtract(dd));
						bp.save();
					}				
				}			//end ininoles
				if(doc.getRequestType().equals("TCP"))//accion para tiempo compensado ininoles
					{											
					MBPartner bp = MBPartner.get(getCtx(), doc.getC_BPartner_ID());											
					BigDecimal dd = DB.getSQLValueBD(get_TrxName(), "select SUM(numberhours) as qty "+
											"from RH_AdministrativeRequestsline "+
											"where RH_AdministrativeRequests_ID = ?", doc.getAdministrativeRequests_ID());
				
					int cantTCPD = DB.getSQLValue(get_TrxName(), "select count(*) from RH_AdministrativeRequestsline "+
							"where RH_AdministrativeRequests_ID=?", doc.getAdministrativeRequests_ID());
				
					if (cantTCPD < 1)
					{
						return "Solicitud Tiempo Compensado debe Tener Detalle";
					}
				
					BigDecimal TCPep = DB.getSQLValueBD(get_TrxName(), "select coalesce ((SUM(numberhours)),0) as qty from RH_AdministrativeRequestsline arl "+
						"inner join RH_AdministrativeRequests ar on (arl.RH_AdministrativeRequests_id = ar.RH_AdministrativeRequests_id) "+
						"where ar.requesttype like 'TCP' and (ar.docstatus = 'DR' OR ar.docstatus = 'IP') and ar.isactive = 'Y' and ar.Signature3 = 'Y'"+
						"and ar.c_bpartner_id = ? and ar.RH_AdministrativeRequests_ID not in (?)",bp.get_ID(),doc.getAdministrativeRequests_ID());
				
					BigDecimal cantTCP = (BigDecimal)bp.get_Value("numberhours");
				
					if (dd.add(TCPep).compareTo(cantTCP) > 0)
					{
						return "Horas Acumuladas Disponibles Insuficientes";
					}
					else 
					{
						bp.set_CustomColumn("numberhours", ((BigDecimal)bp.get_Value("numberhours")).subtract(dd));
						bp.save();
					}				
				}
				if(doc.getRequestType().equals("CMT"))//accion para cometido ininoles
				{
					Date dateStar = new Date(doc.getdatestartrequest().getTime());
					Date dateEnd = new Date(doc.getdateendrequest().getTime());			
					
					Timestamp dateStarTS = new Timestamp(dateStar.getTime());
					Timestamp dateEndTS = new Timestamp(dateEnd.getTime());
					
					String sqlCMT = "SELECT COUNT(*) FROM RH_MedicalLicenses "+
							"WHERE datestartrequest between ? and  ? " +
							" AND C_BPartner_ID = ?";
					
					int cantLM = DB.getSQLValue(get_TrxName(), sqlCMT,dateStarTS,dateEndTS,doc.getC_BPartner_ID());
					
					if (cantLM > 0)
					{
						throw new AdempiereException("No se puede completar, existe una licencia medica en conflicto");
						//return "No se puede completar, existe una licencia medica en conflicto";
					}					
				}
				
				//end ininoles
			
			
				doc.setProcessed(true);
				doc.setDocStatus("CO");
				doc.save();
				return "Completado";
			}
		
			if(P_DocAction.equals("VO")){
				doc.setProcessed(true);
				doc.setDocStatus("VO");
				doc.save();
				return "Anulado";
			}
		}

		return "No es posible Cumplir la Accion ";
		
	}	//	doIt


	
}	//	CopyOrder
