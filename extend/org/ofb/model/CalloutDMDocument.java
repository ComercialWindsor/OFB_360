/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.ofb.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MInvoice;
import org.compiere.model.MProject;
import org.compiere.model.X_DM_Document;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import test.functional.DBTest;

/**
 *	Order Callouts.
 *	
 *  @author Fabian Aguilar OFB faaguilar
 *  @version $Id: CalloutDMDocument.java,v 1.34 2006/11/25 21:57:24  Exp $
 */
public class CalloutDMDocument extends CalloutEngine
{

	 
	public String ProjectAmt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		int Pj_ID = (Integer)mTab.getValue("C_Project_ID");
		String type = (String)mTab.getValue("DM_DocumentType");
		
		if(type.equals("PM")  || type.equals("02"))
		{
			MProject pj = new  MProject(ctx,Pj_ID,null );
			mTab.setValue("Amt", pj.getCommittedAmt());	 	 		
		}
		
		return "";
	}	//	charge

	
	public String AsignacionAmt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		int As_ID = (Integer)mTab.getValue("DM_DocumentRef_ID");
		
		X_DM_Document As = new  X_DM_Document(ctx,As_ID,null );
		mTab.setValue("Amt", As.getAmt());	 	 		
		
		
		return "";
	}	//	charge

	
	public String ProjectSchedule (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		int cc_ID = (Integer)value;
		
		
		//BigDecimal amt = DB.getSQLValueBD(null, "select dueamt from c_projectschedule where c_projectschedule_id = ?" , cc_ID);
		//ininoles -monto solicitud de pago solo es la sumatoria de las rendiciones de la cuota.
		BigDecimal amt = new BigDecimal("0.0");
		amt =  DB.getSQLValueBD(null, "select SUM(Amt) from PM_ProjectPay where c_projectschedule_id = ? ", cc_ID); 
		
		/*if(minus!=null && minus.signum()!=0)
			amt = amt.add(minus);*/
				
		mTab.setValue("PayAmt", amt);	 	
		
		int document_id = DB.getSQLValue(null, "select DM_Document_ID from C_ProjectSchedule where c_projectschedule_id = ?" , cc_ID);
		if(document_id>0)
		{
			int bp_id = DB.getSQLValue(null, "select c_bpartnerp_id from DM_Document where DM_Document_id = ?" , document_id);
			if(bp_id>0)
				mTab.setValue("C_BPartner_ID",bp_id);
			
			//ininoles nuevos campos "acumulados"
			X_DM_Document com = new X_DM_Document(ctx, document_id,null);
			
			mTab.setValue("AcumAnticipo", (BigDecimal)com.get_Value("AcumAnticipo"));
			mTab.setValue("AcumRetencion", (BigDecimal)com.get_Value("AcumRetencion"));
			mTab.setValue("AcumReajusteO", (BigDecimal)com.get_Value("AcumReajusteO"));
			mTab.setValue("AcumReajusteA", (BigDecimal)com.get_Value("AcumReajusteA"));
			mTab.setValue("AcumDevR", (BigDecimal)com.get_Value("AcumDevR"));
			mTab.setValue("AcumDevA", (BigDecimal)com.get_Value("AcumDevA"));
			mTab.setValue("AcumMultas", (BigDecimal)com.get_Value("AcumMultas"));
			mTab.setValue("AcumOtrasR", (BigDecimal)com.get_Value("AcumOtrasR"));
			mTab.setValue("AcumDevOtrR", (BigDecimal)com.get_Value("AcumDevOtrR"));			
			mTab.setValue("AmtDate", (BigDecimal)com.get_Value("AmtDate"));
			
			//setear compromiso en solicitud de pago 
			mTab.setValue("DM_DocumentC", document_id);
			
			//end ininoles
		}
		
		
		return "";
	}	//	charge
	
	
	/**
	 * para PM_ProjectPay.Amt*/
	public String PayTypeAmt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		BigDecimal amt = (BigDecimal)value;
		String payType = (String) mTab.getValue("Pay_Type");
		
		if(payType.startsWith("D") || payType.startsWith("R"))
			if(amt.signum()>0)
			{
				mTab.setValue("Amt", Env.ZERO);
				mTab.fireDataStatusEEvent("Error", "el monto debe ser negativo", false);	
			}
		
		
		return "";
	}	//	charge
	
	/**
	 * para PM_ProjectPay.C_invoice_ID*/
	public String ProjectInvoice (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		Integer C_Invoice_ID = (Integer)value;
		String sql = "SELECT  invoiceOpen(C_Invoice_ID, null) " 
				+ "FROM C_Invoice WHERE C_Invoice_ID=?"; // #4
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				pstmt.setInt (1, C_Invoice_ID.intValue ());
				rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					mTab.setValue("Amt", rs.getBigDecimal(1));
					
					String sqldbt = "select docbasetype from C_DocType cdt "+
									"inner join C_Invoice ci on (ci.C_DocType_ID = cdt.C_DocType_ID) "+
									"where ci.C_Invoice_ID = ?";
					
					String dtB = DB.getSQLValueString(null, sqldbt, C_Invoice_ID);
					
					if (dtB.equals("APB"))
					{
						String sqlbh = "select coalesce(sum(abs(taxamt)),0) from C_InvoiceTax "+
										"where C_Invoice_ID=? and C_Tax_ID=1000002";
						
						BigDecimal montoBH = DB.getSQLValueBD(null, sqlbh, C_Invoice_ID);
						mTab.setValue("ReferenceAmount", montoBH);						
					}				
					else 
					{
						mTab.setValue("ReferenceAmount", new BigDecimal("0.0"));
					}
				}
			}
			catch (SQLException e)
			{
				log.log (Level.SEVERE, sql, e);
				return e.getLocalizedMessage ();
			}
			finally
			{
				DB.close (rs, pstmt);
			}

		return "";
	}	//
	//ininoles actualizacion monto rendicion DM_Document
	public String ProjectDM_Document (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		Integer DM_Document_ID = (Integer)value;
		X_DM_Document doc = new X_DM_Document(ctx, DM_Document_ID, null);
		
		mTab.setValue("Amt", doc.getAmt());
		
		return "";
	}	//
	
	public String Signature1 (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		boolean Signature;
		
		 if (value instanceof Boolean) 
			 Signature = ((Boolean)value).booleanValue(); 
		 else
			 Signature = "Y".equals(value);
		 
		 if(Signature)
		 {
			 int UserID;
			 if (mTab.getValue("UpdatedBySignature1") == null)			 
				 UserID = 0;
			 else
			 {
				 String user = mTab.getValue("UpdatedBySignature1").toString();
				 UserID = Integer.parseInt(user);
			 }
			 if (UserID > 0)
			 {
				 mTab.setValue("UpdatedBySignature1Ref_ID", Env.getAD_User_ID(Env.getCtx()));				 
			 }
			 else 
			 {
				 mTab.setValue("UpdatedBySignature1", Env.getAD_User_ID(Env.getCtx())); 
			 }			 
			 mTab.setValue("UpdatedSignature1", new Timestamp(System.currentTimeMillis()));			 
		 }
		
		return "";
	}	
	
	public String Signature2 (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		boolean Signature;
		
		 if (value instanceof Boolean) 
			 Signature = ((Boolean)value).booleanValue(); 
		 else
			 Signature = "Y".equals(value);
		
		 if(Signature)
		 {
			 mTab.setValue("UpdatedSignature2", new Timestamp(System.currentTimeMillis()));
			 mTab.setValue("UpdatedBySignature2", Env.getAD_User_ID(Env.getCtx()));
		 }
		return "";
	}	
	
	public String Signature3 (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		boolean Signature;
		
		 if (value instanceof Boolean) 
			 Signature = ((Boolean)value).booleanValue(); 
		 else
			 Signature = "Y".equals(value);
		
		 if(Signature)
		 {
			 mTab.setValue("UpdatedSignature3", new Timestamp(System.currentTimeMillis()));
			 mTab.setValue("UpdatedBySignature3", Env.getAD_User_ID(Env.getCtx()));
		 }
		
		return "";
	}	
	
	public String Signature4 (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		boolean Signature;
		
		 if (value instanceof Boolean) 
			 Signature = ((Boolean)value).booleanValue(); 
		 else
			 Signature = "Y".equals(value);
		
		 if(Signature)
		 {
			 mTab.setValue("UpdatedSignature4", new Timestamp(System.currentTimeMillis()));
			 mTab.setValue("UpdatedBySignature4", Env.getAD_User_ID(Env.getCtx()));
		 }
		
		return "";
	}
	
	public String EndDateCompromiso02 (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws ParseException
	{
		if(value==null)
			return "";
		
		if (mTab.get_ValueAsString("DM_DocumentType").equals("02"))			
		{			
			Calendar startCal = Calendar.getInstance();
			
			Date fi = (Date)mTab.getValue("DateStart");
			BigDecimal qtyDays= (BigDecimal)mTab.getValue("NumberDays");
			int qty = Integer.valueOf(qtyDays.intValue());
			
			startCal.setTime(fi);		
			startCal.add(Calendar.DAY_OF_MONTH, qty);
			//se modifica clase para que tome los fines de semana inclusive
			Date ff =  startCal.getTime();			
			Timestamp cff = new Timestamp(ff.getTime());			
			mTab.setValue("DateEnd", cff);			
		}
		
		return "";
	}	
	
}	//

