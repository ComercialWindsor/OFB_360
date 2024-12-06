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

/** Generated Interface for T_BL_CONEXI_PROYCLIENTE
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_CONEXI_PROYCLIENTE 
{

    /** TableName=T_BL_CONEXI_PROYCLIENTE */
    public static final String Table_Name = "T_BL_CONEXI_PROYCLIENTE";

    /** AD_Table_ID=1000154 */
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

    /** Column name H0 */
    public static final String COLUMNNAME_H0 = "H0";

	/** Set H0	  */
	public void setH0 (BigDecimal H0);

	/** Get H0	  */
	public BigDecimal getH0();

    /** Column name H1 */
    public static final String COLUMNNAME_H1 = "H1";

	/** Set H1	  */
	public void setH1 (BigDecimal H1);

	/** Get H1	  */
	public BigDecimal getH1();

    /** Column name H10 */
    public static final String COLUMNNAME_H10 = "H10";

	/** Set H10	  */
	public void setH10 (BigDecimal H10);

	/** Get H10	  */
	public BigDecimal getH10();

    /** Column name H11 */
    public static final String COLUMNNAME_H11 = "H11";

	/** Set H11	  */
	public void setH11 (BigDecimal H11);

	/** Get H11	  */
	public BigDecimal getH11();

    /** Column name H12 */
    public static final String COLUMNNAME_H12 = "H12";

	/** Set H12	  */
	public void setH12 (BigDecimal H12);

	/** Get H12	  */
	public BigDecimal getH12();

    /** Column name H2 */
    public static final String COLUMNNAME_H2 = "H2";

	/** Set H2	  */
	public void setH2 (BigDecimal H2);

	/** Get H2	  */
	public BigDecimal getH2();

    /** Column name H3 */
    public static final String COLUMNNAME_H3 = "H3";

	/** Set H3	  */
	public void setH3 (BigDecimal H3);

	/** Get H3	  */
	public BigDecimal getH3();

    /** Column name H4 */
    public static final String COLUMNNAME_H4 = "H4";

	/** Set H4	  */
	public void setH4 (BigDecimal H4);

	/** Get H4	  */
	public BigDecimal getH4();

    /** Column name H5 */
    public static final String COLUMNNAME_H5 = "H5";

	/** Set H5	  */
	public void setH5 (BigDecimal H5);

	/** Get H5	  */
	public BigDecimal getH5();

    /** Column name H6 */
    public static final String COLUMNNAME_H6 = "H6";

	/** Set H6	  */
	public void setH6 (BigDecimal H6);

	/** Get H6	  */
	public BigDecimal getH6();

    /** Column name H7 */
    public static final String COLUMNNAME_H7 = "H7";

	/** Set H7	  */
	public void setH7 (BigDecimal H7);

	/** Get H7	  */
	public BigDecimal getH7();

    /** Column name H8 */
    public static final String COLUMNNAME_H8 = "H8";

	/** Set H8	  */
	public void setH8 (BigDecimal H8);

	/** Get H8	  */
	public BigDecimal getH8();

    /** Column name H9 */
    public static final String COLUMNNAME_H9 = "H9";

	/** Set H9	  */
	public void setH9 (BigDecimal H9);

	/** Get H9	  */
	public BigDecimal getH9();

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

    /** Column name NOMBRE_PRODUCTO */
    public static final String COLUMNNAME_NOMBRE_PRODUCTO = "NOMBRE_PRODUCTO";

	/** Set NOMBRE_PRODUCTO	  */
	public void setNOMBRE_PRODUCTO (String NOMBRE_PRODUCTO);

	/** Get NOMBRE_PRODUCTO	  */
	public String getNOMBRE_PRODUCTO();

    /** Column name P0 */
    public static final String COLUMNNAME_P0 = "P0";

	/** Set P0	  */
	public void setP0 (BigDecimal P0);

	/** Get P0	  */
	public BigDecimal getP0();

    /** Column name P1 */
    public static final String COLUMNNAME_P1 = "P1";

	/** Set P1	  */
	public void setP1 (BigDecimal P1);

	/** Get P1	  */
	public BigDecimal getP1();

    /** Column name P2 */
    public static final String COLUMNNAME_P2 = "P2";

	/** Set P2	  */
	public void setP2 (BigDecimal P2);

	/** Get P2	  */
	public BigDecimal getP2();

    /** Column name P3 */
    public static final String COLUMNNAME_P3 = "P3";

	/** Set P3	  */
	public void setP3 (BigDecimal P3);

	/** Get P3	  */
	public BigDecimal getP3();

    /** Column name P4 */
    public static final String COLUMNNAME_P4 = "P4";

	/** Set P4	  */
	public void setP4 (BigDecimal P4);

	/** Get P4	  */
	public BigDecimal getP4();

    /** Column name P5 */
    public static final String COLUMNNAME_P5 = "P5";

	/** Set P5	  */
	public void setP5 (BigDecimal P5);

	/** Get P5	  */
	public BigDecimal getP5();

    /** Column name T_BL_CONEXI_PROYCLIENTE_ID */
    public static final String COLUMNNAME_T_BL_CONEXI_PROYCLIENTE_ID = "T_BL_CONEXI_PROYCLIENTE_ID";

	/** Set T_BL_CONEXI_PROYCLIENTE	  */
	public void setT_BL_CONEXI_PROYCLIENTE_ID (int T_BL_CONEXI_PROYCLIENTE_ID);

	/** Get T_BL_CONEXI_PROYCLIENTE	  */
	public int getT_BL_CONEXI_PROYCLIENTE_ID();

    /** Column name T_BL_CONEXI_PROY_ID */
    public static final String COLUMNNAME_T_BL_CONEXI_PROY_ID = "T_BL_CONEXI_PROY_ID";

	/** Set T_BL_CONEXI_PROY	  */
	public void setT_BL_CONEXI_PROY_ID (int T_BL_CONEXI_PROY_ID);

	/** Get T_BL_CONEXI_PROY	  */
	public int getT_BL_CONEXI_PROY_ID();

	public I_T_BL_CONEXI_PROY getT_BL_CONEXI_PROY() throws RuntimeException;

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
