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
import org.compiere.model.MClientInfo;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCostDetail;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MJournalLine;
import org.compiere.model.MLandedCostAllocation;
import org.compiere.model.MPayment;
import org.compiere.model.MProduct;
import org.compiere.model.MTax;
import org.compiere.model.OFBProductCost;
import org.compiere.model.ProductCost;
import org.compiere.model.X_AD_Client;
import org.compiere.model.X_C_DocType;
import org.compiere.model.X_C_PaymentRequest;
import org.compiere.model.X_C_PaymentRequestLine;
import org.compiere.model.X_C_Project;
import org.compiere.model.X_C_ProjectOFB;
import org.compiere.model.X_C_ProjectSchedule;
import org.compiere.model.X_C_ProjectType;
import org.compiere.model.X_C_ValidCombination;
import org.compiere.model.X_DM_Document;
import org.compiere.model.X_Fact_Acct;
import org.compiere.util.DB;
import org.compiere.util.Env;

import com.sfcommerce.jpaymentcomponent.ssl.Client;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Invoice (318)
 *  Document Types:     ARI, ARC, ARF, API, APC
 *  </pre>
 *  @author ininoles OFB
 *  
 *  @version  $Id: Doc_Invoice.java,v 1.2 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_PaymentRequest extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_PaymentRequest(MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, X_C_PaymentRequest.class, rs, null, trxName);
	}	//	Doc_Invoice

		
	/** Currency Precision				*/
	private int				m_precision = -1;
	/** All lines are Service			*/

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		X_C_PaymentRequest pr = new X_C_PaymentRequest(getCtx(), getPO().get_ID(), null);
		//X_C_ProjectSchedule ps = new X_C_ProjectSchedule(getCtx(), pr.get_ValueAsInt("C_ProjectSchedule_ID"), getTrxName());
		
		setDateDoc(pr.getDateTrx());
		setDateAcct(pr.getDateAcct());
		
		//setIsTaxIncluded(invoice.isTaxIncluded());
		//	Amounts
		setAmount(Doc.AMTTYPE_Gross, new BigDecimal("0.0"));
		setAmount(Doc.AMTTYPE_Net, new BigDecimal("0.0"));		
		//setAmount(Doc.AMTTYPE_Charge,ps.getDueAmt());
		setAmount(Doc.AMTTYPE_Charge, new BigDecimal("0.0"));
		
		m_C_BankAccount_ID = pr.getC_BankAccount_ID();				
		//	Contained Objects		
		//log.fine("Lines=" + p_lines.length + ", Taxes=" + m_taxes.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load Invoice Taxes
	 *  @return DocTax Array
	 */
	/**
	 *	Load Invoice Line
	 *	@param invoice invoice
	 *  @return DocLine Array
	 */
	
	/**
	 * 	Get Currency Precision
	 *	@return precision
	 */
	private int getStdPrecision()
	{
		if (m_precision == -1)
			m_precision = MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
		return m_precision;
	}	//	getPrecision
	
	private int			m_C_BankAccount_ID = 0;
	
	private int getBank_Org_ID ()
	{
		if (m_C_BankAccount_ID == 0)
			return 0;
		//
		MBankAccount ba = MBankAccount.get(getCtx(), m_C_BankAccount_ID);
		return ba.getAD_Org_ID();
	}	//	getBank_Org_ID

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;		
		//
		log.fine(toString() + " Balance=" + retValue);
		return retValue;
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  ARI, ARC, ARF, API, APC.
	 *  <pre>
	 *  ARI, ARF
	 *      Receivables     DR
	 *      Charge                  CR
	 *      TaxDue                  CR
	 *      Revenue                 CR
	 *
	 *  ARC
	 *      Receivables             CR
	 *      Charge          DR
	 *      TaxDue          DR
	 *      Revenue         RR
	 *
	 *  API
	 *      Payables                CR
	 *      Charge          DR
	 *      TaxCredit       DR
	 *      Expense         DR
	 *
	 *  APC
	 *      Payables        DR
	 *      Charge                  CR
	 *      TaxCredit               CR
	 *      Expense                 CR
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
	//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		Fact fact2 = new Fact(this, as, Fact.POST_Reservation);
		Fact fact3 = new Fact(this, as, Fact.POST_Budget);	
		
		ArrayList<Fact> facts = new ArrayList<Fact>();
		
		X_C_PaymentRequest pr = new X_C_PaymentRequest(getCtx(), getPO().get_ID(), null);		
		
		//ininoles validacion estados de pago anulados
		if (pr.getDocStatus().equals("VO"))
		{	
			facts.add(fact);
			facts.add(fact2);
			facts.add(fact3);
			return facts;
		}
		
		//se obtiene el nombre de la compañia
		X_AD_Client client = new X_AD_Client(getCtx(), pr.getAD_Client_ID(), null);  
		
		BigDecimal PayAmt = new BigDecimal("0.0");
		
		if (client.getName().equalsIgnoreCase("Gobierno Regional Valparaíso"))
		{
			X_C_ProjectSchedule ps = new X_C_ProjectSchedule(getCtx(), pr.get_ValueAsInt("C_ProjectSchedule_ID"), getTrxName());
			setAmount(Doc.AMTTYPE_Charge,ps.getDueAmt());		
			PayAmt = ps.getDueAmt();
		}
		
		
		
		//faaguilar OFB validation not post begin
		X_C_DocType DocType= MDocType.getOfDocBaseType(getCtx(), DOCTYPE_PaymentRequest)[0];
			if(!DocType.get_ValueAsBoolean("Posted"))
				return facts;			
		//END faaguilar validation not post end

		//ininoles OFB begin
		//desde contabilidad
		if 	(pr.getRequestType().equals("J"))
			return facts;
		//desde resolucion (proyecto para el gore)
		else if(pr.getRequestType().equals("R") && client.getName().equalsIgnoreCase("Gobierno Regional Valparaíso"))
		{
			//  Cash based accounting
			if (!as.isAccrual())
				return facts;
			
			//validacion "no aplicado"
			String sqlVNA ="SELECT COUNT(1) FROM AD_Ref_List where AD_Reference_ID=1000077 and NoAcct = 'Y' and value IN ( "+
					"select Pay_Type from PM_ProjectPay where C_ProjectSchedule_ID = ?)"; 
			
			int cantVNA = DB.getSQLValue(getTrxName(), sqlVNA,  pr.get_ValueAsInt("C_ProjectSchedule_ID"));
			
			if (cantVNA > 0)
				return facts;
			
			
			if(pr.get_ValueAsString("IsPrepayment").equals("false") || pr.get_ValueAsString("IsPrepayment").equals("N"))
			{
				if (getDocumentType().equals(DOCTYPE_PaymentRequest))
				{						
					FactLine fl=null;
					String desc = null;
					
					String sql = "select cpl.m_product_id from C_PaymentRequest cpr "+
						"inner join C_ProjectSchedule cps on (cpr.c_projectschedule_id = cps.c_projectschedule_id) "+
						"inner join DM_Document dmd on (cps.DM_Document_ID = dmd.DM_Document_ID) "+
						"inner join C_ProjectLine cpl on (dmd.C_ProjectLine_ID = cpl.C_ProjectLine_ID) "+
						"where cpr.C_PaymentRequest_ID=?";								
					try
					{
						//BigDecimal PayAmt =ps.getDueAmt() ;						
						//Cuenta debito
						int proID = DB.getSQLValue(getTrxName(), sql, pr.get_ID());
						
						MProduct pro = new MProduct(getCtx(), proID, getTrxName());						
																		
						String sql2 = "select MAX(P_default_acct) from M_Product_Acct where M_Product_ID=?";
						
						int cvcID = DB.getSQLValue(getTrxName(), sql2, pro.get_ID());
						
						desc = "";
						if (PayAmt.compareTo(new BigDecimal("0.0")) > 0)
						{
							fl = fact.createLine(null,MAccount.get(getCtx(), cvcID), getC_Currency_ID(), PayAmt, null);
							//ininoles descripcion tipo gore							
							if (client.getName().equalsIgnoreCase("Gobierno Regional Valparaíso"))
							{
								fl.setDescription("Estado de Pago");
							}
							//ininoles end
							fl.save();
						
						}						
						//fact2.createLine(null,MAccount.get(getCtx(), cvcID), getC_Currency_ID(), PayAmt, null);
						
						//ininoles inicio nueva cuenta de proyecto usada en resolucion de asignacion. programa 02
						//ahora no hay contabilidad presupuestaria en soliciutud de pago 25-10-2013
						/*int projectID = pr.get_ValueAsInt("C_Project_ID");
						X_C_Project pj = new X_C_Project(getCtx(), projectID, getTrxName());						
						X_C_ProjectType pt = new X_C_ProjectType(getCtx(), Integer.parseInt(pj.get_Value("C_ProjectType_ID").toString()), getTrxName());
						
						if (pt.getHelp().equals("C33"))
							sql2 = "select MAX(P_Cir33_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
						else if (pt.getHelp().equals("S24"))
							sql2 = "select MAX(P_Sub24_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
						else if (pt.getHelp().equals("S31"))
							sql2 = "select MAX(P_Sub31_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
						else if (pt.getHelp().equals("S33FIC"))
							sql2 = "select MAX(P_Sub33FIC_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
						else if (pt.getHelp().equals("S33FRIL"))
							sql2 = "select MAX(P_Sub33FRIL_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
						
						int cvcID2 = DB.getSQLValue(getTrxName(), sql2, pro.get_ID(), as.get_ID());*/
						/*X_C_ValidCombination cvc = new X_C_ValidCombination(getCtx(), cvcID2, getTrxName());
												
						String alias = cvc.getAlias() ;
						alias = alias.trim();
						String anb = pj.get_ValueAsString("AccountNo");						
						String aliaspro = alias + anb;
						aliaspro = aliaspro.trim();
						
						String sql3 = "select MAX(C_ValidCombination_ID) from C_ValidCombination where alias like ?";
						int cvcID3 = DB.getSQLValue(getTrxName(), sql3, aliaspro);*/
						
						/*
						int cvcID3 = DB.getSQLValue(getTrxName(),"select PJ_Item_Acct from C_ProjectLine where C_Project_ID =? and M_Product_ID=?",projectID,proID);
						if (cvcID3<=0)
						{
							p_Error = "Invalid Account: " + cvcID3;
							log.log(Level.SEVERE, p_Error);
							fact3 = null;							
						}
						//cuenta de proyecto
						fact3.createLine(null,MAccount.get(getCtx(),cvcID3), getC_Currency_ID(), PayAmt.multiply(new BigDecimal("-1.0")), null);
						//COntracuenta proyecto cuenta de nombre "Cuenta Cargo" en esquema contable
						String sql4 = "select ch_Expense_Acct from C_AcctSchema_Default where C_AcctSchema_ID=1000000 and ad_client_id = 1000000 ";
						int cvcID4 = DB.getSQLValue(getTrxName(), sql4);
						fact3.createLine(null,MAccount.get(getCtx(),cvcID4), getC_Currency_ID(), PayAmt, null);
						//fin programa 02*/
										
					}catch (Exception e)
						{
							log.log(Level.SEVERE, sql, e);
						}
					
					BigDecimal chargeamt = getAmount(Doc.AMTTYPE_Charge);
					
					//Cuenta credito
					int charge_ID = getValidCombination_ID(Doc.ACCTTYPE_Charge, as);
										
					//validacion si monto inversion es 0 no generar la linea sino manda error
					desc = "";
					if(chargeamt.compareTo(new BigDecimal("0.0")) > 0)
					{
						fl = fact.createLine(null, MAccount.get(getCtx(), charge_ID),getC_Currency_ID(), null, chargeamt);
						//ininoles descripcion tipo gore							
						if (client.getName().equalsIgnoreCase("Gobierno Regional Valparaíso"))
						{
							fl.setDescription("Estado de Pago");
						}
						//ininoles end				
						//fact2.createLine(null, MAccount.get(getCtx(), charge_ID),getC_Currency_ID(), null, chargeamt);
						fl.save();						
					}
					
					
					//inset a tabla de auxiliares gore con cuenta de cargo
					/*
					//cabecera
					String sqlAux1C = "SELECT cpr.ad_client_id, cpr.ad_org_id, cpr.isactive, cpr.created, cpr.createdby, cpr.updated, cpr.updatedby, "+
							"cpr.c_bpartner_id,coalesce(cps.description,'--') as description,'CAB' as tipo,dueamt AS amt, "+ 
							"cpr.datetrx, (SELECT M_Product_ID FROM C_ProjectLine cpl INNER JOIN DM_Document dm ON (cpl.C_ProjectLine_ID = dm.C_ProjectLine_ID) WHERE DM_Document_ID = cps.DM_Document_ID) as M_Product_ID "+
							"FROM c_paymentrequest cpr JOIN c_projectschedule cps ON cpr.c_projectschedule_id = cps.c_projectschedule_id "+ 
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
									"VALUES ("+fl.get_ID()+","+rsauxC.getInt("AD_Client_ID")+","+rsauxC.getInt("ad_org_id")+",'"+rsauxC.getString("isactive")+"','"+
									rsauxC.getTimestamp("Created")+"',"+rsauxC.getInt("CreatedBy")+",'"+rsauxC.getTimestamp("Updated")+"',"+rsauxC.getInt("UpdatedBy")+","+
									pr.get_Table_ID()+","+pr.get_ID()+","+rsauxC.getInt("c_bpartner_id")+",'"+rsauxC.getString("description")+"','"+
									rsauxC.getString("tipo")+"',"+rsauxC.getBigDecimal("amt")+",'"+rsauxC.getTimestamp("datetrx")+"',"+
									rsauxC.getInt("M_Product_ID")+","+pr.get_ValueAsInt("C_Project_ID")+")";														
							
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
										
					//Detalle
					String sqlAux1 = "SELECT cpr.ad_client_id, cpr.ad_org_id, cpr.isactive, cpr.created, cpr.createdby, cpr.updated, cpr.updatedby, "+ 
							"cpr.c_bpartner_id,coalesce(pp.description,'--') as description,pp.pay_type as tipo2,'LIN' as tipo,abs(pp.amt + pp.referenceamount) AS amt, "+
							"CASE "+ 
							"WHEN cil.documentno is null THEN coalesce(dm.documentno,'--') "+
							"WHEN cil.documentno is not null THEN coalesce(cil.documentno,'--') "+
							"ELSE coalesce(cil.documentno,'--') "+
							"END as documentno,  "+
							"0 as c_validcombination_id,cpr.datetrx,0 as debito,0 as credito, "+ 
							"(SELECT M_Product_ID FROM C_ProjectLine cpl INNER JOIN DM_Document dm ON (cpl.C_ProjectLine_ID = dm.C_ProjectLine_ID) "+ 
							"WHERE DM_Document_ID = cps.DM_Document_ID) as M_Product_ID "+ 
							"FROM c_paymentrequest cpr "+ 
							"INNER JOIN c_projectschedule cps ON (cpr.c_projectschedule_id = cps.c_projectschedule_id) "+
							"INNER JOIN pm_projectpay pp ON (cps.c_projectschedule_id = pp.c_projectschedule_id) "+
							"LEFT JOIN c_invoice cil ON (pp.c_invoice_id = cil.c_invoice_id) "+
							"LEFT JOIN DM_Document dm ON (pp.DM_Document_ID = dm.DM_Document_ID) "+
							"WHERE cpr.C_PaymentRequest_ID= ? ";
					
					PreparedStatement pstmtaux = null;
					ResultSet rsaux = null;
						
					try{
						
						pstmtaux = DB.prepareStatement(sqlAux1, getTrxName());
						pstmtaux.setInt(1,pr.get_ID());
						rsaux = pstmtaux.executeQuery();
						
						while(rsaux.next())
						{
							String inset = "INSERT INTO  t_fact_acctgore (fact_acct_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
									"ad_table_id,record_id, c_bpartner_id,description,tipo,tipo2,monto,documentno,c_validcombination_id,datetrx,debito,credito,m_product_id,c_project_id) "+
									"VALUES ("+fl.get_ID()+","+rsaux.getInt("AD_Client_ID")+","+rsaux.getInt("ad_org_id")+",'"+rsaux.getString("isactive")+"','"+
									rsaux.getTimestamp("Created")+"',"+rsaux.getInt("CreatedBy")+",'"+rsaux.getTimestamp("Updated")+"',"+rsaux.getInt("UpdatedBy")+","+
									pr.get_Table_ID()+","+pr.get_ID()+","+rsaux.getInt("c_bpartner_id")+",'"+rsaux.getString("description")+"','"+
									rsaux.getString("tipo")+"','"+rsaux.getString("tipo2")+"',"+rsaux.getBigDecimal("amt")+",'"+rsaux.getString("documentno")+"',"+
									rsaux.getInt("c_validcombination_id")+",'"+rsaux.getTimestamp("datetrx")+"',"+rsaux.getBigDecimal("debito")+","+rsaux.getBigDecimal("credito")+","+
									rsaux.getInt("M_Product_ID")+","+pr.get_ValueAsInt("C_Project_ID")+")";														
							
							DB.executeUpdate(inset,getTrxName());
						}
						
					}catch (SQLException e)
					{
						log.log(Level.SEVERE, sqlAux1, e);
					}
					finally {
						DB.close(rsaux, pstmtaux);
						rsaux = null; pstmtaux = null;
					}*/
					
					//end insert 
					
					//asientos de devengo, multas, retencion
					/*
					//Retenciones					
					String sqlamtR = "select abs(Coalesce(sum(pp.referenceamount), 0)) + abs(Coalesce(sum(pp.amt), 0)) "+ 
							"from C_PaymentRequest pr "+ 
							"inner join C_ProjectSchedule cps on (pr.C_ProjectSchedule_id = cps.C_ProjectSchedule_id) "+
							"left join PM_ProjectPay pp on (cps.C_ProjectSchedule_id = pp.C_ProjectSchedule_id) "+
							"where pr.c_paymentrequest_id = ?  and pay_type like 'R1'";
				
					BigDecimal amtR = DB.getSQLValueBD(getTrxName(), sqlamtR, pr.get_ID());
					
					if (amtR == null)
						amtR = Env.ZERO;
					
					if (amtR.compareTo(new BigDecimal("0.0")) > 0)
					{
				
						String sqlD1 = "SELECT C_Intermediation_Acct FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=?";
						int IDAcountD1 = DB.getSQLValue(getTrxName(), sqlD1, 1000000);
					
						fl = fact.createLine(null,MAccount.get(getCtx(), IDAcountD1), getC_Currency_ID(), amtR, null);
						fl.set_CustomColumn("GoreType", "R1");
				
						String sqlC1 = "SELECT MAX(c_account_acct) FROM AD_Ref_List where value = 'R1' and AD_Reference_ID=1000077";
						int IDAcountC1 = DB.getSQLValue(getTrxName(), sqlC1);
															
						fl = fact.createLine(null,MAccount.get(getCtx(), IDAcountC1), getC_Currency_ID(),null, amtR);
						fl.set_CustomColumn("GoreType", "R1");
					
					}
					
					//Devengo multas					
					String sqlamtDM = "select abs(Coalesce(sum(pp.referenceamount), 0)) + abs(Coalesce(sum(pp.amt), 0)) "+ 
							"from C_PaymentRequest pr "+ 
							"inner join C_ProjectSchedule cps on (pr.C_ProjectSchedule_id = cps.C_ProjectSchedule_id) "+
							"left join PM_ProjectPay pp on (cps.C_ProjectSchedule_id = pp.C_ProjectSchedule_id) "+
							"where pr.c_paymentrequest_id = ?  and pay_type like 'D%'";
				
					BigDecimal amtDM = DB.getSQLValueBD(getTrxName(), sqlamtDM, pr.get_ID()) ;
					
					int IDAcountD2 = 0;
					
					if (amtDM == null)
						amtDM = Env.ZERO;
					
					if (amtDM.compareTo(new BigDecimal("0.0")) > 0)
					{
				
						String sqlD2 = "SELECT MAX(c_account_acct) FROM AD_Ref_List where value = 'D1' and AD_Reference_ID=1000077";
						IDAcountD2 = DB.getSQLValue(getTrxName(), sqlD2);
					
						fl=fact.createLine(null,MAccount.get(getCtx(), IDAcountD2), getC_Currency_ID(), amtDM, null);
						fl.set_CustomColumn("GoreType", "DM");
				
						String sqlC2 = "SELECT C_OtherRevenue_Acct FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=?";
						int IDAcountC2 = DB.getSQLValue(getTrxName(), sqlC2, 1000000);
															
						fl=fact.createLine(null,MAccount.get(getCtx(), IDAcountC2), getC_Currency_ID(),null, amtDM);
						fl.set_CustomColumn("GoreType", "DM");
					}
					
					//multas
										
					/if (amtDM.compareTo(new BigDecimal("0.0")) > 0)
					{
						//debito con cuenta del item
						fl = fact.createLine(null,MAccount.get(getCtx(), charge_ID), getC_Currency_ID(), amtDM, null);
						fl.set_CustomColumn("GoreType", "MU");
							
						//credito cuenta de la referencia multa
						fl = fact.createLine(null,MAccount.get(getCtx(), IDAcountD2), getC_Currency_ID(),null, amtDM);
						fl.set_CustomColumn("GoreType", "MU");
					}*/
					
					/*
					//inicio nueva logica detalle rendiciones
					String sqlDetR = "select abs(Coalesce(sum(pp.referenceamount), 0)) as referenceamount, abs(Coalesce(sum(pp.amt), 0)) as amt,pp.pay_type "+ 
							"from C_PaymentRequest pr "+
							"inner join C_ProjectSchedule cps on (pr.C_ProjectSchedule_id = cps.C_ProjectSchedule_id) "+ 
							"inner join PM_ProjectPay pp on (cps.C_ProjectSchedule_id = pp.C_ProjectSchedule_id) "+
							"where pp.pay_type is not null and pr. C_PaymentRequest_ID=? "+
							"group by pp.pay_type ";
					
					PreparedStatement pstmtPP = null;
					ResultSet rsPP = null;
					
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
									fl = fact.createLine(null,MAccount.get(getCtx(), IDAcountD1), getC_Currency_ID(), amtR, null);
									fl.set_CustomColumn("GoreType", payT);									
								
									fl = fact.createLine(null,MAccount.get(getCtx(), IDAcountC1), getC_Currency_ID(),null, amtR);
									fl.set_CustomColumn("GoreType", payT);
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
					
							String sqlCV2 = "SELECT C_OtherRevenue_Acct FROM C_AcctSchema_Default WHERE C_AcctSchema_ID=?";
							int IDAcountCV2 = DB.getSQLValue(getTrxName(), sqlCV2, 1000000);
																
							fl=fact.createLine(null,MAccount.get(getCtx(), IDAcountCV2), getC_Currency_ID(),null, amtDV);
							fl.set_CustomColumn("GoreType", "DM");
						}
						
					}catch (SQLException e)
					{
						log.log(Level.SEVERE, sqlDetR, e);
					}
					finally {
						DB.close(rsPP, pstmtPP);
						rsPP = null; pstmtPP = null;
					}
					//fin logica detalle rendiciones
					 */
					
					//inset a tabla de auxiliares gore con la ultima cuenta guardada
					//para asignar ID de fact_acct
					fl.save();
					
					//cabecera
					String sqlAux1C = "SELECT cpr.ad_client_id, cpr.ad_org_id, cpr.isactive, cpr.created, cpr.createdby, cpr.updated, cpr.updatedby, "+
							"cpr.c_bpartner_id,coalesce(cps.description,'--') as description,'CAB' as tipo,dueamt AS amt, "+ 
							"cpr.datetrx, (SELECT M_Product_ID FROM C_ProjectLine cpl INNER JOIN DM_Document dm ON (cpl.C_ProjectLine_ID = dm.C_ProjectLine_ID) WHERE DM_Document_ID = cps.DM_Document_ID) as M_Product_ID "+
							"FROM c_paymentrequest cpr JOIN c_projectschedule cps ON cpr.c_projectschedule_id = cps.c_projectschedule_id "+ 
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
									"VALUES ("+fl.get_ID()+","+rsauxC.getInt("AD_Client_ID")+","+rsauxC.getInt("ad_org_id")+",'"+rsauxC.getString("isactive")+"','"+
									rsauxC.getTimestamp("Created")+"',"+rsauxC.getInt("CreatedBy")+",'"+rsauxC.getTimestamp("Updated")+"',"+rsauxC.getInt("UpdatedBy")+","+
									pr.get_Table_ID()+","+pr.get_ID()+","+rsauxC.getInt("c_bpartner_id")+",'"+rsauxC.getString("description")+"','"+
									rsauxC.getString("tipo")+"',"+rsauxC.getBigDecimal("amt")+",'"+rsauxC.getTimestamp("datetrx")+"',"+
									rsauxC.getInt("M_Product_ID")+","+pr.get_ValueAsInt("C_Project_ID")+")";														
							
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
										
					//Detalle
					String sqlAux1 = "SELECT cpr.ad_client_id, cpr.ad_org_id, cpr.isactive, cpr.created, cpr.createdby, cpr.updated, cpr.updatedby, "+ 
							"cpr.c_bpartner_id,coalesce(pp.description,'--') as description,pp.pay_type as tipo2,'LIN' as tipo,abs(pp.amt + pp.referenceamount) AS amt, "+
							"CASE "+ 
							"WHEN cil.documentno is null THEN coalesce(dm.documentno,'--') "+
							"WHEN cil.documentno is not null THEN coalesce(cil.documentno,'--') "+
							"ELSE coalesce(cil.documentno,'--') "+
							"END as documentno,  "+
							"0 as c_validcombination_id,cpr.datetrx,0 as debito,0 as credito, "+ 
							"(SELECT M_Product_ID FROM C_ProjectLine cpl INNER JOIN DM_Document dm ON (cpl.C_ProjectLine_ID = dm.C_ProjectLine_ID) "+ 
							"WHERE DM_Document_ID = cps.DM_Document_ID) as M_Product_ID "+ 
							"FROM c_paymentrequest cpr "+ 
							"INNER JOIN c_projectschedule cps ON (cpr.c_projectschedule_id = cps.c_projectschedule_id) "+
							"INNER JOIN pm_projectpay pp ON (cps.c_projectschedule_id = pp.c_projectschedule_id) "+
							"LEFT JOIN c_invoice cil ON (pp.c_invoice_id = cil.c_invoice_id) "+
							"LEFT JOIN DM_Document dm ON (pp.DM_Document_ID = dm.DM_Document_ID) "+
							"WHERE cpr.C_PaymentRequest_ID= ? ";
					
					PreparedStatement pstmtaux = null;
					ResultSet rsaux = null;
						
					try{
						
						pstmtaux = DB.prepareStatement(sqlAux1, getTrxName());
						pstmtaux.setInt(1,pr.get_ID());
						rsaux = pstmtaux.executeQuery();
						
						while(rsaux.next())
						{
							String inset = "INSERT INTO  t_fact_acctgore (fact_acct_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
									"ad_table_id,record_id, c_bpartner_id,description,tipo,tipo2,monto,documentno,c_validcombination_id,datetrx,debito,credito,m_product_id,c_project_id) "+
									"VALUES ("+fl.get_ID()+","+rsaux.getInt("AD_Client_ID")+","+rsaux.getInt("ad_org_id")+",'"+rsaux.getString("isactive")+"','"+
									rsaux.getTimestamp("Created")+"',"+rsaux.getInt("CreatedBy")+",'"+rsaux.getTimestamp("Updated")+"',"+rsaux.getInt("UpdatedBy")+","+
									pr.get_Table_ID()+","+pr.get_ID()+","+rsaux.getInt("c_bpartner_id")+",'"+rsaux.getString("description")+"','"+
									rsaux.getString("tipo")+"','"+rsaux.getString("tipo2")+"',"+rsaux.getBigDecimal("amt")+",'"+rsaux.getString("documentno")+"',"+
									rsaux.getInt("c_validcombination_id")+",'"+rsaux.getTimestamp("datetrx")+"',"+rsaux.getBigDecimal("debito")+","+rsaux.getBigDecimal("credito")+","+
									rsaux.getInt("M_Product_ID")+","+pr.get_ValueAsInt("C_Project_ID")+")";														
							
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
					
				}
			
				else
				{
					p_Error = "DocumentType unknown: " + getDocumentType();
					log.log(Level.SEVERE, p_Error);
					fact = null;
				
				}
		    }
			else if (pr.get_ValueAsString("IsPrepayment").equals("true") || pr.get_ValueAsString("IsPrepayment").equals("Y"))
			{
				
				if (getDocumentType().equals(DOCTYPE_PaymentRequest))
				{
					BigDecimal chargeamt = getAmount(Doc.AMTTYPE_Charge);
					
					String sql = "select MAX(C_IsPrePayment_Acct) from C_AcctSchema_Default "+
								"where C_AcctSchema_ID = ?";								
					try
					{
						//Cuenta debito
						int cacID = DB.getSQLValue(getTrxName(), sql, as.get_ID());
																
						fact.createLine(null,MAccount.get(getCtx(), cacID), getC_Currency_ID(), chargeamt, null);
																
					}catch (Exception e)
						{
							log.log(Level.SEVERE, sql, e);
						}
														
					//Cuenta credito
					
					
					int charge_ID = getValidCombination_ID(Doc.ACCTTYPE_Charge, as);
					
					fact.createLine(null, MAccount.get(getCtx(), charge_ID),getC_Currency_ID(), null, chargeamt);
										
													
				}
			
				else
				{
					p_Error = "DocumentType unknown: " + getDocumentType();
					log.log(Level.SEVERE, p_Error);
					fact = null;
				
				}
			}
		} 
		//desde factura
		else if (pr.getRequestType().equals("I") && client.getName().equalsIgnoreCase("Gobierno Regional Valparaíso"))
		{
			FactLine fl = null;
			
			String sql3 = "select MAX(combinacion_id1),sum(linetotalamt)  from RVOFB_RPaymentLine "+
							"where c_paymentrequest_id = ? group by cuenta_id1 ";
			
			 PreparedStatement pstmt = null;
			 ResultSet rs = null;
				
			try{
				
				pstmt = DB.prepareStatement(sql3, getTrxName());
				pstmt.setInt(1,pr.get_ID());
				rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					int cvc;					
					cvc = rs.getInt(1);
					BigDecimal amtProd = new BigDecimal("0.0");
					amtProd= rs.getBigDecimal(2);
					fact.createLine(null,MAccount.get(getCtx(),cvc), getC_Currency_ID(), amtProd, null);				
					
				}
				
			}catch (SQLException e)
			{
				log.log(Level.SEVERE, sql3, e);
			}
			finally {
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}	
									
			//Cuenta Credito
			String sqlCargo = "SELECT CH_Expense_Acct FROM C_Charge_Acct WHERE C_Charge_ID=? AND C_AcctSchema_ID=1000000"; 
				
			int charge2_ID= DB.getSQLValue(getTrxName(), sqlCargo, pr.getC_Charge_ID());
						
			fl = fact.createLine(null, MAccount.get(getCtx(), charge2_ID),getC_Currency_ID(), null, pr.getPayAmt());
			
			fl.save();
			
			//inicio cabecera auxiliar
			String isACT = "N";
			if (pr.isActive())
				isACT = "Y";
							
			String insetC = "INSERT INTO  t_fact_acctgore (fact_acct_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
					"ad_table_id,record_id, c_bpartner_id,description,tipo,monto,datetrx,m_product_id,c_project_id,documentno) "+
					"VALUES ("+fl.get_ID()+","+pr.getAD_Client_ID()+","+pr.getAD_Org_ID()+",'"+isACT+"','"+
					pr.getCreated()+"',"+pr.getCreatedBy()+",'"+pr.getUpdated()+"',"+pr.getUpdatedBy()+","+
					pr.get_Table_ID()+","+pr.get_ID()+","+pr.getC_BPartner_ID() +",'"+pr.getDescription()+"','"+
					"CAB"+"',"+pr.getPayAmt()+",'"+pr.getDateTrx()+"',0,0,'"+pr.getDocumentNo()+"')";
			
			DB.executeUpdate(insetC,getTrxName());
			//fin cabecera auxiliar
			
			//inicio detalle auxiliar
			
			//Detalle
			String sqlAux1 = "SELECT ci.ad_client_id, ci.ad_org_id, ci.isactive, ci.created, ci.createdby, ci.updated, ci.updatedby, "+
					"ci.c_bpartner_id, coalesce(PrintName,'--') as description ,'LIN' as tipo, ci.grandtotal AS amt, "+ 
					"ci.dateinvoiced as datetrx, ci.documentno as documentno, 0 as c_project_id,0 as M_Product_ID,0 as debito,0 as credito "+
					"FROM C_PaymentRequest pr "+
					"LEFT JOIN C_PaymentRequestLine prl ON (pr.C_PaymentRequest_ID = prl.C_PaymentRequest_ID) "+ 
					"LEFT JOIN C_Invoice ci ON (prl.C_Invoice_ID = ci.C_Invoice_ID) " +
					"LEFT JOIN C_DocType dt ON (ci.C_DocTypeTarget_ID = dt.C_DocType_ID) "+
					"WHERE pr.C_PaymentRequest_ID=?";
			
			PreparedStatement pstmtaux = null;
			ResultSet rsaux = null;
				
			try{
				
				pstmtaux = DB.prepareStatement(sqlAux1, getTrxName());
				pstmtaux.setInt(1,pr.get_ID());
				rsaux = pstmtaux.executeQuery();
				
				while(rsaux.next())
				{
					String inset = "INSERT INTO  t_fact_acctgore (fact_acct_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
							"ad_table_id,record_id, c_bpartner_id,description,tipo,monto,documentno,datetrx,debito,credito,m_product_id,c_project_id) "+
							"VALUES ("+fl.get_ID()+","+rsaux.getInt("AD_Client_ID")+","+rsaux.getInt("ad_org_id")+",'"+rsaux.getString("isactive")+"','"+
							rsaux.getTimestamp("Created")+"',"+rsaux.getInt("CreatedBy")+",'"+rsaux.getTimestamp("Updated")+"',"+rsaux.getInt("UpdatedBy")+","+
							pr.get_Table_ID()+","+pr.get_ID()+","+rsaux.getInt("c_bpartner_id")+",'"+rsaux.getString("description")+"','"+
							rsaux.getString("tipo")+"',"+rsaux.getBigDecimal("amt")+",'"+rsaux.getString("documentno")+"','"+
							rsaux.getTimestamp("datetrx")+"',"+rsaux.getBigDecimal("debito")+","+rsaux.getBigDecimal("credito")+","+
							rsaux.getInt("M_Product_ID")+","+rsaux.getInt("C_Project_ID")+")";														
					
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
			
			
			//fin detalle auxiliar
			
			
			//contabilidad 01
				
			//ininoles se saca cuenta de cargo presupuestaria.
			//fact2.createLine(null, MAccount.get(getCtx(), charge2_ID),getC_Currency_ID(), pr.getPayAmt(),null);
			
			String sqlcc = "select ch_Expense_Acct from C_AcctSchema_Default where C_AcctSchema_ID=1000000 and ad_client_id = 1000000 ";
			int cvcIDcc = DB.getSQLValue(getTrxName(), sqlcc);
						
			fact2.createLine(null,MAccount.get(getCtx(),cvcIDcc), getC_Currency_ID(),pr.getPayAmt().negate() ,null );
			
		}else if(pr.getRequestType().equals("P") && client.getName().equalsIgnoreCase("Grupo Geminis"))
		{
			MAccount acct = null;
			FactLine fl=null;
			
			int AD_Org_ID = getBank_Org_ID();
			
			String sqlDet = "SELECT C_PaymentRequestLine_ID FROM C_PaymentRequestLine WHERE C_PaymentRequest_ID=?";
		
			 PreparedStatement pstmtDet = null;
			 ResultSet rsDet = null;
				
			try{
			
				pstmtDet = DB.prepareStatement(sqlDet, getTrxName());
				pstmtDet.setInt(1,pr.get_ID());
				rsDet = pstmtDet.executeQuery();
				
				while(rsDet.next())
				{
					X_C_PaymentRequestLine prl = new X_C_PaymentRequestLine(getCtx(), rsDet.getInt(1), getTrxName());
					MJournalLine jl = new MJournalLine(getCtx(), prl.getGL_JournalLine_ID(), getTrxName());
					
					//acct = getAccount(jl.getC_ValidCombination_ID(), as);
					//acct = getAccount(Doc.ACCTTYPE_PaymentSelect, as);
					acct = MAccount.get (as.getCtx(), jl.getC_ValidCombination_ID());
					fl = fact.createLine(null, acct,
				 			getC_Currency_ID(),prl.getAmt(),null);
					log.config("acct:"+acct+jl.getC_ValidCombination_ID());
				}
				
				if (getC_Charge_ID() != 0)
				{
					acct = MCharge.getAccount(getC_Charge_ID(), as, pr.getPayAmt());				
				}				
				else
					acct = getAccount(Doc.ACCTTYPE_PaymentSelect, as);
				//faaguilar OFB custom tender end
			 	fl = fact.createLine(null, acct,
			 			getC_Currency_ID(), null, pr.getPayAmt());
			 	if (fl != null && AD_Org_ID != 0
			 			&& getC_Charge_ID() == 0)		//	don't overwrite charge
			 		fl.setAD_Org_ID(AD_Org_ID);
				
			}catch (SQLException e)
			{
				log.log(Level.SEVERE, sqlDet, e);
			}
		}
		
		//
		facts.add(fact);		
		facts.add(fact2);
		facts.add(fact3);
		return facts;
	  }   //  createFact
	
	
	/*public int getValidCombinationGORE_ID (String value) // prueba traer combinacion con metodo
	{
		String sql = "select C_ValidCombination_id from C_ValidCombination where alias like "+
				"'" + value + "'";
		
				
		return 0;
	}*/
	

	/**
	 * ininoles OFB
	 * custom method*/

}   //  

