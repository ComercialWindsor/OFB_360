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

import org.compiere.model.X_TP_CommissionDetailTSM;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
//import org.compiere.util.Env;

/**
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ImportCommission.java $
 */
public class ImportCommission extends SvrProcess
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
		
	    //String clientCheck = " AND AD_Client_ID=" + Env.getAD_Client_ID(getCtx());
		
		//actualizamos comision
		/*StringBuffer sqlf = new StringBuffer ("UPDATE I_Commission icom "
				+ " SET TP_CommissionTSM_ID = (SELECT MAX(TP_CommissionTSM_ID) FROM TP_CommissionTSM tpcom"
				+ " WHERE icom.C_Period_ID = tpcom.C_Period_ID AND icom.AD_Org_ID = tpcom.AD_Org_ID) "
				+ " WHERE icom.TP_CommissionTSM_ID IS NULL AND I_IsImported <> 'Y'").append (clientCheck);
		DB.executeUpdate(sqlf.toString(), get_TrxName());*/
		
		/*actualizar errores*/		
		/*if (DB.isPostgreSQL())
		{	
			sqlf = new StringBuffer ("UPDATE I_Commission "	// no Commission
					  + " SET I_IsImported='N', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=No Header Commision, ' "
					  + " WHERE TP_CommissionTSM_ID IS NULL"
					  + " AND I_IsImported <> 'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		}
		else
		{				
			sqlf = new StringBuffer ("UPDATE I_Commission "	// no Commission
					  + " SET I_IsImported='N', I_ErrorMsg=Coalesce(I_ErrorMsg,n'')||'ERR=No Header Commision, ' "
					  + " WHERE TP_CommissionTSM_ID IS NULL"
					  + " AND I_IsImported <> 'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		}
	    /*fin actualizar errores*/		
		//commitEx();
		
		String ID_sqlUpdate = "0";
		int cant = 0;
		String sqlProd = "SELECT * FROM I_Commission WHERE Processed <> 'Y' " +
				" AND C_BPartner_ID IS NOT NULL AND TP_CommissionTSM_ID IS NOT NULL " +
				" AND I_IsImported='N' AND I_ErrorMsg IS NULL ORDER BY I_Commission_ID ASC";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sqlProd, this.get_TrxName());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				if(rs.getInt("TP_CommissionTSM_ID") > 0 )
				{											
					X_TP_CommissionDetailTSM comDet = new X_TP_CommissionDetailTSM(getCtx(), 0, get_TrxName());
					comDet.setTP_CommissionTSM_ID(rs.getInt("TP_CommissionTSM_ID"));
					comDet.setAD_Org_ID(rs.getInt("AD_Org_ID"));
					comDet.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
					comDet.setCommissionAmt(rs.getBigDecimal("CommissionAmt"));					
					comDet.save();
					ID_sqlUpdate = ID_sqlUpdate + ","+rs.getInt("I_Commission_ID");
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
		DB.executeUpdate("UPDATE I_Commission SET Processed = 'Y',I_IsImported='Y' WHERE I_Commission_ID IN ("+ID_sqlUpdate+")", get_TrxName());
		
		return "Procesado ";
	}	//	doIt
}
