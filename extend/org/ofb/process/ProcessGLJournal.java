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

public class ProcessGLJournal extends SvrProcess 
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
		MJournal mj = new MJournal(getCtx(), Record_ID, get_TrxName());
		
		if (P_DocAction.equals("CO"))
		{			
			if (mj.getDocStatus().equals(X_GL_Journal.DOCSTATUS_Drafted)) 
					{						
						mj.setDocStatus(X_GL_Journal.DOCSTATUS_Completed);
						mj.setDocAction(X_GL_Journal.DOCACTION_Close);
						mj.setProcessed(true);
						mj.save();
																								
						m_sql = "UPDATE GL_JournalLine SET processed = 'Y' WHERE GL_Journal_ID=?";
						DB.executeUpdate(m_sql, Record_ID, get_TrxName());
												
						return "Diario Completado";
					}			
		}
		
		if (P_DocAction.equals("CL"))
		{
			if (mj.getDocStatus().equals(X_GL_Journal.DOCSTATUS_Completed)) 
					{
						mj.setDocStatus(X_GL_Journal.DOCSTATUS_Closed);
						mj.setDocAction("--");
						mj.setProcessed(true);
						mj.save();						
						return "Diario Cerrado";						
					}			
		}
		if (P_DocAction.equals("VO"))
		{
			String nDesc = mj.getDocumentNo() + "-anulado-" + Record_ID;
			if (mj.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_Completed)) 
					{
						mj.setDocStatus(X_GL_Journal.DOCSTATUS_Voided);
						mj.setDocAction("--");
						mj.setProcessed(true);
						mj.setDescription(nDesc);
						mj.save();				
						return "Diario Anulado";
					}			
		}
		return "Sin Accion realizada";
	}	
}
