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
 * 	Create Resolution "Cometidos" 
 *	
 *	
 *  @author Italo NIñoles
 *  @version CreateResolutionsCometidos.java,v 1 2013/03/280 19:27:01
 *  
 *  
 */
public class CreateResolutionsCometidos extends SvrProcess
{
	/** Tipo resolucion					*/
	private String		p_Type = null;	
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
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("Type"))
				p_Type = (String)para[i].getParameter();			
			else if (name.equals("DateTrx"))
			{
				p_DateTrx_From = (Timestamp)para[i].getParameter();
				p_DateTrx_To = (Timestamp)para[i].getParameter_To();
			}	
			else if (name.equals("DM_Document_ID"))
				p_Document_ID = para[i].getParameterAsInt();
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
		
		if (p_Type.equalsIgnoreCase("HO"))
		{
			
			
			sqldoc = "select rh_administrativerequests_id from rh_administrativerequests arD "+
					"where requesttype like 'CMT' and docstatus in ('CO','CL') and rh_administrativerequests_id not in "+
					"(select dl.rh_administrativerequests_id from DM_DocumentLine dl inner join DM_Document dc on (dl.DM_Document_ID = dc.DM_Document_ID) "+
					"where dc.docstatus not in ('VO') and dl.rh_administrativerequests_id is not null "+
					"and dc.dm_documenttype = 'AC') and datestartrequest > ? and datestartrequest < ? and "+
					"(SELECT CASE WHEN ar.FACTOR > 0 then ar.FACTOR ELSE (((SELECT MAX(CommitmentAmt)*ar.QtyCP "+
					"FROM C_Bpartner cbp WHERE cbp.C_Bpartner_ID = ar.C_Bpartner_ID)+(SELECT MAX(CommitmentAmtSP) * ar.QtySP "+
					"FROM C_Bpartner cbp WHERE cbp.C_Bpartner_ID = ar.C_Bpartner_ID)) * "+
					"(select case when (COALESCE((SUM(  case when payable = 'Y' then 1 else 0 end)),0)) > 0 then 1 else 0 end "+
					"from rh_administrativerequestsline arl inner join c_city cc on (arl.c_city_id = cc.c_city_id) "+
					"where arl.RH_AdministrativeRequests_ID = arD.RH_AdministrativeRequests_ID) ) end "+
					"FROM RH_AdministrativeRequests ar where ar.RH_AdministrativeRequests_ID = arD.RH_AdministrativeRequests_ID) > 0 "+
					"and (select typecontract from DM_Document dm where dm.dm_documenttype = 'CE' and dm.docstatus in ('CO') "+
					"and dm.isactive = 'Y' and dm.c_bpartner_id = arD.C_BPartner_ID and dm.datetrx = (select max(dm2.datetrx) from DM_Document dm2 "+
					"where dm2.c_bpartner_id = dm.c_bpartner_id and dm_documenttype = 'CE' and docstatus in ('CO') "+
					"and isactive = 'Y')) in ('HO','HP')";
			
			desc = "Resolucion Cometido Honorarios (Institucionales/De Programa) entre fechas "+p_DateTrx_From+" / "+p_DateTrx_To ;
			descL = "Carga Resolucion Cometido";
		}
		else if (p_Type.equalsIgnoreCase("OT"))
		{
			sqldoc = "select rh_administrativerequests_id from rh_administrativerequests arD "+
					"where requesttype like 'CMT' and docstatus in ('CO','CL') and rh_administrativerequests_id not in "+
					"(select dl.rh_administrativerequests_id from DM_DocumentLine dl inner join DM_Document dc on (dl.DM_Document_ID = dc.DM_Document_ID) "+
					"where dc.docstatus not in ('VO') and dl.rh_administrativerequests_id is not null "+
					"and dc.dm_documenttype = 'AC') and datestartrequest > ? and datestartrequest < ? and "+
					"(SELECT CASE WHEN ar.FACTOR > 0 then ar.FACTOR ELSE (((SELECT MAX(CommitmentAmt)*ar.QtyCP "+
					"FROM C_Bpartner cbp WHERE cbp.C_Bpartner_ID = ar.C_Bpartner_ID)+(SELECT MAX(CommitmentAmtSP) * ar.QtySP "+
					"FROM C_Bpartner cbp WHERE cbp.C_Bpartner_ID = ar.C_Bpartner_ID)) * "+
					"(select case when (COALESCE((SUM(  case when payable = 'Y' then 1 else 0 end)),0)) > 0 then 1 else 0 end "+
					"from rh_administrativerequestsline arl inner join c_city cc on (arl.c_city_id = cc.c_city_id) "+
					"where arl.RH_AdministrativeRequests_ID = arD.RH_AdministrativeRequests_ID) ) end "+
					"FROM RH_AdministrativeRequests ar where ar.RH_AdministrativeRequests_ID = arD.RH_AdministrativeRequests_ID) > 0 "+
					"and (select typecontract from DM_Document dm where dm.dm_documenttype = 'CE' and dm.docstatus in ('CO') "+
					"and dm.isactive = 'Y' and dm.c_bpartner_id = arD.C_BPartner_ID and dm.datetrx = (select max(dm2.datetrx) from DM_Document dm2 "+
					"where dm2.c_bpartner_id = dm.c_bpartner_id and dm_documenttype = 'CE' and docstatus in ('CO') "+
					"and isactive = 'Y')) not in ('HO','HP')";
			
			desc = "Resolucion Cometido Otros (Contrata/Planta/Suplencia)entre fechas "+p_DateTrx_From+" / "+p_DateTrx_To ;
			descL = "Carga Resolucion Cometido";
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
