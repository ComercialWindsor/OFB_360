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
package org.pdv.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.MPayment;
import org.compiere.model.X_C_PaymentRequestLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Import Order from I_Order
 *  @author Oscar Gomez
 * 			<li>BF [ 2936629 ] Error when creating bpartner in the importation order
 * 			<li>https://sourceforge.net/tracker/?func=detail&aid=2936629&group_id=176962&atid=879332
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportOrder.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class ImportInvoicePayScheduleXML extends SvrProcess
{
	/**	Client to be imported to		*/
	
	private String p_pgo_oc = "";
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
			else if (name.equals("pgo_oc"))
				p_pgo_oc = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{		
		String sql = "Select * from I_InvoicePayScheduleXML where PROCESSED='N' AND pgo_oc='"+p_pgo_oc+"'";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//int rImported=0;
		//int rfail =0;
		String statusPay = "false";
		try
		{
			pstmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE, get_TrxName());
			rs = pstmt.executeQuery ();
			String lastPgoOC = "";
			//ininoles nueva forma de calcular el monto del  pago;
			//BigDecimal payAmt = DB.getSQLValueBD(get_TrxName(),"SELECT SUM(monto_cuota) FROM I_InvoicePayScheduleXML where PROCESSED='N' AND pgo_oc='"+p_pgo_oc+"'"); 
			BigDecimal payAmt = Env.ZERO;
			BigDecimal payTemp = Env.ZERO;
			MPayment payment = null;
			MAllocationHdr alloc = null;
			
			while (rs.next())
			{
				int found = DB.getSQLValue(get_TrxName(), "select count(1) from C_InvoicePaySchedule where C_InvoicePaySchedule_ID=?", rs.getInt("id_cuota"));
				if(found<=0)
					continue;
				
				payTemp = rs.getBigDecimal("monto_cuota");
				payAmt = payAmt.add(payTemp);
				MInvoicePaySchedule cuota = new MInvoicePaySchedule(Env.getCtx(), rs.getInt("id_cuota"), get_TrxName());
				if(!lastPgoOC.equals(rs.getString("pgo_oc")))
				{
					lastPgoOC = rs.getString("pgo_oc");
					payment =  new MPayment (Env.getCtx(), 0, get_TrxName());
					//traemos org desde la cuota
					//payment.setAD_Org_ID(rs.getInt("AD_Org_ID"));
					payment.setAD_Org_ID(cuota.getAD_Org_ID());
					payment.setTenderType(rs.getString("tipo_pago"));
					payment.setC_BankAccount_ID(DB.getSQLValue(get_TrxName(), "select min(c_bankaccount_id) from c_bankaccount where ad_client_id=? and isactive = 'Y' and  AD_Org_ID = "+payment.getAD_Org_ID(),payment.getAD_Client_ID()));
					payment.setCreditCardNumber(rs.getString("n_tarjeta"));
					payment.setDateAcct(new Timestamp(System.currentTimeMillis()));
					payment.setDateTrx(new Timestamp(System.currentTimeMillis()));
					payment.setC_BPartner_ID(cuota.getC_Invoice().getC_BPartner_ID());
					//payment.setC_Invoice_ID(cuota.getC_Invoice_ID());
					payment.setIsReceipt(true);
					payment.setC_Currency_ID(228);
					payment.setPayAmt(payAmt);
					if(payment.save())
					{
						statusPay = "true";
					}
					else
					{
						statusPay = "false";
						return statusPay;
					}
					//cabecera asignacion
					alloc = new MAllocationHdr (Env.getCtx(), true,	//	manual
							payment.getDateTrx(), payment.getC_Currency_ID(), "WebPay", get_TrxName());
					alloc.setAD_Org_ID(payment.getAD_Org_ID());
					if (!alloc.save())
					{
						log.log(Level.SEVERE, "Allocation not created");
						statusPay = "false";
						return statusPay;
					}					
				}
				
				MAllocationLine aLine = new MAllocationLine (alloc, rs.getBigDecimal("monto_cuota"), 
							Env.ZERO, Env.ZERO, Env.ZERO);
				aLine.setC_Invoice_ID(cuota.getC_Invoice_ID());
				aLine.setC_Payment_ID(payment.getC_Payment_ID());
				aLine.set_CustomColumn("C_InvoicePaySchedule_ID", rs.getInt("id_cuota"));
				
				if (!aLine.save())
					log.log(Level.SEVERE, "Allocation Line not written ");
				
				rs.updateString("processed", "Y");
				rs.updateRow();
			}
			payment.setPayAmt(payAmt);
			if(payment!=null && payment.save())
			{
				payment.setDocAction("CO");
				payment.processIt ("CO");
				payment.save();
			}
			else
			{
				statusPay = "false";
				return statusPay;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);	
			statusPay = "false";
		}
		finally{
			 DB.close(rs, pstmt);
			 rs=null;pstmt=null;
		 }
		return statusPay;
	}	//	doIt

}	//	ImportOrder
