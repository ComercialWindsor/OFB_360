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

import java.util.logging.Level;

import org.compiere.util.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

 
/**
 *	Generate XML consolidated from Invoice 
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: UpdateSupervisor.java,v 1.2 19/05/2011 $
 */
public class UpdateSupervisor extends SvrProcess
{
	/** Properties						*/
		
	
	private int p_Supervisor_ID;
	private int p_SupervisorTo_ID;

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
			else if (name.equals("Supervisor_ID"))
				p_Supervisor_ID = para[i].getParameterAsInt();
			else if (name.equals("SupervisorTo_ID"))
				p_SupervisorTo_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare
	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		String sqlUpdate = "UPDATE AD_User SET Supervisor_ID = "+p_SupervisorTo_ID+" WHERE DefaultSupervisor_ID = "+p_Supervisor_ID;
		DB.executeUpdate(sqlUpdate, get_TrxName());
		
		return "Usuarios de Supervisor_ID:"+p_Supervisor_ID+" han sido reasignados a Supervisor_ID:"+p_SupervisorTo_ID;
	}	//	doIt
	
}	//	InvoiceCreateInOut
