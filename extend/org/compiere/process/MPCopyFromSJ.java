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
package org.compiere.process;

import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id:  MPCopyFromSJ.java,v 1.2 2008/06/12 00:51:01  $
 *  copy tasks & resources to MP fron standardjob
 */
public class MPCopyFromSJ extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int jobStandard_ID;
	/*OT ID*/
	private int 			Record_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("MP_JobStandar_ID"))
				jobStandard_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		String sql="select * from MP_JobStandar_Task where MP_JobStandar_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, jobStandard_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				X_MP_Maintain_Task task=new X_MP_Maintain_Task(getCtx(), 0, get_TrxName());
				task.setMP_Maintain_ID(Record_ID);
				task.setDescription(rs.getString("Description"));
				task.setC_UOM_ID(rs.getInt("C_UOM_ID"));
				task.setDuration(rs.getBigDecimal("Duration"));
				task.saveEx();
				
				String sql2="select * from MP_JobStandar_Resource where MP_JobStandar_Task_ID=?";
				PreparedStatement pstmt2 = null;
				pstmt2 = DB.prepareStatement(sql2, get_TrxName());
				pstmt2.setInt(1, rs.getInt("MP_JobStandar_Task_ID"));
				ResultSet rs2 = pstmt2.executeQuery();
				while (rs2.next())
				{
					X_MP_Maintain_Resource re=new X_MP_Maintain_Resource(getCtx(), 0, get_TrxName());
					re.setMP_Maintain_Task_ID(task.getMP_Maintain_Task_ID());
					re.setCostAmt( rs2.getBigDecimal("CostAmt"));
					re.setM_BOM_ID( rs2.getInt("M_BOM_ID"));
					re.setM_Product_ID( rs2.getInt("M_Product_ID"));
					re.setResourceQty( rs2.getBigDecimal("ResourceQty"));
					re.setResourceType( rs2.getString("ResourceType"));
					re.setS_Resource_ID( rs2.getInt("S_Resource_ID"));
					re.saveEx();
				}
				rs2.close();
				pstmt2.close();
				pstmt2 = null;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
			
			X_MP_Maintain ma= new X_MP_Maintain(getCtx(), Record_ID, get_TrxName());
			ma.setMP_JobStandar_ID(jobStandard_ID);
			ma.saveEx();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		return "Copied";
	}	//	doIt


	
}	//	CopyOrder
