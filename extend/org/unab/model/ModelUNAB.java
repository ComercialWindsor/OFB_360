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
package org.unab.model;

import java.math.BigDecimal;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_A_Asset;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_C_OrderLine;
import org.compiere.model.X_C_Payment;
import org.compiere.model.X_T_BudgetDistribution;
import org.compiere.model.X_T_BudgetDistributionOrder;
import org.compiere.model.X_T_BudgetDistributionPay;
import org.compiere.model.X_T_PRESUPUESTO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company UNAB
 *
 *  @author Julio Farias
 */
public class ModelUNAB implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelUNAB ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelUNAB.class);
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
		//	Documents to be monitored		
		engine.addModelChange(X_T_BudgetDistribution.Table_Name, this);
		engine.addModelChange(X_C_OrderLine.Table_Name, this);
		engine.addModelChange(X_C_Payment.Table_Name, this);
		engine.addModelChange(X_T_BudgetDistributionPay.Table_Name, this);
		engine.addModelChange(X_T_BudgetDistributionOrder.Table_Name, this);
		engine.addModelChange(X_C_Order.Table_Name, this);
		engine.addModelChange(X_A_Asset.Table_Name, this);
			

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farías
     */
	public static final String DOCSTATUS_Drafted = "DR";
	public static final String DOCSTATUS_Completed = "CO";
	public static final String DOCSTATUS_InProgress = "IP";
	public static final String DOCSTATUS_Voided = "VO";
	
	
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_DELETE) && po.get_Table_ID()==X_T_BudgetDistribution.Table_ID)
		{		
			X_T_BudgetDistribution BDistribution = (X_T_BudgetDistribution) po;
			X_T_PRESUPUESTO pre = new X_T_PRESUPUESTO(po.getCtx(), BDistribution.getT_PRESUPUESTO_ID(), po.get_TrxName());
			
			BigDecimal amtPresupuesto = DB.getSQLValueBD(po.get_TrxName(), "select sum(amt) from T_BudgetDistribution where isactive='Y' and T_PRESUPUESTO_ID="+ BDistribution.getT_PRESUPUESTO_ID());
			
			if (amtPresupuesto.compareTo(pre.getLineNetAmt()) > 0)
			{
				return "No es posible asignar un monto mayor al presupuestado";
			}			
			
			BigDecimal amtEjeUNAB = DB.getSQLValueBD(po.get_TrxName(), "select sum(Balance) from T_BudgetDistribution where isactive='Y' and contributiontype = '01' and  T_PRESUPUESTO_ID="+ BDistribution.getT_PRESUPUESTO_ID());
			BigDecimal amtEjeMINE = DB.getSQLValueBD(po.get_TrxName(), "select sum(Balance) from T_BudgetDistribution where isactive='Y' and contributiontype = '02' and  T_PRESUPUESTO_ID="+ BDistribution.getT_PRESUPUESTO_ID());
			
			BigDecimal amtDevUNAB = DB.getSQLValueBD(po.get_TrxName(), "select sum(Balance2) from T_BudgetDistribution where isactive='Y' and contributiontype = '01' and  T_PRESUPUESTO_ID="+ BDistribution.getT_PRESUPUESTO_ID());
			BigDecimal amtDevMINE = DB.getSQLValueBD(po.get_TrxName(), "select sum(Balance2) from T_BudgetDistribution where isactive='Y' and contributiontype = '02' and  T_PRESUPUESTO_ID="+ BDistribution.getT_PRESUPUESTO_ID());
			
			pre.setexecuted_unab(amtEjeUNAB);
			pre.setexecuted_mineduc(amtEjeMINE);
			pre.setaccrued_unab(amtDevUNAB);
			pre.setaccrued_mineduc(amtDevMINE);
			pre.save();
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_DELETE) && po.get_Table_ID()==X_T_BudgetDistributionPay.Table_ID)
		{		
			X_T_BudgetDistributionPay BDistributionP = (X_T_BudgetDistributionPay) po;
			MPayment pay = new MPayment(po.getCtx(), BDistributionP.getC_Payment_ID(), po.get_TrxName());
			
			BigDecimal amtPresupuesto = DB.getSQLValueBD(po.get_TrxName(), "select sum(amt) from T_BudgetDistributionPay where isactive='Y' and C_Payment_ID="+ BDistributionP.getC_Payment_ID());
			
			if (amtPresupuesto.compareTo(pay.getPayAmt()) > 0)
			{
				return "No es posible asignar un monto mayor al monto del documento";
			}			
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_DELETE) && po.get_Table_ID()==X_T_BudgetDistributionOrder.Table_ID)
		{		
			X_T_BudgetDistributionOrder BDistributionO = (X_T_BudgetDistributionOrder) po;
			MOrderLine oLine = new MOrderLine(po.getCtx(), BDistributionO.getC_OrderLine_ID(), po.get_TrxName());
			
			BigDecimal amtPresupuesto = DB.getSQLValueBD(po.get_TrxName(), "select sum(amt) from T_BudgetDistributionOrder where isactive='Y' and C_OrderLine_ID="+ BDistributionO.getC_OrderLine_ID());
			
			if (amtPresupuesto.compareTo(oLine.getLineNetAmt()) > 0)
			{
				return "No es posible asignar un monto mayor al monto del documento";
			}			
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE || type == TYPE_BEFORE_DELETE) && po.get_Table_ID()==X_C_OrderLine.Table_ID)
		{
			X_C_OrderLine oline = (X_C_OrderLine) po;
			MOrder order = new MOrder(po.getCtx(), oline.getC_Order_ID(), po.get_TrxName());
			
			if (type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)			
			{	
				if (order.getDocStatus().equalsIgnoreCase(DOCSTATUS_Drafted) && oline.isProcessed()==false && oline.getQtyReserved().compareTo(new BigDecimal("0.0"))<=0)
				{				
					DB.executeUpdate("DELETE FROM T_BudgetDistributionOrder WHERE C_OrderLine_ID = "+oline.get_ID(), po.get_TrxName());
				
					X_T_BudgetDistributionOrder dorder1 = new X_T_BudgetDistributionOrder(po.getCtx(), 0, po.get_TrxName());
					dorder1.setC_OrderLine_ID(oline.get_ID());
					dorder1.setContributionType("01");
				
					BigDecimal porcentaje = DB.getSQLValueBD(po.get_TrxName(), "select max(percentage) from T_BudgetDistribution "+
						"where contributiontype = '01' AND T_PRESUPUESTO_ID="+ oline.get_ValueAsInt("T_PRESUPUESTO_ID"));
					BigDecimal amt = new BigDecimal("0.0");
					amt = amt.add(oline.getLineNetAmt().multiply(porcentaje));
					amt = amt.divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP);
					dorder1.setAmt(amt);
					dorder1.setPercentage(porcentaje);
					dorder1.save();
				
					X_T_BudgetDistributionOrder dorder2 = new X_T_BudgetDistributionOrder(po.getCtx(), 0, po.get_TrxName());
					dorder2.setC_OrderLine_ID(oline.get_ID());
					dorder2.setContributionType("02");
				
					BigDecimal porcentaje2 = DB.getSQLValueBD(po.get_TrxName(), "select max(percentage) from T_BudgetDistribution "+
						"where contributiontype = '02' AND T_PRESUPUESTO_ID="+ oline.get_ValueAsInt("T_PRESUPUESTO_ID"));
					BigDecimal amt2 = new BigDecimal("0.0");
					amt2 = amt2.add(oline.getLineNetAmt().multiply(porcentaje2));
					amt2 = amt2.divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP);
					dorder2.setAmt(amt2);
					dorder2.setPercentage(porcentaje2);
					dorder2.save();
				}
			}
			if (type == TYPE_BEFORE_DELETE)
			{
				DB.executeUpdate("DELETE FROM T_BudgetDistributionOrder WHERE C_OrderLine_ID = "+oline.get_ID(), po.get_TrxName());
			}
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE || type == TYPE_BEFORE_DELETE) && po.get_Table_ID()==X_C_Payment.Table_ID)
		{
			X_C_Payment cPay = (X_C_Payment) po;
			
			if(type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)			
			{
				if (cPay.getDocStatus().equalsIgnoreCase(DOCSTATUS_Drafted))
				{
					DB.executeUpdate("DELETE FROM T_BudgetDistributionPay WHERE C_Payment_ID = "+cPay.get_ID(), po.get_TrxName());
				
					X_T_BudgetDistributionPay dPay1 = new X_T_BudgetDistributionPay(po.getCtx(), 0, po.get_TrxName());
					dPay1.setC_Payment_ID(cPay.get_ID());
					dPay1.setContributionType("01");
				
					BigDecimal porcentaje = DB.getSQLValueBD(po.get_TrxName(), "select max(percentage) from T_BudgetDistribution "+
						"where contributiontype = '01' AND T_PRESUPUESTO_ID="+ cPay.get_ValueAsInt("T_PRESUPUESTO_ID"));
					BigDecimal amt = new BigDecimal("0.0");
					amt = amt.add(cPay.getPayAmt().multiply(porcentaje));
					amt = amt.divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP);
					dPay1.setAmt(amt);
					dPay1.setPercentage(porcentaje);
					dPay1.save();
				
					X_T_BudgetDistributionPay dPay2 = new X_T_BudgetDistributionPay(po.getCtx(), 0, po.get_TrxName());
					dPay2.setC_Payment_ID(cPay.get_ID());
					dPay2.setContributionType("02");
				
					BigDecimal porcentaje2 = DB.getSQLValueBD(po.get_TrxName(), "select max(percentage) from T_BudgetDistribution "+
						"where contributiontype = '02' AND T_PRESUPUESTO_ID="+ cPay.get_ValueAsInt("T_PRESUPUESTO_ID"));
					BigDecimal amt2 = new BigDecimal("0.0");
					amt2 = amt2.add(cPay.getPayAmt().multiply(porcentaje2));
					amt2 = amt2.divide(Env.ONEHUNDRED, BigDecimal.ROUND_HALF_UP);
					dPay2.setAmt(amt2);
					dPay2.setPercentage(porcentaje2);
					dPay2.save();
				}
			}
			if (type == TYPE_BEFORE_DELETE)
			{
				DB.executeUpdate("DELETE FROM T_BudgetDistributionPay WHERE C_Payment_ID = "+cPay.get_ID(), po.get_TrxName());
			}
		}		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==X_C_Payment.Table_ID)
		{
			X_C_Payment cPay = (X_C_Payment) po;
			X_T_PRESUPUESTO pres = new X_T_PRESUPUESTO(po.getCtx(), cPay.get_ValueAsInt("T_PRESUPUESTO_ID"), po.get_TrxName());
			BigDecimal amtEjeUNAB = DB.getSQLValueBD(po.get_TrxName(), "select sum(amt) from T_BudgetDistributionPay where isactive='Y' and contributiontype = '01' and C_Payment_ID="+ cPay.getC_Payment_ID());
			BigDecimal amtEjeMine = DB.getSQLValueBD(po.get_TrxName(), "select sum(amt) from T_BudgetDistributionPay where isactive='Y' and contributiontype = '02' and C_Payment_ID="+ cPay.getC_Payment_ID());
			
			if (cPay.getDocStatus() == DOCSTATUS_Completed)
			{	
				BigDecimal tempexe = new BigDecimal("0.0");
				tempexe.add(pres.getexecuted().add(cPay.getPayAmt()));
				
				if (pres.getLineNetAmt().compareTo(tempexe)<0 )
				{
					return "No es posible asignar un monto mayor al monto presupuestado";
				}
				else
				{
					pres.setexecuted(pres.getexecuted().add(cPay.getPayAmt()));
					pres.setexecuted_available(pres.getLineNetAmt().subtract(pres.getexecuted()));
					
					DB.executeUpdate("UPDATE T_BudgetDistribution set Balance = Balance + "+amtEjeUNAB+" WHERE T_PRESUPUESTO_ID = "+pres.get_ID()+" AND contributiontype = '01' ", po.get_TrxName());
					DB.executeUpdate("UPDATE T_BudgetDistribution set Balance = Balance + "+amtEjeMine+" WHERE T_PRESUPUESTO_ID = "+pres.get_ID()+" AND contributiontype = '02' ", po.get_TrxName());
					
					pres.setexecuted_unab(pres.getexecuted_unab().add(amtEjeUNAB));
					pres.setexecuted_mineduc(pres.getexecuted_mineduc().add(amtEjeMine));					
				}								
			}
			if (cPay.getDocStatus() == DOCSTATUS_Voided)
			{					
				pres.setexecuted(pres.getexecuted().subtract(amtEjeUNAB.add(amtEjeMine)));
				pres.setexecuted_available(pres.getLineNetAmt().subtract(pres.getexecuted()));
				
				DB.executeUpdate("UPDATE T_BudgetDistribution set Balance = Balance - "+amtEjeUNAB+" WHERE T_PRESUPUESTO_ID = "+pres.get_ID()+" AND contributiontype = '01' ", po.get_TrxName());
				DB.executeUpdate("UPDATE T_BudgetDistribution set Balance = Balance - "+amtEjeMine+" WHERE T_PRESUPUESTO_ID = "+pres.get_ID()+" AND contributiontype = '02' ", po.get_TrxName());
				
				pres.setexecuted_unab(pres.getexecuted_unab().subtract(amtEjeUNAB));
				pres.setexecuted_mineduc(pres.getexecuted_mineduc().subtract(amtEjeMine));			
			}
			pres.save();			
		}	
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==X_C_Order.Table_ID)
		{
			MOrder order = (MOrder) po;
			
			MOrderLine[] lines = order.getLines(false, null);
			
			for (int i = 0; i < lines.length; i++)				
			{
				MOrderLine oLine = lines[i];
				
				X_T_PRESUPUESTO pres = new X_T_PRESUPUESTO(po.getCtx(), oLine.get_ValueAsInt("T_PRESUPUESTO_ID"), po.get_TrxName());
				BigDecimal amtDevUNAB = DB.getSQLValueBD(po.get_TrxName(), "select sum(amt) from T_BudgetDistributionOrder where isactive='Y' and contributiontype = '01' and C_OrderLine_ID="+ oLine.get_ID());
				BigDecimal amtDevMine = DB.getSQLValueBD(po.get_TrxName(), "select sum(amt) from T_BudgetDistributionOrder where isactive='Y' and contributiontype = '02' and C_OrderLine_ID="+ oLine.get_ID());
				
				if (order.getDocStatus() == DOCSTATUS_Completed)
				{	
					BigDecimal tempexe = new BigDecimal("0.0");
					tempexe.add(pres.getexecuted().add(oLine.getLineNetAmt()));
					
					if (pres.getLineNetAmt().compareTo(tempexe)<0 )
					{
						return "No es posible asignar un monto mayor al monto presupuestado";
					}
					else
					{					
						pres.setaccrued(pres.getaccrued().add(oLine.getLineNetAmt()));
						pres.setaccrued_available(pres.getLineNetAmt().subtract(pres.getaccrued()));
					
						DB.executeUpdate("UPDATE T_BudgetDistribution set Balance2 = Balance2 + "+amtDevUNAB+" WHERE T_PRESUPUESTO_ID = "+pres.get_ID()+" AND contributiontype = '01' ", po.get_TrxName());
						DB.executeUpdate("UPDATE T_BudgetDistribution set Balance2 = Balance2 + "+amtDevMine+" WHERE T_PRESUPUESTO_ID = "+pres.get_ID()+" AND contributiontype = '02' ", po.get_TrxName());
					
						pres.setaccrued_unab(pres.getaccrued_unab().add(amtDevUNAB));
						pres.setaccrued_mineduc(pres.getaccrued_mineduc().add(amtDevMine));		
					}
				}
				if (order.getDocStatus() == DOCSTATUS_Voided)
				{					
					pres.setaccrued(pres.getaccrued().subtract(amtDevUNAB.add(amtDevMine)));
					pres.setaccrued_available(pres.getLineNetAmt().subtract(pres.getaccrued()));
					
					DB.executeUpdate("UPDATE T_BudgetDistribution set Balance2 = Balance2 - "+amtDevUNAB+" WHERE T_PRESUPUESTO_ID = "+pres.get_ID()+" AND contributiontype = '01' ", po.get_TrxName());
					DB.executeUpdate("UPDATE T_BudgetDistribution set Balance2 = Balance2 - "+amtDevMine+" WHERE T_PRESUPUESTO_ID = "+pres.get_ID()+" AND contributiontype = '02' ", po.get_TrxName());
					
					pres.setaccrued_unab(pres.getaccrued_unab().subtract(amtDevUNAB));
					pres.setaccrued_mineduc(pres.getaccrued_mineduc().subtract(amtDevMine));			
				}
				pres.save();				
			}
		}
		if((type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==X_C_Order.Table_ID)
		{
			MOrder order = (MOrder) po;			
			//MOrderLine[] lines = order.getLines(false, null);
			
			if (order.getDocStatus() == DOCSTATUS_Completed)
			{
				
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