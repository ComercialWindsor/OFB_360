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
package org.ofb.process.rpl.imp;

import java.sql.*;


import org.ofb.process.rpl.imp.ImportHelper;
import org.compiere.model.*;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;
import org.w3c.dom.Document;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ImportMessage.java,v 1.2 2010/08/12 00:51:01  $
 */
public class ImportMessage extends SvrProcess
{
	
	
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
		int count=0;
		PreparedStatement pstmt = null;
		String sql="select OFB_RplImp_ID from OFB_RplImp where processed='N' ";
		try {
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			
			while ( rs.next() ) {
				X_OFB_RplImp message= new X_OFB_RplImp(Env.getCtx(), rs.getInt(1),get_TrxName());
				Document documentToBeImported = XMLHelper.createDocumentFromString( message.getxml() );
				StringBuffer result = new StringBuffer();
				ImportHelper impHelper = new ImportHelper( Env.getCtx() );
				
				try{
				impHelper.importXMLDocument(result, documentToBeImported, get_TrxName() );
				message.setProcessed(true);
				message.saveEx();
				count++;
				}
				catch (Exception e){
					log.severe(e.getMessage());
					message.setProcessed(false);
					message.saveEx();
					count--;
				}
				
				
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		} 
		catch (Exception e) {
			log.severe(sql+"/"+e);
			
		} 
		finally {
				if (pstmt != null) 
					pstmt.close ();
				pstmt = null;
		}	
			return "Messages: " + count;
		
	}	//	doIt


	
}	//	CopyOrder
