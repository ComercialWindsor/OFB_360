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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 ininoles$
 */
public class UpdateCreditBP extends SvrProcess
{
	private int	ID_BPartner = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_BPartner_ID"))
			{
				ID_BPartner = para[i].getParameterAsInt();				
			}
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		int contador=0;
		String sql = "SELECT C_BPartner_ID FROM C_BPartner WHERE AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+" AND IsActive = 'Y' ";
		if(ID_BPartner > 0)
			sql = sql+" AND C_BPartner_ID = "+ID_BPartner;
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();		
			//ciclo Socios de negocio
			while (rs.next())
			{								
				MBPartner bp = new MBPartner(getCtx(), rs.getInt("C_BPartner_ID"), get_TrxName());
				updateBP(bp);
				contador= contador +1;
			}
		rs.close();
		pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "Se han actualizado " +contador + " sociosde negocio";
	}	//	doIt
	private void updateBP(MBPartner bp)
	{
			bp.setTotalOpenBalance();		//	recalculates from scratch
			if (bp.save())
				log.fine(bp.toString());
			else
				log.log(Level.SEVERE, "BP not updated - " + bp);
	}	//	updateBP
}	//	
