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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for I_IMPORTINVOICEXMLWLINE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_IMPORTINVOICEXMLWLINE extends PO implements I_I_IMPORTINVOICEXMLWLINE, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170220L;

    /** Standard Constructor */
    public X_I_IMPORTINVOICEXMLWLINE (Properties ctx, int I_IMPORTINVOICEXMLWLINE_ID, String trxName)
    {
      super (ctx, I_IMPORTINVOICEXMLWLINE_ID, trxName);
      /** if (I_IMPORTINVOICEXMLWLINE_ID == 0)
        {
			setI_IMPORTINVOICEXMLWLINE_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_IMPORTINVOICEXMLWLINE (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_IMPORTINVOICEXMLWLINE[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set CODIGO_PRODUTO.
		@param CODIGO_PRODUTO CODIGO_PRODUTO	  */
	public void setCODIGO_PRODUTO (String CODIGO_PRODUTO)
	{
		set_Value (COLUMNNAME_CODIGO_PRODUTO, CODIGO_PRODUTO);
	}

	/** Get CODIGO_PRODUTO.
		@return CODIGO_PRODUTO	  */
	public String getCODIGO_PRODUTO () 
	{
		return (String)get_Value(COLUMNNAME_CODIGO_PRODUTO);
	}

	public I_C_OrderLine getC_OrderLine() throws RuntimeException
    {
		return (I_C_OrderLine)MTable.get(getCtx(), I_C_OrderLine.Table_Name)
			.getPO(getC_OrderLine_ID(), get_TrxName());	}

	/** Set Sales Order Line.
		@param C_OrderLine_ID 
		Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID)
	{
		if (C_OrderLine_ID < 1) 
			set_Value (COLUMNNAME_C_OrderLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_OrderLine_ID, Integer.valueOf(C_OrderLine_ID));
	}

	/** Get Sales Order Line.
		@return Sales Order Line
	  */
	public int getC_OrderLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set COSTO_XML.
		@param COSTO_XML 
		Cost information
	  */
	public void setCOSTO_XML (BigDecimal COSTO_XML)
	{
		set_Value (COLUMNNAME_COSTO_XML, COSTO_XML);
	}

	/** Get COSTO_XML.
		@return Cost information
	  */
	public BigDecimal getCOSTO_XML () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_COSTO_XML);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DESCRICAO.
		@param DESCRICAO DESCRICAO	  */
	public void setDESCRICAO (String DESCRICAO)
	{
		set_Value (COLUMNNAME_DESCRICAO, DESCRICAO);
	}

	/** Get DESCRICAO.
		@return DESCRICAO	  */
	public String getDESCRICAO () 
	{
		return (String)get_Value(COLUMNNAME_DESCRICAO);
	}

	/** Set Comment/Help.
		@param Help 
		Comment or Hint
	  */
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp () 
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	/** Set I_IMPORTINVOICEXMLWLINE.
		@param I_IMPORTINVOICEXMLWLINE_ID I_IMPORTINVOICEXMLWLINE	  */
	public void setI_IMPORTINVOICEXMLWLINE_ID (int I_IMPORTINVOICEXMLWLINE_ID)
	{
		if (I_IMPORTINVOICEXMLWLINE_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_IMPORTINVOICEXMLWLINE_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_IMPORTINVOICEXMLWLINE_ID, Integer.valueOf(I_IMPORTINVOICEXMLWLINE_ID));
	}

	/** Get I_IMPORTINVOICEXMLWLINE.
		@return I_IMPORTINVOICEXMLWLINE	  */
	public int getI_IMPORTINVOICEXMLWLINE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_IMPORTINVOICEXMLWLINE_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Abort Process.
		@param IsAbort 
		Aborts the current process
	  */
	public void setIsAbort (boolean IsAbort)
	{
		set_Value (COLUMNNAME_IsAbort, Boolean.valueOf(IsAbort));
	}

	/** Get Abort Process.
		@return Aborts the current process
	  */
	public boolean isAbort () 
	{
		Object oo = get_Value(COLUMNNAME_IsAbort);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set IVA_CHILE.
		@param IVA_CHILE IVA_CHILE	  */
	public void setIVA_CHILE (BigDecimal IVA_CHILE)
	{
		set_Value (COLUMNNAME_IVA_CHILE, IVA_CHILE);
	}

	/** Get IVA_CHILE.
		@return IVA_CHILE	  */
	public BigDecimal getIVA_CHILE () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_IVA_CHILE);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set NUMERO_NF.
		@param NUMERO_NF NUMERO_NF	  */
	public void setNUMERO_NF (String NUMERO_NF)
	{
		set_Value (COLUMNNAME_NUMERO_NF, NUMERO_NF);
	}

	/** Get NUMERO_NF.
		@return NUMERO_NF	  */
	public String getNUMERO_NF () 
	{
		return (String)get_Value(COLUMNNAME_NUMERO_NF);
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

	/** Set QTDE.
		@param QTDE QTDE	  */
	public void setQTDE (BigDecimal QTDE)
	{
		set_Value (COLUMNNAME_QTDE, QTDE);
	}

	/** Get QTDE.
		@return QTDE	  */
	public BigDecimal getQTDE () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QTDE);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set UNIDADE.
		@param UNIDADE UNIDADE	  */
	public void setUNIDADE (String UNIDADE)
	{
		set_Value (COLUMNNAME_UNIDADE, UNIDADE);
	}

	/** Get UNIDADE.
		@return UNIDADE	  */
	public String getUNIDADE () 
	{
		return (String)get_Value(COLUMNNAME_UNIDADE);
	}

	/** Set VALOR_IVA_CHILE.
		@param VALOR_IVA_CHILE VALOR_IVA_CHILE	  */
	public void setVALOR_IVA_CHILE (BigDecimal VALOR_IVA_CHILE)
	{
		set_Value (COLUMNNAME_VALOR_IVA_CHILE, VALOR_IVA_CHILE);
	}

	/** Get VALOR_IVA_CHILE.
		@return VALOR_IVA_CHILE	  */
	public BigDecimal getVALOR_IVA_CHILE () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VALOR_IVA_CHILE);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VALOR_TOTAL.
		@param VALOR_TOTAL VALOR_TOTAL	  */
	public void setVALOR_TOTAL (BigDecimal VALOR_TOTAL)
	{
		set_Value (COLUMNNAME_VALOR_TOTAL, VALOR_TOTAL);
	}

	/** Get VALOR_TOTAL.
		@return VALOR_TOTAL	  */
	public BigDecimal getVALOR_TOTAL () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VALOR_TOTAL);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set VALOR_UNITARIO.
		@param VALOR_UNITARIO VALOR_UNITARIO	  */
	public void setVALOR_UNITARIO (BigDecimal VALOR_UNITARIO)
	{
		set_Value (COLUMNNAME_VALOR_UNITARIO, VALOR_UNITARIO);
	}

	/** Get VALOR_UNITARIO.
		@return VALOR_UNITARIO	  */
	public BigDecimal getVALOR_UNITARIO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VALOR_UNITARIO);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}