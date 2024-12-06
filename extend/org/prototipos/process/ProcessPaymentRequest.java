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
package org.prototipos.process;

import java.math.BigDecimal;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

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
								"AND cp.DocStatus NOT IN ('VO')";
						
						int cant = DB.getSQLValue(null, sqlVal, invo.getC_BPartner_ID(), invo.getGrandTotal());
						if (cant > 0)
						{
							return "Existe un pago sin asignaciones para el mismo socio de negocio con el mismo monto que la factura "+invo.getDocumentNo();
						}
					}
				}				
			}		
			if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion))
			{
				if (pr.get_ValueAsInt("C_ProjectSchedule_ID") > 0)
				{
					X_C_ProjectSchedule ps = new X_C_ProjectSchedule(getCtx(), pr.get_ValueAsInt("C_ProjectSchedule_ID"), get_TrxName());
					BigDecimal usedRate = (BigDecimal)ps.get_Value("usedRate");
					usedRate = usedRate.add((BigDecimal)pr.get_Value("rate"));
					ps.set_CustomColumn("UsedRate", usedRate);
					ps.save();
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
		
		if(pr.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_WaitingConfirmation) && pr.isSignature1() && pr.isSignature2())
		{
			//ininoles se agrega tipo desde remuneracion para lineas de journal con y sin BP 
			//Se agrega tipo manual para ingreso sin documentos asociados 
			//validacion antes completar
			if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion))
			{			
				int id_invoice = 0;
				id_invoice = pr.get_ValueAsInt("C_Invoice_ID");			
				if (id_invoice > 0)
				{
					;
				}
				else
				{
					return "Debe Estar Generada la Factura";
				}	
			}	
			
			if(requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeFactura) || requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeResolucion) || requestType.equals(X_C_PaymentRequest.REQUESTTYPE_DesdeRemuneracion)
					|| requestType.equals(X_C_PaymentRequest.REQUESTTYPE_IngresoManual))
			{
				
				if (pr.getC_Payment_ID() > 0)
				{
					log.log(Level.SEVERE, "Payment Already Generated");
					return "Pago ya generado";					
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
				
				MPayment pay = new MPayment(Env.getCtx(), 0, get_TrxName());
				pay.setIsReceipt(false);
				pay.setAD_Org_ID(pr.getAD_Org_ID());
				pay.setC_BPartner_ID(pr.getC_BPartner_ID());
				pay.setC_BankAccount_ID(pr.getC_BankAccount_ID());
				pay.setDateTrx(pr.getDateTrx());
				pay.setDateAcct(pr.getDateAcct());
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
				
				/*pay.processIt("CO");
				pay.setProcessed(true);
				pay.save();*/
				
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
					
					if(pr.get_ValueAsInt("C_ProjectSchedule_ID")>0)
						DB.executeUpdate("update C_ProjectSchedule set isvalid='N',C_PaymentRequest_ID="+pr.getC_PaymentRequest_ID()+" where C_ProjectSchedule_ID="+pr.get_ValueAsInt("C_ProjectSchedule_ID"), get_TrxName());
							
					//ininoles actualizacion de compromiso con valores acumulados inicio
					X_C_ProjectSchedule ps = new X_C_ProjectSchedule(getCtx(), pr.get_ValueAsInt("C_ProjectSchedule_ID"),get_TrxName());
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
		
		if(pr.getDocStatus().equals(X_C_PaymentRequest.DOCSTATUS_WaitingConfirmation) && ( !pr.isSignature1() || !pr.isSignature2()) )
			return "La solicitud necesita ser aprobada para generar el pago";
			
		return "Ninguna Accion Realizada";
		
	}	//	doIt
}	//	CopyOrder
