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
package org.ofb.process;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;

import java.math.BigDecimal;
import org.compiere.util.*;
import java.util.Calendar;
import org.compiere.process.*;
 
/**
 *	Reporte Contable Generico GORE
 *	
 *  @author ininoles
 *  @version $Id: TSMReporteConductor.java,v 1.2 2013/10/23 00:51:02 ininoles$
 */
public class TSMReporteConductor extends SvrProcess
{
	/**	The Order				*/
	
	private Timestamp 	p_MovementDateFrom = null;
	private Timestamp 	p_MovementDateTo = null;
	private int 		p_C_ProjectOFB_ID;
	private int			p_C_Period_ID = 0;
	private int 		p_PInstance_ID;
	
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
			else if (name.equals("C_Period_ID"))
			{
				p_C_Period_ID = para[i].getParameterAsInt();
			}	
			else if (name.equals("C_ProjectOFB_ID"))
			{
				p_C_ProjectOFB_ID = para[i].getParameterAsInt();				
			}			
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		
		MPeriod period = new MPeriod(getCtx(), p_C_Period_ID, get_TrxName());
		p_MovementDateFrom = period.getStartDate();
		p_MovementDateTo = period.getEndDate();
						
		/*********/
		DB.executeUpdate("DELETE FROM T_ReporteConductor", get_TrxName());
		/*********/
		
		StringBuilder sqlC = new StringBuilder();
		sqlC.append("SELECT max(rv.ad_client_id) AS ad_client_id, max(rv.ad_org_id) AS ad_org_id, max(rv.isactive) AS isactive, max(rv.created) AS created, "+ 
				"max(rv.createdby) AS createdby, max(rv.updated) AS updated, max(rv.updatedby) AS updatedby, "+ 
				"ROUND((EXTRACT(epoch FROM (sum(rv.sum3::interval)))/3600)::numeric,2) AS sum, "+ 
				"ROUND((EXTRACT(epoch FROM (sum(rv.sum2::interval)))/3600)::numeric,2) AS sum2, "+
				"rv.c_bpartner_id, " +				
				"ROUND((EXTRACT(epoch FROM (sum(rv.sum::interval)))/3600)::numeric,2) AS sumtotal, "+
				
				"round(round((date_part('epoch'::text, sum(rv.sum3::interval)) / 3600::double precision)::numeric, 2)::double precision * 100::double precision / "+
				"CASE "+
				"WHEN round((date_part('epoch'::text, sum(rv.sum::interval)) / 3600::double precision)::numeric, 2)::double precision = 0::double precision "+ 
				"THEN 1::double precision "+
				"ELSE round((date_part('epoch'::text, sum(rv.sum::interval)) / 3600::double precision)::numeric, 2)::double precision "+
				"END)::numeric AS driveporcent, "+
				
				"(( SELECT count(*) AS count FROM m_movement "+
				"WHERE m_movement.c_bpartner_id = rv.c_bpartner_id)) * 12 AS qty, rv.c_projectofb_id, sum(rv.cost)as cost, "+   
				"(SELECT COUNT(DISTINCT(mml.POReference)) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+ 
				"WHERE MovementDate between ? and ? AND mml.POReference is not null AND mml.POReference <> '0' "+ 
				"AND mm.C_ProjectOFB_ID = rv.C_ProjectOFB_ID AND mm.C_BPartner_ID = rv.C_BPartner_ID) as shipmentcount, "+
				"(SELECT COUNT(DISTINCT(mml.POReference)) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
				"WHERE MovementDate between ? and ? AND mml.TP_LineNo is not null AND mml.TP_LineNo <> 0 "+
				"AND mml.TP_LineNo <> 99 AND mm.C_ProjectOFB_ID = rv.C_ProjectOFB_ID AND mm.C_BPartner_ID = rv.C_BPartner_ID) "+	 
				"as travelcount, (SELECT COUNT(1) FROM M_Movement mm WHERE MovementDate between ? and ? "+ 
				"AND mm.C_ProjectOFB_ID = rv.C_ProjectOFB_ID AND mm.C_BPartner_ID = rv.C_BPartner_ID) as cant, "+
				"((SELECT COUNT(1) FROM M_Movement mm WHERE MovementDate between ? and ? "+ 
				"AND mm.C_ProjectOFB_ID = rv.C_ProjectOFB_ID AND mm.C_BPartner_ID = rv.C_BPartner_ID) *  100)/ "+
				"CASE "+
				"WHEN (SELECT Workshift FROM C_ProjectOFB WHERE C_ProjectOFB_ID = rv.C_ProjectOFB_ID) like '30' THEN 26 "+
				"WHEN (SELECT Workshift FROM C_ProjectOFB WHERE C_ProjectOFB_ID = rv.C_ProjectOFB_ID) like '60' THEN 60 "+
				"WHEN (SELECT Workshift FROM C_ProjectOFB WHERE C_ProjectOFB_ID = rv.C_ProjectOFB_ID) like '90' THEN 90 "+				
				"END as hrWorkshift, "+
				
				"round(((EXTRACT(epoch from sum(rv.sum))/648000)*100)::numeric,2) as utilization "+
				
				"FROM rvofb_detallehojarutaConFecha rv where movementdate between ? and ? ");
		
		if (p_C_ProjectOFB_ID > 0)
		{
			sqlC.append(" AND rv.C_ProjectOFB_ID = ? ");
		}
		
		sqlC.append("GROUP BY rv.c_bpartner_id, rv.c_projectofb_id");
		
			PreparedStatement pstmt = null;
			try
			{				
				//registros con filtro de fecha
				pstmt = DB.prepareStatement(sqlC.toString(), get_TrxName());
				pstmt.setTimestamp(1, p_MovementDateFrom);
				pstmt.setTimestamp(2, p_MovementDateTo);
				pstmt.setTimestamp(3, p_MovementDateFrom);
				pstmt.setTimestamp(4, p_MovementDateTo);
				pstmt.setTimestamp(5, p_MovementDateFrom);
				pstmt.setTimestamp(6, p_MovementDateTo);
				pstmt.setTimestamp(7, p_MovementDateFrom);
				pstmt.setTimestamp(8, p_MovementDateTo);
				pstmt.setTimestamp(9, p_MovementDateFrom);
				pstmt.setTimestamp(10, p_MovementDateTo);
				
				if (p_C_ProjectOFB_ID > 0)
				{
					pstmt.setInt(11,p_C_ProjectOFB_ID);
				}			
				
				ResultSet rs = pstmt.executeQuery();
												
				while (rs.next())
				{	
					String Insert=new String("INSERT INTO T_ReporteConductor(ad_pinstance_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,"+
					"sum,sum2,c_bpartner_id,sumtotal,driveporcent,qty,c_projectofb_id,cost,shipmentcount,travelcount,cant,hrWorkshift,utilization) "+
					"VALUES ("+p_PInstance_ID+","+rs.getInt("AD_Client_ID")+","+rs.getInt("AD_Org_ID")+",'"+rs.getString("IsActive")+"','"+rs.getTimestamp("Created")+"',"+
					rs.getInt("CreatedBy")+",'"+rs.getTimestamp("Updated")+"',"+rs.getInt("UpdatedBy")+","+
					rs.getDouble("sum")+","+ rs.getDouble("sum2")+","+rs.getInt("c_bpartner_id")+","
					+rs.getDouble("sumtotal")+","+rs.getDouble("driveporcent")+","+ rs.getDouble("qty")+","+
					rs.getInt("c_projectofb_id")+","+rs.getBigDecimal("cost")+","+rs.getDouble("shipmentcount")+","+
					rs.getDouble("travelcount")+","+rs.getInt("cant")+","+rs.getDouble("hrWorkshift")+","+rs.getDouble("utilization")+")");
						
					DB.executeUpdate(Insert, get_TrxName());
				}		
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			
			DB.executeUpdate("UPDATE T_ReporteConductor SET AD_PInstance_ID="+p_PInstance_ID, get_TrxName());
		
		return "";
	}	//	doIt
		
}	//	Reporte Contable Generico GORE 
