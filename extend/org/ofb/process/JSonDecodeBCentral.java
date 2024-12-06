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
package org.ofb.process;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import org.compiere.process.*;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.compiere.model.MConversionRate;




/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class JSonDecodeBCentral extends SvrProcess
{
	/**	The Order				*/
	private int		p_C_Currency_ID;
	private Timestamp  p_DateTrx;
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
			else if (name.equals("C_Currency_ID"))
				p_C_Currency_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if(name.equals("DateTrx"))
				p_DateTrx = ((Timestamp)para[i].getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		
		String moneda = "";
		
		log.config("fecha = "+p_DateTrx);
		//JSONParser parser = new JSONParser();
		if(p_C_Currency_ID == 2000000)
			moneda = "uf";
		else if(p_C_Currency_ID == 100)
			moneda = "dolar";
		else if(p_C_Currency_ID == 102)
			moneda = "euro";
		else
			return "No hay tasa de conversión";
	    //JsonParser jp = new JsonParser();
		//String date_s = "01-01-2017";
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String date_s = format.format(p_DateTrx);
		log.config("date_s "+date_s);
		String website = "http://mindicador.cl/api/"+moneda+"/"+date_s+"";
		InputStream is = new URL(website).openStream();
		log.config("website = "+website);
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
		
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		
		while ((counter = rd.read()) != -1)
			sb.append((char)counter);
		
		String jsonText = sb.toString();
		log.config("jsonText = "+jsonText);
		
	    JSONObject json = new JSONObject(jsonText);
	    JSONArray jsonarr = json.getJSONArray("serie");
	    
	    //Validar excepciones acá.
	    
	    String valor = jsonarr.getJSONObject(0).get("valor").toString();
	    

//		JsonObject json = sb.getAsJsonObject();

/*		URL url = new URL(website);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		log.config("date _s = "+date_s);
		log.config("currency = "+p_C_Currency_ID);
		log.config("website = "+website);
		//Convert to a JSON object to print data
		
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		
		JsonObject rootobj = root.getAsJsonObject();
		String valor = root.getAsString().
		//String valor = rootobj.get("valor").getAsString();
	*/	
		log.config("Valor : "+json.toString());
		//log.config("valor2 "+json.getString("valor"));
		log.config("valor2 "+valor);
		
		MConversionRate mm = new MConversionRate(getCtx(), 0, get_TrxName());
		
		mm.setC_Currency_ID(p_C_Currency_ID);
		mm.setC_Currency_ID_To(228);
		mm.setC_ConversionType_ID(114);
		mm.setValidFrom(p_DateTrx);
		mm.setValidTo(p_DateTrx);
		mm.setAD_Org_ID(0);
		
		mm.setMultiplyRate(new BigDecimal(valor));
		mm.save();
		
		return "";
	}	//	doIt
	
	
}	//	CopyFromOrder
