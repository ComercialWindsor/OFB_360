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

import org.compiere.model.X_T_BL_CONEXI_SESION;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 *  @author Italo Niñoles
 */
public class ProcessSendMailBLUMOS extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int 			Record_ID;
	private int 			Tabla_ID;
	//private String			p_laTabla;
	
	protected void prepare()
	{
		Record_ID=getRecord_ID();
		Tabla_ID = getTable_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		if(Tabla_ID == X_T_BL_CONEXI_SESION.Table_ID)
		{
			String sendMail = "SELECT p_bl_conexi_sesioncorreo("+Record_ID+")";
			//PreparedStatement pstmtSM = DB.prepareStatement (sendMail, get_TrxName());
			//pstmtSM.execute();
			String result = DB.getSQLValueString(get_TrxName(), sendMail);
			if(result != null && result.toLowerCase().compareTo("ok") == 0)
				DB.executeUpdate("UPDATE T_BL_CONEXI_SESION SET CORREO_ENVIADO = 'Y' WHERE T_BL_CONEXI_SESION_ID = "+Record_ID, get_TrxName());
			
		}
		return "Correo Enviado";
	}	//	doIt
}	//	Replenish

