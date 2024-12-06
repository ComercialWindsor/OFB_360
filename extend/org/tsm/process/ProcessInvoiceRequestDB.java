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

import org.compiere.model.MRequisition;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class ProcessInvoiceRequestDB extends SvrProcess
{
	private int				p_C_Invoice_ID = 0; 
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 p_C_Invoice_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_C_Invoice_ID > 0)
		{
			MRequisition req = new MRequisition(getCtx(),p_C_Invoice_ID, get_TrxName());
			if(req.getDocStatus().compareToIgnoreCase("DR")==0)
				DB.executeUpdate("UPDATE M_REQUISITION SET DOCSTATUS='WV' WHERE M_REQUISITION_ID = "+req.get_ID(), get_TrxName());
			else if(req.getDocStatus().compareToIgnoreCase("WV")==0)
				DB.executeUpdate("UPDATE M_REQUISITION SET DOCSTATUS='WC' WHERE M_REQUISITION_ID = "+req.get_ID(), get_TrxName());
		}
	   return "Procesado ";
	}	//	doIt
}
