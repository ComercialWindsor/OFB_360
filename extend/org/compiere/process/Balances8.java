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
 
/**
 *	report balances 8
 *	
 *  @author faaguilar
 *  @version $Id: Balances8.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class Balances8 extends SvrProcess
{
	/**	The Order				*/
	private int		PERIODO_ID = 0;
	private int		Org_ID = 0;
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
			else if (name.equals("C_Period_ID"))
				PERIODO_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				Org_ID = para[i].getParameterAsInt();
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
		int period_no, year_id; 
		
		MPeriod myperiod = new MPeriod(getCtx(),PERIODO_ID , get_TrxName());
		period_no=myperiod.getPeriodNo();
		year_id=myperiod.getC_Year_ID();
		
		/*********/
		DB.executeUpdate("DELETE FROM  T_BALANCES8", get_TrxName());
		DB.executeUpdate("DELETE FROM T_BALANCEPADRE", get_TrxName());
		/*********/
		String balancespecial=new String("SELECT rf.AD_CLIENT_ID, rf.AD_ORG_ID, rf.ISACTIVE, rf.CREATED, rf.CREATEDBY, rf.UPDATED, rf.UPDATEDBY, "
		+"rf.ACCOUNT_ID,  rf.DATETRX,  rf.DATEACCT,  rf.C_PERIOD_ID, rf.ACCOUNTVALUE,  rf.NAME,  rf.ACCOUNTTYPE, "
		+"rf.AMTACCTDR AS DEBITO,  rf.AMTACCTCR AS CREDITO, "
		+"CASE WHEN  rf.AMTACCTDR >  rf.AMTACCTCR THEN  rf.AMTACCTDR -  rf.AMTACCTCR  ELSE 0 END AS DEUDOR, "
		+"CASE WHEN  rf.AMTACCTDR <  rf.AMTACCTCR THEN  rf.AMTACCTCR -  rf.AMTACCTDR  ELSE 0 END AS ACREEDOR, "
		+"CASE WHEN ( rf.ACCOUNTTYPE='A' OR  rf.ACCOUNTTYPE='L' ) AND  rf.AMTACCTDR >  rf.AMTACCTCR THEN  rf.AMTACCTDR -  rf.AMTACCTCR ELSE 0 END AS ACTIVO, "
		+"CASE WHEN ( rf.ACCOUNTTYPE='A' OR  rf.ACCOUNTTYPE='L' ) AND  rf.AMTACCTDR <  rf.AMTACCTCR THEN  rf.AMTACCTCR -  rf.AMTACCTDR ELSE 0 END AS PASIVO, "
		+"CASE WHEN ( rf.ACCOUNTTYPE='E' OR  rf.ACCOUNTTYPE='R' OR rf.ACCOUNTTYPE='M') AND  rf.AMTACCTDR >  rf.AMTACCTCR  THEN  rf.AMTACCTDR -  rf.AMTACCTCR ELSE 0 END AS PERDIDA, "
		+"CASE WHEN ( rf.ACCOUNTTYPE='E' OR  rf.ACCOUNTTYPE='R' OR rf.ACCOUNTTYPE='M') AND  rf.AMTACCTDR <  rf.AMTACCTCR  THEN  rf.AMTACCTCR -  rf.AMTACCTDR ELSE 0 END AS GANANCIA "
		+"FROM RV_FACT_ACCT rf "
		+"inner join C_PERIOD pe ON (rf.C_Period_ID=pe.C_Period_ID) "
		+"inner join C_YEAR y ON (pe.C_Year_ID=y.C_year_ID) "
		+"WHERE  rf.AD_CLIENT_ID="+getAD_Client_ID() +" AND y.C_year_ID="+year_id+" AND  pe.PERIODNO BETWEEN 1 AND "+period_no
		+" AND  (rf.AD_ORG_ID IN (SELECT oi.AD_ORG_ID FROM AD_ORGINFO Oi WHERE Oi.PARENT_ORG_ID="+Org_ID+") OR rf.ad_org_id="+Org_ID+")");
		
		String balancespecialall=new String("SELECT rf.AD_CLIENT_ID, rf.AD_ORG_ID, rf.ISACTIVE, rf.CREATED, rf.CREATEDBY, rf.UPDATED, rf.UPDATEDBY, "
		+"rf.ACCOUNT_ID,  rf.DATETRX,  rf.DATEACCT,  rf.C_PERIOD_ID, rf.ACCOUNTVALUE,  rf.NAME,  rf.ACCOUNTTYPE, "
		+"rf.AMTACCTDR AS DEBITO,  rf.AMTACCTCR AS CREDITO, "
		+"CASE WHEN  rf.AMTACCTDR >  rf.AMTACCTCR THEN  rf.AMTACCTDR -  rf.AMTACCTCR  ELSE 0 END AS DEUDOR, "
		+"CASE WHEN  rf.AMTACCTDR <  rf.AMTACCTCR THEN  rf.AMTACCTCR -  rf.AMTACCTDR  ELSE 0 END AS ACREEDOR, "
		+"CASE WHEN ( rf.ACCOUNTTYPE='A' OR  rf.ACCOUNTTYPE='L' ) AND  rf.AMTACCTDR >  rf.AMTACCTCR THEN  rf.AMTACCTDR -  rf.AMTACCTCR ELSE 0 END AS ACTIVO, "
		+"CASE WHEN ( rf.ACCOUNTTYPE='A' OR  rf.ACCOUNTTYPE='L' ) AND  rf.AMTACCTDR <  rf.AMTACCTCR THEN  rf.AMTACCTCR -  rf.AMTACCTDR ELSE 0 END AS PASIVO, "
		+"CASE WHEN ( rf.ACCOUNTTYPE='E' OR  rf.ACCOUNTTYPE='R' OR rf.ACCOUNTTYPE='M') AND  rf.AMTACCTDR >  rf.AMTACCTCR  THEN  rf.AMTACCTDR -  rf.AMTACCTCR ELSE 0 END AS PERDIDA, "
		+"CASE WHEN ( rf.ACCOUNTTYPE='E' OR  rf.ACCOUNTTYPE='R' OR rf.ACCOUNTTYPE='M') AND  rf.AMTACCTDR <  rf.AMTACCTCR  THEN  rf.AMTACCTCR -  rf.AMTACCTDR ELSE 0 END AS GANANCIA "
		+"FROM RV_FACT_ACCT rf "
		+"inner join C_PERIOD pe ON (rf.C_Period_ID=pe.C_Period_ID) "
		+"inner join C_YEAR y ON (pe.C_Year_ID=y.C_year_ID) "
		+"WHERE  rf.AD_CLIENT_ID="+getAD_Client_ID() +" AND y.C_year_ID="+year_id+" AND  pe.PERIODNO BETWEEN 1 AND "+period_no);
		
		String apadre= new String("SELECT  AD_CLIENT_ID, AD_ORG_ID, ACCOUNT_ID, C_PERIOD_ID, "
		+"ACCOUNTVALUE,NAME, ACCOUNTTYPE,DEBITO,CREDITO,DEUDOR,ACREEDOR,ACTIVO, "
		+"PASIVO,PERDIDA,GANANCIA "
		+"FROM "
		+"BALANCE8_COLUMNAS_ACUMULADOP "
		+"UNION "
		+"SELECT "
		+"AD_CLIENT_ID, AD_ORG_ID, 100000000 AS ACCOUNT_ID, C_PERIOD_ID, "
		+"CAST('9S2RESULTADO' AS character(40)) AS ACCOUNTVALUE, CAST('RESULTADO' AS character(60)) AS NAME, '-' AS ACCOUNTTYPE, "
		+"CASE WHEN DEBITO < CREDITO THEN CREDITO - DEBITO  ELSE 0  END as DEBITO, "
		+"CASE WHEN DEBITO > CREDITO THEN DEBITO - CREDITO ELSE 0 END AS CREDITO , "
		+"CASE WHEN DEUDOR < ACREEDOR THEN ACREEDOR - DEUDOR ELSE 0 END AS DEUDOR, "
		+"CASE WHEN DEUDOR > ACREEDOR THEN DEUDOR - ACREEDOR ELSE 0 END AS ACREEDOR, "
		+"CASE WHEN ACTIVO < PASIVO THEN PASIVO - ACTIVO ELSE 0 END AS ACTIVO, "
		+"CASE WHEN ACTIVO > PASIVO THEN ACTIVO - PASIVO ELSE 0 END AS PASIVO, "
		+"CASE WHEN PERDIDA < GANANCIA THEN GANANCIA - PERDIDA ELSE 0 END AS PERDIDA, "
		+"CASE WHEN PERDIDA > GANANCIA THEN PERDIDA - GANANCIA  ELSE 0 END AS GANANCIA "
		+"FROM "
		+"BALANCE8_COLUMNAS_RES_AGP_ACUP "
		+"UNION "
		+"SELECT  AD_CLIENT_ID, AD_ORG_ID, 10000000 AS ACCOUNT_ID, C_PERIOD_ID, "
		+"CAST('9S1SUBTOTAL' AS character(40)) AS ACCOUNTVALUE,CAST('SUBTOTAL' AS character(60)) AS NAME, '-' AS ACCOUNTTYPE, "
		+"SUM(DEBITO)as DEBITO,SUM(CREDITO)as CREDITO, "
		+"SUM(DEUDOR)as DEUDOR,SUM(ACREEDOR)as ACREEDOR,SUM(ACTIVO)as ACTIVO, "
		+"SUM(PASIVO)as PASIVO,SUM(PERDIDA)as PERDIDA,SUM (GANANCIA)as GANANCIA "
		+"FROM "
		+"BALANCE8_COLUMNAS_ACUMULADOP "
		+"GROUP BY AD_CLIENT_ID, AD_ORG_ID, C_PERIOD_ID "
		+"UNION "
		+"SELECT AD_CLIENT_ID, AD_ORG_ID,  100000009 AS ACCOUNT_ID, C_PERIOD_ID, "
		+"CAST('9TOTAL' AS character(40)) AS ACCOUNTVALUE,CAST('TOTAL' AS character(60)) AS NAME, '-' AS ACCOUNTTYPE, "
		+"SUM(DEBITO)as DEBITO,SUM(CREDITO)as CREDITO, "
		+"SUM(DEUDOR)as DEUDOR,SUM(ACREEDOR)as ACREEDOR,SUM(ACTIVO)as ACTIVO, "
		+"SUM(PASIVO)as PASIVO,SUM(PERDIDA)as PERDIDA,SUM (GANANCIA)as GANANCIA "
		+"FROM "
		+"RESULTADOS_TOTALES_8_ACUMP "
		+"GROUP BY AD_CLIENT_ID, AD_ORG_ID, C_PERIOD_ID");
		
		if(Org_ID>0)
		{
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(balancespecial, get_TrxName());
				//log.config(balancespecial);
				ResultSet rs = pstmt.executeQuery();
				
				while (rs.next())
				{
					String insert="INSERT INTO T_BALANCEPADRE (AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, UPDATED,UPDATEDBY,ACCOUNT_ID,NAME,ACCOUNTVALUE ,DESCRIPTION,"
			  		 	  +"ACCOUNTTYPE,DEBITO,CREDITO,DEUDOR,ACREEDOR,ACTIVO,PASIVO,PERDIDA,GANANCIA,AD_PINSTANCE_ID,C_PERIOD_ID)"
						  +"VALUES	("+rs.getInt(1)+", "+Org_ID+", '"+rs.getTimestamp(4)+"', 100, '"+rs.getTimestamp(4)+"',100,"+rs.getInt(8)+",'"+rs.getString(13)+"','"+rs.getString(12)+"' ,'"+rs.getString(13)+"',"
			  		 	  +"'"+rs.getString(14)+"',"+rs.getBigDecimal(15)+","+rs.getBigDecimal(16)+","+rs.getBigDecimal(17)+","+rs.getBigDecimal(18)+","+rs.getBigDecimal(19)+","+rs.getBigDecimal(20)+","+rs.getBigDecimal(21)+","+rs.getBigDecimal(22)+","+p_PInstance_ID+","+PERIODO_ID+")";
					
					DB.executeUpdate(insert, get_TrxName());
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			
		}
		else
		{
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(balancespecialall, get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					String insert="INSERT INTO T_BALANCEPADRE (AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, UPDATED,UPDATEDBY,ACCOUNT_ID,NAME,ACCOUNTVALUE ,DESCRIPTION,"
			  		 	  +"ACCOUNTTYPE,DEBITO,CREDITO,DEUDOR,ACREEDOR,ACTIVO,PASIVO,PERDIDA,GANANCIA,AD_PINSTANCE_ID,C_PERIOD_ID)"
						  +"VALUES	("+rs.getInt(1)+", "+Org_ID+", '"+rs.getTimestamp(4)+"', 100, '"+rs.getTimestamp(4)+"',100,"+rs.getInt(8)+",'"+rs.getString(13)+"','"+rs.getString(12)+"' ,'"+rs.getString(13)+"',"
			  		 	  +"'"+rs.getString(14)+"',"+rs.getBigDecimal(15)+","+rs.getBigDecimal(16)+","+rs.getBigDecimal(17)+","+rs.getBigDecimal(18)+","+rs.getBigDecimal(19)+","+rs.getBigDecimal(20)+","+rs.getBigDecimal(21)+","+rs.getBigDecimal(22)+","+p_PInstance_ID+","+PERIODO_ID+")";
					
					DB.executeUpdate(insert, get_TrxName());
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			
		}
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(apadre, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			Timestamp sqlTimestamp = new Timestamp((new java.util.Date()).getTime());

			while (rs.next())
			{
				String insert="INSERT INTO  T_BALANCES8 (AD_CLIENT_ID, AD_ORG_ID,ISACTIVE, CREATED, CREATEDBY, UPDATED,UPDATEDBY,ACCOUNT_ID,NAME,ACCOUNTVALUE ,DESCRIPTION,"
	  		 	  				 +"ACCOUNTTYPE,DEBITO,CREDITO,DEUDOR,ACREEDOR,ACTIVO,PASIVO,PERDIDA,GANANCIA,AD_PINSTANCE_ID,C_PERIOD_ID)"
				  +"VALUES		 ("+rs.getInt(1)+", "+Org_ID+", 'Y','"+sqlTimestamp+"', 100, '"+sqlTimestamp+"',100,"+rs.getInt(3)+",'"+rs.getString(6)+"','"+rs.getString(5)+"' ,'"+rs.getString(6)+"',"
	  		 	  				 +"'"+rs.getString(7)+"',"+rs.getBigDecimal(8)+","+rs.getBigDecimal(9)+","+rs.getBigDecimal(10)+","+rs.getBigDecimal(11)+","+rs.getBigDecimal(12)+","+rs.getBigDecimal(13)+","+rs.getBigDecimal(14)+","+rs.getBigDecimal(15)+","+p_PInstance_ID+","+PERIODO_ID+")";
				
				DB.executeUpdate(insert, get_TrxName());
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
