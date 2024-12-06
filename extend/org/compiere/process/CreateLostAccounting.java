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
 *	Create lost accouting definitions
 *  @author Fabian Aguilar 
 *  @version $Id: CreateLostAccounting.java,v 1.2 2006/07/30 00:51:01 faaguilar Exp $
 */
public class CreateLostAccounting extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		PreparedStatement pstmt = null;
		
		String sql="Select A.C_BP_Group_ID from C_BP_Group A where  "
		+ " not exists (select * from C_BP_Group_Acct C where C.C_BP_Group_ID=A.C_BP_Group_ID)";
		
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MBPGroup pc= new MBPGroup(getCtx(),rs.getInt(1),get_TrxName());
				pc.CreatelostAcct();
					
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		commit();
		
		 sql="Select A.C_BPartner_ID from C_BPartner A where not exists (select * from C_BP_Customer_Acct B where B.C_BPartner_ID=A.C_BPartner_ID) "
		+ "or not exists (select * from C_BP_Vendor_Acct C where C.C_BPartner_ID=A.C_BPartner_ID)";
		
		
		
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MBPartner bp= new MBPartner(getCtx(),rs.getInt(1),get_TrxName());
				bp.CreatelostAcct();
					
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		
		sql="Select A.M_PRODUCT_CATEGORY_ID from M_PRODUCT_CATEGORY A where  "
		+ " not exists (select * from M_PRODUCT_CATEGORY_ACCT C where C.M_PRODUCT_CATEGORY_ID=A.M_PRODUCT_CATEGORY_ID)";
		
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MProductCategory pc= new MProductCategory(getCtx(),rs.getInt(1),get_TrxName());
				pc.CreatelostAcct();
					
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		commit();
		
		
		sql="Select A.M_Product_ID from M_Product A where  "
		+ " not exists (select * from M_Product_Acct C where C.M_Product_ID=A.M_Product_ID)";
		
		
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MProduct p= new MProduct(getCtx(),rs.getInt(1),get_TrxName());
				p.CreatelostAcct();
					
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		sql="Select A.C_BankAccount_ID from C_BankAccount A where  "
		+ " not exists (select * from C_BankAccount_Acct C where C.C_BankAccount_ID=A.C_BankAccount_ID)";
		
		
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MBankAccount b= new MBankAccount(getCtx(),rs.getInt(1),get_TrxName());
				b.CreatelostAcct();
					
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		
		
		
		sql="Select A.C_TAX_ID from C_TAX A where  "
		+ " not exists (select * from C_TAX_ACCT C where C.C_TAX_ID=A.C_TAX_ID)";
		
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MTax tx= new MTax(getCtx(),rs.getInt(1),get_TrxName());
				tx.CreatelostAcct();
					
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		sql="Select A.A_Asset_ID from A_Asset A where  "
			+ " not exists (select * from A_Asset_Acct C where C.A_Asset_ID=A.A_Asset_ID)";
		
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAsset tx= new MAsset(getCtx(),rs.getInt(1),get_TrxName());
				tx.CreatelostAcct();
				tx.save();
					
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
	   return "Accounting Created";
	}	//	doIt


	
}	//	CreateLostAccounting
