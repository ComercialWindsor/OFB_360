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

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	
 *	
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: ImportCartola.java,v 1.0  $
 */
public class ImportCartola extends SvrProcess
{
	
	/**	Organization to be imported to	*/
	private int 			m_AD_Org_ID = 0;
	
	private int 			m_BankAccount_ID=0;
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
			else if (name.equals("AD_Org_ID"))
				m_AD_Org_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("C_BankAccount_ID"))
				m_BankAccount_ID = ((BigDecimal)para[i].getParameter()).intValue();
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
		StringBuffer sql = null;
		int no = 0;
		
		//borra cabeceras
		sql = new StringBuffer("Delete From I_bankMatch i "
				+ "WHERE (i.SerialNo IS NULL "
				+ "OR i.Amt = 0 OR i.description like '%SALDO%') "
				+ "AND i.I_IsImported<>'Y'");
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (no != 0)
				log.info("Deleted =" + no);
		
		sql = new StringBuffer("Update I_bankMatch  "
					+ "Set  ErrorMsg = '' "
					+ " Where I_IsImported<>'Y'");
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (no != 0)
				log.info("updated msg =" + no);

//		Set Bank Account
		sql = new StringBuffer("UPDATE I_bankMatch i "
			+ "SET C_BankAccount_ID="
			+ "( "
			+ " SELECT C_BankAccount_ID "
			+ " FROM C_BankAccount a, C_Bank b "
			+ " WHERE b.IsOwnBank='Y' "
			+ " AND a.AD_Client_ID=i.AD_Client_ID "
			+ " AND a.C_Bank_ID=b.C_Bank_ID "
			+ " AND a.AccountNoFile=i.BankAccountNo "
			+ "), ErrorMsg=''  "
			+ "WHERE i.C_BankAccount_ID IS NULL "
			+ "AND i.I_IsImported<>'Y' "
			+ "OR i.I_IsImported IS NULL");
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		if (no != 0)
			log.info("Bank Account found =" + no);
		
		sql = new StringBuffer("UPDATE I_bankMatch i "
				+ "SET ErrorMsg='NO Bank Account' "
				+ "WHERE i.C_BankAccount_ID IS NULL "
				+ "AND i.I_IsImported<>'Y' "
				+ "OR i.I_IsImported IS NULL");
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (no != 0)
				log.info("Bank Account notfound =" + no);
			
		//ininoles se saca chequeo de folio	
			log.config("**chequeo de folio**");
		/*	
		sql = new StringBuffer("UPDATE I_bankMatch i "
					+ "SET ErrorMsg='Folio ya Existe' "
					+ "WHERE Exists (select * from C_BankMatch b where b.folio=i.folio AND b.serialno=i.serialno) "
					+ "AND i.I_IsImported<>'Y' "
					+ "OR i.I_IsImported IS NULL");
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			if (no != 0)
				log.info("Folio ya Existe =" + no);
			*/
		
		 sql = new StringBuffer("SELECT * "
					+ "FROM I_bankMatch i  "
					+ "WHERE i.I_isImported='N' and i.C_BankAccount_ID>0 and (ErrorMsg = '' or ErrorMsg is null) ");
		 
		sql.append(" And i.createdby="+Env.getAD_User_ID(getCtx()));
		
		if(m_BankAccount_ID>0)
			sql.append(" And i.C_BankAccount_ID="+m_BankAccount_ID);
		
		sql.append(" Order by i.C_BankAccount_ID");
		 
		//se saca validacion de saldo inicial
		/**Antes importar**/
		/*
		int x= DB.getSQLValue(get_TrxName(), "Select count(1) from I_bankMatch i " +
				"Inner join C_BankAccount ba on (i.C_BankAccount_ID=ba.C_BankAccount_ID) " +
				"Inner join C_Bank b on (ba.C_Bank_ID = b.C_Bank_ID)" +
				"where i.I_isImported='N' and i.C_BankAccount_ID>0 and (ErrorMsg = '' or ErrorMsg is null)  and upper(b.name) like '%CHILE%' and i.codsucursal<>'OK'" +
				" and i.createdby="+Env.getAD_User_ID(getCtx()));
		if(x>0)
			actualizaSaldoInicial();
		/***/
		
		
		 PreparedStatement pstmt = null;
			no = 0;
			try
			{
				pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					X_I_BankMatch ibank = new X_I_BankMatch (Env.getCtx(), rs, get_TrxName());
					/*
					//validacion saldo inicial
					BigDecimal  inicial = getSaldoAnterior(ibank.getC_BankAccount_ID(),ibank.getFolio().intValue());
					if(inicial.intValue()>0)
						if(inicial.longValue()< ((BigDecimal)ibank.get_Value("SaldoInicial")).longValue()-1 
								|| inicial.longValue()> ((BigDecimal)ibank.get_Value("SaldoInicial")).longValue()+1 )
							return "El Saldo Anterior "+ inicial +" vs el Inicial " + ((BigDecimal)ibank.get_Value("SaldoInicial")) +" de cartola Folio " + ibank.getFolio() ;
					//fin validacion saldo inicial
					*/
					int Payment_ID=getPayment(ibank.getAmt(), ibank.getSigno(),ibank.getDescription(),ibank.getC_BankAccount_ID(),ibank.getSerialNo());
					
					X_C_BankMatch  cbank = new X_C_BankMatch (Env.getCtx(), 0, get_TrxName());
					
					if(Payment_ID>0){
						cbank.setC_Payment_ID(Payment_ID);
						ibank.setC_Payment_ID(Payment_ID);
						cbank.setIsMatched(true);
						ibank.setIsMatched(true);
					}
					
					cbank.setAD_Org_ID(ibank.getAD_Org_ID());
					cbank.setAmt(ibank.getAmt());
					cbank.setBankAccountNo(ibank.getBankAccountNo());
					cbank.setC_BankAccount_ID(ibank.getC_BankAccount_ID());
					cbank.setCodSucursal(ibank.getCodSucursal());
					cbank.setCodTransaction(ibank.getCodTransaction());
					cbank.setDescription(ibank.getDescription());
					cbank.setDocumentDate(ibank.getDocumentDate());
					cbank.setFolio(ibank.getFolio());
					cbank.setSerialNo(ibank.getSerialNo());
					cbank.setSigno(ibank.getSigno());
					cbank.setStatementDate(ibank.getStatementDate());
					cbank.set_ValueOfColumn("SaldoInicial", ibank.get_Value("SaldoInicial"));
					cbank.save();
					
					ibank.setI_IsImported(true);
					ibank.setProcessed(true);
					ibank.save();
					no++;
				}
				rs.close();
				pstmt.close();
				
				
				rs = null;
				pstmt = null;
				
				if(no>1)
				{
					sql = new StringBuffer("Delete from I_bankMatch i where i.I_isImported='Y' and i.Createdby="+Env.getAD_User_ID(getCtx()));
						no = DB.executeUpdate(sql.toString(), get_TrxName());
				}
				
			}
			catch(Exception e)
			{
				log.log(Level.SEVERE,e.getMessage());
			}
			
		this.commitEx();
					
		return no+" Movimientos Importados";
	   
	}	//	doIt


	private int getPayment(BigDecimal amt, String signo, String Descripcion, int Account_ID, String SerialNo)
	{
		
		String sql= "select p.C_Payment_ID from C_Payment p" +
		" where p.DocStatus IN ('CO','CL') and p.isReconciled='N' and p.PayAmt=? and p.isreceipt=? and p.C_BankAccount_ID=?" +
		" And Not Exists (select * from I_bankMatch c where c.C_Payment_ID = p.C_Payment_ID) "+
		" And (CheckNo = '"+SerialNo+"' OR CreditCardNumber = '"+SerialNo+"' OR AccountNo = '"+SerialNo+"')";
		;
		
		if(Descripcion.toUpperCase().indexOf("CHEQUE")>-1)
			sql+=" and TenderType='K' "; // cheque
		
		return DB.getSQLValue(get_TrxName(),sql, amt, signo.equals("A")?"Y":"N",Account_ID);
	}
	
private BigDecimal getSaldoAnterior(int C_BankAccount_ID, int folio){
		
		int maxFolio= DB.getSQLValue(get_TrxName(), "Select max(folio) from c_bankMatch where C_BankAccount_ID="+C_BankAccount_ID);
		
		if(maxFolio<=0 || maxFolio ==folio)
			return new BigDecimal(0.0);
		
		PreparedStatement pstmt = null;
		String sql = "select saldoinicial,amt,signo from c_bankmatch where folio="+maxFolio;
		BigDecimal saldo = new BigDecimal("0.0");
			int no = 0;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					if(no==0)
						saldo = rs.getBigDecimal(1);
					
					if(rs.getString(3).trim().equals("A"))
					   saldo = saldo.add(rs.getBigDecimal(2));
					if(rs.getString(3).trim().equals("C"))
					   saldo = saldo.subtract(rs.getBigDecimal(2));
					
					no++;
					
				}
				rs.close();
				pstmt.close();
				
				
				rs = null;
				pstmt = null;
			}
			catch(Exception e)
			{
				log.log(Level.SEVERE,e.getMessage());
			}
			
			return saldo;
	}

private void actualizaSaldoInicial(){
	
	StringBuffer sql = new StringBuffer("Select i.* from I_bankMatch i " +
			"Inner join C_BankAccount ba on (i.C_BankAccount_ID=ba.C_BankAccount_ID) " +
			"Inner join C_Bank b on (ba.C_Bank_ID = b.C_Bank_ID)" +
			"where i.I_isImported='N' and i.C_BankAccount_ID>0 and (ErrorMsg = '' or ErrorMsg is null) and upper(b.name) like '%CHILE%' and i.codsucursal<>'OK' " +
			" and i.createdby="+Env.getAD_User_ID(getCtx()) + " order by i.folio");
	
	PreparedStatement pstmt = null;
	try
	{
		pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
		ResultSet rs = pstmt.executeQuery();
		BigDecimal saldo = new BigDecimal("0.0");
		long folio = 0;
		int Account_ID= 0;
		while (rs.next())
		{
			if(folio!=rs.getLong("folio"))
			{	
				if(folio!=0)
					DB.executeUpdate("update I_bankMatch set saldoinicial ="+saldo.longValue() +", codsucursal='OK' where folio="+folio + " and I_isImported='N' and C_BankAccount_ID="+Account_ID, get_TrxName());
				
				folio = rs.getLong("folio");
				Account_ID = rs.getInt("C_BankAccount_ID");
				saldo = rs.getBigDecimal("saldoinicial");
			}
			
			
			if(rs.getString("signo").trim().toUpperCase().equals("A"))
				 saldo = saldo.subtract(rs.getBigDecimal("amt"));
			if(rs.getString("signo").trim().toUpperCase().equals("C"))
				saldo = saldo.add(rs.getBigDecimal("amt"));
		}
		
		if(folio!=0)
			DB.executeUpdate("update I_bankMatch set saldoinicial ="+saldo.longValue() +", codsucursal='OK' where folio="+folio + " and I_isImported='N' and C_BankAccount_ID="+Account_ID, get_TrxName());
		
		rs.close();
		pstmt.close();
		
		
		rs = null;
		pstmt = null;
	}
	catch(Exception e)
	{
		log.log(Level.SEVERE,e.getMessage());
	}
}
	
}	//	ShipInOut
