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
 *****************************************************************************/
package org.compiere.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MInterestArea;
import org.compiere.model.MMailText;
import org.compiere.model.MStore;
import org.compiere.model.MUser;
import org.compiere.model.MUserMail;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Msg;
import java.util.Date;
import java.util.*;
import java.text.*;
/**
 *  Send Mail to Vendors - Payment Request
 *
 *  @author Julio Farias -  SienaIT Consultores
 *  @version $Id: SendMailText.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ProcessPRequestMail extends SvrProcess
{
	/** What to send			*/
	private int				m_R_MailText_ID = -1;
	/**	Mail Text				*/
	private MMailText		m_MailText = null;

	/**	From (sender)			*/
	private int				m_AD_User_ID = -1;
	/** Client Info				*/
	private MClient			m_client = null;
	/**	From					*/
	private MUser			m_from = null;
	/** Recipient List to prevent duplicate mails	*/
	private ArrayList<Integer>	m_list = new ArrayList<Integer>();

	
	private int 			m_counter = 0;
	private int 			m_errors = 0;
	/**	To Subscribers 			*/
	private int				m_R_InterestArea_ID = -1;
	/** Interest Area			*/
	private MInterestArea 	m_ia = null;
	/** To Customer Type		*/
	private int				m_C_BP_Group_ID = -1;
	/** To Purchaser of Product	*/
	//	comes here


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
			else if (name.equals("R_InterestArea_ID"))
				m_R_InterestArea_ID = para[i].getParameterAsInt();
			else if (name.equals("R_MailText_ID"))
				m_R_MailText_ID = para[i].getParameterAsInt();
			else if (name.equals("C_BP_Group_ID"))
				m_C_BP_Group_ID = para[i].getParameterAsInt();
			else if (name.equals("AD_User_ID"))
				m_AD_User_ID = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws Exception
	{
		log.info("R_MailText_ID=" + m_R_MailText_ID);
		//	Mail Test
		m_MailText = new MMailText (getCtx(), m_R_MailText_ID, get_TrxName());
		if (m_MailText.getR_MailText_ID() == 0)
			throw new Exception ("Not found @R_MailText_ID@=" + m_R_MailText_ID);
		//	Client Info
		m_client = MClient.get (getCtx());
		if (m_client.getAD_Client_ID() == 0)
			throw new Exception ("Not found @AD_Client_ID@");
		if (m_client.getSMTPHost() == null || m_client.getSMTPHost().length() == 0)
			throw new Exception ("No SMTP Host found");
		//
		if (m_AD_User_ID > 0)
		{
			m_from = new MUser (getCtx(), m_AD_User_ID, get_TrxName());
			if (m_from.getAD_User_ID() == 0)
				throw new Exception ("No found @AD_User_ID@=" + m_AD_User_ID);
		}
		log.fine("From " + m_from);
		long start = System.currentTimeMillis();
		
		if (m_R_InterestArea_ID > 0)
			sendInterestArea();
		if (m_C_BP_Group_ID > 0)
			sendBPGroup();

		return "@Created@=" + m_counter + ", @Errors@=" + m_errors + " - "
			+ (System.currentTimeMillis()-start) + "ms";
	}	//	doIt

	/**
	 * 	Send to InterestArea
	 */
	private void sendInterestArea()
	{
		log.info("R_InterestArea_ID=" + m_R_InterestArea_ID);
		m_ia = MInterestArea.get(getCtx(), m_R_InterestArea_ID);
		String unsubscribe = null;
		if (m_ia.isSelfService())
		{
			unsubscribe = "\n\n---------.----------.----------.----------.----------.----------\n"
				+ Msg.getElement(getCtx(), "R_InterestArea_ID")
				+ ": " + m_ia.getName()
				+ "\n" + Msg.getMsg(getCtx(), "UnsubscribeInfo")
				+ "\n";
			MStore[] wstores = MStore.getOfClient(m_client);
			int index = 0;
			for (int i = 0; i < wstores.length; i++)
			{
				if (wstores[i].isDefault())
				{
					index = i;
					break;
				}
			}
			if (wstores.length > 0)
				unsubscribe += wstores[index].getWebContext(true);
		}

		//
		String sql = "SELECT u.Name, u.EMail, u.AD_User_ID "
			+ "FROM R_ContactInterest ci"
			+ " INNER JOIN AD_User u ON (ci.AD_User_ID=u.AD_User_ID) "
			+ "WHERE ci.IsActive='Y' AND u.IsActive='Y'"
			+ " AND ci.OptOutDate IS NULL"
			+ " AND u.EMail IS NOT NULL"
			+ " AND ci.R_InterestArea_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, m_R_InterestArea_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Boolean ok = sendIndividualMail (rs.getString(1), rs.getString(2), null,rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7));
				if (ok == null)
					;
				else if (ok.booleanValue())
					m_counter++;
				else
					m_errors++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		//	Clean Up
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		m_ia = null;
	}	//	sendInterestArea
	
	
	/**
	 * 	Send to BPGroup
	 */
	private void sendBPGroup()
	{
		log.info("C_PaymentRequest_ID=" + m_C_BP_Group_ID);
		String sql = "select bp.name,p.email,p.description,bp.value || '-' || bp.digito as rut,i.documentno, p.amt,p.formapago "
		+ " from C_PaymentRequestLine p "
		+ " inner join c_invoice i on (p.c_invoice_id=i.c_invoice_id) "
		+ " inner join c_bpartner bp on (i.c_bpartner_id=bp.c_bpartner_id) "
		+ " where p.email is not null and p.C_PaymentRequest_ID=?";
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, m_C_BP_Group_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Boolean ok = sendIndividualMail (rs.getString(1), rs.getString(2), null,rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7));
				if (ok == null)
					;
				else if (ok.booleanValue())
					m_counter++;
				else
					m_errors++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		//	Clean Up
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
	}	//	sendBPGroup
	
	/**
	 * 	Send Individual Mail
	 *	@param Name user name
	 *	@param AD_User_ID user
	 *	@param unsubscribe unsubscribe message
	 *	@return true if mail has been sent
	 *///String sql = "select            bp.name,     p.email,           null         p.description,      rut,         i.documentno,    p.amt"
	private Boolean sendIndividualMail (String Name, String Correo, String unsubscribe, String Detalle, String Rut, String Factura, int Monto , String Forma)
	{
		
		String Vdesde = "pagoproveedores@aguasantofagasta.cl";
		String Asunto = "Comunica Pago Aguas Antofagasta S.A.";
		String message = Detalle;//m_MailText.getMailText(true);
		String Vrut = Rut;
		String Vfactura = Factura;
		int Vmonto = Monto;
		String Vname = Name;
		String Vforma = Forma;
		
		//	Unsubscribe
		if (unsubscribe != null)
			message += unsubscribe;
		//
		EMail email = m_client.createEMail(m_from, Correo, Asunto, message);
		if (m_MailText.isHtml())
			email.setMessageHTML(Asunto,message);
		else
		{
			java.util.Date fecha = new Date();
			DateFormat df =  DateFormat.getDateInstance();
			DateFormat df1 = DateFormat.getDateInstance(DateFormat.LONG);
			String s =  df.format(fecha);
			String s1 = df1.format(fecha);
			email.setFrom(Vdesde);
			email.setSubject (Asunto);
			
			StringBuilder sb = new StringBuilder();
			sb.append("\n Estimado Provedor, Comunico a usted que con fecha: "+ s1 + "\n, se ha Generado: "+ Vforma + ", por el Pago del Siguiente Documento. " + "\n Rut: "+ Vrut +"\n Nombre o Razón Social: "+ Vname + "\n " + "\n "+ "\n Nro Documento: "+ Vfactura + "\n Monto: "+ Vmonto + "\n " + "\n "+ "\n En caso de consultas, agradeceremos enviar correo electrónico a pagoproveedores@aguasantofagasta.cl"+ "\n "+ "\n "+ "\n Saluda atentamente a Usted."+ "\n Aguas de Antofagasta S.A.");
			
			email.setMessageText(sb.toString());
			
			
			//email.setMessageText ("\n Estimado Provedor, Comunico a usted que con fecha: "+ "24/01/2014 (Corrige aviso con Fecha Erronea anterior 17/01/2014), " + "\n se ha Generado: "+ Vforma + ", por el Pago del Siguiente Documento. " + "\n Rut: "+ Vrut +"\n Nombre o Razón Social: "+ Vname + "\n " + "\n "+ "\n Nro Documento: "+ Vfactura + "\n Monto: "+ Vmonto + "\n " + "\n "+ "\n En caso de consultas, agradeceremos enviar correo electrónico a pagoproveedores@aguasantofagasta.cl"+ "\n "+ "\n "+ "\n Saluda atentamente a Usted."+ "\n Aguas de Antofagasta S.A.");
			
			
		}
		if (!email.isValid() && !email.isValid(true))
		{
			log.warning("NOT VALID - " + email);
			//to.setIsActive(false);
			//to.addDescription("Invalid EMail");
			//to.save();
			return Boolean.FALSE;
		}
		boolean OK = EMail.SENT_OK.equals(email.send());
		//new MUserMail(m_MailText, AD_User_ID, email).save();
		//
		if (OK)
			log.fine(Correo);
		else
			log.warning("FAILURE - " + Correo);
		addLog(0, null, null, (OK ? "@OK@" : "@ERROR@") + " - " + Correo);
		return new Boolean(OK);
	}	//	sendIndividualMail

}	//	SendMailText
