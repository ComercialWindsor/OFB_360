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
import java.math.BigDecimal;
import java.util.logging.*;

/**
 *	
 *	
 *  @author Fabian Aguilar
 *  @version $Id: ChangeOrderCurrency.java  $
 */

public class ChangeOrderCurrency extends SvrProcess 
{
	
	private int 			Currency_ID;
	
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Currency_ID"))
				Currency_ID = para[i].getParameterAsInt();
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
		MOrder order = new MOrder (Env.getCtx(), getRecord_ID() ,get_TrxName());
		if(order.getC_Currency_ID()==Currency_ID)
			return "Ya tiene esta Moneda";

		MOrderLine[] lines = order.getLines();
		for (MOrderLine line:lines)
		{
			BigDecimal amt = MConversionRate.convert(getCtx(),line.getPriceActual(), order.getC_Currency_ID(), Currency_ID,
					order.getDateOrdered(),114, order.getAD_Client_ID(), order.getAD_Org_ID());
			
			//ininoles se agrega calculo de precio de lista para que no se descuenten 2 veces los descuentos
			BigDecimal amtPriceList = MConversionRate.convert(getCtx(),line.getPriceList(), order.getC_Currency_ID(), Currency_ID,
					order.getDateOrdered(),114, order.getAD_Client_ID(), order.getAD_Org_ID());
			
			if(amt==null)
				return "No existe tasa de cambio, no se puede completar la operacion";
			
			line.setPriceList(amtPriceList);
			line.setPrice(amt);
			line.save();
		}
		
		order.setC_Currency_ID(Currency_ID);
		order.save();

		return "Moneda Actualizada";
	}	
}
