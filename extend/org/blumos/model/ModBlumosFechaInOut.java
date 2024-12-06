package org.blumos.model;

import java.sql.Timestamp;

import org.compiere.model.MClient;
import org.compiere.model.MInOut;
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
public class ModBlumosFechaInOut implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosFechaInOut ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosFechaInOut.class);
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
		engine.addModelChange(MInOut.Table_Name, this);
		//	Documents to be monitored
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MInOut.Table_ID )  
		{
			MInOut InOut = (MInOut) po;
			//BLOQUEO DE NOTA DE VENTA ANTE LA EXISTENCIA DE ESTE DESPACHO
			if(InOut.getDocStatus().compareTo("VO") != 0 || 
					InOut.getDocStatus().compareTo("RE") != 0)
			{
				DB.executeUpdate("UPDATE C_Order SET isSelected='N' " +
						" WHERE C_Order_id = "+InOut.getC_Order_ID(), po.get_TrxName());
			}
			//Fecha digitada sea igual a la fecha actual			
			Timestamp today = new Timestamp(System.currentTimeMillis());
			today.setHours(0);
			today.setMinutes(0);
			today.setSeconds(0);
			today.setNanos(0);
			Timestamp movDay = InOut.getMovementDate();
			movDay.setHours(0);
			movDay.setMinutes(0);
			movDay.setSeconds(0);
			movDay.setNanos(0);			
			//si es distinta a bodega central, se da por aprobadsa cualquier fecha que sea ingresada
			if(InOut.getM_Warehouse_ID() == 1000000)
			{
				if(movDay.compareTo(today) != 0 && InOut.getDocStatus().compareTo("CO") != 0
						&& InOut.getDocStatus().compareTo("CL") != 0
						&& InOut.getDocStatus().compareTo("VO") != 0)
					InOut.set_CustomColumn("Bandera_Fecha", false);
				else
					InOut.set_CustomColumn("Bandera_Fecha", true);
			}
			else
				InOut.set_CustomColumn("Bandera_Fecha", true);
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
		if(timing == TIMING_BEFORE_PREPARE && po.get_Table_ID()==MInOut.Table_ID )  
		{
			MInOut InOut = (MInOut) po;
			InOut.set_CustomColumn("Bandera_Fecha", false);
			InOut.save(po.get_TrxName());
		}
		if(timing == TIMING_AFTER_VOID && po.get_Table_ID()==MInOut.Table_ID )  
		{
			MInOut InOut = (MInOut) po;
			//si se anula el despacho, se libera boton completar en NV
			if(InOut.getC_Order_ID() > 0 && InOut.isSOTrx())
			{
				DB.executeUpdate("UPDATE C_Order SET isSelected='Y' " +
					" WHERE C_Order_id = "+InOut.getC_Order_ID(), po.get_TrxName());
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