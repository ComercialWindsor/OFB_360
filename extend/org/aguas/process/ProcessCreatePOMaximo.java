/******************************************************************************
0 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.aguas.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.*;

import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	Proceso Borrado Costeo Flota Petrobras
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class ProcessCreatePOMaximo extends SvrProcess
{		
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{			
		String sqlCreateR = "SELECT ca.WONUM,ca.LOCATION,ca.EQNUM,ca.REPORTDATE,ca.STATUS,ca.ORGID,ca.SITEID," +
				" de.WONUM,de.LOCATION,de.STORELOC,de.REPORTDATE as REPORTDATE_DET,de.ITEMNUM,de.DESCRIPTION,de.ORDERUNIT," +
				" de.GLACCOUNT,de.TIPOTRANS,de.ISSUETYPE,de.ITEMQTY,de.UNITCOST,de.LINECOST,de.ORGID as ORGID_DET ," +
				" de.SITEID as SITEID_DET, SLXINTER_PM_CID, SLXINTER_PM_DID" +
				" FROM SLXINTER_PM_C@DBL_MAXIMO_MAXDEMO_INACALIRI ca" +
				" INNER JOIN SLXINTER_PM_D@DBL_MAXIMO_MAXDEMO_INACALIRI de" +
				" ON (ca.WONUM = de.WONUM)" +
				" WHERE ca.SLXINTER_PM_CID = de.SLXINTER_PM_CID"+ 
				" AND SLXINTER_PM_CID NOT IN (SELECT SLXINTER_PM_CID FROM T_IDS_MAXIMO WHERE PROCESSED = 'Y')" +
				" AND SLXINTER_PM_DID NOT IN (SELECT SLXINTER_PM_DID FROM T_IDS_MAXIMO WHERE PROCESSED = 'Y')" +
				" ORDER BY ca.WONUM";
		
		String num_ot = "";
		try
		{
			
			PreparedStatement pstmt = DB.prepareStatement (sqlCreateR.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			MRequisition req = null;
			while (rs.next ())
			{
				
				if (num_ot.compareTo(rs.getString("WONUM")) != 0)
				{
					req = new MRequisition(getCtx(), 0, get_TrxName());
					req.set_CustomColumn("numot", rs.getString("WONUM"));
					int id_warehouse = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_Warehouse_ID) FROM M_Warehouse WHERE NAME like '"+rs.getString("LOCATION")+"'");
					if (id_warehouse > 0)
						req.setM_Warehouse_ID(id_warehouse);
					else
						req.setM_Warehouse_ID(1000003);
					int id_org = DB.getSQLValue(get_TrxName(), "SELECT MAX(AD_Org_ID) FROM AD_Org WHERE NAME like '"+rs.getString("ORGID")+"'");
					if (id_org > 0)
						req.setAD_Org_ID(id_org);						
					req.setDateRequired(rs.getTimestamp("REPORTDATE"));
					int id_PList = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_PriceList_ID) FROM M_PriceList WHERE IsDefault = 'Y' AND IsSOPriceList = 'N'");
					if (id_PList > 0)
						req.setM_PriceList_ID(id_PList);					
					req.setC_DocType_ID(1000018);
					req.setDescription("Generada Automaticamente Desde Maximo");
					req.save();
				}	
				MRequisitionLine reqLine = new MRequisitionLine(getCtx(), 0, get_TrxName());
				reqLine.setM_Requisition_ID(req.get_ID());
				int id_product = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_Product_ID) FROM M_Product WHERE VALUE like '"+rs.getString("ITEMNUM")+"'");
				if (id_product > 0)
				{					
					reqLine.setM_Product_ID(id_product);
				}else
				{	
					reqLine.set_CustomColumn("help","Error - Producto No Encontrado. Value: "+rs.getString("ITEMNUM"));
					int ID_Charge = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Charge_ID) FROM C_Charge cc INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) WHERE ct.value like 'TCRM'");
					if (ID_Charge > 0)
						reqLine.setC_Charge_ID(ID_Charge);
					else
						reqLine.setC_Charge_ID(1000000);
				}					
				reqLine.setDescription(rs.getString("DESCRIPTION"));
				reqLine.setQty(rs.getBigDecimal("ITEMQTY"));
				reqLine.setPriceActual(rs.getBigDecimal("UNITCOST"));
				int id_orgDet = DB.getSQLValue(get_TrxName(), "SELECT MAX(AD_Org_ID) FROM AD_Org WHERE NAME like '"+rs.getString("ORGID_DET")+"'");
				if (id_orgDet > 0)
					reqLine.setAD_Org_ID(id_orgDet);						
				reqLine.save();
				
				num_ot = rs.getString("WONUM");
								
				String InsertIDS=new String("INSERT INTO T_IDS_MAXIMO (AD_CLIENT_ID,AD_Org_ID,IsActive, Created, CreatedBy, Updated, UpdatedBy, SLXINTER_PM_CID,SLXINTER_PM_DID,PROCESSED,M_REQUISITIONLINE_ID) " +						
						" VALUES ("+Env.getAD_Client_ID(getCtx())+","+Env.getAD_Org_ID(getCtx())+",'Y',SYSDATE,100,SYSDATE,100,"+rs.getInt("SLXINTER_PM_CID")+","+rs.getInt("SLXINTER_PM_DID")+",'Y',"+reqLine.get_ID()+")");
				
				DB.executeUpdate(InsertIDS, get_TrxName());
			}					
				
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "BP - " + sqlCreateR.toString(), e);
		}
		
		return "";
	}	//	doIt
	
}	//	OrderOpen
