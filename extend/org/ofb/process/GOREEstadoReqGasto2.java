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
 *  @author faaguilar
 *  @version $Id: LibroMayor.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class GOREEstadoReqGasto2 extends SvrProcess
{
	/**	The Order				*/
	
	private Timestamp 	p_DateAcct_From;
	private Timestamp 	p_DateAcct_To;
	private int 		p_PInstance_ID;
	private String 		p_postingtype;
	//private int 		p_Account_ID;

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
		DB.executeUpdate("DELETE FROM T_EstadoReqGasto2", get_TrxName());
		/*********/
		
		String sqlC = "select MAX(ad_client_id) as ad_client_id,MAX(ad_org_id) as ad_org_id,MAX(isactive) as isactive, "+
				"MAX(created) as created,MAX(createdby) as createdby,MAX(updated) as updated,MAX(updatedby) as updatedby, " +
				"rv.account_id, btrim(replace(to_char(round((select coalesce(sum(debito),0) "+ 
				"from rvofb_b1test where (description like '%Carga Contable Inicial%' or tipo like 'C') "+ 
				"and dateacct >= ? and dateacct2 <= ? and account_id = rv.account_id) "+
				"),'999999999999'::text), ','::text, '.'::text)) as inicial, "+
				"btrim(replace(to_char(round((select coalesce(sum(debito),0) from rvofb_b1test "+ 
				"where description not like '%Carga Contable Inicial%' and (tipo is null or tipo not like 'C') and account_id = rv.account_id "+ 
				"and dateacct >= ? and dateacct2 <= ?) ), '999999999999'::text), ','::text, '.'::text)) as debito, "+
				"btrim(replace(to_char(round(sum(rv.credito)), '999999999999'::text), ','::text, '.'::text)) as credito, "+
				"(select value || '.00.' || name from c_elementvalue where c_elementvalue_id = rv.account_id) as cuenta, "+
				"(select levelacct from c_elementvalue where c_elementvalue_id = rv.account_id) as levelacct, "+
				"'N' as IsSummary "+
				"from RVOFB_BaseReqGasto rv where rv.dateacct >= ? and rv.dateacct2 <= ? "+				
				"and rv.postingtype = ? and cuenta not like 'Defaul%' "+				
				"group by account_id "+
				"union "+
				"select ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,c_elementvalue_id,'-','-','-',value || '.00.' || name,levelacct,'Y' from C_ElementValue Where IsSummary = 'Y' "+
				"and ad_client_id = "+Env.getAD_Client_ID(getCtx())+
				"and value not like 'Defaul%' "+				
				"order by cuenta";				
		
		
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
			pstmt.setString(7, p_postingtype);
			
			rs = pstmt.executeQuery();
		
			int ind = 0;
														
			while (rs.next())
			{
				ind = ind+1;
				Double debito;
				Double credito;
				Double inicial;
				Double dif1;
				Double dif2;
				
				if (rs.getString("debito").equals("-"))
					debito = (double) 0;		
				else
					debito = Double.parseDouble(rs.getString("debito"));
				
				if (rs.getString("credito").equals("-"))
					credito = (double) 0;		
				else
					credito = Double.parseDouble(rs.getString("credito"));
				
				if (rs.getString("inicial").equals("-"))
					inicial = (double) 0;		
				else
					inicial = Double.parseDouble(rs.getString("inicial"));
				
				String Insert=new String("INSERT INTO T_EstadoReqGasto2(ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,"+
				"account_id,amtacctdr,amtacctcr,inicial,dif1,dif2,nameacct,levelacct,rownum,IsSummary) "+
				"VALUES ("+rs.getInt("AD_Client_ID")+","+rs.getInt("AD_Org_ID")+",'"+rs.getString("IsActive")+"','"+rs.getTimestamp("Created")+"',"+
				rs.getInt("CreatedBy")+",'"+rs.getTimestamp("Updated")+"',"+rs.getInt("UpdatedBy")+","+rs.getInt("account_id")+","+
				debito+","+credito+","+inicial+","+(debito-credito)+","+(inicial-(debito-credito))+",'"+rs.getString("cuenta")+"',"+rs.getInt("levelacct")+","+ind+",'"+rs.getString("IsSummary") +"')");
						
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
			
		DB.executeUpdate("UPDATE T_EstadoReqGasto2 SET AD_PInstance_ID="+p_PInstance_ID, get_TrxName());
		
		
		//inicio actualizacion de monto de entidades padre
		
		String sqlAcct = "select rownum, levelacct,account_id from T_EstadoReqGasto2 where IsSummary = 'Y' order by nameacct";
		String sqlDet = "SELECT levelacct,amtacctdr,amtacctcr,inicial FROM T_EstadoReqGasto2 where rownum > ? order by rownum";
		
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try
		{	
			pstmt2 = DB.prepareStatement(sqlAcct, get_TrxName());
			rs2 = pstmt2.executeQuery();	
														
			while (rs2.next())
			{	
				BigDecimal amtSumI = new BigDecimal("0.0");
				BigDecimal amtSumD = new BigDecimal("0.0");
				BigDecimal amtSumC= new BigDecimal("0.0");
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
							amtSumI = amtSumI.add(rs3.getBigDecimal("inicial"));
							amtSumD = amtSumD.add(rs3.getBigDecimal("amtacctdr"));
							amtSumC = amtSumC.add(rs3.getBigDecimal("amtacctcr"));
						}else
						{
							stop = 1;
							break;
						}
					}
										
					DB.executeUpdate("UPDATE T_EstadoReqGasto2 SET amtacctdr = "+amtSumD+ ", amtacctcr = "+amtSumC+", inicial = "+amtSumI+
								", dif1 = "+amtSumD.subtract(amtSumC) +", dif2 = "+amtSumI.subtract(amtSumD.subtract(amtSumC))+
								" WHERE rownum = "+rs2.getInt("rownum")+" and account_id = "+rs2.getInt("account_id"),get_TrxName());
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
