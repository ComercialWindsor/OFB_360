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
 *  @version $Id: TSMReportePatente.java,v 1.2 2013/10/23 00:51:02 ininoles$
 */
public class TSMReportePatente extends SvrProcess
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
		DB.executeUpdate("DELETE FROM T_ReportePatente", get_TrxName());
		/*********/
		
		StringBuilder sqlC = new StringBuilder();
		sqlC.append("select max(rvh.ad_client_id) AS ad_client_id, max(rvh.ad_org_id) AS ad_org_id, max(rvh.isactive) AS isactive, max(rvh.created) AS created, max(rvh.createdby) AS createdby, "+ 
					"max(rvh.updated) AS updated, max(rvh.updatedby) AS updatedby, max(rvh.m_movement_id) AS m_movement_id, rvh.c_projectofb_id, "+
					"rvh.tractoname, max(rvh.tp_asset_id)as tp_asset_id, "+ 
					"(select sum(tp_sumkm) from rvofb_comparative_fact where tp_a_asset = rvh.tractoname and c_projectOFB_id = rvh.c_projectOFB_id and dateinvoiced between ? and ?) as tp_sumkm, "+
					"(select sum(tp_sumamount) from rvofb_comparative_fact where tp_a_asset = rvh.tractoname and c_projectOFB_id = rvh.c_projectOFB_id and dateinvoiced between ? and ?) as tp_sumamount, "+
					"(select sum(tp_sumvol) from rvofb_comparative_fact where tp_a_asset = rvh.tractoname and c_projectOFB_id = rvh.c_projectOFB_id and dateinvoiced between ? and ?) as tp_sumvol, "+
					"sum(rvh.kmtotal) as kmtotal, sum(rvh.cost)as cost, sum(rvh.tp_volm3)as tp_volm3, "+
					"sum(rvh.kmtotal)-(select coalesce(sum(tp_sumkm),0) from rvofb_comparative_fact where tp_a_asset = rvh.tractoname and c_projectOFB_id = rvh.c_projectOFB_id and dateinvoiced between ? and ?) as kmdif, "+
					"sum(rvh.cost)-(select coalesce(sum(tp_sumamount),0) from rvofb_comparative_fact where tp_a_asset = rvh.tractoname and c_projectOFB_id = rvh.c_projectOFB_id and dateinvoiced between ? and ?) as costdif, "+
					"sum(rvh.tp_volm3) - ((select coalesce(sum(tp_sumvol),0) from rvofb_comparative_fact where tp_a_asset = rvh.tractoname and c_projectOFB_id = rvh.c_projectOFB_id and dateinvoiced between ? and ?)) as voldif, "+
					"sum(rvh.kmrecorrido) as kmrecorrido, sum(rvh.kmnocomercial) as kmnocomercial, "+
					//"round(sum(rvh.cost) / case when sum(rvh.kmrecorrido) =0 then 1 else  sum(rvh.kmrecorrido) end,2) as costeo, "+ se cambioa sql a peticion de jose
					"round(sum(rvh.cost) / case when sum(rvh.kmtotal) =0 then 1 else sum(rvh.kmtotal) end,2) as costeo, "+					
					"round(((sum(rvh.kmnocomercial)/(case when sum(rvh.kmrecorrido)=0 then 1 else sum(rvh.kmrecorrido) end))*100),2) as kmnocomercialporcent, "+
					"(SELECT COUNT(DISTINCT(mml.TP_LineNo)) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
					"WHERE MovementDate between ? and ? AND mml.TP_LineNo is not null AND mml.TP_LineNo <> '0' AND mml.TP_LineNo <> '99' "+ 
					"AND mm.C_ProjectOFB_ID = rvh.C_ProjectOFB_ID AND mm.TP_Asset_ID = rvh.TP_Asset_ID) as tsm_travelcount, "+
					"(SELECT COUNT(DISTINCT(TP_Trip)) FROM rvofb_comparative_fact WHERE C_ProjectOFB_ID = rvh.C_ProjectOFB_ID "+
					"AND dateinvoiced between ? and ? AND TP_Trip is not null AND TP_Trip <> '0' AND tp_a_asset = rvh.tractoname) as enex_travelcount, "+
					"(SELECT COUNT(mml.TP_LineNo) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
					"WHERE MovementDate between ? and ? AND mml.TP_LineNo is not null AND mml.TP_LineNo = '99' "+ 
					"AND mm.C_ProjectOFB_ID = rvh.c_projectofb_id AND mm.TP_Asset_ID = rvh.TP_Asset_ID) as nocomercialtravels, "+
					"(SELECT COUNT(DISTINCT (mml.POReference)) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
					"WHERE MovementDate between ? and ? AND mml.POReference is not null AND mml.POReference <> '0' "+
					"AND mm.C_ProjectOFB_ID = rvh.c_projectofb_id AND mm.TP_Asset_ID = rvh.TP_Asset_ID) as shipmentcount, "+
					"(SELECT  COUNT(DISTINCT(mml.POReference)) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
					"WHERE mml.POReference in "+ 
					"(SELECT mml.POReference FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
					"WHERE MovementDate between ? and ? AND mml.POReference is not null AND mml.POReference <> '0' "+
					"AND mm.C_ProjectOFB_ID = rvh.c_projectofb_id AND mm.TP_Asset_ID = rvh.TP_Asset_ID group by mml.POReference having count(mml.POReference)>2) ) as shipmentcountmas, "+ 
					"0 as shipmentdif, "+
					"to_char((SELECT SUM(total) FROM RVOFB_HRDetailEquipo de "+
					"WHERE de.MovementDate between ? and ? "+
					"AND de.C_ProjectOFB_ID = rvh.c_projectofb_id AND de.TP_Asset_ID = rvh.TP_Asset_ID),'HH24:MI'::text) "+
					"as workingdayhr, "+
					
					"round ((((extract(epoch from (SELECT SUM(total) FROM RVOFB_HRDetailEquipo de  WHERE de.MovementDate between ? and ? "+ 
					"AND de.C_ProjectOFB_ID = rvh.c_projectofb_id AND de.TP_Asset_ID = rvh.TP_Asset_ID))/3600)/720)*100)::numeric,3) "+
					"as workingdayhrporcent, "+
					
					
					"to_char(((coalesce((SELECT SUM(total) FROM RVOFB_HRDetailEquipo de WHERE de.MovementDate between ? and ? "+ 
					"AND de.C_ProjectOFB_ID = rvh.c_projectofb_id AND de.TP_Asset_ID = rvh.TP_Asset_ID AND TP_WorkingDayType = '01'),'00:00:00'::interval) "+
					"+ coalesce((SELECT SUM(total) FROM RVOFB_HRDetailEquipo de WHERE de.MovementDate between ? and ? "+ 
					"AND de.C_ProjectOFB_ID = rvh.c_projectofb_id AND de.TP_Asset_ID = rvh.TP_Asset_ID AND TP_WorkingDayType = '03'),'00:00:00'::interval) "+
					"+ coalesce((SELECT SUM(total) FROM RVOFB_HRDetailEquipo de WHERE de.MovementDate between ? and ? "+ 
					"AND de.C_ProjectOFB_ID = rvh.c_projectofb_id AND de.TP_Asset_ID = rvh.TP_Asset_ID AND TP_WorkingDayType = '02'),'00:00:00'::interval))/30),'HH24:MI'::text)  as workingdaycomodin, "+
					
					
					"(SELECT COUNT(1) FROM M_Movement mm WHERE mm.MovementDate between ? and ? "+
					"AND mm.C_ProjectOFB_ID = rvh.c_projectofb_id AND mm.TP_Asset_ID = rvh.TP_Asset_ID AND TP_WorkingDayType = '01') "+
					"as workingdaymorningcount, "+
					
					"(SELECT COUNT(1) FROM M_Movement mm WHERE mm.MovementDate between ? and ? "+
					"AND mm.C_ProjectOFB_ID = rvh.c_projectofb_id AND mm.TP_Asset_ID = rvh.TP_Asset_ID AND TP_WorkingDayType = '03')" + 
					"as wornkindayafternooncount,"+
					
					"(SELECT COUNT(1) FROM M_Movement mm WHERE mm.MovementDate between ? and ? "+
					"AND mm.C_ProjectOFB_ID = rvh.c_projectofb_id AND mm.TP_Asset_ID = rvh.TP_Asset_ID AND TP_WorkingDayType = '02') "+
					"as wornkindaynightcount, "+	
					
					"to_char(COALESCE((SELECT SUM(total) FROM RVOFB_HRDetailEquipo de "+
					"WHERE de.MovementDate between ? and ? "+
					"AND de.C_ProjectOFB_ID = rvh.c_projectofb_id AND de.TP_Asset_ID = rvh.TP_Asset_ID AND TP_WorkingDayType = '01'),'00:00:00'::interval),'HH24:MI'::text) "+
					"as wornkindayhrmorning, "+
					
					"to_char(COALESCE((SELECT SUM(total) FROM RVOFB_HRDetailEquipo de "+ 
					"WHERE de.MovementDate between ? and ? "+
					"AND de.C_ProjectOFB_ID = rvh.c_projectofb_id AND de.TP_Asset_ID = rvh.TP_Asset_ID AND TP_WorkingDayType = '03'),'00:00:00'::interval),'HH24:MI'::text)"+ 
					"as wornkindayhrafternoon,"+
					
					"to_char(COALESCE((SELECT SUM(total) FROM RVOFB_HRDetailEquipo de "+
					"WHERE de.MovementDate between ? and ? "+
					"AND de.C_ProjectOFB_ID = rvh.c_projectofb_id AND de.TP_Asset_ID = rvh.TP_Asset_ID AND TP_WorkingDayType = '02'),'00:00:00'::interval),'HH24:MI'::text) "+
					"as wornkindayhrnight from  rvofb_hojaderuta_detalle rvh "+
					"where movementdate between ? and ? ");
		
		if (p_C_ProjectOFB_ID > 0)
		{
			sqlC.append(" AND rvh.C_ProjectOFB_ID = ? ");
		}
		
		sqlC.append(" GROUP BY rvh.c_projectofb_id,rvh.tractoname,rvh.TP_Asset_ID order by tractoname");
		
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
				pstmt.setTimestamp(11, p_MovementDateFrom);
				pstmt.setTimestamp(12, p_MovementDateTo);
				pstmt.setTimestamp(13, p_MovementDateFrom);
				pstmt.setTimestamp(14, p_MovementDateTo);
				pstmt.setTimestamp(15, p_MovementDateFrom);
				pstmt.setTimestamp(16, p_MovementDateTo);
				pstmt.setTimestamp(17, p_MovementDateFrom);
				pstmt.setTimestamp(18, p_MovementDateTo);
				pstmt.setTimestamp(19, p_MovementDateFrom);
				pstmt.setTimestamp(20, p_MovementDateTo);
				pstmt.setTimestamp(21, p_MovementDateFrom);
				pstmt.setTimestamp(22, p_MovementDateTo);
				pstmt.setTimestamp(23, p_MovementDateFrom);
				pstmt.setTimestamp(24, p_MovementDateTo);
				pstmt.setTimestamp(25, p_MovementDateFrom);
				pstmt.setTimestamp(26, p_MovementDateTo);
				pstmt.setTimestamp(27, p_MovementDateFrom);
				pstmt.setTimestamp(28, p_MovementDateTo);
				pstmt.setTimestamp(29, p_MovementDateFrom);
				pstmt.setTimestamp(30, p_MovementDateTo);
				pstmt.setTimestamp(31, p_MovementDateFrom);
				pstmt.setTimestamp(32, p_MovementDateTo);
				pstmt.setTimestamp(33, p_MovementDateFrom);
				pstmt.setTimestamp(34, p_MovementDateTo);
				pstmt.setTimestamp(35, p_MovementDateFrom);
				pstmt.setTimestamp(36, p_MovementDateTo);
				pstmt.setTimestamp(37, p_MovementDateFrom);
				pstmt.setTimestamp(38, p_MovementDateTo);
				pstmt.setTimestamp(39, p_MovementDateFrom);
				pstmt.setTimestamp(40, p_MovementDateTo);
				pstmt.setTimestamp(41, p_MovementDateFrom);
				pstmt.setTimestamp(42, p_MovementDateTo);
				pstmt.setTimestamp(43, p_MovementDateFrom);
				pstmt.setTimestamp(44, p_MovementDateTo);
				pstmt.setTimestamp(45, p_MovementDateFrom);
				pstmt.setTimestamp(46, p_MovementDateTo);
				
				if (p_C_ProjectOFB_ID > 0)
				{
					pstmt.setInt(47,p_C_ProjectOFB_ID);
				}			
				
				
				
				ResultSet rs = pstmt.executeQuery();
												
				while (rs.next())
				{
					
					String Insert=new String("INSERT INTO T_ReportePatente(ad_pinstance_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,"+
					"m_movement_id,c_projectofb_id,tractoname,tp_asset_id,tp_sumkm,tp_sumamount,tp_sumvol,kmtotal,cost,tp_volm3,kmdif,costdif,"+
					"voldif,kmrecorrido,kmnocomercial,costeo,kmnocomercialporcent,tsm_travelcount,enex_travelcount,nocomercialtravels,"+
					"shipmentcount,shipmentcountmas,shipmentdif,workingdayhr,workingdayhrporcent,workingdaycomodin,workingdaymorningcount,"+
					"wornkindaynightcount,wornkindayhrmorning,wornkindayhrnight,wornkindayafternooncount,wornkindayhrafternoon) "+
					"VALUES ("+p_PInstance_ID+","+rs.getInt("AD_Client_ID")+","+rs.getInt("AD_Org_ID")+",'"+rs.getString("IsActive")+"','"+rs.getTimestamp("Created")+"',"+
					rs.getInt("CreatedBy")+",'"+rs.getTimestamp("Updated")+"',"+rs.getInt("UpdatedBy")+","+
					rs.getInt("m_movement_id")+","+ rs.getInt("c_projectofb_id")+",'"+rs.getString("tractoname")+"',"+
					rs.getInt("tp_asset_id")+","+rs.getDouble("tp_sumkm")+","+ rs.getDouble("tp_sumamount")+","+
					rs.getDouble("tp_sumvol")+","+rs.getDouble("kmtotal")+","+rs.getBigDecimal("cost")+","+rs.getBigDecimal("tp_volm3")+","+rs.getBigDecimal("kmdif")+","+rs.getBigDecimal("costdif")+","+
					rs.getBigDecimal("voldif")+","+rs.getBigDecimal("kmrecorrido")+","+rs.getBigDecimal("kmnocomercial")+","+rs.getDouble("costeo")+","+rs.getDouble("kmnocomercialporcent")+","+
					rs.getDouble("tsm_travelcount")+","+rs.getDouble("enex_travelcount")+","+rs.getDouble("nocomercialtravels")+","+
					rs.getDouble("shipmentcount")+","+rs.getDouble("shipmentcountmas")+","+rs.getDouble("shipmentdif")+",'"+rs.getString("workingdayhr")+"','"+rs.getString("workingdayhrporcent")+"','"+
					rs.getString("workingdaycomodin")+"',"+rs.getDouble("workingdaymorningcount")+","+rs.getDouble("wornkindaynightcount")+",'"+rs.getString("wornkindayhrmorning")+"','"+
					rs.getString("wornkindayhrnight")+"','"+rs.getDouble("wornkindayafternooncount")+"','"+rs.getString("wornkindayhrafternoon")+"')");
						
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
			
			DB.executeUpdate("UPDATE T_ReportePatente SET AD_PInstance_ID="+p_PInstance_ID, get_TrxName());
		
		return "";
	}	//	doIt
		
}	//	Reporte Contable Generico GORE 
