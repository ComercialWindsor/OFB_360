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

/** Generated Interface for I_IMPORTINVOICEXMLWLINE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_I_IMPORTINVOICEXMLWLINE 
{

    /** TableName=I_IMPORTINVOICEXMLWLINE */
    public static final String Table_Name = "I_IMPORTINVOICEXMLWLINE";

    /** AD_Table_ID=1000209 */
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

    /** Column name CODIGO_PRODUTO */
    public static final String COLUMNNAME_CODIGO_PRODUTO = "CODIGO_PRODUTO";

	/** Set CODIGO_PRODUTO	  */
	public void setCODIGO_PRODUTO (String CODIGO_PRODUTO);

	/** Get CODIGO_PRODUTO	  */
	public String getCODIGO_PRODUTO();

    /** Column name C_OrderLine_ID */
    public static final String COLUMNNAME_C_OrderLine_ID = "C_OrderLine_ID";

	/** Set Sales Order Line.
	  * Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID);

	/** Get Sales Order Line.
	  * Sales Order Line
	  */
	public int getC_OrderLine_ID();

	public I_C_OrderLine getC_OrderLine() throws RuntimeException;

    /** Column name COSTO_XML */
    public static final String COLUMNNAME_COSTO_XML = "COSTO_XML";

	/** Set COSTO_XML.
	  * Cost information
	  */
	public void setCOSTO_XML (BigDecimal COSTO_XML);

	/** Get COSTO_XML.
	  * Cost information
	  */
	public BigDecimal getCOSTO_XML();

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

    /** Column name DESCRICAO */
    public static final String COLUMNNAME_DESCRICAO = "DESCRICAO";

	/** Set DESCRICAO	  */
	public void setDESCRICAO (String DESCRICAO);

	/** Get DESCRICAO	  */
	public String getDESCRICAO();

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

    /** Column name I_IMPORTINVOICEXMLWLINE_ID */
    public static final String COLUMNNAME_I_IMPORTINVOICEXMLWLINE_ID = "I_IMPORTINVOICEXMLWLINE_ID";

	/** Set I_IMPORTINVOICEXMLWLINE	  */
	public void setI_IMPORTINVOICEXMLWLINE_ID (int I_IMPORTINVOICEXMLWLINE_ID);

	/** Get I_IMPORTINVOICEXMLWLINE	  */
	public int getI_IMPORTINVOICEXMLWLINE_ID();

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

    /** Column name IVA_CHILE */
    public static final String COLUMNNAME_IVA_CHILE = "IVA_CHILE";

	/** Set IVA_CHILE	  */
	public void setIVA_CHILE (BigDecimal IVA_CHILE);

	/** Get IVA_CHILE	  */
	public BigDecimal getIVA_CHILE();

    /** Column name NUMERO_NF */
    public static final String COLUMNNAME_NUMERO_NF = "NUMERO_NF";

	/** Set NUMERO_NF	  */
	public void setNUMERO_NF (String NUMERO_NF);

	/** Get NUMERO_NF	  */
	public String getNUMERO_NF();

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

    /** Column name QTDE */
    public static final String COLUMNNAME_QTDE = "QTDE";

	/** Set QTDE	  */
	public void setQTDE (BigDecimal QTDE);

	/** Get QTDE	  */
	public BigDecimal getQTDE();

    /** Column name UNIDADE */
    public static final String COLUMNNAME_UNIDADE = "UNIDADE";

	/** Set UNIDADE	  */
	public void setUNIDADE (String UNIDADE);

	/** Get UNIDADE	  */
	public String getUNIDADE();

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

    /** Column name VALOR_IVA_CHILE */
    public static final String COLUMNNAME_VALOR_IVA_CHILE = "VALOR_IVA_CHILE";

	/** Set VALOR_IVA_CHILE	  */
	public void setVALOR_IVA_CHILE (BigDecimal VALOR_IVA_CHILE);

	/** Get VALOR_IVA_CHILE	  */
	public BigDecimal getVALOR_IVA_CHILE();

    /** Column name VALOR_TOTAL */
    public static final String COLUMNNAME_VALOR_TOTAL = "VALOR_TOTAL";

	/** Set VALOR_TOTAL	  */
	public void setVALOR_TOTAL (BigDecimal VALOR_TOTAL);

	/** Get VALOR_TOTAL	  */
	public BigDecimal getVALOR_TOTAL();

    /** Column name VALOR_UNITARIO */
    public static final String COLUMNNAME_VALOR_UNITARIO = "VALOR_UNITARIO";

	/** Set VALOR_UNITARIO	  */
	public void setVALOR_UNITARIO (BigDecimal VALOR_UNITARIO);

	/** Get VALOR_UNITARIO	  */
	public BigDecimal getVALOR_UNITARIO();
}
