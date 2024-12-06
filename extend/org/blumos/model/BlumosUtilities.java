package org.blumos.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MBPartner;
import org.compiere.model.MConversionRate;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.jfree.util.Log;
import org.ofb.utils.DateUtils;

public class BlumosUtilities {

	public static String DameUltimosPrecios(int ID_Product, int ID_Bpartner,Properties ctx, String trx) 
	{
		String desc = "";
		int ind = 0;
		String sql = "select i.documentno, i.dateinvoiced, il.foreignprice, il.priceentered" +
				" from c_invoiceline il" +
				" inner join c_invoice i on (il.c_invoice_id = i.c_invoice_id)" +
				" where (i.docstatus='CO' OR i.docstatus='CL')" +
				" AND il.m_product_id= ? "+
				" AND i.c_bpartner_id= ? "+
				" AND i.issotrx='Y' order by i.dateinvoiced DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setInt(1, ID_Product);
			pstmt.setInt(2, ID_Bpartner);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				ind++;
				BigDecimal price = rs.getBigDecimal("foreignprice");
				if(price == null)
					price = Env.ZERO;
				if(price.compareTo(Env.ZERO) ==0)
					desc = desc+" F/"+rs.getString("documentno")+"  "+rs.getTimestamp("dateinvoiced")+"  *"+rs.getBigDecimal("priceentered")+"*     ";					
				else
					desc = desc+" F/"+rs.getString("documentno")+"  "+rs.getTimestamp("dateinvoiced")+"  *"+rs.getBigDecimal("foreignprice")+"*     ";
				if(ind == 3)
					break;
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return desc;		
	}
	public static int DameIDVendCartera(String Cod_Blumos, int ID_Bpartner, int ID_BpartnerLocation, 
			Properties ctx, String trx) 
	{
		MBPartner bp = new MBPartner(ctx, ID_Bpartner, trx);
		int userRet = -1;
		String usa_planta = bp.get_ValueAsString("usaplanta");
		if(usa_planta.compareTo("Y") == 0
				&& ID_BpartnerLocation > 0)
		{
			userRet = DB.getSQLValue(trx, "select  max(AU.ad_user_id) FROM AD_USER au" +
					" inner join t_bl_cartera bc ON (au.ad_user_id=bc.salesrep_id)" +
					" where bc.c_bpartner_id="+ID_Bpartner+" AND bc.cod_blumos='"+Cod_Blumos+"' " +
					" AND bc.c_bpartner_location_id="+ID_BpartnerLocation);
		}
		else
		{
			userRet = DB.getSQLValue(trx, "select  max(AU.ad_user_id) FROM AD_USER au" +
					" inner join t_bl_cartera bc ON (au.ad_user_id=bc.salesrep_id)" +
					" where bc.c_bpartner_id="+ID_Bpartner+" AND bc.cod_blumos='"+Cod_Blumos+"'");
		}
		return userRet;		
	}
	public static int DameIDOrgCartera(String Cod_Blumos, int ID_Bpartner, int ID_BpartnerLocation, int orgOProyect,
			Properties ctx, String trx)
	{
		MBPartner bp = new MBPartner(ctx, ID_Bpartner, trx);
		String usa_planta = bp.get_ValueAsString("usaplanta");
		int orgRet = -1;
		if(orgOProyect == 0)
		{
			if(usa_planta.compareTo("Y") == 0)
				orgRet = DB.getSQLValue(trx,"select  max(AG.ad_ORG_id) FROM AD_ORG aG" +
						" inner join t_bl_cartera bc ON (aG.ad_ORG_id=bc.AD_ORGBP_id)" +
						" where bc.c_bpartner_id="+ID_Bpartner+" AND bc.cod_blumos='"+Cod_Blumos+"' " +
						" AND bc.c_bpartner_location_id="+ID_BpartnerLocation);
			else
				orgRet = DB.getSQLValue(trx,"select  max(AG.ad_ORG_id) FROM AD_ORG aG" +
						" inner join t_bl_cartera bc ON (aG.ad_ORG_id=bc.AD_ORGBP_id)" +
						" where bc.c_bpartner_id="+ID_Bpartner+" AND bc.cod_blumos='"+Cod_Blumos+"' ");				
		}
		else
		{
			if(usa_planta.compareTo("Y") == 0)
				orgRet = DB.getSQLValue(trx,"select  max(cp.c_projectofb_id) FROM c_projectofb cp" +
						" inner join t_bl_cartera bc ON (cp.c_projectofb_id=bc.c_projectofb_id)" +
						" where bc.c_bpartner_id="+ID_Bpartner+" AND bc.cod_blumos='"+Cod_Blumos+"' " +
						" AND bc.c_bpartner_location_id="+ID_BpartnerLocation);
			else
				orgRet = DB.getSQLValue(trx,"select  max(cp.c_projectofb_id) FROM c_projectofb cp" +
						" inner join t_bl_cartera bc ON (cp.c_projectofb_id=bc.c_projectofb_id)" +
						" where bc.c_bpartner_id="+ID_Bpartner+" AND bc.cod_blumos='"+Cod_Blumos+"' ");
		}				
		return orgRet;		
	}
	public static String DameMail(int ID_User,int origen, Properties ctx, String trx) throws SQLException
	{
		String mysql = "SELECT damemail("+ID_User+","+origen+") as correos";
		String mailReturn = "";
		PreparedStatement pstmt = DB.prepareStatement(mysql, trx);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next())
		{
			mailReturn = rs.getString("correos");
		}
		return mailReturn;
	}	
	public static int DameDiasHabiles(Timestamp inicio,Timestamp fin, Properties ctx, String trx)
	{	
		if(fin.compareTo(inicio) >= 0)
		{
			int numero_dias = 0;
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(inicio);
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(fin);
			while (startCal.compareTo(endCal) <= 0)
			{
				if(startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY 
						&& startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
					numero_dias++;
				startCal.add(Calendar.DAY_OF_MONTH, 1);
			}
			int cantF = DB.getSQLValue(trx, "SELECT COUNT (FECHA_feriado) " +
					" FROM t_bl_feriados WHERE FECHA_feriado BETWEEN ? AND ?", inicio, fin);
			return numero_dias - cantF;
		}
		else
			return 0;
	}
	public static boolean DameVacFechaUsada(int ID_Bpartner, Timestamp inicio,Timestamp fin, Properties ctx, String trx)
	{	
		int cant = DB.getSQLValue(trx, "SELECT COUNT(1) FROM t_bl_vac_movimientos WHERE c_bpartner_id="+ID_Bpartner+
			" AND dias  <0 and progresivo<> 'Y' AND (? BETWEEN desde AND hasta " +
			"	OR ? BETWEEN desde AND hasta)" +
			//ininoles nueva condición pedida por don christian 04-01-2019
			" and coalesce((select docstatus from t_bl_vac_solicitud where t_bl_vac_solicitud_id=t_bl_vac_movimientos.t_bl_vac_solicitud_id),'CO')<>'VO'",inicio, fin);
		if(cant > 0)
			return true;
		else
			return false;
	}
	public static BigDecimal DameUFBlumos(Timestamp dateInvoice, Properties ctx, String trx)
	{	
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dateInvoice.getTime());
		Calendar today = Calendar.getInstance();		
		if(cal.get(Calendar.MONTH) == today.get(Calendar.MONTH)
				&& cal.get(Calendar.YEAR) == today.get(Calendar.YEAR))
		{	
			cal.add(Calendar.MONTH, -1);
		}
		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_YEAR,-1);
		//cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		Timestamp dateFinMes = new Timestamp(cal.getTimeInMillis());	
		
		BigDecimal MultiplyRate=MConversionRate.getRate(1000000,228,dateFinMes, 114, 
		Env.getAD_Client_ID(Env.getCtx()),Env.getAD_Org_ID(Env.getCtx()));
		
		return MultiplyRate;
	}
	/** Este metodo devuelve la fecha en formato dd-mm-yyyy*/
	public static String formatDate(Timestamp fecha, boolean voltear)
	{	
		String fechaStr = fecha.toString().trim().substring(0,10);		
		//se voltea la fecha
		if(voltear)
		{
			String fechaStrNew = fechaStr.substring(fechaStr.length()-2, fechaStr.length())+"-"+
				fechaStr.substring(fechaStr.length()-5, fechaStr.length()-3)+"-"+
				fechaStr.substring(fechaStr.length()-10, fechaStr.length()-6);	
			fechaStr = fechaStrNew;
		}
		return fechaStr;
	}
	/** Este metodo devuelve la fecha en formato dd-mm-yyyy hh:mm:ss*/
	public static String formatDateFull(Timestamp fecha, boolean voltear)
	{	
		String fechaStr = fecha.toString().trim().substring(0,10);
		String fechaStrHrs = fecha.toString().trim().substring(11,fecha.toString().length()-7);
		//se voltea la fecha
		if(voltear)
		{
			String fechaStrNew = fechaStr.substring(fechaStr.length()-2, fechaStr.length())+"-"+
				fechaStr.substring(fechaStr.length()-5, fechaStr.length()-3)+"-"+
				fechaStr.substring(fechaStr.length()-10, fechaStr.length()-6);	
			fechaStr = fechaStrNew;
		}
		return fechaStr+" "+fechaStrHrs;
	}
	public static String DameMailORACLE(int ID_User,int origen, Properties ctx, String trx)
	{
		String mailReturn = "";
		if(origen == 0)
			mailReturn = DB.getSQLValueString(trx, "SELECT email FROM ad_user WHERE ad_user_id=?", ID_User);
		else if(origen == 1)
			mailReturn = DB.getSQLValueString(trx, "SELECT url FROM c_bpartner WHERE c_bpartner_id=?", ID_User);
		else if(origen == 2)
		{
			int ID_Gerente = DB.getSQLValue(trx, "SELECT c_bpartner_id FROM c_bpartner WHERE t_bl_cargos_id IN (SELECT t_bl_cargos_id FROM" +
					" (select sc.t_bl_cargos_id, sc.esgerente, LEVEL as nivel FROM t_bl_cargos tc" +
					"	left join t_bl_cargos sc on (tc.supervisor_id=sc.t_bl_cargos_id)" +
					" 	left join t_bl_cargos sc2 on (sc.supervisor_id=sc2.t_bl_cargos_id)" +
					"	left join t_bl_cargos sc3 on (sc2.supervisor_id=sc3.t_bl_cargos_id)" +
					"	left join t_bl_cargos sc4 on (sc3.supervisor_id=sc4.t_bl_cargos_id)" +
					"	left join c_bpartner bp on (tc.t_bl_cargos_id=bp.t_bl_cargos_id)" +
					"	start with tc.t_bl_cargos_id=(select t_bl_cargos_id from c_bpartner where c_bpartner_id="+ID_User+")" +
					"	CONNECT BY PRIOR tc.SUPERVISOR_ID=tc.t_bl_cargos_id" +
					"	ORDER BY LEVEL)" +
					"	where esgerente='Y' and ROWNUM =1)");
			if(ID_Gerente > 0)
				mailReturn = DB.getSQLValueString(trx, "SELECT url FROM c_bpartner WHERE c_bpartner_id=?", ID_Gerente);
		}
		else if(origen == 3)
		{
			String mysql="SELECT salesrep_id FROM c_subprojectofb where c_projectofb_id="+ID_User+" and description <> '0' ";
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(mysql, trx);
				ResultSet rs = pstmt.executeQuery();
				String mailW = "";
				while (rs.next())
				{
					mailW = DB.getSQLValueString(trx, "SELECT email FROM ad_user where ad_user_id=?",rs.getInt("salesrep_id"));
					if(mailReturn == null || mailReturn.length() < 1)
						mailReturn = mailW;
					else
						mailReturn = mailReturn+","+mailW;
				}
				pstmt.close();rs.close();
				pstmt = null;rs = null;
			}
			catch (Exception e)
			{
				Log.info(e.getMessage());
			}
		}
		if(mailReturn == null || mailReturn.trim().length() < 1)
			mailReturn = "adempiere@blumos.cl";
		
		return mailReturn;
	}
	public static boolean isStringNull(String cadena)
	{	
		if(cadena != null && cadena.trim().length()>0)
			return false;
		else
			return true;
	}
	public static boolean IsDependiente(int BPartner_ID, int cargo_ID, Properties ctx, String trx )
	{
		//prueba de validacion dependientes
		/*String sql = "Select tc.t_bl_cargos_id   from t_bl_cargos tc" +
				" left join t_bl_cargos sc on (tc.supervisor_id=sc.t_bl_cargos_id)" +
				" left join t_bl_cargos sc2 on (sc.supervisor_id=sc2.t_bl_cargos_id)" +
				" left join t_bl_cargos sc3 on (sc2.supervisor_id=sc3.t_bl_cargos_id)" +
				" left join t_bl_cargos sc4 on (sc3.supervisor_id=sc4.t_bl_cargos_id)" +
				" left join c_bpartner bp on (tc.t_bl_cargos_id=bp.t_bl_cargos_id)" +
				" start with tc.t_bl_cargos_id=(select t_bl_cargos_id from c_bpartner where c_bpartner_id = (select c_bpartner_id from ad_user where ad_user_id="+usuario_ID+"))" +
				" CONNECT BY PRIOR tc.t_bl_cargos_id=tc.SUPERVISOR_ID";*/
		int id_start_with = DB.getSQLValue(trx,"SELECT t_bl_cargos_id FROM c_bpartner WHERE c_bpartner_id="+BPartner_ID);
		String sql ="SELECT t_bl_cargos_id FROM "+ 
				" (" +
				" SELECT t.t_bl_cargos_id, tc.esgerente, t.nivel" +
				" FROM connectby('t_bl_cargos', 'SUPERVISOR_ID', 'T_BL_CARGOS_ID', 't_bl_cargos_id', TO_CHAR("+id_start_with+"), 0)" +
				" AS t(t_bl_cargos_id numeric, parent_keyid numeric, NIVEL int, pos int)" +
				" INNER JOIN t_bl_cargos tc on (t.t_bl_cargos_id=tc.t_bl_cargos_id)" +
				" ) mi_subc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				if(rs.getInt("t_bl_cargos_id") == cargo_ID)
					return true;
			}
		}catch (SQLException e)
		{
			throw new DBException(e, sql);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return false;
	}
	public static String DameNombreProducto(int ID_product,int ID_Socio,String trx)
	{	
		String NOMBREPRODUCTO = "";
		NOMBREPRODUCTO = DB.getSQLValueString(trx,"select count(1) from c_bpartner_product " +
				" WHERE m_product_id="+ID_product+" and c_bpartner_id="+ID_Socio);
		if(NOMBREPRODUCTO == null ||
				NOMBREPRODUCTO.trim().length() <=0)
		{
			NOMBREPRODUCTO = DB.getSQLValueString(trx,"select name from m_product" +
					"where m_product_id="+ID_product);
		}
		return NOMBREPRODUCTO;
	}
	public static String DameTurno(Timestamp dateComplete, Timestamp datePromised)
	{	
		Timestamp dateFin = DateUtils.today();
		dateFin = DateUtils.SumarDias(dateFin, 2);
		if(datePromised.compareTo(dateFin) > 0)
			return "1";
		
		Timestamp topeMin1 = new Timestamp(dateComplete.getTime());
		topeMin1.setHours(15);
		topeMin1.setMinutes(0);
		topeMin1.setSeconds(0);
		
		Timestamp topeMax1 = new Timestamp(dateComplete.getTime());
		topeMax1.setHours(17);
		topeMax1.setMinutes(59);
		topeMax1.setSeconds(59);
		
		if(dateComplete.compareTo(topeMin1)>= 0 && dateComplete.compareTo(topeMax1)<=0)
			return "3";
		else
			return "1";
	}
	/**Metodo que envia cantidad de dias a sumar a la fecha prometida de la NV. 
	 * Si el valor devuelto es 0 significa que no es necesario sobreescribir fecha prometida y 
	 * se debe mantener date promised de la cabecera
	 * */
	public static int DameSumarDiasTurno(Timestamp dateComplete, Timestamp datePromised)
	{	
		Calendar cdateComplete = Calendar.getInstance(); // Obtiene una instancia de Calendar
		cdateComplete.setTime(dateComplete); // Asigna la fecha dateComplete a cdateComplete
		System.out.println("la hora desde calendar: "+cdateComplete.get(Calendar.HOUR_OF_DAY));
		int la_hora=cdateComplete.get(Calendar.HOUR_OF_DAY); // obtenemos la hora desde dateComplete
		// redondear cDateComplete
		cdateComplete.set(Calendar.HOUR_OF_DAY, 0);
		cdateComplete.set(Calendar.MINUTE, 0);
		cdateComplete.set(Calendar.SECOND, 0);
		cdateComplete.set(Calendar.MILLISECOND, 0);
		Calendar cdatePromised = Calendar.getInstance(); // Obtiene otra instancia de calendario
		cdatePromised.setTime(datePromised); // asigna datePromised a cDatePromised
		// redondear cDatePromised
		cdatePromised.set(Calendar.HOUR_OF_DAY, 0);
		cdatePromised.set(Calendar.MINUTE, 0);
		cdatePromised.set(Calendar.SECOND, 0);
		cdatePromised.set(Calendar.MILLISECOND, 0);
        // calculamos diferencia de dias entre cDateComplete y cDatePromised        
        long diferencia = DateUtils.getDifferenceDays(cdateComplete.getTime(), cdatePromised.getTime()) ;
        System.out.println("DIF entre cdateComplete: "+cdateComplete.getTime()+", y cDatePromised: "+cdatePromised.getTime()+" = "+diferencia);
        
        // segun definicion, si es pasado las 18 hrs, la fecha prometida debe ser desde pasado mañana
        // en cualquier otro caso la fecha prometida debe ser desde mañana
        // si la diferencia entre la fecha prometida y la fecha en que se completa es correcta, se devuelve un cero
        long el_valor;
        if(la_hora>= 18)
		{
			if(diferencia==0) return 2;
			else if (diferencia==1) return 1;
			else if (diferencia<0)
			{
				el_valor = Math.abs(diferencia)+2;
				return (int) el_valor;
	}		else return 0;
		}		else 		{
			if(diferencia==0)
				return 1;
			else if (diferencia<0)
			{
				el_valor = Math.abs(diferencia)+1;
				return (int) el_valor;
	}		else return 0;
			
		}	
	}

}
