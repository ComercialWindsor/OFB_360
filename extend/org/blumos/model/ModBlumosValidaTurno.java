package org.blumos.model;

import java.sql.Timestamp;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.ofb.utils.DateUtils;
/**
 *	Validator for company blumos
 *
 *  @author ininoles
 */
public class ModBlumosValidaTurno implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosValidaTurno ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosValidaTurno.class);
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
		engine.addDocValidate(MOrder.Table_Name, this);
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
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
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MOrder.Table_ID)
		{
			MOrder order = (MOrder) po;
			if(order.isSOTrx() && order.getDeliveryViaRule().compareTo("P") != 0
					&& order.getDeliveryViaRule().compareTo("V") != 0
					&& order.getM_Warehouse_ID() == 1000000
					&& !order.get_ValueAsBoolean("Solutec"))
			{	
				//nuevo metodo de actualizacion de turnos y fecha prometida				
				Timestamp newDatePromise = order.getDatePromised();
				if(BlumosUtilities.DameSumarDiasTurno(DateUtils.now(), order.getDatePromised()) > 0)
					//newDatePromise = DateUtils.SumarDias(DateUtils.now(), BlumosUtilities.DameSumarDiasTurno(DateUtils.now(), order.getDatePromised()));
					newDatePromise = DateUtils.SumarDias(newDatePromise, BlumosUtilities.DameSumarDiasTurno(DateUtils.now(), order.getDatePromised()));
				order.setDatePromised(newDatePromise);
				//siempre se setea turno
				order.setPriorityRule(BlumosUtilities.DameTurno(DateUtils.now(), order.getDatePromised()));
				order.save(po.get_TrxName());
				//se actualiza fecha de lineas
				MOrderLine[] lines = order.getLines(true, null);	//	Line is default
				for (int i = 0; i < lines.length; i++)
				{
					MOrderLine line = lines[i];
					//actualizacion de rrstartdate
					Timestamp date= (Timestamp)line.get_Value("rrstartdate");
					if(date == null)
					{
						line.set_CustomColumn("rrstartdate", line.getDatePromised());
						line.set_CustomColumn("Qty_Original", line.getQtyOrdered());
						line.save(po.get_TrxName());
					}
					//actualizacion de datepromised lineas
					if(line.getDatePromised().compareTo(newDatePromise) <= 0)
					{
						line.setDatePromised(newDatePromise);
						line.save(po.get_TrxName());
					}
				}
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