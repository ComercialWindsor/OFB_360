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
 *  @version $Id: CalloutOrder.java,v 1.34 2006/11/25 21:57:24  Exp $
 */
public class CalloutBlumos extends CalloutEngine
{

	 
	public String QtyRate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null)
			return "";
		BigDecimal QtyConverted= new BigDecimal("0.0");
		BigDecimal QtyK = (BigDecimal)mTab.getValue("QtyKilo");
		BigDecimal QtyQ = (BigDecimal)mTab.getValue("QtyOrdered");
		BigDecimal PriceActual = (BigDecimal)mTab.getValue("PriceActual");
		int UOM_ID = (Integer)mTab.getValue("C_UOM_ID");
		
		if(QtyQ.compareTo(Env.ZERO)==0)
			QtyQ = (BigDecimal)mTab.getValue("QtyEntered");
		
		 if (mField.getColumnName().equals("QtyKilo"))
		 {
			 int fromQty_ID=DB.getSQLValue(null, "select C_UOM_ID from C_UOM where description='TQ'");
			 QtyConverted=MUOMConversion.convert(fromQty_ID, UOM_ID, QtyK, true);
			 mTab.setValue("QtyOrdered", QtyConverted);
			 mTab.setValue("QtyEntered", QtyConverted);
			 
			 BigDecimal LineNetAmt = QtyConverted.multiply(PriceActual);
			 if(QtyK!=null)
			 mTab.setValue("PriceKilo", LineNetAmt.divide(QtyK, BigDecimal.ROUND_HALF_EVEN));
		 }
		 else if (mField.getColumnName().equals("QtyOrdered") || mField.getColumnName().equals("QtyEntered"))
		 {
			 int toQty_ID=DB.getSQLValue(null, "select C_UOM_ID from C_UOM where description='TQ'");
			 QtyConverted=MUOMConversion.convert(UOM_ID, toQty_ID, QtyQ, true);
			 mTab.setValue("QtyKilo", QtyConverted);
			 BigDecimal LineNetAmt = QtyQ.multiply(PriceActual);
			 if(QtyConverted!=null)
			 mTab.setValue("PriceKilo", LineNetAmt.divide(QtyConverted, BigDecimal.ROUND_HALF_EVEN));
			 
		 }
		
		return "";
	}	//	charge

	

}	//	BlumosOrder

