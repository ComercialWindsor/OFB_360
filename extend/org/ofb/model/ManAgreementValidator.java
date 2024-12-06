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
package org.ofb.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MPayment;
import org.compiere.model.MProject;
import org.compiere.model.MProjectLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_AD_Ref_List;
import org.compiere.model.X_C_Payment;
import org.compiere.model.X_C_PaymentRequest;
import org.compiere.model.X_C_PaymentRequestLine;
import org.compiere.model.X_C_Project;
import org.compiere.model.X_C_ProjectLine;
import org.compiere.model.X_C_ProjectSchedule;
import org.compiere.model.X_DM_Document;
import org.compiere.model.X_DM_DocumentLine;
import org.compiere.model.X_DM_DocumentReception;
import org.compiere.model.X_DM_MandateAgreement;
import org.compiere.model.X_DM_MandateAgreementLine;
import org.compiere.model.X_PM_ProjectPay;
import org.compiere.model.X_PM_Supervision;
import org.compiere.model.X_PM_SupervisionObs;
import org.compiere.model.X_PM_Tender;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import com.sun.xml.internal.bind.v2.model.core.MaybeElement;

/**
 *	Validator for company Sismode
 *
 *  @author Fabian Aguilar
 */
public class ManAgreementValidator implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ManAgreementValidator ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ManAgreementValidator.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;

	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing global validator: "+this.toString());
		}

		//	Tables to be monitored
		//	Documents to be monitored
		//engine.addDocValidate(X_DM_MandateAgreement.Table_Name, this);
		engine.addModelChange(X_DM_MandateAgreement.Table_Name, this);
		engine.addModelChange(X_C_ProjectSchedule.Table_Name, this);
		engine.addModelChange(X_DM_DocumentLine.Table_Name, this);
		engine.addModelChange(X_DM_Document.Table_Name, this);
		engine.addModelChange(X_C_PaymentRequestLine.Table_Name, this);
		engine.addModelChange(X_C_Payment.Table_Name, this);		
		engine.addModelChange(X_PM_Tender.Table_Name, this);//concurso
		engine.addModelChange(X_PM_SupervisionObs.Table_Name, this);//obs fiscalizacion
		engine.addModelChange(X_C_Project.Table_Name, this);//proyecto
		engine.addModelChange(X_C_ProjectLine.Table_Name, this);//
		engine.addModelChange(X_DM_DocumentReception.Table_Name, this);//
		engine.addModelChange(X_AD_Ref_List.Table_Name, this);//
		engine.addModelChange(MInvoice.Table_Name, this);//		
		engine.addModelChange(MInvoiceLine.Table_Name, this);//
		engine.addModelChange(X_PM_ProjectPay.Table_Name, this);//
		engine.addModelChange(X_PM_Supervision.Table_Name, this);//
		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	Called after PO.beforeSave/PO.beforeDelete
     *	when you called addModelChange for the table
     *	@param po persistent object
     *	@param type TYPE_
     *	@return error message or null
     *	@exception Exception if the recipient wishes the change to be not accept.
     */
	public static final String DOCSTATUS_Drafted = "DR";
	public static final String DOCSTATUS_Completed = "CO";
	public static final String DOCSTATUS_InProgress = "IP";
	public static final String DOCSTATUS_Voided = "VO";
	
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if(type == TYPE_AFTER_NEW && po.get_Table_ID()==X_DM_MandateAgreement.Table_ID)
		{
			X_DM_MandateAgreement cm = (X_DM_MandateAgreement) po;
			MProject pj = new MProject (po.getCtx(), cm.getC_Project_ID(), po.get_TrxName());
			
			cm.setAmt(pj.getCommittedAmt());
			cm.set_CustomColumn("Code", pj.get_Value("POReference"));
			cm.set_CustomColumn("C_BPartner_ID", pj.get_Value("C_BPartner_ID"));
			cm.set_CustomColumn("C_BPartnerC_ID", pj.get_Value("C_BPartnerC_ID"));
			cm.set_CustomColumn("C_BPartnerA_ID", pj.get_Value("C_BPartnerA_ID"));
			cm.set_CustomColumn("Name", pj.getName());
			cm.save();
			
			MProjectLine[] lines = pj.getLines();
			int ii=10;
			for(MProjectLine line : lines){	
				X_DM_MandateAgreementLine  cml = new X_DM_MandateAgreementLine(Env.getCtx(), 0 ,po.get_TrxName());
				cml.setDM_MandateAgreement_ID(cm.getDM_MandateAgreement_ID());
				cml.setAD_Org_ID(line.getAD_Org_ID());
				cml.setAmt(line.getCommittedAmt());
				cml.setLine(ii);
				cml.setDatePromised(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				cml.setDescription(line.getDescription());
				cml.set_CustomColumn("M_Product_ID",line.getM_Product_ID());
				cml.saveEx();
				
				ii+=10;
			}
		}
		if (type == TYPE_AFTER_CHANGE && po.get_Table_ID()==X_C_Payment.Table_ID)
		{
			X_C_Payment pay = (X_C_Payment) po;
			//MPayment Mpay = new MPayment(po.getCtx(), pay.get_ID(), po.get_TrxName());
			
			PreparedStatement pstmt = null;
			String mySql = "select distinct C_AllocationHdr_ID FROM C_AllocationLine WHERE C_Payment_ID = ?";
			
			if (pay.getDocStatus() == DOCSTATUS_Completed)
			{
				try 
				{
					pstmt = DB.prepareStatement(mySql, po.get_TrxName());
					pstmt.setInt(1, pay.get_ID());											
					ResultSet rs = pstmt.executeQuery();
				
					if (rs.next())
					{
						MAllocationHdr hdr = new MAllocationHdr(po.getCtx(), rs.getInt("C_AllocationHdr_ID"), po.get_TrxName());
						if (hdr.getDocStatus().compareTo(DOCSTATUS_Drafted) == 0 || hdr.getDocStatus().compareTo(DOCSTATUS_InProgress) == 0)
						{							
							hdr.setDocStatus(hdr.completeIt());
							hdr.save();
							log.info("Allocation: "+hdr.get_ID()+" Completed");
							
						}
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
			}			
			if (pay.getDocStatus().compareTo(DOCSTATUS_Voided) == 0)
			{
				try 
				{
					pstmt = DB.prepareStatement(mySql, po.get_TrxName());
					pstmt.setInt(1, pay.get_ID());											
					ResultSet rs = pstmt.executeQuery();
				
					if (rs.next())
					{
						MAllocationHdr hdr = new MAllocationHdr(po.getCtx(), rs.getInt("C_AllocationHdr_ID"), po.get_TrxName());
						if (hdr.getDocStatus().compareTo(DOCSTATUS_Completed) == 0)
						{							
							if (hdr.voidIt())
							{
								hdr.save();
								log.info("Allocation: "+hdr.get_ID()+ " Voided");
							}														
						}
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		
		if( (type == TYPE_AFTER_CHANGE || type== TYPE_AFTER_NEW || type== TYPE_AFTER_DELETE) && po.get_Table_ID()==X_DM_DocumentLine.Table_ID)
		{
			X_DM_DocumentLine line = (X_DM_DocumentLine) po;
			
			BigDecimal total = DB.getSQLValueBD(po.get_TrxName(), "select sum(amt) from DM_DocumentLine where isactive='Y' and DM_Document_ID="+ line.getDM_Document_ID());
			
			X_DM_Document hr = new X_DM_Document(po.getCtx(), line.getDM_Document_ID(), po.get_TrxName());
			hr.setAmt(total==null?Env.ZERO:total );
			hr.save();
		}
		
		if( (type == TYPE_AFTER_CHANGE || type== TYPE_AFTER_NEW) && po.get_Table_ID()==X_DM_Document.Table_ID)
		{
			X_DM_Document doc = (X_DM_Document) po;
			
			int cant = 0;
			
			cant = DB.getSQLValue(po.get_TrxName(),
					"select count(1) from DM_Document where resolutionnumber = ? and DM_Document_ID <> ?",doc.get_ValueAsString("ResolutionNumber"),doc.get_ID());
			
			if (cant > 0)
				return "Número de Resolución ya usado";

		}
		
		if( (type == TYPE_AFTER_CHANGE || type== TYPE_AFTER_NEW || type== TYPE_AFTER_DELETE) && po.get_Table_ID()==X_C_PaymentRequestLine.Table_ID)
		{
			X_C_PaymentRequestLine line = (X_C_PaymentRequestLine) po;
			
			BigDecimal total = DB.getSQLValueBD(po.get_TrxName(), "select sum(amt) from C_PaymentRequestLine where isactive='Y' and C_PaymentRequest_ID="+ line.getC_PaymentRequest_ID());
			
			X_C_PaymentRequest hr = new X_C_PaymentRequest(po.getCtx(), line.getC_PaymentRequest_ID(), po.get_TrxName());
			hr.setPayAmt(total==null?Env.ZERO:total );
			hr.save();
		}
		
		if( (type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW)  && po.get_Table_ID()==X_DM_Document.Table_ID)
		{
			X_DM_Document dm = (X_DM_Document) po;
			if(dm.getC_Project_ID()>0 && dm.getDM_DocumentType().equals("AP"))
			{
				MProject pj = new MProject (po.getCtx(), dm.getC_Project_ID(), po.get_TrxName());
				dm.setDescription(pj.getPOReference() + "-" +pj.getName());
			}
			
		}
		
		if (type == TYPE_BEFORE_CHANGE && po.get_Table_ID()==X_DM_MandateAgreement.Table_ID && po.is_ValueChanged(X_DM_MandateAgreement.COLUMNNAME_C_Project_ID))
		{
			X_DM_MandateAgreement cm = (X_DM_MandateAgreement) po;
			DB.executeUpdate("delete from DM_MandateAgreementLine where DM_MandateAgreement_ID="+cm.getDM_MandateAgreement_ID(), po.get_TrxName());
			MProject pj = new MProject (po.getCtx(), cm.getC_Project_ID(), po.get_TrxName());
			
			cm.setAmt(pj.getCommittedAmt());
			cm.set_CustomColumn("Code", pj.get_Value("POReference"));
			cm.set_CustomColumn("C_BPartner_ID", pj.get_Value("C_BPartner_ID"));
			cm.set_CustomColumn("C_BPartnerC_ID", pj.get_Value("C_BPartnerC_ID"));
			cm.set_CustomColumn("C_BPartnerA_ID", pj.get_Value("C_BPartnerA_ID"));
			
			MProjectLine[] lines = pj.getLines();
			int ii=10;
			for(MProjectLine line : lines){	
				X_DM_MandateAgreementLine  cml = new X_DM_MandateAgreementLine(Env.getCtx(), 0 ,po.get_TrxName());
				cml.setDM_MandateAgreement_ID(cm.getDM_MandateAgreement_ID());
				cml.setAD_Org_ID(line.getAD_Org_ID());
				cml.setAmt(line.getCommittedAmt());
				cml.setLine(ii);
				cml.setDatePromised(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				cml.setDescription(line.getDescription());
				cml.set_CustomColumn("M_Product_ID",line.getM_Product_ID());
				cml.saveEx();
				
				ii+=10;
			}
			
		}
		
		if ( (type == TYPE_BEFORE_CHANGE || type== TYPE_BEFORE_NEW) && po.get_Table_ID()==X_C_ProjectSchedule.Table_ID)
		{
			X_C_ProjectSchedule ps = (X_C_ProjectSchedule) po;
			
			BigDecimal total = DB.getSQLValueBD(po.get_TrxName(), "select sum(dueamt) from c_projectschedule where isactive='Y' and c_project_id="+ ps.getC_Project_ID());
			
			if(total == null)
				total = Env.ZERO;
			
			if(type == TYPE_BEFORE_CHANGE)
				total = total.subtract((BigDecimal)ps.get_ValueOld("DueAmt"));
			
			total = total.add(ps.getDueAmt());
			
			if(total.compareTo(ps.getC_Project().getCommittedAmt())>0)
				return "Cuota Supera el Monto Aprobado";
			
		}
		//validacion para fechas concurso gore
		if ( (type == TYPE_BEFORE_CHANGE || type== TYPE_BEFORE_NEW) && po.get_Table_ID()==X_PM_Tender.Table_ID)//validacion fechas de concurso
		{
			X_PM_Tender td = (X_PM_Tender) po;
			Timestamp mi1 = td.getMilestone1();
			Timestamp mi2 = td.getMilestone2();
			Timestamp mi3 = td.getMilestone3();
			Timestamp mi4 = td.getMilestone4();
			Timestamp mi5 = td.getMilestone5();
			Timestamp mi6 = td.getMilestone6();
			Timestamp actual;
			
			java.util.Date date= new java.util.Date();
			actual = (new Timestamp(date.getTime()));
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime((Timestamp)actual);
			
			int year = actual.getYear();
			year = year -1;
			
			if (mi1 != null)
			{
				int yearM1 = mi1.getYear();
				if (yearM1 < year)
					return "Verificar fechas. Hay intervalos de fechas no válidos";
			}
			if (mi2 != null)
			{
				int yearM2 = mi2.getYear();
				if (yearM2 < year)
					return "Verificar fechas. Hay intervalos de fechas no válidos";
			}
			if (mi3 != null)
			{
				int yearM3 = mi3.getYear();
				if (yearM3 < year)
					return "Verificar fechas. Hay intervalos de fechas no válidos";
			}
			if (mi4 != null)
			{
				int yearM4 = mi4.getYear();
				if (yearM4 < year)
					return "Verificar fechas. Hay intervalos de fechas no válidos";
			}
			if (mi5 != null)
			{
				int yearM5 = mi5.getYear();
				if (yearM5 < year)
					return "Verificar fechas. Hay intervalos de fechas no válidos";
			}
			if (mi6 != null)
			{
				int yearM6 = mi6.getYear();
				if (yearM6 < year)
					return "Verificar fechas. Hay intervalos de fechas no válidos";
			}
						
			if (mi2 != null && mi1 != null)
			{
				if(mi2.compareTo(mi1) < 0)
				{
					return "Verificar fechas. Hay intervalos de fechas no válidos";
				}
			}
			if (mi3 != null && mi2 != null)
			{
				if(mi3.compareTo(mi2) <= 0)
				{
					return "Verificar fechas. Hay intervalos de fechas no válidos";
				}
			}
			if (mi4 != null && mi3 != null)
			{
				if(mi4.compareTo(mi3) <= 0)
				{
					return "Verificar fechas. Hay intervalos de fechas no válidos";
				}
			}
			if (mi5 != null && mi4 != null)
			{
				if(mi5.compareTo(mi4) <= 0)
				{
					return "Verificar fechas. Hay intervalos de fechas no válidos";
				}
			}
			if (mi6 != null && mi5 != null)
			{
				if(mi6.compareTo(mi5) <= 0)
				{
					return "Verificar fechas. Hay intervalos de fechas no válidos";
				}
			}
		}
		if ( (type == TYPE_BEFORE_CHANGE || type== TYPE_BEFORE_NEW) && po.get_Table_ID()==X_PM_SupervisionObs.Table_ID)
		{
			X_PM_SupervisionObs SuO = (X_PM_SupervisionObs) po;
			X_PM_Supervision Su = new X_PM_Supervision(po.getCtx(), SuO.getPM_Supervision_ID(), po.get_TrxName());
			
			String sqlVA = "SELECT MAX(c_bpartnerp_id) FROM DM_Document WHERE DM_DocumentType = '02' AND C_Project_ID = ?";
						
			int BPCon = DB.getSQLValue(po.get_TrxName(), sqlVA,Su.getC_Project_ID());
			
			if (BPCon > 0)
			{	
				SuO.setC_BPartnerCon_ID(BPCon);
			}
		}
		
		if ( (type == TYPE_BEFORE_CHANGE || type== TYPE_BEFORE_NEW) && po.get_Table_ID()==X_C_Project.Table_ID)//validacion proyecto
		{
			MProject pro = (MProject) po;
			
			if (pro.get_ValueAsBoolean("PM_Gore_UT") == true)//validacion llenado de unidad tecnica
			{
				pro.set_CustomColumn("C_BPartnerA_ID", 1000234);
			}
			
			if (pro.get_ValueAsString("ProyectStage").equalsIgnoreCase("6")
					   && pro.get_ValueAsInt("C_ProjectType_ID") == 1000000)//validacion llenado campo IsSupervised
			{
				pro.set_CustomColumn("IsSupervised", true);
			}else
			{
				pro.set_CustomColumn("IsSupervised", false);
			}			
			
			String sqlVal = "Select count(1) from c_project where POreference = ? and ProyectStage = ?";
			if(pro.getC_Project_ID()>0)
				sqlVal += " and C_Project_ID<>"+pro.getC_Project_ID();
				
			int qty = DB.getSQLValue(po.get_TrxName(), sqlVal, pro.getPOReference(), pro.get_ValueAsString("ProyectStage"));
			if (qty > 0)
			{
				return "No se pudo guardar. Codigo BIP ya ingresado con misma Etapa";
			}
		}		
		
		/*if ( (type == TYPE_BEFORE_CHANGE || type== TYPE_BEFORE_NEW) && po.get_Table_ID()==X_C_ProjectLine.Table_ID)//validacion proyecto
		{
			MProjectLine prol = (MProjectLine) po;
			if (prol.getCommittedAmt().compareTo(prol.getPlannedPrice()) > 0)
			{
				return "Monto no valido";
			}				
		}*/
		
		if ( (type== TYPE_AFTER_NEW) && po.get_Table_ID()==X_DM_DocumentReception.Table_ID)//validacion proyecto
		{
			int year = 1900;
			
			X_DM_DocumentReception dr = (X_DM_DocumentReception) po;
			year = year + dr.getDateTrx().getYear();
			
			int next = 0;
			
			if(dr.get_ValueAsString("WinType").equalsIgnoreCase("O"))
			{				
				String sql = "Select MAX(EntryNo) from DM_DocumentReception where WinType = 'O' and extract(year from datetrx) = ? ";
				String EntryNo = DB.getSQLValueString(po.get_TrxName(), sql, year);
				if (EntryNo == null)
					next = 0;
				else
					next = Integer.parseInt(EntryNo);
				next = next + 1;
				dr.setEntryNo(Integer.toString(next));						
				
			}else if(dr.get_ValueAsString("WinType").equalsIgnoreCase("S"))
			{
								
				String sql2 = "Select MAX(EntryNo) from DM_DocumentReception "+ 
						"where extract(year from datetrx) = ? "+
						"and WinType = 'S' and ad_org_id = ?"; 
				String EntryNo2 = DB.getSQLValueString(po.get_TrxName(), sql2, year, dr.getAD_Org_ID());
				if (EntryNo2 == null)
					next = 0;
				else
					next = Integer.parseInt(EntryNo2);
				next = next + 1;
				dr.setEntryNo(Integer.toString(next));				
				
			}
			dr.save();
		}
		
		if( (type == TYPE_AFTER_CHANGE || type== TYPE_AFTER_NEW ) && po.get_Table_ID()==X_AD_Ref_List.Table_ID)
		{
			X_AD_Ref_List list = (X_AD_Ref_List) po;
			
			int Acount_ID = list.get_ValueAsInt("C_Account_Acct");
			
			int Client_ID = list.getAD_Client_ID();
			String typeL = list.getValue();
			
			if (Client_ID == 1000000)
			{
				String sqlUp = "UPDATE AD_Ref_List SET C_Account_Acct = "+Acount_ID+ 
						" WHERE AD_Reference_ID = 1000077 "+
						" AND value like '"+typeL+"'";
				
				DB.executeUpdate(sqlUp, po.get_TrxName());
			}
		}
		//actualizacion de secuencia en oficina de partes ininoles
		if ( po.get_TableName().equals("DM_DocumentReception")  && (type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) ) //antes actualizar o insetar Serv Orientación
		{
		
			if (po.get_ValueAsString("WinType").equalsIgnoreCase("O") )
			{
				if (po.get_ValueAsInt("C_DocType_ID") > 0)
				{
					String sqlNext = "select Currentnext from AD_Sequence seq "+
							"inner join C_DocType doc on (seq.AD_Sequence_ID = doc.DocNoSequence_ID) "+
							"where C_DocType_ID=? ";
					
					int next = DB.getSQLValue(null, sqlNext, po.get_ValueAsInt("C_DocType_ID"));
					if (next == po.get_ValueAsInt("DocumentNo"))
					{
						int nextID = po.get_ValueAsInt("DocumentNo")+1;
						String sqlUp = "UPDATE AD_Sequence SET Currentnext = "+nextID+" WHERE AD_Sequence_ID IN "+
								" (select AD_Sequence_ID from AD_Sequence seq inner join C_DocType doc on (seq.AD_Sequence_ID = doc.DocNoSequence_ID) "+
								"where C_DocType_ID= "+po.get_ValueAsInt("C_DocType_ID")+")";
					
						DB.executeUpdate(sqlUp,null); 
					}
				}
			}
		} 
		//ininoles end
		//ininoles validacion impuesto en APB
		if ((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MInvoice.Table_ID) 
		{
			MInvoice inv = (MInvoice) po;
			if (inv.getDocBase().equalsIgnoreCase("APB"))
			{
				String sql = "SELECT COUNT(1) FROM C_InvoiceTax WHERE C_Invoice_ID = ? AND C_Tax_ID <> 1000002";
				int cant = DB.getSQLValue(null, sql, inv.get_ID());
				if (cant > 0)
					return ("Impuesto No Valido");
			}
		}
		//ininoles validacion impuesto en APB line
		if ((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MInvoiceLine.Table_ID) 
		{
			MInvoiceLine invL = (MInvoiceLine) po;
			MInvoice inv = new MInvoice(po.getCtx(), invL.getC_Invoice_ID(), po.get_TrxName());
			if (inv.getDocBase().equalsIgnoreCase("APB"))
			{
				if(invL.getC_Tax_ID() != 1000002)
					return ("Impuesto No Valido");
			}
		}
		
		if ((type == TYPE_BEFORE_NEW || type == TYPE_AFTER_NEW_REPLICATION)  && po.get_Table_ID()==X_C_Payment.Table_ID)
		{
			X_C_Payment pay = (X_C_Payment) po;
			//MPayment Mpay = new MPayment(po.getCtx(), pay.get_ID(), po.get_TrxName());
			pay.set_CustomColumn("Correlative", 0);			
		}
		if( (type == TYPE_AFTER_CHANGE || type== TYPE_AFTER_NEW) && po.get_Table_ID()==X_PM_ProjectPay.Table_ID)
		{
			X_PM_ProjectPay ppay = (X_PM_ProjectPay) po;
			
			if (ppay.getPay_Type().equalsIgnoreCase(ppay.PAY_TYPE_Multa))
			{
				BigDecimal newAmt;
				if (ppay.getAmtM().compareTo(Env.ZERO) > 0)
					newAmt = ppay.getAmtM().negate();					
				else 
					newAmt = ppay.getAmtM();
				String sqlUpdate = "UPDATE PM_ProjectPay SET amt = "+newAmt.toString()+" WHERE PM_ProjectPay_ID = "+ppay.get_ID();
				DB.executeUpdate(sqlUpdate, po.get_TrxName());
			}			
		}
		//ininoles no se permite borrar subitem si tiene compromisos asociados
		if (type == TYPE_BEFORE_DELETE && po.get_Table_ID()==X_C_ProjectLine.Table_ID)
		{
			MProjectLine prol = (MProjectLine) po;
			
			String sqlValid = "SELECT COUNT(1) FROM DM_Document WHERE dm_documenttype = '02' AND C_ProjectLine_ID = ?";
			int cant = DB.getSQLValue(po.get_TrxName(), sqlValid, prol.get_ID());
			if (cant > 0)
			{
				return "No está permitido borrar este SUBITEM debido a que tiene datos ya ingresados en un COMPROMISO";
			}				
		}		
		//ininoles no se permite borrar comproniso si tiene Programas de pago
		if (type == TYPE_BEFORE_DELETE && po.get_Table_ID()==X_DM_Document.Table_ID) 
		{
			X_DM_Document doc = (X_DM_Document) po;
			
			if (doc.getDM_DocumentType().equals("02"))
			{
				String sqlValid = "SELECT COUNT(1) FROM C_ProjectSchedule WHERE DM_Document_ID=?";
				int cant = DB.getSQLValue(doc.get_TrxName(), sqlValid, doc.get_ID());
				if (cant > 0)
				{
					return "No está permitido borrar este COMPROMISO debido a que tiene datos ya ingresados en EL PROGRAMA DE PAGO";
				}					
			}			
		}
		//ininoles no se permite borrar comproniso si tiene Programas de pago
		if (type == TYPE_BEFORE_DELETE && po.get_Table_ID()==X_C_ProjectSchedule.Table_ID) 
		{
			X_C_ProjectSchedule ps = (X_C_ProjectSchedule) po;
					
			String sqlValid = "SELECT COUNT(1) FROM PM_ProjectPay WHERE C_ProjectSchedule_ID=?";
			int cant = DB.getSQLValue(ps.get_TrxName(), sqlValid, ps.get_ID());
			if (cant > 0)
			{
				return "No está permitido borrar este PROGRAMA DE PAGO debido a que tiene datos ya ingresados en LA RENDICION DE PAGO";
			}			
		}
		if (type == TYPE_BEFORE_DELETE && po.get_Table_ID()==X_PM_Supervision.Table_ID) 
		{
			X_PM_Supervision pms = (X_PM_Supervision) po;
					
			String sqlValid = "SELECT COUNT(1) FROM PM_SupervisionObs WHERE PM_Supervision_ID=?";
			int cant = DB.getSQLValue(pms.get_TrxName(), sqlValid, pms.get_ID());
			if (cant > 0)
			{
				return "No está permitido borrar esta FISCALIZACIÓN debido a que tiene datos ya ingresados en EL INFORME FISCALIZACIÓN";
			}			
		}
		
		if ((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)  && po.get_Table_ID()==X_C_Payment.Table_ID)
		{
			X_C_Payment pay = (X_C_Payment) po;
			
			String sqlCant = "SELECT COUNT(1) FROM C_Payment WHERE C_BankAccount_ID = ? AND Correlative = ? " +
					"AND DocStatus NOT IN ('VO') AND C_Payment_ID <> ?";
			int cant = DB.getSQLValue(pay.get_TrxName(), sqlCant, pay.getC_BankAccount_ID(), pay.get_ValueAsInt("Correlative"), pay.get_ID());
			
			if (cant > 0 && pay.get_ValueAsInt("Correlative") != 0)
			{
				return "No esta permitido tener documentos con la misma CUENTA BANCARIA y N° INGRESO/EGRESO";
			}
		}
		
		return null;
	}	//	modelChange

	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);

		
		return null;
	}	//	docValidate

	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString


	

}	