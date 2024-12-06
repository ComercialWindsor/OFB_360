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

/** Generated Interface for I_IMPORTCONSUMPTIONLINE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_I_IMPORTCONSUMPTIONLINE 
{

    /** TableName=I_IMPORTCONSUMPTIONLINE */
    public static final String Table_Name = "I_IMPORTCONSUMPTIONLINE";

    /** AD_Table_ID=1000213 */
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

    /** Column name cant */
    public static final String COLUMNNAME_cant = "cant";

	/** Set cant	  */
	public void setcant (String cant);

	/** Get cant	  */
	public String getcant();

    /** Column name codprod */
    public static final String COLUMNNAME_codprod = "codprod";

	/** Set codprod	  */
	public void setcodprod (String codprod);

	/** Get codprod	  */
	public String getcodprod();

    /** Column name costo */
    public static final String COLUMNNAME_costo = "costo";

	/** Set costo	  */
	public void setcosto (String costo);

	/** Get costo	  */
	public String getcosto();

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

    /** Column name I_IMPORTCONSUMPTIONLINE_ID */
    public static final String COLUMNNAME_I_IMPORTCONSUMPTIONLINE_ID = "I_IMPORTCONSUMPTIONLINE_ID";

	/** Set I_IMPORTCONSUMPTIONLINE	  */
	public void setI_IMPORTCONSUMPTIONLINE_ID (int I_IMPORTCONSUMPTIONLINE_ID);

	/** Get I_IMPORTCONSUMPTIONLINE	  */
	public int getI_IMPORTCONSUMPTIONLINE_ID();

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

    /** Column name nrodoc */
    public static final String COLUMNNAME_nrodoc = "nrodoc";

	/** Set nrodoc	  */
	public void setnrodoc (String nrodoc);

	/** Get nrodoc	  */
	public String getnrodoc();

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

    /** Column name tipomov */
    public static final String COLUMNNAME_tipomov = "tipomov";

	/** Set tipomov	  */
	public void settipomov (String tipomov);

	/** Get tipomov	  */
	public String gettipomov();

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
