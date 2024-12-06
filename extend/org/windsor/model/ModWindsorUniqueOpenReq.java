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
package org.windsor.model;

import java.math.BigDecimal;

import org.compiere.model.MClient;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 *	Validator for company WINDSOR
 *
 *  @author Italo Niñoles
 */
public class ModWindsorUniqueOpenReq implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModWindsorUniqueOpenReq ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModWindsorUniqueOpenReq.class);
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
		engine.addModelChange(MRequisitionLine.Table_Name, this);
		//	Documents to be monitored
		//engine.addDocValidate(MRequisition.Table_Name, this);

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		

		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()== MRequisitionLine.Table_ID)  
		{
			MRequisitionLine reqLine = (MRequisitionLine) po;
			MRequisition req = new MRequisition(po.getCtx(), reqLine.getM_Requisition_ID(), po.get_TrxName());
			if(reqLine.getM_Product_ID() > 0 && reqLine.getM_Product().isStocked()
					&& reqLine.getM_Product().getProductType().compareTo("I") == 0)
			{
				//se valida solo si la solicitud no es prereserva
				if(req.getC_DocType_ID() != 1000569)
				{
					
					//se revisa si existe solicitud abierta
					int ID_req = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr.M_Requisition_ID) FROM M_RequisitionLine rl " +
							" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
							" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+req.get_ValueAsInt("C_BPartner_ID")+
							" AND mr.C_BPartner_Location_ID = "+req.get_ValueAsInt("C_BPartner_Location_ID")+
							" AND rl.M_Product_ID = "+reqLine.getM_Product_ID()+" AND qty > qtyUsed " +
							" AND rl.M_Requisition_ID <>"+req.get_ID() +
							" and mr.AD_User_ID="+req.getAD_User_ID() );
					//se revisa si existe solicitud de distribucion abierta
					int ID_reqD = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr.M_Requisition_ID) FROM M_RequisitionLine rl " +
							" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
							" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+req.get_ValueAsInt("C_BPartner_ID")+
							" AND mr.OverWriteRequisition = 'Y' "+
							" AND rl.M_Product_ID = "+reqLine.getM_Product_ID()+" AND qty > qtyUsed "+
							" AND rl.M_Requisition_ID <>"+req.get_ID()+
							" and mr.AD_User_ID="+req.getAD_User_ID() );
					//ininoles nueva funcion para presolicitud
					int ID_reqP = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr2.M_Requisition_ID) FROM M_RequisitionLine rl " +
							" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID)" +
							" INNER JOIN M_Requisition mr2 ON (mr2.M_Requisition_ID = mr.M_RequisitionRef_ID)" +
							" WHERE mr.DocStatus IN ('CO','CL') AND mr2.C_BPartner_ID = "+req.get_ValueAsInt("C_BPartner_ID")+
							" AND rl.M_Product_ID = "+reqLine.getM_Product_ID()+"" +
							" AND qty > qtyUsed AND rl.M_Requisition_ID <>"+req.get_ID()+
							" AND mr.C_DocType_ID IN (1000111, 1000570)"+
							" and mr.AD_User_ID="+req.getAD_User_ID() );
					
					if(ID_req > 0)
					{
						MRequisition reqAux = new MRequisition(po.getCtx(), ID_req, po.get_TrxName());
						BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
								" WHERE rl.M_Requisition_ID = "+ID_req+" AND rl.M_Product_ID = "+reqLine.getM_Product_ID());								
						return "ERROR: Debe usar solicitud abierta para este cliente. N°: "+reqAux.getDocumentNo()+" con cantidad "+qtySol;
					}else if (ID_reqD > 0)
					{
						MRequisition reqAux = new MRequisition(po.getCtx(), ID_reqD, po.get_TrxName());
						BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
								" WHERE rl.M_Requisition_ID = "+ID_req+" AND rl.M_Product_ID = "+reqLine.getM_Product_ID());
						return "ERROR: Debe usar solicitud de distribución abierta para este cliente. N°: "+reqAux.getDocumentNo()+" con cantidad "+qtySol;
					}else if (ID_reqP > 0)
					{
						MRequisition reqAux = new MRequisition(po.getCtx(), ID_reqP, po.get_TrxName());
						BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
								" WHERE rl.M_Requisition_ID = "+ID_req+" AND rl.M_Product_ID = "+reqLine.getM_Product_ID());
						return "ERROR: Debe usar solicitud E-commerce abierta para este cliente. N°: "+reqAux.getDocumentNo()+" con cantidad "+qtySol;
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

		/*if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()== MRequisition.Table_ID)  
		{
			MRequisition req = (MRequisition) po;
			if(req.getC_DocType().isSOTrx())
			{
				MRequisitionLine[] rLines = req.getLines();
				for (int i = 0; i < rLines.length; i++)
				{
					MRequisitionLine rLine = rLines[i];
					if(rLine.getM_Product_ID() > 0 && rLine.getM_Product().isStocked()
							&& rLine.getM_Product().getProductType().compareTo("I") == 0)
					{
						int cant = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(*) FROM M_RequisitionLine rl " +
								" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
								" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+req.get_ValueAsInt("C_BPartner_ID") +
								" AND rl.M_Product_ID = "+rLine.getM_Product_ID()+" AND qty > qtyUsed " +
								" AND M_RequisitionLine_ID <> "+rLine.get_ID());
						
						if(cant > 0)
						{
							MBPartner bPart = new MBPartner(po.getCtx(), req.get_ValueAsInt("C_BPartner_ID"), po.get_TrxName());
							return "ERROR: Ya Existe una solicitud para el producto "+rLine.getM_Product().getName()+
							" y el socio de negocio "+bPart.getName();
						}
					}
				}
			}
		}*/
		
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