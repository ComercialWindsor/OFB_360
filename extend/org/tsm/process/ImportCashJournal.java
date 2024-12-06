/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is                  Compiere  ERP & CRM  Business Solution
 * The Initial Developer of the Original Code is Jorg Janke  and ComPiere, Inc.
 *
 * Copyright (C) 2005 Robert KLEIN. robeklein@gmail.com * 
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.tsm.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.MCash;
import org.compiere.model.MCashLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 *	Import c_cash
 *
 * 	@author 	Italo Niñoles
 * 	@version 	$Id: ImportCashJournal.java,v 1.0 $
 */
public class ImportCashJournal extends SvrProcess 
{
	/**
	 * 	Import Asset
	 */
	public ImportCashJournal()
	{
		super();
		
	}	//	ImportAsset

	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Organization to be imported to	*/
//	private int				m_AD_Org_ID = 0;
	/** Effective						*/
//	private Timestamp	m_DateValue = null;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		/*ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = (para[i].getParameterAsInt());
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(para[i].getParameter());
			else
				log.info("ImportAsset.prepare - Unknown Parameter: " + name);
		}
			m_DateValue = new Timestamp (System.currentTimeMillis());*/
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE I_Cash "
				+ " WHERE I_IsImported='Y' ").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());			
		}
		// set ORG
		sql = new StringBuffer ("UPDATE I_Cash o "
					 + "SET AD_Org_ID=(SELECT MAX(AD_Org_ID) FROM AD_Org org"
					 + " WHERE o.OrgValue=org.Value AND o.AD_Client_ID=org.AD_Client_ID) "
					 + " WHERE AD_Org_ID IS NULL AND OrgValue IS NOT NULL"
					 + " AND I_IsImported<>'Y'").append (clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			log.fine("Set BP from Value=" + no);
		
		//	Set Client, Org, IaActive, Created/Updated, 	ProductType
		sql = new StringBuffer ("UPDATE I_Asset "
				+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),"
				+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
				+ " IsActive = COALESCE (IsActive, 'Y'),"			
				+ " CreatedBy = COALESCE (CreatedBy, 0),"
				+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
				+ " I_ErrorMsg = NULL,"
				+ " I_IsImported = 'N' "
				+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(sql.toString(),null);		
				// set BP
		sql = new StringBuffer ("UPDATE I_Cash o "
					 + "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp"
					 + " WHERE o.BPartnerValue=bp.Value AND o.AD_Client_ID=bp.AD_Client_ID) "
					 + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
					 + " AND I_IsImported<>'Y'").append (clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set BP from Value=" + no);
			
//			Invalid BP
		sql = new StringBuffer ("UPDATE I_Cash "
				+ "SET I_ErrorMsg=I_ErrorMsg||'ERR=Invalid BP,' "
				+ " WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
//		
		//int noInsert = 0;
		//int noUpdate = 0;
		//int noInsertPO = 0;
		//int noUpdatePO = 0;

		//	Go through Records
		sql = new StringBuffer ("SELECT * "
			+ " FROM I_Cash WHERE I_IsImported= 'N'").append(clientCheck).append(" Order By DocumentNo");		    
//		Connection conn = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
		String noOld = "";
		MCash cash = null;
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		try
		{			
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				if(noOld != rs.getString("DocumentNo"))//se crea cabecera
				{
					cash = new MCash(getCtx(), 0, get_TrxName());
					cash.setAD_Org_ID(rs.getInt("AD_Org_ID"));
					cash.setC_CashBook_ID(rs.getInt("C_CashBook_ID"));
					cash.setDescription(rs.getString("Description"));
					cash.setStatementDate(rs.getTimestamp("StatementDate"));
					cash.setDateAcct(rs.getTimestamp("DateAcct"));
					cash.save(get_TrxName());
					noOld = rs.getString("DocumentNo");
				}
				if(cash != null)
				{
					MCashLine cLine = new MCashLine(cash);
					cLine.set_CustomColumn("C_Bpartner_ID", rs.getInt("C_Bpartner_ID"));
					cLine.setDescription(rs.getString("DescriptionLine"));
					cLine.setCashType(rs.getString("CashType"));
					cLine.setC_Currency_ID(228);
					cLine.setAmount(rs.getBigDecimal("Amount"));
					cLine.set_CustomColumn("DocumentNo", rs.getString("DocumentNoLine"));					
					if(cLine.save(get_TrxName()))
					{
						DB.executeUpdate("UPDATE I_Cash SET C_Cash_ID="+cash.get_ID()+", C_CashLine_ID="+cLine.get_ID()+","
								+" Processed='Y', I_IsImported='Y' WHERE I_Cash_ID="+rs.getInt("I_Cash_ID"), get_TrxName());
					}
				}
				
			}
			
		}
		catch (SQLException e)
		{			
			throw new Exception ("ImportAsset3.doIt", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			pstmt = null;
		}
		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_Cash "
			+ "SET I_IsImported='N' WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(),get_TrxName());		
		this.commitEx();
		return "";
	}	//	doIt

}	//	ImportAsset
