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

import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author faaguilar OFB
 *  @version $Id: GenerateCompromiseInvoice.java,v 1.2 2011/06/12 00:51:01  $
 */

public class GenerateCompromiseInvoice extends SvrProcess 
{
	private String documentno;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DocumentNo"))
				documentno = (String)para[i].getParameter();
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
		MInvoicePaySchedule ps = new MInvoicePaySchedule(Env.getCtx(), this.getRecord_ID(), this.get_TrxName());
		
		
		MInvoice oldinv = new MInvoice(Env.getCtx(), ps.getC_Invoice_ID(), this.get_TrxName());
		MInvoice newinv = new MInvoice(Env.getCtx(), 0, this.get_TrxName());
		
		PO.copyValues (oldinv, newinv);
		newinv.setAD_Org_ID(oldinv.getAD_Org_ID());
		newinv.setDescription("Factura por Cuota de Compromiso "+oldinv.getDocumentNo());
		newinv.set_CustomColumn ("C_Invoice_ID", 0);
		newinv.setDocumentNo(documentno);
		newinv.setDocStatus (MInvoice.DOCSTATUS_Drafted);		//	Draft
		newinv.setDocAction(MInvoice.DOCACTION_Complete);
		newinv.setGrandTotal(Env.ZERO);
		newinv.setTotalLines(Env.ZERO);
		newinv.setC_DocType_ID(0);
		newinv.setPosted (false);
		newinv.setProcessed (false);
		newinv.setDateInvoiced(new Timestamp(System.currentTimeMillis()));
		newinv.setDateAcct(new Timestamp(System.currentTimeMillis()));
		newinv.save();
		
		String sql3 = "SELECT C_Charge_ID FROM C_Charge WHERE upper(TipoCargo)='TC11'  and isactive='Y' and AD_client_ID=" +oldinv.getAD_Client_ID();
		int C_Charge_ID= DB.getSQLValue("C_Charge",sql3); 
		MInvoiceLine line = new MInvoiceLine(newinv);
		line.setC_Charge_ID(C_Charge_ID);
		line.setPrice(ps.getDueAmt());
		line.setQty(Env.ONE);
		line.setLineNetAmt();
		line.save();	
		//debe ser completado manualmnete		
		//newinv.processIt(MInvoice.D);
		//newinv.setProcessed(true);
		//newinv.setIsPaid(true);
		newinv.setDocStatus (MInvoice.DOCSTATUS_InProgress);
		newinv.save();
		
		
		ps.set_CustomColumn("Ref_Invoice_ID", newinv.getC_Invoice_ID());
		ps.save();
		
		return "Factura Creada";
		
	}
		
}
