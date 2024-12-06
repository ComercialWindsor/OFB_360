/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.process;

import org.compiere.acct.*;
import org.compiere.util.*;
import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;

/**
 *	Posting Documents
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: PostDocs.java,v 1.12 2008/05/31 05:42:02 jjanke Exp $
 */
public class PostDocs extends SvrProcess
{
	Timestamp dateFrom=null;
	Timestamp dateTo=null;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
		//	log.fine("prepare - " + para[i]);
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DateAcct")){
				dateFrom = (Timestamp)para[i].getParameter();
			    dateTo = (Timestamp)para[i].getParameter_To();
			}
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
		MAcctSchema[] m_ass = MAcctSchema.getClientAcctSchema(getCtx(),  getAD_Client_ID());
		int count = 0;
		int countError = 0;
		int[] docs=Doc.getDocumentsTableID();
		String[] tables=Doc.getDocumentsTableName();
		StringBuffer sql = new StringBuffer("");
		int docss=0;
		for (int i = 0; i < docs.length; i++)
		{
			int AD_Table_ID = docs[i];
			String TableName = tables[i];
			String TableField = "DateAcct";
			
			if(TableName.equals("C_DocType") || TableName.equals("PP_Order") || TableName.equals("PP_Cost_Collector") || TableName.equals("DD_Order") 
					|| TableName.equals("HR_Process") || TableName.equals("C_BankFactoring") || TableName.equals("HR_Movement"))
				continue;
			
			if(TableName.equals(MBankStatement.Table_Name))
				TableField = "StatementDate";
			
			if(TableName.equals(MInventory.Table_Name) || TableName.equals(MMovement.Table_Name) || TableName.equals(X_M_Production.Table_Name) || TableName.equals(MProjectIssue.Table_Name))
				TableField = "MovementDate";
			if(TableName.equals(MRequisition.Table_Name))
				TableField = "DateDoc";
			if(TableName.equals(X_DM_Document.Table_Name))
				TableField = "DateTrx";
			//	Post only special documents
			if (AD_Table_ID==0 || AD_Table_ID==MOrder.Table_ID || AD_Table_ID==MDocType.Table_ID)
				continue;
		//  SELECT * FROM table
			if(docss>0)
				sql.append(" UNION ");
			
			sql.append("SELECT ").append(TableName).append("_ID").append(",").append(TableField).append(",").append(AD_Table_ID) 
			.append(" FROM ").append(TableName)
				.append(" WHERE AD_Client_ID=").append(getAD_Client_ID())
				.append(" AND Processed='Y' AND Posted IN ('N','E') AND IsActive='Y'");
			
			if(dateFrom!=null && dateTo!=null)
				sql.append(" AND ").append(TableField).append(" Between ? and ?");
			
		    docss++;
			
			/*if(i< (tables.length-1))
				if(!(tables[i+1].equals("C_DocType") || tables[i+1].equals("PP_Order") || tables[i+1].equals("PP_Cost_Collector") || tables[i+1].equals("DD_Order") 
						|| tables[i+1].equals("HR_Process") || tables[i+1].equals("C_BankFactoring") || tables[i+1].equals("HR_Movement")))*/
				
			//
		}
		
		sql.append(" ORDER BY 2");
			//
			
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql.toString(), null);
				if(dateFrom!=null && dateTo!=null){
					int x=1;
					for (int i = 0; i < docss; i++)
					{
						/*int AD_Table_ID = docs[i];
						if (AD_Table_ID==0 || AD_Table_ID==MOrder.Table_ID)
							continue;*/
						
						pstmt.setTimestamp(x++, dateFrom);
						pstmt.setTimestamp(x++, dateTo);
					}
				}
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					count++;
					boolean ok = true;
					try
					{
						Doc doc = Doc.get (m_ass, rs.getInt(3), rs.getInt(1), null);
						if (doc == null)
						{
							log.severe(getName() + ": No Doc for " + rs.getInt(3));
							ok = false;
						}
						else
						{
							if(doc instanceof Doc_MatchInv)//faaguilar OFB 27/06/2012
							{
								MMatchInv match = new MMatchInv(getCtx() ,doc.get_ID(),get_TrxName());
								MInvoice iv = MInvoice.get(getCtx(), match.getC_InvoiceLine().getC_Invoice_ID());
								if(!iv.isPosted())
								{
									Doc tdoc = Doc.get (m_ass, iv.get_Table_ID(),iv.getC_Invoice_ID(), null);
									String tmp = tdoc.post(false, true);   //  post no force/repost
									if(tmp!=null && tmp.length()>4)
										continue;
								}
								MInOut iout = new MInOut (getCtx() ,match.getM_InOutLine().getM_InOut_ID(),get_TrxName());
								if(!iout.isPosted())
								{
									Doc tdoc = Doc.get (m_ass, iout.get_Table_ID(),iout.getM_InOut_ID(), null);
									String tmp = tdoc.post(false, true);   //  post no force/repost
									if(tmp!=null && tmp.length()>4)
										continue;
								}
							}//faaguilar OFB 27/06/2012
							
							String error = doc.post(false, true);   //  post no force/repost
							ok = error == null;
							if(ok)
								count++;
						}
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, getName() + ": " + sql.toString(), e);
						ok = false;
					}
					if (!ok)
						countError++;
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql.toString(), e);
			}
			if (pstmt != null)
			{
				try
				{
					pstmt.close();
				}
				catch (Exception e)
				{
				}
			}
			//
			//
			
		
		
		return "Posted:"+count +"-Locked:"+countError;
		
	}	//	doIt

}	//	PostDocs
