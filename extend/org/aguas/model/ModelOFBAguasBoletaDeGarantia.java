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
package org.aguas.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_DM_PerformanceBond_Proc;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company Sismode
 *
 *  @author Italo Niñoles
 */
public class ModelOFBAguasBoletaDeGarantia implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBAguasBoletaDeGarantia ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBAguasBoletaDeGarantia.class);
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
		engine.addModelChange(X_DM_PerformanceBond_Proc.Table_Name, this); 
		
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==X_DM_PerformanceBond_Proc.Table_ID)
		{
			X_DM_PerformanceBond_Proc proc = (X_DM_PerformanceBond_Proc) po;
			
			//validacion de fechas
			Timestamp dateStat = (Timestamp)proc.get_Value("DateTrxRef");
			Timestamp dateEnd = (Timestamp)proc.get_Value("DateAcctRef");
			Timestamp dateReceipt = (Timestamp)proc.get_Value("DateReception");
			if (dateStat != null && dateEnd != null)
			{
				if (dateStat.compareTo(dateEnd) >= 0)
				{
					return "Error: Fecha Emision Debe Ser menor a Fecha de Vencimiento";
				}
			}		
			//@mfrojas validacion de fecha de recepcion y vencimiento
			if(dateReceipt != null && dateEnd != null)
			{
				if(dateReceipt.compareTo(dateEnd) >=0)
				{
					return "Error: Fecha de recepción no puede ser posterior al vencimiento";
				}
			}
			//ininoles validacion de moneda
			if (proc.getC_DocType().getDocBaseType().equals("PBA"))
			{
				BigDecimal d = proc.getAmt();
				BigDecimal result = d.subtract(d.setScale(0, RoundingMode.FLOOR)).movePointRight(d.scale());      
				if (result.compareTo(Env.ZERO) > 0)
				{
					if (proc.getC_Currency_ID() == 228)
					{
						return "Error: Moneda no soporta numero con decimales";
					}
				}
			}
			
			//validacion de numero
			if (proc.getC_DocType().getDocBaseType().equals("PBA"))
			{
				String documentNo = (String)proc.get_Value("DocumentRefNo");
					
				if (isNumeric(documentNo))
					;
				else
					return "Error: Numero de Boleta No Válido";
				
				// end ininoles
				String sqlCant = "SELECT COUNT(1) FROM DM_PerformanceBond_Proc WHERE DocumentRefNo = '"+proc.get_ValueAsString("DocumentRefNo")
						+"' AND C_Bank_ID = "+proc.get_ValueAsInt("C_Bank_ID")+" AND DM_PerformanceBond_Proc_ID <> "+proc.get_ID();
				
				int cantValid = DB.getSQLValue(po.get_TrxName(), sqlCant);
				
				if (cantValid > 0)
					return "Error: Numero de Boleta ya Usado";
				
				if (proc.getAmt().compareTo(Env.ZERO) <= 0)
				{
					return "Error:  Monto No Puede ser 0";
				}
				//validaciones fechas de alta
				Timestamp emision = proc.getDateTrxRef();
				Timestamp recepcion = (Timestamp)proc.get_Value("DateReception");
				
				if (emision != null && recepcion != null)
				{
					if (recepcion.compareTo(emision) < 0)
					{
						return "Error: Fecha de Recepcion NO Valida";
					}
				}
				// \begin mfrojas
				if(type == TYPE_BEFORE_NEW)
				{
					int actualrol = Env.getContextAsInt(Env.getCtx(), "#AD_Role_ID");
					
					//solo tesoreria puede ingresar boletas (dar de alta inicialmente)
					if(actualrol != 1000048 && actualrol != 1000000)
					{
						return "Sólo el rol de tesorería puede dar de alta una boleta.";
					}
				}// \end mfrojas
			}
			if (proc.getC_DocType().getDocBaseType().equals("PBD"))
			{
				Timestamp Nvencimiento = (Timestamp)proc.get_Value("DateNew");
				Timestamp Vencimiento = (Timestamp)proc.get_Value("DateAcctRef2");
				
				if (Nvencimiento != null && Vencimiento != null)
				{
					if (Nvencimiento.compareTo(Vencimiento) <= 0)
					{
						return "Error: Nueva Fecha de Vencimiento Invalida";
					}
				}
			}
		}
		
		if(type == TYPE_BEFORE_NEW && po.get_Table_ID()==X_DM_PerformanceBond_Proc.Table_ID)
		{			
			X_DM_PerformanceBond_Proc proc = (X_DM_PerformanceBond_Proc) po;
			
			if (proc.getC_DocType().getDocBaseType().equals("PBA"))
			{
				int correlative = 0;
				correlative = DB.getSQLValue(po.get_TrxName(), "SELECT COALESCE(MAX(Correlative),0) FROM DM_PerformanceBond_Proc ");
				if (correlative < 1)
					correlative = 1;
			
				proc.set_CustomColumn("Correlative", correlative+1);
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

	public static boolean isNumeric(String cadena)
	{
		try 
		{
			Integer.parseInt(cadena);
			return true;
		} 
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}

}	