/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
package org.compiere.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.python.core.exceptions;

/**
 *  Physical Inventory Model
 *
 *  @author Jorg Janke
 *  @version $Id: MInventory.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 *  @author victor.perez@e-evolution.com, e-Evolution http://www.e-evolution.com
 * 			<li>FR [ 1948157  ]  Is necessary the reference for document reverse
 * 			<li> FR [ 2520591 ] Support multiples calendar for Org 
 *			@see http://sourceforge.net/tracker2/?func=detail&atid=879335&aid=2520591&group_id=176962 	
 *  @author Armen Rizal, Goodwill Consulting
 * 			<li>BF [ 1745154 ] Cost in Reversing Material Related Docs
 *  @see http://sourceforge.net/tracker/?func=detail&atid=879335&aid=1948157&group_id=176962
 */
public class MInventory extends X_M_Inventory implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7137974064086172763L;

	/**
	 * 	Get Inventory from Cache
	 *	@param ctx context
	 *	@param M_Inventory_ID id
	 *	@return MInventory
	 */
	public static MInventory get (Properties ctx, int M_Inventory_ID)
	{
		Integer key = new Integer (M_Inventory_ID);
		MInventory retValue = (MInventory) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MInventory (ctx, M_Inventory_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MInventory> s_cache = new CCache<Integer,MInventory>("M_Inventory", 5, 5);


	/**
	 * 	Standard Constructor
	 *	@param ctx context 
	 *	@param M_Inventory_ID id
	 *	@param trxName transaction
	 */
	public MInventory (Properties ctx, int M_Inventory_ID, String trxName)
	{
		super (ctx, M_Inventory_ID, trxName);
		if (M_Inventory_ID == 0)
		{
		//	setName (null);
		//  setM_Warehouse_ID (0);		//	FK
			setMovementDate (new Timestamp(System.currentTimeMillis()));
			setDocAction (DOCACTION_Complete);	// CO
			setDocStatus (DOCSTATUS_Drafted);	// DR
			setIsApproved (false);
			setMovementDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
			setPosted (false);
			setProcessed (false);
		}
	}	//	MInventory

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MInventory (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInventory

	/**
	 * Warehouse Constructor
	 * @param wh warehouse
	 * @deprecated since 3.5.3a . Please use {@link #MInventory(MWarehouse, String)}.
	 */
	public MInventory (MWarehouse wh)
	{
		this(wh, wh.get_TrxName());
	}	//	MInventory
	
	/**
	 * Warehouse Constructor
	 * @param wh
	 * @param trxName
	 */
	public MInventory (MWarehouse wh, String trxName)
	{
		this (wh.getCtx(), 0, trxName);
		setClientOrg(wh);
		setM_Warehouse_ID(wh.getM_Warehouse_ID());
	}
	
	
	/**	Lines						*/
	private MInventoryLine[]	m_lines = null;
	
	/**
	 * 	Get Lines
	 *	@param requery requery
	 *	@return array of lines
	 */
	public MInventoryLine[] getLines (boolean requery)
	{
		if (m_lines != null && !requery) {
			set_TrxName(m_lines, get_TrxName());
			return m_lines;
		}
		//
		List<MInventoryLine> list = new Query(getCtx(), I_M_InventoryLine.Table_Name, "M_Inventory_ID=?", get_TrxName())
										.setParameters(get_ID())
										.setOrderBy(MInventoryLine.COLUMNNAME_Line)
										.list();
		m_lines = list.toArray(new MInventoryLine[list.size()]);
		return m_lines;
	}	//	getLines
	
	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription
	
	/**
	 * 	Overwrite Client/Org - from Import.
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInventory[");
		sb.append (get_ID())
			.append ("-").append (getDocumentNo())
			.append (",M_Warehouse_ID=").append(getM_Warehouse_ID())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName()+get_ID()+"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getC_DocType_ID() == 0)
		{
			MDocType types[] = MDocType.getOfDocBaseType(getCtx(), MDocType.DOCBASETYPE_MaterialPhysicalInventory);
			if (types.length > 0)	//	get first
				setC_DocType_ID(types[0].getC_DocType_ID());
			else
			{
				log.saveError("Error", Msg.parseTranslation(getCtx(), "@NotFound@ @C_DocType_ID@"));
				return false;
			}
		}
		return true;
	}	//	beforeSave
	
	
	/**
	 * 	Set Processed.
	 * 	Propagate to Lines/Taxes
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		//
		final String sql = "UPDATE M_InventoryLine SET Processed=? WHERE M_Inventory_ID=?";
		int noLine = DB.executeUpdateEx(sql, new Object[]{processed, getM_Inventory_ID()}, get_TrxName());
		m_lines = null;
		log.fine("Processed=" + processed + " - Lines=" + noLine);
	}	//	setProcessed

	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	processIt
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		log.info(toString());
		setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info(toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt
	
	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Std Period open?
		MPeriod.testPeriodOpen(getCtx(), getMovementDate(), MDocType.DOCBASETYPE_MaterialPhysicalInventory, getAD_Org_ID());
		MInventoryLine[] lines = getLines(false);
		if (lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}

		//faaguilar OFB begin
		MClient clientV = new MClient(getCtx(), get_TrxName());
		for(MInventoryLine line: lines){
			if(!isReversal())
			{
				Boolean useNI = false; 
				try
				{
					useNI = clientV.get_ValueAsBoolean("UseNegativeInventory");
				}catch(Exception e)
				{
					useNI = false;
				}
		
				
				if (useNI)
				{
					;
				}	
				else
				{
					if (line.getQtyCount().signum()< 0)
					{
						m_processMsg = "Not Negative " + " line:"+line.getLine();
						return DocAction.STATUS_Invalid;
					}
				}
			}
			if("N".equals(DB.getSQLValueString(get_TrxName(), "select StockNegative from AD_Client where AD_Client_ID="+getAD_Client_ID())) )
				if(line.getQtyInternalUse().signum()>0)
				{
					//@mfrojas <- IMPORTANTE
					// se agrega validación para que no revise el stock de productos que son SERVICIO
					log.config("locator "+line.getM_Locator_ID());
					// IMPORTANTE!!!
					if(line.getM_Product().getProductType().compareToIgnoreCase("S")!=0)
					{	
						BigDecimal qf = getQtyOnHand(line.getM_AttributeSetInstance_ID(),line.getM_Product_ID(),line.getM_Locator_ID());
						log.config(" qf: " + qf.longValue() + " QtyI: "+line.getQtyInternalUse().longValue());
					
						if(qf.longValue()<line.getQtyInternalUse().longValue())
						{
							m_processMsg = "No se puede consumir la linea (no stock) " + " line:"+line.getLine();
							return DocAction.STATUS_Invalid;
						}
					}
					//end validacion @mfrojas !!!
				}
		}
		
		//validacion de consumo o inventario fisico
		boolean esConsumo = false;
		for(MInventoryLine line: lines)
			if(line.getQtyInternalUse().signum()!=0)
				esConsumo= true;
		
		if(esConsumo)
			for(MInventoryLine line: lines)
				if(line.getQtyInternalUse().signum()==0)
				{
					m_processMsg = "no puede tener cantidad 0 en un consumo " + " line:"+line.getLine();
					return DocAction.STATUS_Invalid;
				}
		//faaguilar OFB end
		
		//faaguilar OFB begin
		String validation=budgetValidation();
		if(validation.length()>2)
		{
			DB.executeUpdate("update m_inventory set help='"+validation+"' where m_inventory_id="+getM_Inventory_ID(), get_TrxName());
			try{
			DB.commit(false, get_TrxName());
			}catch(Exception e){}
			m_processMsg = validation;
			return DocAction.STATUS_Invalid;
		}
		//faaguilar OFB end
		//	TODO: Add up Amounts
	//	setApprovalAmt();
		
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt
	
	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info(toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info(toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info(toString());
		
		String msg=completeBudget();//faaguilar OFB
		if(msg.length()>2){
			m_processMsg=msg;
			return DocAction.STATUS_Invalid;
		}

		MInventoryLine[] lines = getLines(false);
		for (MInventoryLine line : lines)
		{
			if (!line.isActive())
				continue;

			MProduct product = line.getProduct();	

			//Get Quantity to Inventory Inernal Use
			BigDecimal qtyDiff = line.getQtyInternalUse().negate();
			//ininoles cambiamos signo a la cantidad. TSM devolución de consumo
			if(getC_DocType().getDocBaseType().compareToIgnoreCase("RPI") == 0)
				qtyDiff = qtyDiff.negate();
			
			//If Quantity to Inventory Internal Use = Zero Then is Physical Inventory  Else is  Inventory Internal Use 
			if (qtyDiff.signum() == 0)
				qtyDiff = line.getQtyCount().subtract(line.getQtyBook());

			//Ignore the Material Policy when is Reverse Correction
			if(!isReversal())
				checkMaterialPolicy(line, qtyDiff);

			//	Stock Movement - Counterpart MOrder.reserveStock
			if (product != null 
					&& product.isStocked() )
			{
				log.fine("Material Transaction");
				MTransaction mtrx = null; 

				//If AttributeSetInstance = Zero then create new  AttributeSetInstance use Inventory Line MA else use current AttributeSetInstance
				if (line.getM_AttributeSetInstance_ID() == 0 || qtyDiff.compareTo(Env.ZERO) == 0)
				{
					MInventoryLineMA mas[] = MInventoryLineMA.get(getCtx(),
							line.getM_InventoryLine_ID(), get_TrxName());

					for (int j = 0; j < mas.length; j++)
					{
						MInventoryLineMA ma = mas[j];
						BigDecimal QtyMA = ma.getMovementQty();
						BigDecimal QtyNew = QtyMA.add(qtyDiff);
						log.fine("Diff=" + qtyDiff 
								+ " - Instance OnHand=" + QtyMA + "->" + QtyNew);

						if (!MStorage.add(getCtx(), getM_Warehouse_ID(),
								line.getM_Locator_ID(),
								line.getM_Product_ID(), 
								ma.getM_AttributeSetInstance_ID(), 0, 
								QtyMA.negate(), Env.ZERO, Env.ZERO, get_TrxName()))
						{
							m_processMsg = "Cannot correct Inventory (MA)";
							return DocAction.STATUS_Invalid;
						}

						// Only Update Date Last Inventory if is a Physical Inventory
						if(line.getQtyInternalUse().compareTo(Env.ZERO) == 0)
						{	
							MStorage storage = MStorage.get(getCtx(), line.getM_Locator_ID(), 
									line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(), get_TrxName());						
							storage.setDateLastInventory(getMovementDate());
							if (!storage.save(get_TrxName()))
							{
								m_processMsg = "Storage not updated(2)";
								return DocAction.STATUS_Invalid;
							}
						}

						String m_MovementType =null;
						if(QtyMA.negate().compareTo(Env.ZERO) > 0 )
							m_MovementType = MTransaction.MOVEMENTTYPE_InventoryIn;
						else
							m_MovementType = MTransaction.MOVEMENTTYPE_InventoryOut;
						//ininoles nuevo tipo de documento TSM Devolucion de consumo sobreecribo tipo de movimiento
						/*if(getC_DocType().getDocBaseType().compareToIgnoreCase("RPI") == 0)
						{
							m_MovementType = MTransaction.MOVEMENTTYPE_InventoryIn;
							QtyMA = QtyMA.negate();
						}*/
						//	Transaction
						mtrx = new MTransaction (getCtx(), line.getAD_Org_ID(), m_MovementType,
								line.getM_Locator_ID(), line.getM_Product_ID(), ma.getM_AttributeSetInstance_ID(),
								QtyMA.negate(), getMovementDate(), get_TrxName());
						
							mtrx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
							if (!mtrx.save())
							{
								m_processMsg = "Transaction not inserted(2)";
								return DocAction.STATUS_Invalid;
							}
							if(QtyMA.signum() != 0)
							{	
								String err = createCostDetail(line, ma.getM_AttributeSetInstance_ID() , QtyMA.negate());
								if (err != null && err.length() > 0) {
									m_processMsg = err;
									return DocAction.STATUS_Invalid;
								}
							}
							
							qtyDiff = QtyNew;						

					}	
				}

				//sLine.getM_AttributeSetInstance_ID() != 0
				// Fallback
				if (mtrx == null)
				{
					//Fallback: Update Storage - see also VMatch.createMatchRecord
					if (!MStorage.add(getCtx(), getM_Warehouse_ID(),
							line.getM_Locator_ID(),
							line.getM_Product_ID(), 
							line.getM_AttributeSetInstance_ID(), 0, 
							qtyDiff, Env.ZERO, Env.ZERO, get_TrxName()))
					{
						m_processMsg = "Cannot correct Inventory (MA)";
						return DocAction.STATUS_Invalid;
					}

					// Only Update Date Last Inventory if is a Physical Inventory
					if(line.getQtyInternalUse().compareTo(Env.ZERO) == 0)
					{	
						MStorage storage = MStorage.get(getCtx(), line.getM_Locator_ID(), 
								line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(), get_TrxName());						

						storage.setDateLastInventory(getMovementDate());
						if (!storage.save(get_TrxName()))
						{
							m_processMsg = "Storage not updated(2)";
							return DocAction.STATUS_Invalid;
						}
					}

					String m_MovementType = null;
					if(qtyDiff.compareTo(Env.ZERO) > 0 )
						m_MovementType = MTransaction.MOVEMENTTYPE_InventoryIn;
					else
						m_MovementType = MTransaction.MOVEMENTTYPE_InventoryOut;
					//	Transaction
					mtrx = new MTransaction (getCtx(), line.getAD_Org_ID(), m_MovementType,
							line.getM_Locator_ID(), line.getM_Product_ID(), line.getM_AttributeSetInstance_ID(),
							qtyDiff, getMovementDate(), get_TrxName());
					mtrx.setM_InventoryLine_ID(line.getM_InventoryLine_ID());
					if (!mtrx.save())
					{
						m_processMsg = "Transaction not inserted(2)";
						return DocAction.STATUS_Invalid;
					}
					
					if(qtyDiff.signum() != 0)
					{	
						String err = createCostDetail(line, line.getM_AttributeSetInstance_ID(), qtyDiff);
						if (err != null && err.length() > 0) {
							m_processMsg = err;
							return DocAction.STATUS_Invalid;
						}
					}
				}	//	Fallback
			}	//	stock movement

		}	//	for all lines

		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}

		
		// Set the definite document number after completed (if needed)
		setDefiniteDocumentNo();

		
		//
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setMovementDate(new Timestamp (System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = DB.getDocumentNo(getC_DocType_ID(), get_TrxName(), true, this);
			if (value != null)
				setDocumentNo(value);
		}
	}

	/**
	 * 	Check Material Policy.
	 */
	private void checkMaterialPolicy(MInventoryLine line, BigDecimal qtyDiff)
	{
		int no = MInventoryLineMA.deleteInventoryLineMA(line.getM_InventoryLine_ID(), get_TrxName());
		if (no > 0)
			log.config("Delete old #" + no);

		//	Check Line
		boolean needSave = false;
		//	Attribute Set Instance
		if (line.getM_AttributeSetInstance_ID() == 0)
		{
			MProduct product = MProduct.get(getCtx(), line.getM_Product_ID());
			if (qtyDiff.signum() > 0)	//	Incoming Trx
			{
				MAttributeSetInstance asi = null;
				//auto balance negative on hand
				MStorage[] storages = MStorage.getWarehouse(getCtx(), getM_Warehouse_ID(), line.getM_Product_ID(), 0,
						null, MClient.MMPOLICY_FiFo.equals(product.getMMPolicy()), false, line.getM_Locator_ID(), get_TrxName());
				for (MStorage storage : storages)
				{
					if (storage.getQtyOnHand().signum() < 0)
					{
						asi = new MAttributeSetInstance(getCtx(), storage.getM_AttributeSetInstance_ID(), get_TrxName());
						break;
					}
				}
				if (asi == null)
				{
					asi = MAttributeSetInstance.create(getCtx(), product, get_TrxName());
				}
				line.setM_AttributeSetInstance_ID(asi.getM_AttributeSetInstance_ID());
				needSave = true;
			}
			else	//	Outgoing Trx
			{
				String MMPolicy = product.getMMPolicy();
				MStorage[] storages = MStorage.getWarehouse(getCtx(), getM_Warehouse_ID(), line.getM_Product_ID(), 0,
						null, MClient.MMPOLICY_FiFo.equals(MMPolicy), true, line.getM_Locator_ID(), get_TrxName());

				BigDecimal qtyToDeliver = qtyDiff.negate();

				for (MStorage storage: storages)
				{					
					if (storage.getQtyOnHand().compareTo(qtyToDeliver) >= 0)
					{
						MInventoryLineMA ma = new MInventoryLineMA (line, 
								storage.getM_AttributeSetInstance_ID(),
								qtyToDeliver);
						ma.saveEx();		
						qtyToDeliver = Env.ZERO;
						log.fine( ma + ", QtyToDeliver=" + qtyToDeliver);		
					}
					else
					{	
						MInventoryLineMA ma = new MInventoryLineMA (line, 
								storage.getM_AttributeSetInstance_ID(),
								storage.getQtyOnHand());
						ma.saveEx();	
						qtyToDeliver = qtyToDeliver.subtract(storage.getQtyOnHand());
						log.fine( ma + ", QtyToDeliver=" + qtyToDeliver);		
					}
					if (qtyToDeliver.signum() == 0)
						break;
				}

				//	No AttributeSetInstance found for remainder
				if (qtyToDeliver.signum() != 0)
				{
					//deliver using new asi
					MAttributeSetInstance asi = MAttributeSetInstance.create(getCtx(), product, get_TrxName());
					int M_AttributeSetInstance_ID = asi.getM_AttributeSetInstance_ID();
					MInventoryLineMA ma = new MInventoryLineMA (line, M_AttributeSetInstance_ID , qtyToDeliver);

					ma.saveEx();
					log.fine("##: " + ma);
				}
			}	//	outgoing Trx

			if (needSave)
			{
				line.saveEx();
			}
		}	//	for all lines

	}	//	checkMaterialPolicy

	/**
	 * 	Void Document.
	 * 	@return false 
	 */
	public boolean voidIt()
	{
		log.info(toString());
		// Before Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
		if (m_processMsg != null)
			return false;
		
		if (DOCSTATUS_Closed.equals(getDocStatus())
			|| DOCSTATUS_Reversed.equals(getDocStatus())
			|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			m_processMsg = "Document Closed: " + getDocStatus();
			return false;
		}

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
			|| DOCSTATUS_Invalid.equals(getDocStatus())
			|| DOCSTATUS_InProgress.equals(getDocStatus())
			|| DOCSTATUS_Approved.equals(getDocStatus())
			|| DOCSTATUS_NotApproved.equals(getDocStatus()) )
		{
			//	Set lines to 0
			MInventoryLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				MInventoryLine line = lines[i];
				BigDecimal oldCount = line.getQtyCount();
				BigDecimal oldInternal = line.getQtyInternalUse();
				if (oldCount.compareTo(line.getQtyBook()) != 0 
					|| oldInternal.signum() != 0)
				{
					line.setQtyInternalUse(Env.ZERO);
					line.setQtyCount(line.getQtyBook());
					line.addDescription("Void (" + oldCount + "/" + oldInternal + ")");
					line.save(get_TrxName());
				}
			}
		}
		else
		{
			return reverseCorrectIt();
		}
			
		// After Void
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (m_processMsg != null)
			return false;		
		setProcessed(true);
		setDocAction(DOCACTION_None);
		return true;
	}	//	voidIt
	
	/**
	 * 	Close Document.
	 * 	@return true if success 
	 */
	public boolean closeIt()
	{
		log.info(toString());
		// Before Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
		if (m_processMsg != null)
			return false;

		setDocAction(DOCACTION_None);
		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;
		return true;
	}	//	closeIt
	
	/**
	 * 	Reverse Correction
	 * 	@return false 
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;

		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		MPeriod.testPeriodOpen(getCtx(), getMovementDate(), dt.getDocBaseType(), getAD_Org_ID());

		//	Deep Copy
		MInventory reversal = new MInventory(getCtx(), 0, get_TrxName());
		copyValues(this, reversal, getAD_Client_ID(), getAD_Org_ID());
		reversal.setDocStatus(DOCSTATUS_Drafted);
		reversal.setDocAction(DOCACTION_Complete);
		reversal.setIsApproved (false);
		reversal.setPosted(false);
		reversal.setProcessed(false);
		reversal.addDescription("{->" + getDocumentNo() + ")");
		//FR1948157
		reversal.setReversal_ID(getM_Inventory_ID());
		reversal.saveEx();
		reversal.setReversal(true);

		//	Reverse Line Qty
		MInventoryLine[] oLines = getLines(true);
		for (int i = 0; i < oLines.length; i++)
		{
			MInventoryLine oLine = oLines[i];
			MInventoryLine rLine = new MInventoryLine(getCtx(), 0, get_TrxName());
			copyValues(oLine, rLine, oLine.getAD_Client_ID(), oLine.getAD_Org_ID());
			rLine.setM_Inventory_ID(reversal.getM_Inventory_ID());
			rLine.setParent(reversal);
			//AZ Goodwill
			// store original (voided/reversed) document line
			rLine.setReversalLine_ID(oLine.getM_InventoryLine_ID());
			//
			rLine.setQtyBook (oLine.getQtyCount());		//	switch
			rLine.setQtyCount (oLine.getQtyBook());
			rLine.setQtyInternalUse (oLine.getQtyInternalUse().negate());		
			
			rLine.saveEx();

			//We need to copy MA
			if (rLine.getM_AttributeSetInstance_ID() == 0)
			{
				MInventoryLineMA mas[] = MInventoryLineMA.get(getCtx(),
						oLines[i].getM_InventoryLine_ID(), get_TrxName());
				for (int j = 0; j < mas.length; j++)
				{
					MInventoryLineMA ma = new MInventoryLineMA (rLine, 
							mas[j].getM_AttributeSetInstance_ID(),
							mas[j].getMovementQty().negate());
					ma.saveEx();
				}
			}
		}
		//
		if (!reversal.processIt(DocAction.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return false;
		}
		reversal.closeIt();
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.saveEx();
		m_processMsg = reversal.getDocumentNo();

		//	Update Reversed (this)
		addDescription("(" + reversal.getDocumentNo() + "<-)");
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		setProcessed(true);
		//FR1948157
		setReversal_ID(reversal.getM_Inventory_ID());
		setDocStatus(DOCSTATUS_Reversed);	//	may come from void
		setDocAction(DOCACTION_None);

		return true;
	}	//	reverseCorrectIt
	
	/**
	 * 	Reverse Accrual
	 * 	@return false 
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		// Before reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		// After reverseAccrual
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (m_processMsg != null)
			return false;
		
		return false;
	}	//	reverseAccrualIt
	
	/** 
	 * 	Re-activate
	 * 	@return false 
	 */
	public boolean reActivateIt()
	{
		log.info(toString());
		// Before reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
		if (m_processMsg != null)
			return false;	
		
		// After reActivate
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (m_processMsg != null)
			return false;
		
		return false;
	}	//	reActivateIt
	
	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
		//	: Total Lines = 123.00 (#1)
		sb.append(": ")
			.append(Msg.translate(getCtx(),"ApprovalAmt")).append("=").append(getApprovalAmt())
			.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && getDescription().length() > 0)
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary
	
	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
		return getUpdatedBy();
	}	//	getDoc_User_ID
	
	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID()
	{
	//	MPriceList pl = MPriceList.get(getCtx(), getM_PriceList_ID());
	//	return pl.getC_Currency_ID();
		return 0;
	}	//	getC_Currency_ID
	
	/** Reversal Flag		*/
	private boolean m_reversal = false;
	
	/**
	 * 	Set Reversal
	 *	@param reversal reversal
	 */
	private void setReversal(boolean reversal)
	{
		m_reversal = reversal;
	}	//	setReversal
	/**
	 * 	Is Reversal
	 *	@return reversal
	 */
	private boolean isReversal()
	{
		return m_reversal;
	}	//	isReversal
	
	/**
	 * Create Cost Detail
	 * @param line
	 * @param Qty
	 * @return an EMPTY String on success otherwise an ERROR message
	 */
	private String createCostDetail(MInventoryLine line, int M_AttributeSetInstance_ID, BigDecimal qty)
	{
		return "";/*
		// Get Account Schemas to create MCostDetail
		MAcctSchema[] acctschemas = MAcctSchema.getClientAcctSchema(getCtx(), getAD_Client_ID());
		for(int asn = 0; asn < acctschemas.length; asn++)
		{
			MAcctSchema as = acctschemas[asn];
			
			if (as.isSkipOrg(getAD_Org_ID()) || as.isSkipOrg(line.getAD_Org_ID()))
			{
				continue;
			}
			
			BigDecimal costs = Env.ZERO;
			if (isReversal())
			{				
				String sql = "SELECT amt * -1 FROM M_CostDetail WHERE M_InventoryLine_ID=?"; // negate costs				
				MProduct product = new MProduct(getCtx(), line.getM_Product_ID(), line.get_TrxName());
				String CostingLevel = product.getCostingLevel(as);
				if (MAcctSchema.COSTINGLEVEL_Organization.equals(CostingLevel))
					sql = sql + " AND AD_Org_ID=" + getAD_Org_ID(); 
				else if (MAcctSchema.COSTINGLEVEL_BatchLot.equals(CostingLevel) && M_AttributeSetInstance_ID != 0)
					sql = sql + " AND M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID;
				costs = DB.getSQLValueBD(line.get_TrxName(), sql, line.getReversalLine_ID());
			}
			else 
			{
				ProductCost pc = new ProductCost (getCtx(), 
						line.getM_Product_ID(), M_AttributeSetInstance_ID, line.get_TrxName());
				pc.setQty(qty);
				costs = pc.getProductCosts(as, line.getAD_Org_ID(), as.getCostingMethod(), 0,true);							
			}
			if (costs == null)
			{
				return "No Costs for " + line.getProduct().getName();
			}
			*/
			// Set Total Amount and Total Quantity from Inventory
			/*MCostDetail.createInventory(as, line.getAD_Org_ID(), 
					line.getM_Product_ID(), M_AttributeSetInstance_ID,
					line.getM_InventoryLine_ID(), 0,	//	no cost element
					costs, qty,			
					line.getDescription(), line.get_TrxName());*///faaguilar OFB original code commented
			/*OFBProductCost.createInventory(as, line.getAD_Org_ID(), 
					line.getM_Product_ID(), M_AttributeSetInstance_ID,
					line.getM_InventoryLine_ID(), 0,	//	no cost element
					costs, qty,			
					line.getDescription(), line.get_TrxName());//faaguilar OFB
		}
		
		return "";*/
	}

	/**
	 * 	Document Status is Complete or Closed
	 *	@return true if CO, CL or RE
	 */
	public boolean isComplete()
	{
		String ds = getDocStatus();
		return DOCSTATUS_Completed.equals(ds) 
			|| DOCSTATUS_Closed.equals(ds)
			|| DOCSTATUS_Reversed.equals(ds);
	}	//	isComplete
	
	
	/**
	 * faaguilar OFB custom methods begin
	 * */
	
	/**faaguilar OFB
	 * verificacion de presupuesto
	 * */
	public String budgetValidation(){
		
		MClient client=MClient.get(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()));
		Boolean useBudget=false;
		try{
			useBudget=client.get_ValueAsBoolean("UseBudget");
			log.config("useBudget :" + useBudget);
		}
		catch(Exception e){}
		
		if(useBudget){
			MRole myrole = new MRole(getCtx(),Env.getAD_Role_ID(getCtx()),get_TrxName() );
			MUser currentUser = new MUser(getCtx(),Env.getAD_User_ID(getCtx()),get_TrxName() );
			String commenthelp=new String();
			
			if(this.get_ValueAsBoolean("Signature"))
				return "";
			
			String sql="select sum(il.qtyInternalUse),c.m_product_category_id,il.ad_org_id,il.m_product_id,c.name from M_InventoryLine il"
					+ " inner join M_Product p on (il.m_product_id=p.m_product_id)"
					+ " inner join M_Product_Category c on (p.m_product_category_id=c.m_product_category_id)"
					+ " where il.m_inventory_id=? and il.qtyinternaluse!=0" +
					" group by c.m_product_category_id,il.ad_org_id,il.m_product_id,c.name" +
					" order by c.m_product_category_id";
			
			PreparedStatement pstmt = null;
			boolean sobrePresupuesto=false;
			boolean sobreanual=false;
			int last_category =0;
			BigDecimal acumCost = Env.ZERO;
			try{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				pstmt.setInt (1, getM_Inventory_ID()  );
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next ())
				{

					if(rs.getInt(1)==0)//no es consumo
						continue;
					
					long ActualBudget=getBudget(rs.getInt(2),getMovementDate(),rs.getInt(3),0);
					long YearBudget =getBudgetYear(rs.getInt(2),MPeriod.get(getCtx(), getMovementDate()).getC_Year_ID() ,rs.getInt(3),0 );
					
					BigDecimal costo = OFBProductCost.getProductCost(rs.getInt(4), rs.getInt(3), rs.getBigDecimal(1), get_TrxName(), getCtx());
					if(costo.longValue()==0)
					{
						this.set_CustomColumn("Help",MProduct.get(getCtx(), rs.getInt(4)).getName()+ " - No Cost ");
						return DocAction.STATUS_Invalid;
					}
					
					log.config(MProduct.get(getCtx(), rs.getInt(4)).getName()+"-"+ costo);
					log.config("ActualBudget :"+ActualBudget);
					log.config("YearBudget :"+YearBudget);
					
					
					if(last_category!=rs.getInt(2))
					{
						last_category = rs.getInt(2);
						acumCost = costo;
					}
					else
						acumCost =  acumCost.add(costo);
					
					log.config("acumCost :" + acumCost);
					
					if(ActualBudget<acumCost.longValue()){
						
						
						log.config("rs.getDouble(1): "+rs.getDouble(1));
						log.config("ActualBudget: "+ActualBudget);
						log.config("valor: "+acumCost);
						commenthelp+=" | Categoria: "+rs.getString(5)+" Sobrepasado: "+acumCost + " Presupuesto: "+ActualBudget;
						sobrePresupuesto=true;
						
						if(YearBudget<acumCost.longValue())
							sobreanual=true;
						
						if(!sobrePresupuesto && !sobreanual)
							commenthelp="VERDE "+commenthelp;
						if(sobrePresupuesto && !sobreanual)
							commenthelp="AMARILLO "+commenthelp;
						if(sobreanual)
							commenthelp="ROJO "+commenthelp;
						
						if(sobrePresupuesto && !myrole.get_ValueAsBoolean("RequisitionSupervisor") && !myrole.get_ValueAsBoolean("RequisitionAdmin")){
							setProcessed(true);
							set_CustomColumn("Help",commenthelp);
							set_ValueOfColumn("Help",commenthelp);
							return commenthelp;
						}
						else if(sobrePresupuesto && !sobreanual  && myrole.get_ValueAsBoolean("RequisitionAdmin")){
							commenthelp="";
							continue;
						}
						else if (sobreanual && !myrole.get_ValueAsBoolean("RequisitionAdmin")){
							setProcessed(true);
							set_CustomColumn("Help",commenthelp);
							set_ValueOfColumn("Help",commenthelp);
							return commenthelp;
						}
						else if ((sobreanual || sobrePresupuesto) && myrole.get_ValueAsBoolean("RequisitionAdmin") && ((BigDecimal)currentUser.get_Value("AmtApproval")).doubleValue()==0){
							commenthelp="";
							continue;
						}
						else if(sobrePresupuesto && myrole.get_ValueAsBoolean("RequisitionSupervisor")
								&& acumCost.doubleValue()>((BigDecimal)currentUser.get_Value("AmtApproval")).doubleValue())
						{
							setProcessed(true);
							set_CustomColumn("Help",commenthelp);
							set_ValueOfColumn("Help",commenthelp);
							return commenthelp;
						}
						else if ( (sobreanual || sobrePresupuesto) && !myrole.get_ValueAsBoolean("RequisitionAdmin")
								&& acumCost.doubleValue()>((BigDecimal)currentUser.get_Value("AmtApproval")).doubleValue() && ((BigDecimal)currentUser.get_Value("AmtApproval")).doubleValue()!=0){
							setProcessed(true);
							set_CustomColumn("Help",commenthelp);
							set_ValueOfColumn("Help",commenthelp);
							return commenthelp;
						}
					}
				}
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch(Exception e)
			{
				log.severe(e.getMessage());
			}
			 
				
			
		}
		return "";
	}
	
	public String completeBudget(){
		
		boolean useBudget="Y".equals(DB.getSQLValueString(get_TrxName(), "select UseBudget from AD_Client where AD_Client_ID="+getAD_Client_ID()));
		
		if(!useBudget)
			return "";
		
		String sql="select il.m_product_id,il.qtyInternalUse,il.m_attributesetinstance_id,c.m_product_category_id,il.ad_org_id from M_InventoryLine il"
			+ " inner join M_Product p on (il.m_product_id=p.m_product_id)"
			+ " inner join M_Product_Category c on (p.m_product_category_id=c.m_product_category_id)"
			+ " where il.m_inventory_id=? and il.qtyinternaluse!=0";
			
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MPeriod period= MPeriod.get(getCtx(), this.getMovementDate());
//		boolean sobrePresupuesto=false;
		try{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getM_Inventory_ID() );
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				BigDecimal costo = OFBProductCost.getProductCost(rs.getInt(1), rs.getInt(5), rs.getBigDecimal(2), get_TrxName(), getCtx());
				log.config("costo :" + costo);
				if(costo.longValue()==0)
					return MProduct.get(getCtx(), rs.getInt(1)).getName()+ " -Cost 0";

				DB.executeUpdate("Update m_product_category_Budget set " +
						"BudgetUsed=BudgetUsed+"+ costo
						+" where m_product_category_id="+rs.getInt(4)+" And AD_Org_ID="+getAD_Org_ID()+ " and c_period_ID="+period.getC_Period_ID(), get_TrxName());
			}
			
		}
		catch(Exception e)
		{
			log.severe(e.getMessage());
		}
		finally
    	{
    		DB.close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
		
		return "";
	}
	
	
	private BigDecimal getQtyOnHand (int M_AttributeSetInstance_ID, int M_Product_ID, int M_Locator_ID) {
		
		
		log.config("M_Locator_ID: "+ M_Locator_ID + "M_Product_ID: "+M_Product_ID + " M_AttributeSetInstance_ID: "+M_AttributeSetInstance_ID);
		BigDecimal bd = null;
		String sql = "SELECT sum(QtyOnHand) FROM M_Storage "
			+ "WHERE M_Product_ID=?"	//	1
			+ " AND M_Locator_ID=?"		//	2
			+ " AND M_AttributeSetInstance_ID=?";
		if (M_AttributeSetInstance_ID == 0)
			sql = "SELECT SUM(QtyOnHand) FROM M_Storage "
			+ "WHERE M_Product_ID=?"	//	1
			+ " AND M_Locator_ID=?";	//	2
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Product_ID);
			pstmt.setInt(2, M_Locator_ID);
			log.config("sql "+sql);
			if (M_AttributeSetInstance_ID != 0)
				pstmt.setInt(3, M_AttributeSetInstance_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				
				bd = rs.getBigDecimal(1);
				log.config("QtyOnHand: "+ bd);
				if (bd != null)
					return bd;
			} 
			else {
				
				return new BigDecimal(0);
			}
			
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return new BigDecimal(0);
		}
		finally
    	{
    		DB.close(rs, pstmt);
    		rs = null; pstmt = null;
    	}
		
		return new BigDecimal(0);
	}
	
	/**
	 * faaguilar ofb*/
	public long getBudget(int category_ID, Timestamp dateDoc, int Org_ID, int Product_ID){
		
		long actualBudget=0;
		boolean found = false;
		
		if(Product_ID>0)
		{
			String sql="select SUM(b.Budget-b.BudgetUsed) from m_product_category_Budget b"
					+ " inner join C_Period p on (b.C_Period_ID=p.C_Period_ID)"
					+ " where p.periodno between 1 and ?  and p.C_Year_ID=? and b.m_product_id=? and b.ad_org_id=?";
			PreparedStatement pstmt = null;
			MPeriod period= MPeriod.get(getCtx(), dateDoc);
			try
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				pstmt.setInt (1, period.getPeriodNo()  );
				pstmt.setInt (2, period.getC_Year_ID()  );
				pstmt.setInt (3, Product_ID  );
				pstmt.setInt (4, Org_ID  );
				ResultSet rs = pstmt.executeQuery ();
				rs = pstmt.executeQuery ();
				if (rs.next ())
				{
					actualBudget=rs.getLong(1);
					found = true;
				}
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "getBudget", e);
			}
			
		}//*
		
		if(Product_ID==0 || !found){
			String sql="select SUM(b.Budget-b.BudgetUsed) from m_product_category_Budget b"
					+ " inner join C_Period p on (b.C_Period_ID=p.C_Period_ID)"
					+ " where p.periodno between 1 and ?  and p.C_Year_ID=? and b.m_product_category_id=? and b.ad_org_id=?";
			PreparedStatement pstmt = null;
			MPeriod period= MPeriod.get(getCtx(), dateDoc);
			try
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				pstmt.setInt (1, period.getPeriodNo()  );
				pstmt.setInt (2, period.getC_Year_ID()  );
				pstmt.setInt (3, category_ID  );
				pstmt.setInt (4, Org_ID  );
				ResultSet rs = pstmt.executeQuery ();
				rs = pstmt.executeQuery ();
				if (rs.next ())
					actualBudget=rs.getLong(1);
				
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "getBudget", e);
			}
		}//* 
		
		
			return actualBudget;
		}
		
		public long getBudgetYear(int category_ID, int Year_ID, int Org_ID, int Product_ID){
			
			long actualBudget=0;
			boolean found = false;
			
			if(Product_ID>0){
				String sql="select SUM(b.Budget-b.BudgetUsed) from m_product_category_Budget b"
						+ " inner join C_Period p on (b.C_Period_ID=p.C_Period_ID)"
						+ " where p.periodno between 1 and 12  and p.C_Year_ID=? and b.m_product_id=? and b.ad_org_id=?";
				
				PreparedStatement pstmt = null;
				
				try
				{
					pstmt = DB.prepareStatement (sql, get_TrxName());
					pstmt.setInt (1, Year_ID  );
					pstmt.setInt (2, Product_ID  );
					pstmt.setInt (3, Org_ID  );
					ResultSet rs = pstmt.executeQuery ();
					rs = pstmt.executeQuery ();
					if (rs.next ()){
						actualBudget=rs.getLong(1);
						found = true;
					}
					
					rs.close ();
					pstmt.close ();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "getBudget", e);
				}
				
			}//*
			
			if(Product_ID==0 || !found){
				String sql="select SUM(b.Budget-b.BudgetUsed) from m_product_category_Budget b"
						+ " inner join C_Period p on (b.C_Period_ID=p.C_Period_ID)"
						+ " where p.periodno between 1 and 12  and p.C_Year_ID=? and b.m_product_category_id=? and b.ad_org_id=?";
				PreparedStatement pstmt = null;
				
				try
				{
					pstmt = DB.prepareStatement (sql, get_TrxName());
					pstmt.setInt (1, Year_ID  );
					pstmt.setInt (2, category_ID  );
					pstmt.setInt (3, Org_ID  );
					ResultSet rs = pstmt.executeQuery ();
					rs = pstmt.executeQuery ();
					if (rs.next ())
						actualBudget=rs.getLong(1);
					
					rs.close ();
					pstmt.close ();
					pstmt = null;
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "getBudget", e);
				}
			}//*	
				
			return actualBudget;
				
		}
	
	
}	//	MInventory
