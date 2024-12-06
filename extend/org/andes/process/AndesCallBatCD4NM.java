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
package org.andes.process;

import org.compiere.process.SvrProcess;
import org.ofb.model.OFBForward;
 
/**
 *	Generate XML from MInOut
 *	
 *  @author Italo Niñoles Ininoles
 *  @version $Id: ExportDTEMInOut.java,v 1.2 05/09/2014 $
 */
public class AndesCallBatCD4NM extends SvrProcess
{
	/** Properties						*/
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		
	}	//	prepare

	
	/**
	 * 	Create Shipment
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{	
		String rutaBat = OFBForward.PathBatIMacroCD4NM();
		String sqlFile = "C:\\Comp4_Imacros\\cartola_bancochile2.0.iim";
		ejecucion(sqlFile,rutaBat);
		String msg = "OK";
		//delay a espera de terminar de ejecutar imacro
		try 
    	{
    		Thread.sleep (540000);
    	} catch (Exception e) 
    	{
    		log.config("Error al esperar tiempo");
    	}
		
		return msg;
	}	//	doIt
	
	public String ejecucion(String rutaIM, String rutaBat)
	{
		Runtime aplicacion = Runtime.getRuntime(); 
	    try
	    {	
	    	String eje = "cmd.exe /K "+rutaBat+" "+rutaIM;
	    	aplicacion.exec(eje);
	    	/*try 
	    	{
	    		Thread.sleep (200000);
	    	} catch (Exception e) 
	    	{
	    		log.config("Error al esperar tiempo");
	    	}*/
	    }
	    catch(Exception e)
	    {
	    	return e.toString();
	    }	    
	    return "Proceso OK";
	} 
	
}	//	InvoiceCreateInOut
