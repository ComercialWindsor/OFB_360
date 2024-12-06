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

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import java.util.logging.*;

/**
 *	
 *	
 *  @author Italo Niñoles i.ninoles
 *  @version $Id: ProcessPaymentRequest.java,v 1.2 2011/06/12 00:51:01  $
 */

public class ProcessProjectOFB extends SvrProcess 
{
	
	private int 			Record_ID;
	private String P_DocAction;
	private String 		m_sql = null; 
	
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
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	
	protected String doIt() throws Exception
	{
		X_C_ProjectOFB pofb = new X_C_ProjectOFB(Env.getCtx(), Record_ID, get_TrxName());
		
		
		if (P_DocAction.equals("CO"))
		{
			if (pofb.get_ValueAsString("DocStatus").equals(X_C_PaymentRequest.DOCSTATUS_Drafted)) 
					{
						pofb.set_CustomColumn("DocStatus", X_C_PaymentRequest.DOCSTATUS_Completed);
						pofb.set_CustomColumn("Processed", true);						
						pofb.save();			
						
						m_sql = "Update c_subprojectofb set processed = 'Y' where c_projectofb_id = ?";
						
						DB.executeUpdate(m_sql, Record_ID, get_TrxName());
												
						return "Pago Completado";
					}			
		}
		
		if (P_DocAction.equals("CL"))
		{
			if (pofb.get_ValueAsString("DocStatus").equals(X_C_PaymentRequest.DOCSTATUS_Completed)) 
					{
						pofb.set_CustomColumn("DocStatus", X_C_PaymentRequest.DOCSTATUS_Closed);						
						pofb.save();
						return "Pago Cerrado";						
					}			
		}
		if (P_DocAction.equals("VO"))
		{
			String nvalue = pofb.getValue() + "-anulado-" + Record_ID;
			if (pofb.get_ValueAsString("DocStatus").equals(X_C_PaymentRequest.DOCSTATUS_Completed)) 
					{
						pofb.set_CustomColumn("DocStatus", X_C_PaymentRequest.DOCSTATUS_Voided);
						pofb.setValue(nvalue);
						pofb.save();		
						return "Pago Anulado";
					}			
		}
		return "Sin Accion realizada";
	}	
}
