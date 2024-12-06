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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for I_IMPORTINVOICEXMLWP
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_IMPORTINVOICEXMLWP extends PO implements I_I_IMPORTINVOICEXMLWP, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170220L;

    /** Standard Constructor */
    public X_I_IMPORTINVOICEXMLWP (Properties ctx, int I_IMPORTINVOICEXMLWP_ID, String trxName)
    {
      super (ctx, I_IMPORTINVOICEXMLWP_ID, trxName);
      /** if (I_IMPORTINVOICEXMLWP_ID == 0)
        {
			setI_IMPORTINVOICEXMLWP_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_IMPORTINVOICEXMLWP (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_I_IMPORTINVOICEXMLWP[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set BAIRRO.
		@param BAIRRO BAIRRO	  */
	public void setBAIRRO (String BAIRRO)
	{
		set_Value (COLUMNNAME_BAIRRO, BAIRRO);
	}

	/** Get BAIRRO.
		@return BAIRRO	  */
	public String getBAIRRO () 
	{
		return (String)get_Value(COLUMNNAME_BAIRRO);
	}

	/** Set CF_OP.
		@param CF_OP CF_OP	  */
	public void setCF_OP (BigDecimal CF_OP)
	{
		set_Value (COLUMNNAME_CF_OP, CF_OP);
	}

	/** Get CF_OP.
		@return CF_OP	  */
	public BigDecimal getCF_OP () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CF_OP);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CIDADE.
		@param CIDADE CIDADE	  */
	public void setCIDADE (String CIDADE)
	{
		set_Value (COLUMNNAME_CIDADE, CIDADE);
	}

	/** Get CIDADE.
		@return CIDADE	  */
	public String getCIDADE () 
	{
		return (String)get_Value(COLUMNNAME_CIDADE);
	}

	/** Set CNPJ.
		@param CNPJ CNPJ	  */
	public void setCNPJ (String CNPJ)
	{
		set_Value (COLUMNNAME_CNPJ, CNPJ);
	}

	/** Get CNPJ.
		@return CNPJ	  */
	public String getCNPJ () 
	{
		return (String)get_Value(COLUMNNAME_CNPJ);
	}

	/** Set DATA_IMPRESSA.
		@param DATA_IMPRESSA DATA_IMPRESSA	  */
	public void setDATA_IMPRESSA (Timestamp DATA_IMPRESSA)
	{
		set_Value (COLUMNNAME_DATA_IMPRESSA, DATA_IMPRESSA);
	}

	/** Get DATA_IMPRESSA.
		@return DATA_IMPRESSA	  */
	public Timestamp getDATA_IMPRESSA () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DATA_IMPRESSA);
	}

	/** Set DATA_STATUS.
		@param DATA_STATUS DATA_STATUS	  */
	public void setDATA_STATUS (Timestamp DATA_STATUS)
	{
		set_Value (COLUMNNAME_DATA_STATUS, DATA_STATUS);
	}

	/** Get DATA_STATUS.
		@return DATA_STATUS	  */
	public Timestamp getDATA_STATUS () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DATA_STATUS);
	}

	/** Set DATTA_PERIODO_CONTABIL.
		@param DATTA_PERIODO_CONTABIL DATTA_PERIODO_CONTABIL	  */
	public void setDATTA_PERIODO_CONTABIL (Timestamp DATTA_PERIODO_CONTABIL)
	{
		set_Value (COLUMNNAME_DATTA_PERIODO_CONTABIL, DATTA_PERIODO_CONTABIL);
	}

	/** Get DATTA_PERIODO_CONTABIL.
		@return DATTA_PERIODO_CONTABIL	  */
	public Timestamp getDATTA_PERIODO_CONTABIL () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DATTA_PERIODO_CONTABIL);
	}

	/** Set DT_EMISSAO.
		@param DT_EMISSAO DT_EMISSAO	  */
	public void setDT_EMISSAO (Timestamp DT_EMISSAO)
	{
		set_Value (COLUMNNAME_DT_EMISSAO, DT_EMISSAO);
	}

	/** Get DT_EMISSAO.
		@return DT_EMISSAO	  */
	public Timestamp getDT_EMISSAO () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DT_EMISSAO);
	}

	/** Set ENCARGO.
		@param ENCARGO ENCARGO	  */
	public void setENCARGO (BigDecimal ENCARGO)
	{
		set_Value (COLUMNNAME_ENCARGO, ENCARGO);
	}

	/** Get ENCARGO.
		@return ENCARGO	  */
	public BigDecimal getENCARGO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ENCARGO);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ENDERECO.
		@param ENDERECO ENDERECO	  */
	public void setENDERECO (String ENDERECO)
	{
		set_Value (COLUMNNAME_ENDERECO, ENDERECO);
	}

	/** Get ENDERECO.
		@return ENDERECO	  */
	public String getENDERECO () 
	{
		return (String)get_Value(COLUMNNAME_ENDERECO);
	}

	/** Set ENTRADA_SAIDA.
		@param ENTRADA_SAIDA ENTRADA_SAIDA	  */
	public void setENTRADA_SAIDA (String ENTRADA_SAIDA)
	{
		set_Value (COLUMNNAME_ENTRADA_SAIDA, ENTRADA_SAIDA);
	}

	/** Get ENTRADA_SAIDA.
		@return ENTRADA_SAIDA	  */
	public String getENTRADA_SAIDA () 
	{
		return (String)get_Value(COLUMNNAME_ENTRADA_SAIDA);
	}

	/** Set FINALIDADE_NFE.
		@param FINALIDADE_NFE FINALIDADE_NFE	  */
	public void setFINALIDADE_NFE (String FINALIDADE_NFE)
	{
		set_Value (COLUMNNAME_FINALIDADE_NFE, FINALIDADE_NFE);
	}

	/** Get FINALIDADE_NFE.
		@return FINALIDADE_NFE	  */
	public String getFINALIDADE_NFE () 
	{
		return (String)get_Value(COLUMNNAME_FINALIDADE_NFE);
	}

	/** Set FONE_FAX.
		@param FONE_FAX FONE_FAX	  */
	public void setFONE_FAX (String FONE_FAX)
	{
		set_Value (COLUMNNAME_FONE_FAX, FONE_FAX);
	}

	/** Get FONE_FAX.
		@return FONE_FAX	  */
	public String getFONE_FAX () 
	{
		return (String)get_Value(COLUMNNAME_FONE_FAX);
	}

	/** Set FORMA_PAGAMENTO.
		@param FORMA_PAGAMENTO FORMA_PAGAMENTO	  */
	public void setFORMA_PAGAMENTO (BigDecimal FORMA_PAGAMENTO)
	{
		set_Value (COLUMNNAME_FORMA_PAGAMENTO, FORMA_PAGAMENTO);
	}

	/** Get FORMA_PAGAMENTO.
		@return FORMA_PAGAMENTO	  */
	public BigDecimal getFORMA_PAGAMENTO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_FORMA_PAGAMENTO);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HORA_SAIDA.
		@param HORA_SAIDA HORA_SAIDA	  */
	public void setHORA_SAIDA (Timestamp HORA_SAIDA)
	{
		set_Value (COLUMNNAME_HORA_SAIDA, HORA_SAIDA);
	}

	/** Get HORA_SAIDA.
		@return HORA_SAIDA	  */
	public Timestamp getHORA_SAIDA () 
	{
		return (Timestamp)get_Value(COLUMNNAME_HORA_SAIDA);
	}

	/** Set I_IMPORTINVOICEXMLWP.
		@param I_IMPORTINVOICEXMLWP_ID I_IMPORTINVOICEXMLWP	  */
	public void setI_IMPORTINVOICEXMLWP_ID (int I_IMPORTINVOICEXMLWP_ID)
	{
		if (I_IMPORTINVOICEXMLWP_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_IMPORTINVOICEXMLWP_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_IMPORTINVOICEXMLWP_ID, Integer.valueOf(I_IMPORTINVOICEXMLWP_ID));
	}

	/** Get I_IMPORTINVOICEXMLWP.
		@return I_IMPORTINVOICEXMLWP	  */
	public int getI_IMPORTINVOICEXMLWP_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_IMPORTINVOICEXMLWP_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set INSCRICAO_ESTADUAL.
		@param INSCRICAO_ESTADUAL INSCRICAO_ESTADUAL	  */
	public void setINSCRICAO_ESTADUAL (String INSCRICAO_ESTADUAL)
	{
		set_Value (COLUMNNAME_INSCRICAO_ESTADUAL, INSCRICAO_ESTADUAL);
	}

	/** Get INSCRICAO_ESTADUAL.
		@return INSCRICAO_ESTADUAL	  */
	public String getINSCRICAO_ESTADUAL () 
	{
		return (String)get_Value(COLUMNNAME_INSCRICAO_ESTADUAL);
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

	/** Set MOEDA.
		@param MOEDA MOEDA	  */
	public void setMOEDA (String MOEDA)
	{
		set_Value (COLUMNNAME_MOEDA, MOEDA);
	}

	/** Get MOEDA.
		@return MOEDA	  */
	public String getMOEDA () 
	{
		return (String)get_Value(COLUMNNAME_MOEDA);
	}

	/** Set NAT_OP.
		@param NAT_OP NAT_OP	  */
	public void setNAT_OP (String NAT_OP)
	{
		set_Value (COLUMNNAME_NAT_OP, NAT_OP);
	}

	/** Get NAT_OP.
		@return NAT_OP	  */
	public String getNAT_OP () 
	{
		return (String)get_Value(COLUMNNAME_NAT_OP);
	}

	/** Set NOME.
		@param NOME NOME	  */
	public void setNOME (String NOME)
	{
		set_Value (COLUMNNAME_NOME, NOME);
	}

	/** Get NOME.
		@return NOME	  */
	public String getNOME () 
	{
		return (String)get_Value(COLUMNNAME_NOME);
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

	/** Set OBS.
		@param OBS OBS	  */
	public void setOBS (String OBS)
	{
		set_Value (COLUMNNAME_OBS, OBS);
	}

	/** Get OBS.
		@return OBS	  */
	public String getOBS () 
	{
		return (String)get_Value(COLUMNNAME_OBS);
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

	/** Set TERMINOPAGO.
		@param TERMINOPAGO TERMINOPAGO	  */
	public void setTERMINOPAGO (BigDecimal TERMINOPAGO)
	{
		set_Value (COLUMNNAME_TERMINOPAGO, TERMINOPAGO);
	}

	/** Get TERMINOPAGO.
		@return TERMINOPAGO	  */
	public BigDecimal getTERMINOPAGO () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TERMINOPAGO);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TOTAL_NF.
		@param TOTAL_NF TOTAL_NF	  */
	public void setTOTAL_NF (BigDecimal TOTAL_NF)
	{
		set_Value (COLUMNNAME_TOTAL_NF, TOTAL_NF);
	}

	/** Get TOTAL_NF.
		@return TOTAL_NF	  */
	public BigDecimal getTOTAL_NF () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TOTAL_NF);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TOTAL_PRODUTOS.
		@param TOTAL_PRODUTOS TOTAL_PRODUTOS	  */
	public void setTOTAL_PRODUTOS (BigDecimal TOTAL_PRODUTOS)
	{
		set_Value (COLUMNNAME_TOTAL_PRODUTOS, TOTAL_PRODUTOS);
	}

	/** Get TOTAL_PRODUTOS.
		@return TOTAL_PRODUTOS	  */
	public BigDecimal getTOTAL_PRODUTOS () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TOTAL_PRODUTOS);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set UF.
		@param UF UF	  */
	public void setUF (String UF)
	{
		set_Value (COLUMNNAME_UF, UF);
	}

	/** Get UF.
		@return UF	  */
	public String getUF () 
	{
		return (String)get_Value(COLUMNNAME_UF);
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
}