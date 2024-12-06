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
package org.ignisterra.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.logging.Level;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import org.compiere.model.MClient;
import org.compiere.model.MInvoicePaySchedule;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MSequence;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_Order;
import org.compiere.model.X_C_OrderLine;
import org.compiere.model.X_C_OrderPaySchedule;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MProduct;



/**
 *	Validator for company ignisterra
 *
 *  @author Fabian Aguilar
 */
@SuppressWarnings("unused")
public class ModelOFBPrecios implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBPrecios ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBPrecios.class);
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
		//	Documents to be monitored
		engine.addModelChange(X_C_OrderLine.Table_Name, this);
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
		if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID()==X_C_OrderLine.Table_ID )  
		{
			//mfrojas. Se cambia para que no verifique a los productos que no son madera. Además, que no verifique OC. Sólo OV
			MOrderLine orderLine = (MOrderLine) po;
			
			int product = po.get_ValueAsInt("M_Product_ID");
			if(product>0)
			{			
				String sqlvalidate = "Select m_product_category_id from m_product where m_product_id = "+product;
				log.config(""+sqlvalidate);
			
				int pcategory = DB.getSQLValue(null, sqlvalidate);
				
				String sqlissotrx = "Select issotrx from c_order where c_order_id in (select c_order_id from c_orderline where c_orderline_Id =  "+po.get_ID()+")";
				PreparedStatement pstmt1 = null;
				log.config(sqlissotrx);
				pstmt1=DB.prepareStatement(sqlissotrx,null);
				ResultSet rs=pstmt1.executeQuery();
				String issotrx1 = "";
				
				if(rs.next())
					issotrx1 = rs.getString("issotrx");
				else
					issotrx1="N";
				log.config(""+sqlissotrx+" Ademas  "+issotrx1);
				//mfrojas se cambia el if para que nunca ingrese (mproductcategory)
				if(pcategory==10001700 && issotrx1.equals("Y"))
				{
					if(orderLine.getM_AttributeSetInstance_ID()>0)
					{
						PreparedStatement pstmt = null;
						BigDecimal porcentaje = new BigDecimal(0);
						BigDecimal pesos = new BigDecimal(0);
						
						StringBuffer sql1 = new StringBuffer("select sum(av.porcentaje) ")
						.append("from M_AttributeInstance ai ")
						.append("inner join M_AttributeValue av on (ai.m_attributevalue_id =av.m_attributevalue_id ) ")
						.append("where ai.m_attributesetinstance_id=?");
						porcentaje = DB.getSQLValueBD(po.get_TrxName(), sql1.toString(), orderLine.getM_AttributeSetInstance_ID());
						if(porcentaje==null)
							porcentaje = Env.ZERO;
						
						StringBuffer sql2 = new StringBuffer("select sum(av.pesos) ")
						.append("from M_AttributeInstance ai ")
						.append("inner join M_AttributeValue av on (ai.m_attributevalue_id =av.m_attributevalue_id ) ")
						.append("where ai.m_attributesetinstance_id=?");
						pesos = DB.getSQLValueBD(po.get_TrxName(), sql2.toString(), orderLine.getM_AttributeSetInstance_ID());
						if(pesos==null)
							pesos = Env.ZERO;
						
						BigDecimal temp = pesos.abs().multiply(Env.ONEHUNDRED);
						temp = temp.divide(orderLine.getPriceList(),2, BigDecimal.ROUND_HALF_UP);
						if(pesos.signum()<0)
							temp=temp.negate();
						
						porcentaje = porcentaje.add(temp);
						orderLine.setDiscount(porcentaje.negate());
						BigDecimal PriceActual = new BigDecimal ((100.0 - porcentaje.doubleValue()) / 100.0 * orderLine.getPriceList().doubleValue());
						orderLine.setPrice(PriceActual);
						orderLine.setLineNetAmt();
					}		
				}
		
			}
		}
		
		if(type == TYPE_BEFORE_NEW  && po.get_Table_ID()==X_C_OrderLine.Table_ID)  
		{
			MOrderLine orderLine = (MOrderLine) po;
			int product = po.get_ValueAsInt("M_Product_ID");
			if(product>0)
			{			
				//mfrojas se valida que sea del grupo madera y que sea una orden de venta. Si no, no aplica este tema.
				String sqlvalidate = "Select m_product_category_id from m_product where m_product_id = "+product;
				log.config(""+sqlvalidate);
			    int order1 = po.get_ValueAsInt("C_Order_ID");
			    String sqlissotrx = "Select issotrx from c_order where c_order_id = "+order1;
			    log.config("lala "+order1);
				int pcategory = DB.getSQLValue(null, sqlvalidate);

				PreparedStatement pstmt1 = null;
				pstmt1=DB.prepareStatement(sqlissotrx,null);

				//String issotrx1 = DB.getSQLValue(null, sqlissotrx);
				ResultSet rs=pstmt1.executeQuery();
				String issotrx1 = "";
				if(rs.next())
					issotrx1 = rs.getString("issotrx");
				else
					issotrx1 = "N";
				log.config(""+sqlissotrx+" Ademas  "+issotrx1);


				if(pcategory==1000170 && issotrx1.equals("Y"))
				{

					if(orderLine.getM_AttributeSetInstance_ID()>0)
					{	
						PreparedStatement pstmt = null;
						BigDecimal porcentaje = new BigDecimal(0);
						BigDecimal pesos = new BigDecimal(0);
						
						StringBuffer sql1 = new StringBuffer("select sum(av.porcentaje) ")
						.append("from M_AttributeInstance ai ")
						.append("inner join M_AttributeValue av on (ai.m_attributevalue_id =av.m_attributevalue_id ) ")
						.append("where ai.m_attributesetinstance_id=?");
						porcentaje = DB.getSQLValueBD(po.get_TrxName(), sql1.toString(), orderLine.getM_AttributeSetInstance_ID());
						if(porcentaje==null)
							porcentaje = Env.ZERO;
						
						StringBuffer sql2 = new StringBuffer("select sum(av.pesos) ")
						.append("from M_AttributeInstance ai ")
						.append("inner join M_AttributeValue av on (ai.m_attributevalue_id =av.m_attributevalue_id ) ")
						.append("where ai.m_attributesetinstance_id=?");
						pesos = DB.getSQLValueBD(po.get_TrxName(), sql2.toString(), orderLine.getM_AttributeSetInstance_ID());
						if(pesos==null)
							pesos = Env.ZERO;
						
						BigDecimal temp = pesos.abs().multiply(Env.ONEHUNDRED);
						temp = temp.divide(orderLine.getPriceList(),2, BigDecimal.ROUND_HALF_UP);
						if(pesos.signum()<0)
							temp=temp.negate();
				
						porcentaje = porcentaje.add(temp);
						orderLine.setDiscount(porcentaje.negate());
						BigDecimal PriceActual = new BigDecimal ((100.0 - porcentaje.doubleValue()) / 100.0 * orderLine.getPriceList().doubleValue());
						orderLine.setPrice(PriceActual);
						orderLine.setLineNetAmt();
					}
				}
			}
			
		}
	return null;
	}	//	modelChange

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


	

	

}	