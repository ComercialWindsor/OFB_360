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
package org.petroamerica.process;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	CopyFromJobStandar
 *	
 */
public class UpdateBPStatusAB extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */

	protected void prepare()
	{	

	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		
		//String sqlIDExcluidos= "1005526,1005618,1005201,1006159,1005932,1005825,1002311,1005389,1006093,1006232,1006231";
		String sqlIDExcluidos= "0";
		//PASO 1
		//actualizacion de bloqueo a N y dejar credito correo 
		String sqlP1 = "UPDATE C_BPartner bp SET SOCreditStatusOFB = 'O', IsAutoBlock = 'N',IsAutoBlock2 = 'N'" +
		" WHERE (IsAutoBlock = 'Y' OR IsAutoBlock2 = 'Y') AND IsActive = 'Y' AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
		//" WHERE IsActive = 'Y' AND IsAutoBlock = 'Y' AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
		DB.executeUpdate(sqlP1, get_TrxName());
		//PASO 2
		//actualizacion de bloqueo a Y bp en vista RVPETRO_Cliente_RiesgoPropio		
		String sqlP2 = "UPDATE C_BPartner bp SET SOCreditStatusOFB = 'H', IsAutoBlock = 'Y'" +
				" WHERE SOCreditStatusOFB = 'O' " +
				" AND bp.C_BPartner_ID IN (SELECT C_BPartner_ID FROM RVPETRO_Cliente_RiesgoPropio WHERE action::text = 'Y'" +
				" AND C_BPartner_ID NOT IN ("+sqlIDExcluidos+") )";
		DB.executeUpdate(sqlP2, get_TrxName());		
		//PASO 3
		//actualizacion de bloqueo a Y bp en vista RVPETRO_Cliente_Riesgo
		String sqlP3 = "UPDATE C_BPartner bp SET SOCreditStatusOFB = 'H', IsAutoBlock = 'Y'" +
				" WHERE SOCreditStatusOFB = 'O'" +
				" AND bp.C_BPartner_ID IN (SELECT C_BPartner_ID FROM RVPETRO_Cliente_Riesgoso WHERE action::text = 'Y'" +
				" AND C_BPartner_ID NOT IN ("+sqlIDExcluidos+") )"; 
		DB.executeUpdate(sqlP3, get_TrxName());
				
		//ININOLES solicitado 11-11-2019
		//PASO 4 se actualizan socios de negocio bloqueados por dias de mora		
		//actualizacion de bloqueo a N y dejar credito correo
		//ininoles 16-09-2020 ya no se ocupara campo day2 // 28-09-2020 mgarcia pide volver atras  
		
		/*String sqlP5 = "UPDATE C_BPartner bp SET SOCreditStatusOFB = 'O', IsAutoBlock2 = 'N'" +
		" WHERE IsActive = 'Y' AND IsAutoBlock2 = 'Y' AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
		DB.executeUpdate(sqlP5, get_TrxName());*/
		//actualizacion de bloqueo a Y bp en vista RVPETRO_Cliente_Riesgo
		String sqlP4 = "UPDATE C_BPartner bp SET SOCreditStatusOFB = 'H', IsAutoBlock2 = 'Y'" +
				" WHERE bp.C_BPartner_ID IN (SELECT C_BPartner_ID FROM rvpetro_cliente_diasmora " +
				"WHERE C_BPartner_ID NOT IN ("+sqlIDExcluidos+"))";
		DB.executeUpdate(sqlP4, get_TrxName());
		
		//se actualizan socios de negocios relacionados
		String sqlRelated = "UPDATE c_bpartner SET SOCreditStatusOFB = 'H',IsAutoBlock = 'Y' " +
				" WHERE c_bpartner_ID IN (SELECT c_bpartnerrelation_id FROM c_bp_relation re" +
				" INNER JOIN c_bpartner bp ON(re.c_bpartner_id = bp.c_bpartner_id) " +
				" WHERE SOCreditStatusOFB = 'H' AND re.IsActive='Y' " +
				" AND bp.C_BPartner_ID NOT IN ("+sqlIDExcluidos+"))";
		DB.executeUpdate(sqlRelated, get_TrxName());
		
		return "OK";
	}	//	doIt
}	//	Replenish
