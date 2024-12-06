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
package org.tsm.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MCash;
import org.compiere.model.MCashBook;
import org.compiere.model.MCashLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Replenishment Report
 *	
 *  @author Jorg Janke
 *  @version $Id: ReplenishReport.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 *  
 *  Carlos Ruiz globalqss - integrate bug fixing from Chris Farley
 *    [ 1619517 ] Replenish report fails when no records in m_storage
 */
public class GenerateCashJournalFF extends SvrProcess
{	
	/** Warehouse				*/
	private Timestamp	p_DateTrxFrom;
	private Timestamp	p_DateTrxTo;
	private int	p_Org_ID;
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
			else if (name.equals("DateTrx"))
			{
				p_DateTrxFrom = (Timestamp)para[i].getParameter();
				p_DateTrxTo = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("AD_Org_ID"))
			{
				p_Org_ID = para[i].getParameterAsInt();
			}
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
		//Ingreso de viaticos
		int cantLine = 0;
		int canthead = 0;
		
		//agregamos lineas de viaticos desde hojas de rura
		String sqlHR = "SELECT pm.M_Movement_ID,pm.C_BPartner_ID,pm.movementdate,pm.AD_Org_ID, TP_TollSpendingManual " +
				//end
				" FROM M_Movement pm " +
				" WHERE pm.M_Movement_ID NOT IN (SELECT cLine.M_Movement_ID FROM C_CashLine cLine " +
				" INNER JOIN C_Cash c ON (c.C_Cash_ID = cLine.C_Cash_ID) " +
				" WHERE c.DocStatus IN ('CO','DR','IP') AND cLine.M_Movement_ID IS NOT NULL) " +
				" AND pm.AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+
				//" AND pm.MovementDate BETWEEN ? AND ? " +
				//ininoles se revierte cambio de ultima version a pedido de TSM
				" AND COALESCE((select MAX(mml.tp_inicialhr) from m_movementline mml where mml.m_movement_id =  pm.M_Movement_ID), pm.MovementDate) BETWEEN ? AND ? "+
				" AND pm.DocStatus IN ('CO','CL','DR')";
		if(p_Org_ID > 0)
			sqlHR = sqlHR + " AND pm.AD_Org_ID = "+p_Org_ID;
		sqlHR = sqlHR + " ORDER BY C_BPartner_ID, pm.movementdate";
			
		PreparedStatement pstmtHR = null;
		ResultSet rsHR = null;
		try
		{
			//ininoles se revierte cambio de ultima version a pedido de TSM
			
			p_DateTrxTo.setHours(23);
			p_DateTrxTo.setMinutes(59);
			p_DateTrxTo.setSeconds(59);
			
			pstmtHR = DB.prepareStatement (sqlHR, get_TrxName());
			pstmtHR.setTimestamp(1, p_DateTrxFrom);
			pstmtHR.setTimestamp(2, p_DateTrxTo);
			rsHR = pstmtHR.executeQuery();		
			int ID_BPartner = 0;
			int ID_Org = 0;
			while (rsHR.next())
			{
				ID_BPartner = rsHR.getInt("C_BPartner_ID");
				ID_Org = rsHR.getInt("AD_Org_ID");
				//generamos diario para BP
				if(ID_BPartner > 0 && ID_Org > 0)
				{
					//buscamos libro para ese BP
					int ID_CashBook = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_CashBook_ID) FROM C_CashBook " +
							" WHERE IsActive = 'Y' AND TypeBook = 'FF' AND C_BPartner_ID = "+ID_BPartner);
					if(ID_CashBook > 0)
					{
						//buscamos diario abierto existente
						int ID_Cash = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(C_Cash_ID),0)" +
								//" FROM C_Cash WHERE DocStatus NOT IN ('CO','VO') " +
								" FROM C_Cash WHERE DocStatus IN ('DR') " +
								" AND C_CashBook_ID = "+ID_CashBook);
						if(ID_Cash > 0)
						{
							//generamos la linea del diario
							MCashLine cLine = new MCashLine(getCtx(), 0, get_TrxName());
							cLine.setC_Cash_ID(ID_Cash);
							cLine.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
							cLine.setDescription("generado automaticamente");
							cLine.setCashType("R");
							cLine.setAmount(rsHR.getBigDecimal("TP_TollSpendingManual"));
							cLine.set_CustomColumn("M_Movement_ID", rsHR.getInt("M_Movement_ID"));
							cLine.saveEx();							
							cantLine++;
						}
						//no existe diario abierto
						else
						{
							//se genera diario
							MCash cash = new MCash(new MCashBook(getCtx(), ID_CashBook, get_TrxName()),p_DateTrxTo);
							/*MCash cash = new MCash(getCtx(), 0, get_TrxName());
							cash.setC_CashBook_ID(ID_CashBook);*/
							cash.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
							cash.setName("Generado automaticamente. Fondo Fijo");
							cash.saveEx();							
							canthead++;
							//generamos la linea del diario
							MCashLine cLine = new MCashLine(getCtx(), 0, get_TrxName());
							cLine.setC_Cash_ID(cash.get_ID());
							cLine.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
							cLine.setDescription("generado automaticamente");
							cLine.setCashType("R");
							cLine.setAmount(rsHR.getBigDecimal("TP_TollSpendingManual"));
							cLine.set_CustomColumn("M_Movement_ID", rsHR.getInt("M_Movement_ID"));
							cLine.saveEx();							
							cantLine++;	
						}
					}	
				}
			}	
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage()+" SQL:"+sqlHR, e);
		}
		finally
		{
			rsHR.close ();	pstmtHR.close ();
			pstmtHR = null;	rsHR = null;
		}
		return "Se han generado "+canthead+" diario de fondo fijo. Se han agregado "+cantLine+" lineas";
	}	//	doIt
}	//	Replenish
