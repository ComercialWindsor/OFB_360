package org.ofb.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MInvoice;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class RequisitionConsolidateLines extends SvrProcess{
	
	private int 			Record_ID;
	//filtro usuario
	private int				p_AD_User_ID = 0;
	//Filtro Monto
	private BigDecimal		p_TotalLines_From = null;
	private BigDecimal		p_TotalLines_To = null;
	//Filtro Fecha
	private Timestamp		p_DateDoc_From = null;
	private Timestamp		p_DateDoc_To = null;
	
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		Record_ID=getRecord_ID();
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_User_ID"))
				p_AD_User_ID = para[i].getParameterAsInt();
			else if (name.equals("TotalLines"))
			{
				p_TotalLines_From = (BigDecimal)para[i].getParameter();
				p_TotalLines_To = (BigDecimal)para[i].getParameter_To();
			}
			else if (name.equals("DateDoc"))
			{
				p_DateDoc_From = (Timestamp)para[i].getParameter();
				p_DateDoc_To = (Timestamp)para[i].getParameter_To();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {
		
		MRequisition rq = new MRequisition(this.getCtx(), Record_ID, this.get_TrxName());
		int i=0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select rl.Description,rl.Qty,rl.M_Product_ID,rl.m_requisitionline_id,rl.C_Charge_ID "
				+ " from adempiere.m_requisitionline rl"
		+" inner join m_requisition r on (rl.m_requisition_id=r.m_requisition_id)"
		+" inner join c_doctype d on (r.c_doctype_id=d.c_doctype_id)"
		+" where rl.isactive='Y' and" //rl.m_requisitionparent_id is null and"
		+" r.docstatus='CO' and d.docbasetype='PRT' and r.ad_org_id=?";
		if (p_AD_User_ID > 0)
		{
			sql = sql + " and ad_user_id = ? ";
		}
		if (p_DateDoc_To != null && p_DateDoc_From != null)
		{
			sql = sql +" and datedoc between ? and ?";
		}
		if (p_TotalLines_From.compareTo(Env.ZERO) > 0 && p_TotalLines_To.compareTo(Env.ZERO) > 0)
		{
			sql = sql +" and totallines between ? and ?";
		}		
		 sql = sql +" and rl.m_requisitionline_ID NOT IN (SELECT coalesce(rl2.m_requisitionparent_id,0) FROM m_requisitionline rl2  where rl2.isactive='Y' )";
		 
		 int validUser = 0;
		 int validDate = 0;
		 int index = 2;
		 
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, rq.getAD_Org_ID());
			
			if (p_AD_User_ID > 0)
			{	
				pstmt.setInt(index, p_AD_User_ID);
				index = index + 1;
			}			
			if (p_DateDoc_To != null && p_DateDoc_From != null)
			{	
				pstmt.setTimestamp(index,p_DateDoc_From);	
				pstmt.setTimestamp(index+1,p_DateDoc_To);			
				index = index + 2;
			}			
			if (p_TotalLines_From.compareTo(Env.ZERO) > 0 && p_TotalLines_To.compareTo(Env.ZERO) > 0)
			{
				pstmt.setBigDecimal(index, p_TotalLines_From);
				pstmt.setBigDecimal(index+1, p_TotalLines_To);
			}
			rs = pstmt.executeQuery();						
			
			while (rs.next())
			{
				MRequisitionLine line = new MRequisitionLine(rq);
				line.setDescription(rs.getString("Description"));
				line.setQty(rs.getBigDecimal("Qty"));
				line.setM_Product_ID(rs.getInt("M_Product_ID"));
				line.setC_Charge_ID(rs.getInt("C_Charge_ID"));
				//line.save();			
				//ininoles se graba linea padre para no volver a usarla
				try{
					line.set_CustomColumn("M_RequisitionParent_ID", rs.getInt("m_requisitionline_id"));
				}catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
				line.save();
				i++;
			}							
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		finally
		{
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
		}
		return "Copiadas "+i;
	}

}
