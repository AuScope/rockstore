package org.csiro.rockstore.utilities;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

public class SpatialUtilities {
	
	public static Geometry wktToGeometry(String lat, String lon,String datum) throws NoSuchAuthorityCodeException, FactoryException {
		
		int srid = Integer.parseInt(datum.replace("EPSG:", ""));	
		
		String wkt = String.format("Point(%s %s)", lon,lat);
				
        WKTReader fromText = new WKTReader(new GeometryFactory(new PrecisionModel(),srid));
        Geometry geom = null;
        try {
            geom = fromText.read(wkt);
            if( datum != null && !datum.equals("EPSG:4326")){
            	CoordinateReferenceSystem sourceCRS = CRS.decode(datum);
        		CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");        		
        		MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
        		geom = JTS.transform( geom, transform);
        		geom.setSRID(4326);
        		
        		//VT: For some reason this transformation swapped X and Y around for EPGS:4326 vs the others
        		Coordinate[] original = geom.getCoordinates();
                for(int i =0; i<original.length; i++){
                    Double swapValue = original[i].x;
                    original[i].x = original[i].y;
                    original[i].y = swapValue;
                }
            }
            
        } catch (Exception e) {
            return null;
        }
        return geom;
    }

}
