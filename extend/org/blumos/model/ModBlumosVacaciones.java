package org.blumos.model;

import java.math.BigDecimal;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_T_BL_VAC_SOLICITUD;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.ofb.utils.DateUtils;
/**
 *	Validator for company blumos
 *
 *  @author ininoles
 */
public class ModBlumosVacaciones implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosVacaciones ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosVacaciones.class);
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
		engine.addModelChange(X_T_BL_VAC_SOLICITUD.Table_Name, this);
		//	Documents to be monitored
		//engine.addDocValidate(MInvoice.Table_Name, this);
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==X_T_BL_VAC_SOLICITUD.Table_ID)
		{
			X_T_BL_VAC_SOLICITUD solVac = (X_T_BL_VAC_SOLICITUD) po;
			
			if(solVac.getDESDE() == null)
				throw new AdempiereException("DEBE ESPECIFICAR FECHA DE INICIO DE FERIADO. PUSO: "+solVac.getDESDE());
			else if(solVac.getHASTA() == null)
				throw new AdempiereException("DEBE ESPECIFICAR FECHA DE FIN DE FERIADO. PUSO: "+solVac.getHASTA());
			else if(solVac.getHASTA().compareTo(solVac.getDESDE()) < 0)
				throw new AdempiereException("FECHA DE FIN NO PUEDE SER ANTERIOR A FECHA DE INICIO. PUSO DESDE: "+solVac.getDESDE()+" HASTA: "+solVac.getHASTA());
			BigDecimal v_normal = DB.getSQLValueBD(po.get_TrxName(), "Select Sum(Dias) From T_Bl_Vac_Movimientos Where Fecha_Movimiento<=now() And C_Bpartner_Id="+solVac.getC_BPartner_ID()+" And Progresivo='N'");
			if(v_normal == null)
				v_normal = Env.ZERO;
			BigDecimal v_progresivo = DB.getSQLValueBD(po.get_TrxName(), "SELECT coalesce((select sum(dias) from t_bl_vac_movimientos where fecha_movimiento<=sysdate and c_bpartner_id="+solVac.getC_BPartner_ID()+" and progresivo='Y'),0) from dual");
			if(v_progresivo == null)
				v_progresivo = Env.ZERO;
			BigDecimal v_pp = DB.getSQLValueBD(po.get_TrxName(), "SELECT ((Trunc(Mod(Months_Between(?,Fecha_Ingreso),12)))*1.25) from t_bl_vac_maestro where c_bpartner_id="+solVac.getC_BPartner_ID(),DateUtils.today());
			if(v_pp == null)
				v_pp = Env.ZERO;
			BigDecimal v_total = v_normal.add(v_pp).add(v_progresivo);
			
			solVac.setSALDO_NORMAL(v_normal);
			solVac.setSALDO_PP(v_pp);
			solVac.setSALDO_PROGRESIVO(v_progresivo);
			solVac.setSALDO_TOTAL(v_total);

			int v_diasInt = BlumosUtilities.DameDiasHabiles(solVac.getDESDE(), solVac.getHASTA(), po.getCtx(), po.get_TrxName());
			BigDecimal v_dias = BigDecimal.valueOf(v_diasInt);
			
			if(solVac.isMEDIO_DIA())
				v_dias = new BigDecimal("0.5");
			
			solVac.setDIAS(v_dias);
			
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