package org.csiro.rockstore.utilities;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

public class SpatialUtilities {
	
	public static Geometry wktToGeometry(String wktPoint) {
        WKTReader fromText = new WKTReader(new GeometryFactory(new PrecisionModel(),4326));
        Geometry geom = null;
        try {
            geom = fromText.read(wktPoint);
        } catch (Exception e) {
            return null;
        }
        return geom;
    }

}
