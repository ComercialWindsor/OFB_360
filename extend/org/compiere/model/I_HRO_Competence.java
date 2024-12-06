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

/** Generated Interface for HRO_Competence
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_HRO_Competence 
{

    /** TableName=HRO_Competence */
    public static final String Table_Name = "HRO_Competence";

    /** AD_Table_ID=1000093 */
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

    /** Column name ca_especifica1n */
    public static final String COLUMNNAME_ca_especifica1n = "ca_especifica1n";

	/** Set ca_especifica1n	  */
	public void setca_especifica1n (String ca_especifica1n);

	/** Get ca_especifica1n	  */
	public String getca_especifica1n();

    /** Column name ca_especifica1v */
    public static final String COLUMNNAME_ca_especifica1v = "ca_especifica1v";

	/** Set ca_especifica1v	  */
	public void setca_especifica1v (String ca_especifica1v);

	/** Get ca_especifica1v	  */
	public String getca_especifica1v();

    /** Column name ca_especifica2n */
    public static final String COLUMNNAME_ca_especifica2n = "ca_especifica2n";

	/** Set ca_especifica2n	  */
	public void setca_especifica2n (String ca_especifica2n);

	/** Get ca_especifica2n	  */
	public String getca_especifica2n();

    /** Column name ca_especifica2v */
    public static final String COLUMNNAME_ca_especifica2v = "ca_especifica2v";

	/** Set ca_especifica2v	  */
	public void setca_especifica2v (String ca_especifica2v);

	/** Get ca_especifica2v	  */
	public String getca_especifica2v();

    /** Column name ca_especifica3n */
    public static final String COLUMNNAME_ca_especifica3n = "ca_especifica3n";

	/** Set ca_especifica3n	  */
	public void setca_especifica3n (String ca_especifica3n);

	/** Get ca_especifica3n	  */
	public String getca_especifica3n();

    /** Column name ca_especifica3v */
    public static final String COLUMNNAME_ca_especifica3v = "ca_especifica3v";

	/** Set ca_especifica3v	  */
	public void setca_especifica3v (String ca_especifica3v);

	/** Get ca_especifica3v	  */
	public String getca_especifica3v();

    /** Column name CI_BusInformacion */
    public static final String COLUMNNAME_CI_BusInformacion = "CI_BusInformacion";

	/** Set CI_BusInformacion	  */
	public void setCI_BusInformacion (String CI_BusInformacion);

	/** Get CI_BusInformacion	  */
	public String getCI_BusInformacion();

    /** Column name CI_PAbstracto */
    public static final String COLUMNNAME_CI_PAbstracto = "CI_PAbstracto";

	/** Set CI_PAbstracto	  */
	public void setCI_PAbstracto (String CI_PAbstracto);

	/** Get CI_PAbstracto	  */
	public String getCI_PAbstracto();

    /** Column name CI_PAnalitico */
    public static final String COLUMNNAME_CI_PAnalitico = "CI_PAnalitico";

	/** Set CI_PAnalitico	  */
	public void setCI_PAnalitico (String CI_PAnalitico);

	/** Get CI_PAnalitico	  */
	public String getCI_PAnalitico();

    /** Column name CI_PCreativo */
    public static final String COLUMNNAME_CI_PCreativo = "CI_PCreativo";

	/** Set CI_PCreativo	  */
	public void setCI_PCreativo (String CI_PCreativo);

	/** Get CI_PCreativo	  */
	public String getCI_PCreativo();

    /** Column name CI_RaAprendizaje */
    public static final String COLUMNNAME_CI_RaAprendizaje = "CI_RaAprendizaje";

	/** Set CI_RaAprendizaje	  */
	public void setCI_RaAprendizaje (String CI_RaAprendizaje);

	/** Get CI_RaAprendizaje	  */
	public String getCI_RaAprendizaje();

    /** Column name CIP_Argumentacion */
    public static final String COLUMNNAME_CIP_Argumentacion = "CIP_Argumentacion";

	/** Set CIP_Argumentacion	  */
	public void setCIP_Argumentacion (String CIP_Argumentacion);

	/** Get CIP_Argumentacion	  */
	public String getCIP_Argumentacion();

    /** Column name CIP_Autocritica */
    public static final String COLUMNNAME_CIP_Autocritica = "CIP_Autocritica";

	/** Set CIP_Autocritica	  */
	public void setCIP_Autocritica (String CIP_Autocritica);

	/** Get CIP_Autocritica	  */
	public String getCIP_Autocritica();

    /** Column name CIP_Autonomia */
    public static final String COLUMNNAME_CIP_Autonomia = "CIP_Autonomia";

	/** Set CIP_Autonomia	  */
	public void setCIP_Autonomia (String CIP_Autonomia);

	/** Get CIP_Autonomia	  */
	public String getCIP_Autonomia();

    /** Column name CIP_CapInstrucciones */
    public static final String COLUMNNAME_CIP_CapInstrucciones = "CIP_CapInstrucciones";

	/** Set CIP_CapInstrucciones	  */
	public void setCIP_CapInstrucciones (String CIP_CapInstrucciones);

	/** Get CIP_CapInstrucciones	  */
	public String getCIP_CapInstrucciones();

    /** Column name CIP_DireccionEq */
    public static final String COLUMNNAME_CIP_DireccionEq = "CIP_DireccionEq";

	/** Set CIP_DireccionEq	  */
	public void setCIP_DireccionEq (String CIP_DireccionEq);

	/** Get CIP_DireccionEq	  */
	public String getCIP_DireccionEq();

    /** Column name CIP_Empatia */
    public static final String COLUMNNAME_CIP_Empatia = "CIP_Empatia";

	/** Set CIP_Empatia	  */
	public void setCIP_Empatia (String CIP_Empatia);

	/** Get CIP_Empatia	  */
	public String getCIP_Empatia();

    /** Column name CIP_ExpOral */
    public static final String COLUMNNAME_CIP_ExpOral = "CIP_ExpOral";

	/** Set CIP_ExpOral	  */
	public void setCIP_ExpOral (String CIP_ExpOral);

	/** Get CIP_ExpOral	  */
	public String getCIP_ExpOral();

    /** Column name CIP_IntegrarEq */
    public static final String COLUMNNAME_CIP_IntegrarEq = "CIP_IntegrarEq";

	/** Set CIP_IntegrarEq	  */
	public void setCIP_IntegrarEq (String CIP_IntegrarEq);

	/** Get CIP_IntegrarEq	  */
	public String getCIP_IntegrarEq();

    /** Column name CIP_Liderazgo */
    public static final String COLUMNNAME_CIP_Liderazgo = "CIP_Liderazgo";

	/** Set CIP_Liderazgo	  */
	public void setCIP_Liderazgo (String CIP_Liderazgo);

	/** Get CIP_Liderazgo	  */
	public String getCIP_Liderazgo();

    /** Column name CIP_ToCritica */
    public static final String COLUMNNAME_CIP_ToCritica = "CIP_ToCritica";

	/** Set CIP_ToCritica	  */
	public void setCIP_ToCritica (String CIP_ToCritica);

	/** Get CIP_ToCritica	  */
	public String getCIP_ToCritica();

    /** Column name CL_AdapNormas */
    public static final String COLUMNNAME_CL_AdapNormas = "CL_AdapNormas";

	/** Set CL_AdapNormas	  */
	public void setCL_AdapNormas (String CL_AdapNormas);

	/** Get CL_AdapNormas	  */
	public String getCL_AdapNormas();

    /** Column name CL_Desicion */
    public static final String COLUMNNAME_CL_Desicion = "CL_Desicion";

	/** Set CL_Desicion	  */
	public void setCL_Desicion (String CL_Desicion);

	/** Get CL_Desicion	  */
	public String getCL_Desicion();

    /** Column name CL_Dinamismo */
    public static final String COLUMNNAME_CL_Dinamismo = "CL_Dinamismo";

	/** Set CL_Dinamismo	  */
	public void setCL_Dinamismo (String CL_Dinamismo);

	/** Get CL_Dinamismo	  */
	public String getCL_Dinamismo();

    /** Column name CL_Ejecucion */
    public static final String COLUMNNAME_CL_Ejecucion = "CL_Ejecucion";

	/** Set CL_Ejecucion	  */
	public void setCL_Ejecucion (String CL_Ejecucion);

	/** Get CL_Ejecucion	  */
	public String getCL_Ejecucion();

    /** Column name CL_Emprendimiento */
    public static final String COLUMNNAME_CL_Emprendimiento = "CL_Emprendimiento";

	/** Set CL_Emprendimiento	  */
	public void setCL_Emprendimiento (String CL_Emprendimiento);

	/** Get CL_Emprendimiento	  */
	public String getCL_Emprendimiento();

    /** Column name CL_EstCalidad */
    public static final String COLUMNNAME_CL_EstCalidad = "CL_EstCalidad";

	/** Set CL_EstCalidad	  */
	public void setCL_EstCalidad (String CL_EstCalidad);

	/** Get CL_EstCalidad	  */
	public String getCL_EstCalidad();

    /** Column name CL_Flexibilidad */
    public static final String COLUMNNAME_CL_Flexibilidad = "CL_Flexibilidad";

	/** Set CL_Flexibilidad	  */
	public void setCL_Flexibilidad (String CL_Flexibilidad);

	/** Get CL_Flexibilidad	  */
	public String getCL_Flexibilidad();

    /** Column name CL_Integridad */
    public static final String COLUMNNAME_CL_Integridad = "CL_Integridad";

	/** Set CL_Integridad	  */
	public void setCL_Integridad (String CL_Integridad);

	/** Get CL_Integridad	  */
	public String getCL_Integridad();

    /** Column name CL_MejoraContinua */
    public static final String COLUMNNAME_CL_MejoraContinua = "CL_MejoraContinua";

	/** Set CL_MejoraContinua	  */
	public void setCL_MejoraContinua (String CL_MejoraContinua);

	/** Get CL_MejoraContinua	  */
	public String getCL_MejoraContinua();

    /** Column name CL_ObjetivosResultados */
    public static final String COLUMNNAME_CL_ObjetivosResultados = "CL_ObjetivosResultados";

	/** Set CL_ObjetivosResultados	  */
	public void setCL_ObjetivosResultados (String CL_ObjetivosResultados);

	/** Get CL_ObjetivosResultados	  */
	public String getCL_ObjetivosResultados();

    /** Column name CL_Orden */
    public static final String COLUMNNAME_CL_Orden = "CL_Orden";

	/** Set CL_Orden	  */
	public void setCL_Orden (String CL_Orden);

	/** Get CL_Orden	  */
	public String getCL_Orden();

    /** Column name CL_Organizacion */
    public static final String COLUMNNAME_CL_Organizacion = "CL_Organizacion";

	/** Set CL_Organizacion	  */
	public void setCL_Organizacion (String CL_Organizacion);

	/** Get CL_Organizacion	  */
	public String getCL_Organizacion();

    /** Column name CL_Perseverancia */
    public static final String COLUMNNAME_CL_Perseverancia = "CL_Perseverancia";

	/** Set CL_Perseverancia	  */
	public void setCL_Perseverancia (String CL_Perseverancia);

	/** Get CL_Perseverancia	  */
	public String getCL_Perseverancia();

    /** Column name CL_Planificacion */
    public static final String COLUMNNAME_CL_Planificacion = "CL_Planificacion";

	/** Set CL_Planificacion	  */
	public void setCL_Planificacion (String CL_Planificacion);

	/** Get CL_Planificacion	  */
	public String getCL_Planificacion();

    /** Column name CL_Proactividad */
    public static final String COLUMNNAME_CL_Proactividad = "CL_Proactividad";

	/** Set CL_Proactividad	  */
	public void setCL_Proactividad (String CL_Proactividad);

	/** Get CL_Proactividad	  */
	public String getCL_Proactividad();

    /** Column name CP_Adaptabilidad */
    public static final String COLUMNNAME_CP_Adaptabilidad = "CP_Adaptabilidad";

	/** Set CP_Adaptabilidad	  */
	public void setCP_Adaptabilidad (String CP_Adaptabilidad);

	/** Get CP_Adaptabilidad	  */
	public String getCP_Adaptabilidad();

    /** Column name CP_ControlEmocional */
    public static final String COLUMNNAME_CP_ControlEmocional = "CP_ControlEmocional";

	/** Set CP_ControlEmocional	  */
	public void setCP_ControlEmocional (String CP_ControlEmocional);

	/** Get CP_ControlEmocional	  */
	public String getCP_ControlEmocional();

    /** Column name CP_EstabilidadEmocional */
    public static final String COLUMNNAME_CP_EstabilidadEmocional = "CP_EstabilidadEmocional";

	/** Set CP_EstabilidadEmocional	  */
	public void setCP_EstabilidadEmocional (String CP_EstabilidadEmocional);

	/** Get CP_EstabilidadEmocional	  */
	public String getCP_EstabilidadEmocional();

    /** Column name CP_Seguridad */
    public static final String COLUMNNAME_CP_Seguridad = "CP_Seguridad";

	/** Set CP_Seguridad	  */
	public void setCP_Seguridad (String CP_Seguridad);

	/** Get CP_Seguridad	  */
	public String getCP_Seguridad();

    /** Column name CP_Sociabilidad */
    public static final String COLUMNNAME_CP_Sociabilidad = "CP_Sociabilidad";

	/** Set CP_Sociabilidad	  */
	public void setCP_Sociabilidad (String CP_Sociabilidad);

	/** Get CP_Sociabilidad	  */
	public String getCP_Sociabilidad();

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

    /** Column name HR_Job_ID */
    public static final String COLUMNNAME_HR_Job_ID = "HR_Job_ID";

	/** Set Payroll Job	  */
	public void setHR_Job_ID (int HR_Job_ID);

	/** Get Payroll Job	  */
	public int getHR_Job_ID();

	public org.eevolution.model.I_HR_Job getHR_Job() throws RuntimeException;

    /** Column name HRO_Competence_ID */
    public static final String COLUMNNAME_HRO_Competence_ID = "HRO_Competence_ID";

	/** Set HRO_Competence	  */
	public void setHRO_Competence_ID (int HRO_Competence_ID);

	/** Get HRO_Competence	  */
	public int getHRO_Competence_ID();

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
