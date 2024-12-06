package org.compiere.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.acct.DocLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import bsh.This;

/**
 * 	Product Cost Model.
 *	Utilitario con toda la logica de costeo
 *	
 *  @author Fabian Aguilar Fuentes
 *  @version 1.0 $
 */

public class OFBProductCost {

	/**	Logger					*/
	private static CLogger 	log = CLogger.getCLogger (OFBProductCost.class);
	
	
	/**
	 * trae el costo desde la order line para un producto*/
	public static BigDecimal getProductOrderCost(int Product_ID, int OrderLine_ID, BigDecimal qty, String trxName,Properties ctx){
		
		BigDecimal returnCost=Env.ZERO;
		
			if(OrderLine_ID>0)
			{
				MOrderLine line = new MOrderLine (Env.getCtx(),OrderLine_ID,trxName);
				returnCost=line.getLineNetAmt();	
				
			}	
				
		return returnCost;
	}
	
	/**
	 * trae el costo actual del producto, dependiendo si es a nivel de organizacion
	 * o de cliente, si es por organizacion busca la org padre*/
	public static BigDecimal getProductCost(int Product_ID , int Org_ID, BigDecimal qty, String trxName, Properties ctx)
	{
		BigDecimal returnCost=Env.ZERO;
		MProduct product = MProduct.get(ctx, Product_ID);
		MAcctSchema ass = MAcctSchema.getClientAcctSchema(ctx, Env.getAD_Client_ID(ctx))[0];
		
		processCost(Product_ID,trxName, ctx);//actualiza costo
		
		MCost cost=null;		
		
		if(product.getCostingLevel(ass).equals(X_M_Product_Category_Acct.COSTINGLEVEL_Organization)){
			cost = getMCost(Product_ID, getParentOrg(Org_ID,trxName,ctx),0,ass.getC_AcctSchema_ID(),ass.getM_CostType_ID() ,trxName,ctx);
		}
		else{
			cost = getMCost(Product_ID,0,0,ass.getC_AcctSchema_ID(),ass.getM_CostType_ID() ,trxName,ctx);
		}
		
		if(cost.getCurrentCostPrice().signum()<=0)
			returnCost = cost.getFutureCostPrice();
		else
			returnCost = cost.getCurrentCostPrice();
		
		returnCost = returnCost.multiply(qty);
		
		return returnCost;
		
	}
	
	/**
	 * trae la cantidad actual del producto, dependiendo si es a nivel de organizacion
	 * o de cliente, si es por organizacion busca la org padre*/
	public static BigDecimal getProductCostQty(int Product_ID , int Org_ID, String trxName, Properties ctx)
	{
		BigDecimal returnCost=Env.ZERO;
		MProduct product = MProduct.get(Env.getCtx(), Product_ID);
		MAcctSchema ass = MAcctSchema.getClientAcctSchema(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()))[0];
				
		processCost(Product_ID,trxName,ctx);//actualiza costo
		
		if(product.getCostingLevel(ass).equals(X_M_Product_Category_Acct.COSTINGLEVEL_Organization)){
			returnCost = getMCost(Product_ID, getParentOrg(Org_ID,trxName,ctx),0,ass.getC_AcctSchema_ID(),ass.getM_CostType_ID() ,trxName,ctx).getCurrentQty();
		}
		else{
			returnCost = getMCost(Product_ID,0,0,ass.getC_AcctSchema_ID(),ass.getM_CostType_ID() ,trxName,ctx).getCurrentQty();
		}
		
		return returnCost;
		
	}
	
	
	/**
	 * procesa todos los cost details que no esten procesados
	 * */
	public static void processCost(int Product_ID, String trxName, Properties ctx ){
		
		String sql="select * from M_CostDetail where processed='N' and Enabled='Y' " +
				"and M_Product_ID = ? and C_AcctSchema_ID=? ";
		
			
		sql+=" order by DateAcct";
		
		MAcctSchema schema=MAcctSchema.getClientAcctSchema(ctx, Env.getAD_Client_ID(ctx))[0];

		log.fine("schema :"+ schema);
		log.fine("sql: "+sql.toString());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trxName);
			pstmt.setInt(1, Product_ID);
			pstmt.setInt(2, schema.getC_AcctSchema_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MCostDetail detail = new MCostDetail(Env.getCtx(),rs,trxName);
				log.fine("detail :"+detail);
				processDetail(detail,schema,ctx);
				
				
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
	}
	
	public static void processDetail(MCostDetail detail, MAcctSchema schema, Properties ctx){
		
		MProductCategoryAcct category= MProductCategoryAcct.get(ctx, detail.getM_Product().getM_Product_Category_ID(), 
				schema.getC_AcctSchema_ID(), detail.get_TrxName());
		
		if(detail.isProcessed())
			return;
		
		String costingMethod = new String();
		String costingLevel= new String(); //C=CLient  O=Organizaton
		
		if(category.getCostingLevel()==null || category.getCostingLevel().length()==0)
			costingLevel = schema.getCostingLevel();
		else
			costingLevel = category.getCostingLevel();
		
		MCost cost=null;
		log.config("Product_ID :"+detail.getM_Product_ID() +"- Detail_ID: "+detail.getM_CostDetail_ID());
		
		if(detail.getC_OrderLine_ID()>0)
		{	
			detail.setProcessed(true);
			detail.save();
			return;
		}
		
		if(costingMethod.equals(X_M_Product_Category_Acct.COSTINGMETHOD_AverageInvoice) || costingMethod.equals(X_M_Product_Category_Acct.COSTINGMETHOD_LastInvoice))//solo detalles de facturas
			if( (detail.getC_InvoiceLine_ID()==0 || detail.isSOTrx() ) && detail.get_ValueAsInt("GL_JournalLine_ID")==0)
			{	
				detail.setProcessed(true);
				detail.save();
				return;
			}
		
		if(costingLevel.equals("C"))
			cost=getMCost(detail.getM_Product_ID(),0,detail.getM_CostElement_ID(),schema.getC_AcctSchema_ID(),schema.getM_CostType_ID() ,detail.get_TrxName(),ctx);
		else
			cost=getMCost(detail.getM_Product_ID(),detail.getAD_Org_ID(),detail.getM_CostElement_ID(),schema.getC_AcctSchema_ID(),schema.getM_CostType_ID() ,detail.get_TrxName(),ctx);
		
		
		log.config("Processing Cost :" +cost);
		BigDecimal oldAmt = cost.getCumulatedAmt();
				
		if(costingMethod.equals(X_M_Product_Category_Acct.COSTINGMETHOD_LastInvoice)){
			cost.add(detail.getAmt(), detail.getQty());
			cost.setCurrentCostPrice(detail.getAmt().divide(detail.getQty(),BigDecimal.ROUND_HALF_UP));
		}
		else
		{//costo estandard
			log.config("Costo Standard");
			MJournal journal= null;
			if(detail.get_ValueAsInt("GL_JournalLine_ID")>0){
				log.config("journal");
				MJournalLine line = new MJournalLine(ctx,detail.get_ValueAsInt("GL_JournalLine_ID"),detail.get_TrxName());
				journal = line.getParent();
				//ininoles se agrega validacion para diarios sin descripcion 05-09-2017
				if(journal.getDescription() != null && journal.getDescription().toUpperCase().indexOf("INICIAL")<0)
					cost.add(detail.getAmt().setScale(4, BigDecimal.ROUND_HALF_UP), detail.getQty());//ininoles 4 decimales
					
			}
			else if(detail.getC_InvoiceLine_ID()>0){//no suma cantidad
				log.config("factura");
				if (detail.getDeltaAmt().signum() != 0)//es correccion
					cost.add(detail.getDeltaAmt().setScale(4, BigDecimal.ROUND_HALF_UP), Env.ZERO);//ininoles 4 decimales
				else
					cost.add(detail.getAmt().setScale(4, BigDecimal.ROUND_HALF_UP), Env.ZERO);//ininoles 4 decimales
			}
			else{
				log.config("entregas - recibos - inventario");
				if (detail.getDeltaAmt().signum() != 0)//es correccion
					cost.add(detail.getDeltaAmt().setScale(4, BigDecimal.ROUND_HALF_UP), detail.getDeltaQty());//ininoles 4 decimales
				else
					cost.add(detail.getAmt().setScale(4, BigDecimal.ROUND_HALF_UP), detail.getQty());//ininoles 4 decimales
			
			}
			
			BigDecimal TemCost;
			
			if(journal !=null && journal.getDescription() != null && journal.getDescription().toUpperCase().indexOf("INICIAL")>=0)
			{
				log.config("journal inicial");
				TemCost = detail.getAmt();
				
				if(detail.getQty().signum()>0)
				{
					cost.setCurrentQty(detail.getQty());
					cost.setCumulatedQty(detail.getQty());
					TemCost = TemCost.divide(detail.getQty().setScale(4,BigDecimal.ROUND_HALF_EVEN));//ininoles 4 decimales
				}				
				if(detail.getAmt().signum()>0)
					cost.setCumulatedAmt(detail.getAmt());
			}
			else
			{
				//correccion bug inicio
				
				if(detail.getQty().signum()>0 && detail.getAmt().signum()>0 && (detail.getM_InOutLine_ID()>0 || detail.getM_ProductionLine_ID()>0  ))//recibo aumento inventario al costo
					if(cost.getCumulatedQty().longValue()<-4 && cost.getCumulatedAmt().signum()<0)
					{
						cost.setCumulatedAmt(detail.getAmt());
						cost.setCumulatedQty(detail.getQty());
						cost.setCurrentQty(detail.getQty());
					}
				//correccion bug fin
				
				
				if(cost.getCumulatedQty().longValue()>0.7 && cost.getCumulatedAmt().signum()>0)
					TemCost = cost.getCumulatedAmt().divide(cost.getCumulatedQty(),4,BigDecimal.ROUND_HALF_UP);//ininoles 4 decimales
				else
				{ 
					TemCost =cost.getCurrentCostPrice();//no sobre escribo el costo me quedo con el ultimo cuando todo era positivo
					cost.setCumulatedAmt(Env.ZERO);
					cost.setCurrentQty(Env.ZERO);
					cost.setCumulatedQty(Env.ZERO);
				}
				
			}
				
			cost.setCurrentCostPrice(TemCost.setScale(4, BigDecimal.ROUND_HALF_UP));//ininoles 4 decimales
		}
		
		detail.setCurrentCostPrice(cost.getCurrentCostPrice());
		detail.setCurrentQty(cost.getCurrentQty());
		detail.setCumulatedAmt(cost.getCumulatedAmt());
		detail.setDescription(detail.getDescription() + " / Old:"+ oldAmt+ " A:"+cost.getCumulatedAmt() + "| Q:"+cost.getCumulatedQty() +" / CA:"+cost.getCurrentCostPrice());
		detail.setProcessed(true);
		detail.save();
		cost.save();
	}
	
	
	/**
	 * trae el elemento de costo que corresponda para un producto especifico
	 * ya sea a nivel de compañia u organizacion*/
	public static MCost getMCost(int Product_ID, int Org_ID,int CostElement_ID, int AcctSchema_ID, int CostType_ID, String trxName, Properties ctx){
		
		//****validacion de org y centros de costo****//
		if(Org_ID>0)
			Org_ID=getParentOrg(Org_ID,trxName,ctx);
		
		if(CostElement_ID == 0)
			CostElement_ID=getStandarCostelement(ctx).getM_CostElement_ID();
		//**********************************************
		log.config("getMCost :" + Org_ID + "-" + Product_ID);
		MCost cost= get(ctx, Env.getAD_Client_ID(ctx), Org_ID, Product_ID, 
				CostType_ID, AcctSchema_ID, CostElement_ID, 0,trxName);
		
		log.config("getMCost :" +cost);
		if(cost == null)
			cost = createMCost(Product_ID, Org_ID,CostElement_ID, AcctSchema_ID,trxName,ctx);
			
		return cost;
		
	}
	
	/** crea el registro de costo si no existe la primera ves **/
	public static MCost createMCost(int Product_ID, int Org_ID,int CostElement_ID, int AcctSchema_ID,String trxName, Properties ctx){
		
		MProduct product = new MProduct(ctx, Product_ID,trxName);
		MAcctSchema as = MAcctSchema.get(ctx, AcctSchema_ID);

		MCost cost = new MCost(product, 0,
				 as, Org_ID, CostElement_ID);
		
		if(cost.save())
		    return cost;
		else
			return null;
		
	}
	
	/**
	 * busca la org padre recursivamente hasta encontrarla y que no sea centro de costo
	 * si no la encuentra retorna 0 = *  */
	public static int getParentOrg(int Org_ID, String trxName, Properties ctx)
	{
		log.config("getParentOrg : Begin :" +Org_ID );
		MOrg org=MOrg.get(ctx, Org_ID);
		if(org.get_ValueAsBoolean("IsCostCenter")){
			
			MOrgInfo info = MOrgInfo.get(ctx, Org_ID, trxName);
			log.config("getParentOrg : isCostcenter :"+ info );
			if(info.getParent_Org_ID()>0)
				return getParentOrg(info.getParent_Org_ID(),trxName,ctx);
			else
				return 0;
		}
		else{
			log.config("getParentOrg : Return :" +Org_ID );
			return Org_ID;
		}
	}
	
	/**
	 * retorna verdadero o falso si 2 organizacones o centros de costos
	 * pertenecen a la misma Org padre
	 * -usado en doc_movement para saber si se movieron materiales a distintos rut y
	 * calcular el costo*/
	public static boolean sameOrg(int Org1_ID, int Org2_ID, Properties ctx){
		
		if(getParentOrg(Org1_ID,null,ctx)!=getParentOrg(Org2_ID,null,ctx))
			return false;
		else
			return true;
		
	}
	/**
	 * 	Create New Order Cost Detail for Purchase Orders.
	 * 	Called from Doc_MatchPO
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param C_OrderLine_ID order
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param trxName transaction
	 *	@return true if created
	 */
	public static boolean createOrder (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int C_OrderLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, String trxName)
	{
		
		//	Delete Unprocessed zero Differences
		String sql = "DELETE  From M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND C_OrderLine_ID=" + C_OrderLine_ID
			+ " AND C_AcctSchema_ID =" + as.getC_AcctSchema_ID()
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "C_OrderLine_ID=?", 
			C_OrderLine_ID, M_AttributeSetInstance_ID, as.getC_AcctSchema_ID(), trxName);
		//
		if (cd == null)		//	createNew
		{
			Amt=Amt.setScale(0, BigDecimal.ROUND_HALF_UP);
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setC_OrderLine_ID (C_OrderLine_ID);
			//faaguilar begin
			MOrder order = new MOrderLine(as.getCtx(), C_OrderLine_ID ,trxName ).getParent();
			cd.set_CustomColumn("DateAcct", order.getDateAcct());
			cd.set_CustomColumn("Enabled",false);
			if(Qty.signum()!=0)
			cd.setDescription("C_Cost:"+Amt.divide(Qty,0, BigDecimal.ROUND_HALF_UP).setScale(0));
			
			//faaguilar end
		}
		else
		{
			// MZ Goodwill
			// set deltaAmt=Amt, deltaQty=qty, and set Cost Detail for Amt and Qty	 
			cd.setDeltaAmt(Amt.subtract(cd.getAmt()));
			cd.setDeltaQty(Qty.subtract(cd.getQty()));
			if(Qty.signum()!=0)
			cd.setDescription(cd.getDescription()+" U_Cost:"+Amt.divide(Qty,0, BigDecimal.ROUND_HALF_UP).setScale(0));
			if (cd.isDelta())
			{
				cd.setProcessed(false);
				cd.setAmt(Amt);
				cd.setQty(Qty);
			}
			// end MZ
			else
				return true;	//	nothing to do
		}
		//faaguilar OFB begin
		MProductCategory cat = new MProductCategory(as.getCtx(),new MProduct(as.getCtx(),M_Product_ID,trxName).getM_Product_Category_ID(),trxName);
		boolean noCost=(Boolean)cat.get_Value("NoCost");
		if(noCost)
			cd.set_CustomColumn("Enabled",false);
		//faaguilar OFB end
		
		
		boolean ok = cd.save();
		
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate()){
			log.config("Ctx: "+as.getCtx());
			processCost(M_Product_ID,trxName,as.getCtx());
			}
		}
		log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createOrder
	
	/**
	 * 	Create New Invoice Cost Detail for AP Invoices.
	 * 	Called from Doc_Invoice - for Invoice Adjustments
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param C_InvoiceLine_ID invoice
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param trxName transaction
	 *	@return true if created
	 */
	public static boolean createInvoice (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int C_InvoiceLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, String trxName)
	{
		
		log.config("start - C_InvoiceLine_ID:"+C_InvoiceLine_ID+ " M_Product_ID:"+M_Product_ID+"  M_AttributeSetInstance_ID:"+ M_AttributeSetInstance_ID );
		
		//	Delete Unprocessed zero Differences
		String sql = "DELETE  From M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND C_InvoiceLine_ID=" + C_InvoiceLine_ID
			+ " AND C_AcctSchema_ID =" + as.getC_AcctSchema_ID()			
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		if(M_Product_ID>0)
			sql += " AND M_Product_ID=" + M_Product_ID;
		
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			log.config("Deleted #" + no);
		
		MCostDetail cd = null;
		if(M_Product_ID>0)
			cd = getP (as.getCtx(), "C_InvoiceLine_ID=?", 
				C_InvoiceLine_ID, M_AttributeSetInstance_ID, as.getC_AcctSchema_ID(),M_Product_ID, trxName);
		else
			cd = get(as.getCtx(), "C_InvoiceLine_ID=?", 
					C_InvoiceLine_ID, M_AttributeSetInstance_ID, as.getC_AcctSchema_ID(), trxName);
		//
		log.config("cd-found:"+cd);
		if (cd == null)		//	createNew
		{
			Amt=Amt.setScale(0, BigDecimal.ROUND_HALF_UP);
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setC_InvoiceLine_ID (C_InvoiceLine_ID);
			//faaguilar begin
			MInvoice invoice = new MInvoiceLine(as.getCtx(), C_InvoiceLine_ID ,trxName ).getParent();
			cd.set_CustomColumn("DateAcct", invoice.getDateAcct());
			cd.set_CustomColumn("Enabled",true);
			if(Qty.signum()!=0)
				cd.setDescription("C_Cost:"+Amt.divide(Qty,0, BigDecimal.ROUND_HALF_UP).setScale(0));
			//faaguilar end
			
		}
		else
		{
			// MZ Goodwill
			// set deltaAmt=Amt, deltaQty=qty, and set Cost Detail for Amt and Qty	 
			cd.setDeltaAmt(Amt.subtract(cd.getAmt()));
			cd.setDeltaQty(Qty.subtract(cd.getQty()));
			MInvoice invoice = new MInvoiceLine(as.getCtx(), C_InvoiceLine_ID ,trxName ).getParent();
			cd.set_CustomColumn("DateAcct", invoice.getDateAcct());
			cd.setDescription(Description + " Old:" +cd.getAmt().setScale(0, BigDecimal.ROUND_HALF_UP));
			if(Qty.signum()!=0)
				cd.setDescription(cd.getDescription()+" U_Cost:"+Amt.divide(Qty,0, BigDecimal.ROUND_HALF_UP).setScale(0));
			if (cd.isDelta())
			{
				cd.setProcessed(false);
				cd.setAmt(Amt);
				cd.setQty(Qty);
			}
			// end MZ
			else
				return true;	//	nothing to do
		}
		
		//faaguilar OFB begin
		MProductCategory cat = new MProductCategory(as.getCtx(),new MProduct(as.getCtx(),M_Product_ID,trxName).getM_Product_Category_ID(),trxName);
		boolean noCost=(Boolean)cat.get_Value("NoCost");
		if(noCost)
			cd.set_CustomColumn("Enabled",false);
		
		/*cd.setPrice(cd.getAmt());
		if(cd.getQty().signum()>0)
			cd.setPrice(cd.getAmt().divide(cd.getQty(), 0, BigDecimal.ROUND_HALF_EVEN));*/
		//faaguilar OFB end
		
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate()){
				//processDetail(cd,as,as.getCtx());
				log.config("Ctx: "+as.getCtx());
				processCost(M_Product_ID,trxName,as.getCtx());
			}
		}
		log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createInvoice
	
	/**
	 * 	Create New Shipment Cost Detail for SO Shipments.
	 * 	Called from Doc_MInOut - for SO Shipments  
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_InOutLine_ID shipment
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param IsSOTrx sales order
	 *	@param trxName transaction
	 *	@return true if no error
	 */
	public static boolean createShipment (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_InOutLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, boolean IsSOTrx, String trxName)
	{
		
		//	Delete Unprocessed zero Differences
		String sql = "DELETE  From M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_InOutLine_ID=" + M_InOutLine_ID
			+ " AND C_AcctSchema_ID =" + as.getC_AcctSchema_ID()
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_InOutLine_ID=?", 
			M_InOutLine_ID, M_AttributeSetInstance_ID, as.getC_AcctSchema_ID(), trxName);
		//
		if (cd == null)		//	createNew
		{
			Amt=Amt.setScale(0, BigDecimal.ROUND_HALF_UP);
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setM_InOutLine_ID(M_InOutLine_ID);
			cd.setIsSOTrx(IsSOTrx);
			
			log.config("seteo variable Enabled: true");
			//faaguilar begin
			MInOut InOut = new MInOutLine(as.getCtx(), M_InOutLine_ID ,trxName ).getParent();
			cd.set_CustomColumn("DateAcct", InOut.getDateAcct());
			cd.set_CustomColumn("Enabled",true);
			log.config("despues seteo variable Enabled: true");
			if(Qty.signum()!=0)
				cd.setDescription("C_Cost:"+Amt.divide(Qty,0, BigDecimal.ROUND_HALF_UP).setScale(0));
			//faaguilar end
			
		}
		else
		{
			// MZ Goodwill
		  // set deltaAmt=Amt, deltaQty=qty, and set Cost Detail for Amt and Qty	 
			cd.setDeltaAmt(Amt.subtract(cd.getAmt()));
			cd.setDeltaQty(Qty.subtract(cd.getQty()));
			MInOut InOut = new MInOutLine(as.getCtx(), M_InOutLine_ID ,trxName ).getParent();
			cd.set_CustomColumn("DateAcct", InOut.getDateAcct());
			cd.setDescription(Description + " Old:" +cd.getAmt().setScale(0, BigDecimal.ROUND_HALF_UP));
			if(Qty.signum()!=0)
				cd.setDescription(cd.getDescription()+" U_Cost:"+Amt.divide(Qty,0, BigDecimal.ROUND_HALF_UP).setScale(0));
			if (cd.isDelta())
			{
				cd.setProcessed(false);
				cd.setAmt(Amt);
				cd.setQty(Qty);
			}
			// end MZ
			else
				return true;	//	nothing to do
		}
		
		//faaguilar OFB begin
		MProductCategory cat = new MProductCategory(as.getCtx(),new MProduct(as.getCtx(),M_Product_ID,trxName).getM_Product_Category_ID(),trxName);
		boolean noCost=(Boolean)cat.get_Value("NoCost");
		if(noCost)
			cd.set_CustomColumn("Enabled",false);
		
		/*cd.setPrice(cd.getAmt());
		if(cd.getQty().signum()>0)
			cd.setPrice(cd.getAmt().divide(cd.getQty(), 0, BigDecimal.ROUND_HALF_EVEN));*/
		//faaguilar OFB end
		
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate()){
				//processDetail(cd,as,as.getCtx());
				log.config("Ctx: "+as.getCtx());
				processCost(M_Product_ID,trxName,as.getCtx());
			}
		}
		log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createShipment
	
	
	/**
	 * 	Create New Order Cost Detail for Physical Inventory.
	 * 	Called from Doc_Inventory
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_InventoryLine_ID order
	 *	@param M_CostElement_ID optional cost element
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param trxName transaction
	 *	@return true if no error
	 */
	public static boolean createInventory (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_InventoryLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, String trxName)
	{
		
		//	Delete Unprocessed zero Differences
		String sql = "DELETE  From M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_InventoryLine_ID=" + M_InventoryLine_ID
			+ " AND C_AcctSchema_ID =" + as.getC_AcctSchema_ID()
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_InventoryLine_ID=?", 
			M_InventoryLine_ID, M_AttributeSetInstance_ID, as.getC_AcctSchema_ID(), trxName);
		//
		if (cd == null)		//	createNew
		{
			Amt=Amt.setScale(0, BigDecimal.ROUND_HALF_UP);
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setM_InventoryLine_ID(M_InventoryLine_ID);
			//faaguilar begin
			MInventory Inventory = new MInventoryLine(as.getCtx(), M_InventoryLine_ID ,trxName ).getParent();
			cd.set_CustomColumn("DateAcct", Inventory.getMovementDate() );
			cd.set_CustomColumn("Enabled",true);
			if(Qty.signum()!=0)
				cd.setDescription("C_Cost:"+Amt.divide(Qty,0, BigDecimal.ROUND_HALF_UP).setScale(0));
			//faaguilar end
		}
		else
		{
			// MZ Goodwill
			// set deltaAmt=Amt, deltaQty=qty, and set Cost Detail for Amt and Qty	
			cd.setDeltaAmt(Amt.subtract(cd.getAmt()));
			cd.setDeltaQty(Qty.subtract(cd.getQty()));
			MInventory Inventory = new MInventoryLine(as.getCtx(), M_InventoryLine_ID ,trxName ).getParent();
			cd.set_CustomColumn("DateAcct", Inventory.getMovementDate() );
			if(Qty.signum()!=0)
				cd.setDescription(cd.getDescription()+" U_Cost:"+Amt.divide(Qty,0, BigDecimal.ROUND_HALF_UP).setScale(0, BigDecimal.ROUND_HALF_UP));
			if (cd.isDelta())
			{
				cd.setProcessed(false);
				cd.setAmt(Amt);
				cd.setQty(Qty);
			}
			// end MZ
			else
				return true;	//	nothing to do
		}
		
		//faaguilar OFB begin
		MProductCategory cat = new MProductCategory(as.getCtx(),new MProduct(as.getCtx(),M_Product_ID,trxName).getM_Product_Category_ID(),trxName);
		boolean noCost=(Boolean)cat.get_Value("NoCost");
		if(noCost)
			cd.set_CustomColumn("Enabled",false);
		
		/*cd.setPrice(cd.getAmt());
		if(cd.getQty().signum()>0)
			cd.setPrice(cd.getAmt().divide(cd.getQty(), 0, BigDecimal.ROUND_HALF_EVEN));*/
		//faaguilar OFB end
		
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate()){
				//processDetail(cd,as,as.getCtx());
				log.config("Ctx: "+as.getCtx());
				processCost(M_Product_ID,trxName,as.getCtx());
			}
		}
		log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createInventory
	
	/**
	 * 	Create New Order Cost Detail for Movements.
	 * 	Called from Doc_Movement
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_MovementLine_ID movement
	 *	@param M_CostElement_ID optional cost element for Freight
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param from if true the from (reduction)
	 *	@param Description optional description
	 *	@param trxName transaction
	 *	@return true if no error
	 */
	public static boolean createMovement (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_MovementLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty, boolean from,
		String Description, String trxName)
	{
		
		//	Delete Unprocessed zero Differences
		String sql = "DELETE  From M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_MovementLine_ID=" + M_MovementLine_ID 
			+ " AND IsSOTrx=" + (from ? "'Y'" : "'N'")
			+ " AND C_AcctSchema_ID =" + as.getC_AcctSchema_ID()
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_MovementLine_ID=? AND IsSOTrx=" 
			+ (from ? "'Y'" : "'N'"), 
			M_MovementLine_ID, M_AttributeSetInstance_ID, as.getC_AcctSchema_ID(), trxName);
		//
		if (cd == null)		//	createNew
		{
			Amt=Amt.setScale(0, BigDecimal.ROUND_HALF_UP);
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setM_MovementLine_ID (M_MovementLine_ID);
			cd.setIsSOTrx(from);
			//faaguilar begin
			MMovement Movement = new MMovementLine(as.getCtx(), M_MovementLine_ID ,trxName ).getParent();
			cd.set_CustomColumn("DateAcct", Movement.getMovementDate() );
			cd.set_CustomColumn("Enabled",true);
			if(Qty.signum()!=0)
				cd.setDescription("C_Cost:"+Amt.divide(Qty,0, BigDecimal.ROUND_HALF_UP).setScale(0));
			//faaguilar end
		}
		else
		{
			// MZ Goodwill
			// set deltaAmt=Amt, deltaQty=qty, and set Cost Detail for Amt and Qty	
			cd.setDeltaAmt(Amt.subtract(cd.getAmt()));
			cd.setDeltaQty(Qty.subtract(cd.getQty()));
			MMovement Movement = new MMovementLine(as.getCtx(), M_MovementLine_ID ,trxName ).getParent();
			cd.set_CustomColumn("DateAcct", Movement.getMovementDate() );
			if(Qty.signum()!=0)
				cd.setDescription(cd.getDescription()+" U_Cost:"+Amt.divide(Qty,0, BigDecimal.ROUND_HALF_UP).setScale(0));
			if (cd.isDelta())
			{
				cd.setProcessed(false);
				cd.setAmt(Amt);
				cd.setQty(Qty);
			}
			// end MZ
			else
				return true;	//	nothing to do
		}
		
		//faaguilar OFB begin
		MProductCategory cat = new MProductCategory(as.getCtx(),new MProduct(as.getCtx(),M_Product_ID,trxName).getM_Product_Category_ID(),trxName);
		boolean noCost=(Boolean)cat.get_Value("NoCost");
		if(noCost)
			cd.set_CustomColumn("Enabled",false);
		
		/*cd.setPrice(cd.getAmt());
		if(cd.getQty().signum()>0)
			cd.setPrice(cd.getAmt().divide(cd.getQty(), 0, BigDecimal.ROUND_HALF_EVEN));*/
		//faaguilar OFB end
		
		boolean ok = cd.save();
		if (ok && !cd.isProcessed())
		{
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			if (client.isCostImmediate()){
				//processDetail(cd,as,as.getCtx());
				log.config("Ctx: "+as.getCtx());
				processCost(M_Product_ID,trxName,as.getCtx());
			}
		}
		log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createMovement
	
	
	/**
	 * 	Create New Order Cost Detail for Production.
	 * 	Called from Doc_Production
	 *	@param as accounting schema
	 *	@param AD_Org_ID org
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param M_ProductionLine_ID production line
	 *	@param M_CostElement_ID optional cost element
	 *	@param Amt amt total amount
	 *	@param Qty qty
	 *	@param Description optional description
	 *	@param trxName transaction
	 *	@return true if no error
	 */
	public static boolean createProduction (MAcctSchema as, int AD_Org_ID, 
		int M_Product_ID, int M_AttributeSetInstance_ID,
		int M_ProductionLine_ID, int M_CostElement_ID, 
		BigDecimal Amt, BigDecimal Qty,
		String Description, String trxName)
	{
		
		//	Delete Unprocessed zero Differences
		String sql = "DELETE M_CostDetail "
			+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
			+ " AND M_ProductionLine_ID=" + M_ProductionLine_ID
			+ " AND C_AcctSchema_ID =" + as.getC_AcctSchema_ID()
			+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
		int no = DB.executeUpdate(sql, trxName);
		if (no != 0)
			log.config("Deleted #" + no);
		MCostDetail cd = get (as.getCtx(), "M_ProductionLine_ID=?", 
			M_ProductionLine_ID, M_AttributeSetInstance_ID, as.getC_AcctSchema_ID(), trxName);
		//
		if (cd == null)		//	createNew
		{
			Amt=Amt.setScale(0, BigDecimal.ROUND_HALF_UP);
			cd = new MCostDetail (as, AD_Org_ID, 
				M_Product_ID, M_AttributeSetInstance_ID, 
				M_CostElement_ID, 
				Amt, Qty, Description, trxName);
			cd.setM_ProductionLine_ID(M_ProductionLine_ID);
			
			log.config("seteo variable Enabled: true");
			//faaguilar begin
			 
			X_M_ProductionLine MProdL = new X_M_ProductionLine(as.getCtx(), M_ProductionLine_ID, trxName);
			X_M_ProductionPlan MProdP = new X_M_ProductionPlan(as.getCtx(), MProdL.getM_ProductionPlan_ID(), trxName);
			X_M_Production MProdC = new X_M_Production(as.getCtx(), MProdP.getM_Production_ID(), trxName);
					
			cd.set_CustomColumn("DateAcct", MProdC.getMovementDate());			
			cd.set_CustomColumn("Enabled",true);
			log.config("despues seteo variable Enabled: true");
		}
		else
		{
			// MZ Goodwill
			// set deltaAmt=Amt, deltaQty=qty, and set Cost Detail for Amt and Qty	 
			cd.setDeltaAmt(Amt.subtract(cd.getAmt()));
			cd.setDeltaQty(Qty.subtract(cd.getQty()));
			if (cd.isDelta())
			{
				cd.setProcessed(false);
				cd.setAmt(Amt);
				cd.setQty(Qty);
			}
			// end MZ
			else
				return true;	//	nothing to do
		}
		//faaguilar OFB begin
		MProductCategory cat = new MProductCategory(as.getCtx(),new MProduct(as.getCtx(),M_Product_ID,trxName).getM_Product_Category_ID(),trxName);
		boolean noCost=(Boolean)cat.get_Value("NoCost");
		if(noCost)
			cd.set_CustomColumn("Enabled",false);
		//faaguilar OFB end
		//ininoles setear fecha contable producciones		
		
		//end ininoles
		log.config("antes de guardar cd ");
		boolean ok = cd.save();
		log.config("despues de guardar cd y antes de if");
		if (ok && !cd.isProcessed())
		{
			log.config("despues de if1");
			MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
			log.config("antes de if 2");
			if (client.isCostImmediate()){
				//processDetail(cd,as,as.getCtx());
				log.config("despues de if 2");
				log.config("Ctx: "+as.getCtx());
				processCost(M_Product_ID,trxName,as.getCtx());
			}
		}
		log.config("(" + ok + ") " + cd);
		return ok;
	}	//	createProduction
	
	public static boolean createJournal (MAcctSchema as, int AD_Org_ID, 
			int M_Product_ID, int M_AttributeSetInstance_ID,
			int GL_JournalLine_ID, int M_CostElement_ID, 
			BigDecimal Amt, BigDecimal Qty,
			String Description, String trxName)
		{
			//	Delete Unprocessed zero Differences
			String sql = "DELETE  From M_CostDetail "
				+ "WHERE Processed='N' AND COALESCE(DeltaAmt,0)=0 AND COALESCE(DeltaQty,0)=0"
				+ " AND GL_JournalLine_ID=" + GL_JournalLine_ID
				+ " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
			int no = DB.executeUpdate(sql, trxName);
			if (no != 0)
				log.config("Deleted #" + no);
			MCostDetail cd = get (as.getCtx(), "GL_JournalLine_ID=? ", 
					GL_JournalLine_ID, M_AttributeSetInstance_ID, as.getC_AcctSchema_ID(),trxName);
			//
			if (cd == null)		//	createNew
			{
				Amt=Amt.setScale(0, BigDecimal.ROUND_HALF_UP);
				cd = new MCostDetail (as, AD_Org_ID, 
					M_Product_ID, M_AttributeSetInstance_ID, 
					M_CostElement_ID, 
					Amt, Qty, Description, trxName);
				cd.set_Value("GL_JournalLine_ID", GL_JournalLine_ID);
				MJournalLine  jl = new MJournalLine(as.getCtx(), GL_JournalLine_ID ,trxName );
				cd.set_CustomColumn("DateAcct", jl.getDateAcct() );
				cd.set_CustomColumn("Enabled",true);
			}
			else
			{
				// MZ Goodwill
				// set deltaAmt=Amt, deltaQty=qty, and set Cost Detail for Amt and Qty	 
				cd.setDeltaAmt(Amt.subtract(cd.getAmt()));
				cd.setDeltaQty(Qty.subtract(cd.getQty()));
				MJournalLine  jl = new MJournalLine(as.getCtx(), GL_JournalLine_ID ,trxName );
				cd.set_CustomColumn("DateAcct", jl.getDateAcct() );
				if (cd.isDelta())
				{
					cd.setProcessed(false);
					cd.setAmt(Amt);
					cd.setQty(Qty);
				}
				// end MZ
				else
					return true;	//	nothing to do
			}
			boolean ok = cd.save();
			if (ok && !cd.isProcessed())
			{
				MClient client = MClient.get(as.getCtx(), as.getAD_Client_ID());
				if (client.isCostImmediate()){
					//processDetail(cd,as,as.getCtx());
					log.config("Ctx: "+as.getCtx());
					processCost(M_Product_ID,trxName,as.getCtx());
				}
			}
			log.config("(" + ok + ") " + cd);
			return ok;
		}	
	
	/**************************************************************************
	 * 	Get Cost Detail
	 *	@param ctx context
	 *	@param whereClause where clause
	 *	@param ID 1st parameter
	 *  @param M_AttributeSetInstance_ID ASI
	 *	@param trxName trx
	 *	@return cost detail
	 */
	public static MCostDetail get (Properties ctx, String whereClause, 
		int ID, int M_AttributeSetInstance_ID, int C_AcctSchema_ID, String trxName)
	{
		final String localWhereClause = whereClause
			+ " AND C_AcctSchema_ID=?";
		MCostDetail retValue = new Query(ctx,I_M_CostDetail.Table_Name,localWhereClause,trxName)
		.setParameters(ID,C_AcctSchema_ID)
		.first();
		return retValue;
	}	//	get
	
	public static MCostDetail getP (Properties ctx, String whereClause, 
			int ID, int M_AttributeSetInstance_ID, int C_AcctSchema_ID,int M_Product_ID, String trxName)
		{
			final String localWhereClause = whereClause
				+ " AND C_AcctSchema_ID=? AND M_Product_ID=? AND (M_AttributeSetInstance_ID is null or M_AttributeSetInstance_ID=?)";
			MCostDetail retValue = new Query(ctx,I_M_CostDetail.Table_Name,localWhereClause,trxName)
			.setParameters(ID,C_AcctSchema_ID,M_Product_ID,M_AttributeSetInstance_ID)
			.first();
			return retValue;
		}	//	get
	
	/**
	 * costo de producto en base a la ultima factura
	 * se puede llamar desde cualquier proceso Java enviandole los parametros
	 * */
	public static void resetCostToInvoice(int Product_ID, BigDecimal margen, int Org_ID, boolean updateQty, int Warehouse_ID, String trxName,Properties ctx){
		
		if(MProduct.get(ctx, Product_ID).isBOM())
			return;
		
		processCost(Product_ID,trxName,ctx);//proceso los detalles pendientes actualizacion de costo
		
		BigDecimal currentCost = getProductCost(Product_ID,Org_ID,Env.ONE,trxName,ctx);
		BigDecimal currentQty = getProductCostQty(Product_ID,Org_ID,trxName,ctx);
		BigDecimal lastCost = Env.ZERO;
		MInvoice invoice = null;
		
		if(Org_ID>0)
			Org_ID=getParentOrg(Org_ID, trxName,ctx);
			 
			invoice = getLastInvoice(Product_ID, Org_ID, trxName);
		
			if(invoice==null)
				return;
			
		MInvoiceLine[] lines = invoice.getLines();
		for(MInvoiceLine line : lines)
			if(line.getM_Product_ID()==Product_ID)
				lastCost = line.getPriceActual();
		
		
		margen = margen.divide(Env.ONEHUNDRED);
		margen = lastCost.multiply(margen);
		
		
		
		if( (currentCost.compareTo(lastCost.add(margen))>0) || (currentCost.compareTo(lastCost.subtract(margen))<0) ){
			
			BigDecimal qty=Env.ZERO;
			if(updateQty)
				qty=MStorage.getQtyAvailable(Warehouse_ID, 0, Product_ID, 0, trxName);
			
			MCostDetail cd = new MCostDetail (MAcctSchema.getClientAcctSchema(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()))[0], Org_ID, 
					Product_ID, 0, 
					0, 
					lastCost.subtract(currentCost), qty.subtract(currentQty), "Correccion Costo Ultima Factura", trxName);
			cd.set_CustomColumn("Enabled",true);
			cd.save();
			
			processCost(Product_ID,trxName,ctx);//actualizacion de costo
			
		}
			
		
	}
	
	/**
	 * costo de producto bom en base a la ultima factura
	 * se puede llamar desde cualquier proceso Java enviandole los parametros
	 * */
	public static void resetCostBOMToInvoice(int Product_ID, BigDecimal margen, int Org_ID, boolean updateQty, int Warehouse_ID, String trxName, Properties ctx){
		
		if(!MProduct.get(Env.getCtx(), Product_ID).isBOM())
			return;
		
		processCost(Product_ID,trxName,ctx);//proceso los detalles pendientes actualizacion de costo
		
		BigDecimal currentCost = getProductCost(Product_ID,Org_ID,Env.ONE,trxName,ctx);
		BigDecimal lastCost = Env.ZERO;
		
		if(Org_ID>0)
			Org_ID=getParentOrg(Org_ID, trxName,ctx);
			 
		
		margen = margen.divide(Env.ONEHUNDRED);
		margen = lastCost.multiply(margen);
		
		/*********pendiente*****************/
		
		if( (currentCost.compareTo(lastCost.add(margen))>0) || (currentCost.compareTo(lastCost.subtract(margen))<0) ){
			
			BigDecimal qty=Env.ZERO;
			if(updateQty)
				qty=MStorage.getQtyAvailable(Warehouse_ID, 0, Product_ID, 0, trxName);
			
			MCostDetail cd = new MCostDetail (MAcctSchema.getClientAcctSchema(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()))[0], Org_ID, 
					Product_ID, 0, 
					0, 
					lastCost.subtract(currentCost), qty, "Correccion Costo Ultima Factura", trxName);
			cd.save();
			
			processCost(Product_ID,trxName,ctx);//actualizacion de costo
			
		}
			
		
	}
	
	/**
	 * costo de producto en base a la ultima Orden de Compra
	 * se puede llamar desde cualquier proceso Java enviandole los parametros
	 * */
	public static void resetCostToPO(int Product_ID, BigDecimal margen, int Org_ID, boolean updateQty,int Warehouse_ID, String trxName,Properties ctx){
		
		processCost(Product_ID,trxName,ctx);//proceso los detalles pendientes actualizacion de costo
		
		BigDecimal currentCost = getProductCost(Product_ID,Org_ID,Env.ONE,trxName,ctx);
		BigDecimal currentQty = getProductCostQty(Product_ID,Org_ID,trxName,ctx);
		BigDecimal lastCost = Env.ZERO;
		MOrder order = null;
		
		if(Org_ID>0)
			Org_ID=getParentOrg(Org_ID, trxName,ctx);
			 
			order = getLastPO(Product_ID, Org_ID, trxName);
		
			if(order == null)
				return;
			
		MOrderLine[] lines = order.getLines();
		for(MOrderLine line : lines)
			if(line.getM_Product_ID()==Product_ID)
				lastCost = line.getPriceActual();
		
		
		margen = margen.divide(Env.ONEHUNDRED);
		margen = lastCost.multiply(margen);
		
		
		
		if( (currentCost.compareTo(lastCost.add(margen))>0) || (currentCost.compareTo(lastCost.subtract(margen))<0) ){
			
			BigDecimal qty=Env.ZERO;
			if(updateQty)
				qty=MStorage.getQtyAvailable(Warehouse_ID, 0, Product_ID, 0, trxName);
			
			MCostDetail cd = new MCostDetail (MAcctSchema.getClientAcctSchema(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()))[0], Org_ID, 
					Product_ID, 0, 
					0, 
					lastCost.subtract(currentCost), qty.subtract(currentQty), "Correccion Costo Ultima PO", trxName);
			cd.set_CustomColumn("Enabled",true);
			cd.save();
			
			processCost(Product_ID,trxName,ctx);//actualizacion de costo
			
		}
		
		
	}
	
	/**
	 * trae la ultima factura de compra del producto*/
	public static MInvoice getLastInvoice(int Product_ID, int Org_ID, String trxName){
		
		String sqlWhere=" C_Invoice.docstatus IN ('CO','CL') and C_Invoice.IsSoTrx='N' "
			+" and exists (select * from c_invoiceline where c_invoiceline.c_invoice_id=c_invoice.c_invoice_id"
			+" and c_invoiceline.m_product_id=? and c_invoiceline.qtyinvoiced>0)";
		
		if(Org_ID>0)
			sqlWhere+=" and C_Invoice.AD_Org_ID="+Org_ID;
		
		Query qy = new  Query(Env.getCtx(),I_C_Invoice.Table_Name,sqlWhere,trxName);
		qy = qy.setParameters(Product_ID);
		qy = qy.setOrderBy(" C_Invoice.DateInvoiced Desc");
		MInvoice invoice = qy.first();
		return invoice;
		
	}
	
	/**
	 * trae la ultima orden de compra del producto*/
	public static MOrder getLastPO(int Product_ID,int Org_ID, String trxName){
		
		String sqlWhere=" C_Order.docstatus IN ('CO','CL') and C_Order.IsSoTrx='N' "
			+" and exists (select * from C_Orderline where C_Orderline.C_Order_id=C_Order.C_Order_id"
			+" and C_Orderline.m_product_id=? and C_Orderline.qtyOrdered>0)";
		
		if(Org_ID>0)
			sqlWhere+=" and C_Order.AD_Org_ID="+Org_ID;
		
		Query qy = new  Query(Env.getCtx(),I_C_Order.Table_Name,sqlWhere,trxName);
		qy = qy.setParameters(Product_ID);
		qy = qy.setOrderBy(" C_Invoice.DateOrdered Desc");
		
		MOrder order = qy.first();
		return order;
	}
	
	/*trae el elemento de costo para el costoestandard*/
	public static MCostElement getStandarCostelement(Properties ctx){
		
		return MCostElement.getByCostingMethod(ctx, MCostElement.COSTINGMETHOD_StandardCosting).get(0);
	}
	
	public static MCost get (Properties ctx, int AD_Client_ID, int AD_Org_ID, int M_Product_ID,
			int M_CostType_ID, int C_AcctSchema_ID, int M_CostElement_ID,
			int M_AttributeSetInstance_ID,
			String trxName)
		{
			final String whereClause = "AD_Client_ID=? AND AD_Org_ID=?"
										+" AND "+MCost.COLUMNNAME_M_Product_ID+"=?"
										+" AND "+MCost.COLUMNNAME_M_CostType_ID+"=?"
										+" AND "+MCost.COLUMNNAME_C_AcctSchema_ID+"=?"
										+" AND "+MCost.COLUMNNAME_M_CostElement_ID+"=?";
										//+" AND "+MCost.COLUMNNAME_M_AttributeSetInstance_ID+"=?";
			final Object[] params = new Object[]{AD_Client_ID, AD_Org_ID, M_Product_ID,
													M_CostType_ID, C_AcctSchema_ID,
													 M_CostElement_ID};//,M_AttributeSetInstance_ID};
			return new Query(ctx, MCost.Table_Name, whereClause, trxName)
						.setOnlyActiveRecords(true)
						.setParameters(params)
						.first();
		}	//	get
	
	/**
	 *  trae el costo que uso un documento, si el documento no tiene historia de costo, trae el costo actual
	 *  se llama desde Doc_InOut, Doc_Movement
	 *  @param as accounting schema
	 *  @param AD_Org_ID trx org
	 *	@param zeroCostsOK zero/no costs are OK
	 *	@param whereClause null are OK
	 *  @return costs
	 */
	public static BigDecimal getProductCosts (MAcctSchema as, String whereClause, DocLine line,int C_AcctSchema_ID, String trxName, Properties ctx )
	{
		
		if (whereClause != null)
		{
			MCostDetail cd = MCostDetail.get (ctx, whereClause, 
					line.get_ID(),line.getM_AttributeSetInstance_ID(),C_AcctSchema_ID, trxName);
			if (cd != null)
				return cd.getAmt();
		}
		return getProductCost(line.getM_Product_ID(), line.getAD_Org_ID(),line.getQty() , trxName, ctx);
	}   //  getProductCosts
}
