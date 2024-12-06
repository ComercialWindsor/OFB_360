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

/** Generated Interface for MP_AssetPartner
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_MP_AssetPartner 
{

    /** TableName=MP_AssetPartner */
    public static final String Table_Name = "MP_AssetPartner";

    /** AD_Table_ID=1000097 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name A_Asset_ID */
    public static final String COLUMNNAME_A_Asset_ID = "A_Asset_ID";

	/** Set Asset.
	  * Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID);

	/** Get Asset.
	  * Asset used internally or by customers
	  */
	public int getA_Asset_ID();

	public I_A_Asset getA_Asset() throws RuntimeException;

    /** Column name A_AssetRef_ID */
    public static final String COLUMNNAME_A_AssetRef_ID = "A_AssetRef_ID";

	/** Set A_AssetRef_ID	  */
	public void setA_AssetRef_ID (int A_AssetRef_ID);

	/** Get A_AssetRef_ID	  */
	public int getA_AssetRef_ID();

	public I_A_Asset getA_AssetRef() throws RuntimeException;

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

    /** Column name Attribute */
    public static final String COLUMNNAME_Attribute = "Attribute";

	/** Set Attribute	  */
	public void setAttribute (String Attribute);

	/** Get Attribute	  */
	public String getAttribute();

    /** Column name Attribute2 */
    public static final String COLUMNNAME_Attribute2 = "Attribute2";

	/** Set Attribute2	  */
	public void setAttribute2 (String Attribute2);

	/** Get Attribute2	  */
	public String getAttribute2();

    /** Column name Attribute3 */
    public static final String COLUMNNAME_Attribute3 = "Attribute3";

	/** Set Attribute3	  */
	public void setAttribute3 (String Attribute3);

	/** Get Attribute3	  */
	public String getAttribute3();

    /** Column name Attribute4 */
    public static final String COLUMNNAME_Attribute4 = "Attribute4";

	/** Set Attribute4	  */
	public void setAttribute4 (String Attribute4);

	/** Get Attribute4	  */
	public String getAttribute4();

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

    /** Column name MP_AssetPartner_ID */
    public static final String COLUMNNAME_MP_AssetPartner_ID = "MP_AssetPartner_ID";

	/** Set MP_AssetPartner	  */
	public void setMP_AssetPartner_ID (int MP_AssetPartner_ID);

	/** Get MP_AssetPartner	  */
	public int getMP_AssetPartner_ID();

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
