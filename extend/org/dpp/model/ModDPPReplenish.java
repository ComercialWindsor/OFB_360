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
package org.dpp.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.MAsset;
import org.compiere.model.MAssetAcct;
import org.compiere.model.MAssetChange;
import org.compiere.model.MClient;
import org.compiere.model.MDepreciationWorkfile;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_A_Asset;
import org.compiere.model.X_A_Asset_Dep;
import org.compiere.model.X_A_Asset_Forecast;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for PDV Colegios
 *
 *  @author Fabian Aguilar
 */
public class ModDPPReplenish implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModDPPReplenish ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModDPPReplenish.class);
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
			log.info("Initializing Model Price Validator: "+this.toString());
		}
		//	Documents to be monitored		
		engine.addModelChange(X_A_Asset_Dep.Table_Name, this);
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Far�as
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if(type == TYPE_AFTER_CHANGE && po.get_Table_ID() == X_A_Asset_Dep.Table_ID)
		{
			X_A_Asset_Dep assetDep = (X_A_Asset_Dep) po;
			
			if(assetDep.getDocStatus().compareTo("CO") == 0)
			{
				String sql="SELECT jo.A_Asset_ID, C.A_Asset_Acct_ID FROM A_Asset_Dep ad" +
						" INNER JOIN GL_JournalBatch jb ON (ad.GL_JournalBatch_ID = jb.GL_JournalBatch_ID)" +
						" INNER JOIN GL_Journal jo ON (jb.GL_JournalBatch_ID = jo.GL_JournalBatch_ID)" +
						" INNER JOIN A_Asset_Acct C on (jo.A_Asset_ID=C.A_Asset_ID)" +
						" WHERE A_Asset_Dep_ID = ?";
				
				PreparedStatement pstmt = null;
				try
				{
					pstmt = DB.prepareStatement(sql, po.get_TrxName());
					pstmt.setInt(1, assetDep.get_ID());
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
						//DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and A_Asset_ID="+rs.getInt(1), get_TrxName());
						X_A_Asset asset= new X_A_Asset(po.getCtx(), rs.getInt(1), po.get_TrxName());
						MDepreciationWorkfile workfile=MAsset.getWorkFile(rs.getInt(1));
						MAssetAcct acct= new MAssetAcct(po.getCtx(), rs.getInt(2), po.get_TrxName());
						if(workfile==null || workfile.getA_Period_Posted()==acct.getA_Period_End())
							continue;
						
						replanningForecast(asset, workfile.getA_Asset_Cost().subtract(workfile.getA_Accumulated_Depr()),acct, 
								workfile.getA_Period_Posted(),
								DB.getSQLValueTS(po.get_TrxName(), "select max(datedoc) from a_asset_forecast where corrected='Y' and A_Asset_ID="+rs.getInt(1)),
								po.get_TrxName());
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, sql, e);
				}
			}			
		}				
	return null;
	}	//	modelChange

	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.x
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
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString	
	

	public static void createForecast(X_A_Asset asset,MAssetChange change,MAssetAcct  acct, String trxName)
	{
		//BigDecimal SalvageAmt =acct.getA_Salvage_Value().divide(Env.ONEHUNDRED);				 
		//SalvageAmt=change.getAssetValueAmt().multiply(SalvageAmt);
		//ininoles se cambia que campo A_Salvage_Value no sea porcentaje sino monto
		BigDecimal SalvageAmt =acct.getA_Salvage_Value();
		BigDecimal Currentamt=change.getAssetValueAmt();
		//Currentamt=Currentamt.subtract(Env.ONE);ininoles
		
		if(!acct.get_ValueAsBoolean("IsByYear")) //faaguilar por mes
			for(int i=1;i<(acct.getA_Period_End()+1) ;i++){
				X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),0,trxName);
				fore.setA_Asset_ID(asset.getA_Asset_ID());
				fore.setAD_Org_ID(asset.getAD_Org_ID());
				Calendar cal=Calendar.getInstance();
				cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
				cal.add(Calendar.MONTH, i);
				fore.setDateDoc(new Timestamp(cal.getTimeInMillis()));
				fore.setPeriodNo(i);
				//fore.setAmount(Currentamt.divide(SalvageAmt.intValue()<=0?new BigDecimal(acct.getA_Period_End()):new BigDecimal(acct.getA_Period_End()-1),2, BigDecimal.ROUND_DOWN));
				BigDecimal amtAcct = Currentamt.divide(new BigDecimal(acct.getA_Period_End()),2, BigDecimal.ROUND_DOWN);
				if(i == acct.getA_Period_End())
				{
					amtAcct = amtAcct.subtract(SalvageAmt);				
				}
				fore.setAmount(amtAcct.setScale(2, BigDecimal.ROUND_DOWN));			
				fore.getAmount();
				fore.save();
			}
		else//faaguilar por año
			for(int i=0;i<(asset.getUseLifeYears()) ;i++){
				X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),0,trxName);
				fore.setA_Asset_ID(asset.getA_Asset_ID());
				fore.setAD_Org_ID(asset.getAD_Org_ID());
				Calendar cal=Calendar.getInstance();
				cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
				cal.set(Calendar.MONTH, Calendar.DECEMBER);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.YEAR, i);
				fore.setDateDoc(new Timestamp(cal.getTimeInMillis()));
				fore.setPeriodNo(i+1);
				acct.save();
				//fore.setAmount(Currentamt.divide(SalvageAmt.intValue()<=0?new BigDecimal(acct.getA_Period_End()):new BigDecimal(acct.getA_Period_End()-1),2, BigDecimal.ROUND_DOWN));
				BigDecimal divisor = new BigDecimal("0.0");
				if (acct.get_ValueAsBoolean("IsByYear"))
				{
					divisor = new BigDecimal(asset.getUseLifeYears());
				}else
				{
					divisor = new BigDecimal(acct.getA_Period_End());
				}
				
				BigDecimal amtAcct = Currentamt.divide(divisor,2, BigDecimal.ROUND_DOWN);
				if(i+1 == acct.getA_Period_End())
				{
					amtAcct = amtAcct.subtract(SalvageAmt);				
				}
				fore.setAmount(amtAcct.setScale(2, BigDecimal.ROUND_DOWN));			
				fore.getAmount();
				fore.save();
			}
			
		//ininoles las planificaciones se generan siempre en el ciclo 
		/*if(SalvageAmt.intValue()>0){
		X_A_Asset_Forecast forelast=new X_A_Asset_Forecast (Env.getCtx(),0,trxName);
		forelast.setA_Asset_ID(asset.getA_Asset_ID());
		forelast.setAD_Org_ID(asset.getAD_Org_ID());
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
		cal.add(Calendar.MONTH, acct.getA_Period_End());
		forelast.setDateDoc(new Timestamp(cal.getTimeInMillis()));
		forelast.setPeriodNo(acct.getA_Period_End());
		forelast.setAmount(SalvageAmt);
		forelast.save();
		}*/
	}
	//replanning forecast 
	public void replanningForecast(X_A_Asset asset,BigDecimal Amount,MAssetAcct  acct, int Period, Timestamp lastdate, String trxName )
	{
		
		//ininoles modificaciones para nueva forma de calculo de planificacion
		//BigDecimal SalvageAmt =acct.getA_Salvage_Value().divide(Env.ONEHUNDRED);
		//SalvageAmt=Amount.multiply(SalvageAmt);
		BigDecimal SalvageAmt =acct.getA_Salvage_Value();
		BigDecimal Currentamt=Amount;
		//Currentamt=Currentamt.subtract(Env.ONE);
		//if(SalvageAmt.intValue()<=0)
		Currentamt=Currentamt.divide(new BigDecimal(acct.getA_Period_End()-Period),2, BigDecimal.ROUND_DOWN);
		//else
			//Currentamt=Currentamt.divide(Period==(acct.getA_Period_End()-1)? Env.ONE : new BigDecimal(acct.getA_Period_End()-1-Period),2, BigDecimal.ROUND_DOWN);
		
		DB.executeUpdate("Update A_Asset_Forecast set amount="+ Currentamt +" Where Processed='N' AND A_Asset_ID="+asset.getA_Asset_ID(),trxName);
		if(asset.isInPosession()){
			DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and Corrected='N' and A_Asset_ID="+asset.getA_Asset_ID(), trxName);
			Period=DB.getSQLValue(trxName, "select max(PeriodNo) from a_asset_forecast where corrected='Y' and A_Asset_ID="+asset.getA_Asset_ID());
		}
		else
			DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and A_Asset_ID="+asset.getA_Asset_ID(), trxName);	
		
		int mes=1;
		if(!acct.get_ValueAsBoolean("IsByYear")) //faaguilar por mes
			for(int i=Period+1;i<(acct.getA_Period_End()+1) ;i++){
				X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),0,null);
				fore.setA_Asset_ID(asset.getA_Asset_ID());
				fore.setAD_Org_ID(asset.getAD_Org_ID());
				Calendar cal=Calendar.getInstance();
				if(lastdate==null)
					cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
				else
					cal.setTimeInMillis(lastdate.getTime());
				cal.add(Calendar.MONTH, mes);
				fore.setDateDoc(new Timestamp(cal.getTimeInMillis()));
				fore.setPeriodNo(i);
				//fore.setAmount(Currentamt);
				BigDecimal amtAcct = Currentamt;
				if(i == acct.getA_Period_End())
				{
					amtAcct = amtAcct.subtract(SalvageAmt);				
				}
				fore.setAmount(amtAcct.setScale(2, BigDecimal.ROUND_DOWN));		
				fore.getAmount(); 
				fore.save();
				mes++;
			}
		else// faaguilar by year
			for(int i=Period+1;i<(asset.getUseLifeYears()+1) ;i++){
				X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),0,null);
				fore.setA_Asset_ID(asset.getA_Asset_ID());
				fore.setAD_Org_ID(asset.getAD_Org_ID());
				Calendar cal=Calendar.getInstance();
				if(lastdate==null)
					cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
				else
					cal.setTimeInMillis(lastdate.getTime());
				cal.add(Calendar.YEAR, mes);
				cal.set(Calendar.MONTH, Calendar.DECEMBER);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				fore.setDateDoc(new Timestamp(cal.getTimeInMillis()));
				fore.setPeriodNo(i);
				//fore.setAmount(Currentamt);
				BigDecimal amtAcct = Currentamt;
				if(i == acct.getA_Period_End())
				{
					amtAcct = amtAcct.subtract(SalvageAmt);				
				}
				fore.setAmount(amtAcct.setScale(2, BigDecimal.ROUND_DOWN));		
				fore.getAmount(); 
				fore.save();
				mes++;
			}
		/*if(SalvageAmt.intValue()>0){
		X_A_Asset_Forecast forelast=new X_A_Asset_Forecast (Env.getCtx(),0,null);
		forelast.setA_Asset_ID(asset.getA_Asset_ID());
		forelast.setAD_Org_ID(asset.getAD_Org_ID());
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
		cal.add(Calendar.MONTH, acct.getA_Period_End());
		forelast.setDateDoc(new Timestamp(cal.getTimeInMillis()));
		forelast.setPeriodNo(acct.getA_Period_End());
		forelast.setAmount(SalvageAmt);
		forelast.save();
		}*/
	}

}	
