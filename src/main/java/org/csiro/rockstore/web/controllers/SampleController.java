package org.csiro.rockstore.web.controllers;


import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.csiro.rockstore.entity.postgres.RsSample;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.entity.service.CollectionEntityService;
import org.csiro.rockstore.entity.service.SampleEntityService;
import org.csiro.rockstore.entity.service.SubCollectionEntityService;
import org.csiro.rockstore.utilities.NullUtilities;
import org.csiro.rockstore.utilities.SpatialUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vividsolutions.jts.geom.Point;



@Controller
public class SampleController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	SubCollectionEntityService subCollectionEntityService;
	CollectionEntityService collectionEntityService;
	SampleEntityService sampleEntityService;
	
	@Autowired
	public SampleController(CollectionEntityService collectionEntityService, SubCollectionEntityService subCollectionEntityService,SampleEntityService sampleEntityService){
		this.subCollectionEntityService=subCollectionEntityService;
		this.collectionEntityService =collectionEntityService;
		this.sampleEntityService = sampleEntityService;
	}


    @RequestMapping(value = "sampleAddUpdate.do")
    public ResponseEntity<Object> sampleAddUpdate(            
            @RequestParam(required = false, defaultValue="0", value ="id") int id,
            @RequestParam(required = true, value ="subcollectionId") String subcollectionId,           
            @RequestParam(required = false, value ="igsn") String igsn,
            @RequestParam(required = false, value ="csiroSampleId") String csiroSampleId,           
            @RequestParam(required = false, value ="sampleType") String sampleType,
            @RequestParam(required = false, value ="bhid") String bhid,
            @RequestParam(required = false, value ="depth") Double depth,
            @RequestParam(required = false, value ="datum") String datum,
            @RequestParam(required = false, value ="zone") String zone,
            @RequestParam(required = false, value ="containerId") String containerId,
            @RequestParam(required = false, value ="externalRef") String externalRef,
            @RequestParam(required = false, value ="sampleCollector") String sampleCollector,
            @RequestParam(required = false, value ="dateSampled") String dateSampled,
            @RequestParam(required = false, value ="sampleDispose") Boolean sampleDispose,
            @RequestParam(required = false, value ="dateDisposed") String dateDisposed,
            @RequestParam(required = false, value ="staffidDisposed") String staffidDisposed,
            @RequestParam(required = false, value ="locationWkt") String locationWkt,
            Principal user,
            HttpServletResponse response){
    	
    	if(user==null){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Not logged in"),HttpStatus.BAD_REQUEST);
    	}
    	
    
    	try{
	    	if(id != 0){//VT: if id exist, we want to update else insert
	    		RsSubcollection rsc = this.subCollectionEntityService.search(subcollectionId);	    		
	    		RsSample rs = this.sampleEntityService.search(id);
	    		
	    		Point p = (Point)(SpatialUtilities.wktToGeometry(locationWkt));
	    		
	    		rs.update(rsc, igsn,  csiroSampleId,
	    				 sampleType,  bhid,  depth,  datum,
	    				 zone,  containerId,  externalRef,
	    				 sampleCollector,NullUtilities.parseDateAllowNull(dateSampled) , sampleDispose,
	    				 NullUtilities.parseDateAllowNull(dateDisposed),  staffidDisposed, p);
	    		
	    		this.sampleEntityService.merge(rs);
	    		
	    		return  new ResponseEntity<Object>(rs,HttpStatus.OK);  
	    		
	    		
	    	}else{
	    		
	    		RsSubcollection rsc = subCollectionEntityService.search(subcollectionId);
	    		
	    		Point p = (Point)(SpatialUtilities.wktToGeometry(locationWkt));
	    		
	    		RsSample rs= new RsSample(rsc, igsn,  csiroSampleId,
	    				 sampleType,  bhid,  depth,  datum,
	    				 zone,  containerId,  externalRef,
	    				 sampleCollector,NullUtilities.parseDateAllowNull(dateSampled) , sampleDispose,
	    				 NullUtilities.parseDateAllowNull(dateDisposed),  staffidDisposed, p);
	    		
	    		
		       	this.sampleEntityService.persist(rs);
		
		       	return  new ResponseEntity<Object>(rs,HttpStatus.OK);    		
	    	}    
    	}catch(Exception e){
    		logger.warn(e);
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating",e.getMessage()),HttpStatus.BAD_REQUEST);
    	}
    }
	
      
    
    @RequestMapping(value = "getSample.do")
    public ResponseEntity<List<RsSample>> getSample(                       
            HttpServletResponse response) {
    	try{
    		List<RsSample> lrs = this.sampleEntityService.getAllSamples();  		
    		return  new ResponseEntity<List<RsSample>>(lrs,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    @RequestMapping(value = "getSamplesbySubCollection.do")
    public ResponseEntity<List<RsSample>> getSamplesbySubCollection(    
    		@RequestParam(required = true, value ="subCollectionId") String subCollectionId,
    		Principal user,
            HttpServletResponse response) {
    	try{    		    	
    		List<RsSample> lrc = this.sampleEntityService.getSampleBySubCollections(subCollectionId);    		
    		return  new ResponseEntity<List<RsSample>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
}
