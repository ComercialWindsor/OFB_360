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

/** Generated Model for DM_DocumentReception
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_DM_DocumentReception extends PO implements I_DM_DocumentReception, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140103L;

    /** Standard Constructor */
    public X_DM_DocumentReception (Properties ctx, int DM_DocumentReception_ID, String trxName)
    {
      super (ctx, DM_DocumentReception_ID, trxName);
      /** if (DM_DocumentReception_ID == 0)
        {
			setad_orgref_id (0);
			setAD_OrgRef2_ID (0);
			setDateAcct (new Timestamp( System.currentTimeMillis() ));
			setDateTrx (new Timestamp( System.currentTimeMillis() ));
			setdm_documentreception_ID (0);
			setDocumentNo (null);
			setEntryNo (null);
			setProcessed (false);
        } */
    }

    /** Load Constructor */
    public X_DM_DocumentReception (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_DM_DocumentReception[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set ad_orgref_id.
		@param ad_orgref_id ad_orgref_id	  */
	public void setad_orgref_id (int ad_orgref_id)
	{
		set_Value (COLUMNNAME_ad_orgref_id, Integer.valueOf(ad_orgref_id));
	}

	/** Get ad_orgref_id.
		@return ad_orgref_id	  */
	public int getad_orgref_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ad_orgref_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AD_OrgRef2_ID.
		@param AD_OrgRef2_ID AD_OrgRef2_ID	  */
	public void setAD_OrgRef2_ID (int AD_OrgRef2_ID)
	{
		if (AD_OrgRef2_ID < 1) 
			set_Value (COLUMNNAME_AD_OrgRef2_ID, null);
		else 
			set_Value (COLUMNNAME_AD_OrgRef2_ID, Integer.valueOf(AD_OrgRef2_ID));
	}

	/** Get AD_OrgRef2_ID.
		@return AD_OrgRef2_ID	  */
	public int getAD_OrgRef2_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_OrgRef2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set antecedent.
		@param antecedent antecedent	  */
	public void setantecedent (String antecedent)
	{
		set_Value (COLUMNNAME_antecedent, antecedent);
	}

	/** Get antecedent.
		@return antecedent	  */
	public String getantecedent () 
	{
		return (String)get_Value(COLUMNNAME_antecedent);
	}

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		set_Value (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
	}

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** DM_Document_Type AD_Reference_ID=1000170 */
	public static final int DM_DOCUMENT_TYPE_AD_Reference_ID=1000170;
	/** Estado de Pago = 01 */
	public static final String DM_DOCUMENT_TYPE_EstadoDePago = "01";
	/** Boleta = 02 */
	public static final String DM_DOCUMENT_TYPE_Boleta = "02";
	/** Boletín = 03 */
	public static final String DM_DOCUMENT_TYPE_Boletín = "03";
	/** Carpeta = 04 */
	public static final String DM_DOCUMENT_TYPE_Carpeta = "04";
	/** Carta = 05 */
	public static final String DM_DOCUMENT_TYPE_Carta = "05";
	/** Certificados = 06 */
	public static final String DM_DOCUMENT_TYPE_Certificados = "06";
	/** Circular = 07 */
	public static final String DM_DOCUMENT_TYPE_Circular = "07";
	/** Comprobante = 08 */
	public static final String DM_DOCUMENT_TYPE_Comprobante = "08";
	/** Comprobante de Pago = 09 */
	public static final String DM_DOCUMENT_TYPE_ComprobanteDePago = "09";
	/** Cotización = 10 */
	public static final String DM_DOCUMENT_TYPE_Cotizacion = "10";
	/** Currículum Vitae = 11 */
	public static final String DM_DOCUMENT_TYPE_CurriculumVitae = "11";
	/** Decreto = 12 */
	public static final String DM_DOCUMENT_TYPE_Decreto = "12";
	/** Dictámenes = 13 */
	public static final String DM_DOCUMENT_TYPE_Dictamenes = "13";
	/** Factura = 14 */
	public static final String DM_DOCUMENT_TYPE_Factura = "14";
	/** Fax = 15 */
	public static final String DM_DOCUMENT_TYPE_Fax = "15";
	/** Guía de Despacho = 16 */
	public static final String DM_DOCUMENT_TYPE_GuíaDeDespacho = "16";
	/** Impresos = 17 */
	public static final String DM_DOCUMENT_TYPE_Impresos = "17";
	/** Informe = 18 */
	public static final String DM_DOCUMENT_TYPE_Informe = "18";
	/** Invitación = 19 */
	public static final String DM_DOCUMENT_TYPE_Invitacion = "19";
	/** Listado = 20 */
	public static final String DM_DOCUMENT_TYPE_Listado = "20";
	/** Memorándum = 21 */
	public static final String DM_DOCUMENT_TYPE_Memorandum = "21";
	/** Nota = 22 */
	public static final String DM_DOCUMENT_TYPE_Nota = "22";
	/** Nota de Crédito = 23 */
	public static final String DM_DOCUMENT_TYPE_NotaDeCredito = "23";
	/** Nómina = 24 */
	public static final String DM_DOCUMENT_TYPE_Nomina = "24";
	/** Oficio = 25 */
	public static final String DM_DOCUMENT_TYPE_Oficio = "25";
	/** Ordinario = 26 */
	public static final String DM_DOCUMENT_TYPE_Ordinario = "26";
	/** Orden de Pago = 27 */
	public static final String DM_DOCUMENT_TYPE_OrdenDePago = "27";
	/** Remesa de Fondos = 28 */
	public static final String DM_DOCUMENT_TYPE_RemesaDeFondos = "28";
	/** Rendición de Cuentas = 29 */
	public static final String DM_DOCUMENT_TYPE_RendicionDeCuentas = "29";
	/** Resolución Exenta = 30 */
	public static final String DM_DOCUMENT_TYPE_ResolucionExenta = "30";
	/** Resolución = 31 */
	public static final String DM_DOCUMENT_TYPE_Resolucion = "31";
	/** Reservado = 32 */
	public static final String DM_DOCUMENT_TYPE_Reservado = "32";
	/** Cheque = 33 */
	public static final String DM_DOCUMENT_TYPE_Cheque = "33";
	/** Pago de Subsidios Licencias Médicas = 34 */
	public static final String DM_DOCUMENT_TYPE_PagoDeSubsidiosLicenciasMedicas = "34";
	/** Recibo de Arriendo = 35 */
	public static final String DM_DOCUMENT_TYPE_ReciboDeArriendo = "35";
	/** Contrato = 36 */
	public static final String DM_DOCUMENT_TYPE_Contrato = "36";
	/** Set DM_Document_Type.
		@param DM_Document_Type DM_Document_Type	  */
	public void setDM_Document_Type (String DM_Document_Type)
	{

		set_Value (COLUMNNAME_DM_Document_Type, DM_Document_Type);
	}

	/** Get DM_Document_Type.
		@return DM_Document_Type	  */
	public String getDM_Document_Type () 
	{
		return (String)get_Value(COLUMNNAME_DM_Document_Type);
	}

	/** Set dm_documentreception.
		@param dm_documentreception_ID dm_documentreception	  */
	public void setdm_documentreception_ID (int dm_documentreception_ID)
	{
		if (dm_documentreception_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_dm_documentreception_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_dm_documentreception_ID, Integer.valueOf(dm_documentreception_ID));
	}

	/** Get dm_documentreception.
		@return dm_documentreception	  */
	public int getdm_documentreception_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_dm_documentreception_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set EntryNo.
		@param EntryNo 
		Document sequence number of the document
	  */
	public void setEntryNo (String EntryNo)
	{
		set_Value (COLUMNNAME_EntryNo, EntryNo);
	}

	/** Get EntryNo.
		@return Document sequence number of the document
	  */
	public String getEntryNo () 
	{
		return (String)get_Value(COLUMNNAME_EntryNo);
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set step.
		@param step step	  */
	public void setstep (String step)
	{
		set_Value (COLUMNNAME_step, step);
	}

	/** Get step.
		@return step	  */
	public String getstep () 
	{
		return (String)get_Value(COLUMNNAME_step);
	}

	/** Set topic.
		@param topic topic	  */
	public void settopic (String topic)
	{
		set_Value (COLUMNNAME_topic, topic);
	}

	/** Get topic.
		@return topic	  */
	public String gettopic () 
	{
		return (String)get_Value(COLUMNNAME_topic);
	}

	/** topictype AD_Reference_ID=1000171 */
	public static final int TOPICTYPE_AD_Reference_ID=1000171;
	/** Actas = 01 */
	public static final String TOPICTYPE_Actas = "01";
	/** Administración = 02 */
	public static final String TOPICTYPE_Administracion = "02";
	/** Auditoría Interna = 03 */
	public static final String TOPICTYPE_AuditoriaInterna = "03";
	/** Balance Ejercicios Presupuestario = 04 */
	public static final String TOPICTYPE_BalanceEjerciciosPresupuestario = "04";
	/** Otros = 05 */
	public static final String TOPICTYPE_Otros = "05";
	/** Set topictype.
		@param topictype topictype	  */
	public void settopictype (String topictype)
	{

		set_Value (COLUMNNAME_topictype, topictype);
	}

	/** Get topictype.
		@return topictype	  */
	public String gettopictype () 
	{
		return (String)get_Value(COLUMNNAME_topictype);
	}
}