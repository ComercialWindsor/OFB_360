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

/** Generated Interface for T_BL_CTACTESALIDA
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_CTACTESALIDA 
{

    /** TableName=T_BL_CTACTESALIDA */
    public static final String Table_Name = "T_BL_CTACTESALIDA";

    /** AD_Table_ID=1000218 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name ABIERTA */
    public static final String COLUMNNAME_ABIERTA = "ABIERTA";

	/** Set ABIERTA	  */
	public void setABIERTA (boolean ABIERTA);

	/** Get ABIERTA	  */
	public boolean isABIERTA();

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

    /** Column name AD_Table_ID */
    public static final String COLUMNNAME_AD_Table_ID = "AD_Table_ID";

	/** Set Table.
	  * Database Table information
	  */
	public void setAD_Table_ID (int AD_Table_ID);

	/** Get Table.
	  * Database Table information
	  */
	public int getAD_Table_ID();

	public I_AD_Table getAD_Table() throws RuntimeException;

    /** Column name AmtAcctCr */
    public static final String COLUMNNAME_AmtAcctCr = "AmtAcctCr";

	/** Set Accounted Credit.
	  * Accounted Credit Amount
	  */
	public void setAmtAcctCr (BigDecimal AmtAcctCr);

	/** Get Accounted Credit.
	  * Accounted Credit Amount
	  */
	public BigDecimal getAmtAcctCr();

    /** Column name AmtAcctDr */
    public static final String COLUMNNAME_AmtAcctDr = "AmtAcctDr";

	/** Set Accounted Debit.
	  * Accounted Debit Amount
	  */
	public void setAmtAcctDr (BigDecimal AmtAcctDr);

	/** Get Accounted Debit.
	  * Accounted Debit Amount
	  */
	public BigDecimal getAmtAcctDr();

    /** Column name AmtSourceCr */
    public static final String COLUMNNAME_AmtSourceCr = "AmtSourceCr";

	/** Set Source Credit.
	  * Source Credit Amount
	  */
	public void setAmtSourceCr (BigDecimal AmtSourceCr);

	/** Get Source Credit.
	  * Source Credit Amount
	  */
	public BigDecimal getAmtSourceCr();

    /** Column name AmtSourceDr */
    public static final String COLUMNNAME_AmtSourceDr = "AmtSourceDr";

	/** Set Source Debit.
	  * Source Debit Amount
	  */
	public void setAmtSourceDr (BigDecimal AmtSourceDr);

	/** Get Source Debit.
	  * Source Debit Amount
	  */
	public BigDecimal getAmtSourceDr();

    /** Column name C_AcctSchema_ID */
    public static final String COLUMNNAME_C_AcctSchema_ID = "C_AcctSchema_ID";

	/** Set Accounting Schema.
	  * Rules for accounting
	  */
	public void setC_AcctSchema_ID (int C_AcctSchema_ID);

	/** Get Accounting Schema.
	  * Rules for accounting
	  */
	public int getC_AcctSchema_ID();

	public I_C_AcctSchema getC_AcctSchema() throws RuntimeException;

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name C_ElementValue_ID */
    public static final String COLUMNNAME_C_ElementValue_ID = "C_ElementValue_ID";

	/** Set Account Element.
	  * Account Element
	  */
	public void setC_ElementValue_ID (int C_ElementValue_ID);

	/** Get Account Element.
	  * Account Element
	  */
	public int getC_ElementValue_ID();

	public I_C_ElementValue getC_ElementValue() throws RuntimeException;

    /** Column name CLIENTNOMBRE */
    public static final String COLUMNNAME_CLIENTNOMBRE = "CLIENTNOMBRE";

	/** Set CLIENTNOMBRE	  */
	public void setCLIENTNOMBRE (String CLIENTNOMBRE);

	/** Get CLIENTNOMBRE	  */
	public String getCLIENTNOMBRE();

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

    /** Column name Correlativo */
    public static final String COLUMNNAME_Correlativo = "Correlativo";

	/** Set Correlativo	  */
	public void setCorrelativo (int Correlativo);

	/** Get Correlativo	  */
	public int getCorrelativo();

    /** Column name C_Period_ID */
    public static final String COLUMNNAME_C_Period_ID = "C_Period_ID";

	/** Set Period.
	  * Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID);

	/** Get Period.
	  * Period of the Calendar
	  */
	public int getC_Period_ID();

	public I_C_Period getC_Period() throws RuntimeException;

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

    /** Column name DateAcct */
    public static final String COLUMNNAME_DateAcct = "DateAcct";

	/** Set Account Date.
	  * Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct);

	/** Get Account Date.
	  * Accounting Date
	  */
	public Timestamp getDateAcct();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name DESDE */
    public static final String COLUMNNAME_DESDE = "DESDE";

	/** Set DESDE	  */
	public void setDESDE (Timestamp DESDE);

	/** Get DESDE	  */
	public Timestamp getDESDE();

    /** Column name DES_TABLA */
    public static final String COLUMNNAME_DES_TABLA = "DES_TABLA";

	/** Set DES_TABLA	  */
	public void setDES_TABLA (String DES_TABLA);

	/** Get DES_TABLA	  */
	public String getDES_TABLA();

    /** Column name DIGITO */
    public static final String COLUMNNAME_DIGITO = "DIGITO";

	/** Set DIGITO	  */
	public void setDIGITO (String DIGITO);

	/** Get DIGITO	  */
	public String getDIGITO();

    /** Column name FECHA_DCTO */
    public static final String COLUMNNAME_FECHA_DCTO = "FECHA_DCTO";

	/** Set FECHA_DCTO	  */
	public void setFECHA_DCTO (Timestamp FECHA_DCTO);

	/** Get FECHA_DCTO	  */
	public Timestamp getFECHA_DCTO();

    /** Column name FECHA_VCTO */
    public static final String COLUMNNAME_FECHA_VCTO = "FECHA_VCTO";

	/** Set FECHA_VCTO	  */
	public void setFECHA_VCTO (Timestamp FECHA_VCTO);

	/** Get FECHA_VCTO	  */
	public Timestamp getFECHA_VCTO();

    /** Column name HASTA */
    public static final String COLUMNNAME_HASTA = "HASTA";

	/** Set HASTA	  */
	public void setHASTA (Timestamp HASTA);

	/** Get HASTA	  */
	public Timestamp getHASTA();

    /** Column name INFO_FACTURA */
    public static final String COLUMNNAME_INFO_FACTURA = "INFO_FACTURA";

	/** Set INFO_FACTURA	  */
	public void setINFO_FACTURA (String INFO_FACTURA);

	/** Get INFO_FACTURA	  */
	public String getINFO_FACTURA();

    /** Column name ISO_Code */
    public static final String COLUMNNAME_ISO_Code = "ISO_Code";

	/** Set ISO Currency Code.
	  * Three letter ISO 4217 Code of the Currency
	  */
	public void setISO_Code (String ISO_Code);

	/** Get ISO Currency Code.
	  * Three letter ISO 4217 Code of the Currency
	  */
	public String getISO_Code();

    /** Column name MONEDAREFERENCIA */
    public static final String COLUMNNAME_MONEDAREFERENCIA = "MONEDAREFERENCIA";

	/** Set MONEDAREFERENCIA	  */
	public void setMONEDAREFERENCIA (BigDecimal MONEDAREFERENCIA);

	/** Get MONEDAREFERENCIA	  */
	public BigDecimal getMONEDAREFERENCIA();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name PERIODO */
    public static final String COLUMNNAME_PERIODO = "PERIODO";

	/** Set PERIODO	  */
	public void setPERIODO (String PERIODO);

	/** Get PERIODO	  */
	public String getPERIODO();

    /** Column name RAZON_SOCIAL */
    public static final String COLUMNNAME_RAZON_SOCIAL = "RAZON_SOCIAL";

	/** Set RAZON_SOCIAL	  */
	public void setRAZON_SOCIAL (String RAZON_SOCIAL);

	/** Get RAZON_SOCIAL	  */
	public String getRAZON_SOCIAL();

    /** Column name Record_ID */
    public static final String COLUMNNAME_Record_ID = "Record_ID";

	/** Set Record ID.
	  * Direct internal record ID
	  */
	public void setRecord_ID (int Record_ID);

	/** Get Record ID.
	  * Direct internal record ID
	  */
	public int getRecord_ID();

    /** Column name REFERENCIA */
    public static final String COLUMNNAME_REFERENCIA = "REFERENCIA";

	/** Set REFERENCIA	  */
	public void setREFERENCIA (BigDecimal REFERENCIA);

	/** Get REFERENCIA	  */
	public BigDecimal getREFERENCIA();

    /** Column name RUT */
    public static final String COLUMNNAME_RUT = "RUT";

	/** Set RUT	  */
	public void setRUT (String RUT);

	/** Get RUT	  */
	public String getRUT();

    /** Column name SALDO */
    public static final String COLUMNNAME_SALDO = "SALDO";

	/** Set SALDO	  */
	public void setSALDO (BigDecimal SALDO);

	/** Get SALDO	  */
	public BigDecimal getSALDO();

    /** Column name T_BL_CTACTEFORM_ID */
    public static final String COLUMNNAME_T_BL_CTACTEFORM_ID = "T_BL_CTACTEFORM_ID";

	/** Set T_BL_CTACTEFORM	  */
	public void setT_BL_CTACTEFORM_ID (int T_BL_CTACTEFORM_ID);

	/** Get T_BL_CTACTEFORM	  */
	public int getT_BL_CTACTEFORM_ID();

    /** Column name T_BL_CTACTESALIDA_ID */
    public static final String COLUMNNAME_T_BL_CTACTESALIDA_ID = "T_BL_CTACTESALIDA_ID";

	/** Set T_BL_CTACTESALIDA	  */
	public void setT_BL_CTACTESALIDA_ID (int T_BL_CTACTESALIDA_ID);

	/** Get T_BL_CTACTESALIDA	  */
	public int getT_BL_CTACTESALIDA_ID();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
