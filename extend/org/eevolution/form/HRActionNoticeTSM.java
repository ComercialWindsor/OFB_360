/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Contributor(s): Oscar Gomez & Victor Perez www.e-evolution.com             *
 * Copyright (C) 2003-2011 e-Evolution,SC. All Rights Reserved.               *
 *****************************************************************************/
package org.eevolution.form;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.IMiniTable;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 *  @author victor.perez@e-evolution.com, www.e-evolution.com
 *  @author alberto.juarez@e-evolution.com, www.e-evolution.com
 */
public class HRActionNoticeTSM
{

	
	/**	Logger			*/
	protected static CLogger log = CLogger.getCLogger(HRActionNoticeTSM.class);
	//
	/**	Window No			*/
	public int         	m_WindowNo = 0;

	//ininoles variables 
	public static String m_TSM_ValueType = "A";
	public static int m_TSM_BPartner_ID = 0;
	public static int m_TSM_Asset_ID = 0;
	public static int m_TSM_Org_ID = 0;
	public static int sTSMPreBitacora_ID = 0;

	public void dynInit() throws Exception
	{
		log.info("HRActionNoticeTSM");
	}

	public void configureMiniTable(IMiniTable miniTable)
	{	
		miniTable.addColumn("HR_Movement_ID"); 		// 0
		miniTable.addColumn("AD_Org_ID");			// 1
		miniTable.addColumn("HR_Concept_TSM_ID");	// 2
		miniTable.addColumn("DateTrx");				// 3
		miniTable.addColumn("Workshift");			// 4
		
		//  set details
		miniTable.setColumnClass(0, IDColumn.class, false, " ");
		miniTable.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "AD_Org_ID"));
		miniTable.setColumnClass(2, String.class, true, "Concepto");
		miniTable.setColumnClass(3, Timestamp.class, true,"Feha Inicio");
		miniTable.setColumnClass(4, String.class, true, "Turno");
		//
		miniTable.autoSize();
	}

	public static void executeQueryTSM(Properties ctx ,IMiniTable miniTable, String type)
	{	
		StringBuffer sqlQuery = new StringBuffer("SELECT org.name," +	//orgname
				" c.name, pb.datetrx, "+	//concepto, fecha
				" (SELECT rl.name FROM AD_Ref_List rl " +
				" INNER JOIN AD_Reference ref ON (rl.ad_reference_id = ref.ad_reference_id)" +
				" WHERE ref.name like 'Workshift' AND rl.value = pb.Workshift) as Workshift" + // turno
				" FROM HR_Prebitacora pb" +
				" INNER JOIN AD_Org org ON (pb.AD_Org_ID = org.AD_Org_ID)" +
				" INNER JOIN HR_Concept_TSM c ON (pb.HR_Concept_TSM_ID = c.HR_Concept_TSM_ID)" +
				" WHERE pb.IsActive = 'Y' AND pb.AD_Client_ID = 1000000 AND pb.processed = 'N' ");
				if (type.compareToIgnoreCase("A") == 0)
					sqlQuery = sqlQuery.append(" AND pb.a_asset_id = "+m_TSM_Asset_ID);
				if (type.compareToIgnoreCase("B") == 0)
					sqlQuery = sqlQuery.append(" AND pb.C_BPartner_ID = "+m_TSM_BPartner_ID);
		sqlQuery.append(" ORDER BY pb.datetrx,org.name,c.name");
		//  reset table
		int row = 0;
		miniTable.setRowCount(row);
		//  Execute
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sqlQuery.toString(), null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				//  extend table
				miniTable.setRowCount(row+1);
				//  set values
				miniTable.setColumnClass(0, IDColumn.class, false, " ");
				miniTable.setValueAt(rs.getString(1), row, 1);		// orgname
				miniTable.setValueAt(rs.getString(2), row, 2);		// concepto
				miniTable.setValueAt(rs.getTimestamp(3), row, 3);	// fecha
				miniTable.setValueAt(rs.getString(4), row, 4);		// turno
				//  prepare next
				row++;
			}
		}
		catch (SQLException e) {
			log.log(Level.SEVERE, sqlQuery.toString(), e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		miniTable.autoSize();
	}   //  executeQueryTSMEmployee			
	/**
	 *  get Tipo TSM
	 */
	public static KeyNamePair[] getTypeTSM()
	{
		String sql = "SELECT rl.AD_Ref_List_ID, rl.name FROM AD_Ref_List rl " +
				" INNER JOIN AD_Reference ref ON (rl.ad_reference_id = ref.ad_reference_id)" +
				" WHERE ref.name like 'ColumnType' AND rl.IsActive = 'Y'";
		return DB.getKeyNamePairs(sql, true);
	} //getTypeTSM
	
	/**
	 *  get Turno TSM
	 */
	public static KeyNamePair[] getWorkshiftTSM()
	{
		String sql = "SELECT rl.AD_Ref_List_ID, rl.name FROM AD_Ref_List rl " +
				" INNER JOIN AD_Reference ref ON (rl.ad_reference_id = ref.ad_reference_id)" +
				" WHERE ref.name like 'Workshift' AND rl.IsActive = 'Y' ORDER BY rl.value, rl.name";
		return DB.getKeyNamePairs(sql, true);
	} //getWorkshiftTSM
	
	public static KeyNamePair[] getConceptTSM()
	{
		String sql = "SELECT HR_Concept_TSM_ID, value||'-'||name FROM HR_Concept_TSM WHERE IsActive = 'Y'";
		return DB.getKeyNamePairs(sql, true);
	} //getWorkshiftTSM
	
	public static KeyNamePair[] getConceptTSM(String type)
	{
		String sql = "SELECT HR_Concept_TSM_ID, value||'-'||name FROM HR_Concept_TSM WHERE IsActive = 'Y'";
		if (type != null && type != "" && type != " ")
		{
			if(type.compareToIgnoreCase("A") == 0)
				sql = sql + " AND value like 'AS_%' ";
			else if(type.compareToIgnoreCase("B") == 0)
				sql = sql + " AND value like 'BP_%' ";
		}
		return DB.getKeyNamePairs(sql, true);
	} //getWorkshiftTSM

	public static List<KeyNamePair> getEmployeeTSM()
	{
		List<KeyNamePair> list = new ArrayList<KeyNamePair>();
			
		KeyNamePair pp = new KeyNamePair(0, "");
		list.add(pp);
		String sql = "SELECT DISTINCT bp.C_BPartner_ID,bp.value||'-'||digito||' '||bp.Name, " +
				" bp.value from  C_BPartner bp WHERE IsActive = 'Y' AND IsEmployee = 'Y' " +
				" AND AD_Client_ID = 1000000 ORDER BY bp.value";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				list.add(pp);
			}
			return list;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return list;
		}
		finally
		{
			DB.close(rs, pstmt);
		    rs = null; pstmt = null;
		}		
	} //getEmployeeTSM
	public static List<KeyNamePair> getEmployeeTSMOrg(int ID_Org)
	{
		List<KeyNamePair> list = new ArrayList<KeyNamePair>();
			
		KeyNamePair pp = new KeyNamePair(0, "");
		list.add(pp);
		String sql = "SELECT DISTINCT bp.C_BPartner_ID,bp.value||'-'||digito||' '||bp.Name, " +
				" bp.value from  C_BPartner bp WHERE IsActive = 'Y' AND IsEmployee = 'Y' " +
				//" AND AD_Client_ID = 1000000 AND AD_Org_ID = "+ID_Org+" ORDER BY bp.value";
				" AND AD_Client_ID = 1000000 AND AD_OrgRef_ID = "+ID_Org+" ORDER BY bp.value";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				list.add(pp);
			}
			return list;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return list;
		}
		finally
		{
			DB.close(rs, pstmt);
		    rs = null; pstmt = null;
		}		
	} //getEmployeeTSM
	public static List<KeyNamePair> getAssetTSM()
	{
		List<KeyNamePair> list = new ArrayList<KeyNamePair>();
			
		KeyNamePair pp = new KeyNamePair(0, "");
		list.add(pp);
		String sql = "SELECT DISTINCT a.A_Asset_ID,a.value||' '||a.Name, a.value " +
				"from  A_Asset a WHERE IsActive = 'Y' AND AD_Client_ID = 1000000 ORDER BY a.value";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				list.add(pp);
			}
			return list;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return list;
		}
		finally
		{
			DB.close(rs, pstmt);
		    rs = null; pstmt = null;
		}		
	} //getAssetTSM
	public static List<KeyNamePair> getAssetTSMOrg(int ID_Org)
	{
		List<KeyNamePair> list = new ArrayList<KeyNamePair>();
			
		KeyNamePair pp = new KeyNamePair(0, "");
		list.add(pp);
		String sql = "SELECT DISTINCT a.A_Asset_ID,a.value||' '||a.Name, a.value " +
				" from  A_Asset a WHERE IsActive = 'Y' AND a.AD_Client_ID = 1000000 " +
				//" AND a.AD_Org_ID = "+ID_Org+" ORDER BY a.value";
				" AND a.AD_OrgRef_ID = "+ID_Org+" ORDER BY a.value";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				list.add(pp);
			}
			return list;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return list;
		}
		finally
		{
			DB.close(rs, pstmt);
		    rs = null; pstmt = null;
		}		
	} //getAssetTSM
	
	public static List<KeyNamePair> getOrgTSM()
	{
		List<KeyNamePair> list = new ArrayList<KeyNamePair>();
			
		KeyNamePair pp = new KeyNamePair(0, "");
		list.add(pp);
		String sql = "SELECT DISTINCT o.AD_Org_ID ,o.Name " +
				"from  AD_Org o WHERE IsActive = 'Y' AND AD_Client_ID = 1000000 ORDER BY o.Name";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				pp = new KeyNamePair(rs.getInt(1), rs.getString(2));
				list.add(pp);
			}
			return list;
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return list;
		}
		finally
		{
			DB.close(rs, pstmt);
		    rs = null; pstmt = null;
		}		
	} //getAssetTSM

	
}   //  VHRActionNotice