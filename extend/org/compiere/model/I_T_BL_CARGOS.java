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

/** Generated Interface for T_BL_CARGOS
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_CARGOS 
{

    /** TableName=T_BL_CARGOS */
    public static final String Table_Name = "T_BL_CARGOS";

    /** AD_Table_ID=1000189 */
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

    /** Column name CODIGO */
    public static final String COLUMNNAME_CODIGO = "CODIGO";

	/** Set CODIGO	  */
	public void setCODIGO (String CODIGO);

	/** Get CODIGO	  */
	public String getCODIGO();

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

    /** Column name ESGERENTE */
    public static final String COLUMNNAME_ESGERENTE = "ESGERENTE";

	/** Set ESGERENTE	  */
	public void setESGERENTE (boolean ESGERENTE);

	/** Get ESGERENTE	  */
	public boolean isESGERENTE();

    /** Column name FECHA */
    public static final String COLUMNNAME_FECHA = "FECHA";

	/** Set FECHA	  */
	public void setFECHA (Timestamp FECHA);

	/** Get FECHA	  */
	public Timestamp getFECHA();

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

    /** Column name NOMBRECARGO */
    public static final String COLUMNNAME_NOMBRECARGO = "NOMBRECARGO";

	/** Set NOMBRECARGO	  */
	public void setNOMBRECARGO (String NOMBRECARGO);

	/** Get NOMBRECARGO	  */
	public String getNOMBRECARGO();

    /** Column name RAIZ */
    public static final String COLUMNNAME_RAIZ = "RAIZ";

	/** Set RAIZ	  */
	public void setRAIZ (boolean RAIZ);

	/** Get RAIZ	  */
	public boolean isRAIZ();

    /** Column name Supervisor_ID */
    public static final String COLUMNNAME_Supervisor_ID = "Supervisor_ID";

	/** Set Supervisor.
	  * Supervisor for this user/organization - used for escalation and approval
	  */
	public void setSupervisor_ID (int Supervisor_ID);

	/** Get Supervisor.
	  * Supervisor for this user/organization - used for escalation and approval
	  */
	public int getSupervisor_ID();

	public I_T_BL_CARGOS getSupervisor() throws RuntimeException;

    /** Column name T_BL_CARGOS_ID */
    public static final String COLUMNNAME_T_BL_CARGOS_ID = "T_BL_CARGOS_ID";

	/** Set T_BL_CARGOS	  */
	public void setT_BL_CARGOS_ID (int T_BL_CARGOS_ID);

	/** Get T_BL_CARGOS	  */
	public int getT_BL_CARGOS_ID();

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
