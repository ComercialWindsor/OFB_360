/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for OFB_RplExp
 *  @author Adempiere (generated) 
 *  @version Release 3.5.2aOFB v71G - $Id$ */
public class X_OFB_RplExp extends PO implements I_OFB_RplExp, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_OFB_RplExp (Properties ctx, int OFB_RplExp_ID, String trxName)
    {
      super (ctx, OFB_RplExp_ID, trxName);
      /** if (OFB_RplExp_ID == 0)
        {
			setOFB_RplExp_ID (0);
        } */
    }

    /** Load Constructor */
    public X_OFB_RplExp (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_OFB_RplExp[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set OFB_RplExp.
		@param OFB_RplExp_ID OFB_RplExp	  */
	public void setOFB_RplExp_ID (int OFB_RplExp_ID)
	{
		if (OFB_RplExp_ID < 1)
			 throw new IllegalArgumentException ("OFB_RplExp_ID is mandatory.");
		set_ValueNoCheck (COLUMNNAME_OFB_RplExp_ID, Integer.valueOf(OFB_RplExp_ID));
	}

	/** Get OFB_RplExp.
		@return OFB_RplExp	  */
	public int getOFB_RplExp_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_OFB_RplExp_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}