package org.csiro.rockstore.exceptions;

public enum ImportExceptionCode {
	
	DATUM_OUTSIDE_LIST(101),
	SAMPLE_TYPE_OUTSIDE_LIST(102),
	VALUE_NOT_BOOLEAN(103),	
	INVALID_SUBCOLLECTION_ID(104),
	INVALID_LAT_LON(105),
	INVALID_SAMPLE_COLLECTOR(106),
	INVALID_DATE(107),
	INVALID_STAFF_ID(108),
	EXCEED_5000_ROW_BATCH(109),
	INVALID_CELL_FORMAT(110);
	
	
	

	private final int number;
 
	 private ImportExceptionCode(int number) {
	   this.number = number;
	 }
	 
	  
	 public int getNumber() {
	   return number;
	 }
	 
	 
	 public String getMessage() {
		 String message ="";
		 switch(number){
		 	case 101: message="Datum outside of allowed list";
		 		break;
		 	case 102: message="Sample type outside of allowed list";
	 			break;	
		 	case 103: message="Cell value must be TRUE OR FALSE";
 				break;	
		 	case 104: message="Invalid subcollection Id";
 				break;
		 	case 105: message="Missing latitude longtitude";
				break;	
		 	case 106: message="Invalid Sample collector name, must be present in the database";
				break;	
		 	case 107: message="Invalid Date cell or format";
				break;
		 	case 108: message="Invalid staff id, must be presend in the database";
				break;	
		 	case 109: message="Only first 5000 row is processed each batch";
				break;	
		 	case 110: message="Invalid cell format";
				break;	
			default: message="Error not capture, please send a sample of your file to cg-admin@csiro.au";	
 				break;
		 }
		 
		 return message;
	 }
	

}
