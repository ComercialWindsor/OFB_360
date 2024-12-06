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
 *	Proceso de actualizacion de costos flota Bravo Energy
 *	
 *  @author ininoles
 *  @version $Id: PostCosteoBravoEnergyTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class PostCosteoBravoEnergyTSM extends SvrProcess
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
		
		//actualizaciones previas
		//costo a 0
		Timestamp params[] = {p_MovementDateFrom, p_MovementDateTo};		
		
		String sqlUpdatePreview1 = "UPDATE M_MovementLine SET Cost = 0 WHERE M_MovementLine_ID IN ( "+
				"SELECT M_MovementLine_ID FROM M_MovementLine MML INNER JOIN M_Movement MM ON (MM.M_Movement_ID = MML.M_Movement_ID) "+
				"WHERE MM.C_ProjectOFB_ID= "+p_ProjectOFB_ID+" AND MM.MovementDate between ? AND ? "+
				"AND (MMl.TP_LineNo > 0 OR MML.TP_LineNo is null))";
		
		DB.executeUpdateEx(sqlUpdatePreview1, params, get_TrxName());
		
		//traer valor desde destino
		String sqlUpdatePreview2 = "UPDATE M_MovementLine MML2 SET KMValue = COALESCE ((SELECT KMValue FROM TP_Destination TPD WHERE TPD.TP_Destination_ID = MML2.TP_Destination_ID),0) "+
				"WHERE M_MovementLine_ID IN ( SELECT M_MovementLine_ID FROM M_MovementLine MML "+
				"INNER JOIN M_Movement MM ON (MM.M_Movement_ID = MML.M_Movement_ID) WHERE MM.C_ProjectOFB_ID= "+p_ProjectOFB_ID+
				" AND MM.MovementDate between ? AND ? AND MMl.Cost = 0) ";
		
		DB.executeUpdateEx(sqlUpdatePreview2, params, get_TrxName());
	
		//fin actualizacion previa
		
		
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
			
				/*if (mm.get_ID()==1012742)
				{
					int a = 1012742;
					a = a++;
				}*/
				
				String sqlCiclo = "SELECT TP_LineNo FROM M_MovementLine WHERE M_Movement_ID=? "+
						"AND Cost = 0 AND TP_LineNo > 0 GROUP BY TP_LineNo ";
				
				PreparedStatement pstmt2 = null;
				ResultSet rs2 = null;
				
				pstmt2 = null;
				pstmt2 = DB.prepareStatement(sqlCiclo, get_TrxName());
				pstmt2.setInt(1, mm.get_ID());
				rs2 = pstmt2.executeQuery();
			
				//Ciclo de lineas por N° de viaje
					
				while (rs2.next())					
				{
					//valor mayor
					String sqlMaxValue = "SELECT MAX (KMValue) FROM M_MovementLine WHERE M_Movement_ID= ? "+
							"AND TP_LineNo = ?";
					
					BigDecimal maxValue = DB.getSQLValueBD(get_TrxName(), sqlMaxValue,mm.get_ID(),rs2.getInt("TP_LineNo"));
					
					//actualizacion de lineas
					String sqlUValue = "UPDATE M_MovementLine SET Cost = ( "+maxValue+" * (TP_InicialKM - TP_FinalKM)) " +
							"WHERE M_Movement_ID = "+mm.get_ID()+" AND TP_LineNo = "+rs2.getInt("TP_LineNo");
					
					DB.executeUpdate(sqlUValue, get_TrxName());					
				}									
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
