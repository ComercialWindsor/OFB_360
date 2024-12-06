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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.BPartnerNoAddressException;
import org.adempiere.exceptions.DBException;
import org.compiere.FA.CreateAssetForecast;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocAction;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.eevolution.model.MPPProductBOM;
import org.eevolution.model.MPPProductBOMLine;
import org.ofb.model.OFBForward;
import org.ofb.process.ExportDTEInvoiceCG;
import org.ofb.process.ExportDTEInvoiceCGBaskakow;
import org.ofb.process.ExportDTEInvoiceCGGemV2;
import org.ofb.process.ExportDTEInvoiceCGProvectis;
import org.ofb.process.ExportDTEInvoiceFOL;
import org.ofb.process.ExportDTEInvoiceFOLCGGem;
import org.ofb.process.ExportDTEInvoiceFOLNGem;
import org.ofb.process.ExportDTEInvoiceFOLPA;
import org.ofb.process.ExportDTEInvoiceFOLQDC;
import org.ofb.process.ExportDTEInvoiceITSA;
import org.ofb.process.ExportDTEInvoiceNBaskakow;
import org.ofb.process.ExportDTEInvoiceNGem;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 *	Invoice Model.
 * 	Please do not set DocStatus and C_DocType_ID directly.
 * 	They are set in the process() method.
 * 	Use DocAction and C_DocTypeTarget_ID instead.
 *
 *  @author Jorg Janke
 *  @version $Id: MInvoice.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 *  @author victor.perez@e-evolution.com, e-Evolution http://www.e-evolution.com
 *  		@see http://sourceforge.net/tracker/?func=detail&atid=879335&aid=1948157&group_id=176962
 * 			<li> FR [ 2520591 ] Support multiples calendar for Org
 *			@see http://sourceforge.net/tracker2/?func=detail&atid=879335&aid=2520591&group_id=176962
 *  Modifications: Added RMA functionality (Ashley Ramdass)
 */
public class MInvoice extends X_C_Invoice implements DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 816227083897031327L;

	/**
	 * 	Get Payments Of BPartner
	 *	@param ctx context
	 *	@param C_BPartner_ID id
	 *	@param trxName transaction
	 *	@return array
	 */
	public static MInvoice[] getOfBPartner (Properties ctx, int C_BPartner_ID, String trxName)
	{
		List<MInvoice> list = new Query(ctx, Table_Name, COLUMNNAME_C_BPartner_ID+"=?", trxName)
									.setParameters(C_BPartner_ID)
									.list();
		return list.toArray(new MInvoice[list.size()]);
	}	//	getOfBPartner

	/**
	 * 	Create new Invoice by copying
	 * 	@param from invoice
	 * 	@param dateDoc date of the document date
	 *  @param acctDate original account date 
	 * 	@param C_DocTypeTarget_ID target doc type
	 * 	@param isSOTrx sales order
	 * 	@param counter create counter links
	 * 	@param trxName trx
	 * 	@param setOrder set Order links
	 *	@return Invoice
	 */
	public static MInvoice copyFrom (MInvoice from, Timestamp dateDoc, Timestamp dateAcct,
		int C_DocTypeTarget_ID, boolean isSOTrx, boolean counter,
		String trxName, boolean setOrder)
	{
		MInvoice to = new MInvoice (from.getCtx(), 0, trxName);
		PO.copyValues (from, to, from.getAD_Client_ID(), from.getAD_Org_ID());
		to.set_ValueNoCheck ("C_Invoice_ID", I_ZERO);
		to.set_ValueNoCheck ("DocumentNo", null);
		//
		to.setDocStatus (DOCSTATUS_Drafted);		//	Draft
		to.setDocAction(DOCACTION_Complete);
		//
		to.setC_DocType_ID(0);
		to.setC_DocTypeTarget_ID (C_DocTypeTarget_ID);
		to.setIsSOTrx(isSOTrx);
		//
		to.setDateInvoiced (dateDoc);
		to.setDateAcct (dateAcct);
		to.setDatePrinted(null);
		to.setIsPrinted (false);
		//
		to.setIsApproved (false);
		to.setC_Payment_ID(0);
		to.setC_CashLine_ID(0);
		to.setIsPaid (false);
		to.setIsInDispute(false);
		//
		//	Amounts are updated by trigger when adding lines
		to.setGrandTotal(Env.ZERO);
		to.setTotalLines(Env.ZERO);
		//
		to.setIsTransferred (false);
		to.setPosted (false);
		to.setProcessed (false);
		//[ 1633721 ] Reverse Documents- Processing=Y
		to.setProcessing(false);
		//	delete references
		to.setIsSelfService(false);
		if (!setOrder)
			to.setC_Order_ID(0);
		if (counter)
		{
			to.setRef_Invoice_ID(from.getC_Invoice_ID());
			//	Try to find Order link
			if (from.getC_Order_ID() != 0)
			{
				MOrder peer = new MOrder (from.getCtx(), from.getC_Order_ID(), from.get_TrxName());
				if (peer.getRef_Order_ID() != 0)
					to.setC_Order_ID(peer.getRef_Order_ID());
			}
			// Try to find RMA link
			if (from.getM_RMA_ID() != 0)
			{
				MRMA peer = new MRMA (from.getCtx(), from.getM_RMA_ID(), from.get_TrxName());
				if (peer.getRef_RMA_ID() > 0)
					to.setM_RMA_ID(peer.getRef_RMA_ID());
			}
			//
		}
		else
			to.setRef_Invoice_ID(0);

		to.saveEx(trxName);
		if (counter)
			from.setRef_Invoice_ID(to.getC_Invoice_ID());

		//	Lines
		if (to.copyLinesFrom(from, counter, setOrder) == 0)
			throw new IllegalStateException("Could not create Invoice Lines");

		return to;
	}
	
	/** 
	 *  @deprecated
	 * 	Create new Invoice by copying
	 * 	@param from invoice
	 * 	@param dateDoc date of the document date
	 * 	@param C_DocTypeTarget_ID target doc type
	 * 	@param isSOTrx sales order
	 * 	@param counter create counter links
	 * 	@param trxName trx
	 * 	@param setOrder set Order links
	 *	@return Invoice
	 */
	public static MInvoice copyFrom (MInvoice from, Timestamp dateDoc,
		int C_DocTypeTarget_ID, boolean isSOTrx, boolean counter,
		String trxName, boolean setOrder)
	{
		MInvoice to = copyFrom ( from, dateDoc, dateDoc,
				 C_DocTypeTarget_ID, isSOTrx, counter,
				trxName, setOrder);
		return to;
	}	//	copyFrom

	/**
	 * 	Get PDF File Name
	 *	@param documentDir directory
	 * 	@param C_Invoice_ID invoice
	 *	@return file name
	 */
	public static String getPDFFileName (String documentDir, int C_Invoice_ID)
	{
		StringBuffer sb = new StringBuffer (documentDir);
		if (sb.length() == 0)
			sb.append(".");
		if (!sb.toString().endsWith(File.separator))
			sb.append(File.separator);
		sb.append("C_Invoice_ID_")
			.append(C_Invoice_ID)
			.append(".pdf");
		return sb.toString();
	}	//	getPDFFileName


	/**
	 * 	Get MInvoice from Cache
	 *	@param ctx context
	 *	@param C_Invoice_ID id
	 *	@return MInvoice
	 */
	public static MInvoice get (Properties ctx, int C_Invoice_ID)
	{
		Integer key = new Integer (C_Invoice_ID);
		MInvoice retValue = (MInvoice) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MInvoice (ctx, C_Invoice_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MInvoice>	s_cache	= new CCache<Integer,MInvoice>("C_Invoice", 20, 2);	//	2 minutes


	/**************************************************************************
	 * 	Invoice Constructor
	 * 	@param ctx context
	 * 	@param C_Invoice_ID invoice or 0 for new
	 * 	@param trxName trx name
	 */
	public MInvoice (Properties ctx, int C_Invoice_ID, String trxName)
	{
		super (ctx, C_Invoice_ID, trxName);
		if (C_Invoice_ID == 0)
		{
			setDocStatus (DOCSTATUS_Drafted);		//	Draft
			setDocAction (DOCACTION_Complete);
			//
			setPaymentRule(PAYMENTRULE_OnCredit);	//	Payment Terms

			setDateInvoiced (new Timestamp (System.currentTimeMillis ()));
			setDateAcct (new Timestamp (System.currentTimeMillis ()));
			//
			setChargeAmt (Env.ZERO);
			setTotalLines (Env.ZERO);
			setGrandTotal (Env.ZERO);
			//
			setIsSOTrx (true);
			setIsTaxIncluded (false);
			setIsApproved (false);
			setIsDiscountPrinted (false);
			setIsPaid (false);
			setSendEMail (false);
			setIsPrinted (false);
			setIsTransferred (false);
			setIsSelfService(false);
			setIsPayScheduleValid(false);
			setIsInDispute(false);
			setPosted(false);
			super.setProcessed (false);
			setProcessing(false);
		}
	}	//	MInvoice

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *	@param trxName transaction
	 */
	public MInvoice (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInvoice

	/**
	 * 	Create Invoice from Order
	 *	@param order order
	 *	@param C_DocTypeTarget_ID target document type
	 *	@param invoiceDate date or null
	 */
	public MInvoice (MOrder order, int C_DocTypeTarget_ID, Timestamp invoiceDate)
	{
		this (order.getCtx(), 0, order.get_TrxName());
		setClientOrg(order);
		setOrder(order);	//	set base settings
		//
		if (C_DocTypeTarget_ID <= 0)
		{
			MDocType odt = MDocType.get(order.getCtx(), order.getC_DocType_ID());
			if (odt != null)
			{
				C_DocTypeTarget_ID = odt.getC_DocTypeInvoice_ID();
				if (C_DocTypeTarget_ID <= 0)
					throw new AdempiereException("@NotFound@ @C_DocTypeInvoice_ID@ - @C_DocType_ID@:"+odt.get_Translation(MDocType.COLUMNNAME_Name));
			}
		}
		setC_DocTypeTarget_ID(C_DocTypeTarget_ID);
		if (invoiceDate != null)
			setDateInvoiced(invoiceDate);
		setDateAcct(getDateInvoiced());
		//
		setSalesRep_ID(order.getSalesRep_ID());
		//
		setC_BPartner_ID(order.getBill_BPartner_ID());
		setC_BPartner_Location_ID(order.getBill_Location_ID());
		setAD_User_ID(order.getBill_User_ID());
	}	//	MInvoice

	/**
	 * 	Create Invoice from Shipment
	 *	@param ship shipment
	 *	@param invoiceDate date or null
	 */
	public MInvoice (MInOut ship, Timestamp invoiceDate)
	{
		this (ship.getCtx(), 0, ship.get_TrxName());
		setClientOrg(ship);
		setShipment(ship);	//	set base settings
		//
		setC_DocTypeTarget_ID();
		if (invoiceDate != null)
			setDateInvoiced(invoiceDate);
		setDateAcct(getDateInvoiced());
		//
		setSalesRep_ID(ship.getSalesRep_ID());
	}	//	MInvoice

	/**
	 * 	Create Invoice from Batch Line
	 *	@param batch batch
	 *	@param line batch line
	 */
	public MInvoice (MInvoiceBatch batch, MInvoiceBatchLine line)
	{
		this (line.getCtx(), 0, line.get_TrxName());
		setClientOrg(line);
		setDocumentNo(line.getDocumentNo());
		//
		setIsSOTrx(batch.isSOTrx());
		MBPartner bp = new MBPartner (line.getCtx(), line.getC_BPartner_ID(), line.get_TrxName());
		setBPartner(bp);	//	defaults
		//
		setIsTaxIncluded(line.isTaxIncluded());
		//	May conflict with default price list
		setC_Currency_ID(batch.getC_Currency_ID());
		setC_ConversionType_ID(batch.getC_ConversionType_ID());
		//
	//	setPaymentRule(order.getPaymentRule());
	//	setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
	//	setPOReference("");
		setDescription(batch.getDescription());
	//	setDateOrdered(order.getDateOrdered());
		//
		setAD_OrgTrx_ID(line.getAD_OrgTrx_ID());
		setC_Project_ID(line.getC_Project_ID());
	//	setC_Campaign_ID(line.getC_Campaign_ID());
		setC_Activity_ID(line.getC_Activity_ID());
		setUser1_ID(line.getUser1_ID());
		setUser2_ID(line.getUser2_ID());
		//
		setC_DocTypeTarget_ID(line.getC_DocType_ID());
		setDateInvoiced(line.getDateInvoiced());
		setDateAcct(line.getDateAcct());
		//
		setSalesRep_ID(batch.getSalesRep_ID());
		//
		setC_BPartner_ID(line.getC_BPartner_ID());
		setC_BPartner_Location_ID(line.getC_BPartner_Location_ID());
		setAD_User_ID(line.getAD_User_ID());
	}	//	MInvoice

	/**	Open Amount				*/
	private BigDecimal 		m_openAmt = null;

	/**	Invoice Lines			*/
	private MInvoiceLine[]	m_lines;
	/**	Invoice Taxes			*/
	private MInvoiceTax[]	m_taxes;
	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MInvoice.class);

	/**
	 * 	Overwrite Client/Org if required
	 * 	@param AD_Client_ID client
	 * 	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	Set Business Partner Defaults & Details
	 * 	@param bp business partner
	 */
	public void setBPartner (MBPartner bp)
	{
		if (bp == null)
			return;

		setC_BPartner_ID(bp.getC_BPartner_ID());
		//	Set Defaults
		int ii = 0;
		if (isSOTrx())
			ii = bp.getC_PaymentTerm_ID();
		else
			ii = bp.getPO_PaymentTerm_ID();
		if (ii != 0)
			setC_PaymentTerm_ID(ii);
		//
		if (isSOTrx())
			ii = bp.getM_PriceList_ID();
		else
			ii = bp.getPO_PriceList_ID();
		if (ii != 0)
			setM_PriceList_ID(ii);
		//
		String ss = bp.getPaymentRule();
		if (ss != null)
			setPaymentRule(ss);


		//	Set Locations
		MBPartnerLocation[] locs = bp.getLocations(false);
		if (locs != null)
		{
			for (int i = 0; i < locs.length; i++)
			{
				if ((locs[i].isBillTo() && isSOTrx())
				|| (locs[i].isPayFrom() && !isSOTrx()))
					setC_BPartner_Location_ID(locs[i].getC_BPartner_Location_ID());
			}
			//	set to first
			if (getC_BPartner_Location_ID() == 0 && locs.length > 0)
				setC_BPartner_Location_ID(locs[0].getC_BPartner_Location_ID());
		}
		if (getC_BPartner_Location_ID() == 0)
			log.log(Level.SEVERE, new BPartnerNoAddressException(bp).getLocalizedMessage()); //TODO: throw exception?

		//	Set Contact
		MUser[] contacts = bp.getContacts(false);
		if (contacts != null && contacts.length > 0)	//	get first User
			setAD_User_ID(contacts[0].getAD_User_ID());
	}	//	setBPartner

	/**
	 * 	Set Order References
	 * 	@param order order
	 */
	public void setOrder (MOrder order)
	{
		if (order == null)
			return;

		setC_Order_ID(order.getC_Order_ID());
		setIsSOTrx(order.isSOTrx());
		setIsDiscountPrinted(order.isDiscountPrinted());
		setIsSelfService(order.isSelfService());
		setSendEMail(order.isSendEMail());
		//
		setM_PriceList_ID(order.getM_PriceList_ID());
		setIsTaxIncluded(order.isTaxIncluded());
		setC_Currency_ID(order.getC_Currency_ID());
		setC_ConversionType_ID(order.getC_ConversionType_ID());
		//
		setPaymentRule(order.getPaymentRule());
		setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
		setPOReference(order.getPOReference());
		setDescription(order.getDescription());
		setDateOrdered(order.getDateOrdered());
		//
		setAD_OrgTrx_ID(order.getAD_OrgTrx_ID());
		setC_Project_ID(order.getC_Project_ID());
		setC_Campaign_ID(order.getC_Campaign_ID());
		setC_Activity_ID(order.getC_Activity_ID());
		setUser1_ID(order.getUser1_ID());
		setUser2_ID(order.getUser2_ID());
	}	//	setOrder

	/**
	 * 	Set Shipment References
	 * 	@param ship shipment
	 */
	public void setShipment (MInOut ship)
	{
		if (ship == null)
			return;

		setIsSOTrx(ship.isSOTrx());
		//
		MBPartner bp = new MBPartner (getCtx(), ship.getC_BPartner_ID(), null);
		setBPartner (bp);
		//
		setAD_User_ID(ship.getAD_User_ID());
		//
		setSendEMail(ship.isSendEMail());
		//
		setPOReference(ship.getPOReference());
		setDescription(ship.getDescription());
		setDateOrdered(ship.getDateOrdered());
		//
		setAD_OrgTrx_ID(ship.getAD_OrgTrx_ID());
		setC_Project_ID(ship.getC_Project_ID());
		setC_Campaign_ID(ship.getC_Campaign_ID());
		setC_Activity_ID(ship.getC_Activity_ID());
		setUser1_ID(ship.getUser1_ID());
		setUser2_ID(ship.getUser2_ID());
		//
		if (ship.getC_Order_ID() != 0)
		{
			setC_Order_ID(ship.getC_Order_ID());
			MOrder order = new MOrder (getCtx(), ship.getC_Order_ID(), get_TrxName());
			setIsDiscountPrinted(order.isDiscountPrinted());
			setM_PriceList_ID(order.getM_PriceList_ID());
			setIsTaxIncluded(order.isTaxIncluded());
			setC_Currency_ID(order.getC_Currency_ID());
			setC_ConversionType_ID(order.getC_ConversionType_ID());
			setPaymentRule(order.getPaymentRule());
			setC_PaymentTerm_ID(order.getC_PaymentTerm_ID());
			//
			MDocType dt = MDocType.get(getCtx(), order.getC_DocType_ID());
			if (dt.getC_DocTypeInvoice_ID() != 0)
				setC_DocTypeTarget_ID(dt.getC_DocTypeInvoice_ID());
			// Overwrite Invoice BPartner
			setC_BPartner_ID(order.getBill_BPartner_ID());
			// Overwrite Invoice Address
			setC_BPartner_Location_ID(order.getBill_Location_ID());
			// Overwrite Contact
			setAD_User_ID(order.getBill_User_ID());
			//
		}
        // Check if Shipment/Receipt is based on RMA
        if (ship.getM_RMA_ID() != 0)
        {
            setM_RMA_ID(ship.getM_RMA_ID());

            MRMA rma = new MRMA(getCtx(), ship.getM_RMA_ID(), get_TrxName());
            // Retrieves the invoice DocType
            MDocType dt = MDocType.get(getCtx(), rma.getC_DocType_ID());
            if (dt.getC_DocTypeInvoice_ID() != 0)
            {
                setC_DocTypeTarget_ID(dt.getC_DocTypeInvoice_ID());
            }
            setIsSOTrx(rma.isSOTrx());

            MOrder rmaOrder = rma.getOriginalOrder();
            if (rmaOrder != null) {
                setM_PriceList_ID(rmaOrder.getM_PriceList_ID());
                setIsTaxIncluded(rmaOrder.isTaxIncluded());
                setC_Currency_ID(rmaOrder.getC_Currency_ID());
                setC_ConversionType_ID(rmaOrder.getC_ConversionType_ID());
                setPaymentRule(rmaOrder.getPaymentRule());
                setC_PaymentTerm_ID(rmaOrder.getC_PaymentTerm_ID());
                setC_BPartner_Location_ID(rmaOrder.getBill_Location_ID());
            }
        }

	}	//	setShipment

	/**
	 * 	Set Target Document Type
	 * 	@param DocBaseType doc type MDocType.DOCBASETYPE_
	 */
	public void setC_DocTypeTarget_ID (String DocBaseType)
	{
		String sql = "SELECT C_DocType_ID FROM C_DocType "
			+ "WHERE AD_Client_ID=? AND AD_Org_ID in (0,?) AND DocBaseType=?"
			+ " AND IsActive='Y' "
			+ "ORDER BY IsDefault DESC, AD_Org_ID DESC";
		int C_DocType_ID = DB.getSQLValueEx(null, sql, getAD_Client_ID(), getAD_Org_ID(), DocBaseType);
		if (C_DocType_ID <= 0)
			log.log(Level.SEVERE, "Not found for AD_Client_ID="
				+ getAD_Client_ID() + " - " + DocBaseType);
		else
		{
			log.fine(DocBaseType);
			setC_DocTypeTarget_ID (C_DocType_ID);
			boolean isSOTrx = MDocType.DOCBASETYPE_ARInvoice.equals(DocBaseType)
				|| MDocType.DOCBASETYPE_ARCreditMemo.equals(DocBaseType);
			setIsSOTrx (isSOTrx);
		}
	}	//	setC_DocTypeTarget_ID

	/**
	 * 	Set Target Document Type.
	 * 	Based on SO flag AP/AP Invoice
	 */
	public void setC_DocTypeTarget_ID ()
	{
		if (getC_DocTypeTarget_ID() > 0)
			return;
		if (isSOTrx())
			setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_ARInvoice);
		else
			setC_DocTypeTarget_ID(MDocType.DOCBASETYPE_APInvoice);
	}	//	setC_DocTypeTarget_ID


	/**
	 * 	Get Grand Total
	 * 	@param creditMemoAdjusted adjusted for CM (negative)
	 *	@return grand total
	 */
	public BigDecimal getGrandTotal (boolean creditMemoAdjusted)
	{
		if (!creditMemoAdjusted)
			return super.getGrandTotal();
		//
		BigDecimal amt = getGrandTotal();
		if (isCreditMemo())
			return amt.negate();
		return amt;
	}	//	getGrandTotal


	/**
	 * 	Get Invoice Lines of Invoice
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MInvoiceLine[] getLines (String whereClause)
	{
		String whereClauseFinal = "C_Invoice_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MInvoiceLine> list = new Query(getCtx(), I_C_InvoiceLine.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(getC_Invoice_ID())
										.setOrderBy(I_C_InvoiceLine.COLUMNNAME_Line)
										.list();
		return list.toArray(new MInvoiceLine[list.size()]);
	}	//	getLines

	/**
	 * 	Get Invoice Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MInvoiceLine[] getLines (boolean requery)
	{
		if (m_lines == null || m_lines.length == 0 || requery)
			m_lines = getLines(null);
		set_TrxName(m_lines, get_TrxName());
		return m_lines;
	}	//	getLines

	/**
	 * 	Get Lines of Invoice
	 * 	@return lines
	 */
	public MInvoiceLine[] getLines()
	{
		return getLines(false);
	}	//	getLines


	/**
	 * 	Renumber Lines
	 *	@param step start and step
	 */
	public void renumberLines (int step)
	{
		int number = step;
		MInvoiceLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			line.setLine(number);
			line.saveEx();
			number += step;
		}
		m_lines = null;
	}	//	renumberLines

	/**
	 * 	Copy Lines From other Invoice.
	 *	@param otherInvoice invoice
	 * 	@param counter create counter links
	 * 	@param setOrder set order links
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MInvoice otherInvoice, boolean counter, boolean setOrder)
	{
		if (isProcessed() || isPosted() || otherInvoice == null)
			return 0;
		MInvoiceLine[] fromLines = otherInvoice.getLines(false);
		int count = 0;
		for (int i = 0; i < fromLines.length; i++)
		{
			MInvoiceLine line = new MInvoiceLine (getCtx(), 0, get_TrxName());
			MInvoiceLine fromLine = fromLines[i];
			if (counter)	//	header
				PO.copyValues (fromLine, line, getAD_Client_ID(), getAD_Org_ID());
			else
				PO.copyValues (fromLine, line, fromLine.getAD_Client_ID(), fromLine.getAD_Org_ID());
			line.setC_Invoice_ID(getC_Invoice_ID());
			line.setInvoice(this);
			line.set_ValueNoCheck ("C_InvoiceLine_ID", I_ZERO);	// new
			//	Reset
			if (!setOrder)
				line.setC_OrderLine_ID(0);
			line.setRef_InvoiceLine_ID(0);
			line.setM_InOutLine_ID(0);
			line.setA_Asset_ID(0);
			line.setM_AttributeSetInstance_ID(0);
			line.setS_ResourceAssignment_ID(0);
			//	New Tax
			if (getC_BPartner_ID() != otherInvoice.getC_BPartner_ID())
				line.setTax();	//	recalculate
			//
			if (counter)
			{
				line.setRef_InvoiceLine_ID(fromLine.getC_InvoiceLine_ID());
				if (fromLine.getC_OrderLine_ID() != 0)
				{
					MOrderLine peer = new MOrderLine (getCtx(), fromLine.getC_OrderLine_ID(), get_TrxName());
					if (peer.getRef_OrderLine_ID() != 0)
						line.setC_OrderLine_ID(peer.getRef_OrderLine_ID());
				}
				line.setM_InOutLine_ID(0);
				if (fromLine.getM_InOutLine_ID() != 0)
				{
					MInOutLine peer = new MInOutLine (getCtx(), fromLine.getM_InOutLine_ID(), get_TrxName());
					if (peer.getRef_InOutLine_ID() != 0)
						line.setM_InOutLine_ID(peer.getRef_InOutLine_ID());
				}
			}
			//
			line.setProcessed(false);
			if (line.save(get_TrxName()))
				count++;
			//	Cross Link
			if (counter)
			{
				fromLine.setRef_InvoiceLine_ID(line.getC_InvoiceLine_ID());
				fromLine.save(get_TrxName());
			}

			// MZ Goodwill
			// copy the landed cost
			line.copyLandedCostFrom(fromLine);
			line.allocateLandedCosts();
			// end MZ
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom

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
	public boolean isReversal()
	{
		return m_reversal;
	}	//	isReversal

	/**
	 * 	Get Taxes
	 *	@param requery requery
	 *	@return array of taxes
	 */
	public MInvoiceTax[] getTaxes (boolean requery)
	{
		if (m_taxes != null && !requery)
			return m_taxes;

		final String whereClause = MInvoiceTax.COLUMNNAME_C_Invoice_ID+"=?";
		List<MInvoiceTax> list = new Query(getCtx(), I_C_InvoiceTax.Table_Name, whereClause, get_TrxName())
										.setParameters(get_ID())
										.list();
		m_taxes = list.toArray(new MInvoiceTax[list.size()]);
		return m_taxes;
	}	//	getTaxes

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
	 * 	Is it a Credit Memo?
	 *	@return true if CM
	 */
	public boolean isCreditMemo()
	{
		MDocType dt = MDocType.get(getCtx(),
			getC_DocType_ID()==0 ? getC_DocTypeTarget_ID() : getC_DocType_ID());
		return MDocType.DOCBASETYPE_APCreditMemo.equals(dt.getDocBaseType())
			|| MDocType.DOCBASETYPE_ARCreditMemo.equals(dt.getDocBaseType());
	}	//	isCreditMemo

	/**
	 * 	Set Processed.
	 * 	Propergate to Lines/Taxes
	 *	@param processed processed
	 */
	public void setProcessed (boolean processed)
	{
		super.setProcessed (processed);
		if (get_ID() == 0)
			return;
		String set = "SET Processed='"
			+ (processed ? "Y" : "N")
			+ "' WHERE C_Invoice_ID=" + getC_Invoice_ID();
		int noLine = DB.executeUpdate("UPDATE C_InvoiceLine " + set, get_TrxName());
		int noTax = DB.executeUpdate("UPDATE C_InvoiceTax " + set, get_TrxName());
		m_lines = null;
		m_taxes = null;
		log.fine(processed + " - Lines=" + noLine + ", Tax=" + noTax);
	}	//	setProcessed

	/**
	 * 	Validate Invoice Pay Schedule
	 *	@return pay schedule is valid
	 */
	public boolean validatePaySchedule()
	{
		MInvoicePaySchedule[] schedule = MInvoicePaySchedule.getInvoicePaySchedule
			(getCtx(), getC_Invoice_ID(), 0, get_TrxName());
		log.fine("#" + schedule.length);
		if (schedule.length == 0)
		{
			setIsPayScheduleValid(false);
			return false;
		}
		//	Add up due amounts
		BigDecimal total = Env.ZERO;
		for (int i = 0; i < schedule.length; i++)
		{
			schedule[i].setParent(this);
			BigDecimal due = schedule[i].getDueAmt();
			if (due != null)
				total = total.add(due);
		}
		boolean valid = getGrandTotal().compareTo(total) == 0;
		setIsPayScheduleValid(valid);

		//	Update Schedule Lines
		for (int i = 0; i < schedule.length; i++)
		{
			if (schedule[i].isValid() != valid)
			{
				schedule[i].setIsValid(valid);
				schedule[i].saveEx(get_TrxName());
			}
		}
		return valid;
	}	//	validatePaySchedule


	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		log.fine("");
		//	No Partner Info - set Template
		if (getC_BPartner_ID() == 0)
			setBPartner(MBPartner.getTemplate(getCtx(), getAD_Client_ID()));
		if (getC_BPartner_Location_ID() == 0)
			setBPartner(new MBPartner(getCtx(), getC_BPartner_ID(), null));

		//	Price List
		if (getM_PriceList_ID() == 0)
		{
			int ii = Env.getContextAsInt(getCtx(), "#M_PriceList_ID");
			if (ii != 0)
				setM_PriceList_ID(ii);
			else
			{
				String sql = "SELECT M_PriceList_ID FROM M_PriceList WHERE AD_Client_ID=? AND IsDefault='Y'";
				ii = DB.getSQLValue (null, sql, getAD_Client_ID());
				if (ii != 0)
					setM_PriceList_ID (ii);
			}
		}

		//	Currency
		if (getC_Currency_ID() == 0)
		{
			String sql = "SELECT C_Currency_ID FROM M_PriceList WHERE M_PriceList_ID=?";
			int ii = DB.getSQLValue (null, sql, getM_PriceList_ID());
			if (ii != 0)
				setC_Currency_ID (ii);
			else
				setC_Currency_ID(Env.getContextAsInt(getCtx(), "#C_Currency_ID"));
		}

		//	Sales Rep
		if (getSalesRep_ID() == 0)
		{
			int ii = Env.getContextAsInt(getCtx(), "#SalesRep_ID");
			if (ii != 0)
				setSalesRep_ID (ii);
		}

		//	Document Type
		if (getC_DocType_ID() == 0)
			setC_DocType_ID (0);	//	make sure it's set to 0
		if (getC_DocTypeTarget_ID() == 0)
			setC_DocTypeTarget_ID(isSOTrx() ? MDocType.DOCBASETYPE_ARInvoice : MDocType.DOCBASETYPE_APInvoice);

		//	Payment Term
		if (getC_PaymentTerm_ID() == 0)
		{
			int ii = Env.getContextAsInt(getCtx(), "#C_PaymentTerm_ID");
			if (ii != 0)
				setC_PaymentTerm_ID (ii);
			else
			{
				String sql = "SELECT C_PaymentTerm_ID FROM C_PaymentTerm WHERE AD_Client_ID=? AND IsDefault='Y'";
				ii = DB.getSQLValue(null, sql, getAD_Client_ID());
				if (ii != 0)
					setC_PaymentTerm_ID (ii);
			}
		}
		
		//faaguilar OFB redondeo begin
		if(this.getC_Currency_ID()==228 )
			if(this.getTotalLines().signum()!=0)
			{
				int decimals=this.getC_Currency().getStdPrecision();
				 setTotalLines(getTotalLines().setScale(decimals, BigDecimal.ROUND_HALF_EVEN));
				 setGrandTotal(getGrandTotal().setScale(decimals, BigDecimal.ROUND_HALF_EVEN));
			}
		//faaguilar OFB redondeo end
		
		
		return true;
	}	//	beforeSave

	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	protected boolean beforeDelete ()
	{
		if (getC_Order_ID() != 0)
		{
			log.saveError("Error", Msg.getMsg(getCtx(), "CannotDelete"));
			return false;
		}
		return true;
	}	//	beforeDelete

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInvoice[")
			.append(get_ID()).append("-").append(getDocumentNo())
			.append(",GrandTotal=").append(getGrandTotal());
		if (m_lines != null)
			sb.append(" (#").append(m_lines.length).append(")");
		sb.append ("]");
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
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success || newRecord)
			return success;

		if (is_ValueChanged("AD_Org_ID"))
		{
			String sql = "UPDATE C_InvoiceLine ol"
				+ " SET AD_Org_ID ="
					+ "(SELECT AD_Org_ID"
					+ " FROM C_Invoice o WHERE ol.C_Invoice_ID=o.C_Invoice_ID) "
				+ "WHERE C_Invoice_ID=" + getC_Invoice_ID();
			int no = DB.executeUpdate(sql, get_TrxName());
			log.fine("Lines -> #" + no);
		}
		return true;
	}	//	afterSave


	/**
	 * 	Set Price List (and Currency) when valid
	 * 	@param M_PriceList_ID price list
	 */
	@Override
	public void setM_PriceList_ID (int M_PriceList_ID)
	{
		MPriceList pl = MPriceList.get(getCtx(), M_PriceList_ID, null);
		if (pl != null) {
			setC_Currency_ID(pl.getC_Currency_ID());
			super.setM_PriceList_ID(M_PriceList_ID);
		}
	}	//	setM_PriceList_ID


	/**
	 * 	Get Allocated Amt in Invoice Currency
	 *	@return pos/neg amount or null
	 */
	public BigDecimal getAllocatedAmt ()
	{
		BigDecimal retValue = null;
		String sql = "SELECT SUM(currencyConvert(al.Amount+al.DiscountAmt+al.WriteOffAmt,"
				+ "ah.C_Currency_ID, i.C_Currency_ID,ah.DateTrx,COALESCE(i.C_ConversionType_ID,0), al.AD_Client_ID,al.AD_Org_ID)) "
			+ "FROM C_AllocationLine al"
			+ " INNER JOIN C_AllocationHdr ah ON (al.C_AllocationHdr_ID=ah.C_AllocationHdr_ID)"
			+ " INNER JOIN C_Invoice i ON (al.C_Invoice_ID=i.C_Invoice_ID) "
			+ "WHERE al.C_Invoice_ID=?"
			+ " AND ah.IsActive='Y' AND al.IsActive='Y'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_Invoice_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = rs.getBigDecimal(1);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
	//	log.fine("getAllocatedAmt - " + retValue);
		//	? ROUND(NVL(v_AllocatedAmt,0), 2);
		return retValue;
	}	//	getAllocatedAmt

	/**
	 * 	Test Allocation (and set paid flag)
	 *	@return true if updated
	 */
	public boolean testAllocation()
	{
		boolean change = false;

		if ( isProcessed() ) {
			BigDecimal alloc = getAllocatedAmt();	//	absolute
			if (alloc == null)
				alloc = Env.ZERO;
			BigDecimal total = getGrandTotal();
			if (!isSOTrx())
				total = total.negate();
			if (isCreditMemo())
				total = total.negate();
			boolean test = total.compareTo(alloc) == 0;
			change = test != isPaid();
			if (change)
				setIsPaid(test);
			log.fine("Paid=" + test
					+ " (" + alloc + "=" + total + ")");
		}

		return change;
	}	//	testAllocation

	/**
	 * 	Set Paid Flag for invoices
	 * 	@param ctx context
	 *	@param C_BPartner_ID if 0 all
	 *	@param trxName transaction
	 */
	public static void setIsPaid (Properties ctx, int C_BPartner_ID, String trxName)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer("IsPaid='N' AND DocStatus IN ('CO','CL')");
		if (C_BPartner_ID > 1)
		{
			whereClause.append(" AND C_BPartner_ID=?");
			params.add(C_BPartner_ID);
		}
		else
		{
			whereClause.append(" AND AD_Client_ID=?");
			params.add(Env.getAD_Client_ID(ctx));
		}

		POResultSet<MInvoice> rs = new Query(ctx, MInvoice.Table_Name, whereClause.toString(), trxName)
										.setParameters(params)
										.scroll();
		int counter = 0;
		try {
			while(rs.hasNext()) {
				MInvoice invoice = rs.next();
				if (invoice.testAllocation())
					if (invoice.save())
						counter++;
			}
		}
		finally {
			DB.close(rs);
		}
		s_log.config("#" + counter);
		/**/
	}	//	setIsPaid

	/**
	 * 	Get Open Amount.
	 * 	Used by web interface
	 * 	@return Open Amt
	 */
	public BigDecimal getOpenAmt ()
	{
		return getOpenAmt (true, null);
	}	//	getOpenAmt

	/**
	 * 	Get Open Amount
	 * 	@param creditMemoAdjusted adjusted for CM (negative)
	 * 	@param paymentDate ignored Payment Date
	 * 	@return Open Amt
	 */
	public BigDecimal getOpenAmt (boolean creditMemoAdjusted, Timestamp paymentDate)
	{
		if (isPaid())
			return Env.ZERO;
		//
		if (m_openAmt == null)
		{
			m_openAmt = getGrandTotal();
			if (paymentDate != null)
			{
				//	Payment Discount
				//	Payment Schedule
			}
			BigDecimal allocated = getAllocatedAmt();
			if (allocated != null)
			{
				allocated = allocated.abs();	//	is absolute
				m_openAmt = m_openAmt.subtract(allocated);
			}
		}
		//
		if (!creditMemoAdjusted)
			return m_openAmt;
		if (isCreditMemo())
			return m_openAmt.negate();
		return m_openAmt;
	}	//	getOpenAmt


	/**
	 * 	Get Document Status
	 *	@return Document Status Clear Text
	 */
	public String getDocStatusName()
	{
		return MRefList.getListName(getCtx(), 131, getDocStatus());
	}	//	getDocStatusName


	/**************************************************************************
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
		ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID(), get_TrxName());
		if (re == null)
			return null;
		return re.getPDF(file);
	}	//	createPDF

	/**
	 * 	Get PDF File Name
	 *	@param documentDir directory
	 *	@return file name
	 */
	public String getPDFFileName (String documentDir)
	{
		return getPDFFileName (documentDir, getC_Invoice_ID());
	}	//	getPDFFileName

	/**
	 *	Get ISO Code of Currency
	 *	@return Currency ISO
	 */
	public String getCurrencyISO()
	{
		return MCurrency.getISO_Code (getCtx(), getC_Currency_ID());
	}	//	getCurrencyISO

	/**
	 * 	Get Currency Precision
	 *	@return precision
	 */
	public int getPrecision()
	{
		return MCurrency.getStdPrecision(getCtx(), getC_Currency_ID());
	}	//	getPrecision


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
	}	//	process

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
		log.info("unlockIt - " + toString());
		setProcessing(false);
		return true;
	}	//	unlockIt

	/**
	 * 	Invalidate Document
	 * 	@return true if success
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
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

		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocTypeTarget_ID(), getAD_Org_ID());

		//	Lines
		MInvoiceLine[] lines = getLines(true);
		if (lines.length == 0)
		{
			m_processMsg = "@NoLines@";
			return DocAction.STATUS_Invalid;
		}
		
		//faaguilar OFB lines validation begin 
		for (int x = 0; x < lines.length; x++)
			{
				MInvoiceLine line = lines[x];
				if(line.isDescription ())
					continue;
				if (line.getM_Product_ID()==0 && line.getC_Charge_ID()==0 && line.getA_Asset_ID ()==0)
				{
					m_processMsg = "No Productos No Cargos en Lineas";
					return DocAction.STATUS_Invalid;
				}
			}
		//faaguilar OFB lines validation end
		
		//	No Cash Book
		if (PAYMENTRULE_Cash.equals(getPaymentRule())
			&& MCashBook.get(getCtx(), getAD_Org_ID(), getC_Currency_ID()) == null)
		{
			m_processMsg = "@NoCashBook@";
			return DocAction.STATUS_Invalid;
		}

		//	Convert/Check DocType
		if (getC_DocType_ID() != getC_DocTypeTarget_ID() )
			setC_DocType_ID(getC_DocTypeTarget_ID());
		if (getC_DocType_ID() == 0)
		{
			m_processMsg = "No Document Type";
			return DocAction.STATUS_Invalid;
		}

		//explodeBOM(); faaguilar OFB commented
		if (!calculateTaxTotal())	//	setTotals
		{
			m_processMsg = "Error calculating Tax";
			return DocAction.STATUS_Invalid;
		}

		createPaySchedule();

		//	Credit Status
		if (isSOTrx() && !isReversal())
		{
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
			if (MBPartner.SOCREDITSTATUS_CreditStop.equals(bp.getSOCreditStatus()) && !getDocBase().equals(MDocType.DOCBASETYPE_ARCreditMemo))//faaguilar OFB validacion de ARCreditMeMo
			{
				m_processMsg = "@BPartnerCreditStop@ - @TotalOpenBalance@="
					+ bp.getTotalOpenBalance()
					+ ", @SO_CreditLimit@=" + bp.getSO_CreditLimit();
				return DocAction.STATUS_Invalid;
			}
		}

		//	Landed Costs
		if (!isSOTrx())
		{
			for (int i = 0; i < lines.length; i++)
			{
				MInvoiceLine line = lines[i];
				String error = line.allocateLandedCosts();
				if (error != null && error.length() > 0)
				{
					m_processMsg = error;
					return DocAction.STATUS_Invalid;
				}
			}
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;

		//	Add up Amounts
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt

	/**
	 * 	Explode non stocked BOM.
	 */
	private void explodeBOM ()
	{
		String where = "AND IsActive='Y' AND EXISTS "
			+ "(SELECT * FROM M_Product p WHERE C_InvoiceLine.M_Product_ID=p.M_Product_ID"
			+ " AND	p.IsBOM='Y' AND p.IsVerified='Y' AND p.IsStocked='N')";
		//
		String sql = "SELECT COUNT(*) FROM C_InvoiceLine "
			+ "WHERE C_Invoice_ID=? " + where;
		int count = DB.getSQLValueEx(get_TrxName(), sql, getC_Invoice_ID());
		while (count != 0)
		{
			renumberLines (100);

			//	Order Lines with non-stocked BOMs
			MInvoiceLine[] lines = getLines (where);
			for (int i = 0; i < lines.length; i++)
			{
				MInvoiceLine line = lines[i];
				MProduct product = MProduct.get (getCtx(), line.getM_Product_ID());
				log.fine(product.getName());
				//	New Lines
				int lineNo = line.getLine ();

				//find default BOM with valid dates and to this product
				MPPProductBOM bom = MPPProductBOM.get(product, getAD_Org_ID(),getDateInvoiced(), get_TrxName());
				if(bom != null)
				{
					MPPProductBOMLine[] bomlines = bom.getLines(getDateInvoiced());
					for (int j = 0; j < bomlines.length; j++)
					{
						MPPProductBOMLine bomline = bomlines[j];
						MInvoiceLine newLine = new MInvoiceLine (this);
						newLine.setLine (++lineNo);
						newLine.setM_Product_ID (bomline.getM_Product_ID ());
						newLine.setC_UOM_ID (bomline.getC_UOM_ID ());
						newLine.setQty (line.getQtyInvoiced().multiply(
								bomline.getQtyBOM ()));		//	Invoiced/Entered
						if (bomline.getDescription () != null)
							newLine.setDescription (bomline.getDescription ());
						//
						newLine.setPrice ();
						newLine.saveEx (get_TrxName());
					}
				}

				/*MProductBOM[] boms = MProductBOM.getBOMLines (product);
				for (int j = 0; j < boms.length; j++)
				{
					MProductBOM bom = boms[j];
					MInvoiceLine newLine = new MInvoiceLine (this);
					newLine.setLine (++lineNo);
					newLine.setM_Product_ID (bom.getProduct().getM_Product_ID(),
						bom.getProduct().getC_UOM_ID());
					newLine.setQty (line.getQtyInvoiced().multiply(
						bom.getBOMQty ()));		//	Invoiced/Entered
					if (bom.getDescription () != null)
						newLine.setDescription (bom.getDescription ());
					//
					newLine.setPrice ();
					newLine.save (get_TrxName());
				}*/

				//	Convert into Comment Line
				line.setM_Product_ID (0);
				line.setM_AttributeSetInstance_ID (0);
				line.setPriceEntered (Env.ZERO);
				line.setPriceActual (Env.ZERO);
				line.setPriceLimit (Env.ZERO);
				line.setPriceList (Env.ZERO);
				line.setLineNetAmt (Env.ZERO);
				//
				String description = product.getName ();
				if (product.getDescription () != null)
					description += " " + product.getDescription ();
				if (line.getDescription () != null)
					description += " " + line.getDescription ();
				line.setDescription (description);
				line.saveEx (get_TrxName());
			} //	for all lines with BOM

			m_lines = null;
			count = DB.getSQLValue (get_TrxName(), sql, getC_Invoice_ID ());
			renumberLines (10);
		}	//	while count != 0
	}	//	explodeBOM

	/**
	 * 	Calculate Tax and Total
	 * 	@return true if calculated
	 */
	public boolean calculateTaxTotal()
	{
		log.fine("");
		//	Delete Taxes
		DB.executeUpdateEx("DELETE C_InvoiceTax WHERE C_Invoice_ID=" + getC_Invoice_ID(), get_TrxName());
		m_taxes = null;

		//	Lines
		BigDecimal totalLines = Env.ZERO;
		ArrayList<Integer> taxList = new ArrayList<Integer>();
		MInvoiceLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			if (!taxList.contains(line.getC_Tax_ID()))
			{
				MInvoiceTax iTax = MInvoiceTax.get (line, getPrecision(), false, get_TrxName()); //	current Tax
				if (iTax != null)
				{
					iTax.setIsTaxIncluded(isTaxIncluded());
					if (!iTax.calculateTaxFromLines())
						return false;
					iTax.saveEx();
					taxList.add(line.getC_Tax_ID());
				}
			}
			totalLines = totalLines.add(line.getLineNetAmt());
		}

		//	Taxes
		BigDecimal grandTotal = totalLines;
		MInvoiceTax[] taxes = getTaxes(true);
		for (int i = 0; i < taxes.length; i++)
		{
			MInvoiceTax iTax = taxes[i];
			MTax tax = iTax.getTax();
			if (tax.isSummary())
			{
				MTax[] cTaxes = tax.getChildTaxes(false);	//	Multiple taxes
				for (int j = 0; j < cTaxes.length; j++)
				{
					MTax cTax = cTaxes[j];
					BigDecimal taxAmt = cTax.calculateTax(iTax.getTaxBaseAmt(), isTaxIncluded(), getPrecision());
					//
					MInvoiceTax newITax = new MInvoiceTax(getCtx(), 0, get_TrxName());
					newITax.setClientOrg(this);
					newITax.setC_Invoice_ID(getC_Invoice_ID());
					newITax.setC_Tax_ID(cTax.getC_Tax_ID());
					newITax.setPrecision(getPrecision());
					newITax.setIsTaxIncluded(isTaxIncluded());
					newITax.setTaxBaseAmt(iTax.getTaxBaseAmt());
					newITax.setTaxAmt(taxAmt);
					newITax.saveEx(get_TrxName());
					//
					if (!isTaxIncluded())
						grandTotal = grandTotal.add(taxAmt);
				}
				iTax.deleteEx(true, get_TrxName());
			}
			else
			{
				if (!isTaxIncluded())
					grandTotal = grandTotal.add(iTax.getTaxAmt());
			}
		}
		//
		setTotalLines(totalLines);
		setGrandTotal(grandTotal);
		return true;
	}	//	calculateTaxTotal


	/**
	 * 	(Re) Create Pay Schedule
	 *	@return true if valid schedule
	 */
	private boolean createPaySchedule()
	{
		if (getC_PaymentTerm_ID() == 0)
			return false;
		MPaymentTerm pt = new MPaymentTerm(getCtx(), getC_PaymentTerm_ID(), null);
		log.fine(pt.toString());
		return pt.apply(this);		//	calls validate pay schedule
	}	//	createPaySchedule


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
		StringBuffer info = new StringBuffer();

  		//	Create Cash
		if (PAYMENTRULE_Cash.equals(getPaymentRule()))
		{
			// Modifications for POSterita
            //
            //    MCash cash = MCash.get (getCtx(), getAD_Org_ID(),
            //    getDateInvoiced(), getC_Currency_ID(), get_TrxName());

			MCash cash;
			/**
			 * faaguilar OFB
			 * verificacion de existencia de libro efectivo por defecto*/
			//faaguilar OFB cashbook default begin 
			int C_CashBook_ID=DB.getSQLValue(get_TrxName(),"select C_CashBook_ID from C_CashBook where ISDEFAULT='Y' and AD_Org_ID IN (0,"+getAD_Org_ID()+") and AD_Client_ID="+getAD_Client_ID());
			log.config("select C_CashBook_ID from C_CashBook where ISDEFAULT='Y' and AD_Org_ID IN (0,"+getAD_Org_ID()+") and AD_Client_ID="+getAD_Client_ID());
			if(C_CashBook_ID<=0)
			{
				m_processMsg = "@No existe un libro de efectivo por defecto@";
				return DocAction.STATUS_Invalid;
			}
			//faaguilar OFB cashbook default end
			
            int posId = Env.getContextAsInt(getCtx(),Env.POS_ID);

            if (posId != 0)
            {
                MPOS pos = new MPOS(getCtx(),posId,get_TrxName());
                int cashBookId = pos.getC_CashBook_ID();
                cash = MCash.get(getCtx(),cashBookId,getDateInvoiced(),get_TrxName());
            }
            else
            {
                cash = MCash.get (getCtx(), getAD_Org_ID(),
                        getDateInvoiced(), getC_Currency_ID(), get_TrxName());
                
                /**
                 * faaguilar OFB
                 * forma de traer el cashjournal por OFB*/
                cash = MCash.getDefault (getCtx(), getAD_Client_ID(), 
        				C_CashBook_ID,  get_TrxName());
            }

            // End Posterita Modifications

			if (cash == null || cash.get_ID() == 0)
			{
				m_processMsg = "@NoCashBook@";
				return DocAction.STATUS_Invalid;
			}
			MCashLine cl = new MCashLine (cash);
			cl.setInvoice(this);
			if (!cl.save(get_TrxName()))
			{
				m_processMsg = "Could not save Cash Journal Line";
				return DocAction.STATUS_Invalid;
			}
			info.append("@C_Cash_ID@: " + cash.getName() +  " #" + cl.getLine());
			setC_CashLine_ID(cl.getC_CashLine_ID());
		}	//	CashBook

		//	Update Order & Match
		int matchInv = 0;
		int matchPO = 0;
		MInvoiceLine[] lines = getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];

			//	Update Order Line
			MOrderLine ol = null;
			if (line.getC_OrderLine_ID() != 0)
			{
				if (isSOTrx()
					|| line.getM_Product_ID() == 0)
				{
					ol = new MOrderLine (getCtx(), line.getC_OrderLine_ID(), get_TrxName());
					if (line.getQtyInvoiced() != null)
						ol.setQtyInvoiced(ol.getQtyInvoiced().add(line.getQtyInvoiced()));
					if (!ol.save(get_TrxName()))
					{
						m_processMsg = "Could not update Order Line";
						return DocAction.STATUS_Invalid;
					}
				}
				//	Order Invoiced Qty updated via Matching Inv-PO
				else if (!isSOTrx()
					&& line.getM_Product_ID() != 0
					&& !isReversal())
				{
					//	MatchPO is created also from MInOut when Invoice exists before Shipment
					BigDecimal matchQty = line.getQtyInvoiced();
					MMatchPO po = MMatchPO.create (line, null,
						getDateInvoiced(), matchQty);
					boolean isNewMatchPO = false;
					if (po.get_ID() == 0)
						isNewMatchPO = true;
					if (!po.save(get_TrxName()))
					{
						m_processMsg = "Could not create PO Matching";
						return DocAction.STATUS_Invalid;
					}
					matchPO++;
					if (isNewMatchPO)
						addDocsPostProcess(po);
				}
			}

			//Update QtyInvoiced RMA Line
			if (line.getM_RMALine_ID() != 0)
			{
				MRMALine rmaLine = new MRMALine (getCtx(),line.getM_RMALine_ID(), get_TrxName());
				if (rmaLine.getQtyInvoiced() != null)
					rmaLine.setQtyInvoiced(rmaLine.getQtyInvoiced().add(line.getQtyInvoiced()));
				else
					rmaLine.setQtyInvoiced(line.getQtyInvoiced());
				if (!rmaLine.save(get_TrxName()))
				{
					m_processMsg = "Could not update RMA Line";
					return DocAction.STATUS_Invalid;
				}
			}
			//

			//	Matching - Inv-Shipment
			if (!isSOTrx()
				&& line.getM_InOutLine_ID() != 0
				&& line.getM_Product_ID() != 0
				&& !isReversal())
			{
				MInOutLine receiptLine = new MInOutLine (getCtx(),line.getM_InOutLine_ID(), get_TrxName());
				BigDecimal matchQty = line.getQtyInvoiced();

				if (receiptLine.getMovementQty().compareTo(matchQty) < 0)
					matchQty = receiptLine.getMovementQty();

				MMatchInv inv = new MMatchInv(line, getDateAcct(), matchQty);
				boolean isNewMatchInv = false;
				if (inv.get_ID() == 0)
					isNewMatchInv = true;
				if (!inv.save(get_TrxName()))
				{
					m_processMsg = CLogger.retrieveErrorString("Could not create Invoice Matching");
					return DocAction.STATUS_Invalid;
				}
				matchInv++;
				if (isNewMatchInv)
					addDocsPostProcess(inv);
			}
		}	//	for all lines
		if (matchInv > 0)
			info.append(" @M_MatchInv_ID@#").append(matchInv).append(" ");
		if (matchPO > 0)
			info.append(" @M_MatchPO_ID@#").append(matchPO).append(" ");

		/**
		 * faaguilar OFB
		 * solo si no proviene de una orden actualiza los saldos del bp*/
		if(getC_Order_ID()==0 )//faaguilar OFB added
		{
			//	Update BP Statistics
			MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_TrxName());
			//	Update total revenue and balance / credit limit (reversed on AllocationLine.processIt)
			BigDecimal invAmt = MConversionRate.convertBase(getCtx(), getGrandTotal(true),	//	CM adjusted
				getC_Currency_ID(), getDateAcct(), getC_ConversionType_ID(), getAD_Client_ID(), getAD_Org_ID());
			if (invAmt == null)
			{
				m_processMsg = "Could not convert C_Currency_ID=" + getC_Currency_ID()
					+ " to base C_Currency_ID=" + MClient.get(Env.getCtx()).getC_Currency_ID();
				return DocAction.STATUS_Invalid;
			}
			//	Total Balance
			BigDecimal newBalance = bp.getTotalOpenBalance(false);
			if (newBalance == null)
				newBalance = Env.ZERO;
			if (isSOTrx())
			{
				newBalance = newBalance.add(invAmt);
				//
				if (bp.getFirstSale() == null)
					bp.setFirstSale(getDateInvoiced());
				BigDecimal newLifeAmt = bp.getActualLifeTimeValue();
				if (newLifeAmt == null)
					newLifeAmt = invAmt;
				else
					newLifeAmt = newLifeAmt.add(invAmt);
				BigDecimal newCreditAmt = bp.getSO_CreditUsed();
				if (newCreditAmt == null)
					newCreditAmt = invAmt;
				else
					newCreditAmt = newCreditAmt.add(invAmt);
				//
				log.fine("GrandTotal=" + getGrandTotal(true) + "(" + invAmt
					+ ") BP Life=" + bp.getActualLifeTimeValue() + "->" + newLifeAmt
					+ ", Credit=" + bp.getSO_CreditUsed() + "->" + newCreditAmt
					+ ", Balance=" + bp.getTotalOpenBalance(false) + " -> " + newBalance);
				bp.setActualLifeTimeValue(newLifeAmt);
				bp.setSO_CreditUsed(newCreditAmt);
			}	//	SO
			else
			{
				newBalance = newBalance.subtract(invAmt);
				log.fine("GrandTotal=" + getGrandTotal(true) + "(" + invAmt
					+ ") Balance=" + bp.getTotalOpenBalance(false) + " -> " + newBalance);
			}
			bp.setTotalOpenBalance(newBalance);
			bp.setSOCreditStatus();
			if (!bp.save(get_TrxName()))
			{
				m_processMsg = "Could not update Business Partner";
				return DocAction.STATUS_Invalid;
			}
		}

		//	User - Last Result/Contact
		if (getAD_User_ID() != 0)
		{
			MUser user = new MUser (getCtx(), getAD_User_ID(), get_TrxName());
			user.setLastContact(new Timestamp(System.currentTimeMillis()));
			user.setLastResult(Msg.translate(getCtx(), "C_Invoice_ID") + ": " + getDocumentNo());
			if (!user.save(get_TrxName()))
			{
				m_processMsg = "Could not update Business Partner User";
				return DocAction.STATUS_Invalid;
			}
		}	//	user

		//	Update Project
		if (isSOTrx() && getC_Project_ID() != 0)
		{
			MProject project = new MProject (getCtx(), getC_Project_ID(), get_TrxName());
			BigDecimal amt = getGrandTotal(true);
			int C_CurrencyTo_ID = project.getC_Currency_ID();
			if (C_CurrencyTo_ID != getC_Currency_ID())
				amt = MConversionRate.convert(getCtx(), amt, getC_Currency_ID(), C_CurrencyTo_ID,
					getDateAcct(), 0, getAD_Client_ID(), getAD_Org_ID());
			if (amt == null)
			{
				m_processMsg = "Could not convert C_Currency_ID=" + getC_Currency_ID()
					+ " to Project C_Currency_ID=" + C_CurrencyTo_ID;
				return DocAction.STATUS_Invalid;
			}
			BigDecimal newAmt = project.getInvoicedAmt();
			if (newAmt == null)
				newAmt = amt;
			else
				newAmt = newAmt.add(amt);
			log.fine("GrandTotal=" + getGrandTotal(true) + "(" + amt
				+ ") Project " + project.getName()
				+ " - Invoiced=" + project.getInvoicedAmt() + "->" + newAmt);
			project.setInvoicedAmt(newAmt);
			if (!project.save(get_TrxName()))
			{
				m_processMsg = "Could not update Project";
				return DocAction.STATUS_Invalid;
			}
		}	//	project

		//	User Validation
		//Begin faaguilar OFB Correlativo,Protestos,Cambios
		updateSpecialDocs();
		//End faaguilar OFB
		
		

		updateAsset();//faaguilar OFB assets
		// Set the definite document number after completed (if needed)
		setDefiniteDocumentNo();

		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		//	Counter Documents
		MInvoice counter = createCounterDoc();
		if (counter != null)
			info.append(" - @CounterDoc@: @C_Invoice_ID@=").append(counter.getDocumentNo());

		X_AD_Client client = new X_AD_Client(getCtx(), getAD_Client_ID(), get_TrxName());
		//ininoles validacion para generacion de xml generico
		String DTEType = "";
		
		try 
		{
			DTEType = client.get_ValueAsString("invoiceDTEType");
		}
		catch (Exception e)
		{			
			DTEType = "";
			log.log(Level.SEVERE, "No se pudo setear variable invoiceDTEType", e);
		}
		
		if(DTEType.compareToIgnoreCase("N") == 0)
		{
			CreateXML();//faaguilar OFB			
		}		
		else if(DTEType.compareToIgnoreCase("CGN") == 0)
		{
			ExportDTEInvoiceCG cg = new ExportDTEInvoiceCG();
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			cg.CreateXMLCG(this);
		}
		else if(DTEType.compareToIgnoreCase("NGEM") == 0)
		{
			ExportDTEInvoiceNGem cg = new ExportDTEInvoiceNGem();
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			cg.CreateXML(this);
		}
		else if(DTEType.compareToIgnoreCase("CGGEM") == 0)
		{
			ExportDTEInvoiceCGGemV2 cg = new ExportDTEInvoiceCGGemV2();
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			cg.CreateXML(this);
		}
		else if(DTEType.compareToIgnoreCase("CGPRO") == 0)
		{
			ExportDTEInvoiceCGProvectis cg = new ExportDTEInvoiceCGProvectis();
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			cg.CreateXMLCG(this);
		}		
		else if(DTEType.compareToIgnoreCase("NBK") == 0)
		{
			ExportDTEInvoiceNBaskakow cg = new ExportDTEInvoiceNBaskakow();
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			cg.CreateXML(this);
		}	
		else if(DTEType.compareToIgnoreCase("CGBK") == 0)
		{
			ExportDTEInvoiceCGBaskakow cg = new ExportDTEInvoiceCGBaskakow();
			//ExportDTEInvoiceCGBaskakowPRUEBA cg = new ExportDTEInvoiceCGBaskakowPRUEBA();
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			cg.CreateXMLCG(this);
		}
		else if(DTEType.compareToIgnoreCase("FELQDC") == 0)
		{
			ExportDTEInvoiceFOLQDC cg = new ExportDTEInvoiceFOLQDC();
			if(getC_DocTypeTarget().getName().toLowerCase().contains("boleta"))
				cg.CreateXMLCGBoleta(this);
			else
				cg.CreateXMLCG(this);
		}	
		else if(DTEType.compareToIgnoreCase("FELNG") == 0)
		{
			ExportDTEInvoiceFOLNGem cg = new ExportDTEInvoiceFOLNGem();
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			cg.CreateXMLCG(this);
		}
		else if(DTEType.compareToIgnoreCase("FELCGG") == 0)
		{
			ExportDTEInvoiceFOLCGGem cg = new ExportDTEInvoiceFOLCGGem();
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			cg.CreateXMLCG(this);
		}
		else if(DTEType.compareToIgnoreCase("ITSA") == 0)
		{
			ExportDTEInvoiceITSA cg = new ExportDTEInvoiceITSA();
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			cg.CreateXMLCGITSA(this);
		}
		else if(DTEType.compareToIgnoreCase("FELN") == 0)
		{
			ExportDTEInvoiceFOL cg = new ExportDTEInvoiceFOL();
			if(getC_DocTypeTarget().getName().toLowerCase().contains("boleta"))
				cg.CreateXMLCGBoleta(this);
			else
				cg.CreateXMLCG(this);
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			//cg.CreateXMLCG(this);
		}
		else if(DTEType.compareToIgnoreCase("FELPA") == 0)
		{
			ExportDTEInvoiceFOLPA cg = new ExportDTEInvoiceFOLPA();
			if(getC_DocTypeTarget().getName().toLowerCase().contains("boleta"))
				cg.CreateXMLBoleta(this);
			else
				cg.CreateXML(this);
			//MInvoice inv = new MInvoice(getCtx(), get_ID(), get_TrxName());
			//cg.CreateXMLCG(this);
		}
		else
		{
			CreateXML();//faaguilar OFB
		}			
		
		m_processMsg = info.toString().trim();
		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt

	/* Save array of documents to process AFTER completing this one */
	ArrayList<PO> docsPostProcess = new ArrayList<PO>();

	private void addDocsPostProcess(PO doc) {
		docsPostProcess.add(doc);
	}

	public ArrayList<PO> getDocsPostProcess() {
		return docsPostProcess;
	}

	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateInvoiced(new Timestamp (System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = DB.getDocumentNo(getC_DocType_ID(), get_TrxName(), true, this);
			if (value != null)
				setDocumentNo(value);
		}
	}

	/**
	 * 	Create Counter Document
	 * 	@return counter invoice
	 */
	private MInvoice createCounterDoc()
	{
		//	Is this a counter doc ?
		if (getRef_Invoice_ID() != 0)
			return null;

		//	Org Must be linked to BPartner
		MOrg org = MOrg.get(getCtx(), getAD_Org_ID());
		int counterC_BPartner_ID = org.getLinkedC_BPartner_ID(get_TrxName());
		if (counterC_BPartner_ID == 0)
			return null;
		//	Business Partner needs to be linked to Org
		MBPartner bp = new MBPartner (getCtx(), getC_BPartner_ID(), null);
		int counterAD_Org_ID = bp.getAD_OrgBP_ID_Int();
		if (counterAD_Org_ID == 0)
			return null;

		MBPartner counterBP = new MBPartner (getCtx(), counterC_BPartner_ID, null);
//		MOrgInfo counterOrgInfo = MOrgInfo.get(getCtx(), counterAD_Org_ID);
		log.info("Counter BP=" + counterBP.getName());

		//	Document Type
		int C_DocTypeTarget_ID = 0;
		MDocTypeCounter counterDT = MDocTypeCounter.getCounterDocType(getCtx(), getC_DocType_ID());
		if (counterDT != null)
		{
			log.fine(counterDT.toString());
			if (!counterDT.isCreateCounter() || !counterDT.isValid())
				return null;
			C_DocTypeTarget_ID = counterDT.getCounter_C_DocType_ID();
		}
		else	//	indirect
		{
			C_DocTypeTarget_ID = MDocTypeCounter.getCounterDocType_ID(getCtx(), getC_DocType_ID());
			log.fine("Indirect C_DocTypeTarget_ID=" + C_DocTypeTarget_ID);
			if (C_DocTypeTarget_ID <= 0)
				return null;
		}

		//	Deep Copy
		MInvoice counter = copyFrom(this, getDateInvoiced(), getDateAcct(),
			C_DocTypeTarget_ID, !isSOTrx(), true, get_TrxName(), true);
		//
		counter.setAD_Org_ID(counterAD_Org_ID);
	//	counter.setM_Warehouse_ID(counterOrgInfo.getM_Warehouse_ID());
		//
		counter.setBPartner(counterBP);
		//	Refernces (Should not be required
		counter.setSalesRep_ID(getSalesRep_ID());
		counter.save(get_TrxName());

		//	Update copied lines
		MInvoiceLine[] counterLines = counter.getLines(true);
		for (int i = 0; i < counterLines.length; i++)
		{
			MInvoiceLine counterLine = counterLines[i];
			counterLine.setClientOrg(counter);
			counterLine.setInvoice(counter);	//	copies header values (BP, etc.)
			counterLine.setPrice();
			counterLine.setTax();
			//
			counterLine.save(get_TrxName());
		}

		log.fine(counter.toString());

		//	Document Action
		if (counterDT != null)
		{
			if (counterDT.getDocAction() != null)
			{
				counter.setDocAction(counterDT.getDocAction());
				counter.processIt(counterDT.getDocAction());
				counter.save(get_TrxName());
			}
		}
		return counter;
	}	//	createCounterDoc

	/**
	 * 	Void Document.
	 * 	@return true if success
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
			setDocAction(DOCACTION_None);
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
			MInvoiceLine[] lines = getLines(false);
			for (int i = 0; i < lines.length; i++)
			{
				MInvoiceLine line = lines[i];
				BigDecimal old = line.getQtyInvoiced();
				if (old.compareTo(Env.ZERO) != 0)
				{
					line.setQty(Env.ZERO);
					line.setTaxAmt(Env.ZERO);
					line.setLineNetAmt(Env.ZERO);
					line.setLineTotalAmt(Env.ZERO);
					line.addDescription(Msg.getMsg(getCtx(), "Voided") + " (" + old + ")");
					//	Unlink Shipment
					if (line.getM_InOutLine_ID() != 0)
					{
						MInOutLine ioLine = new MInOutLine(getCtx(), line.getM_InOutLine_ID(), get_TrxName());
						ioLine.setIsInvoiced(false);
						ioLine.save(get_TrxName());
						line.setM_InOutLine_ID(0);
					}
					line.save(get_TrxName());
				}
			}
			addDescription(Msg.getMsg(getCtx(), "Voided"));
			setIsPaid(true);
			setC_Payment_ID(0);
		}
		else
		{
			/** faaguilar OFB
			 * valida que no existan pagos completados asociados al documento*/
			if(getPayments()>0)
			{
				m_processMsg = "Este Documento tiene pagos asociados no puede ser ANULADO";
				//setDocAction(DOCACTION_None);
				return false;	
			}//END faaguilar OFB payments relationship
			
			/**faaguilar OFB
			 * validaciones al anular si es PTK, CDC, FAT*/
			if(getDocBase().equals("PTK"))	
					if(isBankReverse())
					{
						m_processMsg="Existe un Movimiento Bancario de Reversa que debe ser Anulado Primero";
						return false;
					}
			
			voidSpecialDocs();
			if(!voidCashBook())
			{
				return false;
			}
			setC_CashLine_ID(0);
			//faaguilar OFB END validaciones documentos especiales
			
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

		setProcessed(true);
		setDocAction(DOCACTION_None);

		// After Close
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (m_processMsg != null)
			return false;
		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction - same date
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());
		// Before reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (m_processMsg != null)
			return false;

		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
		//
		MAllocationHdr[] allocations = MAllocationHdr.getOfInvoice(getCtx(),
			getC_Invoice_ID(), get_TrxName());
		for (int i = 0; i < allocations.length; i++)
		{
			allocations[i].setDocAction(DocAction.ACTION_Reverse_Correct);
			allocations[i].reverseCorrectIt();
			allocations[i].save(get_TrxName());
		}
		//	Reverse/Delete Matching
		if (!isSOTrx())
		{
			MMatchInv[] mInv = MMatchInv.getInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
			for (int i = 0; i < mInv.length; i++)
				mInv[i].delete(true);
			MMatchPO[] mPO = MMatchPO.getInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
			for (int i = 0; i < mPO.length; i++)
			{
				if (mPO[i].getM_InOutLine_ID() == 0)
					mPO[i].delete(true);
				else
				{
					mPO[i].setC_InvoiceLine_ID(null);
					mPO[i].save(get_TrxName());
				}
			}
		}
		//
		load(get_TrxName());	//	reload allocation reversal info

		/**
		 * faaguilar OFB
		 * comentado codigo que genera una factura
		 * espejo con movimientos inversos*/
		//	Deep Copy
		/*MInvoice reversal = copyFrom (this, getDateInvoiced(), getDateAcct(),
			getC_DocType_ID(), isSOTrx(), false, get_TrxName(), true);
		if (reversal == null)
		{
			m_processMsg = "Could not create Invoice Reversal";
			return false;
		}
		reversal.setReversal(true);

		//	Reverse Line Qty
		MInvoiceLine[] rLines = reversal.getLines(false);
		for (int i = 0; i < rLines.length; i++)
		{
			MInvoiceLine rLine = rLines[i];
			rLine.setQtyEntered(rLine.getQtyEntered().negate());
			rLine.setQtyInvoiced(rLine.getQtyInvoiced().negate());
			rLine.setLineNetAmt(rLine.getLineNetAmt().negate());
			if (rLine.getTaxAmt() != null && rLine.getTaxAmt().compareTo(Env.ZERO) != 0)
				rLine.setTaxAmt(rLine.getTaxAmt().negate());
			if (rLine.getLineTotalAmt() != null && rLine.getLineTotalAmt().compareTo(Env.ZERO) != 0)
				rLine.setLineTotalAmt(rLine.getLineTotalAmt().negate());
			if (!rLine.save(get_TrxName()))
			{
				m_processMsg = "Could not correct Invoice Reversal Line";
				return false;
			}
		}
		reversal.setC_Order_ID(getC_Order_ID());
		reversal.addDescription("{->" + getDocumentNo() + ")");
		//FR1948157
		reversal.setReversal_ID(getC_Invoice_ID());
		reversal.saveEx(get_TrxName());
		//
		if (!reversal.processIt(DocAction.ACTION_Complete))
		{
			m_processMsg = "Reversal ERROR: " + reversal.getProcessMsg();
			return false;
		}
		reversal.setC_Payment_ID(0);
		reversal.setIsPaid(true);
		reversal.closeIt();
		reversal.setProcessing (false);
		reversal.setDocStatus(DOCSTATUS_Reversed);
		reversal.setDocAction(DOCACTION_None);
		reversal.saveEx(get_TrxName());
		m_processMsg = reversal.getDocumentNo();
		//
		addDescription("(" + reversal.getDocumentNo() + "<-)");

		//	Clean up Reversed (this)
		MInvoiceLine[] iLines = getLines(false);
		for (int i = 0; i < iLines.length; i++)
		{
			MInvoiceLine iLine = iLines[i];
			if (iLine.getM_InOutLine_ID() != 0)
			{
				MInOutLine ioLine = new MInOutLine(getCtx(), iLine.getM_InOutLine_ID(), get_TrxName());
				ioLine.setIsInvoiced(false);
				ioLine.save(get_TrxName());
				//	Reconsiliation
				iLine.setM_InOutLine_ID(0);
				iLine.save(get_TrxName());
			}
        }
		setProcessed(true);
		//FR1948157
		setReversal_ID(reversal.getC_Invoice_ID());
		setDocStatus(DOCSTATUS_Reversed);	//	may come from void
		setDocAction(DOCACTION_None);
		setC_Payment_ID(0);
		setIsPaid(true);

		//	Create Allocation
		MAllocationHdr alloc = new MAllocationHdr(getCtx(), false, getDateAcct(),
			getC_Currency_ID(),
			Msg.translate(getCtx(), "C_Invoice_ID")	+ ": " + getDocumentNo() + "/" + reversal.getDocumentNo(),
			get_TrxName());
		alloc.setAD_Org_ID(getAD_Org_ID());
		if (alloc.save())
		{
			//	Amount
			BigDecimal gt = getGrandTotal(true);
			if (!isSOTrx())
				gt = gt.negate();
			//	Orig Line
			MAllocationLine aLine = new MAllocationLine (alloc, gt,
				Env.ZERO, Env.ZERO, Env.ZERO);
			aLine.setC_Invoice_ID(getC_Invoice_ID());
			aLine.save();
			//	Reversal Line
			MAllocationLine rLine = new MAllocationLine (alloc, gt.negate(),
				Env.ZERO, Env.ZERO, Env.ZERO);
			rLine.setC_Invoice_ID(reversal.getC_Invoice_ID());
			rLine.save();
			//	Process It
			if (alloc.processIt(DocAction.ACTION_Complete))
				alloc.save();
		}
		* fin comentado codigo de factura reversa/
		// After reverseCorrect
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (m_processMsg != null)
			return false;
		/**
		 * faaguilar OFB
		 * anulacion de factura, cantidades , relaciones
		 * segun esquema OFB*/
		MInvoiceLine[] iLines = getLines(false);
		for (int i = 0; i < iLines.length; i++)
		{
			MInvoiceLine iLine = iLines[i];
			if (iLine.getM_InOutLine_ID() != 0)
			{
				MInOutLine ioLine = new MInOutLine(getCtx(), iLine.getM_InOutLine_ID(), get_TrxName());
				ioLine.setIsInvoiced(false);
				ioLine.save(get_TrxName());
				//	Reconsiliation
				iLine.setM_InOutLine_ID(0);
				iLine.save(get_TrxName());
			}
			if (iLine.getC_OrderLine_ID()!=0)
			{
				MOrderLine oline = new MOrderLine(getCtx(), iLine.getC_OrderLine_ID(), get_TrxName());
				oline.setQtyInvoiced (Env.ZERO);
				oline.save();
				
			}
			
			//faaguilar resetcost begin
			  resetCost();
			//faaguilar resetcost end
			
			iLine.addDescription(":"+iLine.get_ValueAsInt("C_Payment_ID"));
			iLine.setQty(Env.ZERO);//faaguilar OFB deja en 0 todos los montos al anular
			iLine.setTaxAmt(Env.ZERO);//faaguilar OFB deja en 0 todos los montos al anular
			iLine.setLineNetAmt(Env.ZERO);//faaguilar OFB deja en 0 todos los montos al anular
			iLine.setLineTotalAmt(Env.ZERO);//faaguilar OFB deja en 0 todos los montos al anular
			iLine.set_ValueOfColumn("C_Payment_ID", 0);
			iLine.setQtyInvoiced(Env.ZERO);
			iLine.save();
        }
			
		setProcessed(true);
		setDocStatus(DOCSTATUS_Voided);	//	may come from void
		setDocAction(DOCACTION_None);
		setC_Payment_ID(0);
		setIsPaid(true);
		
		setTotalLines(Env.ZERO);//faaguilar OFB deja en 0
		setGrandTotal(Env.ZERO);//faaguilar OFB deja en 0
		/**
		 * faaguilar OFB
		 * reseteo de contabilidad*/
		int i=resetPost(getC_Invoice_ID(),X_C_Invoice.Table_ID);
		log.config("reset post:"+i);
		i=DB.executeUpdate("update C_INVOICETAX set TAXAMT=0 where C_Invoice_ID="+getC_Invoice_ID(), get_TrxName());
		MFactAcct.deleteEx(MInvoice.Table_ID, getC_Invoice_ID(), get_TrxName());
		
		//se usara nuevo metodo para tsm con fecha y hora del anulado ininoles
		if(OFBForward.UseDocNoVoidTSM())
		{
			setDocumentNo(getDocumentNo()+"-"+new Timestamp(System.currentTimeMillis()).toString().substring(0, 16));
		}else // sino se usara metodo normal de fabian
		{	
			/**
			 * faaguilar OFB
			 * le agrega la palabra "anulada" y un numero que representa el numero de 
			 * veces que la misma factura ya ah sido anulada, en el caso de ser factura de compra.*/
			//faaguilar OFB begin		
			if (!isSOTrx())
			{
				int count=DB.getSQLValue(get_TrxName(), "select count(1) from C_Invoice where DocStatus='VO' and  AD_Org_ID="+getAD_Org_ID()
						+" and C_DocType_ID="+getC_DocType_ID()+" and documentno='"+getDocumentNo()+"' and C_Invoice_ID<>"+getC_Invoice_ID());
				if(count<0)
					count=0;
				count ++;
				setDocumentNo(getDocumentNo()+"_Anulada_"+count);
			}
		}
		//faaguilar OFB end
		
		return true;
	}	//	reverseCorrectIt

	/**
	 * 	Reverse Accrual - none
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
		//	: Grand Total = 123.00 (#1)
		sb.append(": ").
			append(Msg.translate(getCtx(),"GrandTotal")).append("=").append(getGrandTotal())
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
		return getSalesRep_ID();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return getGrandTotal();
	}	//	getApprovalAmt

	/**
	 *
	 * @param rma
	 */
	public void setRMA(MRMA rma)
	{
		setM_RMA_ID(rma.getM_RMA_ID());
        setAD_Org_ID(rma.getAD_Org_ID());
        setDescription(rma.getDescription());
        setC_BPartner_ID(rma.getC_BPartner_ID());
        setSalesRep_ID(rma.getSalesRep_ID());

        setGrandTotal(rma.getAmt());
        setIsSOTrx(rma.isSOTrx());
        setTotalLines(rma.getAmt());

        MInvoice originalInvoice = rma.getOriginalInvoice();

        if (originalInvoice == null)
        {
            throw new IllegalStateException("Not invoiced - RMA: " + rma.getDocumentNo());
        }

        setC_BPartner_Location_ID(originalInvoice.getC_BPartner_Location_ID());
        setAD_User_ID(originalInvoice.getAD_User_ID());
        setC_Currency_ID(originalInvoice.getC_Currency_ID());
        setIsTaxIncluded(originalInvoice.isTaxIncluded());
        setM_PriceList_ID(originalInvoice.getM_PriceList_ID());
        setC_Project_ID(originalInvoice.getC_Project_ID());
        setC_Activity_ID(originalInvoice.getC_Activity_ID());
        setC_Campaign_ID(originalInvoice.getC_Campaign_ID());
        setUser1_ID(originalInvoice.getUser1_ID());
        setUser2_ID(originalInvoice.getUser2_ID());
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
	 * faaguilar OFB
	 * trae el docbasetype del documento invoice
	 * */
	public String getDocBase()
	{
		
		String base;
		base=DB.getSQLValueString(get_TrxName(),"select docbasetype from c_doctype where c_doctype_id=?",getC_DocTypeTarget_ID());
		return base;
	}
	
	/**
	 * faaguilar OFB
	 * actualiza correlativo en facturas de compra 
	 * -si es protesto de cheques, reversa el banco y protesta los pagos
	 * -si es factoring realiza los movimientos segun corresponda
	 * -si es cambio de documentos, deja en custodio C los documentos de pago del detalle
	 * */
	public void updateSpecialDocs()
	{
		log.config("docbase");
		String docbase=getDocBase();
		
		log.config("before update correlativo");
		//@mfrojas se agrega validacion para que en un cliente específico no se realice la actualización de correlativo.
		
		X_AD_Client client = new X_AD_Client(getCtx(), getAD_Client_ID(), get_TrxName());
		
		String DTEType = "";
				
		try 
		{
			DTEType = client.get_ValueAsString("invoiceDTEType");
		}
		catch (Exception e)
		{			
			DTEType = "";
			log.log(Level.SEVERE, "No se pudo setear variable invoiceDTEType", e);
		}

		if(DTEType.compareToIgnoreCase("ITSA") != 0)
		{	
			if(docbase.equals("API") || docbase.equals("APC")) //quitadas APB
			{
				if(isSOTrx()==false)
				{
					String date1=getDateAcct().toString().substring(0,10);
					String mysql="Select nvl(max(I.correlativo),0) FROM C_INVOICE I "
							+" WHere i.AD_CLIENT_ID="+ getAD_Client_ID() +" AND "
							+" TRIM(TO_Char(i.DATEacct,'mm'))= '"+ date1.substring(5,7)+"'"
							+" AND	i.ISSOTRX='N' AND TRIM(TO_Char(i.DATEacct,'yyyy'))= '"+ date1.substring(0,4)+"'"
							+" AND i.DOCSTATUS IN ('CO','CL') and Isactive='Y' ";
					if(OFBForward.UseCorrelativeAPForDocType())
						mysql = mysql + "AND C_DocType_ID = "+getC_DocTypeTarget_ID();

					int nextcorr=DB.getSQLValue("C_Invoice",mysql);
					if(nextcorr==0)
					{
						DB.executeUpdate("update c_invoice set correlativo="+1+" where c_invoice_id="+getC_Invoice_ID(),get_TrxName());
					}
					else
					{
						nextcorr=nextcorr+1;
						DB.executeUpdate("update c_invoice set correlativo="+nextcorr+" where c_invoice_id="+getC_Invoice_ID(),get_TrxName());
					}
				}
			}
			else//Boleta and Protesto and Change
				DB.executeUpdate("update c_invoice set correlativo=0 where c_invoice_id="+getC_Invoice_ID(),get_TrxName());
		
			log.config("after update correlativo");
		//END Faaguilar OFB Correlativo issotrx=N
		}
		
		//@mfrojas end
		//faaguilar OFB Protestos, Changes , Factoring
		if(docbase.equals("PTK"))
		{
			MInvoiceLine[] iLines = getLines(false);
			String mysql="Update C_Payment set IsProtested='Y', C_InvoiceLine_ID="+iLines[0].getC_InvoiceLine_ID()
							+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
							log.config("update ptk");
						    int	no = DB.executeUpdate(mysql,get_TrxName());
						   
			MDocType docPtk = new MDocType(getCtx(), getC_DocTypeTarget_ID(), get_TrxName());			
			String valuePTK = "BA";
			Boolean generateBS = true;
			try
			{
				valuePTK = docPtk.get_ValueAsString("ptkType");
				//para evitar errores leemos variable como string y luego se setea el booleano
				//generateBS = docPtk.get_ValueAsBoolean("generateBS");
				String genBSTxt = docPtk.get_ValueAsString("generateBS");
				if(genBSTxt != null && genBSTxt.compareTo("N") == 0)
					generateBS = false;
				else
					generateBS = true;
			}
			catch(Exception e)
	        {
				valuePTK = "BA";
				generateBS = true;
				log.log(Level.SEVERE, "No se pudo setear variable ptkType", e);
				log.log(Level.SEVERE, "No se pudo setear variable generateBS", e);
	        }
			
			if (valuePTK.compareToIgnoreCase("NB")==0)
			{
				String mysqlUPPtkNB="Update C_Payment set IsReconciled = 'Y' "
					+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
				log.config("update ptk NB");
				int	pnbC = DB.executeUpdate(mysqlUPPtkNB,get_TrxName());
			}	
			else
			{
				if(generateBS == false)
				{
					String mysqlUPPtkB="Update C_Payment set IsReconciled = 'Y' "
							+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
					log.config("update ptk Bancario");
					int	pbC = DB.executeUpdate(mysqlUPPtkB,get_TrxName());
				}	
				else if (generateBS)
					reverseBank();
				else
					reverseBank();
			}		
		}
		else if(docbase.equals("CDC") || docbase.equals("VDC"))
		{
			 MInvoiceLine[] iLines = getLines(false);
			    for(MInvoiceLine il:iLines){
			    	if(il.get_ValueAsInt("C_Payment_ID")>0){
						String mysql="Update C_Payment set Custodio='C', C_InvoiceLine_ID="+il.getC_InvoiceLine_ID()
									+" Where C_Payment_ID ="+il.get_ValueAsInt("C_Payment_ID");
									log.config("update cdc vdc");
							    int	no = DB.executeUpdate(mysql,get_TrxName());
			    	}
			    	if(il.get_ValueAsInt("C_InvoiceFac_ID")>0){
			    		String mysql="Update C_Invoice set isPaid='Y' "
						+" Where C_Invoice_ID="+il.get_ValueAsInt("C_InvoiceFac_ID");
						log.config("update cdc vdc");
				    int	no = DB.executeUpdate(mysql,get_TrxName());
			    	}
						    
			    }
			
		}
		else if(docbase.equals("FAT"))
		{
			if(get_ValueAsBoolean("PutInFactoring"))
			{
				MInvoiceLine[] iLines = getLines(false);
				String mysql="Update C_Payment set Custodio='F', C_InvoiceLine_ID="+iLines[0].getC_InvoiceLine_ID()
								+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
								log.config("update fac");
							    int	no = DB.executeUpdate(mysql,get_TrxName());
							    
			    mysql="Update C_Invoice set ISFACTORING='Y', ispaid='Y' "
								+" Where C_Invoice_ID IN (select C_InvoiceFac_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
								log.config("update fac");
							    no = DB.executeUpdate(mysql,get_TrxName());
			}
			else if(!get_ValueAsBoolean("Extinguir") && !get_ValueAsBoolean("PutInFactoring"))
			{
				MInvoiceLine[] iLines = getLines(false);
				String mysql = "";
				int	no = 0;
				String sqlCantRef = "SELECT COUNT(1) FROM AD_Ref_List WHERE AD_Reference_ID=1000006 AND value = 'D'";
				int cantRef = DB.getSQLValue(get_TrxName(), sqlCantRef);
				if (cantRef > 0)
				{
					mysql="Update C_Payment set Custodio='D', C_InvoiceLine_ID="+iLines[0].getC_InvoiceLine_ID()
							+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
							log.config("update fac");
						    no = DB.executeUpdate(mysql,get_TrxName());
				}else
				{
					mysql="Update C_Payment set Custodio=null, C_InvoiceLine_ID="+iLines[0].getC_InvoiceLine_ID()
							+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
							log.config("update fac");
						    no = DB.executeUpdate(mysql,get_TrxName());
				}
							    
			    mysql="Update C_Invoice set ISFACTORING='N', ispaid='N' "
								+" Where C_Invoice_ID IN (select C_InvoiceFac_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
								log.config("update fac");
							    no = DB.executeUpdate(mysql,get_TrxName());
				
			}
			else if(get_ValueAsBoolean("Extinguir") && !get_ValueAsBoolean("PutInFactoring"))
			{
				MInvoiceLine[] iLines = getLines(false);
				String mysql="Update C_Payment set Custodio='E', C_InvoiceLine_ID="+iLines[0].getC_InvoiceLine_ID()
								+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
								log.config("update fac");
							    int	no = DB.executeUpdate(mysql,get_TrxName());
							    
			    mysql="Update C_Invoice set Extinta='Y' "
								+" Where C_Invoice_ID IN (select C_InvoiceFac_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
								log.config("update fac");
							    no = DB.executeUpdate(mysql,get_TrxName());
							    
				mysql="Update C_Invoice set Extinta='Y' "
								+" Where C_Invoice_ID="+getC_Invoice_ID();
									log.config("update fac2");
								    no = DB.executeUpdate(mysql,get_TrxName());
								    
				for(int i=0;i<iLines.length;i++)
				{
					if(iLines[i].get_ValueAsInt("C_InvoiceFac_ID")!=0)
					{
						MInvoice inv=new MInvoice(getCtx(),iLines[i].get_ValueAsInt("C_InvoiceFac_ID"),get_TrxName());
						MBPartner bp = new MBPartner (getCtx(), inv.getC_BPartner_ID(), get_TrxName());
						bp.setTotalOpenBalance();
						bp.save();
					}
				}
			}
		}

	}
	
	/**
	 * faaguilar OFB
	 * si la invoice es un documento de protesto
	 * se reversan los movimientos bancarios relacionados con los pagos
	 * protestados
	 * */
	public boolean reverseBank()
	{
		if (getDocBase().equals("PTK")==false)
			return true;
		boolean result=false;
		String mysql="select p.c_payment_id,p.c_bankaccount_id,p.payamt "+
		"from C_payment p inner join C_Invoiceline il on (p.C_Payment_ID=il.C_Payment_ID) "+
		" and il.C_Invoice_ID=? order by p.c_bankaccount_id";
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(mysql, get_TrxName());
			pstmt.setInt(1,getC_Invoice_ID());
			ResultSet rs = pstmt.executeQuery();
			MBankStatement bankstmt=null;
			int currentaccount=0;
			while (rs.next())
			{	
				if(currentaccount!=rs.getInt(2))
				{
					currentaccount=rs.getInt(2);
					if(bankstmt!=null)
					{	
						bankstmt.setDocAction("CO");
						bankstmt.processIt ("CO");
						bankstmt.save();
					}
						bankstmt=new MBankStatement(getCtx(),0,get_TrxName());
					    bankstmt.setC_BankAccount_ID(rs.getInt(2));
						bankstmt.setName("Reverso por Protesto de Documento:"+ getDocumentNo());
						bankstmt.setDescription("Reverso Protesto");
						bankstmt.set_Value("C_Invoice_ID", getC_Invoice_ID());
						bankstmt.setAD_Org_ID(getAD_Org_ID());
						bankstmt.save();					
				}
				MBankStatementLine line=new MBankStatementLine(bankstmt);
				line.setPayment(new MPayment(getCtx(),rs.getInt(1),get_TrxName()));
				line.setTrxAmt(line.getTrxAmt().negate());
				line.setStmtAmt(line.getStmtAmt().negate());				
				line.setAD_Org_ID(getAD_Org_ID());
				line.save();
			}
			if(currentaccount!=0){
			bankstmt.setDocAction("CO");
			bankstmt.processIt ("CO");
			bankstmt.save();
			}
			result=true;
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, mysql, e);
			return false;
		}
		
		
		
		return result;
	}
	
	/**
	 * faaguilar OFB
	 * custom method*/
	public int getPayments()
	{
		int payments=DB.getSQLValue("C_Payment","select count(1) from C_payment where docstatus<>'VO'"
		+ " and c_invoice_id=" + getC_Invoice_ID());
		
		if(payments<=0)
		payments=DB.getSQLValue("C_Payment","select count(1) from C_allocationLine A where A.C_Payment_ID is not null AND"
		+ " A.c_invoice_id=" + getC_Invoice_ID() +" and 'CO' = (select B.docstatus from C_allocationhdr B where B.c_allocationhdr_ID=A.c_allocationhdr_ID)");
		
		int cashs=0;
		cashs=DB.getSQLValue("C_CashLine","select count(1) from C_CashLine A where "
		+ " A.c_invoice_id=" + getC_Invoice_ID() +" and processed='Y' And Not exists (select * from C_Cash b where a.C_Cash_id=b.C_Cash_ID and b.docstatus='VO')");
		if(cashs>0)
			return 0;
		else
			return payments;
	}
	
	/**
	 * faaguilar OFB
	 * crea xml del documento en formato sii deacuerdo a configuracion*/
	public String CreateXML()
    {
        MDocType doc = new MDocType(getCtx(), getC_DocTypeTarget_ID(), get_TrxName());
        if(doc.get_Value("CreateXML") == null)
            return "";
        if(!((Boolean)doc.get_Value("CreateXML")).booleanValue())
            return "";
        int typeDoc = Integer.parseInt((String)doc.get_Value("DocumentNo"));
        if(typeDoc == 0)
            return "";
        String mylog = new String();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "DTE", null);
            document.setXmlVersion("1.0");
            Element Documento = document.createElement("Documento");
            document.getDocumentElement().appendChild(Documento);
            Documento.setAttribute("ID", (new StringBuilder()).append("DTE-").append(getDocumentNo()).toString());
            Element Encabezado = document.createElement("Encabezado");
            Documento.appendChild(Encabezado);
            Element IdDoc = document.createElement("IdDoc");
            Encabezado.appendChild(IdDoc);
            mylog = "IdDoc";
            Element TipoDTE = document.createElement("TipoDTE");
            org.w3c.dom.Text text = document.createTextNode(Integer.toString(typeDoc));
            TipoDTE.appendChild(text);
            IdDoc.appendChild(TipoDTE);
            Element Folio = document.createElement("Folio");
            org.w3c.dom.Text fo = document.createTextNode(getDocumentNo());
            Folio.appendChild(fo);
            IdDoc.appendChild(Folio);
            Element FchEmis = document.createElement("FchEmis");
            org.w3c.dom.Text emis = document.createTextNode(getDateInvoiced().toString().substring(0, 10));
            FchEmis.appendChild(emis);
            IdDoc.appendChild(FchEmis);
            Element FchCancel = document.createElement("FchCancel");
            org.w3c.dom.Text cancel = document.createTextNode(getDateInvoiced().toString().substring(0, 10));
            FchCancel.appendChild(cancel);
            IdDoc.appendChild(FchCancel);
            Element FchVenc = document.createElement("FchVenc");
            org.w3c.dom.Text venc = document.createTextNode(getDateInvoiced().toString().substring(0, 10));
            FchVenc.appendChild(venc);
            IdDoc.appendChild(FchVenc);
            
            //ininoles nuevo campo termino de pago
            MPaymentTerm pterm = new MPaymentTerm(getCtx(), getC_PaymentTerm_ID(), get_TrxName());
            Element PayTerm = document.createElement("PayTerm");
            org.w3c.dom.Text term = document.createTextNode(pterm.getName());
            PayTerm.appendChild(term);
            IdDoc.appendChild(PayTerm);            
            //ininoles nuevo campo vendedor            
            MUser salesUser = new MUser(getCtx(), getSalesRep_ID(),get_TrxName());
            Element SalesRep = document.createElement("SalesRep");
            org.w3c.dom.Text sales = document.createTextNode(salesUser.getName());
            SalesRep.appendChild(sales);
            IdDoc.appendChild(SalesRep);
            //ininoles nuevo campo descripcion cabecera                        
            Element HDescription = document.createElement("HeaderDescription");
            org.w3c.dom.Text Hdesc = document.createTextNode(getDescription()==null?" ":getDescription());
            HDescription.appendChild(Hdesc);
            IdDoc.appendChild(HDescription);
            //end ininoles
                        
            Element Emisor = document.createElement("Emisor");
            Encabezado.appendChild(Emisor);
            mylog = "Emisor";
            MOrg company = MOrg.get(getCtx(), getAD_Org_ID());
            Element Rut = document.createElement("RUTEmisor");
            org.w3c.dom.Text rut = document.createTextNode((String)company.get_Value("Rut"));
            Rut.appendChild(rut);
            Emisor.appendChild(Rut);
            //ininoles validacion nuevo nombre razon social
            String nameRzn = company.getDescription();
            if (nameRzn == null)
            {
            	nameRzn = " ";
            }
            nameRzn = nameRzn.trim();
            if (nameRzn.length() < 2)
            	nameRzn = company.getName();
            //ininoles end            
            Element RznSoc = document.createElement("RznSoc");
            org.w3c.dom.Text rzn = document.createTextNode(nameRzn);
            RznSoc.appendChild(rzn);
            Emisor.appendChild(RznSoc);
            Element GiroEmis = document.createElement("GiroEmis");
            org.w3c.dom.Text gi = document.createTextNode((String)company.get_Value("Giro"));
            GiroEmis.appendChild(gi);
            Emisor.appendChild(GiroEmis);
            Element Acteco = document.createElement("Acteco");
            org.w3c.dom.Text teco = document.createTextNode((String)company.get_Value("Acteco"));
            Acteco.appendChild(teco);
            Emisor.appendChild(Acteco);
            Element DirOrigen = document.createElement("DirOrigen");
            org.w3c.dom.Text dir = document.createTextNode((String)company.get_Value("Address1"));
            DirOrigen.appendChild(dir);
            Emisor.appendChild(DirOrigen);
            
            Element CmnaOrigen = document.createElement("CmnaOrigen");
            org.w3c.dom.Text com = document.createTextNode((String)company.get_Value("Comuna"));
            CmnaOrigen.appendChild(com);
            Emisor.appendChild(CmnaOrigen);
            Element CiudadOrigen = document.createElement("CiudadOrigen");
            org.w3c.dom.Text city = document.createTextNode((String)company.get_Value("City"));
            CiudadOrigen.appendChild(city);
            Emisor.appendChild(CiudadOrigen);
            mylog = "receptor";
            MBPartner BP = new MBPartner(getCtx(), getC_BPartner_ID(), get_TrxName());
            MBPartnerLocation bloc = new MBPartnerLocation(getCtx(), getC_BPartner_Location_ID(), get_TrxName());
            Element Receptor = document.createElement("Receptor");
            Encabezado.appendChild(Receptor);
            Element RUTRecep = document.createElement("RUTRecep");
            org.w3c.dom.Text rutc = document.createTextNode((new StringBuilder()).append(BP.getValue()).append("-").append(BP.get_ValueAsString("Digito")).toString());
            RUTRecep.appendChild(rutc);
            Receptor.appendChild(RUTRecep);
            Element RznSocRecep = document.createElement("RznSocRecep");
            org.w3c.dom.Text RznSocR = document.createTextNode(BP.getName());
            RznSocRecep.appendChild(RznSocR);
            Receptor.appendChild(RznSocRecep);
            Element GiroRecep = document.createElement("GiroRecep");
            org.w3c.dom.Text giro = document.createTextNode((String)BP.get_Value("Giro"));
            GiroRecep.appendChild(giro);
            Receptor.appendChild(GiroRecep);
            
            Element ContactoRecep = document.createElement("Contacto");
            org.w3c.dom.Text contacto = document.createTextNode(getAD_User_ID()>0?this.getAD_User().getName():" "); //nombre completo contacto
            ContactoRecep.appendChild(contacto);
            Receptor.appendChild(ContactoRecep);
            
            Element CorreoRecep = document.createElement("CorreoRecep");
            org.w3c.dom.Text corrRecep = document.createTextNode(this.getAD_User().getEMail()==null?" ":this.getAD_User().getEMail()); //mail del contacto
            CorreoRecep.appendChild(corrRecep);
            Receptor.appendChild(CorreoRecep);
            
            
            Element DirRecep = document.createElement("DirRecep");
            org.w3c.dom.Text dirr = document.createTextNode(bloc.getLocation(true).getAddress1());
            DirRecep.appendChild(dirr);
            Receptor.appendChild(DirRecep);
            if(bloc.getLocation(true).getAddress2()!=null && bloc.getLocation(true).getAddress2().length()>0 ){
	            Element CmnaRecep = document.createElement("CmnaRecep");
	            org.w3c.dom.Text Cmna = document.createTextNode(bloc.getLocation(true).getAddress2());
	            CmnaRecep.appendChild(Cmna);
	            Receptor.appendChild(CmnaRecep);
            }
            
            Element CiudadRecep = document.createElement("CiudadRecep");
            org.w3c.dom.Text reg = document.createTextNode(bloc.getLocation(true).getC_City_ID()>0?MCity.get(getCtx(), bloc.getLocation(true).getC_City_ID()).getName():"Santiago");
            CiudadRecep.appendChild(reg);
            Receptor.appendChild(CiudadRecep);
            
            mylog = "Totales";
            Element Totales = document.createElement("Totales");
            Encabezado.appendChild(Totales);
            BigDecimal amountex = DB.getSQLValueBD(get_TrxName(), (new StringBuilder()).append("select Round(COALESCE(SUM(il.LineNetAmt),0),0) from C_InvoiceLine il  inner join C_Tax t on (il.C_Tax_ID=t.C_Tax_ID) and t.istaxexempt='Y' and il.C_Invoice_ID=").append(getC_Invoice_ID()).toString());
            BigDecimal amountNeto = DB.getSQLValueBD(get_TrxName(), (new StringBuilder()).append("select Round(COALESCE(SUM(il.LineNetAmt),0),0) from C_InvoiceLine il  inner join C_Tax t on (il.C_Tax_ID=t.C_Tax_ID) and t.istaxexempt='N' and il.C_Invoice_ID=").append(getC_Invoice_ID()).toString());
            
            
	        Element MntNeto = document.createElement("MntNeto");
	        org.w3c.dom.Text neto = document.createTextNode(amountNeto!=null?amountNeto.toString():"0");
	        MntNeto.appendChild(neto);
	        Totales.appendChild(MntNeto);
            
            
            Element MntExe = document.createElement("MntExe");
            org.w3c.dom.Text exe = document.createTextNode(amountex != null ? amountex.toString() : "0");
            MntExe.appendChild(exe);
            Totales.appendChild(MntExe);
            
            if(amountNeto.signum()>0){
	            Element TasaIVA = document.createElement("TasaIVA");
	            org.w3c.dom.Text tiva = document.createTextNode("19");
	            TasaIVA.appendChild(tiva);
	            Totales.appendChild(TasaIVA);
	        }
	        
	            
	            Element IVA = document.createElement("IVA");
	            BigDecimal ivaamt= Env.ZERO;
	            if(amountex.intValue()!=getGrandTotal().intValue())
	            	ivaamt=getGrandTotal().subtract(getTotalLines()).setScale(0, 4);
	            org.w3c.dom.Text iva = document.createTextNode(ivaamt.toString());
	            IVA.appendChild(iva);
	            Totales.appendChild(IVA);
            
            Element MntTotal = document.createElement("MntTotal");
            org.w3c.dom.Text total = document.createTextNode(getGrandTotal().setScale(0, 4).toString());
            MntTotal.appendChild(total);
            Totales.appendChild(MntTotal);
            /*Element MontoPeriodo = document.createElement("MontoPeriodo");
            org.w3c.dom.Text mnt = document.createTextNode(getTotalLines().setScale(0, 4).toString());
            MontoPeriodo.appendChild(mnt);
            Totales.appendChild(MontoPeriodo);*/
            mylog = "detalle";
            MInvoiceLine iLines[] = getLines(false);
            for(int i = 0; i < iLines.length; i++)
            {
            	MInvoiceLine iLine = iLines[i];
            	if(iLine.getM_Product_ID()==0 && iLine.getC_Charge_ID()==0)
            		continue;
            	
                Element Detalle = document.createElement("Detalle");
                Documento.appendChild(Detalle);
                
                MTax tax=new MTax(getCtx() ,iLine.getC_Tax_ID(),get_TrxName() );
                
                if(tax.isTaxExempt()){
                	 Element IndEx = document.createElement("IndExe");
                     org.w3c.dom.Text lineE = document.createTextNode("1");
                     IndEx.appendChild(lineE);
                     Detalle.appendChild(IndEx);	
                }
              
                Element NroLinDet = document.createElement("NroLinDet");
                org.w3c.dom.Text line = document.createTextNode(Integer.toString(iLine.getLine() / 10));
                NroLinDet.appendChild(line);
                Detalle.appendChild(NroLinDet);
                Element NmbItem = document.createElement("NmbItem");
                String pname="";
                if(iLine.getProduct()!=null )
                	pname=iLine.getProduct().getName();
                else
                	pname=iLine.getC_Charge().getName();
                org.w3c.dom.Text Item = document.createTextNode(pname);
                NmbItem.appendChild(Item);
                Detalle.appendChild(NmbItem);
                
                Element DscItem = document.createElement("DscItem");
                org.w3c.dom.Text desc = document.createTextNode(iLine.getDescription()==null?" ":iLine.getDescription());
                DscItem.appendChild(desc);
                Detalle.appendChild(DscItem);
                
                Element QtyRef = document.createElement("QtyRef");
                org.w3c.dom.Text qty = document.createTextNode(iLine.getQtyEntered().toString());
                QtyRef.appendChild(qty);
                Detalle.appendChild(QtyRef);
                Element PrcRef = document.createElement("PrcRef");
                org.w3c.dom.Text pl = document.createTextNode(iLine.getPriceList().toString());
                PrcRef.appendChild(pl);
                Detalle.appendChild(PrcRef);
                Element QtyItem = document.createElement("QtyItem");
                org.w3c.dom.Text qt = document.createTextNode(iLine.getQtyInvoiced().toString());
                QtyItem.appendChild(qt);
                Detalle.appendChild(QtyItem);
                Element PrcItem = document.createElement("PrcItem");
                org.w3c.dom.Text pa = document.createTextNode(iLine.getPriceActual().setScale(0, 4).toString());
                PrcItem.appendChild(pa);
                Detalle.appendChild(PrcItem);
                Element MontoItem = document.createElement("MontoItem");
                org.w3c.dom.Text tl = document.createTextNode(iLine.getLineNetAmt().setScale(0, 4).toString());
                MontoItem.appendChild(tl);
                Detalle.appendChild(MontoItem);
            }

            mylog = "referencia";
            String tiporeferencia = new String();
            String folioreferencia  = new String();
            String fechareferencia = new String();
            int tipo_Ref =0;
            
            if(get_Value("C_RefDoc_ID") != null && ((Integer)get_Value("C_RefDoc_ID")).intValue() > 0)//referencia factura
            {
                mylog = "referencia:invoice";
                MInvoice refdoc = new MInvoice(getCtx(), ((Integer)get_Value("C_RefDoc_ID")).intValue(), get_TrxName());
                MDocType Refdoctype = new MDocType(getCtx(), refdoc.getC_DocType_ID(), get_TrxName());
                tiporeferencia = (String) Refdoctype.get_Value("DocumentNo");
                folioreferencia = (String) refdoc.getDocumentNo();
                fechareferencia = refdoc.getDateInvoiced().toString().substring(0, 10);
                tipo_Ref = 1; //factura
            } 
            
            if(getPOReference() != null && getPOReference().length() > 0)//referencia orden
            {
            	 mylog = "referencia:order";
            	 //MOrder refdoc = new MOrder(getCtx(), ((Integer)get_Value("C_RefOrder_ID")).intValue(), get_TrxName()); 
            	 tiporeferencia = "801";
                 folioreferencia = getPOReference();
                 fechareferencia = this.getDateOrdered().toString().substring(0, 10);
            	 tipo_Ref = 2; //Orden
            }
            
            if(get_Value("C_RefInOut_ID") != null && ((Integer)get_Value("C_RefInOut_ID")).intValue() > 0)//referencia despacho
            {
            	 mylog = "referencia:despacho";
            	 MInOut refdoc = new MInOut(getCtx(), ((Integer)get_Value("C_RefInOut_ID")).intValue(), get_TrxName()); 
            	 tiporeferencia = "52";
                 folioreferencia = (String) refdoc.getDocumentNo();
                 fechareferencia = refdoc.getMovementDate().toString().substring(0, 10);
            	 tipo_Ref = 3; //despacho
            }
            if(get_ValueAsString("POReference2") != null && get_ValueAsString("POReference2").length() > 0)//referencia 802
            {
            	 mylog = "referencia:802";
            	 //MOrder refdoc = new MOrder(invoice.getCtx(), ((Integer)get_Value("C_RefOrder_ID")).intValue(), invoice.get_TrxName()); 
            	 tiporeferencia = "802";
                 folioreferencia = get_ValueAsString("POReference2");
                 fechareferencia = getDateOrdered().toString().substring(0, 10);
            	 tipo_Ref = 4; //802
            }
            if(tipo_Ref>0){
                Element Referencia = document.createElement("Referencia");
                Documento.appendChild(Referencia);
                Element NroLinRef = document.createElement("NroLinRef");
                org.w3c.dom.Text Nro = document.createTextNode("1");
                NroLinRef.appendChild(Nro);
                Referencia.appendChild(NroLinRef);
                Element TpoDocRef = document.createElement("TpoDocRef");
                org.w3c.dom.Text tpo = document.createTextNode(tiporeferencia);
                TpoDocRef.appendChild(tpo);
                Referencia.appendChild(TpoDocRef);
                Element FolioRef = document.createElement("FolioRef");
                org.w3c.dom.Text ref = document.createTextNode(folioreferencia);
                FolioRef.appendChild(ref);
                Referencia.appendChild(FolioRef);
               /* Element RUTOtr = document.createElement("RUTOtr");
                org.w3c.dom.Text rutot = document.createTextNode(BPref.getValue()+"-"+BPref.getDigito());
                RUTOtr.appendChild(rutot);
                Referencia.appendChild(RUTOtr);*/
                
                if(fechareferencia == null)
                	fechareferencia = getDateInvoiced().toString().substring(0, 10);
                if(fechareferencia.compareTo(" ") == 0 || fechareferencia.compareTo("") == 0)
                	fechareferencia = getDateInvoiced().toString().substring(0, 10);
                if(fechareferencia.length()<2)
                	fechareferencia = getDateInvoiced().toString().substring(0, 10);                
                
                Element FchRef = document.createElement("FchRef");
                org.w3c.dom.Text fchref = document.createTextNode(fechareferencia);
                FchRef.appendChild(fchref);
                Referencia.appendChild(FchRef);
                Element CodRef = document.createElement("CodRef");
                org.w3c.dom.Text codref = document.createTextNode(this.get_ValueAsString("CodRef"));
                CodRef.appendChild(codref);
                Referencia.appendChild(CodRef);
                
                //nuevos campos de referencia geminis. ininoles
                /*
                String sqlDN = "Select o.DocumentNo FROM C_Order o INNER JOIN C_Invoice i ON (o.C_Order_ID = i.C_Order_ID) WHERE i.C_Invoice_ID= ?";
                String docNoRef = DB.getSQLValueString(get_TrxName(), sqlDN, get_ID()); 
                
                if (docNoRef != null)
                {
                	Element Referencia2 = document.createElement("Referencia");
                    Documento.appendChild(Referencia2);
                    Element NroLinRef2 = document.createElement("NroLinRef");
                    org.w3c.dom.Text Nro2 = document.createTextNode("2");
                    NroLinRef2.appendChild(Nro2);
                    Referencia2.appendChild(NroLinRef2);
                    
                    Element TpoDocRef2 = document.createElement("TpoDocRef");
                    org.w3c.dom.Text tpo2 = document.createTextNode("HES");
                    TpoDocRef2.appendChild(tpo2);
                    Referencia2.appendChild(TpoDocRef2);
                    
                    Element FolioRef2 = document.createElement("FolioRef");
                    org.w3c.dom.Text ref2 = document.createTextNode(docNoRef);
                    FolioRef2.appendChild(ref2);
                    Referencia2.appendChild(FolioRef2);
                                      
                    Element FchRef2 = document.createElement("FchRef");
                    org.w3c.dom.Text fchref2 = document.createTextNode(fechareferencia);
                    FchRef2.appendChild(fchref2);
                    Referencia2.appendChild(FchRef2);
                    
                    Element CodRef2 = document.createElement("CodRef");
                    org.w3c.dom.Text codref2 = document.createTextNode(get_ValueAsString("CodRef"));
                    CodRef2.appendChild(codref2);
                    Referencia2.appendChild(CodRef2);
                    
                }*/
            }
            //fin referencia
            mylog = "firma";
            Element Firma = document.createElement("TmstFirma");
            Timestamp today = new Timestamp(TimeUtil.getToday().getTimeInMillis());
            org.w3c.dom.Text Ftext = document.createTextNode((new StringBuilder()).append(today.toString().substring(0, 10)).append("T").append(today.toString().substring(11, 19)).toString());
            Firma.appendChild(Ftext);
            Documento.appendChild(Firma);
            mylog = "archivo";
           
            String ExportDir = (String)company.get_Value("ExportDir");
  	        try
  	          {
  	        	  File theDir = new File(ExportDir);
  	        	  if (!theDir.exists())
  	        		  ExportDir = (String)company.get_Value("ExportDir2"); 
  	          }
  	        catch(Exception e)
  	          {
  	        	  throw new AdempiereException("no existe directorio");
  	          }
	            ExportDir = ExportDir.replace("\\", "/");
	            javax.xml.transform.Source source = new DOMSource(document);
	            javax.xml.transform.Result result = new StreamResult(new File(ExportDir, (new StringBuilder()).append(getDocumentNo()).append(".xml").toString()));
	            javax.xml.transform.Result console = new StreamResult(System.out);
	            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.setOutputProperty("indent", "yes");
	            transformer.setOutputProperty("encoding", "ISO-8859-1");
	            transformer.transform(source, result);
	            transformer.transform(source, console);
           
        }
        catch(Exception e)
        {
            log.severe((new StringBuilder()).append("CreateXML: ").append(mylog).append("--").append(e.getMessage()).toString());
            return (new StringBuilder()).append("CreateXML: ").append(mylog).append("--").append(e.getMessage()).toString();
        }
        return "XML Generated";
    }
	
	/**faaguilar OFB
	 * busca si el documento protesto posee movimientos bancarios de reversa*/
	public boolean isBankReverse()
	{
		int banks=DB.getSQLValue(get_TrxName(),"select count(1) from c_bankstatement where docstatus<>'VO'"
		+ " and c_invoice_id=" + getC_Invoice_ID());
		
		
		if(banks>0)
			return true;
		else
			return false;
	}
	
	/**faaguilar OFB
	 * movimiento de reversa si el documento es uno de los doc especiales PTK, CDC,FAT*/
	public void voidSpecialDocs(){
		
		if(getDocBase().equals("PTK"))
		{			
				String mysql="Update C_Payment set IsProtested='N', C_InvoiceLine_ID=null "
						+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
					    int	no = DB.executeUpdate(mysql,get_TrxName());
		}
		else if(getDocBase().equals("CDC") || getDocBase().equals("VDC"))
		{
			    MInvoiceLine[] iLines = getLines(false);
				String mysql="Update C_Payment set Custodio=null, C_InvoiceLine_ID="+iLines[0].getC_InvoiceLine_ID()
							+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
							log.config("update cdc vdc");
						    int	no = DB.executeUpdate(mysql,get_TrxName());
				//actualizacion de facturas
				String mysqlIn="Update C_Invoice set IsPaid = 'N' Where C_Invoice_ID IN (select C_Invoicefac_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
							log.config("update cdc vdc");
						    DB.executeUpdate(mysqlIn,get_TrxName());
			
			
		}
		else if(getDocBase().equals("FAT") && isSOTrx ())
		{
			MInvoiceLine[] iLines = getLines(false);
			String mysql="Update C_Payment set Custodio=null, C_InvoiceLine_ID="+iLines[0].getC_InvoiceLine_ID()
							+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
							log.config("update fac");
						    int	no = DB.executeUpdate(mysql,get_TrxName());
						    
		    mysql="Update C_Invoice set ISFACTORING='N', ispaid='N' "
							+" Where C_Invoice_ID IN (select C_InvoiceFac_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
							log.config("update fac");
						    no = DB.executeUpdate(mysql,get_TrxName());
		}
		else if(getDocBase().equals("FAT") && !isSOTrx ()){
			MInvoiceLine[] iLines = getLines(false);
			String mysql="Update C_Payment set Custodio='F', C_InvoiceLine_ID="+iLines[0].getC_InvoiceLine_ID()
							+" Where C_Payment_ID IN (select C_Payment_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
							log.config("update fac");
						    int	no = DB.executeUpdate(mysql,get_TrxName());
						    
		    mysql="Update C_Invoice set ISFACTORING='Y' "
							+" Where C_Invoice_ID IN (select C_InvoiceFac_ID from C_InvoiceLine where C_Invoice_ID="+getC_Invoice_ID()+")";
							log.config("update fac");
						    no = DB.executeUpdate(mysql,get_TrxName());
		}
	}

	/**faaguilar OFB
	 * deja en 0 la linea de un cashbook al anular la factura si la factura fue pagada en efectivo*/
	public boolean voidCashBook()
	{
		int cash_id =DB.getSQLValue(get_TrxName(),"select c_cash_id from c_cashline where c_invoice_id="+getC_Invoice_ID());
		if(cash_id>0)
		{
			MCash mycash= new MCash(getCtx(),cash_id,get_TrxName());
			if(mycash.getDocStatus().equals("DR"))
			{
				MCashLine[] lines =mycash.getLines(true);
				for (int i=0;i<lines.length;i++)
					if(lines[i].getC_Invoice_ID()==getC_Invoice_ID())
					{
						lines[i].setAmount(Env.ZERO);
						lines[i].setDescription("**Anulada");
						lines[i].save();
					}
						
			}
			else if(mycash.getDocStatus().equals("CO"))
			{
				MCash currentCash= MCash.getDefault(getCtx(), getAD_Client_ID(), mycash.getC_CashBook_ID(), get_TrxName());
				if(currentCash==null)
				{
					m_processMsg = "@No existe un libro de efectivo Activo Actualmente@";
					return false;
				}
				
				MCashLine[] lines =mycash.getLines(true);
				for (int i=0;i<lines.length;i++)
					if(lines[i].getC_Invoice_ID()==getC_Invoice_ID())
					{
						MCashLine newline=new MCashLine(currentCash);
						newline.setC_Invoice_ID(lines[i].getC_Invoice_ID());
						newline.setDescription("Descuento por Anulacion Factura:"+getDocumentNo());
						newline.setCashType(lines[i].getCashType());
						newline.setAmount(lines[i].getAmount().negate());
						if(!newline.save())
							m_processMsg = "@No se puede descontar la Factura en la caja actual@";
					}
			}
		}
		return true;
	}
	
	/**
	 * faaguilar OFB
	 * retorna el tendertype para los documentos de protesto*/
	public String getTenderType()
	{
		MInvoiceLine[] lines = getLines(false);
		String tendertype="K";
		for (int i = 0; i < lines.length; i++)
		{
			MInvoiceLine line = lines[i];
			if(line.get_ValueAsInt("C_Payment_ID")>0)
			{	
		    	MPayment pay = new MPayment(getCtx(), line.get_ValueAsInt("C_Payment_ID"), get_TrxName());
		    	tendertype=pay.getTenderType();
		    }	
	    }
	    
		return tendertype;
	}
	
	/**
	 * faaguilar OFB
	 * reverso costo al anular*/
	public void resetCost(){
		
		if(isSOTrx())
			return;
		
		MAcctSchema as = MAcctSchema.getClientAcctSchema(getCtx(), getAD_Client_ID())[0];
		MInvoiceLine[] iLines = getLines(false);
		for (int i = 0; i < iLines.length; i++)
		{
			MInvoiceLine iLine = iLines[i];
		
			//revision de landed costs
			/*MLandedCostAllocation[] lcas = MLandedCostAllocation.getOfInvoiceLine(
					getCtx(), iLine.getC_InvoiceLine_ID(), get_TrxName());
			
			if(lcas.length>0)
				for (int x = 0; x < lcas.length; x++)//es landed cost la linea
				{
					MLandedCostAllocation lca = lcas[x];
					if (lca.getBase().signum() == 0)
						continue;
					
	//				Cost Detail - Convert to AcctCurrency
					BigDecimal allocationAmt =  lca.getAmt();
					if (getC_Currency_ID() != as.getC_Currency_ID())
						allocationAmt = MConversionRate.convert(getCtx(), allocationAmt, 
							getC_Currency_ID(), as.getC_Currency_ID(),
							getDateAcct(), getC_ConversionType_ID(), 
							getAD_Client_ID(), getAD_Org_ID());
					if (allocationAmt.scale() > as.getCostingPrecision())
						allocationAmt = allocationAmt.setScale(as.getCostingPrecision(), BigDecimal.ROUND_HALF_UP);
					
					
					allocationAmt = allocationAmt.negate();
					
					BigDecimal qty = Env.ZERO;
					OFBProductCost.createInvoice(as, lca.getAD_Org_ID(), 
							lca.getM_Product_ID(), lca.getM_AttributeSetInstance_ID(),
							lca.getC_InvoiceLine_ID(), lca.getM_CostElement_ID(),
							allocationAmt, qty, "Anulacion Factura", get_TrxName());
				}
			else{*/
			MCostDetail cd = OFBProductCost.get (as.getCtx(), "C_InvoiceLine_ID=?", 
					iLine.getC_InvoiceLine_ID(), iLine.getM_AttributeSetInstance_ID(), as.getC_AcctSchema_ID(), get_TrxName());
			
			if (cd != null )//posee detalle de costo la factura
			{
				
				if(cd.isProcessed())
					OFBProductCost.createInvoice(as, cd.getAD_Org_ID(), 
							cd.getM_Product_ID(), cd.getM_AttributeSetInstance_ID(),
							cd.getC_InvoiceLine_ID(), cd.getM_CostElement_ID(),
							cd.getAmt().negate(), Env.ZERO, "Anulacion Factura", get_TrxName());
				else{
					String sql = "DELETE From M_CostDetail "
						+ "WHERE Processed='N' "
						+ " AND C_InvoiceLine_ID=" + iLine.getC_InvoiceLine_ID()
						+ " AND C_AcctSchema_ID =" + as.getC_AcctSchema_ID();			
						
					int no = DB.executeUpdate(sql, this.get_TrxName());
					if (no != 0)
						log.config("Deleted #" + no);
				}
					
			}
				
			//}
			
		}
	}//reset cost
	
	
	/**
	 * faaguilar OFB
	 * update asset for charge TCAF*/
	public void updateAsset(){
		
	  if(isSOTrx())
		  return;
		
		
		MInvoiceLine[] lines = getLines(false);
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++)
		{
			MInvoiceLine sLine = lines[lineIndex];
			boolean createdUpdated = false;
			if(isAsset(sLine))
			{	
				if(sLine.get_ValueAsInt("A_Asset_Group_ID")>0 && sLine.get_ValueAsString("A_CapvsExp").equals(X_C_InvoiceLine.A_CAPVSEXP_Capital))
				{						
					int gacct_id = DB.getSQLValue(get_TableName(), "select a_asset_group_acct_id from a_asset_group_acct where a_asset_group_id="+sLine.get_ValueAsInt("A_Asset_Group_ID"));
					MAssetGroupAcct assetgrpacct = new MAssetGroupAcct (getCtx(), gacct_id, get_TrxName());
						
					MAsset asset = new MAsset(getCtx(),  sLine.getA_Asset_ID() ,get_TrxName());
					asset.setA_Asset_Group_ID(sLine.get_ValueAsInt("A_Asset_Group_ID"));
					asset.setUseLifeYears(assetgrpacct.getUseLifeYears());
					asset.setUseLifeMonths(assetgrpacct.getUseLifeMonths());
					asset.setIsDepreciated(true);
					asset.setIsOwned(true);
					asset.saveEx();
				
					int acct_id = DB.getSQLValue(get_TableName(), "select A_Asset_Acct_ID from A_Asset_Acct where A_Asset_ID="+asset.getA_Asset_ID());
					MAssetAcct assetacct = new MAssetAcct (getCtx(), acct_id, get_TrxName());
					assetacct.setPostingType(assetgrpacct.getPostingType());
					assetacct.setA_Split_Percent(assetgrpacct.getA_Split_Percent());
					assetacct.setA_Depreciation_Conv_ID(assetgrpacct.getConventionType());
					assetacct.setA_Depreciation_ID(assetgrpacct.getDepreciationType());
					assetacct.setA_Asset_Spread_ID(assetgrpacct.getA_Asset_Spread_Type());
					assetacct.setA_Period_Start(1);
					assetacct.setA_Period_End(assetgrpacct.getUseLifeMonths());
					assetacct.setA_Depreciation_Method_ID(assetgrpacct.getA_Depreciation_Calc_Type());
					assetacct.setA_Asset_Acct(assetgrpacct.getA_Asset_Acct());
					assetacct.setC_AcctSchema_ID(assetgrpacct.getC_AcctSchema_ID());
					assetacct.setA_Accumdepreciation_Acct(assetgrpacct.getA_Accumdepreciation_Acct());
					assetacct.setA_Depreciation_Acct(assetgrpacct.getA_Depreciation_Acct());
					assetacct.setA_Disposal_Revenue(assetgrpacct.getA_Disposal_Revenue());
					assetacct.setA_Disposal_Loss(assetgrpacct.getA_Disposal_Loss());
					assetacct.setA_Reval_Accumdep_Offset_Cur(assetgrpacct.getA_Reval_Accumdep_Offset_Cur());
					assetacct.setA_Reval_Accumdep_Offset_Prior(assetgrpacct.getA_Reval_Accumdep_Offset_Prior());
					assetacct.setA_Reval_Cost_Offset(assetgrpacct.getA_Reval_Cost_Offset());
					assetacct.setA_Reval_Cost_Offset_Prior(assetgrpacct.getA_Reval_Cost_Offset_Prior());
					assetacct.setA_Reval_Depexp_Offset(assetgrpacct.getA_Reval_Depexp_Offset());
					assetacct.setA_Depreciation_Manual_Amount(assetgrpacct.getA_Depreciation_Manual_Amount());
					assetacct.setA_Depreciation_Manual_Period(assetgrpacct.getA_Depreciation_Manual_Period());
					assetacct.setA_Depreciation_Table_Header_ID(assetgrpacct.getA_Depreciation_Table_Header_ID());
					assetacct.setA_Depreciation_Variable_Perc(assetgrpacct.getA_Depreciation_Variable_Perc());
					assetacct.setProcessing(false);
					//assetacct.set_Value("A_Salvage_Value",assetgrpacct.get_ValueAsInt("A_Salvage_Value")); 
					assetacct.set_Value("A_Salvage_Value",assetgrpacct.get_Value("A_Salvage_Value"));//ininoles camnio a bigdecimal para evitar error
					assetacct.set_Value("A_Disposal_RevenueD_Acct",assetgrpacct.get_ValueAsInt("A_Disposal_RevenueD_Acct"));
					assetacct.set_Value("A_Disposal_RevenueC_Acct",assetgrpacct.get_ValueAsInt("A_Disposal_RevenueC_Acct"));
					assetacct.set_Value("A_Disposal_Loss_Acct",assetgrpacct.get_ValueAsInt("A_Disposal_Loss_Acct"));
					assetacct.set_Value("A_Disposal_Gain_Acct",assetgrpacct.get_ValueAsInt("A_Disposal_Gain_Acct"));
					assetacct.set_Value("A_AssetComplement_Acct",assetgrpacct.get_ValueAsInt("A_AssetComplement_Acct"));
					assetacct.save();
					
					log.config("MAssetChange change");
					int change_id = DB.getSQLValue(get_TableName(), "select A_Asset_Change_ID from A_Asset_Change where A_Asset_ID="+asset.getA_Asset_ID());
					MAssetChange change = new MAssetChange (getCtx(), change_id, get_TrxName());						
					change.setPostingType(assetacct.getPostingType());
					change.setA_Split_Percent(assetacct.getA_Split_Percent());
					change.setConventionType(assetacct.getA_Depreciation_Conv_ID());
					//change.setA_Asset_ID(asset.getA_Asset_ID());								
					change.setDepreciationType(assetacct.getA_Depreciation_ID());
					change.setA_Asset_Spread_Type(assetacct.getA_Asset_Spread_ID());
					change.setA_Period_Start(assetacct.getA_Period_Start());
					change.setA_Period_End(assetacct.getA_Period_End());
					change.setIsInPosession(asset.isOwned());
					change.setIsDisposed(asset.isDisposed());
					change.setIsDepreciated(asset.isDepreciated());
					change.setIsFullyDepreciated(asset.isFullyDepreciated());					
					change.setA_Depreciation_Calc_Type(assetacct.getA_Depreciation_Method_ID());
					change.setA_Asset_Acct(assetacct.getA_Asset_Acct());
					change.setC_AcctSchema_ID(assetacct.getC_AcctSchema_ID());
					change.setA_Accumdepreciation_Acct(assetacct.getA_Accumdepreciation_Acct());
					change.setA_Depreciation_Acct(assetacct.getA_Depreciation_Acct());
					change.setA_Disposal_Revenue(assetacct.getA_Disposal_Revenue());
					change.setA_Disposal_Loss(assetacct.getA_Disposal_Loss());
					change.setA_Reval_Accumdep_Offset_Cur(assetacct.getA_Reval_Accumdep_Offset_Cur());
					change.setA_Reval_Accumdep_Offset_Prior(assetacct.getA_Reval_Accumdep_Offset_Prior());
					change.setA_Reval_Cal_Method(assetacct.getA_Reval_Cal_Method());
					change.setA_Reval_Cost_Offset(assetacct.getA_Reval_Cost_Offset());
					change.setA_Reval_Cost_Offset_Prior(assetacct.getA_Reval_Cost_Offset_Prior());
					change.setA_Reval_Depexp_Offset(assetacct.getA_Reval_Depexp_Offset());
					change.setA_Depreciation_Manual_Amount(assetacct.getA_Depreciation_Manual_Amount());
					change.setA_Depreciation_Manual_Period(assetacct.getA_Depreciation_Manual_Period());
					change.setA_Depreciation_Table_Header_ID(assetacct.getA_Depreciation_Table_Header_ID());
					change.setA_Depreciation_Variable_Perc(assetacct.getA_Depreciation_Variable_Perc());
					change.setA_Parent_Asset_ID(asset.getA_Parent_Asset_ID());
				    change.setChangeType("CRT");	
					change.setTextDetails(MRefList.getListDescription (getCtx(),"A_Update_Type" , "CRT"));		    
				    change.setIsInPosession(asset.isOwned());
					change.setIsDisposed(asset.isDisposed());
					change.setIsDepreciated(asset.isDepreciated());
					change.setIsFullyDepreciated(asset.isFullyDepreciated());
					change.setLot(asset.getLot());
					change.setSerNo(asset.getSerNo());
					change.setVersionNo(asset.getVersionNo());
				    change.setUseLifeMonths(asset.getUseLifeMonths());
				    change.setUseLifeYears(asset.getUseLifeYears());
				    change.setLifeUseUnits(asset.getLifeUseUnits());
				    change.setAssetDisposalDate(asset.getAssetDisposalDate());
				    change.setAssetServiceDate(asset.getAssetServiceDate());
				    change.setC_BPartner_Location_ID(asset.getC_BPartner_Location_ID());
				    change.setC_BPartner_ID(asset.getC_BPartner_ID());
				    change.setAssetValueAmt(sLine.getLineTotalAmt());
				    change.setA_QTY_Current(sLine.getQtyEntered());
				    change.setA_QTY_Original(sLine.getQtyEntered());
				    change.setA_Asset_CreateDate(asset.getA_Asset_CreateDate());
				    change.setAD_User_ID(asset.getAD_User_ID());
				    change.setC_Location_ID(asset.getC_Location_ID());
				    //faaguilar OFB begin
				    change.setAssetValueAmt(sLine.getLineNetAmt().divide(sLine.getQtyEntered(), BigDecimal.ROUND_HALF_EVEN));
				    change.setA_QTY_Current(Env.ONE);
				    change.setA_QTY_Original(Env.ONE);
				    //faaguilar OFB begin
				    change.save();
					    
				    log.config("X_A_Depreciation_Workfile");
				    int Workfile_id = DB.getSQLValue(get_TableName(), "select A_Depreciation_Workfile_ID from A_Depreciation_Workfile where A_Asset_ID="+asset.getA_Asset_ID());
					X_A_Depreciation_Workfile assetwk = new X_A_Depreciation_Workfile (getCtx(), Workfile_id, get_TrxName());
					//assetwk.setA_Asset_ID(asset.getA_Asset_ID());		
					assetwk.setA_Life_Period(assetgrpacct.getUseLifeMonths());
					assetwk.setA_Asset_Life_Years(assetgrpacct.getUseLifeYears());
					assetwk.setA_Asset_Cost(assetwk.getA_Asset_Cost().add(sLine.getLineTotalAmt()));							
					assetwk.setA_QTY_Current(sLine.getQtyEntered());
					assetwk.setIsDepreciated(assetgrpacct.isProcessing());
					assetwk.setPostingType(assetgrpacct.getPostingType());
					assetwk.setA_Accumulated_Depr(new BigDecimal (0.0));
					assetwk.setA_Period_Posted(0);
					assetwk.setA_Asset_Life_Current_Year(new BigDecimal (0.0));
					assetwk.setA_Curr_Dep_Exp(new BigDecimal (0.0));
					//faaguilar OFB begin
					assetwk.setA_Asset_Cost(sLine.getLineNetAmt().divide(sLine.getQtyEntered(), BigDecimal.ROUND_HALF_EVEN));							
					assetwk.setA_QTY_Current(Env.ONE);
					//faaguilar OFB end
					assetwk.save();
					
					log.config("X_A_Asset_Addition");
					int Addition_id = DB.getSQLValue(get_TableName(), "select A_Asset_Addition_ID from A_Asset_Addition where A_Asset_ID="+asset.getA_Asset_ID());
					X_A_Asset_Addition assetadd = new X_A_Asset_Addition (getCtx(), Addition_id, get_TrxName());
					assetadd.setA_Asset_ID(asset.getA_Asset_ID());
					assetadd.setAssetValueAmt(sLine.getLineTotalAmt());
					assetadd.setA_SourceType("INV");
					assetadd.setA_CapvsExp("Cap");
					//assetadd.setM_InOutLine_ID(rs.getInt("C_Invoice_ID"));				
					assetadd.setC_Invoice_ID(getC_Invoice_ID());
					assetadd.setDocumentNo(getDocumentNo());
					assetadd.setLine(sLine.getLine());
					assetadd.setDescription(sLine.getDescription());
					assetadd.setA_QTY_Current(sLine.getQtyEntered());
					assetadd.setPostingType(assetwk.getPostingType());
					//faaguilar OFB begin
					assetadd.setA_QTY_Current(Env.ONE);
					assetadd.setAssetValueAmt(sLine.getLineNetAmt().divide(sLine.getQtyEntered(), BigDecimal.ROUND_HALF_EVEN));
					//faaguilar OFB end
					assetadd.save();
					
					DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and Corrected='N' and A_Asset_ID="+asset.getA_Asset_ID(), get_TrxName());
					CreateAssetForecast.createForecast(asset, change, assetacct, get_TrxName());
					
					createdUpdated = true;
				}
				
				if(sLine.getA_Asset_ID()>0 && sLine.get_ValueAsString("A_CapvsExp").equals(X_C_InvoiceLine.A_CAPVSEXP_Expense))//gasto relacionado con un activo
				{
					MAsset asset = new MAsset(getCtx(),  sLine.getA_Asset_ID() ,get_TrxName());
					BigDecimal monto = sLine.getLineNetAmt();
					
					int change_id = DB.getSQLValue(get_TableName(), "select A_Asset_Change_ID from A_Asset_Change where A_Asset_ID="+asset.getA_Asset_ID());
					MAssetChange change = new MAssetChange (getCtx(), change_id, get_TrxName());
					change.setAssetValueAmt(change.getAssetValueAmt().add(monto) );
					change.save();
					
					int Workfile_id = DB.getSQLValue(get_TableName(), "select A_Depreciation_Workfile_ID from A_Depreciation_Workfile where A_Asset_ID="+asset.getA_Asset_ID());
					X_A_Depreciation_Workfile assetwk = new X_A_Depreciation_Workfile (getCtx(), Workfile_id, get_TrxName());
					assetwk.setA_Asset_Cost(assetwk.getA_Asset_Cost().add(monto));
					assetwk.save();
					
					X_A_Asset_Addition assetadd = new X_A_Asset_Addition (getCtx(), 0, get_TrxName());
					assetadd.setA_Asset_ID(asset.getA_Asset_ID());
					assetadd.setAssetValueAmt(sLine.getLineTotalAmt());
					assetadd.setA_SourceType("INV");
					assetadd.setA_CapvsExp("Exp");			
					assetadd.setC_Invoice_ID(getC_Invoice_ID());
					assetadd.setDocumentNo(getDocumentNo());
					assetadd.setLine(sLine.getLine());
					assetadd.setDescription(sLine.getDescription());
					assetadd.setA_QTY_Current(sLine.getQtyEntered());
					assetadd.setPostingType(assetwk.getPostingType());
					assetadd.setA_QTY_Current(sLine.getQtyInvoiced());
					assetadd.setAssetValueAmt(sLine.getLineNetAmt());
					assetadd.save();
					
					int acct_id = DB.getSQLValue(get_TableName(), "select A_Asset_Acct_ID from A_Asset_Acct where A_Asset_ID="+asset.getA_Asset_ID());
					MAssetAcct assetacct = new MAssetAcct (getCtx(), acct_id, get_TrxName());
					
					
					DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and Corrected='N' and A_Asset_ID="+asset.getA_Asset_ID(), get_TrxName());
					CreateAssetForecast.createForecast(asset, change, assetacct, get_TrxName());
					
					createdUpdated = true;
				}
				
				if(createdUpdated)
				{
					sLine.setA_Processed(true);
					sLine.save();
				}
					
			}
		}//fin for
	}//updateAssetAsset()	
	/**
	 * faaguilar is la linea tiene relacion con activos*/
	private boolean isAsset(MInvoiceLine sLine)
	{
		if(sLine.getC_Charge_ID()>0)
			  if(sLine.getC_Charge().getC_ChargeType_ID()>0)
				if( (sLine.getC_Charge().getC_ChargeType().getValue().equals("TCAF")) )
						return true;
		if(sLine.getM_Product_ID()>0)
			if(sLine.getM_Product().getM_Product_Category().getA_Asset_Group_ID()>0 )
				return true;
		
		return false;
	}
}	//	MInvoice
