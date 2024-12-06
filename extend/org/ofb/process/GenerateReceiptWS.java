/******************************************************************************
0 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.ofb.process;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	Genera Movimientos de Inventario desde WS
 *	Process_ID = 1000445
 *  AD_Menu_ID=1000495
 *  @author fabian aguilar
 *  @version $Id: GenerateInvMoveWS.java,v 1.2 2014/09/12 $
 */
public class GenerateReceiptWS extends SvrProcess
{
	private Properties 		m_ctx;
	private String 		p_IMEI;
	private int p_WS =0;
	
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
			else if (name.equals("IMEI"))
				p_IMEI = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		m_ctx = Env.getCtx();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		/*********/

		
		String sql = "Select * from M_InOutLineTemp where PROCESSED='N'" ;
		//String sql = "Select * from M_InOutLineTemp where movementqty > 0" ;
		
		if (p_IMEI != null && p_IMEI != "" && p_IMEI != " ")
		{
			sql = sql + " AND IMEI = '"+p_IMEI+"'";
		}
		
		//sql = sql + " AND WS="+p_WS;
		
		MInOut minout = null;
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{	
				MOrderLine oLine = null;
				if(rs.getInt("C_ORDERLINE_ID")>0)
					oLine = new MOrderLine(getCtx(), rs.getInt("C_ORDERLINE_ID"), get_TrxName());				
				if(minout==null)
				{
					
					MOrder order = null;
					if(rs.getInt("C_ORDERLINE_ID")>0)
						order = new MOrder(getCtx(), oLine.getC_Order_ID(), get_TrxName());
					else if(rs.getInt("C_ORDER_ID")>0)
						order = new MOrder(getCtx(), rs.getInt("C_ORDER_ID"), get_TrxName());
					
					Calendar calendar = Calendar.getInstance();
					java.util.Date now = calendar.getTime();
					Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
					minout = new MInOut(order, 1000014, currentTimestamp);
					minout.setDescription("Desde WS");
					minout.setC_DocType_ID(1000014);
					minout.setC_Order_ID(order.get_ID());
					minout.setIsSOTrx(false);
					minout.save();
				}
				
				MInOutLine line = new MInOutLine(minout);
				//Se busca el locator ID
				int Locator_ID = DB.getSQLValue(get_TrxName(), "select MAX(M_locator_ID) " +
						"from M_locator where value like ?", rs.getString("LOCATORVALUE"));
				
				line.setM_Locator_ID(Locator_ID);
				if(rs.getInt("C_ORDERLINE_ID")>0)
				{
					//line.setM_Product_ID(oLine.getM_Product_ID());
					line.setProduct(MProduct.get(getCtx(), oLine.getM_Product_ID()));
				}
				else
				{
					int product_ID = DB.getSQLValue(get_TrxName(), "select M_Product_ID " +
							"from M_Product where value = ? and ad_client_id = ?", rs.getString("PRODUCTVALUE"), rs.getInt("AD_Client_ID"));
					//line.setM_Product_ID(product_ID);
					line.setProduct(MProduct.get(getCtx(), product_ID));
				}
				
				line.setQty(rs.getBigDecimal("MOVEMENTQTY"));
				line.setQtyEntered(rs.getBigDecimal("MOVEMENTQTY"));
				line.setMovementQty(rs.getBigDecimal("MOVEMENTQTY"));
				line.setC_OrderLine_ID(rs.getInt("C_ORDERLINE_ID"));
				line.set_ValueOfColumn("TEMPLINE_ID", rs.getInt("M_INOUTLINETEMP_ID"));
				line.setM_AttributeSetInstance_ID(-1);
				line.save();				
			}
			
			rs.close();
			pstmt.close();
			DB.close(rs, pstmt);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		if(minout!=null)
		{
			minout.processIt(X_M_InOut.DOCACTION_Complete);
			minout.save();
			
			DB.executeUpdate("Update M_INOUTLINETEMP set PROCESSED='Y' Where M_INOUTLINETEMP_ID "
					+ " IN (select TEMPLINE_ID from M_InOutLine where M_InOut_ID="+minout.get_ID()+") ", this.get_TrxName());
			
			return "Generado: "+ minout.getDocumentNo();
		}
		else
			return "No Generado";
	}	//	doIt
	
}	//	OrderOpen
