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

import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 *  @author Italo Niñoles
 */
public class ProcessRecosteoProducto extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private Timestamp		p_Desde;
	private int		p_Product_ID;
	private int		p_Client_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DateAcct"))
				p_Desde = (Timestamp)para[i].getParameter();
			else if (name.equals("M_Product_ID"))
				p_Product_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_Client_ID"))
				p_Client_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		String sendMail = "SELECT P_BL_RECOSTEO_PRODUCTO(?,"+p_Product_ID+","+p_Client_ID+")";
		//PreparedStatement pstmtSM = DB.prepareStatement (sendMail, get_TrxName());
		//pstmtSM.execute();
		String result = DB.getSQLValueString(get_TrxName(), sendMail, p_Desde);
		return result;
	}	//	doIt
}	//	Replenish

