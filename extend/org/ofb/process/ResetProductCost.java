/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.ofb.process;

import java.sql.*;
import java.util.logging.*;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ResetAcct.java,v 1.2 2008/07/30 00:51:01 jjanke Exp $
 */
public class ResetProductCost extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 
	/**	Product				*/
	private int 	p_M_Product_ID = 0; 
	
	private int 	p_M_Product_Category_ID=0;
	
	private Timestamp p_from;
	
	private String p_CostingLevel="C";
	 
	 protected void prepare()
	{
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("M_Product_ID"))
				p_M_Product_ID = para[i].getParameterAsInt();
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = para[i].getParameterAsInt();
			else if (name.equals("DateFrom"))
				p_from = (Timestamp)para[i].getParameter();
			else if (name.equals("CostingLevel"))
				p_CostingLevel = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
	  boolean finCorrecto=false;
	  String sql= "SELECT "+
		"p.M_PRODUCT_ID, "+
		"c.M_INVENTORYLINE_ID, "+ 
		"c.C_INVOICELINE_ID, "+
		"c.M_INOUTLINE_ID, "+
		"c.M_PRODUCTIONLINE_ID "+ 
		"FROM M_COSTDETAIL c  "+
		" Inner Join M_Product p on (c.M_Product_ID=p.M_Product_ID)"+
		"WHERE  DATEACCT >= ? ";	
	  
	  if(p_M_Product_ID>0)
		  sql+="and p.M_Product_ID =?";
		  
	  if(p_M_Product_Category_ID>0)
		  sql+="and p.M_Product_ID in (select m_product_id from m_product where M_Product_Category_ID=?)";
		  
	  PreparedStatement pstmt = null;
	  ResultSet rs = null;	
	  try
      {
		    pstmt = DB.prepareStatement (sql, get_TrxName());
		    pstmt.setTimestamp(1, p_from);
		    if(p_M_Product_ID>0)
		    	pstmt.setInt(2, p_M_Product_ID);
		    if(p_M_Product_Category_ID>0)
		    	pstmt.setInt(2, p_M_Product_Category_ID);
		    
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				
				//inicio unposte de documentos
				if(rs.getInt("M_INVENTORYLINE_ID")>0)
				{
					String update="UPDATE M_INVENTORY "+
					"SET POSTED='N',PROCESSING='N' WHERE M_INVENTORY_ID IN  (SELECT M_INVENTORY_ID FROM M_INVENTORYLINE WHERE M_INVENTORYLINE_ID=?)";
					DB.executeUpdate(update,rs.getInt("M_INVENTORYLINE_ID"), get_TrxName());
				}
	        
	        	if(rs.getInt("M_INOUTLINE_ID")>0)
	        	{
	        		String update="UPDATE M_INOUT "+
	        		"SET POSTED='N',PROCESSING='N' WHERE M_INOUT_ID IN  (SELECT M_INOUT_ID FROM M_INOUTLINE WHERE M_INOUTLINE_ID=?)";
	        		DB.executeUpdate(update,rs.getInt("M_INOUTLINE_ID"), get_TrxName());
	        	}

	        	if(rs.getInt("C_INVOICELINE_ID")>0)
	        	{
	        		String update="UPDATE C_INVOICE "+
	        		"SET POSTED='N',PROCESSING='N' WHERE C_INVOICE_ID IN  (SELECT C_INVOICE_ID FROM C_INVOICELINE WHERE C_INVOICELINE_ID=?)";
	        		DB.executeUpdate(update,rs.getInt("C_INVOICELINE_ID"), get_TrxName());
	        		
	        		String update2="UPDATE M_MATCHINV "+
	               "SET POSTED='N',PROCESSING='N' WHERE C_INVOICELINE_ID =?";
	        		DB.executeUpdate(update2,rs.getInt("C_INVOICELINE_ID"), get_TrxName());
	        	}
	        
	        	if(rs.getInt("M_PRODUCTIONLINE_ID")>0) 
	        	{
	        		String update="UPDATE M_PRODUCTION "+
	        		"SET POSTED='N',PROCESSING='N' WHERE M_PRODUCTION_ID IN  (SELECT M_PRODUCTION_ID FROM M_PRODUCTIONLINE WHERE M_PRODUCTIONLINE_ID=?)";
	        		DB.executeUpdate(update,rs.getInt("M_PRODUCTIONLINE_ID"), get_TrxName());
	        	}
				//fin unpost	
	        	
	        	//borrando detalles de costo
	      	  	String update="DELETE FROM M_COSTDETAIL "+
	      	    "WHERE DATEACCT > ? and DateAcct is not null And M_Product_ID IN ?";
	      	  	DB.executeUpdateEx(update,  new Object[]{p_from,rs.getInt("M_PRODUCT_ID")} , get_TrxName());
	      	   //fin borrado detalles
	        	
	      	  	//actualizo cabeceras de costo
	      	  	
	      	  	if(p_CostingLevel.equals("O"))//a nivel de organizacion
	      	  	{
	      	  		update="update m_cost C "+
		          "set C.cumulatedamt= (select coalesce(sum(cd.amt),0) from m_costdetail cd inner join ad_orginfo org on (cd.ad_org_id=org.ad_org_id) "+
		          "where cd.m_product_id=c.m_product_id and cd.c_orderline_id is null and ( c.ad_org_id=cd.ad_org_id or c.ad_org_id=org.PARENT_ORG_ID )), "+
		          "C.cumulatedqty= (select coalesce(sum(cd.qty),0) from m_costdetail cd inner join ad_orginfo org on (cd.ad_org_id=org.ad_org_id) "+
		          "where cd.m_product_id=c.m_product_id  and cd.c_invoiceline_id is null and cd.c_orderline_id is null and ( c.ad_org_id=cd.ad_org_id or c.ad_org_id=org.PARENT_ORG_ID ) ) "+
		          "where c.m_product_id = ?";
		          
	      	  		DB.executeUpdate(update, rs.getInt("M_PRODUCT_ID"), get_TrxName());
	
	      	  		update="update m_cost "+
		          "set CURRENTQTY = cumulatedqty, "+
		          "CURRENTCOSTPRICE = round(cumulatedamt/decode(cumulatedqty,0,1,cumulatedqty),0) "+
		          "where m_product_id = ?";
	      	  		
	      	  		DB.executeUpdate(update, rs.getInt("M_PRODUCT_ID"), get_TrxName());
	      	  	}
	      	  	else //a nivel de cliente
	      	  	{
	      	  			update="update m_cost C "+
					    "set C.cumulatedamt= (select coalesce(sum(cd.amt),0) from m_costdetail cd  "+
					    "where cd.m_product_id=c.m_product_id and cd.c_orderline_id is null and (cd.M_CostElement_ID=1000000 or cd.M_CostElement_ID is null )),  "+
					    "C.cumulatedqty= (select coalesce(sum(cd.qty),0) from m_costdetail cd  "+
					    "where cd.m_product_id=c.m_product_id  and cd.c_invoiceline_id is null and cd.c_orderline_id is null and (cd.M_CostElement_ID=1000000 or cd.M_CostElement_ID is null ) )  "+
					    "where c.M_CostElement_ID=1000000 and c.m_product_id IN (?)";
	  		          
	  	      	  		DB.executeUpdate(update, rs.getInt("M_PRODUCT_ID"), get_TrxName());
	  	
	  	      	  		update="update m_cost C "+
					    "set C.cumulatedamt= (select coalesce(sum(cd.amt),0) from m_costdetail cd "+ 
					    "where cd.m_product_id=c.m_product_id and cd.c_orderline_id is null and cd.M_CostElement_ID=c.M_CostElement_ID ), "+
					    "C.cumulatedqty= (select coalesce(sum(cd.qty),0) from m_costdetail cd "+ 
					    "where cd.m_product_id=c.m_product_id  and cd.c_invoiceline_id is null and cd.c_orderline_id is null and cd.M_CostElement_ID=c.M_CostElement_ID  ) "+
					    "where c.M_CostElement_ID!=1000000 and  c.m_product_id IN (?)";
	  	      	  		
	  	      	  		DB.executeUpdate(update, rs.getInt("M_PRODUCT_ID"), get_TrxName());
	  	      	  		
	  	      	  		update="update m_cost "+
	  	      	  		"set CURRENTQTY = cumulatedqty, "+
	  			        "CURRENTCOSTPRICE = round(cumulatedamt/decode(cumulatedqty,0,1,cumulatedqty),0) "+
	  			        "where m_product_id = ?";
	  		      	  		
	  		      	   DB.executeUpdate(update, rs.getInt("M_PRODUCT_ID"), get_TrxName());
	      	  		
	      	  	}
	      	  	//fin cabeceras
	        	finCorrecto=true;
				
			}
		  
      }
	  catch (Exception e)
		{
			log.severe(e.getMessage());
		} 	
	  
	//borrado de contabilidad
	  if(finCorrecto)
	  {
		  String update="DELETE FROM FACT_ACCT F "+
	      "WHERE F.AD_TABLE_ID = 318 AND "+
	      "EXISTS (SELECT * FROM C_INVOICE WHERE C_INVOICE_ID=F.RECORD_ID AND POSTED='N' And DateAcct>?)";
		  DB.executeUpdateEx(update,  new Object[]{p_from} , get_TrxName());
	
		  update="DELETE FROM FACT_ACCT F "+
	      "WHERE F.AD_TABLE_ID = 319 AND "+
	      "EXISTS (SELECT * FROM M_InOut WHERE M_InOut_ID=F.RECORD_ID AND POSTED='N' And DateAcct>? )";
		  DB.executeUpdateEx(update,  new Object[]{p_from} , get_TrxName());
	
		  update="DELETE FROM FACT_ACCT F "+
	      "WHERE F.AD_TABLE_ID = 321 AND "+
	      "EXISTS (SELECT * FROM M_Inventory WHERE M_Inventory_ID=F.RECORD_ID AND POSTED='N' And movementdate>?)";
		  DB.executeUpdateEx(update,  new Object[]{p_from} , get_TrxName());
	
		  update="DELETE FROM FACT_ACCT F "+
	      "WHERE F.AD_TABLE_ID = 472 AND "+
	      "EXISTS (SELECT * FROM M_MATCHINV WHERE M_MATCHINV_ID=F.RECORD_ID AND POSTED='N' And DateAcct>?)";
		  DB.executeUpdateEx(update,  new Object[]{p_from} , get_TrxName());
	      
		  update="DELETE FROM FACT_ACCT F "+
	      "WHERE F.AD_TABLE_ID = 325 AND "+
	      "EXISTS (SELECT * FROM M_PRODUCTION WHERE M_PRODUCTION_ID=F.RECORD_ID AND POSTED='N' And movementdate>? )";
		  DB.executeUpdateEx(update,  new Object[]{p_from} , get_TrxName());
	  }
	//fin borrado contabilidad
	  
	  this.commitEx();
		
	   return "Reset Completo";
	}	//	doIt


	
}	//	ResetAcct
