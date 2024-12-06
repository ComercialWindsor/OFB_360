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

/** Generated Interface for T_BL_COMISIONES
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_T_BL_COMISIONES 
{

    /** TableName=T_BL_COMISIONES */
    public static final String Table_Name = "T_BL_COMISIONES";

    /** AD_Table_ID=1000173 */
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

    /** Column name COM_BLM_H */
    public static final String COLUMNNAME_COM_BLM_H = "COM_BLM_H";

	/** Set COM_BLM_H	  */
	public void setCOM_BLM_H (BigDecimal COM_BLM_H);

	/** Get COM_BLM_H	  */
	public BigDecimal getCOM_BLM_H();

    /** Column name COM_BLM_N */
    public static final String COLUMNNAME_COM_BLM_N = "COM_BLM_N";

	/** Set COM_BLM_N	  */
	public void setCOM_BLM_N (BigDecimal COM_BLM_N);

	/** Get COM_BLM_N	  */
	public BigDecimal getCOM_BLM_N();

    /** Column name COM_BLM_T */
    public static final String COLUMNNAME_COM_BLM_T = "COM_BLM_T";

	/** Set COM_BLM_T	  */
	public void setCOM_BLM_T (BigDecimal COM_BLM_T);

	/** Get COM_BLM_T	  */
	public BigDecimal getCOM_BLM_T();

    /** Column name COM_DESDE */
    public static final String COLUMNNAME_COM_DESDE = "COM_DESDE";

	/** Set COM_DESDE	  */
	public void setCOM_DESDE (Timestamp COM_DESDE);

	/** Get COM_DESDE	  */
	public Timestamp getCOM_DESDE();

    /** Column name COM_HASTA */
    public static final String COLUMNNAME_COM_HASTA = "COM_HASTA";

	/** Set COM_HASTA	  */
	public void setCOM_HASTA (Timestamp COM_HASTA);

	/** Get COM_HASTA	  */
	public Timestamp getCOM_HASTA();

    /** Column name COM_SLT_H */
    public static final String COLUMNNAME_COM_SLT_H = "COM_SLT_H";

	/** Set COM_SLT_H	  */
	public void setCOM_SLT_H (BigDecimal COM_SLT_H);

	/** Get COM_SLT_H	  */
	public BigDecimal getCOM_SLT_H();

    /** Column name COM_SLT_N */
    public static final String COLUMNNAME_COM_SLT_N = "COM_SLT_N";

	/** Set COM_SLT_N	  */
	public void setCOM_SLT_N (BigDecimal COM_SLT_N);

	/** Get COM_SLT_N	  */
	public BigDecimal getCOM_SLT_N();

    /** Column name COM_SLT_T */
    public static final String COLUMNNAME_COM_SLT_T = "COM_SLT_T";

	/** Set COM_SLT_T	  */
	public void setCOM_SLT_T (BigDecimal COM_SLT_T);

	/** Get COM_SLT_T	  */
	public BigDecimal getCOM_SLT_T();

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

    /** Column name MGN_BLM_H */
    public static final String COLUMNNAME_MGN_BLM_H = "MGN_BLM_H";

	/** Set MGN_BLM_H	  */
	public void setMGN_BLM_H (BigDecimal MGN_BLM_H);

	/** Get MGN_BLM_H	  */
	public BigDecimal getMGN_BLM_H();

    /** Column name MGN_BLM_N */
    public static final String COLUMNNAME_MGN_BLM_N = "MGN_BLM_N";

	/** Set MGN_BLM_N	  */
	public void setMGN_BLM_N (BigDecimal MGN_BLM_N);

	/** Get MGN_BLM_N	  */
	public BigDecimal getMGN_BLM_N();

    /** Column name MGN_BLM_T */
    public static final String COLUMNNAME_MGN_BLM_T = "MGN_BLM_T";

	/** Set MGN_BLM_T	  */
	public void setMGN_BLM_T (BigDecimal MGN_BLM_T);

	/** Get MGN_BLM_T	  */
	public BigDecimal getMGN_BLM_T();

    /** Column name MGN_SLT_H */
    public static final String COLUMNNAME_MGN_SLT_H = "MGN_SLT_H";

	/** Set MGN_SLT_H	  */
	public void setMGN_SLT_H (BigDecimal MGN_SLT_H);

	/** Get MGN_SLT_H	  */
	public BigDecimal getMGN_SLT_H();

    /** Column name MGN_SLT_N */
    public static final String COLUMNNAME_MGN_SLT_N = "MGN_SLT_N";

	/** Set MGN_SLT_N	  */
	public void setMGN_SLT_N (BigDecimal MGN_SLT_N);

	/** Get MGN_SLT_N	  */
	public BigDecimal getMGN_SLT_N();

    /** Column name MGN_SLT_T */
    public static final String COLUMNNAME_MGN_SLT_T = "MGN_SLT_T";

	/** Set MGN_SLT_T	  */
	public void setMGN_SLT_T (BigDecimal MGN_SLT_T);

	/** Get MGN_SLT_T	  */
	public BigDecimal getMGN_SLT_T();

    /** Column name NOMBRE_VENDEDOR */
    public static final String COLUMNNAME_NOMBRE_VENDEDOR = "NOMBRE_VENDEDOR";

	/** Set NOMBRE_VENDEDOR	  */
	public void setNOMBRE_VENDEDOR (String NOMBRE_VENDEDOR);

	/** Get NOMBRE_VENDEDOR	  */
	public String getNOMBRE_VENDEDOR();

    /** Column name PERIODO_COMISION */
    public static final String COLUMNNAME_PERIODO_COMISION = "PERIODO_COMISION";

	/** Set PERIODO_COMISION	  */
	public void setPERIODO_COMISION (String PERIODO_COMISION);

	/** Get PERIODO_COMISION	  */
	public String getPERIODO_COMISION();

    /** Column name SalesRep_ID */
    public static final String COLUMNNAME_SalesRep_ID = "SalesRep_ID";

	/** Set Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID);

	/** Get Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public int getSalesRep_ID();

	public I_AD_User getSalesRep() throws RuntimeException;

    /** Column name T_BL_COMISIONES_ID */
    public static final String COLUMNNAME_T_BL_COMISIONES_ID = "T_BL_COMISIONES_ID";

	/** Set T_BL_COMISIONES	  */
	public void setT_BL_COMISIONES_ID (int T_BL_COMISIONES_ID);

	/** Get T_BL_COMISIONES	  */
	public int getT_BL_COMISIONES_ID();

    /** Column name T_BL_FLASH_PARAMETROS_ID */
    public static final String COLUMNNAME_T_BL_FLASH_PARAMETROS_ID = "T_BL_FLASH_PARAMETROS_ID";

	/** Set T_BL_FLASH_PARAMETROS	  */
	public void setT_BL_FLASH_PARAMETROS_ID (int T_BL_FLASH_PARAMETROS_ID);

	/** Get T_BL_FLASH_PARAMETROS	  */
	public int getT_BL_FLASH_PARAMETROS_ID();

	public I_T_BL_FLASH_PARAMETROS getT_BL_FLASH_PARAMETROS() throws RuntimeException;

    /** Column name TSA_BLM_H */
    public static final String COLUMNNAME_TSA_BLM_H = "TSA_BLM_H";

	/** Set TSA_BLM_H	  */
	public void setTSA_BLM_H (BigDecimal TSA_BLM_H);

	/** Get TSA_BLM_H	  */
	public BigDecimal getTSA_BLM_H();

    /** Column name TSA_BLM_N */
    public static final String COLUMNNAME_TSA_BLM_N = "TSA_BLM_N";

	/** Set TSA_BLM_N	  */
	public void setTSA_BLM_N (BigDecimal TSA_BLM_N);

	/** Get TSA_BLM_N	  */
	public BigDecimal getTSA_BLM_N();

    /** Column name TSA_BLM_T */
    public static final String COLUMNNAME_TSA_BLM_T = "TSA_BLM_T";

	/** Set TSA_BLM_T	  */
	public void setTSA_BLM_T (BigDecimal TSA_BLM_T);

	/** Get TSA_BLM_T	  */
	public BigDecimal getTSA_BLM_T();

    /** Column name TSA_SLT_H */
    public static final String COLUMNNAME_TSA_SLT_H = "TSA_SLT_H";

	/** Set TSA_SLT_H	  */
	public void setTSA_SLT_H (BigDecimal TSA_SLT_H);

	/** Get TSA_SLT_H	  */
	public BigDecimal getTSA_SLT_H();

    /** Column name TSA_SLT_N */
    public static final String COLUMNNAME_TSA_SLT_N = "TSA_SLT_N";

	/** Set TSA_SLT_N	  */
	public void setTSA_SLT_N (BigDecimal TSA_SLT_N);

	/** Get TSA_SLT_N	  */
	public BigDecimal getTSA_SLT_N();

    /** Column name TSA_SLT_T */
    public static final String COLUMNNAME_TSA_SLT_T = "TSA_SLT_T";

	/** Set TSA_SLT_T	  */
	public void setTSA_SLT_T (BigDecimal TSA_SLT_T);

	/** Get TSA_SLT_T	  */
	public BigDecimal getTSA_SLT_T();

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

    /** Column name VTA_BLM_H */
    public static final String COLUMNNAME_VTA_BLM_H = "VTA_BLM_H";

	/** Set VTA_BLM_H	  */
	public void setVTA_BLM_H (BigDecimal VTA_BLM_H);

	/** Get VTA_BLM_H	  */
	public BigDecimal getVTA_BLM_H();

    /** Column name VTA_BLM_N */
    public static final String COLUMNNAME_VTA_BLM_N = "VTA_BLM_N";

	/** Set VTA_BLM_N	  */
	public void setVTA_BLM_N (BigDecimal VTA_BLM_N);

	/** Get VTA_BLM_N	  */
	public BigDecimal getVTA_BLM_N();

    /** Column name VTA_BLM_T */
    public static final String COLUMNNAME_VTA_BLM_T = "VTA_BLM_T";

	/** Set VTA_BLM_T	  */
	public void setVTA_BLM_T (BigDecimal VTA_BLM_T);

	/** Get VTA_BLM_T	  */
	public BigDecimal getVTA_BLM_T();

    /** Column name VTA_SLT_H */
    public static final String COLUMNNAME_VTA_SLT_H = "VTA_SLT_H";

	/** Set VTA_SLT_H	  */
	public void setVTA_SLT_H (BigDecimal VTA_SLT_H);

	/** Get VTA_SLT_H	  */
	public BigDecimal getVTA_SLT_H();

    /** Column name VTA_SLT_N */
    public static final String COLUMNNAME_VTA_SLT_N = "VTA_SLT_N";

	/** Set VTA_SLT_N	  */
	public void setVTA_SLT_N (BigDecimal VTA_SLT_N);

	/** Get VTA_SLT_N	  */
	public BigDecimal getVTA_SLT_N();

    /** Column name VTA_SLT_T */
    public static final String COLUMNNAME_VTA_SLT_T = "VTA_SLT_T";

	/** Set VTA_SLT_T	  */
	public void setVTA_SLT_T (BigDecimal VTA_SLT_T);

	/** Get VTA_SLT_T	  */
	public BigDecimal getVTA_SLT_T();
}
