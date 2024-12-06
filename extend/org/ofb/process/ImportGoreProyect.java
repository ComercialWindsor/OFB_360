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

import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: SetTransactionLastinventory.java,v 1.2 2012/09/12 00:51:01  $
 */
public class ImportGoreProyect extends SvrProcess
{
    private Properties 		m_ctx;	
	
	private int p_tender_id =0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("PM_Tender_ID"))
				p_tender_id = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		m_ctx = Env.getCtx();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		X_PM_Tender Ten=new X_PM_Tender(Env.getCtx(), p_tender_id,get_TrxName());
		String sql = "select * from t_goreproject where concurso=?";
		PreparedStatement pstmt = null;
		int count = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setString(1, Ten.getValue());
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				if(rs.getString("ingreso")==null || rs.getString("ingreso").length()<1)
					continue;
				
				BigDecimal montoSol = new BigDecimal(rs.getString("montosolicitado").trim().replaceAll(",", ""));
				
				BigDecimal montoAsig = new BigDecimal(rs.getString("montoasignado").trim().replaceAll(",", ""));
				
				//proyecto
				MProject pj = new MProject (Env.getCtx(), 0,get_TrxName());
				String CodigoBip= Ten.getName() +"-"+ rs.getString("ingreso");
				pj.setValue(CodigoBip);
				pj.setPOReference(CodigoBip);
				pj.setName(rs.getString("nombreproyecto"));
				pj.set_CustomColumn("Year", rs.getString("year"));
				pj.setPlannedAmt(montoSol);
				pj.set_CustomColumn("IsPriorized", true);
				pj.setC_Currency_ID(228);
				
				if(rs.getString("evaluacion")!=null && rs.getString("evaluacion").length()>=1)
				{
					try{
					Integer score = Integer.parseInt(rs.getString("evaluacion").trim().replaceAll(",", ""));
					pj.set_CustomColumn("Score", score);
					}
					catch(Exception e){}
				}
				pj.save();
				
				//linea
				MProjectLine pjl = new MProjectLine (pj);
				pjl.setLine(10);
				pjl.setPlannedPrice(montoSol);
				pjl.setM_Product_ID(1000884);
				pjl.save();
				
				//convenio mandato
				X_DM_MandateAgreement cm = new X_DM_MandateAgreement(Env.getCtx(), 0 ,get_TrxName());//convenio mandato
				cm.setAD_Org_ID(pj.getAD_Org_ID());
				cm.setDocStatus("DR");
				cm.setC_Project_ID(pj.getC_Project_ID());
				cm.setDateTrx(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				cm.setAmt(montoAsig);
				cm.setAllocatedAmt(montoAsig);
				cm.set_CustomColumn("Year", pj.get_Value("Year"));
				cm.set_CustomColumn("Code", pj.get_Value("POReference"));
				cm.set_CustomColumn("C_BPartner_ID", pj.get_Value("C_BPartner_ID"));
				cm.set_CustomColumn("C_BPartnerC_ID", pj.get_Value("C_BPartnerC_ID"));
				cm.set_CustomColumn("C_BPartnerA_ID", pj.get_Value("C_BPartnerA_ID"));
				BigDecimal factor = DB.getSQLValueBD(get_TrxName(), "select factor from PM_YearFactor where Year=?",rs.getString("Year"));
				cm.set_CustomColumn("Factor", factor==null?Env.ONE:factor);
				
				if(pj.get_ValueAsBoolean("PM_Gore_UT"))
					cm.set_CustomColumn("DM_MandateAgreementType", "02");
				else
					cm.set_CustomColumn("DM_MandateAgreementType", "01");
	
				cm.save();
				cm.setProcessed(true);
				cm.setDocStatus("CO");
				DB.executeUpdate("update dm_mandateagreementline set processed='Y' where dm_mandateagreement_id = "+ cm.getDM_MandateAgreement_ID(), get_TrxName());
				cm.save();
				
				
				//solicitud de fondos
				X_DM_Document rss = new X_DM_Document(Env.getCtx(), 0 ,get_TrxName());
				rss.setAD_Org_ID(pj.getAD_Org_ID());
				rss.setDocStatus("DR");
				rss.setC_Project_ID(pj.getC_Project_ID());
				rss.setDM_DocumentType("AP");
				rss.setDateTrx(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				rss.setAmt(montoAsig);
				rss.setProcessed(true);
				rss.setDocStatus("CO");
				rss.save();
				
				//resolucion de asignacion
				X_DM_Document ra = new X_DM_Document(Env.getCtx(), 0 ,get_TrxName());
				ra.setAD_Org_ID(pj.getAD_Org_ID());
				ra.setDocStatus("DR");
				ra.setDM_DocumentType("RS");
				ra.setDateTrx(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				ra.setAmt(montoAsig);
				ra.setProcessed(true);
				ra.setDocStatus("CO");
				ra.save();
				
				X_DM_DocumentLine dline = new X_DM_DocumentLine(Env.getCtx(), 0 ,get_TrxName());
				dline.setAmt(montoAsig);
				dline.setDateTrx(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				dline.setDM_Document_ID(ra.getDM_Document_ID());
				dline.setM_Product_ID(1000884);
				dline.set_CustomColumn("C_ProjectLine_ID", pjl.getC_ProjectLine_ID());
				dline.save();
				
				count ++;
				

			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
			return "Importados "+ count;
	}
	
}	//	CopyOrder
