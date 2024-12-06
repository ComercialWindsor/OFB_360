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

/** Generated Interface for I_BPartnerIM
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_I_BPartnerIM 
{

    /** TableName=I_BPartnerIM */
    public static final String Table_Name = "I_BPartnerIM";

    /** AD_Table_ID=1000102 */
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

    /** Column name APE_MAT */
    public static final String COLUMNNAME_APE_MAT = "APE_MAT";

	/** Set APE_MAT	  */
	public void setAPE_MAT (String APE_MAT);

	/** Get APE_MAT	  */
	public String getAPE_MAT();

    /** Column name APE_PAT */
    public static final String COLUMNNAME_APE_PAT = "APE_PAT";

	/** Set APE_PAT	  */
	public void setAPE_PAT (String APE_PAT);

	/** Get APE_PAT	  */
	public String getAPE_PAT();

    /** Column name C_Campaign_ID */
    public static final String COLUMNNAME_C_Campaign_ID = "C_Campaign_ID";

	/** Set Campaign.
	  * Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID);

	/** Get Campaign.
	  * Marketing Campaign
	  */
	public int getC_Campaign_ID();

	public I_C_Campaign getC_Campaign() throws RuntimeException;

    /** Column name CIU_SOL */
    public static final String COLUMNNAME_CIU_SOL = "CIU_SOL";

	/** Set CIU_SOL	  */
	public void setCIU_SOL (int CIU_SOL);

	/** Get CIU_SOL	  */
	public int getCIU_SOL();

	public I_C_City getCIU_() throws RuntimeException;

    /** Column name COLAB */
    public static final String COLUMNNAME_COLAB = "COLAB";

	/** Set COLAB	  */
	public void setCOLAB (String COLAB);

	/** Get COLAB	  */
	public String getCOLAB();

    /** Column name COM_SOL */
    public static final String COLUMNNAME_COM_SOL = "COM_SOL";

	/** Set COM_SOL	  */
	public void setCOM_SOL (String COM_SOL);

	/** Get COM_SOL	  */
	public String getCOM_SOL();

    /** Column name CORR_SEG */
    public static final String COLUMNNAME_CORR_SEG = "CORR_SEG";

	/** Set CORR_SEG	  */
	public void setCORR_SEG (String CORR_SEG);

	/** Get CORR_SEG	  */
	public String getCORR_SEG();

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

    /** Column name DIR_EML */
    public static final String COLUMNNAME_DIR_EML = "DIR_EML";

	/** Set DIR_EML	  */
	public void setDIR_EML (String DIR_EML);

	/** Get DIR_EML	  */
	public String getDIR_EML();

    /** Column name DIR_SOL */
    public static final String COLUMNNAME_DIR_SOL = "DIR_SOL";

	/** Set DIR_SOL	  */
	public void setDIR_SOL (String DIR_SOL);

	/** Get DIR_SOL	  */
	public String getDIR_SOL();

    /** Column name EMP_CIU */
    public static final String COLUMNNAME_EMP_CIU = "EMP_CIU";

	/** Set EMP_CIU	  */
	public void setEMP_CIU (String EMP_CIU);

	/** Get EMP_CIU	  */
	public String getEMP_CIU();

    /** Column name EMP_COM */
    public static final String COLUMNNAME_EMP_COM = "EMP_COM";

	/** Set EMP_COM	  */
	public void setEMP_COM (String EMP_COM);

	/** Get EMP_COM	  */
	public String getEMP_COM();

    /** Column name EMP_DIR_CALLE */
    public static final String COLUMNNAME_EMP_DIR_CALLE = "EMP_DIR_CALLE";

	/** Set EMP_DIR_CALLE	  */
	public void setEMP_DIR_CALLE (String EMP_DIR_CALLE);

	/** Get EMP_DIR_CALLE	  */
	public String getEMP_DIR_CALLE();

    /** Column name EMP_FONO_AREA */
    public static final String COLUMNNAME_EMP_FONO_AREA = "EMP_FONO_AREA";

	/** Set EMP_FONO_AREA	  */
	public void setEMP_FONO_AREA (String EMP_FONO_AREA);

	/** Get EMP_FONO_AREA	  */
	public String getEMP_FONO_AREA();

    /** Column name EMP_FONO_NUM */
    public static final String COLUMNNAME_EMP_FONO_NUM = "EMP_FONO_NUM";

	/** Set EMP_FONO_NUM	  */
	public void setEMP_FONO_NUM (String EMP_FONO_NUM);

	/** Get EMP_FONO_NUM	  */
	public String getEMP_FONO_NUM();

    /** Column name EMP_REG */
    public static final String COLUMNNAME_EMP_REG = "EMP_REG";

	/** Set EMP_REG	  */
	public void setEMP_REG (String EMP_REG);

	/** Get EMP_REG	  */
	public String getEMP_REG();

    /** Column name EMP_RUT */
    public static final String COLUMNNAME_EMP_RUT = "EMP_RUT";

	/** Set EMP_RUT	  */
	public void setEMP_RUT (String EMP_RUT);

	/** Get EMP_RUT	  */
	public String getEMP_RUT();

    /** Column name EST_CIVIL */
    public static final String COLUMNNAME_EST_CIVIL = "EST_CIVIL";

	/** Set EST_CIVIL	  */
	public void setEST_CIVIL (String EST_CIVIL);

	/** Get EST_CIVIL	  */
	public String getEST_CIVIL();

    /** Column name FEC_CRE */
    public static final String COLUMNNAME_FEC_CRE = "FEC_CRE";

	/** Set FEC_CRE	  */
	public void setFEC_CRE (Timestamp FEC_CRE);

	/** Get FEC_CRE	  */
	public Timestamp getFEC_CRE();

    /** Column name FEC_NAC */
    public static final String COLUMNNAME_FEC_NAC = "FEC_NAC";

	/** Set FEC_NAC	  */
	public void setFEC_NAC (Timestamp FEC_NAC);

	/** Get FEC_NAC	  */
	public Timestamp getFEC_NAC();

    /** Column name FILLER1 */
    public static final String COLUMNNAME_FILLER1 = "FILLER1";

	/** Set FILLER1	  */
	public void setFILLER1 (String FILLER1);

	/** Get FILLER1	  */
	public String getFILLER1();

    /** Column name FILLER2 */
    public static final String COLUMNNAME_FILLER2 = "FILLER2";

	/** Set FILLER2	  */
	public void setFILLER2 (String FILLER2);

	/** Get FILLER2	  */
	public String getFILLER2();

    /** Column name FILLER3 */
    public static final String COLUMNNAME_FILLER3 = "FILLER3";

	/** Set FILLER3	  */
	public void setFILLER3 (int FILLER3);

	/** Get FILLER3	  */
	public int getFILLER3();

    /** Column name FONO_AREA */
    public static final String COLUMNNAME_FONO_AREA = "FONO_AREA";

	/** Set FONO_AREA	  */
	public void setFONO_AREA (String FONO_AREA);

	/** Get FONO_AREA	  */
	public String getFONO_AREA();

    /** Column name I_BPartnerIM_ID */
    public static final String COLUMNNAME_I_BPartnerIM_ID = "I_BPartnerIM_ID";

	/** Set I_BPartnerIM_ID	  */
	public void setI_BPartnerIM_ID (int I_BPartnerIM_ID);

	/** Get I_BPartnerIM_ID	  */
	public int getI_BPartnerIM_ID();

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

    /** Column name LEADTYPE */
    public static final String COLUMNNAME_LEADTYPE = "LEADTYPE";

	/** Set LEADTYPE	  */
	public void setLEADTYPE (String LEADTYPE);

	/** Get LEADTYPE	  */
	public String getLEADTYPE();

    /** Column name NOM_SOL */
    public static final String COLUMNNAME_NOM_SOL = "NOM_SOL";

	/** Set NOM_SOL	  */
	public void setNOM_SOL (String NOM_SOL);

	/** Get NOM_SOL	  */
	public String getNOM_SOL();

    /** Column name OBS_SOL */
    public static final String COLUMNNAME_OBS_SOL = "OBS_SOL";

	/** Set OBS_SOL	  */
	public void setOBS_SOL (String OBS_SOL);

	/** Get OBS_SOL	  */
	public String getOBS_SOL();

    /** Column name ORI_SOL */
    public static final String COLUMNNAME_ORI_SOL = "ORI_SOL";

	/** Set ORI_SOL	  */
	public void setORI_SOL (String ORI_SOL);

	/** Get ORI_SOL	  */
	public String getORI_SOL();

    /** Column name PROD_INT1 */
    public static final String COLUMNNAME_PROD_INT1 = "PROD_INT1";

	/** Set PROD_INT1	  */
	public void setPROD_INT1 (String PROD_INT1);

	/** Get PROD_INT1	  */
	public String getPROD_INT1();

    /** Column name PROD_INT2 */
    public static final String COLUMNNAME_PROD_INT2 = "PROD_INT2";

	/** Set PROD_INT2	  */
	public void setPROD_INT2 (String PROD_INT2);

	/** Get PROD_INT2	  */
	public String getPROD_INT2();

    /** Column name RAZON_SOC */
    public static final String COLUMNNAME_RAZON_SOC = "RAZON_SOC";

	/** Set RAZON_SOC	  */
	public void setRAZON_SOC (String RAZON_SOC);

	/** Get RAZON_SOC	  */
	public String getRAZON_SOC();

    /** Column name REG_SOL */
    public static final String COLUMNNAME_REG_SOL = "REG_SOL";

	/** Set REG_SOL	  */
	public void setREG_SOL (String REG_SOL);

	/** Get REG_SOL	  */
	public String getREG_SOL();

    /** Column name RUT_SOL */
    public static final String COLUMNNAME_RUT_SOL = "RUT_SOL";

	/** Set RUT_SOL	  */
	public void setRUT_SOL (String RUT_SOL);

	/** Get RUT_SOL	  */
	public String getRUT_SOL();

    /** Column name SEXO */
    public static final String COLUMNNAME_SEXO = "SEXO";

	/** Set SEXO	  */
	public void setSEXO (String SEXO);

	/** Get SEXO	  */
	public String getSEXO();

    /** Column name SOL_INM_SOL */
    public static final String COLUMNNAME_SOL_INM_SOL = "SOL_INM_SOL";

	/** Set SOL_INM_SOL	  */
	public void setSOL_INM_SOL (String SOL_INM_SOL);

	/** Get SOL_INM_SOL	  */
	public String getSOL_INM_SOL();

    /** Column name SOL_MON_CRED */
    public static final String COLUMNNAME_SOL_MON_CRED = "SOL_MON_CRED";

	/** Set SOL_MON_CRED	  */
	public void setSOL_MON_CRED (int SOL_MON_CRED);

	/** Get SOL_MON_CRED	  */
	public int getSOL_MON_CRED();

    /** Column name SOL_PORC_FIN */
    public static final String COLUMNNAME_SOL_PORC_FIN = "SOL_PORC_FIN";

	/** Set SOL_PORC_FIN	  */
	public void setSOL_PORC_FIN (int SOL_PORC_FIN);

	/** Get SOL_PORC_FIN	  */
	public int getSOL_PORC_FIN();

    /** Column name SOL_TIP_PROP */
    public static final String COLUMNNAME_SOL_TIP_PROP = "SOL_TIP_PROP";

	/** Set SOL_TIP_PROP	  */
	public void setSOL_TIP_PROP (String SOL_TIP_PROP);

	/** Get SOL_TIP_PROP	  */
	public String getSOL_TIP_PROP();

    /** Column name SOL_VAL_PROP */
    public static final String COLUMNNAME_SOL_VAL_PROP = "SOL_VAL_PROP";

	/** Set SOL_VAL_PROP	  */
	public void setSOL_VAL_PROP (int SOL_VAL_PROP);

	/** Get SOL_VAL_PROP	  */
	public int getSOL_VAL_PROP();

    /** Column name TEL_CEL */
    public static final String COLUMNNAME_TEL_CEL = "TEL_CEL";

	/** Set TEL_CEL	  */
	public void setTEL_CEL (String TEL_CEL);

	/** Get TEL_CEL	  */
	public String getTEL_CEL();

    /** Column name TEL_FIJO */
    public static final String COLUMNNAME_TEL_FIJO = "TEL_FIJO";

	/** Set TEL_FIJO	  */
	public void setTEL_FIJO (String TEL_FIJO);

	/** Get TEL_FIJO	  */
	public String getTEL_FIJO();

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
