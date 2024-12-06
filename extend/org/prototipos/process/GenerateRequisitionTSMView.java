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
package org.prototipos.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Replenishment Report
 *	
 *  @author Jorg Janke
 *  @version $Id: ReplenishReport.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 *  
 *  Carlos Ruiz globalqss - integrate bug fixing from Chris Farley
 *    [ 1619517 ] Replenish report fails when no records in m_storage
 */
public class GenerateRequisitionTSMView extends SvrProcess
{	
	/** Parametros */
	/** Organizacion					*/
	private int			p_ID_Org;	
	/**	Warehouse			*/
	
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
				p_ID_Org = para[i].getParameterAsInt();
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
		//obtenemos almacen por defecto
		int ID_WHouse =DB.getSQLValue(get_TrxName(), "SELECT MAX(M_WareHouse_ID) FROM M_WareHouse " +
				" WHERE AD_Client_ID = " + Env.getAD_Client_ID(getCtx())+ "AND isactive = 'Y' ");		
		if (ID_WHouse < 0)
		{
			ID_WHouse = 1000000;
		}
		//seteamos organizacion por defecto
		if (p_ID_Org < 1)
		{
			p_ID_Org =DB.getSQLValue(get_TrxName(), "SELECT MAX(AD_Org_ID) FROM AD_Org " +
					" WHERE AD_Client_ID = " + Env.getAD_Client_ID(getCtx())+ "AND isactive = 'Y' ");
		}
		//setamos PL de compra por defecto
		int ID_PriceL =DB.getSQLValue(get_TrxName(), "SELECT MAX(M_PriceList_ID) FROM M_PriceList " +
					" WHERE AD_Client_ID = " + Env.getAD_Client_ID(getCtx())+ "AND isactive = 'Y' " +
					" AND IsSOPriceList = 'N' AND Upper(NAME) not like '%CAMBIAR%'");
				
		//seteamos las cantidades a ordenar y a consumir
		int qtyToP = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM RVOFB_MaintanceTSMProcess" +
				" where bomqtyavailable(M_Product_ID,"+ID_WHouse+",0) < resourceqty");
		
		int qtyToC = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM RVOFB_MaintanceTSMProcess" +
		" where bomqtyavailable(M_Product_ID,"+ID_WHouse+",0) >= resourceqty"); 
		
		if (qtyToP > 0)
		{
			int id_docType = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_DocType_ID) FROM" +
					" C_DocType WHERE docbasetype like 'POR'");
			
			MRequisition req = new MRequisition(getCtx(), 0, get_TrxName());
			req.setAD_Org_ID(p_ID_Org);
			req.setC_DocType_ID(id_docType);
			req.setAD_User_ID(Env.getAD_User_ID(getCtx()));
			req.setDescription("Generado Automaticamente");
			req.setPriorityRule("5");
			req.setDateRequired(Env.getContextAsDate(getCtx(), "#Date"));
			req.setDateDoc(Env.getContextAsDate(getCtx(), "#Date"));
			req.setM_Warehouse_ID(ID_WHouse);
			req.setM_PriceList_ID(ID_PriceL);
			req.save();
			
			String sqlDet = "SELECT M_Product_ID, ResourceQty, MP_OT_Resource_ID FROM RVOFB_MaintanceTSMProcess " +
			" where bomqtyavailable(M_Product_ID,"+ID_WHouse+",0) < resourceqty";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement (sqlDet, get_TrxName());
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					MRequisitionLine line = new MRequisitionLine(req);
					line.setM_Product_ID(rs.getInt("M_Product_ID"));
					line.setQty(rs.getBigDecimal("ResourceQty"));
					line.set_CustomColumn("MP_OT_Resource_ID", rs.getInt("MP_OT_Resource_ID"));
					line.setPrice();
					line.save();
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;			
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}		
		if (qtyToC > 0)
		{
			int id_docType2 = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_DocType_ID) FROM" +
					" C_DocType WHERE docbasetype like 'RRC'");
			
			MRequisition req2 = new MRequisition(getCtx(), 0, get_TrxName());
			req2.setAD_Org_ID(p_ID_Org);
			req2.setC_DocType_ID(id_docType2);
			req2.setAD_User_ID(Env.getAD_User_ID(getCtx()));
			req2.setDescription("Generado Automaticamente");
			req2.setPriorityRule("5");
			req2.setDateRequired(Env.getContextAsDate(getCtx(), "#Date"));
			req2.setDateDoc(Env.getContextAsDate(getCtx(), "#Date"));
			req2.setM_Warehouse_ID(ID_WHouse);
			req2.setM_PriceList_ID(ID_PriceL);
			req2.save();
			
			String sqlDet2 = "SELECT M_Product_ID, ResourceQty, MP_OT_Resource_ID FROM RVOFB_MaintanceTSMProcess " +
			" where bomqtyavailable(M_Product_ID,"+ID_WHouse+",0) >= resourceqty";
			PreparedStatement pstmt2 = null;
			try
			{
				pstmt2 = DB.prepareStatement (sqlDet2, get_TrxName());
				ResultSet rs2 = pstmt2.executeQuery ();
				while (rs2.next ())
				{
					MRequisitionLine line = new MRequisitionLine(req2);
					line.setM_Product_ID(rs2.getInt("M_Product_ID"));
					line.setQty(rs2.getBigDecimal("ResourceQty"));
					line.set_CustomColumn("MP_OT_Resource_ID", rs2.getInt("MP_OT_Resource_ID"));
					line.setPrice();
					line.save();
				}
				rs2.close ();
				pstmt2.close ();
				pstmt2 = null;			
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}	
		return "Lineas de Solicitudes de Compra: "+qtyToP+". Lineas de Solicitudes de Materiales: "+qtyToC;
	}	//	doIt
}	//	Replenish
