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
package org.blumos.process;


import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.math.BigDecimal;

import org.adempiere.exceptions.AdempiereException;
import org.blumos.model.BlumosUtilities;
import org.compiere.model.MUser;
import org.compiere.model.X_T_BL_VAC_MOVIMIENTOS;
import org.compiere.model.X_T_BL_VAC_SOLICITUD;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *  @author Italo Niñoles
 */
public class ProcessVacations extends SvrProcess
{	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int 			Record_ID;
	private String			p_Action;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("DocAction"))
				p_Action = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		X_T_BL_VAC_SOLICITUD solVac = new X_T_BL_VAC_SOLICITUD(getCtx(), Record_ID, get_TrxName());
		MUser user = new MUser(getCtx(), Env.getAD_User_ID(getCtx()),get_TrxName());
		//calculos seran en model 
		/*double v_normal = DB.getSQLValue(get_TrxName(), "Select Sum(Dias) From T_Bl_Vac_Movimientos Where Fecha_Movimiento<=sysdate And C_Bpartner_Id=partner_Id And Progresivo='N'");
		double v_progresivo = DB.getSQLValue(get_TrxName(), "SELECT nvl((select sum(dias) from t_bl_vac_movimientos where fecha_movimiento<=sysdate and c_bpartner_id=partner_id and progresivo='Y'),0) from dual");
		double v_pp = DB.getSQLValue(get_TrxName(), "SELECT ((Trunc(Mod(Months_Between(Sysdate,Fecha_Ingreso),12)))*1.25) from t_bl_vac_maestro where c_bpartner_id=partner_id");
		double v_total = v_normal+v_pp+v_progresivo;
		
		solVac.setSALDO_NORMAL(BigDecimal.valueOf(v_normal));
		solVac.setSALDO_PP(BigDecimal.valueOf(v_pp));
		solVac.setSALDO_PROGRESIVO(BigDecimal.valueOf(v_progresivo));
		solVac.setSALDO_TOTAL(BigDecimal.valueOf(v_total));
		
		double v_dias = BlumosUtilities.DameDiasHabiles(solVac.getDESDE(), solVac.getHASTA(), getCtx(), get_TrxName());
		
		if(solVac.isMEDIO_DIA())
			v_dias = 0.5;
		
		solVac.setDIAS(BigDecimal.valueOf(v_dias));
		solVac.save(get_TrxName());
		
		*/
		String v_mail_usuario = DB.getSQLValueString(get_TrxName(), "SELECT damemail(bp.c_bpartner_id,1) FROM c_bpartner bp " +
				" inner join t_bl_cargos tc on (bp.t_bl_cargos_id=tc.t_bl_cargos_id)" +
				" left join c_bpartner bpj on (tc.supervisor_id=bpj.t_bl_cargos_id)" +
				" where bp.c_bpartner_id=?", solVac.getC_BPartner_ID());
		
		String v_mail_jefe = DB.getSQLValueString(get_TrxName(), "SELECT damemail(bpj.c_bpartner_id,1) FROM c_bpartner bp " +
				" inner join t_bl_cargos tc on (bp.t_bl_cargos_id=tc.t_bl_cargos_id)" +
				" left join c_bpartner bpj on (tc.supervisor_id=bpj.t_bl_cargos_id)" +
				" where bp.c_bpartner_id=?", solVac.getC_BPartner_ID());
		
		/*int cargo_id = DB.getSQLValue(get_TrxName(), "SELECT tc.t_bl_cargos_id FROM c_bpartner bp " +
				" inner join t_bl_cargos tc on (bp.t_bl_cargos_id=tc.t_bl_cargos_id)" +
				" left join c_bpartner bpj on (tc.supervisor_id=bpj.t_bl_cargos_id)" +
				" where bp.c_bpartner_id=?", solVac.getC_BPartner_ID());*/
		int cargo_id = DB.getSQLValue(get_TrxName(), "SELECT tc.t_bl_cargos_id FROM c_bpartner bp " +
				" inner join t_bl_cargos tc on (bp.t_bl_cargos_id=tc.t_bl_cargos_id)" +
				" left join c_bpartner bpj on (tc.supervisor_id=bpj.t_bl_cargos_id)" +
				" where bp.c_bpartner_id=?", user.getC_BPartner_ID());
		
		String v_mail_gerente = BlumosUtilities.DameMail(solVac.getC_BPartner_ID(), 2, getCtx(), get_TrxName());
		
		MUser updatedBy = new MUser(getCtx(), Env.getAD_User_ID(getCtx()), get_TrxName());
		String pptext = "";
		if(solVac.isPROGRESIVO())
			pptext = "SI";
		else
			pptext = "NO";
		
		if(p_Action.compareTo("SO") == 0 && solVac.getDocStatus().compareTo("DR") == 0)// solicitar
		{
			//validamos que hayan fecha y dias mayor que cero
			if(solVac.getDESDE() == null)
				throw new AdempiereException("DEBE ESPECIFICAR FECHA DE INICIO DE FERIADO. PUSO: "+solVac.getDESDE());
			else if(solVac.getHASTA() == null)
				throw new AdempiereException("DEBE ESPECIFICAR FECHA DE FIN DE FERIADO. PUSO: "+solVac.getHASTA());
			else if(solVac.getHASTA().compareTo(solVac.getDESDE()) < 0)
				throw new AdempiereException("FECHA DE FIN NO PUEDE SER ANTERIOR A FECHA DE INICIO. PUSO DESDE: "+solVac.getDESDE()+" HASTA: "+solVac.getHASTA());
			else if(solVac.getDIAS().compareTo(new BigDecimal("0.5")) == 0 
					&& BlumosUtilities.DameDiasHabiles(solVac.getDESDE(), solVac.getHASTA(), getCtx(), get_TrxName()) != 1)
				throw new AdempiereException("RANGO DE FECHAS EXCESIVO PARA SOLICITAR MEDIO DIA DE VACACIONES. CAMBIE EL RANGO DE FECHAS O VERIFIQUE QUE ESTE SOLICITANDO UN DIA HÁBIL");
			else if(solVac.getDESDE().getMonth() != solVac.getHASTA().getMonth())
				throw new AdempiereException(" FECHA DE INICIO Y FIN DEBE ESTAR EN EL MISMO MES. PUSO DESDE: "+solVac.getDESDE()+" HASTA "+solVac.getHASTA());
			else if(BlumosUtilities.DameVacFechaUsada(solVac.getC_BPartner_ID(),solVac.getDESDE(),solVac.getHASTA(),getCtx(),get_TrxName()))
				throw new AdempiereException(" YA HA SOLICITADO VACACIONES EN UNO O MAS DIAS INCLUIDOS EN EL RANGO DE FECHAS. REVISE SU HISTORIAL ");
			else if(solVac.getCOMENTARIOS() == null || solVac.getCOMENTARIOS().trim().length()<1)
				throw new AdempiereException(" DEBE INGRESAR ALGUN COMENTARIO  ");
			
			String el_subject = "Solicitud de vacaciones";
			String el_correo = "<pre>"+el_subject+", Trabajador: "+solVac.getC_BPartner().getName()+" \n "+
				" Fecha Solicitud: "+BlumosUtilities.formatDate(solVac.getFECHA_MOVIMIENTO(),true)+" \n "+
				" Descripción: "+solVac.getCOMENTARIOS()+" \n \n "+
				" Días solicitados: "+solVac.getDIAS()+", Desde: "+BlumosUtilities.formatDate(solVac.getDESDE(), true)+" Hasta "+BlumosUtilities.formatDate(solVac.getHASTA(),true)+" \n "+
				" Progresivo: "+pptext+" \n \n "+
				" Saldos ANTES de esta solicitud: \n "+
				" Días Normales: "+solVac.getSALDO_NORMAL()+" \n "+
				" Días proporcionales: "+solVac.getSALDO_PP()+" \n "+
				" Días Progresivos: "+solVac.getSALDO_PROGRESIVO()+" \n "+
				" Saldo TOTAL: "+solVac.getSALDO_TOTAL()+" \n \n "+
				" Generado por: "+updatedBy.getName();
			log.config("correo: "+el_correo);
			//envio de correo via DB
			String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+v_mail_jefe+"','"+v_mail_usuario+"'|| ',' ||'"+v_mail_gerente+"','cmendoza@blumos.cl','"+el_subject+"','"+el_correo+"</pre>')";
			PreparedStatement pstmtSM = DB.prepareStatement (sendMail, get_TrxName());
			pstmtSM.execute();
			
			solVac.setDocStatus("IP");
			solVac.save(get_TrxName());
		}
		if(p_Action.compareTo("AP") == 0 && solVac.getDocStatus().compareTo("IP") == 0)// aprobar
		{
			if(solVac.getDIAS().compareTo(Env.ZERO) <= 0)
				throw new AdempiereException("NO PUEDE SOLICITAR 0 DIAS");
			String v_raiz = DB.getSQLValueString(get_TrxName(), "SELECT t_bl_cargos.raiz FROM c_bpartner inner join t_bl_cargos on (c_bpartner.t_bl_cargos_id=t_bl_cargos.t_bl_cargos_id) where c_bpartner_id="+solVac.getC_BPartner_ID());
			if(solVac.getC_BPartner_ID() == updatedBy.getC_BPartner_ID() 
					&& v_raiz.compareTo("Y") != 0)
			{
				solVac.setAPROBAR(false);
				throw new AdempiereException("NO PUEDE APROBAR SUS PROPIAS VACACIONES: "+updatedBy.getName()+" = "+solVac.getC_BPartner().getName()+
						" (ID:"+solVac.getC_BPartner_ID()+"="+updatedBy.getC_BPartner_ID());
			}
			else if(BlumosUtilities.DameVacFechaUsada(solVac.getC_BPartner_ID(),solVac.getDESDE(),solVac.getHASTA(),getCtx(),get_TrxName()))
				throw new AdempiereException("YA SE HA APROBADO VACACIONES EN UNO O MAS DIAS INCLUIDOS EN EL RANGO DE FECHAS. REVISE EL HISTORIAL");
			
			
			if(!BlumosUtilities.IsDependiente(solVac.getC_BPartner_ID(),cargo_id, getCtx(),get_TrxName()))
				throw new AdempiereException("NO PUEDE APROBAR ESTA SOLICITUD, "+solVac.getC_BPartner().getName()+" NO ESTÁ BAJO SU DEPENDENCIA");
			else
			{	
				//envio de correo
				String el_subject = "Solicitud de vacaciones APROBADA";
				String el_correo = "<pre>"+el_subject+", Trabajador: "+solVac.getC_BPartner().getName()+" \n "+
					" Fecha Solicitud: "+BlumosUtilities.formatDate(solVac.getFECHA_MOVIMIENTO(),true)+" \n "+
					" Descripción: "+solVac.getCOMENTARIOS()+" \n \n "+
					" Días solicitados: "+solVac.getDIAS()+", Desde: "+BlumosUtilities.formatDate(solVac.getDESDE(),true)+" Hasta "+BlumosUtilities.formatDate(solVac.getHASTA(),true)+" \n "+
					" Progresivo: "+pptext+" \n \n "+
					" Saldos ANTES de esta solicitud: \n "+
					" Días Normales: "+solVac.getSALDO_NORMAL()+" \n "+
					" Días proporcionales: "+solVac.getSALDO_PP()+" \n "+
					" Días Progresivos: "+solVac.getSALDO_PROGRESIVO()+" \n "+
					" Saldo TOTAL: "+solVac.getSALDO_TOTAL()+" \n \n "+
					" Solicitud APROBADA por: "+updatedBy.getName();
				log.config("correo: "+el_correo);
				
				//envio de correo via DB
				String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+v_mail_jefe+"','"+v_mail_usuario+"'|| ',' ||'"+v_mail_gerente+",ltoloza@blumos.cl','cmendoza@blumos.cl','"+el_subject+"','"+el_correo+"</pre>')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, get_TrxName());
				pstmtSM.execute();
				//se crea registro historico
				X_T_BL_VAC_MOVIMIENTOS newMov = new X_T_BL_VAC_MOVIMIENTOS(getCtx(), 0, get_TrxName());
				newMov.setAD_Org_ID(solVac.getAD_Org_ID());
				newMov.setC_BPartner_ID(solVac.getC_BPartner_ID());
				newMov.setFECHA_MOVIMIENTO(solVac.getFECHA_MOVIMIENTO());
				newMov.setTIPO_MOVIMIENTO("C");
				newMov.setDESDE(solVac.getDESDE());
				newMov.setHASTA(solVac.getHASTA());
				newMov.setPROGRESIVO(solVac.isPROGRESIVO());
				newMov.setDIAS(solVac.getDIAS().negate());
				newMov.setCOMENTARIOS(solVac.getCOMENTARIOS());
				newMov.setProcessed(true);
				newMov.setIMPRIME_COMPROBANTE("N");
				newMov.setT_BL_VAC_SOLICITUD_ID(solVac.get_ID());
				newMov.save(get_TrxName());	
				
				solVac.setProcessed(true);
				solVac.setDocStatus("CO");
				solVac.setT_BL_VAC_MOVIMIENTOS_ID(newMov.get_ID());
				solVac.saveEx(get_TrxName());
			}
		}
		if(p_Action.compareTo("VO") == 0 && solVac.isProcessed() == false)// anular
		{
			if(solVac.getC_BPartner_ID() == updatedBy.getC_BPartner_ID())
			{
				solVac.setANULAR(false);
				solVac.save(get_TrxName());
				throw new AdempiereException("NO PUEDE ANULAR SUS PROPIAS VACACIONES: "+updatedBy.getName()+" = "+solVac.getC_BPartner().getName()+
						" (ID:"+solVac.getC_BPartner_ID()+"="+updatedBy.getC_BPartner_ID());
			}
			else
			{
				solVac.setProcessed(true);
				solVac.setDocStatus("VO");
				solVac.save(get_TrxName());
				
				//envio de correo
				String el_subject = "Solicitud de vacaciones DENEGADA";
				String el_correo = el_subject+", Trabajador: "+solVac.getC_BPartner().getName()+" \n "+
					" Fecha Solicitud: "+BlumosUtilities.formatDate(solVac.getFECHA_MOVIMIENTO(),true)+" \n "+
					" Descripción: "+solVac.getCOMENTARIOS()+" \n \n "+
					" Días solicitados: "+solVac.getDIAS()+", Desde: "+BlumosUtilities.formatDate(solVac.getDESDE(),true)+" Hasta "+BlumosUtilities.formatDate(solVac.getHASTA(),true)+" \n "+
					" Progresivo: "+pptext+" \n \n "+
					" Saldos ANTES de esta solicitud: \n "+
					" Días Normales: "+solVac.getSALDO_NORMAL()+" \n "+
					" Días proporcionales: "+solVac.getSALDO_PP()+" \n "+
					" Días Progresivos: "+solVac.getSALDO_PROGRESIVO()+" \n "+
					" Saldo TOTAL: "+solVac.getSALDO_TOTAL()+" \n \n "+
					" Solicitud RECHAZADA por: : "+updatedBy.getName();
				
				log.config("correo: "+el_correo);
			    //envio de correo via DB
				String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+v_mail_jefe+"','"+v_mail_usuario+"'|| ',' ||'"+v_mail_gerente+"','cmendoza@blumos.cl','"+el_subject+"','"+el_correo+"</pre>')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, get_TrxName());
				pstmtSM.execute();
				
				/*DB.executeUpdate("SELECT send_mail('adempiere@blumos.cl','"+v_mail_jefe+"','"+v_mail_usuario+"'|| ',' ||'"+v_mail_gerente+"','cmendoza@blumos.cl','"+el_subject+"','"+el_correo+"') " +
						" FROM C_Bpartner WHERE C_Bpartner_ID = "+solVac.getC_BPartner_ID(), get_TrxName());*/
			}
		}
		if(p_Action.compareTo("RE") == 0 && solVac.isProcessed())// revertir
		{
			if(solVac.getC_BPartner_ID() == updatedBy.getC_BPartner_ID())
			{
				solVac.setANULAR(false);
				solVac.save(get_TrxName());
				throw new AdempiereException("NO PUEDE ANULAR SUS PROPIAS VACACIONES: "+updatedBy.getName()+" = "+solVac.getC_BPartner().getName()+
						" (ID:"+solVac.getC_BPartner_ID()+"="+updatedBy.getC_BPartner_ID());
			}
			else
			{
				solVac.setProcessed(true);
				solVac.setDocStatus("VO");
				solVac.save(get_TrxName());
				
				//se crea registro que anula
				X_T_BL_VAC_MOVIMIENTOS newMov = new X_T_BL_VAC_MOVIMIENTOS(getCtx(), 0, get_TrxName());
				newMov.setAD_Org_ID(solVac.getAD_Org_ID());
				newMov.setC_BPartner_ID(solVac.getC_BPartner_ID());
				newMov.setFECHA_MOVIMIENTO(solVac.getFECHA_MOVIMIENTO());
				newMov.setTIPO_MOVIMIENTO("C");
				newMov.setDESDE(solVac.getDESDE());
				newMov.setHASTA(solVac.getHASTA());
				newMov.setPROGRESIVO(solVac.isPROGRESIVO());
				newMov.setDIAS(solVac.getDIAS());
				newMov.setCOMENTARIOS("ANULA ID "+solVac.getT_BL_VAC_MOVIMIENTOS_ID());
				newMov.setProcessed(true);
				newMov.setIMPRIME_COMPROBANTE("N");
				newMov.setT_BL_VAC_SOLICITUD_ID(solVac.get_ID());
				newMov.save(get_TrxName());
				
				solVac.setProcessed(true);
				solVac.setDocStatus("VO");
				solVac.setT_BL_VAC_MOVIMIENTOS_ID(newMov.get_ID());
				solVac.save(get_TrxName());
				// no se envia correo al revertir
			}
		}		
		return "Solicitud de Vacaciones ID "+solVac.get_ID()+" ejecutado. DIAS:"+BlumosUtilities.DameDiasHabiles(solVac.getDESDE(),solVac.getHASTA(), getCtx(), get_TrxName());
	}	//	doIt
	
	
}	//	Replenish

