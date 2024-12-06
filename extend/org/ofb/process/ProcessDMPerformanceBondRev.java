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
public class ProcessDMPerformanceBondRev extends SvrProcess
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
		
		X_DM_PerformanceBond_Proc pro = new X_DM_PerformanceBond_Proc(Env.getCtx(), Record_ID,get_TrxName());
		/*
		 * begin mfrojas
		 * Solo ciertos roles tienen permiso para realizar la reversa.
		 * Por ejemplo, un rol de tesorería no puede devolver un estado de alta visada a alta aprobada.
		 * 
		 */
		
		
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
				
				return "Solo el rol de tesorería puede pasar a la etapa previa ";
			}
			
			if(rolactual != 1000070  && rolactual != 1000000 && process.equals("SR"))
			{
				return "Solo el rol de requirente puede pasar a la etapa previa";
			}
			
			if(rolactual != 1000069 && rolactual != 1000000 && process.equals("AS"))
			{
				return "Solo el aprobador puede pasar a la etapa previa";
			}

			if(rolactual != 1000029 && rolactual != 1000000 && process.equals("AA"))
			{
				return "Solo el rol de finanzas puede pasar a la etapa previa";
			}

			if(rolactual != 1000048 && rolactual != 1000000 && process.equals("AV"))
			{
				return "Solo el rol de tesoreria puede pasar a la etapa previa";
			}
		}


		if (pro.getC_DocType().getDocBaseType().equals("PBB"))
		{
			if(rolactual != 1000070 && rolactual != 1000000 && process.equals("DR"))
			{
				return "Solo el rol de requirente puede pasar a la etapa previa";
			}

			if(rolactual != 1000069 && rolactual != 1000000 && process.equals("BS"))
			{
				return "Solo el rol de aprobador puede pasar a la etapa previa";
			}

			if(rolactual != 1000029 && rolactual != 1000000 && process.equals("BA"))
			{
				return "Solo el rol de finanzas puede pasar a la etapa previa";
			}

			if(rolactual != 1000048 && rolactual != 1000000 && process.equals("BV"))
			{
				return "Solo el rol de tesoreria puede pasar a la etapa previa";
			}
		}
			
		if (pro.getC_DocType().getDocBaseType().equals("PBC"))
		{
			if(rolactual != 1000070 && rolactual != 1000000 && process.equals("DR"))
			{
				return "Solo el rol de requirente puede pasar a la etapa previa";
			}

			if(rolactual != 1000069 && rolactual != 1000000 && process.equals("CS"))
			{
				return "Solo el rol de aprobador puede pasar a la etapa previa";
			}

			if(rolactual != 1000029 && rolactual != 1000000 && process.equals("CA"))
			{
				return "Solo el rol de finanzas puede pasar a la etapa previa";
			}

			if(rolactual != 1000048 && rolactual != 1000000 && process.equals("CV"))
			{
				return "Solo el rol de tesoreria puede pasar a la etapa previa";
			}

		}

		//end mfrojas

		if (pro.getC_DocType().getDocBaseType().equals("PBA"))
		{				
			if(pro.getDocStatus().equals("SR"))
			{
				pro.setDocStatus("DR");
			}					
			else if(pro.getDocStatus().equals("AS"))
			{
				pro.setDocStatus("SR");
			}			
			else if(pro.getDocStatus().equals("AA"))
			{
				pro.setDocStatus("AS");
			}			
			else if(pro.getDocStatus().equals("AV"))
			{
				pro.setDocStatus("AA");
			}
			pro.save();	
		}
		if (pro.getC_DocType().getDocBaseType().equals("PBB"))
		{
			X_DM_PerformanceBond bgarantia = new X_DM_PerformanceBond(getCtx(), pro.getDM_PerformanceBond_ID(), get_TrxName());
			
			if(pro.getDocStatus().equals("BS"))
			{
				pro.setDocStatus("DR");
				bgarantia.setDocStatus("DR");
			}
			else if(pro.getDocStatus().equals("BA"))
			{
				pro.setDocStatus("BS");
				bgarantia.setDocStatus("BS");
			}
			else if(pro.getDocStatus().equals("BV"))
			{
				pro.setDocStatus("BA");
				bgarantia.setDocStatus("BA");
			}
			pro.save();
			bgarantia.save();
		}
		if (pro.getC_DocType().getDocBaseType().equals("PBC"))
		{
			X_DM_PerformanceBond bgarantia = new X_DM_PerformanceBond(getCtx(), pro.getDM_PerformanceBond_ID(), get_TrxName());
			
			if(pro.getDocStatus().equals("CS"))
			{
				pro.setDocStatus("DR");
				bgarantia.setDocStatus("DR");
			}			
			else if(pro.getDocStatus().equals("CA"))
			{
				pro.setDocStatus("CS");
				bgarantia.setDocStatus("CS");
			}			
			else if(pro.getDocStatus().equals("CV"))
			{
				pro.setDocStatus("CA");
				bgarantia.setDocStatus("CA");
			}			
			else if(pro.getDocStatus().equals("DC"))
			{
				pro.setDocStatus("CV");
				bgarantia.setDocStatus("CV");
			}			
			pro.save();
			bgarantia.save();
		}
		if (pro.getC_DocType().getDocBaseType().equals("PBD"))
		{	
			if(pro.getDocStatus().equals("CI"))
			{
				pro.setDocStatus("DR");				
			}
			else if(pro.getDocStatus().equals("ES"))
			{
				pro.setDocStatus("CI");
			}			
			else if(pro.getDocStatus().equals("EA"))
			{
				pro.setDocStatus("ES");
			}			
			else if(pro.getDocStatus().equals("EV"))
			{
				pro.setDocStatus("EA");
			}			
			pro.save();	
		}
		
		return "Retornado";
	}	//	doIt	
}	//	CopyOrder
