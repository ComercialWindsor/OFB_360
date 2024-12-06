package org.ofb.model;

import org.compiere.util.DB;

public class OFBForward {
	
	
	public static boolean OrderUNAB()
	{
		String OrderUNAB = "N";
		try 
		{
			OrderUNAB = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UNAB_ValidatorOrder'");
			if(OrderUNAB == null)	
				OrderUNAB = "N";
		}
		catch (Exception e)
		{
			OrderUNAB = "N";
		}
		return OrderUNAB.equals("Y");
	}	
	public static boolean GenerateXMLMinOut()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_GenerateXMLMinOut'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean GenerateXMLMinOutFel()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_GenerateXMLMinOutFEL'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean GenerateXMLMinOutCGProvectis()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_GenerateXMLMinOutCGProvectis'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static String RutEmpresaFEL()
	{
		String rutEmp = "1";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_RutEmpresaFEL'");
			if(rutEmp == null)	
				rutEmp = "1";
		}
		catch (Exception e)
		{
			rutEmp = "1";
		}
		return rutEmp;
	}
	public static String RutEmpresaFELOrg(int ID_Org)
	{
		String rutEmp = "0";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_RutEmpresaFEL' AND AD_Org_ID = "+ID_Org);
			if(rutEmp == null)	
				rutEmp = "0";
		}
		catch (Exception e)
		{
			rutEmp = "0";
		}
		return rutEmp;
	}
	public static String RutUsuarioFEL()
	{
		String rutEmp = "1";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_RutUsuarioFEL'");
			if(rutEmp == null)	
				rutEmp = "1";
		}
		catch (Exception e)
		{
			rutEmp = "1";
		}
		return rutEmp;
	}
	public static String RutUsuarioFELOrg(int ID_Org)
	{
		String rutEmp = "0";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_RutUsuarioFEL' AND AD_Org_ID = "+ID_Org);
			if(rutEmp == null)	
				rutEmp = "0";
		}
		catch (Exception e)
		{
			rutEmp = "0";
		}
		return rutEmp;
	}
	public static String ContrasenaFEL()
	{
		String rutEmp = "1";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_ContraseñaFEL'");
			if(rutEmp == null)	
				rutEmp = "1";
		}
		catch (Exception e)
		{
			rutEmp = "1";
		}
		return rutEmp;
	}
	public static String ContrasenaFELOrg(int ID_Org)
	{
		String rutEmp = "0";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_ContraseñaFEL' AND AD_Org_ID = "+ID_Org);
			if(rutEmp == null)	
				rutEmp = "0";
		}
		catch (Exception e)
		{
			rutEmp = "0";
		}
		return rutEmp;
	}
	public static boolean ValidatorRequisitionTSM()
	{
		String flag = "N";
		try 
		{
			flag = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_TSM_ValidatorRequisition'");
			if(flag == null)	
				flag = "N";
		}
		catch (Exception e)
		{
			flag = "N";
		}
		return flag.equals("Y");
	}	
	public static String PathBatIMacro()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_RutaBatImacro' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static String PathBatIMacroNM()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_RutaBatImacroNM' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static String PathDataIMacro()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_RutaDataImacro' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static String PathDeleteDataIMacro()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_RutaDeleteDataIMacro' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static boolean NoExplodeBOMOrder()
	{
		String ExplodeBOM = "N";
		try 
		{
			ExplodeBOM = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoExplodeBOM_Order'");
			if(ExplodeBOM == null)	
				ExplodeBOM = "N";
		}
		catch (Exception e)
		{
			ExplodeBOM = "N";
		}
		return ExplodeBOM.equals("Y");
	}
	public static boolean NoValidationLineOrderRep()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoValidationLineOrder'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NoValidationPriceOrderLineZero()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoValidationPriceOrderLineZero'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NoValidationPriceInvoiceLineZero()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoValidationPriceInvoiceLineZero'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NewSQLBtnHistory()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NewSQLBtnHistory'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}	
	public static boolean NewSQLBtnHistoryProduct()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NewSQLBtnHistoryProduct'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}	
	public static boolean DateSalesDocumentFromOrderPA()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_DateSalesDocumentFromOrder'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}	
	public static boolean NewDescriptionInvoiceGenPA()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NewDescriptionInvoiceGenPA'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NewUpdateMantainceDetailTSM()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NewUpdateMantainceDetailTSM'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NewUpdateMaintainceParent()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NewUpdateMaintainceParent'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean PayRequestLineValidInvoice()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_PayRequestLineValidInvoice'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean PayRequestUseJLineDate()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_PayRequestUseJLineDate'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean PayRequestNotInCashLine()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_PayRequestNotInCashLine'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean PayRequestFilterDateTrxLine()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_PayRequestFilterDateTrxLine'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean PayRequestNotClosedJournal()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_PayRequestNotClosedJournal'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean PayRequestSelectedAccount()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_PayRequestSelectedAccount'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static String PathDataIMacroCD4()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_PathDataIMacroCD4' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static String PathDataOutIMacroCD4()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_PathDataOutIMacroCD4' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static String PathBatIMacroCD4()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_RutaBatImacroCD4' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static String PathBatIMacroCD4NM()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_RutaBatImacroCD4NM' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static String PathFileBCICD4()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_PathFileBCICD4' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static boolean UseRequisitionForSaleWINDSOR()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UseRequisitionForSaleWINDSOR'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	
	//mfrojas validar stock blumos
	public static boolean ValidatorStockBlumos(int ad_client)
	{
		String flag = "N";
		try 
		{
			flag = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_VALIDATORSTOCKBLUMOS' and ad_client_id = "+ad_client);
			if(flag == null)	
				flag = "N";
		}
		catch (Exception e)
		{
			flag = "N";
		}
		return flag.equals("Y");
	}	
	public static boolean UpdateIVAPA()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UpdateIVAPA'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean UseWindsorSalesReq()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UseWindsorSalesReq'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	//mfrojas validar stock blumos
	public static boolean ValidatorStockBlumos()
	{
		String flag = "N";
		try 
		{
			flag = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_VALIDATORSTOCKBLUMOS'");
			if(flag == null)	
				flag = "N";
		}
		catch (Exception e)
		{
			flag = "N";
		}
		return flag.equals("Y");
	}
	public static boolean InvoiceTaxPA()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_InvoiceTaxPA'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}	
	public static boolean UseDateOrderForCostRM()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UseDateOrderForCostRM'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static String PathFileSafePicking()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_PathFileSafePicking' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static boolean UseCorrelativeAPForDocType()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UseCorrelativeAPForDocType'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean AlwaysInvoiceTaxCalculate()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_AlwaysInvoiceTaxCalculate'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean AllocationUseActualDate()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_AllocationUseActualDate'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean AllocationUsePayDate()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_AllocationUsePayDate'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean ValidationLineOrderRepWindsor()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoValidationLineOrder'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean UseOnlyCashForBBalance()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UseOnlyCashForBBalance'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean UseInfoProductTCInvoice()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UseInfoProductTCInvoice'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean UseDocNoVoidTSM()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UseDocNoVoidTSM'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean WarningMultipleAllocation()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_WarningMultipleAllocation'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean createFromInvSinceTwoMonth()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_createFromInvSinceTwoMonth'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean createFromInvCopySalesRep()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_createFromInvCopySalesRep'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean produccionMINA()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_produccionMINA'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static String PathFileTSMSAMOA()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_PathFileTSMSAMOA' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static String TSMRutaDriversControl()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_CencosudRutaStock' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	
	public static boolean WindsorUseCashBook()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_WindsorUseCashBook'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NoUpdateCreditWindsor()
	{
		String cadena = "N";
		try 
		{
			cadena = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoUpdateCreditWindsor'");
			if(cadena == null)	
				cadena = "N";
		}
		catch (Exception e)
		{
			cadena = "N";
		}
		return cadena.equals("Y");
	}
	public static boolean AllocationUseDateInvoice()
	{
		String cadena = "N";
		try 
		{
			cadena = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_AllocationUseDateInvoice'");
			if(cadena == null)	
				cadena = "N";
		}
		catch (Exception e)
		{
			cadena = "N";
		}
		return cadena.equals("Y");
	}
	public static boolean usePaymentOncePR()
	{
		String cadena = "N";
		try 
		{
			cadena = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_usePaymentOncePR'");
			if(cadena == null)	
				cadena = "N";
		}
		catch (Exception e)
		{
			cadena = "N";
		}
		return cadena.equals("Y");
	}
	public static String getDateVAllocation()
	{
		String cadena = "DueDate";
		try 
		{
			cadena = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_getDateVAllocation'");
			if(cadena == null)	
				cadena = "DueDate";
		}
		catch (Exception e)
		{
			cadena = "DueDate";
		}
		return cadena;
	}
	public static boolean NoValidateStockShipment()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoValidateStockShipment'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NoValidateOrderShipment()
	{
		String cadena = "N";
		try 
		{
			cadena = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoValidateOrderShipment'");
			if(cadena == null)	
				cadena = "N";
		}
		catch (Exception e)
		{
			cadena = "N";
		}
		return cadena.equals("Y");
	}
	public static boolean UseRequisitionECommerce()
	{
		String cadena = "N";
		try 
		{
			cadena = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UseRequisitionECommerce'");
			if(cadena == null)	
				cadena = "N";
		}
		catch (Exception e)
		{
			cadena = "N";
		}
		return cadena.equals("Y");
	}
	public static boolean NoValidationReqNoLine()
	{
		String cadena = "N";
		try 
		{
			cadena = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoValidationReqNoLine'");
			if(cadena == null)	
				cadena = "N";
		}
		catch (Exception e)
		{
			cadena = "N";
		}
		return cadena.equals("Y");
	}
	public static boolean ValidStockMaterialReq()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_ValidStockMaterialReq'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
}

