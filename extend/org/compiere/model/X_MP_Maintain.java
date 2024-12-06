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
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for MP_Maintain
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_MP_Maintain extends PO implements I_MP_Maintain, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20110102L;

    /** Standard Constructor */
    public X_MP_Maintain (Properties ctx, int MP_Maintain_ID, String trxName)
    {
      super (ctx, MP_Maintain_ID, trxName);
      /** if (MP_Maintain_ID == 0)
        {
			setDocStatus (null);
			setDocumentNo (null);
			setMP_Maintain_ID (0);
			setProgrammingType (null);
        } */
    }

    /** Load Constructor */
    public X_MP_Maintain (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_MP_Maintain[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_A_Asset_Group getA_Asset_Group() throws RuntimeException
    {
		return (I_A_Asset_Group)MTable.get(getCtx(), I_A_Asset_Group.Table_Name)
			.getPO(getA_Asset_Group_ID(), get_TrxName());	}

	/** Set Asset Group.
		@param A_Asset_Group_ID 
		Group of Assets
	  */
	public void setA_Asset_Group_ID (int A_Asset_Group_ID)
	{
		if (A_Asset_Group_ID < 1) 
			set_Value (COLUMNNAME_A_Asset_Group_ID, null);
		else 
			set_Value (COLUMNNAME_A_Asset_Group_ID, Integer.valueOf(A_Asset_Group_ID));
	}

	/** Get Asset Group.
		@return Group of Assets
	  */
	public int getA_Asset_Group_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_Group_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_A_Asset getA_Asset() throws RuntimeException
    {
		return (I_A_Asset)MTable.get(getCtx(), I_A_Asset.Table_Name)
			.getPO(getA_Asset_ID(), get_TrxName());	}

	/** Set Asset.
		@param A_Asset_ID 
		Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID)
	{
		if (A_Asset_ID < 1) 
			set_Value (COLUMNNAME_A_Asset_ID, null);
		else 
			set_Value (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
	}

	/** Get Asset.
		@return Asset used internally or by customers
	  */
	public int getA_Asset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Copy From.
		@param CopyFrom 
		Copy From Record
	  */
	public void setCopyFrom (String CopyFrom)
	{
		set_Value (COLUMNNAME_CopyFrom, CopyFrom);
	}

	/** Get Copy From.
		@return Copy From Record
	  */
	public String getCopyFrom () 
	{
		return (String)get_Value(COLUMNNAME_CopyFrom);
	}

	/** Set currentmp.
		@param currentmp currentmp	  */
	public void setcurrentmp (BigDecimal currentmp)
	{
		set_Value (COLUMNNAME_currentmp, currentmp);
	}

	/** Get currentmp.
		@return currentmp	  */
	public BigDecimal getcurrentmp () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_currentmp);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DateLastOT.
		@param DateLastOT DateLastOT	  */
	public void setDateLastOT (Timestamp DateLastOT)
	{
		set_Value (COLUMNNAME_DateLastOT, DateLastOT);
	}

	/** Get DateLastOT.
		@return DateLastOT	  */
	public Timestamp getDateLastOT () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateLastOT);
	}

	/** Set Date last run.
		@param DateLastRun 
		Date the process was last run.
	  */
	public void setDateLastRun (Timestamp DateLastRun)
	{
		set_Value (COLUMNNAME_DateLastRun, DateLastRun);
	}

	/** Get Date last run.
		@return Date the process was last run.
	  */
	public Timestamp getDateLastRun () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateLastRun);
	}

	/** Set datelastrunmp.
		@param datelastrunmp datelastrunmp	  */
	public void setdatelastrunmp (Timestamp datelastrunmp)
	{
		set_Value (COLUMNNAME_datelastrunmp, datelastrunmp);
	}

	/** Get datelastrunmp.
		@return datelastrunmp	  */
	public Timestamp getdatelastrunmp () 
	{
		return (Timestamp)get_Value(COLUMNNAME_datelastrunmp);
	}

	/** Set Date next run.
		@param DateNextRun 
		Date the process will run next
	  */
	public void setDateNextRun (Timestamp DateNextRun)
	{
		set_Value (COLUMNNAME_DateNextRun, DateNextRun);
	}

	/** Get Date next run.
		@return Date the process will run next
	  */
	public Timestamp getDateNextRun () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateNextRun);
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

	/** DocStatus AD_Reference_ID=1000025 */
	public static final int DOCSTATUS_AD_Reference_ID=1000025;
	/** Active = AT */
	public static final String DOCSTATUS_Active = "AT";
	/** Critical = AC */
	public static final String DOCSTATUS_Critical = "AC";
	/** Inacive = IT */
	public static final String DOCSTATUS_Inacive = "IT";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
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

	/** Set Interval.
		@param Interval Interval	  */
	public void setInterval (BigDecimal Interval)
	{
		set_Value (COLUMNNAME_Interval, Interval);
	}

	/** Get Interval.
		@return Interval	  */
	public BigDecimal getInterval () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Interval);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set IsChild.
		@param IsChild IsChild	  */
	public void setIsChild (boolean IsChild)
	{
		set_Value (COLUMNNAME_IsChild, Boolean.valueOf(IsChild));
	}

	/** Get IsChild.
		@return IsChild	  */
	public boolean isChild () 
	{
		Object oo = get_Value(COLUMNNAME_IsChild);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set lastmp.
		@param lastmp lastmp	  */
	public void setlastmp (BigDecimal lastmp)
	{
		set_Value (COLUMNNAME_lastmp, lastmp);
	}

	/** Get lastmp.
		@return lastmp	  */
	public BigDecimal getlastmp () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_lastmp);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set lastread.
		@param lastread lastread	  */
	public void setlastread (BigDecimal lastread)
	{
		set_Value (COLUMNNAME_lastread, lastread);
	}

	/** Get lastread.
		@return lastread	  */
	public BigDecimal getlastread () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_lastread);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_MP_JobStandar getMP_JobStandar() throws RuntimeException
    {
		return (I_MP_JobStandar)MTable.get(getCtx(), I_MP_JobStandar.Table_Name)
			.getPO(getMP_JobStandar_ID(), get_TrxName());	}

	/** Set MP_JobStandard.
		@param MP_JobStandar_ID MP_JobStandard	  */
	public void setMP_JobStandar_ID (int MP_JobStandar_ID)
	{
		if (MP_JobStandar_ID < 1) 
			set_Value (COLUMNNAME_MP_JobStandar_ID, null);
		else 
			set_Value (COLUMNNAME_MP_JobStandar_ID, Integer.valueOf(MP_JobStandar_ID));
	}

	/** Get MP_JobStandard.
		@return MP_JobStandard	  */
	public int getMP_JobStandar_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_JobStandar_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set MP_Maintain.
		@param MP_Maintain_ID MP_Maintain	  */
	public void setMP_Maintain_ID (int MP_Maintain_ID)
	{
		if (MP_Maintain_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_MP_Maintain_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_MP_Maintain_ID, Integer.valueOf(MP_Maintain_ID));
	}

	/** Get MP_Maintain.
		@return MP_Maintain	  */
	public int getMP_Maintain_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_Maintain_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_MP_Maintain getMP_MaintainParent() throws RuntimeException
    {
		return (I_MP_Maintain)MTable.get(getCtx(), I_MP_Maintain.Table_Name)
			.getPO(getMP_MaintainParent_ID(), get_TrxName());	}

	/** Set MP_MaintainParent_ID.
		@param MP_MaintainParent_ID MP_MaintainParent_ID	  */
	public void setMP_MaintainParent_ID (int MP_MaintainParent_ID)
	{
		if (MP_MaintainParent_ID < 1) 
			set_Value (COLUMNNAME_MP_MaintainParent_ID, null);
		else 
			set_Value (COLUMNNAME_MP_MaintainParent_ID, Integer.valueOf(MP_MaintainParent_ID));
	}

	/** Get MP_MaintainParent_ID.
		@return MP_MaintainParent_ID	  */
	public int getMP_MaintainParent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_MaintainParent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_MP_Meter getMP_Meter() throws RuntimeException
    {
		return (I_MP_Meter)MTable.get(getCtx(), I_MP_Meter.Table_Name)
			.getPO(getMP_Meter_ID(), get_TrxName());	}

	/** Set MP_Meter.
		@param MP_Meter_ID MP_Meter	  */
	public void setMP_Meter_ID (int MP_Meter_ID)
	{
		if (MP_Meter_ID < 1) 
			set_Value (COLUMNNAME_MP_Meter_ID, null);
		else 
			set_Value (COLUMNNAME_MP_Meter_ID, Integer.valueOf(MP_Meter_ID));
	}

	/** Get MP_Meter.
		@return MP_Meter	  */
	public int getMP_Meter_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MP_Meter_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set nextmp.
		@param nextmp nextmp	  */
	public void setnextmp (BigDecimal nextmp)
	{
		set_Value (COLUMNNAME_nextmp, nextmp);
	}

	/** Get nextmp.
		@return nextmp	  */
	public BigDecimal getnextmp () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_nextmp);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Priority.
		@param PriorityRule 
		Priority of a document
	  */
	public void setPriorityRule (boolean PriorityRule)
	{
		set_Value (COLUMNNAME_PriorityRule, Boolean.valueOf(PriorityRule));
	}

	/** Get Priority.
		@return Priority of a document
	  */
	public boolean isPriorityRule () 
	{
		Object oo = get_Value(COLUMNNAME_PriorityRule);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** ProgrammingType AD_Reference_ID=1000015 */
	public static final int PROGRAMMINGTYPE_AD_Reference_ID=1000015;
	/** Calendar = C */
	public static final String PROGRAMMINGTYPE_Calendar = "C";
	/** Meter = M */
	public static final String PROGRAMMINGTYPE_Meter = "M";
	/** Set ProgrammingType.
		@param ProgrammingType ProgrammingType	  */
	public void setProgrammingType (String ProgrammingType)
	{

		set_Value (COLUMNNAME_ProgrammingType, ProgrammingType);
	}

	/** Get ProgrammingType.
		@return ProgrammingType	  */
	public String getProgrammingType () 
	{
		return (String)get_Value(COLUMNNAME_ProgrammingType);
	}

	/** Set promuse.
		@param promuse promuse	  */
	public void setpromuse (BigDecimal promuse)
	{
		set_Value (COLUMNNAME_promuse, promuse);
	}

	/** Get promuse.
		@return promuse	  */
	public BigDecimal getpromuse () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_promuse);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Range.
		@param Range Range	  */
	public void setRange (BigDecimal Range)
	{
		set_Value (COLUMNNAME_Range, Range);
	}

	/** Get Range.
		@return Range	  */
	public BigDecimal getRange () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Range);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}