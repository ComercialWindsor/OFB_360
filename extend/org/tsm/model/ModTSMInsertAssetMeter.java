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

import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_MP_AssetMeter;
import org.compiere.model.X_MP_AssetMeter_Log;
import org.compiere.model.X_MP_Meter;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for company TSM
 *
 *  @author Italo Niñoles
 */
public class ModTSMInsertAssetMeter implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTSMInsertAssetMeter ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTSMInsertAssetMeter.class);
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
		//	Documents to be monitored
		engine.addModelChange(X_MP_AssetMeter_Log.Table_Name, this); 

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if(type == TYPE_AFTER_NEW && po.get_Table_ID()==X_MP_AssetMeter_Log.Table_ID)  
		{
			X_MP_AssetMeter_Log aMetLog = (X_MP_AssetMeter_Log) po;	
			X_MP_AssetMeter aMet = new X_MP_AssetMeter(po.getCtx(), aMetLog.getMP_AssetMeter_ID(), po.get_TrxName());
			X_MP_Meter met = new X_MP_Meter(po.getCtx(), aMet.get_ValueAsInt("MP_Meter_ID"), po.get_TrxName());
			
			if(met.get_ValueAsBoolean("InsertAssetPartner"))
			{
				//calculamos la diferencia a agregar en los activos relacionados
				BigDecimal amtOld = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(Amt) FROM MP_AssetMeter_Log " +
						" WHERE IsActive = 'Y' AND MP_AssetMeter_ID = "+aMet.get_ID()+" AND Amt <> "+aMetLog.getAmt());
				if(amtOld == null)
					amtOld = Env.ZERO;
				//activos relacionados con posicion
				String sql = "SELECT DISTINCT(A_AssetRef_ID) as A_AssetRef_ID FROM MP_AssetPartner " +
						"WHERE IsActive = 'Y' AND A_Asset_ID = "+aMet.getA_Asset_ID();
				try
				{
					PreparedStatement pstmt = DB.prepareStatement(sql, po.get_TrxName());
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{	
						int ID_AMetter = DB.getSQLValue(po.get_TrxName(), " SELECT MP_AssetMeter_ID FROM MP_AssetMeter  " +
								" WHERE IsActive = 'Y' AND A_Asset_ID = "+rs.getInt("A_AssetRef_ID")+
								" AND MP_Meter_ID = "+met.get_ID());
						
						X_MP_AssetMeter aMeterRef = null;
						if (ID_AMetter > 0)
							 aMeterRef = new X_MP_AssetMeter(po.getCtx(),ID_AMetter,po.get_TrxName());						
						else
						{
							aMeterRef = new X_MP_AssetMeter(po.getCtx(),0,po.get_TrxName());
							aMeterRef.setA_Asset_ID(rs.getInt("A_AssetRef_ID"));
							aMeterRef.set_CustomColumn("MP_Meter_ID",met.get_ID());
							aMeterRef.setName(met.getName()+" Auto.");
							aMeterRef.save();
						}					
						X_MP_AssetMeter_Log aMetLogRef = new X_MP_AssetMeter_Log(po.getCtx(), 0, po.get_TrxName());
						aMetLogRef.setMP_AssetMeter_ID(aMeterRef.get_ID());
						aMetLogRef.setAD_Org_ID(aMetLogRef.getAD_Org_ID());
						aMetLogRef.setDateTrx(aMetLog.getDateTrx());
						aMetLogRef.setAD_User_ID(aMetLog.getAD_User_ID());
						aMetLogRef.setAmt(aMeterRef.getAmt().add(aMet.getAmt().subtract(amtOld)));
						aMetLogRef.setcurrentamt(aMeterRef.getAmt().add(aMet.getAmt().subtract(amtOld)));
						aMetLogRef.set_CustomColumn("A_AssetRef_ID",aMet.getA_Asset_ID());
						aMeterRef.setAmt(aMetLogRef.getcurrentamt());						
						aMeterRef.save();
						//aMetLogRef.save();						
						aMetLogRef.save();
						aMeterRef.setAmt(aMetLogRef.getcurrentamt());						
						aMeterRef.save();
					}
				
				}catch (Exception e)
				{
					log.config("Error recuperando Activos asociados "+sql);
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