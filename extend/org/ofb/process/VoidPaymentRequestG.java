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

import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class VoidPaymentRequestG extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int 			Record_ID;
	private String P_Message;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("Message"))
				P_Message = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		X_C_PaymentRequest pr = new X_C_PaymentRequest(Env.getCtx(), Record_ID, get_TrxName());

		//ininoles nuevo metodo para anular solicitudes
		String sqlvalid = "SELECT COUNT(*) FROM C_Payment WHERE C_PaymentRequest_ID = ? AND DocStatus in ('CO','DR','CL','IN')";
		int cant = DB.getSQLValue(sqlvalid, sqlvalid, pr.get_ID());
		
		if (cant > 0)
		{
			return "Solicitud no se puede anular, existen "+cant+" pagos asociados esta solicitud";
		}
		else 
		{
			//se anulan operaciones anteriores desde proyecto
			if( pr.getRequestType().equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion ))
			{
				//resto a proyecto
				MProject pj = new  MProject(getCtx(),pr.get_ValueAsInt("C_Project_ID"),get_TrxName() );
				pj.setProjectBalanceAmt(pj.getProjectBalanceAmt().subtract(pr.getPayAmt()));			
				pj.save();
				
				//actualizacion linea de proyecto
				
				String sql2 = "select max(dmd.c_projectline_id) from C_PaymentRequest cpr "+
						"inner join C_ProjectSchedule cps on (cpr.C_ProjectSchedule_id = cps.C_ProjectSchedule_id) "+
						"inner join DM_Document dmd on (dmd.DM_Document_ID = cps.DM_Document_id) where cpr.C_PaymentRequest_ID=?";
											
				int cpl = DB.getSQLValue(get_TrxName(), sql2, pr.get_ID());
				
				MProjectLine pjline = new MProjectLine(getCtx(), cpl, get_TrxName());
				BigDecimal sum = (BigDecimal)pjline.get_Value("ProjectBalanceAmt");
				sum = sum.subtract((BigDecimal)pr.getPayAmt());
				
				pjline.set_CustomColumn("ProjectBalanceAmt",sum ) ;
				pjline.save();
				
				//actualizacion cuota
				if(pr.get_ValueAsInt("C_ProjectSchedule_ID")>0)
					DB.executeUpdate("update C_ProjectSchedule set isvalid='Y',C_PaymentRequest_ID=0 where C_ProjectSchedule_ID="+pr.get_ValueAsInt("C_ProjectSchedule_ID"), get_TrxName());
				
				//actualizacion montos de compromiso
				
				X_C_ProjectSchedule ps = new X_C_ProjectSchedule(getCtx(), pr.get_ValueAsInt("C_ProjectSchedule_ID"),get_TrxName());
				X_DM_Document dm = new X_DM_Document(getCtx(), ps.get_ValueAsInt("DM_Document_ID"), get_TrxName());
				//validacion para anticipo
				
				BigDecimal NAcumAnticipo = (BigDecimal)dm.get_Value("AcumAnticipo");
				if (pr.get_ValueAsString("IsPrepayment").equals("true") || pr.get_ValueAsString("IsPrepayment").equals("Y"))
				{						 
					 NAcumAnticipo = NAcumAnticipo.subtract(pr.getPayAmt());						 
					 dm.set_CustomColumn("AcumAnticipo", NAcumAnticipo);
				}
				else 
				{
					BigDecimal NAmtDate = (BigDecimal)dm.get_Value("AmtDate");
					NAmtDate = NAmtDate.subtract(ps.getDueAmt()); 
					dm.set_CustomColumn("AmtDate", NAmtDate);						
				}
				
				//devolucion de retenciones 
				String sqlER = "select abs(Coalesce(sum(pp.referenceamount), 0)) + abs(Coalesce(sum(pp.amt), 0)) "+
							"from C_ProjectSchedule ps "+
							"inner join PM_ProjectPay pp on (pp.C_ProjectSchedule_id = ps.C_ProjectSchedule_id) "+
							"where ps.C_ProjectSchedule_id = ? and pay_type like 'ER'";
				BigDecimal sumER = new BigDecimal("0.0");
				sumER = DB.getSQLValueBD(get_TrxName(), sqlER, pr.get_ValueAsInt("C_ProjectSchedule_ID"));
									
				BigDecimal NAcumDevR = (BigDecimal)dm.get_Value("AcumDevR");
				NAcumDevR = NAcumDevR.subtract(sumER);
				dm.set_CustomColumn("AcumDevR", NAcumDevR);	
				
				//multas
				String sqlD1 = "select abs(Coalesce(sum(pp.referenceamount), 0)) + abs(Coalesce(sum(pp.amt), 0)) "+
							"from C_ProjectSchedule ps "+
							"inner join PM_ProjectPay pp on (pp.C_ProjectSchedule_id = ps.C_ProjectSchedule_id) "+
							"where ps.C_ProjectSchedule_id = ? and pay_type like 'D1'";
				BigDecimal sumD1 = new BigDecimal("0.0");
				sumD1 = DB.getSQLValueBD(get_TrxName(), sqlD1, pr.get_ValueAsInt("C_ProjectSchedule_ID"));
									
				BigDecimal NAcumMultas = (BigDecimal)dm.get_Value("AcumMultas");
				NAcumMultas = NAcumMultas.subtract(sumD1);
				dm.set_CustomColumn("AcumMultas", NAcumMultas);
							
				//retenciones
				String sqlR1 = "select abs(Coalesce(sum(pp.referenceamount), 0)) + abs(Coalesce(sum(pp.amt), 0)) "+
						"from C_ProjectSchedule ps "+
						"inner join PM_ProjectPay pp on (pp.C_ProjectSchedule_id = ps.C_ProjectSchedule_id) "+
						"where ps.C_ProjectSchedule_id = ? and pay_type like 'R1'";
				BigDecimal sumR1 = new BigDecimal("0.0");
				sumR1 = DB.getSQLValueBD(get_TrxName(), sqlR1, pr.get_ValueAsInt("C_ProjectSchedule_ID"));
									
				BigDecimal NAcumRetencion = (BigDecimal)dm.get_Value("AcumRetencion");
				NAcumRetencion = NAcumRetencion.subtract(sumR1);
				dm.set_CustomColumn("AcumRetencion", NAcumRetencion);
				
				//devolucion de anticipo
				String sqlD2 = "select abs(Coalesce(sum(pp.referenceamount), 0)) + abs(Coalesce(sum(pp.amt), 0)) "+
						"from C_ProjectSchedule ps "+
						"inner join PM_ProjectPay pp on (pp.C_ProjectSchedule_id = ps.C_ProjectSchedule_id) "+
						"where ps.C_ProjectSchedule_id = ? and pay_type like 'D2'";
				BigDecimal sumD2 = new BigDecimal("0.0");
				sumD2 = DB.getSQLValueBD(get_TrxName(), sqlD2, pr.get_ValueAsInt("C_ProjectSchedule_ID"));
									
				BigDecimal NAcumDevA = (BigDecimal)dm.get_Value("AcumDevA");
				NAcumDevA = NAcumDevA.subtract(sumD2);
				dm.set_CustomColumn("AcumDevA", NAcumDevA);								
				
				//dato de anticipo					
				dm.set_CustomColumn("AcumAnticipo", NAcumAnticipo);
				
				dm.save();
				//
			}			
			
			//end ininoles reversar operaciones			
			
			pr.setDocStatus("VO");
			String sqlDelete = "DELETE FROM Fact_Acct WHERE record_id = "+pr.get_ID()+"AND AD_Table_ID = "+pr.get_Table_ID();
			DB.executeUpdate(sqlDelete, get_TrxName());
		}
		pr.save();
		
			return "Solicitud Anulada";
	}
	
}	//	CopyOrder
