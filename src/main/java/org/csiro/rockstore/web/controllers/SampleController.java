package org.csiro.rockstore.web.controllers;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



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


//    @RequestMapping(value = "sampleAddUpdate.do")
//    public ResponseEntity<Object> sampleAddUpdate(            
//            @RequestParam(required = false, value ="subcollectionId") String subcollectionId,
//            @RequestParam(required = false, value ="locationInStorage") String locationInStorage,
//            @RequestParam(required = false, value ="oldId") String oldId,           
//            @RequestParam(required = false, value ="storageType") String storageType,
//            @RequestParam(required = false, value ="hazardous") boolean hazardous,
//            @RequestParam(required = true, value ="collectionId") String collectionId,
//            @RequestParam(required = false, value ="source") String source,
//            @RequestParam(required = false, value ="totalPallet") Integer totalPallet,
//            HttpServletResponse response){
//    	
//    
//    	try{
//	    	if(subcollectionId != null && !subcollectionId.isEmpty()){
//	    		RsSubcollection rsc = this.subCollectionEntityService.search(subcollectionId);
//	    		RsCollection rc = collectionEntityService.search(collectionId);
//	    		rsc.update(rc, oldId,locationInStorage, storageType, hazardous,
//	    					 source,  totalPallet);
//	    		
//	    		this.subCollectionEntityService.merge(rsc);
//	    		
//	    		return  new ResponseEntity<Object>(rsc,HttpStatus.OK);  
//	    		
//	    	}else{
//	    		RsCollection rc = collectionEntityService.search(collectionId);
//	    		RsSubcollection rsc= new RsSubcollection(rc,  oldId,
//	    				  locationInStorage,  storageType,  hazardous,
//	    					 source,  totalPallet,null);
//	       	
//		       	this.subCollectionEntityService.persist(rsc);
//		
//		       	return  new ResponseEntity<Object>(rsc,HttpStatus.OK);    		
//	    	}    
//    	}catch(Exception e){
//    		logger.warn(e);
//    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating",e.getMessage()),HttpStatus.BAD_REQUEST);
//    	}
//    }
	
      
    
    @RequestMapping(value = "getSample.do")
    public ResponseEntity<List<RsSample>> getSample(                       
            HttpServletResponse response) {
    	try{
    		List<RsSample> lrc = this.sampleEntityService.getAllSamples();  		
    		return  new ResponseEntity<List<RsSample>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
}
