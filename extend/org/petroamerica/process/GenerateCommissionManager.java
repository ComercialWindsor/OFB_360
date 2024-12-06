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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MCommissionAmt;
import org.compiere.model.MCommissionDetail;
import org.compiere.model.MCommissionRun;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 */
public class GenerateCommissionManager extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_CommissionR_ID;
		
	protected void prepare()
	{	
		p_CommissionR_ID = getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MCommissionRun commisR = new MCommissionRun(getCtx(), p_CommissionR_ID, get_TrxName());
		//borrado de detalle ya existente sin factura o pago
		DB.executeUpdate("DELETE FROM C_CommissionDetail WHERE C_CommissionDetail_ID IN " +
				"(SELECT C_CommissionDetail_ID  FROM C_CommissionDetail cd " +
				" INNER JOIN C_CommissionAmt ca ON (cd.C_CommissionAmt_ID = ca.C_CommissionAmt_ID) " +
				" INNER JOIN C_CommissionRun cr ON (ca.C_CommissionRun_ID = cr.C_CommissionRun_ID) " +
				" WHERE C_Payment_ID IS NULL AND C_Invoice_Id IS NULL " +
				" AND lower(info) LIKE '%gerencia%' AND cr.C_CommissionRun_ID="+commisR.get_ID()+")",get_TrxName());
		//ciclo inicial de los SalesRep 
		String sqlSalesRep = "SELECT DISTINCT(SalesRep_ID) as SalesRep_ID FROM AD_Org WHERE IsActive = 'Y' " +
				" AND SalesRep_ID IS NOT NULL AND AD_Client_ID = "+commisR.getAD_Client_ID();
		PreparedStatement pstmtSRep = null;
		ResultSet rsSRep = null;
		try
		{
			pstmtSRep = DB.prepareStatement (sqlSalesRep, get_TrxName());
			rsSRep = pstmtSRep.executeQuery();
			while (rsSRep.next ())
			{
				String sqlPList = "SELECT AD_Org_ID,Factor FROM AD_Org WHERE IsActive = 'Y' " +
						"AND SalesRep_ID = "+rsSRep.getInt("SalesRep_ID") +" AND AD_Client_ID = "+commisR.getAD_Client_ID();
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				BigDecimal AmtTotalUser = new BigDecimal("0.0");
				BigDecimal AmtComUserTotal = new BigDecimal("0.0");
				BigDecimal AmtDisUserTotal = new BigDecimal("0.0");
				try
				{
					pstmt = DB.prepareStatement (sqlPList, get_TrxName());
					rs = pstmt.executeQuery ();
					BigDecimal amtPurchase = null;
					BigDecimal amtSales = null;
					MCommissionAmt comAmt = null;
					//buscamos registro ya existente de comision
					int ID_CommAmt = DB.getSQLValue(get_TrxName(), "SELECT C_CommissionAmt_ID FROM C_CommissionAmt " +
							" WHERE IsActive = 'Y' AND SalesRep_ID="+rsSRep.getInt("SalesRep_ID")+" AND C_CommissionRun_ID = "+commisR.get_ID());
					while (rs.next ())
					{
						amtSales = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(i.GrandTotal) FROM C_Invoice i " +
								" WHERE i.IsSOTrx = 'Y' AND i.IsActive = 'Y' AND i.DocStatus IN ('CO','CL') " +
								" AND i.AD_Client_ID = "+commisR.getAD_Client_ID()+" AND DateInvoiced BETWEEN ? AND ? " +
								" AND i.AD_Org_ID = "+rs.getInt("AD_Org_ID"),commisR.getStartDate(),(Timestamp)commisR.get_Value("EndDate"));
						if(amtSales == null)
							amtSales = Env.ZERO;
						amtPurchase = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(i.GrandTotal) FROM C_Invoice i " +
								" WHERE i.IsSOTrx = 'N' AND i.IsActive = 'Y' AND i.DocStatus IN ('CO','CL') " +
								" AND i.AD_Client_ID = "+commisR.getAD_Client_ID()+" AND DateInvoiced BETWEEN ? AND ? " +
								" AND i.AD_Org_ID = "+rs.getInt("AD_Org_ID"),commisR.getStartDate(),(Timestamp)commisR.get_Value("EndDate"));
						if(amtPurchase == null)
							amtPurchase = Env.ZERO;
						//se verifica movimiento y se crea registro de CommissionAmt 
						if(amtSales.compareTo(Env.ZERO) != 0 || amtPurchase.compareTo(Env.ZERO) != 0)
						{							
							if(ID_CommAmt > 0)						
								comAmt = new MCommissionAmt(getCtx(), ID_CommAmt, get_TrxName());
							else
							{
								comAmt = new MCommissionAmt(getCtx(), 0, get_TrxName());
								comAmt.setC_CommissionRun_ID(p_CommissionR_ID);
								comAmt.setAD_Org_ID(commisR.getAD_Org_ID());
								comAmt.set_CustomColumn("SalesRep_ID", rs.getInt("SalesRep_ID"));
								comAmt.save();
							}
							//se genera detalle por org
							BigDecimal amtOrg = amtSales.subtract(amtPurchase);
							amtOrg = amtOrg.multiply(rs.getBigDecimal("Factor"));
							amtOrg = amtOrg.divide(Env.ONEHUNDRED, 0,RoundingMode.HALF_EVEN);
							MCommissionDetail comDet = new MCommissionDetail(getCtx(), 0, get_TrxName());
							comDet.setC_CommissionAmt_ID(comAmt.get_ID());
							comDet.setActualAmt(amtOrg);
							comDet.setC_Currency_ID(228);
							comDet.setAD_Org_ID(rs.getInt("AD_Org_ID"));
							comDet.setInfo("calculo comisión gerencia");
							comDet.save();					
							AmtTotalUser = AmtTotalUser.add(amtOrg);
							//sumamos comisiones de la org
							BigDecimal AmtComUser = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(cd.ActualAmt) FROM C_CommissionDetail cd " +
									" INNER JOIN C_CommissionAmt ca ON (cd.C_CommissionAmt_ID = ca.C_CommissionAmt_ID)" +
									" INNER JOIN C_CommissionRun cr ON (ca.C_CommissionRun_ID = cr.C_CommissionRun_ID)" +
									" WHERE cd.IsActive = 'Y' AND cd.AD_Org_ID="+rs.getInt("AD_Org_ID")+
									" AND cr.C_CommissionRun_ID="+commisR.get_ID()+" AND cd.C_Invoice_ID IS NOT NULL " +
									" AND cd.C_Payment_ID IS NULL");
							if(AmtComUser == null)
								AmtComUser = Env.ZERO;
							AmtComUserTotal = AmtComUserTotal.add(AmtComUser);
							//sumamos descuentos de la org
							BigDecimal AmtDisUser = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(cd.ActualAmt) FROM C_CommissionDetail cd " +
									" INNER JOIN C_CommissionAmt ca ON (cd.C_CommissionAmt_ID = ca.C_CommissionAmt_ID)" +
									" INNER JOIN C_CommissionRun cr ON (ca.C_CommissionRun_ID = cr.C_CommissionRun_ID)" +
									" WHERE cd.IsActive = 'Y' AND cd.AD_Org_ID="+rs.getInt("AD_Org_ID")+
									" AND cr.C_CommissionRun_ID="+commisR.get_ID()+" AND cd.C_Invoice_ID IS NOT NULL " +
									" AND cd.C_Payment_ID IS NOT NULL");
							if(AmtDisUser == null)
								AmtDisUser = Env.ZERO;
							AmtDisUserTotal = AmtDisUserTotal.add(AmtDisUser);							
						}
					}
					AmtTotalUser = AmtTotalUser.setScale(0, RoundingMode.HALF_EVEN);
					//fuera del ciclo de las org calculamos el cumplimiento
					if(AmtComUserTotal != null && AmtDisUserTotal != null && 
							AmtComUserTotal.compareTo(Env.ZERO) != 0 && AmtDisUserTotal.compareTo(Env.ZERO) != 0)
					{
						BigDecimal forDiscount =  AmtDisUserTotal.negate();
						forDiscount = forDiscount.divide(AmtComUserTotal,12,RoundingMode.HALF_EVEN);
						forDiscount = forDiscount.subtract(Env.ONE);
						forDiscount = forDiscount.negate();
						forDiscount = forDiscount.multiply(AmtTotalUser);
						forDiscount = forDiscount.setScale(0, RoundingMode.HALF_EVEN);
						//se genera linea con descuento por cumplimiento
						MCommissionDetail comDet = new MCommissionDetail(getCtx(), 0, get_TrxName());
						comDet.setC_CommissionAmt_ID(ID_CommAmt);
						comDet.setActualAmt(forDiscount.subtract(AmtTotalUser).setScale(0, RoundingMode.HALF_EVEN));
						comDet.setC_Currency_ID(228);
						comDet.setAD_Org_ID(commisR.getAD_Org_ID());
						comDet.setInfo("calculo castigo gerencia cumplimiento");
						comDet.save();		
					}					
					//generamos linea con descuento por incumplimiento
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
				finally
				{
					pstmt.close();
					if(rs != null)
						rs.close();	
					pstmt = null;
					rs = null;	
				}					
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		finally
		{
			pstmtSRep.close();
			if(rsSRep != null)
				rsSRep.close();	
			pstmtSRep = null;
			rsSRep = null;	
		}
		return "OK";
	}	//	doIt
}	//	Replenish
