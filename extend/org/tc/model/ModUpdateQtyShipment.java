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
package org.tc.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.math.BigDecimal;


import org.compiere.model.MClient;
import org.compiere.model.MInOutLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MProduct;
import org.compiere.model.MOrderLine;


/**
 *	Validator for TC
 *
 *  @author Italo Niñoles
 */
public class ModUpdateQtyShipment implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModUpdateQtyShipment ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModUpdateQtyShipment.class);
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
			log.info("Initializing Model Price Validator: "+this.toString());
		}

		//	Tables to be monitored
		engine.addModelChange(MInOutLine.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		//Primero se valida si el producto es de tipo "Agrupacion por viaje" o  por "Unidad de carga". 
		//Si es por unidad de carga, la cantidad de la linea se valida entre el minimo a facturar (seteado en la OC) y la cantidad ingresada.
		//Si es por viaje, la cantidad es uno.
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE )&& po.get_Table_ID()==MInOutLine.Table_ID) 
		{	
			MInOutLine shline = (MInOutLine)po;
			if(shline.getM_InOut().isSOTrx() == false)
				return null;
			//revisar si el documento ya está completo
			if(shline.getM_InOut().getDocStatus().compareTo("CO")==0)
				return null;
			//revisar
			MProduct prod = new MProduct(po.getCtx(),shline.getM_Product_ID(),po.get_TrxName());
			
			String sql = "Select GroupType from m_product_Category where m_product_Category_id = "+prod.getM_Product_Category_ID();
			PreparedStatement pstmt = null;
			String group = "";
			BigDecimal MinQty = new BigDecimal(0);
			try
			{
				pstmt = DB.prepareStatement (sql, shline.get_TrxName());
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					group = rs.getString("GroupType");
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
				
				//BigDecimal cantidad = (BigDecimal)shline.get_Value("QtyUsed");
				BigDecimal cantidad = new BigDecimal(shline.get_ValueAsInt("QtyUsed"));
				BigDecimal zerodecimal = new BigDecimal(1);
				log.config("CANTIDAD INGRESADA ES "+cantidad);
				log.config("CANTIDAD ACTUAL: "+shline.getQtyEntered());
				log.config("Cantidad ingresada real : "+shline.get_Value("QtyUsed"));

				log.config("id "+shline.get_ID());
				if(group != null)
				{
					if(group.compareTo("VIA")==0) //son iguales
						shline.setQtyEntered(zerodecimal);
					if(group.compareTo("UNC")==0) //son iguales 
					{
						String sql2 = "SELECT MinimumQty from c_orderline where c_orderline_id = "+shline.getC_OrderLine_ID();
						pstmt = DB.prepareStatement (sql2, shline.get_TrxName());
						ResultSet rs2 = pstmt.executeQuery ();
						while (rs2.next ())
						{
							MinQty = rs2.getBigDecimal("MinimumQty");
						}
						
						rs2.close ();
						pstmt.close ();
						pstmt = null;
						if(MinQty != null)
						{
							if(MinQty.compareTo(cantidad) == 1)
								shline.setQtyEntered(MinQty);
							else if(MinQty.compareTo(cantidad) == -1)
								shline.setQtyEntered(cantidad);
							else
								shline.setQtyEntered(cantidad);
						}
					}
						
				}
				rs.close();
				
				//mfrojas Traer los datos de tramo, origen y destino desde la OC.
				
				sql = "SELECT C_BPartner_Location2_ID, C_BPartner_Location3_ID,C_BPartner_Location4_ID  from c_orderline where c_orderline_id = "+shline.get_ValueAsInt("c_orderline_id");
				log.config(sql);
				pstmt = null;
				pstmt = DB.prepareStatement(sql,shline.get_TrxName());
				rs = pstmt.executeQuery();
				int loc2=0, loc3=0, loc4=0;
				while (rs.next ())
				{
					loc2 = rs.getInt("C_BPartner_Location2_ID");
					loc3 = rs.getInt("C_BPartner_Location3_ID");
					
					loc4 = rs.getInt("C_BPartner_Location4_ID");
					
				}
				if(loc2 != 0)
					shline.set_CustomColumn("C_BPartner_Location2_ID", loc2);
				if(loc3 != 0)
				{
					shline.set_CustomColumn("C_BPartner_Location3_ID", loc3);
					//MFROJAS
					//Agregar acá el campo de monto de comisión asociado a c_bpartner_location3_ID
					
					//Primero, se verá si el conductor es de Transportes Callegari, o es de una empresa contratista.
					int bpartnerref = 0;
					bpartnerref = shline.get_ValueAsInt("C_BPartner_ID");

					String sqlemployer = "SELECT C_BPartnerRef_ID FROM C_BP_Employer where c_bpartner_id = "+bpartnerref+ "" +
							" AND DateStart <= '"+shline.getM_InOut().getDateAcct()+"' AND EndDate >= '"+shline.getM_InOut().getDateAcct()+"'";
					
					int bpartneremp = DB.getSQLValue(shline.get_TrxName(), sqlemployer);

					
					String sqlcomision = "SELECT AssignmentAmount FROM C_BPartner_Location where C_BPartner_Location_ID = ?";
					BigDecimal comision = DB.getSQLValueBD(shline.get_TrxName(), sqlcomision, loc3);
					log.config("comision "+comision);
					log.config("bpartneremp "+bpartneremp);
					if(comision.compareTo(Env.ZERO)>=0)
					{
						//Si el empleado es de TC, su comision esta asociada al tramo
						if(bpartneremp == 1002238)
						{
							shline.set_CustomColumn("AssignmentAmount",comision);
						}
						//si el empleado no es de tc, entonces se busca el porcentaje asociado al fletero, 
						//y ese valor se multiplica por el monto del viaje (en la linea de entrega).
						else if(bpartneremp > 0)
						{
							BigDecimal half = new BigDecimal(0.5);
							BigDecimal threequarter = new BigDecimal(0.75);
							String sqlpercent = "SELECT percentcomission from c_bpartner where c_bpartner_id = ? ";
							String val = DB.getSQLValueString(shline.get_TrxName(), sqlpercent, bpartnerref);
							comision = new BigDecimal(shline.get_ValueAsInt("PriceActual"));
							log.config("val "+val);
							log.config("comision "+comision);
							
							if(val.compareTo("01")==0)
							{
								log.config("comision "+comision);
								shline.set_CustomColumn("AssignmentAmount",comision);

							}
							else if(val.compareTo("02")==0)
							{
								log.config("comision "+comision);
								comision = comision.multiply(threequarter);
								log.config("comision "+comision);

								shline.set_CustomColumn("AssignmentAmount",comision);

							}
							else if(val.compareTo("03")==0)
							{
								comision = comision.multiply(half);
								shline.set_CustomColumn("AssignmentAmount",comision);

							}
						}
					}
				}
				if(loc4 != 0)
					shline.set_CustomColumn("C_BPartner_Location4_ID", loc4);
				
				//validar si existe diario de efectivo. En caso de que exista, entonces se copiará el monto en la linea correspondiente.
				int idcash = 0;
				idcash = shline.get_ValueAsInt("C_Cash_ID");
				log.config("idcash "+ idcash);
				if(idcash != 0)
				{
					sql = "SELECT EndingBalance from c_cash where c_cash_id = "+idcash;
					pstmt = null;
					pstmt = DB.prepareStatement(sql,shline.get_TrxName());
					rs = pstmt.executeQuery();
					BigDecimal montoporrendir = new  BigDecimal (0);
					while (rs.next())
					{
						montoporrendir = rs.getBigDecimal("EndingBalance");
					}
					
					if(montoporrendir.compareTo(Env.ZERO) == 1)
						shline.set_CustomColumn("Amount", montoporrendir);
				}
				
				
				//Selección de Conductor. Al momento de seleccionarlo, se debe llenar el tracto y el termo (Camión y acoplado). 
				//Esto se hará en base al grupo de socio de negocio y una marca que tendrá este mismo grupo.
				
				int bpartner = 0;
				bpartner = shline.get_ValueAsInt("C_BPartner_ID");
				log.config("conductor: "+bpartner);
				
				int tracto = 0;
				int remolque = 0;
				
				if(bpartner != 0)
				{
					sql = "SELECT max(A_Asset_ID) as a_Asset_id from a_Asset where c_bpartner_id = "+bpartner+" and a_Asset_Group_id in (select a_Asset_group_id from a_Asset_Group where assettype = 'CAM')";
					pstmt = null;
					pstmt = DB.prepareStatement(sql,shline.get_TrxName());
					rs = pstmt.executeQuery();
					while (rs.next())
					{
						tracto = rs.getInt("A_Asset_ID");
					}
					
					if(tracto != 0)
						shline.set_CustomColumn("A_Asset2_ID", tracto);
				

					rs.close();
					
					sql = "SELECT max(A_Asset_ID) as a_asset_Id from a_Asset where c_bpartner_id = "+bpartner+" and a_Asset_Group_id in (select a_Asset_group_id from a_Asset_Group where assettype = 'TER')";
					pstmt = null;
					pstmt = DB.prepareStatement(sql,shline.get_TrxName());
					rs = pstmt.executeQuery();
					while (rs.next())
					{
						remolque = rs.getInt("A_Asset_ID");
					}
					
					if(remolque != 0)
						shline.set_CustomColumn("A_Asset3_ID", remolque);

					rs.close();
					pstmt.close();
					pstmt= null;
				}
				else
				{
					shline.set_CustomColumn("A_Asset3_ID", 0);
					shline.set_CustomColumn("A_Asset2_ID", 0);
				}
				
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
			
		}
		return null;
	}	//	modelChange

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


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString


	

}	