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
package org.windsor.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.X_MP_OT_ResourceOrder;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	GenerateFromJobStandar
 *	
 */
public class GenerateResourceOrder extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_Order_ID;
	private int cantOT;
		
	protected void prepare()
	{	
		p_Order_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		MOrder order = new MOrder(getCtx(), p_Order_ID, get_TrxName());
		MOrderLine[] lines = order.getLines(false, null);	
		cantOT = 0;
		for (int i = 0; i < lines.length; i++)
		{
			MOrderLine line = lines[i];
			log.config("ID de linea: "+line.get_ID());
			if(line.get_ValueAsInt("MP_JobStandar_ID") > 0)
				createNVResourceDetail(line, line.get_ValueAsInt("MP_JobStandar_ID"));
			else
			{
				line.setDescription("No Existe Estandar de Trabajo asociado");
				line.save();
			}
		}	
		return "Se han generado "+cantOT+" lineas de recursos";
	}	//	doIt
	public boolean createNVResourceDetail(MOrderLine oLine, int ID_JobStandar)
	{	
		//creación de recursos OT
		log.config("ID linea antes de sql: "+oLine.get_ID());
		String sql = "SELECT jsr.AD_Org_ID,jsr.CostAmt,jsr.S_Resource_ID,jsr.M_BOM_ID,jsr.RESOURCEQTY,jsr.RESOURCETYPE, " +
				" jsr.M_Product_ID,jsr.MP_JobStandar_Task_ID,jsr.MP_JobStandar_Task_ID " +
				" FROM MP_JobStandar_Task jst " +
				" INNER JOIN MP_JobStandar_Resource jsr ON (jst.MP_JobStandar_Task_ID = jsr.MP_JobStandar_Task_ID) " +
				" WHERE MP_JobStandar_ID = ?"; 
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,ID_JobStandar);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				X_MP_OT_ResourceOrder re=new X_MP_OT_ResourceOrder(Env.getCtx(), 0,null);
				re.setAD_Org_ID(rs.getInt("AD_Org_ID"));
				re.setMP_OT_Task_ID(rs.getInt("MP_JobStandar_Task_ID"));
				re.setCostAmt(rs.getBigDecimal("CostAmt"));
				re.setS_Resource_ID(rs.getInt("S_Resource_ID"));
				re.setM_BOM_ID(rs.getInt("M_BOM_ID"));
				re.setResourceQty(rs.getBigDecimal("RESOURCEQTY").multiply(oLine.getQtyEntered()));
				re.setResourceType(rs.getString("RESOURCETYPE"));
				re.set_ValueOfColumn("M_Product_ID", rs.getInt("M_Product_ID"));
				//re.saveEx();
				re.set_CustomColumn("C_Order_ID", p_Order_ID);
				log.config("ID Linea dentro de metodo:"+oLine.get_ID());
				re.set_CustomColumn("C_OrderLine_ID", oLine.get_ID());
				re.saveEx();
				cantOT++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		return true;		
	}
}	//	Replenish
