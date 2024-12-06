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

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.ofb.utils.DateUtils;
/**
 *  @author Italo Niñoles
 */
public class ProcessCostosFlashDiario extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int		p_Order_ID;
	//private int		p_Product_ID;
	private int		p_Product_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Order_ID"))
				p_Order_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_ID"))
				p_Product_ID = para[i].getParameterAsInt();
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
		//se calcula fecha inicio
		Timestamp la_fecha = DateUtils.now(); 
		if(la_fecha.getMonth() == 0)
		{
			la_fecha.setMonth(11);
			la_fecha.setYear(la_fecha.getYear()-1);
		}
		else
			la_fecha.setMonth(la_fecha.getMonth()-1);
		la_fecha.setDate(1);
		//delete
		String sqlDelete = "DELETE from t_bl_costos_cil coc where coc.c_invoiceline_id IN " +
			" (select c_invoiceline_id from c_invoiceline cil" +
			" inner join c_invoice ci ON (cil.c_invoice_id=ci.c_invoice_id)" +
			" where ci.dateacct >=?)";
		log.config("sqlDelete = "+sqlDelete);
		PreparedStatement pstmtD = DB.prepareStatement (sqlDelete, get_TrxName());
		pstmtD.setTimestamp(1, la_fecha);
		pstmtD.execute();
		
		//insert
		//sql de actualizacion
		String sqlInsert = "Insert Into T_BL_Costos_CIL " +
				" select cil.c_invoiceline_id, damecostounitario(cil.c_invoiceline_id) AS COSTOUNITARIO, now()" +
				" from c_invoiceline cil" +
				" inner join c_invoice ci on (cil.c_invoice_id=ci.c_invoice_id)" +
				" inner join m_product p on (cil.m_product_id=p.m_product_id)" +
				" where ci.dateacct>=? and (ci.docstatus='CO' or ci.docstatus='CL')and ci.issotrx='Y'";
		log.config("sqlInsert = "+sqlInsert);
		PreparedStatement pstmtI = DB.prepareStatement (sqlInsert, get_TrxName());
		pstmtI.setTimestamp(1, la_fecha);
		pstmtI.execute();
		
		// REPASO CON UPDATE DE POSIBLES COSTOS EN CERO
		String sqlCeros = "UPDATE T_BL_COSTOS_CIL SET COSTOUNITARIO=damecostounitario(c_invoiceline_id)" +
				" where c_invoiceline_id in (SELECT CC.C_INVOICELINE_ID" +
				" FROM T_BL_COSTOS_CIL CC" +
				" INNER JOIN C_INVOICELINE CIL ON (cc.c_invoiceline_id=cil.c_invoiceline_id)" +
				" INNER  JOIN C_INVOICE CI ON (cil.c_invoice_id=CI.C_INVOICE_ID)" +
				" WHERE (CI.DOCSTATUS='CO' OR CI.DOCSTATUS='CL')" +
				" AND cc.costounitario=0 AND CI.DATEACCT>=?)";
		log.config("sqlCeros = "+sqlCeros);
		PreparedStatement pstmtCeros = DB.prepareStatement (sqlCeros, get_TrxName());
		//se calcula fecha '10/09/2016'
		Timestamp la_fechaCeros = DateUtils.now(); 
		la_fechaCeros.setDate(1);
		la_fechaCeros.setMonth(8);
		la_fechaCeros.setYear(116);
		pstmtCeros.setTimestamp(1, la_fechaCeros);
		pstmtCeros.execute();

		return "Procesado";
	}	//	doIt
}	