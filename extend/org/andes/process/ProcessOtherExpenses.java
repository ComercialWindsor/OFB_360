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
package org.andes.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.*;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class ProcessOtherExpenses extends SvrProcess
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
		X_C_OtherExpenses other = new X_C_OtherExpenses(getCtx(), Record_ID, get_TrxName());
		
		if(P_DocAction.compareToIgnoreCase("CO") == 0)
		{
			if(other.get_ValueAsBoolean("override"))
			{
				other.setDocStatus("CO");
				other.set_CustomColumn("Processed", true);
			}
			else
			{
				//validación no se puede completar si no existe dispon
				//fecha actual
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				Timestamp dateNow = new Timestamp(cal.getTimeInMillis());
				
				
				if (other.getDocStatus().compareToIgnoreCase("DR") == 0)
				{
					if(other.getExpense().compareToIgnoreCase("RE") == 0)
					{
						//validacion de campos montos antes de completar
						String sql = "SELECT currency_20000+currency_10000+currency_5000+currency_2000+currency_1000+" +
								" currency_500+currency_100+currency_50+currency_10+currency_5+currency_1 " +
								" FROM C_OtherExpenses WHERE C_OtherExpenses_ID = ?";
						BigDecimal totallines = DB.getSQLValueBD(get_TrxName(), sql, Record_ID);
						if(totallines == null)
							totallines = Env.ZERO;
						if(totallines.compareTo(other.getGrandTotal()) != 0)
							throw new AdempiereException("Error: Total Solicitado no cuadra con total detalle");
						else
							other.setDocStatus("L1");
					}					
					else
						other.setDocStatus("L1");
				}		
				else if (other.getDocStatus().compareToIgnoreCase("L1") == 0)
					other.setDocStatus("L2");
				else if (other.getDocStatus().compareToIgnoreCase("L2") == 0)
				{	
					//obtenemos cuenta destino para validar tipo
					MBankAccount bAccountTo = new MBankAccount(getCtx(), other.getC_BankAccount_ID(),get_TrxName());
					int ID_DisponTo = 0;					
					//ahora todas las cuentas poseen dispon
					ID_DisponTo = DB.getSQLValue(get_TrxName(), "SELECT MAX(CD_Header_ID) FROM CD_Header " +
							" WHERE C_BankAccount_ID = "+bAccountTo.get_ID()+" AND DateTrx = ? ",dateNow);
					if(ID_DisponTo <= 0)
						throw new AdempiereException("Error: No existe disponibilidad para cuenta bancaria");
					//si no mando error
					other.setDocStatus("CO");
					other.set_CustomColumn("Processed", true);
					//luego de completar generamos el detalle de dispon
					if(ID_DisponTo > 0)
					{
						X_CD_Header dispon = new X_CD_Header(getCtx(), ID_DisponTo, get_TrxName());
						X_CD_Line newLine = new X_CD_Line(getCtx(), 0, get_TrxName());
						newLine.setCD_Header_ID(dispon.get_ID());
						newLine.setAD_Org_ID(dispon.getAD_Org_ID());
						newLine.setAmt(other.getGrandTotal());
						newLine.setDescription("Generado desde Otro Gasto "+other.getDocumentNo());
						newLine.setC_BankAccount_ID(other.getC_BankAccount_ID());
						newLine.setType("EG");
						newLine.set_CustomColumn("C_OtherExpenses_ID",other.get_ID());
						int id_charge = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Charge_ID) " +
								" FROM C_Charge cc " +
								" INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
								" WHERE cc.IsActive = 'Y' AND ct.value = 'TCRA'");
						if(id_charge > 0)
							newLine.set_CustomColumn("C_Charge_ID",id_charge);
						newLine.save();
					}
				}
			}			
		}
		if(P_DocAction.compareToIgnoreCase("VO") == 0)
		{
			if (other.getDocStatus().compareToIgnoreCase("L2") == 0)
				other.setDocStatus("L1");
			else if (other.getDocStatus().compareToIgnoreCase("L1") == 0)
				other.setDocStatus("DR");	
		}
		other.save();
		
		return "Procesado";
		
	}	//	doIt
}	//	CopyOrder

