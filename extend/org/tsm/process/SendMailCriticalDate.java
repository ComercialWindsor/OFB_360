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
package org.tsm.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.compiere.model.MClient;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SendMailCriticalDate.java $
 */
public class SendMailCriticalDate extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{

	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MClient client = new MClient(getCtx(), Env.getAD_Client_ID(getCtx()), get_TrxName());
		int cant=0;
		String sql = "SELECT days60, days30, days0, ad_user_id, description,email from RVOFB_FechasVencidasView ";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();			
			
			while (rs.next())
			{
				if (rs.getInt("AD_User_ID") > 0)
				{	
					//generamos mensaje completo
					String ln = System.getProperty("line.separator");
					StringBuilder str = new StringBuilder();
					str.append("Estimado Usuario:");
					str.append(ln);
					str.append(ln);
					str.append("A continuación detalle de fechas criticas que tiene asociadas");
					str.append(ln);
					str.append(rs.getString("description"));
					str.append(ln);
					str.append(ln);
					str.append(ln);
					str.append("ATTE Adempiere");					
					//creamos email
					EMail mail = new EMail(client, client.getRequestEMail(), rs.getString("email"),
							"Recordatorio Fechas Criticas", str.toString());
					//seteamos parametros de autenticacion
					mail.createAuthenticator(client.getRequestUser(),client.getRequestUserPW());
					//enviamos correo
					mail.send();					
					log.config("Correo Enviado a "+rs.getString("email"));
					log.config("Errores Correo: "+mail.getSentMsg());
					cant++;
				}				
			}
		}catch (Exception e) {
			log.config("Error enviando Correo");
		}
		/*		
		try {
			EMail mail = new EMail(client, "ofbconsulting1@gmail.com", p_Mail_To,p_Mail_Subject, p_Mail_Text);
			mail.createAuthenticator("ofbconsulting1@gmail.com", "ofb2014!");
			mail.send();
			return "Enviado";
		} catch (Exception e) {
			log.config("Error al enviar correo: "+e);
			return "Envio Fallido";
		}*/
		return "Se han enviado "+cant+ " correos";
	}	//	doIt
}
