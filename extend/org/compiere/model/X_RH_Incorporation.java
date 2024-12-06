/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for Incorporation
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_RH_Incorporation extends PO implements I_RH_Incorporation, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120207L;

    /** Standard Constructor */
    public X_RH_Incorporation (Properties ctx, int Incorporation_ID, String trxName)
    {
      super (ctx, Incorporation_ID, trxName);
      /** if (Incorporation_ID == 0)
        {
			setDocumentDate (new Timestamp( System.currentTimeMillis() ));
			setDocumentType (null);
			setIncorporation_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_Incorporation (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_Incorporation[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_DM_Document getContr() throws RuntimeException
    {
		return (I_DM_Document)MTable.get(getCtx(), I_DM_Document.Table_Name)
			.getPO(getContract(), get_TrxName());	}

	/** Set Contract.
		@param Contract Contract	  */
	public void setContract (int Contract)
	{
		set_Value (COLUMNNAME_Contract, Integer.valueOf(Contract));
	}

	/** Get Contract.
		@return Contract	  */
	public int getContract () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Contract);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DocumentDate.
		@param DocumentDate DocumentDate	  */
	public void setDocumentDate (Timestamp DocumentDate)
	{
		set_Value (COLUMNNAME_DocumentDate, DocumentDate);
	}

	/** Get DocumentDate.
		@return DocumentDate	  */
	public Timestamp getDocumentDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DocumentDate);
	}

	/** DocumentType AD_Reference_ID=1000042 */
	public static final int DOCUMENTTYPE_AD_Reference_ID=1000042;
	/** Incorporación = INC */
	public static final String DOCUMENTTYPE_Incorporacion = "INC";
	/** Desvinculación = DVL */
	public static final String DOCUMENTTYPE_Desvinculacion = "DVL";
	/** Renovación = RVN */
	public static final String DOCUMENTTYPE_Renovacion = "RVN";
	/** Renuncia = RNC */
	public static final String DOCUMENTTYPE_Renuncia = "RNC";
	/** Despido = DSP */
	public static final String DOCUMENTTYPE_Despido = "DSP";
	/** Set Document Type.
		@param DocumentType 
		Document Type
	  */
	public void setDocumentType (String DocumentType)
	{

		set_Value (COLUMNNAME_DocumentType, DocumentType);
	}

	/** Get Document Type.
		@return Document Type
	  */
	public String getDocumentType () 
	{
		return (String)get_Value(COLUMNNAME_DocumentType);
	}

	/** Set Incorporation.
		@param Incorporation_ID Incorporation	  */
	public void setIncorporation_ID (int Incorporation_ID)
	{
		if (Incorporation_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_Incorporation_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_Incorporation_ID, Integer.valueOf(Incorporation_ID));
	}

	/** Get Incorporation.
		@return Incorporation	  */
	public int getIncorporation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Incorporation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Confirmed.
		@param IsConfirmed 
		Assignment is confirmed
	  */
	public void setIsConfirmed (boolean IsConfirmed)
	{
		set_Value (COLUMNNAME_IsConfirmed, Boolean.valueOf(IsConfirmed));
	}

	/** Get Confirmed.
		@return Assignment is confirmed
	  */
	public boolean isConfirmed () 
	{
		Object oo = get_Value(COLUMNNAME_IsConfirmed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_DM_Document getResolut() throws RuntimeException
    {
		return (I_DM_Document)MTable.get(getCtx(), I_DM_Document.Table_Name)
			.getPO(getResolution(), get_TrxName());	}

	/** Set DM_Resolution_ID.
		@param Resolution DM_Resolution_ID	  */
	public void setResolution (int Resolution)
	{
		set_Value (COLUMNNAME_Resolution, Integer.valueOf(Resolution));
	}

	/** Get DM_Resolution_ID.
		@return DM_Resolution_ID	  */
	public int getResolution () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Resolution);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}