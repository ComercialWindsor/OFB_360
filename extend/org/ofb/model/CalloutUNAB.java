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
import java.util.*;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.X_T_PRESUPUESTO;
import org.compiere.util.Env;

/**
 *	Order Callouts.
 *	
 *  @author Fabian Aguilar OFB faaguilar
 *  @version $Id: CalloutDMDocument.java,v 1.34 2006/11/25 21:57:24  Exp $
 */
public class CalloutUNAB extends CalloutEngine
{

	 
	public String updateAmt (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		X_T_PRESUPUESTO pre = new X_T_PRESUPUESTO(Env.getCtx(), (Integer) (mTab.getValue("T_PRESUPUESTO_ID")), null);
		
		if (mField.getColumnName().equals("Amt"))
		{			
			BigDecimal monto = new BigDecimal("0.0");
			monto = monto.add((BigDecimal)value);
			BigDecimal porcentaje = new BigDecimal("0.0");			
			porcentaje = porcentaje.add(monto.multiply(Env.ONEHUNDRED));
			porcentaje = porcentaje.divide(pre.getLineNetAmt(), BigDecimal.ROUND_HALF_UP);			
			mTab.setValue("Percentage", porcentaje);
		}
		
		if (mField.getColumnName().equals("Percentage"))
		{
			BigDecimal porcentaje = new BigDecimal("0.0");
			porcentaje = porcentaje.add((BigDecimal)value);
			BigDecimal amt = new BigDecimal("0.0");			
			amt = amt.add(porcentaje.multiply(pre.getLineNetAmt()));
			amt = amt.divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP);			
			mTab.setValue("Amt", amt);
		}
			
		return "";
	}
	
	public String updateAmtPayment (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		MPayment pay = new MPayment(Env.getCtx(), (Integer) mTab.getValue("C_Payment_ID"), null);
		
		if (mField.getColumnName().equals("Amt"))		
		{	
			BigDecimal monto = new BigDecimal("0.0");
			monto = monto.add((BigDecimal)value);
			BigDecimal porcentaje = new BigDecimal("0.0");			
			porcentaje = porcentaje.add(monto.multiply(Env.ONEHUNDRED));
			porcentaje = porcentaje.divide(pay.getPayAmt(), BigDecimal.ROUND_HALF_UP);			
			mTab.setValue("Percentage", porcentaje);
		}
		
		if (mField.getColumnName().equals("Percentage"))
		{	
			BigDecimal porcentaje = new BigDecimal("0.0");
			porcentaje = porcentaje.add((BigDecimal)value);
			BigDecimal amt = new BigDecimal("0.0");			
			amt = amt.add(porcentaje.multiply(pay.getPayAmt()));
			amt = amt.divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP);			
			mTab.setValue("Amt", amt);
		}
			
		return "";
	}
	public String updateAmtOrder (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		MOrderLine oLine = new MOrderLine(Env.getCtx(), (Integer) mTab.getValue("C_OrderLine_ID"), null);
		
		if (mField.getColumnName().equals("Amt"))		
		{	
			BigDecimal monto = new BigDecimal("0.0");
			monto = monto.add((BigDecimal)value);
			BigDecimal porcentaje = new BigDecimal("0.0");			
			porcentaje = porcentaje.add(monto.multiply(Env.ONEHUNDRED));
			porcentaje = porcentaje.divide(oLine.getLineNetAmt(), BigDecimal.ROUND_HALF_UP);			
			mTab.setValue("Percentage", porcentaje);
		}
		
		if (mField.getColumnName().equals("Percentage"))
		{	
			BigDecimal porcentaje = new BigDecimal("0.0");
			porcentaje = porcentaje.add((BigDecimal)value);
			BigDecimal amt = new BigDecimal("0.0");			
			amt = amt.add(porcentaje.multiply(oLine.getLineNetAmt()));
			amt = amt.divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP);			
			mTab.setValue("Amt", amt);
		}
			
		return "";
	}
	
	
}	//	

