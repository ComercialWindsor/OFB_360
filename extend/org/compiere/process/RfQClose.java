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
 *****************************************************************************/
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRfQ;
import org.compiere.model.MRfQLine;
import org.compiere.model.MRfQResponse;
import org.compiere.util.DB;
 

/**
 *	Close RfQ and Responses	
 *	
 *  @author Jorg Janke
 *  @version $Id: RfQClose.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 *  
 *  @author Teo Sarca, teo.sarca@gmail.com
 *  	<li>BF [ 2892585 ] When closing an RfQ we need to mark the responses as process
 *  		https://sourceforge.net/tracker/?func=detail&aid=2892585&group_id=176962&atid=879332
 */
public class RfQClose extends SvrProcess
{
	/**	RfQ 			*/
	private int		p_C_RfQ_ID = 0;

	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_C_RfQ_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@see org.compiere.process.SvrProcess#doIt()
	 *	@return message
	 */
	protected String doIt () throws Exception
	{
		MRfQ rfq = new MRfQ (getCtx(), p_C_RfQ_ID, get_TrxName());
		if (rfq.get_ID() == 0)
			throw new IllegalArgumentException("No RfQ found");
		log.info("doIt - " + rfq);
		//
		
		//faaguilar OFB begin
		
		if(rfq.get_ValueAsString("TypeRfQ").equals("A")){
			MRfQLine lines[] = rfq.getLines();
			for( MRfQLine line : lines )
			{
				
				if(line.get_ValueAsInt("C_BPartner_ID")==0 || line.get_ValueAsInt("C_BPartner_Location_ID")==0)
					throw new IllegalArgumentException("No BPartner or Location for Line :" + line.getLine());
			
			}
		}
		else{
			if(rfq.getC_BPartner_ID()==0 || rfq.getC_BPartner_Location_ID()==0)
				throw new IllegalArgumentException("No BPartner or Location ");
			
		}
			int count = 0;
			
			String sql="select C_RfQLine_ID from C_RfQLine where C_RfQ_ID=? order by C_BPartner_ID";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, p_C_RfQ_ID);
				ResultSet rs = pstmt.executeQuery();
				
				MOrder order = null;
				if(rfq.get_ValueAsString("TypeRfQ").equals("C")){
					MBPartner bp = MBPartner.get(getCtx(), rfq.getC_BPartner_ID());
					order = new MOrder (getCtx(), 0, get_TrxName());
					order.setIsSOTrx(false);
					order.setC_DocTypeTarget_ID();
					order.setBPartner(bp);
					order.setC_BPartner_Location_ID(rfq.getC_BPartner_Location_ID());
					order.setSalesRep_ID(rfq.getSalesRep_ID());
					order.saveEx();
					
					count++;
				}
				
				while(rs.next())
				{
					MRfQLine line = new MRfQLine(getCtx(), rs.getInt(1), get_TrxName());
					
					if(rfq.get_ValueAsString("TypeRfQ").equals("A")){
						MBPartner bp = MBPartner.get(getCtx(), line.get_ValueAsInt("C_BPartner_ID"));
						order = new MOrder (getCtx(), 0, get_TrxName());
						order.setIsSOTrx(false);
						order.setC_DocTypeTarget_ID();
						order.setBPartner(bp);
						order.setC_BPartner_Location_ID(line.get_ValueAsInt("C_BPartner_Location_ID"));
						order.setSalesRep_ID(rfq.getSalesRep_ID());
						order.saveEx();
						
						count++;
					}
					
					MOrderLine ol = new MOrderLine (order);
					ol.setM_Product_ID(line.getM_Product_ID(), 
						line.getM_Product().getC_UOM_ID());
					ol.setDescription(line.getDescription());
					ol.setQty((BigDecimal)line.get_Value("Qty"));
					BigDecimal price = (BigDecimal)line.get_Value("PriceActual");
					ol.setPrice(price);
					ol.setDescription(line.getDescription());
					ol.saveEx();
				
				} 
				
				rs.close();
				pstmt.close();
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
				
			}
	
		//faaguilar OFB end
		
		rfq.setProcessed(true);
		rfq.saveEx();
		
		//
		int counter = 0;
		MRfQResponse[] responses = rfq.getResponses (false, false);
		for (int i = 0; i < responses.length; i++)
		{
			responses[i].setProcessed(true);
			responses[i].save();
			counter++;
		}
		//
		return "Generated "+ count;
	}	//	doIt
	
}	//	RfQClose
