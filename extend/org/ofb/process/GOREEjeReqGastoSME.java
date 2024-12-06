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
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import java.math.BigDecimal;
import org.compiere.util.*;
import java.util.Calendar;
import org.compiere.process.*;
 
/**
 *	Reporte Contable Generico GORE
 *	
 *  @author ininoles
 *  @version $Id: GOREEjeReqGastoSME.java,v 1.1 2014/03/25 00:51:02 ininoles$
 */
public class GOREEjeReqGastoSME extends SvrProcess
{
	/**	The Order				*/
	
	private Timestamp 	p_DateAcct_From;
	private Timestamp 	p_DateAcct_To;
	private int 		p_PInstance_ID;
	private String 		p_postingtype;

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
			else if (name.equals("DateAcct"))
			{
				p_DateAcct_From = (Timestamp)para[i].getParameter();
				p_DateAcct_To = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("PostingType"))
			{
				p_postingtype = (String)para[i].getParameter();				
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
		
		
		/*********/
		DB.executeUpdate("DELETE FROM T_EjeReqGastoSME", get_TrxName());
		/*********/
		
		String sqlC = "select MAX(ad_client_id) as ad_client_id,MAX(ad_org_id) as ad_org_id,MAX(isactive) as isactive, "+ 
				"MAX(created) as created,MAX(createdby) as createdby,MAX(updated) as updated,MAX(updatedby) as updatedby, "+ 
				"rv.account_id, btrim(replace(to_char(round((select coalesce(sum(debito),0) from rvofb_basegastosme where (description like '%Carga Contable Inicial%' or tipo like 'C') "+ 
				"and dateacct >= ? and dateacct <= ? and account_id = rv.account_id and AD_Table_ID = 224 and PostingType = rv.PostingType) "+ 
				"),'999999999999'::text), ','::text, '.'::text)) as inicial, btrim(replace(to_char(round((select coalesce(sum(debito),0) from rvofb_BaseGastoSME "+ 
				"where description not like '%Carga Contable Inicial%' and (tipo is null or tipo not like 'C') and account_id = rv.account_id "+ 
				"and dateacct >= ? and dateacct <= ? and AD_Table_ID = 224 and PostingType = rv.PostingType) + "+
				"(select coalesce(sum(debito),0) from rvofb_basegastosme "+ 
				"where account_id = rv.account_id and dateacct >= ? and dateacct <= ? and AD_Table_ID = 1000049 and PostingType = rv.PostingType ) "+ 
				"), '999999999999'::text), ','::text, '.'::text)) as requerimiento, "+ 
				"'0'::text as compromiso, "+
				"btrim(replace(to_char(round(coalesce((select (SUM(amtsourcedr) - SUM(amtsourcecr)) "+
				"from fact_acct where ad_table_id = 1000043 AND account_id = rv.account_id AND PostingType = rv.PostingType "+ 
				"group by account_id, postingtype),0)), '999999999999'::text), ','::text, '.'::text)) as devengado, "+
				"'0'::text as pagado, '0'::text as deudaflotante, "+ 
				"(select value || '.00.' || name from c_elementvalue where c_elementvalue_id = rv.account_id) as cuenta, "+ 
				"(select levelacct from c_elementvalue where c_elementvalue_id = rv.account_id) as levelacct, "+ 
				"'N' as IsSummary "+ 
				"from rvofb_basegastosme rv where rv.dateacct >= ? and rv.dateacct <= ? and PostingType = 'B' "+
				"group by account_id, postingtype union "+ 
				"select ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,c_elementvalue_id,'-','-','-','-','-','-',value || '.00.' || name,levelacct,'Y' from C_ElementValue Where IsSummary = 'Y' "+ 
				"and ad_client_id =  "+Env.getAD_Client_ID(getCtx())+ "order by cuenta ";				
		
		
			//Select con parametro de fechas 	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{				
			//registros con filtro de fecha
			pstmt = DB.prepareStatement(sqlC, get_TrxName());
			pstmt.setTimestamp(1, p_DateAcct_From);
			pstmt.setTimestamp(2, p_DateAcct_To);
			pstmt.setTimestamp(3, p_DateAcct_From);
			pstmt.setTimestamp(4, p_DateAcct_To);
			pstmt.setTimestamp(5, p_DateAcct_From);
			pstmt.setTimestamp(6, p_DateAcct_To);
			pstmt.setTimestamp(7, p_DateAcct_From);
			pstmt.setTimestamp(8, p_DateAcct_To);
			
			rs = pstmt.executeQuery();
		
			int ind = 0;
														
			while (rs.next())
			{
				ind = ind+1;
				Double inicial;
				Double requerimiento;
				Double compromiso;
				Double devengado;
				Double pagado;
				Double deudaflotante;
				Double disponibilidad;
				Double dpdevengado;
				Double daplicada;
				
				
				if (rs.getString("inicial").equals("-"))
					inicial = (double) 0;		
				else
					inicial = Double.parseDouble(rs.getString("inicial"));
				
				if (rs.getString("requerimiento").equals("-"))
					requerimiento = (double) 0;		
				else
					requerimiento = Double.parseDouble(rs.getString("requerimiento"));
				
				if (rs.getString("compromiso").equals("-"))
					compromiso = (double) 0;		
				else
					compromiso = Double.parseDouble(rs.getString("compromiso"));
				
				if (rs.getString("devengado").equals("-"))
					devengado = (double) 0;		
				else
					devengado = Double.parseDouble(rs.getString("devengado"));
				
				if (rs.getString("pagado").equals("-"))
					pagado = (double) 0;		
				else
					pagado = Double.parseDouble(rs.getString("pagado"));
				
				if (rs.getString("deudaflotante").equals("-"))
					deudaflotante = (double) 0;		
				else
					deudaflotante = Double.parseDouble(rs.getString("deudaflotante"));
				
				disponibilidad = requerimiento-compromiso;
				dpdevengado = compromiso-devengado;
				daplicada = requerimiento-devengado;
				
				
				String Insert=new String("INSERT INTO T_EjeReqGastoSME(ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,"+
				"account_id,inicial,requerimiento,compromiso,disponibilidad,devengado,dpdevengado,daplicada,pagado,dflotante,nameacct,levelacct,rownum,IsSummary) "+
				"VALUES ("+rs.getInt("AD_Client_ID")+","+rs.getInt("AD_Org_ID")+",'"+rs.getString("IsActive")+"','"+rs.getTimestamp("Created")+"',"+
				rs.getInt("CreatedBy")+",'"+rs.getTimestamp("Updated")+"',"+rs.getInt("UpdatedBy")+","+rs.getInt("account_id")+","+
				inicial+","+requerimiento+","+compromiso+","+disponibilidad+","+devengado+","+dpdevengado+","+daplicada+","+pagado+","+deudaflotante+",'"
				+rs.getString("cuenta")+"',"+rs.getInt("levelacct")+","+ind+",'"+rs.getString("IsSummary") +"')");
						
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
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
			
		DB.executeUpdate("UPDATE T_EjeReqGastoSME SET AD_PInstance_ID="+p_PInstance_ID, get_TrxName());
		
		
		//inicio actualizacion de monto de entidades padre
		
		String sqlAcct = "select rownum, levelacct,account_id from T_EjeReqGastoSME where IsSummary = 'Y' order by nameacct";
		String sqlDet = "SELECT levelacct,inicial,requerimiento,compromiso,disponibilidad,devengado,dpdevengado,daplicada,pagado,dflotante FROM T_EjeReqGastoSME where rownum > ? order by rownum";
		
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try
		{	
			pstmt2 = DB.prepareStatement(sqlAcct, get_TrxName());
			rs2 = pstmt2.executeQuery();	
														
			while (rs2.next())
			{
				BigDecimal sumInicial = new BigDecimal("0.0");
				BigDecimal sumRequerimiento = new BigDecimal("0.0");
				BigDecimal sumCompromiso = new BigDecimal("0.0");
				BigDecimal sumDisponibilidad= new BigDecimal("0.0");
				BigDecimal sumDevengado = new BigDecimal("0.0");
				BigDecimal sumDPDevengado = new BigDecimal("0.0");
				BigDecimal sumDAplicada = new BigDecimal("0.0");
				BigDecimal sumPagado= new BigDecimal("0.0");
				BigDecimal sumDFlotante= new BigDecimal("0.0");
				
				int stop = 0;
			
				PreparedStatement pstmt3 = null;
				ResultSet rs3 = null;
				try
				{	
					pstmt3 = DB.prepareStatement(sqlDet, get_TrxName());
					pstmt3.setInt(1, rs2.getInt("rownum"));
					rs3 = pstmt3.executeQuery();	
																
					while (rs3.next() && stop == 0)						
					{	
						if (rs2.getInt("levelacct") < rs3.getInt("levelacct"))
						{
							sumInicial = sumInicial.add(rs3.getBigDecimal("inicial"));
							sumRequerimiento = sumRequerimiento.add(rs3.getBigDecimal("requerimiento"));
							sumCompromiso = sumCompromiso.add(rs3.getBigDecimal("compromiso"));
							sumDisponibilidad = sumDisponibilidad.add(rs3.getBigDecimal("disponibilidad"));
							sumDevengado = sumDevengado.add(rs3.getBigDecimal("devengado"));
							sumDPDevengado = sumDPDevengado.add(rs3.getBigDecimal("dpdevengado"));
							sumDAplicada = sumDAplicada.add(rs3.getBigDecimal("daplicada"));
							sumPagado = sumPagado.add(rs3.getBigDecimal("pagado"));
							sumDFlotante = sumDFlotante.add(rs3.getBigDecimal("dflotante"));
							
						}else
						{
							stop = 1;
							break;
						}
					}
										
					DB.executeUpdate("UPDATE T_EjeReqGastoSME SET inicial = "+sumInicial+", requerimiento = "+sumRequerimiento+ ", compromiso = "+sumCompromiso+
							", disponibilidad = "+sumDisponibilidad+", devengado = "+sumDevengado+", dpdevengado = "+sumDPDevengado+", daplicada = "+sumDAplicada+
							", pagado = "+sumPagado+", dflotante = "+sumDFlotante+
							" WHERE rownum = "+rs2.getInt("rownum")+"and account_id = "+rs2.getInt("account_id"),get_TrxName());
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
				finally {
					DB.close(rs3, pstmt3);
					rs3 = null; pstmt3 = null;
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		finally {
			DB.close(rs2, pstmt2);
			rs2 = null; pstmt2 = null;
		}
		return "";
	}	//	doIt
		
}	//	Reporte Contable Generico GORE 
