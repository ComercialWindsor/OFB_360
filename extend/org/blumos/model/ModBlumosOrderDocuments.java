package org.blumos.model;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;

/**
 *	Validator for company blumos
 *
 *  @author ininoles
 */
public class ModBlumosOrderDocuments implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosOrderDocuments ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosOrderDocuments.class);
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
			if(!order.isSOTrx())
			{
				if(order.getC_DocTypeTarget_ID() == 1000052)
				{
					if(order.getDescription() == null 
							|| order.getDescription().compareTo("A") == 0 
							|| order.getDescription() == "")
						order.set_CustomColumn("Description2", "The buyer will not accept products whose remaining " +
								"shelf life is less than 75% of that indicated on the Certificate of Analysis, and/or " +
								"products that belong to more than one production batch.  All packing made of wood , " +
								"including dunnage, should be treated according to ISPM 15 from FAO and marked as " +
								"such in accordance with Annex II of said guideline.  Additionaly, in compliance with " +
								"our local regulations, all packages should weigh 25 kilograms or less. " +
								"This International Purchase Order is computer generated, thus no signature is " +
								"required.");
				}
				else
				{
					if(order.getDescription() == null 
							|| order.getDescription().compareTo("A") == 0 
							|| order.getDescription() == "")
						order.set_CustomColumn("Description2", "El comprador no aceptara productos con vida util menor " +
								"al 75% de lo indicado en el Certificado de Analisis y/o productos que pertenezcan a mas " +
								"de un lote de produccion.  Todo el embalaje de madera, incluso el material de estiba, " +
								"debe ser tratado de acuerdo con las normas de ISPM 15 de la FAO y estar marcado segun el " +
								"Anexo II de la mencionada directriz. Adicionalmente, para cumplir con regulacion local, " +
								"ningun envase debe de exceder los 25 kilogramos.");
				}
				if(order.getAD_Client_ID() == 1000002)
					order.set_CustomColumn("Description2", "The buyer will not accept products whose remaining shelf life " +
							"is less than 75% of that indicated on the Certificate of Analysis, and/or products that belong " +
							"to more than one production batch.  All packing made of wood , including dunnage, should be treated " +
							"according to ISPM 15 from FAO and marked as such in accordance with Annex II of said guideline.  " +
							"This International Purchase Order is computer generated, thus no signature is required.");
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