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

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.compiere.util.*;
import org.compiere.model.*;
import org.compiere.process.SvrProcess;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
/**
 *	Generate XML consolidated from Invoice 
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: VOIDHojaRutaTSM.java,v 1.2 19/05/2011 $
 */
public class VOIDHojaRutaTSM extends SvrProcess
{
	/** Properties						*/
	private Properties 		m_ctx;	
	private int p_M_Movement_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		p_M_Movement_ID=getRecord_ID();
		m_ctx = Env.getCtx();
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		MMovement mov=new MMovement(m_ctx,p_M_Movement_ID,get_TrxName());
		int qt = 0;
		String sql = "SELECT COALESCE(COUNT(*),0) FROM M_Movement mm " +
					"INNER JOIN M_MovementLine mml on (mm.M_Movement_ID = mml.M_Movement_ID) "+
					"INNER JOIN M_Product mp on (mml.M_Product_ID = mp.M_Product_ID) "+
					"WHERE ProductType <> 'S' AND mm.M_Movement_ID = ?";
		
		qt = DB.getSQLValue(get_TrxName(), sql, mov.get_ID());
		
		if (qt < 1)
		{
				//	Set lines to 0
			MMovementLine[] lines = mov.getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				MMovementLine line = lines[i];
				BigDecimal old = line.getMovementQty();
				//BigDecimal finalKM = (BigDecimal)line.get_Value("TP_FinalKM");
				//BigDecimal inicialKM = (BigDecimal)line.get_Value("TP_InicialKM");
				//BigDecimal totalKM = (BigDecimal)line.get_Value("TP_InicialKM");
				
				line.addDescription("Void (" + old + ")");
				line.setMovementQty(Env.ONE);
				line.set_CustomColumn("TP_FinalKM", Env.ZERO);
				line.set_CustomColumn("TP_InicialKM", Env.ZERO);
				line.set_CustomColumn("TP_InicialKM", Env.ZERO);
				line.save(get_TrxName());
			}
		}else
		{
			return "No es posible anular, existe un producto articulo";
		}
		
		mov.setProcessed(true);
		mov.setDocAction("--");
		mov.setDocStatus("VO");
		mov.save();
		
		return "Documento Anulado";
	}	//	doIt
	
}	//	InvoiceCreateInOut
