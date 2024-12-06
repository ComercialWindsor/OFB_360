/******************************************************************************
0 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.geminis.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	
 *  @author ininoles
 *  @version $Id: VoidAssetProcess.java,v 1.2 2015/06/17 ininoles$
 */
public class VoidAssetProcess extends SvrProcess
{
	private int	p_Year_ID = 0;	
	private int p_Period_ID = 0;
	private int p_AssetGroup_ID = 0;
	private String p_DepType = null;

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
			else if (name.equals("C_Year_ID"))
			{
				p_Year_ID = para[i].getParameterAsInt();				
			}
			else if (name.equals("C_Period_ID"))
			{
				p_Period_ID = para[i].getParameterAsInt();				
			}
			else if (name.equals("A_Asset_Group_ID"))
			{
				p_AssetGroup_ID = para[i].getParameterAsInt();				
			}
			else if (name.equals("DepType"))
			{
				p_DepType = (String)para[i].getParameter();
			}
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		//validacion de periodo cerrado
		int contador = 0;
		String sqlC = "SELECT ad.A_Asset_Dep_ID FROM A_Asset_Dep ad WHERE ad.DepType = '"+p_DepType+"'";
		if (p_Period_ID > 0)
		{
			sqlC = sqlC + " AND ad.C_Period_ID = "+p_Period_ID;
		}
		else if(p_Year_ID > 0)
		{
			sqlC = sqlC + " AND ad.C_Period_ID IN (SELECT C_Period_ID FROM C_Period WHERE C_Year_ID = "+p_Year_ID+")";
		}
		if (p_AssetGroup_ID > 0)
		{
			sqlC = sqlC + " AND ad.A_Asset_Group_ID = "+p_AssetGroup_ID;
		}
		sqlC = sqlC + " AND DocStatus = 'CO' ORDER BY ad.C_Period_ID DESC ";
				
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sqlC, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
		
			//ciclo de procesos de activo				
			while (rs.next())
			{		
				X_A_Asset_Dep DepDoc= new X_A_Asset_Dep(getCtx(),rs.getInt("A_Asset_Dep_ID"),get_TrxName());
				
				MJournalBatch batch= null;
				batch = new MJournalBatch(getCtx(),DepDoc.getGL_JournalBatch_ID(),get_TrxName());	
				if (DepDoc.getDocStatus().equals("CO"))
				{
					MJournal[] journals = batch.getJournals(true);
					 for (int i = 0; i < journals.length; i++)
						{
							MJournal jour = journals[i];
							
							if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Depreciacion ))){
								MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
								workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().subtract(jour.getTotalCr()) );
								workfile.setA_Period_Posted(workfile.getA_Period_Posted()-1);
								workfile.setAssetDepreciationDate(DepDoc.getDateDoc());
								workfile.save();
								
								X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),get_TrxName());
								fore.setProcessed(false);
								fore.setGL_Journal_ID(0);
								fore.save();
							}
							else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_CorrecionMonetaria ))){
								MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));								
								MAssetAcct group=new MAssetAcct(getCtx(), MAssetAcct.getAssetAcct_ID(jour.get_ValueAsInt("A_Asset_ID")),get_TrxName());								
								int flag = 0;
								try 
								{
									flag = group.get_ValueAsInt("a_assetcomplement_acct");
								}
								catch (Exception e)
								{
									flag = 0;
									log.log(Level.SEVERE, e.getMessage(), e);
								}
								if (flag > 0)
								{	
									/*BigDecimal acum=DB.getSQLValueBD(get_TrxName(),  "select sum(AmtAcctDr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
											+" and C_ValidCombination_ID="+group.getA_Depreciation_Acct());*/
									BigDecimal flag2 = DB.getSQLValueBD(get_TrxName(),  "select sum(AmtAcctDr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
											+" and C_ValidCombination_ID="+group.get_ValueAsInt("a_assetcomplement_acct")); 
									BigDecimal flag3 = DB.getSQLValueBD(get_TrxName(),  "select sum(AmtAcctCr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
											+" and C_ValidCombination_ID="+group.get_ValueAsInt("a_assetcomplement_acct"));
									if (flag2 != null || flag3 != null) 
									{
										if (flag2 == null)
											flag2 = Env.ZERO;
										if (flag3 == null)
											flag3 = Env.ZERO;
										
										if (flag2.compareTo(Env.ZERO) >= 0 || flag3.compareTo(Env.ZERO) >= 0)
										{
											BigDecimal acum=flag2;
											acum=acum.subtract(flag3);
											
											BigDecimal neto=DB.getSQLValueBD(get_TrxName(), "select sum(AmtAcctCr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
													+" and C_ValidCombination_ID="+group.getA_Accumdepreciation_Acct());
											if (workfile != null)
											{
												if (workfile.get_ID() > 0)
												{
													workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().subtract(acum) );
													workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().subtract(neto));
													workfile.save();
												}
											}
											if (jour.get_ValueAsInt("A_Asset_Forecast_ID") > 0)
											{
												X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),null);
												fore.setCorrected(false);
												fore.save();
											}
										}
										else
										{
											BigDecimal acum=DB.getSQLValueBD(get_TrxName(),  "select sum(AmtAcctDr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
													+" and C_ValidCombination_ID="+group.getA_Depreciation_Acct());										
											BigDecimal neto=DB.getSQLValueBD(get_TrxName(), "select sum(AmtAcctCr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
													+" and C_ValidCombination_ID="+group.getA_Accumdepreciation_Acct());
											if (acum == null)
												acum = Env.ZERO;
											if (neto == null)
												neto = Env.ZERO; 
											if (workfile != null)
											{
												if (workfile.get_ID() > 0)
												{
													workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().subtract(acum) );
													workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().subtract(neto));
													workfile.save();
												}
											}
											if (jour.get_ValueAsInt("A_Asset_Forecast_ID") > 0)
											{
												X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),null);
												fore.setCorrected(false);
												fore.save();
											}
										}
									}
									else 
									{
										BigDecimal acum=DB.getSQLValueBD(get_TrxName(),  "select sum(AmtAcctDr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
												+" and C_ValidCombination_ID="+group.getA_Depreciation_Acct());										
										BigDecimal neto=DB.getSQLValueBD(get_TrxName(), "select sum(AmtAcctCr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
												+" and C_ValidCombination_ID="+group.getA_Accumdepreciation_Acct());
										if (acum == null)
											acum = Env.ZERO;
										if (neto == null)
											neto = Env.ZERO;
										if (workfile != null)
										{
											if (workfile.get_ID() > 0)
											{
												workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().subtract(acum) );
												workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().subtract(neto));
												workfile.save();
											}
										}
										if (jour.get_ValueAsInt("A_Asset_Forecast_ID") > 0)
										{
											X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),null);
											fore.setCorrected(false);
											fore.save();
										}
									}
								}
								else
								{									
									BigDecimal acum=DB.getSQLValueBD(get_TrxName(),  "select sum(AmtAcctDr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
											+" and C_ValidCombination_ID="+group.getA_Depreciation_Acct());
									BigDecimal neto=DB.getSQLValueBD(get_TrxName(), "select sum(AmtAcctCr) from gl_journalline where gl_journal_id="+jour.getGL_Journal_ID()
											+" and C_ValidCombination_ID="+group.getA_Accumdepreciation_Acct());
									if (acum == null)
										acum = Env.ZERO;
									if (neto == null)
										neto = Env.ZERO; 
									if (workfile != null)
									{
										if (workfile.get_ID() > 0)
										{
											workfile.setA_Accumulated_Depr(workfile.getA_Accumulated_Depr().subtract(acum) );
											workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().subtract(neto));
											workfile.save();
										}
									}
									if (jour.get_ValueAsInt("A_Asset_Forecast_ID") > 0)
									{
										X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),jour.get_ValueAsInt("A_Asset_Forecast_ID"),null);
										fore.setCorrected(false);
										fore.save();
									}
								}
							}
							else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Reevaluacion))){
								MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
								workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().subtract(jour.getTotalCr()));
								workfile.save();
								
							}
							else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_Deteriodo))){
								MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
								workfile.setA_Asset_Cost(workfile.getA_Asset_Cost().add(jour.getTotalCr()));
								workfile.save();
							}
							else if(DepDoc.getDepType().equalsIgnoreCase((X_A_Asset_Dep.DEPTYPE_VentaActivo))){
								MDepreciationWorkfile workfile=MAsset.getWorkFile(jour.get_ValueAsInt("A_Asset_ID"));
								workfile.setA_Accumulated_Depr(workfile.getA_Calc_Accumulated_Depr() );//antiguo valor antes de vender
								workfile.save();
								DB.executeUpdate("Update A_Asset_Forecast set processed='N', corrected='N' where gl_journal_id is null and A_Asset_ID="+jour.get_ValueAsInt("A_Asset_ID"), get_TrxName());
							}
						}
					 //borrado anulacion 
					 for(MJournal j:journals)
					 {
						 DB.executeUpdate("delete from fact_acct where record_id="+j.getGL_Journal_ID()+" and ad_table_id="+I_GL_Journal.Table_ID, get_TrxName());
						 DB.executeUpdate("delete from gl_journalline where gl_journal_id="+j.getGL_Journal_ID(), get_TrxName());
					 }
					 
					 DB.executeUpdate("delete from gl_journal where gl_journalbatch_id="+batch.getGL_JournalBatch_ID(), get_TrxName());
				 
					 DepDoc.setDocStatus("VO");
					 DepDoc.setProcessed(true);
					 DepDoc.save();
					
					contador= contador +1;
					commit();
				}
				
			}
		rs.close();
		pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "Se han anulado " +contador + " procesos de activo";
	}	//	doIt
	
}	//	OrderOpen
