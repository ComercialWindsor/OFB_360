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
package org.safeenergy.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOut;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ProcessSalesOrder.java $
 */
public class ProcessShipment extends SvrProcess
{
	//private String			p_DocStatus = null;
	private int				p_Order_ID = 0; 
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 p_Order_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_Order_ID > 0)
		{
			MInOut ship = new MInOut(getCtx(), p_Order_ID, get_TrxName());
			if(ship.isSOTrx())
			{
				//seteo de nuevo estado
				String newStatus = "DR";
				String newAction = "DR";
				if(ship.getDocStatus().compareTo("DR") == 0)
				{
					newStatus = "IP";
					newAction = "PR";
				}
				else if(ship.getDocStatus().compareTo("IP") == 0)
				{
					newStatus = "EC";
					newAction = "EC";
				}
				else if(ship.getDocStatus().compareTo("EC") == 0)
				{
					newStatus = "EV";
					newAction = "EV";
				}
				else if(ship.getDocStatus().compareTo("EV") == 0)
				{
					newStatus = "CO";
					newAction = "CO";
				}
				else if(ship.getDocStatus().compareTo("CO") == 0)
				{
					newStatus = "VO";
					newAction = "VO";
				}
				//validacion de rol
				int cant = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_Document_Action_Access daa" +
						" INNER JOIN AD_Ref_List rl ON (daa.AD_Ref_List_ID = rl.AD_Ref_List_ID) " +
						" WHERE value = '"+newAction+"' AND AD_Role_ID = "+Env.getAD_Role_ID(getCtx())+
						" AND C_DocType_ID = "+ship.getC_DocType_ID());
				//procesar
				if(cant > 0)
				{
					if(newStatus.compareTo("CO")==0 || newStatus.compareTo("VO")==0)
					{
						if(newStatus.compareTo("CO")==0)
						{
							ship.setDocStatus("DR");
							ship.processIt(newStatus);							
						}
						else if(newStatus.compareTo("VO")==0)
							ship.setDocStatus(newStatus);
					}
					else
						ship.setDocStatus(newStatus);						
					ship.save();
				}
				//error de permisos
				else
					throw new AdempiereException("Error: Permisos de rol insuficientes");				
			}
		}
	   return "Procesado";
	}	//	doIt
}
