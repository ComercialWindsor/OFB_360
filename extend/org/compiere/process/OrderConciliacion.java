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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: OrderConciliacion.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class OrderConciliacion extends SvrProcess
{
	int order_ID=0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		
		order_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		MOrder order = new MOrder(getCtx(), order_ID, this.get_TrxName());
		order.processIt(X_C_Order.DOCACTION_Re_Activate);
		
		MOrderLine[] lines=order.getLines();
		for(MOrderLine line:lines){
			line.setQty(line.getQtyDelivered());
			line.save();
			
		}
		order.addDescription("Saldada");
		order.processIt(X_C_Order.DOCACTION_Complete);
		order.save();
		
		return("Orden Saldada");
				
	   
	}	//	doIt


	
}	//	CopyOrder
