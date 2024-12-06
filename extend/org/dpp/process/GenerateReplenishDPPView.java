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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.MWarehouse;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;

/**
 *	Replenishment Report
 *	
 *  @author Jorg Janke
 *  @version $Id: ReplenishReport.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 *  
 *  Carlos Ruiz globalqss - integrate bug fixing from Chris Farley
 *    [ 1619517 ] Replenish report fails when no records in m_storage
 */
public class GenerateReplenishDPPView extends SvrProcess
{	
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
		int M_Requisition_ID = getRecord_ID();
		MRequisition req = new MRequisition(getCtx(), M_Requisition_ID, get_TrxName());
						
		MWarehouse wh = new MWarehouse(getCtx(), req.getM_Warehouse_ID(), get_TrxName());
		if (wh.get_ID() == 0)  
			throw new AdempiereSystemError("@FillMandatory@ @M_Warehouse_ID@");
				
		String sqlDet = "SELECT M_Product_ID,C_BPartner_ID, qtytoorder FROM RVOFB_ReplenishDPP " +
				" WHERE M_WareHouse_ID = "+req.getM_Warehouse_ID()+" AND AD_Client_ID = "+req.getAD_Client_ID();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sqlDet, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MRequisitionLine line = new MRequisitionLine(req);
				line.setM_Product_ID(rs.getInt("M_Product_ID"));
				line.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
				line.setQty(rs.getBigDecimal("qtytoorder"));
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
		return "Detalle Generado";
	}	//	doIt
}	//	Replenish
