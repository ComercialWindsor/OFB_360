package org.blumos.model;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MRole;
import org.compiere.model.MUser;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
//import org.ofb.utils.DateUtils;

/**
 *	Validator for company blumos
 *
 *  @author ininoles
 */
public class ModBlumosCOrder implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosCOrder ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosCOrder.class);
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
		//engine.addModelChange(MOrder.Table_Name, this);
		//	Documents to be monitored
		engine.addDocValidate(MOrder.Table_Name, this);
		
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		//CAMBIOS SACADOS DE TRIGGER PRIORIDADES
		/*if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MOrder.Table_ID 
				&& po.is_ValueChanged("ISAPPROVED"))  
		{
			MOrder order = (MOrder) po;
			if(order.get_ValueAsBoolean("ISAPPROVED") && order.getDocStatus().compareTo("DR") ==0)
			{
				if(((DateUtils.now().getHours() == 9 && DateUtils.now().getMinutes() > 0)
						|| DateUtils.now().getHours() > 9)
					&& DateUtils.now().getHours() < 15)
				{
					if(order.getDeliveryViaRule().compareTo("V")==0)
						order.setPriorityRule("3");						
					else
						order.setPriorityRule("1");						
				}
				else
				{
					if(order.getDeliveryViaRule().compareTo("V")==0)
						order.setPriorityRule("1");						
					else
						order.setPriorityRule("3");	
				}
			}	
		}*/
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
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MOrder.Table_ID)  
		{
			MUser user = new MUser(po.getCtx(), Env.getAD_User_ID(po.getCtx()), po.get_TrxName());
			MRole role = new MRole(po.getCtx(), Env.getAD_Role_ID(po.getCtx()), po.get_TrxName());
			MOrder order = (MOrder) po;
			if(!order.isSOTrx() && order.getC_DocType_ID() == 1000052)
			{
				int v_alarma_id = DB.getSQLValue(po.get_TrxName(),"select damealarma_id(c_bpartner_id,to_char(pais_origen),inco_term,inco_via,container) " +
						"from c_order where c_order_id = "+order.get_ID());
				if(v_alarma_id <=0)
					return "No existe definición de alarma para esta compra. Revise INCO_COUNTRY, INCO_TERM, INCO_VIA y CONTAINER para este proveedor en ventana *Mantención de Alarmas de Importaciones*.";
			}
			if(order.isSOTrx() && order.getC_DocType_ID() == 1000030 
					&& order.getM_Warehouse_ID() == 1000070 && role.get_ID() != 1000153)
			{
				return "ESTA NOTA DE VENTA SOLO PUEDE SER COMPLETADA POR EL DIRECTOR TECNICO (DROGUERIA)";
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


	

	

}	