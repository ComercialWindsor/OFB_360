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
import java.math.BigDecimal;

import org.compiere.model.X_T_BL_ER_PARAMETROS;
import org.compiere.model.X_T_BL_ER_SALDOS;
import org.compiere.model.X_T_BL_ER_SALIDA;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  @author Italo Niñoles
 */
public class ProcessER extends SvrProcess
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
		X_T_BL_ER_PARAMETROS para = new X_T_BL_ER_PARAMETROS(getCtx(), Record_ID, get_TrxName());
		//se crean variable
		//variables para registrar totales del detalle que seran traspasados
		//a t_bl_er_salidas
		Timestamp V_DesdeA = null;
		Timestamp V_HastaA = null;
		Timestamp V_DesdeB = null;
		Timestamp V_HastaB = null;
		//par de variables para grandes totales
		BigDecimal V_GTotalA;
		BigDecimal V_GTotalB;
		int V_ID_Actual;
		//VARIABLES PARA LA OBTENCION DE LOS ID'S DE LAS CUENTAS DE ER Y BCE
		//CLIENT_ID NUMBER; se usara variable de entorno del registro
		
		//variables que simulan parametros de entrada
		//ID_PARAMETRO NUMBER(10,0);
		//ID_DEVUELTO NUMBER; -- variable para asignar IDs por tabla USANDO PROCEDIMIENTO NEXTID
		/*P_DESDE_A DATE;
		P_HASTA_A DATE;
		P_DESDE_B DATE;
		P_HASTA_B DATE;*/ //se usaran parametros del registro
		Timestamp P_IniA;
		Timestamp P_IniB;
		String sql_var_org;
	
		//identificacion de empresa ad_client_id
		if(para.getAD_Org_ID() == 0)
			sql_var_org = "";
		else
			sql_var_org = " AND AD_ORG_ID="+para.getAD_Org_ID();
		
		//VARIABLES PARA LA OBTENCION DE LOS ID'S DE LAS CUENTAS DE ER Y BCE
		//CLIENT_ID NUMBER; se usara variable de entorno del registro
		//DETERMINACION ELEMENTOS DE ESTADO DE RESULTADOS

		//TODOS ESTOS ELEMENTOS SE DEFINEN EN ADEMPIERE EN ELEMENTO CONTABLE, Y SON OBLIGATORIOS (OJO, ES PELIGROSO SER...)
		// INGRESOS OPERACIONALES (NIVEL DE PADRE)
		int E1 = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_ELEMENTVALUE_ID) FROM C_ELEMENTVALUE WHERE VALUE='411' AND AD_CLIENT_ID="+para.getAD_Client_ID());
		// COSTOS OPERACIONALES (NIVEL DE PADRE)
		int E2 = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_ELEMENTVALUE_ID) FROM C_ELEMENTVALUE WHERE VALUE='311' AND AD_CLIENT_ID="+para.getAD_Client_ID());
		// GASTOS OPERACIONALES (NIVEL DE ABUELO)
		int E3 = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_ELEMENTVALUE_ID) FROM C_ELEMENTVALUE WHERE VALUE='31' AND AD_CLIENT_ID="+para.getAD_Client_ID());
		// INGRESOS NO OPERACIONALES (NIVEL DE ABUELO)
		int E4 = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_ELEMENTVALUE_ID) FROM C_ELEMENTVALUE WHERE VALUE='42' AND AD_CLIENT_ID="+para.getAD_Client_ID());
		// GASTOS NO OPERACIONALES (NIVEL DE ABUELO)
		int E5 = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_ELEMENTVALUE_ID) FROM C_ELEMENTVALUE WHERE VALUE='32' AND AD_CLIENT_ID="+para.getAD_Client_ID());
		// CORRECCION MONETARIA (NIVEL DE ABUELO)
		int E6 = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_ELEMENTVALUE_ID) FROM C_ELEMENTVALUE WHERE VALUE='51' AND AD_CLIENT_ID="+para.getAD_Client_ID());
		// IMPUESTO A LA RENTA (NIVEL DE ABUELO)
		int E7 = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_ELEMENTVALUE_ID) FROM C_ELEMENTVALUE WHERE VALUE='52' AND AD_CLIENT_ID="+para.getAD_Client_ID());
		
		//BORRA T_BL_ER_SALDOS
		P_IniA = new Timestamp(para.getHASTA_A().getTime());
		P_IniA.setMonth(0);
		P_IniA.setDate(1);
		P_IniB = new Timestamp(para.getHASTA_B().getTime());
		P_IniB.setMonth(0);
		P_IniB.setDate(1);
		
		//BORRA ENCABEZADO DE T_BL_FLASH_VENTAS Y POR DEFINICION DE LLAVE 
		//BORRA DETALLE DE T_BL_FLASH_VENTAS_DETALLE
		
		DB.executeUpdate("DELETE FROM T_BL_ER_SALDOS WHERE T_BL_ER_PARAMETROS_ID="+para.get_ID(), get_TrxName());

		//Formacion de SQL variable
		String sqlVar = "";
		if(DB.isOracle())
		{
			sqlVar = "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,SYSDATE,100,SYSDATE,100,c_elementvalue_id," +
				" sum(saldo_a) as saldo_a, sum(saldo_b) as saldo_b,sum(saldos_a) as saldos_a, sum(saldos_b) as saldos_b," +
				" sum(debitos_a) as debitos_a, sum(creditos_a) as creditos_a,sum(debitos_b) as debitos_b, sum(creditos_b) as creditos_b," +
				" AccountType from c_elementvalue " +
				" inner join (select fa.account_id,(sum(fa.amtacctdr) - sum(fa.amtacctcr)) as saldo_a, 0 as saldo_b," +
				" 0 as saldos_a, 0 as saldos_b, 0 as debitos_a, 0 as creditos_a, 0 as debitos_b, 0 as creditos_b" +
				" from fact_acct fa WHERE fa.dateacct >= ? AND fa.dateacct <= ? "+sql_var_org+" GROUP BY fa.account_id" +
				" union all" +
				" select fa.account_id,0 as saldo_a, (sum(fa.amtacctdr) - sum(fa.amtacctcr)) as saldo_b," +
				" 0 as saldos_a, 0 as saldos_b, 0 as debitos_a, 0 as creditos_a, 0 as debitos_b, 0 as creditos_b" +
				" from fact_acct fa WHERE fa.dateacct >= ? AND fa.dateacct <= ? "+sql_var_org+" GROUP BY fa.account_id" +
				" union all" +
				" select fa.account_id, 0 as saldo_a, 0 as saldo_b," +
				" (sum(fa.amtacctdr) - sum(fa.amtacctcr)) as saldos_a, 0 as saldos_b, sum(fa.amtacctdr) as debitos_a, sum(fa.amtacctcr) as creditos_a, 0 as debitos_b, 0 as creditos_b" +
				" from fact_acct fa WHERE fa.dateacct >=? AND fa.dateacct <=? "+sql_var_org+" GROUP BY fa.account_id " +
				" union all" +
				" select fa.account_id,0 as saldo_a, 0 as saldo_b," +
				" 0 as saldos_a, (sum(fa.amtacctdr) - sum(fa.amtacctcr)) as saldos_b, 0 as debitos_a, 0 as creditos_a, sum(fa.amtacctdr) as debitos_b, sum(fa.amtacctcr) as creditos_b" +
				" from fact_acct fa WHERE fa.dateacct >= ? AND fa.dateacct <= ? "+sql_var_org+" GROUP BY fa.account_id) saldos on (c_elementvalue.c_elementvalue_id=saldos.account_id)" +
				" where ad_client_id="+para.getAD_Client_ID()+" AND issummary='N' AND ISACTIVE='Y'  " +
				" GROUP BY AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,SYSDATE,100,SYSDATE,100,c_elementvalue_id,AccountType ORDER BY c_elementvalue_id";
		}
		else
		{
			sqlVar = "select AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,SYSDATE,100,SYSDATE,100,c_elementvalue_id," +
			" sum(saldo_a) as saldo_a, sum(saldo_b) as saldo_b,sum(saldos_a) as saldos_a, sum(saldos_b) as saldos_b," +
			" sum(debitos_a) as debitos_a, sum(creditos_a) as creditos_a,sum(debitos_b) as debitos_b, sum(creditos_b) as creditos_b," +
			" AccountType from c_elementvalue " +
			" inner join (select fa.account_id,(sum(fa.amtacctdr) - sum(fa.amtacctcr)) as saldo_a, 0 as saldo_b," +
			" 0 as saldos_a, 0 as saldos_b, 0 as debitos_a, 0 as creditos_a, 0 as debitos_b, 0 as creditos_b" +
			" from fact_acct fa WHERE fa.dateacct >= ? AND fa.dateacct <= ? "+sql_var_org+" GROUP BY fa.account_id" +
			" union all" +
			" select fa.account_id,0 as saldo_a, (sum(fa.amtacctdr) - sum(fa.amtacctcr)) as saldo_b," +
			" 0 as saldos_a, 0 as saldos_b, 0 as debitos_a, 0 as creditos_a, 0 as debitos_b, 0 as creditos_b" +
			" from fact_acct fa WHERE fa.dateacct >= ? AND fa.dateacct <= ? "+sql_var_org+" GROUP BY fa.account_id" +
			" union all" +
			" select fa.account_id, 0 as saldo_a, 0 as saldo_b," +
			" (sum(fa.amtacctdr) - sum(fa.amtacctcr)) as saldos_a, 0 as saldos_b, sum(fa.amtacctdr) as debitos_a, sum(fa.amtacctcr) as creditos_a, 0 as debitos_b, 0 as creditos_b" +
			" from fact_acct fa WHERE fa.dateacct >=? AND fa.dateacct <=? "+sql_var_org+" GROUP BY fa.account_id " +
			" union all" +
			" select fa.account_id,0 as saldo_a, 0 as saldo_b," +
			" 0 as saldos_a, (sum(fa.amtacctdr) - sum(fa.amtacctcr)) as saldos_b, 0 as debitos_a, 0 as creditos_a, sum(fa.amtacctdr) as debitos_b, sum(fa.amtacctcr) as creditos_b" +
			" from fact_acct fa WHERE fa.dateacct >= ? AND fa.dateacct <= ? "+sql_var_org+" GROUP BY fa.account_id) saldos on (c_elementvalue.c_elementvalue_id=saldos.account_id)" +
			" where ad_client_id="+para.getAD_Client_ID()+" AND issummary='N' AND ISACTIVE='Y'  " +
			" GROUP BY AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,SYSDATE,SYSDATE,c_elementvalue_id,AccountType ORDER BY c_elementvalue_id";
		}
			
		
		log.config("sqldet "+sqlVar);
		PreparedStatement pstmt = null;
		PreparedStatement pstmtE1 = null;
		PreparedStatement pstmtE2 = null;
		PreparedStatement pstmtE3 = null;
		PreparedStatement pstmtE4 = null;
		PreparedStatement pstmtE5 = null;
		PreparedStatement pstmtE6 = null;
		PreparedStatement pstmtE7 = null;
		//try
		//{
			pstmt = DB.prepareStatement (sqlVar, get_TrxName());
			pstmt.setTimestamp(1, para.getDESDE_A());
			pstmt.setTimestamp(2, para.getHASTA_A());
			pstmt.setTimestamp(3, para.getDESDE_B());
			pstmt.setTimestamp(4, para.getHASTA_B());
			pstmt.setTimestamp(5, P_IniA);
			pstmt.setTimestamp(6, para.getHASTA_A());
			pstmt.setTimestamp(7, P_IniB);
			pstmt.setTimestamp(8, para.getHASTA_B());
			ResultSet rs = pstmt.executeQuery ();		
			log.config("Lista de parametros:");
			log.config("Para 1:"+para.getDESDE_A());
			log.config("Para 2"+para.getHASTA_A());
			log.config("Para 3"+para.getDESDE_B());
			log.config("Para 4"+para.getHASTA_B());
			log.config("Para 5"+P_IniA);
			log.config("Para 6"+para.getHASTA_A());
			log.config("Para 7"+P_IniB);
			log.config("Para 8"+para.getHASTA_B());
			
			while (rs.next())
			{
				X_T_BL_ER_SALDOS saldo = new X_T_BL_ER_SALDOS(getCtx(), 0, get_TrxName());
				saldo.setT_BL_ER_PARAMETROS_ID(para.get_ID());
				saldo.setAD_Org_ID(para.getAD_Org_ID());
				saldo.setDESDE_A(para.getDESDE_A());
				saldo.setHASTA_A(para.getHASTA_A());
				saldo.setDESDE_B(para.getDESDE_B());
				saldo.setHASTA_B(para.getHASTA_B());
				saldo.setAccount_ID(rs.getInt("c_elementvalue_id"));
				saldo.setSALDO_A(rs.getBigDecimal("saldo_a"));
				saldo.setSALDO_B(rs.getBigDecimal("saldo_b"));
				saldo.setSALDOHASTA_A(rs.getBigDecimal("saldos_a"));
				saldo.setSALDOHASTA_B(rs.getBigDecimal("saldos_b"));
				saldo.setDEBEHASTA_A(rs.getBigDecimal("debitos_a"));
				saldo.setHABERHASTA_A(rs.getBigDecimal("creditos_a"));//revisar este campo
				saldo.setDEBEHASTA_B(rs.getBigDecimal("debitos_b"));
				saldo.setHABERHASTA_B(rs.getBigDecimal("creditos_b"));
				saldo.setAccountType(rs.getString("AccountType"));
				saldo.setINI_A(P_IniA);
				saldo.setINI_B(P_IniB);
				saldo.saveEx();
			}
			rs.close (); pstmt.close ();
			pstmt = null; rs = null;
			//ELIMINACION DE CUENTAS SIN MOVIMIENTO Y CONVERSION DE NULOS A CERO
			DB.executeUpdate("update t_bl_er_saldos set saldo_a=0 where saldo_a is null",get_TrxName());
			DB.executeUpdate("update t_bl_er_saldos set saldo_b=0 where saldo_b is null",get_TrxName());
			DB.executeUpdate("update t_bl_er_saldos set saldohasta_A=0 where saldohasta_A is null",get_TrxName());
			DB.executeUpdate("update t_bl_er_saldos set saldohasta_b=0 where saldohasta_b is null",get_TrxName());
			DB.executeUpdate("update t_bl_er_saldos set debehasta_a=0 where debehasta_a is null",get_TrxName());
			DB.executeUpdate("update t_bl_er_saldos set debehasta_b=0 where debehasta_b is null",get_TrxName());
			DB.executeUpdate("update t_bl_er_saldos set haberhasta_A=0 where Haberhasta_A is null",get_TrxName());
			DB.executeUpdate("update t_bl_er_saldos set haberhasta_b=0 where haberhasta_b is null",get_TrxName());
			
			//CONSTRUCCION ESTADO DE RESULTADOS
			//vaciamos la tabla de salida 
			DB.executeUpdate("DELETE FROM T_BL_ER_SALIDA WHERE T_BL_ER_PARAMETROS_ID="+para.get_ID(),get_TrxName());
			//ESCRIBIMOS PRIMER TITULO
			X_T_BL_ER_SALIDA tSalida = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
			tSalida.setT_BL_ER_PARAMETROS_ID(para.get_ID());
			tSalida.setAD_Org_ID(para.getAD_Org_ID());
			tSalida.setNOMBRE_GTOTAL("RESULTADO OPERACIONAL");
			tSalida.setBANDERA_GTOTAL(false);
			tSalida.saveEx();
			
			//INGRESOS OPERACIONALES
			String sqlE1 = "select pc.nombre_abuelo, pc.nombre_padre, pc.account_id, pc.value, pc.NAME, ts.saldo_a, ts.saldo_b, ts.desde_a, ts.hasta_a, ts.desde_b, ts.hasta_b" +
					" from rvbl_plandecuentas pc INNER JOIN t_bl_er_saldos ts On (pc.account_id=ts.account_id)" +
					" where id_padre = "+E1+" AND T_BL_ER_PARAMETROS_ID="+para.get_ID();		
			
			pstmtE1 = DB.prepareStatement (sqlE1, get_TrxName());			
			ResultSet rsE1 = pstmtE1.executeQuery ();			
			while (rsE1.next ())
			{
				X_T_BL_ER_SALIDA salidaE1 = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
				salidaE1.setT_BL_ER_PARAMETROS_ID(para.get_ID());
				salidaE1.setAD_Org_ID(para.getAD_Org_ID());
				salidaE1.setNOMBRE_ABUELO(rsE1.getString("nombre_abuelo"));
				salidaE1.setNOMBRE_PADRE(rsE1.getString("nombre_padre"));
				salidaE1.setAccount_ID(rsE1.getInt("account_id"));
				salidaE1.setCUENTA(rsE1.getString("value"));
				salidaE1.setNOMBRE_CUENTA(rsE1.getString("NAME"));
				salidaE1.setSALDO_A(rsE1.getBigDecimal("saldo_a"));
				salidaE1.setSALDO_B(rsE1.getBigDecimal("saldo_b"));
				salidaE1.setDESDE_A(rsE1.getTimestamp("desde_a"));
				salidaE1.setHASTA_A(rsE1.getTimestamp("hasta_a"));
				salidaE1.setDESDE_B(rsE1.getTimestamp("desde_b"));
				salidaE1.setHASTA_B(rsE1.getTimestamp("hasta_b"));
				salidaE1.saveEx();
			}
			pstmtE1.close ();rsE1.close(); 
			pstmtE1 = null; rsE1 = null;
			
			//COSTOS DE LA OPERACION
			String sqlE2 = "select pc.nombre_abuelo, pc.nombre_padre, pc.account_id, pc.value, pc.NAME, ts.saldo_a, ts.saldo_b, ts.desde_a, ts.hasta_a, ts.desde_b, ts.hasta_b" +
					" from rvbl_plandecuentas pc" +
					" INNER JOIN t_bl_er_saldos ts On (pc.account_id=ts.account_id)" +
					" where id_padre= "+E2+" AND T_BL_ER_PARAMETROS_ID="+para.get_ID()+" ORDER BY pc.sec_padre, sec_cuenta";		
	
			pstmtE2 = DB.prepareStatement (sqlE2, get_TrxName());			
			ResultSet rsE2 = pstmtE2.executeQuery ();			
			while (rsE2.next ())
			{
				X_T_BL_ER_SALIDA salidaE2 = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
				salidaE2.setT_BL_ER_PARAMETROS_ID(para.get_ID());
				salidaE2.setAD_Org_ID(para.getAD_Org_ID());
				salidaE2.setNOMBRE_ABUELO(rsE2.getString("nombre_abuelo"));
				salidaE2.setNOMBRE_PADRE(rsE2.getString("nombre_padre"));
				salidaE2.setAccount_ID(rsE2.getInt("account_id"));
				salidaE2.setCUENTA(rsE2.getString("value"));
				salidaE2.setNOMBRE_CUENTA(rsE2.getString("NAME"));
				salidaE2.setSALDO_A(rsE2.getBigDecimal("saldo_a"));
				salidaE2.setSALDO_B(rsE2.getBigDecimal("saldo_b"));
				salidaE2.setDESDE_A(rsE2.getTimestamp("desde_a"));
				salidaE2.setHASTA_A(rsE2.getTimestamp("hasta_a"));
				salidaE2.setDESDE_B(rsE2.getTimestamp("desde_b"));
				salidaE2.setHASTA_B(rsE2.getTimestamp("hasta_b"));
				salidaE2.saveEx();
			}
			pstmtE2.close ();rsE2.close(); 
			pstmtE2 = null; rsE2 = null;
			
			// GASTOS OPERACIONALES
			String sqlE3 = "select pc.nombre_abuelo, pc.nombre_padre, pc.account_id, pc.value, pc.NAME, ts.saldo_a, ts.saldo_b, ts.desde_a, ts.hasta_a, ts.desde_b, ts.hasta_b" +
					" from rvbl_plandecuentas pc" +
					" INNER JOIN t_bl_er_saldos ts On (pc.account_id=ts.account_id)" +
					" where pc.id_abuelo="+E3+" AND pc.id_padre<> "+E2+" AND T_BL_ER_PARAMETROS_ID="+para.get_ID()+
					" ORDER BY pc.sec_padre, sec_cuenta";
			
			pstmtE3 = DB.prepareStatement (sqlE3, get_TrxName());			
			ResultSet rsE3 = pstmtE3.executeQuery ();			
			while (rsE3.next ())
			{
				X_T_BL_ER_SALIDA salidaE3 = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
				salidaE3.setT_BL_ER_PARAMETROS_ID(para.get_ID());
				salidaE3.setAD_Org_ID(para.getAD_Org_ID());
				salidaE3.setNOMBRE_ABUELO(rsE3.getString("nombre_abuelo"));
				salidaE3.setNOMBRE_PADRE(rsE3.getString("nombre_padre"));
				salidaE3.setAccount_ID(rsE3.getInt("account_id"));
				salidaE3.setCUENTA(rsE3.getString("value"));
				salidaE3.setNOMBRE_CUENTA(rsE3.getString("NAME"));
				salidaE3.setSALDO_A(rsE3.getBigDecimal("saldo_a"));
				salidaE3.setSALDO_B(rsE3.getBigDecimal("saldo_b"));
				salidaE3.setDESDE_A(rsE3.getTimestamp("desde_a"));
				salidaE3.setHASTA_A(rsE3.getTimestamp("hasta_a"));
				salidaE3.setDESDE_B(rsE3.getTimestamp("desde_b"));
				salidaE3.setHASTA_B(rsE3.getTimestamp("hasta_b"));
				salidaE3.saveEx();
				//se setean variables globales
				V_DesdeA = rsE3.getTimestamp("desde_a");
				V_HastaA = rsE3.getTimestamp("hasta_a");
				V_DesdeB = rsE3.getTimestamp("desde_b");
				V_HastaB = rsE3.getTimestamp("hasta_b");
			}
			pstmtE3.close ();rsE3.close(); 
			pstmtE3 = null; rsE3 = null;			
			commitEx();
			
			// SUBTOTAL RESULTADO OPERACIONAL
			// DETERMINACION y escritura de VALORES
			V_GTotalA = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(SALDO_A) FROM t_bl_er_salida WHERE T_BL_ER_PARAMETROS_ID="+para.get_ID());
			if(V_GTotalA == null)
				V_GTotalA = Env.ZERO;
			V_GTotalB = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(SALDO_B) FROM t_bl_er_salida WHERE T_BL_ER_PARAMETROS_ID="+para.get_ID());
			if(V_GTotalB == null)
				V_GTotalB = Env.ZERO;
			V_GTotalA = V_GTotalA.negate();
			V_GTotalB = V_GTotalB.negate();
			
			X_T_BL_ER_SALIDA salidaTO = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
			salidaTO.setT_BL_ER_PARAMETROS_ID(para.get_ID());
			salidaTO.setAD_Org_ID(para.getAD_Org_ID());
			salidaTO.setDESDE_A(V_DesdeA);
			salidaTO.setHASTA_A(V_HastaA);
			salidaTO.setDESDE_B(V_DesdeB);
			salidaTO.setHASTA_B(V_HastaB);
			salidaTO.setNOMBRE_GTOTAL("RESULTADO OPERACIONAL");
			salidaTO.setBANDERA_GTOTAL(false);
			salidaTO.setGTOTAL_A(V_GTotalA);
			salidaTO.setGTOTAL_B(V_GTotalB);
			salidaTO.saveEx();
			V_ID_Actual = salidaTO.get_ID();
			
			//escribimos titulo NO OPERACIONAL
			//ESCRIBIMOS PRIMER TITULO
			X_T_BL_ER_SALIDA tSalidaNO = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
			tSalidaNO.setT_BL_ER_PARAMETROS_ID(para.get_ID());
			tSalidaNO.setAD_Org_ID(para.getAD_Org_ID());
			tSalidaNO.setDESDE_A(V_DesdeA);
			tSalidaNO.setHASTA_A(V_HastaA);
			tSalidaNO.setDESDE_B(V_DesdeB);
			tSalidaNO.setHASTA_B(V_HastaB);
			tSalidaNO.setNOMBRE_GTOTAL("NO OPERACIONAL");
			tSalidaNO.setBANDERA_GTOTAL(false);
			tSalidaNO.saveEx();
			
			//ingresos no operacionales
			String sqlE4 = "select pc.nombre_abuelo, pc.nombre_padre, pc.account_id, pc.value, pc.NAME, ts.saldo_a, ts.saldo_b, ts.desde_a, ts.hasta_a, ts.desde_b, ts.hasta_b" +
					" from rvbl_plandecuentas pc INNER JOIN t_bl_er_saldos ts On (pc.account_id=ts.account_id)" +
					" where id_abuelo="+E4+" AND T_BL_ER_PARAMETROS_ID="+para.get_ID()+" ORDER BY pc.sec_padre, sec_cuenta";		
	
			pstmtE4 = DB.prepareStatement (sqlE4, get_TrxName());			
			ResultSet rsE4 = pstmtE4.executeQuery ();			
			while (rsE4.next ())
			{
				X_T_BL_ER_SALIDA salidaE4 = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
				salidaE4.setT_BL_ER_PARAMETROS_ID(para.get_ID());
				salidaE4.setAD_Org_ID(para.getAD_Org_ID());
				salidaE4.setNOMBRE_ABUELO(rsE4.getString("nombre_abuelo"));
				salidaE4.setNOMBRE_PADRE(rsE4.getString("nombre_padre"));
				salidaE4.setAccount_ID(rsE4.getInt("account_id"));
				salidaE4.setCUENTA(rsE4.getString("value"));
				salidaE4.setNOMBRE_CUENTA(rsE4.getString("NAME"));
				salidaE4.setSALDO_A(rsE4.getBigDecimal("saldo_a"));
				salidaE4.setSALDO_B(rsE4.getBigDecimal("saldo_b"));
				salidaE4.setDESDE_A(rsE4.getTimestamp("desde_a"));
				salidaE4.setHASTA_A(rsE4.getTimestamp("hasta_a"));
				salidaE4.setDESDE_B(rsE4.getTimestamp("desde_b"));
				salidaE4.setHASTA_B(rsE4.getTimestamp("hasta_b"));
				salidaE4.saveEx();
			}
			pstmtE4.close ();rsE4.close(); 
			pstmtE4 = null; rsE4 = null;
			commitEx();
			
			//egresos no operacionales
			String sqlE5 = "select pc.nombre_abuelo, pc.nombre_padre, pc.account_id, pc.value, pc.NAME, ts.saldo_a, ts.saldo_b, ts.desde_a, ts.hasta_a, ts.desde_b, ts.hasta_b" +
					" from rvbl_plandecuentas pc INNER JOIN t_bl_er_saldos ts On (pc.account_id=ts.account_id)" +
					" where id_ABUELO="+E5+" AND T_BL_ER_PARAMETROS_ID="+para.get_ID()+"ORDER BY pc.sec_padre, sec_cuenta";		
		
			pstmtE5 = DB.prepareStatement (sqlE5, get_TrxName());			
			ResultSet rsE5 = pstmtE5.executeQuery ();			
			while (rsE5.next ())
			{
				X_T_BL_ER_SALIDA salidaE5 = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
				salidaE5.setT_BL_ER_PARAMETROS_ID(para.get_ID());
				salidaE5.setAD_Org_ID(para.getAD_Org_ID());
				salidaE5.setNOMBRE_ABUELO(rsE5.getString("nombre_abuelo"));
				salidaE5.setNOMBRE_PADRE(rsE5.getString("nombre_padre"));
				salidaE5.setAccount_ID(rsE5.getInt("account_id"));
				salidaE5.setCUENTA(rsE5.getString("value"));
				salidaE5.setNOMBRE_CUENTA(rsE5.getString("NAME"));
				salidaE5.setSALDO_A(rsE5.getBigDecimal("saldo_a"));
				salidaE5.setSALDO_B(rsE5.getBigDecimal("saldo_b"));
				salidaE5.setDESDE_A(rsE5.getTimestamp("desde_a"));
				salidaE5.setHASTA_A(rsE5.getTimestamp("hasta_a"));
				salidaE5.setDESDE_B(rsE5.getTimestamp("desde_b"));
				salidaE5.setHASTA_B(rsE5.getTimestamp("hasta_b"));
				salidaE5.saveEx();
			}
			pstmtE5.close ();rsE5.close(); 
			pstmtE5 = null; rsE5 = null;
			
			//correccion monetaria
			String sqlE6 = "select pc.nombre_abuelo, pc.nombre_padre, pc.account_id, pc.value, pc.NAME, ts.saldo_a, ts.saldo_b, ts.desde_a, ts.hasta_a, ts.desde_b, ts.hasta_b" +
					" from rvbl_plandecuentas pc INNER JOIN t_bl_er_saldos ts On (pc.account_id=ts.account_id)" +
					" where id_ABUELO="+E6+" AND T_BL_ER_PARAMETROS_ID="+para.get_ID()+" ORDER BY pc.sec_padre, sec_cuenta";		
		
			pstmtE6 = DB.prepareStatement (sqlE6, get_TrxName());			
			ResultSet rsE6 = pstmtE6.executeQuery ();			
			while (rsE6.next ())
			{
				X_T_BL_ER_SALIDA salidaE6 = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
				salidaE6.setT_BL_ER_PARAMETROS_ID(para.get_ID());
				salidaE6.setAD_Org_ID(para.getAD_Org_ID());
				salidaE6.setNOMBRE_ABUELO(rsE6.getString("nombre_abuelo"));
				salidaE6.setNOMBRE_PADRE(rsE6.getString("nombre_padre"));
				salidaE6.setAccount_ID(rsE6.getInt("account_id"));
				salidaE6.setCUENTA(rsE6.getString("value"));
				salidaE6.setNOMBRE_CUENTA(rsE6.getString("NAME"));
				salidaE6.setSALDO_A(rsE6.getBigDecimal("saldo_a"));
				salidaE6.setSALDO_B(rsE6.getBigDecimal("saldo_b"));
				salidaE6.setDESDE_A(rsE6.getTimestamp("desde_a"));
				salidaE6.setHASTA_A(rsE6.getTimestamp("hasta_a"));
				salidaE6.setDESDE_B(rsE6.getTimestamp("desde_b"));
				salidaE6.setHASTA_B(rsE6.getTimestamp("hasta_b"));
				salidaE6.saveEx();
				//se setean variables globales
				V_DesdeA = rsE6.getTimestamp("desde_a");
				V_HastaA = rsE6.getTimestamp("hasta_a");
				V_DesdeB = rsE6.getTimestamp("desde_b");
				V_HastaB = rsE6.getTimestamp("hasta_b");
			}
			pstmtE6.close ();rsE6.close(); 
			pstmtE6 = null; rsE6 = null;			
			commitEx();
			
			//SUBTOTAL RESULTADO NO OPERACIONAL
			//DETERMINACION y escritura de VALORES
			V_GTotalA = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(SALDO_A) FROM t_bl_er_salida WHERE t_bl_er_salida_id > "+V_ID_Actual+" AND T_BL_ER_PARAMETROS_ID="+para.get_ID());
			if(V_GTotalA == null)
				V_GTotalA = Env.ZERO;
			V_GTotalB = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(SALDO_B) FROM t_bl_er_salida WHERE t_bl_er_salida_id > "+V_ID_Actual+" AND T_BL_ER_PARAMETROS_ID="+para.get_ID());
			if(V_GTotalB == null)
				V_GTotalB = Env.ZERO;
			V_GTotalA = V_GTotalA.negate();
			V_GTotalB = V_GTotalB.negate();
			
			X_T_BL_ER_SALIDA salidaTNO = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
			salidaTNO.setT_BL_ER_PARAMETROS_ID(para.get_ID());
			salidaTNO.setAD_Org_ID(para.getAD_Org_ID());
			salidaTNO.setDESDE_A(V_DesdeA);
			salidaTNO.setHASTA_A(V_HastaA);
			salidaTNO.setDESDE_B(V_DesdeB);
			salidaTNO.setHASTA_B(V_HastaB);
			salidaTNO.setNOMBRE_GTOTAL("RESULTADO NO OPERACIONAL");
			salidaTNO.setBANDERA_GTOTAL(false);
			salidaTNO.setGTOTAL_A(V_GTotalA);
			salidaTNO.setGTOTAL_B(V_GTotalB);
			salidaTNO.saveEx();
			V_ID_Actual = salidaTO.get_ID();
			
			//SUBTOTAL RESULTADO antes de impuesto
			//DETERMINACION y escritura de VALORES
			V_GTotalA = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(SALDO_A) FROM t_bl_er_salida WHERE T_BL_ER_PARAMETROS_ID="+para.get_ID());
			V_GTotalB = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(SALDO_B) FROM t_bl_er_salida WHERE T_BL_ER_PARAMETROS_ID="+para.get_ID());
		
			X_T_BL_ER_SALIDA salidaAI = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
			salidaAI.setT_BL_ER_PARAMETROS_ID(para.get_ID());
			salidaAI.setAD_Org_ID(para.getAD_Org_ID());
			salidaAI.setDESDE_A(V_DesdeA);
			salidaAI.setHASTA_A(V_HastaA);
			salidaAI.setDESDE_B(V_DesdeB);
			salidaAI.setHASTA_B(V_HastaB);
			salidaAI.setNOMBRE_GTOTAL("RESULTADO ANTES DE IMPUESTO");
			salidaAI.setBANDERA_GTOTAL(false);
			salidaAI.setGTOTAL_A(V_GTotalA);
			salidaAI.setGTOTAL_B(V_GTotalB);
			salidaAI.saveEx();
			V_ID_Actual = salidaTO.get_ID();
			
			//impuesto Renta
			String sqlE7 = "select pc.nombre_abuelo, pc.nombre_padre, pc.account_id, pc.value, pc.NAME, ts.saldo_a, ts.saldo_b, ts.desde_a, ts.hasta_a, ts.desde_b, ts.hasta_b" +
					" from rvbl_plandecuentas pc INNER JOIN t_bl_er_saldos ts On (pc.account_id=ts.account_id)" +
					" where id_abuelo="+E7+" AND T_BL_ER_PARAMETROS_ID="+para.get_ID()+" ORDER BY pc.sec_padre, sec_cuenta";		
		
			pstmtE7 = DB.prepareStatement (sqlE7, get_TrxName());			
			ResultSet rsE7 = pstmtE7.executeQuery ();			
			while (rsE7.next ())
			{
				X_T_BL_ER_SALIDA salidaE7 = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
				salidaE7.setT_BL_ER_PARAMETROS_ID(para.get_ID());
				salidaE7.setAD_Org_ID(para.getAD_Org_ID());
				salidaE7.setNOMBRE_ABUELO(rsE7.getString("nombre_abuelo"));
				salidaE7.setNOMBRE_PADRE(rsE7.getString("nombre_padre"));
				salidaE7.setAccount_ID(rsE7.getInt("account_id"));
				salidaE7.setCUENTA(rsE7.getString("value"));
				salidaE7.setNOMBRE_CUENTA(rsE7.getString("NAME"));
				salidaE7.setSALDO_A(rsE7.getBigDecimal("saldo_a"));
				salidaE7.setSALDO_B(rsE7.getBigDecimal("saldo_b"));
				salidaE7.setDESDE_A(rsE7.getTimestamp("desde_a"));
				salidaE7.setHASTA_A(rsE7.getTimestamp("hasta_a"));
				salidaE7.setDESDE_B(rsE7.getTimestamp("desde_b"));
				salidaE7.setHASTA_B(rsE7.getTimestamp("hasta_b"));
				salidaE7.saveEx();
				//se setean variables globales
				V_DesdeA = rsE7.getTimestamp("desde_a");
				V_HastaA = rsE7.getTimestamp("hasta_a");
				V_DesdeB = rsE7.getTimestamp("desde_b");
				V_HastaB = rsE7.getTimestamp("hasta_b");
			}
			pstmtE7.close ();rsE7.close(); 
			pstmtE7 = null; rsE7 = null;			
			commitEx();
			
			//SUBTOTAL RESULTADO neto del ejercicio
			//DETERMINACION y escritura de VALORES
			V_GTotalA = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(SALDO_A) FROM t_bl_er_salida WHERE T_BL_ER_PARAMETROS_ID="+para.get_ID());
			if(V_GTotalA == null)
				V_GTotalA = Env.ZERO;
			V_GTotalB = DB.getSQLValueBD(get_TrxName(), "SELECT SUM(SALDO_B) FROM t_bl_er_salida WHERE T_BL_ER_PARAMETROS_ID="+para.get_ID());
			if(V_GTotalB == null)
				V_GTotalB = Env.ZERO;
			V_GTotalA = V_GTotalA.negate();
			V_GTotalB = V_GTotalB.negate();
		
			X_T_BL_ER_SALIDA salidaNE = new X_T_BL_ER_SALIDA(getCtx(), 0, get_TrxName());
			salidaNE.setT_BL_ER_PARAMETROS_ID(para.get_ID());
			salidaNE.setAD_Org_ID(para.getAD_Org_ID());
			salidaNE.setDESDE_A(V_DesdeA);
			salidaNE.setHASTA_A(V_HastaA);
			salidaNE.setDESDE_B(V_DesdeB);
			salidaNE.setHASTA_B(V_HastaB);
			salidaNE.setNOMBRE_GTOTAL("RESULTADO NETO DEL EJERCICIO");
			salidaNE.setBANDERA_GTOTAL(false);
			salidaNE.setGTOTAL_A(V_GTotalA);
			salidaNE.setGTOTAL_B(V_GTotalB);
			salidaNE.saveEx();
			V_ID_Actual = salidaTO.get_ID();
			commitEx();
			
			//inversion signos			
			DB.executeUpdate("update t_bl_er_salida set saldo_a=(saldo_a*-1), saldo_b=(saldo_b*-1)", get_TrxName());
			para.setBANDERA2(true);
			para.saveEx();
		/*}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}*/		
		return "OK";
	}	//	doIt
}	//	Replenish

