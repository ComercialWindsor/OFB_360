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
package org.andes.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.X_CD_Header;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	
 *	
 *  @author Italo Niñoles Ininoles
 */
public class ProcessDisponForDate extends SvrProcess
{
	private Timestamp  p_dateTrx;
	
	private int			created = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 ProcessInfoParameter[] para = getParameter();
			for (int i = 0; i < para.length; i++)
			{
				String name = para[i].getParameterName();
				
				if (name.equals("DateTrx"))
					p_dateTrx = (Timestamp)para[i].getParameter();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	 
		StringBuffer sql= new StringBuffer("SELECT CD_Header_ID FROM CD_Header WHERE IsActive = 'Y' AND DateTrx = ?");		 
	    PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), this.get_TrxName());
			pstmt.setTimestamp(1, p_dateTrx);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				X_CD_Header head = new X_CD_Header(getCtx(), rs.getInt("CD_Header_ID"), get_TrxName());
				head.set_CustomColumn("Processed", true);
				head.save();
				created++;
			}
		}
		catch (Exception e)
		{
			log.severe(e.getMessage());
		}
		finally{
			DB.close(rs, pstmt);
			rs=null;pstmt=null;
		}
		return "Procesadas "+ created;
	}	//	doIt
}
