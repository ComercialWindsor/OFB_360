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
package org.blumos.process;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.model.X_T_BL_CTACTEFORM;
import org.compiere.model.X_T_BL_CTACTESALIDA;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 *  @author Italo Niñoles
 */
public class ProcessCtaCte extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int 			Record_ID;
	//private String			p_Action;
	
	protected void prepare()
	{
		/*ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("DocAction"))
				p_Action = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}*/
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		X_T_BL_CTACTEFORM cta = new X_T_BL_CTACTEFORM(getCtx(), Record_ID, get_TrxName());
		Timestamp vdesde = null;
		Timestamp vhasta = null;
		if(cta.isABIERTA())
			vdesde = cta.getHASTA();
		else
			vdesde = cta.getDESDE();
		vhasta = cta.getHASTA();		
		int cuenta1 = 0;
		if(cta.getAccount_ID() > 0)
			cuenta1 = cta.getAccount_ID();
		int cuenta2 = 0;
		if(cta.getACCOUNT2_ID() > 0)
			cuenta2 = cta.getACCOUNT2_ID();
		int cuenta3 = 0;
		if(cta.getACCOUNT3_ID() > 0)
			cuenta3 = cta.getACCOUNT3_ID();
		int cuenta4 = 0;
		if(cta.getACCOUNT4_ID() > 0)
			cuenta4 = cta.getACCOUNT4_ID();
		int cuenta5 = 0;
		if(cta.getACCOUNT5_ID() > 0)
			cuenta5 = cta.getACCOUNT5_ID();
		
		Timestamp la_fecha_cambio = cta.getHASTA();
		if(cta.getFECHA_CAMBIO() != null)
			la_fecha_cambio = cta.getFECHA_CAMBIO();
		
		DB.executeUpdate("delete from t_bl_ctacte where t_bl_ctacteform_id="+cta.get_ID(),get_TrxName());
		DB.executeUpdate("delete from t_bl_ctacte_saldos where t_bl_ctacteform_id="+cta.get_ID(),get_TrxName());
		DB.executeUpdate("delete from t_bl_ctacte_saldospre where t_bl_ctacteform_id="+cta.get_ID(),get_TrxName());
		DB.executeUpdate("delete from t_bl_ctacteSALIDA where t_bl_ctacteform_id="+cta.get_ID(),get_TrxName());
		
		commitEx();
		if(cuenta1+cuenta2+cuenta3+cuenta4+cuenta5 == 0)
		{
			if(cta.getC_BPartner_ID() <= 0)
			{
				String sqlUpdate = "INSERT INTO T_BL_CTACTE" +
						" select datos.*,nextval('sbl_ctacte') from" +
						" (select FA.AD_CLIENT_ID, "+cta.get_ID()+", dameclientnombre(fa.ad_client_id) as clientnombre,fa.c_period_id, p.name as PERIODO, ce.c_elementvalue_id, ce.value, ce.name, fa.ad_table_id, t.name as Des_tabla," +
						" DAMEVCTO(fa.ad_table_id,fa.record_id,fa.line_id) as FECHA_VCTO," +
						" fa.record_id, fa.dateacct, fa.description," +
						" fa.amtacctdr," +
						" fa.amtacctcr," +
						" fa.amtsourcedr," +
						" fa.amtsourcecr," +
						" fa.c_currency_id," +
						" cc.iso_code," +
						" fa.c_bpartner_id, bp.name as razon_social, bp.value as rut, bp.digito," +
						" dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id) as referencia," +
						" CASE" +
						" when fa.ad_table_id=735 THEN dameinfofactura(dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id),318)" +
						" ELSE dameinfofactura(dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id), fa.ad_table_id)" +
						" END as info_factura," +
						" fa.c_acctschema_id," +
						" dameMONEDAREFERENCIA(fa.ad_table_id,fa.record_id,fa.line_id) AS MONEDAREFERENCIA" +
						" from fact_acct fa" +
						" inner join c_period p on (fa.c_period_id=p.c_period_id)" +
						" inner join c_elementvalue ce on (fa.account_id=ce.c_elementvalue_id)" +
						" inner join ad_table_trl t on (fa.ad_table_id=t.ad_table_id)" +
						" inner join c_bpartner bp ON (fa.c_bpartner_id=bp.c_bpartner_id)" +
						" inner join c_currency cc on (fa.c_currency_id=cc.c_currency_id)" +
						" where fa.dateacct<= ? " +
						" and fa.fact_acct_id NOT IN (    select fa.fact_acct_id" +
						" from gl_journalline jl" +
						" inner join fact_acct fa on (jl.gl_journalline_id=fa.line_id and fa.ad_table_id=224)" +
						" where jl.c_bpartner_id is null" +
						" and fa.c_bpartner_id is not null " +
						" group by fa.fact_acct_id)" +
						" ) datos" +
						" where c_elementvalue_id IN (select c_elementvalue_id from c_elementvalue where ANALISIS='Y' and ad_client_id="+cta.getAD_Client_ID()+") ";
				PreparedStatement pstmt = DB.prepareStatement (sqlUpdate, null);
				pstmt.setTimestamp(1, vhasta);				
				pstmt.execute();
				commitEx();
			}
			else
			{
				String sqlUpdate = "INSERT INTO T_BL_CTACTE" +
						" select datos.*, nextval('sbl_ctacte') from" +
						" (select FA.AD_CLIENT_ID, "+cta.get_ID()+", dameclientnombre(fa.ad_client_id) as clientnombre,fa.c_period_id, p.name as PERIODO, ce.c_elementvalue_id, ce.value, ce.name, fa.ad_table_id, t.name as Des_tabla," +
						" DAMEVCTO(fa.ad_table_id,fa.record_id,fa.line_id) as FECHA_VCTO," +
						" fa.record_id, fa.dateacct, fa.description," +
						" fa.amtacctdr," +
						" fa.amtacctcr," +
						" fa.amtsourcedr," +
						" fa.amtsourcecr," +
						" fa.c_currency_id," +
						" cc.iso_code," +
						" fa.c_bpartner_id, bp.name as razon_social, bp.value as rut, bp.digito," +
						" dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id) as referencia," +
						" CASE" +
						" when fa.ad_table_id=735 THEN dameinfofactura(dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id),318)" +
						" ELSE dameinfofactura(dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id), fa.ad_table_id)" +
						" END as info_factura," +
						" fa.c_acctschema_id," +
						" dameMONEDAREFERENCIA(fa.ad_table_id,fa.record_id,fa.line_id) AS MONEDAREFERENCIA" +
						" from fact_acct fa" +
						" inner join c_period p on (fa.c_period_id=p.c_period_id)" +
						" inner join c_elementvalue ce on (fa.account_id=ce.c_elementvalue_id)" +
						" inner join ad_table_trl t on (fa.ad_table_id=t.ad_table_id)" +
						" inner join c_bpartner bp ON (fa.c_bpartner_id=bp.c_bpartner_id)" +
						" inner join c_currency cc on (fa.c_currency_id=cc.c_currency_id)" +
						" where fa.dateacct<=? AND fa.C_BPartner_ID=?" +
						" and fa.fact_acct_id NOT IN (    select fa.fact_acct_id" +
						" from gl_journalline jl" +
						" inner join fact_acct fa on (jl.gl_journalline_id=fa.line_id and fa.ad_table_id=224)" +
						" where jl.c_bpartner_id is null" +
						" and fa.c_bpartner_id is not null" +
						" group by fa.fact_acct_id)" +
						" ) datos" +
						" where c_elementvalue_id IN (select c_elementvalue_id from c_elementvalue where ANALISIS='Y' and ad_client_id="+cta.getAD_Client_ID()+")";
				PreparedStatement pstmt = DB.prepareStatement (sqlUpdate, null);
				pstmt.setTimestamp(1, vhasta);
				pstmt.setInt(2, cta.getC_BPartner_ID());
				pstmt.execute();
				commitEx();
			}
		}
		// NO SE SELECCIONA NINGÚN PARTNER, PERO SÍ UNA O MÁS CUENTAS, ENTONCES, SE ENTREGAN TODOS LOS PARTNERS DE ESA CUENTA
		else if(cta.getC_BPartner_ID() <= 0)
		{
			String sqlUpdate = "INSERT INTO T_BL_CTACTE" +
					" select datos.*, nextval('sbl_ctacte') from" +
					" (select FA.AD_CLIENT_ID, "+cta.get_ID()+", dameclientnombre(fa.ad_client_id) as clientnombre,fa.c_period_id, p.name as PERIODO, ce.c_elementvalue_id, ce.value, ce.name, fa.ad_table_id, t.name as Des_tabla," +
					" DAMEVCTO(fa.ad_table_id,fa.record_id,fa.line_id) as FECHA_VCTO," +
					" fa.record_id, fa.dateacct, fa.description," +
					" fa.amtacctdr," +
					" fa.amtacctcr," +
					" fa.amtsourcedr," +
					" fa.amtsourcecr," +
					" fa.c_currency_id," +
					" cc.iso_code," +
					" fa.c_bpartner_id, bp.name as razon_social, bp.value as rut, bp.digito," +
					" dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id) as referencia," +
					" CASE" +
					" when fa.ad_table_id=735 THEN dameinfofactura(dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id),318)" +
					" ELSE dameinfofactura(dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id), fa.ad_table_id)" +
					" END as info_factura," +
					" fa.c_acctschema_id," +
					" dameMONEDAREFERENCIA(fa.ad_table_id,fa.record_id,fa.line_id) AS MONEDAREFERENCIA" +
					" from fact_acct fa" +
					" inner join c_period p on (fa.c_period_id=p.c_period_id)" +
					" inner join c_elementvalue ce on (fa.account_id=ce.c_elementvalue_id)" +
					" inner join ad_table_trl t on (fa.ad_table_id=t.ad_table_id)" +
					" inner join c_bpartner bp ON (fa.c_bpartner_id=bp.c_bpartner_id)" +
					" inner join c_currency cc on (fa.c_currency_id=cc.c_currency_id)" +
					" where fa.dateacct<=? "+
					" and fa.fact_acct_id NOT IN (    select fa.fact_acct_id" +
					" from gl_journalline jl " +
					" inner join fact_acct fa on (jl.gl_journalline_id=fa.line_id and fa.ad_table_id=224)" +
					" where jl.c_bpartner_id is null" +
					" and fa.c_bpartner_id is not null" +
					" group by fa.fact_acct_id)" +
					" ) datos" +
					" where c_elementvalue_id IN (select c_elementvalue_id from c_elementvalue where ANALISIS='Y' and ad_client_id="+cta.getAD_Client_ID()+")" +
					" AND C_ELEMENTVALUE_ID IN ("+cuenta1+", "+cuenta2+", "+cuenta3+", "+cuenta4+", "+cuenta5+");";
			PreparedStatement pstmt = DB.prepareStatement (sqlUpdate, null);
			pstmt.setTimestamp(1, vhasta);
			pstmt.execute();
			commitEx();
		}
		else
		{
			String sqlUpdate = "INSERT INTO T_BL_CTACTE" +
					" select datos.*, nextval('sbl_ctacte') from" +
					" (select FA.AD_CLIENT_ID, "+cta.get_ID()+", dameclientnombre(fa.ad_client_id) as clientnombre,fa.c_period_id, p.name as PERIODO, ce.c_elementvalue_id, ce.value, ce.name, fa.ad_table_id, t.name as Des_tabla," +
					" DAMEVCTO(fa.ad_table_id,fa.record_id,fa.line_id) as FECHA_VCTO," +
					" fa.record_id, fa.dateacct, fa.description," +
					" fa.amtacctdr," +
					" fa.amtacctcr," +
					" fa.amtsourcedr," +
					" fa.amtsourcecr, " +
					" fa.c_currency_id," +
					" cc.iso_code," +
					" fa.c_bpartner_id, bp.name as razon_social, bp.value as rut, bp.digito," +
					" dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id) as referencia," +
					" CASE" +
					" when fa.ad_table_id=735 THEN dameinfofactura(dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id),318)" +
					" ELSE dameinfofactura(dameidfactura(fa.ad_table_id,fa.record_id,fa.line_id), fa.ad_table_id)" +
					" END as info_factura," +
					" fa.c_acctschema_id," +
					" dameMONEDAREFERENCIA(fa.ad_table_id,fa.record_id,fa.line_id) AS MONEDAREFERENCIA" +
					" from fact_acct fa" +
					" inner join c_period p on (fa.c_period_id=p.c_period_id)" +
					" inner join c_elementvalue ce on (fa.account_id=ce.c_elementvalue_id)" +
					" inner join ad_table_trl t on (fa.ad_table_id=t.ad_table_id)" +
					" inner join c_bpartner bp ON (fa.c_bpartner_id=bp.c_bpartner_id)" +
					" inner join c_currency cc on (fa.c_currency_id=cc.c_currency_id)" +
					" where fa.dateacct<=? AND fa.C_BPartner_ID=?" +
					" and fa.fact_acct_id NOT IN (    select fa.fact_acct_id" +
					" from gl_journalline jl" +
					" inner join fact_acct fa on (jl.gl_journalline_id=fa.line_id and fa.ad_table_id=224)" +
					" where jl.c_bpartner_id is null" +
					" and fa.c_bpartner_id is not null" +
					" group by fa.fact_acct_id)" +
					" ) datos" +
					" where c_elementvalue_id IN (select c_elementvalue_id from c_elementvalue where ANALISIS='Y' and ad_client_id="+cta.getAD_Client_ID()+")" +
					" AND C_ELEMENTVALUE_ID IN ("+cuenta1+", "+cuenta2+", "+cuenta3+", "+cuenta4+", "+cuenta5+");";
			PreparedStatement pstmt = DB.prepareStatement (sqlUpdate, null);
			pstmt.setTimestamp(1, vhasta);
			pstmt.setInt(2, cta.getC_BPartner_ID());
			pstmt.execute();
			commitEx();
		}
		
		// update que lleva a cero las diferencias de cambio cuamdo su moneda es <> moneda origen
		DB.executeUpdate("UPDATE T_BL_CTACTE SET amtsourcecr=0, amtsourcedr=0" +
				" WHERE t_bl_ctacteform_id="+cta.get_ID()+" and C_CURRENCY_ID<>MONEDAREFERENCIA;", get_TrxName());
		
		commitEx();
		// determinación de saldos entre fechas escogidas
		// saldos a la fecha hasta
		String sqlUp1 = "insert into t_bl_ctacte_saldos " +
				" select datos.*, nextval('sbl_ctacte_saldos')" +
				" from" +
				" (select t_bl_ctacteform_id, ad_client_id, C_ACCTSCHEMA_ID, c_elementvalue_id, referencia, c_bpartner_id, MONEDAREFERENCIA, (sum(amtsourcedr) - sum(amtsourcecr)) as saldo" +
				" from t_bl_ctacte" +
				" WHERE t_bl_ctacteform_id=" +cta.get_ID()+
				" group by t_bl_ctacteform_id, ad_client_id, C_ACCTSCHEMA_ID, c_elementvalue_id, referencia, c_bpartner_id, MONEDAREFERENCIA) datos";
		DB.executeUpdate(sqlUp1, get_TrxName());
		commitEx();
		//saldos a la fecha desde
		if(!cta.isABIERTA())
		{
			String sqlUp2 = "insert into t_bl_ctacte_saldosPRE" +
					" select datos.*, nextval('sbl_ctacte_saldospre')" +
					" from" +
					" (select t_bl_ctacteform_id, ad_client_id, C_ACCTSCHEMA_ID, c_elementvalue_id, referencia, c_bpartner_id, MONEDAREFERENCIA, (sum(amtsourcedr) - sum(amtsourcecr)) as saldos" +
					" from t_bl_ctacte" +
					" WHERE t_bl_ctacteform_id="+cta.get_ID()+" AND DATEACCT < ?" +
					" group by t_bl_ctacteform_id, ad_client_id, C_ACCTSCHEMA_ID, c_elementvalue_id, referencia, c_bpartner_id, MONEDAREFERENCIA) datos";
			PreparedStatement pstmtUp2 = DB.prepareStatement (sqlUp2, null);
			pstmtUp2.setTimestamp(1, vdesde);
			pstmtUp2.execute();
		}
		//se ace commit para ver porque no se genera el detalle
		commitEx();
		if(cta.isABIERTA())
		{
			//se hace con metodo de ciclo y no con insert directo
			String sqlUp2 = "SELECT AD_ORG_ID,T_BL_CTACTEFORM_ID, CLIENTNOMBRE, C_PERIOD_ID, PERIODO," +
					" C_ELEMENTVALUE_ID, VALUE, NAME, AD_TABLE_ID, DES_TABLA, FECHA_VCTO, RECORD_ID, " +
					" DATEACCT, DESCRIPTION, AMTACCTDR, AMTACCTCR, AMTSOURCEDR, AMTSOURCECR," +
					" C_CURRENCY_ID, ISO_CODE, C_BPARTNER_ID, RAZON_SOCIAL, RUT, DIGITO, REFERENCIA, INFO_FACTURA, C_ACCTSCHEMA_ID, MONEDAREFERENCIA, SALDO," +
					" damefechadocreferencia(record_id,ad_table_id) as fecha_dcto," +
					" CASE" +
					" WHEN AD_TABLE_ID=318 THEN (SELECT CORRELATIVO FROM C_INVOICE WHERE C_INVOICE_ID=RECORD_ID)" +
					" ELSE 0" +
					" END AS CORRELATIVO," +
					" dameorden_id(ad_table_id,record_id,0) as carpeta "+
					" FROM" +
					" (SELECT tc.AD_CLIENT_ID, 0 as ad_org_id, now() as created, 0 as createdby, now() as updated, 0 as updatedby, 0 as T_BL_CTACTESALIDA_ID, tc.T_BL_CTACTEFORM_ID, CLIENTNOMBRE, C_PERIOD_ID, PERIODO, C_ELEMENTVALUE_ID, VALUE, NAME," +
					" AD_TABLE_ID, DES_TABLA, FECHA_VCTO, RECORD_ID, DATEACCT, DESCRIPTION, AMTACCTDR, AMTACCTCR, AMTSOURCEDR," +
					" AMTSOURCECR, tc.C_CURRENCY_ID, ISO_CODE, tc.C_BPARTNER_ID, RAZON_SOCIAL, RUT, DIGITO, tc.REFERENCIA, INFO_FACTURA," +
					" tc.C_ACCTSCHEMA_ID, MONEDAREFERENCIA, tcs.saldo AS SALDO" +
					" FROM T_BL_CTACTE tc" +
					" INNER join t_bl_ctacte_saldos tcs on (tc.c_acctschema_id=tcs.c_acctschema_id and tc.c_elementvalue_id=tcs.account_id and tc.referencia=tcs.referencia and tc.c_bpartner_id=tcs.c_bpartner_id and tc.t_bl_ctacteform_id=tcs.t_bl_ctacteform_id) " +
					" where tc.t_bl_ctacteform_id="+cta.get_ID()+") as saldo WHERE SALDO <>0 ";
			PreparedStatement pstmtUp2 = DB.prepareStatement (sqlUp2,get_TrxName());
			ResultSet rsUp2 = pstmtUp2.executeQuery ();
			while(rsUp2.next())
			{
				X_T_BL_CTACTESALIDA salida = new X_T_BL_CTACTESALIDA(getCtx(), 0, get_TrxName());
				salida.setAD_Org_ID(rsUp2.getInt("AD_ORG_ID"));
				salida.setT_BL_CTACTEFORM_ID(cta.get_ID());
				salida.setCLIENTNOMBRE(rsUp2.getString("CLIENTNOMBRE"));
				salida.setC_Period_ID(rsUp2.getInt("C_PERIOD_ID"));
				salida.setC_ElementValue_ID(rsUp2.getInt("C_ELEMENTVALUE_ID"));
				salida.setValue(rsUp2.getString("VALUE"));
				salida.setName(rsUp2.getString("NAME"));
				salida.setAD_Table_ID(rsUp2.getInt("AD_TABLE_ID"));
				salida.setDES_TABLA(rsUp2.getString("DES_TABLA"));
				salida.setFECHA_VCTO(rsUp2.getTimestamp("FECHA_VCTO"));
				salida.setRecord_ID(rsUp2.getInt("RECORD_ID"));
				salida.setDateAcct(rsUp2.getTimestamp("DATEACCT"));
				salida.setDescription(rsUp2.getString("DESCRIPTION"));
				salida.setAmtAcctDr(rsUp2.getBigDecimal("AMTACCTDR"));
				salida.setAmtAcctCr(rsUp2.getBigDecimal("AMTACCTCR"));
				salida.setAmtSourceDr(rsUp2.getBigDecimal("AMTSOURCEDR"));
				salida.setAmtSourceCr(rsUp2.getBigDecimal("AMTSOURCECR"));
				salida.setC_Currency_ID(rsUp2.getInt("C_CURRENCY_ID"));
				salida.setISO_Code(rsUp2.getString("ISO_CODE"));
				salida.setC_BPartner_ID(rsUp2.getInt("C_BPARTNER_ID"));
				salida.setRAZON_SOCIAL(rsUp2.getString("RAZON_SOCIAL"));
				salida.setRUT(rsUp2.getString("RUT"));
				salida.setDIGITO(rsUp2.getString("DIGITO"));
				salida.setREFERENCIA(rsUp2.getBigDecimal("REFERENCIA"));
				salida.setINFO_FACTURA(rsUp2.getString("INFO_FACTURA"));
				salida.setC_AcctSchema_ID(rsUp2.getInt("C_ACCTSCHEMA_ID"));
				salida.setMONEDAREFERENCIA(rsUp2.getBigDecimal("MONEDAREFERENCIA"));
				salida.setSALDO(rsUp2.getBigDecimal("SALDO"));
				salida.setDESDE(cta.getDESDE());
				salida.setHASTA(cta.getHASTA());
				salida.setABIERTA(cta.isABIERTA());
				salida.setFECHA_DCTO(rsUp2.getTimestamp("fecha_dcto"));
				salida.setCorrelativo(rsUp2.getInt("CORRELATIVO"));
				salida.setC_Order_ID(rsUp2.getInt("carpeta"));
				salida.set_CustomColumn("fecha_cambio",la_fecha_cambio);
				salida.set_CustomColumn("c_conversiontype_id",cta.getC_ConversionType_ID());
				salida.save(get_TrxName());
			}
		}
		else
		{
			//se hace con metodo de ciclo y no con insert directo
			String sqlUp2 = "SELECT AD_ORG_ID, CLIENTNOMBRE, C_PERIOD_ID, PERIODO," +
					" C_ELEMENTVALUE_ID, VALUE, NAME, AD_TABLE_ID, DES_TABLA, FECHA_VCTO, RECORD_ID, DATEACCT, DESCRIPTION, AMTACCTDR, AMTACCTCR, AMTSOURCEDR, AMTSOURCECR," +
					" C_CURRENCY_ID, ISO_CODE, C_BPARTNER_ID, RAZON_SOCIAL, RUT, DIGITO, REFERENCIA, INFO_FACTURA, " +
					" C_ACCTSCHEMA_ID, MONEDAREFERENCIA, SALDO,"+
					" damefechadocreferencia(record_id,ad_table_id) as fecha_dcto," +
					" CASE" +
					" WHEN AD_TABLE_ID=318 THEN (SELECT CORRELATIVO FROM C_INVOICE WHERE C_INVOICE_ID=RECORD_ID)" +
					" ELSE 0" +
					" END AS CORRELATIVO, " +
					" dameorden_id(ad_table_id,record_id,0) as carpeta " +
					" FROM" +
					" (SELECT 0 as ad_org_id, CLIENTNOMBRE, C_PERIOD_ID, PERIODO, " +
					" C_ELEMENTVALUE_ID, VALUE, NAME," +
					" AD_TABLE_ID, DES_TABLA, FECHA_VCTO, RECORD_ID, DATEACCT, DESCRIPTION, AMTACCTDR, AMTACCTCR, AMTSOURCEDR," +
					" AMTSOURCECR, tc.C_CURRENCY_ID, ISO_CODE, tc.C_BPARTNER_ID, RAZON_SOCIAL, RUT, DIGITO, tc.REFERENCIA, INFO_FACTURA," +
					" tc.C_ACCTSCHEMA_ID, MONEDAREFERENCIA, 0 AS SALDO" +
					" FROM T_BL_CTACTE tc" +
					" INNER join t_bl_ctacte_saldospre tcs on (tc.c_acctschema_id=tcs.c_acctschema_id and tc.c_elementvalue_id=tcs.account_id and tc.referencia=tcs.referencia and tc.c_bpartner_id=tcs.c_bpartner_id and tc.t_bl_ctacteform_id=tcs.t_bl_ctacteform_id)" +
					" where tc.t_bl_ctacteform_id="+cta.get_ID()+" and tc.DATEACCT < ? AND TCS.SALDO<>0 " +
					" UNION ALL" +
					" SELECT 0 as ad_org_id, CLIENTNOMBRE, C_PERIOD_ID, PERIODO, " +
					" C_ELEMENTVALUE_ID, VALUE, NAME, AD_TABLE_ID, DES_TABLA, FECHA_VCTO, RECORD_ID, DATEACCT, DESCRIPTION, AMTACCTDR, AMTACCTCR, AMTSOURCEDR, AMTSOURCECR," +
					" tc.C_CURRENCY_ID, ISO_CODE, tc.C_BPARTNER_ID, RAZON_SOCIAL, RUT, DIGITO, tc.REFERENCIA, INFO_FACTURA," +
					" tc.C_ACCTSCHEMA_ID, MONEDAREFERENCIA, tcs.saldo AS SALDO" +
					" FROM T_BL_CTACTE tc" +
					" INNER join t_bl_ctacte_saldos tcs on (tc.c_acctschema_id=tcs.c_acctschema_id and tc.c_elementvalue_id=tcs.account_id and tc.referencia=tcs.referencia and tc.c_bpartner_id=tcs.c_bpartner_id and tc.t_bl_ctacteform_id=tcs.t_bl_ctacteform_id)" +
					" where tc.t_bl_ctacteform_id="+cta.get_ID()+" and tc.DATEACCT >= ? ) as saldo";
			PreparedStatement pstmtUp2 = DB.prepareStatement (sqlUp2,get_TrxName());
			pstmtUp2.setTimestamp(1, vdesde);
			pstmtUp2.setTimestamp(2, vdesde);
			ResultSet rsUp2 = pstmtUp2.executeQuery ();
			while(rsUp2.next())
			{
				X_T_BL_CTACTESALIDA salida = new X_T_BL_CTACTESALIDA(getCtx(), 0, get_TrxName());
				salida.setAD_Org_ID(rsUp2.getInt("AD_ORG_ID"));
				salida.setT_BL_CTACTEFORM_ID(cta.get_ID());
				salida.setCLIENTNOMBRE(rsUp2.getString("CLIENTNOMBRE"));
				salida.setC_Period_ID(rsUp2.getInt("C_PERIOD_ID"));
				salida.setC_ElementValue_ID(rsUp2.getInt("C_ELEMENTVALUE_ID"));
				salida.setValue(rsUp2.getString("VALUE"));
				salida.setName(rsUp2.getString("NAME"));
				salida.setAD_Table_ID(rsUp2.getInt("AD_TABLE_ID"));
				salida.setDES_TABLA(rsUp2.getString("DES_TABLA"));
				salida.setFECHA_VCTO(rsUp2.getTimestamp("FECHA_VCTO"));
				salida.setRecord_ID(rsUp2.getInt("RECORD_ID"));
				salida.setDateAcct(rsUp2.getTimestamp("DATEACCT"));
				salida.setDescription(rsUp2.getString("DESCRIPTION"));
				salida.setAmtAcctDr(rsUp2.getBigDecimal("AMTACCTDR"));
				salida.setAmtAcctCr(rsUp2.getBigDecimal("AMTACCTCR"));
				salida.setAmtSourceDr(rsUp2.getBigDecimal("AMTSOURCEDR"));
				salida.setAmtSourceCr(rsUp2.getBigDecimal("AMTSOURCECR"));
				salida.setC_Currency_ID(rsUp2.getInt("C_CURRENCY_ID"));
				salida.setISO_Code(rsUp2.getString("ISO_CODE"));
				salida.setC_BPartner_ID(rsUp2.getInt("C_BPARTNER_ID"));
				salida.setRAZON_SOCIAL(rsUp2.getString("RAZON_SOCIAL"));
				salida.setRUT(rsUp2.getString("RUT"));
				salida.setDIGITO(rsUp2.getString("DIGITO"));
				salida.setREFERENCIA(rsUp2.getBigDecimal("REFERENCIA"));
				salida.setINFO_FACTURA(rsUp2.getString("INFO_FACTURA"));
				salida.setC_AcctSchema_ID(rsUp2.getInt("C_ACCTSCHEMA_ID"));
				salida.setMONEDAREFERENCIA(rsUp2.getBigDecimal("MONEDAREFERENCIA"));
				salida.setSALDO(rsUp2.getBigDecimal("SALDO"));
				salida.setDESDE(cta.getDESDE());
				salida.setHASTA(cta.getHASTA());
				salida.setABIERTA(cta.isABIERTA());
				salida.setFECHA_DCTO(rsUp2.getTimestamp("fecha_dcto"));
				salida.setCorrelativo(rsUp2.getInt("CORRELATIVO"));
				salida.setC_Order_ID(rsUp2.getInt("carpeta"));
				salida.set_CustomColumn("fecha_cambio",la_fecha_cambio);
				salida.set_CustomColumn("c_conversiontype_id",cta.getC_ConversionType_ID());
				salida.save(get_TrxName());
			}
		}		
		return "Procesado";
	}	//	doIt
	
	
}	//	Replenish

