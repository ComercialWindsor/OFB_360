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
package org.tsm.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.X_MP_JobStandar;
import org.compiere.model.X_MP_JobStandar_Resource;
import org.compiere.model.X_MP_JobStandar_Task;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	CopyFromJobStandar
 *	
 */
public class CopyFromJobStandar extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_JobStandar_ID;
	private int p_JobStandarFrom_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("MP_JobStandar_ID"))
				p_JobStandarFrom_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}		
		p_JobStandar_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		X_MP_JobStandar jobS = new X_MP_JobStandar(getCtx(), p_JobStandar_ID, get_TrxName());
		int cantTask = 0;
		int cantRes = 0;
		String sqlDet = "SELECT Description, MP_JobStandar_Task_ID,C_UOM_ID,Duration,Comments" +
				" FROM MP_JobStandar_Task WHERE MP_JobStandar_ID = "+ p_JobStandarFrom_ID;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sqlDet, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();			
			
			while (rs.next ())
			{
				//copiamos la tarea
				X_MP_JobStandar_Task jobStTask = new X_MP_JobStandar_Task(getCtx(), 0, get_TrxName());
				jobStTask.setAD_Org_ID(jobS.getAD_Org_ID());
				jobStTask.setMP_JobStandar_ID(jobS.get_ID());
				jobStTask.setDescription(rs.getString("Description"));
				jobStTask.setC_UOM_ID(rs.getInt("C_UOM_ID"));
				jobStTask.setDuration(rs.getBigDecimal("Duration"));
				jobStTask.set_CustomColumn("Comments",rs.getString("Comments"));
				jobStTask.save();
				cantTask++;
				//generamos los recursos para la tarea
				String sqlResource = "SELECT CostAmt, M_BOM_ID, ResourceQty, ResourceType," +
						" S_Resource_ID, M_Product_ID " +
						" FROM MP_JobStandar_Resource WHERE MP_JobStandar_Task_ID = "+ rs.getInt("MP_JobStandar_Task_ID");
				PreparedStatement pstmtResource = null;
				try
				{
					pstmtResource = DB.prepareStatement (sqlResource, get_TrxName());
					ResultSet rsResource = pstmtResource.executeQuery ();			
					while (rsResource.next ())
					{	
						//copiamos los recursos
						X_MP_JobStandar_Resource jobStResource = new X_MP_JobStandar_Resource(getCtx(), 0, get_TrxName());
						jobStResource.setAD_Org_ID(jobStTask.getAD_Org_ID());
						jobStResource.setMP_JobStandar_Task_ID(jobStTask.get_ID());
						jobStResource.setResourceType(rsResource.getString("ResourceType"));
						jobStResource.setM_BOM_ID(rsResource.getInt("M_BOM_ID"));
						jobStResource.setM_Product_ID(rsResource.getInt("M_Product_ID"));
						jobStResource.setS_Resource_ID(rsResource.getInt("S_Resource_ID"));
						jobStResource.setResourceQty(rsResource.getBigDecimal("ResourceQty"));
						jobStResource.setCostAmt(rsResource.getBigDecimal("CostAmt"));
						jobStResource.save();
						cantRes++;
					}	
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}	
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}		
		return "Se han agregado "+cantTask+" tareas y "+cantRes+" recursos";
	}	//	doIt
}	//	Replenish
