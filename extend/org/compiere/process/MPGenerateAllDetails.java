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
 *  @version $Id:  MPGenerateDetail.java,v 1.2 2012/12/12 00:51:01  $
 *  
 */
public class MPGenerateAllDetails extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */

	
	
	protected void prepare()
	{
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		String sql1 = "select MP_Maintain_ID from MP_Maintain where isActive='Y' and AD_Client_ID=?";
		PreparedStatement pstmt1 = null;
		int i=0;
		try
		{
			pstmt1 = DB.prepareStatement(sql1, get_TrxName());
			pstmt1.setInt(1, getAD_Client_ID());
			ResultSet rs1 = pstmt1.executeQuery();
			while (rs1.next())
			{
				boolean filter = false;
				X_MP_Maintain main = new X_MP_Maintain(getCtx(), rs1.getInt(1), get_TrxName());
				String sql = "select A_Asset_ID, A_Asset_Group_ID from A_Asset where 1=1";
				
				if(main.getA_Asset_Group_ID()>0)
				{
					sql+=" and A_Asset_Group_ID=?";
					filter=true;
				}
				if(main.getA_Asset_ID()>0)
				{
					sql+=" and A_Asset_ID=?";
				filter=true;
				}
				if(main.get_ValueAsInt("MP_MPGroup_ID")>0)
				{
					sql+=" and MP_MPGroup_ID=?";
				filter=true;
				}
				
				if(!filter)
					continue;
				
				PreparedStatement pstmt = null;
				try
				{
					int x=1;
					pstmt = DB.prepareStatement(sql, get_TrxName());
					if(main.getA_Asset_Group_ID()>0)
						pstmt.setInt(x++, main.getA_Asset_Group_ID());
					if(main.getA_Asset_ID()>0)
						pstmt.setInt(x++, main.getA_Asset_ID());
					if(main.get_ValueAsInt("MP_MPGroup_ID")>0)
						pstmt.setInt(x++, main.get_ValueAsInt("MP_MPGroup_ID"));
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
						X_MP_MaintainDetail detail = new X_MP_MaintainDetail(getCtx(), 0, get_TrxName());
						detail.setMP_JobStandar_ID(main.getMP_JobStandar_ID());
						detail.setMP_Meter_ID(main.getMP_Meter_ID());
						detail.setMP_Maintain_ID(main.getMP_Maintain_ID());
						detail.setDescription(main.getDescription());
						detail.setA_Asset_ID(rs.getInt(1));
						detail.setAD_Org_ID(main.getAD_Org_ID());
						detail.setDateNextRun(main.getDateNextRun());
						detail.setDocStatus(main.getDocStatus());
						detail.setInterval(main.getInterval());
						detail.setIsChild(main.get_ValueAsBoolean("IsChild"));
						detail.setdatelastrunmp(main.getdatelastrunmp());
						detail.setlastmp(main.getlastmp());
						detail.setcurrentmp(main.getcurrentmp());
						detail.setMP_MaintainParent_ID(main.getMP_MaintainParent_ID());
						detail.setMP_Meter_ID(main.getMP_Meter_ID());
						detail.setnextmp(main.getnextmp());
						detail.setProgrammingType(main.getProgrammingType());
						detail.setRange(main.getRange());
						detail.setDateLastOT(main.getDateLastOT());
						detail.setDateLastRun(main.getDateLastRun());
						detail.setlastread(main.getlastread());
						detail.setpromuse(main.getpromuse());
						detail.save();
						i++;
						
					}//while 2
					rs.close();
					pstmt.close();
					pstmt = null;
					
					
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, sql, e);
				}
				
				if(i>0)
				{
					main.set_ValueOfColumn("isGenerated", "Y");
					main.save();
				}
			}//while 1
			rs1.close();
			pstmt1.close();
			pstmt1 = null;
			
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql1, e.getMessage());
		}
		
		return "Creados "+i;
	}	//	doIt


	
}	//	CopyOrder
