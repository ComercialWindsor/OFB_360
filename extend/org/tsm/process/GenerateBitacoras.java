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
 * Contributor(s): Chris Farley - northernbrewer                              *
 *****************************************************************************/
package org.tsm.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.MMovement;
import org.compiere.model.MOrg;
import org.compiere.model.X_HR_Bitacora;
import org.compiere.model.X_HR_BitacoraLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Replenishment Report
 *	
 *  @author Jorg Janke
 *  @version $Id: ReplenishReport.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 *  
 *  Carlos Ruiz globalqss - integrate bug fixing from Chris Farley
 *    [ 1619517 ] Replenish report fails when no records in m_storage
 */
public class GenerateBitacoras extends SvrProcess
{	
	/** Warehouse				*/
	private Timestamp	p_DateTrxFrom;
	private Timestamp	p_DateTrxTo;
	private int	p_Org_ID;
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
			else if (name.equals("DateTrx"))
			{
				p_DateTrxFrom = (Timestamp)para[i].getParameter();
				p_DateTrxTo = (Timestamp)para[i].getParameter_To();
			}
			else if (name.equals("AD_Org_ID"))
			{
				p_Org_ID = para[i].getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		//Ingreso de prebitacoras
		int cantLine = 0;
		int canthead = 0;
		
		//agregamos hojas de ruta
		String sqlHR = "SELECT pm.M_Movement_ID,pm.C_BPartner_ID, pm.AD_Org_ID, pm.TP_Asset_ID as A_Asset_ID, pm.movementdate " +
				" FROM M_Movement pm " +
				" WHERE pm.M_Movement_ID NOT IN (SELECT biLine.M_Movement_ID FROM hr_bitacoraline biLine " +
				" INNER JOIN hr_bitacora bi ON (bi.HR_Bitacora_ID = biLine.HR_Bitacora_ID) " +
				" WHERE DocStatus IN ('CO','DR','IP') AND biLine.M_Movement_ID IS NOT NULL) " +
				" AND pm.AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+
				" AND pm.MovementDate BETWEEN ? AND ? AND pm.DocStatus IN ('CO','CL','DR')";
		if(p_Org_ID > 0)
			sqlHR = sqlHR + " AND pm.AD_Org_ID = "+p_Org_ID;
		sqlHR = sqlHR + " ORDER BY C_BPartner_ID,TP_Asset_ID ";
			
		PreparedStatement pstmtHR = null;
		ResultSet rsHR = null;
		try
		{
			pstmtHR = DB.prepareStatement (sqlHR, get_TrxName());
			pstmtHR.setTimestamp(1, p_DateTrxFrom);
			pstmtHR.setTimestamp(2, p_DateTrxTo);
			rsHR = pstmtHR.executeQuery();		
			int ID_BPartner = 0;
			int ID_Asset = 0;
			while (rsHR.next())
			{
				ID_BPartner = rsHR.getInt("C_BPartner_ID");
				ID_Asset = rsHR.getInt("A_Asset_ID");
				//nuevos campos para logicas 23-09
				MMovement mov = new MMovement(getCtx(), rsHR.getInt("M_Movement_ID"), get_TrxName());
				MOrg org = new MOrg(getCtx(), mov.getAD_Org_ID(), get_TrxName());
				Boolean IsMultyDays = org.get_ValueAsBoolean("IsMultipleDays");
				//generamos bitacora para BP
				if(ID_BPartner > 0)
				{
					//buscamos bitacora abierta existente
					int ID_Bitacora = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(HR_Bitacora_ID),0)" +
							" FROM HR_Bitacora WHERE DocStatus NOT IN ('CO','VO') " +
							" AND C_BPartner_ID = "+ID_BPartner);
					if(ID_Bitacora > 0)//existe bitacora abierta para usuario
					{	
						//generamos la linea de la bitacora
						X_HR_BitacoraLine line = new X_HR_BitacoraLine(getCtx(), 0, get_TrxName());
						line.setHR_Bitacora_ID(ID_Bitacora);
						line.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
						//line.set_CustomColumn("Pre_M_MovementLine_ID",rsHR.getInt("Pre_M_MovementLine_ID"));
						line.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
						line.setDescription("Ingresado Automaticamente");
						Timestamp InicialHR = DB.getSQLValueTS(get_TrxName(), "select min(mml.tp_finalhr) " +
								" from m_movementline mml where mml.m_movement_id = ? ",mov.get_ID());
						//line.set_CustomColumn("DateTrx", rsHR.getTimestamp("MovementDate"));
						line.set_CustomColumn("DateTrx",InicialHR);
						line.save();
						cantLine++;
						//agregar lineas de diferencia de dias si la org lo dice						
						if(IsMultyDays)
						{	
							Timestamp FinalHR = DB.getSQLValueTS(get_TrxName(), "select max(mml.tp_inicialhr) " +
									" from m_movementline mml where mml.m_movement_id = ? ",mov.get_ID());
							Calendar calInicial = Calendar.getInstance();		
							calInicial.setTimeInMillis(InicialHR.getTime());
							Calendar calFinal = Calendar.getInstance();		
							calFinal.setTimeInMillis(FinalHR.getTime());
							int difDays = calFinal.get(Calendar.DAY_OF_YEAR) - calInicial.get(Calendar.DAY_OF_YEAR);
							if(difDays > 0)
							{
								for(int a=0;a < difDays;a++)
								{
									X_HR_BitacoraLine line2 = new X_HR_BitacoraLine(getCtx(), 0, get_TrxName());
									line2.setHR_Bitacora_ID(ID_Bitacora);
									line2.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
									//line.set_CustomColumn("Pre_M_MovementLine_ID",rsHR.getInt("Pre_M_MovementLine_ID"));
									line2.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
									line2.setDescription("Ingresado Automaticamente");
									//aumentamos 1 dia a fecha de inicio para que actualice fecha correcta
									calInicial.add(Calendar.DATE, 1);
									//Timestamp newTS = new Timestamp(calInicial.getTimeInMillis());
									line2.set_CustomColumn("DateTrx", new Timestamp(calInicial.getTimeInMillis()));
									line2.save();
									cantLine++;
								}
							}							
						}
						//ininoles end
					}
					else//no existe bitacora
					{
						//se genera vitacora
						X_HR_Bitacora bitacora = new X_HR_Bitacora(getCtx(), ID_Bitacora, get_TrxName());
						bitacora.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
						bitacora.setDateTrx(p_DateTrxTo);
						bitacora.setC_BPartner_ID(ID_BPartner);						
						bitacora.setDescription("Generado Automaticamente");
						bitacora.setDocStatus("DR");
						bitacora.save();
						canthead++;
						//se genera linea de la bitacora
						X_HR_BitacoraLine line = new X_HR_BitacoraLine(getCtx(), 0, get_TrxName());
						line.setHR_Bitacora_ID(bitacora.get_ID());
						line.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
						//line.set_CustomColumn("Pre_M_MovementLine_ID",rsHR.getInt("Pre_M_MovementLine_ID"));
						line.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
						line.setDescription("Ingresado Automaticamente");
						Timestamp InicialHR = DB.getSQLValueTS(get_TrxName(), "select min(mml.tp_finalhr) " +
								" from m_movementline mml where mml.m_movement_id = ? ",mov.get_ID());
						//line.set_CustomColumn("DateTrx", rsHR.getTimestamp("MovementDate"));
						line.set_CustomColumn("DateTrx", InicialHR);
						line.save();
						cantLine++;
						//agregar lineas de diferencia de dias si la org lo dice						
						if(IsMultyDays)
						{							
							Timestamp FinalHR = DB.getSQLValueTS(get_TrxName(), "select max(mml.tp_inicialhr) " +
									" from m_movementline mml where mml.m_movement_id = ? ",mov.get_ID());
							Calendar calInicial = Calendar.getInstance();		
							calInicial.setTimeInMillis(InicialHR.getTime());
							Calendar calFinal = Calendar.getInstance();		
							calFinal.setTimeInMillis(FinalHR.getTime());
							int difDays = calFinal.get(Calendar.DAY_OF_YEAR) - calInicial.get(Calendar.DAY_OF_YEAR);
							if(difDays > 0)
							{
								for(int a=0;a < difDays;a++)
								{
									X_HR_BitacoraLine line2 = new X_HR_BitacoraLine(getCtx(), 0, get_TrxName());
									line2.setHR_Bitacora_ID(bitacora.get_ID());
									line2.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
									//line.set_CustomColumn("Pre_M_MovementLine_ID",rsHR.getInt("Pre_M_MovementLine_ID"));
									line2.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
									line2.setDescription("Ingresado Automaticamente");
									//aumentamos 1 dia a fecha de inicio para que actualice fecha correcta
									calInicial.add(Calendar.DATE, 1);
									//Timestamp newTS = new Timestamp(calInicial.getTimeInMillis());
									line2.set_CustomColumn("DateTrx", new Timestamp(calInicial.getTimeInMillis()));
									line2.save();
									cantLine++;
								}
							}							
						}
						//ininoles end
					}	
				}
				if(ID_Asset > 0)
				{
					//buscamos bitacora abierta existente
					int ID_Bitacora = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(HR_Bitacora_ID),0)" +
							" FROM HR_Bitacora WHERE DocStatus NOT IN ('CO','VO') " +
							" AND A_Asset_ID = "+ID_Asset);
					if(ID_Bitacora > 0)//existe bitacora abierta para usuario
					{	
						//generamos la linea de la bitacora
						X_HR_BitacoraLine line = new X_HR_BitacoraLine(getCtx(), 0, get_TrxName());
						line.setHR_Bitacora_ID(ID_Bitacora);
						line.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
						//line.set_CustomColumn("Pre_M_MovementLine_ID",rsHR.getInt("Pre_M_MovementLine_ID"));
						line.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
						line.setDescription("Ingresado Automaticamente");
						Timestamp InicialHR = DB.getSQLValueTS(get_TrxName(), "select min(mml.tp_finalhr) " +
								" from m_movementline mml where mml.m_movement_id = ? ",mov.get_ID());
						//line.set_CustomColumn("DateTrx", rsHR.getTimestamp("MovementDate"));
						line.set_CustomColumn("DateTrx",InicialHR );
						line.save();
						cantLine++;
						//agregar lineas de diferencia de dias si la org lo dice						
						if(IsMultyDays)
						{	
							Timestamp FinalHR = DB.getSQLValueTS(get_TrxName(), "select max(mml.tp_inicialhr) " +
									" from m_movementline mml where mml.m_movement_id = ? ",mov.get_ID());
							Calendar calInicial = Calendar.getInstance();		
							calInicial.setTimeInMillis(InicialHR.getTime());
							Calendar calFinal = Calendar.getInstance();		
							calFinal.setTimeInMillis(FinalHR.getTime());
							int difDays = calFinal.get(Calendar.DAY_OF_YEAR) - calInicial.get(Calendar.DAY_OF_YEAR);
							if(difDays > 0)
							{
								for(int a=0;a < difDays;a++)
								{
									X_HR_BitacoraLine line2 = new X_HR_BitacoraLine(getCtx(), 0, get_TrxName());									
									line2.setHR_Bitacora_ID(ID_Bitacora);
									line2.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
									//line.set_CustomColumn("Pre_M_MovementLine_ID",rsHR.getInt("Pre_M_MovementLine_ID"));
									line2.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
									line2.setDescription("Ingresado Automaticamente");
									//aumentamos 1 dia a fecha de inicio para que actualice fecha correcta
									calInicial.add(Calendar.DATE, 1);
									//Timestamp newTS = new Timestamp(calInicial.getTimeInMillis());
									line2.set_CustomColumn("DateTrx", new Timestamp(calInicial.getTimeInMillis()));
									line2.save();
									cantLine++;
								}
							}							
						}
						//ininoles end
					}
					else//no existe bitacora
					{
						//se genera vitacora
						X_HR_Bitacora bitacora = new X_HR_Bitacora(getCtx(), ID_Bitacora, get_TrxName());
						bitacora.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
						bitacora.setDateTrx(p_DateTrxTo);
						bitacora.setA_Asset_ID(ID_Asset);						
						bitacora.setDescription("Generado Automaticamente");
						bitacora.setDocStatus("DR");
						bitacora.save();
						canthead++;
						//se genera linea de la bitacora
						X_HR_BitacoraLine line = new X_HR_BitacoraLine(getCtx(), 0, get_TrxName());
						line.setHR_Bitacora_ID(bitacora.get_ID());
						line.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
						//line.set_CustomColumn("Pre_M_MovementLine_ID",rsHR.getInt("Pre_M_MovementLine_ID"));
						line.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
						line.setDescription("Ingresado Automaticamente");
						Timestamp InicialHR = DB.getSQLValueTS(get_TrxName(), "select min(mml.tp_finalhr) " +
								" from m_movementline mml where mml.m_movement_id = ? ",mov.get_ID());
						//line.set_CustomColumn("DateTrx", rsHR.getTimestamp("MovementDate"));
						line.set_CustomColumn("DateTrx", InicialHR);
						line.save();
						cantLine++;
						//agregar lineas de diferencia de dias si la org lo dice						
						if(IsMultyDays)
						{							
							Timestamp FinalHR = DB.getSQLValueTS(get_TrxName(), "select max(mml.tp_inicialhr) " +
									" from m_movementline mml where mml.m_movement_id = ? ",mov.get_ID());
							Calendar calInicial = Calendar.getInstance();		
							calInicial.setTimeInMillis(InicialHR.getTime());
							Calendar calFinal = Calendar.getInstance();		
							calFinal.setTimeInMillis(FinalHR.getTime());
							int difDays = calFinal.get(Calendar.DAY_OF_YEAR) - calInicial.get(Calendar.DAY_OF_YEAR);
							if(difDays > 0)
							{
								for(int a=0;a < difDays;a++)
								{
									X_HR_BitacoraLine line2 = new X_HR_BitacoraLine(getCtx(), 0, get_TrxName());									
									line2.setHR_Bitacora_ID(bitacora.get_ID());
									line2.setM_Movement_ID(rsHR.getInt("M_Movement_ID"));
									//line.set_CustomColumn("Pre_M_MovementLine_ID",rsHR.getInt("Pre_M_MovementLine_ID"));
									line2.setAD_Org_ID(rsHR.getInt("AD_Org_ID"));
									line2.setDescription("Ingresado Automaticamente");
									//aumentamos 1 dia a fecha de inicio para que actualice fecha correcta
									calInicial.add(Calendar.DATE, 1);
									//Timestamp newTS = new Timestamp(calInicial.getTimeInMillis());
									line2.set_CustomColumn("DateTrx", new Timestamp(calInicial.getTimeInMillis()));
									line2.save();
									cantLine++;
								}
							}							
						}
						//ininoles end
					}	
				}	
			}	
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage()+" SQL:"+sqlHR, e);
		}
		finally
		{
			rsHR.close ();	pstmtHR.close ();
			pstmtHR = null;	rsHR = null;
		}
		//generacion de bitacoras desde prebitacoras 
		String sqlPB = "SELECT HR_PreBitacora_ID,C_BPartner_ID, AD_Org_ID, A_Asset_ID, DateTrx FROM HR_Prebitacora " +
		" WHERE Processed = 'N' AND AD_Client_ID = " +Env.getAD_Client_ID(getCtx()) +
		" AND (C_BPartner_ID IS NOT NULL OR A_Asset_ID IS NOT NULL )" +
		" AND DateTrx BETWEEN ? AND ? ";
		if(p_Org_ID > 0)
			sqlHR = sqlHR + " AND AD_Org_ID = "+p_Org_ID;
		sqlHR = sqlHR +" ORDER BY C_BPartner_ID,A_Asset_ID ";
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement (sqlPB, get_TrxName());
			pstmt.setTimestamp(1, p_DateTrxFrom);
			pstmt.setTimestamp(2, p_DateTrxTo);
			rs = pstmt.executeQuery ();		
			int ID_BPartner = 0;
			int ID_Asset = 0;			
			String strID_PreBitacora = "0";
			while (rs.next ())
			{
				ID_BPartner = rs.getInt("C_BPartner_ID");
				ID_Asset = rs.getInt("A_Asset_ID");
				//buscamos bitacora abierta existente
				int ID_Bitacora = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(MAX(HR_Bitacora_ID),0)" +
						" FROM HR_Bitacora WHERE DocStatus NOT IN ('CO','VO') AND " +
						" (C_BPartner_ID = "+ID_BPartner+" OR A_Asset_ID = "+ID_Asset+")");
				if(ID_Bitacora > 0)//existe bitacora abierta para usuario
				{	
					//generamos la linea de la bitacora
					X_HR_BitacoraLine line = new X_HR_BitacoraLine(getCtx(), 0, get_TrxName());
					line.setHR_Bitacora_ID(ID_Bitacora);
					line.setHR_Prebitacora_ID(rs.getInt("HR_PreBitacora_ID"));
					line.setAD_Org_ID(rs.getInt("AD_Org_ID"));
					line.setDescription("Ingresado Automaticamente");
					line.set_CustomColumn("DateTrx", rs.getTimestamp("DateTrx"));
					line.save();
					cantLine++;
					//agregamos ID a string para actualizar a posterior
					strID_PreBitacora = strID_PreBitacora+","+rs.getInt("HR_PreBitacora_ID");
				}
				else//no existe bitacora
				{
					//se genera vitacora
					X_HR_Bitacora bitacora = new X_HR_Bitacora(getCtx(), ID_Bitacora, get_TrxName());
					bitacora.setAD_Org_ID(rs.getInt("AD_Org_ID"));
					bitacora.setDateTrx(p_DateTrxTo);
					if(ID_BPartner > 0)
						bitacora.setC_BPartner_ID(ID_BPartner);
					if(ID_Asset > 0)
						bitacora.setA_Asset_ID(ID_Asset);
					bitacora.setDescription("Generado Automaticamente");
					bitacora.setDocStatus("DR");
					bitacora.save();
					canthead++;
					//se genera linea de la bitacora
					X_HR_BitacoraLine line = new X_HR_BitacoraLine(getCtx(), 0, get_TrxName());
					line.setHR_Bitacora_ID(bitacora.get_ID());
					line.setHR_Prebitacora_ID(rs.getInt("HR_PreBitacora_ID"));
					line.setAD_Org_ID(rs.getInt("AD_Org_ID"));
					line.setDescription("Ingresado Automaticamente");
					line.set_CustomColumn("DateTrx", rs.getTimestamp("DateTrx"));
					line.save();
					cantLine++;
					//agregamos ID a string para actualizar a posterior
					strID_PreBitacora = strID_PreBitacora+","+rs.getInt("HR_PreBitacora_ID");
				}				
			}	
			DB.executeUpdate("UPDATE HR_Prebitacora SET Processed = 'Y' WHERE HR_Prebitacora_ID IN ("+strID_PreBitacora+")",get_TrxName());
					
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage()+" SQL:"+sqlPB, e);
		}
		finally
		{
			rs.close ();	pstmt.close ();
			pstmt = null;	rs = null;
		}
		
		return "Se han generado "+canthead+" bitacoras. Se han agregado "+cantLine+" lineas";
	}	//	doIt
}	//	Replenish
