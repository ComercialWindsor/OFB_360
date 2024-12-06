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
package org.tsm.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.X_MP_AssetMeter_Log;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	GenerateAssetMeterLog
 *	
 *  @author ininoles
 *  
 */
public class GenerateAssetMeterLog extends SvrProcess
{	
	/** Warehouse				*/
	private Timestamp	p_DateTrxFrom;
	private Timestamp	p_DateTrxTo;
	private int	p_Org_ID;
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
			else if (name.equals("DateTrx"))
			{
				p_DateTrxFrom = (Timestamp)para[i].getParameter();
				p_DateTrxTo = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("AD_Org_ID"))
			{
				p_Org_ID = para[i].getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		//ciclo de activos 
		int cantLine = 0;
		String sql = "SELECT a_asset_id FROM a_asset WHERE A_Asset_type_ID IN ('01') ";
		if(p_Org_ID > 0)
			sql = sql+" AND AD_Org_ID = "+p_Org_ID;
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());			
			rs = pstmt.executeQuery();		
			while (rs.next())
			{
				BigDecimal cant = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(ml.TP_InicialKM-ml.TP_FinalKM)" +
						" FROM M_Movement mm" +
						" INNER JOIN M_MovementLine ml ON (mm.M_Movement_ID = ml.M_Movement_ID)" +
						" WHERE mm.TP_Asset_Id = "+rs.getInt("a_asset_id")+
						" AND ml.TP_FinalHr > ? " +
						" AND ml.TP_InicialHr < ?", p_DateTrxFrom,p_DateTrxTo);
				if(cant == null)
					cant = Env.ZERO;
				if(cant.compareTo(Env.ZERO) > 0)
				{
					int IDAMeter = DB.getSQLValue(get_TrxName(), "SELECT MAX(MP_AssetMeter_ID) FROM MP_AssetMeter m " +
							" WHERE A_Asset_ID = "+rs.getInt("a_asset_id")+" AND MP_Meter_ID=1000001");
					
					BigDecimal lastOdo = DB.getSQLValueBD(get_TrxName(), "SELECT MAX(ml.AMT) FROM MP_AssetMeter_Log ml" +
							" INNER JOIN MP_AssetMeter m ON (ml.MP_AssetMeter_ID = m.MP_AssetMeter_ID)" +
							" WHERE ml.IsActive='Y' AND A_Asset_ID = "+rs.getInt("a_asset_id")+" AND MP_Meter_ID=1000001");
					if(lastOdo == null)
						lastOdo = Env.ZERO;
					cant = cant.add(lastOdo);
					//se crea registro nuevo
					X_MP_AssetMeter_Log aLog = new X_MP_AssetMeter_Log(getCtx(), 0, get_TrxName());
					aLog.setMP_AssetMeter_ID(IDAMeter);
					aLog.setAD_Org_ID(0);
					aLog.setDateTrx(p_DateTrxTo);
					aLog.setAD_User_ID(Env.getAD_User_ID(getCtx()));
					aLog.setAmt(cant);
					aLog.saveEx(get_TrxName());
					cantLine++;
				}
			}
		}		
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage()+" SQL:"+sql, e);
		}
		finally
		{
			rs.close ();pstmt.close ();
			pstmt = null;rs = null;
		}
		return "Se han generado "+cantLine+" registros";
	}	//	doIt
}	//	Replenish
