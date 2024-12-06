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

import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class ProcessDMDocument extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private String P_DocAction;
	/*OT ID*/
	private int 			Record_ID;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DocAction"))
				P_DocAction = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		X_DM_Document doc=new X_DM_Document(Env.getCtx(), Record_ID,get_TrxName());
		
		if(P_DocAction.equals("CO") && !doc.isProcessed()){
			
			if(doc.getDM_DocumentType().equals(X_DM_Document.DM_DOCUMENTTYPE_ConvenioMandato))
			{
				if(doc.getDM_MandateAgreement_ID()<=0)
					return "se necesita un Convenio Mandato asignada a este documento";
				if(doc.getDM_RS_ID()<=0)
					return "se necesita una Resolucion de Asignacion  relacionada a este documento";
			}
			
			if(doc.getDM_DocumentType().equals("PM"))//solicitud monto proyecto
			{
				if(doc.getC_Project_ID()==0)
					return "Proyecto es Obligatorio";
				
				String sqlfx = "SELECT SUM(FixAmt) FROM DM_DocumentLine WHERE DM_Document_ID = ?";
			    BigDecimal fxAmt = DB.getSQLValueBD(get_TrxName(), sqlfx, doc.get_ID());			    
			    
			    MProject pj = new  MProject(getCtx(),doc.getC_Project_ID(),get_TrxName() );
			    
				if(fxAmt.signum()==0 )
					return "El monto solicitado no puede ser 0";
				
				if(doc.getDocStatus().equals( X_DM_Document.DOCSTATUS_Drafted))
				{
					//doc.setProcessed(true);
					doc.setDocStatus("WC");
					doc.save();
					
					// chat solicitud en project
					int chat_id = getChat_ID(X_C_Project.Table_ID, doc.getC_Project_ID());
					MChat chat = null;
					if(chat_id>0)
						chat = new  MChat (Env.getCtx(), chat_id, get_TrxName());
					else{
						chat = new MChat (Env.getCtx(), X_C_Project.Table_ID, doc.getC_Project_ID(), 
								"C_Project : "+doc.getC_Project_ID(), get_TrxName());
						chat.saveEx();
					}
					MChatEntry chatentry = new MChatEntry (chat, "Se Solicita Aprobacion Cambio Monto a "+ pj.getCommittedAmt().add(fxAmt).longValue() +" Documento :" + doc.getDocumentNo());
					chatentry.saveEx();
					//******
					return "Solicitado";
				}
				
				String sqlcm = "SELECT SUM(CommittedAmt) FROM DM_DocumentLine WHERE DM_Document_ID = ?";
			    BigDecimal cmAmt = DB.getSQLValueBD(get_TrxName(), sqlcm, doc.get_ID());
				
				if(cmAmt.signum()==0 )
					return "El monto Aprovado no puede ser 0, debe ser mayor o igual al monto original del proyecto";
				
				if(doc.getDocStatus().equals( X_DM_Document.DOCSTATUS_WaitingConfirmation))
				{
					
					doc.setProcessed(true);
					doc.setDocStatus("CO");
					doc.save();
					// chat solicitud en project
					int chat_id = getChat_ID(X_C_Project.Table_ID, doc.getC_Project_ID());
					MChat chat = null;
					if(chat_id>0)
						chat = new  MChat (Env.getCtx(), chat_id, get_TrxName());
					else{
						chat = new MChat (Env.getCtx(), X_C_Project.Table_ID, doc.getC_Project_ID(), 
								"C_Project : "+doc.getC_Project_ID(), get_TrxName());
						chat.saveEx();
					}
					
					
										
					MChatEntry chatentry = new MChatEntry (chat, "Completado Cambio Monto, nuevo monto "+pj.getCommittedAmt().add(cmAmt).longValue()+" Documento :" + doc.getDocumentNo());
					chatentry.saveEx();
					//******
					
					//se agrega distribucion de aumento de fondos por item ININOLES
					PreparedStatement pstmt = null;
					String sqlp = "SELECT * FROM DM_DocumentLine WHERE DM_Document_ID = ?";						
					
					try
					{
						pstmt = DB.prepareStatement(sqlp, get_TrxName());
						pstmt.setInt(1, doc.get_ID());
						
						ResultSet rs = pstmt.executeQuery();						
						
						while (rs.next())
						{
							X_DM_DocumentLine dl = new X_DM_DocumentLine(getCtx(), rs.getInt("DM_DocumentLine_ID"), get_TrxName());
							MProjectLine pl = new MProjectLine(getCtx(), dl.get_ValueAsInt("C_ProjectLine2_ID"), get_TrxName());
							BigDecimal sum = pl.getCommittedAmt().add((BigDecimal)dl.get_Value("CommittedAmt"));
							pl.setCommittedAmt(sum);																					
							pl.save();
							
							
						}							
					}
					catch (Exception e)
					{
						log.log(Level.SEVERE, e.getMessage(), e);
					}
					//fin distribucion por item
					pj.setCommittedAmt(pj.getCommittedAmt().add(cmAmt));
					
					return "Completado";
				}
					
			}
			
			if(doc.getDM_DocumentType().equals("AP"))//asignacion fondos proyecto (no actualiza el asignado)
			{
				if(doc.getC_Project_ID()==0)
					return "Proyecto es Obligatorio";
				if(doc.getAmt().signum()==0 )
					return "El monto asignado no puede ser 0";
				if(doc.get_ValueAsInt("C_Charge_ID")==0)
					return "El cargo es obligatorio";
				
				int lines = DB.getSQLValue(get_TrxName(), "select count(1) from DM_DocumentLine where DM_Document_ID="+doc.getDM_Document_ID());
				if(lines<=0)
					return "El documento no posee lineas";
				
				//validacion de lineas
				PreparedStatement pstmt = null;
				String mysql="SELECT * from DM_DocumentLine where DM_Document_ID= ?";
				try
				{
					pstmt = DB.prepareStatement(mysql, get_TrxName());
					pstmt.setInt(1, doc.getDM_Document_ID());
					
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
						int item=rs.getInt("M_Product_ID");
						int existe = DB.getSQLValue(get_TrxName(), "select count(1) from C_ProjectLine where C_Project_ID="+doc.getC_Project_ID()+" and M_Product_ID="+item);
						if(existe<=0)
						{
							return "el Item :"+MProduct.get(getCtx(), item).getName() +" no pertenece al Proyecto" ;
						}
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
				//validacion de lineas
				
				MProject pj = new  MProject(getCtx(),doc.getC_Project_ID(),get_TrxName() );
				
				if(pj.getPlannedMarginAmt().add(doc.getAmt()).compareTo(pj.getCommittedAmt())>0 )
					return "esta asignación supera el monto que posee el proyecto";
				
			}//fin asignacion de fondos
			
			
			if(doc.getDM_DocumentType().equals("RS"))//Resolucion de asignacion, actualiza el asignado al proyecto
			{
				
				
				if(doc.getAmt().signum()==0 )
					return "El monto asignado no puede ser 0";
				
				int lines = DB.getSQLValue(get_TrxName(), "select count(1) from DM_DocumentLine where DM_Document_ID="+doc.getDM_Document_ID());
				if(lines<=0)
					return "El documento no posee lineas";
				
				PreparedStatement pstmt = null;
				String mysql="SELECT DM_DocumentLine_ID from DM_DocumentLine where DM_Document_ID= ?";
				try
				{
					pstmt = DB.prepareStatement(mysql, get_TrxName());
					pstmt.setInt(1, doc.getDM_Document_ID());
					
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
						X_DM_DocumentLine line = new X_DM_DocumentLine(getCtx(),rs.getInt(1),get_TrxName() );
						
						MProject pj = new  MProject(getCtx(),line.get_ValueAsInt("C_Project_ID"),get_TrxName() );
						pj.setPlannedMarginAmt(pj.getPlannedMarginAmt().add(line.getAmt()));
						pj.save();
						
						//ininoles actualizacion y distribucion de asignacion de montos por linea de proyecto
						PreparedStatement pstmt2 = null;
						String sql2 ="select dm_documentline_id from dm_documentline where dm_document_id = ?";
						
						pstmt2 = DB.prepareStatement(mysql, get_TrxName());
						pstmt2.setInt(1, line.get_ValueAsInt("DM_DocumentRef_ID"));
						
						ResultSet rs2 = pstmt2.executeQuery();
						while (rs2.next())
						{
							X_DM_DocumentLine line2 = new X_DM_DocumentLine(getCtx(),rs2.getInt(1),get_TrxName() );
							MProjectLine pjline = new MProjectLine(getCtx(),line2.get_ValueAsInt("C_ProjectLine_ID"), get_TrxName());
							
							pjline.setPlannedMarginAmt(pjline.getPlannedMarginAmt().add(line2.getAmt()));
							pjline.save();						
						}
						
						//fin distribucion por linea
						
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
				
				
				
			}//fin resolucion de asignacion
			
			if(doc.getDM_DocumentType().equals("AC"))//ininoles Resolucion de cometido , genera diario de cg
			{
				MJournalBatch jb = new MJournalBatch(Env.getCtx(), 0, get_TrxName());
				jb.setPostingType("A");
				jb.setDescription("Contabilizacion resolucion de cometido ID: " + doc.getDM_Document_ID() + " Numero documento: " + doc.getDocumentNo());
				jb.setC_DocType_ID(1000000);
				//jb.setDocStatus("DR");
				//jb.setDocAction("CO");
				jb.setDateDoc(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				jb.setDateAcct(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				jb.setC_Period_ID(MPeriod.getC_Period_ID(Env.getCtx(), new Timestamp(TimeUtil.getToday().getTimeInMillis())));
				jb.setC_Currency_ID(228);												
				jb.save();		
				
				MJournal j = new MJournal(Env.getCtx(), 0, get_TrxName());
				j.setGL_JournalBatch_ID(jb.get_ID());
				j.setPostingType("A");
				j.setDescription("Contabilizacion resolucion de cometido ID: " + doc.getDM_Document_ID() + " Numero documento: " + doc.getDocumentNo());
				j.setC_DocType_ID(1000000);
				//j.setDocStatus("DR");
				//j.setDocAction("CO");
				j.setC_Currency_ID(228);
				j.setC_AcctSchema_ID(1000000);
				j.setGL_Category_ID(1000000);
				j.setDateDoc(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				j.setDateAcct(new Timestamp(TimeUtil.getToday().getTimeInMillis()));
				j.setC_Period_ID(MPeriod.getC_Period_ID(Env.getCtx(), new Timestamp(TimeUtil.getToday().getTimeInMillis())));	
				j.setC_ConversionType_ID(114);
				j.save();
								
				BigDecimal totalamt = new BigDecimal("0.0");
				
				int line = 0;
				
				String sqlcc= "select max(Accountability_Acct) from C_AcctSchema_Default where ad_client_id = 1000000";
				int contracuenta = DB.getSQLValue(get_TrxName(), sqlcc);
				
				PreparedStatement pstmt = null;
				String sql="SELECT dl.RH_Administrativerequests_ID FROM DM_DocumentLine dl "+
						"INNER JOIN RH_Administrativerequests ar on (dl.RH_Administrativerequests_ID = ar.RH_Administrativerequests_ID) "+
						"inner join c_bpartner cbp on (cbp.c_bpartner_ID = ar.c_bpartner_ID) "+
						"WHERE dl.DM_Document_ID = ? order by cbp.name";
				try
				{	
					pstmt = DB.prepareStatement(sql, get_TrxName());
					pstmt.setInt(1, doc.getDM_Document_ID());
					
					ResultSet rs = pstmt.executeQuery();		
					
					line = 10;
										
					while (rs.next()) //ininoles lineas por cada cometido del documento  
					{
						X_RH_AdministrativeRequests ar = new X_RH_AdministrativeRequests(getCtx(),rs.getInt(1) , get_TrxName());
						
						String sqlCP = "select COALESCE (commitmentamt,0) from c_bpartner where c_bpartner_id = ?";
						String sqlSP = "select COALESCE (commitmentamtSP,0) from c_bpartner where c_bpartner_id = ?";
						
						BigDecimal montoCP = DB.getSQLValueBD(get_TrxName(), sqlCP, ar.getC_BPartner_ID());
						BigDecimal montoSP = DB.getSQLValueBD(get_TrxName(), sqlSP, ar.getC_BPartner_ID());
						
						BigDecimal montoTCP = montoCP.multiply((BigDecimal)ar.get_Value("QtyCP"));
						BigDecimal montoTSP = montoSP.multiply((BigDecimal)ar.get_Value("QtySP"));
						
						BigDecimal montoT = montoTCP.add(montoTSP);
																	
						String sqlTC = "SELECT MAX(dm.typecontract) FROM DM_Document dm	where dm.dm_documentType = 'CE' "+ 
										"AND dm.c_bpartner_id = ? ";
						
						String typecontract = DB.getSQLValueString(get_TrxName(), sqlTC, ar.getC_BPartner_ID());
						int combinacionID;						
						if(typecontract == null)
						{
							return "Tipo de Contrato desconocido";
						}
						else if (typecontract.equals("CO"))
						{
							String sqltype = "select max(viaticoc_acct) from C_AcctSchema_Default where ad_client_id = 1000000";
							combinacionID = DB.getSQLValue(get_TrxName(), sqltype);
						}
						else if (typecontract.equals("HO")) //tipo antiguo de honorarios ahora es honorarios institucionales
						{
							String sqltype = "select max(viaticoh_acct) from C_AcctSchema_Default where ad_client_id = 1000000";
							combinacionID = DB.getSQLValue(get_TrxName(), sqltype);
						}else if (typecontract.equals("PL"))
						{
							String sqltype = "select max(viaticop_acct) from C_AcctSchema_Default where ad_client_id = 1000000";
							combinacionID = DB.getSQLValue(get_TrxName(), sqltype);
						}else if (typecontract.equals("HP")) //nuevo tipo de contrato honorarios de programa 
						{
							String sqltype = "select max(viaticohp_acct) from C_AcctSchema_Default where ad_client_id = 1000000";
							combinacionID = DB.getSQLValue(get_TrxName(), sqltype);
						}else //ininoles validacion otros tipos de contratos
						{
							return "Tipo de Contrato desconocido";
						}
						
						
						
						MJournalLine jl = new MJournalLine(j);
						//jl.setGL_Journal_ID(j.get_ID());
						jl.setC_ValidCombination_ID(combinacionID);
						jl.setAmtSourceDr(montoT);
						jl.setAmtSourceCr(new BigDecimal("0.0"));
						jl.setAmtAcct(montoT,new BigDecimal("0.0"));
						jl.setLine(line);
						jl.set_CustomColumn("C_Bpartner_ID", ar.getC_BPartner_ID());
						jl.save();
						
						totalamt = totalamt.add(montoT);
						
						line = line +10;
					}	//end ininoles fin lineas por cometido
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
				
				//ininoles linea contracuenta
				MJournalLine jl = new MJournalLine(j);
				//jl.setGL_Journal_ID(j.get_ID());
				jl.setAmtSourceCr(totalamt);
				jl.setAmtSourceDr(new BigDecimal("0.0"));
				jl.setC_ValidCombination_ID(contracuenta);
				jl.setAmtAcct(new BigDecimal("0.0"), totalamt);
				jl.setLine(line);				
				jl.save();
				doc.set_CustomColumn("Gl_Journal_ID",j.get_ID()); //actualizacion de campo referencial al journal(no al batch)
				
			}// fin Resolucion de cometido
			
			doc.setProcessed(true);
			doc.setDocStatus("CO");
			doc.save();			
			return "Confirmado";
		}
		
		
		
		if(P_DocAction.equals("VO") && doc.isProcessed() && doc.getDocStatus().equals("CO")){
			doc.setProcessed(false);
			doc.setDocStatus("VO");
			doc.save();
			return "Anulado";
		}

			return "No es posible Cumplir la Accion ";
		
	}	//	doIt

	
	private int getChat_ID (int Table_ID, int Record_ID)
	{
		
		int returnValue = DB.getSQLValue(get_TrxName(), "select cm_chat_id from cm_chat where ad_table_id = "+ Table_ID+ " and record_id = "+Record_ID);
		
		return returnValue;
	}

	
}	//	CopyOrder
