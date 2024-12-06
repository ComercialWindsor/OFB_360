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
package org.ofb.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	proceso prototipo metlife
 *	
 *  @author ininoles
 *  @version $Id: GenerateFollowCampaign.java,v 1.2 2014/09/12 ininoles$
 */
public class GenerateFollowCampaign extends SvrProcess
{
	//private Properties 		m_ctx;	
	private int p_C_Campaign_ID = 0;
	private int	p_R_InterestArea_ID = 0;	
	private int p_InterestAreaValue_ID = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_C_Campaign_ID=getRecord_ID();
		//m_ctx = Env.getCtx();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("R_InterestArea_ID"))
			{
				p_R_InterestArea_ID = para[i].getParameterAsInt();				
			}
			else if (name.equals("R_InterestAreaValues_ID"))
			{
				p_InterestAreaValue_ID = para[i].getParameterAsInt();	
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
		/*********/
		//DB.executeUpdate("DELETE FROM C_CampaignFollow WHERE C_Campaign_ID ="+p_C_Campaign_ID, get_TrxName());
		/*********/
		
		int ID_R_InteresA = p_R_InterestArea_ID;
		
		X_C_Campaign cam = new X_C_Campaign(getCtx(),p_C_Campaign_ID,get_TrxName());
				
		String sqlBP = "SELECT distinct (C_BPartner_ID) as C_BPartner_ID " +				
				" FROM R_ContactInterest WHERE R_InterestArea_ID = ? " +
				" AND C_BPartner_ID NOT IN (SELECT C_BPartner_ID FROM C_campaignFollow WHERE C_Campaign_ID = "+p_C_Campaign_ID+")";
		
		if (p_InterestAreaValue_ID > 0)
		{
			sqlBP = sqlBP + " AND R_InterestAreaValues_ID = "+p_InterestAreaValue_ID;
		}
		
		String sqlID = "SELECT MAX (C_CampaignFollow_ID) as C_CampaignFollow_ID FROM C_CampaignFollow" ;
		int ID_Follow = DB.getSQLValue(get_TrxName(), sqlID);
		
		String sqlIDInterest = "SELECT MAX(R_CampaignInterest_ID) as R_CampaignInterest_ID FROM R_CampaignInterest" ;
		int ID_Interest = DB.getSQLValue(get_TrxName(), sqlIDInterest);
		
		if (ID_Interest < 1)
			ID_Interest = 1000000;
		else
			ID_Interest	= ID_Interest + 1; 
		
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sqlBP, get_TrxName());
			pstmt.setInt(1, ID_R_InteresA);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{	
				String createdStrCF = ""+cam.getCreated();
				String updatedStrCF = ""+cam.getUpdated();
				
				
				String createdCF ="to_date('"+createdStrCF.substring(0, 19)+ "','YYYY-MM-DD HH24:MI:SS')";
				String updatedCF ="to_date('"+updatedStrCF.substring(0, 19)+ "','YYYY-MM-DD HH24:MI:SS')";
				
				if (ID_Follow < 1)
					ID_Follow=1000000;
				
				ID_Follow = ID_Follow+1;
				
				String InsertD=new String("INSERT INTO C_CampaignFollow(C_CampaignFollow_ID,ad_client_id,ad_org_id,isactive,created,createdby," +
						"updated,updatedby,C_Campaign_ID,C_BPartner_ID) "+
						"VALUES ("+ID_Follow+","+cam.getAD_Client_ID()+","+cam.getAD_Org_ID()+",'Y',"+createdCF+","+
						cam.getCreatedBy()+","+updatedCF+","+cam.getUpdatedBy()+","+p_C_Campaign_ID+","+
						rs.getInt("C_BPartner_ID")+")");
						
				DB.executeUpdate(InsertD, get_TrxName());
			}
			
			ID_Follow = ID_Follow+1;			
			DB.executeUpdate("UPDATE ad_sequence SET currentnext = "+ID_Follow+" where name like '%C_CampaignFollow%'", get_TrxName());
			DB.executeUpdate("UPDATE C_Campaign SET Status = 'DG' where C_Campaign_ID = "+p_C_Campaign_ID, get_TrxName());
			
			rs.close();
			pstmt.close();
			
			//insertamos las areas de interes relacionadas
			String InsertCI = "";
			String createdStr = ""+cam.getCreated();
			String updatedStr = ""+cam.getUpdated();
			
			
			String created ="to_date('"+createdStr.substring(0, 19)+ "','YYYY-MM-DD HH24:MI:SS')";
			String updated ="to_date('"+updatedStr.substring(0, 19)+ "','YYYY-MM-DD HH24:MI:SS')";
			if (p_InterestAreaValue_ID > 0)
			{
				InsertCI = new String("INSERT INTO R_CampaignInterest (r_campaigninterest_id,ad_client_id,ad_org_id,isactive,created,createdby," +
						"updated,updatedby,C_Campaign_ID,R_InterestArea_ID,R_InterestAreaValues_ID) "+
						"VALUES ("+ID_Interest+","+cam.getAD_Client_ID()+","+cam.getAD_Org_ID()+",'Y',"+created+","+
						cam.getCreatedBy()+","+updated+","+cam.getUpdatedBy()+","+p_C_Campaign_ID+","+
						p_R_InterestArea_ID+","+p_InterestAreaValue_ID+")");
			}
			else
			{
				InsertCI = new String("INSERT INTO R_CampaignInterest (r_campaigninterest_id,ad_client_id,ad_org_id,isactive,created,createdby," +
						"updated,updatedby,C_Campaign_ID,R_InterestArea_ID) "+
						"VALUES ("+ID_Interest+","+cam.getAD_Client_ID()+","+cam.getAD_Org_ID()+",'Y',"+created+","+
						cam.getCreatedBy()+","+updated+","+cam.getUpdatedBy()+","+p_C_Campaign_ID+","+
						p_R_InterestArea_ID+")");
			}
			
			DB.executeUpdate(InsertCI, get_TrxName());
			
			//actualizamos secuencia de tabla R_CampaignInterest
			ID_Interest++;
			DB.executeUpdate("UPDATE ad_sequence SET currentnext = "+ID_Interest+" where name like '%R_CampaignInterest%'", get_TrxName());
			
			assignUser();			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}		
		
		return "Detalle de Campaña Generado";
		
		
	}	//	doIt
	
	public void assignUser()
	{
		X_C_Campaign cam = new X_C_Campaign(getCtx(),p_C_Campaign_ID,get_TrxName());
		
		//borrado de ejecutivos ya asignados
		String sqlDeleteUser = "UPDATE C_CampaignFollow SET AD_User_ID = null WHERE C_Campaign_ID = "+cam.get_ID();		
		DB.executeUpdate(sqlDeleteUser, get_TrxName());
		
		//ciclo de seguimiento de campaña
		String sqlCiclo = "SELECT C_CampaignFollow_ID FROM C_CampaignFollow WHERE C_Campaign_ID = ? ";
		
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sqlCiclo, get_TrxName());
			pstmt.setInt(1, cam.get_ID());
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				X_C_CampaignFollow follow = new X_C_CampaignFollow(getCtx(), rs.getInt(1), get_TrxName());
				
				if(follow.getAD_User_ID() > 0)
				{
					;
				}
				else
				{
					//variables de areas de interes y valor
					int contador = 0;
					int InterestArea1_ID = 0; int InterestAreaValue1_ID = 0;
					int InterestArea2_ID = 0; int InterestAreaValue2_ID = 0;
					int InterestArea3_ID = 0; int InterestAreaValue3_ID = 0;
					
					String detalleAreasI = "SELECT ci.R_InterestArea_ID, ci.R_InterestAreaValues_ID"+
							" FROM R_ContactInterest ci "+
							" WHERE C_BPartner_ID = ? AND ci.R_InterestArea_ID IN "+ 
							" (SELECT R_InterestArea_ID FROM R_CampaignInterest WHERE C_Campaign_ID = ? "+
							" UNION "+
							" SELECT R_InterestArea_ID FROM R_InterestArea ci WHERE IsFilterNoOnGoin = 'Y')";
					
					PreparedStatement pstmt2 = null;
					pstmt2 = DB.prepareStatement(detalleAreasI, get_TrxName());
					pstmt2.setInt(1, follow.getC_BPartner_ID());
					pstmt2.setInt(2, cam.get_ID());
					ResultSet rs2 = pstmt2.executeQuery();
					
					while (rs2.next())
					{
						contador++;
						if (contador == 1)
						{
							InterestArea1_ID = rs2.getInt("R_InterestArea_ID");
							InterestAreaValue1_ID = rs2.getInt("R_InterestAreaValues_ID");
						}						
						else if (contador == 2)
						{
							InterestArea2_ID = rs2.getInt("R_InterestArea_ID");
							InterestAreaValue2_ID = rs2.getInt("R_InterestAreaValues_ID");
						}
						else if (contador == 3)
						{
							InterestArea3_ID = rs2.getInt("R_InterestArea_ID");
							InterestAreaValue3_ID = rs2.getInt("R_InterestAreaValues_ID");
						}
					}
					
					//contruimos cadena de IDs
					String InteresAreasIDs = "";
					String InteresAreasValuesIDs = "";
					
					if (InterestArea1_ID > 0 )
					{
						InteresAreasIDs = InteresAreasIDs+InterestArea1_ID;
						if (InterestArea2_ID > 0)
						{
							InteresAreasIDs = InteresAreasIDs+","+InterestArea2_ID;
							if (InterestArea3_ID > 0)
							{
								InteresAreasIDs = InteresAreasIDs+","+InterestArea3_ID;
							}
						}							
					}
					
					if (InterestAreaValue1_ID > 0 )
					{
						InteresAreasValuesIDs = InteresAreasValuesIDs+InterestAreaValue1_ID;
						if (InterestAreaValue2_ID > 0)
						{
							InteresAreasValuesIDs = InteresAreasValuesIDs+","+InterestAreaValue2_ID;
							if (InterestAreaValue3_ID > 0)
							{
								InteresAreasValuesIDs = InteresAreasValuesIDs+","+InterestAreaValue3_ID;
							}
						}							
					}
					//fin construccion de cadenas de IDs
					
					//obtenemos los id y la cantidad de los ejecutivos a asignar
					String sqlEje = "SELECT au.AD_User_ID FROM AD_User au "+
							" INNER JOIN R_UserInterest ui ON (au.AD_User_ID = ui.AD_User_ID)"+
							" WHERE R_InterestArea_ID IN ("+InteresAreasIDs+")";
					
					if (InterestArea2_ID > 0)
					{
						sqlEje = sqlEje +" AND R_InterestAreaRef2_ID IN ("+InteresAreasIDs+") ";
					}
					if (InterestArea3_ID > 0)
					{
						sqlEje = sqlEje +" AND R_InterestAreaRef3_ID IN ("+InteresAreasIDs+") ";
					}
					if (InterestAreaValue1_ID > 0)
					{
						sqlEje = sqlEje + "AND (R_InterestAreaValues_ID IN ("+InteresAreasValuesIDs+") OR R_InterestAreaValues_ID IS NULL)";
					}
					if (InterestAreaValue2_ID > 0)
					{
						sqlEje = sqlEje + "AND (R_InterestAreaValues2_ID IN ("+InteresAreasValuesIDs+") OR R_InterestAreaValues2_ID IS NULL)";
					}
					if (InterestAreaValue3_ID > 0)
					{
						sqlEje = sqlEje + "AND (R_InterestAreaValues3_ID IN ("+InteresAreasValuesIDs+") OR R_InterestAreaValues3_ID IS NULL)";
					}					

					PreparedStatement pstmtEje = null;
					pstmtEje = DB.prepareStatement(sqlEje, get_TrxName());					
					ResultSet rsEje = pstmtEje.executeQuery();
					
					int cantEje = 0; 
					int[] Ejecutivos = new int[100];
					int indice = 0;
					
					while (rsEje.next())
					{
						cantEje++;
						Ejecutivos[indice] = rsEje.getInt("AD_User_ID");
						indice++;
					}
					//fin llenado de arreglo ID Ejecutivos		
					
					//ciclo con todos los leads iguales
					
					String sqlFollow = "SELECT C_Campaignfollow_ID FROM C_CampaignFollow ccf "+
							"INNER JOIN C_Bpartner cbp ON (ccf.C_Bpartner_ID = cbp.C_Bpartner_ID) "+ 
							"WHERE C_Campaign_ID = ? AND "+
							"(SELECT COUNT(1) FROM R_ContactInterest rci WHERE rci.R_interestArea_ID = "+InterestArea1_ID+" AND rci.R_InterestAreaValues_ID = "+InterestAreaValue1_ID+" AND rci.C_Bpartner_ID = cbp.C_Bpartner_ID) > 0 ";
					
					if (InterestArea2_ID > 0 && InterestAreaValue2_ID > 0)
					{
						sqlFollow = sqlFollow + "AND (SELECT COUNT(1) FROM R_ContactInterest rci WHERE rci.R_interestArea_ID = "+InterestArea2_ID+" AND rci.R_InterestAreaValues_ID = "+InterestAreaValue2_ID+" AND rci.C_Bpartner_ID = cbp.C_Bpartner_ID) > 0"; 
					}					
					if (InterestArea3_ID > 0 && InterestAreaValue3_ID > 0)
					{
						sqlFollow = sqlFollow + "AND (SELECT COUNT(1) FROM R_ContactInterest rci WHERE rci.R_interestArea_ID = "+InterestArea3_ID+" AND rci.R_InterestAreaValues_ID = "+InterestAreaValue3_ID+" AND rci.C_Bpartner_ID = cbp.C_Bpartner_ID) > 0"; 
					}
					
					PreparedStatement pstmt3 = null;
					pstmt3 = DB.prepareStatement(sqlFollow, get_TrxName());
					pstmt3.setInt(1, cam.get_ID());
					//pstmt3.setInt(2, InterestArea1_ID);
					//pstmt3.setInt(3, InterestAreaValue1_ID);
					ResultSet rs3 = pstmt3.executeQuery();
					
					int indiceLocal = 0;
					
					while (rs3.next())
					{
						if (indiceLocal >= cantEje)
						{
							indiceLocal = 0;
						}
							
						X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(),rs3.getInt("C_CampaignFollow_ID"),get_TrxName());
						if (Ejecutivos[indiceLocal] > 0)
						{
							cFollow.setAD_User_ID(Ejecutivos[indiceLocal]);
							cFollow.save();
						}
						indiceLocal++;						
					}	
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
}	//	OrderOpen
