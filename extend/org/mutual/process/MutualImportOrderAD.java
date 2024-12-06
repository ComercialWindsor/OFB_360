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
package org.mutual.process;

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
import org.compiere.model.X_I_Order;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: GenerateDocImages.java $
 */
public class MutualImportOrderAD extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		
		
			// copiar data de tabla temporal o tabla estandar adempiere
		  	String sql= "";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			int rImported=0;
			
			//actualizacion de datos basicos
			String clientCheck = " AND AD_Client_ID=" + Env.getAD_Client_ID(getCtx());
			StringBuffer sqlUB = new StringBuffer ("UPDATE I_Order o "	//	SO Document Type Name
					  + "SET C_Currency_ID = 228, IsSOTrx = 'Y', QtyOrdered = 1, M_Warehouse_ID = 1000000, "
					  + "C_PaymentTerm_ID = 1000001, ContactName = MU_RESPONSABLE_SOLICITUD, "
					  + "Address1 = MU_TRA_CENTRO_COSTO, PAtr1 = MU_RUT_TRABAJADOR, PAtr3 = MU_NOMBRE, "
					  + "PAtr4 = MU_APELLIDO_PATERNO, PAtr5 = MU_APELLIDO_MATERNO, PAtr6 = MU_FEC_NAC "
					  + "WHERE I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlUB.toString(), get_TrxName());
			
			//actualizamos servicio
			StringBuffer sqlS = new StringBuffer ("UPDATE I_Order o "
					  + "SET Servicio = '01' "
					  + "where upper(MU_Servicio) like 'PREOCU%'").append (clientCheck);
			DB.executeUpdate(sqlS.toString(), get_TrxName());
			sqlS = new StringBuffer ("UPDATE I_Order o "
					  + "SET Servicio = '02' "
					  + "where upper(MU_Servicio) like 'OCUPACIO%'").append (clientCheck);
			DB.executeUpdate(sqlS.toString(), get_TrxName());
			sqlS = new StringBuffer ("UPDATE I_Order o "
					  + "SET Servicio = '03' "
					  + "where upper(MU_Servicio) like '%SALUD INTEGRAL%'").append (clientCheck);
			DB.executeUpdate(sqlS.toString(), get_TrxName());
			/*verificacion de datos copiados*/
			
			// verificar tipo de documento
			StringBuffer sqlf = new StringBuffer ("UPDATE I_Order o "	//	SO Document Type Name
					  + " SET C_DocType_ID=(SELECT C_DocType_ID FROM C_DocType d WHERE d.Printname=o.Servicio"
					  + " AND d.DocBaseType='SOO' AND o.AD_Client_ID=d.AD_Client_ID) "
					  + " WHERE C_DocType_ID IS NULL AND IsSOTrx='Y' AND MU_Servicio IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//actualizamos rut
			sqlf = new StringBuffer ("UPDATE I_Order o "
					  + "SET BPartnerValue = substr(mu_rut_empresa, 0 , 8) "					  
					  + "WHERE I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//Verificar si existe el BP
			sqlf = new StringBuffer ("UPDATE I_Order o "
					  + "SET C_BPartner_ID=(SELECT MAX(C_BPartner_ID) FROM C_BPartner bp"
					  + " WHERE o.BPartnerValue=bp.Value AND o.AD_Client_ID=bp.AD_Client_ID) "
					  + "WHERE C_BPartner_ID IS NULL AND BPartnerValue IS NOT NULL"
					  + " AND I_IsImported<>'Y'").append (clientCheck);
		    DB.executeUpdate(sqlf.toString(), get_TrxName());	
		    
		  //Verificar BP location
		    sqlf = new StringBuffer ("UPDATE I_Order o "
					  + "SET C_BPartner_Location_ID=(SELECT MAX(C_BPartner_Location_ID) FROM C_BPartner_Location l"
					  + " WHERE l.C_BPartner_ID=o.C_BPartner_ID AND o.AD_Client_ID=l.AD_Client_ID"
					  + " AND l.Name = o.MU_TRA_CENTRO_COSTO) "
					  + "WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL"
					  + " AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//Verificar BP Contacto
		    sqlf = new StringBuffer ("UPDATE I_Order o "
					  + "SET AD_User_ID=(SELECT MAX(AD_User_ID) FROM AD_User l"
					  + " WHERE l.C_BPartner_ID=o.C_BPartner_ID AND o.MU_RESPONSABLE_SOLICITUD=l.Name) "
					  + "WHERE AD_User_ID IS NULL"
					  + " AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//actualizamos tipo empresa
			//StringBuffer sqlTe = new StringBuffer ("UPDATE I_Order o "
			//		  + "SET MU_Adherente = (select companytype from C_BPartner bp " +
			//		    " inner join T_TradeAgreements ta ON (bp.T_TradeAgreements_ID = ta.T_TradeAgreements_ID)" +
			//		    " where C_BPartner_ID=o.C_BPartner_ID) WHERE I_IsImported<>'Y' ").append (clientCheck);
			//DB.executeUpdate(sqlTe.toString(), get_TrxName());
			//@mfrojas Mu_adherente según el archivo
			
			//StringBuffer sqlTeDe = new StringBuffer ("UPDATE I_Order o "
			//		  + "SET MU_Adherente = 'ADHE' "
			//		  + "where MU_Adherente IS NULL AND I_IsImported<>'Y'").append (clientCheck);
			//DB.executeUpdate(sqlTeDe.toString(), get_TrxName());
			
			StringBuffer sqlTeDe1 = new StringBuffer ("UPDATE I_Order o "
					  + "SET MU_Adherente = 'ADHE' "
					  + "where MU_Adherente like 'ADHE%' AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlTeDe1.toString(), get_TrxName());
			sqlTeDe1 = new StringBuffer ("UPDATE I_Order o "
					  + "SET MU_Adherente = 'NOAD' "
					  + "where MU_Adherente like 'NO A%' AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlTeDe1.toString(), get_TrxName());
			sqlTeDe1 = new StringBuffer ("UPDATE I_Order o "
					  + "SET MU_Adherente = 'CONV' "
					  + "where MU_Adherente like 'CONV%' AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlTeDe1.toString(), get_TrxName());
			
			//@mfrojas actualizamos MU_Tipo
			StringBuffer sqlMM1 = new StringBuffer ("UPDATE I_Order o "
					  + "SET MU_TIPO_DE_PRESTACION = 'ADI' "
					  + "where upper(MU_TIPO_DE_PRESTACION) like 'ADICION%'").append (clientCheck);
			DB.executeUpdate(sqlMM1.toString(), get_TrxName());
			sqlMM1 = new StringBuffer ("UPDATE I_Order o "
					  + "SET MU_TIPO_DE_PRESTACION = 'BAT' "
					  + "where upper(MU_TIPO_DE_PRESTACION) like 'BATER%'").append (clientCheck);
			DB.executeUpdate(sqlMM1.toString(), get_TrxName());
			sqlMM1 = new StringBuffer ("UPDATE I_Order o "
					  + "SET MU_TIPO_DE_PRESTACION = 'RIE' "
					  + "where upper(MU_TIPO_DE_PRESTACION) like '%RIESG%'").append (clientCheck);
			DB.executeUpdate(sqlMM1.toString(), get_TrxName());
			//Aplicar lista de precios general
			sqlf = new StringBuffer ("UPDATE I_Order o "
					  + "SET M_PriceList_ID=(SELECT MAX(M_PriceList_ID) FROM M_PriceList p WHERE p.IsDefault='Y'"
					  + " AND p.C_Currency_ID=o.C_Currency_ID AND p.IsSOPriceList=o.IsSOTrx AND o.AD_Client_ID=p.AD_Client_ID) "
					  + "WHERE M_PriceList_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			// Verificar si existe el producto
			sqlf = new StringBuffer ("UPDATE I_Order o "
					  + "SET M_Product_ID=(SELECT MAX(M_Product_ID) FROM M_Product p"
					  + " WHERE o.ProductValue=p.Value AND o.AD_Client_ID=p.AD_Client_ID) "
					  + "WHERE M_Product_ID IS NULL AND ProductValue IS NOT NULL"
					  + " AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//Aplicar version de lista de precios
			sqlf = new StringBuffer ("UPDATE I_Order o "
					  + "SET M_PriceList_Version_ID=(SELECT MAX(M_PriceList_Version_ID) FROM M_PriceList_Version p WHERE p.MU_Servicio=o.Servicio "
					  + "AND p.MU_Adherente=o.MU_Adherente  AND p.M_PriceList_ID=o.M_PriceList_ID AND o.AD_Client_ID=p.AD_Client_ID AND IsBOM = 'Y' AND p.MU_Tipo = o.MU_TIPO_DE_PRESTACION) "
					  + "WHERE o.M_PriceList_Version_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			log.config("Version lista de precios "+sqlf);
			//Verificar Organizacion
			sqlf = new StringBuffer ("UPDATE I_Order o "
					  + "SET AD_Org_ID=(SELECT COALESCE(MAX(AD_Org_ID),0) FROM AD_Org p WHERE p.Value= o.MU_Sucursal"
					  + " AND o.AD_Client_ID=p.AD_Client_ID) "
					  + "WHERE I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		    /*fin verificacion*/
			
			sqlf = new StringBuffer ("UPDATE I_Order "	// No DocType
					  + "SET I_ErrorMsg=null,I_IsImported='N' "
					  + "WHERE "
					  + " I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			
			//actualizamos lista de precios bp especiales
			sqlf = new StringBuffer ("UPDATE I_Order o SET M_PriceList_ID = COALESCE(" +
					" (SELECT MAX(M_PriceList_ID) FROM C_Bpartner p WHERE p.C_Bpartner_ID = o.C_Bpartner_ID ),o.M_PriceList_ID) " +
					" WHERE C_Bpartner_ID IS NOT NULL AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
		    
		    /*actualizar errores*/
			if (DB.isPostgreSQL())
			{
				sqlf = new StringBuffer ("UPDATE I_Order "	// No DocType
						  + "SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=No DocType, ' "
						  + "WHERE C_DocType_ID IS NULL"
						  + " AND I_IsImported<>'Y'").append (clientCheck);
				DB.executeUpdate(sqlf.toString(), get_TrxName());
			    
				sqlf = new StringBuffer ("UPDATE I_Order "
						  + "SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=No PriceList Version, ' "
						  + "WHERE M_PriceList_Version_ID IS NULL"
						  + " AND I_IsImported<>'Y'").append (clientCheck);
				DB.executeUpdate(sqlf.toString(), get_TrxName());
				
				sqlf = new StringBuffer ("UPDATE I_Order "
						  + "SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,'')||'ERR=Invalid Product, ' "
						  + "WHERE M_Product_ID IS NULL "
						  + " AND I_IsImported<>'Y'").append (clientCheck);
			    DB.executeUpdate(sqlf.toString(), get_TrxName());
			}
			else
			{
				sqlf = new StringBuffer ("UPDATE I_Order "	// No DocType
						  + "SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,n'')||'ERR=No DocType, ' "
						  + "WHERE C_DocType_ID IS NULL"
						  + " AND I_IsImported<>'Y'").append (clientCheck);
				DB.executeUpdate(sqlf.toString(), get_TrxName());
			    
				sqlf = new StringBuffer ("UPDATE I_Order "
						  + "SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,n'')||'ERR=No PriceList Version, ' "
						  + "WHERE M_PriceList_Version_ID IS NULL"
						  + " AND I_IsImported<>'Y'").append (clientCheck);
				DB.executeUpdate(sqlf.toString(), get_TrxName());
				
				sqlf = new StringBuffer ("UPDATE I_Order "
						  + "SET I_IsImported='E', I_ErrorMsg=Coalesce(I_ErrorMsg,n'')||'ERR=Invalid Product, ' "
						  + "WHERE M_Product_ID IS NULL "
						  + " AND I_IsImported<>'Y'").append (clientCheck);
			    DB.executeUpdate(sqlf.toString(), get_TrxName());
			}
				
		    /*fin actualizar errores*/
		    
		    //crear BP que no existan
			sql= "SELECT * " +
					"from I_Order where processed='N' and i_isimported='N' and C_BPartner_ID is null" +
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
					X_I_Order temp = new X_I_Order(this.getCtx(), rs, this.get_TrxName());
					
					if(!lastRut.equals(rs.getString("BPartnerValue")))
					{
						lastRut=rs.getString("BPartnerValue");
						bp = new MBPartner(this.getCtx(), 0, this.get_TrxName());
						bp.setAD_Org_ID(0);
						bp.setValue(rs.getString("BPartnerValue"));						
						bp.setName(rs.getString("MU_RAZON_SOCIAL"));
						bp.setC_BP_Group_ID(1000000);
						bp.set_CustomColumn("MU_Adherente", rs.getString("MU_Adherente"));
						bp.save();
						try {
							bp.set_CustomColumn("digito", rs.getString("digito"));
							bp.save();
						} catch (Exception e) {
							log.config("no se pudo setear digito");
						}
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
			
			//crear direcciones que no existen			
			sql= "SELECT * " +
					"from I_Order where processed='N' and i_isimported='N' and C_BPartner_ID is not null AND Address1 is not null " +
					" AND C_BPartner_Location_ID is null ORDER BY Address1, BPartnerValue";					
	
			pstmt = null;
			rs = null;	
			try
			  {
				pstmt = DB.prepareStatement(sql, get_TrxName());
				rs = pstmt.executeQuery ();
				String lastBP="";
				String lastAddres="";
				MLocation loc2 = null;
				MBPartnerLocation bploc2 = null;
				while (rs.next ())
				{
					X_I_Order temp = new X_I_Order(this.getCtx(), rs, this.get_TrxName());					
					
					if(!lastBP.equals(rs.getString("BPartnerValue")))
					{
						if (!lastAddres.equals(rs.getString("Address1")))
						{
							lastBP = rs.getString("BPartnerValue");
							lastAddres = rs.getString("Address1"); 
							
							loc2 = new MLocation(getCtx(), 152, 1000001, "Santiago", get_TrxName());
							loc2.setAddress1(rs.getString("Address1"));
							loc2.save();
							
							MBPartner tempBP = new MBPartner(getCtx(),rs.getInt("C_BPartner_ID"),get_TrxName());
							bploc2 = new MBPartnerLocation(tempBP);
							bploc2.setC_Location_ID(loc2.getC_Location_ID());
							bploc2.setName(rs.getString("Address1"));
							bploc2.save();
						}						
					}
					
					if(loc2!=null)
					{
						temp.setC_BPartner_Location_ID(bploc2.getC_BPartner_Location_ID());
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
			//crear usuarios que no existen ininoles			
			sql= "SELECT * " +
					"from I_Order where processed='N' and i_isimported='N' and C_BPartner_ID is not null AND Address1 is not null " +
					" AND AD_User_ID is null ORDER BY ContactName, BPartnerValue";					
	
			pstmt = null;
			rs = null;	
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				rs = pstmt.executeQuery ();
				String lastBP="";
				String lastUser="";
				MUser user2 = null;				
				while (rs.next ())
				{
					X_I_Order temp = new X_I_Order(this.getCtx(), rs, this.get_TrxName());					
					
					if(!lastBP.equals(rs.getString("BPartnerValue")))
					{
						if (!lastUser.equals(rs.getString("ContactName")))
						{
							lastBP = rs.getString("BPartnerValue");
							lastUser = rs.getString("ContactName"); 
							
							MBPartner tempBP = new MBPartner(getCtx(),rs.getInt("C_BPartner_ID"),get_TrxName());
							user2 = new MUser(tempBP);
							user2.setName(rs.getString("ContactName"));
							user2.save();
						}						
					}
					
					if(user2!=null)
					{
						temp.setAD_User_ID(user2.get_ID());
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
			 //crear organizacion, centros de atencion
			sql= "SELECT * " +
					"from I_Order where processed='N' and i_isimported='N' and (AD_Org_ID is null OR AD_Org_ID < 1)" +
					" Order by MU_SUCURSAL";
			
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
					X_I_Order temp = new X_I_Order(this.getCtx(), rs, this.get_TrxName());
					
					
					if(!lastOrg.equals(rs.getString("MU_SUCURSAL")))
					{
						lastOrg = rs.getString("MU_SUCURSAL");
						org = new MOrg(getCtx(), 0, get_TrxName());
						org.setValue(rs.getString("MU_SUCURSAL"));
						org.setName(rs.getString("MU_SUCURSAL"));
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
			
			//antes de generar ordenes, ver validacion de preadherente @ininoles		
			//verificamos fecha preadherente
			StringBuffer sqlUpPAdherente = new StringBuffer ("UPDATE I_ORDER SET MU_ADHERENTE = 'ADHE', M_PriceList_Version_ID = null" +
					" WHERE I_ORDER_ID IN (SELECT I_ORDER_ID FROM I_ORDER IO " +
					" INNER JOIN C_BPARTNER BP ON (IO.C_BPARTNER_ID = BP.C_BPARTNER_ID) " +
					" WHERE BP.DATEPREADHERENTE IS NOT NULL AND BP.DATEPREADHERENTE > sysdate " +
					" AND IO.MU_ADHERENTE = 'NOAD' AND I_ISIMPORTED <> 'Y')").append (clientCheck);
			//actualizamos campo adherente
			DB.executeUpdate(sqlUpPAdherente.toString(), get_TrxName());			
			//ReAplicar version de lista de precios
			sqlf = new StringBuffer ("UPDATE I_Order o "
					  + "SET M_PriceList_Version_ID=(SELECT MAX(M_PriceList_Version_ID) FROM M_PriceList_Version p WHERE p.MU_Servicio=o.Servicio "
					  + "AND p.MU_Adherente=o.MU_Adherente  AND p.M_PriceList_ID=o.M_PriceList_ID AND o.AD_Client_ID=p.AD_Client_ID AND IsBOM = 'Y' AND p.MU_Tipo = o.MU_TIPO_DE_PRESTACION) "
					  + "WHERE o.M_PriceList_Version_ID IS NULL AND I_IsImported<>'Y'").append (clientCheck);
			DB.executeUpdate(sqlf.toString(), get_TrxName());
			log.config("Version lista de precios "+sqlf);
			//fin pre adherente
			commitEx();
			
			addLog (0, null, new BigDecimal (rImported), "Registros Importados");
			addLog (0, null, new BigDecimal (bpCreated), " Clientes Creados");
			addLog (0, null, new BigDecimal (orgCreated), " Sucursales Creadas");
		//fin preparar
		
		//generar order
			StringBuffer sql1 = new StringBuffer ("SELECT * FROM I_Order "
					  + "WHERE I_IsImported='N' AND I_ErrorMsg IS NULL").append (clientCheck)
			.append(" ORDER BY C_BPartner_ID, Documentno, i_order_id");
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
						X_I_Order imp = new X_I_Order(this.getCtx(), rs, this.get_TrxName());
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
							
							order.save();
							noInsert++;
							lineNo = 10;
							
					   }//new Order
						
//						New OrderLine
						MOrderLine line = new MOrderLine (order);
						line.setLine(lineNo);
						lineNo += 10;
						log.config("Numero de linea"+lineNo);
						if (imp.getM_Product_ID() != 0)
							line.setM_Product_ID(imp.getM_Product_ID(), true);
						line.setQty(imp.getQtyOrdered());
						//Seteamos impuesto con exento
						int id_taxexe = DB.getSQLValue(get_TrxName(), "SELECT C_Tax_ID FROM C_Tax WHERE IsDefault = 'Y' AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx()) );
						if (id_taxexe > 0)
							line.setC_Tax_ID(id_taxexe);
						else
						{
							if (imp.getC_Tax_ID() != 0)
								line.setC_Tax_ID(imp.getC_Tax_ID());
							else
								line.setTax();
						}						
						MProductPricing pp = new MProductPricing (imp.get_ValueAsInt("M_Product_ID"), imp.getC_BPartner_ID(),(BigDecimal)imp.get_Value("QtyOrdered"), true);
						pp.setM_PriceList_ID(imp.getM_PriceList_ID());
						pp.setM_PriceList_Version_ID(imp.get_ValueAsInt("M_PriceList_Version_ID"));
						pp.setPriceDate(imp.getDateOrdered());
						//aca?
						
						line.set_CustomColumn("M_PriceList_Version_ID", imp.get_ValueAsInt("M_PriceList_Version_ID"));
						line.setPrice(pp.getPriceStd());
						//@mfrojas se realiza una nueva consulta con el precio.
						//int preciostdr = DB.getSQLValue(get_TrxName(), "SELECT coalesce(pricestd,0) FROM M_ProductPrice where m_product_id = "+imp.get_ValueAsInt("M_Product_ID")+" and m_pricelist_version_id = "+ imp.get_ValueAsInt("M_Pricelist_version_id"));
						//log.config("precio estandar: "+preciostdr);
						//StringBuffer preciostdr = new StringBuffer ("SELECT coalesce(pricestd,0) FROM M_ProductPrice where m_product_id = "+imp.get_ValueAsInt("M_Product_ID")+" and m_pricelist_version_id = "+ imp.get_ValueAsInt("M_Pricelist_version_id"));
						//log.config("preciostdr: "+preciostdr);
						//int no = DB.executeUpdate(preciostdr.toString(), get_TrxName());
						//line.set_CustomColumn("Help",String.valueOf(no));
						line.setLineNetAmt();
						//@mfrojas se comenta bloque de código para poder revisar si son los atributos los que están generando problemas.
						MAttributeSetInstance att = new MAttributeSetInstance(getCtx(), 0, 
								1000006, get_TrxName());
						att.save();
						
						MAttributeInstance at1 = new MAttributeInstance(getCtx(), 1000009, 
								att.getM_AttributeSetInstance_ID(), imp.get_ValueAsString("PAtr1") , get_TrxName()); // Rut Paciente
						at1.save();
						MAttributeInstance at2 = new MAttributeInstance(getCtx(), 1000010, 
								att.getM_AttributeSetInstance_ID(), imp.get_ValueAsString("PAtr3"), get_TrxName()); // Nombre Paciente
						at2.save();
						MAttributeInstance at3 = new MAttributeInstance(getCtx(), 1000012, 
								att.getM_AttributeSetInstance_ID(), imp.get_ValueAsString("PAtr5"), get_TrxName()); // A. Materno Paciente
						at3.save();
						MAttributeInstance at4 = new MAttributeInstance(getCtx(), 1000011, 
								att.getM_AttributeSetInstance_ID(), imp.get_ValueAsString("PAtr4"), get_TrxName()); // A. Paterno Paciente
						at4.save();
						
						
						/*MAttributeInstance at5 = new MAttributeInstance(getCtx(), 1000014, 
								att.getM_AttributeSetInstance_ID(), imp.get_ValueAsString("PAtr7"), get_TrxName()); // Cargo
						at5.save();*/
						
						
						// @mfrojas
						if (imp.get_ValueAsString("PAtr6") != null && imp.get_ValueAsString("PAtr6") != " "
							&& imp.get_ValueAsString("PAtr6").trim().length() > 0)
						{
							 Calendar fechaActual = Calendar.getInstance();
							 Calendar fechaNac = Calendar.getInstance();
							 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");	
							 fechaNac.setTime(sdf.parse(imp.get_ValueAsString("PAtr6").substring(0, 10)));
							 							
							    // Cálculo de las diferencias.
							 int anios = fechaActual.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
							 int meses = fechaActual.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
							 int dias = fechaActual.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);
							 if(meses < 0 || (meses==0 && dias < 0)) 
								 anios--;
		
							MAttributeInstance at6 = new MAttributeInstance(getCtx(), 1000013, 
									att.getM_AttributeSetInstance_ID(), new BigDecimal(anios), get_TrxName()); // Edad
							at6.save();
							line.set_CustomColumn("Anios", anios);
							
						}
						
						line.setM_AttributeSetInstance_ID(att.getM_AttributeSetInstance_ID());
						
						//nuevos						
						line.set_CustomColumn("CECOEmpresa", imp.get_ValueAsString("MU_TRA_CENTRO_COSTO"));
						//line.set_CustomColumn("PONo", imp.get_ValueAsString("PONo"));
						//line.set_CustomColumn("Hes", imp.get_ValueAsString("Hes"));
						//line.set_CustomColumn("Sep", imp.get_ValueAsString("Sep"));
						//line.set_CustomColumn("Contrato", imp.get_ValueAsString("Contrato"));
						line.set_CustomColumn("ResSolicitud", imp.get_ValueAsString("ContactName"));
						//line.set_CustomColumn("RutMedico", imp.get_ValueAsString("RutMedico"));
						//line.set_CustomColumn("NombreMedico", imp.get_ValueAsString("NombreMedico"));
						line.set_CustomColumn("MU_SOL_CENTRO_COSTO", imp.get_ValueAsString("MU_SOL_CENTRO_COSTO"));
						line.set_CustomColumn("Asistio", imp.get_ValueAsString("MU_ASISTIO"));
						line.set_CustomColumn("Anulo", imp.get_ValueAsString("MU_ANULO"));
						//line.set_CustomColumn("MU_TIPO_DE_PRESTACION", imp.get_ValueAsString("MU_TIPO_DE_PRESTACION"));
						//fecha evaluacion				
						if (imp.get_ValueAsString("MU_FECHA_EVALUACION") != null && imp.get_ValueAsString("MU_FECHA_EVALUACION") != ""
							&& imp.get_ValueAsString("MU_FECHA_EVALUACION") != " " && imp.get_ValueAsString("MU_FECHA_EVALUACION").trim().length() > 0)
						{
							Calendar fechaEva = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");	
							fechaEva.setTime(sdf.parse(imp.get_ValueAsString("MU_FECHA_EVALUACION").substring(0, 10)));
							line.setDatePromised(new Timestamp(fechaEva.getTimeInMillis()));
						}
						line.save();
						//nuevo campo de horario inhabil
						Boolean isIn50 = false;
						log.config("antes de setear variable IsIncrease50"+isIn50);
						log.config("leemos campos antes de setar"+imp.get_ValueAsString("IsIncrease50"));
						try 
						{						 
							if(imp.get_ValueAsString("IsIncrease50").compareTo("SI") == 0 || imp.get_ValueAsString("IsIncrease50").compareTo("si") == 0)
							{
								isIn50 = true;
								log.config("IsIncrease50 verdadrero"+isIn50);
							}
							log.config("antes de setear campo IsIncrease50 en la linea"+isIn50);
							line.set_CustomColumn("IsIncrease50", isIn50);
						}						
						catch (Exception e) {
							log.config("error al setear variable IsIncrease50: "+e.toString());
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
				DB.executeUpdate(sql1.toString(), get_TrxName());
				//addLog (0, null, new BigDecimal (no), "@Errors@");
				//
				//addLog (0, null, new BigDecimal (noInsert), "@C_Order_ID@: @Inserted@");
				addLog (0, null, new BigDecimal (noInsert), "Solicitudes de Examen: @Inserted@");
		
		
	
		
	   return "";
	}	//	doIt


	
}	//	ResetAcct
