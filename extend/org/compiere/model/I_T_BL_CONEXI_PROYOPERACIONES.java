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

/** Generated Interface for T_BL_CONEXI_PROYOPERACIONES
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_CONEXI_PROYOPERACIONES 
{

    /** TableName=T_BL_CONEXI_PROYOPERACIONES */
    public static final String Table_Name = "T_BL_CONEXI_PROYOPERACIONES";

    /** AD_Table_ID=1000174 */
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

    /** Column name COD_BLUMOS */
    public static final String COLUMNNAME_COD_BLUMOS = "COD_BLUMOS";

	/** Set COD_BLUMOS	  */
	public void setCOD_BLUMOS (String COD_BLUMOS);

	/** Get COD_BLUMOS	  */
	public String getCOD_BLUMOS();

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

    /** Column name T_BL_CONEXI_PROY_ID */
    public static final String COLUMNNAME_T_BL_CONEXI_PROY_ID = "T_BL_CONEXI_PROY_ID";

	/** Set T_BL_CONEXI_PROY	  */
	public void setT_BL_CONEXI_PROY_ID (int T_BL_CONEXI_PROY_ID);

	/** Get T_BL_CONEXI_PROY	  */
	public int getT_BL_CONEXI_PROY_ID();

	public I_T_BL_CONEXI_PROY getT_BL_CONEXI_PROY() throws RuntimeException;

    /** Column name T_BL_CONEXI_PROYOPERACIONES_ID */
    public static final String COLUMNNAME_T_BL_CONEXI_PROYOPERACIONES_ID = "T_BL_CONEXI_PROYOPERACIONES_ID";

	/** Set T_BL_CONEXI_PROYOPERACIONES	  */
	public void setT_BL_CONEXI_PROYOPERACIONES_ID (int T_BL_CONEXI_PROYOPERACIONES_ID);

	/** Get T_BL_CONEXI_PROYOPERACIONES	  */
	public int getT_BL_CONEXI_PROYOPERACIONES_ID();

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

    /** Column name USUARIO_ID */
    public static final String COLUMNNAME_USUARIO_ID = "USUARIO_ID";

	/** Set USUARIO_ID	  */
	public void setUSUARIO_ID (int USUARIO_ID);

	/** Get USUARIO_ID	  */
	public int getUSUARIO_ID();

	public I_AD_User getUSUARIO() throws RuntimeException;
}
