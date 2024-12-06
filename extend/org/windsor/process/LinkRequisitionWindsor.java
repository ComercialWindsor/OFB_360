/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
package org.windsor.process;

import java.util.logging.Level;

import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *
 *	@author Italo Niñoles
 *	@version $Id: LinkRequisitionWindsor.java 
 */
public class LinkRequisitionWindsor extends SvrProcess
{
	/**	The Order				*/
	private int		p_M_Requisition_ID = 0;
	private int		p_M_RequisitionRef_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_M_Requisition_ID = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_RequisitionRef_ID"))
				p_M_RequisitionRef_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MRequisition req = new MRequisition(getCtx(), p_M_Requisition_ID, get_TrxName());
		MRequisition reqFinal = new MRequisition(getCtx(), p_M_RequisitionRef_ID, get_TrxName());
		
		//se analizan lineas de nueva solicitud
		MRequisitionLine[] lines = req.getLines();
		for (int i = 0; i < lines.length; i++)
		{
			MRequisitionLine line = lines[i];
			if(line.getM_Product_ID() > 0)
			{
				//se busca si existe linea en reqfinal con el mismo producto
				int ID_LineReqFinal = DB.getSQLValue(get_TrxName(), "SELECT M_RequisitionLine_ID FROM M_RequisitionLine " +
						" WHERE M_Product_ID="+line.getM_Product_ID()+" AND M_Requisition_ID="+reqFinal.get_ID());
				if(ID_LineReqFinal  > 0) //si existe linea se actualiza sumando la cantidad
				{
					DB.executeUpdate("UPDATE M_RequisitionLine SET qtyReserved = qtyReserved+("+line.getQty()+"),"+
							" qty = qty+("+line.getQty()+") "+
							" WHERE M_RequisitionLine_ID="+ID_LineReqFinal, get_TrxName());
					
					//se dejan cantidades en 0
					DB.executeUpdate("UPDATE M_RequisitionLine SET qtyReserved=0, qty=0 "+
							" WHERE M_RequisitionLine_ID="+line.get_ID(), get_TrxName());
				}	
				else //si no existe linea se cambia el M_Requisition_Id 
				{
					DB.executeUpdate("UPDATE M_RequisitionLine SET M_Requisition_ID="+reqFinal.get_ID()+
							" WHERE M_RequisitionLine_ID="+line.get_ID(), get_TrxName());
				}				
			}
		}		
		return "actualizado";
	}	//	doIt	
}	//	CopyFromOrder
