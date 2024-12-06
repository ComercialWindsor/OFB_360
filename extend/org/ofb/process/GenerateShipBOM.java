/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;
import org.eevolution.model.MPPProductBOM;
import org.eevolution.model.MPPProductBOMLine;

import java.sql.Timestamp;
import java.util.logging.*;

/**
 *	
 *	
 *  @author Italo Niñoles i.ninoles
 *  @version $Id: ProcessPaymentRequest.java,v 1.2 2011/06/12 00:51:01  $
 */

public class GenerateShipBOM extends SvrProcess 
{
	
	private int 			Order_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Order_ID"))
				Order_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	
	protected String doIt() throws Exception
	{
		MOrder order = new MOrder (Env.getCtx(), Order_ID ,get_TrxName());
		
		MInOut out = new MInOut (order, 0, new Timestamp(TimeUtil.getToday().getTimeInMillis()));
		out.save();
		
		MOrderLine[] lines = order.getLines();
		for (int i=0; i< lines.length; i++)
		{
			if(lines[i].getM_Product_ID()>0)
				if(lines[i].getM_Product().isBOM())
				{
					MPPProductBOM bom = MPPProductBOM.getDefault(MProduct.get(getCtx(), lines[i].getM_Product_ID()), get_TrxName());
					MPPProductBOMLine[] bomLines= bom.getLines();
					for(int x=0;x<bomLines.length;x++)
					{
						MInOutLine line =  new MInOutLine(out);
						line.setC_OrderLine_ID(lines[i].getC_OrderLine_ID());
						MProduct product = MProduct.get(getCtx(), bomLines[x].getM_Product_ID());
						line.setProduct(product);
						line.setQty(bomLines[x].getQtyBOM().multiply(lines[i].getQtyOrdered()));
						line.setM_Locator_ID(MLocator.getDefault(MWarehouse.get(getCtx(), order.getM_Warehouse_ID())).getM_Locator_ID());
						line.save();
						
					}
				}
				else
				{
					MInOutLine line =  new MInOutLine(out);
					line.setOrderLine(lines[i], MLocator.getDefault(MWarehouse.get(getCtx(), order.getM_Warehouse_ID())).getM_Locator_ID(), Env.ZERO);
					line.setQty(lines[i].getQtyOrdered());
					line.save();
				}
			
		}
		

		return "Despacho Generado "+ out.getDocumentNo();
	}	
}
