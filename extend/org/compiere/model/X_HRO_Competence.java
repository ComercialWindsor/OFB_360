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
import java.util.Properties;

/** Generated Model for HRO_Competence
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_HRO_Competence extends PO implements I_HRO_Competence, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140930L;

    /** Standard Constructor */
    public X_HRO_Competence (Properties ctx, int HRO_Competence_ID, String trxName)
    {
      super (ctx, HRO_Competence_ID, trxName);
      /** if (HRO_Competence_ID == 0)
        {
			setHR_Job_ID (0);
			setHRO_Competence_ID (0);
        } */
    }

    /** Load Constructor */
    public X_HRO_Competence (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_HRO_Competence[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ca_especifica1n.
		@param ca_especifica1n ca_especifica1n	  */
	public void setca_especifica1n (String ca_especifica1n)
	{
		set_Value (COLUMNNAME_ca_especifica1n, ca_especifica1n);
	}

	/** Get ca_especifica1n.
		@return ca_especifica1n	  */
	public String getca_especifica1n () 
	{
		return (String)get_Value(COLUMNNAME_ca_especifica1n);
	}

	/** ca_especifica1v AD_Reference_ID=1000109 */
	public static final int CA_ESPECIFICA1V_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CA_ESPECIFICA1V_1 = "1";
	/** 2 = 2 */
	public static final String CA_ESPECIFICA1V_2 = "2";
	/** 3 = 3 */
	public static final String CA_ESPECIFICA1V_3 = "3";
	/** 4 = 4 */
	public static final String CA_ESPECIFICA1V_4 = "4";
	/** 5 = 5 */
	public static final String CA_ESPECIFICA1V_5 = "5";
	/** 0 = 0 */
	public static final String CA_ESPECIFICA1V_0 = "0";
	/** Set ca_especifica1v.
		@param ca_especifica1v ca_especifica1v	  */
	public void setca_especifica1v (String ca_especifica1v)
	{

		set_Value (COLUMNNAME_ca_especifica1v, ca_especifica1v);
	}

	/** Get ca_especifica1v.
		@return ca_especifica1v	  */
	public String getca_especifica1v () 
	{
		return (String)get_Value(COLUMNNAME_ca_especifica1v);
	}

	/** Set ca_especifica2n.
		@param ca_especifica2n ca_especifica2n	  */
	public void setca_especifica2n (String ca_especifica2n)
	{
		set_Value (COLUMNNAME_ca_especifica2n, ca_especifica2n);
	}

	/** Get ca_especifica2n.
		@return ca_especifica2n	  */
	public String getca_especifica2n () 
	{
		return (String)get_Value(COLUMNNAME_ca_especifica2n);
	}

	/** ca_especifica2v AD_Reference_ID=1000109 */
	public static final int CA_ESPECIFICA2V_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CA_ESPECIFICA2V_1 = "1";
	/** 2 = 2 */
	public static final String CA_ESPECIFICA2V_2 = "2";
	/** 3 = 3 */
	public static final String CA_ESPECIFICA2V_3 = "3";
	/** 4 = 4 */
	public static final String CA_ESPECIFICA2V_4 = "4";
	/** 5 = 5 */
	public static final String CA_ESPECIFICA2V_5 = "5";
	/** 0 = 0 */
	public static final String CA_ESPECIFICA2V_0 = "0";
	/** Set ca_especifica2v.
		@param ca_especifica2v ca_especifica2v	  */
	public void setca_especifica2v (String ca_especifica2v)
	{

		set_Value (COLUMNNAME_ca_especifica2v, ca_especifica2v);
	}

	/** Get ca_especifica2v.
		@return ca_especifica2v	  */
	public String getca_especifica2v () 
	{
		return (String)get_Value(COLUMNNAME_ca_especifica2v);
	}

	/** Set ca_especifica3n.
		@param ca_especifica3n ca_especifica3n	  */
	public void setca_especifica3n (String ca_especifica3n)
	{
		set_Value (COLUMNNAME_ca_especifica3n, ca_especifica3n);
	}

	/** Get ca_especifica3n.
		@return ca_especifica3n	  */
	public String getca_especifica3n () 
	{
		return (String)get_Value(COLUMNNAME_ca_especifica3n);
	}

	/** ca_especifica3v AD_Reference_ID=1000109 */
	public static final int CA_ESPECIFICA3V_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CA_ESPECIFICA3V_1 = "1";
	/** 2 = 2 */
	public static final String CA_ESPECIFICA3V_2 = "2";
	/** 3 = 3 */
	public static final String CA_ESPECIFICA3V_3 = "3";
	/** 4 = 4 */
	public static final String CA_ESPECIFICA3V_4 = "4";
	/** 5 = 5 */
	public static final String CA_ESPECIFICA3V_5 = "5";
	/** 0 = 0 */
	public static final String CA_ESPECIFICA3V_0 = "0";
	/** Set ca_especifica3v.
		@param ca_especifica3v ca_especifica3v	  */
	public void setca_especifica3v (String ca_especifica3v)
	{

		set_Value (COLUMNNAME_ca_especifica3v, ca_especifica3v);
	}

	/** Get ca_especifica3v.
		@return ca_especifica3v	  */
	public String getca_especifica3v () 
	{
		return (String)get_Value(COLUMNNAME_ca_especifica3v);
	}

	/** CI_BusInformacion AD_Reference_ID=1000109 */
	public static final int CI_BUSINFORMACION_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CI_BUSINFORMACION_1 = "1";
	/** 2 = 2 */
	public static final String CI_BUSINFORMACION_2 = "2";
	/** 3 = 3 */
	public static final String CI_BUSINFORMACION_3 = "3";
	/** 4 = 4 */
	public static final String CI_BUSINFORMACION_4 = "4";
	/** 5 = 5 */
	public static final String CI_BUSINFORMACION_5 = "5";
	/** 0 = 0 */
	public static final String CI_BUSINFORMACION_0 = "0";
	/** Set CI_BusInformacion.
		@param CI_BusInformacion CI_BusInformacion	  */
	public void setCI_BusInformacion (String CI_BusInformacion)
	{

		set_Value (COLUMNNAME_CI_BusInformacion, CI_BusInformacion);
	}

	/** Get CI_BusInformacion.
		@return CI_BusInformacion	  */
	public String getCI_BusInformacion () 
	{
		return (String)get_Value(COLUMNNAME_CI_BusInformacion);
	}

	/** CI_PAbstracto AD_Reference_ID=1000109 */
	public static final int CI_PABSTRACTO_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CI_PABSTRACTO_1 = "1";
	/** 2 = 2 */
	public static final String CI_PABSTRACTO_2 = "2";
	/** 3 = 3 */
	public static final String CI_PABSTRACTO_3 = "3";
	/** 4 = 4 */
	public static final String CI_PABSTRACTO_4 = "4";
	/** 5 = 5 */
	public static final String CI_PABSTRACTO_5 = "5";
	/** 0 = 0 */
	public static final String CI_PABSTRACTO_0 = "0";
	/** Set CI_PAbstracto.
		@param CI_PAbstracto CI_PAbstracto	  */
	public void setCI_PAbstracto (String CI_PAbstracto)
	{

		set_Value (COLUMNNAME_CI_PAbstracto, CI_PAbstracto);
	}

	/** Get CI_PAbstracto.
		@return CI_PAbstracto	  */
	public String getCI_PAbstracto () 
	{
		return (String)get_Value(COLUMNNAME_CI_PAbstracto);
	}

	/** CI_PAnalitico AD_Reference_ID=1000109 */
	public static final int CI_PANALITICO_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CI_PANALITICO_1 = "1";
	/** 2 = 2 */
	public static final String CI_PANALITICO_2 = "2";
	/** 3 = 3 */
	public static final String CI_PANALITICO_3 = "3";
	/** 4 = 4 */
	public static final String CI_PANALITICO_4 = "4";
	/** 5 = 5 */
	public static final String CI_PANALITICO_5 = "5";
	/** 0 = 0 */
	public static final String CI_PANALITICO_0 = "0";
	/** Set CI_PAnalitico.
		@param CI_PAnalitico CI_PAnalitico	  */
	public void setCI_PAnalitico (String CI_PAnalitico)
	{

		set_Value (COLUMNNAME_CI_PAnalitico, CI_PAnalitico);
	}

	/** Get CI_PAnalitico.
		@return CI_PAnalitico	  */
	public String getCI_PAnalitico () 
	{
		return (String)get_Value(COLUMNNAME_CI_PAnalitico);
	}

	/** CI_PCreativo AD_Reference_ID=1000109 */
	public static final int CI_PCREATIVO_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CI_PCREATIVO_1 = "1";
	/** 2 = 2 */
	public static final String CI_PCREATIVO_2 = "2";
	/** 3 = 3 */
	public static final String CI_PCREATIVO_3 = "3";
	/** 4 = 4 */
	public static final String CI_PCREATIVO_4 = "4";
	/** 5 = 5 */
	public static final String CI_PCREATIVO_5 = "5";
	/** 0 = 0 */
	public static final String CI_PCREATIVO_0 = "0";
	/** Set CI_PCreativo.
		@param CI_PCreativo CI_PCreativo	  */
	public void setCI_PCreativo (String CI_PCreativo)
	{

		set_Value (COLUMNNAME_CI_PCreativo, CI_PCreativo);
	}

	/** Get CI_PCreativo.
		@return CI_PCreativo	  */
	public String getCI_PCreativo () 
	{
		return (String)get_Value(COLUMNNAME_CI_PCreativo);
	}

	/** CI_RaAprendizaje AD_Reference_ID=1000109 */
	public static final int CI_RAAPRENDIZAJE_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CI_RAAPRENDIZAJE_1 = "1";
	/** 2 = 2 */
	public static final String CI_RAAPRENDIZAJE_2 = "2";
	/** 3 = 3 */
	public static final String CI_RAAPRENDIZAJE_3 = "3";
	/** 4 = 4 */
	public static final String CI_RAAPRENDIZAJE_4 = "4";
	/** 5 = 5 */
	public static final String CI_RAAPRENDIZAJE_5 = "5";
	/** 0 = 0 */
	public static final String CI_RAAPRENDIZAJE_0 = "0";
	/** Set CI_RaAprendizaje.
		@param CI_RaAprendizaje CI_RaAprendizaje	  */
	public void setCI_RaAprendizaje (String CI_RaAprendizaje)
	{

		set_Value (COLUMNNAME_CI_RaAprendizaje, CI_RaAprendizaje);
	}

	/** Get CI_RaAprendizaje.
		@return CI_RaAprendizaje	  */
	public String getCI_RaAprendizaje () 
	{
		return (String)get_Value(COLUMNNAME_CI_RaAprendizaje);
	}

	/** CIP_Argumentacion AD_Reference_ID=1000109 */
	public static final int CIP_ARGUMENTACION_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CIP_ARGUMENTACION_1 = "1";
	/** 2 = 2 */
	public static final String CIP_ARGUMENTACION_2 = "2";
	/** 3 = 3 */
	public static final String CIP_ARGUMENTACION_3 = "3";
	/** 4 = 4 */
	public static final String CIP_ARGUMENTACION_4 = "4";
	/** 5 = 5 */
	public static final String CIP_ARGUMENTACION_5 = "5";
	/** 0 = 0 */
	public static final String CIP_ARGUMENTACION_0 = "0";
	/** Set CIP_Argumentacion.
		@param CIP_Argumentacion CIP_Argumentacion	  */
	public void setCIP_Argumentacion (String CIP_Argumentacion)
	{

		set_Value (COLUMNNAME_CIP_Argumentacion, CIP_Argumentacion);
	}

	/** Get CIP_Argumentacion.
		@return CIP_Argumentacion	  */
	public String getCIP_Argumentacion () 
	{
		return (String)get_Value(COLUMNNAME_CIP_Argumentacion);
	}

	/** CIP_Autocritica AD_Reference_ID=1000109 */
	public static final int CIP_AUTOCRITICA_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CIP_AUTOCRITICA_1 = "1";
	/** 2 = 2 */
	public static final String CIP_AUTOCRITICA_2 = "2";
	/** 3 = 3 */
	public static final String CIP_AUTOCRITICA_3 = "3";
	/** 4 = 4 */
	public static final String CIP_AUTOCRITICA_4 = "4";
	/** 5 = 5 */
	public static final String CIP_AUTOCRITICA_5 = "5";
	/** 0 = 0 */
	public static final String CIP_AUTOCRITICA_0 = "0";
	/** Set CIP_Autocritica.
		@param CIP_Autocritica CIP_Autocritica	  */
	public void setCIP_Autocritica (String CIP_Autocritica)
	{

		set_Value (COLUMNNAME_CIP_Autocritica, CIP_Autocritica);
	}

	/** Get CIP_Autocritica.
		@return CIP_Autocritica	  */
	public String getCIP_Autocritica () 
	{
		return (String)get_Value(COLUMNNAME_CIP_Autocritica);
	}

	/** CIP_Autonomia AD_Reference_ID=1000109 */
	public static final int CIP_AUTONOMIA_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CIP_AUTONOMIA_1 = "1";
	/** 2 = 2 */
	public static final String CIP_AUTONOMIA_2 = "2";
	/** 3 = 3 */
	public static final String CIP_AUTONOMIA_3 = "3";
	/** 4 = 4 */
	public static final String CIP_AUTONOMIA_4 = "4";
	/** 5 = 5 */
	public static final String CIP_AUTONOMIA_5 = "5";
	/** 0 = 0 */
	public static final String CIP_AUTONOMIA_0 = "0";
	/** Set CIP_Autonomia.
		@param CIP_Autonomia CIP_Autonomia	  */
	public void setCIP_Autonomia (String CIP_Autonomia)
	{

		set_Value (COLUMNNAME_CIP_Autonomia, CIP_Autonomia);
	}

	/** Get CIP_Autonomia.
		@return CIP_Autonomia	  */
	public String getCIP_Autonomia () 
	{
		return (String)get_Value(COLUMNNAME_CIP_Autonomia);
	}

	/** CIP_CapInstrucciones AD_Reference_ID=1000109 */
	public static final int CIP_CAPINSTRUCCIONES_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CIP_CAPINSTRUCCIONES_1 = "1";
	/** 2 = 2 */
	public static final String CIP_CAPINSTRUCCIONES_2 = "2";
	/** 3 = 3 */
	public static final String CIP_CAPINSTRUCCIONES_3 = "3";
	/** 4 = 4 */
	public static final String CIP_CAPINSTRUCCIONES_4 = "4";
	/** 5 = 5 */
	public static final String CIP_CAPINSTRUCCIONES_5 = "5";
	/** 0 = 0 */
	public static final String CIP_CAPINSTRUCCIONES_0 = "0";
	/** Set CIP_CapInstrucciones.
		@param CIP_CapInstrucciones CIP_CapInstrucciones	  */
	public void setCIP_CapInstrucciones (String CIP_CapInstrucciones)
	{

		set_Value (COLUMNNAME_CIP_CapInstrucciones, CIP_CapInstrucciones);
	}

	/** Get CIP_CapInstrucciones.
		@return CIP_CapInstrucciones	  */
	public String getCIP_CapInstrucciones () 
	{
		return (String)get_Value(COLUMNNAME_CIP_CapInstrucciones);
	}

	/** CIP_DireccionEq AD_Reference_ID=1000109 */
	public static final int CIP_DIRECCIONEQ_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CIP_DIRECCIONEQ_1 = "1";
	/** 2 = 2 */
	public static final String CIP_DIRECCIONEQ_2 = "2";
	/** 3 = 3 */
	public static final String CIP_DIRECCIONEQ_3 = "3";
	/** 4 = 4 */
	public static final String CIP_DIRECCIONEQ_4 = "4";
	/** 5 = 5 */
	public static final String CIP_DIRECCIONEQ_5 = "5";
	/** 0 = 0 */
	public static final String CIP_DIRECCIONEQ_0 = "0";
	/** Set CIP_DireccionEq.
		@param CIP_DireccionEq CIP_DireccionEq	  */
	public void setCIP_DireccionEq (String CIP_DireccionEq)
	{

		set_Value (COLUMNNAME_CIP_DireccionEq, CIP_DireccionEq);
	}

	/** Get CIP_DireccionEq.
		@return CIP_DireccionEq	  */
	public String getCIP_DireccionEq () 
	{
		return (String)get_Value(COLUMNNAME_CIP_DireccionEq);
	}

	/** CIP_Empatia AD_Reference_ID=1000109 */
	public static final int CIP_EMPATIA_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CIP_EMPATIA_1 = "1";
	/** 2 = 2 */
	public static final String CIP_EMPATIA_2 = "2";
	/** 3 = 3 */
	public static final String CIP_EMPATIA_3 = "3";
	/** 4 = 4 */
	public static final String CIP_EMPATIA_4 = "4";
	/** 5 = 5 */
	public static final String CIP_EMPATIA_5 = "5";
	/** 0 = 0 */
	public static final String CIP_EMPATIA_0 = "0";
	/** Set CIP_Empatia.
		@param CIP_Empatia CIP_Empatia	  */
	public void setCIP_Empatia (String CIP_Empatia)
	{

		set_Value (COLUMNNAME_CIP_Empatia, CIP_Empatia);
	}

	/** Get CIP_Empatia.
		@return CIP_Empatia	  */
	public String getCIP_Empatia () 
	{
		return (String)get_Value(COLUMNNAME_CIP_Empatia);
	}

	/** CIP_ExpOral AD_Reference_ID=1000109 */
	public static final int CIP_EXPORAL_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CIP_EXPORAL_1 = "1";
	/** 2 = 2 */
	public static final String CIP_EXPORAL_2 = "2";
	/** 3 = 3 */
	public static final String CIP_EXPORAL_3 = "3";
	/** 4 = 4 */
	public static final String CIP_EXPORAL_4 = "4";
	/** 5 = 5 */
	public static final String CIP_EXPORAL_5 = "5";
	/** 0 = 0 */
	public static final String CIP_EXPORAL_0 = "0";
	/** Set CIP_ExpOral.
		@param CIP_ExpOral CIP_ExpOral	  */
	public void setCIP_ExpOral (String CIP_ExpOral)
	{

		set_Value (COLUMNNAME_CIP_ExpOral, CIP_ExpOral);
	}

	/** Get CIP_ExpOral.
		@return CIP_ExpOral	  */
	public String getCIP_ExpOral () 
	{
		return (String)get_Value(COLUMNNAME_CIP_ExpOral);
	}

	/** CIP_IntegrarEq AD_Reference_ID=1000109 */
	public static final int CIP_INTEGRAREQ_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CIP_INTEGRAREQ_1 = "1";
	/** 2 = 2 */
	public static final String CIP_INTEGRAREQ_2 = "2";
	/** 3 = 3 */
	public static final String CIP_INTEGRAREQ_3 = "3";
	/** 4 = 4 */
	public static final String CIP_INTEGRAREQ_4 = "4";
	/** 5 = 5 */
	public static final String CIP_INTEGRAREQ_5 = "5";
	/** 0 = 0 */
	public static final String CIP_INTEGRAREQ_0 = "0";
	/** Set CIP_IntegrarEq.
		@param CIP_IntegrarEq CIP_IntegrarEq	  */
	public void setCIP_IntegrarEq (String CIP_IntegrarEq)
	{

		set_Value (COLUMNNAME_CIP_IntegrarEq, CIP_IntegrarEq);
	}

	/** Get CIP_IntegrarEq.
		@return CIP_IntegrarEq	  */
	public String getCIP_IntegrarEq () 
	{
		return (String)get_Value(COLUMNNAME_CIP_IntegrarEq);
	}

	/** CIP_Liderazgo AD_Reference_ID=1000109 */
	public static final int CIP_LIDERAZGO_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CIP_LIDERAZGO_1 = "1";
	/** 2 = 2 */
	public static final String CIP_LIDERAZGO_2 = "2";
	/** 3 = 3 */
	public static final String CIP_LIDERAZGO_3 = "3";
	/** 4 = 4 */
	public static final String CIP_LIDERAZGO_4 = "4";
	/** 5 = 5 */
	public static final String CIP_LIDERAZGO_5 = "5";
	/** 0 = 0 */
	public static final String CIP_LIDERAZGO_0 = "0";
	/** Set CIP_Liderazgo.
		@param CIP_Liderazgo CIP_Liderazgo	  */
	public void setCIP_Liderazgo (String CIP_Liderazgo)
	{

		set_Value (COLUMNNAME_CIP_Liderazgo, CIP_Liderazgo);
	}

	/** Get CIP_Liderazgo.
		@return CIP_Liderazgo	  */
	public String getCIP_Liderazgo () 
	{
		return (String)get_Value(COLUMNNAME_CIP_Liderazgo);
	}

	/** CIP_ToCritica AD_Reference_ID=1000109 */
	public static final int CIP_TOCRITICA_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CIP_TOCRITICA_1 = "1";
	/** 2 = 2 */
	public static final String CIP_TOCRITICA_2 = "2";
	/** 3 = 3 */
	public static final String CIP_TOCRITICA_3 = "3";
	/** 4 = 4 */
	public static final String CIP_TOCRITICA_4 = "4";
	/** 5 = 5 */
	public static final String CIP_TOCRITICA_5 = "5";
	/** 0 = 0 */
	public static final String CIP_TOCRITICA_0 = "0";
	/** Set CIP_ToCritica.
		@param CIP_ToCritica CIP_ToCritica	  */
	public void setCIP_ToCritica (String CIP_ToCritica)
	{

		set_Value (COLUMNNAME_CIP_ToCritica, CIP_ToCritica);
	}

	/** Get CIP_ToCritica.
		@return CIP_ToCritica	  */
	public String getCIP_ToCritica () 
	{
		return (String)get_Value(COLUMNNAME_CIP_ToCritica);
	}

	/** CL_AdapNormas AD_Reference_ID=1000109 */
	public static final int CL_ADAPNORMAS_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_ADAPNORMAS_1 = "1";
	/** 2 = 2 */
	public static final String CL_ADAPNORMAS_2 = "2";
	/** 3 = 3 */
	public static final String CL_ADAPNORMAS_3 = "3";
	/** 4 = 4 */
	public static final String CL_ADAPNORMAS_4 = "4";
	/** 5 = 5 */
	public static final String CL_ADAPNORMAS_5 = "5";
	/** 0 = 0 */
	public static final String CL_ADAPNORMAS_0 = "0";
	/** Set CL_AdapNormas.
		@param CL_AdapNormas CL_AdapNormas	  */
	public void setCL_AdapNormas (String CL_AdapNormas)
	{

		set_Value (COLUMNNAME_CL_AdapNormas, CL_AdapNormas);
	}

	/** Get CL_AdapNormas.
		@return CL_AdapNormas	  */
	public String getCL_AdapNormas () 
	{
		return (String)get_Value(COLUMNNAME_CL_AdapNormas);
	}

	/** CL_Desicion AD_Reference_ID=1000109 */
	public static final int CL_DESICION_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_DESICION_1 = "1";
	/** 2 = 2 */
	public static final String CL_DESICION_2 = "2";
	/** 3 = 3 */
	public static final String CL_DESICION_3 = "3";
	/** 4 = 4 */
	public static final String CL_DESICION_4 = "4";
	/** 5 = 5 */
	public static final String CL_DESICION_5 = "5";
	/** 0 = 0 */
	public static final String CL_DESICION_0 = "0";
	/** Set CL_Desicion.
		@param CL_Desicion CL_Desicion	  */
	public void setCL_Desicion (String CL_Desicion)
	{

		set_Value (COLUMNNAME_CL_Desicion, CL_Desicion);
	}

	/** Get CL_Desicion.
		@return CL_Desicion	  */
	public String getCL_Desicion () 
	{
		return (String)get_Value(COLUMNNAME_CL_Desicion);
	}

	/** CL_Dinamismo AD_Reference_ID=1000109 */
	public static final int CL_DINAMISMO_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_DINAMISMO_1 = "1";
	/** 2 = 2 */
	public static final String CL_DINAMISMO_2 = "2";
	/** 3 = 3 */
	public static final String CL_DINAMISMO_3 = "3";
	/** 4 = 4 */
	public static final String CL_DINAMISMO_4 = "4";
	/** 5 = 5 */
	public static final String CL_DINAMISMO_5 = "5";
	/** 0 = 0 */
	public static final String CL_DINAMISMO_0 = "0";
	/** Set CL_Dinamismo.
		@param CL_Dinamismo CL_Dinamismo	  */
	public void setCL_Dinamismo (String CL_Dinamismo)
	{

		set_Value (COLUMNNAME_CL_Dinamismo, CL_Dinamismo);
	}

	/** Get CL_Dinamismo.
		@return CL_Dinamismo	  */
	public String getCL_Dinamismo () 
	{
		return (String)get_Value(COLUMNNAME_CL_Dinamismo);
	}

	/** CL_Ejecucion AD_Reference_ID=1000109 */
	public static final int CL_EJECUCION_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_EJECUCION_1 = "1";
	/** 2 = 2 */
	public static final String CL_EJECUCION_2 = "2";
	/** 3 = 3 */
	public static final String CL_EJECUCION_3 = "3";
	/** 4 = 4 */
	public static final String CL_EJECUCION_4 = "4";
	/** 5 = 5 */
	public static final String CL_EJECUCION_5 = "5";
	/** 0 = 0 */
	public static final String CL_EJECUCION_0 = "0";
	/** Set CL_Ejecucion.
		@param CL_Ejecucion CL_Ejecucion	  */
	public void setCL_Ejecucion (String CL_Ejecucion)
	{

		set_Value (COLUMNNAME_CL_Ejecucion, CL_Ejecucion);
	}

	/** Get CL_Ejecucion.
		@return CL_Ejecucion	  */
	public String getCL_Ejecucion () 
	{
		return (String)get_Value(COLUMNNAME_CL_Ejecucion);
	}

	/** CL_Emprendimiento AD_Reference_ID=1000109 */
	public static final int CL_EMPRENDIMIENTO_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_EMPRENDIMIENTO_1 = "1";
	/** 2 = 2 */
	public static final String CL_EMPRENDIMIENTO_2 = "2";
	/** 3 = 3 */
	public static final String CL_EMPRENDIMIENTO_3 = "3";
	/** 4 = 4 */
	public static final String CL_EMPRENDIMIENTO_4 = "4";
	/** 5 = 5 */
	public static final String CL_EMPRENDIMIENTO_5 = "5";
	/** 0 = 0 */
	public static final String CL_EMPRENDIMIENTO_0 = "0";
	/** Set CL_Emprendimiento.
		@param CL_Emprendimiento CL_Emprendimiento	  */
	public void setCL_Emprendimiento (String CL_Emprendimiento)
	{

		set_Value (COLUMNNAME_CL_Emprendimiento, CL_Emprendimiento);
	}

	/** Get CL_Emprendimiento.
		@return CL_Emprendimiento	  */
	public String getCL_Emprendimiento () 
	{
		return (String)get_Value(COLUMNNAME_CL_Emprendimiento);
	}

	/** CL_EstCalidad AD_Reference_ID=1000109 */
	public static final int CL_ESTCALIDAD_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_ESTCALIDAD_1 = "1";
	/** 2 = 2 */
	public static final String CL_ESTCALIDAD_2 = "2";
	/** 3 = 3 */
	public static final String CL_ESTCALIDAD_3 = "3";
	/** 4 = 4 */
	public static final String CL_ESTCALIDAD_4 = "4";
	/** 5 = 5 */
	public static final String CL_ESTCALIDAD_5 = "5";
	/** 0 = 0 */
	public static final String CL_ESTCALIDAD_0 = "0";
	/** Set CL_EstCalidad.
		@param CL_EstCalidad CL_EstCalidad	  */
	public void setCL_EstCalidad (String CL_EstCalidad)
	{

		set_Value (COLUMNNAME_CL_EstCalidad, CL_EstCalidad);
	}

	/** Get CL_EstCalidad.
		@return CL_EstCalidad	  */
	public String getCL_EstCalidad () 
	{
		return (String)get_Value(COLUMNNAME_CL_EstCalidad);
	}

	/** CL_Flexibilidad AD_Reference_ID=1000109 */
	public static final int CL_FLEXIBILIDAD_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_FLEXIBILIDAD_1 = "1";
	/** 2 = 2 */
	public static final String CL_FLEXIBILIDAD_2 = "2";
	/** 3 = 3 */
	public static final String CL_FLEXIBILIDAD_3 = "3";
	/** 4 = 4 */
	public static final String CL_FLEXIBILIDAD_4 = "4";
	/** 5 = 5 */
	public static final String CL_FLEXIBILIDAD_5 = "5";
	/** 0 = 0 */
	public static final String CL_FLEXIBILIDAD_0 = "0";
	/** Set CL_Flexibilidad.
		@param CL_Flexibilidad CL_Flexibilidad	  */
	public void setCL_Flexibilidad (String CL_Flexibilidad)
	{

		set_Value (COLUMNNAME_CL_Flexibilidad, CL_Flexibilidad);
	}

	/** Get CL_Flexibilidad.
		@return CL_Flexibilidad	  */
	public String getCL_Flexibilidad () 
	{
		return (String)get_Value(COLUMNNAME_CL_Flexibilidad);
	}

	/** CL_Integridad AD_Reference_ID=1000109 */
	public static final int CL_INTEGRIDAD_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_INTEGRIDAD_1 = "1";
	/** 2 = 2 */
	public static final String CL_INTEGRIDAD_2 = "2";
	/** 3 = 3 */
	public static final String CL_INTEGRIDAD_3 = "3";
	/** 4 = 4 */
	public static final String CL_INTEGRIDAD_4 = "4";
	/** 5 = 5 */
	public static final String CL_INTEGRIDAD_5 = "5";
	/** 0 = 0 */
	public static final String CL_INTEGRIDAD_0 = "0";
	/** Set CL_Integridad.
		@param CL_Integridad CL_Integridad	  */
	public void setCL_Integridad (String CL_Integridad)
	{

		set_Value (COLUMNNAME_CL_Integridad, CL_Integridad);
	}

	/** Get CL_Integridad.
		@return CL_Integridad	  */
	public String getCL_Integridad () 
	{
		return (String)get_Value(COLUMNNAME_CL_Integridad);
	}

	/** CL_MejoraContinua AD_Reference_ID=1000109 */
	public static final int CL_MEJORACONTINUA_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_MEJORACONTINUA_1 = "1";
	/** 2 = 2 */
	public static final String CL_MEJORACONTINUA_2 = "2";
	/** 3 = 3 */
	public static final String CL_MEJORACONTINUA_3 = "3";
	/** 4 = 4 */
	public static final String CL_MEJORACONTINUA_4 = "4";
	/** 5 = 5 */
	public static final String CL_MEJORACONTINUA_5 = "5";
	/** 0 = 0 */
	public static final String CL_MEJORACONTINUA_0 = "0";
	/** Set CL_MejoraContinua.
		@param CL_MejoraContinua CL_MejoraContinua	  */
	public void setCL_MejoraContinua (String CL_MejoraContinua)
	{

		set_Value (COLUMNNAME_CL_MejoraContinua, CL_MejoraContinua);
	}

	/** Get CL_MejoraContinua.
		@return CL_MejoraContinua	  */
	public String getCL_MejoraContinua () 
	{
		return (String)get_Value(COLUMNNAME_CL_MejoraContinua);
	}

	/** CL_ObjetivosResultados AD_Reference_ID=1000109 */
	public static final int CL_OBJETIVOSRESULTADOS_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_OBJETIVOSRESULTADOS_1 = "1";
	/** 2 = 2 */
	public static final String CL_OBJETIVOSRESULTADOS_2 = "2";
	/** 3 = 3 */
	public static final String CL_OBJETIVOSRESULTADOS_3 = "3";
	/** 4 = 4 */
	public static final String CL_OBJETIVOSRESULTADOS_4 = "4";
	/** 5 = 5 */
	public static final String CL_OBJETIVOSRESULTADOS_5 = "5";
	/** 0 = 0 */
	public static final String CL_OBJETIVOSRESULTADOS_0 = "0";
	/** Set CL_ObjetivosResultados.
		@param CL_ObjetivosResultados CL_ObjetivosResultados	  */
	public void setCL_ObjetivosResultados (String CL_ObjetivosResultados)
	{

		set_Value (COLUMNNAME_CL_ObjetivosResultados, CL_ObjetivosResultados);
	}

	/** Get CL_ObjetivosResultados.
		@return CL_ObjetivosResultados	  */
	public String getCL_ObjetivosResultados () 
	{
		return (String)get_Value(COLUMNNAME_CL_ObjetivosResultados);
	}

	/** CL_Orden AD_Reference_ID=1000109 */
	public static final int CL_ORDEN_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_ORDEN_1 = "1";
	/** 2 = 2 */
	public static final String CL_ORDEN_2 = "2";
	/** 3 = 3 */
	public static final String CL_ORDEN_3 = "3";
	/** 4 = 4 */
	public static final String CL_ORDEN_4 = "4";
	/** 5 = 5 */
	public static final String CL_ORDEN_5 = "5";
	/** 0 = 0 */
	public static final String CL_ORDEN_0 = "0";
	/** Set CL_Orden.
		@param CL_Orden CL_Orden	  */
	public void setCL_Orden (String CL_Orden)
	{

		set_Value (COLUMNNAME_CL_Orden, CL_Orden);
	}

	/** Get CL_Orden.
		@return CL_Orden	  */
	public String getCL_Orden () 
	{
		return (String)get_Value(COLUMNNAME_CL_Orden);
	}

	/** CL_Organizacion AD_Reference_ID=1000109 */
	public static final int CL_ORGANIZACION_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_ORGANIZACION_1 = "1";
	/** 2 = 2 */
	public static final String CL_ORGANIZACION_2 = "2";
	/** 3 = 3 */
	public static final String CL_ORGANIZACION_3 = "3";
	/** 4 = 4 */
	public static final String CL_ORGANIZACION_4 = "4";
	/** 5 = 5 */
	public static final String CL_ORGANIZACION_5 = "5";
	/** 0 = 0 */
	public static final String CL_ORGANIZACION_0 = "0";
	/** Set CL_Organizacion.
		@param CL_Organizacion CL_Organizacion	  */
	public void setCL_Organizacion (String CL_Organizacion)
	{

		set_Value (COLUMNNAME_CL_Organizacion, CL_Organizacion);
	}

	/** Get CL_Organizacion.
		@return CL_Organizacion	  */
	public String getCL_Organizacion () 
	{
		return (String)get_Value(COLUMNNAME_CL_Organizacion);
	}

	/** CL_Perseverancia AD_Reference_ID=1000109 */
	public static final int CL_PERSEVERANCIA_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_PERSEVERANCIA_1 = "1";
	/** 2 = 2 */
	public static final String CL_PERSEVERANCIA_2 = "2";
	/** 3 = 3 */
	public static final String CL_PERSEVERANCIA_3 = "3";
	/** 4 = 4 */
	public static final String CL_PERSEVERANCIA_4 = "4";
	/** 5 = 5 */
	public static final String CL_PERSEVERANCIA_5 = "5";
	/** 0 = 0 */
	public static final String CL_PERSEVERANCIA_0 = "0";
	/** Set CL_Perseverancia.
		@param CL_Perseverancia CL_Perseverancia	  */
	public void setCL_Perseverancia (String CL_Perseverancia)
	{

		set_Value (COLUMNNAME_CL_Perseverancia, CL_Perseverancia);
	}

	/** Get CL_Perseverancia.
		@return CL_Perseverancia	  */
	public String getCL_Perseverancia () 
	{
		return (String)get_Value(COLUMNNAME_CL_Perseverancia);
	}

	/** CL_Planificacion AD_Reference_ID=1000109 */
	public static final int CL_PLANIFICACION_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_PLANIFICACION_1 = "1";
	/** 2 = 2 */
	public static final String CL_PLANIFICACION_2 = "2";
	/** 3 = 3 */
	public static final String CL_PLANIFICACION_3 = "3";
	/** 4 = 4 */
	public static final String CL_PLANIFICACION_4 = "4";
	/** 5 = 5 */
	public static final String CL_PLANIFICACION_5 = "5";
	/** 0 = 0 */
	public static final String CL_PLANIFICACION_0 = "0";
	/** Set CL_Planificacion.
		@param CL_Planificacion CL_Planificacion	  */
	public void setCL_Planificacion (String CL_Planificacion)
	{

		set_Value (COLUMNNAME_CL_Planificacion, CL_Planificacion);
	}

	/** Get CL_Planificacion.
		@return CL_Planificacion	  */
	public String getCL_Planificacion () 
	{
		return (String)get_Value(COLUMNNAME_CL_Planificacion);
	}

	/** CL_Proactividad AD_Reference_ID=1000109 */
	public static final int CL_PROACTIVIDAD_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CL_PROACTIVIDAD_1 = "1";
	/** 2 = 2 */
	public static final String CL_PROACTIVIDAD_2 = "2";
	/** 3 = 3 */
	public static final String CL_PROACTIVIDAD_3 = "3";
	/** 4 = 4 */
	public static final String CL_PROACTIVIDAD_4 = "4";
	/** 5 = 5 */
	public static final String CL_PROACTIVIDAD_5 = "5";
	/** 0 = 0 */
	public static final String CL_PROACTIVIDAD_0 = "0";
	/** Set CL_Proactividad.
		@param CL_Proactividad CL_Proactividad	  */
	public void setCL_Proactividad (String CL_Proactividad)
	{

		set_Value (COLUMNNAME_CL_Proactividad, CL_Proactividad);
	}

	/** Get CL_Proactividad.
		@return CL_Proactividad	  */
	public String getCL_Proactividad () 
	{
		return (String)get_Value(COLUMNNAME_CL_Proactividad);
	}

	/** CP_Adaptabilidad AD_Reference_ID=1000109 */
	public static final int CP_ADAPTABILIDAD_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CP_ADAPTABILIDAD_1 = "1";
	/** 2 = 2 */
	public static final String CP_ADAPTABILIDAD_2 = "2";
	/** 3 = 3 */
	public static final String CP_ADAPTABILIDAD_3 = "3";
	/** 4 = 4 */
	public static final String CP_ADAPTABILIDAD_4 = "4";
	/** 5 = 5 */
	public static final String CP_ADAPTABILIDAD_5 = "5";
	/** 0 = 0 */
	public static final String CP_ADAPTABILIDAD_0 = "0";
	/** Set CP_Adaptabilidad.
		@param CP_Adaptabilidad CP_Adaptabilidad	  */
	public void setCP_Adaptabilidad (String CP_Adaptabilidad)
	{

		set_Value (COLUMNNAME_CP_Adaptabilidad, CP_Adaptabilidad);
	}

	/** Get CP_Adaptabilidad.
		@return CP_Adaptabilidad	  */
	public String getCP_Adaptabilidad () 
	{
		return (String)get_Value(COLUMNNAME_CP_Adaptabilidad);
	}

	/** CP_ControlEmocional AD_Reference_ID=1000109 */
	public static final int CP_CONTROLEMOCIONAL_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CP_CONTROLEMOCIONAL_1 = "1";
	/** 2 = 2 */
	public static final String CP_CONTROLEMOCIONAL_2 = "2";
	/** 3 = 3 */
	public static final String CP_CONTROLEMOCIONAL_3 = "3";
	/** 4 = 4 */
	public static final String CP_CONTROLEMOCIONAL_4 = "4";
	/** 5 = 5 */
	public static final String CP_CONTROLEMOCIONAL_5 = "5";
	/** 0 = 0 */
	public static final String CP_CONTROLEMOCIONAL_0 = "0";
	/** Set CP_ControlEmocional.
		@param CP_ControlEmocional CP_ControlEmocional	  */
	public void setCP_ControlEmocional (String CP_ControlEmocional)
	{

		set_Value (COLUMNNAME_CP_ControlEmocional, CP_ControlEmocional);
	}

	/** Get CP_ControlEmocional.
		@return CP_ControlEmocional	  */
	public String getCP_ControlEmocional () 
	{
		return (String)get_Value(COLUMNNAME_CP_ControlEmocional);
	}

	/** CP_EstabilidadEmocional AD_Reference_ID=1000109 */
	public static final int CP_ESTABILIDADEMOCIONAL_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CP_ESTABILIDADEMOCIONAL_1 = "1";
	/** 2 = 2 */
	public static final String CP_ESTABILIDADEMOCIONAL_2 = "2";
	/** 3 = 3 */
	public static final String CP_ESTABILIDADEMOCIONAL_3 = "3";
	/** 4 = 4 */
	public static final String CP_ESTABILIDADEMOCIONAL_4 = "4";
	/** 5 = 5 */
	public static final String CP_ESTABILIDADEMOCIONAL_5 = "5";
	/** 0 = 0 */
	public static final String CP_ESTABILIDADEMOCIONAL_0 = "0";
	/** Set CP_EstabilidadEmocional.
		@param CP_EstabilidadEmocional CP_EstabilidadEmocional	  */
	public void setCP_EstabilidadEmocional (String CP_EstabilidadEmocional)
	{

		set_Value (COLUMNNAME_CP_EstabilidadEmocional, CP_EstabilidadEmocional);
	}

	/** Get CP_EstabilidadEmocional.
		@return CP_EstabilidadEmocional	  */
	public String getCP_EstabilidadEmocional () 
	{
		return (String)get_Value(COLUMNNAME_CP_EstabilidadEmocional);
	}

	/** CP_Seguridad AD_Reference_ID=1000109 */
	public static final int CP_SEGURIDAD_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CP_SEGURIDAD_1 = "1";
	/** 2 = 2 */
	public static final String CP_SEGURIDAD_2 = "2";
	/** 3 = 3 */
	public static final String CP_SEGURIDAD_3 = "3";
	/** 4 = 4 */
	public static final String CP_SEGURIDAD_4 = "4";
	/** 5 = 5 */
	public static final String CP_SEGURIDAD_5 = "5";
	/** 0 = 0 */
	public static final String CP_SEGURIDAD_0 = "0";
	/** Set CP_Seguridad.
		@param CP_Seguridad CP_Seguridad	  */
	public void setCP_Seguridad (String CP_Seguridad)
	{

		set_Value (COLUMNNAME_CP_Seguridad, CP_Seguridad);
	}

	/** Get CP_Seguridad.
		@return CP_Seguridad	  */
	public String getCP_Seguridad () 
	{
		return (String)get_Value(COLUMNNAME_CP_Seguridad);
	}

	/** CP_Sociabilidad AD_Reference_ID=1000109 */
	public static final int CP_SOCIABILIDAD_AD_Reference_ID=1000109;
	/** 1 = 1 */
	public static final String CP_SOCIABILIDAD_1 = "1";
	/** 2 = 2 */
	public static final String CP_SOCIABILIDAD_2 = "2";
	/** 3 = 3 */
	public static final String CP_SOCIABILIDAD_3 = "3";
	/** 4 = 4 */
	public static final String CP_SOCIABILIDAD_4 = "4";
	/** 5 = 5 */
	public static final String CP_SOCIABILIDAD_5 = "5";
	/** 0 = 0 */
	public static final String CP_SOCIABILIDAD_0 = "0";
	/** Set CP_Sociabilidad.
		@param CP_Sociabilidad CP_Sociabilidad	  */
	public void setCP_Sociabilidad (String CP_Sociabilidad)
	{

		set_Value (COLUMNNAME_CP_Sociabilidad, CP_Sociabilidad);
	}

	/** Get CP_Sociabilidad.
		@return CP_Sociabilidad	  */
	public String getCP_Sociabilidad () 
	{
		return (String)get_Value(COLUMNNAME_CP_Sociabilidad);
	}

	public org.eevolution.model.I_HR_Job getHR_Job() throws RuntimeException
    {
		return (org.eevolution.model.I_HR_Job)MTable.get(getCtx(), org.eevolution.model.I_HR_Job.Table_Name)
			.getPO(getHR_Job_ID(), get_TrxName());	}

	/** Set Payroll Job.
		@param HR_Job_ID Payroll Job	  */
	public void setHR_Job_ID (int HR_Job_ID)
	{
		if (HR_Job_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HR_Job_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HR_Job_ID, Integer.valueOf(HR_Job_ID));
	}

	/** Get Payroll Job.
		@return Payroll Job	  */
	public int getHR_Job_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HR_Job_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set HRO_Competence.
		@param HRO_Competence_ID HRO_Competence	  */
	public void setHRO_Competence_ID (int HRO_Competence_ID)
	{
		if (HRO_Competence_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_HRO_Competence_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_HRO_Competence_ID, Integer.valueOf(HRO_Competence_ID));
	}

	/** Get HRO_Competence.
		@return HRO_Competence	  */
	public int getHRO_Competence_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_HRO_Competence_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}