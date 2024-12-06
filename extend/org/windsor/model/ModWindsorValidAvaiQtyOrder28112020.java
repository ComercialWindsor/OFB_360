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
package org.windsor.model;

import java.math.BigDecimal;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for company WINDSOR
 *
 *  @author Italo Niñoles
 */
public class ModWindsorValidAvaiQtyOrder28112020 implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModWindsorValidAvaiQtyOrder28112020 ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModWindsorValidAvaiQtyOrder28112020.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;
	

	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing global validator: "+this.toString());
		}

		//	Tables to be monitored
		engine.addModelChange(MOrderLine.Table_Name, this);
		//	Documents to be monitored
		engine.addDocValidate(MOrder.Table_Name, this);

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By italo niñoles
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE)&& po.get_Table_ID()== MOrderLine.Table_ID)  
		{
			MOrderLine oLine = (MOrderLine) po;
			MOrder order = oLine.getParent();
			//que valide en borrador e invalido antes solo lo hacia en borrador
			if(order.isSOTrx() && (order.getDocStatus().compareTo("CO") != 0 && order.getDocStatus().compareTo("IP") != 0)
					&& order.getC_DocTypeTarget().getDocSubTypeSO().compareTo("SO") == 0
					&& order.getC_DocTypeTarget_ID() == 1000030 
					&& order.getAD_Client_ID() == 1000000) 
					//&& order.getDocStatus().compareTo("IN") != 0))
			{
				if(oLine.getM_Product_ID() > 0 && oLine.getM_Product().isStocked()
						&& oLine.getM_Product().getProductType().compareTo("I") == 0)
				{
					//nueva validacion se slata validacion si notprint='Y' y la qtyenteren en 0 13-09-2017
					if(oLine.get_ValueAsBoolean("NotPrint") && oLine.getQtyEntered().compareTo(Env.ZERO) == 0)
						;
					else
					{	
						//validaciones nuevas
						//ininoles 27-06 nueva logica pedida por fernando
						if(oLine.get_ValueAsInt("M_RequisitionLine_ID")>0)
						{
							//se revisa que no supere cantidad de solicitud
							MRequisitionLine rLine = new MRequisitionLine(po.getCtx(), oLine.get_ValueAsInt("M_RequisitionLine_ID"), po.get_TrxName());							
							//nuevo calculo para disponible
							BigDecimal qtyUsed = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM (QtyOrdered) FROM C_OrderLine col " +
									" INNER JOIN C_Order co ON (col.C_Order_ID = co.C_Order_ID) " +
									" WHERE co.DocStatus NOT IN ('VO') AND C_OrderLine_ID <> " +oLine.get_ID()+								
									" AND M_RequisitionLine_ID = "+rLine.get_ID());
							if(qtyUsed == null)
								qtyUsed = Env.ZERO;
							BigDecimal qtyAvarLine =  rLine.getQty().subtract(qtyUsed);							
							if(qtyAvarLine.compareTo(oLine.getQtyOrdered())<0)
								return "Error: Cantidad Supera Cantidad de Solicitud";
							//se valida que sea de la misma direccion y cliente
							if(order.get_ValueAsString("MedioCompra").compareTo("Internet")!=0)
							{
								if(rLine.getParent().get_ValueAsInt("C_Bpartner_ID") != order.getC_BPartner_ID())
									return "Error: Solo puede usar reservas del mismo cliente";
								if(rLine.getParent().get_ValueAsInt("C_Bpartner_Location_ID") != order.getC_BPartner_Location_ID())
								{
									if(!rLine.getParent().get_ValueAsBoolean("OverWriteRequisition"))
										return "Error: Solicitud no es de distribución";
								}
							}
							//validaciones para ecomerce
							if(order.get_ValueAsString("MedioCompra").compareTo("Internet")==0)
							{
								//se valida que solicitud de ecomerce tenga el mismo socio de negocio que NV
								int cant = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM M_Requisition " +
										" WHERE DocStatus IN ('CO','CL') AND C_BPartner_ID = "+order.getC_BPartner_ID()+" AND M_RequisitionRef_ID="+rLine.getParent().get_ID());
								if(cant <= 0 && rLine.getParent().get_ValueAsInt("C_Bpartner_ID") != order.getC_BPartner_ID())
									return "Error: Solo puede usar reservas del mismo cliente";
							}
						}
						else
						{
							//se separa revision de internet y normal
							if(order.get_ValueAsString("MedioCompra").compareTo("Internet")==0)
							{
								//ininoles nueva funcion para presolicitud
								/*int ID_reqP = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mrl2.M_Requisition_ID) FROM M_RequisitionLine rl " +
										" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID)" +
										" INNER JOIN M_Requisition mr2 ON (mr2.M_Requisition_ID = mr.M_RequisitionRef_ID)" +
										" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+order.getC_BPartner_ID()+
										" AND rl.M_Product_ID = "+oLine.getM_Product_ID()+"" +
										" AND qty > qtyUsed AND mr.C_DocType_ID IN (1000111,1000570)");
								*/
								int ID_reqP = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mrl2.M_Requisition_ID)" +
										" FROM  M_Requisition mr" +
										" INNER JOIN M_Requisition mr2 ON (mr.M_RequisitionRef_ID = mr2.M_Requisition_ID)" +
										" INNER JOIN M_Requisitionline mrl2 ON (mr2.M_Requisition_ID = mrl2.M_Requisition_ID)" +
										" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID=" +order.getC_BPartner_ID()+
										" and mrl2.M_Product_ID = "+oLine.getM_Product_ID()+"" +
										//ininoles se agrega que solo use la misma bodega
										" and mr.M_Warehouse_ID = "+order.getM_Warehouse_ID()+
										" AND (mrl2.qty - mrl2.qtyUsed) > 0");
								if (ID_reqP > 0)
								{
									MRequisition req = new MRequisition(po.getCtx(), ID_reqP, po.get_TrxName());
									/*int ID_reqAux = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr.M_Requisition_ID) FROM M_RequisitionLine rl " +
											" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID)" +
											" INNER JOIN M_Requisition mr2 ON (mr2.M_Requisition_ID = mr.M_RequisitionRef_ID)" +
											" WHERE mr.DocStatus IN ('CO','CL') AND mr2.C_BPartner_ID = "+order.getC_BPartner_ID()+
											" AND rl.M_Product_ID = "+oLine.getM_Product_ID()+"" +
											" AND qty > qtyUsed AND mr.C_DocType_ID IN (1000111,1000570)");*/								
									BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
											" WHERE rl.IsActive = 'Y' AND rl.M_Requisition_ID = "+ID_reqP+" AND rl.M_Product_ID = "+oLine.getM_Product_ID());
									return "ERROR: Debe usar solicitud Ecommerce abierta para este cliente. N°: "+req.getDocumentNo()+", producto "+oLine.getM_Product().getName()+" con cantidad "+qtySol;
								}
							}
							//se revisa si existe solicitud abierta
							int ID_req = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr.M_Requisition_ID) FROM M_RequisitionLine rl " +
									" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
									" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+order.getC_BPartner_ID()+
									" AND mr.C_BPartner_Location_ID = "+order.getC_BPartner_Location_ID()+
									//ininoles se agrega que solo use la misma bodega
									" AND mr.M_Warehouse_ID = "+order.getM_Warehouse_ID()+
									" AND rl.M_Product_ID = "+oLine.getM_Product_ID()+" AND qty > qtyUsed");
							//se revisa si existe solicitud de distribucion abierta
							int ID_reqD = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr.M_Requisition_ID) FROM M_RequisitionLine rl " +
									" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
									" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+order.getC_BPartner_ID()+
									" AND mr.M_Warehouse_ID = "+order.getM_Warehouse_ID()+
									" AND mr.OverWriteRequisition = 'Y' AND mr.C_DocType_ID NOT IN (1000111,1000570)"+
									" AND rl.M_Product_ID = "+oLine.getM_Product_ID()+" AND qty > qtyUsed");
							
							if(ID_req > 0)
							{
								MRequisition req = new MRequisition(po.getCtx(), ID_req, po.get_TrxName());
								BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
										" WHERE rl.IsActive = 'Y' AND rl.M_Requisition_ID = "+ID_req+" AND rl.M_Product_ID = "+oLine.getM_Product_ID());								
								return "ERROR: Debe usar solicitud abierta para este cliente. N°: "+req.getDocumentNo()+", producto "+oLine.getM_Product().getName()+"con cantidad "+qtySol;
							}
							else if (ID_reqD > 0)
							{
								MRequisition req = new MRequisition(po.getCtx(), ID_reqD, po.get_TrxName());
								BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
										" WHERE rl.IsActive = 'Y' AND rl.M_Requisition_ID = "+ID_reqD+" AND rl.M_Product_ID = "+oLine.getM_Product_ID());
								return "ERROR: Debe usar solicitud de distribución abierta para este cliente. N°: "+req.getDocumentNo()+", producto "+oLine.getM_Product().getName()+" con cantidad "+qtySol;
							}
							//ininoles 24-09-2019 se mueven validaciones  de solicitud y presolicitud de venta al principio
							BigDecimal qtyAvai = Env.ZERO; 
							if(order.getAD_Client_ID() == 1000000)
							{
								qtyAvai= DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000001) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
								if(qtyAvai == null)
									qtyAvai = Env.ZERO;
								BigDecimal aux = DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000010) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
								if(aux == null)
									aux = Env.ZERO;
								qtyAvai = qtyAvai.add(aux);							
							}
							else
							{
								qtyAvai= DB.getSQLValueBD(po.get_TrxName(), "SELECT bomqtyavailableofb(mp.M_Product_ID,"+order.getM_Warehouse_ID()+",0) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
							}
							if(qtyAvai == null)
								qtyAvai = Env.ZERO;
							//se agrega validacion a peticion de fernando en caso de ser direccion ID = 1010879
							//se envia mensaje personalizado
							if(order.getC_BPartner_Location_ID() == 1010879 || order.getC_BPartner_Location_ID()  == 1011338)
							{
								if(order.getM_Warehouse_ID()== 1000027)
								{
									BigDecimal aux3 = DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000027) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
									if(aux3 == null)
										aux3 = Env.ZERO;
									qtyAvai = qtyAvai.add(aux3);
								}							
								BigDecimal aux2 = DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000024) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
								if(aux2 == null)
									aux2 = Env.ZERO;							
								if(aux2.subtract(oLine.getQtyOrdered()).compareTo(Env.ZERO) < 0)								
									return "ERROR: Stock Insuficiente: "+aux2.intValue()+". Stock en otras bodegas:"+qtyAvai.intValue();
								else//si es la direccion pero no manda el error que reemplaze el disponible
									qtyAvai = aux2;								
							}
							//ininoles nueva validación pedida por fernando 27-12-2018
							if(order.getM_Warehouse_ID() == 1000025)
							{
								BigDecimal aux2 = DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000025) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
								if(aux2 == null)
									aux2 = Env.ZERO;							
								if(aux2.subtract(oLine.getQtyOrdered()).compareTo(Env.ZERO) < 0)								
									return "ERROR: Stock Insuficiente: "+aux2.intValue()+". Stock en otras bodegas:"+qtyAvai.intValue();
								else//si es la direccion pero no manda el error que reemplaze el disponible
								{
									//ininoles si la bodega es 1000025 no se valida nada mas que el stock.
									//qtyAvai = aux2;		
									return null;
								}
							}
							if(order.getM_Warehouse_ID() == 1000027)
							{
								BigDecimal aux2 = DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000027) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
								if(aux2 == null)
									aux2 = Env.ZERO;							
								if(aux2.subtract(oLine.getQtyOrdered()).compareTo(Env.ZERO) < 0)								
									return "ERROR: Stock Insuficiente: "+aux2.intValue()+". Stock en otras bodegas:"+qtyAvai.intValue();
								else//si es la direccion pero no manda el error que reemplaze el disponible
								{
									//ininoles si la bodega es 1000027 no se valida nada mas que el stock.
									//qtyAvai = aux2;		
									return null;
								}
							}
							
							if(qtyAvai.subtract(oLine.getQtyOrdered()).compareTo(Env.ZERO) < 0)
								return "ERROR: Stock Insuficiente y NO Existen solicitudes abiertas";
						}
						
						//logica antigua
						/*
						if(qtyAvai.subtract(oLine.getQtyOrdered()).compareTo(Env.ZERO) >= 0 && oLine.get_ValueAsInt("M_RequisitionLine_ID")<=0)
							;//pasa siempre que tiene disponible y no viene de req
						else
						{
							if(oLine.get_ValueAsInt("M_RequisitionLine_ID")>0)
							{
								//se revisa que no supere cantidad de solicitud
								MRequisitionLine rLine = new MRequisitionLine(po.getCtx(), oLine.get_ValueAsInt("M_RequisitionLine_ID"), po.get_TrxName());							
								//nuevo calculo para disponible
								BigDecimal qtyUsed = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM (QtyOrdered) FROM C_OrderLine col " +
										" INNER JOIN C_Order co ON (col.C_Order_ID = co.C_Order_ID) " +
										" WHERE co.DocStatus NOT IN ('VO') AND C_OrderLine_ID <> " +oLine.get_ID()+								
										" AND M_RequisitionLine_ID = "+rLine.get_ID());
								if(qtyUsed == null)
									qtyUsed = Env.ZERO;
								BigDecimal qtyAvarLine =  rLine.getQty().subtract(qtyUsed);							
								if(qtyAvarLine.compareTo(oLine.getQtyOrdered())<0)
									return "Error: Cantidad Supera Cantidad de Solicitud";
								//se valida que sea de la misma direccion
								if(rLine.getParent().get_ValueAsInt("C_Bpartner_Location_ID") != order.getC_BPartner_Location_ID())
								{
									if(!rLine.getParent().get_ValueAsBoolean("OverWriteRequisition"))
										return "Error: Solicitud no es de distribución";
								}
							}	
							else
							{
								//si no tiene disponible y si no tiene solicitud, se busca solicitud abierta si la ubiera
								int ID_req = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr.M_Requisition_ID) FROM M_RequisitionLine rl " +
										" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
										" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+order.getC_BPartner_ID()+
										" AND mr.C_BPartner_Location_ID = "+order.getC_BPartner_Location_ID()+
										" AND rl.M_Product_ID = "+oLine.getM_Product_ID()+" AND qty > qtyUsed");
								if(ID_req > 0)
								{
									MRequisition req = new MRequisition(po.getCtx(), ID_req, po.get_TrxName());
									BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
											" WHERE rl.M_Requisition_ID = "+ID_req+" AND rl.M_Product_ID = "+oLine.getM_Product_ID());								
									return "ERROR: Sin Stock Disponible "+qtyAvai.intValue()+". Existe solicitud abierta para este cliente. N°: "+req.getDocumentNo()+" con cantidad "+qtySol;
								}else
								{
									//se busca alguna solicitu de distribucion abierta
									int ID_reqD = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr.M_Requisition_ID) FROM M_RequisitionLine rl " +
											" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
											" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+order.getC_BPartner_ID()+
											" AND mr.OverWriteRequisition = 'Y' "+
											" AND rl.M_Product_ID = "+oLine.getM_Product_ID()+" AND qty > qtyUsed");
									if(ID_reqD > 0)
									{
										MRequisition req = new MRequisition(po.getCtx(), ID_req, po.get_TrxName());
										BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
												" WHERE rl.M_Requisition_ID = "+ID_req+" AND rl.M_Product_ID = "+oLine.getM_Product_ID());
										return "ERROR: Sin Stock Disponible "+qtyAvai.intValue()+". Existe solicitud de distribución abierta para este cliente. N°: "+req.getDocumentNo()+" con cantidad "+qtySol;
									}
									else
										return "ERROR: No hay stock disponible ni solicitud asociada";
								}
								
							}
						}*/
					}
				}
			}
			//validacion de devolucion
			if(order.isSOTrx() && order.getC_DocTypeTarget().getDocSubTypeSO().compareTo("RM") == 0)
			{
				if(oLine.get_ValueAsInt("M_RequisitionLine_ID") > 0)
					return "ERROR. Linea de devolución NO debe tener solicitud asociada";
			}
		}
		
		return null;
	}	//	modelChange
	
	public static String rtrim(String s, char c) {
	    int i = s.length()-1;
	    while (i >= 0 && s.charAt(i) == c)
	    {
	        i--;
	    }
	    return s.substring(0,i+1);
	}

	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);

		if(timing == TIMING_BEFORE_PREPARE && po.get_Table_ID()== MOrder.Table_ID)  
		{
			MOrder order = (MOrder) po;
			if(order.isSOTrx() && order.getAD_Client_ID() == 1000000 && order.getC_DocTypeTarget_ID() == 1000030
					&& ((order.getDocStatus().compareTo("CO") != 0 && order.getDocStatus().compareTo("IP") != 0
					&& order.getDocStatus().compareTo("IN") != 0)|| order.get_ValueAsBoolean("ReValidar")))
			{
				MOrderLine[] oLines = order.getLines(false, null);
				for (int i = 0; i < oLines.length; i++)
				{
					MOrderLine oLine = oLines[i];
					if(oLine.getM_Product_ID() > 0 && oLine.getM_Product().isStocked()
							&& oLine.getM_Product().getProductType().compareTo("I") == 0)
					{
						//nueva validacion se salta validacion si noprint='Y' y la qtyenteren en 0 13-09-2017
						if(oLine.get_ValueAsBoolean("NotPrint") && oLine.getQtyEntered().compareTo(Env.ZERO) == 0)
							;
						else
						{
							
							//validaciones nuevas
							//ininoles 27-06 nueva logica pedida por fernando
							if(oLine.get_ValueAsInt("M_RequisitionLine_ID")>0)
							{
								//si viene de solicitud ya tendra hecha las validaciones
								/*
								//se revisa que no supere cantidad de solicitud
								MRequisitionLine rLine = new MRequisitionLine(po.getCtx(), oLine.get_ValueAsInt("M_RequisitionLine_ID"), po.get_TrxName());							
								//nuevo calculo para disponible
								BigDecimal qtyUsed = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM (QtyOrdered) FROM C_OrderLine col " +
										" INNER JOIN C_Order co ON (col.C_Order_ID = co.C_Order_ID) " +
										" WHERE co.DocStatus NOT IN ('VO') AND C_OrderLine_ID <> " +oLine.get_ID()+								
										" AND M_RequisitionLine_ID = "+rLine.get_ID());
								if(qtyUsed == null)
									qtyUsed = Env.ZERO;
								BigDecimal qtyAvarLine =  rLine.getQty().subtract(qtyUsed);							
								if(qtyAvarLine.compareTo(oLine.getQtyOrdered())<0)
									return "Error: Cantidad Supera Cantidad de Solicitud";
								//se valida que sea de la misma direccion
								if(rLine.getParent().get_ValueAsInt("C_Bpartner_Location_ID") != order.getC_BPartner_Location_ID())
								{
									if(!rLine.getParent().get_ValueAsBoolean("OverWriteRequisition"))
										return "Error: Solicitud no es de distribución";
								}*/
								;
							}
							else
							{							
								
								//se separa revision de internet y normal
								//se separa revision de internet y normal
								if(order.get_ValueAsString("MedioCompra").compareTo("Internet")==0)
								{
									//ininoles nueva funcion para presolicitud
									/*int ID_reqP = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mrl2.M_Requisition_ID) FROM M_RequisitionLine rl " +
											" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID)" +
											" INNER JOIN M_Requisition mr2 ON (mr2.M_Requisition_ID = mr.M_RequisitionRef_ID)" +
											" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+order.getC_BPartner_ID()+
											" AND rl.M_Product_ID = "+oLine.getM_Product_ID()+"" +
											" AND qty > qtyUsed AND mr.C_DocType_ID IN (1000111,1000570)");
									*/
									int ID_reqP = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mrl2.M_Requisition_ID)" +
											" FROM  M_Requisition mr" +
											" INNER JOIN M_Requisition mr2 ON (mr.M_RequisitionRef_ID = mr2.M_Requisition_ID)" +
											" INNER JOIN M_Requisitionline mrl2 ON (mr2.M_Requisition_ID = mrl2.M_Requisition_ID)" +
											" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID=" +order.getC_BPartner_ID()+
											//ininoles se agrega que solo use la misma bodega
											" AND mr.M_Warehouse_ID = "+order.getM_Warehouse_ID()+
											" and mrl2.M_Product_ID = "+oLine.getM_Product_ID()+
											" AND (mrl2.qty - mrl2.qtyUsed) > 0");
									if (ID_reqP > 0)
									{
										MRequisition req = new MRequisition(po.getCtx(), ID_reqP, po.get_TrxName());
										/*int ID_reqAux = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr.M_Requisition_ID) FROM M_RequisitionLine rl " +
												" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID)" +
												" INNER JOIN M_Requisition mr2 ON (mr2.M_Requisition_ID = mr.M_RequisitionRef_ID)" +
												" WHERE mr.DocStatus IN ('CO','CL') AND mr2.C_BPartner_ID = "+order.getC_BPartner_ID()+
												" AND rl.M_Product_ID = "+oLine.getM_Product_ID()+"" +
												" AND qty > qtyUsed AND mr.C_DocType_ID IN (1000111,1000570)");*/								
										BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
												" WHERE rl.IsActive = 'Y' AND rl.M_Requisition_ID = "+ID_reqP+" AND rl.M_Product_ID = "+oLine.getM_Product_ID());
										return "ERROR: Debe usar solicitud Ecommerce abierta para este cliente. N°: "+req.getDocumentNo()+", producto "+oLine.getM_Product().getName()+" con cantidad "+qtySol;
									}
								}
								//se revisa si existe solicitud abierta
								int ID_req = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr.M_Requisition_ID) FROM M_RequisitionLine rl " +
										" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
										" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+order.getC_BPartner_ID()+
										" AND mr.M_Warehouse_ID = "+order.getM_Warehouse_ID()+
										" AND mr.C_BPartner_Location_ID = "+order.getC_BPartner_Location_ID()+
										//ininoles se agrega que solo use la misma bodega
										" AND mr.M_Warehouse_ID = "+order.getM_Warehouse_ID()+
										" AND rl.M_Product_ID = "+oLine.getM_Product_ID()+" AND qty > qtyUsed");
								//se revisa si existe solicitud de distribucion abierta
								int ID_reqD = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(mr.M_Requisition_ID) FROM M_RequisitionLine rl " +
										" INNER JOIN M_Requisition mr ON (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
										" WHERE mr.DocStatus IN ('CO','CL') AND mr.C_BPartner_ID = "+order.getC_BPartner_ID()+
										" AND mr.M_Warehouse_ID = "+order.getM_Warehouse_ID()+
										" AND mr.OverWriteRequisition = 'Y' AND mr.C_DocType_ID NOT IN (1000111,1000570)"+
										" AND rl.M_Product_ID = "+oLine.getM_Product_ID()+" AND qty > qtyUsed");
								
								if(ID_req > 0)
								{
									MRequisition req = new MRequisition(po.getCtx(), ID_req, po.get_TrxName());
									BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
											" WHERE rl.IsActive = 'Y' AND rl.M_Requisition_ID = "+ID_req+" AND rl.M_Product_ID = "+oLine.getM_Product_ID());								
									return "ERROR: Debe usar solicitud abierta para este cliente. N°: "+req.getDocumentNo()+", producto "+oLine.getM_Product().getName()+"con cantidad "+qtySol;
								}
								else if (ID_reqD > 0)
								{
									MRequisition req = new MRequisition(po.getCtx(), ID_reqD, po.get_TrxName());
									BigDecimal qtySol = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty - qtyUsed) FROM M_RequisitionLine rl " +
											" WHERE rl.IsActive = 'Y' AND rl.M_Requisition_ID = "+ID_reqD+" AND rl.M_Product_ID = "+oLine.getM_Product_ID());
									return "ERROR: Debe usar solicitud de distribución abierta para este cliente. N°: "+req.getDocumentNo()+", producto "+oLine.getM_Product().getName()+" con cantidad "+qtySol;
								}
								
								//ininoles 24-09-2019 se mueven validaciones  de solicitud y presolicitud de venta al principio
								BigDecimal qtyAvai = Env.ZERO; 
								if(order.getAD_Client_ID() == 1000000)
								{
									qtyAvai= DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000001) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
									if(qtyAvai == null)
										qtyAvai = Env.ZERO;
									BigDecimal aux = DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000010) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
									if(aux == null)
										aux = Env.ZERO;
									qtyAvai = qtyAvai.add(aux);
									if(order.getC_BPartner_Location_ID() == 1010879 || order.getC_BPartner_Location_ID()  == 1011338)
									{
										BigDecimal aux2 = DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000024) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
										if(aux2 == null)
											aux2 = Env.ZERO;
										qtyAvai = qtyAvai.add(aux2);
									}
									if(order.getM_Warehouse_ID() == 1000025)
	                                {
	                                      BigDecimal aux2 = DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000025) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
	                                      if(aux2 == null)
	                                             aux2 = Env.ZERO;
	                                      qtyAvai = qtyAvai.add(aux2);
	                                      return null;
	                                }
								}
								else
								{
									qtyAvai= DB.getSQLValueBD(po.get_TrxName(), "SELECT bomqtyavailableofb(mp.M_Product_ID,"+order.getM_Warehouse_ID()+",0) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
									if(order.getC_BPartner_Location_ID() == 1010879 || order.getC_BPartner_Location_ID()  == 1011338)
									{
										if(order.getM_Warehouse_ID()== 1000027)
										{
											BigDecimal aux3 = DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000027) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
											if(aux3 == null)
												aux3 = Env.ZERO;
											qtyAvai = qtyAvai.add(aux3);
										}
										BigDecimal aux2 = DB.getSQLValueBD(po.get_TrxName(), "SELECT qtyavailableofb(mp.M_Product_ID,1000024) FROM M_Product mp WHERE mp.M_Product_ID = "+oLine.getM_Product_ID());
										if(aux2 == null)
											aux2 = Env.ZERO;
										qtyAvai = qtyAvai.add(aux2);
									}
								}
								if(qtyAvai == null)
									qtyAvai = Env.ZERO;								

								if(qtyAvai.subtract(oLine.getQtyOrdered()).compareTo(Env.ZERO) < 0)
									return "ERROR: Stock Insuficiente y NO Existen solicitudes abiertas";
							}
						}
					}
				}
			}
		}
		
		return null;
	}	//	docValidate

	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString
}	