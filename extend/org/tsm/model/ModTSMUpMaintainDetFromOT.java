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
package org.tsm.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_MP_MaintainDetail;
import org.compiere.model.X_MP_OT;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for company TSM
 *
 *  @author Italo Niñoles
 */
public class ModTSMUpMaintainDetFromOT implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModTSMUpMaintainDetFromOT ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModTSMUpMaintainDetFromOT.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;
	

	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing global validator: "+this.toString());
		}

		//	Tables to be monitored
		engine.addModelChange(X_MP_OT.Table_Name, this);
		//	Documents to be monitored


	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE)&& po.get_Table_ID()==X_MP_OT.Table_ID && po.is_ValueChanged("DocStatus"))  
		{
			X_MP_OT ot = (X_MP_OT) po;
			if(ot.getDocStatus().compareTo("CO") == 0)
			{
				String mysql="select distinct MP_MAINTAINDetail_ID from MP_OT_TASK where MP_OT_ID=?";
				PreparedStatement pstmt = null;
				try
				{
					pstmt = DB.prepareStatement(mysql, po.get_TrxName());
					pstmt.setInt(1, ot.get_ID());
					ResultSet rs = pstmt.executeQuery();
					BigDecimal Dif = Env.ZERO;
					while (rs.next())
					{
						Dif = Env.ZERO;
						X_MP_MaintainDetail mp= new X_MP_MaintainDetail(Env.getCtx(), rs.getInt(1),po.get_TrxName());
						BigDecimal actualKm = null;
						if(!mp.isProgrammingType().equals("C"))
						{	
							actualKm = (BigDecimal)ot.get_Value("tsm_km");
							if(actualKm != null && actualKm.compareTo(Env.ZERO) > 0)
							{
								Dif = actualKm.subtract(mp.getnextmp().subtract(mp.getlastmp()));
								//Dif = Dif.abs();
								mp.setnextmp(mp.getInterval().add(actualKm).setScale(2, BigDecimal.ROUND_HALF_EVEN));
								mp.setlastmp(actualKm);
							}
							else
								mp.setnextmp(mp.getInterval().add(mp.getlastread()).setScale(2, BigDecimal.ROUND_HALF_EVEN));
							//ininoles por el momento no actualizaremos segunda variable 
							//mp.setlastmp(new BigDecimal(Float.parseFloat(description.split(":")[description.split(":").length-1].replace(',', '.'))).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						}
						mp.save();
						//buscamos mp hijos para actualizar el valor de ultima mantencion lastmp
						if(actualKm != null && actualKm.compareTo(Env.ZERO) > 0)
						{
							String sql = "SELECT MP_MaintainDetail_ID FROM MP_MaintainDetail md " +
								" WHERE md.MP_Maintain_ID IN (SELECT MP_MaintainRef_ID FROM MP_MaintainParent WHERE MP_Maintain_ID = ?) " +
								" AND md.A_Asset_ID = ? ";
							PreparedStatement pstmt2 = null;
							try
							{
								pstmt2 = DB.prepareStatement(sql, po.get_TrxName());
								pstmt2.setInt(1, mp.getMP_Maintain_ID());
								pstmt2.setInt(2, mp.getA_Asset_ID());
								ResultSet rs2 = pstmt2.executeQuery();
								while (rs2.next())
								{
									X_MP_MaintainDetail mp2= new X_MP_MaintainDetail(Env.getCtx(), rs2.getInt(1),po.get_TrxName());
									if(!mp2.isProgrammingType().equals("C"))
									{	
										if(actualKm != null && actualKm.compareTo(Env.ZERO) > 0)
										{
											mp2.setlastmp(actualKm);
											//mp2.setnextmp(mp2.getInterval().add(actualKm).setScale(2, BigDecimal.ROUND_HALF_EVEN));
											//ininoles al intervalo se le suma la diferencia
											mp2.setnextmp(mp2.getInterval().add(Dif).setScale(2, BigDecimal.ROUND_HALF_EVEN));
										}
									}
									mp2.save();
								}
								rs2.close(); pstmt2.close();
								pstmt2 = null; rs2 = null;
							}
							catch (Exception e)
							{
								log.log(Level.SEVERE, mysql, e);
							}
						}//ininoles end
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, mysql, e);
				}
			}
		}
		return null;
	}	//	modelChange	


	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);

		
		return null;
	}	//	docValidate

	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString


	

}	