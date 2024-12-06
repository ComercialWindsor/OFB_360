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

/** Generated Model for EO_BudgetHdrEmployee
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_EO_BudgetHdrEmployee extends PO implements I_EO_BudgetHdrEmployee, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20141113L;

    /** Standard Constructor */
    public X_EO_BudgetHdrEmployee (Properties ctx, int EO_BudgetHdrEmployee_ID, String trxName)
    {
      super (ctx, EO_BudgetHdrEmployee_ID, trxName);
      /** if (EO_BudgetHdrEmployee_ID == 0)
        {
			setEO_BudgetHdrEmployee_ID (0);
        } */
    }

    /** Load Constructor */
    public X_EO_BudgetHdrEmployee (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_EO_BudgetHdrEmployee[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_User getAD_User() throws RuntimeException
    {
		return (I_AD_User)MTable.get(getCtx(), I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
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

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
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

	/** Set EO_BudgetHdrEmployee.
		@param EO_BudgetHdrEmployee_ID EO_BudgetHdrEmployee	  */
	public void setEO_BudgetHdrEmployee_ID (int EO_BudgetHdrEmployee_ID)
	{
		if (EO_BudgetHdrEmployee_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_EO_BudgetHdrEmployee_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_EO_BudgetHdrEmployee_ID, Integer.valueOf(EO_BudgetHdrEmployee_ID));
	}

	/** Get EO_BudgetHdrEmployee.
		@return EO_BudgetHdrEmployee	  */
	public int getEO_BudgetHdrEmployee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_EO_BudgetHdrEmployee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Status AD_Reference_ID=1000054 */
	public static final int STATUS_AD_Reference_ID=1000054;
	/** En Proceso = EP */
	public static final String STATUS_EnProceso = "EP";
	/** Esperando Aprobación = EA */
	public static final String STATUS_EsperandoAprobacion = "EA";
	/** Aprobado = AP */
	public static final String STATUS_Aprobado = "AP";
	/** Rechazado = RE */
	public static final String STATUS_Rechazado = "RE";
	/** Set Status.
		@param Status 
		Status of the currently running check
	  */
	public void setStatus (String Status)
	{

		set_Value (COLUMNNAME_Status, Status);
	}

	/** Get Status.
		@return Status of the currently running check
	  */
	public String getStatus () 
	{
		return (String)get_Value(COLUMNNAME_Status);
	}
}