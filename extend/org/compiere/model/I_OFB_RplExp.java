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

/** Generated Interface for OFB_RplExp
 *  @author Adempiere (generated) 
 *  @version Release 3.5.2aOFB v71G
 */
public interface I_OFB_RplExp 
{

    /** TableName=OFB_RplExp */
    public static final String Table_Name = "OFB_RplExp";

    /** AD_Table_ID=1000035 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name OFB_RplExp_ID */
    public static final String COLUMNNAME_OFB_RplExp_ID = "OFB_RplExp_ID";

	/** Set OFB_RplExp	  */
	public void setOFB_RplExp_ID (int OFB_RplExp_ID);

	/** Get OFB_RplExp	  */
	public int getOFB_RplExp_ID();

    /** Column name Record_ID */
    public static final String COLUMNNAME_Record_ID = "Record_ID";
}
