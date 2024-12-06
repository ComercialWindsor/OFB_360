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

import java.util.*;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

/**
 *	Order Callouts.
 *	
 *  @author Italo Niñoles OFB ininoles
 *  @version $Id: CalloutJasperOFB.java,v 1.0 2015/07/05  Exp $
 */
public class CalloutJasperOFB extends CalloutEngine
{
	public String CreateURL (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{	
		String url = null;
		String link = mTab.get_ValueAsString("Link");
		String para1 = mTab.get_ValueAsString("PA_Parameter1");
		String para2 = mTab.get_ValueAsString("PA_Parameter2");
		String para3 = mTab.get_ValueAsString("PA_Parameter3");
		String para4 = mTab.get_ValueAsString("PA_Parameter4");
		String paraUser = mTab.get_ValueAsString("PA_User");
		String paraPass = mTab.get_ValueAsString("PA_Pass");
		
		if(link != null && link != "" && link != " ")
		{
			url = link;
			if(para1 != null && para1 != "" && para1 != " ")
			{
				url = url + para1;
			}
			if(para2 != null && para2 != "" && para2 != " ")
			{
				url = url + para2;
			}
			if(para3 != null && para3 != "" && para3 != " ")
			{
				url = url + para3;
			}
			if(para4 != null && para4 != "" && para4 != " ")
			{
				url = url + para4;
			}
			if(paraUser != null && paraUser != "" && paraUser != " ")
			{
				url = url + paraUser;
				if(paraPass != null && paraPass != "" && paraPass != " ")
				{
					url = url + paraPass;				
				}
			}			
			mTab.setValue("urlJasper",url);
		}				
		return "";
	}
}


