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
package org.ofb.process;


import java.math.BigDecimal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MConversionRate;
import org.compiere.model.X_DM_PerformanceBond;
import org.compiere.model.X_DM_PerformanceBondDet_Proc;
import org.compiere.model.X_DM_PerformanceBond_Proc;
import org.compiere.util.DB;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

/**
 * 	Create Resolution "Cometidos" 
 *	
 *	
 *  @author Italo NIñoles
 *  @version CreateResolutionsCometidos.java,v 1 2013/03/280 19:27:01
 *  
 *  
 */
public class CreatePerformanceBondDet extends SvrProcess
{	
	/**	Doc Date To			*/
	//private Timestamp	p_DateTrx_To;
	/**	Doc Date From		*/
	private Timestamp	p_DateTrx_From;
	/** ID Resolucion*/
	private int 		p_Document_ID = 0;
	
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_Document_ID = this.getRecord_ID();		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;		
			else if (name.equals("DateTrx"))
			{
				p_DateTrx_From = (Timestamp)para[i].getParameter();
				//p_DateTrx_To = (Timestamp)para[i].getParameter_To();
			}
			else				
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare
	
	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		
		X_DM_PerformanceBond_Proc pro = new X_DM_PerformanceBond_Proc(getCtx(), p_Document_ID, get_TrxName());
		int cant = 0;		
		if(pro.getC_DocType().getDocBaseType().equals("PBV"))
		{
			
			PreparedStatement pstmt = null;
			
			String sql = "SELECT dm.DM_PerformanceBond_ID FROM DM_PerformanceBond dm WHERE dm.dateend <= ? AND dm.Status like 'ENC'" +
					" AND dm.DM_PerformanceBond_ID NOT IN (SELECT DM_PerformanceBond_ID FROM DM_PerformanceBondDet_Proc)";
			
			try
			{	
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setTimestamp(1, p_DateTrx_From);
				//pstmt.setTimestamp(2, p_DateTrx_To);
				
				ResultSet rs = pstmt.executeQuery();		
				
				while (rs.next()) //ininoles lineas por boleta vencida  
				{
					X_DM_PerformanceBond boleta = new X_DM_PerformanceBond(getCtx(), rs.getInt(1), get_TrxName()) ;
					X_DM_PerformanceBondDet_Proc det = new X_DM_PerformanceBondDet_Proc(getCtx(), 0, get_TrxName());
					det.setDM_PerformanceBond_Proc_ID(pro.get_ID());
					det.setDM_PerformanceBond_ID(boleta.get_ID());
					det.setAmt(boleta.getAmt());
					det.set_CustomColumn("C_Bpartner_ID", boleta.getC_BPartner_ID());
					det.set_CustomColumn("correlative", boleta.getcorrelative());
					det.set_CustomColumn("status", boleta.getStatus());
					det.set_CustomColumn("C_Bank_ID", boleta.getC_Bank_ID());
					det.set_CustomColumn("C_Currency_ID", boleta.getC_Currency_ID());					
					det.setDescription(boleta.getDescription());
					//campo moneda en pesos
					BigDecimal amtConvert = new BigDecimal("0.0");
					String sqlSchema = "SELECT MAX(C_AcctSchema1_ID) FROM AD_ClientInfo WHERE AD_Client_ID = "+boleta.getAD_Client_ID();
					int schema_ID = DB.getSQLValue(get_TrxName(), sqlSchema);
					MAcctSchema as =  new MAcctSchema(getCtx(),schema_ID,get_TrxName());
					log.config("variables para la conversion:"+boleta.getAmt()+" , "+boleta.getC_Currency_ID()+
							" , "+ boleta.getC_Currency_ID()+" , "+	boleta.getDateAcct()+" , "+114+" , "+boleta.getAD_Client_ID()+" , "+boleta.getAD_Org_ID());
					
					if (boleta.getC_Currency_ID() != as.getC_Currency_ID())
					{
						amtConvert = MConversionRate.convert(getCtx(), boleta.getAmt(), 
								boleta.getC_Currency_ID(), as.getC_Currency_ID(),
								(Timestamp)boleta.get_Value("DateDoc"), 114, 
							boleta.getAD_Client_ID(), boleta.getAD_Org_ID());
						log.config("monto dentro de if: "+ amtConvert);
					}else
					{
						amtConvert = boleta.getAmt();
					}					
					if(amtConvert != null)
					{
						det.set_CustomColumn("AmtRef", amtConvert);
					}
					det.save();
					cant = cant +1;
				}		
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}	
		}
		return "Se han agregado " + cant + " Boletas de Honorarios";
	}
}	//	RequisitionPOCreate
