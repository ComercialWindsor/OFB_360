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
import java.sql.Timestamp;
import org.compiere.model.MClient;
import org.compiere.model.MMovement;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_M_MovementLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company TSM
 *
 *  @author Italo Niñoles
 */
public class Mod_TSM_ValidacionDetVolumen implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public Mod_TSM_ValidacionDetVolumen ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(Mod_TSM_ValidacionDetVolumen.class);
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
		engine.addModelChange(X_M_MovementLine.Table_Name, this);
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		//validaciones detalle hr
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==X_M_MovementLine.Table_ID)  
		{
			X_M_MovementLine mml = (X_M_MovementLine) po;			
			//MMovement mm = new MMovement(po.getCtx(), mml.getM_Movement_ID(), po.get_TrxName());
						
			BigDecimal kmSalida = (BigDecimal)mml.get_Value("TP_FinalKM");
			BigDecimal kmLlegada = (BigDecimal)mml.get_Value("TP_InicialKm");
			
			if (kmSalida == null)
				kmSalida = Env.ZERO;
			if (kmLlegada == null)
				kmLlegada = Env.ZERO;
			
			
			//validacion km
			if(kmSalida.compareTo(kmLlegada) > 0)//validacion kilometros 
			{
				return "KM Salida No puede ser Mayor a KM Llegada";
			}
			
			Timestamp inicio = (Timestamp)mml.get_Value("TP_FinalHR");
			Timestamp termino = (Timestamp)mml.get_Value("TP_InicialHR");
			
			//validacion fechas
			
			if (inicio != null && termino != null)
			{
				if (termino.compareTo(inicio) < 0)
				{
					return "Fecha de Termino no puede ser Menor que Fecha de Inicio";
				}
			}
			
			//validaciones enteros y decimales
			BigDecimal volIn = (BigDecimal)mml.get_Value("TP_VolIn");
			BigDecimal m3dev = (BigDecimal)mml.get_Value("TP_M3Devol");
			BigDecimal volm3 = (BigDecimal)mml.get_Value("TP_volM3");
			
			if (volIn == null)
				volIn = Env.ZERO;			
			if (m3dev == null)
				m3dev = Env.ZERO;
			if (volm3 == null)
				volm3 = Env.ZERO;
			
			if (volIn.compareTo(new BigDecimal("100.0")) >= 0 || m3dev.compareTo(new BigDecimal("100.0")) >= 0 || volm3.compareTo(new BigDecimal("100.0")) >= 0) 
			{
				return "Valor Solo Puede tener 2 enteros y 2 decimales";
			}
			
			BigDecimal fraction1 = volIn.remainder(BigDecimal.ONE);
			String frac1 = fraction1.toString();
			frac1 = rtrim(frac1,'0');
			
			if (frac1.equals("0E-12"))
				frac1 = "0";
			
			BigDecimal fraction2 = m3dev.remainder(BigDecimal.ONE);
			String frac2 = fraction2.toString();
			frac2 = rtrim(frac2,'0');
			
			if (frac2.equals("0E-12"))
				frac2 = "0";
			
			BigDecimal fraction3 = volm3.remainder(BigDecimal.ONE);
			String frac3 = fraction3.toString();
			frac3 = rtrim(frac3,'0');
			
			if (frac3.equals("0E-12"))
				frac3 = "0";
						
			if (frac1.length()> 4 || frac2.length()> 4 || frac3.length()> 4)
			{
				return "Valor Solo Puede tener 2 enteros y 2 decimales";
			}
		}
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==X_M_MovementLine.Table_ID) 
		{		
			X_M_MovementLine mml = (X_M_MovementLine) po;
			MMovement mo = new MMovement(po.getCtx(), mml.getM_Movement_ID(), po.get_TrxName());
			BigDecimal kmSalida = (BigDecimal)mml.get_Value("TP_FinalKM");
			
			int cantLineas = DB.getSQLValue(po.get_TrxName(), "select count(1) from m_movementLine where M_Movement_ID=? and m_movementLine_id <> ?", mo.get_ID(),mml.get_ID()); 
			
			BigDecimal maxKmLine;
			
			if (cantLineas < 1)
			{
				maxKmLine = (BigDecimal)mo.get_Value("TP_InicialKM");
			}else
			{
				String sqlV1 = "select coalesce((max(mml.TP_inicialkm)),0) from m_movementLine mml "+
						"inner join m_movement mm on (mml.m_movement_ID = mm.m_movement_ID) "+
						"where mm.TP_Asset_ID = ? and mm.docstatus <> 'VO' and m_movementLine_ID <> ?";
				maxKmLine = DB.getSQLValueBD(po.get_TrxName(), sqlV1, mo.get_ValueAsInt("TP_Asset_ID"), mml.get_ID());								
			}
			
			String activity = mml.get_ValueAsString("TP_ActivityCode");
			
			if (maxKmLine != null && activity.equalsIgnoreCase("01"))				
			{
				BigDecimal maxKmCalLine = maxKmLine.add(Env.ONE);
				
				if (kmSalida.compareTo(maxKmLine) < 0 || kmSalida.compareTo(maxKmCalLine) > 0)
				{
					return "Odometro Inicial Fuera de Rango. Ultimo odometro: "+maxKmLine;
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