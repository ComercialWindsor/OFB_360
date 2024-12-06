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

/** Generated Interface for CD_Summary
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS
 */
public interface I_CD_Summary 
{

    /** TableName=CD_Summary */
    public static final String Table_Name = "CD_Summary";

    /** AD_Table_ID=1000117 */
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

    /** Column name CD_Header_ID */
    public static final String COLUMNNAME_CD_Header_ID = "CD_Header_ID";

	/** Set CD_Header	  */
	public void setCD_Header_ID (int CD_Header_ID);

	/** Get CD_Header	  */
	public int getCD_Header_ID();

	public I_CD_Header getCD_Header() throws RuntimeException;

    /** Column name CD_Summary_ID */
    public static final String COLUMNNAME_CD_Summary_ID = "CD_Summary_ID";

	/** Set CD_Summary	  */
	public void setCD_Summary_ID (int CD_Summary_ID);

	/** Get CD_Summary	  */
	public int getCD_Summary_ID();

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

    /** Column name CreditLine */
    public static final String COLUMNNAME_CreditLine = "CreditLine";

	/** Set CreditLine	  */
	public void setCreditLine (BigDecimal CreditLine);

	/** Get CreditLine	  */
	public BigDecimal getCreditLine();

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

    /** Column name GrandTotal */
    public static final String COLUMNNAME_GrandTotal = "GrandTotal";

	/** Set Grand Total.
	  * Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal);

	/** Get Grand Total.
	  * Total amount of document
	  */
	public BigDecimal getGrandTotal();

    /** Column name InvestmentAmount */
    public static final String COLUMNNAME_InvestmentAmount = "InvestmentAmount";

	/** Set InvestmentAmount	  */
	public void setInvestmentAmount (BigDecimal InvestmentAmount);

	/** Get InvestmentAmount	  */
	public BigDecimal getInvestmentAmount();

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

    /** Column name Surplus */
    public static final String COLUMNNAME_Surplus = "Surplus";

	/** Set Surplus	  */
	public void setSurplus (BigDecimal Surplus);

	/** Get Surplus	  */
	public BigDecimal getSurplus();

    /** Column name TotalBanks */
    public static final String COLUMNNAME_TotalBanks = "TotalBanks";

	/** Set TotalBanks	  */
	public void setTotalBanks (BigDecimal TotalBanks);

	/** Get TotalBanks	  */
	public BigDecimal getTotalBanks();

    /** Column name TotalExpenses */
    public static final String COLUMNNAME_TotalExpenses = "TotalExpenses";

	/** Set TotalExpenses	  */
	public void setTotalExpenses (BigDecimal TotalExpenses);

	/** Get TotalExpenses	  */
	public BigDecimal getTotalExpenses();

    /** Column name TotalIncome */
    public static final String COLUMNNAME_TotalIncome = "TotalIncome";

	/** Set TotalIncome	  */
	public void setTotalIncome (BigDecimal TotalIncome);

	/** Get TotalIncome	  */
	public BigDecimal getTotalIncome();

    /** Column name TotalTransfer */
    public static final String COLUMNNAME_TotalTransfer = "TotalTransfer";

	/** Set TotalTransfer	  */
	public void setTotalTransfer (BigDecimal TotalTransfer);

	/** Get TotalTransfer	  */
	public BigDecimal getTotalTransfer();

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
