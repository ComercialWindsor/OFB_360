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

import java.sql.Timestamp;
import java.util.*;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MPeriod;
import org.compiere.util.DB;

/**
 *	Order Callouts.
 *	
 *  @author Fabian Aguilar OFB faaguilar
 *  @version $Id: CalloutDMDocument.java,v 1.34 2006/11/25 21:57:24  Exp $
 */
public class CalloutSeqNo extends CalloutEngine
{

	 
	public String sequence (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		
		if (mTab.get_ValueAsString("WinType").equalsIgnoreCase("O") )
		{
			String sqlNext = "select Currentnext from AD_Sequence seq "+
					"inner join C_DocType doc on (seq.AD_Sequence_ID = doc.DocNoSequence_ID) "+
					"where C_DocType_ID=? ";
			
			int next = DB.getSQLValue(null, sqlNext, mTab.getValue("C_DocType_ID"));
			
			if (next > 0)
			{
				mTab.setValue("DocumentNo", Integer.toString(next));
			}
			
		}
			
		return "";
	}	//	charge
	
	
}	//	BlumosOrder

