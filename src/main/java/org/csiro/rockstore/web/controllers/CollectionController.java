package org.csiro.rockstore.web.controllers;


import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.auth.AuthenticationException;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.csiro.rockstore.entity.service.CollectionEntityService;
import org.csiro.rockstore.utilities.NullUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class CollectionController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	CollectionEntityService collectionEntityService;
	
	
	@Autowired
	public CollectionController(CollectionEntityService collectionEntityService){
		this.collectionEntityService=collectionEntityService;
	}


    @RequestMapping(value = "collectionAddUpdate.do")
    public ResponseEntity<Object> collectionAddUpdate(            
            @RequestParam(required = false, value ="collectionId") String collectionId,
            @RequestParam(required = false, value ="projectId") String projectId,
            @RequestParam(required = false, value ="staffFieldManager") String staffFieldManager,
            @RequestParam(required = false, value ="staffResponsible") String staffResponsible,
            @RequestParam(required = false, value ="projectResult") String projectResult,
            @RequestParam(required = false, value ="projectPublication") String projectPublication,
            @RequestParam(required = false, value ="projectCloseDate") String projectCloseDate,
            @RequestParam(required = false, value ="availableToPublic") String availableToPublic,
            @RequestParam(required = false, value ="archiveDue") String archiveDue,
            Principal user,
            HttpServletResponse response) {
    	
    	if(user==null){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Not logged in"),HttpStatus.BAD_REQUEST);
    	}
    
    	try{
	    	if(collectionId != null && !collectionId.isEmpty()){
	    		RsCollection rc = this.collectionEntityService.search(collectionId);
	    		rc.update(projectId, staffFieldManager,  staffResponsible,
	       			 projectResult,  projectPublication, NullUtilities.parseDateAllowNull(projectCloseDate),
	       			 Boolean.parseBoolean(availableToPublic),NullUtilities.parseDateAllowNull(archiveDue));
	    		
	    		this.collectionEntityService.merge(rc);
	    		
	    		return  new ResponseEntity<Object>(rc,HttpStatus.OK);  
	    		
	    	}else{
	    		RsCollection rc= new RsCollection(projectId,
	       			 staffFieldManager,  staffResponsible,
	       			 projectResult,  projectPublication,
	       			NullUtilities.parseDateAllowNull(projectCloseDate),  Boolean.parseBoolean(availableToPublic), NullUtilities.parseDateAllowNull(archiveDue),
	       			null,null);
	       	
		       	this.collectionEntityService.persist(rc);
		
		       	return  new ResponseEntity<Object>(rc,HttpStatus.OK);    		
	    	}  
    	}catch(Exception e){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating",e.getMessage()),HttpStatus.BAD_REQUEST);
    	}
    }
	
  
    @RequestMapping(value = "getCollections.do")
    public ResponseEntity<List<RsCollection>> getCollections(       
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		List<RsCollection> lrc = this.collectionEntityService.getCollections();    		
    		return  new ResponseEntity<List<RsCollection>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    
}
