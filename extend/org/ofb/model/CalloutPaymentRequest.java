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
import org.compiere.model.X_C_ProjectSchedule;
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
public class CalloutPaymentRequest extends CalloutEngine
{
		
	public String ProjectSchedule (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		int cc_ID = (Integer)value;
		
		BigDecimal amt = new BigDecimal("0.0");		
		X_C_ProjectSchedule ps = new X_C_ProjectSchedule(ctx, cc_ID, null);
		amt = (BigDecimal)ps.get_Value("Amt");
				
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
	
}	//

