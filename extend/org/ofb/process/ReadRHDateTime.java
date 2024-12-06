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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.*;

import org.compiere.model.X_RH_AttendanceAndPunctuality;
import org.compiere.model.X_RH_AttendancePunctualityLog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class ReadRHDateTime extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private String P_Archive;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("FileName"))
				P_Archive = (String)para[i].getParameter();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		BufferedReader br = null;
		int ok=0, err=0; 
		
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(P_Archive));
 
			while ((sCurrentLine = br.readLine()) != null) {
				log.fine("line: "+ sCurrentLine);
				
				String[] data = sCurrentLine.split(" ");
				String rut = data[1];
				String fecha = data[2];
				String hora = data[3];
				String tipo = data[4];
				
				//fix rut
				if(rut.charAt(0)=='0')
					rut = rut.substring(1);
				//-----
				
				int bPartner_ID = DB.getSQLValue(get_TrxName(), "select C_BPartner_ID from C_BPartner where value=?", rut);
				
				if(bPartner_ID>0)
				{
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					java.util.Date parsedDate = dateFormat.parse(fecha);
					java.sql.Timestamp rDate = new java.sql.Timestamp(parsedDate.getTime());
					
					int count = DB.getSQLValue(get_TrxName(), "select count(1) from RH_AttendanceAndPunctuality where C_BPartner_ID=? and RegisterTime=? and RegisterDate=?"
							,bPartner_ID,hora,rDate);
					if(count<=0)
					{
						X_RH_AttendanceAndPunctuality rh = new X_RH_AttendanceAndPunctuality( getCtx() ,0, get_TrxName());
						rh.setDescription(sCurrentLine);
						rh.setRegisterTime(hora);
						rh.setTimeType(tipo);
						rh.setC_BPartner_ID(bPartner_ID);
						rh.setRegisterDate(rDate);
						rh.save();
						ok++;
					}
				}
				else//error
				{
					X_RH_AttendancePunctualityLog rh = new X_RH_AttendancePunctualityLog( getCtx() ,0, get_TrxName());
					rh.setDescription(sCurrentLine);
					rh.save();
					err++;
				}
				
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

			return "OK : " + ok + " Errores :" + err;
		
	}	//	doIt


	
}	//	CopyOrder
