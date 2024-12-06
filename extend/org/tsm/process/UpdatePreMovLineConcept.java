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

import org.adempiere.exceptions.DBException;
import org.compiere.model.X_HR_Prebitacora;
import org.compiere.model.X_Pre_M_MovementLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class UpdatePreMovLineConcept extends SvrProcess
{
	private int				p_PRE_Mov_ID = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{	 
		 p_PRE_Mov_ID=getRecord_ID();		 
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_PRE_Mov_ID > 0)
		{
			String sql= "SELECT * FROM Pre_M_MovementLine " +
						" WHERE IsActive = 'Y' AND Pre_M_Movement_ID = ?";				
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, p_PRE_Mov_ID);
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					X_Pre_M_MovementLine pmLine = new X_Pre_M_MovementLine(getCtx(), rs.getInt("Pre_M_MovementLine_ID"), get_TrxName());
					
					if(pmLine.getC_BPartner_ID() > 0)
					{
						int ID_preBitacora = DB.getSQLValue(get_TrxName(), "SELECT MAX(HR_Prebitacora_ID) FROM HR_Prebitacora  " +
								" WHERE C_BPartner_ID = "+pmLine.getC_BPartner_ID()+" AND DateTrx = ?",pmLine.getPre_M_Movement().getMovementDate());
						if(ID_preBitacora > 0)
						{
							X_HR_Prebitacora preBitacora = new X_HR_Prebitacora(getCtx(), ID_preBitacora,get_TrxName());
							pmLine.set_CustomColumn("HR_PrebitacoraRef_ID", ID_preBitacora);
							pmLine.set_CustomColumn("HR_Concept_TSMBP_ID", preBitacora.getHR_Concept_TSM_ID());
							pmLine.set_CustomColumn("WorkshiftBP",preBitacora.getHR_Concept_TSM().getAcronym().trim());
						}
						else
						{
							pmLine.set_CustomColumn("HR_PrebitacoraRef_ID", null);					
							pmLine.set_CustomColumn("HR_Concept_TSMBP_ID",null);
							pmLine.set_CustomColumn("WorkshiftBP",null);
						}
					}
					//incidencia tracto
					if(pmLine.getA_Asset_ID() > 0)
					{
						int ID_preBitacora = DB.getSQLValue(get_TrxName(), "SELECT MAX(HR_Prebitacora_ID) FROM HR_Prebitacora  " +
								" WHERE A_Asset_ID = "+pmLine.getA_Asset_ID()+" AND DateTrx = ?",pmLine.getPre_M_Movement().getMovementDate());
						if(ID_preBitacora > 0)
						{
							X_HR_Prebitacora preBitacora = new X_HR_Prebitacora(getCtx(), ID_preBitacora, get_TrxName());
							pmLine.set_CustomColumn("HR_Prebitacora_ID", ID_preBitacora);
							pmLine.set_CustomColumn("HR_Concept_TSM_ID", preBitacora.getHR_Concept_TSM_ID());
							pmLine.set_CustomColumn("Workshift",preBitacora.getHR_Concept_TSM().getAcronym().trim());
						}
						else
						{
							pmLine.set_CustomColumn("HR_Prebitacora_ID", null);
							pmLine.set_CustomColumn("HR_Concept_TSM_ID",null);
							pmLine.set_CustomColumn("Workshift",null);
						}
					}
					pmLine.save(get_TrxName());
				}
			}
			catch (SQLException e)
			{
				throw new DBException(e, sql);
			}
		}
		return "Procesado ";
	}	//	doIt
}
