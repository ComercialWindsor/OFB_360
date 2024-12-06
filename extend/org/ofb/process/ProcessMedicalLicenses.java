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
import java.util.Calendar;
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
public class ProcessMedicalLicenses extends SvrProcess
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
		
		X_RH_MedicalLicenses doc=new X_RH_MedicalLicenses(Env.getCtx(), Record_ID,get_TrxName());
		
		if(P_DocAction.equals("IP") && !doc.isProcessed()){
			
			doc.setProcessed(true);
			doc.setDocStatus("IP");
			doc.save();
			return "Procesado";
		}

		if(P_DocAction.equals("CO") &&  (!doc.isProcessed() || doc.getDocStatus().equals("IP")  ) ){
			
			//validacion no deben existir documentos en las mismas fechas

			Timestamp fechaInicio = (Timestamp)doc.get_Value("datestartrequest");

	        Calendar calCalendario = Calendar.getInstance();
	        calCalendario.setTimeInMillis(fechaInicio.getTime());
	        calCalendario.add(Calendar.DATE, doc.getDays().intValueExact());

	        Timestamp fechaFin = new Timestamp(calCalendario.getTimeInMillis());
	        
	        //validacion cometidos 
	        
	        String sqlValid1 = "SELECT COUNT(DISTINCT (ar.rh_administrativerequests_ID)) "+
	        "FROM rh_administrativerequests  ar "+	        
	        "WHERE "+
	        "( "+
	        "( "+
	        "( "+ 
	        "? >= ar.datestartrequest "+ 
	        "AND "+ 
	        "? <= ar.dateendrequest "+
	        ") "+ 
	        "OR "+ 
	        "( "+ 
	        "? >= ar.datestartrequest "+
	        "AND "+ 
	        "? <= ar.dateendrequest "+ 
	        ") "+
	        ") "+ 
	        "OR "+ 
	        "( "+ 
	        "( "+ 
	        "ar.datestartrequest >= ? "+ 
	        "AND "+ 
	        "ar.datestartrequest <= ? "+
	        ") "+
	        "OR "+ 
	        "( "+ 
	        "ar.dateendrequest >= ? "+
	        "AND "+ 
	        "ar.dateendrequest <=  ? "+ 
	        ") "+
	        ") "+
	        ") "+		
	        "AND ar.c_bpartner_id = ? "+
	        "AND ar.requesttype in ('CMT') "+ 
	        "AND ar.IsActive = 'Y' "+
	        "AND ar.DocStatus not in ('VO') ";
	        
	        int cantValid1 = DB.getSQLValue(get_TrxName(), sqlValid1, fechaInicio,fechaInicio,
					fechaFin,fechaFin,fechaInicio,fechaFin,fechaInicio,fechaFin,doc.getC_BPartner_ID());
					
	        if (cantValid1 > 0)
	        {
	        	throw new AdempiereException("No se puede completar Licencia Medica. Existe un Cometido en Conflicto");
	        	//return "No se puede completar Licencia Medica. Existe un Cometido en Conflicto";
	        }
	        
	        //validacion vacaciones
	        
	        String sqlValid2 = "SELECT COUNT(DISTINCT (ar.rh_administrativerequests_ID)) "+
	        "FROM rh_administrativerequests  ar "+	        
	        "WHERE "+
	        "( "+
	        "( "+
	        "( "+ 
	        "? >= ar.datestartrequest "+ 
	        "AND "+ 
	        "? <= ar.dateendrequest "+
	        ") "+ 
	        "OR "+ 
	        "( "+ 
	        "? >= ar.datestartrequest "+
	        "AND "+ 
	        "? <= ar.dateendrequest "+ 
	        ") "+
	        ") "+ 
	        "OR "+ 
	        "( "+ 
	        "( "+ 
	        "ar.datestartrequest >= ? "+ 
	        "AND "+ 
	        "ar.datestartrequest <= ? "+
	        ") "+
	        "OR "+ 
	        "( "+ 
	        "ar.dateendrequest >= ? "+
	        "AND "+ 
	        "ar.dateendrequest <=  ? "+ 
	        ") "+
	        ") "+
	        ") "+		
	        "AND ar.c_bpartner_id = ? "+
	        "AND ar.requesttype in ('SVC') "+ 
	        "AND ar.IsActive = 'Y' "+
	        "AND ar.DocStatus not in ('VO') ";
	        
	        int cantValid2 = DB.getSQLValue(get_TrxName(), sqlValid2, fechaInicio,fechaInicio,
					fechaFin,fechaFin,fechaInicio,fechaFin,fechaInicio,fechaFin,doc.getC_BPartner_ID());
					
	        if (cantValid2 > 0)
	        	return "No se puede completar Licencia Medica. Existe una Solicitud de Feriado Legal en Conflicto";
	        
	        //validacion Permisos administrativos
	        
	        String sqlValid3 = "SELECT COUNT(DISTINCT (ar.rh_administrativerequests_ID)) "+
	        "FROM rh_administrativerequests  ar "+ 
	        "LEFT JOIN RH_AdministrativeRequestsLine arl on (ar.rh_administrativerequests_ID = arl.rh_administrativerequests_ID ) "+
	        "WHERE "+ 
	        "( "+
	        "arl.datestartrequest >= ? "+
	        "AND "+ 
	        "arl.datestartrequest <= ? "+	
	        ") "+		
	        "AND ar.c_bpartner_id = ? "+
	        "AND ar.requesttype in ('PAD') "+ 
	        "AND ar.IsActive = 'Y' "+
	        "AND ar.DocStatus not in ('VO') "; 

	        int cantValid3 = DB.getSQLValue(get_TrxName(), sqlValid3, fechaInicio,
					fechaFin,doc.getC_BPartner_ID());
	        					
	        if (cantValid3 > 0)
	        	return "No se puede completar Licencia Medica. Existe un Permiso Administrativo en Conflicto";
	        
	        //validacion Horas Extra
	        
	        String sqlValid4 = "SELECT COUNT(DISTINCT (ar.rh_administrativerequests_ID)) "+
	        "FROM rh_administrativerequests  ar "+ 
	        "WHERE "+ 
	        "( "+
	        "ar.datestartrequest >= ? "+
	        "AND "+ 
	        "ar.datestartrequest <= ? "+	
	        ") "+		
	        "AND ar.c_bpartner_id = ? "+
	        "AND ar.requesttype in ('SHE') "+ 
	        "AND ar.IsActive = 'Y' "+
	        "AND ar.DocStatus not in ('VO')";
	        
	        int cantValid4 = DB.getSQLValue(get_TrxName(), sqlValid4, fechaInicio,
					fechaFin,doc.getC_BPartner_ID());
	        					
	        if (cantValid4 > 0)
	        	return "No se puede completar Licencia Medica. Existe una Solicitud de Hora Extra en Conflicto";
	        
			
			 
			doc.setProcessed(true);
			doc.setDocStatus("CO");
			doc.save();
			return "Completado";
		}
		
		if(P_DocAction.equals("VO") && doc.isProcessed() && doc.getDocStatus().equals("CO")){
			doc.setProcessed(true);
			doc.setDocStatus("VO");
			doc.save();
			return "Anulado";
		}

			return "No es posible Cumplir la Accion ";
		
	}	//	doIt


	
}	//	CopyOrder
