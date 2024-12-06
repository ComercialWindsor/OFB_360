/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
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
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for C_PaymentMassiveLine
 *  @author Adempiere (generated) 
 *  @version Release 3.5.2aOFB v60 - $Id$ */
public class X_C_PaymentMassiveLine extends PO implements I_C_PaymentMassiveLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_C_PaymentMassiveLine (Properties ctx, int C_PaymentMassiveLine_ID, String trxName)
    {
      super (ctx, C_PaymentMassiveLine_ID, trxName);
      /** if (C_PaymentMassiveLine_ID == 0)
        {
			setC_Invoice_ID (0);
			setC_PaymentMassive_ID (0);
			setC_PaymentMassiveLine_ID (0);
			setPayAmt (Env.ZERO);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_C_PaymentMassiveLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_PaymentMassiveLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_Invoice getC_Invoice() throws Exception 
    {
        Class<?> clazz = MTable.getClass(I_C_Invoice.Table_Name);
        I_C_Invoice result = null;
        try	{
	        Constructor<?> constructor = null;
	    	constructor = clazz.getDeclaredConstructor(new Class[]{Properties.class, int.class, String.class});
    	    result = (I_C_Invoice)constructor.newInstance(new Object[] {getCtx(), new Integer(getC_Invoice_ID()), get_TrxName()});
        } catch (Exception e) {
	        log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
	        log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
           throw e;
        }
        return result;
    }

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1)
			 throw new IllegalArgumentException ("C_Invoice_ID is mandatory.");
		set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** C_PaymentMassive_ID AD_Reference_ID=1000013 */
	public static final int C_PAYMENTMASSIVE_ID_AD_Reference_ID=1000013;
	/** Set C_PaymentMassive_ID.
		@param C_PaymentMassive_ID C_PaymentMassive_ID	  */
	public void setC_PaymentMassive_ID (int C_PaymentMassive_ID)
	{
		if (C_PaymentMassive_ID < 1)
			 throw new IllegalArgumentException ("C_PaymentMassive_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_C_PaymentMassive_ID, Integer.valueOf(C_PaymentMassive_ID));
	}

	/** Get C_PaymentMassive_ID.
		@return C_PaymentMassive_ID	  */
	public int getC_PaymentMassive_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentMassive_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_PaymentMassiveLine_ID.
		@param C_PaymentMassiveLine_ID C_PaymentMassiveLine_ID	  */
	public void setC_PaymentMassiveLine_ID (int C_PaymentMassiveLine_ID)
	{
		if (C_PaymentMassiveLine_ID < 1)
			 throw new IllegalArgumentException ("C_PaymentMassiveLine_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_C_PaymentMassiveLine_ID, Integer.valueOf(C_PaymentMassiveLine_ID));
	}

	/** Get C_PaymentMassiveLine_ID.
		@return C_PaymentMassiveLine_ID	  */
	public int getC_PaymentMassiveLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentMassiveLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payment amount.
		@param PayAmt 
		Amount being paid
	  */
	public void setPayAmt (BigDecimal PayAmt)
	{
		if (PayAmt == null)
			throw new IllegalArgumentException ("PayAmt is mandatory.");
		set_Value (COLUMNNAME_PayAmt, PayAmt);
	}

	/** Get Payment amount.
		@return Amount being paid
	  */
	public BigDecimal getPayAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PayAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}