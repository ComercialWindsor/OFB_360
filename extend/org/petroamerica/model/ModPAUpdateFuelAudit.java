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
package org.petroamerica.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_PA_FuelAudit;
import org.compiere.model.X_TP_PumpFuel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	Validator for PA 
 *
 *  @author Italo Niñoles
 */
public class ModPAUpdateFuelAudit implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPAUpdateFuelAudit ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPAUpdateFuelAudit.class);
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

		//	Tables to be monitored
		engine.addModelChange(X_PA_FuelAudit.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==X_PA_FuelAudit.Table_ID) 
		{	
			X_PA_FuelAudit fa = (X_PA_FuelAudit)po;
			//actualizacion de auditoria 
			if(fa.getType().compareToIgnoreCase("FI") == 0 && fa.isProcessed() == false
					&& fa.getTP_PumpFuel_ID() > 0
					&& fa.getAuditedAmount() != null && fa.getAuditedAmount().compareTo(Env.ZERO) > 0)
			{
				/*
				int ID_FAuditOld = DB.getSQLValue(po.get_TrxName(),"SELECT MAX(PA_FuelAudit_ID) FROM PA_FuelAudit " +
						" WHERE IsActive = 'Y' AND PA_FuelAudit_ID <> "+fa.get_ID()+
						" AND TP_PumpFuel_ID = "+fa.getTP_PumpFuel_ID()+" AND M_WareHouse_ID = "+fa.getM_Warehouse_ID()+
						" AND M_Locator_ID = "+fa.getM_Locator_ID());
				if(ID_FAuditOld > 0)
				{
					X_PA_FuelAudit oldFA = new X_PA_FuelAudit(po.getCtx(), ID_FAuditOld, po.get_TrxName());
					BigDecimal dif = fa.getAuditedAmount().subtract(oldFA.getAuditedAmount());				
					DB.executeUpdate("UPDATE PA_FuelAudit SET DifAuditedAmount = "+dif+" WHERE PA_FuelAudit_ID = "+fa.get_ID(), po.get_TrxName());
				}*/
				//ahora se trae mayor auditoria numeral en vez de ID
				Calendar calendar = Calendar.getInstance();
		        calendar.setTimeInMillis(fa.getDateTrx().getTime());
		        calendar.set(Calendar.HOUR_OF_DAY, 0);
		        calendar.set(Calendar.MINUTE, 0);
		        calendar.set(Calendar.SECOND, 0);
		        
				Timestamp minDate = new Timestamp(calendar.getTimeInMillis());
				//calendar.setTimeInMillis(fa.getDateTrx().getTime());
		        calendar.set(Calendar.HOUR_OF_DAY, 23);
		        calendar.set(Calendar.MINUTE, 59);
		        calendar.set(Calendar.SECOND, 59);
				
		        Timestamp maxDate = new Timestamp(calendar.getTimeInMillis());
				BigDecimal amtAuditOld = DB.getSQLValueBD(po.get_TrxName(),"SELECT MAX(auditedamount) FROM PA_FuelAudit " +
						" WHERE IsActive = 'Y' AND PA_FuelAudit_ID <> "+fa.get_ID()+
						" AND TP_PumpFuel_ID = "+fa.getTP_PumpFuel_ID()+" AND M_WareHouse_ID = "+fa.getM_Warehouse_ID()+
						" AND M_Locator_ID = "+fa.getM_Locator_ID()+
						" AND Type = 'IN' " +
						" AND DateTrx BETWEEN ? AND ?", minDate, maxDate);
				if(amtAuditOld != null && amtAuditOld.compareTo(Env.ZERO) > 0)
				{
					BigDecimal dif = fa.getAuditedAmount().subtract(amtAuditOld);				
					DB.executeUpdate("UPDATE PA_FuelAudit SET DifAuditedAmount = "+dif+" WHERE PA_FuelAudit_ID = "+fa.get_ID(), po.get_TrxName());
				}
			}
			boolean isManual = false;
			isManual = fa.get_ValueAsBoolean("IsManual");
			if(fa.isProcessed() == false && fa.getM_Locator_ID() > 0 && isManual == false)
			{
				if(fa.getTP_PumpFuel_ID() > 0)
				{
					X_TP_PumpFuel pFuel = new X_TP_PumpFuel(po.getCtx(), fa.getTP_PumpFuel_ID(), po.get_TrxName());
					if(pFuel.get_ValueAsBoolean("IsDefault"))
					{
						BigDecimal qtyOnHand = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qtyonhand) " +
								" FROM M_Storage WHERE M_Locator_ID = ? AND IsActive = 'Y'",fa.getM_Locator_ID());
						if(qtyOnHand == null)
							qtyOnHand = Env.ZERO;
						DB.executeUpdate("UPDATE PA_FuelAudit SET QtyOnHand = "+qtyOnHand+" WHERE PA_FuelAudit_ID = "+fa.get_ID(), po.get_TrxName());
					}
				}				
			}
		}		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==X_PA_FuelAudit.Table_ID && po.is_ValueChanged("Processed")) 
		{	
			X_PA_FuelAudit fa = (X_PA_FuelAudit)po;
			//Creacion de auditorio inicial para dia posterior 
			if(fa.getType().compareToIgnoreCase("FI") == 0 && fa.isActive() && fa.isProcessed())
			{
				X_PA_FuelAudit faNew = new X_PA_FuelAudit(po.getCtx(), 0, po.get_TrxName());
				faNew.setAD_Org_ID(fa.getAD_Org_ID());
				faNew.setType("IN");
				faNew.setIsActive(true);
				faNew.setM_Warehouse_ID(fa.getM_Warehouse_ID());
				faNew.setM_Locator_ID(fa.getM_Locator_ID());
				faNew.setM_Product_ID(fa.getM_Product_ID());
				//calculo dia siguiente
				Calendar calendar = Calendar.getInstance();
		        calendar.setTimeInMillis(fa.getDateTrx().getTime());
		        calendar.add(Calendar.DAY_OF_YEAR, 1);
		        Timestamp newDate = new Timestamp(calendar.getTimeInMillis());
		        //
		        faNew.setDateTrx(newDate);				
		        faNew.setTP_PumpFuel_ID(fa.getTP_PumpFuel_ID());
		        faNew.setAuditedAmount(fa.getAuditedAmount());
		        faNew.setDescription(fa.getDescription());
		        faNew.set_CustomColumn("IsManual", false);
		        faNew.setProcessed(false);
		        faNew.save();				
			}
		}		
		return null;
	}	//	modelChange

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


	

}	