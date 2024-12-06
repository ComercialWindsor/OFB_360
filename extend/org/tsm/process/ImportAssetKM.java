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

import org.compiere.model.MAsset;
import org.compiere.model.X_MP_AssetMeter;
import org.compiere.model.X_MP_AssetMeter_Log;
import org.compiere.model.X_MP_Meter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class ImportAssetKM extends SvrProcess
{
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{	 
			//p_C_Invoice_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		String clientCheck = " AND AD_Client_ID=" + Env.getAD_Client_ID(getCtx());
		//actualizamos Asset
		StringBuffer sqlf = new StringBuffer ("UPDATE I_AssetMeter_Log aml "
				+ " SET A_Asset_ID = (SELECT MAX(A_Asset_ID) FROM A_Asset aa"
				+ " WHERE aa.IsActive = 'Y' AND aml.AssetValue = aa.Value AND aml.AD_Client_ID = aa.AD_Client_ID) "
				+ " WHERE aml.A_Asset_ID IS NULL AND aml.AssetValue IS NOT NULL"
				+ " AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());
		
		/*actualizar errores*/
		if (DB.isPostgreSQL())
		{
			sqlf = new StringBuffer ("UPDATE I_AssetMeter_Log "	// no asset
					  + " SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=No Asset, ' "
					  + " WHERE A_Asset_ID IS NULL"
					  + " AND I_IsImported <> 'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		}
		else
		{
			sqlf = new StringBuffer ("UPDATE I_AssetMeter_Log "	// no asset
					  + " SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,n'')||'ERR=No Asset, ' "
					  + " WHERE A_Asset_ID IS NULL"
					  + " AND I_IsImported <> 'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		}	
	    /*fin actualizar errores*/		
		
		commitEx();
		
		String ID_sqlUpdate = "0";
		int cant = 0;
		String sqlProd = "SELECT I_AssetMeter_Log_ID,AssetValue,DateTrx,MP_Meter_ID,A_Asset_ID,amt  FROM I_AssetMeter_Log WHERE Processed <> 'Y' " +
				" AND A_Asset_ID IS NOT NULL " +
				" AND I_IsImported='N' AND I_ErrorMsg IS NULL ORDER BY A_Asset_ID, amt ASC";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sqlProd, this.get_TrxName());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				if(rs.getInt("A_Asset_ID") > 0 && rs.getInt("MP_Meter_ID") > 0)
				{
					MAsset asset = new MAsset(getCtx(), rs.getInt("A_Asset_ID"), get_TrxName());
					X_MP_Meter met = new X_MP_Meter(getCtx(), rs.getInt("MP_Meter_ID"),get_TrxName());
						
					int ID_AMetter = DB.getSQLValue(get_TrxName(), " SELECT MP_AssetMeter_ID FROM MP_AssetMeter  " +
							" WHERE IsActive = 'Y' AND A_Asset_ID = "+asset.get_ID()+" AND MP_Meter_ID = "+met.get_ID());
						
					X_MP_AssetMeter aMeter = null;
					if (ID_AMetter > 0)
						aMeter = new X_MP_AssetMeter(getCtx(),ID_AMetter,get_TrxName());						
					else
					{
						aMeter = new X_MP_AssetMeter(getCtx(),0,get_TrxName());
						aMeter.setA_Asset_ID(asset.get_ID());
						aMeter.set_CustomColumn("MP_Meter_ID",met.get_ID());
						aMeter.setName(met.getName()+" Auto.");
						aMeter.save();
					}					
					X_MP_AssetMeter_Log aMetLog = new X_MP_AssetMeter_Log(getCtx(), 0,get_TrxName());
					aMetLog.setMP_AssetMeter_ID(aMeter.get_ID());
					aMetLog.setAD_Org_ID(aMeter.getAD_Org_ID());
					aMetLog.setDateTrx(rs.getTimestamp("DateTrx"));
					aMetLog.setAD_User_ID(Env.getAD_User_ID(getCtx()));
					aMetLog.setAmt(rs.getBigDecimal("Amt"));
					aMetLog.setcurrentamt(rs.getBigDecimal("Amt"));
					aMeter.setAmt(aMetLog.getcurrentamt());						
					aMeter.save();
					aMetLog.save();
					aMeter.setAmt(aMetLog.getcurrentamt());						
					aMeter.save();
					ID_sqlUpdate = ID_sqlUpdate + ","+rs.getInt("I_AssetMeter_Log_ID");
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
		DB.executeUpdate("UPDATE I_AssetMeter_Log SET Processed = 'Y',I_IsImported='Y' WHERE I_AssetMeter_Log_ID IN ("+ID_sqlUpdate+")", get_TrxName());
		
	   return "Procesado ";
	}	//	doIt
}
