package org.blumos.model;

import org.compiere.model.MCashLine;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
/**
 *	Validator for company blumos
 *
 *  @author ininoles
 */
public class ModBlumosCashLine implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosCashLine ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosCashLine.class);
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
		engine.addModelChange(MCashLine.Table_Name, this);
		//	Documents to be monitored
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MCashLine.Table_ID )  
		{
			MCashLine cLine = (MCashLine) po;
			if(cLine.isProcessed() == false && cLine.getC_Charge_ID() > 0)
			{
				String an = DB.getSQLValueString(po.get_TrxName(), "SELECT CE.ANALISIS FROM c_elementvalue ce " +
						" INNER JOIN c_validcombination cvc on (ce.c_elementvalue_id=cvc.account_id) " +
						" inner join c_charge_acct cca on (cvc.c_validcombination_id=cca.ch_revenue_acct) " +
						" WHERE cca.c_charge_id="+cLine.getC_Charge_ID());
				Boolean analisis = false; 
				if(an != null && an.compareTo("Y") == 0)
					analisis = true;				
				int ID_BP = cLine.get_ValueAsInt("C_BPartner_ID"); 
				if(ID_BP < 1 && analisis)
					return "ERROR: DEBE INGRESAR UN SOCIO DE NEGOCIO PARA EL CARGO ESCOGIDO";			
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