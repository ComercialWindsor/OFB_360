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
import org.compiere.util.*;
import java.util.Calendar;
 
/**
 *	report balances 8
 *	
 *  @author faaguilar
 *  @version $Id: LibroMayor.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class LibroMayor extends SvrProcess
{
	/**	The Order				*/
	
	private int		Org_ID = 0;
	private int 	Period_ID=0;
	private int 	p_PInstance_ID=0;
	private int 	p_Account_ID=0;

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
			else if (name.equals("AD_Org_ID"))
				Org_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Period_ID"))
				Period_ID = para[i].getParameterAsInt();
			else if (name.equals("Account_ID"))
				p_Account_ID = para[i].getParameterAsInt();
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
		DB.executeUpdate("DELETE FROM T_LIBROMAYOR", get_TrxName());
		/*********/
		String accountlist=new String("SELECT AD_CLIENT_ID,AD_ORG_ID,ACCOUNT_ID,ACCOUNTVALUE ,NAME, Account_ID "
				+"FROM RV_FACT_ACCT rf "
				+"WHERE DateAcct BETWEEN ? AND ? AND (AD_Org_ID="+Org_ID
				+" OR AD_Org_ID in (Select oi.AD_ORG_ID from AD_orginfo oi where oi.PARENT_ORG_ID="+Org_ID+") )"
				+"GROUP BY AD_CLIENT_ID,AD_ORG_ID,ACCOUNT_ID,ACCOUNTVALUE ,NAME "
				+"ORDER BY ACCOUNTVALUE ASC");
		
		
		String PeriodMov=new String("SELECT ACCOUNT_ID,TRUNC(DATEACCT)AS DATEACCT,SUM(AMTACCTDR) AS AMTACCTDR,SUM(AMTACCTCR) AS AMTACCTCR,ACCOUNTVALUE ,NAME "
				+"FROM RV_FACT_ACCT rf "
				+"WHERE C_PERIOD_ID=? AND ACCOUNT_ID=? AND (AD_Org_ID="+Org_ID
				+" OR AD_Org_ID in (Select oi.AD_ORG_ID from AD_orginfo oi where oi.PARENT_ORG_ID="+Org_ID+") )"
				+"GROUP BY ACCOUNT_ID,TRUNC(DATEACCT),ACCOUNTVALUE ,NAME "
				+"ORDER BY DATEACCT ASC");
		
		MPeriod periodEnd = new MPeriod(getCtx(),Period_ID , get_TrxName());
		MPeriod periodStart =MPeriod.getFirstInYear(getCtx(), periodEnd.getStartDate());
		MPeriod periodAnt=null;
		if(periodEnd.getPeriodNo()>1)
			periodAnt=getPeriodAnt(periodEnd);
		
		//log.info("periodEnd no  :" + periodEnd.getPeriodNo());
		//log.info("periodStart :" + periodStart.getStartDate().toString());
		//if(periodEnd.getPeriodNo()>1)
		//log.info("periodAnt :" + periodAnt.getEndDate().toString());
		
		int LineNo=1;
		double ACUMD=0;
		double ACUMC=0;
		BigDecimal BACUMD=new BigDecimal(0);
		BigDecimal BACUMC=new BigDecimal(0);
		double SALDOD=0;
		double SALDOC=0;
		double TOTALD=0;
		double TOTALC=0;
		double SALDOFD=0;
		double SALDOFC=0;
			
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(accountlist, get_TrxName());
				pstmt.setTimestamp(1, periodStart.getStartDate());
				pstmt.setTimestamp(2, periodEnd.getEndDate());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					
					String sysdate="TO_DATE('"+TimeUtil.getToday().get(Calendar.YEAR)+"-"+ TimeUtil.getToday().get(Calendar.MONTH)+"-"+ TimeUtil.getToday().get(Calendar.DAY_OF_MONTH)+"','YYYY-MM-DD')";
						
					String Insert=new String("INSERT INTO T_LIBROMAYOR(AD_CLIENT_ID,AD_ORG_ID,CREATED,UPDATED,CREATEDBY,UPDATEDBY,DESCRIP1,DESCRIP2,AMTACCTDR,AMTACCTCR,LINENO,C_PERIOD_ID)"
					+" VALUES ("+rs.getInt("AD_CLIENT_ID")+","+Org_ID+","+sysdate+","+sysdate+",100,100,'"+rs.getString("ACCOUNTVALUE")+"','"+rs.getString("NAME")+"',NULL,NULL,"+LineNo+","+Period_ID+")");
					
					DB.executeUpdate(Insert, get_TrxName());
					
					LineNo++;
					ACUMD=0;
					ACUMC=0;
					if(periodEnd.getPeriodNo()>1){
						BACUMD=new BigDecimal(0);
						BACUMC=new BigDecimal(0);
						BigDecimal[] valAcums = getAcum(periodStart.getStartDate(),periodAnt.getEndDate(),rs.getInt("Account_ID") );
						BACUMD = valAcums[0];
						BACUMC = valAcums[1];
						//log.info("Count_ID :" + rs.getInt("Account_ID") + " "+BACUMD.doubleValue()+ " "+BACUMC.doubleValue());
						ACUMD=BACUMD.doubleValue();
						ACUMC=BACUMC.doubleValue();
					}else{
						ACUMD=0;
						ACUMC=0;
					}
					
					//acumulado anterior
					Insert="INSERT INTO T_LIBROMAYOR(AD_CLIENT_ID,AD_ORG_ID,CREATED,UPDATED,CREATEDBY,UPDATEDBY,DESCRIP1,DESCRIP2,AMTACCTDR,AMTACCTCR,LINENO,C_PERIOD_ID) "
					+"VALUES ("+rs.getInt("AD_CLIENT_ID")+","+Org_ID+","+sysdate+","+sysdate+",100,100,NULL,"+ "'ACUMULADO AL : " + periodAnt.getEndDate().toString().substring(0, 10)+"',"+ACUMD+","+ACUMC+","+LineNo+","+Period_ID+")";
					DB.executeUpdate(Insert, get_TrxName());
					
					LineNo++;
				    SALDOD=0;
					SALDOC=0;
					
					if(ACUMD>ACUMC){
					  SALDOD=0;
					  SALDOC=ACUMD-ACUMC;
					}else{
					  SALDOD=ACUMC-ACUMD;
					  SALDOC=0;
					}
					
					//saldo acumulado anterior
					Insert="INSERT INTO T_LIBROMAYOR(AD_CLIENT_ID,AD_ORG_ID,CREATED,UPDATED,CREATEDBY,UPDATEDBY,DESCRIP1,DESCRIP2,AMTACCTDR,AMTACCTCR,LINENO,C_PERIOD_ID) "
					+"VALUES ("+rs.getInt("AD_CLIENT_ID")+","+Org_ID+","+sysdate+","+sysdate+",100,100,NULL,"+"'SALDO AL : "+periodAnt.getEndDate().toString().substring(0, 10)+"',"+SALDOD+","+SALDOC+","+LineNo+","+Period_ID+")";
					DB.executeUpdate(Insert, get_TrxName());
					LineNo++;
					TOTALD=ACUMD;
					TOTALC=ACUMC;
					
					
					PreparedStatement pstmt2 = DB.prepareStatement(PeriodMov, get_TrxName());
					pstmt2.setInt(1,Period_ID);
					pstmt2.setInt(2, rs.getInt("Account_ID"));
					ResultSet rs2 = pstmt2.executeQuery();
					while (rs2.next())
					{
						String tempDate = rs2.getString("DATEACCT");
						if(tempDate.length()>10)
							tempDate = tempDate.substring(0, 10);
						Insert="INSERT INTO T_LIBROMAYOR(AD_CLIENT_ID,AD_ORG_ID,CREATED,UPDATED,CREATEDBY,UPDATEDBY,DESCRIP1,DESCRIP2,AMTACCTDR,AMTACCTCR,LINENO,C_PERIOD_ID) "
						+"VALUES ("+rs.getInt("AD_CLIENT_ID")+","+Org_ID+","+sysdate+","+sysdate+",100,100,'"+tempDate+"','"+ rs2.getString("NAME")+"',"+rs2.getDouble("AMTACCTDR")+","+rs2.getDouble("AMTACCTCR")+","+LineNo+","+Period_ID+")";
						DB.executeUpdate(Insert, get_TrxName());
						
						LineNo++;
						TOTALD+=rs2.getDouble("AMTACCTDR");
						TOTALC+=rs2.getDouble("AMTACCTCR");
					}
					rs2.close();
					pstmt2.close();
					pstmt2 = null;
					
					//total acumulado + periodo reportado
					Insert="INSERT INTO T_LIBROMAYOR(AD_CLIENT_ID,AD_ORG_ID,CREATED,UPDATED,CREATEDBY,UPDATEDBY,DESCRIP1,DESCRIP2,AMTACCTDR,AMTACCTCR,LINENO,C_PERIOD_ID) "
					+"VALUES ("+rs.getInt("AD_CLIENT_ID")+","+Org_ID+","+sysdate+","+sysdate+",100,100,NULL,'" +"TOTAL AL : "+periodEnd.getEndDate().toString().substring(0, 10) +"',"+TOTALD+","+TOTALC+","+LineNo+","+Period_ID+")";
					DB.executeUpdate(Insert, get_TrxName());
					
					LineNo++;
					
					//saldo acumulado + periodo reportado
					SALDOFD=0;
					SALDOFC=0;
					if(TOTALD>TOTALC){
					  SALDOFD=0;
					  SALDOFC=TOTALD-TOTALC;
					}else{
					  SALDOFD=TOTALC-TOTALD;
					  SALDOFC=0;
					}
					
					Insert="INSERT INTO T_LIBROMAYOR(AD_CLIENT_ID,AD_ORG_ID,CREATED,UPDATED,CREATEDBY,UPDATEDBY,DESCRIP1,DESCRIP2,AMTACCTDR,AMTACCTCR,LINENO,C_PERIOD_ID) "
					+"VALUES ("+rs.getInt("AD_CLIENT_ID")+","+Org_ID+","+sysdate+","+sysdate+",100,100,NULL,'"+"SALDO AL : "+periodEnd.getEndDate().toString().substring(0, 10)+"',"+SALDOFD+","+SALDOFC+","+LineNo+","+Period_ID+")";
					DB.executeUpdate(Insert, get_TrxName());
					
					LineNo++;
					//ESPACIO BLANCO
					Insert="INSERT INTO T_LIBROMAYOR(AD_CLIENT_ID,AD_ORG_ID,CREATED,UPDATED,CREATEDBY,UPDATEDBY,DESCRIP1,DESCRIP2,AMTACCTDR,AMTACCTCR,LINENO,C_PERIOD_ID) "
					+"VALUES ("+rs.getInt("AD_CLIENT_ID")+","+Org_ID+","+sysdate+","+sysdate+",100,100,NULL, NULL ,NULL,NULL,"+LineNo+","+Period_ID+")";
					DB.executeUpdate(Insert, get_TrxName());
					LineNo++;
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			
			DB.executeUpdate("UPDATE T_LIBROMAYOR SET AD_PInstance_ID="+p_PInstance_ID, get_TrxName());
			
		
		
		return "";
	}	//	doIt
	
	public BigDecimal[] getAcum(Timestamp dateFrom, Timestamp dateTo, int Account_ID){
		
		PreparedStatement pstmt = null;
		BigDecimal[] reValue = new BigDecimal[]{new BigDecimal(0),new BigDecimal(0)};
		String mysql="SELECT coalesce(SUM(AMTACCTDR),0), coalesce(SUM(AMTACCTCR),0) "
					 +"	FROM RV_FACT_ACCT"
					 +"	WHERE DateAcct BETWEEN ? AND ? AND Account_ID=?"
					 +" AND (AD_Org_ID="+Org_ID
					 +" OR AD_Org_ID in (Select oi.AD_ORG_ID from AD_orginfo oi where oi.PARENT_ORG_ID="+Org_ID+") )";
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setTimestamp(1, dateFrom);
			pstmt.setTimestamp(2, dateTo);
			pstmt.setInt(3, Account_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				reValue[0]=(rs.getBigDecimal(1));
				reValue[1]=(rs.getBigDecimal(2));
				//log.info("getAcum: "+ rs.getBigDecimal(1) + " " + rs.getBigDecimal(2));
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return reValue;
		
	}
	
	public MPeriod getPeriodAnt(MPeriod actual){
		MPeriod retValue=null;
		PreparedStatement pstmt = null;
		String mysql="SELECT * from C_Period where C_Year_ID=? and PeriodNo=?";
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1, actual.getC_Year_ID());
			pstmt.setInt(2, actual.getPeriodNo()-1);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue= new MPeriod(getCtx(),rs , get_TrxName());
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return retValue;
	}
}	//	OrderOpen
