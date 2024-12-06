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

/** Generated Model for I_IMPORTINVOICEXMLWPLINE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_IMPORTINVOICEXMLWPLINE extends PO implements I_I_IMPORTINVOICEXMLWPLINE, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170220L;

    /** Standard Constructor */
    public X_I_IMPORTINVOICEXMLWPLINE (Properties ctx, int I_IMPORTINVOICEXMLWPLINE_ID, String trxName)
    {
      super (ctx, I_IMPORTINVOICEXMLWPLINE_ID, trxName);
      /** if (I_IMPORTINVOICEXMLWPLINE_ID == 0)
        {
			setI_IMPORTINVOICEXMLWPLINE_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_IMPORTINVOICEXMLWPLINE (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_IMPORTINVOICEXMLWPLINE[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set CLAS_FISCAL.
		@param CLAS_FISCAL CLAS_FISCAL	  */
	public void setCLAS_FISCAL (String CLAS_FISCAL)
	{
		set_Value (COLUMNNAME_CLAS_FISCAL, CLAS_FISCAL);
	}

	/** Get CLAS_FISCAL.
		@return CLAS_FISCAL	  */
	public String getCLAS_FISCAL () 
	{
		return (String)get_Value(COLUMNNAME_CLAS_FISCAL);
	}

	/** Set CODIGO_MP.
		@param CODIGO_MP CODIGO_MP	  */
	public void setCODIGO_MP (String CODIGO_MP)
	{
		set_Value (COLUMNNAME_CODIGO_MP, CODIGO_MP);
	}

	/** Get CODIGO_MP.
		@return CODIGO_MP	  */
	public String getCODIGO_MP () 
	{
		return (String)get_Value(COLUMNNAME_CODIGO_MP);
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

	/** Set I_IMPORTINVOICEXMLWPLINE.
		@param I_IMPORTINVOICEXMLWPLINE_ID I_IMPORTINVOICEXMLWPLINE	  */
	public void setI_IMPORTINVOICEXMLWPLINE_ID (int I_IMPORTINVOICEXMLWPLINE_ID)
	{
		if (I_IMPORTINVOICEXMLWPLINE_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_IMPORTINVOICEXMLWPLINE_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_IMPORTINVOICEXMLWPLINE_ID, Integer.valueOf(I_IMPORTINVOICEXMLWPLINE_ID));
	}

	/** Get I_IMPORTINVOICEXMLWPLINE.
		@return I_IMPORTINVOICEXMLWPLINE	  */
	public int getI_IMPORTINVOICEXMLWPLINE_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_IMPORTINVOICEXMLWPLINE_ID);
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

	/** Set IVA.
		@param IVA IVA	  */
	public void setIVA (BigDecimal IVA)
	{
		set_Value (COLUMNNAME_IVA, IVA);
	}

	/** Get IVA.
		@return IVA	  */
	public BigDecimal getIVA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_IVA);
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

	/** Set NUMERO_PEDIDO.
		@param NUMERO_PEDIDO NUMERO_PEDIDO	  */
	public void setNUMERO_PEDIDO (String NUMERO_PEDIDO)
	{
		set_Value (COLUMNNAME_NUMERO_PEDIDO, NUMERO_PEDIDO);
	}

	/** Get NUMERO_PEDIDO.
		@return NUMERO_PEDIDO	  */
	public String getNUMERO_PEDIDO () 
	{
		return (String)get_Value(COLUMNNAME_NUMERO_PEDIDO);
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

	/** Set VALOR_IVA.
		@param VALOR_IVA VALOR_IVA	  */
	public void setVALOR_IVA (BigDecimal VALOR_IVA)
	{
		set_Value (COLUMNNAME_VALOR_IVA, VALOR_IVA);
	}

	/** Get VALOR_IVA.
		@return VALOR_IVA	  */
	public BigDecimal getVALOR_IVA () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_VALOR_IVA);
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