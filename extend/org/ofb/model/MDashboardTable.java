package org.ofb.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.compiere.model.Query;
import org.compiere.model.X_OFB_DashboardTable;
import org.compiere.model.X_OFB_DashboardTableCol;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MDashboardTable extends X_OFB_DashboardTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4850406201098810602L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context 
	 *	@param M_Inventory_ID id
	 *	@param trxName transaction
	 */
	public MDashboardTable(Properties ctx, int OFB_DashboardTable_ID,
			String trxName) {
		super(ctx, OFB_DashboardTable_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MDashboardTable (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}
	
	public X_OFB_DashboardTableCol[] getTableCols()
	{
		StringBuffer whereClauseFinal = new StringBuffer(X_OFB_DashboardTableCol.COLUMNNAME_OFB_DashboardTable_ID+"=? and "+ X_OFB_DashboardTableCol.COLUMNNAME_IsActive+"='Y'");
		
		String	orderClause = "Line";
		//
		List<X_OFB_DashboardTableCol> list = new Query(getCtx(), X_OFB_DashboardTableCol.Table_Name, whereClauseFinal.toString(), get_TrxName())
										.setParameters(get_ID())
										.setOrderBy(orderClause)
										.list();
		//
		return list.toArray(new X_OFB_DashboardTableCol[list.size()]);		
	}
	/*
	 * utility
	 * indicator 0 current month
	 * indicator 1 current month -1
	*/
	public BigDecimal getColTotal(int OFB_DashboardTableCol_ID, int indicator)
	{
		X_OFB_DashboardTableCol col = new X_OFB_DashboardTableCol(getCtx(),OFB_DashboardTableCol_ID,get_TrxName() );
		BigDecimal value = null;
		
		
		StringBuffer sb = new StringBuffer(col.getSelectClause())
		.append(" Where ")
		.append(col.getWhereClause());
		
		if (getMeasureDisplay().equals(MEASUREDISPLAY_Total))
		{
			BigDecimal ManualActual = DB.getSQLValueBD(null, sb.toString(), new Object[]{});
			if(ManualActual==null)
				ManualActual=Env.ZERO;
			value=ManualActual;
		}
		else if(getMeasureDisplay().equals(MEASUREDISPLAY_Month))//two months
		{
			
				String trunc = "MM";
				Timestamp reportDate = new Timestamp(System.currentTimeMillis());
				
				if(indicator==1)
					reportDate = addMonths(reportDate,-1);
				
				sb.append(" AND TRUNC(")
				.append(col.getDateColumn()).append(",'").append(trunc).append("')=TRUNC(")
				.append(DB.TO_DATE(reportDate)).append(",'").append(trunc).append("')");
				
				BigDecimal ManualActual = DB.getSQLValueBD(null, sb.toString(), new Object[]{});
				if(ManualActual==null)
					ManualActual=Env.ZERO;
				value=ManualActual;
			
		}
		
		return value;
	}
	
	private Timestamp addMonths (Timestamp day, int offset)
	{
		if (day == null)
			day = new Timestamp(System.currentTimeMillis());
		//
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(day.getTime());
		
		if (offset == 0)
			return new Timestamp (cal.getTimeInMillis());
		cal.add(Calendar.MONTH, offset);
		
		return new Timestamp (cal.getTimeInMillis());
	}	//	addMonths
	
	

}
