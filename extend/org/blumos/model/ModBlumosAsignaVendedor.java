package org.blumos.model;

import java.math.BigDecimal;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
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
public class ModBlumosAsignaVendedor implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosAsignaVendedor ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosAsignaVendedor.class);
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
		engine.addModelChange(MOrder.Table_Name, this);
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MOrder.Table_ID)  
		{
			MOrder order = (MOrder) po;
			if(order.isSOTrx())
			{
				//emision desde masivo de NC sin stock
				if(order.getC_DocTypeTarget_ID() == 1000029)
					order.setDeliveryRule(MOrder.DELIVERYRULE_Force);
				//Vendedor en C_Order
				if(Env.getAD_Role_ID(po.getCtx()) == 1000020)
					order.setSalesRep_ID(Env.getAD_User_ID(po.getCtx()));
				//controla Stocks
				BigDecimal qty = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(ol.qty) FROM c_orderline ol" +
						" WHERE ol.c_order_id="+order.get_ID());
				if(qty == null)
					qty = Env.ZERO;
				if(qty.compareTo(Env.ZERO) == 0)
					order.set_CustomColumn("Signature3", true);
				else
					order.set_CustomColumn("Signature3", false);
				
				//ASIGNA PARTNER SOLUTEC SI ES SOLICITUD DE MUESTRA
				if(order.getC_DocTypeTarget_ID() == 1000057)
					order.setBill_BPartner_ID(1001197);
				
				//control de stocks se ejecutara siempre que la NV no este completa o cerrada
				if(order.getDocStatus().compareTo("CO")!=0 && order.getDocStatus().compareTo("CL")!=0)
				{
					if(order.get_ValueAsBoolean("signature1") == false && order.getAD_Client_ID() == 1000000)						
						order.set_CustomColumn("IsSelected", false);
					else
					{
						order.set_CustomColumn("IsSelected", false);
						if(order.getC_DocTypeTarget_ID() == 1000029)//solicitud de nota de credito
							order.set_CustomColumn("IsSelected", true);
						if(order.getC_DocTypeTarget_ID() == 1000030 && 
								qty.compareTo(Env.ZERO)==0)//nota de venta
							order.set_CustomColumn("IsSelected", true);
						if(order.getC_DocTypeTarget_ID() == 1000033)//venta meson
							order.set_CustomColumn("IsSelected", true);
						if(order.getC_DocTypeTarget_ID() == 1000050 && 
								order.getM_Warehouse_ID() != 1000000)//contrato de abastecimiento
							order.set_CustomColumn("IsSelected", true);
						if(order.getC_DocTypeTarget_ID() == 1000054 && 
								qty.compareTo(Env.ZERO)==0)// nota de venta exportacion
							order.set_CustomColumn("IsSelected", true);
						if(order.getC_DocTypeTarget_ID() == 1000056)//solicitud de nota de debito
							order.set_CustomColumn("IsSelected", true);
						if(order.getC_DocTypeTarget_ID() == 1000057 && 
								qty.compareTo(Env.ZERO)==0)// solicitud de muestra
							order.set_CustomColumn("IsSelected", true);
						if(order.getC_DocTypeTarget_ID() == 1000057 && 
								qty.compareTo(Env.ZERO)==0)// solicitud de muestra
							order.set_CustomColumn("IsSelected", true);
						//CONTROLAR SOLO STOCK EN CASO MULTIEMPRESA
						if(order.getAD_Client_ID() != 1000000 && 
								qty.compareTo(Env.ZERO)==0)
							order.set_CustomColumn("IsSelected", true);
					}
				}
			}
			//control de correctas condiciones de regla de pago
			if(order.getC_BPartner().getPaymentRule() != null && order.getPaymentRule() != null)
			{
				if(order.getC_BPartner().getPaymentRule().compareTo(order.getPaymentRule()) != 0 )
					order.setPaymentRule(order.getC_BPartner().getPaymentRule());
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