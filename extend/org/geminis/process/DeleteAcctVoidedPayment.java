/******************************************************************************
0 * Product: Adempiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.geminis.process;

import java.util.logging.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;

 
/**
 *	report infoprojectPROVECTIS
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class DeleteAcctVoidedPayment extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		
		String sqlDelete = "DELETE from Fact_acct where AD_Table_ID = 335 and Record_ID IN "+ 
				"(SELECT C_Payment_ID  from C_Payment ci WHERE docstatus IN ('VO'))";
		try
		{
			DB.executeUpdate(sqlDelete, get_TrxName());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "Contabilidad Borrada";
	}	//	doIt
	
}	//	OrderOpen
