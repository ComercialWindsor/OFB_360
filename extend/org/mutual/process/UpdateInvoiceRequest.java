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
package org.mutual.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MInvoice;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class UpdateInvoiceRequest extends SvrProcess
{
	private String			p_DocStatus = null;
	private String			p_DocStatusTo = null;
	private Timestamp p_DateInvoiced_From;
	
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
				
				if (name.equals("DocStatus"))
				{
					p_DocStatus = (String) para[i].getParameter();
					p_DocStatusTo = (String) para[i].getParameter_To();
				}
				else if (name.equals("DateInvoiced"))
					p_DateInvoiced_From = (Timestamp)para[i].getParameter();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
	}//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		 StringBuffer sql= new StringBuffer("select i.C_Invoice_ID ")
			.append("from C_Invoice i ")
			.append("where C_DocTypeTarget_ID = 1000066 and ")
			.append("docstatus IN ('"+p_DocStatus+"') ");
		 
		 String clientCheck = " AND i.AD_Client_ID=" + Env.getAD_Client_ID(getCtx());
		 sql.append (clientCheck);
		 	 
		 if(p_DateInvoiced_From != null)
			 sql.append(" and i.DateInvoiced <= '"+p_DateInvoiced_From+"'");
		 
	     PreparedStatement pstmt = null;
		 ResultSet rs = null;
		 try
		 {
			pstmt = DB.prepareStatement(sql.toString(), this.get_TrxName());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MInvoice inv = new MInvoice(getCtx(), rs.getInt(1), get_TrxName());
				if(p_DocStatusTo.compareToIgnoreCase("IP")==0)
				{
					inv.setDocStatus(p_DocStatusTo);		
					insertSAPTemp(inv.get_ID());
				}else if(p_DocStatusTo.compareToIgnoreCase("CO")==0)
				{
					inv.processIt(p_DocStatusTo);
					insertSAPTemp(inv.get_ID());
				}
				inv.save();
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
	   return "Actualizadas "+ created;
	}	//	doIt	
	public void insertSAPTemp (int Invoice_ID)
	{
		log.info("Invoice_ID=" + Invoice_ID);		
		/*int id_solpesap = DB.getSQLValue(get_TrxName(), "SELECT MAX(i_solpesap_ID+1) FROM i_solpesap ");
		if (id_solpesap == 0)
			id_solpesap = 1000000;
		id_solpesap = id_solpesap + 1;*/
		
		String sqlInsert = "INSERT INTO i_solpesap (ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,c_invreqnumber,doc_type_id,c_distribution, " +
				"org_value,c_bpartnervalue,c_bparnername,c_bparnername2,c_orderreference,referencetype,invoicereference, " +
				"c_dateinvreq,c_dateref,address1,address2,city,productvalue,m_productacct,qty,totallines,orderorg, " +
				"ceconamecust,ceconamemut,ad_username,isinmediate,c_creditmemoinfo,referencereason,currency, " +
				"headerdescription) SELECT ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,c_invreqnumber,doc_type_id,c_distribution, " +
				"org_value,c_bpartnervalue,c_bparnername,c_bparnername2,c_orderreference,referencetype,invoicereference, " +
				"c_dateinvreq,c_dateref,address1,address2,city,productvalue,m_productacct,qty,totallines,orderorg, " +
				"ceconamecust,ceconamemut,ad_username,isinmediate,c_creditmemoinfo,referencereason,currency, " +
				"headerdescription FROM RVOFB_solpesapview WHERE C_Invoice_ID = "+Invoice_ID;				
		DB.executeUpdate(sqlInsert, get_TrxName());
		log.info(sqlInsert);
	}
}
