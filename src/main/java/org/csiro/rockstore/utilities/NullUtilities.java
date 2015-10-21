package org.csiro.rockstore.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.DateUtil;

public class NullUtilities {
	
	public static Date parseDateAllowNull(String date) throws ParseException{		
		DateFormat df =new SimpleDateFormat("dd/MMM/yyyy");		
		return (date==null || date.isEmpty())?null:df.parse(date);			
	}
	

	public static Date parseExcelDateAllowNull(Double date) throws ParseException{		
		
		return (date==null || Double.isNaN(date) || date==0)?null:DateUtil.getJavaDate(date);			
	}

}
