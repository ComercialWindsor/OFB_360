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

/**
 *	Order Callouts.
 *	
 *  @author Fabian Aguilar OFB faaguilar
 *  @version $Id: CalloutProduct.java,v 1.34 2008/06/06 21:57:24 $
 */
public class CalloutProduct extends CalloutEngine
{
	/**	Debug Steps			*/
	private boolean steps = false;


	 
	public String barCode (Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value)
	{
			
		
		String code=(String)value;
		int sumimpar=0;
		int sumpar=0;
		
		if(code==null)
		return "";
		
		if(code.length()>12 || code.length()==0)
		return "Digitos Incorrectos";
	
		//sum par
		//log.config("code.charAt(0):"+Character.getNumericValue(code.charAt(0)));
		for(int i=0;i<code.length();i=i+2)
		sumpar=sumpar+Character.getNumericValue(code.charAt(i));
		
		//sum impar
		//log.config("code.charAt(1):"+Character.getNumericValue(code.charAt(1)));
		for(int x=1;x<code.length();x=x+2)
		sumimpar=sumimpar+Character.getNumericValue(code.charAt(x));
		
		sumimpar=sumimpar*3;
		
		int total=sumimpar+sumpar;
		
		int tenp=((total/10)+1)*10;
		
		int ctl=tenp-total;
		
		log.config("sumpar:"+sumpar+" sumimpar:"+sumimpar+ " total:"+total+" tenp:"+tenp +" ctl:"+ctl );
		
		if(ctl!=10)
		mTab.setValue("Classification",new Integer(ctl));
		else
		mTab.setValue("Classification",new Integer(0));
		
		return "";
	}	//	

	
}	//	CalloutProduct
