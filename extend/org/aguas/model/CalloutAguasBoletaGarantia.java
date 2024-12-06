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
package org.aguas.model;

import java.util.*;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MDocType;
import org.compiere.model.X_DM_PerformanceBond;
import org.compiere.util.Env;

/**
 *	Order Callouts.
 *	
 *  @author Fabian Aguilar OFB faaguilar
 *  @version $Id: CalloutDMDocument.java,v 1.34 2006/11/25 21:57:24  Exp $
 */
public class CalloutAguasBoletaGarantia extends CalloutEngine
{

	 
	public String updateCurrency (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		X_DM_PerformanceBond boleta = new X_DM_PerformanceBond(Env.getCtx(), (Integer) (mTab.getValue("DM_PerformanceBond_ID")), null);
		
		mTab.setValue("C_Currency_ID", boleta.getC_Currency_ID());
		
		return "";
	}	
	public String updateFieldsPB(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		X_DM_PerformanceBond boleta = new X_DM_PerformanceBond(Env.getCtx(), (Integer) (mTab.getValue("DM_PerformanceBond_ID")), null);
		
		mTab.setValue("C_Bpartner2_ID", boleta.getC_BPartner_ID());		
		mTab.setValue("Amt2", boleta.getAmt());
		mTab.setValue("C_Currency2_ID", boleta.getC_Currency_ID());
		mTab.setValue("C_Bank2_ID", boleta.getC_Bank_ID());
		mTab.setValue("DateTrxRef2", boleta.getDateTrx());
		mTab.setValue("DateAcctRef2", boleta.getDateEnd());
		mTab.setValue("correlative2", boleta.getcorrelative());
		mTab.setValue("Description2", boleta.getDescription());
		String infoRut = "";
		String infoName = "";
		infoRut = (String)boleta.get_Value("InfoRut");
		infoName = (String)boleta.get_Value("InfoName");
		if (infoRut != null)
			mTab.setValue("InfoRutRef", infoRut);
		if ( infoName != null)			
			mTab.setValue("InfoNameRef", infoRut);
		MDocType docT = new MDocType(Env.getCtx(), (Integer)mTab.getValue("C_DocType_ID"), null);		
		if (docT.getDocBaseType().equals("PBB"))
		{
			mTab.setValue("AD_Client_ID", boleta.getAD_Client_ID());
			mTab.setValue("AD_Org_ID", boleta.getAD_Org_ID());
			mTab.setValue("C_Currency_ID", boleta.getC_Currency_ID());			
		}				
		return "";
	}
	
}	//	

