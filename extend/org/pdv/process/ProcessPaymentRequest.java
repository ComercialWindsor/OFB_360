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
import java.util.Date;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;
import org.jruby.RubyProcess.Sys;

import com.sun.corba.ee.org.apache.bcel.generic.NEWARRAY;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessPaymentRequest.java,v 1.2 2011/06/12 00:51:01  $
 */
public class ProcessPaymentRequest extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int 			Record_ID;
	private String P_DocAction;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DocAction"))
				P_DocAction = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		Record_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		X_C_PaymentRequest pr = new X_C_PaymentRequest(Env.getCtx(), Record_ID, get_TrxName());
		
		String requestType = pr.getRequestType();
		
		requestType = Character.toString(requestType.charAt(0));
		
		if(pr.getLines().length==0 && !requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion) )
			return  "No Lines";
		
		/*if(!pr.getRequestType().equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura))
			if(pr.getC_Charge_ID()<=0)
				return "Cargo es obligatorio para este tipo de solicitud";*/
		
		if(pr.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_WaitingConfirmation) && pr.getC_BankAccount_ID()<=0)
			return "Debe indicar una cuenta bancaria para generar el pago";
		
		
		if(pr.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_Completed) &&  P_DocAction.equals("VO"))
		{
			MPayment pay = new MPayment(Env.getCtx(), pr.getC_Payment_ID(), get_TrxName());
			if(pay.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_Completed) || pay.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_InProgress) )
				return "el pago relacionado con esta solicitud se encuentra compleado o procesado, no puede se puede anular";
			
			pay.deleteEx(true, get_TrxName());
			pr.setC_Payment_ID(0);
			pr.setDocStatus("VO");
			pr.save();
			
			 if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion ))
			   {	
							MProject pj = new  MProject(getCtx(),pr.get_ValueAsInt("C_Project_ID"),get_TrxName() );
							pj.setProjectBalanceAmt(pj.getProjectBalanceAmt().subtract(pr.getPayAmt()));
							pj.save();
							
							if(pr.get_ValueAsInt("C_ProjectSchedule_ID")>0)
								DB.executeUpdate("update C_ProjectSchedule set isvalid='Y',C_PaymentRequest_ID="+pr.getC_PaymentRequest_ID()+" where C_ProjectSchedule_ID="+pr.get_ValueAsInt("C_ProjectSchedule_ID"), get_TrxName());
						//}
						
					//}
			   }
			 
			 return "Pago generado borrado y solicitud de pago anulada";
		}
		
		if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion ) && pr.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_Drafted))
		{			
			MProject pj = new  MProject(getCtx(),pr.get_ValueAsInt("C_Project_ID"),get_TrxName() );
			if(pj.getCommittedAmt().compareTo( pj.getProjectBalanceAmt().add(pr.getPayAmt())     )<0 )
				return " esta solicitud de pago sobrepasara el monto asignado al proyecto :" + pj.getName();
		}
		if(pr.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_Drafted))
		{
			
			//ininoles validacion detalle desde remuneracion
			if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeRemuneracion))
			{
				String sqlcandet = "SELECT COUNT(*) FROM C_PaymentRequestLine "+
						"WHERE (gl_journalline_id is null OR isactive = 'N') AND C_Paymentrequest_id = "+pr.get_ID();
				   
				   int cantDet = DB.getSQLValue(get_TrxName(), sqlcandet);
				   
				   if (cantDet > 0)
					   return "Linea de Detalle no Válida";
			}
			
			boolean override = pr.get_ValueAsBoolean("Override");
			
			if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura) && override == false)
			{
				X_C_PaymentRequestLine lines[]	= pr.getLines();
				for(X_C_PaymentRequestLine  line:lines)
				{
					if(line.getC_Invoice_ID()>0)
					{
						MInvoice invo = new MInvoice(getCtx(), line.getC_Invoice_ID(), get_TrxName());
						
						String sqlVal = "SELECT COUNT(*) FROM " +
								"C_Payment cp WHERE cp.C_BPartner_ID = ? "+
								"AND (SELECT COUNT(*) FROM C_AllocationLine cal WHERE cal.C_Payment_ID = cp.C_Payment_ID) < 1 "+
								"AND cp.PayAmt = ? "+
								"AND cp.IsReceipt = 'N' " +
								"AND cp.DocStatus NOT IN ('VO')"+
								"AND cp.AD_Org_ID = ?"+
								"AND C_Charge_ID IS NULL";
						
						int cant = DB.getSQLValue(null, sqlVal, invo.getC_BPartner_ID(), invo.getGrandTotal(),pr.getAD_Org_ID());
						if (cant > 0)
						{
							return "Existe un pago sin asignaciones para el mismo socio de negocio con el mismo monto que la factura "+invo.getDocumentNo();
						}
					}
				}				
			}
						
			pr.setDocStatus(X_C_PaymentRequest.DOCSTATUS_WaitingConfirmation);
			pr.setProcessed(true);
			pr.save();
			
			for(X_C_PaymentRequestLine line: pr.getLines()){
				line.setProcessed(true);
				line.save();
			}	
			
			this.commitEx();
			return "Solicitud Enviada";
		}
		
		if(pr.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_WaitingConfirmation))
		{	 
			//Se agrega tipo manual para ingreso sin documentos asociados 
			if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura) || requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion) || requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeRemuneracion)
					|| requestType.equals(X_C_PaymentRequest.REQUESTTYPE_IngresoManual))
			{
				
				if (pr.getC_Payment_ID() > 0)
				{
					log.log(Level.SEVERE, "Payment Already Generated");
					return "Pago ya generado";					
				}
				//validacion de facturas usadas
				/*
				String sqlCountV1 = "SELECT COUNT(1) FROM C_PaymentRequestLine rl WHERE rl.C_PaymentRequest_ID = "+pr.get_ID()+
						" AND C_InvoicePaySchedule_ID is null AND rl.C_Invoice_ID IN (select rl2.c_invoice_id "+ 
						" from c_paymentrequestline rl2 inner join c_paymentrequest r2 on (rl2.c_paymentrequest_id=r2.c_paymentrequest_id) "+
						" where r2.docstatus IN ('DR','CL','WC','CO') and rl2.c_invoice_id is not null "+
						" AND r2.c_paymentrequest_ID <> "+pr.get_ID()+" AND rl2.C_InvoicePaySchedule_ID is null)";
				
				int cant1 = DB.getSQLValue(get_TrxName(), sqlCountV1);
				
				String sqlCountV2 = "SELECT COUNT(1) FROM C_PaymentRequestLine rl WHERE rl.C_PaymentRequest_ID = "+pr.get_ID()+
						" AND rl.C_InvoicePaySchedule_ID is not null AND rl.C_InvoicePaySchedule_ID IN (select rl2.C_InvoicePaySchedule_ID "+ 
						" from c_paymentrequestline rl2 inner join c_paymentrequest r2 on (rl2.c_paymentrequest_id=r2.c_paymentrequest_id) "+ 
						"where r2.docstatus IN ('DR','CL','WC','CO') " +
						"AND r2.c_paymentrequest_ID <> "+pr.get_ID()+" AND rl2.C_InvoicePaySchedule_ID is not null)";
				
				int cant2 = DB.getSQLValue(get_TrxName(), sqlCountV2);
				
				if (cant1 > 0 || cant2 > 0)
				{
					log.log(Level.SEVERE, "Invoice Already Used");
					return "Factura ya usada en otra Solicitud";
				}
				*/ 
				//se cambiavalidacion por monto
				//Validamos monto linea a linea
				X_C_PaymentRequestLine linesValid[]	= pr.getLines();
				
				for(X_C_PaymentRequestLine  lineValid:linesValid)
				{
					int id_schedule = lineValid.getC_InvoicePaySchedule_ID();
					if (id_schedule < 0)
						id_schedule = 0;
					if (lineValid.getC_Invoice_ID() > 0)
					{	
						BigDecimal amtValidator = DB.getSQLValueBD(get_TrxName(), "" +
								" SELECT ABS(currencyConvert(invoiceOpen(C_Invoice_ID,"+id_schedule+"),i.C_Currency_ID,"+pr.getC_Currency_ID()+",i.dateacct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP) as abierto " +
								" FROM C_Invoice_v i WHERE C_Invoice_ID = "+lineValid.getC_Invoice_ID());
						if(amtValidator == null)
							amtValidator = Env.ZERO;
						amtValidator = amtValidator.subtract(lineValid.getAmt());
						
						if (amtValidator.compareTo(Env.ZERO) < 0)
						{
							log.log(Level.SEVERE, "Invoice Already Used");
							return "Monto de Pago Supera Monto de Factura ";
						}
					}					
				}
				//correlativo cheque
				if(pr.getTenderType().equals(X_C_PaymentRequest.TENDERTYPE_Check))
					if(pr.getCheckNo()==null || pr.getCheckNo().length()==0)
					{
						MBankAccount bank = MBankAccount.get(getCtx(), pr.getC_BankAccount_ID());
						if(bank.get_ValueAsInt("CheckSequence")>0)
						{
							pr.setCheckNo(bank.get_ValueAsString("CheckSequence"));
							bank.set_CustomColumn("CheckSequence", bank.get_ValueAsInt("CheckSequence")+1);
							bank.save();
						}
					}
				//correlativo cheque fin
				Date datePay = new java.util.Date();
				Timestamp datePayTS = new Timestamp(datePay.getTime());
				
				MPayment pay = new MPayment(Env.getCtx(), 0, get_TrxName());
				pay.setIsReceipt(false);
				pay.setAD_Org_ID(pr.getAD_Org_ID());
				pay.setC_BPartner_ID(pr.getC_BPartner_ID());
				pay.setC_BankAccount_ID(pr.getC_BankAccount_ID());
				//ininoles se cambia fechas por fecha del sistema
				pay.setDateTrx(datePayTS);
				pay.setDateAcct(datePayTS);
				pay.setC_Currency_ID(pr.getC_Currency_ID());
				pay.setTenderType(pr.getTenderType());
				
				if(pr.getTenderType().equals(X_C_PaymentRequest.TENDERTYPE_Check))
					pay.setCheckNo(pr.getCheckNo());	
				if(!requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura))
					pay.setC_Currency_ID(pr.getC_Currency_ID());
					
				if(pr.getC_Charge_ID()>0)
				  pay.setC_Charge_ID(pr.getC_Charge_ID());
				
				pay.setPayAmt(pr.getPayAmt());
				pay.set_CustomColumn("C_PaymentRequest_ID", pr.getC_PaymentRequest_ID());
				
				
				//ininoles que no se siga ejecutando si hay error al guardar el pago
				if (!pay.save())
				{
					log.log(Level.SEVERE, "Payment not save");
					return "Payment not save ";
				}   
				
				log.config("Payment ID :"+pay.getC_Payment_ID() );				
				pr.setDocStatus(X_C_PaymentRequest.DOCSTATUS_Completed);
				pr.setC_Payment_ID(pay.getC_Payment_ID());
				pr.setProcessed(true);
				pr.save();
				log.config("pr saved");
				
				//ininoles que no se cambie el estado antes de guardar el pago.
				pr.setDocStatus(X_C_PaymentRequest.DOCSTATUS_Completed);
				
				//se crean todas las asignaciones desde factura, aunque tenga solo 1 factura asociada.
				if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura) && pr.getLines().length>0){
					//create allocations
					MAllocationHdr alloc = new MAllocationHdr (Env.getCtx(), true,	//	manual
							pr.getDateTrx(), pr.getC_Currency_ID(), "Payment Request", get_TrxName());
						alloc.setAD_Org_ID(pr.getAD_Org_ID());
						if (!alloc.save())
						{
							log.log(Level.SEVERE, "Allocation not created");
							return  "Allocation not created";
						}
					
					X_C_PaymentRequestLine lines[]	= pr.getLines();
					for(X_C_PaymentRequestLine  line:lines){
						if(line.getC_Invoice_ID()>0){
							
							MAllocationLine aLine = new MAllocationLine (alloc, line.getAmt().negate(), 
									Env.ZERO, Env.ZERO, Env.ZERO);
							aLine.setC_Invoice_ID(line.getC_Invoice_ID());
							aLine.setC_Payment_ID(pay.getC_Payment_ID());
							if(line.getC_InvoicePaySchedule_ID() > 0)
								aLine.set_CustomColumn("C_InvoicePaySchedule_ID", line.getC_InvoicePaySchedule_ID());
								
							if (!aLine.save())
								log.log(Level.SEVERE, "Allocation Line not written ");
						}
					}
				}
				
			   if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion ))
			   {			
							MProject pj = new  MProject(getCtx(),pr.get_ValueAsInt("C_Project_ID"),get_TrxName() );
							pj.setProjectBalanceAmt(pj.getProjectBalanceAmt().add(pr.getPayAmt()));
							pj.save();
							
							//ininoles, actualizacion item de proyecto, no solo cabecera
							String sql2 = "select max(dmd.c_projectline_id) from C_PaymentRequest cpr "+
									"inner join C_ProjectSchedule cps on (cpr.C_ProjectSchedule_id = cps.C_ProjectSchedule_id) "+
									"inner join DM_Document dmd on (dmd.DM_Document_ID = cps.DM_Document_id) where cpr.C_PaymentRequest_ID=?";
														
							int cpl = DB.getSQLValue(get_TrxName(), sql2, pr.get_ID());
							
							if (cpl < 1)
								return "No se puede generar el pago, la cuota debe tener un compromiso";
							
							
							MProjectLine pjline = new MProjectLine(getCtx(), cpl, get_TrxName());
							BigDecimal sum = (BigDecimal)pjline.get_Value("ProjectBalanceAmt");
							sum = sum.add((BigDecimal)pr.getPayAmt());
							
							pjline.set_CustomColumn("ProjectBalanceAmt",sum ) ;
							pjline.save();
							
							//fin actualizacion de montos por item
							
							if(pr.get_ValueAsInt("C_ProjectSchedule_ID")>0)
								DB.executeUpdate("update C_ProjectSchedule set isvalid='N',C_PaymentRequest_ID="+pr.getC_PaymentRequest_ID()+" where C_ProjectSchedule_ID="+pr.get_ValueAsInt("C_ProjectSchedule_ID"), get_TrxName());
						//}
						
					//}
							
					//ininoles actualizacion de compromiso con valores acumulados inicio
					X_C_ProjectSchedule ps = new X_C_ProjectSchedule(getCtx(), pr.get_ValueAsInt("C_ProjectSchedule_ID"),get_TrxName());
					X_DM_Document dm = new X_DM_Document(getCtx(), ps.get_ValueAsInt("DM_Document_ID"), get_TrxName());
					//validacion para anticipo
					
					BigDecimal NAcumAnticipo = (BigDecimal)dm.get_Value("AcumAnticipo");
					if (pr.get_ValueAsString("IsPrepayment").equals("true") || pr.get_ValueAsString("IsPrepayment").equals("Y"))
					{						 
						 NAcumAnticipo = NAcumAnticipo.add(pr.getPayAmt());						 
						 dm.set_CustomColumn("AcumAnticipo", NAcumAnticipo);
					}
					else 
					{
						//Total a la fecha
						BigDecimal NAmtDate = (BigDecimal)dm.get_Value("AmtDate");
						//NAmtDate = NAmtDate.add(pr.getPayAmt());
						//ininoles modificacion para que tome monto de inversion en vez de monto de solicitud de pago
						NAmtDate = NAmtDate.add(ps.getDueAmt()); 
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
					NAcumDevR = NAcumDevR.add(sumER);
					dm.set_CustomColumn("AcumDevR", NAcumDevR);	
					
					//multas
					String sqlD1 = "select abs(Coalesce(sum(pp.referenceamount), 0)) + abs(Coalesce(sum(pp.amt), 0)) "+
								"from C_ProjectSchedule ps "+
								"inner join PM_ProjectPay pp on (pp.C_ProjectSchedule_id = ps.C_ProjectSchedule_id) "+
								"where ps.C_ProjectSchedule_id = ? and pay_type like 'D1'";
					BigDecimal sumD1 = new BigDecimal("0.0");
					sumD1 = DB.getSQLValueBD(get_TrxName(), sqlD1, pr.get_ValueAsInt("C_ProjectSchedule_ID"));
										
					BigDecimal NAcumMultas = (BigDecimal)dm.get_Value("AcumMultas");
					NAcumMultas = NAcumMultas.add(sumD1);
					dm.set_CustomColumn("AcumMultas", NAcumMultas);
					
					//retenciones
					String sqlR1 = "select abs(Coalesce(sum(pp.referenceamount), 0)) + abs(Coalesce(sum(pp.amt), 0)) "+
							"from C_ProjectSchedule ps "+
							"inner join PM_ProjectPay pp on (pp.C_ProjectSchedule_id = ps.C_ProjectSchedule_id) "+
							"where ps.C_ProjectSchedule_id = ? and pay_type like 'R1'";
					BigDecimal sumR1 = new BigDecimal("0.0");
					sumR1 = DB.getSQLValueBD(get_TrxName(), sqlR1, pr.get_ValueAsInt("C_ProjectSchedule_ID"));
										
					BigDecimal NAcumRetencion = (BigDecimal)dm.get_Value("AcumRetencion");
					NAcumRetencion = NAcumRetencion.add(sumR1);
					dm.set_CustomColumn("AcumRetencion", NAcumRetencion);
					
					//devolucion de anticipo
					String sqlD2 = "select abs(Coalesce(sum(pp.referenceamount), 0)) + abs(Coalesce(sum(pp.amt), 0)) "+
							"from C_ProjectSchedule ps "+
							"inner join PM_ProjectPay pp on (pp.C_ProjectSchedule_id = ps.C_ProjectSchedule_id) "+
							"where ps.C_ProjectSchedule_id = ? and pay_type like 'D2'";
					BigDecimal sumD2 = new BigDecimal("0.0");
					sumD2 = DB.getSQLValueBD(get_TrxName(), sqlD2, pr.get_ValueAsInt("C_ProjectSchedule_ID"));
										
					BigDecimal NAcumDevA = (BigDecimal)dm.get_Value("AcumDevA");
					NAcumDevA = NAcumDevA.add(sumD2);
					dm.set_CustomColumn("AcumDevA", NAcumDevA);
					
						
					dm.set_CustomColumn("AcumAnticipo", NAcumAnticipo);
					
					dm.save();
					//fin ininoles
					
					//ininoles se agrega seteo de campo processed a cuota y rendicion
					
					String sqlUpdatePP = "update PM_ProjectPay set processed = 'Y' where C_ProjectSchedule_ID = "+ps.get_ID();
					DB.executeUpdate(sqlUpdatePP, get_TrxName());		
					
					String sqlUpdatePS = "";
					
					try
					{
						sqlUpdatePS = "update C_ProjectSchedule set processed = 'Y' where C_ProjectSchedule_ID = "+ps.get_ID();
						DB.executeUpdate(sqlUpdatePS, get_TrxName());										
					}catch (Exception e)
					{
						log.log(Level.SEVERE,sqlUpdatePS, e);
					}		
			   }
			   
			   //ininoles validacion para generar pagos completados 
			   String com = pr.get_ValueAsString("PayComplete");
				
			   if (com.equals("Y") || com.equals(true) || com.equalsIgnoreCase("true"))
			   {
				   pay.setDocStatus(pay.completeIt());//ininoles pago completo
				   				   
				   //ininoles validacion para generar pagos conciliados
				   String IsCon = pr.get_ValueAsString("IsReconciled");
					
				   if (IsCon.equals("Y") || IsCon.equals(true) || IsCon.equalsIgnoreCase("true"))
				   {
					   pay.setIsReconciled(true);					
				   }
				   
				   pay.save();
			   }
			   //ininoles se agrega logica para que pagos desde remuneraciones no sean asignables
			   if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeRemuneracion))
			   {
				   pay.setIsAllocated(true);				   
				   pay.save();   
			   }
			   			   
			   this.commitEx();
				return "Pago Generado :" + pay.getDocumentNo();
				
			}//fin desde factura o resolucion
			else{
				
				X_C_PaymentRequestLine lines[]	= pr.getLines();
				int count =0;
				for(X_C_PaymentRequestLine  line:lines){
					
					MJournalLine gline = new MJournalLine (getCtx(),line.getGL_JournalLine_ID(),get_TrxName() );
					
					MPayment pay = new MPayment(Env.getCtx(), 0, get_TrxName());
					pay.setIsReceipt(false);
					pay.setAD_Org_ID(pr.getAD_Org_ID());
					pay.setC_BPartner_ID( gline.get_ValueAsInt("C_BPartner_ID") );
					pay.setC_BankAccount_ID(pr.getC_BankAccount_ID());
					pay.setDateTrx(pr.getDateTrx());
					pay.setDateAcct(pr.getDateAcct());
					pay.setC_Currency_ID(pr.getC_Currency_ID());
					pay.setTenderType(pr.getTenderType());
					pay.setDescription(gline.getDescription());
					
					if(pr.getTenderType().equals(X_C_PaymentRequest.TENDERTYPE_Check))
						pay.setCheckNo(pr.getCheckNo());	
					if(!requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura))
						pay.setC_Currency_ID(pr.getC_Currency_ID());
					
					if(pr.getC_Charge_ID()>0)
						  pay.setC_Charge_ID(pr.getC_Charge_ID());
						
					pay.setPayAmt(line.getAmt());
					pay.set_CustomColumn("C_PaymentRequest_ID", pr.getC_PaymentRequest_ID());
					pay.save();
					
					count ++;
					
					line.set_ValueOfColumn("C_Payment_ID", pay.getC_Payment_ID());
					line.save();
					
					//ininoles validacion para generar pagos completados 
					String com = pr.get_ValueAsString("PayComplete");
					if (com.equals("Y") || com.equals(true) || com.equalsIgnoreCase("true"))
					{
						pay.setDocStatus(pay.completeIt());//ininoles pago completo
						
						//ininoles validacion para generar pagos conciliados
						String IsCon = pr.get_ValueAsString("IsReconciled");
							
						if (IsCon.equals("Y") || IsCon.equals(true) || IsCon.equalsIgnoreCase("true"))
						{
							pay.setIsReconciled(true);						
						}
						
						pay.save();
					} 
				}
				
				pr.setDocStatus(X_C_PaymentRequest.DOCSTATUS_Completed);
				pr.setProcessed(true);
				pr.save();
				
				return "Pagos Generados :"+count;
			}
		}	
		return "Ninguna Accion Realizada";
	}	//	doIt	
}	//	CopyOrder
