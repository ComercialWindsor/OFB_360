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
package org.ofb.model;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_OtherExpenses;
import org.compiere.util.CLogger;

/**
 *	Validator for ANDES Colegios
 *
 *  @author Fabian Aguilar
 */
public class ModANDESOtherExpenses implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModANDESOtherExpenses ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModANDESOtherExpenses.class);
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
		//	Documents to be monitored
		
		engine.addModelChange(X_C_OtherExpenses.Table_Name, this);		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farías
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
			
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==X_C_OtherExpenses.Table_ID)
		{
			X_C_OtherExpenses oExpenses = (X_C_OtherExpenses)po;
			//validacion de monedas
			BigDecimal c20000 = new BigDecimal("20000.00");
			BigDecimal c10000 = new BigDecimal("10000.00");
			BigDecimal c5000 = new BigDecimal("5000.00");
			BigDecimal c2000 = new BigDecimal("2000.00");
			BigDecimal c1000 = new BigDecimal("1000.00");
			BigDecimal c500 = new BigDecimal("500.00");
			BigDecimal c100 = new BigDecimal("100.00");
			BigDecimal c50 = new BigDecimal("50.00");
			BigDecimal c10 = new BigDecimal("10.00");
			BigDecimal c5 = new BigDecimal("5.00");
			
			if(oExpenses.getcurrency_20000() != null && 
				oExpenses.getcurrency_20000().doubleValue() % c20000.doubleValue() > 0)
			{
				return "Monto de 20.000 no concordante";
			}
			if(oExpenses.getcurrency_10000() != null &&
				oExpenses.getcurrency_10000().doubleValue() % c10000.doubleValue() > 0)
			{
				return "Monto de 10.000 no concordante";
			}
			if(oExpenses.getcurrency_5000() != null &&
				oExpenses.getcurrency_5000().doubleValue() % c5000.doubleValue() > 0)
			{
				return "Monto de 5.000 no concordante";
			}
			if(oExpenses.getcurrency_2000() != null &&
				oExpenses.getcurrency_2000().doubleValue() % c2000.doubleValue() > 0)
			{
				return "Monto de 2.000 no concordante";
			}
			if(oExpenses.getcurrency_1000() != null &&
				oExpenses.getcurrency_1000().doubleValue() % c1000.doubleValue() > 0)
			{
				return "Monto de 1.000 no concordante";
			}
			if(oExpenses.getcurrency_500() != null &&
				oExpenses.getcurrency_500().doubleValue() % c500.doubleValue() > 0)
			{
				return "Monto de 500 no concordante";
			}
			if(oExpenses.getcurrency_100() != null &&
				oExpenses.getcurrency_100().doubleValue() % c100.doubleValue() > 0)
			{
				return "Monto de 100 no concordante";
			}
			if(oExpenses.getcurrency_50() != null &&
					oExpenses.getcurrency_50().doubleValue() % c50.doubleValue() > 0)
			{
				return "Monto de 50 no concordante";
			}
			if(oExpenses.getcurrency_10() != null &&
					oExpenses.getcurrency_10().doubleValue() % c10.doubleValue() > 0)
			{
				return "Monto de 10 no concordante";
			}
			if(oExpenses.getcurrency_5() != null &&
				oExpenses.getcurrency_5().doubleValue() % c5.doubleValue() > 0)
			{
				return "Monto de 5 no concordante";
			}
			//validacion de fechas
			if(oExpenses.getDocumentDate() != null || oExpenses.getDatePromised() != null)
			{
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY,0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				Timestamp today = new Timestamp(calendar.getTimeInMillis());
				Timestamp docDate = oExpenses.getDocumentDate(); 
				Timestamp proDate = oExpenses.getDatePromised();
				Calendar calendarAux = Calendar.getInstance();
				
				if(docDate != null)
				{
					calendarAux.setTimeInMillis(docDate.getTime());
					calendarAux.set(Calendar.HOUR_OF_DAY, 0);
					calendarAux.set(Calendar.MINUTE, 0);
					calendarAux.set(Calendar.SECOND, 0);
					calendarAux.set(Calendar.MILLISECOND, 0);
					docDate = new Timestamp(calendarAux.getTimeInMillis());
					if(docDate.compareTo(today) < 0 )
					{
						log.config("Fecha hoy: "+today+" Fecha Documento: "+docDate);
						return "Fecha Incorrecta";
					}
				}
				if(proDate != null)
				{
					calendarAux.setTimeInMillis(proDate.getTime());
					calendarAux.set(Calendar.HOUR_OF_DAY, 0);
					calendarAux.set(Calendar.MINUTE, 0);
					calendarAux.set(Calendar.SECOND, 0);
					calendarAux.set(Calendar.MILLISECOND, 0);
					proDate = new Timestamp(calendarAux.getTimeInMillis());
					if(proDate.compareTo(today) < 0 )
					{
						log.config("Fecha hoy: "+today+" Fecha Prometida: "+proDate);
						return "Fecha Incorrecta";
					}
				}
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

