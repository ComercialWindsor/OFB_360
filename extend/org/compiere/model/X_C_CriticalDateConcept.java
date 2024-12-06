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
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for C_CriticalDateConcept
 *  @author Adempiere (generated) 
 *  @version Release 3.6.0LTS - $Id$ */
public class X_C_CriticalDateConcept extends PO implements I_C_CriticalDateConcept, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151111L;

    /** Standard Constructor */
    public X_C_CriticalDateConcept (Properties ctx, int C_CriticalDateConcept_ID, String trxName)
    {
      super (ctx, C_CriticalDateConcept_ID, trxName);
      /** if (C_CriticalDateConcept_ID == 0)
        {
			setC_CriticalDateConcept_ID (0);
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_C_CriticalDateConcept (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_CriticalDateConcept[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set C_CriticalDateConcept.
		@param C_CriticalDateConcept_ID C_CriticalDateConcept	  */
	public void setC_CriticalDateConcept_ID (int C_CriticalDateConcept_ID)
	{
		if (C_CriticalDateConcept_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_CriticalDateConcept_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_CriticalDateConcept_ID, Integer.valueOf(C_CriticalDateConcept_ID));
	}

	/** Get C_CriticalDateConcept.
		@return C_CriticalDateConcept	  */
	public int getC_CriticalDateConcept_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_CriticalDateConcept_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ProjectOFB getC_ProjectOFB() throws RuntimeException
    {
		return (I_C_ProjectOFB)MTable.get(getCtx(), I_C_ProjectOFB.Table_Name)
			.getPO(getC_ProjectOFB_ID(), get_TrxName());	}

	/** Set C_ProjectOFB_ID.
		@param C_ProjectOFB_ID C_ProjectOFB_ID	  */
	public void setC_ProjectOFB_ID (int C_ProjectOFB_ID)
	{
		if (C_ProjectOFB_ID < 1) 
			set_Value (COLUMNNAME_C_ProjectOFB_ID, null);
		else 
			set_Value (COLUMNNAME_C_ProjectOFB_ID, Integer.valueOf(C_ProjectOFB_ID));
	}

	/** Get C_ProjectOFB_ID.
		@return C_ProjectOFB_ID	  */
	public int getC_ProjectOFB_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectOFB_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Classname.
		@param Classname 
		Java Classname
	  */
	public void setClassname (String Classname)
	{
		set_Value (COLUMNNAME_Classname, Classname);
	}

	/** Get Classname.
		@return Java Classname
	  */
	public String getClassname () 
	{
		return (String)get_Value(COLUMNNAME_Classname);
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

	/** Set Due.
		@param IsDue 
		Subscription Renewal is Due
	  */
	public void setIsDue (BigDecimal IsDue)
	{
		set_Value (COLUMNNAME_IsDue, IsDue);
	}

	/** Get Due.
		@return Subscription Renewal is Due
	  */
	public BigDecimal getIsDue () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_IsDue);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set PickupTruck.
		@param PickupTruck PickupTruck	  */
	public void setPickupTruck (boolean PickupTruck)
	{
		set_Value (COLUMNNAME_PickupTruck, Boolean.valueOf(PickupTruck));
	}

	/** Get PickupTruck.
		@return PickupTruck	  */
	public boolean isPickupTruck () 
	{
		Object oo = get_Value(COLUMNNAME_PickupTruck);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Requirement AD_Reference_ID=1000061 */
	public static final int REQUIREMENT_AD_Reference_ID=1000061;
	/** Legal = LE */
	public static final String REQUIREMENT_Legal = "LE";
	/** Interno = IN */
	public static final String REQUIREMENT_Interno = "IN";
	/** Cliente = CL */
	public static final String REQUIREMENT_Cliente = "CL";
	/** Set Requirement.
		@param Requirement Requirement	  */
	public void setRequirement (String Requirement)
	{

		set_Value (COLUMNNAME_Requirement, Requirement);
	}

	/** Get Requirement.
		@return Requirement	  */
	public String getRequirement () 
	{
		return (String)get_Value(COLUMNNAME_Requirement);
	}

	/** Set Semitrailer.
		@param Semitrailer Semitrailer	  */
	public void setSemitrailer (boolean Semitrailer)
	{
		set_Value (COLUMNNAME_Semitrailer, Boolean.valueOf(Semitrailer));
	}

	/** Get Semitrailer.
		@return Semitrailer	  */
	public boolean isSemitrailer () 
	{
		Object oo = get_Value(COLUMNNAME_Semitrailer);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set TankTruck.
		@param TankTruck TankTruck	  */
	public void setTankTruck (boolean TankTruck)
	{
		set_Value (COLUMNNAME_TankTruck, Boolean.valueOf(TankTruck));
	}

	/** Get TankTruck.
		@return TankTruck	  */
	public boolean isTankTruck () 
	{
		Object oo = get_Value(COLUMNNAME_TankTruck);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Tractors.
		@param Tractors Tractors	  */
	public void setTractors (boolean Tractors)
	{
		set_Value (COLUMNNAME_Tractors, Boolean.valueOf(Tractors));
	}

	/** Get Tractors.
		@return Tractors	  */
	public boolean isTractors () 
	{
		Object oo = get_Value(COLUMNNAME_Tractors);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Trailer.
		@param Trailer Trailer	  */
	public void setTrailer (boolean Trailer)
	{
		set_Value (COLUMNNAME_Trailer, Boolean.valueOf(Trailer));
	}

	/** Get Trailer.
		@return Trailer	  */
	public boolean isTrailer () 
	{
		Object oo = get_Value(COLUMNNAME_Trailer);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Type AD_Reference_ID=1000060 */
	public static final int TYPE_AD_Reference_ID=1000060;
	/** Equipo = EQ */
	public static final String TYPE_Equipo = "EQ";
	/** Persona = PE */
	public static final String TYPE_Persona = "PE";
	/** Set Type.
		@param Type 
		Type of Validation (SQL, Java Script, Java Language)
	  */
	public void setType (String Type)
	{

		set_Value (COLUMNNAME_Type, Type);
	}

	/** Get Type.
		@return Type of Validation (SQL, Java Script, Java Language)
	  */
	public String getType () 
	{
		return (String)get_Value(COLUMNNAME_Type);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}