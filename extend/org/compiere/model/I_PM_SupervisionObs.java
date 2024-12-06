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

/** Generated Interface for PM_SupervisionObs
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_PM_SupervisionObs 
{

    /** TableName=PM_SupervisionObs */
    public static final String Table_Name = "PM_SupervisionObs";

    /** AD_Table_ID=1000086 */
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

    /** Column name adviceabout */
    public static final String COLUMNNAME_adviceabout = "adviceabout";

	/** Set adviceabout	  */
	public void setadviceabout (String adviceabout);

	/** Get adviceabout	  */
	public String getadviceabout();

    /** Column name approve1 */
    public static final String COLUMNNAME_approve1 = "approve1";

	/** Set approve1	  */
	public void setapprove1 (boolean approve1);

	/** Get approve1	  */
	public boolean isapprove1();

    /** Column name approve2 */
    public static final String COLUMNNAME_approve2 = "approve2";

	/** Set approve2	  */
	public void setapprove2 (boolean approve2);

	/** Get approve2	  */
	public boolean isapprove2();

    /** Column name ATOInspector */
    public static final String COLUMNNAME_ATOInspector = "ATOInspector";

	/** Set ATOInspector	  */
	public void setATOInspector (String ATOInspector);

	/** Get ATOInspector	  */
	public String getATOInspector();

    /** Column name ATOInspectorPosition */
    public static final String COLUMNNAME_ATOInspectorPosition = "ATOInspectorPosition";

	/** Set ATOInspectorPosition	  */
	public void setATOInspectorPosition (String ATOInspectorPosition);

	/** Get ATOInspectorPosition	  */
	public String getATOInspectorPosition();

    /** Column name bookabout */
    public static final String COLUMNNAME_bookabout = "bookabout";

	/** Set bookabout	  */
	public void setbookabout (String bookabout);

	/** Get bookabout	  */
	public String getbookabout();

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

    /** Column name C_BPartnerATO_ID */
    public static final String COLUMNNAME_C_BPartnerATO_ID = "C_BPartnerATO_ID";

	/** Set C_BPartnerATO_ID	  */
	public void setC_BPartnerATO_ID (int C_BPartnerATO_ID);

	/** Get C_BPartnerATO_ID	  */
	public int getC_BPartnerATO_ID();

	public I_C_BPartner getC_BPartnerATO() throws RuntimeException;

    /** Column name C_BPartnerCon_ID */
    public static final String COLUMNNAME_C_BPartnerCon_ID = "C_BPartnerCon_ID";

	/** Set C_BPartnerCon_ID	  */
	public void setC_BPartnerCon_ID (int C_BPartnerCon_ID);

	/** Get C_BPartnerCon_ID	  */
	public int getC_BPartnerCon_ID();

	public I_C_BPartner getC_BPartnerCon() throws RuntimeException;

    /** Column name C_BPartnerITO_ID */
    public static final String COLUMNNAME_C_BPartnerITO_ID = "C_BPartnerITO_ID";

	/** Set C_BPartnerITO_ID	  */
	public void setC_BPartnerITO_ID (int C_BPartnerITO_ID);

	/** Get C_BPartnerITO_ID	  */
	public int getC_BPartnerITO_ID();

	public I_C_BPartner getC_BPartnerITO() throws RuntimeException;

    /** Column name C_BPartnerRO_ID */
    public static final String COLUMNNAME_C_BPartnerRO_ID = "C_BPartnerRO_ID";

	/** Set C_BPartnerRO_ID	  */
	public void setC_BPartnerRO_ID (int C_BPartnerRO_ID);

	/** Get C_BPartnerRO_ID	  */
	public int getC_BPartnerRO_ID();

	public I_C_BPartner getC_BPartnerRO() throws RuntimeException;

    /** Column name Company */
    public static final String COLUMNNAME_Company = "Company";

	/** Set Company	  */
	public void setCompany (String Company);

	/** Get Company	  */
	public String getCompany();

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

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

    /** Column name decrease */
    public static final String COLUMNNAME_decrease = "decrease";

	/** Set decrease	  */
	public void setdecrease (String decrease);

	/** Get decrease	  */
	public String getdecrease();

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

    /** Column name descriptionsituation */
    public static final String COLUMNNAME_descriptionsituation = "descriptionsituation";

	/** Set descriptionsituation	  */
	public void setdescriptionsituation (String descriptionsituation);

	/** Get descriptionsituation	  */
	public String getdescriptionsituation();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name explanationmotive7 */
    public static final String COLUMNNAME_explanationmotive7 = "explanationmotive7";

	/** Set explanationmotive7	  */
	public void setexplanationmotive7 (String explanationmotive7);

	/** Get explanationmotive7	  */
	public String getexplanationmotive7();

    /** Column name GenerateTo */
    public static final String COLUMNNAME_GenerateTo = "GenerateTo";

	/** Set Generate To.
	  * Generate To
	  */
	public void setGenerateTo (String GenerateTo);

	/** Get Generate To.
	  * Generate To
	  */
	public String getGenerateTo();

    /** Column name img_desc1 */
    public static final String COLUMNNAME_img_desc1 = "img_desc1";

	/** Set img_desc1	  */
	public void setimg_desc1 (String img_desc1);

	/** Get img_desc1	  */
	public String getimg_desc1();

    /** Column name img_desc2 */
    public static final String COLUMNNAME_img_desc2 = "img_desc2";

	/** Set img_desc2	  */
	public void setimg_desc2 (String img_desc2);

	/** Get img_desc2	  */
	public String getimg_desc2();

    /** Column name img_desc3 */
    public static final String COLUMNNAME_img_desc3 = "img_desc3";

	/** Set img_desc3	  */
	public void setimg_desc3 (String img_desc3);

	/** Get img_desc3	  */
	public String getimg_desc3();

    /** Column name img_desc4 */
    public static final String COLUMNNAME_img_desc4 = "img_desc4";

	/** Set img_desc4	  */
	public void setimg_desc4 (String img_desc4);

	/** Get img_desc4	  */
	public String getimg_desc4();

    /** Column name img_desc5 */
    public static final String COLUMNNAME_img_desc5 = "img_desc5";

	/** Set img_desc5	  */
	public void setimg_desc5 (String img_desc5);

	/** Get img_desc5	  */
	public String getimg_desc5();

    /** Column name img_desc6 */
    public static final String COLUMNNAME_img_desc6 = "img_desc6";

	/** Set img_desc6	  */
	public void setimg_desc6 (String img_desc6);

	/** Get img_desc6	  */
	public String getimg_desc6();

    /** Column name img_desc7 */
    public static final String COLUMNNAME_img_desc7 = "img_desc7";

	/** Set img_desc7	  */
	public void setimg_desc7 (String img_desc7);

	/** Get img_desc7	  */
	public String getimg_desc7();

    /** Column name increase */
    public static final String COLUMNNAME_increase = "increase";

	/** Set increase	  */
	public void setincrease (String increase);

	/** Get increase	  */
	public String getincrease();

    /** Column name increase1 */
    public static final String COLUMNNAME_increase1 = "increase1";

	/** Set increase1	  */
	public void setincrease1 (String increase1);

	/** Get increase1	  */
	public String getincrease1();

    /** Column name Inspector */
    public static final String COLUMNNAME_Inspector = "Inspector";

	/** Set Inspector	  */
	public void setInspector (String Inspector);

	/** Get Inspector	  */
	public String getInspector();

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

    /** Column name ITOInspector */
    public static final String COLUMNNAME_ITOInspector = "ITOInspector";

	/** Set ITOInspector	  */
	public void setITOInspector (String ITOInspector);

	/** Get ITOInspector	  */
	public String getITOInspector();

    /** Column name ITOInspectorPosition */
    public static final String COLUMNNAME_ITOInspectorPosition = "ITOInspectorPosition";

	/** Set ITOInspectorPosition	  */
	public void setITOInspectorPosition (String ITOInspectorPosition);

	/** Get ITOInspectorPosition	  */
	public String getITOInspectorPosition();

    /** Column name motive1 */
    public static final String COLUMNNAME_motive1 = "motive1";

	/** Set motive1	  */
	public void setmotive1 (boolean motive1);

	/** Get motive1	  */
	public boolean ismotive1();

    /** Column name motive2 */
    public static final String COLUMNNAME_motive2 = "motive2";

	/** Set motive2	  */
	public void setmotive2 (boolean motive2);

	/** Get motive2	  */
	public boolean ismotive2();

    /** Column name motive3 */
    public static final String COLUMNNAME_motive3 = "motive3";

	/** Set motive3	  */
	public void setmotive3 (boolean motive3);

	/** Get motive3	  */
	public boolean ismotive3();

    /** Column name motive4 */
    public static final String COLUMNNAME_motive4 = "motive4";

	/** Set motive4	  */
	public void setmotive4 (boolean motive4);

	/** Get motive4	  */
	public boolean ismotive4();

    /** Column name motive5 */
    public static final String COLUMNNAME_motive5 = "motive5";

	/** Set motive5	  */
	public void setmotive5 (boolean motive5);

	/** Get motive5	  */
	public boolean ismotive5();

    /** Column name motive6 */
    public static final String COLUMNNAME_motive6 = "motive6";

	/** Set motive6	  */
	public void setmotive6 (boolean motive6);

	/** Get motive6	  */
	public boolean ismotive6();

    /** Column name motive7 */
    public static final String COLUMNNAME_motive7 = "motive7";

	/** Set motive7	  */
	public void setmotive7 (boolean motive7);

	/** Get motive7	  */
	public boolean ismotive7();

    /** Column name observations1 */
    public static final String COLUMNNAME_observations1 = "observations1";

	/** Set observations1	  */
	public void setobservations1 (String observations1);

	/** Get observations1	  */
	public String getobservations1();

    /** Column name observations2 */
    public static final String COLUMNNAME_observations2 = "observations2";

	/** Set observations2	  */
	public void setobservations2 (String observations2);

	/** Get observations2	  */
	public String getobservations2();

    /** Column name PM_Supervision_ID */
    public static final String COLUMNNAME_PM_Supervision_ID = "PM_Supervision_ID";

	/** Set PM_Supervision	  */
	public void setPM_Supervision_ID (int PM_Supervision_ID);

	/** Get PM_Supervision	  */
	public int getPM_Supervision_ID();

	public I_PM_Supervision getPM_Supervision() throws RuntimeException;

    /** Column name PM_SupervisionObs_ID */
    public static final String COLUMNNAME_PM_SupervisionObs_ID = "PM_SupervisionObs_ID";

	/** Set PM_SupervisionObs	  */
	public void setPM_SupervisionObs_ID (int PM_SupervisionObs_ID);

	/** Get PM_SupervisionObs	  */
	public int getPM_SupervisionObs_ID();

    /** Column name PreviousDate */
    public static final String COLUMNNAME_PreviousDate = "PreviousDate";

	/** Set PreviousDate	  */
	public void setPreviousDate (Timestamp PreviousDate);

	/** Get PreviousDate	  */
	public Timestamp getPreviousDate();

    /** Column name qtyman */
    public static final String COLUMNNAME_qtyman = "qtyman";

	/** Set qtyman	  */
	public void setqtyman (BigDecimal qtyman);

	/** Get qtyman	  */
	public BigDecimal getqtyman();

    /** Column name qtytotal */
    public static final String COLUMNNAME_qtytotal = "qtytotal";

	/** Set qtytotal	  */
	public void setqtytotal (BigDecimal qtytotal);

	/** Get qtytotal	  */
	public BigDecimal getqtytotal();

    /** Column name qtywoman */
    public static final String COLUMNNAME_qtywoman = "qtywoman";

	/** Set qtywoman	  */
	public void setqtywoman (BigDecimal qtywoman);

	/** Get qtywoman	  */
	public BigDecimal getqtywoman();

    /** Column name recommendationsproject */
    public static final String COLUMNNAME_recommendationsproject = "recommendationsproject";

	/** Set recommendationsproject	  */
	public void setrecommendationsproject (String recommendationsproject);

	/** Get recommendationsproject	  */
	public String getrecommendationsproject();

    /** Column name reject1 */
    public static final String COLUMNNAME_reject1 = "reject1";

	/** Set reject1	  */
	public void setreject1 (boolean reject1);

	/** Get reject1	  */
	public boolean isreject1();

    /** Column name reject2 */
    public static final String COLUMNNAME_reject2 = "reject2";

	/** Set reject2	  */
	public void setreject2 (boolean reject2);

	/** Get reject2	  */
	public boolean isreject2();

    /** Column name ReportDate */
    public static final String COLUMNNAME_ReportDate = "ReportDate";

	/** Set ReportDate	  */
	public void setReportDate (Timestamp ReportDate);

	/** Get ReportDate	  */
	public Timestamp getReportDate();

    /** Column name ResidentProfessional */
    public static final String COLUMNNAME_ResidentProfessional = "ResidentProfessional";

	/** Set ResidentProfessional	  */
	public void setResidentProfessional (String ResidentProfessional);

	/** Get ResidentProfessional	  */
	public String getResidentProfessional();

    /** Column name ResidentProfessionalJob */
    public static final String COLUMNNAME_ResidentProfessionalJob = "ResidentProfessionalJob";

	/** Set ResidentProfessionalJob	  */
	public void setResidentProfessionalJob (String ResidentProfessionalJob);

	/** Get ResidentProfessionalJob	  */
	public String getResidentProfessionalJob();

    /** Column name ResidentProfessionalMail */
    public static final String COLUMNNAME_ResidentProfessionalMail = "ResidentProfessionalMail";

	/** Set ResidentProfessionalMail	  */
	public void setResidentProfessionalMail (String ResidentProfessionalMail);

	/** Get ResidentProfessionalMail	  */
	public String getResidentProfessionalMail();

    /** Column name SupervisionRealizationDate */
    public static final String COLUMNNAME_SupervisionRealizationDate = "SupervisionRealizationDate";

	/** Set SupervisionRealizationDate	  */
	public void setSupervisionRealizationDate (Timestamp SupervisionRealizationDate);

	/** Get SupervisionRealizationDate	  */
	public Timestamp getSupervisionRealizationDate();

    /** Column name termincrease */
    public static final String COLUMNNAME_termincrease = "termincrease";

	/** Set termincrease	  */
	public void settermincrease (String termincrease);

	/** Get termincrease	  */
	public String gettermincrease();

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

    /** Column name useImgDesc1 */
    public static final String COLUMNNAME_useImgDesc1 = "useImgDesc1";

	/** Set useImgDesc1	  */
	public void setuseImgDesc1 (boolean useImgDesc1);

	/** Get useImgDesc1	  */
	public boolean isuseImgDesc1();

    /** Column name useImgDesc2 */
    public static final String COLUMNNAME_useImgDesc2 = "useImgDesc2";

	/** Set useImgDesc2	  */
	public void setuseImgDesc2 (boolean useImgDesc2);

	/** Get useImgDesc2	  */
	public boolean isuseImgDesc2();

    /** Column name useImgDesc3 */
    public static final String COLUMNNAME_useImgDesc3 = "useImgDesc3";

	/** Set useImgDesc3	  */
	public void setuseImgDesc3 (boolean useImgDesc3);

	/** Get useImgDesc3	  */
	public boolean isuseImgDesc3();

    /** Column name useImgDesc4 */
    public static final String COLUMNNAME_useImgDesc4 = "useImgDesc4";

	/** Set useImgDesc4	  */
	public void setuseImgDesc4 (boolean useImgDesc4);

	/** Get useImgDesc4	  */
	public boolean isuseImgDesc4();

    /** Column name useImgDesc5 */
    public static final String COLUMNNAME_useImgDesc5 = "useImgDesc5";

	/** Set useImgDesc5	  */
	public void setuseImgDesc5 (boolean useImgDesc5);

	/** Get useImgDesc5	  */
	public boolean isuseImgDesc5();

    /** Column name useImgDesc6 */
    public static final String COLUMNNAME_useImgDesc6 = "useImgDesc6";

	/** Set useImgDesc6	  */
	public void setuseImgDesc6 (boolean useImgDesc6);

	/** Get useImgDesc6	  */
	public boolean isuseImgDesc6();

    /** Column name useImgDesc7 */
    public static final String COLUMNNAME_useImgDesc7 = "useImgDesc7";

	/** Set useImgDesc7	  */
	public void setuseImgDesc7 (boolean useImgDesc7);

	/** Get useImgDesc7	  */
	public boolean isuseImgDesc7();

    /** Column name vositsuggestion */
    public static final String COLUMNNAME_vositsuggestion = "vositsuggestion";

	/** Set vositsuggestion	  */
	public void setvositsuggestion (String vositsuggestion);

	/** Get vositsuggestion	  */
	public String getvositsuggestion();
}
