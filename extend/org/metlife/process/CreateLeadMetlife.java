/******************************************************************************
0 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.metlife.process;

import org.adempiere.process.ImportProcess;
import org.compiere.model.MBPartner;
import org.compiere.model.X_C_Campaign;
import org.compiere.model.X_C_CampaignFollow;
import org.compiere.model.X_I_BPartnerIM;
import org.compiere.model.X_R_ContactInterest;
import org.compiere.model.X_R_InterestArea;
import org.compiere.model.X_R_InterestAreaValues;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
 
/**
 *	
 *  @author ininoles
 *  @version $Id: CreateLeadMetlife.java,v 1.2 2009/04/17 00:51:02 faaguilar$
 */
public class CreateLeadMetlife extends SvrProcess
implements ImportProcess
{
	
	//private int p_PInstance_ID;	
	private int	m_AD_Client_ID = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	private int p_I_BPartnerIM_ID = 0;
	
	protected void prepare()
	{
		p_I_BPartnerIM_ID = getRecord_ID();		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws java.lang.Exception
	{	
		X_I_BPartnerIM im = new X_I_BPartnerIM(getCtx(), p_I_BPartnerIM_ID, get_TrxName());
		int id_bp = 0;
		if (im.getRUT_SOL() == null || im.getRUT_SOL() == "" || im.getRUT_SOL() == " ")
		{
			im.setRUT_SOL(Integer.toString(im.get_ID()));
			im.save();
		}		
		int id_bpSql = DB.getSQLValue(get_TrxName(), "SELECT C_Bpartner_ID FROM C_Bpartner WHERE value like '"+im.getRUT_SOL()+"'");		
		if(id_bpSql > 0)
			id_bp = id_bpSql;
		else
			id_bp = 0;
		//creacion del BP
		MBPartner bp = new MBPartner(getCtx(), id_bp, get_TrxName());
		bp.setValue(im.getRUT_SOL());		
		if (im.getNOM_SOL() != null && im.getAPE_PAT() != null && im.getAPE_MAT() != null)
			bp.setName(im.getNOM_SOL().concat(" ").concat(im.getAPE_PAT().concat(" ").concat(im.getAPE_MAT())));
		else if (im.getNOM_SOL() != null && im.getAPE_PAT() != null)
			bp.setName(im.getNOM_SOL().concat(" ").concat(im.getAPE_PAT()));
		else if (im.getNOM_SOL() != null)
			bp.setName(im.getNOM_SOL());
		else
			bp.setName("Desconocido");
		int id_groupBP = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_BP_Group_ID) FROM C_BP_Group WHERE IsActive = 'Y' AND IsDefault = 'Y'");
		if (id_groupBP < 1)
			id_groupBP = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_BP_Group_ID) FROM C_BP_Group WHERE IsActive = 'Y'");
		if (id_groupBP < 1)
			id_groupBP = 1000000;
		bp.setC_BP_Group_ID(id_groupBP);
		bp.save();
		
		//actualizacion de campos del ingreso 
		im.setORI_SOL("ADempiere");
		im.setFILLER1("Ingreso Manual");
		String nameCampaign = DB.getSQLValueString(get_TrxName(),"SELECT MAX(Name) FROM AD_Ref_List " +
				" WHERE AD_Reference_ID=1000050 AND VALUE = '"+im.getLEADTYPE()+"' ");
		im.setFILLER2(nameCampaign);
		im.setFILLER3(im.get_ID());
		im.setFEC_CRE(im.getCreated());
		im.set_CustomColumn("C_BPartner_ID",bp.get_ID());
		im.save();
		
		//interest area metlife
		//Creamos o seteamos area de interes		
		//TelFijo			
		String namesAInterest[] = {"NOM_SOL","APE_PAT","APE_MAT","RUT_SOL","TEL_FIJO","TEL_CEL","DIR_SOL",
				"COM_SOL","CIU_SOL","REG_SOL","DIR_EML","FONO_AREA","FEC_NAC","SEXO","EST_CIVIL","PROD_INT1",
				"PROD_INT2","OBS_SOL","SOL_INM_SOL","SOL_TIP_PROP","SOL_VAL_PROP","SOL_MON_CRED","SOL_PORC_FIN",
				"RAZON_SOC","EMP_RUT","EMP_FONO_AREA","EMP_FONO_NUM","EMP_REG","EMP_CIU","EMP_COM","EMP_DIR_CALLE",
				"COLAB","CORR_SEG","ORI_SOL","FILLER1","FILLER2","FILLER3","FEC_CRE"};
		
		String namesAInterestTrd[] = {"Nombres","Apellido Paterno","Apellido Materno","Rut","Telefono Fijo","Telefono Celular","Direccion",
				"Comuna","Ciudad","Region","Email","Codigo de Area","Fecha Nacimiento","Sexo","Estado Civil","Producto de Interes 1",
				"Producto de Interes 2","Observacion","Inmobiliaria","Tipo de Propiedad","Valor Propiedad","Monto Credito","% Financiamiento",
				"Razon Social","Rut Empresa","Codigo Area Empresa","Telefono Fijo Empresa","Region Empresa","Ciudad Empresa","Comuna Empresa","Direccion Empresa",
				"Cantidad Colaboradores","Corredor de Seguro","Origen","Etiqueta","Valor Etiqueta","ID","Fecha Creacion"};
	
		for(int i=0;i<namesAInterest.length;i++)
		{
			X_R_InterestArea IArea = null;
			String nameIA = namesAInterest[i];
			String nameIATrd = namesAInterestTrd[i];
			String sqlIA1 = "Select R_InterestArea_ID from R_InterestArea WHERE name = '"+nameIATrd+"'";
			int IA_ID = DB.getSQLValue(get_TrxName(), sqlIA1);
			if (IA_ID > 0 )
			{
				IArea = new X_R_InterestArea(getCtx(), IA_ID, get_TrxName());
			}
			else
			{
				IArea = new X_R_InterestArea(getCtx(), 0, get_TrxName());
				IArea.setName(nameIATrd);
				IArea.setValue(nameIATrd);
			}
			IArea.save();
			//seteamos o creamo valor del area de interest
			X_R_InterestAreaValues IAreaValue= null;
			String sqlVIA1 = "SELECT R_InterestAreaValues_ID FROM R_InterestAreaValues " +
					"WHERE R_InterestArea_ID = "+IArea.get_ID()+" AND value = '"+im.get_ValueAsString(nameIA).replace("'","")+"'";
			int IAV_ID = DB.getSQLValue(get_TrxName(), sqlVIA1);
			if (IAV_ID > 0)
			{
				IAreaValue = new X_R_InterestAreaValues(getCtx(), IAV_ID, get_TrxName());
			}
			else
			{
				if (im.get_ValueAsString(nameIA) == null || im.get_ValueAsString(nameIA) == "" || im.get_ValueAsString(nameIA) == " ")
				{
					;
				}
				else
				{
					IAreaValue = new X_R_InterestAreaValues(getCtx(), 0, get_TrxName());
					IAreaValue.setR_InterestArea_ID(IArea.get_ID());
					IAreaValue.setValue(im.get_ValueAsString(nameIA));
					String valueEtiquetaRM = im.get_ValueAsString(nameIA).toLowerCase();							
					if (IArea.getValue().toLowerCase().contains("región") || IArea.getValue().toLowerCase().contains("region"))
					{
						if(valueEtiquetaRM.contains("metropo"))
						{
							IAreaValue.set_CustomColumn("IsValueRM", true);
						}
					}
					IAreaValue.save();
				}
			}
			//creamos la asignacion del area de interes con el socio de negocio
			if (im.get_ValueAsString(nameIA) == null || im.get_ValueAsString(nameIA) == "" || im.get_ValueAsString(nameIA) == " ")
			{
				;
			}
			else
			{
				if (IArea.get_ValueAsBoolean("IsEditable"))
				{
					X_R_ContactInterest cInterest = null;
					String sqlcInteret = "SELECT MAX(R_ContactInterest_ID) FROM R_ContactInterest WHERE C_BPartner_ID = "+bp.get_ID()+
							" AND R_InterestArea_ID = "+IArea.get_ID();
					int CInterest_ID = DB.getSQLValue(get_TrxName(), sqlcInteret);
					if (CInterest_ID > 0)
					{
						cInterest = new X_R_ContactInterest(getCtx(), CInterest_ID, get_TrxName());
						cInterest.set_CustomColumn("R_InterestAreaValues_ID", IAreaValue.get_ID());						
					}else
					{
						cInterest = new X_R_ContactInterest(getCtx(), 0, get_TrxName());
						cInterest.set_CustomColumn("C_BPartner_ID", bp.get_ID());
						cInterest.setR_InterestArea_ID(IArea.get_ID());
						cInterest.set_CustomColumn("R_InterestAreaValues_ID", IAreaValue.get_ID());											
					}					
					cInterest.save();
				}else
				{
					if (IArea.get_ID() > 0 && IAreaValue.get_ID() > 0)
					{
						X_R_ContactInterest cInterest = new X_R_ContactInterest(getCtx(), 0, get_TrxName());
						cInterest.set_CustomColumn("C_BPartner_ID", bp.get_ID());
						cInterest.setR_InterestArea_ID(IArea.get_ID());
						cInterest.set_CustomColumn("R_InterestAreaValues_ID", IAreaValue.get_ID());
						cInterest.save();						
					}
				}
			}
		}
		commitEx();
		int id_Campa = 0;
		if(im.getLEADTYPE().compareTo("AG") == 0)
		{
			id_Campa = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Campaign_ID) FROM C_Campaign WHERE upper(name) like '%AGENCIA%'");
		}
		else if(im.getLEADTYPE().compareTo("CC") == 0)
		{
			id_Campa = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Campaign_ID) FROM C_Campaign WHERE upper(name) like '%CONSUMO%'");
		}
		else if(im.getLEADTYPE().compareTo("DD") == 0)
		{
			id_Campa = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Campaign_ID) FROM C_Campaign WHERE upper(name) like '%DTC%'");
		}
		else if(im.getLEADTYPE().compareTo("RV") == 0)
		{
			id_Campa = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Campaign_ID) FROM C_Campaign WHERE upper(name) like '%VITALICIA%'");
		}
		else if(im.getLEADTYPE().compareTo("SC") == 0)
		{
			id_Campa = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Campaign_ID) FROM C_Campaign WHERE upper(name) like '%COLECTIVOS%'");
		}
		else if(im.getLEADTYPE().compareTo("MU") == 0)
		{
			id_Campa = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Campaign_ID) FROM C_Campaign WHERE upper(name) like '%MUTUARIA%'");
		}
		else
		{
			id_Campa = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_Campaign_ID) FROM C_Campaign WHERE IsCampaignDefault='Y'");
		}		
		im.setC_Campaign_ID(id_Campa);
		im.save();
		X_C_Campaign cam = new X_C_Campaign(getCtx(), id_Campa, get_TrxName());
		X_C_CampaignFollow cFollow = new X_C_CampaignFollow(getCtx(), 0, get_TrxName());
		cFollow.setAD_Org_ID(cam.getAD_Org_ID());
		cFollow.setC_Campaign_ID(cam.get_ID());
		cFollow.setIsActive(true);
		cFollow.setC_BPartner_ID(bp.get_ID());
		cFollow.setStatus("NC");
		cFollow.setFinalStatus("IN");
		if (im.get_ValueAsInt("AD_User_ID") > 0)
		{
			cFollow.setAD_User_ID(im.get_ValueAsInt("AD_User_ID"));
			cFollow.setFinalStatus("AS");
		}
		else
		{
			cFollow.setAD_User_ID(Env.getAD_User_ID(getCtx()));
			cFollow.setFinalStatus("AS");
		}
		cFollow.save();		
		commitEx();
		im.set_CustomColumn("Processed", true);		
		im.save();
		return "OK";
	}	//	doIt
	
	public String getWhereClause()
	{
		return " AND AD_Client_ID=" + m_AD_Client_ID;
	}
	public String getImportTableName()
	{
		return X_I_BPartnerIM.Table_Name;
	}	
}	//	OrderOpen

