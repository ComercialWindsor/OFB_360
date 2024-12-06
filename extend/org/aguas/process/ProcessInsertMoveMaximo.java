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
import org.compiere.util.Env;
 
/**
 *	Proceso Borrado Costeo Flota Petrobras
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class ProcessInsertMoveMaximo extends SvrProcess
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
		String sqlInOut = "SELECT tipo_transacccion,tipo_des_dev, cod_articulo,cod_bodega,nom_producto,num_ot,qtyentered," +
				"priceentered, linenetamt, movementdate, dateacct,name_user,user_to,Num_OC,Num_Line_OC," +
				"substr(org_name,0,8) as org_name,id_planta, M_inout_id FROM RVOFB_InOutMaximo where ad_client_id = "+Env.getAD_Client_ID(getCtx());
		
		try
		{
			int Max_ID = 0;
			Max_ID = DB.getSQLValue(get_TrxName(), "select MAX(SLXINTER_DESID) from SLXINTER_DES@DBL_MAXIMO_MAXDEMO_INACALIRI");
			
			PreparedStatement pstmt = DB.prepareStatement (sqlInOut.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				Max_ID++;
				String codArticulo = rs.getString("cod_articulo");
				String codBodega = rs.getString("cod_bodega");
				String userTo = rs.getString("user_to");
				
				//String modificados 
				if (codArticulo != null)
					codArticulo = "'"+codArticulo+"'";
				if (codBodega != null)
					codBodega = "'"+codBodega+"'";
				if (userTo != null)
					userTo = "'"+userTo+"'";
								
				String movementDate = rs.getString("movementdate").substring(8,10);
				movementDate = movementDate + "-" + rs.getString("movementdate").substring(5,7);
				movementDate = movementDate +"-"+rs.getString("movementdate").substring(0,4) ;
				
				String transactionDate = rs.getString("dateacct").substring(8,10) + "-" +
						rs.getString("dateacct").substring(5,7) +"-"+rs.getString("dateacct").substring(0,4) ;
				
				
				String InsertD=new String("INSERT INTO SLXINTER_DES@DBL_MAXIMO_MAXDEMO_INACALIRI (SLXINTER_DESID,TIPOTRANS,ISSUETYPE,ITEMNUM,ALMACEN,DESCRIPCION,WONUM,QUANTITY," +
						"UNITCOST,LINECOST,ACTUALDATE,TRANSDATE,ENTERBY,ISSUETO,PONUM,POLINENUM,ORGID,SITEID) " +						
						" VALUES ("+Max_ID+","+rs.getInt("tipo_transacccion")+",'"+rs.getString("tipo_des_dev")+"',"+codArticulo+","+
						codBodega+",'"+rs.getString("nom_producto")+"','"+rs.getString("num_ot")+"',"+rs.getDouble("qtyentered")+","+
						rs.getDouble("priceentered")+","+rs.getDouble("linenetamt")+",'"+movementDate+"','" +transactionDate+"','"+
						rs.getString("name_user")+"',"+userTo+",'"+rs.getString("Num_OC")+"',"+rs.getInt("Num_Line_OC")+",'"+
						rs.getString("org_name")+"','"+rs.getString("id_planta")+"')");
				
				DB.executeUpdate(InsertD, get_TrxName());		
				
				DB.executeUpdate("UPDATE M_InOut SET processmaximo = 'Y' WHERE M_InOut_ID = "+rs.getInt("M_inout_id"), get_TrxName());
			}					
				
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "BP - " + sqlInOut.toString(), e);
		}
		
		return "OK";
	}	//	doIt
	
}	//	OrderOpen
