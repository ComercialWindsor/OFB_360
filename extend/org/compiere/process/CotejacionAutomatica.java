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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: CotejacionAutomatica.java,v 1.0  $
 */
public class CotejacionAutomatica extends SvrProcess
{
	
	
	private int 			m_BankAccount_ID=0;
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
			
			else if (name.equals("C_BankAccount_ID"))
				m_BankAccount_ID = ((BigDecimal)para[i].getParameter()).intValue();
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
		StringBuffer sql = null;
		int no=0;
		sql = new StringBuffer("SELECT * "
					+ "FROM C_bankMatch i  "
					+ "WHERE i.C_BankAccount_ID>0 and ismatched='N' ");
		 
		sql.append(" And i.createdby="+Env.getAD_User_ID(getCtx()));
		
		if(m_BankAccount_ID>0)
			sql.append(" And i.C_BankAccount_ID="+m_BankAccount_ID);
		 
		 PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					X_C_BankMatch  cbank = new X_C_BankMatch (Env.getCtx(), rs, get_TrxName());
					
					int Payment_ID=getPayment(cbank.getAmt(), cbank.getSigno(),cbank.getDescription(),cbank.getC_BankAccount_ID());
										
					if(Payment_ID>0){
						cbank.setC_Payment_ID(Payment_ID);
						cbank.setIsMatched(true);
						cbank.save();
						no++;
					}
					
				}
				rs.close();
				pstmt.close();
				
				
				rs = null;
				pstmt = null;
				
			}
			catch(Exception e)
			{
				log.log(Level.SEVERE,e.getMessage());
			}
			
		this.commitEx();
					
		return no +" Cotejados";
	   
	}	//	doIt


	private int getPayment(BigDecimal amt, String signo, String Descripcion, int Account_ID){
		
		String sql= "select p.C_Payment_ID from C_Payment p" +
		" where p.DocStatus IN ('CO','CL') and p.isReconciled='N' and p.PayAmt=? and p.isreceipt=? and p.C_BankAccount_ID=?" +
		" And Not Exists (select * from C_bankMatch c where c.C_Payment_ID = p.C_Payment_ID) ";
		
		if(Descripcion.toUpperCase().indexOf("CHEQUE")>-1)
			sql+=" and p.TenderType='K' "; // cheque
		
		return DB.getSQLValue(get_TrxName(),sql, amt, signo.equals("+")?"Y":"N",Account_ID);
	}
	
}	//	ShipInOut
