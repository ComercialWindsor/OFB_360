package org.safeenergy.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;

import org.compiere.model.MOrder;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.ofb.model.OFBForward;

public class GenerateFilePicking  extends SvrProcess
{
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	int ID_Order = 0;
	protected void prepare()
	{
		ID_Order =  getRecord_ID();
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message 
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MOrder order = new MOrder(getCtx(), ID_Order, get_TrxName());
		String filePath = OFBForward.PathFileSafePicking();
		String infoLine = "";
		infoLine = order.getDocumentNo()+";";
		infoLine = infoLine+order.getPOReference()+";";
		infoLine = infoLine+order.getC_BPartner().getName()+";";
		String via_entrega = DB.getSQLValueString(get_TrxName(), "SELECT rlt.name FROM AD_Ref_List rl " +
				" INNER JOIN AD_Ref_List_Trl rlt ON (rl.AD_Ref_List_ID = rlt.AD_Ref_List_ID AND AD_Language='es_CL') " +
				" WHERE rl.AD_Reference_ID=152 AND rl.value = '"+order.getDeliveryViaRule()+"'")		;
		infoLine = infoLine+via_entrega+";";
		infoLine = infoLine+order.getC_BPartner_Location().getC_Location().getAddress1()+";";
		infoLine = infoLine+order.getDescription()+";";
		Calendar today = Calendar.getInstance();
		infoLine = infoLine+today.get(Calendar.DAY_OF_MONTH)+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR)+";";
		infoLine = infoLine+today.get(Calendar.HOUR_OF_DAY)+":"+today.get(Calendar.MINUTE);
		//Escritura
		File filePicking = new File(filePath);
		BufferedWriter bw;
		if(!filePicking.exists()) 
		{
		      bw = new BufferedWriter(new FileWriter(filePicking));
		      bw.write("N_Picking;Ref_OC;Nombre_Cliente;Via_entrega;Dir_cliente;Observacion;fecha;hora");
		      bw.newLine();
		      bw.write(infoLine);
		}else 
		{
		      bw = new BufferedWriter(new FileWriter(filePicking, true));	
		      bw.newLine();
		      bw.write(infoLine);
		}
		bw.close();	
		return "Picking Agregado";
	}	//	doIt

	
	
}
