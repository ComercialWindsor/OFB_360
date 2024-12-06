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

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
import java.sql.CallableStatement;
import java.util.logging.Level;

 
/**
 *	proceso prototipo metlife
 *	
 *  @author ininoles
 *  @version $Id: GenerateFollowCampaign.java,v 1.2 2014/09/12 ininoles$
 */
public class UpdatedCampaignFollow extends SvrProcess
{
	//private Properties 		m_ctx;
	private int p_PInstance_ID;
	private int p_C_CampaignFollow_ID = 0;
	private String p_finalStatus = "";
	private int p_idUser = 0;
	private String p_description = "";	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_PInstance_ID = getAD_PInstance_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("FinalStatus"))
				p_finalStatus = (String)para[i].getParameter();
			else if (name.equals("AD_User_ID"))
				p_idUser = para[i].getParameterAsInt();
			else if (name.equals("Description"))
				p_description = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		p_C_CampaignFollow_ID=getRecord_ID();
		//m_ctx = Env.getCtx();
		
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{	
		try
		{
			if (p_finalStatus != null && p_finalStatus != "" && p_finalStatus.length() > 0)
			{
				X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), p_C_CampaignFollow_ID, get_TrxName());
				//DB.executeUpdate("UPDATE C_CampaignFollow SET FinalStatus = '"+finalStatus+"' where C_CampaignFollow_ID = "+p_C_CampaignFollow_ID, get_TrxName());
				cFollow.setFinalStatus(p_finalStatus);
				cFollow.save();			
				if (p_idUser > 0 && p_finalStatus.compareToIgnoreCase("D") ==0 )
				{
					cFollow.setAD_User_ID(p_idUser);
					X_C_CampaignActivities cAct = new X_C_CampaignActivities(getCtx(), 0, get_TrxName());				
					cAct.setC_CampaignFollow_ID(cFollow.get_ID());
					cAct.setAD_Org_ID(cFollow.getAD_Org_ID());
					cAct.set_CustomColumn("AD_User_ID", Env.getAD_User_ID(getCtx()));
					cAct.setDescription("Lead Derivado");
					if(p_description != null)
					{
						Calendar cal = new GregorianCalendar();
						String fecha = Integer.toString(cal.get(Calendar.DATE))+"-"
										+Integer.toString(cal.get(Calendar.MONTH)+1)+"-"
										+Integer.toString(cal.get(Calendar.YEAR));
						cAct.setDescription(cAct.getDescription()+". Comentario:"+p_description+". Fecha:"+fecha);
					}
					cAct.save();					
				}				
				cFollow.save();
				//cambio de campaña en derivacion usuario genericos si existiese
				try
				{
					MUser user = new MUser(getCtx(), p_idUser, get_TrxName());
					if(user.get_ValueAsInt("C_Campaign_ID") > 0)
						cFollow.setC_Campaign_ID(user.get_ValueAsInt("C_Campaign_ID"));
					cFollow.save();
						
				}catch (Exception e)
				{	
					log.log(Level.SEVERE, "No se pudo cambiar campaña", e);
				}				
				//se pregunta si se deriva  usuario servicio al clliente para enviar correo
				try
				{
					if(p_idUser >0)
					{
						MUser user = new MUser(getCtx(), p_idUser, get_TrxName());
						if(user.getName().toUpperCase().contains("CLIENTE"))
						{						
							CallableStatement cst = DB.prepareCall("{call PA_CUSTOMERSERVICES (?)}");
							cst.setInt(1, p_PInstance_ID);
							cst.execute();
						}
					}
				}catch (Exception e)
				{	
					log.log(Level.SEVERE, "No se pudo ejecutar proceso", e);
				}
				
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "Seguimiento Actualizado";
	}	//	doIt
	
}	//	OrderOpen
