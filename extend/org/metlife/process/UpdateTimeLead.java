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
package org.metlife.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MCampaign;
import org.compiere.model.X_C_Campaign;
import org.compiere.model.X_C_CampaignFollow;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	report infoprojectPROVECTIS
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class UpdateTimeLead extends SvrProcess
{
	
	//private int p_PInstance_ID;	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_C_Campaign = 0;
	
	protected void prepare()
	{			
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Campaign_ID"))
				p_C_Campaign = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws java.lang.Exception
	{	
		MCampaign camp = new MCampaign(getCtx(), p_C_Campaign, get_TrxName());
		
		if (updateExpiredLeadAgencia(camp.get_ID()))
		{
			if (camp.getName().toUpperCase().contains("AGENCIA"))
			{
				if (asignUserAgencia(camp.get_ID()))
				{
					if(SubAsignUserAgencia(camp.get_ID()))
						;
				}
			}
		}
		/*else if (camp.getName().toUpperCase().contains("COLECTIVOS"))
		{
			if (asignUserSColectivos(camp.get_ID()))
			{
				;
			}			
		}
		else if (camp.getName().toUpperCase().contains("DTC"))
		{
			if (asignUserDMDTC(camp.get_ID()))
			{
				;
			}			
		}
		else if (camp.getName().toUpperCase().contains("VITALICIAS"))
		{
			if (asignUserRVitalicias(camp.get_ID()))
			{
				;
			}						
		}
		else if (camp.getName().toUpperCase().contains("CONSUMO"))
		{
			if (asignUserCConsumo(camp.get_ID()))
			{
				;
			}			
		}
		*/
		return "OK";
	}	//	doIt

	public Boolean asignUserRVitalicias(int ID_Capaign ) throws SQLException
	{	
				X_C_Campaign cam = new X_C_Campaign(getCtx(),ID_Capaign,get_TrxName());
				
				//calculo carga de ejecutivos 
				//mfrojas son los ejecutivos que existen en las campañas a asignar los socios nuevos.
				//ininoles se modifica para que solo tome ne cuenta los en estado asignado
				/*String sqlEje = "SELECT DISTINCT (cf.AD_User_ID), COALESCE((SELECT COUNT(1) FROM C_CampaignFollow " +
						" WHERE C_Campaign_ID = cf.C_Campaign_ID AND AD_User_ID = cf.AD_User_ID),0) as count FROM C_CampaignFollow cf" +
						" WHERE cf.C_Campaign_ID = ? AND cf.AD_User_ID IS NOT NULL AND FinalStatus IN ('AS','CO','IN')";
				*/
				String sqlEje = "SELECT DISTINCT (cf.AD_User_ID), COALESCE((SELECT COUNT(1) FROM C_CampaignFollow " +
				" WHERE C_Campaign_ID = cf.C_Campaign_ID AND AD_User_ID = cf.AD_User_ID),0) as count FROM C_CampaignFollow cf" +
				" WHERE cf.C_Campaign_ID = ? AND cf.AD_User_ID IS NOT NULL AND FinalStatus = 'AS'";
				
				ArrayList<Integer> ArrayIDUsuario = new ArrayList<Integer>();
				ArrayList<Integer> ArrayCantLeads = new ArrayList<Integer>();
				
				PreparedStatement pstmtEje =  null;
				ResultSet rsEje = null;
				try
				{
					pstmtEje = DB.prepareStatement(sqlEje.toString(), get_TrxName());
					pstmtEje.setInt(1, cam.get_ID());
					rsEje = pstmtEje.executeQuery();			
					while (rsEje.next())
					{
						ArrayIDUsuario.add(rsEje.getInt(1));
						ArrayCantLeads.add(rsEje.getInt(2));
					}
				}
				catch (SQLException e)
				{
				
					throw new DBException(e, sqlEje.toString());
				}
				finally
				{
					DB.close(rsEje, pstmtEje);
					rsEje = null; pstmtEje = null;
				}
				//fin calculo de carga
				
				//calculo de etiquetas	
				int contador = 0;
				int InterestArea1_ID = 0; int InterestArea2_ID = 0; int InterestArea3_ID = 0; 
				//selecciona las etiquetas de una campaña unidas con las etiquetas obligatorias.
				String sqlEtiquetas = "SELECT ci.R_InterestArea_ID, COALESCE(MAX(ci.R_InterestAreaValues_ID),0) as R_InterestAreaValues_ID "+
						"FROM C_Campaign cc INNER JOIN R_CampaignInterest ci ON (cc.C_Campaign_ID = ci.C_Campaign_ID) "+
						"WHERE cc.C_Campaign_ID=? GROUP BY ci.R_InterestArea_ID "+ 
						"UNION SELECT R_InterestArea_ID, 0 FROM R_InterestArea WHERE IsFilter = 'Y' ";
				
				PreparedStatement pstmtEt = null;
				pstmtEt = DB.prepareStatement(sqlEtiquetas, get_TrxName());
				pstmtEt.setInt(1, cam.get_ID()); 
				//pstmtEt.setInt(2, ID_EtiquetaUrlGL); 
				log.config("sqlEtiquetas: "+sqlEtiquetas ); //mfrojas
				ResultSet rsEt = pstmtEt.executeQuery();
				
				while (rsEt.next())
				{
					log.config("contador: "+contador);
					contador++;
					if (contador == 1)
					{
						InterestArea1_ID = rsEt.getInt("R_InterestArea_ID");
					}						
					else if (contador == 2)
					{
						InterestArea2_ID = rsEt.getInt("R_InterestArea_ID");
					}
					else if (contador == 3)
					{
						InterestArea3_ID = rsEt.getInt("R_InterestArea_ID");
					}
				}		
				//fin lista de etiquetas y valores
				//ciclo lineas de de campaña parte 1
				//parte 1 de asignaciones distintos de region II,VII,VIII
				String sqlCLinesP1 = "SELECT C_CampaignFollow_ID FROM C_CampaignFollow cf" +
						" INNER JOIN R_ContactInterest ci ON (cf.C_Bpartner_ID = ci.C_Bpartner_ID)" +
						" INNER JOIN R_InterestArea ia ON (ci.R_InterestArea_ID = ia.R_InterestArea_ID)" +
						" INNER JOIN R_InterestAreaValues iav ON (ci.R_InterestAreaValues_ID = iav.R_InterestAreaValues_ID)" +
						" WHERE C_Campaign_ID = ? AND (upper(ia.name) like 'REGION' OR upper(ia.name) like 'REGIÓN')" +
						" AND (upper(iav.value) like 'II %' OR (iav.value) like 'VII %' OR (iav.value) like 'VIII %')" +
						" AND FinalStatus IN ('IN') Order By C_CampaignFollow_ID";
				
				PreparedStatement pstmtCLinesP1 =  null;
				ResultSet rsCLinesP1 = null;				
				try
				{
					pstmtCLinesP1 = DB.prepareStatement(sqlCLinesP1, get_TrxName());
					pstmtCLinesP1.setInt(1, cam.get_ID());
					rsCLinesP1 = pstmtCLinesP1.executeQuery();
							
					while (rsCLinesP1.next())
					{
						X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), rsCLinesP1.getInt("C_CampaignFollow_ID"), get_TrxName());
						
						String sqlEtBP = "SELECT R_InterestArea_ID, MAX(R_InterestAreaValues_ID) as R_InterestAreaValues_ID FROM R_ContactInterest "+
						"WHERE C_Bpartner_ID = ? AND R_InterestArea_ID IN ("+InterestArea1_ID+","+InterestArea2_ID+","+InterestArea3_ID+")" +
								" GROUP BY R_InterestArea_ID ";
						
						int contadorBP = 0;
						int InterestAreaBP1_ID = 0; int InterestAreaValueBP1_ID = 0;
						int InterestAreaBP2_ID = 0; int InterestAreaValueBP2_ID = 0;
						int InterestAreaBP3_ID = 0; int InterestAreaValueBP3_ID = 0;
								
						PreparedStatement pstmtEtBP = null;
						pstmtEtBP = DB.prepareStatement(sqlEtBP, get_TrxName());
						pstmtEtBP.setInt(1, cFollow.getC_BPartner_ID());				
						ResultSet rsEtBP = pstmtEtBP.executeQuery();
						while (rsEtBP.next())
						{
							contadorBP++;
							if (contadorBP == 1)
							{
								InterestAreaBP1_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP1_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
							}						
							else if (contadorBP == 2)
							{
								InterestAreaBP2_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP2_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
		
							}
							else if (contadorBP == 3)
							{
								InterestAreaBP3_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP3_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
							}
						}
						//fin seteo valores de socio de negocio						
						//ciclo ID usuarios posibles de asignar
						String id_IaBPsql = "0";
						if (InterestAreaBP1_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP1_ID;
						if (InterestAreaBP2_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP2_ID;
						if (InterestAreaBP3_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP3_ID;
						
						String id_IaVBPsql = "0";
						if (InterestAreaValueBP1_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP1_ID;
						if (InterestAreaValueBP2_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP2_ID;
						if (InterestAreaValueBP3_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP3_ID;
														
						String sqlEtUser = "SELECT DISTINCT(ui.AD_User_ID) as AD_User_ID " +								
								" FROM R_UserInterest ui " +
								" INNER JOIN AD_User adu ON (ui.AD_User_ID = adu.AD_User_ID) "+
								" WHERE ui.IsActive = 'Y' AND adu.UseAgencia = 'N' AND adu.qtyLead > 0 AND ui.R_InterestArea_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues_ID IN ("+id_IaVBPsql+") OR ui.R_InterestAreaValues_ID IS NULL) ";						
						if (InterestAreaBP2_ID > 0)
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef2_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues2_ID IN ("+id_IaVBPsql+") OR ui.R_InterestAreaValues2_ID IS NULL)";
						else
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef2_ID IS NULL";
						if (InterestAreaBP3_ID > 0)
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef3_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues3_ID IN ("+id_IaVBPsql+") OR ui.R_InterestAreaValues3_ID IS NULL)";				
						else
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef3_ID IS NULL";
						sqlEtUser = sqlEtUser + " ORDER BY adu.qtyLead desc";
						
						//inicio validaciones y actualizaciones nuevo metodo de carga con volumen a asignar
						int cantIndUser1 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_User " +
								"WHERE AD_User_ID IN ("+sqlEtUser+")");					
						if (cantIndUser1 < 1)
						{
							DB.executeUpdate("UPDATE AD_User SET UseAgencia = 'N' WHERE AD_User_ID IN ("+sqlEtUser+")", get_TrxName());
							commitEx();
						}
						int cantIndUser2 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_User " +
								"WHERE AD_User_ID IN ("+sqlEtUser+")");					
						if (cantIndUser2 < 1)
						{
							DB.executeUpdate("UPDATE AD_User SET qtyLead = qtyLeadBase WHERE AD_User_ID IN ("+sqlEtUser+")", get_TrxName());
							commitEx();
						}						
						//fin validaciones y actualizaciones
						
						PreparedStatement pstmtEtUser = null; 
						pstmtEtUser = DB.prepareStatement(sqlEtUser, get_TrxName());
						ResultSet rsEtUser = pstmtEtUser.executeQuery();		
											
						rsEtUser.next();
						
						if (rsEtUser.getInt("AD_User_ID") > 0)
						{
							cFollow.set_CustomColumn("AD_User_ID", rsEtUser.getInt("AD_User_ID"));						
							cFollow.save();
							DB.executeUpdate("UPDATE AD_User SET UseAgencia = 'Y', qtyLead = qtyLead-1 WHERE AD_User_ID = "+rsEtUser.getString("AD_User_ID"), get_TrxName());
						}									
						commitEx();
						rsEtUser.close();
						pstmtEtUser.close();							
					}
				}
				catch (SQLException e)
				{
					throw new DBException(e, sqlCLinesP1.toString());
				}
				finally
				{
					DB.close(rsCLinesP1, pstmtCLinesP1);
					rsCLinesP1 = null; pstmtCLinesP1 = null;
				}
				//parte 2 se setearan lead de regiones II, VII, VIII
				String sqlCLinesP2 = "SELECT C_CampaignFollow_ID FROM C_CampaignFollow cf" +
						" INNER JOIN R_ContactInterest ci ON (cf.C_Bpartner_ID = ci.C_Bpartner_ID)" +
						" INNER JOIN R_InterestArea ia ON (ci.R_InterestArea_ID = ia.R_InterestArea_ID)" +
						" INNER JOIN R_InterestAreaValues iav ON (ci.R_InterestAreaValues_ID = iav.R_InterestAreaValues_ID)" +
						" WHERE C_Campaign_ID = ? AND (upper(ia.name) like 'REGION' OR upper(ia.name) like 'REGIÓN')" +
						" AND NOT(upper(iav.value) like 'II %' OR (iav.value) like 'VII %' OR (iav.value) like 'VIII %')" +
						" AND FinalStatus IN ('IN') Order By C_CampaignFollow_ID";
				
				PreparedStatement pstmtCLinesP2 =  null;
				ResultSet rsCLinesP2 = null;				
				try
				{
					pstmtCLinesP2 = DB.prepareStatement(sqlCLinesP2, get_TrxName());
					pstmtCLinesP2.setInt(1, cam.get_ID());
					rsCLinesP2 = pstmtCLinesP2.executeQuery();
							
					while (rsCLinesP2.next())
					{
						X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), rsCLinesP2.getInt("C_CampaignFollow_ID"), get_TrxName());
						
						//sobreescribimos InterestArea3_ID con valor de comuna para que tambien actue como filtro.
						InterestArea3_ID = DB.getSQLValue(get_TrxName(), "SELECT MAX(R_InterestArea_ID) FROM R_InterestArea WHERE UPPER(value) like 'COMUNA'");
						String sqlEtBP = "SELECT R_InterestArea_ID, MAX(R_InterestAreaValues_ID) as R_InterestAreaValues_ID FROM R_ContactInterest "+
						"WHERE C_Bpartner_ID = ? AND R_InterestArea_ID IN ("+InterestArea1_ID+","+InterestArea2_ID+","+InterestArea3_ID+")" +
								" GROUP BY R_InterestArea_ID ";
						
						int contadorBP = 0;
						int InterestAreaBP1_ID = 0; int InterestAreaValueBP1_ID = 0;
						int InterestAreaBP2_ID = 0; int InterestAreaValueBP2_ID = 0;
						int InterestAreaBP3_ID = 0; int InterestAreaValueBP3_ID = 0;
								
						PreparedStatement pstmtEtBP = null;
						pstmtEtBP = DB.prepareStatement(sqlEtBP, get_TrxName());
						pstmtEtBP.setInt(1, cFollow.getC_BPartner_ID());				
						ResultSet rsEtBP = pstmtEtBP.executeQuery();
						while (rsEtBP.next())
						{
							contadorBP++;
							if (contadorBP == 1)
							{
								InterestAreaBP1_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP1_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
							}						
							else if (contadorBP == 2)
							{
								InterestAreaBP2_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP2_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
		
							}
							else if (contadorBP == 3)
							{
								InterestAreaBP3_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP3_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
							}
						}
						//fin seteo valores de socio de negocio						
						//ciclo ID usuarios posibles de asignar
						String id_IaBPsql = "0";
						if (InterestAreaBP1_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP1_ID;
						if (InterestAreaBP2_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP2_ID;
						if (InterestAreaBP3_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP3_ID;
						
						String id_IaVBPsql = "0";
						if (InterestAreaValueBP1_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP1_ID;
						if (InterestAreaValueBP2_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP2_ID;
						if (InterestAreaValueBP3_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP3_ID;
														
						String sqlEtUser = "SELECT DISTINCT(ui.AD_User_ID) as AD_User_ID " +								
								" FROM R_UserInterest ui " +
								" INNER JOIN AD_User adu ON (ui.AD_User_ID = adu.AD_User_ID) "+
								" WHERE ui.IsActive = 'Y' AND adu.UseAgencia = 'N' AND adu.qtyLead > 0 AND ui.R_InterestArea_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues_ID IN ("+id_IaVBPsql+") OR ui.R_InterestAreaValues_ID IS NULL) ";						
						if (InterestAreaBP2_ID > 0)
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef2_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues2_ID IN ("+id_IaVBPsql+") OR ui.R_InterestAreaValues2_ID IS NULL)";
						else
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef2_ID IS NULL";
						if (InterestAreaBP3_ID > 0)
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef3_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues3_ID IN ("+id_IaVBPsql+") OR ui.R_InterestAreaValues3_ID IS NULL)";				
						else
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef3_ID IS NULL";
						sqlEtUser = sqlEtUser + " ORDER BY adu.qtyLead desc";
						
						//inicio validaciones y actualizaciones nuevo metodo de carga con volumen a asignar
						int cantIndUser1 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_User " +
								"WHERE AD_User_ID IN ("+sqlEtUser+")");					
						if (cantIndUser1 < 1)
						{
							DB.executeUpdate("UPDATE AD_User SET UseAgencia = 'N' WHERE AD_User_ID IN ("+sqlEtUser+")", get_TrxName());
							commitEx();
						}
						int cantIndUser2 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_User " +
								"WHERE AD_User_ID IN ("+sqlEtUser+")");					
						if (cantIndUser2 < 1)
						{
							DB.executeUpdate("UPDATE AD_User SET qtyLead = qtyLeadBase WHERE AD_User_ID IN ("+sqlEtUser+")", get_TrxName());
							commitEx();
						}						
						//fin validaciones y actualizaciones
						
						PreparedStatement pstmtEtUser = null; 
						pstmtEtUser = DB.prepareStatement(sqlEtUser, get_TrxName());
						ResultSet rsEtUser = pstmtEtUser.executeQuery();		
											
						rsEtUser.next();
						
						if (rsEtUser.getInt("AD_User_ID") > 0)
						{
							cFollow.set_CustomColumn("AD_User_ID", rsEtUser.getInt("AD_User_ID"));						
							cFollow.save();
							DB.executeUpdate("UPDATE AD_User SET UseAgencia = 'Y', qtyLead = qtyLead-1 WHERE AD_User_ID = "+rsEtUser.getString("AD_User_ID"), get_TrxName());
						}									
						commitEx();
						rsEtUser.close();
						pstmtEtUser.close();							
					}
				}
				catch (SQLException e)
				{
					throw new DBException(e, sqlCLinesP1.toString());
				}
				finally
				{
					DB.close(rsCLinesP1, pstmtCLinesP1);
					rsCLinesP1 = null; pstmtCLinesP1 = null;
				}				
		return true;
	}	
	

	public Boolean asignUserSColectivos(int ID_Campaign) throws SQLException
	{	
		X_C_Campaign cam = new X_C_Campaign(getCtx(),ID_Campaign,get_TrxName());
				
				//ciclo lineas de de campaña
				String sqlCLines = "SELECT C_CampaignFollow_ID FROM C_CampaignFollow WHERE C_Campaign_ID = ? " +
						" AND FinalStatus IN ('IN') Order By C_CampaignFollow_ID";
						
				PreparedStatement pstmtCLines =  null;
				ResultSet rsCLines = null;
				log.config("sqlCLines: "+sqlCLines); //mfrojas
				try
				{
					pstmtCLines = DB.prepareStatement(sqlCLines, get_TrxName());
					pstmtCLines.setInt(1, cam.get_ID());
					rsCLines = pstmtCLines.executeQuery();
							
					while (rsCLines.next())
					{
						X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), rsCLines.getInt("C_CampaignFollow_ID"), get_TrxName());
						
						String IDEBUserStr = "SELECT MAX(AD_User_ID) FROM AD_User WHERE IsDefaultSC = 'Y'";							
						int ID_EBUser = DB.getSQLValue(get_TrxName(), IDEBUserStr);
						if (ID_EBUser > 0)
						{
							cFollow.setAD_User_ID(ID_EBUser);
							cFollow.save();
						}																	
					}	
				}
				catch (SQLException e)
				{
				
					throw new DBException(e, sqlCLines.toString());
				}
				finally
				{
					DB.close(rsCLines, pstmtCLines);
					rsCLines = null; pstmtCLines = null;
				}
		return true;
	}		

	public Boolean asignUserDMDTC(int ID_Campaign) throws SQLException
	{	
				X_C_Campaign cam = new X_C_Campaign(getCtx(),ID_Campaign,get_TrxName());
				
				//calculo carga de ejecutivos 
				//mfrojas son los ejecutivos que existen en las campañas a asignar los socios nuevos.
				//ininoles se modifica para que solo tome ne cuenta los en estado asignado
				/*String sqlEje = "SELECT DISTINCT (cf.AD_User_ID), COALESCE((SELECT COUNT(1) FROM C_CampaignFollow " +
						" WHERE C_Campaign_ID = cf.C_Campaign_ID AND AD_User_ID = cf.AD_User_ID),0) as count FROM C_CampaignFollow cf" +
						" WHERE cf.C_Campaign_ID = ? AND cf.AD_User_ID IS NOT NULL AND FinalStatus IN ('AS','CO','IN')";
				*/
				String sqlEje = "SELECT DISTINCT (cf.AD_User_ID), COALESCE((SELECT COUNT(1) FROM C_CampaignFollow " +
				" WHERE C_Campaign_ID = cf.C_Campaign_ID AND AD_User_ID = cf.AD_User_ID),0) as count FROM C_CampaignFollow cf" +
				" WHERE cf.C_Campaign_ID = ? AND cf.AD_User_ID IS NOT NULL AND FinalStatus = 'AS'";
				
				ArrayList<Integer> ArrayIDUsuario = new ArrayList<Integer>();
				ArrayList<Integer> ArrayCantLeads = new ArrayList<Integer>();
				
				PreparedStatement pstmtEje =  null;
				ResultSet rsEje = null;
				try
				{
					pstmtEje = DB.prepareStatement(sqlEje.toString(), get_TrxName());
					pstmtEje.setInt(1, cam.get_ID());
					rsEje = pstmtEje.executeQuery();			
					while (rsEje.next())
					{
						ArrayIDUsuario.add(rsEje.getInt(1));
						ArrayCantLeads.add(rsEje.getInt(2));
					}
				}
				catch (SQLException e)
				{
				
					throw new DBException(e, sqlEje.toString());
				}
				finally
				{
					DB.close(rsEje, pstmtEje);
					rsEje = null; pstmtEje = null;
				}
				//fin calculo de carga
				
				//calculo de etiquetas	
				log.config("arrayidusuario: "+ArrayIDUsuario+" SQLEJE: "+sqlEje); //mfrojas
				int contador = 0;
				int InterestArea1_ID = 0; //int InterestAreaValue1_ID = 0;
				int InterestArea2_ID = 0; //int InterestAreaValue2_ID = 0;
				int InterestArea3_ID = 0; //int InterestAreaValue3_ID = 0;
				//selecciona las etiquetas de una campaña unidas con las etiquetas obligatorias.
				String sqlEtiquetas = "SELECT ci.R_InterestArea_ID, COALESCE(MAX(ci.R_InterestAreaValues_ID),0) as R_InterestAreaValues_ID "+
						"FROM C_Campaign cc INNER JOIN R_CampaignInterest ci ON (cc.C_Campaign_ID = ci.C_Campaign_ID) "+
						"WHERE cc.C_Campaign_ID=? GROUP BY ci.R_InterestArea_ID ";
				
				//sobreescribimos id de variable etiqueta URL ininoles en vacaciones :D
				/*int ID_EtiquetaURL = DB.getSQLValue(get_TrxName(), "SELECT MAX(R_InterestArea_ID) FROM R_InterestArea WHERE value like 'Lead Web'");
				if (ID_EtiquetaURL > 0)
					ID_EtiquetaUrlGL = ID_EtiquetaURL;
				*/
				PreparedStatement pstmtEt = null;
				pstmtEt = DB.prepareStatement(sqlEtiquetas, get_TrxName());
				pstmtEt.setInt(1, cam.get_ID()); 
				//pstmtEt.setInt(2, ID_EtiquetaUrlGL); 
				log.config("sqlEtiquetas: "+sqlEtiquetas ); //mfrojas
				ResultSet rsEt = pstmtEt.executeQuery();
				
				while (rsEt.next())
				{
					log.config("contador: "+contador);
					contador++;
					if (contador == 1)
					{
						InterestArea1_ID = rsEt.getInt("R_InterestArea_ID");
						log.config("interestarea1 : "+InterestArea1_ID); //mfrojas
						//InterestAreaValue1_ID = rsEt.getInt("R_InterestAreaValues_ID");
					}						
					else if (contador == 2)
					{
						InterestArea2_ID = rsEt.getInt("R_InterestArea_ID");
						log.config("interestarea2 : "+InterestArea2_ID); //mfrojas

						//InterestAreaValue2_ID = rsEt.getInt("R_InterestAreaValues_ID");
					}
					else if (contador == 3)
					{
						InterestArea3_ID = rsEt.getInt("R_InterestArea_ID");
						log.config("interestarea3: "+InterestArea3_ID);
						//InterestAreaValue3_ID = rsEt.getInt("R_InterestAreaValues_ID");
					}
				}		
				//fin lista de etiquetas y valores
				//ciclo lineas de de campaña
				String sqlCLines = "SELECT C_CampaignFollow_ID FROM C_CampaignFollow WHERE C_Campaign_ID = ? " +
						" AND FinalStatus IN ('IN') Order By C_CampaignFollow_ID";
						
				PreparedStatement pstmtCLines =  null;
				ResultSet rsCLines = null;
				log.config("sqlCLines: "+sqlCLines); //mfrojas
				try
				{
					pstmtCLines = DB.prepareStatement(sqlCLines, get_TrxName());
					pstmtCLines.setInt(1, cam.get_ID());
					rsCLines = pstmtCLines.executeQuery();
							
					while (rsCLines.next())
					{
						X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), rsCLines.getInt("C_CampaignFollow_ID"), get_TrxName());
						
						log.config("INT1: " +InterestArea1_ID+"  INT2: "+InterestArea2_ID+"   INT3: "+InterestArea3_ID);
						//etiquetas y valores de socio de negocio
						String sqlEtBP = "SELECT R_InterestArea_ID, MAX(R_InterestAreaValues_ID) as R_InterestAreaValues_ID FROM R_ContactInterest "+
								"WHERE C_Bpartner_ID = ? AND R_InterestArea_ID IN ("+InterestArea1_ID+","+InterestArea2_ID+","+InterestArea3_ID+")" +
										" GROUP BY R_InterestArea_ID ";
						int contadorBP = 0;
						int InterestAreaBP1_ID = 0; int InterestAreaValueBP1_ID = 0;
						int InterestAreaBP2_ID = 0; int InterestAreaValueBP2_ID = 0;
						int InterestAreaBP3_ID = 0; int InterestAreaValueBP3_ID = 0;
								
						PreparedStatement pstmtEtBP = null;
						pstmtEtBP = DB.prepareStatement(sqlEtBP, get_TrxName());
						pstmtEtBP.setInt(1, cFollow.getC_BPartner_ID());				
						ResultSet rsEtBP = pstmtEtBP.executeQuery();
						log.config("sqlEtBP: "+sqlEtBP); //mfrojas	
						while (rsEtBP.next())
						{
							contadorBP++;
							if (contadorBP == 1)
							{
								InterestAreaBP1_ID = rsEtBP.getInt("R_InterestArea_ID");
								
								InterestAreaValueBP1_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
								log.config("InterestAreaBP1_ID: "+InterestAreaBP1_ID+"  InterestAreaValueBP1_ID: "+InterestAreaValueBP1_ID); //mfrojas
							}						
							else if (contadorBP == 2)
							{
								InterestAreaBP2_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP2_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
								log.config("InterestAreaBP2_ID: "+InterestAreaBP2_ID+"  InterestAreaValueBP2_ID: "+InterestAreaValueBP2_ID);//mfrojas

							}
							else if (contadorBP == 3)
							{
								InterestAreaBP3_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP3_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
								log.config("InterestAreaBP3_ID: "+InterestAreaBP3_ID+"  InterestAreaValueBP3_ID: "+InterestAreaValueBP3_ID);//mfrojas

							}
						}
						//fin seteo valores de socio de negocio
						
						//ciclo ID usuarios posibles de asignar
						String id_IaBPsql = "0";
						if (InterestAreaBP1_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP1_ID;
						if (InterestAreaBP2_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP2_ID;
						if (InterestAreaBP3_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP3_ID;
						
						String id_IaVBPsql = "0";
						log.config("id_IaBPsql: "+id_IaBPsql);
						if (InterestAreaValueBP1_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP1_ID;
						if (InterestAreaValueBP2_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP2_ID;
						if (InterestAreaValueBP3_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP3_ID;
						
						log.config("id_iavbpsql: "+id_IaVBPsql);
						//Sobreescribimos id de cadenas y variables de valores para que si no es RM solo tome la region
						int ID_IsRM = DB.getSQLValue(get_TrxName(), "SELECT MAX(rci.R_InterestArea_ID) FROM R_ContactInterest rci" +								
								" INNER JOIN R_InterestAreaValues riaV ON (rci.R_InterestAreaValues_ID = riaV.R_InterestAreaValues_ID)" +
								" WHERE rci.C_Bpartner_ID = "+cFollow.getC_BPartner_ID()+" AND riaV.ISVALUERM = 'Y' GROUP BY rci.R_InterestArea_ID ");
						log.config("ID_IsRM: "+ID_IsRM); //mfrojas 
						if (ID_IsRM > 0)
						{
							;
						}
						else
						{    
							id_IaBPsql = "0";
							id_IaVBPsql = "0";
							
							int ID_EtiquetaFiltro = DB.getSQLValue(get_TrxName(), "SELECT R_InterestArea_ID, 0 FROM R_InterestArea WHERE IsFilter = 'Y'");
							if (ID_EtiquetaFiltro > 0)
							{
								id_IaBPsql = id_IaBPsql + ","+ID_EtiquetaFiltro;
								int ID_ValEF = DB.getSQLValue(get_TrxName(), "SELECT R_InterestAreaValues_ID FROM R_ContactInterest " +
										" WHERE C_Bpartner_ID = "+cFollow.getC_BPartner_ID()+" AND R_InterestArea_ID IN ("+ID_EtiquetaFiltro+")");
								if (ID_ValEF > 0)
									id_IaVBPsql = id_IaVBPsql+","+ID_ValEF;
							}		
							InterestAreaBP2_ID = 0;
							InterestAreaBP3_ID = 0; 
						}					
						String sqlEtUser = "SELECT DISTINCT(AD_User_ID), " +
								" COALESCE((SELECT COUNT(1) FROM C_CampaignFollow WHERE C_Campaign_ID = "+cFollow.getC_Campaign_ID()+"AND AD_User_ID = ui.AD_User_ID),0) as cant " +
								" FROM R_UserInterest ui "+
								" WHERE IsActive = 'Y' AND R_InterestArea_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues_ID IN ("+id_IaVBPsql+") OR R_InterestAreaValues_ID IS NULL) ";
						if (InterestAreaBP2_ID > 0)
						{
							sqlEtUser = sqlEtUser + " AND R_InterestAreaRef2_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues2_ID IN ("+id_IaVBPsql+") OR R_InterestAreaValues2_ID IS NULL)";
						}else
						{
							sqlEtUser = sqlEtUser + " AND R_InterestAreaRef2_ID IS NULL";
						}						
						if (InterestAreaBP3_ID > 0)
						{
							sqlEtUser = sqlEtUser + " AND R_InterestAreaRef3_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues3_ID IN ("+id_IaVBPsql+") OR R_InterestAreaValues3_ID IS NULL)";
						}
						else
						{
							sqlEtUser = sqlEtUser + " AND R_InterestAreaRef3_ID IS NULL";
						}
						sqlEtUser = sqlEtUser + " ORDER BY cant desc";
						
						log.config("sqlEtUser: "+sqlEtUser); //mfrojas
						PreparedStatement pstmtEtUser = null; 
						pstmtEtUser = DB.prepareStatement(sqlEtUser, get_TrxName());
						ResultSet rsEtUser = pstmtEtUser.executeQuery();
						
						int minCantidad = 100000;
						int CantLeadEje = 0;
						int ID_EjecutivoM = 0;
						while (rsEtUser.next())
						{				
							if (ArrayIDUsuario != null)
							{
								if(ArrayIDUsuario.contains(rsEtUser.getInt(1)))
								{
									int indexCant = ArrayIDUsuario.indexOf(rsEtUser.getInt(1));
									CantLeadEje = ArrayCantLeads.get(indexCant);
								}
								else
								{
									//ID_EjecutivoM = rsEtUser.getInt(1);
									CantLeadEje = 0;
								}
							}
							if (CantLeadEje < minCantidad)
							{
								minCantidad = CantLeadEje;
								ID_EjecutivoM = rsEtUser.getInt(1);
							}				
						}
						//seteamos el valor del ejecutivo menor que esta guardado en una variable			
						if (ID_EjecutivoM > 0)
						{
							cFollow.setAD_User_ID(ID_EjecutivoM);
							cFollow.setFinalStatus("AS");
							cFollow.save();
							//actualizamos carga de ejecutivos
							if(ArrayIDUsuario.contains(ID_EjecutivoM))
							{
								int indexCant = ArrayIDUsuario.indexOf(ID_EjecutivoM);
								int CantLeadEjeN = ArrayCantLeads.get(indexCant);
								CantLeadEjeN = CantLeadEjeN + 1;
								ArrayCantLeads.set(indexCant, CantLeadEjeN);
							}
							else
							{
								ArrayIDUsuario.add(ID_EjecutivoM);
								ArrayCantLeads.add(1);
							}
						}												
					}	
				}
				catch (SQLException e)
				{
				
					throw new DBException(e, sqlCLines.toString());
				}
				finally
				{
					DB.close(rsCLines, pstmtCLines);
					rsCLines = null; pstmtCLines = null;
				}		
		return true;
	}
	
	public Boolean asignUserAgencia(int ID_Campaign) throws SQLException
	{	
			int ID_EtiquetaUrlGL = 0;
				X_C_Campaign cam = new X_C_Campaign(getCtx(),ID_Campaign,get_TrxName());
				
				//calculo carga de ejecutivos 
				//mfrojas son los ejecutivos que existen en las campañas a asignar los socios nuevos.
				//ininoles se modifica para que solo tome ne cuenta los en estado asignado
				/*String sqlEje = "SELECT DISTINCT (cf.AD_User_ID), COALESCE((SELECT COUNT(1) FROM C_CampaignFollow " +
						" WHERE C_Campaign_ID = cf.C_Campaign_ID AND AD_User_ID = cf.AD_User_ID),0) as count FROM C_CampaignFollow cf" +
						" WHERE cf.C_Campaign_ID = ? AND cf.AD_User_ID IS NOT NULL AND FinalStatus IN ('AS','CO','IN')";
				*/
				String sqlEje = "SELECT DISTINCT (cf.AD_User_ID), COALESCE((SELECT COUNT(1) FROM C_CampaignFollow " +
				" WHERE C_Campaign_ID = cf.C_Campaign_ID AND AD_User_ID = cf.AD_User_ID),0) as count FROM C_CampaignFollow cf" +
				" WHERE cf.C_Campaign_ID = ? AND cf.AD_User_ID IS NOT NULL AND FinalStatus = 'AS'";
				
				ArrayList<Integer> ArrayIDUsuario = new ArrayList<Integer>();
				ArrayList<Integer> ArrayCantLeads = new ArrayList<Integer>();
				
				PreparedStatement pstmtEje =  null;
				ResultSet rsEje = null;
				try
				{
					pstmtEje = DB.prepareStatement(sqlEje.toString(), get_TrxName());
					pstmtEje.setInt(1, cam.get_ID());
					rsEje = pstmtEje.executeQuery();			
					while (rsEje.next())
					{
						ArrayIDUsuario.add(rsEje.getInt(1));
						ArrayCantLeads.add(rsEje.getInt(2));
					}
				}
				catch (SQLException e)
				{
				
					throw new DBException(e, sqlEje.toString());
				}
				finally
				{
					DB.close(rsEje, pstmtEje);
					rsEje = null; pstmtEje = null;
				}
				//fin calculo de carga
			
				//calculo de etiquetas	
				log.config("arrayidusuario: "+ArrayIDUsuario+" SQLEJE: "+sqlEje); //mfrojas
				int contador = 0;
				int InterestArea1_ID = 0; //int InterestAreaValue1_ID = 0;
				int InterestArea2_ID = 0; //int InterestAreaValue2_ID = 0;
				int InterestArea3_ID = 0; //int InterestAreaValue3_ID = 0;
				//selecciona las etiquetas de una campaña unidas con las etiquetas obligatorias.
				String sqlEtiquetas = "SELECT ci.R_InterestArea_ID, COALESCE(MAX(ci.R_InterestAreaValues_ID),0) as R_InterestAreaValues_ID "+
						"FROM C_Campaign cc INNER JOIN R_CampaignInterest ci ON (cc.C_Campaign_ID = ci.C_Campaign_ID) "+
						"WHERE cc.C_Campaign_ID=? AND ci.R_InterestArea_ID <> ? GROUP BY ci.R_InterestArea_ID "+ 
						"UNION SELECT R_InterestArea_ID, 0 FROM R_InterestArea WHERE IsFilter = 'Y' ";
				
				//sobreescribimos id de variable etiqueta URL ininoles en vacaciones :D
				int ID_EtiquetaURL = DB.getSQLValue(get_TrxName(), "SELECT MAX(R_InterestArea_ID) FROM R_InterestArea WHERE value like 'Lead Web'");
				if (ID_EtiquetaURL > 0)
					ID_EtiquetaUrlGL = ID_EtiquetaURL;
				
				PreparedStatement pstmtEt = null;
				pstmtEt = DB.prepareStatement(sqlEtiquetas, get_TrxName());
				pstmtEt.setInt(1, cam.get_ID()); 
				pstmtEt.setInt(2, ID_EtiquetaUrlGL); 
				log.config("sqlEtiquetas: "+sqlEtiquetas ); //mfrojas
				ResultSet rsEt = pstmtEt.executeQuery();
				
				while (rsEt.next())
				{
					log.config("contador: "+contador);
					contador++;
					if (contador == 1)
					{
						InterestArea1_ID = rsEt.getInt("R_InterestArea_ID");
						log.config("interestarea1 : "+InterestArea1_ID); //mfrojas
						//InterestAreaValue1_ID = rsEt.getInt("R_InterestAreaValues_ID");
					}						
					else if (contador == 2)
					{
						InterestArea2_ID = rsEt.getInt("R_InterestArea_ID");
						log.config("interestarea2 : "+InterestArea2_ID); //mfrojas

						//InterestAreaValue2_ID = rsEt.getInt("R_InterestAreaValues_ID");
					}
					else if (contador == 3)
					{
						InterestArea3_ID = rsEt.getInt("R_InterestArea_ID");
						log.config("interestarea3: "+InterestArea3_ID);
						//InterestAreaValue3_ID = rsEt.getInt("R_InterestAreaValues_ID");
					}
				}		
				//fin lista de etiquetas y valores
				//ciclo lineas de de campaña
				String sqlCLines = "SELECT C_CampaignFollow_ID FROM C_CampaignFollow WHERE C_Campaign_ID = ? " +
						" AND FinalStatus IN ('IN') Order By C_CampaignFollow_ID";
						
				PreparedStatement pstmtCLines =  null;
				ResultSet rsCLines = null;
				log.config("sqlCLines: "+sqlCLines); //mfrojas
				try
				{
					pstmtCLines = DB.prepareStatement(sqlCLines, get_TrxName());
					pstmtCLines.setInt(1, cam.get_ID());
					rsCLines = pstmtCLines.executeQuery();
							
					while (rsCLines.next())
					{
						X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), rsCLines.getInt("C_CampaignFollow_ID"), get_TrxName());
						
						log.config("INT1: " +InterestArea1_ID+"  INT2: "+InterestArea2_ID+"   INT3: "+InterestArea3_ID);
						//etiquetas y valores de socio de negocio
						String sqlEtBP = "SELECT R_InterestArea_ID, MAX(R_InterestAreaValues_ID) as R_InterestAreaValues_ID FROM R_ContactInterest "+
								"WHERE C_Bpartner_ID = ? AND R_InterestArea_ID IN ("+InterestArea1_ID+","+InterestArea2_ID+","+InterestArea3_ID+")" +
										" GROUP BY R_InterestArea_ID ";
						int contadorBP = 0;
						int InterestAreaBP1_ID = 0; int InterestAreaValueBP1_ID = 0;
						int InterestAreaBP2_ID = 0; int InterestAreaValueBP2_ID = 0;
						int InterestAreaBP3_ID = 0; int InterestAreaValueBP3_ID = 0;
								
						PreparedStatement pstmtEtBP = null;
						pstmtEtBP = DB.prepareStatement(sqlEtBP, get_TrxName());
						pstmtEtBP.setInt(1, cFollow.getC_BPartner_ID());				
						ResultSet rsEtBP = pstmtEtBP.executeQuery();
						log.config("sqlEtBP: "+sqlEtBP); //mfrojas	
						while (rsEtBP.next())
						{
							contadorBP++;
							if (contadorBP == 1)
							{
								InterestAreaBP1_ID = rsEtBP.getInt("R_InterestArea_ID");
								
								InterestAreaValueBP1_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
								log.config("InterestAreaBP1_ID: "+InterestAreaBP1_ID+"  InterestAreaValueBP1_ID: "+InterestAreaValueBP1_ID); //mfrojas
							}						
							else if (contadorBP == 2)
							{
								InterestAreaBP2_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP2_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
								log.config("InterestAreaBP2_ID: "+InterestAreaBP2_ID+"  InterestAreaValueBP2_ID: "+InterestAreaValueBP2_ID);//mfrojas

							}
							else if (contadorBP == 3)
							{
								InterestAreaBP3_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP3_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
								log.config("InterestAreaBP3_ID: "+InterestAreaBP3_ID+"  InterestAreaValueBP3_ID: "+InterestAreaValueBP3_ID);//mfrojas

							}
						}
						//fin seteo valores de socio de negocio
						
						//ciclo ID usuarios posibles de asignar
						String id_IaBPsql = "0";
						if (InterestAreaBP1_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP1_ID;
						if (InterestAreaBP2_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP2_ID;
						if (InterestAreaBP3_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP3_ID;
						
						String id_IaVBPsql = "0";
						log.config("id_IaBPsql: "+id_IaBPsql);
						if (InterestAreaValueBP1_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP1_ID;
						if (InterestAreaValueBP2_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP2_ID;
						if (InterestAreaValueBP3_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP3_ID;
						
						log.config("id_iavbpsql: "+id_IaVBPsql);
										
						String sqlEtUser = "SELECT DISTINCT(ui.AD_User_ID), " +
								" COALESCE((SELECT COUNT(1) FROM C_CampaignFollow WHERE C_Campaign_ID = "+cFollow.getC_Campaign_ID()+"AND AD_User_ID = ui.AD_User_ID),0) as cant " +
								" FROM R_UserInterest ui " +
								" INNER JOIN AD_User adu ON (ui.AD_User_ID = adu.AD_User_ID) "+
								" WHERE ui.IsActive = 'Y'  AND adu.ActivePool = 'Y' AND ui.R_InterestArea_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues_ID IN ("+id_IaVBPsql+") OR ui.R_InterestAreaValues_ID IS NULL) ";
						if (InterestAreaBP2_ID > 0)
						{
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef2_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues2_ID IN ("+id_IaVBPsql+") OR ui.R_InterestAreaValues2_ID IS NULL)";
						}else
						{
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef2_ID IS NULL";
						}						
						if (InterestAreaBP3_ID > 0)
						{
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef3_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues3_ID IN ("+id_IaVBPsql+") OR ui.R_InterestAreaValues3_ID IS NULL)";
						}
						else
						{
							sqlEtUser = sqlEtUser + " AND ui.R_InterestAreaRef3_ID IS NULL";
						}
						sqlEtUser = sqlEtUser + " ORDER BY cant desc";
						
						log.config("sqlEtUser: "+sqlEtUser); //mfrojas
						PreparedStatement pstmtEtUser = null; 
						pstmtEtUser = DB.prepareStatement(sqlEtUser, get_TrxName());
						ResultSet rsEtUser = pstmtEtUser.executeQuery();
						
						int minCantidad = 100000;
						int CantLeadEje = 0;
						int ID_EjecutivoM = 0;
						while (rsEtUser.next())
						{				
							if (ArrayIDUsuario != null)
							{
								if(ArrayIDUsuario.contains(rsEtUser.getInt(1)))
								{
									int indexCant = ArrayIDUsuario.indexOf(rsEtUser.getInt(1));
									CantLeadEje = ArrayCantLeads.get(indexCant);
								}
								else
								{
									//ID_EjecutivoM = rsEtUser.getInt(1);
									CantLeadEje = 0;
								}
							}
							if (CantLeadEje < minCantidad)
							{
								minCantidad = CantLeadEje;
								ID_EjecutivoM = rsEtUser.getInt(1);
							}				
						}
						//seteamos el valor del ejecutivo menor que esta guardado en una variable			
						if (ID_EjecutivoM > 0)
						{
							cFollow.set_CustomColumn("Supervisor_ID", ID_EjecutivoM);
							cFollow.setFinalStatus("AS");
							cFollow.save();
							//actualizamos carga de ejecutivos
							if(ArrayIDUsuario.contains(ID_EjecutivoM))
							{
								int indexCant = ArrayIDUsuario.indexOf(ID_EjecutivoM);
								int CantLeadEjeN = ArrayCantLeads.get(indexCant);
								CantLeadEjeN = CantLeadEjeN + 1;
								ArrayCantLeads.set(indexCant, CantLeadEjeN);
							}
							else
							{
								ArrayIDUsuario.add(ID_EjecutivoM);
								ArrayCantLeads.add(1);
							}
						}												
					}	
				}
				catch (SQLException e)
				{
				
					throw new DBException(e, sqlCLines.toString());
				}
				finally
				{
					DB.close(rsCLines, pstmtCLines);
					rsCLines = null; pstmtCLines = null;
				}		
		return true;
	}
	
	public Boolean SubAsignUserAgencia(int ID_Campaign) throws SQLException
	{	
				X_C_Campaign cam = new X_C_Campaign(getCtx(),ID_Campaign,get_TrxName());
				
				String sqlCLines = "SELECT C_CampaignFollow_ID FROM C_CampaignFollow WHERE C_Campaign_ID = ? " +
				" AND Supervisor_ID > 0 AND (AD_User_ID = 0 OR AD_User_ID is null) Order By C_CampaignFollow_ID";
				
				PreparedStatement pstmtCLines =  null;
				ResultSet rsCLines = null;
				log.config("sqlCLines: "+sqlCLines); //mfrojas
				try
				{
					pstmtCLines = DB.prepareStatement(sqlCLines, get_TrxName());
					pstmtCLines.setInt(1, cam.get_ID());
					rsCLines = pstmtCLines.executeQuery();
							
					while (rsCLines.next())
					{
						X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), rsCLines.getInt("C_CampaignFollow_ID"), get_TrxName());
						
						int cantIndUser1 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_User WHERE Supervisor_ID = "+cFollow.get_ValueAsInt("Supervisor_ID")+
							"AND UseAgencia = 'N' AND qtylead > 0 AND IsActive = 'Y'");
						
						if (cantIndUser1 < 1)
						{
							DB.executeUpdate("UPDATE AD_User SET UseAgencia = 'N' WHERE Supervisor_ID = "+cFollow.get_ValueAsInt("Supervisor_ID"), get_TrxName());
							commitEx();
						}
						int cantIndUser2 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_User WHERE Supervisor_ID = "+cFollow.get_ValueAsInt("Supervisor_ID")+
						"AND UseAgencia = 'N' AND qtylead > 0 AND IsActive = 'Y'");					
						if (cantIndUser2 < 1)
						{
							DB.executeUpdate("UPDATE AD_User SET qtyLead = qtyLeadBase WHERE Supervisor_ID = "+cFollow.get_ValueAsInt("Supervisor_ID"), get_TrxName());
							commitEx();
						}
						
						String sqlPUser = "SELECT AD_User_ID FROM AD_User WHERE Supervisor_ID = "+cFollow.get_ValueAsInt("Supervisor_ID") +
						"AND UseAgencia = 'N' AND qtyLead > 0 AND IsActive = 'Y' ORDER BY qtyLead Desc";
					
						PreparedStatement pstmtSubUser =  null;
						ResultSet rsSubUser = null;
						
						pstmtSubUser = DB.prepareStatement(sqlPUser, get_TrxName());
						rsSubUser = pstmtSubUser.executeQuery();						
						rsSubUser.next();
						
						if (rsSubUser.getInt("AD_User_ID") > 0)
						{
							cFollow.set_CustomColumn("AD_User_ID", rsSubUser.getInt("AD_User_ID"));						
							cFollow.save();
							DB.executeUpdate("UPDATE AD_User SET UseAgencia = 'Y', qtyLead = qtyLead-1 WHERE AD_User_ID = "+rsSubUser.getString("AD_User_ID"), get_TrxName());
						}						
						commitEx();
						rsSubUser.close();
						pstmtSubUser.close();
					}			
				}
				catch (SQLException e)
				{
				
					throw new DBException(e, sqlCLines.toString());
				}
				finally
				{
					DB.close(rsCLines, pstmtCLines);
					rsCLines = null; pstmtCLines = null;
				}			
		commitEx();
		return true;
	}
	

	public Boolean asignUserCConsumo(int ID_Campaign) throws SQLException
	{	
		int ID_EtiquetaUrlGL = 0;
				X_C_Campaign cam = new X_C_Campaign(getCtx(),ID_Campaign,get_TrxName());
				
				//calculo carga de ejecutivos 
				//mfrojas son los ejecutivos que existen en las campañas a asignar los socios nuevos.
				//ininoles se modifica para que solo tome ne cuenta los en estado asignado
				/*String sqlEje = "SELECT DISTINCT (cf.AD_User_ID), COALESCE((SELECT COUNT(1) FROM C_CampaignFollow " +
						" WHERE C_Campaign_ID = cf.C_Campaign_ID AND AD_User_ID = cf.AD_User_ID),0) as count FROM C_CampaignFollow cf" +
						" WHERE cf.C_Campaign_ID = ? AND cf.AD_User_ID IS NOT NULL AND FinalStatus IN ('AS','CO','IN')";
				*/
				String sqlEje = "SELECT DISTINCT (cf.AD_User_ID), COALESCE((SELECT COUNT(1) FROM C_CampaignFollow " +
				" WHERE C_Campaign_ID = cf.C_Campaign_ID AND AD_User_ID = cf.AD_User_ID),0) as count FROM C_CampaignFollow cf" +
				" WHERE cf.C_Campaign_ID = ? AND cf.AD_User_ID IS NOT NULL AND FinalStatus = 'AS'";
				
				ArrayList<Integer> ArrayIDUsuario = new ArrayList<Integer>();
				ArrayList<Integer> ArrayCantLeads = new ArrayList<Integer>();
				
				PreparedStatement pstmtEje =  null;
				ResultSet rsEje = null;
				try
				{
					pstmtEje = DB.prepareStatement(sqlEje.toString(), get_TrxName());
					pstmtEje.setInt(1, cam.get_ID());
					rsEje = pstmtEje.executeQuery();			
					while (rsEje.next())
					{
						ArrayIDUsuario.add(rsEje.getInt(1));
						ArrayCantLeads.add(rsEje.getInt(2));
					}
				}
				catch (SQLException e)
				{
				
					throw new DBException(e, sqlEje.toString());
				}
				finally
				{
					DB.close(rsEje, pstmtEje);
					rsEje = null; pstmtEje = null;
				}
				//fin calculo de carga
				
				//calculo de etiquetas	
				log.config("arrayidusuario: "+ArrayIDUsuario+" SQLEJE: "+sqlEje); //mfrojas
				int contador = 0;
				int InterestArea1_ID = 0; //int InterestAreaValue1_ID = 0;
				int InterestArea2_ID = 0; //int InterestAreaValue2_ID = 0;
				int InterestArea3_ID = 0; //int InterestAreaValue3_ID = 0;
				//selecciona las etiquetas de una campaña unidas con las etiquetas obligatorias.
				String sqlEtiquetas = "SELECT ci.R_InterestArea_ID, COALESCE(MAX(ci.R_InterestAreaValues_ID),0) as R_InterestAreaValues_ID "+
						"FROM C_Campaign cc INNER JOIN R_CampaignInterest ci ON (cc.C_Campaign_ID = ci.C_Campaign_ID) "+
						"WHERE cc.C_Campaign_ID=? AND ci.R_InterestArea_ID <> ? GROUP BY ci.R_InterestArea_ID "+ 
						"UNION SELECT R_InterestArea_ID, 0 FROM R_InterestArea WHERE IsFilter = 'Y' ";
				
				log.config("Etiqueta: "+ID_EtiquetaUrlGL); //mfrojas 
				//sobreescribimos id de variable etiqueta URL ininoles en vacaciones :D
				int ID_EtiquetaURL = DB.getSQLValue(get_TrxName(), "SELECT MAX(R_InterestArea_ID) FROM R_InterestArea WHERE value like 'Lead Sitio Privado'");
				if (ID_EtiquetaURL > 0)
					ID_EtiquetaUrlGL = ID_EtiquetaURL;
				
				PreparedStatement pstmtEt = null;
				pstmtEt = DB.prepareStatement(sqlEtiquetas, get_TrxName());
				pstmtEt.setInt(1, cam.get_ID()); 
				pstmtEt.setInt(2, ID_EtiquetaUrlGL); 
				log.config("sqlEtiquetas: "+sqlEtiquetas ); //mfrojas
				ResultSet rsEt = pstmtEt.executeQuery();
				
				while (rsEt.next())
				{
					log.config("contador: "+contador);
					contador++;
					if (contador == 1)
					{
						InterestArea1_ID = rsEt.getInt("R_InterestArea_ID");
						log.config("interestarea1 : "+InterestArea1_ID); //mfrojas
						//InterestAreaValue1_ID = rsEt.getInt("R_InterestAreaValues_ID");
					}						
					else if (contador == 2)
					{
						InterestArea2_ID = rsEt.getInt("R_InterestArea_ID");
						log.config("interestarea2 : "+InterestArea2_ID); //mfrojas

						//InterestAreaValue2_ID = rsEt.getInt("R_InterestAreaValues_ID");
					}
					else if (contador == 3)
					{
						InterestArea3_ID = rsEt.getInt("R_InterestArea_ID");
						log.config("interestarea3: "+InterestArea3_ID);
						//InterestAreaValue3_ID = rsEt.getInt("R_InterestAreaValues_ID");
					}
				}		
				//fin lista de etiquetas y valores
				//ciclo lineas de de campaña
				String sqlCLines = "SELECT C_CampaignFollow_ID FROM C_CampaignFollow WHERE C_Campaign_ID = ? " +
						" AND FinalStatus IN ('IN') Order By C_CampaignFollow_ID";
						
				PreparedStatement pstmtCLines =  null;
				ResultSet rsCLines = null;
				log.config("sqlCLines: "+sqlCLines); //mfrojas
				try
				{
					pstmtCLines = DB.prepareStatement(sqlCLines, get_TrxName());
					pstmtCLines.setInt(1, cam.get_ID());
					rsCLines = pstmtCLines.executeQuery();
							
					while (rsCLines.next())
					{
						X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), rsCLines.getInt("C_CampaignFollow_ID"), get_TrxName());
						
						log.config("INT1: " +InterestArea1_ID+"  INT2: "+InterestArea2_ID+"   INT3: "+InterestArea3_ID);
						//etiquetas y valores de socio de negocio
						String sqlEtBP = "SELECT R_InterestArea_ID, MAX(R_InterestAreaValues_ID) as R_InterestAreaValues_ID FROM R_ContactInterest "+
								"WHERE C_Bpartner_ID = ? AND R_InterestArea_ID IN ("+InterestArea1_ID+","+InterestArea2_ID+","+InterestArea3_ID+")" +
										" GROUP BY R_InterestArea_ID ";
						int contadorBP = 0;
						int InterestAreaBP1_ID = 0; int InterestAreaValueBP1_ID = 0;
						int InterestAreaBP2_ID = 0; int InterestAreaValueBP2_ID = 0;
						int InterestAreaBP3_ID = 0; int InterestAreaValueBP3_ID = 0;
								
						PreparedStatement pstmtEtBP = null;
						pstmtEtBP = DB.prepareStatement(sqlEtBP, get_TrxName());
						pstmtEtBP.setInt(1, cFollow.getC_BPartner_ID());				
						ResultSet rsEtBP = pstmtEtBP.executeQuery();
						log.config("sqlEtBP: "+sqlEtBP); //mfrojas	
						while (rsEtBP.next())
						{
							contadorBP++;
							if (contadorBP == 1)
							{
								InterestAreaBP1_ID = rsEtBP.getInt("R_InterestArea_ID");
								
								InterestAreaValueBP1_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
								log.config("InterestAreaBP1_ID: "+InterestAreaBP1_ID+"  InterestAreaValueBP1_ID: "+InterestAreaValueBP1_ID); //mfrojas
							}						
							else if (contadorBP == 2)
							{
								InterestAreaBP2_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP2_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
								log.config("InterestAreaBP2_ID: "+InterestAreaBP2_ID+"  InterestAreaValueBP2_ID: "+InterestAreaValueBP2_ID);//mfrojas

							}
							else if (contadorBP == 3)
							{
								InterestAreaBP3_ID = rsEtBP.getInt("R_InterestArea_ID");
								InterestAreaValueBP3_ID = rsEtBP.getInt("R_InterestAreaValues_ID");
								log.config("InterestAreaBP3_ID: "+InterestAreaBP3_ID+"  InterestAreaValueBP3_ID: "+InterestAreaValueBP3_ID);//mfrojas

							}
						}
						//fin seteo valores de socio de negocio
						
						//ciclo ID usuarios posibles de asignar
						String id_IaBPsql = "0";
						if (InterestAreaBP1_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP1_ID;
						if (InterestAreaBP2_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP2_ID;
						if (InterestAreaBP3_ID > 0 )
							id_IaBPsql = id_IaBPsql +","+InterestAreaBP3_ID;
						
						String id_IaVBPsql = "0";
						log.config("id_IaBPsql: "+id_IaBPsql);
						if (InterestAreaValueBP1_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP1_ID;
						if (InterestAreaValueBP2_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP2_ID;
						if (InterestAreaValueBP3_ID > 0 )
							id_IaVBPsql = id_IaVBPsql +","+InterestAreaValueBP3_ID;
											
						String sqlEtUser = "SELECT DISTINCT(AD_User_ID), " +
								" COALESCE((SELECT COUNT(1) FROM C_CampaignFollow WHERE C_Campaign_ID = "+cFollow.getC_Campaign_ID()+"AND AD_User_ID = ui.AD_User_ID),0) as cant " +
								" FROM R_UserInterest ui "+
								" WHERE IsActive = 'Y' AND R_InterestArea_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues_ID IN ("+id_IaVBPsql+") OR R_InterestAreaValues_ID IS NULL) ";
						if (InterestAreaBP2_ID > 0)
						{
							sqlEtUser = sqlEtUser + " AND R_InterestAreaRef2_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues2_ID IN ("+id_IaVBPsql+") OR R_InterestAreaValues2_ID IS NULL)";
						}else
						{
							sqlEtUser = sqlEtUser + " AND R_InterestAreaRef2_ID IS NULL";
						}						
						if (InterestAreaBP3_ID > 0)
						{
							sqlEtUser = sqlEtUser + " AND R_InterestAreaRef3_ID IN ("+id_IaBPsql+") AND (R_InterestAreaValues3_ID IN ("+id_IaVBPsql+") OR R_InterestAreaValues3_ID IS NULL)";
						}
						else
						{
							sqlEtUser = sqlEtUser + " AND R_InterestAreaRef3_ID IS NULL";
						}
						sqlEtUser = sqlEtUser + " ORDER BY cant desc";
						
						log.config("sqlEtUser: "+sqlEtUser); //mfrojas
						PreparedStatement pstmtEtUser = null; 
						pstmtEtUser = DB.prepareStatement(sqlEtUser, get_TrxName());
						ResultSet rsEtUser = pstmtEtUser.executeQuery();
						
						int minCantidad = 100000;
						int CantLeadEje = 0;
						int ID_EjecutivoM = 0;
						while (rsEtUser.next())
						{				
							if (ArrayIDUsuario != null)
							{
								if(ArrayIDUsuario.contains(rsEtUser.getInt(1)))
								{
									int indexCant = ArrayIDUsuario.indexOf(rsEtUser.getInt(1));
									CantLeadEje = ArrayCantLeads.get(indexCant);
								}
								else
								{
									//ID_EjecutivoM = rsEtUser.getInt(1);
									CantLeadEje = 0;
								}
							}
							if (CantLeadEje < minCantidad)
							{
								minCantidad = CantLeadEje;
								ID_EjecutivoM = rsEtUser.getInt(1);
							}				
						}
						//seteamos el valor del ejecutivo menor que esta guardado en una variable			
						if (ID_EjecutivoM > 0)
						{
							cFollow.setAD_User_ID(ID_EjecutivoM);
							cFollow.setFinalStatus("AS");
							cFollow.save();
							//actualizamos carga de ejecutivos
							if(ArrayIDUsuario.contains(ID_EjecutivoM))
							{
								int indexCant = ArrayIDUsuario.indexOf(ID_EjecutivoM);
								int CantLeadEjeN = ArrayCantLeads.get(indexCant);
								CantLeadEjeN = CantLeadEjeN + 1;
								ArrayCantLeads.set(indexCant, CantLeadEjeN);
							}
							else
							{
								ArrayIDUsuario.add(ID_EjecutivoM);
								ArrayCantLeads.add(1);
							}
						}												
					}	
				}
				catch (SQLException e)
				{
				
					throw new DBException(e, sqlCLines.toString());
				}
				finally
				{
					DB.close(rsCLines, pstmtCLines);
					rsCLines = null; pstmtCLines = null;
				}			
		return true;
	}
	public boolean updateExpiredLead(int ID_Campaign) throws SQLException
	{
		String sqlUpdate = "UPDATE C_CampaignFollow SET finalstatus = 'IN' " +
				" WHERE C_CampaignFollow_ID IN (" +
				" SELECT ccf.C_campaignFollow_ID FROM C_CampaignFollow ccf" +
				" INNER JOIN C_Campaign cc ON (ccf.C_Campaign_ID = cc.C_Campaign_ID)" +
				" WHERE ccf.AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+" AND " +
				" cc.c_campaign_id = " + ID_Campaign +
				" AND TRUNC((sysdate - ccf.updated),0) > COALESCE(cc.timeLimit,365) " +
				" AND ccf.finalstatus NOT IN ('CCV','CSV'))";
		
		DB.executeUpdate(sqlUpdate, get_TrxName());		
		
		return true;
	}
	public boolean updateExpiredLeadAgencia(int ID_Campaign) throws SQLException
	{
		String sqlUpdate = "UPDATE C_CampaignFollow SET finalstatus = 'IN', AD_User_ID = null, Supervisor_ID = null" +
				" WHERE C_CampaignFollow_ID IN (" +
				" SELECT ccf.C_campaignFollow_ID FROM C_CampaignFollow ccf" +
				" INNER JOIN C_Campaign cc ON (ccf.C_Campaign_ID = cc.C_Campaign_ID)" +
				" WHERE ccf.AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+" AND " +
				" cc.c_campaign_id = " + ID_Campaign +
				" AND TRUNC((sysdate - ccf.updated),0) > COALESCE(cc.timeLimit,365) " +
				" AND ccf.finalstatus NOT IN ('CCV','CSV'))";
		
		DB.executeUpdate(sqlUpdate, get_TrxName());		
		
		return true;
	}
	

}	//	OrderOpen

