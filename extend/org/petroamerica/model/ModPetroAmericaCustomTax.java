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
package org.petroamerica.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company PT
 *
 *  @author Italo Niñoles
 */
public class ModPetroAmericaCustomTax implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModPetroAmericaCustomTax ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModPetroAmericaCustomTax.class);
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
		engine.addModelChange(MOrderLine.Table_Name, this);
		engine.addModelChange(MInvoiceLine.Table_Name, this);
		engine.addModelChange(MOrder.Table_Name, this);
		engine.addModelChange(MInvoice.Table_Name, this);
		//	Documents to be monitored
		//engine.addDocValidate(MOrder.Table_Name, this);
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE)&& po.get_Table_ID()==MOrderLine.Table_ID)
		{
			MOrderLine oLine = (MOrderLine)po;
			MOrder order = new MOrder(po.getCtx(), oLine.getC_Order_ID(), po.get_TrxName());
			if(order.getDocStatus().compareToIgnoreCase("CO") != 0 && order.getDocStatus().compareToIgnoreCase("CL") != 0)
			{
				if(oLine.getM_Product_ID() > 0)
				{	
					//ininoles impuesto se sacaran del product price en vez del producto
					//MProduct prod = new MProduct(po.getCtx(), oLine.getM_Product_ID(), po.get_TrxName());
					//calculamos version de lista de precios
					int ID_PriceListVersion = 0;
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					String sql = "SELECT plv.M_PriceList_Version_ID,plv.ValidFrom "
						+ "FROM M_PriceList pl,C_Currency c,M_PriceList_Version plv "
						+ "WHERE pl.C_Currency_ID=c.C_Currency_ID"
						+ " AND pl.M_PriceList_ID=plv.M_PriceList_ID"
						+ " AND pl.M_PriceList_ID=? "						//	1
						+ " AND plv.ValidFrom <= ? "
						+ "ORDER BY plv.ValidFrom DESC";
					try
					{
						pstmt = DB.prepareStatement(sql, null);
						pstmt.setInt(1, oLine.getC_Order().getM_PriceList_ID());											
						pstmt.setTimestamp(2, oLine.getC_Order().getDateOrdered());					
						rs = pstmt.executeQuery();
						if (rs.next())						
							ID_PriceListVersion = rs.getInt(1);
					}catch (SQLException e)
					{
						log.log(Level.SEVERE, sql, e);
						return e.getLocalizedMessage();
					}
					finally
					{
						DB.close(rs, pstmt);
						rs = null; pstmt = null;
					}
					//impuesto fijo				
					BigDecimal taxNewPro = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(FixedTax) " +
							"FROM M_ProductPrice WHERE M_PriceList_Version_ID = "+ID_PriceListVersion+" AND M_Product_ID = "+oLine.getM_Product_ID());				
					BigDecimal taxNewLine = (BigDecimal)oLine.get_Value("FixedTax");				
					if((taxNewLine == null || taxNewLine.compareTo(Env.ZERO) == 0) && taxNewPro != null)
					{
						oLine.set_CustomColumn("FixedTax",taxNewPro);
						taxNewLine = taxNewPro;
					}
					if(taxNewLine != null)
					{
						taxNewPro = taxNewLine.multiply(oLine.getQtyOrdered());					
						oLine.set_CustomColumn("FixedTaxAmt",taxNewPro);
					}
					//impuesto variable
					taxNewPro = null;
					taxNewLine = null;
					taxNewPro = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(VariableTax) " +
							"FROM M_ProductPrice WHERE M_PriceList_Version_ID = "+ID_PriceListVersion+" AND M_Product_ID = "+oLine.getM_Product_ID());
					taxNewLine = (BigDecimal)oLine.get_Value("VariableTax");
					if((taxNewLine == null || taxNewLine.compareTo(Env.ZERO) == 0) && taxNewPro != null)
					{
						oLine.set_CustomColumn("VariableTax",taxNewPro);
						taxNewLine = taxNewPro;
					}
					if(taxNewLine != null)
					{
						taxNewPro = taxNewLine.multiply(oLine.getQtyOrdered());
						oLine.set_CustomColumn("VariableTaxAmt",taxNewPro);
					}	
				}
				//calculamos impuesto IVA
				BigDecimal IVATax = (BigDecimal)oLine.get_Value("IVATaxAmt");
				if(oLine.getC_Tax().getName().toLowerCase().contains("iva"))
				{
					IVATax = oLine.getLineNetAmt().multiply(oLine.getC_Tax().getRate());
					IVATax = IVATax.divide(Env.ONEHUNDRED);
					IVATax = IVATax.setScale(10,BigDecimal.ROUND_HALF_EVEN);
					oLine.set_CustomColumn("IVATaxAmt", IVATax);
				}
			}
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)&& po.get_Table_ID()==MOrderLine.Table_ID)
		{	
			MOrderLine oLine = (MOrderLine)po;
			//MOrder order = new MOrder(po.getCtx(), oLine.getC_Order_ID(), po.get_TrxName());
			//if(order.getDocStatus().compareToIgnoreCase("CO") != 0)
			//{
				BigDecimal taxNewLine = Env.ZERO;
				if((BigDecimal)oLine.get_Value("FixedTaxAmt") != null)
					taxNewLine = taxNewLine.add((BigDecimal)oLine.get_Value("FixedTaxAmt"));
				if((BigDecimal)oLine.get_Value("VariableTax") != null)
					taxNewLine = taxNewLine.add((BigDecimal)oLine.get_Value("VariableTaxAmt"));
				if((BigDecimal)oLine.get_Value("IVATaxAmt") != null)
					taxNewLine = taxNewLine.add((BigDecimal)oLine.get_Value("IVATaxAmt"));
				if(taxNewLine != null)
				{				
					//ininoles redondeamos grandtotal sin decimales
					taxNewLine = taxNewLine.setScale(10,BigDecimal.ROUND_HALF_EVEN);
					BigDecimal newGrandTotalSD = oLine.getC_Order().getTotalLines().add(taxNewLine).setScale(0, RoundingMode.HALF_EVEN);
					DB.executeUpdate("UPDATE C_Order SET GrandTotal = "+newGrandTotalSD+" " +
							" WHERE C_Order_ID = "+oLine.getC_Order_ID(), po.get_TrxName());
			//	}
			}
		}	
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)&& po.get_Table_ID()==MOrder.Table_ID)
		{	
			MOrder order = (MOrder)po;			
			MOrderLine[] olines = order.getLines();
			BigDecimal taxNewLine = Env.ZERO;
			for (int i = 0; i < olines.length; i++)
			{
				MOrderLine oline = olines[i];
				if((BigDecimal)oline.get_Value("FixedTaxAmt") != null)
					taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("FixedTaxAmt"));
				if((BigDecimal)oline.get_Value("VariableTax") != null)
					taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("VariableTaxAmt"));
				if((BigDecimal)oline.get_Value("IVATaxAmt") != null)
					taxNewLine = taxNewLine.add((BigDecimal)oline.get_Value("IVATaxAmt"));				
			}			
			if(taxNewLine != null)
			{				
				taxNewLine = taxNewLine.setScale(10,BigDecimal.ROUND_HALF_EVEN);
				BigDecimal newGrandTotalSD = order.getTotalLines().add(taxNewLine).setScale(0, RoundingMode.HALF_EVEN);
				DB.executeUpdate("UPDATE C_Order SET GrandTotal = "+newGrandTotalSD+" " +
						" WHERE C_Order_ID = "+order.get_ID(), po.get_TrxName());
			}
		}
		//if((type == TYPE_BEFORE_NEW || po.is_ValueChanged("QtyEntered")) && po.get_Table_ID()==MInvoiceLine.Table_ID)
		//que siempre se actualice si es venta
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==MInvoiceLine.Table_ID)
		{
			MInvoiceLine iLine = (MInvoiceLine)po;
			MInvoice inv = new MInvoice(po.getCtx(), iLine.getC_Invoice_ID(), po.get_TrxName());
			if((inv.isSOTrx() && inv.getDocStatus().compareToIgnoreCase("CO") != 0)
					||(po.is_ValueChanged("QtyEntered") && inv.getDocStatus().compareToIgnoreCase("CO") != 0 )
					||(po.is_ValueChanged("IVATaxAmt") && inv.getDocStatus().compareToIgnoreCase("CO") != 0 ))
			{
				MOrderLine oLine = null;
				if(iLine.getC_OrderLine_ID() > 0)
					oLine = new MOrderLine(po.getCtx(), iLine.getC_OrderLine_ID(), po.get_TrxName());
				if(iLine.getM_Product_ID() > 0)
				{
					//MProduct prod = new MProduct(po.getCtx(), iLine.getM_Product_ID(), po.get_TrxName());
					BigDecimal taxOLine = null;
					//calculamos version de lista de precios
					int ID_PriceListVersion = 0;
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					String sql = "SELECT plv.M_PriceList_Version_ID,plv.ValidFrom "
						+ "FROM M_PriceList pl,C_Currency c,M_PriceList_Version plv "
						+ "WHERE pl.C_Currency_ID=c.C_Currency_ID"
						+ " AND pl.M_PriceList_ID=plv.M_PriceList_ID"
						+ " AND pl.M_PriceList_ID=? "						//	1
						+ " AND plv.ValidFrom <= ? "
						+ "ORDER BY plv.ValidFrom DESC";
					try
					{
						pstmt = DB.prepareStatement(sql, null);
						pstmt.setInt(1, iLine.getC_Invoice().getM_PriceList_ID());											
						pstmt.setTimestamp(2, iLine.getC_Invoice().getDateInvoiced());					
						rs = pstmt.executeQuery();
						if (rs.next())						
							ID_PriceListVersion = rs.getInt(1);
					}catch (SQLException e)
					{
						log.log(Level.SEVERE, sql, e);
						return e.getLocalizedMessage();
					}
					finally
					{
						DB.close(rs, pstmt);
						rs = null; pstmt = null;
					}				
					//impuesto fijo
					//seteamos si biene de una orden			
					if(oLine != null)
					{
						taxOLine = (BigDecimal)oLine.get_Value("FixedTax");
						if(taxOLine != null)
							iLine.set_CustomColumn("FixedTax",taxOLine);
					}
					BigDecimal taxNewProd = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(FixedTax) " +
							"FROM M_ProductPrice WHERE M_PriceList_Version_ID = "+ID_PriceListVersion+" AND M_Product_ID = "+iLine.getM_Product_ID());
					BigDecimal taxNewLine = (BigDecimal)iLine.get_Value("FixedTax");		
					if(taxNewLine == null && taxNewProd != null)
					{
						iLine.set_CustomColumn("FixedTax",taxNewProd);
						taxNewLine = taxNewProd;
					}								
					if(taxNewLine != null)
					{
						taxNewProd = taxNewLine.multiply(iLine.getQtyInvoiced());
						iLine.set_CustomColumn("FixedTaxAmt",taxNewProd);
					}
					//impuesto variable
					//seteamos si biene de una orden				
					if(oLine != null)
					{
						taxOLine = (BigDecimal)oLine.get_Value("VariableTax");
						if(taxOLine != null)
							iLine.set_CustomColumn("VariableTax",taxOLine);
					}	
					taxNewProd = null;
					taxNewLine = null;
					taxNewProd = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(VariableTax) " +
							"FROM M_ProductPrice WHERE M_PriceList_Version_ID = "+ID_PriceListVersion+" AND M_Product_ID = "+iLine.getM_Product_ID());
					taxNewLine = (BigDecimal)iLine.get_Value("VariableTax");				
					if(taxNewLine == null && taxNewProd != null)
					{
						iLine.set_CustomColumn("VariableTax",taxNewProd);
						taxNewLine = taxNewProd;
					}				
					if(taxNewLine != null)
					{
						taxNewProd = taxNewLine.multiply(iLine.getQtyInvoiced());
						iLine.set_CustomColumn("VariableTaxAmt",taxNewProd);
					}
					/*BigDecimal totalTaxNewLine = Env.ZERO;
					if((BigDecimal)iLine.get_Value("FixedTaxAmt") != null)
						totalTaxNewLine = totalTaxNewLine.add((BigDecimal)iLine.get_Value("FixedTaxAmt"));
					if((BigDecimal)iLine.get_Value("VariableTax") != null)
						totalTaxNewLine = totalTaxNewLine.add((BigDecimal)iLine.get_Value("VariableTaxAmt"));
					if(totalTaxNewLine != null && totalTaxNewLine.compareTo(Env.ZERO) > 0)
						iLine.setTaxAmt(iLine.getTaxAmt().add(totalTaxNewLine));*/
				}
				//calculamos impuesto IVA
				BigDecimal IVATax = null;
				if(oLine != null)
				{
					IVATax = (BigDecimal)oLine.get_Value("IVATaxAmt");
					if(IVATax != null)
					{
						IVATax = IVATax.multiply(iLine.getQtyEntered());
						if(oLine.getQtyOrdered().compareTo(Env.ZERO) == 0)
							IVATax = IVATax.divide(Env.ONE,10, RoundingMode.HALF_EVEN);
						else
							IVATax = IVATax.divide(oLine.getQtyOrdered(),10, RoundingMode.HALF_EVEN);
					}				
					iLine.set_CustomColumn("IVATaxAmt", IVATax);
				}
				else
				{
					if(iLine.getC_Tax_ID() > 0 && iLine.getC_Tax().getName().toLowerCase().contains("iva"))
					{
						IVATax = iLine.getLineNetAmt().multiply(iLine.getC_Tax().getRate());
						IVATax = IVATax.divide(Env.ONEHUNDRED);
						IVATax = IVATax.setScale(10,BigDecimal.ROUND_HALF_EVEN);
						iLine.set_CustomColumn("IVATaxAmt", IVATax);
					}
				}
				BigDecimal totalTaxNewLine = Env.ZERO;
				if((BigDecimal)iLine.get_Value("FixedTaxAmt") != null)
					totalTaxNewLine = totalTaxNewLine.add((BigDecimal)iLine.get_Value("FixedTaxAmt"));
				if((BigDecimal)iLine.get_Value("VariableTax") != null)
					totalTaxNewLine = totalTaxNewLine.add((BigDecimal)iLine.get_Value("VariableTaxAmt"));
				if((BigDecimal)iLine.get_Value("IVATaxAmt") != null)
					totalTaxNewLine = totalTaxNewLine.add((BigDecimal)iLine.get_Value("IVATaxAmt"));
				if(totalTaxNewLine != null)
				{
					iLine.setTaxAmt(totalTaxNewLine);
					iLine.setLineTotalAmt(iLine.getLineNetAmt().add(totalTaxNewLine));
				}	
			}						
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)&& po.get_Table_ID()==MInvoiceLine.Table_ID)
		{
			MInvoiceLine iLine = (MInvoiceLine)po;
			MInvoice inv = new MInvoice(po.getCtx(), iLine.getC_Invoice_ID(), po.get_TrxName());
			if(inv.getDocStatus().compareToIgnoreCase("CO") != 0)
			{
				MInvoiceLine[] ilines = inv.getLines();
				BigDecimal taxNewLine = Env.ZERO;
				BigDecimal taxLineFixed = Env.ZERO;
				BigDecimal taxLineVariable = Env.ZERO;
				BigDecimal taxLineIVA = Env.ZERO;
				for (int i = 0; i < ilines.length; i++)
				{
					MInvoiceLine iline = ilines[i];
					if((BigDecimal)iline.get_Value("FixedTaxAmt") != null)
						taxLineFixed = taxLineFixed.add((BigDecimal)iline.get_Value("FixedTaxAmt"));
					if((BigDecimal)iline.get_Value("VariableTax") != null)
						taxLineVariable = taxLineVariable.add((BigDecimal)iline.get_Value("VariableTaxAmt"));
					if((BigDecimal)iline.get_Value("IVATaxAmt") != null)
						taxLineIVA = taxLineIVA.add((BigDecimal)iline.get_Value("IVATaxAmt"));
				}		
				taxLineFixed = taxLineFixed.setScale(0,BigDecimal.ROUND_HALF_EVEN);
				taxLineVariable = taxLineVariable.setScale(0,BigDecimal.ROUND_HALF_EVEN);
				taxLineIVA = taxLineIVA.setScale(0,BigDecimal.ROUND_HALF_EVEN);
				taxNewLine = taxLineFixed.add(taxLineVariable).add(taxLineIVA);
				if(taxNewLine != null)
				{				
					taxNewLine = taxNewLine.setScale(10,BigDecimal.ROUND_HALF_EVEN);
					BigDecimal newGrandTotalSD = inv.getTotalLines().add(taxNewLine).setScale(0, RoundingMode.HALF_EVEN);
					DB.executeUpdate("UPDATE C_Invoice SET GrandTotal = "+newGrandTotalSD+" " +
							" WHERE C_Invoice_ID = "+inv.get_ID(), po.get_TrxName());
				}
			}	
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)&& po.get_Table_ID() == MInvoice.Table_ID)
		{	
			MInvoice inv = (MInvoice)po;
			if(inv.getDocStatus().compareToIgnoreCase("CO") != 0)
			{
				MInvoiceLine[] ilines = inv.getLines();
				BigDecimal taxNewLine = Env.ZERO;
				BigDecimal taxLineFixed = Env.ZERO;
				BigDecimal taxLineVariable = Env.ZERO;
				BigDecimal taxLineIVA = Env.ZERO;
				for (int i = 0; i < ilines.length; i++)
				{
					MInvoiceLine iline = ilines[i];
					/*if((BigDecimal)iline.get_Value("FixedTaxAmt") != null)
						taxNewLine = taxNewLine.add((BigDecimal)iline.get_Value("FixedTaxAmt"));
					if((BigDecimal)iline.get_Value("VariableTax") != null)
						taxNewLine = taxNewLine.add((BigDecimal)iline.get_Value("VariableTaxAmt"));
					if((BigDecimal)iline.get_Value("IVATaxAmt") != null)
						taxNewLine = taxNewLine.add((BigDecimal)iline.get_Value("IVATaxAmt"));*/
					if((BigDecimal)iline.get_Value("FixedTaxAmt") != null)
						taxLineFixed = taxLineFixed.add((BigDecimal)iline.get_Value("FixedTaxAmt"));
					if((BigDecimal)iline.get_Value("VariableTax") != null)
						taxLineVariable = taxLineVariable.add((BigDecimal)iline.get_Value("VariableTaxAmt"));
					if((BigDecimal)iline.get_Value("IVATaxAmt") != null)
						taxLineIVA = taxLineIVA.add((BigDecimal)iline.get_Value("IVATaxAmt"));
					
				}		
				taxLineFixed = taxLineFixed.setScale(0,BigDecimal.ROUND_HALF_EVEN);
				taxLineVariable = taxLineVariable.setScale(0,BigDecimal.ROUND_HALF_EVEN);
				taxLineIVA = taxLineIVA.setScale(0,BigDecimal.ROUND_HALF_EVEN);
				taxNewLine = taxLineFixed.add(taxLineVariable).add(taxLineIVA);
				if(taxNewLine != null)
				{				
					taxNewLine = taxNewLine.setScale(10,BigDecimal.ROUND_HALF_EVEN);
					BigDecimal newGrandTotalSD = inv.getTotalLines().add(taxNewLine).setScale(0, RoundingMode.HALF_EVEN);
					DB.executeUpdate("UPDATE C_Invoice SET GrandTotal = "+newGrandTotalSD+" " +
							" WHERE C_Invoice_ID = "+inv.get_ID(), po.get_TrxName());
				}
			}
		}
		
		return null;
	}	//	modelChange
	
	public static String rtrim(String s, char c) {
	    int i = s.length()-1;
	    while (i >= 0 && s.charAt(i) == c)
	    {
	        i--;
	    }
	    return s.substring(0,i+1);
	}

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
		/*if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MOrder.Table_ID)
		{
			MOrder order = (MOrder)po;		
			BigDecimal amtTaxNew =  DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(FixedTaxAmt) + SUM(VariableTaxAmt) " +
					" FROM C_OrderLine WHERE C_Order_ID = "+order.get_ID());
			if(amtTaxNew != null)				
			{
				amtTaxNew = amtTaxNew.add(order.getGrandTotal());
				amtTaxNew = amtTaxNew.setScale(4,BigDecimal.ROUND_HALF_EVEN);
				DB.executeUpdate("UPDATE C_Order SET GrandTotal = "+amtTaxNew+
					" WHERE C_Order_ID = "+order.get_ID(), po.get_TrxName());
			}
		}*/
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInvoice.Table_ID)
		{
			MInvoice inv = (MInvoice)po;		
			BigDecimal amtTaxNew =  DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(FixedTaxAmt) + SUM(VariableTaxAmt) " +
					" FROM C_Invoice WHERE C_Invoice_ID = "+inv.get_ID());
			if(amtTaxNew != null)				
			{
				amtTaxNew = amtTaxNew.add(inv.getGrandTotal());
				amtTaxNew = amtTaxNew.setScale(4,BigDecimal.ROUND_HALF_EVEN);
				DB.executeUpdate("UPDATE C_Invoice SET GrandTotal = "+amtTaxNew+
					" WHERE C_Invoice_ID = "+inv.get_ID(), po.get_TrxName());
			}
		}
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
