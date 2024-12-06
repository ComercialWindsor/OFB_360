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
import java.sql.SQLException;
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
public class UpdateNewFieldsOFB extends SvrProcess
{
	private int	p_ProjectOFB_ID = 0;	
	private Timestamp p_MovementDateFrom = null;
	private Timestamp p_MovementDateTo = null;
	private int	p_C_Period_ID = 0;
	private int p_PInstance_ID;	

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 * @throws SQLException 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError, SQLException
	{
		//validacion de campo cuota en allocation
		int cant = 0;
		String sqlUpdate = "";
		String sql = "";
		if (DB.isPostgreSQL())
		{
			sql = "SELECT COUNT(1) FROM information_schema.columns c " +
					" LEFT JOIN information_schema.element_types e" +
					" ON c.table_catalog = e.object_catalog AND" +
					" c.table_schema = e.object_schema AND" +
					" c.table_name = e.object_name AND" +
					" 'TABLE' = e.object_type" +
					" WHERE UPPER(c.table_name) = upper('C_ALLOCATIONLINE')" +
					" AND UPPER(c.column_name) = upper('C_INVOICEPAYSCHEDULE_ID')";
			
			sqlUpdate = "ALTER TABLE c_allocationline ADD c_invoicepayschedule_id numeric(10,0)";

		}
		cant = DB.getSQLValue(get_TrxName(), sql);
		
		if(!(cant>1))
		{
			DB.executeUpdate(sqlUpdate, get_TrxName());
		}
		commitEx();
		
		return "Tablas Actualizadas";
	}	//	doIt
	
}	//	OrderOpen
