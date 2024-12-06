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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	proceso prototipo metlife
 *	
 *  @author ininoles
 *  @version $Id: GenerateFollowCampaign.java,v 1.2 2014/09/12 ininoles$
 */
public class CloseCampaign extends SvrProcess
{
	private Properties 		m_ctx;	
	private int p_C_Campaign_ID = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_C_Campaign_ID=getRecord_ID();
		m_ctx = Env.getCtx();
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
			DB.executeUpdate("UPDATE C_Campaign SET Status = 'CE' where C_Campaign_ID = "+p_C_Campaign_ID, get_TrxName());
			DB.executeUpdate("UPDATE C_Campaign SET DateEnd = now() where C_Campaign_ID = "+p_C_Campaign_ID, get_TrxName());
			DB.executeUpdate("UPDATE C_Campaign SET Processed = 'Y' where C_Campaign_ID = "+p_C_Campaign_ID, get_TrxName());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "Campaña Cerrada";
	}	//	doIt
	
}	//	OrderOpen
