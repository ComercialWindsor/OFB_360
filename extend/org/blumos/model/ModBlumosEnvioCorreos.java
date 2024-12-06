package org.blumos.model;

import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.compiere.model.MClient;
import org.compiere.model.MInOut;
import org.compiere.model.MMovement;
import org.compiere.model.MOrder;
import org.compiere.model.MUser;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_ProjectOFB;
import org.compiere.model.X_T_SEGUIMIENTO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.ofb.utils.DateUtils;

/**
 *	Mail for company blumos
 *
 *  @author ininoles
 */
public class ModBlumosEnvioCorreos implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModBlumosEnvioCorreos ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModBlumosEnvioCorreos.class);
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
		engine.addModelChange(MOrder.Table_Name, this);
		engine.addModelChange(X_T_SEGUIMIENTO.Table_Name, this);
		engine.addModelChange(X_C_ProjectOFB.Table_Name, this);
		//	Documents to be monitored
		engine.addDocValidate(MInOut.Table_Name, this);
		
				

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);		
		//correos de trigger PRIORIDADES
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MOrder.Table_ID 
				&& po.is_ValueChanged("vigila_eta") && po.getAD_Client_ID() != 1000005)  
		{
			MOrder order = (MOrder) po;
			String subjet= "";
			String v_query = "";
			String v_via= "VIA DESCONOCIDA";
			if(order.get_ValueAsString("inco_via").compareTo("I00")==0)
				v_via = "AEREO";
			else if(order.get_ValueAsString("inco_via").compareTo("I10")==0)
				v_via = "MARITIMO";
			else if(order.get_ValueAsString("inco_via").compareTo("I20")==0)
				v_via = "TERRESTRE";
			String lista = "";
			v_query="Select substr(mp.name,0,30) as cod_blumos, col.qtyordered, cu.name from c_orderline col inner join m_product mp on (col.m_product_id=mp.m_product_id) inner join c_uom cu on (mp.c_uom_id=cu.c_uom_id) where col.c_order_id="+order.get_ID();
			String el_detalle = "";
			MUser user = new MUser(po.getCtx(), Env.getAD_User_ID(po.getCtx()), po.get_TrxName());			
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement (v_query, null);
			ResultSet rs = pstmt.executeQuery ();
			while(rs.next ())
			{
				lista = lista+rs.getString("cod_blumos")+", Cantidad: "+rs.getBigDecimal("qtyordered")+
					" "+rs.getString("name")+" \n ";
			}
			
			if((order.getDocStatus().compareTo("CO")==0 || order.getDocStatus().compareTo("IP")==0) 
					&& order.get_ValueAsBoolean("vigila_eta")
					&& !order.isSOTrx())
			{
				//RUTINA PARA ENVIAR CORREOS SI SE ACTIVA LA CASILLA VIGILA_ETA				
				subjet="ACTIVADO CONTROL ETA_BODEGA EN OC : "+order.getDocumentNo();				
				el_detalle = "<pre>Se ha Activado Control sobre ETA_BODEGA de la Orden "+order.getDocumentNo()+", cuya ETA_BODEGA actual es "+BlumosUtilities.formatDate((Timestamp)order.get_Value("eta_bodega"),true)+" \n "+
					v_via+" \n "+
					"<b>Proveedor: "+order.getC_BPartner().getName().replace("'","''")+"</b>"+" \n \n "+
					"</b>Productos afectados: \n "+lista+" \n "+"Cambio generado por <b>"+user.getName()+"</b></pre>";
				String el_correo = subjet+el_detalle;
				int id_correo=1000003;
				String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+subjet+"','"+el_correo+"')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
				pstmtSM.execute();
			}			
			else if((order.getDocStatus().compareTo("CO")==0 || order.getDocStatus().compareTo("IP")==0) 
					&& !order.get_ValueAsBoolean("vigila_eta")
					&& !order.isSOTrx())
			{
				//RUTINA PARA ENVIAR CORREOS SI SE DESACTIVA LA CASILLA VIGILA_ETA
				subjet="DESACTIVADO CONTROL ETA_BODEGA EN OC : "+order.getDocumentNo();
				el_detalle = "<pre>Se ha DESACTIVADO Control sobre ETA_BODEGA de la Orden "+order.getDocumentNo()+", cuya ETA_BODEGA actual es "+BlumosUtilities.formatDate((Timestamp)order.get_Value("eta_bodega"),true)+" \n "+
					v_via+" \n "+
					"<b>Proveedor: "+order.getC_BPartner().getName().replace("'","''")+"</b>"+" \n \n "+
					"</b>Productos afectados: \n "+lista+" \n "+"Cambio generado por <b>"+user.getName()+"</b></pre>";
				String el_correo = subjet+el_detalle;
				int id_correo=1000004;
				//int flag = DB.executeUpdate("SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+subjet+"','"+el_correo+"')", po.get_TrxName());
				String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+subjet+"','"+el_correo+"')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
				pstmtSM.execute();
			}
		}
		//correo de trigger PRIORIDADES
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MOrder.Table_ID 
				&& po.is_ValueChanged("ETA_Bodega") && po.getAD_Client_ID() != 1000005)  
		{
			MOrder order = (MOrder) po;
			//RUTINA PARA ENVIAR CORREO SI HAY CAMBIOS DE ETA_BODEGA
			if((order.getDocStatus().compareTo("CO")==0 || order.getDocStatus().compareTo("IP")==0) 
					&& !order.isSOTrx())
			{
				Timestamp newEta = (Timestamp)order.get_Value("ETA_Bodega");
				Timestamp oldEta = (Timestamp)order.get_ValueOld("ETA_Bodega");
				int dif_dias = (int) DateUtils.getDifferenceDays(newEta, oldEta);
				String la_frase = "Dias de Atraso: ";
				if (newEta.compareTo(oldEta)<0) 
				{
					// modificado para tratamiento de negativos. CMM 1/10/18
					//dif_dias = dif_dias*-1;
					la_frase="Dias de ADELANTO ";
				} else {
					dif_dias = dif_dias*-1;
				}
				if(((dif_dias > 2 && order.get_ValueAsString("inco_via").compareTo("I10") != 0)
						|| (dif_dias > 6 && order.get_ValueAsString("inco_via").compareTo("I10") == 0)
						|| order.get_ValueAsBoolean("vigila_eta")) && order.getAD_Client_ID() == 1000000)
				{
					MUser user = new MUser(po.getCtx(), Env.getAD_User_ID(po.getCtx()), po.get_TrxName());
					String v_via= "VIA DESCONOCIDA";
					if(order.get_ValueAsString("inco_via").compareTo("I00")==0)
						v_via = "AEREO";
					else if(order.get_ValueAsString("inco_via").compareTo("I10")==0)
						v_via = "MARITIMO";
					else if(order.get_ValueAsString("inco_via").compareTo("I20")==0)
						v_via = "TERRESTRE";
					String v_texto = "";
					if(order.get_ValueAsBoolean("vigila_eta"))
						v_texto = " (Sujeta a Control de ETA_Bodega)";
					else
						v_texto = "";
					String el_subjet= "CAMBIO ETA_BODEGA OC: "+order.getDocumentNo();
					String v_query = "";					
					String lista = "";
					v_query="Select substr(mp.name,0,30) as cod_blumos, col.qtyordered, cu.name from c_orderline col inner join m_product mp on (col.m_product_id=mp.m_product_id) inner join c_uom cu on (mp.c_uom_id=cu.c_uom_id) where col.c_order_id="+order.get_ID();
					PreparedStatement pstmt = null;
					pstmt = DB.prepareStatement (v_query, null);
					ResultSet rs = pstmt.executeQuery ();
					while(rs.next ())
					{
						lista = lista+rs.getString("cod_blumos")+", Cantidad: "+rs.getBigDecimal("qtyordered")+
							" "+rs.getString("name")+" \n ";
					}					
					String el_detalle = "<pre>Se ha cambiado la ETA_BODEGA de la Orden "+order.getDocumentNo()+v_texto+", desde <b>"+BlumosUtilities.formatDate(oldEta,true)+" a "+BlumosUtilities.formatDate(newEta,true)+" \n "+
						la_frase+Math.abs(dif_dias)+" , Via: "+v_via+" \n "+
					 	"<b>Proveedor: "+order.getC_BPartner().getName().replace("'","''")+"</b>"+" \n \n"+
					 	"</b>Productos afectados: \n"+
					 	lista+" \n "+
					 	"Cambio generado por <b>"+user.getName()+"</b></pre>";
					String el_correo = el_subjet+el_detalle;
					int id_correo = 1000002;
					//int flag = DB.executeUpdate("SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subjet+"','"+el_correo+"')", po.get_TrxName());
					String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subjet+"','"+el_correo+"')";
					PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
					pstmtSM.execute();
				}
			}
		}	
		//correo de trigger PRIORIDADES
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MOrder.Table_ID 
				&& po.is_ValueChanged("ISAPPROVED") && po.getAD_Client_ID() != 1000005)  
		{
			MOrder order = (MOrder) po;
			String el_subjet= "";
			String el_detalle= "";
			String el_correo = "";
			String v_query = "";
			int id_correo = 0;
			String desc = " ";
			if(order.getDescription() != null 
					&& order.getDescription().trim().length()>0)
				desc = order.getDescription();
			if(order.get_ValueAsBoolean("ISAPPROVED") && 
					(order.getDocStatus().compareTo("DR") ==0 || order.getDocStatus().compareTo("IP") ==0))
			{
				MUser user = new MUser(po.getCtx(), Env.getAD_User_ID(po.getCtx()), po.get_TrxName());
				//se calcula turno
				//correo1 CORREOS AUTOMATICOS de Ventas de productos a cuenta de solutec
				String v_turno = "";
				if(((DateUtils.now().getHours() == 9 && DateUtils.now().getMinutes() > 0)
						|| DateUtils.now().getHours() > 9)
					&& DateUtils.now().getHours() < 15)
				{
					if(order.getDeliveryViaRule().compareTo("V")==0)
						v_turno = "TARDE";				
					else
						v_turno ="MAÑANA";						
				}
				else
				{
					if(order.getDeliveryViaRule().compareTo("V")==0)
						v_turno ="MAÑANA";							
					else
						v_turno ="TARDE";	
				}
				v_query = "Select mp.m_product_id, mp.name as producto, mp.solutec, col.qtyordered, col.priceentered, col.datepromised " +
						" from c_orderline col" +
						" inner join m_product mp ON (col.m_product_id=mp.m_product_id)" +
						" where mp.solutec='Y' AND col.C_ORDER_ID="+order.get_ID();
				int v_control = 0;
				el_subjet= "Venta Solutec NV "+order.getDocumentNo();
				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement (v_query, null);
				ResultSet rs = pstmt.executeQuery ();
				while(rs.next ())
				{
					v_control++;
					el_detalle = el_detalle+"\n Fecha Prometida linea: "+BlumosUtilities.formatDate(rs.getTimestamp("datepromised"),true)+
					" Producto: "+rs.getString("producto")+" - Cantidad ordenada: "+rs.getBigDecimal("qtyordered").setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",")+" - Valor Unitario: "+
					rs.getBigDecimal("priceentered")+" "+order.getC_Currency().getISO_Code();
				}	
				el_detalle = el_detalle+"\n"+"Orden generada por "+user.getName()+" el "+BlumosUtilities.formatDateFull(DateUtils.now(), true);
				el_correo = "<pre>"+el_subjet+", Fecha de la Orden: "+BlumosUtilities.formatDate(order.getDateOrdered(), true)+
					", Fecha Prometida: "+BlumosUtilities.formatDate(order.getDatePromised(), true)+" Turno: "+v_turno+" \n "+
					"Cliente: "+order.getC_BPartner().getName().replace("'","''")+". Vendedor: "+order.getSalesRep().getName()+"\n"+
					"Descripción: "+desc+" \n\n"+"Detalle de la Orden: "+" \n"+el_detalle;
				if(v_control > 0)
				{
					id_correo =1000005;
					//String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),'"+BlumosUtilities.DameMail(user.get_ID(), 0,po.getCtx(),po.get_TrxName())+"'||,||DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subjet+"','"+el_correo+"')";
					String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),damemail("+user.get_ID()+",0)||','|| DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subjet+"','"+el_correo+"</pre>')";
					PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
					pstmtSM.execute();
				}
				//correo 2 PARA ENVIAR CORREOS SI BODEGA DE ORIGEN ES FARMA
				if(order.getM_Warehouse_ID() == 1000070 && order.isSOTrx())
				{
					v_query = "Select mp.m_product_id, mp.name as producto, mp.solutec, col.qtyordered, col.priceentered" +
							" from c_orderline col" +
							" inner join m_product mp ON (col.m_product_id=mp.m_product_id)" +
							" where col.C_ORDER_ID="+order.get_ID();
					
					el_subjet= "Venta FARMA: "+order.getDocumentNo();
					el_detalle = "";
					PreparedStatement pstmt2 = null;
					pstmt2 = DB.prepareStatement (v_query, null);
					ResultSet rs2 = pstmt2.executeQuery ();
					while(rs2.next ())
					{
						el_detalle =" Producto: "+rs2.getString("producto")+" - Cantidad ordenada: "+rs2.getBigDecimal("qtyordered").setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",")+" - Valor Unitario: "+
						rs2.getBigDecimal("priceentered")+" "+order.getC_Currency().getISO_Code()+" \n";
					}
					el_detalle = el_detalle+"\n"+"Orden generada por "+user.getName()+" el "+BlumosUtilities.formatDateFull(DateUtils.now(), true);
					el_correo = "<pre>"+el_subjet+", Fecha de la Orden: "+BlumosUtilities.formatDate(order.getDateOrdered(), true)+
						", Fecha Prometida: "+BlumosUtilities.formatDate(order.getDatePromised(), true)+"\n"+
						"Cliente: "+order.getC_BPartner().getName().replace("'","''")+". Vendedor: "+order.getSalesRep().getName()+"\n"+
						"Descripción: "+desc+"\n \n"+"Detalle de la Orden: "+"\n"+el_detalle;
					id_correo =1000047;
					//String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),'"+BlumosUtilities.DameMail(user.get_ID(), 0,po.getCtx(),po.get_TrxName())+"'||,||DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subjet+"','"+el_correo+"')";
					String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),damemail("+user.get_ID()+",0)||','||DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subjet+"','"+el_correo+"</pre>')";
					PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
					pstmtSM.execute();
				}
				
				//correo 3 PARA ENVIAR CORREOS POR NV QUE DICEN RETIRA
				if(order.getDeliveryViaRule().compareTo("P") == 0)
				{
					v_query = "Select mp.m_product_id, mp.name as producto, mp.solutec, col.qtyordered, col.priceentered" +
							" from c_orderline col" +
							" inner join m_product mp ON (col.m_product_id=mp.m_product_id)" +
							" where col.C_ORDER_ID="+order.get_ID();
			
					el_subjet= "Venta RETIRA NV: "+order.getDocumentNo();
					el_detalle = "";
					PreparedStatement pstmt2 = null;
					pstmt2 = DB.prepareStatement (v_query, null);
					ResultSet rs2 = pstmt2.executeQuery ();
					while(rs2.next ())
					{
						el_detalle ="Producto: "+rs2.getString("producto")+" - Cantidad ordenada: "+rs2.getBigDecimal("qtyordered")+" - Valor Unitario: "+
						rs2.getBigDecimal("priceentered")+" "+order.getC_Currency().getISO_Code()+" \n";
					}
					el_detalle = el_detalle+"\n"+"Orden generada por "+user.getName()+" el "+BlumosUtilities.formatDateFull(DateUtils.now(), true);
					el_correo = "<pre>"+el_subjet+", Fecha de la Orden: "+BlumosUtilities.formatDate(order.getDateOrdered(), true)+
						", Fecha Prometida: "+BlumosUtilities.formatDate(order.getDatePromised(), true)+"\n"+
						"Cliente: "+order.getC_BPartner().getName().replace("'","''")+". Vendedor: "+order.getSalesRep().getName()+"\n"+
						"Descripción: "+desc+"\n \n"+"Detalle de la Orden: "+"\n"+el_detalle;
					id_correo =1000006;
					//String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),'"+BlumosUtilities.DameMail(user.get_ID(), 0,po.getCtx(),po.get_TrxName())+"'||,||DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subjet+"','"+el_correo+"')";
					String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subjet+"','"+el_correo+"</pre>')";
					PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
					pstmtSM.execute();
				}
			}
		}
		//correos de proyecto TR_BL_GPMUWORKFLOW
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==X_T_SEGUIMIENTO.Table_ID)  
		{
			X_T_SEGUIMIENTO seg = (X_T_SEGUIMIENTO) po;
			//trigger TR_BL_GPMuWORKFLOW
			MUser usuarioactivo = new MUser(po.getCtx(), Env.getAD_User_ID(po.getCtx()), po.get_TrxName());
			String destinos = "";
			String copiados = BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName());
			copiados = copiados+","+BlumosUtilities.DameMail(seg.getC_ProjectOFB().getSalesRep_ID(), 0, po.getCtx(), po.get_TrxName());
			copiados = copiados+","+BlumosUtilities.DameMail(seg.getC_ProjectOFB().getSalesRep().getSupervisor_ID(), 0, po.getCtx(), po.get_TrxName());
			copiados = copiados+","+BlumosUtilities.DameMail(seg.getC_ProjectOFB_ID(), 3, po.getCtx(), po.get_TrxName());
			int id_correo;
			String subject = "Proy. "+seg.getC_ProjectOFB().getValue();
			String mensaje = "";
			String desc = "";
			String casillas_activadas = "";
			if(seg.getDescription() != null && seg.getDescription().trim().length()>0)
				desc = seg.getDescription(); 
			//CODIGO PARA DESPACHO DE MUESTRAS
			if(seg.isSOLICITAR_MUESTRA() && !seg.isCORREO_ENVIADO())
			{
				if(seg.getQTY_DELIVERED().compareTo(Env.ZERO) <= 0)
					return "Debe ingresar un valor positivo en campo Cantidad";
				//determinacion origen muestra
				if(seg.isORIGEN_MUESTRA())
				{
					if(seg.getM_PriceList_ID().compareTo("1001715") != 0)
					{
						//bodega de muestras en ventas	
						subject = "Proy. "+seg.getC_ProjectOFB().getValue();
						subject = "FAVOR Despachar Muestra "+subject;
						mensaje = "<pre>"+subject+"\n\n"+"Se ha generado una solicitud de muestra para el Proyecto "+seg.getC_ProjectOFB().getValue()+"\n"+
							"Producto: "+seg.getM_Product().getName()+" Codigo: "+seg.getM_Product().getValue()+"\n"+"Cantidad: "+seg.getQTY_DELIVERED().setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",")+" "+
							seg.getC_UOM().getName()+"\n"+"Vendedor: "+seg.getC_ProjectOFB().getSalesRep().getName()+"\n"+"Comentarios: "+desc+
							"\n"+"Nombre del Cliente: "+seg.getC_ProjectOFB().getC_BPartner().getName()+
							"\n\n"+"Solicitud generada por "+usuarioactivo.getName();
						id_correo =1000010;
						//se sobreescribe asunto para pruebas 
						//subject = "NO CONSIDERAR 2. Prueba."+subject;
						String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),'"+copiados+"','cmendoza@blumos.cl','"+subject+"','"+mensaje+"</pre>')";
						PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
						pstmtSM.execute();
						seg.setCORREO_ENVIADO(true);
					}
					else
					{
						//bodega de muestras en ventas	
						subject = "Proy. "+seg.getC_ProjectOFB().getValue();
						subject = "FAVOR Despachar Muestra "+subject;
						mensaje = "<pre>"+subject+"\n\n"+"Se ha generado una solicitud de muestra para el Proyecto "+seg.getC_ProjectOFB().getValue()+"\n"+
							"Producto: "+seg.getM_Product().getName()+" Codigo: "+seg.getM_Product().getValue()+"\n"+"Cantidad: "+seg.getQTY_DELIVERED().setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",")+" "+
							seg.getC_UOM().getName()+"\n"+"Vendedor: "+seg.getC_ProjectOFB().getSalesRep().getName()+"\n"+"Comentarios: "+desc+
							"\n"+"Nombre del Cliente: "+seg.getC_ProjectOFB().getC_BPartner().getName()+
							"\n\n"+"Solicitud generada por "+usuarioactivo.getName();
						id_correo =1000048;
						//se sobreescribe asunto para pruebas 
						//subject = "NO CONSIDERAR. Prueba."+subject;
						String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),'"+copiados+"'||','||DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+subject+"','"+mensaje+"</pre>')";
						PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
						pstmtSM.execute();
						seg.setCORREO_ENVIADO(true);
					}
				}
				if(seg.isBODEGA_SOLUTEC() && !seg.isORIGEN_MUESTRA())
				{
					subject = "Proy. "+seg.getC_ProjectOFB().getValue();
					subject = "Solicitud Muestra "+subject;
					mensaje = "<pre>"+"Se ha generado una solicitud de muestra para el Proyecto "+seg.getC_ProjectOFB().getName().replace("'","''")+"\n"+
						"Producto: "+seg.getNOMBRE_MUESTRA()+"\n"+"Cantidad: "+seg.getQTY_DELIVERED().setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",")+" "+
						seg.getC_UOM().getName()+"\n"+"Vendedor: "+seg.getC_ProjectOFB().getSalesRep().getName()+
						"\n"+"Comentarios: "+desc+"\n\n"+"Solicitud generada por "+usuarioactivo.getName();					
						id_correo =1000049;
						//se sobreescribe asunto para pruebas 
						//subject = "NO CONSIDERAR. Prueba."+subject;
						String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),'"+copiados+"','cmendoza@blumos.cl','"+subject+"','"+mensaje+"</pre>')";
						PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
						pstmtSM.execute();
						seg.setCORREO_ENVIADO(true);
						seg.setProcessed(true);
				}
			}
			if(seg.isSOLICITAR_MUESTRA() && seg.isCORREO_ENVIADO() && seg.isDESPACHADO_BODEGA())
			{
				destinos = copiados;
				subject = "Proy. "+seg.getC_ProjectOFB().getValue();
				subject = "Muestra Preparada "+subject;
				mensaje = "<pre>"+subject+"\n\n"+"La Muestra solicitada ya ha sido preparada y se encuentra en bodega para su retiro. Proyecto: "+
					seg.getC_ProjectOFB().getName()+"\n"+"Producto: "+seg.getM_Product().getName()+" Codigo: "+seg.getM_Product().getValue()+
					"\n"+"Cantidad: "+seg.getQTY_DELIVERED().setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ",")+" "+seg.getC_UOM().getName()+"\n"+"Vendedor: "+
					seg.getC_ProjectOFB().getSalesRep().getName()+"\n"+"Comentarios: "+desc+"\n\n"+
					"Respuesta generada por "+usuarioactivo.getName();
				id_correo =1000011;
				//se sobreescribe asunto para pruebas 
				//subject = "NO CONSIDERAR. Prueba."+subject;
				String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+destinos+"',DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+subject+"','"+mensaje+"</pre>')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
				pstmtSM.execute();
				seg.setProcessed(true);
			}
			if((seg.isVISITA() || seg.isLLAMADA() || seg.isEMail()) 
					&& (seg.getDescription() != null || seg.getRESPUESTA() != null)
					&& !seg.isMotivo())
					
			{
				casillas_activadas = "Marco las casillas ";
				destinos = copiados;
				copiados = "";
				X_C_ProjectOFB proOFB = new X_C_ProjectOFB(po.getCtx(), seg.getC_ProjectOFB_ID(), po.get_TrxName());
				String id_solutec1 = proOFB.get_ValueAsString("ad_user_id_solu1");
				String id_solutec2 = proOFB.get_ValueAsString("ad_user_id_solu2");
				if((id_solutec1 != null && id_solutec1.trim().length() > 0)
						&& (id_solutec2 != null && id_solutec2.trim().length()>0))
				{
					copiados = BlumosUtilities.DameMail(Integer.parseInt(id_solutec1), 0, po.getCtx(), po.get_TrxName())+","+
								BlumosUtilities.DameMail(Integer.parseInt(id_solutec2), 0, po.getCtx(), po.get_TrxName())	;
				}
				if((id_solutec1 != null && id_solutec1.trim().length()>0) && (id_solutec2 == null || id_solutec2.trim().length()<1))
				{
					copiados = BlumosUtilities.DameMail(Integer.parseInt(id_solutec1), 0, po.getCtx(), po.get_TrxName());
				}
				if((id_solutec1 == null || id_solutec1.trim().length()<1) && (id_solutec2 != null && id_solutec2.trim().length()>0))
				{
					copiados = BlumosUtilities.DameMail(Integer.parseInt(id_solutec2), 0, po.getCtx(), po.get_TrxName());
				}
				subject = "Proy. "+seg.getC_ProjectOFB().getValue();
				subject = "Nuevo Comentario en "+subject;
				if(seg.isVISITA())
				{
					casillas_activadas = casillas_activadas+" *Visita* ";
				}
				if(seg.isLLAMADA())
				{
					casillas_activadas = casillas_activadas+" *Llamada* ";
				}
				if(seg.isEMail())
				{
					casillas_activadas = casillas_activadas+" *Email* ";
				}
				mensaje = "<pre>El usuario "+usuarioactivo.getName()+" ha ingresado un nuevo comentario en el Proyecto "+seg.getC_ProjectOFB().getName()+"\n"+
					casillas_activadas+"\n"+"Comentario: "+desc+"\n"+"Respuesta del Cliente: "+seg.getRESPUESTA()+
					"\n"+"Fecha: "+BlumosUtilities.formatDate(seg.getFECHA(), true);
				seg.setProcessed(true);
				seg.setCORREO_ENVIADO(true);
				id_correo =1000049;
				//se sobreescribe asunto para pruebas 
				//subject = "NO CONSIDERAR. Prueba. "+subject;
				String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+destinos+"','"+copiados+"','cmendoza@blumos.cl','"+subject+"','"+mensaje+"</pre>')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
				pstmtSM.execute();
				seg.setCORREO_ENVIADO(true);
				seg.setProcessed(true);
			}
		}		
		//correos de proyecto TR_BL_GPWORKFLOW
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==X_C_ProjectOFB.Table_ID)  
		{
			X_C_ProjectOFB proy = (X_C_ProjectOFB) po;			
			MUser usuarioactivo = new MUser(po.getCtx(), Env.getAD_User_ID(po.getCtx()), po.get_TrxName());
			String destino = "";
			String copiados = "";
			/*copiados = BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName());
			copiados = copiados+","+BlumosUtilities.DameMail(seg.getC_ProjectOFB().getSalesRep_ID(), 0, po.getCtx(), po.get_TrxName());
			copiados = copiados+","+BlumosUtilities.DameMail(seg.getC_ProjectOFB().getSalesRep().getSupervisor_ID(), 0, po.getCtx(), po.get_TrxName());
			copiados = copiados+","+BlumosUtilities.DameMail(seg.getC_ProjectOFB_ID(), 3, po.getCtx(), po.get_TrxName());*/
			//int id_correo;
			String subject = "";
			String mensaje = "";
			//String desc = "";
			destino= BlumosUtilities.DameMail(proy.getSalesRep_ID(), 0, po.getCtx(), po.get_TrxName())+","+
				BlumosUtilities.DameMail(proy.getSalesRep().getSupervisor_ID(), 0, po.getCtx(), po.get_TrxName())+","+
				BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName())+","+
				BlumosUtilities.DameMail(proy.get_ID(), 3, po.getCtx(), po.get_TrxName());
			// Control de Estados del Proyecto
			if(po.is_ValueChanged("projectstate"))
			{
				copiados = BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName());
				String id_solutec1 = proy.get_ValueAsString("ad_user_id_solu1");
				String id_solutec2 = proy.get_ValueAsString("ad_user_id_solu2");
				if(!BlumosUtilities.isStringNull(id_solutec1)
						&& !BlumosUtilities.isStringNull(id_solutec2))
				{
					copiados = BlumosUtilities.DameMail(Integer.parseInt(id_solutec1), 0, po.getCtx(), po.get_TrxName())+","+
							BlumosUtilities.DameMail(Integer.parseInt(id_solutec2), 0, po.getCtx(), po.get_TrxName()+","+
							BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName()))	;
				}
				else if(!BlumosUtilities.isStringNull(id_solutec1) && BlumosUtilities.isStringNull(id_solutec2))
				{
					copiados = BlumosUtilities.DameMail(Integer.parseInt(id_solutec1), 0, po.getCtx(), po.get_TrxName())+","+
						BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName())	;
				}
				else if(BlumosUtilities.isStringNull(id_solutec1) && !BlumosUtilities.isStringNull(id_solutec2))
				{
					copiados = BlumosUtilities.DameMail(Integer.parseInt(id_solutec2), 0, po.getCtx(), po.get_TrxName())+","+
					BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName())	;
				}
				subject = "Proyecto "+proy.getValue()+": Cambio ESTADO";
				String proyName = proy.getName();
				if(proy.getName() != null)
					proyName = proyName.replace("'","''");
				mensaje = "<pre>"+subject+"\n\n"+"Ha cambiado el ESTADO del proyecto: "+proyName+"\n\n"+
					"Cambio desde: "+proy.get_ValueOld("projectstate")+" a: *"+proy.get_Value("projectstate")+
					"*"+"\n\n"+"Registro generado por: *"+usuarioactivo.getName()+"*";
				//subject = "NO CONSIDERAR. Prueba. "+subject;
				String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+destino+"','"+copiados+"','cmendoza@blumos.cl','"+subject+"','"+mensaje+"</pre>')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
				pstmtSM.execute();
				
			}
			//Control de Etapas del Proyecto
			if(po.is_ValueChanged("etapa"))
			{
				copiados = BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName());
				String id_solutec1 = proy.get_ValueAsString("ad_user_id_solu1");
				String id_solutec2 = proy.get_ValueAsString("ad_user_id_solu2");
				if(!BlumosUtilities.isStringNull(id_solutec1)
						&& !BlumosUtilities.isStringNull(id_solutec2))
				{
					copiados = BlumosUtilities.DameMail(Integer.parseInt(id_solutec1), 0, po.getCtx(), po.get_TrxName())+","+
							BlumosUtilities.DameMail(Integer.parseInt(id_solutec2), 0, po.getCtx(), po.get_TrxName()+","+
							BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName()))	;
				}
				else if(!BlumosUtilities.isStringNull(id_solutec1) && BlumosUtilities.isStringNull(id_solutec2))
				{
					copiados = BlumosUtilities.DameMail(Integer.parseInt(id_solutec1), 0, po.getCtx(), po.get_TrxName())+","+
						BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName())	;
				}
				else if(BlumosUtilities.isStringNull(id_solutec1) && !BlumosUtilities.isStringNull(id_solutec2))
				{
					copiados = BlumosUtilities.DameMail(Integer.parseInt(id_solutec2), 0, po.getCtx(), po.get_TrxName())+","+
					BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName())	;
				}
				subject = "Proyecto "+proy.getValue()+": Cambio ETAPA";
				String proyName = proy.getName();
				if(proy.getName() != null)
					proyName = proyName.replace("'","''");
				mensaje = "<pre>"+subject+"\n\n"+"Ha cambiado la Etapa para el proyecto: "+proyName+"\n\n"+
					"Cambio desde: "+proy.get_ValueOld("etapa")+" a: *"+proy.get_Value("etapa")+
					"*"+"\n\n"+"Registro generado por: *"+usuarioactivo.getName()+"*";
				//subject = "NO CONSIDERAR. Prueba. "+subject;
				String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+destino+"','"+copiados+"','cmendoza@blumos.cl','"+subject+"','"+mensaje+"</pre>')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
				pstmtSM.execute();
			}
			//Control de cambios de Jefes de Solutec			
			if(po.is_ValueChanged("ad_user_id_solu1"))
			{				
				String soluOld = po.get_ValueOld("ad_user_id_solu1")==null?" ":po.get_ValueOld("ad_user_id_solu1").toString();				
				String soluNew = po.get_Value("ad_user_id_solu1")==null?" ":po.get_Value("ad_user_id_solu1").toString();
				String soluName1 = "";
				String soluName2 = "";
				copiados = BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName());
				subject = "Proyecto "+proy.getValue()+": Cambio Jefe SOLUTEC1";		
				//cambios de Jefes de Solutec	
				if(!BlumosUtilities.isStringNull(soluOld) && !BlumosUtilities.isStringNull(soluNew))
				{
					MUser userSolu1 = new MUser(po.getCtx(), Integer.parseInt(soluOld), po.get_TrxName());
					soluName1 = userSolu1.getName();					
					MUser userSolu2 = new MUser(po.getCtx(), Integer.parseInt(soluNew), po.get_TrxName());
					soluName2 = userSolu2.getName();
					copiados = copiados+","+BlumosUtilities.DameMail(Integer.parseInt(soluOld), 0, po.getCtx(), po.get_TrxName())+
							","+BlumosUtilities.DameMail(Integer.parseInt(soluNew), 0, po.getCtx(), po.get_TrxName());
					mensaje = "<pre>"+subject+"\n\n"+"Se ha cambiado al Jefe de Proyecto de Solutec1 para el proyecto "+proy.getValue()+":"+proy.getName().replace("'","''")+
					 "\n\n"+"Nuevo asignado: "+soluName2+". Antiguo asignado: "+soluName1+"\n\n"+
					 "Registro generado por: *"+usuarioactivo.getName();
				}
				//asigna un usuario solutec 1 nuevo
				if(BlumosUtilities.isStringNull(soluOld) && !BlumosUtilities.isStringNull(soluNew))
				{
					MUser userSoluNew = new MUser(po.getCtx(), Integer.parseInt(soluNew), po.get_TrxName());
					soluName2 = userSoluNew.getName();
					copiados = copiados+","+BlumosUtilities.DameMail(Integer.parseInt(soluNew), 0, po.getCtx(), po.get_TrxName());
					String proyName = proy.getName();
					if(proy.getName() != null)
						proyName = proyName.replace("'","''");
					mensaje = "<pre>"+subject+"\n\n"+"Se ha asignado al Jefe de Proyecto de Solutec1 para el proyecto "+proy.getValue()+":"+proyName+
					 "\n\n"+"Nuevo asignado: "+soluName2+"\n\n"+
					 "Registro generado por: *"+usuarioactivo.getName();
				}
				//subject = "NO CONSIDERAR. Prueba. "+subject;
				String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+destino+"','"+copiados+"','cmendoza@blumos.cl','"+subject+"','"+mensaje+"</pre>')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
				pstmtSM.execute();
			}
			
			// CONTROL CAMBIOS USUARIO JEFE DE SOLUTEC 2
			if(po.is_ValueChanged("ad_user_id_solu2"))
			{
				String soluOld = "";
				String soluNew = "";
				if(po.get_ValueOld("ad_user_id_solu2") != null)
					soluOld = po.get_ValueOld("ad_user_id_solu2").toString();				
				if(proy.get_Value("ad_user_id_solu2") != null)
					soluNew = proy.get_Value("ad_user_id_solu2").toString();
				String soluName1 = "";
				String soluName2 = "";
				copiados = BlumosUtilities.DameMail(usuarioactivo.get_ID(), 0, po.getCtx(), po.get_TrxName());
				subject = "Proyecto "+proy.getValue()+": Cambio Jefe SOLUTEC1";
				String proyName = proy.getName();
				if(proy.getName() != null)
					proyName = proyName.replace("'","''");
				//cambios de Jefes de Solutec	
				if(!BlumosUtilities.isStringNull(soluOld) && !BlumosUtilities.isStringNull(soluNew))
				{
					MUser userSolu1 = new MUser(po.getCtx(), Integer.parseInt(soluOld), po.get_TrxName());
					soluName1 = userSolu1.getName();					
					MUser userSolu2 = new MUser(po.getCtx(), Integer.parseInt(soluNew), po.get_TrxName());
					soluName2 = userSolu2.getName();
					copiados = copiados+","+BlumosUtilities.DameMail(Integer.parseInt(soluOld), 0, po.getCtx(), po.get_TrxName())+
							","+BlumosUtilities.DameMail(Integer.parseInt(soluNew), 0, po.getCtx(), po.get_TrxName());
					mensaje = "<pre>"+subject+"\n\n"+"Se ha cambiado al Jefe de Proyecto de Solutec1 para el proyecto "+proy.getValue()+":"+proyName+
					 "\n\n"+"Nuevo asignado: "+soluName2+". Antiguo asignado: "+soluName1+"\n\n"+
					 "Registro generado por: *"+usuarioactivo.getName();
				}
				//asigna un usuario solutec 1 nuevo
				if(BlumosUtilities.isStringNull(soluOld) && !BlumosUtilities.isStringNull(soluNew))
				{
					MUser userSoluNew = new MUser(po.getCtx(), Integer.parseInt(soluNew), po.get_TrxName());
					soluName2 = userSoluNew.getName();
					copiados = copiados+","+BlumosUtilities.DameMail(Integer.parseInt(soluNew), 0, po.getCtx(), po.get_TrxName());
					mensaje = "<pre>"+subject+"\n\n"+"Se ha asignado al Jefe de Proyecto de Solutec1 para el proyecto "+proy.getValue()+":"+proyName+
					 "\n\n"+"Nuevo asignado: "+soluName2+"\n\n"+
					 "Registro generado por: *"+usuarioactivo.getName();
				}
				//subject = "NO CONSIDERAR. Prueba. "+subject;
				String sendMail = "SELECT send_mail('adempiere@blumos.cl','"+destino+"','"+copiados+"','cmendoza@blumos.cl','"+subject+"','"+mensaje+"</pre>')";
				PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
				pstmtSM.execute();
			}
		}
		
		
		
		return null;
	}	//	modelChange

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
		//TRIGGER TR_BL_CORRIGE_RESERVADO
		if((timing == TIMING_BEFORE_COMPLETE || timing == TIMING_BEFORE_VOID) && po.get_Table_ID()==MInOut.Table_ID )  
		{
			MInOut inOut = (MInOut) po;
			String desc = " ";
			if(inOut.getDescription() != null 
					&& inOut.getDescription().trim().length()>0)
				desc = inOut.getDescription();
			MUser user = new MUser(po.getCtx(), Env.getAD_User_ID(po.getCtx()), po.get_TrxName());
			String el_subject ="";
			String el_detalle = "";
			String el_correo ="";
			int id_correo = 0;
			//PROCEDIMIENTO PARA GENERAR CORREO DE NOTIFICACION POR CADA DESPACHO SOLUTEC
			if(inOut.getAD_Client_ID() == 1000005 && inOut.isSOTrx())
			{
				String v_query = "Select mp.m_product_id, mp.name as producto, mil.movementqty" +
						" from m_inoutline mil" +
						" inner join m_product mp ON (mil.m_product_id=mp.m_product_id)" +
						" where mil.m_inout_id="+inOut.get_ID();
				el_subject ="Despacho Solutec "+inOut.getDocumentNo();
				try 
				{
					PreparedStatement pstmt = null;
					pstmt = DB.prepareStatement (v_query, null);
					ResultSet rs = pstmt.executeQuery();
					int v_control = 0;
					while(rs.next ())
					{
						v_control++;
						el_detalle = el_detalle +"Producto: "+rs.getString("producto")+
							" - Cantidad despachada: "+rs.getBigDecimal("movementqty")+"\n";
					}
					el_detalle = el_detalle+"\n"+"Despacho generado por "+user.getName()+" el "+DateUtils.today().toString();
					el_correo = "<pre>"+el_subject+", Fecha de Despacho: "+inOut.getMovementDate()+"\n"+
						"Cliente: "+inOut.getC_BPartner().getName()+"\n"+
						"Descripcion: "+desc+"\n\n"+"Detalle del Despacho: "+"\n"+el_detalle;
					if(v_control > 0)
					{
						id_correo = 1000008;
						String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subject+"','"+el_correo+"</pre>')";
						PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
						pstmtSM.execute();
					}
						
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}					
			if((inOut.getAD_Client_ID() == 1000000 || inOut.getAD_Client_ID() == 1000001) && !inOut.isSOTrx())
			{
				String v_query = "Select mp.m_product_id, mp.name as producto, mil.movementqty" +
						" from m_inoutline mil" +
						" inner join m_product mp ON (mil.m_product_id=mp.m_product_id)" +
						" where mil.m_inout_id="+inOut.get_ID();
				el_subject ="RECIBO "+inOut.getDocumentNo();
				try 
				{
					PreparedStatement pstmt = null;
					pstmt = DB.prepareStatement (v_query, null);
					ResultSet rs = pstmt.executeQuery();
					el_detalle = "";
					el_detalle = el_detalle+"CARPETA "+inOut.getC_Order().getDocumentNo()+"\n";
					int v_control = 0;
					while(rs.next ())
					{
						v_control++;
						el_detalle = el_detalle +"Producto: "+rs.getString("producto")+
							" - Cantidad Recibida: "+rs.getBigDecimal("movementqty")+"\n";
					}
					el_detalle = el_detalle+"\n"+"Despacho generado por "+user.getName()+" el "+DateUtils.today().toString();
					el_correo = "<pre>"+el_subject+", Fecha de Despacho: "+inOut.getMovementDate()+"\n"+
						"Cliente: "+inOut.getC_BPartner().getName()+"\n"+
						"Descripcion: "+desc+"\n\n"+"Detalle del Despacho: "+"\n"+el_detalle;
					if(v_control > 0)
					{
						id_correo = 1000009;
						String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subject+"','"+el_correo+"</pre>')";
						PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
						pstmtSM.execute();
					}
						
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//trigger TR_BL_TRASPASOS
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MMovement.Table_ID )  
		{
			MMovement mov = (MMovement) po;
			if(mov.getAD_Client_ID() == 1000005)
			{
				int id_correo =0;
				String desc = " ";
				if(mov.getDescription() != null && mov.getDescription().trim().length() > 0)
					desc = mov.getDescription();
				MUser user = new MUser(po.getCtx(), Env.getAD_User_ID(po.getCtx()), po.get_TrxName());
				String v_query = "Select mp.m_product_id, mp.name as producto, mp.solutec, mml.movementqty, mml.m_locator_id, mml.m_locatorto_id, mml.m_attributesetinstance_id, mml.line" +
						" FROM m_movementline mml" +
						" INNER JOIN m_product mp ON (mml.m_product_id=mp.m_product_id)" +
						" WHERE mml.m_movement_id= "+mov.get_ID()+" order by mml.line";
				String el_subject ="Traspaso N° "+mov.getDocumentNo();
				try 
				{
					PreparedStatement pstmt = null;
					pstmt = DB.prepareStatement (v_query, null);
					ResultSet rs = pstmt.executeQuery();
					String el_detalle = "";
					int v_control = 0;
					while(rs.next ())
					{
						v_control++;
						String v_ubicacion = DB.getSQLValueString(po.get_TrxName(), "select value from m_locator where m_locator_id="+rs.getInt("m_locator_id")); 
						String v_ubicacionTO = DB.getSQLValueString(po.get_TrxName(), "select value from m_locator where m_locator_id="+rs.getInt("m_locatorto_id"));
			            String el_lote = DB.getSQLValueString(po.get_TrxName(), "select LOT from m_attributesetinstance where m_attributesetinstance_id="+rs.getInt("m_attributesetinstance_id"));
						
						el_detalle = el_detalle+"Línea: "+rs.getString("line")+" - Desde: "+v_ubicacion+" - Hacia -->: "+v_ubicacionTO+"\n"+
							"Producto: "+rs.getString("name")+" - Lote: "+el_lote+" - Cantidad: "+rs.getBigDecimal("movementqty").toString()+"\n\n";
							el_detalle = el_detalle +"Producto: "+rs.getString("producto")+
							" - Cantidad Recibida: "+rs.getBigDecimal("movementqty")+"\n";
					}
					el_detalle =el_detalle+"\n"+"Movimiento generado por "+user.getName()+" el "+DateUtils.today().toString();
					String el_correo="<pre>"+el_subject+", Fecha: "+mov.getMovementDate()+"\n"+
				      	"Descripción: "+desc+"\n\n"+"Detalle del Traspaso: "+"\n\n"+el_detalle;					
					if(v_control > 0)
					{
						id_correo = 1000015;
						String sendMail = "SELECT send_mail('adempiere@blumos.cl',DAMECORREO("+id_correo+",'to'),damemail("+user.get_ID()+",0)||','||DAMECORREO("+id_correo+",'cc'),'cmendoza@blumos.cl','"+el_subject+"','"+el_correo+"</pre>')";
						PreparedStatement pstmtSM = DB.prepareStatement (sendMail, null);
						pstmtSM.execute();
					}
						
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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


	

	

}	