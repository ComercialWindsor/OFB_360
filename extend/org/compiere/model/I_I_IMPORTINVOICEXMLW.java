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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for I_IMPORTINVOICEXMLW
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_I_IMPORTINVOICEXMLW 
{

    /** TableName=I_IMPORTINVOICEXMLW */
    public static final String Table_Name = "I_IMPORTINVOICEXMLW";

    /** AD_Table_ID=1000208 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name CF_OP */
    public static final String COLUMNNAME_CF_OP = "CF_OP";

	/** Set CF_OP	  */
	public void setCF_OP (BigDecimal CF_OP);

	/** Get CF_OP	  */
	public BigDecimal getCF_OP();

    /** Column name CIDADE */
    public static final String COLUMNNAME_CIDADE = "CIDADE";

	/** Set CIDADE	  */
	public void setCIDADE (String CIDADE);

	/** Get CIDADE	  */
	public String getCIDADE();

    /** Column name CNPJ */
    public static final String COLUMNNAME_CNPJ = "CNPJ";

	/** Set CNPJ	  */
	public void setCNPJ (String CNPJ);

	/** Get CNPJ	  */
	public String getCNPJ();

    /** Column name CODIGO_CLIENTE */
    public static final String COLUMNNAME_CODIGO_CLIENTE = "CODIGO_CLIENTE";

	/** Set CODIGO_CLIENTE	  */
	public void setCODIGO_CLIENTE (String CODIGO_CLIENTE);

	/** Get CODIGO_CLIENTE	  */
	public String getCODIGO_CLIENTE();

    /** Column name C_Order_ID */
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";

	/** Set Order.
	  * Order
	  */
	public void setC_Order_ID (int C_Order_ID);

	/** Get Order.
	  * Order
	  */
	public int getC_Order_ID();

	public I_C_Order getC_Order() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DATA_IMPRESSA */
    public static final String COLUMNNAME_DATA_IMPRESSA = "DATA_IMPRESSA";

	/** Set DATA_IMPRESSA	  */
	public void setDATA_IMPRESSA (Timestamp DATA_IMPRESSA);

	/** Get DATA_IMPRESSA	  */
	public Timestamp getDATA_IMPRESSA();

    /** Column name DT_EMISSAO */
    public static final String COLUMNNAME_DT_EMISSAO = "DT_EMISSAO";

	/** Set DT_EMISSAO	  */
	public void setDT_EMISSAO (Timestamp DT_EMISSAO);

	/** Get DT_EMISSAO	  */
	public Timestamp getDT_EMISSAO();

    /** Column name ENCARGO */
    public static final String COLUMNNAME_ENCARGO = "ENCARGO";

	/** Set ENCARGO	  */
	public void setENCARGO (BigDecimal ENCARGO);

	/** Get ENCARGO	  */
	public BigDecimal getENCARGO();

    /** Column name ENTRADA_SAIDA */
    public static final String COLUMNNAME_ENTRADA_SAIDA = "ENTRADA_SAIDA";

	/** Set ENTRADA_SAIDA	  */
	public void setENTRADA_SAIDA (String ENTRADA_SAIDA);

	/** Get ENTRADA_SAIDA	  */
	public String getENTRADA_SAIDA();

    /** Column name FINALIDADE_NFE */
    public static final String COLUMNNAME_FINALIDADE_NFE = "FINALIDADE_NFE";

	/** Set FINALIDADE_NFE	  */
	public void setFINALIDADE_NFE (String FINALIDADE_NFE);

	/** Get FINALIDADE_NFE	  */
	public String getFINALIDADE_NFE();

    /** Column name FONE_FAX */
    public static final String COLUMNNAME_FONE_FAX = "FONE_FAX";

	/** Set FONE_FAX	  */
	public void setFONE_FAX (String FONE_FAX);

	/** Get FONE_FAX	  */
	public String getFONE_FAX();

    /** Column name FORMA_PAGAMENTO */
    public static final String COLUMNNAME_FORMA_PAGAMENTO = "FORMA_PAGAMENTO";

	/** Set FORMA_PAGAMENTO	  */
	public void setFORMA_PAGAMENTO (BigDecimal FORMA_PAGAMENTO);

	/** Get FORMA_PAGAMENTO	  */
	public BigDecimal getFORMA_PAGAMENTO();

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String Help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

    /** Column name HORA_SAIDA */
    public static final String COLUMNNAME_HORA_SAIDA = "HORA_SAIDA";

	/** Set HORA_SAIDA	  */
	public void setHORA_SAIDA (Timestamp HORA_SAIDA);

	/** Get HORA_SAIDA	  */
	public Timestamp getHORA_SAIDA();

    /** Column name I_IMPORTINVOICEXMLW_ID */
    public static final String COLUMNNAME_I_IMPORTINVOICEXMLW_ID = "I_IMPORTINVOICEXMLW_ID";

	/** Set I_IMPORTINVOICEXMLW	  */
	public void setI_IMPORTINVOICEXMLW_ID (int I_IMPORTINVOICEXMLW_ID);

	/** Get I_IMPORTINVOICEXMLW	  */
	public int getI_IMPORTINVOICEXMLW_ID();

    /** Column name INSCRICAO_ESTADUAL */
    public static final String COLUMNNAME_INSCRICAO_ESTADUAL = "INSCRICAO_ESTADUAL";

	/** Set INSCRICAO_ESTADUAL	  */
	public void setINSCRICAO_ESTADUAL (String INSCRICAO_ESTADUAL);

	/** Get INSCRICAO_ESTADUAL	  */
	public String getINSCRICAO_ESTADUAL();

    /** Column name IsAbort */
    public static final String COLUMNNAME_IsAbort = "IsAbort";

	/** Set Abort Process.
	  * Aborts the current process
	  */
	public void setIsAbort (boolean IsAbort);

	/** Get Abort Process.
	  * Aborts the current process
	  */
	public boolean isAbort();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name MOEDA */
    public static final String COLUMNNAME_MOEDA = "MOEDA";

	/** Set MOEDA	  */
	public void setMOEDA (String MOEDA);

	/** Get MOEDA	  */
	public String getMOEDA();

    /** Column name NAT_OP */
    public static final String COLUMNNAME_NAT_OP = "NAT_OP";

	/** Set NAT_OP	  */
	public void setNAT_OP (String NAT_OP);

	/** Get NAT_OP	  */
	public String getNAT_OP();

    /** Column name NOME */
    public static final String COLUMNNAME_NOME = "NOME";

	/** Set NOME	  */
	public void setNOME (String NOME);

	/** Get NOME	  */
	public String getNOME();

    /** Column name NUMERO_NF */
    public static final String COLUMNNAME_NUMERO_NF = "NUMERO_NF";

	/** Set NUMERO_NF	  */
	public void setNUMERO_NF (String NUMERO_NF);

	/** Get NUMERO_NF	  */
	public String getNUMERO_NF();

    /** Column name NUMERO_PEDIDO */
    public static final String COLUMNNAME_NUMERO_PEDIDO = "NUMERO_PEDIDO";

	/** Set NUMERO_PEDIDO	  */
	public void setNUMERO_PEDIDO (String NUMERO_PEDIDO);

	/** Get NUMERO_PEDIDO	  */
	public String getNUMERO_PEDIDO();

    /** Column name OBS */
    public static final String COLUMNNAME_OBS = "OBS";

	/** Set OBS	  */
	public void setOBS (String OBS);

	/** Get OBS	  */
	public String getOBS();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Status */
    public static final String COLUMNNAME_Status = "Status";

	/** Set Status.
	  * Status of the currently running check
	  */
	public void setStatus (String Status);

	/** Get Status.
	  * Status of the currently running check
	  */
	public String getStatus();

    /** Column name TERMINO_PAGO */
    public static final String COLUMNNAME_TERMINO_PAGO = "TERMINO_PAGO";

	/** Set TERMINO_PAGO	  */
	public void setTERMINO_PAGO (BigDecimal TERMINO_PAGO);

	/** Get TERMINO_PAGO	  */
	public BigDecimal getTERMINO_PAGO();

    /** Column name TERMINOPAGO */
    public static final String COLUMNNAME_TERMINOPAGO = "TERMINOPAGO";

	/** Set TERMINOPAGO	  */
	public void setTERMINOPAGO (String TERMINOPAGO);

	/** Get TERMINOPAGO	  */
	public String getTERMINOPAGO();

    /** Column name TOTAL_IVA_CHILE */
    public static final String COLUMNNAME_TOTAL_IVA_CHILE = "TOTAL_IVA_CHILE";

	/** Set TOTAL_IVA_CHILE	  */
	public void setTOTAL_IVA_CHILE (BigDecimal TOTAL_IVA_CHILE);

	/** Get TOTAL_IVA_CHILE	  */
	public BigDecimal getTOTAL_IVA_CHILE();

    /** Column name TOTAL_NF */
    public static final String COLUMNNAME_TOTAL_NF = "TOTAL_NF";

	/** Set TOTAL_NF	  */
	public void setTOTAL_NF (BigDecimal TOTAL_NF);

	/** Get TOTAL_NF	  */
	public BigDecimal getTOTAL_NF();

    /** Column name TOTAL_PRODUTOS */
    public static final String COLUMNNAME_TOTAL_PRODUTOS = "TOTAL_PRODUTOS";

	/** Set TOTAL_PRODUTOS	  */
	public void setTOTAL_PRODUTOS (BigDecimal TOTAL_PRODUTOS);

	/** Get TOTAL_PRODUTOS	  */
	public BigDecimal getTOTAL_PRODUTOS();

    /** Column name UF */
    public static final String COLUMNNAME_UF = "UF";

	/** Set UF	  */
	public void setUF (String UF);

	/** Get UF	  */
	public String getUF();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name VENDEDOR */
    public static final String COLUMNNAME_VENDEDOR = "VENDEDOR";

	/** Set VENDEDOR	  */
	public void setVENDEDOR (String VENDEDOR);

	/** Get VENDEDOR	  */
	public String getVENDEDOR();
}
