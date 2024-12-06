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

import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ProcessInvoiceRequest.java $
 */
public class ProcessInvoiceRequest extends SvrProcess
{
	private int				p_C_Invoice_ID = 0;
	private String				P_DocAction = "CO"; 
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
				else if (name.equals("DocAction"))
					P_DocAction = (String)para[i].getParameter();
				else
					log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
			}
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
			if(P_DocAction.compareTo("CO") == 0)
			{
				if(req.getDocStatus().compareToIgnoreCase("DR")==0)
					req.setDocStatus("AJ");
				else if(req.getDocStatus().compareToIgnoreCase("AJ")==0)
					req.setDocStatus("WV");
				else if(req.getDocStatus().compareToIgnoreCase("WV")==0)
					req.setDocStatus("WC");
			}
			else if (P_DocAction.compareTo("VO") == 0)
			{	
				//validación antes de anular
				MRequisitionLine[] lines = req.getLines();
				for (int i = 0; i < lines.length; i++)
				{
					MRequisitionLine rline = lines[i];
					int cantV = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine" +
							" WHERE M_requisitionLine_ID = "+rline.get_ID()+" AND IsActive = 'Y'");
					if(cantV > 0)
						throw new AdempiereException("ERROR: No se puede anular existe orden de compra asociada");
					else
					{
						rline.setQty(Env.ZERO);
						rline.saveEx();
					}
				}						
				req.setDocStatus("VO");
				req.setProcessed(true);
				DB.executeUpdate("UPDATE M_requisitionLine SET Processed = 'Y' WHERE M_requisition_ID = "+req.get_ID(), get_TrxName());
			}
			req.save();
		}
	   return "Procesado ";
	}	//	doIt
}
