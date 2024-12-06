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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.X_TP_DriversControl;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ImportDriversControl.java $
 */
public class ImportDriversControl extends SvrProcess
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
		String clientCheck = " AND AD_Client_ID=" + Env.getAD_Client_ID(getCtx());
		
		//actualizamos flota
		StringBuffer sqlf = new StringBuffer ("UPDATE I_DriversControl dc "
				+ " SET C_ProjectOFB_ID = (SELECT MAX(C_ProjectOFB_ID) FROM C_ProjectOFB aa"
				+ " WHERE upper(dc.I_FlotaName) = upper(aa.Value) AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.C_ProjectOFB_ID IS NULL AND dc.I_FlotaName IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		//actualizamos Periodo
		sqlf = new StringBuffer ("UPDATE I_DriversControl dc  "
				+ " SET C_Period_ID = (SELECT MAX(C_Period_ID) FROM C_Period aa"
				+ " WHERE dc.I_MonthYear = aa.description AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE C_Period_ID IS NULL AND dc.I_MonthYear IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		//actualizamos activo
		sqlf = new StringBuffer ("UPDATE I_DriversControl dc "
				+ " SET A_Asset_ID = (SELECT MAX(A_Asset_ID) FROM A_Asset aa"
				+ " WHERE dc.I_TractoValue = aa.Value AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.A_Asset_ID IS NULL AND dc.I_TractoValue IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		//actualizamos conductor
		sqlf = new StringBuffer ("UPDATE I_DriversControl dc "
				+ " SET C_BPartner_ID = (SELECT MAX(C_BPartner_ID) FROM C_BPartner aa"
				+ " WHERE upper(dc.I_BPartnerName) = upper(aa.name) AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.C_BPartner_ID IS NULL AND dc.I_BPartnerName IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		//actualizamos Supervisor
		sqlf = new StringBuffer ("UPDATE I_DriversControl dc "
				+ " SET Supervisor_ID = (SELECT MAX(AD_User_ID) FROM AD_User aa"
				+ " WHERE upper(dc.I_Supervisor) = upper(aa.description) AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.Supervisor_ID IS NULL AND dc.I_Supervisor IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		/*actualizar errores*/
		if (DB.isPostgreSQL())
		{
			sqlf = new StringBuffer ("UPDATE I_DriversControl "	// no flota
					  + " SET I_IsImported='N', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=No Flota, ' "
					  + " WHERE C_ProjectOFB_ID IS NULL"
					  + " AND I_IsImported <> 'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			sqlf = new StringBuffer ("UPDATE I_DriversControl "	// no asset
					  + " SET I_IsImported='N', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=No Asset, ' "
					  + " WHERE A_Asset_ID IS NULL"
					  + " AND I_IsImported <> 'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		}
		else
		{
			sqlf = new StringBuffer ("UPDATE I_DriversControl "	// no flota
					  + " SET I_IsImported='N', I_ErrorMsg=Coalesce(I_ErrorMsg,n'')||'ERR=No Flota, ' "
					  + " WHERE C_ProjectOFB_ID IS NULL"
					  + " AND I_IsImported <> 'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			sqlf = new StringBuffer ("UPDATE I_DriversControl "	// no asset
					  + " SET I_IsImported='N', I_ErrorMsg=Coalesce(I_ErrorMsg,n'')||'ERR=No Asset, ' "
					  + " WHERE A_Asset_ID IS NULL"
					  + " AND I_IsImported <> 'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		}	
	    /*fin actualizar errores*/		
		commitEx();
		
		String ID_sqlUpdate = "0";
		int cant = 0;
		String sqlProd = "SELECT * FROM I_DriversControl WHERE Processed <> 'Y' " +
				" AND A_Asset_ID IS NOT NULL " +
				" AND I_IsImported='N' AND I_ErrorMsg IS NULL ORDER BY A_Asset_ID ASC";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sqlProd, this.get_TrxName());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				X_TP_DriversControl dControl = null;
				if(rs.getInt("A_Asset_ID") > 0 && rs.getInt("C_ProjectOFB_ID") > 0
						&& rs.getString("TypeMatrix") != null 
						&& rs.getString("TypeMatrix").trim() != "" 
							&& rs.getString("TypeMatrix").trim() != " ")
				{											
					dControl = new X_TP_DriversControl(getCtx(), 0, get_TrxName());
					//dControl.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
					//ininoles la organizacion siempre debe ser *
					dControl.setAD_Org_ID(0);
					dControl.setTypeMatrix(rs.getString("TypeMatrix"));
					dControl.setC_ProjectOFB_ID(rs.getInt("C_ProjectOFB_ID"));
					dControl.setA_Asset_ID(rs.getInt("A_Asset_ID"));
					if(rs.getInt("C_BPartner_ID") > 0)
						dControl.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
					if(rs.getInt("Supervisor_ID") > 0)
						dControl.setSupervisor_ID(rs.getInt("Supervisor_ID"));
					if(rs.getTimestamp("Date1") != null)
						dControl.setDate1(rs.getTimestamp("Date1"));
					if(rs.getInt("C_Period_ID") > 0)
						dControl.setC_Period_ID(rs.getInt("C_Period_ID"));
					if(rs.getTimestamp("StartTime") != null)
						dControl.setStartTime(rs.getTimestamp("StartTime"));
					if(rs.getTimestamp("EndTime") != null)
						dControl.setEndTime(rs.getTimestamp("EndTime"));
					if(rs.getTimestamp("WaitTime") != null)
						dControl.setWaitTime(rs.getTimestamp("WaitTime"));
					if(rs.getInt("SpeedCategory") > 0)
						dControl.setSpeedCategory(rs.getInt("SpeedCategory"));
					if(rs.getInt("MaxSpeed") > 0)
						dControl.setMaxSpeed(rs.getInt("MaxSpeed"));
					dControl.setAddress1(rs.getString("Address1"));
					if(rs.getBigDecimal("Latitude") != null)
						dControl.setLatitude(rs.getBigDecimal("Latitude"));
					if(rs.getBigDecimal("Longitude") != null)
						dControl.setLongitude(rs.getBigDecimal("Longitude"));
					if(rs.getString("CategoryType") != null && rs.getString("CategoryType").trim() != "")
					{
						String cType = DB.getSQLValueString(get_TrxName(), "SELECT MAX(value) FROM AD_Ref_List " +
								" WHERE AD_Reference_ID=1000090 AND upper(Description) like '"+rs.getString("CategoryType")+"'");
						if(cType != null && cType.trim() != "")
							dControl.setCategoryType(cType);
					}
					if(rs.getString("CategoryType2") != null && rs.getString("CategoryType2").trim() != "")
					{
						String cType2 = DB.getSQLValueString(get_TrxName(), "SELECT MAX(value) FROM AD_Ref_List " +
								" WHERE AD_Reference_ID = 1000089 AND upper(Description) like upper('"+rs.getString("CategoryType2")+"')");
						if(cType2 != null && cType2.trim() != "")
							dControl.setCategoryType2(cType2);
					}
					dControl.setStatus(rs.getString("Status"));
					dControl.save();
					ID_sqlUpdate = ID_sqlUpdate + ","+rs.getInt("I_DriversControl_ID");
					cant++;
				}	
			}
		}catch (Exception e)
		{
			log.severe(e.getMessage());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs=null;pstmt=null;
		}
		addLog (0, null, new BigDecimal (cant), "Registros Importados");
		DB.executeUpdate("UPDATE I_DriversControl SET Processed = 'Y',I_IsImported='Y' WHERE I_DriversControl_ID IN ("+ID_sqlUpdate+")", get_TrxName());
		
		return "Procesado ";
	}	//	doIt
}
