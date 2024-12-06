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
package org.ofb.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	report infoprojectPROVECTIS
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class OFBGenerateFields extends SvrProcess
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
		//
		if (DB.isPostgreSQL())
		{
			String field1 = "ALTER TABLE c_allocationline ADD COLUMN c_invoicepayschedule_id numeric(10,0)";
			DB.executeUpdate(field1, get_TrxName());
			
			String field1_2 = "ALTER TABLE c_allocationline ALTER COLUMN c_invoicepayschedule_id SET DEFAULT NULL::numeric"; 
			DB.executeUpdate(field1_2, get_TrxName());
			
			String field2 = "ALTER TABLE ad_role ADD COLUMN deleteattachment character(1)";
			DB.executeUpdate(field2, get_TrxName());
			
			String field2_2 = "ALTER TABLE ad_role ALTER COLUMN deleteattachment SET DEFAULT 'Y'::bpchar"; 
			DB.executeUpdate(field2_2, get_TrxName());
			
			String field3 = "ALTER TABLE c_paymentrequestline ADD COLUMN c_invoicepayschedule_id numeric(10,0)";
			DB.executeUpdate(field3, get_TrxName());
			
			String field3_2 = "ALTER TABLE c_paymentrequestline ALTER COLUMN c_invoicepayschedule_id SET DEFAULT NULL::numeric"; 
			DB.executeUpdate(field3_2, get_TrxName());
		}
		if (DB.isOracle())
		{
			
		}
		return "Campos Creados";
	}	//	doIt
	
}	//	OrderOpen
