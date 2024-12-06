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
package org.tsm.process;

import java.sql.*;
import java.util.Calendar;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: ProcessDMPerformanceBondProc.java,v 1.2 2014/12/16 00:51:01  $
 */
public class ProcessCriticalDateProc extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int 			Record_ID;
	
	protected void prepare()
	{	
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{		
		X_C_CriticalDateProcess pro = new X_C_CriticalDateProcess(Env.getCtx(), Record_ID,get_TrxName());

		if(pro.getDocStatus().equals("DR"))
		{
			if (pro.getC_DocType().getDocBaseType().equals("CDA"))
			{		
				pro.setDocStatus("IP");
				if(pro.getAD_Org_Ref_ID() > 0)
				{
					String sqlDetOrg = "SELECT A_Asset_ID FROM A_Asset " +
					"WHERE AD_Org_ID = ? ";
					PreparedStatement pstmtOrg = DB.prepareStatement(sqlDetOrg, get_TrxName());
					pstmtOrg.setInt(1,pro.getAD_Org_Ref_ID());
					ResultSet rsOrg = pstmtOrg.executeQuery();
					while (rsOrg.next())
					{
						MAsset asset = new MAsset(getCtx(), rsOrg.getInt("A_Asset_ID"));
						int cantVal = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) " +
								" FROM C_CriticalDateConceptR WHERE C_CriticalDateConceptRef_ID = " +
								pro.get_ValueAsInt("C_CriticalDateConcept_ID"));
						if (cantVal > 0)//el concepto tiene hijos
						{
							String sqlDet = "SELECT C_CriticalDateConcept_ID " +
								" FROM C_CriticalDateConceptR WHERE C_CriticalDateConceptRef_ID = " +
								pro.get_ValueAsInt("C_CriticalDateConcept_ID") +
								" and C_CriticalDateConcept_ID not in (SELECT C_CriticalDateConcept_ID " +
								" FROM A_AssetCriticalDate WHERE Status IN ('AL','CO') AND A_Asset_ID = "+asset.get_ID()+")";
							PreparedStatement pstmt = DB.prepareStatement(sqlDet, get_TrxName());
							//pstmt.setInt(1,pro.get_ValueAsInt("C_CriticalDateConcept_ID"));
							ResultSet rs = pstmt.executeQuery();
							while (rs.next())
							{
								//creamos concepto
								X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(),rs.getInt("C_CriticalDateConcept_ID"),get_TrxName());
								//generamos la linea del proceso
								X_C_CriticalDateProcessLine line = new X_C_CriticalDateProcessLine(getCtx(), 0, get_TrxName()); 
								line.setC_CriticalDateProcess_ID(pro.get_ID());
								line.setAD_Org_ID(pro.getAD_Org_ID());
								line.setA_Asset_ID(asset.get_ID());
								line.setC_CriticalDateConcept_ID(cpt.get_ID());
								line.setAD_User_ID(pro.getAD_User_ID());
								if(cpt.getDescription() != null)
									line.setDescription(cpt.getDescription());
								//calculamos fechas de vencimiento 
						        Calendar calendario = Calendar.getInstance();
						        calendario.setTimeInMillis(pro.getDateTrx().getTime());
						        //calculamos los años a agregar
						        //no se agregan años
						        /*BigDecimal cYears = cpt.getIsDue();
						        if (cYears == null)
						        	cYears = Env.ZERO;
						        int mont = cYears.multiply(new BigDecimal("12.0")).intValue();
						        calendario.add(Calendar.MONTH, mont);*/
						        line.setDueDate(new Timestamp(calendario.getTimeInMillis()));
						        //calculamos fechas de avisos
						        calendario.add(Calendar.DATE, -30);
						        line.setSecondDateReminder(new Timestamp(calendario.getTimeInMillis()));
						        calendario.add(Calendar.DATE, -30);
						        line.setFirstDateReminder(new Timestamp(calendario.getTimeInMillis()));
						        //agregamos fecha de emision
						        Timestamp dateIssue = (Timestamp)pro.get_Value("DateIssue");
						        line.set_CustomColumn("DateIssue", dateIssue);
								line.save();	
							}
						}
						else //concepto no tiene hijos
						{
							//validamos que no exista registro anterior
							int cantValDet = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) " +
									" FROM A_AssetCriticalDate WHERE C_CriticalDateConcept_ID = " +
									pro.getC_CriticalDateConcept_ID()+ " AND Status IN ('AL','CO')");			
							
							if (cantValDet > 0)
							{
								throw new AdempiereException("Ya existe una fecha critica para este concepto");
							}
							else 
							{			
								//creamos concepto
								X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(),pro.getC_CriticalDateConcept_ID(),get_TrxName());
								//generamos la linea del proceso
								X_C_CriticalDateProcessLine line = new X_C_CriticalDateProcessLine(getCtx(), 0, get_TrxName()); 
								line.setC_CriticalDateProcess_ID(pro.get_ID());
								line.setAD_Org_ID(pro.getAD_Org_ID());
								line.setA_Asset_ID(asset.get_ID());
								line.setC_CriticalDateConcept_ID(cpt.get_ID());
								line.setAD_User_ID(pro.getAD_User_ID());
								if(cpt.getDescription() != null)
									line.setDescription(cpt.getDescription());
								//calculamos fechas de vencimiento 
						        Calendar calendario = Calendar.getInstance();
						        calendario.setTimeInMillis(pro.getDateTrx().getTime());
						        //calculamos los años a agregar
						        //no se agregan años
						        /*
						        BigDecimal cYears = cpt.getIsDue();
						        if (cYears == null)
						        	cYears = Env.ZERO;
						        int mont = cYears.multiply(new BigDecimal("12.0")).intValue();
						        calendario.add(Calendar.MONTH, mont);*/
						        line.setDueDate(new Timestamp(calendario.getTimeInMillis()));
						        //calculamos fechas de avisos
						        calendario.add(Calendar.DATE, -30);
						        line.setSecondDateReminder(new Timestamp(calendario.getTimeInMillis()));
						        calendario.add(Calendar.DATE, -30);
						        line.setFirstDateReminder(new Timestamp(calendario.getTimeInMillis()));
						        //agregamos fecha de emision
						        Timestamp dateIssue = (Timestamp)pro.get_Value("DateIssue");
						        line.set_CustomColumn("DateIssue", dateIssue);
								line.save();			
							}
						}
					}
				}
				else if (pro.getA_Asset_ID() > 0)
				{
					int cantVal = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) " +
							" FROM C_CriticalDateConceptR WHERE C_CriticalDateConceptRef_ID = " +
							pro.get_ValueAsInt("C_CriticalDateConcept_ID"));
					if (cantVal > 0)//el concepto tiene hijos
					{
						String sqlDet = "SELECT C_CriticalDateConcept_ID " +
							" FROM C_CriticalDateConceptR WHERE C_CriticalDateConceptRef_ID = " +
							pro.get_ValueAsInt("C_CriticalDateConcept_ID") +
							" and C_CriticalDateConcept_ID not in (SELECT C_CriticalDateConcept_ID " +
							" FROM A_AssetCriticalDate WHERE Status IN ('AL','CO') AND A_Asset_ID = "+pro.getA_Asset_ID()+")";
						PreparedStatement pstmt = DB.prepareStatement(sqlDet, get_TrxName());
						//pstmt.setInt(1,pro.get_ValueAsInt("C_CriticalDateConcept_ID"));
						ResultSet rs = pstmt.executeQuery();
						while (rs.next())
						{
							//creamos concepto
							X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(),rs.getInt("C_CriticalDateConcept_ID"),get_TrxName());
							//generamos la linea del proceso
							X_C_CriticalDateProcessLine line = new X_C_CriticalDateProcessLine(getCtx(), 0, get_TrxName()); 
							line.setC_CriticalDateProcess_ID(pro.get_ID());
							line.setAD_Org_ID(pro.getAD_Org_ID());
							line.setA_Asset_ID(pro.getA_Asset_ID());
							line.setC_CriticalDateConcept_ID(cpt.get_ID());
							line.setAD_User_ID(pro.getAD_User_ID());
							if(cpt.getDescription() != null)
								line.setDescription(cpt.getDescription());
							//calculamos fechas de vencimiento 
					        Calendar calendario = Calendar.getInstance();
					        calendario.setTimeInMillis(pro.getDateTrx().getTime());
					        //calculamos los años a agregar
					        //no se agregan años
					        /*
					        BigDecimal cYears = cpt.getIsDue();
					        if (cYears == null)
					        	cYears = Env.ZERO;
					        int mont = cYears.multiply(new BigDecimal("12.0")).intValue();
					        calendario.add(Calendar.MONTH, mont);*/
					        line.setDueDate(new Timestamp(calendario.getTimeInMillis()));
					        //calculamos fechas de avisos
					        calendario.add(Calendar.DATE, -30);
					        line.setSecondDateReminder(new Timestamp(calendario.getTimeInMillis()));
					        calendario.add(Calendar.DATE, -30);
					        line.setFirstDateReminder(new Timestamp(calendario.getTimeInMillis()));
					        //agregamos fecha de emision
					        Timestamp dateIssue = (Timestamp)pro.get_Value("DateIssue");
					        line.set_CustomColumn("DateIssue", dateIssue);
							line.save();	
						}
					}
					else //concepto no tiene hijos
					{
						//validamos que no exista registro anterior
						int cantValDet = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) " +
								" FROM A_AssetCriticalDate WHERE C_CriticalDateConcept_ID = " +
								pro.getC_CriticalDateConcept_ID()+ " AND Status IN ('AL','CO') " +
								" AND A_Asset_ID = "+pro.getA_Asset_ID());			
						
						if (cantValDet > 0)
						{
							throw new AdempiereException("Ya existe una fecha critica para este concepto");
						}
						else 
						{	
							//creamos concepto
							X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(),pro.getC_CriticalDateConcept_ID(),get_TrxName());
							//generamos la linea del proceso
							X_C_CriticalDateProcessLine line = new X_C_CriticalDateProcessLine(getCtx(), 0, get_TrxName()); 
							line.setC_CriticalDateProcess_ID(pro.get_ID());
							line.setAD_Org_ID(pro.getAD_Org_ID());
							line.setA_Asset_ID(pro.getA_Asset_ID());
							line.setC_CriticalDateConcept_ID(cpt.get_ID());
							line.setAD_User_ID(pro.getAD_User_ID());
							if(cpt.getDescription() != null)
								line.setDescription(cpt.getDescription());
							//calculamos fechas de vencimiento 
					        Calendar calendario = Calendar.getInstance();
					        calendario.setTimeInMillis(pro.getDateTrx().getTime());
					        //calculamos los años a agregar
					        //no se agregan años
					        /*
					        BigDecimal cYears = cpt.getIsDue();
					        if (cYears == null)
					        	cYears = Env.ZERO;
					        int mont = cYears.multiply(new BigDecimal("12.0")).intValue();
					        calendario.add(Calendar.MONTH, mont);*/
					        line.setDueDate(new Timestamp(calendario.getTimeInMillis()));
					        //calculamos fechas de avisos
					        calendario.add(Calendar.DATE, -30);
					        line.setSecondDateReminder(new Timestamp(calendario.getTimeInMillis()));
					        calendario.add(Calendar.DATE, -30);
					        line.setFirstDateReminder(new Timestamp(calendario.getTimeInMillis()));
					        //	agregamos fecha de emision
					        Timestamp dateIssue = (Timestamp)pro.get_Value("DateIssue");
					        line.set_CustomColumn("DateIssue", dateIssue);
							line.save();	
						}
					}
				}			
			}
			else if(pro.getC_DocType().getDocBaseType().equals("CDB"))
			{
				pro.setDocStatus("IP");
				if(pro.getAD_Org_Ref_ID() > 0)
				{
					String sqlDetOrg = "SELECT C_BPartner_ID FROM C_BPartner " +
					"WHERE AD_Org_ID = ? ";
					PreparedStatement pstmtOrg = DB.prepareStatement(sqlDetOrg, get_TrxName());
					pstmtOrg.setInt(1,pro.getAD_Org_Ref_ID());
					ResultSet rsOrg = pstmtOrg.executeQuery();
					while (rsOrg.next())
					{
						MBPartner bPart = new MBPartner(getCtx(), rsOrg.getInt("C_BPartner_ID"), get_TrxName());
						int cantVal = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) " +
								" FROM C_CriticalDateConceptR WHERE C_CriticalDateConceptRef_ID = " +
								pro.get_ValueAsInt("C_CriticalDateConcept_ID"));
						if (cantVal > 0)//el concepto tiene hijos
						{
							String sqlDet = "SELECT C_CriticalDateConcept_ID " +
							" FROM C_CriticalDateConceptR WHERE C_CriticalDateConceptRef_ID = " +
							pro.get_ValueAsInt("C_CriticalDateConcept_ID") +
							" and C_CriticalDateConcept_ID not in (SELECT C_CriticalDateConcept_ID " +
							" FROM C_BPCriticalDate WHERE Status IN ('AL','CO') AND C_Bpartner_ID = "+bPart.get_ID()+")";
							PreparedStatement pstmt = DB.prepareStatement(sqlDet, get_TrxName());
							//pstmt.setInt(1,pro.get_ValueAsInt("C_CriticalDateConcept_ID"));
							ResultSet rs = pstmt.executeQuery();
							while (rs.next())
							{
								//creamos concepto
								X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(),rs.getInt("C_CriticalDateConcept_ID"),get_TrxName());
								//generamos la linea del proceso
								X_C_CriticalDateProcessLine line = new X_C_CriticalDateProcessLine(getCtx(), 0, get_TrxName()); 
								line.setC_CriticalDateProcess_ID(pro.get_ID());
								line.setAD_Org_ID(pro.getAD_Org_ID());
								line.setC_BPartner_ID(bPart.get_ID());
								line.setC_CriticalDateConcept_ID(cpt.get_ID());
								line.setAD_User_ID(pro.getAD_User_ID());
								if(cpt.getDescription() != null)
									line.setDescription(cpt.getDescription());
								//calculamos fechas de vencimiento 
						        Calendar calendario = Calendar.getInstance();
						        calendario.setTimeInMillis(pro.getDateTrx().getTime());
						        //calculamos los años a agregar
						        //no se agregan años
						        /*
						        BigDecimal cYears = cpt.getIsDue();
						        if (cYears == null)
						        	cYears = Env.ZERO;
						        int mont = cYears.multiply(new BigDecimal("12.0")).intValue();
						        calendario.add(Calendar.MONTH, mont);*/
						        line.setDueDate(new Timestamp(calendario.getTimeInMillis()));
						        //calculamos fechas de avisos
						        calendario.add(Calendar.DATE, -30);
						        line.setSecondDateReminder(new Timestamp(calendario.getTimeInMillis()));
						        calendario.add(Calendar.DATE, -30);
						        line.setFirstDateReminder(new Timestamp(calendario.getTimeInMillis()));
						        //agregamos fecha de emision
						        Timestamp dateIssue = (Timestamp)pro.get_Value("DateIssue");
						        line.set_CustomColumn("DateIssue", dateIssue);
								line.save();	
							}
						}
						else //concepto no tiene hijos
						{
							//validamos que no exista registro anterior
							int cantValDet = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) " +
									" FROM C_BPCriticalDate WHERE C_CriticalDateConcept_ID = " +
									pro.getC_CriticalDateConcept_ID()+ " AND Status IN ('AL','CO')" +
									" AND C_Bpartner_ID = "+bPart.get_ID());			
							
							if (cantValDet > 0)
							{
								throw new AdempiereException("Ya existe una fecha critica para este concepto");
							}
							else
							{
								//creamos concepto
								X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(),pro.getC_CriticalDateConcept_ID(),get_TrxName());
								//generamos la linea del proceso
								X_C_CriticalDateProcessLine line = new X_C_CriticalDateProcessLine(getCtx(), 0, get_TrxName()); 
								line.setC_CriticalDateProcess_ID(pro.get_ID());
								line.setAD_Org_ID(pro.getAD_Org_ID());
								line.setC_BPartner_ID(bPart.get_ID());
								line.setC_CriticalDateConcept_ID(cpt.get_ID());
								line.setAD_User_ID(pro.getAD_User_ID());
								if(cpt.getDescription() != null)
									line.setDescription(cpt.getDescription());
								//calculamos fechas de vencimiento
						        Calendar calendario = Calendar.getInstance();
						        calendario.setTimeInMillis(pro.getDateTrx().getTime());
						        //calculamos los años a agregar
						        //no se agregan años
						        /*
						        BigDecimal cYears = cpt.getIsDue();
						        if (cYears == null)
						        	cYears = Env.ZERO;
						        int mont = cYears.multiply(new BigDecimal("12.0")).intValue();
						        calendario.add(Calendar.MONTH, mont);*/
						        line.setDueDate(new Timestamp(calendario.getTimeInMillis()));
						        //calculamos fechas de avisos
						        calendario.add(Calendar.DATE, -30);
						        line.setSecondDateReminder(new Timestamp(calendario.getTimeInMillis()));
						        calendario.add(Calendar.DATE, -30);
						        line.setFirstDateReminder(new Timestamp(calendario.getTimeInMillis()));
						        //agregamos fecha de emision
						        Timestamp dateIssue = (Timestamp)pro.get_Value("DateIssue");
						        line.set_CustomColumn("DateIssue", dateIssue);
								line.save();						
						
							}
						}
					}
				}
				else if (pro.getC_BPartner_ID() > 0)
				{
					int cantVal = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) " +
							" FROM C_CriticalDateConceptR WHERE C_CriticalDateConceptRef_ID = " +
							pro.get_ValueAsInt("C_CriticalDateConcept_ID"));
					if (cantVal > 0)//el concepto tiene hijos
					{
						String sqlDet = "SELECT C_CriticalDateConcept_ID " +
							" FROM C_CriticalDateConceptR WHERE C_CriticalDateConceptRef_ID = " +
							pro.get_ValueAsInt("C_CriticalDateConcept_ID") +
							" and C_CriticalDateConcept_ID not in (SELECT C_CriticalDateConcept_ID " +
							" FROM C_BPCriticalDate WHERE Status IN ('AL','CO') AND C_Bpartner_ID = "+pro.getC_BPartner_ID()+")";
						PreparedStatement pstmt = DB.prepareStatement(sqlDet, get_TrxName());
						//pstmt.setInt(1,pro.get_ValueAsInt("C_CriticalDateConcept_ID"));
						ResultSet rs = pstmt.executeQuery();
						while (rs.next())
						{
							//creamos concepto
							X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(),rs.getInt("C_CriticalDateConcept_ID"),get_TrxName());
							//generamos la linea del proceso
							X_C_CriticalDateProcessLine line = new X_C_CriticalDateProcessLine(getCtx(), 0, get_TrxName()); 
							line.setC_CriticalDateProcess_ID(pro.get_ID());
							line.setAD_Org_ID(pro.getAD_Org_ID());
							line.setC_BPartner_ID(pro.getC_BPartner_ID());
							line.setC_CriticalDateConcept_ID(cpt.get_ID());
							line.setAD_User_ID(pro.getAD_User_ID());
							if(cpt.getDescription() != null)
								line.setDescription(cpt.getDescription());
							//calculamos fechas de vencimiento 
					        Calendar calendario = Calendar.getInstance();
					        calendario.setTimeInMillis(pro.getDateTrx().getTime());
					        //calculamos los años a agregar
					        //no se agregan años
					        /*
					        BigDecimal cYears = cpt.getIsDue();
					        if (cYears == null)
					        	cYears = Env.ZERO;
					        int mont = cYears.multiply(new BigDecimal("12.0")).intValue();
					        calendario.add(Calendar.MONTH, mont);*/
					        line.setDueDate(new Timestamp(calendario.getTimeInMillis()));
					        //calculamos fechas de avisos
					        calendario.add(Calendar.DATE, -30);
					        line.setSecondDateReminder(new Timestamp(calendario.getTimeInMillis()));
					        calendario.add(Calendar.DATE, -30);
					        line.setFirstDateReminder(new Timestamp(calendario.getTimeInMillis()));
					        //agregamos fecha de emision
					        Timestamp dateIssue = (Timestamp)pro.get_Value("DateIssue");
					        line.set_CustomColumn("DateIssue", dateIssue);
							line.save();	
						}
					}
					else //concepto no tiene hijos
					{
						//validamos que no exista registro anterior
						int cantValDet = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) " +
								" FROM C_BPCriticalDate WHERE C_CriticalDateConcept_ID = " +
								pro.getC_CriticalDateConcept_ID()+ " AND Status IN ('AL','CO')" +
								" AND C_Bpartner_ID = "+pro.getC_BPartner_ID());			
						
						if (cantValDet > 0)
						{
							throw new AdempiereException("Ya existe una fecha critica para este concepto");
						}
						else
						{
							//creamos concepto
							X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(),pro.getC_CriticalDateConcept_ID(),get_TrxName());
							//generamos la linea del proceso
							X_C_CriticalDateProcessLine line = new X_C_CriticalDateProcessLine(getCtx(), 0, get_TrxName()); 
							line.setC_CriticalDateProcess_ID(pro.get_ID());
							line.setAD_Org_ID(pro.getAD_Org_ID());
							line.setC_BPartner_ID(pro.getC_BPartner_ID());
							line.setC_CriticalDateConcept_ID(cpt.get_ID());
							line.setAD_User_ID(pro.getAD_User_ID());
							if(cpt.getDescription() != null)
								line.setDescription(cpt.getDescription());
							//calculamos fechas de vencimiento
					        Calendar calendario = Calendar.getInstance();
					        calendario.setTimeInMillis(pro.getDateTrx().getTime());
					        //calculamos los años a agregar
					        //no se agregan años
					        /*
					        BigDecimal cYears = cpt.getIsDue();
					        if (cYears == null)
					        	cYears = Env.ZERO;
					        int mont = cYears.multiply(new BigDecimal("12.0")).intValue();
					        calendario.add(Calendar.MONTH, mont);*/
					        line.setDueDate(new Timestamp(calendario.getTimeInMillis()));
					        //calculamos fechas de avisos
					        calendario.add(Calendar.DATE, -30);
					        line.setSecondDateReminder(new Timestamp(calendario.getTimeInMillis()));
					        calendario.add(Calendar.DATE, -30);
					        line.setFirstDateReminder(new Timestamp(calendario.getTimeInMillis()));
					        //agregamos fecha de emision
					        Timestamp dateIssue = (Timestamp)pro.get_Value("DateIssue");
					        line.set_CustomColumn("DateIssue", dateIssue);
							line.save();		
						}
					}
				}	
			}
			else if(pro.getC_DocType().getDocBaseType().equals("CDU"))
			{
				if (pro.get_ValueAsInt("A_AssetCriticalDate_ID") > 0)
				{
					X_A_AssetCriticalDate ACDate = new X_A_AssetCriticalDate(getCtx(), pro.get_ValueAsInt("A_AssetCriticalDate_ID"), get_TrxName());
					
					X_C_CriticalDateProcessLine line = new X_C_CriticalDateProcessLine(getCtx(), 0, get_TrxName()); 
					//X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(), ACDate.getC_CriticalDateConcept_ID(), get_TrxName());
					
					line.setC_CriticalDateProcess_ID(pro.get_ID());
					line.setAD_Org_ID(pro.getAD_Org_ID());
					line.set_CustomColumn("A_AssetCriticalDate_ID", ACDate.get_ID());
					line.setA_Asset_ID(ACDate.getA_Asset_ID());
					line.setC_CriticalDateConcept_ID(ACDate.getC_CriticalDateConcept_ID());
					line.setAD_User_ID(ACDate.getAD_User_ID());
					if(ACDate.getDescription() != null)
						line.setDescription(ACDate.getDescription());
					//calculamos fechas de vencimiento
					//se usara fechas de la linea traida
			        /*Calendar calendario = Calendar.getInstance();
			        calendario.setTimeInMillis(pro.getDateTrx().getTime());*/
			        //calculamos los años a agregar
			        //no se agregan años
			        /*
			        BigDecimal cYears = cpt.getIsDue();
			        if (cYears == null)
			        	cYears = Env.ZERO;
			        int mont = cYears.multiply(new BigDecimal("12.0")).intValue();
			        calendario.add(Calendar.MONTH, mont);*/
			        line.setDueDate(ACDate.getDueDate());
			        //calculamos fechas de avisos
			        //calendario.add(Calendar.DATE, -30);
			        line.setSecondDateReminder(ACDate.getSecondDateReminder());
			        //calendario.add(Calendar.DATE, -30);
			        line.setFirstDateReminder(ACDate.getFirstDateReminder());
					line.save();	
					/*
			        line.setDueDate(ACDate.getDueDate());
			        line.setSecondDateReminder(ACDate.getSecondDateReminder());
			        line.setFirstDateReminder(ACDate.getFirstDateReminder());
			        */
			        line.set_CustomColumn("Status", ACDate.get_ValueAsString("Status"));
			        //agregamos fecha de emision
			        Timestamp dateIssue = (Timestamp)ACDate.get_Value("DateIssue");
			        line.set_CustomColumn("DateIssue", dateIssue);
			        if(ACDate.get_ValueAsString("StatusDesc") != null && ACDate.get_ValueAsString("StatusDesc") != "")
			        	line.set_CustomColumn("StatusDesc", ACDate.get_ValueAsString("StatusDesc"));
					line.save();
				}
				else if(pro.get_ValueAsInt("C_BPCriticalDate_ID") > 0)
				{
					X_C_BPCriticalDate BPCDate = new X_C_BPCriticalDate(getCtx(), pro.get_ValueAsInt("C_BPCriticalDate_ID"), get_TrxName());
					//X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(), BPCDate.getC_CriticalDateConcept_ID(), get_TrxName());
					
					X_C_CriticalDateProcessLine line = new X_C_CriticalDateProcessLine(getCtx(), 0, get_TrxName()); 
					line.setC_CriticalDateProcess_ID(pro.get_ID());
					line.setAD_Org_ID(pro.getAD_Org_ID());
					line.set_CustomColumn("C_BPCriticalDate_ID", BPCDate.get_ID());
					line.setC_BPartner_ID(BPCDate.getC_BPartner_ID());
					line.setC_CriticalDateConcept_ID(BPCDate.getC_CriticalDateConcept_ID());
					line.setAD_User_ID(BPCDate.getAD_User_ID());
					if(BPCDate.getDescription() != null)
						line.setDescription(BPCDate.getDescription());
					/*
			        line.setDueDate(BPCDate.getDueDate());
			        line.setSecondDateReminder(BPCDate.getSecondDateReminder());
			        line.setFirstDateReminder(BPCDate.getFirstDateReminder());
			        */
					//calculamos las nuevas fechas
					//calculamos fechas de vencimiento 
					//se usara fechas de la linea traida
					/*
			        Calendar calendario = Calendar.getInstance();
			        calendario.setTimeInMillis(pro.getDateTrx().getTime());*/
			        //calculamos los años a agregar
			        //no se agregan años
			        /*
			        BigDecimal cYears = cpt.getIsDue();
			        if (cYears == null)
			        	cYears = Env.ZERO;
			        int mont = cYears.multiply(new BigDecimal("12.0")).intValue();
			        calendario.add(Calendar.MONTH, mont);*/
			        line.setDueDate(BPCDate.getDueDate());
			        //calculamos fechas de avisos
			        //calendario.add(Calendar.DATE, -30);
			        line.setSecondDateReminder(BPCDate.getSecondDateReminder());
			        //calendario.add(Calendar.DATE, -30);
			        line.setFirstDateReminder(BPCDate.getFirstDateReminder());
					line.save();	
			        line.set_CustomColumn("Status", BPCDate.get_ValueAsString("Status"));
			        //agregamos fecha de emision
			        Timestamp dateIssue = (Timestamp)BPCDate.get_Value("DateIssue");
			        line.set_CustomColumn("DateIssue", dateIssue);
			        if(BPCDate.get_ValueAsString("StatusDesc") != null && BPCDate.get_ValueAsString("StatusDesc") != "")
			        	line.set_CustomColumn("StatusDesc", BPCDate.get_ValueAsString("StatusDesc"));
					line.save();
				}
				pro.setDocStatus("IP");
			}
		
		}
		else if (pro.getDocStatus().equals("IP"))
		{
			if(pro.getC_DocType().getDocBaseType().equals("CDA"))
			{
				String sqlDet = "SELECT C_CriticalDateProcessLine_ID FROM C_CriticalDateProcessLine " +
				"WHERE C_CriticalDateProcess_ID = ? ";
				PreparedStatement pstmt = DB.prepareStatement(sqlDet, get_TrxName());
				pstmt.setInt(1,pro.get_ID());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					X_C_CriticalDateProcessLine cdLine = new X_C_CriticalDateProcessLine(getCtx(), 
							rs.getInt("C_CriticalDateProcessLine_ID"), get_TrxName());
					X_A_AssetCriticalDate assetCD= new X_A_AssetCriticalDate(getCtx(), 0, get_TrxName());
					assetCD.setAD_Org_ID(pro.getAD_Org_ID());
					assetCD.setA_Asset_ID(cdLine.getA_Asset_ID());
					assetCD.setC_CriticalDateConcept_ID(cdLine.getC_CriticalDateConcept_ID());
					assetCD.setDueDate(cdLine.getDueDate());
					assetCD.setFirstDateReminder(cdLine.getFirstDateReminder());
					assetCD.setSecondDateReminder(cdLine.getSecondDateReminder());
					if(cdLine.getAmt() != null)
						assetCD.setAmt(cdLine.getAmt());
					if(cdLine.getAD_User_ID() > 0)
						assetCD.setAD_User_ID(cdLine.getAD_User_ID());
					if(cdLine.getDescription() != null)
						assetCD.setDescription(cdLine.getDescription());
					if(cdLine.get_ValueAsString("Status") != null && cdLine.get_ValueAsString("Status") != ""
						&& cdLine.get_ValueAsString("Status") != " ")
						assetCD.set_CustomColumn("Status", cdLine.get_ValueAsString("Status"));
					assetCD.set_CustomColumn("DateIssue", cdLine.get_Value("DateIssue"));
					if(cdLine.get_ValueAsString("StatusDesc") != null && cdLine.get_ValueAsString("StatusDesc") != "")
						assetCD.set_CustomColumn("StatusDesc", cdLine.get_ValueAsString("StatusDesc"));					
					assetCD.save();
					cdLine.set_CustomColumn("Processed",true);					
					cdLine.save();
				}
				pro.set_CustomColumn("Processed",true);
				pro.setDocStatus("CO");
			}
			else if(pro.getC_DocType().getDocBaseType().equals("CDB"))
			{
				String sqlDet = "SELECT C_CriticalDateProcessLine_ID FROM C_CriticalDateProcessLine " +
				"WHERE C_CriticalDateProcess_ID = ? ";
				PreparedStatement pstmt = DB.prepareStatement(sqlDet, get_TrxName());
				pstmt.setInt(1,pro.get_ID());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					X_C_CriticalDateProcessLine cdLine = new X_C_CriticalDateProcessLine(getCtx(), 
							rs.getInt("C_CriticalDateProcessLine_ID"), get_TrxName());
					X_C_BPCriticalDate bPartCD= new X_C_BPCriticalDate(getCtx(), 0, get_TrxName());
					bPartCD.setAD_Org_ID(pro.getAD_Org_ID());
					bPartCD.setC_BPartner_ID(cdLine.getC_BPartner_ID());
					bPartCD.setC_CriticalDateConcept_ID(cdLine.getC_CriticalDateConcept_ID());
					bPartCD.setDueDate(cdLine.getDueDate());
					bPartCD.setFirstDateReminder(cdLine.getFirstDateReminder());
					bPartCD.setSecondDateReminder(cdLine.getSecondDateReminder());
					if(cdLine.getAmt() != null)
						bPartCD.setAmt(cdLine.getAmt());
					if(cdLine.getAD_User_ID() > 0)
						bPartCD.setAD_User_ID(cdLine.getAD_User_ID());
					if(cdLine.getDescription() != null)
						bPartCD.setDescription(cdLine.getDescription());
					if(cdLine.get_ValueAsString("Status") != null && cdLine.get_ValueAsString("Status") != ""
						&& cdLine.get_ValueAsString("Status") != " ")
						bPartCD.set_CustomColumn("Status", cdLine.get_ValueAsString("Status"));
					bPartCD.set_CustomColumn("DateIssue", cdLine.get_Value("DateIssue"));
					if(cdLine.get_ValueAsString("StatusDesc") != null && cdLine.get_ValueAsString("StatusDesc") != "")
						bPartCD.set_CustomColumn("StatusDesc", cdLine.get_ValueAsString("StatusDesc"));
					bPartCD.save();
					cdLine.set_CustomColumn("Processed",true);
					cdLine.save();
					//se crea registro en incidencias ininoles
					//ahora se hara un ciclo con fecha deemision y de vencimiento
					//No se creara incidencia si es de "estado" baja
					if(cdLine.get_ValueAsString("Status").compareTo("BA") != 0)
					{		
						Timestamp fechaEmision = (Timestamp)cdLine.get_Value("DateIssue");					
						Timestamp fechaVencimiento = cdLine.getDueDate();
						Calendar calCalendario = Calendar.getInstance();
						if(fechaEmision != null && fechaVencimiento != null)
						{	
							X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(),cdLine.getC_CriticalDateConcept_ID(),get_TrxName());
							if(cpt.get_ValueAsBoolean("genPreB"))
							{
								while (fechaEmision.compareTo(fechaVencimiento) <= 0)
								{	
									X_HR_Prebitacora prebitacoraSub = null;
									prebitacoraSub = new X_HR_Prebitacora(Env.getCtx(),0,null);
									MBPartner bpIn = new MBPartner(getCtx(), cdLine.getC_BPartner_ID(), get_TrxName());					
									if(bpIn.get_ValueAsInt("AD_OrgRef_ID") > 0)
										prebitacoraSub.setAD_Org_ID(bpIn.get_ValueAsInt("AD_OrgRef_ID"));
									else
										prebitacoraSub.setAD_Org_ID(bpIn.getAD_Org_ID());
									prebitacoraSub.setColumnType("B");					
									prebitacoraSub.setC_BPartner_ID(bpIn.getC_BPartner_ID());				
									//prebitacoraSub.setWorkshift(prebitacora.getWorkshift());
									prebitacoraSub.setHR_Concept_TSM_ID(1000005);
									prebitacoraSub.setProcessed(false);
									prebitacoraSub.setIsActive(true);		
									//Timestamp dateIssue = fechaEmision;
									prebitacoraSub.setDateTrx(fechaEmision);	
									prebitacoraSub.set_CustomColumn("C_CriticalDateProcessLine_ID",cdLine.get_ID());
									prebitacoraSub.saveEx();
									//se le suma un dia a la fecha de emision
									calCalendario.setTimeInMillis(fechaEmision.getTime());
									calCalendario.add(Calendar.DATE,1);
									fechaEmision = new Timestamp(calCalendario.getTimeInMillis());
								}  
							}
						}
					}
					//ininoles end
				}
				pro.set_CustomColumn("Processed",true);
				pro.setDocStatus("CO");
			}
			else if(pro.getC_DocType().getDocBaseType().equals("CDU"))
			{
				String sqlDet = "SELECT C_CriticalDateProcessLine_ID FROM C_CriticalDateProcessLine " +
				"WHERE C_CriticalDateProcess_ID = ? ";
				PreparedStatement pstmt = DB.prepareStatement(sqlDet, get_TrxName());
				pstmt.setInt(1,pro.get_ID());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					
					X_C_CriticalDateProcessLine cdLine = new X_C_CriticalDateProcessLine(getCtx(), 
							rs.getInt("C_CriticalDateProcessLine_ID"), get_TrxName());
					
					if (cdLine.get_ValueAsInt("A_AssetCriticalDate_ID") > 0)
					{
						X_A_AssetCriticalDate ACDate = new X_A_AssetCriticalDate(getCtx(), pro.get_ValueAsInt("A_AssetCriticalDate_ID"), get_TrxName());
						if (ACDate.get_ValueAsString("Status").compareToIgnoreCase(cdLine.get_ValueAsString("Status")) == 0)
						{
							//generar nueva fecha critica							
							X_A_AssetCriticalDate assetCD= new X_A_AssetCriticalDate(getCtx(), 0, get_TrxName());
							assetCD.setAD_Org_ID(pro.getAD_Org_ID());
							assetCD.setA_Asset_ID(ACDate.getA_Asset_ID());							
							assetCD.setC_CriticalDateConcept_ID(cdLine.getC_CriticalDateConcept_ID());
							assetCD.setDueDate(cdLine.getDueDate());							
							assetCD.setSecondDateReminder(cdLine.getSecondDateReminder());
							assetCD.setFirstDateReminder(cdLine.getFirstDateReminder());
							
							//calculamos fechas de vencimiento
					        /*
							Calendar calendario = Calendar.getInstance();
					        calendario.setTimeInMillis(cdLine.getDueDate().getTime());
					        //calculamos fechas de avisos
					        calendario.add(Calendar.DATE, -30);
					        assetCD.setSecondDateReminder(new Timestamp(calendario.getTimeInMillis()));					        
					        calendario.add(Calendar.DATE, -30);					        
							assetCD.setFirstDateReminder(new Timestamp(calendario.getTimeInMillis()));
							*/
							if(cdLine.getAmt() != null)
								assetCD.setAmt(cdLine.getAmt());
							if(cdLine.getAD_User_ID() > 0)
								assetCD.setAD_User_ID(cdLine.getAD_User_ID());
							if(cdLine.getDescription() != null)
								assetCD.setDescription(cdLine.getDescription());
							assetCD.set_CustomColumn("DateIssue", cdLine.get_Value("DateIssue"));
							if(cdLine.get_ValueAsString("StatusDesc") != null && cdLine.get_ValueAsString("StatusDesc") != "")
								assetCD.set_CustomColumn("StatusDesc", cdLine.get_ValueAsString("StatusDesc"));
							assetCD.save();
							cdLine.set_CustomColumn("Processed",true);
							cdLine.save();
							//se da de baja fecha critica antigua
							ACDate.set_CustomColumn("Status", "BA");
						}
						else
						{
							//solo actualiza estado				
							cdLine.set_CustomColumn("Processed",true);
							cdLine.save();
							ACDate.set_CustomColumn("Status", cdLine.get_ValueAsString("Status"));		
							if(cdLine.get_ValueAsString("StatusDesc") != null && cdLine.get_ValueAsString("StatusDesc") != "")
								ACDate.set_CustomColumn("StatusDesc", cdLine.get_ValueAsString("StatusDesc"));
						}
						ACDate.save();
					}
					else if (cdLine.get_ValueAsInt("C_BPCriticalDate_ID") > 0)
					{
						X_C_BPCriticalDate BPCDate = new X_C_BPCriticalDate(getCtx(), pro.get_ValueAsInt("C_BPCriticalDate_ID"), get_TrxName());
						if (BPCDate.get_ValueAsString("Status").compareToIgnoreCase(cdLine.get_ValueAsString("Status")) == 0)
						{
							//generar nueva fecha critica
							X_C_BPCriticalDate bPartCD= new X_C_BPCriticalDate(getCtx(), 0, get_TrxName());
							bPartCD.setAD_Org_ID(pro.getAD_Org_ID());
							bPartCD.setC_BPartner_ID(BPCDate.getC_BPartner_ID());
							bPartCD.setC_CriticalDateConcept_ID(cdLine.getC_CriticalDateConcept_ID());
							bPartCD.setDueDate(cdLine.getDueDate());
							bPartCD.setSecondDateReminder(cdLine.getSecondDateReminder());
							bPartCD.setFirstDateReminder(cdLine.getFirstDateReminder());
							
							/*Calendar calendario = Calendar.getInstance();
					        calendario.setTimeInMillis(cdLine.getDueDate().getTime());
					        //calculamos fechas de avisos
					        calendario.add(Calendar.DATE, -30);
					        bPartCD.setSecondDateReminder(new Timestamp(calendario.getTimeInMillis()));					        
					        calendario.add(Calendar.DATE, -30);					        
					        bPartCD.setFirstDateReminder(new Timestamp(calendario.getTimeInMillis()));
							*/
							if(cdLine.getAmt() != null)
								bPartCD.setAmt(cdLine.getAmt());
							if(cdLine.getAD_User_ID() > 0)
								bPartCD.setAD_User_ID(cdLine.getAD_User_ID());
							if(cdLine.getDescription() != null)
								bPartCD.setDescription(cdLine.getDescription());
							bPartCD.set_CustomColumn("DateIssue", cdLine.get_Value("DateIssue"));
							if(cdLine.get_ValueAsString("StatusDesc") != null && cdLine.get_ValueAsString("StatusDesc") != "")
								bPartCD.set_CustomColumn("StatusDesc", cdLine.get_ValueAsString("StatusDesc"));
							bPartCD.save();
							cdLine.set_CustomColumn("Processed",true);
							cdLine.save();
							//se da de baja fecha critica antigua
							BPCDate.set_CustomColumn("Status", "BA");
						}
						else
						{
							//solo actualiza estado
							cdLine.set_CustomColumn("Processed",true);
							cdLine.save();
							BPCDate.set_CustomColumn("Status", cdLine.get_ValueAsString("Status"));
							if(cdLine.get_ValueAsString("StatusDesc") != null && cdLine.get_ValueAsString("StatusDesc") != "")
								BPCDate.set_CustomColumn("StatusDesc", cdLine.get_ValueAsString("StatusDesc"));
						}
						BPCDate.save();						
						//se crea registro en incidencias ininoles
						//ahora se hara un ciclo con fecha deemision y de vencimiento
						//solo se generara si status es distinto de baja
						if(cdLine.get_ValueAsString("Status").compareTo("BA") != 0)
						{
							Timestamp fechaEmision = (Timestamp)cdLine.get_Value("DateIssue");					
							Timestamp fechaVencimiento = cdLine.getDueDate();
							Calendar calCalendario = Calendar.getInstance();
							if(fechaEmision != null && fechaVencimiento != null)
							{	
								X_C_CriticalDateConcept cpt = new X_C_CriticalDateConcept(getCtx(),cdLine.getC_CriticalDateConcept_ID(),get_TrxName());
								if(cpt.get_ValueAsBoolean("genPreB"))
								{
									while (fechaEmision.compareTo(fechaVencimiento) <= 0)
									{	
										X_HR_Prebitacora prebitacoraSub = null;
										prebitacoraSub = new X_HR_Prebitacora(Env.getCtx(),0,null);
										MBPartner bpIn = new MBPartner(getCtx(), cdLine.getC_BPartner_ID(), get_TrxName());					
										if(bpIn.get_ValueAsInt("AD_OrgRef_ID") > 0)
											prebitacoraSub.setAD_Org_ID(bpIn.get_ValueAsInt("AD_OrgRef_ID"));
										else
											prebitacoraSub.setAD_Org_ID(bpIn.getAD_Org_ID());
										prebitacoraSub.setColumnType("B");					
										prebitacoraSub.setC_BPartner_ID(bpIn.getC_BPartner_ID());				
										//prebitacoraSub.setWorkshift(prebitacora.getWorkshift());
										prebitacoraSub.setHR_Concept_TSM_ID(1000005);
										prebitacoraSub.setProcessed(false);
										prebitacoraSub.setIsActive(true);		
										//Timestamp dateIssue = fechaEmision;
										prebitacoraSub.setDateTrx(fechaEmision);	
										prebitacoraSub.set_CustomColumn("C_CriticalDateProcessLine_ID",cdLine.get_ID());
										prebitacoraSub.saveEx();
										//se le suma un dia a la fecha de emision
										calCalendario.setTimeInMillis(fechaEmision.getTime());
										calCalendario.add(Calendar.DATE,1);
										fechaEmision = new Timestamp(calCalendario.getTimeInMillis());
									}  
								}
							}
						//ininoles end
						}
					}					
				}				
				pro.set_CustomColumn("Processed",true);
				pro.setDocStatus("CO");
			}
		}
		pro.save();
		return "Procesado";
	}	//	doIt	
}	//	CopyOrder

