/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Copyright (C) 2003-2007 e-Evolution,SC. All Rights Reserved.               *
 * Contributor(s): Victor Perez www.e-evolution.com                           *
 *****************************************************************************/

package org.geminis.process;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import org.compiere.model.X_C_Recurring;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class ProcessERPG_PInsertar_Servicio extends SvrProcess {
	/**					*/
	private int	p_Record_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{	
		p_Record_ID = getRecord_ID();
	} //	prepare

	protected String doIt() throws Exception
	{		
		X_C_Recurring recu = new X_C_Recurring(getCtx(), p_Record_ID, get_TrxName());
		//se ejecuta procesimiento
		String dateStr;
		if(recu.getDateNextRun().getDate() < 10)
			dateStr = "0"+recu.getDateNextRun().getDate();
		else
			dateStr = Integer.toString(recu.getDateNextRun().getDate());
		dateStr=dateStr+"/";
		if((recu.getDateNextRun().getMonth()+1) < 10)
			dateStr = dateStr+"0"+(recu.getDateNextRun().getMonth()+1);
		else
			dateStr = dateStr+(recu.getDateNextRun().getMonth()+1);
		dateStr=dateStr+"/";
		dateStr = dateStr+(1900+recu.getDateNextRun().getYear());
		log.config("fecha:"+dateStr);
		
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //Date fecha = new Date(simpleDateFormat.parse(dateStr).getTime());
		//PreparedStatement pstmt = null;
		//String sql = "execute ERPG_KGENERA_SERVICIOS.ERPG_PInsertar_Servicio("+recu.get_ValueAsInt("s_timeexpense_id")+",'"+dateStr+"')";
		//log.config("SQL a ejecutar:"+sql);
		//pstmt = DB.prepareStatement(sql, get_TrxName());
		//pstmt.execute();
        String sql="{call ERPG_KGENERA_SERVICIOS.ERPG_PInsertar_Serv_ADempiere(?,?)}";
		try {			
			CallableStatement cst = DB.prepareCall(sql);
			cst.setInt(1, recu.get_ValueAsInt("s_timeexpense_id"));			
			cst.setString(2, dateStr);
			cst.execute();		
			commitEx();
		} catch (SQLException e) {
			log.config("error catch:"+e.toString());
		}
		return "Procesado";
	}	
}
