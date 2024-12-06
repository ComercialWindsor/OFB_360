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
package org.mutual.model;

import java.math.BigDecimal;


import org.compiere.model.MClient;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	Validator for company Sismode
 *
 *  @author Julio Farias
 */
public class ModMutualUpdateAmt implements ModelValidator 
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModMutualUpdateAmt ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModMutualUpdateAmt.class);
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
		engine.addModelChange(MOrderLine.Table_Name, this);
			

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Italo Niñoles
     */
	
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==MOrderLine.Table_ID)
		{		
			MOrderLine oLine = (MOrderLine) po;
			String descValid = oLine.getDescription();
			if(descValid == null)
				descValid = ".";
			if(oLine.getC_Order().getC_BPartner().getM_PriceList_ID() > 0)
				;
			else
			{
				int estaenbom = 0;
				MProductCategory cat  = null;
				if(oLine.getM_Product_ID() > 0)
					cat = new MProductCategory(po.getCtx(), oLine.getM_Product().getM_Product_Category_ID(), po.get_TrxName());
				if(oLine.getM_Product_ID() > 0 && (oLine.getDescription() == null || oLine.getDescription() == ""))
				{
					//Inicialmente se actualiza el precio unitario.
					
					int versionlista = oLine.get_ValueAsInt("M_PriceList_Version_ID");
					String sqlprecio = "Select coalesce(pricestd,0) from m_productprice where m_pricelist_Version_id = " +versionlista+" and m_product_id = "+oLine.getM_Product_ID() ;
					log.config("sqlprecio "+sqlprecio);
					int precio = DB.getSQLValue( po.get_TrxName(),sqlprecio);
					log.config("precio: "+precio);
					//@mfrojas consulta para saber si el producto en el que estoy parado es bom o no
					//Esto se realiza pues, si el producto no es bom, pero se encuentra en alguna de las baterías de la orden, su precio debe ser cero.
					
					String sqlesbom = "SELECT case when mp.isverified = 'Y' then 1 else 0 end from m_product mp where mp.m_product_id = "+oLine.getM_Product_ID();
					log.config("sql para ver si el prod es bom: "+sqlesbom);
					int esbom = DB.getSQLValue(po.get_TrxName(), sqlesbom);
					log.config("es bom: "+esbom);
					//@mfrojas consulta para saber si el examen adicional está o no en los bom anteriores (ya se sabe que cant > 0, por lo que hay bom)
					
					if(esbom == 0)
					{	
							String sqlbuscaenbom = "SELECT count(1) FROM M_ProductPrice pp " +
									" INNER JOIN M_Product mp ON (pp.M_Product_ID = mp.M_Product_ID) " +
									" INNER JOIN M_PriceList_Version plv ON (plv.M_PriceList_Version_ID = pp.M_PriceList_Version_ID) " +
									" WHERE plv.M_Pricelist_Version_ID = " + oLine.get_ValueAsInt("M_Pricelist_Version_ID")+" AND mp.M_Product_ID IN ( SELECT pbl.M_Product_ID FROM M_Product mp" +
									" INNER JOIN C_OrderLine col ON (col.M_Product_ID = mp.M_Product_ID) " +
									" INNER JOIN PP_Product_BOM pb ON (mp.M_Product_ID = pb.M_Product_ID) " +
									" INNER JOIN PP_Product_BOMLine pbl ON (pbl.PP_Product_BOM_ID = pb.PP_Product_BOM_ID) " +
									" WHERE  col.C_Order_ID = " + oLine.getC_Order_ID() + "AND col.line < "+oLine.getLine()+") " +
									" AND mp.M_Product_ID = "+oLine.getM_Product_ID();
							log.config("sqlbuscaenbom "+sqlbuscaenbom);
							estaenbom = DB.getSQLValue(po.get_TrxName(), sqlbuscaenbom);
							log.config("estaenbom: "+estaenbom);
							if(estaenbom > 0)
								precio = 0; 
							log.config("precio despues del bom: "+precio);
					}
					
					
					String sqlprecioupdate = "Update c_orderline set priceentered = "+precio+", priceactual = "+precio+", linenetamt = "+precio+"*qtyordered where c_orderline_id = "+oLine.get_ID();
					log.config("sqlprecioupdate  "+sqlprecioupdate); 
					//oLine.setPrice(precio);
					if(precio > 0)
						DB.executeUpdate(sqlprecioupdate.toString(), po.get_TrxName());
					String actualizagrandtotal1 = "Update c_order set grandtotal = (select sum(linenetamt) from c_orderline where c_order_id ="+oLine.getC_Order_ID()+"), totallines = (select sum(linenetamt) from c_orderline where c_order_id ="+oLine.getC_Order_ID()+") where c_order_id = "+oLine.getC_Order_ID();
					log.config("c_order : "+actualizagrandtotal1);
					DB.executeUpdate(actualizagrandtotal1, po.get_TrxName());
					//commit();
				}
				if (oLine.getM_Product_ID() > 0 && oLine.getM_Product().isVerified())
				{
					//@mfrojas consulta para saber si el producto en el que estoy parado es bom o no
					//Esto se realiza pues, si el producto no es bom, pero se encuentra en alguna de las baterías de la orden, su precio debe ser cero.
					
					String sqlesbom = "SELECT case when mp.isverified = 'Y' then 1 else 0 end from m_product mp where mp.m_product_id = "+oLine.getM_Product_ID();
					log.config("sql para ver si el prod es bom: "+sqlesbom);
					int esbom = DB.getSQLValue(po.get_TrxName(), sqlesbom);
					log.config("es bom: "+esbom);
					
					//vemos si existe una linea anterior con bom
					String sql = "SELECT COUNT(1) FROM C_OrderLine col" +
							" INNER JOIN M_Product mp ON (col.M_Product_ID = mp.M_Product_ID) " +
							" WHERE C_Order_ID = "+oLine.getC_Order_ID()+" AND line < "+oLine.getLine()+"" +
							" AND mp.isverified = 'Y'";
					log.config("sql para ver si hay bom: "+sql);
					int cant = DB.getSQLValue(po.get_TrxName(), sql);
					log.config("Cantidad de boms: "+cant);
					MProduct prod = new MProduct(po.getCtx(), oLine.getM_Product_ID(), po.get_TrxName());
					if(cant > 0)//si existe bom buscamos monto a descontar
					{
		/*				String StrAmt = "SELECT SUM (pp.PriceStd) FROM M_ProductPrice pp " +
								" INNER JOIN M_Product mp ON (pp.M_Product_ID = mp.M_Product_ID) " +
								" INNER JOIN M_PriceList_Version plv ON (plv.M_PriceList_Version_ID = pp.M_PriceList_Version_ID) " +
								" WHERE plv.IsBOM = 'Y' AND mp.M_Product_ID IN ( SELECT pbl.M_Product_ID FROM M_Product mp" +
								" INNER JOIN C_OrderLine col ON (col.M_Product_ID = mp.M_Product_ID) " +
								" INNER JOIN PP_Product_BOM pb ON (mp.M_Product_ID = pb.M_Product_ID) " +
								" INNER JOIN PP_Product_BOMLine pbl ON (pbl.PP_Product_BOM_ID = pb.PP_Product_BOM_ID) " +
								" WHERE  col.C_Order_ID = " + oLine.getC_Order_ID() + "AND col.line < "+oLine.getLine()+") " +
								" AND mp.M_Product_ID IN (SELECT pbl.M_Product_ID FROM M_Product mp " +
								" INNER JOIN C_OrderLine col ON (col.M_Product_ID = mp.M_Product_ID) " +
								" INNER JOIN PP_Product_BOM pb ON (mp.M_Product_ID = pb.M_Product_ID) " +
								" INNER JOIN PP_Product_BOMLine pbl ON (pbl.PP_Product_BOM_ID = pb.PP_Product_BOM_ID) " +
								" WHERE  col.C_Order_ID = " + oLine.getC_Order_ID() + " AND col.line = "+oLine.getLine()+")";
								*/
						
						//Se modifica más la consulta. Primero fue por la versión de lista de precios que se obtuvo desde la línea. Luego se van agregando los
						//atributos, pues se debe diferenciar entre baterías de uno u otro usuario
						String StrAmt = "SELECT coalesce(SUM(pp.PriceStd),0) FROM M_ProductPrice pp " +
								" INNER JOIN M_Product mp ON (pp.M_Product_ID = mp.M_Product_ID) " +
								" INNER JOIN M_PriceList_Version plv ON (plv.M_PriceList_Version_ID = pp.M_PriceList_Version_ID) " + //se agrega join con m_attributesetinstance
								" INNER JOIN M_AttributesetInstance mat on (mat.M_AttributeSetInstance_ID = "+oLine.getM_AttributeSetInstance_ID()+")"+
								" INNER JOIN M_AttributeInstance ma on (ma.M_AttributeSetInstance_ID = mat.M_AttributeSetInstance_ID)"+
								" WHERE ma.M_Attribute_ID = 1000009 "+
								" AND plv.M_Pricelist_Version_ID = " + oLine.get_ValueAsInt("M_Pricelist_Version_ID")+" AND mp.M_Product_ID IN ( SELECT pbl.M_Product_ID FROM M_Product mp" +
								" INNER JOIN C_OrderLine col ON (col.M_Product_ID = mp.M_Product_ID) " +
								" INNER JOIN PP_Product_BOM pb ON (mp.M_Product_ID = pb.M_Product_ID) " +
								" INNER JOIN PP_Product_BOMLine pbl ON (pbl.PP_Product_BOM_ID = pb.PP_Product_BOM_ID) " +
								" WHERE  col.C_Order_ID = " + oLine.getC_Order_ID() + "AND col.line < "+oLine.getLine()+" AND col.m_attributesetinstance_id in ( "+
								" SELECT m_attributesetinstance_id from m_Attributeinstance where m_Attribute_id = 1000009 and value = ma.value )) " +
								" AND mp.M_Product_ID IN (SELECT pbl.M_Product_ID FROM M_Product mp " +
								" INNER JOIN C_OrderLine col ON (col.M_Product_ID = mp.M_Product_ID) " +
								" INNER JOIN PP_Product_BOM pb ON (mp.M_Product_ID = pb.M_Product_ID) " +
								" INNER JOIN PP_Product_BOMLine pbl ON (pbl.PP_Product_BOM_ID = pb.PP_Product_BOM_ID) " +
								" WHERE  col.C_Order_ID = " + oLine.getC_Order_ID() + " AND col.line = "+oLine.getLine()+")";
						
						BigDecimal amt = DB.getSQLValueBD(po.get_TrxName(), StrAmt);	
						log.config("StrAmt: "+StrAmt);
						
						log.config("AMT: "+amt);
						log.config("RESULTADO: "+oLine.getPriceEntered().subtract(amt));
						
						//@mfrojas Se agrega codigo para validar si es que la persona es menor a 40. 
						//Esto consiste en un select que deberá sumar los productos de la batería que 
						//son menores a 40, sin contar los que ya dejó afuera. 
						BigDecimal amtMenoresA40 = Env.ZERO;					
						if(prod.get_ValueAsBoolean("IsDiscount40"))
						{
							String sqlMenoresA40 = "SELECT coalesce(SUM(pp.PriceStd),0) FROM M_ProductPrice pp " +
							" INNER JOIN M_Product mp ON (pp.M_Product_ID = mp.M_Product_ID) " +
							" WHERE IsDiscount40 = 'Y' and pp.M_Pricelist_Version_ID = "+oLine.get_ValueAsInt("M_Pricelist_Version_ID") +
							" AND mp.M_Product_ID IN ( SELECT pbl.M_Product_ID FROM M_Product mp" +
							" INNER JOIN C_OrderLine col ON (col.M_Product_ID = mp.M_Product_ID)" +
							" INNER JOIN PP_Product_BOM pb ON (mp.M_Product_ID = pb.M_Product_ID)" +
							" INNER JOIN PP_Product_BOMLine pbl ON (pbl.PP_Product_BOM_ID = pb.PP_Product_BOM_ID)" +
							" WHERE  col.C_OrderLine_ID = "+oLine.get_ID()+") AND mp.M_Product_ID not in ("+
							" SELECT pbl.M_Product_ID FROM M_Product mp" +
							" INNER JOIN C_OrderLine col ON (col.M_Product_ID = mp.M_Product_ID) " +
							" INNER JOIN PP_Product_BOM pb ON (mp.M_Product_ID = pb.M_Product_ID) " +
							" INNER JOIN PP_Product_BOMLine pbl ON (pbl.PP_Product_BOM_ID = pb.PP_Product_BOM_ID) " +
							" WHERE  col.C_Order_ID = " + oLine.getC_Order_ID() + "AND col.line < "+oLine.getLine()+" AND col.M_AttributeSetInstance_ID in ("+
							" SELECT M_AttributesetInstance_id from m_Attributeinstance where m_Attribute_id = 1000009 and value in "+
							" (SELECT value from m_Attributeinstance where m_attribute_id = 1000009 and m_attributesetinstance_id = "+oLine.get_ValueAsInt("M_AttributeSetInstance_ID")+")))";

							if(oLine.get_ValueAsInt("Anios") < 40)
								amtMenoresA40 = DB.getSQLValueBD(po.get_TrxName(), sqlMenoresA40);
							log.config("sqlMenoresA40: "+sqlMenoresA40);
							log.config("monto menores a 40: "+amtMenoresA40);
							log.config("Diferencia: "+oLine.getPriceEntered().subtract(amt).subtract(amtMenoresA40));
						}						
						if (oLine.getDescription() == null)
							oLine.setDescription("");
						log.config("preguntamos por descripcion: "+oLine.getDescription());
						if(!oLine.getDescription().toLowerCase().contains("precio actualizado automatico") && oLine.getLine() < 1000)
						{
							log.config("entra en if de descripcion");
							//@mfrojas se agrega al if el subtract(amtmenoresa40)						
							if (oLine.getPriceEntered().subtract(amt).subtract(amtMenoresA40).compareTo(Env.ZERO) > 0)
							{
								log.config("if seteo de monto mayor a 0: "+oLine.getPriceEntered().subtract(amt).subtract(amtMenoresA40));
								oLine.setPrice(oLine.getPriceEntered().subtract(amt).subtract(amtMenoresA40));
							}
							else//seteo de monto en 0 debe ser hecho por DB
							{
								log.config("seteo de monto en 0");//se setea en 1 nomas
								oLine.setPrice(Env.ZERO);
								oLine.setPriceList(Env.ZERO);
								/* 
								String sqlPrUpLine = "Update c_orderline set priceentered = 0, priceactual = 0, linenetamt = 0 where c_orderline_id = "+oLine.get_ID();
								log.config("sqlprecioupdate  "+sqlPrUpLine); 
								DB.executeUpdate(sqlPrUpLine.toString(), po.get_TrxName());
								String sqlPrUpLine2 = "Update c_order set grandtotal = (select sum(linenetamt) from c_orderline where c_order_id ="+oLine.getC_Order_ID()+"), totallines = (select sum(linenetamt) from c_orderline where c_order_id ="+oLine.getC_Order_ID()+") where c_order_id = "+oLine.getC_Order_ID();
								log.config("c_order : "+sqlPrUpLine2);
								DB.executeUpdate(sqlPrUpLine2, po.get_TrxName());*/
							}				
							oLine.setDescription(oLine.getDescription()+" Precio Actualizado Automatico");
							oLine.setLineNetAmt();
							oLine.save();
						}
					}
					else
					{
						if(oLine.get_ValueAsInt("Anios") < 40 && prod.get_ValueAsBoolean("IsDiscount40"))
						{
							String StrAmt = "SELECT SUM (pp.PriceStd) FROM M_ProductPrice pp " +
									" INNER JOIN M_Product mp ON (pp.M_Product_ID = mp.M_Product_ID) " +
									" WHERE IsDiscount40 = 'Y' and pp.M_Pricelist_Version_ID = "+oLine.get_ValueAsInt("M_Pricelist_Version_ID") +
									" AND mp.M_Product_ID IN ( SELECT pbl.M_Product_ID FROM M_Product mp" +
									" INNER JOIN C_OrderLine col ON (col.M_Product_ID = mp.M_Product_ID)" +
									" INNER JOIN PP_Product_BOM pb ON (mp.M_Product_ID = pb.M_Product_ID)" +
									" INNER JOIN PP_Product_BOMLine pbl ON (pbl.PP_Product_BOM_ID = pb.PP_Product_BOM_ID)" +
									" WHERE  col.C_OrderLine_ID = "+oLine.get_ID()+") ";
							BigDecimal amt = DB.getSQLValueBD(po.get_TrxName(), StrAmt);
							log.config("sqlMenoresA40: "+StrAmt);
							log.config("monto menores a 40: "+amt);
							if (amt != null)
								log.config("Diferencia: "+oLine.getPriceEntered().subtract(amt));
							
							if(amt != null && amt.compareTo(Env.ZERO) > 0)
							{
								if (oLine.getDescription() == null)
									oLine.setDescription("");
								if(!oLine.getDescription().toLowerCase().contains("descuento edad") && oLine.getLine() < 1000)
								{
									if (oLine.getPriceEntered().subtract(amt).compareTo(Env.ZERO) > 0)
										oLine.setPrice(oLine.getPriceEntered().subtract(amt));
									else
									{
										//oLine.setPrice(Env.ZERO);
										log.config("seteo de monto en 0");//se setea en 1 nomas
										oLine.setPrice(Env.ZERO);
										oLine.setPriceList(Env.ZERO);
									}
									oLine.setDescription(oLine.getDescription()+" Descuento Edad");
									oLine.setLineNetAmt();
									oLine.save();
								}
							}
						}
					}
				}
				else
				{
					//MProductCategory cat = new MProductCategory(po.getCtx(), oLine.getM_Product().getM_Product_Category_ID(), po.get_TrxName());
					if(cat.get_ValueAsBoolean("DrugPrice"))//sustancias
					{
						//buscamos sustancias anteriores
						//nuevo modelo de drogas con catidad en qtyordered
						/*int cant = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine col " +
								"INNER JOIN M_product mp ON(col.M_product_ID = mp.M_product_ID) " +
								"INNER JOIN M_Product_Category pcp ON (pcp.M_Product_Category_ID = mp.M_Product_Category_ID) " +
								"WHERE  col.C_Order_ID = "+oLine.getC_Order_ID()+" AND col.line < "+oLine.getLine()+" AND pcp.DrugPrice = 'Y'");
						*/
						int cant = oLine.getQtyEntered().intValue();
						//cant = cant+1;
						//seteamos nivel
						String valueNivel = "0"+cant;
						
						BigDecimal amtDrug = DB.getSQLValueBD(po.get_TrxName(), "SELECT Price FROM M_ProductCategoryPrice pcp " +
								"WHERE  M_Product_Category_ID = "+cat.get_ID()+" AND LevelPrice = '"+valueNivel+"'");
						
						if (amtDrug.compareTo(oLine.getPriceEntered())!= 0)
						//if(!oLine.getDescription().toLowerCase().contains("precio actualizado automatico"))
						{
							oLine.setPrice(amtDrug);						
							oLine.setLineNetAmt();
							oLine.setDescription(oLine.getDescription()==null?" Precio Actualizado Automatico":oLine.getDescription()+ " Precio Actualizado Automatico");
							oLine.save();
						}
					}
					else//si no es bom y no es droga buscar precio no bateria
					{
						//creamos version de lista de precios para usar misma version pero sin BOM
						//MPriceListVersion pListVersion = new MPriceListVersion(po.getCtx(), oLine.get_ValueAsInt("M_Pricelist_Version_ID"), po.get_TrxName());
						
						/*String StrAmt = "SELECT MAX (pp.PriceStd) FROM M_ProductPrice pp " +
						" INNER JOIN M_Product mp ON (pp.M_Product_ID = mp.M_Product_ID) " +
						" INNER JOIN M_PriceList_Version plv ON (plv.M_PriceList_Version_ID = pp.M_PriceList_Version_ID) " +
						" WHERE plv.IsBOM = 'N' AND mp.M_Product_ID  = "+oLine.getM_Product_ID()+
						" AND plv.MU_Servicio = '"+pListVersion.get_ValueAsString("MU_Servicio")+"'"+
						" AND plv.MU_Adherente = '"+pListVersion.get_ValueAsString("MU_Adherente")+"'";*/
						
						//@mfrojas nueva forma de obtener el precio
						String StrAmt = "SELECT MAX (pp.PriceStd) FROM M_ProductPrice pp " +
								" INNER JOIN M_Product mp ON (pp.M_Product_ID = mp.M_Product_ID) " +
								" INNER JOIN M_PriceList_Version plv ON (plv.M_PriceList_Version_ID = pp.M_PriceList_Version_ID) " +
								" WHERE plv.M_PriceList_Version_ID = "+oLine.get_ValueAsInt("M_PriceList_Version_ID") +"AND mp.M_Product_ID  = "+oLine.getM_Product_ID();
								
						BigDecimal amt = DB.getSQLValueBD(po.get_TrxName(), StrAmt);
						log.config("amt es : "+amt);
						if (oLine.getDescription() == null)
							oLine.setDescription("");
						if(amt == null || amt.compareTo(Env.ZERO) == 0)
							amt = Env.ZERO;
						if(estaenbom > 0)
						{
							//amt = Env.ZERO;//precios no pueden ser 0
							amt = Env.ZERO;
						}
						if(!oLine.getDescription().toLowerCase().contains("precio actualizado automatico") && oLine.getLine() < 1000)
						{
							if(estaenbom > 0)
								oLine.setPrice(0);
							else
								oLine.setPrice(amt);
							oLine.setDescription(oLine.getDescription()+" Precio Actualizado Automatico");
							oLine.setLineNetAmt();
							oLine.save();
						}
					}
				}
				//aumento de 50%
				boolean IsIncrease = false;
				try {				
					IsIncrease =oLine.get_ValueAsBoolean("IsIncrease50");
				} catch (Exception e) {
					IsIncrease = false;
				}
				if(IsIncrease)
				{
					if(!descValid.toLowerCase().contains("aumento inhabil"))
					{
						oLine.setPrice(oLine.getPriceEntered().multiply(new BigDecimal("1.5")));						
						oLine.setDescription(descValid+" Aumento Inhabil 50");
						oLine.setLineNetAmt();
						oLine.save();
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