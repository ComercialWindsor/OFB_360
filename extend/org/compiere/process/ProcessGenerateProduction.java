package org.compiere.process;

import java.math.BigDecimal;
//import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import org.compiere.model.Query;
import org.compiere.model.X_M_Product;
import org.compiere.model.X_M_Production;
import org.compiere.model.X_M_ProductionPlan;
import org.compiere.model.X_M_RelatedProduct;
import org.compiere.util.DB;

public class ProcessGenerateProduction extends SvrProcess{

	private int p_Record_ID = 0;	
	private int m_product_idP = 0;	
	private int loteP = 0;	
	private int m_locator_id;
	private String 		m_sql = null; 
	private Timestamp p_DateDoc_To = null;
	
	
	
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Product_ID"))
				m_product_idP = ((Number)para[i].getParameter()).intValue();
			else if (name.equals("Lote"))
				loteP = (para[i].getParameterAsInt());
			else if (name.equals("MovementDate"))
				p_DateDoc_To = (Timestamp)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Record_ID = getRecord_ID();		
	}
	
	protected String doIt() throws Exception	
	{
		
		X_M_Product product = new X_M_Product(getCtx(), m_product_idP, get_TrxName());
		
		m_sql = "select name from m_product where m_product_id = ?";
		
		m_locator_id = DB.getSQLValue(null, m_sql, m_product_idP );
		
		//Genera Produccion Producto seleccionado.
		X_M_Production production = new X_M_Production(getCtx(), p_Record_ID, get_TrxName());
		production.setName(Integer.toString(loteP).trim()+ " " + product.getName().trim());
		production.setMovementDate(p_DateDoc_To);
		//production.set_ValueOfColumn("Lote", loteP);
		production.saveEx();
		
		m_sql = "select m_locator_id from m_product where m_product_id = ?";
		
		m_locator_id = DB.getSQLValue(null, m_sql, m_product_idP );
		
		//Genera PLan de Produccion producto selecccionado
		X_M_ProductionPlan pp = new X_M_ProductionPlan(getCtx(), 0 , get_TrxName());
		pp.setM_Production_ID(production.getM_Production_ID());
		pp.setM_Product_ID(m_product_idP);
		pp.setProductionQty(new BigDecimal(1));
		pp.setM_Locator_ID(m_locator_id);
		pp.setLine(10);
		pp.saveEx();
				
		String whereClause = "M_Product_ID=? ";		
		List<X_M_RelatedProduct> rProducts= new Query(getCtx(), X_M_RelatedProduct.Table_Name, whereClause, get_TrxName())
		  									.setParameters(m_product_idP)
		  									.setOrderBy("RelatedProduct_ID")
		  									.list();  
		
		//genera produccion y plan de produccion por productos relacionado
		for (X_M_RelatedProduct rp :rProducts)
		{	
			product = new X_M_Product(getCtx(), rp.getRelatedProduct_ID(), get_TrxName());
			X_M_Production productionI = new X_M_Production(getCtx(), 0 , get_TrxName());
			productionI.setName(Integer.toString(loteP).trim() + " " + product.getName().trim());
			productionI.setMovementDate(production.getMovementDate());
			//productionI.set_ValueOfColumn("lote", loteP);
			productionI.saveEx();
			
			m_sql = "select m_locator_id from m_product where m_product_id = ?";
			
			m_locator_id = DB.getSQLValue(null, m_sql, rp.getRelatedProduct_ID() );
			
			X_M_ProductionPlan ppi = new X_M_ProductionPlan(getCtx(), 0 , get_TrxName());
			ppi.setM_Production_ID(productionI.getM_Production_ID());
			ppi.setM_Product_ID(rp.getRelatedProduct_ID());
			ppi.setProductionQty(new BigDecimal(1));
			ppi.setM_Locator_ID(m_locator_id);
			ppi.setLine(10);
			ppi.saveEx();			
		}				
		return "@OK@";
	}
	
}
