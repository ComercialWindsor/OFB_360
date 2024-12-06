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
package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;
import org.eevolution.model.MPPProductBOM;
import org.eevolution.model.MPPProductBOMLine;

/**
 *	AssetMeasure Callouts.
 *	
 *  @author Fabian Aguilar OFB
 *  @version $Id: CalloutMPMeasure.java,v 1.34 2006/11/25 21:57:24  Exp $
 */
public class CalloutMPMeasure extends CalloutEngine
{


	 
	public String checkAmt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		
		Integer AssetMeter_ID=(Integer)mTab.getValue("MP_AssetMeter_ID");
		BigDecimal maxDay=DB.getSQLValueBD(null, "select m.maxday from MP_Meter m" +
				" inner join MP_ASSETMETER a on (a.MP_Meter_ID=m.MP_Meter_ID)" +
				" where a.MP_ASSETMETER_ID="+AssetMeter_ID);
		Timestamp Datetrx=(Timestamp)mTab.getValue("DateTrx");
		BigDecimal Amt=(BigDecimal)value;
		BigDecimal lastAmt=DB.getSQLValueBD(null, "select amt from MP_AssetMeter_Log " +
				"where MP_AssetMeter_ID=? and DateTrx<? order by datetrx desc", AssetMeter_ID,Datetrx);
		
		Timestamp LastDate= DB.getSQLValueTSEx(null, "select datetrx from MP_AssetMeter_Log " +
				"where MP_AssetMeter_ID=? and DateTrx<? order by datetrx desc", AssetMeter_ID,Datetrx);
		if(lastAmt!=null){
			if(Amt.compareTo(lastAmt)<0)
				mTab.fireDataStatusEEvent ("La entrada es inferior a una entrada anterior", "", false);
			
			if(LastDate!=null){
				long diferenciaMils = Datetrx.getTime() - LastDate.getTime();
				long segundos = diferenciaMils / 1000;
				long horas = segundos / 3600;
				int dias=(int) (horas/24);
				BigDecimal diff=Amt.subtract(lastAmt);
				if(diff.compareTo(new BigDecimal(maxDay.intValue()*dias ))>0)
					mTab.fireDataStatusEEvent ("La entrada supera el maximo por dias", "", false);
			}
		}

		return "";
	}	//	charge

	public String getProductCost (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		int M_Product_ID=((Integer)value).intValue();
		
		BigDecimal cost = OFBProductCost.getProductCost(M_Product_ID,  (Integer)mTab.getValue("AD_Org_ID"), Env.ONE, Trx.createTrxName(), ctx);
		
		mTab.setValue("CostAmt", cost);
		return "";
	}
	
	public String getBOMCost (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		int M_BOM_ID=((Integer)value).intValue();
		MPPProductBOM bom= new MPPProductBOM(ctx, M_BOM_ID,null);
		MPPProductBOMLine[] lista =bom.getLines();
		String Trxname=Trx.createTrxName();
		BigDecimal cost= Env.ZERO;
		for(int i=0;i<lista.length;i++){
			
			cost = cost.add( OFBProductCost.getProductCost(lista[i].getM_Product_ID(),  (Integer)mTab.getValue("AD_Org_ID"), lista[i].getQtyBOM(), Trxname, ctx) );
		}
		mTab.setValue("CostAmt", cost);
		return "";
	}
	
	
	public String getResourceCost (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		int Resource_ID=((Integer)value).intValue();
		MResource resource=new MResource(ctx, Resource_ID, null);
		mTab.setValue("CostNormal", (BigDecimal)resource.get_Value("CostNormal"));
		mTab.setValue("CostExtra", (BigDecimal)resource.get_Value("CostExtra"));
		return "";
	}
	
	public String calcAmt(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if (isCalloutActive() || value == null)
			return "";
		
		
		BigDecimal CN = mTab.getValue("CostNormal")==null?Env.ZERO:(BigDecimal) mTab.getValue("CostNormal");
		BigDecimal CE = mTab.getValue("CostExtra")==null?Env.ZERO:(BigDecimal) mTab.getValue("CostExtra");
		
		if (mField.getColumnName().equals("ResourceQty") || mField.getColumnName().equals("ResourceQtyExtra") ){
			
			CN= (mTab.getValue("ResourceQty")==null?Env.ZERO:(BigDecimal)mTab.getValue("ResourceQty")).multiply(CN);
			CE = (mTab.getValue("ResourceQtyExtra")==null?Env.ZERO:(BigDecimal)mTab.getValue("ResourceQtyExtra")).multiply(CE);
		}
		
		mTab.setValue("CostAmt", CN.add(CE));
		return "";
	}
}	//	CalloutOrder

