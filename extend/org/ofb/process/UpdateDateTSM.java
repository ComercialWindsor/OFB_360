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
package org.ofb.process;

import java.util.logging.*;

import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
 
/**
 *	report infoprojectPROVECTIS
 *	
 *  @author ininoles
 *  @version $Id: UpdateDateTSM.java,v 1.2 2013/10/21 16:00:00 ininoles$
 */
public class UpdateDateTSM extends SvrProcess
{
	private int p_PInstance_ID;	

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
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		
		String sql ="update m_movement set movementdate = "+ 
				"(select to_timestamp(to_char(min(m_movementline.tp_finalhr),'YYYY-MM-DD'),'YYYY-MM-DD') "+
				"from m_movementline where m_movementline.m_movement_id=m_movement.m_movement_id) "+
				"WHERE (select to_timestamp(to_char(min(m_movementline.tp_finalhr),'YYYY-MM-DD'),'YYYY-MM-DD') "+
				"from m_movementline where m_movementline.m_movement_id=m_movement.m_movement_id) is not null";
		
		DB.executeUpdate(sql, get_TrxName());
				
		return "Fechas Actualizadas";
	}	//	doIt
	
}	//	OrderOpen
