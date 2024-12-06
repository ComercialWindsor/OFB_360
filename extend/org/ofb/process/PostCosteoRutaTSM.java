/******************************************************************************
0 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	Proceso Borrado Costeo Flota Petrobras
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class PostCosteoRutaTSM extends SvrProcess
{
	private int	p_ProjectOFB_ID = 0;	
	private Timestamp p_MovementDateFrom = null;
	private Timestamp p_MovementDateTo = null;
	
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
			else if (name.equals("C_ProjectOFB_ID"))
			{
				p_ProjectOFB_ID = para[i].getParameterAsInt();				
			}
			else if (name.equals("MovementDate"))
			{
				p_MovementDateFrom = (Timestamp)para[i].getParameter();
				p_MovementDateTo = (Timestamp)para[i].getParameter_To();
			}
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		
		String sqlPrincipal = "SELECT M_Movement_ID FROM M_Movement WHERE isactive = 'Y'";
		
		if(p_ProjectOFB_ID > 0)
		{
			sqlPrincipal = sqlPrincipal + " AND C_ProjectOFB_ID = " + p_ProjectOFB_ID;
		}
		if (p_MovementDateFrom != null && p_MovementDateTo != null)
		{
			sqlPrincipal = sqlPrincipal + " AND MovementDate between ? AND ?";
		}
		
		sqlPrincipal = sqlPrincipal +  " ORDER BY MovementDate, M_Movement_ID"; 
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = null;
			pstmt = DB.prepareStatement(sqlPrincipal, get_TrxName());
			pstmt.setTimestamp(1, p_MovementDateFrom); 
			pstmt.setTimestamp(2, p_MovementDateTo);						
			
			rs = pstmt.executeQuery();
		
			//ciclo de hojas de ruta
				
			while (rs.next())
			{								
				MMovement mm = new MMovement(getCtx(), rs.getInt("M_Movement_ID"), get_TrxName());
				
				int viaje = -1;
				int CComercial = 0;
				int Destination_ID = 0;
				String Destination_IDS = "";
				/*int min = 0;
				int max = 0;*/
				
				if (mm.get_ID()==1012742)
				{
					int a = 1012742;
					a = a++;
				}
								
				MMovementLine[] lines = mm.getLines(false);
				for (int i = 0; i < lines.length; i++)
				{
					MMovementLine mml = lines[i];
					
					if (viaje == -1)
						viaje = mml.get_ValueAsInt("TP_LineNo");
					
					
					int cost = DB.getSQLValue(get_TrxName(), "SELECT cost FROM TP_Destination WHERE TP_Destination_ID=? ", mml.get_ValueAsInt("TP_Destination_ID"));
					
					if (mml.get_ValueAsInt("TP_LineNo") != 0 && mml.get_ValueAsInt("TP_LineNo") != viaje)
					{						
						CComercial = 0;
						if (Destination_ID > 0)
						{
							Destination_IDS = Destination_IDS+Integer.toString(Destination_ID)+" , ";
						}
						Destination_ID = 0;
						viaje = mml.get_ValueAsInt("TP_LineNo");
					}						
					
					if (cost > CComercial && viaje == mml.get_ValueAsInt("TP_LineNo") && mml.get_ValueAsInt("TP_LineNo") != 0)
					{
						CComercial = cost;
						Destination_ID = mml.get_ID();
					}
				}	
				
				Destination_IDS = Destination_IDS+Integer.toString(Destination_ID);
				
				String sqlUpdate = "UPDATE M_MovementLine SET Cost = 0 "+
						"WHERE M_Movement_ID = "+mm.get_ID()+" AND M_MovementLine_ID NOT IN ( "+Destination_IDS+" )";
		
				DB.executeUpdate(sqlUpdate, get_TrxName());											
		}
		
		rs.close();
		pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}		
		
		return "Hojas de Ruta Actualizadas: Flota "+p_ProjectOFB_ID+" entre fechas "+p_MovementDateFrom+" and "+p_MovementDateTo;
	}	//	doIt
	
}	//	OrderOpen
