/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Contributor(s): Chris Farley - northernbrewer                              *
 *****************************************************************************/
package org.blumos.process;


//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Timestamp;

//import org.blumos.model.BlumosUtilities;
//import org.compiere.model.MConversionRate;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
//import org.ofb.utils.DateUtils;

/**
 *  @author Italo Niñoles
 */
public class CurrencyCopy extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	//private int 			Record_ID;
	
	protected void prepare()
	{
		//Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		/*Timestamp la_fecha = DateUtils.today();
		Timestamp p_inicio = DateUtils.SumarDias(la_fecha, -7);
		
		String msg = "Monedas copiadas desde "+BlumosUtilities.formatDate(p_inicio, true)+
		" hasta "+BlumosUtilities.formatDate(la_fecha, true);
		
		String sql = "SELECT AD_CLIENT_ID FROM AD_CLIENT WHERE AD_CLIENT_ID > 1000000";
		log.config("sql "+sql);
		PreparedStatement pstmt = DB.prepareStatement (sql, get_TrxName());
		ResultSet rs = pstmt.executeQuery ();	
		while (rs.next())
		{
			//borrado de tasas
			String sqlDelete = "DELETE FROM C_CONVERSION_RATE where ad_client_id="+rs.getInt("AD_Client_ID")+" and validfrom >= ?";
			PreparedStatement pstmtD = DB.prepareStatement (sqlDelete, null);
			pstmtD.setTimestamp(1, p_inicio);
			pstmtD.execute();
			//commitEx();
			String ID_A_ActuaLizar = "0";
			String sqlCConver = "select C_CURRENCY_ID,C_CURRENCY_ID_TO,VALIDFROM,VALIDTO,MULTIPLYRATE,DIVIDERATE,C_CONVERSIONTYPE_ID " +
					" FROM C_CONVERSION_RATE where ad_client_id=1000000 and validfrom >= ?";
			
			PreparedStatement pstmtC = DB.prepareStatement (sqlCConver, null);
			pstmtC.setTimestamp(1, p_inicio);
			ResultSet rsC = pstmtC.executeQuery ();	
			while (rsC.next())
			{
				MConversionRate cRate = new MConversionRate(getCtx(), 0, get_TrxName());
				cRate.setAD_Org_ID(0);
				cRate.setC_Currency_ID(rsC.getInt("C_Currency_ID"));
				cRate.setC_Currency_ID_To(rsC.getInt("C_Currency_ID_To"));
				cRate.setValidFrom(rsC.getTimestamp("ValidFrom"));
				cRate.setValidTo(rsC.getTimestamp("ValidTo"));
				cRate.setMultiplyRate(rsC.getBigDecimal("MultiplyRate"));
				cRate.setDivideRate(rsC.getBigDecimal("DivideRate"));
				cRate.setC_ConversionType_ID(rsC.getInt("C_ConversionType_ID"));
				cRate.saveEx(get_TrxName());
				ID_A_ActuaLizar = ID_A_ActuaLizar+","+cRate.get_ID();
				//se cambia id de cliente
				//DB.executeUpdate("UPDATE C_CONVERSION_RATE SET AD_Client_ID = "+rs.getInt("AD_Client_ID")+" WHERE C_CONVERSION_RATE_ID = "+cRate.get_ID(), get_TrxName());
				//commitEx();
			}
			pstmtC.close(); pstmtC = null;
			rsC.close(); rsC = null;
			DB.executeUpdate("UPDATE C_CONVERSION_RATE SET AD_Client_ID = "+rs.getInt("AD_Client_ID")+" WHERE C_CONVERSION_RATE_ID IN ("+ID_A_ActuaLizar+")", get_TrxName());
			commitEx();
		}
		pstmt.close(); pstmt = null;
		rs.close(); rs = null;*/
		String sendMail = "SELECT p_bl_copia_monedas("+Env.getAD_Client_ID(getCtx())+")";
		String result = DB.getSQLValueString(get_TrxName(), sendMail);
		return result;
	}	//	doIt
}	//	Replenish

