package org.blumos.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.logging.Level;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import org.compiere.model.MClient;
import org.compiere.model.MSequence;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MProduct;
import org.compiere.model.MInventory;
import org.compiere.model.MFactAcct;
import org.compiere.model.X_Fact_Acct;
import org.compiere.acct.FactLine;



/**
 *	Validator for company ignisterra
 *
 *  @author mfrojas
 */
@SuppressWarnings("unused")
public class ModBlumosFactAcct implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosFactAcct ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosFactAcct.class);
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
		engine.addModelChange(X_Fact_Acct.Table_Name, this);
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==X_Fact_Acct.Table_ID )  
		{
			//mfrojas se validará la información para que sólo se haga en documentos
			//de tipo consumo de producción en m_inventory
			
			FactLine fac = (FactLine) po;
			
			if(fac.getAD_Table_ID() == 321)
			{
				//@mfrojas se realizará modificación sólo cuando el tipo de documento sea el "consumo de producción"
				String sqldocumenttype = "select c_doctype_id from m_inventory where m_inventory_id = "+fac.getRecord_ID();
				int tp = DB.getSQLValue(po.get_TrxName(), sqldocumenttype);
				if(tp == 1000808)
				{
					//@mfrojas momento de analizar el costo. 
					// se obtiene la información del asiento a guardar.
					if(fac.getAmtAcctCr().compareTo(Env.ZERO) > 0)
					{
						String sqlcosto = "SELECT costo from m_inventoryline where m_inventory_id = "+fac.getRecord_ID()+" and line_id = "+fac.getLine_ID();
						int costo = DB.getSQLValue(po.get_TrxName(), sqldocumenttype);
						log.config("COSTO PRODUCTO = "+costo);
						fac.setAmtAcctCr(BigDecimal.valueOf(costo));
						fac.setAmtSourceCr(BigDecimal.valueOf(costo));
						
					}
					else if(fac.getAmtAcctDr().compareTo(Env.ZERO) > 0)
					{
						String sqlcosto = "SELECT costo from m_inventoryline where m_inventory_id = "+fac.getRecord_ID()+" and line_id = "+fac.getLine_ID();
						int costo = DB.getSQLValue(po.get_TrxName(), sqldocumenttype);
						log.config("COSTO PRODUCTO = "+costo);
						fac.setAmtAcctDr(BigDecimal.valueOf(costo));
						fac.setAmtSourceDr(BigDecimal.valueOf(costo));
						
					}
					
					fac.save();
						
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