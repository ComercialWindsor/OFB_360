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
import java.sql.Timestamp;
import java.util.Calendar;
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
public class GenerateReplenishTSMView extends SvrProcess
{	
	/** Warehouse				*/
	private int		p_M_Warehouse_ID = 0;
	private int		p_BPartner_ID = 0;
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
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		String sqlDet = "SELECT M_Product_ID,C_BPartner_ID, qtytoorder,M_Warehouse_ID FROM RVOFB_ReplenishOFB " +
				" WHERE  AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+" ";
		if (p_BPartner_ID > 0)
			sqlDet = sqlDet + " AND C_BPartner_ID = "+p_BPartner_ID;
		if (p_M_Warehouse_ID > 0)
			sqlDet = sqlDet + " AND M_Warehouse_ID = "+p_M_Warehouse_ID;		
		sqlDet = sqlDet + " Order By M_Warehouse_ID Desc ";
		PreparedStatement pstmt = null;
		int cant = 0;
		try
		{
			pstmt = DB.prepareStatement (sqlDet, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();			
			int weareHouse_ID = 0;
			int LastWH = 0;
			MRequisition req = null;			
			while (rs.next ())
			{
				weareHouse_ID = rs.getInt("M_Warehouse_ID");
				if(weareHouse_ID != LastWH)
				{
					req = new MRequisition(getCtx(), 0, get_TrxName());
					req.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));					
					req.setC_DocType_ID(1000018);
					req.setAD_User_ID(Env.getAD_User_ID(getCtx()));
					req.setDescription("Reabastecimiento Automatico");
					req.setPriorityRule("5");
					Calendar calendar = Calendar.getInstance();
					Timestamp actual = new Timestamp(calendar.getTime().getTime());
					req.setDateRequired(actual);
					req.setDateDoc(actual);
					req.setM_Warehouse_ID(rs.getInt("M_Warehouse_ID"));
					req.setM_PriceList_ID(1000001);
					req.save();
					req.setDocStatus("IP");
					cant++;
				}				
				MRequisitionLine line = new MRequisitionLine(req);
				line.setM_Product_ID(rs.getInt("M_Product_ID"));
				if (rs.getInt("C_BPartner_ID") > 0)
					line.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
				line.setQty(rs.getBigDecimal("qtytoorder"));
				line.setPrice();
				line.save();
				LastWH = req.getM_Warehouse_ID();
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}		
		return "Se han generado "+cant+" Solicitudes";
	}	//	doIt
}	//	Replenish
