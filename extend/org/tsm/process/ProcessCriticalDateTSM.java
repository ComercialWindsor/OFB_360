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

import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;
import org.ofb.utils.DateUtils;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ProcessCriticalDateTSM.java,v 1.2 2019/10/07 00:51:01  $
 */
public class ProcessCriticalDateTSM extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int 			Record_ID;
	
	protected void prepare()
	{	
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{		
		X_C_CriticalDate cDate = new X_C_CriticalDate(Env.getCtx(), Record_ID,get_TrxName());
				
		//ininoles se valida que exista adjunto
		int flag = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM ad_attachment WHERE AD_Table_ID="+X_C_CriticalDate.Table_ID+
				" AND record_id="+cDate.get_ID());
		if(flag <=0)
		{
			throw new AdempiereException("ERROR: Debe Adjuntar documento");
		}
		else
		{	
			String sqlValid= "SELECT MAX(C_CriticalDate_ID) FROM C_CriticalDate WHERE IsActive='Y' AND Status='AL' " +
					" AND C_CriticalDateConcept_ID = "+cDate.getC_CriticalDateConcept_ID()+" AND C_CriticalDate_ID NOT IN ("+cDate.get_ID()+")";
			if(cDate.getC_BPartner_ID() > 0)
				sqlValid = sqlValid+" AND C_Bpartner_ID = "+cDate.getC_BPartner_ID();
			else if(cDate.getA_Asset_ID() > 0)
				sqlValid = sqlValid+" AND A_Asset_ID = "+cDate.getA_Asset_ID();
			
			int ID_CDAte = DB.getSQLValue(get_TrxName(), sqlValid);
			//existe registro anterior. Se da de baja y se completa registro nuevo
			if(ID_CDAte > 0)
			{
				X_C_CriticalDate cDateOld = new X_C_CriticalDate(getCtx(), ID_CDAte, get_TrxName());
				cDateOld.setStatus("BA");
				cDateOld.saveEx(get_TrxName());
				
				if(cDate.getC_CriticalDateConcept().getIsDue().compareTo(Env.ZERO) > 0)
				{
					Timestamp dateEnd = DateUtils.SumarMeses(cDate.getDateTrx(), 
							cDate.getC_CriticalDateConcept().getIsDue().multiply(new BigDecimal("12.0")).intValue());
					cDate.setDueDate(dateEnd);				
				}
				cDate.setDocStatus("CO");
				cDate.setProcessed(true);
			}
			else //si no existe solo se completa registro
			{
				cDate.setDocStatus("CO");
				cDate.setProcessed(true);			
			}		
			//se genera incidencia
			X_C_CriticalDateConcept concept = new X_C_CriticalDateConcept(getCtx(), cDate.getC_CriticalDateConcept_ID(),get_TrxName());
			if(concept.get_ValueAsBoolean("genPreB"))
			{
				//ininoles se genera 1 incidencia por dia
				Timestamp fechaEmision = cDate.getDateTrx();					
				Timestamp fechaVencimiento = cDate.getDueDate();
				Calendar calCalendario = Calendar.getInstance();
				if(fechaEmision != null && fechaVencimiento != null)
				{	
					while (fechaEmision.compareTo(fechaVencimiento) <= 0)
					{	
						X_HR_Prebitacora preB = new X_HR_Prebitacora(getCtx(), 0, get_TrxName());
						preB.setAD_Org_ID(cDate.getAD_Org_ID());
						if(cDate.getC_BPartner_ID() > 0)
						{
							preB.setColumnType("B");
							preB.setC_BPartner_ID(cDate.getC_BPartner_ID());
						}
						else if (cDate.getA_Asset_ID() > 0)
						{
							preB.setColumnType("A");
							preB.setA_Asset_ID(cDate.getA_Asset_ID());
						}
						preB.setDateTrx(fechaEmision);
						preB.set_CustomColumn("C_CriticalDate_ID", cDate.get_ID());
						preB.setHR_Concept_TSM_ID(1000005);
						preB.saveEx(get_TrxName());		
						
						//se le suma un dia a la fecha de emision
						calCalendario.setTimeInMillis(fechaEmision.getTime());
						calCalendario.add(Calendar.DATE,1);
						fechaEmision = new Timestamp(calCalendario.getTimeInMillis());
					}
				}	
			}		
		}
		cDate.saveEx(get_TrxName());
		return "Procesado";
	}	//	doIt	
}	//	CopyOrder

