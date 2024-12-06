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
package org.mutual.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: SeparateInvoices.java $
 */
public class SeparateInvoices extends SvrProcess
{
	private int			m_C_Invoice_ID = 0;
	private Timestamp p_DateInvoiced_From;
	
	private int			created = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 ProcessInfoParameter[] para = getParameter();
			for (int i = 0; i < para.length; i++)
			{
				String name = para[i].getParameterName();
				
				if (name.equals("C_Invoice_ID"))
					m_C_Invoice_ID = para[i].getParameterAsInt();
				else if (name.equals("DateInvoiced"))
					p_DateInvoiced_From = (Timestamp)para[i].getParameter();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		 StringBuffer sql= new StringBuffer("select i.C_Invoice_ID, i.DocumentNo, i.C_BPartner_ID ")
			.append(" from C_Invoice i ")
			//.append(" where C_DocType_ID=1000002 AND CECOMutual is not null AND")
			.append(" where C_DocType_ID=1000002 AND")//ceco mutual se sacara de la ORG
			.append(" not exists (select * from C_Invoice where ref_invoice_id = i.C_Invoice_ID AND DocStatus IN ('CO','DR','IP','CL')) "); // prefacturas sin solicitud
		 
		 String clientCheck = " AND i.AD_Client_ID=" + Env.getAD_Client_ID(getCtx());
		 sql.append (clientCheck);
		 
		 if(m_C_Invoice_ID>0)
			 sql.append(" and i.C_Invoice_ID="+m_C_Invoice_ID);
		 
		 if(p_DateInvoiced_From != null)
			 sql.append(" and i.DateInvoiced <= '"+p_DateInvoiced_From+"'");
		 
	     PreparedStatement pstmt = null;
		 ResultSet rs = null;
		 try
		  {
			pstmt = DB.prepareStatement(sql.toString(), this.get_TrxName());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MBPartner bp = MBPartner.get(getCtx(), rs.getInt(3));
				String groupOrder = "01";
				if(bp.get_ValueAsInt("T_TradeAgreements_ID")>0)
				{
					String goTemp = DB.getSQLValueString(get_TrxName(), "SELECT GroupOrder FROM T_TradeAgreements WHERE T_TradeAgreements_ID = ? ", bp.get_ValueAsInt("T_TradeAgreements_ID"));
					if(goTemp != null)
						groupOrder = goTemp; 
				}
				//creating header
				createHeaders(rs.getInt(1), groupOrder);
				
			}
		  }
		  catch (Exception e)
		  {
				log.severe(e.getMessage());
		  }
		finally{
			 DB.close(rs, pstmt);
			 rs=null;pstmt=null;
		 }
		
		
	   return "Generadas "+ created;
	}	//	doIt


	private int createHeaders(int C_Invoice_ID, String groupOrder)
	{
		String cAgrupado = "o.C_Order_ID";
		if(groupOrder.compareToIgnoreCase("03") == 0)
			cAgrupado = "o.C_BPartner_Location_ID";
		else if(groupOrder.compareToIgnoreCase("02") == 0)
			cAgrupado = "o.AD_User_ID";
		else if(groupOrder.compareToIgnoreCase("04") == 0)
			cAgrupado = "il.POReference";
		else if(groupOrder.compareToIgnoreCase("01") == 0)
			cAgrupado = "il.C_Invoice_ID";
		else
			cAgrupado = "il.C_Invoice_ID";
		
		 StringBuffer sqlHeader= new StringBuffer("select "+cAgrupado+", count(1) ")
			.append("from C_InvoiceLine il ")
			.append("inner join C_OrderLine ol on (il.C_OrderLine_ID=ol.C_OrderLine_ID) ")
			.append("inner join C_Order o on (ol.C_Order_ID=o.C_Order_ID) ")
			.append("where il.C_Invoice_ID=? ")
			.append(" AND il.AD_Client_ID=" + Env.getAD_Client_ID(getCtx()))
			.append(" group by "+cAgrupado); 
		 
	     PreparedStatement pstmt = null;
		 ResultSet rs = null;
		 try
		  {
			pstmt = DB.prepareStatement(sqlHeader.toString(), this.get_TrxName());
			pstmt.setInt(1, C_Invoice_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				//creating header
				MInvoice original = MInvoice.get(getCtx(), C_Invoice_ID);
				MInvoice newinvoice = new MInvoice(getCtx(),0,get_TrxName());
				newinvoice.setBPartner(MBPartner.get(getCtx(), original.getC_BPartner_ID())); 
				if(DB.isPostgreSQL())
					newinvoice.setC_DocTypeTarget_ID(1000061);//documento solicitud //66 oracle 61 postgreSQL
				else if (DB.isOracle())
					newinvoice.setC_DocTypeTarget_ID(1000066);//documento solicitud //66 oracle 61 postgreSQL
				else
					newinvoice.setC_DocTypeTarget_ID(1000066);//documento solicitud //66 oracle 61 postgreSQL
				newinvoice.setRef_Invoice_ID(C_Invoice_ID);
				newinvoice.setAD_Org_ID(original.getAD_Org_ID());
				if(groupOrder.compareToIgnoreCase("03") == 0)
					newinvoice.setC_BPartner_Location_ID(rs.getInt(1));
				else
					newinvoice.setC_BPartner_Location_ID(original.getC_BPartner_Location_ID());
				if(groupOrder.compareToIgnoreCase("02") == 0)
					newinvoice.setAD_User_ID(rs.getInt(1));
				//si no es agrupada por usuario que no guarde el usuario
				/*else
					newinvoice.setAD_User_ID(original.getAD_User_ID());*/
				if(groupOrder.compareToIgnoreCase("04") == 0)
					newinvoice.setPOReference(rs.getString(1));
				else
					newinvoice.setPOReference(original.getPOReference());				
				newinvoice.set_CustomColumn("GroupOrder", groupOrder);
				newinvoice.save();
				created ++;
				if(groupOrder.compareToIgnoreCase("03") == 0 ||
					groupOrder.compareToIgnoreCase("02") == 0 ||
					groupOrder.compareToIgnoreCase("01") == 0)
					createLines(newinvoice,C_Invoice_ID,cAgrupado,rs.getInt(1));
				else if (groupOrder.compareToIgnoreCase("04") == 0)
					createLinesPORef(newinvoice,C_Invoice_ID,cAgrupado,rs.getString(1));
				
			}
		  }
		  catch (Exception e)
		  {
				log.severe(e.getMessage());
		  }
		finally{
			 DB.close(rs, pstmt);
			 rs=null;pstmt=null;
		 }
		 
		 
		 return 0;
	}
	
	private void  createLines(MInvoice newinvoice, int C_Invoice_ID, String cAgrupado, int valueCAgrupado)
	{
		StringBuffer sqlsinDroga= new StringBuffer("select il.M_Product_ID,p.Name,d.Name as Servicio, il.QtyInvoiced, il.C_InvoiceLine_ID, il.PriceActual ")
		.append("from C_InvoiceLine il ")
		.append("inner join C_OrderLine ol on (il.C_OrderLine_ID=ol.C_OrderLine_ID) ")
		.append("inner join C_Order o on (ol.C_Order_ID=o.C_Order_ID) ")
		.append("inner join C_DocType d on (o.C_DocType_ID=d.C_DocType_ID) ")
		.append("inner join M_Product p on (il.M_Product_ID=p.M_Product_ID) ")
		.append("where il.C_Invoice_ID = ? and "+cAgrupado+" = "+valueCAgrupado+" ")
		.append(" AND il.AD_Client_ID=" + Env.getAD_Client_ID(getCtx()));
		
		
		sqlsinDroga.append(" Order by d.Name,p.Name");
	 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sqlsinDroga.toString(), this.get_TrxName());
			pstmt.setInt(1, C_Invoice_ID);
				
			rs = pstmt.executeQuery ();
			MInvoiceLine line = null;
			while (rs.next ())//sin droga
			{	
				MInvoiceLine oldLine = new MInvoiceLine(getCtx(),rs.getInt(5) ,get_TrxName());
				new MInvoiceLine(newinvoice);
				line = new MInvoiceLine(newinvoice); //nueva linea
				line.setM_Product_ID(rs.getInt(1), true);
				line.setPrice(rs.getBigDecimal(6));
				line.setQty(rs.getBigDecimal(4));
				line.set_CustomColumn("Ref_InvoiceLine_ID", rs.getInt(5));
				line.setC_Tax_ID(oldLine.getC_Tax_ID());
				line.setM_AttributeSetInstance_ID(oldLine.getM_AttributeSetInstance_ID());
				line.setLineNetAmt();				
				line.save(); // y guardo
			}//while	
			
			if(line!=null)
				line.save();
		  }
		catch (Exception e)
		{
			 log.severe(e.getMessage());
		}
		finally{
			DB.close(rs, pstmt);
			rs=null;pstmt=null;
		}
	}
	private void  createLinesPORef(MInvoice newinvoice, int C_Invoice_ID, String cAgrupado, String valueCAgrupado)
	{
		StringBuffer sqlsinDroga= new StringBuffer("select il.M_Product_ID,p.Name,d.Name as Servicio, il.QtyInvoiced, il.C_InvoiceLine_ID, il.PriceActual ")
		.append("from C_InvoiceLine il ")
		.append("inner join C_OrderLine ol on (il.C_OrderLine_ID=ol.C_OrderLine_ID) ")
		.append("inner join C_Order o on (ol.C_Order_ID=o.C_Order_ID) ")
		.append("inner join C_DocType d on (o.C_DocType_ID=d.C_DocType_ID) ")
		.append("inner join M_Product p on (il.M_Product_ID=p.M_Product_ID) ")
		.append("where il.C_Invoice_ID = ? and "+cAgrupado+" = '"+valueCAgrupado+"' ")
		.append(" AND il.AD_Client_ID=" + Env.getAD_Client_ID(getCtx()));
		
		
		sqlsinDroga.append(" Order by d.Name,p.Name");
	 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sqlsinDroga.toString(), this.get_TrxName());
			pstmt.setInt(1, C_Invoice_ID);
				
			rs = pstmt.executeQuery ();
			MInvoiceLine line = null;
			while (rs.next ())//sin droga
			{	
				MInvoiceLine oldLine = new MInvoiceLine(getCtx(),rs.getInt(5) ,get_TrxName());
				new MInvoiceLine(newinvoice);
				line = new MInvoiceLine(newinvoice); //nueva linea
				line.setM_Product_ID(rs.getInt(1), true);
				line.setPrice(rs.getBigDecimal(6));
				line.setQty(rs.getBigDecimal(4));
				line.set_CustomColumn("Ref_InvoiceLine_ID", rs.getInt(5));
				line.setC_Tax_ID(oldLine.getC_Tax_ID());
				line.setM_AttributeSetInstance_ID(oldLine.getM_AttributeSetInstance_ID());
				line.setLineNetAmt();				
				line.save(); // y guardo
			}//while	
			
			if(line!=null)
				line.save();
		  }
		catch (Exception e)
		{
			 log.severe(e.getMessage());
		}
		finally{
			DB.close(rs, pstmt);
			rs=null;pstmt=null;
		}
	}
}
