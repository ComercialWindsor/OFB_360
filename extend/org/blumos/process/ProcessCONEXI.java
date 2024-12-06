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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.compiere.model.X_T_BL_CONEXI_SESION;
import org.compiere.model.X_T_BL_CONEXI_SESIONLINE;
import org.compiere.model.X_T_BL_CONEXI_SESION_SALDLINE;
import org.compiere.model.X_T_BL_CONEXI_SESION_SALDOS;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.ofb.utils.DateUtils;

/**
 *  @author Italo Niñoles
 */
public class ProcessCONEXI extends SvrProcess
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
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		X_T_BL_CONEXI_SESION conexi = new X_T_BL_CONEXI_SESION(getCtx(), Record_ID, get_TrxName());
		
		/*int v_verif;
		int IdDevuelto;
		String v_cod_blumos;
		int v_m_product_id;
		int v_conexi_id_responsable;
		String la_salida;*/
		//variables para la construccion de sql variable
		String sql_cod_blumos ="";
		String sql_producto_solutec = "";
		String sql_bpartner_id = "";
		String sql_responsable_id = "";
		String sql_base = "";
		//variables para la captura de criterios
		String v_usuario_ventas ="";
		String v_representada ="";
		String v_solutec ="";
		String v_cod_blumos_cr ="";
		
		if(conexi.getUSUARIO_COMPRAS_ID() > 0)
		{
			sql_base = "Select T_BL_CONEXI_PROY.cod_blumos from t_bl_conexi_proyoperaciones inner join t_bl_conexi_prOy on (t_bl_conexi_proyoperaciones.cod_blumos=t_bl_conexi_proy.cod_blumos)" +
					" where 1=1 and t_bl_conexi_proyoperaciones.usuario_id="+conexi.getUSUARIO_COMPRAS_ID();
			conexi.setCRITERIOS("Seleccionado Usuario Compras: "+conexi.getUSUARIO_COMPRAS().getName());
		}
		else
		{
			//segundo camino se consideran todas las demas variables
			sql_base = "SELECT COD_BLUMOS FROM T_BL_CONEXI_PROY WHERE 1=1 ";
			//segmentos agregados
			if(conexi.getCOD_BLUMOS() != null 
					&& conexi.getCOD_BLUMOS().compareToIgnoreCase("NONE")!= 0 
					&& conexi.getCOD_BLUMOS().trim().length() > 0)
			{
				sql_cod_blumos = "and cod_blumos='"+conexi.getCOD_BLUMOS()+"'";
				v_cod_blumos_cr = " Producto: "+conexi.getCOD_BLUMOS();
			}
			
			if(conexi.isPRODUCTO_SOLUTEC())
			{
				sql_producto_solutec = " AND dameproductosolutec(cod_blumos)='Y'";
				v_solutec = " - Solo Productos Solutec";
			}
			else
				sql_producto_solutec = " AND dameproductosolutec(cod_blumos)<>'Y'";
			if(conexi.getC_BPartner_ID() >0)
			{
				sql_bpartner_id = " AND damerepresentadacod_blm(cod_blumos)="+conexi.getC_BPartner_ID();
				v_representada = " - Representada: "+conexi.getC_BPartner().getName();
			}
			if(conexi.getRESPONSABLE_PROYECCIONES_ID() > 0)
			{
				sql_responsable_id = " AND responsable_id="+conexi.getRESPONSABLE_PROYECCIONES_ID();
				v_usuario_ventas = " - Usuario Ventas: "+conexi.getRESPONSABLE_PROYECCIONES().getName();
			}
			//impresion de criterior para reporte
			conexi.setCRITERIOS((conexi.getCRITERIOS()==null?"":conexi.getCRITERIOS())+v_solutec+v_representada+v_usuario_ventas+v_cod_blumos_cr);
		}
		
		String v_query = sql_base+sql_cod_blumos+sql_producto_solutec+sql_bpartner_id+sql_responsable_id;
		//las siguientes instrucciones determinan sobre que universo operara el proceso de calculos conexi p_bl_conexi
		String desc = "";
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (v_query, get_TrxName());
		ResultSet rs = pstmt.executeQuery ();	
		while (rs.next())
		{
			desc = conexi.getLOG_CONTROL();
			if(desc == null)
				desc = "";
			String salida = executeCONEXI(rs.getString("cod_blumos"),conexi);
			conexi.setLOG_CONTROL(desc+salida);
		}
		//conexi.set_CustomColumn("DateTrx",DateUtils.todayAdempiere());
		conexi.setProcessed(true);
        conexi.save(get_TrxName());		
		return "Procesado";
	}	//	doIt
	
	private String executeCONEXI(String cod_Blumos,X_T_BL_CONEXI_SESION conexi) throws SQLException
	{
		//OTRAS VARIABLES
		String concepto_link = "";
		//int id_saldos;
		//variables para acumular saldos
		BigDecimal saldo_actual = Env.ZERO;
		BigDecimal stb[] = {Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO};//SALDOS TODAS LAS BODEGAS
		BigDecimal sbc[] = {Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO};//SALDOS BODEGA CENTRAL
		BigDecimal sob[] = {Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO};//SALDOS OTRAS BODEGAS
		BigDecimal acum[] = {Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO};//acumulador de cantidades 1
		BigDecimal acumcp[] = {Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO};//acumulador de compras proyectadas
		BigDecimal acumvp[] = {Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO};//acumulador de ventas proyectadas
		BigDecimal acumsaldoP[] = {Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO,Env.ZERO};//acumulador de saldos proyectados
		BigDecimal acum_sr = Env.ZERO; //acumulador para periodo actual historico
		BigDecimal acum_scp = Env.ZERO; //acumulador para periodo actual compras proyectadas
		BigDecimal acum_svp = Env.ZERO; //acumulador para periodo actual compras proyectadas
		//arreglos de fecha
		Timestamp la_fecha[] = {DateUtils.todayAdempiere(),DateUtils.todayAdempiere(),DateUtils.todayAdempiere(),DateUtils.todayAdempiere(),DateUtils.todayAdempiere(),DateUtils.todayAdempiere()}; 
		Timestamp la_fecha2[] = {DateUtils.todayAdempiere(),DateUtils.todayAdempiere(),DateUtils.todayAdempiere(),DateUtils.todayAdempiere(),DateUtils.todayAdempiere(),DateUtils.todayAdempiere()};
		//VARIABLEs para tratamiento de fechas
		Timestamp fecha_actual;
		Timestamp fecha_desde;
		//Timestamp fecha_hasta;
		//Timestamp fecha_saldo;
		Timestamp inicio_mes;
		Timestamp fin_mes;
		//VARIABLES PARA REFERENCIAS
		int m_inoutline_id;
		int m_inventoryline_id;
		int m_movementline_id;
		int m_productionline_id;
		//int v_m_product_id;
		//variables para capturar datos de tarjeta de existencias
		//String V_COD_BLUMOS;
		//int V_C_DOCTYPE_ID;
		//String V_ISSOTRX;
		//BigDecimal V_CANTIDAD = Env.ZERO;
		//int V_CONCEPTO_ID;
		int v_verif; //variable para comprobaciones de consultas
		//int V_AD_CLIENT_ID;
		
		//V_COD_BLUMOS = cod_Blumos;
		//V_AD_CLIENT_ID = 100000;
		fecha_actual = DateUtils.todayAdempiere();
		fecha_desde = DateUtils.UltimoDiaMes(DateUtils.SumarMeses(fecha_actual, -6));
		inicio_mes = DateUtils.UltimoDiaMes(DateUtils.SumarMeses(fecha_actual, -1));
		fin_mes = DateUtils.UltimoDiaMes(fecha_actual);

		DB.executeUpdate("delete from t_bl_conexi_sesionline where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND cod_blumos='"+cod_Blumos+"'",get_TrxName()); 
		DB.executeUpdate("delete from t_bl_conexi_sesion_saldos where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND cod_blumos='"+cod_Blumos+"'",get_TrxName()); 
		
		PreparedStatement pstmt = null;
		String v_queryProd = "";
		if(DB.isOracle())
		{
			v_queryProd = "SELECT mp.m_product_id FROM M_PRODUCT mp " +
				" WHERE (SUBSTR(mp.name,0,4)= '"+cod_Blumos+"') AND MP.AD_CLIENT_ID=1000000" ;
		}
		else
			v_queryProd = "SELECT mp.m_product_id FROM M_PRODUCT mp " +
				" WHERE (SUBSTR(mp.name,0,5)= '"+cod_Blumos+"') AND MP.AD_CLIENT_ID=1000000" ;
		pstmt = DB.prepareStatement (v_queryProd, get_TrxName());
		ResultSet rs = pstmt.executeQuery ();	
		while (rs.next())
		{
			for(int a=5;a >=0;a--)
			{
				String indMes = Integer.toString(a+1);
				BigDecimal aux1 = DB.getSQLValueBD(get_TrxName(), "SELECT damesaldoproducto(M_product_ID,last_day(add_months(?, "+indMes+"*-1)),0) " +
						" FROM M_Product WHERE M_product_ID = "+rs.getInt("M_Product_ID"),DateUtils.todayAdempiere());
				if(aux1 == null)
					aux1 = Env.ZERO;
				stb[a] = stb[a].add(aux1);
				
				/*BigDecimal aux2 = DB.getSQLValueBD(get_TrxName(), "SELECT damesaldoproducto(M_product_ID,last_day(add_months(?, "+indMes+"*-1)),1000000)+damesaldoproducto(M_product_ID,last_day(add_months(?, "+indMes+"*-1)),1000108)" +
						" FROM M_Product WHERE M_product_ID = "+rs.getInt("M_Product_ID"),DateUtils.todayAdempiere(),DateUtils.todayAdempiere());*/
				BigDecimal aux2 = DB.getSQLValueBD(get_TrxName(), "SELECT damesaldoproducto(M_product_ID,last_day(add_months(?, "+indMes+"*-1)),1000000)+damesaldoproducto(M_product_ID,last_day(add_months(?, "+indMes+"*-1)),1000108)+damesaldoproducto(M_product_ID,last_day(add_months(?, "+indMes+"*-1)),1000117)+damesaldoproducto(M_product_ID,last_day(add_months(?, "+indMes+"*-1)),1000123)" +
                        " FROM M_Product WHERE M_product_ID = "+rs.getInt("M_Product_ID"),DateUtils.todayAdempiere(),DateUtils.todayAdempiere(),DateUtils.todayAdempiere(),DateUtils.todayAdempiere());
				if(aux2 == null)
					aux2 = Env.ZERO;
				sbc[a] = sbc[a].add(aux2);
			}
			BigDecimal aux3 = DB.getSQLValueBD(get_TrxName(), "SELECT damesaldoproducto(M_Product_ID,?,0) FROM M_product WHERE M_Product_ID="+rs.getInt("M_Product_ID"),DateUtils.todayAdempiere());
			if(aux3 == null)
				aux3 = Env.ZERO;
			saldo_actual = saldo_actual.add(aux3); 
			
			String v_query = "SELECT ad_client_id, dateacct, datetrx, account_id, qty, m_locator_id, m_product_id, qty_real, ad_table_id, origen, record_id, line_id" +
					" FROM " +
					" (SELECT movs.ad_client_id, movs.dateacct, movs.datetrx, movs.account_id, movs.qty, movs.cuenta," +
					" movs.m_locator_id, movs.bodega, movs.m_product_id, movs.codigo, movs.producto, movs.amtacctdr," +
					" movs.amtacctcr, movs.costo_salidas, movs.qty_real, movs.id_carpeta, movs.ad_table_id, movs.origen," +
					" movs.record_id, movs.line_id, movs.costo_base, movs.otros_costos, movs.info_factura" +
					" FROM" +
					" (SELECT j.ad_client_id, j.dateacct, j.dateacct AS datetrx, null as account_id, ap.QTY, null as cuenta, ap.m_locator_id, j.description as bodega," +
					" jl.m_product_id, p.value as codigo, p.name as producto, (jl.amtacctdr/jl.qty*ap.qty) as amtacctdr, (jl.amtacctcr/jl.qty*ap.qty) as amtacctcr," +
					" null as costo_salidas," +
					" ap.qty AS QTY_REAL," +
					" null as id_carpeta," +
					" null AS ad_table_id, NULL as origen, jl.gl_journal_id AS record_id, jl.gl_journalline_id  as line_id," +
					" (((jl.amtacctdr-jl.amtacctcr)/jl.qty)*ap.qty) as costo_base, 0 as otros_costos," +
					" null as info_factura" +
					" from Gl_JournalLine jl" +
					" inner join t_bl_saldos_bodega_apertura ap ON (jl.m_product_id=ap.m_product_id) " +
					" inner join gl_journal j ON (jl.gl_journal_id=j.gl_journal_id)" +
					" inner join m_product p ON (jl.m_product_id=p.m_product_id)" +
					" where jl.GL_JOURNAL_ID=1000838" +
					" UNION ALL" +
					" SELECT fa.ad_client_id, fa.dateacct, fa.datetrx, FA.ACCOUNT_ID, QTY, ce.name as cuenta, fa.m_locator_id, war.name as bodega," +
					" fa.m_product_id, p.value as codigo, p.name as producto, fa.amtacctdr, fa.amtacctcr," +
					" case" +
					" when fa.m_locator_id is null then 0" +
					" else (fa.amtacctcr/fa.qty)" +
					" end  as costo_salidas," +
					" case " +
					" when fa.m_locator_id is null then 0" +
					" else fa.qty " +
					" end as qty_real," +
					" dameorden_id(fa.ad_table_id, fa.record_id, fa.line_id)  as Id_Carpeta, " +
					" fa.ad_table_id, t.name as origen, fa.record_id, fa.line_id," +
					" case" +
					" when fa.ad_table_id=318 and line_id NOT IN (select c_invoiceline_id from c_landedcostallocation lc inner join m_costelement ce on (lc.m_costelement_id=ce.m_costelement_id) where ce.isactive='N') then amtacctdr-amtacctcr" +
					" when fa.ad_table_id<>318 then amtacctdr-amtacctcr" +
					" else 0 " +
					" end as Costo_Base," +
					" case" +
					" when fa.ad_table_id=318 and line_id  IN (select c_invoiceline_id from c_landedcostallocation lc inner join m_costelement ce on (lc.m_costelement_id=ce.m_costelement_id) where ce.isactive='N') then amtacctdr-amtacctcr" +
					" else 0" +
					" end as Otros_Costos, " +
					" dameinfofactura(record_id,fa.ad_table_id) as info_factura" +
					" from fact_acct fa" +
					" inner join c_elementvalue ce On (fa.account_id=ce.c_elementvalue_id)" +
					" inner join m_product p ON (fa.m_product_id=p.m_product_id)" +
					" LEFT JOIN M_LOCATOR loc ON (fa.m_locator_id=loc.m_locator_id)" +
					" LEFT JOIN M_WAREHOUSE war ON (loc.m_warehouse_id=war.m_warehouse_id) " +
					" INNER JOIN AD_TABLE_TRL t ON (fa.ad_table_id=t.ad_table_id)" +
					" WHERE (ce.value='115010' OR ce.value='115011')" +
					" and fa.dateacct> ?) movs" +
					" WHERE movs.dateacct> ? AND movs.dateacct<=? AND movs.M_Product_id="+rs.getInt("M_Product_ID")+" And MOVS.OTROS_COSTOS=0 And movs.qty_real<>0 " +
					" ) movs2" +
					" GROUP BY ad_client_id, dateacct, datetrx, account_id, qty, m_locator_id, m_product_id, qty_real, ad_table_id, origen, record_id, line_id";
			//fecha de párametro directa en codigo
			Timestamp datePara = DateUtils.todayAdempiere();
			datePara.setYear(2011-1900);
			datePara.setMonth(11);
			datePara.setDate(31);
			String IssoTrx = "";
			PreparedStatement pstmt2 = null;
			pstmt2 = DB.prepareStatement (v_query, get_TrxName());
			pstmt2.setTimestamp(1, datePara);
			pstmt2.setTimestamp(2, fecha_desde);
			pstmt2.setTimestamp(3, DateUtils.todayAdempiere());
			ResultSet rs2 = pstmt2.executeQuery();	
			while (rs2.next())
			{ 
				m_inoutline_id = 0;
				m_inventoryline_id = 0;
				m_movementline_id = 0;
				m_productionline_id = 0;
				IssoTrx = "";
				
				switch (rs2.getInt("ad_table_id")) {
				case 319:
					m_inoutline_id = rs2.getInt("line_id");
					IssoTrx = DB.getSQLValueString(get_TrxName(), "select issotrx from m_inout where m_inout_id="+rs2.getInt("record_id"));					
					break;
				case 323:
					m_movementline_id = rs2.getInt("line_id");
					break;
				case 325:
					m_productionline_id = rs2.getInt("line_id");
					break;
				case 321:
					m_inventoryline_id = rs2.getInt("line_id");
					break;
				}
				
				if(IssoTrx.compareTo("N") == 0)
					concepto_link = cod_Blumos+"-COMPRAS";
				else if(IssoTrx.compareTo("Y") == 0)
					concepto_link = cod_Blumos+"-VENTAS";
				else
					concepto_link = cod_Blumos+"-OTROS";
					
				//ESCRIBE TARJETA DE EXISTENCIAS
				X_T_BL_CONEXI_SESIONLINE seLine = new X_T_BL_CONEXI_SESIONLINE(getCtx(), 0, get_TrxName()); 
				seLine.setAD_Org_ID(0);
				seLine.setT_BL_CONEXI_SESION_ID(conexi.get_ID());
				seLine.setM_Product_ID(rs2.getInt("M_Product_ID"));
				seLine.setCOD_BLUMOS(cod_Blumos);
				seLine.setIsSOTrx(IssoTrx.compareTo("Y")==0?true:false);
				seLine.setCANTIDAD(rs2.getBigDecimal("qty"));
				seLine.setCONCEPTO(rs2.getString("origen"));
				seLine.setDES_PRODUCTO(DB.getSQLValueString(get_TrxName(), "SELECT damenombreproducto(m_product_id,0) FROM M_Product WHERE M_product_ID = "+rs2.getInt("M_Product_ID")));
				seLine.setM_Locator_ID(rs2.getInt("M_Locator_ID"));
				seLine.setAD_Table_ID(rs2.getInt("AD_Table_ID"));
				seLine.setRecord_ID(rs2.getInt("Record_ID"));
				seLine.setLine_ID(rs2.getInt("Line_ID"));
				seLine.setORIGEN(rs2.getString("origen"));
				seLine.setDateAcct(rs2.getTimestamp("DateAcct"));
				seLine.setDateTrx(rs2.getTimestamp("DateTrx"));
				seLine.setCONCEPTO_LINK(concepto_link);
				seLine.setM_InOutLine_ID(m_inoutline_id);
				seLine.setM_InventoryLine_ID(m_inventoryline_id);
				seLine.setM_MovementLine_ID(m_movementline_id);
				seLine.setM_ProductionLine_ID(m_productionline_id);
				seLine.setH_O_P("HISTORICO");
				seLine.save(get_TrxName());
			}
			rs2.close (); pstmt2.close ();
			pstmt2 = null; rs2 = null;
			//CARGA DE MOVIMIENTOS PROYECTADOS
		    //COMPRAS SE HACE EN ESTA PASADA YA QUE ES POR M_PRODUCT_ID
			v_query = "SELECT ad_client_id, dateacct, datetrx, m_locator_id, m_product_id, CANTIDAD, c_orderline_id FROM RVBL_CONEXI_COMPRAS_P WHERE M_PRODUCT_ID="+rs.getInt("M_Product_ID");
				
			PreparedStatement pstmt3 = null;
			pstmt3 = DB.prepareStatement (v_query, get_TrxName());
			ResultSet rs3 = pstmt3.executeQuery ();	
			concepto_link = cod_Blumos+"-COMPRAS";
			while (rs3.next())
			{ 
				X_T_BL_CONEXI_SESIONLINE seLine2 = new X_T_BL_CONEXI_SESIONLINE(getCtx(), 0, get_TrxName());
				seLine2.setAD_Org_ID(0);
				seLine2.setT_BL_CONEXI_SESION_ID(conexi.get_ID());				
				seLine2.setM_Product_ID(rs3.getInt("M_Product_ID"));
				seLine2.setCOD_BLUMOS(cod_Blumos);				
				seLine2.setIsSOTrx(false);
				seLine2.setCANTIDAD(rs3.getBigDecimal("CANTIDAD"));
				seLine2.setCONCEPTO("");
				seLine2.setDES_PRODUCTO(DB.getSQLValueString(get_TrxName(), "SELECT damenombreproducto(m_product_id,0) FROM M_Product WHERE M_product_ID = "+rs3.getInt("M_Product_ID")));
				seLine2.setM_Locator_ID(rs3.getInt("M_Locator_ID"));
				seLine2.setLine_ID(rs3.getInt("C_Orderline_ID"));
				seLine2.setDateAcct(rs3.getTimestamp("DateAcct"));
				seLine2.setDateTrx(rs3.getTimestamp("DateTrx"));				
				seLine2.setCONCEPTO_LINK(concepto_link);
				seLine2.setC_OrderLine_ID(rs3.getInt("C_Orderline_ID"));
				seLine2.setH_O_P("PROYECTADO");
				seLine2.save(get_TrxName());
			}	
			rs3.close (); pstmt3.close ();
			pstmt3 = null; rs3 = null;
		}
		rs.close (); pstmt.close ();
		pstmt = null; rs = null;
		//VENTAS
		String v_query = "SELECT CP.ad_client_id, CPL.FECHA_P AS dateacct, CANTIDAD_P AS CANTIDAD, DESCRIPTION, T_BL_CONEXI_PROYLINE_ID, cpl.c_bpartner_id FROM T_BL_CONEXI_PROYLINE CPL " +
				" INNER JOIN T_BL_CONEXI_PROY CP ON (cpl.t_bl_conexi_proy_id=CP.T_BL_CONEXI_PROY_ID) WHERE CP.COD_BLUMOS='"+cod_Blumos+"' and cpl.fecha_p> ? and 1=1 ";
		
		PreparedStatement pstmt4 = null;
		pstmt4 = DB.prepareStatement (v_query, get_TrxName());
		pstmt4.setTimestamp(1,inicio_mes);
		ResultSet rs4 = pstmt4.executeQuery ();	
		concepto_link = cod_Blumos+"-VENTAS";
		while (rs4.next())
		{ 
			X_T_BL_CONEXI_SESIONLINE seLine = new X_T_BL_CONEXI_SESIONLINE(getCtx(), 0, get_TrxName());
			seLine.setAD_Org_ID(0);
			seLine.setT_BL_CONEXI_SESION_ID(conexi.get_ID());
			seLine.setCOD_BLUMOS(cod_Blumos);
			seLine.setIsSOTrx(true);
			seLine.setCANTIDAD(rs4.getBigDecimal("CANTIDAD").negate());
			seLine.setDateAcct(rs4.getTimestamp("DateAcct"));
			seLine.setCONCEPTO_LINK(concepto_link);
			seLine.setT_BL_CONEXI_PROYLINE_ID(rs4.getInt("T_BL_CONEXI_PROYLINE_ID"));
			seLine.setDescription(rs4.getString("Description"));
			seLine.setH_O_P("PROYECTADO");
			seLine.setC_BPartner_ID(rs4.getInt("C_BPartner_ID"));
			seLine.save(get_TrxName());
		}	
		rs4.close (); pstmt4.close ();
		pstmt4 = null; rs4 = null;
		
		/*******************************************************************************************************/
		//	SEGUNDA SECCION - ESCRITURA DE TABLA SALDOS
		// obtencion de saldos otras bodegas
		sob[5]= stb[5].subtract(sbc[5]==null?Env.ZERO:sbc[5]);
		sob[4]= stb[4].subtract(sbc[4]==null?Env.ZERO:sbc[4]);
		sob[3]= stb[3].subtract(sbc[3]==null?Env.ZERO:sbc[3]);
		sob[2]= stb[2].subtract(sbc[2]==null?Env.ZERO:sbc[2]);
		sob[1]= stb[1].subtract(sbc[1]==null?Env.ZERO:sbc[1]);
		sob[0]= stb[0].subtract(sbc[0]==null?Env.ZERO:sbc[0]);
		//PROCESO PREVIO PARA CALCULOS DE PROYECCIONES Y FECHAS LIMITE
	    //DETERMINACION DE FECHAS LIMITE      
		for(int a=5; a>=0; a--)
		{
			la_fecha[a] = DateUtils.UltimoDiaMes(DateUtils.SumarMeses(DateUtils.todayAdempiere(),(a+1)*-1));
			la_fecha2[a] = DateUtils.UltimoDiaMes(DateUtils.SumarMeses(DateUtils.todayAdempiere(),(a+1)));
		}
		// DETERMINACION DE COMPRAS proyectadas
		v_verif = DB.getSQLValue(get_TrxName(), "select count(t_bl_conexi_sesionline_id) from t_bl_conexi_sesionline" +
				" where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND h_o_p='PROYECTADO' AND issotrx='N' " +
				" AND COD_BLUMOS='"+cod_Blumos+"'");
		if(v_verif == 0)
		{
			acum_scp = Env.ZERO;
	        acumcp[0]= Env.ZERO;
	        acumcp[1]= Env.ZERO;
	        acumcp[2]= Env.ZERO;
	        acumcp[3]= Env.ZERO;
	        acumcp[4]= Env.ZERO;
		}
		else
		{
			//codigo que traera los 5 valores dentro de un ciclo para no hacer tantas consultas a la DB 
			String v_queryAcum = "select sum(p0) as p0, sum(p1) as p1, sum(p2) as p2, sum(p3) as p3, sum(p4) as p4, sum(p5) as p5 from " +
					"(SELECT CASE WHEN dateacct<=? then cantidad else 0" +
					" END as p0," +
					" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
					" END as p1," +
					" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
					" END as p2," +
					" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
					" END as p3," +
					" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
					" END as p4," +
					" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
					" END as p5,cod_blumos" +
					" from t_bl_conexi_sesionline" +
					" where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND h_o_p='PROYECTADO' AND issotrx='N' " +
					" AND COD_BLUMOS='"+cod_Blumos+"')as salida group by cod_blumos";
	
			PreparedStatement pstmt5 = null;
			pstmt5 = DB.prepareStatement (v_queryAcum, get_TrxName());
			pstmt5.setTimestamp(1,fin_mes);
			pstmt5.setTimestamp(2,fin_mes);
			pstmt5.setTimestamp(3,la_fecha2[0]);
			pstmt5.setTimestamp(4,la_fecha2[0]);
			pstmt5.setTimestamp(5,la_fecha2[1]);
			pstmt5.setTimestamp(6,la_fecha2[1]);
			pstmt5.setTimestamp(7,la_fecha2[2]);
			pstmt5.setTimestamp(8,la_fecha2[2]);
			pstmt5.setTimestamp(9,la_fecha2[3]);
			pstmt5.setTimestamp(10,la_fecha2[3]);
			pstmt5.setTimestamp(11,la_fecha2[4]);
			ResultSet rs5 = pstmt5.executeQuery ();	
			while (rs5.next())
			{
				acum_scp = acum_scp.add(rs5.getBigDecimal("p0")==null?Env.ZERO:rs5.getBigDecimal("p0"));
				acumcp[0] = acumcp[0].add(rs5.getBigDecimal("p1")==null?Env.ZERO:rs5.getBigDecimal("p1"));
				acumcp[1] = acumcp[1].add(rs5.getBigDecimal("p2")==null?Env.ZERO:rs5.getBigDecimal("p2"));
				acumcp[2] = acumcp[2].add(rs5.getBigDecimal("p3")==null?Env.ZERO:rs5.getBigDecimal("p3"));
				acumcp[3] = acumcp[3].add(rs5.getBigDecimal("p4")==null?Env.ZERO:rs5.getBigDecimal("p4"));
				acumcp[4] = acumcp[4].add(rs5.getBigDecimal("p5")==null?Env.ZERO:rs5.getBigDecimal("p5"));				
			}
			rs5.close (); pstmt5.close ();
			pstmt5 = null; rs5 = null;
		}	      
		// DETERMINACION DE ventas proyectadas
		v_verif = DB.getSQLValue(get_TrxName(), "select count(t_bl_conexi_sesionline_id) from t_bl_conexi_sesionline" +
				" where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND h_o_p='PROYECTADO' AND issotrx='Y' " +
				" AND COD_BLUMOS='"+cod_Blumos+"'");
		if(v_verif == 0)
		{
			acum_svp = Env.ZERO;
	        acumvp[0]= Env.ZERO;
	        acumvp[1]= Env.ZERO;
	        acumvp[2]= Env.ZERO;
	        acumvp[3]= Env.ZERO;
	        acumvp[4]= Env.ZERO;
		}
		else
		{
			//codigo que traera los 6 valores dentro de un ciclo para no hacer tantas consultas a la DB 
			String v_queryAcum = "select sum(p0) as p0, sum(p1) as p1, sum(p2) as p2, sum(p3) as p3, sum(p4) as p4, sum(p5) as p5 from " +
					"(SELECT CASE WHEN dateacct<=? then cantidad else 0" +
					" END as p0," +
					" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
					" END as p1," +
					" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
					" END as p2," +
					" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
					" END as p3," +
					" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
					" END as p4," +
					" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
					" END as p5,cod_blumos" +
					" from t_bl_conexi_sesionline" +
					" where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND h_o_p='PROYECTADO' AND issotrx='Y' " +
					" AND COD_BLUMOS='"+cod_Blumos+"') as salida group by cod_blumos";
	
			PreparedStatement pstmt5 = null;
			pstmt5 = DB.prepareStatement (v_queryAcum, get_TrxName());
			pstmt5.setTimestamp(1,fin_mes);
			pstmt5.setTimestamp(2,fin_mes);
			pstmt5.setTimestamp(3,la_fecha2[0]);
			pstmt5.setTimestamp(4,la_fecha2[0]);
			pstmt5.setTimestamp(5,la_fecha2[1]);
			pstmt5.setTimestamp(6,la_fecha2[1]);
			pstmt5.setTimestamp(7,la_fecha2[2]);
			pstmt5.setTimestamp(8,la_fecha2[2]);
			pstmt5.setTimestamp(9,la_fecha2[3]);
			pstmt5.setTimestamp(10,la_fecha2[3]);
			pstmt5.setTimestamp(11,la_fecha2[4]);
			ResultSet rs5 = pstmt5.executeQuery ();	
			if (rs5.next())
			{
				acum_svp = rs5.getBigDecimal("p0")==null?Env.ZERO:rs5.getBigDecimal("p0");
				acumvp[0] = rs5.getBigDecimal("p1")==null?Env.ZERO:rs5.getBigDecimal("p1");
				acumvp[1] = rs5.getBigDecimal("p2")==null?Env.ZERO:rs5.getBigDecimal("p2");
				acumvp[2] = rs5.getBigDecimal("p3")==null?Env.ZERO:rs5.getBigDecimal("p3");
				acumvp[3] = rs5.getBigDecimal("p4")==null?Env.ZERO:rs5.getBigDecimal("p4");
				acumvp[4] = rs5.getBigDecimal("p5")==null?Env.ZERO:rs5.getBigDecimal("p5");				
			}
			rs5.close (); pstmt5.close ();
			pstmt5 = null; rs5 = null;
		}		
		// determinacion de saldos proyectados //se niegan montos por descuadre. Una vez encontrado el problema no negar
	    acumsaldoP[0] =sbc[0].add(acum_scp).add(acum_svp);
	    acumsaldoP[1] =acumsaldoP[0].add(acumcp[0]).add(acumvp[0]);
	    acumsaldoP[2] =acumsaldoP[1].add(acumcp[1]).add(acumvp[1]);
	    acumsaldoP[3] =acumsaldoP[2].add(acumcp[2]).add(acumvp[2]);
	    acumsaldoP[4] =acumsaldoP[3].add(acumcp[3]).add(acumvp[3]);
	    acumsaldoP[5] =acumsaldoP[4].add(acumcp[4]).add(acumvp[4]);
	    
	    //ESCRITURA DE DATOS EN T_BL_CONEXI_SESION_SALDOS (2A PESTAÑA)
	    X_T_BL_CONEXI_SESION_SALDOS saldos = new X_T_BL_CONEXI_SESION_SALDOS(getCtx(), 0, get_TrxName());
	    saldos.setAD_Org_ID(0);
	    saldos.setT_BL_CONEXI_SESION_ID(conexi.get_ID());
	    saldos.setCOD_BLUMOS(cod_Blumos);
	    saldos.setH5(stb[4]);
	    saldos.setH4(stb[3]);
	    saldos.setH3(stb[2]);
	    saldos.setH2(stb[1]);
	    saldos.setH1(stb[0]);
	    saldos.setH0(saldo_actual);
	    saldos.setP0(acumsaldoP[0]);
	    saldos.setP1(acumsaldoP[1]);
	    saldos.setP2(acumsaldoP[2]);
	    saldos.setP3(acumsaldoP[3]);
	    saldos.setP4(acumsaldoP[4]);
	    saldos.setP5(acumsaldoP[5]);
	    saldos.save(get_TrxName());
	 
	    //ESCRITURA DE DATOS EN T_BL_CONEXI_SESION_SALDLINE (3A PESTAÑA)
	    //SALDOS APERTURA
	    X_T_BL_CONEXI_SESION_SALDLINE saLine = new X_T_BL_CONEXI_SESION_SALDLINE(getCtx(), 0, get_TrxName());
	    saLine.setAD_Org_ID(0);
	    saLine.setT_BL_CONEXI_SESION_ID(conexi.get_ID());
	    saLine.setT_BL_CONEXI_SESION_SALDOS_ID(saldos.get_ID());
	    saLine.setCOD_BLUMOS(cod_Blumos);
	    saLine.setH5(sbc[5]);
	    saLine.setH4(sbc[4]);
	    saLine.setH3(sbc[3]);
	    saLine.setH2(sbc[2]);
	    saLine.setH1(sbc[1]);
	    saLine.setH0(sbc[0]);
	    saLine.setP0(sbc[0]);
	    saLine.setP1(acumsaldoP[0]);
	    saLine.setP2(acumsaldoP[1]);
	    saLine.setP3(acumsaldoP[2]);
	    saLine.setP4(acumsaldoP[3]);
	    saLine.setP5(acumsaldoP[4]);
	    saLine.setCONCEPTO("SALDO AP. CENTRAL");
	    saLine.save(get_TrxName());
	    
	    X_T_BL_CONEXI_SESION_SALDLINE saLine2 = new X_T_BL_CONEXI_SESION_SALDLINE(getCtx(), 0, get_TrxName());
	    saLine2.setAD_Org_ID(0);
	    saLine2.setT_BL_CONEXI_SESION_ID(conexi.get_ID());
	    saLine2.setT_BL_CONEXI_SESION_SALDOS_ID(saldos.get_ID());
	    saLine2.setCOD_BLUMOS(cod_Blumos);
	    saLine2.setH5(sob[5]);
	    saLine2.setH4(sob[4]);
	    saLine2.setH3(sob[3]);
	    saLine2.setH2(sob[2]);
	    saLine2.setH1(sob[1]);
	    saLine2.setH0(sob[0]);
	    saLine2.setP0(Env.ZERO);
	    saLine2.setP1(Env.ZERO);
	    saLine2.setP2(Env.ZERO);
	    saLine2.setP3(Env.ZERO);
	    saLine2.setP4(Env.ZERO);
	    saLine2.setP5(Env.ZERO);
	    saLine2.setCONCEPTO("SALDO AP. OTRAS");
	    saLine2.save(get_TrxName());
	    
	    //DETERMINACION DE COMPRAS historicas
	    v_verif = DB.getSQLValue(get_TrxName(), "select count(t_bl_conexi_sesionline_id) from t_bl_conexi_sesionline " +
	    		" where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND ad_table_id=319 AND issotrx='N' AND COD_BLUMOS='"+cod_Blumos+"'");
	    if(v_verif == 0)
	    {
	    	acum[0] = Env.ZERO;
	    	acum[1] = Env.ZERO;
	    	acum[2] = Env.ZERO;
	    	acum[3] = Env.ZERO;
	    	acum[4] = Env.ZERO;
	    	acum_sr = Env.ZERO;
	    }
	    else
	    {
	    	//codigo que traera los 6 valores dentro de un ciclo para no hacer tantas consultas a la DB 
			String v_queryH = "select sum(h5) as h5, sum(h4) as h4, sum(h3) as h3, sum(h2) as h2, sum(h1) as h1, sum(h0) as h0 from " +
				"(SELECT CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h5," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h4," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h3," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h2," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h1," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h0,cod_blumos" +
				" from t_bl_conexi_sesionline" +
				" where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND ad_table_id=319 AND issotrx='N' AND COD_BLUMOS='"+cod_Blumos+"') as salida" +
				" group by cod_blumos";	
			PreparedStatement pstmt6 = null;
			pstmt6 = DB.prepareStatement (v_queryH, get_TrxName());
			pstmt6.setTimestamp(1,la_fecha[5]);
			pstmt6.setTimestamp(2,la_fecha[4]);
			pstmt6.setTimestamp(3,la_fecha[4]);
			pstmt6.setTimestamp(4,la_fecha[3]);
			pstmt6.setTimestamp(5,la_fecha[3]);
			pstmt6.setTimestamp(6,la_fecha[2]);
			pstmt6.setTimestamp(7,la_fecha[2]);
			pstmt6.setTimestamp(8,la_fecha[1]);
			pstmt6.setTimestamp(9,la_fecha[1]);
			pstmt6.setTimestamp(10,la_fecha[0]);
			pstmt6.setTimestamp(11,la_fecha[0]);
			pstmt6.setTimestamp(12,DateUtils.todayAdempiere());
			ResultSet rs6 = pstmt6.executeQuery ();	
			if (rs6.next())
			{
				acum[4] = rs6.getBigDecimal("h5")==null?Env.ZERO:rs6.getBigDecimal("h5");
				acum[3] = rs6.getBigDecimal("h4")==null?Env.ZERO:rs6.getBigDecimal("h4");
				acum[2] = rs6.getBigDecimal("h3")==null?Env.ZERO:rs6.getBigDecimal("h3");
				acum[1] = rs6.getBigDecimal("h2")==null?Env.ZERO:rs6.getBigDecimal("h2");
				acum[0] = rs6.getBigDecimal("h1")==null?Env.ZERO:rs6.getBigDecimal("h1");
				acum_sr = rs6.getBigDecimal("h0")==null?Env.ZERO:rs6.getBigDecimal("h0");			
			}
			rs6.close (); pstmt6.close ();
			pstmt6 = null; rs6 = null;
	    }
	     //ESCRIBIMOS SUMA DE COMPRAS
	    concepto_link = cod_Blumos+"-COMPRAS";
	    X_T_BL_CONEXI_SESION_SALDLINE saLine3 = new X_T_BL_CONEXI_SESION_SALDLINE(getCtx(), 0, get_TrxName());
	    saLine3.setAD_Org_ID(0);
	    saLine3.setT_BL_CONEXI_SESION_ID(conexi.get_ID());	    
	    saLine3.setT_BL_CONEXI_SESION_SALDOS_ID(saldos.get_ID());	    
	    saLine3.setCOD_BLUMOS(cod_Blumos);	    
	    saLine3.setH5(acum[4]);
	    saLine3.setH4(acum[3]);
	    saLine3.setH3(acum[2]);
	    saLine3.setH2(acum[1]);
	    saLine3.setH1(acum[0]);
	    saLine3.setH0(acum_sr);
	    saLine3.setP0(acum_scp);
	    saLine3.setP1(acumcp[0]);
	    saLine3.setP2(acumcp[1]);
	    saLine3.setP3(acumcp[2]);
	    saLine3.setP4(acumcp[3]);
	    saLine3.setP5(acumcp[4]);
	    saLine3.setCONCEPTO("COMPRAS");
	    saLine3.setCONCEPTO_LINK(concepto_link);
	    saLine3.save(get_TrxName());
	    
	    // --DETERMINACION DE VENTAS HISTORICAS
	    v_verif = DB.getSQLValue(get_TrxName(), "select count(t_bl_conexi_sesionline_id) from t_bl_conexi_sesionline" +
	    		" where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND ad_table_id=319 AND issotrx='Y' AND COD_BLUMOS='"+cod_Blumos+"'");
	    if(v_verif == 0)
	    {
	    	acum[0] = Env.ZERO;
	    	acum[1] = Env.ZERO;
	    	acum[2] = Env.ZERO;
	    	acum[3] = Env.ZERO;
	    	acum[4] = Env.ZERO;
	    	acum_sr = Env.ZERO;
	    }
	    else
	    {
	    	//codigo que traera los 6 valores dentro de un ciclo para no hacer tantas consultas a la DB 
			String v_queryH = "select sum(h5) as h5, sum(h4) as h4, sum(h3) as h3, sum(h2) as h2, sum(h1) as h1, sum(h0) as h0 from " +
				"(SELECT CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h5," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h4," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h3," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h2," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h1," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h0,cod_blumos" +
				" from t_bl_conexi_sesionline" +
				" where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND ad_table_id=319 AND issotrx='Y' AND COD_BLUMOS='"+cod_Blumos+"') as salida" +
				" group by cod_blumos";	
			PreparedStatement pstmt6 = null;
			pstmt6 = DB.prepareStatement (v_queryH, get_TrxName());
			pstmt6.setTimestamp(1,la_fecha[5]);
			pstmt6.setTimestamp(2,la_fecha[4]);
			pstmt6.setTimestamp(3,la_fecha[4]);
			pstmt6.setTimestamp(4,la_fecha[3]);
			pstmt6.setTimestamp(5,la_fecha[3]);
			pstmt6.setTimestamp(6,la_fecha[2]);
			pstmt6.setTimestamp(7,la_fecha[2]);
			pstmt6.setTimestamp(8,la_fecha[1]);
			pstmt6.setTimestamp(9,la_fecha[1]);
			pstmt6.setTimestamp(10,la_fecha[0]);
			pstmt6.setTimestamp(11,la_fecha[0]);
			pstmt6.setTimestamp(12,DateUtils.todayAdempiere());
			ResultSet rs6 = pstmt6.executeQuery ();	
			if (rs6.next())
			{
				acum[4] = rs6.getBigDecimal("h5")==null?Env.ZERO:rs6.getBigDecimal("h5");
				acum[3] = rs6.getBigDecimal("h4")==null?Env.ZERO:rs6.getBigDecimal("h4");
				acum[2] = rs6.getBigDecimal("h3")==null?Env.ZERO:rs6.getBigDecimal("h3");
				acum[1] = rs6.getBigDecimal("h2")==null?Env.ZERO:rs6.getBigDecimal("h2");
				acum[0] = rs6.getBigDecimal("h1")==null?Env.ZERO:rs6.getBigDecimal("h1");
				acum_sr = rs6.getBigDecimal("h0")==null?Env.ZERO:rs6.getBigDecimal("h0");			
			}
			rs6.close (); pstmt6.close ();
			pstmt6 = null; rs6 = null;
	    }
	     //ESCRIBIMOS SUMA DE ventas
	    concepto_link = cod_Blumos+"-VENTAS";
	    X_T_BL_CONEXI_SESION_SALDLINE saLine4 = new X_T_BL_CONEXI_SESION_SALDLINE(getCtx(), 0, get_TrxName());
	    saLine4.setAD_Org_ID(0);
	    saLine4.setT_BL_CONEXI_SESION_ID(conexi.get_ID());	    
	    saLine4.setT_BL_CONEXI_SESION_SALDOS_ID(saldos.get_ID());	    
	    saLine4.setCOD_BLUMOS(cod_Blumos);	    
	    saLine4.setH5(acum[4]);
	    saLine4.setH4(acum[3]);
	    saLine4.setH3(acum[2]);
	    saLine4.setH2(acum[1]);
	    saLine4.setH1(acum[0]);
	    saLine4.setH0(acum_sr);
	    saLine4.setP0(acum_svp);
	    saLine4.setP1(acumvp[0]);
	    saLine4.setP2(acumvp[1]);
	    saLine4.setP3(acumvp[2]);
	    saLine4.setP4(acumvp[3]);
	    saLine4.setP5(acumvp[4]);
	    saLine4.setCONCEPTO("VENTAS");
	    saLine4.setCONCEPTO_LINK(concepto_link);
	    saLine4.save(get_TrxName());
	    
	    //DETERMINACION DE OTROS CONCEPTOS
	    v_verif = DB.getSQLValue(get_TrxName(), "select count(t_bl_conexi_sesionline_id) from t_bl_conexi_sesionline " +
	    		" where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND ad_table_id<>319 AND COD_BLUMOS='"+cod_Blumos+"'");
	    if(v_verif == 0)
	    {
	    	acum[0] = Env.ZERO;
	    	acum[1] = Env.ZERO;
	    	acum[2] = Env.ZERO;
	    	acum[3] = Env.ZERO;
	    	acum[4] = Env.ZERO;
	    	acum_sr = Env.ZERO;
	    }
	    else
	    {
	    	//codigo que traera los 6 valores dentro de un ciclo para no hacer tantas consultas a la DB 
			String v_queryH = "select sum(h5) as h5, sum(h4) as h4, sum(h3) as h3, sum(h2) as h2, sum(h1) as h1, sum(h0) as h0 from " +
				"(SELECT CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h5," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h4," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h3," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h2," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h1," +
				" CASE WHEN dateacct>? and dateacct<=? then cantidad else 0" +
				" END as h0,cod_blumos " +
				" from t_bl_conexi_sesionline" +
				" where t_bl_conexi_sesion_id="+conexi.get_ID()+" AND ad_table_id<>319 AND COD_BLUMOS='"+cod_Blumos+"') as salida" +
				" group by cod_blumos";	
			PreparedStatement pstmt6 = null;
			pstmt6 = DB.prepareStatement (v_queryH, get_TrxName());
			pstmt6.setTimestamp(1,la_fecha[5]);
			pstmt6.setTimestamp(2,la_fecha[4]);
			pstmt6.setTimestamp(3,la_fecha[4]);
			pstmt6.setTimestamp(4,la_fecha[3]);
			pstmt6.setTimestamp(5,la_fecha[3]);
			pstmt6.setTimestamp(6,la_fecha[2]);
			pstmt6.setTimestamp(7,la_fecha[2]);
			pstmt6.setTimestamp(8,la_fecha[1]);
			pstmt6.setTimestamp(9,la_fecha[1]);
			pstmt6.setTimestamp(10,la_fecha[0]);
			pstmt6.setTimestamp(11,la_fecha[0]);
			pstmt6.setTimestamp(12,DateUtils.todayAdempiere());
			ResultSet rs6 = pstmt6.executeQuery ();	
			if (rs6.next())
			{
				acum[4] = rs6.getBigDecimal("h5")==null?Env.ZERO:rs6.getBigDecimal("h5");
				acum[3] = rs6.getBigDecimal("h4")==null?Env.ZERO:rs6.getBigDecimal("h4");
				acum[2] = rs6.getBigDecimal("h3")==null?Env.ZERO:rs6.getBigDecimal("h3");
				acum[1] = rs6.getBigDecimal("h2")==null?Env.ZERO:rs6.getBigDecimal("h2");
				acum[0] = rs6.getBigDecimal("h1")==null?Env.ZERO:rs6.getBigDecimal("h1");
				acum_sr = rs6.getBigDecimal("h0")==null?Env.ZERO:rs6.getBigDecimal("h0");			
			}
			rs6.close (); pstmt6.close ();
			pstmt6 = null; rs6 = null;
	    }
	     //ESCRIBIMOS SUMA DE OTROS CONCEPTOS 
	    concepto_link = cod_Blumos+"-OTROS";
	    X_T_BL_CONEXI_SESION_SALDLINE saLine5 = new X_T_BL_CONEXI_SESION_SALDLINE(getCtx(), 0, get_TrxName());
	    saLine5.setAD_Org_ID(0);
	    saLine5.setT_BL_CONEXI_SESION_ID(conexi.get_ID());	    
	    saLine5.setT_BL_CONEXI_SESION_SALDOS_ID(saldos.get_ID());	    
	    saLine5.setCOD_BLUMOS(cod_Blumos);	    
	    saLine5.setH5(acum[4]);
	    saLine5.setH4(acum[3]);
	    saLine5.setH3(acum[2]);
	    saLine5.setH2(acum[1]);
	    saLine5.setH1(acum[0]);
	    saLine5.setH0(acum_sr);
	    saLine5.setP0(Env.ZERO);
	    saLine5.setP1(Env.ZERO);
	    saLine5.setP2(Env.ZERO);
	    saLine5.setP3(Env.ZERO);
	    saLine5.setP4(Env.ZERO);
	    saLine5.setP5(Env.ZERO);
	    saLine5.setCONCEPTO("OTROS");
	    saLine5.setCONCEPTO_LINK(concepto_link);
	    saLine5.save(get_TrxName());
	    
	    //ESCRIBIMOS SALDO FINAL
	    X_T_BL_CONEXI_SESION_SALDLINE saLine6 = new X_T_BL_CONEXI_SESION_SALDLINE(getCtx(), 0, get_TrxName());
	    saLine6.setAD_Org_ID(0);
	    saLine6.setT_BL_CONEXI_SESION_ID(conexi.get_ID());	    
	    saLine6.setT_BL_CONEXI_SESION_SALDOS_ID(saldos.get_ID());	    
	    saLine6.setCOD_BLUMOS(cod_Blumos);	    
	    saLine6.setH5(stb[4]);
	    saLine6.setH4(stb[3]);
	    saLine6.setH3(stb[2]);
	    saLine6.setH2(stb[1]);
	    saLine6.setH1(stb[0]);
	    saLine6.setH0(saldo_actual);
	    saLine6.setP0(acumsaldoP[0]);
	    saLine6.setP1(acumsaldoP[1]);
	    saLine6.setP2(acumsaldoP[2]);
	    saLine6.setP3(acumsaldoP[3]);
	    saLine6.setP4(acumsaldoP[4]);
	    saLine6.setP5(acumsaldoP[5]);
	    saLine6.setCONCEPTO("SALDO AL FIN DEL MES");
	    saLine6.save(get_TrxName());
	    
	    DB.executeUpdate("UPDATE t_bl_conexi_sesion_saldline SET representada=(select name from c_bpartner where c_bpartner_id=damerepresentadacod_blm('"+cod_Blumos+"'))" +
	    		" WHERE T_BL_CONEXI_SESION_ID="+conexi.get_ID()+" AND COD_BLUMOS='"+cod_Blumos+"'", get_TrxName());	  
	    
		return " OK "+cod_Blumos;
	}		
}	//	Replenish


