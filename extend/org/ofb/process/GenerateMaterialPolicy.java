/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
package org.ofb.process;

import java.math.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ProcessOT.java,v 1.2 2008/06/12 00:51:01  $
 */
public class GenerateMaterialPolicy extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	private int 			Record_ID;
	
	protected void prepare()
	{
		
		Record_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MInOut inout = new MInOut(getCtx(),Record_ID,get_TrxName());
		
		if(inout.getMovementType().equals(MInOut.MOVEMENTTYPE_CustomerReturns) || inout.getMovementType().equals(MInOut.MOVEMENTTYPE_VendorReceipts))
			return "este tipo de movimiento no genera politicas ya que es un recibo";
		
		
		MInOutLine[] lines = inout.getLines(false);
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++)
		{
			MInOutLine line = lines[lineIndex];
			
			int no = MInOutLineMA.deleteInOutLineMA(line.getM_InOutLine_ID(), get_TrxName());
			if (no > 0)
				log.config("Delete old #" + no);

			//	Incoming Trx
			String MovementType = inout.getMovementType();
			boolean inTrx = MovementType.charAt(1) == '+';	//	V+ Vendor Receipt


			boolean needSave = false;

			MProduct product = line.getProduct();

			//	Need to have Location
			if (product != null
					&& line.getM_Locator_ID() == 0)
			{
				//MWarehouse w = MWarehouse.get(getCtx(), getM_Warehouse_ID());
				line.setM_Warehouse_ID(inout.getM_Warehouse_ID());
				line.setM_Locator_ID(inTrx ? Env.ZERO : line.getMovementQty());	//	default Locator
				needSave = true;
			}

			//	Attribute Set Instance
			//  Create an  Attribute Set Instance to any receipt FIFO/LIFO
			if (product != null && line.getM_AttributeSetInstance_ID() == 0)
			{
				//Validate Transaction
				if (inout.getMovementType().compareTo(MInOut.MOVEMENTTYPE_CustomerReturns) == 0 
						|| inout.getMovementType().compareTo(MInOut.MOVEMENTTYPE_VendorReceipts) == 0 )
				{
					MAttributeSetInstance asi = null;
					//auto balance negative on hand
					MStorage[] storages = MStorage.getWarehouse(getCtx(), inout.getM_Warehouse_ID(), line.getM_Product_ID(), 0,
							null, MClient.MMPOLICY_FiFo.equals(product.getMMPolicy()), false, line.getM_Locator_ID(), get_TrxName());
					for (MStorage storage : storages)
					{
						if (storage.getQtyOnHand().signum() < 0)
						{
							asi = new MAttributeSetInstance(getCtx(), storage.getM_AttributeSetInstance_ID(), get_TrxName());
							break;
						}
					}
					//always create asi so fifo/lifo work.
					if (asi == null)
					{
						asi = MAttributeSetInstance.create(getCtx(), product, get_TrxName());
					}
					line.setM_AttributeSetInstance_ID(asi.getM_AttributeSetInstance_ID());
					log.config("New ASI=" + line);
					needSave = true;
				}
				// Create consume the Attribute Set Instance using policy FIFO/LIFO
				else if(inout.getMovementType().compareTo(MInOut.MOVEMENTTYPE_VendorReturns) == 0 || inout.getMovementType().compareTo(MInOut.MOVEMENTTYPE_CustomerShipment) == 0)
				{
					String MMPolicy = product.getMMPolicy();
					Timestamp minGuaranteeDate = inout.getMovementDate();
					MStorage[] storages = MStorage.getWarehouse(getCtx(), inout.getM_Warehouse_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							minGuaranteeDate, MClient.MMPOLICY_FiFo.equals(MMPolicy), true, line.getM_Locator_ID(), get_TrxName());
					BigDecimal qtyToDeliver = line.getMovementQty();
					for (MStorage storage: storages)
					{
						if (storage.getQtyOnHand().compareTo(qtyToDeliver) >= 0)
						{
							MInOutLineMA ma = new MInOutLineMA (line,
									storage.getM_AttributeSetInstance_ID(),
									qtyToDeliver);
							ma.saveEx();
							qtyToDeliver = Env.ZERO;
						}
						else
						{
							MInOutLineMA ma = new MInOutLineMA (line,
									storage.getM_AttributeSetInstance_ID(),
									storage.getQtyOnHand());
							ma.saveEx();
							qtyToDeliver = qtyToDeliver.subtract(storage.getQtyOnHand());
							log.fine( ma + ", QtyToDeliver=" + qtyToDeliver);
						}

						if (qtyToDeliver.signum() == 0)
							break;
					}

					if (qtyToDeliver.signum() != 0)
					{
						//deliver using new asi
						MAttributeSetInstance asi = MAttributeSetInstance.create(getCtx(), product, get_TrxName());
						int M_AttributeSetInstance_ID = asi.getM_AttributeSetInstance_ID();
						MInOutLineMA ma = new MInOutLineMA (line, M_AttributeSetInstance_ID, qtyToDeliver);
						ma.saveEx();
						log.fine("##: " + ma);
					}
				}	//	outgoing Trx
			}	//	attributeSetInstance

			if (needSave)
			{
				line.saveEx();
			}
			
		}
		
			return "Generados";
	}
	
}	//	CopyOrder
