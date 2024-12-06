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
package org.geminis.process;

import java.util.Properties;

import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.process.SvrProcess;
 
/**
 *	Generate XML from Invoice
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ExportDTEInvoice.java,v 1.2 19/05/2011 $
 */
public class ProcessVoidOrder extends SvrProcess
{
	/** Properties						*/
	private Properties 		m_ctx;	
	private int p_C_Order_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_C_Order_ID=getRecord_ID();
		m_ctx = Env.getCtx();
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		MOrder order = new MOrder(m_ctx,p_C_Order_ID,get_TrxName());
		String msg = "";
		if (order.voidIt())
		{
			order.setDocStatus(MInvoice.DOCSTATUS_Voided);
			msg = "Orden Anulada";
			order.save();
		}		
		else
			msg = "No se pudo anular, revisar log para determinar causa";		
		return msg;
	}	//	doIt
	
}	//	InvoiceCreateInOut
