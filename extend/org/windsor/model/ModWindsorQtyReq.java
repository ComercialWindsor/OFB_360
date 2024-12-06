/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
package org.windsor.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_M_StorageReservation;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company WINDSOR
 *
 *  @author Italo Niñoles
 */
public class ModWindsorQtyReq implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModWindsorQtyReq ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModWindsorQtyReq.class);
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
		engine.addModelChange(MOrderLine.Table_Name, this);

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if(type == TYPE_BEFORE_NEW && po.get_Table_ID()== MOrderLine.Table_ID)  
		{
			MOrderLine oLine = (MOrderLine) po;
			if(oLine.getParent().isSOTrx())
			{
				if(oLine.get_ValueAsInt("M_RequisitionLine_ID") > 0)
				{
					MRequisitionLine rline = new MRequisitionLine(po.getCtx(), oLine.get_ValueAsInt("M_RequisitionLine_ID"), po.get_TrxName());
					BigDecimal qtyUsed = (BigDecimal)rline.get_Value("QtyUsed");
					qtyUsed = qtyUsed.setScale(4,RoundingMode.HALF_EVEN);
					BigDecimal qtyAv = rline.getQty();					
					qtyAv = qtyAv.setScale(4,RoundingMode.HALF_EVEN);
					qtyAv = qtyAv.subtract(qtyUsed);
					if(qtyAv.subtract(oLine.getQtyOrdered()).compareTo(Env.ZERO) < 0) 
						return "Error: Cantidad supera cantidad de solicitud. Favor ingrese otra linea";
				}
			}
		}
		
		if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID()== MOrderLine.Table_ID)  
		{
			MOrderLine oLine = (MOrderLine) po;
			if(oLine.getParent().isSOTrx())
			{
				if(oLine.get_ValueAsInt("M_RequisitionLine_ID") > 0)
				{
					MRequisitionLine rline = new MRequisitionLine(po.getCtx(), oLine.get_ValueAsInt("M_RequisitionLine_ID"), po.get_TrxName());
					BigDecimal qtyUse = DB.getSQLValueBD(po.get_TrxName(), "SELECT COALESCE(SUM(QtyOrdered),0) FROM C_OrderLine col" +
							" INNER JOIN C_Order co ON (col.C_Order_ID = co.C_Order_ID) WHERE co.DocStatus IN ('CO','DR','IP','IN')" +
							" AND M_RequisitionLine_ID = "+oLine.get_ValueAsInt("M_RequisitionLine_ID")+
							" AND C_OrderLine_ID <> "+oLine.get_ID());
					if(qtyUse == null)
						qtyUse = Env.ZERO;
					qtyUse = qtyUse.add(oLine.getQtyOrdered());
					BigDecimal qtyAv = rline.getQty().subtract(qtyUse);
					if(qtyAv.compareTo(Env.ZERO) < 0) 
						return "Error: Cantidad supera cantidad de solicitud. Favor ingrese otra linea";
				}
			}
		}
		return null;
	}	//	modelChange
	
	public static String rtrim(String s, char c) {
	    int i = s.length()-1;
	    while (i >= 0 && s.charAt(i) == c)
	    {
	        i--;
	    }
	    return s.substring(0,i+1);
	}

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


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString

	public void OFBReservation(int M_WareHouse_ID, int M_Product_ID, BigDecimal ordered, BigDecimal reserved, PO po)
	{
		PreparedStatement pstmt = null;
		
		String mysql="SELECT * from M_StorageReservation where M_WareHouse_ID = ? and M_Product_ID = ?";
		try
		{
			pstmt = DB.prepareStatement(mysql, po.get_TrxName());
			pstmt.setInt(1, M_WareHouse_ID);
			pstmt.setInt(2, M_Product_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
				X_M_StorageReservation storage = new X_M_StorageReservation (po.getCtx(),rs,po.get_TrxName());
					storage.setQtyReserved(storage.getQtyReserved().subtract(reserved));
					//storage.setQtyOrdered(storage.getQtyOrdered().subtract(ordered));
					storage.save();
			}
			else{
				X_M_StorageReservation storage = new X_M_StorageReservation (po.getCtx(),0,po.get_TrxName() );
				storage.setQtyReserved(reserved.negate());
				//storage.setQtyOrdered(ordered);
				storage.setM_Product_ID(M_Product_ID);
				storage.setM_Warehouse_ID(M_WareHouse_ID);
				storage.save();
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}	
	public boolean existReservationTable(String Trx)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean val = false;
		
		String mysql="SELECT count(1) from M_StorageReservation";
		
		if(!DB.isOracle())
			mysql = "select count(1) from AD_Table where tablename='M_StorageReservation'";
		try
		{
			pstmt = DB.prepareStatement(mysql,Trx);
			rs = pstmt.executeQuery();
			if (rs.next())
				if(rs.getInt(1)>0)
					val = true;
			
		}
		catch (Exception e)
		{
			
			val = false;
		}
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return val;
	}
}	