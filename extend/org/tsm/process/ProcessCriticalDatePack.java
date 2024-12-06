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
import java.util.logging.Level;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;
import org.ofb.utils.DateUtils;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ProcessCriticalDateTSM.java,v 1.2 2019/10/07 00:51:01  $
 */
public class ProcessCriticalDatePack extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	int			p_BParter_ID;
	int 		p_CDateConcept_ID;
	Timestamp	p_date;
	protected void prepare()
	{	
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("C_BPartner_ID"))
				p_BParter_ID = para[i].getParameterAsInt();
			else if (name.equals("DateTrx"))
				p_date = (Timestamp)para[i].getParameter();
			else if (name.equals("C_CriticalDateConcept_ID"))
				p_CDateConcept_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{		
		X_C_CriticalDateConcept co = new X_C_CriticalDateConcept(getCtx(), p_CDateConcept_ID, get_TrxName());
		//no es concepto padre
		if(!co.get_ValueAsBoolean("IsSummary"))
		{
			int cant = DB.getSQLValue(get_TrxName(),"SELECT MAX(C_CriticalDate_ID) " +
					" FROM C_CriticalDate WHERE IsActive='Y' AND Status='AL' " +
					" AND C_CriticalDateConcept_ID = "+co.get_ID()+
					" AND C_Bpartner_ID = "+p_BParter_ID);
			//si no existe se crea registro
			if(cant <=0)
			{
				X_C_CriticalDate cDate = new X_C_CriticalDate(getCtx(),0, get_TrxName());
				cDate.setDateTrx(p_date);
				cDate.setC_BPartner_ID(p_BParter_ID);
				cDate.setC_CriticalDateConcept_ID(p_CDateConcept_ID);
				if(co.getIsDue().compareTo(Env.ZERO) > 0)
				{
					Timestamp dateEnd = DateUtils.SumarMeses(cDate.getDateTrx(), 
							cDate.getC_CriticalDateConcept().getIsDue().multiply(new BigDecimal("12.0")).intValue());
					cDate.setDueDate(dateEnd);				
				}
				cDate.setStatus("AL");
				cDate.setDocStatus("DR");
				//cDate.setProcessed(true);
				cDate.saveEx(get_TrxName());
			}
		}else // es concepto padre
		{
			String sql = "SELECT * FROM RVOFB_FC_Concept_Parent WHERE C_CriticalDateConceptRef_ID=?";
			int cant = 0;
			PreparedStatement ps = DB.prepareStatement(sql, get_TrxName());
			ps.setInt(1, co.get_ID());
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) 
			{
				//se busca si existe una fecha critica ya existente
				cant = DB.getSQLValue(get_TrxName(),"SELECT MAX(C_CriticalDate_ID) " +
						" FROM C_CriticalDate WHERE IsActive='Y' AND Status='AL' " +
						" AND C_CriticalDateConcept_ID = "+rs.getInt("C_CriticalDateConcept_ID")+
						" AND C_Bpartner_ID = "+p_BParter_ID);				
				//si no existe se crea registro
				if(cant <=0)
				{
					X_C_CriticalDateConcept con2 = new X_C_CriticalDateConcept(getCtx(), rs.getInt("C_CriticalDateConcept_ID"), get_TrxName());
					
					X_C_CriticalDate cDate = new X_C_CriticalDate(getCtx(),0, get_TrxName());
					cDate.setDateTrx(p_date);
					cDate.setC_BPartner_ID(p_BParter_ID);
					cDate.setC_CriticalDateConcept_ID(rs.getInt("C_CriticalDateConcept_ID"));
					if(con2.getIsDue().compareTo(Env.ZERO) > 0)
					{
						Timestamp dateEnd = DateUtils.SumarMeses(p_date, 
								cDate.getC_CriticalDateConcept().getIsDue().multiply(new BigDecimal("12.0")).intValue());
						cDate.setDueDate(dateEnd);				
					}
					cDate.setStatus("AL");
					cDate.setDocStatus("DR");
					//cDate.setProcessed(true);
					cDate.saveEx(get_TrxName());
				}
			}
			rs.close();
			ps.close();
		}
		return "Procesado";
	}	//	doIt	
}	//	CopyOrder

