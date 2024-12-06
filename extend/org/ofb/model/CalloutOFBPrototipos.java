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
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MProject;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.X_C_ProjectSchedule;
import org.compiere.model.X_DM_Document;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;

import test.functional.DBTest;

/**
 *	Order Callouts.
 *	
 *  @author Fabian Aguilar OFB faaguilar
 *  @version $Id: CalloutDMDocument.java,v 1.34 2006/11/25 21:57:24  Exp $
 */
public class CalloutOFBPrototipos extends CalloutEngine
{
	public String PayAmtProjectSchedule (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		int ps_ID = (Integer)value;
		X_C_ProjectSchedule sche = new X_C_ProjectSchedule(ctx, ps_ID, null);
		
		BigDecimal amt = new BigDecimal("0.0");
		BigDecimal rate = (BigDecimal)sche.get_Value("UsedRate");
		rate = Env.ONEHUNDRED.subtract(rate);
		
		
		amt = DB.getSQLValueBD(null, "select dueamt from c_projectschedule where c_projectschedule_id = ?" , ps_ID);			 
		if (amt != null)
		{
			BigDecimal OpenAmt = (amt.divide(Env.ONEHUNDRED)).multiply(rate);
			mTab.setValue("PayAmt", OpenAmt);
			mTab.setValue("Amt", amt);								
			mTab.setValue("OpenAmt", OpenAmt);
			mTab.setValue("Rate", rate);
		}	
		return "";
	}	//	charge	
	
	public String AmtRateProjectSchedule (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		int ps_ID = (Integer)mTab.getValue("C_ProjectSchedule_ID");
		X_C_ProjectSchedule sche = new X_C_ProjectSchedule(ctx, ps_ID, null);
		BigDecimal rateMax = (BigDecimal)sche.get_Value("UsedRate");
		rateMax = Env.ONEHUNDRED.subtract(rateMax);
				
		BigDecimal rate = (BigDecimal)value;
		BigDecimal amt = (BigDecimal)mTab.getValue("Amt");
		if (rate.compareTo(rateMax) > 0)
		{
			mTab.fireDataStatusEEvent("Error", "% A Pago no puede se mayor a Saldo", false);
			mTab.setValue("Rate", Env.ZERO);
			mTab.setValue("PayAmt", Env.ZERO);
			mTab.setValue("OpenAmt", Env.ZERO);
		}
		else
		{		
			BigDecimal newAmt = Env.ZERO;				
			if (rate != null && amt != null && rate != Env.ZERO)
			{
				newAmt = (amt.divide(Env.ONEHUNDRED)).multiply(rate);
				mTab.setValue("PayAmt", newAmt);
				mTab.setValue("OpenAmt", newAmt);
			}
		}			 
		return "";
	}
	public String EmulaBtnInventario (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		boolean flag = false;
		if(value.toString() == "true")		
			flag = true;
		else
			flag = false;
		
		String msg = "";		
		int id_Client = Env.getAD_Client_ID(ctx);
		
		if(flag)
		{
			int id_role = Env.getAD_Role_ID(ctx);
			
			int M_Inventory_ID = (Integer)mTab.getValue("M_Inventory_ID");
			
			MInventory inv = new MInventory(ctx, M_Inventory_ID, null);
			
			if(mTab.getValue("DocStatus").toString().compareToIgnoreCase("DR") ==0)
			{	
				mTab.setValue("DocStatus", "L1");
				msg = "Procesado";
			}
			else if(mTab.getValue("DocStatus").toString().compareToIgnoreCase("L1") ==0)
			{	
				if (id_role != 1000022 && id_role != 1000021 && id_role != 1000012)		
				{
					mTab.setValue("Procesar", false);
					return("Rol sin privilegios suficientes");
				}
				mTab.setValue("DocStatus", "L2");
				msg = "Procesado";
			}
			else if(mTab.getValue("DocStatus").toString().compareToIgnoreCase("L2") ==0)
			{
				if (id_role != 1000021 && id_role != 1000012)				
				{
					mTab.setValue("Procesar", false);
					return ("Rol sin privilegios suficientes");
				}
				mTab.setValue("DocStatus", "L3");
				msg = "Procesado";
			}
			else if(mTab.getValue("DocStatus").toString().compareToIgnoreCase("L3") ==0)
			{	
				if (id_role != 1000012)			
				{
					mTab.setValue("Procesar", false);
					return ("Rol sin privilegios suficientes");
				}
				int idReqRe = 0;
				try
				{
					idReqRe = (Integer)mTab.getValue("M_requisition_ID");
				}
				catch (Exception e)
				{
					log.severe("No se pudo setear campo M_Requisition_ID");
					idReqRe = 0;
				}			
				if (idReqRe > 0)
					msg = "Procesado. Solicitud ya relacionada ";
				else
				{
					MRequisition req = new MRequisition(ctx, 0, null);
					//c_doctype
					int idDocType = DB.getSQLValue(null, "SELECT MAX(C_DocType_ID) FROM C_DocType WHERE docbasetype = 'POR' "
							+ " AND IsActive = 'Y' AND AD_Client_ID = "+id_Client);
					if (idDocType > 0)
						req.setC_DocType_ID(idDocType);
					else
						req.setC_DocType_ID(1000018);
					//org
					req.setAD_Org_ID((Integer)mTab.getValue("AD_Org_ID"));
					//pricelist
					int idPriceList = DB.getSQLValue(null, "SELECT MAX(M_PriceList_ID) FROM M_PriceList WHERE IsActive = 'Y'"
							+ " AND issopricelist = 'N' AND isdefault = 'Y' AND AD_Client_ID = "+id_Client);
					if (idPriceList > 0)
						req.setM_PriceList_ID(idPriceList);
					else
						req.setC_DocType_ID(1000001);
					//user				
					int idUser = 0;
					try
					{
						idUser =(Integer)mTab.getValue("AD_User_ID"); 
					}
					catch (Exception e)
					{
						log.severe("No se pudo setear variable AD_User_ID");
						idUser = 0;
					}				
					if(idUser > 0)
						req.setAD_User_ID(idUser);
					else
						req.setAD_User_ID(100);
					req.setM_Warehouse_ID((Integer)mTab.getValue("M_Warehouse_ID"));
					req.save();
									
					//lines
					MInventoryLine[] invLines = inv.getLines(false);
					
					for (int i = 0; i < invLines.length; i++)
					{
						MInventoryLine iLine = invLines[i];					
						MRequisitionLine reqLine = new MRequisitionLine(req);
						reqLine.setAD_Org_ID(req.getAD_Org_ID());
						reqLine.setM_Product_ID(iLine.getM_Product_ID());
						BigDecimal qtyReal = (BigDecimal)iLine.get_Value("qtyReal");
						reqLine.setQty(qtyReal);
						reqLine.save();
						try
						{
							reqLine.set_CustomColumn("M_InventoryLine_ID", iLine.get_ID());
							reqLine.save();
						}
						catch (Exception e)
						{
							log.severe("No se pudo setear campo M_InventoryLine_ID");
						}
					}	
					mTab.setValue("M_requisition_ID", req.get_ID());
					//inv.set_CustomColumn("M_requisition_ID", req.get_ID());
					msg = "Procesado. Solicitud Creada";
				}
				mTab.setValue("DocStatus", "CO");
				mTab.setValue("Processed", true);
				//inv.setDocStatus("CO");
				//inv.setProcessed(true);
			}		
			//inv.save();
		}
		mTab.setValue("Procesar", false);
		return msg;
	}
		
}	//

