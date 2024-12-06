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

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.*;

/**
 *	
 *	
 *  @author faaguilar OFB
 *  @version $Id: GenerateAssetWF.java,v 1.2 2011/06/12 00:51:01  $
 */

public class GenerateAssetWF extends SvrProcess 
{
	
	
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
		int x=0;
		String sql="select a.a_asset_id from a_asset a where a.ad_client_id=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getAD_Client_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next() )
			{
				X_A_Asset asset = new X_A_Asset (getCtx(), rs.getInt(1), get_TrxName());
				String sql2 = "SELECT COUNT(*) FROM A_Depreciation_Workfile WHERE A_Asset_ID=? ";
				if (DB.getSQLValue(get_TrxName(), sql2, asset.getA_Asset_ID())== 0) 
				{

					if (asset.isDepreciated())						
					{
						String sql3 ="SELECT A_Asset_Acct_ID FROM A_Asset_Acct WHERE A_Asset_ID = ? AND IsActive='Y'";
						int acct_ID=DB.getSQLValue(get_TrxName(), sql3, asset.getA_Asset_ID());
						if(acct_ID>0)
						{
							MAssetAcct assetgrpacct = new MAssetAcct (getCtx(), acct_ID, get_TrxName());
							
							X_A_Depreciation_Workfile assetwk = new X_A_Depreciation_Workfile (getCtx(), 0, get_TrxName());
							assetwk.setA_Asset_ID(asset.getA_Asset_ID());		
							assetwk.setA_Life_Period(asset.getUseLifeMonths() );
							assetwk.setA_Asset_Life_Years(asset.getUseLifeYears());
							assetwk.setIsDepreciated(true);
							assetwk.setPostingType(assetgrpacct.getPostingType());
							assetwk.setA_Accumulated_Depr(new BigDecimal(0.0));
							assetwk.setA_QTY_Current(new BigDecimal(0.0));
							assetwk.setA_Asset_Cost(new BigDecimal(0.0));
							assetwk.setA_Period_Posted(0);
							assetwk.setA_QTY_Current(Env.ONE);//faaguilar OFB
							assetwk.save();
							x++;
						}
					}
				}
			}
		rs.close();
		pstmt.close();
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		
		return "Generados :"+ x;
		
	}
		
}
