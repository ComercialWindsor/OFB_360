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
package org.compiere.process;

import java.sql.*;
import java.util.Calendar;
import java.util.logging.*;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.util.*;
import org.ofb.model.OFBForward;

/**
 *	
 *	
 *  @author Fabian Aguilar -faaguilar
 *  @version $Id: MPAssetPlanning.java,v 1.2 2010/12/15 00:51:01  $
 */
public class MPAssetPlanning extends SvrProcess
{
	int periodNo=0;
	private int p_PInstance_ID;
	String stringDate;
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
			else if (name.equals("PeriodNo"))
				periodNo = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		p_PInstance_ID = getAD_PInstance_ID();
		
		if(DB.isOracle())
			stringDate="sysdate";
		else
			stringDate="now()";
			
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if(periodNo <= 0)
			  throw new AdempiereException("No se puede ejecutar proceso para 0 periodos");
		
		DB.executeUpdate("DELETE FROM MP_Prognosis", get_TrxName());
		
		//searching by calendar
		StringBuffer byCalendar = new StringBuffer();
		byCalendar.append("SELECT MA.AD_CLIENT_ID,MA.AD_ORG_ID,MA.MP_MAINTAIN_ID,MA.DESCRIPTION,MA.PROGRAMMINGTYPE, A.A_ASSET_ID,DATENEXTRUN ,MA.DATELASTRUN,MA.INTERVAL, MA.MP_MAINTAINDetail_ID");
		byCalendar.append(" FROM MP_MAINTAINDetail MA");
		byCalendar.append(" INNER JOIN A_ASSET A ON (MA.A_ASSET_ID=A.A_ASSET_ID)");
		if(DB.isOracle())
			byCalendar.append(" WHERE PROGRAMMINGTYPE='C' AND DATENEXTRUN BETWEEN SYSDATE-60 AND SYSDATE+(7*"+ periodNo+")");
		else
			byCalendar.append(" WHERE PROGRAMMINGTYPE='C' AND DATENEXTRUN BETWEEN now()-60 AND now()+(7*"+ periodNo+")");
		byCalendar.append(" AND MA.DOCSTATUS<>'IT' Order by DATENEXTRUN asc");
		
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(byCalendar.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Calendar datefrom = Calendar.getInstance();
				Calendar dateto = Calendar.getInstance();
				int diaA=0,period=1;
				int ciclo=0;
				
				while(period<=periodNo){
					
				 Timestamp currentDate=rs.getTimestamp("DATENEXTRUN");	
				 datefrom.add(Calendar.DATE, diaA*7);
				 dateto.add(Calendar.DATE, (diaA+1)*7);
				 
				if(currentDate.compareTo(datefrom.getTime())>=0 &&   currentDate.compareTo(dateto.getTime())<=0)
		            ciclo=period;
				
				period++;
				diaA++;
				}
				
				
				X_MP_Prognosis mp=new X_MP_Prognosis(getCtx(), 0, get_TrxName());
				 mp.setA_Asset_ID(rs.getInt("A_ASSET_ID"));
		            mp.setAD_Org_ID(rs.getInt("AD_ORG_ID"));
		            mp.setAD_PInstance_ID(p_PInstance_ID);
		            mp.setCiclo(ciclo);
		            mp.setDateTrx(rs.getTimestamp("DATENEXTRUN"));
		            mp.setDescription("Por Calendario :"+rs.getString("DATENEXTRUN"));
		            mp.setMP_Maintain_ID(rs.getInt("MP_MAINTAIN_ID"));
		            mp.setProgrammingType(rs.getString("PROGRAMMINGTYPE"));
		            mp.setSelected(false);
		            mp.set_CustomColumn("MP_MaintainDetail_ID", rs.getInt("MP_MaintainDetail_ID"));
		            mp.saveEx();
			}
		
			rs.close();
			pstmt.close();
			pstmt = null;
			
			commitEx();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		//searching by meter
		StringBuffer byMeter = new StringBuffer();
		/*byMeter.append("SELECT MA.AD_CLIENT_ID,MA.AD_ORG_ID,MA.MP_MAINTAIN_ID,MA.DESCRIPTION,MA.PROGRAMMINGTYPE, A.A_ASSET_ID,MA.MP_METER_ID, MA.RANGE, MA.NEXTMP");
		byMeter.append(" , Min(MLOG.DateTrx)as FirstDay, Max(MLOG.DateTrx)as LastDay, Sum(CurrentAmt)as Total, Count(1)as Qty,MS.MESURETYPE");
		byMeter.append(" FROM MP_MAINTAIN MA");
		byMeter.append(" INNER JOIN MP_METER MS ON (MA.MP_METER_ID=MS.MP_METER_ID)");
		byMeter.append(" INNER JOIN MP_ASSETMETER ME ON (MS.MP_METER_ID=ME.MP_METER_ID AND MA.A_ASSET_ID=ME.A_ASSET_ID)");
		byMeter.append(" INNER JOIN MP_AssetMeter_Log MLOG ON (ME.MP_ASSETMETER_ID=MLOG.MP_ASSETMETER_ID)");
		byMeter.append(" INNER JOIN A_ASSET A ON (MA.A_ASSET_ID=A.A_ASSET_ID OR A.MP_MPGroup_ID=MA.MP_MPGroup_ID)");
		byMeter.append(" WHERE PROGRAMMINGTYPE='M' AND MA.DOCSTATUS<>'IT'");
		byMeter.append(" AND Mlog.DateTrx BETWEEN (Sysdate-40) AND sysdate");
		byMeter.append(" Group by MA.AD_CLIENT_ID,MA.AD_ORG_ID,MA.MP_MAINTAIN_ID,MA.DESCRIPTION,MA.PROGRAMMINGTYPE, A.A_ASSET_ID,MA.MP_METER_ID,MA.RANGE, MA.NEXTMP,MS.MESURETYPE");*/
		
		byMeter.append("SELECT MA.AD_CLIENT_ID,MA.AD_ORG_ID,MA.MP_MAINTAIN_ID,MA.DESCRIPTION,MA.PROGRAMMINGTYPE, A.A_ASSET_ID,MA.MP_METER_ID, MA.RANGE, MA.NEXTMP");
		byMeter.append(", (select MIN(log2.DateTrx) from MP_AssetMeter_Log log2 where  ME.MP_ASSETMETER_ID=log2.MP_ASSETMETER_ID");
		byMeter.append(" and log2.DateTrx BETWEEN (Sysdate-40) AND sysdate)as FirstDay, ");
		byMeter.append(" (select MAX(log2.DateTrx) from MP_AssetMeter_Log log2 where  ME.MP_ASSETMETER_ID=log2.MP_ASSETMETER_ID");
		byMeter.append(" and log2.DateTrx BETWEEN (Sysdate-40) AND sysdate) as LastDay, ");
		byMeter.append(" Count(1)as Qty,ME.MP_ASSETMETER_ID,MS.Name, MA.MP_MAINTAINDetail_ID");
		byMeter.append(" FROM MP_MAINTAINDetail MA");
		byMeter.append(" INNER JOIN MP_Maintain mp2 ON (MA.MP_Maintain_ID = mp2.MP_Maintain_ID)");
		byMeter.append(" INNER JOIN A_ASSET A ON (MA.A_ASSET_ID=A.A_ASSET_ID)");
		byMeter.append(" INNER JOIN MP_METER MS ON (MA.MP_METER_ID=MS.MP_METER_ID)");
		byMeter.append(" INNER JOIN MP_ASSETMETER ME ON (MS.MP_METER_ID=ME.MP_METER_ID and a.a_asset_id= me.a_asset_id)");
		byMeter.append(" INNER JOIN MP_AssetMeter_Log MLOG ON (ME.MP_ASSETMETER_ID=MLOG.MP_ASSETMETER_ID)");
		byMeter.append(" WHERE MA.PROGRAMMINGTYPE='M' AND MA.DOCSTATUS<>'IT'");
		//ininoles se agreg logica que no tome los desactivados
		byMeter.append(" AND MA.ISACTIVE = 'Y' AND mp2.ISACTIVE = 'Y' ");
		//ininoles se agreg logica que no tome los activos desactivados
		byMeter.append(" AND A.ISACTIVE = 'Y' ");
		if(DB.isOracle())
			byMeter.append(" AND Mlog.DateTrx BETWEEN (Sysdate-40) AND sysdate");
		else
			byMeter.append(" AND Mlog.DateTrx BETWEEN (now()-40) AND now()");
		//ininoles where para no tomar mantenciones sin campo "proxima matención"
		byMeter.append(" AND MA.NEXTMP IS NOT NULL ");
		//ininoles where para no medidores desactivados"
		byMeter.append(" AND MS.IsActive = 'Y' AND ME.IsActive = 'Y' ");
		//byMeter.append(" AND A.A_Asset_ID = 1000702 ");
		
		//ininoles se agrega where para no tomar en cuenta hijos de nueva tabla MP_MaintainParent
		/*byMeter.append("AND MP_Maintain_ID NOT IN ");
		byMeter.append("(SELECT MP_MaintainRef_ID as MP_Maintain_ID ");
		byMeter.append("FROM MP_MaintainParent WHERE MP_Maintain_ID IN (");
		byMeter.append("SELECT MA.MP_MAINTAIN_ID FROM MP_MAINTAINDetail MA ");
		byMeter.append("INNER JOIN A_ASSET A ON (MA.A_ASSET_ID=A.A_ASSET_ID) ");
		byMeter.append("INNER JOIN MP_METER MS ON (MA.MP_METER_ID=MS.MP_METER_ID) ");
		byMeter.append("INNER JOIN MP_ASSETMETER ME ON (MS.MP_METER_ID=ME.MP_METER_ID and a.a_asset_id= me.a_asset_id) ");
		byMeter.append("INNER JOIN MP_AssetMeter_Log MLOG ON (ME.MP_ASSETMETER_ID=MLOG.MP_ASSETMETER_ID) ");
		byMeter.append("WHERE PROGRAMMINGTYPE='M' AND MA.DOCSTATUS<>'IT' ");
		if(DB.isOracle())
			byMeter.append(" AND Mlog.DateTrx BETWEEN (Sysdate-40) AND sysdate");
		else
			byMeter.append(" AND Mlog.DateTrx BETWEEN (now()-40) AND now()");
		byMeter.append("Group by MA.AD_CLIENT_ID,MA.AD_ORG_ID,MA.MP_MAINTAIN_ID,MA.DESCRIPTION,MA.PROGRAMMINGTYPE, A.A_ASSET_ID,MA.MP_METER_ID,MA.RANGE, MA.NEXTMP,ME.MP_ASSETMETER_ID,MS.Name, MA.MP_MAINTAINDetail_ID ");
		byMeter.append("))");*/
		//ininoles end
		byMeter.append(" Group by MA.AD_CLIENT_ID,MA.AD_ORG_ID,MA.MP_MAINTAIN_ID,MA.DESCRIPTION,MA.PROGRAMMINGTYPE, A.A_ASSET_ID,MA.MP_METER_ID,MA.RANGE, MA.NEXTMP,ME.MP_ASSETMETER_ID,MS.Name, MA.MP_MAINTAINDetail_ID");

		pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(byMeter.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				
			    if(rs.getInt("A_ASSET_ID") == 1000702)
			    	log.config("stop para debug");
			    
				int cumple=0;
				float lastM=0,firstM=0,prom=0;
		        if(rs.getInt("Qty")>1)
		        {
		        	log.info("ReadingLastM");
		        	Object params[]=new Object[]{rs.getInt("A_ASSET_ID"),rs.getInt("MP_Meter_ID"),rs.getTimestamp("LastDay")};

		        	log.info("ReadingFirstM:"+rs.getInt("A_ASSET_ID")+"-"+rs.getInt("MP_Meter_ID")+"-"+ rs.getTimestamp("LastDay"));
		        	lastM=DB.getSQLValueBD(get_TrxName(), " SELECT mlog.CurrentAmt FROM MP_AssetMeter_Log mlog"+
		            " Inner join MP_AssetMeter met on (mlog.MP_AssetMeter_ID=met.MP_AssetMeter_ID)"+
		            " WHERE met.A_ASSET_ID=? AND met.MP_Meter_ID=? AND mlog.DATETRX=? ",params ).floatValue();
		        	
		        	log.info("ReadingFirstM:"+rs.getInt("A_ASSET_ID")+"-"+rs.getInt("MP_Meter_ID")+"-"+ rs.getTimestamp("FIRSTDAY"));

		        	params=new Object[]{rs.getInt("A_ASSET_ID"),rs.getInt("MP_Meter_ID"),rs.getTimestamp("FIRSTDAY")};
		        	firstM=DB.getSQLValueBD(get_TrxName(),"SELECT mlog.CurrentAmt "+
		                    " FROM MP_AssetMeter_Log mlog"+
		                    " Inner join MP_AssetMeter met on (mlog.MP_AssetMeter_ID=met.MP_AssetMeter_ID)"+
		                    " WHERE met.A_ASSET_ID=? AND met.MP_Meter_ID=? AND mlog.DATETRX=?",params ).floatValue();
		        	
		        	float days=Math.abs(rs.getTimestamp("LastDay").getTime()-rs.getTimestamp("FIRSTDAY").getTime());
		        	prom=(lastM-firstM) / (days/(60*60*24*1000));
		        	
		        	if(prom == Double.NaN)
		        	{
		        		log.config("Activo con problemas"+rs.getInt("A_ASSET_ID"));
		        		throw new AdempiereException("Activo con problemas: "+rs.getInt("A_ASSET_ID")+" Favor Revisar");
		        	}
		        	
		        	StringBuffer update=new StringBuffer();
		        	update.append("UPDATE MP_MAINTAINDetail ");
		        	update.append(" SET PROMUSE="+prom +", lastread="+lastM);
		        	update.append(" WHERE MP_MAINTAINDetail_ID="+rs.getInt("MP_MAINTAINDetail_ID"));
		        	DB.executeUpdate(update.toString(), get_TrxName());	
		        
		            int cont=0;
		            cumple=0;
		            Calendar dateExe=Calendar.getInstance();
		            dateExe.setTime(rs.getTimestamp("LastDay"));
		            //VER SI ENTRA DE AQUI A LOS PROXIMOS N DIAS ( 7 * PERIODONO) 
		            log.info("LoopMedidor");
		            do
		            {       
		                if((lastM >= (rs.getFloat("NEXTMP")-rs.getFloat("RANGE")) && lastM<= (rs.getFloat("NEXTMP")+rs.getFloat("RANGE")) )
		                		|| (lastM>rs.getFloat("NEXTMP")) )
		                {                
		                	cumple=1;
		                	dateExe.add(Calendar.DATE,cont);		
		                }
		                else
		                {
                			lastM=lastM + prom;
		                	cont++;
		                }
		            } 
		            while( cumple==0 && cont!=(10*periodNo));
		            
		            Timestamp currentDate=new Timestamp(dateExe.getTimeInMillis());
		            if(cumple==1 || lastM > rs.getFloat("NEXTMP"))
		            {
			            int ciclo=0,period=1;
			            Calendar datefrom = Calendar.getInstance();
						Calendar dateto = Calendar.getInstance();
						int diaA=0;
			            while(period<=periodNo)
			            {	
							 datefrom.add(Calendar.DATE, diaA*7);
							 dateto.add(Calendar.DATE, (diaA+1)*7);
							 
							if(currentDate.compareTo(datefrom.getTime())>=0 &&   currentDate.compareTo(dateto.getTime())<=0)
					            ciclo=period;
							period++;
							diaA++;
						}
			            
			            log.info("Insertmeter");
			            /*StringBuffer insert=new StringBuffer();
			            
			            	insert.append("INSERT INTO MP_Prognosis");
			            	insert.append("VALUES ("+p_PInstance_ID+","+rs.getInt("AD_CLIENT_ID")+","+rs.getInt("AD_ORG_ID")+",'Y',"+stringDate +",100,"+stringDate +",100,");
			            	insert.append(rs.getInt("A_ASSET_ID")+","+rs.getInt("MP_MAINTAIN_ID")+",'"+(ciclo==0?"by Meter behind schedule":"by Meter"));
			            	insert.append(rs.getString("MESURETYPE")+":'||ROUND("+lastM+",2),'"+rs.getString("PROGRAMMINGTYPE")+"',"+ciclo+",'"+currentDate+"','N','N')");
			            DB.executeUpdate(insert.toString(), get_TrxName());*/
			            
			            X_MP_Prognosis mp=new X_MP_Prognosis(getCtx(), 0, get_TrxName());
			            mp.setA_Asset_ID(rs.getInt("A_ASSET_ID"));
			            mp.setAD_Org_ID(rs.getInt("AD_ORG_ID"));
			            mp.setAD_PInstance_ID(p_PInstance_ID);
			            mp.setCiclo(ciclo);
			            mp.setDateTrx(currentDate);
			            mp.setDescription((ciclo==0?"by Meter behind schedule":"by Meter")+rs.getString("name")+":"+lastM);
			            mp.setMP_Maintain_ID(rs.getInt("MP_MAINTAIN_ID"));
			            mp.setProgrammingType(rs.getString("PROGRAMMINGTYPE"));
			            mp.setSelected(false);
			            mp.set_CustomColumn("MP_MaintainDetail_ID", rs.getInt("MP_MaintainDetail_ID"));
			            mp.saveEx();
		            }
		        }
			}			
			commitEx();
			
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		log.info("Correccion para anidados");		
		
		String sql="select * from MP_Prognosis where AD_PINSTANCE_ID="+p_PInstance_ID+" and PROGRAMMINGTYPE='M' "+
					" ORDER BY MP_Maintain_ID DESC";
		pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				if(OFBForward.NewUpdateMaintainceParent())
				{
					//ininoles nuevo metodo de actualizacion en base a hijos para TSM
					DB.executeUpdate("UPDATE MP_Prognosis "+
							"SET SELECTED='Y' "+
					        "WHERE MP_MAINTAIN_ID IN " +
					        "(SELECT MP_MaintainRef_ID FROM MP_MaintainParent " +
					        "	WHERE MP_Maintain_ID = "+rs.getInt("MP_MAINTAIN_ID")+")" +
					        " AND A_Asset_ID = "+rs.getInt("A_ASSET_ID")+" AND AD_PINSTANCE_ID = "+p_PInstance_ID, get_TrxName());						
					//ininoles end
					
					//se actualizan tambien mantenciones padre
					/*DB.executeUpdate("UPDATE MP_Prognosis "+
							"SET SELECTED='Y' "+
					        "WHERE MP_MAINTAIN_ID IN " +
					        "(SELECT MP_Maintain_ID FROM MP_MaintainParent " +
					        " WHERE MP_MaintainRef_ID = "+rs.getInt("MP_MAINTAIN_ID")+")" +
					        " AND A_Asset_ID = "+rs.getInt("A_ASSET_ID")+" AND AD_PINSTANCE_ID = "+p_PInstance_ID, get_TrxName());
					 */
				}
				else
				{
					int MP_ID=DB.getSQLValue(get_TrxName(), "SELECT P.MP_MAINTAIN_ID  "+
						"FROM MP_Prognosis P "+
						"INNER JOIN MP_MAINTAINDetail M ON (P.MP_MAINTAINDetail_ID=M.MP_MAINTAINDetail_ID) "+
						"WHERE M.MP_MAINTAINPARENT_ID="+ rs.getInt("MP_MAINTAIN_ID") +
						"AND P.A_ASSET_ID="+rs.getInt("A_ASSET_ID")+" AND P.AD_PINSTANCE_ID="+p_PInstance_ID);
					
					while(MP_ID>0)
					{
						DB.executeUpdate("UPDATE MP_Prognosis "+
						"SET SELECTED='Y' "+
				        "WHERE A_ASSET_ID="+rs.getInt("A_ASSET_ID")+"AND "+
				        "MP_MAINTAIN_ID="+ MP_ID +" AND "+
				        "AD_PINSTANCE_ID="+p_PInstance_ID+" AND CICLO="+rs.getInt("ciclo"), get_TrxName());
						
						 MP_ID=DB.getSQLValue(get_TrxName(), "SELECT P.MP_MAINTAIN_ID  "+
									"FROM MP_Prognosis P "+
									"INNER JOIN MP_MAINTAINDetail M ON (P.MP_MAINTAINDetail_ID=M.MP_MAINTAINDetail_ID) "+
									"WHERE M.MP_MAINTAINPARENT_ID="+ MP_ID +
									"AND P.A_ASSET_ID="+rs.getInt("A_ASSET_ID")+" AND P.AD_PINSTANCE_ID="+p_PInstance_ID);
					}
				}
			}			
			rs.close();
			pstmt.close();
			pstmt = null;
			
			commitEx();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
			
		return("");
				
	   
	}	//	doIt


	
}	//	ShipInOut
