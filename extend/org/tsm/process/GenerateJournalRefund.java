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
package org.tsm.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MDocType;
import org.compiere.model.MGLCategory;
import org.compiere.model.MJournal;
import org.compiere.model.MJournalBatch;
import org.compiere.model.MJournalLine;
import org.compiere.model.MPeriod;
import org.compiere.model.X_TP_Refund;
import org.compiere.model.X_TP_RefundHeader;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: GenerateJournalRefund.java $
 */
public class GenerateJournalRefund extends SvrProcess
{
	private int				p_HR_Viatico_ID = 0; 
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 p_HR_Viatico_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_HR_Viatico_ID > 0)
		{
			X_TP_RefundHeader rHead = new X_TP_RefundHeader(getCtx(), p_HR_Viatico_ID, get_TrxName());						
			//se crea Batch
			MJournalBatch batch = new MJournalBatch(getCtx(),0,get_TrxName());
			batch.setAD_Org_ID(rHead.getAD_Org_ID());
			batch.setDescription("VIATICOS AUTOMATICO "+rHead.getDocumentDate()+" a "+rHead.getDateEnd());
			batch.setPostingType("A");
			batch.setC_DocType_ID(MDocType.getOfDocBaseType(getCtx(), "GLJ")[0].getC_DocType_ID());
			batch.setGL_Category_ID(MGLCategory.getDefault(getCtx(), MGLCategory.CATEGORYTYPE_Manual).getGL_Category_ID());
			batch.setDateAcct(new Timestamp(System.currentTimeMillis()));
			batch.setDateDoc(new Timestamp(System.currentTimeMillis()));
			batch.setC_Period_ID(MPeriod.getC_Period_ID(getCtx(),new Timestamp(System.currentTimeMillis()),rHead.getAD_Org_ID()));
			MClient client= MClient.get(getCtx(), rHead.getAD_Client_ID());
			batch.setC_Currency_ID(client.getC_Currency_ID());
			batch.set_CustomColumn("TP_RefundHeader_ID", rHead.get_ID());
			if(batch.save(get_TrxName()))
			{
				rHead.set_CustomColumn("GL_JournalBatch_ID",batch.get_ID());
				rHead.save(get_TrxName());
			}
			//calculamos cuenta credito
			int ID_Credito= DB.getSQLValue(get_TrxName(), "SELECT tp_refundCredit_Acct FROM C_AcctSchema_Default WHERE C_AcctSchema_ID = 1000000");
			
			String sql = "SELECT TP_Refund_ID FROM TP_Refund WHERE TP_RefundHeader_ID = ?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, rHead.get_ID());
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					X_TP_Refund refund = new X_TP_Refund(getCtx(), rs.getInt("TP_Refund_ID"), get_TrxName());
					//se crea diario
					MJournal journal = new MJournal(batch);					
					journal.setDescription("VIATICO DE "+refund.getC_BPartner().getName().toUpperCase());
					journal.setC_AcctSchema_ID(MAcctSchema.getClientAcctSchema(getCtx(), Env.getAD_Client_ID(getCtx()))[0].getC_AcctSchema_ID() );
					journal.setGL_Category_ID(MGLCategory.getDefault(getCtx(), MGLCategory.CATEGORYTYPE_Document).getGL_Category_ID() );
					journal.setC_ConversionType_ID(114);
					journal.set_CustomColumn("TP_Refund_ID", refund.get_ID());
					if(journal.save(get_TrxName()))
					{
						refund.set_CustomColumn("GL_Journal_ID",journal.get_ID());
						refund.save(get_TrxName());
					}
					
					//se generan lineas
					//linea debito
					BigDecimal amtRefund = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(amt) FROM TP_RefundLine WHERE TP_Refund_ID = ?", refund.get_ID());
					if(amtRefund == null)
						amtRefund = Env.ZERO;
					String nameAccount = "";
					if(refund.getType().compareTo("01") == 0)
						nameAccount = "tp_refundn_acct";
					else
						nameAccount = "tp_refunds_acct";
					int ID_Debito= DB.getSQLValue(get_TrxName(), "SELECT "+nameAccount+" FROM C_BP_Employee_Acct WHERE  C_BPartner_ID = ?", refund.getC_BPartner_ID());
					
					//linea debito
					MJournalLine line1= new MJournalLine(journal);
					line1.set_CustomColumn("C_Bpartner_ID", refund.getC_BPartner_ID());
					line1.setAmtSourceDr(amtRefund);
					line1.setAmtSourceCr(Env.ZERO);
					line1.setAmtAcct(amtRefund, Env.ZERO);
					line1.setC_ValidCombination_ID(ID_Debito);
					line1.save();
					
					//linea Credito
					MJournalLine line2= new MJournalLine(journal);
					line2.set_CustomColumn("C_Bpartner_ID", refund.getC_BPartner_ID());
					line2.setAmtSourceDr(Env.ZERO);
					line2.setAmtSourceCr(amtRefund);
					line2.setAmtAcct(Env.ZERO,amtRefund);
					line2.setC_ValidCombination_ID(ID_Credito);
					line2.save();
				}
			}
			catch (SQLException e)
			{
				throw new DBException(e, sql);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
		}	
		return "Procesado ";
	}	//	doIt
}
