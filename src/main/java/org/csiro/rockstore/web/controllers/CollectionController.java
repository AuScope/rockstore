package org.csiro.rockstore.web.controllers;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csiro.rockstore.entity.postgres.RsCollection;
import org.csiro.rockstore.entity.postgres.RsCollectionAudit;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.entity.service.CollectionEntityService;
import org.csiro.rockstore.security.LdapUser;
import org.csiro.rockstore.utilities.NullUtilities;
import org.csiro.rockstore.utilities.SecurityUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



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
    	
    	LdapUser authUser = SecurityUtilities.getLdapUser(user); 
    	if(authUser.getUserPermission()==null || authUser.getUserPermission().getCanEdit()==false){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Authentication Error","Insufficient Permission"),HttpStatus.BAD_REQUEST);
    	}
    
    	try{
	    	if(collectionId != null && !collectionId.isEmpty()){
	    		RsCollection rc = this.collectionEntityService.searchByCollectionId(collectionId);
	    		rc.update(projectId, staffFieldManager,  staffResponsible,
	       			 projectResult,  projectPublication, NullUtilities.parseDateAllowNull(projectCloseDate),
	       			 Boolean.parseBoolean(availableToPublic),NullUtilities.parseDateAllowNull(archiveDue),user.getName());
	    		
	    		this.collectionEntityService.merge(rc);
	    		
	    		return  new ResponseEntity<Object>(rc,HttpStatus.OK);  
	    		
	    	}else{
	    		RsCollection rc= new RsCollection(projectId,
	       			 staffFieldManager,  staffResponsible,
	       			 projectResult,  projectPublication,
	       			NullUtilities.parseDateAllowNull(projectCloseDate),  Boolean.parseBoolean(availableToPublic), NullUtilities.parseDateAllowNull(archiveDue),
	       			user.getName());
	       	
		       	this.collectionEntityService.persist(rc);
		
		       	return  new ResponseEntity<Object>(rc,HttpStatus.OK);    		
	    	}  
    	}catch(Exception e){
    		return new  ResponseEntity<Object>(new ExceptionWrapper("Error updating",e.getMessage()),HttpStatus.BAD_REQUEST);
    	}
    }
	
  
    @RequestMapping(value = "getCollections.do")
    public ResponseEntity<List<RsCollection>> getCollections(
    		@RequestParam(required = false, value ="collectionId") String collectionId,
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		List<RsCollection> lrc = null;  
    		if(collectionId != null && !collectionId.isEmpty()){
    			ArrayList<RsCollection> result= (new ArrayList<RsCollection>());
    			result.add(this.collectionEntityService.searchByCollectionId(collectionId));
    			lrc = result;
    			
    		}else{
    			lrc = this.collectionEntityService.getAllCollections();
    		}
    		
    		return  new ResponseEntity<List<RsCollection>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    @RequestMapping(value = "getCollectionAudit.do")
    public ResponseEntity<List<RsCollectionAudit>> getCollectionAudit(
    		@RequestParam(required = true, value ="collectionId") String collectionId,
    		Principal user,
            HttpServletResponse response) throws Exception{
    	if(user == null){
    		throw new AuthenticationException();
    	}
    	
    	try{    		    		
    		List<RsCollectionAudit>	lrc = this.collectionEntityService.getCollectionsAudit(collectionId);    		    		
    		return  new ResponseEntity<List<RsCollectionAudit>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    
    
    @RequestMapping(value = "searchCollections.do")
    public ResponseEntity<List<RsCollection>> searchCollections(    		
             @RequestParam(required = false, value ="project") String project,
             @RequestParam(required = false, value ="staffIdFieldManager") String staffIdFieldManager,
             @RequestParam(required = false, value ="staffidResponsible") String staffidResponsible,            
             @RequestParam(required = false, value ="projectPublication") String projectPublication,
             @RequestParam(required = false, value ="pageNumber") Integer pageNumber, 
             @RequestParam(required = false, value ="pageSize") Integer pageSize, 
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		List<RsCollection> lrc = null;  
    		
    		lrc = this.collectionEntityService.searchCollections(project,staffIdFieldManager,staffidResponsible,projectPublication,pageNumber,pageSize);
    		
    		
    		return  new ResponseEntity<List<RsCollection>>(lrc,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    @RequestMapping(value = "searchCollectionsCount.do")
    public ResponseEntity<Long> searchCollectionsCount(    		
             @RequestParam(required = false, value ="project") String project,
             @RequestParam(required = false, value ="staffIdFieldManager") String staffIdFieldManager,
             @RequestParam(required = false, value ="staffidResponsible") String staffidResponsible,            
             @RequestParam(required = false, value ="projectPublication") String projectPublication, 
             @RequestParam(required = false, value ="pageNumber") Integer pageNumber, 
             @RequestParam(required = false, value ="pageSize") Integer pageSize, 
    		Principal user,
            HttpServletResponse response) throws Exception{
    	try{    		
    		
    		
    		Long count = this.collectionEntityService.searchCollectionsCount(project,staffIdFieldManager,staffidResponsible,projectPublication);
    		
    		
    		return  new ResponseEntity<Long>(count,HttpStatus.OK);
    	}catch(Exception e){
    		logger.warn(e);
    		throw e;
    	}

    } 
    
    
}
