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
package org.pdv.process;

import java.math.*;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import com.sun.corba.ee.org.apache.bcel.generic.NEWARRAY;

/**
 *	
 *	
 *  @author Italo Niñoles Ininoles
 *  @version $Id: ProcessPaymentRequest.java,v 1.2 2014/08/26 10:11:01  $
 */
public class UpdateAmtInvoicePaySchedule extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int p_Org_ID;
	private String P_DocAction;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Org_ID"))
				p_Org_ID = para[i].getParameterAsInt();
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
		int cantUpdated = 0;
		String sqlOrders = "SELECT cps.C_InvoicePaySchedule_ID " +
				" FROM C_InvoicePaySchedule cps" +
				" INNER JOIN C_Invoice ci ON (cps.C_Invoice_ID = ci.C_Invoice_ID)" +
				" WHERE ci.DocStatus IN ('CO','CL')" +
				//" and ci.issotrx = 'Y'" +
				" and cps.foreignprice is not null"+
				" and cps.foreignprice > 0" +
				" and cps.IsPaid <> 'Y'";
				//" and C_DocTypeTarget_ID IN (1000070)";
				
		
		if (p_Org_ID > 0 )
		{
			sqlOrders+=" AND co.AD_Org_ID = ? ";
		}
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sqlOrders, get_TrxName());
			if (p_Org_ID > 0 )
			{
				pstmt.setInt(1, p_Org_ID);
			}
			
			ResultSet rs = pstmt.executeQuery();		
			//ciclo de cuotas
			Timestamp p_DateDoc = new Timestamp(System.currentTimeMillis());
			//ininoles dejamos en 0 campos que no son fecha para que encuentre tasa de cambio
			p_DateDoc.setHours(0);
			p_DateDoc.setMinutes(0);
			p_DateDoc.setSeconds(0);
			p_DateDoc.setNanos(0);
				
			while (rs.next())
			{	
				MInvoicePaySchedule ps = new MInvoicePaySchedule(getCtx(), rs.getInt("C_InvoicePaySchedule_ID"), get_TrxName());
				MInvoice inv = new MInvoice(getCtx(), ps.getC_Invoice_ID(), get_TrxName());
				BigDecimal fprice = (BigDecimal)ps.get_Value("ForeignPrice");
				if(fprice.compareTo(Env.ZERO) > 0)					
				{		
					if(inv.getC_Currency().getISO_Code().equals("CLP"))
					{	
						BigDecimal amt = MConversionRate.convert(getCtx(), fprice, 1000000, 
									inv.getC_Currency_ID(),p_DateDoc, 114, inv.getAD_Client_ID(), inv.getAD_Org_ID());
							
						if(amt==null)
							return "No existe tasa de cambio para UF, no se puede completar la operacion";
						if(amt.compareTo(Env.ZERO) > 0)
						{
							try{
								String sqlUpdate = "UPDATE C_InvoicePaySchedule SET dueamt = "+amt+
										" WHERE C_InvoicePaySchedule_ID = "+ps.get_ID();
								DB.executeUpdate(sqlUpdate, get_TrxName());
								/*ps.setDueAmt(amt);
								ps.save();*/
							}
							catch (Exception e)
							{
								log.log(Level.SEVERE, e.getMessage(), e);
							}
						}
					}
				}
				cantUpdated++;
			}						
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}	
		return "Se Han actualizado "+cantUpdated+" Cuotas";
	}	//	doIt	
}	//	CopyOrder
