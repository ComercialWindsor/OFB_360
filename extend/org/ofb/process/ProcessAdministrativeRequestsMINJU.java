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

import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.logging.*;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class ProcessAdministrativeRequestsMINJU extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private String P_DocAction;
	/*OT ID*/
	private int 			Record_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DocAction"))
				P_DocAction = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		X_RH_AdministrativeRequests doc=new X_RH_AdministrativeRequests(Env.getCtx(), Record_ID,get_TrxName());
		if(P_DocAction.equals("IP") && !doc.isProcessed())
		{
			doc.setProcessed(true);
			doc.setDocStatus("IP");
			doc.save();
			return "Procesado";
		}

		if(P_DocAction.equals("CO") && !doc.isProcessed())
		{	
			Timestamp dateRequestCalc = doc.getdateendrequest();
			if(doc.getRequestType().equals("TAH"))//accion para abonar horas
			{
				X_RH_HoursAvailable ha = new X_RH_HoursAvailable(getCtx(), 0, get_TrxName());
				ha.setAD_Org_ID(doc.getAD_Org_ID());
				ha.setC_BPartner_ID(doc.getC_BPartner_ID());
				ha.sethours((BigDecimal)doc.get_Value("hours"));
				ha.setDateExpiration(doc.getdateendrequest());
				ha.setHoursAvailable((BigDecimal)doc.get_Value("hours"));
				ha.setHoursUsed(Env.ZERO);
				ha.setRH_AdministrativeRequests_ID(doc.get_ID());
				ha.save();				
				//calculamos fecha final menos 2 años				
				Calendar startCal = Calendar.getInstance();		 
				startCal.setTime(dateRequestCalc);
				startCal.add(Calendar.YEAR, -2);
				dateRequestCalc.setTime(startCal.getTimeInMillis());
			}
			//end ininoles
			if(doc.getRequestType().equals("TMH"))//accion para restar horas ininoles
			{
				BigDecimal cantH = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(hoursavailable) " +
						" FROM RH_HoursAvailable WHERE HoursAvailable > 0 AND C_BPartner_ID = "+doc.getC_BPartner_ID()+
						" AND DateExpiration >= ? ",doc.getdateendrequest());
				
				BigDecimal hoursToDiscount = (BigDecimal)doc.get_Value("hours");
				if(hoursToDiscount.compareTo(cantH) > 0)
					throw new AdempiereException("No Existen Horas Disponibles Suficientes. Horas Disponibles: "+cantH);
				
				//int hoursToDiscount = doc.get_ValueAsInt("hours");
				String sql = "SELECT hoursavailable,dateexpiration, RH_HoursAvailable_ID " +
						" FROM RH_HoursAvailable " +
						" WHERE HoursAvailable > 0 AND C_BPartner_ID = ? " +
						" AND DateExpiration >= ? ORDER BY DateExpiration";
				PreparedStatement pstmt = null;
				
				try
				{	
					pstmt = DB.prepareStatement(sql, get_TrxName());
					pstmt.setInt(1, doc.getC_BPartner_ID());
					pstmt.setTimestamp(2, doc.getdateendrequest());
					ResultSet rs = pstmt.executeQuery();
					while(rs.next() && hoursToDiscount.compareTo(Env.ZERO) > 0)
					{
						X_RH_HoursAvailable haTo = new X_RH_HoursAvailable(getCtx(), rs.getInt("RH_HoursAvailable_ID"), get_TrxName());
						if(rs.getBigDecimal("hoursavailable").compareTo(hoursToDiscount) >= 0)
						{
							haTo.setHoursUsed(haTo.getHoursUsed().add(hoursToDiscount));
							haTo.setHoursAvailable(haTo.gethours().subtract(haTo.getHoursUsed()));							
							//actualizar tabla con cantidad hoursToDiscount
							X_RH_HoursUsedDetail uDet = new X_RH_HoursUsedDetail(getCtx(),0,get_TrxName());
							uDet.setAD_Org_ID(doc.getAD_Org_ID());
							uDet.setIsActive(true);
							uDet.setRH_AdministrativeRequests_ID(doc.get_ID());
							uDet.sethours(hoursToDiscount);
							uDet.setRH_HoursAvailable_ID(haTo.get_ID());
							uDet.setDescription("Generado Automaticamente");
							uDet.save();
							hoursToDiscount = Env.ZERO;
						}	
						else
						{	
							haTo.setHoursUsed(haTo.getHoursUsed().add(rs.getBigDecimal("hoursavailable")));
							//actualizar tabla nueva con cantidad rs.getInt("hoursavailable")
							X_RH_HoursUsedDetail uDet = new X_RH_HoursUsedDetail(getCtx(),0,get_TrxName());
							uDet.setAD_Org_ID(doc.getAD_Org_ID());
							uDet.setIsActive(true);
							uDet.setRH_AdministrativeRequests_ID(doc.get_ID());
							uDet.sethours(rs.getBigDecimal("hoursavailable"));
							uDet.setRH_HoursAvailable_ID(haTo.get_ID());
							uDet.setDescription("Generado Automaticamente");
							uDet.save();
							//end
							haTo.setHoursAvailable(haTo.gethours().subtract(haTo.getHoursUsed()));
							hoursToDiscount = hoursToDiscount.subtract(rs.getBigDecimal("hoursavailable"));
						}
						haTo.save();
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
				dateRequestCalc = doc.getdateendrequest();
			}
			//end ininoles		
			//ininoles actualizacion de nuevo campo con horas despues de completar
			commitEx();
			if(dateRequestCalc == null)
				dateRequestCalc = doc.getdateendrequest();
			/*int afterQty = DB.getSQLValue(get_TrxName(), "SELECT SUM(hoursavailable) " +
					" FROM RH_HoursAvailable WHERE C_BPartner_ID = "+doc.getC_BPartner_ID()+
					" AND DateExpiration >= ? ",dateRequestCalc);*/
			BigDecimal afterQty = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(hoursavailable) " +
			" FROM RH_HoursAvailable WHERE C_BPartner_ID = "+doc.getC_BPartner_ID()+
			" AND DateExpiration >= ? ",dateRequestCalc);
			if(afterQty != null && afterQty.compareTo(Env.ZERO) > 0)
				doc.set_CustomColumn("HoursAvailable", afterQty);
			doc.setProcessed(true);
			doc.setDocStatus("CO");
			doc.save();
			return "Completado";
		}		
		if(P_DocAction.equals("VO"))
		{
			doc.setProcessed(true);
			doc.setDocStatus("VO");
			doc.save();
			return "Anulado";
		}
		return "No es posible Cumplir la Accion ";
		
	}	//	doIt


	
}	//	CopyOrder
