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

import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	
 *	
 */
public class UpdateCreditDB extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_BPartner_ID;

	protected void prepare()
	{	
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("C_BPartner_ID"))
				p_BPartner_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		if(p_BPartner_ID > 0)
		{
			MBPartner part = new MBPartner(getCtx(), p_BPartner_ID, get_TrxName());
			//deja todo en 0
			DB.executeUpdate("update c_bpartner set totalopenbalanceofb=coalesce(owncreditlimit,0)+coalesce(externalcreditlimit,0),so_creditusedofb=0" +
					" where C_Bpartner_ID =  ?",p_BPartner_ID,get_TrxName());
			
			if(part.isCustomer())
			{				
				//actualiza credito usado
				DB.executeUpdate("update c_bpartner cbp set so_creditusedofb=Coalesce(( SELECT sum(t.dueamt) AS sum" +
						" FROM ( SELECT coalesce(dc.dueamt,0) AS dueamt FROM rvofb_dias_calle_v2 dc" +
						" WHERE dc.c_bpartner_id = cbp.c_bpartner_id and (dc.dueamt not between -5 and 5 or  dc.option=1) and dc.option2=1) t),0) + coalesce((select sum(co.grandtotal) from c_order co where co.c_bpartner_id=cbp.c_bpartner_id and co.c_order_id in (select c_order_id from rvofb_guias_pendientes)),0)" +
						" where cbp.C_Bpartner_ID =  ?",p_BPartner_ID,get_TrxName());
				
				//calcula credito disponible restando el credito usado calculado antes
				DB.executeUpdate("update c_bpartner set totalopenbalanceofb=(coalesce(owncreditlimit,0)+coalesce(externalcreditlimit,0))-so_creditusedofb" +
						" where C_Bpartner_ID =  ?",p_BPartner_ID,get_TrxName());
				
				//se agrega codigo que actualiza el estado del BP
				//PASO 1
				//actualizacion de bloqueo a N y dejar credito correo 
				String sqlP1 = "UPDATE C_BPartner bp SET SOCreditStatusOFB = 'O', IsAutoBlock = 'N',IsAutoBlock2 = 'N'" +
				" WHERE C_Bpartner_ID = "+p_BPartner_ID;
				DB.executeUpdate(sqlP1, get_TrxName());
				//PASO 2
				//actualizacion de bloqueo a Y bp en vista RVPETRO_Cliente_RiesgoPropio		
				String sqlP2 = "UPDATE C_BPartner bp SET SOCreditStatusOFB = 'H', IsAutoBlock = 'Y'" +
						" WHERE SOCreditStatusOFB = 'O' " +
						" AND bp.C_BPartner_ID IN (SELECT C_BPartner_ID FROM RVPETRO_Cliente_RiesgoPropio WHERE action::text = 'Y'" +
						" AND C_BPartner_ID ="+p_BPartner_ID+" )";
				DB.executeUpdate(sqlP2, get_TrxName());		
				//PASO 3
				//actualizacion de bloqueo a Y bp en vista RVPETRO_Cliente_Riesgo
				String sqlP3 = "UPDATE C_BPartner bp SET SOCreditStatusOFB = 'H', IsAutoBlock = 'Y'" +
						" WHERE SOCreditStatusOFB = 'O'" +
						" AND bp.C_BPartner_ID IN (SELECT C_BPartner_ID FROM RVPETRO_Cliente_Riesgoso WHERE action::text = 'Y'" +
						" AND C_BPartner_ID ="+p_BPartner_ID+")";
				DB.executeUpdate(sqlP3, get_TrxName());
						
				//actualizacion de bloqueo a Y bp en vista RVPETRO_Cliente_Riesgo
				String sqlP4 = "UPDATE C_BPartner bp SET SOCreditStatusOFB = 'H', IsAutoBlock2 = 'Y'" +
						" WHERE bp.C_BPartner_ID IN (SELECT C_BPartner_ID FROM rvpetro_cliente_diasmora " +
						"WHERE C_BPartner_ID ="+p_BPartner_ID+")";
				DB.executeUpdate(sqlP4, get_TrxName());
				
				//se actualizan socios de negocios relacionados
				//String sqlIDExcluidos= "1005526,1005618,1005201,1006159,1005932,1005825,1002311,1005389,1006093,1006232,1006231";
				String sqlIDExcluidos= "0";
				String sqlRelated = "UPDATE c_bpartner SET SOCreditStatusOFB = 'H',IsAutoBlock = 'Y' " +
						" WHERE c_bpartner_ID IN (SELECT c_bpartnerrelation_id FROM c_bp_relation re" +
						" INNER JOIN c_bpartner bp ON(re.c_bpartner_id = bp.c_bpartner_id) " +
						" WHERE SOCreditStatusOFB = 'H' AND re.IsActive='Y'" +
						" AND bp.C_BPartner_ID NOT IN ("+sqlIDExcluidos+"))";
				DB.executeUpdate(sqlRelated, get_TrxName());
			}	
		}
		else
		{
			//deja todo en 0
			DB.executeUpdate("update c_bpartner set totalopenbalanceofb=coalesce(owncreditlimit,0)+coalesce(externalcreditlimit,0),so_creditusedofb=0 ", get_TrxName());
			
			//actualiza credito usado
			DB.executeUpdate("update c_bpartner cbp set so_creditusedofb=Coalesce(( SELECT sum(t.dueamt) AS sum" +
					" FROM ( SELECT coalesce(dc.dueamt,0) AS dueamt" +
					" FROM rvofb_dias_calle_v2 dc" +
					" WHERE dc.c_bpartner_id = cbp.c_bpartner_id and (dc.dueamt not between -5 and 5 or  dc.option=1) and dc.option2=1) t),0) + coalesce((select sum(co.grandtotal) from c_order co where co.c_bpartner_id=cbp.c_bpartner_id and co.c_order_id in (select c_order_id from rvofb_guias_pendientes)),0)" +
					" where cbp.iscustomer='Y'", get_TrxName());
			
			//calcula credito disponible restando el credito usado calculado antes
			DB.executeUpdate("update c_bpartner set totalopenbalanceofb=(coalesce(owncreditlimit,0)+coalesce(externalcreditlimit,0))-so_creditusedofb" +
					" where iscustomer='Y'",get_TrxName());
		}
		return "Procesado";
	}	//	doIt
}	//	Replenish
