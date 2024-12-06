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

import org.compiere.model.X_OFB_Ssomac;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ImportSsomac.java $
 */
public class ImportSsomac extends SvrProcess
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
		StringBuffer sqlf = new StringBuffer ("UPDATE I_Ssomac dc "
				+ " SET C_ProjectOFB_ID = (SELECT MAX(C_ProjectOFB_ID) FROM C_ProjectOFB aa"
				+ " WHERE upper(dc.I_FlotaName) = upper(aa.Value) AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.C_ProjectOFB_ID IS NULL AND dc.I_FlotaName IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		//actualizamos Org
		sqlf = new StringBuffer ("UPDATE I_Ssomac dc "
				+ " SET AD_Org_ID = (SELECT MAX(AD_Org_ID) FROM AD_Org aa"
				+ " WHERE upper(dc.I_OrgName) = upper(aa.name) AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.AD_Org_ID IS NULL AND dc.I_OrgName IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		//actualizamos Supervisor
		sqlf = new StringBuffer ("UPDATE I_Ssomac dc "
				+ " SET Supervisor_ID = (SELECT MAX(AD_User_ID) FROM AD_User aa"
				+ " WHERE upper(dc.I_Supervisor) = upper(aa.description) AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.Supervisor_ID IS NULL AND dc.I_Supervisor IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		//actualizamos gerente
		sqlf = new StringBuffer ("UPDATE I_Ssomac dc "
				+ " SET AD_User_ID = (SELECT MAX(AD_User_ID) FROM AD_User aa"
				+ " WHERE upper(dc.I_Gerente) = upper(aa.description) AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.AD_User_ID IS NULL AND dc.I_Gerente IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		//actualizamos trabajador
		/*sqlf = new StringBuffer ("UPDATE I_Ssomac dc "
				+ " SET C_BPartner_ID = (SELECT MAX(C_BPartner_ID) FROM C_BPartner aa"
				+ " WHERE upper(dc.I_BPartnerName) = upper(aa.name) AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.C_BPartner_ID IS NULL AND dc.I_BPartnerName IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());*/
		
		sqlf = new StringBuffer ("UPDATE I_Ssomac dc "
				+ " SET C_BPartner_ID = (SELECT MAX(C_BPartner_ID) FROM C_BPartner aa"
				+ " WHERE upper(dc.I_BPartnerName) like upper(aa.name) AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.C_BPartner_ID IS NULL AND dc.I_BPartnerName IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		//actualizamos activo
		sqlf = new StringBuffer ("UPDATE I_Ssomac dc "
				+ " SET A_Asset_ID = (SELECT MAX(A_Asset_ID) FROM A_Asset aa"
				+ " WHERE dc.I_TractoValue = aa.Value AND dc.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE dc.A_Asset_ID IS NULL AND dc.I_TractoValue IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		
		/*actualizar errores*/
		if (DB.isPostgreSQL())
		{	
			sqlf = new StringBuffer ("UPDATE I_Ssomac "	// no asset
					  + " SET I_IsImported='N', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=No Asset, ' "
					  + " WHERE A_Asset_ID IS NULL"
					  + " AND I_IsImported <> 'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		}
		else
		{				
			sqlf = new StringBuffer ("UPDATE I_Ssomac "	// no asset
					  + " SET I_IsImported='N', I_ErrorMsg=Coalesce(I_ErrorMsg,n'')||'ERR=No Asset, ' "
					  + " WHERE A_Asset_ID IS NULL"
					  + " AND I_IsImported <> 'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		}	
	    /*fin actualizar errores*/		
		commitEx();
		
		String ID_sqlUpdate = "0";
		int cant = 0;
		String sqlProd = "SELECT * FROM I_Ssomac WHERE Processed <> 'Y' " +
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
				if(rs.getInt("A_Asset_ID") > 0 )
				{											
					X_OFB_Ssomac somac = new X_OFB_Ssomac(getCtx(), 0, get_TrxName());
					if(rs.getTimestamp("DeviceEntryTime") != null)
						somac.set_CustomColumn("DeviceEntryTime",rs.getTimestamp("DeviceEntryTime"));
					if(rs.getTimestamp("ServerReceiveTime") != null)
						somac.set_CustomColumn("ServerReceiveTime",rs.getTimestamp("ServerReceiveTime"));
					somac.set_CustomColumn("FormName", rs.getString("FormName"));
					somac.set_CustomColumn("UserName", rs.getString("UserName"));
					if(rs.getTimestamp("Date1") != null)
						somac.setDate1(rs.getTimestamp("Date1"));
					somac.set_CustomColumn("C_ProjectOFB_ID",rs.getInt("C_ProjectOFB_ID"));					
					somac.setAD_Org_ID(rs.getInt("AD_Org_ID"));
					if(rs.getInt("Supervisor_ID") > 0)
						somac.setSupervisor_ID(rs.getInt("Supervisor_ID"));
					if(rs.getInt("AD_User_ID") > 0)
						somac.setAD_User_ID(rs.getInt("AD_User_ID"));
					if(rs.getInt("C_BPartner_ID") > 0)
						somac.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
					somac.set_CustomColumn("Comments", rs.getString("Comments"));
					somac.setA_Asset_ID(rs.getInt("A_Asset_ID"));
					somac.setDescription(rs.getString("Description"));
					if(rs.getString("I_Type") != null && rs.getString("I_Type").trim() != "")
					{
						String cType = DB.getSQLValueString(get_TrxName(), "SELECT MAX(value) FROM AD_Ref_List " +
								" WHERE AD_Reference_ID=1000083 AND upper(Description) like upper('"+rs.getString("I_Type")+"')");
						if(cType != null && cType.trim() != "")
							somac.setType(cType);
					}
					somac.save();
					ID_sqlUpdate = ID_sqlUpdate + ","+rs.getInt("I_Ssomac_ID");
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
		DB.executeUpdate("UPDATE I_Ssomac SET Processed = 'Y',I_IsImported='Y' WHERE I_Ssomac_ID IN ("+ID_sqlUpdate+")", get_TrxName());
		
		return "Procesado ";
	}	//	doIt
}
