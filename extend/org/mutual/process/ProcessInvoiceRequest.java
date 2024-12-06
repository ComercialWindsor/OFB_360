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

import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class ProcessInvoiceRequest extends SvrProcess
{
	private String			p_DocStatus = null;
	private int				p_C_Invoice_ID = 0; 
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
					p_DocStatus = (String) para[i].getParameter();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
			p_C_Invoice_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_C_Invoice_ID > 0)
		{
			MInvoice inv = new MInvoice(getCtx(),p_C_Invoice_ID, get_TrxName());
			if(p_DocStatus.compareToIgnoreCase("IP")==0)
			{
				inv.setDocStatus(p_DocStatus);		
				inv.save();
				commitEx();
				insertSAPTemp(inv.get_ID());				
			}else if(p_DocStatus.compareToIgnoreCase("CO")==0)
			{	
				inv.processIt(p_DocStatus);
				inv.save();
				commitEx();
				insertSAPTemp(inv.get_ID());
			}
			else if(p_DocStatus.compareToIgnoreCase("VO")==0)
			{	
				if(inv.getDocStatus().compareToIgnoreCase("CO") == 0)
				{
					inv.processIt(p_DocStatus);		
				}else
				{
					throw new AdempiereException("Documento no esta completo");
				}
				
			}
			inv.save();
		}
	   return "Procesado ";
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
				"headerdescription,desc_glosa) SELECT ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,c_invreqnumber,doc_type_id,c_distribution, " +
				"org_value,c_bpartnervalue,c_bparnername,c_bparnername2,c_orderreference,referencetype,invoicereference, " +
				"c_dateinvreq,c_dateref,address1,address2,city,productvalue,m_productacct,qty,totallines,orderorg, " +
				"ceconamecust,ceconamemut,ad_username,isinmediate,c_creditmemoinfo,referencereason,currency, " +
				"headerdescription,desc_glosa FROM RVOFB_solpesapview WHERE C_Invoice_ID = "+Invoice_ID;				
		DB.executeUpdate(sqlInsert, get_TrxName());
		log.info(sqlInsert);
	}
}
