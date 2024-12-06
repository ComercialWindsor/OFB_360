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
import org.compiere.model.X_M_Movement;
import org.compiere.model.X_M_MovementLine;
import org.compiere.model.X_TP_Destination;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;



/**
 *	Validator for company Sismode
 *
 *  @author Italo Niñoles
 */
public class ModelOFBTransportes implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBTransportes ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBTransportes.class);
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
		engine.addModelChange(X_M_Movement.Table_Name, this); // ID tabla 323
		engine.addModelChange(X_M_MovementLine.Table_Name, this);
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		//validaciones cabecera
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==323)//cabecera hr
		{
			X_M_Movement mm = (X_M_Movement) po;
			
			BigDecimal tpIni = (BigDecimal)mm.get_Value("TP_InicialKm");
			BigDecimal tpFin = (BigDecimal)mm.get_Value("TP_FinalKm");
			
			if(tpIni.compareTo(tpFin) > 0)
			{
				return "Odometro Inicial no puede ser Mayor a Odometro Final";
			}
			
			if(tpIni.compareTo(new BigDecimal("0.0")) <= 0)
			{
				return "Odometro Inicial debe ser mayor a 0";
			}			
			if(tpFin.compareTo(new BigDecimal("0.0")) <= 0)
			{
				return "Odometro Final debe ser mayor a 0";
			}
			
			//validacion fechas ingreso actual
			
			Timestamp fhoy = new Timestamp (System.currentTimeMillis());
			Timestamp fmov = mm.getMovementDate();
			
			if (fmov.compareTo(fhoy) > 0)
			{
				return "Fecha Ingresa mayor a fecha actual";
			}
		}		
		
		//validaciones detalle hr
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==X_M_MovementLine.Table_ID)  
		{
			X_M_MovementLine mml = (X_M_MovementLine) po;			
			MMovement mm = new MMovement(po.getCtx(), mml.getM_Movement_ID(), po.get_TrxName());
						
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
			
			//nuevas validaciones de detalle
			//kmLlegada
			
			X_TP_Destination dest = new X_TP_Destination(po.getCtx(), mml.get_ValueAsInt("TP_Destination_ID"), po.get_TrxName());
			
			if (dest.get_ValueAsInt("C_ProjectOFB_ID") != mm.get_ValueAsInt("C_ProjectOFB_ID"))
			{
				return "Flota de Destino no coindice con Flota de Cabecera";
			}
		}
		if(type == TYPE_BEFORE_NEW  && po.get_Table_ID()==X_M_MovementLine.Table_ID) 
		{		
			X_M_MovementLine mml = (X_M_MovementLine) po;			
			//MMovement mm = new MMovement(po.getCtx(), mml.getM_Movement_ID(), po.get_TrxName());
			BigDecimal kmSalida = (BigDecimal)mml.get_Value("TP_FinalKM");
			
			String sqlV1 = "select coalesce((max(TP_inicialkm)),0) from m_movementLine where M_Movement_ID = ? and m_movementLine_ID <> ?";
			BigDecimal maxKmLine = DB.getSQLValueBD(po.get_TrxName(), sqlV1, mml.getM_Movement_ID(), mml.get_ID());
			
			if (maxKmLine == null)
				maxKmLine = Env.ZERO;
			
			BigDecimal maxKmCalLine = maxKmLine.add(new BigDecimal("1000.0"));
			
			if (kmSalida.compareTo(maxKmCalLine) > 0)
			{
				return "Odometro Inicial Fuera de Rango. Debe estar entre "+maxKmLine+" y "+maxKmCalLine;
			}
			
			if (kmSalida.compareTo(maxKmLine) <= 0)
			{
				return "Odometro Inicial Fuera de Rango. Debe estar entre "+maxKmLine+" y "+maxKmCalLine;
			}			
		}
		
		if(type == TYPE_BEFORE_NEW  && po.get_Table_ID()==X_M_Movement.Table_ID) 
		{		
			X_M_Movement mm = (X_M_Movement) po;
			
			BigDecimal tpIni = (BigDecimal)mm.get_Value("TP_InicialKm");
			
			String sqlV1 = "select max(TP_Finalkm) from m_movement where TP_Asset_ID = ? and m_movement_id <> ? and docstatus <> 'VO'";
			BigDecimal maxKm = DB.getSQLValueBD(po.get_TrxName(), sqlV1, mm.get_ValueAsInt("TP_Asset_ID"), mm.get_ID());
						
			BigDecimal maxKmCal = maxKm.add(new BigDecimal("500.0"));
			
			if(maxKmCal == null)
			{
				maxKmCal = Env.ZERO;
			}
			
			
			if (tpIni.compareTo(maxKmCal) > 0)
			{
				return "Odometro Inicial Fuera de Rango. Debe estar entre "+maxKm+" y "+maxKmCal;
			}
			
			if (tpIni.compareTo(maxKm) <0)
			{
				return "Odometro Inicial Fuera de Rango. Debe estar entre "+maxKm+" y "+maxKmCal;
			}
		}
		
	
		/*if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_DELETE) && po.get_Table_ID()==X_M_MovementLine.Table_ID)  
		{
			X_M_MovementLine mml = (X_M_MovementLine) po;			
			MMovement mm = new MMovement(po.getCtx(), mml.getM_Movement_ID(), po.get_TrxName());			
		
			//	validacion flota petrobras
			if (mm.get_ValueAsInt("C_ProjectOFB_ID") == 1000017)
			{
				String volMin = "SELECT MAX(LINE) FROM M_MovementLine mml "+
					"INNER JOIN TP_Destination des ON  (mml.TP_Destination_ID = des.TP_Destination_ID) "+
					"WHERE M_Movement_ID = "+mm.get_ID()+" AND line < "+mml.getLine()+" AND IsSource = 'Y' "; 
			
				int minLine = DB.getSQLValue(po.get_TrxName(), volMin);
			
				String volMax = "SELECT MIN(LINE) FROM M_MovementLine mml "+
					"INNER JOIN TP_Destination des ON  (mml.TP_Destination_ID = des.TP_Destination_ID) "+
					"WHERE M_Movement_ID = "+mm.get_ID()+" AND line >= "+mml.getLine()+" AND IsSource = 'Y'";
			
				int maxLine = DB.getSQLValue(po.get_TrxName(), volMax);
				if (maxLine == 0)
					maxLine = 1000000;
			
			
				String costMax = "SELECT MAX(des.cost) FROM M_MovementLine mml "+
					"INNER JOIN TP_Destination des ON  (mml.TP_Destination_ID = des.TP_Destination_ID) "+
					"WHERE M_Movement_ID = "+mm.get_ID()+" AND line > "+minLine+" AND line <= "+maxLine+" AND IsSource = 'N'";

				BigDecimal maxC = (BigDecimal) DB.getSQLValueBD(po.get_TrxName(), costMax);

				String idMax = "SELECT max(m_movementline_id) FROM M_MovementLine mml "+
					"INNER JOIN TP_Destination des ON  (mml.TP_Destination_ID = des.TP_Destination_ID) "+
					"WHERE M_Movement_ID = "+mm.get_ID()+" AND line > "+minLine+" AND line <= "+maxLine+" AND IsSource = 'N' AND des.cost = "+maxC;
			
				int maxID = DB.getSQLValue(po.get_TrxName(), idMax);
				
				String sqlUpdate = "UPDATE M_MovementLine SET TP_LineNo = 90 "+
							"WHERE M_Movement_ID = "+mm.get_ID()+" AND line >= "+minLine+" AND line <= "+maxLine+
							" AND M_MovementLine_ID <> "+maxID;
			
				DB.executeUpdate(sqlUpdate, po.get_TrxName());
				
				String sqlUpdate2 = "UPDATE M_MovementLine SET TP_LineNo = 0 "+
						"WHERE M_Movement_ID = "+mm.get_ID()+" AND M_MovementLine_ID = "+maxID+" AND TP_LineNo = 90 ";
		
				DB.executeUpdate(sqlUpdate2, po.get_TrxName());
						
			}
		}*/
		
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