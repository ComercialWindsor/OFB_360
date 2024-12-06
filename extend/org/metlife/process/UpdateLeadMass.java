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
package org.metlife.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MCampaign;
import org.compiere.model.X_C_CampaignActivities;
import org.compiere.model.X_C_CampaignFollow;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	report infoprojectPROVECTIS
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class UpdateLeadMass extends SvrProcess
{
	
	//private int p_PInstance_ID;	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_ID_User = 0;
	private String p_Description = "";	
	private String p_FinalStatus = "";
	
	protected void prepare()
	{			
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_User_ID"))
				p_ID_User = para[i].getParameterAsInt();
			else if (name.equals("Description"))
				p_Description = (String)para[i].getParameter();
			else if (name.equals("FinalStatus"))
				p_FinalStatus = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws java.lang.Exception
	{	
		//nueva forma de ejecutar el proceso
		String sql = "SELECT C_CampaignFollow_ID FROM C_CampaignFollow " +
				"WHERE IsUseUpdate = 'Y' and AD_UserRefUp_ID = " + Env.getAD_User_ID(getCtx());

		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			rs = pstmt.executeQuery();					
			while (rs.next())
			{
				X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), rs.getInt("C_CampaignFollow_ID"), get_TrxName());
				//cFollow.setAD_User_ID(p_ID_User);
				X_C_CampaignActivities cAct = new X_C_CampaignActivities(getCtx(), 0, get_TrxName());				
				cAct.setC_CampaignFollow_ID(cFollow.get_ID());
				cAct.setAD_Org_ID(cFollow.getAD_Org_ID());
				cAct.set_CustomColumn("AD_User_ID", Env.getAD_User_ID(getCtx()));
				cAct.setDescription("Lead Derivado");
				Calendar cal = new GregorianCalendar();
				String fecha = Integer.toString(cal.get(Calendar.DATE))+"-"
								+Integer.toString(cal.get(Calendar.MONTH)+1)+"-"
								+Integer.toString(cal.get(Calendar.YEAR));
				if(p_Description != null)
					cAct.setDescription(cAct.getDescription()+". Comentario:"+p_Description+". Fecha:"+fecha);
				else
					cAct.setDescription(cAct.getDescription()+". Fecha:"+fecha);
				cAct.save();	
			}
		}
		catch (SQLException e)
		{
			rollback();
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}	
		
		String sqlUpdate = "UPDATE C_CampaignFollow SET " ;		
		
		if (p_ID_User > 0)
			sqlUpdate = sqlUpdate+" AD_User_ID = "+p_ID_User+", ";
		if(p_FinalStatus != null)
			sqlUpdate = sqlUpdate+" FinalStatus = '" +p_FinalStatus+"', ";
		sqlUpdate = sqlUpdate+" IsUseUpdate = 'N', AD_UserRefUp_ID = null WHERE IsUseUpdate = 'Y' and AD_UserRefUp_ID = " + Env.getAD_User_ID(getCtx());		
		int cant = DB.executeUpdate(sqlUpdate, get_TrxName());
				
		return "Se han actualizado "+cant+" registros";
	}	//	doIt
}	//	OrderOpen

