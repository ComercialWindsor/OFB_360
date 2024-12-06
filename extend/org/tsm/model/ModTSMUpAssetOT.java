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
import java.util.logging.Level;

import org.compiere.model.MAsset;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_MP_AssetMeter;
import org.compiere.model.X_MP_AssetMeter_Log;
import org.compiere.model.X_MP_AssetPartner;
import org.compiere.model.X_MP_Meter;
import org.compiere.model.X_MP_OT;
import org.compiere.model.X_MP_OT_Task;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for company TSM
 *
 *  @author Italo Niñoles
 */
public class ModTSMUpAssetOT implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTSMUpAssetOT ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTSMUpAssetOT.class);
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
		engine.addModelChange(MAsset.Table_Name, this); 
		engine.addModelChange(X_MP_OT.Table_Name, this);

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)&& po.get_Table_ID()==MAsset.Table_ID)  
		{
			MAsset asset = (MAsset) po;
			if(asset.get_ValueAsBoolean("IsInWorkshop"))
			{
				DB.executeUpdate("UPDATE MP_AssetPartner SET IsActive = 'N' " +
						" WHERE A_AssetRef_ID = "+asset.get_ID(), po.get_TrxName());
			}
			if(asset.get_ValueAsString("custodio") != null && 
					(asset.get_ValueAsString("custodio").compareToIgnoreCase("TR") == 0 
							|| asset.get_ValueAsString("custodio").compareToIgnoreCase("TN") == 0))//agregamos codigo para copiar custodio
			{
				DB.executeUpdate("UPDATE MP_AssetPartner SET IsActive = 'N' " +
						" WHERE A_AssetRef_ID = "+asset.get_ID(), po.get_TrxName());
			}
		}
		
		if(type == TYPE_AFTER_CHANGE && po.get_Table_ID()==X_MP_OT.Table_ID 
				&& po.is_ValueChanged("DocStatus"))  
		{
			X_MP_OT ot = (X_MP_OT)po;
			if (ot.getDocStatus().compareToIgnoreCase("CO") == 0)
			{	
				String mysql="SELECT MP_OT_Task_ID FROM MP_OT_TASK WHERE MP_OT_ID = ?";
				PreparedStatement pstmt = null;
				try
				{
					pstmt = DB.prepareStatement(mysql, po.get_TrxName());
					pstmt.setInt(1, ot.get_ID());
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
						X_MP_OT_Task task= new X_MP_OT_Task(Env.getCtx(), rs.getInt(1),po.get_TrxName());
										
						//generamos registro de medicion				
						if (task.get_ValueAsInt("MP_Meter_ID") > 0)//si tiene medida
						{
							//validamos si ya estan relacionados
							int cantRe = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) " +
									" FROM MP_AssetPartner WHERE A_Asset_ID = "+ot.getA_Asset_ID()+" AND " +
									" A_AssetRef_ID = "+task.get_ValueAsInt("A_AssetRef_ID"));
							//si no existe se crea registro
							if(cantRe < 1)
							{
								X_MP_AssetPartner aPart = new X_MP_AssetPartner(po.getCtx(), 0, po.get_TrxName());
								aPart.setAD_Org_ID(ot.getAD_Org_ID());
								aPart.setA_Asset_ID(ot.getA_Asset_ID());
								aPart.setA_AssetRef_ID(task.get_ValueAsInt("A_AssetRef_ID"));
								aPart.save();
							}		
							//actualizamos los registros a isactive = Y
							DB.executeUpdate("UPDATE MP_AssetPartner SET IsActive = 'Y' WHERE A_Asset_ID = "+ot.getA_Asset_ID()+" AND " +
									" A_AssetRef_ID = "+task.get_ValueAsInt("A_AssetRef_ID"), po.get_TrxName());							
							
							X_MP_Meter met = new X_MP_Meter(po.getCtx(), task.get_ValueAsInt("MP_Meter_ID"), po.get_TrxName());
							if(met.get_ValueAsBoolean("UpdateFromOT") && met.get_ValueAsBoolean("NoValidationAMLog"))
							{
								int ID_AMetter = DB.getSQLValue(po.get_TrxName(), " SELECT MP_AssetMeter_ID FROM MP_AssetMeter  " +
										" WHERE IsActive = 'Y' AND A_Asset_ID = "+task.get_ValueAsInt("A_AssetRef_ID")+
										" AND MP_Meter_ID = "+met.get_ID());
								
								X_MP_AssetMeter aMeterRef = null;
								if (ID_AMetter > 0)
									 aMeterRef = new X_MP_AssetMeter(po.getCtx(),ID_AMetter,po.get_TrxName());						
								else
								{
									aMeterRef = new X_MP_AssetMeter(po.getCtx(),0,po.get_TrxName());
									aMeterRef.setA_Asset_ID(task.get_ValueAsInt("A_AssetRef_ID"));
									aMeterRef.set_CustomColumn("MP_Meter_ID",met.get_ID());
									aMeterRef.setName(met.getName()+" Auto.");
									aMeterRef.save();
								}					
								X_MP_AssetMeter_Log aMetLogRef = new X_MP_AssetMeter_Log(po.getCtx(), 0, po.get_TrxName());
								aMetLogRef.setMP_AssetMeter_ID(aMeterRef.get_ID());
								aMetLogRef.setAD_Org_ID(aMetLogRef.getAD_Org_ID());
								aMetLogRef.setDateTrx(ot.getDateTrx());
								aMetLogRef.setAD_User_ID(ot.getAD_Org_ID());
								aMetLogRef.setAmt((BigDecimal)task.get_Value("Amt"));
								aMetLogRef.setcurrentamt((BigDecimal)task.get_Value("Amt"));
								aMetLogRef.set_CustomColumn("A_AssetRef_ID",ot.getA_Asset_ID());
								aMetLogRef.save();
								aMeterRef.setAmt(aMetLogRef.getcurrentamt());								
								aMeterRef.save();
							}
							//buscamos si esta relacionado a otros activos y lo eliminamos
							DB.executeUpdate("DELETE FROM MP_AssetPartner WHERE A_Asset_ID <> "+ot.getA_Asset_ID()+
									" AND A_AssetRef_ID = "+task.get_ValueAsInt("A_AssetRef_ID"), po.get_TrxName());
							
							MAsset assetRef = new MAsset(po.getCtx(), task.get_ValueAsInt("A_AssetRef_ID"), po.get_TrxName());
							assetRef.set_CustomColumn("IsInWorkshop", false);
							assetRef.set_CustomColumn("custodio", "AS");
							assetRef.save();
						}		
						else if(task.get_ValueAsBoolean("IsInWorkshop"))
						{
							MAsset assetRef = new MAsset(po.getCtx(), task.get_ValueAsInt("A_AssetRef_ID"), po.get_TrxName());
							assetRef.set_CustomColumn("IsInWorkshop", true);
							assetRef.save();
						}
						else if(task.get_ValueAsString("custodio") != null && task.get_ValueAsString("custodio") != "" && task.get_ValueAsString("custodio") != "null")//agregamos codigo para copiar custodio
						{
							MAsset assetRef = new MAsset(po.getCtx(), task.get_ValueAsInt("A_AssetRef_ID"), po.get_TrxName());
							assetRef.set_CustomColumn("custodio", task.get_ValueAsString("custodio"));
							assetRef.save();
						}
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, mysql, e);
				}
			}
		}
		if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID()==X_MP_OT.Table_ID 
				&& po.is_ValueChanged("DocStatus"))  
		{
			X_MP_OT ot = (X_MP_OT)po;
			if (ot.getDocStatus().compareToIgnoreCase("DR") == 0 || ot.getDocStatus().compareToIgnoreCase("IP") == 0
					|| ot.getDocStatus().compareToIgnoreCase("CO") == 0)
			{	
				String mysql="SELECT MP_OT_Task_ID FROM MP_OT_TASK WHERE MP_OT_ID = ?";
				PreparedStatement pstmt = null;				
				ResultSet rs = null;
				pstmt = DB.prepareStatement(mysql, po.get_TrxName());
				pstmt.setInt(1, ot.get_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					X_MP_OT_Task task= new X_MP_OT_Task(Env.getCtx(), rs.getInt(1),po.get_TrxName());
									
					//generamos registro de medicion				
					if (task.get_ValueAsInt("MP_Meter_ID") > 0)//si tiene medida
					{
						X_MP_Meter met = new X_MP_Meter(po.getCtx(), task.get_ValueAsInt("MP_Meter_ID"), po.get_TrxName());
						if(met.getName().toUpperCase().contains("POSICI"))
						{
							MAsset assetRef = new MAsset(po.getCtx(), task.get_ValueAsInt("A_AssetRef_ID"), po.get_TrxName());
							BigDecimal amt = (BigDecimal)task.get_Value("Amt");
							if(assetRef.get_ValueAsBoolean("IsDirectional") && amt.compareTo(new BigDecimal("2.0")) > 0)
							{
								DB.executeUpdate("UPDATE MP_Ot SET Description = Description||' Error: Neumatico direccional en posicion no direccional' " +
										" WHERE MP_OT_ID = "+ot.get_ID(),null);
								//new AdempiereException("Error: Neumatico direccional en posicion no direccional");
								return "Error: Neumatico direccional en posicion no direccional";								
							}
						}
					}
				}
				rs.close(); pstmt.close();
				pstmt = null; rs = null;
			}
		}
		return null;
	}	//	modelChange	


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