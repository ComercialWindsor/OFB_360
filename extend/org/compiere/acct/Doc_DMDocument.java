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
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCostDetail;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.MElementValue;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MLandedCostAllocation;
import org.compiere.model.MPayment;
import org.compiere.model.MProduct;
import org.compiere.model.MTax;
import org.compiere.model.OFBProductCost;
import org.compiere.model.ProductCost;
import org.compiere.model.X_C_DocType;
import org.compiere.model.X_C_Project;
import org.compiere.model.X_C_ProjectOFB;
import org.compiere.model.X_C_ProjectType;
import org.compiere.model.X_C_ValidCombination;
import org.compiere.model.X_DM_Document;
import org.compiere.model.X_Fact_Acct;
import org.compiere.util.DB;
import org.compiere.util.Env;

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
public class Doc_DMDocument extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	public Doc_DMDocument(MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super (ass, X_DM_Document.class, rs, null, trxName);
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
		X_DM_Document dmd = new X_DM_Document(getCtx(), getPO().get_ID(), null);
		
		setDateDoc(dmd.getDateTrx());
		setDateAcct(dmd.getDateTrx());
		
		//setIsTaxIncluded(invoice.isTaxIncluded());
		//	Amounts
		setAmount(Doc.AMTTYPE_Gross, new BigDecimal("0.0"));
		setAmount(Doc.AMTTYPE_Net, new BigDecimal("0.0"));
		//setAmount(Doc.AMTTYPE_Charge, new BigDecimal("0.0"));
		
				
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
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		
		
		String dt = p_po.get_Value("DM_DocumentType").toString();
		if (!dt.equals("RS"))
			return facts;
		
		//faaguilar OFB validation not post begin
		X_C_DocType DocType= MDocType.getOfDocBaseType(getCtx(), DOCTYPE_DMResolution)[0];
			if(!DocType.get_ValueAsBoolean("Posted"))
				return facts;			
		//END faaguilar validation not post end
			
		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Budget);		

		//faaguilar OFB begin
		
		X_DM_Document DMDoc = new X_DM_Document(getCtx(), p_po.get_ID(),getTrxName());		
		
		
		//  Cash based accounting
		if (!as.isAccrual())
			return facts;

		//  DMR Resoluciond e asignación 
		if (getDocumentType().equals(DOCTYPE_DMResolution))
		{
		    String sql = "Select dm1.dm_document_id, dl.c_project_id, dl2.c_projectline_id, dl2.amt, dl2.m_product_id "+ 
		    		"from dm_document dm1 inner join dm_documentline dl on (dm1.dm_document_id = dl.dm_document_id) "+
		    		"inner join DM_Document dm2 on (dl.DM_DocumentRef_ID = dm2.DM_Document_ID) "+
		    		"inner join dm_documentline dl2 on (dm2.DM_Document_ID = dl2.DM_Document_ID) "+
		    		"where dm1.DM_Document_ID = ?";
			
		    PreparedStatement pstmt = null;
			ResultSet rs = null;
		    
			
			try
			{
				pstmt = DB.prepareStatement(sql, getTrxName());
				pstmt.setInt(1,p_po.get_ID());
				rs = pstmt.executeQuery();
				FactLine fl = null;
				
				while(rs.next())
				{
					X_C_Project pj = new X_C_Project(getCtx(), rs.getInt(2), getTrxName());
					X_C_ProjectType pt = new X_C_ProjectType(getCtx(), Integer.parseInt(pj.get_Value("C_ProjectType_ID").toString()), getTrxName());
					MProduct pro = new MProduct(getCtx(), rs.getInt(5), getTrxName());
					BigDecimal amt = new BigDecimal("0.0");
					amt = rs.getBigDecimal(4);
					
					//sql que trae la cuenta segun tipo de proyecto
					
					String sql2 = "";
					//if (pt.getHelp().equals("C33"))
					sql2 = "select MAX(P_Cir33_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
					//ininoles 09-12 ahora solo se va a tomar la cuenta que esta en el campo P_Cir33_Acct para las resoluciones y no discriminara por tipo de proyecto 
					/*else if (pt.getHelp().equals("S24"))
						sql2 = "select MAX(P_Sub24_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
					else if (pt.getHelp().equals("S31"))
						sql2 = "select MAX(P_Sub31_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
					else if (pt.getHelp().equals("S33FIC"))
						sql2 = "select MAX(P_Sub33FIC_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
					else if (pt.getHelp().equals("S33FRIL"))
						sql2 = "select MAX(P_Sub33FRIL_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";*/
					
					/*PreparedStatement pstmt2 = null;
					pstmt2 = DB.prepareStatement(sql2, getTrxName());
					pstmt2.setInt(1,pro.get_ID());
					pstmt2.setInt(2, as.get_ID());*/
					
					//cuenta debito
					int cvcID = DB.getSQLValue(getTrxName(), sql2, pro.get_ID(), as.get_ID());
					
					X_C_ValidCombination cvc = new X_C_ValidCombination(getCtx(), cvcID, getTrxName());
										
					//cuenta credito
					String alias = cvc.getAlias()+pro.get_ValueAsString("AccountNo")+".";							 
					alias = alias.trim();
					
					String anb = pj.get_ValueAsString("AccountNo");					
										
					String anS = anb;
					String aliaspro = alias + anS;
					aliaspro = aliaspro.trim();
										
					String sql3 = "select MAX(C_ValidCombination_ID) from C_ValidCombination where alias like ?";
					
					int cvcID3 = DB.getSQLValue(getTrxName(), sql3, aliaspro);
					
					if(cvcID3<=0)// crear cuenta
					{
						String sql4 = "select C_ElementValue_ID from C_ElementValue where Value = ?";
						int element_ID = DB.getSQLValue(getTrxName(), sql4, aliaspro);
						log.config("element_ID 1:"+ element_ID);
						if(element_ID<=0)//crear elememto
						{
							log.config("crear elemento");
							MElementValue element= new MElementValue(getCtx(), aliaspro, aliaspro, "Cuenta Proyecto " + pj.getName(),
									MElementValue.ACCOUNTTYPE_Expense, "N",
									false, false, getTrxName());
							element.setC_Element_ID(1000000);
						
							element.save();
							log.config("elemento guardado");
							element_ID=element.getC_ElementValue_ID();
							log.config("element_ID 2:"+ element_ID);
						}
						//crear combinacion
						MAccount newAccount = new MAccount (getCtx(), 0, getTrxName());
						newAccount.setAD_Org_ID(0);
						newAccount.setAlias(aliaspro);
						newAccount.setC_AcctSchema_ID(1000000);
						newAccount.setAccount_ID(element_ID);
						newAccount.save();
						
						cvcID3 = newAccount.getC_ValidCombination_ID();
						
					}
					
					DB.executeUpdate("Update C_ProjectLine set PJ_Item_Acct=" +cvcID3 + " Where C_Project_ID="+rs.getInt(2)+" and M_Product_ID="+rs.getInt(5), getTrxName());//actualiza linea cuenta proyecto
					//crea contabilidad
					 fl = fact.createLine(null,MAccount.get(getCtx(), cvcID), getC_Currency_ID(), amt.multiply(new BigDecimal("-1.0")), null);
					fact.createLine(null,MAccount.get(getCtx(),cvcID3), getC_Currency_ID(), amt, null);
					//fin crea contabilidad
					
					fl.save();
				
				}
				
				//inicio cabecera auxiliar
				String isACT = "N";
				if (DMDoc.isActive())
					isACT = "Y";
								
				String insetC = "INSERT INTO  t_fact_acctgore (fact_acct_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
						"ad_table_id,record_id, c_bpartner_id,description,tipo,monto,datetrx,m_product_id,c_project_id,documentno) "+
						"VALUES ("+fl.get_ID()+","+DMDoc.getAD_Client_ID()+","+DMDoc.getAD_Org_ID()+",'"+isACT+"','"+
						DMDoc.getCreated()+"',"+DMDoc.getCreatedBy()+",'"+DMDoc.getUpdated()+"',"+DMDoc.getUpdatedBy()+","+
						DMDoc.get_Table_ID()+","+DMDoc.get_ID()+",0,'"+DMDoc.getDescription()+"','"+
						"CAB"+"',"+DMDoc.getAmt()+",'"+DMDoc.getDateTrx()+"',0,0,'"+DMDoc.getDocumentNo()+"')";
				
				DB.executeUpdate(insetC,getTrxName());
				//fin cabecera auxiliar
								
				//inicio detalle auxiliar
				
				//Detalle
				String sqlAux1 = "SELECT dl.ad_client_id, dl.ad_org_id, dl.isactive, dl.created, dl.createdby, dl.updated, dl.updatedby, "+
						"0 as c_bpartner_id,coalesce(dr.description,'--') as description,'LIN' as tipo, dl.amt AS amt, "+ 
						"dr.datetrx, dr.documentno as documentno, dr.c_project_id,0 as M_Product_ID,0 as debito,0 as credito "+ 
						"FROM DM_Document doc "+
						"LEFT JOIN DM_DocumentLine dl ON (doc.DM_Document_ID = dl.DM_Document_ID) "+
						"LEFT JOIN DM_Document dr ON (dl.DM_DocumentRef_ID = dr.DM_Document_ID) "+
						"WHERE doc.DM_Document_ID=?";
				
				PreparedStatement pstmtaux = null;
				ResultSet rsaux = null;
					
				try{
					
					pstmtaux = DB.prepareStatement(sqlAux1, getTrxName());
					pstmtaux.setInt(1,DMDoc.get_ID());
					rsaux = pstmtaux.executeQuery();
					
					while(rsaux.next())
					{
						String inset = "INSERT INTO  t_fact_acctgore (fact_acct_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby, "+
								"ad_table_id,record_id, c_bpartner_id,description,tipo,monto,documentno,datetrx,debito,credito,m_product_id,c_project_id) "+
								"VALUES ("+fl.get_ID()+","+rsaux.getInt("AD_Client_ID")+","+rsaux.getInt("ad_org_id")+",'"+rsaux.getString("isactive")+"','"+
								rsaux.getTimestamp("Created")+"',"+rsaux.getInt("CreatedBy")+",'"+rsaux.getTimestamp("Updated")+"',"+rsaux.getInt("UpdatedBy")+","+
								DMDoc.get_Table_ID()+","+DMDoc.get_ID()+","+rsaux.getInt("c_bpartner_id")+",'"+rsaux.getString("description")+"','"+
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
				
			
			}catch (SQLException e)
				{
					log.log(Level.SEVERE, sql, e);
				}
				finally {
					DB.close(rs, pstmt);
					rs = null; pstmt = null;
				}
												
		}
		
		else
		{
			p_Error = "DocumentType unknown: " + getDocumentType();
			log.log(Level.SEVERE, p_Error);
			fact = null;
		}
		//
		facts.add(fact);		
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
