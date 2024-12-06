/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.ofb.process;

import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class UpdateReconciliedPayment extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int 			Record_ID;
	private String P_Message;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();		
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		//ininoles proceso de actualizacion campo reconcilied
		String sqlvalidBS = "UPDATE C_Payment SET IsReconciled = 'Y' WHERE DocStatus IN ('CO','CL','VO') "+
				"AND C_Payment_ID IN "+
				"(SELECT C_Payment_ID FROM C_BankStatementLine cl INNER JOIN C_BankStatement c ON (cl.C_BankStatement_ID = c.C_BankStatement_ID) "+
				"AND DocStatus IN ('CO','CL')) AND (IsReconciled IS NULL OR IsReconciled = 'N')";
		
		int cant = DB.executeUpdate(sqlvalidBS);
		
		String sqlvalidCD = "UPDATE C_Payment SET IsReconciled = 'Y' WHERE DocStatus IN ('CO','CL','VO') "+
				"AND C_Payment_ID IN (SELECT cil.C_Payment_ID FROM C_InvoiceLine cil "+
				"INNER JOIN C_Invoice ci ON (cil.C_Invoice_ID = ci.C_Invoice_ID) "+
				"INNER JOIN C_DocType cdt ON (ci.C_DocTypeTarget_ID = cdt.C_DocType_ID) "+
				"WHERE DocStatus IN ('CO','CL') AND DocBaseType IN ('VDC','CDC') ) "+ 
				"AND (IsReconciled IS NULL OR IsReconciled = 'N')";
		
		int cant2 = DB.executeUpdate(sqlvalidCD);
		cant = cant +cant2;
		
		return "Se han actualizado "+cant+" pagos";
	}
	
}	//	CopyOrder
