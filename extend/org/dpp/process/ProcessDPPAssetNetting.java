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
package org.dpp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MAsset;
import org.compiere.model.MDepreciationWorkfile;
/**
 *	Process Netting
 *	
 *  @author mfrojas
 *  @version $Id: ProcessDPPAssetNetting.java,v 1.0 2018/05/07 mfrojas $
 *  
 */
public class ProcessDPPAssetNetting extends SvrProcess
{
	/** Warehouse				*/
	private int		p_C_Year_ID = 0;
	/** Return Info				*/

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
			else if(name.equals("Year"))
				p_C_Year_ID = para[i].getParameterAsInt();
				
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
		/**
		 * Objetivo: Netear los bienes que llegaron a valor libro 1 en un tiempo determinado. 
		 */
		
		int Year = DB.getSQLValue(get_TrxName(), "SELECT cast(fiscalyear as int) from c_year where c_year_id = "+p_C_Year_ID);
		//Seleccionar todos lo sbienes a netear
		
		String sqlassets = "SELECT a_asset_id from a_depreciation_workfile where " +
				" Round(A_Asset_Cost) - Round(A_Accumulated_Depr) <= 2 and " +
				" ROUND(A_Asset_Cost) > 2 AND A_Asset_ID in " +
				" (SELECT A_Asset_ID from A_Asset where IsInPosession='Y' and isactive='Y' and AssetStatus='ALT')";
				
		PreparedStatement pstmt = null;
		
		log.info("p_C_Year_ID=" + p_C_Year_ID 
			+ ", Year=" + Year);
		int count = 0;
		BigDecimal amountdep = Env.ZERO;
	
		try
		{
			log.config("sqlassets "+sqlassets);
			pstmt = DB.prepareStatement (sqlassets, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			
			while (rs.next ())
			{
				int assetwf = DB.getSQLValue(get_TrxName(), "SELECT max(A_depreciation_workfile_id) from a_depreciation_workfile where a_Asset_id = "+rs.getInt("A_Asset_ID"));
				if(assetwf <= 0)
					continue;
				MDepreciationWorkfile wf = new MDepreciationWorkfile(getCtx(),  assetwf ,get_TrxName());
				wf.set_CustomColumn("AmtAcctBalance", wf.getA_Accumulated_Depr().setScale(0, BigDecimal.ROUND_HALF_UP));
				wf.setA_Asset_Cost(Env.ONE);
				wf.setA_Accumulated_Depr(Env.ZERO);
				wf.set_CustomColumn("ANNO", Year);
				
				wf.save();
				count++;
				amountdep = amountdep.add(wf.getA_Accumulated_Depr().setScale(0,BigDecimal.ROUND_HALF_UP));
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
			log.config("total de bienes "+count+". Monto total: "+amountdep);
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return "Detalle Generado. Total de bienes: "+count;
	}	//	doIt

	/**
	 * 	Prepare/Check Replenishment Table
	 */

}	//	Replenish
