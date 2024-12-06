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

/** Generated Interface for HRO_Selection
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_HRO_Selection 
{

    /** TableName=HRO_Selection */
    public static final String Table_Name = "HRO_Selection";

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

    /** Column name cartareco */
    public static final String COLUMNNAME_cartareco = "cartareco";

	/** Set cartareco	  */
	public void setcartareco (boolean cartareco);

	/** Get cartareco	  */
	public boolean iscartareco();

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

    /** Column name cv */
    public static final String COLUMNNAME_cv = "cv";

	/** Set cv	  */
	public void setcv (boolean cv);

	/** Get cv	  */
	public boolean iscv();

    /** Column name evaficha */
    public static final String COLUMNNAME_evaficha = "evaficha";

	/** Set evaficha	  */
	public void setevaficha (boolean evaficha);

	/** Get evaficha	  */
	public boolean isevaficha();

    /** Column name evapsico */
    public static final String COLUMNNAME_evapsico = "evapsico";

	/** Set evapsico	  */
	public void setevapsico (boolean evapsico);

	/** Get evapsico	  */
	public boolean isevapsico();

    /** Column name grado */
    public static final String COLUMNNAME_grado = "grado";

	/** Set grado	  */
	public void setgrado (String grado);

	/** Get grado	  */
	public String getgrado();

    /** Column name HRO_CallEntries_ID */
    public static final String COLUMNNAME_HRO_CallEntries_ID = "HRO_CallEntries_ID";

	/** Set HRO_CallEntries	  */
	public void setHRO_CallEntries_ID (int HRO_CallEntries_ID);

	/** Get HRO_CallEntries	  */
	public int getHRO_CallEntries_ID();

	public I_HRO_CallEntries getHRO_CallEntries() throws RuntimeException;

    /** Column name HRO_Selection_ID */
    public static final String COLUMNNAME_HRO_Selection_ID = "HRO_Selection_ID";

	/** Set HRO_Selection	  */
	public void setHRO_Selection_ID (int HRO_Selection_ID);

	/** Get HRO_Selection	  */
	public int getHRO_Selection_ID();

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

    /** Column name puntaje */
    public static final String COLUMNNAME_puntaje = "puntaje";

	/** Set puntaje	  */
	public void setpuntaje (int puntaje);

	/** Get puntaje	  */
	public int getpuntaje();

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

    /** Column name titulo */
    public static final String COLUMNNAME_titulo = "titulo";

	/** Set titulo	  */
	public void settitulo (String titulo);

	/** Get titulo	  */
	public String gettitulo();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get UpdatedBy.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
