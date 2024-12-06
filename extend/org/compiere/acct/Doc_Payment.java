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
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MBankAccount;
import org.compiere.model.MCharge;
import org.compiere.model.MPayment;
import org.compiere.model.X_AD_Client;
import org.compiere.model.X_C_PaymentRequest;
import org.compiere.model.X_C_Project;
import org.compiere.model.X_C_ProjectType;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Payment (335)
 *  Document Types      ARP, APP
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Payment.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_Payment extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_Payment (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, MPayment.class, rs, null, trxName);
	}	//	Doc_Payment
	
	/**	Tender Type				*/
	private String		m_TenderType = null;
	/** Prepayment				*/
	private boolean		m_Prepayment = false;
	/** Bank Account			*/
	private int			m_C_BankAccount_ID = 0;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		MPayment pay = (MPayment)getPO();
		setDateDoc(pay.getDateTrx());
		m_TenderType = pay.getTenderType();
		m_Prepayment = pay.isPrepayment();
		m_C_BankAccount_ID = pay.getC_BankAccount_ID();
		//	Amount
		setAmount(Doc.AMTTYPE_Gross, pay.getPayAmt());
		return null;
	}   //  loadDocumentDetails

	
	/**************************************************************************
	 *  Get Source Currency Balance - always zero
	 *  @return Zero (always balanced)
	 */
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
	//	log.config( toString() + " Balance=" + retValue);
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  ARP, APP.
	 *  <pre>
	 *  ARP
	 *      BankInTransit   DR
	 *      UnallocatedCash         CR
	 *      or Charge/C_Prepayment
	 *  APP
	 *      PaymentSelect   DR
	 *      or Charge/V_Prepayment
	 *      BankInTransit           CR
	 *  CashBankTransfer
	 *      -
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);		
		Fact fact2 = new Fact(this, as, Fact.POST_Budget);	
		
		
		
		//faaguilar OFB begin
		//	Cash Transfer
		MPayment pay = (MPayment)getPO();
		if (pay.getTrxType().equals("X") || pay.getDocStatus().equals("VO"))
		{
			ArrayList<Fact> facts = new ArrayList<Fact>();
			facts.add(fact);
			return facts;
		}
		//faaguilar OFB end
		
		X_AD_Client client = new X_AD_Client(getCtx(), pay.getAD_Client_ID(), null);
		
		//ininoles validacion gore
		//faaguilar fix
		int cant = 0;
		int prID =0;
		X_C_PaymentRequest pr = null;
		try{
			String sqlC = "select count(*) from C_PaymentRequest where c_payment_id = ?";								
			String sqlI = "select COALESCE(MAX(C_PaymentRequest_id), 0) from C_PaymentRequest where c_payment_id = ?";
			cant = DB.getSQLValue(getTrxName(), sqlC, pay.get_ID());
			prID = DB.getSQLValue(getTrxName(), sqlI, pay.get_ID());
		}
		catch (Exception e)
		{
			cant =0;
			prID = 0;
		}
		if(prID>0)
			pr = new X_C_PaymentRequest(getCtx(), prID, getTrxName());
				
		if(cant > 0 && pr.getRequestType().equals("R") && (pr.get_ValueAsString("IsPrepayment").equals("false") || pr.get_ValueAsString("IsPrepayment").equals("N")))
		{
		
			String desc = "";
			int AD_Org_ID = getBank_Org_ID();		//	Bank Account Org	
			AD_Org_ID=getPO().getAD_Org_ID();//faaguilar OFB
			
			//ininoles linea de retenciones
			String sqlS = "select abs(Coalesce(sum(pp.referenceamount), 0)) from C_PaymentRequest pr "+
					"inner join C_ProjectSchedule cps on (pr.C_ProjectSchedule_id = cps.C_ProjectSchedule_id) "+
					"left join PM_ProjectPay pp on (cps.C_ProjectSchedule_id = pp.C_ProjectSchedule_id) "+
					"where pr.C_PaymentRequest_id = ? and pay_type like 'R1'";
			
			BigDecimal sum = DB.getSQLValueBD(getTrxName(), sqlS, pr.get_ID());
			
			//ininoles linea retencion honorarios
			String sqlH = "select abs(Coalesce(sum(pp.referenceamount), 0)) from C_PaymentRequest pr "+
					"inner join C_ProjectSchedule cps on (pr.C_ProjectSchedule_id = cps.C_ProjectSchedule_id) "+
					"left join PM_ProjectPay pp on (cps.C_ProjectSchedule_id = pp.C_ProjectSchedule_id) "+
					"where pr.C_PaymentRequest_id = ? and pay_type like 'FA'";
			
			BigDecimal sumH = DB.getSQLValueBD(getTrxName(), sqlH, pr.get_ID());
			
			
			BigDecimal total =  getAmount().add(sum);
			total = total.add(sumH);
			
			
			if (getDocumentType().equals(DOCTYPE_APPayment))
			{
				MAccount acct = null;
				FactLine fl=null;				
				
				if (getC_Charge_ID() != 0)
					acct = MCharge.getAccount(getC_Charge_ID(), as, total); 
				else if (m_Prepayment)
					acct = getAccount(Doc.ACCTTYPE_V_Prepayment, as);
				else
					acct = getAccount(Doc.ACCTTYPE_PaymentSelect, as);
						//faaguilar OFB custom tender end
			 	fl = fact.createLine(null, acct,
			 			getC_Currency_ID(), total, null);
			 	
			 	//ininoles descripcion tipo gore							
				if (client.getName().equalsIgnoreCase("Gobierno Regional Valparaíso"))
				{
					fl.setDescription(".Egreso/Ingreso");
				}
				//ininoles end	
			 	
				fl.save();
			 	
			 	if (fl != null && AD_Org_ID != 0
			 			&& getC_Charge_ID() == 0)		//	don't overwrite charge
			 			fl.setAD_Org_ID(AD_Org_ID);
			 	
			 	//inset a tabla de auxiliares gore con cuenta de cargo
			 	
			 	fl.save();
				
				//cabecera
			 	String descriptionAux;
				if (pay.getDescription() == null)
					descriptionAux = "--";
				else 
					descriptionAux = pay.getDescription();
			 	
				String sqlAux1C = "SELECT (SELECT M_Product_ID FROM C_ProjectLine cpl "+
						"INNER JOIN DM_Document dm ON (cpl.C_ProjectLine_ID = dm.C_ProjectLine_ID) "+
						"WHERE DM_Document_ID = cps.DM_Document_ID) as M_Product_ID, cpr.C_Project_ID "+
						"FROM c_paymentrequest cpr "+ 
						"JOIN c_projectschedule cps ON cpr.c_projectschedule_id = cps.c_projectschedule_id "+ 
						"WHERE cpr.C_PaymentRequest_ID=?";
				
				PreparedStatement pstmtauxC = null;
				ResultSet rsauxC = null;
					
				try{
					
					pstmtauxC = DB.prepareStatement(sqlAux1C, getTrxName());
					pstmtauxC.setInt(1,pr.get_ID());
					rsauxC = pstmtauxC.executeQuery();
					
					
					
					
					while(rsauxC.next())
					{
						String insetC = "INSERT INTO  t_fact_acctgore (fact_acct_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
								"ad_table_id,record_id, c_bpartner_id,description,tipo,monto,datetrx,m_product_id,c_project_id) "+
								"VALUES ("+fl.get_ID()+","+getAD_Client_ID()+","+getAD_Org_ID()+",'"+"Y"+"','"+
								pay.getCreated()+"',"+pay.getCreatedBy()+",'"+pay.getUpdated()+"',"+pay.getUpdatedBy()+","+
								pay.get_Table_ID()+","+pay.get_ID()+","+pay.getC_BPartner_ID()+",'"+descriptionAux+"','"+
								"CA"+"',"+total+",'"+pay.getDateAcct()+"',"+
								rsauxC.getInt("M_Product_ID")+","+rsauxC.getInt("c_project_id")+")";														
						
						DB.executeUpdate(insetC,getTrxName());
					}
					
				}catch (SQLException e)
				{
					log.log(Level.SEVERE, sqlAux1C, e);
				}
				finally {
					DB.close(rsauxC, pstmtauxC);
					rsauxC = null; pstmtauxC = null;
				}
				
				//detalle pago
				String insetDP = "INSERT INTO  t_fact_acctgore (fact_acct_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
								"ad_table_id,record_id, c_bpartner_id,description,tipo,monto,documentno,datetrx,c_project_id) "+
								"VALUES ("+fl.get_ID()+","+getAD_Client_ID()+","+getAD_Org_ID()+",'"+"Y"+"','"+
								pay.getCreated()+"',"+pay.getCreatedBy()+",'"+pay.getUpdated()+"',"+pay.getUpdatedBy()+","+
								pay.get_Table_ID()+","+pay.get_ID()+","+pay.getC_BPartner_ID()+",'"+"Cheque"+"','"+
								"LIN"+"',"+pay.getPayAmt()+",'"+pay.getDocumentNo()+"','"+pay.getDateAcct()+"',"+pr.get_ValueAsInt("C_Project_ID")+")";														
						
				DB.executeUpdate(insetDP,getTrxName());									
									
				//Detalle rendicion
				String sqlAux1 = "SELECT cpr.c_bpartner_id,coalesce(pp.description,'--') as description,pp.pay_type as tipo2,'LIN' as tipo, pp.referenceamount AS amt,coalesce(cil.documentno,'--') as documentno, "+ 
						"(SELECT M_Product_ID FROM C_ProjectLine cpl INNER JOIN DM_Document dm ON (cpl.C_ProjectLine_ID = dm.C_ProjectLine_ID) "+
						"WHERE DM_Document_ID = cps.DM_Document_ID) as M_Product_ID " +
						"FROM c_paymentrequest cpr JOIN c_projectschedule cps ON cpr.c_projectschedule_id = cps.c_projectschedule_id "+ 
						"JOIN pm_projectpay pp ON cps.c_projectschedule_id = pp.c_projectschedule_id LEFT JOIN c_invoice cil ON pp.c_invoice_id = cil.c_invoice_id "+ 
						"WHERE cpr.C_PaymentRequest_ID= ? AND (pay_type like 'R1' OR pay_type like 'FA') AND referenceamount > 0";
				
				PreparedStatement pstmtaux = null;
				ResultSet rsaux = null;
					
				try{
					
					pstmtaux = DB.prepareStatement(sqlAux1, getTrxName());
					pstmtaux.setInt(1,pr.get_ID());
					rsaux = pstmtaux.executeQuery();
					
					while(rsaux.next())
					{
						String inset = "INSERT INTO  t_fact_acctgore (fact_acct_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
								"ad_table_id,record_id, c_bpartner_id,description,tipo,tipo2,monto,documentno,datetrx,m_product_id,c_project_id) "+
								"VALUES ("+fl.get_ID()+","+getAD_Client_ID()+","+getAD_Org_ID()+",'"+"Y"+"','"+
								pay.getCreated()+"',"+pay.getCreatedBy()+",'"+pay.getUpdated()+"',"+pay.getUpdatedBy()+","+
								pay.get_Table_ID()+","+pay.get_ID()+","+rsaux.getInt("c_bpartner_id")+",'"+"Otros Cargos"+"','"+
								"LIN"+"','"+rsaux.getString("tipo2")+"',"+rsaux.getBigDecimal("amt")+",'"+rsaux.getString("documentno")+"','"+
								pay.getDateAcct()+"',"+rsaux.getInt("M_Product_ID")+","+pr.get_ValueAsInt("C_Project_ID")+")";														
						
						DB.executeUpdate(inset,getTrxName());
					}
					
				}catch (SQLException e)
				{
					log.log(Level.SEVERE, sqlAux1, e);
				}
				finally {
					DB.close(rsaux, pstmtaux);
					rsaux = null; pstmtaux = null;
				}
				
				//end insert 
			 	
			 	
			 	//faaguilar OFB custom tender begin
				
				if("K".equals(m_TenderType)){ //cheques
			     	acct = getAccount(Doc.ACCTTYPE_CheckToPay, as);
			     	fl = fact.createLine(null, acct,
							getC_Currency_ID(), null, getAmount());
				}	
				else if("L".equals(m_TenderType))//letras
				{
					acct = getAccount(Doc.ACCTTYPE_LetrasToPay, as);
					fl = fact.createLine(null, acct,
						getC_Currency_ID(), null, getAmount());
				}
				else if("V".equals(m_TenderType))//v vista
				{
					acct = getAccount(Doc.ACCTTYPE_VistaToPay, as);
					fl = fact.createLine(null, acct,
						getC_Currency_ID(), null, getAmount());
				}
				else if("P".equals(m_TenderType))//pagare
				{
					acct = getAccount(Doc.ACCTTYPE_PagareToPay, as);
					fl = fact.createLine(null, acct,
						getC_Currency_ID(), null, getAmount());
				}	
				else if("H".equals(m_TenderType))//canje
				{
					acct = getAccount(Doc.ACCTTYPE_ExchangeToPay, as);
					fl = fact.createLine(null, acct,
						getC_Currency_ID(), null, getAmount());
				}
				else if("B".equals(m_TenderType))//vale personal
				{
					acct = getAccount(Doc.ACCTTYPE_PersonalVoucherToPay, as);
					fl = fact.createLine(null, acct,
						getC_Currency_ID(), null, getAmount());
				}
				else
					fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_BankInTransit, as),
						getC_Currency_ID(), null, getAmount());
				//se agrega descripcion ininoles
				if (client.getName().equalsIgnoreCase("Gobierno Regional Valparaíso"))
				{
					fl.setDescription(".Egreso/Ingreso");
				}
				//ininoles end	
				fl.save();
				
		    	if (fl != null && AD_Org_ID != 0)
					fl.setAD_Org_ID(AD_Org_ID);
			
				//Asset
		    	
		    	
		    	//X_C_Project pj = new X_C_Project(getCtx(), pr.get_ValueAsInt("C_Project_ID"), getTrxName());
			 	//X_C_ProjectType pt = new X_C_ProjectType(getCtx(), pj.get_ValueAsInt("C_ProjectType_ID"), getTrxName());
			 	client = new X_AD_Client(getCtx(), pr.getAD_Client_ID(), null);
			 				 	
			 	if (client.getName().equalsIgnoreCase("Gobierno Regional Valparaíso"))//pt.getHelp().equals("S31")
			 	{
			 		//Linea de intermediacion de fondos
				 	MAccount acct2 = getAccount(Doc.ACCTTYPE_PIntermediation, as);
				    fl = fact.createLine(null, acct2,
								getC_Currency_ID(), null, sum);
				    
				    if (sum.compareTo(Env.ZERO) != 0)
				    {
				    	if (client.getName().equalsIgnoreCase("Gobierno Regional Valparaíso"))
						{
							fl.setDescription(".Egreso/Ingreso");
						}
				    	fl.save();
				    }
				    //end			 		
				    
				  //Linea de retencion honorarios
				 	MAccount acct3 = getAccount(Doc.ACCTTYPE_RetentionH, as);
				    fl = fact.createLine(null, acct3,
								getC_Currency_ID(), null, sumH);
				    
				    int idBP = DB.getSQLValue(getTrxName(), "SELECT C_BPartnerRef_ID FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=? ", as.getC_AcctSchema_ID());
				    
				    if (idBP > 0 && fl != null)
				    {
				    	fl.setC_BPartner_ID(idBP);
				    }
				    
				    
					
				  //validacion "no aplicado"
					String sqlVNA ="SELECT COUNT(1) FROM AD_Ref_List where AD_Reference_ID=1000077 and NoAcct = 'Y' and value IN ( "+
							"select Pay_Type from PM_ProjectPay where C_ProjectSchedule_ID = ?)"; 
					
					int cantVNA = DB.getSQLValue(getTrxName(), sqlVNA,  pr.get_ValueAsInt("C_ProjectSchedule_ID"));
					
					if (cantVNA > 0)
					{
						
					}else											
					{
						String sqlAB= "SELECT cpl.PJ_Item_Acct FROM C_PaymentRequest cpr "+
					    		"INNER JOIN C_ProjectSchedule cps ON (cpr.C_ProjectSchedule_ID = cps.C_ProjectSchedule_ID) "+
					    		"INNER JOIN DM_Document dd ON (cps.DM_Document_ID = dd.DM_Document_ID) "+
					    		"INNER JOIN C_ProjectLine cpl ON (dd.C_ProjectLine_ID = cpl.C_ProjectLine_ID) "+
					    		"WHERE cpr.C_Payment_ID=?";
					    
					    int IDAcountAB = DB.getSQLValue(getTrxName(), sqlAB,pay.get_ID());
						
						fact2.createLine(null,MAccount.get(getCtx(), IDAcountAB), getC_Currency_ID(), total.negate(), null);
					}
					//nuevos asientos de rendiciones y devengos
					
					//validacion devolucion de retencion //no va
					
					//String validDR = "SELECT COUNT(1) FROM PM_ProjectPay where pay_type = 'ER' and C_ProjectSchedule_ID = ?";
					//int cantDR = DB.getSQLValue(getTrxName(), validDR, pr.get_ValueAsInt("C_ProjectSchedule_ID"));
					
					//if (cantDR < 1)
					//{
					
						//inicio nueva logica detalle rendiciones
						String sqlDetR = "select abs(Coalesce(sum(pp.referenceamount), 0)) as referenceamount, abs(Coalesce(sum(pp.amt), 0)) as amt,pp.pay_type "+ 
							"from C_PaymentRequest pr "+
							"inner join C_ProjectSchedule cps on (pr.C_ProjectSchedule_id = cps.C_ProjectSchedule_id) "+ 
							"inner join PM_ProjectPay pp on (cps.C_ProjectSchedule_id = pp.C_ProjectSchedule_id) "+
							"where pp.pay_type is not null and pr. C_PaymentRequest_ID=? "+
							"group by pp.pay_type ";
					
						PreparedStatement pstmtPP = null;
						ResultSet rsPP = null;										
						int charge_ID = getValidCombination_ID(Doc.ACCTTYPE_Charge, as);
					
						try{
						
							pstmtPP = DB.prepareStatement(sqlDetR, getTrxName());
							pstmtPP.setInt(1,pr.get_ID());
							rsPP = pstmtPP.executeQuery();
						
							while(rsPP.next())
							{								
								BigDecimal amtRef = rsPP.getBigDecimal("referenceamount");
								if (amtRef == null)
									amtRef = Env.ZERO;
								
								BigDecimal amtP = rsPP.getBigDecimal("amt");
								if (amtP == null)
									amtP = Env.ZERO;
								
								String payT = rsPP.getString("pay_type");
								
								BigDecimal amtR = amtRef.add(amtP);
								
								int IDAcountD1 = 0;
								int IDAcountC1 = 0;
								
								
								if (amtR.compareTo(new BigDecimal("0.0")) > 0)
								{
									int marca = 0;								
									String desc2 = "";
									if (charge_ID == 0 && getAmount(Doc.AMTTYPE_Charge).compareTo(new BigDecimal("0.0")) == 0) 
									{
										marca= 1;
										String sqlChargeD = "SELECT CH_Expense_Acct FROM C_Charge_Acct WHERE C_Charge_ID=? AND C_AcctSchema_ID=?";
										charge_ID = DB.getSQLValue(getTrxName(),sqlChargeD,getC_Charge_ID(), 1000000);
									}
									
									
									if (payT.equalsIgnoreCase("R1"))
									{
										String sqlD1 = "SELECT C_Intermediation_Acct FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=?";
										IDAcountD1 = DB.getSQLValue(getTrxName(), sqlD1, 1000000);
																	
										String sqlC1 = "SELECT MAX(c_account_acct) FROM AD_Ref_List where value = ? and AD_Reference_ID=1000077";
										IDAcountC1 = DB.getSQLValue(getTrxName(), sqlC1,payT);
										
									}
									else if(payT.equalsIgnoreCase("D1")) 
									{
										IDAcountD1 = charge_ID;
																	
										String sqlC2 = "SELECT MAX(c_account_acct) FROM AD_Ref_List where value = ? and AD_Reference_ID=1000077";
										IDAcountC1 = DB.getSQLValue(getTrxName(), sqlC2,payT);
										
									}else
									{
																			
										IDAcountD1 = charge_ID;
																			
										String sqlC3 = "SELECT MAX(c_account_acct) FROM AD_Ref_List where value = ? and AD_Reference_ID=1000077";
										IDAcountC1 = DB.getSQLValue(getTrxName(), sqlC3,payT);
									}
									
									if (IDAcountC1 > 0)
									{
										String sqlDes = "SELECT MAX(description) FROM AD_Ref_List where value = ? and AD_Reference_ID=1000077";
										desc2 = DB.getSQLValueString(getTrxName(), sqlDes , payT);
										
										fl = fact.createLine(null,MAccount.get(getCtx(), IDAcountD1), getC_Currency_ID(), amtR, null);
										fl.set_CustomColumn("GoreType", payT);									
										fl.setDescription(desc2);	
										
										fl = fact.createLine(null,MAccount.get(getCtx(), IDAcountC1), getC_Currency_ID(),null, amtR);
										fl.set_CustomColumn("GoreType", payT);									
										fl.setDescription(desc2);	
									}	
									
									if (marca == 1 && getAmount(Doc.AMTTYPE_Charge).compareTo(new BigDecimal("0.0")) == 0)
									{
										charge_ID = 0;
									}
								}
							}
							
							//asiento de devengo
						
							//Devengo multas					
							String sqlamtDV = "select abs(Coalesce(sum(pp.referenceamount), 0)) + abs(Coalesce(sum(pp.amt), 0)) "+ 
									"from C_PaymentRequest pr "+ 
									"inner join C_ProjectSchedule cps on (pr.C_ProjectSchedule_id = cps.C_ProjectSchedule_id) "+
									"left join PM_ProjectPay pp on (cps.C_ProjectSchedule_id = pp.C_ProjectSchedule_id) "+
									"where pr.c_paymentrequest_id = ?  and pay_type like 'D1'";
							
							BigDecimal amtDV = DB.getSQLValueBD(getTrxName(), sqlamtDV, pr.get_ID()) ;
							
							int IDAcountDV2 = 0;
							
							if (amtDV == null)
								amtDV = Env.ZERO;
							
							if (amtDV.compareTo(new BigDecimal("0.0")) > 0)
							{
								
								String sqlDV2 = "SELECT MAX(c_account_acct) FROM AD_Ref_List where value = 'D1' and AD_Reference_ID=1000077";
								IDAcountDV2 = DB.getSQLValue(getTrxName(), sqlDV2);
								
								fl=fact.createLine(null,MAccount.get(getCtx(), IDAcountDV2), getC_Currency_ID(), amtDV, null);
								fl.set_CustomColumn("GoreType", "DM");
								fl.setDescription("Devengo Multa");
								
								String sqlCV2 = "SELECT C_OtherRevenue_Acct FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=?";
								int IDAcountCV2 = DB.getSQLValue(getTrxName(), sqlCV2, 1000000);
																	
								fl=fact.createLine(null,MAccount.get(getCtx(), IDAcountCV2), getC_Currency_ID(),null, amtDV);
								fl.set_CustomColumn("GoreType", "DM");
								fl.setDescription("Devengo Multa");
							}
							
						}catch (SQLException e)
						{
							log.log(Level.SEVERE, sqlDetR, e);
						}
						finally {
							DB.close(rsPP, pstmtPP);
							rsPP = null; pstmtPP = null;
						}
						//	fin logica detalle rendiciones					
					//}
			 	}
			}
			else
			{
				p_Error = "DocumentType unknown: " + getDocumentType();
				log.log(Level.SEVERE, p_Error);
				fact = null;
			}
			
		}
		else 
		{
			
			FactLine fl2 = null;
									
			int AD_Org_ID = getBank_Org_ID();		//	Bank Account Org	
			
			
			AD_Org_ID=getPO().getAD_Org_ID();//faaguilar OFB			 
		
			if (getDocumentType().equals(DOCTYPE_ARReceipt))
			{
				//	Asset
				FactLine fl=null;
				if("K".equals(m_TenderType)){ //para cheques
			    	fl =fact.createLine(null, getAccount(Doc.ACCTTYPE_CheckInTransit, as),
			    		getC_Currency_ID(), getAmount(), null);			
			    	fl2 = fl;
				}	
				else if("L".equals(m_TenderType)) //para letras
				{
			 	  	fl =fact.createLine(null, getAccount(Doc.ACCTTYPE_LetrasInTransit, as),
			 			  getC_Currency_ID(), getAmount(), null);
			 	  	fl2 = fl;
				}
				else if("V".equals(m_TenderType)) //para vale vista
				{
			 	  	fl =fact.createLine(null, getAccount(Doc.ACCTTYPE_VistaInTransit, as),
			 			  getC_Currency_ID(), getAmount(), null);
			 	  	fl2 = fl;
				}
				else if("P".equals(m_TenderType)) //para pagare
				{
			 	  	fl =fact.createLine(null, getAccount(Doc.ACCTTYPE_PagareInTransit, as),
			 			  getC_Currency_ID(), getAmount(), null);
			 	  	fl2 = fl;
				}
				else if("H".equals(m_TenderType))//canje
				{					
					fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_ExchangeInTransit, as),
						getC_Currency_ID(), null, getAmount());
					fl2 = fl;
				}
				else if("B".equals(m_TenderType))//vale personal
				{					
					fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_PersonalVoucherInTransit, as),
						getC_Currency_ID(), null, getAmount());
					fl2 = fl;
				}
				else{
				fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_BankInTransit, as),
					 getC_Currency_ID(), getAmount(), null);
					fl2 = fl;
				}
				if (fl != null && AD_Org_ID != 0)
					fl.setAD_Org_ID(AD_Org_ID);
			//	
				MAccount acct = null;
				if (getC_Charge_ID() != 0)
					acct = MCharge.getAccount(getC_Charge_ID(), as, getAmount());				
				else if (m_Prepayment)
					acct = getAccount(Doc.ACCTTYPE_C_Prepayment, as);
				else
					acct = getAccount(Doc.ACCTTYPE_UnallocatedCash, as);
				
				fl = fact.createLine(null, acct,
					getC_Currency_ID(), null, getAmount());
				if (fl != null && AD_Org_ID != 0
						&& getC_Charge_ID() == 0)		//	don't overwrite charge
						fl.setAD_Org_ID(AD_Org_ID);
			}
			//  APP
			else if (getDocumentType().equals(DOCTYPE_APPayment))
			{
				MAccount acct = null;
				FactLine fl=null;
				//faaguilar OFB custom tender begin
							 					
				if("K".equals(m_TenderType)){ //cheques
			     	acct = getAccount(Doc.ACCTTYPE_CheckToPay, as);
			     	fl = fact.createLine(null, acct,
							getC_Currency_ID(), null, getAmount());		
			     	fl2 = fl;			     	
				}	
				else if("L".equals(m_TenderType))//letras
				{
					acct = getAccount(Doc.ACCTTYPE_LetrasToPay, as);
					fl = fact.createLine(null, acct,
						getC_Currency_ID(), null, getAmount());
					fl2 = fl;
				}
				else if("V".equals(m_TenderType))//v vista
				{
					acct = getAccount(Doc.ACCTTYPE_VistaToPay, as);
					fl = fact.createLine(null, acct,
						getC_Currency_ID(), null, getAmount());
					fl2 = fl;
				}
				else if("P".equals(m_TenderType))//pagare
				{
					acct = getAccount(Doc.ACCTTYPE_PagareToPay, as);
					fl = fact.createLine(null, acct,
						getC_Currency_ID(), null, getAmount());
					fl2 = fl;
				}
				else if("H".equals(m_TenderType))//canje ininoles
				{
					acct = getAccount(Doc.ACCTTYPE_ExchangeToPay, as);
					fl = fact.createLine(null, acct,
						getC_Currency_ID(), null, getAmount());
					fl2 = fl;
				}
				else if("B".equals(m_TenderType))//vale personal ininoles
				{
					acct = getAccount(Doc.ACCTTYPE_PersonalVoucherToPay, as);
					fl = fact.createLine(null, acct,
						getC_Currency_ID(), null, getAmount());
					fl2 = fl;
				}
				else 
				{
					fl = fact.createLine(null, getAccount(Doc.ACCTTYPE_BankInTransit, as),
						getC_Currency_ID(), null, getAmount());		
					fl2 = fl;
					//ininoles modificacion de descripcion: tipo tarjeta o en blanco 	
				 	if("C".equals(m_TenderType)){ //Tarjeta de credito
				 		String cct = pay.getCreditCardType();
				 		String sqlcct = "SELECT Name FROM AD_Ref_List WHERE AD_Reference_ID = 149 AND value = ?";
				 		String cctName = DB.getSQLValueString(null, sqlcct, cct);
					    fl.setDescription(cctName);				    
					    fl.save();
					    fl2 = fl;
					}
				 	else
				 	{
				 		log.log(Level.SEVERE,"ininoles "+fl.getDescription());
				 		fl.setDescription(null);
				 		log.log(Level.SEVERE,"ininoles "+fl.getDescription());
				 		fl.save();
				 		fl2 = fl;
				 	}
				}				
				
		    	if (fl != null && AD_Org_ID != 0)
					fl.setAD_Org_ID(AD_Org_ID);
			
		    
				if (getC_Charge_ID() != 0)
				{
					acct = MCharge.getAccount(getC_Charge_ID(), as, getAmount());				
					}
				else if (m_Prepayment)
					acct = getAccount(Doc.ACCTTYPE_V_Prepayment, as);
				else
					acct = getAccount(Doc.ACCTTYPE_PaymentSelect, as);
				//faaguilar OFB custom tender end
			 	fl = fact.createLine(null, acct,
			 			getC_Currency_ID(), getAmount(), null);
			 	if (fl != null && AD_Org_ID != 0
			 			&& getC_Charge_ID() == 0)		//	don't overwrite charge
			 		fl.setAD_Org_ID(AD_Org_ID);			
				//	Asset
			 	
			}
			else
			{
				p_Error = "DocumentType unknown: " + getDocumentType();
				log.log(Level.SEVERE, p_Error);
				fact = null;
			}
			
			fl2.save();
			
			client = new X_AD_Client(getCtx(), pay.getAD_Client_ID(), null);  
			
			if (client.getName().equalsIgnoreCase("Gobierno Regional Valparaíso"))
			{
				String descriptionAux = "--";
				
				if (pay.getDescription() == null)
					descriptionAux = "--";
			
				String insetC1 = "INSERT INTO  t_fact_acctgore (fact_acct_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
					"ad_table_id,record_id, c_bpartner_id,description,tipo,monto,datetrx,m_product_id,c_project_id) "+
					"VALUES ("+fl2.get_ID()+","+getAD_Client_ID()+","+getAD_Org_ID()+",'"+"Y"+"','"+
					pay.getCreated()+"',"+pay.getCreatedBy()+",'"+pay.getUpdated()+"',"+pay.getUpdatedBy()+","+
					pay.get_Table_ID()+","+pay.get_ID()+","+pay.getC_BPartner_ID()+",'"+descriptionAux+"','"+
					"CA"+"',"+pay.getPayAmt()+",'"+pay.getDateAcct()+"', 0 , 0 )";
					
				DB.executeUpdate(insetC1,getTrxName());
			}
			
			
		}
		
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		facts.add(fact2);
		return facts;
	}   //  createFact

	/**
	 * 	Get AD_Org_ID from Bank Account
	 * 	@return AD_Org_ID or 0
	 */
	private int getBank_Org_ID ()
	{
		if (m_C_BankAccount_ID == 0)
			return 0;
		//
		MBankAccount ba = MBankAccount.get(getCtx(), m_C_BankAccount_ID);
		return ba.getAD_Org_ID();
	}	//	getBank_Org_ID
	
}   //  Doc_Payment
