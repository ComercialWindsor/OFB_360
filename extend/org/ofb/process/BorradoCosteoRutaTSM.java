/******************************************************************************
0 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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

import java.sql.Timestamp;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
 
/**
 *	report infoprojectPROVECTIS
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class BorradoCosteoRutaTSM extends SvrProcess
{
	private int	p_ProjectOFB_ID = 0;	
	private Timestamp p_MovementDateFrom = null;
	private Timestamp p_MovementDateTo = null;
	private int	p_C_Period_ID = 0;
//	private int p_PInstance_ID;		

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_ProjectOFB_ID"))
			{
				p_ProjectOFB_ID = para[i].getParameterAsInt();				
			}
			else if (name.equals("C_Period_ID"))
			{
				p_C_Period_ID = para[i].getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		//p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		//validacion de periodo cerrado
		MPeriod period = new MPeriod(getCtx(), p_C_Period_ID, get_TrxName());
		String sqlCP = "SELECT periodstatus FROM C_PeriodControl WHERE docbasetype = 'MMM' AND C_Period_ID = ?";
		String periodS = DB.getSQLValueString(get_TrxName(), sqlCP, p_C_Period_ID);
		
		if (periodS.compareTo("C") == 0 )
		{
			return "No se puede borrar el costo de un periodo cerrado";
		}
		else//se asignan valores de rango de fechas 
		{
			p_MovementDateFrom = period.getStartDate();
			p_MovementDateTo = period.getEndDate();
		}
		
		String sqlPrincipal = "update m_movementline set cost=0, kmacu=0, volacu=0,Tract=0 "+
				"where m_movementline_id IN "+ 
				"(SELECT m_movementline_id "+ 
				" FROM m_movementline mml "+
				" INNER JOIN m_movement mm on (mml.m_movement_id = mm.m_movement_id) "+ 
				" WHERE mm.c_projectofb_id = "+p_ProjectOFB_ID+
				" AND mm.movementdate between '"+p_MovementDateFrom+"' and '"+p_MovementDateTo+"') ";
		
		try
		{
			DB.executeUpdate(sqlPrincipal, get_TrxName());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "Hojas de Ruta Reiniciadas: Flota "+p_ProjectOFB_ID+" entre fechas "+p_MovementDateFrom+" and "+p_MovementDateTo;
	}	//	doIt
	
}	//	OrderOpen
