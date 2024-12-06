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
package org.tsm.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrder;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.model.MMovement;
import org.compiere.model.MOrg;
import org.compiere.model.X_HR_Bitacora;
import org.compiere.model.X_HR_BitacoraLine;
import org.compiere.model.X_RH_EvaluationLine;
import org.compiere.model.X_RH_EvaluationHeader;
import org.compiere.model.X_RH_EvaluationGuide;

/**
 *	
 *	
 *  @author mfrojas
 *  @version $Id: GenerateEvaluationLine.java $
 *  
 *  Generate Evaluation Line on Evaluation Driver
 */
public class GenerateEvaluationLine extends SvrProcess
{
	//private String			p_DocStatus = null;
	private int				p_Ev_ID = 0; 
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 p_Ev_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_Ev_ID > 0)
		{
			X_RH_EvaluationHeader eHeader = new X_RH_EvaluationHeader(getCtx(), p_Ev_ID, get_TrxName());
			X_RH_EvaluationGuide eGuide = new X_RH_EvaluationGuide(getCtx(),eHeader.getRH_EvaluationGuide_ID(),get_TrxName());
			
			if(eHeader.getAD_OrgRef_ID() <= 0)
				throw new AdempiereException("Debe ingresar una flota");
			
			String sqldrivers = "SELECT C_BPartner_ID FROM C_BPartner where IsActive='Y' and " +
					" isunlinked='N' and AD_OrgRef_ID ="+eHeader.getAD_OrgRef_ID();
			
			log.config("sql drivers : "+sqldrivers);
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try
			{
				pstmt = DB.prepareStatement (sqldrivers, get_TrxName());
				rs = pstmt.executeQuery();		

				int ID_BPartner;
				while (rs.next())
				{
					ID_BPartner = rs.getInt("C_BPartner_ID");

					X_RH_EvaluationLine eLine = new X_RH_EvaluationLine(getCtx(),0,get_TrxName());

					eLine.setAD_Org_ID(eHeader.getAD_Org_ID());
	/*				eLine.setAnswer1("Y");
					eLine.setAnswer2("Y");
					eLine.setAnswer3("Y");
					eLine.setAnswer4("Y");
					eLine.setAnswer5("Y");
					eLine.setAnswer6("Y");
					eLine.setAnswer7("Y");
					eLine.setAnswer8("Y");
					eLine.setAnswer9("Y");
					eLine.setAnswer10("Y");
*/	
					eLine.setAnswer1(eGuide.get_Value("ExpectedResult1").toString());
					eLine.setAnswer2(eGuide.get_Value("ExpectedResult2").toString());
					eLine.setAnswer3(eGuide.get_Value("ExpectedResult3").toString());
					eLine.setAnswer4(eGuide.get_Value("ExpectedResult4").toString());
					eLine.setAnswer5(eGuide.get_Value("ExpectedResult5").toString());
					eLine.setAnswer6(eGuide.get_Value("ExpectedResult6").toString());
					eLine.setAnswer7(eGuide.get_Value("ExpectedResult7").toString());
					eLine.setAnswer8(eGuide.get_Value("ExpectedResult8").toString());
					eLine.setAnswer9(eGuide.get_Value("ExpectedResult9").toString());
					eLine.setAnswer10(eGuide.get_Value("ExpectedResult10").toString());
					eLine.setquestion1_drive(eGuide.getquestion1_drive());
					eLine.setquestion2_drive(eGuide.getquestion2_drive());
					eLine.setquestion3_drive(eGuide.getquestion3_drive());
					eLine.setquestion4_drive(eGuide.getquestion4_drive());
					eLine.setquestion5_drive(eGuide.getquestion5_drive());
					eLine.setquestion6_drive(eGuide.getquestion6_drive());
					eLine.setquestion7_drive(eGuide.getquestion7_drive());
					eLine.setquestion8_drive(eGuide.getquestion8_drive());
					eLine.setquestion9_drive(eGuide.getquestion9_drive());
					eLine.setquestion10_drive(eGuide.getquestion10_drive());
					eLine.setC_BPartner_ID(ID_BPartner);
					log.config("evaluation header "+p_Ev_ID);
					eLine.setRH_EvaluationHeader_ID(p_Ev_ID);
					
					eLine.save();
				}	
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, e.getMessage()+" SQL:"+sqldrivers, e);
			}

		}
	   return "Procesado";
	}	//	doIt
}
