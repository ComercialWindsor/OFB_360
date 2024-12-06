/**********************************************************************
 * This file is part of Adempiere ERP Bazaar                          *
 * http://www.adempiere.org                                           *
 *                                                                    *
 * Copyright (C) Trifon Trifonov.                                     *
 * Copyright (C) Contributors                                         *
 *                                                                    *
 * This program is free software, you can redistribute it and/or      *
 * modify it under the terms of the GNU General Public License        *
 * as published by the Free Software Foundation, either version 2     *
 * of the License, or (at your option) any later version.             *
 *                                                                    *
 * This program is distributed in the hope that it will be useful,    *
 * but WITHOUT ANY WARRANTY, without even the implied warranty of     *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the       *
 * GNU General Public License for more details.                       *
 *                                                                    *
 * You should have received a copy of the GNU General Public License  *
 * along with this program, if not, write to the Free Software        *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,         *
 * MA 02110-1301, USA.                                                *
 *                                                                    *
 * Contributors:                                                      *
 * - Trifon Trifonov (trifonnt@users.sourceforge.net)                 *
 *                                                                    *
 * Sponsors:                                                          *
 * - Company (http://www.site.com)                                    *
 **********************************************************************/
package org.compiere.model;

import java.math.BigDecimal;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for OFB_ImpProcessor
 *  @author Adempiere (generated) 
 *  @version Release 3.5.2aOFB v71G
 */
public interface I_OFB_ImpProcessor 
{

    /** TableName=OFB_ImpProcessor */
    public static final String Table_Name = "OFB_ImpProcessor";

    /** AD_Table_ID=1000036 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name Account */
    public static final String COLUMNNAME_Account = "Account";

	/** Set Account	  */
	public void setAccount (String Account);

	/** Get Account	  */
	public String getAccount();

    /** Column name DBInstance */
    public static final String COLUMNNAME_DBInstance = "DBInstance";

	/** Set Database Name.
	  * Database Name
	  */
	public void setDBInstance (String DBInstance);

	/** Get Database Name.
	  * Database Name
	  */
	public String getDBInstance();

    /** Column name DBType */
    public static final String COLUMNNAME_DBType = "DBType";

	/** Set DBType	  */
	public void setDBType (String DBType);

	/** Get DBType	  */
	public String getDBType();

    /** Column name Host */
    public static final String COLUMNNAME_Host = "Host";

	/** Set Host	  */
	public void setHost (String Host);

	/** Get Host	  */
	public String getHost();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name OFB_ImpProcessor_ID */
    public static final String COLUMNNAME_OFB_ImpProcessor_ID = "OFB_ImpProcessor_ID";

	/** Set OFB_ImpProcessor	  */
	public void setOFB_ImpProcessor_ID (int OFB_ImpProcessor_ID);

	/** Get OFB_ImpProcessor	  */
	public int getOFB_ImpProcessor_ID();

    /** Column name PasswordInfo */
    public static final String COLUMNNAME_PasswordInfo = "PasswordInfo";

	/** Set PasswordInfo	  */
	public void setPasswordInfo (String PasswordInfo);

	/** Get PasswordInfo	  */
	public String getPasswordInfo();

    /** Column name Port */
    public static final String COLUMNNAME_Port = "Port";

	/** Set Port	  */
	public void setPort (int Port);

	/** Get Port	  */
	public int getPort();
}
