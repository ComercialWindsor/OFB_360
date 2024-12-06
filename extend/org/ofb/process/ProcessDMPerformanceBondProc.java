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

import java.sql.*;
import java.util.Calendar;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ProcessDMPerformanceBondProc.java,v 1.2 2014/12/16 00:51:01  $
 */
public class ProcessDMPerformanceBondProc extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int 			Record_ID;
	
	protected void prepare()
	{	
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		/*
		 * begin @mfrojas
		 * Validación de roles que pueden o no acceder al proceso.
		 * Definición de Roles:
		 * Tesorería: 1000048
		 * Requirente de boleta: 1000070
		 * Aprobador Boleta: 1000069
		 * Finanzas: 1000029
		 */
		
		X_DM_PerformanceBond_Proc pro = new X_DM_PerformanceBond_Proc(Env.getCtx(), Record_ID,get_TrxName());

		String process = (String)pro.get_Value("DocStatus");
		int rolactual = Env.getContextAsInt(Env.getCtx(), "#AD_Role_ID");
		
		log.config("Proceso: "+process+" rol actual: "+rolactual);
		
		/*
		 * Definición de Roles:
		 * Tesorería: 1000048
		 * Requirente de boleta: 1000070
		 * Aprobador Boleta: 1000069
		 * Finanzas: 1000029
		 * 
		 */
		if (pro.getC_DocType().getDocBaseType().equals("PBA"))
		{
		
			if(rolactual != 1000048 && rolactual != 1000000 && process.equals("DR"))
			{
				
				return "Solo el rol de tesorería puede pasar a la siguiente etapa";
			}
			
			if(rolactual != 1000070  && rolactual != 1000000 && process.equals("SR"))
			{
				return "Solo el rol de requirente puede pasar a la siguiente etapa";
			}
			
			if(rolactual != 1000069 && rolactual != 1000000 && process.equals("AS"))
			{
				return "Solo el aprobador puede pasar a la siguiente etapa";
			}

			if(rolactual != 1000029 && rolactual != 1000000 && process.equals("AA"))
			{
				return "Solo el rol de finanzas puede pasar a la siguiente etapa";
			}

			if(rolactual != 1000048 && rolactual != 1000000 && process.equals("AV"))
			{
				return "Solo el rol de tesoreria puede pasar a la siguiente etapa";
			}
		}


		if (pro.getC_DocType().getDocBaseType().equals("PBB"))
		{
			if(rolactual != 1000070 && rolactual != 1000000 && process.equals("DR"))
			{
				return "Solo el rol de requirente puede pasar a la siguiente etapa";
			}

			if(rolactual != 1000069 && rolactual != 1000000 && process.equals("BS"))
			{
				return "Solo el rol de aprobador puede pasar a la siguiente etapa";
			}

			if(rolactual != 1000029 && rolactual != 1000000 && process.equals("BA"))
			{
				return "Solo el rol de finanzas puede pasar a la siguiente etapa";
			}

			if(rolactual != 1000048 && rolactual != 1000000 && process.equals("BV"))
			{
				return "Solo el rol de tesoreria puede pasar a la siguiente etapa";
			}
		}
			
		if (pro.getC_DocType().getDocBaseType().equals("PBC"))
		{
			if(rolactual != 1000070 && rolactual != 1000000 && process.equals("DR"))
			{
				return "Solo el rol de requirente puede pasar a la siguiente etapa";
			}

			if(rolactual != 1000069 && rolactual != 1000000 && process.equals("CS"))
			{
				return "Solo el rol de aprobador puede pasar a la siguiente etapa";
			}

			if(rolactual != 1000029 && rolactual != 1000000 && process.equals("CA"))
			{
				return "Solo el rol de finanzas puede pasar a la siguiente etapa";
			}

			if(rolactual != 1000048 && rolactual != 1000000 && process.equals("CV"))
			{
				return "Solo el rol de tesoreria puede pasar a la siguiente etapa";
			}

		}

		//end mfrojas
		
		
		//X_DM_PerformanceBond_Proc pro = new X_DM_PerformanceBond_Proc(Env.getCtx(), Record_ID,get_TrxName());
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		Timestamp currentTimestamp = new Timestamp(now.getTime());
		
		
		if (pro.getC_DocType().getDocBaseType().equals("PBA"))
		{
						
			if(pro.getDocStatus().equals("DR"))
			{	
				pro.setDocStatus("SR");
				//se crea la boleta en el primer proceso
				X_DM_PerformanceBond bgarantia = new X_DM_PerformanceBond(getCtx(), 0, get_TrxName());
				bgarantia.setAD_Org_ID(pro.getAD_Org_ID());
				bgarantia.setDocumentNo(pro.get_ValueAsString("DocumentRefNo"));
				bgarantia.setDateTrx((Timestamp)pro.get_Value("DateTrxRef"));
				bgarantia.setDateEnd((Timestamp)pro.get_Value("DateAcctRef"));
				bgarantia.setC_BPartner_ID(pro.get_ValueAsInt("C_BPartner_ID"));
				bgarantia.setAmt(pro.getAmt());
				bgarantia.setC_Currency_ID(pro.getC_Currency_ID());
				bgarantia.setGuarantee(pro.get_ValueAsString("Guarantee"));
				bgarantia.set_CustomColumn("C_Bank_ID", pro.get_ValueAsInt("C_Bank_ID"));
				bgarantia.setBondType(pro.get_ValueAsString("BondType"));
				bgarantia.setBondCondition(pro.get_ValueAsString("BondCondition"));
				bgarantia.set_CustomColumn("AD_User_ID", pro.get_ValueAsInt("AD_User_ID"));
				bgarantia.set_CustomColumn("Status", "CSR");
				bgarantia.set_CustomColumn("Correlative", pro.get_ValueAsInt("Correlative"));
				bgarantia.setDescription(pro.getDescription());
				bgarantia.save();		
				pro.setDM_PerformanceBond_ID(bgarantia.get_ID());
				pro.save();
			}			
			
			else if(pro.getDocStatus().equals("SR"))
			{
				pro.setDocStatus("AS");
			}
			
			else if(pro.getDocStatus().equals("AS"))
			{
				pro.setDocStatus("AA");
			}
			
			else if(pro.getDocStatus().equals("AA"))
			{
				pro.setDocStatus("AV");
			}
			else if(pro.getDocStatus().equals("AV"))
			{
				//proceso completar					
				X_DM_PerformanceBond bgarantia = new X_DM_PerformanceBond(getCtx(), pro.getDM_PerformanceBond_ID(), get_TrxName());
				bgarantia.set_CustomColumn("Status", "ENC");
				bgarantia.set_CustomColumn("DateDoc", currentTimestamp);
				String infoRut = "";
				String infoName = "";
				infoRut = (String)pro.get_Value("InfoRut");
				infoName = (String)pro.get_Value("InfoName");
				if (infoRut != null)
					bgarantia.set_CustomColumn("InfoRut", infoRut);
				if ( infoName != null)
					bgarantia.set_CustomColumn("InfoName", infoName);
				bgarantia.save();
				try
				{
					if (pro.get_ValueAsString("Type").equals("AR"))
					{
						if (pro.get_ValueAsInt("DM_PerformanceBondRef_ID") > 0)
						{
							X_DM_PerformanceBond bgarantiaAR = new X_DM_PerformanceBond(getCtx(), pro.get_ValueAsInt("DM_PerformanceBondRef_ID"), get_TrxName());
							bgarantiaAR.set_CustomColumn("Status", "REM");
							bgarantiaAR.save();						
						}
					}
				}catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
				pro.setDocStatus("CO");
				pro.setProcessed(true);			
				pro.setDateAcct(currentTimestamp);
			}
			pro.save();	
		}
		if (pro.getC_DocType().getDocBaseType().equals("PBB"))
		{
			X_DM_PerformanceBond bgarantia = new X_DM_PerformanceBond(getCtx(), pro.getDM_PerformanceBond_ID(), get_TrxName());
			
			if(pro.getDocStatus().equals("DR"))
			{
				pro.setDocStatus("BS");
				bgarantia.setDocStatus("BS");				
			}
			else if(pro.getDocStatus().equals("BS"))
			{
				pro.setDocStatus("BA");
				bgarantia.setDocStatus("BA");
			}
			else if(pro.getDocStatus().equals("BA"))
			{
				pro.setDocStatus("BV");
				bgarantia.setDocStatus("BV");
			}
			else if(pro.getDocStatus().equals("BV"))
			{	
				pro.setDocStatus("CO");
				bgarantia.setDocStatus("CO");
				pro.setProcessed(true);				
				bgarantia.set_CustomColumn("Status", "BVI");
				bgarantia.save();
				pro.setDateAcct(currentTimestamp);				
			}
			pro.save();
			bgarantia.save();
		}		
		if (pro.getC_DocType().getDocBaseType().equals("PBV"))
		{
			String sqlcantDetail = "SELECT COUNT(1) FROM DM_PerformanceBondDet_Proc WHERE DM_PerformanceBond_Proc_ID = "+pro.get_ID();
			int cant = DB.getSQLValue(get_TrxName(), sqlcantDetail);			
			if (cant > 0)
			{
				if(pro.getDocStatus().equals("DR"))
				{	
					pro.setDocStatus("CO");
					pro.setProcessed(true);
					//X_DM_PerformanceBond bgarantia = new X_DM_PerformanceBond(getCtx(), pro.getDM_PerformanceBond_ID(), get_TrxName());
					//bgarantia.set_CustomColumn("Status", "DOV");
					//bgarantia.save();
					pro.setDateAcct((Timestamp)pro.get_Value("DateWithdrawal"));
					DB.executeUpdate("UPDATE DM_PerformanceBond SET Status = 'DOV' WHERE DM_PerformanceBond_ID IN " +
							"(SELECT DM_PerformanceBond_ID FROM DM_PerformanceBondDet_Proc WHERE DM_PerformanceBond_Proc_ID = "+pro.get_ID()+")",get_TrxName());				
				}
				pro.save();	
			}
			else
				return "Error: Sin Detalle. No procesado";
		}
		if (pro.getC_DocType().getDocBaseType().equals("PBC"))
		{
			X_DM_PerformanceBond bgarantia = new X_DM_PerformanceBond(getCtx(), pro.getDM_PerformanceBond_ID(), get_TrxName());
			
			if(pro.getDocStatus().equals("DR"))
			{
				pro.setDocStatus("CS");
				bgarantia.setDocStatus("CS");
			}			
			else if(pro.getDocStatus().equals("CS"))
			{
				pro.setDocStatus("CA");
				bgarantia.setDocStatus("CA");
			}			
			else if(pro.getDocStatus().equals("CA"))
			{
				pro.setDocStatus("CV");
				bgarantia.setDocStatus("CV");
			}			
			else if(pro.getDocStatus().equals("CV"))
			{
				pro.setDocStatus("DC");
				bgarantia.setDocStatus("DC");
				bgarantia.set_CustomColumn("Status", "DPC");				
			}
			if(pro.getDocStatus().equals("DC"))
			{	
				pro.setDocStatus("CO");
				bgarantia.setDocStatus("CO");
				pro.setProcessed(true);				
				bgarantia.set_CustomColumn("Status", "DCO");				
				pro.setDateAcct(currentTimestamp);
			}
			pro.save();
			bgarantia.save();
		}
		if (pro.getC_DocType().getDocBaseType().equals("PBD"))
		{	
			if(pro.getDocStatus().equals("DR"))
			{
				pro.setDocStatus("CI");				
			}
			else if(pro.getDocStatus().equals("CI"))
			{
				pro.setDocStatus("ES");
			}			
			else if(pro.getDocStatus().equals("ES"))
			{
				pro.setDocStatus("EA");
			}			
			else if(pro.getDocStatus().equals("EA"))
			{
				pro.setDocStatus("EV");
			}
			else if(pro.getDocStatus().equals("EV"))
			{
				X_DM_PerformanceBond bgarantia = new X_DM_PerformanceBond(getCtx(), pro.getDM_PerformanceBond_ID(), get_TrxName());
				Timestamp newDate = (Timestamp)pro.get_Value("DateNew");
				pro.set_CustomColumn("DateOld",bgarantia.getDateEnd());
				bgarantia.setDateEnd(newDate);				
				//proceso completar
				pro.setDocStatus("CO");
				pro.setProcessed(true);
				pro.setDateAcct(currentTimestamp);
				bgarantia.save();
			}
			pro.save();	
		}
		
		return "Procesado";
	}	//	doIt	
}	//	CopyOrder

