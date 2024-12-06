/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.dpp.process;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.process.SvrProcess;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Special Process
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: DepreAssets.java,v 1.3 2006/02/24 07:15:43 jjanke Exp $
 */
public class DepreAssetsDefensoria extends SvrProcess
{

	/** Properties						*/
	private Properties 		m_ctx;
	/*Order ID*/
	private int 			Record_ID;
	
	X_A_Asset_Dep DepDoc = null;
	
	
	protected void prepare()
	{
	  
		m_ctx = Env.getCtx();
		Record_ID=getRecord_ID();
		DepDoc= new X_A_Asset_Dep(m_ctx,Record_ID,get_TrxName());
		
	}	//	prepare

	/**
	 * 	Proccess
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
	/*Timestamp datefrom=null;
	Ti*/
	
	
	
	String sql="select a.a_asset_id,a.name,a.value,acct.c_acctschema_id, acct.a_asset_acct, "
		+" acct.a_depreciation_acct, fore.amount,fore.a_asset_forecast_id,p.c_period_id,p.name,"
		+" acct.a_accumdepreciation_acct,acct.A_Disposal_Gain_Acct,wk.a_asset_cost,acct.A_Disposal_RevenueC_Acct,"
		+" acct.A_Disposal_RevenueD_Acct,acct.A_Disposal_Loss_Acct, wk.a_accumulated_depr, acct.A_AssetComplement_Acct"
		+" from a_asset a"
		+" inner join a_asset_acct acct on (a.a_asset_id=acct.a_asset_id)"
		+" inner join A_Depreciation_Workfile wk on (a.a_asset_id=wk.a_asset_id)"
		+" inner join a_asset_forecast fore on (a.a_asset_id=fore.a_asset_id)"
		+" inner join c_period p on (fore.datedoc between p.startdate and p.enddate)"
		+" where a.ad_client_id=? ";// and p.c_period_id=? ";
		
	
	if(DepDoc.isSameYear()){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//datefrom= new Timestamp(cal.getTimeInMillis());
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//dateto= new Timestamp(cal.getTimeInMillis());
	}else{
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)-1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//datefrom= new Timestamp(cal.getTimeInMillis());
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//dateto= new Timestamp(cal.getTimeInMillis());
	}
	String ID_grupo = "";
	try
	{
		int id_GrupoPadre = DepDoc.get_ValueAsInt("A_Asset_Group_Ref_ID");
		if (id_GrupoPadre > 0)
			ID_grupo = " a.A_Asset_Group_Ref_ID = "+DepDoc.get_ValueAsInt("A_Asset_Group_Ref_ID");
		else
			ID_grupo = " a.a_asset_group_id = "+DepDoc.getA_Asset_Group_ID();
	}
	catch(Exception e)
	{
		log.log(Level.SEVERE, sql.toString(), e);
		ID_grupo = " a.a_asset_group_id="+DepDoc.getA_Asset_Group_ID();
	}
	if (ID_grupo == null || ID_grupo == "")
		ID_grupo = " a.a_asset_group_id="+DepDoc.getA_Asset_Group_ID();
	
	if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Depreciacion )))
		sql+=" And p.c_period_id="+DepDoc.getC_Period_ID()+" And fore.processed='N' and a.isinposession='Y' And "+ID_grupo;
	
	if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria )))
		sql+=" And p.c_period_id="+DepDoc.getC_Period_ID()+" And fore.Corrected='N' And a.isinposession='Y' And "+ID_grupo;
		//sql+=" And a.assetservicedate between ? and ? And fore.Corrected='N' And a.a_asset_group_id="+DepDoc.getA_Asset_Group_ID();
	
	if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Reevaluacion)) || DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Deteriodo)))
		sql+= " And p.c_period_id="+DepDoc.getC_Period_ID()+" And wk.a_asset_cost>wk.a_accumulated_depr+2 And "+ID_grupo+ " and a.isinposession="+ (DepDoc.isInPosession()?"'Y'":"'N'");
	
	/*if (DepDoc.getDepType().equalsIgnoreCase(X_A_Asset_Dep.DEPTYPE_VentaActivo))
		sql += " And a.A_Asset_ID=" + DepDoc.getA_Asset_ID();*/
	
	if(DepDoc.getDepType().equalsIgnoreCase(X_A_Asset_Dep.DEPTYPE_VentaActivo))
	{
		sql+=" And p.c_period_id="+DepDoc.getC_Period_ID();
		int id_invoice = 0; 
		try
		{ 
			id_invoice = DepDoc.get_ValueAsInt("C_Invoice_ID");
		}
		catch(Exception e)
		{
			id_invoice = 0;
			log.log(Level.SEVERE, sql.toString(), e);			
		}
		if (id_invoice > 0)
		{
			sql+= " And a.A_Asset_ID IN (SELECT DISTINCT(A_Asset_ID) FROM C_InvoiceLine WHERE C_Invoice_ID = "+DepDoc.get_ValueAsInt("C_Invoice_ID")+")" ;			
		}
		else
		{
			sql+= " And a.A_Asset_ID="+DepDoc.getA_Asset_ID();
		}		
	}
	MOrg orgP = new MOrg(getCtx(), DepDoc.getAD_Org_ID(), get_TrxName());
	if(orgP.getValue()!= null)
		if(!orgP.getValue().contains("*"))
			sql+= " and a.AD_OrgRef_ID="+DepDoc.getAD_Org_ID();
	
	if(DepDoc.getDepType().equalsIgnoreCase("VCO"))//baja consolidad para dpp
	{
		int id_invoice = 0; 
		try
		{ 
			id_invoice = DepDoc.get_ValueAsInt("C_Invoice_ID");
		}
		catch(Exception e)
		{
			id_invoice = 0;
			log.log(Level.SEVERE, sql.toString(), e);			
		}
		if (id_invoice > 0)//solo traemos activos de la factura asociada
			sql+= " And a.A_Asset_ID IN (SELECT DISTINCT(A_Asset_ID) FROM C_InvoiceLine WHERE C_Invoice_ID = "+DepDoc.get_ValueAsInt("C_Invoice_ID")+")" ;			
		else
			sql+= " And a.A_Asset_ID = 0";
	}
	if(DepDoc.getDepType().equalsIgnoreCase("VCP"))//baja consolidad para dpp
	{
		int id_invoice = 0;
		//Se sobreescribira sql para no necesitar periodo
		sql="select a.a_asset_id,a.name,a.value,acct.c_acctschema_id, acct.a_asset_acct, "
			+" acct.a_depreciation_acct,"
			//+" fore.amount,fore.a_asset_forecast_id,p.c_period_id,p.name,"
			+" acct.a_accumdepreciation_acct,acct.A_Disposal_Gain_Acct,wk.a_asset_cost,acct.A_Disposal_RevenueC_Acct,"
			+" acct.A_Disposal_RevenueD_Acct,acct.A_Disposal_Loss_Acct, wk.a_accumulated_depr, acct.A_AssetComplement_Acct"
			+" from a_asset a"
			+" inner join a_asset_acct acct on (a.a_asset_id=acct.a_asset_id)"
			+" inner join A_Depreciation_Workfile wk on (a.a_asset_id=wk.a_asset_id)"
			//+" inner join a_asset_forecast fore on (a.a_asset_id=fore.a_asset_id)"
			+" where a.ad_client_id=? ";
			//and fore.a_asset_forecast_ID = ";
			/*+" (SELECT MAX(fa.a_asset_forecast_ID) FROM a_asset_forecast fa " +
			 "  WHERE fa.isactive = 'Y' AND fore.A_Asset_ID = a.A_Asset_ID) ";*/
		try
		{ 
			id_invoice = DepDoc.get_ValueAsInt("C_Invoice_ID");
		}
		catch(Exception e)
		{
			id_invoice = 0;
			log.log(Level.SEVERE, sql.toString(), e);			
		}
		if (id_invoice > 0)//solo traemos activos de la factura asociada
			sql+= " And a.A_Asset_ID IN (SELECT DISTINCT(A_Asset_ID) FROM C_InvoiceLine WHERE C_Invoice_ID = "+DepDoc.get_ValueAsInt("C_Invoice_ID")+")" ;			
		else
			sql+= " And a.A_Asset_ID = 0";
	}
	
	
	/**
	 * mfrojas 20181002
	 * se agrega nuevo proceso de reevaluacion y deterioro, para que sean por activo, y no por grupo
	 */
	
	if(DepDoc.getDepType().equalsIgnoreCase("RRV")) //Reevaluacion
	{
		int id_activo = 0;
		//Se sobreescribe sql para no utilizar periodo y que sea por activo y no por grupo.
		sql="select a.a_asset_id,a.name,a.value,acct.c_acctschema_id, acct.a_asset_acct, "
				+" acct.a_depreciation_acct,"
				//+" fore.amount,fore.a_asset_forecast_id,p.c_period_id,p.name,"
				+" acct.a_accumdepreciation_acct,acct.A_Disposal_Gain_Acct,wk.a_asset_cost,acct.A_Disposal_RevenueC_Acct,"
				+" acct.A_Disposal_RevenueD_Acct,acct.A_Disposal_Loss_Acct, wk.a_accumulated_depr, acct.A_AssetComplement_Acct"
				+" from a_asset a"
				+" inner join a_asset_acct acct on (a.a_asset_id=acct.a_asset_id)"
				+" inner join A_Depreciation_Workfile wk on (a.a_asset_id=wk.a_asset_id)"
				//+" inner join a_asset_forecast fore on (a.a_asset_id=fore.a_asset_id)"
				+" where a.ad_client_id=? ";
				//and fore.a_asset_forecast_ID = ";
				/*+" (SELECT MAX(fa.a_asset_forecast_ID) FROM a_asset_forecast fa " +
				 "  WHERE fa.isactive = 'Y' AND fore.A_Asset_ID = a.A_Asset_ID) ";*/
		try
		{ 
			id_activo = DepDoc.get_ValueAsInt("A_Asset_ID");
		}
		catch(Exception e)
		{
			id_activo = 0;
			log.log(Level.SEVERE, sql.toString(), e);			
		}
		if (id_activo > 0)//solo el activo mencionado en el proceso
			sql+= " And a.A_Asset_ID IN ("+DepDoc.get_ValueAsInt("A_Asset_ID")+")" ;			
		else
			sql+= " And a.A_Asset_ID = 0";


		
	}
	
	if(DepDoc.getDepType().equalsIgnoreCase("DDT")) //Deterioro
	{
		int id_activo = 0;
		//Se sobreescribe sql para no utilizar periodo y que sea por activo y no por grupo.
		sql="select a.a_asset_id,a.name,a.value,acct.c_acctschema_id, acct.a_asset_acct, "
				+" acct.a_depreciation_acct,"
				//+" fore.amount,fore.a_asset_forecast_id,p.c_period_id,p.name,"
				+" acct.a_accumdepreciation_acct,acct.A_Disposal_Gain_Acct,wk.a_asset_cost,acct.A_Disposal_RevenueC_Acct,"
				+" acct.A_Disposal_RevenueD_Acct,acct.A_Disposal_Loss_Acct, wk.a_accumulated_depr, acct.A_AssetComplement_Acct"
				+" from a_asset a"
				+" inner join a_asset_acct acct on (a.a_asset_id=acct.a_asset_id)"
				+" inner join A_Depreciation_Workfile wk on (a.a_asset_id=wk.a_asset_id)"
				//+" inner join a_asset_forecast fore on (a.a_asset_id=fore.a_asset_id)"
				+" where a.ad_client_id=? ";
				//and fore.a_asset_forecast_ID = ";
				/*+" (SELECT MAX(fa.a_asset_forecast_ID) FROM a_asset_forecast fa " +
				 "  WHERE fa.isactive = 'Y' AND fore.A_Asset_ID = a.A_Asset_ID) ";*/
		try
		{ 
			id_activo = DepDoc.get_ValueAsInt("A_Asset_ID");
		}
		catch(Exception e)
		{
			id_activo = 0;
			log.log(Level.SEVERE, sql.toString(), e);			
		}
		if (id_activo > 0)//solo el activo mencionado en el proceso
			sql+= " And a.A_Asset_ID IN ("+DepDoc.get_ValueAsInt("A_Asset_ID")+")" ;			
		else
			sql+= " And a.A_Asset_ID = 0";


		
	}
	/**
	 * mfrojas 20181003
	 * Se genera nuevo proceso de depreciacion
	 * 
	 */
	if(DepDoc.getDepType().equalsIgnoreCase("DDP")) //Depreciacion OFB
	{
		//Se sobreescribe: Los periodos a depreciar ahora corresponderan a aquellos que correspondan al mismo año del periodo que aca se obtiene. 
		sql="select a.a_asset_id,a.name,a.value,acct.c_acctschema_id, acct.a_asset_acct, "
				+" acct.a_depreciation_acct, fore.amount,fore.a_asset_forecast_id,"
				+" acct.a_accumdepreciation_acct,acct.A_Disposal_Gain_Acct,wk.a_asset_cost,acct.A_Disposal_RevenueC_Acct,"
				+" acct.A_Disposal_RevenueD_Acct,acct.A_Disposal_Loss_Acct, wk.a_accumulated_depr, acct.A_AssetComplement_Acct"
				+" from a_asset a"
				+" inner join a_asset_acct acct on (a.a_asset_id=acct.a_asset_id)"
				+" inner join A_Depreciation_Workfile wk on (a.a_asset_id=wk.a_asset_id)"
				+" inner join a_asset_forecast fore on (a.a_asset_id=fore.a_asset_id)"
				//+" inner join c_period p on (fore.datedoc between p.startdate and p.enddate)"
				+" where a.ad_client_id=? ";// and p.c_period_id=? ";
		
		sql += " AND extract(year from fore.datedoc) = (select to_number(fiscalyear,'9999') " +
				" from c_year where c_year_id in (select c_year_id from c_period where c_period_id = "+DepDoc.getC_Period_ID()+")) AND fore.processed='N' ";
		sql += " and a.isinposession='Y' And "+ID_grupo;
		sql += " and a.isactive='Y'";

		//MOrg orgP = new MOrg(getCtx(), DepDoc.getAD_Org_ID(), get_TrxName());
		if(orgP.getValue()!= null)
			if(!orgP.getValue().contains("*"))
				sql+= " and a.AD_OrgRef_ID="+DepDoc.getAD_Org_ID();

	}

	log.config("SQL Activos: "+sql);
	PreparedStatement pstmt = null;
	
	MJournalBatch batch= null;
	MJournal journal = null;
	MClient client= MClient.get(m_ctx,Env.getAD_Client_ID(m_ctx));
	int count=0;
	if(DepDoc.getDocStatus().equals("DR") && DepDoc.getDepType().equalsIgnoreCase("ANC")== false)
	{		
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, DepDoc.getAD_Client_ID());
			//pstmt.setInt(2, DepDoc.getC_Period_ID());			
			/*if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria ))){
				pstmt.setTimestamp(3, datefrom);
				pstmt.setTimestamp(4, dateto);
			}*/
			ResultSet rs = pstmt.executeQuery();
			while (rs.next() )
			{
				int ID_Org = 0;
				if(DepDoc.getDepType().equalsIgnoreCase("VCO") || DepDoc.getDepType().equalsIgnoreCase("VCP")
						&& DepDoc.getAD_Org_ID() < 1)//baja consolidad para dpp
				{
					MInvoice inv = new MInvoice(getCtx(), DepDoc.get_ValueAsInt("C_Invoice_ID"), get_TrxName());
					ID_Org = inv.getAD_Org_ID();
				}
				else
					ID_Org = DepDoc.getAD_Org_ID();
				if(batch==null)
				{
					batch = new MJournalBatch(m_ctx,0,get_TrxName());
					batch.setAD_Org_ID(ID_Org);
					batch.setDescription("Assets Deprecation Process");
					batch.setPostingType("A");
					batch.setC_DocType_ID(MDocType.getOfDocBaseType(m_ctx, "GLJ")[0].getC_DocType_ID());
					batch.setGL_Category_ID(MGLCategory.getDefault(m_ctx, MGLCategory.CATEGORYTYPE_Manual).getGL_Category_ID());
					batch.setDateAcct(DepDoc.getDateDoc());
					batch.setDateDoc(DepDoc.getDateDoc());
					batch.setC_Period_ID(MPeriod.getC_Period_ID(m_ctx, DepDoc.getDateAcct()));
					batch.setC_Currency_ID(client.getC_Currency_ID());
					if(!batch.save())
					{
						return "la transaccion no puede ser generara para *";
					}
				}
				count++;
				MAsset assetTemp = new MAsset(getCtx(), rs.getInt("a_asset_id"), get_TrxName());
				journal= new MJournal(batch);
				journal.setDescription(rs.getString("value")+"-"+rs.getString("name"));
				journal.setC_AcctSchema_ID(MAcctSchema.getClientAcctSchema(m_ctx, Env.getAD_Client_ID(m_ctx))[0].getC_AcctSchema_ID() );
				journal.setGL_Category_ID(MGLCategory.getDefault(m_ctx, MGLCategory.CATEGORYTYPE_Document).getGL_Category_ID() );
				journal.setC_ConversionType_ID(114);
				journal.set_ValueOfColumn("A_Asset_ID", rs.getInt("a_asset_id"));
				if(!DepDoc.getDepType().equalsIgnoreCase("VCP"))//baja para activos sin planificacion
						journal.set_ValueOfColumn("A_Asset_Forecast_ID",rs.getInt("a_asset_forecast_id")); 
				journal.save();
				//ininoles se setea org con campo OrgRef de Activo
				journal.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
				journal.save();
				
				if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Depreciacion ))){ //depreciacion
					MJournalLine line1= new MJournalLine(journal);
					line1.setA_Asset_ID(rs.getInt("a_asset_id"));
					line1.setAmtSourceDr(rs.getBigDecimal("amount"));
					line1.setAmtSourceCr(Env.ZERO);
					line1.setAmtAcct(rs.getBigDecimal("amount"), Env.ZERO);
					line1.setC_ValidCombination_ID(rs.getInt("a_depreciation_acct"));
					line1.save();
					line1.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line1.save();
					
					MJournalLine line2= new MJournalLine(journal);
					line2.setA_Asset_ID(rs.getInt("a_asset_id"));
					line2.setAmtSourceDr(Env.ZERO);
					line2.setAmtSourceCr(rs.getBigDecimal("amount"));
					line2.setAmtAcct(Env.ZERO, rs.getBigDecimal("amount"));
					//line2.setC_ValidCombination_ID(rs.getInt("a_asset_acct")); // old
					line2.setC_ValidCombination_ID(rs.getInt("A_AssetComplement_Acct")); //new
					line2.save();
					line2.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line2.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria ))){//correccion monetaria
					batch.setDescription("Correcion Monetaria");
					MJournalLine line1= new MJournalLine(journal);
					line1.setA_Asset_ID(rs.getInt("a_asset_id"));
					if(DepDoc.getRate().signum()>0){
						line1.setAmtSourceDr(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)));
						line1.setAmtSourceCr(Env.ZERO);
						line1.setAmtAcct(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)), Env.ZERO);
					}
					else if(DepDoc.getRate().signum()<0){
						line1.setAmtSourceDr(Env.ZERO);
						line1.setAmtSourceCr(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)));
						line1.setAmtAcct(Env.ZERO, rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)));
					}
					line1.setC_ValidCombination_ID(rs.getInt("a_depreciation_acct"));
					line1.save();
					line1.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line1.save();
					
					MJournalLine line2= new MJournalLine(journal);
					line2.setA_Asset_ID(rs.getInt("a_asset_id"));
					if(DepDoc.getRate().signum()>0){
						line2.setAmtSourceDr(Env.ZERO);
						line2.setAmtSourceCr(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)));
						line2.setAmtAcct(Env.ZERO, rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)));
					}
					if(DepDoc.getRate().signum()<0){
						line2.setAmtSourceDr(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)));
						line2.setAmtSourceCr(Env.ZERO);
						line2.setAmtAcct(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)),Env.ZERO);
					}
					line2.setC_ValidCombination_ID(rs.getInt("a_accumdepreciation_acct"));
					line2.save();
					line2.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line2.save();
					
					MJournalLine line3= new MJournalLine(journal);
					line3.setA_Asset_ID(rs.getInt("a_asset_id"));
					if(DepDoc.getRate().signum()>0){
						line3.setAmtSourceDr(rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)));
						line3.setAmtSourceCr(Env.ZERO);
						line3.setAmtAcct(rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)),Env.ZERO );
					}
					if(DepDoc.getRate().signum()<0){
						line3.setAmtSourceDr(Env.ZERO);
						line3.setAmtSourceCr(rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)));
						line3.setAmtAcct(Env.ZERO,rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)));
					}
					line3.setC_ValidCombination_ID(rs.getInt("a_asset_acct"));
					line3.save();
					line3.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line3.save();
					
					MJournalLine line4= new MJournalLine(journal);
					line4.setA_Asset_ID(rs.getInt("a_asset_id"));
					if(DepDoc.getRate().signum()>0){
						line4.setAmtSourceDr(Env.ZERO);
						line4.setAmtSourceCr(rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)));
						line4.setAmtAcct(Env.ZERO, rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)));
					}
					if(DepDoc.getRate().signum()<0){
						line4.setAmtSourceDr(rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)));
						line4.setAmtSourceCr(Env.ZERO);
						line4.setAmtAcct(rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)),Env.ZERO);
					}
					line4.setC_ValidCombination_ID(rs.getInt("a_accumdepreciation_acct"));
					line4.save();
					line4.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line4.save();
				
				}else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Reevaluacion))){ //Reevaluacion
					batch.setDescription("Reevaluacion Activos");
					BigDecimal newAmount= new BigDecimal(0.0);
					newAmount=rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED));
					
					MJournalLine line1= new MJournalLine(journal);
					line1.setA_Asset_ID(rs.getInt("a_asset_id"));
					line1.setAmtSourceDr(newAmount);
					line1.setAmtSourceCr(Env.ZERO);
					line1.setAmtAcct(newAmount, Env.ZERO);
					line1.setC_ValidCombination_ID(rs.getInt("a_asset_acct"));
					line1.save();
					line1.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line1.save();
					
					MJournalLine line2= new MJournalLine(journal);
					line2.setA_Asset_ID(rs.getInt("a_asset_id"));
					line2.setAmtSourceDr(Env.ZERO);
					line2.setAmtSourceCr(newAmount);
					line2.setAmtAcct(Env.ZERO, newAmount);
					line2.setC_ValidCombination_ID(rs.getInt("A_Disposal_Gain_Acct"));
					line2.save();
					line2.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line2.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Deteriodo))){//Deteriodo
					batch.setDescription("Deteriodo Activos");
					BigDecimal newAmount= new BigDecimal(0.0);
					newAmount=rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED));
					
					MJournalLine line1= new MJournalLine(journal);
					line1.setA_Asset_ID(rs.getInt("a_asset_id"));
					line1.setAmtSourceDr(newAmount);
					line1.setAmtSourceCr(Env.ZERO);
					line1.setAmtAcct(newAmount, Env.ZERO);
					line1.setC_ValidCombination_ID(rs.getInt("A_Disposal_Loss_Acct"));
					line1.save();
					line1.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line1.save();
					
					MJournalLine line2= new MJournalLine(journal);
					line2.setA_Asset_ID(rs.getInt("a_asset_id"));
					line2.setAmtSourceDr(Env.ZERO);
					line2.setAmtSourceCr(newAmount);
					line2.setAmtAcct(Env.ZERO, newAmount);
					line2.setC_ValidCombination_ID(rs.getInt("a_asset_acct"));
					line2.save();
					line2.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line2.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase(X_A_Asset_Dep.DEPTYPE_VentaActivo))
				{ //venta activo
					BigDecimal newAmount= new BigDecimal(0.0);
					newAmount=rs.getBigDecimal("a_asset_cost").subtract(rs.getBigDecimal("a_accumulated_depr"));
					newAmount=newAmount.add(Env.ONE);
					
					MJournalLine line1= new MJournalLine(journal);
					line1.setA_Asset_ID(DepDoc.getA_Asset_ID());
					line1.setAmtSourceDr(newAmount);
					line1.setAmtSourceCr(Env.ZERO);
					line1.setAmtAcct(newAmount, Env.ZERO);
					line1.setC_ValidCombination_ID(rs.getInt("a_depreciation_acct"));
					line1.save();
					line1.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line1.save();
					
					MJournalLine line2= new MJournalLine(journal);
					line2.setA_Asset_ID(DepDoc.getA_Asset_ID());
					line2.setAmtSourceDr(Env.ZERO);
					line2.setAmtSourceCr(newAmount);
					line2.setAmtAcct(Env.ZERO, newAmount);
					line2.setC_ValidCombination_ID(rs.getInt("a_asset_acct"));
					line2.save();
					line2.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line2.save();
					
					MJournalLine line3= new MJournalLine(journal);
					line3.setA_Asset_ID(DepDoc.getA_Asset_ID());
					line3.setAmtSourceDr(DepDoc.getRate());
					line3.setAmtSourceCr(Env.ZERO);
					line3.setAmtAcct(DepDoc.getRate(), Env.ZERO);
					line3.setC_ValidCombination_ID(rs.getInt("A_Disposal_RevenueD_Acct"));
					line3.save();
					line3.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line3.save();
					
					MJournalLine line4= new MJournalLine(journal);
					line4.setA_Asset_ID(DepDoc.getA_Asset_ID());
					line4.setAmtSourceDr(Env.ZERO);
					line4.setAmtSourceCr(DepDoc.getRate());
					line4.setAmtAcct(Env.ZERO, DepDoc.getRate());
					line4.setC_ValidCombination_ID(rs.getInt("A_Disposal_RevenueC_Acct"));
					line4.save();
					line4.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line4.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase("VCO")
						|| DepDoc.getDepType().equalsIgnoreCase("VCP"))//venta consolidada DPP
				{ //venta activo
					BigDecimal newAmountCost = new BigDecimal(0.0);
					BigDecimal newAmountDep = new BigDecimal(0.0);
					newAmountCost =rs.getBigDecimal("a_asset_cost");
					newAmountDep = rs.getBigDecimal("a_accumulated_depr");
					
					MJournalLine line1= new MJournalLine(journal);
					line1.setA_Asset_ID(DepDoc.getA_Asset_ID());
					line1.setAmtSourceDr(newAmountDep);
					line1.setAmtSourceCr(Env.ZERO);
					line1.setAmtAcct(newAmountDep, Env.ZERO);
					line1.setC_ValidCombination_ID(rs.getInt("a_depreciation_acct"));
					line1.save();
					line1.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line1.save();
					
					MJournalLine line2= new MJournalLine(journal);
					line2.setA_Asset_ID(DepDoc.getA_Asset_ID());
					line2.setAmtSourceDr(newAmountCost.subtract(newAmountDep));
					line2.setAmtSourceCr(Env.ZERO);
					line2.setAmtAcct(newAmountCost.subtract(newAmountDep),Env.ZERO);
					line2.setC_ValidCombination_ID(rs.getInt("A_Disposal_RevenueD_Acct"));
					line2.save();
					line2.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line2.save();
					
					MJournalLine line3= new MJournalLine(journal);
					line3.setA_Asset_ID(DepDoc.getA_Asset_ID());
					line3.setAmtSourceDr(Env.ZERO);
					line3.setAmtSourceCr(newAmountCost);
					line3.setAmtAcct(Env.ZERO,newAmountCost);
					line3.setC_ValidCombination_ID(rs.getInt("a_asset_acct"));
					line3.save();
					line3.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line3.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase("RRV")){ //Reevaluacion OFB
					batch.setDescription("Reevaluacion Activos");
					BigDecimal newAmount= new BigDecimal(0.0);
					newAmount=rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED));
					
					MJournalLine line1= new MJournalLine(journal);
					line1.setA_Asset_ID(rs.getInt("a_asset_id"));
					line1.setAmtSourceDr(newAmount);
					line1.setAmtSourceCr(Env.ZERO);
					line1.setAmtAcct(newAmount, Env.ZERO);
					line1.setC_ValidCombination_ID(rs.getInt("a_asset_acct"));
					line1.save();
					line1.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line1.save();
					
					MJournalLine line2= new MJournalLine(journal);
					line2.setA_Asset_ID(rs.getInt("a_asset_id"));
					line2.setAmtSourceDr(Env.ZERO);
					line2.setAmtSourceCr(newAmount);
					line2.setAmtAcct(Env.ZERO, newAmount);
					line2.setC_ValidCombination_ID(rs.getInt("A_Disposal_Gain_Acct"));
					line2.save();
					line2.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line2.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase("DDT")){//Deterioro OFB
					batch.setDescription("Deterioro Activos");
					BigDecimal newAmount= new BigDecimal(0.0);
					newAmount=rs.getBigDecimal("a_asset_cost").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED));
					
					MJournalLine line1= new MJournalLine(journal);
					line1.setA_Asset_ID(rs.getInt("a_asset_id"));
					line1.setAmtSourceDr(newAmount);
					line1.setAmtSourceCr(Env.ZERO);
					line1.setAmtAcct(newAmount, Env.ZERO);
					line1.setC_ValidCombination_ID(rs.getInt("A_Disposal_Loss_Acct"));
					line1.save();
					line1.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line1.save();
					
					MJournalLine line2= new MJournalLine(journal);
					line2.setA_Asset_ID(rs.getInt("a_asset_id"));
					line2.setAmtSourceDr(Env.ZERO);
					line2.setAmtSourceCr(newAmount);
					line2.setAmtAcct(Env.ZERO, newAmount);
					line2.setC_ValidCombination_ID(rs.getInt("a_asset_acct"));
					line2.save();
					line2.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line2.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase("DDP")){ //depreciacion OFB
					MJournalLine line1= new MJournalLine(journal);
					line1.setA_Asset_ID(rs.getInt("a_asset_id"));
					line1.setAmtSourceDr(rs.getBigDecimal("amount"));
					line1.setAmtSourceCr(Env.ZERO);
					line1.setAmtAcct(rs.getBigDecimal("amount"), Env.ZERO);
					line1.setC_ValidCombination_ID(rs.getInt("a_depreciation_acct"));
					line1.save();
					line1.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line1.save();
					
					MJournalLine line2= new MJournalLine(journal);
					line2.setA_Asset_ID(rs.getInt("a_asset_id"));
					line2.setAmtSourceDr(Env.ZERO);
					line2.setAmtSourceCr(rs.getBigDecimal("amount"));
					line2.setAmtAcct(Env.ZERO, rs.getBigDecimal("amount"));
					//line2.setC_ValidCombination_ID(rs.getInt("a_asset_acct")); // old
					line2.setC_ValidCombination_ID(rs.getInt("A_AssetComplement_Acct")); //new
					line2.save();
					line2.setAD_Org_ID(assetTemp.get_ValueAsInt("AD_OrgRef_ID"));
					line2.save();
				}
			}
			rs.close();
			pstmt.close();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		
		if(count>0)
		{
			DepDoc.setGL_JournalBatch_ID(batch.getGL_JournalBatch_ID());
			
			DB.executeUpdate("Update GL_JournalLine set A_Asset_Dep_ID="+DepDoc.getA_Asset_Dep_ID()
					+ " Where GL_Journal_ID IN (select GL_Journal_ID from GL_Journal where GL_JournalBatch_ID="+batch.getGL_JournalBatch_ID()+" )", get_TrxName());
		}
		else
		{
			if(batch!=null)
				batch.delete(true, get_TrxName());
		}
		DepDoc.setDocStatus("IP");
		DepDoc.save();	
	}
	else if(DepDoc.getDocStatus().equals("DR") && DepDoc.getDepType().equalsIgnoreCase("ANC"))
	{
		DepDoc.setDocStatus("IP");
		DepDoc.save();
	}	
	else if (DepDoc.getDocStatus().equals("IP")  && !DepDoc.isVoid() && DepDoc.getDepType().equalsIgnoreCase("ANC")== false
				&& DepDoc.getDepType().equalsIgnoreCase("VCO")== false
				&& DepDoc.getDepType().equalsIgnoreCase("VCP")== false)
	{	
		log.info("IP");
		
		
		 batch = new MJournalBatch(m_ctx,DepDoc.getGL_JournalBatch_ID(),get_TrxName());
		 batch.processIt("CO");
		 batch.save();
		 batch.setProcessed(true);
		 batch.save();
		 
		 MJournal[] journals = batch.getJournals(true);
		 for (int i = 0; i < journals.length; i++)
			{
				MJournal jour = journals[i];
				X_A_Asset asset = new X_A_Asset (getCtx(), jour.get_ValueAsInt("A_Asset_ID"), null);
				
				if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Depreciacion ))){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().add(jour.getTotalCr()) );
					workfile.setA_Period_Posted(workfile.getA_Period_Posted()+1);
					workfile.setAssetDepreciationDate(DepDoc.getDateDoc());
					workfile.save();
					
					X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),null);
					fore.setProcessed(true);
					fore.setGL_Journal_ID(jour.getGL_Journal_ID());
					fore.save();
					
					asset.setLastMaintenanceNote("Depreciacion Periodo :"+(workfile.getA_Period_Posted()+1)+" por "+jour.getTotalCr());
				}
				else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria ))){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					MAssetAcct group=new MAssetAcct(getCtx(), MAssetAcct.getAssetAcct_ID(jour.get_ValueAsInt("A_Asset_ID")),get_TrxName());
					BigDecimal acum=DB.getSQLValueBD(get_TrxName(),  "select sum(AmtAcctDr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
							+" and C_ValidCombination_ID="+group.getA_Depreciation_Acct());
					acum=acum.subtract(DB.getSQLValueBD(get_TrxName(),  "select sum(AmtAcctCr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
							+" and C_ValidCombination_ID="+group.getA_Depreciation_Acct()));
					
					BigDecimal neto=DB.getSQLValueBD(get_TrxName(), "select sum(AmtAcctDr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
							+" and C_ValidCombination_ID="+group.getA_Asset_Acct());
					neto=neto.subtract(DB.getSQLValueBD(get_TrxName(), "select sum(AmtAcctCr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
							+" and C_ValidCombination_ID="+group.getA_Asset_Acct()));
					
					workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().add(acum) );
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().add(neto));
					workfile.save();
					
					X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),null);
					fore.setCorrected(true);
					fore.save();
					
					asset.setLastMaintenanceNote("CM Periodo "+DepDoc.getRate() +" % Valor:"+neto);
				}
				else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Reevaluacion))){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().add(jour.getTotalCr()));
					workfile.save();
					
				}
				else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Deteriodo))){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().subtract(jour.getTotalCr()));
					workfile.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase("RRV")){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().add(jour.getTotalCr()));
					workfile.save();
					//cambio de vida util.
					if(DepDoc.get_ValueAsInt("A_Asset_Life_Years") > 0)
					{
						DB.executeUpdate("Update a_asset_acct set a_period_end = "+DepDoc.get_ValueAsInt("A_Asset_Life_Years")+" WHERE a_Asset_id = "+jour.get_ValueAsInt("A_Asset_ID"), get_TrxName());
					}
					try
					{
						asset.set_CustomColumn("GrandTotal", workfile.getA_Asset_Cost());
						asset.save();
						
					}
					catch(Exception e)
					{
						log.log(Level.SEVERE, sql.toString(), e);			
					}
					
				}
				else if(DepDoc.getDepType().equalsIgnoreCase("DDT")){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().subtract(jour.getTotalCr()));
					workfile.save();
					//cambio de vida util.
					if(DepDoc.get_ValueAsInt("A_Asset_Life_Years") > 0)
					{
						DB.executeUpdate("Update a_asset_acct set a_period_end = "+DepDoc.get_ValueAsInt("A_Asset_Life_Years")+" WHERE a_Asset_id = "+jour.get_ValueAsInt("A_Asset_ID"), get_TrxName());
					}
					try
					{
						asset.set_CustomColumn("GrandTotal", workfile.getA_Asset_Cost());
						asset.save();
						
					}
					catch(Exception e)
					{
						log.log(Level.SEVERE, sql.toString(), e);			
					}

				}

				else if(DepDoc.getDepType().equalsIgnoreCase(X_A_Asset_Dep.DEPTYPE_VentaActivo))
				{
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Calc_Accumulated_Depr(workfile.getA_Accumulated_Depr()); //guardo por si necesito volver al estado anterior
					workfile.setA_Accumulated_Depr(workfile.getA_Asset_Cost() );
					workfile.save();
					asset.setLastMaintenanceNote("Venta Activo");
					
					DB.executeUpdate("Update A_Asset_Forecast set processed='Y', corrected='Y' where A_Asset_ID="+jour.get_ValueAsInt("A_Asset_ID"), get_TrxName());
					//nuevos acciones para DPP
					asset.setIsFullyDepreciated(true);					
					asset.set_CustomColumn("AssetStatus", "BAJ");
					asset.set_CustomColumn("A_Asset_RevalDate",DepDoc.getDateDoc());
					asset.save();
					asset.setIsActive(false);
					asset.save();
				}		
				else if(DepDoc.getDepType().equalsIgnoreCase("DDP"))
				{
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().add(jour.getTotalCr()) );
					workfile.setA_Period_Posted(workfile.getA_Period_Posted()+1);
					workfile.setAssetDepreciationDate(DepDoc.getDateDoc());
					workfile.save();
					
					X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),null);
					fore.setProcessed(true);
					fore.setGL_Journal_ID(jour.getGL_Journal_ID());
					fore.save();
					
					asset.setLastMaintenanceNote("Depreciacion Periodo :"+(workfile.getA_Period_Posted()+1)+" por "+jour.getTotalCr());
				}
				log.config("deptype "+DepDoc.getDepType());

			}		 
		 DepDoc.setDocStatus("CO");
		 DepDoc.setProcessed(true);
		 DepDoc.save();
		 
		 //boolean assetadvise="Y".equals(DB.getSQLValueString(get_TrxName(), "select assetadvise from ad_client where ad_client_id="+getAD_Client_ID()));
//		 if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria )) && assetadvise)
//			 org.compiere.OFBapp.ADialog.warn(2, new org.compiere.OFBapp.ConfirmPanel(), "No Olvide Replanificar el Activo luego de correr la correccion Monetaria");
		 
	}
	else if (DepDoc.getDocStatus().equals("IP")  && !DepDoc.isVoid() && DepDoc.getDepType().equalsIgnoreCase("ANC"))
	{	
		if(DepDoc.getDepType().equalsIgnoreCase("ANC"))
		{
			String sqlANC = " ";
			int id_invoice = 0; 
			try{
				id_invoice = DepDoc.get_ValueAsInt("C_Invoice_ID");
			}
			catch(Exception e){
				id_invoice = 0;
				log.log(Level.SEVERE, sql.toString(), e);			
			}
			if (id_invoice > 0)
			{
				sqlANC = " SELECT DISTINCT(A_Asset_ID) as A_Asset_ID FROM C_InvoiceLine WHERE C_Invoice_ID = "+DepDoc.get_ValueAsInt("C_Invoice_ID") ;			
			}
			else
			{
				sqlANC = " SELECT A_Asset_ID FROM A_Asset WHERE A_Asset_ID="+DepDoc.getA_Asset_ID();
			}							
			PreparedStatement pstmtANC = DB.prepareStatement(sqlANC, get_TrxName());					
			ResultSet rsANC = pstmtANC.executeQuery();
			while (rsANC.next())
			{
				if (rsANC.getInt("A_Asset_ID") > 0)
					{
					X_A_Asset assetANC = new X_A_Asset (getCtx(), rsANC.getInt("A_Asset_ID"), null);
					assetANC.setIsFullyDepreciated(true);
					assetANC.setLastMaintenanceNote("Venta Activo");
					assetANC.set_CustomColumn("AssetStatus", "BAJ");					
					assetANC.save();
					assetANC.setIsActive(false);
					assetANC.set_CustomColumn("A_Asset_RevalDate",DepDoc.getDateDoc());
					assetANC.save();
					
					int cantWK =DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM A_Depreciation_Workfile WHERE IsActive = 'Y' AND A_Asset_ID  = "+assetANC.get_ID());
					if (cantWK > 0)
					{
						MDepreciationWorkfile workfile=MAsset.getWorkFile(assetANC.get_ID());
						workfile.setA_Calc_Accumulated_Depr(workfile.getA_Accumulated_Depr()); //guardo por si necesito volver al estado anterior
						workfile.setA_Accumulated_Depr(workfile.getA_Asset_Cost());
						workfile.save();
					}						
					DB.executeUpdate("Update A_Asset_Forecast set processed='Y', corrected='Y' where A_Asset_ID="+assetANC.get_ID(), get_TrxName());
				}
			}
		}
		
		DepDoc.setDocStatus("CO");
		DepDoc.setProcessed(true);
		DepDoc.save();
	}
	else if (DepDoc.getDocStatus().equals("IP")  && !DepDoc.isVoid() && (DepDoc.getDepType().equalsIgnoreCase("VCO")
			|| DepDoc.getDepType().equalsIgnoreCase("VCP")))
	{
		//primero realizamos venta(baja) de activos contables
		log.info("IP, Baja consolidad activos contables");		
		
		if (DepDoc.getGL_JournalBatch_ID() > 0)// solo si existe journal
		{
			batch = new MJournalBatch(m_ctx,DepDoc.getGL_JournalBatch_ID(),get_TrxName());
			batch.processIt("CO");
			batch.save();
			batch.setProcessed(true);
			batch.save();
			 
			MJournal[] journals = batch.getJournals(true);
			for (int i = 0; i < journals.length; i++)
			{
				MJournal jour = journals[i];
				X_A_Asset asset = new X_A_Asset (getCtx(), jour.get_ValueAsInt("A_Asset_ID"), null);
			
				MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
				workfile.setA_Calc_Accumulated_Depr(workfile.getA_Accumulated_Depr()); //guardo por si necesito volver al estado anterior
				workfile.setA_Accumulated_Depr(workfile.getA_Asset_Cost() );
				workfile.save();
				asset.setLastMaintenanceNote("Venta Activo");
					
				DB.executeUpdate("Update A_Asset_Forecast set processed='Y', corrected='Y' where A_Asset_ID="+jour.get_ValueAsInt("A_Asset_ID"), get_TrxName());
				//nuevos acciones para DPP
				asset.setIsFullyDepreciated(true);					
				asset.set_CustomColumn("AssetStatus", "BAJ");
				asset.set_CustomColumn("A_Asset_RevalDate",DepDoc.getDateDoc());
				asset.save();
				asset.setIsActive(false);
				asset.save();
			}
		}
		//realizamos venta(baja) de activos NO contables
		log.info("IP, Baja consolidad activos NO contables");
		String sqlANC = " ";
		int id_invoice = 0; 
		try{
			id_invoice = DepDoc.get_ValueAsInt("C_Invoice_ID");
		}
		catch(Exception e){
			id_invoice = 0;
			log.log(Level.SEVERE, sql.toString(), e);			
		}
		if (id_invoice > 0)//solo se hace baja si existe factura
		{
			sqlANC = " SELECT DISTINCT(A_Asset_ID) as A_Asset_ID FROM C_InvoiceLine WHERE C_Invoice_ID = "+DepDoc.get_ValueAsInt("C_Invoice_ID") ;
			
			PreparedStatement pstmtANC = DB.prepareStatement(sqlANC, get_TrxName());					
			ResultSet rsANC = pstmtANC.executeQuery();
			while (rsANC.next())
			{
				if (rsANC.getInt("A_Asset_ID") > 0)
				{
					X_A_Asset assetANC = new X_A_Asset (getCtx(), rsANC.getInt("A_Asset_ID"), null);
					assetANC.setIsFullyDepreciated(true);
					assetANC.setLastMaintenanceNote("Venta Activo");
					assetANC.set_CustomColumn("AssetStatus", "BAJ");					
					assetANC.save();
					assetANC.setIsActive(false);
					assetANC.set_CustomColumn("A_Asset_RevalDate",DepDoc.getDateDoc());
					assetANC.save();
						
					int cantWK =DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM A_Depreciation_Workfile WHERE IsActive = 'Y' AND A_Asset_ID  = "+assetANC.get_ID());
					if (cantWK > 0)
					{
						MDepreciationWorkfile workfile=MAsset.getWorkFile(assetANC.get_ID());
						workfile.setA_Calc_Accumulated_Depr(workfile.getA_Accumulated_Depr()); //guardo por si necesito volver al estado anterior
						workfile.setA_Accumulated_Depr(workfile.getA_Asset_Cost());
						workfile.save();
					}						
					DB.executeUpdate("Update A_Asset_Forecast set processed='Y', corrected='Y' where A_Asset_ID="+assetANC.get_ID(), get_TrxName());
				}
			}
		}							
		
		DepDoc.setDocStatus("CO");
		DepDoc.setProcessed(true);
		DepDoc.save();
	}	
	else if (DepDoc.getDocStatus().equals("CO") && DepDoc.isVoid())
	{
		batch = new MJournalBatch(m_ctx,DepDoc.getGL_JournalBatch_ID(),get_TrxName());
		
		 
		 MJournal[] journals = batch.getJournals(true);
		 for (int i = 0; i < journals.length; i++)
			{
				MJournal jour = journals[i];
				
				if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Depreciacion ))){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().subtract(jour.getTotalCr()) );
					workfile.setA_Period_Posted(workfile.getA_Period_Posted()-1);
					workfile.setAssetDepreciationDate(DepDoc.getDateDoc());
					workfile.save();
					
					X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),get_TrxName());
					fore.setProcessed(false);
					fore.setGL_Journal_ID(0);
					fore.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria ))){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					//MAsset asset=new MAsset(Env.getCtx(),jour.get_ValueAsInt("A_Asset_ID"),get_TrxName());
					MAssetAcct group=new MAssetAcct(getCtx(), MAssetAcct.getAssetAcct_ID(jour.get_ValueAsInt("A_Asset_ID")),get_TrxName());
					BigDecimal acum=DB.getSQLValueBD(get_TrxName(),  "select sum(AmtAcctDr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
							+" and C_ValidCombination_ID="+group.getA_Depreciation_Acct());
					BigDecimal neto=DB.getSQLValueBD(get_TrxName(), "select sum(AmtAcctCr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
							+" and C_ValidCombination_ID="+group.getA_Accumdepreciation_Acct());
					workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().subtract(acum) );
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().subtract(neto));
					workfile.save();
					
					X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),null);
					fore.setCorrected(false);
					fore.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Reevaluacion))){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().subtract(jour.getTotalCr()));
					workfile.save();
					
				}
				else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Deteriodo))){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().add(jour.getTotalCr()));
					workfile.save();
				}
				else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_VentaActivo))){
					MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Accumulated_Depr(workfile.getA_Calc_Accumulated_Depr() );//antiguo valor antes de vender
					workfile.save();
					DB.executeUpdate("Update A_Asset_Forecast set processed='N', corrected='N' where gl_journal_id is null and A_Asset_ID="+jour.get_ValueAsInt("A_Asset_ID"), get_TrxName());
				}
			}
		 
		 
		 //borrado anulacion 
		 for(MJournal j:journals)
		 {
			 DB.executeUpdate("delete from fact_acct where record_id="+j.getGL_Journal_ID()+" and ad_table_id="+I_GL_Journal.Table_ID, get_TrxName());
			 DB.executeUpdate("delete from gl_journalline where gl_journal_id="+j.getGL_Journal_ID(), get_TrxName());
		 }
		 
		 DB.executeUpdate("delete from gl_journal where gl_journalbatch_id="+batch.getGL_JournalBatch_ID(), get_TrxName());
		 
		 /*batch.reverseCorrectIt();
		 batch.setProcessed(true);
		 batch.save();*/
		 
		 DepDoc.setDocStatus("VO");
		 DepDoc.setProcessed(true);
		 DepDoc.save();
	}
	
	
	    this.commitEx();
		return "";
	}	//	doIt		
}	//	ImportPayment

