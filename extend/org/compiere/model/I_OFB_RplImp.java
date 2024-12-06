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

/** Generated Interface for OFB_RplImp
 *  @author Adempiere (generated) 
 *  @version Release 3.5.2aOFB v71G
 */
public interface I_OFB_RplImp 
{

    /** TableName=OFB_RplImp */
    public static final String Table_Name = "OFB_RplImp";

    /** AD_Table_ID=1000034 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name OFB_RplImp_ID */
    public static final String COLUMNNAME_OFB_RplImp_ID = "OFB_RplImp_ID";

	/** Set OFB_RplImp	  */
	public void setOFB_RplImp_ID (int OFB_RplImp_ID);

	/** Get OFB_RplImp	  */
	public int getOFB_RplImp_ID();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name RemoteHost */
    public static final String COLUMNNAME_RemoteHost = "RemoteHost";

	/** Set RemoteHost	  */
	public void setRemoteHost (String RemoteHost);

	/** Get RemoteHost	  */
	public String getRemoteHost();

    /** Column name RemoteID */
    public static final String COLUMNNAME_RemoteID = "RemoteID";

	/** Set RemoteID	  */
	public void setRemoteID (int RemoteID);

	/** Get RemoteID	  */
	public int getRemoteID();

    /** Column name RemoteRecord_ID */
    public static final String COLUMNNAME_RemoteRecord_ID = "RemoteRecord_ID";

	/** Set RemoteRecord_ID	  */
	public void setRemoteRecord_ID (int RemoteRecord_ID);

	/** Get RemoteRecord_ID	  */
	public int getRemoteRecord_ID();

    /** Column name TableName */
    public static final String COLUMNNAME_TableName = "TableName";

	/** Set DB Table Name.
	  * Name of the table in the database
	  */
	public void setTableName (String TableName);

	/** Get DB Table Name.
	  * Name of the table in the database
	  */
	public String getTableName();

    /** Column name xml */
    public static final String COLUMNNAME_xml = "xml";

	/** Set xml	  */
	public void setxml (String xml);

	/** Get xml	  */
	public String getxml();
}
