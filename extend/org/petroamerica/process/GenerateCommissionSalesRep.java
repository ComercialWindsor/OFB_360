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

import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MCommissionAmt;
import org.compiere.model.MCommissionDetail;
import org.compiere.model.MCommissionRun;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	CopyFromJobStandar
 *	
 */
public class GenerateCommissionSalesRep extends SvrProcess
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
		//calculo de comisiones positivas
		MCommissionRun commisR = new MCommissionRun(getCtx(), p_CommissionR_ID, get_TrxName());
		String sqlPList = "SELECT ci.C_Invoice_ID,bp.SalesRep_ID " +
		//String sqlPList = "SELECT ci.C_Invoice_ID,ci.SalesRep_ID " +
				" FROM C_Invoice ci " +
				" INNER JOIN C_DocType dt ON (ci.C_DocTypeTarget_ID = dt.C_DocType_ID) " +
				" INNER JOIN C_Bpartner bp ON (ci.C_Bpartner_ID = bp.C_Bpartner_ID)" +
				" WHERE ci.IsSOTrx = 'Y' AND dt.DocBaseType IN ('ARI','ARC') " +
				" AND ci.IsActive = 'Y' AND ci.DocStatus IN ('CO','CL') AND ci.AD_Client_ID = "+commisR.getAD_Client_ID()+
				" AND ci.DateInvoiced BETWEEN ? AND ? " +
				" AND ci.C_DocType_ID NOT IN (1000077,1000003)"+
				//" AND C_Invoice_ID = 1010467 "+
				" AND ci.C_Invoice_ID NOT IN (SELECT C_Invoice_ID FROM C_CommissionDetail " +
				" WHERE C_Invoice_ID IS NOT NULL AND IsActive = 'Y') ORDER BY bp.SalesRep_ID DESC";				
				//" WHERE C_Invoice_ID IS NOT NULL AND IsActive = 'Y') ORDER BY ci.SalesRep_ID DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sqlPList, get_TrxName());
			pstmt.setTimestamp(1, commisR.getStartDate());
			pstmt.setTimestamp(2, (Timestamp)commisR.get_Value("EndDate"));
			rs = pstmt.executeQuery ();
			BigDecimal amtInv = null;
			MCommissionAmt comAmt = null;
			int ID_UserOld = 0;
			while (rs.next ())
			{
				amtInv = new BigDecimal("0.0");
				MInvoice inv = new MInvoice(getCtx(), rs.getInt("C_Invoice_ID"), get_TrxName());
				MBPartnerLocation iloc = new MBPartnerLocation(getCtx(), inv.getC_BPartner_Location_ID(), get_TrxName());
				//ininoles se cambia campo de diferencia que trae a peticion de mario
				//BigDecimal qtyCost = (BigDecimal)iloc.get_Value("Cost");
				//ininoles monto ahora sera comparacion con lista de precios
				BigDecimal qtyCost = (BigDecimal)iloc.get_Value("totalCost");
				if(qtyCost == null)
					qtyCost = Env.ZERO;
				//se busca version de lista de precios
				int M_PriceList_Version_ID = 0;
				if ( M_PriceList_Version_ID == 0 && inv.getM_PriceList_ID() > 0)
				{
					String sql = "SELECT plv.M_PriceList_Version_ID "
						+ "FROM M_PriceList_Version plv "
						+ "WHERE plv.M_PriceList_ID=? "						//	1
						+ " AND plv.ValidFrom <= ? "
						+ "ORDER BY plv.ValidFrom DESC";
					M_PriceList_Version_ID = DB.getSQLValueEx(null, sql, inv.getM_PriceList_ID(), inv.getDateInvoiced());
				}
				//si version de lista de precios es mayor a 0 se reemplaza el valor de diferencia
				if(M_PriceList_Version_ID > 0)
				{
					BigDecimal qtyCostOld = DB.getSQLValueBD(get_TrxName(), "SELECT MAX(PriceEntered -(SELECT PriceList FROM M_ProductPrice" +
							" WHERE M_PriceList_Version_ID = "+M_PriceList_Version_ID+" AND M_Product_ID = cil.M_Product_ID))" +
							" FROM C_InvoiceLine cil WHERE M_Product_ID > 0 AND IsActive = 'Y' AND C_Invoice_ID = "+inv.get_ID());
					if(qtyCostOld != null)
						qtyCost = qtyCostOld;
				}
				//se busca valor en referencia 
				int ID_ref = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Commission_ID) FROM C_Commission WHERE LOWER(NAME) LIKE '%escala comisi%'");
				BigDecimal amtCost = DB.getSQLValueBD(get_TrxName(), "SELECT MAX(AmtSubtract) FROM C_CommissionLine " +
						" WHERE C_Commission_ID = "+ID_ref+" AND qtySubtract = ?", qtyCost.setScale(0, RoundingMode.HALF_EVEN));
				if(amtCost == null)
					amtCost = Env.ZERO;
				//se crea total de comision				
				if(ID_UserOld != rs.getInt("SalesRep_ID"))
				{
					//antes de crear buscamos si existe un registro para el SalesRep
					int ID_CommAmt = DB.getSQLValue(get_TrxName(), "SELECT C_CommissionAmt_ID FROM C_CommissionAmt " +
							" WHERE IsActive = 'Y' AND SalesRep_ID="+rs.getInt("SalesRep_ID")+" AND C_CommissionRun_ID = "+commisR.get_ID());
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
				}
				ID_UserOld = rs.getInt("SalesRep_ID");
				//se genera detalle por factura
				MInvoiceLine[] lines = inv.getLines(false);
				for (int i = 0; i < lines.length; i++)
				{
					MInvoiceLine iline = lines[i];			
					iline.set_CustomColumn("CommissionAmt",amtCost.multiply(iline.getQtyInvoiced()));
					iline.set_CustomColumn("CommissionCost",qtyCost);
					iline.save();
					amtInv = amtInv.add(amtCost.multiply(iline.getQtyInvoiced()));		
				}
				MCommissionDetail comDet = new MCommissionDetail(getCtx(), 0, get_TrxName());
				comDet.setC_CommissionAmt_ID(comAmt.get_ID());
				//se revisa si es nota de credito para que reste en vez de sumar
				if(inv.getC_DocType().getDocBaseType().compareTo("ARC") == 0)
					amtInv = amtInv.negate();
				comDet.setActualAmt(amtInv);
				comDet.setC_Currency_ID(inv.getC_Currency_ID());
				comDet.set_CustomColumn("C_Invoice_ID", inv.get_ID());
				comDet.setAD_Org_ID(inv.getAD_Org_ID());
				comDet.save();
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		finally
		{
			pstmt.close();
			rs.close();	
			pstmt = null;
			rs = null;	
		}		
		commitEx();
		//calculo de reversos de comisiones
		String sqlRev = "SELECT ca.C_Invoice_ID,extract(day from (cp.datetrx - i.duedate)) as days,cp.C_Payment_ID,bp.SalesRep_ID" +
				" FROM C_Payment cp " +
				" INNER JOIN C_AllocationLine ca ON (cp.C_Payment_ID = ca.C_Payment_ID)" +
				" INNER JOIN C_AllocationHdr a ON (a.C_AllocationHdr_ID = ca.C_AllocationHdr_ID) " +
				" INNER JOIN C_Invoice_v i ON (ca.C_Invoice_ID = i.C_Invoice_ID) " +
				" INNER JOIN C_BPartner bp ON (i.C_BPartner_ID = bp.C_BPartner_ID) " +
				" WHERE i.IsSOTrx = 'Y' " +
				" AND cp.IsActive = 'Y' AND a.DocStatus IN ('CO','CL') AND i.AD_Client_ID = "+commisR.getAD_Client_ID()+
				" AND cp.DateTrx BETWEEN ? AND ? " +
				" AND (select count(1) FROM C_CommissionDetail cm WHERE cm.isactive = 'Y' AND cm.C_Invoice_ID = i.C_Invoice_ID) = 1 " +
				" AND invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID) <= 0 AND extract(day from (cp.datetrx - i.duedate)) > 7 " +
				" ORDER BY bp.SalesRep_ID DESC";
		PreparedStatement pstmtRev = null;
		ResultSet rsRev = null;
		try
		{
			pstmtRev = DB.prepareStatement (sqlRev, get_TrxName());
			pstmtRev.setTimestamp(1, commisR.getStartDate());
			pstmtRev.setTimestamp(2, (Timestamp)commisR.get_Value("EndDate"));
			rsRev = pstmtRev.executeQuery ();
			BigDecimal amtRev = null;
			while (rsRev.next())
			{
				amtRev = new BigDecimal("0.0");
				BigDecimal days = rsRev.getBigDecimal("days");
				//buscamos % a descontar
				int ID_Comm = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Commission_ID) FROM C_Commission WHERE LOWER(NAME) LIKE '%reverso de comisi%'");
				BigDecimal pDiscount = DB.getSQLValueBD(get_TrxName(), "SELECT MAX(AmtSubtract) FROM C_CommissionLine " +
						" WHERE C_Commission_ID = "+ID_Comm+" AND ? BETWEEN QtySubTract AND QtyMultiplier", days.setScale(0, RoundingMode.HALF_EVEN));
				if(pDiscount == null)
					pDiscount = Env.ZERO;
				//buscamos registro ya existente de comision
				int ID_CommDetOld = DB.getSQLValue(get_TrxName(), "SELECT C_CommissionDetail_ID FROM C_CommissionDetail " +
						" WHERE IsActive = 'Y' AND C_Invoice_ID = "+rsRev.getInt("C_Invoice_ID"));
				//solo si hay porcentaje a descontar y registro de comision antes creado se realiza la reversa
				if(ID_CommDetOld > 0 && pDiscount.compareTo(Env.ZERO) != 0)
				{
					MCommissionDetail comDetOld = new MCommissionDetail(getCtx(), ID_CommDetOld, get_TrxName());
					//buscamos registro dentro del mismo calculo de comisión
					int ID_ComAmtOld = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_CommissionAmt_ID) FROM C_CommissionAmt WHERE " +
							" C_CommissionRun_ID = "+commisR.get_ID()+" AND SalesRep_ID = "+rsRev.getInt("SalesRep_ID"));
					if(ID_ComAmtOld <= 0)
					{
						MCommissionAmt comAmtNew = new MCommissionAmt(getCtx(), 0, get_TrxName());
						comAmtNew.setAD_Org_ID(commisR.getAD_Org_ID());
						comAmtNew.setC_CommissionRun_ID(commisR.get_ID());
						comAmtNew.set_CustomColumn("SalesRep_ID",rsRev.getInt("SalesRep_ID"));
						comAmtNew.save();
						ID_ComAmtOld = comAmtNew.get_ID();
					}	
					MCommissionDetail comDetnew = new MCommissionDetail(getCtx(), 0, get_TrxName());
					comDetnew.setC_CommissionAmt_ID(ID_ComAmtOld);
					comDetnew.setC_Currency_ID(comDetOld.getC_Currency_ID());
					comDetnew.set_CustomColumn("C_Invoice_ID",comDetOld.get_ValueAsInt("C_Invoice_ID"));
					comDetnew.set_CustomColumn("C_Payment_ID",rsRev.getInt("C_Payment_ID"));
					//se calcula monto a descontar
					amtRev = comDetOld.getActualAmt().multiply(pDiscount);
					amtRev = amtRev.divide(Env.ONEHUNDRED).negate();
					comDetnew.setActualAmt(amtRev);					
					comDetnew.setAD_Org_ID(comDetOld.getAD_Org_ID());
					comDetnew.save();					
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		finally
		{
			pstmtRev.close();
			rsRev.close();	
			pstmtRev = null;
			rsRev = null;	
		}		
		return "OK";
	}	//	doIt
}	//	Replenish
