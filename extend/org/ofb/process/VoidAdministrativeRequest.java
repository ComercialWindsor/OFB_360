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

import java.util.Properties;

import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.process.SvrProcess;

 
/**
 *	Generate XML consolidated from Invoice 
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: VoidAdministrativeRequest.java,v 1.2 19/05/2011 $
 */
public class VoidAdministrativeRequest extends SvrProcess
{
	/** Properties						*/
	private Properties 		m_ctx;	
	private int p_RH_AdministrativeR_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_RH_AdministrativeR_ID=getRecord_ID();
		m_ctx = Env.getCtx();
	}	//	prepare
	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		X_RH_AdministrativeRequests admR=new X_RH_AdministrativeRequests(m_ctx,p_RH_AdministrativeR_ID,get_TrxName());
		
		admR.setProcessed(true);
		admR.setDocStatus("QQ");
		admR.save();
		
		return "Solicitud No Aprobada";
	}	//	doIt
	
}	//	InvoiceCreateInOut
