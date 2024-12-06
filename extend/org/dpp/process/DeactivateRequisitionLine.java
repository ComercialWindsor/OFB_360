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
 *****************************************************************************/
package org.dpp.process;

import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MDocType;
import org.compiere.util.DB;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

/**
 * 	Create PO from Requisition 
 *	
 */
public class DeactivateRequisitionLine extends SvrProcess
{
	/** Tipo resolucion					*/
	private int			p_ID_DocType;	
	/**	Doc Date To			*/
	private Timestamp	p_DateDoc_To;
	/**	Doc Date From		*/
	private Timestamp	p_DateDoc_From;	
	
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
			else if (name.equals("C_DocType_ID"))
				p_ID_DocType = para[i].getParameterAsInt();			
			else if (name.equals("DateDoc"))
			{
				p_DateDoc_From = (Timestamp)para[i].getParameter();
				p_DateDoc_To = (Timestamp)para[i].getParameter_To();
			}			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare
	
	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		MDocType docType = new MDocType(getCtx(), p_ID_DocType, get_TrxName());
		
		int cant = 0;
		
		String sqlUpdate = "UPDATE M_RequisitionLine SET IsActive = 'N'" +
				" WHERE M_RequisitionLine_ID IN (";
		
		if (docType.getDocBaseType().equalsIgnoreCase("PRT"))
		{
			sqlUpdate = sqlUpdate + "SELECT M_RequisitionLine_ID FROM M_RequisitionLine mrl "+
					" INNER JOIN M_Requisition mr on (mrl.M_Requisition_ID = mr.M_Requisition_ID) "+
					" INNER JOIN C_DocType cdt on (mr.C_DocType_ID = cdt.C_DocType_ID) "+
					" WHERE cdt.DocBaseType = 'PRT'" +
					" AND mr.datedoc between ? and ?";
		}
		
		if (docType.getDocBaseType().equalsIgnoreCase("POR"))
		{
			sqlUpdate = sqlUpdate + "SELECT M_RequisitionLine_ID FROM M_RequisitionLine mrl "+
					" INNER JOIN M_Requisition mr on (mrl.M_Requisition_ID = mr.M_Requisition_ID) "+
					" INNER JOIN C_DocType cdt on (mr.C_DocType_ID = cdt.C_DocType_ID) "+
					" WHERE C_OrderLine_ID is null AND cdt.DocBaseType = 'POR'"+
					" AND mr.datedoc between ? and ?";
		}
		
		sqlUpdate = sqlUpdate + ")";

		try
		{	
			cant = DB.executeUpdateEx(sqlUpdate,new Object[]{p_DateDoc_From,p_DateDoc_To},get_TrxName());		
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}	//	doit
		
		this.commitEx();
		
		return "Lineas Actualizadas: "+cant;
	}
	
}	//	RequisitionPOCreate
