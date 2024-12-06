/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.ofb.process;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.process.SvrProcess;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * Special Process
 * 
 * @author Fabian Aguilar faaguilar
 * @version $Id: DepreAssets.java,v 1.3 2006/02/24 07:15:43 jjanke Exp $
 */
public class DepreAssetsDefensoria extends SvrProcess {

	/** Properties */
	private Properties m_ctx;
	/* Order ID */
	private int Record_ID;

	X_A_Asset_Dep DepDoc = null;

	protected void prepare() {

		m_ctx = Env.getCtx();
		Record_ID = getRecord_ID();
		DepDoc = new X_A_Asset_Dep(m_ctx, Record_ID, get_TrxName());

	} // prepare

	/**
	 * Proccess
	 * 
	 * @return info
	 * @throws Exception
	 */
	protected String doIt() throws Exception {
		Timestamp datefrom = null;
		Timestamp dateto = null;
		
		//replanificacion si es que es una correcion DPP
		if (DepDoc.getDepType().equalsIgnoreCase("DCM") && DepDoc.getDocStatus().equals("DR"))
		{
			replanningDPP(0, DepDoc.getAD_Org_ID(), DepDoc.getA_Asset_Group_ID(), "Y");
			this.commitEx();
		}

		String sql = "select a.a_asset_id,a.name,a.value,acct.c_acctschema_id, acct.a_asset_acct, "
				+ " acct.a_depreciation_acct, fore.amount,fore.a_asset_forecast_id,p.c_period_id,p.name,"
				+ " acct.a_accumdepreciation_acct,acct.A_Disposal_Gain_Acct,wk.a_asset_cost,acct.A_Disposal_RevenueC_Acct,"
				+ " acct.A_Disposal_RevenueD_Acct,acct.A_Disposal_Loss_Acct, wk.a_accumulated_depr, acct.A_AssetComplement_Acct"
				+ " from a_asset a"
				+ " inner join a_asset_acct acct on (a.a_asset_id=acct.a_asset_id)"
				+ " inner join A_Depreciation_Workfile wk on (a.a_asset_id=wk.a_asset_id)"
				+ " inner join a_asset_forecast fore on (a.a_asset_id=fore.a_asset_id)"
				+ " inner join c_period p on (fore.datedoc between p.startdate and p.enddate)"
				+ " where a.ad_client_id=? and p.c_period_id=? and acct.IsActive='Y' ";

		if (DepDoc.isSameYear()) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			datefrom = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 31);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateto = new Timestamp(cal.getTimeInMillis());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			datefrom = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 31);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateto = new Timestamp(cal.getTimeInMillis());
		}
		if (DepDoc.getDepType().equalsIgnoreCase(
				(X_A_Asset_Dep.DEPTYPE_Depreciacion)))
			sql += " And fore.processed='N' and a.isinposession='Y' And a.a_asset_group_id="
					+ DepDoc.getA_Asset_Group_ID();

		if (DepDoc.getDepType().equalsIgnoreCase(
				(X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria)))
			sql += " And fore.Corrected='N' And a.a_asset_group_id="
					+ DepDoc.getA_Asset_Group_ID();
		// sql+=" And a.assetservicedate between ? and ? And fore.Corrected='N' And a.a_asset_group_id="+DepDoc.getA_Asset_Group_ID();

		if (DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Reevaluacion)) || DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Deteriodo)))
			if (DepDoc.getA_Asset_ID() > 0) 
			{
				sql += " And wk.a_asset_cost>wk.a_accumulated_depr+2 And a.A_Asset_ID="
						+ DepDoc.getA_Asset_ID()
						+ " and fore.amount > 0  and a.isinposession="
						+ (DepDoc.isInPosession() ? "'Y'" : "'N'");
			} 
			else 
			{
				sql += " And wk.a_asset_cost>wk.a_accumulated_depr+2 And a.a_asset_group_id="
						+ DepDoc.getA_Asset_Group_ID()
						+ " and fore.amount > 0 and a.isinposession="
						+ (DepDoc.isInPosession() ? "'Y'" : "'N'");
			}

		/*if (DepDoc.getDepType().equalsIgnoreCase(
				X_A_Asset_Dep.DEPTYPE_VentaActivo))
			sql += " And a.A_Asset_ID=" + DepDoc.getA_Asset_ID();
		 */
		if(DepDoc.getDepType().equalsIgnoreCase(X_A_Asset_Dep.DEPTYPE_VentaActivo)) //ininoles nuevo proceso de venta con ID de factura
		{
			int id_invoice = 0; 
			try
			{ 
				id_invoice = DepDoc.get_ValueAsInt("C_Invoice_ID");
			}
			catch(Exception e)
			{
				id_invoice = 0;
				log.log(Level.SEVERE, sql.toString(), e);			
			}
			if (id_invoice > 0)
			{
				sql+= " And a.A_Asset_ID IN (SELECT DISTINCT(A_Asset_ID) FROM C_InvoiceLine WHERE C_Invoice_ID = "+DepDoc.get_ValueAsInt("C_Invoice_ID")+")" ;			
			}
			else
			{
				sql+= " And a.A_Asset_ID="+DepDoc.getA_Asset_ID();
			}		
		}
		
		if (DepDoc.getDepType().equalsIgnoreCase("CRN"))
			sql += " And fore.Corrected='N' And a.a_asset_group_id="
					+ DepDoc.getA_Asset_Group_ID();
		// sql+=" And a.assetservicedate between ? and ? And fore.Corrected='N' And a.a_asset_group_id="+DepDoc.getA_Asset_Group_ID();

		if (DepDoc.getDepType().equalsIgnoreCase("DCM"))
			sql += " And fore.Corrected='N' and a.isinposession='Y' and a.a_asset_group_id="
					+ DepDoc.getA_Asset_Group_ID();

		if (DepDoc.getAD_Org_ID() > 0)
		{
			sql += " and a.AD_Org_ID=" + DepDoc.getAD_Org_ID();
		}

		sql += " and a.IsInPosession = 'Y' and a.IsActive = 'Y' ";

		PreparedStatement pstmt = null;
		log.config("SQL: "+sql);

		MJournalBatch batch = null;
		MJournal journal = null;
		MClient client = MClient.get(m_ctx, Env.getAD_Client_ID(m_ctx));
		int count = 0;
		if (DepDoc.getDocStatus().equals("DR")) {
			try {
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, DepDoc.getAD_Client_ID());
				// ininoles que solo tome un periodo para la reevaluacion
				if (DepDoc.getDepType().equalsIgnoreCase(
						X_A_Asset_Dep.DEPTYPE_Reevaluacion)) {
					int period_ID = DB
							.getSQLValue(
									get_TrxName(),
									"select max(p.c_period_id) from A_Asset_Forecast fore "
											+ "inner join c_period p on (fore.datedoc between p.startdate and p.enddate)"
											+ "where A_Asset_ID = "
											+ DepDoc.getA_Asset_ID());
					pstmt.setInt(2, period_ID);
				} else {
					pstmt.setInt(2, DepDoc.getC_Period_ID());
				}

				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					if (batch == null) {
						batch = new MJournalBatch(m_ctx, 0, get_TrxName());
						batch.setAD_Org_ID(DepDoc.getAD_Org_ID());
						batch.setDescription("Assets Deprecation Process");
						batch.setPostingType("A");
						batch.setC_DocType_ID(MDocType.getOfDocBaseType(m_ctx,
								"GLJ")[0].getC_DocType_ID());
						batch.setGL_Category_ID(MGLCategory.getDefault(m_ctx,
								MGLCategory.CATEGORYTYPE_Manual)
								.getGL_Category_ID());
						batch.setDateAcct(DepDoc.getDateDoc());
						batch.setDateDoc(DepDoc.getDateDoc());
						batch.setC_Period_ID(MPeriod.getC_Period_ID(m_ctx,
								DepDoc.getDateAcct()));
						batch.setC_Currency_ID(client.getC_Currency_ID());
						if (!batch.save()) {
							return "la transaccion no puede ser generara para *";
						}
					}
					count++;
					journal = new MJournal(batch);
					journal.setDescription(rs.getString("value") + "-"
							+ rs.getString("name"));
					journal.setC_AcctSchema_ID(MAcctSchema.getClientAcctSchema(
							m_ctx, Env.getAD_Client_ID(m_ctx))[0]
							.getC_AcctSchema_ID());
					journal.setGL_Category_ID(MGLCategory.getDefault(m_ctx,
							MGLCategory.CATEGORYTYPE_Document)
							.getGL_Category_ID());
					journal.setC_ConversionType_ID(114);
					journal.set_ValueOfColumn("A_Asset_ID",
							rs.getInt("a_asset_id"));
					journal.set_ValueOfColumn("A_Asset_Forecast_ID",
							rs.getInt("a_asset_forecast_id"));
					journal.save();

					if (DepDoc.getDepType().equalsIgnoreCase(
							(X_A_Asset_Dep.DEPTYPE_Depreciacion))) { // depreciacion
						MJournalLine line1 = new MJournalLine(journal);
						line1.setA_Asset_ID(rs.getInt("a_asset_id"));
						line1.setAmtSourceDr(rs.getBigDecimal("amount"));
						line1.setAmtSourceCr(Env.ZERO);
						line1.setAmtAcct(rs.getBigDecimal("amount"), Env.ZERO);
						line1.setC_ValidCombination_ID(rs
								.getInt("a_depreciation_acct"));
						line1.save();

						MJournalLine line2 = new MJournalLine(journal);
						line2.setA_Asset_ID(rs.getInt("a_asset_id"));
						line2.setAmtSourceDr(Env.ZERO);
						line2.setAmtSourceCr(rs.getBigDecimal("amount"));
						line2.setAmtAcct(Env.ZERO, rs.getBigDecimal("amount"));
						// line2.setC_ValidCombination_ID(rs.getInt("a_asset_acct"));
						// // old
						line2.setC_ValidCombination_ID(rs
								.getInt("A_AssetComplement_Acct")); // new
						line2.save();
					} else if (DepDoc.getDepType().equalsIgnoreCase(
							(X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria))) {// correccion
																			// monetaria
						batch.setDescription("Correcion Monetaria");
						MJournalLine line1 = new MJournalLine(journal);
						line1.setA_Asset_ID(rs.getInt("a_asset_id"));
						if (DepDoc.getRate().signum() > 0) {
							line1.setAmtSourceDr(rs.getBigDecimal(
									"a_accumulated_depr").multiply(
									DepDoc.getRate().divide(Env.ONEHUNDRED)));
							line1.setAmtSourceCr(Env.ZERO);
							line1.setAmtAcct(
									rs.getBigDecimal("a_accumulated_depr")
											.multiply(
													DepDoc.getRate().divide(
															Env.ONEHUNDRED)),
									Env.ZERO);
						} else if (DepDoc.getRate().signum() < 0) {
							line1.setAmtSourceDr(Env.ZERO);
							line1.setAmtSourceCr(rs.getBigDecimal(
									"a_accumulated_depr").multiply(
									DepDoc.getRate().abs()
											.divide(Env.ONEHUNDRED)));
							line1.setAmtAcct(
									Env.ZERO,
									rs.getBigDecimal("a_accumulated_depr")
											.multiply(
													DepDoc.getRate()
															.abs()
															.divide(Env.ONEHUNDRED)));
						}
						line1.setC_ValidCombination_ID(rs
								.getInt("a_depreciation_acct"));
						line1.save();

						MJournalLine line2 = new MJournalLine(journal);
						line2.setA_Asset_ID(rs.getInt("a_asset_id"));
						if (DepDoc.getRate().signum() > 0) {
							line2.setAmtSourceDr(Env.ZERO);
							line2.setAmtSourceCr(rs.getBigDecimal(
									"a_accumulated_depr").multiply(
									DepDoc.getRate().divide(Env.ONEHUNDRED)));
							line2.setAmtAcct(
									Env.ZERO,
									rs.getBigDecimal("a_accumulated_depr")
											.multiply(
													DepDoc.getRate().divide(
															Env.ONEHUNDRED)));
						}
						if (DepDoc.getRate().signum() < 0) {
							line2.setAmtSourceDr(rs.getBigDecimal(
									"a_accumulated_depr").multiply(
									DepDoc.getRate().abs()
											.divide(Env.ONEHUNDRED)));
							line2.setAmtSourceCr(Env.ZERO);
							line2.setAmtAcct(
									rs.getBigDecimal("a_accumulated_depr")
											.multiply(
													DepDoc.getRate()
															.abs()
															.divide(Env.ONEHUNDRED)),
									Env.ZERO);
						}
						line2.setC_ValidCombination_ID(rs
								.getInt("a_accumdepreciation_acct"));
						line2.save();

						MJournalLine line3 = new MJournalLine(journal);
						line3.setA_Asset_ID(rs.getInt("a_asset_id"));
						if (DepDoc.getRate().signum() > 0) {
							line3.setAmtSourceDr(rs.getBigDecimal(
									"a_asset_cost").multiply(
									DepDoc.getRate().divide(Env.ONEHUNDRED)));
							line3.setAmtSourceCr(Env.ZERO);
							line3.setAmtAcct(
									rs.getBigDecimal("a_asset_cost").multiply(
											DepDoc.getRate().divide(
													Env.ONEHUNDRED)), Env.ZERO);
						}
						if (DepDoc.getRate().signum() < 0) {
							line3.setAmtSourceDr(Env.ZERO);
							line3.setAmtSourceCr(rs.getBigDecimal(
									"a_asset_cost").multiply(
									DepDoc.getRate().abs()
											.divide(Env.ONEHUNDRED)));
							line3.setAmtAcct(
									Env.ZERO,
									rs.getBigDecimal("a_asset_cost").multiply(
											DepDoc.getRate().abs()
													.divide(Env.ONEHUNDRED)));
						}
						line3.setC_ValidCombination_ID(rs
								.getInt("a_asset_acct"));
						line3.save();

						MJournalLine line4 = new MJournalLine(journal);
						line4.setA_Asset_ID(rs.getInt("a_asset_id"));
						if (DepDoc.getRate().signum() > 0) {
							line4.setAmtSourceDr(Env.ZERO);
							line4.setAmtSourceCr(rs.getBigDecimal(
									"a_asset_cost").multiply(
									DepDoc.getRate().divide(Env.ONEHUNDRED)));
							line4.setAmtAcct(
									Env.ZERO,
									rs.getBigDecimal("a_asset_cost").multiply(
											DepDoc.getRate().divide(
													Env.ONEHUNDRED)));
						}
						if (DepDoc.getRate().signum() < 0) {
							line4.setAmtSourceDr(rs.getBigDecimal(
									"a_asset_cost").multiply(
									DepDoc.getRate().abs()
											.divide(Env.ONEHUNDRED)));
							line4.setAmtSourceCr(Env.ZERO);
							line4.setAmtAcct(
									rs.getBigDecimal("a_asset_cost").multiply(
											DepDoc.getRate().abs()
													.divide(Env.ONEHUNDRED)),
									Env.ZERO);
						}
						line4.setC_ValidCombination_ID(rs
								.getInt("a_accumdepreciation_acct"));
						line4.save();

					} else if (DepDoc.getDepType().equalsIgnoreCase(
							(X_A_Asset_Dep.DEPTYPE_Reevaluacion))) { // Reevaluacion
						batch.setDescription("Reevaluacion Activos");
						BigDecimal newAmount = new BigDecimal(0.0);

						// ininoles usar campo monto por defecto

						if (((BigDecimal) DepDoc.get_Value("Amt"))
								.compareTo(Env.ZERO) != 0) {
							newAmount = (BigDecimal) DepDoc.get_Value("Amt");
						} else // si campo monto es cero usar tasa
						{
							newAmount = rs.getBigDecimal("a_asset_cost")
									.multiply(
											DepDoc.getRate().divide(
													Env.ONEHUNDRED));
						}

						MJournalLine line1 = new MJournalLine(journal);
						line1.setA_Asset_ID(rs.getInt("a_asset_id"));
						line1.setAmtSourceDr(newAmount);
						line1.setAmtSourceCr(Env.ZERO);
						line1.setAmtAcct(newAmount, Env.ZERO);
						line1.setC_ValidCombination_ID(rs
								.getInt("a_asset_acct"));
						line1.save();

						MJournalLine line2 = new MJournalLine(journal);
						line2.setA_Asset_ID(rs.getInt("a_asset_id"));
						line2.setAmtSourceDr(Env.ZERO);
						line2.setAmtSourceCr(newAmount);
						line2.setAmtAcct(Env.ZERO, newAmount);
						line2.setC_ValidCombination_ID(rs
								.getInt("A_Disposal_Gain_Acct"));
						line2.save();
					} else if (DepDoc.getDepType().equalsIgnoreCase(
							(X_A_Asset_Dep.DEPTYPE_Deteriodo))) {// Deteriodo
						batch.setDescription("Deteriodo Activos");
						BigDecimal newAmount = new BigDecimal(0.0);
						newAmount = rs.getBigDecimal("a_asset_cost").multiply(
								DepDoc.getRate().divide(Env.ONEHUNDRED));

						MJournalLine line1 = new MJournalLine(journal);
						line1.setA_Asset_ID(rs.getInt("a_asset_id"));
						line1.setAmtSourceDr(newAmount);
						line1.setAmtSourceCr(Env.ZERO);
						line1.setAmtAcct(newAmount, Env.ZERO);
						line1.setC_ValidCombination_ID(rs
								.getInt("A_Disposal_Loss_Acct"));
						line1.save();

						MJournalLine line2 = new MJournalLine(journal);
						line2.setA_Asset_ID(rs.getInt("a_asset_id"));
						line2.setAmtSourceDr(Env.ZERO);
						line2.setAmtSourceCr(newAmount);
						line2.setAmtAcct(Env.ZERO, newAmount);
						line2.setC_ValidCombination_ID(rs
								.getInt("a_asset_acct"));
						line2.save();
					} else if (DepDoc.getDepType().equalsIgnoreCase(
							(X_A_Asset_Dep.DEPTYPE_VentaActivo))) { // venta
																	// activo
						BigDecimal newAmount = new BigDecimal(0.0);
						newAmount = rs.getBigDecimal("a_asset_cost").subtract(
								rs.getBigDecimal("a_accumulated_depr"));
						newAmount = newAmount.add(Env.ONE);

						MJournalLine line1 = new MJournalLine(journal);
						line1.setA_Asset_ID(DepDoc.getA_Asset_ID());
						line1.setAmtSourceDr(newAmount);
						line1.setAmtSourceCr(Env.ZERO);
						line1.setAmtAcct(newAmount, Env.ZERO);
						line1.setC_ValidCombination_ID(rs
								.getInt("a_depreciation_acct"));
						line1.save();

						MJournalLine line2 = new MJournalLine(journal);
						line2.setA_Asset_ID(DepDoc.getA_Asset_ID());
						line2.setAmtSourceDr(Env.ZERO);
						line2.setAmtSourceCr(newAmount);
						line2.setAmtAcct(Env.ZERO, newAmount);
						line2.setC_ValidCombination_ID(rs
								.getInt("a_asset_acct"));
						line2.save();

						MJournalLine line3 = new MJournalLine(journal);
						line3.setA_Asset_ID(DepDoc.getA_Asset_ID());
						line3.setAmtSourceDr(DepDoc.getRate());
						line3.setAmtSourceCr(Env.ZERO);
						line3.setAmtAcct(DepDoc.getRate(), Env.ZERO);
						line3.setC_ValidCombination_ID(rs
								.getInt("A_Disposal_RevenueD_Acct"));
						line3.save();

						MJournalLine line4 = new MJournalLine(journal);
						line4.setA_Asset_ID(DepDoc.getA_Asset_ID());
						line4.setAmtSourceDr(Env.ZERO);
						line4.setAmtSourceCr(DepDoc.getRate());
						line4.setAmtAcct(Env.ZERO, DepDoc.getRate());
						line4.setC_ValidCombination_ID(rs
								.getInt("A_Disposal_RevenueC_Acct"));
						line4.save();
					} else if (DepDoc.getDepType().equalsIgnoreCase("CRN")) {// ininoles
																				// nueva
																				// correccion
																				// monetaria
																				// para
																				// prototipo
																				// DPP
						batch.setDescription("Correcion Monetaria");
						MJournalLine line1 = new MJournalLine(journal);
						line1.setA_Asset_ID(rs.getInt("a_asset_id"));
						if (DepDoc.getRate().signum() > 0) {
							line1.setAmtSourceDr(rs.getBigDecimal(
									"a_accumulated_depr").multiply(
									DepDoc.getRate().divide(Env.ONEHUNDRED)));
							line1.setAmtSourceCr(Env.ZERO);
							line1.setAmtAcct(
									rs.getBigDecimal("a_accumulated_depr")
											.multiply(
													DepDoc.getRate().divide(
															Env.ONEHUNDRED)),
									Env.ZERO);
						} else if (DepDoc.getRate().signum() < 0) {
							line1.setAmtSourceDr(Env.ZERO);
							line1.setAmtSourceCr(rs.getBigDecimal(
									"a_accumulated_depr").multiply(
									DepDoc.getRate().abs()
											.divide(Env.ONEHUNDRED)));
							line1.setAmtAcct(
									Env.ZERO,
									rs.getBigDecimal("a_accumulated_depr")
											.multiply(
													DepDoc.getRate()
															.abs()
															.divide(Env.ONEHUNDRED)));
						}
						line1.setC_ValidCombination_ID(rs
								.getInt("a_accumdepreciation_acct"));
						line1.save();

						MJournalLine line2 = new MJournalLine(journal);
						line2.setA_Asset_ID(rs.getInt("a_asset_id"));
						if (DepDoc.getRate().signum() > 0) {
							line2.setAmtSourceDr(Env.ZERO);
							line2.setAmtSourceCr(rs.getBigDecimal(
									"a_accumulated_depr").multiply(
									DepDoc.getRate().divide(Env.ONEHUNDRED)));
							line2.setAmtAcct(
									Env.ZERO,
									rs.getBigDecimal("a_accumulated_depr")
											.multiply(
													DepDoc.getRate().divide(
															Env.ONEHUNDRED)));
						}
						if (DepDoc.getRate().signum() < 0) {
							line2.setAmtSourceDr(rs.getBigDecimal(
									"a_accumulated_depr").multiply(
									DepDoc.getRate().abs()
											.divide(Env.ONEHUNDRED)));
							line2.setAmtSourceCr(Env.ZERO);
							line2.setAmtAcct(
									rs.getBigDecimal("a_accumulated_depr")
											.multiply(
													DepDoc.getRate()
															.abs()
															.divide(Env.ONEHUNDRED)),
									Env.ZERO);
						}
						line2.setC_ValidCombination_ID(rs
								.getInt("a_asset_acct"));
						line2.save();
					} else if (DepDoc.getDepType().equalsIgnoreCase("DCM")) // replnificacion +
																			// depreciacion
																			// +
																			// corecion
																			// DPP
					{

						// depreciacion
						MJournalLine line1 = new MJournalLine(journal);
						line1.setA_Asset_ID(rs.getInt("a_asset_id"));
						line1.setAmtSourceDr(rs.getBigDecimal("amount"));
						line1.setAmtSourceCr(Env.ZERO);
						line1.setAmtAcct(rs.getBigDecimal("amount"), Env.ZERO);
						line1.setC_ValidCombination_ID(rs.getInt("a_depreciation_acct"));
						line1.save();

						MJournalLine line2 = new MJournalLine(journal);
						line2.setA_Asset_ID(rs.getInt("a_asset_id"));
						line2.setAmtSourceDr(Env.ZERO);
						line2.setAmtSourceCr(rs.getBigDecimal("amount"));
						line2.setAmtAcct(Env.ZERO, rs.getBigDecimal("amount"));
						// line2.setC_ValidCombination_ID(rs.getInt("a_asset_acct"));
						// // old
						line2.setC_ValidCombination_ID(rs.getInt("A_AssetComplement_Acct")); // new
						line2.save();

						// correcion monetaria DPP
						batch.setDescription("Depreciacion y Correcion Monetaria");
						MJournalLine line3 = new MJournalLine(journal);
						line3.setA_Asset_ID(rs.getInt("a_asset_id"));
						if (DepDoc.getRate().signum() > 0) 
						{
							line3.setAmtSourceDr(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)));
							line3.setAmtSourceCr(Env.ZERO);
							line3.setAmtAcct(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)),Env.ZERO);
						} else if (DepDoc.getRate().signum() < 0) 
						{
							line3.setAmtSourceDr(Env.ZERO);
							line3.setAmtSourceCr(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)));
							line3.setAmtAcct(Env.ZERO,rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)));
						}
						line3.setC_ValidCombination_ID(rs.getInt("a_accumdepreciation_acct"));
						line3.save();

						MJournalLine line4 = new MJournalLine(journal);
						line4.setA_Asset_ID(rs.getInt("a_asset_id"));
						if (DepDoc.getRate().signum() > 0) 
						{
							line4.setAmtSourceDr(Env.ZERO);
							line4.setAmtSourceCr(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)));
							line4.setAmtAcct(Env.ZERO,rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().divide(Env.ONEHUNDRED)));
						}
						if (DepDoc.getRate().signum() < 0) 
						{
							line4.setAmtSourceDr(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)));
							line4.setAmtSourceCr(Env.ZERO);
							line4.setAmtAcct(rs.getBigDecimal("a_accumulated_depr").multiply(DepDoc.getRate().abs().divide(Env.ONEHUNDRED)),Env.ZERO);
						}
						line4.setC_ValidCombination_ID(rs.getInt("a_asset_acct"));
						line4.save();
					}
				}
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				log.log(Level.SEVERE, sql.toString(), e);
			}

			if (count > 0) {
				DepDoc.setGL_JournalBatch_ID(batch.getGL_JournalBatch_ID());
				DepDoc.setDocStatus("IP");
				DepDoc.save();

				DB.executeUpdate(
						"Update GL_JournalLine set A_Asset_Dep_ID="
								+ DepDoc.getA_Asset_Dep_ID()
								+ " Where GL_Journal_ID IN (select GL_Journal_ID from GL_Journal where GL_JournalBatch_ID="
								+ batch.getGL_JournalBatch_ID() + " )",
						get_TrxName());
			} else {
				if (batch != null)
					batch.delete(true, get_TrxName());
			}

		} else if (DepDoc.getDocStatus().equals("IP") && !DepDoc.isVoid()) {
			log.info("IP");

			batch = new MJournalBatch(m_ctx, DepDoc.getGL_JournalBatch_ID(),
					get_TrxName());
			batch.processIt("CO");
			batch.save();
			batch.setProcessed(true);
			batch.save();

			MJournal[] journals = batch.getJournals(true);
			for (int i = 0; i < journals.length; i++) {
				MJournal jour = journals[i];
				X_A_Asset asset = new X_A_Asset(getCtx(),
						jour.get_ValueAsInt("A_Asset_ID"), null);

				if (DepDoc.getDepType().equalsIgnoreCase(
						(X_A_Asset_Dep.DEPTYPE_Depreciacion))) {
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Accumulated_Depr(workfile
							.getA_Accumulated_Depr().add(jour.getTotalCr()));
					workfile.setA_Period_Posted(workfile.getA_Period_Posted() + 1);
					workfile.setAssetDepreciationDate(DepDoc.getDateDoc());
					workfile.save();

					X_A_Asset_Forecast fore = new X_A_Asset_Forecast(
							Env.getCtx(),
							jour.get_ValueAsInt("A_Asset_Forecast_ID"), null);
					fore.setProcessed(true);
					fore.setGL_Journal_ID(jour.getGL_Journal_ID());
					fore.save();

					asset.setLastMaintenanceNote("Depreciacion Periodo :"
							+ (workfile.getA_Period_Posted() + 1) + " por "
							+ jour.getTotalCr());
				} else if (DepDoc.getDepType().equalsIgnoreCase(
						(X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria))) {
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					MAssetAcct group = new MAssetAcct(getCtx(),
							MAssetAcct.getAssetAcct_ID(jour
									.get_ValueAsInt("A_Asset_ID")),
							get_TrxName());
					BigDecimal acum = DB.getSQLValueBD(
							get_TrxName(),
							"select sum(AmtAcctDr) from gl_journalline where gl_journal_id="
									+ jour.getGL_Journal_ID()
									+ " and C_ValidCombination_ID="
									+ group.getA_Depreciation_Acct());
					acum = acum.subtract(DB.getSQLValueBD(
							get_TrxName(),
							"select sum(AmtAcctCr) from gl_journalline where gl_journal_id="
									+ jour.getGL_Journal_ID()
									+ " and C_ValidCombination_ID="
									+ group.getA_Depreciation_Acct()));

					BigDecimal neto = DB.getSQLValueBD(
							get_TrxName(),
							"select sum(AmtAcctDr) from gl_journalline where gl_journal_id="
									+ jour.getGL_Journal_ID()
									+ " and C_ValidCombination_ID="
									+ group.getA_Asset_Acct());
					neto = neto.subtract(DB.getSQLValueBD(
							get_TrxName(),
							"select sum(AmtAcctCr) from gl_journalline where gl_journal_id="
									+ jour.getGL_Journal_ID()
									+ " and C_ValidCombination_ID="
									+ group.getA_Asset_Acct()));

					workfile.setA_Accumulated_Depr(workfile
							.getA_Accumulated_Depr().add(acum));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().add(
							neto));
					workfile.save();

					X_A_Asset_Forecast fore = new X_A_Asset_Forecast(
							Env.getCtx(),
							jour.get_ValueAsInt("A_Asset_Forecast_ID"), null);
					fore.setCorrected(true);
					fore.save();

					asset.setLastMaintenanceNote("CM Periodo "
							+ DepDoc.getRate() + " % Valor:" + neto);
				} else if (DepDoc.getDepType().equalsIgnoreCase(
						(X_A_Asset_Dep.DEPTYPE_Reevaluacion))) {
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().add(
							jour.getTotalCr()));
					workfile.save();

				} else if (DepDoc.getDepType().equalsIgnoreCase(
						(X_A_Asset_Dep.DEPTYPE_Deteriodo))) {
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost()
							.subtract(jour.getTotalCr()));
					workfile.save();
				} else if (DepDoc.getDepType().equalsIgnoreCase(
						(X_A_Asset_Dep.DEPTYPE_VentaActivo))) {
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Calc_Accumulated_Depr(workfile
							.getA_Accumulated_Depr()); // guardo por si necesito
														// volver al estado
														// anterior
					workfile.setA_Accumulated_Depr(workfile.getA_Asset_Cost());
					workfile.save();
					asset.setLastMaintenanceNote("Venta Activo");
					DB.executeUpdate(
							"Update A_Asset_Forecast set processed='Y', corrected='Y' where A_Asset_ID="
									+ jour.get_ValueAsInt("A_Asset_ID"),
							get_TrxName());
				} else if (DepDoc.getDepType().equalsIgnoreCase("CRN"))// ininoles
																		// nuevo
																		// metodo
																		// de
																		// correccion
																		// para
																		// prototipo
																		// DPP
				{
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					MAssetAcct group = new MAssetAcct(getCtx(),
							MAssetAcct.getAssetAcct_ID(jour
									.get_ValueAsInt("A_Asset_ID")),
							get_TrxName());
					BigDecimal acum = DB.getSQLValueBD(
							get_TrxName(),
							"select sum(AmtAcctDr) from gl_journalline where gl_journal_id="
									+ jour.getGL_Journal_ID()
									+ " and C_ValidCombination_ID="
									+ group.getA_Accumdepreciation_Acct()); // a_accumdepreciation_acct

					workfile.setA_Accumulated_Depr(workfile
							.getA_Accumulated_Depr().add(acum));
					workfile.save();
					/* si actualiza la planificacion � */
					X_A_Asset_Forecast fore = new X_A_Asset_Forecast(
							Env.getCtx(),
							jour.get_ValueAsInt("A_Asset_Forecast_ID"), null);
					fore.setCorrected(true);
					fore.save();

					asset.setLastMaintenanceNote("CM DPP Periodo "
							+ DepDoc.getRate() + " % Valor:" + acum);
				} else if (DepDoc.getDepType().equalsIgnoreCase("DCM"))// ininoles
																		// nuevo
																		// metodo
																		// de
																		// correccion
																		// para
																		// prototipo
																		// DPP
				{
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().add(jour.getTotalCr()));
					workfile.setA_Period_Posted(workfile.getA_Period_Posted() + 1);
					workfile.setAssetDepreciationDate(DepDoc.getDateDoc());
					workfile.save();

					X_A_Asset_Forecast fore = new X_A_Asset_Forecast(Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"), null);
					fore.setProcessed(true);
					fore.setGL_Journal_ID(jour.getGL_Journal_ID());
					fore.save();

					// asset.setLastMaintenanceNote("Depreciacion Periodo :"+(workfile.getA_Period_Posted()+1)+" por "+jour.getTotalCr());

					MAssetAcct group = new MAssetAcct(getCtx(),MAssetAcct.getAssetAcct_ID(jour.get_ValueAsInt("A_Asset_ID")),get_TrxName());
					BigDecimal acum = DB.getSQLValueBD(get_TrxName(),"select sum(AmtAcctDr) from gl_journalline where gl_journal_id="
									+ jour.getGL_Journal_ID()+ " and C_ValidCombination_ID="+ group.getA_Accumdepreciation_Acct()); // a_accumdepreciation_acct

					workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().add(acum));
					workfile.save();
					/* si actualiza la planificacion � */
					// fore=new X_A_Asset_Forecast
					// (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),null);
					fore.setCorrected(true);
					fore.save();

					asset.setLastMaintenanceNote("Depreciacion y CM DPP Periodo "
							+ DepDoc.getRate() + " % Valor:" + acum);

				}
			}

			DepDoc.setDocStatus("CO");
			DepDoc.setProcessed(true);
			DepDoc.save();

			boolean assetadvise = "Y".equals(DB.getSQLValueString(
					get_TrxName(),
					"select assetadvise from ad_client where ad_client_id="
							+ getAD_Client_ID()));
			// if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria
			// )) && assetadvise)
			// org.compiere.OFBapp.ADialog.warn(2, new
			// org.compiere.OFBapp.ConfirmPanel(),
			// "No Olvide Replanificar el Activo luego de correr la correccion Monetaria");

		} else if (DepDoc.getDocStatus().equals("CO") && DepDoc.isVoid()) {
			batch = new MJournalBatch(m_ctx, DepDoc.getGL_JournalBatch_ID(),
					get_TrxName());

			MJournal[] journals = batch.getJournals(true);
			for (int i = 0; i < journals.length; i++) {
				MJournal jour = journals[i];

				if (DepDoc.getDepType().equalsIgnoreCase(
						(X_A_Asset_Dep.DEPTYPE_Depreciacion))) {
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Accumulated_Depr(workfile
							.getA_Accumulated_Depr()
							.subtract(jour.getTotalCr()));
					workfile.setA_Period_Posted(workfile.getA_Period_Posted() - 1);
					workfile.setAssetDepreciationDate(DepDoc.getDateDoc());
					workfile.save();

					X_A_Asset_Forecast fore = new X_A_Asset_Forecast(
							Env.getCtx(),
							jour.get_ValueAsInt("A_Asset_Forecast_ID"),
							get_TrxName());
					fore.setProcessed(false);
					fore.setGL_Journal_ID(0);
					fore.save();
				} else if (DepDoc.getDepType().equalsIgnoreCase(
						(X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria))) {
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					MAsset asset = new MAsset(Env.getCtx(),
							jour.get_ValueAsInt("A_Asset_ID"), get_TrxName());
					MAssetAcct group = new MAssetAcct(getCtx(),
							MAssetAcct.getAssetAcct_ID(jour
									.get_ValueAsInt("A_Asset_ID")),
							get_TrxName());
					BigDecimal acum = DB.getSQLValueBD(
							get_TrxName(),
							"select sum(AmtAcctDr) from gl_journalline where gl_journal_id="
									+ jour.getGL_Journal_ID()
									+ " and C_ValidCombination_ID="
									+ group.getA_Depreciation_Acct());
					BigDecimal neto = DB.getSQLValueBD(
							get_TrxName(),
							"select sum(AmtAcctCr) from gl_journalline where gl_journal_id="
									+ jour.getGL_Journal_ID()
									+ " and C_ValidCombination_ID="
									+ group.getA_Accumdepreciation_Acct());
					workfile.setA_Accumulated_Depr(workfile
							.getA_Accumulated_Depr().subtract(acum));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost()
							.subtract(neto));
					workfile.save();

					X_A_Asset_Forecast fore = new X_A_Asset_Forecast(
							Env.getCtx(),
							jour.get_ValueAsInt("A_Asset_Forecast_ID"), null);
					fore.setCorrected(false);
					fore.save();
				} else if (DepDoc.getDepType().equalsIgnoreCase(
						(X_A_Asset_Dep.DEPTYPE_Reevaluacion))) {
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost()
							.subtract(jour.getTotalCr()));
					workfile.save();

				} else if (DepDoc.getDepType().equalsIgnoreCase(
						(X_A_Asset_Dep.DEPTYPE_Deteriodo))) {
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().add(
							jour.getTotalCr()));
					workfile.save();
				} else if (DepDoc.getDepType().equalsIgnoreCase(
						(X_A_Asset_Dep.DEPTYPE_VentaActivo))) {
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					workfile.setA_Accumulated_Depr(workfile
							.getA_Calc_Accumulated_Depr());// antiguo valor
															// antes de vender
					workfile.save();
					DB.executeUpdate(
							"Update A_Asset_Forecast set processed='N', corrected='N' where gl_journal_id is null and A_Asset_ID="
									+ jour.get_ValueAsInt("A_Asset_ID"),
							get_TrxName());
				} else if (DepDoc.getDepType().equalsIgnoreCase("CRN"))// ininoles
																		// nuevo
																		// metodo
																		// de
																		// correccion
																		// monetaria
																		// DPP
				{
					MDepreciationWorkfile workfile = MAsset.getWorkFile(jour
							.get_ValueAsInt("A_Asset_ID"));
					MAsset asset = new MAsset(Env.getCtx(),
							jour.get_ValueAsInt("A_Asset_ID"), get_TrxName());
					MAssetAcct group = new MAssetAcct(getCtx(),
							MAssetAcct.getAssetAcct_ID(jour
									.get_ValueAsInt("A_Asset_ID")),
							get_TrxName());
					BigDecimal acum = DB.getSQLValueBD(
							get_TrxName(),
							"select sum(AmtAcctDr) from gl_journalline where gl_journal_id="
									+ jour.getGL_Journal_ID()
									+ " and C_ValidCombination_ID="
									+ group.getA_Accumdepreciation_Acct());

					workfile.setA_Accumulated_Depr(workfile
							.getA_Accumulated_Depr().subtract(acum));
					workfile.save();

					X_A_Asset_Forecast fore = new X_A_Asset_Forecast(
							Env.getCtx(),
							jour.get_ValueAsInt("A_Asset_Forecast_ID"), null);
					fore.setCorrected(false);
					fore.save();
				}

			}

			// borrado anulacion
			for (MJournal j : journals) {
				DB.executeUpdate(
						"delete from fact_acct where record_id="
								+ j.getGL_Journal_ID() + " and ad_table_id="
								+ I_GL_Journal.Table_ID, get_TrxName());
				DB.executeUpdate(
						"delete from gl_journalline where gl_journal_id="
								+ j.getGL_Journal_ID(), get_TrxName());
			}

			DB.executeUpdate("delete from gl_journal where gl_journalbatch_id="
					+ batch.getGL_JournalBatch_ID(), get_TrxName());

			/*
			 * batch.reverseCorrectIt(); batch.setProcessed(true); batch.save();
			 */

			DepDoc.setDocStatus("VO");
			DepDoc.setProcessed(true);
			DepDoc.save();
		}

		this.commitEx();
		return "";
	} // doIt
	
	public void replanningDPP (int asset_ID, int Org_ID,int PGroup_ID,String PPosession)
	{
		String sql="Select A.A_Asset_ID, C.A_Asset_Acct_ID "
				+" From A_Asset A "
				+" Inner join A_Asset_Acct C on (A.A_Asset_ID=C.A_Asset_ID)"
				+" where 1=1";
				
			    if(asset_ID>0)
			    	sql+=" and A.A_Asset_ID=?";
			    else
			    	sql+=" and A.A_Asset_Group_ID=?";
			    
				sql+=" and A.IsInPosession=?";
				
				if(Org_ID>0)
					sql+=" and A.AD_Org_ID=?";
			
			int count=0;
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				if(asset_ID>0)
					 pstmt.setInt(1, asset_ID);
				 else
					 pstmt.setInt(1, PGroup_ID);
				pstmt.setString(2, PPosession);
				if(Org_ID>0)
					pstmt.setInt(3, Org_ID);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					//DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and A_Asset_ID="+rs.getInt(1), get_TrxName());
					X_A_Asset asset= new X_A_Asset(getCtx(), rs.getInt(1), get_TrxName());
					MDepreciationWorkfile workfile=MAsset.getWorkFile(rs.getInt(1));
					MAssetAcct acct= new MAssetAcct(getCtx(), rs.getInt(2), get_TrxName());
					if(workfile==null || workfile.getA_Period_Posted()==acct.getA_Period_End())
						continue;
					
					replanningForecast(asset, workfile.getA_Asset_Cost().subtract(workfile.getA_Accumulated_Depr()),acct, 
							workfile.getA_Period_Posted(),
							DB.getSQLValueTS(get_TrxName(), "select max(datedoc) from a_asset_forecast where corrected='Y' and A_Asset_ID="+rs.getInt(1)));
					count++;
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
	}
	
	public void replanningForecast(X_A_Asset asset,BigDecimal Amount,MAssetAcct  acct, int Period, Timestamp lastdate)
	{
		
		//ininoles modificaciones para nueva forma de calculo de planificacion
		//BigDecimal SalvageAmt =acct.getA_Salvage_Value().divide(Env.ONEHUNDRED);
		//SalvageAmt=Amount.multiply(SalvageAmt);
		BigDecimal SalvageAmt =acct.getA_Salvage_Value();
		BigDecimal Currentamt=Amount;
		//Currentamt=Currentamt.subtract(Env.ONE);
		//if(SalvageAmt.intValue()<=0)
		Currentamt=Currentamt.divide(new BigDecimal(acct.getA_Period_End()-Period),2, BigDecimal.ROUND_DOWN);
		//else
			//Currentamt=Currentamt.divide(Period==(acct.getA_Period_End()-1)? Env.ONE : new BigDecimal(acct.getA_Period_End()-1-Period),2, BigDecimal.ROUND_DOWN);
		
		DB.executeUpdate("Update A_Asset_Forecast set amount="+ Currentamt +" Where Processed='N' AND A_Asset_ID="+asset.getA_Asset_ID(),get_TrxName() );
		if(asset.isInPosession()){
			DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and Corrected='N' and A_Asset_ID="+asset.getA_Asset_ID(), get_TrxName());
			Period=DB.getSQLValue(get_TrxName(), "select max(PeriodNo) from a_asset_forecast where corrected='Y' and A_Asset_ID="+asset.getA_Asset_ID());
		}
		else
			DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and A_Asset_ID="+asset.getA_Asset_ID(), get_TrxName());	
		
		int mes=1;
		if(!acct.get_ValueAsBoolean("IsByYear")) //faaguilar por mes
			for(int i=Period+1;i<(acct.getA_Period_End()+1) ;i++){
				X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),0,null);
				fore.setA_Asset_ID(asset.getA_Asset_ID());
				fore.setAD_Org_ID(asset.getAD_Org_ID());
				Calendar cal=Calendar.getInstance();
				if(lastdate==null)
					cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
				else
					cal.setTimeInMillis(lastdate.getTime());
				cal.add(Calendar.MONTH, mes);
				fore.setDateDoc(new Timestamp(cal.getTimeInMillis()));
				fore.setPeriodNo(i);
				//fore.setAmount(Currentamt);
				BigDecimal amtAcct = Currentamt;
				if(i == acct.getA_Period_End())
				{
					amtAcct = amtAcct.subtract(SalvageAmt);				
				}
				fore.setAmount(amtAcct.setScale(2, BigDecimal.ROUND_DOWN));		
				fore.getAmount(); 
				fore.save();
				mes++;
			}
		else// faaguilar by year
			for(int i=Period+1;i<(asset.getUseLifeYears()+1) ;i++){
				X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),0,null);
				fore.setA_Asset_ID(asset.getA_Asset_ID());
				fore.setAD_Org_ID(asset.getAD_Org_ID());
				Calendar cal=Calendar.getInstance();
				if(lastdate==null)
					cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
				else
					cal.setTimeInMillis(lastdate.getTime());
				cal.add(Calendar.YEAR, mes);
				cal.set(Calendar.MONTH, Calendar.DECEMBER);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				fore.setDateDoc(new Timestamp(cal.getTimeInMillis()));
				fore.setPeriodNo(i);
				//fore.setAmount(Currentamt);
				BigDecimal amtAcct = Currentamt;
				if(i == acct.getA_Period_End())
				{
					amtAcct = amtAcct.subtract(SalvageAmt);				
				}
				fore.setAmount(amtAcct.setScale(2, BigDecimal.ROUND_DOWN));		
				fore.getAmount(); 
				fore.save();
				mes++;
			}
		/*if(SalvageAmt.intValue()>0){
		X_A_Asset_Forecast forelast=new X_A_Asset_Forecast (Env.getCtx(),0,null);
		forelast.setA_Asset_ID(asset.getA_Asset_ID());
		forelast.setAD_Org_ID(asset.getAD_Org_ID());
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
		cal.add(Calendar.MONTH, acct.getA_Period_End());
		forelast.setDateDoc(new Timestamp(cal.getTimeInMillis()));
		forelast.setPeriodNo(acct.getA_Period_End());
		forelast.setAmount(SalvageAmt);
		forelast.save();
		}*/
	}


} // ImportPayment
