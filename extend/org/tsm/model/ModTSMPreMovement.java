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
//import java.sql.Timestamp;
//import java.util.Calendar;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MCash;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_Pre_M_Movement;
import org.compiere.model.X_Pre_M_MovementLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
/**
 *	Validator for company TSM
 *
 *  @author Italo Niñoles
 */
public class ModTSMPreMovement implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTSMPreMovement ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTSMPreMovement.class);
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
		//engine.addModelChange(X_TP_Refund.Table_Name, this);
		engine.addModelChange(X_Pre_M_MovementLine.Table_Name, this);
		//	Documents to be monitored
		
		


	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
				
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==X_Pre_M_MovementLine.Table_ID)  
		{
			X_Pre_M_MovementLine pMovLine = (X_Pre_M_MovementLine) po;
			X_Pre_M_Movement preMov = new X_Pre_M_Movement(po.getCtx(), pMovLine.getPre_M_Movement_ID(), po.get_TrxName());
			
			if(pMovLine.isActive())
			{
				//validacion que no se repita la linea de activo
				if(pMovLine.getA_Asset_ID() > 0)
				{
					int cantPMovLine = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM Pre_M_Movement mm" +
							" INNER JOIN Pre_M_MovementLine mml ON (mm.Pre_M_Movement_ID = mml.Pre_M_Movement_ID) " +
							" WHERE mm.Pre_M_Movement_ID <> "+preMov.get_ID()+"AND mm.MovementDate = ? " +
							" AND mm.AD_Org_ID="+preMov.getAD_Org_ID()+" AND mm.Workshift='"+preMov.get_ValueAsString("Workshift")+"'"+
							" AND mml.A_Asset_ID = "+pMovLine.getA_Asset_ID(),preMov.getMovementDate());
					if(cantPMovLine > 0)
						throw new AdempiereException("ERROR: Ya existe una disponibilidad para el mismo activo:"+pMovLine.getA_Asset().getValue());
				}
				//validacion que no se repita la linea de socio de negocio
				if(pMovLine.getC_BPartner_ID() > 0)
				{
					int cantPMovLine = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM Pre_M_Movement mm" +
							" INNER JOIN Pre_M_MovementLine mml ON (mm.Pre_M_Movement_ID = mml.Pre_M_Movement_ID) " +
							" WHERE mm.Pre_M_Movement_ID <> "+preMov.get_ID()+" AND mm.MovementDate = ? " +
							" AND mm.AD_Org_ID="+preMov.getAD_Org_ID()+" AND mm.Workshift='"+preMov.get_ValueAsString("Workshift")+"'"+
							" AND mml.C_Bpartner_ID = "+pMovLine.getC_BPartner_ID(),preMov.getMovementDate());
					if(cantPMovLine > 0)
						throw new AdempiereException("ERROR: Ya existe una disponibilidad para el mismo conductor:"+pMovLine.getC_BPartner().getValue());
				}
				//validaciones de socio de negocio
				//String turno = preMov.get_ValueAsString("Workshift");
				//if(turno == null)
					//turno = "30";
				if(pMovLine.getC_BPartner_ID() > 0)
				{
					//validacion 6 dias seguidos trabajados		
					//Timestamp dateTo = preMov.getMovementDate();
					//calculamos 6 dias atras
					/*Calendar calCalendario = Calendar.getInstance();
			        calCalendario.setTimeInMillis(dateTo.getTime());
			        int cantDaysAgo = 6;*/
			        /*if(turno.compareTo("30")==0)
			        	cantDaysAgo = 6;
			        else
			        	cantDaysAgo = 5;
			        */					        
			        /*calCalendario.add(Calendar.DATE, - cantDaysAgo);
					Timestamp dateFrom = new Timestamp(calCalendario.getTimeInMillis());
					
					int cantDays = DB.getSQLValue(po.get_TrxName(), 
							" SELECT COUNT(pm.Pre_M_Movement_ID) FROM Pre_M_MovementLine pml " +
							" INNER JOIN Pre_M_Movement pm ON (pml.Pre_M_Movement_ID = pm.Pre_M_Movement_ID)" +
							" WHERE pml.IsActive = 'Y' AND pm.DocStatus IN ('CO') AND pml.C_Bpartner_ID = "+pMovLine.getC_BPartner_ID()+
							" AND pm.movementdate BETWEEN ? AND ? ", dateFrom,dateTo);
					if(cantDays >= cantDaysAgo)
					{
						throw new AdempiereException("Mas de "+cantDaysAgo+" dias Trabajados. Conductor: "+pMovLine.getC_BPartner().getName());
					}
					*/
					//validación  domingos trabajados
					/*int month = preMov.getMovementDate().getMonth()+1; 
					String monthTxt = Integer.toString(month);
					if(month < 10 && monthTxt.trim().length() < 2)
						monthTxt = "0"+monthTxt;
					
					int cantSunday = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(DISTINCT(pm.Pre_M_Movement_ID))" +
							" FROM Pre_M_MovementLine pml " +
							" INNER JOIN Pre_M_Movement pm ON (pml.Pre_M_Movement_ID = pm.Pre_M_Movement_ID)" +
							" WHERE pml.IsActive = 'Y' AND pm.DocStatus IN ('CO') AND to_char(movementdate, 'd') = '1'" +
							" AND pml.C_Bpartner_ID = "+pMovLine.getC_BPartner_ID()+" AND to_char(movementdate, 'mm') = '"+monthTxt+"'");
					
					if(cantSunday > 2)
						throw new AdempiereException("Mas de 2 domingos Trabajados. Conductor: "+pMovLine.getC_BPartner().getName());
					*/
				}
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
	private boolean updateHeader(MCash cash)
	{
		String sql = "SELECT COALESCE(SUM(cl.Amount),0) "
				+ "FROM C_CashLine cl "
				+ "WHERE cl.IsActive='Y'"
				+ " AND cl.C_Cash_ID=" + cash.getC_Cash_ID();
		BigDecimal StatementDifference = DB.getSQLValueBD(cash.get_TrxName(), sql);
		cash.setStatementDifference(StatementDifference);
		cash.setEndingBalance(cash.getBeginningBalance().add(StatementDifference));
		cash.saveEx();
		return true;
	}	//	updateHeader

	

}	
