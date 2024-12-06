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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.X_MP_OT;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.ofb.utils.DateUtils;

/**
 *	Replenishment Report
 *	
 *  @author Jorg Janke
 *  @version $Id: ReplenishReport.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 *  
 *  Carlos Ruiz globalqss - integrate bug fixing from Chris Farley
 *    [ 1619517 ] Replenish report fails when no records in m_storage
 */
public class GenerateRequisitionFromOT extends SvrProcess
{	
	/** Warehouse				*/
	private int		p_M_Warehouse_ID = 0;
	private int		p_BPartner_ID = 0;
	private int 	p_OT_ID = 0;
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
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BPartner_ID"))
				p_BPartner_ID = para[i].getParameterAsInt();	
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_OT_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{			
		X_MP_OT ot = new X_MP_OT(getCtx(), p_OT_ID, get_TrxName());
		String sqlDet = "select *  from rvofb_detailottorequisition where MP_OT_ID=" +p_OT_ID;
		PreparedStatement pstmt = null;
		int ind = DB.getSQLValue(get_TrxName(), "select COUNT(1) from rvofb_detailottorequisition where MP_OT_ID="+p_OT_ID);
		int cant = 0;
		MRequisition req = null;		
		if(ind > 0)
		{	
			try
			{
				pstmt = DB.prepareStatement (sqlDet, get_TrxName());
				ResultSet rs = pstmt.executeQuery ();						
				req = new MRequisition(getCtx(), 0, get_TrxName());
				req.setAD_Org_ID(ot.getAD_Org_ID()==0?Env.getAD_Org_ID(getCtx()):ot.getAD_Org_ID());					
				req.setC_DocType_ID(1000018);
				req.setAD_User_ID(Env.getAD_User_ID(getCtx()));
				req.setDescription("Generado desde OT "+ot.getDocumentNo());
				req.setPriorityRule("5");			
				req.setDateRequired(DateUtils.now());
				req.setDateDoc(DateUtils.now());
				req.setM_Warehouse_ID(p_M_Warehouse_ID);
				req.setM_PriceList_ID(1000001);
				if(ot.get_ValueAsInt("A_Asset_ID")> 0)
					req.set_CustomColumn("A_Asset_ID", ot.get_ValueAsInt("A_Asset_ID"));
				req.save();
				while (rs.next ())
				{				
					MRequisitionLine line = new MRequisitionLine(req);
					line.setM_Product_ID(rs.getInt("M_Product_ID"));
					if(p_BPartner_ID > 0)
						line.setC_BPartner_ID(p_BPartner_ID);
					line.setQty(rs.getBigDecimal("resourceqty"));
					if(rs.getString("description") != null)
						line.setDescription(rs.getString("description"));
					line.setPrice();
					line.save();
					cant++;
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
				if(req != null)
					DB.executeUpdate("UPDATE MP_OT SET M_Requisition_ID = "+req.get_ID()+" WHERE MP_OT_ID = "+ot.get_ID(), get_TrxName());
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}		
		}
		if(req != null)
			return "Se han generado la solicitud "+req.getDocumentNo()+" con "+cant+" lineas";
		else
			return "No se ha podido generar la solicitud";
	}	//	doIt
}	//	Replenish
