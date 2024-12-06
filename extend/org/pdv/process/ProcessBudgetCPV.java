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
package org.pdv.process;

import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Italo Niñoles Ininoles
 *  @version $Id: ProcessPaymentRequest.java,v 1.2 2014/08/26 10:11:01  $
 */
public class ProcessBudgetCPV extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private String p_status;
	private int p_IDBudget;
	private Properties 		m_ctx;
		
	protected void prepare()
	{		
		m_ctx = Env.getCtx();
		p_IDBudget = getRecord_ID();
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("Status"))
				p_status = (String)para[i].getParameter();			
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
		X_EO_BudgetHdr budHdr = new X_EO_BudgetHdr(getCtx(), p_IDBudget, get_TrxName());
		
		if (p_status.length() > 0)
		{
			budHdr.setStatus(p_status);
			if (p_status.compareTo("AD") == 0 || p_status.compareTo("AP") == 0)
			{
				budHdr.set_CustomColumn("Processed", true);
			}
			budHdr.save();
		}			
		if (p_status.length() > 0)
		{
			budHdr.setStatus(p_status);
			if (p_status.compareTo("EP") == 0)
			{
				budHdr.set_CustomColumn("Processed", false);
			}
			budHdr.save();
		}
		
		
		return "Procesado";
	}	//	doIt	
}	//	CopyOrder
