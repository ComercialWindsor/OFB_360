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

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class CreatePJCombinations extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int project_id =0;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Project_ID"))
				project_id = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		String mysql = "select c_project_id , c_projectline_id, m_product_id from c_projectline where AD_Client_ID=? and m_product_id is not null";
		int ac=0,acf=0;;
		
		if(project_id>0)
			mysql +=" and C_Project_ID=?";
		
		 PreparedStatement pstmt = null;
			ResultSet rs = null;
		    
			
			try
			{
				pstmt = DB.prepareStatement(mysql, get_TrxName());
				pstmt.setInt(1,getAD_Client_ID());
				if(project_id>0)
					pstmt.setInt(2,project_id);
				
				rs = pstmt.executeQuery();
				
				while(rs.next())
				{
					X_C_Project pj = new X_C_Project(getCtx(), rs.getInt(1), get_TrxName());
					X_C_ProjectType pt = new X_C_ProjectType(getCtx(), Integer.parseInt(pj.get_Value("C_ProjectType_ID").toString()), get_TrxName());
					MProduct pro = new MProduct(getCtx(), rs.getInt(3), get_TrxName());
					
					//sql que trae la cuenta segun tipo de proyecto
					
					String sql2 = "";
					if (pt.getHelp().equals("C33"))
						sql2 = "select MAX(P_Cir33_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
					else if (pt.getHelp().equals("S24"))
						sql2 = "select MAX(P_Sub24_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
					else if (pt.getHelp().equals("S31"))
						sql2 = "select MAX(P_Sub31_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
					else if (pt.getHelp().equals("S33FIC"))
						sql2 = "select MAX(P_Sub33FIC_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
					else if (pt.getHelp().equals("S33FRIL"))
						sql2 = "select MAX(P_Sub33FRIL_Acct) from M_Product_Acct where M_Product_ID=? and C_AcctSchema_ID=?";
					
					
					//cuenta debito
					int cvcID = DB.getSQLValue(get_TrxName(), sql2, pro.get_ID(), 1000000);
					
					X_C_ValidCombination cvc = new X_C_ValidCombination(getCtx(), cvcID, get_TrxName());
										
					//cuenta credito
					String alias = cvc.getAlias() ;
					alias = alias.trim();
					
					String anb = pj.get_ValueAsString("AccountNo");					
										
					String anS = anb;
					String aliaspro = alias + anS;
					aliaspro = aliaspro.trim();
										
					String sql3 = "select MAX(C_ValidCombination_ID) from C_ValidCombination where alias like ?";
					
					int cvcID3 = DB.getSQLValue(get_TrxName(), sql3, aliaspro);
					
					if(cvcID3<=0)// crear cuenta
					{
						String sql4 = "select C_ElementValue_ID from C_ElementValue where Value = ?";
						int element_ID = DB.getSQLValue(get_TrxName(), sql4, aliaspro);
						log.config("element_ID 1:"+ element_ID);
						if(element_ID<=0)//crear elememto
						{
							log.config("crear elemento");
							MElementValue element= new MElementValue(getCtx(), aliaspro, aliaspro, "Cuenta Proyecto " + pj.getName(),
									MElementValue.ACCOUNTTYPE_Expense, "N",
									false, false, get_TrxName());
							element.setC_Element_ID(1000000);
						
							element.save();
							log.config("elemento guardado");
							element_ID=element.getC_ElementValue_ID();
							log.config("element_ID 2:"+ element_ID);
						}
						//crear combinacion
						MAccount newAccount = new MAccount (getCtx(), 0, get_TrxName());
						newAccount.setAD_Org_ID(0);
						newAccount.setAlias(aliaspro);
						newAccount.setC_AcctSchema_ID(1000000);
						newAccount.setAccount_ID(element_ID);
						newAccount.save();
						
						ac++;
						cvcID3 = newAccount.getC_ValidCombination_ID();
						
					}
					else
						acf++;
					
					DB.executeUpdate("Update C_ProjectLine set PJ_Item_Acct=" +cvcID3 + " Where C_Project_ID="+rs.getInt(1)+" and M_Product_ID="+rs.getInt(3), get_TrxName());//actualiza linea cuenta proyecto
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, mysql, e);
			}
			finally {
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
		
      return "Creados :" + ac + " Encontrados :" + acf;

	}	//	doIt


	
}	//	CopyOrder
