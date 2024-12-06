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
import java.util.logging.Level;

import org.blumos.model.BlumosUtilities;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 *  @author Italo Niñoles
 */
public class ProcessCORREOSNC extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int 			Record_ID;
	private String			p_laTabla;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("la_tabla"))
				p_laTabla = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		//no se creara registro porque es invocado de 2 tablas distintas
		String v_cc = "";
		String v_to = "";
		String el_subject = "";
        String el_correo = ""; 
        int id_correo = 0;
        String v_query = "";
		if(p_laTabla.compareTo("T_BL_NCACCION") == 0)
		{
			v_query = "SELECT nc.documentno, substr(nc.description,1,160) as desc_nc, damerefnombre(1000080,nc.tipo_nc) as Tipo, damerefnombre(1000081,nc.origen_nc) as origen," +
				" damerefnombre(1000083,nc.status) as status," +
				" auc.name as creado_por, aur.name as responsable, aud.name as auditor," +
				" nca.created as fecha_accion, nca.movementdate as fecha_limite," +
				" damerefnombre(1000082,nca.tipo_accion) as tipo_correccion, nca.description, nca.analisis_causa," +
				" nca.createdby as creado_por_id, nc.auditor_id, nc.ad_user_id as responsable_id, nc.tipo_nc, nc.movementdate as fecha_nc" +
				" FROM T_BL_NCACCION nca " +
				" inner join t_bl_nc nc on (nca.t_bl_nc_id=nc.t_bl_nc_id)" +
				" inner join ad_user auc on (nca.createdby=auc.ad_user_id)" +
				" left join ad_user aur on (nc.ad_user_id=aur.ad_user_id)" +
				" left join ad_user aud on (nc.auditor_id=aud.ad_user_id)" +
				" left join ad_user auv on (nc.salesrep_id=auv.ad_user_id)" +
				" where nca.t_bl_ncaccion_id="+Record_ID;
			id_correo = 1000040;
			
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (v_query, null);
			ResultSet rs = pstmt.executeQuery ();
			while(rs.next ())
			{
				v_to = BlumosUtilities.DameMail(rs.getInt("creado_por_id"), 0, getCtx(), get_TrxName());
				v_to = v_to+","+BlumosUtilities.DameMail(rs.getInt("responsable_id"), 0, getCtx(), get_TrxName());
				v_cc = BlumosUtilities.DameMail(rs.getInt("auditor_id"), 0, getCtx(), get_TrxName());
				v_cc = v_cc + "'||','||damecorreo("+id_correo+",'cc')";
				el_subject = "SGC-SEG "+rs.getString("tipo_nc")+" No."+rs.getString("documentno");
				el_correo = "<pre>"+el_subject+"\n\n"+
					"Se ha ingresado una accion para : "+rs.getString("Tipo")+" No. "+rs.getString("documentno")+"\n"+
					"Origen: "+rs.getString("origen")+" de fecha: "+rs.getTimestamp("fecha_nc")+"\n"+
					"Texto de "+rs.getString("tipo_nc")+": "+rs.getString("desc_nc")+"\n\n"+
					"Por el usuario: "+rs.getString("creado_por")+
					"Fecha de accion: "+rs.getTimestamp("fecha_accion")+
					"Tipo de Correccion: "+rs.getString("tipo_correccion")+"\n\n"+
					"Analisis de causa: "+rs.getString("analisis_causa")+"\n\n"+
					"Descripcion Correccion: "+rs.getString("description")+"\n\n"+
					"Fecha limite de implementacion: "+rs.getTimestamp("fecha_limite")+"\n"+
					"\n\nUsuario Responsable: "+rs.getString("responsable")+"\n";
				DB.executeUpdate("UPDATE t_bl_ncaccion SET correo_enviado='Y' WHERE t_bl_ncaccion_id="+Record_ID, get_TrxName());
				String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+v_to+"','"+v_cc+",'cmendoza@blumos.cl','"+el_subject+"','"+el_correo+"</pre>')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
				pstmtSM.execute();
			}
		}
		else if(p_laTabla.compareTo("T_BL_NC") == 0)
		{
			v_query = "SELECT nc.documentno, nc.description as desc_nc, damerefnombre(1000080,nc.tipo_nc) as Tipo, damerefnombre(1000081,nc.origen_nc) as origen," +
				" damerefnombre(1000083,nc.status) as status," +
				" auc.name as creado_por, aur.name as responsable, aud.name as auditor," +
				" nc.createdby as creado_por_id, nc.auditor_id, nc.ad_user_id as responsable_id, nc.tipo_nc, nc.movementdate as fecha_nc" +
				" FROM t_bl_nc nc" + 
				" inner join ad_user auc on (nc.createdby=auc.ad_user_id)" +
            	" left join ad_user aur on (nc.ad_user_id=aur.ad_user_id)" +
            	" left join ad_user aud on (nc.auditor_id=aud.ad_user_id)" +
            	" left join ad_user auv on (nc.salesrep_id=auv.ad_user_id)" +
            	" where nc.t_bl_nc_id="+Record_ID;
			id_correo = 1000039;
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (v_query, null);
			ResultSet rs = pstmt.executeQuery ();
			while(rs.next ())
			{
				v_to = BlumosUtilities.DameMail(rs.getInt("creado_por_id"), 0, getCtx(), get_TrxName());
				v_to = v_to+","+BlumosUtilities.DameMail(rs.getInt("responsable_id"), 0, getCtx(), get_TrxName());
				v_cc = BlumosUtilities.DameMail(rs.getInt("auditor_id"), 0, getCtx(), get_TrxName());
				v_cc = v_cc + "'||','||damecorreo("+id_correo+",'cc')";
				el_subject = "SGC-"+rs.getString("tipo_nc")+" No."+rs.getString("documentno");
				el_correo = "<pre>"+el_subject+"\n\n"+
					"Registro de : "+rs.getString("Tipo")+" No. "+rs.getString("documentno")+"\n"+
					"Origen: "+rs.getString("origen")+" de fecha: "+rs.getTimestamp("fecha_nc")+"\n"+
					"Descripcion: "+rs.getString("tipo_nc")+": "+rs.getString("desc_nc")+"\n"+
					"Por el usuario: "+rs.getString("creado_por")+
					"\n\nUsuario Responsable: "+rs.getString("responsable")+"\n";
				DB.executeUpdate("UPDATE t_bl_nc SET correo_enviado='Y' WHERE t_bl_nc_id="+Record_ID, get_TrxName());
				String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+v_to+"','"+v_cc+",'cmendoza@blumos.cl','"+el_subject+"','"+el_correo+"</pre>')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
				pstmtSM.execute();
			}
		}
		return "Procesado";
	}	//	doIt
}	//	Replenish

