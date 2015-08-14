package org.csiro.rockstore.web.controllers;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
	
	CollectionEntityService collectionEntityService;
	
	@Autowired
	public CollectionController(CollectionEntityService collectionEntityService){
		this.collectionEntityService=collectionEntityService;
	}


    @RequestMapping(value = "addUpdate.do")
    public ResponseEntity<RsCollection> addUpdate(            
            @RequestParam(required = false, value ="collectionId") String collectionId,
            @RequestParam(required = false, value ="projectId") String projectId,
            @RequestParam(required = false, value ="staffFieldManager") String staffFieldManager,
            @RequestParam(required = false, value ="staffResponsible") String staffResponsible,
            @RequestParam(required = false, value ="projectResult") String projectResult,
            @RequestParam(required = false, value ="projectPublication") String projectPublication,
            @RequestParam(required = false, value ="projectCloseDate") String projectCloseDate,
            @RequestParam(required = false, value ="availableToPublic") String availableToPublic,
            @RequestParam(required = false, value ="archiveDue") String archiveDue,            
            HttpServletResponse response)throws Exception {
    	
    
    	
    	if(collectionId != null && !collectionId.isEmpty()){
    		RsCollection rc = this.collectionEntityService.search(collectionId);
    		rc.update(projectId, staffFieldManager,  staffResponsible,
       			 projectResult,  projectPublication, NullUtilities.parseDateAllowNull(projectCloseDate),
       			 Boolean.parseBoolean(availableToPublic),NullUtilities.parseDateAllowNull(archiveDue),null,null);
    		
    		this.collectionEntityService.merge(rc);
    		
    		return  new ResponseEntity<RsCollection>(rc,HttpStatus.OK);  
    		
    	}else{
    		RsCollection rc= new RsCollection(projectId,
       			 staffFieldManager,  staffResponsible,
       			 projectResult,  projectPublication,
       			NullUtilities.parseDateAllowNull(projectCloseDate),  Boolean.parseBoolean(availableToPublic), NullUtilities.parseDateAllowNull(archiveDue),
       			null,null);
       	
	       	this.collectionEntityService.persist(rc);
	
	       	return  new ResponseEntity<RsCollection>(rc,HttpStatus.OK);    		
    	}       
    }
	
  
    @RequestMapping(value = "getCollections.do")
    public ResponseEntity<List<RsCollection>> getCollections(                       
            HttpServletResponse response)throws Exception {
    		List<RsCollection> lrc = this.collectionEntityService.getCollections();    		
    		return  new ResponseEntity<List<RsCollection>>(lrc,HttpStatus.OK);  

    }    
}
