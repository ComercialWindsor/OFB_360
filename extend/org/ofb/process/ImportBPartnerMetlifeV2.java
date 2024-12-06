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
 * Contributor: Carlos Ruiz - globalqss                                       *
 *****************************************************************************/
package org.ofb.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.adempiere.model.ImportValidator;
import org.adempiere.process.ImportProcess;
import org.compiere.model.MBPartner;
import org.compiere.model.MCampaign;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.X_C_CampaignFollow;
import org.compiere.model.X_I_BPartner;
import org.compiere.model.X_R_ContactInterest;
import org.compiere.model.X_R_InterestArea;
import org.compiere.model.X_R_InterestAreaValues;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Import BPartners from I_BPartner metlife
 *
 * 	@author 	Italo Niñoles
 * 	*/
public class ImportBPartnerMetlifeV2 extends SvrProcess 
implements ImportProcess
{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;
	/** Effective						*/
	private Timestamp		m_DateValue = null;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(para[i].getParameter());			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
		m_AD_Client_ID = Env.getAD_Client_ID(getCtx());
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = getWhereClause();

		//	****	Prepare	****

		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE I_BPartner "
					+ "WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdateEx(sql.toString(), get_TrxName());
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_BPartner "
				+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),"
						+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
						+ " IsActive = COALESCE (IsActive, 'Y'),"
						+ " Created = COALESCE (Created, SysDate),"
						+ " CreatedBy = COALESCE (CreatedBy, 0),"
						+ " Updated = COALESCE (Updated, SysDate),"
						+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
						+ " I_ErrorMsg = ' ',"
						+ " I_IsImported = 'N' "
						+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.fine("Reset=" + no);

		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_BEFORE_VALIDATE);
		
		//seteos metlife ininoles
		//seteo value null con ID
		sql = new StringBuffer ("UPDATE I_BPartner  "
				+ "SET Value = I_BPartner_ID "
				+ "WHERE Value IS NULL AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.config("Set Value Default=" + no);
		
		//seteo nombre
		sql = new StringBuffer ("UPDATE I_BPartner  "
				+ "SET name = COALESCE(IA_Nombre,N'') ||' '||COALESCE(IA_Paterno,N'')||' '||COALESCE(IA_Materno,N'')  "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.config("Set Name Default=" + no);

		//seteo ejecutivo
		sql = new StringBuffer ("UPDATE I_BPartner i "
				+ "SET i.AD_User_ID = (SELECT MAX(AD_User_ID) FROM AD_User au WHERE upper(au.name) = upper(i.nameUser)"
				+ " AND au.AD_Client_ID=i.AD_Client_ID) "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.config("Set Name Default=" + no);
		
		
		//seteos metlie end
		
		//	Set BP_Group
		sql = new StringBuffer ("UPDATE I_BPartner i "
				+ "SET GroupValue=(SELECT MAX(Value) FROM C_BP_Group g WHERE g.IsDefault='Y'"
				+ " AND g.AD_Client_ID=i.AD_Client_ID) ");
		sql.append("WHERE GroupValue IS NULL AND C_BP_Group_ID IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.fine("Set Group Default=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner i "
				+ "SET C_BP_Group_ID=(SELECT C_BP_Group_ID FROM C_BP_Group g"
				+ " WHERE i.GroupValue=g.Value AND g.AD_Client_ID=i.AD_Client_ID) "
				+ "WHERE C_BP_Group_ID IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.fine("Set Group=" + no);
		//
		sql = new StringBuffer ("UPDATE I_BPartner "
				+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Invalid Group, ' "
				+ "WHERE C_BP_Group_ID IS NULL"
				+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.config("Invalid Group=" + no);

		//	Existing BPartner ? Match Value
		sql = new StringBuffer ("UPDATE I_BPartner i "
				+ "SET C_BPartner_ID=(SELECT C_BPartner_ID FROM C_BPartner p"
				+ " WHERE i.Value=p.Value AND p.AD_Client_ID=i.AD_Client_ID) "
				+ "WHERE C_BPartner_ID IS NULL AND Value IS NOT NULL"
				+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.fine("Found BPartner=" + no);
		
		//	Interest Area
		sql = new StringBuffer ("UPDATE I_BPartner i " 
				+ "SET R_InterestArea_ID=(SELECT R_InterestArea_ID FROM R_InterestArea ia "
				+ "WHERE i.InterestAreaName=ia.Name AND ia.AD_Client_ID=i.AD_Client_ID) "
				+ "WHERE R_InterestArea_ID IS NULL AND InterestAreaName IS NOT NULL"
				+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.fine("Set Interest Area=" + no);
		
		
		// Value is mandatory error
		sql = new StringBuffer ("UPDATE I_BPartner "
				+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Value is mandatory, ' "
				+ "WHERE Value IS NULL "
				+ " AND I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.config("Value is mandatory=" + no);
		
		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_AFTER_VALIDATE);

		commitEx();
		
		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;

		//	Go through Records
		sql = new StringBuffer ("SELECT * FROM I_BPartner "
				+ "WHERE I_IsImported='N'").append(clientCheck);
		// gody: 20070113 - Order so the same values are consecutive.
		sql.append(" ORDER BY Value, I_BPartner_ID");
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();

			String Old_BPValue = "" ; 
			MBPartner bp = null;

			while (rs.next())
			{	
				// Remember Value - only first occurance of the value is BP
				String New_BPValue = rs.getString("Value") ;

				X_I_BPartner impBP = new X_I_BPartner (getCtx(), rs, get_TrxName());
				log.fine("I_BPartner_ID=" + impBP.getI_BPartner_ID()
						+ ", C_BPartner_ID=" + impBP.getC_BPartner_ID()
						+ ", C_BPartner_Location_ID=" + impBP.getC_BPartner_Location_ID()
						+ ", AD_User_ID=" + impBP.getAD_User_ID());


				if ( ! New_BPValue.equals(Old_BPValue)) 
				{
					//	****	Create/Update BPartner	****
					bp = null;

					if (impBP.getC_BPartner_ID() == 0)	//	Insert new BPartner
					{
						bp = new MBPartner(impBP);
						ModelValidationEngine.get().fireImportValidate(this, impBP, bp, ImportValidator.TIMING_AFTER_IMPORT);
						
						setTypeOfBPartner(impBP,bp);
						
						if (bp.save())
						{
							impBP.setC_BPartner_ID(bp.getC_BPartner_ID());
							log.finest("Insert BPartner - " + bp.getC_BPartner_ID());
							noInsert++;
						}
						else
						{
							sql = new StringBuffer ("UPDATE I_BPartner i "
									+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||")
							.append("'Cannot Insert BPartner, ' ")
							.append("WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
							DB.executeUpdateEx(sql.toString(), get_TrxName());
							continue;
						}
					}
					else				//	Update existing BPartner
					{
						bp = new MBPartner(getCtx(), impBP.getC_BPartner_ID(), get_TrxName());
						//	if (impBP.getValue() != null)			//	not to overwite
						//		bp.setValue(impBP.getValue());
						if (impBP.getName() != null)
						{
							bp.setName(impBP.getName());
							bp.setName2(impBP.getName2());
						}
						if (impBP.getDUNS() != null)
							bp.setDUNS(impBP.getDUNS());
						if (impBP.getTaxID() != null)
							bp.setTaxID(impBP.getTaxID());
						if (impBP.getNAICS() != null)
							bp.setNAICS(impBP.getNAICS());
						if (impBP.getDescription() != null)
							bp.setDescription(impBP.getDescription());
						if (impBP.getC_BP_Group_ID() != 0)
							bp.setC_BP_Group_ID(impBP.getC_BP_Group_ID());
						ModelValidationEngine.get().fireImportValidate(this, impBP, bp, ImportValidator.TIMING_AFTER_IMPORT);
						
						setTypeOfBPartner(impBP,bp);
						
						//
						if (bp.save())
						{
							log.finest("Update BPartner - " + bp.getC_BPartner_ID());
							noUpdate++;
						}
						else
						{
							sql = new StringBuffer ("UPDATE I_BPartner i "
									+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||")
							.append("'Cannot Update BPartner, ' ")
							.append("WHERE I_BPartner_ID=").append(impBP.getI_BPartner_ID());
							DB.executeUpdateEx(sql.toString(), get_TrxName());
							continue;
						}
					}
				}

				Old_BPValue = New_BPValue ;
				
				//interet area metlife
				//Creamos o seteamos area de interes		
				//TelFijo			
				String namesAInterest[] = {"TelFijo","Celular","Direccion","Comuna","Ciudad","Region","Email",
						"MotivoContacto","Origen","Inmueble","AreaFono","TipoPropiedad","ValorPropiedad",
						"MontoCredito","Financ","FechaNac","Genero","EstadoCivil","RazonSocial","RutEmpresa",
						"SolColab","ProdInteres","Corredora","UrlOrigen","Observacion"};				
				for(int i=0;i<namesAInterest.length;i++)
				{
					X_R_InterestArea IArea = null;
					String nameIA = namesAInterest[i];
					String sqlIA1 = "Select R_InterestArea_ID from R_InterestArea WHERE name = '"+nameIA+"'";
					int IA_ID = DB.getSQLValue(get_TrxName(), sqlIA1);
					if (IA_ID > 0 )
					{
						IArea = new X_R_InterestArea(getCtx(), IA_ID, get_TrxName());
					}
					else
					{
						IArea = new X_R_InterestArea(getCtx(), 0, get_TrxName());
						IArea.setName(nameIA);
						IArea.setValue(nameIA);
					}
					IArea.save();
					//seteamos o creamo valor del area de interest
					X_R_InterestAreaValues IAreaValue= null;
					String sqlVIA1 = "SELECT R_InterestAreaValues_ID FROM R_InterestAreaValues " +
							"WHERE R_InterestArea_ID = "+IArea.get_ID()+" AND value = '"+impBP.get_ValueAsString("IA_"+nameIA)+"'";
					int IAV_ID = DB.getSQLValue(get_TrxName(), sqlVIA1);
					if (IAV_ID > 0)
					{
						IAreaValue = new X_R_InterestAreaValues(getCtx(), IAV_ID, get_TrxName());
					}
					else
					{
						if (impBP.get_ValueAsString("IA_"+nameIA) == null || impBP.get_ValueAsString("IA_"+nameIA) == "" || impBP.get_ValueAsString("IA_"+nameIA) == " ")
						{
							;
						}
						else
						{
							IAreaValue = new X_R_InterestAreaValues(getCtx(), 0, get_TrxName());
							IAreaValue.setR_InterestArea_ID(IArea.get_ID());
							IAreaValue.setValue(impBP.get_ValueAsString("IA_"+nameIA));
							IAreaValue.save();
						}
					}

					//creamos la asignacion del area de interes con el socio de negocio
					if (impBP.get_ValueAsString("IA_"+nameIA) == null || impBP.get_ValueAsString("IA_"+nameIA) == "" || impBP.get_ValueAsString("IA_"+nameIA) == " ")
					{
						;
					}else
					{
						if (IArea.get_ValueAsBoolean("IsEditable"))
						{
							X_R_ContactInterest cInterest = null;
							String sqlcInteret = "SELECT MAX(R_ContactInterest_ID) FROM R_ContactInterest WHERE C_BPartner_ID = "+bp.get_ID()+
									"AND R_InterestArea_ID = "+IArea.get_ID();
							int CInterest_ID = DB.getSQLValue(get_TrxName(), sqlcInteret);
							if (CInterest_ID > 0)
							{
								cInterest = new X_R_ContactInterest(getCtx(), CInterest_ID, get_TrxName());
								cInterest.set_CustomColumn("R_InterestAreaValues_ID", IAreaValue.get_ID());						
							}else
							{
								cInterest = new X_R_ContactInterest(getCtx(), 0, get_TrxName());
								cInterest.set_CustomColumn("C_BPartner_ID", bp.get_ID());
								cInterest.setR_InterestArea_ID(IArea.get_ID());
								cInterest.set_CustomColumn("R_InterestAreaValues_ID", IAreaValue.get_ID());											
							}					
							cInterest.save();
						}else
						{
							if (IArea.get_ID() > 0 && IAreaValue.get_ID() > 0)
							{
								X_R_ContactInterest cInterest = new X_R_ContactInterest(getCtx(), 0, get_TrxName());
								cInterest.set_CustomColumn("C_BPartner_ID", bp.get_ID());
								cInterest.setR_InterestArea_ID(IArea.get_ID());
								cInterest.set_CustomColumn("R_InterestAreaValues_ID", IAreaValue.get_ID());
								cInterest.save();						
							}
						}
					}
				}
				//ininoles fin asignacion de etiquetas
				//ininoles etiquetas independientes de campos finales
				if (impBP.get_ValueAsString("IA_TipoEtiqueta") == null || impBP.get_ValueAsString("IA_TipoEtiqueta") == " " 
						|| impBP.get_ValueAsString("IA_TipoEtiqueta")== "")
				{
					;
				}
				else
				{
					X_R_InterestArea IArea2 = null;				
					String sqlIA2 = "Select R_InterestArea_ID from R_InterestArea WHERE name = '"+impBP.get_ValueAsString("IA_TipoEtiqueta")+"'";
					int IA_ID2 = DB.getSQLValue(get_TrxName(), sqlIA2);
					if (IA_ID2 > 0 )
					{
						IArea2 = new X_R_InterestArea(getCtx(), IA_ID2, get_TrxName());
					}
					else
					{
						IArea2 = new X_R_InterestArea(getCtx(), 0, get_TrxName());
						IArea2.setName(impBP.get_ValueAsString("IA_TipoEtiqueta"));
						IArea2.setValue(impBP.get_ValueAsString("IA_TipoEtiqueta"));
					}
					IArea2.save();
					//seteamos o creamos valor del area de interest					
					if (impBP.get_ValueAsString("IA_ValorEtiqueta") == null || impBP.get_ValueAsString("IA_ValorEtiqueta") == "" 
							|| impBP.get_ValueAsString("IA_ValorEtiqueta")== " ")
					{	 
						String sqlcInteret2 = "SELECT MAX(R_ContactInterest_ID) FROM R_ContactInterest WHERE C_BPartner_ID = "+bp.get_ID()+
								"AND R_InterestArea_ID = "+IArea2.get_ID();
						int CInterest_ID2 = DB.getSQLValue(get_TrxName(), sqlcInteret2);
						if (CInterest_ID2 > 0)
						{
							;					
						}else
						{
							X_R_ContactInterest cInterest2 = new X_R_ContactInterest(getCtx(), 0, get_TrxName());
							cInterest2.set_CustomColumn("C_BPartner_ID", bp.get_ID());
							cInterest2.setR_InterestArea_ID(IArea2.get_ID());
							cInterest2.save();
						}												
					}
					else
					{
						X_R_InterestAreaValues IAreaValue2 = null;
						String sqlVIA2 = "SELECT R_InterestAreaValues_ID FROM R_InterestAreaValues " +
								"WHERE R_InterestArea_ID = "+IArea2.get_ID()+" AND value = '"+impBP.get_ValueAsString("IA_ValorEtiqueta")+"'";
						int IAV_ID2 = DB.getSQLValue(get_TrxName(), sqlVIA2);
						if (IAV_ID2 > 0)
						{
							IAreaValue2 = new X_R_InterestAreaValues(getCtx(), IAV_ID2, get_TrxName());							
						}
						else
						{
							IAreaValue2 = new X_R_InterestAreaValues(getCtx(), 0, get_TrxName());
							IAreaValue2.setR_InterestArea_ID(IArea2.get_ID());
							IAreaValue2.setValue(impBP.get_ValueAsString("IA_ValorEtiqueta"));										
						}	
						IAreaValue2.save();
												
						if (IArea2.get_ValueAsBoolean("IsEditable"))
						{
							X_R_ContactInterest cInterest = null;
							String sqlcInteret = "SELECT MAX(R_ContactInterest_ID) FROM R_ContactInterest WHERE C_BPartner_ID = "+bp.get_ID()+
									"AND R_InterestArea_ID = "+IArea2.get_ID();
							int CInterest_ID = DB.getSQLValue(get_TrxName(), sqlcInteret);
							if (CInterest_ID > 0)
							{
								cInterest = new X_R_ContactInterest(getCtx(), CInterest_ID, get_TrxName());
								cInterest.set_CustomColumn("R_InterestAreaValues_ID", IAreaValue2.get_ID());						
							}else
							{
								cInterest = new X_R_ContactInterest(getCtx(), 0, get_TrxName());
								cInterest.set_CustomColumn("C_BPartner_ID", bp.get_ID());
								cInterest.setR_InterestArea_ID(IArea2.get_ID());
								cInterest.set_CustomColumn("R_InterestAreaValues_ID", IAreaValue2.get_ID());											
							}					
							cInterest.save();
						}else
						{
							if (IArea2.get_ID() > 0 && IAreaValue2.get_ID() > 0)
							{
								X_R_ContactInterest cInterest = new X_R_ContactInterest(getCtx(), 0, get_TrxName());
								cInterest.set_CustomColumn("C_BPartner_ID", bp.get_ID());
								cInterest.setR_InterestArea_ID(IArea2.get_ID());
								cInterest.set_CustomColumn("R_InterestAreaValues_ID", IAreaValue2.get_ID());
								cInterest.save();						
							}
						}
					}
				}
				//ininoles end
				
				impBP.setI_IsImported(true);
				impBP.setProcessed(true);
				impBP.setProcessing(false);
				impBP.saveEx();
				commitEx();
				
			}	//	for all I_Product			
			commitEx();			
			asignBPartner();
			DB.close(rs, pstmt);
		}
		catch (SQLException e)
		{
			rollback();
			//log.log(Level.SEVERE, "", e);
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
			//	Set Error to indicator to not imported
			sql = new StringBuffer ("UPDATE I_BPartner "
					+ "SET I_IsImported='N', Updated=SysDate "
					+ "WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdateEx(sql.toString(), get_TrxName());
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@C_BPartner_ID@: @Inserted@");
			addLog (0, null, new BigDecimal (noUpdate), "@C_BPartner_ID@: @Updated@");
		}
		return "";
	}	//	doIt


	//@Override
	public String getWhereClause()
	{
		return " AND AD_Client_ID=" + m_AD_Client_ID;
	}


	//@Override
	public String getImportTableName()
	{
		return X_I_BPartner.Table_Name;
	}
	
	/**
	 * Set type of Business Partner 
	 *
	 * @param X_I_BPartner impBP
	 * @param MBPartner bp
	 */
	private void setTypeOfBPartner(X_I_BPartner impBP, MBPartner bp){
		if (impBP.isVendor()){		
			bp.setIsVendor(true);
			bp.setIsCustomer(false); // It is put to false since by default in C_BPartner is true
		}
		if (impBP.isEmployee()){ 		
			bp.setIsEmployee(true);
			bp.setIsCustomer(false); // It is put to false since by default in C_BPartner is true
		}
		// it has to be the last if, to subscribe the bp.setIsCustomer (false) of the other two
		if (impBP.isCustomer()){		
			bp.setIsCustomer(true);
		}
	}	// setTypeOfBPartner
	
	public Boolean asignBPartner() throws SQLException
	{
		log.config("asignBPartner"); //faaguilar
		String clientCheck = getWhereClause();
		StringBuffer sql = null;
		//asignacion de campaña		
		sql = new StringBuffer ("SELECT C_Bpartner_ID, I_BPartner_ID FROM I_BPartner " +
				"WHERE I_IsImported='Y' AND I_IsAsigned='N' AND C_Bpartner_ID IS NOT NULL ").append(clientCheck);
		sql.append("ORDER BY C_Bpartner_ID");
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{	
				// Remember Value - only first occurance of the value is BP
				MBPartner bp = new MBPartner(getCtx(), rs.getInt("C_Bpartner_ID"), get_TrxName());
				log.config("entra while general BP - BP_ID:" + bp.get_ID()); //faaguilar
				X_I_BPartner ixml = new X_I_BPartner(getCtx(), rs.getInt("I_BPartner_ID"), get_TrxName());
				
				PreparedStatement pstmtEtiqueta =  null;
				ResultSet rsEtiqueta = null;
				//int ID_EtiquetaURL = DB.getSQLValue(get_TrxName(), "SELECT MAX(R_InterestArea_ID) FROM R_InterestArea WHERE value like 'UrlOrigen'");
				String sqlEtiqueta = "SELECT R_InterestArea_ID FROM r_contactinterest WHERE C_BPartner_ID =?";
				pstmtEtiqueta = DB.prepareStatement(sqlEtiqueta, get_TrxName());
				pstmtEtiqueta.setInt(1, bp.get_ID());
				rsEtiqueta = pstmtEtiqueta.executeQuery();
				int existeCampaña = 0;
				while (rsEtiqueta.next() )
				{
					
					int ID_EtiquetaURL = rsEtiqueta.getInt("R_InterestArea_ID");
					
					log.config("entra while InterestArea BP - BP_ID:" + bp.get_ID() + " InterestArea_ID:"+ID_EtiquetaURL); //faaguilar
					
					String sqlvalueIABP = "SELECT VALUE FROM R_ContactInterest CI INNER JOIN R_InterestAreaValues AV ON (CI.R_InterestAreaValues_ID = AV.R_InterestAreaValues_ID)" +
							"WHERE CI.C_BPartner_ID = "+bp.get_ID() +" AND CI.R_InterestArea_ID = "+ID_EtiquetaURL;
					String valueIABP = DB.getSQLValueString(get_TrxName(), sqlvalueIABP);
					
					String sqlCadenasUrl = "SELECT iav.value, cc.C_Campaign_ID FROM C_Campaign cc INNER JOIN R_CampaignInterest ci ON (cc.C_Campaign_ID = ci.C_Campaign_ID) "+
							"INNER JOIN R_InterestAreaValues iav ON(iav.R_InterestAreaValues_ID = ci.R_InterestAreaValues_ID)"+
							"WHERE IsOnGoing = 'Y' AND iav.R_InterestArea_ID=?";
					
					PreparedStatement pstmtCadenas =  null;
					ResultSet rsCadenas = null;
					pstmtCadenas = DB.prepareStatement(sqlCadenasUrl, get_TrxName());
					pstmtCadenas.setInt(1, ID_EtiquetaURL);
					rsCadenas = pstmtCadenas.executeQuery();
					while (rsCadenas.next())
					{	
						log.config("entra while campañas"); //faaguilar
						String valueUrl = rsCadenas.getString("value");
						log.config("valor valueIABP :" + valueIABP); //faaguilar
						log.config("valor valueUrl :" + valueUrl); //faaguilar
						if (valueIABP!= null && valueUrl != null)
						{
							log.config("Pregunta if (valueIABP.contains(valueUrl)):" + (valueIABP.contains(valueUrl)?"true":"false")); //faaguilar
							if (valueIABP.contains(valueUrl))
							{
								existeCampaña = 1;
								MCampaign cam = new MCampaign(getCtx(), rsCadenas.getInt("C_Campaign_ID"), get_TrxName());
								int ID_Campaign = cam.get_ID();
								String countBPStr = "SELECT COUNT(1) FROM C_CampaignFollow WHERE C_BPartner_ID = "+bp.get_ID()+" AND C_Campaign_ID = "+ID_Campaign;
								int countBP = DB.getSQLValue(get_TrxName(), countBPStr);
								log.config("countBP :" + countBP); //faaguilar
								if (countBP > 0)
								{
									ixml.set_CustomColumn("I_IsAsigned",true);
									ixml.save();
								}
								else
								{
									X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), 0, get_TrxName());
									cFollow.setAD_Org_ID(cam.getAD_Org_ID());
									cFollow.setC_Campaign_ID(cam.get_ID());
									cFollow.setIsActive(true);
									cFollow.setC_BPartner_ID(bp.get_ID());
									cFollow.setStatus("NC");
									cFollow.setFinalStatus("IN");
									if (ixml.getAD_User_ID() > 0)
										cFollow.setAD_User_ID(ixml.getAD_User_ID());
									cFollow.save();
									log.config("crea cFollow"); //faaguilar
									ixml.set_CustomColumn("I_IsAsigned",true);
									ixml.save();
									commitEx();
								}
							}
						}	
					}//fin while
					DB.close(rsCadenas, pstmtCadenas);
					rsCadenas = null; pstmtCadenas = null;
				}//fin while
				DB.close(rsEtiqueta, pstmtEtiqueta);
				rsEtiqueta = null; pstmtEtiqueta = null;
				log.config("existeCampaña:" + existeCampaña); //faaguilar
				if (existeCampaña < 1)
				{
					int ID_CampaignDefault = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Campaign_ID) FROM C_Campaign WHERE IsCampaignDefault='Y'");
					log.config("ID_CampaignDefault:" + ID_CampaignDefault); //faaguilar
					if(ID_CampaignDefault > 0)
					{
						MCampaign cam = new MCampaign(getCtx(), ID_CampaignDefault, get_TrxName());
						int ID_Campaign = cam.get_ID();
						String countBPStr = "SELECT COUNT(1) FROM C_CampaignFollow WHERE C_BPartner_ID = "+bp.get_ID()+" AND C_Campaign_ID = "+ID_Campaign;
						
						int countBP = DB.getSQLValue(get_TrxName(), countBPStr);
						log.config("countBP :" + countBP); //faaguilar
						if (countBP > 0)
						{
							ixml.set_CustomColumn("I_IsAsigned",true);
							ixml.save();
						}
						else
						{
							X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), 0, get_TrxName());
							cFollow.setAD_Org_ID(cam.getAD_Org_ID());
							cFollow.setC_Campaign_ID(cam.get_ID());
							cFollow.setIsActive(true);
							cFollow.setC_BPartner_ID(bp.get_ID());
							cFollow.setStatus("NC");
							cFollow.setFinalStatus("IN");							
							cFollow.save();
							log.config("crea cFollow default"); //faaguilar
							ixml.set_CustomColumn("I_IsAsigned",true);
							ixml.save();
							
							String IDEBUserStr = "SELECT MAX(AD_User_ID) FROM AD_User WHERE IsDefaultEB = 'Y'";							
							int ID_EBUser = DB.getSQLValue(get_TrxName(), IDEBUserStr);
							if (ID_EBUser > 0)
							{
								cFollow.setAD_User_ID(ID_EBUser);
								cFollow.save();
							}							
						}
					}
				}
			}
		}
		catch (SQLException e)
		{
			rollback();
			//log.log(Level.SEVERE, "", e);
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		commitEx();
		return true;
	}
	
	
}	//	ImportBPartner
