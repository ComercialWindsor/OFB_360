package org.blumos.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.model.MClient;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrg;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company blumos
 *
 *  @author ininoles
 */
public class ModBlumosValidaStockLine implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosValidaStockLine ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosValidaStockLine.class);
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
		//	Documents to be monitored
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MOrderLine.Table_ID
				&& (po.is_ValueChanged("qtyentered") || po.is_ValueChanged("priceentered")))  
		{
			MOrderLine oLine = (MOrderLine) po;
			if(oLine.getParent().isSOTrx() && oLine.getAD_Client_ID() != 1000005)
			{
				Timestamp dateCalc = new Timestamp(System.currentTimeMillis());
				dateCalc.setHours(0);
				dateCalc.setMinutes(0);
				dateCalc.setSeconds(0);
				dateCalc.setNanos(0);
				dateCalc.setYear(116);
				dateCalc.setMonth(2);
				dateCalc.setDate(6);
				//obtener locator 
				int ID_locator = DB.getSQLValue(po.get_TrxName(), "SELECT m.m_locator_id FROM m_locator m" +
						" WHERE m.M_WAREHOUSE_ID = ?  and m.ISACTIVE='Y'", oLine.getParent().getM_Warehouse_ID());
				//obtener cantidad en bodega				
				BigDecimal qtyOnHand = DB.getSQLValueBD(po.get_TrxName(),  "SELECT sum(st.qtyonhand) FROM m_storage st" +
						" WHERE st.m_product_ID = ? and st.M_LOCATOR_ID="+ID_locator, oLine.getParent().getM_Warehouse_ID());
				//obtener saldo de bodega X
				BigDecimal qtyAva = DB.getSQLValueBD(po.get_TrxName(), "SELECT sum(st.qtyonhand)-sum(st.qtyreserved) FROM m_storage st" +
						" WHERE st.m_product_ID=? and st.M_LOCATOR_ID="+ID_locator, oLine.getParent().getM_Warehouse_ID());
				//obtengo max de pricelist_version que corresponde a la cabecera
				int ID_PLVersion = DB.getSQLValue(po.get_TrxName(), "SELECT max(M_pricelist_Version_ID) as plvid FROM M_Pricelist_Version " +
						" WHERE m_pricelist_ID = "+oLine.getParent().getM_PriceList_ID()+" AND M_Pricelist_version.ISACTIVE='Y'");
				//obtengo precio puesto en plv de la cabecera
				BigDecimal price = DB.getSQLValueBD(po.get_TrxName(), "SELECT pricelist FROM M_ProductPrice" +
						" WHERE M_PRICELIST_VERSION_ID="+ID_PLVersion+" AND m_product_id=?", oLine.getM_Product_ID());
				if(oLine.getM_Product().getProductType().compareTo("S") == 0)
				{
					oLine.set_CustomColumn("qty", Env.ZERO);
				}
				else
				{
					//control version LP
					
					if(price.compareTo(oLine.getPriceEntered()) >= 0 
						&& oLine.getParent().getDateOrdered().compareTo(dateCalc) >= 0)
					{
						oLine.set_CustomColumn("qty", Env.ONE.add(Env.ONE));
						if(oLine.getAD_Client_ID() == 1000006)
								oLine.setDescription("Error en la Lista de precios: El precio ofrecido por el sistema no es correcto. Elimine esta linea e intente de nuevo");						
					}		
					
					else
					{
						if(qtyOnHand.compareTo(oLine.getQtyEntered()) >= 0 
								|| oLine.getParent().getDateOrdered().compareTo(dateCalc) <= 0)
							oLine.set_CustomColumn("qty", Env.ZERO);
						else
						{
							oLine.setDescription("Stock Insuficiente : stock actual :"+qtyAva.toString());
							oLine.set_CustomColumn("qty", Env.ZERO);//ponemos cero para no controlar estock segun correo de ccampbell del 2/12/2016
						}
					}
				}
				oLine.set_CustomColumn("info_precios", BlumosUtilities.DameUltimosPrecios(oLine.getM_Product_ID(),oLine.getParent().getC_BPartner_ID(), po.getCtx(), po.get_TrxName()));
				String codBlumos = oLine.getM_Product().getName().substring(0, 4);
				int ID_vendedorCartera = BlumosUtilities.DameIDVendCartera(codBlumos, oLine.getParent().getC_BPartner_ID(), oLine.getParent().getC_BPartner_Location_ID(), po.getCtx(), po.get_TrxName());
				if(oLine.getParent().getSalesRep_ID() != ID_vendedorCartera || ID_vendedorCartera < 1)
				{
					oLine.set_CustomColumn("INFO_CARTERA","PRODUCTO NO HALLADO EN CARTERA, ESCOGIO PRODUCTO:"+codBlumos+" ID_VENDEDOR:"+oLine.getParent().getSalesRep_ID()+" ID_VENDEDOR_CARTERA:"+ID_vendedorCartera);
					oLine.set_CustomColumn("qty", Env.ONE);
				}
				else
					oLine.set_CustomColumn("INFO_CARTERA","CARTERA ok,, ESCOGIO PRODUCTO:"+codBlumos+" ID_VENDEDOR:"+oLine.getParent().getSalesRep_ID()+" ID_VENDEDOR_CARTERA:"+ID_vendedorCartera);
				String nomOrgCartera = ""; 
				int ID_OrgCartera = BlumosUtilities.DameIDOrgCartera(codBlumos, oLine.getParent().getC_BPartner_ID(),oLine.getParent().getC_BPartner_Location_ID(),0, po.getCtx(), po.get_TrxName());
				if(ID_OrgCartera > 0)
					nomOrgCartera = DB.getSQLValueString(po.get_TrxName(), "SELECT name FROM ad_org WHERE ad_org_id=?",ID_OrgCartera);
				else
					nomOrgCartera = "NO EXISTE ASIGNACION DE AREA EN CARTERA PARA ESTE PRODUCTO, CLIENTE, PLANTA ";
				if((oLine.getParent().getAD_Org_ID() != ID_OrgCartera 
					|| ID_OrgCartera < 1 ) && oLine.getParent().getDateOrdered().compareTo(dateCalc) > 0)
				{
					MOrg orgCab = new MOrg(po.getCtx(), oLine.getParent().getAD_Org_ID(), po.get_TrxName());
					oLine.set_CustomColumn("INFO_CARTERA","EL AREA DE NEGOCIOS SELECCIONADO EN ESTA ORDEN NO COINCIDE CON CARTERA, ESCOGIO AREA:"+orgCab.getName()+
							", LA CARTERA INDICA :"+nomOrgCartera);
					oLine.set_CustomColumn("qty", Env.ONE);
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