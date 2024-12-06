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
 * Contributor(s): Chris Farley - northernbrewer                              *
 *****************************************************************************/
package org.petroamerica.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MOrder;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 */
public class CompleteOrderInvalid extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private Timestamp p_DateTrx;
	private Timestamp p_DateTrxTo;
	private int p_Org_ID;
	private String p_Hora;

	protected void prepare()
	{	
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("DateTrx"))
			{
				p_DateTrx = (Timestamp)para[i].getParameter();
				p_DateTrxTo = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("AD_Org_ID"))
				p_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("HourEnd"))
				p_Hora = para[i].getParameter().toString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		String sql = "SELECT C_Order_ID FROM C_Order WHERE IsActive = 'Y' " +
				" AND IssoTrx = 'Y' AND DocStatus IN ('IN') AND IsInValidation <> 'Y' " +
				" AND Created BETWEEN ? AND ? AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
		if(p_Org_ID > 0)
			sql = sql +" AND AD_Org_ID = "+p_Org_ID;		
		int cant = 0;
		//se agrega hora a fecha fin
		p_DateTrxTo.setHours(Integer.parseInt(p_Hora.substring(0, 2)));
		p_DateTrxTo.setMinutes(Integer.parseInt(p_Hora.substring(3, 5)));
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;	
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setTimestamp(1, p_DateTrx);
			pstmt.setTimestamp(2, p_DateTrxTo);
			rs = pstmt.executeQuery ();			
			while (rs.next ())
			{
				MOrder order = new MOrder(getCtx(), rs.getInt("C_Order_ID"), get_TrxName());
				order.set_CustomColumn("Override", true);
				if(order.getDescription() != null)
					order.setDescription(order.getDescription()+". Orden completada automaticamente.");
				else
					order.setDescription("Orden completada automaticamente.");
				order.setDocStatus("DR");
				order.processIt("CO");
				if(order.save(get_TrxName()))							
					cant++;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		finally
		{
			pstmt.close();
			rs.close();	
			pstmt = null;
			rs = null;	
		}		
		return "Se Han completado "+cant+" ordenes";
	}	//	doIt
}	//	Replenish
