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
 *****************************************************************************/
package org.tsm.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MCash;
import org.compiere.model.MClient;
import org.compiere.model.MMovement;
import org.compiere.model.MRole;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_DM_Document;
import org.compiere.model.X_TP_Refund;
import org.compiere.model.X_TP_RefundHeader;
import org.compiere.model.X_TP_RefundLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company TSM
 *
 *  @author Italo Niñoles
 */
public class ModTSMRefund implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTSMRefund ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTSMRefund.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;
	

	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing global validator: "+this.toString());
		}

		//	Tables to be monitored
		engine.addModelChange(X_TP_Refund.Table_Name, this);
		engine.addModelChange(X_TP_RefundHeader.Table_Name, this);
		engine.addModelChange(X_TP_RefundLine.Table_Name, this);
		//	Documents to be monitored
		
		


	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID()==X_TP_RefundHeader.Table_ID
				&& po.is_ValueChanged("DocStatus"))  
		{
			X_TP_RefundHeader refundHead = (X_TP_RefundHeader) po;
			if(refundHead.getDocStatus().compareTo("CO") == 0)
			{
				//solo se genera resolucion si no tiene marcado ticket
				if(!refundHead.get_ValueAsBoolean("IsSalary"))
				{
					BigDecimal amt =  DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM TP_RefundLine rl" +
							" INNER JOIN TP_Refund rf ON (rl.TP_Refund_ID = rf.TP_Refund_ID) " +
							" WHERE rl.IsActive = 'Y' AND rf.TP_RefundHeader_ID ="+refundHead.get_ID());
					if(amt != null && amt.compareTo(Env.ZERO) > 0)
					{
						//generacion de resolucion
						X_DM_Document doc = new X_DM_Document(po.getCtx(), 0, po.get_TrxName());
						doc.setAD_Org_ID(refundHead.getAD_Org_ID());
						doc.set_CustomColumn("C_BPartner_ID",refundHead.getC_BPartner_ID());
						doc.setAmt(amt);
						doc.setDescription("Generado desde viatico "+refundHead.getDocumentNo());
						String desc = DB.getSQLValueString(po.get_TrxName(), "SELECT rlt.name FROM AD_Ref_List rl " +
								" INNER JOIN AD_Ref_List_Trl rlt ON (rl.AD_Ref_List_ID = rlt.AD_Ref_List_ID AND AD_Language='es_CL') " +
								" WHERE AD_Reference_ID=1000092 AND rl.VALUE = '"+refundHead.getType()+"'");
						if(desc != null && desc.trim().length() > 3)
							doc.setDescription("Tipo: "+desc);
						doc.set_CustomColumn("TP_RefundHeader_ID", refundHead.get_ID());
						doc.setDateTrx(refundHead.getDateEnd());
						doc.setDocStatus("CO");
						doc.setProcessed(true);
						doc.saveEx();
						//refund.set_CustomColumn("DM_Document_ID",doc.get_ID());
					}
				}
			}			
		}		
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==X_TP_RefundLine.Table_ID)  
		{
			X_TP_RefundLine rLine = (X_TP_RefundLine) po;
			X_TP_RefundHeader head = new X_TP_RefundHeader(po.getCtx(), rLine.getTP_Refund().getTP_RefundHeader_ID(), po.get_TrxName());
			//se agregan viaticos 
			if(head.getType().compareTo("01") == 0)
			{
				if(rLine.getM_Movement_ID() > 0)
				{
					MMovement mov = new MMovement(po.getCtx(), rLine.getM_Movement_ID(), po.get_TrxName());
					//validacion chofer
					if(mov.getC_BPartner_ID() != rLine.getTP_Refund().getC_BPartner_ID())
						return "ERROR: Conductor HR no coincide con viatico";
					//validacion fecha
					//ininoles la comparacion se hace con los valores de las fecha de las lineas 
					Timestamp MaxDate = DB.getSQLValueTS(po.get_TrxName(), "SELECT MAX(TP_InicialHR) FROM M_MovementLine WHERE M_Movement_ID = "+rLine.getM_Movement_ID());
					Timestamp MinDate = DB.getSQLValueTS(po.get_TrxName(), "SELECT MIN(TP_FinalHR) FROM M_MovementLine WHERE M_Movement_ID = "+rLine.getM_Movement_ID());
					MinDate.setHours(0);
					MinDate.setMinutes(0);
					MinDate.setSeconds(0);
					if(rLine.getDateTrx().compareTo(MaxDate) > 0 
							||rLine.getDateTrx().compareTo(MinDate) < 0)
						throw new AdempiereException("ERROR: Fecha de HR no coincide con fecha de viatico");
				}
			}
			if(head.getType().compareTo("02") == 0)
			{
				if(rLine.getM_Movement_ID() > 0)
				{
					MMovement mov = new MMovement(po.getCtx(), rLine.getM_Movement_ID(), po.get_TrxName());
					//validacion chofer
					if(mov.getC_BPartner_ID() != rLine.getTP_Refund().getC_BPartner_ID())
						return "ERROR: Conductor HR no coincide con viatico";
					//validacion fecha
					//ininoles la comparacion se hace con los valores de las fecha de las lineas 
					Timestamp MaxDate = DB.getSQLValueTS(po.get_TrxName(), "SELECT MAX(TP_InicialHR) FROM M_MovementLine WHERE M_Movement_ID = "+rLine.getM_Movement_ID());
					Timestamp MinDate = DB.getSQLValueTS(po.get_TrxName(), "SELECT MIN(TP_FinalHR) FROM M_MovementLine WHERE M_Movement_ID = "+rLine.getM_Movement_ID());				
					MinDate.setHours(0);
					MinDate.setMinutes(0);
					MinDate.setSeconds(0);
					if(rLine.getDateTrx().compareTo(MaxDate) > 0 
							||rLine.getDateTrx().compareTo(MinDate) < 0)
						throw new AdempiereException("ERROR: Fecha de HR no coincide con fecha de viatico");
					//viatico con Disponibilidad ya existente
					int cantRepLine = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
						" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
						" WHERE r.type = '"+head.getType()+"' AND Pre_M_Movement_ID > 0 AND r.C_BPartner_ID = "+rLine.getTP_Refund().getC_BPartner_ID()+
						" AND rl.DateTrx = ?",rLine.getDateTrx());
					if (cantRepLine > 0)
						throw new AdempiereException("ERROR: Ya existe un viatico por disponibilidad para la misma fecha y conductor:"+rLine.getTP_Refund().getC_BPartner().getName());
				}
				//hoja de ruta ya existente
				if(rLine.get_ValueAsInt("Pre_M_Movement_ID") > 0)
				{
					int cantRep = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
						" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
						" WHERE r.type = '"+head.getType()+"' AND M_Movement_ID > 0 AND r.C_BPartner_ID = "+rLine.getTP_Refund().getC_BPartner_ID()+
						" AND rl.DateTrx = ?",rLine.getDateTrx());
					if (cantRep > 0)
						throw new AdempiereException("ERROR: Ya existe un viatico por hoja de ruta para la misma fecha y conductor:"+rLine.getTP_Refund().getC_BPartner().getName());
				}
				//hr repetida y obligatoria
				int cant = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine" +
					" WHERE IsActive = 'Y' AND TP_RefundLine_ID <> "+rLine.get_ID()+" AND M_Movement_ID = "+rLine.getM_Movement_ID()+
					" AND TP_RefundAmt_ID = "+rLine.get_ValueAsInt("TP_RefundAmt_ID")+" AND DateTrx = ?",rLine.getDateTrx());
				if (cant > 0)
					throw new AdempiereException("ERROR: Existe una HR y concepto repetido");
				//hr obligatoria
				if(rLine.getM_Movement_ID() <=0 && rLine.get_ValueAsInt("TP_RefundAmt_ID") > 0 
						&& (rLine.getTP_Refund().getAD_Org_ID() != 1000004 && rLine.getTP_Refund().getAD_Org_ID() != 1000017 && rLine.getTP_Refund().getAD_Org_ID() != 1000067))    
					throw new AdempiereException("ERROR: Debe ingresar HR");
				//nueva validacion misma fecha 2 veces y mismo concepto //se opta por sacar validacion 09-03-2018
				/*int cantRep = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
						" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
						" WHERE r.C_BPartner_ID = "+rLine.getTP_Refund().getC_BPartner_ID()+
						" AND TP_RefundAmt_ID = "+rLine.get_ValueAsInt("TP_RefundAmt_ID")+
						" AND r.Type = '"+head.getType()+"' AND rl.DateTrx = ?",rLine.getDateTrx());
					if (cantRep > 0)
						throw new AdempiereException("ERROR: Ya existe un viatico para la misma fecha y conductor: "+rLine.getTP_Refund().getC_BPartner().getName());
				*/
			}		
		}		
		/*if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==X_TP_Refund.Table_ID)  
		{
			X_TP_Refund refund = (X_TP_Refund) po;
			X_TP_RefundHeader head = new X_TP_RefundHeader(po.getCtx(), refund.getTP_RefundHeader_ID(), po.get_TrxName());
			if(head.getType().compareTo("02") == 0)
			{
				
				int cantRep = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM TP_Refund r" +
						" WHERE r.C_BPartner_ID = "+refund.getC_BPartner_ID()+
						" AND r.DateDoc = ?",refund.getDateDoc());
				if (cantRep > 0)
					throw new AdempiereException("ERROR: Ya existe un viatico para la misma fecha y conductor: "+refund.getC_BPartner().getName());
			}
		}*/		
		if(type == TYPE_BEFORE_DELETE && po.get_Table_ID()==X_TP_RefundLine.Table_ID)  
		{
			X_TP_RefundLine rLine = (X_TP_RefundLine) po;
			X_TP_RefundHeader head = new X_TP_RefundHeader(po.getCtx(), rLine.getTP_Refund().getTP_RefundHeader_ID(), po.get_TrxName());
			MRole role = new MRole(po.getCtx(), Env.getAD_Role_ID(Env.getCtx()), po.get_TrxName());
			boolean flag = role.get_ValueAsBoolean("RefundMod");			
			if(head.isSignature1() && flag == false)
				return "ERROR: No se puede borrar linea de viatico";				
			if(head.isSignature2() && flag == false)
				return "ERROR: No se puede borrar linea de viatico";
		}
		if(type == TYPE_BEFORE_DELETE && po.get_Table_ID()==X_TP_Refund.Table_ID)  
		{
			X_TP_Refund refund = (X_TP_Refund) po;
			X_TP_RefundHeader head = new X_TP_RefundHeader(po.getCtx(), refund.getTP_RefundHeader_ID(), po.get_TrxName());
			MRole role = new MRole(po.getCtx(), Env.getAD_Role_ID(Env.getCtx()), po.get_TrxName());
			boolean flag = role.get_ValueAsBoolean("RefundMod");			
			if(head.isSignature1() && flag == false)
				return "ERROR: No se puede borrar linea de viatico";				
			if(head.isSignature2() && flag == false)
				return "ERROR: No se puede borrar linea de viatico";
		}
		if(type == TYPE_BEFORE_DELETE && po.get_Table_ID()==X_TP_Refund.Table_ID)  
		{
			X_TP_Refund refund = (X_TP_Refund) po;
			DB.executeUpdate("DELETE FROM TP_RefundLine WHERE TP_Refund_ID = "+refund.get_ID(), po.get_TrxName());
			//se actualiza cabecera
			X_TP_RefundHeader head = new X_TP_RefundHeader(po.getCtx(), refund.getTP_RefundHeader_ID(), po.get_TrxName());
			BigDecimal amtrefund = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(rl.amt) " +
					" FROM tp_refund r " +
					" INNER JOIN tp_refundline rl ON (rl.tp_refund_id = r.tp_refund_id)" +
					" WHERE r.tp_refundheader_id = "+head.get_ID());
			if(amtrefund != null)
			{
				head.set_CustomColumn("GrandTotal",amtrefund);
				head.saveEx(po.get_TrxName());
			}
			
			
		}	
		if(type == TYPE_BEFORE_DELETE && po.get_Table_ID()==X_TP_RefundHeader.Table_ID)  
		{
			X_TP_RefundHeader head = (X_TP_RefundHeader) po;
			DB.executeUpdate("DELETE FROM TP_RefundLine WHERE TP_Refund_ID IN (SELECT TP_Refund_ID FROM TP_Refund WHERE TP_RefundHeader_ID = "+head.get_ID()+") ", po.get_TrxName());
			DB.executeUpdate("DELETE FROM TP_Refund WHERE TP_RefundHeader_ID = "+head.get_ID(), po.get_TrxName());		
		}
		
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW || type == TYPE_AFTER_DELETE) && po.get_Table_ID()==X_TP_RefundLine.Table_ID)  
		{
			X_TP_RefundLine rLine = (X_TP_RefundLine) po;
			X_TP_RefundHeader head = new X_TP_RefundHeader(po.getCtx(), rLine.getTP_Refund().getTP_RefundHeader_ID(), po.get_TrxName());
			BigDecimal amtrefund = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(rl.amt) " +
					" FROM tp_refund r " +
					" INNER JOIN tp_refundline rl ON (rl.tp_refund_id = r.tp_refund_id)" +
					" WHERE r.tp_refundheader_id = "+head.get_ID());
			if(amtrefund != null)
			{
				head.set_CustomColumn("GrandTotal",amtrefund);
				head.saveEx(po.get_TrxName());
			}
		}
		
		return null;
	}	//	modelChange	


	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		
		return null;
	}	//	docValidate

	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString
	private boolean updateHeader(MCash cash)
	{
		String sql = "SELECT COALESCE(SUM(cl.Amount),0) "
				+ "FROM C_CashLine cl "
				+ "WHERE cl.IsActive='Y'"
				+ " AND cl.C_Cash_ID=" + cash.getC_Cash_ID();
		BigDecimal StatementDifference = DB.getSQLValueBD(cash.get_TrxName(), sql);
		cash.setStatementDifference(StatementDifference);
		cash.setEndingBalance(cash.getBeginningBalance().add(StatementDifference));
		cash.saveEx();
		return true;
	}	//	updateHeader

	

}	
