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

//import java.sql.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.*;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class MPProcessOT extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private String P_DocAction;
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
			else if (name.equals("DocAction"))
				P_DocAction = (String)para[i].getParameter();
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
		
		X_MP_OT OT=new X_MP_OT(Env.getCtx(), Record_ID,get_TrxName());
		//ininoles que solo valide tareas no iniciadas
		String sql="Select count(1) from MP_OT_TASK where STATUS ='NS' and MP_OT_ID="+Record_ID;
		if(DB.getSQLValue(get_TrxName(), sql)>0 && OT.getDocStatus().equals("DR") && P_DocAction.equals("CO"))
			return "Tasks Not Completed";
		//ininoles validacion de km cabecera OT
		if(P_DocAction.equals("CO"))
		{
			BigDecimal actualKm = (BigDecimal)OT.get_Value("tsm_km");
			if(actualKm == null)
				actualKm = Env.ZERO;
			if(actualKm != null && actualKm.compareTo(Env.ZERO) >= 0 && OT.getA_Asset_ID() > 0)
			{
				BigDecimal amtLog = DB.getSQLValueBD(get_TrxName(), "SELECT MAX(ml.amt) " +
						" FROM A_Asset aa " +
						" INNER JOIN MP_AssetMeter am ON (aa.A_Asset_ID = am.A_Asset_ID) " +
						" INNER JOIN MP_AssetMeter_Log ml ON (am.MP_AssetMeter_ID = ml.MP_AssetMeter_ID)" + 
						" INNER JOIN MP_Meter me ON (me.MP_Meter_ID = am.MP_Meter_ID) " +
						" WHERE upper(me.name) like 'KM' AND aa.A_Asset_ID = "+OT.getA_Asset_ID()+
						" AND aa.IsActive = 'Y' AND ml.IsActive = 'Y'");
				if(amtLog != null && amtLog.compareTo(Env.ZERO) > 0)
				{
					if(actualKm.compareTo(amtLog) <= 0)
						throw new AdempiereException("No se puede completar OT. Ultimo odometro registrado "+amtLog);
				}
			}
		}
			
		String mysql="select distinct MP_MAINTAINDetail_ID from MP_OT_TASK where MP_OT_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1, Record_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				X_MP_MaintainDetail mp= new X_MP_MaintainDetail(Env.getCtx(), rs.getInt(1),get_TrxName());
				if(!OT.getDocStatus().equals("VO") && P_DocAction.equals("VO"))
				{
					if(mp.isProgrammingType().equals("C"))
						mp.setDateNextRun(new Timestamp(mp.getDateNextRun().getTime() -(mp.getInterval().longValue()*86400000) ));
					else
					{
						mp.setnextmp(mp.getnextmp().subtract(mp.getInterval()));
						mp.setlastmp(mp.getnextmp().subtract(mp.getInterval()));
					}
				}
				if((OT.getDocStatus().equals("DR") || OT.getDocStatus().equals("PR")) && P_DocAction.equals("CO"))
				{
					mp.setDateLastOT(OT.getDateTrx() );
					mp.set_CustomColumn("MP_OT_ID", OT.getMP_OT_ID());
				}
				
				mp.save();
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}

		if(OT.getDocStatus().equals("DR") && P_DocAction.equals("PR"))			
		{
			OT.setDocStatus("PR");
			OT.setDocAction("CO");
			OT.save();
			return "OT Processed";
		}
		if((OT.getDocStatus().equals("DR") || OT.getDocStatus().equals("PR")) && P_DocAction.equals("CO"))
		{
			DB.executeUpdate("Update MP_OT_RESOURCE set Processed='Y' where MP_OT_TASK_ID IN (select MP_OT_TASK_ID from MP_OT_TASK where MP_OT_ID="+OT.getMP_OT_ID()+")", get_TrxName());
			DB.executeUpdate("Update MP_OT_TASK set Processed='Y' where MP_OT_ID="+OT.getMP_OT_ID(), get_TrxName());
			OT.setDocStatus("CO");
			OT.setDocAction("--");
			OT.setProcessed(true);
			OT.save();
			
			return "OT Completed";
		}
		else if(!OT.getDocStatus().equals("VO") && P_DocAction.equals("VO")){
			DB.executeUpdate("Update MP_OT_RESOURCE set Processed='Y' where MP_OT_TASK_ID IN (select MP_OT_TASK_ID from MP_OT_TASK where MP_OT_ID="+OT.getMP_OT_ID()+")", get_TrxName());
			DB.executeUpdate("Update MP_OT_TASK set Processed='Y' where MP_OT_ID="+OT.getMP_OT_ID(), get_TrxName());
			OT.setDocStatus("VO");
			OT.setDocAction("--");
			OT.setProcessed(true);
			OT.save();
			return "OT Voided";
		}
		return "";
		
	}	//	doIt


	
}	//	CopyOrder
