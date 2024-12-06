package org.blumos.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.MClient;
import org.compiere.model.MInOut;
import org.compiere.model.MOrder;
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
public class ModBlumosCorrigeReservado implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosCorrigeReservado ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosCorrigeReservado.class);
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
		engine.addDocValidate(MInOut.Table_Name, this);
		
				

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
		if((timing == TIMING_BEFORE_COMPLETE || timing == TIMING_BEFORE_VOID) && po.get_Table_ID()==MOrder.Table_ID)  
		{
			//MInOut inOut = (MInOut) po;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			//actualizar reservas de venta
			String v_query = "SELECT SUM(OL.QTYRESERVED) AS TOTAL, OL.M_WAREHOUSE_ID, OL.M_PRODUCT_ID " +
					" FROM C_ORDERLINE OL" +
					" INNER JOIN C_ORDER O ON (OL.C_ORDER_ID=O.C_ORDER_ID)" +
					" WHERE (O.DOCSTATUS='CO' OR O.DOCSTATUS='CL') AND OL.QTYRESERVED>0 AND O.ISSOTRX='Y'" +
					" GROUP BY OL.M_WAREHOUSE_ID, OL.M_PRODUCT_ID";
			
			pstmt = DB.prepareStatement (v_query, null);
			try 
			{
				rs = pstmt.executeQuery ();			
				DB.executeUpdate("UPDATE M_STORAGE SET QTYRESERVED=0", po.get_TrxName());
				while(rs.next ())
				{
					int ID_Locator = DB.getSQLValue(po.get_TrxName(), "SELECT M_LOCATOR_ID FROM M_LOCATOR" +
							" WHERE  M_WAREHOUSE_ID="+rs.getInt("M_WAREHOUSE_ID")+" LIMIT 1");
					DB.executeUpdate("UPDATE M_STORAGE SET QTYRESERVED="+rs.getBigDecimal("TOTAL")+
							" WHERE M_LOCATOR_ID="+ID_Locator+" AND M_PRODUCT_ID="+rs.getInt("M_PRODUCT_ID")+" " +
									" LIMIT 1 ",po.get_TrxName());
				}
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//actualizar reservas de compra			
			pstmt = null;
			rs = null;
			String v_query2 = "SELECT SUM(OL.QTYRESERVED) AS TOTAL, OL.M_WAREHOUSE_ID, OL.M_PRODUCT_ID FROM C_ORDERLINE OL" +
					" INNER JOIN C_ORDER O ON (OL.C_ORDER_ID=O.C_ORDER_ID)" +
					" WHERE (O.DOCSTATUS='CO' OR O.DOCSTATUS='CL') AND OL.QTYRESERVED>0 AND O.ISSOTRX='N'" +
					" GROUP BY OL.M_WAREHOUSE_ID, OL.M_PRODUCT_ID";
	
			pstmt = DB.prepareStatement (v_query2, null);
			try 
			{
				rs = pstmt.executeQuery ();			
				DB.executeUpdate("UPDATE M_STORAGE SET QTYORDERED=0", po.get_TrxName());
				while(rs.next ())
				{
					int ID_Locator = DB.getSQLValue(po.get_TrxName(), "SELECT M_LOCATOR_ID FROM M_LOCATOR" +
							" WHERE  M_WAREHOUSE_ID="+rs.getInt("M_WAREHOUSE_ID")+" LIMIT 1");
					DB.executeUpdate("UPDATE M_STORAGE SET QTYORDERED="+rs.getBigDecimal("TOTAL")+
							" WHERE M_LOCATOR_ID="+ID_Locator+" AND M_PRODUCT_ID="+rs.getInt("M_PRODUCT_ID")+
							"  LIMIT 1 ",po.get_TrxName());
				}
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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