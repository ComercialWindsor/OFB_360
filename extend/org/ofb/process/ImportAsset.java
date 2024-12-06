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
package org.ofb.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.compiere.FA.CreateAssetForecast;
import org.compiere.model.MAssetAcct;
import org.compiere.model.MAssetChange;
import org.compiere.model.MRefList;
import org.compiere.model.X_A_Asset;
import org.compiere.model.X_A_Asset_Addition;
import org.compiere.model.X_A_Asset_Group_Acct;
import org.compiere.model.X_A_Depreciation_Workfile;
import org.compiere.model.X_I_Asset;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

/**
 *	Import Assets
 *
 * 	@author 	Fabian aguilar
 * 	@version 	$Id: ImportAsset.java,v 1.0 $
 */
public class ImportAsset extends SvrProcess 
{
	/**
	 * 	Import Asset
	 */
	public ImportAsset()
	{
		super();
		
	}	//	ImportAsset

	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Organization to be imported to	*/
	private int				m_AD_Org_ID = 0;
	/** Effective						*/
	private Timestamp	m_DateValue = null;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
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
			m_DateValue = new Timestamp (System.currentTimeMillis());
			//java.util.Date today = new java.util.Date();
			//m_DateValue =  new java.sql.Date(today.getTime());

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
			sql = new StringBuffer ("DELETE I_Asset "
				+ "WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());			
		}

		//	Set Client, Org, IaActive, Created/Updated, 	ProductType
		sql = new StringBuffer ("UPDATE I_Asset "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"			
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(sql.toString(),null);		


		//	set Group  a_asset_group_name
		sql = new StringBuffer ("UPDATE I_Asset i "
				+ "SET a_asset_group_id=(SELECT a_asset_group_id FROM a_asset_group c"
				+ " WHERE i.A_Asset_Group_Name=c.name AND i.AD_Client_ID=c.AD_Client_ID) "
				+ "WHERE A_Asset_Group_Name IS NOT NULL AND a_asset_group_id IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			log.info("Set Group=" + no);
		
			// set BP
		sql = new StringBuffer ("UPDATE I_Asset o "
					 + "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp"
					 + " WHERE o.BPartnerValue=bp.Value AND o.AD_Client_ID=bp.AD_Client_ID) "
					 + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
					 + " AND I_IsImported<>'Y'").append (clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			log.fine("Set BP from Value=" + no);
		
//			Invalid Group
			sql = new StringBuffer ("UPDATE I_Asset "
				+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Group,' "
				+ "WHERE a_asset_group_id IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (no != 0)
				log.warning("Invalid Category=" + no);
			
//			Invalid BP
			sql = new StringBuffer ("UPDATE I_Asset "
				+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid BP,' "
				+ "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (no != 0)
				log.warning("Invalid Category=" + no);
//			Mandatory Value
			sql = new StringBuffer ("UPDATE I_Asset i "
				+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=No Mandatory Value,' "
				+ "WHERE Value IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (no != 0)
				log.warning("No Mandatory Value=" + no);
			
		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;
		//int noInsertPO = 0;
		//int noUpdatePO = 0;

		//	Go through Records
		sql = new StringBuffer ("SELECT * "
			+ "FROM I_Asset WHERE I_IsImported='N'").append(clientCheck).append(" Order By I_Asset_ID");		    
//		Connection conn = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
		
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		try
		{			
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				
				X_I_Asset imp = new X_I_Asset(getCtx(),rs,get_TrxName() );
				X_A_Asset_Group_Acct assetgrpacct = null;
				String sqla ="SELECT * FROM A_Asset_Group_Acct WHERE A_Asset_Group_ID = ? AND IsActive='Y'";
				PreparedStatement pstmt1 = null;
				pstmt1 = DB.prepareStatement(sqla,get_TrxName());
				pstmt1.setInt(1, imp.getA_Asset_Group_ID());
				ResultSet rs2 = pstmt1.executeQuery();
				if (rs2.next())
					assetgrpacct = new X_A_Asset_Group_Acct (getCtx(), rs2,get_TrxName());
				
				//creacion del activo
				X_A_Asset asset = new X_A_Asset (getCtx(), 0, get_TrxName());
				asset.setAD_Org_ID(rs.getInt("AD_Org_ID"));
				if(imp.getAssetServiceDate()!=null)
					asset.setAssetServiceDate(imp.getAssetServiceDate());
				else
					asset.setAssetServiceDate(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				asset.setIsOwned(true);
				asset.setIsDepreciated(true);
				if(imp.getAssetServiceDate()!=null)
					asset.setA_Asset_CreateDate(imp.getAssetServiceDate());
				else
					asset.setA_Asset_CreateDate(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				asset.setIsInPosession(true);									
			    asset.setName(imp.getName());									
				asset.setHelp(imp.getHelp());
				asset.setDescription(imp.getDescription());
				asset.setUseLifeMonths(assetgrpacct.getUseLifeMonths());
				asset.setUseLifeYears(assetgrpacct.getUseLifeYears());
				asset.setA_Asset_Group_ID(imp.getA_Asset_Group_ID());
				asset.setA_QTY_Current(Env.ONE);
				asset.setA_QTY_Original(Env.ONE);
				asset.setIsOwned(imp.isOwned());
				asset.setC_BPartner_ID(imp.getC_BPartner_ID());
				asset.save();
				
				//*******************
				
				//contabilidad del activo			
				MAssetAcct assetacct = new MAssetAcct (getCtx(), 0, get_TrxName());
				assetacct.setAD_Org_ID(imp.getAD_Org_ID());
				assetacct.setPostingType(assetgrpacct.getPostingType());
				assetacct.setA_Split_Percent(assetgrpacct.getA_Split_Percent());
				assetacct.setA_Depreciation_Conv_ID(assetgrpacct.getConventionType());
				assetacct.setA_Asset_ID(asset.getA_Asset_ID());
				assetacct.setA_Depreciation_ID(assetgrpacct.getDepreciationType());
				assetacct.setA_Asset_Spread_ID(assetgrpacct.getA_Asset_Spread_Type());
				assetacct.setA_Period_Start(1);	
				
				
					assetacct.setA_Period_End(imp.getA_Period_End());
					asset.setUseLifeYears(assetgrpacct.getUseLifeYears());
					asset.setUseLifeMonths(assetgrpacct.getUseLifeMonths());
					asset.setIsDepreciated(true);
					asset.setIsOwned(true);
					asset.save();
					
				assetacct.setA_Depreciation_Method_ID(assetgrpacct.getA_Depreciation_Calc_Type());
				assetacct.setA_Asset_Acct(assetgrpacct.getA_Asset_Acct());
				assetacct.setC_AcctSchema_ID(assetgrpacct.getC_AcctSchema_ID());
				assetacct.setA_Accumdepreciation_Acct(assetgrpacct.getA_Accumdepreciation_Acct());
				assetacct.setA_Depreciation_Acct(assetgrpacct.getA_Depreciation_Acct());
				assetacct.setA_Disposal_Revenue(assetgrpacct.getA_Disposal_Revenue());
				assetacct.setA_Disposal_Loss(assetgrpacct.getA_Disposal_Loss());
				assetacct.setA_Salvage_Value((BigDecimal)assetgrpacct.get_Value("A_Salvage_Value")); //add by faaguilar OFB
				assetacct.setA_Reval_Accumdep_Offset_Cur(assetgrpacct.getA_Reval_Accumdep_Offset_Cur());
				assetacct.setA_Reval_Accumdep_Offset_Prior(assetgrpacct.getA_Reval_Accumdep_Offset_Prior());
				assetacct.setA_Reval_Cal_Method(assetgrpacct.getA_Reval_Cal_Method());
				assetacct.setA_Reval_Cost_Offset(assetgrpacct.getA_Reval_Cost_Offset());
				assetacct.setA_Reval_Cost_Offset_Prior(assetgrpacct.getA_Reval_Cost_Offset_Prior());
				assetacct.setA_Reval_Depexp_Offset(assetgrpacct.getA_Reval_Depexp_Offset());
				assetacct.setA_Depreciation_Manual_Amount(assetgrpacct.getA_Depreciation_Manual_Amount());
				assetacct.setA_Depreciation_Manual_Period(assetgrpacct.getA_Depreciation_Manual_Period());
				assetacct.setA_Depreciation_Table_Header_ID(assetgrpacct.getA_Depreciation_Table_Header_ID());
				assetacct.setA_Depreciation_Variable_Perc(assetgrpacct.getA_Depreciation_Variable_Perc());
				assetacct.set_ValueOfColumn("Salvage_Value",assetgrpacct.get_ValueAsInt("Salvage_Value")); 
				assetacct.set_ValueOfColumn("Disposal_RevenueD_Acct",assetgrpacct.get_ValueAsInt("Disposal_RevenueD_Acct"));
				assetacct.set_ValueOfColumn("Disposal_RevenueC_Acct",assetgrpacct.get_ValueAsInt("Disposal_RevenueC_Acct"));
				assetacct.set_ValueOfColumn("Disposal_Loss_Acct",assetgrpacct.get_ValueAsInt("Disposal_Loss_Acct"));
				assetacct.set_ValueOfColumn("Disposal_Gain_Acct",assetgrpacct.get_ValueAsInt("Disposal_Gain_Acct"));
				assetacct.set_ValueOfColumn("AssetComplement_Acct",assetgrpacct.get_ValueAsInt("AssetComplement_Acct"));
				assetacct.setProcessing(false);
				assetacct.getAD_Client_ID();
				assetacct.save();
				//********************
				
				//MAssetChange change
				MAssetChange change = new MAssetChange (getCtx(), 0, get_TrxName());						
				change.setPostingType(assetacct.getPostingType());
				change.setA_Split_Percent(assetacct.getA_Split_Percent());
				change.setConventionType(assetacct.getA_Depreciation_Conv_ID());
				change.setA_Asset_ID(asset.getA_Asset_ID());								
				change.setDepreciationType(assetacct.getA_Depreciation_ID());
				change.setA_Asset_Spread_Type(assetacct.getA_Asset_Spread_ID());
				change.setA_Period_Start(assetacct.getA_Period_Start());
				change.setA_Period_End(assetacct.getA_Period_End());
				change.setIsInPosession(asset.isOwned());
				change.setIsDisposed(asset.isDisposed());
				change.setIsDepreciated(asset.isDepreciated());
				change.setIsFullyDepreciated(asset.isFullyDepreciated());					
				change.setA_Depreciation_Calc_Type(assetacct.getA_Depreciation_Method_ID());
				change.setA_Asset_Acct(assetacct.getA_Asset_Acct());
				change.setC_AcctSchema_ID(assetacct.getC_AcctSchema_ID());
				change.setA_Accumdepreciation_Acct(assetacct.getA_Accumdepreciation_Acct());
				change.setA_Depreciation_Acct(assetacct.getA_Depreciation_Acct());
				change.setA_Disposal_Revenue(assetacct.getA_Disposal_Revenue());
				change.setA_Disposal_Loss(assetacct.getA_Disposal_Loss());
				change.setA_Reval_Accumdep_Offset_Cur(assetacct.getA_Reval_Accumdep_Offset_Cur());
				change.setA_Reval_Accumdep_Offset_Prior(assetacct.getA_Reval_Accumdep_Offset_Prior());
				change.setA_Reval_Cal_Method(assetacct.getA_Reval_Cal_Method());
				change.setA_Reval_Cost_Offset(assetacct.getA_Reval_Cost_Offset());
				change.setA_Reval_Cost_Offset_Prior(assetacct.getA_Reval_Cost_Offset_Prior());
				change.setA_Reval_Depexp_Offset(assetacct.getA_Reval_Depexp_Offset());
				change.setA_Depreciation_Manual_Amount(assetacct.getA_Depreciation_Manual_Amount());
				change.setA_Depreciation_Manual_Period(assetacct.getA_Depreciation_Manual_Period());
				change.setA_Depreciation_Table_Header_ID(assetacct.getA_Depreciation_Table_Header_ID());
				change.setA_Depreciation_Variable_Perc(assetacct.getA_Depreciation_Variable_Perc());
				change.setA_Parent_Asset_ID(asset.getA_Parent_Asset_ID());
			    change.setChangeType("CRT");	
				change.setTextDetails(MRefList.getListDescription (getCtx(),"A_Update_Type" , "CRT"));		    
			    change.setIsInPosession(asset.isOwned());
				change.setIsDisposed(asset.isDisposed());
				change.setIsDepreciated(asset.isDepreciated());
				change.setIsFullyDepreciated(asset.isFullyDepreciated());
				change.setLot(asset.getLot());
				change.setSerNo(asset.getSerNo());
				change.setVersionNo(asset.getVersionNo());
			    change.setUseLifeMonths(asset.getUseLifeMonths());
			    change.setUseLifeYears(asset.getUseLifeYears());
			    change.setLifeUseUnits(asset.getLifeUseUnits());
			    change.setAssetDisposalDate(asset.getAssetDisposalDate());
			    change.setAssetServiceDate(asset.getAssetServiceDate());
			    change.setC_BPartner_Location_ID(asset.getC_BPartner_Location_ID());
			    change.setC_BPartner_ID(asset.getC_BPartner_ID());
			    change.setAssetValueAmt(imp.getA_Asset_Cost());
			    change.setA_Asset_CreateDate(asset.getA_Asset_CreateDate());
			    change.setAD_User_ID(asset.getAD_User_ID());
			    change.setC_Location_ID(asset.getC_Location_ID());
			    change.setA_QTY_Current(Env.ONE);
			    change.setA_QTY_Original(Env.ONE);
			    
			    change.save();
				//*********************
			    CreateAssetForecast.createForecast(asset, change, assetacct,get_TrxName());
			    
			    
			    //*********************
			    log.config("X_A_Depreciation_Workfile");
				X_A_Depreciation_Workfile assetwk = new X_A_Depreciation_Workfile (getCtx(), 0, get_TrxName());
				assetwk.setA_Asset_ID(asset.getA_Asset_ID());		
				assetwk.setA_Life_Period(assetgrpacct.getUseLifeMonths());
				assetwk.setA_Asset_Life_Years(assetgrpacct.getUseLifeYears());
				assetwk.setA_Asset_Cost(imp.getA_Asset_Cost());							
				assetwk.setA_QTY_Current(Env.ONE);
				assetwk.setIsDepreciated(assetgrpacct.isProcessing());
				assetwk.setPostingType(assetgrpacct.getPostingType());
				assetwk.setA_Accumulated_Depr(imp.getA_Accumulated_Depr());
				assetwk.setA_Period_Posted(0);
				assetwk.setA_Asset_Life_Current_Year(new BigDecimal (0.0));
				assetwk.setA_Curr_Dep_Exp(new BigDecimal (0.0));						
				assetwk.setA_QTY_Current(Env.ONE);
				assetwk.save();
				
				/*log.config("X_A_Asset_Addition");
				X_A_Asset_Addition assetadd = new X_A_Asset_Addition (getCtx(), 0, get_TrxName());
				assetadd.setA_Asset_ID(asset.getA_Asset_ID());
				assetadd.setAssetValueAmt(imp.getA_Asset_Cost());
				assetadd.setA_SourceType("IMP");
				assetadd.setA_CapvsExp("Cap");
				assetadd.setM_InOutLine_ID(rs.getInt("C_Invoice_ID"));				
				assetadd.setC_Invoice_ID(rs.getInt("C_Invoice_ID"));
				assetadd.setDocumentNo(Invoice.getDocumentNo());
				assetadd.setLine(InvoiceLine.getLine());
				assetadd.setDescription(InvoiceLine.getDescription());
				assetadd.setA_QTY_Current(InvoiceLine.getQtyEntered());
				assetadd.setPostingType(assetwk.getPostingType());
				assetadd.setA_QTY_Current(Env.ONE);
				assetadd.setAssetValueAmt(InvoiceLine.getLineNetAmt().divide(InvoiceLine.getQtyEntered(), BigDecimal.ROUND_HALF_EVEN));
				assetadd.save();*/
				
				imp.setA_Asset_ID(asset.getA_Asset_ID());
				imp.setI_IsImported(true);
				imp.save();
				noInsert ++;
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
		sql = new StringBuffer ("UPDATE I_Asset "
			+ "SET I_IsImported='N' WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(sql.toString(),get_TrxName());
		
		this.commitEx();
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@A_Asset_ID@: @Inserted@");
		
		return "";
	}	//	doIt

}	//	ImportAsset
