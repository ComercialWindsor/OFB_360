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
package org.geminis.process;

import java.util.logging.Level;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
 
/**
 *	Generate XML from Invoice
 *	
 *  @author ininoles
 *  @version $Id: ProcessTimeExpense.java,v 1.2 19/05/2011 $
 */
public class ProcessTimeExpense extends SvrProcess
{
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int 	Record_ID;
	private String P_DocAction;
	
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
		Record_ID = getRecord_ID();
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		MTimeExpense exp = new MTimeExpense(getCtx(), Record_ID, get_TrxName());
		if(P_DocAction != null)
		{
			if(P_DocAction.compareTo("CO") == 0 
					|| P_DocAction.compareTo("VO") == 0 
					|| P_DocAction.compareTo("CL") == 0 )
			{
				exp.processIt("P_DocAction");
			}
			else if (exp.getDocStatus().compareTo("CO") == 0
					&& P_DocAction.compareTo("RE") == 0)
			{
				exp.setProcessed(false);
				exp.setDocStatus("DR");
				exp.setApprovalAmt(Env.ZERO);
			}
			exp.saveEx(get_TrxName());
		}		
		return "Procesado";
	}	//	doIt
	
}	//	InvoiceCreateInOut
