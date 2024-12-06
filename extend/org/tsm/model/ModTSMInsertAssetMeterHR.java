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
package org.tsm.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.model.MClient;
import org.compiere.model.MMovement;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_MP_AssetMeter;
import org.compiere.model.X_MP_AssetMeter_Log;
import org.compiere.model.X_M_MovementLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for company TSM
 *
 *  @author Italo Niñoles
 */
public class ModTSMInsertAssetMeterHR implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTSMInsertAssetMeterHR ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTSMInsertAssetMeterHR.class);
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
		engine.addModelChange(X_M_MovementLine.Table_Name, this); 
		//	Documents to be monitored
		

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if(type == TYPE_AFTER_NEW && po.get_Table_ID()==X_M_MovementLine.Table_ID)  
		{
			X_M_MovementLine HRLine = (X_M_MovementLine) po;	
			//se calcula kms de linea de hr.
			BigDecimal dif = (BigDecimal)HRLine.get_Value("TP_InicialKM");
			if(dif == null)
				dif = Env.ZERO;
			dif = dif.subtract((BigDecimal)HRLine.get_Value("TP_FinalKM") );
			if(dif == null)
				dif = Env.ZERO;
			//si hay diferencia en el kilometraje 
			if(dif.compareTo(Env.ZERO) > 0)
			{	
				MMovement mov = new MMovement(po.getCtx(), HRLine.getM_Movement_ID(), po.get_TrxName());
				//se actualiza odometro de tracto
				if(mov.get_ValueAsInt("TP_Asset_Id") > 0)
				{
					//se busca registro maximo
					String sqlLast = "SELECT * FROM MP_AssetMeter_Log ml" +
							" INNER JOIN MP_AssetMeter m ON (ml.MP_AssetMeter_ID = m.MP_AssetMeter_ID)" +
							" WHERE ml.IsActive='Y' AND A_Asset_ID = "+mov.get_ValueAsInt("TP_Asset_Id")+" AND MP_Meter_ID=1000001" +
							" ORDER BY ml.amt Desc";
					int ID_AssetMLog = 0;
					BigDecimal lastOdo = Env.ZERO;
					Timestamp lastDate  = null;
					PreparedStatement pstmt = DB.prepareStatement(sqlLast, po.get_TrxName());
					ResultSet rs = pstmt.executeQuery();
					if(rs.next())
					{	
						lastOdo = rs.getBigDecimal("amt");
						lastDate = rs.getTimestamp("DateTrx");
						ID_AssetMLog = rs.getInt("MP_AssetMeter_Log_ID");
					}
					if(lastDate != null && lastOdo != null && lastOdo.compareTo(Env.ZERO) > 0)
					{
						//se crea registro nuevo
						if(mov.getMovementDate().compareTo(lastDate) > 0)
						{					
							int IDAMeter = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(MP_AssetMeter_ID) FROM MP_AssetMeter m " +
									" WHERE A_Asset_ID = "+mov.get_ValueAsInt("TP_Asset_Id")+" AND MP_Meter_ID=1000001");
							if(IDAMeter <= 0)
							{
								X_MP_AssetMeter aMeter = new X_MP_AssetMeter(po.getCtx(),0,po.get_TrxName());
								aMeter.setA_Asset_ID(mov.get_ValueAsInt("TP_Asset_Id"));
								aMeter.set_CustomColumn("MP_Meter_ID",1000001);
								aMeter.setName(" Auto.");
								aMeter.save();
								IDAMeter = aMeter.get_ID();
							}
								
							X_MP_AssetMeter_Log aMetLog = new X_MP_AssetMeter_Log(po.getCtx(), 0, po.get_TrxName());
							aMetLog.setMP_AssetMeter_ID(IDAMeter);
							aMetLog.setAD_Org_ID(mov.getAD_Org_ID());
							aMetLog.setDateTrx(mov.getMovementDate());
							aMetLog.setAD_User_ID(mov.getCreatedBy());
							aMetLog.setAmt(lastOdo.add(dif));
							aMetLog.setcurrentamt(lastOdo.add(dif));
							aMetLog.save();
						}
						else // se actualiza ultimo registro existente
						{
							X_MP_AssetMeter_Log aMetLog = new X_MP_AssetMeter_Log(po.getCtx(), ID_AssetMLog, po.get_TrxName());
							aMetLog.setAmt(lastOdo.add(dif));
							aMetLog.setcurrentamt(lastOdo.add(dif));
							aMetLog.save();
						}
					}
					
				}
				//se actualiza odometro de rampla.				
				if(mov.get_ValueAsInt("tp_asset_id2") > 0)
				{
					//se busca registro maximo
					String sqlLast = "SELECT * FROM MP_AssetMeter_Log ml" +
							" INNER JOIN MP_AssetMeter m ON (ml.MP_AssetMeter_ID = m.MP_AssetMeter_ID)" +
							" WHERE ml.IsActive='Y' AND A_Asset_ID = "+mov.get_ValueAsInt("tp_asset_id2")+" AND MP_Meter_ID=1000001" +
							" ORDER BY ml.amt Desc";
					int ID_AssetMLog = 0;
					BigDecimal lastOdo = Env.ZERO;
					Timestamp lastDate  = null;
					PreparedStatement pstmt = DB.prepareStatement(sqlLast, po.get_TrxName());
					ResultSet rs = pstmt.executeQuery();
					if(rs.next())
					{	
						lastOdo = rs.getBigDecimal("amt");
						lastDate = rs.getTimestamp("DateTrx");
						ID_AssetMLog = rs.getInt("MP_AssetMeter_Log_ID");
					}
					if(lastDate != null && lastOdo != null && lastOdo.compareTo(Env.ZERO) > 0)
					{
						//se crea registro nuevo
						if(mov.getMovementDate().compareTo(lastDate) > 0)
						{					
							int IDAMeter = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(MP_AssetMeter_ID) FROM MP_AssetMeter m " +
									" WHERE A_Asset_ID = "+mov.get_ValueAsInt("tp_asset_id2")+" AND MP_Meter_ID=1000001");
							if(IDAMeter <= 0)
							{
								X_MP_AssetMeter aMeter = new X_MP_AssetMeter(po.getCtx(),0,po.get_TrxName());
								aMeter.setA_Asset_ID(mov.get_ValueAsInt("tp_asset_id2"));
								aMeter.set_CustomColumn("MP_Meter_ID",1000001);
								aMeter.setName(" Auto.");
								aMeter.save();
								IDAMeter = aMeter.get_ID();
							}
								
							X_MP_AssetMeter_Log aMetLog = new X_MP_AssetMeter_Log(po.getCtx(), 0, po.get_TrxName());
							aMetLog.setMP_AssetMeter_ID(IDAMeter);
							aMetLog.setAD_Org_ID(mov.getAD_Org_ID());
							aMetLog.setDateTrx(mov.getMovementDate());
							aMetLog.setAD_User_ID(mov.getCreatedBy());
							aMetLog.setAmt(lastOdo.add(dif));
							aMetLog.setcurrentamt(lastOdo.add(dif));
							aMetLog.save();
						}
						else // se actualiza ultimo registro existente
						{
							X_MP_AssetMeter_Log aMetLog = new X_MP_AssetMeter_Log(po.getCtx(), ID_AssetMLog, po.get_TrxName());
							aMetLog.setAmt(lastOdo.add(dif));
							aMetLog.setcurrentamt(lastOdo.add(dif));
							aMetLog.save();
						}
					}
				}
			}
		}
		return null;
	}	//	modelChange
	
	public static String rtrim(String s, char c) {
	    int i = s.length()-1;
	    while (i >= 0 && s.charAt(i) == c)
	    {
	        i--;
	    }
	    return s.substring(0,i+1);
	}

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