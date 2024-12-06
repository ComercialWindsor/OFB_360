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
package org.petroamerica.process;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;

import org.compiere.model.X_I_ConsolidadoBase;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;
import org.ofb.utils.DateUtils;


/**
 *	
 *	
 *  @author italo niñoles ininoles
 */
public class ImportFileCB extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private String 			p_PathFile;
	
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("archivo"))
				p_PathFile = para[i].getParameter().toString();
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
		String[] datosFStr = new String[4];
    	FileInputStream fis =new FileInputStream(p_PathFile);
	    InputStreamReader isr = new InputStreamReader(fis, "ISO-8859-1");
	    BufferedReader br = new BufferedReader(isr);
	    //se lee primera linea de archivo 
	    String linea=br.readLine();		   
	    //DB.executeUpdate("DELETE FROM I_ConsolidadoBase",get_TrxName());
	    DB.executeUpdate("UPDATE I_ConsolidadoBase SET processed = 'Y'",get_TrxName());
	    int cantLine = 0;
	    Timestamp date1 = null;
	    //int lastWarehouse =0;
	    //int lastLocator =0;
	    int ID_Warehouse = 0;
	    int ID_Locator=0;
	    int ID_Product =0;
	    int flag1 = 0;
	    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
	    //se recorre archivo
	    while(linea!=null)
	    {	
	    	log.config(linea);
	    	//se separan campos separados por;
	    	datosFStr = linea.split(";");
	    	//se valida que no hayan datos nulos
	    	if(datosFStr.length > 0)
	    	{
		    	if(datosFStr[1] != null || datosFStr[5] != null ||
		    			datosFStr[4] != null)
		    	{
		    		//se busca fecha y producto
		    		if(datosFStr.length > 4)
		    		{
			    		if(datosFStr[1] != null && datosFStr[5] != null
			    				&& datosFStr[1].toString().trim().length() > 0
		    					&& datosFStr[5].toString().trim().length() > 0
		    					&& datosFStr[18].toString().trim().length() > 0)
			    		{
			    			if(DateUtils.isDateddMMyyyyV2(datosFStr[5].toString().substring(0, 10)))
			    			{
			    				date1 = DateUtils.convertDateddMMyyyyV2(datosFStr[5].toString().substring(0, 10));
			    			}
			    			ID_Product = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_Product_ID) FROM M_Product WHERE IsActive='Y' AND name like '"+datosFStr[18].toString().trim()+"'");
			    		}
			    		
			    		
			    		if(date1 != null)
			    		{
			    			//datos de 
			    			if(datosFStr[1] != null && datosFStr[2] != null 
			    					&& datosFStr[1].toString().trim().length() > 0
			    					&& datosFStr[2].toString().trim().length() > 0)
			    			{	    				
			    				//se genera registro en tabla I
			    				ID_Warehouse = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_Warehouse_ID) FROM M_Warehouse WHERE IsActive='Y' AND  Value = '"+datosFStr[1]+"'") ;
			    				ID_Locator = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_Locator_ID) FROM M_Locator WHERE IsActive='Y' AND Value = '"+datosFStr[2]+"'") ;
					    		if(ID_Warehouse > 0 || ID_Locator > 0 )
			    				{
					    			//se valida que no exista registro anterior
					    			flag1 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM I_ConsolidadoBase " +
					    					" WHERE IsActive='Y' AND M_Locator_ID="+ID_Locator+" AND Date1=?",date1);
					    			if(flag1 <=0)
					    			{
					    				X_I_ConsolidadoBase con = new X_I_ConsolidadoBase(getCtx(), 0, get_TrxName());
					    				con.setM_Warehouse_ID(ID_Warehouse);
					    				con.setM_Locator_ID(ID_Locator);
					    				con.setSaldoInicial((BigDecimal)decimalFormat.parse(datosFStr[3].trim()));
							    		con.setComprasENAP((BigDecimal)decimalFormat.parse(datosFStr[4].trim()));
							    		con.setComprasESMAX((BigDecimal)decimalFormat.parse(datosFStr[6].trim()));
					    				con.setComprasENEX((BigDecimal)decimalFormat.parse(datosFStr[7].trim()));
					    				con.setComprasCOPEC((BigDecimal)decimalFormat.parse(datosFStr[8].trim()));
					    				con.setVentas((BigDecimal)decimalFormat.parse(datosFStr[10].trim()));
					    				con.setUsoPropio((BigDecimal)decimalFormat.parse(datosFStr[13].trim()));
					    				con.setUsoPropioPA((BigDecimal)decimalFormat.parse(datosFStr[14].trim()));
					    				con.setTransferencias((BigDecimal)decimalFormat.parse(datosFStr[15].trim()));
					    				con.setOtrasEntradas((BigDecimal)decimalFormat.parse(datosFStr[17].trim()));
					    				con.setOtrasSalidas((BigDecimal)decimalFormat.parse(datosFStr[18].trim()));
					    				con.setSaldoantesdevariacion((BigDecimal)decimalFormat.parse(datosFStr[19].trim()));
					    				con.setVariacion((BigDecimal)decimalFormat.parse(datosFStr[20].trim()));
					    				con.setStockFisico((BigDecimal)decimalFormat.parse(datosFStr[21].trim()));
					    				if(date1 != null)
					    					con.setDate1(date1);
					    				if(ID_Product > 0)
					    					con.set_CustomColumn("M_Product_ID", ID_Product);
					    				con.saveEx(get_TrxName());
					    				cantLine++;
					    			}						    		
						    		//lastWarehouse = ID_Warehouse;
						    		//lastLocator = ID_Locator;
			    				}
			    			}
			    			//sumatorias
			    			else if(datosFStr[3] != null && datosFStr[4] != null
			    					&& datosFStr[3].toString().trim().length() > 0
			    					&& datosFStr[4].toString().trim().length() > 0 )
			    			{
		    				
			    				X_I_ConsolidadoBase con = new X_I_ConsolidadoBase(getCtx(), 0, get_TrxName());
			    				con.setSaldoInicial((BigDecimal)decimalFormat.parse(datosFStr[3].trim()));
					    		con.setComprasENAP((BigDecimal)decimalFormat.parse(datosFStr[4].trim()));
					    		con.setComprasESMAX((BigDecimal)decimalFormat.parse(datosFStr[6].trim()));
			    				con.setComprasENEX((BigDecimal)decimalFormat.parse(datosFStr[7].trim()));
			    				con.setComprasCOPEC((BigDecimal)decimalFormat.parse(datosFStr[8].trim()));
			    				con.setVentas((BigDecimal)decimalFormat.parse(datosFStr[10].trim()));
			    				con.setUsoPropio((BigDecimal)decimalFormat.parse(datosFStr[13].trim()));
			    				con.setUsoPropioPA((BigDecimal)decimalFormat.parse(datosFStr[14].trim()));
			    				con.setTransferencias((BigDecimal)decimalFormat.parse(datosFStr[15].trim()));
			    				con.setOtrasEntradas((BigDecimal)decimalFormat.parse(datosFStr[17].trim()));
			    				con.setOtrasSalidas((BigDecimal)decimalFormat.parse(datosFStr[18].trim()));
			    				con.setSaldoantesdevariacion((BigDecimal)decimalFormat.parse(datosFStr[19].trim()));
			    				con.setVariacion((BigDecimal)decimalFormat.parse(datosFStr[20].trim()));
			    				con.setStockFisico((BigDecimal)decimalFormat.parse(datosFStr[21].trim()));
			    				if(date1 != null)
			    					con.setDate1(date1);
			    				if(ID_Product > 0)
			    					con.set_CustomColumn("M_Product_ID", ID_Product);
			    				con.saveEx(get_TrxName());
			    				cantLine++;
			    			}
			    		}
		    		}
		    	}
	    	}
	    	linea=br.readLine();	    	
		}
	    
	    commitEx();		 
		return "se han agregado "+cantLine+" lineas desde archivo";
	}	//	doIt
	
}	//	CopyOrder
