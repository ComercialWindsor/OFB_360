/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.geminis.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import org.compiere.util.*;

import java.util.Calendar;
 
/**
 *	report balances 8
 *	
 *  @author faaguilar
 *  @version $Id: LibroMayor.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class LibroCVxmlFEL extends SvrProcess
{
	/**	The Order				*/
	
	private int		p_Org_ID = 0;
	private int 	p_PInstance_ID=0;
	private Timestamp p_datefrom = null;
	private Timestamp p_dateto = null;
	private boolean p_TipoOperacion;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Org_ID"))
				p_Org_ID = para[i].getParameterAsInt();
			else if (name.equals("IsSOTrx"))
				p_TipoOperacion = "Y".equals(para[i].getParameter());
			else if (name.equals("DateTrx"))
			{
				p_datefrom = (Timestamp)para[i].getParameter();
				p_dateto = (Timestamp)para[i].getParameter_To();
			}
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{			
		/*********/
		DB.executeUpdate("DELETE FROM T_LIBROCVXMLFEL", get_TrxName());
		/*********/
		String p_where,whereDoc,p_date;
		
		if(p_TipoOperacion)
		{
        	p_where=" and i.issotrx='Y' and i.ad_org_id="+p_Org_ID;
        	p_date="dateinvoiced";
        	whereDoc ="'ARI','ARC'";
        }
        else{
        	p_date="dateacct";
        	p_where =" and i.issotrx='N'  and i.ad_org_id="+p_Org_ID;
        	whereDoc="'API','APC'";
        }
		
		addDetalle(p_datefrom,p_dateto, whereDoc,p_date,p_where);
		
		DB.executeUpdate("UPDATE T_LIBROCVXMLFEL SET AD_PInstance_ID="+p_PInstance_ID, get_TrxName());
			
		
		
		return "";
	}	//	doIt
	
	public void addDetalle(Timestamp dateFrom, Timestamp dateTo, String whereDoc, String p_date, String p_where)
	{
		String sql = "select doc.documentno,i.documentno as folio,19 as tasa,i.dateinvoiced,bp.value||'-'||bp.digito as rut," //1..5
			+" bp.name, (select coalesce(round(sum(currencyConvert(tax.taxamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0),0) from c_invoicetax tax" //6
			+" inner join c_tax tt on (tax.c_tax_id=tt.c_tax_id and upper(tt.name) LIKE '%IVA%')"
			+" where tax.c_invoice_id=i.c_invoice_id) as iva"  // 7 iva
			+" , currencyConvert(i.grandtotal,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID) as grandtotal, doc.docbasetype, doc.name as documentname," //8 total
			+"(select coalesce(round(sum(currencyConvert(tax.taxbaseamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0),0) from c_invoicetax tax"
					+" inner join c_tax tt on (tax.c_tax_id=tt.c_tax_id )"
					+" where tax.c_invoice_id=i.c_invoice_id and tt.istaxexempt='Y') as exento, " //11 exe
					+"(select coalesce(sum(currencyConvert(il.linenetamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0) from c_invoiceline il where il.c_invoice_id=i.c_invoice_id" +
					" and il.c_charge_id=1000014) as diesel" // 12 diesel
					+",(select coalesce(sum(currencyConvert(il.linenetamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0) from c_invoiceline il where il.c_invoice_id=i.c_invoice_id" +
					" and il.c_charge_id=1000059) as gasolina" // 13 gasolina
			+" ,i.c_invoice_id" //14 id
			
			+",(select coalesce(sum(currencyConvert(il.taxamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0) from c_invoiceline il where il.c_invoice_id=i.c_invoice_id" +
			" and il.c_tax_id=1000004) as rdiesel" //15 rdiesel
			
			+",(select coalesce(sum(currencyConvert(il.taxamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0) from c_invoiceline il where il.c_invoice_id=i.c_invoice_id" +
			" and il.c_tax_id=1000008) as rgasolina" //16 rgasolina
			
			+",(select coalesce(sum(currencyConvert(il.linenetamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0) from c_invoiceline il where il.c_invoice_id=i.c_invoice_id" +
			" and il.c_tax_id=1000006) as ndiesel" //17 ndiesel
			
			+",(select coalesce(sum(currencyConvert(il.linenetamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0) from c_invoiceline il where il.c_invoice_id=i.c_invoice_id" +
			" and il.c_tax_id=1000007) as ngasolina" //18 ngasolina
			
			+",(select coalesce(sum(currencyConvert(il.linenetamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0) from c_invoiceline il where il.c_invoice_id=i.c_invoice_id" +
			" and il.c_tax_id=1000004) as ndiesel2" //19 ndiesel2
			
			+",(select coalesce(sum(currencyConvert(il.linenetamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0) from c_invoiceline il where il.c_invoice_id=i.c_invoice_id" +
			" and il.c_tax_id=1000008) as ngasolina2, i.docstatus,i.ad_client_id" //20 rgasolina
			
			+" from c_invoice i"
			+" inner join c_bpartner bp on (i.c_bpartner_id=bp.c_bpartner_id)"
			+" inner join c_doctype doc on (i.c_doctype_id=doc.c_doctype_id)"
			+" where i.docstatus not in ('DR','VO') and i.isactive='Y'  and doc.docbasetype IN ("+whereDoc+")"
			+"  and i."+p_date+" between ? and ? "+p_where+ " and doc.documentno is not null";
		
		log.fine("SQL:" + sql);
		MClient client = MClient.get(getCtx(), getAD_Client_ID());
    	if (client.getName().equals("Grupo Geminis"))
    		if(p_TipoOperacion)
				sql+=" and doc.documentno not in ('35','38','919','924') ";
		
    	log.fine("SQL2: " + sql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			log.fine("ININOLES Dentro de try");
			pstmt = DB.prepareStatement(sql, get_TrxName());
		
			pstmt.setTimestamp(1, p_datefrom);
			pstmt.setTimestamp(2, p_dateto);
			rs = pstmt.executeQuery();			
			log.fine("ININOLES antes de while");    
			int id_flag = DB.getSQLValue(get_TrxName(), "SELECT MAX(T_LIBROCVXMLFEL_ID) FROM T_LIBROCVXMLFEL ");
			while (rs.next())
			{
				log.fine("ININOLES Dentro de ciclo while");
				if (client.getName().equals("Grupo Geminis") )
					if(p_TipoOperacion)
						if(rs.getString(1).equals("35") || rs.getString(1).equals("38"))
							continue;
				
				int invoice_id=rs.getInt(14);
				
				String docname = rs.getString("documentname");
            	String bprut=rs.getString("rut").trim();
            	log.fine("ininoles 1" + sql);
            	if(p_TipoOperacion)//venta
	            	if(docname.toLowerCase().indexOf("interna")>0 ||  docname.toLowerCase().indexOf("export")>0 ||  docname.toLowerCase().indexOf("import")>0)
	            		bprut="55555555-5";
            	
            	log.fine("ininoles 2");
            	if (!client.getName().equals("Grupo Geminis") )
            		if(p_TipoOperacion)//venta
            			if(rs.getString(1).equals("35") || rs.getString(1).equals("38"))
            				bprut="66666666-6";
            	String anulado = "";
            	if(rs.getString("DocStatus").compareToIgnoreCase("VO") == 0)
            			anulado = "A";
            	log.fine("ininoles 3");
            	int mesNow = TimeUtil.getToday().get(Calendar.MONTH) + 1;
				String sysdate="TO_DATE('"+TimeUtil.getToday().get(Calendar.YEAR)+"-"+mesNow+"-"+ TimeUtil.getToday().get(Calendar.DAY_OF_MONTH)+"','YYYY-MM-DD')";
				Timestamp dateInv = rs.getTimestamp("dateinvoiced");
				Calendar calendar = Calendar.getInstance();
		        calendar.setTimeInMillis(dateInv.getTime());
		        int mesInvoice =  calendar.get(Calendar.MONTH)+1;
				String dateInvTxt="TO_DATE('"+calendar.get(Calendar.YEAR)+"-"+ mesInvoice+"-"+ calendar.get(Calendar.DAY_OF_MONTH)+"','YYYY-MM-DD')";
				
				
				//BigDecimal cargo_diesel = rs.getBigDecimal(12).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            	//BigDecimal cargo_gasolina = rs.getBigDecimal(13).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            	BigDecimal r_diesel = rs.getBigDecimal(15).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            	BigDecimal r_gasolina = rs.getBigDecimal(16).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            	BigDecimal n_diesel = rs.getBigDecimal(17).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            	BigDecimal n_gasolina = rs.getBigDecimal(18).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            	BigDecimal n_diesel2 = rs.getBigDecimal(19).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            	BigDecimal n_gasolina2 = rs.getBigDecimal(20).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            	
            	BigDecimal mntexe=rs.getBigDecimal(11);
            	
            	BigDecimal mntneto = DB.getSQLValueBD(get_TrxName(), (new StringBuilder()).append("select COALESCE(SUM(currencyConvert(tax.taxbaseamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0) " +
    			" FROM c_invoicetax tax  inner join c_invoice i on (tax.c_invoice_id=i.c_invoice_id) inner join C_Tax t on (tax.c_tax_id = t.c_tax_id) where UPPER (t.NAME) Like '%IVA%' and i.C_Invoice_ID=").append(rs.getInt("C_Invoice_ID")).toString());
            
	        	if(rs.getString(1).trim().equals("914") && mntneto.signum()==0) 
					mntneto =  rs.getBigDecimal("iva").multiply(Env.ONEHUNDRED).divide(new BigDecimal(19),0, BigDecimal.ROUND_HALF_EVEN).setScale(0, BigDecimal.ROUND_HALF_EVEN);
	        	
	        	BigDecimal mntiva = rs.getBigDecimal("iva");
	        	//algoritmo iva no recuperable inicio
				log.fine("iva no recuperable");
				
				
            	PreparedStatement pstmt2 = null;
    			ResultSet rs2 = null;
    			String sql2="select coalesce(sum(currencyConvert(il.taxamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)),0),il.CodIVANoRec" 
    			+" from  c_invoice i " 
    			+" inner join c_invoiceline il on (il.c_invoice_id=i.c_invoice_id)"
				+" inner join c_tax tt on (il.c_tax_id=tt.c_tax_id and upper(tt.name)='IVA NO RECUPERABLE')"
				+" where i.c_invoice_id="+rs.getInt("c_invoice_id")
				+ " group by il.CodIVANoRec";
    			String CodIVANoRec = "";
    			BigDecimal ivaNoRec = new BigDecimal("0.0");
    			
    			try
    			{
    				pstmt2 = DB.prepareStatement(sql2, get_TrxName());
    				rs2 = pstmt2.executeQuery();
    				while (rs2.next())
    				{	
    					if(rs2.getString(1)==null)
    						continue;
    					if(rs2.getString(2)!=null && rs2.getString(2).length()>0)
    						CodIVANoRec = rs2.getString(2).trim();	    					
    	            	ivaNoRec = rs2.getBigDecimal(1).setScale(0, BigDecimal.ROUND_HALF_EVEN);
    				}

					rs2.close();
					pstmt2.close();
					pstmt2 = null;
				}
				catch (Exception e)
				{
					log.severe(e.getMessage());
				}
            	//algoritmo iva no recuperable fin
	        	
	        	//compra de activos fijos inicio
            	BigDecimal activoNeto = Env.ZERO;
            	BigDecimal activoIva = Env.ZERO;
				if(!p_TipoOperacion)//compras
				{
					PreparedStatement pstmt3 = null;
	    			ResultSet rs3 = null;
	    			String sql3 = " select  sum(currencyConvert(il.linenetamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)) as neto , sum (currencyConvert(il.taxamt,i.C_Currency_ID,228,i.DateAcct,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)) as iva  " +
	    					"from C_InvoiceLine il inner join c_invoice i on (il.c_invoice_id=i.c_invoice_id) " +
	    					" Where il.c_invoice_id = ? and il.a_capvsexp = 'Cap' and il.a_createasset='Y' ";
	    			try
	    			{
	    				pstmt3 = DB.prepareStatement(sql3, get_TrxName());
	    				pstmt3.setInt(1, invoice_id);
	    				
	    				rs3 = pstmt3.executeQuery();
	    				while (rs3.next())
	    				{		    					
	    					if(rs3.getString(1) == null)
	    						continue;		    	            	
	    	            	activoNeto = rs3.getBigDecimal(1);
	    	            	activoIva = rs3.getBigDecimal(2);		    	            	
	    				}
	    				rs3.close();
						pstmt3.close();
						pstmt3 = null;
					}
					catch (Exception e)
					{
						log.severe(e.getMessage());
					}
				}
				//compra de activos fijos fin
				//impuesto especifico inicio
				int codImp_1 = 0;
    			BigDecimal TasaImp_1 = null;
    			BigDecimal MntImp_1 = null;
    			int codImp_2 = 0;
    			BigDecimal TasaImp_2 = null;
    			BigDecimal MntImp_2 = null;
    			BigDecimal norec= Env.ZERO;
            	if(!p_TipoOperacion)
            	{//compras	            		
            		    		
            		if(p_Org_ID == 1000002 && (r_diesel.signum()>0 || r_gasolina.signum()>0) )
            		{	
            			if(r_diesel.signum()>0)
            			{
            				codImp_1 = 29;
            				TasaImp_1 = new BigDecimal("29.65");
            				MntImp_1 = r_diesel.setScale(0, BigDecimal.ROUND_HALF_EVEN);
		            	}
		            	if( r_gasolina.signum()>0)
		            	{
		            		codImp_2 = 35;
            				TasaImp_2 = new BigDecimal("29.65");
            				MntImp_2 = r_gasolina.setScale(0, BigDecimal.ROUND_HALF_EVEN);				            	
		            	}
            		}
	            	//---------------------------
	            	if(norec.signum()>0 || n_gasolina.signum()>0 || n_diesel.signum()>0 || n_diesel2.signum()>0 || n_gasolina2.signum()>0)
	            	{
	            		norec = norec.add(n_gasolina);
	            		norec = norec.add(n_diesel);
	            		norec = norec.add(n_diesel2);
	            		norec = norec.add(n_gasolina2);
	            		norec = norec.subtract(r_gasolina);
	            		norec = norec.subtract(r_diesel);
	            	}
	            	
            	}
            	//impuesto especifico fin
            	log.fine("ininoles 4");
            	BigDecimal mtotal = rs.getBigDecimal("grandtotal").setScale(0, BigDecimal.ROUND_HALF_EVEN);
            	log.fine("ininoles 5");
				if(rs.getString(1).trim().equals("914"))
            		mtotal = mntexe.add(mntneto).add(mntiva);
				log.fine("ininoles 6");
				String IssoTrxTXT = "Y";
				if(!p_TipoOperacion)
					IssoTrxTXT = "N";
				if(CodIVANoRec == "" || CodIVANoRec == " ")
					CodIVANoRec = "null";
				else if(CodIVANoRec != null && CodIVANoRec.length() <=0)
					CodIVANoRec = "null";				
				id_flag++;
				String Insert=new String("INSERT INTO T_LIBROCVXMLFEL(T_LIBROCVXMLFEL_ID,AD_CLIENT_ID,AD_ORG_ID,CREATED,UPDATED,CREATEDBY,UPDATEDBY," +
						" Tipo_Doc,Folio,Rut_Contraparte,Tasa_Impuesto,Razon_Social_Contraparte,Tipo_Impuesto,Fecha_Emision,Anulado,Monto_Exento,Monto_Neto,Monto_IVA_R,Cod_IVA_N_R,Monto_IVA_N_R," +
						" IVA_Uso_Comun,Factor_IVA_Uso_Comun,Cod_Otro_Imp_C_C,Tasa_Otro_Imp_C_C,Monto_Otro_Imp_CC,Monto_Total,Monto_Otro_Imp_S_C,Monto_Activo_Fijo,Monto_IVA_Activo_Fijo," +
						" IVA_No_Retenido,Cod_Otro_Imp_C_C_2,Tasa_Otro_Imp_C_C_2,Monto_Otro_Imp_C_C_2,Cod_Otro_Imp_C_C_3,Tasa_Otro_Imp_C_C_3,Monto_Otro_Imp_C_C_3,IsSOTrx,DateTrx)"+
						" VALUES ("+id_flag+","+rs.getInt("AD_CLIENT_ID")+","+p_Org_ID+","+sysdate+","+sysdate+",100,100," +
						rs.getString(1).trim()+","+rs.getString("folio").trim()+",'"+bprut+"',"+rs.getInt("tasa")+","+
						"'"+rs.getString("name").trim()+"',1,"+dateInvTxt+",'"+anulado+"',"+mntexe.setScale(0, BigDecimal.ROUND_HALF_EVEN)+","+
						mntneto.setScale(0, BigDecimal.ROUND_HALF_EVEN)+","+mntiva.setScale(0, BigDecimal.ROUND_HALF_EVEN)+","+CodIVANoRec+","+ivaNoRec.setScale(0, BigDecimal.ROUND_HALF_EVEN)+","+
						"null,null,"+codImp_1+","+TasaImp_1+","+MntImp_1+","+mtotal+","+norec.setScale(0, BigDecimal.ROUND_HALF_EVEN)+","+activoNeto+","+activoIva+",null,"+
						codImp_2+","+TasaImp_2+","+MntImp_2+",null,null,null,'"+IssoTrxTXT+"',"+dateInvTxt+")");
				log.fine("ininoles 7:"+Insert);
				log.fine("*****Insert*****: "+Insert);
				
				DB.executeUpdate(Insert, get_TrxName());    	
            	
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.severe(e.getMessage());
		}
	}
}	//	OrderOpen
