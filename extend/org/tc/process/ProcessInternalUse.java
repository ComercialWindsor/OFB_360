/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Contributor(s): Chris Farley - northernbrewer                              *
 *****************************************************************************/
package org.tc.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  @author mfrojas
 */
public class ProcessInternalUse extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		String sqlDet = "SELECT m_inventory_id from m_inventory where docstatus='DR' AND "+
				" created > '2017-06-28' and created < '2017-06-29'";
		
		PreparedStatement pstmt = null;
		int cant = 0;
		log.config("sqldet "+sqlDet);
		try
		{
			pstmt = DB.prepareStatement (sqlDet, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();			
			MInventory inv = null;			
			
			while (rs.next ())
			{
					if(cant == 150)
						break;
					log.config("ID INVENTARIO "+rs.getInt("m_inventory_id"));
					
					inv = new MInventory(getCtx(), rs.getInt("m_inventory_id"), get_TrxName());
					inv.processIt("CO");
					inv.setDescription(inv.getDescription()+" Procesado Automático");
					inv.save();

					cant++;
					log.config("cantidad actual: "+cant);
			}					
			log.config("Se ha completado "+cant+" consumos ");
			//actualizamos registros procesados
			rs.close ();
			pstmt.close ();
			pstmt = null;	
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}		
		return "OK "+cant;
	}	//	doIt
}	//	Replenish

