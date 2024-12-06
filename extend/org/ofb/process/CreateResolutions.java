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
 * 	Create PO from Requisition 
 *	
 *	
 *  @author Jorg Janke
 *  @version $Id: RequisitionPOCreate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 *  
 *  @author Teo Sarca, www.arhipac.ro
 *  		<li>BF [ 2609760 ] RequisitionPOCreate not using DateRequired
 *  		<li>BF [ 2605888 ] CreatePOfromRequisition creates more PO than needed
 *  		<li>BF [ 2811718 ] Create PO from Requsition without any parameter teminate in NPE
 *  			http://sourceforge.net/tracker/?func=detail&atid=879332&aid=2811718&group_id=176962
 *  		<li>FR [ 2844074  ] Requisition PO Create - more selection fields
 *  			https://sourceforge.net/tracker/?func=detail&aid=2844074&group_id=176962&atid=879335
 */
public class CreateResolutions extends SvrProcess
{
	/** Tipo resolucion					*/
	private String			p_Type = null;	
	/**	Doc Date To			*/
	private Timestamp	p_DateTrx_To;
	/**	Doc Date From		*/
	private Timestamp	p_DateTrx_From;
	
	
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
		
		X_DM_Document dc = new X_DM_Document(getCtx(), 0, get_TrxName());
		
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
		
		if (p_Type.equalsIgnoreCase("RTC"))
		{
			sqldoc = "select rh_administrativerequests_id from rh_administrativerequests "+
					"where requesttype like 'TCP' and docstatus like 'CO' and signature3 = 'Y' "+
					"and rh_administrativerequests_id in (select rh_administrativerequests_id "+
					"from rh_administrativerequestsline where datestartrequest > ? "+
					"and datestartrequest < ?)";
			
			desc = "Resolucion Tiempo compensado entre fechas "+p_DateTrx_From+" / "+p_DateTrx_To ;
			descL = "Carga Resolucion Tiempo Compensado";
		}
		else if (p_Type.equalsIgnoreCase("HTC"))
		{
			sqldoc = "select rh_administrativerequests_id from rh_administrativerequests "+
					"where requesttype like 'SHE' and typeshe like 'TC' and docstatus like 'CO' "+
					"and signature3 = 'Y' and (datestartrequest > ? "+
					"and datestartrequest < ?)";
			
			desc = "Resolucion hora extra Tiempo compensado entre fechas "+p_DateTrx_From+" / "+p_DateTrx_To ;
			descL = "Carga Resolucion Hora extra Tiempo Compensado";
		}
		else if (p_Type.equalsIgnoreCase("HAP"))
		{
			sqldoc = "select rh_administrativerequests_id from rh_administrativerequests "+
					"where requesttype like 'SHE' and typeshe like 'PG' and docstatus like 'CO' "+
					"and signature3 = 'Y' and (datestartrequest > ? "+
					"and datestartrequest < ?)";
			
			desc = "Resolucion hora extra A Pago entre fechas "+p_DateTrx_From+" / "+p_DateTrx_To ;
			descL = "Carga Resolucion Hora extra A Pago";
		}
		else 
		{
			if(dc.getDM_DocumentType().equalsIgnoreCase("RF"))//resolución feriados legales
			{
				sqldoc = "select rh_administrativerequests_id from rh_administrativerequests "+
						"where requesttype like 'SVC' and docstatus like 'CO' and signature3 = 'Y' "+
						"and isactive = 'Y' and datestartrequest > ? and datestartrequest < ? ";
				
				desc = "Resolucion hora extra A Pago entre fechas "+p_DateTrx_From+" / "+p_DateTrx_To ;
				descL = "Carga Resolucion Hora extra A Pago";
				
			}
			
			else if(dc.getDM_DocumentType().equalsIgnoreCase("RF"))//resolución permisos administrativos 
			{
				sqldoc = "select rh_administrativerequests_id from rh_administrativerequests "+
						"where requesttype like 'PAD' and docstatus like 'CO' and signature3 = 'Y' "+
						"and isactive = 'Y' and rh_administrativerequests_id in "+ 
						"(select rh_administrativerequests_id from rh_administrativerequestsline " +
						"where datestartrequest > ? and datestartrequest < ?)";
				
				desc = "Resolucion hora extra A Pago entre fechas "+p_DateTrx_From+" / "+p_DateTrx_To ;
				descL = "Carga Resolucion Hora extra A Pago";				
			}			
		}
		dc.setDescription(desc);
		dc.setDateTrx(p_DateTrx_From);
		dc.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
		dc.set_CustomColumn("AD_Client_ID", Env.getAD_Client_ID(getCtx()));
		dc.setDM_DocumentType("RD");		
		dc.setDocStatus("DR");
		dc.set_CustomColumn("C_DocType_ID", 1000063);		
		
		dc.save();

		try
		{	
			pstmt = DB.prepareStatement(sqldoc, get_TrxName());
			pstmt.setTimestamp(1, p_DateTrx_FromM);
			pstmt.setTimestamp(2, p_DateTrx_ToM);
			
			ResultSet rs = pstmt.executeQuery();		
			
			int IDline = 0;
			int IDDoc = 0;
			
			
			while (rs.next()) //ininoles lineas por cada cometido del documento  
			{
				IDline = IDline + 1;
				X_DM_DocumentLine dl = new X_DM_DocumentLine(getCtx(), 0, get_TrxName());		
				dl.setDM_Document_ID(dc.get_ID());
				dl.set_CustomColumn("RH_AdministrativeRequests_ID", rs.getInt(1));
				dl.setDescription(descL);
				dl.setAmt(new BigDecimal(0.0));
				dl.setIsActive(true);
				//dl.setDM_DocumentLine_ID(IDline);
				dl.save();		
			}
			
			//actualizar sequencias
			
			/*String sqlIDLine = "select max (dm_documentline_ID) from dm_documentline";
			IDline = DB.getSQLValue(get_TrxName(), sqlIDLine);
			IDline = IDline + 1;
			
			String sqldlUpdateL = "update ad_sequence set currentnext = ? where name like 'DM_DocumentLine' ";
			
			String sqlIDDoc = "select max (dm_document_ID) from dm_document";
			IDDoc = DB.getSQLValue(get_TrxName(), sqlIDDoc);
			IDDoc = IDDoc + 1;
			
			String sqldlUpdateD = "update ad_sequence set currentnext = ? where name like 'DM_Document' ";
			
			DB.executeUpdate(sqldlUpdateL, IDline, get_TrxName());
			DB.executeUpdate(sqldlUpdateD, IDDoc, get_TrxName());
			*/			
					
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}	//	doit
		return "Resolucion Generada N°: "+dc.getDocumentNo();
	}
	
}	//	RequisitionPOCreate
