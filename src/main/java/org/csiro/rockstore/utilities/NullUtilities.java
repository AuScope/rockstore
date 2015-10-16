package org.csiro.rockstore.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NullUtilities {
	
	public static Date parseDateAllowNull(String date) throws ParseException{		
		DateFormat df =new SimpleDateFormat("dd/MMM/yyyy");		
		return (date==null || date.isEmpty())?null:df.parse(date);			
	}
	
	
	public static Date parseDateAllowNullddMMyyyy(String date) throws ParseException{		
		DateFormat df =new SimpleDateFormat("dd/MM/yyyy");		
		return (date==null || date.isEmpty())?null:df.parse(date);			
	}

}
