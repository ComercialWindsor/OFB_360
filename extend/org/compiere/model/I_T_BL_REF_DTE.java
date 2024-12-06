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

/** Generated Interface for T_BL_REF_DTE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_REF_DTE 
{

    /** TableName=T_BL_REF_DTE */
    public static final String Table_Name = "T_BL_REF_DTE";

    /** AD_Table_ID=1000096 */
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

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public I_C_Invoice getC_Invoice() throws RuntimeException;

    /** Column name COD_REF */
    public static final String COLUMNNAME_COD_REF = "COD_REF";

	/** Set COD_REF	  */
	public void setCOD_REF (String COD_REF);

	/** Get COD_REF	  */
	public String getCOD_REF();

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

    /** Column name DATE_REF */
    public static final String COLUMNNAME_DATE_REF = "DATE_REF";

	/** Set DATE_REF	  */
	public void setDATE_REF (Timestamp DATE_REF);

	/** Get DATE_REF	  */
	public Timestamp getDATE_REF();

    /** Column name DOC_REFERENCIADO */
    public static final String COLUMNNAME_DOC_REFERENCIADO = "DOC_REFERENCIADO";

	/** Set DOC_REFERENCIADO	  */
	public void setDOC_REFERENCIADO (String DOC_REFERENCIADO);

	/** Get DOC_REFERENCIADO	  */
	public String getDOC_REFERENCIADO();

    /** Column name ID_DOC_REF */
    public static final String COLUMNNAME_ID_DOC_REF = "ID_DOC_REF";

	/** Set ID_DOC_REF	  */
	public void setID_DOC_REF (int ID_DOC_REF);

	/** Get ID_DOC_REF	  */
	public int getID_DOC_REF();

	public I_C_Invoice getID_DOC_() throws RuntimeException;

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

    /** Column name Line */
    public static final String COLUMNNAME_Line = "Line";

	/** Set Line No.
	  * Unique line for this document
	  */
	public void setLine (int Line);

	/** Get Line No.
	  * Unique line for this document
	  */
	public int getLine();

    /** Column name RAZON_REF */
    public static final String COLUMNNAME_RAZON_REF = "RAZON_REF";

	/** Set RAZON_REF	  */
	public void setRAZON_REF (String RAZON_REF);

	/** Get RAZON_REF	  */
	public String getRAZON_REF();

    /** Column name T_BL_REF_DTE_ID */
    public static final String COLUMNNAME_T_BL_REF_DTE_ID = "T_BL_REF_DTE_ID";

	/** Set T_BL_REF_DTE	  */
	public void setT_BL_REF_DTE_ID (int T_BL_REF_DTE_ID);

	/** Get T_BL_REF_DTE	  */
	public int getT_BL_REF_DTE_ID();

    /** Column name TIPO_DOC_REF */
    public static final String COLUMNNAME_TIPO_DOC_REF = "TIPO_DOC_REF";

	/** Set TIPO_DOC_REF	  */
	public void setTIPO_DOC_REF (String TIPO_DOC_REF);

	/** Get TIPO_DOC_REF	  */
	public String getTIPO_DOC_REF();

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
}
