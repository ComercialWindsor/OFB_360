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
package org.ofb.process;


import java.math.BigDecimal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.exceptions.NoVendorForProductException;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.compiere.model.MBPartner;
import org.compiere.model.MCharge;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPO;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.POResultSet;
import org.compiere.model.Query;
import org.compiere.model.X_DM_Document;
import org.compiere.model.X_DM_DocumentLine;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Env;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.process.SequenceCheck;


/**
 * 	Create Resolution "otros" 
 *	
 *	
 *  @author Italo NIñoles
 *  @version CreateResolutionsOther.java,v 1 2013/03/280 19:27:01
 *  
 *  
 */
public class CreateResolutionsOther extends SvrProcess
{	
	/**	Doc Date To			*/
	private Timestamp	p_DateTrx_To;
	/**	Doc Date From		*/
	private Timestamp	p_DateTrx_From;
	/** ID Resolucion*/
	private int 		p_Document_ID = 0;
	
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		p_Document_ID = this.getRecord_ID();		
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;						
			else if (name.equals("DateTrx"))
			{
				p_DateTrx_From = (Timestamp)para[i].getParameter();
				p_DateTrx_To = (Timestamp)para[i].getParameter_To();
			}			
			else				
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}		
	}	//	prepare
	
	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		
		X_DM_Document dc = new X_DM_Document(getCtx(), p_Document_ID, get_TrxName());
		
		String sqldoc = null;
		String desc = null;
		String descL = null;
		
		Timestamp	p_DateTrx_FromM;
		Timestamp	p_DateTrx_ToM;
		
		Calendar cal = Calendar.getInstance();		
        cal.setTimeInMillis(p_DateTrx_From.getTime());
        cal.add (Calendar.DATE, -1);        
        p_DateTrx_FromM = new Timestamp(cal.getTimeInMillis());

        cal.setTimeInMillis(p_DateTrx_To.getTime());
        cal.add(Calendar.DATE, 1);        
        p_DateTrx_ToM = new Timestamp(cal.getTimeInMillis());
		
				
		PreparedStatement pstmt = null;
		
		if (dc.getDM_DocumentType().equalsIgnoreCase("RF"))//resolución feriados legales
		{
						
			sqldoc = "select rh_administrativerequests_id from rh_administrativerequests "+
					"where requesttype like 'SVC' and docstatus like 'CO' and signature3 = 'Y' "+
					"and isactive = 'Y' and datestartrequest > ? and datestartrequest < ? ";
			
			desc = "Resolución de Feriado Legal entre fechas "+p_DateTrx_From+" / "+p_DateTrx_To ;
			descL = "Carga Resolución de Feriado Legal";

		}
		else if (dc.getDM_DocumentType().equalsIgnoreCase("RP"))//resolución permisos administrativos
		{
			sqldoc = "select rh_administrativerequests_id from rh_administrativerequests "+
					"where requesttype like 'PAD' and docstatus like 'CO' and signature3 = 'Y' "+
					"and isactive = 'Y' and rh_administrativerequests_id in "+ 
					"(select rh_administrativerequests_id from rh_administrativerequestsline " +
					"where datestartrequest > ? and datestartrequest < ?)";
			
			desc = "Resolución Permisos Administrativos entre fechas "+p_DateTrx_From+" / "+p_DateTrx_To ;
			descL = "Carga Resolución Permisos Administrativos";			
		}			
		else
		{
			return "Tipo de Resolucion no valido";
		}
		
		dc.setDescription(desc);		
		dc.save();
		
		int cantC = 0;

		try
		{	
			pstmt = DB.prepareStatement(sqldoc, get_TrxName());
			pstmt.setTimestamp(1, p_DateTrx_FromM);
			pstmt.setTimestamp(2, p_DateTrx_ToM);
			
			ResultSet rs = pstmt.executeQuery();		
			
			while (rs.next()) //ininoles lineas por cada cometido del documento  
			{
				//IDline = IDline + 1;
				X_DM_DocumentLine dl = new X_DM_DocumentLine(getCtx(), 0, get_TrxName());		
				dl.setDM_Document_ID(dc.get_ID());
				dl.set_CustomColumn("RH_AdministrativeRequests_ID", rs.getInt(1));
				dl.setDescription(descL);				
				dl.setIsActive(true);
				dl.setAmt(new BigDecimal(0.0));
				//dl.setDM_DocumentLine_ID(IDline);
				dl.save();
				cantC = cantC +1;
			}		
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}	//	doit
		return "Se han agregado " + cantC + " Lineas de Cometidos";
	}
	
}	//	RequisitionPOCreate
