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
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.X_MP_OT;
import org.compiere.model.X_MP_OT_Resource;
import org.compiere.model.X_MP_OT_Task;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	GenerateFromJobStandar
 *	
 */
public class GenerateOTFromOrder extends SvrProcess
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
		MOrderLine[] lines = order.getLines(true, null);	
		cantOT = 0;
		for (int i = 0; i < lines.length; i++)
		{
			MOrderLine line = lines[i];
			if(line.get_ValueAsInt("MP_JobStandar_ID") > 0)
				createOT(line, order.getDateOrdered(),line.get_ValueAsInt("MP_JobStandar_ID"));
			else
			{
				line.setDescription("No Existe Estandar de Trabajo asociado");
				line.save();
			}
		}	
		return "Se han generado "+cantOT+" Órdenes de Trabajo";
	}	//	doIt
	public boolean createOT(MOrderLine oLine,Timestamp Datetrx,int ID_SJob)
	{
		//creacion cabecera ot
		X_MP_OT newOT=new X_MP_OT(Env.getCtx(), 0,null);
		newOT.setDateTrx(Datetrx);
		newOT.setDescription("Generado Automaticamente");
		//newOT.setA_Asset_ID(Asset_ID);
		//newOT.setMP_Maintain_ID(MP_ID);
		newOT.set_CustomColumn("C_OrderLine_ID", oLine.get_ID());
		newOT.set_CustomColumn("C_Order_ID", oLine.getC_Order_ID());
		newOT.setDocStatus("DR");
		newOT.setDocAction("CO");
		newOT.setC_DocType_ID(MDocType.getOfDocBaseType(Env.getCtx(), "OTP")[0].getC_DocType_ID());
		if(!newOT.save())//creada nueva OT
			return false;
		if(!createOTTaskDetail(ID_SJob,newOT, oLine.get_ID()))
			return false;
		//actualizacion de id_ot en order line
		cantOT++;
		oLine.set_CustomColumn("MP_OT_ID", newOT.get_ID());
		oLine.save();
		return true;
	}
	public boolean createOTTaskDetail(int ID_JobS, X_MP_OT OT, int ID_OrderLine)
	{	
		//creación de tareas OT
		String sql = "select * from MP_JobStandar_Task where MP_JobStandar_ID = ? ";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,ID_JobS);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				X_MP_OT_Task ta = new X_MP_OT_Task(Env.getCtx(), 0,null);				
				ta.setAD_Org_ID(rs.getInt("AD_Org_ID"));
				ta.setMP_OT_ID(OT.getMP_OT_ID());
				ta.setDescription(rs.getString("Description"));
				ta.setDuration(rs.getBigDecimal("Duration"));
				ta.setC_UOM_ID(rs.getInt("C_UOM_ID"));
				ta.setStatus(X_MP_OT_Task.STATUS_NotStarted);
				ta.saveEx();				
				createOTResourceDetail(rs.getInt("MP_JobStandar_Task_ID"),ta.getMP_OT_Task_ID(), ID_OrderLine);
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
	public boolean createOTResourceDetail(int ID_TaskSJob, int ID_OTTask, int ID_OrderLine)
	{	
		//creación de recursos OT
		String sql = "select * from MP_JobStandar_Resource where MP_JobStandar_Task_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql,null);
			pstmt.setInt(1,ID_TaskSJob);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				X_MP_OT_Resource re=new X_MP_OT_Resource(Env.getCtx(), 0,null);
				re.setAD_Org_ID(rs.getInt("AD_Org_ID"));
				re.setMP_OT_Task_ID(ID_OTTask);
				re.setCostAmt(rs.getBigDecimal("CostAmt"));
				re.setS_Resource_ID(rs.getInt("S_Resource_ID"));
				re.setM_BOM_ID(rs.getInt("M_BOM_ID"));
				re.setResourceQty(rs.getBigDecimal("RESOURCEQTY"));
				re.setResourceType(rs.getString("RESOURCETYPE"));
				re.set_ValueOfColumn("M_Product_ID", rs.getInt("M_Product_ID"));
				re.saveEx();
				re.set_CustomColumn("C_Order_ID", p_Order_ID);
				re.set_CustomColumn("C_OrderLine_ID", ID_OrderLine);
				re.saveEx();
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
