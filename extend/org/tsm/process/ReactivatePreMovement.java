/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Contributor(s): Chris Farley - northernbrewer                              *
 *****************************************************************************/
package org.tsm.process;


import org.compiere.model.X_Pre_M_Movement;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	CopyFromJobStandar
 *	
 */
public class ReactivatePreMovement extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_PreMovment_ID;
	//private String p_Action;
		
	protected void prepare()
	{	
		p_PreMovment_ID = getRecord_ID();
		
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		X_Pre_M_Movement preMov = new X_Pre_M_Movement(getCtx(), p_PreMovment_ID, get_TrxName());		
		//procesamos por primera vez para traer lineas
		if(preMov.getDocStatus().compareTo("CO") == 0)
		{
			DB.executeUpdate("UPDATE Pre_M_Movement SET DocStatus = 'WC', Processed = 'N' WHERE Pre_M_Movement_ID = "+p_PreMovment_ID, get_TrxName());
			DB.executeUpdate("UPDATE Pre_M_MovementLine SET Processed = 'N' WHERE Pre_M_Movement_ID = "+p_PreMovment_ID, get_TrxName());
		}
		return "Procesado";		
	}	//	doIt
}	//	Replenish
