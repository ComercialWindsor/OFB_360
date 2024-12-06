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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.X_TP_Refund;
import org.compiere.model.X_TP_RefundLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class ProcessRefund extends SvrProcess
{
	private int				p_TP_Viatico_ID = 0; 
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 p_TP_Viatico_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_TP_Viatico_ID > 0)
		{
			X_TP_Refund viatico = new X_TP_Refund(getCtx(), p_TP_Viatico_ID, get_TrxName());
			//firmas solo se validaran antes de completar, no al proecsar
			if(viatico.getDocStatus().compareTo("WC") == 0)
			{
				Boolean sig1 = viatico.get_ValueAsBoolean("Signature1");
				Boolean sig2 = viatico.get_ValueAsBoolean("Signature2");
				Boolean sig3 = viatico.get_ValueAsBoolean("Signature3");
				if(viatico.getType().compareTo("01") == 0)
				{
					if(sig2 == false || sig3 == false)
						throw new AdempiereException("Error: No se puede completar sin las firmas necesarias");
				}
				else if (viatico.getType().compareTo("02") == 0)
					if(sig1 == false || sig2 == false || sig3 == false)
						throw new AdempiereException("Error: No se puede completar sin las firmas necesarias");
			}
			if(viatico.get_ValueAsBoolean("overwrite"))
			{
				viatico.setDocStatus("CO");
				viatico.setProcessed(true);
			}
			else
			{	
				//validacion 2 viaticos normales existentes
				if(viatico.getType().compareTo("01") == 0)
				{
					/*int cantV = DB.getSQLValue(get_TrxName(),"SELECT COUNT(1) FROM TP_Refund WHERE C_BPartner_ID = "+viatico.getC_BPartner_ID()+
							" AND Type = '01' AND TP_Refund_ID <> "+viatico.get_ID()+" AND DateDoc = ?",viatico.getDateDoc());
					if(cantV > 0)
						throw new AdempiereException("ERROR: Ya existe mas de un viatico para la misma fecha");*/
					int cantRep = DB.getSQLValue(get_TrxName(), "SELECT COALESCE(COUNT(1),0)FROM TP_RefundLine " +
							" WHERE M_Movement_ID > 0 AND TP_Refund_ID="+viatico.get_ID()+" GROUP BY DateTrx HAVING COUNT(1) > 1");
						if(cantRep > 0)
							throw new AdempiereException("ERROR: Existe mas de una Hoja de Ruta para la misma Fecha");
				}
				//validaciones de linea
				String sqlLine = "SELECT TP_RefundLine_ID FROM TP_RefundLine WHERE IsActive = 'Y'" +
						" AND TP_Refund_ID = ?";
				PreparedStatement pstmt = null;					

				pstmt = DB.prepareStatement(sqlLine, get_TrxName());
				pstmt.setInt(1, viatico.get_ID());
				ResultSet rs = pstmt.executeQuery();
				while(rs.next())
				{
					//validación misma linea mas de una vez
					X_TP_RefundLine rLine = new X_TP_RefundLine (getCtx(),rs.getInt("TP_RefundLine_ID"),get_TrxName() );
					
					//validacion de fecha
					if(rLine.getM_Movement_ID() > 0)
					{
						//MMovement mov = new MMovement(getCtx(), rLine.getM_Movement_ID(), get_TrxName());
						//ininoles la comparacion se hace con los valores de las fecha de las lineas 
						Timestamp MaxDate = DB.getSQLValueTS(get_TrxName(), "SELECT MAX(TP_InicialHR) FROM M_MovementLine WHERE M_Movement_ID = "+rLine.getM_Movement_ID());
						Timestamp MinDate = DB.getSQLValueTS(get_TrxName(), "SELECT MIN(TP_FinalHR) FROM M_MovementLine WHERE M_Movement_ID = "+rLine.getM_Movement_ID());
						MinDate.setHours(0);
						MinDate.setMinutes(0);
						MinDate.setSeconds(0);
						if(rLine.getDateTrx().compareTo(MaxDate) > 0 
								||rLine.getDateTrx().compareTo(MinDate) < 0)
							throw new AdempiereException("ERROR: Fecha de HR no coincide con fecha de viatico");
					}	
					//validacion misma fecha y socio de negocio repetido no importa el tipo de viatico
					//Disponibilidad ya existente
					/*if(rLine.getM_Movement_ID() > 0)
					{
						int cantRepLine = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
							" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
							" WHERE Pre_M_Movement_ID > 0 AND r.C_BPartner_ID = "+viatico.getC_BPartner_ID()+
							" AND rl.DateTrx = ?",rLine.getDateTrx());
						if (cantRepLine > 0)
							throw new AdempiereException("ERROR: Ya existe un viatico por disponibilidad para la misma fecha y conductor");
					}*/	
					//hoja de ruta ya existente
					/*if(rLine.get_ValueAsInt("Pre_M_Movement_ID") > 0)
					{
						int cantRepLine = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine rl" +
							" INNER JOIN TP_Refund r ON (rl.TP_Refund_ID = r.TP_Refund_ID) " +
							" WHERE M_Movement_ID > 0 AND r.C_BPartner_ID = "+viatico.getC_BPartner_ID()+
							" AND rl.DateTrx = ?",rLine.getDateTrx());
						if (cantRepLine > 0)
							throw new AdempiereException("ERROR: Ya existe un viatico por hoja de ruta para la misma fecha y conductor");
					}*/
					if(viatico.getType().compareTo("02") == 0)
					{
						int cant = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM TP_RefundLine" +
							" WHERE IsActive = 'Y' AND TP_RefundLine_ID <> "+rLine.get_ID()+" AND M_Movement_ID = "+rLine.getM_Movement_ID()+
							" AND TP_RefundAmt_ID = "+rLine.get_ValueAsInt("TP_RefundAmt_ID")+" AND DateTrx = ?",rLine.getDateTrx());
						if (cant > 0)
							throw new AdempiereException("ERROR: Existe una HR y concepto repetido");
						//hr obligatoria
						//ininoles se excluye enex pelambres y enex caldera
						//if(rLine.getM_Movement_ID() <=0 && rLine.get_ValueAsInt("TP_RefundAmt_ID") > 0)
						if(rLine.getM_Movement_ID() <=0 && rLine.get_ValueAsInt("TP_RefundAmt_ID") > 0 
								&& (rLine.getTP_Refund().getAD_Org_ID() != 1000004 && rLine.getTP_Refund().getAD_Org_ID() != 1000017))
							throw new AdempiereException("ERROR: Concepto sin HR ingresada");														
					}
				}
				rs.close();
				pstmt.close();
				pstmt = null;
				
				if(viatico.getDocStatus().compareTo("DR") == 0)
					viatico.setDocStatus("WC");
				else if (viatico.getDocStatus().compareTo("WC") == 0)
				{
					viatico.setDocStatus("WC");
					viatico.setProcessed(true);
				}
			}
			viatico.save();
		}
		
		
	   return "Procesado ";
	}	//	doIt
}
