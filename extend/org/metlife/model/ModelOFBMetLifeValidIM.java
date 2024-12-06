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
package org.metlife.model;

import java.util.regex.Pattern;

import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_I_BPartnerIM;
import org.compiere.util.CLogger;

import java.util.regex.Matcher;
/**
 *	Validator for Metlife
 *
 *  @author Italo Niñoles
 */
public class ModelOFBMetLifeValidIM implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBMetLifeValidIM ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBMetLifeValidIM.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;
	

	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing global validator: "+this.toString());
		}

		//	Tables to be monitored
		//	Documents to be monitored
		engine.addModelChange(X_I_BPartnerIM.Table_Name, this); // ID tabla 323
				
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE)&& po.get_Table_ID()==X_I_BPartnerIM.Table_ID)  
		{
			X_I_BPartnerIM bpIM = (X_I_BPartnerIM) po;
			
			//validacion rut bp
			if (bpIM.getRUT_SOL() != null && bpIM.getRUT_SOL().trim() != "" && bpIM.getRUT_SOL().trim() != " ")
			{
				int x =validateRut(bpIM.getRUT_SOL());
				if(x==1)
					return "Formato no valido ingrese guion";
				if(x==2)
					return "Rut no Valido";
			}

			//validacion rut empresa
			if (bpIM.getEMP_RUT() != null && bpIM.getEMP_RUT().trim() != "" && bpIM.getEMP_RUT().trim() != " ")
			{
				int y =validateRut(bpIM.getEMP_RUT());
				if(y==1)
					return "Formato no valido ingrese guion";
				if(y==2)
					return "Rut no Valido";
			}			
			
			//validacion correo
			if (bpIM.getDIR_EML() != null && bpIM.getDIR_EML().trim() != "" && bpIM.getDIR_EML().trim() != " ")
			{
				if(!isEmail(bpIM.getDIR_EML()))
				return "Correo No Valido";
			}
			//validación nuevos campos
			if(bpIM.getTEL_CEL() != null)
				if (!isNumeric(bpIM.getTEL_CEL()))
					return "Número No valido";
			if(bpIM.getTEL_FIJO() != null)
				if (!isNumeric(bpIM.getTEL_FIJO()))
					return "Número No valido";
			if(bpIM.getFONO_AREA() != null)
				if (!isNumeric(bpIM.getFONO_AREA()))
					return "Número No valido";
			if(bpIM.getEMP_FONO_AREA() != null)
				if (!isNumeric(bpIM.getEMP_FONO_AREA()))
					return "Número No valido";
			if(bpIM.getEMP_FONO_NUM() != null)
				if (!isNumeric(bpIM.getEMP_FONO_NUM()))
					return "Número No valido";			
		}		
	return null;
	}	//	modelChange

	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);

		
		return null;
	}	//	docValidate

	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString}	
	private int validateRut (String p_rut)
	{
		
		if(p_rut.indexOf("-")<0)
			return 1;
		
		if(p_rut.length()<5)
			return 2;
		
		String rut= p_rut.split("-")[0];
		String digito = p_rut.split("-")[1];
		
		
		if(digito.length()>1)
			return 2;
		
		int M=0,S=1,T;
		try
		{
			T=Integer.parseInt(rut);
		}
		catch (Exception e)
		{
			return 2;
		}
		
		for(;T!=0;T/=10)
			S=(S+T%10*(9-M++%6))%11;
			
		char dig=(char)(S!=0?S+47:75);
		
		if(digito.charAt(0)!=dig)
			return 2;
			
		return 0;
	}
	
	public boolean isEmail(String correo) 
	{
		Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            System.out.println("[" + mat.group() + "]");
            return true;
        }else{
            return false;
        }        
    }
	
	private boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
}