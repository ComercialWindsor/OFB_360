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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for PM_SupervisionObs
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_PM_SupervisionObs extends PO implements I_PM_SupervisionObs, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20140515L;

    /** Standard Constructor */
    public X_PM_SupervisionObs (Properties ctx, int PM_SupervisionObs_ID, String trxName)
    {
      super (ctx, PM_SupervisionObs_ID, trxName);
      /** if (PM_SupervisionObs_ID == 0)
        {
			setPM_Supervision_ID (0);
			setPM_SupervisionObs_ID (0);
        } */
    }

    /** Load Constructor */
    public X_PM_SupervisionObs (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_PM_SupervisionObs[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set adviceabout.
		@param adviceabout adviceabout	  */
	public void setadviceabout (String adviceabout)
	{
		set_Value (COLUMNNAME_adviceabout, adviceabout);
	}

	/** Get adviceabout.
		@return adviceabout	  */
	public String getadviceabout () 
	{
		return (String)get_Value(COLUMNNAME_adviceabout);
	}

	/** Set approve1.
		@param approve1 approve1	  */
	public void setapprove1 (boolean approve1)
	{
		set_Value (COLUMNNAME_approve1, Boolean.valueOf(approve1));
	}

	/** Get approve1.
		@return approve1	  */
	public boolean isapprove1 () 
	{
		Object oo = get_Value(COLUMNNAME_approve1);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set approve2.
		@param approve2 approve2	  */
	public void setapprove2 (boolean approve2)
	{
		set_Value (COLUMNNAME_approve2, Boolean.valueOf(approve2));
	}

	/** Get approve2.
		@return approve2	  */
	public boolean isapprove2 () 
	{
		Object oo = get_Value(COLUMNNAME_approve2);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ATOInspector.
		@param ATOInspector ATOInspector	  */
	public void setATOInspector (String ATOInspector)
	{
		set_Value (COLUMNNAME_ATOInspector, ATOInspector);
	}

	/** Get ATOInspector.
		@return ATOInspector	  */
	public String getATOInspector () 
	{
		return (String)get_Value(COLUMNNAME_ATOInspector);
	}

	/** Set ATOInspectorPosition.
		@param ATOInspectorPosition ATOInspectorPosition	  */
	public void setATOInspectorPosition (String ATOInspectorPosition)
	{
		set_Value (COLUMNNAME_ATOInspectorPosition, ATOInspectorPosition);
	}

	/** Get ATOInspectorPosition.
		@return ATOInspectorPosition	  */
	public String getATOInspectorPosition () 
	{
		return (String)get_Value(COLUMNNAME_ATOInspectorPosition);
	}


	/** Set bookabout.
		@param bookabout bookabout	  */
	public void setbookabout (String bookabout)
	{
		set_Value (COLUMNNAME_bookabout, bookabout);
	}

	/** Get bookabout.
		@return bookabout	  */
	public String getbookabout () 
	{
		return (String)get_Value(COLUMNNAME_bookabout);
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

	public I_C_BPartner getC_BPartnerATO() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartnerATO_ID(), get_TrxName());	}

	/** Set C_BPartnerATO_ID.
		@param C_BPartnerATO_ID C_BPartnerATO_ID	  */
	public void setC_BPartnerATO_ID (int C_BPartnerATO_ID)
	{
		if (C_BPartnerATO_ID < 1) 
			set_Value (COLUMNNAME_C_BPartnerATO_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartnerATO_ID, Integer.valueOf(C_BPartnerATO_ID));
	}

	/** Get C_BPartnerATO_ID.
		@return C_BPartnerATO_ID	  */
	public int getC_BPartnerATO_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartnerATO_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getC_BPartnerCon() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartnerCon_ID(), get_TrxName());	}

	/** Set C_BPartnerCon_ID.
		@param C_BPartnerCon_ID C_BPartnerCon_ID	  */
	public void setC_BPartnerCon_ID (int C_BPartnerCon_ID)
	{
		if (C_BPartnerCon_ID < 1) 
			set_Value (COLUMNNAME_C_BPartnerCon_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartnerCon_ID, Integer.valueOf(C_BPartnerCon_ID));
	}

	/** Get C_BPartnerCon_ID.
		@return C_BPartnerCon_ID	  */
	public int getC_BPartnerCon_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartnerCon_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getC_BPartnerITO() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartnerITO_ID(), get_TrxName());	}

	/** Set C_BPartnerITO_ID.
		@param C_BPartnerITO_ID C_BPartnerITO_ID	  */
	public void setC_BPartnerITO_ID (int C_BPartnerITO_ID)
	{
		if (C_BPartnerITO_ID < 1) 
			set_Value (COLUMNNAME_C_BPartnerITO_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartnerITO_ID, Integer.valueOf(C_BPartnerITO_ID));
	}

	/** Get C_BPartnerITO_ID.
		@return C_BPartnerITO_ID	  */
	public int getC_BPartnerITO_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartnerITO_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getC_BPartnerRO() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartnerRO_ID(), get_TrxName());	}

	/** Set C_BPartnerRO_ID.
		@param C_BPartnerRO_ID C_BPartnerRO_ID	  */
	public void setC_BPartnerRO_ID (int C_BPartnerRO_ID)
	{
		if (C_BPartnerRO_ID < 1) 
			set_Value (COLUMNNAME_C_BPartnerRO_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartnerRO_ID, Integer.valueOf(C_BPartnerRO_ID));
	}

	/** Get C_BPartnerRO_ID.
		@return C_BPartnerRO_ID	  */
	public int getC_BPartnerRO_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartnerRO_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Company.
		@param Company Company	  */
	public void setCompany (String Company)
	{
		set_Value (COLUMNNAME_Company, Company);
	}

	/** Get Company.
		@return Company	  */
	public String getCompany () 
	{
		return (String)get_Value(COLUMNNAME_Company);
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

	/** Set decrease.
		@param decrease decrease	  */
	public void setdecrease (String decrease)
	{
		set_Value (COLUMNNAME_decrease, decrease);
	}

	/** Get decrease.
		@return decrease	  */
	public String getdecrease () 
	{
		return (String)get_Value(COLUMNNAME_decrease);
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

	/** Set descriptionsituation.
		@param descriptionsituation descriptionsituation	  */
	public void setdescriptionsituation (String descriptionsituation)
	{
		set_Value (COLUMNNAME_descriptionsituation, descriptionsituation);
	}

	/** Get descriptionsituation.
		@return descriptionsituation	  */
	public String getdescriptionsituation () 
	{
		return (String)get_Value(COLUMNNAME_descriptionsituation);
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

	/** Set explanationmotive7.
		@param explanationmotive7 explanationmotive7	  */
	public void setexplanationmotive7 (String explanationmotive7)
	{
		set_Value (COLUMNNAME_explanationmotive7, explanationmotive7);
	}

	/** Get explanationmotive7.
		@return explanationmotive7	  */
	public String getexplanationmotive7 () 
	{
		return (String)get_Value(COLUMNNAME_explanationmotive7);
	}

	/** Set Generate To.
		@param GenerateTo 
		Generate To
	  */
	public void setGenerateTo (String GenerateTo)
	{
		set_Value (COLUMNNAME_GenerateTo, GenerateTo);
	}

	/** Get Generate To.
		@return Generate To
	  */
	public String getGenerateTo () 
	{
		return (String)get_Value(COLUMNNAME_GenerateTo);
	}

	/** Set img_desc1.
		@param img_desc1 img_desc1	  */
	public void setimg_desc1 (String img_desc1)
	{
		set_Value (COLUMNNAME_img_desc1, img_desc1);
	}

	/** Get img_desc1.
		@return img_desc1	  */
	public String getimg_desc1 () 
	{
		return (String)get_Value(COLUMNNAME_img_desc1);
	}

	/** Set img_desc2.
		@param img_desc2 img_desc2	  */
	public void setimg_desc2 (String img_desc2)
	{
		set_Value (COLUMNNAME_img_desc2, img_desc2);
	}

	/** Get img_desc2.
		@return img_desc2	  */
	public String getimg_desc2 () 
	{
		return (String)get_Value(COLUMNNAME_img_desc2);
	}

	/** Set img_desc3.
		@param img_desc3 img_desc3	  */
	public void setimg_desc3 (String img_desc3)
	{
		set_Value (COLUMNNAME_img_desc3, img_desc3);
	}

	/** Get img_desc3.
		@return img_desc3	  */
	public String getimg_desc3 () 
	{
		return (String)get_Value(COLUMNNAME_img_desc3);
	}

	/** Set img_desc4.
		@param img_desc4 img_desc4	  */
	public void setimg_desc4 (String img_desc4)
	{
		set_Value (COLUMNNAME_img_desc4, img_desc4);
	}

	/** Get img_desc4.
		@return img_desc4	  */
	public String getimg_desc4 () 
	{
		return (String)get_Value(COLUMNNAME_img_desc4);
	}

	/** Set img_desc5.
		@param img_desc5 img_desc5	  */
	public void setimg_desc5 (String img_desc5)
	{
		set_Value (COLUMNNAME_img_desc5, img_desc5);
	}

	/** Get img_desc5.
		@return img_desc5	  */
	public String getimg_desc5 () 
	{
		return (String)get_Value(COLUMNNAME_img_desc5);
	}

	/** Set img_desc6.
		@param img_desc6 img_desc6	  */
	public void setimg_desc6 (String img_desc6)
	{
		set_Value (COLUMNNAME_img_desc6, img_desc6);
	}

	/** Get img_desc6.
		@return img_desc6	  */
	public String getimg_desc6 () 
	{
		return (String)get_Value(COLUMNNAME_img_desc6);
	}

	/** Set img_desc7.
		@param img_desc7 img_desc7	  */
	public void setimg_desc7 (String img_desc7)
	{
		set_Value (COLUMNNAME_img_desc7, img_desc7);
	}

	/** Get img_desc7.
		@return img_desc7	  */
	public String getimg_desc7 () 
	{
		return (String)get_Value(COLUMNNAME_img_desc7);
	}

	/** Set increase.
		@param increase increase	  */
	public void setincrease (String increase)
	{
		set_Value (COLUMNNAME_increase, increase);
	}

	/** Get increase.
		@return increase	  */
	public String getincrease () 
	{
		return (String)get_Value(COLUMNNAME_increase);
	}

	/** Set increase1.
		@param increase1 increase1	  */
	public void setincrease1 (String increase1)
	{
		set_Value (COLUMNNAME_increase1, increase1);
	}

	/** Get increase1.
		@return increase1	  */
	public String getincrease1 () 
	{
		return (String)get_Value(COLUMNNAME_increase1);
	}

	/** Set Inspector.
		@param Inspector Inspector	  */
	public void setInspector (String Inspector)
	{
		set_Value (COLUMNNAME_Inspector, Inspector);
	}

	/** Get Inspector.
		@return Inspector	  */
	public String getInspector () 
	{
		return (String)get_Value(COLUMNNAME_Inspector);
	}

	/** Set ITOInspector.
		@param ITOInspector ITOInspector	  */
	public void setITOInspector (String ITOInspector)
	{
		set_Value (COLUMNNAME_ITOInspector, ITOInspector);
	}

	/** Get ITOInspector.
		@return ITOInspector	  */
	public String getITOInspector () 
	{
		return (String)get_Value(COLUMNNAME_ITOInspector);
	}

	/** Set ITOInspectorPosition.
		@param ITOInspectorPosition ITOInspectorPosition	  */
	public void setITOInspectorPosition (String ITOInspectorPosition)
	{
		set_Value (COLUMNNAME_ITOInspectorPosition, ITOInspectorPosition);
	}

	/** Get ITOInspectorPosition.
		@return ITOInspectorPosition	  */
	public String getITOInspectorPosition () 
	{
		return (String)get_Value(COLUMNNAME_ITOInspectorPosition);
	}

	/** Set motive1.
		@param motive1 motive1	  */
	public void setmotive1 (boolean motive1)
	{
		set_Value (COLUMNNAME_motive1, Boolean.valueOf(motive1));
	}

	/** Get motive1.
		@return motive1	  */
	public boolean ismotive1 () 
	{
		Object oo = get_Value(COLUMNNAME_motive1);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set motive2.
		@param motive2 motive2	  */
	public void setmotive2 (boolean motive2)
	{
		set_Value (COLUMNNAME_motive2, Boolean.valueOf(motive2));
	}

	/** Get motive2.
		@return motive2	  */
	public boolean ismotive2 () 
	{
		Object oo = get_Value(COLUMNNAME_motive2);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set motive3.
		@param motive3 motive3	  */
	public void setmotive3 (boolean motive3)
	{
		set_Value (COLUMNNAME_motive3, Boolean.valueOf(motive3));
	}

	/** Get motive3.
		@return motive3	  */
	public boolean ismotive3 () 
	{
		Object oo = get_Value(COLUMNNAME_motive3);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set motive4.
		@param motive4 motive4	  */
	public void setmotive4 (boolean motive4)
	{
		set_Value (COLUMNNAME_motive4, Boolean.valueOf(motive4));
	}

	/** Get motive4.
		@return motive4	  */
	public boolean ismotive4 () 
	{
		Object oo = get_Value(COLUMNNAME_motive4);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set motive5.
		@param motive5 motive5	  */
	public void setmotive5 (boolean motive5)
	{
		set_Value (COLUMNNAME_motive5, Boolean.valueOf(motive5));
	}

	/** Get motive5.
		@return motive5	  */
	public boolean ismotive5 () 
	{
		Object oo = get_Value(COLUMNNAME_motive5);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set motive6.
		@param motive6 motive6	  */
	public void setmotive6 (boolean motive6)
	{
		set_Value (COLUMNNAME_motive6, Boolean.valueOf(motive6));
	}

	/** Get motive6.
		@return motive6	  */
	public boolean ismotive6 () 
	{
		Object oo = get_Value(COLUMNNAME_motive6);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set motive7.
		@param motive7 motive7	  */
	public void setmotive7 (boolean motive7)
	{
		set_Value (COLUMNNAME_motive7, Boolean.valueOf(motive7));
	}

	/** Get motive7.
		@return motive7	  */
	public boolean ismotive7 () 
	{
		Object oo = get_Value(COLUMNNAME_motive7);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set observations1.
		@param observations1 observations1	  */
	public void setobservations1 (String observations1)
	{
		set_Value (COLUMNNAME_observations1, observations1);
	}

	/** Get observations1.
		@return observations1	  */
	public String getobservations1 () 
	{
		return (String)get_Value(COLUMNNAME_observations1);
	}

	/** Set observations2.
		@param observations2 observations2	  */
	public void setobservations2 (String observations2)
	{
		set_Value (COLUMNNAME_observations2, observations2);
	}

	/** Get observations2.
		@return observations2	  */
	public String getobservations2 () 
	{
		return (String)get_Value(COLUMNNAME_observations2);
	}

	public I_PM_Supervision getPM_Supervision() throws RuntimeException
    {
		return (I_PM_Supervision)MTable.get(getCtx(), I_PM_Supervision.Table_Name)
			.getPO(getPM_Supervision_ID(), get_TrxName());	}

	/** Set PM_Supervision.
		@param PM_Supervision_ID PM_Supervision	  */
	public void setPM_Supervision_ID (int PM_Supervision_ID)
	{
		if (PM_Supervision_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PM_Supervision_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PM_Supervision_ID, Integer.valueOf(PM_Supervision_ID));
	}

	/** Get PM_Supervision.
		@return PM_Supervision	  */
	public int getPM_Supervision_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PM_Supervision_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set PM_SupervisionObs.
		@param PM_SupervisionObs_ID PM_SupervisionObs	  */
	public void setPM_SupervisionObs_ID (int PM_SupervisionObs_ID)
	{
		if (PM_SupervisionObs_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_PM_SupervisionObs_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_PM_SupervisionObs_ID, Integer.valueOf(PM_SupervisionObs_ID));
	}

	/** Get PM_SupervisionObs.
		@return PM_SupervisionObs	  */
	public int getPM_SupervisionObs_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PM_SupervisionObs_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set PreviousDate.
		@param PreviousDate PreviousDate	  */
	public void setPreviousDate (Timestamp PreviousDate)
	{
		set_Value (COLUMNNAME_PreviousDate, PreviousDate);
	}

	/** Get PreviousDate.
		@return PreviousDate	  */
	public Timestamp getPreviousDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_PreviousDate);
	}

	/** Set qtyman.
		@param qtyman qtyman	  */
	public void setqtyman (BigDecimal qtyman)
	{
		set_Value (COLUMNNAME_qtyman, qtyman);
	}

	/** Get qtyman.
		@return qtyman	  */
	public BigDecimal getqtyman () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_qtyman);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set qtytotal.
		@param qtytotal qtytotal	  */
	public void setqtytotal (BigDecimal qtytotal)
	{
		throw new IllegalArgumentException ("qtytotal is virtual column");	}

	/** Get qtytotal.
		@return qtytotal	  */
	public BigDecimal getqtytotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_qtytotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set qtywoman.
		@param qtywoman qtywoman	  */
	public void setqtywoman (BigDecimal qtywoman)
	{
		set_Value (COLUMNNAME_qtywoman, qtywoman);
	}

	/** Get qtywoman.
		@return qtywoman	  */
	public BigDecimal getqtywoman () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_qtywoman);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set recommendationsproject.
		@param recommendationsproject recommendationsproject	  */
	public void setrecommendationsproject (String recommendationsproject)
	{
		set_Value (COLUMNNAME_recommendationsproject, recommendationsproject);
	}

	/** Get recommendationsproject.
		@return recommendationsproject	  */
	public String getrecommendationsproject () 
	{
		return (String)get_Value(COLUMNNAME_recommendationsproject);
	}

	/** Set reject1.
		@param reject1 reject1	  */
	public void setreject1 (boolean reject1)
	{
		set_Value (COLUMNNAME_reject1, Boolean.valueOf(reject1));
	}

	/** Get reject1.
		@return reject1	  */
	public boolean isreject1 () 
	{
		Object oo = get_Value(COLUMNNAME_reject1);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set reject2.
		@param reject2 reject2	  */
	public void setreject2 (boolean reject2)
	{
		set_ValueNoCheck (COLUMNNAME_reject2, Boolean.valueOf(reject2));
	}

	/** Get reject2.
		@return reject2	  */
	public boolean isreject2 () 
	{
		Object oo = get_Value(COLUMNNAME_reject2);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set ReportDate.
		@param ReportDate ReportDate	  */
	public void setReportDate (Timestamp ReportDate)
	{
		set_Value (COLUMNNAME_ReportDate, ReportDate);
	}

	/** Get ReportDate.
		@return ReportDate	  */
	public Timestamp getReportDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ReportDate);
	}

	/** Set ResidentProfessional.
		@param ResidentProfessional ResidentProfessional	  */
	public void setResidentProfessional (String ResidentProfessional)
	{
		set_Value (COLUMNNAME_ResidentProfessional, ResidentProfessional);
	}

	/** Get ResidentProfessional.
		@return ResidentProfessional	  */
	public String getResidentProfessional () 
	{
		return (String)get_Value(COLUMNNAME_ResidentProfessional);
	}

	/** Set ResidentProfessionalJob.
		@param ResidentProfessionalJob ResidentProfessionalJob	  */
	public void setResidentProfessionalJob (String ResidentProfessionalJob)
	{
		set_Value (COLUMNNAME_ResidentProfessionalJob, ResidentProfessionalJob);
	}

	/** Get ResidentProfessionalJob.
		@return ResidentProfessionalJob	  */
	public String getResidentProfessionalJob () 
	{
		return (String)get_Value(COLUMNNAME_ResidentProfessionalJob);
	}

	/** Set ResidentProfessionalMail.
		@param ResidentProfessionalMail ResidentProfessionalMail	  */
	public void setResidentProfessionalMail (String ResidentProfessionalMail)
	{
		set_Value (COLUMNNAME_ResidentProfessionalMail, ResidentProfessionalMail);
	}

	/** Get ResidentProfessionalMail.
		@return ResidentProfessionalMail	  */
	public String getResidentProfessionalMail () 
	{
		return (String)get_Value(COLUMNNAME_ResidentProfessionalMail);
	}

	/** Set SupervisionRealizationDate.
		@param SupervisionRealizationDate SupervisionRealizationDate	  */
	public void setSupervisionRealizationDate (Timestamp SupervisionRealizationDate)
	{
		set_Value (COLUMNNAME_SupervisionRealizationDate, SupervisionRealizationDate);
	}

	/** Get SupervisionRealizationDate.
		@return SupervisionRealizationDate	  */
	public Timestamp getSupervisionRealizationDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_SupervisionRealizationDate);
	}

	/** Set termincrease.
		@param termincrease termincrease	  */
	public void settermincrease (String termincrease)
	{
		set_Value (COLUMNNAME_termincrease, termincrease);
	}

	/** Get termincrease.
		@return termincrease	  */
	public String gettermincrease () 
	{
		return (String)get_Value(COLUMNNAME_termincrease);
	}

	/** Set useImgDesc1.
		@param useImgDesc1 useImgDesc1	  */
	public void setuseImgDesc1 (boolean useImgDesc1)
	{
		set_Value (COLUMNNAME_useImgDesc1, Boolean.valueOf(useImgDesc1));
	}

	/** Get useImgDesc1.
		@return useImgDesc1	  */
	public boolean isuseImgDesc1 () 
	{
		Object oo = get_Value(COLUMNNAME_useImgDesc1);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set useImgDesc2.
		@param useImgDesc2 useImgDesc2	  */
	public void setuseImgDesc2 (boolean useImgDesc2)
	{
		set_Value (COLUMNNAME_useImgDesc2, Boolean.valueOf(useImgDesc2));
	}

	/** Get useImgDesc2.
		@return useImgDesc2	  */
	public boolean isuseImgDesc2 () 
	{
		Object oo = get_Value(COLUMNNAME_useImgDesc2);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set useImgDesc3.
		@param useImgDesc3 useImgDesc3	  */
	public void setuseImgDesc3 (boolean useImgDesc3)
	{
		set_Value (COLUMNNAME_useImgDesc3, Boolean.valueOf(useImgDesc3));
	}

	/** Get useImgDesc3.
		@return useImgDesc3	  */
	public boolean isuseImgDesc3 () 
	{
		Object oo = get_Value(COLUMNNAME_useImgDesc3);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set useImgDesc4.
		@param useImgDesc4 useImgDesc4	  */
	public void setuseImgDesc4 (boolean useImgDesc4)
	{
		set_Value (COLUMNNAME_useImgDesc4, Boolean.valueOf(useImgDesc4));
	}

	/** Get useImgDesc4.
		@return useImgDesc4	  */
	public boolean isuseImgDesc4 () 
	{
		Object oo = get_Value(COLUMNNAME_useImgDesc4);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set useImgDesc5.
		@param useImgDesc5 useImgDesc5	  */
	public void setuseImgDesc5 (boolean useImgDesc5)
	{
		set_Value (COLUMNNAME_useImgDesc5, Boolean.valueOf(useImgDesc5));
	}

	/** Get useImgDesc5.
		@return useImgDesc5	  */
	public boolean isuseImgDesc5 () 
	{
		Object oo = get_Value(COLUMNNAME_useImgDesc5);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set useImgDesc6.
		@param useImgDesc6 useImgDesc6	  */
	public void setuseImgDesc6 (boolean useImgDesc6)
	{
		set_Value (COLUMNNAME_useImgDesc6, Boolean.valueOf(useImgDesc6));
	}

	/** Get useImgDesc6.
		@return useImgDesc6	  */
	public boolean isuseImgDesc6 () 
	{
		Object oo = get_Value(COLUMNNAME_useImgDesc6);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set useImgDesc7.
		@param useImgDesc7 useImgDesc7	  */
	public void setuseImgDesc7 (boolean useImgDesc7)
	{
		set_Value (COLUMNNAME_useImgDesc7, Boolean.valueOf(useImgDesc7));
	}

	/** Get useImgDesc7.
		@return useImgDesc7	  */
	public boolean isuseImgDesc7 () 
	{
		Object oo = get_Value(COLUMNNAME_useImgDesc7);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set vositsuggestion.
		@param vositsuggestion vositsuggestion	  */
	public void setvositsuggestion (String vositsuggestion)
	{
		set_Value (COLUMNNAME_vositsuggestion, vositsuggestion);
	}

	/** Get vositsuggestion.
		@return vositsuggestion	  */
	public String getvositsuggestion () 
	{
		return (String)get_Value(COLUMNNAME_vositsuggestion);
	}

}