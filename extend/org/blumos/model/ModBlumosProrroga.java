package org.blumos.model;

import java.sql.Timestamp;

import org.compiere.model.MClient;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
/**
 *	Validator for company blumos
 *
 *  @author ininoles
 */
public class ModBlumosProrroga implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosProrroga ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosProrroga.class);
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
		engine.addModelChange(MPayment.Table_Name, this);
		//	Documents to be monitored
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MPayment.Table_ID
				&& po.is_ValueChanged("Prorrogar"))  
		{
			MPayment pay = (MPayment) po;
			if(pay.get_ValueAsBoolean("Prorrogar"))
			{
				Timestamp today = new Timestamp(System.currentTimeMillis());
				String p_proroga = "";
				if(pay.get_ValueAsString("prorroga_historial") != null && 
						pay.get_ValueAsString("prorroga_historial").length() > 0)
					p_proroga = "\n";
				
				pay.set_CustomColumn("PRORROGA_HISTORIAL", 
						p_proroga+"Vcto. Anterior: "+pay.getDateTrx()+" - Motivo: "+pay.get_ValueAsString("razon_prorroga")+
						pay.get_ValueAsString("PRORROGA_HISTORIAL")+"  - Usuario: "+pay.getUpdatedBy()+" - "+today+" ");
				pay.set_CustomColumn("prorrogado", true);
				pay.set_CustomColumn("razon_prorroga", "");
				pay.set_CustomColumn("prorrogar", false);
				pay.setDateTrx((Timestamp)pay.get_Value("fecha_prorroga"));
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