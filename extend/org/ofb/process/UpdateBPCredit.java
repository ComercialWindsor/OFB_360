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

import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: UpdateBPCredit.java,v 1.2 2012/06/12 00:51:01  $
 */
public class UpdateBPCredit extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int 			m_Bpartner_ID = 0;
	
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_BPartner_ID"))
				m_Bpartner_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int i =0;
		PreparedStatement pstmt = null;
		   String mysql = "select C_BPartner_ID from C_BPartner where isCustomer='Y' and AD_Client_ID=" + getAD_Client_ID();
		   
		   if(m_Bpartner_ID >0)
			   mysql += " and C_BPartner_ID="+ m_Bpartner_ID ;
		   
		   try
			{
				pstmt = DB.prepareStatement(mysql, get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					int C_BPartner_ID = rs.getInt(1);
					MBPartner bp = new MBPartner(getCtx(), C_BPartner_ID, get_TrxName());
					bp.setTotalOpenBalance();		//	recalculates from scratch
				//	bp.setSOCreditStatus();			//	called automatically
					if (bp.save())
						log.fine(bp.toString());
					else
						log.log(Level.SEVERE, "BP not updated - " + bp);
					i++;
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		
			return "Creditos Actualizados :" + i;
	}
	
}	//	CopyOrder
