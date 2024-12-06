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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.MAttributeInstance;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrg;
import org.compiere.model.MProductPricing;
import org.compiere.model.MUser;
import org.compiere.model.X_I_OrderMutual;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: GenerateDocImages.java $
 */
public class VitaclinicImportOrder extends SvrProcess
{
	
	private String p_action;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 ProcessInfoParameter[] para = getParameter();
			for (int i = 0; i < para.length; i++)
			{
				String name = para[i].getParameterName();
				if (para[i].getParameter() == null)
					;
				else if (name.equals("DocAction"))
					p_action = (String)para[i].getParameter();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + name);
				
			}
		 
		 
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{	
		if (p_action.equalsIgnoreCase("GO"))
		{
		// copiar data de tabla temporal o tabla estandard adempiere
		  String sql= "SELECT * from T_MutualOrder where processed='N'";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			int rImported=0;
			try
			  {
				pstmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE, get_TrxName());
				rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					X_I_OrderMutual temp = new X_I_OrderMutual(this.getCtx(), 0, this.get_TrxName());
				
					temp.setC_Currency_ID(228);
					temp.setDateOrdered(rs.getTimestamp("FECHAEVALUACION"));					
					temp.setProductValue(rs.getString("CODIGOPRESTACION"));
					temp.setDocTypeName(rs.getString("SERVICIO"));
					
					//nuevos campos					
					temp.setQtyOrdered(rs.getBigDecimal("qty"));
					temp.set_CustomColumn("Servicio", rs.getString("servicio"));
					temp.set_CustomColumn("Procedimiento", rs.getString("procedimiento"));
					
					//datos cliente
					temp.setBPartnerValue(rs.getString("RUTEMPRESA"));// rut cliente
					temp.set_CustomColumn("BPName", rs.getString("EMPRESA"));//nombre
					temp.setPhone(rs.getString("FONOEMPRESA")); //telefono
					temp.setAddress1(rs.getString("CENTROCOSTOCLIENTE")); //direccion
					
					temp.setContactName(rs.getString("RESPONZABLESOLICITUD"));
					temp.setDocumentNo(rs.getString("CODIGOSOLICITUD"));
					temp.set_CustomColumn("OrgName", rs.getString("CENTROATENCION"));
					
					//datos medico si hay
					temp.set_CustomColumn("RutMedico", rs.getString("rutmedico"));
					temp.set_CustomColumn("DVMedico", rs.getString("dvmedico"));
					temp.set_CustomColumn("NombreMedico", rs.getString("nombremedico"));					
					
					//temp.set_CustomColumn("Adherente", rs.getString("ADHERENTE"));
					
					//temp.set_CustomColumn("ActEconomica", rs.getString("ACTECONOMICA"));
					//temp.set_CustomColumn("CSuministro", rs.getString("CENTROSUMINISTRO"));
					//atributos
					
					/*
					temp.set_CustomColumn("PAtr4", rs.getString("APATERNO"));
					temp.set_CustomColumn("PAtr5", rs.getString("AMATERNO"));
					temp.set_CustomColumn("PAtr6", rs.getString("FECHANACIMIENTO"));
					temp.set_CustomColumn("PAtr7", rs.getString("CARGO"));
					temp.set_CustomColumn("PAtr1", rs.getString("RUT"));
					temp.set_CustomColumn("PAtr2", rs.getString("DV"));
					temp.set_CustomColumn("PAtr3", rs.getString("NOMBRES"));
					*/
					
				
					temp.setIsSOTrx(true);
					//temp.setQtyOrdered(Env.ONE);
					
					temp.setM_Warehouse_ID(1000000);
					temp.setC_PaymentTerm_ID(1000001);
				
					if(temp.save())
					{
						rImported++;
						rs.updateString("processed", "Y");
						rs.updateRow();
					}
				}
			  }
			  catch (Exception e)
			  {
					log.severe(e.getMessage());
			  }
			finally{
				 DB.close(rs, pstmt);
				 rs=null;pstmt=null;
			 }
			
			/*verificacion de datos copiados*/
			// verificar tipo de documento
			String clientCheck = " AND AD_Client_ID=" + Env.getAD_Client_ID(getCtx());
			StringBuffer sqlf = new StringBuffer ("UPDATE I_OrderMutual o "	//	SO Document Type Name
					  + "SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Printname=o.DocTypeName"
					  + " AND d.DocBaseType='SOO' AND o.AD_Client_ID=d.AD_Client_ID) "
					  + "WHERE C_DocType_ID IS NULL AND IsSOTrx='Y' AND DocTypeName IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//Aplicar lista de precios general
			sqlf = new StringBuffer ("UPDATE I_OrderMutual o "
					  + "SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p WHERE p.IsDefault='Y'"
					  + " AND p.C_Currency_ID=o.C_Currency_ID AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) "
					  + "WHERE M_PriceList_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			// Verificar si existe el producto
			sqlf = new StringBuffer ("UPDATE I_OrderMutual o "
					  + "SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
					  + " WHERE o.ProductValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) "
					  + "WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL"
					  + " AND I_IsImported<>'Y'").append (clientCheck);
			int i =DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//Aplicar version de lista de precios
			sqlf = new StringBuffer ("UPDATE I_OrderMutual o "
					  + "SET M_PriceList_Version_ID=(SELECT MAX(M_PriceList_Version_ID) FROM M_PriceList_Version p WHERE "
					  + "p.M_PriceList_ID=o.M_PriceList_ID AND o.AD_Client_ID=p.AD_Client_ID) "
					  + "WHERE o.M_PriceList_Version_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck);
			i = DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//Verificar si existe el BP
			sqlf = new StringBuffer ("UPDATE I_OrderMutual o "
					  + "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp"
					  + " WHERE o.BPartnerValue=bp.Value AND o.AD_Client_ID=bp.AD_Client_ID) "
					  + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
					  + " AND I_IsImported<>'Y'").append (clientCheck);
		    DB.executeUpdate(sqlf.toString(), get_TrxName());	
			
		  //Verificar BP location
		    sqlf = new StringBuffer ("UPDATE I_OrderMutual o "
					  + "SET C_BPartner_Location_ID=(SELECT MAX(C_BPartner_Location_ID) FROM C_BPartner_Location l"
					  + " WHERE l.C_BPartner_ID=o.C_BPartner_ID AND o.AD_Client_ID=l.AD_Client_ID"
					  + " AND l.Name = o.Address1) "
					  + "WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL"
					  + " AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//Verificar BP Contacto
		    sqlf = new StringBuffer ("UPDATE I_OrderMutual o "
					  + "SET AD_User_ID=(SELECT MAX(AD_User_ID) FROM AD_User l"
					  + " WHERE l.C_BPartner_ID=o.C_BPartner_ID AND o.ContactName=l.Name) "
					  + "WHERE AD_User_ID IS NULL"
					  + " AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//Verificar Organizacion
			sqlf = new StringBuffer ("UPDATE I_OrderMutual o "
					  + "SET AD_Org_ID=(SELECT MAX(AD_Org_ID) FROM AD_Org p WHERE p.Value= o.OrgName"
					  + " AND o.AD_Client_ID=p.AD_Client_ID) "
					  + "WHERE AD_Org_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		    /*fin verificacion*/
			
			sqlf = new StringBuffer ("UPDATE I_OrderMutual "	// No DocType
					  + "SET I_ErrorMsg=null,I_IsImported='N' "
					  + "WHERE "
					  + " I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		    
		    /*actualizar errores*/
			
			sqlf = new StringBuffer ("UPDATE I_OrderMutual "	// No DocType
					  + "SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=No DocType, ' "
					  + "WHERE C_DocType_ID IS NULL"
					  + " AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		    
			sqlf = new StringBuffer ("UPDATE I_OrderMutual "
					  + "SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=No PriceList Version, ' "
					  + "WHERE M_PriceList_Version_ID IS NULL"
					  + " AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			sqlf = new StringBuffer ("UPDATE I_OrderMutual "
					  + "SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=Invalid Product, ' "
					  + "WHERE M_Product_ID IS NULL "
					  + " AND I_IsImported<>'Y'").append (clientCheck);
		    DB.executeUpdate(sqlf.toString(), get_TrxName());
		    
		    /*fin actualizar errores*/
		    
		    //crear BP que no existan
			sql= "SELECT * " +
					"from I_OrderMutual where processed='N' and i_isimported='N' and C_BPartner_ID is null" +
					" Order by BPartnerValue,ContactName";
			pstmt = null;
			rs = null;
			int bpCreated=0;
			try
			  {
				pstmt = DB.prepareStatement(sql, get_TrxName());
				rs = pstmt.executeQuery ();
				String lastRut="";
				String lastResponsable="";
				MBPartner bp = null;
				MBPartnerLocation bploc = null;
				MUser user = null;
				while (rs.next ())
				{
					X_I_OrderMutual temp = new X_I_OrderMutual(this.getCtx(), rs, this.get_TrxName());
					
					if(!lastRut.equals(rs.getString("BPartnerValue")))
					{
						lastRut=rs.getString("BPartnerValue");
						bp = new MBPartner(this.getCtx(), 0, this.get_TrxName());
						bp.setAD_Org_ID(0);
						bp.setValue(rs.getString("BPartnerValue"));
						bp.setName(rs.getString("BPName"));
						bp.setC_BP_Group_ID(1000000);
						bp.set_CustomColumn("MU_Adherente", rs.getString("adherente"));
						bp.save();
						bpCreated++;
						
						MLocation loc = new MLocation(getCtx(), 152, 1000001, "Santiago", get_TrxName());
						loc.setAddress1(rs.getString("Address1"));
						loc.save();
						
						bploc = new MBPartnerLocation(bp);
						bploc.setC_Location_ID(loc.getC_Location_ID());
						bploc.setName(rs.getString("Address1"));
						bploc.save();
						
						if(!lastResponsable.equals(rs.getString("ContactName")))
						{
							lastResponsable = rs.getString("ContactName");
							user = new MUser(bp);
							user.setName(rs.getString("ContactName"));
							user.save();
						}
					}
					
					if(bp!=null)
					{
						temp.setC_BPartner_ID(bp.getC_BPartner_ID());
						temp.setC_BPartner_Location_ID(bploc.getC_BPartner_Location_ID());
						temp.setAD_User_ID(user.getAD_User_ID());
						temp.setI_IsImported(false);
						temp.save();
					}
				}
			  }
			  catch (Exception e)
			  {
					log.severe(e.getMessage());
			  }
			finally{
				 DB.close(rs, pstmt);
				 rs=null;pstmt=null;
			 }
			
			
			 //crear organizacion, centros de atencion
			sql= "SELECT * " +
					"from I_OrderMutual where processed='N' and i_isimported='N' and AD_Org_ID is null" +
					" Order by OrgName";
			
			int orgCreated=0;
			pstmt = null;
			rs = null;	
			try
			  {
				pstmt = DB.prepareStatement(sql, get_TrxName());
				rs = pstmt.executeQuery ();
				String lastOrg="";
				MOrg org = null;
				while (rs.next ())
				{
					X_I_OrderMutual temp = new X_I_OrderMutual(this.getCtx(), rs, this.get_TrxName());
					
					
					if(!lastOrg.equals(rs.getString("OrgName")))
					{
						lastOrg = rs.getString("OrgName");
						org = new MOrg(getCtx(), 0, get_TrxName());
						org.setValue(rs.getString("OrgName"));
						org.setName(rs.getString("OrgName"));
						org.save();
						orgCreated++;
					}
					
					if(org!=null)
					{
						temp.setAD_Org_ID(org.getAD_Org_ID());
						temp.setI_IsImported(false);
						temp.save();
					}
				}
			  }
			  catch (Exception e)
			  {
					log.severe(e.getMessage());
			  }
			finally{
				 DB.close(rs, pstmt);
				 rs=null;pstmt=null;
			 }
			commitEx();
			
			addLog (0, null, new BigDecimal (rImported), "Registros Importados");
			addLog (0, null, new BigDecimal (bpCreated), " Clientes Creados");
			addLog (0, null, new BigDecimal (orgCreated), " Sucursales Creadas");
			
		//fin preparar
		
		
		
		//generar order
			StringBuffer sql1 = new StringBuffer ("SELECT * FROM I_OrderMutual "
					  + "WHERE I_IsImported='N' AND I_ErrorMsg IS NULL").append (clientCheck)
			.append(" ORDER BY C_BPartner_ID, Documentno");
			int noInsert = 0;
			
			pstmt = null;
			rs = null;
				try
				{
					pstmt = DB.prepareStatement (sql1.toString(), get_TrxName());
					rs = pstmt.executeQuery ();
					String oldDocumentNo = "";
					int oldC_BPartner_ID = 0;
					MOrder order = null;
					int lineNo = 0;
					while (rs.next ())
					{
						X_I_OrderMutual imp = new X_I_OrderMutual(this.getCtx(), rs, this.get_TrxName());
						String cmpDocumentNo = imp.getDocumentNo();
						if (cmpDocumentNo == null)
							cmpDocumentNo = "";
						
//						New Order
						if (oldC_BPartner_ID != imp.getC_BPartner_ID() 
							|| !oldDocumentNo.equals(cmpDocumentNo))
						{
							if (order != null)
							{
								
								order.setDocAction("CO");
								order.processIt ("CO");
								order.save();
							}
							
							oldC_BPartner_ID = imp.getC_BPartner_ID();
							oldDocumentNo = imp.getDocumentNo();
							
							order = new MOrder (getCtx(), 0, get_TrxName());
							order.setClientOrg (imp.getAD_Client_ID(), imp.getAD_Org_ID());
							order.setC_DocTypeTarget_ID(imp.getC_DocType_ID());
							order.setIsSOTrx(imp.isSOTrx());
							if (imp.getDocumentNo() != null)
								order.setDocumentNo(imp.getDocumentNo());
							//	Ship Partner
							order.setC_BPartner_ID(imp.getC_BPartner_ID());
							order.setC_BPartner_Location_ID(imp.getC_BPartner_Location_ID());
							order.setC_PaymentTerm_ID(imp.getC_PaymentTerm_ID());
							order.setM_PriceList_ID(imp.getM_PriceList_ID());
							order.setM_Warehouse_ID(imp.getM_Warehouse_ID());
							if (imp.getAD_User_ID() != 0)
								order.setAD_User_ID(imp.getAD_User_ID());
							
							if (imp.getDateOrdered() != null){
								order.setDateOrdered(imp.getDateOrdered());
								order.setDateAcct(imp.getDateOrdered());
							}
							
							
							order.setSalesRep_ID(Env.getAD_User_ID(getCtx()));
							order.setDescription(imp.get_ValueAsString("Procedimiento"));
							
							
							order.save();
							noInsert++;
							lineNo = 10;
							
					   }//new Order
						
//						New OrderLine
						MOrderLine line = new MOrderLine (order);
						line.setLine(lineNo);
						lineNo += 10;
						if (imp.getM_Product_ID() != 0)
							line.setM_Product_ID(imp.getM_Product_ID(), true);
						line.setQty(imp.getQtyOrdered());
						if (imp.getC_Tax_ID() != 0)
							line.setC_Tax_ID(imp.getC_Tax_ID());
						else
							line.setTax();
						
						MProductPricing pp = new MProductPricing (imp.getM_Product_ID(), imp.getC_BPartner_ID(),imp.getQtyOrdered(), true);
						pp.setM_PriceList_ID(imp.getM_PriceList_ID());
						pp.setM_PriceList_Version_ID(imp.get_ValueAsInt("M_PriceList_Version_ID"));
						pp.setPriceDate(imp.getDateOrdered());
						
						line.set_CustomColumn("M_PriceList_Version_ID", imp.get_ValueAsInt("M_PriceList_Version_ID"));
						line.setPrice(pp.getPriceStd());
						line.setLineNetAmt();
						
						//nuevos campos
						//line.set_CustomColumn("Servicio", imp.get_ValueAsInt("Servicio"));
						line.set_CustomColumn("RutMedico", imp.get_ValueAsString("RutMedico"));
						line.set_CustomColumn("DVMedico", imp.get_ValueAsString("DVMedico"));
						line.set_CustomColumn("NombreMedico", imp.get_ValueAsString("NombreMedico"));
						line.setDescription(imp.get_ValueAsString("Servicio"));						

						//revision de rut doctor para asignar impuesto
						if (imp.get_ValueAsString("RutMedico") != null && imp.get_ValueAsString("RutMedico") != "" && imp.get_ValueAsString("RutMedico") != " ")
						{
							line.setC_Tax_ID(1000002);
						}
						
						line.save();
						
						imp.setC_OrderLine_ID(line.getC_OrderLine_ID());
						imp.setC_Order_ID(order.getC_Order_ID());
						imp.setI_IsImported(true);
						imp.setProcessed(true);
						imp.save();
					}
					
					if (order != null)
					{
						
						order.setDocAction("CO");
						order.processIt ("CO");
						order.save();
					}
				}
				catch (SQLException e)
				{
					log.log(Level.SEVERE, "BP - " + sql1.toString(), e);
				}
				finally{
					 DB.close(rs, pstmt);
					 rs=null;pstmt=null;
				 }
				
				commitEx();
				
//				Set Error to indicator to not imported
				sql1 = new StringBuffer ("UPDATE I_OrderMutual "
					+ "SET I_IsImported='N', Updated=SysDate "
					+ "WHERE I_IsImported<>'Y'").append(clientCheck);
				int no = DB.executeUpdate(sql1.toString(), get_TrxName());
				addLog (0, null, new BigDecimal (no), "@Errors@");
				//
				addLog (0, null, new BigDecimal (noInsert), "@C_Order_ID@: @Inserted@");
		}
	   return "";
	}	//	doIt


	
}	//	ResetAcct
