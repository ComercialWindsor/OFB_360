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

import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_HR_Prebitacora;
import org.compiere.model.X_Pre_M_MovementLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;


/**
 *	Validator for PDV Colegios
 *
 *  @author Italo Niñoles
 */
public class ModTSMUpdatePreMovLine implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTSMUpdatePreMovLine ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTSMUpdatePreMovLine.class);
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
		engine.addModelChange(X_Pre_M_MovementLine.Table_Name, this);		
		
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
			X_Pre_M_MovementLine pmLine = (X_Pre_M_MovementLine)po;
			//incidencia socio de negocio
			if(pmLine.getC_BPartner_ID() > 0)
			{
				int ID_preBitacora = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(HR_Prebitacora_ID) FROM HR_Prebitacora  " +
						" WHERE C_BPartner_ID = "+pmLine.getC_BPartner_ID()+" AND DateTrx = ?",pmLine.getPre_M_Movement().getMovementDate());
				if(ID_preBitacora > 0)
				{
					X_HR_Prebitacora preBitacora = new X_HR_Prebitacora(po.getCtx(), ID_preBitacora, po.get_TrxName());
					pmLine.set_CustomColumn("HR_PrebitacoraRef_ID", ID_preBitacora);
					pmLine.set_CustomColumn("HR_Concept_TSMBP_ID", preBitacora.getHR_Concept_TSM_ID());
					if(preBitacora.getHR_Concept_TSM().getAcronym() != null 
							&& preBitacora.getHR_Concept_TSM().getDescription().trim().length() > 0
							&& type == TYPE_BEFORE_NEW)
						pmLine.set_CustomColumn("WorkshiftBP",preBitacora.getHR_Concept_TSM().getAcronym().trim());
				}
				else
				{
					pmLine.set_CustomColumn("HR_PrebitacoraRef_ID", null);					
					pmLine.set_CustomColumn("HR_Concept_TSMBP_ID",null);
					if(type == TYPE_BEFORE_NEW)
						pmLine.set_CustomColumn("WorkshiftBP",null);
				}
			}
			//incidencia tracto
			if(pmLine.getA_Asset_ID() > 0)
			{
				int ID_preBitacora = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(HR_Prebitacora_ID) FROM HR_Prebitacora  " +
						" WHERE A_Asset_ID = "+pmLine.getA_Asset_ID()+" AND DateTrx = ?",pmLine.getPre_M_Movement().getMovementDate());
				if(ID_preBitacora > 0)
				{
					X_HR_Prebitacora preBitacora = new X_HR_Prebitacora(po.getCtx(), ID_preBitacora, po.get_TrxName());
					pmLine.set_CustomColumn("HR_Prebitacora_ID", ID_preBitacora);
					pmLine.set_CustomColumn("HR_Concept_TSM_ID", preBitacora.getHR_Concept_TSM_ID());
					if(preBitacora.getHR_Concept_TSM().getAcronym() != null 
							&& preBitacora.getHR_Concept_TSM().getDescription().trim().length() > 0
							&& type == TYPE_BEFORE_NEW)
						pmLine.set_CustomColumn("Workshift",preBitacora.getHR_Concept_TSM().getAcronym().trim());
				}
				else
				{
					pmLine.set_CustomColumn("HR_Prebitacora_ID", null);
					pmLine.set_CustomColumn("HR_Concept_TSM_ID",null);
					if(type == TYPE_BEFORE_NEW)
						pmLine.set_CustomColumn("Workshift",null);
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