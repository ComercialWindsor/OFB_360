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
package org.aguas.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.*;

import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
 
/**
 *	Proceso Borrado Costeo Flota Petrobras
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class ProcessInsertStorageMaximo extends SvrProcess
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
		DB.executeUpdate("DELETE FROM SLXINTER_INV@DBL_MAXIMO_MAXDEMO_INACALIRI", get_TrxName());
		
		String sqlStorage = "SELECT M_Product_ID, substr(value_product,0,10) as value_product, name_product,value_warehouse, uom," +
				"cost, lastprice,qtyonhand, value_org, planta " +
				"FROM RVOFB_StorageMaximo";
		
		try
		{
			int Max_ID = DB.getSQLValue(get_TrxName(), "select MAX(SLXINTER_INVID) from SLXINTER_INV@DBL_MAXIMO_MAXDEMO_INACALIRI");
			
			
			PreparedStatement pstmt = DB.prepareStatement (sqlStorage.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				Max_ID++;
				String InsertD=new String("INSERT INTO SLXINTER_INV@DBL_MAXIMO_MAXDEMO_INACALIRI(SLXINTER_INVID,ITEMNUM,DESCRIPCION,ALMACEN,UNIDADES,AVGCOST," +
						" LASTCOST,CURBAL,ORGID,SITEID)" +
						" VALUES ("+Max_ID+",'"+rs.getString("value_product")+"','"+rs.getString("name_product")+"','"+rs.getString("value_warehouse")+
						"','"+rs.getString("uom")+"',"+rs.getBigDecimal("cost")+","+rs.getBigDecimal("lastprice")+","+rs.getDouble("qtyonhand")+
						",'"+rs.getString("value_org")+"','"+rs.getString("planta")+"')");
				
				DB.executeUpdate(InsertD, get_TrxName());
			}					
				
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "BP - " + sqlStorage.toString(), e);
		}
		
		return "";
	}	//	doIt
	
}	//	OrderOpen
