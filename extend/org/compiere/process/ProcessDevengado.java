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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessDevengado.java,v 1.2 2008/06/12 00:51:01  $
 */
public class ProcessDevengado extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private String p_Type;
	
	private BigDecimal 			p_Rate;
	
	private int p_Currency_ID;
	
	private int p_Org_ID;
	
	private int  p_Period_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DevType"))
				p_Type = (String)para[i].getParameter();
			else if (name.equals("C_Currency_ID"))
				p_Currency_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Org_ID"))
				p_Org_ID = para[i].getParameterAsInt();
			/*else if (name.equals("Rate"))
				p_Rate	= (BigDecimal)para[i].getParameter();*/
			else if (name.equals("C_Period_ID"))
				p_Period_ID = para[i].getParameterAsInt();
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
		
		String sql ="";
		int count = 0;
		
		if(p_Type.equals("I") ) // invoice
			sql="select c.C_RECEIVABLE_ACCT as Account_ID,i.c_invoice_id,invoiceopen(i.c_invoice_id,null),currencyConvert(invoiceopen(i.c_invoice_id,null),i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID) as posted,doc.docbasetype" 
			+" from c_invoice i "
			+" inner join C_BP_CUSTOMER_ACCT c on (i.c_bpartner_id=c.c_bpartner_id) "
			+" inner join C_DocType doc on (i.c_doctype_id=doc.c_doctype_id) "
			+" where invoiceopen(c_invoice_id,null)>0 and i.issotrx='Y' and i.docstatus in ('CO','CL') and doc.docbasetype in ('ARI','ARC')"
			+" and i.c_currency_id =   ?   and (i.ad_org_id = ? OR i.AD_Org_ID in (Select oi.AD_ORG_ID from AD_orginfo oi where oi.PARENT_ORG_ID=?) ) "
			+" UNION "
			+" select c.V_LIABILITY_ACCT as Account_ID,c_invoice_id ,invoiceopen(i.c_invoice_id,null),currencyConvert(invoiceopen(i.c_invoice_id,null),i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID) as posted,doc.docbasetype "
			+" from c_invoice i "
			+" inner join C_BP_VENDOR_ACCT c on (i.c_bpartner_id=c.c_bpartner_id) "
			+" inner join C_DocType doc on (i.c_doctype_id=doc.c_doctype_id) "
			+" where invoiceopen(c_invoice_id,null)>0 and i.issotrx='N' and i.docstatus in ('CO','CL') and doc.docbasetype in ('API','APC')"
			+" and i.c_currency_id =   ?   and (i.ad_org_id = ? OR i.AD_Org_ID in (Select oi.AD_ORG_ID from AD_orginfo oi where oi.PARENT_ORG_ID=?) )";
		else //payment
			sql = "select c.C_Prepayment_Acct,i.c_payment_id, paymentAvailable(i.c_payment_id), currencyConvert(paymentAvailable(i.c_payment_id),i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID) as posted, i.isreceipt"
				+" from c_payment i "
				+" inner join C_BP_CUSTOMER_ACCT c on (i.c_bpartner_id=c.c_bpartner_id)"
				+" where paymentAvailable(i.c_payment_id)>0 and i.isreceipt='Y' and i.docstatus in ('CO','CL')"
				+" and c_currency_id =   ?   and (i.ad_org_id = ? OR i.AD_Org_ID in (Select oi.AD_ORG_ID from AD_orginfo oi where oi.PARENT_ORG_ID=?) )"
				+" union "
				+" select V_Prepayment_Acct,c_payment_id, paymentAvailable(c_payment_id), currencyConvert(paymentAvailable(c_payment_id),i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID) as posted, i.isreceipt"
				+" from c_payment i"
				+" inner join C_BP_VENDOR_ACCT c on (i.c_bpartner_id=c.c_bpartner_id)"
				+" where paymentAvailable(i.c_payment_id)>0 and i.isreceipt='N' and i.docstatus in ('CO','CL')"
				+" and c_currency_id =   ?   and (i.ad_org_id = ? OR i.AD_Org_ID in (Select oi.AD_ORG_ID from AD_orginfo oi where oi.PARENT_ORG_ID=?) )";	
		
		
		
		MOrg org = MOrg.get(getCtx(), p_Org_ID);
		if(org.get_ValueAsBoolean("IsCostCenter"))
			return "Debe seleccionar una Organizacion Padre";
		
		if(p_Currency_ID == 228)
			return "Debe seleccionar una Moneda Distinta a CLP";
		
		MPeriod current = MPeriod.get(getCtx(), p_Period_ID);
		p_Rate = MConversionRate.getRate(p_Currency_ID, 228, current.getEndDate(), 0, getAD_Client_ID(), p_Org_ID);
		if(p_Rate == null)
			return "no se encontro una tasa de cambio para lo moneda indicada en el ultimo dia del periodo";
		
		String msg=searchSame(p_Org_ID, current.getC_Period_ID(), p_Currency_ID);
		if(msg.length()>0)
			return msg;
		
		MPeriod after = getPeriodAnt(current);
			
		reverseBeforeBatch(p_Org_ID, after.getC_Period_ID());
		
		MAcctSchema acct = MAcctSchema.getClientAcctSchema(getCtx(), getAD_Client_ID(), get_TrxName())[0];
		MAcctSchemaGL acctgl = acct.getAcctSchemaGL();
		
		if(acctgl.get_ValueAsInt("CMDev_Acct")<=0)
			return "No se encuentra definida la cuenta para Diferencia de Cambio";
		
		MJournalBatch batch = null;
		MJournal journal = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, p_Currency_ID);
			pstmt.setInt(2, p_Org_ID);
			pstmt.setInt(3, p_Org_ID);
			pstmt.setInt(4, p_Currency_ID);
			pstmt.setInt(5, p_Org_ID);
			pstmt.setInt(6, p_Org_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				
				if(batch == null)
				{
					MClient client= MClient.get(getCtx(),Env.getAD_Client_ID(getCtx()));
					MAcctSchema as = MAcctSchema.getClientAcctSchema(getCtx(), Env.getAD_Client_ID(getCtx()))[0];
					
					batch = new MJournalBatch (getCtx(), 0, get_TrxName());
					batch.setClientOrg(getAD_Client_ID(),p_Org_ID);
					batch.setDescription("Devengado de " + (p_Type.equals("I")?"Facturas":"Pagos") );
					batch.setDateDoc(current.getEndDate());
					batch.setDateAcct(current.getEndDate());
					batch.setC_DocType_ID(MDocType.getOfDocBaseType(getCtx(), "GLJ")[0].getC_DocType_ID());
					batch.setGL_Category_ID(MGLCategory.getDefault(getCtx(), MGLCategory.CATEGORYTYPE_Manual).getGL_Category_ID());
					batch.setC_Period_ID(current.getC_Period_ID());
					batch.setC_Currency_ID(client.getC_Currency_ID());
					batch.set_ValueOfColumn("C_CurrencyFrom_ID", p_Currency_ID);
					batch.save();
					
					journal= new MJournal(batch);
					journal.setDescription("Devengado de " + (p_Type.equals("I")?"Facturas":"Pagos") );
					journal.setC_AcctSchema_ID(as.getC_AcctSchema_ID() );
					journal.setGL_Category_ID(MGLCategory.getDefault(getCtx(), MGLCategory.CATEGORYTYPE_Document).getGL_Category_ID() );
					journal.setC_ConversionType_ID(114);
					journal.save();
				}
				
				BigDecimal cambioActual = rs.getBigDecimal(3).multiply(p_Rate);
				BigDecimal posted = rs.getBigDecimal(4);
				String base = rs.getString(5);
				
				BigDecimal  credit = Env.ZERO;
				BigDecimal  debit = Env.ZERO;
				
				if(cambioActual.compareTo(posted)>0)
				{
					if(p_Type.equals("I") && ( base.equals("ARI") || base.equals("APC") ) )//sales invoices
						debit = cambioActual.subtract(posted);
					else if(p_Type.equals("I") && ( base.equals("API") || base.equals("ARC") ) )//vendor invoice
						credit = cambioActual.subtract(posted);
					else if( !p_Type.equals("I") &&  base.equals("Y") ) //recibo cliente
						credit = cambioActual.subtract(posted);
					else if( !p_Type.equals("I") &&  base.equals("N") ) //pago proveedor
						debit = cambioActual.subtract(posted);
				}
				else if(cambioActual.compareTo(posted)==0)
				{
					continue;
				}
				else//menor
				{
					if(p_Type.equals("I") && ( base.equals("ARI") || base.equals("APC") ) )//sales invoices
						credit = posted.subtract(cambioActual);
					else if(p_Type.equals("I") && ( base.equals("API") || base.equals("ARC") ) )//vendor invoice
						debit = posted.subtract(cambioActual);
					else if( !p_Type.equals("I") &&  base.equals("Y") ) //recibo cliente
						debit = cambioActual.subtract(posted);
					else if( !p_Type.equals("I") &&  base.equals("N") ) //pago proveedor
						credit = cambioActual.subtract(posted);
				}
				
				MJournalLine line1= new MJournalLine(journal); //linea cuenta doc
				line1.setAmtSourceDr(debit);
				line1.setAmtSourceCr(credit);
				line1.setAmtAcct(debit, credit);
				line1.setC_ValidCombination_ID(rs.getInt(1));
				if(p_Type.equals("I"))
					line1.set_CustomColumn("C_Invoice_ID", rs.getInt(2));
				else
					line1.set_CustomColumn("C_Payment_ID", rs.getInt(2));
				line1.save();
				
				MJournalLine line2= new MJournalLine(journal); // linea correccion monetaria
				line2.setAmtSourceDr(credit);
				line2.setAmtSourceCr(debit);
				line2.setAmtAcct(credit,debit);
				line2.setC_ValidCombination_ID(acctgl.get_ValueAsInt("CMDev_Acct")); 
				if(p_Type.equals("I"))
					line2.set_CustomColumn("C_Invoice_ID", rs.getInt(2));
				else
					line2.set_CustomColumn("C_Payment_ID", rs.getInt(2));
				line2.save();
					
				
				count ++ ;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		/*if(batch != null){
			 batch.completeIt();
			 batch.setProcessed(true);
			 batch.save();
		}*/
		 batch.save();
		
		if(count>0)
			return count + " Documentos Encontrados   Journal : " +batch.getDocumentNo();
		else
			return "Ningun documento encontrado";
		
	}	//	doIt


	public MPeriod getPeriodAnt(MPeriod actual){
		MPeriod retValue=null;
		PreparedStatement pstmt = null;
		String mysql="SELECT * from C_Period where C_Year_ID=? and PeriodNo=?";
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			if(actual.getPeriodNo()==1)
			{
				pstmt.setInt(1, actual.getC_Year_ID()-1);
				pstmt.setInt(2, 12);
			}
			else
			{
				pstmt.setInt(1, actual.getC_Year_ID());
				pstmt.setInt(2, actual.getPeriodNo()-1);
			}
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue= new MPeriod(getCtx(),rs , get_TrxName());
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return retValue;
	}
	
	public void reverseBeforeBatch(int Org_ID, int Period_ID){
		
		
		
		PreparedStatement pstmt = null;
		
		String mysql="SELECT * from GL_JournalBatch where C_Period_ID=? and AD_Org_ID=? and upper(description) like '%DEVENGADO DE "+ (p_Type.equals("I")?"FACTURAS":"PAGOS") +"%'";
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1, Period_ID);
			pstmt.setInt(2, Org_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				MJournalBatch batch = new MJournalBatch(getCtx(),rs , get_TrxName());
				if(searchReverso(Org_ID,batch.getDocumentNo())>0)
					return;
				
				MJournal[] journals = batch.getJournals(true);
//				Reverse it
				MJournalBatch reverse = new MJournalBatch (batch);
				reverse.setC_Period_ID(0);
				reverse.setDateDoc(new Timestamp(System.currentTimeMillis()));
				reverse.setDateAcct(reverse.getDateDoc());
				//	Reverse indicator
				String description = reverse.getDescription();
				if (description == null)
					description = "** Reverso" + batch.getDocumentNo() + " **";
				else
					description += " ** Reverso" + batch.getDocumentNo() + " **";
				reverse.setDescription(description);
				reverse.save();
//				Reverse Journals
				for (int i = 0; i < journals.length; i++)
				{
					MJournal journal = journals[i];
					if (!journal.isActive())
						continue;
					journal.reverseAccrualIt(reverse.getGL_JournalBatch_ID());
					journal.save();
				}
				
				batch.completeIt();
				batch.setProcessed(true);
				batch.save();
				
			}
			
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		
	}
	
	
	public int searchReverso(int Org_ID, String documentNo){
		
		PreparedStatement pstmt = null;
		int count = 0;
		String mysql="SELECT * from GL_JournalBatch where DocStatus IN ('DR','CO') and  AD_Org_ID=? and upper(description) like '%REVERSO% "+ documentNo +"%'";
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1, Org_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				count ++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return count;
		
	}

      public String searchSame(int Org_ID, int Period_ID, int Currency_ID){
		
		PreparedStatement pstmt = null;
		String msg ="";
		String mysql="SELECT * from GL_JournalBatch where DocStatus IN ('DR','CO') and C_Period_ID=? and AD_Org_ID=? and C_CurrencyFrom_ID=? and upper(description) like '%DEVENGADO DE "+ (p_Type.equals("I")?"FACTURAS":"PAGOS") +"%'";
		try
		{
			pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1, Period_ID);
			pstmt.setInt(2, Org_ID);
			pstmt.setInt(3, Currency_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				msg ="Ya existe un documento devengado para este periodo : " + rs.getString("DocumentNo");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return msg;
		
	}
	
}	//	CopyOrder
