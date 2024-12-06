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
public class MPGenerateDetail extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */

	private int 			Record_ID;
	
	protected void prepare()
	{
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int i=0;
		boolean filter = false;
		
		X_MP_Maintain main = new X_MP_Maintain(getCtx(), Record_ID, get_TrxName());
		if(main.get_ValueAsBoolean("IsGenerated"))
			return "ya se encuentra generada esta mantención";
		
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
			return "Debe definir un activo o un grupo";
		
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
				int detail_ID = DB.getSQLValue(get_TrxName(), "select MP_MaintainDetail_ID from MP_MaintainDetail where A_Asset_ID=? and MP_Maintain_ID=?", rs.getInt(1),main.getMP_Maintain_ID());
				
				
				X_MP_MaintainDetail detail = new X_MP_MaintainDetail(getCtx(), detail_ID, get_TrxName());
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
				
			}
			rs.close();
			pstmt.close();
			pstmt = null;
			
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		
		main.set_CustomColumn("IsGenerated", true);
		main.save();
		return "Creados "+i;
	}	//	doIt


	
}	//	CopyOrder
