package org.csiro.rockstore.utilities;

import java.util.Hashtable;

public class ImportUtilities {
	
	public static final Hashtable<String,String> DATUM_TABLE;
	static {
		DATUM_TABLE = new Hashtable<String,String>();
		DATUM_TABLE.put("WGC84/EPSG:4326".toUpperCase(), "EPSG:4326");
		DATUM_TABLE.put("GDA94/MGA Zone 48".toUpperCase(), "EPSG:28348");
		DATUM_TABLE.put("GDA94/MGA Zone 49".toUpperCase(), "EPSG:28349");
		DATUM_TABLE.put("GDA94/MGA Zone 50".toUpperCase(), "EPSG:28350");
		DATUM_TABLE.put("GDA94/MGA Zone 51".toUpperCase(), "EPSG:28351");
		DATUM_TABLE.put("GDA94/MGA Zone 52".toUpperCase(), "EPSG:28352");
		DATUM_TABLE.put("GDA94/MGA Zone 53".toUpperCase(), "EPSG:28353");
		DATUM_TABLE.put("GDA94/MGA Zone 54".toUpperCase(), "EPSG:28354");
		DATUM_TABLE.put("GDA94/MGA Zone 55".toUpperCase(), "EPSG:28355");
		DATUM_TABLE.put("GDA94/MGA Zone 56".toUpperCase(), "EPSG:28356");
		DATUM_TABLE.put("GDA94/MGA Zone 57".toUpperCase(), "EPSG:28357");
		DATUM_TABLE.put("GDA94/MGA Zone 58".toUpperCase(), "EPSG:28358");
	
	}

	
	
	
	
	 
}
