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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	proceso prototipo metlife
 *	
 *  @author ininoles
 *  @version $Id: GenerateFollowCampaign.java,v 1.2 2014/09/12 ininoles$
 */
public class DeleteInvoiceLineBaskakow extends SvrProcess
{
	private Properties 		m_ctx;	
	private int p_invoice_id = 0;
	private Timestamp p_date;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_invoice_id=getRecord_ID();
		m_ctx = Env.getCtx();
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("MovementDate"))
			{
				p_date = (Timestamp)para[i].getParameter();				
			}			
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{	
		MInvoice inv = new MInvoice(getCtx(), p_invoice_id, get_TrxName());
		MInvoiceLine[] lines = inv.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			if (line.getM_InOutLine_ID() > 0)
			{
				MInOutLine mLine = new MInOutLine(getCtx(), line.getM_InOutLine_ID(), get_TrxName());
				MInOut despacho = new MInOut(getCtx(), mLine.getM_InOut_ID(),get_TrxName());				
				if (despacho.getMovementDate().compareTo(p_date) >= 0)
				{
					line.delete(false);
					line.save();
				}
			}		
		}
		inv.calculateTaxTotal();
		inv.save();		
		
		return "Lineas Borradas";
	}	//	doIt
	
}	//	OrderOpen
