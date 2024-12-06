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
package org.andes.model;

import java.math.BigDecimal;

import org.compiere.model.MBankAccount;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_CD_Header;
import org.compiere.model.X_CD_Line;
import org.compiere.model.X_CD_Summary;
import org.compiere.model.X_CD_Transfer;
import org.compiere.model.X_C_BankAccountBalance;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company Andes
 *
 *  @author Italo Niñoles
 */
public class ModAndesCD3 implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModAndesCD3 ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModAndesCD3.class);
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
		engine.addModelChange(X_CD_Line.Table_Name, this);
		engine.addModelChange(X_CD_Header.Table_Name, this);
		engine.addModelChange(X_CD_Transfer.Table_Name, this);
		engine.addModelChange(X_CD_Summary.Table_Name, this);
		//	Documents to be monitored
		

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==X_CD_Line.Table_ID)  
		{
			X_CD_Line line = (X_CD_Line) po;
			int ID_Summary = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Summary_ID) " +
					" FROM CD_Summary WHERE IsActive = 'Y' AND CD_Header_ID = "+line.getCD_Header_ID());
			BigDecimal amtIN = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Line " +
					" WHERE CD_Header_ID = "+line.getCD_Header_ID()+" AND Type = 'IN'");
			BigDecimal amtEG = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Line " +
					" WHERE CD_Header_ID = "+line.getCD_Header_ID()+" AND Type = 'EG'");
			
			if(amtIN == null)
				amtIN =Env.ZERO;
			if(amtEG == null)
				amtEG =Env.ZERO;
			
			if(ID_Summary > 0)
			{
				X_CD_Summary sum = new X_CD_Summary(po.getCtx(),ID_Summary, po.get_TrxName());
				sum.setTotalIncome(amtIN);
				sum.setTotalExpenses(amtEG);
				sum.save();
			}
			else
			{
				//se crea summary
				X_CD_Summary sum = new X_CD_Summary(po.getCtx(),0, po.get_TrxName());
				sum.setCD_Header_ID(line.getCD_Header_ID());
				sum.setTotalIncome(amtIN);
				sum.setTotalExpenses(amtEG);
				sum.save();
			}
		}		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID() == X_CD_Header.Table_ID)  
		{
			X_CD_Header cab = (X_CD_Header) po;
			MBankAccount bAccount = new MBankAccount(po.getCtx(), cab.getC_BankAccount_ID(), po.get_TrxName());
			String typeBAccount = bAccount.get_ValueAsString("Type");
			if(typeBAccount == null || typeBAccount.trim().length() < 1)
				typeBAccount = "H";
			if(typeBAccount.compareTo("M") == 0)//cuenta madre
			{
				BigDecimal bBalance = null;
				if(cab.get_ValueAsInt("C_BankAccountBalance_ID") > 0)
				{	
					X_C_BankAccountBalance balance = new X_C_BankAccountBalance(po.getCtx(), cab.get_ValueAsInt("C_BankAccountBalance_ID"), po.get_TrxName()) ;
					bBalance = balance.getAvailableBalance();
					String sqlChild = "SELECT SUM(AvailableBalance) FROM C_BankAccountBalance " +
							" WHERE C_BankAccountBalance_ID IN " +
							" (SELECT MIN(C_BankAccountBalance_ID)" +
							" FROM C_BankAccountBalance" +
							" WHERE C_BankAccount_ID IN " +
							"  (SELECT C_BankAccount_ID FROM C_BankAccount" +
							"  WHERE C_Bank_ID = "+bAccount.getC_Bank_ID()+
							"  AND (Type IS NULL OR Type IN ('H'))) " +
							"  AND DateDoc = ? AND IsActive = 'Y')";
					BigDecimal amountChild = DB.getSQLValueBD(po.get_TrxName(), sqlChild, cab.getDateTrx());
					if(amountChild != null)
						bBalance = bBalance.add(amountChild);
				}
				else
				{
					log.config("monto total antes de sql: "+bBalance);
					String sql = "SELECT SUM(AvailableBalance) FROM C_BankAccountBalance " +
							"WHERE C_BankAccountBalance_ID IN " +
							" (SELECT MIN(C_BankAccountBalance_ID) " +
							"   FROM C_BankAccountBalance " +
							"   WHERE C_BankAccount_ID IN " +
							"     (SELECT C_BankAccount_ID FROM C_BankAccount " +
							"       WHERE IsActive = 'Y' AND C_Bank_ID = "+bAccount.getC_Bank_ID()+
							"       AND (Type IS NULL OR Type IN ('H')) " +
							"		OR C_BankAccount_ID = "+bAccount.get_ID()+") AND DateDoc IS NOT NULL AND DateDoc = ? AND IsActive = 'Y' " +
							"	GROUP BY C_BankAccount_ID) ";					
					bBalance = DB.getSQLValueBD(po.get_TrxName(), sql, cab.getDateTrx());
					log.config("SQL SUmatoria de saldos: "+sql);
					log.config("parametros: banco="+bAccount.getC_Bank_ID()+". Account_ID="+bAccount.get_ID()+". DateDoc="+cab.getDateTrx());
					log.config("monto total: "+bBalance);
				}
				
				if(bBalance != null)
				{
					//cab.setBeginningBalance(bBalance);
					//la actualizacion sera por DB para evitar error de requery
					DB.executeUpdate("UPDATE CD_Header SET BeginningBalance="+bBalance+" WHERE CD_Header_ID = "+cab.get_ID(), po.get_TrxName());
				}
			}
			else
			{
				//return "Error: Cuenta debe ser madre o recaudadora";
				//ahora no mandara error las cuentas que no son madre o recolectoras
				BigDecimal bBalance = null;
				if(cab.get_ValueAsInt("C_BankAccountBalance_ID") > 0)
				{	
					X_C_BankAccountBalance balance = new X_C_BankAccountBalance(po.getCtx(), cab.get_ValueAsInt("C_BankAccountBalance_ID"), po.get_TrxName()) ;
					bBalance = balance.getAvailableBalance();
				}
				else
				{
					String sql = "SELECT SUM(AvailableBalance) FROM C_BankAccountBalance " +
							" WHERE C_BankAccountBalance_ID IN ( " +
							" SELECT MIN(C_BankAccountBalance_ID) " +
							" FROM C_BankAccountBalance " +
							" WHERE IsActive = 'Y' AND C_BankAccount_ID IN ("+bAccount.get_ID()+") " +
							" AND DateDoc = ? GROUP BY C_BankAccount_ID)";
					 bBalance = DB.getSQLValueBD(po.get_TrxName(), sql, cab.getDateTrx());					
				}
				if(bBalance != null)
				{
					//cab.setBeginningBalance(bBalance);
					//la actualizacion sera por DB para evitar error de requery
					DB.executeUpdate("UPDATE CD_Header SET BeginningBalance="+bBalance+" WHERE CD_Header_ID = "+cab.get_ID(), po.get_TrxName());
				}
			}
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID() == X_CD_Header.Table_ID)  
		{
			X_CD_Header cab = (X_CD_Header) po;
			int ID_Summary = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Summary_ID) " +
					" FROM CD_Summary WHERE IsActive = 'Y' AND CD_Header_ID = "+cab.get_ID());		
			if(ID_Summary <= 0)
			{
				//se crea summary
				X_CD_Summary sum = new X_CD_Summary(po.getCtx(),0, po.get_TrxName());
				sum.setCD_Header_ID(cab.get_ID());
				sum.save();
			}			
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE )&& po.get_Table_ID() == X_CD_Transfer.Table_ID)  
		{
			X_CD_Transfer trans = (X_CD_Transfer) po;
			//actualizamos summary origen
			BigDecimal amtTrans = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Transfer " +
					" WHERE CD_Header_ID = "+trans.getCD_Header_ID()+" AND IsActive = 'Y'");
			if(amtTrans == null)
				amtTrans = Env.ZERO;
			BigDecimal amtIN = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Line " +
					" WHERE CD_Header_ID = "+trans.getCD_Header_ID()+" AND Type = 'IN'");
			if(amtIN == null)
				amtIN = Env.ZERO;
			int ID_Summary = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Summary_ID) " +
					" FROM CD_Summary WHERE IsActive = 'Y' AND CD_Header_ID = "+trans.getCD_Header_ID());
			if(ID_Summary > 0)
			{
				X_CD_Summary sum = new X_CD_Summary(po.getCtx(),ID_Summary, po.get_TrxName());
				sum.setTotalTransfer(trans.getCD_Header().getBeginningBalance().add(amtIN).add(amtTrans));
				sum.save();
			}
			else
			{
				//se crea summary
				X_CD_Summary sum = new X_CD_Summary(po.getCtx(),0, po.get_TrxName());
				sum.setCD_Header_ID(trans.getCD_Header_ID());
				sum.setTotalTransfer(trans.getCD_Header().getBeginningBalance().add(amtIN).add(amtTrans));
				sum.save();
			}
		}
		//ininoles ahora no se pueden modificar transferencias destino
		if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID() == X_CD_Transfer.Table_ID 
				&& (po.is_ValueChanged("Amt") || po.is_ValueChanged("C_BankAccount_ID")))  
		{
			X_CD_Transfer trans = (X_CD_Transfer) po;
			if(trans.getDescription() != null && trans.getDescription().contains("Generado desde cuenta")
					|| trans.get_ValueAsInt("CD_TransferRef_ID") > 0)
			{
				X_CD_Transfer transFrom = new X_CD_Transfer(po.getCtx(), trans.get_ValueAsInt("CD_TransferRef_ID"), po.get_TrxName());
				if(trans.getAmt().negate().compareTo(transFrom.getAmt()) != 0)
					return "Error: No se puede modificar movimiento destino. Debe modificar movimiento origen";
			}
		}
		if(type == TYPE_BEFORE_DELETE && po.get_Table_ID() == X_CD_Transfer.Table_ID)  
		{
			X_CD_Transfer trans = (X_CD_Transfer) po;
			if(trans.getDescription() != null && trans.getDescription().contains("Generado desde cuenta")
					|| trans.get_ValueAsInt("CD_TransferRef_ID") > 0)
			{
				return "Error: No se puede eliminar movimiento destino. Debe eliminar movimiento origen";
			}
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)&& po.get_Table_ID() == X_CD_Transfer.Table_ID)  
		{
			X_CD_Transfer trans = (X_CD_Transfer) po;
			if(!(trans.getDescription() != null && trans.getDescription().contains("Generado desde cuenta")))
			{	
				MBankAccount bAccountTo = new MBankAccount(po.getCtx(), trans.getC_BankAccount_ID(), po.get_TrxName());
				int ID_DisponTo = 0;
				ID_DisponTo = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Header_ID) FROM CD_Header " +
						" WHERE C_BankAccount_ID = "+bAccountTo.get_ID()+" AND DateTrx = ? ", trans.getCD_Header().getDateTrx());
				if(ID_DisponTo > 0)
				{
					X_CD_Header head = new X_CD_Header(po.getCtx(), ID_DisponTo, po.get_TrxName());
					//validamos si existe llinea ya creada
					int ID_TransRef = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Transfer_ID) FROM CD_Transfer " +
							" WHERE CD_Header_ID ="+ID_DisponTo+" AND IsActive='Y' AND CD_TransferRef_ID = "+trans.get_ID());
					if(ID_TransRef > 0)
					{
						X_CD_Transfer newTrans = new X_CD_Transfer(po.getCtx(), ID_TransRef, po.get_TrxName());
						newTrans.setAmt(trans.getAmt().negate());
						newTrans.save();
					}
					else
					{
						X_CD_Transfer newTrans = new X_CD_Transfer(po.getCtx(), 0, po.get_TrxName());
						newTrans.setCD_Header_ID(head.get_ID());
						newTrans.setAD_Org_ID(head.getAD_Org_ID());
						newTrans.setC_Charge_ID(trans.getC_Charge_ID());
						newTrans.setDescription("Generado desde cuenta "+trans.getCD_Header().getC_BankAccount().getAccountNo()+". "+trans.getCD_Header().getC_BankAccount().getC_Bank().getName());
						newTrans.setC_BankAccount_ID(trans.getCD_Header().getC_BankAccount_ID());
						newTrans.setAmt(trans.getAmt().negate());
						newTrans.set_CustomColumn("CD_TransferRef_ID", trans.get_ID());
						newTrans.save();					
					}
										
					//actualizamos summary destino
					BigDecimal amtTransDest = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Transfer " +
							" WHERE CD_Header_ID = "+head.get_ID()+" AND IsActive = 'Y'");
					if(amtTransDest == null)
						amtTransDest = Env.ZERO;
					BigDecimal amtINDest = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Line " +
							" WHERE CD_Header_ID = "+head.get_ID()+" AND Type = 'IN'");
					if(amtINDest == null)
						amtINDest = Env.ZERO;
					int ID_SummaryDest = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Summary_ID) " +
							" FROM CD_Summary WHERE IsActive = 'Y' AND CD_Header_ID = "+head.get_ID());
					if(ID_SummaryDest > 0)
					{
						X_CD_Summary sumDest = new X_CD_Summary(po.getCtx(),ID_SummaryDest, po.get_TrxName());
						sumDest.setTotalTransfer(head.getBeginningBalance().add(amtINDest).add(amtTransDest));
						sumDest.save();
					}
					else
					{
						//se crea summary
						X_CD_Summary sumDest = new X_CD_Summary(po.getCtx(),0, po.get_TrxName());
						sumDest.setCD_Header_ID(head.get_ID());
						sumDest.setTotalTransfer(head.getBeginningBalance().add(amtINDest).add(amtTransDest));
						sumDest.save();
					}
				}
				else
					return "Error: No existe disponibilidad destino";
				//actualizamos summary origen
				BigDecimal amtTrans = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Transfer " +
						" WHERE CD_Header_ID = "+trans.getCD_Header_ID()+" AND IsActive = 'Y'");
				if(amtTrans == null)
					amtTrans = Env.ZERO;
				BigDecimal amtIN = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Line " +
						" WHERE CD_Header_ID = "+trans.getCD_Header_ID()+" AND Type = 'IN'");
				if(amtIN == null)
					amtIN = Env.ZERO;
				int ID_Summary = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Summary_ID) " +
						" FROM CD_Summary WHERE IsActive = 'Y' AND CD_Header_ID = "+trans.getCD_Header_ID());
				if(ID_Summary > 0)
				{
					X_CD_Summary sum = new X_CD_Summary(po.getCtx(),ID_Summary, po.get_TrxName());
					sum.setTotalTransfer(trans.getCD_Header().getBeginningBalance().add(amtIN).add(amtTrans));
					sum.save();
				}
				else
				{
					//se crea summary
					X_CD_Summary sum = new X_CD_Summary(po.getCtx(),0, po.get_TrxName());
					sum.setCD_Header_ID(trans.getCD_Header_ID());
					sum.setTotalTransfer(trans.getCD_Header().getBeginningBalance().add(amtIN).add(amtTrans));
					sum.save();
				}					
			}					
		}
		if(type == TYPE_BEFORE_DELETE && po.get_Table_ID() == X_CD_Transfer.Table_ID)  
		{
			X_CD_Transfer trans = (X_CD_Transfer) po;
			if(!(trans.getDescription() != null && trans.getDescription().contains("Generado desde cuenta")))
			{	
				//destino
				int ID_HeadDes = DB.getSQLValue(po.get_TrxName(), "SELECT CD_Header_ID FROM CD_Transfer WHERE IsActive = 'Y' AND CD_TransferRef_ID ="+trans.get_ID());
				DB.executeUpdate("DELETE FROM CD_Transfer WHERE CD_TransferRef_ID = "+trans.get_ID(), po.get_TrxName());
				//actualizamos sumary y cabecera destino
				if(ID_HeadDes > 0)
				{
					//actualizamos summary destino
					X_CD_Header head = new X_CD_Header(po.getCtx(), ID_HeadDes, po.get_TrxName());
					BigDecimal amtTransDest = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Transfer " +
							" WHERE CD_Header_ID = "+head.get_ID()+" AND IsActive = 'Y'");
					if(amtTransDest == null)
						amtTransDest = Env.ZERO;
					BigDecimal amtINDest = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Line " +
							" WHERE CD_Header_ID = "+head.get_ID()+" AND Type = 'IN'");
					if(amtINDest == null)
						amtINDest = Env.ZERO;
					int ID_SummaryDest = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Summary_ID) " +
							" FROM CD_Summary WHERE IsActive = 'Y' AND CD_Header_ID = "+head.get_ID());
					if(ID_SummaryDest > 0)
					{
						X_CD_Summary sumDest = new X_CD_Summary(po.getCtx(),ID_SummaryDest, po.get_TrxName());
						sumDest.setTotalTransfer(head.getBeginningBalance().add(amtINDest).add(amtTransDest));
						sumDest.save();
					}
				}
			}
		}	
		if(type == TYPE_AFTER_DELETE && po.get_Table_ID() == X_CD_Transfer.Table_ID)  
		{
			X_CD_Transfer trans = (X_CD_Transfer) po;
			if(!(trans.getDescription() != null && trans.getDescription().contains("Generado desde cuenta")))
			{
				//origen
				//actualizamos sumary y cabecera origen
				//actualizamos summary origen
				X_CD_Header headOrg= new X_CD_Header(po.getCtx(), trans.getCD_Header_ID(), po.get_TrxName());
				BigDecimal amtTransOrg = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Transfer " +
						" WHERE CD_Header_ID = "+headOrg.get_ID()+" AND IsActive = 'Y'");
				if(amtTransOrg == null)
					amtTransOrg = Env.ZERO;
				BigDecimal amtINOrg = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Line " +
						" WHERE CD_Header_ID = "+headOrg.get_ID()+" AND Type = 'IN'");
				if(amtINOrg == null)
					amtINOrg = Env.ZERO;
				int ID_SummaryOrg = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Summary_ID) " +
						" FROM CD_Summary WHERE IsActive = 'Y' AND CD_Header_ID = "+headOrg.get_ID());
				if(ID_SummaryOrg > 0)
				{
					X_CD_Summary sumOrg = new X_CD_Summary(po.getCtx(),ID_SummaryOrg, po.get_TrxName());
					sumOrg.setTotalTransfer(headOrg.getBeginningBalance().add(amtINOrg).add(amtTransOrg));
					sumOrg.save();
				}
			}
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)&& po.get_Table_ID() == X_CD_Transfer.Table_ID)  
		{
			X_CD_Transfer trans = (X_CD_Transfer) po;
			int ID_Summary = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Summary_ID) " +
					" FROM CD_Summary WHERE IsActive = 'Y' AND CD_Header_ID = "+trans.getCD_Header_ID());
			if(ID_Summary > 0)
			{
				X_CD_Summary sum = new X_CD_Summary(po.getCtx(),ID_Summary, po.get_TrxName());
				//trans.set_CustomColumn("Amt2", sum.getGrandTotal());
				DB.executeUpdate("UPDATE CD_Transfer SET Amt2 = "+sum.getGrandTotal()+" WHERE CD_Transfer_ID = "+trans.get_ID(),po.get_TrxName());
				DB.executeUpdate("UPDATE CD_Transfer SET Amt3 = "+sum.getGrandTotal().subtract(trans.getAmt())+" WHERE CD_Transfer_ID = "+trans.get_ID(),po.get_TrxName());
			}
		}
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==X_CD_Summary.Table_ID)  
		{
			X_CD_Summary summ = (X_CD_Summary) po;
			
			//calculo de traspasos
			BigDecimal amtTrans = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Transfer " +
					" WHERE CD_Header_ID = "+summ.getCD_Header_ID()+" AND IsActive = 'Y'");
			if(amtTrans == null)
				amtTrans = Env.ZERO;
			// total ingreso y egreso						
			BigDecimal amtIN = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Line " +
					" WHERE CD_Header_ID = "+summ.getCD_Header_ID()+" AND Type = 'IN'");
			BigDecimal amtEG = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(amt) FROM CD_Line " +
					" WHERE CD_Header_ID = "+summ.getCD_Header_ID()+" AND Type = 'EG'");
			
			if(amtIN == null)
				amtIN =Env.ZERO;
			if(amtEG == null)
				amtEG =Env.ZERO;
			
			summ.setTotalIncome(amtIN);
			summ.setTotalExpenses(amtEG);
			
			//total banco
			summ.setTotalBanks(summ.getCD_Header().getBeginningBalance().add(amtIN));
			
			//total linea de credito
			String sqlCLine = "SELECT SUM(AvailableBalance) FROM C_BankAccountBalance " +
					"WHERE C_BankAccountBalance_ID IN (SELECT MIN(C_BankAccountBalance_ID) " +
					"   FROM C_BankAccountBalance WHERE C_BankAccount_ID IN " +
					"      (SELECT C_BankAccount_ID FROM C_BankAccount " +
					"       WHERE C_Bank_ID = "+summ.getCD_Header().getC_Bank_ID()+" AND IsCreditLine = 'Y') " +
					"   AND DateDoc = ? AND IsActive = 'Y' GROUP BY C_BankAccount_ID)";
			BigDecimal amtCLine = DB.getSQLValueBD(po.get_TrxName(),sqlCLine, summ.getCD_Header().getDateTrx());
			if(amtCLine == null)
				amtCLine = Env.ZERO;
			summ.setCreditLine(amtCLine);
			
			//total despues de traspasos
			BigDecimal amtATrans = summ.getCD_Header().getBeginningBalance().add(amtIN).add(amtTrans);
			summ.setTotalTransfer(amtATrans);
			
			//calculo excedente
			BigDecimal excedente = summ.getCD_Header().getBeginningBalance().subtract(amtEG);
			excedente = excedente.add(amtTrans);
			excedente = excedente.add(amtIN);
			summ.setSurplus(excedente);
			
			//calculo total final
			BigDecimal invest2 = (BigDecimal)summ.get_Value("InvestmentAmount2");
			if(invest2 == null)
				invest2 = Env.ZERO;
			BigDecimal totalAmt = excedente.subtract(summ.getInvestmentAmount());
			totalAmt = totalAmt.subtract(invest2);
			summ.setGrandTotal(totalAmt);
			/**/
		}	
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==X_CD_Summary.Table_ID)  
		{
			X_CD_Summary summ = (X_CD_Summary) po;
			//actualizacion será por DB
			//X_CD_Header dispon = new X_CD_Header(po.getCtx(), summ.getCD_Header_ID(), po.get_TrxName());
			/*dispon.setEndingBalance(summ.getGrandTotal());
			dispon.save();*/
			DB.executeUpdate("UPDATE CD_Header SET EndingBalance="+summ.getGrandTotal()+" WHERE CD_Header_ID = "+summ.getCD_Header_ID(), po.get_TrxName());
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)&& po.get_Table_ID() == X_CD_Line.Table_ID)  
		{
			X_CD_Line line = (X_CD_Line) po;
			int ID_Summary = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(CD_Summary_ID) " +
					" FROM CD_Summary WHERE IsActive = 'Y' AND CD_Header_ID = "+line.getCD_Header_ID());
			if(ID_Summary > 0)
			{
				X_CD_Summary sum = new X_CD_Summary(po.getCtx(),ID_Summary, po.get_TrxName());
				DB.executeUpdate("UPDATE CD_Line SET Amt2 = "+sum.getGrandTotal()+" WHERE CD_Line_ID = "+line.get_ID(),po.get_TrxName());
				if(line.get_ValueAsString("Type").compareTo("IN") == 0)
					DB.executeUpdate("UPDATE CD_Line SET Amt3 = "+sum.getGrandTotal().subtract(line.getAmt())+" WHERE CD_Line_ID = "+line.get_ID(),po.get_TrxName());
				else if(line.get_ValueAsString("Type").compareTo("EG") == 0)
					DB.executeUpdate("UPDATE CD_Line SET Amt3 = "+sum.getGrandTotal().add(line.getAmt())+" WHERE CD_Line_ID = "+line.get_ID(),po.get_TrxName());
				else
					DB.executeUpdate("UPDATE CD_Line SET Amt3 = "+sum.getGrandTotal().subtract(line.getAmt())+" WHERE CD_Line_ID = "+line.get_ID(),po.get_TrxName());
				
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


	

}	