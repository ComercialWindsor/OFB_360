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
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.adempiere.exceptions.AdempiereException;
import org.blumos.model.BlumosUtilities;
import org.compiere.model.X_T_BL_COMISIONES;
import org.compiere.model.X_T_BL_FLASH_PARAMETROS;
import org.compiere.model.X_T_BL_FLASH_VENTAS_AGRUPADO;
import org.compiere.model.X_T_BL_FLASH_VENTAS_AGRUPADO2;
import org.compiere.model.X_T_BL_FLASH_VENTAS_COMPARA;
import org.compiere.model.X_T_BL_FLASH_VENTAS_DETALLE;
import org.compiere.model.X_T_BL_FLASH_VENTAS_DETALLE2;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *  @author Italo Niñoles
 */
public class ProcessFlashVentas extends SvrProcess
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
		X_T_BL_FLASH_PARAMETROS para = new X_T_BL_FLASH_PARAMETROS(getCtx(), Record_ID, get_TrxName());
		String Vend_query = "";
		String v_la_vista = "";
		
		//validaciones logicas
		if(para.getHASTA().compareTo(para.getDESDE()) < 0)
			throw new AdempiereException("ERROR: Fecha hasta no puede ser menor a fecha desde");
		if(para.getHASTA_B() != null && para.getDESDE_B() != null)
			if(para.getHASTA_B().compareTo(para.getDESDE_B()) < 0)
				throw new AdempiereException("ERROR: Fecha hasta B no puede ser menor a fecha desde B");
		if(para.isCOMPARATIVO())
			if(para.getDESDE_B() == null || para.getHASTA_B()== null)
				return "ERROR: Debe ingresar fechas hasta B y desde B";
		
		/*BigDecimal v_totalCosto = new BigDecimal("0.0");
		BigDecimal v_totalMargen = new BigDecimal("0.0");
		BigDecimal v_totalmExtranjera = new BigDecimal("0.0");
		BigDecimal v_totalDolar = new BigDecimal("0.0");
		BigDecimal v_Cantidad = new BigDecimal("0.0");
		int v_moneda = 0;
		int v_unidad = 0;*/
		
		if(para.getCreatedBy() == 1001395)
		{
			para.setSOLUTEC("Y");
			para.save(get_TrxName());
		}
		
		DB.executeUpdate("DELETE FROM T_Bl_Flash_Ventas_Detalle WHERE T_Bl_Flash_Parametros_Id="+para.get_ID(), get_TrxName());
		DB.executeUpdate("DELETE FROM T_Bl_Flash_Ventas_Detalle2 WHERE T_Bl_Flash_Parametros_Id="+para.get_ID(), get_TrxName());
		
		if(para.isCOMPARATIVO())
		{
			para.setAGRUPA_MES(true);
			para.save(get_TrxName());
		}
		if(para.isUSA_FAMILIA_REPRESENTADA())
			v_la_vista = " rvbl_flash_ventas_002 ";
		else
			v_la_vista = " rvbl_flash_ventas_001 ";
		if(para.isVER_COMPRAS())
			v_la_vista = " rvbl_flash_ventas_003 ";
		
		//condición vendedor
		String t_representada = " ";
		if(para.getREPRESENTADA_ID() > 0)
		{
			if(para.isUSA_FAMILIA_REPRESENTADA())
				t_representada = " REPRESENTADA_PADRE_ID= "+para.getREPRESENTADA_ID()+" AND ";
			else
			{
				t_representada = " REPRESENTADA_ID= "+para.getREPRESENTADA_ID()+" AND ";
				para.setAGRUPA_REPRESENTADA(true);
			}
		}			
		//condición cliente
		String t_cliente = " ";
		if(para.getC_BPartner_ID() > 0)
		{
			t_cliente = " C_BPARTNER_ID= "+para.getC_BPartner_ID()+" AND ";
			para.setAGRUPA_CLIENTE(true);
		}
		//condición producto
		String t_producto = " ";
		if(para.getM_Product_ID() > 0)
		{
			t_producto = " M_PRODUCT_ID= "+para.getM_Product_ID()+" AND ";
			para.setAGRUPA_PRODUCTO(true);
		}
		para.save(get_TrxName());		
		//condición vendedores
		String t_vendedor = " ";
		String tpre_vendedor;
		if(para.getSalesRep_ID() > 0)
		{
			if(para.isUSACARTERA())
			{
				t_vendedor = " VENDEDORCARTERA_ID="+para.getSalesRep_ID()+ " AND ";
				para.setAGRUPA_VENDEDOR(true);
			}
			else
			{
				t_vendedor = " SALESREP_ID="+para.getSalesRep_ID()+ " AND ";
				para.setAGRUPA_VENDEDOR(true);
			}
		}
		else
		{
			Vend_query = "SELECT C_Orgassignment.ad_user_id FROM C_Orgassignment " +
					" INNER JOIN AD_User on (C_Orgassignment.AD_User_ID=AD_User.AD_User_ID) " +
					" INNER JOIN C_Bpartner on (Ad_user.C_BPartner_ID=C_Bpartner.C_BPartner_ID)" +
					" WHERE C_Bpartner.IsSalesRep= 'Y' AND C_Orgassignment.ad_client_id= "+para.getAD_Client_ID()+
					" AND C_Orgassignment.ad_Org_id IN (select C_Orgassignment.AD_Org_ID FROM C_Orgassignment" +
					" INNER JOIN AD_User ON (C_Orgassignment.AD_User_ID=AD_User.AD_User_ID) " +
					" WHERE AD_User.AD_User_ID="+para.getCreatedBy()+
					" UNION SELECT AD_org_Id FROM Ad_Org WHERE Ad_Client_Id="+para.getAD_Client_ID()+
					" AND 0 in (SELECT C_Orgassignment.AD_Org_ID FROM C_Orgassignment " +
					" INNER JOIN AD_User on (C_Orgassignment.AD_User_ID=AD_User.AD_User_ID)" +
					" WHERE AD_User.AD_User_ID="+para.getCreatedBy()+")) GROUP BY C_Orgassignment.AD_User_ID" +
					" UNION SELECT 1000840 as ad_user_id from dual";
			t_vendedor = " ( ";
			if(para.isUSACARTERA())
				tpre_vendedor = " VENDEDORCARTERA_ID = ";
			else
				tpre_vendedor = " SALESREP_ID = ";
			//ciclo de vendedores
			PreparedStatement pstmtVQ = null;
			pstmtVQ = DB.prepareStatement (Vend_query, get_TrxName());
			ResultSet rsVQ = pstmtVQ.executeQuery();			
			while (rsVQ.next())
			{
				t_vendedor = t_vendedor+tpre_vendedor+rsVQ.getInt("ad_user_id");
				t_vendedor = t_vendedor + " OR ";
			}
			t_vendedor = t_vendedor + " 1=2) AND ";
			//se liberan recursos
			pstmtVQ.close();rsVQ.close();
			pstmtVQ = null; rsVQ = null;
		}		
		para.save(get_TrxName());
		

		//condición fechas
		String t_desde = "";
		if(para.getDESDE() != null)
			t_desde = " DATEINVOICED >= ? AND ";
		
		String t_hasta = "";
		if(para.getHASTA() != null)
			t_hasta = " DATEINVOICED <= ? AND ";
		//condición organizacion
		String t_ad_org_id = "";
		if(para.getAD_Org_ID() > 0)
		{
			t_ad_org_id = "AD_ORG_ID= "+para.getAD_Org_ID()+" AND ";
			para.setAGRUPA_AREA(true);
		}
		//condición solutec
		String t_solutec = "";
		if(para.getSOLUTEC() != null && para.getSOLUTEC().compareTo("X") != 0)
		{
			t_solutec = " SOLUTEC = '"+para.getSOLUTEC()+"' AND ";
			para.setAGRUPA_SOLUTEC(true);
		}
		//condición codigo blumos
		String t_codigoBlumos = "";
		if(para.getCODIGO_BLUMOS() != null && para.getCODIGO_BLUMOS().compareTo("X") != 0)
		{
			t_codigoBlumos = " CODIGO_BLUMOS= '"+para.getCODIGO_BLUMOS()+"' AND ";
			para.setAGRUPA_CODIGO_BLUMOS(true);
		}
		//condicion proyecto 
		/*String t_proyecto_id = "";
		if(para.getC_ProjectOFB_ID() > 0)
		{
			t_proyecto_id = " C_PROJECTOFB_ID="+para.getC_ProjectOFB_ID()+" AND ";
			para.setAGRUPA_PROYECTO(true);
		}*/
		/*-- ANALIZA SI INCLUIR O NO INDENT */
		String t_indent ="";
		if(para.isINCLUIR_INDENT())
		{
			if(para.isSOLO_INDENT())
				t_indent = " ESINDENT=1 AND ";
		}
		else
			t_indent = " ESINDENT<>1 AND ";
		
		// activar bandera de grupo si es el caso
		if(para.isAGRUPA_VENDEDOR() || para.isAGRUPA_CLIENTE() || para.isAGRUPA_REPRESENTADA() ||
				para.isAGRUPA_PRODUCTO() || para.isAGRUPA_AREA() || para.isAGRUPA_SOLUTEC() || 
				para.isAGRUPA_ANO() || para.isAGRUPA_MES() || para.isAGRUPA_CODIGO_BLUMOS() || 
				para.isAGRUPA_PROYECTO())
		{
			para.setACTIVA_GRUPO(true);
		}
		else
			para.setACTIVA_GRUPO(false);
		para.save(get_TrxName());
		
		//ciclo principal de detalle 1 y detalle 2
		ResultSet rsDet = null;
		PreparedStatement pstmtDet = null;
		ResultSet rsDetC = null;
		PreparedStatement pstmtDetC = null;
		//try 
		//{	
			String queryDet = "SELECT AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,SYSDATE,100,SYSDATE,100,c_invoice_id, c_order_id,c_invoiceline_id," +
				"M_PRODUCT_ID,C_BPARTNER_ID,REPRESENTADA_ID,SALESREP_ID,C_CURRENCY_ID,QTYINVOICED,C_UOM_ID,PRICEENTERED,LINENETAMT," +
				"FOREIGNPRICE,VENTA_MON_EXTRANJERA, DATEINVOICED, VENTA_EN_DOLAR, C_DocType_ID, SOLUTEC, COSTOUNITARIO," +
				"VENDEDORCARTERA_ID,CLASSIFICATION,ULTIMA_FECHA_DESPACHO,PLANTA_ID,CODIGO_BLUMOS,QTYONHAND,representada_padre_id,c_projectofb_id,ESINDENT FROM "+v_la_vista+
				" WHERE "+t_representada+t_cliente+t_producto+t_vendedor+t_desde+t_hasta+t_ad_org_id+t_solutec+t_codigoBlumos+t_indent+" AD_Client_ID = "+para.getAD_Client_ID();
		
			log.config("sql det: "+queryDet);
			pstmtDet = DB.prepareStatement (queryDet, get_TrxName());
			if(para.getDESDE() != null)
				pstmtDet.setTimestamp(1, para.getDESDE());
			if(para.getHASTA() != null)
				pstmtDet.setTimestamp(2, para.getHASTA());
			rsDet = pstmtDet.executeQuery();
			BigDecimal valorUF = Env.ZERO;
			BigDecimal valorDolar = Env.ZERO;
			BigDecimal v_UF = Env.ZERO;
			while (rsDet.next())
			{
				valorUF = BlumosUtilities.DameUFBlumos(rsDet.getTimestamp("DATEINVOICED"), getCtx(), get_TrxName());
				//ininoles validacion en caso de UF nula
				if(valorUF == null)
					throw new AdempiereException("ERROR: NO existe UF para la fecha "+rsDet.getTimestamp("DATEINVOICED"));
				
				if(rsDet.getBigDecimal("LINENETAMT") == null || rsDet.getBigDecimal("VENTA_EN_DOLAR") == null)
					valorDolar = Env.ZERO;
				else
				{
					//ininoles nueva validacion en caso que campo VENTA_EN_DOLAR sea cero no mande error aritmetico.
					if(rsDet.getBigDecimal("VENTA_EN_DOLAR") != null && rsDet.getBigDecimal("VENTA_EN_DOLAR").compareTo(Env.ZERO)==0)
						valorDolar = Env.ZERO;
					else						
						valorDolar = rsDet.getBigDecimal("LINENETAMT").divide(rsDet.getBigDecimal("VENTA_EN_DOLAR"),10,RoundingMode.HALF_EVEN);
				}
				if(rsDet.getBigDecimal("LINENETAMT") == null || valorUF == null)
					v_UF = Env.ZERO;
				else
					v_UF = rsDet.getBigDecimal("LINENETAMT").divide(valorUF,12,RoundingMode.HALF_EVEN);
				int v2_ad_Org_ID = rsDet.getInt("AD_ORG_ID");
				int Org_ID = rsDet.getInt("AD_ORG_ID");
				if(rsDet.getInt("AD_CLIENT_ID") == 1000000)
					Org_ID = 1000000;
				
				//se crea registro nuevo
				X_T_BL_FLASH_VENTAS_DETALLE det = new X_T_BL_FLASH_VENTAS_DETALLE(getCtx(), 0, get_TrxName());
				//se verifica ID en trace
				if(rsDet.getInt("c_invoice_id") == 1148455)
				{
					log.config("factura con problemas:"+rsDet.getInt("c_invoice_id"));					
				}
				det.setAD_Org_ID(Org_ID);
				det.setIsActive(true);
				det.setC_Invoice_ID(rsDet.getInt("c_invoice_id"));
				det.setC_Order_ID(rsDet.getInt("c_order_id"));
				det.setC_InvoiceLine_ID(rsDet.getInt("c_invoiceline_id"));
				det.setM_Product_ID(rsDet.getInt("M_PRODUCT_ID"));
				det.setC_BPartner_ID(rsDet.getInt("C_BPARTNER_ID"));
				det.setREPRESENTADA_ID(rsDet.getInt("REPRESENTADA_ID"));
				det.setSalesRep_ID(rsDet.getInt("SALESREP_ID"));
				det.setC_Currency_ID(rsDet.getInt("C_CURRENCY_ID"));
				if(rsDet.getBigDecimal("QTYINVOICED") != null)
					det.setQtyInvoiced(rsDet.getBigDecimal("QTYINVOICED"));
				det.setC_UOM_ID(rsDet.getInt("C_UOM_ID"));
				det.setPriceEntered(rsDet.getBigDecimal("PRICEENTERED"));
				if(rsDet.getBigDecimal("LINENETAMT") != null)
					det.setLineNetAmt(rsDet.getBigDecimal("LINENETAMT"));
				if(rsDet.getBigDecimal("FOREIGNPRICE") != null)
					det.setForeignPrice(rsDet.getBigDecimal("FOREIGNPRICE"));
				if(rsDet.getBigDecimal("VENTA_MON_EXTRANJERA") != null)
					det.setVENTA_MON_EXTRANJERA(rsDet.getBigDecimal("VENTA_MON_EXTRANJERA"));
				det.setDateInvoiced(rsDet.getTimestamp("DATEINVOICED"));
				if(rsDet.getBigDecimal("VENTA_EN_DOLAR") != null)
					det.setVENTA_EN_DOLAR(rsDet.getBigDecimal("VENTA_EN_DOLAR"));
				det.setT_BL_FLASH_PARAMETROS_ID(para.get_ID());
				det.setC_DocType_ID(rsDet.getInt("C_DocType_ID"));
				det.setVENTA_UF(v_UF);
				det.setSOLUTEC(rsDet.getString("SOLUTEC").compareTo("Y")==0 ? true:false);
				if(rsDet.getBigDecimal("COSTOUNITARIO") != null)
					det.setCOSTOUNITARIO(rsDet.getBigDecimal("COSTOUNITARIO"));
				det.setVENDEDORCARTERA_ID(rsDet.getInt("VENDEDORCARTERA_ID"));
				if(rsDet.getBigDecimal("QTYINVOICED") != null && rsDet.getBigDecimal("COSTOUNITARIO") != null)
					det.setCOSTOLINEA(rsDet.getBigDecimal("QTYINVOICED").multiply(rsDet.getBigDecimal("COSTOUNITARIO")));
				if(rsDet.getBigDecimal("QTYINVOICED") != null && rsDet.getBigDecimal("QTYINVOICED") != null && rsDet.getBigDecimal("COSTOUNITARIO")!= null)
					det.setMARGENLINEA(rsDet.getBigDecimal("LINENETAMT").subtract(rsDet.getBigDecimal("QTYINVOICED").multiply(rsDet.getBigDecimal("COSTOUNITARIO"))));
				det.setORG_DE_VENTA(v2_ad_Org_ID);
				det.setCODIGO_BLUMOS(rsDet.getString("CODIGO_BLUMOS"));			
				det.setDIA(rsDet.getTimestamp("DATEINVOICED").getDate() < 10 ? "0"+rsDet.getTimestamp("DATEINVOICED").getDate() : Integer.toString(rsDet.getTimestamp("DATEINVOICED").getDate()));
				det.setMES(rsDet.getTimestamp("DATEINVOICED").getMonth()+1 < 10 ? "0"+(rsDet.getTimestamp("DATEINVOICED").getMonth()+1) : Integer.toString(rsDet.getTimestamp("DATEINVOICED").getMonth()+1));
				det.setANO(Integer.toString(rsDet.getTimestamp("DATEINVOICED").getYear()+1900));
				det.setLINEA_PRODUCTO(rsDet.getString("CLASSIFICATION"));
				det.setULTIMA_FECHA_DESPACHO(rsDet.getTimestamp("ULTIMA_FECHA_DESPACHO"));
				det.setPLANTA_ID(rsDet.getInt("PLANTA_ID"));
				BigDecimal amtComodin = Env.ZERO; 
				if(rsDet.getBigDecimal("LINENETAMT") != null && rsDet.getBigDecimal("QTYINVOICED") != null && rsDet.getBigDecimal("COSTOUNITARIO") != null)
					amtComodin = rsDet.getBigDecimal("LINENETAMT").subtract(rsDet.getBigDecimal("QTYINVOICED").multiply(rsDet.getBigDecimal("COSTOUNITARIO")));
				if(rsDet.getBigDecimal("LINENETAMT") != null && rsDet.getBigDecimal("LINENETAMT").compareTo(Env.ZERO) != 0)
					det.setMARGENPORC(amtComodin.multiply(Env.ONEHUNDRED).divide(rsDet.getBigDecimal("LINENETAMT"), 2, RoundingMode.HALF_EVEN));
				else
					det.setMARGENPORC(Env.ZERO);
				//det.setMARGENPORC(amtComodin.divide(rsDet.getBigDecimal("LINENETAMT")).multiply(Env.ONEHUNDRED).setScale(2, RoundingMode.HALF_EVEN));
				det.setQtyOnHand(rsDet.getBigDecimal("QTYONHAND"));
				det.setMARGEN_UF(amtComodin.divide(valorUF, 10,RoundingMode.HALF_EVEN));
				if(valorDolar != null && valorDolar.compareTo(Env.ZERO) != 0)
					det.setMARGEN_DOLAR(amtComodin.divide(valorDolar, 10,RoundingMode.HALF_EVEN));
				det.setREPRESENTADA_PADRE_ID(rsDet.getInt("representada_padre_id"));
				det.setC_ProjectOFB_ID(rsDet.getInt("c_projectofb_id"));
				det.setESINDENT(rsDet.getInt("ESINDENT"));
				det.save(get_TrxName());
			}		
			if(para.isCOMPARATIVO())
			{
				/****************************************************/
				// TRATAMIENTO DE PARAMETROS PARA COMPARATIVO
				String t_desdeB = "";
				if(para.getDESDE_B() != null)
					t_desdeB = " DATEINVOICED >= ? AND ";
				
				String t_hastaB = "";
				if(para.getHASTA_B() != null)
					t_hastaB = " DATEINVOICED <= ? AND ";
				
				/***************************************************/				
				//ciclo 2 comparativo
				String queryDetC = "SELECT AD_CLIENT_ID,AD_ORG_ID,ISACTIVE,SYSDATE,100,SYSDATE,100,c_invoice_id, c_order_id,c_invoiceline_id," +
				"M_PRODUCT_ID,C_BPARTNER_ID,REPRESENTADA_ID,SALESREP_ID,C_CURRENCY_ID,QTYINVOICED,C_UOM_ID,PRICEENTERED,LINENETAMT," +
				"FOREIGNPRICE,VENTA_MON_EXTRANJERA, DATEINVOICED, VENTA_EN_DOLAR, C_DocType_ID, SOLUTEC, COSTOUNITARIO," +
				"VENDEDORCARTERA_ID,CLASSIFICATION,ULTIMA_FECHA_DESPACHO,PLANTA_ID,CODIGO_BLUMOS,QTYONHAND,representada_padre_id,c_projectofb_id,ESINDENT FROM "+v_la_vista+
				" WHERE "+t_representada+t_cliente+t_producto+t_vendedor+t_desdeB+t_hastaB+t_ad_org_id+t_solutec+t_codigoBlumos+t_indent+" AD_Client_ID = "+para.getAD_Client_ID();
				
				log.config("sql det2: "+queryDetC);
				pstmtDetC = DB.prepareStatement (queryDetC, get_TrxName());
				if(para.getDESDE_B() != null)
					pstmtDetC.setTimestamp(1, para.getDESDE_B());
				if(para.getHASTA_B() != null)
					pstmtDetC.setTimestamp(2, para.getHASTA_B());
				rsDetC = pstmtDetC.executeQuery();
				BigDecimal valorUFC = Env.ZERO;
				BigDecimal valorDolarC = Env.ZERO;
				BigDecimal v_UFC = Env.ZERO;
				while (rsDetC.next())
				{
					valorUFC = BlumosUtilities.DameUFBlumos(rsDetC.getTimestamp("DATEINVOICED"), getCtx(), get_TrxName());
					if(rsDetC.getBigDecimal("LINENETAMT") != null && rsDetC.getBigDecimal("VENTA_EN_DOLAR") != null)
						valorDolarC = rsDetC.getBigDecimal("LINENETAMT").divide(rsDetC.getBigDecimal("VENTA_EN_DOLAR"),10, RoundingMode.HALF_EVEN);
					if(rsDetC.getBigDecimal("LINENETAMT") != null && valorUFC != null && valorUFC.compareTo(Env.ZERO) != 0)
						v_UFC = rsDetC.getBigDecimal("LINENETAMT").divide(valorUFC,12,RoundingMode.HALF_EVEN);
					//int v2_ad_OrgC_ID = rsDetC.getInt("AD_ORG_ID");
					int OrgC_ID = rsDetC.getInt("AD_ORG_ID");
					if(rsDetC.getInt("AD_CLIENT_ID") == 1000000)
						OrgC_ID = 1000000;
					
					//se crea registro nuevo
					X_T_BL_FLASH_VENTAS_DETALLE2 detC = new X_T_BL_FLASH_VENTAS_DETALLE2(getCtx(), 0, get_TrxName());
					detC.setAD_Org_ID(OrgC_ID);
					detC.setIsActive(true);
					detC.setC_Invoice_ID(rsDetC.getInt("c_invoice_id"));
					detC.setC_Order_ID(rsDetC.getInt("c_order_id"));
					detC.setC_InvoiceLine_ID(rsDetC.getInt("c_invoiceline_id"));
					detC.setM_Product_ID(rsDetC.getInt("M_PRODUCT_ID"));
					detC.setC_BPartner_ID(rsDetC.getInt("C_BPARTNER_ID"));
					detC.setREPRESENTADA_ID(rsDetC.getInt("REPRESENTADA_ID"));
					detC.setSalesRep_ID(rsDetC.getInt("SALESREP_ID"));
					detC.setC_Currency_ID(rsDetC.getInt("C_CURRENCY_ID"));
					if(rsDetC.getBigDecimal("QTYINVOICED") != null)
						detC.setQtyInvoiced(rsDetC.getBigDecimal("QTYINVOICED"));
					detC.setC_UOM_ID(rsDetC.getInt("C_UOM_ID"));
					if(rsDetC.getBigDecimal("PRICEENTERED") != null)
						detC.setPriceEntered(rsDetC.getBigDecimal("PRICEENTERED"));
					if(rsDetC.getBigDecimal("LINENETAMT") != null)
						detC.setLineNetAmt(rsDetC.getBigDecimal("LINENETAMT"));
					if(rsDetC.getBigDecimal("FOREIGNPRICE") != null)
						detC.setForeignPrice(rsDetC.getBigDecimal("FOREIGNPRICE"));
					if(rsDetC.getBigDecimal("VENTA_MON_EXTRANJERA") != null)
						detC.setVENTA_MON_EXTRANJERA(rsDetC.getBigDecimal("VENTA_MON_EXTRANJERA"));
					detC.setDateInvoiced(rsDetC.getTimestamp("DATEINVOICED"));
					if(rsDetC.getBigDecimal("VENTA_EN_DOLAR") != null)
						detC.setVENTA_EN_DOLAR(rsDetC.getBigDecimal("VENTA_EN_DOLAR"));
					detC.setT_BL_FLASH_PARAMETROS_ID(para.get_ID());
					detC.setC_DocType_ID(rsDetC.getInt("C_DocType_ID"));
					detC.setVENTA_UF(v_UFC);
					detC.setSOLUTEC(rsDetC.getString("SOLUTEC").compareTo("Y")==0 ? true:false);
					if(rsDetC.getBigDecimal("COSTOUNITARIO") != null)
						detC.setCOSTOUNITARIO(rsDetC.getBigDecimal("COSTOUNITARIO"));
					detC.setVENDEDORCARTERA_ID(rsDetC.getInt("VENDEDORCARTERA_ID"));
					if(rsDetC.getBigDecimal("QTYINVOICED") != null && rsDetC.getBigDecimal("COSTOUNITARIO") != null)
						detC.setCOSTOLINEA(rsDetC.getBigDecimal("QTYINVOICED").multiply(rsDetC.getBigDecimal("COSTOUNITARIO")));
					if(rsDetC.getBigDecimal("QTYINVOICED") != null && rsDetC.getBigDecimal("COSTOUNITARIO") != null)
						detC.setMARGENLINEA(rsDetC.getBigDecimal("LINENETAMT").subtract(rsDetC.getBigDecimal("QTYINVOICED").multiply(rsDetC.getBigDecimal("COSTOUNITARIO"))));
					detC.setCODIGO_BLUMOS(rsDetC.getString("CODIGO_BLUMOS"));			
					detC.setDIA(rsDetC.getTimestamp("DATEINVOICED").getDate() < 10 ? "0"+rsDetC.getTimestamp("DATEINVOICED").getDate() : Integer.toString(rsDetC.getTimestamp("DATEINVOICED").getDate()));
					detC.setMES(rsDetC.getTimestamp("DATEINVOICED").getMonth()+1 < 10 ? "0"+(rsDetC.getTimestamp("DATEINVOICED").getMonth()+1) : Integer.toString(rsDetC.getTimestamp("DATEINVOICED").getMonth()+1));
					detC.setANO(Integer.toString(rsDetC.getTimestamp("DATEINVOICED").getYear()+1900));
					detC.setLINEA_PRODUCTO(rsDetC.getString("CLASSIFICATION"));
					detC.setULTIMA_FECHA_DESPACHO(rsDetC.getTimestamp("ULTIMA_FECHA_DESPACHO"));
					detC.setPLANTA_ID(rsDetC.getInt("PLANTA_ID"));
					BigDecimal amtComodinC = Env.ZERO;
					if(rsDetC.getBigDecimal("LINENETAMT") != null && rsDetC.getBigDecimal("QTYINVOICED") != null && rsDetC.getBigDecimal("COSTOUNITARIO") != null)						
						amtComodinC = rsDetC.getBigDecimal("LINENETAMT").subtract(rsDetC.getBigDecimal("QTYINVOICED").multiply(rsDetC.getBigDecimal("COSTOUNITARIO")));
					if(rsDetC.getBigDecimal("LINENETAMT") != null)
						detC.setMARGENPORC(amtComodinC.multiply(Env.ONEHUNDRED).divide(rsDetC.getBigDecimal("LINENETAMT"), 2, RoundingMode.HALF_EVEN));
					//det.setMARGENPORC(amtComodin.divide(rsDet.getBigDecimal("LINENETAMT")).multiply(Env.ONEHUNDRED).setScale(2, RoundingMode.HALF_EVEN));
					detC.setQtyOnHand(rsDetC.getBigDecimal("QTYONHAND"));
					detC.setMARGEN_UF(amtComodinC.divide(valorUFC, 10,RoundingMode.HALF_EVEN));
					if(valorDolarC != null && valorDolarC.compareTo(Env.ZERO) != 0)
						detC.setMARGEN_DOLAR(amtComodinC.divide(valorDolarC, 10,RoundingMode.HALF_EVEN));
					detC.setREPRESENTADA_PADRE_ID(rsDetC.getInt("representada_padre_id"));
					detC.setC_ProjectOFB_ID(rsDetC.getInt("c_projectofb_id"));
					//detC.setESINDENT(rsDetC.getInt("ESINDENT"));
					detC.saveEx(get_TrxName());
				}
			}
			/*pstmtDet.close();rsDet.close();	
			pstmtDet = null;rsDet = null;		
			pstmtDetC.close();rsDetC.close();
			pstmtDetC = null;rsDetC = null;*/
		/*}catch (Exception e)		{
			log.config("ERROR: "+e.toString());
		}
		finally{
			pstmtDet.close();rsDet.close();	
			pstmtDet = null;rsDet = null;	
			if(pstmtDetC != null && rsDetC != null)
			{
				pstmtDetC.close();rsDetC.close();
				pstmtDetC = null;rsDetC = null;
			}
		}*/
		commitEx();
		//parte 2 se setean montos de cabecera
		para.setTOTAL_PESOS(DB.getSQLValueBD(get_TrxName(), "SELECT SUM(linenetamt) " +
				" FROM t_bl_flash_ventas_detalle WHERE t_bl_flash_parametros_id ="+para.get_ID()));
		para.setTOTAL_DOLARES(DB.getSQLValueBD(get_TrxName(), "SELECT SUM(VENTA_EN_DOLAR) " +
				" FROM t_bl_flash_ventas_detalle WHERE t_bl_flash_parametros_id ="+para.get_ID()));
		para.setTOTAL_CANTIDAD(DB.getSQLValueBD(get_TrxName(),"SELECT SUM(QTYINVOICED) "+
				" FROM t_bl_flash_ventas_detalle WHERE t_bl_flash_parametros_id ="+para.get_ID()));
		para.setTOTAL_UF(DB.getSQLValueBD(get_TrxName(), "SELECT SUM(VENTA_UF) "+
				" FROM t_bl_flash_ventas_detalle WHERE t_bl_flash_parametros_id ="+para.get_ID()));
		//ESCRIBE COSTO
		para.setTOTAL_COSTO(DB.getSQLValueBD(get_TrxName(), "SELECT SUM(COSTOLINEA) "+
				" FROM t_bl_flash_ventas_detalle WHERE t_bl_flash_parametros_id ="+para.get_ID()));
		para.setTOTAL_MARGEN(DB.getSQLValueBD(get_TrxName(), "SELECT SUM(MARGENLINEA) "+
				" FROM t_bl_flash_ventas_detalle WHERE t_bl_flash_parametros_id ="+para.get_ID()));
		para.setMARGENPORC(DB.getSQLValueBD(get_TrxName(), "SELECT ROUND(((SUM(MARGENLINEA)/SUM(LINENETAMT))*100),2) "+
				" FROM t_bl_flash_ventas_detalle WHERE t_bl_flash_parametros_id ="+para.get_ID()));
		
		//para.set_CustomColumn("Processed", true);
		para.setIsActive(false);
		para.save(get_TrxName());
		
		//parte 3 agrupamiento
		if(para.isACTIVA_GRUPO())
		{
			String sqlG_1 = "SELECT AD_CLIENT_ID,'Y',sysdate,100,sysdate,100,sum(qtyinvoiced) as qtyinvoiced, sum(linenetamt) as linenetamt, "+
              " sum(venta_en_dolar) as venta_en_dolar, sum(venta_uf) as venta_uf, sum(costolinea) as costolinea , sum(margenlinea) as margenlinea,"+ 
              "case "+
              "  when sum(linenetamt)=0 then 0 "+
              "  else ROUND(((SUM(MARGENLINEA)/SUM(LINENETAMT))*100),2) "+
              "  end AS MARGENPORC, "+
              "SUM(MARGEN_UF) AS MARGEN_UF, SUM(MARGEN_DOLAR) AS MARGEN_DOLAR ";
			
			String sqlG_2 = " from t_bl_flash_ventas_detalle where t_bl_flash_parametros_id = "+para.get_ID()+" group by AD_CLIENT_ID,sysdate,sysdate ";
			String sqlG_3 = " from t_bl_flash_ventas_detalle2 where t_bl_flash_parametros_id = "+para.get_ID()+" group by AD_CLIENT_ID,sysdate,sysdate ";
			
			//agrupa cliente
			String g_Cliente = "";
			Boolean g_AgrupaCliente = false;
			if(para.isAGRUPA_CLIENTE())
			{
				g_Cliente = ", c_bpartner_id";
				g_AgrupaCliente = true;
			}
			else
			{
				g_Cliente = " ";
				g_AgrupaCliente = false;
			}
			//agrupa representada
			String g_Representada = "";
			Boolean g_AgrupaRepresentada = false;
			if(para.isAGRUPA_REPRESENTADA())
			{
				if(para.isUSA_FAMILIA_REPRESENTADA())
					g_Representada = ", representada_PADRE_ID as representada_id";
				else
					g_Representada = ", representada_id";
				g_AgrupaRepresentada = true;
			}
			else
			{
				g_Representada = " ";
				g_AgrupaRepresentada = false;
			}
			//agrupa vendedor
			String g_Vendedor = "";
			String g_VendedorAgru = "";
			Boolean g_AgrupaVendedor = false;
			if(para.isAGRUPA_VENDEDOR())
			{
				if(para.isUSACARTERA())
				{
					g_Vendedor = ", VENDEDORCARTERA_ID as salesrep_id";
					g_VendedorAgru = ",VENDEDORCARTERA_ID";
				}
				else
				{
					g_Vendedor = ", salesrep_id";
					g_VendedorAgru = ",salesrep_id";
				}
				g_AgrupaVendedor = true;
			}
			else
			{
				g_Vendedor = " ";
				g_AgrupaVendedor = false;
			}
			//agrupa producto
			String g_Producto = "";
			Boolean g_AgrupaProducto = false;
			if(para.isAGRUPA_PRODUCTO())
			{
				g_Producto = ", m_product_id";
				g_AgrupaProducto = true;
			}
			else
			{
				g_Producto = " ";
				g_AgrupaProducto = false;
			}
			//agrupa area
			String g_Area = "";
			Boolean g_AgrupaArea = false;
			if(para.isAGRUPA_AREA())
			{
				g_Area = ", org_de_venta";
				g_AgrupaArea = true;
			}
			else
			{
				g_Area = " ";
				g_AgrupaArea = false;
			}
			//agrupa solutec
			String g_Solutec = "";
			Boolean g_AgrupaSolutec = false;
			if(para.isAGRUPA_SOLUTEC())
			{
				g_Solutec = ", solutec";
				g_AgrupaSolutec = true;
			}
			else
			{
				g_Solutec = " ";
				g_AgrupaSolutec = false;
			}
			//agrupa año
			String g_Ano = "";
			Boolean g_AgrupaAno = false;
			if(para.isAGRUPA_ANO())
			{
				g_Ano = ", ANO";
				g_AgrupaAno = true;
			}
			else
			{
				g_Ano = " ";
				g_AgrupaAno = false;
			}
			//agrupa mes
			String g_Mes = "";
			Boolean g_AgrupaMes = false;
			if(para.isAGRUPA_MES())
			{
				g_Mes = ", MES";
				g_AgrupaMes = true;
				g_Ano = ", ANO";
				g_AgrupaAno = true;
			}
			else
			{
				g_Mes = " ";
				g_AgrupaMes = false;
			}
			//agrupa codigo blumos
			String g_CodigoBlumos = "";
			Boolean g_AgrupaCBlumos = false;
			if(para.isAGRUPA_CODIGO_BLUMOS())
			{
				g_CodigoBlumos = ", codigo_blumos";
				g_AgrupaCBlumos = true;
			}
			else
			{
				//g_CodigoBlumos = ",0";
				g_CodigoBlumos = " ";
				g_AgrupaCBlumos = false;
			} 
			//agrupa codigo proyecto
			String g_Proyecto = "";
			Boolean g_AgrupaProyecto = false;
			if(para.isAGRUPA_PROYECTO())
			{
				g_Proyecto = ", c_projectofb_id";
				g_AgrupaProyecto = true;
			}
			else
			{
				g_Proyecto = " ";
				g_AgrupaProyecto = false;
			}
			String sqlGroup1 = sqlG_1+g_Area+g_Cliente+g_Representada+g_Vendedor+g_Producto+g_Solutec+g_Ano+g_Mes+g_CodigoBlumos+g_Proyecto+sqlG_2+g_Area+g_Cliente+g_Representada+g_VendedorAgru+g_Producto+g_Solutec+g_Ano+g_Mes+g_CodigoBlumos+g_Proyecto;
				
			log.config("sql detG1: "+sqlGroup1);
			ResultSet rsG1 = null;
			PreparedStatement pstmtG1 = null;
			//try 
			//{
				pstmtG1 = DB.prepareStatement (sqlGroup1, get_TrxName());			
				rsG1 = pstmtG1.executeQuery();
				while (rsG1.next())
				{
					X_T_BL_FLASH_VENTAS_AGRUPADO agr = new X_T_BL_FLASH_VENTAS_AGRUPADO(getCtx(), 0, get_TrxName());
					agr.setAD_Org_ID(0);
					if(para.isAGRUPA_AREA())
						agr.setORG_DE_VENTA(rsG1.getInt("org_de_venta"));
					agr.setIsActive(true);
					agr.setQtyInvoiced(rsG1.getBigDecimal("qtyinvoiced"));
					agr.setLineNetAmt(rsG1.getBigDecimal("linenetamt"));
					agr.setVENTA_EN_DOLAR(rsG1.getBigDecimal("venta_en_dolar"));
					agr.setVENTA_UF(rsG1.getBigDecimal("venta_uf"));
					agr.setCOSTOLINEA(rsG1.getBigDecimal("costolinea"));
					agr.setMARGENLINEA(rsG1.getBigDecimal("margenlinea"));
					agr.setMARGENPORC(rsG1.getBigDecimal("MARGENPORC"));
					if(para.isAGRUPA_PRODUCTO())
						agr.setM_Product_ID(rsG1.getInt("m_product_id"));
					if(para.isAGRUPA_CLIENTE())
						agr.setC_BPartner_ID(rsG1.getInt("c_bpartner_id"));
					if(para.isAGRUPA_REPRESENTADA())
						agr.setREPRESENTADA_ID(rsG1.getInt("representada_id"));
					if(para.isAGRUPA_VENDEDOR())
						agr.setSalesRep_ID(rsG1.getInt("salesrep_id"));
					agr.setT_BL_FLASH_PARAMETROS_ID(para.get_ID());
					agr.setAGRUPA_VENDEDOR(g_AgrupaVendedor);
					agr.setAGRUPA_CLIENTE(g_AgrupaCliente);
					agr.setAGRUPA_REPRESENTADA(g_AgrupaRepresentada);
					agr.setAGRUPA_PRODUCTO(g_AgrupaProducto);
					agr.setAGRUPA_AREA(g_AgrupaArea);
					agr.setAGRUPA_SOLUTEC(g_AgrupaSolutec);
					if(para.isAGRUPA_SOLUTEC())
						agr.setSOLUTEC(rsG1.getString("SOLUTEC").compareTo("Y")==0 ? true:false);
					agr.setAGRUPA_ANO(g_AgrupaAno);
					agr.setAGRUPA_MES(g_AgrupaMes);
					if(para.isAGRUPA_ANO() || para.isAGRUPA_MES())
						agr.setANO(rsG1.getString("ANO"));
					if(para.isAGRUPA_MES())
						agr.setMES(rsG1.getString("MES"));
					agr.setAGRUPA_CODIGO_BLUMOS(g_AgrupaCBlumos);
					if(para.isAGRUPA_CODIGO_BLUMOS())
						agr.setCODIGO_BLUMOS(rsG1.getString("codigo_blumos"));
					agr.setMARGEN_UF(rsG1.getBigDecimal("MARGEN_UF").setScale(2, RoundingMode.HALF_EVEN));
					agr.setMARGEN_DOLAR(rsG1.getBigDecimal("MARGEN_DOLAR").setScale(2, RoundingMode.HALF_EVEN));
					agr.setAGRUPA_PROYECTO(g_AgrupaProyecto);
					if(para.isAGRUPA_PROYECTO())
						agr.setC_ProjectOFB_ID(rsG1.getInt("c_projectofb_id"));
					agr.saveEx(get_TrxName());
				}
			//}
			/*catch (Exception e)		{
				log.config("ERROR: "+e.toString());
			}
			finally{
				if(pstmtG1 != null)
				{
					pstmtG1.close();
					pstmtG1 = null;
				}
				if(rsG1 != null)
				{
					rsG1.close();	
					rsG1 = null;
				}
			}*/
			if(para.isCOMPARATIVO())//agrupamiento detalle 2				
			{
				String sqlGroup2 = sqlG_1+g_Area+g_Cliente+g_Representada+g_Vendedor+g_Producto+g_Solutec+g_Ano+g_Mes+g_CodigoBlumos+g_Proyecto+sqlG_3+g_Area+g_Cliente+g_Representada+g_VendedorAgru+g_Producto+g_Solutec+g_Ano+g_Mes+g_CodigoBlumos+g_Proyecto;
				log.config("sql detG2: "+sqlGroup2);
				ResultSet rsG2 = null;
				PreparedStatement pstmtG2 = null;
				//try 
				//{
					pstmtG2 = DB.prepareStatement (sqlGroup2, get_TrxName());			
					rsG2 = pstmtG2.executeQuery();
					while (rsG2.next())
					{
						X_T_BL_FLASH_VENTAS_AGRUPADO2 agr2 = new X_T_BL_FLASH_VENTAS_AGRUPADO2(getCtx(), 0, get_TrxName());
						agr2.setAD_Org_ID(0);
						agr2.setIsActive(true);
						agr2.setQtyInvoiced(rsG2.getBigDecimal("qtyinvoiced"));
						agr2.setLineNetAmt(rsG2.getBigDecimal("linenetamt"));
						agr2.setVENTA_EN_DOLAR(rsG2.getBigDecimal("venta_en_dolar"));
						agr2.setVENTA_UF(rsG2.getBigDecimal("venta_uf"));
						agr2.setCOSTOLINEA(rsG2.getBigDecimal("costolinea"));
						agr2.setMARGENLINEA(rsG2.getBigDecimal("margenlinea"));
						agr2.setMARGENPORC(rsG2.getBigDecimal("MARGENPORC"));
						if(para.isAGRUPA_PRODUCTO())
							agr2.setM_Product_ID(rsG2.getInt("m_product_id"));
						if(para.isAGRUPA_CLIENTE())
							agr2.setC_BPartner_ID(rsG2.getInt("c_bpartner_id"));
						if(para.isAGRUPA_REPRESENTADA())
							agr2.setREPRESENTADA_ID(rsG2.getInt("representada_id"));
						if(para.isAGRUPA_VENDEDOR())
							agr2.setSalesRep_ID(rsG2.getInt("salesrep_id"));
						agr2.setT_BL_FLASH_PARAMETROS_ID(para.get_ID());
						agr2.setAGRUPA_VENDEDOR(g_AgrupaVendedor);
						agr2.setAGRUPA_CLIENTE(g_AgrupaCliente);
						agr2.setAGRUPA_REPRESENTADA(g_AgrupaRepresentada);
						agr2.setAGRUPA_PRODUCTO(g_AgrupaProducto);
						agr2.setAGRUPA_AREA(g_AgrupaArea);
						agr2.setAGRUPA_SOLUTEC(g_AgrupaSolutec);
						if(para.isAGRUPA_SOLUTEC())
							agr2.setSOLUTEC(rsG2.getString("SOLUTEC").compareTo("Y")==0 ? true:false);
						agr2.setAGRUPA_ANO(g_AgrupaAno);
						agr2.setAGRUPA_MES(g_AgrupaMes);
						if(para.isAGRUPA_ANO() || para.isAGRUPA_MES())
							agr2.setANO(rsG2.getString("ANO"));
						if(para.isAGRUPA_MES())
							agr2.setMES(rsG2.getString("MES"));
						agr2.setAGRUPA_CODIGO_BLUMOS(g_AgrupaCBlumos);
						if(para.isAGRUPA_CODIGO_BLUMOS())
							agr2.setCODIGO_BLUMOS(rsG2.getString("codigo_blumos"));
						agr2.setMARGEN_UF(rsG2.getBigDecimal("MARGEN_UF").setScale(2, RoundingMode.HALF_EVEN));
						agr2.setMARGEN_DOLAR(rsG2.getBigDecimal("MARGEN_DOLAR").setScale(2,RoundingMode.HALF_EVEN));
						agr2.setAGRUPA_PROYECTO(g_AgrupaProyecto);
						if(para.isAGRUPA_PROYECTO())
							agr2.setC_ProjectOFB_ID(rsG2.getInt("c_projectofb_id"));
						agr2.saveEx(get_TrxName());
					}
				//}
				/*catch (Exception e)		{
					log.config("ERROR: "+e.toString());
				}
				finally{
					if(pstmtG2 != null)
					{
						pstmtG2.close();
						pstmtG2 = null;
					}
					if(rsG2 != null)
					{
						rsG2.close();	
						rsG2 = null;
					}
				}*/
			}
		}
		commitEx();
		//parte 4 comparativo
		if(para.isCOMPARATIVO())//agrupamiento detalle 2				
		{
			//strdesde
			String strDesde = ""; 
			if(para.getDESDE().getDate()<10)
				strDesde = "0"+para.getDESDE().getDate()+"-";
			else	
				strDesde = para.getDESDE().getDate()+"-";
			if(para.getDESDE().getMonth()+1 < 10)
				strDesde = strDesde+"0"+(para.getDESDE().getMonth()+1);
			else
				strDesde = strDesde+(para.getDESDE().getMonth()+1);
			strDesde = strDesde+"-"+(para.getDESDE().getYear()+1900);
			//strdesdeB
			String strDesdeB = ""; 
			if(para.getDESDE_B().getDate()<10)
				strDesdeB = "0"+para.getDESDE_B().getDate()+"-";
			else	
				strDesdeB = para.getDESDE_B().getDate()+"-";
			if(para.getDESDE_B().getMonth()+1 < 10)
				strDesdeB = strDesdeB+"0"+(para.getDESDE_B().getMonth()+1);
			else
				strDesdeB = strDesdeB+(para.getDESDE_B().getMonth()+1);
			strDesdeB = strDesdeB+"-"+(para.getDESDE_B().getYear()+1900);
						
			String sqlQueryCom = "SELECT t1.m_product_id,t1.c_bpartner_id,t1.representada_id,t1.c_projectofb_id,t1.salesrep_id,t1.solutec,t1.codigo_blumos,t1.org_de_venta," +
					" t1.mes, t1.ano, t1.qtyinvoiced, t1.linenetamt, t1.costolinea, t1.margenlinea,t1.margenporc,t1.venta_uf,t1.venta_en_dolar," +
					" t1.margen_uf, t1.margen_dolar," +
					" t2.mes AS mes2, t2.ano as ano2, t2.qtyinvoiced as qtyinvoiced2, t2.linenetamt as linenetamt2, t2.costolinea as costolinea2, t2.margenlinea as margenlinea2,t2.margenporc as margenporc2,t2.venta_uf as venta_uf2,t2.venta_en_dolar as venta_en_dolar2," +
					" t2.margen_uf as margen_uf2, t2.margen_dolar as margen_dolar2," +
					" t1.qtyinvoiced-t2.qtyinvoiced as DIFcantidad, t1.linenetamt-t2.linenetamt as DIFneto, t1.costolinea-t2.costolinea as DIFcosto," +
					" t1.margenlinea-t2.margenlinea as DIFmargen,t1.venta_uf-t2.venta_uf as DIFventaUF, t1.venta_en_dolar-t2.venta_en_dolar as DIFventaDolar," +
					" t1.margen_uf-t2.margen_uf as dif_margen_uf, t1.margen_dolar-t2.margen_dolar as dif_margen_dolar," +
					" case" +
					" when (t1.venta_en_dolar-t2.venta_en_dolar)=0 then 0" +
					" when t1.venta_en_dolar=0 then 0" +
					" else (t1.venta_en_dolar-t2.venta_en_dolar)/t1.venta_en_dolar" +
					" end as DIFPORCVTADOLAR " +
					" FROM (" +
					" SELECT" +
					" concat_ws('',dameidcomparativo(?::timestamp without time zone,?::timestamp without time zone,to_number(t1.mes), to_number(t1.ano)) , t1.t_bl_flash_parametros_id  ," +
					" t1.m_product_id , t1.c_bpartner_id , t1.representada_id , t1.salesrep_id , t1.solutec ," +
					" t1.codigo_blumos , t1.org_de_venta , t1.c_projectofb_id) AS ID_UNICO,t1.t_bl_flash_parametros_id," +
					" t1.mes, t1.ano, t1.qtyinvoiced, t1.linenetamt, t1.costolinea, t1.margenlinea,t1.margenporc,t1.venta_uf,t1.venta_en_dolar," +
					" t1.m_product_id,t1.c_bpartner_id,t1.representada_id,t1.c_projectofb_id,t1.salesrep_id,t1.solutec,t1.codigo_blumos,t1.org_de_venta," +
					" t1.margen_uf,t1.margen_dolar" +
					" FROM T_BL_FLASH_VENTAS_AGRUPADO t1" +
					" ) t1" +
					" inner JOIN" +
					" (" +
					" SELECT" +
					" concat_ws('',dameidcomparativo(?::timestamp without time zone,?::timestamp without time zone,to_number(t1.mes),to_number(t1.ano)) , t1.t_bl_flash_parametros_id  ," +
					" t1.m_product_id , t1.c_bpartner_id , t1.representada_id , t1.salesrep_id , t1.solutec ," +
					" t1.codigo_blumos , t1.org_de_venta , t1.c_projectofb_id) AS ID_UNICO,t1.t_bl_flash_parametros_id," +
					" t1.mes, t1.ano, t1.qtyinvoiced, t1.linenetamt, t1.costolinea, t1.margenlinea,t1.margenporc,t1.venta_uf,t1.venta_en_dolar," +
					" t1.m_product_id,t1.c_bpartner_id,t1.representada_id,t1.c_projectofb_id,t1.salesrep_id,t1.solutec,t1.codigo_blumos,t1.org_de_venta," +
					" t1.margen_uf,t1.margen_dolar" +
					" FROM T_BL_FLASH_VENTAS_AGRUPADO2 t1" +
					" ) t2 ON (t1.id_unico=t2.id_unico)" +
					" where t1.t_bl_flash_parametros_id="+para.get_ID()+
					" UNION ALL" +
					" SELECT t1.m_product_id,t1.c_bpartner_id,t1.representada_id,t1.c_projectofb_id,t1.salesrep_id,t1.solutec,t1.codigo_blumos,t1.org_de_venta," +
					" t1.mes, t1.ano, t1.qtyinvoiced, t1.linenetamt, t1.costolinea, t1.margenlinea,t1.margenporc,t1.venta_uf,t1.venta_en_dolar," +
					" t1.margen_uf, t1.margen_dolar," +
					" to_char((add_months(?,damemesflash('"+strDesde+"',t1.mes,t1.ano))),'MM') as mes2, to_char((add_months(?,damemesflash('"+strDesde+"',t1.mes,t1.ano))),'YYYY') as ano2, 0 as qtyinvoiced2, 0 as linenetamt2, 0 as costolinea2, 0 as margenlinea2,0 as margenporc2, 0 as venta_uf2, 0 as venta_en_dolar2," +
					" 0 as margen_uf2, 0 as margen_dolar2," +
					" t1.qtyinvoiced as DIFcantidad, t1.linenetamt as DIFneto," +
					" t1.costolinea as DIFcosto, t1.margenlinea as DIFmargen," +
					" t1.venta_uf as DIFventaUF, t1.venta_en_dolar as DIFventaDolar," +
					" t1.margen_uf as dif_margen_uf, t1.margen_dolar as dif_margen_dolar," +
					" case" +
					" when (t1.venta_en_dolar-coalesce(t2.venta_en_dolar,0))=0 then 0" +
					" when t1.venta_en_dolar=0 then 0" +
					" else (t1.venta_en_dolar-coalesce(t2.venta_en_dolar,0))/t1.venta_en_dolar" +
					" end as DIFPORCVTADOLAR" +
					" FROM" +
					" (SELECT" +
					" concat_ws('',dameidcomparativo(?::timestamp without time zone,?::timestamp without time zone,to_number(t1.mes), to_number(t1.ano)) , t1.t_bl_flash_parametros_id  ," +
					" t1.m_product_id , t1.c_bpartner_id , t1.representada_id , t1.salesrep_id , t1.solutec ," +
					" t1.codigo_blumos , t1.org_de_venta , t1.c_projectofb_id) AS ID_UNICO,t1.t_bl_flash_parametros_id," +
					" t1.mes, t1.ano, t1.qtyinvoiced, t1.linenetamt, t1.costolinea, t1.margenlinea,t1.margenporc,t1.venta_uf,t1.venta_en_dolar," +
					" t1.m_product_id,t1.c_bpartner_id,t1.representada_id,t1.c_projectofb_id,t1.salesrep_id,t1.solutec,t1.codigo_blumos,t1.org_de_venta," +
					" t1.margen_uf,t1.margen_dolar" +
					" FROM T_BL_FLASH_VENTAS_AGRUPADO t1) t1" +
					" LEFT join (SELECT" +
					" concat_ws('',dameidcomparativo(?::timestamp without time zone,?::timestamp without time zone,to_number(t1.mes), to_number(t1.ano)) , t1.t_bl_flash_parametros_id  ," +
					" t1.m_product_id , t1.c_bpartner_id , t1.representada_id , t1.salesrep_id , t1.solutec ," +
					" t1.codigo_blumos , t1.org_de_venta , t1.c_projectofb_id) AS ID_UNICO,t1.t_bl_flash_parametros_id," +
					" t1.mes, t1.ano, t1.qtyinvoiced, t1.linenetamt, t1.costolinea, t1.margenlinea,t1.margenporc,t1.venta_uf,t1.venta_en_dolar," +
					" t1.m_product_id,t1.c_bpartner_id,t1.representada_id,t1.c_projectofb_id,t1.salesrep_id,t1.solutec,t1.codigo_blumos,t1.org_de_venta," +
					" t1.margen_uf,t1.margen_dolar" +
					" FROM T_BL_FLASH_VENTAS_AGRUPADO2 t1) t2 ON (t1.id_unico=t2.id_unico)" +
					" where t1.t_bl_flash_parametros_id="+para.get_ID()+
					" AND t2.id_unico is null" +
					" UNION ALL" +
					" SELECT t2.m_product_id, t2.c_bpartner_id, t2.representada_id, t2.c_projectofb_id, t2.salesrep_id, t2.solutec, t2.codigo_blumos," +
					" t2.org_de_venta, to_char((add_months(?,damemesflash('"+strDesdeB+"',t2.mes,t2.ano))),'MM') as mes, to_char((add_months(?,damemesflash('"+strDesdeB+"',t2.mes,t2.ano))),'YYYY') as ano, 0 as qtyinvoiced, 0 as linenetamt, 0 as costolinea, 0 as margenlinea, 0 as margenporc," +
					" 0 as venta_uf, 0 as venta_en_dolar, 0 as margen_uf, 0 as margen_dolar," +
					" t2.mes as mes2, t2.ano as ano2, t2.qtyinvoiced as qtyinvoiced2, t2.linenetamt as linenetamt2, t2.costolinea as costolinea2, t2.margenlinea as margenlinea2," +
					" t2.margenporc as margenporc2, t2.venta_uf as venta_uf2,t2.venta_en_dolar as venta_en_dolar2," +
					" t2.margen_uf as margen_uf2, t2.margen_dolar as margen_dolar2," +
					" 0-t2.qtyinvoiced as DIFcantidad, 0-t2.linenetamt as DIFneto, 0-t2.costolinea as DIFcosto, 0-t2.margenlinea as DIFmargen," +
					" 0-t2.venta_uf as DIFventaUF, 0-t2.venta_en_dolar as DIFventaDolar," +
					" 0-t2.margen_uf as DIF_Margen_UF, 0-t2.margen_dolar as dif_margen_dolar," +
					" 0 as DIFPORCVTADOLAR" +
					" FROM (SELECT" +
					" concat_ws('',dameidcomparativo(?::timestamp without time zone,?::timestamp without time zone,to_number(t1.mes), to_number(t1.ano)) , t1.t_bl_flash_parametros_id  ," +
					" t1.m_product_id , t1.c_bpartner_id , t1.representada_id , t1.salesrep_id , t1.solutec ," +
					" t1.codigo_blumos , t1.org_de_venta , t1.c_projectofb_id) AS ID_UNICO,t1.t_bl_flash_parametros_id," +
					" t1.mes, t1.ano, t1.qtyinvoiced, t1.linenetamt, t1.costolinea, t1.margenlinea,t1.margenporc,t1.venta_uf,t1.venta_en_dolar," +
					" t1.m_product_id,t1.c_bpartner_id,t1.representada_id,t1.c_projectofb_id,t1.salesrep_id,t1.solutec,t1.codigo_blumos,t1.org_de_venta," +
					" t1.margen_uf,t1.margen_dolar" +
					" FROM T_BL_FLASH_VENTAS_AGRUPADO t1) t1 " +
					" right JOIN (SELECT" +
					" concat_ws('',dameidcomparativo(?::timestamp without time zone,?::timestamp without time zone,to_number(t1.mes), to_number(t1.ano)) , t1.t_bl_flash_parametros_id  ," +
					" t1.m_product_id , t1.c_bpartner_id , t1.representada_id , t1.salesrep_id , t1.solutec ," +
					" t1.codigo_blumos , t1.org_de_venta , t1.c_projectofb_id) AS ID_UNICO,t1.t_bl_flash_parametros_id," +
					" t1.mes, t1.ano, t1.qtyinvoiced, t1.linenetamt, t1.costolinea, t1.margenlinea,t1.margenporc,t1.venta_uf,t1.venta_en_dolar," +
					" t1.m_product_id,t1.c_bpartner_id,t1.representada_id,t1.c_projectofb_id,t1.salesrep_id,t1.solutec,t1.codigo_blumos,t1.org_de_venta," +
					" t1.margen_uf,t1.margen_dolar" +
					" FROM T_BL_FLASH_VENTAS_AGRUPADO2 t1) t2 ON (t1.id_unico=t2.id_unico) " +
					" where t2.t_bl_flash_parametros_id="+para.get_ID()+
					" AND t1.id_unico is null";
			log.config("sql sqlQueryCom: "+sqlQueryCom);
			ResultSet rsCom = null;
			PreparedStatement pstmtCom = null;					
			//try 
			//{
				pstmtCom = DB.prepareStatement (sqlQueryCom, get_TrxName());
				pstmtCom.setTimestamp(1, para.getDESDE());
				pstmtCom.setTimestamp(2, para.getHASTA());
				pstmtCom.setTimestamp(3, para.getDESDE_B());
				pstmtCom.setTimestamp(4, para.getHASTA_B());
				
				pstmtCom.setTimestamp(5, para.getDESDE_B());
				//pstmtCom.setTimestamp(6, para.getDESDE());
				pstmtCom.setTimestamp(6, para.getDESDE_B());
				//pstmtCom.setTimestamp(8, para.getDESDE());
				
				pstmtCom.setTimestamp(7, para.getDESDE());
				pstmtCom.setTimestamp(8, para.getHASTA());
				pstmtCom.setTimestamp(9, para.getDESDE_B());
				pstmtCom.setTimestamp(10, para.getHASTA_B());
				
				pstmtCom.setTimestamp(11, para.getDESDE());
				//pstmtCom.setTimestamp(14, para.getDESDE_B());
				pstmtCom.setTimestamp(12, para.getDESDE());
				//pstmtCom.setTimestamp(16, para.getDESDE_B());
				
				pstmtCom.setTimestamp(13, para.getDESDE());
				pstmtCom.setTimestamp(14, para.getHASTA());
				pstmtCom.setTimestamp(15, para.getDESDE_B());
				pstmtCom.setTimestamp(16, para.getHASTA_B());	
				rsCom = pstmtCom.executeQuery();
				while (rsCom.next())
				{
					X_T_BL_FLASH_VENTAS_COMPARA com = new X_T_BL_FLASH_VENTAS_COMPARA(getCtx(), 0, get_TrxName());
					com.setT_BL_FLASH_PARAMETROS_ID(para.get_ID());
					com.setM_Product_ID(rsCom.getInt("M_Product_ID"));
					com.setC_BPartner_ID(rsCom.getInt("C_BPartner_ID"));
					com.setREPRESENTADA_ID(rsCom.getInt("Representada_ID"));
					com.setC_ProjectOFB_ID(rsCom.getInt("C_ProjectOFB_ID"));
					com.setSalesRep_ID(rsCom.getInt("SalesRep_ID"));
					//com.setSOLUTEC(rsCom.getBoolean("Solutec"));
					if(rsCom.getString("SOLUTEC") != null)
						com.setSOLUTEC(rsCom.getString("SOLUTEC").compareTo("Y")==0 ? true:false);
					com.setCODIGO_BLUMOS(rsCom.getString("Codigo_Blumos"));
					com.setORG_DE_VENTA(rsCom.getInt("org_de_venta"));
					com.setMES(rsCom.getString("Mes"));
					com.setANO(rsCom.getString("Ano"));
					com.setQtyInvoiced(rsCom.getBigDecimal("QtyInvoiced"));
					com.setLineNetAmt(rsCom.getBigDecimal("linenetamt"));
					com.setCOSTOLINEA(rsCom.getBigDecimal("CostoLinea"));
					com.setMARGENLINEA(rsCom.getBigDecimal("MargenLinea"));
					com.setMARGENPORC(rsCom.getBigDecimal("MargenPorc"));
					com.setVENTA_UF(rsCom.getBigDecimal("Venta_UF"));
					com.setVENTA_EN_DOLAR(rsCom.getBigDecimal("Venta_en_Dolar"));
					com.setMES2(rsCom.getString("Mes2"));
					com.setANO2(rsCom.getString("Ano2"));
					com.setQTYINVOICED2(rsCom.getBigDecimal("QtyInvoiced2"));
					com.setLINENETAMT2(rsCom.getBigDecimal("LineNetAmt2"));
					com.setCOSTOLINEA2(rsCom.getBigDecimal("CostoLinea2"));
					com.setMARGENLINEA2(rsCom.getBigDecimal("MargenLinea2"));
					com.setMARGENPORC2(rsCom.getBigDecimal("MargenPorc2"));
					com.setVENTA_UF2(rsCom.getBigDecimal("Venta_UF2"));
					com.setVENTA_EN_DOLAR2(rsCom.getBigDecimal("Venta_en_Dolar2"));
					com.setDIFERENCIA("Diferencia");
					com.setDIFCANTIDAD(rsCom.getBigDecimal("DIFcantidad"));
					com.setDIFNETO(rsCom.getBigDecimal("DIFneto"));
					com.setDIFCOSTO(rsCom.getBigDecimal("DIFcosto"));
					com.setDIFMARGEN(rsCom.getBigDecimal("DIFmargen"));
					com.setDIFVENTAUF(rsCom.getBigDecimal("DIFventaUF"));
					com.setDIFVENTADOLAR(rsCom.getBigDecimal("DIFventaDolar"));
					com.setDIFPORCVTADOLAR(rsCom.getBigDecimal("DIFPORCVTADOLAR"));
					com.setMARGEN_UF(rsCom.getBigDecimal("Margen_UF"));
					com.setMARGEN_DOLAR(rsCom.getBigDecimal("Margen_Dolar"));
					com.setMARGEN_UF2(rsCom.getBigDecimal("Margen_UF2"));
					com.setMARGEN_DOLAR2(rsCom.getBigDecimal("Margen_Dolar2"));
					com.setDIF_MARGEN_UF(rsCom.getBigDecimal("dif_margen_uf"));
					com.setDIF_MARGEN_DOLAR(rsCom.getBigDecimal("dif_margen_dolar"));
					com.saveEx(get_TrxName());
				}
			/*}
			catch (Exception e)		{
				log.config("ERROR: "+e.toString());
			}
			finally{
				if(pstmtCom != null)
				{
					pstmtCom.close();
					pstmtCom = null;
				}
				if(rsCom != null)
				{
					rsCom.close();	
					rsCom = null;
				}
			}*/
		}
		//ventas nuevas
		if(para.isVENTA_NUEVA())
		{
			String sqlVNueva = "SELECT T_BL_FLASH_VENTAS_DETALLE_ID, DATEINVOICED, codigo_blumos, C_BPARTNER_ID, C_invoiceline_id FROM T_BL_FLASH_VENTAS_DETALLE WHERE T_BL_FLASH_PARAMETROS_ID="+para.get_ID();
			ResultSet rsVN = null;
			PreparedStatement pstmtVn = null;					
			pstmtVn = DB.prepareStatement (sqlVNueva, get_TrxName());
			rsVN = pstmtVn.executeQuery();
			while (rsVN.next())
			{
				String isNewStr = DB.getSQLValueString(get_TrxName(), "SELECT Ci.Calcular_Nueva FROM C_Invoiceline Cil " +
						" INNER JOIN C_Invoice Ci ON (Cil.C_Invoice_Id=Ci.C_Invoice_Id)" +
						" WHERE Cil.C_Invoiceline_Id="+rsVN.getInt("C_InvoiceLine_ID"));
				String isnew = "";
				String dateStr = "";
				if(rsVN.getTimestamp("DATEINVOICED").getDate()<10)
					dateStr = "0"+rsVN.getTimestamp("DATEINVOICED").getDate()+"-";
				else	
					dateStr = rsVN.getTimestamp("DATEINVOICED").getDate()+"-";
				if(rsVN.getTimestamp("DATEINVOICED").getMonth()+1 < 10)
					dateStr = dateStr+"0"+(rsVN.getTimestamp("DATEINVOICED").getMonth()+1);
				else
					dateStr = dateStr+(rsVN.getTimestamp("DATEINVOICED").getMonth()+1);
				dateStr = dateStr+"-"+(rsVN.getTimestamp("DATEINVOICED").getYear()+1900);				
				
				if(isNewStr.compareToIgnoreCase("Y") == 0)
					isnew = DB.getSQLValueString(get_TrxName(), "SELECT COALESCE(dameventanueva('"+dateStr+"','"+rsVN.getString("codigo_blumos")+"',"+rsVN.getInt("C_BPARTNER_ID")+"),'N') FROM dual");
				else
					isnew = "N";
				DB.executeUpdate("UPDATE t_bl_flash_ventas_detalle SET ventanueva='"+isnew+"' " +
						" WHERE t_bl_flash_ventas_detalle_id="+rsVN.getInt("T_BL_FLASH_VENTAS_DETALLE_ID"), get_TrxName());					
			}
		}
		//codigo para comisiones //usa comisiones antigua
		if(para.isCALCULA_COMISIONES() && para.isVENTA_NUEVA() && para.get_ValueAsBoolean("usar_comision_antigua"))
		{
			String sqlQueryComi = "SELECT DA.ad_user_id," +
					" da.venta," +
					" round(da.vta_blm_h,0) as vta_blm_h_bl ," +
					" da.vta_blm_n," +
					" da.vta_slt_h," +
					
					" round(da.vta_slt_n,0) as vta_slt_n_bl," +
					" round(da.mgn_blm_h,0) as mgn_blm_h_bl," +
					" da.mgn_blm_n," +
					" round(da.mgn_slt_h,0) as mgn_slt_h_bl," +
					" round(mgn_slt_n,0) as mgn_slt_n_bl," +
					
					" (da.VTA_BLM_H+da.VTA_BLM_N) as VTA_BLM_T, " +
					" (da.VTA_SLT_H+da.VTA_SLT_N) as VTA_SLT_T," +
					" CASE DA.VTA_BLM_H" +
					" 	WHEN 0 THEN 0" +
					" 	ELSE round((da.mgn_blm_h*DAME_TASA_COMISION(1,DA.MGN_BLM_H/DA.VTA_BLM_H))*DAME_TASA_COMISION(10,DA.MGN_BLM_H/DA.VTA_BLM_H),0)" +
					" END as com_blm_h," +
					" CASE DA.VTA_BLM_N" +
					" WHEN 0 THEN 0" +
					" ELSE round((da.mgn_blm_n*DAME_TASA_COMISION(2,DA.MGN_BLM_n/DA.VTA_BLM_n))*DAME_TASA_COMISION(11,DA.MGN_BLM_n/DA.VTA_BLM_n),0)" +
					" END as com_blm_n," +
					" round(((da.VTA_BLM_H+da.VTA_BLM_N)*DAME_TASA_COMISION(12,VTA_BLM_H+da.VTA_BLM_N))*DAME_TASA_COMISION(13,VTA_BLM_H+da.VTA_BLM_N),0) as COM_BLM_T," +
					
					" CASE DA.VTA_SLT_H" +
					" WHEN 0 THEN 0" +
					" ELSE (da.vta_SLT_H*DAME_TASA_COMISION(3,DA.VTA_SLT_H))" +
					" END as com_SLT_H," +
					" CASE DA.VTA_SLT_N" +
					" WHEN 0 THEN 0" +
					" ELSE (da.vta_SLT_N*DAME_TASA_COMISION(4,DA.VTA_SLT_N))" +
					" END as com_SLT_N," +
					" CASE DA.VTA_BLM_H" +
					" WHEN 0 THEN 0" +
					" ELSE DAME_TASA_COMISION(1,DA.MGN_BLM_H/DA.VTA_BLM_H) " +
					" END AS TSA_BLM_H," +
					" CASE DA.VTA_BLM_N" +
					" WHEN 0 THEN 0" +
					" ELSE DAME_TASA_COMISION(2,DA.MGN_BLM_N/DA.VTA_BLM_N) " +
					" END AS TSA_BLM_N," +
					" CASE DA.VTA_SLT_H" +
					" WHEN 0 THEN 0" +
					" ELSE DAME_TASA_COMISION(3,DA.VTA_SLT_H) " +
					" END AS TSA_SLT_H," +
					
					" CASE DA.VTA_SLT_N" +
					" WHEN 0 THEN 0" +
					" ELSE DAME_TASA_COMISION(4,DA.VTA_SLT_N) " +
					" END AS TSA_SLT_N" +
					" FROM" +
					" (" +
					" SELECT MAX(D.ad_user_id) AS ad_user_id, SUM(D.VENTA) AS VENTA," +
					" SUM(D.VTA_BLM_H) AS VTA_BLM_H, SUM(D.VTA_BLM_N) AS VTA_BLM_N," +
					" SUM(D.VTA_SLT_H) AS VTA_SLT_H, SUM(D.VTA_SLT_N) AS VTA_SLT_N," +
					" SUM(D.MGN_BLM_H) AS MGN_BLM_H,SUM(D.MGN_BLM_N) AS MGN_BLM_N," +
					" SUM(D.MGN_SLT_H) AS MGN_SLT_H, SUM(D.MGN_SLT_N)AS MGN_SLT_N" +
					" FROM " +
					" (" +
					" SELECT au.ad_user_id, " +
					" lv.linenetamt as venta," +
					" case lv.solutec " +
					" when 'N' then" +
					" case lv.ventanueva " +
					" when 'N' then lv.linenetamt" +
					" else 0 " +
					" end" +
					" else 0" +
					" end as VTA_BLM_H," +
					" case lv.solutec " +
					" when 'N' then" +
					" case lv.ventanueva " +
					" when 'Y' then lv.linenetamt" +
					" else 0" +
					" end" +
					" else 0" +
					" end as VTA_BLM_N," +
					" case lv.solutec " +
					" when 'Y' then" +
					" case lv.ventanueva " +
					" when 'N' then lv.linenetamt" +
					" else 0" +
					" end" +
					" else 0" +
					" end as VTA_SLT_H," +
					" case lv.solutec " +
					" when 'Y' then" +
					" case lv.ventanueva " +
					" when 'Y' then lv.linenetamt" +
					" else 0" +
					" end" +
					" else 0" +
					" end as VTA_SLT_N," +
					" case lv.solutec " +
					" when 'N' then" +
					" case lv.ventanueva " +
					" when 'N' then lv.MARGENLINEA" +
					" else 0" +
					" end" +
					" else 0" +
					" end as MGN_BLM_H," +
					" case lv.solutec " +
					" when 'N' then" +
					" case lv.ventanueva " +
					" when 'Y' then lv.MARGENLINEA" +
					" else 0" +
					" end" +
					" else 0" +
					" end as MGN_BLM_N," +
					" case lv.solutec " +
					" when 'Y' then" +
					" case lv.ventanueva " +
					" when 'N' then lv.MARGENLINEA" +
					" else 0" +
					" end" +
					" else 0" +
					" end as MGN_SLT_H," +
					" case lv.solutec " +
					" when 'Y' then" +
					" case lv.ventanueva " +
					" when 'Y' then lv.MARGENLINEA" +
					" else 0" +
					" end" +
					" else 0" +
					" end as MGN_SLT_N" +
					" FROM t_bl_flash_Ventas_Detalle lv" +
					" inner join ad_user au on (lv.vendedorcartera_id=au.ad_user_id)" +
					" where lv.t_bl_flash_parametros_id="+para.get_ID()+
					" ) d" +
					" GROUP BY D.ad_user_id" +
					" ) da ";
			log.config("sql sqlQueryComi: "+sqlQueryComi);
			ResultSet rsComi = null;
			PreparedStatement pstmtComi = null;	
			pstmtComi = DB.prepareStatement (sqlQueryComi, get_TrxName());
			rsComi = pstmtComi.executeQuery();
			String vc_vendedor = "";
			String vc_nombre_vendedor = "";
			while (rsComi.next())
			{
				vc_vendedor = DB.getSQLValueString(get_TrxName(), "SELECT name FROM ad_user where ad_user_id="+rsComi.getInt("ad_user_id"));
				vc_nombre_vendedor = DB.getSQLValueString(get_TrxName(), "SELECT description FROM ad_user where ad_user_id="+rsComi.getInt("ad_user_id"));

				X_T_BL_COMISIONES comi = new X_T_BL_COMISIONES(getCtx(), 0, get_TrxName());
				comi.setAD_Org_ID(0);
				comi.setT_BL_FLASH_PARAMETROS_ID(para.get_ID());
				comi.setCOM_DESDE(para.getDESDE());
				comi.setCOM_HASTA(para.getHASTA());
				comi.setSalesRep_ID(rsComi.getInt("ad_user_id"));
				comi.setVENDEDOR(vc_vendedor);
				comi.setVTA_BLM_T(rsComi.getBigDecimal("VTA_SLT_T"));
				comi.setVTA_BLM_H(rsComi.getBigDecimal("vta_blm_h_bl"));
				comi.setVTA_BLM_N(rsComi.getBigDecimal("vta_blm_n"));
				comi.setVTA_SLT_T(rsComi.getBigDecimal("VTA_BLM_T"));
				comi.setVTA_SLT_H(rsComi.getBigDecimal("vta_slt_h"));
				comi.setVTA_SLT_N(rsComi.getBigDecimal("vta_slt_n_bl"));
				comi.setMGN_BLM_H(rsComi.getBigDecimal("mgn_blm_h_bl"));
				comi.setMGN_BLM_N(rsComi.getBigDecimal("mgn_blm_n"));
				comi.setMGN_SLT_H(rsComi.getBigDecimal("mgn_slt_h_bl"));
				comi.setMGN_SLT_N(rsComi.getBigDecimal("mgn_slt_n_bl"));
				comi.setTSA_BLM_H(rsComi.getBigDecimal("TSA_BLM_H"));
				comi.setTSA_BLM_N(rsComi.getBigDecimal("TSA_BLM_N"));
				comi.setTSA_SLT_H(rsComi.getBigDecimal("TSA_SLT_H"));
				comi.setTSA_SLT_N(rsComi.getBigDecimal("TSA_SLT_N"));
				comi.setCOM_BLM_T(rsComi.getBigDecimal("COM_BLM_T"));
				comi.setCOM_BLM_H(rsComi.getBigDecimal("com_blm_h"));
				comi.setCOM_BLM_N(rsComi.getBigDecimal("com_blm_n"));
				comi.setCOM_SLT_H(rsComi.getBigDecimal("com_SLT_H"));
				comi.setCOM_SLT_N(rsComi.getBigDecimal("com_SLT_N"));
				comi.setNOMBRE_VENDEDOR(vc_nombre_vendedor);
				comi.save(get_TrxName());
			}
		}
		//codigo para comisiones //usa comisiones nueva
		if(para.isCALCULA_COMISIONES() && para.isVENTA_NUEVA() && !para.get_ValueAsBoolean("usar_comision_antigua"))
		{
			String sqlQueryComiN = "SELECT DA.ad_user_id," +
					"  da.venta, " +
					" round(da.vta_blm_h,0) as vta_blm_h_bl," +
					" da.vta_blm_n," +
					" da.vta_slt_h," +
					
					" round(da.vta_slt_n,0) as vta_slt_n_bl," +
					" round(da.mgn_blm_h,0) as mgn_blm_h_bl," +
					" da.mgn_blm_n," +
					" round(da.mgn_slt_h,0) as mgn_slt_h_bl," +
					" round(mgn_slt_n,0) as mgn_slt_n_bl," +
					
					" (da.VTA_BLM_H+da.VTA_BLM_N) as VTA_BLM_T," +
					" (da.VTA_SLT_H+da.VTA_SLT_N) as VTA_SLT_T," +
					" CASE DA.VTA_BLM_H" +
					" WHEN 0 THEN 0" +
					" ELSE round((da.mgn_blm_h*DAME_TASA_COMISION(1,DA.MGN_BLM_H/DA.VTA_BLM_H))*DAME_TASA_COMISION(10,DA.MGN_BLM_H/DA.VTA_BLM_H),0)" +
					" END as com_blm_h," +
					" CASE DA.VTA_BLM_N" +
					" WHEN 0 THEN 0" +
					" ELSE round((da.mgn_blm_n*DAME_TASA_COMISION(2,DA.MGN_BLM_n/DA.VTA_BLM_n))*DAME_TASA_COMISION(11,DA.MGN_BLM_n/DA.VTA_BLM_n),0)" +
					" END as com_blm_n," +
					" round(((da.VTA_BLM_H+da.VTA_BLM_N)*DAME_TASA_COMISION(12,VTA_BLM_H+da.VTA_BLM_N))*DAME_TASA_COMISION(13,VTA_BLM_H+da.VTA_BLM_N),0) as COM_BLM_T," +
					
					" CASE DA.VTA_SLT_H" +
					" WHEN 0 THEN 0" +
					" ELSE (da.vta_SLT_H*DAME_TASA_COMISION(3,DA.VTA_SLT_H))" +
					" END as com_SLT_H," +
					" CASE DA.VTA_SLT_N" +
					" WHEN 0 THEN 0" +
					" ELSE (da.vta_SLT_N*DAME_TASA_COMISION(4,DA.VTA_SLT_N))" +
					" END as com_SLT_N," +
					" CASE DA.VTA_BLM_H" +
					" WHEN 0 THEN 0" +
					" ELSE DAME_TASA_COMISION(1,DA.MGN_BLM_H/DA.VTA_BLM_H)" +					
					" END AS TSA_BLM_H," +
					" CASE DA.VTA_BLM_N" +
					" WHEN 0 THEN 0" +
					" ELSE DAME_TASA_COMISION(2,DA.MGN_BLM_N/DA.VTA_BLM_N)" +
					" END AS TSA_BLM_N," +
					" CASE DA.VTA_SLT_H" +
					" WHEN 0 THEN 0" +
					" ELSE DAME_TASA_COMISION(3,DA.VTA_SLT_H)" +
					" END AS TSA_SLT_H," +
					
					" CASE DA.VTA_SLT_N" +
					" WHEN 0 THEN 0" +
					" ELSE DAME_TASA_COMISION(4,DA.VTA_SLT_N)" +					
					" END AS TSA_SLT_N, " +
					" da.c_bpartner_id, " +
					" da.m_product_id" +
					" FROM" +
					" (" +
					" SELECT MAX(D.ad_user_id) AS ad_user_id, MAX(d.c_bpartner_id) as c_Bpartner_ID, MAX(d.m_product_id) as m_product_id," +
					" SUM(D.VENTA) AS VENTA," +
					" SUM(D.VTA_BLM_H) AS VTA_BLM_H, SUM(D.VTA_BLM_N) AS VTA_BLM_N," +
					" SUM(D.VTA_SLT_H) AS VTA_SLT_H, SUM(D.VTA_SLT_N) AS VTA_SLT_N," +
					" SUM(D.MGN_BLM_H) AS MGN_BLM_H,SUM(D.MGN_BLM_N) AS MGN_BLM_N," +
					" SUM(D.MGN_SLT_H) AS MGN_SLT_H, SUM(D.MGN_SLT_N)AS MGN_SLT_N " +
					" FROM" +
					" (" +
					" SELECT au.ad_user_id, " +
					" lv.linenetamt as venta," +
					" case lv.solutec" +
					"  when 'N' then" +
					" case lv.ventanueva " +
					" when 'N' then lv.linenetamt" +
					" else 0 " +
					" end" +
					" else 0" +
					" end as VTA_BLM_H," +
					" case lv.solutec " +
					" when 'N' then" +
					" case lv.ventanueva " +
					" when 'Y' then lv.linenetamt" +
					" else 0" +
					" end" +
					" else 0" +
					" end as VTA_BLM_N," +
					" case lv.solutec " +
					" when 'Y' then" +
					" case lv.ventanueva " +
					" when 'N' then lv.linenetamt" +
					" else 0" +
					" end" +
					" else 0" +
					" end as VTA_SLT_H," +
					" case lv.solutec " +
					" when 'Y' then" +
					" case lv.ventanueva " +
					" when 'Y' then lv.linenetamt" +
					" else 0" +
					" end" +
					" else 0" +
					" end as VTA_SLT_N," +
					" case lv.solutec " +
					" when 'N' then" +
					" case lv.ventanueva " +
					" when 'N' then lv.MARGENLINEA" +
					" else 0" +
					" end" +
					" else 0" +
					" end as MGN_BLM_H," +
					" case lv.solutec " +
					" when 'N' then" +
					" case lv.ventanueva " +
					" when 'Y' then lv.MARGENLINEA" +
					" else 0" +
					" end" +
					" else 0" +
					" end as MGN_BLM_N," +
					" case lv.solutec " +
					" when 'Y' then" +
					" case lv.ventanueva " +
					" when 'N' then lv.MARGENLINEA" +
					" else 0" +
					" end" +
					" else 0" +
					" end as MGN_SLT_H," +
					" case lv.solutec " +
					" when 'Y' then" +
					" case lv.ventanueva " +
					" when 'Y' then lv.MARGENLINEA" +
					" else 0" +
					" end" +
					" else 0 " +
					" end as MGN_SLT_N," +
					" lv.c_bpartner_id, lv.m_product_id" +
					" FROM t_bl_flash_Ventas_Detalle lv" +
					" inner join ad_user au on (lv.vendedorcartera_id=au.ad_user_id)" +
					" where lv.t_bl_flash_parametros_id="+para.get_ID()+
					") d" +
					" GROUP BY D.ad_user_id, d.c_bpartner_id, d.m_product_id" +
					" ) da";
			log.config("sql sqlQueryComi: "+sqlQueryComiN);
			ResultSet rsComiN = null;
			PreparedStatement pstmtComiN = null;	
			pstmtComiN = DB.prepareStatement (sqlQueryComiN, get_TrxName());
			rsComiN = pstmtComiN.executeQuery();
			String vc_vendedor = "";
			String vc_nombre_vendedor = "";
			while (rsComiN.next())
			{
				vc_vendedor = DB.getSQLValueString(get_TrxName(), "SELECT name FROM ad_user where ad_user_id="+rsComiN.getInt("ad_user_id"));
				vc_nombre_vendedor = DB.getSQLValueString(get_TrxName(), "SELECT description FROM ad_user where ad_user_id="+rsComiN.getInt("ad_user_id"));

				X_T_BL_COMISIONES comi = new X_T_BL_COMISIONES(getCtx(), 0, get_TrxName());
				comi.setAD_Org_ID(0);
				comi.setT_BL_FLASH_PARAMETROS_ID(para.get_ID());
				comi.setCOM_DESDE(para.getDESDE());
				comi.setCOM_HASTA(para.getHASTA());
				comi.setSalesRep_ID(rsComiN.getInt("ad_user_id"));
				comi.setVENDEDOR(vc_vendedor);
				comi.setVTA_BLM_T(rsComiN.getBigDecimal("VTA_SLT_T"));
				comi.setVTA_BLM_H(rsComiN.getBigDecimal("vta_blm_h_bl"));
				comi.setVTA_BLM_N(rsComiN.getBigDecimal("vta_blm_n"));
				comi.setVTA_SLT_T(rsComiN.getBigDecimal("VTA_BLM_T"));
				comi.setVTA_SLT_H(rsComiN.getBigDecimal("vta_slt_h"));
				comi.setVTA_SLT_N(rsComiN.getBigDecimal("vta_slt_n_bl"));
				comi.setMGN_BLM_H(rsComiN.getBigDecimal("mgn_blm_h_bl"));
				comi.setMGN_BLM_N(rsComiN.getBigDecimal("mgn_blm_n"));
				comi.setMGN_SLT_H(rsComiN.getBigDecimal("mgn_slt_h_bl"));
				comi.setMGN_SLT_N(rsComiN.getBigDecimal("mgn_slt_n_bl"));
				comi.setTSA_BLM_H(rsComiN.getBigDecimal("TSA_BLM_H"));
				comi.setTSA_BLM_N(rsComiN.getBigDecimal("TSA_BLM_N"));
				comi.setTSA_SLT_H(rsComiN.getBigDecimal("TSA_SLT_H"));
				comi.setTSA_SLT_N(rsComiN.getBigDecimal("TSA_SLT_N"));
				comi.setCOM_BLM_T(rsComiN.getBigDecimal("COM_BLM_T"));
				comi.setCOM_BLM_H(rsComiN.getBigDecimal("com_blm_h"));
				comi.setCOM_BLM_N(rsComiN.getBigDecimal("com_blm_n"));
				comi.setCOM_SLT_H(rsComiN.getBigDecimal("com_SLT_H"));
				comi.setCOM_SLT_N(rsComiN.getBigDecimal("com_SLT_N"));
				comi.setNOMBRE_VENDEDOR(vc_nombre_vendedor);
				comi.set_CustomColumn("C_BPartner_ID", rsComiN.getInt("C_BPartner_ID"));
				comi.set_CustomColumn("M_Product_ID", rsComiN.getInt("M_Product_ID"));
				comi.save(get_TrxName());
			}      
		}		
		return "procesado";
	}	//	doIt
}	//	Replenish

