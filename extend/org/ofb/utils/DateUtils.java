package org.ofb.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.compiere.util.Env;

public class DateUtils {
	public static Timestamp today()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);						
		Timestamp instant = new Timestamp(calendar.getTimeInMillis());
		return instant;
	}
	public static Timestamp todayAdempiere()
	{	
		return Env.getContextAsDate(Env.getCtx(), "#Date");
	}

	public static Timestamp now()
	{
		Calendar calendar = Calendar.getInstance();
		Timestamp instant = new Timestamp(calendar.getTimeInMillis());
		return instant;
	}

	public static Timestamp nextDay( Timestamp _day )
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(_day.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);						
		calendar.add(Calendar.DATE, 1);							
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	public static long getDifferenceDays(Date d1, Date d2) {
	    long diff = d2.getTime() - d1.getTime();
	    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	
	public static Timestamp SumarMeses(Timestamp fecha, int Meses)
	{
		Timestamp result = fecha;
		Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(result.getTime());
        calendar.add(Calendar.MONTH, Meses);
        result = new Timestamp(calendar.getTimeInMillis());		
		return result;
	}
	public static Timestamp UltimoDiaMes(Timestamp fecha)
	{
		Timestamp result = fecha;
		Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(result.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));        
        result = new Timestamp(calendar.getTimeInMillis());		
		return result;
	}
	public static Timestamp SumarDias(Timestamp fecha, int Dias)
	{
		Timestamp result = fecha;
		Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(result.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, Dias);
        result = new Timestamp(calendar.getTimeInMillis());		
		return result;
	}
	/** Este metodo devuelve la fecha en formato yyyy-mm-dd*/
	public static String formatDate(Timestamp fecha, boolean voltear)
	{	
		String fechaStr = fecha.toString().trim().substring(0,10);		
		//se voltea la fecha
		if(voltear)
		{
			/*String fechaStrNew = fechaStr.substring(fechaStr.length()-2, fechaStr.length())+"-"+
				fechaStr.substring(fechaStr.length()-5, fechaStr.length()-3)+"-"+
				fechaStr.substring(fechaStr.length()-10, fechaStr.length()-6);	
			fechaStr = fechaStrNew;*/
		}
		return fechaStr;
	}	
	public static boolean  isDateddMMyy(String dateStr) 
	{
		try 
		{
		    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
		    Date parsedDate = dateFormat.parse(dateStr);
		    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		    return true;
		} catch(Exception e) 
		{ 
		    return false;
		}
	}
	public static boolean  isDateddMMyyyy(String dateStr) 
	{
		try 
		{
		    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		    Date parsedDate = dateFormat.parse(dateStr);
		    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		    return true;
		} catch(Exception e) 
		{ 
		    return false;
		}
	}
	public static boolean  isDateddMMyyyyV2(String dateStr) 
	{
		try 
		{
		    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		    Date parsedDate = dateFormat.parse(dateStr);
		    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
		    return true;
		} catch(Exception e) 
		{ 
		    return false;
		}
	}
	public static Timestamp convertDateddMMyy(String dateStr) 
	{
		Timestamp timestamp = null;
		try {
		    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
		    Date parsedDate = dateFormat.parse(dateStr);
		    timestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch(Exception e) {
			;
		} 
		return timestamp;
	}
	public static Timestamp convertDateddMMyyyy(String dateStr) 
	{
		Timestamp timestamp = null;
		try {
		    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		    Date parsedDate = dateFormat.parse(dateStr);
		    timestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch(Exception e) {
			;
		} 
		return timestamp;
	}
	public static Timestamp convertDateddMMyyyyV2(String dateStr) 
	{
		Timestamp timestamp = null;
		try {
		    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		    Date parsedDate = dateFormat.parse(dateStr);
		    timestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch(Exception e) {
			;
		} 
		return timestamp;
	}
}
