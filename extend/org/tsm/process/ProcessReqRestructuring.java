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

import org.compiere.model.X_RH_RequisitionRestructuring;
import org.compiere.process.SvrProcess;
/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ProcessReqRestructuring.java $
 */
public class ProcessReqRestructuring extends SvrProcess
{
	private int				ID_ReqRes = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{	 
		 ID_ReqRes=getRecord_ID();		 
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		String msg = "Procesado";
		if (ID_ReqRes > 0)
		{
			X_RH_RequisitionRestructuring req = new X_RH_RequisitionRestructuring(getCtx(), ID_ReqRes, get_TrxName());
			if(req.getDocStatus().compareTo("DR") == 0)
			{
				req.setDocStatus("CO");
				req.setProcessed(true);
				msg = "Completado";
			}
			else if(req.getDocStatus().compareTo("CO") == 0)
			{
				req.setDocStatus("VO");
				req.setProcessed(true);
				msg = "Anulado";
			}
			req.saveEx();
		}
		return "Documento "+msg;
	}	//	doIt
}
