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
import java.sql.Timestamp;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	report infoprojectPROVECTIS
 *	
 *  @author ininoles
 *  @version $Id: CosteoRutaTSM.java,v 1.2 2009/04/17 00:51:02 ininoles$
 */
public class CosteoRutaTSM extends SvrProcess
{
	private int	p_ProjectOFB_ID = 0;	
	private Timestamp p_MovementDateFrom = null;
	private Timestamp p_MovementDateTo = null;
	private int	p_C_Period_ID = 0;
	private int p_PInstance_ID;	

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
			else if (name.equals("C_ProjectOFB_ID"))
			{
				p_ProjectOFB_ID = para[i].getParameterAsInt();				
			}
			/*else if (name.equals("MovementDate"))
			{
				p_MovementDateFrom = (Timestamp)para[i].getParameter();
				p_MovementDateTo = (Timestamp)para[i].getParameter_To();
			}*/
			else if (name.equals("C_Period_ID"))
			{
				p_C_Period_ID = para[i].getParameterAsInt();
			}
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_PInstance_ID = getAD_PInstance_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws AdempiereSystemError
	{
		//validacion de periodo cerrado
		MPeriod period = new MPeriod(getCtx(), p_C_Period_ID, get_TrxName());
		String sqlCP = "SELECT periodstatus FROM C_PeriodControl WHERE docbasetype = 'MMM' AND C_Period_ID = ?";
		String periodS = DB.getSQLValueString(get_TrxName(), sqlCP, p_C_Period_ID);
		
		if (periodS.compareTo("C") == 0 )
		{
			return "No se puede costear un periodo cerrado";
		}
		else//se asignan valores de rango de fechas 
		{
			p_MovementDateFrom = period.getStartDate();
			p_MovementDateTo = period.getEndDate();
		}
		
		int contador = 0;
		BigDecimal kmAcuT1 = new BigDecimal("0.0");
		BigDecimal volAcuT1 = new BigDecimal("0.0");
		BigDecimal kmAcuT2 = new BigDecimal("0.0");
		BigDecimal volAcuT2 = new BigDecimal("0.0");
		BigDecimal kmAcu = new BigDecimal("0.0");
		BigDecimal volAcu = new BigDecimal("0.0");
		String sqlPrincipal = "SELECT M_Movement_ID FROM M_Movement WHERE isactive = 'Y'";
		
		if(p_ProjectOFB_ID > 0)
		{
			sqlPrincipal = sqlPrincipal + " AND C_ProjectOFB_ID = " + p_ProjectOFB_ID;
		}
		if (p_MovementDateFrom != null && p_MovementDateTo != null)
		{
			sqlPrincipal = sqlPrincipal + " AND MovementDate between ? AND ?";
		}
		
		sqlPrincipal = sqlPrincipal +  " ORDER BY MovementDate, M_Movement_ID"; 
		
		try
		{
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sqlPrincipal, get_TrxName());
			pstmt.setTimestamp(1, p_MovementDateFrom); 
			pstmt.setTimestamp(2, p_MovementDateTo);						
			
			ResultSet rs = pstmt.executeQuery();
		
			//ciclo de hojas de ruta
				
			while (rs.next())
			{								
				MMovement move = new MMovement(getCtx(), rs.getInt("M_Movement_ID"), get_TrxName());					
				X_C_ProjectOFB pro = new X_C_ProjectOFB(getCtx(), move.get_ValueAsInt("C_ProjectOFB_ID"), get_TrxName());
				String CTC =pro.get_ValueAsString("TP_CostingType");
				
				if ( move.get_ID() == 1005386)
				{
					int aa = move.get_ID();
					aa = 0;
				}
				
				String sqlType = "SELECT TP_Type FROM A_Asset WHERE A_Asset_ID = ?";
				String type = DB.getSQLValueString(get_TrxName(), sqlType, move.get_ValueAsInt("TP_Asset_ID"));
				
				int lineaunica = 0;//se crea variable para nuevo tipo por destino
				
				int IDPuerto = 0;
				
				//sacamos si tiene puerto para usar en costeo por matriz
				if (CTC.equalsIgnoreCase("M"))
				{
					String sqlPBase = "SELECT des.TP_Destination_ID FROM M_Movementline ml LEFT JOIN TP_Destination des ON (ml.TP_Destination_ID = des.TP_Destination_ID) "+
							"WHERE M_Movement_ID = ? AND IsHarbor = 'Y'";
					
					IDPuerto = DB.getSQLValue(get_TrxName(), sqlPBase, move.get_ID());
					
				}
				
				//se setea origen para costeo por matriz costo fijo
				
				int idOrigen = move.get_ValueAsInt("TP_Destination_ID");
				
				MMovementLine[] lines = move.getLines(false);
				for (int i = 0; i < lines.length; i++)
				{
				  MMovementLine line = lines[i];
				  int noViaje = line.get_ValueAsInt("TP_LineNo");
				  //BigDecimal valid = new BigDecimal("99.0");
				  
				  //validacion de No de viaje ininoles
				  if (noViaje == 99 || noViaje == 87 || noViaje == 88 || noViaje == 89 || noViaje == 90)
				  {
					  
				  }
				  else
				  {
					
					BigDecimal kmHR = (BigDecimal)line.get_Value("TP_TotalKM");
					BigDecimal volHR = (BigDecimal)line.get_Value("TP_VolM3");
					BigDecimal costoTotal = new BigDecimal("0.0");			
					if (kmHR == null)
					{
						kmHR = Env.ZERO;
					}
					if (volHR == null)
					{
						volHR = Env.ZERO;
					}
					
					if (type==null)
						type = "1";
					
					if (type.equalsIgnoreCase("1"))
					{
						kmAcuT1 = kmAcuT1.add(kmHR);
						volAcuT1 = volAcuT1.add(volHR);
						kmAcu = kmAcuT1;
						volAcu = volAcuT1;
					}
					else if (type.equalsIgnoreCase("2"))  
					{
						kmAcuT2 = kmAcuT2.add(kmHR);
						volAcuT2 = volAcuT2.add(volHR);
						kmAcu = kmAcuT2;
						volAcu = volAcuT2;
					}
					
					
					if (CTC.equalsIgnoreCase("K"))
					{
						String sqlTP_V = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
							"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'K' AND TP_TypeValue = 'K' "+
							"AND MinQty <= ? AND MaxQty >= ? AND TP_Type = ? AND Isactive = 'Y' ";
						
						int CostingValues_ID = DB.getSQLValue(get_TrxName(), sqlTP_V, pro.get_ID(),kmAcu,kmAcu,type);
						
						if (CostingValues_ID > 0)
						{
							X_TP_CostingValues cvalues = new X_TP_CostingValues(getCtx(), CostingValues_ID, get_TrxName());
							
							BigDecimal kmnew = new BigDecimal("0.0"); 
							try{
								kmnew = (BigDecimal)cvalues.get_Value("MaxType");		
								if (kmnew != null && kmHR.compareTo(kmnew) > 0 && kmnew.compareTo(Env.ZERO) > 0)
									kmHR=kmnew;
							} catch (Exception e)
							{
								log.log(Level.SEVERE, e.getMessage(), e);
							}
							
							costoTotal = kmHR.multiply(cvalues.getAmt());
							
							line.set_CustomColumn("Cost", costoTotal);					
							line.set_CustomColumn("KMAcu", kmAcu);
							line.set_CustomColumn("VolAcu", volAcu);							
							if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
								line.setMovementQty(Env.ONE);
							line.save();
							
						}else
						{
							return "Linea sin rango de valores ingresados: Flota "+pro.getName()+
									" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
						}
					}
					if (CTC.equalsIgnoreCase("V"))
					{		
						String sqlTP_V = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
								"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'V' AND TP_TypeValue = 'V' "+
								"AND MinQty <= ? AND MaxQty >= ? AND TP_Type = ? AND Isactive = 'Y' ";
							
						int CostingValues_ID = DB.getSQLValue(get_TrxName(), sqlTP_V, pro.get_ID(),volAcu,volAcu,type);
							
						if (CostingValues_ID > 0)
						{
							X_TP_CostingValues cvalues = new X_TP_CostingValues(getCtx(), CostingValues_ID, get_TrxName());
								
							boolean ISM3 = false;
							boolean ISDN = false;							
							boolean ISValid = false;							
							int validLine = 0;
							
							ISM3 = cvalues.get_ValueAsBoolean("ExistM3");
							ISDN = cvalues.get_ValueAsBoolean("ExistDN");
							ISValid = cvalues.get_ValueAsBoolean("ValidDocument");
		
							//validacion numero de doc unico
							if (ISValid)
							{
								String sqlValidDM= "SELECT  count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE C_ProjectOFB_ID = ? AND mml.POReference like ? AND M_MovementLine_ID <> ? and mml.cost > 0";
								int cantSQL1 = DB.getSQLValue(get_TrxName(), sqlValidDM,move.get_ValueAsInt("C_ProjectOFB_ID"),line.get_ValueAsString("POReference"),line.get_ID());
								if (cantSQL1 > 0)
									validLine=1;
							}
							//validacion existencia de m3
							if (ISM3)
							{
								String sqlExM3= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE mm.M_Movement_ID = ? AND mml.TP_VolM3 > 0 ";
								int cantSQL2 = DB.getSQLValue(get_TrxName(), sqlExM3,move.get_ID());
								if(cantSQL2 == 0)
									validLine=1;													
							}							
							//validacion existencia de numero de documento
							if (ISDN)
							{
								String sqlExDN= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE mm.M_Movement_ID = ? AND mml.POReference IS NOT NULL ";
								int cantSQL3 = DB.getSQLValue(get_TrxName(), sqlExDN,move.get_ID());
								if(cantSQL3 == 0)
									validLine=1;							
							}
							//end validaciones
							
							if (validLine == 0)
							{			
								
								BigDecimal volnew = new BigDecimal("0.0"); 
								try{
									volnew = (BigDecimal)cvalues.get_Value("MaxType");		
									if (volnew != null && volHR.compareTo(volnew) > 0 && volnew.compareTo(Env.ZERO) > 0)
										volHR=volnew;
								} catch (Exception e)
								{
									log.log(Level.SEVERE, e.getMessage(), e);
								}
								
								costoTotal = volHR.multiply(cvalues.getAmt());
						
								line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
							}
						
						}else
						{
							return "Linea sin rango de valores ingresados: Flota "+pro.getName()+
									" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
						}
					}
					//por volumen peso de salida
					if (CTC.equalsIgnoreCase("M"))
					{	
						boolean ISM3 = false;
						boolean ISDN = false;	
						boolean ISPE = false;							
						boolean ISValid = false;							
						int validLine = 0;
						
						String sqlTP_M = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'M' "+
								"AND MinQty <= ? AND MaxQty >= ? AND TP_Type = ? AND Isactive = 'Y'";
							
						int CostingValues_ID = DB.getSQLValue(get_TrxName(), sqlTP_M, pro.get_ID(),volAcu,volAcu,type);
						
						if (CostingValues_ID > 0)
						{
							X_TP_CostingValues cvalues = new X_TP_CostingValues(getCtx(), CostingValues_ID, get_TrxName());
							
							ISM3 = cvalues.get_ValueAsBoolean("ExistM3");
							ISDN = cvalues.get_ValueAsBoolean("ExistDN");
							ISPE = cvalues.get_ValueAsBoolean("ExistPE");							
							ISValid = cvalues.get_ValueAsBoolean("ValidDocument");
		
							//validacion numero de doc unico
							if (ISValid)
							{
								String sqlValidDM= "SELECT  count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE C_ProjectOFB_ID = ? AND mml.POReference like ? AND M_MovementLine_ID <> ? and mml.cost > 0";
								int cantSQL1 = DB.getSQLValue(get_TrxName(), sqlValidDM,move.get_ValueAsInt("C_ProjectOFB_ID"),line.get_ValueAsString("POReference"),line.get_ID());
								if (cantSQL1 > 0)
									validLine=1;
							}
							//validacion existencia de m3
							if (ISM3)
							{
								String sqlExM3= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE mm.M_Movement_ID = ? AND mml.TP_VolM3 > 0 ";
								int cantSQL2 = DB.getSQLValue(get_TrxName(), sqlExM3,move.get_ID());
								if(cantSQL2 == 0)
									validLine=1;													
							}							
							//validacion existencia de numero de documento
							if (ISDN)
							{
								String sqlExDN= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE mm.M_Movement_ID = ? AND mml.POReference IS NOT NULL ";
								int cantSQL3 = DB.getSQLValue(get_TrxName(), sqlExDN,move.get_ID());
								if(cantSQL3 == 0)
									validLine=1;							
							}
							//validacion existencia de peso de salida
							if (ISPE)
							{
								String sqlExM3= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE mm.M_Movement_ID = ? AND mml.TP_VolIN > 0 ";
								int cantSQL4 = DB.getSQLValue(get_TrxName(), sqlExM3,move.get_ID());
								if(cantSQL4 == 0)
									validLine=1;												
							}								
							//end validaciones
							
							int des_ID = line.get_ValueAsInt("TP_Destination_ID");
							if (des_ID == IDPuerto)
								validLine=1;	
							
												
							if (validLine == 0)//solo costeara las lineas que no son puerto
							{
								String sqlMD = "SELECT MAX(cost) FROM TP_DestinationM WHERE TP_Destination_ID = ? AND TP_DestinationRef_ID = ? AND IsActive = 'Y'";
								BigDecimal costM = DB.getSQLValueBD(get_TrxName(), sqlMD, IDPuerto, des_ID);
								
								BigDecimal VolEntrada = (BigDecimal)line.get_Value("TP_VolIN");
								
								if (costM != null )
								{									 
									costoTotal = VolEntrada.multiply(costM);
								}	
								else
								{
									
									costoTotal = VolEntrada.multiply(cvalues.getAmt());
								}
								
								line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
								
							}
						}else
						{
							return "Linea sin rango de valores ingresados: Flota "+pro.getName()+
								" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
						}
						
					}
					//end por volumen PS
					
					//por matriz monto fijo
					if (CTC.equalsIgnoreCase("F"))
					{	
						boolean ISM3 = false;
						boolean ISDN = false;	
						boolean ISPE = false;							
						boolean ISValid = false;							
						int validLine = 0;
												
						String sqlTP_M = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'F' "+
								"AND MinQty <= ? AND MaxQty >= ? AND TP_Type = ? AND Isactive = 'Y'";
							
						int CostingValues_ID = DB.getSQLValue(get_TrxName(), sqlTP_M, pro.get_ID(),volAcu,volAcu,type);
						
						if (CostingValues_ID > 0)
						{
							X_TP_CostingValues cvalues = new X_TP_CostingValues(getCtx(), CostingValues_ID, get_TrxName());
							
							ISM3 = cvalues.get_ValueAsBoolean("ExistM3");
							ISDN = cvalues.get_ValueAsBoolean("ExistDN");
							ISPE = cvalues.get_ValueAsBoolean("ExistPE");							
							ISValid = cvalues.get_ValueAsBoolean("ValidDocument");
		
							//validacion numero de doc unico
							if (ISValid)
							{
								String sqlValidDM= "SELECT  count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE C_ProjectOFB_ID = ? AND mml.POReference like ? AND M_MovementLine_ID <> ? and mml.cost > 0";
								int cantSQL1 = DB.getSQLValue(get_TrxName(), sqlValidDM,move.get_ValueAsInt("C_ProjectOFB_ID"),line.get_ValueAsString("POReference"),line.get_ID());
								if (cantSQL1 > 0)
									validLine=1;
							}
							//validacion existencia de m3
							if (ISM3)
							{
								String sqlExM3= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE mm.M_Movement_ID = ? AND mml.TP_VolM3 > 0 ";
								int cantSQL2 = DB.getSQLValue(get_TrxName(), sqlExM3,move.get_ID());
								if(cantSQL2 == 0)
									validLine=1;													
							}							
							//validacion existencia de numero de documento
							if (ISDN)
							{
								String sqlExDN= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE mm.M_Movement_ID = ? AND mml.POReference IS NOT NULL ";
								int cantSQL3 = DB.getSQLValue(get_TrxName(), sqlExDN,move.get_ID());
								if(cantSQL3 == 0)
									validLine=1;							
							}
							//validacion existencia de peso de salida
							if (ISPE)
							{
								String sqlExM3= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
										"WHERE mm.M_Movement_ID = ? AND mml.TP_VolIN > 0 ";
								int cantSQL4 = DB.getSQLValue(get_TrxName(), sqlExM3,move.get_ID());
								if(cantSQL4 == 0)
									validLine=1;												
							}								
							//end validaciones
							
							int des_ID = line.get_ValueAsInt("TP_Destination_ID");
														
							/*if (des_ID == move.get_ValueAsInt("TP_Destination_ID"))
								validLine=1;*/	
							
												
							if (validLine == 0 && des_ID > 0 && idOrigen > 0)
							{
								String sqlMD = "SELECT MAX(cost) FROM TP_DestinationM WHERE TP_Destination_ID = ? AND TP_DestinationRef_ID = ? AND IsActive = 'Y'";
								BigDecimal costM = DB.getSQLValueBD(get_TrxName(), sqlMD, idOrigen, des_ID);
								
								if (costM != null)
								{
									line.set_CustomColumn("Cost", costM);
								}else
								{
									/*return "Linea sin rango de valores ingresados: Flota "+pro.getName()+
											" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();*/
									;
								}															
													
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
								
								if (des_ID > 0)
								{
									X_TP_Destination tpDes2 = new X_TP_Destination(getCtx(), des_ID, get_TrxName());
									if (tpDes2.get_ValueAsBoolean("IsSource"))
									{
										idOrigen = des_ID;
									}									
								}								
							}	
						}else
						{
							return "Linea sin rango de valores ingresados: Flota "+pro.getName()+
								" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
						}
						
					}
					//end por matriz costo fijo					
					
					
					if (CTC.equalsIgnoreCase("D"))		
					{
						String TP_TypeValue = "";
						int TP_DestinationRef_ID = 0;
						X_TP_CostingValues cvalues = null;
						boolean ISM3 = false;
						boolean ISDN = false;
						boolean ISValid = false;
						int validLine = 0;
						
						String sqlTP_V = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
								"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' "+
								"AND Isactive = 'Y' AND TP_Destination_ID = ? AND (TP_Type = ?  OR TP_Type Is null)  ";
							
						int CostingValues_ID = DB.getSQLValue(get_TrxName(), sqlTP_V, pro.get_ID(),line.get_ValueAsInt("TP_Destination_ID"),type);
							
						if (CostingValues_ID > 0)
						{
							cvalues = new X_TP_CostingValues(getCtx(), CostingValues_ID, get_TrxName());
							TP_TypeValue = cvalues.getTP_TypeValue(); 
							TP_DestinationRef_ID = cvalues.get_ValueAsInt("TP_Destination_ID");
							ISM3 = cvalues.get_ValueAsBoolean("ExistM3");
							ISDN = cvalues.get_ValueAsBoolean("ExistDN");
							ISValid = cvalues.get_ValueAsBoolean("ValidDocument");							
						}	
						else 									
						{
							String sqlTP_Valid = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
									"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' "+
									"AND Isactive = 'Y' AND TP_Destination_ID > 0 ";
								
							int CostingValuesValid_ID = DB.getSQLValue(get_TrxName(), sqlTP_Valid, pro.get_ID());
								
							if (CostingValuesValid_ID > 0)
							{
								cvalues = new X_TP_CostingValues(getCtx(), CostingValuesValid_ID, get_TrxName());
								ISM3 = cvalues.get_ValueAsBoolean("ExistM3");
								ISDN = cvalues.get_ValueAsBoolean("ExistDN");
								ISValid = cvalues.get_ValueAsBoolean("ValidDocument");
							}
							else 
							{
								return "No se Pueden setear parametros de validacion: Flota "+pro.getName()+
										" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
							}
							
							if(pro.get_ValueAsString("DefaultValue") == null)
							{
								validLine = 1;								
							}
							else if (pro.get_ValueAsString("DefaultValue").equalsIgnoreCase("K"))
							{
								TP_TypeValue = "K";
								TP_DestinationRef_ID =0;								
							}
							else if (pro.get_ValueAsString("DefaultValue").equalsIgnoreCase("V"))
							{
								TP_TypeValue = "V";
								TP_DestinationRef_ID =0;								
							}
							else if (pro.get_ValueAsString("DefaultValue").equalsIgnoreCase("U"))
							{
								TP_TypeValue = "U";
								TP_DestinationRef_ID =0;								
							}
							else if (pro.get_ValueAsString("DefaultValue").equalsIgnoreCase("F"))
							{
								TP_TypeValue = "F";
								TP_DestinationRef_ID =0;								
							}
						}
						
						//validaciones que no se repitan numero de doc y que existan valores						
						//validacion numero de doc unico
						if (ISValid)
						{
							String sqlValidDM= "SELECT  count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
									"WHERE C_ProjectOFB_ID = ? AND mml.POReference like ? AND M_MovementLine_ID <> ? and mml.cost > 0";
							int cantSQL1 = DB.getSQLValue(get_TrxName(), sqlValidDM,move.get_ValueAsInt("C_ProjectOFB_ID"),line.get_ValueAsString("POReference"),line.get_ID());
							if (cantSQL1 > 0)
								validLine=1;
						}
						//validacion existencia de m3
						if (ISM3)
						{
							String sqlExM3= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
									"WHERE mm.M_Movement_ID = ? AND mml.TP_VolM3 > 0 ";
							int cantSQL2 = DB.getSQLValue(get_TrxName(), sqlExM3,move.get_ID());
							if(cantSQL2 == 0)
								validLine=1;													
						}							
						//validacion existencia de numero de documento
						if (ISDN)
						{
							String sqlExDN= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
									"WHERE mm.M_Movement_ID = ? AND mml.POReference IS NOT NULL ";
							int cantSQL3 = DB.getSQLValue(get_TrxName(), sqlExDN,move.get_ID());
							if(cantSQL3 == 0)
								validLine=1;							
						}
						//end validaciones
						boolean ValidNull = pro.get_ValueAsBoolean("ValidNull");
						int TP_DesValid = line.get_ValueAsInt("TP_Destination_ID");
						
						if (TP_DesValid < 1 & ValidNull == false)
							validLine =1;
						
						//ciclo para costear mas de 1 ves la misma linea  
						
						String sqlTP_Cant = "SELECT COUNT(TP_CostingValues_ID) FROM TP_CostingValues "+
						"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' "+
						"AND Isactive = 'Y' AND TP_Destination_ID = ? "+
						"AND (TP_Type = ?  OR TP_Type Is null)";
					
						int cant = DB.getSQLValue(get_TrxName(), sqlTP_Cant, pro.get_ID(),line.get_ValueAsInt("TP_Destination_ID"),type);
						
						//si hay mas de una vez hara un ciclo y costera mas veces
						if (cant > 1)
						{
							BigDecimal SumCosto = new BigDecimal("0.0");
							
							String sqlCiclo = "SELECT TP_CostingValues_ID FROM TP_CostingValues "+
									"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' "+
									"AND Isactive = 'Y' AND TP_Destination_ID = ? "+
									"AND (TP_Type = ?  OR TP_Type Is null)";

							try
							{
								PreparedStatement pstmtC = null;
								pstmtC = DB.prepareStatement(sqlCiclo, get_TrxName());
								pstmtC.setInt(1, pro.get_ID()); 
								pstmtC.setInt(2, line.get_ValueAsInt("TP_Destination_ID"));
								pstmtC.setString(3, type);
								
								ResultSet rsC = pstmtC.executeQuery();
							
								while (rsC.next())
								{
									
									X_TP_CostingValues CVAlueIDC = new X_TP_CostingValues(getCtx(), rsC.getInt("TP_CostingValues_ID"), get_TrxName());
									
									if (CVAlueIDC.getTP_TypeValue().equalsIgnoreCase("F") && validLine==0)
									{						
										SumCosto = SumCosto.add(CVAlueIDC.getAmt());
									}									
									else if (CVAlueIDC.getTP_TypeValue().equalsIgnoreCase("K") && validLine==0)
									{										
										//se comprueba variable valor maximo
										BigDecimal kmnew = new BigDecimal("0.0"); 
										try{
											kmnew = (BigDecimal)CVAlueIDC.get_Value("MaxType");		
											if (kmnew != null && kmHR.compareTo(kmnew) > 0 && kmnew.compareTo(Env.ZERO) > 0)
												kmHR=kmnew;
										} catch (Exception e)
										{
											log.log(Level.SEVERE, e.getMessage(), e);
										}
										//end
										
										if (CVAlueIDC.getAmt().compareTo(Env.ZERO) > 0)
										{	
											SumCosto = SumCosto.add(kmHR.multiply(CVAlueIDC.getAmt()));											
										}
										else
										{
											String sqlTP_KMC = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
													"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' AND TP_TypeValue = 'K' "+
													"AND MinQty <= ? AND MaxQty >= ? AND (TP_Type = ?  OR TP_Type Is null) AND Isactive = 'Y'";
											
											int CostingValuesCicloK_ID = DB.getSQLValue(get_TrxName(), sqlTP_KMC, pro.get_ID(),kmAcu,kmAcu,type);
											
											if (CostingValuesCicloK_ID > 0)
											{
												X_TP_CostingValues CVAlueIDCK = new X_TP_CostingValues(getCtx(), CostingValuesCicloK_ID, get_TrxName());
											
												SumCosto = SumCosto.add(kmHR.multiply(CVAlueIDCK.getAmt()));									
											}
											else 
											{
												return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
														" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
											}
										}
									}else if (CVAlueIDC.getTP_TypeValue().equalsIgnoreCase("V") && validLine==0)
									{
										
										//se comprueba variable valor maximo
										BigDecimal volnew = new BigDecimal("0.0"); 
										try{
											volnew = (BigDecimal)CVAlueIDC.get_Value("MaxType");		
											if (volnew != null && volHR.compareTo(volnew) > 0 && volnew.compareTo(Env.ZERO) > 0)
												volHR=volnew;
										} catch (Exception e)
										{
											log.log(Level.SEVERE, e.getMessage(), e);
										}
										//end
										
										if (CVAlueIDC.getAmt().compareTo(Env.ZERO) > 0)
										{
											SumCosto = SumCosto.add(volHR.multiply(CVAlueIDC.getAmt()));											
										}
										else
										{
											String sqlTP_KMC = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
													"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' AND TP_TypeValue = 'V' "+
													"AND MinQty <= ? AND MaxQty >= ? AND (TP_Type = ?  OR TP_Type Is null) AND Isactive = 'Y'";
											
											int CostingValuesCicloK_ID = DB.getSQLValue(get_TrxName(), sqlTP_KMC, pro.get_ID(),volAcu,volAcu,type);
											
											if (CostingValuesCicloK_ID > 0)
											{
												X_TP_CostingValues CVAlueIDCK = new X_TP_CostingValues(getCtx(), CostingValuesCicloK_ID, get_TrxName());
											
												SumCosto = SumCosto.add(volHR.multiply(CVAlueIDCK.getAmt()));									
											}
											else 
											{
												return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
														" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
											}
										}
									}
								}
								//fuera del ciclo se actualizan los valores
								if(lineaunica == 0)
									line.set_CustomColumn("Cost", SumCosto);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
								
								rsC.close();
								pstmtC.close();
							}
							catch (Exception e)
							{
								log.log(Level.SEVERE, e.getMessage(), e);
							}
							
						}//se mantiene logica anterior
						else
						{	
							if (TP_TypeValue.equalsIgnoreCase("F") && validLine==0)
							{						
								if (TP_DestinationRef_ID > 0)
								{	
									costoTotal = cvalues.getAmt();
								}else
								{
									return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
											" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();									
								}							
								if (lineaunica == 0)
									line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
							}
							if (TP_TypeValue.equalsIgnoreCase("U") && validLine==0)
							{
								costoTotal = cvalues.getAmt();		
								if (lineaunica == 0)
									line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
								//	para que no entre en otras lineas de esta hr
								lineaunica = 1;
								//	para corregir lineas ya costeadas
							
								String sqlLN = "UPDATE M_MovementLine SET Cost=0 WHERE M_Movement_ID ="+
										line.getM_Movement_ID()+" AND M_MovementLine_ID <> "+line.getM_MovementLine_ID();   

								//	correcion hojas ya costeadas 
								DB.executeUpdate(sqlLN,get_TrxName());
							
							}
							else if (TP_TypeValue.equalsIgnoreCase("K") && TP_DestinationRef_ID > 0 && validLine==0)
							{	
								//se comprueba variable valor maximo
								BigDecimal kmnew = new BigDecimal("0.0"); 
								try{
									kmnew = (BigDecimal)cvalues.get_Value("MaxType");		
									if (kmnew != null && kmHR.compareTo(kmnew) > 0 && kmnew.compareTo(Env.ZERO) > 0)
										kmHR=kmnew;
								} catch (Exception e)
								{
									log.log(Level.SEVERE, e.getMessage(), e);
								}
								//end
								
								if (cvalues.getAmt().compareTo(Env.ZERO) > 0)
								{									
									costoTotal = kmHR.multiply(cvalues.getAmt());											
								}
								else
								{
									String sqlTP_Vn2 = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
											"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' AND TP_TypeValue = 'K' "+
											"AND MinQty <= ? AND MaxQty >= ? AND TP_Type = ? AND Isactive = 'Y'";
									
									int CostingValuesN2_ID = DB.getSQLValue(get_TrxName(), sqlTP_Vn2, pro.get_ID(),kmAcu,kmAcu,type);
								
									if (CostingValuesN2_ID > 0)
									{
										X_TP_CostingValues cvaluesN2 = new X_TP_CostingValues(getCtx(), CostingValuesN2_ID, get_TrxName());
									
										costoTotal = kmHR.multiply(cvaluesN2.getAmt());
									}
									else
									{
										return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
												" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
									}
									
								}
								if(lineaunica == 0)
									line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
								
								
							}
							else if (TP_TypeValue.equalsIgnoreCase("K") && TP_DestinationRef_ID < 1 && validLine==0)
							{
								
								//se comprueba variable valor maximo
								BigDecimal kmnew = new BigDecimal("0.0"); 
								try{
									kmnew = (BigDecimal)cvalues.get_Value("MaxType");		
									if (kmnew != null && kmHR.compareTo(kmnew) > 0 && kmnew.compareTo(Env.ZERO) > 0)
										kmHR=kmnew;
								} catch (Exception e)
								{
									log.log(Level.SEVERE, e.getMessage(), e);
								}
								//end
								
								String sqlTP_Vn2 = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
										"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' AND TP_TypeValue = 'K' "+
										"AND MinQty <= ? AND MaxQty >= ? AND TP_Type = ? AND Isactive = 'Y' ";
								
								int CostingValuesN2_ID = DB.getSQLValue(get_TrxName(), sqlTP_Vn2, pro.get_ID(),kmAcu,kmAcu,type);
																
								if (CostingValuesN2_ID > 0)
								{
									X_TP_CostingValues cvaluesN2 = new X_TP_CostingValues(getCtx(), CostingValuesN2_ID, get_TrxName());
								
									costoTotal = kmHR.multiply(cvaluesN2.getAmt());								

								}
								else
								{
									return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
											" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
								}
								
								if(lineaunica == 0)
									line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
							}
							else if (TP_TypeValue.equalsIgnoreCase("V") && validLine==0)
							{
								//se comprueba variable valor maximo
								BigDecimal volnew = new BigDecimal("0.0"); 
								try{
									volnew = (BigDecimal)cvalues.get_Value("MaxType");		
									if (volnew != null && volHR.compareTo(volnew) > 0 && volnew.compareTo(Env.ZERO) > 0)
										volHR=volnew;
								} catch (Exception e)
								{
									log.log(Level.SEVERE, e.getMessage(), e);
								}
								//end
								
								if (cvalues.getAmt().compareTo(Env.ZERO) > 0)
								{
									costoTotal = volHR.multiply(cvalues.getAmt());											
								}
								else
								{
								
									String sqlTP_Vn2 = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
											"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' AND TP_TypeValue = 'V' "+
											"AND MinQty <= ? AND MaxQty >= ? AND TP_Type = ? AND Isactive = 'Y'";
									
									int CostingValuesN2_ID = DB.getSQLValue(get_TrxName(), sqlTP_Vn2, pro.get_ID(),volAcu,volAcu,type);
								
									if (CostingValuesN2_ID > 0)
									{
										X_TP_CostingValues cvaluesN2 = new X_TP_CostingValues(getCtx(), CostingValuesN2_ID, get_TrxName());
									
										costoTotal = volHR.multiply(cvaluesN2.getAmt());
									
									}
									else
									{
										return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
												" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
									}																		
								}
								if(lineaunica == 0)
									line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
							}
						}//end 
					}
					//nuevo costeo por matriz desde flota  
					
					if (CTC.equalsIgnoreCase("P"))		
					{
						String TP_TypeValue = "";
						int TP_DestinationRef_ID = 0;
						X_TP_CostingValues cvalues = null;
						boolean ISM3 = false;
						boolean ISDN = false;
						boolean ISValid = false;
						int validLine = 0;
						
						String sqlTP_V = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
								"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'P' "+
								"AND Isactive = 'Y' AND TP_Destination_ID = ? AND TP_DestinationRef_ID = ? AND (TP_Type = ?  OR TP_Type Is null)  ";
							
						int CostingValues_ID = DB.getSQLValue(get_TrxName(), sqlTP_V, pro.get_ID(),line.get_ValueAsInt("TP_Destination_ID"),String.valueOf(idOrigen),type);
							
						if (CostingValues_ID > 0)
						{
							cvalues = new X_TP_CostingValues(getCtx(), CostingValues_ID, get_TrxName());
							TP_TypeValue = cvalues.getTP_TypeValue(); 
							TP_DestinationRef_ID = cvalues.get_ValueAsInt("TP_Destination_ID");
							ISM3 = cvalues.get_ValueAsBoolean("ExistM3");
							ISDN = cvalues.get_ValueAsBoolean("ExistDN");
							ISValid = cvalues.get_ValueAsBoolean("ValidDocument");							
						}	
						else 									
						{
							String sqlTP_Valid = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
									"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'P' "+
									"AND Isactive = 'Y' AND TP_Destination_ID > 0 ";
								
							int CostingValuesValid_ID = DB.getSQLValue(get_TrxName(), sqlTP_Valid, pro.get_ID());
								
							if (CostingValuesValid_ID > 0)
							{
								cvalues = new X_TP_CostingValues(getCtx(), CostingValuesValid_ID, get_TrxName());
								ISM3 = cvalues.get_ValueAsBoolean("ExistM3");
								ISDN = cvalues.get_ValueAsBoolean("ExistDN");
								ISValid = cvalues.get_ValueAsBoolean("ValidDocument");
							}
							else 
							{
								return "No se Pueden setear parametros de validacion: Flota "+pro.getName()+
										" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
							}
							
							
						}
						
						//validaciones que no se repitan numero de doc y que existan valores						
						//validacion numero de doc unico
						if (ISValid)
						{
							String sqlValidDM= "SELECT  count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
									"WHERE C_ProjectOFB_ID = ? AND mml.POReference like ? AND M_MovementLine_ID <> ? and mml.cost > 0";
							int cantSQL1 = DB.getSQLValue(get_TrxName(), sqlValidDM,move.get_ValueAsInt("C_ProjectOFB_ID"),line.get_ValueAsString("POReference"),line.get_ID());
							if (cantSQL1 > 0)
								validLine=1;
						}
						//validacion existencia de m3
						if (ISM3)
						{
							String sqlExM3= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
									"WHERE mm.M_Movement_ID = ? AND mml.TP_VolM3 > 0 ";
							int cantSQL2 = DB.getSQLValue(get_TrxName(), sqlExM3,move.get_ID());
							if(cantSQL2 == 0)
								validLine=1;													
						}							
						//validacion existencia de numero de documento
						if (ISDN)
						{
							String sqlExDN= "SELECT count(1) FROM M_MovementLine mml INNER JOIN M_Movement mm ON (mml.M_Movement_ID = mm.M_Movement_ID) "+
									"WHERE mm.M_Movement_ID = ? AND mml.POReference IS NOT NULL ";
							int cantSQL3 = DB.getSQLValue(get_TrxName(), sqlExDN,move.get_ID());
							if(cantSQL3 == 0)
								validLine=1;							
						}
						//end validaciones
						boolean ValidNull = pro.get_ValueAsBoolean("ValidNull");
						int TP_DesValid = line.get_ValueAsInt("TP_Destination_ID");
						
						if (TP_DesValid < 1 & ValidNull == false)
							validLine =1;
						
						//ciclo para costear mas de 1 ves la misma linea  
						
						String sqlTP_Cant = "SELECT COUNT(TP_CostingValues_ID) FROM TP_CostingValues "+
						"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'P' "+
						"AND Isactive = 'Y' AND TP_Destination_ID = ? AND TP_DestinationRef_ID = ?"+
						"AND (TP_Type = ?  OR TP_Type Is null)";
					
						int cant = DB.getSQLValue(get_TrxName(), sqlTP_Cant, pro.get_ID(),line.get_ValueAsInt("TP_Destination_ID"),String.valueOf(idOrigen),type);
						
						//si hay mas de una vez hara un ciclo y costera mas veces
						if (cant > 1)
						{
							BigDecimal SumCosto = new BigDecimal("0.0");
							
							String sqlCiclo = "SELECT TP_CostingValues_ID FROM TP_CostingValues "+
									"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'P' "+
									"AND Isactive = 'Y' AND TP_Destination_ID = ? AND TP_DestinationRef_ID = ? "+
									"AND (TP_Type = ?  OR TP_Type Is null)";

							try
							{
								PreparedStatement pstmtC = null;
								pstmtC = DB.prepareStatement(sqlCiclo, get_TrxName());
								pstmtC.setInt(1, pro.get_ID()); 
								pstmtC.setInt(2, line.get_ValueAsInt("TP_Destination_ID"));
								pstmtC.setString(3, String.valueOf(idOrigen));
								pstmtC.setString(4, type);
								
								ResultSet rsC = pstmtC.executeQuery();
							
								while (rsC.next())
								{									
									X_TP_CostingValues CVAlueIDC = new X_TP_CostingValues(getCtx(), rsC.getInt("TP_CostingValues_ID"), get_TrxName());
									
									if (CVAlueIDC.getTP_TypeValue().equalsIgnoreCase("F") && validLine==0)
									{						
										SumCosto = SumCosto.add(CVAlueIDC.getAmt());
									}									
									else if (CVAlueIDC.getTP_TypeValue().equalsIgnoreCase("K") && validLine==0)
									{
										
										//se comprueba variable valor maximo
										BigDecimal kmnew = new BigDecimal("0.0"); 
										try{
											kmnew = (BigDecimal)CVAlueIDC.get_Value("MaxType");		
											if (kmnew != null && kmHR.compareTo(kmnew) > 0 && kmnew.compareTo(Env.ZERO) > 0)
												kmHR=kmnew;
										} catch (Exception e)
										{
											log.log(Level.SEVERE, e.getMessage(), e);
										}
										//end
										
										if (CVAlueIDC.getAmt().compareTo(Env.ZERO) > 0)
										{
											SumCosto = SumCosto.add(kmHR.multiply(CVAlueIDC.getAmt()));											
										}
										else
										{
											String sqlTP_KMC = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
													"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'P' AND TP_TypeValue = 'K' "+
													"AND MinQty <= ? AND MaxQty >= ? AND (TP_Type = ?  OR TP_Type Is null) AND Isactive = 'Y'";
											
											int CostingValuesCicloK_ID = DB.getSQLValue(get_TrxName(), sqlTP_KMC, pro.get_ID(),kmAcu,kmAcu,type);
											
											if (CostingValuesCicloK_ID > 0)
											{
												X_TP_CostingValues CVAlueIDCK = new X_TP_CostingValues(getCtx(), CostingValuesCicloK_ID, get_TrxName());
											
												SumCosto = SumCosto.add(kmHR.multiply(CVAlueIDCK.getAmt()));									
											}
											else 
											{
												return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
														" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
											}
										}
									}else if (CVAlueIDC.getTP_TypeValue().equalsIgnoreCase("V") && validLine==0)
									{
										
										BigDecimal volnew = new BigDecimal("0.0"); 
										try{
											volnew = (BigDecimal)CVAlueIDC.get_Value("MaxType");		
											if (volnew != null && volHR.compareTo(volnew) > 0 && volnew.compareTo(Env.ZERO) > 0)
												volHR=volnew;
										} catch (Exception e)
										{
											log.log(Level.SEVERE, e.getMessage(), e);
										}
										//end
										
										if (CVAlueIDC.getAmt().compareTo(Env.ZERO) > 0)
										{
											SumCosto = SumCosto.add(kmHR.multiply(CVAlueIDC.getAmt()));											
										}
										else
										{
											String sqlTP_KMC = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
													"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' AND TP_TypeValue = 'V' "+
													"AND MinQty <= ? AND MaxQty >= ? AND (TP_Type = ?  OR TP_Type Is null) AND Isactive = 'Y'";
											
											int CostingValuesCicloK_ID = DB.getSQLValue(get_TrxName(), sqlTP_KMC, pro.get_ID(),volAcu,volAcu,type);
											
											if (CostingValuesCicloK_ID > 0)
											{
												X_TP_CostingValues CVAlueIDCK = new X_TP_CostingValues(getCtx(), CostingValuesCicloK_ID, get_TrxName());
											
												SumCosto = SumCosto.add(kmHR.multiply(CVAlueIDCK.getAmt()));									
											}
											else 
											{
												return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
														" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
											}
										}
									}
								}
								//fuera del ciclo se actualizan los valores
								if(lineaunica == 0)
									line.set_CustomColumn("Cost", SumCosto);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
								
								rsC.close();
								pstmtC.close();
							}
							catch (Exception e)
							{
								log.log(Level.SEVERE, e.getMessage(), e);
							}
							
						}//se mantiene logica anterior
						else
						{		
							if (TP_TypeValue.equalsIgnoreCase("F") && validLine==0)
							{						
								if (TP_DestinationRef_ID > 0)
								{	
									costoTotal = cvalues.getAmt();
								}else
								{
									return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
											" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();									
								}							
								if (lineaunica == 0)
									line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
							}
							if (TP_TypeValue.equalsIgnoreCase("U") && validLine==0)
							{
								costoTotal = cvalues.getAmt();		
								if (lineaunica == 0)
									line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
								//	para que no entre en otras lineas de esta hr
								lineaunica = 1;
								//	para corregir lineas ya costeadas
							
								String sqlLN = "UPDATE M_MovementLine SET Cost=0 WHERE M_Movement_ID ="+
										line.getM_Movement_ID()+" AND M_MovementLine_ID <> "+line.getM_MovementLine_ID();   

								//	correcion hojas ya costeadas 
								DB.executeUpdate(sqlLN,get_TrxName());
							
							}
							else if (TP_TypeValue.equalsIgnoreCase("K") && TP_DestinationRef_ID > 0 && validLine==0)
							{
								//se comprueba variable valor maximo
								BigDecimal kmnew = new BigDecimal("0.0"); 
								try{
									kmnew = (BigDecimal)cvalues.get_Value("MaxType");		
									if (kmnew != null && kmHR.compareTo(kmnew) > 0 && kmnew.compareTo(Env.ZERO) > 0)
										kmHR=kmnew;
								} catch (Exception e)
								{
									log.log(Level.SEVERE, e.getMessage(), e);
								}
								//end
								
								if (cvalues.getAmt().compareTo(Env.ZERO) > 0)
								{
									costoTotal = kmHR.multiply(cvalues.getAmt());											
								}
								else
								{
									String sqlTP_Vn2 = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
											"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' AND TP_TypeValue = 'K' "+
											"AND MinQty <= ? AND MaxQty >= ? AND TP_Type = ? AND Isactive = 'Y'";
									
									int CostingValuesN2_ID = DB.getSQLValue(get_TrxName(), sqlTP_Vn2, pro.get_ID(),kmAcu,kmAcu,type);
								
									if (CostingValuesN2_ID > 0)
									{
										X_TP_CostingValues cvaluesN2 = new X_TP_CostingValues(getCtx(), CostingValuesN2_ID, get_TrxName());
									
										costoTotal = kmHR.multiply(cvaluesN2.getAmt());
									}
									else
									{
										return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
												" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
									}
									
								}
								if(lineaunica == 0)
									line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
								
								
							}							
							else if (TP_TypeValue.equalsIgnoreCase("V") && validLine==0)
							{
								
								//se comprueba variable valor maximo
								BigDecimal volnew = new BigDecimal("0.0"); 
								try{
									volnew = (BigDecimal)cvalues.get_Value("MaxType");		
									if (volnew != null && volHR.compareTo(volnew) > 0 && volnew.compareTo(Env.ZERO) > 0)
										volHR=volnew;
								} catch (Exception e)
								{
									log.log(Level.SEVERE, e.getMessage(), e);
								}
								//end
								
								if (cvalues.getAmt().compareTo(Env.ZERO) > 0)
								{
									costoTotal = volHR.multiply(cvalues.getAmt());											
								}
								else
								{								
									String sqlTP_Vn2 = "SELECT MAX(TP_CostingValues_ID) FROM TP_CostingValues "+
											"WHERE C_ProjectOFB_ID = ? AND TP_CostingType = 'D' AND TP_TypeValue = 'V' "+
											"AND MinQty <= ? AND MaxQty >= ? AND TP_Type = ? AND Isactive = 'Y'";
									
									int CostingValuesN2_ID = DB.getSQLValue(get_TrxName(), sqlTP_Vn2, pro.get_ID(),volAcu,volAcu,type);
								
									if (CostingValuesN2_ID > 0)
									{
										X_TP_CostingValues cvaluesN2 = new X_TP_CostingValues(getCtx(), CostingValuesN2_ID, get_TrxName());
									
										costoTotal = volHR.multiply(cvaluesN2.getAmt());
									
									}
									else
									{
										return "SubLinea sin rango de valores ingresados: Flota "+pro.getName()+
												" Hoja de Ruta(ID) "+move.get_ID()+ " Linea(ID) "+line.get_ID();
									}																		
								}
								if(lineaunica == 0)
									line.set_CustomColumn("Cost", costoTotal);					
								line.set_CustomColumn("KMAcu", kmAcu);
								line.set_CustomColumn("VolAcu", volAcu);							
								if (line.getMovementQty().equals(Env.ZERO) | line.getMovementQty() == null )
									line.setMovementQty(Env.ONE);
								line.save();
							}
						}//end 
						if (line.get_ValueAsInt("TP_Destination_ID") > 0)
						{
							X_TP_Destination tpDes2 = new X_TP_Destination(getCtx(), line.get_ValueAsInt("TP_Destination_ID"), get_TrxName());
							if (tpDes2.get_ValueAsBoolean("IsSource"))
							{
								idOrigen = line.get_ValueAsInt("TP_Destination_ID");
							}									
						}						
					}
					
					//end nuevo costeo
				  }
				}
				move.set_CustomColumn("Costed", true);				
				move.save();
				
				contador= contador +1;
			}
		rs.close();
		pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return "Se han costeado " +contador + " Hojas de Ruta";
	}	//	doIt
	
}	//	OrderOpen
