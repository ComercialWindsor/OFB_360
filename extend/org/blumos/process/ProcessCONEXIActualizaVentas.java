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
package org.blumos.process;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.model.X_T_BL_CONEXI_PROY;
import org.compiere.model.X_T_BL_CONEXI_PROYCLIENTE;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 *  @author Italo Niñoles
 */
public class ProcessCONEXIActualizaVentas extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int 			Record_ID;
	
	protected void prepare()
	{
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		X_T_BL_CONEXI_PROY proy = new X_T_BL_CONEXI_PROY(getCtx(), Record_ID, get_TrxName());
		String V_query ="SELECT c_bpartner_id, H0, H1, H2, H3, H4, H5, H6, H7, H8, H9, H10, H11, H12, P0, P1, P2, P3, P4, P5 FROM rvbl_conexi_ventas_ultimoano WHERE cod_blumos='"+proy.getCOD_BLUMOS()+"'";
		
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (V_query, get_TrxName());
		ResultSet rs = pstmt.executeQuery ();	
		while (rs.next())
		{
			DB.executeUpdate("DELETE FROM T_bl_conexi_proyCLIENTE where T_bl_conexi_proy_id="+proy.get_ID()+" And c_bpartner_id="+rs.getInt("C_BPartner_ID"), get_TrxName());
			X_T_BL_CONEXI_PROYCLIENTE pClient = new X_T_BL_CONEXI_PROYCLIENTE(getCtx(), 0, get_TrxName());
			pClient.setAD_Org_ID(0);
			pClient.setT_BL_CONEXI_PROY_ID(proy.get_ID());
			pClient.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
			pClient.setCOD_BLUMOS(proy.getCOD_BLUMOS());
			pClient.setH12(rs.getBigDecimal("H12"));
			pClient.setH11(rs.getBigDecimal("H11"));
			pClient.setH10(rs.getBigDecimal("H10"));
			pClient.setH9(rs.getBigDecimal("H9"));
			pClient.setH8(rs.getBigDecimal("H8"));
			pClient.setH7(rs.getBigDecimal("H7"));
			pClient.setH6(rs.getBigDecimal("H6"));
			pClient.setH5(rs.getBigDecimal("H5"));
			pClient.setH4(rs.getBigDecimal("H4"));
			pClient.setH3(rs.getBigDecimal("H3"));
			pClient.setH2(rs.getBigDecimal("H2"));
			pClient.setH1(rs.getBigDecimal("H1"));
			pClient.setH0(rs.getBigDecimal("H0"));
			pClient.setP0(rs.getBigDecimal("P0"));
			pClient.setP1(rs.getBigDecimal("P1"));
			pClient.setP2(rs.getBigDecimal("P2"));
			pClient.setP3(rs.getBigDecimal("P3"));
			pClient.setP4(rs.getBigDecimal("P4"));
			pClient.setP5(rs.getBigDecimal("P5"));
			pClient.save(get_TrxName());
		}
		proy.setLOG_CONTROL("Ventas Actualizadas al "+new Timestamp(System.currentTimeMillis()));
		proy.save(get_TrxName());
		
		return "Procesado";
	}	//	doIt
}	//	Replenish

