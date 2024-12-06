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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.adempiere.exceptions.DBException;
import org.adempiere.model.ImportValidator;
import org.adempiere.process.ImportProcess;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MCampaign;
import org.compiere.model.MLocation;
import org.compiere.model.MUser;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.X_C_Campaign;
import org.compiere.model.X_C_CampaignFollow;
import org.compiere.model.X_I_BPartnerXML;
import org.compiere.model.X_R_ContactInterest;
import org.compiere.model.X_R_InterestArea;
import org.compiere.model.X_R_InterestAreaValues;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	report infoprojectPROVECTIS
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class CopyOfCreateBPMetlifePG extends SvrProcess
implements ImportProcess
{
	
	private int p_PInstance_ID;	
	private int	m_AD_Client_ID = 0;
	private String ID_IBpartner = "0";
	private int ID_Campaign = 0;
	private int ID_EtiquetaUrlGL = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_PInstance_ID = getAD_PInstance_ID();
		m_AD_Client_ID = Env.getAD_Client_ID(getCtx());
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws java.lang.Exception
	{
		if (insertBPartner())
		{	
			if (asignBPartner())
			{
				;
			}
		}

		return "OK";
	}	//	doIt
	
	public Boolean insertBPartner() throws SQLException
	{		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = getWhereClause();
		
		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_BPartnerXML "
				+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),"
						+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
						+ " IsActive = COALESCE (IsActive, 'Y'),"
						+ " Created = COALESCE (Created, SysDate),"
						+ " CreatedBy = COALESCE (CreatedBy, 0),"
						+ " Updated = COALESCE (Updated, SysDate),"
						+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
						+ " I_ErrorMsg = ' ',"
						+ " I_IsImported = 'N' "
						+ "WHERE (I_IsImported<>'Y' OR I_IsImported IS NULL) AND I_IsAsigned<>'Y' ");
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.fine("Reset=" + no);

		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_BEFORE_VALIDATE);
		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_BEFORE_VALIDATE);
		
		//seteos metlife ininoles
		//seteo value null con ID
		sql = new StringBuffer ("UPDATE I_BPartnerXML  "
				+ "SET Value = I_BPartnerXML_ID "
				+ "WHERE Value IS NULL AND I_IsImported<>'Y' AND I_IsAsigned<>'Y'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.config("Set Value Default=" + no);
		
		//seteo nombre
		sql = new StringBuffer ("UPDATE I_BPartnerXML  "
				+ "SET name = COALESCE(IA_Nombre,'') ||' '||COALESCE(IA_Paterno,'')||' '||COALESCE(IA_Materno,'')  "
				+ "WHERE I_IsImported<>'Y' AND I_IsAsigned<>'Y'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.config("Set Name Default=" + no);

		
		//seteos metlie end
		
		//	Set BP_Group
		sql = new StringBuffer ("UPDATE I_BPartnerXML set C_BP_Group_ID = " +
				" (SELECT MIN(C_BP_Group_ID) FROM C_BP_Group WHERE IsDefault = 'Y' AND AD_Client_ID = 1000000)" +
				" WHERE I_IsImported<>'Y' AND I_IsAsigned<>'Y' ").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.fine("Set Group Default=" + no);
		
		// Value is mandatory error
		sql = new StringBuffer ("UPDATE I_BPartnerXML "
				+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||'ERR=Value is mandatory, ' "
				+ "WHERE Value IS NULL "
				+ " AND I_IsImported<>'Y' AND I_IsAsigned<>'Y'").append(clientCheck);
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		log.config("Value is mandatory=" + no);
		
		ModelValidationEngine.get().fireImportValidate(this, null, null, ImportValidator.TIMING_AFTER_VALIDATE);

		
		commitEx();
		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;

		//	Go through Records
		sql = new StringBuffer ("SELECT * FROM I_BPartnerXML "
				+ "WHERE I_IsImported='N' AND I_IsAsigned='N'").append(clientCheck);
		// gody: 20070113 - Order so the same values are consecutive.
		sql.append(" ORDER BY Value, I_BPartnerXML_ID");
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();

			// Remember Previous BP Value BP is only first one, others are contacts.
			// All contacts share BP location.
			// bp and bpl declarations before loop, we need them for data.
			String Old_BPValue = "" ; 
			MBPartner bp = null;
			MBPartnerLocation bpl = null;

			while (rs.next())
			{	
				// Remember Value - only first occurance of the value is BP				
				String New_BPValue = rs.getString("Value") ;

				X_I_BPartnerXML impBP = new X_I_BPartnerXML (getCtx(), rs, get_TrxName());
				log.fine("I_BPartner_ID=" + impBP.getI_BPartnerXML_ID()
						+ ", C_BPartner_ID=" + impBP.getC_BPartner_ID()
						+ ", C_BPartner_Location_ID=" + impBP.getC_BPartner_Location_ID()
						+ ", AD_User_ID=" + impBP.getAD_User_ID());


				if (!New_BPValue.equals(Old_BPValue)) 
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
							sql = new StringBuffer ("UPDATE I_BPartnerXML i "
									+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||")
							.append("'Cannot Insert BPartner, ' ")
							.append("WHERE I_BPartnerXML_ID=").append(impBP.getI_BPartnerXML_ID());
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
							sql = new StringBuffer ("UPDATE I_BPartnerXML i "
									+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||")
							.append("'Cannot Update BPartner, ' ")
							.append("WHERE I_BPartnerXML_ID=").append(impBP.getI_BPartnerXML_ID());
							DB.executeUpdateEx(sql.toString(), get_TrxName());
							continue;
						}
					}

					//	****	Create/Update BPartner Location	****
					bpl = null;
					if (impBP.getC_BPartner_Location_ID() != 0)		//	Update Location
					{
						bpl = new MBPartnerLocation(getCtx(), impBP.getC_BPartner_Location_ID(), get_TrxName());
						MLocation location = new MLocation(getCtx(), bpl.getC_Location_ID(), get_TrxName());
						location.setC_Country_ID(impBP.getC_Country_ID());
						location.setC_Region_ID(impBP.getC_Region_ID());
						location.setCity(impBP.getCity());
						location.setAddress1(impBP.getAddress1());
						location.setAddress2(impBP.getAddress2());
						location.setPostal(impBP.getPostal());
						location.setPostal_Add(impBP.getPostal_Add());
						if (!location.save())
							log.warning("Location not updated");
						else
							bpl.setC_Location_ID(location.getC_Location_ID());
						if (impBP.getPhone() != null)
							bpl.setPhone(impBP.getPhone());
						if (impBP.getPhone2() != null)
							bpl.setPhone2(impBP.getPhone2());
						if (impBP.getFax() != null)
							bpl.setFax(impBP.getFax());
						ModelValidationEngine.get().fireImportValidate(this, impBP, bpl, ImportValidator.TIMING_AFTER_IMPORT);
						bpl.save();
					}
					else 	//	New Location
						if (impBP.getC_Country_ID() != 0
								&& impBP.getAddress1() != null 
								&& impBP.getCity() != null)
						{
							MLocation location = new MLocation(getCtx(), impBP.getC_Country_ID(), 
									impBP.getC_Region_ID(), impBP.getCity(), get_TrxName());
							location.setAddress1(impBP.getAddress1());
							location.setAddress2(impBP.getAddress2());
							location.setPostal(impBP.getPostal());
							location.setPostal_Add(impBP.getPostal_Add());
							if (location.save())
								log.finest("Insert Location - " + location.getC_Location_ID());
							else
							{
								rollback();
								noInsert--;
								sql = new StringBuffer ("UPDATE I_BPartnerXML i "
										+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||")
								.append("'Cannot Insert Location, ' ")
								.append("WHERE I_BPartnerXML_ID=").append(impBP.getI_BPartnerXML_ID());
								DB.executeUpdateEx(sql.toString(), get_TrxName());
								continue;
							}
							//
							bpl = new MBPartnerLocation (bp);
							bpl.setC_Location_ID(location.getC_Location_ID());
							bpl.setPhone(impBP.getPhone());
							bpl.setPhone2(impBP.getPhone2());
							bpl.setFax(impBP.getFax());
							ModelValidationEngine.get().fireImportValidate(this, impBP, bpl, ImportValidator.TIMING_AFTER_IMPORT);
							if (bpl.save())
							{
								log.finest("Insert BP Location - " + bpl.getC_BPartner_Location_ID());
								impBP.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
							}
							else
							{
								rollback();
								noInsert--;
								sql = new StringBuffer ("UPDATE I_BPartnerXML i "
										+ "SET I_IsImported='E', I_ErrorMsg=I_ErrorMsg||")
								.append("'Cannot Insert BPLocation, ' ")
								.append("WHERE I_BPartnerXML_ID=").append(impBP.getI_BPartnerXML_ID());
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
		commitEx();
		return true;
	}
	
	public String getWhereClause()
	{
		return " AND AD_Client_ID=" + m_AD_Client_ID;
	}
	public String getImportTableName()
	{
		return X_I_BPartnerXML.Table_Name;
	}
	private void setTypeOfBPartner(X_I_BPartnerXML impBP, MBPartner bp){
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
	}	
	
	public Boolean asignBPartner() throws SQLException
	{
		String clientCheck = getWhereClause();
		StringBuffer sql = null;
		//asignacion de campaña		
		sql = new StringBuffer ("SELECT C_Bpartner_ID FROM I_BPartnerXML " +
				"WHERE I_IsImported='Y' AND I_IsAsigned='N'").append(clientCheck);
		sql.append(" GROUP BY C_Bpartner_ID ORDER BY C_Bpartner_ID");
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
				
				int ID_EtiquetaURL = DB.getSQLValue(get_TrxName(), "SELECT MAX(R_InterestArea_ID) FROM R_InterestArea WHERE value like 'UrlOrigen'");
				
				if (ID_EtiquetaURL > 0)
					ID_EtiquetaUrlGL = ID_EtiquetaURL;
				
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
					String valueUrl = rsCadenas.getString("value");
					if (valueIABP.contains(valueUrl))
					{
						MCampaign cam = new MCampaign(getCtx(), rsCadenas.getInt("C_Campaign_ID"), get_TrxName());
						ID_Campaign = cam.get_ID();
						X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), 0, get_TrxName());
						cFollow.setAD_Org_ID(cam.getAD_Org_ID());
						cFollow.setC_Campaign_ID(cam.get_ID());
						cFollow.setIsActive(true);
						cFollow.setC_BPartner_ID(bp.get_ID());
						cFollow.setStatus("NC");
						cFollow.setFinalStatus("IN");
						cFollow.save();
						break;
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
		return true;
	}
	public Boolean asignUser() throws SQLException
	{	
		X_C_Campaign cam = new X_C_Campaign(getCtx(),ID_Campaign,get_TrxName());
		
		//calculo carga de ejecutivos 
		
		String sqlEje = "SELECT DISTINCT (cf.AD_User_ID), (SELECT COUNT(1) FROM C_CampaignFollow " +
				" WHERE C_Campaign_ID = cf.C_Campaign_ID AND AD_User_ID = cf.AD_User_ID) as count FROM C_CampaignFollow cf" +
				" WHERE cf.C_Campaign_ID = ? AND cf.AD_User_ID IS NOT NULL ";
		
		ArrayList<Integer> ArrayIDUsuario = null;
		ArrayList<Integer> ArrayCantLeads = null;
		
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
		//calculo de posibles ejecutivo por campaña
		
		//traer posibles ejecutivos
		String sqlPeje = "SELECT DISTINCT(AD_User_ID) AD_User_ID FROM R_UserInterest "+ 
				" WHERE (R_InterestArea_ID IN (?) OR R_InterestAreaRef2_ID IN (?) OR R_InterestAreaRef3_ID IN (?)) "+
				" AND"+ 
				"(    R_InterestAreaValues_ID IN (SELECT R_InterestAreaValues_ID FROM R_CampaignInterest WHERE R_InterestArea_ID = ? AND C_Campaign_ID = ?)"+ 
				"OR R_InterestAreaValues2_ID IN (SELECT R_InterestAreaValues_ID FROM R_CampaignInterest WHERE R_InterestArea_ID = ? AND C_Campaign_ID = ?) "+
				"OR R_InterestAreaValues3_ID IN (SELECT R_InterestAreaValues_ID FROM R_CampaignInterest WHERE R_InterestArea_ID = ? AND C_Campaign_ID = ?)"+
				")";
		
		ArrayList<Integer> ArrayIDPEjecutivos = null;
		String ID_PEjecutivos = "0";
		
		PreparedStatement pstmtPeje  =  null;
		ResultSet rsPeje  = null;
		
		try
		{		
			pstmtPeje   = DB.prepareStatement(sqlPeje , get_TrxName());
			pstmtPeje .setInt(1,ID_EtiquetaUrlGL);
			pstmtPeje .setInt(2,ID_EtiquetaUrlGL);
			pstmtPeje .setInt(3,ID_EtiquetaUrlGL);
			pstmtPeje .setInt(4,ID_EtiquetaUrlGL);
			pstmtPeje .setInt(5,cam.get_ID());
			pstmtPeje .setInt(6,ID_EtiquetaUrlGL);
			pstmtPeje .setInt(7,cam.get_ID());
			pstmtPeje .setInt(8,ID_EtiquetaUrlGL);
			pstmtPeje .setInt(9,cam.get_ID());
			rsPeje  = pstmtPeje .executeQuery();
				
			while (rsPeje.next())
				
			{
				ArrayIDPEjecutivos.add(rsPeje.getInt(1));
				ID_PEjecutivos = ID_PEjecutivos +","+rsPeje.getInt(1); 
			}
		}
		catch (SQLException e)
		{
		
			throw new DBException(e, sqlPeje.toString());
		}
		finally
		{
			DB.close(rsPeje, pstmtPeje);
			rsPeje = null; pstmtPeje = null;
		}
		
		//ciclo de lineas de campaña
		String sqlCLines = "SELECT C_CampaignFollow_ID FROM C_CampaignFollow WHERE C_Campaign_ID = ? Order By C_CampaignFollow_ID";
		
		PreparedStatement pstmtCLines =  null;
		ResultSet rsCLines = null;
		try
		{
			pstmtCLines = DB.prepareStatement(sqlCLines, get_TrxName());
			pstmtCLines.setInt(1, cam.get_ID());
			rsCLines = pstmtCLines.executeQuery();
			
			while (rsCLines.next())
			{
				X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), rsCLines.getInt("C_CampaignFollow_ID"), get_TrxName());
								
				//variables de areas de interes y valor en la campaña
				int contador = 0;
				int InterestArea1_ID = 0; int InterestAreaValue1_ID = 0;
				int InterestArea2_ID = 0; int InterestAreaValue2_ID = 0;
				int InterestArea3_ID = 0; int InterestAreaValue3_ID = 0;
				
				String detalleAreasI = "SELECT ci.R_InterestArea_ID, ci.R_InterestAreaValues_ID "+
						"FROM R_ContactInterest ci " +
						"WHERE C_BPartner_ID = ? AND ci.R_InterestArea_ID IN " +
						"(SELECT R_InterestArea_ID FROM R_CampaignInterest WHERE C_Campaign_ID = ? AND R_InterestArea_ID <> ? "+
						"UNION "+ 
						"SELECT R_InterestArea_ID FROM R_InterestArea ci WHERE IsFilter = 'Y' AND R_InterestArea_ID <> ?)"; 
				
				PreparedStatement pstmt2 = null;
				pstmt2 = DB.prepareStatement(detalleAreasI, get_TrxName());
				pstmt2.setInt(1, cFollow.getC_BPartner_ID());
				pstmt2.setInt(2, cam.get_ID());
				pstmt2.setInt(3, ID_EtiquetaUrlGL);
				pstmt2.setInt(4, ID_EtiquetaUrlGL);
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
				//fin seteo de valores de variables 
				
				
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
	
	
}	//	OrderOpen

