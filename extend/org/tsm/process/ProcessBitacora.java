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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.compiere.model.X_HR_Bitacora;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class ProcessBitacora extends SvrProcess
{
	private int				p_HR_Bitacora_ID = 0; 
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 p_HR_Bitacora_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_HR_Bitacora_ID > 0)
		{
			X_HR_Bitacora bitacora = new X_HR_Bitacora(getCtx(), p_HR_Bitacora_ID, get_TrxName());			
			if(bitacora.getDocStatus().compareToIgnoreCase("DR")==0)
			{	
				//validaciones hoja de ruta
				int cantHR = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM HR_BitacoraLine " +
						" WHERE IsActive = 'Y' AND M_Movement_ID > 0 AND HR_Bitacora_ID = "+bitacora.get_ID());
				int cantPB = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM HR_BitacoraLine " +
						" WHERE IsActive = 'Y' AND HR_Prebitacora_ID > 0 AND HR_Bitacora_ID = "+bitacora.get_ID());
				if(cantHR > 0 && cantPB > 0)
					throw new AdempiereException("Error: No puede haber una hoja de ruta y una incidencia el mismo dia");
				if(cantHR >= 2)
				{
					String sql = "SELECT COUNT(1) as cant FROM HR_BitacoraLine bl " +
							" INNER JOIN M_Movement mm ON (bl.M_Movement_ID = mm.M_Movement_ID) " +
							" WHERE bl.IsActive = 'Y' AND bl.M_Movement_ID > 0 AND HR_Bitacora_ID = ?" +
							" GROUP BY mm.movementdate, TP_WorkingDayType";
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try
					{
						pstmt = DB.prepareStatement(sql, get_TrxName());
						pstmt.setInt(1, bitacora.get_ID());
						rs = pstmt.executeQuery();
						while(rs.next())
						{
							if(rs.getInt("cant") > 1)
								throw new AdempiereException("Error: No puede  mas de 1 hoja de ruta para el mismo dia y turno");
						}
					}
					catch (SQLException e)
					{
						throw new DBException(e, sql);
					}
					finally
					{
						DB.close(rs, pstmt);
						rs = null; pstmt = null;
					}
				}	
				bitacora.setDocStatus("CO");
				bitacora.setDocAction("--");
				bitacora.setProcessed(true);
				DB.executeUpdate("UPDATE HR_BitacoraLine SET Processed = 'Y' " +
						" WHERE HR_Bitacora_ID = "+p_HR_Bitacora_ID, get_TrxName());
			}
			bitacora.save();
		}
	   return "Procesado ";
	}	//	doIt
}
