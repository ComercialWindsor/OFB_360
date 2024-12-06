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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import java.math.BigDecimal;
 
/**
 *	report balances 8
 *	
 *  @author faaguilar
 *  @version $Id: EstadoResultadoGrl.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class EstadoResultadoGrl extends SvrProcess
{
	/**	The Order				*/
	private int		Year_ID = 0;
	private int		Org_ID = 0;
	private int FACTORV=0;
	private String TIPO;
	private int p_PInstance_ID;

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
			else if (name.equals("C_Year_ID"))
				Year_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				Org_ID = para[i].getParameterAsInt();
			else if (name.equals("FACTOR"))
				FACTORV = para[i].getParameterAsInt();
			else if (name.equals("TIPO"))
				TIPO = (String)para[i].getParameter();
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
		DB.executeUpdate("DELETE FROM T_EstResultado;", get_TrxName());
		/*********/
		String Grupo=new String("SELECT L.AD_ORG_ID,L.AD_CLIENT_ID,L.PA_REPORTLINE_ID,L.NAME as GRUPO,S.C_ELEMENTVALUE_ID,E.ISSUMMARY,E.NAME,cast(L.SEQNO as numeric) AS SEQNO,"
	    +"E.ACCOUNTTYPE,coalesce(cast(S.DESCRIPTION as numeric),0) AS orden "
		+"FROM PA_REPORTLINESET LS "
		+"INNER JOIN PA_REPORTLINE L  ON (LS.PA_REPORTLINESET_ID=L.PA_REPORTLINESET_ID) "
		+"INNER JOIN PA_REPORTSOURCE S ON (L.PA_REPORTLINE_ID=S.PA_REPORTLINE_ID) "
		+"INNER JOIN C_ELEMENTVALUE E ON (S.C_ELEMENTVALUE_ID=E.C_ELEMENTVALUE_ID) "
		+"WHERE UPPER(TRIM(LS.NAME))='ESTADO DE RESULTADO GENERAL'");
		
		
		
		
		
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(Grupo, get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					char type=rs.getString("accounttype").charAt(0);
					BigDecimal meses[] = new  BigDecimal[13];
					
					if(type=='A' || type=='E')
					{
						for(int i=1;i<13;i++)
						 meses[i]=DB.getSQLValueBD(get_TrxName(), "SELECT coalesce((SUM(amtacctdr)-SUM(amtacctcr)),0) " 
								+"FROM FACT_ACCT f "
								+"INNER JOIN C_PERIOD Pe ON(pe.c_period_id=f.C_Period_ID) "
								+"WHERE f.account_id="+rs.getInt("c_elementvalue_id")+" AND "
								+"PE.c_Year_ID="+Year_ID+" AND pe.PERIODNO="+i);
						
					}
					else
					{
						for(int i=1;i<13;i++)
							 meses[i]=DB.getSQLValueBD(get_TrxName(), "SELECT coalesce((SUM(amtacctcr)-SUM(amtacctdr)),0) " 
									+"FROM FACT_ACCT f "
									+"INNER JOIN C_PERIOD Pe ON(pe.c_period_id=f.C_Period_ID) "
									+"WHERE f.account_id="+rs.getInt("c_elementvalue_id")+" AND "
									+"PE.c_Year_ID="+Year_ID+" AND pe.PERIODNO="+i);
						
					}
					
					String Insert=new String("INSERT INTO T_EstResultado (AD_ORG_ID,AD_CLIENT_ID,NAME,DESCRIPTION,Enero,Febrero,Marzo,abril,mayo,junio,julio,agosto,septiembre,octubre,noviembre,diciembre,C_YEAR_ID,ACCOUNTTYPE,SEQNO,ORDEN2)"
					+"VALUES("+rs.getInt(1)+","+rs.getInt(2)+",'"+rs.getString(4)+"','"+rs.getString(7)+"',ROUND("+meses[1]+"/1000),ROUND("+meses[2]+"/1000),"
					+"ROUND("+meses[3]+"/1000),ROUND("+meses[4]+"/1000),ROUND("+meses[5]+"/1000),ROUND("+meses[6]+"/1000),ROUND("+meses[7]+"/1000),ROUND("+meses[8]+"/1000),ROUND("+meses[9]+"/1000),ROUND("+meses[10]+"/1000),"
					+"ROUND("+meses[11]+"/1000),ROUND("+meses[12]+"/1000),"+Year_ID+",'"+rs.getString(9)+"',"+rs.getInt(8)+","+rs.getInt(10)+")");
					
					/*********/
					DB.executeUpdate(Insert, get_TrxName());
					/*********/
					
					for(int i=1;i<13;i++)
						meses[i]=new BigDecimal("0.0");
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			
		
		
		
		return "";
	}	//	doIt
	
}	//	OrderOpen
