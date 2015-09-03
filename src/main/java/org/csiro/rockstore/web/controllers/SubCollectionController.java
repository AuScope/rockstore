package org.csiro.rockstore.web.controllers;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.entity.service.CollectionEntityService;
import org.csiro.rockstore.entity.service.SubCollectionEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
 


@Controller
public class SubCollectionController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	SubCollectionEntityService subCollectionEntityService;
	CollectionEntityService collectionEntityService;
	
	@Autowired
	public SubCollectionController(CollectionEntityService collectionEntityService, SubCollectionEntityService subCollectionEntityService){
		this.subCollectionEntityService=subCollectionEntityService;
		this.collectionEntityService =collectionEntityService;
	}


    @RequestMapping(value = "subCollectionAddUpdate.do")
    public ResponseEntity<Object> addUpdate(            
            @RequestParam(required = false, value ="subcollectionId") String subcollectionId,
            @RequestParam(required = false, value ="locationInStorage") String locationInStorage,
            @RequestParam(required = false, value ="oldId") String oldId,           
            @RequestParam(required = false, value ="storageType") String storageType,
            @RequestParam(required = false, value ="hazardous") boolean hazardous,
            @RequestParam(required = true, value ="collectionId") String collectionId,
            @RequestParam(required = false, value ="source") String source,
            @RequestParam(required = false, value ="totalPallet") Integer totalPallet,
            Principal user,
            HttpServletResponse response){
    	
    	if(user==null){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Not logged in"),HttpStatus.BAD_REQUEST);
    	}
    	try{
	    	if(subcollectionId != null && !subcollectionId.isEmpty()){
	    		RsSubcollection rsc = this.subCollectionEntityService.search(subcollectionId);
	    		RsCollection rc = collectionEntityService.searchByCollectionId(collectionId);
	    		rsc.update(rc, oldId,locationInStorage, storageType, hazardous,
	    					 source,  totalPallet);
	    		
	    		this.subCollectionEntityService.merge(rsc);
	    		
	    		return  new ResponseEntity<Object>(rsc,HttpStatus.OK);  
	    		
	    	}else{
	    		RsCollection rc = collectionEntityService.searchByCollectionId(collectionId);
	    		RsSubcollection rsc= new RsSubcollection(rc,  oldId,
	    				  locationInStorage,  storageType,  hazardous,
	    					 source,  totalPallet,null);
	       	
		       	this.subCollectionEntityService.persist(rsc);
		
		       	return  new ResponseEntity<Object>(rsc,HttpStatus.OK);    		
	    	}    
    	}catch(Exception e){
    		logger.warn(e);
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating",e.getMessage()),HttpStatus.BAD_REQUEST);
    	}
    }
	
      
    
    @RequestMapping(value = "getSubCollections.do")
    public ResponseEntity<List<RsSubcollection>> getSubCollections(  
    		@RequestParam(required = false, value ="subCollectionId") String subCollectionId,
    		Principal user,
            HttpServletResponse response) {
    	try{
    		List<RsSubcollection> lrc = null;
    		if(subCollectionId != null && !subCollectionId.isEmpty()){
    			ArrayList<RsSubcollection> result= (new ArrayList<RsSubcollection>());
    			result.add(this.subCollectionEntityService.search(subCollectionId));
    			lrc = result;
    		}else{
    			lrc= this.subCollectionEntityService.getSubCollections();
    		}    		    		
    		return  new ResponseEntity<List<RsSubcollection>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    @RequestMapping(value = "getSubCollectionsByCollection.do")
    public ResponseEntity<List<RsSubcollection>> getSubCollectionsByCollection(    
    		@RequestParam(required = true, value ="collectionId") String collectionId,
    		Principal user,
            HttpServletResponse response) {
    	try{    		    	
    		List<RsSubcollection> lrc = this.subCollectionEntityService.getSubCollectionsByCollection(collectionId);    		
    		return  new ResponseEntity<List<RsSubcollection>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);   
    		throw e;
    	}
    } 
    
    
    
    @RequestMapping(value = "searchSubCollections.do")
    public ResponseEntity<List<RsSubcollection>> searchCollections(    		
    		 @RequestParam(required = false, value ="collectionId") String collectionId,
    		 @RequestParam(required = false, value ="project") String project,
    		 @RequestParam(required = false, value ="oldId") String oldId,
             @RequestParam(required = false, value ="locationInStorage") String locationInStorage,                  
             @RequestParam(required = false, value ="storageType") String storageType,            
             @RequestParam(required = false, value ="source") String source,   
             @RequestParam(required = false, value ="pageNumber") Integer pageNumber, 
             @RequestParam(required = false, value ="pageSize") Integer pageSize, 
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		List<RsSubcollection> lrc = null;  
    		
    		lrc = this.subCollectionEntityService.searchSubCollections(collectionId,project,oldId,locationInStorage,storageType,source,pageNumber,pageSize);
    		
    		
    		return  new ResponseEntity<List<RsSubcollection>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    @RequestMapping(value = "searchSubCollectionsCount.do")
    public ResponseEntity<Long> searchCollectionsCount(    		
    		 @RequestParam(required = false, value ="collectionId") String collectionId,
    		 @RequestParam(required = false, value ="project") String project,
    		 @RequestParam(required = false, value ="oldId") String oldId,
             @RequestParam(required = false, value ="locationInStorage") String locationInStorage,                  
             @RequestParam(required = false, value ="storageType") String storageType,                     
             @RequestParam(required = false, value ="source") String source,   
             @RequestParam(required = false, value ="pageNumber") Integer pageNumber, 
             @RequestParam(required = false, value ="pageSize") Integer pageSize, 
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		
    		
    		Long count = this.subCollectionEntityService.searchSubCollectionsCount(collectionId,project,oldId,locationInStorage,storageType,source);
    		
    		
    		return  new ResponseEntity<Long>(count,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
}
