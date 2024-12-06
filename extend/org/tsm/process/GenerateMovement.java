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
import java.util.logging.Level;

import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  @author Italo Niñoles
 */
public class GenerateMovement extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int ID_ProcessTSM = 0;
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;			
			else if (name.equals("ID_ProcessTSM"))
			{
				ID_ProcessTSM = para[i].getParameterAsInt();
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
		if(ID_ProcessTSM < 0)
			ID_ProcessTSM = 0;
		
		String sqlDet = "SELECT mm.id_hoja_ruta,mm.id_flota,mm.mov_codigo,mm.rampla_codigo,mm.rutconductor, " +
				" mm.MovementDate,mm.fechahorainicio,mm.fechahoracierre,mm.id_base_inicio,mm.odometro_inicial, " +
				" mm.odometro_final,TP_TollSpendingManual,I_MovementXML_ID, " +
				//ininoles campos de movementline
				" ml.Id_Hoja_Ruta_Detalle,ml.Id_Hoja_Ruta,ml.TP_LineNo,ml.POReference,ml.TP_FolioOUT,ml.TP_VolOUT, " +
				" M_ProductValue,ml.ID_Base_Inicio as Id_Base_InicioLine,ml.ID_Base_Destino,ml.TP_FolioIN,ml.TP_VolIN, " +
				" ml.TP_KMInicio,ml.TP_KMFin,ml.FechaHoraInicio as FechaHoraInicioLine,ml.FechaHoraFin, " +
				" ml.Id_Flota_Actividad,ml.TP_VolM3,ml.TP_M3Devol,ml.TipoParametro,ml.ValorParametro, ml.I_MovementLineXML_ID " +
				" FROM i_movementxml mm " +
				" LEFT JOIN i_movementlinexml ml ON (mm.Id_Hoja_Ruta = ml.Id_Hoja_Ruta)" +
				" WHERE mm.processed = 'N' AND (ml.processed = 'N' OR ml.processed IS NULL) " +
				" AND mm.AD_Org_ID != 100 and ml.AD_Org_ID != 100  ";
		if(ID_ProcessTSM > 0)
			sqlDet+= " AND ID_ProcessTSM = "+ID_ProcessTSM;
		else
			sqlDet+= " AND (ID_ProcessTSM = 0 OR ID_ProcessTSM IS NULL) ";	
		sqlDet+=" Order By mm.Id_Hoja_Ruta Desc, Id_Hoja_Ruta_Detalle ASC";
		
		PreparedStatement pstmt = null;
		int cant = 0;
		String ID_movXML = "0";
		String ID_movXMLLine = "0";
		log.config("sqldet "+sqlDet);
		try
		{
			pstmt = DB.prepareStatement (sqlDet, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();			
			int LastMov = -1;
			int id_MovAD = 0;
			MMovement mov = null;			
			while (rs.next ())
			{
				id_MovAD = DB.getSQLValue(get_TrxName(),"SELECT MAX(M_Movement_ID) FROM M_Movement " +
						" WHERE TP_FolioTSM = '"+rs.getInt("Id_Hoja_Ruta")+"' AND DocStatus IN ('DR','IP','CO')"); 
				/*if(id_MovAD < 0)
					id_MovAD = 0;*/
				//ininoles generacion de cabecera
				
				//@mfrojas Al momento de revisar un registro, inmediatamente éste quedará con ad_org_id = 100.
				DB.executeUpdate("UPDATE I_MovementXML SET ad_org_id = 100 WHERE I_MovementXML_ID = "+rs.getInt("I_MovementXML_id"),get_TrxName());
				log.config("i_movementxml_id = "+rs.getInt("I_MovementXML_ID"));
				log.config("i_movementlinexml_id = "+rs.getInt("I_MovementLineXML_ID"));
				DB.executeUpdate("UPDATE I_MovementLineXML SET ad_org_id = 100 WHERE I_MovementLineXML_ID ="+rs.getInt("I_MovementLineXML_ID"),get_TrxName());
				//@mfrojas
				if(id_MovAD != LastMov)
				{
					boolean flag = false;
					mov = new MMovement(getCtx(), 0, get_TrxName());
					mov.setAD_Org_ID(1000000);					
					mov.setC_DocType_ID(1000022);
					mov.setDescription("Hoja de ruta XML");
					mov.save();
					mov.setDocStatus("DR");
					//campos TSM adicionales
					//buscamos ID flota
					mov.set_CustomColumn("TP_FolioTSM", Integer.toString(rs.getInt("ID_Hoja_Ruta")));
					if(rs.getInt("Id_Flota") <= 0)
						flag = true;
					mov.set_CustomColumn("C_ProjectOFB_ID", rs.getInt("ID_Flota"));					
					//buscamos id tracto
					int ID_Tracto = DB.getSQLValue(get_TrxName(), "SELECT MAX(A_Asset_ID) FROM A_Asset " +
							" WHERE IsActive = 'Y' AND value = '"+rs.getString("Mov_Codigo")+"'");
					if(ID_Tracto > 0)
						mov.set_CustomColumn("TP_Asset_ID", ID_Tracto);
					else
						flag = true;
					//buscamos id rampla
					int ID_Rampla = DB.getSQLValue(get_TrxName(), "SELECT MAX(A_Asset_ID) FROM A_Asset " +
							" WHERE IsActive = 'Y' AND value = '"+rs.getString("Rampla_Codigo")+"'");
					if(ID_Rampla > 0)
						mov.set_CustomColumn("TP_Asset_ID2", ID_Rampla);
					else
						flag = true;
					//buscamos id conductor
					int ID_Conductor = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Bpartner_ID) FROM C_Bpartner " +
							" WHERE IsActive = 'Y' AND (value = '"+rs.getString("rutconductor")+"' OR value||'-'||digito = '"+rs.getString("rutconductor")+"')");
					if(ID_Conductor > 0)
						mov.set_CustomColumn("C_Bpartner_ID", ID_Conductor);
					else
						flag = true;
					//seteo de fecha
					/*if(rs.getTimestamp("MovementDate") != null)
						mov.setMovementDate(rs.getTimestamp("MovementDate"));*/
					//ininoles se saca fecha de campo fecha hora inicio
					if(rs.getTimestamp("fechahorainicio") != null)
						mov.setMovementDate(rs.getTimestamp("fechahorainicio"));
					/*if(rs.getTimestamp("fechahorainicio") != null)
						mov.set_CustomColumn("TP_InicialHR", rs.getTimestamp("fechahorainicio"));
					if(rs.getTimestamp("fechahoracierre") != null)
						mov.set_CustomColumn("TP_FinalHR", rs.getTimestamp("fechahoracierre"));
						*/
					//buscamos base origen
					int ID_Base = DB.getSQLValue(get_TrxName(), "SELECT MAX(TP_Destination_ID) FROM TP_Destination " +
							" WHERE IsActive = 'Y' AND TP_CodDestination = '"+rs.getString("ID_Base_Inicio")+"'");
					//String valuesql = "";
					if(ID_Base > 0)
						mov.set_CustomColumn("TP_Destination_ID", ID_Base);		
					else
						flag = true;
					//seteamos odometros
					if(rs.getBigDecimal("odometro_inicial") != null)
						mov.set_CustomColumn("TP_InicialKm",rs.getBigDecimal("odometro_inicial"));
					if(rs.getBigDecimal("odometro_final") != null)
						mov.set_CustomColumn("TP_FinalKm",rs.getBigDecimal("odometro_final"));
					//peaje
					if(rs.getBigDecimal("TP_TollSpendingManual") != null)
						mov.set_CustomColumn("TP_TollSpendingManual",rs.getBigDecimal("TP_TollSpendingManual"));
					if(flag)
						mov.setDescription("Cabecera Hoja de ruta cargada con errores");
					mov.save();
					cant++;
				}		
				//generacion de detalle
				boolean flagLine = false;
				MMovementLine line = new MMovementLine(mov);
				line.setM_Product_ID(1000036);
				line.setMovementQty(Env.ONE);
				line.setM_Locator_ID(1000000);
				line.setM_LocatorTo_ID(1000005);
				line.save();
				//campos TSM Adicionales
				//N de Viaje
				if(rs.getBigDecimal("TP_LineNo") != null)
					line.set_CustomColumn("TP_LineNo",rs.getBigDecimal("TP_LineNo"));
				//guia / Factura
				if(rs.getString("POReference") != null)
					line.set_CustomColumn("POReference",rs.getString("POReference"));				
				//folio salida
				if(rs.getString("TP_FolioOUT") != null)
					line.set_CustomColumn("TP_FolioOUT",rs.getString("TP_FolioOUT"));
				//kg salida
				if(rs.getBigDecimal("TP_VolOUT") != null)
					line.set_CustomColumn("TP_VolOUT",rs.getBigDecimal("TP_VolOUT"));
				//producto buscado por value
				int ID_Product = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_Product_ID) FROM M_Product " +
						" WHERE IsActive = 'Y' AND Value = '"+rs.getString("M_ProductValue")+"'");
				if(ID_Product > 0)
					line.setM_Product_ID(ID_Product);		
				//base se buscara como codigo String, no como int
				//origen
				int ID_BaseOrigen = DB.getSQLValue(get_TrxName(), "SELECT MAX(TP_Destination_ID) FROM TP_Destination " +
						" WHERE IsActive = 'Y' AND TP_CodDestination = '"+rs.getString("Id_Base_InicioLine")+"'");
				if(ID_BaseOrigen > 0)
					line.set_CustomColumn("TP_Origen_ID", ID_BaseOrigen);
				else
					flagLine = true;
				//buscamos base destino
				int ID_Base = DB.getSQLValue(get_TrxName(), "SELECT MAX(TP_Destination_ID) FROM TP_Destination " +
						" WHERE IsActive = 'Y' AND TP_CodDestination = '"+rs.getString("ID_Base_Destino")+"'");
				if(ID_Base > 0)
					line.set_CustomColumn("TP_Destination_ID", ID_Base);
				else
					flagLine = true;
				//folio entrada
				if(rs.getString("TP_FolioIN") != null)
					line.set_CustomColumn("TP_FolioIN",rs.getString("TP_FolioIN"));
				//kg entrada
				if(rs.getBigDecimal("TP_VolIN") != null)
					line.set_CustomColumn("TP_VolIN",rs.getBigDecimal("TP_VolIN"));
				//KM Inicio
				if(rs.getBigDecimal("TP_KMInicio") != null)
					line.set_CustomColumn("TP_FinalKM",rs.getBigDecimal("TP_KMInicio"));
				//KM Fin
				if(rs.getBigDecimal("TP_KMFin") != null)
					line.set_CustomColumn("TP_InicialKM",rs.getBigDecimal("TP_KMFin"));
				//Hora/fecha salida
				if(rs.getTimestamp("FechaHoraInicio") != null)
					line.set_CustomColumn("TP_FinalHR", rs.getTimestamp("FechaHoraInicioLine"));
				//Hora/fecha llegada
				if(rs.getTimestamp("FechaHoraFin") != null)
					line.set_CustomColumn("TP_InicialHR", rs.getTimestamp("FechaHoraFin"));
				//codigo actividad
				if(rs.getInt("Id_Flota_Actividad") >= 0)
				{
					int id_codAct = rs.getInt("Id_Flota_Actividad");
					String codAct = "0"+id_codAct;
					line.set_CustomColumn("TP_ActivityCode",codAct);
				}
				else
					flagLine = true;
				//vol m3
				if(rs.getBigDecimal("TP_VolM3") != null)
					line.set_CustomColumn("TP_VolM3",rs.getBigDecimal("TP_VolM3"));
				//devolucion de m3
				if(rs.getBigDecimal("TP_M3Devol") != null)
					line.set_CustomColumn("TP_M3Devol",rs.getBigDecimal("TP_M3Devol"));
				line.save();
				LastMov = mov.get_ID();
				if(flagLine)
				{
					mov.setDescription("Linea Hoja de ruta cargada con errores");
					mov.save();
				}
				if(rs.getString("I_MovementXML_ID") != null)
					ID_movXML = ID_movXML+","+rs.getString("I_MovementXML_ID");
				if(rs.getString("I_MovementLineXML_ID") != null)
					ID_movXMLLine = ID_movXMLLine+","+rs.getString("I_MovementLineXML_ID");					
			}
					
			log.config("Se han generado "+cant+" hojas de ruta");
			//actualizamos registros procesados
			DB.executeUpdate("UPDATE I_MovementXML SET processed = 'Y' WHERE I_MovementXML_ID IN ("+ID_movXML+")",get_TrxName());
			DB.executeUpdate("UPDATE I_MovementLineXML SET processed = 'Y' WHERE I_MovementLineXML_ID IN ("+ID_movXMLLine+")",get_TrxName());
			rs.close ();
			pstmt.close ();
			pstmt = null;	
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}		
		return "OK";
	}	//	doIt
}	//	Replenish

