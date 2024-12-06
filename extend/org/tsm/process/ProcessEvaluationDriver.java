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

import org.adempiere.exceptions.AdempiereException;

import org.compiere.model.MOrder;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.X_RH_EvaluationHeader;

/**
 *	
 *	
 *  @author mfrojas
 *  @version $Id: ProcessSalesOrder.java $
 */
public class ProcessEvaluationDriver extends SvrProcess
{
	//private String			p_DocStatus = null;
	private int				p_Ev_ID = 0; 
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 p_Ev_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_Ev_ID > 0)
		{
			X_RH_EvaluationHeader eHeader = new X_RH_EvaluationHeader(getCtx(), p_Ev_ID, get_TrxName());

			//seteo de nuevo estado
				String newStatus = "DR";
				String newAction = "DR";
				
				if(eHeader.getDocStatus().compareTo("DR") == 0)
				{
					newStatus = "IP";
					newAction = "IP";
				}
				else if(eHeader.getDocStatus().compareTo("IP") == 0)
				{
					newStatus = "AP";
					newAction = "AP";
				}
				else if(eHeader.getDocStatus().compareTo("AP") == 0)
				{
					newStatus = "CO";
					newAction = "CO";
				}
				//validacion de rol
/**					if(newStatus.compareTo("CO")==0 || newStatus.compareTo("VO")==0)
					{
						if(newStatus.compareTo("CO")==0)
						{
							eHeader.setDocStatus("DR");
							eHeader.processIt(newStatus);							
						}
						else if(newStatus.compareTo("VO")==0)
							eHeader.setDocStatus(newStatus);
					}
					else
					
**/					if(newStatus.compareTo("CO") == 0)
						eHeader.setProcessed(true);
					eHeader.setDocStatus(newStatus);						
					eHeader.save();
				
				//error de permisos
				//else
				//	throw new AdempiereException("Error: Permisos de rol insuficientes");				
			
		}
	   return "Procesado";
	}	//	doIt
}
