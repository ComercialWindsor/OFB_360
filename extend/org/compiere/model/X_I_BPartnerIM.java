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
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for I_BPartnerIM
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_I_BPartnerIM extends PO implements I_I_BPartnerIM, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150820L;

    /** Standard Constructor */
    public X_I_BPartnerIM (Properties ctx, int I_BPartnerIM_ID, String trxName)
    {
      super (ctx, I_BPartnerIM_ID, trxName);
      /** if (I_BPartnerIM_ID == 0)
        {
			setI_BPartnerIM_ID (0);
        } */
    }

    /** Load Constructor */
    public X_I_BPartnerIM (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_I_BPartnerIM[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set APE_MAT.
		@param APE_MAT APE_MAT	  */
	public void setAPE_MAT (String APE_MAT)
	{
		set_Value (COLUMNNAME_APE_MAT, APE_MAT);
	}

	/** Get APE_MAT.
		@return APE_MAT	  */
	public String getAPE_MAT () 
	{
		return (String)get_Value(COLUMNNAME_APE_MAT);
	}

	/** Set APE_PAT.
		@param APE_PAT APE_PAT	  */
	public void setAPE_PAT (String APE_PAT)
	{
		set_Value (COLUMNNAME_APE_PAT, APE_PAT);
	}

	/** Get APE_PAT.
		@return APE_PAT	  */
	public String getAPE_PAT () 
	{
		return (String)get_Value(COLUMNNAME_APE_PAT);
	}

	public I_C_Campaign getC_Campaign() throws RuntimeException
    {
		return (I_C_Campaign)MTable.get(getCtx(), I_C_Campaign.Table_Name)
			.getPO(getC_Campaign_ID(), get_TrxName());	}

	/** Set Campaign.
		@param C_Campaign_ID 
		Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID)
	{
		if (C_Campaign_ID < 1) 
			set_Value (COLUMNNAME_C_Campaign_ID, null);
		else 
			set_Value (COLUMNNAME_C_Campaign_ID, Integer.valueOf(C_Campaign_ID));
	}

	/** Get Campaign.
		@return Marketing Campaign
	  */
	public int getC_Campaign_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Campaign_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_City getCIU_() throws RuntimeException
    {
		return (I_C_City)MTable.get(getCtx(), I_C_City.Table_Name)
			.getPO(getCIU_SOL(), get_TrxName());	}

	/** Set CIU_SOL.
		@param CIU_SOL CIU_SOL	  */
	public void setCIU_SOL (int CIU_SOL)
	{
		set_Value (COLUMNNAME_CIU_SOL, Integer.valueOf(CIU_SOL));
	}

	/** Get CIU_SOL.
		@return CIU_SOL	  */
	public int getCIU_SOL () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CIU_SOL);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set COLAB.
		@param COLAB COLAB	  */
	public void setCOLAB (String COLAB)
	{
		set_Value (COLUMNNAME_COLAB, COLAB);
	}

	/** Get COLAB.
		@return COLAB	  */
	public String getCOLAB () 
	{
		return (String)get_Value(COLUMNNAME_COLAB);
	}

	/** Set COM_SOL.
		@param COM_SOL COM_SOL	  */
	public void setCOM_SOL (String COM_SOL)
	{
		set_Value (COLUMNNAME_COM_SOL, COM_SOL);
	}

	/** Get COM_SOL.
		@return COM_SOL	  */
	public String getCOM_SOL () 
	{
		return (String)get_Value(COLUMNNAME_COM_SOL);
	}

	/** Set CORR_SEG.
		@param CORR_SEG CORR_SEG	  */
	public void setCORR_SEG (String CORR_SEG)
	{
		set_Value (COLUMNNAME_CORR_SEG, CORR_SEG);
	}

	/** Get CORR_SEG.
		@return CORR_SEG	  */
	public String getCORR_SEG () 
	{
		return (String)get_Value(COLUMNNAME_CORR_SEG);
	}

	/** Set DIR_EML.
		@param DIR_EML DIR_EML	  */
	public void setDIR_EML (String DIR_EML)
	{
		set_Value (COLUMNNAME_DIR_EML, DIR_EML);
	}

	/** Get DIR_EML.
		@return DIR_EML	  */
	public String getDIR_EML () 
	{
		return (String)get_Value(COLUMNNAME_DIR_EML);
	}

	/** Set DIR_SOL.
		@param DIR_SOL DIR_SOL	  */
	public void setDIR_SOL (String DIR_SOL)
	{
		set_Value (COLUMNNAME_DIR_SOL, DIR_SOL);
	}

	/** Get DIR_SOL.
		@return DIR_SOL	  */
	public String getDIR_SOL () 
	{
		return (String)get_Value(COLUMNNAME_DIR_SOL);
	}

	/** Set EMP_CIU.
		@param EMP_CIU EMP_CIU	  */
	public void setEMP_CIU (String EMP_CIU)
	{
		set_Value (COLUMNNAME_EMP_CIU, EMP_CIU);
	}

	/** Get EMP_CIU.
		@return EMP_CIU	  */
	public String getEMP_CIU () 
	{
		return (String)get_Value(COLUMNNAME_EMP_CIU);
	}

	/** Set EMP_COM.
		@param EMP_COM EMP_COM	  */
	public void setEMP_COM (String EMP_COM)
	{
		set_Value (COLUMNNAME_EMP_COM, EMP_COM);
	}

	/** Get EMP_COM.
		@return EMP_COM	  */
	public String getEMP_COM () 
	{
		return (String)get_Value(COLUMNNAME_EMP_COM);
	}

	/** Set EMP_DIR_CALLE.
		@param EMP_DIR_CALLE EMP_DIR_CALLE	  */
	public void setEMP_DIR_CALLE (String EMP_DIR_CALLE)
	{
		set_Value (COLUMNNAME_EMP_DIR_CALLE, EMP_DIR_CALLE);
	}

	/** Get EMP_DIR_CALLE.
		@return EMP_DIR_CALLE	  */
	public String getEMP_DIR_CALLE () 
	{
		return (String)get_Value(COLUMNNAME_EMP_DIR_CALLE);
	}

	/** Set EMP_FONO_AREA.
		@param EMP_FONO_AREA EMP_FONO_AREA	  */
	public void setEMP_FONO_AREA (String EMP_FONO_AREA)
	{
		set_Value (COLUMNNAME_EMP_FONO_AREA, EMP_FONO_AREA);		
	}

	/** Get EMP_FONO_AREA.
		@return EMP_FONO_AREA	  */
	public String getEMP_FONO_AREA () 
	{
		return (String)get_Value(COLUMNNAME_EMP_FONO_AREA);
	}

	/** Set EMP_FONO_NUM.
		@param EMP_FONO_NUM EMP_FONO_NUM	  */
	public void setEMP_FONO_NUM (String EMP_FONO_NUM)
	{
		set_Value (COLUMNNAME_EMP_FONO_NUM, EMP_FONO_NUM);
	}

	/** Get EMP_FONO_NUM.
		@return EMP_FONO_NUM	  */
	public String getEMP_FONO_NUM () 
	{
		return (String)get_Value(COLUMNNAME_EMP_FONO_NUM);
	}

	/** Set EMP_REG.
		@param EMP_REG EMP_REG	  */
	public void setEMP_REG (String EMP_REG)
	{
		set_Value (COLUMNNAME_EMP_REG, EMP_REG);
	}

	/** Get EMP_REG.
		@return EMP_REG	  */
	public String getEMP_REG () 
	{
		return (String)get_Value(COLUMNNAME_EMP_REG);
	}

	/** Set EMP_RUT.
		@param EMP_RUT EMP_RUT	  */
	public void setEMP_RUT (String EMP_RUT)
	{
		set_Value (COLUMNNAME_EMP_RUT, EMP_RUT);
	}

	/** Get EMP_RUT.
		@return EMP_RUT	  */
	public String getEMP_RUT () 
	{
		return (String)get_Value(COLUMNNAME_EMP_RUT);
	}

	/** EST_CIVIL AD_Reference_ID=1000118 */
	public static final int EST_CIVIL_AD_Reference_ID=1000118;
	/** Casado = CA */
	public static final String EST_CIVIL_Casado = "CA";
	/** Separado = SE */
	public static final String EST_CIVIL_Separado = "SE";
	/** Soltero = SO */
	public static final String EST_CIVIL_Soltero = "SO";
	/** Viudo = VI */
	public static final String EST_CIVIL_Viudo = "VI";
	/** Set EST_CIVIL.
		@param EST_CIVIL EST_CIVIL	  */
	public void setEST_CIVIL (String EST_CIVIL)
	{

		set_Value (COLUMNNAME_EST_CIVIL, EST_CIVIL);
	}

	/** Get EST_CIVIL.
		@return EST_CIVIL	  */
	public String getEST_CIVIL () 
	{
		return (String)get_Value(COLUMNNAME_EST_CIVIL);
	}

	/** Set FEC_CRE.
		@param FEC_CRE FEC_CRE	  */
	public void setFEC_CRE (Timestamp FEC_CRE)
	{
		set_Value (COLUMNNAME_FEC_CRE, FEC_CRE);
	}

	/** Get FEC_CRE.
		@return FEC_CRE	  */
	public Timestamp getFEC_CRE () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FEC_CRE);
	}

	/** Set FEC_NAC.
		@param FEC_NAC FEC_NAC	  */
	public void setFEC_NAC (Timestamp FEC_NAC)
	{
		set_Value (COLUMNNAME_FEC_NAC, FEC_NAC);
	}

	/** Get FEC_NAC.
		@return FEC_NAC	  */
	public Timestamp getFEC_NAC () 
	{
		return (Timestamp)get_Value(COLUMNNAME_FEC_NAC);
	}

	/** Set FILLER1.
		@param FILLER1 FILLER1	  */
	public void setFILLER1 (String FILLER1)
	{
		set_Value (COLUMNNAME_FILLER1, FILLER1);
	}

	/** Get FILLER1.
		@return FILLER1	  */
	public String getFILLER1 () 
	{
		return (String)get_Value(COLUMNNAME_FILLER1);
	}

	/** Set FILLER2.
		@param FILLER2 FILLER2	  */
	public void setFILLER2 (String FILLER2)
	{
		set_Value (COLUMNNAME_FILLER2, FILLER2);
	}

	/** Get FILLER2.
		@return FILLER2	  */
	public String getFILLER2 () 
	{
		return (String)get_Value(COLUMNNAME_FILLER2);
	}

	/** Set FILLER3.
		@param FILLER3 FILLER3	  */
	public void setFILLER3 (int FILLER3)
	{
		set_Value (COLUMNNAME_FILLER3, Integer.valueOf(FILLER3));
	}

	/** Get FILLER3.
		@return FILLER3	  */
	public int getFILLER3 () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_FILLER3);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set FONO_AREA.
		@param FONO_AREA FONO_AREA	  */
	public void setFONO_AREA (String FONO_AREA)
	{
		set_Value (COLUMNNAME_FONO_AREA, FONO_AREA);
	}

	/** Get FONO_AREA.
		@return FONO_AREA	  */
	public String getFONO_AREA () 
	{
		return (String)get_Value(COLUMNNAME_FONO_AREA);
	}

	/** Set I_BPartnerIM_ID.
		@param I_BPartnerIM_ID I_BPartnerIM_ID	  */
	public void setI_BPartnerIM_ID (int I_BPartnerIM_ID)
	{
		if (I_BPartnerIM_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_I_BPartnerIM_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_I_BPartnerIM_ID, Integer.valueOf(I_BPartnerIM_ID));
	}

	/** Get I_BPartnerIM_ID.
		@return I_BPartnerIM_ID	  */
	public int getI_BPartnerIM_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_I_BPartnerIM_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** LEADTYPE AD_Reference_ID=1000119 */
	public static final int LEADTYPE_AD_Reference_ID=1000119;
	/** Agencia = AG */
	public static final String LEADTYPE_Agencia = "AG";
	/** Creditos de Consumo = CC */
	public static final String LEADTYPE_CreditosDeConsumo = "CC";
	/** DM/DTC = DD */
	public static final String LEADTYPE_DMDTC = "DD";
	/** Rentas Vitalicias = RV */
	public static final String LEADTYPE_RentasVitalicias = "RV";
	/** Seguros Colectivos = SC */
	public static final String LEADTYPE_SegurosColectivos = "SC";
	/** Set LEADTYPE.
		@param LEADTYPE LEADTYPE	  */
	public void setLEADTYPE (String LEADTYPE)
	{

		set_Value (COLUMNNAME_LEADTYPE, LEADTYPE);
	}

	/** Get LEADTYPE.
		@return LEADTYPE	  */
	public String getLEADTYPE () 
	{
		return (String)get_Value(COLUMNNAME_LEADTYPE);
	}

	/** Set NOM_SOL.
		@param NOM_SOL NOM_SOL	  */
	public void setNOM_SOL (String NOM_SOL)
	{
		set_Value (COLUMNNAME_NOM_SOL, NOM_SOL);
	}

	/** Get NOM_SOL.
		@return NOM_SOL	  */
	public String getNOM_SOL () 
	{
		return (String)get_Value(COLUMNNAME_NOM_SOL);
	}

	/** Set OBS_SOL.
		@param OBS_SOL OBS_SOL	  */
	public void setOBS_SOL (String OBS_SOL)
	{
		set_Value (COLUMNNAME_OBS_SOL, OBS_SOL);
	}

	/** Get OBS_SOL.
		@return OBS_SOL	  */
	public String getOBS_SOL () 
	{
		return (String)get_Value(COLUMNNAME_OBS_SOL);
	}

	/** Set ORI_SOL.
		@param ORI_SOL ORI_SOL	  */
	public void setORI_SOL (String ORI_SOL)
	{
		set_Value (COLUMNNAME_ORI_SOL, ORI_SOL);
	}

	/** Get ORI_SOL.
		@return ORI_SOL	  */
	public String getORI_SOL () 
	{
		return (String)get_Value(COLUMNNAME_ORI_SOL);
	}

	/** Set PROD_INT1.
		@param PROD_INT1 PROD_INT1	  */
	public void setPROD_INT1 (String PROD_INT1)
	{
		set_Value (COLUMNNAME_PROD_INT1, PROD_INT1);
	}

	/** Get PROD_INT1.
		@return PROD_INT1	  */
	public String getPROD_INT1 () 
	{
		return (String)get_Value(COLUMNNAME_PROD_INT1);
	}

	/** Set PROD_INT2.
		@param PROD_INT2 PROD_INT2	  */
	public void setPROD_INT2 (String PROD_INT2)
	{
		set_Value (COLUMNNAME_PROD_INT2, PROD_INT2);
	}

	/** Get PROD_INT2.
		@return PROD_INT2	  */
	public String getPROD_INT2 () 
	{
		return (String)get_Value(COLUMNNAME_PROD_INT2);
	}

	/** Set RAZON_SOC.
		@param RAZON_SOC RAZON_SOC	  */
	public void setRAZON_SOC (String RAZON_SOC)
	{
		set_Value (COLUMNNAME_RAZON_SOC, RAZON_SOC);
	}

	/** Get RAZON_SOC.
		@return RAZON_SOC	  */
	public String getRAZON_SOC () 
	{
		return (String)get_Value(COLUMNNAME_RAZON_SOC);
	}

	/** Set REG_SOL.
		@param REG_SOL REG_SOL	  */
	public void setREG_SOL (String REG_SOL)
	{
		set_Value (COLUMNNAME_REG_SOL, REG_SOL);
	}

	/** Get REG_SOL.
		@return REG_SOL	  */
	public String getREG_SOL () 
	{
		return (String)get_Value(COLUMNNAME_REG_SOL);
	}

	/** Set RUT_SOL.
		@param RUT_SOL RUT_SOL	  */
	public void setRUT_SOL (String RUT_SOL)
	{
		set_Value (COLUMNNAME_RUT_SOL, RUT_SOL);
	}

	/** Get RUT_SOL.
		@return RUT_SOL	  */
	public String getRUT_SOL () 
	{
		return (String)get_Value(COLUMNNAME_RUT_SOL);
	}

	/** SEXO AD_Reference_ID=1000117 */
	public static final int SEXO_AD_Reference_ID=1000117;
	/** Female = F */
	public static final String SEXO_Female = "F";
	/** Male = M */
	public static final String SEXO_Male = "M";
	/** Set SEXO.
		@param SEXO SEXO	  */
	public void setSEXO (String SEXO)
	{

		set_Value (COLUMNNAME_SEXO, SEXO);
	}

	/** Get SEXO.
		@return SEXO	  */
	public String getSEXO () 
	{
		return (String)get_Value(COLUMNNAME_SEXO);
	}

	/** Set SOL_INM_SOL.
		@param SOL_INM_SOL SOL_INM_SOL	  */
	public void setSOL_INM_SOL (String SOL_INM_SOL)
	{
		set_Value (COLUMNNAME_SOL_INM_SOL, SOL_INM_SOL);
	}

	/** Get SOL_INM_SOL.
		@return SOL_INM_SOL	  */
	public String getSOL_INM_SOL () 
	{
		return (String)get_Value(COLUMNNAME_SOL_INM_SOL);
	}

	/** Set SOL_MON_CRED.
		@param SOL_MON_CRED SOL_MON_CRED	  */
	public void setSOL_MON_CRED (int SOL_MON_CRED)
	{
		set_Value (COLUMNNAME_SOL_MON_CRED, Integer.valueOf(SOL_MON_CRED));
	}

	/** Get SOL_MON_CRED.
		@return SOL_MON_CRED	  */
	public int getSOL_MON_CRED () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SOL_MON_CRED);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SOL_PORC_FIN.
		@param SOL_PORC_FIN SOL_PORC_FIN	  */
	public void setSOL_PORC_FIN (int SOL_PORC_FIN)
	{
		set_Value (COLUMNNAME_SOL_PORC_FIN, Integer.valueOf(SOL_PORC_FIN));
	}

	/** Get SOL_PORC_FIN.
		@return SOL_PORC_FIN	  */
	public int getSOL_PORC_FIN () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SOL_PORC_FIN);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SOL_TIP_PROP.
		@param SOL_TIP_PROP SOL_TIP_PROP	  */
	public void setSOL_TIP_PROP (String SOL_TIP_PROP)
	{
		set_Value (COLUMNNAME_SOL_TIP_PROP, SOL_TIP_PROP);
	}

	/** Get SOL_TIP_PROP.
		@return SOL_TIP_PROP	  */
	public String getSOL_TIP_PROP () 
	{
		return (String)get_Value(COLUMNNAME_SOL_TIP_PROP);
	}

	/** Set SOL_VAL_PROP.
		@param SOL_VAL_PROP SOL_VAL_PROP	  */
	public void setSOL_VAL_PROP (int SOL_VAL_PROP)
	{
		set_Value (COLUMNNAME_SOL_VAL_PROP, Integer.valueOf(SOL_VAL_PROP));
	}

	/** Get SOL_VAL_PROP.
		@return SOL_VAL_PROP	  */
	public int getSOL_VAL_PROP () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SOL_VAL_PROP);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TEL_CEL.
		@param TEL_CEL TEL_CEL	  */
	public void setTEL_CEL (String TEL_CEL)
	{	
		set_Value (COLUMNNAME_TEL_CEL, TEL_CEL);
	}

	/** Get TEL_CEL.
		@return TEL_CEL	  */
	public String getTEL_CEL () 
	{
		return (String)get_Value(COLUMNNAME_TEL_CEL);
	}

	/** Set TEL_FIJO.
		@param TEL_FIJO TEL_FIJO	  */
	public void setTEL_FIJO (String TEL_FIJO)
	{	
		set_Value (COLUMNNAME_TEL_FIJO, TEL_FIJO);
	}

	/** Get TEL_FIJO.
		@return TEL_FIJO	  */
	public String getTEL_FIJO () 
	{
		return (String)get_Value(COLUMNNAME_TEL_FIJO);		
	}
}