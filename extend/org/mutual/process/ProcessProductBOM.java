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
package org.mutual.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.model.MProduct;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.eevolution.model.X_PP_Product_BOM;
import org.eevolution.model.X_PP_Product_BOMLine;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class ProcessProductBOM extends SvrProcess
{
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{	 
			//p_C_Invoice_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		String sqlProd = "SELECT M_Product_ID,ValueProductRef  FROM I_Product WHERE M_Product_ID IS NOT NULL " +
				" AND ValueProductRef IS NOT NULL ORDER BY M_Product_ID";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sqlProd, this.get_TrxName());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MProduct prod = new MProduct(getCtx(), rs.getInt("M_Product_ID"), get_TrxName());
				//buscamos valor de referencia.
				int id_ProdRef = DB.getSQLValue(get_TrxName(), "SELECT M_Product_ID FROM M_Product WHERE value like '"+rs.getString("ValueProductRef")+"'");
				if (id_ProdRef > 0)//existe producto asociado
				{
					MProduct prodRef = new MProduct(getCtx(), id_ProdRef, get_TrxName());
					int id_ProdutBOM = DB.getSQLValue(get_TrxName(), "SELECT MAX(PP_Product_BOM_ID) FROM PP_Product_BOM WHERE M_Product_ID = "+prodRef.get_ID());
					//calculamos fecha a usar despues
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.YEAR, -1);
					Timestamp current = new Timestamp(calendar.getTime().getTime());					
					
					if (id_ProdutBOM > 0)//existe bom
					{	
						X_PP_Product_BOMLine pBomLine = new X_PP_Product_BOMLine(getCtx(),0,get_TrxName());
						pBomLine.setPP_Product_BOM_ID(id_ProdutBOM);
						pBomLine.setM_Product_ID(prod.get_ID());
						pBomLine.setQtyBOM(Env.ONE);						
						pBomLine.setValidFrom(current);
						int iLine = DB.getSQLValue(get_TrxName(), "SELECT MAX(Line) FROM PP_Product_BOMLine WHERE PP_Product_BOM_ID = "+id_ProdutBOM);
						iLine = iLine+10;
						pBomLine.setLine(iLine);
						pBomLine.save();
					}
					else // no existe bom, crear
					{
						//creamos el BOM
						X_PP_Product_BOM pBom = new X_PP_Product_BOM(getCtx(), 0, get_TrxName());
						pBom.setM_Product_ID(prodRef.get_ID());
						pBom.setName(prodRef.getName());
						pBom.setValue(prodRef.getValue());
						pBom.setValidFrom(current);
						pBom.save();
						
						//agregamos la linea
						X_PP_Product_BOMLine pBomLine = new X_PP_Product_BOMLine(getCtx(),0,get_TrxName());
						pBomLine.setPP_Product_BOM_ID(pBom.get_ID());
						pBomLine.setM_Product_ID(prod.get_ID());
						pBomLine.setQtyBOM(Env.ONE);
						pBomLine.setValidFrom(current);
						int iLine = DB.getSQLValue(get_TrxName(), "SELECT MAX(Line) FROM PP_Product_BOMLine WHERE PP_Product_BOM_ID = "+pBom.get_ID());
						iLine = iLine+10;
						pBomLine.setLine(iLine);
						pBomLine.save();
					}
				}
			}	
		}catch (Exception e)
		{
			log.severe(e.getMessage());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs=null;pstmt=null;
		}
	   return "Procesado ";
	}	//	doIt
}
