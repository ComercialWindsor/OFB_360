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
import java.util.*;
import java.util.logging.Level;

import javax.print.Doc;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MProduct;
import org.compiere.model.MProject;
import org.compiere.model.X_DM_Document;
import org.compiere.model.X_PM_Tender;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import bsh.This;

/**
 *	Order Callouts.
 *	
 *  @author Italo Niñoles OFB ininoles
 *  @version $Id: CalloutCProject.java,v 1.0 2012/12/07  Exp $
 */
public class CalloutCProject extends CalloutEngine
{
	//copia concurso desde concurso 2 para evitar perder datos
	public String PMTender_Copy (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		int IDPMTender;
		int IDPMT2;
		String sqlU;
		String sql2;
				
		
		
		if (mTab.getValue("PM_Tender_ID") == null)
			IDPMTender = 0;
		else 
				IDPMTender = Integer.parseInt((mTab.getValue("PM_Tender_ID")).toString());
				X_PM_Tender tender = new X_PM_Tender(ctx, IDPMTender, null);
		if (value == null)
			IDPMT2 = 0;
		else 
			IDPMT2 = Integer.parseInt(value.toString());		
		
		if (IDPMT2 > 0)
		{
			sqlU = "update C_Project set PM_Tender_ID = " + value.toString()
					+ " WHERE C_Project_ID = " + mTab.getRecord_ID();
			int a = DB.executeUpdate(sqlU);
			mTab.setValue("PM_Tender_ID", value);
			
		}

		else if (IDPMT2 == 0)
			if (tender.getDocStatus().equals("CL"))
			{
				sql2 = "update C_Project set PM_Tender_ID2 = null" 
						+ " WHERE C_Project_ID = " + mTab.getRecord_ID();
				int c = DB.executeUpdate(sql2);
				return "";
			}
			else 
				mTab.setValue("PM_Tender_ID", 0);		

		if (value == null)
		{
			sql2 = "update C_Project set PM_Tender_ID2 = null" 
					+ " WHERE C_Project_ID = " + mTab.getRecord_ID();
			int b = DB.executeUpdate(sql2);
		}
		else 
		{
			sql2 = "update C_Project set PM_Tender_ID2 = " + value.toString()
					+ " WHERE C_Project_ID = " + mTab.getRecord_ID();
					int b = DB.executeUpdate(sql2);
			
		}		 
		return "";
	}
	//ininoles call out para actualizar codigo BIP
	public String CodigoBIP_Copy (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		String BIP = mTab.get_ValueAsString("POReference");
				
		if (value == null || value == "")
		{			
			return "";
		}
		else 
		{
			BIP = value + " - " + mTab.getRecord_ID();
			mTab.setValue("value", BIP);
		}	
		return "";
	}
	//ininoles call out para no crear la misma linea 2 veces.
	public String ValidacionItem (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (value == null || value == "")
		{			
			return "";
		}
		
		int project_ID = Integer.parseInt((mTab.getValue("C_Project_ID")).toString());
		String mysql ="SELECT POReference FROM C_Project WHERE C_Project_ID = ?";
		String BIP = DB.getSQLValueString(null, mysql, project_ID);
		int Product_ID = Integer.parseInt((mTab.getValue("M_Product_ID")).toString()); 
		MProduct product = new MProduct(ctx, Product_ID, null);
										
		String mysqlCant = "select count(*) from c_projectline cp "+
				"inner join c_project pl on (cp.c_project_ID = pl.c_project_ID) "+
				"where POReference like ? AND M_Product_ID = ?";
		
		int cant = DB.getSQLValue(null, mysqlCant, BIP, Product_ID);
			
		if (cant > 0) 
		{
			mTab.fireDataStatusEEvent ("", "Ya existe un item "+ product.getName()+" para el Proyecto con codigo BIP "+BIP , true);
			mTab.setValue("M_product_ID", null);
		}	
		return "";
	}
	
	public String UpdateDateFinish (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{					
		if (value == null || value == "")
		{			
			return "";
		}
		else 
		{	
			Date fi = (java.util.Date)mTab.getValue("DateFinish");
			int qty = 16;						
			Date ff = CalloutRHAdministrativeRequests.calculateEndDateFeriados(fi, qty);

			Timestamp cff = new Timestamp(ff.getTime());
			
			mTab.setValue("DateDoc", cff);
		}	
		return "";
	}
	public String UpdateSupervisionDate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{					
		if (value == null || value == "")
		{			
			return "";
		}
		else 
		{	
			Timestamp dateTrx = (Timestamp)value;
			Date factual = new Date();
			Timestamp actual = new Timestamp(factual.getTime());	
			
			if (dateTrx.compareTo(actual) > 0)
			{
				mTab.fireDataStatusEEvent("Error", "No Puede Ingresar una fecha posterior a la actual", false);
				mTab.setValue("dateTrx",actual);
			}
			mTab.setValue("Status", "RE");								
		}	
		return "";
	}	
}


